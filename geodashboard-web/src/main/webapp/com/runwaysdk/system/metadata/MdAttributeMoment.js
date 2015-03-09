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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdAttribute', {
  Extends : 'com.runwaysdk.system.metadata.Metadata',
  IsAbstract : true,
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdAttribute'
  },
  Instance: 
  {
    getAllDefiningClass : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassAttribute');
    },
    getAllDefiningClassRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParentRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassAttribute');
    },
    addDefiningClass : function(clientRequest, parent)
    {
      var parentId = (parent instanceof Object) ? parent.getId() : parent;
      Mojo.$.com.runwaysdk.Facade.addParent(clientRequest, parentId, this.getId(), 'com.runwaysdk.system.metadata.ClassAttribute');
    },
    removeDefiningClass : function(clientRequest, relationship)
    {
      var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteParent(clientRequest, relationshipId);
    },
    removeAllDefiningClass : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassAttribute');
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdAttributeQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MetadataQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdAttribute'
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdAttributeConcrete', {
  Extends : 'com.runwaysdk.system.metadata.MdAttribute',
  IsAbstract : true,
  Constants : 
  {
    ATTRIBUTENAME : 'attributeName',
    COLUMNNAME : 'columnName',
    DEFININGMDCLASS : 'definingMdClass',
    DISPLAYLABEL : 'displayLabel',
    GENERATEACCESSOR : 'generateAccessor',
    GETTERVISIBILITY : 'getterVisibility',
    IMMUTABLE : 'immutable',
    INDEXNAME : 'indexName',
    INDEXTYPE : 'indexType',
    REQUIRED : 'required',
    SETTERVISIBILITY : 'setterVisibility',
    SYSTEM : 'system',
    CLASS : 'com.runwaysdk.system.metadata.MdAttributeConcrete'
  },
  Instance: 
  {
    getAttributeName : function()
    {
      return this.getAttributeDTO('attributeName').getValue();
    },
    setAttributeName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('attributeName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isAttributeNameReadable : function()
    {
      return this.getAttributeDTO('attributeName').isReadable();
    },
    isAttributeNameWritable : function()
    {
      return this.getAttributeDTO('attributeName').isWritable();
    },
    isAttributeNameModified : function()
    {
      return this.getAttributeDTO('attributeName').isModified();
    },
    getAttributeNameMd : function()
    {
      return this.getAttributeDTO('attributeName').getAttributeMdDTO();
    },
    getColumnName : function()
    {
      return this.getAttributeDTO('columnName').getValue();
    },
    setColumnName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('columnName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isColumnNameReadable : function()
    {
      return this.getAttributeDTO('columnName').isReadable();
    },
    isColumnNameWritable : function()
    {
      return this.getAttributeDTO('columnName').isWritable();
    },
    isColumnNameModified : function()
    {
      return this.getAttributeDTO('columnName').isModified();
    },
    getColumnNameMd : function()
    {
      return this.getAttributeDTO('columnName').getAttributeMdDTO();
    },
    getDefiningMdClass : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('definingMdClass');
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
    setDefiningMdClass : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('definingMdClass');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isDefiningMdClassReadable : function()
    {
      return this.getAttributeDTO('definingMdClass').isReadable();
    },
    isDefiningMdClassWritable : function()
    {
      return this.getAttributeDTO('definingMdClass').isWritable();
    },
    isDefiningMdClassModified : function()
    {
      return this.getAttributeDTO('definingMdClass').isModified();
    },
    getDefiningMdClassMd : function()
    {
      return this.getAttributeDTO('definingMdClass').getAttributeMdDTO();
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
    getGenerateAccessor : function()
    {
      return this.getAttributeDTO('generateAccessor').getValue();
    },
    setGenerateAccessor : function(value)
    {
      var attributeDTO = this.getAttributeDTO('generateAccessor');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isGenerateAccessorReadable : function()
    {
      return this.getAttributeDTO('generateAccessor').isReadable();
    },
    isGenerateAccessorWritable : function()
    {
      return this.getAttributeDTO('generateAccessor').isWritable();
    },
    isGenerateAccessorModified : function()
    {
      return this.getAttributeDTO('generateAccessor').isModified();
    },
    getGenerateAccessorMd : function()
    {
      return this.getAttributeDTO('generateAccessor').getAttributeMdDTO();
    },
    getGetterVisibility : function()
    {
      var attributeDTO = this.getAttributeDTO('getterVisibility');
      var names = attributeDTO.getEnumNames();
      var enums = [];
      for(var i=0; i<names.length; i++)
      {
        enums.push(Mojo.$.com.runwaysdk.system.metadata.VisibilityModifier[names[i]]);
      }
      return enums;
    },
    removeGetterVisibility : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('getterVisibility');
      attributeDTO.remove(enumValue);
      this.setModified(true);
    },
    clearGetterVisibility : function()
    {
      var attributeDTO = this.getAttributeDTO('getterVisibility');
      attributeDTO.clear();
      this.setModified(true);
    },
    addGetterVisibility : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('getterVisibility');
      attributeDTO.add(enumValue);
      this.setModified(true);
    },
    isGetterVisibilityReadable : function()
    {
      return this.getAttributeDTO('getterVisibility').isReadable();
    },
    isGetterVisibilityWritable : function()
    {
      return this.getAttributeDTO('getterVisibility').isWritable();
    },
    isGetterVisibilityModified : function()
    {
      return this.getAttributeDTO('getterVisibility').isModified();
    },
    getGetterVisibilityMd : function()
    {
      return this.getAttributeDTO('getterVisibility').getAttributeMdDTO();
    },
    getImmutable : function()
    {
      return this.getAttributeDTO('immutable').getValue();
    },
    setImmutable : function(value)
    {
      var attributeDTO = this.getAttributeDTO('immutable');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isImmutableReadable : function()
    {
      return this.getAttributeDTO('immutable').isReadable();
    },
    isImmutableWritable : function()
    {
      return this.getAttributeDTO('immutable').isWritable();
    },
    isImmutableModified : function()
    {
      return this.getAttributeDTO('immutable').isModified();
    },
    getImmutableMd : function()
    {
      return this.getAttributeDTO('immutable').getAttributeMdDTO();
    },
    getIndexName : function()
    {
      return this.getAttributeDTO('indexName').getValue();
    },
    isIndexNameReadable : function()
    {
      return this.getAttributeDTO('indexName').isReadable();
    },
    isIndexNameWritable : function()
    {
      return this.getAttributeDTO('indexName').isWritable();
    },
    isIndexNameModified : function()
    {
      return this.getAttributeDTO('indexName').isModified();
    },
    getIndexNameMd : function()
    {
      return this.getAttributeDTO('indexName').getAttributeMdDTO();
    },
    getIndexType : function()
    {
      var attributeDTO = this.getAttributeDTO('indexType');
      var names = attributeDTO.getEnumNames();
      var enums = [];
      for(var i=0; i<names.length; i++)
      {
        enums.push(Mojo.$.com.runwaysdk.system.metadata.MdAttributeIndices[names[i]]);
      }
      return enums;
    },
    removeIndexType : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('indexType');
      attributeDTO.remove(enumValue);
      this.setModified(true);
    },
    clearIndexType : function()
    {
      var attributeDTO = this.getAttributeDTO('indexType');
      attributeDTO.clear();
      this.setModified(true);
    },
    addIndexType : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('indexType');
      attributeDTO.add(enumValue);
      this.setModified(true);
    },
    isIndexTypeReadable : function()
    {
      return this.getAttributeDTO('indexType').isReadable();
    },
    isIndexTypeWritable : function()
    {
      return this.getAttributeDTO('indexType').isWritable();
    },
    isIndexTypeModified : function()
    {
      return this.getAttributeDTO('indexType').isModified();
    },
    getIndexTypeMd : function()
    {
      return this.getAttributeDTO('indexType').getAttributeMdDTO();
    },
    getRequired : function()
    {
      return this.getAttributeDTO('required').getValue();
    },
    setRequired : function(value)
    {
      var attributeDTO = this.getAttributeDTO('required');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isRequiredReadable : function()
    {
      return this.getAttributeDTO('required').isReadable();
    },
    isRequiredWritable : function()
    {
      return this.getAttributeDTO('required').isWritable();
    },
    isRequiredModified : function()
    {
      return this.getAttributeDTO('required').isModified();
    },
    getRequiredMd : function()
    {
      return this.getAttributeDTO('required').getAttributeMdDTO();
    },
    getSetterVisibility : function()
    {
      var attributeDTO = this.getAttributeDTO('setterVisibility');
      var names = attributeDTO.getEnumNames();
      var enums = [];
      for(var i=0; i<names.length; i++)
      {
        enums.push(Mojo.$.com.runwaysdk.system.metadata.VisibilityModifier[names[i]]);
      }
      return enums;
    },
    removeSetterVisibility : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('setterVisibility');
      attributeDTO.remove(enumValue);
      this.setModified(true);
    },
    clearSetterVisibility : function()
    {
      var attributeDTO = this.getAttributeDTO('setterVisibility');
      attributeDTO.clear();
      this.setModified(true);
    },
    addSetterVisibility : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('setterVisibility');
      attributeDTO.add(enumValue);
      this.setModified(true);
    },
    isSetterVisibilityReadable : function()
    {
      return this.getAttributeDTO('setterVisibility').isReadable();
    },
    isSetterVisibilityWritable : function()
    {
      return this.getAttributeDTO('setterVisibility').isWritable();
    },
    isSetterVisibilityModified : function()
    {
      return this.getAttributeDTO('setterVisibility').isModified();
    },
    getSetterVisibilityMd : function()
    {
      return this.getAttributeDTO('setterVisibility').getAttributeMdDTO();
    },
    getSystem : function()
    {
      return this.getAttributeDTO('system').getValue();
    },
    setSystem : function(value)
    {
      var attributeDTO = this.getAttributeDTO('system');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isSystemReadable : function()
    {
      return this.getAttributeDTO('system').isReadable();
    },
    isSystemWritable : function()
    {
      return this.getAttributeDTO('system').isWritable();
    },
    isSystemModified : function()
    {
      return this.getAttributeDTO('system').isModified();
    },
    getSystemMd : function()
    {
      return this.getAttributeDTO('system').getAttributeMdDTO();
    },
    getAllDefiningClass : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassAttributeConcrete');
    },
    getAllDefiningClassRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParentRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassAttributeConcrete');
    },
    addDefiningClass : function(clientRequest, parent)
    {
      var parentId = (parent instanceof Object) ? parent.getId() : parent;
      Mojo.$.com.runwaysdk.Facade.addParent(clientRequest, parentId, this.getId(), 'com.runwaysdk.system.metadata.ClassAttributeConcrete');
    },
    removeDefiningClass : function(clientRequest, relationship)
    {
      var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteParent(clientRequest, relationshipId);
    },
    removeAllDefiningClass : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.ClassAttributeConcrete');
    },
    getAllIndex : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.IndexAttribute');
    },
    getAllIndexRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParentRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.IndexAttribute');
    },
    addIndex : function(clientRequest, parent)
    {
      var parentId = (parent instanceof Object) ? parent.getId() : parent;
      Mojo.$.com.runwaysdk.Facade.addParent(clientRequest, parentId, this.getId(), 'com.runwaysdk.system.metadata.IndexAttribute');
    },
    removeIndex : function(clientRequest, relationship)
    {
      var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteParent(clientRequest, relationshipId);
    },
    removeAllIndex : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.IndexAttribute');
    },
    getAllgetMdDimension : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttribute');
    },
    getAllgetMdDimensionRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParentRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttribute');
    },
    addGetMdDimension : function(clientRequest, parent)
    {
      var parentId = (parent instanceof Object) ? parent.getId() : parent;
      Mojo.$.com.runwaysdk.Facade.addParent(clientRequest, parentId, this.getId(), 'com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttribute');
    },
    removegetMdDimension : function(clientRequest, relationship)
    {
      var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteParent(clientRequest, relationshipId);
    },
    removeAllgetMdDimension : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttribute');
    },
    getAllVirtualAttribute : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildren(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.VirtualizeAttribute');
    },
    getAllVirtualAttributeRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.VirtualizeAttribute');
    },
    addVirtualAttribute : function(clientRequest, child)
    {
      var childId = (child instanceof Object) ? child.getId() : child;
      Mojo.$.com.runwaysdk.Facade.addChild(clientRequest, this.getId(), childId, 'com.runwaysdk.system.metadata.VirtualizeAttribute');
    },
    removeVirtualAttribute : function(clientRequest, relationship)
    {
        var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteChild(clientRequest, relationshipId);
    },
    removeAllVirtualAttribute : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteChildren(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.VirtualizeAttribute');
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdAttributeConcreteQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MdAttributeQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdAttributeConcrete'
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdAttributePrimitive', {
  Extends : 'com.runwaysdk.system.metadata.MdAttributeConcrete',
  IsAbstract : true,
  Constants : 
  {
    EXPRESSION : 'expression',
    ISEXPRESSION : 'isExpression',
    CLASS : 'com.runwaysdk.system.metadata.MdAttributePrimitive'
  },
  Instance: 
  {
    getExpression : function()
    {
      return this.getAttributeDTO('expression').getValue();
    },
    setExpression : function(value)
    {
      var attributeDTO = this.getAttributeDTO('expression');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isExpressionReadable : function()
    {
      return this.getAttributeDTO('expression').isReadable();
    },
    isExpressionWritable : function()
    {
      return this.getAttributeDTO('expression').isWritable();
    },
    isExpressionModified : function()
    {
      return this.getAttributeDTO('expression').isModified();
    },
    getExpressionMd : function()
    {
      return this.getAttributeDTO('expression').getAttributeMdDTO();
    },
    getIsExpression : function()
    {
      return this.getAttributeDTO('isExpression').getValue();
    },
    setIsExpression : function(value)
    {
      var attributeDTO = this.getAttributeDTO('isExpression');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isIsExpressionReadable : function()
    {
      return this.getAttributeDTO('isExpression').isReadable();
    },
    isIsExpressionWritable : function()
    {
      return this.getAttributeDTO('isExpression').isWritable();
    },
    isIsExpressionModified : function()
    {
      return this.getAttributeDTO('isExpression').isModified();
    },
    getIsExpressionMd : function()
    {
      return this.getAttributeDTO('isExpression').getAttributeMdDTO();
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdAttributePrimitiveQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MdAttributeConcreteQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdAttributePrimitive'
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdAttributeMoment', {
  Extends : 'com.runwaysdk.system.metadata.MdAttributePrimitive',
  IsAbstract : true,
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdAttributeMoment'
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdAttributeMomentQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MdAttributePrimitiveQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdAttributeMoment'
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
