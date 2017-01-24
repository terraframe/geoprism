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
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.geotools.geojson.geom.GeometryJSON;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;

import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.dataaccess.AttributeGeometryIF;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.system.gis.geo.GeoEntity;

import net.geoprism.data.DatabaseUtil;
import net.geoprism.gis.geoserver.GeoserverFacade;
import net.geoprism.gis.geoserver.GeoserverLayerIF;

public abstract class LayerPublisher
{
  public static enum LayerType {
    POINT, POLYGON
  }

  private String layers;

  public LayerPublisher(String layers)
  {
    this.layers = layers;
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

  protected JSONObject getGeometry(ValueObject object) throws IOException, JSONException
  {
    StringWriter geomWriter = new StringWriter();

    AttributeGeometryIF attributeIF = (AttributeGeometryIF) object.getAttributeIF(GeoserverFacade.GEOM_COLUMN);

    GeometryJSON gjson = new GeometryJSON();
    gjson.write(attributeIF.getGeometry(), geomWriter);

    return new JSONObject(geomWriter.toString());
  }

  protected long writeFeatures(JSONWriter jw, ValueQuery query) throws IOException
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

        JSONObject geometry = this.getGeometry(object);

        JSONObject feature = new JSONObject();
        feature.put("type", "Feature");
        feature.put("geometry", geometry);
        feature.put("properties", properties);
        feature.put("id", object.getValue(GeoEntity.ID));

        jw.value(feature);
      }
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

  public abstract void writeGeojson(Writer writer);
}
