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
package net.geoprism.data.importer;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.dataaccess.io.excel.ExcelColumn;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTWriter;

public class GeometryColumn extends ExcelColumn 
{

  private WKTWriter writer;

  public GeometryColumn(String attributeName, String displayLabel, WKTWriter writer)
  {
    super(attributeName, displayLabel);

    this.writer = writer;
  }

  public String getValue(ComponentIF component)
  {
    Object value = component.getObjectValue(this.attributeName);
    
    if(this.getTransform() != null)
    {
      value = this.getTransform().transform(value);
    }

    if (value != null)
    {
      String wkt = this.writer.writeFormatted((Geometry) value);

      return wkt;
    }

    return null;
  }
}
