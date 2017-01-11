package net.geoprism.dhis2.exporter;

import org.json.JSONObject;

import com.runwaysdk.system.metadata.MdBusiness;

public class MdBusinessToTrackerJson
{
  MdBusiness mdbiz;
  
  public MdBusinessToTrackerJson(MdBusiness mdbiz)
  {
    this.mdbiz = mdbiz;
  }
  
  /**
   * Creates the JSON representation of a DHIS2 TrackedEntity from the MdBusiness.
   * 
   * Example format: http://localhost:8085/api/25/metadata.json?trackedEntities=true
   */
  public JSONObject getTrackedEntityJson()
  {
    JSONObject trackedEntity = new JSONObject();
    
    trackedEntity.put("name", mdbiz.getDisplayLabel().getValue());
    trackedEntity.put("description", mdbiz.getDescription().getValue());
    
    return trackedEntity;
  }
}
