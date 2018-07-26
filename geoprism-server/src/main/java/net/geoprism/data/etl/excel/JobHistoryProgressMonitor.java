/*******************************************************************************
 * Copyright (C) 2018 IVCC
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.geoprism.data.etl.excel;

import com.runwaysdk.system.scheduler.JobHistory;

import net.geoprism.data.etl.AbstractProgressMonitor;
import net.geoprism.data.etl.ProgressMonitorIF;
import net.geoprism.data.etl.TargetDefinitionIF;


public class JobHistoryProgressMonitor extends AbstractProgressMonitor implements ProgressMonitorIF
{
  private int importCount = 0;
  
  private JobHistory history;
  
  public JobHistoryProgressMonitor(JobHistory history)
  {
    this.history = history;
  }
  
  @Override
  public void entityImported(TargetDefinitionIF entity)
  {
    importCount++;
    
    history.appLock();
    if (history instanceof ExcelImportHistory)
    {
      ((ExcelImportHistoryBase) history).setImportCount(importCount);
    }
    history.apply();
  }  
  
  @Override
  public int getImportCount()
  {
    return this.importCount;
  }

  @Override
  public void setTotalProgressUnits(int total)
  {
    history.appLock();
    if (history instanceof ExcelImportHistory)
    {
      ((ExcelImportHistoryBase) history).setTotalRecords(total);
    }
    history.apply();
  }
  
  public JobHistory getHistory()
  {
    return history;
  }

}
