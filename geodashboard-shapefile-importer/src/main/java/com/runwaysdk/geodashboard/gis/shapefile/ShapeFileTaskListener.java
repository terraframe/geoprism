package com.runwaysdk.geodashboard.gis.shapefile;

import org.eclipse.core.runtime.IProgressMonitor;

import com.runwaysdk.dataaccess.transaction.ITaskListener;
import com.runwaysdk.geodashboard.gis.Localizer;


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
