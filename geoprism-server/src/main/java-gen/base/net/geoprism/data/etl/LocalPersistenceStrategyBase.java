/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package net.geoprism.data.etl;

@com.runwaysdk.business.ClassSignature(hash = -1736536689)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to LocalPersistenceStrategy.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class LocalPersistenceStrategyBase extends net.geoprism.data.etl.PersistenceStrategy 
{
  public final static String CLASS = "net.geoprism.data.etl.LocalPersistenceStrategy";
  private static final long serialVersionUID = -1736536689;
  
  public LocalPersistenceStrategyBase()
  {
    super();
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static LocalPersistenceStrategyQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    LocalPersistenceStrategyQuery query = new LocalPersistenceStrategyQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
  }
  
  public static LocalPersistenceStrategy get(String oid)
  {
    return (LocalPersistenceStrategy) com.runwaysdk.business.Business.get(oid);
  }
  
  public static LocalPersistenceStrategy getByKey(String key)
  {
    return (LocalPersistenceStrategy) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static LocalPersistenceStrategy lock(java.lang.String oid)
  {
    LocalPersistenceStrategy _instance = LocalPersistenceStrategy.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static LocalPersistenceStrategy unlock(java.lang.String oid)
  {
    LocalPersistenceStrategy _instance = LocalPersistenceStrategy.get(oid);
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
