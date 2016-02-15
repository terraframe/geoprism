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
package com.runwayskd.geodashboard.etl.excel;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.runwayskd.geodashboard.etl.ColumnType;

/**
 * This class handles the processing of a sheet#.xml sheet part of a XSSF .xlsx file, and generates row and cell events
 * for it.
 */
public class XSSFSheetXMLHandler extends DefaultHandler
{
  /**
   * Table with the styles used for formatting
   */
  private StylesTable                stylesTable;

  private ReadOnlySharedStringsTable sharedStringsTable;

  /**
   * Where our text is going
   */
  private final SheetHandler         output;

  // Set when V start element is seen
  private boolean                    vIsOpen;

  // Set when F start element is seen
  private boolean                    fIsOpen;

  // Set when an Inline String "is" is seen
  private boolean                    isIsOpen;

  // Set when a header/footer element is seen
  private boolean                    hfIsOpen;

  // Set when cell start element is seen;
  // used when cell close element is seen.
  private ColumnType                   nextDataType;

  // Used to format numeric cell values.
  private short                      formatIndex;

  private String                     formatString;

  private final DataFormatter        formatter;

  private String                     cellRef;

  private boolean                    formulasNotResults;

  // Gathers characters as they are seen.
  private StringBuffer               value        = new StringBuffer();

  private StringBuffer               formula      = new StringBuffer();

  private StringBuffer               headerFooter = new StringBuffer();

  /**
   * Accepts objects needed while parsing.
   *
   * @param styles
   *          Table of styles
   * @param strings
   *          Table of shared strings
   */
  public XSSFSheetXMLHandler(StylesTable styles, ReadOnlySharedStringsTable strings, SheetHandler sheetContentsHandler, DataFormatter dataFormatter, boolean formulasNotResults)
  {
    this.stylesTable = styles;
    this.sharedStringsTable = strings;
    this.output = sheetContentsHandler;
    this.formulasNotResults = formulasNotResults;
    this.nextDataType = ColumnType.NUMBER;
    this.formatter = dataFormatter;
  }

  /**
   * Accepts objects needed while parsing.
   *
   * @param styles
   *          Table of styles
   * @param strings
   *          Table of shared strings
   */
  public XSSFSheetXMLHandler(StylesTable styles, ReadOnlySharedStringsTable strings, SheetHandler sheetContentsHandler, boolean formulasNotResults)
  {
    this(styles, strings, sheetContentsHandler, new DataFormatter(), formulasNotResults);
  }

  private boolean isTextTag(String name)
  {
    if ("v".equals(name))
    {
      // Easy, normal v text tag
      return true;
    }
    if ("inlineStr".equals(name))
    {
      // Easy inline string
      return true;
    }
    if ("t".equals(name) && isIsOpen)
    {
      // Inline string <is><t>...</t></is> pair
      return true;
    }
    // It isn't a text tag
    return false;
  }

  public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException
  {

    if (isTextTag(name))
    {
      vIsOpen = true;
      // Clear contents cache
      value.setLength(0);
    }
    else if ("is".equals(name))
    {
      // Inline string outer tag
      isIsOpen = true;
    }
    else if ("f".equals(name))
    {
      // Clear contents cache
      formula.setLength(0);

      // Mark us as being a formula if not already
      if (nextDataType == ColumnType.NUMBER)
      {
        nextDataType = ColumnType.FORMULA;
      }

      // Decide where to get the formula string from
      String type = attributes.getValue("t");
      if (type != null && type.equals("shared"))
      {
        // Is it the one that defines the shared, or uses it?
        String ref = attributes.getValue("ref");
        String si = attributes.getValue("si");

        if (ref != null)
        {
          // This one defines it
          // TODO Save it somewhere
          fIsOpen = true;
        }
        else
        {
          // This one uses a shared formula
          // TODO Retrieve the shared formula and tweak it to
          // match the current cell
          if (formulasNotResults)
          {
            System.err.println("Warning - shared formulas not yet supported!");
          }
          else
          {
            // It's a shared formula, so we can't get at the formula string yet
            // However, they don't care about the formula string, so that's ok!
          }
        }
      }
      else
      {
        fIsOpen = true;
      }
    }
    else if ("oddHeader".equals(name) || "evenHeader".equals(name) || "firstHeader".equals(name) || "firstFooter".equals(name) || "oddFooter".equals(name) || "evenFooter".equals(name))
    {
      hfIsOpen = true;
      // Clear contents cache
      headerFooter.setLength(0);
    }
    else if ("row".equals(name))
    {
      int rowNum = Integer.parseInt(attributes.getValue("r")) - 1;
      output.startRow(rowNum);
    }
    // c => cell
    else if ("c".equals(name))
    {
      // Set up defaults.
      this.nextDataType = ColumnType.NUMBER;
      this.formatIndex = -1;
      this.formatString = null;
      cellRef = attributes.getValue("r");
      String cellType = attributes.getValue("t");
      String cellStyleStr = attributes.getValue("s");
      if ("b".equals(cellType))
        nextDataType = ColumnType.BOOLEAN;
      else if ("e".equals(cellType))
        nextDataType = ColumnType.ERROR;
      else if ("inlineStr".equals(cellType))
        nextDataType = ColumnType.INLINE_STRING;
      else if ("s".equals(cellType))
        nextDataType = ColumnType.TEXT;
      else if ("str".equals(cellType))
        nextDataType = ColumnType.FORMULA;
      else if (cellStyleStr != null)
      {
        // Number, but almost certainly with a special style or format
        int styleIndex = Integer.parseInt(cellStyleStr);
        XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
        this.formatIndex = style.getDataFormat();
        this.formatString = style.getDataFormatString();
        if (this.formatString == null)
          this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
      }
    }
  }

  public void endElement(String uri, String localName, String name) throws SAXException
  {
    String thisStr = null;

    // v => contents of a cell
    if (isTextTag(name))
    {
      vIsOpen = false;

      // Process the value contents as required, now we have it all
      switch (nextDataType)
      {
        case BOOLEAN:
          char first = value.charAt(0);
          thisStr = first == '0' ? "FALSE" : "TRUE";
          break;

        case ERROR:
          thisStr = "ERROR:" + value.toString();
          break;

        case FORMULA:
          if (formulasNotResults)
          {
            thisStr = formula.toString();
          }
          else
          {
            String fv = value.toString();

            if (this.formatString != null)
            {
              try
              {
                // Try to use the value as a formattable number
                double d = Double.parseDouble(fv);
                thisStr = formatter.formatRawCellContents(d, this.formatIndex, this.formatString);
              }
              catch (NumberFormatException e)
              {
                // Formula is a String result not a Numeric one
                thisStr = fv;
              }
            }
            else
            {
              // No formating applied, just do raw value in all cases
              thisStr = fv;
            }
          }
          break;

        case INLINE_STRING:
          // TODO: Can these ever have formatting on them?
          XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
          thisStr = rtsi.toString();
          break;

        case TEXT:
          String sstIndex = value.toString();
          try
          {
            int idx = Integer.parseInt(sstIndex);
            XSSFRichTextString rtss = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx));
            thisStr = rtss.toString();
          }
          catch (NumberFormatException ex)
          {
            System.err.println("Failed to parse SST index '" + sstIndex + "': " + ex.toString());
          }
          break;

        case NUMBER:
          String n = value.toString();
          if (this.formatString != null)
            thisStr = formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex, this.formatString);
          else
            thisStr = n;
          break;

        default:
          thisStr = "(TODO: Unexpected type: " + nextDataType + ")";
          break;
      }

      if (DateUtil.isADateFormat(this.formatIndex, this.formatString))
      {
        output.cell(cellRef, thisStr, ColumnType.DATE);
      }
      else
      {
        output.cell(cellRef, thisStr, nextDataType);
      }
    }
    else if ("f".equals(name))
    {
      fIsOpen = false;
    }
    else if ("is".equals(name))
    {
      isIsOpen = false;
    }
    else if ("row".equals(name))
    {
      output.endRow();
    }
    else if ("oddHeader".equals(name) || "evenHeader".equals(name) || "firstHeader".equals(name))
    {
      hfIsOpen = false;
      output.headerFooter(headerFooter.toString(), true, name);
    }
    else if ("oddFooter".equals(name) || "evenFooter".equals(name) || "firstFooter".equals(name))
    {
      hfIsOpen = false;
      output.headerFooter(headerFooter.toString(), false, name);
    }
  }

  /**
   * Captures characters only if a suitable element is open. Originally was just "v"; extended for inlineStr also.
   */
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    if (vIsOpen)
    {
      value.append(ch, start, length);
    }
    if (fIsOpen)
    {
      formula.append(ch, start, length);
    }
    if (hfIsOpen)
    {
      headerFooter.append(ch, start, length);
    }
  }
}
