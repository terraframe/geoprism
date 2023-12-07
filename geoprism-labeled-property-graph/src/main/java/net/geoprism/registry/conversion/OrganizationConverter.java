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
package net.geoprism.registry.conversion;

import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.OrganizationDTO;

import net.geoprism.registry.Organization;
import net.geoprism.registry.graph.GraphOrganization;
import net.geoprism.registry.model.ServerOrganization;

public class OrganizationConverter extends LocalizedValueConverter
{

  public OrganizationDTO build(Organization organization)
  {
    String code = organization.getCode();

    LocalizedValue label = convertNoAutoCoalesce(organization.getDisplayLabel());

    LocalizedValue contactInfo = convertNoAutoCoalesce(organization.getContactInfo());

    OrganizationDTO dto = new OrganizationDTO(code, label, contactInfo);
    dto.setEnabled(organization.getEnabled());
    return dto;
  }

  public ServerOrganization fromDTO(OrganizationDTO organizationDTO)
  {
    ServerOrganization organization = new ServerOrganization(new Organization(), new GraphOrganization());

    organization.setCode(organizationDTO.getCode());
    organization.setDisplayLabel(organizationDTO.getLabel());
    organization.setContactInfo(organizationDTO.getContactInfo());
    organization.setEnabled(organizationDTO.getEnabled());

    return organization;
  }

}
