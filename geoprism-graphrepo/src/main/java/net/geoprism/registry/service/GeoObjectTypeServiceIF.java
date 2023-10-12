package net.geoprism.registry.service;

import java.util.List;

import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.springframework.stereotype.Component;

import net.geoprism.registry.permission.PermissionContext;

@Component
public interface GeoObjectTypeServiceIF
{
  public List<GeoObjectType> getAncestors(String sessionId, String code, String hierarchyCode, Boolean includeInheritedTypes, Boolean includeChild);

  public void deleteGeoObjectType(String sessionId, String code);

  public AttributeType createAttributeType(String sessionId, String geoObjectTypeCode, String attributeTypeJSON);

  public AttributeType updateAttributeType(String sessionId, String geoObjectTypeCode, String attributeTypeJSON);

  public void deleteAttributeType(String sessionId, String gtId, String attributeName);

  public GeoObjectType updateGeoObjectType(String sessionId, String gtJSON);

  public GeoObjectType createGeoObjectType(String sessionId, String gtJSON);

  public GeoObjectType[] getGeoObjectTypes(String sessionId, String[] codes, PermissionContext context);

}
