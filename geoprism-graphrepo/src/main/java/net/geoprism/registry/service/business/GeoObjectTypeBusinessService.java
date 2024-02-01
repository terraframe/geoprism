/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Geoprism(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service.business;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.commongeoregistry.adapter.Term;
import org.commongeoregistry.adapter.constants.DefaultAttribute;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.AttributeBooleanType;
import org.commongeoregistry.adapter.metadata.AttributeCharacterType;
import org.commongeoregistry.adapter.metadata.AttributeClassificationType;
import org.commongeoregistry.adapter.metadata.AttributeDateType;
import org.commongeoregistry.adapter.metadata.AttributeFloatType;
import org.commongeoregistry.adapter.metadata.AttributeIntegerType;
import org.commongeoregistry.adapter.metadata.AttributeLocalType;
import org.commongeoregistry.adapter.metadata.AttributeTermType;
import org.commongeoregistry.adapter.metadata.AttributeType;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.RegistryRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.dataaccess.metadata.graph.MdGeoVertexDAO;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.Actor;
import com.runwaysdk.system.Roles;
import com.runwaysdk.system.gis.geo.GeometryType;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.metadata.graph.MdGeoVertex;
import com.runwaysdk.system.gis.metadata.graph.MdGeoVertexQuery;
import com.runwaysdk.system.metadata.MdAttributeBoolean;
import com.runwaysdk.system.metadata.MdAttributeCharacter;
import com.runwaysdk.system.metadata.MdAttributeClassification;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdAttributeDateTime;
import com.runwaysdk.system.metadata.MdAttributeDouble;
import com.runwaysdk.system.metadata.MdAttributeIndices;
import com.runwaysdk.system.metadata.MdAttributeLocalCharacterEmbedded;
import com.runwaysdk.system.metadata.MdAttributeLocalText;
import com.runwaysdk.system.metadata.MdAttributeLong;
import com.runwaysdk.system.metadata.MdAttributeTerm;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.system.metadata.MdGraphClass;

import net.geoprism.ontology.Classifier;
import net.geoprism.registry.ChainInheritanceException;
import net.geoprism.registry.CodeLengthException;
import net.geoprism.registry.DuplicateGeoObjectTypeException;
import net.geoprism.registry.GeoObjectTypeAssignmentException;
import net.geoprism.registry.HierarchicalRelationshipType;
import net.geoprism.registry.HierarchyRootException;
import net.geoprism.registry.InheritedHierarchyAnnotation;
import net.geoprism.registry.TypeInUseException;
import net.geoprism.registry.conversion.GeometryTypeFactory;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.conversion.RegistryAttributeTypeConverter;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.conversion.TermConverter;
import net.geoprism.registry.graph.GeoVertexType;
import net.geoprism.registry.model.Classification;
import net.geoprism.registry.model.ClassificationType;
import net.geoprism.registry.model.GeoObjectMetadata;
import net.geoprism.registry.model.GeoObjectTypeMetadata;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.permission.PermissionContext;
import net.geoprism.registry.service.request.ServiceFactory;

@Service
public class GeoObjectTypeBusinessService implements GeoObjectTypeBusinessServiceIF
{
  @Autowired
  private TransitionEventBusinessServiceIF    tranEventServ;

  @Autowired
  private TransitionBusinessServiceIF         tranServ;

  @Autowired
  private HierarchyTypeBusinessServiceIF      htServ;

  @Autowired
  private ClassificationTypeBusinessServiceIF cTypeService;

  @Autowired
  private ClassificationBusinessServiceIF     cService;

  public GeoObjectTypeBusinessService()
  {
  }

  public List<ServerGeoObjectType> getSubtypes(ServerGeoObjectType sgot)
  {
    List<ServerGeoObjectType> children = new LinkedList<>();

    if (sgot.getIsAbstract())
    {
      MdGeoVertexQuery query = new MdGeoVertexQuery(new QueryFactory());
      query.WHERE(query.getSuperMdVertex().EQ(sgot.getMdVertex().getOid()));

      try (OIterator<? extends MdGeoVertex> iterator = query.getIterator())
      {
        while (iterator.hasNext())
        {
          MdGeoVertex cUniversal = (MdGeoVertex) iterator.next();

          children.add(ServerGeoObjectType.get(MdGeoVertexDAO.get(cUniversal.getOid())));
        }

      }
    }

    return children;
  }

  public Set<ServerHierarchyType> getHierarchiesOfSubTypes(ServerGeoObjectType sgot)
  {
    List<ServerGeoObjectType> subtypes = getSubtypes(sgot);
    Set<ServerHierarchyType> hierarchyTypes = new TreeSet<ServerHierarchyType>(new Comparator<ServerHierarchyType>()
    {
      @Override
      public int compare(ServerHierarchyType o1, ServerHierarchyType o2)
      {
        return o1.getCode().compareTo(o2.getCode());
      }
    });

    for (ServerGeoObjectType type : subtypes)
    {
      hierarchyTypes.addAll(getHierarchies(type, false));
    }

    return hierarchyTypes;
  }

  /**
   * @param sType
   *          Hierarchy Type
   * 
   * @return If this geo object type is the direct (non-inherited) root of the
   *         given hierarchy
   */
  public boolean isRoot(ServerGeoObjectType sgot, ServerHierarchyType sType)
  {
    List<ServerGeoObjectType> roots = htServ.getDirectRootNodes(sType);

    for (ServerGeoObjectType root : roots)
    {
      if (root.getCode().equals(sgot.getType().getCode()))
      {
        return true;
      }
    }

    return false;
  }

  @Transaction
  public InheritedHierarchyAnnotation setInheritedHierarchy(ServerGeoObjectType sgot, ServerHierarchyType forHierarchy, ServerHierarchyType inheritedHierarchy)
  {
    // Ensure that this geo object type is the root geo object type for the "For
    // Hierarchy"
    if (!this.isRoot(sgot, forHierarchy) || sgot.getIsAbstract())
    {
      throw new HierarchyRootException();
    }

    HierarchicalRelationshipType fhrt = forHierarchy.getHierarchicalRelationshipType();
    HierarchicalRelationshipType ihrt = inheritedHierarchy.getHierarchicalRelationshipType();

    if (InheritedHierarchyAnnotation.getByForHierarchical(fhrt) != null)
    {
      throw new UnsupportedOperationException("A hierarchy cannot inherit from more than one other hierarchy");
    }

    if (isRoot(sgot, inheritedHierarchy))
    {
      throw new UnsupportedOperationException("A root node in a hierarchy cannot be inherited");
    }

    // TODO: HEADS UP
    // InheritedHierarchyAnnotation annotation = new
    // InheritedHierarchyAnnotation();
    // annotation.setUniversal(sgot.getUniversal());
    // annotation.setInheritedHierarchicalRelationshipType(ihrt);
    // annotation.setForHierarchicalRelationshipType(fhrt);
    // annotation.apply();
    //
    // return annotation;
    
    return null;
  }

  @Transaction
  public void removeInheritedHierarchy(ServerHierarchyType forHierarchy)
  {
    InheritedHierarchyAnnotation annotation = InheritedHierarchyAnnotation.getByForHierarchical(forHierarchy.getHierarchicalRelationshipType());

    if (annotation != null)
    {
      annotation.delete();
    }
  }

  public List<ServerHierarchyType> getHierarchies(ServerGeoObjectType sgot)
  {
    return getHierarchies(sgot, true);
  }

  public List<ServerHierarchyType> getHierarchies(ServerGeoObjectType sgot, boolean includeFromSuperType)
  {
    // TODO: HEADS UP

    List<ServerHierarchyType> hierarchies = new LinkedList<ServerHierarchyType>();

    // List<ServerHierarchyType> hierarchyTypes =
    // ServiceFactory.getMetadataCache().getAllHierarchyTypes();
    // Universal root = Universal.getRoot();
    //
    // for (ServerHierarchyType hierarchyType : hierarchyTypes)
    // {
    // Organization org = hierarchyType.getOrganization();
    //
    // if
    // (ServiceFactory.getHierarchyPermissionService().canRead(org.getCode()))
    // {
    // if (isRoot(sgot, hierarchyType))
    // {
    // hierarchies.add(hierarchyType);
    // }
    // else
    // {
    // // Note: Ordered ancestors always includes self
    // Collection<?> parents = GeoEntityUtil.getOrderedAncestors(root,
    // sgot.getUniversal(), hierarchyType.getUniversalType());
    //
    // if (parents.size() > 1)
    // {
    // hierarchies.add(hierarchyType);
    // }
    // }
    //
    // }
    // }
    //
    // if (includeFromSuperType)
    // {
    // ServerGeoObjectType superType = sgot.getSuperType();
    //
    // if (superType != null)
    // {
    // hierarchies.addAll(getHierarchies(superType, includeFromSuperType));
    // }
    // }

    return hierarchies;
  }

  public ServerHierarchyType getInheritedHierarchy(ServerGeoObjectType sgot, ServerHierarchyType hierarchy)
  {
    return this.getInheritedHierarchy(sgot, hierarchy.getHierarchicalRelationshipType());
  }

  public ServerHierarchyType getInheritedHierarchy(ServerGeoObjectType sgot, HierarchicalRelationshipType hierarchicalRelationship)
  {
    // TODO: HEADS UP
    // InheritedHierarchyAnnotation annotation =
    // InheritedHierarchyAnnotation.get(sgot.getUniversal(),
    // hierarchicalRelationship);
    //
    // if (annotation != null)
    // {
    // HierarchicalRelationshipType inheritedHierarchicalRelationshipType =
    // annotation.getInheritedHierarchicalRelationshipType();
    //
    // return ServerHierarchyType.get(inheritedHierarchicalRelationshipType);
    // }

    return null;
  }

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
  public List<ServerGeoObjectType> getTypeAncestors(ServerGeoObjectType sgot, ServerHierarchyType hierarchyType, Boolean includeInheritedTypes)
  {
    List<ServerGeoObjectType> ancestors = new LinkedList<ServerGeoObjectType>();

    // TODO: HEADS UP
    // Collection<com.runwaysdk.business.ontology.Term> list =
    // GeoEntityUtil.getOrderedAncestors(Universal.getRoot(),
    // sgot.getUniversal(), hierarchyType.getUniversalType());
    //
    // list.forEach(term -> {
    // Universal parent = (Universal) term;
    //
    // if (!parent.getKeyName().equals(Universal.ROOT) &&
    // !parent.getOid().equals(sgot.getUniversal().getOid()))
    // {
    // ServerGeoObjectType sParent = ServerGeoObjectType.get(parent);
    //
    // ancestors.add(sParent);
    //
    // if (includeInheritedTypes && isRoot(sParent, hierarchyType))
    // {
    // ServerHierarchyType inheritedHierarchy = getInheritedHierarchy(sParent,
    // hierarchyType);
    //
    // if (inheritedHierarchy != null)
    // {
    // ancestors.addAll(0, getTypeAncestors(sParent, inheritedHierarchy,
    // includeInheritedTypes));
    // }
    // }
    // }
    // });
    //
    // if (ancestors.size() == 0)
    // {
    // ServerGeoObjectType superType = sgot.getSuperType();
    //
    // if (superType != null)
    // {
    // return getTypeAncestors(superType, hierarchyType, includeInheritedTypes);
    // }
    // }

    return ancestors;
  }

  /**
   * Finds the actual hierarchy used for the parent type if the parent type is
   * inherited from a different hierarchy
   * 
   * @param hierarchyType
   * @param parent
   * @return
   */
  public ServerHierarchyType findHierarchy(ServerGeoObjectType sgot, ServerHierarchyType hierarchyType, ServerGeoObjectType parent)
  {
    // TODO: HEADS UP
    //
    // Collection<com.runwaysdk.business.ontology.Term> list =
    // GeoEntityUtil.getOrderedAncestors(Universal.getRoot(),
    // sgot.getUniversal(), hierarchyType.getUniversalType());
    //
    // for (Object term : list)
    // {
    // Universal universal = (Universal) term;
    //
    // if (parent.getUniversal().getOid().equals(universal.getOid()))
    // {
    // return hierarchyType;
    // }
    //
    // ServerGeoObjectType sParent = ServerGeoObjectType.get(universal);
    //
    // if (isRoot(sParent, hierarchyType))
    // {
    // ServerHierarchyType inheritedHierarchy = getInheritedHierarchy(sParent,
    // hierarchyType);
    //
    // if (inheritedHierarchy != null)
    // {
    // return findHierarchy(sParent, inheritedHierarchy, parent);
    // }
    // }
    // }
    //
    // return hierarchyType;
    return null;
  }

  public List<ServerGeoObjectType> getChildren(ServerGeoObjectType sgot, ServerHierarchyType hierarchy)
  {
    return htServ.getChildren(hierarchy, sgot);
  }

  @Transaction
  public ServerGeoObjectType create(String json)
  {
    GeoObjectType geoObjectType = GeoObjectType.fromJSON(json, ServiceFactory.getAdapter());

    return this.create(geoObjectType);
  }

  @Transaction
  public ServerGeoObjectType create(GeoObjectType dto)
  {
    if (!isValidName(dto.getCode()))
    {
      throw new AttributeValueException("The geo object type code has an invalid character", dto.getCode());
    }

    if (dto.getCode().length() > 64)
    {
      // Setting the typename on the MdBusiness creates this limitation.
      CodeLengthException ex = new CodeLengthException();
      ex.setLength(64);
      throw ex;
    }

    ServiceFactory.getGeoObjectTypePermissionService().enforceCanCreate(dto.getOrganizationCode(), dto.getIsPrivate());

    String superTypeCode = dto.getSuperTypeCode();
    Boolean isAbstract = dto.getIsAbstract();

    ServerGeoObjectType superType = null;

    if (superTypeCode != null && superTypeCode.length() > 0)
    {
      superType = ServerGeoObjectType.get(superTypeCode);
      dto.setGeometryType(superType.getGeometryType());
    }

    if (isAbstract && superType != null)
    {
      throw new ChainInheritanceException();
    }

    if (superType != null && !superType.getIsAbstract())
    {
      throw new GeoObjectTypeAssignmentException();
    }

    // 1st create the MdVertex
    MdVertexDAO mdVertex = GeoVertexType.create(dto.getCode(), isAbstract, superType);

    GeometryType geometryType = GeometryTypeFactory.get(dto.getGeometryType());

    net.geoprism.registry.graph.GeoObjectType type = new net.geoprism.registry.graph.GeoObjectType();
    type.setGeometryType(geometryType.getEnumName());
    type.setIsAbstract(dto.getIsAbstract());
    type.setIsPrivate(dto.getIsPrivate());
    type.setIsGeometryEditable(dto.isGeometryEditable());
    LocalizedValueConverter.populate(type, net.geoprism.registry.graph.GeoObjectType.LABEL, dto.getLabel());
    LocalizedValueConverter.populate(type, net.geoprism.registry.graph.GeoObjectType.DESCRIPTION, dto.getDescription());
    type.setOrganization(ServerOrganization.getByCode(dto.getOrganizationCode(), true).getGraphOrganization());
    type.setValue(net.geoprism.registry.graph.GeoObjectType.MDVERTEX, mdVertex.getOid());

    if (superType != null)
    {
      type.setSuperType(superType.getType());
    }

    try
    {
      // The DuplicateDataException on code was found to be thrown here.
      // I've created a larger try/catch here just in case.
      type.apply();

      type.createDefaultAttributes();

    }
    catch (DuplicateDataException ex)
    {
      DuplicateGeoObjectTypeException ex2 = new DuplicateGeoObjectTypeException();
      ex2.setDuplicateValue(dto.getCode());
      throw ex2;
    }

    // HEADS UP:
//    // Build the parent class term root if it does not exist.
//    TermConverter.buildIfNotExistdMdBusinessClassifier(mdBusiness);

    return this.build(type);
  }

  /**
   * Assigns all permissions to the {@link ComponentIF} to the given role.
   * 
   * Precondition: component is either a {@link MdGeoVertex} or a
   * {@link MdBusiness}.
   * 
   * @param component
   * @param role
   */
  protected void assignAllPermissions(ComponentIF component, Roles role)
  {
    RoleDAO roleDAO = (RoleDAO) BusinessFacade.getEntityDAO(role);
    roleDAO.grantPermission(Operation.CREATE, component.getOid());
    roleDAO.grantPermission(Operation.DELETE, component.getOid());
    roleDAO.grantPermission(Operation.WRITE, component.getOid());
    roleDAO.grantPermission(Operation.WRITE_ALL, component.getOid());
  }


  public void removeAttribute(ServerGeoObjectType serverType, String attributeName)
  {
    serverType.removeAttribute(attributeName);

    // If this did not error out then add to the cache
    refreshCache(serverType);

    // Refresh the users session
    ( (Session) Session.getCurrentSession() ).reloadPermissions();
  }


  @Override
  public AttributeType updateAttributeType(ServerGeoObjectType serverType, String attributeTypeJSON)
  {
    JsonObject attrObj = JsonParser.parseString(attributeTypeJSON).getAsJsonObject();
    AttributeType attrType = AttributeType.parse(attrObj);

    MdAttributeConcrete mdAttribute = this.updateAttributeTypeFromDTO(serverType.getMdBusiness(), attrType);
    attrType = new RegistryAttributeTypeConverter().build(MdAttributeConcreteDAO.get(mdAttribute.getOid()));

    serverType.getType().addAttribute(attrType);

    // If this did not error out then add to the cache
    refreshCache(serverType);

    return attrType;
  }

  @Override
  public AttributeType createAttributeType(ServerGeoObjectType serverType, AttributeType attributeType)
  {
    MdAttributeConcrete mdAttribute = createAttributeTypeFromDTO(serverType, attributeType);

    attributeType = new RegistryAttributeTypeConverter().build(MdAttributeConcreteDAO.get(mdAttribute.getOid()));

    serverType.getType().addAttribute(attributeType);

    // If this did not error out then add to the cache
    refreshCache(serverType);

    // Refresh the users session
    if (Session.getCurrentSession() != null)
    {
      ( (Session) Session.getCurrentSession() ).reloadPermissions();
    }

    return attributeType;
  }

  @Override
  public AttributeType createAttributeType(ServerGeoObjectType sgot, String attributeTypeJSON)
  {
    JsonObject attrObj = JsonParser.parseString(attributeTypeJSON).getAsJsonObject();

    AttributeType attrType = AttributeType.parse(attrObj);

    return createAttributeType(sgot, attrType);
  }

  private void refreshCache(ServerGeoObjectType type)
  {
    ServiceFactory.getMetadataCache().addGeoObjectType(type);

    // Refresh all of the subtypes
    List<ServerGeoObjectType> subtypes = getSubtypes(type);
    for (ServerGeoObjectType subtype : subtypes)
    {
      ServerGeoObjectType type2 = build(subtype.getUniversal());

      ServiceFactory.getMetadataCache().addGeoObjectType(type2);
    }
  }

  public ServerGeoObjectType build(net.geoprism.registry.graph.GeoObjectType type)
  {
    ServerGeoObjectType server = new ServerGeoObjectType(type);
    buildType(server);

    return server;
  }

  public List<GeoObjectType> getAncestors(String code, String hierarchyCode, Boolean includeInheritedTypes, Boolean includeChild)
  {
    // TODO: HEADS UP
//    ServerGeoObjectType child = ServerGeoObjectType.get(code);
//    ServerHierarchyType hierarchyType = ServerHierarchyType.get(hierarchyCode);
//
//    List<ServerGeoObjectType> ancestors = getTypeAncestors(child, hierarchyType, includeInheritedTypes);
//
//    if (includeChild)
//    {
//      ancestors.add(child);
//    }
//
//    return ancestors.stream().map(stype -> stype.getType()).collect(Collectors.toList());
    
    return null;
  }

  /**
   * Deletes the {@link GeoObjectType} with the given code. Do nothing if the
   * type does not exist.
   * 
   * @param sessionId
   * @param code
   *          code of the {@link GeoObjectType} to delete.
   */

  public void deleteGeoObjectType(String code)
  {
    ServerGeoObjectType type = ServerGeoObjectType.get(code);

    if (type != null)
    {
      delete(type);
    }
  }

  protected void delete(ServerGeoObjectType type)
  {
    ServiceFactory.getGeoObjectTypePermissionService().enforceCanDelete(type.getOrganization().getCode(), type, type.getIsPrivate());

    this.deleteInTransaction(type);

    Session session = (Session) Session.getCurrentSession();

    // If this is being called in a JUnit test scenario then there is no
    // session object in the request.
    if (session != null)
    {
      // Refresh the users session
      session.setUser(Session.getCurrentSession().getUser());
    }

    // If we get here then it was successfully deleted
    // We have to do a full metadata cache
    // refresh because the GeoObjectType is
    // embedded in the HierarchyType
    ServiceFactory.getGraphRepoService().refreshMetadataCache();
  }

  @Transaction
  protected void deleteInTransaction(ServerGeoObjectType type)
  {
    List<ServerHierarchyType> hierarchies = getHierarchies(type, true);

    if (hierarchies.size() > 0)
    {
      StringBuilder codes = hierarchies.stream().collect(StringBuilder::new, (x, y) -> x.append(y.getCode()), (a, b) -> a.append(",").append(b));

      throw new TypeInUseException("Cannot delete a GeoObjectType used in the hierarchies: " + codes);
    }

    /*
     * Delete all subtypes
     */
    List<ServerGeoObjectType> subtypes = getSubtypes(type);

    for (ServerGeoObjectType subtype : subtypes)
    {
      deleteInTransaction(subtype);
    }

    /*
     * Delete all inherited hierarchies
     */
    List<? extends InheritedHierarchyAnnotation> annotations = InheritedHierarchyAnnotation.getByUniversal(type.getUniversal());

    for (InheritedHierarchyAnnotation annotation : annotations)
    {
      annotation.delete();
    }

    GeoVertexType.remove(type.getUniversal().getUniversalId());

    /*
     * Delete all Attribute references
     */
    // AttributeHierarchy.deleteByUniversal(this.universal);

    type.getMetadata().delete();

    // This deletes the {@link MdBusiness} as well
    type.getUniversal().delete(false);

    // Delete the term root
    Classifier classRootTerm = TermConverter.buildIfNotExistdMdBusinessClassifier(type.getMdBusiness());
    classRootTerm.delete();

    // Delete the roles. Sub types don't have direct roles, they only have the
    // roles specified on the super type.
    if (type.getSuperType() == null)
    {
      Actor ownerActor = type.getUniversal().getOwner();

      if (ownerActor instanceof Roles)
      {
        Roles ownerRole = (Roles) ownerActor;
        String roleName = ownerRole.getRoleName();

        if (RegistryRole.Type.isOrgRole(roleName))
        {
          String organizationCode = RegistryRole.Type.parseOrgCode(roleName);

          String geoObjectTypeCode = type.getCode();

          String rmRoleName = RegistryRole.Type.getRM_RoleName(organizationCode, geoObjectTypeCode);
          Roles role = Roles.findRoleByName(rmRoleName);
          role.delete();

          String rcRoleName = RegistryRole.Type.getRC_RoleName(organizationCode, geoObjectTypeCode);
          role = Roles.findRoleByName(rcRoleName);
          role.delete();

          String acRoleName = RegistryRole.Type.getAC_RoleName(organizationCode, geoObjectTypeCode);
          role = Roles.findRoleByName(acRoleName);
          role.delete();
        }
      }
    }

    // Delete the transition and transition events
    tranEventServ.removeAll(type);

    tranServ.removeAll(type);
  }

  /**
   * Adds an attribute to the given {@link GeoObjectType}.
   * 
   * @pre given {@link GeoObjectType} must already exist.
   * 
   * @param sessionId
   *
   * @param geoObjectTypeCode
   *          string of the {@link GeoObjectType} to be updated.
   * @param attributeTypeJSON
   *          AttributeType to be added to the GeoObjectType
   * @return updated {@link GeoObjectType}
   */
  @Override
  public AttributeType createAttributeType(String geoObjectTypeCode, String attributeTypeJSON)
  {
    ServerGeoObjectType got = ServerGeoObjectType.get(geoObjectTypeCode);

    ServiceFactory.getGeoObjectTypePermissionService().enforceCanWrite(got.getOrganization().getCode(), got, got.getIsPrivate());

    AttributeType attrType = createAttributeType(got, attributeTypeJSON);

    return attrType;
  }

  /**
   * Updates an attribute in the given {@link GeoObjectType}.
   * 
   * @pre given {@link GeoObjectType} must already exist.
   * 
   * @param sessionId
   * @param geoObjectTypeCode
   *          string of the {@link GeoObjectType} to be updated.
   * @param attributeTypeJSON
   *          AttributeType to be added to the GeoObjectType
   * @return updated {@link AttributeType}
   */
  @Override
  public AttributeType updateAttributeType(String geoObjectTypeCode, String attributeTypeJSON)
  {
    ServerGeoObjectType got = ServerGeoObjectType.get(geoObjectTypeCode);

    ServiceFactory.getGeoObjectTypePermissionService().enforceCanWrite(got.getOrganization().getCode(), got, got.getIsPrivate());

    AttributeType attrType = updateAttributeType(got, attributeTypeJSON);

    return attrType;
  }

  /**
   * Deletes an attribute from the given {@link GeoObjectType}.
   * 
   * @pre given {@link GeoObjectType} must already exist.
   * @pre given {@link GeoObjectType} must already exist.
   * 
   * @param sessionId
   * @param gtId
   *          string of the {@link GeoObjectType} to be updated.
   * @param attributeName
   *          Name of the attribute to be removed from the GeoObjectType
   * @return updated {@link GeoObjectType}
   */

  public void deleteAttributeType(String gtId, String attributeName)
  {
    ServerGeoObjectType got = ServerGeoObjectType.get(gtId);

    ServiceFactory.getGeoObjectTypePermissionService().enforceCanWrite(got.getOrganization().getCode(), got, got.getIsPrivate());

    removeAttribute(got, attributeName);
  }

  @Override
  @Transaction
  public MdAttributeConcrete createAttributeTypeFromDTO(MdClass mdClass, AttributeType attributeType)
  {
    MdAttributeConcrete mdAttribute = null;

    if (attributeType.getType().equals(AttributeCharacterType.TYPE))
    {
      mdAttribute = new MdAttributeCharacter();
      MdAttributeCharacter mdAttributeCharacter = (MdAttributeCharacter) mdAttribute;
      mdAttributeCharacter.setDatabaseSize(MdAttributeCharacterInfo.MAX_CHARACTER_SIZE);
    }
    else if (attributeType.getType().equals(AttributeDateType.TYPE))
    {
      mdAttribute = new MdAttributeDateTime();
    }
    else if (attributeType.getType().equals(AttributeIntegerType.TYPE))
    {
      mdAttribute = new MdAttributeLong();
    }
    else if (attributeType.getType().equals(AttributeFloatType.TYPE))
    {
      AttributeFloatType attributeFloatType = (AttributeFloatType) attributeType;

      mdAttribute = new MdAttributeDouble();
      mdAttribute.setValue(MdAttributeDoubleInfo.LENGTH, Integer.toString(attributeFloatType.getPrecision()));
      mdAttribute.setValue(MdAttributeDoubleInfo.DECIMAL, Integer.toString(attributeFloatType.getScale()));
    }
    else if (attributeType.getType().equals(AttributeTermType.TYPE))
    {
      mdAttribute = new MdAttributeTerm();
      MdAttributeTerm mdAttributeTerm = (MdAttributeTerm) mdAttribute;

      MdBusiness classifierMdBusiness = MdBusiness.getMdBusiness(Classifier.CLASS);
      mdAttributeTerm.setMdBusiness(classifierMdBusiness);
      // TODO implement support for multi-term
      // mdAttribute = new MdAttributeMultiTerm();
      // MdAttributeMultiTerm mdAttributeMultiTerm =
      // (MdAttributeMultiTerm)mdAttribute;
      //
      // MdBusiness classifierMdBusiness =
      // MdBusiness.getMdBusiness(Classifier.CLASS);
      // mdAttributeMultiTerm.setMdBusiness(classifierMdBusiness);
    }
    else if (attributeType.getType().equals(AttributeClassificationType.TYPE))
    {
      AttributeClassificationType attributeClassificationType = (AttributeClassificationType) attributeType;
      String classificationTypeCode = attributeClassificationType.getClassificationType();

      ClassificationType classificationType = this.cTypeService.getByCode(classificationTypeCode);

      mdAttribute = new MdAttributeClassification();
      MdAttributeClassification mdAttributeTerm = (MdAttributeClassification) mdAttribute;
      mdAttributeTerm.setReferenceMdClassification(classificationType.getMdClassificationObject());

      Term root = attributeClassificationType.getRootTerm();

      if (root != null)
      {
        Classification classification = this.cService.get(classificationType, root.getCode());

        if (classification == null)
        {
          net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
          ex.setTypeLabel(classificationType.getDisplayLabel().getValue());
          ex.setDataIdentifier(root.getCode());
          ex.setAttributeLabel(GeoObjectMetadata.get().getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));

          throw ex;
        }

        mdAttributeTerm.setValue(MdAttributeClassification.ROOT, classification.getOid());
      }
    }
    else if (attributeType.getType().equals(AttributeBooleanType.TYPE))
    {
      mdAttribute = new MdAttributeBoolean();
    }
    else if (attributeType.getType().equals(AttributeLocalType.TYPE))
    {
      if (mdClass instanceof MdGraphClass)
      {
        mdAttribute = new MdAttributeLocalCharacterEmbedded();
      }
      else
      {
        mdAttribute = new MdAttributeLocalText();
      }
    }
    else
    {
      throw new UnsupportedOperationException();
    }

    mdAttribute.setAttributeName(attributeType.getName());
    mdAttribute.setValue(MdAttributeConcreteInfo.REQUIRED, Boolean.toString(attributeType.isRequired()));

    if (attributeType.isUnique())
    {
      mdAttribute.addIndexType(MdAttributeIndices.UNIQUE_INDEX);
    }

    RegistryLocalizedValueConverter.populate(mdAttribute.getDisplayLabel(), attributeType.getLabel());
    RegistryLocalizedValueConverter.populate(mdAttribute.getDescription(), attributeType.getDescription());

    mdAttribute.setDefiningMdClass(mdClass);
    mdAttribute.apply();

    if (attributeType.getType().equals(AttributeTermType.TYPE))
    {
      MdAttributeTerm mdAttributeTerm = (MdAttributeTerm) mdAttribute;

      // Build the parent class term root if it does not exist.
      Classifier classTerm = TermConverter.buildIfNotExistdMdBusinessClassifier(mdClass);

      // Create the root term node for this attribute
      Classifier attributeTermRoot = TermConverter.buildIfNotExistAttribute(mdClass, mdAttributeTerm.getAttributeName(), classTerm);

      // Make this the root term of the multi-attribute
      attributeTermRoot.addClassifierTermAttributeRoots(mdAttributeTerm).apply();

      AttributeTermType attributeTermType = (AttributeTermType) attributeType;

      LocalizedValue label = RegistryLocalizedValueConverter.convert(attributeTermRoot.getDisplayLabel());

      org.commongeoregistry.adapter.Term term = new org.commongeoregistry.adapter.Term(attributeTermRoot.getClassifierId(), label, new LocalizedValue(""));
      attributeTermType.setRootTerm(term);
    }
    return mdAttribute;
  }

  /**
   * Returns the {link MdAttributeConcreteDAOIF} for the given
   * {@link AttributeType} defined on the given {@link MdBusiness} or null no
   * such attribute is defined.
   * 
   * @param attributeName
   * 
   * @return
   */
  @Override
  public MdAttributeConcreteDAOIF getMdAttribute(MdClass mdClass, String attributeName)
  {
    MdClassDAOIF mdClassDAO = (MdClassDAOIF) BusinessFacade.getEntityDAO(mdClass);

    return (MdAttributeConcreteDAOIF) mdClassDAO.definesAttribute(attributeName);
  }

  /**
   * Creates an {@link MdAttributeConcrete} for the given {@link MdBusiness}
   * from the given {@link AttributeType}
   * 
   * @pre assumes no attribute has been defined on the type with the given name.
   * 
   * @param mdBusiness
   *          Type to receive attribute definition
   * @param attributeType
   *          newly defined attribute
   * 
   * @return {@link AttributeType}
   */
  @Override
  @Transaction
  public net.geoprism.registry.graph.AttributeType updateAttributeTypeFromDTO(ServerGeoObjectType type, AttributeType dto)
  {
    Optional<net.geoprism.registry.graph.AttributeType> optional = type.getAttribute(dto.getName());    
    
    if (optional.isPresent())
    {
      net.geoprism.registry.graph.AttributeType attribute = optional.get();
      attribute.fromDTO(dto);
      attribute.apply();
      
      return attribute;
    }
      

    return null;
  }

  /**
   * Updates the given {@link GeoObjectType} represented as JSON.
   * 
   * @pre given {@link GeoObjectType} must already exist.
   * 
   * @param sessionId
   * @param gtJSON
   *          JSON of the {@link GeoObjectType} to be updated.
   * @return updated {@link GeoObjectType}
   */
  public GeoObjectType updateGeoObjectType(String gtJSON)
  {
    GeoObjectType geoObjectType = GeoObjectType.fromJSON(gtJSON, ServiceFactory.getAdapter());
    ServerGeoObjectType serverGeoObjectType = ServerGeoObjectType.get(geoObjectType.getCode());

    ServiceFactory.getGeoObjectTypePermissionService().enforceCanWrite(geoObjectType.getOrganizationCode(), serverGeoObjectType, geoObjectType.getIsPrivate());

    update(serverGeoObjectType, geoObjectType);

    return serverGeoObjectType.getType();
  }

  protected void update(ServerGeoObjectType serverGeoObjectType, GeoObjectType geoObjectTypeNew)
  {
    GeoObjectType geoObjectTypeModified = serverGeoObjectType.getType().copy(geoObjectTypeNew);

    Universal universal = updateInTrans(serverGeoObjectType, geoObjectTypeModified);

    ServerGeoObjectType geoObjectTypeModifiedApplied = build(universal);

    // If this did not error out then add to the cache
    ServiceFactory.getMetadataCache().refreshGeoObjectType(geoObjectTypeModifiedApplied);

    // Modifications to supertypes can affect subtypes (i.e. changing
    // isPrivate). We should refresh them as well.
    if (geoObjectTypeModifiedApplied.getIsAbstract())
    {
      List<ServerGeoObjectType> subtypes = getSubtypes(geoObjectTypeModifiedApplied);

      for (ServerGeoObjectType subtype : subtypes)
      {
        ServerGeoObjectType refreshedSubtype = build(subtype.getUniversal());
        ServiceFactory.getMetadataCache().refreshGeoObjectType(refreshedSubtype);
      }
    }
    serverGeoObjectType.setType(geoObjectTypeModifiedApplied.getType());
    serverGeoObjectType.setUniversal(geoObjectTypeModifiedApplied.getUniversal());
    serverGeoObjectType.setMdBusiness(geoObjectTypeModifiedApplied.getMdBusiness());
  }

  @Transaction
  protected Universal updateInTrans(ServerGeoObjectType serverGeoObjectType, GeoObjectType geoObjectType)
  {
    serverGeoObjectType.getUniversal().lock();

    serverGeoObjectType.getUniversal().setIsGeometryEditable(geoObjectType.isGeometryEditable());
    RegistryLocalizedValueConverter.populate(serverGeoObjectType.getUniversal().getDisplayLabel(), geoObjectType.getLabel());
    RegistryLocalizedValueConverter.populate(serverGeoObjectType.getUniversal().getDescription(), geoObjectType.getDescription());

    serverGeoObjectType.getUniversal().apply();

    MdBusiness mdBusiness = serverGeoObjectType.getUniversal().getMdBusiness();

    mdBusiness.lock();
    mdBusiness.getDisplayLabel().setValue(serverGeoObjectType.getUniversal().getDisplayLabel().getValue());
    mdBusiness.getDescription().setValue(serverGeoObjectType.getUniversal().getDescription().getValue());
    mdBusiness.apply();

    GeoObjectTypeMetadata metadata = serverGeoObjectType.getMetadata();
    if (!metadata.getIsPrivate().equals(geoObjectType.getIsPrivate()))
    {
      metadata.appLock();
      metadata.setIsPrivate(geoObjectType.getIsPrivate());
      metadata.apply();
    }

    mdBusiness.unlock();

    serverGeoObjectType.getUniversal().unlock();

    return serverGeoObjectType.getUniversal();
  }

  /**
   * Creates a {@link GeoObjectType} from the given JSON.
   * 
   * @param sessionId
   * @param gtJSON
   *          JSON of the {@link GeoObjectType} to be created.
   * @return newly created {@link GeoObjectType}
   */

  public GeoObjectType createGeoObjectType(String gtJSON)
  {
    ServerGeoObjectType type = null;

    type = create(gtJSON);

    // Refresh the users session
    ( (Session) Session.getCurrentSession() ).reloadPermissions();

    // If this did not error out then add to the cache
    ServiceFactory.getMetadataCache().addGeoObjectType(type);

    return type.getType();
  }

  /**
   * Returns the {@link GeoObjectType}s with the given codes or all
   * {@link GeoObjectType}s if no codes are provided.
   * 
   * @param codes
   *          codes of the {@link GeoObjectType}s.
   * @param context
   * @return the {@link GeoObjectType}s with the given codes or all
   *         {@link GeoObjectType}s if no codes are provided.
   */
  public List<GeoObjectType> getGeoObjectTypes(String[] codes, PermissionContext context)
  {
    List<ServerGeoObjectType> gots;

    if (codes == null || codes.length == 0)
    {
      gots = ServiceFactory.getMetadataCache().getAllGeoObjectTypes();
    }
    else
    {
      gots = new ArrayList<ServerGeoObjectType>(codes.length);

      for (int i = 0; i < codes.length; ++i)
      {
        Optional<ServerGeoObjectType> optional = ServiceFactory.getMetadataCache().getGeoObjectType(codes[i]);

        if (optional.isPresent())
        {
          gots.add(optional.get());
        }
        else
        {
          net.geoprism.registry.DataNotFoundException ex = new net.geoprism.registry.DataNotFoundException();
          ex.setTypeLabel(GeoObjectTypeMetadata.sGetClassDisplayLabel());
          ex.setDataIdentifier(codes[i]);
          ex.setAttributeLabel(GeoObjectTypeMetadata.getAttributeDisplayLabel(DefaultAttribute.CODE.getName()));
          throw ex;
        }
      }
    }

    Iterator<ServerGeoObjectType> it = gots.iterator();
    while (it.hasNext())
    {
      ServerGeoObjectType serverGot = it.next();

      // Filter ones that they can't see due to permissions
      if (context.equals(PermissionContext.READ))
      {
        if (!ServiceFactory.getGeoObjectTypePermissionService().canRead(serverGot.getOrganization().getCode(), serverGot, serverGot.getIsPrivate()))
        {
          it.remove();
          continue; // If we don't have continue here, then it could invoke
                    // it.remove twice which throws an error.
        }
      }
      else
      {
        if (!ServiceFactory.getGeoObjectTypePermissionService().canWrite(serverGot.getOrganization().getCode(), serverGot, serverGot.getIsPrivate()))
        {
          it.remove();
          continue; // If we don't have continue here, then it could invoke
                    // it.remove twice which throws an error.
        }
      }
    }

    return gots.stream().map(server -> buildType(server)).collect(Collectors.toList());
  }

  public static boolean isValidName(String name)
  {
    if (name.contains(" ") || name.contains("<") || name.contains(">") || name.contains("-") || name.contains("+") || name.contains("=") || name.contains("!") || name.contains("@") || name.contains("#") || name.contains("$") || name.contains("%") || name.contains("^") || name.contains("&") || name.contains("*") || name.contains("?") || name.contains(";") || name.contains(":") || name.contains(",") || name.contains("^") || name.contains("{") || name.contains("}") || name.contains("]") || name.contains("[") || name.contains("`") || name.contains("~") || name.contains("|") || name.contains("/") || name.contains("\\"))
    {
      return false;
    }

    return true;
  }

}
