/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package net.geoprism;

@com.runwaysdk.business.ClassSignature(hash = 529266961)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to DataUploader.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class DataUploaderBase extends com.runwaysdk.business.Util 
{
  public final static String CLASS = "net.geoprism.DataUploader";
  public static java.lang.String OID = "oid";
  private static final long serialVersionUID = 529266961;
  
  public DataUploaderBase()
  {
    super();
  }
  
  public String getOid()
  {
    return getValue(OID);
  }
  
  public void validateId()
  {
    this.validateAttribute(OID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getOidMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.DataUploader.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static DataUploader get(String oid)
  {
    return (DataUploader) com.runwaysdk.business.Util.get(oid);
  }
  
  public static void cancelImport(java.lang.String configuration)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.DataUploader.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static java.lang.String createClassifierSynonym(java.lang.String classifierId, java.lang.String label)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.DataUploader.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static java.lang.String createGeoEntity(java.lang.String parentOid, java.lang.String universalId, java.lang.String label)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.DataUploader.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static java.lang.String createGeoEntitySynonym(java.lang.String entityId, java.lang.String label)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.DataUploader.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static void deleteClassifierSynonym(java.lang.String synonymId)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.DataUploader.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static void deleteGeoEntity(java.lang.String entityId)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.DataUploader.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static void deleteGeoEntitySynonym(java.lang.String synonymId)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.DataUploader.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static java.lang.String getAttributeInformation(java.lang.String fileName, java.io.InputStream fileStream)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.DataUploader.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static java.io.InputStream getErrorFile(java.lang.String oid)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.DataUploader.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static java.lang.String getOptionsJSON()
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.DataUploader.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static java.lang.String getSavedConfiguration(java.lang.String oid, java.lang.String sheetName)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.DataUploader.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static java.io.InputStream importData(java.lang.String configuration)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.DataUploader.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static void validateDatasetName(java.lang.String name, java.lang.String oid)
  {
    String msg = "This method should never be invoked.  It should be overwritten in net.geoprism.DataUploader.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
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
