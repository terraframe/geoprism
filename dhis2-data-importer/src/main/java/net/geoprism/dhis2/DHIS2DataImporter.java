package net.geoprism.dhis2;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONObject;

/**
 * This class is the main entrypoint for all DHIS2 data importing. Run the main method in this class to kick off a data import.
 * 
 * @author rick
 *
 */
public class DHIS2DataImporter
{
  public static void main(String[] args)
  {
    CommandLineParser parser = new DefaultParser();
    Options options = new Options();
    options.addOption(Option.builder("l").hasArg().argName("url").longOpt("url").desc("URL of the DHIS2 server to connect to, including the port. Defaults to: http://127.0.0.1:8085/").build());
    options.addOption(Option.builder("u").hasArg().argName("username").longOpt("username").desc("The username of the root (admin) DHIS2 user.").build());
    options.addOption(Option.builder("p").hasArg().argName("password").longOpt("password").desc("The password for the root (admin) DHIS2 user.").build());

    try {
      CommandLine line = parser.parse( options, args );
      
      String url = line.getOptionValue("l");
      String username = line.getOptionValue("u");
      String password = line.getOptionValue("p");
      
      if (url == null)
      {
        url = "http://127.0.0.1:8085/";
      }
      if (username == null)
      {
        throw new RuntimeException("Username is required.");
      }
      if (password == null)
      {
        throw new RuntimeException("Password is required.");
      }
      
      doIt(url, username, password);
    }
    catch (ParseException e)
    {
      throw new RuntimeException(e);
    }
  }

  private static void doIt(String url, String username, String password) {
    DHIS2BasicConnector dhis2 = new DHIS2BasicConnector(url, username, password);
    dhis2.createOauthClient();
    dhis2.logIn();
    
    // curl -H "Accept: application/xml" -u admin:district "http://localhost:8085/api/metadata?assumeTrue=false&organisationUnits=true&lastUpdated=2014-08-01"
    GetMethod get = new GetMethod(dhis2.getServerUrl() + "api/25/metadata");
    get.setRequestHeader("Authorization", "Bearer " + dhis2.getAccessToken());
    get.setRequestHeader("Accept", "application/json");
    NameValuePair[] params = new NameValuePair[] {
        new NameValuePair("assumeTrue", "false"),
        new NameValuePair("organisationUnits", "true"),
        new NameValuePair("lastUpdated", "2014-08-01"),
    };
    get.setQueryString(params);
    
    JSONObject response = new JSONObject();
    int statusCode = dhis2.httpRequest(get, response);
    
    System.out.println(response);
  }
}
