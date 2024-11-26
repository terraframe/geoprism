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

@com.runwaysdk.business.ClassSignature(hash = 662217928)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to UndirectedGraphTypeSnapshotDisplayLabel.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class UndirectedGraphTypeSnapshotDisplayLabelBase extends com.runwaysdk.business.LocalStruct
{
  public final static String CLASS = "net.geoprism.graph.UndirectedGraphTypeSnapshotDisplayLabel";
  public final static java.lang.String DEFAULTLOCALE = "defaultLocale";
  public final static java.lang.String KEYNAME = "keyName";
  public final static java.lang.String OID = "oid";
  public final static java.lang.String SITEMASTER = "siteMaster";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 662217928;
  
  public UndirectedGraphTypeSnapshotDisplayLabelBase()
  {
    super();
  }
  
  public UndirectedGraphTypeSnapshotDisplayLabelBase(com.runwaysdk.business.MutableWithStructs component, String structName)
  {
    super(component, structName);
  }
  
  public static UndirectedGraphTypeSnapshotDisplayLabel get(String oid)
  {
    return (UndirectedGraphTypeSnapshotDisplayLabel) com.runwaysdk.business.Struct.get(oid);
  }
  
  public static UndirectedGraphTypeSnapshotDisplayLabel getByKey(String key)
  {
    return (UndirectedGraphTypeSnapshotDisplayLabel) com.runwaysdk.business.Struct.get(CLASS, key);
  }
  
  public String getKeyName()
  {
    return getValue(KEYNAME);
  }
  
  public void validateKeyName()
  {
    this.validateAttribute(KEYNAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getKeyNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.UndirectedGraphTypeSnapshotDisplayLabel.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(KEYNAME);
  }
  
  public void setKeyName(String value)
  {
    if(value == null)
    {
      setValue(KEYNAME, "");
    }
    else
    {
      setValue(KEYNAME, value);
    }
  }
  
  public String getOid()
  {
    return getValue(OID);
  }
  
  public void validateOid()
  {
    this.validateAttribute(OID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF getOidMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.UndirectedGraphTypeSnapshotDisplayLabel.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public String getSiteMaster()
  {
    return getValue(SITEMASTER);
  }
  
  public void validateSiteMaster()
  {
    this.validateAttribute(SITEMASTER);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getSiteMasterMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.UndirectedGraphTypeSnapshotDisplayLabel.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(SITEMASTER);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static UndirectedGraphTypeSnapshotDisplayLabelQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    UndirectedGraphTypeSnapshotDisplayLabelQuery query = new UndirectedGraphTypeSnapshotDisplayLabelQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
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
