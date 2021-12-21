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
package net.geoprism.data.etl;

public interface ProgressMonitorIF
{
  public void setFilename(String filename);
  
  public String getFilename();

  public void setState(DataImportState state);
  
  public DataImportState getState();

  /**
   * Sets the total number of "progress units" that are requested to be imported. Should only be called once at the beginning of the import. In the case of an excel import, this is the total number of rows.
   */
  public void setTotalProgressUnits(int total);
  
  /**
   * @return The total number of records that are requested to be imported. Does not increment as the import progresses.
   */
  public int getTotalProgressUnits();
  
  /**
   * @return The current "progress unit" for the import. In the case of an excel spreadsheet, this is the current row number.
   */
  public int getCurrentProgressUnit();
  
  public void setCurrentProgressUnit(int unit);
  
  /**
   * @return The number of successfully imported records thus far in the import process.
   */
  public int getImportCount();

  /**
   * Invoked after the successful import of an entity. Invoking this will increment the import count.
   * 
   * @param entity The successfully imported entity.
   */
  public void entityImported(TargetDefinitionIF entity);

  public void finished();
}
