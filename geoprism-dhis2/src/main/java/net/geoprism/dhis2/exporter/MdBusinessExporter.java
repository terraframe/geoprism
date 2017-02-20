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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.GeoEntity;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.system.metadata.MdAttributeConcreteDTO;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdBusinessDTO;

import net.geoprism.dhis2.DHIS2BasicConnector;
import net.geoprism.dhis2.response.DHIS2DuplicateDataException;
import net.geoprism.dhis2.response.DHIS2ResponseProcessor;
import net.geoprism.dhis2.response.DHIS2UnexpectedResponseException;
import net.geoprism.ontology.Classifier;
import net.geoprism.ontology.ClassifierSynonym;

/**
 * This class is responsible for exporting an MdBusiness to DHIS2.
 * 
 * @author rrowlands
 */
public class MdBusinessExporter
{
  private static Logger logger = LoggerFactory.getLogger(MdBusinessExporter.class);
  
  protected MdBusiness mdbiz;
  
  protected MdBusinessToTrackerJson converter;
  
  protected DHIS2BasicConnector dhis2;
  
  private String trackedEntityId;
  
  private Map<String, String> trackedEntityAttributeIds;
  
  private Map<String, String> programTrackedEntityAttributeIds;
  
  private String programId = null;
  
  private final int pageSize = 1000;
  
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
    createProgramTrackedEntityAttributes();
    createProgram();
    createAndEnrollTrackedEntityInstances();
  }
  
  protected void createAndEnrollTrackedEntityInstances()
  {
    List<? extends MdAttribute> mdAttrs = mdbiz.getAllAttribute().getAll();
    
    QueryFactory qf = new QueryFactory();
    BusinessQuery bq = qf.businessQuery(mdbiz.definesType());
    
    long count = bq.getCount();
    int page = 0;
    
    while ( (page * pageSize) < count )
    {
      JSONObject jsonMetadata = new JSONObject();
      
      JSONArray trackedEntityInstances = new JSONArray();
      
      OIterator<Business> it = bq.getIterator(pageSize, page+1);
      try
      {
        while (it.hasNext())
        {
          Business biz = it.next();
          
          JSONObject trackedEntityInstance = new JSONObject();
          
          trackedEntityInstance.put("trackedEntity", trackedEntityId);
          
          GeoEntity orgUnit = null;
          
          JSONArray jAttributes = new JSONArray();
          for (MdAttribute mdAttr : mdAttrs)
          {
            if (mdAttr.getValue(MdAttributeConcreteDTO.SYSTEM).equals(MdAttributeBooleanInfo.FALSE) && 
                !ArrayUtils.contains(MdBusinessExporter.skipAttrs, mdAttr.getValue(MdAttributeConcreteDTO.ATTRIBUTENAME))
              )
            {
              JSONObject jAttribute = new JSONObject();
              String attrName = mdAttr.getAttributeName();
              jAttribute.put("attribute", trackedEntityAttributeIds.get(mdAttr.getId()));
              
              if (mdAttr instanceof MdAttributeReference)
              {
                String refId = biz.getValue(attrName);
                if (!refId.equals(""))
                {
                  MdBusiness reference = ((MdAttributeReference) mdAttr).getMdBusiness();
                  
                  if (reference.definesType().equals(GeoEntity.CLASS))
                  {
                    orgUnit = GeoEntity.get(refId);
                  }
                  else if (reference.definesType().equals(Classifier.CLASS))
                  {
                    Classifier classy = Classifier.get(refId);
                    jAttribute.put("value", classy.getDisplayLabel().getValue());
                  }
                  else if (reference.definesType().equals(ClassifierSynonym.CLASS))
                  {
//                    System.out.println("TODO : We just hit a ClassifierSynonym reference in tei.");
                  }
                }
              }
              else if (trackedEntityAttributeIds.containsKey(mdAttr.getId()))
              {
                jAttribute.put("value", biz.getValue(attrName));
              }
              
              if (jAttribute.has("value"))
              {
                jAttributes.put(jAttribute);
              }
            }
          }
          
          if (orgUnit != null)
          {
            trackedEntityInstance.put("attributes", jAttributes);
            
            JSONArray enrollments = new JSONArray();
            JSONObject enrollment = new JSONObject();
            enrollment.put("orgUnit", orgUnit.getGeoId());
            enrollment.put("program", programId);
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            enrollment.put("incidentDate", date);
            enrollment.put("enrollmentDate", date);
            enrollments.put(enrollment);
            trackedEntityInstance.put("enrollments", enrollments);
            
            // TODO : The geoId contains the DHIS2 id, and not the actual geoid. We do this because it has to be referenced when we export and there's no other place to save the DHIS2 id.
            // If we make this a real product perhaps we need to store a map from runway id to dhis2 id in some table somewhere.
            trackedEntityInstance.put("orgUnit", orgUnit.getGeoId());
            
            trackedEntityInstances.put(trackedEntityInstance);
          }
        }
      }
      finally
      {
        it.close();
      }
      
      jsonMetadata.put("trackedEntityInstances", trackedEntityInstances);
      
      JSONObject response = dhis2.httpPost("api/25/trackedEntityInstances", jsonMetadata.toString());
      DHIS2ResponseProcessor.validateImportSummaryResponse(response);
      
      page = page + 1;
    }
  }
  
  protected void createTrackedEntity()
  {
    JSONObject jsonMetadata = new JSONObject();
    
    JSONArray trackedEntities = new JSONArray();
    trackedEntities.put(converter.getTrackedEntityJson());
    jsonMetadata.put("trackedEntities", trackedEntities);
    
    JSONObject response = dhis2.httpPost("api/25/metadata", jsonMetadata.toString());
    DHIS2ResponseProcessor.validateTypeReportResponse(response, false);
    
    getTrackedEntityId();
  }
  
  protected void getTrackedEntityId()
  {
    JSONObject response = dhis2.httpGet("api/25/metadata", new NameValuePair[] {
      new NameValuePair("assumeTrue", "false"),
      new NameValuePair("trackedEntities", "true")
    });
    
    if (response != null && response.has("trackedEntities"))
    {
      JSONArray trackedEntities = response.getJSONArray("trackedEntities");
      
      for (int i = 0; i < trackedEntities.length(); ++i)
      {
        JSONObject trackedEntity = trackedEntities.getJSONObject(i);
        
        if (trackedEntity.getString("name").equals(mdbiz.getDisplayLabel().getValue()))
        {
          trackedEntityId = trackedEntity.getString("id");
          break;
        }
      }
    }
    
    if (trackedEntityId == null)
    {
      throw new RuntimeException("Unable to find previously created TrackedEntitiy.");
    }
  }
  
  protected void createTrackedEntityAttributes()
  {
    JSONObject jsonMetadata = new JSONObject();
    
    JSONArray trackedEntityAttributes = converter.getTrackedEntityAttributes();
    
    jsonMetadata.put("trackedEntityAttributes", trackedEntityAttributes);
    
    JSONObject response = dhis2.httpPost("api/25/metadata", jsonMetadata.toString());
    
    try
    {
      DHIS2ResponseProcessor.validateTypeReportResponse(response, true);
    }
    catch (DHIS2DuplicateDataException e)
    {
      List<String> msgs = e.getErrorMessages();
      
      Set<String> attrNames = new HashSet<String>();
      for (String msg : msgs)
      {
        // Property `name`Â with value `Gender` on object Gender [XWImNYPqAIz] (TrackedEntityAttribute)Â already exists on object cejWyOfXge6.
        Pattern p = Pattern.compile("Property `(.*)`.*with value `(.*)` on object .*");
        Matcher m = p.matcher(msg);
        
        if (m.find())
        {
          String attrName = m.group(2);
          
          if (attrName != null)
          {
            attrNames.add(attrName);
          }
        }
        else
        {
          // If we get some weird message back that doesn't match our regex
          throw new DHIS2UnexpectedResponseException("Unexpected response [" + response + "]");
        }
      }
      
      String msg = "Attributes [" + StringUtils.join(attrNames, ", ") + "] already exist in DHIS2 as a TrackedEntityAttribute. Delete your Geoprism dataset and reimport your spreadsheet with a different attribute name (or delete the attributes in DHIS2).";
      throw new DHIS2DuplicateDataException(msg);
    }
    
    getTrackedEntityAttributeIds();
  }
  
  static String[] skipAttrs = new String[]{
    MdBusinessDTO.CACHEALGORITHM, MdBusinessDTO.TABLENAME, MdBusinessDTO.KEYNAME,
    MdBusinessDTO.BASECLASS, MdBusinessDTO.BASESOURCE, MdBusinessDTO.DTOCLASS, MdBusinessDTO.DTOSOURCE, MdBusinessDTO.STUBCLASS, MdBusinessDTO.STUBDTOCLASS, MdBusinessDTO.STUBDTOSOURCE, MdBusinessDTO.STUBSOURCE,
    MdAttributeConcreteDTO.GETTERVISIBILITY, MdAttributeConcreteDTO.INDEXTYPE, MdAttributeConcreteDTO.INDEXNAME, MdAttributeConcreteDTO.COLUMNNAME,
    MdAttributeConcreteDTO.DEFININGMDCLASS, MdAttributeConcreteDTO.ENTITYDOMAIN, MdAttributeConcreteDTO.OWNER, MdAttributeConcreteDTO.SETTERVISIBILITY, MdAttributeConcreteDTO.SITEMASTER
  };
  protected void getTrackedEntityAttributeIds()
  {
    trackedEntityAttributeIds = new HashMap<String, String>();
    
    List<? extends MdAttribute> mdAttrs = mdbiz.getAllAttribute().getAll();
    
    JSONObject response = dhis2.httpGet("api/25/metadata", new NameValuePair[] {
      new NameValuePair("assumeTrue", "false"),
      new NameValuePair("trackedEntityAttributes", "true")
    });
    
    boolean fatalResponse = false;
    
    if (response != null && response.has("trackedEntityAttributes"))
    {
      JSONArray TEAs = response.getJSONArray("trackedEntityAttributes");
      
      if (TEAs.length() == 0)
      {
        fatalResponse = true;
      }
      
      for (int i = 0; i < TEAs.length(); ++i)
      {
        JSONObject TEA = TEAs.getJSONObject(i);
        
        for (MdAttribute mdAttr : mdAttrs)
        {
          if (mdAttr.getValue(MdAttributeConcreteDTO.SYSTEM).equals(MdAttributeBooleanInfo.FALSE) && 
              !ArrayUtils.contains(skipAttrs, mdAttr.getValue(MdAttributeConcreteDTO.ATTRIBUTENAME)) &&
              TEA.getString("name").equals(mdAttr.getDisplayLabel().getValue())
            )
          {
            trackedEntityAttributeIds.put(mdAttr.getId(), TEA.getString("id"));
          }
        }
      }
    }
    else
    {
      fatalResponse = true;
    }
    
    if (fatalResponse || trackedEntityAttributeIds.values().size() == 0)
    {
      throw new RuntimeException("Unable to find previously created TrackedEntitiyAttributes.");
    }
  }
  
  protected void createProgramTrackedEntityAttributes()
  {
    JSONObject jsonMetadata = new JSONObject();
    
    jsonMetadata.put("programTrackedEntityAttributes", converter.getProgramTrackedEntityAttributes(programId, trackedEntityAttributeIds));
    
    JSONObject response = dhis2.httpPost("api/25/metadata", jsonMetadata.toString());
    DHIS2ResponseProcessor.validateTypeReportResponse(response, false);
    
    getProgramTrackedEntityAttributeIds();
  }
  
  protected void getProgramTrackedEntityAttributeIds()
  {
    programTrackedEntityAttributeIds = new HashMap<String, String>();
    
    List<? extends MdAttribute> mdAttrs = mdbiz.getAllAttribute().getAll();
    
    JSONObject response = dhis2.httpGet("api/25/metadata", new NameValuePair[] {
      new NameValuePair("assumeTrue", "false"),
      new NameValuePair("programTrackedEntityAttributes", "true")
    });
    
    boolean fatalResponse = false;
    
    if (response != null && response.has("programTrackedEntityAttributes"))
    {
      JSONArray PTEAs = response.getJSONArray("programTrackedEntityAttributes");
      
      if (PTEAs.length() == 0)
      {
        fatalResponse = true;
      }
      
      for (int i = 0; i < PTEAs.length(); ++i)
      {
        JSONObject PTEA = PTEAs.getJSONObject(i);
        
        for (MdAttribute mdAttr : mdAttrs)
        {
          if (mdAttr.getValue(MdAttributeConcreteDTO.SYSTEM).equals(MdAttributeBooleanInfo.FALSE) && 
              !ArrayUtils.contains(skipAttrs, mdAttr.getValue(MdAttributeConcreteDTO.ATTRIBUTENAME)) &&
              ( PTEA.has("trackedEntityAttribute") && PTEA.getJSONObject("trackedEntityAttribute").getString("id").equals(trackedEntityAttributeIds.get(mdAttr.getId())) )
            )
          {
            programTrackedEntityAttributeIds.put(mdAttr.getId(), PTEA.getString("id"));
          }
        }
      }
    }
    else
    {
      fatalResponse = true;
    }
    
    if (fatalResponse || programTrackedEntityAttributeIds.values().size() == 0)
    {
      throw new RuntimeException("Unable to find previously created ProgramTrackedEntitiyAttributes.");
    }
  }
  
  /**
   * Fetches the id of a category combo with the provided name
   * 
   * Example: http://localhost:8085/api/25/metadata?assumeTrue=false&categoryCombos=true
   */
  public String getCategoryComboId(String name)
  {
    String categoryComboId = null;
    
    JSONObject response = dhis2.httpGet("api/25/metadata", new NameValuePair[] {
      new NameValuePair("assumeTrue", "false"),
      new NameValuePair("categoryCombos", "true")
    });
    
    if (response != null && response.has("categoryCombos"))
    {
      JSONArray combos = response.getJSONArray("categoryCombos");
      
      for (int i = 0; i < combos.length(); ++i)
      {
        JSONObject combo = combos.getJSONObject(i);
        
        if (combo.getString("name").equals("default"))
        {
          categoryComboId = combo.getString("id");
          break;
        }
      }
    }
    
    if (categoryComboId == null)
    {
      throw new RuntimeException("Unable to find a DHIS2 category combo by name [" + name + "].");
    }
    
    return categoryComboId;
  }
  
  protected void createProgram()
  {
    JSONObject jsonMetadata = new JSONObject();
    
    JSONArray programs = new JSONArray();
    programs.put(converter.getProgramJson(trackedEntityId, getCategoryComboId("default"), programTrackedEntityAttributeIds.values()));
    jsonMetadata.put("programs", programs);
    
    JSONObject response = dhis2.httpPost("api/25/metadata", jsonMetadata.toString());
    DHIS2ResponseProcessor.validateTypeReportResponse(response, false);
    
    getProgramId();
  }
  
  protected void getProgramId()
  {
    JSONObject response = dhis2.httpGet("api/25/metadata", new NameValuePair[] {
      new NameValuePair("assumeTrue", "false"),
      new NameValuePair("programs", "true")
    });
    
    if (response != null && response.has("programs"))
    {
      JSONArray programs = response.getJSONArray("programs");
      
      for (int i = 0; i < programs.length(); ++i)
      {
        JSONObject program = programs.getJSONObject(i);
        
        if (program.getString("name").equals(mdbiz.getDisplayLabel().getValue() + " Program") && program.getJSONObject("trackedEntity").getString("id").equals(trackedEntityId))
        {
          programId = program.getString("id");
          break;
        }
      }
    }
    
    if (programId == null)
    {
      throw new RuntimeException("Unable to find previously created Program.");
    }
  }
}
