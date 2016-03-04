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

import net.geoprism.data.etl.ExcelFieldBinding;
import net.geoprism.data.etl.ExcelSourceBinding;

import com.runwaysdk.system.metadata.MdAttribute;

public class SourceField implements SourceFieldIF
{
  private String     columnName;

  private String     attributeName;

  private String     label;

  private ColumnType type;

  public String getColumnName()
  {
    return columnName;
  }

  public void setColumnName(String columnName)
  {
    this.columnName = columnName;
  }

  public String getAttributeName()
  {
    return attributeName;
  }

  public void setAttributeName(String attributeName)
  {
    this.attributeName = attributeName;
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

  @Override
  public ColumnType getType()
  {
    return this.type;
  }

  public void setType(ColumnType type)
  {
    this.type = type;
  }

  @Override
  public void persist(ExcelSourceBinding source)
  {
    String type = source.getMdView().definesType();
    MdAttribute mdAttribute = MdAttribute.getByKey(type + "." + this.attributeName);

    ExcelFieldBinding field = new ExcelFieldBinding();
    field.setColumnHeader(this.columnName);
    field.setColumnLabel(this.label);
    field.setColumnType(this.type.name());
    field.setMdAttribute(mdAttribute);
    field.setSourceDefinition(source);
    field.apply();
  }
}
