package net.geoprism.registry.cache;

import java.util.List;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.Optional;
import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.HierarchyType;
import org.commongeoregistry.adapter.metadata.MetadataCacheIF;
import org.commongeoregistry.adapter.metadata.OrganizationDTO;

import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.service.business.ServiceFactory;

public class ServerAdapterCache implements MetadataCacheIF
{

  @Override
  public void rebuild()
  {
  }

  @Override
  public void addTerm(Term term)
  {
  }

  @Override
  public Optional<Term> getTerm(String code)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addOrganization(OrganizationDTO organization)
  {
  }

  @Override
  public Optional<OrganizationDTO> getOrganization(String code)
  {
    return ServiceFactory.getMetadataCache().getOrganization(code).map(org -> Optional.of(org.toDTO())).orElse(Optional.of(null));
  }

  @Override
  public List<OrganizationDTO> getAllOrganizations()
  {
    return ServiceFactory.getMetadataCache().getAllOrganizations().stream().map(org -> org.toDTO()).collect(Collectors.toList());
  }

  @Override
  public void removeOrganization(String code)
  {
  }

  @Override
  public void addGeoObjectType(GeoObjectType geoObjectType)
  {
  }

  @Override
  public Optional<GeoObjectType> getGeoObjectType(String code)
  {
    java.util.Optional<ServerGeoObjectType> result = ServiceFactory.getMetadataCache().getGeoObjectType(code);

    if (result.isPresent())
    {
      return Optional.of(result.get().toDTO());
    }

    return Optional.of(null);
  }

  @Override
  public void removeGeoObjectType(String code)
  {
  }

  @Override
  public void addHierarchyType(HierarchyType hierarchyType)
  {
  }

  @Override
  public Optional<HierarchyType> getHierachyType(String code)
  {
    return ServiceFactory.getMetadataCache().getHierachyType(code).map(type -> Optional.of(type.toDTO())).orElse(Optional.of(null));
  }

  @Override
  public void removeHierarchyType(String code)
  {
  }

  @Override
  public List<OrganizationDTO> getAllOrganizationsTypes()
  {
    return ServiceFactory.getMetadataCache().getAllOrganizations().stream().map(org -> org.toDTO()).collect(Collectors.toList());
  }

  @Override
  public List<String> getAllOrganizationCodes()
  {
    return ServiceFactory.getMetadataCache().getAllOrganizations().stream().map(org -> org.getCode()).collect(Collectors.toList());
  }

  @Override
  public List<GeoObjectType> getAllGeoObjectTypes()
  {
    return ServiceFactory.getMetadataCache().getAllGeoObjectTypes().stream().map(type -> type.toDTO()).collect(Collectors.toList());
  }

  @Override
  public List<String> getAllGeoObjectTypeCodes()
  {
    return ServiceFactory.getMetadataCache().getAllGeoObjectTypes().stream().map(type -> type.getCode()).collect(Collectors.toList());
  }

  @Override
  public List<HierarchyType> getAllHierarchyTypes()
  {
    return ServiceFactory.getMetadataCache().getAllHierarchyTypes().stream().map(type -> type.toDTO()).collect(Collectors.toList());
  }

}
