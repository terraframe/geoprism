/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.dhis2.exporter;

import java.util.Collection;
import java.util.Map;

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

import net.geoprism.dhis2.DHIS2Util;

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
    trackedEntity.put("id", DHIS2Util.runwayIdToDHIS2Id(mdbiz.getId()));
    
    return trackedEntity;
  }
  
  /**
   * Creates the JSON representation of an array of DHIS2 tracked entity attributes by looping over the MdAttributes associated with this MdBusiness.
   * 
   * Example format: http://localhost:8085/api/25/metadata.json?trackedEntityAttributes=true
   */
  public JSONArray getTrackedEntityAttributes()
  {
    JSONArray jsonAttrs = new JSONArray();
    
    OIterator<? extends MdAttribute> mdAttrs = mdbiz.getAllAttribute();
    for (MdAttribute mdAttr : mdAttrs)
    {
      if (mdAttr.getValue(MdAttributeConcreteDTO.SYSTEM).equals(MdAttributeBooleanInfo.FALSE) && 
          !ArrayUtils.contains(MdBusinessExporter.skipAttrs, mdAttr.getValue(MdAttributeConcreteDTO.ATTRIBUTENAME))
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
  
  public JSONArray getProgramTrackedEntityAttributes(String programId, Map<String, String> trackedEntityAttributeIds)
  {
    JSONArray jsonAttrs = new JSONArray();
    
    OIterator<? extends MdAttribute> mdAttrs = mdbiz.getAllAttribute();
    for (MdAttribute mdAttr : mdAttrs)
    {
      if (mdAttr.getValue(MdAttributeConcreteDTO.SYSTEM).equals(MdAttributeBooleanInfo.FALSE) && 
          !ArrayUtils.contains(MdBusinessExporter.skipAttrs, mdAttr.getValue(MdAttributeConcreteDTO.ATTRIBUTENAME))
        )
      {
        JSONObject jsonAttr = new JSONObject();
        if (programId != null)
        {
          jsonAttr.put("program", new JSONObject().put("id", programId));
        }
        jsonAttr.put("trackedEntityAttribute", new JSONObject().put("id", trackedEntityAttributeIds.get(mdAttr.getId())));
        jsonAttr.put("displayInList", "true");
        jsonAttr.put("mandatory", "false");
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
  public JSONObject getProgramJson(String trackedEntityId, String categoryComboId, Collection<String> attributeIds)
  {
    JSONObject program = new JSONObject();
    
    program.put("name", mdbiz.getDisplayLabel().getValue() + " Program");
    program.put("shortName", mdbiz.getDisplayLabel().getValue() + " Program");
    program.put("programType", "WITH_REGISTRATION");
    program.put("trackedEntity", new JSONObject().put("id", DHIS2Util.runwayIdToDHIS2Id(mdbiz.getId())));
    program.put("incidentDateLabel", "Incident date");
    program.put("enrollmentDateLabel", "Enrollment date");
    program.put("categoryCombo", new JSONObject().put("id", categoryComboId));
    
    JSONArray attrs = new JSONArray();
    if (attributeIds != null)
    {
      for (String id : attributeIds)
      {
        attrs.put(new JSONObject().put("id", id));
      }
    }
    program.put("programTrackedEntityAttributes", attrs);
    
    return program;
  }
  
  public JSONObject getPatchProgramJson(Collection<String> attributeIds)
  {
    JSONObject program = new JSONObject();
    
    
    
    return program;
  }
}
