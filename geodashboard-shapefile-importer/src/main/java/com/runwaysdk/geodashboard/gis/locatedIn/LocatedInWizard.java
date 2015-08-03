/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.geodashboard.gis.locatedIn;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.GISImportLogger;
import com.runwaysdk.geodashboard.gis.GISManagerWindow;
import com.runwaysdk.geodashboard.gis.Localizer;
import com.runwaysdk.geodashboard.gis.TaskListener;
import com.runwaysdk.geodashboard.service.GISImportLoggerIF;
import com.runwaysdk.logging.LogLevel;
import com.runwaysdk.logging.RunwayLogUtil;

public class LocatedInWizard extends Wizard implements Reloadable
{

  class LocatedInRunner implements IRunnableWithProgress, Reloadable
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
    addPage(new LocatedInPage(bean));
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

      final GISImportLogger logger = new GISImportLogger(file);

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
