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
package net.geoprism.shapefile.ui;

import net.geoprism.shapefile.Localizer;

import org.eclipse.core.runtime.IProgressMonitor;

import com.runwaysdk.dataaccess.transaction.ITaskListener;


public class ShapeFileTaskListener implements ITaskListener
{
  protected IProgressMonitor monitor;

  public ShapeFileTaskListener(IProgressMonitor monitor)
  {
    this.monitor = monitor;
  }

  @Override
  public void start()
  {
    // Do nothing
  }

  public IProgressMonitor getMonitor()
  {
    return monitor;
  }

  @Override
  public void taskStart(String name, int amount)
  {
    getMonitor().beginTask(Localizer.getMessage(name), ( amount != -1 ) ? amount : IProgressMonitor.UNKNOWN);
  }

  @Override
  public void taskProgress(int worked)
  {
    getMonitor().worked(worked);
  }

  @Override
  public void done(boolean success)
  {
    getMonitor().done();
  }
}
