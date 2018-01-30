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

public class AbstractProgressMonitor implements ProgressMonitorIF
{
  private int progressUnit;
  
  private int importCount;
  
  private int totalProgressUnits;
  
  private DataImportState state;
  
  private String          filename;
  
  public AbstractProgressMonitor()
  {
    this.importCount = 0;
    this.totalProgressUnits = 0;
  }
  
  @Override
  public void setFilename(String filename)
  {
    this.filename = filename;
  }
  
  @Override
  public String getFilename()
  {
    return this.filename;
  }

  @Override
  public void setState(DataImportState state)
  {
    this.state = state;
  }
  
  @Override
  public DataImportState getState()
  {
    return this.state;
  }

  @Override
  public void setTotalProgressUnits(int totalProgressUnits)
  {
    this.totalProgressUnits = totalProgressUnits;
  }

  @Override
  public int getTotalProgressUnits()
  {
    return totalProgressUnits;
  }
  
  @Override
  public void setCurrentProgressUnit(int unit)
  {
    this.progressUnit = unit;
  }

  @Override
  public int getCurrentProgressUnit()
  {
    return this.progressUnit;
  }

  @Override
  public int getImportCount()
  {
    return this.importCount;
  }

  @Override
  public void entityImported(TargetDefinitionIF entity)
  {
    this.importCount++;
  }
  
  @Override
  public void finished()
  {
    
  }
}
