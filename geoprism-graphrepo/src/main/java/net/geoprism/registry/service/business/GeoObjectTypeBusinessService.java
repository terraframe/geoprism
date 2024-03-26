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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
import com.orientechnologies.orient.core.exception.OConcurrentModificationException;
import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.Roles;
import com.runwaysdk.system.gis.metadata.graph.MdGeoVertex;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdClass;

import net.geoprism.registry.ChainInheritanceException;
import net.geoprism.registry.CodeLengthException;
import net.geoprism.registry.DuplicateGeoObjectTypeException;
import net.geoprism.registry.GeoObjectTypeAssignmentException;
import net.geoprism.registry.HierarchyRootException;
import net.geoprism.registry.TypeInUseException;
import net.geoprism.registry.command.CacheEventType;
import net.geoprism.registry.command.GeoObjectTypeCacheEventCommand;
import net.geoprism.registry.command.HierarchicalRelationshipTypeCacheEventCommand;
import net.geoprism.registry.conversion.TermConverter;
import net.geoprism.registry.graph.GeoVertexType;
import net.geoprism.registry.graph.HierarchicalRelationshipType;
import net.geoprism.registry.graph.InheritedHierarchyAnnotation;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.permission.PermissionContext;

@Service
public class GeoObjectTypeBusinessService implements GeoObjectTypeBusinessServiceIF
{
  @Autowired
  private TransitionEventBusinessServiceIF tranEventServ;

  @Autowired
  private TransitionBusinessServiceIF      tranServ;

  @Autowired
  private HierarchyTypeBusinessServiceIF   htServ;

  @Override
  public List<ServerGeoObjectType> getSubtypes(ServerGeoObjectType type)
  {
    if (type.getIsAbstract())
    {
      return type.getSubTypes();
    }

    return new LinkedList<>();
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

    HierarchicalRelationshipType fhrt = forHierarchy.getObject();
    HierarchicalRelationshipType ihrt = inheritedHierarchy.getObject();

    if (InheritedHierarchyAnnotation.getByForHierarchical(fhrt) != null)
    {
      throw new UnsupportedOperationException("A hierarchy cannot inherit from more than one other hierarchy");
    }

    if (isRoot(sgot, inheritedHierarchy))
    {
      throw new UnsupportedOperationException("A root node in a hierarchy cannot be inherited");
    }

    InheritedHierarchyAnnotation annotation = new InheritedHierarchyAnnotation();
    annotation.setGeoObjectType(sgot.getType());
    annotation.setInheritedHierarchy(ihrt);
    annotation.setForHierarchy(fhrt);
    annotation.apply();

    new HierarchicalRelationshipTypeCacheEventCommand(forHierarchy, CacheEventType.UPDATE).doIt();

    return annotation;

  }

  @Transaction
  public void removeInheritedHierarchy(ServerHierarchyType forHierarchy)
  {
    InheritedHierarchyAnnotation annotation = InheritedHierarchyAnnotation.getByForHierarchical(forHierarchy.getObject());

    if (annotation != null)
    {
      annotation.delete();

      new HierarchicalRelationshipTypeCacheEventCommand(forHierarchy, CacheEventType.UPDATE).doIt();
    }
  }

  public List<ServerHierarchyType> getHierarchies(ServerGeoObjectType sgot)
  {
    return getHierarchies(sgot, true);
  }

  public List<ServerHierarchyType> getHierarchies(ServerGeoObjectType sgot, boolean includeFromSuperType)
  {
    List<ServerHierarchyType> hierarchies = new LinkedList<ServerHierarchyType>();

    // Hack to only get the ServerHierarchyType that are in the database and not
    // in the cache. They are never different in the course of normal usage,
    // just in the test setup
    HierarchicalRelationshipType.getAll().stream().map(t -> ServerHierarchyType.get(t.getCode())).forEach(hierarchyType -> {
      ServerOrganization org = hierarchyType.getOrganization();

      if (ServiceFactory.getHierarchyPermissionService().canRead(org.getCode()))
      {
        if (isRoot(sgot, hierarchyType))
        {
          hierarchies.add(hierarchyType);
        }
        else
        {
          List<ServerGeoObjectType> parents = hierarchyType.getAncestors(sgot);

          if (parents.size() > 1)
          {
            hierarchies.add(hierarchyType);
          }
        }

      }
    });

    if (includeFromSuperType)
    {
      ServerGeoObjectType superType = sgot.getSuperType();

      if (superType != null)
      {
        hierarchies.addAll(getHierarchies(superType, includeFromSuperType));
      }
    }

    return hierarchies;
  }

  public ServerHierarchyType getInheritedHierarchy(ServerGeoObjectType sgot, ServerHierarchyType hierarchy)
  {
    return this.getInheritedHierarchy(sgot, hierarchy.getObject());
  }

  public ServerHierarchyType getInheritedHierarchy(ServerGeoObjectType sgot, HierarchicalRelationshipType hierarchicalRelationship)
  {
    InheritedHierarchyAnnotation annotation = InheritedHierarchyAnnotation.get(sgot.getType(), hierarchicalRelationship);

    if (annotation != null)
    {
      return ServerHierarchyType.get(annotation.getInheritedHierarchyCode());
    }

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

    List<ServerGeoObjectType> list = hierarchyType.getAncestors(sgot);

    list.forEach(parent -> {

      if (!parent.getOid().equals(sgot.getOid()))
      {
        ancestors.add(parent);

        if (includeInheritedTypes && isRoot(parent, hierarchyType))
        {
          ServerHierarchyType inheritedHierarchy = getInheritedHierarchy(parent, hierarchyType);

          if (inheritedHierarchy != null)
          {
            ancestors.addAll(0, getTypeAncestors(parent, inheritedHierarchy, includeInheritedTypes));
          }
        }
      }
    });

    if (ancestors.size() == 0)
    {
      ServerGeoObjectType superType = sgot.getSuperType();

      if (superType != null)
      {
        return getTypeAncestors(superType, hierarchyType, includeInheritedTypes);
      }
    }
    
    Collections.reverse(ancestors);
    
    return  ancestors;
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
    List<ServerGeoObjectType> ancestors = hierarchyType.getAncestors(sgot);

    for (ServerGeoObjectType ancestor : ancestors)
    {
      if (parent.getOid().equals(ancestor.getOid()))
      {
        return hierarchyType;
      }

      if (isRoot(ancestor, hierarchyType))
      {
        ServerHierarchyType inheritedHierarchy = getInheritedHierarchy(ancestor, hierarchyType);

        if (inheritedHierarchy != null)
        {
          return findHierarchy(ancestor, inheritedHierarchy, parent);
        }
      }
    }

    return hierarchyType;
  }

  public List<ServerGeoObjectType> getChildren(ServerGeoObjectType sgot, ServerHierarchyType hierarchy)
  {
    return htServ.getChildren(hierarchy, sgot);
  }

  public ServerGeoObjectType create(String json)
  {
    GeoObjectType dto = GeoObjectType.fromJSON(json, ServiceFactory.getAdapter());

    ServerGeoObjectType type = this.create(dto);

    // Refresh the users session
    ( (Session) Session.getCurrentSession() ).reloadPermissions();

    return type;
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
    ServerOrganization organization = ServerOrganization.getByCode(dto.getOrganizationCode(), true);

    net.geoprism.registry.graph.GeoObjectType type = new net.geoprism.registry.graph.GeoObjectType();
    type.setRootTerm(TermConverter.buildIfNotExistGeoObjectTypeClassifier(type));
    type.setOrganization(organization.getGraphOrganization());
    type.setValue(net.geoprism.registry.graph.GeoObjectType.MDVERTEX, mdVertex.getOid());
    type.setDbClassName(mdVertex.getDBClassName());
    type.fromDTO(dto);

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

    ServerGeoObjectType sType = new ServerGeoObjectType(type);

    new GeoObjectTypeCacheEventCommand(sType, CacheEventType.CREATE).doIt();

    return sType;
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

    new GeoObjectTypeCacheEventCommand(serverType, CacheEventType.UPDATE).doIt();

    // If this did not error out then add to the cache
    // Refresh the users session
    Session session = (Session) Session.getCurrentSession();

    if (session != null)
    {
      session.reloadPermissions();
    }
  }

  @Override
  public <T extends AttributeType> T updateAttributeType(ServerGeoObjectType serverType, String attributeTypeJSON)
  {
    JsonObject attrObj = JsonParser.parseString(attributeTypeJSON).getAsJsonObject();
    AttributeType dto = AttributeType.parse(attrObj);

    return updateAttributeType(serverType, dto);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends AttributeType> T updateAttributeType(ServerGeoObjectType serverType, AttributeType dto)
  {
    net.geoprism.registry.graph.AttributeType attributeType = this.updateAttributeTypeFromDTO(serverType, dto);
    dto = attributeType.toDTO();

    return (T) dto;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends AttributeType> T createAttributeType(ServerGeoObjectType serverType, AttributeType dto)
  {
    net.geoprism.registry.graph.AttributeType attributeType = createAttributeTypeFromDTO(serverType, dto);

    dto = attributeType.toDTO();

    // Refresh the users session
    if (Session.getCurrentSession() != null)
    {
      ( (Session) Session.getCurrentSession() ).reloadPermissions();
    }

    return (T) dto;
  }

  @Override
  public <T extends AttributeType> T createAttributeType(ServerGeoObjectType sgot, String attributeTypeJSON)
  {
    JsonObject attrObj = JsonParser.parseString(attributeTypeJSON).getAsJsonObject();

    AttributeType attrType = AttributeType.parse(attrObj);

    return createAttributeType(sgot, attrType);
  }

  public List<GeoObjectType> getAncestors(String code, String hierarchyCode, Boolean includeInheritedTypes, Boolean includeChild)
  {
    ServerGeoObjectType child = ServerGeoObjectType.get(code);
    ServerHierarchyType hierarchyType = ServerHierarchyType.get(hierarchyCode);

    List<ServerGeoObjectType> ancestors = getTypeAncestors(child, hierarchyType, includeInheritedTypes);

    if (includeChild)
    {
      ancestors.add(child);
    }

    return ancestors.stream().map(stype -> stype.toDTO()).collect(Collectors.toList());
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
    ServiceFactory.getGeoObjectTypePermissionService().enforceCanDelete(type.getOrganizationCode(), type, type.getIsPrivate());

    /*
     * In a multi-threaded import it is likely to get an
     * OCurrentModificationException because adding a link to the parent geo
     * object adds a pointer to the parent vertex and causes an optimistic lock
     * check on the parent vertex. So if multiple geo-objects are assigned to
     * the same parent at the same time the system will throw a
     * OConcurrentModificationException. Retry the commit again.
     */
    for (int i = 0; i < 100; i++)
    {
      try
      {
        this.deleteInTransaction(type);

        break;
      }
      catch (ProgrammingErrorException e)
      {
        if (! ( e.getCause() instanceof OConcurrentModificationException ))
        {
          throw e;
        }
        else
        {
          try
          {
            Thread.sleep(100);
          }
          catch (InterruptedException e1)
          {
          }
        }
      }
    }

    Session session = (Session) Session.getCurrentSession();

    // If this is being called in a JUnit test scenario then there is no
    // session object in the request.
    if (session != null)
    {
      // Refresh the users session
      session.setUser(Session.getCurrentSession().getUser());
    }
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
    List<? extends InheritedHierarchyAnnotation> annotations = InheritedHierarchyAnnotation.getByGeoObjectType(type.getType());

    for (InheritedHierarchyAnnotation annotation : annotations)
    {
      annotation.delete();
    }

    //
    // GeoVertexType.remove(type.getUniversal().getUniversalId());
    //
    // /*
    // * Delete all Attribute references
    // */
    // // AttributeHierarchy.deleteByUniversal(this.universal);
    //
    // type.getMetadata().delete();
    //
    // // This deletes the {@link MdBusiness} as well
    // type.getUniversal().delete(false);
    //

    // Delete the roles. Sub types don't have direct roles, they only have the
    // roles specified on the super type.
    if (type.getSuperType() == null)
    {
      String organizationCode = type.getOrganizationCode();

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

    // Delete the transition and transition events
    tranEventServ.removeAll(type);

    tranServ.removeAll(type);

    type.delete();

    // // Delete the term root
    // Classifier term = type.getType().getRootTerm();
    //
    // if (term != null)
    // {
    // term.delete();
    // }

    new GeoObjectTypeCacheEventCommand(type, CacheEventType.DELETE).doIt();
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
  @SuppressWarnings("unchecked")
  @Override
  public <T extends AttributeType> T createAttributeType(String geoObjectTypeCode, String attributeTypeJSON)
  {
    ServerGeoObjectType got = ServerGeoObjectType.get(geoObjectTypeCode);

    ServiceFactory.getGeoObjectTypePermissionService().enforceCanWrite(got.getOrganizationCode(), got, got.getIsPrivate());

    AttributeType dto = createAttributeType(got, attributeTypeJSON);

    return (T) dto;
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
  @SuppressWarnings("unchecked")
  @Override
  public <T extends AttributeType> T updateAttributeType(String geoObjectTypeCode, String attributeTypeJSON)
  {
    ServerGeoObjectType got = ServerGeoObjectType.get(geoObjectTypeCode);

    ServiceFactory.getGeoObjectTypePermissionService().enforceCanWrite(got.getOrganization().getCode(), got, got.getIsPrivate());

    AttributeType attrType = updateAttributeType(got, attributeTypeJSON);

    return (T) attrType;
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
  @Override
  public void deleteAttributeType(String gtId, String attributeName)
  {
    this.deleteAttributeType(ServerGeoObjectType.get(gtId), attributeName);
  }

  @Override
  public void deleteAttributeType(ServerGeoObjectType type, String attributeName)
  {
    ServiceFactory.getGeoObjectTypePermissionService().enforceCanWrite(type.getOrganization().getCode(), type, type.getIsPrivate());

    removeAttribute(type, attributeName);
  }

  @Transaction
  public net.geoprism.registry.graph.AttributeType createAttributeTypeFromDTO(ServerGeoObjectType type, AttributeType dto)
  {
    if (type.getAttributeMap().containsKey(dto.getName()))
    {
      // TODO: Change exception type
      throw new UnsupportedOperationException("Duplicate attribute");
    }

    net.geoprism.registry.graph.AttributeType attributeType = null;

    if (dto.getType().equals(AttributeCharacterType.TYPE))
    {
      attributeType = new net.geoprism.registry.graph.AttributeCharacterType();
    }
    else if (dto.getType().equals(AttributeDateType.TYPE))
    {
      attributeType = new net.geoprism.registry.graph.AttributeDateType();
    }
    else if (dto.getType().equals(AttributeIntegerType.TYPE))
    {
      attributeType = new net.geoprism.registry.graph.AttributeLongType();
    }
    else if (dto.getType().equals(AttributeFloatType.TYPE))
    {
      attributeType = new net.geoprism.registry.graph.AttributeDoubleType();
    }
    else if (dto.getType().equals(AttributeTermType.TYPE))
    {
      attributeType = new net.geoprism.registry.graph.AttributeTermType();
    }
    else if (dto.getType().equals(AttributeClassificationType.TYPE))
    {
      attributeType = new net.geoprism.registry.graph.AttributeClassificationType();
    }
    else if (dto.getType().equals(AttributeBooleanType.TYPE))
    {
      attributeType = new net.geoprism.registry.graph.AttributeBooleanType();
    }
    else if (dto.getType().equals(AttributeLocalType.TYPE))
    {
      attributeType = new net.geoprism.registry.graph.AttributeLocalType();
    }
    else
    {
      throw new UnsupportedOperationException();
    }

    attributeType.setGeoObjectType(type.getType());
    attributeType.fromDTO(dto);
    attributeType.setIsDefault(false);
    attributeType.apply();

    new GeoObjectTypeCacheEventCommand(type, CacheEventType.UPDATE).doIt();

    // mdAttribute.setAttributeName(dto.getName());
    // mdAttribute.setValue(MdAttributeConcreteInfo.REQUIRED,
    // Boolean.toString(dto.isRequired()));
    //
    // if (dto.isUnique())
    // {
    // mdAttribute.addIndexType(MdAttributeIndices.UNIQUE_INDEX);
    // }
    //
    // RegistryLocalizedValueConverter.populate(mdAttribute.getDisplayLabel(),
    // dto.getLabel());
    // RegistryLocalizedValueConverter.populate(mdAttribute.getDescription(),
    // dto.getDescription());
    //
    // mdAttribute.setDefiningMdClass(mdClass);
    // mdAttribute.apply();
    //
    return attributeType;
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
  @Transaction
  public net.geoprism.registry.graph.AttributeType updateAttributeTypeFromDTO(ServerGeoObjectType type, AttributeType dto)
  {
    Optional<net.geoprism.registry.graph.AttributeType> optional = type.getAttribute(dto.getName());

    if (optional.isPresent())
    {
      net.geoprism.registry.graph.AttributeType attribute = optional.get();
      attribute.fromDTO(dto);
      attribute.apply();

      new GeoObjectTypeCacheEventCommand(type, CacheEventType.UPDATE).doIt();

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
    GeoObjectType dto = GeoObjectType.fromJSON(gtJSON, ServiceFactory.getAdapter());
    ServerGeoObjectType type = ServerGeoObjectType.get(dto.getCode());

    return this.update(type, dto);
  }

  protected GeoObjectType update(ServerGeoObjectType type, GeoObjectType dto)
  {
    ServiceFactory.getGeoObjectTypePermissionService().enforceCanWrite(dto.getOrganizationCode(), type, dto.getIsPrivate());

    updateFromDTO(type, dto);

    return type.toDTO();
  }

  @Transaction
  protected void updateFromDTO(ServerGeoObjectType serverGeoObjectType, GeoObjectType dto)
  {
    net.geoprism.registry.graph.GeoObjectType type = serverGeoObjectType.getType();
    type.fromDTO(dto);
    type.apply();

    new GeoObjectTypeCacheEventCommand(serverGeoObjectType, CacheEventType.UPDATE).doIt();
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
    return this.create(gtJSON).toDTO();
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
      gots = new LinkedList<>(ServerGeoObjectType.getAll());
    }
    else
    {
      gots = new ArrayList<ServerGeoObjectType>(codes.length);

      for (int i = 0; i < codes.length; ++i)
      {
        gots.add(ServerGeoObjectType.get(codes[i], false));
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

    return gots.stream().map(server -> server.toDTO()).collect(Collectors.toList());
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
