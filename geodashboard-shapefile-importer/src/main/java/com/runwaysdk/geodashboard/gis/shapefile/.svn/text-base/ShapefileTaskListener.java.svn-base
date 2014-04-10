package dss.vector.solutions.gis.shapefile;

import org.eclipse.core.runtime.IProgressMonitor;

import com.runwaysdk.dataaccess.transaction.ITaskListener;

import dss.vector.solutions.gis.Localizer;

public class ShapefileTaskListener implements ITaskListener
{
  protected IProgressMonitor monitor;

  public ShapefileTaskListener(IProgressMonitor monitor)
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
