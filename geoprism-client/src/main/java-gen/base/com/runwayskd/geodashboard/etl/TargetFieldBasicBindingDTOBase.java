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
package com.runwayskd.geodashboard.etl;

@com.runwaysdk.business.ClassSignature(hash = 709980760)
public abstract class TargetFieldBasicBindingDTOBase extends com.runwayskd.geodashboard.etl.TargetFieldBindingDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwayskd.geodashboard.etl.TargetFieldBasicBinding";
  private static final long serialVersionUID = 709980760;
  
  protected TargetFieldBasicBindingDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected TargetFieldBasicBindingDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String SOURCEATTRIBUTE = "sourceAttribute";
  public com.runwaysdk.system.metadata.MdAttributeDTO getSourceAttribute()
  {
    if(getValue(SOURCEATTRIBUTE) == null || getValue(SOURCEATTRIBUTE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdAttributeDTO.get(getRequest(), getValue(SOURCEATTRIBUTE));
    }
  }
  
  public String getSourceAttributeId()
  {
    return getValue(SOURCEATTRIBUTE);
  }
  
  public void setSourceAttribute(com.runwaysdk.system.metadata.MdAttributeDTO value)
  {
    if(value == null)
    {
      setValue(SOURCEATTRIBUTE, "");
    }
    else
    {
      setValue(SOURCEATTRIBUTE, value.getId());
    }
  }
  
  public boolean isSourceAttributeWritable()
  {
    return isWritable(SOURCEATTRIBUTE);
  }
  
  public boolean isSourceAttributeReadable()
  {
    return isReadable(SOURCEATTRIBUTE);
  }
  
  public boolean isSourceAttributeModified()
  {
    return isModified(SOURCEATTRIBUTE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getSourceAttributeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(SOURCEATTRIBUTE).getAttributeMdDTO();
  }
  
  public static com.runwayskd.geodashboard.etl.TargetFieldBasicBindingDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwayskd.geodashboard.etl.TargetFieldBasicBindingDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createBusiness(this);
    }
    else
    {
      getRequest().update(this);
    }
  }
  public void delete()
  {
    getRequest().delete(this.getId());
  }
  
  public static com.runwayskd.geodashboard.etl.TargetFieldBasicBindingQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwayskd.geodashboard.etl.TargetFieldBasicBindingQueryDTO) clientRequest.getAllInstances(com.runwayskd.geodashboard.etl.TargetFieldBasicBindingDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwayskd.geodashboard.etl.TargetFieldBasicBindingDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwayskd.geodashboard.etl.TargetFieldBasicBindingDTO.CLASS, "lock", _declaredTypes);
    return (com.runwayskd.geodashboard.etl.TargetFieldBasicBindingDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwayskd.geodashboard.etl.TargetFieldBasicBindingDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwayskd.geodashboard.etl.TargetFieldBasicBindingDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwayskd.geodashboard.etl.TargetFieldBasicBindingDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
