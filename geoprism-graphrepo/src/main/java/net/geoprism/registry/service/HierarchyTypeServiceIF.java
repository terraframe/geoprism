package net.geoprism.registry.service;

import org.commongeoregistry.adapter.metadata.HierarchyType;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;

import net.geoprism.registry.permission.PermissionContext;

@Component
public interface HierarchyTypeServiceIF
{
  public HierarchyType addToHierarchy(String sessionId, String hierarchyCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode);

  public HierarchyType removeFromHierarchy(String sessionId, String hierarchyCode, String parentGeoObjectTypeCode, String childGeoObjectTypeCode, boolean b);

  public HierarchyType insertBetweenTypes(String sessionId, String hierarchyCode, String parentGeoObjectTypeCode, String middleGeoObjectTypeCode, String youngestGeoObjectTypeCode);

  public HierarchyType setInheritedHierarchy(String sessionId, String hierarchyTypeCode, String inheritedHierarchyTypeCode, String geoObjectTypeCode);

  public HierarchyType removeInheritedHierarchy(String sessionId, String hierarchyTypeCode, String geoObjectTypeCode);

  public HierarchyType[] getHierarchyTypes(String sessionId, String[] aTypes, PermissionContext pContext);

  public HierarchyType createHierarchyType(String sessionId, String string);

  public HierarchyType updateHierarchyType(String sessionId, String string);

  public void deleteHierarchyType(String sessionId, String code);
  
  public JsonArray getHierarchiesForType(String sessionId, String code, Boolean includeTypes);
  
  public JsonArray getHierarchiesForSubtypes(String sessionId, String code);
  
  public JsonArray getHierarchiesForGeoObjectOverTime(String sessionId, String code, String typeCode);
}
