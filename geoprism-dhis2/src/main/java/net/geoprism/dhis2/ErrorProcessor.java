package net.geoprism.dhis2;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorProcessor
{
  private static Logger logger = LoggerFactory.getLogger(ErrorProcessor.class);
  
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
      throw new RuntimeException("Unexpected response [" + response + "]");
    }
    
    JSONArray summaries = response.getJSONArray("importSummaries");
    
    for (int i = 0; i < summaries.length(); ++i)
    {
      JSONObject summary = summaries.getJSONObject(i);
      
      JSONArray conflicts = summary.getJSONArray("conflicts");
      
      for (int j = 0; j < conflicts.length(); ++j)
      {
        JSONObject conflict = conflicts.getJSONObject(j);
        String value = conflict.getString("value");
        
        if (value.contains("No org unit"))
        {
//          throw new RuntimeException("No org unit found on TEI. [" + response + "].");
        }
      }
    }
  }
  
  public static void validateTypeReportResponse(JSONObject response)
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
      throw new RuntimeException("Unexpected response [" + response + "]");
    }
    
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
              logger.error("Entity [" + klass + "] already exists.");
            }
            else
            {
              throw new RuntimeException("Unexpected response [" + response + "]");
            }
          }
        }
      }
    }
  }
}
