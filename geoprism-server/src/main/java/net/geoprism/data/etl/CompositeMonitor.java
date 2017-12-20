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
package net.geoprism.data.etl;

public class CompositeMonitor extends AbstractProgressMonitor
{
  private ProgressMonitorIF[] monitors;
  
  public CompositeMonitor(ProgressMonitorIF... monitors)
  {
    this.monitors = monitors;
  }

  @Override
  public void setFilename(String filename)
  {
    super.setFilename(filename);
    
    for (ProgressMonitorIF monitor : monitors)
    {
      monitor.setFilename(filename);
    }
  }

  @Override
  public void setState(DataImportState state)
  {
    super.setState(state);
    
    for (ProgressMonitorIF monitor : monitors)
    {
      monitor.setState(state);
    }
  }

  @Override
  public void setTotalProgressUnits(int totalProgressUnits)
  {
    super.setTotalProgressUnits(totalProgressUnits);
    
    for (ProgressMonitorIF monitor : monitors)
    {
      monitor.setTotalProgressUnits(totalProgressUnits);
    }
  }
  
  @Override
  public void setCurrentProgressUnit(int current)
  {
    super.setCurrentProgressUnit(current);
    
    for (ProgressMonitorIF monitor : monitors)
    {
      monitor.setCurrentProgressUnit(current);
    }
  }

  @Override
  public void entityImported(TargetDefinitionIF entity)
  {
    super.entityImported(entity);
    
    for (ProgressMonitorIF monitor : monitors)
    {
      monitor.entityImported(entity);
    }
  }

  @Override
  public void finished()
  {
    super.finished();
    
    for (ProgressMonitorIF monitor : monitors)
    {
      monitor.finished();
    }
  }

}
