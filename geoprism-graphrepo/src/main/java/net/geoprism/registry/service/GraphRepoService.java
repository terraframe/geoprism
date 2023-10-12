package net.geoprism.registry.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.system.gis.geo.UniversalQuery;

import net.geoprism.registry.HierarchicalRelationshipType;
import net.geoprism.registry.Organization;
import net.geoprism.registry.OrganizationQuery;
import net.geoprism.registry.business.GeoObjectTypeBusinessServiceIF;
import net.geoprism.registry.business.HierarchyTypeBusinessServiceIF;
import net.geoprism.registry.model.ServerGeoObjectType;
import net.geoprism.registry.model.ServerHierarchyType;
import net.geoprism.registry.model.ServerOrganization;

@Service
public class GraphRepoService implements GraphRepoServiceIF
{
  @Autowired
  private HierarchyTypeBusinessServiceIF hierarchies;
  
  @Autowired
  private GeoObjectTypeBusinessServiceIF gots;
  
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

        ServerGeoObjectType type = gots.build(uni);

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
      ServerHierarchyType ht = hierarchies.get(relationship, false);

      ServiceFactory.getMetadataCache().addHierarchyType(ht);
    });

    HierarchicalRelationshipType.getAll().forEach(relationship -> {
      ServerHierarchyType ht = hierarchies.get(relationship, false);

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
}
