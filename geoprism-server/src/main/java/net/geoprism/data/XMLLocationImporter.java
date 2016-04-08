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
package net.geoprism.data;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import net.geoprism.context.CountryDataConfiguration;
import net.geoprism.context.ProjectDataConfiguration;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.instance.VersioningUnzipper;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.Universal;
import com.runwaysdk.util.IDGenerator;

public class XMLLocationImporter implements LocationImporter
{
  private static Logger logger = LoggerFactory.getLogger(XMLLocationImporter.class);

  private XMLEndpoint   endpoint;

  public XMLLocationImporter(XMLEndpoint endpoint)
  {
    this.endpoint = endpoint;
  }

  @Override
  @Transaction
  public boolean loadProjectData(ProjectDataConfiguration configuration)
  {
    boolean additive = false;

    List<CountryDataConfiguration> countries = configuration.getCountries();

    for (CountryDataConfiguration country : countries)
    {
      if (country.getFormat().equals("xml"))
      {
        boolean result = this.loadCountryData(country.getKey(), country.getVersion());

        additive = additive || result;
      }
    }

    if (additive)
    {
      /*
       * New locations may have been added: We must re-initialize the all paths tables
       */
      Universal.getStrategy().reinitialize(AllowedIn.CLASS);

      GeoEntity.getStrategy().reinitialize(LocatedIn.CLASS);
    }

    return additive;
  }

  public boolean loadCountryData(String country, String version)
  {
    boolean additive = false;

    Set<Date> timestamps = this.getTimestamps();
    
    /*
     * First: Load the universals
     */
    additive = this.loadUniversals(country, version, timestamps) || additive;
    
    /*
     * Second: Load the entities
     */
    additive = this.loadGeoEntities(country, version, timestamps) || additive;

    return additive;
  }

  private Set<Date> getTimestamps()
  {
    Set<Date> timestamps = new TreeSet<Date>();

    List<String> values = Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY);

    for (String timestamp : values)
    {
      timestamps.add(new Date(Long.parseLong(timestamp)));
    }

    return timestamps;
  }

  private boolean loadGeoEntities(String country, String version, Set<Date> timestamps)
  {
    List<String> keys = this.endpoint.listGeoEntityKeys(country, version);

    if (keys.size() == 0)
    {
      logger.error("No universal endpoint keys for (" + country + "," + version + ")");
    }

    return this.loadFiles(keys, timestamps);
  }

  private boolean loadUniversals(String country, String version, Set<Date> timestamps)
  {
    List<String> keys = this.endpoint.listUniversalKeys(country, version);

    if (keys.size() == 0)
    {
      logger.error("No universal endpoint keys for (" + country + "," + version + ")");
    }

    return this.loadFiles(keys, timestamps);
  }

  private boolean loadFiles(List<String> keys, Set<Date> timestamps)
  {
    /*
     * IMPORTANT: Actually getting the files from the endpoint could be expensive so only grab the files which haven't
     * already been imported.
     */
    TimestampPredicate predicate = new TimestampPredicate(timestamps);
    List<String> list = keys.stream().filter(predicate).collect(Collectors.toList());

    if (list.size() > 0)
    {
      File directory = new File(FileUtils.getTempDirectory(), IDGenerator.nextID());
      directory.mkdirs();

      try
      {
        this.endpoint.copyFiles(directory, list);

        VersioningUnzipper.processZipDir(directory.getAbsolutePath());

        return true;
      }
      finally
      {
        try
        {
          FileUtils.deleteDirectory(directory);
        }
        catch (IOException e)
        {
          throw new ProgrammingErrorException(e);
        }
      }
    }

    return false;
  }
}
