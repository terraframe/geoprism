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
package net.geoprism.registry;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collector;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class JsonCollectors
{
  public static Collector<JsonElement, ?, JsonArray> toJsonArray()
  {
    return Collector.of(new Supplier<JsonArray>()
    {

      @Override
      public JsonArray get()
      {
        return new JsonArray();
      }

    }, new BiConsumer<JsonArray, JsonElement>()
    {

      @Override
      public void accept(JsonArray array, JsonElement element)
      {
        array.add(element);
      }
    }, new BinaryOperator<JsonArray>()
    {
      @Override
      public JsonArray apply(JsonArray arg0, JsonArray arg1)
      {
        arg0.addAll(arg1);

        return arg0;
      }
    });

  }
}
