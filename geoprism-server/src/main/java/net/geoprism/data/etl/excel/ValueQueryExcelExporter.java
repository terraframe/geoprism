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
package net.geoprism.data.etl.excel;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.geoprism.util.Predicate;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.runwaysdk.SystemException;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.attributes.value.Attribute;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Session;

public class ValueQueryExcelExporter
{
  /**
   * Excel workbook being exported
   */
  private Workbook              workbook;

  /**
   * <code>ValueQuery</code> to export
   */
  private ValueQuery            valueQuery;

  /**
   * Predicate to determine which selectables are included in the export
   */
  private Predicate<Selectable> predicate;

  /**
   * Name of the sheet.
   */
  private String                sheetName;

  /**
   * Holder for the date style
   */
  private CellStyle             dateStyle;

  public ValueQueryExcelExporter(ValueQuery valueQuery, String sheetName, Predicate<Selectable> predicate)
  {
    this(valueQuery, sheetName, predicate, new HSSFWorkbook());
  }

  public ValueQueryExcelExporter(ValueQuery valueQuery, String sheetName, Predicate<Selectable> predicate, Workbook workbook)
  {
    this.predicate = predicate;
    this.workbook = workbook;

    this.dateStyle = this.workbook.createCellStyle();
    this.dateStyle.setDataFormat(workbook.createDataFormat().getFormat("dd/mm/yyyy"));

    this.valueQuery = valueQuery;

    this.sheetName = sheetName;
  }

  public void write(OutputStream ostream)
  {
    this.prepareSheet();

    try
    {
      workbook.write(ostream);
    }
    catch (IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public InputStream exportStream()
  {
    this.prepareSheet();

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    try
    {
      BufferedOutputStream buffer = new BufferedOutputStream(bytes);

      workbook.write(buffer);
      buffer.flush();
      buffer.close();

      return new ByteArrayInputStream(bytes.toByteArray());
    }
    catch (IOException e)
    {
      throw new SystemException(e);
    }
  }

  public Byte[] export()
  {
    this.prepareSheet();

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    try
    {
      BufferedOutputStream buffer = new BufferedOutputStream(bytes);

      workbook.write(buffer);
      buffer.flush();
      buffer.close();

      byte[] byteArray = bytes.toByteArray();
      Byte[] bigByteArray = new Byte[byteArray.length];

      for (int i = 0; i < byteArray.length; i++)
      {
        bigByteArray[i] = Byte.valueOf(byteArray[i]);
      }

      return bigByteArray;
    }
    catch (IOException e)
    {
      throw new SystemException(e);
    }
  }

  /**
   * Prepares a new sheet (which represents a type) in the workbook. Fills in all necessary information for the sheet.
   * 
   * @return
   */
  private Sheet prepareSheet()
  {
    OIterator<ValueObject> iterator = this.valueQuery.getIterator();

    Sheet sheet = workbook.createSheet(this.sheetName);
    Row labelRow = sheet.createRow(0);

    List<Selectable> selectableList = this.valueQuery.getSelectableRefs();

    int selectableCount = 0;
    for (Selectable selectable : selectableList)
    {
      if (predicate.evaulate(selectable))
      {
        MdAttributeConcreteDAOIF mdAttribute = selectable.getMdAttributeIF();
        labelRow.createCell(selectableCount).setCellValue(this.workbook.getCreationHelper().createRichTextString(mdAttribute.getDisplayLabel(Session.getCurrentLocale())));
        selectableCount++;
      }
    }

    int rowCount = 1;
    for (ValueObject valueObject : iterator)
    {
      Row valueRow = sheet.createRow(rowCount++);

      Map<String, Attribute> attributeMap = valueObject.getAttributeMap();

      selectableCount = 0;

      for (Selectable selectable : selectableList)
      {
        if (predicate.evaulate(selectable))
        {
          String attributeName = selectable.getResultAttributeName();

          Attribute attribute = attributeMap.get(attributeName);

          String value = attribute.getValue();

          if (attribute instanceof com.runwaysdk.dataaccess.attributes.value.AttributeBoolean)
          {
            com.runwaysdk.dataaccess.attributes.value.AttributeBoolean attributeBoolean = (com.runwaysdk.dataaccess.attributes.value.AttributeBoolean) attribute;

            // exports as 1 and 0 as per #2735
            String displayLabel;
            if (value == null || value.trim().length() == 0)
            {
              displayLabel = "";
            }
            else if (attributeBoolean.getBooleanValue())
            {
              displayLabel = MdAttributeBooleanDAOIF.DB_TRUE;
            }
            else
            {
              displayLabel = MdAttributeBooleanDAOIF.DB_FALSE;
            }

            valueRow.createCell(selectableCount).setCellValue(this.workbook.getCreationHelper().createRichTextString(displayLabel));
          }
          else if (attribute instanceof com.runwaysdk.dataaccess.attributes.value.AttributeNumber)
          {
            com.runwaysdk.dataaccess.attributes.value.AttributeNumber attributeNumber = (com.runwaysdk.dataaccess.attributes.value.AttributeNumber) attribute;

            String numberValue = attributeNumber.getValue();

            if (numberValue != null && !numberValue.equals(""))
            {
              // Precondition - assumes value is a valid couble.
              valueRow.createCell(selectableCount).setCellValue(Double.valueOf(numberValue));
            }
          }
          else if (attribute instanceof com.runwaysdk.dataaccess.attributes.value.AttributeDate)
          {
            com.runwaysdk.dataaccess.attributes.value.AttributeDate attributeDate = (com.runwaysdk.dataaccess.attributes.value.AttributeDate) attribute;

            String dateValue = attributeDate.getValue();

            if (dateValue != null && !dateValue.equals(""))
            {
              SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

              Date date = dateFormat.parse(dateValue, new java.text.ParsePosition(0));

              // Precondition - assumes value is a valid couble.
              Cell cell = valueRow.createCell(selectableCount);
              cell.setCellValue(date);
              cell.setCellStyle(dateStyle);
            }
          }
          else if (attribute instanceof com.runwaysdk.dataaccess.attributes.value.AttributeDateTime)
          {
            com.runwaysdk.dataaccess.attributes.value.AttributeDateTime attributeDate = (com.runwaysdk.dataaccess.attributes.value.AttributeDateTime) attribute;

            String dateValue = attributeDate.getValue();

            if (dateValue != null && !dateValue.equals(""))
            {
              SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATETIME_FORMAT);

              Date date = dateFormat.parse(dateValue, new java.text.ParsePosition(0));

              // Precondition - assumes value is a valid couble.
              valueRow.createCell(selectableCount).setCellValue(date);
            }
          }
          else if (attribute instanceof com.runwaysdk.dataaccess.attributes.value.AttributeTime)
          {
            com.runwaysdk.dataaccess.attributes.value.AttributeTime attributeDate = (com.runwaysdk.dataaccess.attributes.value.AttributeTime) attribute;

            String dateValue = attributeDate.getValue();

            if (dateValue != null && !dateValue.equals(""))
            {
              SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIME_FORMAT);

              Date date = dateFormat.parse(dateValue, new java.text.ParsePosition(0));

              // Precondition - assumes value is a valid couble.
              valueRow.createCell(selectableCount).setCellValue(date);
            }
          }
          else if (attribute instanceof com.runwaysdk.dataaccess.attributes.value.AttributeChar || attribute instanceof com.runwaysdk.dataaccess.attributes.value.AttributeReference)
          {
            valueRow.createCell(selectableCount).setCellValue(this.workbook.getCreationHelper().createRichTextString(value));
          }

          selectableCount++;
        }
      }
    }

    return sheet;
  }
}
