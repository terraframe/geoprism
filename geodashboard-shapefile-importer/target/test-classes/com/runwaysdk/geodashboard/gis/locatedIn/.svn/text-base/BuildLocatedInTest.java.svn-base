package dss.vector.solutions.gis.locatedIn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;

import dss.vector.solutions.geo.LocatedIn;
import dss.vector.solutions.geo.generated.Earth;
import dss.vector.solutions.geo.generated.GeoEntity;
import dss.vector.solutions.geo.generated.GeoEntityQuery;
import dss.vector.solutions.gis.Localizer;
import dss.vector.solutions.gis.MockLogger;
import dss.vector.solutions.gis.MockTaskListener;
import dss.vector.solutions.gis.Task;
import dss.vector.solutions.gis.locatedIn.LocatedInBean.BuildTypes;

public class BuildLocatedInTest
{
  @Test
  @Request
  public void testRebuildAll() throws Exception
  {
    MockLogger logger = new MockLogger();

    LocatedInBean bean = new LocatedInBean();
    bean.setOption(BuildTypes.REBUILD_ALL);
    bean.setOverlapPercent(70);

    LocatedInManager manager = new LocatedInManager(bean);
    manager.addListener(new MockTaskListener());
    manager.run(logger);

    assertFalse(logger.hasLogged());
  }
  
  @Test
  @Request
  public void testAdditive() throws Exception
  {
    MockLogger logger = new MockLogger();
    
    LocatedInBean bean = new LocatedInBean();
    bean.setOption(BuildTypes.ADDITIVE);
    bean.setOverlapPercent(70);
    
    MockTaskListener listener = new MockTaskListener();
    
    LocatedInManager manager = new LocatedInManager(bean);
    manager.addListener(listener);
    manager.run(logger);
    
    List<Task> tasks = listener.getTasks();
    
    for(Task task : tasks)
    {
      assertFalse(Localizer.getMessage("DELETE_EXISTING").equals(task.getTaskName()));
    }
    
    assertFalse(logger.hasLogged());
  }
  
  @Test
  @Request
  public void testOrphanedOnly() throws Exception
  {
    MockLogger logger = new MockLogger();
    
    LocatedInBean bean = new LocatedInBean();
    bean.setOption(BuildTypes.ORPHANED_ONLY);
    bean.setOverlapPercent(70);
    
    MockTaskListener listener = new MockTaskListener();
    
    LocatedInManager manager = new LocatedInManager(bean);
    manager.addListener(listener);
    manager.run(logger);
    
    List<Task> tasks = listener.getTasks();
    
    for(Task task : tasks)
    {
      assertFalse(Localizer.getMessage("DELETE_EXISTING").equals(task.getTaskName()));
    }
    
    assertFalse(logger.hasLogged());
  }

  @Test
  @Request
  public void testGetOrphanedChildren() throws Exception
  {
    LocatedInBean bean = new LocatedInBean();
    bean.setOption(BuildTypes.REBUILD_ALL);
    bean.setOverlapPercent(70);

    LocatedInManager manager = new LocatedInManager(bean);
    manager.addListener(new MockTaskListener());
    GeoEntityQuery query = manager.getOrphanedChildren();

    assertEquals(0, query.getCount());
  }

  @Request
  @Test
  public void testHandleOrphanedChildren()
  {
    Earth earth = Earth.getEarthInstance();
    String earthId = earth.getId();

    LocatedInBean bean = new LocatedInBean();
    bean.setOption(BuildTypes.REBUILD_ALL);
    bean.setOverlapPercent(70);

    LocatedInManager manager = new LocatedInManager(bean);
    manager.deleteExisting();
    manager.handleOrphanedEntities();

    GeoEntityQuery query = new GeoEntityQuery(new QueryFactory());
    query.WHERE(query.getId().NE(earthId));

    OIterator<? extends GeoEntity> it = query.getIterator();

    try
    {
      while (it.hasNext())
      {
        GeoEntity entity = it.next();

        OIterator<? extends LocatedIn> relationships = entity.getAllLocatedInGeoEntityRel();

        try
        {
          List<? extends LocatedIn> all = relationships.getAll();

          assertEquals(1, all.size());

          for (LocatedIn locatedIn : all)
          {
            String parentId = locatedIn.getParentId();

            assertEquals(earthId, parentId);
            assertEquals(entity.getId(), locatedIn.getChildId());
          }
        }
        finally
        {
          relationships.close();
        }
      }
    }
    finally
    {
      it.close();
    }

  }

  @Request
  @Test
  public void testDeleteOfExistingRelationships()
  {
    LocatedInBean bean = new LocatedInBean();
    bean.setOption(BuildTypes.REBUILD_ALL);
    bean.setOverlapPercent(70);

    LocatedInManager manager = new LocatedInManager(bean);
    manager.deleteExisting();

    GeoEntityQuery query = new GeoEntityQuery(new QueryFactory());

    OIterator<? extends GeoEntity> it = query.getIterator();

    try
    {
      while (it.hasNext())
      {
        GeoEntity entity = it.next();

        OIterator<? extends LocatedIn> relationships = entity.getAllLocatedInGeoEntityRel();

        try
        {
          assertFalse(relationships.hasNext());
        }
        finally
        {
          relationships.close();
        }
      }
    }
    finally
    {
      it.close();
    }
  }

  public void testTaskListener()
  {

  }
}
