package com.runwaysdk.geodashboard.gis.model;

import java.util.HashMap;

import com.runwaysdk.generation.loader.Reloadable;


public interface ThematicLayer extends Reloadable, Layer
{
  
  public HashMap<String, Double> getLayerMinMax(String attribute);

}
