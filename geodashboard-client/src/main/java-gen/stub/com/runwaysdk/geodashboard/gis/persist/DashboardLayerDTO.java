package com.runwaysdk.geodashboard.gis.persist;

import org.json.JSONException;
import org.json.JSONObject;

public class DashboardLayerDTO extends DashboardLayerDTOBase
 implements com.runwaysdk.generation.loader.Reloadable{
  private static final long serialVersionUID = -410301370;
  
  public DashboardLayerDTO(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected DashboardLayerDTO(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  public JSONObject toJSON() {
    JSONObject json = new JSONObject();
    
    try {
      json.put("id", this.getId());
      json.put("name", this.getName());
    }
    catch (JSONException e) {
      throw new RuntimeException(e);
    }
    
    return json;
  }
  
}
