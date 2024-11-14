/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.registry.service.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.commongeoregistry.adapter.dataaccess.LocalizedValue;
import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.HierarchyNode;
import org.commongeoregistry.adapter.metadata.HierarchyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.ontology.InitializationStrategyIF;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.constants.GISConstants;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.InvalidGeoEntityUniversalException;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.metadata.AssociationType;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdEdge;
import com.runwaysdk.system.metadata.MdTermRelationship;
import com.runwaysdk.system.metadata.RelationshipCache;

import net.geoprism.ontology.GeoEntityUtil;
import net.geoprism.rbac.RoleConstants;
import net.geoprism.registry.AbstractParentException;
import net.geoprism.registry.CodeLengthException;
import net.geoprism.registry.DuplicateHierarchyTypeException;
import net.geoprism.registry.HierarchicalRelationshipType;
import net.geoprism.registry.InheritedHierarchyAnnotation;
import net.geoprism.registry.NoChildForLeafGeoObjectType;
import net.geoprism.registry.Organization;
import net.geoprism.registry.RegistryConstants;
import net.geoprism.registry.RootNodeCannotBeInheritedException;
import net.geoprism.registry.conversion.LocalizedValueConverter;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.geoobjecttype.AssignPublicChildOfPrivateType;
import net.geoprism.registry.graph.CantRemoveInheritedGOT;
import net.geoprism.registry.graph.GeoObjectTypeAlreadyInHierarchyException;
import net.geoprism.registry.graph.GeoVertex;
import net.geoprism.registry.model.RootGeoObjectType;
import net.geoprism.registry.model.ServerGeoObjectIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
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

  public void refresh(ServerHierarchyType sht)
  {
    sht.setHierarchicalRelationshipType(HierarchicalRelationshipType.getByCode(sht.getHierarchicalRelationshipType().getCode()));

    ServiceFactory.getMetadataCache().addHierarchyType(sht, this.toHierarchyType(sht));
  }

  public void update(ServerHierarchyType sht, HierarchyType hierarchyType)
  {
    sht.getHierarchicalRelationshipType().update(hierarchyType);

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

    // No error at this point so the transaction completed successfully.
    ServiceFactory.getMetadataCache().removeHierarchyType(sht.getCode());
  }

  @Transaction
  protected void deleteInTrans(ServerHierarchyType sht)
  {
    /*
     * Delete all inherited hierarchies
     */
    sht.getHierarchicalRelationshipType().delete();
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
      exception.setChildGeoObjectTypeLabel(childType.getUniversal().getDisplayLabel().getValue());
      exception.setHierarchyTypeLabel(sht.getDisplayLabel().getValue());
      exception.setParentGeoObjectTypeLabel(parentType.getUniversal().getDisplayLabel().getValue());
      exception.apply();

      throw exception;
    }

    if (parentType.getUniversal().getIsLeafType())
    {
      NoChildForLeafGeoObjectType exception = new NoChildForLeafGeoObjectType();

      exception.setChildGeoObjectTypeLabel(childType.getUniversal().getDisplayLabel().getValue());
      exception.setHierarchyTypeLabel(sht.getDisplayLabel().getValue());
      exception.setParentGeoObjectTypeLabel(parentType.getUniversal().getDisplayLabel().getValue());
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

    sht.getHierarchicalRelationshipType().addToHierarchy(parentType, childType);

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

    Universal rootUniversal = Universal.getByKey(Universal.ROOT);

    // try (OIterator<? extends Business> i =
    // rootUniversal.getAllDescendants(sht.getHierarchicalRelationshipType().getMdTermRelationship().definesType()))
    // {
    // i.forEach(u -> types.add(ServerGeoObjectType.get((Universal) u)));
    // }

    GeoEntityUtil.getOrderedDescendants(rootUniversal, sht.getHierarchicalRelationshipType().getMdTermRelationship().definesType()).forEach(universal -> {
      if (!universal.getKey().equals(rootUniversal.getKey()))
        types.add(ServerGeoObjectType.get((Universal) universal));
    });

    java.util.Optional<ServerGeoObjectType> rootOfHierarchy = types.stream().findFirst();
    if (rootOfHierarchy.isPresent() && includeInherited)
    {
      ServerGeoObjectType rootType = rootOfHierarchy.get();

      InheritedHierarchyAnnotation anno = InheritedHierarchyAnnotation.get(rootType.getUniversal(), sht.getHierarchicalRelationshipType());

      if (anno != null)
      {
        HierarchicalRelationshipType hrt = anno.getInheritedHierarchicalRelationshipType();
        ServerHierarchyType sht2 = ServerHierarchyType.get(hrt);

        List<ServerGeoObjectType> inheritedTypes = typeService.getTypeAncestors(rootType, sht2, true);

        types.addAll(0, inheritedTypes);
      }
    }

    return types;
  }

  public HierarchyType toHierarchyType(ServerHierarchyType sht)
  {
    return this.toHierarchyType(sht, true);
  }

  public HierarchyType toHierarchyType(ServerHierarchyType sht, boolean includePrivateTypes)
  {
    LocalizedValue description = RegistryLocalizedValueConverter.convert(sht.getDescription());

    final HierarchyType hierarchyType = new HierarchyType(sht.getCode(), sht.getLabel(), description, sht.getOrganizationCode());
    hierarchyType.setAbstractDescription(sht.getHierarchicalRelationshipType().getAbstractDescription());
    hierarchyType.setAcknowledgement(sht.getHierarchicalRelationshipType().getAcknowledgement());
    hierarchyType.setDisclaimer(sht.getHierarchicalRelationshipType().getDisclaimer());
    hierarchyType.setContact(sht.getHierarchicalRelationshipType().getContact());
    hierarchyType.setPhoneNumber(sht.getHierarchicalRelationshipType().getPhoneNumber());
    hierarchyType.setEmail(sht.getHierarchicalRelationshipType().getEmail());
    hierarchyType.setProgress(sht.getHierarchicalRelationshipType().getProgress());
    hierarchyType.setAccessConstraints(sht.getHierarchicalRelationshipType().getAccessConstraints());
    hierarchyType.setUseConstraints(sht.getHierarchicalRelationshipType().getUseConstraints());

    this.getRootGeoObjectTypes(sht, includePrivateTypes).forEach(rootType -> hierarchyType.addRootGeoObjects(rootType));

    return hierarchyType;
  }

  public List<ServerGeoObjectType> getChildren(ServerHierarchyType sht, ServerGeoObjectType parent)
  {
    return sht.getHierarchicalRelationshipType().getChildren(parent);
  }

  protected HierarchyNode buildHierarchy(ServerHierarchyType sht, HierarchyNode parentNode, ServerGeoObjectType parent)
  {
    List<ServerGeoObjectType> children = getChildren(sht, parent);

    for (ServerGeoObjectType child : children)
    {
      HierarchyNode node = new HierarchyNode(child.getType());

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

      if (!typePermServ.canRead(rootGot.getOrganizationCode(), ServerGeoObjectType.get(rootGot), rootGot.getIsPrivate()))
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
      ServerHierarchyType inheritedHierarchy = typeService.getInheritedHierarchy(geoObjectType, sht.getHierarchicalRelationshipType());

      if (inheritedHierarchy != null)
      {
        List<ServerGeoObjectType> ancestors = typeService.getTypeAncestors(geoObjectType, inheritedHierarchy, true);
        Collections.reverse(ancestors);

        HierarchyNode child = new HierarchyNode(geoObjectType.getType(), null);
        HierarchyNode root = child;

        for (ServerGeoObjectType ancestor : ancestors)
        {
          HierarchyNode cNode = new HierarchyNode(ancestor.getType(), inheritedHierarchy.getCode());
          cNode.addChild(root);

          root = cNode;
        }
        buildHierarchy(sht, child, geoObjectType);
        rootGeoObjectTypes.add(root);
      }
      else
      {
        HierarchyNode node = new HierarchyNode(geoObjectType.getType());
        node = buildHierarchy(sht, node, geoObjectType);
        rootGeoObjectTypes.add(node);
      }

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
  public ServerHierarchyType createHierarchyType(HierarchyType hierarchyType)
  {
    ServiceFactory.getHierarchyPermissionService().enforceCanCreate(hierarchyType.getOrganizationCode());

    ServerHierarchyType sht = createHierarchyTypeInTrans(hierarchyType);

    // The transaction did not error out, so it is safe to put into the cache.
    ServiceFactory.getMetadataCache().addHierarchyType(sht, this.toHierarchyType(sht));

    ( (Session) Session.getCurrentSession() ).reloadPermissions();

    return sht;
  }

  @Transaction
  public ServerHierarchyType createHierarchyTypeInTrans(HierarchyType hierarchyType)
  {
    if (hierarchyType.getOrganizationCode() == null || hierarchyType.getOrganizationCode().equals(""))
    {
      // TODO : A better exception
      throw new AttributeValueException("Organization code cannot be null.", hierarchyType.getOrganizationCode());
    }

    Organization organization = Organization.getByCode(hierarchyType.getOrganizationCode());

    String addons = RegistryConstants.UNIVERSAL_RELATIONSHIP_POST + "AllPathsTable";

    if (hierarchyType.getCode().length() > ( 64 - addons.length() ))
    {
      // Initializing the Universal allpaths strategy creates this limitation.
      CodeLengthException ex = new CodeLengthException();
      ex.setLength(64 - addons.length());
      throw ex;
    }

    InitializationStrategyIF strategy = new InitializationStrategyIF()
    {
      @Override
      public void preApply(MdBusinessDAO mdBusiness)
      {
        mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      }

      @Override
      public void postApply(MdBusinessDAO mdBusiness)
      {
        RoleDAO adminRole = RoleDAO.findRole(RoleConstants.ADMIN).getBusinessDAO();

        adminRole.grantPermission(Operation.READ, mdBusiness.getOid());
        adminRole.grantPermission(Operation.READ_ALL, mdBusiness.getOid());
        adminRole.grantPermission(Operation.WRITE, mdBusiness.getOid());
        adminRole.grantPermission(Operation.WRITE_ALL, mdBusiness.getOid());
        adminRole.grantPermission(Operation.CREATE, mdBusiness.getOid());
        adminRole.grantPermission(Operation.DELETE, mdBusiness.getOid());
      }
    };

    try
    {
      MdTermRelationship mdTermRelUniversal = this.newHierarchyToMdTermRelForUniversals(hierarchyType);
      mdTermRelUniversal.apply();

      this.grantWritePermissionsOnMdTermRel(mdTermRelUniversal);

      Universal.getStrategy().initialize(mdTermRelUniversal.definesType(), strategy);

      MdEdge mdEdge = this.createMdEdge(hierarchyType);

      this.grantWritePermissionsOnMdTermRel(mdEdge);

      HierarchicalRelationshipType hierarchicalRelationship = new HierarchicalRelationshipType();
      hierarchicalRelationship.setCode(hierarchyType.getCode());
      hierarchicalRelationship.setOrganization(organization);
      LocalizedValueConverter.populate(hierarchicalRelationship.getDisplayLabel(), hierarchyType.getLabel());
      LocalizedValueConverter.populate(hierarchicalRelationship.getDescription(), hierarchyType.getDescription());
      hierarchicalRelationship.setMdTermRelationship(mdTermRelUniversal);
      hierarchicalRelationship.setMdEdge(mdEdge);
      hierarchicalRelationship.setAbstractDescription(hierarchyType.getAbstractDescription());
      hierarchicalRelationship.setAcknowledgement(hierarchyType.getAcknowledgement());
      hierarchicalRelationship.setDisclaimer(hierarchyType.getDisclaimer());
      hierarchicalRelationship.setContact(hierarchyType.getContact());
      hierarchicalRelationship.setPhoneNumber(hierarchyType.getPhoneNumber());
      hierarchicalRelationship.setEmail(hierarchyType.getEmail());
      hierarchicalRelationship.setProgress(hierarchyType.getProgress());
      hierarchicalRelationship.setAccessConstraints(hierarchyType.getAccessConstraints());
      hierarchicalRelationship.setUseConstraints(hierarchyType.getUseConstraints());
      hierarchicalRelationship.apply();

      return this.get(hierarchicalRelationship);
    }
    catch (DuplicateDataException ex)
    {
      DuplicateHierarchyTypeException ex2 = new DuplicateHierarchyTypeException(ex);
      ex2.setDuplicateValue(hierarchyType.getCode());
      throw ex2;
    }
  }

  /**
   * It creates an {@link MdTermRelationship} to model the relationship between
   * {@link Universal}s.
   * 
   * Needs to occur in a transaction.
   * 
   * @param hierarchyType
   * @return
   */
  public MdTermRelationship newHierarchyToMdTermRelForUniversals(HierarchyType hierarchyType)
  {
    if (!isValidName(hierarchyType.getCode()))
    {
      throw new AttributeValueException("The hierarchy type code has an invalid character.", hierarchyType.getCode());
    }

    MdBusiness mdBusUniversal = MdBusiness.getMdBusiness(Universal.CLASS);

    MdTermRelationship mdTermRelationship = new MdTermRelationship();

    // The Universal allpaths has a more restrictive limitation.
    // try
    // {
    mdTermRelationship.setTypeName(hierarchyType.getCode() + RegistryConstants.UNIVERSAL_RELATIONSHIP_POST);
    // }
    // catch (AttributeLengthCharacterException e)
    // {
    // CodeLengthException ex = new CodeLengthException();
    // ex.setLength(64 -
    // RegistryConstants.UNIVERSAL_RELATIONSHIP_POST.length());
    // throw ex;
    // }

    mdTermRelationship.setPackageName(GISConstants.GEO_PACKAGE);
    LocalizedValueConverter.populate(mdTermRelationship.getDisplayLabel(), hierarchyType.getLabel());
    LocalizedValueConverter.populate(mdTermRelationship.getDescription(), hierarchyType.getDescription());
    mdTermRelationship.setIsAbstract(false);
    mdTermRelationship.setGenerateSource(false);
    mdTermRelationship.addCacheAlgorithm(RelationshipCache.CACHE_EVERYTHING);
    mdTermRelationship.addAssociationType(AssociationType.Graph);
    mdTermRelationship.setRemove(true);
    // Create the relationship between different universals.
    mdTermRelationship.setParentMdBusiness(mdBusUniversal);
    mdTermRelationship.setParentCardinality("1");
    mdTermRelationship.setChildMdBusiness(mdBusUniversal);
    mdTermRelationship.setChildCardinality("*");
    mdTermRelationship.setParentMethod("Parent");
    mdTermRelationship.setChildMethod("Children");

    // Set the owner of the universal to the id of the corresponding role of the
    // responsible organization.
    String organizationCode = hierarchyType.getOrganizationCode();
    RegistryLocalizedValueConverter.setOwner(mdTermRelationship, organizationCode);

    return mdTermRelationship;
  }

  /**
   * It creates an {@link MdTermRelationship} to model the relationship between
   * {@link GeoEntity}s.
   * 
   * Needs to occur in a transaction.
   * 
   * @param hierarchyType
   * @return
   */
  public MdTermRelationship newHierarchyToMdTermRelForGeoEntities(HierarchyType hierarchyType)
  {
    MdBusiness mdBusGeoEntity = MdBusiness.getMdBusiness(GeoEntity.CLASS);

    MdTermRelationship mdTermRelationship = new MdTermRelationship();

    mdTermRelationship.setTypeName(hierarchyType.getCode());
    mdTermRelationship.setPackageName(GISConstants.GEO_PACKAGE);
    LocalizedValueConverter.populate(mdTermRelationship.getDisplayLabel(), hierarchyType.getLabel());
    LocalizedValueConverter.populate(mdTermRelationship.getDescription(), hierarchyType.getDescription());
    mdTermRelationship.setIsAbstract(false);
    mdTermRelationship.setGenerateSource(false);
    mdTermRelationship.addCacheAlgorithm(RelationshipCache.CACHE_NOTHING);
    mdTermRelationship.addAssociationType(AssociationType.Graph);
    mdTermRelationship.setRemove(true);
    // Create the relationship between different universals.
    mdTermRelationship.setParentMdBusiness(mdBusGeoEntity);
    mdTermRelationship.setParentCardinality("1");
    mdTermRelationship.setChildMdBusiness(mdBusGeoEntity);
    mdTermRelationship.setChildCardinality("*");
    mdTermRelationship.setParentMethod("Parent");
    mdTermRelationship.setChildMethod("Children");

    // Set the owner of the universal to the id of the corresponding role of the
    // responsible organization.
    String organizationCode = hierarchyType.getOrganizationCode();
    RegistryLocalizedValueConverter.setOwner(mdTermRelationship, organizationCode);

    return mdTermRelationship;
  }

  /**
   * It creates an {@link MdTermRelationship} to model the relationship between
   * {@link GeoEntity}s.
   * 
   * Needs to occur in a transaction.
   * 
   * @param hierarchyType
   * @return
   */
  public MdEdge createMdEdge(HierarchyType hierarchyType)
  {
    MdVertexDAOIF mdBusGeoEntity = MdVertexDAO.getMdVertexDAO(GeoVertex.CLASS);

    MdEdgeDAO mdEdgeDAO = MdEdgeDAO.newInstance();
    mdEdgeDAO.setValue(MdEdgeInfo.PACKAGE, RegistryConstants.UNIVERSAL_GRAPH_PACKAGE);
    mdEdgeDAO.setValue(MdEdgeInfo.NAME, hierarchyType.getCode());
    mdEdgeDAO.setValue(MdEdgeInfo.PARENT_MD_VERTEX, mdBusGeoEntity.getOid());
    mdEdgeDAO.setValue(MdEdgeInfo.CHILD_MD_VERTEX, mdBusGeoEntity.getOid());
    LocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DISPLAY_LABEL, hierarchyType.getLabel());
    LocalizedValueConverter.populate(mdEdgeDAO, MdEdgeInfo.DESCRIPTION, hierarchyType.getDescription());
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
    // LocalizedValue displayLabel =
    // AttributeTypeConverter.convert(hierarchicalRelationship.getDisplayLabel());
    // LocalizedValue description =
    // AttributeTypeConverter.convert(hierarchicalRelationship.getDescription());
    // Organization organization = hierarchicalRelationship.getOrganization();

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

      if (!typePermServ.canRead(got.getOrganizationCode(), ServerGeoObjectType.get(got), got.getIsPrivate()))
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
    GeoObjectBusinessService service = new GeoObjectBusinessService();

    List<? extends InheritedHierarchyAnnotation> annotations = InheritedHierarchyAnnotation.getByInheritedHierarchy(childType.getUniversal(), sht.getHierarchicalRelationshipType());

    if (annotations.size() > 0)
    {
      List<String> codes = new ArrayList<String>();

      for (InheritedHierarchyAnnotation annot : annotations)
      {
        String code = ServerHierarchyType.buildHierarchyKeyFromMdTermRelUniversal(annot.getForHierarchy().getKey());
        codes.add(code);
      }

      CantRemoveInheritedGOT ex = new CantRemoveInheritedGOT();
      ex.setGotCode(childType.getCode());
      ex.setHierCode(sht.getCode());
      ex.setInheritedHierarchyList(StringUtils.join(codes, ", "));
      throw ex;

    }

    // If the child type is the root of the hierarchy then determine if removing
    // it will push up a child node to the root which is used in an inherited
    // hierarchy. If so we must prevent this, because the inherited hierarchy
    // model assumes that the inherited node is not the root of the inherited
    // hierarchy.
    if (parentType instanceof RootGeoObjectType)
    {
      List<ServerGeoObjectType> children = typeService.getChildren(childType, sht);

      if (children.size() == 1)
      {
        ServerGeoObjectType nextRoot = children.get(0);

        List<? extends InheritedHierarchyAnnotation> results = InheritedHierarchyAnnotation.getByInheritedHierarchy(nextRoot.getUniversal(), sht.getHierarchicalRelationshipType());

        if (results.size() > 0)
        {
          throw new RootNodeCannotBeInheritedException("Cannot remove the root Geo-Object Type of a hierarchy if the new root Geo-Object Type is inherited by another hierarchy");
        }
      }
    }

    sht.getHierarchicalRelationshipType().removeFromHierarchy(parentType, childType, migrateChildren);

    service.removeAllEdges(sht, childType);

    InheritedHierarchyAnnotation annotation = InheritedHierarchyAnnotation.get(childType.getUniversal(), sht.getHierarchicalRelationshipType());

    if (annotation != null)
    {
      annotation.delete();
    }
  }

  public List<ServerGeoObjectType> getDirectRootNodes(ServerHierarchyType sht)
  {
    Universal rootUniversal = Universal.getByKey(Universal.ROOT);

    LinkedList<ServerGeoObjectType> roots = new LinkedList<ServerGeoObjectType>();

    try (OIterator<? extends Business> i = rootUniversal.getChildren(sht.getHierarchicalRelationshipType().getMdTermRelationship().definesType()))
    {
      i.forEach(u -> roots.add(ServerGeoObjectType.get((Universal) u)));
    }

    return roots;
  }

  public void validateUniversalRelationship(ServerHierarchyType sht, ServerGeoObjectType childType, ServerGeoObjectType parentType)
  {
    // Total hack for super types
    Universal childUniversal = childType.getUniversal();
    Universal parentUniversal = parentType.getUniversal();

    List<Term> ancestors = childUniversal.getAllAncestors(sht.getUniversalType()).getAll();

    if (!ancestors.contains(parentUniversal))
    {
      ServerGeoObjectType superType = childType.getSuperType();

      if (superType != null)
      {
        ancestors = superType.getUniversal().getAllAncestors(sht.getUniversalType()).getAll();
      }
    }

    if (!ancestors.contains(parentUniversal))
    {
      InvalidGeoEntityUniversalException exception = new InvalidGeoEntityUniversalException();
      exception.setChildUniversal(childUniversal.getDisplayLabel().getValue());
      exception.setParentUniversal(parentUniversal.getDisplayLabel().getValue());
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
      Organization organization = hierarchy.getOrganization();

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
          object.addProperty("label", sHT.getDisplayLabel().getValue());

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
            object.addProperty("label", sHT.getDisplayLabel().getValue());
            object.add("parents", new JsonArray());

            hierarchies.add(object);
          }
        }
      }
    }

    return hierarchies;
  }


}
