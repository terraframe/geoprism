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

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

import net.geoprism.registry.graph.BusinessEdgeType;
import net.geoprism.registry.graph.BusinessType;
import net.geoprism.registry.view.BusinessTypeDTO;
import net.geoprism.registry.view.JsonSerializable;
import net.geoprism.registry.view.Page;

@Component
public interface BusinessTypeBusinessServiceIF extends ObjectClassBusinessServiceIF<BusinessType, BusinessTypeDTO>
{

  void setLabelAttribute(BusinessType type, String attributeName);

  Page<JsonSerializable> data(BusinessType type, JsonObject criteria);

  List<BusinessEdgeType> getParentEdgeTypes(BusinessType type);

  List<BusinessEdgeType> getChildEdgeTypes(BusinessType type);

  List<BusinessEdgeType> getEdgeTypes(BusinessType type);

  BusinessType apply(BusinessType businessType);

}
