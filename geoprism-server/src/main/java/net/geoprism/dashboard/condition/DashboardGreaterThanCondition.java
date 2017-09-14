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
package net.geoprism.dashboard.condition;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.AttributeDate;
import com.runwaysdk.query.AttributeNumber;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.ValueQuery;

public class DashboardGreaterThanCondition extends DashboardPrimitiveCondition implements Reloadable
{
  /**
   * Greater than comparison
   */
  public static final String OPERATION = "gt";

  public DashboardGreaterThanCondition(String mdAttributeId, String comparisonValue)
  {
    super(mdAttributeId, comparisonValue);
  }

  @Override
  public void restrictQuery(ValueQuery query, Selectable attr)
  {
    if (attr instanceof AttributeNumber)
    {
      query.AND( ( (AttributeNumber) attr ).GT(this.getComparisonValue()));
    }
    else if (attr instanceof AttributeDate)
    {
      query.AND( ( (AttributeDate) attr ).GT(this.getComparisonValueAsDate()));
    }
    else
    {
      // Unsupported
      throw new ProgrammingErrorException("Unsupported condition attribute type [" + attr.getClass().getName() + "]");
    }
  }

  @Override
  public String getOperation()
  {
    return OPERATION;
  }
}
