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
package net.geoprism.gis;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.dataaccess.transaction.ITaskListener;

public class MockTaskListener implements ITaskListener
{
  private boolean    isStarted;

  private boolean    isDone;

  private boolean    status;

  private List<Task> tasks;

  public MockTaskListener()
  {
    this.tasks = new LinkedList<Task>();
    this.isDone = false;
    this.isStarted = false;
    this.status = false;
  }

  @Override
  public void done(boolean status)
  {
    this.isDone = true;
    this.status = status;
  }

  @Override
  public void start()
  {
    this.isStarted = true;
  }

  @Override
  public void taskProgress(int amount)
  {
    this.tasks.get(0).addWork(amount);
  }

  @Override
  public void taskStart(String name, int amount)
  {
    this.tasks.add(0, new Task(name, amount));

    System.out.println("Starting: " + name);
  }

  public List<Task> getTasks()
  {
    return tasks;
  }

  public boolean isDone()
  {
    return isDone;
  }

  public boolean isStarted()
  {
    return isStarted;
  }

  public boolean isStatus()
  {
    return status;
  }
}
