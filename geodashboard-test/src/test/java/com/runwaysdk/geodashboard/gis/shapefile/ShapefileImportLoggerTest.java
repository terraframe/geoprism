package com.runwaysdk.geodashboard.gis.shapefile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.runwaysdk.geodashboard.gis.GISImportLogger;
import com.runwaysdk.util.FileIO;

public class ShapefileImportLoggerTest
{
  public static String FILE = "output/import.log";

  @Before
  public void startup() throws Exception
  {
    File file = new File(FILE);

    if (!file.exists())
    {
      file.getParentFile().mkdirs();
    }
  }

  @After
  public void tearDown() throws Exception
  {
    FileIO.deleteFile(new File(FILE));
  }

  /**
   * Ensure that a log file is created on the file system and the contents of
   * the file are logged correctly.
   * 
   * @throws Exception
   */
  @Test
  public void testLog() throws Exception
  {
    File file = new File(FILE);

    GISImportLogger logger = new GISImportLogger(file);

    logger.log("123", new RuntimeException("This is a test log"));

    logger.close();

    Assert.assertTrue(logger.hasLogged());
    Assert.assertTrue(file.exists());

    BufferedReader reader = new BufferedReader(new FileReader(file));

    Assert.assertTrue(reader.ready());

    while (reader.ready())
    {
      Assert.assertEquals("123 : This is a test log", reader.readLine());
    }
  }

  /**
   * Ensure that the logger doesn't create file if nothing is logged
   * 
   * @throws Exception
   */
  @Test
  public void testEmptyLog() throws Exception
  {
    File file = new File(FILE);

    Assert.assertFalse(file.exists());

    GISImportLogger logger = new GISImportLogger(file);
    logger.close();

    Assert.assertFalse(logger.hasLogged());
    Assert.assertFalse(file.exists());
  }
}
