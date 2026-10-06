/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.spring.core;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;

public class JsonObjectDeserializer extends StdDeserializer<JsonObject>
{
  public JsonObjectDeserializer()
  {
    super(JsonObject.class);
  }

  @Override
  public JsonObject deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException
  {
    JsonNode node = p.readValueAsTree();
    String text = node.toPrettyString();

    if (!StringUtils.isEmpty(text))
    {
      JsonElement element = com.google.gson.JsonParser.parseString(text.toString());
      return element.getAsJsonObject();
    }

    return null;
  }
}