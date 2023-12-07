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

import org.commongeoregistry.adapter.metadata.AttributeType;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;

import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.BusinessType;
import net.geoprism.registry.Organization;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.view.JsonSerializable;
import net.geoprism.registry.view.Page;

@Component
public interface BusinessTypeBusinessServiceIF
{

  void delete(BusinessType type);

  AttributeType createAttributeType(BusinessType type, AttributeType attributeType);

  AttributeType createAttributeType(BusinessType type, JsonObject attrObj);

  AttributeType updateAttributeType(BusinessType type, JsonObject attrObj);

  void setLabelAttribute(BusinessType type, String attributeName);

  AttributeType updateAttributeType(BusinessType type, AttributeType attrType);

  void removeAttribute(BusinessType type, String attributeName);

  void deleteMdAttributeFromAttributeType(BusinessType type, String attributeName);

  JsonObject toJSON(BusinessType type);

  JsonObject toJSON(BusinessType type, boolean includeAttribute, boolean flattenLocalAttributes);

  Page<JsonSerializable> data(BusinessType type, JsonObject criteria);

  BusinessType apply(JsonObject object);

  List<BusinessEdgeType> getParentEdgeTypes(BusinessType type);

  List<BusinessEdgeType> getChildEdgeTypes(BusinessType type);

  List<BusinessEdgeType> getEdgeTypes(BusinessType type);

  BusinessType getByCode(String code);

  JsonArray listByOrg();

  List<BusinessType> getAll();

  List<BusinessType> getForOrganization(ServerOrganization organization);

  List<BusinessType> getForOrganization(Organization organization);

  boolean isEdgeABusinessType(MdEdgeDAOIF mdEdge);

  BusinessType getByMdEdge(MdEdgeDAOIF mdEdge);

  BusinessType getByMdVertex(MdVertexDAOIF mdVertex);

}
