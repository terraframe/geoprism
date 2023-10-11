package net.geoprism.registry.service;

import java.util.List;

import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;

import net.geoprism.registry.business.GeoObjectTypeBusinessServiceIF;
import net.geoprism.registry.permission.PermissionContext;

@Component
public class GeoObjectTypeService implements GeoObjectTypeServiceIF
{
  @Autowired
  private GeoObjectTypeBusinessServiceIF service;

  @Override
  @Request(RequestType.SESSION)
  public List<GeoObjectType> getAncestors(String sessionId, String code, String hierarchyCode, Boolean includeInheritedTypes, Boolean includeChild)
  {
    return this.service.getAncestors(code, hierarchyCode, includeInheritedTypes, includeChild);
  }

  @Override
  @Request(RequestType.SESSION)
  public void deleteGeoObjectType(String sessionId, String code)
  {
    this.service.deleteGeoObjectType(code);
  }

  @Override
  @Request(RequestType.SESSION)
  public AttributeType createAttributeType(String sessionId, String geoObjectTypeCode, String attributeTypeJSON)
  {
    return this.service.createAttributeType(geoObjectTypeCode, attributeTypeJSON);
  }

  @Override
  @Request(RequestType.SESSION)
  public AttributeType updateAttributeType(String sessionId, String geoObjectTypeCode, String attributeTypeJSON)
  {
    return this.service.updateAttributeType(geoObjectTypeCode, attributeTypeJSON);
  }

  @Override
  @Request(RequestType.SESSION)
  public void deleteAttributeType(String sessionId, String gtId, String attributeName)
  {
    this.service.deleteAttributeType(gtId, attributeName);
  }

  @Override
  @Request(RequestType.SESSION)
  public GeoObjectType updateGeoObjectType(String sessionId, String gtJSON)
  {
    return this.service.updateGeoObjectType(gtJSON);
  }

  @Override
  @Request(RequestType.SESSION)
  public GeoObjectType createGeoObjectType(String sessionId, String gtJSON)
  {
    return this.service.createGeoObjectType(gtJSON);
  }

  @Override
  @Request(RequestType.SESSION)
  public GeoObjectType[] getGeoObjectTypes(String sessionId, String[] codes, PermissionContext context)
  {
    List<GeoObjectType> types = this.service.getGeoObjectTypes(codes, context);
    return types.toArray(new GeoObjectType[types.size()]);
  }

}
