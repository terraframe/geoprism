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

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.runwaysdk.system.metadata.MdEdge;

import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.BusinessType;

@Component
public interface BusinessEdgeTypeBusinessServiceIF
{

  BusinessType getParent(BusinessEdgeType edgeType);

  BusinessType getChild(BusinessEdgeType edgeType);

  void update(BusinessEdgeType edgeType, JsonObject object);

  void delete(BusinessEdgeType edgeType);

  JsonObject toJSON(BusinessEdgeType edgeType);

  List<BusinessEdgeType> getAll();

  BusinessEdgeType getByCode(String code);

  BusinessEdgeType getByMdEdge(MdEdge mdEdge);

  BusinessEdgeType create(JsonObject object);

  BusinessEdgeType create(String organizationCode, String code, LocalizedValue label, LocalizedValue description, String parentTypeCode, String childTypeCode);

}
