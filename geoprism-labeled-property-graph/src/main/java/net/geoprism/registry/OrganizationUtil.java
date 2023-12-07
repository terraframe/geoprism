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
package net.geoprism.registry;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.commongeoregistry.adapter.metadata.OrganizationDTO;

import net.geoprism.registry.model.ServerOrganization;

public class OrganizationUtil
{
  public static List<OrganizationDTO> sortDTOs(List<OrganizationDTO> orgs)
  {
    // Group by depth first parent
    Map<String, List<OrganizationDTO>> map = new HashMap<>();

    orgs.forEach(org -> {
      String parentCode = org.getParentCode() != null ? org.getParentCode() : "";

      map.putIfAbsent(parentCode, new LinkedList<>());

      map.get(parentCode).add(org);
    });

    List<OrganizationDTO> sortedList = new LinkedList<OrganizationDTO>();

    sortDTOs(map, sortedList, "");

    return sortedList;
  }

  private static void sortDTOs(Map<String, List<OrganizationDTO>> map, List<OrganizationDTO> results, String code)
  {
    List<OrganizationDTO> orgs = map.get(code);

    if (orgs != null)
    {
      orgs.sort((a, b) -> {
        return a.getLabel().getValue().compareTo(b.getLabel().getValue());
      });

      orgs.forEach(org -> {
        results.add(org);

        if (map.containsKey(org.getCode()))
        {
          sortDTOs(map, results, org.getCode());
        }
      });
    }
  }

  public static List<ServerOrganization> sort(List<ServerOrganization> orgs)
  {
    // Group by depth first parent
    Map<String, List<ServerOrganization>> map = new HashMap<>();

    orgs.forEach(org -> {
      ServerOrganization parent = org.getParent();
      String parentCode = parent != null ? parent.getCode() : "";

      map.putIfAbsent(parentCode, new LinkedList<>());

      map.get(parentCode).add(org);
    });

    List<ServerOrganization> list = new LinkedList<ServerOrganization>();

    sort(map, list, "");

    return list;
  }

  private static void sort(Map<String, List<ServerOrganization>> map, List<ServerOrganization> results, String code)
  {
    List<ServerOrganization> orgs = map.get(code);

    orgs.sort((a, b) -> {
      return a.getDisplayLabel().getValue().compareTo(b.getDisplayLabel().getValue());
    });

    orgs.forEach(org -> {
      results.add(org);

      if (map.containsKey(org.getCode()))
      {
        sort(map, results, org.getCode());
      }
    });
  }

}
