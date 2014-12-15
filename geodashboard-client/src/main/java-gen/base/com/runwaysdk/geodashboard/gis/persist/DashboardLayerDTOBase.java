package com.runwaysdk.geodashboard.gis.persist;

@com.runwaysdk.business.ClassSignature(hash = 1822089257)
public abstract class DashboardLayerDTOBase extends com.runwaysdk.business.BusinessDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.gis.persist.DashboardLayer";
  private static final long serialVersionUID = 1822089257;
  
  protected DashboardLayerDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected DashboardLayerDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String BBOXINCLUDED = "BBoxIncluded";
  public static java.lang.String ACTIVEBYDEFAULT = "activeByDefault";
  public static java.lang.String CREATEDATE = "createDate";
  public static java.lang.String CREATEDBY = "createdBy";
  public static java.lang.String DASHBOARDLEGEND = "dashboardLegend";
  public static java.lang.String DISPLAYINLEGEND = "displayInLegend";
  public static java.lang.String ENTITYDOMAIN = "entityDomain";
  public static java.lang.String GEOENTITY = "geoEntity";
  public static java.lang.String ID = "id";
  public static java.lang.String KEYNAME = "keyName";
  public static java.lang.String LASTPUBLISHDATE = "lastPublishDate";
  public static java.lang.String LASTUPDATEDATE = "lastUpdateDate";
  public static java.lang.String LASTUPDATEDBY = "lastUpdatedBy";
  public static java.lang.String LAYERENABLED = "layerEnabled";
  public static java.lang.String LAYERTYPE = "layerType";
  public static java.lang.String LOCKEDBY = "lockedBy";
  public static java.lang.String NAME = "name";
  public static java.lang.String OWNER = "owner";
  public static java.lang.String SEQ = "seq";
  public static java.lang.String SITEMASTER = "siteMaster";
  public static java.lang.String TYPE = "type";
  public static java.lang.String UNIVERSAL = "universal";
  public static java.lang.String VIEWNAME = "viewName";
  public static java.lang.String VIRTUAL = "virtual";
  public Boolean getBBoxIncluded()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(BBOXINCLUDED));
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
  
  public boolean isBBoxIncludedWritable()
  {
    return isWritable(BBOXINCLUDED);
  }
  
  public boolean isBBoxIncludedReadable()
  {
    return isReadable(BBOXINCLUDED);
  }
  
  public boolean isBBoxIncludedModified()
  {
    return isModified(BBOXINCLUDED);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getBBoxIncludedMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(BBOXINCLUDED).getAttributeMdDTO();
  }
  
  public Boolean getActiveByDefault()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ACTIVEBYDEFAULT));
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
  
  public boolean isActiveByDefaultWritable()
  {
    return isWritable(ACTIVEBYDEFAULT);
  }
  
  public boolean isActiveByDefaultReadable()
  {
    return isReadable(ACTIVEBYDEFAULT);
  }
  
  public boolean isActiveByDefaultModified()
  {
    return isModified(ACTIVEBYDEFAULT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getActiveByDefaultMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(ACTIVEBYDEFAULT).getAttributeMdDTO();
  }
  
  public java.util.Date getCreateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(CREATEDATE));
  }
  
  public boolean isCreateDateWritable()
  {
    return isWritable(CREATEDATE);
  }
  
  public boolean isCreateDateReadable()
  {
    return isReadable(CREATEDATE);
  }
  
  public boolean isCreateDateModified()
  {
    return isModified(CREATEDATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO getCreateDateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO) getAttributeDTO(CREATEDATE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.SingleActorDTO getCreatedBy()
  {
    if(getValue(CREATEDBY) == null || getValue(CREATEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActorDTO.get(getRequest(), getValue(CREATEDBY));
    }
  }
  
  public String getCreatedById()
  {
    return getValue(CREATEDBY);
  }
  
  public boolean isCreatedByWritable()
  {
    return isWritable(CREATEDBY);
  }
  
  public boolean isCreatedByReadable()
  {
    return isReadable(CREATEDBY);
  }
  
  public boolean isCreatedByModified()
  {
    return isModified(CREATEDBY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getCreatedByMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(CREATEDBY).getAttributeMdDTO();
  }
  
  public com.runwaysdk.geodashboard.gis.persist.DashboardLegendDTO getDashboardLegend()
  {
    return (com.runwaysdk.geodashboard.gis.persist.DashboardLegendDTO) this.getAttributeStructDTO(DASHBOARDLEGEND).getStructDTO();
  }
  
  public boolean isDashboardLegendWritable()
  {
    return isWritable(DASHBOARDLEGEND);
  }
  
  public boolean isDashboardLegendReadable()
  {
    return isReadable(DASHBOARDLEGEND);
  }
  
  public boolean isDashboardLegendModified()
  {
    return isModified(DASHBOARDLEGEND);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeStructMdDTO getDashboardLegendMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeStructMdDTO) getAttributeDTO(DASHBOARDLEGEND).getAttributeMdDTO();
  }
  
  public Boolean getDisplayInLegend()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(DISPLAYINLEGEND));
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
  
  public boolean isDisplayInLegendWritable()
  {
    return isWritable(DISPLAYINLEGEND);
  }
  
  public boolean isDisplayInLegendReadable()
  {
    return isReadable(DISPLAYINLEGEND);
  }
  
  public boolean isDisplayInLegendModified()
  {
    return isModified(DISPLAYINLEGEND);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getDisplayInLegendMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(DISPLAYINLEGEND).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdDomainDTO getEntityDomain()
  {
    if(getValue(ENTITYDOMAIN) == null || getValue(ENTITYDOMAIN).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdDomainDTO.get(getRequest(), getValue(ENTITYDOMAIN));
    }
  }
  
  public String getEntityDomainId()
  {
    return getValue(ENTITYDOMAIN);
  }
  
  public void setEntityDomain(com.runwaysdk.system.metadata.MdDomainDTO value)
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
  
  public boolean isEntityDomainWritable()
  {
    return isWritable(ENTITYDOMAIN);
  }
  
  public boolean isEntityDomainReadable()
  {
    return isReadable(ENTITYDOMAIN);
  }
  
  public boolean isEntityDomainModified()
  {
    return isModified(ENTITYDOMAIN);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getEntityDomainMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(ENTITYDOMAIN).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdAttributeReferenceDTO getGeoEntity()
  {
    if(getValue(GEOENTITY) == null || getValue(GEOENTITY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdAttributeReferenceDTO.get(getRequest(), getValue(GEOENTITY));
    }
  }
  
  public String getGeoEntityId()
  {
    return getValue(GEOENTITY);
  }
  
  public void setGeoEntity(com.runwaysdk.system.metadata.MdAttributeReferenceDTO value)
  {
    if(value == null)
    {
      setValue(GEOENTITY, "");
    }
    else
    {
      setValue(GEOENTITY, value.getId());
    }
  }
  
  public boolean isGeoEntityWritable()
  {
    return isWritable(GEOENTITY);
  }
  
  public boolean isGeoEntityReadable()
  {
    return isReadable(GEOENTITY);
  }
  
  public boolean isGeoEntityModified()
  {
    return isModified(GEOENTITY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getGeoEntityMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(GEOENTITY).getAttributeMdDTO();
  }
  
  public String getKeyName()
  {
    return getValue(KEYNAME);
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
  
  public boolean isKeyNameWritable()
  {
    return isWritable(KEYNAME);
  }
  
  public boolean isKeyNameReadable()
  {
    return isReadable(KEYNAME);
  }
  
  public boolean isKeyNameModified()
  {
    return isModified(KEYNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getKeyNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(KEYNAME).getAttributeMdDTO();
  }
  
  public java.util.Date getLastPublishDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(LASTPUBLISHDATE));
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
  
  public boolean isLastPublishDateWritable()
  {
    return isWritable(LASTPUBLISHDATE);
  }
  
  public boolean isLastPublishDateReadable()
  {
    return isReadable(LASTPUBLISHDATE);
  }
  
  public boolean isLastPublishDateModified()
  {
    return isModified(LASTPUBLISHDATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO getLastPublishDateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO) getAttributeDTO(LASTPUBLISHDATE).getAttributeMdDTO();
  }
  
  public java.util.Date getLastUpdateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(LASTUPDATEDATE));
  }
  
  public boolean isLastUpdateDateWritable()
  {
    return isWritable(LASTUPDATEDATE);
  }
  
  public boolean isLastUpdateDateReadable()
  {
    return isReadable(LASTUPDATEDATE);
  }
  
  public boolean isLastUpdateDateModified()
  {
    return isModified(LASTUPDATEDATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO getLastUpdateDateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO) getAttributeDTO(LASTUPDATEDATE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.SingleActorDTO getLastUpdatedBy()
  {
    if(getValue(LASTUPDATEDBY) == null || getValue(LASTUPDATEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActorDTO.get(getRequest(), getValue(LASTUPDATEDBY));
    }
  }
  
  public String getLastUpdatedById()
  {
    return getValue(LASTUPDATEDBY);
  }
  
  public boolean isLastUpdatedByWritable()
  {
    return isWritable(LASTUPDATEDBY);
  }
  
  public boolean isLastUpdatedByReadable()
  {
    return isReadable(LASTUPDATEDBY);
  }
  
  public boolean isLastUpdatedByModified()
  {
    return isModified(LASTUPDATEDBY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getLastUpdatedByMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(LASTUPDATEDBY).getAttributeMdDTO();
  }
  
  public Boolean getLayerEnabled()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(LAYERENABLED));
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
  
  public boolean isLayerEnabledWritable()
  {
    return isWritable(LAYERENABLED);
  }
  
  public boolean isLayerEnabledReadable()
  {
    return isReadable(LAYERENABLED);
  }
  
  public boolean isLayerEnabledModified()
  {
    return isModified(LAYERENABLED);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getLayerEnabledMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(LAYERENABLED).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.geodashboard.gis.persist.AllLayerTypeDTO> getLayerType()
  {
    return (java.util.List<com.runwaysdk.geodashboard.gis.persist.AllLayerTypeDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.geodashboard.gis.persist.AllLayerTypeDTO.CLASS, getEnumNames(LAYERTYPE));
  }
  
  public java.util.List<String> getLayerTypeEnumNames()
  {
    return getEnumNames(LAYERTYPE);
  }
  
  public void addLayerType(com.runwaysdk.geodashboard.gis.persist.AllLayerTypeDTO enumDTO)
  {
    addEnumItem(LAYERTYPE, enumDTO.toString());
  }
  
  public void removeLayerType(com.runwaysdk.geodashboard.gis.persist.AllLayerTypeDTO enumDTO)
  {
    removeEnumItem(LAYERTYPE, enumDTO.toString());
  }
  
  public void clearLayerType()
  {
    clearEnum(LAYERTYPE);
  }
  
  public boolean isLayerTypeWritable()
  {
    return isWritable(LAYERTYPE);
  }
  
  public boolean isLayerTypeReadable()
  {
    return isReadable(LAYERTYPE);
  }
  
  public boolean isLayerTypeModified()
  {
    return isModified(LAYERTYPE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getLayerTypeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(LAYERTYPE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.UsersDTO getLockedBy()
  {
    if(getValue(LOCKEDBY) == null || getValue(LOCKEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.UsersDTO.get(getRequest(), getValue(LOCKEDBY));
    }
  }
  
  public String getLockedById()
  {
    return getValue(LOCKEDBY);
  }
  
  public boolean isLockedByWritable()
  {
    return isWritable(LOCKEDBY);
  }
  
  public boolean isLockedByReadable()
  {
    return isReadable(LOCKEDBY);
  }
  
  public boolean isLockedByModified()
  {
    return isModified(LOCKEDBY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getLockedByMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(LOCKEDBY).getAttributeMdDTO();
  }
  
  public String getName()
  {
    return getValue(NAME);
  }
  
  public void setName(String value)
  {
    if(value == null)
    {
      setValue(NAME, "");
    }
    else
    {
      setValue(NAME, value);
    }
  }
  
  public boolean isNameWritable()
  {
    return isWritable(NAME);
  }
  
  public boolean isNameReadable()
  {
    return isReadable(NAME);
  }
  
  public boolean isNameModified()
  {
    return isModified(NAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(NAME).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.ActorDTO getOwner()
  {
    if(getValue(OWNER) == null || getValue(OWNER).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.ActorDTO.get(getRequest(), getValue(OWNER));
    }
  }
  
  public String getOwnerId()
  {
    return getValue(OWNER);
  }
  
  public void setOwner(com.runwaysdk.system.ActorDTO value)
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
  
  public boolean isOwnerWritable()
  {
    return isWritable(OWNER);
  }
  
  public boolean isOwnerReadable()
  {
    return isReadable(OWNER);
  }
  
  public boolean isOwnerModified()
  {
    return isModified(OWNER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getOwnerMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(OWNER).getAttributeMdDTO();
  }
  
  public Long getSeq()
  {
    return com.runwaysdk.constants.MdAttributeLongUtil.getTypeSafeValue(getValue(SEQ));
  }
  
  public boolean isSeqWritable()
  {
    return isWritable(SEQ);
  }
  
  public boolean isSeqReadable()
  {
    return isReadable(SEQ);
  }
  
  public boolean isSeqModified()
  {
    return isModified(SEQ);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getSeqMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(SEQ).getAttributeMdDTO();
  }
  
  public String getSiteMaster()
  {
    return getValue(SITEMASTER);
  }
  
  public boolean isSiteMasterWritable()
  {
    return isWritable(SITEMASTER);
  }
  
  public boolean isSiteMasterReadable()
  {
    return isReadable(SITEMASTER);
  }
  
  public boolean isSiteMasterModified()
  {
    return isModified(SITEMASTER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getSiteMasterMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(SITEMASTER).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.gis.geo.UniversalDTO getUniversal()
  {
    if(getValue(UNIVERSAL) == null || getValue(UNIVERSAL).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.gis.geo.UniversalDTO.get(getRequest(), getValue(UNIVERSAL));
    }
  }
  
  public String getUniversalId()
  {
    return getValue(UNIVERSAL);
  }
  
  public void setUniversal(com.runwaysdk.system.gis.geo.UniversalDTO value)
  {
    if(value == null)
    {
      setValue(UNIVERSAL, "");
    }
    else
    {
      setValue(UNIVERSAL, value.getId());
    }
  }
  
  public boolean isUniversalWritable()
  {
    return isWritable(UNIVERSAL);
  }
  
  public boolean isUniversalReadable()
  {
    return isReadable(UNIVERSAL);
  }
  
  public boolean isUniversalModified()
  {
    return isModified(UNIVERSAL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getUniversalMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(UNIVERSAL).getAttributeMdDTO();
  }
  
  public String getViewName()
  {
    return getValue(VIEWNAME);
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
  
  public boolean isViewNameWritable()
  {
    return isWritable(VIEWNAME);
  }
  
  public boolean isViewNameReadable()
  {
    return isReadable(VIEWNAME);
  }
  
  public boolean isViewNameModified()
  {
    return isModified(VIEWNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getViewNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(VIEWNAME).getAttributeMdDTO();
  }
  
  public Boolean getVirtual()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(VIRTUAL));
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
  
  public boolean isVirtualWritable()
  {
    return isWritable(VIRTUAL);
  }
  
  public boolean isVirtualReadable()
  {
    return isReadable(VIRTUAL);
  }
  
  public boolean isVirtualModified()
  {
    return isModified(VIRTUAL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getVirtualMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(VIRTUAL).getAttributeMdDTO();
  }
  
  public final java.lang.String applyWithStyle(com.runwaysdk.geodashboard.gis.persist.DashboardStyleDTO style, java.lang.String mapId, com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionDTO[] conditions)
  {
    String[] _declaredTypes = new String[]{"com.runwaysdk.geodashboard.gis.persist.DashboardStyle", "java.lang.String", "[Lcom.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;"};
    Object[] _parameters = new Object[]{style, mapId, conditions};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO.CLASS, "applyWithStyle", _declaredTypes);
    return (java.lang.String) getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final java.lang.String applyWithStyle(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id, com.runwaysdk.geodashboard.gis.persist.DashboardStyleDTO style, java.lang.String mapId, com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionDTO[] conditions)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "com.runwaysdk.geodashboard.gis.persist.DashboardStyle", "java.lang.String", "[Lcom.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;"};
    Object[] _parameters = new Object[]{id, style, mapId, conditions};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO.CLASS, "applyWithStyle", _declaredTypes);
    return (java.lang.String) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final java.lang.String getOntologyAttributeCategories()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO.CLASS, "getOntologyAttributeCategories", _declaredTypes);
    return (java.lang.String) getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final java.lang.String getOntologyAttributeCategories(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO.CLASS, "getOntologyAttributeCategories", _declaredTypes);
    return (java.lang.String) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.system.gis.geo.UniversalQueryDTO getSortedUniversals(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO.CLASS, "getSortedUniversals", _declaredTypes);
    return (com.runwaysdk.system.gis.geo.UniversalQueryDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final java.lang.String getThematicAttributeCategories()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO.CLASS, "getThematicAttributeCategories", _declaredTypes);
    return (java.lang.String) getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final java.lang.String getThematicAttributeCategories(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO.CLASS, "getThematicAttributeCategories", _declaredTypes);
    return (java.lang.String) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final void updateLegend(java.lang.Integer legendXPosition, java.lang.Integer legendYPosition, java.lang.Boolean groupedInLegend)
  {
    String[] _declaredTypes = new String[]{"java.lang.Integer", "java.lang.Integer", "java.lang.Boolean"};
    Object[] _parameters = new Object[]{legendXPosition, legendYPosition, groupedInLegend};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO.CLASS, "updateLegend", _declaredTypes);
    getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final void updateLegend(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id, java.lang.Integer legendXPosition, java.lang.Integer legendYPosition, java.lang.Boolean groupedInLegend)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.lang.Integer", "java.lang.Integer", "java.lang.Boolean"};
    Object[] _parameters = new Object[]{id, legendXPosition, legendYPosition, groupedInLegend};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO.CLASS, "updateLegend", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.gis.persist.DashboardStyleDTO> getAllHasStyle()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.gis.persist.DashboardStyleDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.geodashboard.gis.persist.HasStyleDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.gis.persist.DashboardStyleDTO> getAllHasStyle(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.gis.persist.DashboardStyleDTO>) clientRequestIF.getChildren(id, com.runwaysdk.geodashboard.gis.persist.HasStyleDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.gis.persist.HasStyleDTO> getAllHasStyleRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.gis.persist.HasStyleDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.geodashboard.gis.persist.HasStyleDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.gis.persist.HasStyleDTO> getAllHasStyleRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.gis.persist.HasStyleDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.geodashboard.gis.persist.HasStyleDTO.CLASS);
  }
  
  public com.runwaysdk.geodashboard.gis.persist.HasStyleDTO addHasStyle(com.runwaysdk.geodashboard.gis.persist.DashboardStyleDTO child)
  {
    return (com.runwaysdk.geodashboard.gis.persist.HasStyleDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.geodashboard.gis.persist.HasStyleDTO.CLASS);
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.HasStyleDTO addHasStyle(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.geodashboard.gis.persist.DashboardStyleDTO child)
  {
    return (com.runwaysdk.geodashboard.gis.persist.HasStyleDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.geodashboard.gis.persist.HasStyleDTO.CLASS);
  }
  
  public void removeHasStyle(com.runwaysdk.geodashboard.gis.persist.HasStyleDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeHasStyle(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.geodashboard.gis.persist.HasStyleDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllHasStyle()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.geodashboard.gis.persist.HasStyleDTO.CLASS);
  }
  
  public static void removeAllHasStyle(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.geodashboard.gis.persist.HasStyleDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO> getAllContainingMap()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO>) getRequest().getParents(this.getId(), com.runwaysdk.geodashboard.gis.persist.HasLayerDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO> getAllContainingMap(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO>) clientRequestIF.getParents(id, com.runwaysdk.geodashboard.gis.persist.HasLayerDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.gis.persist.HasLayerDTO> getAllContainingMapRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.gis.persist.HasLayerDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.geodashboard.gis.persist.HasLayerDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.gis.persist.HasLayerDTO> getAllContainingMapRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.gis.persist.HasLayerDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.geodashboard.gis.persist.HasLayerDTO.CLASS);
  }
  
  public com.runwaysdk.geodashboard.gis.persist.HasLayerDTO addContainingMap(com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO parent)
  {
    return (com.runwaysdk.geodashboard.gis.persist.HasLayerDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.geodashboard.gis.persist.HasLayerDTO.CLASS);
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.HasLayerDTO addContainingMap(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO parent)
  {
    return (com.runwaysdk.geodashboard.gis.persist.HasLayerDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.geodashboard.gis.persist.HasLayerDTO.CLASS);
  }
  
  public void removeContainingMap(com.runwaysdk.geodashboard.gis.persist.HasLayerDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeContainingMap(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.geodashboard.gis.persist.HasLayerDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllContainingMap()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.geodashboard.gis.persist.HasLayerDTO.CLASS);
  }
  
  public static void removeAllContainingMap(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.geodashboard.gis.persist.HasLayerDTO.CLASS);
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO) dto;
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
  
  public static com.runwaysdk.geodashboard.gis.persist.DashboardLayerQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.geodashboard.gis.persist.DashboardLayerQueryDTO) clientRequest.getAllInstances(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
