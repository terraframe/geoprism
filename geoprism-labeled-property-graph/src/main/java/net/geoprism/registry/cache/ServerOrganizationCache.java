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
package net.geoprism.registry.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.commongeoregistry.adapter.RegistryAdapter;

import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

import net.geoprism.registry.Organization;
import net.geoprism.registry.OrganizationQuery;
import net.geoprism.registry.model.ServerOrganization;

public class ServerOrganizationCache
{
  private Map<String, ServerOrganization> organizationMap;

  private RegistryAdapter                 adapter;

  public ServerOrganizationCache(RegistryAdapter adapter)
  {
    this.adapter = adapter;
    this.organizationMap = new ConcurrentHashMap<String, ServerOrganization>();
  }

  public RegistryAdapter getAdapter()
  {
    return this.adapter;
  }

  public Map<String, ServerOrganization> getOrganizationMap()
  {
    return organizationMap;
  }

  public void setOrganizationMap(Map<String, ServerOrganization> organizationMap)
  {
    this.organizationMap = organizationMap;
  }

  /**
   * Clears the metadata cache.
   */
  public void rebuild()
  {
    this.organizationMap = new ConcurrentHashMap<String, ServerOrganization>();
  }

  public void addOrganization(ServerOrganization organization)
  {
    this.organizationMap.put(organization.getCode(), organization);

    getAdapter().getMetadataCache().addOrganization(organization.toDTO());
  }

  public Optional<ServerOrganization> getOrganization(String code)
  {
    return Optional.of(this.organizationMap.get(code));
  }

  public List<ServerOrganization> getAllOrganizations()
  {
    // return this.organizationMap.values().toArray(new
    // Organization[this.organizationMap.values().size()]);

    return new ArrayList<ServerOrganization>(this.organizationMap.values());
  }

  public void removeOrganization(String code)
  {
    this.organizationMap.remove(code);

    getAdapter().getMetadataCache().removeOrganization(code);
  }

  public List<ServerOrganization> getAllOrganizationsTypes()
  {
    return new ArrayList<ServerOrganization>(this.organizationMap.values());
  }

  public List<String> getAllOrganizationCodes()
  {
    List<ServerOrganization> organizations = this.getAllOrganizationsTypes();

    List<String> codes = new ArrayList<String>(organizations.size());

    for (int i = 0; i < organizations.size(); ++i)
    {
      codes.add(organizations.get(i).getCode());
    }

    return codes;
  }

  public void refresh()
  {
    this.rebuild();

    try
    {
      MdClassDAO.getMdClassDAO(Organization.CLASS);

      OrganizationQuery oQ = new OrganizationQuery(new QueryFactory());

      try (OIterator<? extends Organization> it3 = oQ.getIterator())
      {
        while (it3.hasNext())
        {
          Organization organization = it3.next();

          this.addOrganization(ServerOrganization.get(organization));
        }
      }
    }
    catch (com.runwaysdk.dataaccess.cache.DataNotFoundException e)
    {
      // skip for now
    }

  }

}
