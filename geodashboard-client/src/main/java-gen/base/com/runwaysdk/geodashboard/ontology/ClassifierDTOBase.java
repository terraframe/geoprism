package com.runwaysdk.geodashboard.ontology;

@com.runwaysdk.business.ClassSignature(hash = 1069620461)
public abstract class ClassifierDTOBase extends com.runwaysdk.business.ontology.TermDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.ontology.Classifier";
  private static final long serialVersionUID = 1069620461;
  
  protected ClassifierDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected ClassifierDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CLASSIFIERID = "classifierId";
  public static java.lang.String CLASSIFIERPACKAGE = "classifierPackage";
  public static java.lang.String CREATEDATE = "createDate";
  public static java.lang.String CREATEDBY = "createdBy";
  public static java.lang.String DISPLAYLABEL = "displayLabel";
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
  public String getClassifierId()
  {
    return getValue(CLASSIFIERID);
  }
  
  public void setClassifierId(String value)
  {
    if(value == null)
    {
      setValue(CLASSIFIERID, "");
    }
    else
    {
      setValue(CLASSIFIERID, value);
    }
  }
  
  public boolean isClassifierIdWritable()
  {
    return isWritable(CLASSIFIERID);
  }
  
  public boolean isClassifierIdReadable()
  {
    return isReadable(CLASSIFIERID);
  }
  
  public boolean isClassifierIdModified()
  {
    return isModified(CLASSIFIERID);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getClassifierIdMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(CLASSIFIERID).getAttributeMdDTO();
  }
  
  public String getClassifierPackage()
  {
    return getValue(CLASSIFIERPACKAGE);
  }
  
  public void setClassifierPackage(String value)
  {
    if(value == null)
    {
      setValue(CLASSIFIERPACKAGE, "");
    }
    else
    {
      setValue(CLASSIFIERPACKAGE, value);
    }
  }
  
  public boolean isClassifierPackageWritable()
  {
    return isWritable(CLASSIFIERPACKAGE);
  }
  
  public boolean isClassifierPackageReadable()
  {
    return isReadable(CLASSIFIERPACKAGE);
  }
  
  public boolean isClassifierPackageModified()
  {
    return isModified(CLASSIFIERPACKAGE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getClassifierPackageMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(CLASSIFIERPACKAGE).getAttributeMdDTO();
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
  
  public com.runwaysdk.geodashboard.ontology.ClassifierDisplayLabelDTO getDisplayLabel()
  {
    return (com.runwaysdk.geodashboard.ontology.ClassifierDisplayLabelDTO) this.getAttributeStructDTO(DISPLAYLABEL).getStructDTO();
  }
  
  public boolean isDisplayLabelWritable()
  {
    return isWritable(DISPLAYLABEL);
  }
  
  public boolean isDisplayLabelReadable()
  {
    return isReadable(DISPLAYLABEL);
  }
  
  public boolean isDisplayLabelModified()
  {
    return isModified(DISPLAYLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO getDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO) getAttributeDTO(DISPLAYLABEL).getAttributeMdDTO();
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
  
  public static final com.runwaysdk.business.ontology.TermAndRelDTO create(com.runwaysdk.constants.ClientRequestIF clientRequest, com.runwaysdk.geodashboard.ontology.ClassifierDTO dto, java.lang.String parentId)
  {
    String[] _declaredTypes = new String[]{"com.runwaysdk.geodashboard.ontology.Classifier", "java.lang.String"};
    Object[] _parameters = new Object[]{dto, parentId};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.ontology.ClassifierDTO.CLASS, "create", _declaredTypes);
    return (com.runwaysdk.business.ontology.TermAndRelDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.geodashboard.ontology.ClassifierDTO getRoot(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.ontology.ClassifierDTO.CLASS, "getRoot", _declaredTypes);
    return (com.runwaysdk.geodashboard.ontology.ClassifierDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierSynonymDTO> getAllHasSynonym()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierSynonymDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierSynonymDTO> getAllHasSynonym(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierSynonymDTO>) clientRequestIF.getChildren(id, com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO> getAllHasSynonymRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO> getAllHasSynonymRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO.CLASS);
  }
  
  public com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO addHasSynonym(com.runwaysdk.geodashboard.ontology.ClassifierSynonymDTO child)
  {
    return (com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO.CLASS);
  }
  
  public static com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO addHasSynonym(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.geodashboard.ontology.ClassifierSynonymDTO child)
  {
    return (com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO.CLASS);
  }
  
  public void removeHasSynonym(com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeHasSynonym(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllHasSynonym()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO.CLASS);
  }
  
  public static void removeAllHasSynonym(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierDTO> getAllIsAChild()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierDTO> getAllIsAChild(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierDTO>) clientRequestIF.getChildren(id, com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO> getAllIsAChildRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO> getAllIsAChildRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO.CLASS);
  }
  
  public com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO addIsAChild(com.runwaysdk.geodashboard.ontology.ClassifierDTO child)
  {
    return (com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO.CLASS);
  }
  
  public static com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO addIsAChild(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.geodashboard.ontology.ClassifierDTO child)
  {
    return (com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO.CLASS);
  }
  
  public void removeIsAChild(com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeIsAChild(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllIsAChild()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO.CLASS);
  }
  
  public static void removeAllIsAChild(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeMultiTermDTO> getAllClassifierMultiTermAttributeRoots()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeMultiTermDTO>) getRequest().getParents(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeMultiTermDTO> getAllClassifierMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeMultiTermDTO>) clientRequestIF.getParents(id, com.runwaysdk.geodashboard.ontology.ClassifierMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierMultiTermAttributeRootDTO> getAllClassifierMultiTermAttributeRootsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierMultiTermAttributeRootDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierMultiTermAttributeRootDTO> getAllClassifierMultiTermAttributeRootsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierMultiTermAttributeRootDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.geodashboard.ontology.ClassifierMultiTermAttributeRootDTO.CLASS);
  }
  
  public com.runwaysdk.geodashboard.ontology.ClassifierMultiTermAttributeRootDTO addClassifierMultiTermAttributeRoots(com.runwaysdk.system.metadata.MdAttributeMultiTermDTO parent)
  {
    return (com.runwaysdk.geodashboard.ontology.ClassifierMultiTermAttributeRootDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierMultiTermAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.geodashboard.ontology.ClassifierMultiTermAttributeRootDTO addClassifierMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdAttributeMultiTermDTO parent)
  {
    return (com.runwaysdk.geodashboard.ontology.ClassifierMultiTermAttributeRootDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.geodashboard.ontology.ClassifierMultiTermAttributeRootDTO.CLASS);
  }
  
  public void removeClassifierMultiTermAttributeRoots(com.runwaysdk.geodashboard.ontology.ClassifierMultiTermAttributeRootDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeClassifierMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.geodashboard.ontology.ClassifierMultiTermAttributeRootDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllClassifierMultiTermAttributeRoots()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierMultiTermAttributeRootDTO.CLASS);
  }
  
  public static void removeAllClassifierMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.geodashboard.ontology.ClassifierMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO> getAllClassifierTermAttributeRoots()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO>) getRequest().getParents(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO> getAllClassifierTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO>) clientRequestIF.getParents(id, com.runwaysdk.geodashboard.ontology.ClassifierTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierTermAttributeRootDTO> getAllClassifierTermAttributeRootsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierTermAttributeRootDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierTermAttributeRootDTO> getAllClassifierTermAttributeRootsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierTermAttributeRootDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.geodashboard.ontology.ClassifierTermAttributeRootDTO.CLASS);
  }
  
  public com.runwaysdk.geodashboard.ontology.ClassifierTermAttributeRootDTO addClassifierTermAttributeRoots(com.runwaysdk.system.metadata.MdAttributeTermDTO parent)
  {
    return (com.runwaysdk.geodashboard.ontology.ClassifierTermAttributeRootDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierTermAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.geodashboard.ontology.ClassifierTermAttributeRootDTO addClassifierTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdAttributeTermDTO parent)
  {
    return (com.runwaysdk.geodashboard.ontology.ClassifierTermAttributeRootDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.geodashboard.ontology.ClassifierTermAttributeRootDTO.CLASS);
  }
  
  public void removeClassifierTermAttributeRoots(com.runwaysdk.geodashboard.ontology.ClassifierTermAttributeRootDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeClassifierTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.geodashboard.ontology.ClassifierTermAttributeRootDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllClassifierTermAttributeRoots()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierTermAttributeRootDTO.CLASS);
  }
  
  public static void removeAllClassifierTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.geodashboard.ontology.ClassifierTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierDTO> getAllIsAParent()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierDTO>) getRequest().getParents(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierDTO> getAllIsAParent(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierDTO>) clientRequestIF.getParents(id, com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO> getAllIsAParentRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO> getAllIsAParentRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO.CLASS);
  }
  
  public com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO addIsAParent(com.runwaysdk.geodashboard.ontology.ClassifierDTO parent)
  {
    return (com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO.CLASS);
  }
  
  public static com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO addIsAParent(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.geodashboard.ontology.ClassifierDTO parent)
  {
    return (com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO.CLASS);
  }
  
  public void removeIsAParent(com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeIsAParent(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllIsAParent()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO.CLASS);
  }
  
  public static void removeAllIsAParent(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO.CLASS);
  }
  
  public static com.runwaysdk.geodashboard.ontology.ClassifierDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.geodashboard.ontology.ClassifierDTO) dto;
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
  
  public static com.runwaysdk.geodashboard.ontology.ClassifierQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.geodashboard.ontology.ClassifierQueryDTO) clientRequest.getAllInstances(com.runwaysdk.geodashboard.ontology.ClassifierDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.geodashboard.ontology.ClassifierDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.ontology.ClassifierDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.geodashboard.ontology.ClassifierDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.geodashboard.ontology.ClassifierDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.ontology.ClassifierDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.geodashboard.ontology.ClassifierDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
