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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.geometry.GeometryHelper;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.AllowedIn;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.LocatedIn;
import com.runwaysdk.system.gis.geo.Universal;
import com.vividsolutions.jts.geom.GeometryFactory;

import net.geoprism.configuration.GeoprismConfigurationResolver;
import net.geoprism.dhis2.DHIS2Configuration;
import net.geoprism.dhis2.connector.AbstractDHIS2Connector;
import net.geoprism.dhis2.connector.DHIS2HTTPCredentialConnector;
import net.geoprism.dhis2.response.DHIS2TrackerResponseProcessor;
import net.geoprism.dhis2.response.HTTPResponse;
import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierIsARelationship;

/**
 * This class is the main entrypoint for all DHIS2 data importing. Run the main method in this class to kick off a data import.
 * 
 * @author rrowlands
 *
 */
public class DHIS2DataImporter
{
  private AbstractDHIS2Connector dhis2;
  
  private GeometryFactory     geometryFactory;

  private GeometryHelper      geometryHelper;
  
  private OrgUnitLevelJsonToUniversal[] universals;
  
//  private String[] countryOrgUnitExcludes;
  
  private String countryOrgUnitId;
  
  public static void main(String[] args)
  {
    CommandLineParser parser = new DefaultParser();
    Options options = new Options();
    options.addOption(Option.builder("url").hasArg().argName("url").longOpt("url").desc("URL of the DHIS2 server to connect to, including the port. Defaults to: http://127.0.0.1:8085/").optionalArg(true).build());
    options.addOption(Option.builder("username").hasArg().argName("username").longOpt("username").desc("The username of the root (admin) DHIS2 user.").required().build());
    options.addOption(Option.builder("password").hasArg().argName("password").longOpt("password").desc("The password for the root (admin) DHIS2 user.").required().build());
    options.addOption(Option.builder("appcfgPath").hasArg().argName("appcfgPath").longOpt("appcfgPath").desc("An absolute path to the external configuration directory for this geoprism app.").optionalArg(true).build());
    options.addOption(Option.builder("countryOrgUnitId").hasArg().argName("countryOrgUnitId").longOpt("countryOrgUnitId").desc("DHIS2 does not support multiple countries. However, some systems are misconfigured and have multiple countries. Our importer does not support this. You must hardcode the real country org unit to get this importer to work.").optionalArg(true).build());
    
    try {
      CommandLine line = parser.parse( options, args );
      
      String url = line.getOptionValue("url");
      String username = line.getOptionValue("username");
      String password = line.getOptionValue("password");
      String appcfgPath = line.getOptionValue("appcfgPath");
      String countryOrgUnitId = line.getOptionValue("countryOrgUnitId");
      
      if (url == null)
      {
        url = "http://127.0.0.1:8085/";
      }
      if (appcfgPath != null)
      {
        GeoprismConfigurationResolver resolver = (GeoprismConfigurationResolver) ConfigurationManager.Singleton.INSTANCE.getConfigResolver();
        resolver.setExternalConfigDir(new File(appcfgPath));
      }
      
      doImportInRequest(url, username, password, countryOrgUnitId);
    }
    catch (ParseException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  // Don't ever put an @Request on a main method or you will regret it I promise you
  @Request
  public static void doImportInRequest(String url, String username, String password, String countryOrgUnitId)
  {
    DHIS2Configuration config = DHIS2Configuration.getByKey("DEFAULT");
    config.setPazzword(password);
    config.setUsername(username);
    config.setUrl(url);
    config.appLock();
    config.apply();
    
    new DHIS2DataImporter(url, username, password, countryOrgUnitId).importAll();
  }
  
  public DHIS2DataImporter(String url, String username, String password, String countryOrgUnitId)
  {
    this.geometryFactory = new GeometryFactory();
    this.geometryHelper = new GeometryHelper();
    
    this.countryOrgUnitId = countryOrgUnitId;
    
    dhis2 = new DHIS2HTTPCredentialConnector();
    dhis2.setServerUrl(url);
    dhis2.setCredentials(username, password);
  }

  private void importAll()
  {
    importAllInTransaction();
  }
  
  @Transaction
  private void importAllInTransaction()
  {
    deleteAll();
    
    importOrgUnitLevels();
    importOrgUnits();
    buildAllpaths();
    
    importOptionSets();
    importOptions();
    importOptionSetRelationships();
  }
  
  // TODO: Create or Update is a lot harder than just deleting everything
  private void deleteAll()
  {
    Database.executeStatement("truncate geo_entity;");
    Database.executeStatement("truncate geo_entity_problem;");
    Database.executeStatement("truncate universal;");
    Database.executeStatement("truncate located_in;");
    Database.executeStatement("truncate allowed_in;");
    Database.executeStatement("truncate classifier;");
    Database.executeStatement("truncate classifier_is_a_relationship;");
    Database.executeStatement("truncate rwsynonym;");
    Database.executeStatement("truncate synonym_relationship;");
    Database.executeStatement("truncate dhis2_id_mapping;");
   
    Universal rootUni = new Universal();
    rootUni.getDisplayLabel().setValue("ROOT");
    rootUni.setUniversalId("ROOT");
    rootUni.getDescription().setValue("ROOT");
    rootUni.apply();
   
    GeoEntity root = new GeoEntity();
    root.getDisplayLabel().setValue("ROOT");
    root.setGeoId("ROOT");
    root.setUniversal(rootUni);
    root.apply();
   
    BusinessDAO rootC = BusinessDAO.newInstance("net.geoprism.ontology.Classifier");
    rootC.setStructValue(Classifier.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "ROOT");
    rootC.setValue(Classifier.CLASSIFIERID, "ROOT");
    rootC.setValue(Classifier.CLASSIFIERPACKAGE, "ROOT");
    rootC.setValue(Classifier.KEYNAME, Term.ROOT_KEY);
    rootC.apply();
  }
  
  public OrgUnitLevelJsonToUniversal getUniversalByLevel(int level)
  {
    return universals[level];
  }
  
  private void buildAllpaths()
  {
    Universal.getStrategy().reinitialize(AllowedIn.CLASS);
    GeoEntity.getStrategy().reinitialize(LocatedIn.CLASS);
    Classifier.getStrategy().reinitialize(ClassifierIsARelationship.CLASS);
  }
  
  private void importOrgUnitLevels()
  {
    // curl -H "Accept: application/json" -u admin:district "http://localhost:8085/api/metadata.json?assumeTrue=false&organisationUnitLevels=true"
    HTTPResponse response = dhis2.httpGet("api/25/metadata", new NameValuePair[] {
        new NameValuePair("assumeTrue", "false"),
        new NameValuePair("organisationUnitLevels", "true")
    });
    DHIS2TrackerResponseProcessor.validateStatusCode(response); // TODO : We need better validation than just status code.
    
    JSONObject json = response.getJSON();
    
    // Create Universals from OrgUnitLevels
    JSONArray levels = json.getJSONArray("organisationUnitLevels");
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
    // curl -H "Accept: application/json" -u admin:district "http://localhost:8085/api/metadata.json?assumeTrue=false&organisationUnits=true"
    HTTPResponse response = dhis2.httpGet("api/25/metadata", new NameValuePair[] {
        new NameValuePair("assumeTrue", "false"),
        new NameValuePair("organisationUnits", "true")
    });
    DHIS2TrackerResponseProcessor.validateStatusCode(response); // TODO : We need better validation than just status code.
    
    JSONObject json = response.getJSON();
    
    // Create GeoEntities from OrgUnits
    JSONArray units = json.getJSONArray("organisationUnits");
    OrgUnitJsonToGeoEntity[] converters = new OrgUnitJsonToGeoEntity[units.length()];
    for (int i = 0; i < units.length(); ++i)
    {
      JSONObject unit = units.getJSONObject(i);
      
      OrgUnitJsonToGeoEntity converter = new OrgUnitJsonToGeoEntity(this, geometryFactory, geometryHelper, unit, this.countryOrgUnitId);
      converter.apply();
      
      converters[i] = converter;
    }
    
    if (OrgUnitJsonToGeoEntity.countryGeos.size() > 1)
    {
      throw new RuntimeException("Multiple country geo entities were detected. You need to figure out which ones of these is the real country level Org unit and add it as an argument to this program.\n" + StringUtils.join(OrgUnitJsonToGeoEntity.countryGeos, "\n"));
    }
    
    // Assign parents
    for (int i = 0; i < converters.length; ++i)
    {
      OrgUnitJsonToGeoEntity converter = converters[i];
      
      converter.applyLocatedIn();
    }
    
    
    
    // TODO : The geoId contains the DHIS2 id, and not the actual geoid. We do this because it has to be referenced when we export and there's no other place to save the DHIS2 id.
    
    // The OrgUnit parent reference is done by a DIHS2 id. In order to find the parent, we first set the GeoId to the DHIS2 internal id. Now we're swapping out those DHIS2 internal ids with geoids.
    // This could also be solved with a hashmap from   {DHIS2 id -> geoId}  to help us find the parent, but in the interest of saving memory I decided to loop through it again and hammer the processor instead.
//    for (int i = 0; i < converters.length; ++i)
//    {
//      OrgUnitJsonToGeoEntity converter = converters[i];
//      
//      converter.swapGeoId();
//    }
  }
  
  private void importOptionSets()
  {
    // curl -H "Accept: application/json" -u admin:district "http://localhost:8085/api/metadata.json?assumeTrue=false&categories=true"
    HTTPResponse response = dhis2.httpGet("api/25/metadata", new NameValuePair[] {
        new NameValuePair("assumeTrue", "false"),
        new NameValuePair("optionSets", "true")
    });
    DHIS2TrackerResponseProcessor.validateStatusCode(response); // TODO : We need better validation than just status code.
    
    JSONObject json = response.getJSON();
    
    // Create Classifiers from OptionSets
    JSONArray units = json.getJSONArray("optionSets");
    for (int i = 0; i < units.length(); ++i)
    {
      JSONObject unit = units.getJSONObject(i);
      
      OptionSetJsonToClassifier converter = new OptionSetJsonToClassifier(unit);
      converter.apply();
    }
  }
  
  // An optionSet contains a list of options. When we import the OptionSet relationships these will be set as a child to the optionSet.
  private void importOptions()
  {
    // curl -H "Accept: application/json" -u admin:district "http://localhost:8085/api/metadata.json?assumeTrue=false&options=true"
    HTTPResponse response = dhis2.httpGet("api/25/metadata", new NameValuePair[] {
        new NameValuePair("assumeTrue", "false"),
        new NameValuePair("options", "true")
    });
    DHIS2TrackerResponseProcessor.validateStatusCode(response); // TODO : We need better validation than just status code.
    
    JSONObject json = response.getJSON();
    
    // Create Classifiers from Options
    JSONArray units = json.getJSONArray("options");
    for (int i = 0; i < units.length(); ++i)
    {
      JSONObject unit = units.getJSONObject(i);
      
      OptionJsonToClassifier converter = new OptionJsonToClassifier(unit);
      converter.apply();
    }
  }
  
  private void importOptionSetRelationships()
  {
 // curl -H "Accept: application/json" -u admin:district "http://localhost:8085/api/metadata.json?assumeTrue=false&categories=true"
    HTTPResponse response = dhis2.httpGet("api/25/metadata", new NameValuePair[] {
        new NameValuePair("assumeTrue", "false"),
        new NameValuePair("optionSets", "true")
    });
    DHIS2TrackerResponseProcessor.validateStatusCode(response); // TODO : We need better validation than just status code.
    
    JSONObject json = response.getJSON();
    
    // Create Relationships
    JSONArray units = json.getJSONArray("optionSets");
    for (int i = 0; i < units.length(); ++i)
    {
      JSONObject unit = units.getJSONObject(i);
      
      OptionSetJsonToClassifier converter = new OptionSetJsonToClassifier(unit);
      converter.applyCategoryRelationships();
    }
  }
}

