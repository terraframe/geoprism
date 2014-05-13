package com.runwaysdk.geodashboard.gis.persist;

import java.util.List;

import com.runwaysdk.geodashboard.gis.model.Layer;
import com.runwaysdk.geodashboard.gis.model.Map;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;

public class DashboardMap extends DashboardMapBase implements com.runwaysdk.generation.loader.Reloadable, Map
{
  private static final long serialVersionUID = 861649895;
  
  public DashboardMap()
  {
    super();
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);    
  }

  @Override
  public List<? extends Layer> getLayers()
  {
    return this.getAllHasLayer().getAll();
  }
  
//  @Override
  public String getMapJSON()
  {
   
    String json = "test json";
    
    // TODO Auto-generated method stub
    return json;
  }

  public void delete()
  {
    for(DashboardLayer layer : this.getAllHasLayer())
    {
      layer.delete();
    }
    
    super.delete();
  }
  
}
