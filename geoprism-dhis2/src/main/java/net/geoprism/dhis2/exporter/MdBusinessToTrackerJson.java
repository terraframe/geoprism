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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.dataaccess.metadata.MdAttributeTermDAO;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.gis.geo.LocatedIn;
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
import com.runwaysdk.system.metadata.MdAttributeText;
import com.runwaysdk.system.metadata.MdBusiness;

import net.geoprism.dhis2.importer.OptionSetJsonToClassifier;
import net.geoprism.dhis2.util.DHIS2IdCache;
import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierSynonym;

/**
 * Responsible for exporting an MdBusiness directly to DHIS2 Tracker
 * 
 * @author rrowlands
 */
public class MdBusinessToTrackerJson
{
  MdBusiness mdbiz;
  
  DHIS2IdCache idCache;
  
  private String trackedEntityId;
  
  private String programId;
  
  private Map<String, String> trackedEntityAttributeIds;
  
  public MdBusinessToTrackerJson(MdBusiness mdbiz, DHIS2IdCache idCache)
  {
    this.mdbiz = mdbiz;
    
    this.idCache = idCache;
  }
  
  public String getProgramId()
  {
    return programId;
  }
  
  public void setProgramId(String programId)
  {
    this.programId = programId;
  }
  
  public void setTrackedEntityId(String trackedEntityId)
  {
    this.trackedEntityId = trackedEntityId;
  }
  
  public String getTrackedEntityId()
  {
    return trackedEntityId;
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
    
    trackedEntityId = idCache.next();
    trackedEntity.put("id", trackedEntityId);
    
    return trackedEntity;
  }
  
  public static String getDHIS2TypeFromMdAttribute(MdAttribute mdAttr)
  {
    String valueType = null;
    
    // Complete list of valid DHIS2 (API version 25) datatypes:
    // UNIT_INTERVAL, LETTER, BOOLEAN, NUMBER, TEXT, DATE, LONG_TEXT, FILE_RESOURCE, USERNAME, TRACKER_ASSOCIATE, COORDINATE, INTEGER_POSITIVE, DATETIME, EMAIL, TRUE_ONLY, INTEGER, INTEGER_ZERO_OR_POSITIVE, ORGANISATION_UNIT, TIME, INTEGER_NEGATIVE, PERCENTAGE, PHONE_NUMBER
    
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
    else if (mdAttr instanceof MdAttributeInteger)
    {
      valueType = "INTEGER";
    }
    else if (mdAttr instanceof MdAttributeLong)
    {
      valueType = "NUMBER";
    }
    else if (mdAttr instanceof MdAttributeDouble)
    {
      valueType = "NUMBER";
    }
    else if (mdAttr instanceof MdAttributeFloat)
    {
      valueType = "NUMBER";
    }
    else
    {
      valueType = "TEXT";
    }
    
    return valueType;
  }
  
  public void setTrackedEntityAttributeIds(Map<String, String> teaid)
  {
    trackedEntityAttributeIds = teaid;
  }
  
  public Map<String, String> getTrackedEntityAttributeIds()
  {
    return trackedEntityAttributeIds;
  }
  
  /**
   * Creates a DHIS2 metadata export for creating tracked entity attributes based on the mdBusiness. This method will also create classifiers should they be required.
   * 
   * Example format: http://localhost:8085/api/25/metadata.json?trackedEntityAttributes=true
   */
  public JSONObject getTrackedEntityAttributesJSON()
  {
    JSONObject jsonMetadata = new JSONObject();
    
    JSONArray jsonAttrs = new JSONArray();
    
    ArrayList<Classifier> rootsToExport = new ArrayList<Classifier>();
    
    // When we export our roots, we need to remember what we've referenced the ids as.
    // key=runwayId value=DHIS2id
    Map<String, String> rootIdMap = new HashMap<String, String>();
    
    
    OIterator<? extends MdAttribute> mdAttrs = mdbiz.getAllAttribute();
    for (MdAttribute mdAttr : mdAttrs)
    {
      if (mdAttr.getValue(MdAttributeConcreteDTO.SYSTEM).equals(MdAttributeBooleanInfo.FALSE) && 
          !ArrayUtils.contains(MdBusinessExporter.skipAttrs, mdAttr.getValue(MdAttributeConcreteDTO.ATTRIBUTENAME))
        )
      {
        JSONObject jsonAttr = new JSONObject();
        
        String dhis2Id = idCache.next();
        jsonAttr.put("id", dhis2Id);
        
        jsonAttr.put("name", mdAttr.getDisplayLabel().getValue());
        jsonAttr.put("shortName", mdAttr.getDisplayLabel().getValue());
        jsonAttr.put("aggregationType", "NONE");
        
        // Find the corresponding DHIS2 attribute type from our Runway MdAttribute types
        String valueType = null;
        
        if (mdAttr instanceof MdAttributeDate)
        {
          valueType = "DATE";
        }
        else if (mdAttr instanceof MdAttributeCharacter)
        {
          if (mdAttr.getAttributeName().endsWith("FeatureId"))
          {
            continue;
          }
          
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
        else if (mdAttr instanceof MdAttributeInteger)
        {
          valueType = "INTEGER";
        }
        else if (mdAttr instanceof MdAttributeLong)
        {
          valueType = "NUMBER";
        }
        else if (mdAttr instanceof MdAttributeDouble)
        {
          valueType = "NUMBER";
        }
        else if (mdAttr instanceof MdAttributeFloat)
        {
          valueType = "NUMBER";
        }
        else if (mdAttr instanceof MdAttributeReference)
        {
          MdBusiness reference = ((MdAttributeReference) mdAttr).getMdBusiness();
          
          if (reference.definesType().equals(GeoEntity.CLASS))
          {
//            valueType = "ORGANISATION_UNIT";
            // We are skipping this one because the orgUnit is not set as an attribute, it has its own separate requirements
          }
          else if (reference.definesType().equals(Classifier.CLASS))
          {
            valueType = "TEXT";
            
            JSONObject optionSet = new JSONObject();
            
            Classifier root = Classifier.findClassifierRoot(MdAttributeTermDAO.get(mdAttr.getId()));
            
            String pack = root.getClassifierPackage();
            
            if (!pack.startsWith(OptionSetJsonToClassifier.DHIS2_CLASSIFIER_PACKAGE_PREFIX))
            {
              // If its not prefixed then it doesn't exist already in DHIS2
              rootsToExport.add(root);
              
              String rootDhis2Id = idCache.next();
              rootIdMap.put(root.getId(), rootDhis2Id);
              optionSet.put("id", rootDhis2Id);
            }
            else
            {
              optionSet.put("id", root.getClassifierId());
            }
            optionSet.put("valueType", "TEXT");
            jsonAttr.put("optionSet", optionSet);
          }
          else if (reference.definesType().equals(ClassifierSynonym.CLASS))
          {
//            System.out.println("TODO : We just hit a ClassifierSynonym reference in metadata.");
          }
        }
        else if (mdAttr instanceof MdAttributeGeometry)
        {
          valueType = "COORDINATE";
          continue; // TODO ?
        }
        
        if (valueType != null)
        {
          trackedEntityAttributeIds.put(mdAttr.getId(), dhis2Id);
          
          jsonAttr.put("valueType", valueType);
          jsonAttrs.put(jsonAttr);
        }
      }
    }
    
    JSONArray allOptions = new JSONArray();
    
    
    // Export all unknown classifiers and all their children to DHIS2 as new optionsets and options.
    JSONArray optionSets = new JSONArray();
    for (Classifier root : rootsToExport)
    {
      JSONObject optionSet = new JSONObject();
      
      String rootIdInDHIS2 = rootIdMap.get(root.getId());
      
      optionSet.put("name", root.getDisplayLabel().getValue());
      optionSet.put("id", rootIdInDHIS2);
      optionSet.put("valueType", "TEXT");
      optionSet.put("code", idCache.next()); // Required for 2.27 but not 2.25
      
      JSONArray optionSetOptions = new JSONArray();
      
      OIterator<? extends Classifier> cit = root.getAllIsAChild();
      while (cit.hasNext())
      {
        Classifier child = cit.next();
        String childIdInDHIS2 = idCache.next();
        
        JSONObject jChild = new JSONObject();
        jChild.put("id", childIdInDHIS2);
        optionSetOptions.put(jChild);
        
        
        JSONObject jOption = new JSONObject();
        jOption.put("id", childIdInDHIS2);
        jOption.put("name", child.getDisplayLabel().getValue());
        jOption.put("code", childIdInDHIS2); // Required for 2.27 but not 2.25
        
        JSONObject optionSetRef = new JSONObject();
        optionSetRef.put("id", rootIdInDHIS2);
        jOption.put("optionSet", optionSetRef);
        
        allOptions.put(jOption);
        
        
        // TODO : This should be managed in a table somewhere, rather than storing the DHIS2 id in the "classifierId" attribute.
        child.appLock();
        child.setClassifierId(childIdInDHIS2);
        child.setClassifierPackage(OptionSetJsonToClassifier.DHIS2_CLASSIFIER_PACKAGE_PREFIX + childIdInDHIS2);
        child.apply();
      }
      optionSet.put("options", optionSetOptions); // not required for 2.25
      
      root.appLock();
      root.setClassifierId(rootIdInDHIS2);
      root.setClassifierPackage(OptionSetJsonToClassifier.DHIS2_CLASSIFIER_PACKAGE_PREFIX + rootIdInDHIS2);
      root.apply();
      
      optionSets.put(optionSet);
    }
    jsonMetadata.put("optionSets", optionSets);
    
    jsonMetadata.put("options", allOptions);
    
    jsonMetadata.put("trackedEntityAttributes", jsonAttrs);
    
    return jsonMetadata;
  }
  
  // This code only works on v2.25
//  public JSONArray getProgramTrackedEntityAttributes(String programId, Map<String, String> trackedEntityAttributeIds)
//  {
//    JSONArray jsonAttrs = new JSONArray();
//    
//    OIterator<? extends MdAttribute> mdAttrs = mdbiz.getAllAttribute();
//    for (MdAttribute mdAttr : mdAttrs)
//    {
//      if (mdAttr.getValue(MdAttributeConcreteDTO.SYSTEM).equals(MdAttributeBooleanInfo.FALSE) && 
//          !ArrayUtils.contains(MdBusinessExporter.skipAttrs, mdAttr.getValue(MdAttributeConcreteDTO.ATTRIBUTENAME)) && 
//          trackedEntityAttributeIds.containsKey(mdAttr.getId())
//        )
//      {
//        JSONObject jsonAttr = new JSONObject();
//        if (programId != null)
//        {
//          jsonAttr.put("program", new JSONObject().put("id", programId));
//        }
//        jsonAttr.put("trackedEntityAttribute", new JSONObject().put("id", trackedEntityAttributeIds.get(mdAttr.getId())));
//        jsonAttr.put("displayInList", "true");
//        jsonAttr.put("mandatory", "false");
//        jsonAttrs.put(jsonAttr);
//      }
//    }
//    
//    return jsonAttrs;
//  }
  
  /**
   * Creates the JSON representation of a DHIS2 Program from the MdBusiness.
   * 
   * Example format: http://localhost:8085/api/25/metadata.json?programs=true
   */
  // 2.25 version
//  public JSONObject getProgramJson(String trackedEntityId, String categoryComboId, Collection<String> attributeIds)
//  {
//    JSONObject program = new JSONObject();
//    
//    program.put("name", mdbiz.getDisplayLabel().getValue() + " Program");
//    program.put("shortName", mdbiz.getDisplayLabel().getValue() + " Program");
//    program.put("programType", "WITH_REGISTRATION");
//    program.put("trackedEntity", new JSONObject().put("id", mdbiz.getId().substring(0, 11)));
//    program.put("incidentDateLabel", "Incident date");
//    program.put("enrollmentDateLabel", "Enrollment date");
//    program.put("categoryCombo", new JSONObject().put("id", categoryComboId));
//    
//    JSONArray units = new JSONArray();
//    GeoEntity.getRoot().getAllDescendants(LocatedIn.CLASS).forEach(term -> units.put(new JSONObject().put("id", ((GeoEntity) term).getGeoId())));
//    program.put("organisationUnits", units);
//    
//    JSONArray attrs = new JSONArray();
//    if (attributeIds != null)
//    {
//      for (String id : attributeIds)
//      {
//        attrs.put(new JSONObject().put("id", id));
//      }
//    }
//    program.put("programTrackedEntityAttributes", attrs);
//    
//    return program;
//  }
  
  // version 2.27
  public JSONObject getProgramJson(String categoryComboId, Map<String, String> trackedEntityAttributeIds)
  {
    JSONObject program = new JSONObject();
    
    programId = idCache.next();
    
    program.put("name", mdbiz.getDisplayLabel().getValue() + " Program");
    program.put("shortName", mdbiz.getDisplayLabel().getValue() + " Program");
    program.put("programType", "WITH_REGISTRATION");
    program.put("id", programId);
    program.put("trackedEntity", new JSONObject().put("id", trackedEntityId));
    program.put("incidentDateLabel", "Incident date");
    program.put("enrollmentDateLabel", "Enrollment date");
    program.put("categoryCombo", new JSONObject().put("id", categoryComboId));
    
    JSONArray units = new JSONArray();
    GeoEntity.getRoot().getAllDescendants(LocatedIn.CLASS).forEach(term -> units.put(new JSONObject().put("id", ((GeoEntity) term).getGeoId())));
    program.put("organisationUnits", units);
    
    JSONArray attrs = new JSONArray();
    if (trackedEntityAttributeIds != null)
    {
      OIterator<? extends MdAttribute> mdAttrs = mdbiz.getAllAttribute();
      for (MdAttribute mdAttr : mdAttrs)
      {
        if (mdAttr.getValue(MdAttributeConcreteDTO.SYSTEM).equals(MdAttributeBooleanInfo.FALSE) && 
            !ArrayUtils.contains(MdBusinessExporter.skipAttrs, mdAttr.getValue(MdAttributeConcreteDTO.ATTRIBUTENAME)) && 
            trackedEntityAttributeIds.containsKey(mdAttr.getId())
          )
        {
          JSONObject jattr = new JSONObject();
          
          jattr.put("program", new JSONObject().put("id", programId));
          jattr.put("trackedEntityAttribute", new JSONObject().put("id", trackedEntityAttributeIds.get(mdAttr.getId())));
          jattr.put("displayInList", "true");
          jattr.put("mandatory", "false");
          
          attrs.put(jattr);
        }
      }
    }
    program.put("programTrackedEntityAttributes", attrs);
    
    return program;
  }
}
