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
package org.commongeoregistry.adapter.metadata;

import com.google.gson.JsonObject;

public class CodeReference
{
  private String code;

  private String type;

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
    this.code = code;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public JsonObject toJSON()
  {
    JsonObject object = new JsonObject();
    object.addProperty("code", this.code);
    object.addProperty("type", this.type);

    return object;
  }

  public CodeReference fromJSON(JsonObject object)
  {
    this.setCode(object.get("code").getAsString());
    this.setType(object.get("type").getAsString());

    return this;
  }

  public static CodeReference build(String code, String type)
  {
    CodeReference reference = new CodeReference();
    reference.setCode(code);
    reference.setType(type);

    return reference;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof CodeReference)
    {
      return this.type.equals( ( (CodeReference) obj ).type) && this.code.equals( ( (CodeReference) obj ).code);
    }

    return super.equals(obj);
  }

}
