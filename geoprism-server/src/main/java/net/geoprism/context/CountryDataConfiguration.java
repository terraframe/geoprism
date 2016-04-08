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
package net.geoprism.context;

public class CountryDataConfiguration
{
  private String key;

  private String version;

  private String format;

  public CountryDataConfiguration(String key, String version, String format)
  {
    this.key = key;
    this.version = version;
    this.format = format;
  }

  public String getKey()
  {
    return key;
  }

  public String getVersion()
  {
    return version;
  }

  public String getFormat()
  {
    return format;
  }

}
