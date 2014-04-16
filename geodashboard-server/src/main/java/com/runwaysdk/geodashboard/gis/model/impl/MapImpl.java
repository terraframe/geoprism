package com.runwaysdk.geodashboard.gis.model.impl;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.geodashboard.gis.model.Layer;
import com.runwaysdk.geodashboard.gis.model.Map;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;

public class MapImpl extends ComponentImpl implements Map
{
  private List<Layer> layers;
  
  private String name;
  
  public MapImpl()
  {
    this.name = null;
    this.layers = new LinkedList<Layer>(); 
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  public String getName()
  {
    return name;
  }
  
  @Override
  public void addLayer(Layer layer)
  {
    this.layers.add(layer);    
  }
  
  @Override
  public List<Layer> getLayers()
  {
    return layers;
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
    
    for(Layer layer : this.layers)
    {
      layer.accepts(visitor);
    }
  }
}
