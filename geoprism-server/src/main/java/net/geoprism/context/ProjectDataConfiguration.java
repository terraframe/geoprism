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
package net.geoprism.context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.RunwayConfigurationException;

import net.geoprism.configuration.GeoprismConfigGroup;

public class ProjectDataConfiguration
{
  private static Logger                  logger = LoggerFactory.getLogger(ProjectDataConfiguration.class);

  private List<CountryDataConfiguration> countries;

  public ProjectDataConfiguration()
  {
    this.countries = new LinkedList<CountryDataConfiguration>();

    try
    {
      InputStream stream = ConfigurationManager.getResourceAsStream(GeoprismConfigGroup.ROOT, "project.json");
      
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

      try
      {
        String content = reader.lines().collect(Collectors.joining("\n"));

        JSONObject json = new JSONObject(content);
        JSONArray countries = json.getJSONArray("countries");

        for (int i = 0; i < countries.length(); i++)
        {
          JSONObject country = countries.getJSONObject(i);

          String key = country.getString("key");
          String version = country.getString("version");
          String format = country.getString("format");

          this.countries.add(new CountryDataConfiguration(key, version, format));
        }
      }
      finally
      {
        reader.close();
      }
    }
    catch (IOException | JSONException | RunwayConfigurationException e)
    {
      logger.error("Unable to load project.json", e);
    }
  }

  public List<CountryDataConfiguration> getCountries()
  {
    return countries;
  }

  public void addCountry(CountryDataConfiguration contry)
  {
    this.countries.add(contry);
  }
}
