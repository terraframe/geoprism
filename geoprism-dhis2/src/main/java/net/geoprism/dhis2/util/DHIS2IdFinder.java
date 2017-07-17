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
