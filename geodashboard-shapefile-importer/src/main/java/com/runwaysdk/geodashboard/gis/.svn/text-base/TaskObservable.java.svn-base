package dss.vector.solutions.gis;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.dataaccess.transaction.ITaskListener;

public abstract class TaskObservable
{
  /**
   * Collection of ITaskListeners which are interested in the progress of a task
   */
  protected List<ITaskListener> listeners;

  public TaskObservable()
  {
    this.listeners = new LinkedList<ITaskListener>();
  }

  public void addListener(ITaskListener listener)
  {
    this.listeners.add(listener);
  }

  public void removeListener(ITaskListener listener)
  {
    this.listeners.remove(listener);
  }

  /**
   * Fires a start event
   */
  protected void fireStart()
  {
    for (ITaskListener listener : listeners)
    {
      listener.start();
    }
  }

  /**
   * Fires a start task event
   * 
   * @param taskName
   *          Name of the task
   * @param amount
   *          Total amount of work which needs to be done by the task.
   */
  protected void fireStartTask(String taskName, int amount)
  {
    for (ITaskListener listener : listeners)
    {
      listener.taskStart(taskName, amount);
    }
  }

  /**
   * Fires a task progress event
   * 
   * @param amount
   *          Amount of progress
   */
  protected void fireTaskProgress(int amount)
  {
    for (ITaskListener listener : listeners)
    {
      listener.taskProgress(amount);
    }
  }

  /**
   * Fires a task done event
   * 
   * @param status
   *          Flag indicating if the task finished succesfully or not.
   */
  protected void fireTaskDone(boolean status)
  {
    for (ITaskListener listener : listeners)
    {
      listener.done(status);
    }
  }

}
