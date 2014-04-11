package com.runwaysdk.geodashboard.gis.model.builder;

import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.geodashboard.gis.model.Layer;
import com.runwaysdk.geodashboard.gis.model.impl.LayerImpl;

public class LayerBuilder
{
  private MapBuilder mb;
  
  private Layer layer;
  
  public LayerBuilder(MapBuilder mb, String name)
  {
    this.mb = mb;
    layer = new LayerImpl(name);
    this.mb.getMap().addLayer(layer);
  }
  
  public LayerBuilder virtual(Boolean virtual)
  {
    this.layer.setVirtual(virtual);
    return this;
  }
  
  Layer getLayer()
  {
    return layer;
  }
  
  public LayerBuilder featureType(FeatureType type)
  {
    layer.setFeatureType(type);
    return this;
  }
  
  public MapBuilder add()
  {
    return this.mb;
  }
  
  public StyleBuilder style(String name)
  {
    StyleBuilder sb = new StyleBuilder(this, name);
    return sb;
  }

  public ThematicStyleBuilder tStyle(String name)
  {
    return new ThematicStyleBuilder(this, name);
  }
}
