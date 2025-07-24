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
package net.geoprism.registry.service.business;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

import net.geoprism.graph.GeoObjectTypeSnapshot;
import net.geoprism.graph.HierarchyTypeSnapshot;
import net.geoprism.registry.model.SnapshotContainer;

@Component
public interface HierarchyTypeSnapshotBusinessServiceIF
{

  void delete(HierarchyTypeSnapshot snapshot);

  String getTableName(String className);

  HierarchyTypeSnapshot create(SnapshotContainer<?> version, JsonObject type, GeoObjectTypeSnapshot root);

  HierarchyTypeSnapshot get(SnapshotContainer<?> version, String code);

  List<GeoObjectTypeSnapshot> getChildren(HierarchyTypeSnapshot hierarchy, GeoObjectTypeSnapshot parent);

  void createHierarchyRelationship(HierarchyTypeSnapshot hierarchy, GeoObjectTypeSnapshot parent, GeoObjectTypeSnapshot child);

  JsonObject toJSON(HierarchyTypeSnapshot hierarchy, GeoObjectTypeSnapshot root);

}
