Mojo.Meta.newClass('com.runwaysdk.system.scheduler.AbstractJob', {
  Extends : 'com.runwaysdk.business.BusinessDTO',
  IsAbstract : true,
  Constants : 
  {
    CREATEDATE : 'createDate',
    CREATEDBY : 'createdBy',
    CRONEXPRESSION : 'cronExpression',
    ENTITYDOMAIN : 'entityDomain',
    ID : 'id',
    JOBOPERATION : 'jobOperation',
    KEYNAME : 'keyName',
    LASTRUN : 'lastRun',
    LASTUPDATEDATE : 'lastUpdateDate',
    LASTUPDATEDBY : 'lastUpdatedBy',
    LOCKEDBY : 'lockedBy',
    MAXRETRIES : 'maxRetries',
    OWNER : 'owner',
    SEQ : 'seq',
    SITEMASTER : 'siteMaster',
    TIMEOUT : 'timeout',
    TYPE : 'type',
    WORKTOTAL : 'workTotal',
    CLASS : 'com.runwaysdk.system.scheduler.AbstractJob'
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
    getCronExpression : function()
    {
      return this.getAttributeDTO('cronExpression').getValue();
    },
    setCronExpression : function(value)
    {
      var attributeDTO = this.getAttributeDTO('cronExpression');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isCronExpressionReadable : function()
    {
      return this.getAttributeDTO('cronExpression').isReadable();
    },
    isCronExpressionWritable : function()
    {
      return this.getAttributeDTO('cronExpression').isWritable();
    },
    isCronExpressionModified : function()
    {
      return this.getAttributeDTO('cronExpression').isModified();
    },
    getCronExpressionMd : function()
    {
      return this.getAttributeDTO('cronExpression').getAttributeMdDTO();
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
    getJobOperation : function()
    {
      var attributeDTO = this.getAttributeDTO('jobOperation');
      var names = attributeDTO.getEnumNames();
      var enums = [];
      for(var i=0; i<names.length; i++)
      {
        enums.push(Mojo.$.com.runwaysdk.system.scheduler.AllJobOperation[names[i]]);
      }
      return enums;
    },
    removeJobOperation : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('jobOperation');
      attributeDTO.remove(enumValue);
      this.setModified(true);
    },
    clearJobOperation : function()
    {
      var attributeDTO = this.getAttributeDTO('jobOperation');
      attributeDTO.clear();
      this.setModified(true);
    },
    addJobOperation : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('jobOperation');
      attributeDTO.add(enumValue);
      this.setModified(true);
    },
    isJobOperationReadable : function()
    {
      return this.getAttributeDTO('jobOperation').isReadable();
    },
    isJobOperationWritable : function()
    {
      return this.getAttributeDTO('jobOperation').isWritable();
    },
    isJobOperationModified : function()
    {
      return this.getAttributeDTO('jobOperation').isModified();
    },
    getJobOperationMd : function()
    {
      return this.getAttributeDTO('jobOperation').getAttributeMdDTO();
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
    getLastRun : function()
    {
      return this.getAttributeDTO('lastRun').getValue();
    },
    setLastRun : function(value)
    {
      var attributeDTO = this.getAttributeDTO('lastRun');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isLastRunReadable : function()
    {
      return this.getAttributeDTO('lastRun').isReadable();
    },
    isLastRunWritable : function()
    {
      return this.getAttributeDTO('lastRun').isWritable();
    },
    isLastRunModified : function()
    {
      return this.getAttributeDTO('lastRun').isModified();
    },
    getLastRunMd : function()
    {
      return this.getAttributeDTO('lastRun').getAttributeMdDTO();
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
    getMaxRetries : function()
    {
      return this.getAttributeDTO('maxRetries').getValue();
    },
    setMaxRetries : function(value)
    {
      var attributeDTO = this.getAttributeDTO('maxRetries');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isMaxRetriesReadable : function()
    {
      return this.getAttributeDTO('maxRetries').isReadable();
    },
    isMaxRetriesWritable : function()
    {
      return this.getAttributeDTO('maxRetries').isWritable();
    },
    isMaxRetriesModified : function()
    {
      return this.getAttributeDTO('maxRetries').isModified();
    },
    getMaxRetriesMd : function()
    {
      return this.getAttributeDTO('maxRetries').getAttributeMdDTO();
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
    getTimeout : function()
    {
      return this.getAttributeDTO('timeout').getValue();
    },
    setTimeout : function(value)
    {
      var attributeDTO = this.getAttributeDTO('timeout');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isTimeoutReadable : function()
    {
      return this.getAttributeDTO('timeout').isReadable();
    },
    isTimeoutWritable : function()
    {
      return this.getAttributeDTO('timeout').isWritable();
    },
    isTimeoutModified : function()
    {
      return this.getAttributeDTO('timeout').isModified();
    },
    getTimeoutMd : function()
    {
      return this.getAttributeDTO('timeout').getAttributeMdDTO();
    },
    getWorkTotal : function()
    {
      return this.getAttributeDTO('workTotal').getValue();
    },
    setWorkTotal : function(value)
    {
      var attributeDTO = this.getAttributeDTO('workTotal');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isWorkTotalReadable : function()
    {
      return this.getAttributeDTO('workTotal').isReadable();
    },
    isWorkTotalWritable : function()
    {
      return this.getAttributeDTO('workTotal').isWritable();
    },
    isWorkTotalModified : function()
    {
      return this.getAttributeDTO('workTotal').isModified();
    },
    getWorkTotalMd : function()
    {
      return this.getAttributeDTO('workTotal').getAttributeMdDTO();
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
Mojo.Meta.newClass('com.runwaysdk.system.scheduler.AbstractJobQueryDTO', {
  Extends : 'com.runwaysdk.business.BusinessQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.scheduler.AbstractJob'
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
Mojo.Meta.newClass('com.runwaysdk.system.scheduler.JobSnapshot', {
  Extends : 'com.runwaysdk.system.scheduler.AbstractJob',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.scheduler.JobSnapshot'
  },
  Instance: 
  {
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"readable\":true,\"dto_type\":\"com.runwaysdk.business.BusinessDTO\",\"attributeMap\":{\"maxRetries\":{\"attributeName\":\"maxRetries\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"zncr6whvppf4auqpzfnb2wonimgvwhw000000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Max Retries\",\"description\":\"Max Retries\",\"name\":\"maxRetries\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"lockedBy\":{\"attributeName\":\"lockedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"07znvsbjpd3ljrlvy9d43aayv3yvm5xx00000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Locked By\",\"description\":\"User who has a write lock on this object\",\"name\":\"lockedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.Users\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"lastRun\":{\"attributeName\":\"lastRun\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"zju51m0wwboc8dnzczed7pmefwzdlgv000000000000000000000000000000341\",\"system\":false,\"displayLabel\":\"Last Run\",\"description\":\"Last Run\",\"name\":\"lastRun\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"entityDomain\":{\"attributeName\":\"entityDomain\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"0421p92m5c8nxj3elcpoygbra137xrrv00000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Entity Domain\",\"description\":\"The Domain of this entity\",\"name\":\"entityDomain\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdDomain\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"type\":{\"attributeName\":\"type\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"2eezcevqb4tlqinyu3dgpsvcn45bf9i800000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Type\",\"description\":\"Fully qualified type of this object\",\"name\":\"type\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"com.runwaysdk.system.scheduler.JobSnapshot\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"lastUpdateDate\":{\"attributeName\":\"lastUpdateDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"t1nisso05vc6hasbqdpjmtu00gmj6cpi00000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Last Update Date\",\"description\":\"Date and time this record was last updated\",\"name\":\"lastUpdateDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"22h9nmusqjkr1mnscd48u42vphnbckgh00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"8leyvaxv6iu8ezjhwo77efoyulf6esoq2hhpg3x31r0t11svybr296b66ilnuk6r\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"jobOperation\":{\"attributeName\":\"jobOperation\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"szzb0q8oojv78hfuu4axc52t688pxbio00000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.scheduler.AllJobOperation\",\"system\":false,\"displayLabel\":\"Job Operation\",\"description\":\"The current Job Operation called on the Job.\",\"name\":\"jobOperation\",\"immutable\":false,\"enumNames\":{\"RESUME\":\"Resume\",\"START\":\"Start\",\"STOP\":\"Stop\",\"PAUSE\":\"Pause\",\"CANCEL\":\"Cancel\"},\"_selectMultiple\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"enumNames\":[],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"8glnx8r2jh0lgiv05xoyjejgrtp95tdk00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"createdBy\":{\"attributeName\":\"createdBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"soq5xdumnsf5o7ygoopr6xvc3gv7t6b800000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Created By\",\"description\":\"User that created this object\",\"name\":\"createdBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"07qjm03asvsj2bw5wls8w4l3og7nm7e800000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"lastUpdatedBy\":{\"attributeName\":\"lastUpdatedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"t0fnw6kgnayhdfq1m5udml6smyr17u9e00000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Last Updated By\",\"description\":\"User who last updated this object\",\"name\":\"lastUpdatedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"seq\":{\"attributeName\":\"seq\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"xzvi05ab43b3e8mpfr8lfpracj0iuf2z00000000000000000000000000000338\",\"system\":true,\"displayLabel\":\"Sequence\",\"description\":\"Sequence number is incremented every time this object is updated\",\"name\":\"seq\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false},\"owner\":{\"attributeName\":\"owner\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"932tehfp7lak98pwtzlaut4ormt5fftn00000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Owner\",\"description\":\"A reference to the user who own this object\",\"name\":\"owner\",\"referencedMdBusiness\":\"com.runwaysdk.system.Actor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"workTotal\":{\"attributeName\":\"workTotal\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"z8g4l4wyli8gnn1vi83cz5y4sc4ckfyk00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Work Total\",\"description\":\"Work Total\",\"name\":\"workTotal\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"createDate\":{\"attributeName\":\"createDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"0a9o7jd6ddf1sx1fjlsgkohnjt32mmt600000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Create Date\",\"description\":\"Date and time this record was created\",\"name\":\"createDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"cronExpression\":{\"attributeName\":\"cronExpression\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"28pjirwu8nu9g2f5e2j7i0umnm5crnpd00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Cron Expression\",\"description\":\"Cron Expression\",\"name\":\"cronExpression\",\"immutable\":false,\"required\":false,\"size\":60},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"timeout\":{\"attributeName\":\"timeout\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"yz5jdl3l8yaxk1xzav00ctaygx9c8tgi00000000000000000000000000000338\",\"system\":false,\"displayLabel\":\"Timeout\",\"description\":\"Timeout\",\"name\":\"timeout\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false}},\"_toString\":\"New: JobSnapshot\",\"writable\":true,\"state\":\"\",\"modified\":true,\"id\":\"8leyvaxv6iu8ezjhwo77efoyulf6esoq2hhpg3x31r0t11svybr296b66ilnuk6r\",\"_type\":\"com.runwaysdk.system.scheduler.JobSnapshot\",\"_typeMd\":{\"id\":\"2hhpg3x31r0t11svybr296b66ilnuk6r00000000000000000000000000000001\",\"displayLabel\":\"JobSnapshot\",\"description\":\"JobSnapshot\"},\"transitions\":[],\"newInstance\":true,\"lockedByCurrentUser\":false}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.scheduler.JobSnapshotQueryDTO', {
  Extends : 'com.runwaysdk.system.scheduler.AbstractJobQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.scheduler.JobSnapshot'
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
