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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.geoprism.registry.model.GeoObjectTypeMetadata;

@Service
public class GeoObjectTypeMetadataBusinessService implements GeoObjectTypeMetadataBusinessServiceIF
{
  @Autowired
  protected HierarchyTypeBusinessServiceIF htService;
  
  @Autowired
  protected GeoObjectTypeBusinessServiceIF gotService;
  
  @Override
  public void apply(GeoObjectTypeMetadata gotm)
  {
    // TODO: HEADS UP

//    if (!gotm.isNew() && gotm.isModified(GeoObjectTypeMetadata.ISPRIVATE))
//    {
//      final ServerGeoObjectType type = gotm.getServerType();
//
//      // They aren't allowed to set this to private in certain scenarios
//      if (gotm.getIsPrivate())
//      {
//        if (hasPublicChildren(gotm))
//        {
//          PrivateTypeHasPublicChildren ex = new PrivateTypeHasPublicChildren();
//          ex.setTypeLabel(gotm.getServerType().getLabel().getValue());
//          throw ex;
//        }
//      }
//      else
//      {
//        if (hasPrivateParents(gotm))
//        {
//          TypeHasPrivateParents ex = new TypeHasPrivateParents();
//          ex.setTypeLabel(gotm.getServerType().getLabel().getValue());
//          throw ex;
//        }
//      }
//
//      // Set the isPrivate field for all children
//      if (gotm.isModified(GeoObjectTypeMetadata.ISPRIVATE) && type.getIsAbstract())
//      {
//        List<ServerGeoObjectType> subtypes = gotService.getSubtypes(type);
//
//        for (ServerGeoObjectType subtype : subtypes)
//        {
//          GeoObjectTypeMetadata submetadata = subtype.getMetadata();
//
//          submetadata.appLock();
//          submetadata.setIsPrivate(gotm.getIsPrivate());
//          submetadata.apply();
//        }
//      }
//    }
//
//    gotm.apply();
  }

  @Override
  public boolean hasPrivateParents(GeoObjectTypeMetadata gotm)
  {
    // TODO: HEADS UP

//    List<ServerHierarchyType> hierarchyTypes = ServiceFactory.getMetadataCache().getAllHierarchyTypes();
//
//    final Universal root = Universal.getRoot();
//
//    for (ServerHierarchyType ht : hierarchyTypes)
//    {
//      Collection<Term> uniParents = GeoEntityUtil.getOrderedAncestors(root, gotm.getUniversal(), ht.getUniversalType());
//
//      if (uniParents.size() > 1)
//      {
//        for (Term uniParent : uniParents)
//        {
//          if (!gotm.getKey().equals(uniParent.getKey()) && GeoObjectTypeMetadata.getByKey(uniParent.getKey()).getIsPrivate())
//          {
//            return true;
//          }
//        }
//      }
//    }

    return false;
  }

  @Override
  public boolean hasPublicChildren(GeoObjectTypeMetadata gotm)
  {
    // TODO: HEADS UP
//    ServerGeoObjectType type = gotm.getServerType();
//    GeoObjectType typeDTO = type.getType();
//
//    List<ServerHierarchyType> hierarchyTypes = ServiceFactory.getMetadataCache().getAllHierarchyTypes();
//
//    for (ServerHierarchyType ht : hierarchyTypes)
//    {
//      List<HierarchyNode> roots = htService.getRootGeoObjectTypes(ht);
//
//      for (HierarchyNode root : roots)
//      {
//        HierarchyNode node = root.findChild(typeDTO);
//
//        if (node != null)
//        {
//          Iterator<HierarchyNode> it = node.getDescendantsIterator();
//
//          while (it.hasNext())
//          {
//            HierarchyNode child = it.next();
//
//            if (!child.getGeoObjectType().getIsPrivate())
//            {
//              return true;
//            }
//          }
//        }
//      }
//    }

    return false;
  }
}
