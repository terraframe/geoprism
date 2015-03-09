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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdAttributeRef', {
  Extends : 'com.runwaysdk.system.metadata.MdAttributeConcrete',
  IsAbstract : true,
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdAttributeRef'
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdAttributeRefQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MdAttributeConcreteQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdAttributeRef'
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdAttributeReference', {
  Extends : 'com.runwaysdk.system.metadata.MdAttributeRef',
  Constants : 
  {
    DEFAULTVALUE : 'defaultValue',
    MDBUSINESS : 'mdBusiness',
    CLASS : 'com.runwaysdk.system.metadata.MdAttributeReference'
  },
  Instance: 
  {
    getDefaultValue : function()
    {
      return this.getAttributeDTO('defaultValue').getValue();
    },
    setDefaultValue : function(value)
    {
      var attributeDTO = this.getAttributeDTO('defaultValue');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isDefaultValueReadable : function()
    {
      return this.getAttributeDTO('defaultValue').isReadable();
    },
    isDefaultValueWritable : function()
    {
      return this.getAttributeDTO('defaultValue').isWritable();
    },
    isDefaultValueModified : function()
    {
      return this.getAttributeDTO('defaultValue').isModified();
    },
    getDefaultValueMd : function()
    {
      return this.getAttributeDTO('defaultValue').getAttributeMdDTO();
    },
    getMdBusiness : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('mdBusiness');
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
    setMdBusiness : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('mdBusiness');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isMdBusinessReadable : function()
    {
      return this.getAttributeDTO('mdBusiness').isReadable();
    },
    isMdBusinessWritable : function()
    {
      return this.getAttributeDTO('mdBusiness').isWritable();
    },
    isMdBusinessModified : function()
    {
      return this.getAttributeDTO('mdBusiness').isModified();
    },
    getMdBusinessMd : function()
    {
      return this.getAttributeDTO('mdBusiness').getAttributeMdDTO();
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"readable\":true,\"dto_type\":\"com.runwaysdk.business.BusinessDTO\",\"attributeMap\":{\"mdBusiness\":{\"attributeName\":\"mdBusiness\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000018800000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"MdBusiness\",\"description\":\"Metadata that defines a class.  Instances of that class can be referenced by this attribute.\",\"name\":\"mdBusiness\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdBusiness\",\"immutable\":true,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"displayLabel\":{\"attributeName\":\"displayLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM2009041200000000000000000000510000000MdAttributeLocalCharacter\",\"system\":false,\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\",\"definingMdStruct\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"name\":\"displayLabel\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"2a4wp93yqc8bxvisb0qtr8o0pkejeo5kNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"2a4wp93yqc8bxvisb0qtr8o0pkejeo5kNM200904120000000000000000000030\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002900000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"2a4wp93yqc8bxvisb0qtr8o0pkejeo5kNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"defaultLocale\",\"description\":\"displayLabel for the default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"NM2009041200000000000000000000300000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"lockedBy\":{\"attributeName\":\"lockedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000008100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Locked By\",\"description\":\"User who has a write lock on this object\",\"name\":\"lockedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.Users\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"remove\":{\"attributeName\":\"remove\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000003400000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Remove\",\"negativeDisplayLabel\":\"Can Remove\",\"description\":\"If set then this attribute cannot be dropped\",\"name\":\"remove\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Remove\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"entityDomain\":{\"attributeName\":\"entityDomain\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"NM20081011000000000000000000006200000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Entity Domain\",\"description\":\"The Domain of this entity\",\"name\":\"entityDomain\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdDomain\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"type\":{\"attributeName\":\"type\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000005100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"type\",\"description\":\"Fully qualified type of this object\",\"name\":\"type\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000001100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"79tbqcrol5ckkb72eylx0feuzjqvqi5l00000000000000000000000000000186\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"indexType\":{\"attributeName\":\"indexType\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000040600000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.metadata.MdAttributeIndices\",\"system\":false,\"displayLabel\":\"Index Type\",\"description\":\"\",\"name\":\"indexType\",\"immutable\":false,\"enumNames\":{\"UNIQUE_INDEX\":\"Unique Index\",\"NO_INDEX\":\"No Index\",\"NON_UNIQUE_INDEX\":\"Non Unique Index\"},\"_selectMultiple\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"enumNames\":[\"NO_INDEX\"],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":true},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080227NM000000000000000000001900000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"setterVisibility\":{\"attributeName\":\"setterVisibility\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"20071110NM000000000000000000000100000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.metadata.VisibilityModifier\",\"system\":false,\"displayLabel\":\"Setter Visibility\",\"description\":\"\",\"name\":\"setterVisibility\",\"immutable\":false,\"enumNames\":{\"PROTECTED\":\"Protected\",\"PUBLIC\":\"Public\"},\"_selectMultiple\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"enumNames\":[\"PUBLIC\"],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":true},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"JS20070104000000000000000000120000000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"description\":{\"attributeName\":\"description\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20090518NM00000000000000000000090000000MdAttributeLocalCharacter\",\"system\":false,\"displayLabel\":\"Description\",\"description\":\"Provides a description of the metadata that can be provided to the end user\",\"definingMdStruct\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"name\":\"description\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"2gm70ewudrc4ipo1bmahqyuf8qowmmjdNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"2gm70ewudrc4ipo1bmahqyuf8qowmmjdNM200904120000000000000000000030\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002900000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"2gm70ewudrc4ipo1bmahqyuf8qowmmjdNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"defaultLocale\",\"description\":\"displayLabel for the default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"NM2009041200000000000000000000300000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"indexName\":{\"attributeName\":\"indexName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080520NM000000000000000000000100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Index Name\",\"description\":\"Name of the database index used for this attribute\",\"name\":\"indexName\",\"immutable\":false,\"required\":false,\"size\":30},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"immutable\":{\"attributeName\":\"immutable\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000021000000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Immutable\",\"negativeDisplayLabel\":\"Mutable\",\"description\":\"If set then the value of this attribute cannot be changed once it is set\",\"name\":\"immutable\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Immutable\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"columnName\":{\"attributeName\":\"columnName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20070405EG000000000000000000143100000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Accessor\",\"description\":\"Method name used in generating getters and setters for this attribute\",\"name\":\"columnName\",\"immutable\":true,\"required\":true,\"size\":28},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"defaultValue\":{\"attributeName\":\"defaultValue\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20081226000000000000000000002300000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Default Value\",\"description\":\"Default value that will be assigned to instances of this attribute\",\"name\":\"defaultValue\",\"immutable\":false,\"required\":false,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"createDate\":{\"attributeName\":\"createDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000007100000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Create Date\",\"description\":\"Date and time this record was created\",\"name\":\"createDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"required\":{\"attributeName\":\"required\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000004900000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Required\",\"negativeDisplayLabel\":\"Optional\",\"description\":\"If set then this attribute requires a value\",\"name\":\"required\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Required\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"generateAccessor\":{\"attributeName\":\"generateAccessor\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"zgwjxr3y30rggn2g4a8bojuh4jkb7pzs00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Generate Accessor\",\"negativeDisplayLabel\":\"\",\"description\":\"\",\"name\":\"generateAccessor\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"lastUpdateDate\":{\"attributeName\":\"lastUpdateDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000002100000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Last Update Date\",\"description\":\"Date and time this record was last updated\",\"name\":\"lastUpdateDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"attributeName\":{\"attributeName\":\"attributeName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000004000000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Attribute Name\",\"description\":\"The cannonical name of this attribute\",\"name\":\"attributeName\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"system\":{\"attributeName\":\"system\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000020800000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"System\",\"negativeDisplayLabel\":\"Not System\",\"description\":\"If set then the value of this attribute can only be modified by a user\",\"name\":\"system\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"System\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"createdBy\":{\"attributeName\":\"createdBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000004100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Created By\",\"description\":\"User that created this object\",\"name\":\"createdBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"lastUpdatedBy\":{\"attributeName\":\"lastUpdatedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000003100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Last Updated By\",\"description\":\"User who last updated this object\",\"name\":\"lastUpdatedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"owner\":{\"attributeName\":\"owner\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000000100000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Owner\",\"description\":\"A reference to the user who own this object\",\"name\":\"owner\",\"referencedMdBusiness\":\"com.runwaysdk.system.Actor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"seq\":{\"attributeName\":\"seq\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000006100000000000000000000000000000338\",\"system\":true,\"displayLabel\":\"Sequence\",\"description\":\"Sequence number is incremented every time this object is updated\",\"name\":\"seq\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false},\"definingMdClass\":{\"attributeName\":\"definingMdClass\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000019200000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Defining MdClass\",\"description\":\"Reference to the entity metadata object that defines this attribute\",\"name\":\"definingMdClass\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdClass\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"getterVisibility\":{\"attributeName\":\"getterVisibility\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"20071110NM000000000000000000000400000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.metadata.VisibilityModifier\",\"system\":false,\"displayLabel\":\"Getter Visibility\",\"description\":\"\",\"name\":\"getterVisibility\",\"immutable\":false,\"enumNames\":{\"PROTECTED\":\"Protected\",\"PUBLIC\":\"Public\"},\"_selectMultiple\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"enumNames\":[\"PUBLIC\"],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":true}},\"_toString\":\"New: Meta Data Attribute Reference\",\"writable\":true,\"state\":\"\",\"modified\":true,\"id\":\"79tbqcrol5ckkb72eylx0feuzjqvqi5l00000000000000000000000000000186\",\"_type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"_typeMd\":{\"id\":\"0000000000000000000000000000018600000000000000000000000000000001\",\"displayLabel\":\"Meta Data Attribute Reference\",\"description\":\"Defines attributes that do not contain values, but rather reference other objects\"},\"transitions\":[],\"newInstance\":true,\"lockedByCurrentUser\":false}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdAttributeReferenceQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MdAttributeRefQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdAttributeReference'
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdAttributeTerm', {
  Extends : 'com.runwaysdk.system.metadata.MdAttributeReference',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdAttributeTerm'
  },
  Instance: 
  {
    getAllClassifierAttributeRoots : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildren(clientRequest, this.getId(), 'com.runwaysdk.geodashboard.ontology.ClassifierAttributeRoot');
    },
    getAllClassifierAttributeRootsRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildRelationships(clientRequest, this.getId(), 'com.runwaysdk.geodashboard.ontology.ClassifierAttributeRoot');
    },
    addClassifierAttributeRoots : function(clientRequest, child)
    {
      var childId = (child instanceof Object) ? child.getId() : child;
      Mojo.$.com.runwaysdk.Facade.addChild(clientRequest, this.getId(), childId, 'com.runwaysdk.geodashboard.ontology.ClassifierAttributeRoot');
    },
    removeClassifierAttributeRoots : function(clientRequest, relationship)
    {
        var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteChild(clientRequest, relationshipId);
    },
    removeAllClassifierAttributeRoots : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteChildren(clientRequest, this.getId(), 'com.runwaysdk.geodashboard.ontology.ClassifierAttributeRoot');
    },
    getAllClassifierSynonymAttributeRoots : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildren(clientRequest, this.getId(), 'com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRoot');
    },
    getAllClassifierSynonymAttributeRootsRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildRelationships(clientRequest, this.getId(), 'com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRoot');
    },
    addClassifierSynonymAttributeRoots : function(clientRequest, child)
    {
      var childId = (child instanceof Object) ? child.getId() : child;
      Mojo.$.com.runwaysdk.Facade.addChild(clientRequest, this.getId(), childId, 'com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRoot');
    },
    removeClassifierSynonymAttributeRoots : function(clientRequest, relationship)
    {
        var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteChild(clientRequest, relationshipId);
    },
    removeAllClassifierSynonymAttributeRoots : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteChildren(clientRequest, this.getId(), 'com.runwaysdk.geodashboard.ontology.ClassifierSynonymAttributeRoot');
    },
    getAllGeoEntityAttributeRoots : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildren(clientRequest, this.getId(), 'com.runwaysdk.system.gis.geo.GeoEntityAttributeRoot');
    },
    getAllGeoEntityAttributeRootsRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.gis.geo.GeoEntityAttributeRoot');
    },
    addGeoEntityAttributeRoots : function(clientRequest, child)
    {
      var childId = (child instanceof Object) ? child.getId() : child;
      Mojo.$.com.runwaysdk.Facade.addChild(clientRequest, this.getId(), childId, 'com.runwaysdk.system.gis.geo.GeoEntityAttributeRoot');
    },
    removeGeoEntityAttributeRoots : function(clientRequest, relationship)
    {
        var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteChild(clientRequest, relationshipId);
    },
    removeAllGeoEntityAttributeRoots : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteChildren(clientRequest, this.getId(), 'com.runwaysdk.system.gis.geo.GeoEntityAttributeRoot');
    },
    getAllSynonymAttributeRoots : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildren(clientRequest, this.getId(), 'com.runwaysdk.system.gis.geo.SynonymAttributeRoot');
    },
    getAllSynonymAttributeRootsRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.gis.geo.SynonymAttributeRoot');
    },
    addSynonymAttributeRoots : function(clientRequest, child)
    {
      var childId = (child instanceof Object) ? child.getId() : child;
      Mojo.$.com.runwaysdk.Facade.addChild(clientRequest, this.getId(), childId, 'com.runwaysdk.system.gis.geo.SynonymAttributeRoot');
    },
    removeSynonymAttributeRoots : function(clientRequest, relationship)
    {
        var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteChild(clientRequest, relationshipId);
    },
    removeAllSynonymAttributeRoots : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteChildren(clientRequest, this.getId(), 'com.runwaysdk.system.gis.geo.SynonymAttributeRoot');
    },
    getAllUniversalAttributeRoots : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildren(clientRequest, this.getId(), 'com.runwaysdk.system.gis.geo.UniversalAttributeRoot');
    },
    getAllUniversalAttributeRootsRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.gis.geo.UniversalAttributeRoot');
    },
    addUniversalAttributeRoots : function(clientRequest, child)
    {
      var childId = (child instanceof Object) ? child.getId() : child;
      Mojo.$.com.runwaysdk.Facade.addChild(clientRequest, this.getId(), childId, 'com.runwaysdk.system.gis.geo.UniversalAttributeRoot');
    },
    removeUniversalAttributeRoots : function(clientRequest, relationship)
    {
        var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteChild(clientRequest, relationshipId);
    },
    removeAllUniversalAttributeRoots : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteChildren(clientRequest, this.getId(), 'com.runwaysdk.system.gis.geo.UniversalAttributeRoot');
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"readable\":true,\"dto_type\":\"com.runwaysdk.business.BusinessDTO\",\"attributeMap\":{\"mdBusiness\":{\"attributeName\":\"mdBusiness\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000018800000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"MdBusiness\",\"description\":\"Metadata that defines a class.  Instances of that class can be referenced by this attribute.\",\"name\":\"mdBusiness\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdBusiness\",\"immutable\":true,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"displayLabel\":{\"attributeName\":\"displayLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM2009041200000000000000000000510000000MdAttributeLocalCharacter\",\"system\":false,\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\",\"definingMdStruct\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"name\":\"displayLabel\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"75skncl9ml8ba2jsad8xd65npydtxaxnNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"75skncl9ml8ba2jsad8xd65npydtxaxnNM200904120000000000000000000030\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002900000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"75skncl9ml8ba2jsad8xd65npydtxaxnNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"defaultLocale\",\"description\":\"displayLabel for the default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"NM2009041200000000000000000000300000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"lockedBy\":{\"attributeName\":\"lockedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000008100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Locked By\",\"description\":\"User who has a write lock on this object\",\"name\":\"lockedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.Users\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"remove\":{\"attributeName\":\"remove\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000003400000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Remove\",\"negativeDisplayLabel\":\"Can Remove\",\"description\":\"If set then this attribute cannot be dropped\",\"name\":\"remove\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Remove\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"entityDomain\":{\"attributeName\":\"entityDomain\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"NM20081011000000000000000000006200000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Entity Domain\",\"description\":\"The Domain of this entity\",\"name\":\"entityDomain\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdDomain\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"type\":{\"attributeName\":\"type\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000005100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"type\",\"description\":\"Fully qualified type of this object\",\"name\":\"type\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"com.runwaysdk.system.metadata.MdAttributeTerm\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000001100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"zf7k3ifvhdnwvtcmzc7ayosx9jbgl22928oi9qf3idd5rdmnnsvyzx2eis0t62r0\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"indexType\":{\"attributeName\":\"indexType\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000040600000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.metadata.MdAttributeIndices\",\"system\":false,\"displayLabel\":\"Index Type\",\"description\":\"\",\"name\":\"indexType\",\"immutable\":false,\"enumNames\":{\"UNIQUE_INDEX\":\"Unique Index\",\"NO_INDEX\":\"No Index\",\"NON_UNIQUE_INDEX\":\"Non Unique Index\"},\"_selectMultiple\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"enumNames\":[\"NO_INDEX\"],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":true},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080227NM000000000000000000001900000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"setterVisibility\":{\"attributeName\":\"setterVisibility\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"20071110NM000000000000000000000100000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.metadata.VisibilityModifier\",\"system\":false,\"displayLabel\":\"Setter Visibility\",\"description\":\"\",\"name\":\"setterVisibility\",\"immutable\":false,\"enumNames\":{\"PROTECTED\":\"Protected\",\"PUBLIC\":\"Public\"},\"_selectMultiple\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"enumNames\":[\"PUBLIC\"],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":true},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"JS20070104000000000000000000120000000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"description\":{\"attributeName\":\"description\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20090518NM00000000000000000000090000000MdAttributeLocalCharacter\",\"system\":false,\"displayLabel\":\"Description\",\"description\":\"Provides a description of the metadata that can be provided to the end user\",\"definingMdStruct\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"name\":\"description\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"1wqtz7krdjqnunnbyz98m3xhpyevl4h5NM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"1wqtz7krdjqnunnbyz98m3xhpyevl4h5NM200904120000000000000000000030\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002900000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"1wqtz7krdjqnunnbyz98m3xhpyevl4h5NM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"defaultLocale\",\"description\":\"displayLabel for the default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"NM2009041200000000000000000000300000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"indexName\":{\"attributeName\":\"indexName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080520NM000000000000000000000100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Index Name\",\"description\":\"Name of the database index used for this attribute\",\"name\":\"indexName\",\"immutable\":false,\"required\":false,\"size\":30},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"immutable\":{\"attributeName\":\"immutable\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000021000000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Immutable\",\"negativeDisplayLabel\":\"Mutable\",\"description\":\"If set then the value of this attribute cannot be changed once it is set\",\"name\":\"immutable\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Immutable\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"columnName\":{\"attributeName\":\"columnName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20070405EG000000000000000000143100000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Accessor\",\"description\":\"Method name used in generating getters and setters for this attribute\",\"name\":\"columnName\",\"immutable\":true,\"required\":true,\"size\":28},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"defaultValue\":{\"attributeName\":\"defaultValue\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20081226000000000000000000002300000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Default Value\",\"description\":\"Default value that will be assigned to instances of this attribute\",\"name\":\"defaultValue\",\"immutable\":false,\"required\":false,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"createDate\":{\"attributeName\":\"createDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000007100000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Create Date\",\"description\":\"Date and time this record was created\",\"name\":\"createDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"required\":{\"attributeName\":\"required\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000004900000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Required\",\"negativeDisplayLabel\":\"Optional\",\"description\":\"If set then this attribute requires a value\",\"name\":\"required\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Required\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"generateAccessor\":{\"attributeName\":\"generateAccessor\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"zgwjxr3y30rggn2g4a8bojuh4jkb7pzs00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Generate Accessor\",\"negativeDisplayLabel\":\"\",\"description\":\"\",\"name\":\"generateAccessor\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"lastUpdateDate\":{\"attributeName\":\"lastUpdateDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000002100000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Last Update Date\",\"description\":\"Date and time this record was last updated\",\"name\":\"lastUpdateDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"attributeName\":{\"attributeName\":\"attributeName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000004000000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Attribute Name\",\"description\":\"The cannonical name of this attribute\",\"name\":\"attributeName\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"system\":{\"attributeName\":\"system\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000020800000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"System\",\"negativeDisplayLabel\":\"Not System\",\"description\":\"If set then the value of this attribute can only be modified by a user\",\"name\":\"system\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"System\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"createdBy\":{\"attributeName\":\"createdBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000004100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Created By\",\"description\":\"User that created this object\",\"name\":\"createdBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"lastUpdatedBy\":{\"attributeName\":\"lastUpdatedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000003100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Last Updated By\",\"description\":\"User who last updated this object\",\"name\":\"lastUpdatedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"owner\":{\"attributeName\":\"owner\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000000100000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Owner\",\"description\":\"A reference to the user who own this object\",\"name\":\"owner\",\"referencedMdBusiness\":\"com.runwaysdk.system.Actor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"seq\":{\"attributeName\":\"seq\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000006100000000000000000000000000000338\",\"system\":true,\"displayLabel\":\"Sequence\",\"description\":\"Sequence number is incremented every time this object is updated\",\"name\":\"seq\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false},\"definingMdClass\":{\"attributeName\":\"definingMdClass\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000019200000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Defining MdClass\",\"description\":\"Reference to the entity metadata object that defines this attribute\",\"name\":\"definingMdClass\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdClass\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"getterVisibility\":{\"attributeName\":\"getterVisibility\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"20071110NM000000000000000000000400000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.metadata.VisibilityModifier\",\"system\":false,\"displayLabel\":\"Getter Visibility\",\"description\":\"\",\"name\":\"getterVisibility\",\"immutable\":false,\"enumNames\":{\"PROTECTED\":\"Protected\",\"PUBLIC\":\"Public\"},\"_selectMultiple\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"enumNames\":[\"PUBLIC\"],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":true}},\"_toString\":\"New: MdAttributeTerm\",\"writable\":true,\"state\":\"\",\"modified\":true,\"id\":\"zf7k3ifvhdnwvtcmzc7ayosx9jbgl22928oi9qf3idd5rdmnnsvyzx2eis0t62r0\",\"_type\":\"com.runwaysdk.system.metadata.MdAttributeTerm\",\"_typeMd\":{\"id\":\"28oi9qf3idd5rdmnnsvyzx2eis0t62r000000000000000000000000000000001\",\"displayLabel\":\"MdAttributeTerm\",\"description\":\"Metadata definition for term attributes\"},\"transitions\":[],\"newInstance\":true,\"lockedByCurrentUser\":false}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdAttributeTermQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MdAttributeReferenceQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdAttributeTerm'
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
