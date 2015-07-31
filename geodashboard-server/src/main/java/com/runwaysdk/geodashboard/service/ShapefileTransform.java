package com.runwaysdk.geodashboard.service;

import com.runwaysdk.generation.loader.Reloadable;

public interface ShapefileTransform extends Reloadable
{
  public Object transform(Object value);
}
