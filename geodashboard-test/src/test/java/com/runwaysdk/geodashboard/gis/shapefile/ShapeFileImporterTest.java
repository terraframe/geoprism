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
package com.runwaysdk.geodashboard.gis.shapefile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.geodashboard.gis.GISImportLoggerIF;
import com.runwaysdk.geodashboard.gis.MockLogger;
import com.runwaysdk.geodashboard.gis.MockTaskListener;
import com.runwaysdk.geodashboard.gis.Task;
import com.runwaysdk.geodashboard.gis.TransactionExecuter;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.Universal;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;

public class ShapeFileImporterTest
{
  private static String    SHAPE_FILE = "output/shapefile.shp";

  private static Universal country;

  private static Universal facility;

  private static Universal settlement;

  private static GeoEntity parent;

  private static URL       url;

  private static GeoEntity entity1;

  private static GeoEntity entity2;

  @BeforeClass
  public static void classSetup() throws Exception
  {
    File file = new File(SHAPE_FILE);

    if (!file.exists())
    {
      file.getParentFile().mkdirs();
      file.createNewFile();
    }

    url = file.toURI().toURL();

    new TransactionExecuter()
    {
      @Override
      protected void executeMethod() throws Exception
      {
        Universal.getStrategy().initialize(AllowedIn.CLASS);
        GeoEntity.getStrategy().initialize(LocatedIn.CLASS);

        country = new Universal();
        country.getDisplayLabel().setValue("Country");
        country.setUniversalId("Country");
        country.apply();

        facility = new Universal();
        facility.getDisplayLabel().setValue("Facility");
        facility.setUniversalId("Facility");
        facility.apply();

        settlement = new Universal();
        settlement.getDisplayLabel().setValue("Settlement");
        settlement.setUniversalId("Settlement");
        settlement.apply();

        facility.addLink(country, AllowedIn.CLASS);
        settlement.addLink(country, AllowedIn.CLASS);

        parent = new GeoEntity();
        parent.setGeoId("TestParent04");
        parent.getDisplayLabel().setDefaultValue("Test Parent Entity 2");
        parent.setUniversal(country);
        parent.apply();

        entity1 = new GeoEntity();
        entity1.setGeoId("TestParent05");
        entity1.getDisplayLabel().setDefaultValue("Test Parent Entity Duplicate");
        entity1.setUniversal(country);
        entity1.apply();

        entity2 = new GeoEntity();
        entity2.setGeoId("TestParent06");
        entity2.getDisplayLabel().setDefaultValue("Test Parent Entity Duplicate");
        entity2.setUniversal(country);
        entity2.apply();
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
        if (entity1 != null)
        {
          entity1.delete();
        }

        if (entity2 != null)
        {
          entity2.delete();
        }

        if (parent != null)
        {
          parent.delete();
        }

        if (settlement != null)
        {
          settlement.delete();
        }

        if (facility != null)
        {
          facility.delete();
        }

        if (country != null)
        {
          country.delete();
        }

      }
    }.execute();
  }

  @Test
  public void testGetAttribute() throws IOException, ParseException
  {
    new ShapeFileBuilder(SHAPE_FILE, parent).build();

    ShapeFileImporter importer = new ShapeFileImporter(url);

    List<String> attributes = importer.getAttributes();

    Assert.assertEquals(5, attributes.size());

    Assert.assertTrue(attributes.contains(ShapeFileBuilder.ID_ATTRIBUTE));
    Assert.assertTrue(attributes.contains(ShapeFileBuilder.NAME_ATTRIBUTE));
    Assert.assertTrue(attributes.contains(ShapeFileBuilder.PARENT_ATTRIBUTE));
    Assert.assertTrue(attributes.contains(ShapeFileBuilder.PARENT_TYPE_ATTRIBUTE));
    Assert.assertTrue(attributes.contains(ShapeFileBuilder.TYPE_ATTRIBUTE));
  }

  @Request
  @Test
  public void testImportWithGeneratedIds() throws Exception
  {
    ShapeFileBuilder builder = new ShapeFileBuilder(SHAPE_FILE, parent);
    builder.build();

    GISImportLoggerIF logger = new MockLogger();

    ShapeFileImporter importer = new ShapeFileImporter(url);
    importer.setUniversalId(country.getId());
    importer.setName(ShapeFileBuilder.NAME_ATTRIBUTE);
    importer.run(logger);

    Map<String, String> map = importer.getEntityIdMap();

    try
    {
      Assert.assertFalse(logger.hasLogged());
      Assert.assertEquals(100, map.size());

      for (int i = 0; i < 100; i++)
      {
        try
        {
          Assert.assertNotNull(ShapeFileImporter.getByEntityName(builder.getName(i)));
        }
        catch (AmbigiousGeoEntityException e)
        {
          Assert.fail("Ambigious geo entity of the name [" + builder.getName(i) + "]");
        }
      }
    }
    finally
    {
      for (String key : map.keySet())
      {
        String id = map.get(key);

        GeoEntity entity = GeoEntity.get(id);

        entity.delete();
      }
    }
  }

  @Request
  @Test
  public void testImportWithIdAttribute() throws Exception
  {
    ShapeFileBuilder builder = new ShapeFileBuilder(SHAPE_FILE, parent);
    builder.build();

    GeoEntity root = GeoEntity.getRoot();

    GISImportLoggerIF logger = new MockLogger();

    ShapeFileImporter importer = new ShapeFileImporter(url);
    importer.setUniversalId(country.getId());
    importer.setName(ShapeFileBuilder.NAME_ATTRIBUTE);
    importer.setId(ShapeFileBuilder.ID_ATTRIBUTE);
    importer.run(logger);

    Map<String, String> map = importer.getEntityIdMap();

    try
    {
      Assert.assertFalse(logger.hasLogged());
      Assert.assertEquals(100, map.size());

      for (int i = 0; i < 100; i++)
      {
        GeoEntity entity = ShapeFileImporter.getByEntityName(builder.getName(i));

        Assert.assertNotNull(entity);

        OIterator<Term> parentsIter = entity.getDirectAncestors(LocatedIn.CLASS);
        List<Term> parents = parentsIter.getAll();
        
        Assert.assertEquals(builder.getName(i), entity.getDisplayLabel().getValue());
        Assert.assertEquals(builder.getGeoId(i), entity.getGeoId());
        Assert.assertEquals(1, parents.size());
        Assert.assertTrue(parents.contains(root));
      }
    }
    finally
    {
      for (String key : map.keySet())
      {
        String id = map.get(key);

        GeoEntity.get(id).delete();
      }
    }
  }

  @Request
  @Test
  public void testImportWithNullIdAttribute() throws Exception
  {
    GISImportLoggerIF logger = new MockLogger();

    ShapeFileBuilder builder = new ShapeFileBuilder(SHAPE_FILE, parent)
    {
      public String getGeoId(int i)
      {
        return "";
      }
    };
    builder.build();

    ShapeFileImporter importer = new ShapeFileImporter(url);
    importer.setUniversalId(country.getId());
    importer.setName(ShapeFileBuilder.NAME_ATTRIBUTE);
    importer.setId(ShapeFileBuilder.ID_ATTRIBUTE);
    importer.run(logger);

    Map<String, String> map = importer.getEntityIdMap();

    try
    {
      Assert.assertFalse(logger.hasLogged());
      Assert.assertEquals(100, map.size());

      for (int i = 0; i < 100; i++)
      {
        GeoEntity entity = ShapeFileImporter.getByEntityName(builder.getName(i));

        Assert.assertNotNull(entity);
        Assert.assertEquals(builder.getName(i), entity.getDisplayLabel().getValue());
        Assert.assertTrue(entity.getGeoId().length() > 0);
      }
    }
    finally
    {
      for (String key : map.keySet())
      {
        String id = map.get(key);

        GeoEntity.get(id).delete();
      }
    }
  }

  @Request
  @Test
  public void testMultipolygonImport() throws Exception
  {
    GISImportLoggerIF logger = new MockLogger();

    ShapeFileBuilder builder = new ShapeFileBuilder(SHAPE_FILE, parent)
    {
      protected Geometry getGeometry(int i) throws ParseException
      {
        int j = i + 100;
        int k = i + 200;

        return reader.read("MULTIPOLYGON(((" + i + " " + i + "," + j + " " + j + "," + k + " " + k + "," + i + " " + i + "),(" + i + " " + j + "," + i + " " + k + "," + j + " " + j + "," + i + " " + j + ")),((" + j + " " + i + "," + j + " " + k + "," + k + " " + k + "," + j + " " + i + ")))");
      };
    };
    builder.build();

    ShapeFileImporter importer = new ShapeFileImporter(url);
    importer.setUniversalId(country.getId());
    importer.setName(ShapeFileBuilder.NAME_ATTRIBUTE);
    importer.setId(ShapeFileBuilder.ID_ATTRIBUTE);
    importer.run(logger);

    Map<String, String> map = importer.getEntityIdMap();

    try
    {
      Assert.assertFalse(logger.hasLogged());
      Assert.assertEquals(100, map.size());

      for (int i = 0; i < 100; i++)
      {
        GeoEntity entity = ShapeFileImporter.getByEntityName(builder.getName(i));

        Assert.assertNotNull(entity);
        Assert.assertEquals(builder.getName(i), entity.getDisplayLabel().getDefaultValue());
        Assert.assertEquals(builder.getGeoId(i), entity.getGeoId());
      }
    }
    finally
    {
      for (String key : map.keySet())
      {
        String id = map.get(key);

        GeoEntity.get(id).delete();
      }
    }
  }

  @Request
  @Test
  public void testLocatedIn() throws Exception
  {
    GISImportLoggerIF logger = new MockLogger();

    ShapeFileBuilder builder = new ShapeFileBuilder(SHAPE_FILE, parent);
    builder.build();

    ShapeFileImporter importer = new ShapeFileImporter(url);
    importer.setUniversalId(facility.getId());
    importer.setName(ShapeFileBuilder.NAME_ATTRIBUTE);
    importer.setId(ShapeFileBuilder.ID_ATTRIBUTE);
    importer.setParent(ShapeFileBuilder.PARENT_ATTRIBUTE);
    importer.run(logger);

    Map<String, String> map = importer.getEntityIdMap();

    try
    {
      Assert.assertFalse(logger.hasLogged());
      Assert.assertEquals(100, map.size());

      for (int i = 0; i < 100; i++)
      {
        GeoEntity entity = ShapeFileImporter.getByEntityName(builder.getName(i));

        Assert.assertNotNull(entity);

        OIterator<Term> parentsIter = entity.getDirectAncestors(LocatedIn.CLASS);
        List<Term> parents = parentsIter.getAll();

        Assert.assertEquals(builder.getName(i), entity.getDisplayLabel().getDefaultValue());
        Assert.assertEquals(builder.getGeoId(i), entity.getGeoId());
        Assert.assertEquals(1, parents.size());
        Assert.assertTrue(parents.contains(parent));
      }
    }
    finally
    {
      for (String key : map.keySet())
      {
        String id = map.get(key);

        GeoEntity.get(id).delete();
      }
    }
  }

  @Request
  @Test
  public void testUnknownLocatedIn() throws Exception
  {
    GISImportLoggerIF logger = new MockLogger();

    ShapeFileBuilder builder = new ShapeFileBuilder(SHAPE_FILE, parent);
    builder.build();

    ShapeFileImporter importer = new ShapeFileImporter(url);
    importer.setUniversalId(facility.getId());
    importer.setName(ShapeFileBuilder.NAME_ATTRIBUTE);
    importer.setId(ShapeFileBuilder.ID_ATTRIBUTE);
    importer.setParent("bogusValue");
    importer.run(logger);

    Map<String, String> map = importer.getEntityIdMap();

    GeoEntity root = GeoEntity.getRoot();

    try
    {
      Assert.assertFalse(logger.hasLogged());
      Assert.assertEquals(100, map.size());

      for (int i = 0; i < 100; i++)
      {
        GeoEntity entity = ShapeFileImporter.getByEntityName(builder.getName(i));

        Assert.assertNotNull(entity);

        OIterator<Term> parentsIter = entity.getDirectAncestors(LocatedIn.CLASS);
        List<Term> parents = parentsIter.getAll();

        Assert.assertEquals(builder.getName(i), entity.getDisplayLabel().getDefaultValue());
        Assert.assertEquals(builder.getGeoId(i), entity.getGeoId());
        Assert.assertEquals(1, parents.size());
        Assert.assertTrue(parents.contains(root));
      }
    }
    finally
    {
      for (String key : map.keySet())
      {
        String id = map.get(key);

        GeoEntity.get(id).delete();
      }
    }
  }

  @Request
  @Test
  public void testLocatedInWithLocatedInType() throws Exception
  {
    GISImportLoggerIF logger = new MockLogger();

    ShapeFileBuilder builder = new ShapeFileBuilder(SHAPE_FILE, parent);
    builder.build();

    ShapeFileImporter importer = new ShapeFileImporter(url);
    importer.setUniversalId(facility.getId());
    importer.setName(ShapeFileBuilder.NAME_ATTRIBUTE);
    importer.setId(ShapeFileBuilder.ID_ATTRIBUTE);
    importer.setParent(ShapeFileBuilder.PARENT_ATTRIBUTE);
    importer.setParentType(ShapeFileBuilder.PARENT_TYPE_ATTRIBUTE);
    importer.run(logger);

    Map<String, String> map = importer.getEntityIdMap();

    try
    {
      Assert.assertFalse(logger.hasLogged());
      Assert.assertEquals(100, map.size());

      for (int i = 0; i < 100; i++)
      {
        GeoEntity entity = ShapeFileImporter.getByEntityName(builder.getName(i));

        Assert.assertNotNull(entity);

        OIterator<Term> parentsIter = entity.getDirectAncestors(LocatedIn.CLASS);
        List<Term> parents = parentsIter.getAll();

        Assert.assertEquals(builder.getName(i), entity.getDisplayLabel().getDefaultValue());
        Assert.assertEquals(builder.getGeoId(i), entity.getGeoId());
        Assert.assertEquals(1, parents.size());
        Assert.assertTrue(parents.contains(parent));
      }
    }
    finally
    {
      for (String key : map.keySet())
      {
        String id = map.get(key);

        GeoEntity.get(id).delete();
      }
    }
  }

  @Request
  @Test
  public void testNullLocatedIn() throws Exception
  {
    GISImportLoggerIF logger = new MockLogger();

    ShapeFileBuilder builder = new ShapeFileBuilder(SHAPE_FILE, parent)
    {
      public String getParentEntityName(int i)
      {
        return "";
      }
    };
    builder.build();

    ShapeFileImporter importer = new ShapeFileImporter(url);
    importer.setUniversalId(facility.getId());
    importer.setName(ShapeFileBuilder.NAME_ATTRIBUTE);
    importer.setId(ShapeFileBuilder.ID_ATTRIBUTE);
    importer.setParent(ShapeFileBuilder.PARENT_ATTRIBUTE);
    importer.run(logger);

    Map<String, String> map = importer.getEntityIdMap();

    GeoEntity root = GeoEntity.getRoot();

    try
    {
      Assert.assertFalse(logger.hasLogged());
      Assert.assertEquals(100, map.size());

      for (int i = 0; i < 100; i++)
      {
        GeoEntity entity = ShapeFileImporter.getByEntityName(builder.getName(i));

        Assert.assertNotNull(entity);

        OIterator<Term> parentsIter = entity.getDirectAncestors(LocatedIn.CLASS);
        List<Term> parents = parentsIter.getAll();

        Assert.assertEquals(builder.getName(i), entity.getDisplayLabel().getDefaultValue());
        Assert.assertEquals(builder.getGeoId(i), entity.getGeoId());
        Assert.assertEquals(1, parents.size());
        Assert.assertTrue(parents.contains(root));
      }
    }
    finally
    {
      for (String key : map.keySet())
      {
        String id = map.get(key);

        GeoEntity.get(id).delete();
      }
    }
  }

  @Request
  @Test
  public void testLocatedInWithNullLocatedInType() throws Exception
  {
    GISImportLoggerIF logger = new MockLogger();

    ShapeFileBuilder builder = new ShapeFileBuilder(SHAPE_FILE, parent)
    {
      public String getParentType(int i)
      {
        return "";
      }
    };
    builder.build();

    ShapeFileImporter importer = new ShapeFileImporter(url);
    importer.setUniversalId(facility.getId());
    importer.setName(ShapeFileBuilder.NAME_ATTRIBUTE);
    importer.setId(ShapeFileBuilder.ID_ATTRIBUTE);
    importer.setParent(ShapeFileBuilder.PARENT_ATTRIBUTE);
    importer.setParentType(ShapeFileBuilder.PARENT_ATTRIBUTE);
    importer.run(logger);

    Map<String, String> map = importer.getEntityIdMap();

    try
    {
      Assert.assertFalse(logger.hasLogged());
      Assert.assertEquals(100, map.size());

      for (int i = 0; i < 100; i++)
      {
        GeoEntity entity = ShapeFileImporter.getByEntityName(builder.getName(i));

        Assert.assertNotNull(entity);

        OIterator<Term> parentsIter = entity.getDirectAncestors(LocatedIn.CLASS);
        List<Term> parents = parentsIter.getAll();
        
        Assert.assertEquals(builder.getName(i), entity.getDisplayLabel().getDefaultValue());
        Assert.assertEquals(builder.getGeoId(i), entity.getGeoId());
        Assert.assertEquals(1, parents.size());
        Assert.assertTrue(parents.contains(parent));
      }
    }
    finally
    {
      for (String key : map.keySet())
      {
        String id = map.get(key);

        GeoEntity.get(id).delete();
      }
    }
  }

  @Request
  @Test
  public void testLocatedInWithUnknownLocatedInType() throws Exception
  {
    GISImportLoggerIF logger = new MockLogger();

    ShapeFileBuilder builder = new ShapeFileBuilder(SHAPE_FILE, parent)
    {
      public String getParentType(int i)
      {
        return "NoAValidUniversal";
      }
    };
    builder.build();

    ShapeFileImporter importer = new ShapeFileImporter(url);
    importer.setUniversalId(facility.getId());
    importer.setName(ShapeFileBuilder.NAME_ATTRIBUTE);
    importer.setId(ShapeFileBuilder.ID_ATTRIBUTE);
    importer.setParent(ShapeFileBuilder.PARENT_ATTRIBUTE);
    importer.setParentType(ShapeFileBuilder.PARENT_TYPE_ATTRIBUTE);
    importer.run(logger);

    Map<String, String> map = importer.getEntityIdMap();

    try
    {
      Assert.assertFalse(logger.hasLogged());
      Assert.assertEquals(100, map.size());

      for (int i = 0; i < 100; i++)
      {
        GeoEntity entity = ShapeFileImporter.getByEntityName(builder.getName(i));

        Assert.assertNotNull(entity);

        OIterator<Term> parentsIter = entity.getDirectAncestors(LocatedIn.CLASS);
        List<Term> parents = parentsIter.getAll();
        
        Assert.assertEquals(builder.getName(i), entity.getDisplayLabel().getDefaultValue());
        Assert.assertEquals(builder.getGeoId(i), entity.getGeoId());
        Assert.assertEquals(1, parents.size());
        Assert.assertTrue(parents.contains(parent));
      }
    }
    finally
    {
      for (String key : map.keySet())
      {
        String id = map.get(key);

        GeoEntity.get(id).delete();
      }
    }
  }

  @Request
  @Test
  public void testTaskListener() throws Exception
  {
    GISImportLoggerIF logger = new MockLogger();

    ShapeFileBuilder builder = new ShapeFileBuilder(SHAPE_FILE, parent);
    builder.build();

    GeoEntity root = GeoEntity.getRoot();

    MockTaskListener listener = new MockTaskListener();

    ShapeFileImporter importer = new ShapeFileImporter(url);
    importer.setUniversalId(facility.getId());
    importer.setName(ShapeFileBuilder.NAME_ATTRIBUTE);
    importer.setId(ShapeFileBuilder.ID_ATTRIBUTE);
    importer.addListener(listener);
    importer.run(logger);

    Map<String, String> map = importer.getEntityIdMap();

    try
    {
      Assert.assertFalse(logger.hasLogged());
      Assert.assertEquals(100, map.size());

      for (int i = 0; i < 100; i++)
      {
        GeoEntity entity = ShapeFileImporter.getByEntityName(builder.getName(i));

        Assert.assertNotNull(entity);

        OIterator<Term> parentsIter = entity.getDirectAncestors(LocatedIn.CLASS);
        List<Term> parents = parentsIter.getAll();
        
        Assert.assertEquals(builder.getName(i), entity.getDisplayLabel().getDefaultValue());
        Assert.assertEquals(builder.getGeoId(i), entity.getGeoId());
        Assert.assertEquals(1, parents.size());
        Assert.assertTrue(parents.contains(root));
      }

      Assert.assertEquals(true, listener.isDone());
      Assert.assertEquals(true, listener.isStatus());
      Assert.assertEquals(true, listener.isStarted());

      List<Task> tasks = listener.getTasks();

      Assert.assertEquals(2, tasks.size());
      Assert.assertEquals(-1, tasks.get(0).getTotal());
      Assert.assertEquals(100, tasks.get(1).getTotal());

      Assert.assertEquals(0, tasks.get(0).getWork());
      Assert.assertEquals(100, tasks.get(1).getWork());
    }
    finally
    {
      for (String key : map.keySet())
      {
        String id = map.get(key);

        GeoEntity.get(id).delete();
      }
    }
  }

  @Request
  @Test
  public void testDuplicateLocatedIn() throws Exception
  {
    MockLogger logger = new MockLogger();

    ShapeFileBuilder builder = new ShapeFileBuilder(SHAPE_FILE, entity1);
    builder.build();

    ShapeFileImporter importer = new ShapeFileImporter(url);

    try
    {
      importer.setUniversalId(facility.getId());
      importer.setName(ShapeFileBuilder.NAME_ATTRIBUTE);
      importer.setId(ShapeFileBuilder.ID_ATTRIBUTE);
      importer.setParent(ShapeFileBuilder.PARENT_ATTRIBUTE);
      importer.run(logger);

      Assert.assertTrue(logger.hasLogged());
      Assert.assertEquals(0, importer.getEntityIdMap().size());

      HashMap<String, Throwable> map = logger.getMap();

      // There should be an exception logged for every feature of the
      // shapefile
      Assert.assertEquals(100, map.size());

      for (String key : map.keySet())
      {
        Assert.assertTrue(map.get(key) instanceof AmbigiousGeoEntityException);
      }
    }
    finally
    {
      Map<String, String> map = importer.getEntityIdMap();

      for (String key : map.keySet())
      {
        String id = map.get(key);

        GeoEntity.get(id).delete();
      }
    }
  }

  @Request
  @Test
  public void testDuplicateLocatedInWithType() throws Exception
  {
    MockLogger logger = new MockLogger();

    ShapeFileBuilder builder = new ShapeFileBuilder(SHAPE_FILE, entity1);
    builder.build();

    ShapeFileImporter importer = new ShapeFileImporter(url);
    try
    {
      importer.setUniversalId(facility.getId());
      importer.setName(ShapeFileBuilder.NAME_ATTRIBUTE);
      importer.setId(ShapeFileBuilder.ID_ATTRIBUTE);
      importer.setParent(ShapeFileBuilder.PARENT_ATTRIBUTE);
      importer.setParentType(ShapeFileBuilder.PARENT_TYPE_ATTRIBUTE);
      importer.run(logger);

      Assert.assertTrue(logger.hasLogged());
      Assert.assertEquals(0, importer.getEntityIdMap().size());

      HashMap<String, Throwable> map = logger.getMap();

      // There should be an exception logged for every feature of the
      // shapefile
      Assert.assertEquals(100, map.size());

      for (String key : map.keySet())
      {
        Assert.assertTrue(map.get(key) instanceof AmbigiousGeoEntityException);
      }
    }
    finally
    {
      Map<String, String> map = importer.getEntityIdMap();

      for (String key : map.keySet())
      {
        String id = map.get(key);

        GeoEntity.get(id).delete();
      }
    }
  }

  @Request
  @Test
  public void testPartialSuccess() throws Exception
  {
    ShapeFileBuilder builder = new ShapeFileBuilder(SHAPE_FILE, parent)
    {
      public String getParentEntityName(int i)
      {
        if (i % 2 == 0)
        {
          return entity1.getDisplayLabel().getDefaultValue();
        }

        return parent.getDisplayLabel().getDefaultValue();
      }
    };
    builder.build();

    MockLogger logger = new MockLogger();
    ShapeFileImporter importer = new ShapeFileImporter(url);

    try
    {
      importer.setUniversalId(facility.getId());
      importer.setName(ShapeFileBuilder.NAME_ATTRIBUTE);
      importer.setId(ShapeFileBuilder.ID_ATTRIBUTE);
      importer.setParent(ShapeFileBuilder.PARENT_ATTRIBUTE);
      importer.setParentType(ShapeFileBuilder.PARENT_TYPE_ATTRIBUTE);
      importer.run(logger);

      Assert.assertTrue("Logger did not log any errors", logger.hasLogged());
      Assert.assertEquals(50, importer.getEntityIdMap().size());

      HashMap<String, Throwable> map = logger.getMap();

      // There should be an exception logged for every feature of the
      // shapefile
      Assert.assertEquals(50, map.size());

      for (String key : map.keySet())
      {
        Assert.assertTrue("Bad exception type [" + map.get(key).getClass().getName() + "]", map.get(key) instanceof AmbigiousGeoEntityException);
      }
    }
    finally
    {
      Map<String, String> map = importer.getEntityIdMap();

      for (String key : map.keySet())
      {
        String id = map.get(key);

        GeoEntity.get(id).delete();
      }
    }
  }

  @Request
  @Test
  public void testType() throws Exception
  {
    GISImportLoggerIF logger = new MockLogger();

    ShapeFileBuilder builder = new ShapeFileBuilder(SHAPE_FILE, parent);
    builder.build();

    ShapeFileImporter importer = new ShapeFileImporter(url);
    importer.setUniversalId(settlement.getId());
    importer.setType(ShapeFileBuilder.TYPE_ATTRIBUTE);
    importer.setName(ShapeFileBuilder.NAME_ATTRIBUTE);
    importer.setId(ShapeFileBuilder.ID_ATTRIBUTE);
    importer.setParent(ShapeFileBuilder.PARENT_ATTRIBUTE);
    importer.setParentType(ShapeFileBuilder.PARENT_TYPE_ATTRIBUTE);
    importer.run(logger);

    Map<String, String> map = importer.getEntityIdMap();

    try
    {
      Assert.assertFalse(logger.hasLogged());
      Assert.assertEquals(100, map.size());

      for (int i = 0; i < 100; i++)
      {
        GeoEntity entity = ShapeFileImporter.getByEntityName(builder.getName(i));

        Assert.assertNotNull(entity);

        OIterator<Term> parentsIter = entity.getDirectAncestors(LocatedIn.CLASS);
        List<Term> parents = parentsIter.getAll();
        
        Assert.assertEquals(facility, entity.getUniversal());
        Assert.assertEquals(builder.getName(i), entity.getDisplayLabel().getDefaultValue());
        Assert.assertEquals(builder.getGeoId(i), entity.getGeoId());
        Assert.assertEquals(1, parents.size());
        Assert.assertTrue(parents.contains(parent));
      }
    }
    finally
    {
      for (String key : map.keySet())
      {
        String id = map.get(key);

        GeoEntity.get(id).delete();
      }
    }
  }

  @Request
  @Test
  public void testUnknownType() throws Exception
  {
    MockLogger logger = new MockLogger();

    ShapeFileBuilder builder = new ShapeFileBuilder(SHAPE_FILE, parent)
    {
      @Override
      public String getType(int i)
      {
        return "Gibberish";
      }
    };
    builder.build();

    ShapeFileImporter importer = new ShapeFileImporter(url);
    importer.setUniversalId(settlement.getId());
    importer.setName(ShapeFileBuilder.NAME_ATTRIBUTE);
    importer.setId(ShapeFileBuilder.ID_ATTRIBUTE);
    importer.setType(ShapeFileBuilder.TYPE_ATTRIBUTE);
    importer.run(logger);

    try
    {

      Assert.assertTrue(logger.hasLogged());
      Assert.assertEquals(0, importer.getEntityIdMap().size());

      HashMap<String, Throwable> map = logger.getMap();

      // There should be an exception logged for every feature of the
      // shapefile
      Assert.assertEquals(100, map.size());

      for (String key : map.keySet())
      {
        Assert.assertTrue(map.get(key) instanceof RuntimeException);
      }
    }
    finally
    {
      Map<String, String> map = importer.getEntityIdMap();

      for (String key : map.keySet())
      {
        String id = map.get(key);

        GeoEntity.get(id).delete();
      }
    }
  }
}
