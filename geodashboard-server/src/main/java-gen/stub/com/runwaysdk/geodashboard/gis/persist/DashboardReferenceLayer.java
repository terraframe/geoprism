/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.geodashboard.gis.persist;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.geodashboard.gis.EmptyLayerInformation;
import com.runwaysdk.geodashboard.gis.geoserver.GeoserverFacade;
import com.runwaysdk.geodashboard.gis.model.FeatureType;
import com.runwaysdk.geodashboard.gis.model.MapVisitor;
import com.runwaysdk.geodashboard.gis.model.ReferenceLayer;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableSingle;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.Universal;

public class DashboardReferenceLayer extends DashboardReferenceLayerBase implements com.runwaysdk.generation.loader.Reloadable, ReferenceLayer
{
  private static final long serialVersionUID = -1393835330;

  public DashboardReferenceLayer()
  {
    super();
  }

  @Override
  public void accepts(MapVisitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  protected DashboardLayer newInstance()
  {
    return new DashboardReferenceLayer();
  }

  @Override
  public String getJSON()
  {
    return this.toJSON().toString();
  }

  @Override
  public JSONObject toJSON()
  {
    try
    {
      JSONObject json = new JSONObject();
      json.put("viewName", getViewName());
      json.put("sldName", getSLDName());
      json.put("layerName", getName());
      json.put("layerId", getId());
      json.put("inLegend", this.getDisplayInLegend());
      json.put("legendXPosition", this.getDashboardLegend().getLegendXPosition());
      json.put("legendYPosition", this.getDashboardLegend().getLegendYPosition());
      json.put("groupedInLegend", this.getDashboardLegend().getGroupedInLegend());
      json.put("featureStrategy", getFeatureStrategy());
      json.put("universalId", this.getUniversalId());
      json.put("mapId", this.getDashboardMapId());
      json.put("layerExists", true);
      json.put("isActive", true);
      json.put("layerType", "REFERENCELAYER");

      JSONArray jsonStyles = new JSONArray();
      List<? extends DashboardStyle> styles = this.getStyles();
      for (int i = 0; i < styles.size(); ++i)
      {
        DashboardStyle style = styles.get(i);
        jsonStyles.put(style.toJSON());
      }
      json.put("styles", jsonStyles);

      return json;
    }
    catch (JSONException ex)
    {
      log.error("Could not properly form DashboardLayer [" + this.toString() + "] into valid JSON to send back to the client.");
      throw new ProgrammingErrorException(ex);
    }
  }

  /**
   * @prerequisite conditions is populated with any DashboardConditions necessary for restricting the view dataset.
   * 
   * @return A ValueQuery for use in creating/dropping the database view which will be used with GeoServer.
   */
  @Override
  public ValueQuery getViewQuery()
  {
    QueryFactory factory = new QueryFactory();
    ValueQuery query = new ValueQuery(factory);

    OIterator<? extends DashboardStyle> iter = this.getAllHasStyle();
    try
    {
      while (iter.hasNext())
      {
        DashboardStyle style = iter.next();
        if (style instanceof DashboardStyle)
        {

          // geoentity label
          GeoEntityQuery geQ1 = new GeoEntityQuery(query);
          SelectableSingle label = geQ1.getDisplayLabel().localize(GeoEntity.DISPLAYLABEL);
          label.setColumnAlias(GeoEntity.DISPLAYLABEL);

          // geo id (for uniqueness)
          Selectable geoId1 = geQ1.getGeoId(GeoEntity.GEOID);
          geoId1.setColumnAlias(GeoEntity.GEOID);

          // make sure the parent GeoEntity is of the proper Universal
          Universal universal = this.getUniversal();
          query.AND(geQ1.getUniversal().EQ(universal));

          query.SELECT(label);
          query.SELECT(geoId1);

          Selectable geom;
          if (this.getFeatureType().equals(FeatureType.POINT))
          {
            geom = geQ1.get(GeoEntity.GEOPOINT);
          }
          else
          {
            geom = geQ1.get(GeoEntity.GEOMULTIPOLYGON);
          }

          geom.setColumnAlias(GeoserverFacade.GEOM_COLUMN);
          geom.setUserDefinedAlias(GeoserverFacade.GEOM_COLUMN);
          query.SELECT(geom);
        }
      }
    }
    finally
    {
      iter.close();
    }

    if (log.isDebugEnabled())
    {
      // print the SQL if the generated
      log.debug("SLD for Layer [%s], this:\n" + query.getSQL());
    }

    this.viewHasData = true;
    if (query.getCount() == 0)
    {
      EmptyLayerInformation info = new EmptyLayerInformation();
      info.setLayerName(this.getName());
      info.apply();

      info.throwIt();

      this.viewHasData = false;
    }

    return query;
  }

  @Override
  protected void populate(DashboardLayer source)
  {
    super.populate(source);

    if (source instanceof DashboardReferenceLayer)
    {
      DashboardReferenceLayer tSource = (DashboardReferenceLayer) source;

      this.setUniversal(tSource.getUniversal());
    }
  }

  /**
   * JSONObject with containing all options used in the CRUD form.
   * 
   * @return
   */
  public static String getOptionsJSON()
  {
    try
    {
      String[] fonts = DashboardThematicStyle.getSortedFonts();

      // Set possible layer types based on attribute type
      Map<String, String> layerTypes = new LinkedHashMap<String, String>();
      layerTypes.put(AllLayerType.BASICPOINT.getEnumName(), AllLayerType.BASICPOINT.getDisplayLabel());
      layerTypes.put(AllLayerType.BASICPOLYGON.getEnumName(), AllLayerType.BASICPOLYGON.getDisplayLabel());

      JSONArray pointTypes = new JSONArray();
      pointTypes.put("CIRCLE");
      pointTypes.put("STAR");
      pointTypes.put("SQUARE");
      pointTypes.put("TRIANGLE");
      pointTypes.put("CROSS");
      pointTypes.put("X");

      JSONObject object = new JSONObject();
      object.put("availableFonts", new JSONArray(Arrays.asList(fonts)));
      object.put("layerTypeNames", new JSONArray(layerTypes.keySet().toArray()));
      object.put("layerTypeLabels", new JSONArray(layerTypes.values().toArray()));
      object.put("pointTypes", pointTypes);

      return object.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }
}