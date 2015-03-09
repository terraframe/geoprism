Mojo.Meta.newClass('com.runwaysdk.system.transaction.TransactionItem', {
  Extends : 'com.runwaysdk.business.BusinessDTO',
  Constants : 
  {
    COMPONENTID : 'componentId',
    COMPONENTSEQ : 'componentSeq',
    COMPONENTSITEMASTER : 'componentSiteMaster',
    CREATEDATE : 'createDate',
    CREATEDBY : 'createdBy',
    ENTITYDOMAIN : 'entityDomain',
    ID : 'id',
    IGNORESEQUENCENUMBER : 'ignoreSequenceNumber',
    ITEMACTION : 'itemAction',
    KEYNAME : 'keyName',
    LASTUPDATEDATE : 'lastUpdateDate',
    LASTUPDATEDBY : 'lastUpdatedBy',
    LOCKEDBY : 'lockedBy',
    OWNER : 'owner',
    SEQ : 'seq',
    SITEMASTER : 'siteMaster',
    TRANSACTIONRECORD : 'transactionRecord',
    TYPE : 'type',
    XMLRECORD : 'xmlRecord',
    CLASS : 'com.runwaysdk.system.transaction.TransactionItem'
  },
  Instance: 
  {
    getComponentId : function()
    {
      return this.getAttributeDTO('componentId').getValue();
    },
    isComponentIdReadable : function()
    {
      return this.getAttributeDTO('componentId').isReadable();
    },
    isComponentIdWritable : function()
    {
      return this.getAttributeDTO('componentId').isWritable();
    },
    isComponentIdModified : function()
    {
      return this.getAttributeDTO('componentId').isModified();
    },
    getComponentIdMd : function()
    {
      return this.getAttributeDTO('componentId').getAttributeMdDTO();
    },
    getComponentSeq : function()
    {
      return this.getAttributeDTO('componentSeq').getValue();
    },
    isComponentSeqReadable : function()
    {
      return this.getAttributeDTO('componentSeq').isReadable();
    },
    isComponentSeqWritable : function()
    {
      return this.getAttributeDTO('componentSeq').isWritable();
    },
    isComponentSeqModified : function()
    {
      return this.getAttributeDTO('componentSeq').isModified();
    },
    getComponentSeqMd : function()
    {
      return this.getAttributeDTO('componentSeq').getAttributeMdDTO();
    },
    getComponentSiteMaster : function()
    {
      return this.getAttributeDTO('componentSiteMaster').getValue();
    },
    isComponentSiteMasterReadable : function()
    {
      return this.getAttributeDTO('componentSiteMaster').isReadable();
    },
    isComponentSiteMasterWritable : function()
    {
      return this.getAttributeDTO('componentSiteMaster').isWritable();
    },
    isComponentSiteMasterModified : function()
    {
      return this.getAttributeDTO('componentSiteMaster').isModified();
    },
    getComponentSiteMasterMd : function()
    {
      return this.getAttributeDTO('componentSiteMaster').getAttributeMdDTO();
    },
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
    getIgnoreSequenceNumber : function()
    {
      return this.getAttributeDTO('ignoreSequenceNumber').getValue();
    },
    setIgnoreSequenceNumber : function(value)
    {
      var attributeDTO = this.getAttributeDTO('ignoreSequenceNumber');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isIgnoreSequenceNumberReadable : function()
    {
      return this.getAttributeDTO('ignoreSequenceNumber').isReadable();
    },
    isIgnoreSequenceNumberWritable : function()
    {
      return this.getAttributeDTO('ignoreSequenceNumber').isWritable();
    },
    isIgnoreSequenceNumberModified : function()
    {
      return this.getAttributeDTO('ignoreSequenceNumber').isModified();
    },
    getIgnoreSequenceNumberMd : function()
    {
      return this.getAttributeDTO('ignoreSequenceNumber').getAttributeMdDTO();
    },
    getItemAction : function()
    {
      var attributeDTO = this.getAttributeDTO('itemAction');
      var names = attributeDTO.getEnumNames();
      var enums = [];
      for(var i=0; i<names.length; i++)
      {
        enums.push(Mojo.$.com.runwaysdk.system.transaction.ActionEnum[names[i]]);
      }
      return enums;
    },
    isItemActionReadable : function()
    {
      return this.getAttributeDTO('itemAction').isReadable();
    },
    isItemActionWritable : function()
    {
      return this.getAttributeDTO('itemAction').isWritable();
    },
    isItemActionModified : function()
    {
      return this.getAttributeDTO('itemAction').isModified();
    },
    getItemActionMd : function()
    {
      return this.getAttributeDTO('itemAction').getAttributeMdDTO();
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
    getTransactionRecord : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('transactionRecord');
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
    isTransactionRecordReadable : function()
    {
      return this.getAttributeDTO('transactionRecord').isReadable();
    },
    isTransactionRecordWritable : function()
    {
      return this.getAttributeDTO('transactionRecord').isWritable();
    },
    isTransactionRecordModified : function()
    {
      return this.getAttributeDTO('transactionRecord').isModified();
    },
    getTransactionRecordMd : function()
    {
      return this.getAttributeDTO('transactionRecord').getAttributeMdDTO();
    },
    getXmlRecord : function()
    {
      return this.getAttributeDTO('xmlRecord').getValue();
    },
    setXmlRecord : function(value)
    {
      var attributeDTO = this.getAttributeDTO('xmlRecord');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isXmlRecordReadable : function()
    {
      return this.getAttributeDTO('xmlRecord').isReadable();
    },
    isXmlRecordWritable : function()
    {
      return this.getAttributeDTO('xmlRecord').isWritable();
    },
    isXmlRecordModified : function()
    {
      return this.getAttributeDTO('xmlRecord').isModified();
    },
    getXmlRecordMd : function()
    {
      return this.getAttributeDTO('xmlRecord').getAttributeMdDTO();
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"readable\":true,\"dto_type\":\"com.runwaysdk.business.BusinessDTO\",\"attributeMap\":{\"lockedBy\":{\"attributeName\":\"lockedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"6xs3q74znk8jwapp9k7z9uzesxbrsq9500000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Locked By\",\"description\":\"User who has a write lock on this object\",\"name\":\"lockedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.Users\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"xmlRecord\":{\"attributeName\":\"xmlRecord\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeClobDTO\",\"attributeMdDTO\":{\"id\":\"0d3j6jnotv7bcwuv83j1w7o4tltte71z20101231NM0000000000000000000010\",\"system\":false,\"displayLabel\":\"XML Record\",\"description\":\"Object serialized into XML\",\"name\":\"xmlRecord\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeClob\",\"modified\":false},\"componentSeq\":{\"attributeName\":\"componentSeq\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"2evkoasamldsls40om4lp5vyzld3ygkv00000000000000000000000000000338\",\"system\":true,\"displayLabel\":\"Component Sequence\",\"description\":\"\",\"name\":\"componentSeq\",\"immutable\":true,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":false},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false},\"entityDomain\":{\"attributeName\":\"entityDomain\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"0f01fbmjaekhawp81ncjnyquozpk8vjd00000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Entity Domain\",\"description\":\"The Domain of this entity\",\"name\":\"entityDomain\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdDomain\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"type\":{\"attributeName\":\"type\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"zchbjcodfa88rpa81up2qovrvsi8m05d00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Type\",\"description\":\"Fully qualified type of this object\",\"name\":\"type\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"com.runwaysdk.system.transaction.TransactionItem\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"componentSiteMaster\":{\"attributeName\":\"componentSiteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"8h32ky9vjp52bodcokyuoedpnk8jnu3100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Component Site Master\",\"description\":\"\",\"name\":\"componentSiteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"componentId\":{\"attributeName\":\"componentId\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"rdrqymholtvrcgxovg8k1im1bzk8hi8w00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ComponentId\",\"description\":\"\",\"name\":\"componentId\",\"immutable\":true,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"itemAction\":{\"attributeName\":\"itemAction\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"r6gin6j5s5ezvh1xbsl57l9to47w2hgp00000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.transaction.ActionEnum\",\"system\":true,\"displayLabel\":\"Action\",\"description\":\"\",\"name\":\"itemAction\",\"immutable\":true,\"enumNames\":{\"UPDATE\":\"Update\",\"CREATE\":\"Create\",\"DELETE\":\"Delete\",\"NO_OPERATION\":\"No Operation\"},\"_selectMultiple\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"enumNames\":[],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":false},\"lastUpdateDate\":{\"attributeName\":\"lastUpdateDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"26bu2r5x28g0ety6zeuimph0rbj7a85h00000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Last Update Date\",\"description\":\"Date and time this record was last updated\",\"name\":\"lastUpdateDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"seaz7608utr9aeytwete0skzd87woxx100000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"8ic117r39v10itco97zts7fkzddxu4ru70vh63h88xco3vu6mmr1d4segrzlwfto\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"xk8tn2z4m4wdq3gh3ua0qlh16td477ve00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"createdBy\":{\"attributeName\":\"createdBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"yz0g1atim4b1dxk07jg7qqk443263tug00000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Created By\",\"description\":\"User that created this object\",\"name\":\"createdBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"x9orkdjybadh8inn6kb9jybpeaxzef8g00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"lastUpdatedBy\":{\"attributeName\":\"lastUpdatedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"8uf1zfz6r73jm7up9xk4an2d67yvbged00000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Last Updated By\",\"description\":\"User who last updated this object\",\"name\":\"lastUpdatedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"seq\":{\"attributeName\":\"seq\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"23ab0inirltoy0hwjntgrh4oll5hnam100000000000000000000000000000338\",\"system\":true,\"displayLabel\":\"Sequence\",\"description\":\"Sequence number is incremented every time this object is updated\",\"name\":\"seq\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false},\"owner\":{\"attributeName\":\"owner\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"8tzumwfasf2qys1zvkottbllnuojgumb00000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Owner\",\"description\":\"A reference to the user who own this object\",\"name\":\"owner\",\"referencedMdBusiness\":\"com.runwaysdk.system.Actor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"transactionRecord\":{\"attributeName\":\"transactionRecord\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"0sf7qj2idez5lqimdnh847hhn0gub9qy00000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Transaction Record\",\"description\":\"\",\"name\":\"transactionRecord\",\"referencedMdBusiness\":\"com.runwaysdk.system.transaction.TransactionRecord\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"createDate\":{\"attributeName\":\"createDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"6p66qpgd4h8jj9edsg6i71l5oby1ifsj00000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Create Date\",\"description\":\"Date and time this record was created\",\"name\":\"createDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"ignoreSequenceNumber\":{\"attributeName\":\"ignoreSequenceNumber\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"xin3qzttxv8s5v9qreoswhcuoue2p0aa00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Ignore sequence number\",\"negativeDisplayLabel\":\"false\",\"description\":\"\",\"name\":\"ignoreSequenceNumber\",\"immutable\":false,\"required\":false,\"positiveDisplayLabel\":\"true\"},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false}},\"_toString\":\"New: Transaction Item\",\"writable\":true,\"state\":\"\",\"modified\":true,\"id\":\"8ic117r39v10itco97zts7fkzddxu4ru70vh63h88xco3vu6mmr1d4segrzlwfto\",\"_type\":\"com.runwaysdk.system.transaction.TransactionItem\",\"_typeMd\":{\"id\":\"70vh63h88xco3vu6mmr1d4segrzlwfto00000000000000000000000000000001\",\"displayLabel\":\"Transaction Item\",\"description\":\"Individual item in a transaction\"},\"transitions\":[],\"newInstance\":true,\"lockedByCurrentUser\":false}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.transaction.TransactionItemQueryDTO', {
  Extends : 'com.runwaysdk.business.BusinessQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.transaction.TransactionItem'
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
