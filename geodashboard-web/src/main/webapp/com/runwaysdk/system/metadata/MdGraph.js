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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdElement', {
  Extends : 'com.runwaysdk.system.metadata.MdEntity',
  IsAbstract : true,
  Constants : 
  {
    EXTENDABLE : 'extendable',
    ISABSTRACT : 'isAbstract',
    CLASS : 'com.runwaysdk.system.metadata.MdElement'
  },
  Instance: 
  {
    getExtendable : function()
    {
      return this.getAttributeDTO('extendable').getValue();
    },
    setExtendable : function(value)
    {
      var attributeDTO = this.getAttributeDTO('extendable');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isExtendableReadable : function()
    {
      return this.getAttributeDTO('extendable').isReadable();
    },
    isExtendableWritable : function()
    {
      return this.getAttributeDTO('extendable').isWritable();
    },
    isExtendableModified : function()
    {
      return this.getAttributeDTO('extendable').isModified();
    },
    getExtendableMd : function()
    {
      return this.getAttributeDTO('extendable').getAttributeMdDTO();
    },
    getIsAbstract : function()
    {
      return this.getAttributeDTO('isAbstract').getValue();
    },
    setIsAbstract : function(value)
    {
      var attributeDTO = this.getAttributeDTO('isAbstract');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isIsAbstractReadable : function()
    {
      return this.getAttributeDTO('isAbstract').isReadable();
    },
    isIsAbstractWritable : function()
    {
      return this.getAttributeDTO('isAbstract').isWritable();
    },
    isIsAbstractModified : function()
    {
      return this.getAttributeDTO('isAbstract').isModified();
    },
    getIsAbstractMd : function()
    {
      return this.getAttributeDTO('isAbstract').getAttributeMdDTO();
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdElementQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MdEntityQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdElement'
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdRelationship', {
  Extends : 'com.runwaysdk.system.metadata.MdElement',
  Constants : 
  {
    CACHEALGORITHM : 'cacheAlgorithm',
    CHILDCARDINALITY : 'childCardinality',
    CHILDDISPLAYLABEL : 'childDisplayLabel',
    CHILDMDBUSINESS : 'childMdBusiness',
    CHILDMETHOD : 'childMethod',
    CHILDVISIBILITY : 'childVisibility',
    COMPOSITION : 'composition',
    INDEX1NAME : 'index1Name',
    INDEX2NAME : 'index2Name',
    PARENTCARDINALITY : 'parentCardinality',
    PARENTDISPLAYLABEL : 'parentDisplayLabel',
    PARENTMDBUSINESS : 'parentMdBusiness',
    PARENTMETHOD : 'parentMethod',
    PARENTVISIBILITY : 'parentVisibility',
    SORTMDATTRIBUTE : 'sortMdAttribute',
    SUPERMDRELATIONSHIP : 'superMdRelationship',
    CLASS : 'com.runwaysdk.system.metadata.MdRelationship'
  },
  Instance: 
  {
    getCacheAlgorithm : function()
    {
      var attributeDTO = this.getAttributeDTO('cacheAlgorithm');
      var names = attributeDTO.getEnumNames();
      var enums = [];
      for(var i=0; i<names.length; i++)
      {
        enums.push(Mojo.$.com.runwaysdk.system.metadata.RelationshipCache[names[i]]);
      }
      return enums;
    },
    removeCacheAlgorithm : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('cacheAlgorithm');
      attributeDTO.remove(enumValue);
      this.setModified(true);
    },
    clearCacheAlgorithm : function()
    {
      var attributeDTO = this.getAttributeDTO('cacheAlgorithm');
      attributeDTO.clear();
      this.setModified(true);
    },
    addCacheAlgorithm : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('cacheAlgorithm');
      attributeDTO.add(enumValue);
      this.setModified(true);
    },
    isCacheAlgorithmReadable : function()
    {
      return this.getAttributeDTO('cacheAlgorithm').isReadable();
    },
    isCacheAlgorithmWritable : function()
    {
      return this.getAttributeDTO('cacheAlgorithm').isWritable();
    },
    isCacheAlgorithmModified : function()
    {
      return this.getAttributeDTO('cacheAlgorithm').isModified();
    },
    getCacheAlgorithmMd : function()
    {
      return this.getAttributeDTO('cacheAlgorithm').getAttributeMdDTO();
    },
    getChildCardinality : function()
    {
      return this.getAttributeDTO('childCardinality').getValue();
    },
    setChildCardinality : function(value)
    {
      var attributeDTO = this.getAttributeDTO('childCardinality');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isChildCardinalityReadable : function()
    {
      return this.getAttributeDTO('childCardinality').isReadable();
    },
    isChildCardinalityWritable : function()
    {
      return this.getAttributeDTO('childCardinality').isWritable();
    },
    isChildCardinalityModified : function()
    {
      return this.getAttributeDTO('childCardinality').isModified();
    },
    getChildCardinalityMd : function()
    {
      return this.getAttributeDTO('childCardinality').getAttributeMdDTO();
    },
    getChildDisplayLabel : function()
    {
      if(Mojo.Meta.classExists('com.runwaysdk.system.metadata.MetadataDisplayLabel'))
      {
        var structDTO = this.getAttributeDTO('childDisplayLabel').getStructDTO();
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
          this.getAttributeDTO('childDisplayLabel').setStructDTO(structDTO);
          return structDTO;
        }
      }
      else
      {
        throw new Mojo.$.com.runwaysdk.Exception('Must import type com.runwaysdk.system.metadata.MetadataDisplayLabel');
      }
    },
    isChildDisplayLabelReadable : function()
    {
      return this.getAttributeDTO('childDisplayLabel').isReadable();
    },
    isChildDisplayLabelWritable : function()
    {
      return this.getAttributeDTO('childDisplayLabel').isWritable();
    },
    isChildDisplayLabelModified : function()
    {
      return this.getAttributeDTO('childDisplayLabel').isModified();
    },
    getChildDisplayLabelMd : function()
    {
      return this.getAttributeDTO('childDisplayLabel').getAttributeMdDTO();
    },
    getChildMdBusiness : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('childMdBusiness');
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
    setChildMdBusiness : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('childMdBusiness');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isChildMdBusinessReadable : function()
    {
      return this.getAttributeDTO('childMdBusiness').isReadable();
    },
    isChildMdBusinessWritable : function()
    {
      return this.getAttributeDTO('childMdBusiness').isWritable();
    },
    isChildMdBusinessModified : function()
    {
      return this.getAttributeDTO('childMdBusiness').isModified();
    },
    getChildMdBusinessMd : function()
    {
      return this.getAttributeDTO('childMdBusiness').getAttributeMdDTO();
    },
    getChildMethod : function()
    {
      return this.getAttributeDTO('childMethod').getValue();
    },
    setChildMethod : function(value)
    {
      var attributeDTO = this.getAttributeDTO('childMethod');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isChildMethodReadable : function()
    {
      return this.getAttributeDTO('childMethod').isReadable();
    },
    isChildMethodWritable : function()
    {
      return this.getAttributeDTO('childMethod').isWritable();
    },
    isChildMethodModified : function()
    {
      return this.getAttributeDTO('childMethod').isModified();
    },
    getChildMethodMd : function()
    {
      return this.getAttributeDTO('childMethod').getAttributeMdDTO();
    },
    getChildVisibility : function()
    {
      var attributeDTO = this.getAttributeDTO('childVisibility');
      var names = attributeDTO.getEnumNames();
      var enums = [];
      for(var i=0; i<names.length; i++)
      {
        enums.push(Mojo.$.com.runwaysdk.system.metadata.VisibilityModifier[names[i]]);
      }
      return enums;
    },
    removeChildVisibility : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('childVisibility');
      attributeDTO.remove(enumValue);
      this.setModified(true);
    },
    clearChildVisibility : function()
    {
      var attributeDTO = this.getAttributeDTO('childVisibility');
      attributeDTO.clear();
      this.setModified(true);
    },
    addChildVisibility : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('childVisibility');
      attributeDTO.add(enumValue);
      this.setModified(true);
    },
    isChildVisibilityReadable : function()
    {
      return this.getAttributeDTO('childVisibility').isReadable();
    },
    isChildVisibilityWritable : function()
    {
      return this.getAttributeDTO('childVisibility').isWritable();
    },
    isChildVisibilityModified : function()
    {
      return this.getAttributeDTO('childVisibility').isModified();
    },
    getChildVisibilityMd : function()
    {
      return this.getAttributeDTO('childVisibility').getAttributeMdDTO();
    },
    getComposition : function()
    {
      return this.getAttributeDTO('composition').getValue();
    },
    setComposition : function(value)
    {
      var attributeDTO = this.getAttributeDTO('composition');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isCompositionReadable : function()
    {
      return this.getAttributeDTO('composition').isReadable();
    },
    isCompositionWritable : function()
    {
      return this.getAttributeDTO('composition').isWritable();
    },
    isCompositionModified : function()
    {
      return this.getAttributeDTO('composition').isModified();
    },
    getCompositionMd : function()
    {
      return this.getAttributeDTO('composition').getAttributeMdDTO();
    },
    getIndex1Name : function()
    {
      return this.getAttributeDTO('index1Name').getValue();
    },
    isIndex1NameReadable : function()
    {
      return this.getAttributeDTO('index1Name').isReadable();
    },
    isIndex1NameWritable : function()
    {
      return this.getAttributeDTO('index1Name').isWritable();
    },
    isIndex1NameModified : function()
    {
      return this.getAttributeDTO('index1Name').isModified();
    },
    getIndex1NameMd : function()
    {
      return this.getAttributeDTO('index1Name').getAttributeMdDTO();
    },
    getIndex2Name : function()
    {
      return this.getAttributeDTO('index2Name').getValue();
    },
    isIndex2NameReadable : function()
    {
      return this.getAttributeDTO('index2Name').isReadable();
    },
    isIndex2NameWritable : function()
    {
      return this.getAttributeDTO('index2Name').isWritable();
    },
    isIndex2NameModified : function()
    {
      return this.getAttributeDTO('index2Name').isModified();
    },
    getIndex2NameMd : function()
    {
      return this.getAttributeDTO('index2Name').getAttributeMdDTO();
    },
    getParentCardinality : function()
    {
      return this.getAttributeDTO('parentCardinality').getValue();
    },
    setParentCardinality : function(value)
    {
      var attributeDTO = this.getAttributeDTO('parentCardinality');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isParentCardinalityReadable : function()
    {
      return this.getAttributeDTO('parentCardinality').isReadable();
    },
    isParentCardinalityWritable : function()
    {
      return this.getAttributeDTO('parentCardinality').isWritable();
    },
    isParentCardinalityModified : function()
    {
      return this.getAttributeDTO('parentCardinality').isModified();
    },
    getParentCardinalityMd : function()
    {
      return this.getAttributeDTO('parentCardinality').getAttributeMdDTO();
    },
    getParentDisplayLabel : function()
    {
      if(Mojo.Meta.classExists('com.runwaysdk.system.metadata.MetadataDisplayLabel'))
      {
        var structDTO = this.getAttributeDTO('parentDisplayLabel').getStructDTO();
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
          this.getAttributeDTO('parentDisplayLabel').setStructDTO(structDTO);
          return structDTO;
        }
      }
      else
      {
        throw new Mojo.$.com.runwaysdk.Exception('Must import type com.runwaysdk.system.metadata.MetadataDisplayLabel');
      }
    },
    isParentDisplayLabelReadable : function()
    {
      return this.getAttributeDTO('parentDisplayLabel').isReadable();
    },
    isParentDisplayLabelWritable : function()
    {
      return this.getAttributeDTO('parentDisplayLabel').isWritable();
    },
    isParentDisplayLabelModified : function()
    {
      return this.getAttributeDTO('parentDisplayLabel').isModified();
    },
    getParentDisplayLabelMd : function()
    {
      return this.getAttributeDTO('parentDisplayLabel').getAttributeMdDTO();
    },
    getParentMdBusiness : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('parentMdBusiness');
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
    setParentMdBusiness : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('parentMdBusiness');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isParentMdBusinessReadable : function()
    {
      return this.getAttributeDTO('parentMdBusiness').isReadable();
    },
    isParentMdBusinessWritable : function()
    {
      return this.getAttributeDTO('parentMdBusiness').isWritable();
    },
    isParentMdBusinessModified : function()
    {
      return this.getAttributeDTO('parentMdBusiness').isModified();
    },
    getParentMdBusinessMd : function()
    {
      return this.getAttributeDTO('parentMdBusiness').getAttributeMdDTO();
    },
    getParentMethod : function()
    {
      return this.getAttributeDTO('parentMethod').getValue();
    },
    setParentMethod : function(value)
    {
      var attributeDTO = this.getAttributeDTO('parentMethod');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isParentMethodReadable : function()
    {
      return this.getAttributeDTO('parentMethod').isReadable();
    },
    isParentMethodWritable : function()
    {
      return this.getAttributeDTO('parentMethod').isWritable();
    },
    isParentMethodModified : function()
    {
      return this.getAttributeDTO('parentMethod').isModified();
    },
    getParentMethodMd : function()
    {
      return this.getAttributeDTO('parentMethod').getAttributeMdDTO();
    },
    getParentVisibility : function()
    {
      var attributeDTO = this.getAttributeDTO('parentVisibility');
      var names = attributeDTO.getEnumNames();
      var enums = [];
      for(var i=0; i<names.length; i++)
      {
        enums.push(Mojo.$.com.runwaysdk.system.metadata.VisibilityModifier[names[i]]);
      }
      return enums;
    },
    removeParentVisibility : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('parentVisibility');
      attributeDTO.remove(enumValue);
      this.setModified(true);
    },
    clearParentVisibility : function()
    {
      var attributeDTO = this.getAttributeDTO('parentVisibility');
      attributeDTO.clear();
      this.setModified(true);
    },
    addParentVisibility : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('parentVisibility');
      attributeDTO.add(enumValue);
      this.setModified(true);
    },
    isParentVisibilityReadable : function()
    {
      return this.getAttributeDTO('parentVisibility').isReadable();
    },
    isParentVisibilityWritable : function()
    {
      return this.getAttributeDTO('parentVisibility').isWritable();
    },
    isParentVisibilityModified : function()
    {
      return this.getAttributeDTO('parentVisibility').isModified();
    },
    getParentVisibilityMd : function()
    {
      return this.getAttributeDTO('parentVisibility').getAttributeMdDTO();
    },
    getSortMdAttribute : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('sortMdAttribute');
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
    setSortMdAttribute : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('sortMdAttribute');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isSortMdAttributeReadable : function()
    {
      return this.getAttributeDTO('sortMdAttribute').isReadable();
    },
    isSortMdAttributeWritable : function()
    {
      return this.getAttributeDTO('sortMdAttribute').isWritable();
    },
    isSortMdAttributeModified : function()
    {
      return this.getAttributeDTO('sortMdAttribute').isModified();
    },
    getSortMdAttributeMd : function()
    {
      return this.getAttributeDTO('sortMdAttribute').getAttributeMdDTO();
    },
    getSuperMdRelationship : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('superMdRelationship');
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
    setSuperMdRelationship : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('superMdRelationship');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isSuperMdRelationshipReadable : function()
    {
      return this.getAttributeDTO('superMdRelationship').isReadable();
    },
    isSuperMdRelationshipWritable : function()
    {
      return this.getAttributeDTO('superMdRelationship').isWritable();
    },
    isSuperMdRelationshipModified : function()
    {
      return this.getAttributeDTO('superMdRelationship').isModified();
    },
    getSuperMdRelationshipMd : function()
    {
      return this.getAttributeDTO('superMdRelationship').getAttributeMdDTO();
    },
    getAllInheritsFromRelationship : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.RelationshipInheritance');
    },
    getAllInheritsFromRelationshipRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParentRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.RelationshipInheritance');
    },
    addInheritsFromRelationship : function(clientRequest, parent)
    {
      var parentId = (parent instanceof Object) ? parent.getId() : parent;
      Mojo.$.com.runwaysdk.Facade.addParent(clientRequest, parentId, this.getId(), 'com.runwaysdk.system.metadata.RelationshipInheritance');
    },
    removeInheritsFromRelationship : function(clientRequest, relationship)
    {
      var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteParent(clientRequest, relationshipId);
    },
    removeAllInheritsFromRelationship : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.RelationshipInheritance');
    },
    getAllSubRelationship : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildren(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.RelationshipInheritance');
    },
    getAllSubRelationshipRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.RelationshipInheritance');
    },
    addSubRelationship : function(clientRequest, child)
    {
      var childId = (child instanceof Object) ? child.getId() : child;
      Mojo.$.com.runwaysdk.Facade.addChild(clientRequest, this.getId(), childId, 'com.runwaysdk.system.metadata.RelationshipInheritance');
    },
    removeSubRelationship : function(clientRequest, relationship)
    {
        var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteChild(clientRequest, relationshipId);
    },
    removeAllSubRelationship : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteChildren(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.RelationshipInheritance');
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"readable\":true,\"dto_type\":\"com.runwaysdk.business.BusinessDTO\",\"attributeMap\":{\"lockedBy\":{\"attributeName\":\"lockedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000008100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Locked By\",\"description\":\"User who has a write lock on this object\",\"name\":\"lockedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.Users\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"packageName\":{\"attributeName\":\"packageName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000034900000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Package Name\",\"description\":\"Name of the package for this type\",\"name\":\"packageName\",\"immutable\":true,\"required\":true,\"size\":127},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"exported\":{\"attributeName\":\"exported\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"EG20100125000000000000000000000100000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Exported\",\"negativeDisplayLabel\":\"Not Exported\",\"description\":\"Indicates if this type is being exported through synchronization\",\"name\":\"exported\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Exported\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"jsBase\":{\"attributeName\":\"jsBase\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"sx1bsc4gxk1lrjkl544m7fak7sg2t1j820101231NM0000000000000000000010\",\"system\":false,\"displayLabel\":\"JavaScript Base Source\",\"description\":\"\",\"name\":\"jsBase\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"entityDomain\":{\"attributeName\":\"entityDomain\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"NM20081011000000000000000000006200000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Entity Domain\",\"description\":\"The Domain of this entity\",\"name\":\"entityDomain\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdDomain\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"type\":{\"attributeName\":\"type\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000005100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"type\",\"description\":\"Fully qualified type of this object\",\"name\":\"type\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"com.runwaysdk.system.metadata.MdRelationship\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"extendable\":{\"attributeName\":\"extendable\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000003700000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Extendable\",\"negativeDisplayLabel\":\"Not Extendable\",\"description\":\"If set then this class can be extended by a subclass\",\"name\":\"extendable\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Extendable\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"cacheAlgorithm\":{\"attributeName\":\"cacheAlgorithm\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"20071004NM000000000000000000000600000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.metadata.RelationshipCache\",\"system\":false,\"displayLabel\":\"Cache Algorithm\",\"description\":\"Relationship Caching Algorithm\",\"name\":\"cacheAlgorithm\",\"immutable\":false,\"enumNames\":{\"CACHE_HARDCODED\":\"Cache Hardcoded\",\"CACHE_EVERYTHING\":\"Cache Everything\",\"CACHE_NOTHING\":\"Cache Nothing\"},\"_selectMultiple\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"enumNames\":[\"CACHE_NOTHING\"],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":true},\"isAbstract\":{\"attributeName\":\"isAbstract\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000003800000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Abstract\",\"negativeDisplayLabel\":\"Concrete\",\"description\":\"If set then instances of this class cannot be instantiated\",\"name\":\"isAbstract\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Abstract\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"dtoSource\":{\"attributeName\":\"dtoSource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"NM20070418000000000000000000004020101231NM0000000000000000000010\",\"system\":true,\"displayLabel\":\"DTO Source\",\"description\":\"The DTO source of the MdType\",\"name\":\"dtoSource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"JS20070104000000000000000000120000000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"description\":{\"attributeName\":\"description\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20090518NM00000000000000000000090000000MdAttributeLocalCharacter\",\"system\":false,\"displayLabel\":\"Description\",\"description\":\"Provides a description of the metadata that can be provided to the end user\",\"definingMdStruct\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"name\":\"description\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"8gk0j62urbogo48cqyqb5hpqjkpbokwwNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"8gk0j62urbogo48cqyqb5hpqjkpbokwwNM200904120000000000000000000030\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002900000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"8gk0j62urbogo48cqyqb5hpqjkpbokwwNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"defaultLocale\",\"description\":\"displayLabel for the default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"NM2009041200000000000000000000300000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"composition\":{\"attributeName\":\"composition\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000035300000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Composition\",\"negativeDisplayLabel\":\"Aggregation\",\"description\":\"If set then child objects in a relationship are deleted whenever the parent object is deleted.  This works like a database cascade delete on the parent object.\",\"name\":\"composition\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Composition\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"createdBy\":{\"attributeName\":\"createdBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000004100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Created By\",\"description\":\"User that created this object\",\"name\":\"createdBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"owner\":{\"attributeName\":\"owner\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000000100000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Owner\",\"description\":\"A reference to the user who own this object\",\"name\":\"owner\",\"referencedMdBusiness\":\"com.runwaysdk.system.Actor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"index1Name\":{\"attributeName\":\"index1Name\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20070427JS000000000000000000151600000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Index 1 Name\",\"description\":\"\",\"name\":\"index1Name\",\"immutable\":true,\"required\":true,\"size\":30},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"superMdRelationship\":{\"attributeName\":\"superMdRelationship\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20070926NM000000000000000000000100000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Super MdRelationship\",\"description\":\"Metadata of the parent relationship\",\"name\":\"superMdRelationship\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdRelationship\",\"immutable\":true,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"cacheSize\":{\"attributeName\":\"cacheSize\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000036700000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Cache Size\",\"description\":\"\",\"name\":\"cacheSize\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":true},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"jsStub\":{\"attributeName\":\"jsStub\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"6ozempoo35sdmp69nkuagmeb0kejpe5r20101231NM0000000000000000000010\",\"system\":false,\"displayLabel\":\"JavaScript Stub Source\",\"description\":\"\",\"name\":\"jsStub\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"displayLabel\":{\"attributeName\":\"displayLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM2009041200000000000000000000250000000MdAttributeLocalCharacter\",\"system\":false,\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\",\"definingMdStruct\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"name\":\"displayLabel\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"2hapdwmurensx8iq16vompc2jj9424b3NM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"2hapdwmurensx8iq16vompc2jj9424b3NM200904120000000000000000000030\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002900000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"2hapdwmurensx8iq16vompc2jj9424b3NM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"defaultLocale\",\"description\":\"displayLabel for the default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"NM2009041200000000000000000000300000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"remove\":{\"attributeName\":\"remove\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000003400000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Remove\",\"negativeDisplayLabel\":\"Can Remove\",\"description\":\"If set then this attribute cannot be dropped\",\"name\":\"remove\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Remove\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"childMethod\":{\"attributeName\":\"childMethod\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000095500000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Child Method\",\"description\":\"Generated classes for the parent type of this relationship have getters and setters to access their children.  This lets you custom define the names of the accessor methods.\",\"name\":\"childMethod\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"childCardinality\":{\"attributeName\":\"childCardinality\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000006700000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Child Cardinality\",\"description\":\"Maximum upperbound child cardinality on instances of this relationship\",\"name\":\"childCardinality\",\"immutable\":false,\"required\":true,\"size\":16},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"stubSource\":{\"attributeName\":\"stubSource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20070923NM000000000000000000000120101231NM0000000000000000000010\",\"system\":false,\"displayLabel\":\"Stub Source\",\"description\":\"\",\"name\":\"stubSource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"enforceSiteMaster\":{\"attributeName\":\"enforceSiteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"qlzuvymh2i7bewd3f61i5xdj03ysydou00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Enforce Site Master\",\"negativeDisplayLabel\":\"False\",\"description\":\"\",\"name\":\"enforceSiteMaster\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"True\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"parentMdBusiness\":{\"attributeName\":\"parentMdBusiness\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000006400000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Parent MdBusiness\",\"description\":\"Parent objects in this relationship must be instances of this class\",\"name\":\"parentMdBusiness\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdBusiness\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"index2Name\":{\"attributeName\":\"index2Name\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20070427JS000000000000000000151100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Index 2 Name\",\"description\":\"\",\"name\":\"index2Name\",\"immutable\":true,\"required\":true,\"size\":30},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000001100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"rbdtdlro6uypihfg7ckbpx7egu3d8vt200000000000000000000000000000061\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080227NM000000000000000000001900000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"parentCardinality\":{\"attributeName\":\"parentCardinality\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000006500000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Parent Cardinality\",\"description\":\"Maximum upperbound parent cardinality on instances of this relationship\",\"name\":\"parentCardinality\",\"immutable\":false,\"required\":true,\"size\":16},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"baseSource\":{\"attributeName\":\"baseSource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20070320EG000000000000000000145420101231NM0000000000000000000010\",\"system\":true,\"displayLabel\":\"Base Source\",\"description\":\"\",\"name\":\"baseSource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"tableName\":{\"attributeName\":\"tableName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20070919NM000000000000000000000100000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Table Name\",\"description\":\"Name of the table in the database that stores instances of this entity\",\"name\":\"tableName\",\"immutable\":true,\"required\":true,\"size\":31},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"publish\":{\"attributeName\":\"publish\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"NM20071105000000000000000000000100000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Publish\",\"negativeDisplayLabel\":\"Do not Publish\",\"description\":\"If set, then this class will be exposed outside the business layer with generated DTOs\",\"name\":\"publish\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Publish\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"childMdBusiness\":{\"attributeName\":\"childMdBusiness\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000006600000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Child MdBusiness\",\"description\":\"Child objects in this relationship must be instances of this class\",\"name\":\"childMdBusiness\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdBusiness\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"createDate\":{\"attributeName\":\"createDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000007100000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Create Date\",\"description\":\"Date and time this record was created\",\"name\":\"createDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"stubDTOsource\":{\"attributeName\":\"stubDTOsource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20070923NM000000000000000000000420101231NM0000000000000000000010\",\"system\":false,\"displayLabel\":\"Stub DTO Source\",\"description\":\"\",\"name\":\"stubDTOsource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"childVisibility\":{\"attributeName\":\"childVisibility\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"20071109NM000000000000000000001500000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.metadata.VisibilityModifier\",\"system\":false,\"displayLabel\":\"Child Visibility\",\"description\":\"\",\"name\":\"childVisibility\",\"immutable\":false,\"enumNames\":{\"PROTECTED\":\"Protected\",\"PUBLIC\":\"Public\"},\"_selectMultiple\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"enumNames\":[\"PUBLIC\"],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":true},\"parentMethod\":{\"attributeName\":\"parentMethod\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000095400000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Parent Method\",\"description\":\"Generated classes for the child type of this relationship have getters and setters to access their parents.  This lets you custom define the names of the accessor methods.\",\"name\":\"parentMethod\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"parentVisibility\":{\"attributeName\":\"parentVisibility\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"20071109NM000000000000000000001300000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.metadata.VisibilityModifier\",\"system\":false,\"displayLabel\":\"Parent Visibility\",\"description\":\"\",\"name\":\"parentVisibility\",\"immutable\":false,\"enumNames\":{\"PROTECTED\":\"Protected\",\"PUBLIC\":\"Public\"},\"_selectMultiple\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"enumNames\":[\"PUBLIC\"],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":true},\"hasDeterministicIds\":{\"attributeName\":\"hasDeterministicIds\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"isol1les55gorsb2reqwvu9wo85th34t00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Generate Deterministic IDs\",\"negativeDisplayLabel\":\"IDs are not deterministic\",\"description\":\"If set to TRUE, then IDs that are generated are deterministic, false otherwise. Deterministic IDs are generated from a hash of the KeyName value.\",\"name\":\"hasDeterministicIds\",\"immutable\":false,\"required\":false,\"positiveDisplayLabel\":\"IDs are deterministic\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"lastUpdateDate\":{\"attributeName\":\"lastUpdateDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000002100000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Last Update Date\",\"description\":\"Date and time this record was last updated\",\"name\":\"lastUpdateDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"queryDTOsource\":{\"attributeName\":\"queryDTOsource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20080408NM000000000000000000000120101231NM0000000000000000000010\",\"system\":true,\"displayLabel\":\"Query DTO Source\",\"description\":\"Attribute that stores the source code of the generated Query DTO class\",\"name\":\"queryDTOsource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"typeName\":{\"attributeName\":\"typeName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000003300000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Type Name\",\"description\":\"Name of the type\",\"name\":\"typeName\",\"immutable\":true,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"parentDisplayLabel\":{\"attributeName\":\"parentDisplayLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20090518NM00000000000000000000070000000MdAttributeLocalCharacter\",\"system\":false,\"displayLabel\":\"Display Label of Parent\",\"description\":\"Display label of the parent end of the relationship\",\"definingMdStruct\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"name\":\"parentDisplayLabel\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"8xo1741jriiti474xunsho65sysahsscNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"8xo1741jriiti474xunsho65sysahsscNM200904120000000000000000000030\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002900000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"8xo1741jriiti474xunsho65sysahsscNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"defaultLocale\",\"description\":\"displayLabel for the default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"NM2009041200000000000000000000300000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"childDisplayLabel\":{\"attributeName\":\"childDisplayLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20090518NM00000000000000000000080000000MdAttributeLocalCharacter\",\"system\":false,\"displayLabel\":\"Display Label of Child\",\"description\":\"Display label of the child end of the relationship\",\"definingMdStruct\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"name\":\"childDisplayLabel\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"xu4sw0xt7e8efpk8kjx4p51ibxnki7tgNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"xu4sw0xt7e8efpk8kjx4p51ibxnki7tgNM200904120000000000000000000030\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002900000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"xu4sw0xt7e8efpk8kjx4p51ibxnki7tgNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"defaultLocale\",\"description\":\"displayLabel for the default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"NM2009041200000000000000000000300000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"lastUpdatedBy\":{\"attributeName\":\"lastUpdatedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000003100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Last Updated By\",\"description\":\"User who last updated this object\",\"name\":\"lastUpdatedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"seq\":{\"attributeName\":\"seq\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000006100000000000000000000000000000338\",\"system\":true,\"displayLabel\":\"Sequence\",\"description\":\"Sequence number is incremented every time this object is updated\",\"name\":\"seq\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false},\"sortMdAttribute\":{\"attributeName\":\"sortMdAttribute\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000016600000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Sort MdAttribute\",\"description\":\"Default sort order of relationship queries will be sorted by this attribute\",\"name\":\"sortMdAttribute\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdAttributePrimitive\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"querySource\":{\"attributeName\":\"querySource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20070923NM000000000000000000001020101231NM0000000000000000000010\",\"system\":true,\"displayLabel\":\"Query Source\",\"description\":\"Stores the Java source for the query class\",\"name\":\"querySource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false}},\"_toString\":\"New: Relationship Meta Data\",\"writable\":true,\"state\":\"\",\"modified\":true,\"id\":\"rbdtdlro6uypihfg7ckbpx7egu3d8vt200000000000000000000000000000061\",\"_type\":\"com.runwaysdk.system.metadata.MdRelationship\",\"_typeMd\":{\"id\":\"0000000000000000000000000000006100000000000000000000000000000001\",\"displayLabel\":\"Relationship Meta Data\",\"description\":\"Defines Meta Data for a relationship type\"},\"transitions\":[],\"newInstance\":true,\"lockedByCurrentUser\":false}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdRelationshipQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MdElementQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdRelationship'
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdGraph', {
  Extends : 'com.runwaysdk.system.metadata.MdRelationship',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdGraph'
  },
  Instance: 
  {
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"readable\":true,\"dto_type\":\"com.runwaysdk.business.BusinessDTO\",\"attributeMap\":{\"lockedBy\":{\"attributeName\":\"lockedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000008100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Locked By\",\"description\":\"User who has a write lock on this object\",\"name\":\"lockedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.Users\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"packageName\":{\"attributeName\":\"packageName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000034900000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Package Name\",\"description\":\"Name of the package for this type\",\"name\":\"packageName\",\"immutable\":true,\"required\":true,\"size\":127},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"exported\":{\"attributeName\":\"exported\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"EG20100125000000000000000000000100000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Exported\",\"negativeDisplayLabel\":\"Not Exported\",\"description\":\"Indicates if this type is being exported through synchronization\",\"name\":\"exported\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Exported\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"jsBase\":{\"attributeName\":\"jsBase\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"sx1bsc4gxk1lrjkl544m7fak7sg2t1j820101231NM0000000000000000000010\",\"system\":false,\"displayLabel\":\"JavaScript Base Source\",\"description\":\"\",\"name\":\"jsBase\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"entityDomain\":{\"attributeName\":\"entityDomain\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"NM20081011000000000000000000006200000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Entity Domain\",\"description\":\"The Domain of this entity\",\"name\":\"entityDomain\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdDomain\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"type\":{\"attributeName\":\"type\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000005100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"type\",\"description\":\"Fully qualified type of this object\",\"name\":\"type\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"com.runwaysdk.system.metadata.MdGraph\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"extendable\":{\"attributeName\":\"extendable\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000003700000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Extendable\",\"negativeDisplayLabel\":\"Not Extendable\",\"description\":\"If set then this class can be extended by a subclass\",\"name\":\"extendable\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Extendable\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"cacheAlgorithm\":{\"attributeName\":\"cacheAlgorithm\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"20071004NM000000000000000000000600000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.metadata.RelationshipCache\",\"system\":false,\"displayLabel\":\"Cache Algorithm\",\"description\":\"Relationship Caching Algorithm\",\"name\":\"cacheAlgorithm\",\"immutable\":false,\"enumNames\":{\"CACHE_HARDCODED\":\"Cache Hardcoded\",\"CACHE_EVERYTHING\":\"Cache Everything\",\"CACHE_NOTHING\":\"Cache Nothing\"},\"_selectMultiple\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"enumNames\":[\"CACHE_NOTHING\"],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":true},\"isAbstract\":{\"attributeName\":\"isAbstract\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000003800000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Abstract\",\"negativeDisplayLabel\":\"Concrete\",\"description\":\"If set then instances of this class cannot be instantiated\",\"name\":\"isAbstract\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Abstract\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"dtoSource\":{\"attributeName\":\"dtoSource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"NM20070418000000000000000000004020101231NM0000000000000000000010\",\"system\":true,\"displayLabel\":\"DTO Source\",\"description\":\"The DTO source of the MdType\",\"name\":\"dtoSource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"JS20070104000000000000000000120000000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"description\":{\"attributeName\":\"description\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20090518NM00000000000000000000090000000MdAttributeLocalCharacter\",\"system\":false,\"displayLabel\":\"Description\",\"description\":\"Provides a description of the metadata that can be provided to the end user\",\"definingMdStruct\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"name\":\"description\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"7d4wszlv8fx6yqw25sbiw2210wbllxhfNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"7d4wszlv8fx6yqw25sbiw2210wbllxhfNM200904120000000000000000000030\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002900000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"7d4wszlv8fx6yqw25sbiw2210wbllxhfNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"defaultLocale\",\"description\":\"displayLabel for the default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"NM2009041200000000000000000000300000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"composition\":{\"attributeName\":\"composition\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000035300000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Composition\",\"negativeDisplayLabel\":\"Aggregation\",\"description\":\"If set then child objects in a relationship are deleted whenever the parent object is deleted.  This works like a database cascade delete on the parent object.\",\"name\":\"composition\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Composition\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"createdBy\":{\"attributeName\":\"createdBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000004100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Created By\",\"description\":\"User that created this object\",\"name\":\"createdBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"owner\":{\"attributeName\":\"owner\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000000100000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Owner\",\"description\":\"A reference to the user who own this object\",\"name\":\"owner\",\"referencedMdBusiness\":\"com.runwaysdk.system.Actor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"index1Name\":{\"attributeName\":\"index1Name\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20070427JS000000000000000000151600000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Index 1 Name\",\"description\":\"\",\"name\":\"index1Name\",\"immutable\":true,\"required\":true,\"size\":30},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"superMdRelationship\":{\"attributeName\":\"superMdRelationship\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20070926NM000000000000000000000100000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Super MdRelationship\",\"description\":\"Metadata of the parent relationship\",\"name\":\"superMdRelationship\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdRelationship\",\"immutable\":true,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"cacheSize\":{\"attributeName\":\"cacheSize\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000036700000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Cache Size\",\"description\":\"\",\"name\":\"cacheSize\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":true},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"jsStub\":{\"attributeName\":\"jsStub\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"6ozempoo35sdmp69nkuagmeb0kejpe5r20101231NM0000000000000000000010\",\"system\":false,\"displayLabel\":\"JavaScript Stub Source\",\"description\":\"\",\"name\":\"jsStub\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"displayLabel\":{\"attributeName\":\"displayLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM2009041200000000000000000000250000000MdAttributeLocalCharacter\",\"system\":false,\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\",\"definingMdStruct\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"name\":\"displayLabel\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"8pez5estfz3mra0ki12chjpnkyc0pubjNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"8pez5estfz3mra0ki12chjpnkyc0pubjNM200904120000000000000000000030\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002900000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"8pez5estfz3mra0ki12chjpnkyc0pubjNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"defaultLocale\",\"description\":\"displayLabel for the default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"NM2009041200000000000000000000300000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"remove\":{\"attributeName\":\"remove\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000003400000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Remove\",\"negativeDisplayLabel\":\"Can Remove\",\"description\":\"If set then this attribute cannot be dropped\",\"name\":\"remove\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Remove\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"childMethod\":{\"attributeName\":\"childMethod\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000095500000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Child Method\",\"description\":\"Generated classes for the parent type of this relationship have getters and setters to access their children.  This lets you custom define the names of the accessor methods.\",\"name\":\"childMethod\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"childCardinality\":{\"attributeName\":\"childCardinality\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000006700000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Child Cardinality\",\"description\":\"Maximum upperbound child cardinality on instances of this relationship\",\"name\":\"childCardinality\",\"immutable\":false,\"required\":true,\"size\":16},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"stubSource\":{\"attributeName\":\"stubSource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20070923NM000000000000000000000120101231NM0000000000000000000010\",\"system\":false,\"displayLabel\":\"Stub Source\",\"description\":\"\",\"name\":\"stubSource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"enforceSiteMaster\":{\"attributeName\":\"enforceSiteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"qlzuvymh2i7bewd3f61i5xdj03ysydou00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Enforce Site Master\",\"negativeDisplayLabel\":\"False\",\"description\":\"\",\"name\":\"enforceSiteMaster\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"True\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"parentMdBusiness\":{\"attributeName\":\"parentMdBusiness\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000006400000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Parent MdBusiness\",\"description\":\"Parent objects in this relationship must be instances of this class\",\"name\":\"parentMdBusiness\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdBusiness\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"index2Name\":{\"attributeName\":\"index2Name\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20070427JS000000000000000000151100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Index 2 Name\",\"description\":\"\",\"name\":\"index2Name\",\"immutable\":true,\"required\":true,\"size\":30},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000001100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"y02aradwy2ub4lbb6pbams3a6phifoeo20060824000000000000000000000001\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080227NM000000000000000000001900000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"parentCardinality\":{\"attributeName\":\"parentCardinality\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000006500000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Parent Cardinality\",\"description\":\"Maximum upperbound parent cardinality on instances of this relationship\",\"name\":\"parentCardinality\",\"immutable\":false,\"required\":true,\"size\":16},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"baseSource\":{\"attributeName\":\"baseSource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20070320EG000000000000000000145420101231NM0000000000000000000010\",\"system\":true,\"displayLabel\":\"Base Source\",\"description\":\"\",\"name\":\"baseSource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"tableName\":{\"attributeName\":\"tableName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20070919NM000000000000000000000100000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Table Name\",\"description\":\"Name of the table in the database that stores instances of this entity\",\"name\":\"tableName\",\"immutable\":true,\"required\":true,\"size\":31},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"publish\":{\"attributeName\":\"publish\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"NM20071105000000000000000000000100000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Publish\",\"negativeDisplayLabel\":\"Do not Publish\",\"description\":\"If set, then this class will be exposed outside the business layer with generated DTOs\",\"name\":\"publish\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Publish\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"childMdBusiness\":{\"attributeName\":\"childMdBusiness\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000006600000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Child MdBusiness\",\"description\":\"Child objects in this relationship must be instances of this class\",\"name\":\"childMdBusiness\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdBusiness\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"createDate\":{\"attributeName\":\"createDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000007100000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Create Date\",\"description\":\"Date and time this record was created\",\"name\":\"createDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"stubDTOsource\":{\"attributeName\":\"stubDTOsource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20070923NM000000000000000000000420101231NM0000000000000000000010\",\"system\":false,\"displayLabel\":\"Stub DTO Source\",\"description\":\"\",\"name\":\"stubDTOsource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"childVisibility\":{\"attributeName\":\"childVisibility\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"20071109NM000000000000000000001500000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.metadata.VisibilityModifier\",\"system\":false,\"displayLabel\":\"Child Visibility\",\"description\":\"\",\"name\":\"childVisibility\",\"immutable\":false,\"enumNames\":{\"PROTECTED\":\"Protected\",\"PUBLIC\":\"Public\"},\"_selectMultiple\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"enumNames\":[\"PUBLIC\"],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":true},\"parentMethod\":{\"attributeName\":\"parentMethod\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000095400000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Parent Method\",\"description\":\"Generated classes for the child type of this relationship have getters and setters to access their parents.  This lets you custom define the names of the accessor methods.\",\"name\":\"parentMethod\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"parentVisibility\":{\"attributeName\":\"parentVisibility\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"20071109NM000000000000000000001300000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.metadata.VisibilityModifier\",\"system\":false,\"displayLabel\":\"Parent Visibility\",\"description\":\"\",\"name\":\"parentVisibility\",\"immutable\":false,\"enumNames\":{\"PROTECTED\":\"Protected\",\"PUBLIC\":\"Public\"},\"_selectMultiple\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"enumNames\":[\"PUBLIC\"],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":true},\"hasDeterministicIds\":{\"attributeName\":\"hasDeterministicIds\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"isol1les55gorsb2reqwvu9wo85th34t00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Generate Deterministic IDs\",\"negativeDisplayLabel\":\"IDs are not deterministic\",\"description\":\"If set to TRUE, then IDs that are generated are deterministic, false otherwise. Deterministic IDs are generated from a hash of the KeyName value.\",\"name\":\"hasDeterministicIds\",\"immutable\":false,\"required\":false,\"positiveDisplayLabel\":\"IDs are deterministic\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"lastUpdateDate\":{\"attributeName\":\"lastUpdateDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000002100000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Last Update Date\",\"description\":\"Date and time this record was last updated\",\"name\":\"lastUpdateDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"queryDTOsource\":{\"attributeName\":\"queryDTOsource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20080408NM000000000000000000000120101231NM0000000000000000000010\",\"system\":true,\"displayLabel\":\"Query DTO Source\",\"description\":\"Attribute that stores the source code of the generated Query DTO class\",\"name\":\"queryDTOsource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"typeName\":{\"attributeName\":\"typeName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000003300000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Type Name\",\"description\":\"Name of the type\",\"name\":\"typeName\",\"immutable\":true,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"parentDisplayLabel\":{\"attributeName\":\"parentDisplayLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20090518NM00000000000000000000070000000MdAttributeLocalCharacter\",\"system\":false,\"displayLabel\":\"Display Label of Parent\",\"description\":\"Display label of the parent end of the relationship\",\"definingMdStruct\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"name\":\"parentDisplayLabel\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"8ebmr8ug1gmuqos28azfo0c0xcut3h2oNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"8ebmr8ug1gmuqos28azfo0c0xcut3h2oNM200904120000000000000000000030\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002900000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"8ebmr8ug1gmuqos28azfo0c0xcut3h2oNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"defaultLocale\",\"description\":\"displayLabel for the default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"NM2009041200000000000000000000300000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"childDisplayLabel\":{\"attributeName\":\"childDisplayLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20090518NM00000000000000000000080000000MdAttributeLocalCharacter\",\"system\":false,\"displayLabel\":\"Display Label of Child\",\"description\":\"Display label of the child end of the relationship\",\"definingMdStruct\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"name\":\"childDisplayLabel\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"qufj666x90jyh0wq83c253477o9c2x3cNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"qufj666x90jyh0wq83c253477o9c2x3cNM200904120000000000000000000030\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002900000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"qufj666x90jyh0wq83c253477o9c2x3cNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"defaultLocale\",\"description\":\"displayLabel for the default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"NM2009041200000000000000000000300000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"lastUpdatedBy\":{\"attributeName\":\"lastUpdatedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000003100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Last Updated By\",\"description\":\"User who last updated this object\",\"name\":\"lastUpdatedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"seq\":{\"attributeName\":\"seq\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000006100000000000000000000000000000338\",\"system\":true,\"displayLabel\":\"Sequence\",\"description\":\"Sequence number is incremented every time this object is updated\",\"name\":\"seq\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false},\"sortMdAttribute\":{\"attributeName\":\"sortMdAttribute\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000016600000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Sort MdAttribute\",\"description\":\"Default sort order of relationship queries will be sorted by this attribute\",\"name\":\"sortMdAttribute\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdAttributePrimitive\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"querySource\":{\"attributeName\":\"querySource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20070923NM000000000000000000001020101231NM0000000000000000000010\",\"system\":true,\"displayLabel\":\"Query Source\",\"description\":\"Stores the Java source for the query class\",\"name\":\"querySource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false}},\"_toString\":\"New: Graph Meta Data\",\"writable\":true,\"state\":\"\",\"modified\":true,\"id\":\"y02aradwy2ub4lbb6pbams3a6phifoeo20060824000000000000000000000001\",\"_type\":\"com.runwaysdk.system.metadata.MdGraph\",\"_typeMd\":{\"id\":\"2006082400000000000000000000000100000000000000000000000000000001\",\"displayLabel\":\"Graph Meta Data\",\"description\":\"Defines Meta Data for a graph structure\"},\"transitions\":[],\"newInstance\":true,\"lockedByCurrentUser\":false}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdGraphQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MdRelationshipQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdGraph'
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
