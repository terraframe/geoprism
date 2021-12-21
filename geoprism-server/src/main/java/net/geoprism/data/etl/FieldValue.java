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
package net.geoprism.data.etl;


public class FieldValue
{
  /**
   * Flag denoting if the source value for the field was blank. This may be true even if the value is not null, because
   * the value may be derived.
   */
  private boolean blank;

  private Object  value;

  public FieldValue()
  {
    this(null, true);
  }

  public FieldValue(Object value)
  {
    this(value, false);
  }

  public FieldValue(Object value, boolean blank)
  {
    this.value = value;
    this.blank = blank;
  }

  public boolean isBlank()
  {
    return this.blank;
  }

  public void setBlank(boolean blank)
  {
    this.blank = blank;
  }

  public void setValue(Object value)
  {
    this.value = value;
  }

  public Object getValue()
  {
    return value;
  }
}
