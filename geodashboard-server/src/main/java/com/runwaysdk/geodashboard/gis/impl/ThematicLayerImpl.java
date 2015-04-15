package com.runwaysdk.geodashboard.gis.impl;

import java.util.HashMap;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.ThematicLayer;

public class ThematicLayerImpl extends LayerImpl implements ThematicLayer
{

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

}
