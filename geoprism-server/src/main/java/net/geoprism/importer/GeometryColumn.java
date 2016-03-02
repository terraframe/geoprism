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
package net.geoprism.importer;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.dataaccess.io.excel.ExcelColumn;
import com.runwaysdk.generation.loader.Reloadable;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTWriter;

public class GeometryColumn extends ExcelColumn implements Reloadable
{

  private WKTWriter writer;

  public GeometryColumn(String attributeName, String displayLabel, WKTWriter writer)
  {
    super(attributeName, displayLabel);

    this.writer = writer;
  }

  @Override
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
