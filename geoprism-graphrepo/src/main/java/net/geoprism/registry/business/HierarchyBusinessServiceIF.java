package net.geoprism.registry.business;

import org.commongeoregistry.adapter.metadata.HierarchyType;

import net.geoprism.registry.HierarchicalRelationshipType;
import net.geoprism.registry.model.ServerHierarchyType;

public interface HierarchyBusinessServiceIF
{
  public ServerHierarchyType createHierarchyType(HierarchyType hierarchyType);
  
  public ServerHierarchyType get(HierarchicalRelationshipType hierarchicalRelationship);
  
  public ServerHierarchyType get(HierarchicalRelationshipType hierarchicalRelationship, boolean buildHierarchyNodes);
}
