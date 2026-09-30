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
package net.geoprism.registry.view.serialization;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.ext.javatime.DateTimeParseException;

public class DateTimeDeserializer extends StdDeserializer<Date>
{
  public DateTimeDeserializer()
  {
    super(Date.class);
  }

  @Override
  public Date deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException
  {
    String date = p.getString();
    if (!StringUtils.isBlank(date))
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mmZ");
      sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

      try
      {
        return sdf.parse(date);
      }
      catch (ParseException e)
      {
        throw new DateTimeParseException(p, "Failed to parse date time value [" + date + "]", date, Date.class, e);
      }

    }
    return null;
  }
}