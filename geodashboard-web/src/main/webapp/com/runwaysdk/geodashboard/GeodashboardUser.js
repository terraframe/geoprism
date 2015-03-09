Mojo.Meta.newClass('com.runwaysdk.system.Actor', {
  Extends : 'com.runwaysdk.business.BusinessDTO',
  IsAbstract : true,
  Constants : 
  {
    CREATEDATE : 'createDate',
    CREATEDBY : 'createdBy',
    ENTITYDOMAIN : 'entityDomain',
    ID : 'id',
    KEYNAME : 'keyName',
    LASTUPDATEDATE : 'lastUpdateDate',
    LASTUPDATEDBY : 'lastUpdatedBy',
    LOCKEDBY : 'lockedBy',
    OWNER : 'owner',
    SEQ : 'seq',
    SITEMASTER : 'siteMaster',
    TYPE : 'type',
    CLASS : 'com.runwaysdk.system.Actor'
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
    getAllMetadata : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildren(clientRequest, this.getId(), 'com.runwaysdk.system.TypePermission');
    },
    getAllMetadataRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.TypePermission');
    },
    addMetadata : function(clientRequest, child)
    {
      var childId = (child instanceof Object) ? child.getId() : child;
      Mojo.$.com.runwaysdk.Facade.addChild(clientRequest, this.getId(), childId, 'com.runwaysdk.system.TypePermission');
    },
    removeMetadata : function(clientRequest, relationship)
    {
        var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteChild(clientRequest, relationshipId);
    },
    removeAllMetadata : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteChildren(clientRequest, this.getId(), 'com.runwaysdk.system.TypePermission');
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
Mojo.Meta.newClass('com.runwaysdk.system.ActorQueryDTO', {
  Extends : 'com.runwaysdk.business.BusinessQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.Actor'
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
Mojo.Meta.newClass('com.runwaysdk.system.SingleActor', {
  Extends : 'com.runwaysdk.system.Actor',
  IsAbstract : true,
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.SingleActor'
  },
  Instance: 
  {
    getAllAssignedRole : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildren(clientRequest, this.getId(), 'com.runwaysdk.system.Assignments');
    },
    getAllAssignedRoleRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.Assignments');
    },
    addAssignedRole : function(clientRequest, child)
    {
      var childId = (child instanceof Object) ? child.getId() : child;
      Mojo.$.com.runwaysdk.Facade.addChild(clientRequest, this.getId(), childId, 'com.runwaysdk.system.Assignments');
    },
    removeAssignedRole : function(clientRequest, relationship)
    {
        var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteChild(clientRequest, relationshipId);
    },
    removeAllAssignedRole : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteChildren(clientRequest, this.getId(), 'com.runwaysdk.system.Assignments');
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
Mojo.Meta.newClass('com.runwaysdk.system.SingleActorQueryDTO', {
  Extends : 'com.runwaysdk.system.ActorQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.SingleActor'
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
Mojo.Meta.newClass('com.runwaysdk.system.Users', {
  Extends : 'com.runwaysdk.system.SingleActor',
  Constants : 
  {
    INACTIVE : 'inactive',
    LOCALE : 'locale',
    PASSWORD : 'password',
    SESSIONLIMIT : 'sessionLimit',
    USERNAME : 'username',
    CLASS : 'com.runwaysdk.system.Users'
  },
  Instance: 
  {
    getInactive : function()
    {
      return this.getAttributeDTO('inactive').getValue();
    },
    setInactive : function(value)
    {
      var attributeDTO = this.getAttributeDTO('inactive');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isInactiveReadable : function()
    {
      return this.getAttributeDTO('inactive').isReadable();
    },
    isInactiveWritable : function()
    {
      return this.getAttributeDTO('inactive').isWritable();
    },
    isInactiveModified : function()
    {
      return this.getAttributeDTO('inactive').isModified();
    },
    getInactiveMd : function()
    {
      return this.getAttributeDTO('inactive').getAttributeMdDTO();
    },
    getLocale : function()
    {
      var attributeDTO = this.getAttributeDTO('locale');
      var names = attributeDTO.getEnumNames();
      var enums = [];
      for(var i=0; i<names.length; i++)
      {
        enums.push(Mojo.$.com.runwaysdk.system.metadata.UserLocales[names[i]]);
      }
      return enums;
    },
    removeLocale : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('locale');
      attributeDTO.remove(enumValue);
      this.setModified(true);
    },
    clearLocale : function()
    {
      var attributeDTO = this.getAttributeDTO('locale');
      attributeDTO.clear();
      this.setModified(true);
    },
    addLocale : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('locale');
      attributeDTO.add(enumValue);
      this.setModified(true);
    },
    isLocaleReadable : function()
    {
      return this.getAttributeDTO('locale').isReadable();
    },
    isLocaleWritable : function()
    {
      return this.getAttributeDTO('locale').isWritable();
    },
    isLocaleModified : function()
    {
      return this.getAttributeDTO('locale').isModified();
    },
    getLocaleMd : function()
    {
      return this.getAttributeDTO('locale').getAttributeMdDTO();
    },
    getPassword : function()
    {
      return this.getAttributeDTO('password').getValue();
    },
    setPassword : function(value)
    {
      var attributeDTO = this.getAttributeDTO('password');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPasswordReadable : function()
    {
      return this.getAttributeDTO('password').isReadable();
    },
    isPasswordWritable : function()
    {
      return this.getAttributeDTO('password').isWritable();
    },
    isPasswordModified : function()
    {
      return this.getAttributeDTO('password').isModified();
    },
    getPasswordMd : function()
    {
      return this.getAttributeDTO('password').getAttributeMdDTO();
    },
    getSessionLimit : function()
    {
      return this.getAttributeDTO('sessionLimit').getValue();
    },
    setSessionLimit : function(value)
    {
      var attributeDTO = this.getAttributeDTO('sessionLimit');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isSessionLimitReadable : function()
    {
      return this.getAttributeDTO('sessionLimit').isReadable();
    },
    isSessionLimitWritable : function()
    {
      return this.getAttributeDTO('sessionLimit').isWritable();
    },
    isSessionLimitModified : function()
    {
      return this.getAttributeDTO('sessionLimit').isModified();
    },
    getSessionLimitMd : function()
    {
      return this.getAttributeDTO('sessionLimit').getAttributeMdDTO();
    },
    getUsername : function()
    {
      return this.getAttributeDTO('username').getValue();
    },
    setUsername : function(value)
    {
      var attributeDTO = this.getAttributeDTO('username');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isUsernameReadable : function()
    {
      return this.getAttributeDTO('username').isReadable();
    },
    isUsernameWritable : function()
    {
      return this.getAttributeDTO('username').isWritable();
    },
    isUsernameModified : function()
    {
      return this.getAttributeDTO('username').isModified();
    },
    getUsernameMd : function()
    {
      return this.getAttributeDTO('username').getAttributeMdDTO();
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"readable\":true,\"dto_type\":\"com.runwaysdk.business.BusinessDTO\",\"attributeMap\":{\"sessionLimit\":{\"attributeName\":\"sessionLimit\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000056600000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Session Limit\",\"description\":\"The number of sessions a user can open at the same time.\",\"name\":\"sessionLimit\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":true,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":1,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"lockedBy\":{\"attributeName\":\"lockedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060808NM000000000000000000005100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Locked By\",\"description\":\"User who has a write lock on this object\",\"name\":\"lockedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.Users\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"locale\":{\"attributeName\":\"locale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"20070322JN000000000000000000000600000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.metadata.UserLocales\",\"system\":false,\"displayLabel\":\"Locale\",\"description\":\"The user locale\",\"name\":\"locale\",\"immutable\":false,\"enumNames\":{},\"_selectMultiple\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"enumNames\":[],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":false},\"entityDomain\":{\"attributeName\":\"entityDomain\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"NM20081011000000000000000000006500000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Entity Domain\",\"description\":\"The Domain of this entity\",\"name\":\"entityDomain\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdDomain\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"type\":{\"attributeName\":\"type\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060808NM000000000000000000000100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Type\",\"description\":\"Fully qualified type of this object\",\"name\":\"type\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"com.runwaysdk.system.Users\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"password\":{\"attributeName\":\"password\",\"readable\":false,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeHashDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000002900000000000000000000000000000631\",\"system\":false,\"encryptionMethod\":\"SHA\",\"displayLabel\":\"Password\",\"description\":\"User password\",\"name\":\"password\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeHash\",\"modified\":false},\"lastUpdateDate\":{\"attributeName\":\"lastUpdateDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060808NM000000000000000000004100000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Last Update Date\",\"description\":\"Date and time this record was last updated\",\"name\":\"lastUpdateDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060808NM000000000000000000008100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"0iz1vsztlayrl3q1o15a47yvbqpxjtpz00000000000000000000000000000003\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"inactive\":{\"attributeName\":\"inactive\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"20070320EG000000000000000000145500000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Inactive\",\"negativeDisplayLabel\":\"Active\",\"description\":\"Inactive flag of the user, when true user cannot log in.\",\"name\":\"inactive\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Inactive\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"username\":{\"attributeName\":\"username\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000002800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Username\",\"description\":\"User login\",\"name\":\"username\",\"immutable\":false,\"required\":true,\"size\":32},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080227NM000000000000000000002100000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"createdBy\":{\"attributeName\":\"createdBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060808NM000000000000000000001100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Created By\",\"description\":\"User that created this object\",\"name\":\"createdBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"JS20070104000000000000000000122100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"lastUpdatedBy\":{\"attributeName\":\"lastUpdatedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060808NM000000000000000000007100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Last Updated By\",\"description\":\"User who last updated this object\",\"name\":\"lastUpdatedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"owner\":{\"attributeName\":\"owner\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060808NM000000000000000000002100000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Owner\",\"description\":\"A reference to the user who own this object\",\"name\":\"owner\",\"referencedMdBusiness\":\"com.runwaysdk.system.Actor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"seq\":{\"attributeName\":\"seq\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"20060808NM000000000000000000003100000000000000000000000000000338\",\"system\":true,\"displayLabel\":\"Sequence\",\"description\":\"Sequence number is incremented every time this object is updated\",\"name\":\"seq\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false},\"createDate\":{\"attributeName\":\"createDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060808NM000000000000000000006100000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Create Date\",\"description\":\"Date and time this record was created\",\"name\":\"createDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false}},\"_toString\":\"New: Users\",\"writable\":true,\"state\":\"\",\"modified\":true,\"id\":\"0iz1vsztlayrl3q1o15a47yvbqpxjtpz00000000000000000000000000000003\",\"_type\":\"com.runwaysdk.system.Users\",\"_typeMd\":{\"id\":\"0000000000000000000000000000000300000000000000000000000000000001\",\"displayLabel\":\"Users\",\"description\":\"Registered users of the application\"},\"transitions\":[],\"newInstance\":true,\"lockedByCurrentUser\":false}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.UsersQueryDTO', {
  Extends : 'com.runwaysdk.system.SingleActorQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.Users'
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
Mojo.Meta.newClass('com.runwaysdk.geodashboard.GeodashboardUser', {
  Extends : 'com.runwaysdk.system.Users',
  Constants : 
  {
    EMAIL : 'email',
    FIRSTNAME : 'firstName',
    LASTNAME : 'lastName',
    PHONENUMBER : 'phoneNumber',
    CLASS : 'com.runwaysdk.geodashboard.GeodashboardUser'
  },
  Instance: 
  {
    getEmail : function()
    {
      return this.getAttributeDTO('email').getValue();
    },
    setEmail : function(value)
    {
      var attributeDTO = this.getAttributeDTO('email');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isEmailReadable : function()
    {
      return this.getAttributeDTO('email').isReadable();
    },
    isEmailWritable : function()
    {
      return this.getAttributeDTO('email').isWritable();
    },
    isEmailModified : function()
    {
      return this.getAttributeDTO('email').isModified();
    },
    getEmailMd : function()
    {
      return this.getAttributeDTO('email').getAttributeMdDTO();
    },
    getFirstName : function()
    {
      return this.getAttributeDTO('firstName').getValue();
    },
    setFirstName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('firstName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isFirstNameReadable : function()
    {
      return this.getAttributeDTO('firstName').isReadable();
    },
    isFirstNameWritable : function()
    {
      return this.getAttributeDTO('firstName').isWritable();
    },
    isFirstNameModified : function()
    {
      return this.getAttributeDTO('firstName').isModified();
    },
    getFirstNameMd : function()
    {
      return this.getAttributeDTO('firstName').getAttributeMdDTO();
    },
    getLastName : function()
    {
      return this.getAttributeDTO('lastName').getValue();
    },
    setLastName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('lastName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isLastNameReadable : function()
    {
      return this.getAttributeDTO('lastName').isReadable();
    },
    isLastNameWritable : function()
    {
      return this.getAttributeDTO('lastName').isWritable();
    },
    isLastNameModified : function()
    {
      return this.getAttributeDTO('lastName').isModified();
    },
    getLastNameMd : function()
    {
      return this.getAttributeDTO('lastName').getAttributeMdDTO();
    },
    getPhoneNumber : function()
    {
      return this.getAttributeDTO('phoneNumber').getValue();
    },
    setPhoneNumber : function(value)
    {
      var attributeDTO = this.getAttributeDTO('phoneNumber');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPhoneNumberReadable : function()
    {
      return this.getAttributeDTO('phoneNumber').isReadable();
    },
    isPhoneNumberWritable : function()
    {
      return this.getAttributeDTO('phoneNumber').isWritable();
    },
    isPhoneNumberModified : function()
    {
      return this.getAttributeDTO('phoneNumber').isModified();
    },
    getPhoneNumberMd : function()
    {
      return this.getAttributeDTO('phoneNumber').getAttributeMdDTO();
    },
    applyWithRoles : function(clientRequest, roleIds)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.GeodashboardUser', methodName:'applyWithRoles', declaredTypes: ["[Ljava.lang.String;"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, this, [].splice.call(arguments, 1));
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"readable\":true,\"dto_type\":\"com.runwaysdk.business.BusinessDTO\",\"attributeMap\":{\"sessionLimit\":{\"attributeName\":\"sessionLimit\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000056600000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Session Limit\",\"description\":\"The number of sessions a user can open at the same time.\",\"name\":\"sessionLimit\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":true,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":1,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"lastName\":{\"attributeName\":\"lastName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ikfdh4bqzwr865cztcodm3du55iatyub00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Last name\",\"description\":\"\",\"name\":\"lastName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"lockedBy\":{\"attributeName\":\"lockedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060808NM000000000000000000005100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Locked By\",\"description\":\"User who has a write lock on this object\",\"name\":\"lockedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.Users\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"locale\":{\"attributeName\":\"locale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"20070322JN000000000000000000000600000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.metadata.UserLocales\",\"system\":false,\"displayLabel\":\"Locale\",\"description\":\"The user locale\",\"name\":\"locale\",\"immutable\":false,\"enumNames\":{},\"_selectMultiple\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"enumNames\":[],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":false},\"entityDomain\":{\"attributeName\":\"entityDomain\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"NM20081011000000000000000000006500000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Entity Domain\",\"description\":\"The Domain of this entity\",\"name\":\"entityDomain\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdDomain\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"type\":{\"attributeName\":\"type\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060808NM000000000000000000000100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Type\",\"description\":\"Fully qualified type of this object\",\"name\":\"type\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"com.runwaysdk.geodashboard.GeodashboardUser\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"password\":{\"attributeName\":\"password\",\"readable\":false,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeHashDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000002900000000000000000000000000000631\",\"system\":false,\"encryptionMethod\":\"SHA\",\"displayLabel\":\"Password\",\"description\":\"User password\",\"name\":\"password\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeHash\",\"modified\":false},\"lastUpdateDate\":{\"attributeName\":\"lastUpdateDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060808NM000000000000000000004100000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Last Update Date\",\"description\":\"Date and time this record was last updated\",\"name\":\"lastUpdateDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060808NM000000000000000000008100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"xwjxddizz3ddgaf74wjwldswqbukmpepiajn2ib5uzf6xtk7rrhkkpf9gwgz0ntn\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"inactive\":{\"attributeName\":\"inactive\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"20070320EG000000000000000000145500000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Inactive\",\"negativeDisplayLabel\":\"Active\",\"description\":\"Inactive flag of the user, when true user cannot log in.\",\"name\":\"inactive\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Inactive\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"username\":{\"attributeName\":\"username\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000002800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Username\",\"description\":\"User login\",\"name\":\"username\",\"immutable\":false,\"required\":true,\"size\":32},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080227NM000000000000000000002100000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"phoneNumber\":{\"attributeName\":\"phoneNumber\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ins3h75uenk60zm1qxgyscousxfxfsik00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Phone number\",\"description\":\"\",\"name\":\"phoneNumber\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"createdBy\":{\"attributeName\":\"createdBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060808NM000000000000000000001100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Created By\",\"description\":\"User that created this object\",\"name\":\"createdBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"JS20070104000000000000000000122100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"email\":{\"attributeName\":\"email\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i2v4nbrhw1hrq35f7927wy1nc3co02h100000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Email\",\"description\":\"\",\"name\":\"email\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"lastUpdatedBy\":{\"attributeName\":\"lastUpdatedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060808NM000000000000000000007100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Last Updated By\",\"description\":\"User who last updated this object\",\"name\":\"lastUpdatedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"seq\":{\"attributeName\":\"seq\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"20060808NM000000000000000000003100000000000000000000000000000338\",\"system\":true,\"displayLabel\":\"Sequence\",\"description\":\"Sequence number is incremented every time this object is updated\",\"name\":\"seq\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false},\"owner\":{\"attributeName\":\"owner\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060808NM000000000000000000002100000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Owner\",\"description\":\"A reference to the user who own this object\",\"name\":\"owner\",\"referencedMdBusiness\":\"com.runwaysdk.system.Actor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"createDate\":{\"attributeName\":\"createDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060808NM000000000000000000006100000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Create Date\",\"description\":\"Date and time this record was created\",\"name\":\"createDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"firstName\":{\"attributeName\":\"firstName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"iqvo81r9n4s9ohifidhhcy2ha9ij2qfe00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"First name\",\"description\":\"\",\"name\":\"firstName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"_toString\":\"New: User\",\"writable\":true,\"state\":\"\",\"modified\":true,\"id\":\"xwjxddizz3ddgaf74wjwldswqbukmpepiajn2ib5uzf6xtk7rrhkkpf9gwgz0ntn\",\"_type\":\"com.runwaysdk.geodashboard.GeodashboardUser\",\"_typeMd\":{\"id\":\"iajn2ib5uzf6xtk7rrhkkpf9gwgz0ntn00000000000000000000000000000001\",\"displayLabel\":\"User\",\"description\":\"\"},\"transitions\":[],\"newInstance\":true,\"lockedByCurrentUser\":false}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
    isRoleMemeber : function(clientRequest, roles)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.GeodashboardUser', methodName:'isRoleMemeber', declaredTypes: ["java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getCurrentUser : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.GeodashboardUser', methodName:'getCurrentUser', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    applyWithRoles : function(clientRequest, id, roleIds)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.GeodashboardUser', methodName:'applyWithRoles', declaredTypes: ["java.lang.String", "[Ljava.lang.String;"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    }
  }
});
Mojo.Meta.newClass('com.runwaysdk.geodashboard.GeodashboardUserQueryDTO', {
  Extends : 'com.runwaysdk.system.UsersQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.GeodashboardUser'
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
