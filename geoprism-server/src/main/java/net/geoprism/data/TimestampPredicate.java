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
package net.geoprism.data;

import java.io.File;
import java.util.Date;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimestampPredicate implements Predicate<String>
{
  private Set<Date> timestamps;

  private Pattern   pattern;

  public TimestampPredicate(Set<Date> timestamps)
  {
    this.timestamps = timestamps;
    this.pattern = Pattern.compile("[A-Za-z_\\s\\-]*\\((\\d{13,})\\)[A-Za-z_\\-]*.xml.gz");
  }

  @Override
  public boolean test(String key)
  {
    File file = new File(key);
    String name = file.getName();

    Matcher matcher = pattern.matcher(name);

    if (matcher.matches())
    {
      Long timestamp = Long.parseLong(matcher.group(1));
      Date date = new Date(timestamp);

      return !timestamps.contains(date);
    }

    return true;
  }

}
