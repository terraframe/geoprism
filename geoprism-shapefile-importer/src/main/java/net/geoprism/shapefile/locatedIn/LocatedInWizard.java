/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.shapefile.locatedIn;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import net.geoprism.data.importer.FileImportLogger;
import net.geoprism.data.importer.GISImportLoggerIF;
import net.geoprism.data.importer.LocatedInBean;
import net.geoprism.data.importer.LocatedInManager;
import net.geoprism.shapefile.GISManagerWindow;
import net.geoprism.shapefile.Localizer;
import net.geoprism.shapefile.TaskListener;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;


import com.runwaysdk.logging.LogLevel;
import com.runwaysdk.logging.RunwayLogUtil;

public class LocatedInWizard extends Wizard 
{

  class LocatedInRunner implements IRunnableWithProgress
  {
    private GISImportLoggerIF logger;

    public LocatedInRunner(GISImportLoggerIF logger)
    {
      this.logger = logger;
    }

    public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
    {
      setImporting(true);

      LocatedInManager manager = new LocatedInManager(bean);
      manager.addListener(new TaskListener(monitor));
      manager.run(logger);
    }
  }

  public static String  LOCATED_IN_LOG_DIR = "located_in";

  private LocatedInBean bean;

  private boolean       importing;

  public LocatedInWizard()
  {
    setWindowTitle(Localizer.getMessage("LOCATED_IN_WIZARD"));
    setNeedsProgressMonitor(true);

    this.bean = new LocatedInBean();
    this.importing = false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.wizard.IWizard#addPages()
   */
  public void addPages()
  {
    this.addPage(new LocatedInPage(bean));
    this.addPage(new PathFilterPage(bean));
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
      File file = new File(GISManagerWindow.LOG_DIR + File.separator + LOCATED_IN_LOG_DIR + File.separator + System.currentTimeMillis() + GISManagerWindow.LOG_EXT);

      file.getParentFile().mkdirs();

      final FileImportLogger logger = new FileImportLogger(file);

      try
      {
        // puts the data into a database ...
        getContainer().run(true, false, new LocatedInRunner(logger));

        if (logger.hasLogged())
        {
          String message = Localizer.getMessage("ERROR_IN_LOCATED_IN") + " " + file.getAbsolutePath();

          MessageDialog.openConfirm(getShell(), Localizer.getMessage("MESSAGE"), message);
        }
      }
      finally
      {
        logger.close();
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
    RunwayLogUtil.logToLevel(LogLevel.ERROR, "Uncaught exception in the located in builder", throwable);

    if (throwable == null || throwable instanceof NullPointerException)
    {
      String message = Localizer.getMessage("UNKNOWN_LOCATED_IN_EXCEPTION");

      error(message);
      this.setImporting(false);
    }
    else if (throwable instanceof InvocationTargetException)
    {
      this.handleException(throwable.getCause());
    }
    else
    {
      this.getLastPage().setErrorMessage(throwable.getLocalizedMessage());
      this.setImporting(false);
    }
  }

  private void error(String message)
  {
    WizardPage lastPage = this.getLastPage();

    lastPage.setErrorMessage(message);
  }

  public WizardPage getLastPage()
  {
    return (WizardPage) this.getPage(LocatedInPage.PAGE_NAME);
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

  public LocatedInBean getData()
  {
    return bean;
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
