Mojo.Meta.newClass('com.runwaysdk.system.VaultFile', {
  Extends : 'com.runwaysdk.business.BusinessDTO',
  Constants : 
  {
    CREATEDATE : 'createDate',
    CREATEDBY : 'createdBy',
    ENTITYDOMAIN : 'entityDomain',
    FILEEXTENSION : 'fileExtension',
    FILENAME : 'fileName',
    FILESIZE : 'fileSize',
    ID : 'id',
    KEYNAME : 'keyName',
    LASTUPDATEDATE : 'lastUpdateDate',
    LASTUPDATEDBY : 'lastUpdatedBy',
    LOCKEDBY : 'lockedBy',
    OWNER : 'owner',
    SEQ : 'seq',
    SITEMASTER : 'siteMaster',
    TYPE : 'type',
    VAULTNAME : 'vaultName',
    VAULTPATH : 'vaultPath',
    VAULTREFERENCE : 'vaultReference',
    CLASS : 'com.runwaysdk.system.VaultFile'
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
    getFileExtension : function()
    {
      return this.getAttributeDTO('fileExtension').getValue();
    },
    setFileExtension : function(value)
    {
      var attributeDTO = this.getAttributeDTO('fileExtension');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isFileExtensionReadable : function()
    {
      return this.getAttributeDTO('fileExtension').isReadable();
    },
    isFileExtensionWritable : function()
    {
      return this.getAttributeDTO('fileExtension').isWritable();
    },
    isFileExtensionModified : function()
    {
      return this.getAttributeDTO('fileExtension').isModified();
    },
    getFileExtensionMd : function()
    {
      return this.getAttributeDTO('fileExtension').getAttributeMdDTO();
    },
    getFileName : function()
    {
      return this.getAttributeDTO('fileName').getValue();
    },
    setFileName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('fileName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isFileNameReadable : function()
    {
      return this.getAttributeDTO('fileName').isReadable();
    },
    isFileNameWritable : function()
    {
      return this.getAttributeDTO('fileName').isWritable();
    },
    isFileNameModified : function()
    {
      return this.getAttributeDTO('fileName').isModified();
    },
    getFileNameMd : function()
    {
      return this.getAttributeDTO('fileName').getAttributeMdDTO();
    },
    getFileSize : function()
    {
      return this.getAttributeDTO('fileSize').getValue();
    },
    isFileSizeReadable : function()
    {
      return this.getAttributeDTO('fileSize').isReadable();
    },
    isFileSizeWritable : function()
    {
      return this.getAttributeDTO('fileSize').isWritable();
    },
    isFileSizeModified : function()
    {
      return this.getAttributeDTO('fileSize').isModified();
    },
    getFileSizeMd : function()
    {
      return this.getAttributeDTO('fileSize').getAttributeMdDTO();
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
    getVaultName : function()
    {
      return this.getAttributeDTO('vaultName').getValue();
    },
    isVaultNameReadable : function()
    {
      return this.getAttributeDTO('vaultName').isReadable();
    },
    isVaultNameWritable : function()
    {
      return this.getAttributeDTO('vaultName').isWritable();
    },
    isVaultNameModified : function()
    {
      return this.getAttributeDTO('vaultName').isModified();
    },
    getVaultNameMd : function()
    {
      return this.getAttributeDTO('vaultName').getAttributeMdDTO();
    },
    getVaultPath : function()
    {
      return this.getAttributeDTO('vaultPath').getValue();
    },
    isVaultPathReadable : function()
    {
      return this.getAttributeDTO('vaultPath').isReadable();
    },
    isVaultPathWritable : function()
    {
      return this.getAttributeDTO('vaultPath').isWritable();
    },
    isVaultPathModified : function()
    {
      return this.getAttributeDTO('vaultPath').isModified();
    },
    getVaultPathMd : function()
    {
      return this.getAttributeDTO('vaultPath').getAttributeMdDTO();
    },
    getVaultReference : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('vaultReference');
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
    isVaultReferenceReadable : function()
    {
      return this.getAttributeDTO('vaultReference').isReadable();
    },
    isVaultReferenceWritable : function()
    {
      return this.getAttributeDTO('vaultReference').isWritable();
    },
    isVaultReferenceModified : function()
    {
      return this.getAttributeDTO('vaultReference').isModified();
    },
    getVaultReferenceMd : function()
    {
      return this.getAttributeDTO('vaultReference').getAttributeMdDTO();
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"readable\":true,\"dto_type\":\"com.runwaysdk.business.BusinessDTO\",\"attributeMap\":{\"fileExtension\":{\"attributeName\":\"fileExtension\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060911JS000000000000000000083700000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"File Extension\",\"description\":\"The file extension\",\"name\":\"fileExtension\",\"immutable\":false,\"required\":false,\"size\":10},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"lockedBy\":{\"attributeName\":\"lockedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060911JS000000000000000000084500000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Locked By\",\"description\":\"User who has a write lock on this object\",\"name\":\"lockedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.Users\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"vaultPath\":{\"attributeName\":\"vaultPath\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060911JS000000000000000000083400000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Vault Path\",\"description\":\"The vault path of the file\",\"name\":\"vaultPath\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"entityDomain\":{\"attributeName\":\"entityDomain\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"NM20081011000000000000000000007100000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Entity Domain\",\"description\":\"The Domain of this entity\",\"name\":\"entityDomain\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdDomain\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"type\":{\"attributeName\":\"type\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060911JS000000000000000000085500000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Type\",\"description\":\"Fully qualified type of this object\",\"name\":\"type\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"com.runwaysdk.system.VaultFile\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"vaultReference\":{\"attributeName\":\"vaultReference\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060911JS000000000000000000083100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Vault Reference\",\"description\":\"The vault this file is stored within\",\"name\":\"vaultReference\",\"referencedMdBusiness\":\"com.runwaysdk.system.Vault\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"lastUpdateDate\":{\"attributeName\":\"lastUpdateDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060911JS000000000000000000084700000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Last Update Date\",\"description\":\"Date and time this record was last updated\",\"name\":\"lastUpdateDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"fileSize\":{\"attributeName\":\"fileSize\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"20080403NM000000000000000000015000000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"File Size\",\"description\":\"Size of the file in bytes.  Not necessarily the size of the file on the file system.\",\"name\":\"fileSize\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":false},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060911JS000000000000000000085700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"xypvnjn2fjwg4mps6ianqawd02wi9yrn20060911JS0000000000000000000829\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080227NM000000000000000000002500000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"createdBy\":{\"attributeName\":\"createdBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060911JS000000000000000000085300000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Created By\",\"description\":\"User that created this object\",\"name\":\"createdBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"JS20070104000000000000000000124800000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"lastUpdatedBy\":{\"attributeName\":\"lastUpdatedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060911JS000000000000000000084900000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Last Updated By\",\"description\":\"User who last updated this object\",\"name\":\"lastUpdatedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"fileName\":{\"attributeName\":\"fileName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060911JS000000000000000000083900000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"File Name\",\"description\":\"The file name\",\"name\":\"fileName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"owner\":{\"attributeName\":\"owner\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"20060911JS000000000000000000084100000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Owner\",\"description\":\"A reference to the user who own this object\",\"name\":\"owner\",\"referencedMdBusiness\":\"com.runwaysdk.system.Actor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"seq\":{\"attributeName\":\"seq\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"20060911JS000000000000000000084300000000000000000000000000000338\",\"system\":true,\"displayLabel\":\"Sequence\",\"description\":\"Sequence number is incremented every time this object is updated\",\"name\":\"seq\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false},\"vaultName\":{\"attributeName\":\"vaultName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20060911JS000000000000000000083300000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Vault Name\",\"description\":\"The file name inside of the vault\",\"name\":\"vaultName\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"createDate\":{\"attributeName\":\"createDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"20060911JS000000000000000000085100000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Create Date\",\"description\":\"Date and time this record was created\",\"name\":\"createDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false}},\"_toString\":\"New: VaultFile\",\"writable\":true,\"state\":\"\",\"modified\":true,\"id\":\"xypvnjn2fjwg4mps6ianqawd02wi9yrn20060911JS0000000000000000000829\",\"_type\":\"com.runwaysdk.system.VaultFile\",\"_typeMd\":{\"id\":\"20060911JS000000000000000000082900000000000000000000000000000001\",\"displayLabel\":\"VaultFile\",\"description\":\"A file in the file vault\"},\"transitions\":[],\"newInstance\":true,\"lockedByCurrentUser\":false}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.VaultFileQueryDTO', {
  Extends : 'com.runwaysdk.business.BusinessQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.VaultFile'
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
