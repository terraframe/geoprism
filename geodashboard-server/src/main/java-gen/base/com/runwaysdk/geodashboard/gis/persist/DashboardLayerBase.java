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
package com.runwaysdk.geodashboard.gis.persist;

@com.runwaysdk.business.ClassSignature(hash = 1319696710)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to DashboardLayer.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class DashboardLayerBase extends com.runwaysdk.business.Business implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.gis.persist.DashboardLayer";
  public static java.lang.String BBOXINCLUDED = "BBoxIncluded";
  public static java.lang.String ACTIVEBYDEFAULT = "activeByDefault";
  public static java.lang.String CREATEDATE = "createDate";
  public static java.lang.String CREATEDBY = "createdBy";
  public static java.lang.String DASHBOARDLEGEND = "dashboardLegend";
  private com.runwaysdk.business.Struct dashboardLegend = null;
  
  public static java.lang.String DASHBOARDMAP = "dashboardMap";
  public static java.lang.String DISPLAYINLEGEND = "displayInLegend";
  public static java.lang.String ENTITYDOMAIN = "entityDomain";
  public static java.lang.String ID = "id";
  public static java.lang.String KEYNAME = "keyName";
  public static java.lang.String LASTPUBLISHDATE = "lastPublishDate";
  public static java.lang.String LASTUPDATEDATE = "lastUpdateDate";
  public static java.lang.String LASTUPDATEDBY = "lastUpdatedBy";
  public static java.lang.String LAYERENABLED = "layerEnabled";
  public static java.lang.String LAYERTYPE = "layerType";
  public static java.lang.String LOCKEDBY = "lockedBy";
  public static java.lang.String NAMELABEL = "nameLabel";
  private com.runwaysdk.business.Struct nameLabel = null;
  
  public static java.lang.String OWNER = "owner";
  public static java.lang.String SEQ = "seq";
  public static java.lang.String SITEMASTER = "siteMaster";
  public static java.lang.String TYPE = "type";
  public static java.lang.String VIEWNAME = "viewName";
  public static java.lang.String VIRTUAL = "virtual";
  private static final long serialVersionUID = 1319696710;
  
  public DashboardLayerBase()
  {
    super();
    dashboardLegend = super.getStruct("dashboardLegend");
    nameLabel = super.getStruct("nameLabel");
  }
  
  public Boolean getBBoxIncluded()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(BBOXINCLUDED));
  }
  
  public void validateBBoxIncluded()
  {
    this.validateAttribute(BBOXINCLUDED);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF getBBoxIncludedMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF)mdClassIF.definesAttribute(BBOXINCLUDED);
  }
  
  public void setBBoxIncluded(Boolean value)
  {
    if(value == null)
    {
      setValue(BBOXINCLUDED, "");
    }
    else
    {
      setValue(BBOXINCLUDED, java.lang.Boolean.toString(value));
    }
  }
  
  public Boolean getActiveByDefault()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ACTIVEBYDEFAULT));
  }
  
  public void validateActiveByDefault()
  {
    this.validateAttribute(ACTIVEBYDEFAULT);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF getActiveByDefaultMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF)mdClassIF.definesAttribute(ACTIVEBYDEFAULT);
  }
  
  public void setActiveByDefault(Boolean value)
  {
    if(value == null)
    {
      setValue(ACTIVEBYDEFAULT, "");
    }
    else
    {
      setValue(ACTIVEBYDEFAULT, java.lang.Boolean.toString(value));
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
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
  
  public String getCreatedById()
  {
    return getValue(CREATEDBY);
  }
  
  public void validateCreatedBy()
  {
    this.validateAttribute(CREATEDBY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getCreatedByMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(CREATEDBY);
  }
  
  public com.runwaysdk.geodashboard.gis.persist.DashboardLegend getDashboardLegend()
  {
    return (com.runwaysdk.geodashboard.gis.persist.DashboardLegend) dashboardLegend;
  }
  
  public void validateDashboardLegend()
  {
    this.validateAttribute(DASHBOARDLEGEND);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeStructDAOIF getDashboardLegendMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeStructDAOIF)mdClassIF.definesAttribute(DASHBOARDLEGEND);
  }
  
  public com.runwaysdk.geodashboard.gis.persist.DashboardMap getDashboardMap()
  {
    if (getValue(DASHBOARDMAP).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.geodashboard.gis.persist.DashboardMap.get(getValue(DASHBOARDMAP));
    }
  }
  
  public String getDashboardMapId()
  {
    return getValue(DASHBOARDMAP);
  }
  
  public void validateDashboardMap()
  {
    this.validateAttribute(DASHBOARDMAP);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getDashboardMapMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(DASHBOARDMAP);
  }
  
  public void setDashboardMap(com.runwaysdk.geodashboard.gis.persist.DashboardMap value)
  {
    if(value == null)
    {
      setValue(DASHBOARDMAP, "");
    }
    else
    {
      setValue(DASHBOARDMAP, value.getId());
    }
  }
  
  public Boolean getDisplayInLegend()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(DISPLAYINLEGEND));
  }
  
  public void validateDisplayInLegend()
  {
    this.validateAttribute(DISPLAYINLEGEND);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF getDisplayInLegendMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF)mdClassIF.definesAttribute(DISPLAYINLEGEND);
  }
  
  public void setDisplayInLegend(Boolean value)
  {
    if(value == null)
    {
      setValue(DISPLAYINLEGEND, "");
    }
    else
    {
      setValue(DISPLAYINLEGEND, java.lang.Boolean.toString(value));
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
  
  public String getEntityDomainId()
  {
    return getValue(ENTITYDOMAIN);
  }
  
  public void validateEntityDomain()
  {
    this.validateAttribute(ENTITYDOMAIN);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getEntityDomainMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
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
      setValue(ENTITYDOMAIN, value.getId());
    }
  }
  
  public String getId()
  {
    return getValue(ID);
  }
  
  public void validateId()
  {
    this.validateAttribute(ID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getIdMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(ID);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
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
  
  public java.util.Date getLastPublishDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(LASTPUBLISHDATE));
  }
  
  public void validateLastPublishDate()
  {
    this.validateAttribute(LASTPUBLISHDATE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF getLastPublishDateMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF)mdClassIF.definesAttribute(LASTPUBLISHDATE);
  }
  
  public void setLastPublishDate(java.util.Date value)
  {
    if(value == null)
    {
      setValue(LASTPUBLISHDATE, "");
    }
    else
    {
      setValue(LASTPUBLISHDATE, new java.text.SimpleDateFormat(com.runwaysdk.constants.Constants.DATETIME_FORMAT).format(value));
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
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
  
  public String getLastUpdatedById()
  {
    return getValue(LASTUPDATEDBY);
  }
  
  public void validateLastUpdatedBy()
  {
    this.validateAttribute(LASTUPDATEDBY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getLastUpdatedByMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(LASTUPDATEDBY);
  }
  
  public Boolean getLayerEnabled()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(LAYERENABLED));
  }
  
  public void validateLayerEnabled()
  {
    this.validateAttribute(LAYERENABLED);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF getLayerEnabledMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF)mdClassIF.definesAttribute(LAYERENABLED);
  }
  
  public void setLayerEnabled(Boolean value)
  {
    if(value == null)
    {
      setValue(LAYERENABLED, "");
    }
    else
    {
      setValue(LAYERENABLED, java.lang.Boolean.toString(value));
    }
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.geodashboard.gis.persist.AllLayerType> getLayerType()
  {
    return (java.util.List<com.runwaysdk.geodashboard.gis.persist.AllLayerType>) getEnumValues(LAYERTYPE);
  }
  
  public void addLayerType(com.runwaysdk.geodashboard.gis.persist.AllLayerType value)
  {
    if(value != null)
    {
      addEnumItem(LAYERTYPE, value.getId());
    }
  }
  
  public void removeLayerType(com.runwaysdk.geodashboard.gis.persist.AllLayerType value)
  {
    if(value != null)
    {
      removeEnumItem(LAYERTYPE, value.getId());
    }
  }
  
  public void clearLayerType()
  {
    clearEnum(LAYERTYPE);
  }
  
  public void validateLayerType()
  {
    this.validateAttribute(LAYERTYPE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF getLayerTypeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF)mdClassIF.definesAttribute(LAYERTYPE);
  }
  
  public com.runwaysdk.system.Users getLockedBy()
  {
    if (getValue(LOCKEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.Users.get(getValue(LOCKEDBY));
    }
  }
  
  public String getLockedById()
  {
    return getValue(LOCKEDBY);
  }
  
  public void validateLockedBy()
  {
    this.validateAttribute(LOCKEDBY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getLockedByMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(LOCKEDBY);
  }
  
  public com.runwaysdk.geodashboard.gis.persist.DashboardLayerNameLabel getNameLabel()
  {
    return (com.runwaysdk.geodashboard.gis.persist.DashboardLayerNameLabel) nameLabel;
  }
  
  public void validateNameLabel()
  {
    this.validateAttribute(NAMELABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF getNameLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF)mdClassIF.definesAttribute(NAMELABEL);
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
  
  public String getOwnerId()
  {
    return getValue(OWNER);
  }
  
  public void validateOwner()
  {
    this.validateAttribute(OWNER);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getOwnerMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
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
      setValue(OWNER, value.getId());
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
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
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(TYPE);
  }
  
  public String getViewName()
  {
    return getValue(VIEWNAME);
  }
  
  public void validateViewName()
  {
    this.validateAttribute(VIEWNAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getViewNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(VIEWNAME);
  }
  
  public void setViewName(String value)
  {
    if(value == null)
    {
      setValue(VIEWNAME, "");
    }
    else
    {
      setValue(VIEWNAME, value);
    }
  }
  
  public Boolean getVirtual()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(VIRTUAL));
  }
  
  public void validateVirtual()
  {
    this.validateAttribute(VIRTUAL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF getVirtualMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.geodashboard.gis.persist.DashboardLayer.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF)mdClassIF.definesAttribute(VIRTUAL);
  }
  
  public void setVirtual(Boolean value)
  {
    if(value == null)
    {
      setValue(VIRTUAL, "");
    }
    else
    {
      setValue(VIRTUAL, java.lang.Boolean.toString(value));
    }
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static DashboardLayerQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    DashboardLayerQuery query = new DashboardLayerQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
  }
  
  public com.runwaysdk.geodashboard.gis.persist.HasStyle addHasStyle(com.runwaysdk.geodashboard.gis.persist.DashboardStyle dashboardStyle)
  {
    return (com.runwaysdk.geodashboard.gis.persist.HasStyle) addChild(dashboardStyle, com.runwaysdk.geodashboard.gis.persist.HasStyle.CLASS);
  }
  
  public void removeHasStyle(com.runwaysdk.geodashboard.gis.persist.DashboardStyle dashboardStyle)
  {
    removeAllChildren(dashboardStyle, com.runwaysdk.geodashboard.gis.persist.HasStyle.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.geodashboard.gis.persist.DashboardStyle> getAllHasStyle()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.geodashboard.gis.persist.DashboardStyle>) getChildren(com.runwaysdk.geodashboard.gis.persist.HasStyle.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.geodashboard.gis.persist.HasStyle> getAllHasStyleRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.geodashboard.gis.persist.HasStyle>) getChildRelationships(com.runwaysdk.geodashboard.gis.persist.HasStyle.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.geodashboard.gis.persist.HasStyle> getHasStyleRel(com.runwaysdk.geodashboard.gis.persist.DashboardStyle dashboardStyle)
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.geodashboard.gis.persist.HasStyle>) getRelationshipsWithChild(dashboardStyle, com.runwaysdk.geodashboard.gis.persist.HasStyle.CLASS);
  }
  
  public com.runwaysdk.geodashboard.gis.persist.HasLayer addContainingMap(com.runwaysdk.geodashboard.gis.persist.DashboardMap dashboardMap)
  {
    return (com.runwaysdk.geodashboard.gis.persist.HasLayer) addParent(dashboardMap, com.runwaysdk.geodashboard.gis.persist.HasLayer.CLASS);
  }
  
  public void removeContainingMap(com.runwaysdk.geodashboard.gis.persist.DashboardMap dashboardMap)
  {
    removeAllParents(dashboardMap, com.runwaysdk.geodashboard.gis.persist.HasLayer.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.geodashboard.gis.persist.DashboardMap> getAllContainingMap()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.geodashboard.gis.persist.DashboardMap>) getParents(com.runwaysdk.geodashboard.gis.persist.HasLayer.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.geodashboard.gis.persist.HasLayer> getAllContainingMapRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.geodashboard.gis.persist.HasLayer>) getParentRelationships(com.runwaysdk.geodashboard.gis.persist.HasLayer.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.geodashboard.gis.persist.HasLayer> getContainingMapRel(com.runwaysdk.geodashboard.gis.persist.DashboardMap dashboardMap)
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.geodashboard.gis.persist.HasLayer>) getRelationshipsWithParent(dashboardMap, com.runwaysdk.geodashboard.gis.persist.HasLayer.CLASS);
  }
  
  public static DashboardLayer get(String id)
  {
    return (DashboardLayer) com.runwaysdk.business.Business.get(id);
  }
  
  public static DashboardLayer getByKey(String key)
  {
    return (DashboardLayer) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public java.lang.String applyWithStyle(com.runwaysdk.geodashboard.gis.persist.DashboardStyle style, java.lang.String mapId, java.lang.String state)
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.geodashboard.gis.persist.DashboardLayer.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static final java.lang.String applyWithStyle(java.lang.String id, com.runwaysdk.geodashboard.gis.persist.DashboardStyle style, java.lang.String mapId, java.lang.String state)
  {
    DashboardLayer _instance = DashboardLayer.get(id);
    return _instance.applyWithStyle(style, mapId, state);
  }
  
  public java.lang.String getJSON()
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.geodashboard.gis.persist.DashboardLayer.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static final java.lang.String getJSON(java.lang.String id)
  {
    DashboardLayer _instance = DashboardLayer.get(id);
    return _instance.getJSON();
  }
  
  public static com.runwaysdk.system.gis.geo.UniversalQuery getSortedUniversals()
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.geodashboard.gis.persist.DashboardLayer.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public void updateLegend(java.lang.Integer legendXPosition, java.lang.Integer legendYPosition, java.lang.Boolean groupedInLegend)
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.geodashboard.gis.persist.DashboardLayer.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static final void updateLegend(java.lang.String id, java.lang.Integer legendXPosition, java.lang.Integer legendYPosition, java.lang.Boolean groupedInLegend)
  {
    DashboardLayer _instance = DashboardLayer.get(id);
    _instance.updateLegend(legendXPosition, legendYPosition, groupedInLegend);
  }
  
  public static DashboardLayer lock(java.lang.String id)
  {
    DashboardLayer _instance = DashboardLayer.get(id);
    _instance.lock();
    
    return _instance;
  }
  
  public static DashboardLayer unlock(java.lang.String id)
  {
    DashboardLayer _instance = DashboardLayer.get(id);
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
