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
