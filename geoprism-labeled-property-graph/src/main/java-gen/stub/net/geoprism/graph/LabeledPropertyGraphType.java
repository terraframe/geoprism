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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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

  public LabeledPropertyGraphType()
  {
    super();
  }

  public abstract JsonObject formatVersionLabel(LabeledVersion version);

  public abstract List<Date> getEntryDates();

  public void setStrategyConfiguration(StrategyConfiguration configuration)
  {
    super.setStrategyConfiguration(configuration.toJson().toString());
  }

  public StrategyConfiguration toStrategyConfiguration()
  {
    if (this.getStrategyType().equals(TREE))
    {
      return TreeStrategyConfiguration.parse(this.getStrategyConfiguration());
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

    this.setStrategyType(object.get(LabeledPropertyGraphType.STRATEGYTYPE).getAsString());
    this.setStrategyConfiguration(object.get(LabeledPropertyGraphType.STRATEGYCONFIGURATION).toString());
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
    object.addProperty(LabeledPropertyGraphType.STRATEGYTYPE, this.getStrategyType());
    object.add(LabeledPropertyGraphType.STRATEGYCONFIGURATION, this.getStrategyConfigurationAsJson());

    return object;
  }

  public boolean isValid()
  {
    return ( this.getValid() == null || this.getValid() );
  }

}
