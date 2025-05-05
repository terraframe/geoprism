/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Common Geo Registry Adapter(tm).
 *
 * Common Geo Registry Adapter(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Common Geo Registry Adapter(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Common Geo Registry Adapter(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package org.commongeoregistry.adapter.metadata;

import java.util.List;

import org.commongeoregistry.adapter.Optional;
import org.commongeoregistry.adapter.Term;

public interface MetadataCacheIF
{

  /**
   * Clears the metadata cache.
   */
  void rebuild();

  void addTerm(Term term);

  Optional<Term> getTerm(String code);

  void addOrganization(OrganizationDTO organization);

  Optional<OrganizationDTO> getOrganization(String code);

  List<OrganizationDTO> getAllOrganizations();

  void removeOrganization(String code);

  void addGeoObjectType(GeoObjectType geoObjectType);

  Optional<GeoObjectType> getGeoObjectType(String code);

  void removeGeoObjectType(String code);

  void addHierarchyType(HierarchyType hierarchyType);

  Optional<HierarchyType> getHierachyType(String code);

  void removeHierarchyType(String code);

  List<OrganizationDTO> getAllOrganizationsTypes();

  List<String> getAllOrganizationCodes();

  List<GeoObjectType> getAllGeoObjectTypes();

  List<String> getAllGeoObjectTypeCodes();

  List<HierarchyType> getAllHierarchyTypes();

}