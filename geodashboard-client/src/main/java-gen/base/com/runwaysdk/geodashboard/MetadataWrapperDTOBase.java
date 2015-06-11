package com.runwaysdk.geodashboard;

@com.runwaysdk.business.ClassSignature(hash = 1171284837)
public abstract class MetadataWrapperDTOBase extends com.runwaysdk.business.BusinessDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.MetadataWrapper";
  private static final long serialVersionUID = 1171284837;
  
  protected MetadataWrapperDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MetadataWrapperDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CREATEDATE = "createDate";
  public static java.lang.String CREATEDBY = "createdBy";
  public static java.lang.String ENTITYDOMAIN = "entityDomain";
  public static java.lang.String ID = "id";
  public static java.lang.String KEYNAME = "keyName";
  public static java.lang.String LASTUPDATEDATE = "lastUpdateDate";
  public static java.lang.String LASTUPDATEDBY = "lastUpdatedBy";
  public static java.lang.String LOCKEDBY = "lockedBy";
  public static java.lang.String OWNER = "owner";
  public static java.lang.String SEQ = "seq";
  public static java.lang.String SITEMASTER = "siteMaster";
  public static java.lang.String TYPE = "type";
  public static java.lang.String UNIVERSAL = "universal";
  public static java.lang.String WRAPPEDMDCLASS = "wrappedMdClass";
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
  
  public com.runwaysdk.system.metadata.MdClassDTO getWrappedMdClass()
  {
    if(getValue(WRAPPEDMDCLASS) == null || getValue(WRAPPEDMDCLASS).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdClassDTO.get(getRequest(), getValue(WRAPPEDMDCLASS));
    }
  }
  
  public String getWrappedMdClassId()
  {
    return getValue(WRAPPEDMDCLASS);
  }
  
  public void setWrappedMdClass(com.runwaysdk.system.metadata.MdClassDTO value)
  {
    if(value == null)
    {
      setValue(WRAPPEDMDCLASS, "");
    }
    else
    {
      setValue(WRAPPEDMDCLASS, value.getId());
    }
  }
  
  public boolean isWrappedMdClassWritable()
  {
    return isWritable(WRAPPEDMDCLASS);
  }
  
  public boolean isWrappedMdClassReadable()
  {
    return isReadable(WRAPPEDMDCLASS);
  }
  
  public boolean isWrappedMdClassModified()
  {
    return isModified(WRAPPEDMDCLASS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getWrappedMdClassMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(WRAPPEDMDCLASS).getAttributeMdDTO();
  }
  
  public final com.runwaysdk.geodashboard.MdAttributeViewDTO[] getSortedAttributes()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.MetadataWrapperDTO.CLASS, "getSortedAttributes", _declaredTypes);
    return (com.runwaysdk.geodashboard.MdAttributeViewDTO[]) getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final com.runwaysdk.geodashboard.MdAttributeViewDTO[] getSortedAttributes(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.MetadataWrapperDTO.CLASS, "getSortedAttributes", _declaredTypes);
    return (com.runwaysdk.geodashboard.MdAttributeViewDTO[]) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.AttributeWrapperDTO> getAllAttributeWrapper()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.AttributeWrapperDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.geodashboard.DashboardAttributesDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.AttributeWrapperDTO> getAllAttributeWrapper(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.AttributeWrapperDTO>) clientRequestIF.getChildren(id, com.runwaysdk.geodashboard.DashboardAttributesDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.DashboardAttributesDTO> getAllAttributeWrapperRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.DashboardAttributesDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.geodashboard.DashboardAttributesDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.DashboardAttributesDTO> getAllAttributeWrapperRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.DashboardAttributesDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.geodashboard.DashboardAttributesDTO.CLASS);
  }
  
  public com.runwaysdk.geodashboard.DashboardAttributesDTO addAttributeWrapper(com.runwaysdk.geodashboard.AttributeWrapperDTO child)
  {
    return (com.runwaysdk.geodashboard.DashboardAttributesDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.geodashboard.DashboardAttributesDTO.CLASS);
  }
  
  public static com.runwaysdk.geodashboard.DashboardAttributesDTO addAttributeWrapper(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.geodashboard.AttributeWrapperDTO child)
  {
    return (com.runwaysdk.geodashboard.DashboardAttributesDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.geodashboard.DashboardAttributesDTO.CLASS);
  }
  
  public void removeAttributeWrapper(com.runwaysdk.geodashboard.DashboardAttributesDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeAttributeWrapper(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.geodashboard.DashboardAttributesDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllAttributeWrapper()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.geodashboard.DashboardAttributesDTO.CLASS);
  }
  
  public static void removeAllAttributeWrapper(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.geodashboard.DashboardAttributesDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.GeoNodeDTO> getAllGeoNode()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoNodeDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.geodashboard.MetadataGeoNodeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.GeoNodeDTO> getAllGeoNode(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoNodeDTO>) clientRequestIF.getChildren(id, com.runwaysdk.geodashboard.MetadataGeoNodeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.MetadataGeoNodeDTO> getAllGeoNodeRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.MetadataGeoNodeDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.geodashboard.MetadataGeoNodeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.MetadataGeoNodeDTO> getAllGeoNodeRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.MetadataGeoNodeDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.geodashboard.MetadataGeoNodeDTO.CLASS);
  }
  
  public com.runwaysdk.geodashboard.MetadataGeoNodeDTO addGeoNode(com.runwaysdk.system.gis.geo.GeoNodeDTO child)
  {
    return (com.runwaysdk.geodashboard.MetadataGeoNodeDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.geodashboard.MetadataGeoNodeDTO.CLASS);
  }
  
  public static com.runwaysdk.geodashboard.MetadataGeoNodeDTO addGeoNode(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.gis.geo.GeoNodeDTO child)
  {
    return (com.runwaysdk.geodashboard.MetadataGeoNodeDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.geodashboard.MetadataGeoNodeDTO.CLASS);
  }
  
  public void removeGeoNode(com.runwaysdk.geodashboard.MetadataGeoNodeDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeGeoNode(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.geodashboard.MetadataGeoNodeDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllGeoNode()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.geodashboard.MetadataGeoNodeDTO.CLASS);
  }
  
  public static void removeAllGeoNode(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.geodashboard.MetadataGeoNodeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.DashboardDTO> getAllDashboard()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.DashboardDTO>) getRequest().getParents(this.getId(), com.runwaysdk.geodashboard.DashboardMetadataDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.DashboardDTO> getAllDashboard(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.DashboardDTO>) clientRequestIF.getParents(id, com.runwaysdk.geodashboard.DashboardMetadataDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.DashboardMetadataDTO> getAllDashboardRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.DashboardMetadataDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.geodashboard.DashboardMetadataDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.DashboardMetadataDTO> getAllDashboardRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.DashboardMetadataDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.geodashboard.DashboardMetadataDTO.CLASS);
  }
  
  public com.runwaysdk.geodashboard.DashboardMetadataDTO addDashboard(com.runwaysdk.geodashboard.DashboardDTO parent)
  {
    return (com.runwaysdk.geodashboard.DashboardMetadataDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.geodashboard.DashboardMetadataDTO.CLASS);
  }
  
  public static com.runwaysdk.geodashboard.DashboardMetadataDTO addDashboard(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.geodashboard.DashboardDTO parent)
  {
    return (com.runwaysdk.geodashboard.DashboardMetadataDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.geodashboard.DashboardMetadataDTO.CLASS);
  }
  
  public void removeDashboard(com.runwaysdk.geodashboard.DashboardMetadataDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeDashboard(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.geodashboard.DashboardMetadataDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllDashboard()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.geodashboard.DashboardMetadataDTO.CLASS);
  }
  
  public static void removeAllDashboard(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.geodashboard.DashboardMetadataDTO.CLASS);
  }
  
  public static com.runwaysdk.geodashboard.MetadataWrapperDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.geodashboard.MetadataWrapperDTO) dto;
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
  
  public static com.runwaysdk.geodashboard.MetadataWrapperQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.geodashboard.MetadataWrapperQueryDTO) clientRequest.getAllInstances(com.runwaysdk.geodashboard.MetadataWrapperDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.geodashboard.MetadataWrapperDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.MetadataWrapperDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.geodashboard.MetadataWrapperDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.geodashboard.MetadataWrapperDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.MetadataWrapperDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.geodashboard.MetadataWrapperDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
