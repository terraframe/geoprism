/**
 * Copyright (c) 2023 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package net.geoprism.graph;

@com.runwaysdk.business.ClassSignature(hash = 920363349)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to ChangeFrequencyMaster.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class ChangeFrequencyMasterBase extends com.runwaysdk.system.EnumerationMaster
{
  public final static String CLASS = "net.geoprism.graph.ChangeFrequencyMaster";
  private static final long serialVersionUID = 920363349;
  
  public ChangeFrequencyMasterBase()
  {
    super();
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static ChangeFrequencyMasterQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    ChangeFrequencyMasterQuery query = new ChangeFrequencyMasterQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
  }
  
  public static ChangeFrequencyMaster get(String oid)
  {
    return (ChangeFrequencyMaster) com.runwaysdk.business.Business.get(oid);
  }
  
  public static ChangeFrequencyMaster getByKey(String key)
  {
    return (ChangeFrequencyMaster) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static ChangeFrequencyMaster getEnumeration(String enumName)
  {
    return (ChangeFrequencyMaster) com.runwaysdk.business.Business.getEnumeration(net.geoprism.graph.ChangeFrequencyMaster.CLASS ,enumName);
  }
  
  public static ChangeFrequencyMaster lock(java.lang.String oid)
  {
    ChangeFrequencyMaster _instance = ChangeFrequencyMaster.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static ChangeFrequencyMaster unlock(java.lang.String oid)
  {
    ChangeFrequencyMaster _instance = ChangeFrequencyMaster.get(oid);
    _instance.unlock();
    
    return _instance;
  }
  
  public String toString()
  {
    if (this.isNew())
    {
      return "New: "+ this.getClassDisplayLabel();
    }
    else
    {
      return super.toString();
    }
  }
}
