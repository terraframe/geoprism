package net.geoprism.dhis2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.mvc.Controller;
import com.runwaysdk.mvc.Endpoint;
import com.runwaysdk.mvc.ErrorSerialization;
import com.runwaysdk.mvc.ResponseIF;
import com.runwaysdk.mvc.RestBodyResponse;

@Controller(url = "dhis2")
public class DHIS2Controller implements Reloadable
{ 
  @Endpoint(method = ServletMethod.GET, error = ErrorSerialization.JSON)
  public ResponseIF getTrackedEntities(ClientRequestIF request) throws java.io.IOException, javax.servlet.ServletException, JSONException
  {
    String json = DHIS2IdMappingDTO.findTrackedEntityIds(request);
    
    return new RestBodyResponse(new JSONArray(json));
  }
  
  @Endpoint(method = ServletMethod.GET, error = ErrorSerialization.JSON)
  public ResponseIF getPrograms(ClientRequestIF request) throws java.io.IOException, javax.servlet.ServletException, JSONException
  {
    String json = DHIS2IdMappingDTO.findPrograms(request);
    
    return new RestBodyResponse(new JSONArray(json));
  }
  
  @Endpoint(method = ServletMethod.GET, error = ErrorSerialization.JSON)
  public ResponseIF getTrackedEntityAttributes(ClientRequestIF request) throws java.io.IOException, javax.servlet.ServletException, JSONException
  {
    String json = DHIS2IdMappingDTO.findAttributes(request);
    
    return new RestBodyResponse(new JSONArray(json));
  }
}
