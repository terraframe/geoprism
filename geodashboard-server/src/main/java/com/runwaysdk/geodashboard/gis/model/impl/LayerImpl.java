package com.runwaysdk.geodashboard.gis.model.impl;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.geodashboard.gis.model.Layer;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.Style;

public class LayerImpl extends ComponentImpl implements Layer
{
  private List<Style> styles;
  
  private String name;

  private Boolean virtual;

  private FeatureType featureType;
  
  public LayerImpl(String name)
  {
    this.name = name;
    this.styles = new LinkedList<Style>();
    
    this.virtual = false;
  }
  
  public String getName()
  {
    return name;
  }
  
  public void addStyle(Style style)
  {
    this.styles.add(style);
  }
  
  @Override
  public List<Style> getStyles()
  {
    return this.styles;
  }
  
  @Override
  public void setVirtual(Boolean virtual)
  {
    this.virtual = virtual;
  }
  
  @Override
  public Boolean getVirtual()
  {
    return this.virtual;
  }
  
  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
    
    for(Style style : styles)
    {
      style.accepts(visitor);
    }
  }

  @Override
  public void setFeatureType(FeatureType featureType)
  {
    this.featureType = featureType;    
  }

  @Override
  public FeatureType getFeatureType()
  {
    return this.featureType;
  }

}
