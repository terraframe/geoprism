package net.geoprism.dhis2.exporter;

import org.apache.commons.lang.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.metadata.MdAttributeGeometry;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.system.metadata.MdAttributeBoolean;
import com.runwaysdk.system.metadata.MdAttributeCharacter;
import com.runwaysdk.system.metadata.MdAttributeConcreteDTO;
import com.runwaysdk.system.metadata.MdAttributeDate;
import com.runwaysdk.system.metadata.MdAttributeDouble;
import com.runwaysdk.system.metadata.MdAttributeFloat;
import com.runwaysdk.system.metadata.MdAttributeInteger;
import com.runwaysdk.system.metadata.MdAttributeLong;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.system.metadata.MdAttributeTerm;
import com.runwaysdk.system.metadata.MdAttributeText;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdBusinessDTO;

/**
 * Responsible for exporting an MdBusiness directly to DHIS2 Tracker
 * 
 * @author rrowlands
 */
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
  
  /**
   * Creates the JSON representation of an array of DHIS2 tracked entity attributes by looping over the MdAttributes associated with this MdBusiness.
   * 
   * Example format: http://localhost:8085/api/25/metadata.json?trackedEntityAttributes=true
   */
  private String[] skipAttrs = new String[]{
    MdBusinessDTO.CACHEALGORITHM, MdBusinessDTO.TABLENAME, MdBusinessDTO.KEYNAME,
    MdBusinessDTO.BASECLASS, MdBusinessDTO.BASESOURCE, MdBusinessDTO.DTOCLASS, MdBusinessDTO.DTOSOURCE, MdBusinessDTO.STUBCLASS, MdBusinessDTO.STUBDTOCLASS, MdBusinessDTO.STUBDTOSOURCE, MdBusinessDTO.STUBSOURCE,
    MdAttributeConcreteDTO.GETTERVISIBILITY, MdAttributeConcreteDTO.INDEXTYPE, MdAttributeConcreteDTO.INDEXNAME, MdAttributeConcreteDTO.COLUMNNAME,
    MdAttributeConcreteDTO.DEFININGMDCLASS, MdAttributeConcreteDTO.ENTITYDOMAIN, MdAttributeConcreteDTO.OWNER, MdAttributeConcreteDTO.SETTERVISIBILITY, MdAttributeConcreteDTO.SITEMASTER
  };
  public JSONArray getTrackedEntityAttributes()
  {
    JSONArray jsonAttrs = new JSONArray();
    
    OIterator<? extends MdAttribute> mdAttrs = mdbiz.getAllAttribute();
    for (MdAttribute mdAttr : mdAttrs)
    {
      if (mdAttr.getValue(MdAttributeConcreteDTO.SYSTEM).equals(MdAttributeBooleanInfo.FALSE) && 
          !ArrayUtils.contains(skipAttrs, mdAttr.getValue(MdAttributeConcreteDTO.ATTRIBUTENAME))
        )
      {
        JSONObject jsonAttr = new JSONObject();
        jsonAttr.put("name", mdAttr.getDisplayLabel().getValue());
        jsonAttr.put("shortName", mdAttr.getDisplayLabel().getValue());
        
        // Find the corresponding DHIS2 attribute type from our Runway MdAttribute types
        String valueType = "TEXT";
        if (mdAttr instanceof MdAttributeDate)
        {
          valueType = "DATE";
        }
        else if (mdAttr instanceof MdAttributeCharacter)
        {
          valueType = "TEXT";
        }
        else if (mdAttr instanceof MdAttributeText)
        {
          valueType = "LONG_TEXT";
        }
        else if (mdAttr instanceof MdAttributeBoolean)
        {
          valueType = "BOOLEAN";
        }
        else if (mdAttr instanceof MdAttributeInteger || mdAttr instanceof MdAttributeLong)
        {
          valueType = "INTEGER";
        }
        else if (mdAttr instanceof MdAttributeDouble || mdAttr instanceof MdAttributeFloat)
        {
          valueType = "NUMBER";
        }
        else if (mdAttr instanceof MdAttributeTerm)
        {
  //        throw new UnsupportedOperationException();
        }
        else if (mdAttr instanceof MdAttributeReference)
        {
          MdBusiness reference = ((MdAttributeReference) mdAttr).getMdBusiness();
          
          if (reference.definesType().equals(GeoEntity.CLASS))
          {
            valueType = "ORGANISATION_UNIT";
          }
        }
        else if (mdAttr instanceof MdAttributeGeometry)
        {
          valueType = "COORDINATE";
        }
        jsonAttr.put("valueType", valueType);
        
        jsonAttrs.put(jsonAttr);
      }
    }
    
    return jsonAttrs;
  }
  
  /**
   * Creates the JSON representation of a DHIS2 Program from the MdBusiness.
   * 
   * Example format: http://localhost:8085/api/25/metadata.json?programs=true
   */
  public JSONObject getProgramJson()
  {
    JSONObject program = new JSONObject();
    
    program.put("name", mdbiz.getDisplayLabel().getValue());
    program.put("shortName", mdbiz.getDisplayLabel().getValue());
    program.put("programType", "WITH_REGISTRATION");
    program.put("trackedEntity", new JSONObject().put("id", "???"));
    program.put("incidentDateLabel", "Date of creation");
    program.put("enrollmentDateLabel", "Date of enrollment");
    
    JSONArray attrs = new JSONArray();
    program.put("programTrackedEntityAttributes", attrs);
    
    
//    "displayFrontPageList": false,
//    "enrollmentDateLabel": "Date of enrollment",
//    "onlyEnrollOnce": true,
//    "programType": "WITH_REGISTRATION",
    
    
    return program;
  }
}
