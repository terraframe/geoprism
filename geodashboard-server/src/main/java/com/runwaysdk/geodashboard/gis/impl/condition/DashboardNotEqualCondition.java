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

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeCharacter;
import com.runwaysdk.query.AttributeText;
import com.runwaysdk.query.AttributeDate;
import com.runwaysdk.query.AttributeNumber;
import com.runwaysdk.query.ValueQuery;

public class DashboardNotEqualCondition extends DashboardPrimitiveCondition implements Reloadable
{
  /**
   * Not Equal comparison
   */
  public static final String OPERATION = "neq";

  public DashboardNotEqualCondition(String mdAttributeId, String comparisonValue)
  {
    super(mdAttributeId, comparisonValue);
  }

  @Override
  public void restrictQuery(ValueQuery query, Attribute attr)
  {
    if (attr instanceof AttributeNumber)
    {
      query.AND( ( (AttributeNumber) attr ).NE(this.getComparisonValue()));
    }
    else if (attr instanceof AttributeDate)
    {
      query.AND( ( (AttributeDate) attr ).NE(this.getComparisonValueAsDate()));
    }
    else if (attr instanceof AttributeCharacter)
    {
      query.AND( ( (AttributeCharacter) attr ).NE(this.getComparisonValue()));
    }
    else if (attr instanceof AttributeText)
    {
      query.AND( ( (AttributeText) attr ).NE(this.getComparisonValue()));
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
