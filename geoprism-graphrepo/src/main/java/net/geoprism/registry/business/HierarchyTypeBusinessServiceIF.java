package net.geoprism.registry.business;

import java.util.List;

import org.commongeoregistry.adapter.metadata.HierarchyNode;
import org.commongeoregistry.adapter.metadata.HierarchyType;

import net.geoprism.registry.HierarchicalRelationshipType;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;

public interface HierarchyTypeBusinessServiceIF
{
  public ServerHierarchyType createHierarchyType(HierarchyType hierarchyType);
  
  public ServerHierarchyType get(HierarchicalRelationshipType hierarchicalRelationship);
  
  public ServerHierarchyType get(HierarchicalRelationshipType hierarchicalRelationship, boolean buildHierarchyNodes);
  
  public void refresh(ServerHierarchyType sht);
  
  public void update(ServerHierarchyType sht, HierarchyType hierarchyType);
  
  public void delete(ServerHierarchyType sht);
  
  public void addToHierarchy(ServerHierarchyType sht, ServerGeoObjectType parentType, ServerGeoObjectType childType);

  public void addToHierarchy(ServerHierarchyType sht, ServerGeoObjectType parentType, ServerGeoObjectType childType, boolean refresh);
  
  public void removeChild(ServerHierarchyType sht, ServerGeoObjectType parentType, ServerGeoObjectType childType, boolean migrateChildren);
  
  public List<ServerGeoObjectType> getAllTypes(ServerHierarchyType sht);

  public List<ServerGeoObjectType> getAllTypes(ServerHierarchyType sht, boolean includeInherited);

  public HierarchyType toHierarchyType(ServerHierarchyType sht);

  public HierarchyType toHierarchyType(ServerHierarchyType sht, boolean includePrivateTypes);
  
  public List<ServerGeoObjectType> getChildren(ServerHierarchyType sht, ServerGeoObjectType parent);

  public boolean hasVisibleRoot(ServerHierarchyType sht);

  public List<HierarchyNode> getRootGeoObjectTypes(ServerHierarchyType sht);

  public List<HierarchyNode> getRootGeoObjectTypes(ServerHierarchyType sht, boolean includePrivateTypes);

  public void insertBetween(ServerHierarchyType sht, ServerGeoObjectType parentType, ServerGeoObjectType middleType, List<ServerGeoObjectType> youngestTypes);

  public List<ServerGeoObjectType> getDirectRootNodes(ServerHierarchyType sht);

  public void validateUniversalRelationship(ServerHierarchyType sht, ServerGeoObjectType childType, ServerGeoObjectType parentType);
}
