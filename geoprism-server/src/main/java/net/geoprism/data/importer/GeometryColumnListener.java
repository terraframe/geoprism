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
package net.geoprism.data.importer;

import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.runwaysdk.business.Mutable;
import com.runwaysdk.dataaccess.io.ExcelExportListener;
import com.runwaysdk.dataaccess.io.excel.ExcelAdapter;
import com.runwaysdk.dataaccess.io.excel.ExcelColumn;
import com.runwaysdk.dataaccess.io.excel.ExcelUtil;
import com.runwaysdk.dataaccess.io.excel.ImportListener;

import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;
import com.runwaysdk.session.Session;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

public class GeometryColumnListener extends ExcelAdapter implements ExcelExportListener, ImportListener
{
  private MdAttributeGeometryDAOIF mdAttributeGeometry;

  private WKTReader                reader;

  private WKTWriter                writer;

  public GeometryColumnListener(MdAttributeGeometryDAOIF _mdAttributeGeometry)
  {
    this.mdAttributeGeometry = _mdAttributeGeometry;

    this.reader = new WKTReader();
    this.writer = new WKTWriter();
  }

  @Override
  public void addColumns(List<ExcelColumn> extraColumns)
  {
    Locale currentLocale = Session.getCurrentLocale();

    String attributeName = this.mdAttributeGeometry.definesAttribute();
    String attributeLabel = this.mdAttributeGeometry.getDisplayLabel(currentLocale);

    extraColumns.add(new GeometryColumn(attributeName, attributeLabel, this.writer));
  }

  @Override
  public void handleExtraColumns(Mutable instance, List<ExcelColumn> extraColumns, Row row) throws Exception
  {
    ExcelColumn column = this.getColumn(extraColumns);

    if (column != null)
    {
      String attributeName = this.mdAttributeGeometry.definesAttribute();
      Cell cell = row.getCell(column.getIndex());
      String value = ExcelUtil.getString(cell);

      if (value != null && value.length() > 0)
      {
        Geometry geometry = reader.read(value);

        instance.setValue(attributeName, geometry);
      }
    }
  }

  private ExcelColumn getColumn(List<ExcelColumn> columns)
  {
    String attributeName = this.mdAttributeGeometry.definesAttribute();

    for (ExcelColumn column : columns)
    {
      if (attributeName.equals(column.getAttributeName()))
      {
        return column;
      }
    }

    return null;
  }
}
