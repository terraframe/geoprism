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
package net.geoprism.shapefile;

public class LabeledValueBean
{
  private String value;

  private String label;

  public LabeledValueBean(String value)
  {
    super();
    this.value = value;
    this.label = value;
  }

  public LabeledValueBean(String value, String label)
  {
    super();
    this.value = value;
    this.label = label;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof LabeledValueBean)
    {
      LabeledValueBean bean = (LabeledValueBean) obj;
      String _value = bean.getValue();

      if (value != null && _value != null)
      {
        return value.equals(_value);
      }

      return value == _value;
    }

    return super.equals(obj);
  }

}
