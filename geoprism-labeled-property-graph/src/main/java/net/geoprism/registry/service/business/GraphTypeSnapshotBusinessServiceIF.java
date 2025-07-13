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

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

import net.geoprism.graph.GeoObjectTypeSnapshot;
import net.geoprism.graph.GraphTypeSnapshot;
import net.geoprism.registry.model.SnapshotContainer;

@Component
public interface GraphTypeSnapshotBusinessServiceIF
{

  void delete(GraphTypeSnapshot snapshot);

  String getTableName(String className);

  GraphTypeSnapshot create(SnapshotContainer<?> version, JsonObject type, GeoObjectTypeSnapshot root);

  GraphTypeSnapshot get(SnapshotContainer<?> version, String typeCode, String code);

}
