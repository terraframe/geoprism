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

import net.geoprism.dhis2.response.HTTPResponse;

public class DHIS2HTTPCredentialConnector extends AbstractDHIS2Connector
{
  synchronized public void initialize()
  {
    super.initialize();
    
    client.getParams().setAuthenticationPreemptive(true);
    Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
    client.getState().setCredentials(AuthScope.ANY, defaultcreds);
  }
  
  public HTTPResponse httpGet(String url, NameValuePair[] params)
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
    
    return new HTTPResponse(response, statusCode);
  }
  
  public HTTPResponse httpPost(String url, String body)
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

      return new HTTPResponse(response, statusCode);
    }
    catch (JSONException | UnsupportedEncodingException e)
    {
      throw new RuntimeException(e);
    }
  }
}
