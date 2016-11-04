package com.teravation.solr;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.CSVReader;

/**
 * @author chris
 *
 *         This is the current interface to the Runway GeoTree. At this point it is simply an in-memory hashmap that get
 *         created from a CSV file that is exported from Runway. It only really cares about the location id (as the text
 *         is typically multiple tokens).
 */
// TODO Turn this into a more hierarchical version including parental lineage to handle uniqueness and sufficiency
// requirements
// TODO Actually access Runway directly, rather than through this im-memory map (if performance is good enough)
public class GeoTree
{
  private Map<String, GeoData> data = new HashMap<String, GeoData>();

  /**
   * @param filename
   * @throws FileNotFoundException
   * @throws IOException
   */
  public void loadData(String filename) throws FileNotFoundException, IOException
  {
    CSVReader reader = new CSVReader(new FileReader(filename));

    try
    {
      String[] nextLine;

      while ( ( nextLine = reader.readNext() ) != null)
      {
        this.addGeoData(nextLine);
      }
    }
    finally
    {
      reader.close();
    }
  }

  /**
   * @param name
   * @return
   */
  public GeoData get(String name)
  {
    return data.get(name);
  }

  /**
   * @param locationId
   * @param displayLabel
   */
  public void addGeoData(String[] line)
  {
    String label = line[0];
    String locationId = line[1];

    data.putIfAbsent(label, new GeoData(label));

    GeoData geoData = data.get(label);

    if (line.length > 2)
    {
      String[] parentIds = Arrays.copyOfRange(line, 2, line.length);

      geoData.addLocation(locationId, parentIds);
    }
    else
    {
      geoData.addLocation(locationId);
    }
  }

  public void addGeoData(String label, String locationId, Collection<String> parentIds)
  {
    data.putIfAbsent(label, new GeoData(label));

    GeoData geoData = data.get(label);

    if (parentIds.size() > 0)
    {
      geoData.addLocation(locationId, parentIds);
    }
    else
    {
      geoData.addLocation(locationId);
    }
  }
}
