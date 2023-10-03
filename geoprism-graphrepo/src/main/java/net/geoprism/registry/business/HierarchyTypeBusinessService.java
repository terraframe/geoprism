package net.geoprism.registry.business;

import java.util.ArrayList;
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
import org.springframework.stereotype.Component;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.gis.geo.InvalidGeoEntityUniversalException;
import com.runwaysdk.system.gis.geo.Universal;

import net.geoprism.graphrepo.permission.GeoObjectTypePermissionServiceIF;
import net.geoprism.ontology.GeoEntityUtil;
import net.geoprism.registry.AbstractParentException;
import net.geoprism.registry.HierarchicalRelationshipType;
import net.geoprism.registry.InheritedHierarchyAnnotation;
import net.geoprism.registry.NoChildForLeafGeoObjectType;
import net.geoprism.registry.RootNodeCannotBeInheritedException;
import net.geoprism.registry.conversion.RegistryLocalizedValueConverter;
import net.geoprism.registry.geoobjecttype.AssignPublicChildOfPrivateType;
import net.geoprism.registry.graph.CantRemoveInheritedGOT;
import net.geoprism.registry.graph.GeoObjectTypeAlreadyInHierarchyException;
import net.geoprism.registry.model.RootGeoObjectType;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.service.ServiceFactory;

@Component
public class HierarchyTypeBusinessService implements HierarchyTypeBusinessServiceIF
{
  @Autowired
  private GeoObjectTypeBusinessService gotServ;
  
  public void refresh(ServerHierarchyType sht)
  {
    sht.setHierarchicalRelationshipType(HierarchicalRelationshipType.getByCode(sht.getHierarchicalRelationshipType().getCode()));

    ServiceFactory.getMetadataCache().addHierarchyType(sht);
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
    List<ServerHierarchyType> hierarchies = gotServ.getHierarchies(childType, true);

    if (hierarchies.contains(this))
    {
      GeoObjectTypeAlreadyInHierarchyException ex = new GeoObjectTypeAlreadyInHierarchyException();
      ex.setGotCode(childType.getCode());
      throw ex;
    }

    // Ensure a subtype is not already in the hierarchy
    if (childType.getIsAbstract())
    {
      Set<ServerHierarchyType> hierarchiesOfSubTypes = gotServ.getHierarchiesOfSubTypes(childType);

      if (hierarchiesOfSubTypes.contains(this))
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
    
//    try (OIterator<? extends Business> i = rootUniversal.getAllDescendants(sht.getHierarchicalRelationshipType().getMdTermRelationship().definesType()))
//    {
//      i.forEach(u -> types.add(ServerGeoObjectType.get((Universal) u)));
//    }
    
    GeoEntityUtil.getOrderedDescendants(rootUniversal, sht.getHierarchicalRelationshipType().getMdTermRelationship().definesType()).forEach(universal -> {
      if (!universal.getKey().equals(rootUniversal.getKey())) types.add(ServerGeoObjectType.get((Universal) universal));
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
        
        List<ServerGeoObjectType> inheritedTypes = gotServ.getTypeAncestors(rootType, sht2, true);
        
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
    return this.getChildren(sht, parent);
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
      ServerHierarchyType inheritedHierarchy = gotServ.getInheritedHierarchy(geoObjectType, sht.getHierarchicalRelationshipType());

      if (inheritedHierarchy != null)
      {
        List<ServerGeoObjectType> ancestors = gotServ.getTypeAncestors(geoObjectType, inheritedHierarchy, true);
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
      List<ServerGeoObjectType> children = gotServ.getChildren(childType, sht);

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
}
