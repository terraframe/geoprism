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