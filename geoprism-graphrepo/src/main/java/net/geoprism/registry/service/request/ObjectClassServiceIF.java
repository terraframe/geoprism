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
package net.geoprism.registry.service.request;

import java.util.List;

import org.commongeoregistry.adapter.metadata.AttributeType;

import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.registry.graph.BusinessType;
import net.geoprism.registry.graph.ObjectClass;
import net.geoprism.registry.view.OrganizationGroup;

public interface ObjectClassServiceIF<T extends ObjectClass, D>
{

  /**
   * Creates a {@link BusinessType} from the given JSON.
   * 
   * @param sessionId
   * @param ptJSON
   *          JSON of the {@link BusinessType} to be created.
   * @return newly created {@link BusinessType}
   */
  D apply(String sessionId, D dto);

  void remove(String sessionId, String oid);

  D edit(String sessionId, String oid);

  void unlock(String sessionId, String oid);

  List<OrganizationGroup<D>> listByOrg(String sessionId);

  List<D> getAll(String sessionId);

  D get(String sessionId, String oid);

  /**
   * Adds an attribute to the given {@link T}.
   * 
   * @pre given {@link T} must already exist.
   * 
   * @param sessionId
   *
   * @param businessTypeCode
   *          string of the {@link T} to be updated.
   * @param attributeTypeJSON
   *          AttributeType to be added to the T
   * @return updated {@link T}
   */
  AttributeType createAttributeType(String sessionId, String businessTypeCode, AttributeType attributeType);

  /**
   * Updates an attribute in the given {@link T}.
   * 
   * @pre given {@link T} must already exist.
   * 
   * @param sessionId
   * @param businessTypeCode
   *          string of the {@link T} to be updated.
   * @param attributeTypeJSON
   *          AttributeType to be added to the T
   * @return updated {@link AttributeType}
   */
  AttributeType updateAttributeType(String sessionId, String businessTypeCode, AttributeType attributeType);

  /**
   * Deletes an attribute from the given {@link T}.
   * 
   * @pre given {@link T} must already exist.
   * @pre given {@link T} must already exist.
   * 
   * @param sessionId
   * @param code
   *          string of the {@link T} to be updated.
   * @param attributeName
   *          Name of the attribute to be removed from the T
   * @return updated {@link T}
   */
  void removeAttributeType(String sessionId, String businessTypeCode, String attributeName);

}