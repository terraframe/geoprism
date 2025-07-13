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
package net.geoprism.registry.service.business;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdVertexDAOIF;

import net.geoprism.graph.BusinessTypeSnapshot;
import net.geoprism.registry.model.SnapshotContainer;

@Component
public interface BusinessTypeSnapshotBusinessServiceIF extends ObjectTypeBusinessServiceIF<BusinessTypeSnapshot>
{

  void truncate(BusinessTypeSnapshot snapshot);

  void delete(BusinessTypeSnapshot snapshot);

  String getTableName(String className);

  BusinessTypeSnapshot create(SnapshotContainer<?> version, JsonObject typeDto);

  JsonObject toDTO(BusinessTypeSnapshot snapshot, VertexObject vertex);

  BusinessTypeSnapshot get(SnapshotContainer<?> version, MdVertexDAOIF mdVertex);

  BusinessTypeSnapshot get(SnapshotContainer<?> version, String code);
}
