package com.runwaysdk.geodashboard.gis.persist;

import org.json.JSONObject;

import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoNode;

public abstract class AggregationStrategy extends AggregationStrategyBase implements com.runwaysdk.generation.loader.Reloadable
{
  private static final long serialVersionUID = -27686881;

  public AggregationStrategy()
  {
    super();
  }

  public abstract ValueQuery getViewQuery(DashboardThematicLayer layer);

  public abstract JSONObject getJSON();

  public abstract String getCategoryLabel(GeoNode geoNode, String categoryId);

  public abstract AggregationStrategy clone();

  public String getGeometryColumn(DashboardThematicLayer layer)
  {
    GeoNode geoNode = layer.getGeoNode();

    if (layer.getFeatureType().equals(FeatureType.POINT))
    {
      return geoNode.getPointAttribute().getAttributeName();
    }
    else
    {
      return geoNode.getMultiPolygonAttribute().getAttributeName();
    }
  }

}
