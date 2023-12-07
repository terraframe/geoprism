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
package net.geoprism.registry;

@com.runwaysdk.business.ClassSignature(hash = 1238642493)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to HierarchyMetadata.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class HierarchyMetadataBase extends com.runwaysdk.business.Business
{
  public final static String CLASS = "net.geoprism.registry.HierarchyMetadata";
  public static final java.lang.String ABSTRACTDESCRIPTION = "abstractDescription";
  public static final java.lang.String ACCESSCONSTRAINTS = "accessConstraints";
  public static final java.lang.String ACKNOWLEDGEMENT = "acknowledgement";
  public static final java.lang.String CONTACT = "contact";
  public static final java.lang.String CREATEDATE = "createDate";
  public static final java.lang.String CREATEDBY = "createdBy";
  public static final java.lang.String DISCLAIMER = "disclaimer";
  public static final java.lang.String EMAIL = "email";
  public static final java.lang.String ENTITYDOMAIN = "entityDomain";
  public static final java.lang.String KEYNAME = "keyName";
  public static final java.lang.String LASTUPDATEDATE = "lastUpdateDate";
  public static final java.lang.String LASTUPDATEDBY = "lastUpdatedBy";
  public static final java.lang.String LOCKEDBY = "lockedBy";
  public static final java.lang.String MDTERMRELATIONSHIP = "mdTermRelationship";
  public static final java.lang.String OID = "oid";
  public static final java.lang.String OWNER = "owner";
  public static final java.lang.String PHONENUMBER = "phoneNumber";
  public static final java.lang.String PROGRESS = "progress";
  public static final java.lang.String SEQ = "seq";
  public static final java.lang.String SITEMASTER = "siteMaster";
  public static final java.lang.String TYPE = "type";
  public static final java.lang.String USECONSTRAINTS = "useConstraints";
  private static final long serialVersionUID = 1238642493;
  
  public HierarchyMetadataBase()
  {
    super();
  }
  
  public String getAbstractDescription()
  {
    return getValue(ABSTRACTDESCRIPTION);
  }
  
  public void validateAbstractDescription()
  {
    this.validateAttribute(ABSTRACTDESCRIPTION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getAbstractDescriptionMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(ABSTRACTDESCRIPTION);
  }
  
  public void setAbstractDescription(String value)
  {
    if(value == null)
    {
      setValue(ABSTRACTDESCRIPTION, "");
    }
    else
    {
      setValue(ABSTRACTDESCRIPTION, value);
    }
  }
  
  public String getAccessConstraints()
  {
    return getValue(ACCESSCONSTRAINTS);
  }
  
  public void validateAccessConstraints()
  {
    this.validateAttribute(ACCESSCONSTRAINTS);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getAccessConstraintsMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(ACCESSCONSTRAINTS);
  }
  
  public void setAccessConstraints(String value)
  {
    if(value == null)
    {
      setValue(ACCESSCONSTRAINTS, "");
    }
    else
    {
      setValue(ACCESSCONSTRAINTS, value);
    }
  }
  
  public String getAcknowledgement()
  {
    return getValue(ACKNOWLEDGEMENT);
  }
  
  public void validateAcknowledgement()
  {
    this.validateAttribute(ACKNOWLEDGEMENT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getAcknowledgementMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(ACKNOWLEDGEMENT);
  }
  
  public void setAcknowledgement(String value)
  {
    if(value == null)
    {
      setValue(ACKNOWLEDGEMENT, "");
    }
    else
    {
      setValue(ACKNOWLEDGEMENT, value);
    }
  }
  
  public String getContact()
  {
    return getValue(CONTACT);
  }
  
  public void validateContact()
  {
    this.validateAttribute(CONTACT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getContactMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(CONTACT);
  }
  
  public void setContact(String value)
  {
    if(value == null)
    {
      setValue(CONTACT, "");
    }
    else
    {
      setValue(CONTACT, value);
    }
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(CREATEDBY);
  }
  
  public String getDisclaimer()
  {
    return getValue(DISCLAIMER);
  }
  
  public void validateDisclaimer()
  {
    this.validateAttribute(DISCLAIMER);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getDisclaimerMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(DISCLAIMER);
  }
  
  public void setDisclaimer(String value)
  {
    if(value == null)
    {
      setValue(DISCLAIMER, "");
    }
    else
    {
      setValue(DISCLAIMER, value);
    }
  }
  
  public String getEmail()
  {
    return getValue(EMAIL);
  }
  
  public void validateEmail()
  {
    this.validateAttribute(EMAIL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getEmailMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(EMAIL);
  }
  
  public void setEmail(String value)
  {
    if(value == null)
    {
      setValue(EMAIL, "");
    }
    else
    {
      setValue(EMAIL, value);
    }
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(LOCKEDBY);
  }
  
  public com.runwaysdk.system.metadata.MdTermRelationship getMdTermRelationship()
  {
    if (getValue(MDTERMRELATIONSHIP).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdTermRelationship.get(getValue(MDTERMRELATIONSHIP));
    }
  }
  
  public String getMdTermRelationshipOid()
  {
    return getValue(MDTERMRELATIONSHIP);
  }
  
  public void validateMdTermRelationship()
  {
    this.validateAttribute(MDTERMRELATIONSHIP);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getMdTermRelationshipMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(MDTERMRELATIONSHIP);
  }
  
  public void setMdTermRelationship(com.runwaysdk.system.metadata.MdTermRelationship value)
  {
    if(value == null)
    {
      setValue(MDTERMRELATIONSHIP, "");
    }
    else
    {
      setValue(MDTERMRELATIONSHIP, value.getOid());
    }
  }
  
  public void setMdTermRelationshipId(java.lang.String oid)
  {
    if(oid == null)
    {
      setValue(MDTERMRELATIONSHIP, "");
    }
    else
    {
      setValue(MDTERMRELATIONSHIP, oid);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
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
  
  public String getPhoneNumber()
  {
    return getValue(PHONENUMBER);
  }
  
  public void validatePhoneNumber()
  {
    this.validateAttribute(PHONENUMBER);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getPhoneNumberMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(PHONENUMBER);
  }
  
  public void setPhoneNumber(String value)
  {
    if(value == null)
    {
      setValue(PHONENUMBER, "");
    }
    else
    {
      setValue(PHONENUMBER, value);
    }
  }
  
  public String getProgress()
  {
    return getValue(PROGRESS);
  }
  
  public void validateProgress()
  {
    this.validateAttribute(PROGRESS);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getProgressMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(PROGRESS);
  }
  
  public void setProgress(String value)
  {
    if(value == null)
    {
      setValue(PROGRESS, "");
    }
    else
    {
      setValue(PROGRESS, value);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(TYPE);
  }
  
  public String getUseConstraints()
  {
    return getValue(USECONSTRAINTS);
  }
  
  public void validateUseConstraints()
  {
    this.validateAttribute(USECONSTRAINTS);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getUseConstraintsMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.registry.HierarchyMetadata.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(USECONSTRAINTS);
  }
  
  public void setUseConstraints(String value)
  {
    if(value == null)
    {
      setValue(USECONSTRAINTS, "");
    }
    else
    {
      setValue(USECONSTRAINTS, value);
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static HierarchyMetadataQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    HierarchyMetadataQuery query = new HierarchyMetadataQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
  }
  
  public static HierarchyMetadata get(String oid)
  {
    return (HierarchyMetadata) com.runwaysdk.business.Business.get(oid);
  }
  
  public static HierarchyMetadata getByKey(String key)
  {
    return (HierarchyMetadata) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static HierarchyMetadata lock(java.lang.String oid)
  {
    HierarchyMetadata _instance = HierarchyMetadata.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static HierarchyMetadata unlock(java.lang.String oid)
  {
    HierarchyMetadata _instance = HierarchyMetadata.get(oid);
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
