package net.geoprism.dhis2;

import java.io.File;

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
import com.runwaysdk.session.Request;
import com.runwaysdk.system.gis.geo.Universal;

import net.geoprism.configuration.GeoprismConfigurationResolver;

/**
 * This class is the main entrypoint for all DHIS2 data importing. Run the main method in this class to kick off a data import.
 * 
 * @author rick
 *
 */
public class DHIS2DataImporter
{
  private DHIS2BasicConnector dhis2;
  
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
    dhis2 = new DHIS2BasicConnector(url, username, password);
    
    try
    {
      dhis2.createOauthClient();
    }
    catch (DHIS2ConflictException e)
    {
      // If it threw an error because the oauth client already exists, ignore it.
      if (!e.isDuplicateGeoprismOauth())
      {
        throw e;
      }
    }
    
    dhis2.logIn();
  }

  @Request
  private void importAll()
  {
    importOrgUnitLevels();
  }
  
  private void importOrgUnitLevels() {
    // curl -H "Accept: application/json" -u admin:district "http://localhost:8085/api/metadata?assumeTrue=false&organisationUnitLevels=true"
    JSONObject response = dhis2.httpGetRequest("api/25/metadata", new NameValuePair[] {
        new NameValuePair("assumeTrue", "false"),
        new NameValuePair("organisationUnitLevels", "true")
    });
    
    // Create Universals from OrgUnits
    JSONArray levels = response.getJSONArray("organisationUnitLevels");
    Universal[] universals = new Universal[levels.length()];
    for (int i = 0; i < levels.length(); ++i)
    {
      JSONObject level = levels.getJSONObject(i);
      
      Universal universal = new Universal();
      universal.getDisplayLabel().setValue(level.getString("name"));
      universal.setUniversalId(level.getString("id"));
      universal.apply();
      universals[level.getInt("level")-1] = universal;
    }
    
    // Apply Universal relationships
    universals[0].addAllowedIn(Universal.getRoot()).apply();
    for (int i = 1; i < universals.length; ++i)
    {
      universals[i].addAllowedIn(universals[i-1]).apply();
    }
  }
}
