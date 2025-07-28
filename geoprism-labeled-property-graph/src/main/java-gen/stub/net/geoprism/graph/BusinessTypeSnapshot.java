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
package net.geoprism.graph;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.geoprism.registry.conversion.LocalizedValueConverter;

public class BusinessTypeSnapshot extends BusinessTypeSnapshotBase
{
  public static final String TABLE_PACKAGE    = "net.geoprism.lpg";

  public static final String PREFIX           = "b_";

  public static final String SPLIT            = "__";

  @SuppressWarnings("unused")
  private static final long  serialVersionUID = -1232639915;

  public BusinessTypeSnapshot()
  {
    super();
  }
  
  @Override
  public String getKey()
  {
    return super.getCode();
  }

  @Override
  public String toString()
  {
    return this.getCode();
  }

  public JsonObject toJSON()
  {
    JsonArray attributes = new JsonArray();

    this.getAttributeTypes().stream().forEach(attribute -> attributes.add(attribute.toJSON()));

    JsonObject typeObject = new JsonObject();
    typeObject.addProperty(CODE, this.getCode());
    typeObject.addProperty(ORGCODE, this.getOrgCode());
    typeObject.addProperty(ORIGIN, this.getOrigin());
    typeObject.addProperty(ORGCODE, this.getOrgCode());
    typeObject.addProperty(SEQUENCE, this.getSequence());
    typeObject.addProperty(LABELATTRIBUTE, this.getLabelAttribute());
    typeObject.add(DISPLAYLABEL, LocalizedValueConverter.convertNoAutoCoalesce(this.getDisplayLabel()).toJSON());
    typeObject.add("attributes", attributes);

    return typeObject;
  }

}
