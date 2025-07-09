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

import org.commongeoregistry.adapter.constants.GeometryType;
import org.commongeoregistry.adapter.dataaccess.GeoObject;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

import net.geoprism.graph.GeoObjectTypeSnapshot;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.registry.model.SnapshotContainer;

@Component
public interface GeoObjectTypeSnapshotBusinessServiceIF
{

  void truncate(GeoObjectTypeSnapshot snapshot);

  void delete(GeoObjectTypeSnapshot snapshot);

  void createGeometryAttribute(GeometryType geometryType, MdVertexDAO mdTableDAO);

  String getTableName(String className);

  GeoObjectTypeSnapshot createRoot(SnapshotContainer<?> version);

  GeoObjectTypeSnapshot create(LabeledPropertyGraphTypeVersion version, JsonObject type);

  GeoObject toGeoObject(GeoObjectTypeSnapshot snapshot, VertexObject vertex);

  GeoObject toGeoObject(VertexObject vertex, GeoObjectType type);

  GeoObjectTypeSnapshot get(LabeledPropertyGraphTypeVersion version, MdVertexDAOIF mdVertex);

  GeoObjectTypeSnapshot get(LabeledPropertyGraphTypeVersion version, String code);

  GeoObjectTypeSnapshot getRoot(LabeledPropertyGraphTypeVersion version);

}
