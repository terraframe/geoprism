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
package net.geoprism.registry.lpg;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TreeStrategyConfiguration implements StrategyConfiguration
{

  private String code;

  private String typeCode;

  public TreeStrategyConfiguration()
  {
  }

  public TreeStrategyConfiguration(String code, String typeCode)
  {
    this.code = code;
    this.typeCode = typeCode;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
    this.code = code;
  }

  public String getTypeCode()
  {
    return typeCode;
  }

  public void setTypeCode(String typeCode)
  {
    this.typeCode = typeCode;
  }

  @Override
  public JsonElement toJson()
  {
    JsonObject object = new JsonObject();
    object.addProperty("code", code);
    object.addProperty("typeCode", typeCode);

    return object;
  }
  
  public static TreeStrategyConfiguration parse(String jsonString)
  {
    return parse(JsonParser.parseString(jsonString));
  }

  public static TreeStrategyConfiguration parse(JsonElement element)
  {
    JsonObject object = element.getAsJsonObject();
    String typeCode = object.get("typeCode").getAsString();
    String code = object.get("code").getAsString();

    return new TreeStrategyConfiguration(code, typeCode);
  }

}
