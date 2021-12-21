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
package net.geoprism.ontology;

@com.runwaysdk.business.ClassSignature(hash = -489982773)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to ClassifierProblemTypeMaster.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class ClassifierProblemTypeMasterBase extends com.runwaysdk.system.EnumerationMaster 
{
  public final static String CLASS = "net.geoprism.ontology.ClassifierProblemTypeMaster";
  public static java.lang.String DESCRIPTION = "description";
  private com.runwaysdk.business.Struct description = null;
  
  private static final long serialVersionUID = -489982773;
  
  public ClassifierProblemTypeMasterBase()
  {
    super();
    description = super.getStruct("description");
  }
  
  public net.geoprism.ontology.ClassifierProblemTypeMasterDescription getDescription()
  {
    return (net.geoprism.ontology.ClassifierProblemTypeMasterDescription) description;
  }
  
  public void validateDescription()
  {
    this.validateAttribute(DESCRIPTION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF getDescriptionMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.ontology.ClassifierProblemTypeMaster.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF)mdClassIF.definesAttribute(DESCRIPTION);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static ClassifierProblemTypeMasterQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    ClassifierProblemTypeMasterQuery query = new ClassifierProblemTypeMasterQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
  }
  
  public static ClassifierProblemTypeMaster get(String oid)
  {
    return (ClassifierProblemTypeMaster) com.runwaysdk.business.Business.get(oid);
  }
  
  public static ClassifierProblemTypeMaster getByKey(String key)
  {
    return (ClassifierProblemTypeMaster) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static ClassifierProblemTypeMaster getEnumeration(String enumName)
  {
    return (ClassifierProblemTypeMaster) com.runwaysdk.business.Business.getEnumeration(net.geoprism.ontology.ClassifierProblemTypeMaster.CLASS ,enumName);
  }
  
  public static ClassifierProblemTypeMaster lock(java.lang.String oid)
  {
    ClassifierProblemTypeMaster _instance = ClassifierProblemTypeMaster.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static ClassifierProblemTypeMaster unlock(java.lang.String oid)
  {
    ClassifierProblemTypeMaster _instance = ClassifierProblemTypeMaster.get(oid);
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
