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
package com.runwaysdk.geodashboard.gis.impl.condition;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.IsBetween;

public class IsBetweenImpl extends ConditionImpl implements IsBetween
{
  
  private String lowerBound;
  
  private String upperBound;
  
  public IsBetweenImpl()
  {
    super();
  }
  
  @Override
  public void setLowerBound(String lowerBound)
  {
    this.lowerBound = lowerBound;
  }
  
  @Override
  public void setUpperBound(String upperBound)
  {
    this.upperBound = upperBound;
  }
  
  @Override
  public String getLowerBound()
  {
    return lowerBound;
  }
  
  @Override
  public String getUpperBound()
  {
    return upperBound;
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);    
  }


}
