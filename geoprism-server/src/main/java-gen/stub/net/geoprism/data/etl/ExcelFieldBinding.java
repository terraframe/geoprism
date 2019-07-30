/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import com.runwaysdk.system.metadata.MdAttribute;

public class ExcelFieldBinding extends ExcelFieldBindingBase 
{
  private static final long serialVersionUID = 1684656210;

  public ExcelFieldBinding()
  {
    super();
  }

  public SourceFieldIF getSourceField()
  {
    MdAttribute mdAttribute = this.getMdAttribute();

    SourceField field = new SourceField();
    field.setAttributeName(mdAttribute.getAttributeName());
    field.setColumnName(this.getColumnHeader());
    field.setLabel(mdAttribute.getDisplayLabel().getValue());
    field.setType(ColumnType.valueOf(this.getColumnType()));

    return field;
  }

}
