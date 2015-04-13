package com.runwaysdk.geodashboard.gis.geoserver;

import java.util.ArrayList;
import java.util.List;

import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.geodashboard.gis.persist.DashboardLayer;
import com.runwaysdk.geodashboard.gis.persist.DashboardStyle;

public class GeoserverBatch implements Reloadable
{
  /**
   * These are for storing mass publish/deletes which can be pushArrayList<E> once for maximum efficiency.
   */
  private ArrayList<DashboardLayer> layersToPublish;

  private ArrayList<String>         layersToDrop;

  private ArrayList<String>         stylesToDrop;

  public GeoserverBatch()
  {
    this.layersToPublish = new ArrayList<DashboardLayer>();
    this.layersToDrop = new ArrayList<String>();
    this.stylesToDrop = new ArrayList<String>();
  }

  public void addLayerToPublish(DashboardLayer layer)
  {
    this.layersToPublish.add(layer);
  }

  public void addLayerToDrop(DashboardLayer layer)
  {
    layersToDrop.add(layer.getViewName());

    List<? extends DashboardStyle> styles = layer.getStyles();

    for (int i = 0; i < styles.size(); ++i)
    {
      DashboardStyle style = styles.get(i);

      stylesToDrop.add(style.getName());
    }
  }

  public ArrayList<String> getLayersToDrop()
  {
    return layersToDrop;
  }

  public ArrayList<DashboardLayer> getLayersToPublish()
  {
    return layersToPublish;
  }

  public ArrayList<String> getStylesToDrop()
  {
    return stylesToDrop;
  }
}
