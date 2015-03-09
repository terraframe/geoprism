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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdStruct', {
  Extends : 'com.runwaysdk.system.metadata.MdEntity',
  Constants : 
  {
    CACHEALGORITHM : 'cacheAlgorithm',
    CLASS : 'com.runwaysdk.system.metadata.MdStruct'
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
        enums.push(Mojo.$.com.runwaysdk.system.metadata.StructCache[names[i]]);
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
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"readable\":true,\"dto_type\":\"com.runwaysdk.business.BusinessDTO\",\"attributeMap\":{\"jsStub\":{\"attributeName\":\"jsStub\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"6ozempoo35sdmp69nkuagmeb0kejpe5r20101231NM0000000000000000000010\",\"system\":false,\"displayLabel\":\"JavaScript Stub Source\",\"description\":\"\",\"name\":\"jsStub\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"displayLabel\":{\"attributeName\":\"displayLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM2009041200000000000000000000250000000MdAttributeLocalCharacter\",\"system\":false,\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\",\"definingMdStruct\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"name\":\"displayLabel\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"8oyy9j0dh6dvsomyg0xgcwl1knbyms6wNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"8oyy9j0dh6dvsomyg0xgcwl1knbyms6wNM200904120000000000000000000030\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002900000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"8oyy9j0dh6dvsomyg0xgcwl1knbyms6wNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"defaultLocale\",\"description\":\"displayLabel for the default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"NM2009041200000000000000000000300000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"lockedBy\":{\"attributeName\":\"lockedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000008100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Locked By\",\"description\":\"User who has a write lock on this object\",\"name\":\"lockedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.Users\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"packageName\":{\"attributeName\":\"packageName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000034900000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Package Name\",\"description\":\"Name of the package for this type\",\"name\":\"packageName\",\"immutable\":true,\"required\":true,\"size\":127},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"remove\":{\"attributeName\":\"remove\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000003400000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Remove\",\"negativeDisplayLabel\":\"Can Remove\",\"description\":\"If set then this attribute cannot be dropped\",\"name\":\"remove\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Remove\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"exported\":{\"attributeName\":\"exported\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"EG20100125000000000000000000000100000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Exported\",\"negativeDisplayLabel\":\"Not Exported\",\"description\":\"Indicates if this type is being exported through synchronization\",\"name\":\"exported\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Exported\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"entityDomain\":{\"attributeName\":\"entityDomain\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"NM20081011000000000000000000006200000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Entity Domain\",\"description\":\"The Domain of this entity\",\"name\":\"entityDomain\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdDomain\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"jsBase\":{\"attributeName\":\"jsBase\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"sx1bsc4gxk1lrjkl544m7fak7sg2t1j820101231NM0000000000000000000010\",\"system\":false,\"displayLabel\":\"JavaScript Base Source\",\"description\":\"\",\"name\":\"jsBase\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"type\":{\"attributeName\":\"type\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000005100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"type\",\"description\":\"Fully qualified type of this object\",\"name\":\"type\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"com.runwaysdk.system.metadata.MdStruct\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"stubSource\":{\"attributeName\":\"stubSource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20070923NM000000000000000000000120101231NM0000000000000000000010\",\"system\":false,\"displayLabel\":\"Stub Source\",\"description\":\"\",\"name\":\"stubSource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"enforceSiteMaster\":{\"attributeName\":\"enforceSiteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"qlzuvymh2i7bewd3f61i5xdj03ysydou00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Enforce Site Master\",\"negativeDisplayLabel\":\"False\",\"description\":\"\",\"name\":\"enforceSiteMaster\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"True\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000001100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"05nq5gvkghdqo5o0ytbvm90k61a0moyd00000000000000000000000000000979\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"cacheAlgorithm\":{\"attributeName\":\"cacheAlgorithm\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"20071005NM000000000000000000000700000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.metadata.StructCache\",\"system\":false,\"displayLabel\":\"Cache Algorithm\",\"description\":\"Class Caching Algorithm\",\"name\":\"cacheAlgorithm\",\"immutable\":false,\"enumNames\":{\"CACHE_HARDCODED\":\"Cache Hardcoded\",\"CACHE_EVERYTHING\":\"Cache Everything\",\"CACHE_MOST_RECENTLY_USED\":\"Cache Most Recently Used\",\"CACHE_NOTHING\":\"Cache Nothing\"},\"_selectMultiple\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"enumNames\":[\"CACHE_NOTHING\"],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":true},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080227NM000000000000000000001900000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"dtoSource\":{\"attributeName\":\"dtoSource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"NM20070418000000000000000000004020101231NM0000000000000000000010\",\"system\":true,\"displayLabel\":\"DTO Source\",\"description\":\"The DTO source of the MdType\",\"name\":\"dtoSource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"JS20070104000000000000000000120000000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"baseSource\":{\"attributeName\":\"baseSource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20070320EG000000000000000000145420101231NM0000000000000000000010\",\"system\":true,\"displayLabel\":\"Base Source\",\"description\":\"\",\"name\":\"baseSource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"tableName\":{\"attributeName\":\"tableName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20070919NM000000000000000000000100000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Table Name\",\"description\":\"Name of the table in the database that stores instances of this entity\",\"name\":\"tableName\",\"immutable\":true,\"required\":true,\"size\":31},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"description\":{\"attributeName\":\"description\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20090518NM00000000000000000000090000000MdAttributeLocalCharacter\",\"system\":false,\"displayLabel\":\"Description\",\"description\":\"Provides a description of the metadata that can be provided to the end user\",\"definingMdStruct\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"name\":\"description\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"raajz1n8ktshb902xlc1d3nedyrjbsyzNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"raajz1n8ktshb902xlc1d3nedyrjbsyzNM200904120000000000000000000030\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002900000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"raajz1n8ktshb902xlc1d3nedyrjbsyzNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"defaultLocale\",\"description\":\"displayLabel for the default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"NM2009041200000000000000000000300000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"publish\":{\"attributeName\":\"publish\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"NM20071105000000000000000000000100000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Publish\",\"negativeDisplayLabel\":\"Do not Publish\",\"description\":\"If set, then this class will be exposed outside the business layer with generated DTOs\",\"name\":\"publish\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Publish\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"createDate\":{\"attributeName\":\"createDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000007100000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Create Date\",\"description\":\"Date and time this record was created\",\"name\":\"createDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"stubDTOsource\":{\"attributeName\":\"stubDTOsource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20070923NM000000000000000000000420101231NM0000000000000000000010\",\"system\":false,\"displayLabel\":\"Stub DTO Source\",\"description\":\"\",\"name\":\"stubDTOsource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"hasDeterministicIds\":{\"attributeName\":\"hasDeterministicIds\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"isol1les55gorsb2reqwvu9wo85th34t00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Generate Deterministic IDs\",\"negativeDisplayLabel\":\"IDs are not deterministic\",\"description\":\"If set to TRUE, then IDs that are generated are deterministic, false otherwise. Deterministic IDs are generated from a hash of the KeyName value.\",\"name\":\"hasDeterministicIds\",\"immutable\":false,\"required\":false,\"positiveDisplayLabel\":\"IDs are deterministic\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"lastUpdateDate\":{\"attributeName\":\"lastUpdateDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000002100000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Last Update Date\",\"description\":\"Date and time this record was last updated\",\"name\":\"lastUpdateDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"typeName\":{\"attributeName\":\"typeName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000003300000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Type Name\",\"description\":\"Name of the type\",\"name\":\"typeName\",\"immutable\":true,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"queryDTOsource\":{\"attributeName\":\"queryDTOsource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20080408NM000000000000000000000120101231NM0000000000000000000010\",\"system\":true,\"displayLabel\":\"Query DTO Source\",\"description\":\"Attribute that stores the source code of the generated Query DTO class\",\"name\":\"queryDTOsource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"createdBy\":{\"attributeName\":\"createdBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000004100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Created By\",\"description\":\"User that created this object\",\"name\":\"createdBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"lastUpdatedBy\":{\"attributeName\":\"lastUpdatedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000003100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Last Updated By\",\"description\":\"User who last updated this object\",\"name\":\"lastUpdatedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"seq\":{\"attributeName\":\"seq\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000006100000000000000000000000000000338\",\"system\":true,\"displayLabel\":\"Sequence\",\"description\":\"Sequence number is incremented every time this object is updated\",\"name\":\"seq\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false},\"owner\":{\"attributeName\":\"owner\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000000100000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Owner\",\"description\":\"A reference to the user who own this object\",\"name\":\"owner\",\"referencedMdBusiness\":\"com.runwaysdk.system.Actor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"cacheSize\":{\"attributeName\":\"cacheSize\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000036700000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Cache Size\",\"description\":\"\",\"name\":\"cacheSize\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":true},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"querySource\":{\"attributeName\":\"querySource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20070923NM000000000000000000001020101231NM0000000000000000000010\",\"system\":true,\"displayLabel\":\"Query Source\",\"description\":\"Stores the Java source for the query class\",\"name\":\"querySource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false}},\"_toString\":\"New: Struct Class Metadata\",\"writable\":true,\"state\":\"\",\"modified\":true,\"id\":\"05nq5gvkghdqo5o0ytbvm90k61a0moyd00000000000000000000000000000979\",\"_type\":\"com.runwaysdk.system.metadata.MdStruct\",\"_typeMd\":{\"id\":\"0000000000000000000000000000097900000000000000000000000000000001\",\"displayLabel\":\"Struct Class Metadata\",\"description\":\"Defines a struct class\"},\"transitions\":[],\"newInstance\":true,\"lockedByCurrentUser\":false}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdStructQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MdEntityQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdStruct'
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdLocalStruct', {
  Extends : 'com.runwaysdk.system.metadata.MdStruct',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdLocalStruct'
  },
  Instance: 
  {
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"readable\":true,\"dto_type\":\"com.runwaysdk.business.BusinessDTO\",\"attributeMap\":{\"jsStub\":{\"attributeName\":\"jsStub\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"6ozempoo35sdmp69nkuagmeb0kejpe5r20101231NM0000000000000000000010\",\"system\":false,\"displayLabel\":\"JavaScript Stub Source\",\"description\":\"\",\"name\":\"jsStub\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"displayLabel\":{\"attributeName\":\"displayLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM2009041200000000000000000000250000000MdAttributeLocalCharacter\",\"system\":false,\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\",\"definingMdStruct\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"name\":\"displayLabel\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"z6vw9sfxs301445duv7g6p0eq6mktoffNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"z6vw9sfxs301445duv7g6p0eq6mktoffNM200904120000000000000000000030\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002900000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"z6vw9sfxs301445duv7g6p0eq6mktoffNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"defaultLocale\",\"description\":\"displayLabel for the default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"NM2009041200000000000000000000300000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"lockedBy\":{\"attributeName\":\"lockedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000008100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Locked By\",\"description\":\"User who has a write lock on this object\",\"name\":\"lockedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.Users\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"packageName\":{\"attributeName\":\"packageName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000034900000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Package Name\",\"description\":\"Name of the package for this type\",\"name\":\"packageName\",\"immutable\":true,\"required\":true,\"size\":127},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"remove\":{\"attributeName\":\"remove\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000003400000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Remove\",\"negativeDisplayLabel\":\"Can Remove\",\"description\":\"If set then this attribute cannot be dropped\",\"name\":\"remove\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Remove\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"exported\":{\"attributeName\":\"exported\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"EG20100125000000000000000000000100000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Exported\",\"negativeDisplayLabel\":\"Not Exported\",\"description\":\"Indicates if this type is being exported through synchronization\",\"name\":\"exported\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Exported\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"entityDomain\":{\"attributeName\":\"entityDomain\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"NM20081011000000000000000000006200000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Entity Domain\",\"description\":\"The Domain of this entity\",\"name\":\"entityDomain\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdDomain\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"jsBase\":{\"attributeName\":\"jsBase\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"sx1bsc4gxk1lrjkl544m7fak7sg2t1j820101231NM0000000000000000000010\",\"system\":false,\"displayLabel\":\"JavaScript Base Source\",\"description\":\"\",\"name\":\"jsBase\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"type\":{\"attributeName\":\"type\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000005100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"type\",\"description\":\"Fully qualified type of this object\",\"name\":\"type\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"com.runwaysdk.system.metadata.MdLocalStruct\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"stubSource\":{\"attributeName\":\"stubSource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20070923NM000000000000000000000120101231NM0000000000000000000010\",\"system\":false,\"displayLabel\":\"Stub Source\",\"description\":\"\",\"name\":\"stubSource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"enforceSiteMaster\":{\"attributeName\":\"enforceSiteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"qlzuvymh2i7bewd3f61i5xdj03ysydou00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Enforce Site Master\",\"negativeDisplayLabel\":\"False\",\"description\":\"\",\"name\":\"enforceSiteMaster\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"True\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000001100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"1udd565ruzhvph932k9kjdad9hfko9ve0000000000000000000MdLocalStruct\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"cacheAlgorithm\":{\"attributeName\":\"cacheAlgorithm\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"20071005NM000000000000000000000700000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.metadata.StructCache\",\"system\":false,\"displayLabel\":\"Cache Algorithm\",\"description\":\"Class Caching Algorithm\",\"name\":\"cacheAlgorithm\",\"immutable\":false,\"enumNames\":{\"CACHE_HARDCODED\":\"Cache Hardcoded\",\"CACHE_EVERYTHING\":\"Cache Everything\",\"CACHE_MOST_RECENTLY_USED\":\"Cache Most Recently Used\",\"CACHE_NOTHING\":\"Cache Nothing\"},\"_selectMultiple\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"enumNames\":[\"CACHE_NOTHING\"],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":true},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080227NM000000000000000000001900000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"dtoSource\":{\"attributeName\":\"dtoSource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"NM20070418000000000000000000004020101231NM0000000000000000000010\",\"system\":true,\"displayLabel\":\"DTO Source\",\"description\":\"The DTO source of the MdType\",\"name\":\"dtoSource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"JS20070104000000000000000000120000000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"baseSource\":{\"attributeName\":\"baseSource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20070320EG000000000000000000145420101231NM0000000000000000000010\",\"system\":true,\"displayLabel\":\"Base Source\",\"description\":\"\",\"name\":\"baseSource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"tableName\":{\"attributeName\":\"tableName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20070919NM000000000000000000000100000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Table Name\",\"description\":\"Name of the table in the database that stores instances of this entity\",\"name\":\"tableName\",\"immutable\":true,\"required\":true,\"size\":31},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"description\":{\"attributeName\":\"description\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20090518NM00000000000000000000090000000MdAttributeLocalCharacter\",\"system\":false,\"displayLabel\":\"Description\",\"description\":\"Provides a description of the metadata that can be provided to the end user\",\"definingMdStruct\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"name\":\"description\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"xf74snbp2ja9y3pnby7oonbafsn1u23fNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"xf74snbp2ja9y3pnby7oonbafsn1u23fNM200904120000000000000000000030\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002900000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"xf74snbp2ja9y3pnby7oonbafsn1u23fNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"defaultLocale\",\"description\":\"displayLabel for the default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"NM2009041200000000000000000000300000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"publish\":{\"attributeName\":\"publish\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"NM20071105000000000000000000000100000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Publish\",\"negativeDisplayLabel\":\"Do not Publish\",\"description\":\"If set, then this class will be exposed outside the business layer with generated DTOs\",\"name\":\"publish\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Publish\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"createDate\":{\"attributeName\":\"createDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000007100000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Create Date\",\"description\":\"Date and time this record was created\",\"name\":\"createDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"stubDTOsource\":{\"attributeName\":\"stubDTOsource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20070923NM000000000000000000000420101231NM0000000000000000000010\",\"system\":false,\"displayLabel\":\"Stub DTO Source\",\"description\":\"\",\"name\":\"stubDTOsource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"hasDeterministicIds\":{\"attributeName\":\"hasDeterministicIds\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"isol1les55gorsb2reqwvu9wo85th34t00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Generate Deterministic IDs\",\"negativeDisplayLabel\":\"IDs are not deterministic\",\"description\":\"If set to TRUE, then IDs that are generated are deterministic, false otherwise. Deterministic IDs are generated from a hash of the KeyName value.\",\"name\":\"hasDeterministicIds\",\"immutable\":false,\"required\":false,\"positiveDisplayLabel\":\"IDs are deterministic\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"lastUpdateDate\":{\"attributeName\":\"lastUpdateDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000002100000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Last Update Date\",\"description\":\"Date and time this record was last updated\",\"name\":\"lastUpdateDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"typeName\":{\"attributeName\":\"typeName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000003300000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Type Name\",\"description\":\"Name of the type\",\"name\":\"typeName\",\"immutable\":true,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"queryDTOsource\":{\"attributeName\":\"queryDTOsource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20080408NM000000000000000000000120101231NM0000000000000000000010\",\"system\":true,\"displayLabel\":\"Query DTO Source\",\"description\":\"Attribute that stores the source code of the generated Query DTO class\",\"name\":\"queryDTOsource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"createdBy\":{\"attributeName\":\"createdBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000004100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Created By\",\"description\":\"User that created this object\",\"name\":\"createdBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"lastUpdatedBy\":{\"attributeName\":\"lastUpdatedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000003100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Last Updated By\",\"description\":\"User who last updated this object\",\"name\":\"lastUpdatedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"seq\":{\"attributeName\":\"seq\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000006100000000000000000000000000000338\",\"system\":true,\"displayLabel\":\"Sequence\",\"description\":\"Sequence number is incremented every time this object is updated\",\"name\":\"seq\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false},\"owner\":{\"attributeName\":\"owner\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000000100000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Owner\",\"description\":\"A reference to the user who own this object\",\"name\":\"owner\",\"referencedMdBusiness\":\"com.runwaysdk.system.Actor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"cacheSize\":{\"attributeName\":\"cacheSize\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000036700000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Cache Size\",\"description\":\"\",\"name\":\"cacheSize\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":true},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"querySource\":{\"attributeName\":\"querySource\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"20070923NM000000000000000000001020101231NM0000000000000000000010\",\"system\":true,\"displayLabel\":\"Query Source\",\"description\":\"Stores the Java source for the query class\",\"name\":\"querySource\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false}},\"_toString\":\"New: Localized Struct Class Metadata\",\"writable\":true,\"state\":\"\",\"modified\":true,\"id\":\"1udd565ruzhvph932k9kjdad9hfko9ve0000000000000000000MdLocalStruct\",\"_type\":\"com.runwaysdk.system.metadata.MdLocalStruct\",\"_typeMd\":{\"id\":\"0000000000000000000MdLocalStruct00000000000000000000000000000001\",\"displayLabel\":\"Localized Struct Class Metadata\",\"description\":\"Defines a struct class for localized values\"},\"transitions\":[],\"newInstance\":true,\"lockedByCurrentUser\":false}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdLocalStructQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MdStructQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdLocalStruct'
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
