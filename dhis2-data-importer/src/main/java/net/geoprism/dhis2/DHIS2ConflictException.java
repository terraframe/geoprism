package net.geoprism.dhis2;

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
