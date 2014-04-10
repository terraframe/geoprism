package dss.vector.solutions.gis;

import org.eclipse.core.runtime.IProgressMonitor;

import com.runwaysdk.dataaccess.transaction.ITaskListener;

public class TaskListener implements ITaskListener
{
  protected IProgressMonitor monitor;

  public TaskListener(IProgressMonitor monitor)
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
  public void taskProgress(int work)
  {
    getMonitor().worked(work);
  }

  @Override
  public void done(boolean success)
  {
    getMonitor().done();
  }
}
