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
package org.commongeoregistry.adapter.serialization;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;

public class LocalizedValueDeserializer extends StdDeserializer<LocalizedValue>
{
  public LocalizedValueDeserializer()
  {
    super(LocalizedValue.class);
  }

  @Override
  public LocalizedValue deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException
  {
    JsonNode node = p.readValueAsTree();
    String text = node.toPrettyString();

    if (text != null && !text.isEmpty())
    {
      return LocalizedValue.fromJSON(com.google.gson.JsonParser.parseString(text).getAsJsonObject());
    }

    return null;
  }
}