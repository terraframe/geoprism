/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.dhis2.util;

import org.apache.commons.httpclient.NameValuePair;
import org.json.JSONObject;

import net.geoprism.dhis2.connector.DHIS2HTTPCredentialConnector;
import net.geoprism.dhis2.response.DHIS2TrackerResponseProcessor;
import net.geoprism.dhis2.response.HTTPResponse;

public class DHIS2IdFinder
{
  public static String findAttributes()
  {
    DHIS2HTTPCredentialConnector dhis2 = new DHIS2HTTPCredentialConnector();
    dhis2.readConfigFromDB();
    
    HTTPResponse response = dhis2.httpGet("api/25/metadata", new NameValuePair[] {
      new NameValuePair("assumeTrue", "false"),
      new NameValuePair("trackedEntityAttributes", "true")
    });
    DHIS2TrackerResponseProcessor.validateStatusCode(response); // TODO : We need better validation than just status code.
    
    JSONObject json = response.getJSON();
    
    if (json != null && json.has("trackedEntityAttributes"))
    {
      return json.getJSONArray("trackedEntityAttributes").toString();
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
    
    HTTPResponse response = dhis2.httpGet("api/25/metadata", new NameValuePair[] {
      new NameValuePair("assumeTrue", "false"),
      new NameValuePair("programs", "true")
    });
    DHIS2TrackerResponseProcessor.validateStatusCode(response); // TODO : We need better validation than just status code.
    
    JSONObject json = response.getJSON();
    
    if (json != null && json.has("programs"))
    {
      return json.getJSONArray("programs").toString();
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
    
    HTTPResponse response = dhis2.httpGet("api/25/metadata", new NameValuePair[] {
      new NameValuePair("assumeTrue", "false"),
      new NameValuePair("trackedEntities", "true")
    });
    
    JSONObject json = response.getJSON();
    
    if (json != null && json.has("trackedEntities"))
    {
      return json.getJSONArray("trackedEntities").toString();
    }
    else
    {
      return "[]";
    }
  }
}
