/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.gis.geoserver;

import java.util.ArrayList;
import java.util.List;

import net.geoprism.dashboard.DashboardStyle;
import net.geoprism.dashboard.layer.DashboardLayer;



public class GeoserverBatch 
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
    String viewName = layer.getViewName();

    if (viewName != null && viewName.length() > 0)
    {
      layersToDrop.add(viewName);
    }

    List<? extends DashboardStyle> styles = layer.getStyles();

    for (int i = 0; i < styles.size(); ++i)
    {
      DashboardStyle style = styles.get(i);

      String styleName = style.getName();

      if (styleName != null && styleName.length() > 0)
      {
        stylesToDrop.add(styleName);
      }
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
