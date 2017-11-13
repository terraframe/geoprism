package net.geoprism;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.mvc.Controller;
import com.runwaysdk.mvc.Endpoint;
import com.runwaysdk.mvc.ErrorSerialization;
import com.runwaysdk.mvc.ResponseIF;
import com.runwaysdk.mvc.RestResponse;
import com.runwaysdk.system.gis.geo.AllowedInDTO;
import com.runwaysdk.system.gis.geo.GeoEntityDTO;
import com.runwaysdk.system.gis.geo.IsARelationshipDTO;
import com.runwaysdk.system.gis.geo.LocatedInDTO;
import com.runwaysdk.system.gis.geo.UniversalDTO;
import com.runwaysdk.web.json.JSONController;

import net.geoprism.data.browser.DataBrowserUtilDTO;
import net.geoprism.ontology.ClassifierDTO;
import net.geoprism.ontology.ClassifierIsARelationshipDTO;

@Controller(url = "tree")
public class TreeController
{
  @Endpoint(error = ErrorSerialization.JSON)
  public ResponseIF geoentity(ClientRequestIF request)
  {
    GeoEntityDTO root = GeoEntityDTO.getRoot(request);

    JSONArray relationships = new JSONArray();
    relationships.put(LocatedInDTO.CLASS);

    RestResponse response = new RestResponse();
    response.set("type", GeoEntityDTO.CLASS);
    response.set("rootId", root.getId());
    response.set("relationships", relationships);

    return response;
  }

  @Endpoint(error = ErrorSerialization.JSON)
  public ResponseIF universal(ClientRequestIF request)
  {
    UniversalDTO root = UniversalDTO.getRoot(request);

    JSONArray relationships = new JSONArray();
    relationships.put(AllowedInDTO.CLASS);
    relationships.put(IsARelationshipDTO.CLASS);

    RestResponse response = new RestResponse();
    response.set("type", UniversalDTO.CLASS);
    response.set("rootId", root.getId());
    response.set("relationships", relationships);

    return response;
  }

  @Endpoint(error = ErrorSerialization.JSON)
  public ResponseIF classifier(ClientRequestIF request)
  {
    ClassifierDTO root = ClassifierDTO.getRoot(request);

    JSONArray relationships = new JSONArray();
    relationships.put(ClassifierIsARelationshipDTO.CLASS);

    RestResponse response = new RestResponse();
    response.set("type", ClassifierDTO.CLASS);
    response.set("rootId", root.getId());
    response.set("relationships", relationships);

    return response;
  }

  @Endpoint(error = ErrorSerialization.JSON)
  public ResponseIF browser(ClientRequestIF request) throws JSONException
  {
    String sessionId = request.getSessionId();
    String metadata = "{className:'net.geoprism.data.browser.DataBrowserUtil', methodName:'getDefaultTypes', declaredTypes: []}";
    String types = JSONController.invokeMethod(sessionId, metadata, null, "[]");

    RestResponse response = new RestResponse();
    response.set("types", new JSONObject(types));
    response.set("editData", GeoprismUserDTO.hasAccess(request, AccessConstants.EDIT_DATA));

    return response;
  }
}
