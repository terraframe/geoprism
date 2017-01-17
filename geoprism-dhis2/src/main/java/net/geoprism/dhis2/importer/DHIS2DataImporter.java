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
package net.geoprism.dhis2.importer;

import java.io.File;
import java.sql.Savepoint;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.httpclient.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.geometry.GeometryHelper;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.Universal;
import com.vividsolutions.jts.geom.GeometryFactory;

import net.geoprism.account.OauthServer;
import net.geoprism.configuration.GeoprismConfigurationResolver;
import net.geoprism.dhis2.DHIS2BasicConnector;
import net.geoprism.dhis2.DHIS2ConflictException;

/**
 * This class is the main entrypoint for all DHIS2 data importing. Run the main method in this class to kick off a data import.
 * 
 * @author rrowlands
 *
 */
public class DHIS2DataImporter
{
  private DHIS2BasicConnector dhis2;
  
  private GeometryFactory     geometryFactory;

  private GeometryHelper      geometryHelper;
  
  private OrgUnitLevelJsonToUniversal[] universals;
  
  public static void main(String[] args)
  {
    CommandLineParser parser = new DefaultParser();
    Options options = new Options();
    options.addOption(Option.builder("url").hasArg().argName("url").longOpt("url").desc("URL of the DHIS2 server to connect to, including the port. Defaults to: http://127.0.0.1:8085/").optionalArg(true).build());
    options.addOption(Option.builder("username").hasArg().argName("username").longOpt("username").desc("The username of the root (admin) DHIS2 user.").required().build());
    options.addOption(Option.builder("password").hasArg().argName("password").longOpt("password").desc("The password for the root (admin) DHIS2 user.").required().build());
    options.addOption(Option.builder("appcfgPath").hasArg().argName("appcfgPath").longOpt("appcfgPath").desc("An absolute path to the external configuration directory for this geoprism app.").optionalArg(true).build());
    
    try {
      CommandLine line = parser.parse( options, args );
      
      String url = line.getOptionValue("url");
      String username = line.getOptionValue("username");
      String password = line.getOptionValue("password");
      String appcfgPath = line.getOptionValue("appcfgPath");
      
      if (url == null)
      {
        url = "http://127.0.0.1:8085/";
      }
      if (appcfgPath != null)
      {
        GeoprismConfigurationResolver resolver = (GeoprismConfigurationResolver) ConfigurationManager.Singleton.INSTANCE.getConfigResolver();
        resolver.setExternalConfigDir(new File(appcfgPath));
      }
      
      new DHIS2DataImporter(url, username, password).importAll();
    }
    catch (ParseException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public DHIS2DataImporter(String url, String username, String password)
  {
    this.geometryFactory = new GeometryFactory();
    this.geometryHelper = new GeometryHelper();
    
    dhis2 = new DHIS2BasicConnector(url, username, password);
  }

  @Request
  private void importAll()
  {
    importAllInTransaction();
  }
  
  @Transaction
  private void importAllInTransaction()
  {
    dhis2.initialize();
    importOrgUnitLevels();
    importOrgUnits();
    buildAllpaths();
  }
  
  public OrgUnitLevelJsonToUniversal getUniversalByLevel(int level)
  {
    return universals[level];
  }
  
  private void buildAllpaths()
  {
    Universal.getStrategy().reinitialize(AllowedIn.CLASS);
    GeoEntity.getStrategy().reinitialize(LocatedIn.CLASS);
//    Classifier.getStrategy().reinitialize(ClassifierIsARelationship.CLASS);
  }
  
  private void importOrgUnitLevels()
  {
    // curl -H "Accept: application/json" -u admin:district "http://localhost:8085/api/metadata?assumeTrue=false&organisationUnitLevels=true"
    JSONObject response = dhis2.httpGet("api/25/metadata", new NameValuePair[] {
        new NameValuePair("assumeTrue", "false"),
        new NameValuePair("organisationUnitLevels", "true")
    });
    
    // Create Universals from OrgUnitLevels
    JSONArray levels = response.getJSONArray("organisationUnitLevels");
    universals = new OrgUnitLevelJsonToUniversal[levels.length()];
    for (int i = 0; i < levels.length(); ++i)
    {
      JSONObject level = levels.getJSONObject(i);
      
      OrgUnitLevelJsonToUniversal converter = new OrgUnitLevelJsonToUniversal(this, level);
      converter.apply();
      
      universals[level.getInt("level")-1] = converter;
    }
    
    // Apply Universal relationships
    for (int i = 0; i < universals.length; ++i)
    {
      universals[i].applyLocatedIn();
    }
  }
  
  private void importOrgUnits()
  {
    // curl -H "Accept: application/json" -u admin:district "http://localhost:8085/api/metadata?assumeTrue=false&organisationUnits=true"
    JSONObject response = dhis2.httpGet("api/25/metadata", new NameValuePair[] {
        new NameValuePair("assumeTrue", "false"),
        new NameValuePair("organisationUnits", "true")
    });
    
    // Create GeoEntities from OrgUnits
    JSONArray units = response.getJSONArray("organisationUnits");
    OrgUnitJsonToGeoEntity[] converters = new OrgUnitJsonToGeoEntity[units.length()];
    for (int i = 0; i < units.length(); ++i)
    {
      JSONObject unit = units.getJSONObject(i);
      
      OrgUnitJsonToGeoEntity converter = new OrgUnitJsonToGeoEntity(this, geometryFactory, geometryHelper, unit);
      converter.apply();
      
      converters[i] = converter;
    }
    
    // Assign parents
    for (int i = 0; i < converters.length; ++i)
    {
      OrgUnitJsonToGeoEntity converter = converters[i];
      
      converter.applyLocatedIn();
    }
    
    // The OrgUnit parent reference is done by a DIHS2 id. In order to find the parent, we first set the GeoId to the DHIS2 internal id. Now we're swapping out those DHIS2 internal ids with geoids.
    // This could also be solved with a hashmap from   {DHIS2 id -> geoId}  to help us find the parent, but in the interest of saving memory I decided to loop through it again and hammer the processor instead.
    for (int i = 0; i < converters.length; ++i)
    {
      OrgUnitJsonToGeoEntity converter = converters[i];
      
      converter.swapGeoId();
    }
  }
}

