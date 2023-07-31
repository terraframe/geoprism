/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.registry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.runwaysdk.dataaccess.ProgrammingErrorException;

public class DateUtil
{
  public static final String   DATE_FORMAT     = "yyyy-MM-dd";

  public static final TimeZone SYSTEM_TIMEZONE = TimeZone.getTimeZone("UTC");

  public static String formatDate(Date date, boolean includeTime)
  {
    if (date != null)
    {

      if (!includeTime)
      {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        formatter.setTimeZone(SYSTEM_TIMEZONE);
        return formatter.format(date);
      }
      else
      {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatter.setTimeZone(SYSTEM_TIMEZONE);
        return formatter.format(date);
      }
    }

    return null;
  }

  public static String formatIso8601(Date date, boolean includeTime)
  {
    if (date == null)
    {
      return "null";
    }

    if (!includeTime)
    {
      SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
      formatter.setTimeZone(SYSTEM_TIMEZONE);
      return formatter.format(date);
    }
    else
    {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      formatter.setTimeZone(SYSTEM_TIMEZONE);
      return formatter.format(date);
    }
  }

  public static Date parseIso8601(String date)
  {
    String s = "2020-02-13T18:51:09.840Z";
    TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(s);
    Instant i = Instant.from(ta);
    Date d = Date.from(i);
    return d;
  }

  public static Date parseDate(String date)
  {
    return parseDate(date, false);
  }

  public static Date parseDate(String date, boolean throwClientException)
  {
    if (date != null && date.length() > 0)
    {

      try
      {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        format.setTimeZone(SYSTEM_TIMEZONE);

        return format.parse(date);
      }
      catch (ParseException e)
      {
        if (throwClientException)
        {
          throw new RuntimeException("Unable to parse the date [" + date + "]. The date format must be [" + DATE_FORMAT + "]");
        }
        else
        {
          throw new ProgrammingErrorException(e);
        }
      }
    }

    return null;
  }
  
  public static Date getCurrentDate()
  {
    Calendar calendar = Calendar.getInstance(SYSTEM_TIMEZONE);
    String dateString = calendar.get(Calendar.YEAR) + "-" + ( calendar.get(Calendar.MONTH) + 1 ) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

    return parseDate(dateString);
  }

  public static boolean isBetweenInclusive(Date versionDate, Date startDate, Date endDate)
  {
    return ( versionDate.after(startDate) || versionDate.equals(startDate) ) && ( versionDate.before(endDate) || versionDate.equals(endDate) );
  }


}
