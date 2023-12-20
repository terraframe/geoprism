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
package net.geoprism.registry.service.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.geo.UniversalQuery;

import net.geoprism.registry.BusinessEdgeType;
import net.geoprism.registry.BusinessType;
import net.geoprism.registry.DirectedAcyclicGraphType;
import net.geoprism.registry.HierarchicalRelationshipType;
import net.geoprism.registry.Organization;
import net.geoprism.registry.OrganizationQuery;
import net.geoprism.registry.UndirectedGraphType;
import net.geoprism.registry.model.ServerElement;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.model.ServerOrganization;
import net.geoprism.registry.service.business.BusinessEdgeTypeBusinessServiceIF;
import net.geoprism.registry.service.business.BusinessTypeBusinessServiceIF;
import net.geoprism.registry.service.business.DirectedAcyclicGraphTypeBusinessServiceIF;
import net.geoprism.registry.service.business.GeoObjectTypeBusinessServiceIF;
import net.geoprism.registry.service.business.HierarchyTypeBusinessServiceIF;
import net.geoprism.registry.service.business.UndirectedGraphTypeBusinessServiceIF;

@Service
public class GraphRepoService implements GraphRepoServiceIF
{
  @Autowired
  private HierarchyTypeBusinessServiceIF hierarchyService;

  @Autowired
  private GeoObjectTypeBusinessServiceIF typeService;
  
  @Autowired
  private BusinessEdgeTypeBusinessServiceIF bizEdgeTypeService;
  
  @Autowired
  private BusinessTypeBusinessServiceIF bizTypeService;
  
  @Autowired
  private DirectedAcyclicGraphTypeBusinessServiceIF dagTypeService;
  
  @Autowired
  private UndirectedGraphTypeBusinessServiceIF ugtTypeService;

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

    QueryFactory qf = new QueryFactory();
    UniversalQuery uq = new UniversalQuery(qf);
    OIterator<? extends Universal> it = uq.getIterator();

    try
    {
      while (it.hasNext())
      {
        Universal uni = it.next();

        if (uni.getKey().equals(Universal.ROOT_KEY))
        {
          continue;
        }

        ServerGeoObjectType type = typeService.build(uni);

        ServiceFactory.getMetadataCache().addGeoObjectType(type);
      }
    }
    finally
    {
      it.close();
    }

    // We must build the hierarchy types which are inherited first
    // Otherwise you will end up with a NPE when building the hierarchies
    // which inherit the inherited hierarchy if it hasn't been built
    HierarchicalRelationshipType.getInheritedTypes().forEach(relationship -> {
      ServerHierarchyType ht = hierarchyService.get(relationship, false);

      ServiceFactory.getMetadataCache().addHierarchyType(ht, this.hierarchyService.toHierarchyType(ht));
    });

    HierarchicalRelationshipType.getAll().forEach(relationship -> {
      ServerHierarchyType ht = hierarchyService.get(relationship, false);

      if (!ServiceFactory.getMetadataCache().getHierachyType(ht.getCode()).isPresent())
      {
        ServiceFactory.getMetadataCache().addHierarchyType(ht, this.hierarchyService.toHierarchyType(ht));
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

      OrganizationQuery oQ = new OrganizationQuery(qf);
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
