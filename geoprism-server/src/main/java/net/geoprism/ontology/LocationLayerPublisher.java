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
package net.geoprism.ontology;

import java.util.LinkedList;
import java.util.List;

import net.geoprism.data.DatabaseUtil;
import net.geoprism.gis.geoserver.GeoserverFacade;
import net.geoprism.gis.geoserver.GeoserverLayer;
import net.geoprism.gis.geoserver.GeoserverLayerIF;
import net.geoprism.gis.geoserver.SessionPredicate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableSingle;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.LocatedInQuery;
import com.runwaysdk.system.gis.geo.Universal;

public class LocationLayerPublisher
{
  public static enum LayerType {
    POINT, POLYGON
  }

  private String id;

  private String layers;

  private String universalId;

  public LocationLayerPublisher(String id, String universalId, String layers)
  {
    this.id = id;
    this.universalId = universalId;
    this.layers = layers;
  }

  public String publish()
  {
    try
    {
      /*
       * Unpublish any layer existing layers from geoserver
       */
      if (this.layers != null)
      {
        JSONArray deserialized = new JSONArray(this.layers);

        for (int i = 0; i < deserialized.length(); i++)
        {
          JSONObject layer = deserialized.getJSONObject(i);
          String layerName = layer.getString("layerName");

          GeoserverFacade.removeLayer(layerName);
        }
      }

      /*
       * Create new Database views
       */
      List<GeoserverLayerIF> layers = this.createDatabaseViews();

      /*
       * Publish new layers to geoserver
       */
      JSONArray serialized = new JSONArray();

      for (GeoserverLayerIF layer : layers)
      {
        GeoserverFacade.publishLayer(layer);

        serialized.put(layer.toJSON());
      }

      return serialized.toString();
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Transaction
  private List<GeoserverLayerIF> createDatabaseViews() throws JSONException
  {
    if (this.layers != null)
    {
      JSONArray deserialized = new JSONArray(this.layers);

      for (int i = 0; i < deserialized.length(); i++)
      {
        JSONObject layer = deserialized.getJSONObject(i);
        String layerName = layer.getString("layerName");

        DatabaseUtil.dropView(layerName, "", false);
      }
    }

    GeoEntity entity = GeoEntity.get(this.id);
    Universal universal = entity.getUniversal();
    List<Term> descendants = universal.getAllDescendants(AllowedIn.CLASS).getAll();

    List<GeoserverLayerIF> layers = new LinkedList<GeoserverLayerIF>();
    layers.add(this.publishEntityLayer(entity, descendants));

    if (this.universalId != null)
    {
      layers.add(this.publishChildLayer(entity, descendants));
    }

    return layers;
  }

  private GeoserverLayerIF publishChildLayer(GeoEntity entity, List<Term> descendants) throws JSONException
  {
    LayerType layerType = this.getChildLayerType(descendants);
    String viewName = SessionPredicate.generateId();
    String sql = this.getChildQuery(entity, layerType).getSQL();

    DatabaseUtil.createView(viewName, sql);

    GeoserverLayer layer = new GeoserverLayer();
    layer.setLayerName(viewName);
    layer.setStyleName(this.getStyle(layerType));
    layer.setLayerType(layerType);

    return layer;
  }

  private GeoserverLayerIF publishEntityLayer(GeoEntity entity, List<Term> descendants) throws JSONException
  {
    LayerType layerType = this.getEntityLayerType(descendants);
    String viewName = SessionPredicate.generateId();
    String sql = this.getEntityQuery(entity, layerType).getSQL();

    DatabaseUtil.createView(viewName, sql);

    GeoserverLayer layer = new GeoserverLayer();
    layer.setLayerName(viewName);
    layer.setStyleName(this.getStyle(layerType));
    layer.setLayerType(layerType);

    return layer;
  }

  private ValueQuery getEntityQuery(GeoEntity entity, LayerType type)
  {
    QueryFactory factory = new QueryFactory();
    ValueQuery query = new ValueQuery(factory);

    // geoentity label
    GeoEntityQuery geQ1 = new GeoEntityQuery(query);
    SelectableSingle label = geQ1.getDisplayLabel().localize(GeoEntity.DISPLAYLABEL);
    label.setColumnAlias(GeoEntity.DISPLAYLABEL);

    // geo id (for uniqueness)
    Selectable geoId = geQ1.getGeoId(GeoEntity.GEOID);
    geoId.setColumnAlias(GeoEntity.GEOID);

    Selectable geom = ( type.equals(LayerType.POINT) ? geQ1.get(GeoEntity.GEOPOINT) : geQ1.get(GeoEntity.GEOMULTIPOLYGON) );
    geom.setColumnAlias(GeoserverFacade.GEOM_COLUMN);
    geom.setUserDefinedAlias(GeoserverFacade.GEOM_COLUMN);

    query.SELECT(label, geoId, geom);
    query.WHERE(geQ1.getId().EQ(entity.getId()));

    return query;
  }

  private ValueQuery getChildQuery(GeoEntity entity, LayerType type)
  {
    ValueQuery vQuery = new ValueQuery(new QueryFactory());
    LocatedInQuery liQuery = new LocatedInQuery(vQuery);
    GeoEntityQuery query = new GeoEntityQuery(vQuery);

    SelectableSingle label = query.getDisplayLabel().localize(GeoEntity.DISPLAYLABEL);
    label.setColumnAlias(GeoEntity.DISPLAYLABEL);

    Selectable geoId = query.getGeoId(GeoEntity.GEOID);
    geoId.setColumnAlias(GeoEntity.GEOID);

    Selectable geom = ( type.equals(LayerType.POINT) ? query.get(GeoEntity.GEOPOINT) : query.get(GeoEntity.GEOMULTIPOLYGON) );
    geom.setColumnAlias(GeoserverFacade.GEOM_COLUMN);
    geom.setUserDefinedAlias(GeoserverFacade.GEOM_COLUMN);

    vQuery.SELECT(label, geoId, geom);
    vQuery.WHERE(liQuery.parentId().EQ(entity.getId()));
    vQuery.AND(query.locatedIn(liQuery));
    vQuery.AND(query.getUniversal().EQ(this.universalId));

    return vQuery;
  }

  private LayerType getChildLayerType(List<Term> descendants)
  {
    return descendants.size() <= 1 ? LayerType.POINT : LayerType.POLYGON;
  }

  private LayerType getEntityLayerType(List<Term> descendants)
  {
    return descendants.size() == 0 ? LayerType.POINT : LayerType.POLYGON;
  }

  private String getStyle(LayerType layerType)
  {
    return ( layerType.equals(LayerType.POINT) ? "point" : "polygon" );
  }
}
