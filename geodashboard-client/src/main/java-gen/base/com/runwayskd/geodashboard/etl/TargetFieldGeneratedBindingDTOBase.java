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

@com.runwaysdk.business.ClassSignature(hash = 1324100136)
public abstract class TargetFieldGeneratedBindingDTOBase extends com.runwayskd.geodashboard.etl.TargetFieldBindingDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwayskd.geodashboard.etl.TargetFieldGeneratedBinding";
  private static final long serialVersionUID = 1324100136;
  
  protected TargetFieldGeneratedBindingDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected TargetFieldGeneratedBindingDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static com.runwayskd.geodashboard.etl.TargetFieldGeneratedBindingDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwayskd.geodashboard.etl.TargetFieldGeneratedBindingDTO) dto;
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
  
  public static com.runwayskd.geodashboard.etl.TargetFieldGeneratedBindingQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwayskd.geodashboard.etl.TargetFieldGeneratedBindingQueryDTO) clientRequest.getAllInstances(com.runwayskd.geodashboard.etl.TargetFieldGeneratedBindingDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwayskd.geodashboard.etl.TargetFieldGeneratedBindingDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwayskd.geodashboard.etl.TargetFieldGeneratedBindingDTO.CLASS, "lock", _declaredTypes);
    return (com.runwayskd.geodashboard.etl.TargetFieldGeneratedBindingDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwayskd.geodashboard.etl.TargetFieldGeneratedBindingDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwayskd.geodashboard.etl.TargetFieldGeneratedBindingDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwayskd.geodashboard.etl.TargetFieldGeneratedBindingDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
