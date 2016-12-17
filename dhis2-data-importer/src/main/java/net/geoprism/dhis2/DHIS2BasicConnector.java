package net.geoprism.dhis2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConstants;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DHIS2BasicConnector
{
  private Logger logger = LoggerFactory.getLogger(DHIS2BasicConnector.class);
  
  private HttpClient client;
  
  private String serverurl;
  
  private String username;
  
  private String password;
  
  public static final String CLIENT_ID = "geoprism";
  
  public static final String SECRET = "1e6db50c-0fee-11e5-98d0-3c15c2c6caf6";
  
  private String accessToken;
  
  private String refreshToken;
  
  public DHIS2BasicConnector(String serverurl, String username, String password)
  {
    this.serverurl = serverurl;
    this.username = username;
    this.password = password;
  }
  
  public String getAccessToken()
  {
    return accessToken;
  }
  
  public String getServerUrl()
  {
    return serverurl;
  }
  
  /**
   * Uses the DHIS2 REST API to register a new OAuth configuration for Geoprism
   */
  // curl -X POST -H "Content-Type: application/json" -d '{ "name" : "OAuth2 Demo Client", "cid" : "demo", "secret" : "1e6db50c-0fee-11e5-98d0-3c15c2c6caf6", "grantTypes" : [ "password", "refresh_token", "authorization_code" ], "redirectUris" : [ "http://www.example.org" ]}' -u admin:district 
  public void createOauthClient()
  {
    this.client = new HttpClient();
    
    client.getParams().setAuthenticationPreemptive(true);
    Credentials defaultcreds = new UsernamePasswordCredentials(this.username, this.password);
    client.getState().setCredentials(AuthScope.ANY, defaultcreds);
    
    try
    {
      PostMethod post = new PostMethod(this.serverurl + "api/oAuth2Clients");
      
      post.setRequestHeader("Content-Type", "application/json");
      
      String body = "{ \"name\" : \"Geoprism\", \"cid\" : \"" + CLIENT_ID + "\", \"secret\" : \"" + SECRET + "\", \"grantTypes\" : [ \"password\", \"refresh_token\", \"authorization_code\" ], \"redirectUris\" : [ \"\" ]}";
      post.setRequestEntity(new StringRequestEntity(body, null, null));

      JSONObject response = new JSONObject();
      int statusCode = httpRequest(post, response);

      if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_CREATED)
      {
        
      }
      else if (statusCode == HttpStatus.SC_CONFLICT)
      {
        throw new DHIS2ConflictException(response);
      }
      else
      {
        if (response.has("message"))
        {
          String message = response.getString("message");

          throw new RuntimeException(message);
        }

        String message = "Unable to create OAuth client.  Ensure that your login credentials are correct.";
        throw new RuntimeException(message);
      }
    }
    catch (JSONException | UnsupportedEncodingException e)
    {
      throw new RuntimeException(e);
    }
    
    this.client = new HttpClient();
  }
  
  // curl -X POST -H "Accept: application/json" -u demo:$SECRET $SERVER/uaa/oauth/token -d grant_type=password -d username=admin -d password=district
  public void logIn()
  {
    this.client = new HttpClient();
    
    client.getParams().setAuthenticationPreemptive(true);
    Credentials defaultcreds = new UsernamePasswordCredentials("geoprism", SECRET);
    client.getState().setCredentials(AuthScope.ANY, defaultcreds);

    try
    {
      PostMethod post = new PostMethod(this.serverurl + "uaa/oauth/token");
      post.setRequestHeader("Accept", "application/json");
      post.addParameter("grant_type", "password");
      post.addParameter("username", username);
      post.addParameter("password", password);
      post.addParameter("format", "json");

      JSONObject json = new JSONObject();
      int statusCode = httpRequest(post, json);

      if (statusCode == HttpStatus.SC_OK)
      {
        this.accessToken = json.getString("access_token");
        this.refreshToken = json.getString("refresh_token");
      }
      else
      {
        if (json.has("message"))
        {
          String message = json.getString("message");

          throw new RuntimeException(message);
        }

        String message = "Unable to log into sales force.  Ensure that salesforce.properties is up to date.";
        throw new RuntimeException(message);
      }
    }
    catch (JSONException e)
    {
      throw new RuntimeException(e);
    }
    
    this.client = new HttpClient();
  }
  
  public JSONObject httpGetRequest(String url, NameValuePair[] params)
  {
    GetMethod get = new GetMethod(this.getServerUrl() + url);
    get.setRequestHeader("Authorization", "Bearer " + this.getAccessToken());
    get.setRequestHeader("Accept", "application/json");
    
    get.setQueryString(params);
    
    JSONObject response = new JSONObject();
    int statusCode = this.httpRequest(get, response);
    
    if (statusCode != HttpStatus.SC_OK)
    {
      throw new RuntimeException("DHIS2 returned unexpected status code [" + statusCode + "].");
    }
    
    return response;
  }
  
  public int httpRequest(HttpMethod method, JSONObject response)
  {
    String sResponse = null;
    try
    {
      this.logger.info("Sending request to " + method.getURI());

      // Execute the method.
      int statusCode = this.client.executeMethod(method);
      
      // Follow Redirects
      if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_TEMPORARY_REDIRECT || statusCode == HttpStatus.SC_SEE_OTHER)
      {
        this.logger.info("Redirected [" + statusCode + "] to [" + method.getResponseHeader("location").getValue() + "].");
        method.setURI(new URI(method.getResponseHeader("location").getValue(), true, method.getParams().getUriCharset()));
        method.releaseConnection();
        return httpRequest(method, response);
      }

      // TODO : we might blow the memory stack here, read this as a stream somehow if possible.
      sResponse = method.getResponseBodyAsString();
      if (sResponse.length() < 1000)
      {
        this.logger.info("Response string = '" + sResponse + "'.");
      }
      else
      {
        this.logger.info("Receieved a very large response.");
      }

      JSONObject jsonResp;
      if (sResponse.startsWith("["))
      {
        jsonResp = new JSONArray(sResponse).getJSONObject(0);
      }
      else if (sResponse.startsWith("{"))
      {
        jsonResp = new JSONObject(sResponse);
      }
      else
      {
        jsonResp = new JSONObject();
        jsonResp.put("errorCode", statusCode);
        jsonResp.put("message", sResponse);
      }

      @SuppressWarnings("unchecked")
      Iterator<String> it = jsonResp.keys();
      while (it.hasNext())
      {
        String key = it.next();
        response.put(key, jsonResp.get(key));
      }

      return statusCode;
    }
    catch (HttpException e)
    {
      throw new RuntimeException(e);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    catch (JSONException e)
    {
      throw new RuntimeException(e);
    }
    finally
    {
      method.releaseConnection();
    }
  }
}
