package com.runwaysdk.geodashboard.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.VaultFileInfo;
import com.runwaysdk.dataaccess.io.FileReadException;
import com.runwaysdk.dataaccess.io.FileWriteException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.CreatePermissionException;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.system.VaultFile;
import com.runwaysdk.util.FileIO;
import com.runwaysdk.util.IDGenerator;
import com.runwaysdk.vault.VaultFileDAO;
import com.runwaysdk.vault.VaultFileDAOIF;

public class ReportItem extends ReportItemBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long  serialVersionUID      = -1301378633;

  public static final String RPTDESIGN_EXTENSION   = "rptdesign";

  public static final String RPTDOCUMENT_EXTENSION = "rptdoc";

  public static final String BASE_URL              = "dss.vector.solutions.report.ReportController.generate.mojo?report=";

  public ReportItem()
  {
    super();
  }

  @Override
  public String toString()
  {
    return this.getReportLabel().getValue();
  }

  @Override
  @Transaction
  public void delete()
  {
    /*
     * Delete the design file and if there is a document file delete that too
     */
    if (this.getDesign() != null && this.getDesign().length() > 0)
    {
      VaultFile file = VaultFile.lock(this.getDesign());
      file.delete();
    }

    if (this.getDocument() != null && this.getDocument().length() > 0)
    {
      VaultFile file = VaultFile.lock(this.getDocument());
      file.delete();
    }

    // /*
    // * Delete the job report
    // */
    // ReportJob job = ReportJob.get(this);
    //
    // if (job != null)
    // {
    // job.lock();
    // job.delete();
    // }

    super.delete();
  }

  @Override
  public void apply()
  {
    this.setOutputFormatIndex(this.getOutputFormat().get(0).getEnumName());

    super.apply();
  }

  @Override
  @Transaction
  public void applyWithFile(InputStream fileStream)
  {
    boolean isNew = this.isNew() && !this.isAppliedToDB();

    /*
     * Validate the report name
     */
    if (isNew)
    {
      /*
       * Create a new vault file
       */
      if (fileStream != null)
      {
        VaultFile entity = new VaultFile();
        VaultFileDAO file = (VaultFileDAO) BusinessFacade.getEntityDAO(entity);

        this.checkVaultPermissions(entity, Operation.CREATE);

        String reportName = this.getReportName();

        int index = reportName.lastIndexOf('.');

        String filename = reportName.substring(0, index);
        String extension = reportName.substring(index + 1);

        if (extension != null && extension.equals(RPTDESIGN_EXTENSION))
        {
          entity.setValue(VaultFileInfo.FILE_NAME, filename);
          entity.setValue(VaultFileInfo.EXTENSION, extension);

          file.setSize(0);
          entity.apply();
          file.putFile(fileStream);

          this.setDesign(entity.getId());
        }
        else
        {
          throw new ReportItemException("Report design must have the rptdesign extension");
        }
      }
      else
      {
        throw new ReportItemException("Report item must have a report document");
      }
    }
    else
    {
      /*
       * Update the existing vault file
       */
      if (fileStream != null)
      {
        VaultFile entity = VaultFile.lock(this.getDesign());
        VaultFileDAO file = (VaultFileDAO) BusinessFacade.getEntityDAO(entity);

        this.checkVaultPermissions(entity, Operation.WRITE);

        String reportName = this.getReportName();

        int index = reportName.lastIndexOf('.');

        String filename = reportName.substring(0, index);
        String extension = reportName.substring(index + 1);

        if (extension != null && extension.equals(RPTDESIGN_EXTENSION))
        {
          entity.setValue(VaultFileInfo.FILE_NAME, filename);
          entity.setValue(VaultFileInfo.EXTENSION, extension);

          file.setSize(0);
          entity.apply();
          file.putFile(fileStream);
        }
        else
        {
          throw new ReportItemException("Report design must have the rptdesign extension");
        }
      }
    }

    this.apply();

    // /*
    // * Handle the job report
    // */
    // ReportJob job = ReportJob.get(this);
    //
    // if (this.getCacheDocument())
    // {
    // if (job == null)
    // {
    // job = new ReportJob();
    // job.setReportItem(this);
    // }
    // else
    // {
    // job.lock();
    // }
    //
    // // job.getDisplayLabel().setValue(this.getReportLabel().getValue() + " ["
    // // + this.getOutputFormat().get(0).getDisplayLabel() + "]");
    // job.apply();
    // }
    // else
    // {
    // if (job != null)
    // {
    // job.lock();
    // job.delete();
    // }
    // }
  }

  private void checkVaultPermissions(VaultFile entity, Operation operation)
  {
    String sessionId = Session.getCurrentSession().getId();
    boolean access = SessionFacade.checkAccess(sessionId, operation, entity);

    if (!access)
    {
      UserDAOIF user = SessionFacade.getUser(sessionId);
      String errorMsg = "User [" + user.getSingleActorName() + "] does not have permission to upload a new design file ";
      throw new CreatePermissionException(errorMsg, entity, user);
    }
  }

  @Override
  public String getURL()
  {
    return BASE_URL + this.getId();
  }

  @Override
  public InputStream getDesignAsStream()
  {
    String design = this.getDesign();

    if (design == null || design.equals(""))
    {
      String msg = "A report template has not been defined for this report";

      MissingReportDesignException e = new MissingReportDesignException(msg);
      e.apply();

      throw e;
    }

    VaultFileDAOIF file = VaultFileDAO.get(design);

    return file.getFileStream();
  }

  @Override
  public InputStream getDocumentAsStream()
  {
    VaultFileDAOIF file = getDocumentAsVaultFile();

    return file.getFileStream();
  }

  private File getDocumentAsFile()
  {
    VaultFileDAOIF file = this.getDocumentAsVaultFile();

    return file.getFile();
  }

  private VaultFileDAOIF getDocumentAsVaultFile()
  {
    String document = this.getDocument();

    if (document == null || document.equals(""))
    {
      String msg = "A report document has not been defined for this report";

      MissingReportDocumentException e = new MissingReportDocumentException(msg);
      e.apply();

      throw e;
    }

    VaultFileDAOIF file = VaultFileDAO.get(document);
    return file;
  }

  @Override
  @Transaction
  @Authenticate
  public void render(OutputStream outputStream, ReportParameter[] parameters, String baseURL)
  {
    /*
     * Ensure the user has permissions to view the report
     */
    this.validatePermissions();

    try
    {
      if (!this.getCacheDocument())
      {
        this.runAndRender(outputStream, parameters, baseURL);
      }
      else
      {
        this.renderFromDocument(outputStream, parameters, baseURL);
      }
    }
    catch (EngineException e)
    {
      if (e.getCause() != null)
      {
        ReportRenderException exception = new ReportRenderException(e.getCause());
        exception.setErrorMessage(e.getCause().getLocalizedMessage());
        exception.apply();

        throw exception;
      }
      else
      {
        ReportRenderException exception = new ReportRenderException(e);
        exception.setErrorMessage(e.getLocalizedMessage());
        exception.apply();

        throw exception;
      }
    }
    catch (BirtException e)
    {
      ReportRenderException exception = new ReportRenderException(e);
      exception.setErrorMessage(e.getLocalizedMessage());
      exception.apply();

      throw exception;
    }
  }

  private void renderFromDocument(OutputStream outputStream, ReportParameter[] parameters, String baseURL) throws BirtException, EngineException
  {
    IReportEngine engine = BirtEngine.getBirtEngine(LocalProperties.getLogDirectory());

    HashMap<String, Object> contextMap = new HashMap<String, Object>();
    contextMap.put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, this.getClass().getClassLoader());

    if (this.getDocument() == null || this.getDocument().length() == 0)
    {
      throw new MissingReportDocumentException();
    }

    File file = this.getDocumentAsFile();
    IReportDocument document = engine.openReportDocument(file.getAbsolutePath());

    try
    {
      IRenderTask task = engine.createRenderTask(document);
      try
      {
        task.setAppContext(contextMap);
        task.setRenderOption(this.getRenderOptions(outputStream, baseURL));

        // Set parameters
        for (ReportParameter parameter : parameters)
        {
          task.setParameterValue(parameter.getParameterName(), parameter.getParameterValue());
        }

        task.validateParameters();

        // run report
        task.render();
      }
      finally
      {
        task.close();
      }
    }
    finally
    {
      document.close();
    }
  }

  @Transaction
  public void generateAndSaveDocument(ReportParameter[] parameters)
  {
    try
    {
      // Run the report and get the path of the temp rptdocument file
      String path = this.run(parameters);

      try
      {
        // Copy the temp rptdocument file over to the document vault
        File file = new File(path);

        try
        {
          // If a vault file doesn't exist for the rptdocument then create one
          if (this.getDocument() == null || this.getDocument().length() == 0)
          {
            this.appLock();

            VaultFile entity = new VaultFile();
            VaultFileDAO fileDao = (VaultFileDAO) BusinessFacade.getEntityDAO(entity);

            this.checkVaultPermissions(entity, Operation.CREATE);

            String reportName = this.getReportName();

            int index = reportName.lastIndexOf('.');

            String filename = reportName.substring(0, index);
            String extension = RPTDOCUMENT_EXTENSION;

            entity.setValue(VaultFileInfo.FILE_NAME, filename);
            entity.setValue(VaultFileInfo.EXTENSION, extension);

            fileDao.setSize(0);
            entity.apply();
            fileDao.putFile(new FileInputStream(file));

            this.setDocument(entity.getId());
            this.apply();
          }
          else
          {
            VaultFileDAOIF document = this.getDocumentAsVaultFile();
            document.putFile(new FileInputStream(file));
          }
        }
        catch (FileNotFoundException e)
        {
          throw new FileReadException(file, e);
        }
      }
      finally
      {
        File file = new File(path);

        try
        {
          FileIO.deleteFile(file);
        }
        catch (IOException e)
        {
          throw new FileWriteException(file, e);
        }
      }

    }
    catch (BirtException e)
    {
      ReportRenderException exception = new ReportRenderException(e);
      exception.setErrorMessage(e.getLocalizedMessage());
      exception.apply();

      throw exception;
    }
  }

  private String run(ReportParameter[] parameters) throws BirtException, EngineException
  {
    String path = DeployProperties.getJspDir() + File.separator + "temp" + File.separator + IDGenerator.nextID();
    IReportEngine engine = BirtEngine.getBirtEngine(LocalProperties.getLogDirectory());

    HashMap<String, Object> contextMap = new HashMap<String, Object>();
    contextMap.put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, this.getClass().getClassLoader());

    IReportRunnable design = engine.openReportDesign(this.getDesignAsStream());

    IRunTask task = engine.createRunTask(design);
    try
    {
      task.setAppContext(contextMap);

      // Set parameters
      for (ReportParameter parameter : parameters)
      {
        task.setParameterValue(parameter.getParameterName(), parameter.getParameterValue());
      }

      task.validateParameters();

      task.run(path);
    }
    finally
    {
      task.close();
    }

    return path;
  }

  private void runAndRender(OutputStream outputStream, ReportParameter[] parameters, String baseURL) throws BirtException, EngineException
  {
    IReportEngine engine = BirtEngine.getBirtEngine(LocalProperties.getLogDirectory());

    HashMap<String, Object> contextMap = new HashMap<String, Object>();
    contextMap.put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, this.getClass().getClassLoader());

    // create task to run and render report
    IReportRunnable design = engine.openReportDesign(this.getDesignAsStream());

    IRunAndRenderTask task = engine.createRunAndRenderTask(design);

    try
    {
      task.setAppContext(contextMap);
      task.setRenderOption(this.getRenderOptions(outputStream, baseURL));

      // Set parameters
      for (ReportParameter parameter : parameters)
      {
        task.setParameterValue(parameter.getParameterName(), parameter.getParameterValue());
      }

      task.validateParameters();

      // run report
      task.run();
    }
    finally
    {
      task.close();
    }
  }

  private IRenderOption getRenderOptions(OutputStream outputStream, String baseURL)
  {
    if (this.getOutputFormat().contains(OutputFormat.HTML))
    {
      // set output options
      HTMLRenderOption options = new HTMLRenderOption();
      options.setOutputFormat(this.getRenderOutputFormat());
      options.setOutputStream(outputStream);
      options.setBaseURL(baseURL);
      options.setImageHandler(new HTMLServerImageHandler());
      options.setBaseImageURL(baseURL + "/imgs");
      options.setImageDirectory(DeployProperties.getDeployPath() + "/imgs");
      options.setActionHandler(new HTMLUrlActionHandler(baseURL));
      options.setHtmlTitle(this.getReportLabel().getValue());

      return options;
    }
    else
    {
      // set output options
      PDFRenderOption options = new PDFRenderOption();
      options.setOutputFormat(this.getRenderOutputFormat());
      options.setOutputStream(outputStream);
      options.setBaseURL(baseURL);
      options.setActionHandler(new PDFUrlActionHandler(baseURL));

      return options;
    }
  }

  public String getRenderOutputFormat()
  {
    if (this.getOutputFormat().contains(OutputFormat.PDF))
    {
      return RenderOption.OUTPUT_FORMAT_PDF;
    }
    else if (this.getOutputFormat().contains(OutputFormat.HTML))
    {
      return RenderOption.OUTPUT_FORMAT_HTML;
    }

    UnsupportedOutputFormatException e = new UnsupportedOutputFormatException("Unknown output format type");
    e.apply();

    throw e;
  }

  public static OutputFormat getOutputFormat(String format)
  {
    if (format.equals(RenderOption.OUTPUT_FORMAT_PDF))
    {
      return OutputFormat.PDF;
    }
    else if (format.equals(RenderOption.OUTPUT_FORMAT_HTML))
    {
      return OutputFormat.HTML;
    }

    UnsupportedOutputFormatException e = new UnsupportedOutputFormatException("Unknown output format type");
    e.apply();

    throw e;
  }

  public static ReportItem find(String reportName, OutputFormat outputFormat)
  {
    ReportItemQuery query = new ReportItemQuery(new QueryFactory());
    query.WHERE(query.getReportName().EQ(reportName));
    query.AND(query.getOutputFormat().containsAny(outputFormat));

    OIterator<? extends ReportItem> it = query.getIterator();

    try
    {
      if (it.hasNext())
      {
        ReportItem item = it.next();

        if (it.hasNext())
        {
          String message = "Multiple report items have been found with the report [" + reportName + "] and output format [" + outputFormat.name() + "]";

          MultipleReportException e = new MultipleReportException(message);
          e.setReportName(reportName);
          e.setFormat(outputFormat.name());
          e.apply();

          throw e;
        }

        return item;
      }
      else
      {
        return null;
      }
    }
    finally
    {
      it.close();
    }

  }

}
