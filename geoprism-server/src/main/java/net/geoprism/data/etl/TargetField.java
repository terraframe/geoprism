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
package net.geoprism.data.etl;

public abstract class TargetField implements TargetFieldIF
{
  private String  name;

  private String  label;

  private String  key;

  private Boolean aggregatable;

  public TargetField()
  {
    this.aggregatable = true;
  }

  @Override
  public String getKey()
  {
    return this.key;
  }

  public void setKey(String key)
  {
    this.key = key;
  }

  @Override
  public String getLabel()
  {
    return this.label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @Override
  public String getName()
  {
    return this.name;
  }

  @Override
  public Boolean getAggregatable()
  {
    return this.aggregatable;
  }

  public void setAggregatable(Boolean aggregatable)
  {
    this.aggregatable = aggregatable;
  }
}
