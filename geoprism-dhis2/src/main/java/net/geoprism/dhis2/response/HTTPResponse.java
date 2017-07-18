package net.geoprism.dhis2.response;

import org.json.JSONObject;

public class HTTPResponse
{
  private JSONObject response;
  
  private int statusCode;
  
  public HTTPResponse(JSONObject response, int statusCode)
  {
    this.response = response;
    this.statusCode = statusCode;
  }

  public JSONObject getJSON() {
    return response;
  }

  public void setResponse(JSONObject response) {
    this.response = response;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }
}
