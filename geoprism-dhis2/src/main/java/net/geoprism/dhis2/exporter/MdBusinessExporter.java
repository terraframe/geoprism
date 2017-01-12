package net.geoprism.dhis2.exporter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.system.metadata.MdBusiness;

import net.geoprism.dhis2.DHIS2BasicConnector;

/**
 * This class is responsible for exporting an MdBusiness to DHIS2.
 * 
 * @author rrowlands
 */
public class MdBusinessExporter
{
  private Logger logger = LoggerFactory.getLogger(MdBusinessExporter.class);
  
  protected MdBusiness mdbiz;
  
  protected MdBusinessToTrackerJson converter;
  
  protected DHIS2BasicConnector dhis2;
  
  public MdBusinessExporter(MdBusiness mdbiz, DHIS2BasicConnector dhis2)
  {
    this.mdbiz = mdbiz;
    this.converter = new MdBusinessToTrackerJson(mdbiz);
    this.dhis2 = dhis2;
  }
  
  public void exportToTracker()
  {
    createTrackedEntity();
    createTrackedEntityAttributes();
//    createProgram();
  }
  
  public void createTrackedEntity()
  {
    JSONObject jsonMetadata = new JSONObject();
    
    JSONArray trackedEntities = new JSONArray();
    trackedEntities.put(converter.getTrackedEntityJson());
    jsonMetadata.put("trackedEntities", trackedEntities);
    
    JSONObject response = dhis2.httpPost("api/25/metadata", jsonMetadata.toString());
    if (response != null)
    {
      logger.info(response.toString());
      
      if (response.has("status") && response.get("status").equals("ERROR"))
      {
//        {
//          "status": "ERROR",
//          "typeReports": [
//            {
//              "klass": "org.hisp.dhis.trackedentity.TrackedEntity",
//              "stats": {
//                "total": 1,
//                "created": 0,
//                "updated": 0,
//                "deleted": 0,
//                "ignored": 1
//              },
//              "objectReports": [
//                {
//                  "klass": "org.hisp.dhis.trackedentity.TrackedEntity",
//                  "index": 0,
//                  "errorReports": [
//                    {
//                      "message": "Property `name`Â with value `Labor Force By State 2015` on object Labor Force By State 2015 [Lg3bG0HelP0] (TrackedEntity)Â already exists on object Yw2zJnEX0o4.",
//                      "mainKlass": "org.hisp.dhis.trackedentity.TrackedEntity",
//                      "errorCode": "E5003"
//                    }
//                  ]
//                }
//              ]
//            }
//          ],
//          "stats": {
//            "total": 1,
//            "created": 0,
//            "updated": 0,
//            "deleted": 0,
//            "ignored": 1
//          }
//        }
        
        JSONArray typeReports = response.getJSONArray("typeReports");
        
        for (int i = 0; i < typeReports.length(); ++i)
        {
          JSONObject typeReport = typeReports.getJSONObject(i);
          
          if (typeReport.getString("klass").equals("org.hisp.dhis.trackedentity.TrackedEntity"))
          {
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
                  logger.error("Tracked entity already exists.");
                }
              }
            }
          }
        }
      }
    }
  }
  
  public void createTrackedEntityAttributes()
  {
    JSONObject jsonMetadata = new JSONObject();
    
    jsonMetadata.put("trackedEntityAttributes", converter.getTrackedEntityAttributes());
    
    JSONObject response = dhis2.httpPost("api/25/metadata", jsonMetadata.toString());
    if (response != null)
    {
      logger.info(response.toString());
      
      if (response.has("status") && response.get("status").equals("ERROR"))
      {
        
      }
    }
  }
  
  public void createProgram()
  {
    JSONObject jsonMetadata = new JSONObject();
    
    JSONArray programs = new JSONArray();
    programs.put(converter.getProgramJson());
    jsonMetadata.put("programs", programs);
    
    JSONObject response = dhis2.httpPost("api/25/metadata", jsonMetadata.toString());
    if (response != null)
    {
      logger.info(response.toString());
      
      if (response.has("status") && response.get("status").equals("ERROR"))
      {
        
      }
    }
  }
}
