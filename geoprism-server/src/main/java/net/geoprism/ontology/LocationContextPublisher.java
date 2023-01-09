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
package net.geoprism.ontology;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONWriter;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableSingle;
import com.runwaysdk.query.SelectableUUID;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.GeometryType;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.mapping.GeoserverFacade;
import org.locationtech.jts.geom.Envelope;
import com.wdtinc.mapbox_vector_tile.VectorTile;
import com.wdtinc.mapbox_vector_tile.VectorTile.Tile;
import com.wdtinc.mapbox_vector_tile.VectorTile.Tile.Layer;

import net.geoprism.data.DatabaseUtil;
import net.geoprism.gis.geoserver.GeoserverLayer;
import net.geoprism.gis.geoserver.GeoserverLayerIF;
import net.geoprism.gis.geoserver.SessionPredicate;

public class LocationContextPublisher extends LayerPublisher implements VectorLayerPublisherIF
{
  private String oid;

  public LocationContextPublisher(MdRelationshipDAOIF mdRelationship, String oid, String layers, GeometryType geometryType)
  {
    super(mdRelationship, layers, geometryType);

    this.oid = oid;
  }

  @Override
  protected List<GeoserverLayerIF> buildLayers()
  {

    GeoEntity entity = GeoEntity.get(this.oid);
    Universal universal = entity.getUniversal();
    List<Term> descendants = universal.getAllDescendants(AllowedIn.CLASS).getAll();

    List<GeoserverLayerIF> layers = new LinkedList<GeoserverLayerIF>();
    layers.add(this.publishEntityLayer(entity, descendants));

    return layers;
  }

  private GeoserverLayerIF publishEntityLayer(GeoEntity entity, List<Term> descendants)
  {
    LayerType layerType = this.getEntityLayerType(descendants);
    String sql = this.getEntityQuery(entity, layerType).getSQL();
    String viewName = SessionPredicate.generateId();

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

    GeoEntityQuery geQ1 = new GeoEntityQuery(query);

    // Id column
    SelectableUUID oid = geQ1.getOid(GeoEntity.OID);
    oid.setColumnAlias(GeoEntity.OID);

    // geoentity label
    SelectableSingle label = geQ1.getDisplayLabel().localize(GeoEntity.DISPLAYLABEL);
    label.setColumnAlias(GeoEntity.DISPLAYLABEL);

    // geo oid (for uniqueness)
    Selectable geoId = geQ1.getGeoId(GeoEntity.GEOID);
    geoId.setColumnAlias(GeoEntity.GEOID);

    Selectable geom = ( type.equals(LayerType.POINT) ? geQ1.get(GeoEntity.GEOPOINT) : geQ1.get(GeoEntity.GEOMULTIPOLYGON) );
    geom.setColumnAlias(GeoserverFacade.GEOM_COLUMN);
    geom.setUserDefinedAlias(GeoserverFacade.GEOM_COLUMN);

    query.SELECT(oid, label, geoId, geom);
    query.WHERE(geQ1.getOid().EQ(entity.getOid()));

    return query;
  }

  private LayerType getEntityLayerType(List<Term> descendants)
  {
    return LayerType.POLYGON;
  }

  public void writeGeojson(JSONWriter writer)
  {
    GeoEntity entity = GeoEntity.get(this.oid);
    Universal universal = entity.getUniversal();
    List<Term> descendants = universal.getAllDescendants(AllowedIn.CLASS).getAll();

    try
    {
      LayerType entityLayerType = this.getEntityLayerType(descendants);
      ValueQuery entityQuery = this.getEntityQuery(entity, entityLayerType);

      this.writeGeojson(writer, entityQuery);
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

  public String getLayerName()
  {
    return "context";
  }

  private ResultSet getResultSet(String entityId, LayerType type)
  {
    String column = this.getGeometryColumn();

    String labelColumn = getLabelColumn();

    StringBuilder sql = new StringBuilder();
    sql.append("SELECT ge.oid, gdl." + labelColumn + " AS default_locale, ge.geo_id, ST_Transform(ge." + column + ", 3857) AS " + GeoserverFacade.GEOM_COLUMN + "\n");
    sql.append("FROM geo_entity AS ge\n");
    sql.append("JOIN geo_entity_display_label AS gdl ON gdl.oid = ge.display_label\n");
    sql.append("WHERE ge.oid::text = '" + entityId + "'\n");
    sql.append("AND ge." + column + " IS NOT NULL\n");

    return Database.query(sql.toString());
  }

  @Override
  public List<Layer> writeVectorLayers(Envelope envelope, Envelope bounds)
  {
    try
    {
      ResultSet resultSet = this.getResultSet(this.oid, LayerType.POLYGON);

      try
      {
        String layerName = this.getLayerName();

        List<Layer> layers = new LinkedList<Layer>();
        layers.add(this.writeVectorLayer(layerName, bounds, resultSet));

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

  public byte[] writeVectorTiles(Envelope envelope, Envelope bounds)
  {
    // Add built layer to MVT
    final VectorTile.Tile.Builder builder = VectorTile.Tile.newBuilder();

    List<Layer> layers = this.writeVectorLayers(envelope, bounds);

    for (Layer layer : layers)
    {
      builder.addLayers(layer);
    }

    /// Build MVT
    Tile mvt = builder.build();

    return mvt.toByteArray();
  }
}
