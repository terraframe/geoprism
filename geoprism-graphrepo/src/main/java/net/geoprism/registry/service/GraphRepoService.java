package net.geoprism.registry.service;

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
import net.geoprism.registry.business.BusinessEdgeTypeBusinessServiceIF;
import net.geoprism.registry.business.BusinessTypeBusinessServiceIF;
import net.geoprism.registry.business.DirectedAcyclicGraphTypeBusinessServiceIF;
import net.geoprism.registry.business.GeoObjectTypeBusinessServiceIF;
import net.geoprism.registry.business.HierarchyTypeBusinessServiceIF;
import net.geoprism.registry.business.UndirectedGraphTypeBusinessServiceIF;
import net.geoprism.registry.model.ServerElement;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.model.ServerOrganization;

@Service
public class GraphRepoService implements GraphRepoServiceIF, ApplicationListener<ContextRefreshedEvent>
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
  public void onApplicationEvent(ContextRefreshedEvent event)
  {
    this.initialize();
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
