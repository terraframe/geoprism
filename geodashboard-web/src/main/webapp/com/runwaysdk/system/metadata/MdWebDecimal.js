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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdField', {
  Extends : 'com.runwaysdk.system.metadata.Metadata',
  IsAbstract : true,
  Constants : 
  {
    DISPLAYLABEL : 'displayLabel',
    FIELDCONDITION : 'fieldCondition',
    FIELDNAME : 'fieldName',
    FIELDORDER : 'fieldOrder',
    REQUIRED : 'required',
    CLASS : 'com.runwaysdk.system.metadata.MdField'
  },
  Instance: 
  {
    getDisplayLabel : function()
    {
      if(Mojo.Meta.classExists('com.runwaysdk.system.metadata.MdFieldDisplayLabel'))
      {
        var structDTO = this.getAttributeDTO('displayLabel').getStructDTO();
        if(structDTO == null)
        {
          return null;
        }
        else if(structDTO instanceof Mojo.$.com.runwaysdk.system.metadata.MdFieldDisplayLabel)
        {
          return structDTO;
        }
        else
        {
          structDTO = new Mojo.$.com.runwaysdk.system.metadata.MdFieldDisplayLabel(structDTO);
          this.getAttributeDTO('displayLabel').setStructDTO(structDTO);
          return structDTO;
        }
      }
      else
      {
        throw new Mojo.$.com.runwaysdk.Exception('Must import type com.runwaysdk.system.metadata.MdFieldDisplayLabel');
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
    getFieldCondition : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('fieldCondition');
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
    setFieldCondition : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('fieldCondition');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isFieldConditionReadable : function()
    {
      return this.getAttributeDTO('fieldCondition').isReadable();
    },
    isFieldConditionWritable : function()
    {
      return this.getAttributeDTO('fieldCondition').isWritable();
    },
    isFieldConditionModified : function()
    {
      return this.getAttributeDTO('fieldCondition').isModified();
    },
    getFieldConditionMd : function()
    {
      return this.getAttributeDTO('fieldCondition').getAttributeMdDTO();
    },
    getFieldName : function()
    {
      return this.getAttributeDTO('fieldName').getValue();
    },
    setFieldName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('fieldName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isFieldNameReadable : function()
    {
      return this.getAttributeDTO('fieldName').isReadable();
    },
    isFieldNameWritable : function()
    {
      return this.getAttributeDTO('fieldName').isWritable();
    },
    isFieldNameModified : function()
    {
      return this.getAttributeDTO('fieldName').isModified();
    },
    getFieldNameMd : function()
    {
      return this.getAttributeDTO('fieldName').getAttributeMdDTO();
    },
    getFieldOrder : function()
    {
      return this.getAttributeDTO('fieldOrder').getValue();
    },
    setFieldOrder : function(value)
    {
      var attributeDTO = this.getAttributeDTO('fieldOrder');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isFieldOrderReadable : function()
    {
      return this.getAttributeDTO('fieldOrder').isReadable();
    },
    isFieldOrderWritable : function()
    {
      return this.getAttributeDTO('fieldOrder').isWritable();
    },
    isFieldOrderModified : function()
    {
      return this.getAttributeDTO('fieldOrder').isModified();
    },
    getFieldOrderMd : function()
    {
      return this.getAttributeDTO('fieldOrder').getAttributeMdDTO();
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
    getAllMdForm : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.FormField');
    },
    getAllMdFormRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParentRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.FormField');
    },
    addMdForm : function(clientRequest, parent)
    {
      var parentId = (parent instanceof Object) ? parent.getId() : parent;
      Mojo.$.com.runwaysdk.Facade.addParent(clientRequest, parentId, this.getId(), 'com.runwaysdk.system.metadata.FormField');
    },
    removeMdForm : function(clientRequest, relationship)
    {
      var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteParent(clientRequest, relationshipId);
    },
    removeAllMdForm : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.FormField');
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdFieldQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MetadataQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdField'
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdWebField', {
  Extends : 'com.runwaysdk.system.metadata.MdField',
  IsAbstract : true,
  Constants : 
  {
    DEFININGMDFORM : 'definingMdForm',
    CLASS : 'com.runwaysdk.system.metadata.MdWebField'
  },
  Instance: 
  {
    getDefiningMdForm : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('definingMdForm');
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
    setDefiningMdForm : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('definingMdForm');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isDefiningMdFormReadable : function()
    {
      return this.getAttributeDTO('definingMdForm').isReadable();
    },
    isDefiningMdFormWritable : function()
    {
      return this.getAttributeDTO('definingMdForm').isWritable();
    },
    isDefiningMdFormModified : function()
    {
      return this.getAttributeDTO('definingMdForm').isModified();
    },
    getDefiningMdFormMd : function()
    {
      return this.getAttributeDTO('definingMdForm').getAttributeMdDTO();
    },
    getAllGroupFields : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.WebGroupField');
    },
    getAllGroupFieldsRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParentRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.WebGroupField');
    },
    addGroupFields : function(clientRequest, parent)
    {
      var parentId = (parent instanceof Object) ? parent.getId() : parent;
      Mojo.$.com.runwaysdk.Facade.addParent(clientRequest, parentId, this.getId(), 'com.runwaysdk.system.metadata.WebGroupField');
    },
    removeGroupFields : function(clientRequest, relationship)
    {
      var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteParent(clientRequest, relationshipId);
    },
    removeAllGroupFields : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.WebGroupField');
    },
    getAllMdForm : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.WebFormField');
    },
    getAllMdFormRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParentRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.WebFormField');
    },
    addMdForm : function(clientRequest, parent)
    {
      var parentId = (parent instanceof Object) ? parent.getId() : parent;
      Mojo.$.com.runwaysdk.Facade.addParent(clientRequest, parentId, this.getId(), 'com.runwaysdk.system.metadata.WebFormField');
    },
    removeMdForm : function(clientRequest, relationship)
    {
      var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteParent(clientRequest, relationshipId);
    },
    removeAllMdForm : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.WebFormField');
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdWebFieldQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MdFieldQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdWebField'
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdWebAttribute', {
  Extends : 'com.runwaysdk.system.metadata.MdWebField',
  IsAbstract : true,
  Constants : 
  {
    DEFININGMDATTRIBUTE : 'definingMdAttribute',
    SHOWONSEARCH : 'showOnSearch',
    SHOWONVIEWALL : 'showOnViewAll',
    CLASS : 'com.runwaysdk.system.metadata.MdWebAttribute'
  },
  Instance: 
  {
    getDefiningMdAttribute : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('definingMdAttribute');
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
    setDefiningMdAttribute : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('definingMdAttribute');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isDefiningMdAttributeReadable : function()
    {
      return this.getAttributeDTO('definingMdAttribute').isReadable();
    },
    isDefiningMdAttributeWritable : function()
    {
      return this.getAttributeDTO('definingMdAttribute').isWritable();
    },
    isDefiningMdAttributeModified : function()
    {
      return this.getAttributeDTO('definingMdAttribute').isModified();
    },
    getDefiningMdAttributeMd : function()
    {
      return this.getAttributeDTO('definingMdAttribute').getAttributeMdDTO();
    },
    getShowOnSearch : function()
    {
      return this.getAttributeDTO('showOnSearch').getValue();
    },
    setShowOnSearch : function(value)
    {
      var attributeDTO = this.getAttributeDTO('showOnSearch');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isShowOnSearchReadable : function()
    {
      return this.getAttributeDTO('showOnSearch').isReadable();
    },
    isShowOnSearchWritable : function()
    {
      return this.getAttributeDTO('showOnSearch').isWritable();
    },
    isShowOnSearchModified : function()
    {
      return this.getAttributeDTO('showOnSearch').isModified();
    },
    getShowOnSearchMd : function()
    {
      return this.getAttributeDTO('showOnSearch').getAttributeMdDTO();
    },
    getShowOnViewAll : function()
    {
      return this.getAttributeDTO('showOnViewAll').getValue();
    },
    setShowOnViewAll : function(value)
    {
      var attributeDTO = this.getAttributeDTO('showOnViewAll');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isShowOnViewAllReadable : function()
    {
      return this.getAttributeDTO('showOnViewAll').isReadable();
    },
    isShowOnViewAllWritable : function()
    {
      return this.getAttributeDTO('showOnViewAll').isWritable();
    },
    isShowOnViewAllModified : function()
    {
      return this.getAttributeDTO('showOnViewAll').isModified();
    },
    getShowOnViewAllMd : function()
    {
      return this.getAttributeDTO('showOnViewAll').getAttributeMdDTO();
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdWebAttributeQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MdWebFieldQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdWebAttribute'
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdWebPrimitive', {
  Extends : 'com.runwaysdk.system.metadata.MdWebAttribute',
  IsAbstract : true,
  Constants : 
  {
    EXPRESSION : 'expression',
    ISEXPRESSION : 'isExpression',
    CLASS : 'com.runwaysdk.system.metadata.MdWebPrimitive'
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
    getAllGrid : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.WebGridField');
    },
    getAllGridRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParentRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.WebGridField');
    },
    addGrid : function(clientRequest, parent)
    {
      var parentId = (parent instanceof Object) ? parent.getId() : parent;
      Mojo.$.com.runwaysdk.Facade.addParent(clientRequest, parentId, this.getId(), 'com.runwaysdk.system.metadata.WebGridField');
    },
    removeGrid : function(clientRequest, relationship)
    {
      var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteParent(clientRequest, relationshipId);
    },
    removeAllGrid : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteParents(clientRequest, this.getId(), 'com.runwaysdk.system.metadata.WebGridField');
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdWebPrimitiveQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MdWebAttributeQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdWebPrimitive'
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdWebNumber', {
  Extends : 'com.runwaysdk.system.metadata.MdWebPrimitive',
  IsAbstract : true,
  Constants : 
  {
    ENDRANGE : 'endRange',
    STARTRANGE : 'startRange',
    CLASS : 'com.runwaysdk.system.metadata.MdWebNumber'
  },
  Instance: 
  {
    getEndRange : function()
    {
      return this.getAttributeDTO('endRange').getValue();
    },
    setEndRange : function(value)
    {
      var attributeDTO = this.getAttributeDTO('endRange');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isEndRangeReadable : function()
    {
      return this.getAttributeDTO('endRange').isReadable();
    },
    isEndRangeWritable : function()
    {
      return this.getAttributeDTO('endRange').isWritable();
    },
    isEndRangeModified : function()
    {
      return this.getAttributeDTO('endRange').isModified();
    },
    getEndRangeMd : function()
    {
      return this.getAttributeDTO('endRange').getAttributeMdDTO();
    },
    getStartRange : function()
    {
      return this.getAttributeDTO('startRange').getValue();
    },
    setStartRange : function(value)
    {
      var attributeDTO = this.getAttributeDTO('startRange');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isStartRangeReadable : function()
    {
      return this.getAttributeDTO('startRange').isReadable();
    },
    isStartRangeWritable : function()
    {
      return this.getAttributeDTO('startRange').isWritable();
    },
    isStartRangeModified : function()
    {
      return this.getAttributeDTO('startRange').isModified();
    },
    getStartRangeMd : function()
    {
      return this.getAttributeDTO('startRange').getAttributeMdDTO();
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdWebNumberQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MdWebPrimitiveQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdWebNumber'
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdWebDec', {
  Extends : 'com.runwaysdk.system.metadata.MdWebNumber',
  IsAbstract : true,
  Constants : 
  {
    DECPRECISION : 'decPrecision',
    DECSCALE : 'decScale',
    CLASS : 'com.runwaysdk.system.metadata.MdWebDec'
  },
  Instance: 
  {
    getDecPrecision : function()
    {
      return this.getAttributeDTO('decPrecision').getValue();
    },
    setDecPrecision : function(value)
    {
      var attributeDTO = this.getAttributeDTO('decPrecision');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isDecPrecisionReadable : function()
    {
      return this.getAttributeDTO('decPrecision').isReadable();
    },
    isDecPrecisionWritable : function()
    {
      return this.getAttributeDTO('decPrecision').isWritable();
    },
    isDecPrecisionModified : function()
    {
      return this.getAttributeDTO('decPrecision').isModified();
    },
    getDecPrecisionMd : function()
    {
      return this.getAttributeDTO('decPrecision').getAttributeMdDTO();
    },
    getDecScale : function()
    {
      return this.getAttributeDTO('decScale').getValue();
    },
    setDecScale : function(value)
    {
      var attributeDTO = this.getAttributeDTO('decScale');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isDecScaleReadable : function()
    {
      return this.getAttributeDTO('decScale').isReadable();
    },
    isDecScaleWritable : function()
    {
      return this.getAttributeDTO('decScale').isWritable();
    },
    isDecScaleModified : function()
    {
      return this.getAttributeDTO('decScale').isModified();
    },
    getDecScaleMd : function()
    {
      return this.getAttributeDTO('decScale').getAttributeMdDTO();
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdWebDecQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MdWebNumberQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdWebDec'
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
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdWebDecimal', {
  Extends : 'com.runwaysdk.system.metadata.MdWebDec',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdWebDecimal'
  },
  Instance: 
  {
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"readable\":true,\"dto_type\":\"com.runwaysdk.business.BusinessDTO\",\"attributeMap\":{\"expression\":{\"attributeName\":\"expression\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeTextDTO\",\"attributeMdDTO\":{\"id\":\"igvrol3trqk0lxjauzb0vuhpoxpcp0eb00000000000000000000000000000139\",\"system\":false,\"displayLabel\":\"Expression\",\"description\":\"Field for defining an expression that is evaluated on object apply and stored in the database\",\"name\":\"expression\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeText\",\"modified\":false},\"displayLabel\":{\"attributeName\":\"displayLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"8ngq09909esuftf6wn138h62crnmqrtc0000000MdAttributeLocalCharacter\",\"system\":false,\"displayLabel\":\"Display Label\",\"description\":\"The display label of the form\",\"definingMdStruct\":\"com.runwaysdk.system.metadata.MdFieldDisplayLabel\",\"name\":\"displayLabel\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"zoictccu2r6is27vrjxh0r1eb3b711z48ual4o5g20fv0rl7dqgfe54tghwhmx8j\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"zoictccu2r6is27vrjxh0r1eb3b711z48ual4o5g20fv0rl7dqgfe54tghwhmx8j\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MdFieldDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"8l0caneb4yc0jcp4afyqxxqguez7lrrn00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"zoictccu2r6is27vrjxh0r1eb3b711z48ual4o5g20fv0rl7dqgfe54tghwhmx8j\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"1xrbu9pfblsbac5ch4bnsr46hn7perqb00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Default Locale\",\"description\":\"Default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":4000},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"xrqfoimyo52v3154hyn09bjg2cqgc2d000000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0lj953aur2l7u5hpkvhzk8z12pf080jc00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"8ual4o5g20fv0rl7dqgfe54tghwhmx8j0000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"The display label of the form\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"lockedBy\":{\"attributeName\":\"lockedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000008100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Locked By\",\"description\":\"User who has a write lock on this object\",\"name\":\"lockedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.Users\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"remove\":{\"attributeName\":\"remove\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000003400000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Remove\",\"negativeDisplayLabel\":\"Can Remove\",\"description\":\"If set then this attribute cannot be dropped\",\"name\":\"remove\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Remove\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"entityDomain\":{\"attributeName\":\"entityDomain\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"NM20081011000000000000000000006200000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Entity Domain\",\"description\":\"The Domain of this entity\",\"name\":\"entityDomain\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdDomain\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"type\":{\"attributeName\":\"type\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000005100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"type\",\"description\":\"Fully qualified type of this object\",\"name\":\"type\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"com.runwaysdk.system.metadata.MdWebDecimal\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"fieldOrder\":{\"attributeName\":\"fieldOrder\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"r7boxd2s0i8lg2z92tv5752ms5aauwg900000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Field Order\",\"description\":\"The order of the field in the form\",\"name\":\"fieldOrder\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":false},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"startRange\":{\"attributeName\":\"startRange\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"xcqyc7qdz87dhgorlfakud2t97mkr5dw00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Start Range\",\"description\":\"The number start range.\",\"name\":\"startRange\",\"immutable\":false,\"required\":false,\"size\":100},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"definingMdAttribute\":{\"attributeName\":\"definingMdAttribute\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"76m5v2e3vacqdem6h8tfvgmob50xdh3000000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Defining MdAttribute\",\"description\":\"The defining MdAttribute of this Field\",\"name\":\"definingMdAttribute\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdAttribute\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000001100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"8moaygkgmyaghwitrdmj1crskc6gonmw0gkv0hn61pikf6oxjt7q3c9grzz9dia9\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080227NM000000000000000000001900000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"JS20070104000000000000000000120000000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"description\":{\"attributeName\":\"description\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20090518NM00000000000000000000090000000MdAttributeLocalCharacter\",\"system\":false,\"displayLabel\":\"Description\",\"description\":\"Provides a description of the metadata that can be provided to the end user\",\"definingMdStruct\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"name\":\"description\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"20klrvd4z6ety743xvycs10zux5i7h3wNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"20klrvd4z6ety743xvycs10zux5i7h3wNM200904120000000000000000000030\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MetadataDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002900000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"20klrvd4z6ety743xvycs10zux5i7h3wNM200904120000000000000000000030\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"defaultLocale\",\"description\":\"displayLabel for the default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"NM20090412000000000000000000002700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"NM2009041200000000000000000000300000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"Name of the attribute that end users will see\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"fieldName\":{\"attributeName\":\"fieldName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"8tr2ed5n6ql70d3bjsk6c457r3tttwct00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Field Name\",\"description\":\"The name of the field.\",\"name\":\"fieldName\",\"immutable\":false,\"required\":true,\"size\":50},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"createDate\":{\"attributeName\":\"createDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000007100000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Create Date\",\"description\":\"Date and time this record was created\",\"name\":\"createDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"required\":{\"attributeName\":\"required\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"xo1tm6dt6l3qp1eezu98299p2huxwmyd00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Required\",\"negativeDisplayLabel\":\"false\",\"description\":\"Denotes if this field is required\",\"name\":\"required\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"true\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"decPrecision\":{\"attributeName\":\"decPrecision\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"8s5gcyypgoyd6ay4q9fxnz692g9i60dw00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Precision\",\"description\":\"The number of signifcant digits.\",\"name\":\"decPrecision\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":true,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":6,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"showOnSearch\":{\"attributeName\":\"showOnSearch\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"ia5u3ms9yxi69qv19zcyt2mu5u5556x100000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Show on search\",\"negativeDisplayLabel\":\"False\",\"description\":\"Flag indicating if the field should be shown on the search page.\",\"name\":\"showOnSearch\",\"immutable\":false,\"required\":false,\"positiveDisplayLabel\":\"True\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"endRange\":{\"attributeName\":\"endRange\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"7gyjyf1gb4r2wxobqlasajagrms1uqfe00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"End Range\",\"description\":\"The number end range.\",\"name\":\"endRange\",\"immutable\":false,\"required\":false,\"size\":100},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"fieldCondition\":{\"attributeName\":\"fieldCondition\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"sigd00vvrrel0uqcpup82j7pftkgpthf00000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Condition\",\"description\":\"The condition which must be satisfied\",\"name\":\"fieldCondition\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.FieldCondition\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"lastUpdateDate\":{\"attributeName\":\"lastUpdateDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000002100000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Last Update Date\",\"description\":\"Date and time this record was last updated\",\"name\":\"lastUpdateDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"definingMdForm\":{\"attributeName\":\"definingMdForm\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"03ik47a90v41celewcfyr82f7x830ais00000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Defining MdForm\",\"description\":\"The defining MdForm of this Field\",\"name\":\"definingMdForm\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdWebForm\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"createdBy\":{\"attributeName\":\"createdBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000004100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Created By\",\"description\":\"User that created this object\",\"name\":\"createdBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"showOnViewAll\":{\"attributeName\":\"showOnViewAll\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"ip2fqmopx8wz3h7tpbsyv53bbucs004f00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Show on view all\",\"negativeDisplayLabel\":\"False\",\"description\":\"Flag indicating if the field should be shown on the view all page.\",\"name\":\"showOnViewAll\",\"immutable\":false,\"required\":false,\"positiveDisplayLabel\":\"True\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"lastUpdatedBy\":{\"attributeName\":\"lastUpdatedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000003100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Last Updated By\",\"description\":\"User who last updated this object\",\"name\":\"lastUpdatedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"decScale\":{\"attributeName\":\"decScale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"xe0qxzlnq02ktpoq0cfj3ngmww1je3tj00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Scale\",\"description\":\"The number of decimal digits.\",\"name\":\"decScale\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":true},\"writable\":true,\"value\":1,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"owner\":{\"attributeName\":\"owner\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000000100000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Owner\",\"description\":\"A reference to the user who own this object\",\"name\":\"owner\",\"referencedMdBusiness\":\"com.runwaysdk.system.Actor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"seq\":{\"attributeName\":\"seq\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"20060807NM000000000000000000006100000000000000000000000000000338\",\"system\":true,\"displayLabel\":\"Sequence\",\"description\":\"Sequence number is incremented every time this object is updated\",\"name\":\"seq\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false},\"isExpression\":{\"attributeName\":\"isExpression\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"ipw4nhorictnmmdftvcbgezagg7up73300000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Is Expression Attribute\",\"negativeDisplayLabel\":\"false\",\"description\":\"Calculates a value as a result of an expression and stores the results into the database\",\"name\":\"isExpression\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"true\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false}},\"_toString\":\"New: Decimal Field\",\"writable\":true,\"state\":\"\",\"modified\":true,\"id\":\"8moaygkgmyaghwitrdmj1crskc6gonmw0gkv0hn61pikf6oxjt7q3c9grzz9dia9\",\"_type\":\"com.runwaysdk.system.metadata.MdWebDecimal\",\"_typeMd\":{\"id\":\"0gkv0hn61pikf6oxjt7q3c9grzz9dia900000000000000000000000000000001\",\"displayLabel\":\"Decimal Field\",\"description\":\"Metadata Web Decimal Field\"},\"transitions\":[],\"newInstance\":true,\"lockedByCurrentUser\":false}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdWebDecimalQueryDTO', {
  Extends : 'com.runwaysdk.system.metadata.MdWebDecQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdWebDecimal'
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
