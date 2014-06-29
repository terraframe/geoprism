package com.runwaysdk.geodashboard.gis.impl;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.geodashboard.gis.model.FeatureStrategy;
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

  private Boolean displayInLegend;
  
  private FeatureStrategy featureStrategy;
  
  public LayerImpl()
  {
    this.name = null;
    this.styles = new LinkedList<Style>();
    
    this.virtual = false;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  @Override
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
  }

  public void setFeatureType(FeatureType featureType)
  {
    this.featureType = featureType;    
  }

  @Override
  public FeatureType getFeatureType()
  {
    return this.featureType;
  }

  @Override
  public Boolean getDisplayInLegend()
  {
    return this.displayInLegend;
  }

  public void setDisplayInLegend(Boolean display)
  {
    this.displayInLegend = display;
  }
  
  @Override
  public FeatureStrategy getFeatureStrategy()
  {
    return featureStrategy;
  }
  
  public void setFeatureStrategy(FeatureStrategy featureStrategy)
  {
    this.featureStrategy = featureStrategy;
  }

}
