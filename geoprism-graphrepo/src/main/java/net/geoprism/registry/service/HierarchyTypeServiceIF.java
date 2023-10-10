package net.geoprism.registry.service;

import org.commongeoregistry.adapter.metadata.HierarchyType;

import net.geoprism.registry.permission.PermissionContext;

public interface HierarchyTypeServiceIF
{

  HierarchyType addToHierarchy(String sessionId, String hierarchyCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode);

  HierarchyType removeFromHierarchy(String sessionId, String hierarchyCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode, boolean b);

  HierarchyType insertBetweenTypes(String sessionId, String hierarchyCode, String parentGeoObjectTypeCode, String middleGeoObjectTypeCode, String youngestGeoObjectTypeCode);

  HierarchyType setInheritedHierarchy(String sessionId, String hierarchyTypeCode, String inheritedHierarchyTypeCode, String geoObjectTypeCode);

  HierarchyType removeInheritedHierarchy(String sessionId, String hierarchyTypeCode, String geoObjectTypeCode);

  HierarchyType[] getHierarchyTypes(String sessionId, String[] aTypes, PermissionContext pContext);

  HierarchyType createHierarchyType(String sessionId, String string);

  HierarchyType updateHierarchyType(String sessionId, String string);

  void deleteHierarchyType(String sessionId, String code);

}
