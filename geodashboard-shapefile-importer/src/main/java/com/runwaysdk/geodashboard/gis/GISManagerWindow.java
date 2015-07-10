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
package com.runwaysdk.geodashboard.gis;

import java.io.InvalidClassException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import net.sf.ehcache.CacheException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;

import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdown;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.locatedIn.BuildLocatedInAction;
import com.runwaysdk.geodashboard.gis.shapefile.ImportShapefileAction;
import com.runwaysdk.logging.LogLevel;
import com.runwaysdk.logging.RunwayLogUtil;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.geo.UniversalQuery;

public class GISManagerWindow extends ApplicationWindow implements Reloadable
{
  private final class ShutdownRunnable implements IRunnableWithProgress, Reloadable
  {
    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
    {
      monitor.beginTask(Localizer.getMessage("MANAGER_SHUTDOWN"), IProgressMonitor.UNKNOWN);

      CacheShutdown.shutdown();

      monitor.done();
    }
  }

  public static String LOG_DIR = "logs";

  public static String LOG_EXT = ".log";

  public GISManagerWindow()
  {
    super(null);
  }

  @Override
  protected Control createContents(Composite parent)
  {
    Display display = parent.getDisplay();
    Monitor monitor = display.getPrimaryMonitor();

    parent.getShell().setSize(300, 100);
    parent.getShell().setText(Localizer.getMessage("GISI"));
    parent.getShell().setImage(ImageDescriptor.createFromURL(Object.class.getResource("/icons/globe.png")).createImage());

    Rectangle windowRect = parent.getShell().getBounds();
    Rectangle monitorRect = monitor.getBounds();

    int x = ( monitorRect.width - windowRect.width ) / 2;
    int y = ( monitorRect.height - windowRect.height ) / 2;

    parent.getShell().setLocation(x, y);

    Splash splash = this.createSplash(monitor);
    splash.open();

    try
    {
      Universal[] universals = this.getUniversals();

      Composite container = new Composite(parent, SWT.NONE);
      container.setLayout(new FillLayout());

      new ActionContributionItem(new ImportShapefileAction(universals)).fill(container);
      new ActionContributionItem(new BuildLocatedInAction()).fill(container);
    }
    catch (Throwable e)
    {
      /*
       * Close the splash before showing the error
       */
      splash.close();

      this.error(e);

      try
      {
        CacheShutdown.shutdown();
      }
      catch (Exception e2)
      {
      }

      System.exit(-1);
    }
    finally
    {
      if (!splash.isDisposed())
      {
        splash.close();
      }
    }

    // Bring this shell to the front
    parent.getShell().forceFocus();

    return parent;
  }

  public void error(Throwable throwable)
  {
    if (throwable instanceof InvocationTargetException)
    {
      throwable = throwable.getCause();
    }

    if (throwable instanceof CacheException)
    {
      if (throwable.getCause() instanceof InvalidClassException)
      {
        throwable = new RuntimeException(Localizer.getMessage("CACHE_MISMATCH"), throwable);
      }
    }

    if (throwable instanceof NoSuchMethodError)
    {
      throwable = new RuntimeException(Localizer.getMessage("RUNWAY_VERSION_MISMATCH"), throwable);
    }

    RunwayLogUtil.logToLevel(LogLevel.ERROR, "Error in GIS Manager", throwable);

    MessageDialog.openError(this.getShell(), Localizer.getMessage("ERROR_TITLE"), throwable.getLocalizedMessage());
  }

  @Request
  private Universal[] getUniversals()
  {
    UniversalQuery query = new UniversalQuery(new QueryFactory());
    query.ORDER_BY_ASC(query.getDisplayLabel().localize());

    OIterator<? extends Universal> iterator = query.getIterator();

    try
    {
      List<? extends Universal> list = iterator.getAll();

      return list.toArray(new Universal[list.size()]);
    }
    finally
    {
      iterator.close();
    }
  }

  private Splash createSplash(Monitor monitor)
  {
    return new Splash(monitor);
  }

  public void run()
  {
    // Don't return from open() until window closes
    this.setBlockOnOpen(true);

    this.addMenuBar();

    // Open the main window
    this.open();

    // Dispose the display
    Display.getCurrent().dispose();
  }

  @Override
  public boolean close()
  {
    ProgressMonitorDialog dialog = new ProgressMonitorDialog(this.getShell());

    try
    {
      IRunnableWithProgress runnable = new ShutdownRunnable();
      dialog.run(true, false, runnable);
    }
    catch (Throwable e)
    {
      this.error(e);

      return false;
    }

    return super.close();
  }
}
