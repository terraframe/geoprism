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

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.geoprism.dhis2.DHIS2Configuration;
import net.geoprism.dhis2.response.HTTPResponse;

abstract public class AbstractDHIS2Connector
{
  public static final String CLIENT_ID = "geoprism";
  
  public static final String SECRET = "1e6db50c-0fee-11e5-98d0-3c15c2c6caf6"; // TODO : Don't hardcode this
  
  HttpClient client;
  
  Logger logger = LoggerFactory.getLogger(DHIS2OAuthConnector.class);
  
  String serverurl;
  
  String username;
  
  String password;
  
  public void setCredentials(String username, String password)
  {
    this.username = username;
    this.password = password;
  }
  
  public String getServerUrl()
  {
    return serverurl;
  }
  
  public void setServerUrl(String url)
  {
    if (!url.endsWith("/"))
    {
      url = url + "/";
    }
    
    this.serverurl = url;
  }
  
  synchronized public void initialize()
  {
    this.client = new HttpClient();
  }
  
  public boolean isInitialized()
  {
    return client != null;
  }
  
  abstract public HTTPResponse httpGet(String url, NameValuePair[] params);
  
  abstract public HTTPResponse httpPost(String url, String body);
  
  public void readConfigFromDB()
  {
    DHIS2Configuration config = DHIS2Configuration.getByKey("DEFAULT");
    this.setServerUrl(config.getUrl());
    this.setCredentials(config.getUsername(), config.getPazzword());
  }
  
  public int httpRequest(HttpClient client, HttpMethod method, JSONObject response)
  {
    String sResponse = null;
    try
    {
      this.logger.info("Sending request to " + method.getURI());

      // Execute the method.
      int statusCode = client.executeMethod(method);
      
      // Follow Redirects
      if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_TEMPORARY_REDIRECT || statusCode == HttpStatus.SC_SEE_OTHER)
      {
        this.logger.info("Redirected [" + statusCode + "] to [" + method.getResponseHeader("location").getValue() + "].");
        method.setURI(new URI(method.getResponseHeader("location").getValue(), true, method.getParams().getUriCharset()));
        method.releaseConnection();
        return httpRequest(client, method, response);
      }
      
      // TODO : we might blow the memory stack here, read this as a stream somehow if possible.
      Header contentTypeHeader = method.getResponseHeader("Content-Type");
      if (contentTypeHeader == null)
      {
        sResponse = new String(method.getResponseBody(), "UTF-8");
      }
      else
      {
        sResponse = method.getResponseBodyAsString();
      }
      
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
