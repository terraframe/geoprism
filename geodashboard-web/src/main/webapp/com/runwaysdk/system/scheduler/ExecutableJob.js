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
Mojo.Meta.newClass('com.runwaysdk.system.scheduler.ExecutableJob', {
  Extends : 'com.runwaysdk.system.scheduler.AbstractJob',
  IsAbstract : true,
  Constants : 
  {
    DESCRIPTION : 'description',
    ENTRYDATE : 'entryDate',
    JOBID : 'jobId',
    RECORDHISTORY : 'recordHistory',
    CLASS : 'com.runwaysdk.system.scheduler.ExecutableJob'
  },
  Instance: 
  {
    getDescription : function()
    {
      if(Mojo.Meta.classExists('com.runwaysdk.system.scheduler.ExecutableJobDescription'))
      {
        var structDTO = this.getAttributeDTO('description').getStructDTO();
        if(structDTO == null)
        {
          return null;
        }
        else if(structDTO instanceof Mojo.$.com.runwaysdk.system.scheduler.ExecutableJobDescription)
        {
          return structDTO;
        }
        else
        {
          structDTO = new Mojo.$.com.runwaysdk.system.scheduler.ExecutableJobDescription(structDTO);
          this.getAttributeDTO('description').setStructDTO(structDTO);
          return structDTO;
        }
      }
      else
      {
        throw new Mojo.$.com.runwaysdk.Exception('Must import type com.runwaysdk.system.scheduler.ExecutableJobDescription');
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
    getEntryDate : function()
    {
      return this.getAttributeDTO('entryDate').getValue();
    },
    setEntryDate : function(value)
    {
      var attributeDTO = this.getAttributeDTO('entryDate');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isEntryDateReadable : function()
    {
      return this.getAttributeDTO('entryDate').isReadable();
    },
    isEntryDateWritable : function()
    {
      return this.getAttributeDTO('entryDate').isWritable();
    },
    isEntryDateModified : function()
    {
      return this.getAttributeDTO('entryDate').isModified();
    },
    getEntryDateMd : function()
    {
      return this.getAttributeDTO('entryDate').getAttributeMdDTO();
    },
    getJobId : function()
    {
      return this.getAttributeDTO('jobId').getValue();
    },
    setJobId : function(value)
    {
      var attributeDTO = this.getAttributeDTO('jobId');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isJobIdReadable : function()
    {
      return this.getAttributeDTO('jobId').isReadable();
    },
    isJobIdWritable : function()
    {
      return this.getAttributeDTO('jobId').isWritable();
    },
    isJobIdModified : function()
    {
      return this.getAttributeDTO('jobId').isModified();
    },
    getJobIdMd : function()
    {
      return this.getAttributeDTO('jobId').getAttributeMdDTO();
    },
    getRecordHistory : function()
    {
      return this.getAttributeDTO('recordHistory').getValue();
    },
    setRecordHistory : function(value)
    {
      var attributeDTO = this.getAttributeDTO('recordHistory');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isRecordHistoryReadable : function()
    {
      return this.getAttributeDTO('recordHistory').isReadable();
    },
    isRecordHistoryWritable : function()
    {
      return this.getAttributeDTO('recordHistory').isWritable();
    },
    isRecordHistoryModified : function()
    {
      return this.getAttributeDTO('recordHistory').isModified();
    },
    getRecordHistoryMd : function()
    {
      return this.getAttributeDTO('recordHistory').getAttributeMdDTO();
    },
    cancel : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.system.scheduler.ExecutableJob', methodName:'cancel', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, this, [].splice.call(arguments, 1));
    },
    stop : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.system.scheduler.ExecutableJob', methodName:'stop', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, this, [].splice.call(arguments, 1));
    },
    start : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.system.scheduler.ExecutableJob', methodName:'start', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, this, [].splice.call(arguments, 1));
    },
    pause : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.system.scheduler.ExecutableJob', methodName:'pause', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, this, [].splice.call(arguments, 1));
    },
    resume : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.system.scheduler.ExecutableJob', methodName:'resume', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, this, [].splice.call(arguments, 1));
    },
    getAllJobHistory : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildren(clientRequest, this.getId(), 'com.runwaysdk.system.scheduler.JobHistoryRecord');
    },
    getAllJobHistoryRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.scheduler.JobHistoryRecord');
    },
    addJobHistory : function(clientRequest, child)
    {
      var childId = (child instanceof Object) ? child.getId() : child;
      Mojo.$.com.runwaysdk.Facade.addChild(clientRequest, this.getId(), childId, 'com.runwaysdk.system.scheduler.JobHistoryRecord');
    },
    removeJobHistory : function(clientRequest, relationship)
    {
        var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteChild(clientRequest, relationshipId);
    },
    removeAllJobHistory : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteChildren(clientRequest, this.getId(), 'com.runwaysdk.system.scheduler.JobHistoryRecord');
    },
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  },
  Static: 
  {
    cancel : function(clientRequest, id)
    {
      var metadata = {className:'com.runwaysdk.system.scheduler.ExecutableJob', methodName:'cancel', declaredTypes: ["java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    stop : function(clientRequest, id)
    {
      var metadata = {className:'com.runwaysdk.system.scheduler.ExecutableJob', methodName:'stop', declaredTypes: ["java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    start : function(clientRequest, id)
    {
      var metadata = {className:'com.runwaysdk.system.scheduler.ExecutableJob', methodName:'start', declaredTypes: ["java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    pause : function(clientRequest, id)
    {
      var metadata = {className:'com.runwaysdk.system.scheduler.ExecutableJob', methodName:'pause', declaredTypes: ["java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    resume : function(clientRequest, id)
    {
      var metadata = {className:'com.runwaysdk.system.scheduler.ExecutableJob', methodName:'resume', declaredTypes: ["java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    }
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.scheduler.ExecutableJobQueryDTO', {
  Extends : 'com.runwaysdk.system.scheduler.AbstractJobQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.scheduler.ExecutableJob'
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
