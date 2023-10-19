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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.business.graph.VertexObject;

import net.geoprism.graph.LabeledPropertyGraphSynchronization;
import net.geoprism.registry.view.Page;

@Component
public interface LabeledPropertyGraphSynchronizationBusinessServiceIF
{

  void delete(LabeledPropertyGraphSynchronization synchronization);

  void updateRemoteVersion(LabeledPropertyGraphSynchronization synchronization, String versionId, Integer versionNumber);

  void execute(LabeledPropertyGraphSynchronization synchronization);

  void executeNoAuth(LabeledPropertyGraphSynchronization synchronization);

  VertexObject getObject(LabeledPropertyGraphSynchronization synchronization, String uid);

  LabeledPropertyGraphSynchronization fromJSON(JsonObject object);

  LabeledPropertyGraphSynchronization apply(JsonObject object);

  JsonArray getAll();

  Page<LabeledPropertyGraphSynchronization> page(JsonObject criteria);

  LabeledPropertyGraphSynchronization get(String oid);

}
