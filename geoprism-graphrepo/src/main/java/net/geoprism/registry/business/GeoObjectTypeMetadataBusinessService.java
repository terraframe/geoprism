package net.geoprism.registry.business;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.commongeoregistry.adapter.metadata.GeoObjectType;
import org.commongeoregistry.adapter.metadata.HierarchyNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.system.gis.geo.Universal;

import net.geoprism.ontology.GeoEntityUtil;
import net.geoprism.registry.geoobjecttype.PrivateTypeHasPublicChildren;
import net.geoprism.registry.geoobjecttype.TypeHasPrivateParents;
import net.geoprism.registry.model.GeoObjectTypeMetadata;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.service.ServiceFactory;

@Component
public class GeoObjectTypeMetadataBusinessService implements GeoObjectTypeMetadataBusinessServiceIF
{
  @Autowired
  protected HierarchyTypeBusinessServiceIF htService;
  
  @Autowired
  protected GeoObjectTypeBusinessServiceIF gotService;
  
  @Override
  public void apply(GeoObjectTypeMetadata gotm)
  {
    if (!gotm.isNew() && gotm.isModified(GeoObjectTypeMetadata.ISPRIVATE))
    {
      final ServerGeoObjectType type = gotm.getServerType();

      // They aren't allowed to set this to private in certain scenarios
      if (gotm.getIsPrivate())
      {
        if (hasPublicChildren(gotm))
        {
          PrivateTypeHasPublicChildren ex = new PrivateTypeHasPublicChildren();
          ex.setTypeLabel(gotm.getServerType().getLabel().getValue());
          throw ex;
        }
      }
      else
      {
        if (hasPrivateParents(gotm))
        {
          TypeHasPrivateParents ex = new TypeHasPrivateParents();
          ex.setTypeLabel(gotm.getServerType().getLabel().getValue());
          throw ex;
        }
      }

      // Set the isPrivate field for all children
      if (gotm.isModified(GeoObjectTypeMetadata.ISPRIVATE) && type.getIsAbstract())
      {
        List<ServerGeoObjectType> subtypes = gotService.getSubtypes(type);

        for (ServerGeoObjectType subtype : subtypes)
        {
          GeoObjectTypeMetadata submetadata = subtype.getMetadata();

          submetadata.appLock();
          submetadata.setIsPrivate(gotm.getIsPrivate());
          submetadata.apply();
        }
      }
    }

    gotm.apply();
  }

  @Override
  public boolean hasPrivateParents(GeoObjectTypeMetadata gotm)
  {
    List<ServerHierarchyType> hierarchyTypes = ServiceFactory.getMetadataCache().getAllHierarchyTypes();

    final Universal root = Universal.getRoot();

    for (ServerHierarchyType ht : hierarchyTypes)
    {
      Collection<Term> uniParents = GeoEntityUtil.getOrderedAncestors(root, gotm.getUniversal(), ht.getUniversalType());

      if (uniParents.size() > 1)
      {
        for (Term uniParent : uniParents)
        {
          if (!gotm.getKey().equals(uniParent.getKey()) && GeoObjectTypeMetadata.getByKey(uniParent.getKey()).getIsPrivate())
          {
            return true;
          }
        }
      }
    }

    return false;
  }

  @Override
  public boolean hasPublicChildren(GeoObjectTypeMetadata gotm)
  {
    ServerGeoObjectType type = gotm.getServerType();
    GeoObjectType typeDTO = type.getType();

    List<ServerHierarchyType> hierarchyTypes = ServiceFactory.getMetadataCache().getAllHierarchyTypes();

    for (ServerHierarchyType ht : hierarchyTypes)
    {
      List<HierarchyNode> roots = htService.getRootGeoObjectTypes(ht);

      for (HierarchyNode root : roots)
      {
        HierarchyNode node = root.findChild(typeDTO);

        if (node != null)
        {
          Iterator<HierarchyNode> it = node.getDescendantsIterator();

          while (it.hasNext())
          {
            HierarchyNode child = it.next();

            if (!child.getGeoObjectType().getIsPrivate())
            {
              return true;
            }
          }
        }
      }
    }

    return false;
  }
}
