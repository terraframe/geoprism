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

import org.commongeoregistry.adapter.metadata.OrganizationDTO;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.geoprism.registry.model.GraphNode;
import net.geoprism.registry.model.OrganizationView;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.view.Page;

@Component
public interface OrganizationBusinessServiceIF
{

  public ServerOrganization create(OrganizationDTO organizationDTO);

  public ServerOrganization update(OrganizationDTO organizationDTO);

  public void delete(ServerOrganization sorg);

  void move(ServerOrganization organization, ServerOrganization newParent);

  void removeAllParents(ServerOrganization organization);

  GraphNode<ServerOrganization> getAncestorTree(ServerOrganization child, String rootCode, Integer pageSize);

  Page<ServerOrganization> getChildren(ServerOrganization parent, Integer pageSize, Integer pageNumber);

  void removeChild(ServerOrganization parent, ServerOrganization child);

  void addChild(ServerOrganization parent, ServerOrganization child);

  void apply(ServerOrganization organization, ServerOrganization parent);

  JsonArray exportToJson();

  public void importJsonTree(JsonArray array);

  public void importJsonTree(ServerOrganization parent, JsonObject object);

  public Page<OrganizationView> getPage(Integer pageSize, Integer pageNumber);
}
