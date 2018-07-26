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

import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.MdAttributeDecInfo;
import com.runwaysdk.dataaccess.MdAttributeDecDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.VaultFile;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.system.metadata.MdWebAttribute;
import com.runwaysdk.system.metadata.MdWebForm;
import com.runwaysdk.system.scheduler.JobHistory;

import net.geoprism.MappableClass;
import net.geoprism.data.etl.ImportValidator.DecimalAttribute;
import net.geoprism.data.etl.excel.ExcelDataFormatter;
import net.geoprism.data.etl.excel.ExcelImportHistory;
import net.geoprism.data.etl.excel.ExcelImportHistoryBase;
import net.geoprism.data.etl.excel.ExcelSheetReader;
import net.geoprism.data.etl.excel.JobHistoryProgressMonitor;
import net.geoprism.data.etl.excel.SourceContentHandler;

public class DataImportRunnable implements Reloadable
{
  static class CopyRunnable implements Runnable, UncaughtExceptionHandler
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

  public ImportResponseIF run(String bindingId, String sheetName) throws FileNotFoundException, Exception, IOException, JSONException
  {
    this.monitor = new JobHistoryProgressMonitor(history);

    SourceContextIF sContext = this.getSourceContext(bindingId, sheetName);
    TargetContextIF tContext = this.getTargetContext(bindingId);

    /*
     * Before importing the data we must validate that the location text
     * information
     */
    this.validateAndConfigure(sContext, tContext);

    /*
     * Import the data
     */
    // monitor.setState(DataImportState.DATAIMPORT);

    SuccessResponse summary = this.importData(file, sContext, tContext);

    /*
     * Return a JSONArray of the datasets which were created as part of the
     * import. Do not include datasets which have already been created.
     */
    JSONArray datasets = new JSONArray();

    List<TargetDefinitionIF> definitions = tContext.getDefinitions();

    for (TargetDefinitionIF definition : definitions)
    {
      String type = definition.getTargetType();

      MdWebForm mdForm = MdWebForm.getByKey(type);
      MdClass mdClass = mdForm.getFormMdClass();
      MappableClass mClass = MappableClass.getMappableClass((MdClassDAO)MdClassDAO.get(mdClass.getId()));

      datasets.put(mClass.toJSON());

      /*
       * Update classifiers value
       */
      List<TargetFieldIF> fields = definition.getFields();

      for (TargetFieldIF field : fields)
      {
        if (field instanceof TargetFieldClassifier)
        {
          String key = field.getKey();

          MdWebAttribute mdWebAttribute = MdWebAttribute.getByKey(key);

          TargetFieldClassifierBindingQuery query = new TargetFieldClassifierBindingQuery(new QueryFactory());
          query.WHERE(query.getTargetAttribute().EQ(mdWebAttribute));

          OIterator<? extends TargetFieldClassifierBinding> it = query.getIterator();

          try
          {
            while (it.hasNext())
            {
              TargetFieldClassifierBinding binding = it.next();
              binding.lock();
              binding.setIsValidate(true);
              binding.apply();
            }
          }
          finally
          {
            it.close();
          }
        }
      }

    }

    // monitor.setState(DataImportState.COMPLETE);

    summary.setDatasets(datasets);

    
    
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

    JSONObject object = new JSONObject(this.configuration);

    String vaultId = object.getString("vaultId");
    VaultFile vf = VaultFile.get(vaultId);
    vf.delete();

    return summary;
  }

  private TargetContext getTargetContext(String bindingId)
  {
    TargetContext tContext = new TargetContext();

    ExcelSourceBinding sBinding = ExcelSourceBinding.get(bindingId);
    TargetBinding tBinding = sBinding.getTargetBinding();

    tContext.addDefinition(tBinding.getDefinition());
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

  private SuccessResponse importData(File file, SourceContextIF sContext, TargetContextIF tContext) throws FileNotFoundException, IOException, Exception
  {
    Converter converter = new Converter(tContext, this.monitor);

    FileInputStream istream = new FileInputStream(file);

    try
    {
      SourceContentHandler handler = new SourceContentHandler(converter, sContext, this.monitor, DataImportState.DATAIMPORT);
      ExcelDataFormatter formatter = new ExcelDataFormatter();

      ExcelSheetReader reader = new ExcelSheetReader(handler, formatter);
      reader.process(istream);

      SuccessResponse summary = new SuccessResponse(sContext, tContext);
      summary.setTotal(this.monitor.getImportCount());
      summary.setFailures(handler.getNumberOfErrors());

      Workbook errors = converter.getErrors();

      if (errors != null)
      {
        try (PipedInputStream pis = new PipedInputStream())
        {
          CopyRunnable runnable = new CopyRunnable(pis, errors);
          Thread t = new Thread(runnable);
          t.setUncaughtExceptionHandler(runnable);
          t.setDaemon(true);
          t.start();

          VaultFile vf2 = VaultFile.createAndApply(this.filename, pis);

          t.join();

          summary.setFileId(vf2.getId());

          if (runnable.getThrowable() != null)
          {
            throw new ProgrammingErrorException(runnable.getThrowable());
          }
        }
      }

      if (converter.getProblems().size() > 0)
      {
        summary.setProblems(converter.getProblems());
        // ProblemResponse response = new
        // ProblemResponse(converter.getProblems(), sContext, tContext,
        // current);
        // summary.put("problems", response.getProblemsJSON());
      }

      return summary;
    }
    finally
    {
      istream.close();
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
