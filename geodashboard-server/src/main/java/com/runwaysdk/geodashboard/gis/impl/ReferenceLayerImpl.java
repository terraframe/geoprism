package com.runwaysdk.geodashboard.gis.impl;

import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.ReferenceLayer;

public class ReferenceLayerImpl extends LayerImpl implements ReferenceLayer
{

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

}
