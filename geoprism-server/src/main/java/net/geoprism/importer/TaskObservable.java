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
package net.geoprism.importer;

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
