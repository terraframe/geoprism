package com.runwaysdk.geodashboard.gis.persist;

import org.json.JSONObject;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.ReferenceLayer;
import com.runwaysdk.query.ValueQuery;

public class DashboardReferenceLayer extends DashboardReferenceLayerBase implements com.runwaysdk.generation.loader.Reloadable, ReferenceLayer
{
  private static final long serialVersionUID = -1393835330;

  public DashboardReferenceLayer()
  {
    super();
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public ValueQuery getViewQuery()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JSONObject toJSON()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected DashboardLayer newInstance()
  {
    return new DashboardReferenceLayer();
  }

}
