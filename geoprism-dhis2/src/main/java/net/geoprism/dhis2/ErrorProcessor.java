package net.geoprism.dhis2;

import org.json.JSONArray;
import org.json.JSONObject;

public class ErrorProcessor
{
  public static boolean isDuplicateGeoprismOauth(JSONObject response)
  {
    JSONObject innerResponse = response.getJSONObject("response");
    
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
