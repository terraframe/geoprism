Mojo.Meta.newClass('com.runwaysdk.system.metadata.Metadata', {
  Extends : 'com.runwaysdk.business.BusinessDTO',
  IsAbstract : true,
  Constants : 
  {
    CREATEDATE : 'createDate',
    CREATEDBY : 'createdBy',
    DESCRIPTION : 'description',
    ENTITYDOMAIN : 'entityDomain',
    ID : 'id',
    KEYNAME : 'keyName',
    LASTUPDATEDATE : 'lastUpdateDate',
    LASTUPDATEDBY : 'lastUpdatedBy',
    LOCKEDBY : 'lockedBy',
    OWNER : 'owner',
    REMOVE : 'remove',
    SEQ : 'seq',
    SITEMASTER : 'siteMaster',
    TYPE : 'type',
    CLASS : 'com.runwaysdk.system.metadata.Metadata'
  },
  Instance: 
  {
    getCreateDate : function()
    {
      return this.getAttributeDTO('createDate').getValue();
    },
    isCreateDateReadable : function()
    {
      return this.getAttributeDTO('createDate').isReadable();
    },
    isCreateDateWritable : function()
    {
      return this.getAttributeDTO('createDate').isWritable();
    },
    isCreateDateModified : function()
    {
      return this.getAttributeDTO('createDate').isModified();
    },
    getCreateDateMd : function()
    {
      return this.getAttributeDTO('createDate').getAttributeMdDTO();
    },
    getCreatedBy : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('createdBy');
      var value = attributeDTO.getValue();
      if(value == null || value == '')
      {
        clientRequest.onSuccess(null);
      }
      else
      {
        Mojo.$.com.runwaysdk.Facade.get(clientRequest, value);
      }
    },
    isCreatedByReadable : function()
    {
      return this.getAttributeDTO('createdBy').isReadable();
    },
    isCreatedByWritable : function()
    {
      return this.getAttributeDTO('createdBy').isWritable();
    },
    isCreatedByModified : function()
    {
      return this.getAttributeDTO('createdBy').isModified();
    },
    getCreatedByMd : function()
    {
      return this.getAttributeDTO('createdBy').getAttributeMdDTO();
    },
    getDescription : function()
    {
      if(Mojo.Meta.classExists('com.runwaysdk.system.metadata.MetadataDisplayLabel'))
      {
        var structDTO = this.getAttributeDTO('description').getStructDTO();
        if(structDTO == null)
        {
          return null;
        }
        else if(structDTO instanceof Mojo.$.com.runwaysdk.system.metadata.MetadataDisplayLabel)
        {
          return structDTO;
        }
        else
        {
          structDTO = new Mojo.$.com.runwaysdk.system.metadata.MetadataDisplayLabel(structDTO);
          this.getAttributeDTO('description').setStructDTO(structDTO);
          return structDTO;
        }
      }
      else
      {
        throw new Mojo.$.com.runwaysdk.Exception('Must import type com.runwaysdk.system.metadata.MetadataDisplayLabel');
      }
    },
    isDescriptionReadable : function()
    {
      return this.getAttributeDTO('description').isReadable();
    },
    isDescriptionWritable : function()
    {
      return this.getAttributeDTO('description').isWritable();
    },
    isDescriptionModified : function()
    {
      return this.getAttributeDTO('description').isModified();
    },
    getDescriptionMd : function()
    {
      return this.getAttributeDTO('description').getAttributeMdDTO();
    },
    getEntityDomain : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('entityDomain');
      var value = attributeDTO.getValue();
      if(value == null || value == '')
      {
        clientRequest.onSuccess(null);
      }
      else
      {
        Mojo.$.com.runwaysdk.Facade.get(clientRequest, value);
      }
    },
    setEntityDomain : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('entityDomain');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isEntityDomainReadable : function()
    {
      return this.getAttributeDTO('entityDomain').isReadable();
    },
    isEntityDomainWritable : function()
    {
      return this.getAttributeDTO('entityDomain').isWritable();
    },
    isEntityDomainModified : function()
    {
      return this.getAttributeDTO('entityDomain').isModified();
    },
    getEntityDomainMd : function()
    {
      return this.getAttributeDTO('entityDomain').getAttributeMdDTO();
    },
    getKeyName : function()
    {
      return this.getAttributeDTO('keyName').getValue();
    },
    setKeyName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('keyName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isKeyNameReadable : function()
    {
      return this.getAttributeDTO('keyName').isReadable();
    },
    isKeyNameWritable : function()
    {
      return this.getAttributeDTO('keyName').isWritable();
    },
    isKeyNameModified : function()
    {
      return this.getAttributeDTO('keyName').isModified();
    },
    getKeyNameMd : function()
    {
      return this.getAttributeDTO('keyName').getAttributeMdDTO();
    },
    getLastUpdateDate : function()
    {
      return this.getAttributeDTO('lastUpdateDate').getValue();
    },
    isLastUpdateDateReadable : function()
    {
      return this.getAttributeDTO('lastUpdateDate').isReadable();
    },
    isLastUpdateDateWritable : function()
    {
      return this.getAttributeDTO('lastUpdateDate').isWritable();
    },
    isLastUpdateDateModified : function()
    {
      return this.getAttributeDTO('lastUpdateDate').isModified();
    },
    getLastUpdateDateMd : function()
    {
      return this.getAttributeDTO('lastUpdateDate').getAttributeMdDTO();
    },
    getLastUpdatedBy : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('lastUpdatedBy');
      var value = attributeDTO.getValue();
      if(value == null || value == '')
      {
        clientRequest.onSuccess(null);
      }
      else
      {
        Mojo.$.com.runwaysdk.Facade.get(clientRequest, value);
      }
    },
    isLastUpdatedByReadable : function()
    {
      return this.getAttributeDTO('lastUpdatedBy').isReadable();
    },
    isLastUpdatedByWritable : function()
    {
      return this.getAttributeDTO('lastUpdatedBy').isWritable();
    },
    isLastUpdatedByModified : function()
    {
      return this.getAttributeDTO('lastUpdatedBy').isModified();
    },
    getLastUpdatedByMd : function()
    {
      return this.getAttributeDTO('lastUpdatedBy').getAttributeMdDTO();
    },
    getLockedBy : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('lockedBy');
      var value = attributeDTO.getValue();
      if(value == null || value == '')
      {
        clientRequest.onSuccess(null);
      }
      else
      {
        Mojo.$.com.runwaysdk.Facade.get(clientRequest, value);
      }
    },
    isLockedByReadable : function()
    {
      return this.getAttributeDTO('lockedBy').isReadable();
    },
    isLockedByWritable : function()
    {
      return this.getAttributeDTO('lockedBy').isWritable();
    },
    isLockedByModified : function()
    {
      return this.getAttributeDTO('lockedBy').isModified();
    },
    getLockedByMd : function()
    {
      return this.getAttributeDTO('lockedBy').getAttributeMdDTO();
    },
    getOwner : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('owner');
      var value = attributeDTO.getValue();
      if(value == null || value == '')
      {
        clientRequest.onSuccess(null);
      }
      else
      {
        Mojo.$.com.runwaysdk.Facade.get(clientRequest, value);
      }
    },
    setOwner : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('owner');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isOwnerReadable : function()
    {
      return this.getAttributeDTO('owner').isReadable();
    },
    isOwnerWritable : function()
    {
      return this.getAttributeDTO('owner').isWritable();
    },
    isOwnerModified : function()
    {
      return this.getAttributeDTO('owner').isModified();
    },
    getOwnerMd : function()
    {
      return this.getAttributeDTO('owner').getAttributeMdDTO();
    },
    getRemove : function()
    {
      return this.getAttributeDTO('remove').getValue();
    },
    setRemove : function(value)
    {
      var attributeDTO = this.getAttributeDTO('remove');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isRemoveReadable : function()
    {
      return this.getAttributeDTO('remove').isReadable();
    },
    isRemoveWritable : function()
    {
      return this.getAttributeDTO('remove').isWritable();
    },
    isRemoveModified : function()
    {
      return this.getAttributeDTO('remove').isModified();
    },
    getRemoveMd : function()
    {
      return this.getAttributeDTO('remove').getAttributeMdDTO();
    },
    getSeq : function()
    {
      return this.getAttributeDTO('seq').getValue();
    },
    isSeqReadable : function()
    {
      return this.getAttributeDTO('seq').isReadable();
    },
    isSeqWritable : function()
    {
      return this.getAttributeDTO('seq').isWritable();
    },
    isSeqModified : function()
    {
      return this.getAttributeDTO('seq').isModified();
    },
    getSeqMd : function()
    {
      return this.getAttributeDTO('seq').getAttributeMdDTO();
    },
    getSiteMaster : function()
    {
      return this.getAttributeDTO('siteMaster').getValue();
    },
    isSiteMasterReadable : function()
    {
      return this.getAttributeDTO('siteMaster').isReadable();
    },
    isSiteMasterWritable : function()
    {
      return this.getAttributeDTO('siteMaster').isWritable();
    },
    isSiteMasterModified : function()
    {
      return this.getAttributeDTO('siteMaster').isModified();
    },
    getSiteMasterMd : function()
    {
      return this.getAttributeDTO('siteMaster').getAttributeMdDTO();
    },
    getAllActor : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParents(clientRequest, this.getId(), 'com.runwaysdk.system.TypePermission');
    },
    getAllActorRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParentRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.TypePermission');
    },
    addActor : function(clientRequest, parent)
    {
      var parentId = (parent instanceof Object) ? parent.getId() : parent;
      Mojo.$.com.runwaysdk.Facade.addParent(clientRequest, parentId, this.getId(), 'com.runwaysdk.system.TypePermission');
    },
    removeActor : function(clientRequest, relationship)
    {
      var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteParent(clientRequest, relationshipId);
    },
    removeAllActor : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteParents(clientRequest, this.getId(), 'com.runwaysdk.system.TypePermission');
    },
    getAllChildMetadata : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildren(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.MetadataRelationship');
    },
    getAllChildMetadataRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.MetadataRelationship');
    },
    addChildMetadata : function(clientRequest, child)
    {
      var childId = (child instanceof Object) ? child.getId() : child;
      Mojo.$.com.runwaysdk.Facade.addChild(clientRequest, this.getId(), childId, 'com.runwaysdk.system.metadata.MetadataRelationship');
    },
    removeChildMetadata : function(clientRequest, relationship)
    {
        var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteChild(clientRequest, relationshipId);
    },
    removeAllChildMetadata : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteChildren(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.MetadataRelationship');
    },
    getAllMdParameter : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildren(clientRequest, this.getId(), 'com.runwaysdk.system.MetadataParameter');
    },
    getAllMdParameterRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.MetadataParameter');
    },
    addMdParameter : function(clientRequest, child)
    {
      var childId = (child instanceof Object) ? child.getId() : child;
      Mojo.$.com.runwaysdk.Facade.addChild(clientRequest, this.getId(), childId, 'com.runwaysdk.system.MetadataParameter');
    },
    removeMdParameter : function(clientRequest, relationship)
    {
        var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteChild(clientRequest, relationshipId);
    },
    removeAllMdParameter : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteChildren(clientRequest, this.getId(), 'com.runwaysdk.system.MetadataParameter');
    },
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MetadataQueryDTO', {
  Extends : 'com.runwaysdk.business.BusinessQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.Metadata'
  },
  Instance: 
  {
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdType', {
  Extends : 'com.runwaysdk.system.metadata.Metadata',
  IsAbstract : true,
  Constants : 
  {
    BASECLASS : 'baseClass',
    BASESOURCE : 'baseSource',
    DISPLAYLABEL : 'displayLabel',
    DTOCLASS : 'dtoClass',
    DTOSOURCE : 'dtoSource',
    EXPORTED : 'exported',
    JSBASE : 'jsBase',
    JSSTUB : 'jsStub',
    PACKAGENAME : 'packageName',
    ROOTID : 'rootId',
    TYPENAME : 'typeName',
    CLASS : 'com.runwaysdk.system.metadata.MdType'
  },
  Instance: 
  {
    getBaseSource : function()
    {
      return this.getAttributeDTO('baseSource').getValue();
    },
    isBaseSourceReadable : function()
    {
      return this.getAttributeDTO('baseSource').isReadable();
    },
    isBaseSourceWritable : function()
    {
      return this.getAttributeDTO('baseSource').isWritable();
    },
    isBaseSourceModified : function()
    {
      return this.getAttributeDTO('baseSource').isModified();
    },
    getBaseSourceMd : function()
    {
      return this.getAttributeDTO('baseSource').getAttributeMdDTO();
    },
    getDisplayLabel : function()
    {
      if(Mojo.Meta.classExists('com.runwaysdk.system.metadata.MetadataDisplayLabel'))
      {
        var structDTO = this.getAttributeDTO('displayLabel').getStructDTO();
        if(structDTO == null)
        {
          return null;
        }
        else if(structDTO instanceof Mojo.$.com.runwaysdk.system.metadata.MetadataDisplayLabel)
        {
          return structDTO;
        }
        else
        {
          structDTO = new Mojo.$.com.runwaysdk.system.metadata.MetadataDisplayLabel(structDTO);
          this.getAttributeDTO('displayLabel').setStructDTO(structDTO);
          return structDTO;
        }
      }
      else
      {
        throw new Mojo.$.com.runwaysdk.Exception('Must import type com.runwaysdk.system.metadata.MetadataDisplayLabel');
      }
    },
    isDisplayLabelReadable : function()
    {
      return this.getAttributeDTO('displayLabel').isReadable();
    },
    isDisplayLabelWritable : function()
    {
      return this.getAttributeDTO('displayLabel').isWritable();
    },
    isDisplayLabelModified : function()
    {
      return this.getAttributeDTO('displayLabel').isModified();
    },
    getDisplayLabelMd : function()
    {
      return this.getAttributeDTO('displayLabel').getAttributeMdDTO();
    },
    getDtoSource : function()
    {
      return this.getAttributeDTO('dtoSource').getValue();
    },
    isDtoSourceReadable : function()
    {
      return this.getAttributeDTO('dtoSource').isReadable();
    },
    isDtoSourceWritable : function()
    {
      return this.getAttributeDTO('dtoSource').isWritable();
    },
    isDtoSourceModified : function()
    {
      return this.getAttributeDTO('dtoSource').isModified();
    },
    getDtoSourceMd : function()
    {
      return this.getAttributeDTO('dtoSource').getAttributeMdDTO();
    },
    getExported : function()
    {
      return this.getAttributeDTO('exported').getValue();
    },
    setExported : function(value)
    {
      var attributeDTO = this.getAttributeDTO('exported');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isExportedReadable : function()
    {
      return this.getAttributeDTO('exported').isReadable();
    },
    isExportedWritable : function()
    {
      return this.getAttributeDTO('exported').isWritable();
    },
    isExportedModified : function()
    {
      return this.getAttributeDTO('exported').isModified();
    },
    getExportedMd : function()
    {
      return this.getAttributeDTO('exported').getAttributeMdDTO();
    },
    getJsBase : function()
    {
      return this.getAttributeDTO('jsBase').getValue();
    },
    setJsBase : function(value)
    {
      var attributeDTO = this.getAttributeDTO('jsBase');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isJsBaseReadable : function()
    {
      return this.getAttributeDTO('jsBase').isReadable();
    },
    isJsBaseWritable : function()
    {
      return this.getAttributeDTO('jsBase').isWritable();
    },
    isJsBaseModified : function()
    {
      return this.getAttributeDTO('jsBase').isModified();
    },
    getJsBaseMd : function()
    {
      return this.getAttributeDTO('jsBase').getAttributeMdDTO();
    },
    getJsStub : function()
    {
      return this.getAttributeDTO('jsStub').getValue();
    },
    setJsStub : function(value)
    {
      var attributeDTO = this.getAttributeDTO('jsStub');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isJsStubReadable : function()
    {
      return this.getAttributeDTO('jsStub').isReadable();
    },
    isJsStubWritable : function()
    {
      return this.getAttributeDTO('jsStub').isWritable();
    },
    isJsStubModified : function()
    {
      return this.getAttributeDTO('jsStub').isModified();
    },
    getJsStubMd : function()
    {
      return this.getAttributeDTO('jsStub').getAttributeMdDTO();
    },
    getPackageName : function()
    {
      return this.getAttributeDTO('packageName').getValue();
    },
    setPackageName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('packageName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPackageNameReadable : function()
    {
      return this.getAttributeDTO('packageName').isReadable();
    },
    isPackageNameWritable : function()
    {
      return this.getAttributeDTO('packageName').isWritable();
    },
    isPackageNameModified : function()
    {
      return this.getAttributeDTO('packageName').isModified();
    },
    getPackageNameMd : function()
    {
      return this.getAttributeDTO('packageName').getAttributeMdDTO();
    },
    isRootIdReadable : function()
    {
      return this.getAttributeDTO('rootId').isReadable();
    },
    isRootIdWritable : function()
    {
      return this.getAttributeDTO('rootId').isWritable();
    },
    isRootIdModified : function()
    {
      return this.getAttributeDTO('rootId').isModified();
    },
    getRootIdMd : function()
    {
      return this.getAttributeDTO('rootId').getAttributeMdDTO();
    },
    getTypeName : function()
    {
      return this.getAttributeDTO('typeName').getValue();
    },
    setTypeName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('typeName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isTypeNameReadable : function()
    {
      return this.getAttributeDTO('typeName').isReadable();
    },
    isTypeNameWritable : function()
    {
      return this.getAttributeDTO('typeName').isWritable();
    },
    isTypeNameModified : function()
    {
      return this.getAttributeDTO('typeName').isModified();
    },
    getTypeNameMd : function()
    {
      return this.getAttributeDTO('typeName').getAttributeMdDTO();
    },
    getAllMdMethod : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildren(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.TypeMethod');
    },
    getAllMdMethodRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.TypeMethod');
    },
    addMdMethod : function(clientRequest, child)
    {
      var childId = (child instanceof Object) ? child.getId() : child;
      Mojo.$.com.runwaysdk.Facade.addChild(clientRequest, this.getId(), childId, 'com.runwaysdk.system.metadata.TypeMethod');
    },
    removeMdMethod : function(clientRequest, relationship)
    {
        var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteChild(clientRequest, relationshipId);
    },
    removeAllMdMethod : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteChildren(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.TypeMethod');
    },
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdTypeQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MetadataQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdType'
  },
  Instance: 
  {
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdClass', {
  Extends : 'com.runwaysdk.system.metadata.MdType',
  IsAbstract : true,
  Constants : 
  {
    PUBLISH : 'publish',
    STUBCLASS : 'stubClass',
    STUBDTOCLASS : 'stubDTOclass',
    STUBDTOSOURCE : 'stubDTOsource',
    STUBSOURCE : 'stubSource',
    CLASS : 'com.runwaysdk.system.metadata.MdClass'
  },
  Instance: 
  {
    getPublish : function()
    {
      return this.getAttributeDTO('publish').getValue();
    },
    setPublish : function(value)
    {
      var attributeDTO = this.getAttributeDTO('publish');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPublishReadable : function()
    {
      return this.getAttributeDTO('publish').isReadable();
    },
    isPublishWritable : function()
    {
      return this.getAttributeDTO('publish').isWritable();
    },
    isPublishModified : function()
    {
      return this.getAttributeDTO('publish').isModified();
    },
    getPublishMd : function()
    {
      return this.getAttributeDTO('publish').getAttributeMdDTO();
    },
    getStubDTOsource : function()
    {
      return this.getAttributeDTO('stubDTOsource').getValue();
    },
    setStubDTOsource : function(value)
    {
      var attributeDTO = this.getAttributeDTO('stubDTOsource');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isStubDTOsourceReadable : function()
    {
      return this.getAttributeDTO('stubDTOsource').isReadable();
    },
    isStubDTOsourceWritable : function()
    {
      return this.getAttributeDTO('stubDTOsource').isWritable();
    },
    isStubDTOsourceModified : function()
    {
      return this.getAttributeDTO('stubDTOsource').isModified();
    },
    getStubDTOsourceMd : function()
    {
      return this.getAttributeDTO('stubDTOsource').getAttributeMdDTO();
    },
    getStubSource : function()
    {
      return this.getAttributeDTO('stubSource').getValue();
    },
    setStubSource : function(value)
    {
      var attributeDTO = this.getAttributeDTO('stubSource');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isStubSourceReadable : function()
    {
      return this.getAttributeDTO('stubSource').isReadable();
    },
    isStubSourceWritable : function()
    {
      return this.getAttributeDTO('stubSource').isWritable();
    },
    isStubSourceModified : function()
    {
      return this.getAttributeDTO('stubSource').isModified();
    },
    getStubSourceMd : function()
    {
      return this.getAttributeDTO('stubSource').getAttributeMdDTO();
    },
    getAllInheritsFromEntity : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassInheritance');
    },
    getAllInheritsFromEntityRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParentRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassInheritance');
    },
    addInheritsFromEntity : function(clientRequest, parent)
    {
      var parentId = (parent instanceof Object) ? parent.getId() : parent;
      Mojo.$.com.runwaysdk.Facade.addParent(clientRequest, parentId, this.getId(), 'com.runwaysdk.system.metadata.ClassInheritance');
    },
    removeInheritsFromEntity : function(clientRequest, relationship)
    {
      var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteParent(clientRequest, relationshipId);
    },
    removeAllInheritsFromEntity : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassInheritance');
    },
    getAllAttribute : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildren(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassAttribute');
    },
    getAllAttributeRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassAttribute');
    },
    addAttribute : function(clientRequest, child)
    {
      var childId = (child instanceof Object) ? child.getId() : child;
      Mojo.$.com.runwaysdk.Facade.addChild(clientRequest, this.getId(), childId, 'com.runwaysdk.system.metadata.ClassAttribute');
    },
    removeAttribute : function(clientRequest, relationship)
    {
        var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteChild(clientRequest, relationshipId);
    },
    removeAllAttribute : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteChildren(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassAttribute');
    },
    getAllConcreteAttribute : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildren(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassAttributeConcrete');
    },
    getAllConcreteAttributeRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassAttributeConcrete');
    },
    addConcreteAttribute : function(clientRequest, child)
    {
      var childId = (child instanceof Object) ? child.getId() : child;
      Mojo.$.com.runwaysdk.Facade.addChild(clientRequest, this.getId(), childId, 'com.runwaysdk.system.metadata.ClassAttributeConcrete');
    },
    removeConcreteAttribute : function(clientRequest, relationship)
    {
        var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteChild(clientRequest, relationshipId);
    },
    removeAllConcreteAttribute : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteChildren(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassAttributeConcrete');
    },
    getAllMdClassDimensions : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildren(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassHasDimension');
    },
    getAllMdClassDimensionsRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassHasDimension');
    },
    addMdClassDimensions : function(clientRequest, child)
    {
      var childId = (child instanceof Object) ? child.getId() : child;
      Mojo.$.com.runwaysdk.Facade.addChild(clientRequest, this.getId(), childId, 'com.runwaysdk.system.metadata.ClassHasDimension');
    },
    removeMdClassDimensions : function(clientRequest, relationship)
    {
        var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteChild(clientRequest, relationshipId);
    },
    removeAllMdClassDimensions : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteChildren(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassHasDimension');
    },
    getAllSubEntity : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildren(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassInheritance');
    },
    getAllSubEntityRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassInheritance');
    },
    addSubEntity : function(clientRequest, child)
    {
      var childId = (child instanceof Object) ? child.getId() : child;
      Mojo.$.com.runwaysdk.Facade.addChild(clientRequest, this.getId(), childId, 'com.runwaysdk.system.metadata.ClassInheritance');
    },
    removeSubEntity : function(clientRequest, relationship)
    {
        var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteChild(clientRequest, relationshipId);
    },
    removeAllSubEntity : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteChildren(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassInheritance');
    },
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdClassQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MdTypeQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdClass'
  },
  Instance: 
  {
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdEntity', {
  Extends : 'com.runwaysdk.system.metadata.MdClass',
  IsAbstract : true,
  Constants : 
  {
    CACHESIZE : 'cacheSize',
    ENFORCESITEMASTER : 'enforceSiteMaster',
    HASDETERMINISTICIDS : 'hasDeterministicIds',
    QUERYCLASS : 'queryClass',
    QUERYDTOCLASS : 'queryDTOclass',
    QUERYDTOSOURCE : 'queryDTOsource',
    QUERYSOURCE : 'querySource',
    TABLENAME : 'tableName',
    CLASS : 'com.runwaysdk.system.metadata.MdEntity'
  },
  Instance: 
  {
    getCacheSize : function()
    {
      return this.getAttributeDTO('cacheSize').getValue();
    },
    setCacheSize : function(value)
    {
      var attributeDTO = this.getAttributeDTO('cacheSize');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isCacheSizeReadable : function()
    {
      return this.getAttributeDTO('cacheSize').isReadable();
    },
    isCacheSizeWritable : function()
    {
      return this.getAttributeDTO('cacheSize').isWritable();
    },
    isCacheSizeModified : function()
    {
      return this.getAttributeDTO('cacheSize').isModified();
    },
    getCacheSizeMd : function()
    {
      return this.getAttributeDTO('cacheSize').getAttributeMdDTO();
    },
    getEnforceSiteMaster : function()
    {
      return this.getAttributeDTO('enforceSiteMaster').getValue();
    },
    setEnforceSiteMaster : function(value)
    {
      var attributeDTO = this.getAttributeDTO('enforceSiteMaster');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isEnforceSiteMasterReadable : function()
    {
      return this.getAttributeDTO('enforceSiteMaster').isReadable();
    },
    isEnforceSiteMasterWritable : function()
    {
      return this.getAttributeDTO('enforceSiteMaster').isWritable();
    },
    isEnforceSiteMasterModified : function()
    {
      return this.getAttributeDTO('enforceSiteMaster').isModified();
    },
    getEnforceSiteMasterMd : function()
    {
      return this.getAttributeDTO('enforceSiteMaster').getAttributeMdDTO();
    },
    getHasDeterministicIds : function()
    {
      return this.getAttributeDTO('hasDeterministicIds').getValue();
    },
    setHasDeterministicIds : function(value)
    {
      var attributeDTO = this.getAttributeDTO('hasDeterministicIds');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isHasDeterministicIdsReadable : function()
    {
      return this.getAttributeDTO('hasDeterministicIds').isReadable();
    },
    isHasDeterministicIdsWritable : function()
    {
      return this.getAttributeDTO('hasDeterministicIds').isWritable();
    },
    isHasDeterministicIdsModified : function()
    {
      return this.getAttributeDTO('hasDeterministicIds').isModified();
    },
    getHasDeterministicIdsMd : function()
    {
      return this.getAttributeDTO('hasDeterministicIds').getAttributeMdDTO();
    },
    getQueryDTOsource : function()
    {
      return this.getAttributeDTO('queryDTOsource').getValue();
    },
    isQueryDTOsourceReadable : function()
    {
      return this.getAttributeDTO('queryDTOsource').isReadable();
    },
    isQueryDTOsourceWritable : function()
    {
      return this.getAttributeDTO('queryDTOsource').isWritable();
    },
    isQueryDTOsourceModified : function()
    {
      return this.getAttributeDTO('queryDTOsource').isModified();
    },
    getQueryDTOsourceMd : function()
    {
      return this.getAttributeDTO('queryDTOsource').getAttributeMdDTO();
    },
    getQuerySource : function()
    {
      return this.getAttributeDTO('querySource').getValue();
    },
    isQuerySourceReadable : function()
    {
      return this.getAttributeDTO('querySource').isReadable();
    },
    isQuerySourceWritable : function()
    {
      return this.getAttributeDTO('querySource').isWritable();
    },
    isQuerySourceModified : function()
    {
      return this.getAttributeDTO('querySource').isModified();
    },
    getQuerySourceMd : function()
    {
      return this.getAttributeDTO('querySource').getAttributeMdDTO();
    },
    getTableName : function()
    {
      return this.getAttributeDTO('tableName').getValue();
    },
    setTableName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('tableName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isTableNameReadable : function()
    {
      return this.getAttributeDTO('tableName').isReadable();
    },
    isTableNameWritable : function()
    {
      return this.getAttributeDTO('tableName').isWritable();
    },
    isTableNameModified : function()
    {
      return this.getAttributeDTO('tableName').isModified();
    },
    getTableNameMd : function()
    {
      return this.getAttributeDTO('tableName').getAttributeMdDTO();
    },
    getAllIndex : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildren(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.EntityIndex');
    },
    getAllIndexRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.EntityIndex');
    },
    addIndex : function(clientRequest, child)
    {
      var childId = (child instanceof Object) ? child.getId() : child;
      Mojo.$.com.runwaysdk.Facade.addChild(clientRequest, this.getId(), childId, 'com.runwaysdk.system.metadata.EntityIndex');
    },
    removeIndex : function(clientRequest, relationship)
    {
        var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteChild(clientRequest, relationshipId);
    },
    removeAllIndex : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteChildren(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.EntityIndex');
    },
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdEntityQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MdClassQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdEntity'
  },
  Instance: 
  {
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
