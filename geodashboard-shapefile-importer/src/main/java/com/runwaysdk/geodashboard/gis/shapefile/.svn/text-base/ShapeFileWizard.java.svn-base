package dss.vector.solutions.gis.shapefile;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;

import com.runwaysdk.generation.loader.Reloadable;

import dss.vector.solutions.MdssLog;
import dss.vector.solutions.geo.GeoHierarchyView;
import dss.vector.solutions.gis.GISImportLogger;
import dss.vector.solutions.gis.GISImportLoggerIF;
import dss.vector.solutions.gis.GISManagerWindow;
import dss.vector.solutions.gis.Localizer;
import dss.vector.solutions.gis.TaskListener;

public class ShapeFileWizard extends Wizard implements Reloadable
{
  class ShapefileImportRunner implements IRunnableWithProgress, Reloadable
  {
    private ShapefileImporter importer;

    private GISImportLoggerIF logger;

    public ShapefileImportRunner(ShapefileImporter importer, GISImportLoggerIF logger)
    {
      this.importer = importer;
      this.logger = logger;
    }

    public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
    {
      setImporting(true);

      importer.addListener(new TaskListener(monitor));
      importer.setUniversal(data.getUniversal());
      importer.setType(data.getType());
      importer.setType(data.getType());
      importer.setName(data.getName());
      importer.setId(data.getId());
      importer.setParent(data.getParent());
      importer.setParentType(data.getParentType());
      importer.run(logger);
    }
  }

  public static String       SHAPEFILE_LOG_DIR = "shapefile";

  private ShapeFileBean      data;

  private GeoHierarchyView[] views;

  private boolean            importing;

  public ShapeFileWizard(String appName, GeoHierarchyView[] views)
  {
    this.views = views;
    this.data = new ShapeFileBean();
    this.importing = false;

    setWindowTitle(appName + " " + Localizer.getMessage("SHAPE_FILE_WIZARD"));
    setNeedsProgressMonitor(true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.wizard.IWizard#addPages()
   */
  public void addPages()
  {
    addPage(new ShapeFilePage(data));
    addPage(new UniversalPage(data, views));
    addPage(new ShapeFileAttributePage(data));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.wizard.IWizard#performFinish()
   */
  public boolean performFinish()
  {
    try
    {
      File file = new File(GISManagerWindow.LOG_DIR + File.separator + SHAPEFILE_LOG_DIR + File.separator + System.currentTimeMillis() + GISManagerWindow.LOG_EXT);

      file.getParentFile().mkdirs();

      GISImportLogger logger = new GISImportLogger(file);

      ShapefileImporter importer = new ShapefileImporter(data.getShapeFile());

      // Run the importer
      getContainer().run(true, false, new ShapefileImportRunner(importer, logger));

      if (logger.hasLogged())
      {
        String message = Localizer.getMessage("ERROR_IN_IMPORT") + " " + file.getAbsolutePath();

        MessageDialog.openConfirm(getShell(), Localizer.getMessage("MESSAGE"), message);
      }
    }
    catch (Exception e)
    {
      this.handleException(e);

      return false;
    }

    return true;
  }

  private void handleException(Throwable throwable)
  {
    MdssLog.error("Uncaught exception in the shapefile importer", throwable);

    if (throwable == null || throwable instanceof NullPointerException)
    {
      String message = Localizer.getMessage("UNKNOWN_SHAPE_FILE_EXCEPTION");

      this.error(message);
      this.setImporting(false);
    }
    else if (throwable instanceof InvocationTargetException)
    {
      this.handleException(throwable.getCause());
    }
    else
    {
      this.error(throwable.getLocalizedMessage());
      this.setImporting(false);
    }
  }

  private void error(String message)
  {
    this.getLastPage().setErrorMessage(message);
  }

  public WizardPage getLastPage()
  {
    return (WizardPage) this.getPage(ShapeFileAttributePage.PAGE_NAME);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.wizard.IWizard#performCancel()
   */
  public boolean performCancel()
  {
    if (!this.isImporting())
    {
      return MessageDialog.openConfirm(getShell(), Localizer.getMessage("CONFIRMATION"), Localizer.getMessage("CANCEL_MESSAGE"));
    }

    return false;
  }

  public ShapeFileBean getData()
  {
    return data;
  }

  private synchronized void setImporting(boolean importing)
  {
    this.importing = importing;
  }

  private synchronized boolean isImporting()
  {
    return importing;
  }
}