package net.geoprism.dhis2.util;

import org.apache.commons.httpclient.NameValuePair;
import org.json.JSONObject;

import net.geoprism.dhis2.connector.DHIS2HTTPCredentialConnector;

public class DHIS2IdFinder
{
  public static String findAttributes()
  {
    DHIS2HTTPCredentialConnector dhis2 = new DHIS2HTTPCredentialConnector();
    dhis2.readConfigFromDB();
    
    JSONObject response = dhis2.httpGet("api/25/metadata", new NameValuePair[] {
      new NameValuePair("assumeTrue", "false"),
      new NameValuePair("trackedEntityAttributes", "true")
    });
    
    if (response != null && response.has("trackedEntityAttributes"))
    {
      return response.getJSONArray("trackedEntityAttributes").toString();
    }
    else
    {
      return "[]";
    }
  }

  public static String findPrograms()
  {
    DHIS2HTTPCredentialConnector dhis2 = new DHIS2HTTPCredentialConnector();
    dhis2.readConfigFromDB();
    
    JSONObject response = dhis2.httpGet("api/25/metadata", new NameValuePair[] {
      new NameValuePair("assumeTrue", "false"),
      new NameValuePair("programs", "true")
    });
    
    if (response != null && response.has("programs"))
    {
      return response.getJSONArray("programs").toString();
    }
    else
    {
      return "[]";
    }
  }

  public static String findTrackedEntities()
  {
    DHIS2HTTPCredentialConnector dhis2 = new DHIS2HTTPCredentialConnector();
    dhis2.readConfigFromDB();
    
    JSONObject response = dhis2.httpGet("api/25/metadata", new NameValuePair[] {
      new NameValuePair("assumeTrue", "false"),
      new NameValuePair("trackedEntities", "true")
    });
    
    if (response != null && response.has("trackedEntities"))
    {
      return response.getJSONArray("trackedEntities").toString();
    }
    else
    {
      return "[]";
    }
  }
}
