package com.runwaysdk.geodashboard.report;

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
