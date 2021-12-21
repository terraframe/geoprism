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
package net.geoprism.dashboard.layer;

/**
 *
 * @author Autogenerated by RunwaySDK
 */
public class DashboardReferenceLayerViewQuery extends net.geoprism.dashboard.layer.DashboardReferenceLayerViewQueryBase  
{

  public DashboardReferenceLayerViewQuery(com.runwaysdk.query.QueryFactory queryFactory)
  {
    super(queryFactory);
    this.buildQuery(new DefaultDashboardReferenceLayerViewBuilder(queryFactory));
  }

  public DashboardReferenceLayerViewQuery(com.runwaysdk.query.QueryFactory queryFactory, com.runwaysdk.query.ViewQueryBuilder viewQueryBuilder)
  {
    super(queryFactory, viewQueryBuilder);
  }

  class DefaultDashboardReferenceLayerViewBuilder extends com.runwaysdk.query.ViewQueryBuilder 
  {
    public DefaultDashboardReferenceLayerViewBuilder(com.runwaysdk.query.QueryFactory queryFactory)
    {
      super(queryFactory);
    }

    protected DashboardReferenceLayerViewQuery getViewQuery()
    {
      return (DashboardReferenceLayerViewQuery)super.getViewQuery();
    }

    /**
     * build the select clause
     */
    protected void buildSelectClause()
    {
      String errMsg = "buildSelectClause() method in class DefaultDashboardReferenceLayerViewBuilder needs to be overwritten.";
      throw new com.runwaysdk.query.QueryException(errMsg);
    }

    /**
     * Implement only if additional join criteria is required.
     */
    protected void buildWhereClause()
    {

    }

  }
}
