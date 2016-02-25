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
package com.runwayskd.geodashboard.etl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.runwaysdk.system.metadata.MdView;

public class SourceDefinition implements SourceDefinitionIF
{
  private String                         sheetName;

  private String                         type;

  private Map<String, SourceFieldIF>     fieldMap;

  private HashMap<String, SourceFieldIF> labelMap;

  public SourceDefinition()
  {
    this.fieldMap = new HashMap<String, SourceFieldIF>();
    this.labelMap = new HashMap<String, SourceFieldIF>();
  }

  @Override
  public String getType()
  {
    return this.type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  @Override
  public String getName()
  {
    return this.sheetName;
  }

  public void setSheetName(String sheetName)
  {
    this.sheetName = sheetName;
  }

  public void addField(SourceFieldIF field)
  {
    this.fieldMap.put(field.getColumnName(), field);
    this.labelMap.put(field.getLabel(), field);
  }

  @Override
  public SourceFieldIF getFieldByName(String columnName)
  {
    return this.fieldMap.get(columnName);
  }

  @Override
  public SourceFieldIF getFieldByLabel(String label)
  {
    return this.labelMap.get(label);
  }

  @Override
  public void persist()
  {
    ExcelSourceBinding source = new ExcelSourceBinding();
    source.setSheetName(this.sheetName);
    source.setMdView(MdView.getMdView(this.type));
    source.apply();
    
    Collection<SourceFieldIF> fields = this.fieldMap.values();

    for (SourceFieldIF field : fields)
    {
      field.persist(source);
    }
  }
}
