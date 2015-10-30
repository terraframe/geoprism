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

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generated.system.gis.geo.GeoEntityAllPathsTableQuery;
import com.runwaysdk.geodashboard.GeoEntityUtil;
import com.runwaysdk.query.Attribute;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;

public class LocationCondition extends DashboardCondition implements com.runwaysdk.generation.loader.Reloadable
{

  /**
   * Condition type for restricting on global location
   */
  public static final String CONDITION_TYPE = "LOCATION_CONDITION";

  /**
   * JSON key for the serialized label attribute
   */
  public static final String LABEL_KEY      = "label";

  private String             comparisonValueId;

  public LocationCondition()
  {
    super();
  }

  public LocationCondition(String comparisonValueId)
  {
    super();

    this.comparisonValueId = comparisonValueId;
  }

  public String getComparisonValueId()
  {
    return comparisonValueId;
  }

  public void setComparisonValueId(String comparisonValueId)
  {
    this.comparisonValueId = comparisonValueId;
  }

  public GeoEntity getComparisonValue()
  {
    return GeoEntity.get(comparisonValueId);
  }

  @Override
  public void restrictQuery(ValueQuery query, Attribute attr)
  {
    GeoEntity entity = this.getComparisonValue();

    if (entity != null)
    {
      AttributeReference attributeReference = (AttributeReference) attr;

      GeoEntityAllPathsTableQuery aptQuery = new GeoEntityAllPathsTableQuery(query);

      query.AND(aptQuery.getParentTerm().EQ(entity));
      query.AND(attributeReference.EQ(aptQuery.getChildTerm()));
    }
  }

  // @Override
  public String getComparisonLabel()
  {
    GeoEntity entity = this.getComparisonValue();

    if (entity != null)
    {
      return GeoEntityUtil.getEntityLabel(entity);
    }

    return "";
  }

  @Override
  public JSONObject getJSON()
  {
    try
    {
      JSONObject object = new JSONObject();
      object.put(TYPE_KEY, CONDITION_TYPE);
      object.put(OPERATION_KEY, OPERATION_KEY);
      object.put(VALUE_KEY, this.getComparisonValueId());
      object.put(LABEL_KEY, this.getComparisonLabel());

      return object;
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public String getJSONKey()
  {
    return CONDITION_TYPE;
  }
}
