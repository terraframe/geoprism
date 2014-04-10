package com.runwaysdk.geodashboard.gis.locatedIn;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.geodashboard.gis.Localizer;
import com.runwaysdk.geodashboard.gis.MockLogger;
import com.runwaysdk.geodashboard.gis.MockTaskListener;
import com.runwaysdk.geodashboard.gis.Task;
import com.runwaysdk.geodashboard.gis.TransactionExecuter;
import com.runwaysdk.geodashboard.gis.locatedIn.LocatedInBean.BuildTypes;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.GeoEntityQuery;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.Universal;

public class BuildLocatedInTest
{
  @BeforeClass
  public static void classSetup() throws Exception
  {
    new TransactionExecuter()
    {
      @Override
      protected void executeMethod() throws Exception
      {
        Universal.getStrategy().initialize(AllowedIn.CLASS);
        GeoEntity.getStrategy().initialize(LocatedIn.CLASS);
      }
    }.execute();
  }

  @AfterClass
  public static void classTearDown() throws Exception
  {
    new TransactionExecuter()
    {
      @Override
      protected void executeMethod() throws Exception
      {
      }
    }.execute();
  }

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

    Assert.assertFalse(logger.hasLogged());
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

    for (Task task : tasks)
    {
      Assert.assertFalse(Localizer.getMessage("DELETE_EXISTING").equals(task.getTaskName()));
    }

    Assert.assertFalse(logger.hasLogged());
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

    Assert.assertEquals(0, query.getCount());
  }

  @Request
  @Test
  public void testHandleOrphanedChildren()
  {
    GeoEntity earth = GeoEntity.getRoot();
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

        OIterator<? extends LocatedIn> relationships = entity.getAllLocatedInRel();

        try
        {
          List<? extends LocatedIn> all = relationships.getAll();

          Assert.assertEquals(1, all.size());

          for (LocatedIn locatedIn : all)
          {
            String parentId = locatedIn.getParentId();

            Assert.assertEquals(earthId, parentId);
            Assert.assertEquals(entity.getId(), locatedIn.getChildId());
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

        OIterator<? extends LocatedIn> relationships = entity.getAllContainsRel();

        try
        {
          Assert.assertFalse(relationships.hasNext());
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

}
