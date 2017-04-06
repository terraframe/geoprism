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

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONWriter;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableChar;
import com.runwaysdk.query.SelectableSingle;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.LocatedInQuery;
import com.runwaysdk.system.gis.geo.Universal;
import com.vividsolutions.jts.geom.Envelope;
import com.wdtinc.mapbox_vector_tile.VectorTile;
import com.wdtinc.mapbox_vector_tile.VectorTile.Tile;
import com.wdtinc.mapbox_vector_tile.VectorTile.Tile.Layer;

import net.geoprism.data.DatabaseUtil;
import net.geoprism.gis.geoserver.GeoserverFacade;
import net.geoprism.gis.geoserver.GeoserverLayer;
import net.geoprism.gis.geoserver.GeoserverLayerIF;
import net.geoprism.gis.geoserver.SessionPredicate;

public class LocationTargetPublisher extends LayerPublisher implements VectorLayerPublisherIF
{
  private String id;

  private String universalId;

  public LocationTargetPublisher(String id, String universalId, String layers)
  {
    super(layers);

    this.id = id;
    this.universalId = universalId;
  }

  @Override
  protected List<GeoserverLayerIF> buildLayers()
  {

    GeoEntity entity = GeoEntity.get(this.id);
    Universal universal = entity.getUniversal();
    List<Term> descendants = universal.getAllDescendants(AllowedIn.CLASS).getAll();

    List<GeoserverLayerIF> layers = new LinkedList<GeoserverLayerIF>();
    layers.add(this.publishLayer(entity, descendants));

    return layers;
  }

  private GeoserverLayerIF publishLayer(GeoEntity entity, List<Term> descendants)
  {
    LayerType layerType = this.getLayerType(descendants);
    String sql = this.getQuery(entity, layerType).getSQL();
    String viewName = SessionPredicate.generateId();

    DatabaseUtil.createView(viewName, sql);

    GeoserverLayer layer = new GeoserverLayer();
    layer.setLayerName(viewName);
    layer.setStyleName(this.getStyle(layerType));
    layer.setLayerType(layerType);

    return layer;
  }

  private ValueQuery getQuery(GeoEntity entity, LayerType type)
  {
    ValueQuery vQuery = new ValueQuery(new QueryFactory());
    LocatedInQuery liQuery = new LocatedInQuery(vQuery);
    GeoEntityQuery query = new GeoEntityQuery(vQuery);

    // Id column
    SelectableChar id = query.getId(GeoEntity.ID);
    id.setColumnAlias(GeoEntity.ID);

    SelectableSingle label = query.getDisplayLabel().localize(GeoEntity.DISPLAYLABEL);
    label.setColumnAlias(GeoEntity.DISPLAYLABEL);

    Selectable geoId = query.getGeoId(GeoEntity.GEOID);
    geoId.setColumnAlias(GeoEntity.GEOID);

    Selectable geom = ( type.equals(LayerType.POINT) ? query.get(GeoEntity.GEOPOINT) : query.get(GeoEntity.GEOMULTIPOLYGON) );
    geom.setColumnAlias(GeoserverFacade.GEOM_COLUMN);
    geom.setUserDefinedAlias(GeoserverFacade.GEOM_COLUMN);

    vQuery.SELECT(id, label, geoId, geom);
    vQuery.WHERE(liQuery.parentId().EQ(entity.getId()));
    vQuery.AND(query.locatedIn(liQuery));

    if (this.universalId != null && this.universalId.length() > 0)
    {
      vQuery.AND(query.getUniversal().EQ(this.universalId));
    }

    return vQuery;
  }

  private LayerType getLayerType(List<Term> descendants)
  {
    return LayerType.POLYGON;
  }

  public void writeGeojson(JSONWriter writer)
  {
    GeoEntity entity = GeoEntity.get(this.id);
    Universal universal = entity.getUniversal();
    List<Term> descendants = universal.getAllDescendants(AllowedIn.CLASS).getAll();

    try
    {
      LayerType childLayerType = this.getLayerType(descendants);
      ValueQuery childQuery = this.getQuery(entity, childLayerType);

      this.writeGeojson(writer, childQuery);
    }
    catch (JSONException | IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private void writeGeojson(JSONWriter writer, ValueQuery query) throws IOException
  {
    writer.object();

    writer.key("type");
    writer.value("FeatureCollection");
    writer.key("features");
    writer.array();

    long count = this.writeFeatures(writer, query);

    writer.endArray();

    writer.key("totalFeatures");
    writer.value(count);

    this.writeCRS(writer);

    writer.endObject();
  }

  private ResultSet getResultSet(String entityId, LayerType type)
  {
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT ge.id, gdl.default_locale, ge.geo_id, ST_Transform(ge.geo_multi_polygon, 3857) AS " + GeoserverFacade.GEOM_COLUMN + "\n");
    sql.append("FROM geo_entity AS ge\n");
    sql.append("JOIN geo_entity_display_label AS gdl ON gdl.id = ge.display_label\n");
    sql.append("JOIN located_in AS li ON li.child_id = ge.id\n");
    sql.append("WHERE li.parent_id = '" + entityId + "'\n");
    sql.append("AND ge.geo_multi_polygon IS NOT NULL\n");

    if (this.universalId != null && this.universalId.length() > 0)
    {
      sql.append("AND ge.universal = '" + this.universalId + "'\n");
    }

    return Database.query(sql.toString());
  }

  public String getLayerName()
  {
    return "target";
  }

  @Override
  public List<Layer> writeVectorLayers(Envelope envelope)
  {
    try
    {
      ResultSet resultSet = this.getResultSet(this.id, LayerType.POLYGON);

      try
      {
        String layerName = this.getLayerName();

        List<Layer> layers = new LinkedList<Layer>();
        layers.add(this.writeVectorLayer(layerName, envelope, resultSet));

        return layers;
      }
      finally
      {
        resultSet.close();
      }
    }
    catch (JSONException | IOException | SQLException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public byte[] writeVectorTiles(Envelope envelope)
  {
    // Add built layer to MVT
    final VectorTile.Tile.Builder builder = VectorTile.Tile.newBuilder();

    List<Layer> layers = this.writeVectorLayers(envelope);

    for (Layer layer : layers)
    {
      builder.addLayers(layer);
    }

    /// Build MVT
    Tile mvt = builder.build();

    return mvt.toByteArray();
  }
}
