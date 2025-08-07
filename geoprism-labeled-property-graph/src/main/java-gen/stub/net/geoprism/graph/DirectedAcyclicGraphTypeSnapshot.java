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

import com.google.gson.JsonObject;

import net.geoprism.registry.conversion.LocalizedValueConverter;

public class DirectedAcyclicGraphTypeSnapshot extends DirectedAcyclicGraphTypeSnapshotBase implements GraphTypeSnapshot
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1867928283;
  
  public DirectedAcyclicGraphTypeSnapshot()
  {
    super();
  }
  
  @Override
  public String getTypeCode()
  {
    return GraphTypeSnapshot.getTypeCode(this);
  }

  public JsonObject toJSON()
  {
    JsonObject hierarchyObject = new JsonObject();
    hierarchyObject.addProperty(DirectedAcyclicGraphTypeSnapshot.CODE, this.getCode());
    hierarchyObject.addProperty(DirectedAcyclicGraphTypeSnapshot.ORIGIN, this.getOrigin());
    hierarchyObject.addProperty(DirectedAcyclicGraphTypeSnapshot.SEQUENCE, this.getSequence());
    hierarchyObject.addProperty(GraphTypeSnapshot.TYPE_CODE, GraphTypeSnapshot.DIRECTED_ACYCLIC_GRAPH_TYPE);
    hierarchyObject.add(DirectedAcyclicGraphTypeSnapshot.DISPLAYLABEL, LocalizedValueConverter.convertNoAutoCoalesce(this.getDisplayLabel()).toJSON());
    hierarchyObject.add(DirectedAcyclicGraphTypeSnapshot.DESCRIPTION, LocalizedValueConverter.convertNoAutoCoalesce(this.getDescription()).toJSON());

    return hierarchyObject;
  }
}
