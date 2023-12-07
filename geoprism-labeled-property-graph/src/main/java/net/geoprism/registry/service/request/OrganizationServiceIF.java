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

import org.commongeoregistry.adapter.metadata.OrganizationDTO;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.geoprism.registry.model.OrganizationView;
import net.geoprism.registry.view.Page;

@Component
public interface OrganizationServiceIF
{
  /**
   * Returns the {@link OrganizationDTO}s with the given codes or all
   * {@link OrganizationDTO}s if no codes are provided.
   * 
   * @param sessionId
   * @param codes
   *          codes of the {@link OrganizationDTO}s.
   * @return the {@link OrganizationDTO}s with the given codes or all
   *         {@link OrganizationDTO}s if no codes are provided.
   */
  public OrganizationDTO[] getOrganizations(String sessionId, String[] codes);

  public Page<OrganizationView> getPage(String sessionId, Integer pageSize, Integer pageNumber);

  /**
   * Creates a {@link OrganizationDTO} from the given JSON.
   * 
   * @param sessionId
   * @param json
   *          JSON of the {@link OrganizationDTO} to be created.
   * @return newly created {@link OrganizationDTO}
   */
  public OrganizationDTO createOrganization(String sessionId, String json);

  /**
   * Updates the given {@link OrganizationDTO} represented as JSON.
   * 
   * @pre given {@link OrganizationDTO} must already exist.
   * 
   * @param sessionId
   * @param json
   *          JSON of the {@link OrganizationDTO} to be updated.
   * @return updated {@link OrganizationDTO}
   */
  public OrganizationDTO updateOrganization(String sessionId, String json);

  /**
   * Deletes the {@link OrganizationDTO} with the given code.
   * 
   * @param sessionId
   * @param code
   *          code of the {@link OrganizationDTO} to delete.
   */
  public void deleteOrganization(String sessionId, String code);

  public void addChild(String sessionId, String parentCode, String childCode);

  public void removeChild(String sessionId, String parentCode, String childCode);

  public JsonObject getChildren(String sessionId, String code, Integer pageSize, Integer pageNumber);

  public JsonObject getAncestorTree(String sessionId, String rootCode, String code, Integer pageSize);

  public void move(String sessionId, String code, String parentCode);

  public void removeAllParents(String sessionId, String code);

  public JsonArray exportToJson(String sessionId);

  public void importJsonTree(String sessionId, String json);

}
