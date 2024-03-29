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

@com.runwaysdk.business.ClassSignature(hash = 2067769605)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to LabeledPropertyGraphSynchronization.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class LabeledPropertyGraphSynchronizationBase extends com.runwaysdk.business.Business
{
  public final static String CLASS = "net.geoprism.graph.LabeledPropertyGraphSynchronization";
  public final static java.lang.String CREATEDATE = "createDate";
  public final static java.lang.String CREATEDBY = "createdBy";
  public final static java.lang.String DISPLAYLABEL = "displayLabel";
  private com.runwaysdk.business.Struct displayLabel = null;
  
  public final static java.lang.String ENTITYDOMAIN = "entityDomain";
  public final static java.lang.String ENTRY = "entry";
  public final static java.lang.String FORDATE = "forDate";
  public final static java.lang.String GRAPHTYPE = "graphType";
  public final static java.lang.String KEYNAME = "keyName";
  public final static java.lang.String LASTUPDATEDATE = "lastUpdateDate";
  public final static java.lang.String LASTUPDATEDBY = "lastUpdatedBy";
  public final static java.lang.String LOCKEDBY = "lockedBy";
  public final static java.lang.String OID = "oid";
  public final static java.lang.String OWNER = "owner";
  public final static java.lang.String REMOTEENTRY = "remoteEntry";
  public final static java.lang.String REMOTETYPE = "remoteType";
  public final static java.lang.String REMOTEVERSION = "remoteVersion";
  public final static java.lang.String SEQ = "seq";
  public final static java.lang.String SITEMASTER = "siteMaster";
  public final static java.lang.String TYPE = "type";
  public final static java.lang.String URL = "url";
  public final static java.lang.String VERSION = "version";
  public final static java.lang.String VERSIONNUMBER = "versionNumber";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 2067769605;
  
  public LabeledPropertyGraphSynchronizationBase()
  {
    super();
    displayLabel = super.getStruct("displayLabel");
  }
  
  public java.util.Date getCreateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(CREATEDATE));
  }
  
  public void validateCreateDate()
  {
    this.validateAttribute(CREATEDATE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF getCreateDateMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF)mdClassIF.definesAttribute(CREATEDATE);
  }
  
  public com.runwaysdk.system.SingleActor getCreatedBy()
  {
    if (getValue(CREATEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActor.get(getValue(CREATEDBY));
    }
  }
  
  public String getCreatedByOid()
  {
    return getValue(CREATEDBY);
  }
  
  public void validateCreatedBy()
  {
    this.validateAttribute(CREATEDBY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getCreatedByMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(CREATEDBY);
  }
  
  public net.geoprism.graph.LabeledPropertyGraphSynchronizationDisplayLabel getDisplayLabel()
  {
    return (net.geoprism.graph.LabeledPropertyGraphSynchronizationDisplayLabel) displayLabel;
  }
  
  public void validateDisplayLabel()
  {
    this.validateAttribute(DISPLAYLABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF getDisplayLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF)mdClassIF.definesAttribute(DISPLAYLABEL);
  }
  
  public com.runwaysdk.system.metadata.MdDomain getEntityDomain()
  {
    if (getValue(ENTITYDOMAIN).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdDomain.get(getValue(ENTITYDOMAIN));
    }
  }
  
  public String getEntityDomainOid()
  {
    return getValue(ENTITYDOMAIN);
  }
  
  public void validateEntityDomain()
  {
    this.validateAttribute(ENTITYDOMAIN);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getEntityDomainMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(ENTITYDOMAIN);
  }
  
  public void setEntityDomain(com.runwaysdk.system.metadata.MdDomain value)
  {
    if(value == null)
    {
      setValue(ENTITYDOMAIN, "");
    }
    else
    {
      setValue(ENTITYDOMAIN, value.getOid());
    }
  }
  
  public void setEntityDomainId(java.lang.String oid)
  {
    if(oid == null)
    {
      setValue(ENTITYDOMAIN, "");
    }
    else
    {
      setValue(ENTITYDOMAIN, oid);
    }
  }
  
  public net.geoprism.graph.LabeledPropertyGraphTypeEntry getEntry()
  {
    if (getValue(ENTRY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return net.geoprism.graph.LabeledPropertyGraphTypeEntry.get(getValue(ENTRY));
    }
  }
  
  public String getEntryOid()
  {
    return getValue(ENTRY);
  }
  
  public void validateEntry()
  {
    this.validateAttribute(ENTRY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getEntryMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(ENTRY);
  }
  
  public void setEntry(net.geoprism.graph.LabeledPropertyGraphTypeEntry value)
  {
    if(value == null)
    {
      setValue(ENTRY, "");
    }
    else
    {
      setValue(ENTRY, value.getOid());
    }
  }
  
  public void setEntryId(java.lang.String oid)
  {
    if(oid == null)
    {
      setValue(ENTRY, "");
    }
    else
    {
      setValue(ENTRY, oid);
    }
  }
  
  public java.util.Date getForDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(FORDATE));
  }
  
  public void validateForDate()
  {
    this.validateAttribute(FORDATE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF getForDateMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF)mdClassIF.definesAttribute(FORDATE);
  }
  
  public void setForDate(java.util.Date value)
  {
    if(value == null)
    {
      setValue(FORDATE, "");
    }
    else
    {
      setValue(FORDATE, new java.text.SimpleDateFormat(com.runwaysdk.constants.Constants.DATETIME_FORMAT).format(value));
    }
  }
  
  public net.geoprism.graph.LabeledPropertyGraphType getGraphType()
  {
    if (getValue(GRAPHTYPE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return net.geoprism.graph.LabeledPropertyGraphType.get(getValue(GRAPHTYPE));
    }
  }
  
  public String getGraphTypeOid()
  {
    return getValue(GRAPHTYPE);
  }
  
  public void validateGraphType()
  {
    this.validateAttribute(GRAPHTYPE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getGraphTypeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(GRAPHTYPE);
  }
  
  public void setGraphType(net.geoprism.graph.LabeledPropertyGraphType value)
  {
    if(value == null)
    {
      setValue(GRAPHTYPE, "");
    }
    else
    {
      setValue(GRAPHTYPE, value.getOid());
    }
  }
  
  public void setGraphTypeId(java.lang.String oid)
  {
    if(oid == null)
    {
      setValue(GRAPHTYPE, "");
    }
    else
    {
      setValue(GRAPHTYPE, oid);
    }
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
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
  
  public java.util.Date getLastUpdateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(LASTUPDATEDATE));
  }
  
  public void validateLastUpdateDate()
  {
    this.validateAttribute(LASTUPDATEDATE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF getLastUpdateDateMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF)mdClassIF.definesAttribute(LASTUPDATEDATE);
  }
  
  public com.runwaysdk.system.SingleActor getLastUpdatedBy()
  {
    if (getValue(LASTUPDATEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActor.get(getValue(LASTUPDATEDBY));
    }
  }
  
  public String getLastUpdatedByOid()
  {
    return getValue(LASTUPDATEDBY);
  }
  
  public void validateLastUpdatedBy()
  {
    this.validateAttribute(LASTUPDATEDBY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getLastUpdatedByMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(LASTUPDATEDBY);
  }
  
  public com.runwaysdk.system.SingleActor getLockedBy()
  {
    if (getValue(LOCKEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActor.get(getValue(LOCKEDBY));
    }
  }
  
  public String getLockedByOid()
  {
    return getValue(LOCKEDBY);
  }
  
  public void validateLockedBy()
  {
    this.validateAttribute(LOCKEDBY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getLockedByMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(LOCKEDBY);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public com.runwaysdk.system.Actor getOwner()
  {
    if (getValue(OWNER).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.Actor.get(getValue(OWNER));
    }
  }
  
  public String getOwnerOid()
  {
    return getValue(OWNER);
  }
  
  public void validateOwner()
  {
    this.validateAttribute(OWNER);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getOwnerMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(OWNER);
  }
  
  public void setOwner(com.runwaysdk.system.Actor value)
  {
    if(value == null)
    {
      setValue(OWNER, "");
    }
    else
    {
      setValue(OWNER, value.getOid());
    }
  }
  
  public void setOwnerId(java.lang.String oid)
  {
    if(oid == null)
    {
      setValue(OWNER, "");
    }
    else
    {
      setValue(OWNER, oid);
    }
  }
  
  public String getRemoteEntry()
  {
    return getValue(REMOTEENTRY);
  }
  
  public void validateRemoteEntry()
  {
    this.validateAttribute(REMOTEENTRY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getRemoteEntryMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(REMOTEENTRY);
  }
  
  public void setRemoteEntry(String value)
  {
    if(value == null)
    {
      setValue(REMOTEENTRY, "");
    }
    else
    {
      setValue(REMOTEENTRY, value);
    }
  }
  
  public String getRemoteType()
  {
    return getValue(REMOTETYPE);
  }
  
  public void validateRemoteType()
  {
    this.validateAttribute(REMOTETYPE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getRemoteTypeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(REMOTETYPE);
  }
  
  public void setRemoteType(String value)
  {
    if(value == null)
    {
      setValue(REMOTETYPE, "");
    }
    else
    {
      setValue(REMOTETYPE, value);
    }
  }
  
  public String getRemoteVersion()
  {
    return getValue(REMOTEVERSION);
  }
  
  public void validateRemoteVersion()
  {
    this.validateAttribute(REMOTEVERSION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getRemoteVersionMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(REMOTEVERSION);
  }
  
  public void setRemoteVersion(String value)
  {
    if(value == null)
    {
      setValue(REMOTEVERSION, "");
    }
    else
    {
      setValue(REMOTEVERSION, value);
    }
  }
  
  public Long getSeq()
  {
    return com.runwaysdk.constants.MdAttributeLongUtil.getTypeSafeValue(getValue(SEQ));
  }
  
  public void validateSeq()
  {
    this.validateAttribute(SEQ);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeLongDAOIF getSeqMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeLongDAOIF)mdClassIF.definesAttribute(SEQ);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(SITEMASTER);
  }
  
  public String getType()
  {
    return getValue(TYPE);
  }
  
  public void validateType()
  {
    this.validateAttribute(TYPE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getTypeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(TYPE);
  }
  
  public String getUrl()
  {
    return getValue(URL);
  }
  
  public void validateUrl()
  {
    this.validateAttribute(URL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getUrlMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(URL);
  }
  
  public void setUrl(String value)
  {
    if(value == null)
    {
      setValue(URL, "");
    }
    else
    {
      setValue(URL, value);
    }
  }
  
  public net.geoprism.graph.LabeledPropertyGraphTypeVersion getVersion()
  {
    if (getValue(VERSION).trim().equals(""))
    {
      return null;
    }
    else
    {
      return net.geoprism.graph.LabeledPropertyGraphTypeVersion.get(getValue(VERSION));
    }
  }
  
  public String getVersionOid()
  {
    return getValue(VERSION);
  }
  
  public void validateVersion()
  {
    this.validateAttribute(VERSION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getVersionMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(VERSION);
  }
  
  public void setVersion(net.geoprism.graph.LabeledPropertyGraphTypeVersion value)
  {
    if(value == null)
    {
      setValue(VERSION, "");
    }
    else
    {
      setValue(VERSION, value.getOid());
    }
  }
  
  public void setVersionId(java.lang.String oid)
  {
    if(oid == null)
    {
      setValue(VERSION, "");
    }
    else
    {
      setValue(VERSION, oid);
    }
  }
  
  public Integer getVersionNumber()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(VERSIONNUMBER));
  }
  
  public void validateVersionNumber()
  {
    this.validateAttribute(VERSIONNUMBER);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF getVersionNumberMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.LabeledPropertyGraphSynchronization.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF)mdClassIF.definesAttribute(VERSIONNUMBER);
  }
  
  public void setVersionNumber(Integer value)
  {
    if(value == null)
    {
      setValue(VERSIONNUMBER, "");
    }
    else
    {
      setValue(VERSIONNUMBER, java.lang.Integer.toString(value));
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static LabeledPropertyGraphSynchronization get(String oid)
  {
    return (LabeledPropertyGraphSynchronization) com.runwaysdk.business.Business.get(oid);
  }
  
  public static LabeledPropertyGraphSynchronization getByKey(String key)
  {
    return (LabeledPropertyGraphSynchronization) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static LabeledPropertyGraphSynchronization lock(java.lang.String oid)
  {
    LabeledPropertyGraphSynchronization _instance = LabeledPropertyGraphSynchronization.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static LabeledPropertyGraphSynchronization unlock(java.lang.String oid)
  {
    LabeledPropertyGraphSynchronization _instance = LabeledPropertyGraphSynchronization.get(oid);
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
