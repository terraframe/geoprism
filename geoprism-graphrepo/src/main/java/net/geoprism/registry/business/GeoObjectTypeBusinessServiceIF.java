package net.geoprism.registry.business;

import java.util.List;

import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;

import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdBusiness;

import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.permission.PermissionContext;

public interface GeoObjectTypeBusinessServiceIF
{
  public void createDefaultAttributes(Universal universal, MdBusiness definingMdBusiness);

  public void createDefaultAttributes(Universal universal, MdGraphClassDAOIF mdClass);

  public ServerGeoObjectType create(String json);

  public ServerGeoObjectType create(GeoObjectType geoObjectType);
  
  public ServerGeoObjectType build(Universal universal);

  public GeoObjectType convertAttributeTypes(Universal uni, GeoObjectType gt, MdBusiness mdBusiness);
  
  /**
   * The GeoObjectType is a DTO type, which means it contains data which has been localized to a particular user's session.
   * We need to rebuild this object such that it includes relevant request information (like the correct locale).
   */
  public GeoObjectType buildType(ServerGeoObjectType serverType);
  
  public List<GeoObjectType> getAncestors(String sessionId, String code, String hierarchyCode, Boolean includeInheritedTypes, Boolean includeChild);
  
  public void deleteGeoObjectType(String sessionId, String code);
  
  public AttributeType createAttributeType(String sessionId, String geoObjectTypeCode, String attributeTypeJSON);

  public AttributeType updateAttributeType(String sessionId, String geoObjectTypeCode, String attributeTypeJSON);

  public void deleteAttributeType(String sessionId, String gtId, String attributeName);
  
  public GeoObjectType updateGeoObjectType(String sessionId, String gtJSON);
  
  public GeoObjectType createGeoObjectType(String sessionId, String gtJSON);
  
  public List<GeoObjectType> getGeoObjectTypes(String[] codes, PermissionContext context);
}
