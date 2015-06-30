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
package com.runwaysdk.geodashboard.gis.persist.condition;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.condition.Equal;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeBoolean;
import com.runwaysdk.query.AttributeCharacter;
import com.runwaysdk.query.AttributeDate;
import com.runwaysdk.query.AttributeNumber;
import com.runwaysdk.query.ValueQuery;

public class DashboardEqual extends DashboardEqualBase implements com.runwaysdk.generation.loader.Reloadable, Equal
{

  /**
   * Equal comparison
   */
  public static final String OPERATION        = "eq";

  private static final long  serialVersionUID = 1213634698;

  public DashboardEqual()
  {
    super();
  }

  @Override
  public void restrictQuery(ValueQuery query, Attribute attr)
  {
    if (attr instanceof AttributeNumber)
    {
      query.AND( ( (AttributeNumber) attr ).EQ(this.getComparisonValue()));
    }
    else if (attr instanceof AttributeBoolean)
    {
      query.AND( ( (AttributeBoolean) attr ).EQ(this.getComparisonValueAsBoolean()));
    }
    else if (attr instanceof AttributeDate)
    {
      query.AND( ( (AttributeDate) attr ).EQ(this.getComparisonValueAsDate()));
    }
    else if (attr instanceof AttributeCharacter)
    {
      query.AND( ( (AttributeCharacter) attr ).EQ(this.getComparisonValue()));
    }
    else
    {
      // Unsupported
      throw new ProgrammingErrorException("Unsupported condition attribute type [" + attr.getClass().getName() + "]");
    }
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public String getOperation()
  {
    return OPERATION;
  }

  @Override
  protected DashboardCondition newInstance()
  {
    return new DashboardEqual();
  }

}
