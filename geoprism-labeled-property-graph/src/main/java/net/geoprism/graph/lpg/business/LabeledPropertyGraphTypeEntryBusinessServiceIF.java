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
package net.geoprism.graph.lpg.business;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.geoprism.graph.LabeledPropertyGraphType;
import net.geoprism.graph.LabeledPropertyGraphTypeEntry;
import net.geoprism.graph.LabeledPropertyGraphTypeVersion;

@Component
public interface LabeledPropertyGraphTypeEntryBusinessServiceIF
{

  public void delete(LabeledPropertyGraphTypeEntry entry);

  JsonObject toJSON(LabeledPropertyGraphTypeEntry entry);

  List<LabeledPropertyGraphTypeVersion> getVersions(LabeledPropertyGraphTypeEntry entry);

  JsonArray getVersionJson(LabeledPropertyGraphTypeEntry entry);

  LabeledPropertyGraphTypeVersion publish(LabeledPropertyGraphTypeEntry entry);

  LabeledPropertyGraphTypeVersion publishNoAuth(LabeledPropertyGraphTypeEntry entry);

  LabeledPropertyGraphTypeEntry create(LabeledPropertyGraphType type, Date forDate);

  LabeledPropertyGraphTypeEntry create(LabeledPropertyGraphType list, JsonObject json);

  public LabeledPropertyGraphTypeEntry get(String oid);

}
