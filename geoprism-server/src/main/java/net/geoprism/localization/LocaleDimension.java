/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.localization;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.RunwayConfigurationException;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.io.FileReadException;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;


import net.geoprism.configuration.GeoprismConfigGroup;

public class LocaleDimension 
{
  private String           locale;

  private MdDimensionDAOIF dimension;

  public LocaleDimension()
  {
    this(null, null);
  }

  public LocaleDimension(String locale)
  {
    this(locale, null);
  }

  public LocaleDimension(String locale, MdDimensionDAOIF dimension)
  {
    this.locale = locale;
    this.dimension = dimension;
  }

  public static LocaleDimension parseColumnHeader(String column)
  {
    String[] split = column.split(" ");
    if (split.length == 1)
    {
      return new LocaleDimension(column);
    }
    else
    {
      MdDimensionDAOIF dim = MdDimensionDAO.getByName(split[0]);
      return new LocaleDimension(split[1], dim);
    }
  }

  public String getAttributeName()
  {
    if (dimension != null)
    {
      return dimension.getLocaleAttributeName(locale);
    }
    return locale;
  }

  public String getColumnName()
  {
    if (dimension != null)
    {
      return dimension.getName() + " " + locale;
    }
    return locale;
  }

  public String getResourceName(String bundle)
  {
    String filename = bundle;
    if (dimension != null)
    {
      filename += "-" + dimension.getName();
    }
    if (locale != null && !locale.equals(MdAttributeLocalInfo.DEFAULT_LOCALE))
    {
      filename += "_" + locale.toString();
    }
    filename += ".properties";

    return "/" + filename;
  }

  public Properties getPropertiesFromFile(String bundle)
  {
    String fileName = this.getResourceName(bundle);

    InputStream stream;
    try
    {
      stream = ConfigurationManager.getResourceAsStream(GeoprismConfigGroup.ROOT, fileName);
    }
    catch (RunwayConfigurationException e)
    {
      return new Properties();
    }
    
    try
    {
      Properties prop = new Properties();
      prop.load(new InputStreamReader(stream, "UTF-8"));

      return prop;
    }
    catch (IOException e)
    {
      throw new FileReadException(new File(fileName), e);
    }
  }

  public String getLocaleString()
  {
    return locale;
  }

  public boolean hasDimension()
  {
    return dimension != null;
  }

  /**
   * Returns the parent dimension of this LocaleDimension. Dimension is
   * unchanged; only the locale becomes more generic. If this instance already
   * represents the default locale, then null is returned.
   * 
   * @return The parent dimension of this LocaleDimension
   */
  public LocaleDimension getParent()
  {
    if (locale.equals(MdAttributeLocalInfo.DEFAULT_LOCALE) || locale.length() == 0)
    {
      return null;
    }

    int index = locale.lastIndexOf("_");
    if (index == -1)
    {
      return new LocaleDimension(MdAttributeLocalInfo.DEFAULT_LOCALE, this.dimension);
    }
    else
    {
      return new LocaleDimension(locale.substring(0, index), this.dimension);
    }
  }

  @Override
  public boolean equals(Object obj)
  {
    if (! ( obj instanceof LocaleDimension ))
    {
      return false;
    }

    LocaleDimension other = (LocaleDimension) obj;
    if (this.hasDimension() && !other.hasDimension() || !this.hasDimension() && other.hasDimension())
    {
      return false;
    }

    if (this.hasDimension())
    {
      if (!this.dimension.getName().equals(other.dimension.getName()))
      {
        return false;
      }
    }

    if (!this.locale.equals(other.locale))
    {
      return false;
    }

    return true;
  }

  /**
   * True IFF this object has a dimension, the parent has no dimension, and the
   * locales are equal.
   * 
   * @param parent
   * @return
   */
  public boolean isDimensionChildOf(LocaleDimension parent)
  {
    if (parent.hasDimension() || !this.hasDimension())
    {
      return false;
    }

    return this.locale.equals(parent.locale);
  }

  @Override
  public String toString()
  {
    String string = new String();
    if (dimension != null)
    {
      string += dimension.getName() + " ";
    }
    string += locale;
    return string;
  }
}
