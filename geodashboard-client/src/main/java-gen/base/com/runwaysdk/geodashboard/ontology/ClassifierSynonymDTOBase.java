package com.runwaysdk.geodashboard.ontology;

@com.runwaysdk.business.ClassSignature(hash = 854937269)
public abstract class ClassifierSynonymDTOBase extends com.runwaysdk.business.ontology.TermDTO implements com.runwaysdk.generation.loader.Reloadable
{
  public final static String CLASS = "com.runwaysdk.geodashboard.ontology.ClassifierSynonym";
  private static final long serialVersionUID = 854937269;
  
  protected ClassifierSynonymDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected ClassifierSynonymDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CLASSIFIER = "classifier";
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
  public com.runwaysdk.geodashboard.ontology.ClassifierDTO getClassifier()
  {
    if(getValue(CLASSIFIER) == null || getValue(CLASSIFIER).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.geodashboard.ontology.ClassifierDTO.get(getRequest(), getValue(CLASSIFIER));
    }
  }
  
  public String getClassifierId()
  {
    return getValue(CLASSIFIER);
  }
  
  public void setClassifier(com.runwaysdk.geodashboard.ontology.ClassifierDTO value)
  {
    if(value == null)
    {
      setValue(CLASSIFIER, "");
    }
    else
    {
      setValue(CLASSIFIER, value.getId());
    }
  }
  
  public boolean isClassifierWritable()
  {
    return isWritable(CLASSIFIER);
  }
  
  public boolean isClassifierReadable()
  {
    return isReadable(CLASSIFIER);
  }
  
  public boolean isClassifierModified()
  {
    return isModified(CLASSIFIER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getClassifierMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(CLASSIFIER).getAttributeMdDTO();
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
  
  public com.runwaysdk.geodashboard.ontology.ClassifierSynonymDisplayLabelDTO getDisplayLabel()
  {
    return (com.runwaysdk.geodashboard.ontology.ClassifierSynonymDisplayLabelDTO) this.getAttributeStructDTO(DISPLAYLABEL).getStructDTO();
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
  
  public static final com.runwaysdk.geodashboard.ontology.ClassifierSynonymDTO create(com.runwaysdk.constants.ClientRequestIF clientRequest, com.runwaysdk.geodashboard.ontology.ClassifierDTO classifier, com.runwaysdk.geodashboard.ontology.ClassifierSynonymDTO synonym)
  {
    String[] _declaredTypes = new String[]{"com.runwaysdk.geodashboard.ontology.Classifier", "com.runwaysdk.geodashboard.ontology.ClassifierSynonym"};
    Object[] _parameters = new Object[]{classifier, synonym};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.ontology.ClassifierSynonymDTO.CLASS, "create", _declaredTypes);
    return (com.runwaysdk.geodashboard.ontology.ClassifierSynonymDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO> getAllClassifierSynonymAttributeRoots()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO>) getRequest().getParents(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO> getAllClassifierSynonymAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO>) clientRequestIF.getParents(id, com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRootDTO> getAllClassifierSynonymAttributeRootsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRootDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRootDTO> getAllClassifierSynonymAttributeRootsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRootDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRootDTO.CLASS);
  }
  
  public com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRootDTO addClassifierSynonymAttributeRoots(com.runwaysdk.system.metadata.MdAttributeTermDTO parent)
  {
    return (com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRootDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRootDTO addClassifierSynonymAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdAttributeTermDTO parent)
  {
    return (com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRootDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRootDTO.CLASS);
  }
  
  public void removeClassifierSynonymAttributeRoots(com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRootDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeClassifierSynonymAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRootDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllClassifierSynonymAttributeRoots()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRootDTO.CLASS);
  }
  
  public static void removeAllClassifierSynonymAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierDTO> getAllIsSynonymFor()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierDTO>) getRequest().getParents(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierDTO> getAllIsSynonymFor(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierDTO>) clientRequestIF.getParents(id, com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO> getAllIsSynonymForRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO> getAllIsSynonymForRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO.CLASS);
  }
  
  public com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO addIsSynonymFor(com.runwaysdk.geodashboard.ontology.ClassifierDTO parent)
  {
    return (com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO.CLASS);
  }
  
  public static com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO addIsSynonymFor(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.geodashboard.ontology.ClassifierDTO parent)
  {
    return (com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO.CLASS);
  }
  
  public void removeIsSynonymFor(com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeIsSynonymFor(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllIsSynonymFor()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO.CLASS);
  }
  
  public static void removeAllIsSynonymFor(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.geodashboard.ontology.ClassifierHasSynonymDTO.CLASS);
  }
  
  public static com.runwaysdk.geodashboard.ontology.ClassifierSynonymDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.geodashboard.ontology.ClassifierSynonymDTO) dto;
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
  
  public static com.runwaysdk.geodashboard.ontology.ClassifierSynonymQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.geodashboard.ontology.ClassifierSynonymQueryDTO) clientRequest.getAllInstances(com.runwaysdk.geodashboard.ontology.ClassifierSynonymDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.geodashboard.ontology.ClassifierSynonymDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.ontology.ClassifierSynonymDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.geodashboard.ontology.ClassifierSynonymDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.geodashboard.ontology.ClassifierSynonymDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.geodashboard.ontology.ClassifierSynonymDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.geodashboard.ontology.ClassifierSynonymDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
