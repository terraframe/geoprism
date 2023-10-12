package net.geoprism.registry.business;

import java.util.List;
import java.util.Set;

import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.springframework.stereotype.Component;

import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.MdBusiness;

import net.geoprism.registry.HierarchicalRelationshipType;
import net.geoprism.registry.InheritedHierarchyAnnotation;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.permission.PermissionContext;

@Component
public interface GeoObjectTypeBusinessServiceIF
{
  public List<ServerGeoObjectType> getSubtypes(ServerGeoObjectType sgot);

  public Set<ServerHierarchyType> getHierarchiesOfSubTypes(ServerGeoObjectType sgot);

  /**
   * @param sType
   *          Hierarchy Type
   * 
   * @return If this geo object type is the direct (non-inherited) root of the
   *         given hierarchy
   */
  public boolean isRoot(ServerGeoObjectType sgot, ServerHierarchyType sType);

  @Transaction
  public InheritedHierarchyAnnotation setInheritedHierarchy(ServerGeoObjectType sgot, ServerHierarchyType forHierarchy, ServerHierarchyType inheritedHierarchy);

  @Transaction
  public void removeInheritedHierarchy(ServerHierarchyType forHierarchy);

  public List<ServerHierarchyType> getHierarchies(ServerGeoObjectType sgot);

  public List<ServerHierarchyType> getHierarchies(ServerGeoObjectType sgot, boolean includeFromSuperType);

  public ServerHierarchyType getInheritedHierarchy(ServerGeoObjectType sgot, ServerHierarchyType hierarchy);

  public ServerHierarchyType getInheritedHierarchy(ServerGeoObjectType sgot, HierarchicalRelationshipType hierarchicalRelationship);

  /**
   * Returns all ancestors of a GeoObjectType
   * 
   * @param hierarchyType
   *          The Hierarchy code
   * @param includeInheritedTypes
   *          TODO
   * @param GeoObjectType
   *          child
   * 
   * @return
   */
  public List<ServerGeoObjectType> getTypeAncestors(ServerGeoObjectType sgot, ServerHierarchyType hierarchyType, Boolean includeInheritedTypes);

  /**
   * Finds the actual hierarchy used for the parent type if the parent type is
   * inherited from a different hierarchy
   * 
   * @param hierarchyType
   * @param parent
   * @return
   */
  public ServerHierarchyType findHierarchy(ServerGeoObjectType sgot, ServerHierarchyType hierarchyType, ServerGeoObjectType parent);

  public List<ServerGeoObjectType> getChildren(ServerGeoObjectType sgot, ServerHierarchyType hierarchy);

  public void createDefaultAttributes(Universal universal, MdBusiness definingMdBusiness);

  public void createDefaultAttributes(Universal universal, MdGraphClassDAOIF mdClass);

  public ServerGeoObjectType create(String json);

  public ServerGeoObjectType create(GeoObjectType geoObjectType);

  public ServerGeoObjectType build(Universal universal);

  public GeoObjectType convertAttributeTypes(Universal uni, GeoObjectType gt, MdBusiness mdBusiness);

  /**
   * The GeoObjectType is a DTO type, which means it contains data which has
   * been localized to a particular user's session. We need to rebuild this
   * object such that it includes relevant request information (like the correct
   * locale).
   */
  public GeoObjectType buildType(ServerGeoObjectType serverType);

  public List<GeoObjectType> getAncestors(String code, String hierarchyCode, Boolean includeInheritedTypes, Boolean includeChild);

  public void deleteGeoObjectType(String code);

  public AttributeType createAttributeType(String geoObjectTypeCode, String attributeTypeJSON);

  public AttributeType createAttributeType(ServerGeoObjectType type, String attributeTypeJSON);

  public AttributeType createAttributeType(ServerGeoObjectType type, AttributeType attributeType);

  public AttributeType updateAttributeType(String geoObjectTypeCode, String attributeTypeJSON);

  public void deleteAttributeType(String gtId, String attributeName);

  public GeoObjectType updateGeoObjectType(String gtJSON);

  public GeoObjectType createGeoObjectType(String gtJSON);

  public List<GeoObjectType> getGeoObjectTypes(String[] codes, PermissionContext context);

}
