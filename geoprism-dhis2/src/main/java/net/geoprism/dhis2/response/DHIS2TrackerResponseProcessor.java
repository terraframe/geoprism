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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.Pair;

public class DHIS2TrackerResponseProcessor
{
  private static Logger logger = LoggerFactory.getLogger(DHIS2TrackerResponseProcessor.class);
  
  public static void validateStatusCode(HTTPResponse response)
  {
    int statusCode = response.getStatusCode();
    
    if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_CREATED)
    {
      throw new RuntimeException("Invalid status code [" + statusCode + "].");
    }
  }
  
  // This code works on the 25 api version of DHIS2 2.27 (what a mess)
  public static void validateImportSummaryResponse(HTTPResponse response)
  {
//    {
//      "response": {
//        "ignored": 0,
//        "importOptions": {
//          "strictAttributeOptionCombos": false,TTPREs
//          "dryRun": false,
//          "skipExistingCheck": false,
//          "datasetAllowsPeriods": false,
//          "skipNotifications": false,
//          "mergeMode": "REPLACE",
//          "sharing": false,
//          "requireAttributeOptionCombo": false,
//          "strictCategoryOptionCombos": false,
//          "async": false,
//          "requireCategoryOptionCombo": false,
//          "idSchemes": {},
//          "importStrategy": "CREATE_AND_UPDATE",
//          "strictOrganisationUnits": false,
//          "strictPeriods": false
//        },
//        "responseType": "ImportSummaries",
//        "deleted": 0,
//        "imported": 0,
//        "updated": 0,
//        "status": "SUCCESS"
//      },
//      "httpStatus": "OK",
//      "message": "Import was successful.",
//      "httpStatusCode": 200,
//      "status": "OK"
//    }
    
    JSONObject json = response.getJSON();
    int statusCode = response.getStatusCode();
    
    if (json.has("response"))
    {
      validateImportSummaryResponse(new HTTPResponse(json.getJSONObject("response"), statusCode));
      return;
    }
    
    if (!json.has("responseType") || !json.getString("responseType").equals("ImportSummaries"))
    {
      DHIS2UnexpectedResponseException ex = new DHIS2UnexpectedResponseException();
      if (json.has("message"))
      {
        ex.setDhis2Response(json.getString("message").substring(0,1000));
      }
      else if (json.has("responseType"))
      {
        ex.setDhis2Response(json.getString("responseType").substring(0,1000));
      }
      throw ex;
    }
    
    if (json.has("importSummaries"))
    {
      JSONArray summaries = json.getJSONArray("importSummaries");
    
      for (int i = 0; i < summaries.length(); ++i)
      {
        JSONObject summary = summaries.getJSONObject(i);
        
        if (summary.has("conflicts"))
        {
          JSONArray conflicts = summary.getJSONArray("conflicts");
          
          for (int j = 0; j < conflicts.length(); ++j)
          {
            JSONObject conflict = conflicts.getJSONObject(j);
            String value = conflict.getString("value");
            String object = conflict.getString("object");
            
            DHIS2UnexpectedResponseException ex = new DHIS2UnexpectedResponseException();
            ex.setDhis2Response(value + ". [" + object + "]");
            throw ex;
            
//                if (value.contains("No org unit"))
//                {
//                  DHIS2UnexpectedResponseException ex = new DHIS2UnexpectedResponseException();
//                  ex.setDhis2Response(value);
//                  throw ex;
//                }
//                else if (object.equals("Attribute.value") && value.contains("is not a valid option for attribute") && value.contains("option set"))
//                {
//                  DHIS2UnexpectedResponseException ex = new DHIS2UnexpectedResponseException();
//                  ex.setDhis2Response("The specified value does not match the expected "); // TODO : Localize
//                  throw ex;
//                }
          }
        }
        
        if (summary.has("enrollments"))
        {
          JSONObject enrollments = summary.getJSONObject("enrollments");
          
          validateImportSummaryResponse(new HTTPResponse(enrollments, statusCode));
        }
        
        String status = summary.getString("status");
        if (!status.equals("SUCCESS"))
        {
          DHIS2UnexpectedResponseException ex = new DHIS2UnexpectedResponseException();
          ex.setDhis2Response(summary.toString().substring(0, 1000)); // TODO : We need to be very careful about putting the entire response in here because the response could be very large. Also its raw JSON.
          throw ex;
        }
      }
    }
    
    int modified = 0;
    if (json.has("deleted"))
    {
      modified = modified + json.getInt("deleted");
    }
    if (json.has("imported"))
    {
      modified = modified + json.getInt("imported");
    }
    if (json.has("updated"))
    {
      modified = modified + json.getInt("updated");
    }
//    if (json.has("ignored")) // TODO : Should we throw an error when they ignore us?
//    {
//      modified = modified + json.getInt("updated");
//    }
    
    if (modified == 0)
    {
      DHIS2UnexpectedResponseException ex = new DHIS2UnexpectedResponseException();
      ex.setDhis2Response("No objects were imported"); // TODO : Localize
      throw ex;
    }
    
    validateStatusCode(response);
  }
  
  public static void validateTypeReportResponse(HTTPResponse response, boolean errorOnAlreadyExists)
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
    
    JSONObject json = response.getJSON();
    int statusCode = response.getStatusCode();
    
    if (!json.has("status"))
    {
      DHIS2UnexpectedResponseException ex = new DHIS2UnexpectedResponseException();
      ex.setDhis2Response(json.toString().substring(0, 1000)); // TODO : We need to be very careful about putting the entire response in here because the response could be very large. Also its raw JSON.
      throw ex;
    }
    
    List<String> attrErrs = new ArrayList<String>();
    
    if (json.getString("status").equals("ERROR"))
    {
      JSONArray typeReports = json.getJSONArray("typeReports");
      
      for (int i = 0; i < typeReports.length(); ++i)
      {
        JSONObject typeReport = typeReports.getJSONObject(i);
        
        String klass = typeReport.getString("klass");
        
        if (typeReport.has("objectReports")) // It may not have an objectReport if this particular typeReport completed successfully.
        {
          JSONArray objectReports = typeReport.getJSONArray("objectReports");
          
          for (int j = 0; j < objectReports.length(); ++j)
          {
            JSONObject objectReport = objectReports.getJSONObject(j);
            
            JSONArray errorReports = objectReport.getJSONArray("errorReports");
            
            for (int k = 0; k < errorReports.length(); ++k)
            {
              JSONObject errorReport = errorReports.getJSONObject(k);
              
              String errorCode = errorReport.getString("errorCode");
              String message = errorReport.getString("message");
              String mainKlass = errorReport.getString("mainKlass");
  
              if (errorCode.equals("E5003")) // Duplicate data ex
              {
                if (mainKlass.equals("org.hisp.dhis.trackedentity.TrackedEntityAttribute"))
                {
                  attrErrs.add(message);
                }
                else
                {
                  Pair<String, String> pair = parseDuplicateDataMsg(message);
                  
                  DHIS2DuplicateDataException ex = new DHIS2DuplicateDataException();
                  ex.setDataType(mainKlass);
                  ex.setPropertyName(pair.getFirst());
                  ex.setPropertyValue(pair.getSecond());
                  throw ex;
                }
              }
              else if (errorCode.equals("E4001"))
              {
                String attrLen;
                Pattern p = Pattern.compile("but given length was ([0-9]*)\\.");
                Matcher m = p.matcher(message);
                if (m.find())
                {
                  attrLen = m.group(1);
                }
                else
                {
                  // If we get some weird message back that doesn't match our regex
                  DHIS2UnexpectedResponseException ex = new DHIS2UnexpectedResponseException();
                  ex.setDhis2Response(message.substring(0, 1000));
                  throw ex;
                }
                
                
                DHIS2AttributeLengthException ex = new DHIS2AttributeLengthException();
                ex.setAttrLen(attrLen);
                throw ex;
              }
              else
              {
                DHIS2UnexpectedResponseException ex = new DHIS2UnexpectedResponseException();
                ex.setDhis2Response(message.substring(0, 1000));
                throw ex;
              }
            }
          }
        }
      }
    }
    
    if (errorOnAlreadyExists && attrErrs.size() > 0)
    {
      Set<String> attrNames = new HashSet<String>();
      for (String msg : attrErrs)
      {
        Pair<String, String> pair = parseDuplicateDataMsg(msg);
        
        String attrName = pair.getSecond();
        
        if (attrName != null)
        {
          attrNames.add(attrName);
        }
      }
      
      DHIS2DuplicateAttributeException ex = new DHIS2DuplicateAttributeException();
      ex.setDhis2Attrs(StringUtils.join(attrNames, ", "));
      throw ex;
    }
    
    validateStatusCode(response);
  }
  
  /**
   * Parses the DHIS2 duplicate data message and returns a pair where the property name is first and the property value is second.
   */
  public static Pair<String, String> parseDuplicateDataMsg(String msg)
  {
    // Property `name` with value `AGYW’s surname` on object AGYW’s surname [imcsp063qb8] (OptionSet) already exists on object irje6etwqpy.
    // A [option set] already exists with name [AGYW’s surname]
    
    // This pattern contains non breaking space so just be aware that you can have it not match because sometimes what looks like a space is not actually a space
    Pattern p = Pattern.compile("Property `(.*?)` with value `(.*?)` on object (.*?) already exists on object (.*?)\\.");
    Matcher m = p.matcher(msg);
    
    if (m.find())
    {
      String propertyName = m.group(1);
      String propertyValue = m.group(2);
      
      return new Pair<String, String>(propertyName, propertyValue);
    }
    else
    {
      // If we get some weird message back that doesn't match our regex
      DHIS2UnexpectedResponseException ex = new DHIS2UnexpectedResponseException();
      ex.setDhis2Response(msg.substring(0, 1000));
      throw ex;
    }
  }
}
