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

@com.runwaysdk.business.ClassSignature(hash = 1885032645)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to GeoObjectTypeSnapshot.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class GeoObjectTypeSnapshotBase extends com.runwaysdk.business.Business
{
  public final static String CLASS = "net.geoprism.graph.GeoObjectTypeSnapshot";
  public final static java.lang.String CODE = "code";
  public final static java.lang.String CREATEDATE = "createDate";
  public final static java.lang.String CREATEDBY = "createdBy";
  public final static java.lang.String DESCRIPTION = "description";
  private com.runwaysdk.business.Struct description = null;
  
  public final static java.lang.String DISPLAYLABEL = "displayLabel";
  private com.runwaysdk.business.Struct displayLabel = null;
  
  public final static java.lang.String ENTITYDOMAIN = "entityDomain";
  public final static java.lang.String GEOMETRYTYPE = "geometryType";
  public final static java.lang.String GRAPHMDVERTEX = "graphMdVertex";
  public final static java.lang.String ISABSTRACT = "isAbstract";
  public final static java.lang.String ISGEOMETRYEDITABLE = "isGeometryEditable";
  public final static java.lang.String ISPRIVATE = "isPrivate";
  public final static java.lang.String ISROOT = "isRoot";
  public final static java.lang.String KEYNAME = "keyName";
  public final static java.lang.String LASTUPDATEDATE = "lastUpdateDate";
  public final static java.lang.String LASTUPDATEDBY = "lastUpdatedBy";
  public final static java.lang.String LOCKEDBY = "lockedBy";
  public final static java.lang.String OID = "oid";
  public final static java.lang.String ORGCODE = "orgCode";
  public final static java.lang.String OWNER = "owner";
  public final static java.lang.String PARENT = "parent";
  public final static java.lang.String SEQ = "seq";
  public final static java.lang.String SITEMASTER = "siteMaster";
  public final static java.lang.String TYPE = "type";
  public final static java.lang.String VERSION = "version";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1885032645;
  
  public GeoObjectTypeSnapshotBase()
  {
    super();
    description = super.getStruct("description");
    displayLabel = super.getStruct("displayLabel");
  }
  
  public String getCode()
  {
    return getValue(CODE);
  }
  
  public void validateCode()
  {
    this.validateAttribute(CODE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getCodeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(CODE);
  }
  
  public void setCode(String value)
  {
    if(value == null)
    {
      setValue(CODE, "");
    }
    else
    {
      setValue(CODE, value);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(CREATEDBY);
  }
  
  public net.geoprism.graph.GeoObjectTypeSnapshotDescription getDescription()
  {
    return (net.geoprism.graph.GeoObjectTypeSnapshotDescription) description;
  }
  
  public void validateDescription()
  {
    this.validateAttribute(DESCRIPTION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeLocalTextDAOIF getDescriptionMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeLocalTextDAOIF)mdClassIF.definesAttribute(DESCRIPTION);
  }
  
  public net.geoprism.graph.GeoObjectTypeSnapshotDisplayLabel getDisplayLabel()
  {
    return (net.geoprism.graph.GeoObjectTypeSnapshotDisplayLabel) displayLabel;
  }
  
  public void validateDisplayLabel()
  {
    this.validateAttribute(DISPLAYLABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF getDisplayLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
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
  
  public String getGeometryType()
  {
    return getValue(GEOMETRYTYPE);
  }
  
  public void validateGeometryType()
  {
    this.validateAttribute(GEOMETRYTYPE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getGeometryTypeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(GEOMETRYTYPE);
  }
  
  public void setGeometryType(String value)
  {
    if(value == null)
    {
      setValue(GEOMETRYTYPE, "");
    }
    else
    {
      setValue(GEOMETRYTYPE, value);
    }
  }
  
  public com.runwaysdk.system.metadata.MdVertex getGraphMdVertex()
  {
    if (getValue(GRAPHMDVERTEX).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdVertex.get(getValue(GRAPHMDVERTEX));
    }
  }
  
  public String getGraphMdVertexOid()
  {
    return getValue(GRAPHMDVERTEX);
  }
  
  public void validateGraphMdVertex()
  {
    this.validateAttribute(GRAPHMDVERTEX);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getGraphMdVertexMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(GRAPHMDVERTEX);
  }
  
  public void setGraphMdVertex(com.runwaysdk.system.metadata.MdVertex value)
  {
    if(value == null)
    {
      setValue(GRAPHMDVERTEX, "");
    }
    else
    {
      setValue(GRAPHMDVERTEX, value.getOid());
    }
  }
  
  public void setGraphMdVertexId(java.lang.String oid)
  {
    if(oid == null)
    {
      setValue(GRAPHMDVERTEX, "");
    }
    else
    {
      setValue(GRAPHMDVERTEX, oid);
    }
  }
  
  public Boolean getIsAbstract()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ISABSTRACT));
  }
  
  public void validateIsAbstract()
  {
    this.validateAttribute(ISABSTRACT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF getIsAbstractMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF)mdClassIF.definesAttribute(ISABSTRACT);
  }
  
  public void setIsAbstract(Boolean value)
  {
    if(value == null)
    {
      setValue(ISABSTRACT, "");
    }
    else
    {
      setValue(ISABSTRACT, java.lang.Boolean.toString(value));
    }
  }
  
  public Boolean getIsGeometryEditable()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ISGEOMETRYEDITABLE));
  }
  
  public void validateIsGeometryEditable()
  {
    this.validateAttribute(ISGEOMETRYEDITABLE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF getIsGeometryEditableMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF)mdClassIF.definesAttribute(ISGEOMETRYEDITABLE);
  }
  
  public void setIsGeometryEditable(Boolean value)
  {
    if(value == null)
    {
      setValue(ISGEOMETRYEDITABLE, "");
    }
    else
    {
      setValue(ISGEOMETRYEDITABLE, java.lang.Boolean.toString(value));
    }
  }
  
  public Boolean getIsPrivate()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ISPRIVATE));
  }
  
  public void validateIsPrivate()
  {
    this.validateAttribute(ISPRIVATE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF getIsPrivateMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF)mdClassIF.definesAttribute(ISPRIVATE);
  }
  
  public void setIsPrivate(Boolean value)
  {
    if(value == null)
    {
      setValue(ISPRIVATE, "");
    }
    else
    {
      setValue(ISPRIVATE, java.lang.Boolean.toString(value));
    }
  }
  
  public Boolean getIsRoot()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ISROOT));
  }
  
  public void validateIsRoot()
  {
    this.validateAttribute(ISROOT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF getIsRootMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF)mdClassIF.definesAttribute(ISROOT);
  }
  
  public void setIsRoot(Boolean value)
  {
    if(value == null)
    {
      setValue(ISROOT, "");
    }
    else
    {
      setValue(ISROOT, java.lang.Boolean.toString(value));
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeUUIDDAOIF)mdClassIF.definesAttribute(OID);
  }
  
  public String getOrgCode()
  {
    return getValue(ORGCODE);
  }
  
  public void validateOrgCode()
  {
    this.validateAttribute(ORGCODE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeTextDAOIF getOrgCodeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeTextDAOIF)mdClassIF.definesAttribute(ORGCODE);
  }
  
  public void setOrgCode(String value)
  {
    if(value == null)
    {
      setValue(ORGCODE, "");
    }
    else
    {
      setValue(ORGCODE, value);
    }
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
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
  
  public net.geoprism.graph.GeoObjectTypeSnapshot getParent()
  {
    if (getValue(PARENT).trim().equals(""))
    {
      return null;
    }
    else
    {
      return net.geoprism.graph.GeoObjectTypeSnapshot.get(getValue(PARENT));
    }
  }
  
  public String getParentOid()
  {
    return getValue(PARENT);
  }
  
  public void validateParent()
  {
    this.validateAttribute(PARENT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getParentMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(PARENT);
  }
  
  public void setParent(net.geoprism.graph.GeoObjectTypeSnapshot value)
  {
    if(value == null)
    {
      setValue(PARENT, "");
    }
    else
    {
      setValue(PARENT, value.getOid());
    }
  }
  
  public void setParentId(java.lang.String oid)
  {
    if(oid == null)
    {
      setValue(PARENT, "");
    }
    else
    {
      setValue(PARENT, oid);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(TYPE);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(net.geoprism.graph.GeoObjectTypeSnapshot.CLASS);
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
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static GeoObjectTypeSnapshotQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    GeoObjectTypeSnapshotQuery query = new GeoObjectTypeSnapshotQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
  }
  
  public net.geoprism.graph.SnapshotHierarchy addChildSnapshot(net.geoprism.graph.GeoObjectTypeSnapshot geoObjectTypeSnapshot)
  {
    return (net.geoprism.graph.SnapshotHierarchy) addChild(geoObjectTypeSnapshot, net.geoprism.graph.SnapshotHierarchy.CLASS);
  }
  
  public void removeChildSnapshot(net.geoprism.graph.GeoObjectTypeSnapshot geoObjectTypeSnapshot)
  {
    removeAllChildren(geoObjectTypeSnapshot, net.geoprism.graph.SnapshotHierarchy.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends net.geoprism.graph.GeoObjectTypeSnapshot> getAllChildSnapshot()
  {
    return (com.runwaysdk.query.OIterator<? extends net.geoprism.graph.GeoObjectTypeSnapshot>) getChildren(net.geoprism.graph.SnapshotHierarchy.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends net.geoprism.graph.SnapshotHierarchy> getAllChildSnapshotRel()
  {
    return (com.runwaysdk.query.OIterator<? extends net.geoprism.graph.SnapshotHierarchy>) getChildRelationships(net.geoprism.graph.SnapshotHierarchy.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends net.geoprism.graph.SnapshotHierarchy> getChildSnapshotRel(net.geoprism.graph.GeoObjectTypeSnapshot geoObjectTypeSnapshot)
  {
    return (com.runwaysdk.query.OIterator<? extends net.geoprism.graph.SnapshotHierarchy>) getRelationshipsWithChild(geoObjectTypeSnapshot, net.geoprism.graph.SnapshotHierarchy.CLASS);
  }
  
  public net.geoprism.graph.SnapshotHierarchy addParentSnapshot(net.geoprism.graph.GeoObjectTypeSnapshot geoObjectTypeSnapshot)
  {
    return (net.geoprism.graph.SnapshotHierarchy) addParent(geoObjectTypeSnapshot, net.geoprism.graph.SnapshotHierarchy.CLASS);
  }
  
  public void removeParentSnapshot(net.geoprism.graph.GeoObjectTypeSnapshot geoObjectTypeSnapshot)
  {
    removeAllParents(geoObjectTypeSnapshot, net.geoprism.graph.SnapshotHierarchy.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends net.geoprism.graph.GeoObjectTypeSnapshot> getAllParentSnapshot()
  {
    return (com.runwaysdk.query.OIterator<? extends net.geoprism.graph.GeoObjectTypeSnapshot>) getParents(net.geoprism.graph.SnapshotHierarchy.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends net.geoprism.graph.SnapshotHierarchy> getAllParentSnapshotRel()
  {
    return (com.runwaysdk.query.OIterator<? extends net.geoprism.graph.SnapshotHierarchy>) getParentRelationships(net.geoprism.graph.SnapshotHierarchy.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends net.geoprism.graph.SnapshotHierarchy> getParentSnapshotRel(net.geoprism.graph.GeoObjectTypeSnapshot geoObjectTypeSnapshot)
  {
    return (com.runwaysdk.query.OIterator<? extends net.geoprism.graph.SnapshotHierarchy>) getRelationshipsWithParent(geoObjectTypeSnapshot, net.geoprism.graph.SnapshotHierarchy.CLASS);
  }
  
  public static GeoObjectTypeSnapshot get(String oid)
  {
    return (GeoObjectTypeSnapshot) com.runwaysdk.business.Business.get(oid);
  }
  
  public static GeoObjectTypeSnapshot getByKey(String key)
  {
    return (GeoObjectTypeSnapshot) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static GeoObjectTypeSnapshot lock(java.lang.String oid)
  {
    GeoObjectTypeSnapshot _instance = GeoObjectTypeSnapshot.get(oid);
    _instance.lock();
    
    return _instance;
  }
  
  public static GeoObjectTypeSnapshot unlock(java.lang.String oid)
  {
    GeoObjectTypeSnapshot _instance = GeoObjectTypeSnapshot.get(oid);
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
