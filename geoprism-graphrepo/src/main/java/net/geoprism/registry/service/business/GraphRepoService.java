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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;

import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.BusinessType;
import net.geoprism.registry.DirectedAcyclicGraphType;
import net.geoprism.registry.Organization;
import net.geoprism.registry.OrganizationQuery;
import net.geoprism.registry.UndirectedGraphType;
import net.geoprism.registry.graph.HierarchicalRelationshipType;
import net.geoprism.registry.graph.InheritedHierarchyAnnotation;
import net.geoprism.registry.model.ServerElement;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.model.ServerOrganization;

@Service
public class GraphRepoService implements GraphRepoServiceIF
{
  @Autowired
  private HierarchyTypeBusinessServiceIF            hierarchyService;

  @Autowired
  private GeoObjectTypeBusinessServiceIF            typeService;

  @Autowired
  private BusinessEdgeTypeBusinessServiceIF         bizEdgeTypeService;

  @Autowired
  private BusinessTypeBusinessServiceIF             bizTypeService;

  @Autowired
  private DirectedAcyclicGraphTypeBusinessServiceIF dagTypeService;

  @Autowired
  private UndirectedGraphTypeBusinessServiceIF      ugtTypeService;

  @Request
  @Override
  public synchronized void initialize()
  {
    refreshMetadataCache();
  }

  @Override
  public void refreshMetadataCache()
  {
    ServiceFactory.getMetadataCache().rebuild();

    ServerGeoObjectType.getAllFromDatabase().stream().forEach(type -> {
      ServiceFactory.getMetadataCache().addGeoObjectType(type);
    });

    // We must build the hierarchy types which are inherited first
    // Otherwise you will end up with a NPE when building the hierarchies
    // which inherit the inherited hierarchy if it hasn't been built
    InheritedHierarchyAnnotation.getInheritedTypes().forEach(relationship -> {
      ServerHierarchyType ht = hierarchyService.get(relationship, false);

      ServiceFactory.getMetadataCache().addHierarchyType(ht);
    });

    HierarchicalRelationshipType.getAll().forEach(relationship -> {
      ServerHierarchyType ht = hierarchyService.get(relationship, false);

      if (!ServiceFactory.getMetadataCache().getHierachyType(ht.getCode()).isPresent())
      {
        ServiceFactory.getMetadataCache().addHierarchyType(ht);
      }
    });

    // Due to inherited hierarchy references, this has to wait until all types
    // exist in the cache.
    // for (ServerHierarchyType type :
    // ServiceFactory.getMetadataCache().getAllHierarchyTypes())
    // {
    //// type.buildHierarchyNodes();
    // }

    try
    {
      // This is, unfortunately, a big hack. Some patch items need to occur
      // before the organization class is defined
      MdClassDAO.getMdClassDAO(Organization.CLASS);

      OrganizationQuery oQ = new OrganizationQuery(new QueryFactory());
      OIterator<? extends Organization> it3 = oQ.getIterator();

      try
      {
        while (it3.hasNext())
        {
          Organization organization = it3.next();

          ServiceFactory.getMetadataCache().addOrganization(ServerOrganization.get(organization));
        }
      }
      finally
      {
        it3.close();
      }
    }
    catch (com.runwaysdk.dataaccess.cache.DataNotFoundException e)
    {
      // skip for now
    }
  }

  @Override
  public void deleteObject(ServerElement obj)
  {
    if (obj instanceof ServerGeoObjectType)
    {
      typeService.deleteGeoObjectType(obj.getCode());
    }
    else if (obj instanceof BusinessEdgeType)
    {
      bizEdgeTypeService.delete((BusinessEdgeType) obj);
    }
    else if (obj instanceof BusinessType)
    {
      bizTypeService.delete((BusinessType) obj);
    }
    else if (obj instanceof DirectedAcyclicGraphType)
    {
      dagTypeService.delete((DirectedAcyclicGraphType) obj);
    }
    else if (obj instanceof ServerHierarchyType)
    {
      hierarchyService.delete((ServerHierarchyType) obj);
    }
    else if (obj instanceof UndirectedGraphType)
    {
      ugtTypeService.delete((UndirectedGraphType) obj);
    }
    else
    {
      throw new UnsupportedOperationException(obj.getClass().getName());
    }
  }
}
