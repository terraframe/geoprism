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

import com.runwaysdk.geodashboard.gis.model.condition.Composite;
import com.runwaysdk.geodashboard.gis.model.condition.Condition;

public abstract class CompositeImpl extends ConditionImpl implements Composite
{
  private Condition leftCondition;
  
  private Condition rightCondition;
  
  public CompositeImpl()
  {
    super();
  }
  
  public void setRightCondition(Condition condition)
  {
    this.rightCondition = condition;    
  }
  
  public void setLeftCondition(Condition condition)
  {
    this.leftCondition = condition;    
  }
  
  @Override
  public Condition getLeftCondition()
  {
    return leftCondition;
  }
  
  @Override
  public Condition getRightCondition()
  {
    return rightCondition;
  }
}
