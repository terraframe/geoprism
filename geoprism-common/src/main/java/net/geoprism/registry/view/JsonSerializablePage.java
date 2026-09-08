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
package net.geoprism.registry.view;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JsonSerializablePage<T extends JsonSerializable> extends Page<T>
{

  public JsonSerializablePage()
  {
    super();
  }

  public JsonSerializablePage(Integer count, Integer pageNumber, Integer pageSize, List<T> results)
  {
    super(count, pageNumber, pageSize, results);
  }

  public JsonSerializablePage(Long count, Integer pageNumber, Integer pageSize, List<T> results)
  {
    super(count, pageNumber, pageSize, results);
  }

  public JsonObject toJSON()
  {
    JsonArray array = new JsonArray();

    for (JsonSerializable result : this.getResultSet())
    {
      array.add(result.toJSON());
    }

    JsonObject object = new JsonObject();
    object.addProperty("count", this.count);
    object.addProperty("pageNumber", this.pageNumber);
    object.addProperty("pageSize", this.pageSize);
    object.add("resultSet", array);

    return object;
  }

}
