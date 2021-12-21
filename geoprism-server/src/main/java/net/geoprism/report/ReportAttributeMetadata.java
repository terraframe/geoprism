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
package net.geoprism.report;

import net.geoprism.dashboard.AllAggregationType;



public class ReportAttributeMetadata 
{
  private String             attributeName;

  private AllAggregationType aggregation;

  private Boolean            orderBy;

  public ReportAttributeMetadata(String attributeName)
  {
    this(null, attributeName, false);
  }

  public ReportAttributeMetadata(AllAggregationType aggregation, String attributeName)
  {
    this(aggregation, attributeName, false);
  }

  public ReportAttributeMetadata(AllAggregationType aggregation, String attributeName, Boolean orderBy)
  {
    super();
    this.aggregation = aggregation;
    this.attributeName = attributeName;
    this.orderBy = orderBy;
  }

  public AllAggregationType getAggregation()
  {
    return aggregation;
  }

  public void setAggregation(AllAggregationType aggregation)
  {
    this.aggregation = aggregation;
  }

  public String getAttributeName()
  {
    return attributeName;
  }

  public void setAttributeName(String attributeName)
  {
    this.attributeName = attributeName;
  }

  public void setOrderBy(Boolean orderBy)
  {
    this.orderBy = orderBy;
  }

  public Boolean isOrderBy()
  {
    return orderBy;
  }
}
