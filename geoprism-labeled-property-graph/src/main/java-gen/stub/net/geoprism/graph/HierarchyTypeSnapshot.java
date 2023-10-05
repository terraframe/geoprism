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
import com.runwaysdk.query.OIterator;

import net.geoprism.registry.conversion.AttributeTypeConverter;

public class HierarchyTypeSnapshot extends HierarchyTypeSnapshotBase
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 2091038362;

  public HierarchyTypeSnapshot()
  {
    super();
  }

  public JsonObject toJSON(GeoObjectTypeSnapshot root)
  {
    JsonArray nodes = new JsonArray();

    try (OIterator<? extends GeoObjectTypeSnapshot> it = root.getAllChildSnapshot())
    {
      it.forEach(snapshot -> {
        nodes.add(this.toNode(snapshot));
      });
    }

    JsonObject hierarchyObject = new JsonObject();
    hierarchyObject.addProperty(CODE, this.getCode());
    hierarchyObject.add(DISPLAYLABEL, AttributeTypeConverter.convertNoAutoCoalesce(this.getDisplayLabel()).toJSON());
    hierarchyObject.add(DESCRIPTION, AttributeTypeConverter.convertNoAutoCoalesce(this.getDescription()).toJSON());
    hierarchyObject.add("nodes", nodes);

    return hierarchyObject;
  }

  private JsonObject toNode(GeoObjectTypeSnapshot snapshot)
  {
    JsonArray children = new JsonArray();

    try (OIterator<? extends GeoObjectTypeSnapshot> it = snapshot.getAllChildSnapshot())
    {
      it.forEach(child -> {
        children.add(this.toNode(child));
      });
    }

    JsonObject node = new JsonObject();
    node.addProperty(GeoObjectTypeSnapshot.CODE, snapshot.getCode());
    node.add("nodes", children);

    return node;
  }

}
