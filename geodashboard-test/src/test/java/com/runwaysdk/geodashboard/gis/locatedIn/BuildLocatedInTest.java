/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.geodashboard.gis.locatedIn;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.geodashboard.gis.MockLogger;
import com.runwaysdk.geodashboard.gis.MockTaskListener;
import com.runwaysdk.geodashboard.gis.Task;
import com.runwaysdk.geodashboard.gis.TransactionExecuter;
import com.runwaysdk.geodashboard.localization.LocalizationFacade;
import com.runwaysdk.geodashboard.service.LocatedInBean;
import com.runwaysdk.geodashboard.service.LocatedInBean.BuildTypes;
import com.runwaysdk.geodashboard.service.LocatedInManager;
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
//  @BeforeClass
//  public static void classSetup() throws Exception
//  {
//    new TransactionExecuter()
//    {
//      @Override
//      protected void executeMethod() throws Exception
//      {
//        Universal.getStrategy().initialize(AllowedIn.CLASS);
//        GeoEntity.getStrategy().initialize(LocatedIn.CLASS);
//      }
//    }.execute();
//  }
//
//  @AfterClass
//  public static void classTearDown() throws Exception
//  {
//    new TransactionExecuter()
//    {
//      @Override
//      protected void executeMethod() throws Exception
//      {
//      }
//    }.execute();
//  }
//
//  @Test
//  @Request
//  public void testRebuildAll() throws Exception
//  {
//    MockLogger logger = new MockLogger();
//
//    LocatedInBean bean = new LocatedInBean();
//    bean.setOption(BuildTypes.REBUILD_ALL);
//    bean.setOverlapPercent(70);
//
//    LocatedInManager manager = new LocatedInManager(bean);
//    manager.addListener(new MockTaskListener());
//    manager.run(logger);
//
//    // Assert.assertFalse(logger.hasLogged());
//  }
//
//  @Test
//  @Request
//  public void testOrphanedOnly() throws Exception
//  {
//    MockLogger logger = new MockLogger();
//
//    LocatedInBean bean = new LocatedInBean();
//    bean.setOption(BuildTypes.ORPHANED_ONLY);
//    bean.setOverlapPercent(70);
//
//    MockTaskListener listener = new MockTaskListener();
//
//    LocatedInManager manager = new LocatedInManager(bean);
//    manager.addListener(listener);
//    manager.run(logger);
//
//    List<Task> tasks = listener.getTasks();
//
//    for (Task task : tasks)
//    {
//      Assert.assertFalse(LocalizationFacade.getFromBundles("builder.deleteExisting").equals(task.getTaskName()));
//    }
//
//    // Assert.assertFalse(logger.hasLogged());
//  }
//
//  @Test
//  @Request
//  public void testGetOrphanedChildren() throws Exception
//  {
//    LocatedInBean bean = new LocatedInBean();
//    bean.setOption(BuildTypes.REBUILD_ALL);
//    bean.setOverlapPercent(70);
//
//    LocatedInManager manager = new LocatedInManager(bean);
//    manager.addListener(new MockTaskListener());
//    GeoEntityQuery query = manager.getOrphanedChildren();
//
//    Assert.assertEquals(0, query.getCount());
//  }
//
//  @Request
//  @Test
//  public void testHandleOrphanedChildren()
//  {
//    GeoEntity earth = GeoEntity.getRoot();
//    String earthId = earth.getId();
//
//    LocatedInBean bean = new LocatedInBean();
//    bean.setOption(BuildTypes.REBUILD_ALL);
//    bean.setOverlapPercent(70);
//
//    LocatedInManager manager = new LocatedInManager(bean);
//    manager.deleteExisting();
//    manager.handleOrphanedEntities();
//
//    GeoEntityQuery query = new GeoEntityQuery(new QueryFactory());
//    query.WHERE(query.getId().NE(earthId));
//
//    OIterator<? extends GeoEntity> it = query.getIterator();
//
//    try
//    {
//      while (it.hasNext())
//      {
//        GeoEntity entity = it.next();
//
//        OIterator<? extends LocatedIn> relationships = entity.getAllLocatedInRel();
//
//        try
//        {
//          List<? extends LocatedIn> all = relationships.getAll();
//
//          Assert.assertEquals(1, all.size());
//
//          for (LocatedIn locatedIn : all)
//          {
//            String parentId = locatedIn.getParentId();
//
//            Assert.assertEquals(earthId, parentId);
//            Assert.assertEquals(entity.getId(), locatedIn.getChildId());
//          }
//        }
//        finally
//        {
//          relationships.close();
//        }
//      }
//    }
//    finally
//    {
//      it.close();
//    }
//
//  }
//
//  @Request
//  @Test
//  public void testDeleteOfExistingRelationships()
//  {
//    LocatedInBean bean = new LocatedInBean();
//    bean.setOption(BuildTypes.REBUILD_ALL);
//    bean.setOverlapPercent(70);
//
//    LocatedInManager manager = new LocatedInManager(bean);
//    manager.deleteExisting();
//
//    GeoEntityQuery query = new GeoEntityQuery(new QueryFactory());
//
//    OIterator<? extends GeoEntity> it = query.getIterator();
//
//    try
//    {
//      while (it.hasNext())
//      {
//        GeoEntity entity = it.next();
//
//        OIterator<? extends LocatedIn> relationships = entity.getAllContainsRel();
//
//        try
//        {
//          Assert.assertFalse(relationships.hasNext());
//        }
//        finally
//        {
//          relationships.close();
//        }
//      }
//    }
//    finally
//    {
//      it.close();
//    }
//  }
}
