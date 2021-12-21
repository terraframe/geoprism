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
package net.geoprism.data.etl.excel;

import java.io.InputStream;
import java.util.List;

import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.ooxml.POIXMLProperties.CustomProperties;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class ExcelSheetReader
{
  /**
   * Handler used to parse the sheet
   */
  private SheetHandler  handler;

  /**
   * Data formatter used when parsing cell values
   */
  private DataFormatter formatter;

  public ExcelSheetReader(SheetHandler handler)
  {
    this(handler, new DataFormatter());
  }

  public ExcelSheetReader(SheetHandler handler, DataFormatter formatter)
  {
    this.handler = handler;
    this.formatter = formatter;
  }

  public void process(InputStream stream) throws Exception
  {
    try
    {
      OPCPackage pkg = OPCPackage.open(stream);

      POIXMLProperties props = new POIXMLProperties(pkg);

      String dataset = this.getProperty(props.getCustomProperties(), "dataset");

      this.handler.setDatasetProperty(dataset);

      ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);
      XSSFReader xssfReader = new XSSFReader(pkg);
      StylesTable styles = xssfReader.getStylesTable();
      XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();

      while (iter.hasNext())
      {
        InputStream sheet = iter.next();

        try
        {
          String sheetName = iter.getSheetName();

          this.handler.startSheet(sheetName);

          InputSource sheetSource = new InputSource(sheet);
          ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, this.handler, this.formatter, false);

          XMLReader reader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
          reader.setContentHandler(handler);
          reader.parse(sheetSource);

          this.handler.endSheet();
        }
        finally
        {
          sheet.close();
        }
      }
    }
    finally
    {
      stream.close();
    }
  }

  public String getProperty(CustomProperties props, String name)
  {
    List<CTProperty> customProperties = props.getUnderlyingProperties().getPropertyList();

    for (CTProperty prop : customProperties)
    {
      if (prop.getName().equals(name))
      {
        return prop.getLpwstr();
      }
    }

    return null;
  }
}