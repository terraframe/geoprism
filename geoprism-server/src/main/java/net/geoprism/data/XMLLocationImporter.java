/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.data;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
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

import net.geoprism.context.CountryDataConfiguration;
import net.geoprism.context.ProjectDataConfiguration;

public class XMLLocationImporter implements LocationImporter
{
  private static Logger logger = LoggerFactory.getLogger(XMLLocationImporter.class);

  private XMLEndpoint   endpoint;

  private boolean       rebuild;

  public XMLLocationImporter(XMLEndpoint endpoint)
  {
    this(endpoint, true);
  }

  public XMLLocationImporter(XMLEndpoint endpoint, boolean rebuild)
  {
    this.endpoint = endpoint;
    this.rebuild = rebuild;
  }

  @Override
  @Transaction
  public boolean loadProjectData(ProjectDataConfiguration configuration)
  {
    boolean additive = false;

    List<CountryDataConfiguration> countries = configuration.getCountries();
    
    logger.info("Location data will be imported for countries [" + StringUtils.join(countries, ", ") + "].");

    for (CountryDataConfiguration country : countries)
    {
      if (country.getFormat().equals("xml"))
      {
        boolean result = this.loadCountryData(country.getKey(), country.getVersion());

        additive = additive || result;
      }
    }

    if (rebuild && additive)
    {
      /*
       * New locations may have been added: We must re-initialize the all paths tables
       */
      // Heads up: Hierarchies - make this dynamic
      Universal.getStrategy().reinitialize(AllowedIn.CLASS);

      GeoEntity.getStrategy().reinitialize(LocatedIn.CLASS);
    }

    return additive;
  }

  public boolean loadCountryData(String country, String version)
  {
    logger.info("Importing location data for country [" + country + ":" + version + "].");
    
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
        this.endpoint.copyFiles(directory, list, false);

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
