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
package net.geoprism.dhis2.response;

import org.json.JSONArray;
import org.json.JSONObject;

public class DHIS2ConflictException extends RuntimeException
{
  private static final long serialVersionUID = -4285516551413638133L;
  
  private JSONObject response;
  
  public DHIS2ConflictException(JSONObject response)
  {
    this.response = response;
  }
  
  public boolean isDuplicateGeoprismOauth()
  {
    JSONObject innerResponse = this.response.getJSONObject("response");
    
    JSONArray errorReports = innerResponse.getJSONArray("errorReports");
    
    for (int i = 0; i < errorReports.length(); ++i)
    {
      JSONObject errorReport = (JSONObject) errorReports.get(i);
      
      String errorCode = errorReport.getString("errorCode");
      String msg = errorReport.getString("message");
      
      if (errorCode.equals("E5003") && msg.contains("Property `cid`") && msg.contains("with value `geoprism`"))
      {
        return true;
      }
    }
    
    return false;
  }
}
