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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.metadata.OrganizationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.registry.OrganizationUtil;
import net.geoprism.registry.model.OrganizationMetadata;
import net.geoprism.registry.model.OrganizationView;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.service.business.CacheProviderIF;
import net.geoprism.registry.service.business.OrganizationBusinessServiceIF;
import net.geoprism.registry.service.permission.OrganizationPermissionServiceIF;
import net.geoprism.registry.view.Page;

@Service
public class OrganizationService implements OrganizationServiceIF
{
  @Autowired
  private CacheProviderIF                 provider;

  @Autowired
  private OrganizationBusinessServiceIF   service;

  @Autowired
  private OrganizationPermissionServiceIF permissionService;

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
  @Request(RequestType.SESSION)
  public OrganizationDTO[] getOrganizations(String sessionId, String[] codes)
  {
    List<OrganizationDTO> orgs = new LinkedList<OrganizationDTO>();

    if (codes == null || codes.length == 0)
    {
      List<ServerOrganization> cachedOrgs = provider.getServerCache().getAllOrganizations();

      for (ServerOrganization cachedOrg : cachedOrgs)
      {
        orgs.add(cachedOrg.toDTO());
      }
    }
    else
    {
      for (int i = 0; i < codes.length; ++i)
      {
        Optional<ServerOrganization> optional = provider.getServerCache().getOrganization(codes[i]);

        if (optional.isPresent())
        {
          orgs.add(optional.get().toDTO());
        }
        else
        {
          net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
          ex.setTypeLabel(OrganizationMetadata.get().getClassDisplayLabel());
          ex.setDataIdentifier(codes[i]);
          ex.setAttributeLabel(OrganizationMetadata.get().getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));
          throw ex;
        }
      }
    }

    // Filter out orgs based on permissions
    Iterator<OrganizationDTO> it = orgs.iterator();
    while (it.hasNext())
    {
      OrganizationDTO orgDTO = it.next();

      if (!this.permissionService.canActorRead(orgDTO.getCode()))
      {
        it.remove();
      }
    }

    List<OrganizationDTO> list = OrganizationUtil.sortDTOs(orgs);

    return list.toArray(new OrganizationDTO[orgs.size()]);
  }

  /**
   * Creates a {@link OrganizationDTO} from the given JSON.
   * 
   * @param sessionId
   * @param json
   *          JSON of the {@link OrganizationDTO} to be created.
   * @return newly created {@link OrganizationDTO}
   */
  @Request(RequestType.SESSION)
  public OrganizationDTO createOrganization(String sessionId, String json)
  {
    OrganizationDTO organizationDTO = OrganizationDTO.fromJSON(json);

    final ServerOrganization org = this.service.create(organizationDTO);

    // If this did not error out then add to the cache
    provider.getServerCache().addOrganization(org);

    return org.toDTO();
  }

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
  @Request(RequestType.SESSION)
  public OrganizationDTO updateOrganization(String sessionId, String json)
  {
    OrganizationDTO organizationDTO = OrganizationDTO.fromJSON(json);

    final ServerOrganization org = this.service.update(organizationDTO);

    // If this did not error out then add to the cache
    provider.getServerCache().addOrganization(org);

    return org.toDTO();
  }

  /**
   * Deletes the {@link OrganizationDTO} with the given code.
   * 
   * @param sessionId
   * @param code
   *          code of the {@link OrganizationDTO} to delete.
   */
  @Request(RequestType.SESSION)
  public void deleteOrganization(String sessionId, String code)
  {
    ServerOrganization organization = ServerOrganization.getByCode(code);

    this.service.delete(organization);

    // If this did not error out then remove from the cache
    provider.getServerCache().removeOrganization(code);
  }

  @Request(RequestType.SESSION)
  public void addChild(String sessionId, String parentCode, String childCode)
  {
    ServerOrganization parent = ServerOrganization.getByCode(parentCode);
    ServerOrganization child = ServerOrganization.getByCode(childCode);

    this.service.addChild(parent, child);

    provider.getServerCache().addOrganization(child);
  }

  @Request(RequestType.SESSION)
  public void removeChild(String sessionId, String parentCode, String childCode)
  {
    ServerOrganization parent = ServerOrganization.getByCode(parentCode);
    ServerOrganization child = ServerOrganization.getByCode(childCode);

    this.service.removeChild(parent, child);

    provider.getServerCache().addOrganization(child);
  }

  @Request(RequestType.SESSION)
  public JsonObject getChildren(String sessionId, String code, Integer pageSize, Integer pageNumber)
  {
    ServerOrganization parent = code != null ? ServerOrganization.getByCode(code) : null;

    return this.service.getChildren(parent, pageSize, pageNumber).toJSON();
  }

  @Request(RequestType.SESSION)
  public JsonObject getAncestorTree(String sessionId, String rootCode, String code, Integer pageSize)
  {
    ServerOrganization child = ServerOrganization.getByCode(code);

    return this.service.getAncestorTree(child, rootCode, pageSize).toJSON();
  }

  @Request(RequestType.SESSION)
  public void move(String sessionId, String code, String parentCode)
  {
    ServerOrganization organization = ServerOrganization.getByCode(code);
    ServerOrganization newParent = ServerOrganization.getByCode(parentCode);

    this.service.move(organization, newParent);

    // Rebuild the entire organization cache
    provider.getServerCache().addOrganization(organization);
  }

  @Request(RequestType.SESSION)
  public void removeAllParents(String sessionId, String code)
  {
    ServerOrganization organization = ServerOrganization.getByCode(code);

    this.service.removeAllParents(organization);

    // Rebuild the entire organization cache
    provider.getServerCache().addOrganization(organization);
  }

  @Override
  @Request(RequestType.SESSION)
  public JsonArray exportToJson(String sessionId)
  {
    return this.service.exportToJson();
  }

  @Override
  @Request(RequestType.SESSION)
  public void importJsonTree(String sessionId, String json)
  {
    this.service.importJsonTree(JsonParser.parseString(json).getAsJsonArray());

    provider.getServerCache().refresh();
  }

  @Override
  @Request(RequestType.SESSION)
  public Page<OrganizationView> getPage(String sessionId, Integer pageSize, Integer pageNumber)
  {
    return this.service.getPage(pageSize, pageNumber);
  }
}
