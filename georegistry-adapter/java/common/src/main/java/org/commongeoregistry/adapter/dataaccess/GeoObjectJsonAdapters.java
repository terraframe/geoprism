/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Common Geo Registry Adapter(tm).
 *
 * Common Geo Registry Adapter(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Common Geo Registry Adapter(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Common Geo Registry Adapter(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package org.commongeoregistry.adapter.dataaccess;

import java.lang.reflect.Type;
import java.util.Map;

import org.commongeoregistry.adapter.RegistryAdapter;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.metadata.CustomSerializer;
import org.commongeoregistry.adapter.metadata.DefaultSerializer;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.locationtech.jts.io.geojson.GeoJsonWriter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GeoObjectJsonAdapters
{
  public static final String JSON_PROPERTIES = "properties";

  public static final String JSON_TYPE       = "type";

  public static final String JSON_GEOMETRY   = "geometry";

  public static final String JSON_FEATURE    = "Feature";

  public static class GeoObjectDeserializer implements JsonDeserializer<GeoObject>
  {
    private RegistryAdapter registry;

    public GeoObjectDeserializer(RegistryAdapter registry)
    {
      this.registry = registry;
    }
    
    public static String getCode(String goJson)
    {
      JsonObject jo = JsonParser.parseString(goJson).getAsJsonObject();
      
      return getTypeCode(jo);
    }
    
    public static String getTypeCode(String goJson)
    {
      return getTypeCode(JsonParser.parseString(goJson).getAsJsonObject());
    }

    public static String getTypeCode(JsonObject jo)
    {
      JsonObject properties = jo.get(GeoObjectJsonAdapters.JSON_PROPERTIES).getAsJsonObject();
      
      return properties.get(GeoObjectJsonAdapters.JSON_TYPE).getAsString();
    }

    @Override
    public GeoObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
      JsonObject oJson = json.getAsJsonObject();
      JsonObject oJsonProps = oJson.getAsJsonObject(JSON_PROPERTIES);

      GeoObject geoObj;
      if (oJsonProps.has("uid"))
      {
        geoObj = registry.newGeoObjectInstance(oJsonProps.get(JSON_TYPE).getAsString(), false);
      }
      else
      {
        geoObj = registry.newGeoObjectInstance(oJsonProps.get(JSON_TYPE).getAsString(), true);
      }

      JsonElement oGeom = oJson.get(JSON_GEOMETRY);
      if (oGeom != null)
      {
        GeoJsonReader reader = new GeoJsonReader();
        Geometry jtsGeom;
        try
        {
          jtsGeom = reader.read(oGeom.toString());
        }
        catch (ParseException e)
        {
          throw new RuntimeException(e);
        }

        geoObj.setGeometry(jtsGeom);
      }

      for (String key : geoObj.attributeMap.keySet())
      {
        Attribute attr = geoObj.attributeMap.get(key);

        if (oJsonProps.has(key) && !oJsonProps.get(key).isJsonNull())
        {
          attr.fromJSON(oJsonProps.get(key), registry);
        }
      }

      return geoObj;
    }
  }

  public static class BasicGeoObjectDeserializer implements JsonDeserializer<GeoObject>
  {
    private GeoObjectType type;
    
    public BasicGeoObjectDeserializer(GeoObjectType type)
    {
      this.type = type;
    }
    
    @Override
    public GeoObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
      JsonObject oJson = json.getAsJsonObject();
      JsonObject oJsonProps = oJson.getAsJsonObject(JSON_PROPERTIES);
      
      Map<String, Attribute> attributeMap = GeoObject.buildAttributeMap(this.type);
      
      GeoObject geoObj = new GeoObject(this.type, this.type.getGeometryType(), attributeMap);

      geoObj.getAttribute(DefaultAttribute.EXISTS.getName()).setValue(true);
      
      geoObj.getAttribute(DefaultAttribute.INVALID.getName()).setValue(false);

      JsonElement oGeom = oJson.get(JSON_GEOMETRY);
      if (oGeom != null)
      {
        GeoJsonReader reader = new GeoJsonReader();
        Geometry jtsGeom;
        try
        {
          jtsGeom = reader.read(oGeom.toString());
        }
        catch (ParseException e)
        {
          throw new RuntimeException(e);
        }
        
        geoObj.setGeometry(jtsGeom);
      }
      
      for (String key : geoObj.attributeMap.keySet())
      {
        Attribute attr = geoObj.attributeMap.get(key);
        
        if (oJsonProps.has(key) && !oJsonProps.get(key).isJsonNull())
        {
          attr.fromJSON(oJsonProps.get(key), null);
        }
      }
      
      return geoObj;
    }
  }
  
  public static class GeoObjectSerializer implements JsonSerializer<GeoObject>
  {
    private CustomSerializer serializer;

    public GeoObjectSerializer(CustomSerializer serializer)
    {
      this.serializer = serializer;
    }

    public GeoObjectSerializer()
    {
      this.serializer = new DefaultSerializer();
    }

    @Override
    public JsonElement serialize(GeoObject go, Type typeOfSrc, JsonSerializationContext context)
    {
      JsonObject jsonObj = new JsonObject();

      // It's assumed that GeoObjects are simple features rather than
      // FeatureCollections.
      // Spec reference: https://tools.ietf.org/html/rfc7946#section-3.3
      jsonObj.addProperty(JSON_TYPE, JSON_FEATURE);

      if (go.getGeometry() != null)
      {
        GeoJsonWriter gw = new GeoJsonWriter();
        String json = gw.write(go.getGeometry());

        JsonObject geomObj = JsonParser.parseString(json).getAsJsonObject();

        jsonObj.add(JSON_GEOMETRY, geomObj);
      }

      JsonObject props = new JsonObject();
      for (String key : go.attributeMap.keySet())
      {
        Attribute attr = go.attributeMap.get(key);

        JsonElement value = attr.toJSON(serializer);
        if (value != null && !value.isJsonNull())
        {
          props.add(attr.getName(), value);
        }

        // if(attr instanceof AttributeTerm)
        // {
        // attrs.add(key, attr.toJSON());
        // }
        // else
        // {
        //
        // System.out.println(attr.toJSON());
        //
        // // TODO: All these attributes are required by the CGR spec. Adding an
        // // empty string is a temporary step for me to work on another area of
        // // the adapter. Ensure that Values are always present and handle
        // // NULLs as errors.
        // if(attr.getValue() == null )
        // {
        // attrs.addProperty(key, "");
        // }
        // else
        // {
        // attrs.addProperty(key, attr.getValue().toString() );
        // }
        // }

        // JsonParser attrParser = new JsonParser();
        // JsonObject geomObj =
        // attrParser.parse(attr.toJSON().toString()).getAsJsonObject();

      }

      if (go.getWritable() != null)
      {
        props.addProperty("writable", go.getWritable());

      }

      jsonObj.add(JSON_PROPERTIES, props);

      return jsonObj;
    }
  }

}
