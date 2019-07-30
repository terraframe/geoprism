/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;

public class QueryConfiguration
{
  private String       queryId;

  private String       criteria;

  private String       aggregation;

  private String       layerId;

  private String       category;

  private String       defaultGeoId;

  private String       geoNodeId;

  private ValueQuery   vQuery;

  private QueryFactory factory;

  public QueryConfiguration(String queryId, String context) throws JSONException
  {
    JSONObject object = new JSONObject(context);

    this.queryId = queryId;
    this.criteria = this.getCriteria(object);
    this.aggregation = this.getAggregation(object);
    this.category = this.getCategory(object);
    this.layerId = this.getLayerId(object);
    this.defaultGeoId = this.getDefaultGeoId(object);
    this.geoNodeId = this.getGeoNodeId(object);

    this.factory = new QueryFactory();
    this.vQuery = new ValueQuery(this.factory);
  }

  protected String getCategory(JSONObject object) throws JSONException
  {
    return this.get(object, BirtConstants.CATEGORY);
  }

  protected String getCriteria(JSONObject object) throws JSONException
  {
    return this.get(object, BirtConstants.CRITERIA);
  }

  protected String getAggregation(JSONObject object) throws JSONException
  {
    return this.get(object, BirtConstants.AGGREGATION);
  }

  protected String getLayerId(JSONObject object) throws JSONException
  {
    return this.get(object, BirtConstants.LAYER_ID);
  }

  protected String getDefaultGeoId(JSONObject object) throws JSONException
  {
    return this.get(object, BirtConstants.DEFAULT_GEO_ID);
  }

  protected String getGeoNodeId(JSONObject object) throws JSONException
  {
    return this.get(object, BirtConstants.GEO_NODE_ID);
  }

  private String get(JSONObject object, String key) throws JSONException
  {
    if (object.has(key))
    {
      return object.getString(key);
    }

    return null;
  }

  public String getCriteria()
  {
    return criteria;
  }

  public void setCriteria(String criteria)
  {
    this.criteria = criteria;
  }

  public String getAggregation()
  {
    return aggregation;
  }

  public void setAggregation(String aggregation)
  {
    this.aggregation = aggregation;
  }

  public String getLayerId()
  {
    return layerId;
  }

  public void setLayerId(String layerId)
  {
    this.layerId = layerId;
  }

  public String getCategory()
  {
    return category;
  }

  public void setCategory(String category)
  {
    this.category = category;
  }

  public ValueQuery getValueQuery()
  {
    return vQuery;
  }

  public QueryFactory getFactory()
  {
    return factory;
  }

  public String getQueryId()
  {
    return queryId;
  }

  public String getDefaultGeoId()
  {
    return defaultGeoId;
  }

  public void setDefaultGeoId(String defaultGeoId)
  {
    this.defaultGeoId = defaultGeoId;
  }

  public String getGeoNodeId()
  {
    return this.geoNodeId;
  }

  public void setGeoNodeId(String geoNodeId)
  {
    this.geoNodeId = geoNodeId;
  }
  
  public boolean hasGeoNodeId()
  {
    return ( this.geoNodeId != null && this.geoNodeId.length() > 0 );
  }

  public boolean hasDefaultGeoId()
  {
    return ( this.defaultGeoId != null && this.defaultGeoId.length() > 0 );
  }

  public boolean hasLayerId()
  {
    return ( this.layerId != null && this.layerId.length() > 0 );
  }

  public boolean hasCategory()
  {
    return ( this.category != null && this.category.length() > 0 );
  }
}
