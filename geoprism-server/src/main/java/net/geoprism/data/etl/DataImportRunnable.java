package net.geoprism.data.etl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.geoprism.MappableClass;
import net.geoprism.PluginUtil;
import net.geoprism.data.etl.ImportValidator.DecimalAttribute;
import net.geoprism.data.etl.excel.ExcelDataFormatter;
import net.geoprism.data.etl.excel.ExcelImportHistory;
import net.geoprism.data.etl.excel.ExcelImportHistoryBase;
import net.geoprism.data.etl.excel.ExcelSheetReader;
import net.geoprism.data.etl.excel.JobHistoryProgressMonitor;
import net.geoprism.data.etl.excel.SourceContentHandler;
import net.geoprism.dhis2.DHIS2PluginIF;

import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.MdAttributeDecInfo;
import com.runwaysdk.dataaccess.MdAttributeDecDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.system.VaultFile;
import com.runwaysdk.system.scheduler.JobHistory;

public class DataImportRunnable implements Reloadable
{
  public static class CopyRunnable implements Runnable, UncaughtExceptionHandler
  {
    private PipedInputStream istream;

    private Workbook         errors;

    private Throwable        throwable;

    public CopyRunnable(PipedInputStream istream, Workbook errors)
    {
      this.istream = istream;
      this.errors = errors;
      this.throwable = null;
    }

    @Override
    public void run()
    {
      try (PipedOutputStream pos = new PipedOutputStream(this.istream))
      {
        errors.write(pos);
      }
      catch (IOException e)
      {
        this.throwable = e;
      }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e)
    {
      this.throwable = e;
    }

    public Throwable getThrowable()
    {
      return throwable;
    }
  }

  static class ValidationResult implements Reloadable
  {
    private ImportResponseIF              response;

    private Map<String, DecimalAttribute> attributes;

    public ValidationResult(ImportResponseIF response, Map<String, DecimalAttribute> attributes)
    {
      this.response = response;
      this.attributes = attributes;
    }

    public Map<String, DecimalAttribute> getAttributes()
    {
      return attributes;
    }

    public ImportResponseIF getResponse()
    {
      return response;
    }
  }

  private ProgressMonitorIF monitor;

  private String            configuration;

  private File              file;

  private String            filename;

  private JobHistory        history;

  public DataImportRunnable(String configuration, String filename, File file, JobHistory history)
  {
    this.configuration = configuration;
    this.filename = filename;
    this.file = file;
    this.history = history;
  }

  public ImportResponseIF run() throws FileNotFoundException, Exception, IOException, JSONException
  {
    this.monitor = new JobHistoryProgressMonitor(history);

    /*
     * Import the data
     */
    ImportResponseIF summary = this.importData(file, this.configuration);
    
    // Update the history
    history.appLock();

    JSONObject config = new JSONObject(this.configuration);

    if (summary.hasProblems())
    {
      int catProbs = 0;
      int locProbs = 0;

      Collection<ImportProblemIF> problems = summary.getProblems();

      for (ImportProblemIF problem : problems)
      {
        if (problem instanceof CategoryProblem)
        {
          catProbs++;
        }
        else if (problem instanceof LocationProblem)
        {
          locProbs++;
        }
      }

      if (history instanceof ExcelImportHistory)
      {
        ((ExcelImportHistoryBase) history).setNumberUnknownTerms(catProbs);
        ((ExcelImportHistoryBase) history).setNumberUnknownGeos(locProbs);
      }
    }

    if (summary.getFileId() != null)
    {
      ((ExcelImportHistoryBase) history).setErrorFileId(summary.getFileId());

      // Use the error file in the future
      config.put("vaultId", summary.getFileId());
    }

    JSONObject reconstructionJSON = new JSONObject();
    reconstructionJSON.put("importResponse", summary.toJSON());
    reconstructionJSON.put("configuration", config);

    // referred // to // in // angular // as // 'workbook' // or //
    // 'information'
    ((ExcelImportHistoryBase) history).setReconstructionJSON(reconstructionJSON.toString());
    history.apply();

    return summary;
  }

  private TargetContext getTargetContext(String bindingId)
  {
    TargetContext tContext = new TargetContext();

    TargetBuilder.restoreBindingFromDatabase(bindingId, tContext, new JSONObject(this.configuration));
    
    return tContext;
  }

  private SourceContext getSourceContext(String id, String sheetName)
  {
    SourceContext sContext = new SourceContext();
    ExcelSourceBinding sBinding = ExcelSourceBinding.get(id);
    sContext.addSheetDefinition(sBinding.getDefinition(sheetName));
    return sContext;
  }

  @Transaction
  private ImportResponseIF validateAndConfigure(SourceContextIF sContext, TargetContextIF tContext) throws FileNotFoundException, Exception, IOException
  {
    /*
     * Before importing the data we must validate that the location text
     * information
     */
    // monitor.setState(DataImportState.VALIDATION);
    ValidationResult result = this.validateData(file, sContext, tContext);

    // if (result.getResponse() != null)
    // {
    // monitor.setState(DataImportState.VALIDATIONFAIL);
    // return result.getResponse();
    // }

    /*
     * Update any scale or precision which is greater than its current
     * definition
     */
    this.updateScaleAndPrecision(result.getAttributes());

    return null;
  }

  private void updateScaleAndPrecision(Map<String, DecimalAttribute> attributes)
  {
    Set<Entry<String, DecimalAttribute>> entries = attributes.entrySet();

    for (Entry<String, DecimalAttribute> entry : entries)
    {
      String mdAttributeId = entry.getKey();
      DecimalAttribute attribute = entry.getValue();

      int scale = attribute.getScale();
      int precision = attribute.getPrecision();

      MdAttributeDecDAOIF mdAttributeIF = (MdAttributeDecDAOIF) MdAttributeDecDAO.get(mdAttributeId);

      int eLength = new Integer(mdAttributeIF.getLength()).intValue();
      int eScale = new Integer(mdAttributeIF.getDecimal()).intValue();
      int ePrecision = eLength - eScale;

      if (precision > ePrecision || scale > eScale)
      {
        int nLength = Math.max(Math.max(precision + scale, precision + eScale), Math.max(ePrecision + scale, ePrecision + eScale));
        int nScale = Math.max(scale, eScale);

        MdAttributeDecDAO mdAttribute = (MdAttributeDecDAO) mdAttributeIF.getBusinessDAO();
        mdAttribute.setValue(MdAttributeDecInfo.LENGTH, new Integer(nLength).toString());
        mdAttribute.setValue(MdAttributeDecInfo.DECIMAL, new Integer(nScale).toString());
        mdAttribute.apply();
      }
    }
  }

  private ImportResponseIF importData(File file, String configuration) throws FileNotFoundException, IOException, Exception
  {
    DHIS2PluginIF plugin = PluginUtil.getDhis2Plugin();
    
    if (plugin != null)
    {
      return plugin.importData(file, filename, monitor, configuration);
    }
    else
    {
      return null;
    }
  }

  private ValidationResult validateData(File file, SourceContextIF sContext, TargetContextIF tContext) throws FileNotFoundException, Exception, IOException
  {
    ImportValidator converter = new ImportValidator(tContext);

    FileInputStream istream = new FileInputStream(file);

    try
    {
      SourceContentHandler handler = new SourceContentHandler(converter, sContext, this.monitor, DataImportState.VALIDATION);
      ExcelDataFormatter formatter = new ExcelDataFormatter();

      ExcelSheetReader reader = new ExcelSheetReader(handler, formatter);
      reader.process(istream);
    }
    finally
    {
      istream.close();
    }

    ImportResponseIF response = null;

//    if (converter.getProblems().size() > 0)
//    {
//      response = new ProblemResponse(converter.getProblems(), sContext, tContext, current);
//    }

    return new ValidationResult(response, converter.getAttributes());
  }
}
