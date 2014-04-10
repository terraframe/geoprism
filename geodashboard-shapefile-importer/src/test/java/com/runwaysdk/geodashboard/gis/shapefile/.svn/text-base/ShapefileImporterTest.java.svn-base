package dss.vector.solutions.gis.shapefile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.session.Request;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;

import dss.vector.solutions.AmbigiousGeoEntityException;
import dss.vector.solutions.AmbigiousTermException;
import dss.vector.solutions.TransactionExecuter;
import dss.vector.solutions.geo.GeoHierarchy;
import dss.vector.solutions.geo.generated.Country;
import dss.vector.solutions.geo.generated.Earth;
import dss.vector.solutions.geo.generated.GeoEntity;
import dss.vector.solutions.geo.generated.HealthFacility;
import dss.vector.solutions.gis.GISImportLoggerIF;
import dss.vector.solutions.gis.MockLogger;
import dss.vector.solutions.gis.MockTaskListener;
import dss.vector.solutions.gis.Task;
import dss.vector.solutions.ontology.Term;

public class ShapefileImporterTest
{
//  private static String    SHAPE_FILE = "shapefiles/shapefile.shp";
//
//  private static GeoEntity parent;
//
//  private static URL       url;
//
//  private static GeoEntity entity1;
//
//  private static GeoEntity entity2;
//
//  @BeforeClass
//  public static void classSetup() throws Exception
//  {
//    File file = new File(SHAPE_FILE);
//
//    if (!file.exists())
//    {
//      file.getParentFile().mkdirs();
//      file.createNewFile();
//    }
//
//    url = file.toURI().toURL();
//
//    new TransactionExecuter()
//    {
//
//      @Override
//      protected void executeMethod() throws Exception
//      {
//        parent = new Country();
//        parent.setGeoId("TestParent04");
//        parent.getEntityLabel().setDefaultValue("Test Parent Entity 2");
//        parent.apply();
//
//        entity1 = new Country();
//        entity1.setGeoId("TestParent05");
//        entity1.getEntityLabel().setDefaultValue("Test Parent Entity Duplicate");
//        entity1.apply();
//
//        entity2 = new Country();
//        entity2.setGeoId("TestParent06");
//        entity2.getEntityLabel().setDefaultValue("Test Parent Entity Duplicate");
//        entity2.apply();
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
//        parent.deleteEntity();
//        entity1.deleteEntity();
//        entity2.deleteEntity();
//      }
//    }.execute();
//  }
//
//  @Test
//  public void testGetAttribute() throws IOException, ParseException
//  {
//    new ShapefileBuilder(SHAPE_FILE, parent).build();
//
//    ShapefileImporter importer = new ShapefileImporter(url);
//
//    List<String> attributes = importer.getAttributes();
//
//    assertEquals(6, attributes.size());
//
//    assertTrue(attributes.contains(ShapefileBuilder.ID_ATTRIBUTE));
//    assertTrue(attributes.contains(ShapefileBuilder.NAME_ATTRIBUTE));
//    assertTrue(attributes.contains(ShapefileBuilder.PARENT_ATTRIBUTE));
//    assertTrue(attributes.contains(ShapefileBuilder.PARENT_TYPE_ATTRIBUTE));
//    assertTrue(attributes.contains(ShapefileBuilder.TYPE_ATTRIBUTE));
//    assertTrue(attributes.contains(ShapefileBuilder.SUBTYPE_ATTRIBUTE));
//  }
//
//  @Request
//  @Test
//  public void testImportWithGeneratedIds() throws Exception
//  {
//    ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, parent);
//    builder.build();
//
//    GISImportLoggerIF logger = new MockLogger();
//
//    ShapefileImporter importer = new ShapefileImporter(url);
//    importer.setUniversal(Country.CLASS);
//    importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//    importer.run(logger);
//
//    Map<String, String> map = importer.getEntityIdMap();
//
//    try
//    {
//      assertFalse(logger.hasLogged());
//      assertEquals(100, map.size());
//
//      for (int i = 0; i < 100; i++)
//      {
//        try
//        {
//          assertNotNull(ShapefileImporter.getByEntityName(builder.getName(i)));
//        }
//        catch (AmbigiousGeoEntityException e)
//        {
//          fail("Ambigious geo entity of the name [" + builder.getName(i) + "]");
//        }
//      }
//    }
//    finally
//    {
//      for (String key : map.keySet())
//      {
//        String id = map.get(key);
//
//        GeoEntity entity = GeoEntity.get(id);
//
//        entity.deleteEntity();
//      }
//    }
//  }
//
//  @Request
//  @Test
//  public void testImportWithIdAttribute() throws Exception
//  {
//    ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, parent);
//    builder.build();
//
//    Earth earth = Earth.getEarthInstance();
//
//    GISImportLoggerIF logger = new MockLogger();
//
//    ShapefileImporter importer = new ShapefileImporter(url);
//    importer.setUniversal(HealthFacility.CLASS);
//    importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//    importer.setId(ShapefileBuilder.ID_ATTRIBUTE);
//    importer.run(logger);
//
//    Map<String, String> map = importer.getEntityIdMap();
//
//    try
//    {
//      assertFalse(logger.hasLogged());
//      assertEquals(100, map.size());
//
//      for (int i = 0; i < 100; i++)
//      {
//        GeoEntity entity = ShapefileImporter.getByEntityName(builder.getName(i));
//
//        assertNotNull(entity);
//
//        List<GeoEntity> parents = entity.getAllParents();
//
//        assertEquals(builder.getName(i), entity.getEntityLabel().getDefaultValue());
//        assertEquals(builder.getGeoId(i), entity.getGeoId());
//        assertEquals(2, parents.size());
//        assertTrue(parents.contains(earth));
//        assertTrue(parents.contains(entity));
//      }
//    }
//    finally
//    {
//      for (String key : map.keySet())
//      {
//        String id = map.get(key);
//
//        GeoEntity.get(id).deleteEntity();
//      }
//    }
//  }
//
//  @Request
//  @Test
//  public void testImportWithNullIdAttribute() throws Exception
//  {
//    GISImportLoggerIF logger = new MockLogger();
//
//    ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, parent)
//    {
//      public String getGeoId(int i)
//      {
//        return "";
//      }
//    };
//    builder.build();
//
//    ShapefileImporter importer = new ShapefileImporter(url);
//    importer.setUniversal(Country.CLASS);
//    importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//    importer.setId(ShapefileBuilder.ID_ATTRIBUTE);
//    importer.run(logger);
//
//    Map<String, String> map = importer.getEntityIdMap();
//
//    try
//    {
//      assertFalse(logger.hasLogged());
//      assertEquals(100, map.size());
//
//      for (int i = 0; i < 100; i++)
//      {
//        GeoEntity entity = ShapefileImporter.getByEntityName(builder.getName(i));
//
//        assertNotNull(entity);
//        assertEquals(builder.getName(i), entity.getEntityLabel().getDefaultValue());
//        assertTrue(entity.getGeoId().length() > 0);
//      }
//    }
//    finally
//    {
//      for (String key : map.keySet())
//      {
//        String id = map.get(key);
//
//        GeoEntity.get(id).deleteEntity();
//      }
//    }
//  }
//
//  @Request
//  @Test
//  public void testMultipolygonImport() throws Exception
//  {
//    GISImportLoggerIF logger = new MockLogger();
//
//    ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, parent)
//    {
//      protected Geometry getGeometry(int i) throws ParseException
//      {
//        int j = i + 100;
//        int k = i + 200;
//
//        return reader.read("MULTIPOLYGON(((" + i + " " + i + "," + j + " " + j + "," + k + " " + k + "," + i + " " + i + "),(" + i + " " + j + "," + i + " " + k + "," + j + " " + j + "," + i + " " + j + ")),((" + j + " " + i + "," + j + " " + k + "," + k + " " + k + "," + j + " " + i + ")))");
//      };
//    };
//    builder.build();
//
//    ShapefileImporter importer = new ShapefileImporter(url);
//    importer.setUniversal(Country.CLASS);
//    importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//    importer.setId(ShapefileBuilder.ID_ATTRIBUTE);
//    importer.run(logger);
//
//    Map<String, String> map = importer.getEntityIdMap();
//
//    try
//    {
//      assertFalse(logger.hasLogged());
//      assertEquals(100, map.size());
//
//      for (int i = 0; i < 100; i++)
//      {
//        GeoEntity entity = ShapefileImporter.getByEntityName(builder.getName(i));
//
//        assertNotNull(entity);
//        assertEquals(builder.getName(i), entity.getEntityLabel().getDefaultValue());
//        assertEquals(builder.getGeoId(i), entity.getGeoId());
//      }
//    }
//    finally
//    {
//      for (String key : map.keySet())
//      {
//        String id = map.get(key);
//
//        GeoEntity.get(id).deleteEntity();
//      }
//    }
//  }
//
//  @Request
//  @Test
//  public void testLocatedIn() throws Exception
//  {
//    GISImportLoggerIF logger = new MockLogger();
//
//    ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, parent);
//    builder.build();
//
//    ShapefileImporter importer = new ShapefileImporter(url);
//    importer.setUniversal(HealthFacility.CLASS);
//    importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//    importer.setId(ShapefileBuilder.ID_ATTRIBUTE);
//    importer.setParent(ShapefileBuilder.PARENT_ATTRIBUTE);
//    importer.run(logger);
//
//    Map<String, String> map = importer.getEntityIdMap();
//
//    try
//    {
//      assertFalse(logger.hasLogged());
//      assertEquals(100, map.size());
//
//      for (int i = 0; i < 100; i++)
//      {
//        GeoEntity entity = ShapefileImporter.getByEntityName(builder.getName(i));
//
//        assertNotNull(entity);
//
//        List<GeoEntity> parents = entity.getAllParents();
//
//        assertEquals(builder.getName(i), entity.getEntityLabel().getDefaultValue());
//        assertEquals(builder.getGeoId(i), entity.getGeoId());
//        assertEquals(2, parents.size());
//        assertTrue(parents.contains(parent));
//        assertTrue(parents.contains(entity));
//      }
//    }
//    finally
//    {
//      for (String key : map.keySet())
//      {
//        String id = map.get(key);
//
//        GeoEntity.get(id).deleteEntity();
//      }
//    }
//  }
//
//  @Request
//  @Test
//  public void testUnknownLocatedIn() throws Exception
//  {
//    GISImportLoggerIF logger = new MockLogger();
//
//    ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, parent);
//    builder.build();
//
//    ShapefileImporter importer = new ShapefileImporter(url);
//    importer.setUniversal(HealthFacility.CLASS);
//    importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//    importer.setId(ShapefileBuilder.ID_ATTRIBUTE);
//    importer.setParent("bogusValue");
//    importer.run(logger);
//
//    Map<String, String> map = importer.getEntityIdMap();
//
//    Earth earth = Earth.getEarthInstance();
//
//    try
//    {
//      assertFalse(logger.hasLogged());
//      assertEquals(100, map.size());
//
//      for (int i = 0; i < 100; i++)
//      {
//        GeoEntity entity = ShapefileImporter.getByEntityName(builder.getName(i));
//
//        assertNotNull(entity);
//
//        List<GeoEntity> parents = entity.getAllParents();
//
//        assertEquals(builder.getName(i), entity.getEntityLabel().getDefaultValue());
//        assertEquals(builder.getGeoId(i), entity.getGeoId());
//        assertEquals(2, parents.size());
//        assertTrue(parents.contains(earth));
//        assertTrue(parents.contains(entity));
//      }
//    }
//    finally
//    {
//      for (String key : map.keySet())
//      {
//        String id = map.get(key);
//
//        GeoEntity.get(id).deleteEntity();
//      }
//    }
//  }
//
//  @Request
//  @Test
//  public void testLocatedInWithLocatedInType() throws Exception
//  {
//    GISImportLoggerIF logger = new MockLogger();
//
//    ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, parent);
//    builder.build();
//
//    ShapefileImporter importer = new ShapefileImporter(url);
//    importer.setUniversal(HealthFacility.CLASS);
//    importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//    importer.setId(ShapefileBuilder.ID_ATTRIBUTE);
//    importer.setParent(ShapefileBuilder.PARENT_ATTRIBUTE);
//    importer.setParentType(ShapefileBuilder.PARENT_TYPE_ATTRIBUTE);
//    importer.run(logger);
//
//    Map<String, String> map = importer.getEntityIdMap();
//
//    try
//    {
//      assertFalse(logger.hasLogged());
//      assertEquals(100, map.size());
//
//      for (int i = 0; i < 100; i++)
//      {
//        GeoEntity entity = ShapefileImporter.getByEntityName(builder.getName(i));
//
//        assertNotNull(entity);
//
//        List<GeoEntity> parents = entity.getAllParents();
//
//        assertEquals(builder.getName(i), entity.getEntityLabel().getDefaultValue());
//        assertEquals(builder.getGeoId(i), entity.getGeoId());
//        assertEquals(2, parents.size());
//        assertTrue(parents.contains(parent));
//        assertTrue(parents.contains(entity));
//      }
//    }
//    finally
//    {
//      for (String key : map.keySet())
//      {
//        String id = map.get(key);
//
//        GeoEntity.get(id).deleteEntity();
//      }
//    }
//  }
//
//  @Request
//  @Test
//  public void testNullLocatedIn() throws Exception
//  {
//    GISImportLoggerIF logger = new MockLogger();
//
//    ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, parent)
//    {
//      public String getParentEntityName(int i)
//      {
//        return "";
//      }
//    };
//    builder.build();
//
//    ShapefileImporter importer = new ShapefileImporter(url);
//    importer.setUniversal(HealthFacility.CLASS);
//    importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//    importer.setId(ShapefileBuilder.ID_ATTRIBUTE);
//    importer.setParent(ShapefileBuilder.PARENT_ATTRIBUTE);
//    importer.run(logger);
//
//    Map<String, String> map = importer.getEntityIdMap();
//
//    Earth earth = Earth.getEarthInstance();
//
//    try
//    {
//      assertFalse(logger.hasLogged());
//      assertEquals(100, map.size());
//
//      for (int i = 0; i < 100; i++)
//      {
//        GeoEntity entity = ShapefileImporter.getByEntityName(builder.getName(i));
//
//        assertNotNull(entity);
//
//        List<GeoEntity> parents = entity.getAllParents();
//
//        assertEquals(builder.getName(i), entity.getEntityLabel().getDefaultValue());
//        assertEquals(builder.getGeoId(i), entity.getGeoId());
//        assertEquals(2, parents.size());
//        assertTrue(parents.contains(earth));
//        assertTrue(parents.contains(entity));
//      }
//    }
//    finally
//    {
//      for (String key : map.keySet())
//      {
//        String id = map.get(key);
//
//        GeoEntity.get(id).deleteEntity();
//      }
//    }
//  }
//
//  @Request
//  @Test
//  public void testLocatedInWithNullLocatedInType() throws Exception
//  {
//    GISImportLoggerIF logger = new MockLogger();
//
//    ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, parent)
//    {
//      public String getParentType(int i)
//      {
//        return "";
//      }
//    };
//    builder.build();
//
//    ShapefileImporter importer = new ShapefileImporter(url);
//    importer.setUniversal(HealthFacility.CLASS);
//    importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//    importer.setId(ShapefileBuilder.ID_ATTRIBUTE);
//    importer.setParent(ShapefileBuilder.PARENT_ATTRIBUTE);
//    importer.setParentType(ShapefileBuilder.PARENT_ATTRIBUTE);
//    importer.run(logger);
//
//    Map<String, String> map = importer.getEntityIdMap();
//
//    try
//    {
//      assertFalse(logger.hasLogged());
//      assertEquals(100, map.size());
//
//      for (int i = 0; i < 100; i++)
//      {
//        GeoEntity entity = ShapefileImporter.getByEntityName(builder.getName(i));
//
//        assertNotNull(entity);
//
//        List<GeoEntity> parents = entity.getAllParents();
//
//        assertEquals(builder.getName(i), entity.getEntityLabel().getDefaultValue());
//        assertEquals(builder.getGeoId(i), entity.getGeoId());
//        assertEquals(2, parents.size());
//        assertTrue(parents.contains(parent));
//        assertTrue(parents.contains(entity));
//      }
//    }
//    finally
//    {
//      for (String key : map.keySet())
//      {
//        String id = map.get(key);
//
//        GeoEntity.get(id).deleteEntity();
//      }
//    }
//  }
//
//  @Request
//  @Test
//  public void testLocatedInWithUnknownLocatedInType() throws Exception
//  {
//    GISImportLoggerIF logger = new MockLogger();
//
//    ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, parent)
//    {
//      public String getParentType(int i)
//      {
//        return "NoAValidUniversal";
//      }
//    };
//    builder.build();
//
//    ShapefileImporter importer = new ShapefileImporter(url);
//    importer.setUniversal(HealthFacility.CLASS);
//    importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//    importer.setId(ShapefileBuilder.ID_ATTRIBUTE);
//    importer.setParent(ShapefileBuilder.PARENT_ATTRIBUTE);
//    importer.setParentType(ShapefileBuilder.PARENT_TYPE_ATTRIBUTE);
//    importer.run(logger);
//
//    Map<String, String> map = importer.getEntityIdMap();
//
//    try
//    {
//      assertFalse(logger.hasLogged());
//      assertEquals(100, map.size());
//
//      for (int i = 0; i < 100; i++)
//      {
//        GeoEntity entity = ShapefileImporter.getByEntityName(builder.getName(i));
//
//        assertNotNull(entity);
//
//        List<GeoEntity> parents = entity.getAllParents();
//
//        assertEquals(builder.getName(i), entity.getEntityLabel().getDefaultValue());
//        assertEquals(builder.getGeoId(i), entity.getGeoId());
//        assertEquals(2, parents.size());
//        assertTrue(parents.contains(parent));
//        assertTrue(parents.contains(entity));
//      }
//    }
//    finally
//    {
//      for (String key : map.keySet())
//      {
//        String id = map.get(key);
//
//        GeoEntity.get(id).deleteEntity();
//      }
//    }
//  }
//
//  @Request
//  @Test
//  public void testTaskListener() throws Exception
//  {
//    GISImportLoggerIF logger = new MockLogger();
//
//    ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, parent);
//    builder.build();
//
//    Earth earth = Earth.getEarthInstance();
//
//    MockTaskListener listener = new MockTaskListener();
//
//    ShapefileImporter importer = new ShapefileImporter(url);
//    importer.setUniversal(HealthFacility.CLASS);
//    importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//    importer.setId(ShapefileBuilder.ID_ATTRIBUTE);
//    importer.addListener(listener);
//    importer.run(logger);
//
//    Map<String, String> map = importer.getEntityIdMap();
//
//    try
//    {
//      assertFalse(logger.hasLogged());
//      assertEquals(100, map.size());
//
//      for (int i = 0; i < 100; i++)
//      {
//        GeoEntity entity = ShapefileImporter.getByEntityName(builder.getName(i));
//
//        assertNotNull(entity);
//
//        List<GeoEntity> parents = entity.getAllParents();
//
//        assertEquals(builder.getName(i), entity.getEntityLabel().getDefaultValue());
//        assertEquals(builder.getGeoId(i), entity.getGeoId());
//        assertEquals(2, parents.size());
//        assertTrue(parents.contains(earth));
//        assertTrue(parents.contains(entity));
//      }
//
//      assertEquals(true, listener.isDone());
//      assertEquals(true, listener.isStatus());
//      assertEquals(true, listener.isStarted());
//
//      List<Task> tasks = listener.getTasks();
//
//      assertEquals(2, tasks.size());
//      assertEquals(-1, tasks.get(0).getTotal());
//      assertEquals(100, tasks.get(1).getTotal());
//
//      assertEquals(0, tasks.get(0).getWork());
//      assertEquals(100, tasks.get(1).getWork());
//    }
//    finally
//    {
//      for (String key : map.keySet())
//      {
//        String id = map.get(key);
//
//        GeoEntity.get(id).deleteEntity();
//      }
//    }
//  }
//
//  @Request
//  @Test
//  public void testDuplicateLocatedIn() throws Exception
//  {
//    MockLogger logger = new MockLogger();
//
//    ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, entity1);
//    builder.build();
//
//    ShapefileImporter importer = new ShapefileImporter(url);
//
//    try
//    {
//      importer.setUniversal(HealthFacility.CLASS);
//      importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//      importer.setId(ShapefileBuilder.ID_ATTRIBUTE);
//      importer.setParent(ShapefileBuilder.PARENT_ATTRIBUTE);
//      importer.run(logger);
//
//      assertTrue(logger.hasLogged());
//      assertEquals(0, importer.getEntityIdMap().size());
//
//      HashMap<String, Throwable> map = logger.getMap();
//
//      // There should be an exception logged for every feature of the
//      // shapefile
//      assertEquals(100, map.size());
//
//      for (String key : map.keySet())
//      {
//        assertTrue(map.get(key) instanceof AmbigiousGeoEntityException);
//      }
//    }
//    finally
//    {
//      Map<String, String> map = importer.getEntityIdMap();
//
//      for (String key : map.keySet())
//      {
//        String id = map.get(key);
//
//        GeoEntity.get(id).deleteEntity();
//      }
//    }
//  }
//
//  @Request
//  @Test
//  public void testDuplicateLocatedInWithType() throws Exception
//  {
//    MockLogger logger = new MockLogger();
//
//    ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, entity1);
//    builder.build();
//
//    ShapefileImporter importer = new ShapefileImporter(url);
//    try
//    {
//      importer.setUniversal(HealthFacility.CLASS);
//      importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//      importer.setId(ShapefileBuilder.ID_ATTRIBUTE);
//      importer.setParent(ShapefileBuilder.PARENT_ATTRIBUTE);
//      importer.setParentType(ShapefileBuilder.PARENT_TYPE_ATTRIBUTE);
//      importer.run(logger);
//
//      assertTrue(logger.hasLogged());
//      assertEquals(0, importer.getEntityIdMap().size());
//
//      HashMap<String, Throwable> map = logger.getMap();
//
//      // There should be an exception logged for every feature of the
//      // shapefile
//      assertEquals(100, map.size());
//
//      for (String key : map.keySet())
//      {
//        assertTrue(map.get(key) instanceof AmbigiousGeoEntityException);
//      }
//    }
//    finally
//    {
//      Map<String, String> map = importer.getEntityIdMap();
//
//      for (String key : map.keySet())
//      {
//        String id = map.get(key);
//
//        GeoEntity.get(id).deleteEntity();
//      }
//    }
//  }
//
//  @Request
//  @Test
//  public void testPartialSuccess() throws Exception
//  {
//    ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, parent)
//    {
//      public String getParentEntityName(int i)
//      {
//        if (i % 2 == 0)
//        {
//          return entity1.getEntityLabel().getDefaultValue();
//        }
//
//        return parent.getEntityLabel().getDefaultValue();
//      }
//    };
//    builder.build();
//
//    MockLogger logger = new MockLogger();
//    ShapefileImporter importer = new ShapefileImporter(url);
//
//    try
//    {
//      importer.setUniversal(HealthFacility.CLASS);
//      importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//      importer.setId(ShapefileBuilder.ID_ATTRIBUTE);
//      importer.setParent(ShapefileBuilder.PARENT_ATTRIBUTE);
//      importer.setParentType(ShapefileBuilder.PARENT_TYPE_ATTRIBUTE);
//      importer.run(logger);
//
//      assertTrue("Logger did not log any errors", logger.hasLogged());
//      assertEquals(50, importer.getEntityIdMap().size());
//
//      HashMap<String, Throwable> map = logger.getMap();
//
//      // There should be an exception logged for every feature of the
//      // shapefile
//      assertEquals(50, map.size());
//
//      for (String key : map.keySet())
//      {
//        assertTrue(map.get(key) instanceof AmbigiousGeoEntityException);
//      }
//    }
//    finally
//    {
//      Map<String, String> map = importer.getEntityIdMap();
//
//      for (String key : map.keySet())
//      {
//        String id = map.get(key);
//
//        GeoEntity.get(id).deleteEntity();
//      }
//    }
//  }
//
//  @Request
//  @Test
//  public void testType() throws Exception
//  {
//    GISImportLoggerIF logger = new MockLogger();
//
//    ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, parent);
//    builder.build();
//
//    ShapefileImporter importer = new ShapefileImporter(url);
//    importer.setUniversal(HealthFacility.CLASS);
//    importer.setType(ShapefileBuilder.TYPE_ATTRIBUTE);
//    importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//    importer.setId(ShapefileBuilder.ID_ATTRIBUTE);
//    importer.run(logger);
//
//    Map<String, String> map = importer.getEntityIdMap();
//
//    try
//    {
//      assertFalse(logger.hasLogged());
//      assertEquals(100, map.size());
//
//      for (int i = 0; i < 100; i++)
//      {
//        GeoEntity entity = ShapefileImporter.getByEntityName(builder.getName(i));
//
//        assertNotNull(entity);
//
//        List<GeoEntity> parents = entity.getAllParents();
//
//        assertEquals(Settlement.CLASS, entity.getType());
//        assertEquals(builder.getName(i), entity.getEntityLabel().getDefaultValue());
//        assertEquals(builder.getGeoId(i), entity.getGeoId());
//        assertEquals(2, parents.size());
//        assertTrue(parents.contains(entity));
//      }
//    }
//    finally
//    {
//      for (String key : map.keySet())
//      {
//        String id = map.get(key);
//
//        GeoEntity.get(id).deleteEntity();
//      }
//    }
//  }
//
//  @Request
//  @Test
//  public void testSubtype() throws Exception
//  {
//    Earth earth = Earth.getEarthInstance();
//    Term city = ShapefileImporter.getSubtype("City", "dss.vector.solutions.geo.generated", "Settlement");
//    GISImportLoggerIF logger = new MockLogger();
//
//    ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, parent);
//    builder.build();
//
//    ShapefileImporter importer = new ShapefileImporter(url);
//    importer.setUniversal(Settlement.CLASS);
//    importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//    importer.setId(ShapefileBuilder.ID_ATTRIBUTE);
//    importer.setSubtype(ShapefileBuilder.SUBTYPE_ATTRIBUTE);
//    importer.run(logger);
//
//    Map<String, String> map = importer.getEntityIdMap();
//
//    try
//    {
//      assertFalse(logger.hasLogged());
//      assertEquals(100, map.size());
//
//      for (int i = 0; i < 100; i++)
//      {
//        GeoEntity entity = ShapefileImporter.getByEntityName(builder.getName(i));
//
//        assertNotNull(entity);
//
//        List<GeoEntity> parents = entity.getAllParents();
//
//        assertEquals(builder.getName(i), entity.getEntityLabel().getDefaultValue());
//        assertEquals(builder.getGeoId(i), entity.getGeoId());
//        assertEquals(2, parents.size());
//        assertTrue(parents.contains(earth));
//        assertTrue(parents.contains(entity));
//        assertNotNull(entity.getTerm());
//        assertEquals(city.getId(), entity.getTerm().getId());
//      }
//    }
//    finally
//    {
//      for (String key : map.keySet())
//      {
//        String id = map.get(key);
//
//        GeoEntity.get(id).deleteEntity();
//      }
//    }
//  }
//
//  @Request
//  @Test
//  public void testEmptySubtype() throws Exception
//  {
//    Earth earth = Earth.getEarthInstance();
//    GISImportLoggerIF logger = new MockLogger();
//
//    ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, parent)
//    {
//      @Override
//      public String getSubtype(int i)
//      {
//        return "";
//      }
//
//    };
//    builder.build();
//
//    ShapefileImporter importer = new ShapefileImporter(url);
//    importer.setUniversal(HealthFacility.CLASS);
//    importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//    importer.setId(ShapefileBuilder.ID_ATTRIBUTE);
//    importer.setSubtype(ShapefileBuilder.SUBTYPE_ATTRIBUTE);
//    importer.run(logger);
//
//    Map<String, String> map = importer.getEntityIdMap();
//
//    try
//    {
//      assertFalse(logger.hasLogged());
//      assertEquals(100, map.size());
//
//      for (int i = 0; i < 100; i++)
//      {
//        GeoEntity entity = ShapefileImporter.getByEntityName(builder.getName(i));
//
//        assertNotNull(entity);
//
//        List<GeoEntity> parents = entity.getAllParents();
//
//        assertEquals(builder.getName(i), entity.getEntityLabel().getDefaultValue());
//        assertEquals(builder.getGeoId(i), entity.getGeoId());
//        assertEquals(2, parents.size());
//        assertTrue(parents.contains(earth));
//        assertTrue(parents.contains(entity));
//        assertNull(entity.getTerm());
//      }
//    }
//    finally
//    {
//      for (String key : map.keySet())
//      {
//        String id = map.get(key);
//
//        GeoEntity.get(id).deleteEntity();
//      }
//    }
//  }
//
//  @Request
//  @Test
//  public void testAmbigiousSubtype() throws Exception
//  {
//    Term settlementTerm = this.getSettlementTerm();
//
//    final String label = "TestTcerm";
//
//    Term term1 = new Term();
//    term1.setTermId("TermTest1");
//    term1.setName(label);
//    term1.getTermDisplayLabel().setValue(label);
//    term1.applyWithParent(settlementTerm.getId(), false, "", false);
//
//    try
//    {
//      Term term2 = new Term();
//      term2.setTermId("TermTest2");
//      term2.setName(label);
//      term2.getTermDisplayLabel().setValue(label);
//      term2.applyWithParent(settlementTerm.getId(), false, "", false);
//
//      try
//      {
//
//        MockLogger logger = new MockLogger();
//
//        ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, parent)
//        {
//          @Override
//          public String getSubtype(int i)
//          {
//            return label;
//          }
//
//        };
//        builder.build();
//
//        ShapefileImporter importer = new ShapefileImporter(url);
//        importer.setUniversal(Settlement.CLASS);
//        importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//        importer.setId(ShapefileBuilder.ID_ATTRIBUTE);
//        importer.setSubtype(ShapefileBuilder.SUBTYPE_ATTRIBUTE);
//        importer.run(logger);
//
//        try
//        {
//
//          assertTrue(logger.hasLogged());
//          assertEquals(0, importer.getEntityIdMap().size());
//
//          HashMap<String, Throwable> map = logger.getMap();
//
//          // There should be an exception logged for every feature of the
//          // shapefile
//          assertEquals(100, map.size());
//
//          for (String key : map.keySet())
//          {
//            assertTrue(map.get(key) instanceof AmbigiousTermException);
//          }
//        }
//        finally
//        {
//          Map<String, String> map = importer.getEntityIdMap();
//
//          for (String key : map.keySet())
//          {
//            String id = map.get(key);
//
//            GeoEntity.get(id).deleteEntity();
//          }
//        }
//      }
//      finally
//      {
//        term2.deleteTerm();
//      }
//    }
//    finally
//    {
//      term1.deleteTerm();
//    }
//  }
//
//  @Request
//  @Test
//  public void testSubtypeWithDifferentRoot() throws Exception
//  {
//    final String label = "TestTcerm";
//
//    Term term1 = new Term();
//    term1.setTermId("TermTest1");
//    term1.setName(label);
//    term1.getTermDisplayLabel().setValue(label);
//    term1.apply();
//
//    try
//    {
//      Term term2 = new Term();
//      term2.setTermId("TermTest2");
//      term2.setName(label);
//      term2.getTermDisplayLabel().setValue(label);
//      term2.apply();
//
//      try
//      {
//
//        MockLogger logger = new MockLogger();
//
//        ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, parent)
//        {
//          @Override
//          public String getSubtype(int i)
//          {
//            return label;
//          }
//
//        };
//        builder.build();
//
//        ShapefileImporter importer = new ShapefileImporter(url);
//        importer.setUniversal(Settlement.CLASS);
//        importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//        importer.setId(ShapefileBuilder.ID_ATTRIBUTE);
//        importer.setSubtype(ShapefileBuilder.SUBTYPE_ATTRIBUTE);
//        importer.run(logger);
//
//        try
//        {
//
//          assertTrue(logger.hasLogged());
//          assertEquals(0, importer.getEntityIdMap().size());
//
//          HashMap<String, Throwable> map = logger.getMap();
//
//          // There should be an exception logged for every feature of the
//          // shapefile
//          assertEquals(100, map.size());
//
//          for (String key : map.keySet())
//          {
//            assertTrue(map.get(key) instanceof RuntimeException);
//          }
//        }
//        finally
//        {
//          Map<String, String> map = importer.getEntityIdMap();
//
//          for (String key : map.keySet())
//          {
//            String id = map.get(key);
//
//            GeoEntity.get(id).deleteEntity();
//          }
//        }
//      }
//      finally
//      {
//        term2.deleteTerm();
//      }
//    }
//    finally
//    {
//      term1.deleteTerm();
//    }
//  }
//
//  @Request
//  @Test
//  public void testUnknownType() throws Exception
//  {
//    MockLogger logger = new MockLogger();
//
//    ShapefileBuilder builder = new ShapefileBuilder(SHAPE_FILE, parent)
//    {
//      @Override
//      public String getType(int i)
//      {
//        return "Gibberish";
//      }
//    };
//    builder.build();
//
//    ShapefileImporter importer = new ShapefileImporter(url);
//    importer.setUniversal(Settlement.CLASS);
//    importer.setName(ShapefileBuilder.NAME_ATTRIBUTE);
//    importer.setId(ShapefileBuilder.ID_ATTRIBUTE);
//    importer.setType(ShapefileBuilder.TYPE_ATTRIBUTE);
//    importer.run(logger);
//
//    try
//    {
//
//      assertTrue(logger.hasLogged());
//      assertEquals(0, importer.getEntityIdMap().size());
//
//      HashMap<String, Throwable> map = logger.getMap();
//
//      // There should be an exception logged for every feature of the
//      // shapefile
//      assertEquals(100, map.size());
//
//      for (String key : map.keySet())
//      {
//        assertTrue(map.get(key) instanceof RuntimeException);
//      }
//    }
//    finally
//    {
//      Map<String, String> map = importer.getEntityIdMap();
//
//      for (String key : map.keySet())
//      {
//        String id = map.get(key);
//
//        GeoEntity.get(id).deleteEntity();
//      }
//    }
//  }
//
//  public Term getSettlementTerm()
//  {
//    GeoHierarchy hierarchy = GeoHierarchy.getGeoHierarchyFromLabel("Settlement");
//
//    return hierarchy.getTerm();
//  }
}
