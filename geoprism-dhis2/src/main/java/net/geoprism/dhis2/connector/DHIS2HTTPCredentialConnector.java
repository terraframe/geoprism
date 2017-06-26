package net.geoprism.dhis2.connector;

import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONException;
import org.json.JSONObject;

public class DHIS2HTTPCredentialConnector extends AbstractDHIS2Connector
{
  synchronized public void initialize()
  {
    super.initialize();
    
    client.getParams().setAuthenticationPreemptive(true);
    Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
    client.getState().setCredentials(AuthScope.ANY, defaultcreds);
  }
  
  public JSONObject httpGet(String url, NameValuePair[] params)
  {
    if (!isInitialized())
    {
      initialize();
    }
    
    GetMethod get = new GetMethod(this.getServerUrl() + url);
    
    get.setRequestHeader("Accept", "application/json");
    
    get.setQueryString(params);
    
    JSONObject response = new JSONObject();
    int statusCode = this.httpRequest(this.client, get, response);
    
    if (statusCode != HttpStatus.SC_OK)
    {
      throw new RuntimeException("DHIS2 returned unexpected status code [" + statusCode + "].");
    }
    
    return response;
  }
  
  public JSONObject httpPost(String url, String body)
  {
    if (!isInitialized())
    {
      initialize();
    }
    
    try
    {
      PostMethod post = new PostMethod(this.serverurl + url);
      
      post.setRequestHeader("Content-Type", "application/json");
      
      post.setRequestEntity(new StringRequestEntity(body, null, null));

      JSONObject response = new JSONObject();
      int statusCode = this.httpRequest(this.client, post, response);

      if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_CREATED)
      {
        throw new RuntimeException("DHIS2 returned unexpected status code [" + statusCode + "].");
      }
      
      return response;
    }
    catch (JSONException | UnsupportedEncodingException e)
    {
      throw new RuntimeException(e);
    }
  }
}
