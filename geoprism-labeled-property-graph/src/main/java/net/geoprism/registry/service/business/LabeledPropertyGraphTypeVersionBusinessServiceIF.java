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
import com.runwaysdk.business.graph.VertexObject;

import net.geoprism.graph.GeoObjectTypeSnapshot;
import net.geoprism.graph.HierarchyTypeSnapshot;
import net.geoprism.graph.LabeledPropertyGraphTypeEntry;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;
import net.geoprism.spring.core.ApplicationContextHolder;

@Component
public interface LabeledPropertyGraphTypeVersionBusinessServiceIF
{
  // Hack for published jobs
  public static LabeledPropertyGraphTypeVersionBusinessServiceIF getInstance()
  {
    return ApplicationContextHolder.getContext().getBean(LabeledPropertyGraphTypeVersionBusinessServiceIF.class);
  }

  public void delete(LabeledPropertyGraphTypeVersion version);

  public void remove(LabeledPropertyGraphTypeVersion version);

  GeoObjectTypeSnapshot getRootType(LabeledPropertyGraphTypeVersion version);

  GeoObjectTypeSnapshot getSnapshot(LabeledPropertyGraphTypeVersion version, String typeCode);

  List<GeoObjectTypeSnapshot> getTypes(LabeledPropertyGraphTypeVersion version);

  List<HierarchyTypeSnapshot> getHierarchies(LabeledPropertyGraphTypeVersion version);

  HierarchyTypeSnapshot getHierarchySnapshot(LabeledPropertyGraphTypeVersion version, String typeCode);

  VertexObject getVertex(LabeledPropertyGraphTypeVersion version, String uid, String typeCode);

  void truncate(LabeledPropertyGraphTypeVersion version);

  VertexObject getObject(LabeledPropertyGraphTypeVersion version, String uid);

  JsonObject toJSON(LabeledPropertyGraphTypeVersion version);

  JsonObject toJSON(LabeledPropertyGraphTypeVersion version, boolean includeTableDefinitions);

  public LabeledPropertyGraphTypeVersion create(LabeledPropertyGraphTypeEntry entry, boolean working, int versionNumber);

  LabeledPropertyGraphTypeVersion create(LabeledPropertyGraphTypeEntry entry, JsonObject json);

  public LabeledPropertyGraphTypeVersion get(String oid);

  List<? extends LabeledPropertyGraphTypeVersion> getAll();

  public void publish(LabeledPropertyGraphTypeVersion version);

  public void publishNoAuth(LabeledPropertyGraphTypeVersion version);

  public void createPublishJob(LabeledPropertyGraphTypeVersion version);

}
