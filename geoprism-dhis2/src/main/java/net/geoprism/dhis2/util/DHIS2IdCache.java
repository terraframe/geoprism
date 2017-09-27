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
package net.geoprism.dhis2.util;

import java.util.Stack;

import org.apache.commons.httpclient.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import net.geoprism.dhis2.connector.AbstractDHIS2Connector;
import net.geoprism.dhis2.response.DHIS2TrackerResponseProcessor;
import net.geoprism.dhis2.response.DHIS2UnexpectedResponseException;
import net.geoprism.dhis2.response.HTTPResponse;

/**
 * This class is responsible for maintaining a cache of new DHIS2 ids that can be fetched at will.
 * 
 * @author rrowlands
 */
public class DHIS2IdCache
{
  // Number of ids to fetch and cache
  private static final String FETCH_NUM = "10";
  
  Stack<String> cache = new Stack<String>();
  
  AbstractDHIS2Connector dhis2;
  
  public DHIS2IdCache(AbstractDHIS2Connector dhis2)
  {
    this.dhis2 = dhis2;
  }
  
  /**
   * Fetches more ids from DHIS2 and adds them to our internal cache.
   */
  public void fetchIds()
  {
    HTTPResponse response = dhis2.httpGet("api/25/system/id.json", new NameValuePair[]{
        new NameValuePair("limit", FETCH_NUM)
    });
    DHIS2TrackerResponseProcessor.validateStatusCode(response); // TODO : We need better validation than just status code.
    
    JSONObject json = response.getJSON();
    
    if (json.has("codes"))
    {
      JSONArray codes = json.getJSONArray("codes");
      
      for (int i = 0; i < codes.length(); ++i)
      {
        String id = codes.getString(i);
        cache.push(id);
      }
    }
    else
    {
      DHIS2UnexpectedResponseException ex = new DHIS2UnexpectedResponseException();
      ex.setDhis2Response(response.toString().substring(0, 500)); // TODO : We need a better solution for this
      throw ex;
    }
  }
  
  /**
   * Fetches the next id from the cache. If the cache is empty, this method will fetch more ids and return one when it becomes available, haulting execution in the meantime.
   */
  public String next()
  {
    if (cache.isEmpty())
    {
      this.fetchIds();
    }
    
    return cache.pop();
  }
}
