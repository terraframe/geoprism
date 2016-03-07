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

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;

public class ExcelDataFormatter extends DataFormatter
{
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  public static final String DATE_FORMAT      = "yyyy-MM-dd";

  public static final String TIME_FORMAT      = "HH:mm:ss";

  @Override
  public String formatRawCellContents(double value, int formatIndex, String formatString)
  {
    if (DateUtil.isADateFormat(formatIndex, formatString))
    {
      return super.formatRawCellContents(value, formatIndex, DATE_TIME_FORMAT);
    }
    else
    {
      return new Double(value).toString();
//      return super.formatRawCellContents(value, formatIndex, formatString);
    }
  }

  @Override
  public String formatRawCellContents(double value, int formatIndex, String formatString, boolean use1904Windowing)
  {
    if (DateUtil.isADateFormat(formatIndex, formatString))
    {
      return super.formatRawCellContents(value, formatIndex, DATE_TIME_FORMAT, use1904Windowing);
    }
    else
    {
      return new Double(value).toString();
//       return super.formatRawCellContents(value, formatIndex, formatString, use1904Windowing);
    }
  }
}
