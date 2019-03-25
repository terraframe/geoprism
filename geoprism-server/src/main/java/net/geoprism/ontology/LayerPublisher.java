/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.ontology;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.geotools.geojson.geom.GeometryJSON;
import org.hsqldb.lib.StringInputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;
import org.postgis.jts.JtsGeometry;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.dataaccess.AttributeGeometryIF;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityDisplayLabel;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.GeometryType;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.wdtinc.mapbox_vector_tile.VectorTile;
import com.wdtinc.mapbox_vector_tile.VectorTile.Tile;
import com.wdtinc.mapbox_vector_tile.adapt.jts.IGeometryFilter;
import com.wdtinc.mapbox_vector_tile.adapt.jts.IUserDataConverter;
import com.wdtinc.mapbox_vector_tile.adapt.jts.JtsAdapter;
import com.wdtinc.mapbox_vector_tile.adapt.jts.TileGeomResult;
import com.wdtinc.mapbox_vector_tile.build.MvtLayerBuild;
import com.wdtinc.mapbox_vector_tile.build.MvtLayerParams;
import com.wdtinc.mapbox_vector_tile.build.MvtLayerProps;

import net.geoprism.JSONStringImpl;
import net.geoprism.data.DatabaseUtil;
import net.geoprism.gis.geoserver.GeoserverFacade;
import net.geoprism.gis.geoserver.GeoserverLayerIF;

public abstract class LayerPublisher
{
  public static enum LayerType {
    POINT, POLYGON
  }

  private String              layers;

  private GeometryType        geometryType;

  private GeometryJSON        gjson;

  private MdRelationshipDAOIF mdRelationship;

  public LayerPublisher(MdRelationshipDAOIF mdRelationship, String layers, GeometryType geometryType)
  {
    this.mdRelationship = mdRelationship;
    this.layers = layers;
    this.geometryType = geometryType;
    this.gjson = new GeometryJSON(8);
  }

  public MdRelationshipDAOIF getMdRelationship()
  {
    return mdRelationship;
  }

  public GeometryType getGeometryType()
  {
    return geometryType;
  }

  protected String getLabelColumn()
  {
    String labelColumn = "default_locale";

//    Locale locale = Session.getCurrentLocale();
//
//    if (locale != null)
//    {
//      MdStructDAOIF mdStruct = MdStructDAO.getMdStructDAO(GeoEntityDisplayLabel.CLASS);
//
//      if (mdStruct.definesAttribute(locale.toString()) != null)
//      {
//        labelColumn = locale.toString().toLowerCase();
//      }
//    }

    return labelColumn;
  }

  protected String getGeometryColumn()
  {
    if (this.geometryType.equals(GeometryType.POINT))
    {
      return "geo_point";
    }
    else if (this.geometryType.equals(GeometryType.MULTIPOINT))
    {
      return "geo_multi_point";
    }
    else if (this.geometryType.equals(GeometryType.LINE))
    {
      return "geo_line";
    }
    else if (this.geometryType.equals(GeometryType.MULTILINE))
    {
      return "geo_multi_line";
    }
    else if (this.geometryType.equals(GeometryType.POLYGON))
    {
      return "geo_polygon";
    }

    return "geo_multi_polygon";
  }

  protected Selectable getGeometrySelectable(GeoEntityQuery query)
  {
    if (this.geometryType.equals(GeometryType.POINT))
    {
      return query.get(GeoEntity.GEOPOINT);
    }
    else if (this.geometryType.equals(GeometryType.MULTIPOINT))
    {
      return query.get(GeoEntity.GEOMULTIPOINT);
    }
    else if (this.geometryType.equals(GeometryType.LINE))
    {
      return query.get(GeoEntity.GEOLINE);
    }
    else if (this.geometryType.equals(GeometryType.MULTILINE))
    {
      return query.get(GeoEntity.GEOMULTILINE);
    }
    else if (this.geometryType.equals(GeometryType.POLYGON))
    {
      return query.get(GeoEntity.GEOPOLYGON);
    }

    return query.get(GeoEntity.GEOMULTIPOLYGON);
  }

  protected void removeGeoserverLayers() throws JSONException
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
  }

  protected void removeDatabaseViews() throws JSONException
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
  }

  protected String getStyle(LayerType layerType)
  {
    return ( layerType.equals(LayerType.POINT) ? "point" : "polygon" );
  }

  public JSONArray publish()
  {
    try
    {
      /*
       * Create new Database views
       */
      List<GeoserverLayerIF> layers = this.createDatabaseViews();

      /*
       * Remove old geoserver layers
       */
      this.removeGeoserverLayers();

      /*
       * Publish new layers to geoserver
       */
      JSONArray serialized = new JSONArray();

      for (GeoserverLayerIF layer : layers)
      {
        GeoserverFacade.publishLayer(layer);

        serialized.put(layer.toJSON());
      }

      return serialized;
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Transaction
  private List<GeoserverLayerIF> createDatabaseViews() throws JSONException
  {
    this.removeDatabaseViews();

    return this.buildLayers();
  }

  protected abstract List<GeoserverLayerIF> buildLayers();

  protected JSONObject getProperties(ValueObject object) throws JSONException
  {
    AttributeIF[] attributes = object.getAttributeArrayIF();

    JSONObject properties = new JSONObject();

    for (AttributeIF attribute : attributes)
    {
      String name = attribute.getName();

      if (!name.equals(GeoserverFacade.GEOM_COLUMN))
      {
        properties.put(name, attribute.getValue());
      }
    }

    return properties;
  }

  protected void writeGeometry(ValueObject object, JSONWriter writer) throws IOException, JSONException
  {
    StringWriter geomWriter = new StringWriter();

    AttributeGeometryIF attributeIF = (AttributeGeometryIF) object.getAttributeIF(GeoserverFacade.GEOM_COLUMN);

    this.gjson.write(attributeIF.getGeometry(), geomWriter);

    writer.value(new JSONStringImpl(geomWriter.toString()));
  }

  protected long writeFeatures(JSONWriter writer, ValueQuery query) throws IOException
  {
    long count = 0;

    OIterator<ValueObject> iterator = query.getIterator();

    try
    {

      while (iterator.hasNext())
      {
        count++;

        ValueObject object = iterator.next();

        JSONObject properties = this.getProperties(object);

        writer.object();

        writer.key("type");
        writer.value("Feature");

        writer.key("properties");
        writer.value(properties);

        writer.key("oid");
        writer.value(object.getValue(GeoEntity.OID));

        writer.key("geometry");
        this.writeGeometry(object, writer);

        writer.endObject();
      }
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }
    finally
    {
      iterator.close();
    }

    return count;
  }

  protected void writeCRS(JSONWriter jw)
  {
    jw.key("crs");
    jw.value(new JSONObject("{\"type\":\"name\",\"properties\":{\"name\":\"urn:ogc:def:crs:EPSG::4326\"}}"));
  }

  public abstract void writeGeojson(JSONWriter writer);

  protected Map<String, String> getUserData(ValueObject object)
  {
    AttributeIF[] attributes = object.getAttributeArrayIF();

    Map<String, String> data = new TreeMap<String, String>();

    for (AttributeIF attribute : attributes)
    {
      String name = attribute.getName();

      if (!name.equals(GeoserverFacade.GEOM_COLUMN))
      {
        data.put(name, attribute.getValue());
      }
    }

    return data;
  }

  protected byte[] writeVectorTiles(String layerName, Envelope envelope, ValueQuery query) throws IOException
  {
    OIterator<ValueObject> iterator = query.getIterator();

    try
    {
      List<Geometry> geometries = new LinkedList<Geometry>();

      while (iterator.hasNext())
      {
        ValueObject object = iterator.next();

        AttributeGeometryIF attributeIF = (AttributeGeometryIF) object.getAttributeIF(GeoserverFacade.GEOM_COLUMN);

        Geometry geometry = attributeIF.getGeometry();
        geometry.setUserData(this.getUserData(object));

        geometries.add(geometry);
      }

      GeometryFactory geomFactory = new GeometryFactory();
      IGeometryFilter acceptAllGeomFilter = geometry -> true;

      MvtLayerParams layerParams = new MvtLayerParams();

      TileGeomResult tileGeom = JtsAdapter.createTileGeom(geometries, envelope, geomFactory, layerParams, acceptAllGeomFilter);

      final VectorTile.Tile.Builder tileBuilder = VectorTile.Tile.newBuilder();

      // Create MVT layer
      final MvtLayerProps layerProps = new MvtLayerProps();
      final IUserDataConverter ignoreUserData = new UserDataConverter();

      // MVT tile geometry to MVT features
      final List<VectorTile.Tile.Feature> features = JtsAdapter.toFeatures(tileGeom.mvtGeoms, layerProps, ignoreUserData);

      final VectorTile.Tile.Layer.Builder layerBuilder = MvtLayerBuild.newLayerBuilder(layerName, layerParams);
      layerBuilder.addAllFeatures(features);

      MvtLayerBuild.writeProps(layerBuilder, layerProps);

      // Build MVT layer
      final VectorTile.Tile.Layer layer = layerBuilder.build();

      // Add built layer to MVT
      tileBuilder.addLayers(layer);

      /// Build MVT
      Tile mvt = tileBuilder.build();

      return mvt.toByteArray();
    }
    finally
    {
      iterator.close();
    }
  }

  protected byte[] writeVectorTiles(String layerName, Envelope envelope, ResultSet resultSet) throws IOException
  {
    // Add built layer to MVT
    final VectorTile.Tile.Builder builder = VectorTile.Tile.newBuilder();

    builder.addLayers(this.writeVectorLayer(layerName, envelope, resultSet));

    /// Build MVT
    Tile mvt = builder.build();

    return mvt.toByteArray();
  }

  public VectorTile.Tile.Layer writeVectorLayer(String layerName, Envelope bounds, ResultSet resultSet) throws IOException
  {
    try
    {
      List<Geometry> geometries = new LinkedList<Geometry>();

      while (resultSet.next())
      {
        String label = resultSet.getString("default_locale");
        
        Map<String, String> data = new TreeMap<String, String>();
        data.put(GeoEntity.OID, resultSet.getString("oid"));
        data.put(GeoEntity.DISPLAYLABEL, label);
        data.put(GeoEntity.GEOID, resultSet.getString("geo_id"));
        data.put("height", "15"); // TODO: This should be set on the GeoEntity
        data.put("base", "0"); // TODO: This should be set on the GeoEntity
        data.put("isClickable", "true");

        JtsGeometry geom = (JtsGeometry) resultSet.getObject(GeoserverFacade.GEOM_COLUMN);

        if (geom != null)
        {
          Geometry geometry = geom.getGeometry();
          geometry.setUserData(data);

          geometries.add(geometry);
        }
      }

      GeometryFactory geomFactory = new GeometryFactory();
      IGeometryFilter acceptAllGeomFilter = geometry -> true;

      MvtLayerParams layerParams = new MvtLayerParams();

      TileGeomResult tileGeom = JtsAdapter.createTileGeom(geometries, bounds, geomFactory, layerParams, acceptAllGeomFilter);

      // Create MVT layer
      final MvtLayerProps layerProps = new MvtLayerProps();
      final IUserDataConverter ignoreUserData = new UserDataConverter();

      // MVT tile geometry to MVT features
      final List<VectorTile.Tile.Feature> features = JtsAdapter.toFeatures(tileGeom.mvtGeoms, layerProps, ignoreUserData);

      final VectorTile.Tile.Layer.Builder layerBuilder = MvtLayerBuild.newLayerBuilder(layerName, layerParams);
      layerBuilder.addAllFeatures(features);

      MvtLayerBuild.writeProps(layerBuilder, layerProps);

      // Build MVT layer
      return layerBuilder.build();
    }
    catch (SQLException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }
}
