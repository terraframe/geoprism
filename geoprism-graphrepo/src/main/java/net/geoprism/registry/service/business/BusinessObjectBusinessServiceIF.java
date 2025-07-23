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

import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.BusinessType;
import net.geoprism.registry.model.BusinessObject;
import net.geoprism.registry.model.EdgeDirection;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.graph.VertexServerGeoObject;

@Component
public interface BusinessObjectBusinessServiceIF
{

  public JsonObject toJSON(BusinessObject object);

  public void apply(BusinessObject object);

  public void apply(BusinessObject object, boolean validateOrigin);

  public void delete(BusinessObject object);

  public void delete(BusinessObject object, boolean validateOrigin);

  public boolean exists(BusinessObject object, BusinessEdgeType edgeType, ServerGeoObjectIF geoObject, EdgeDirection direction);

  public void addGeoObject(BusinessObject object, BusinessEdgeType edgeType, ServerGeoObjectIF geoObject, EdgeDirection direction, String uid, boolean validateOrigin);

  public void removeGeoObject(BusinessObject object, BusinessEdgeType edgeType, ServerGeoObjectIF geoObject, EdgeDirection direction, boolean validateOrigin);

  public List<VertexServerGeoObject> getGeoObjects(BusinessObject object, BusinessEdgeType edgeType, EdgeDirection direction);

  public boolean exists(BusinessEdgeType type, BusinessObject parent, BusinessObject child);

  public void addParent(BusinessObject object, BusinessEdgeType type, BusinessObject parent, String uid);

  public void removeParent(BusinessObject object, BusinessEdgeType type, BusinessObject parent);

  public void addParent(BusinessObject object, BusinessEdgeType type, BusinessObject parent, String uid, boolean validateOrigin);

  public void removeParent(BusinessObject object, BusinessEdgeType type, BusinessObject parent, boolean validateOrigin);

  public List<BusinessObject> getParents(BusinessObject object, BusinessEdgeType type);

  public void addChild(BusinessObject object, BusinessEdgeType type, BusinessObject child, String uid);

  public void removeChild(BusinessObject object, BusinessEdgeType type, BusinessObject child);

  public void addChild(BusinessObject object, BusinessEdgeType type, BusinessObject child, String uid, boolean validateOrigin);

  public void removeChild(BusinessObject object, BusinessEdgeType type, BusinessObject child, boolean validateOrigin);

  public List<BusinessObject> getChildren(BusinessObject object, BusinessEdgeType type);

  public BusinessObject newInstance(BusinessType type);

  public BusinessObject get(BusinessType type, String attributeName, Object value);

  public BusinessObject getByCode(BusinessType type, Object value);

  public BusinessObject newInstance(BusinessType type, JsonObject json);

  public void populate(BusinessObject object, JsonObject json);

}
