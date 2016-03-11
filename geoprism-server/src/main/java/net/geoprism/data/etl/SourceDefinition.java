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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.system.metadata.MdView;

public class SourceDefinition implements SourceDefinitionIF
{
  private String                         sheetName;

  private String                         label;

  private String                         type;

  private Map<String, SourceFieldIF>     fieldMap;

  private HashMap<String, SourceFieldIF> labelMap;

  private boolean                        isNew;

  private String                         id;

  private String                         country;

  public SourceDefinition()
  {
    this.fieldMap = new HashMap<String, SourceFieldIF>();
    this.labelMap = new HashMap<String, SourceFieldIF>();
    this.isNew = true;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  @Override
  public String getId()
  {
    return id;
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
  public String getCountry()
  {
    return this.country;
  }

  public void setCountry(String country)
  {
    this.country = country;
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

  public void setNew(boolean isNew)
  {
    this.isNew = isNew;
  }

  @Override
  public boolean isNew()
  {
    return this.isNew;
  }

  @Override
  public String getLabel()
  {
    return this.label;
  }

  public void setLabel(String label)
  {
    this.label = label;
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
  public List<SourceFieldIF> getFields()
  {
    return new LinkedList<SourceFieldIF>(this.fieldMap.values());
  }

  @Override
  public void persist()
  {
    if (this.isNew())
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
}
