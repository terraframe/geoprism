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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.HierarchyNode;
import org.commongeoregistry.adapter.metadata.HierarchyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.InvalidGeoEntityUniversalException;
import com.runwaysdk.system.metadata.MdEdge;
import com.runwaysdk.system.metadata.MdTermRelationship;

import net.geoprism.rbac.RoleConstants;
import net.geoprism.registry.AbstractParentException;
import net.geoprism.registry.CodeLengthException;
import net.geoprism.registry.DuplicateHierarchyTypeException;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.command.CacheEventType;
import net.geoprism.registry.command.HierarchicalRelationshipTypeCacheEventCommand;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.geoobjecttype.AssignPublicChildOfPrivateType;
import net.geoprism.registry.graph.GeoObjectTypeAlreadyInHierarchyException;
import net.geoprism.registry.graph.GeoVertex;
import net.geoprism.registry.graph.HierarchicalRelationshipType;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.model.graph.GraphTableUtil;
import net.geoprism.registry.service.permission.GeoObjectRelationshipPermissionServiceIF;
import net.geoprism.registry.service.permission.GeoObjectTypePermissionServiceIF;
import net.geoprism.registry.service.permission.HierarchyTypePermissionServiceIF;
import net.geoprism.registry.service.request.ServiceFactory;
import net.geoprism.registry.view.ServerParentTreeNodeOverTime;

@Service
public class HierarchyTypeBusinessService implements HierarchyTypeBusinessServiceIF
{
  @Autowired
  private GeoObjectTypeBusinessServiceIF           typeService;

  @Autowired
  private GeoObjectBusinessServiceIF               objectService;

  @Autowired
  private GeoObjectRelationshipPermissionServiceIF permissions;

  @Override
  public void refresh(ServerHierarchyType sht)
  {
  }

  public void update(ServerHierarchyType sht, HierarchyType dto)
  {
    sht.update(dto);

    refresh(sht);
  }

  public void delete(ServerHierarchyType sht)
  {
    deleteInTrans(sht);

    if (Session.getCurrentSession() != null)
    {
      // Refresh the users session
      ( (Session) Session.getCurrentSession() ).reloadPermissions();
    }
  }

  @Transaction
  protected void deleteInTrans(ServerHierarchyType sht)
  {
    /*
     * Delete all inherited hierarchies
     */
    sht.delete();

    new HierarchicalRelationshipTypeCacheEventCommand(sht, CacheEventType.DELETE).doIt();
  }

  public void addToHierarchy(ServerHierarchyType sht, ServerGeoObjectType parentType, ServerGeoObjectType childType)
  {
    this.addToHierarchy(sht, parentType, childType, true);
  }

  public void addToHierarchy(ServerHierarchyType sht, ServerGeoObjectType parentType, ServerGeoObjectType childType, boolean refresh)
  {
    if (parentType.getIsAbstract())
    {
      AbstractParentException exception = new AbstractParentException();
      exception.setChildGeoObjectTypeLabel(childType.getLabel().getValue());
      exception.setHierarchyTypeLabel(sht.getLabel().getValue());
      exception.setParentGeoObjectTypeLabel(parentType.getLabel().getValue());
      exception.apply();

      throw exception;
    }

    if (parentType.getIsPrivate() && !childType.getIsPrivate())
    {
      AssignPublicChildOfPrivateType ex = new AssignPublicChildOfPrivateType();
      throw ex;
    }

    // Check to see if the child type is already in the hierarchy
    List<ServerHierarchyType> hierarchies = typeService.getHierarchies(childType, true);

    if (hierarchies.contains(sht))
    {
      GeoObjectTypeAlreadyInHierarchyException ex = new GeoObjectTypeAlreadyInHierarchyException();
      ex.setGotCode(childType.getCode());
      throw ex;
    }

    // Ensure a subtype is not already in the hierarchy
    if (childType.getIsAbstract())
    {
      Set<ServerHierarchyType> hierarchiesOfSubTypes = typeService.getHierarchiesOfSubTypes(childType);

      if (hierarchiesOfSubTypes.contains(sht))
      {
        GeoObjectTypeAlreadyInHierarchyException ex = new GeoObjectTypeAlreadyInHierarchyException();
        ex.setGotCode(childType.getCode());
        throw ex;
      }
    }

    sht.addToHierarchy(parentType, childType);

    // No exceptions thrown. Refresh the HierarchyType object to include the new
    // relationships.
    if (refresh)
    {
      this.refresh(sht);
    }
  }

  public void removeChild(ServerHierarchyType sht, ServerGeoObjectType parentType, ServerGeoObjectType childType, boolean migrateChildren)
  {
    this.removeFromHierarchy(sht, parentType, childType, migrateChildren);

    // No exceptions thrown. Refresh the HierarchyType object to include the new
    // relationships.
    this.refresh(sht);
  }

  public List<ServerGeoObjectType> getAllTypes(ServerHierarchyType sht)
  {
    return this.getAllTypes(sht, true);
  }

  public List<ServerGeoObjectType> getAllTypes(ServerHierarchyType sht, boolean includeInherited)
  {
    List<ServerGeoObjectType> types = new LinkedList<ServerGeoObjectType>();

    sht.getRootNodes().forEach(node -> {
      types.addAll(sht.getOrderedDescendants(node));
    });

    java.util.Optional<ServerGeoObjectType> rootOfHierarchy = types.stream().findFirst();
    if (rootOfHierarchy.isPresent() && includeInherited)
    {
      // TODO: HEADS UP
      // ServerGeoObjectType rootType = rootOfHierarchy.get();
      //
      // InheritedHierarchyAnnotation anno =
      // InheritedHierarchyAnnotation.get(rootType,
      // sht);
      //
      // if (anno != null)
      // {
      // HierarchicalRelationshipType hrt =
      // anno.getInheritedHierarchicalRelationshipType();
      // ServerHierarchyType sht2 = ServerHierarchyType.get(hrt);
      //
      // List<ServerGeoObjectType> inheritedTypes =
      // typeService.getTypeAncestors(rootType, sht2, true);
      //
      // types.addAll(0, inheritedTypes);
      // }
    }

    return types;
  }

  public HierarchyType toHierarchyType(ServerHierarchyType sht)
  {
    return this.toHierarchyType(sht, true);
  }

  public HierarchyType toHierarchyType(ServerHierarchyType sht, boolean includePrivateTypes)
  {
    HierarchyType dto = sht.toDTO();

    this.getRootGeoObjectTypes(sht, includePrivateTypes).forEach(rootType -> dto.addRootGeoObjects(rootType));

    return dto;
  }

  public List<ServerGeoObjectType> getChildren(ServerHierarchyType sht, ServerGeoObjectType parent)
  {
    return sht.getChildren(parent);
  }

  protected HierarchyNode buildHierarchy(ServerHierarchyType sht, HierarchyNode parentNode, ServerGeoObjectType parent)
  {
    List<ServerGeoObjectType> children = getChildren(sht, parent);

    for (ServerGeoObjectType child : children)
    {
      HierarchyNode node = new HierarchyNode(child.toDTO());

      node = buildHierarchy(sht, node, child);

      parentNode.addChild(node);
    }

    return parentNode;
  }

  public boolean hasVisibleRoot(ServerHierarchyType sht)
  {
    List<ServerGeoObjectType> roots = this.getDirectRootNodes(sht);

    if (roots.size() > 0)
    {
      final GeoObjectTypePermissionServiceIF typePermServ = ServiceFactory.getGeoObjectTypePermissionService();

      for (ServerGeoObjectType root : roots)
      {
        if (typePermServ.canRead(root.getOrganizationCode(), root, root.getIsPrivate()))
        {
          return true;
        }
      }

      return false;
    }

    return true;
  }

  protected boolean isRootPrivate(ServerHierarchyType sht, HierarchyNode parent)
  {
    final GeoObjectTypePermissionServiceIF typePermServ = ServiceFactory.getGeoObjectTypePermissionService();

    if (parent.getInheritedHierarchyCode() == null || parent.getInheritedHierarchyCode().equals(""))
    {
      GeoObjectType rootGot = parent.getGeoObjectType();

      if (!typePermServ.canRead(rootGot.getOrganizationCode(), ServerGeoObjectType.get(rootGot.getCode()), rootGot.getIsPrivate()))
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else
    {
      for (HierarchyNode child : parent.getChildren())
      {
        if (this.isRootPrivate(sht, child))
        {
          return true;
        }
      }

      return false;
    }
  }

  public List<HierarchyNode> getRootGeoObjectTypes(ServerHierarchyType sht)
  {
    return this.getRootGeoObjectTypes(sht, true);
  }

  public List<HierarchyNode> getRootGeoObjectTypes(ServerHierarchyType sht, boolean includePrivateTypes)
  {
    List<HierarchyNode> rootGeoObjectTypes = new LinkedList<HierarchyNode>();

    List<ServerGeoObjectType> types = this.getDirectRootNodes(sht);

    for (ServerGeoObjectType geoObjectType : types)
    {
      // TODO: HEADS UP
      // ServerHierarchyType inheritedHierarchy =
      // typeService.getInheritedHierarchy(geoObjectType,
      // sht);
      //
      // if (inheritedHierarchy != null)
      // {
      // List<ServerGeoObjectType> ancestors =
      // typeService.getTypeAncestors(geoObjectType, inheritedHierarchy, true);
      // Collections.reverse(ancestors);
      //
      // HierarchyNode child = new HierarchyNode(geoObjectType.toDTO(), null);
      // HierarchyNode root = child;
      //
      // for (ServerGeoObjectType ancestor : ancestors)
      // {
      // HierarchyNode cNode = new HierarchyNode(ancestor.toDTO(),
      // inheritedHierarchy.getCode());
      // cNode.addChild(root);
      //
      // root = cNode;
      // }
      // buildHierarchy(sht, child, geoObjectType);
      // rootGeoObjectTypes.add(root);
      // }
      // else
      // {
      // HierarchyNode node = new HierarchyNode(geoObjectType.toDTO());
      // node = buildHierarchy(sht, node, geoObjectType);
      // rootGeoObjectTypes.add(node);
      // }

    }

    if (!includePrivateTypes)
    {
      Iterator<HierarchyNode> rootIt = rootGeoObjectTypes.iterator();

      while (rootIt.hasNext())
      {
        HierarchyNode hn = rootIt.next();

        if (isRootPrivate(sht, hn))
        {
          rootIt.remove();
        }
        else
        {
          this.filterOutPrivateNodes(hn);
        }
      }
    }

    return rootGeoObjectTypes;
  }

  /**
   * Create the {@link HierarchyType}
   * 
   * @param sessionId
   * @param htJSON
   */
  public ServerHierarchyType createHierarchyType(HierarchyType dto)
  {
    ServiceFactory.getHierarchyPermissionService().enforceCanCreate(dto.getOrganizationCode());

    ServerHierarchyType sht = createHierarchyTypeInTrans(dto);

    Session session = (Session) Session.getCurrentSession();

    if (session != null)
    {
      session.reloadPermissions();
    }

    return sht;
  }

  @Transaction
  protected ServerHierarchyType createHierarchyTypeInTrans(HierarchyType dto)
  {
    if (dto.getOrganizationCode() == null || dto.getOrganizationCode().equals(""))
    {
      // TODO : A better exception
      throw new AttributeValueException("Organization code cannot be null.", dto.getOrganizationCode());
    }

    ServerOrganization organization = ServerOrganization.getByCode(dto.getOrganizationCode());

    String addons = RegistryConstants.UNIVERSAL_RELATIONSHIP_POST + "AllPathsTable";

    if (dto.getCode().length() > ( 64 - addons.length() ))
    {
      // Initializing the Universal allpaths strategy creates this limitation.
      CodeLengthException ex = new CodeLengthException();
      ex.setLength(64 - addons.length());
      throw ex;
    }

    try
    {
      MdEdge objectEdge = this.createObjectMdEdge(dto);
      MdEdge defitionEdge = this.createDefitionMdEdge(dto);

      this.grantWritePermissionsOnMdTermRel(objectEdge);
      this.grantWritePermissionsOnMdTermRel(defitionEdge);

      HierarchicalRelationshipType hierarchicalRelationship = new HierarchicalRelationshipType();
      hierarchicalRelationship.fromDTO(dto);
      hierarchicalRelationship.setObjectEdge(objectEdge);
      hierarchicalRelationship.setDefinitionEdge(defitionEdge);
      hierarchicalRelationship.setOrganization(organization.getGraphOrganization());
      hierarchicalRelationship.apply();

      ServerHierarchyType sht = this.get(hierarchicalRelationship);

      new HierarchicalRelationshipTypeCacheEventCommand(sht, CacheEventType.CREATE).doIt();

      return sht;
    }
    catch (DuplicateDataException ex)
    {
      DuplicateHierarchyTypeException ex2 = new DuplicateHierarchyTypeException();
      ex2.setDuplicateValue(dto.getCode());
      throw ex2;
    }
  }

  /**
   * It creates an {@link MdTermRelationship} to model the relationship between
   * {@link GeoEntity}s.
   * 
   * Needs to occur in a transaction.
   * 
   * @param dto
   * @return
   */
  protected MdEdge createObjectMdEdge(HierarchyType dto)
  {
    MdVertexDAOIF mdBusGeoEntity = MdVertexDAO.getMdVertexDAO(GeoVertex.CLASS);

    MdEdgeDAO mdEdgeDAO = MdEdgeDAO.newInstance();
    mdEdgeDAO.setValue(MdEdgeInfo.PACKAGE, RegistryConstants.UNIVERSAL_GRAPH_PACKAGE);
    mdEdgeDAO.setValue(MdEdgeInfo.NAME, dto.getCode());
    mdEdgeDAO.setValue(MdEdgeInfo.PARENT_MD_VERTEX, mdBusGeoEntity.getOid());
    mdEdgeDAO.setValue(MdEdgeInfo.CHILD_MD_VERTEX, mdBusGeoEntity.getOid());
    LocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DISPLAY_LABEL, dto.getLabel());
    LocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DESCRIPTION, dto.getDescription());
    mdEdgeDAO.setValue(MdEdgeInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
    mdEdgeDAO.apply();

    MdAttributeDateTimeDAO startDate = MdAttributeDateTimeDAO.newInstance();
    startDate.setValue(MdAttributeDateTimeInfo.NAME, GeoVertex.START_DATE);
    startDate.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Date");
    startDate.setStructValue(MdAttributeDateTimeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Date");
    startDate.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS, mdEdgeDAO.getOid());
    startDate.apply();

    MdAttributeDateTimeDAO endDate = MdAttributeDateTimeDAO.newInstance();
    endDate.setValue(MdAttributeDateTimeInfo.NAME, GeoVertex.END_DATE);
    endDate.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "End Date");
    endDate.setStructValue(MdAttributeDateTimeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "End Date");
    endDate.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS, mdEdgeDAO.getOid());
    endDate.apply();

    return (MdEdge) BusinessFacade.get(mdEdgeDAO);
  }

  /**
   * It creates an {@link MdTermRelationship} to model the relationship between
   * {@link GeoEntity}s.
   * 
   * Needs to occur in a transaction.
   * 
   * @param dto
   * @return
   */
  protected MdEdge createDefitionMdEdge(HierarchyType dto)
  {
    MdVertexDAOIF mdGeoObjectType = MdVertexDAO.getMdVertexDAO(net.geoprism.registry.graph.GeoObjectType.CLASS);

    MdEdgeDAO mdEdgeDAO = MdEdgeDAO.newInstance();
    mdEdgeDAO.setValue(MdEdgeInfo.PACKAGE, RegistryConstants.UNIVERSAL_GRAPH_PACKAGE);
    mdEdgeDAO.setValue(MdEdgeInfo.NAME, GraphTableUtil.generateTableName("htg_", "_" + dto.getCode()));
    mdEdgeDAO.setValue(MdEdgeInfo.PARENT_MD_VERTEX, mdGeoObjectType.getOid());
    mdEdgeDAO.setValue(MdEdgeInfo.CHILD_MD_VERTEX, mdGeoObjectType.getOid());
    LocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DISPLAY_LABEL, dto.getLabel());
    LocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DESCRIPTION, dto.getDescription());
    mdEdgeDAO.setValue(MdEdgeInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.FALSE);
    mdEdgeDAO.apply();

    return (MdEdge) BusinessFacade.get(mdEdgeDAO);
  }

  @Override
  public void grantWritePermissionsOnMdTermRel(ComponentIF mdTermRelationship)
  {
    RoleDAO adminRole = RoleDAO.findRole(RoleConstants.ADMIN).getBusinessDAO();

    grantWritePermissionsOnMdTermRel(adminRole, mdTermRelationship);
  }

  @Override
  public void grantWritePermissionsOnMdTermRel(RoleDAO role, ComponentIF mdTermRelationship)
  {
    role.grantPermission(Operation.ADD_PARENT, mdTermRelationship.getOid());
    role.grantPermission(Operation.ADD_CHILD, mdTermRelationship.getOid());
    role.grantPermission(Operation.DELETE_PARENT, mdTermRelationship.getOid());
    role.grantPermission(Operation.DELETE_CHILD, mdTermRelationship.getOid());
    role.grantPermission(Operation.READ_PARENT, mdTermRelationship.getOid());
    role.grantPermission(Operation.READ_CHILD, mdTermRelationship.getOid());
    role.grantPermission(Operation.READ_ALL, mdTermRelationship.getOid());
    role.grantPermission(Operation.READ, mdTermRelationship.getOid());
    role.grantPermission(Operation.WRITE_ALL, mdTermRelationship.getOid());
    role.grantPermission(Operation.WRITE, mdTermRelationship.getOid());
    role.grantPermission(Operation.CREATE, mdTermRelationship.getOid());
    role.grantPermission(Operation.DELETE, mdTermRelationship.getOid());
  }

  @Override
  public void grantReadPermissionsOnMdTermRel(RoleDAO role, ComponentIF mdTermRelationship)
  {
    role.grantPermission(Operation.READ, mdTermRelationship.getOid());
    role.grantPermission(Operation.READ_ALL, mdTermRelationship.getOid());
  }

  @Override
  public ServerHierarchyType get(String code)
  {
    return ServiceFactory.getMetadataCache().getHierachyType(code).orElse(null);
  }

  public ServerHierarchyType get(HierarchicalRelationshipType hierarchicalRelationship)
  {
    return this.get(hierarchicalRelationship, true);
  }

  /**
   * 
   * @param universalRelationship
   * @return
   */
  public ServerHierarchyType get(HierarchicalRelationshipType hierarchicalRelationship, boolean buildHierarchyNodes)
  {

    ServerHierarchyType sht = new ServerHierarchyType(hierarchicalRelationship);

    // if (buildHierarchyNodes)
    // {
    // sht.buildHierarchyNodes();
    // }

    return sht;
  }

  public static boolean isValidName(String name)
  {
    if (name.contains(" ") || name.contains("<") || name.contains(">") || name.contains("-") || name.contains("+") || name.contains("=") || name.contains("!") || name.contains("@") || name.contains("#") || name.contains("$") || name.contains("%") || name.contains("^") || name.contains("&") || name.contains("*") || name.contains("?") || name.contains(";") || name.contains(":") || name.contains(",") || name.contains("^") || name.contains("{") || name.contains("}") || name.contains("]") || name.contains("[") || name.contains("`") || name.contains("~") || name.contains("|") || name.contains("/") || name.contains("\\"))
    {
      return false;
    }

    return true;
  }

  protected void filterOutPrivateNodes(HierarchyNode parent)
  {
    final GeoObjectTypePermissionServiceIF typePermServ = ServiceFactory.getGeoObjectTypePermissionService();
    List<HierarchyNode> list = parent.getChildren();

    Iterator<HierarchyNode> it = list.iterator();
    while (it.hasNext())
    {
      HierarchyNode child = it.next();

      GeoObjectType got = child.getGeoObjectType();

      if (!typePermServ.canRead(got.getOrganizationCode(), ServerGeoObjectType.get(got.getCode()), got.getIsPrivate()))
      {
        it.remove();
      }
      else
      {
        this.filterOutPrivateNodes(child);
      }
    }
  }

  @Transaction
  public void insertBetween(ServerHierarchyType sht, ServerGeoObjectType parentType, ServerGeoObjectType middleType, List<ServerGeoObjectType> youngestTypes)
  {
    this.addToHierarchy(sht, parentType, middleType);

    for (ServerGeoObjectType youngest : youngestTypes)
    {
      this.removeFromHierarchy(sht, parentType, youngest, false);
      this.addToHierarchy(sht, middleType, youngest);
    }
  }

  @Transaction
  protected void removeFromHierarchy(ServerHierarchyType sht, ServerGeoObjectType parentType, ServerGeoObjectType childType, boolean migrateChildren)
  {

    // TODO: Heads up
    // List<? extends InheritedHierarchyAnnotation> annotations =
    // InheritedHierarchyAnnotation.getByInheritedHierarchy(childType,
    // sht);
    //
    // if (annotations.size() > 0)
    // {
    // List<String> codes = new ArrayList<String>();
    //
    // for (InheritedHierarchyAnnotation annot : annotations)
    // {
    // String code =
    // ServerHierarchyType.buildHierarchyKeyFromMdTermRelUniversal(annot.getForHierarchy().getKey());
    // codes.add(code);
    // }
    //
    // CantRemoveInheritedGOT ex = new CantRemoveInheritedGOT();
    // ex.setGotCode(childType.getCode());
    // ex.setHierCode(sht.getCode());
    // ex.setInheritedHierarchyList(StringUtils.join(codes, ", "));
    // throw ex;
    //
    // }

    // If the child type is the root of the hierarchy then determine if
    // removing it will push up a child node to the root which is used in an
    // inherited hierarchy. If so we must prevent this, because the inherited
    // hierarchy model assumes that the inherited node is not the root of the
    // inherited hierarchy.
    // TODO: HEADS UP
    // if (parentType instanceof RootGeoObjectType)
    // {
    // List<ServerGeoObjectType> children = typeService.getChildren(childType,
    // sht);
    //
    // if (children.size() == 1)
    // {
    // ServerGeoObjectType nextRoot = children.get(0);
    //
    // List<? extends InheritedHierarchyAnnotation> results =
    // InheritedHierarchyAnnotation.getByInheritedHierarchy(nextRoot,
    // sht);
    //
    // if (results.size() > 0)
    // {
    // throw new RootNodeCannotBeInheritedException("Cannot remove the root
    // Geo-Object Type of a hierarchy if the new root Geo-Object Type is
    // inherited by another hierarchy");
    // }
    // }
    // }

    sht.removeFromHierarchy(parentType, childType, migrateChildren);

    objectService.removeAllEdges(sht, childType);

    // InheritedHierarchyAnnotation annotation =
    // InheritedHierarchyAnnotation.get(childType,
    // sht);
    //
    // if (annotation != null)
    // {
    // annotation.delete();
    // }
  }

  public List<ServerGeoObjectType> getDirectRootNodes(ServerHierarchyType sht)
  {
    return sht.getRootNodes();
  }

  public void validateUniversalRelationship(ServerHierarchyType sht, ServerGeoObjectType childType, ServerGeoObjectType parentType)
  {
    // Total hack for super types
    List<ServerGeoObjectType> ancestors = sht.getAncestors(childType);

    if (!ancestors.contains(parentType))
    {
      ServerGeoObjectType superType = childType.getSuperType();

      if (superType != null)
      {
        ancestors = sht.getAncestors(superType);
      }
    }

    if (!ancestors.contains(parentType))
    {
      InvalidGeoEntityUniversalException exception = new InvalidGeoEntityUniversalException();
      exception.setChildUniversal(childType.getLabel().getValue());
      exception.setParentUniversal(parentType.getLabel().getValue());
      exception.apply();

      throw exception;
    }
  }

  @Override
  public JsonArray getHierarchiesForGeoObjectOverTime(String code, String typeCode)
  {
    ServerGeoObjectIF geoObject = this.objectService.getGeoObjectByCode(code, typeCode);
    ServerParentTreeNodeOverTime pot = this.objectService.getParentsOverTime(geoObject, null, true, true);

    this.filterHierarchiesFromPermissions(geoObject.getType(), pot);

    return pot.toJSON();
  }

  @Override
  public void filterHierarchiesFromPermissions(ServerGeoObjectType type, ServerParentTreeNodeOverTime pot)
  {
    Collection<ServerHierarchyType> hierarchies = pot.getHierarchies();

    // Boolean isCR = ServiceFactory.getRolePermissionService().isRC() ||
    // ServiceFactory.getRolePermissionService().isAC();

    for (ServerHierarchyType hierarchy : hierarchies)
    {
      ServerOrganization organization = hierarchy.getOrganization();

      // if ( ( isCR && !service.canAddChildCR(organization.getCode(), null,
      // type) ) || ( !isCR && !service.canAddChild(organization.getCode(),
      // null, type) ))
      if (!this.permissions.canViewChild(organization.getCode(), null, type))
      {
        pot.remove(hierarchy);
      }
    }
  }

  @Override
  public JsonArray getHierarchiesForType(String code, Boolean includeTypes)
  {
    ServerGeoObjectType geoObjectType = ServerGeoObjectType.get(code);

    List<ServerHierarchyType> hierarchyTypes = ServerHierarchyType.getAll();

    JsonArray hierarchies = new JsonArray();

    HierarchyTypePermissionServiceIF htpService = ServiceFactory.getHierarchyPermissionService();
    GeoObjectRelationshipPermissionServiceIF grpService = ServiceFactory.getGeoObjectRelationshipPermissionService();

    for (ServerHierarchyType sHT : hierarchyTypes)
    {
      if (htpService.canRead(sHT.getOrganizationCode()))
      {
        List<ServerGeoObjectType> parents = this.typeService.getTypeAncestors(geoObjectType, sHT, true);

        if (parents.size() > 0 || this.typeService.isRoot(geoObjectType, sHT))
        {
          JsonObject object = new JsonObject();
          object.addProperty("code", sHT.getCode());
          object.addProperty("label", sHT.getLabel().getValue());

          if (includeTypes)
          {
            JsonArray pArray = new JsonArray();

            for (ServerGeoObjectType pType : parents)
            {
              if (!pType.getCode().equals(geoObjectType.getCode()) && grpService.canViewChild(sHT.getOrganizationCode(), null, pType))
              {
                JsonObject pObject = new JsonObject();
                pObject.addProperty("code", pType.getCode());
                pObject.addProperty("label", pType.getLabel().getValue());

                pArray.add(pObject);
              }
            }

            object.add("parents", pArray);
          }

          hierarchies.add(object);
        }
      }
    }

    if (hierarchies.size() == 0)
    {
      for (ServerHierarchyType sHT : hierarchyTypes)
      {
        if (htpService.canWrite(sHT.getOrganizationCode()))
        {
          if (this.typeService.isRoot(geoObjectType, sHT))
          {
            JsonObject object = new JsonObject();
            object.addProperty("code", sHT.getCode());
            object.addProperty("label", sHT.getLabel().getValue());
            object.add("parents", new JsonArray());

            hierarchies.add(object);
          }
        }
      }
    }

    return hierarchies;
  }

}
