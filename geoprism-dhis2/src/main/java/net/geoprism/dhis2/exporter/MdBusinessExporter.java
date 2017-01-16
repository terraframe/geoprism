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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdAttribute;
import com.runwaysdk.system.metadata.MdAttributeConcreteDTO;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdBusinessDTO;

import net.geoprism.dhis2.DHIS2BasicConnector;
import net.geoprism.dhis2.ErrorProcessor;

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
    createTrackedEntityInstances();
    // registerTrackedEntityInstances();
  }
  
  protected void createTrackedEntityInstances()
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
          
          JSONArray jAttributes = new JSONArray();
          for (MdAttribute mdAttr : mdAttrs)
          {
            if (mdAttr.getValue(MdAttributeConcreteDTO.SYSTEM).equals(MdAttributeBooleanInfo.FALSE) && 
                !ArrayUtils.contains(MdBusinessExporter.skipAttrs, mdAttr.getValue(MdAttributeConcreteDTO.ATTRIBUTENAME)) &&
                trackedEntityAttributeIds.containsKey(mdAttr.getId())
              )
            {
              String attrName = mdAttr.getAttributeName();
              
              JSONObject jAttribute = new JSONObject();
              jAttribute.put("attribute", trackedEntityAttributeIds.get(mdAttr.getId()));
              jAttribute.put("value", biz.getValue(attrName));
            }
          }
          trackedEntityInstance.put("attributes", jAttributes);
          
//          trackedEntityInstance.put("orgUnit", value);
          
          trackedEntityInstances.put(trackedEntityInstance);
        }
      }
      finally
      {
        it.close();
      }
      
      jsonMetadata.put("trackedEntityInstances", trackedEntityInstances);
      
      JSONObject response = dhis2.httpPost("api/25/trackedEntityInstances", jsonMetadata.toString());
      ErrorProcessor.validateImportSummaryResponse(response);
      
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
    ErrorProcessor.validateTypeReportResponse(response);
    
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
    
    jsonMetadata.put("trackedEntityAttributes", converter.getTrackedEntityAttributes());
    
    JSONObject response = dhis2.httpPost("api/25/metadata", jsonMetadata.toString());
    ErrorProcessor.validateTypeReportResponse(response);
    
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
              TEA.getString("name").equals(mdAttr.getDisplayLabel().getValue()
             )
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
    ErrorProcessor.validateTypeReportResponse(response);
    
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
    ErrorProcessor.validateTypeReportResponse(response);
    
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
