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
package net.geoprism.dhis2.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DHIS2ResponseProcessor
{
  private static Logger logger = LoggerFactory.getLogger(DHIS2ResponseProcessor.class);
  
  public static void validateImportSummaryResponse(JSONObject response)
  {
//    {
//      "ignored": 0,
//      "responseType": "ImportSummaries",
//      "deleted": 0,
//      "imported": 0,
//      "importSummaries": [
//        {
//          "responseType": "ImportSummary",
//          "importCount": {
//            "ignored": 0,
//            "deleted": 0,
//            "imported": 0,
//            "updated": 0
//          },
//          "conflicts": [
//            {
//              "value": "No org unit ID in tracked entity instance object."
//            }
//          ],
//          "status": "SUCCESS"
//        },
//    }
    
    if (!response.has("responseType") || !response.getString("responseType").equals("ImportSummaries"))
    {
      throw new DHIS2UnexpectedResponseException("Unexpected response [" + response + "]");
    }
    
    JSONArray summaries = response.getJSONArray("importSummaries");
    
    for (int i = 0; i < summaries.length(); ++i)
    {
      JSONObject summary = summaries.getJSONObject(i);
      
      String status = summary.getString("status");
      if (!status.equals("SUCCESS"))
      {
        throw new DHIS2UnexpectedResponseException("Unexpected response [" + response + "]");
      }
      
      if (summary.has("conflicts"))
      {
        JSONArray conflicts = summary.getJSONArray("conflicts");
        
        for (int j = 0; j < conflicts.length(); ++j)
        {
          JSONObject conflict = conflicts.getJSONObject(j);
          String value = conflict.getString("value");
          
          if (value.contains("No org unit"))
          {
            throw new RuntimeException("No org unit found on TEI. [" + response + "].");
          }
        }
      }
      
      if (summary.has("enrollments"))
      {
        JSONObject enrollments = summary.getJSONObject("enrollments");
        
        validateImportSummaryResponse(enrollments);
      }
    }
  }
  
  public static void validateTypeReportResponse(JSONObject response, boolean errorOnAlreadyExists)
  {
//  {
//  "status": "ERROR",
//  "typeReports": [
//    {
//      "klass": "org.hisp.dhis.trackedentity.TrackedEntity",
//      "stats": {
//        "total": 1,
//        "created": 0,
//        "updated": 0,
//        "deleted": 0,
//        "ignored": 1
//      },
//      "objectReports": [
//        {
//          "klass": "org.hisp.dhis.trackedentity.TrackedEntity",
//          "index": 0,
//          "errorReports": [
//            {
//              "message": "Property `name`Â with value `Labor Force By State 2015` on object Labor Force By State 2015 [Lg3bG0HelP0] (TrackedEntity)Â already exists on object Yw2zJnEX0o4.",
//              "mainKlass": "org.hisp.dhis.trackedentity.TrackedEntity",
//              "errorCode": "E5003"
//            }
//          ]
//        }
//      ]
//    }
//  ],
//  "stats": {
//    "total": 1,
//    "created": 0,
//    "updated": 0,
//    "deleted": 0,
//    "ignored": 1
//  }
//}
    if (!response.has("status"))
    {
      throw new DHIS2UnexpectedResponseException("Unexpected response [" + response + "]");
    }
    
    List<String> failMsgs = new ArrayList<String>();
    
    if (response.getString("status").equals("ERROR"))
    {
      JSONArray typeReports = response.getJSONArray("typeReports");
      
      for (int i = 0; i < typeReports.length(); ++i)
      {
        JSONObject typeReport = typeReports.getJSONObject(i);
        
        String klass = typeReport.getString("klass");
        
        JSONArray objectReports = typeReport.getJSONArray("objectReports");
        
        for (int j = 0; j < objectReports.length(); ++j)
        {
          JSONObject objectReport = objectReports.getJSONObject(j);
          
          JSONArray errorReports = objectReport.getJSONArray("errorReports");
          
          for (int k = 0; k < errorReports.length(); ++k)
          {
            JSONObject errorReport = errorReports.getJSONObject(k);
            
            if (errorReport.getString("errorCode").equals("E5003"))
            {
              if (errorOnAlreadyExists)
              {
                failMsgs.add(errorReport.getString("message"));
              }
              else
              {
                logger.error(errorReport.getString("message"));
              }
            }
            else
            {
              throw new DHIS2UnexpectedResponseException("Unexpected response [" + response + "]");
            }
          }
        }
      }
    }
    
    if (errorOnAlreadyExists && failMsgs.size() > 0)
    {
      throw new DHIS2DuplicateDataException(failMsgs);
    }
  }
}
