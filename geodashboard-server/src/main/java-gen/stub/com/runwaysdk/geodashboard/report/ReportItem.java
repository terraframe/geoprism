package com.runwaysdk.geodashboard.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.axis.encoding.Base64;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveReader;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.DocxRenderOption;
import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IExcelRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.json.JSONArray;
import org.json.JSONException;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.VaultFileInfo;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.io.FileReadException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.geodashboard.Dashboard;
import com.runwaysdk.geodashboard.oda.driver.session.IClientSession;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.CreatePermissionException;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.session.SessionIF;
import com.runwaysdk.system.VaultFile;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.util.FileIO;
import com.runwaysdk.vault.VaultFileDAO;
import com.runwaysdk.vault.VaultFileDAOIF;

public class ReportItem extends ReportItemBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long   serialVersionUID      = -935561311;

  private static final String TEMP_REPORT_PREFIX    = "birt-temp-doc-archive";

  private static final String PAGE_NUMBER           = "pageNumber";

  private static final String FORMAT                = "format";

  public static final String  RPTDESIGN_EXTENSION   = "rptdesign";

  public static final String  RPTDOCUMENT_EXTENSION = "rptdoc";

  public static final String  CATEGORY              = "category";

  public static final String  CRITERIA              = "criteria";

  public static final String  BASE_URL              = "dss.vector.solutions.report.ReportController.generate.mojo?report=";

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

    super.delete();
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

        /*
         * This invalidates all cached report documents so we need to delete
         */
        new CacheDocumentManager().run();
      }
    }

    this.apply();

    try
    {
      this.run(new HashMap<String, String>());
    }
    catch (Exception e)
    {
      throw new InvalidReportDefinitionException(e);
    }
  }

  @Override
  public void apply()
  {
    Dashboard dashboard = this.getDashboard();

    if (dashboard != null)
    {
      this.getReportLabel().setValue(dashboard.getDisplayLabel().getValue());
    }

    super.apply();
  }

  private void checkVaultPermissions(VaultFile entity, Operation operation)
  {
    SessionIF session = Session.getCurrentSession();

    if (session != null)
    {
      String sessionId = session.getId();
      boolean access = SessionFacade.checkAccess(sessionId, operation, entity);

      if (!access)
      {
        UserDAOIF user = SessionFacade.getUser(sessionId);
        String errorMsg = "User [" + user.getSingleActorName() + "] does not have permission to upload a new design file ";
        throw new CreatePermissionException(errorMsg, entity, user);
      }
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
  public Long render(OutputStream outputStream, ReportParameter[] parameters, String baseURL, String reportURL)
  {
    /*
     * Ensure the user has permissions to view the report
     */
    this.validatePermissions();

    try
    {
      Map<String, String> parameterMap = this.createParameterMap(parameters);

      return this.runAndRender(outputStream, parameterMap, baseURL, reportURL);
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
    catch (IOException e)
    {
      // TODO change exception type
      throw new RuntimeException("Unable to generate the report document", e);
    }
  }

  private Long renderFromDocument(OutputStream outputStream, Map<String, String> parameterMap, String baseURL, String reportURL, IDocArchiveReader reader) throws BirtException, EngineException
  {
    IReportEngine engine = BirtEngine.getBirtEngine(LocalProperties.getLogDirectory());

    HashMap<String, Object> contextMap = new HashMap<String, Object>();
    contextMap.put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, this.getClass().getClassLoader());

    IReportDocument document = engine.openReportDocument(this.getReportName(), reader, new HashMap<Object, Object>());

    String format = this.getFormat(parameterMap);

    try
    {
      IRenderTask task = engine.createRenderTask(document);
      try
      {
        task.setAppContext(contextMap);
        task.setRenderOption(this.getRenderOptions(outputStream, document, baseURL, reportURL, format));

        if (task.getRenderOption() instanceof HTMLRenderOption)
        {
          // long pageNumber = this.getPageNumber(parameterMap);
          //
          // if (pageNumber > 0)
          // {
          // task.setPageNumber(pageNumber);
          // }
        }

        IReportRunnable design = engine.openReportDesign(document.getDesignStream());

        Map<String, Object> convertedParameters = new ReportParameterUtil().convertParameters(design, parameterMap);

        // set and validate the parameters
        task.setParameterValues(convertedParameters);
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

    return document.getPageCount();
  }

  private Map<String, String> createParameterMap(ReportParameter[] parameters)
  {
    Map<String, String> map = new HashMap<String, String>();

    for (ReportParameter parameter : parameters)
    {
      map.put(parameter.getParameterName(), parameter.getParameterValue());
    }

    /*
     * Set the default category if one is not provided. The default category is the geo Id of the country of the
     * dashboard.
     */
    String geoId = map.get(CATEGORY);

    if (geoId == null || geoId.length() == 0)
    {
      Dashboard dashboard = this.getDashboard();
      GeoEntity country = dashboard.getCountry();

      map.put(CATEGORY, country.getGeoId());
    }

    GeoEntity geoEntity = GeoEntity.getByKey(map.get(CATEGORY));

    map.put("categoryLabel", geoEntity.getDisplayLabel().getValue());

    if (map.containsKey(CRITERIA))
    {
      String criteria = map.get(CRITERIA);

      // Get the user friendly description of the criteria
      if (criteria != null && criteria.length() > 0)
      {
        String information = ReportProviderUtil.getConditionInformation(criteria);

        map.put("criteriaInfo", information);
      }
    }

    return map;
  }

  private String getFormat(Map<String, String> parameters)
  {
    if (parameters.containsKey(FORMAT))
    {
      return parameters.get(FORMAT);
    }

    return IRenderOption.OUTPUT_FORMAT_HTML;
  }

  @Transaction
  public void generateAndSaveDocument(ReportParameter[] parameters)
  {
    try
    {
      // Run the report and get the path of the temp rptdocument file
      File file = this.run(this.createParameterMap(parameters));

      try
      {
        // If a vault file doesn't exist for the rptdocument then create one
        if (this.getDocument() == null || this.getDocument().length() == 0)
        {
          this.lock();

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
          VaultFile vaultFile = VaultFile.lock(this.getDocument());

          try
          {
            VaultFileDAO document = (VaultFileDAO) BusinessFacade.getEntityDAO(vaultFile);

            document.putFile(new FileInputStream(file));
          }
          finally
          {
            vaultFile.unlock();
          }
        }
      }
      catch (FileNotFoundException e)
      {
        throw new FileReadException(file, e);
      }
      finally
      {
        if (file.getName().startsWith(TEMP_REPORT_PREFIX))
        {
          FileIO.deleteFile(file);
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
    catch (IOException e)
    {
      // TODO change exception type
      throw new RuntimeException("Unable to get a report document", e);
    }
  }

  private File run(Map<String, String> parameterMap) throws BirtException, EngineException, IOException
  {
    File file = this.getCachedDocument(parameterMap);

    if (!file.exists() || file.getName().startsWith(TEMP_REPORT_PREFIX) || ( !parameterMap.containsKey(PAGE_NUMBER) && !this.getCacheDocument() ))
    {
      IReportEngine engine = BirtEngine.getBirtEngine(LocalProperties.getLogDirectory());

      HashMap<String, Object> contextMap = new HashMap<String, Object>();
      contextMap.put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, this.getClass().getClassLoader());
      contextMap.put(IClientSession.SESSION_ID, Session.getCurrentSession().getId());

      IReportRunnable design = engine.openReportDesign(this.getDesignAsStream());

      IRunTask task = engine.createRunTask(design);

      try
      {
        Map<String, Object> convertedParameters = new ReportParameterUtil().convertParameters(design, parameterMap);

        task.setAppContext(contextMap);
        task.setParameterValues(convertedParameters);
        task.validateParameters();

        task.run(new FileArchiveWriter(file.getAbsolutePath()));
      }
      finally
      {
        task.close();
      }
    }

    return file;
  }

  public File getCachedDocument(Map<String, String> parameters)
  {
    SessionIF session = Session.getCurrentSession();

    if (session != null)
    {
      StringBuffer key = new StringBuffer();
      key.append(this.getReportName());

      Iterator<Entry<String, String>> iterator = parameters.entrySet().iterator();

      while (iterator.hasNext())
      {
        Entry<String, String> entry = iterator.next();

        if (this.isValidParameter(entry))
        {
          key.append(entry.getKey() + "-" + entry.getValue());
        }
      }

      int hashCode = key.toString().hashCode();

      String filepath = BirtConstants.CACHE_DIR + File.separator + this.getCacheFolderName();
      String filename = hashCode + ".rptdocument";

      return new File(filepath + File.separator + filename);
    }

    try
    {
      return File.createTempFile(TEMP_REPORT_PREFIX, "tempReportDocument");
    }
    catch (IOException e)
    {
      // Change exception
      throw new RuntimeException(e);
    }
  }

  private boolean isValidParameter(Entry<String, String> parameter)
  {
    if (parameter.getKey().equals(PAGE_NUMBER))
    {
      return false;
    }

    return true;
  }

  private Long runAndRender(OutputStream outputStream, Map<String, String> parameterMap, String baseURL, String reportURL) throws BirtException, EngineException, IOException
  {
    File document = null;

    if (this.getCacheDocument() && this.getDocument() != null && this.getDocument().length() > 0)
    {
      document = this.getDocumentAsFile();
    }

    if (document == null || !document.exists() || ( !parameterMap.containsKey(PAGE_NUMBER) && !this.getCacheDocument() ))
    {
      document = this.run(parameterMap);
    }

    try
    {
      IDocArchiveReader reader = new ArchiveReader(document.getAbsolutePath());

      return this.renderFromDocument(outputStream, parameterMap, baseURL, reportURL, reader);
    }
    finally
    {
      if (document != null && document.getName().startsWith(TEMP_REPORT_PREFIX))
      {
        FileIO.deleteFile(document);
      }
    }
  }

  private IRenderOption getRenderOptions(OutputStream outputStream, IReportDocument document, String baseURL, String reportURL, String format)
  {
    if (format.equals("xlsx"))
    {
      EXCELRenderOption options = new EXCELRenderOption();
      options.setOutputStream(outputStream);
      options.setOutputFormat("xlsx");
      options.setOption(IExcelRenderOption.OFFICE_VERSION, "office2007");

      return options;
    }
    else if (format.equals("docx"))
    {
      DocxRenderOption option = new DocxRenderOption();
      option.setOutputStream(outputStream);
      option.setOption(DocxRenderOption.OPTION_EMBED_HTML, Boolean.FALSE);
      option.setOption(IRenderOption.EMITTER_ID, "org.eclipse.birt.report.engine.emitter.docx");
      return option;
    }
    else if (format.equalsIgnoreCase(OutputFormat.PDF.name()))
    {
      // set output options
      PDFRenderOption options = new PDFRenderOption();
      options.setOutputFormat(RenderOption.OUTPUT_FORMAT_PDF);
      options.setOutputStream(outputStream);
      options.setBaseURL(baseURL);
      options.setActionHandler(new PDFUrlActionHandler(document, baseURL, reportURL));

      return options;
    }
    else if (format.equalsIgnoreCase(OutputFormat.HTML.name()))
    {
      String folderName = this.getCacheFolderName();

      // set output options
      HTMLRenderOption options = new HTMLRenderOption();
      options.setOutputFormat(RenderOption.OUTPUT_FORMAT_HTML);
      options.setOutputStream(outputStream);
      options.setBaseURL(baseURL);
      options.setImageHandler(new HTMLServerImageHandler());
      options.setBaseImageURL(baseURL + "/" + BirtConstants.BIRT_SUFFIX + "/" + folderName);
      options.setImageDirectory(BirtConstants.IMGS_DIR + File.separator + folderName);
      options.setActionHandler(new HTMLUrlActionHandler(document, baseURL, reportURL));
      options.setHtmlTitle(this.getReportLabel().getValue());
      options.setEmbeddable(true);
      options.setHtmlPagination(true);

      return options;
    }

    UnsupportedOutputFormatException e = new UnsupportedOutputFormatException("Unknown output format type");
    e.setOutputFormat(format);
    e.apply();

    throw e;
  }

  private String getCacheFolderName()
  {
    SessionIF session = Session.getCurrentSession();
    String sessionId = session.getId();

    return Base64.encode(sessionId.getBytes());
  }

  public void validatePermissions()
  {

  }

  public static ReportItem find(String reportName)
  {
    ReportItemQuery query = new ReportItemQuery(new QueryFactory());
    query.WHERE(query.getReportName().EQ(reportName));

    OIterator<? extends ReportItem> it = query.getIterator();

    try
    {
      if (it.hasNext())
      {
        return it.next();
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

  // @Override
  public String getParameterDefinitions()
  {
    InputStream stream = this.getDesignAsStream();

    try
    {
      ReportParameterUtil util = new ReportParameterUtil();
      JSONArray definition = util.getParameterDefinitions(stream);

      return definition.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (BirtException e)
    {
      throw new ProgrammingErrorException(e);
    }
    finally
    {
      try
      {
        stream.close();
      }
      catch (IOException e)
      {
        // TODO change exception type
        throw new RuntimeException("Unable to get a report document", e);
      }
    }
  }

  public static ReportItem getReportItemForDashboard(String dashboardId)
  {
    ReportItemQuery query = new ReportItemQuery(new QueryFactory());
    query.WHERE(query.getDashboard().EQ(dashboardId));

    OIterator<? extends ReportItem> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        return iterator.next();
      }

      return null;
    }
    finally
    {
      iterator.close();
    }
  }

  public static ValueQuery getValuesForReporting(String queryId, String category, String criteria, String aggregation)
  {
    return ReportProviderBridge.getValuesForReporting(queryId, category, criteria, aggregation);
  }

  public static ValueQuery getMetadataForReporting(String queryId, String category, String criteria, String aggregation)
  {
    ValueQuery query = ReportProviderBridge.getValuesForReporting(queryId, category, criteria, aggregation);
    query.restrictRows(1, 1);

    return query;
  }

  public static PairView[] getQueriesForReporting()
  {
    return ReportProviderBridge.getQueriesForReporting();
  }

  public static PairView[] getSupportedAggregation(String queryId)
  {
    return ReportProviderBridge.getSupportedAggregation(queryId);
  }

  public static ReportItem lockOrCreateReport(String dashboardId)
  {
    ReportItemQuery query = new ReportItemQuery(new QueryFactory());
    query.WHERE(query.getDashboard().EQ(dashboardId));

    OIterator<? extends ReportItem> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        ReportItem item = iterator.next();
        item.lock();

        return item;
      }
      else
      {
        return new ReportItem();
      }
    }
    finally
    {
      iterator.close();
    }

  }

}
