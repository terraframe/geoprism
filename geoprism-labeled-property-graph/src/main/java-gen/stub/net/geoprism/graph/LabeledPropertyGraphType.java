/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.graph;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runwaysdk.Pair;
import com.runwaysdk.session.Session;

import net.geoprism.configuration.GeoprismProperties;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.lpg.LabeledVersion;
import net.geoprism.registry.lpg.LocaleSerializer;
import net.geoprism.registry.lpg.StrategyConfiguration;
import net.geoprism.registry.lpg.TreeStrategyConfiguration;

public abstract class LabeledPropertyGraphType extends LabeledPropertyGraphTypeBase
{
  private static final long  serialVersionUID = 190790165;

  public static final String GRAPH_TYPE       = "graphType";

  public static final String SINGLE           = "single";

  public static final String INTERVAL         = "interval";

  public static final String INCREMENTAL      = "incremental";

  public static final String TREE             = "TREE";
  
  public static final String GRAPH            = "GRAPH";
  
  public static String GRAPH_TYPE_REFERENECE_SEPARATOR = "$@~";
  
  public LabeledPropertyGraphType()
  {
    super();
  }

  public abstract JsonObject formatVersionLabel(LabeledVersion version);

  public abstract List<Date> getEntryDates();

  public void setStrategyConfiguration(StrategyConfiguration configuration)
  {
    if (configuration == null)
    {
      super.setStrategyConfiguration(null);
    }
    else
    {
      super.setStrategyConfiguration(configuration.toJson().toString());
    }
  }

  public StrategyConfiguration toStrategyConfiguration()
  {
    if (this.getStrategyType().equals(TREE))
    {
      return TreeStrategyConfiguration.parse(this.getStrategyConfiguration());
    }
    else if (this.getStrategyType().equals(GRAPH))
    {
      return null;
    }

    throw new UnsupportedOperationException();
  }

  public File getShapefileDirectory()
  {
    final File root = GeoprismProperties.getGeoprismFileStorage();
    final File directory = new File(root, "shapefiles");

    return new File(directory, this.getOid());
  }

  public JsonElement getStrategyConfigurationAsJson()
  {
    if (this.getStrategyConfiguration() != null && this.getStrategyConfiguration().length() > 0)
    {
      return JsonParser.parseString(this.getStrategyConfiguration());
    }

    return new JsonObject();
  }
  
  public List<String> getGeoObjectTypeCodesList()
  {
    List<String> result = new ArrayList<String>();
    
    if (StringUtils.isEmpty(this.getGeoObjectTypeCodes())) return result;
    
    JsonArray jaCodes = JsonParser.parseString(this.getGeoObjectTypeCodes()).getAsJsonArray();
    
    jaCodes.forEach(je -> result.add(je.getAsString()));
    
    return result;
  }
  
  public void setGeoObjectTypeCodesList(String... codes)
  {
    JsonArray jaCodes = new JsonArray();
    
    if (codes != null)
    {
      Arrays.asList(codes).forEach(s -> jaCodes.add(s));
    }
    
    this.setGeoObjectTypeCodes(jaCodes.toString());
  }
  
  public List<GraphTypeReference> getGraphTypeReferences()
  {
    List<GraphTypeReference> result = new ArrayList<GraphTypeReference>();
    
    if (StringUtils.isEmpty(this.getGraphTypes())) return result;
    
    JsonArray jaCodes = JsonParser.parseString(this.getGraphTypes()).getAsJsonArray();
    
    jaCodes.forEach(je -> result.add(GraphTypeReference.build(je.getAsString().split("\\" + GRAPH_TYPE_REFERENECE_SEPARATOR))));
    
    return result;
  }
  
  public void setGraphTypeReferences(GraphTypeReference... graphTypeReferences)
  {
    JsonArray jaCodes = new JsonArray();
    
    if (graphTypeReferences != null)
    {
      Arrays.asList(graphTypeReferences).forEach(r -> jaCodes.add(r.typeCode + GRAPH_TYPE_REFERENECE_SEPARATOR + r.code));
    }
    
    this.setGraphTypes(jaCodes.toString());
  }

  public List<Pair<String, Integer>> getParentCodes(JsonObject hierarchy)
  {
    List<Pair<String, Integer>> list = new LinkedList<Pair<String, Integer>>();

    JsonArray parents = hierarchy.get("parents").getAsJsonArray();

    for (int i = 0; i < parents.size(); i++)
    {
      JsonObject parent = parents.get(i).getAsJsonObject();

      if (parent.has("selected") && parent.get("selected").getAsBoolean())
      {
        list.add(new Pair<String, Integer>(parent.get("code").getAsString(), Integer.valueOf(i + 1)));
      }
    }

    return list;
  }

  public void parse(JsonObject object)
  {
    LocalizedValueConverter.populate(this.getDisplayLabel(), LocalizedValue.fromJSON(object.get(LabeledPropertyGraphType.DISPLAYLABEL).getAsJsonObject()));
    LocalizedValueConverter.populate(this.getDescription(), LocalizedValue.fromJSON(object.get(LabeledPropertyGraphType.DESCRIPTION).getAsJsonObject()));
    this.setCode(object.get(LabeledPropertyGraphType.CODE).getAsString());
    this.setHierarchy(object.get(LabeledPropertyGraphType.HIERARCHY).getAsString());
    
    if (object.has(STRATEGYCONFIGURATION))
    {
      this.setStrategyConfiguration(object.get(STRATEGYCONFIGURATION).toString());
    }

    this.setStrategyType(jsonRead(object, STRATEGYTYPE));
    this.setGraphTypes(jsonRead(object, GRAPHTYPES));
    this.setGeoObjectTypeCodes(jsonRead(object, GEOOBJECTTYPECODES));
  }
  
  private String jsonRead(JsonObject ele, String property)
  {
    if (ele.has(property))
    {
      return ele.get(property).getAsString();
    }
    
    return null;
  }

  public JsonObject toJSON()
  {
    Locale locale = Session.getCurrentLocale();
    LocaleSerializer serializer = new LocaleSerializer(locale);

    JsonObject object = new JsonObject();

    if (this.isAppliedToDB())
    {
      object.addProperty(LabeledPropertyGraphType.OID, this.getOid());
    }

    object.add(LabeledPropertyGraphType.DISPLAYLABEL, LocalizedValueConverter.convertNoAutoCoalesce(this.getDisplayLabel()).toJSON(serializer));
    object.add(LabeledPropertyGraphType.DESCRIPTION, LocalizedValueConverter.convertNoAutoCoalesce(this.getDescription()).toJSON(serializer));
    object.addProperty(LabeledPropertyGraphType.CODE, this.getCode());
    object.addProperty(LabeledPropertyGraphType.HIERARCHY, this.getHierarchy());
    object.addProperty(LabeledPropertyGraphType.GEOOBJECTTYPECODES, this.getGeoObjectTypeCodes());
    object.addProperty(LabeledPropertyGraphType.GRAPHTYPES, this.getGraphTypes());
    object.addProperty(LabeledPropertyGraphType.STRATEGYTYPE, this.getStrategyType());
    object.add(LabeledPropertyGraphType.STRATEGYCONFIGURATION, this.getStrategyConfigurationAsJson());
    object.addProperty(LabeledPropertyGraphType.ORGANIZATION, this.getOrganization().getCode());

    return object;
  }

  public boolean isValid()
  {
    return ( this.getValid() == null || this.getValid() );
  }
}
