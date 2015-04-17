package com.runwaysdk.geodashboard.gis.impl;

import java.util.HashMap;

import com.runwaysdk.geodashboard.gis.model.AttributeType;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.SecondaryAttributeStyleIF;
import com.runwaysdk.geodashboard.gis.model.ThematicLayer;

public class ThematicLayerImpl extends LayerImpl implements ThematicLayer
{
  private String                  attribute;

  private AttributeType           attributeType;

  private HashMap<String, Double> layerMinMax;

  public HashMap<String, Double> getLayerMinMax(String attribute)
  {
    return layerMinMax;
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public String getAttribute()
  {
    return this.attribute;
  }

  public void setAttribute(String attribute)
  {
    this.attribute = attribute;
  }

  @Override
  public AttributeType getAttributeType()
  {
    return this.attributeType;
  }

  public void setAttributeType(AttributeType attributeType)
  {
    this.attributeType = attributeType;
  }

  @Override
  public SecondaryAttributeStyleIF getSecondaryAttributeStyle()
  {
    return null;
  }

}
