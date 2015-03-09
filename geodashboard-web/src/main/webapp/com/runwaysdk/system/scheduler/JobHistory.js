Mojo.Meta.newClass('com.runwaysdk.system.scheduler.JobHistory', {
  Extends : 'com.runwaysdk.business.BusinessDTO',
  Constants : 
  {
    CREATEDATE : 'createDate',
    CREATEDBY : 'createdBy',
    ENDTIME : 'endTime',
    ENTITYDOMAIN : 'entityDomain',
    HISTORYCOMMENT : 'historyComment',
    HISTORYINFORMATION : 'historyInformation',
    ID : 'id',
    KEYNAME : 'keyName',
    LASTUPDATEDATE : 'lastUpdateDate',
    LASTUPDATEDBY : 'lastUpdatedBy',
    LOCKEDBY : 'lockedBy',
    OWNER : 'owner',
    RETRIES : 'retries',
    SEQ : 'seq',
    SITEMASTER : 'siteMaster',
    STARTTIME : 'startTime',
    STATUS : 'status',
    TYPE : 'type',
    WORKPROGRESS : 'workProgress',
    CLASS : 'com.runwaysdk.system.scheduler.JobHistory'
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
    getEndTime : function()
    {
      return this.getAttributeDTO('endTime').getValue();
    },
    setEndTime : function(value)
    {
      var attributeDTO = this.getAttributeDTO('endTime');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isEndTimeReadable : function()
    {
      return this.getAttributeDTO('endTime').isReadable();
    },
    isEndTimeWritable : function()
    {
      return this.getAttributeDTO('endTime').isWritable();
    },
    isEndTimeModified : function()
    {
      return this.getAttributeDTO('endTime').isModified();
    },
    getEndTimeMd : function()
    {
      return this.getAttributeDTO('endTime').getAttributeMdDTO();
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
    getHistoryComment : function()
    {
      if(Mojo.Meta.classExists('com.runwaysdk.system.scheduler.JobHistoryHistoryComment'))
      {
        var structDTO = this.getAttributeDTO('historyComment').getStructDTO();
        if(structDTO == null)
        {
          return null;
        }
        else if(structDTO instanceof Mojo.$.com.runwaysdk.system.scheduler.JobHistoryHistoryComment)
        {
          return structDTO;
        }
        else
        {
          structDTO = new Mojo.$.com.runwaysdk.system.scheduler.JobHistoryHistoryComment(structDTO);
          this.getAttributeDTO('historyComment').setStructDTO(structDTO);
          return structDTO;
        }
      }
      else
      {
        throw new Mojo.$.com.runwaysdk.Exception('Must import type com.runwaysdk.system.scheduler.JobHistoryHistoryComment');
      }
    },
    isHistoryCommentReadable : function()
    {
      return this.getAttributeDTO('historyComment').isReadable();
    },
    isHistoryCommentWritable : function()
    {
      return this.getAttributeDTO('historyComment').isWritable();
    },
    isHistoryCommentModified : function()
    {
      return this.getAttributeDTO('historyComment').isModified();
    },
    getHistoryCommentMd : function()
    {
      return this.getAttributeDTO('historyComment').getAttributeMdDTO();
    },
    getHistoryInformation : function()
    {
      if(Mojo.Meta.classExists('com.runwaysdk.system.scheduler.JobHistoryHistoryInformation'))
      {
        var structDTO = this.getAttributeDTO('historyInformation').getStructDTO();
        if(structDTO == null)
        {
          return null;
        }
        else if(structDTO instanceof Mojo.$.com.runwaysdk.system.scheduler.JobHistoryHistoryInformation)
        {
          return structDTO;
        }
        else
        {
          structDTO = new Mojo.$.com.runwaysdk.system.scheduler.JobHistoryHistoryInformation(structDTO);
          this.getAttributeDTO('historyInformation').setStructDTO(structDTO);
          return structDTO;
        }
      }
      else
      {
        throw new Mojo.$.com.runwaysdk.Exception('Must import type com.runwaysdk.system.scheduler.JobHistoryHistoryInformation');
      }
    },
    isHistoryInformationReadable : function()
    {
      return this.getAttributeDTO('historyInformation').isReadable();
    },
    isHistoryInformationWritable : function()
    {
      return this.getAttributeDTO('historyInformation').isWritable();
    },
    isHistoryInformationModified : function()
    {
      return this.getAttributeDTO('historyInformation').isModified();
    },
    getHistoryInformationMd : function()
    {
      return this.getAttributeDTO('historyInformation').getAttributeMdDTO();
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
    getRetries : function()
    {
      return this.getAttributeDTO('retries').getValue();
    },
    setRetries : function(value)
    {
      var attributeDTO = this.getAttributeDTO('retries');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isRetriesReadable : function()
    {
      return this.getAttributeDTO('retries').isReadable();
    },
    isRetriesWritable : function()
    {
      return this.getAttributeDTO('retries').isWritable();
    },
    isRetriesModified : function()
    {
      return this.getAttributeDTO('retries').isModified();
    },
    getRetriesMd : function()
    {
      return this.getAttributeDTO('retries').getAttributeMdDTO();
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
    getStartTime : function()
    {
      return this.getAttributeDTO('startTime').getValue();
    },
    setStartTime : function(value)
    {
      var attributeDTO = this.getAttributeDTO('startTime');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isStartTimeReadable : function()
    {
      return this.getAttributeDTO('startTime').isReadable();
    },
    isStartTimeWritable : function()
    {
      return this.getAttributeDTO('startTime').isWritable();
    },
    isStartTimeModified : function()
    {
      return this.getAttributeDTO('startTime').isModified();
    },
    getStartTimeMd : function()
    {
      return this.getAttributeDTO('startTime').getAttributeMdDTO();
    },
    getStatus : function()
    {
      var attributeDTO = this.getAttributeDTO('status');
      var names = attributeDTO.getEnumNames();
      var enums = [];
      for(var i=0; i<names.length; i++)
      {
        enums.push(Mojo.$.com.runwaysdk.system.scheduler.AllJobStatus[names[i]]);
      }
      return enums;
    },
    removeStatus : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('status');
      attributeDTO.remove(enumValue);
      this.setModified(true);
    },
    clearStatus : function()
    {
      var attributeDTO = this.getAttributeDTO('status');
      attributeDTO.clear();
      this.setModified(true);
    },
    addStatus : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('status');
      attributeDTO.add(enumValue);
      this.setModified(true);
    },
    isStatusReadable : function()
    {
      return this.getAttributeDTO('status').isReadable();
    },
    isStatusWritable : function()
    {
      return this.getAttributeDTO('status').isWritable();
    },
    isStatusModified : function()
    {
      return this.getAttributeDTO('status').isModified();
    },
    getStatusMd : function()
    {
      return this.getAttributeDTO('status').getAttributeMdDTO();
    },
    getWorkProgress : function()
    {
      return this.getAttributeDTO('workProgress').getValue();
    },
    setWorkProgress : function(value)
    {
      var attributeDTO = this.getAttributeDTO('workProgress');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isWorkProgressReadable : function()
    {
      return this.getAttributeDTO('workProgress').isReadable();
    },
    isWorkProgressWritable : function()
    {
      return this.getAttributeDTO('workProgress').isWritable();
    },
    isWorkProgressModified : function()
    {
      return this.getAttributeDTO('workProgress').isModified();
    },
    getWorkProgressMd : function()
    {
      return this.getAttributeDTO('workProgress').getAttributeMdDTO();
    },
    getAllJob : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParents(clientRequest, this.getId(), 'com.runwaysdk.system.scheduler.JobHistoryRecord');
    },
    getAllJobRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParentRelationships(clientRequest, this.getId(), 'com.runwaysdk.system.scheduler.JobHistoryRecord');
    },
    addJob : function(clientRequest, parent)
    {
      var parentId = (parent instanceof Object) ? parent.getId() : parent;
      Mojo.$.com.runwaysdk.Facade.addParent(clientRequest, parentId, this.getId(), 'com.runwaysdk.system.scheduler.JobHistoryRecord');
    },
    removeJob : function(clientRequest, relationship)
    {
      var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteParent(clientRequest, relationshipId);
    },
    removeAllJob : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteParents(clientRequest, this.getId(), 'com.runwaysdk.system.scheduler.JobHistoryRecord');
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"readable\":true,\"dto_type\":\"com.runwaysdk.business.BusinessDTO\",\"attributeMap\":{\"lockedBy\":{\"attributeName\":\"lockedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"xkravrf9mg21oha2h9byr0by0546ukgb00000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Locked By\",\"description\":\"User who has a write lock on this object\",\"name\":\"lockedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.Users\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"status\":{\"attributeName\":\"status\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"ijo9kjdvn8pk3son3xpck0dhwda6ozz500000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.scheduler.AllJobStatus\",\"system\":false,\"displayLabel\":\"Status\",\"description\":\"\",\"name\":\"status\",\"immutable\":false,\"enumNames\":{\"SUCCESS\":\"Success\",\"STOPPED\":\"Stopped\",\"RUNNING\":\"Running\",\"FAILURE\":\"Failure\",\"CANCELED\":\"Canceled\"},\"_selectMultiple\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"enumNames\":[],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":false},\"historyInformation\":{\"attributeName\":\"historyInformation\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalTextDTO\",\"attributeMdDTO\":{\"id\":\"93q4l4k5qp20ynk7n2win58beeeg2hbs000000000000MdAttributeLocalText\",\"system\":false,\"displayLabel\":\"History Information\",\"description\":\"History Information with Data and Results\",\"definingMdStruct\":\"com.runwaysdk.system.scheduler.JobHistoryHistoryInformation\",\"name\":\"historyInformation\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"0jmp9wb31b8dkj18alo28m5wqhmqgtqqqp07ac4ckrfbmoon7a84mpl6a6yzn16l\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalText\",\"structDTO\":{\"id\":\"0jmp9wb31b8dkj18alo28m5wqhmqgtqqqp07ac4ckrfbmoon7a84mpl6a6yzn16l\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.scheduler.JobHistoryHistoryInformation\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"r4llx3xvoclq434ouccd9gkojqzbfdxx00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"0jmp9wb31b8dkj18alo28m5wqhmqgtqqqp07ac4ckrfbmoon7a84mpl6a6yzn16l\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeTextDTO\",\"attributeMdDTO\":{\"id\":\"07lmuuipe34rkv3tsz1nin7m730cxbnn00000000000000000000000000000139\",\"system\":false,\"displayLabel\":\"Default Locale\",\"description\":\"Default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeText\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"szm3q72o75pm8zppby7cxncyzewltf3h00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"8ikz5kkigqmy6x52y9uf9qbvzb4o5yo700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"qp07ac4ckrfbmoon7a84mpl6a6yzn16l0000000000000000000MdLocalStruct\",\"displayLabel\":\"History Information\",\"description\":\"History Information with Data and Results\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"entityDomain\":{\"attributeName\":\"entityDomain\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"6uvky0lrl0m6dfos211gdhnybfjuk8gu00000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Entity Domain\",\"description\":\"The Domain of this entity\",\"name\":\"entityDomain\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdDomain\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"type\":{\"attributeName\":\"type\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"7g4ine1me7fx8dcxk6mktrm2n3yqtscf00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Type\",\"description\":\"Fully qualified type of this object\",\"name\":\"type\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"com.runwaysdk.system.scheduler.JobHistory\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"endTime\":{\"attributeName\":\"endTime\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"idl7r1zaflhidv1rxhpjwb3a2w9bwb4i00000000000000000000000000000341\",\"system\":false,\"displayLabel\":\"End Time\",\"description\":\"\",\"name\":\"endTime\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"lastUpdateDate\":{\"attributeName\":\"lastUpdateDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"8net1gu99hrx9u85a6mf865jlzv6aldy00000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Last Update Date\",\"description\":\"Date and time this record was last updated\",\"name\":\"lastUpdateDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"startTime\":{\"attributeName\":\"startTime\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"ilu8f4mgo8kpn8122ucvj2ttx14u98qt00000000000000000000000000000341\",\"system\":false,\"displayLabel\":\"Last Run\",\"description\":\"\",\"name\":\"startTime\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"si979x7lzl0jcbz5hooxh15ritbenpo400000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"0inj2gvduplbfgopraudkeyq9lkkcrbv2ft54d1ik9d4qmj2ffopey6uwk000fvr\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"workProgress\":{\"attributeName\":\"workProgress\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"id6b3h1iof1e4r9rusmp9vxun44928bu00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"work progress\",\"description\":\"\",\"name\":\"workProgress\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":false},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0bmtiqezkcp09rsgzj3x1vtcscju455h00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"createdBy\":{\"attributeName\":\"createdBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"y1lsaw2ocvnuw73c6sulrzfreb6li5gl00000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Created By\",\"description\":\"User that created this object\",\"name\":\"createdBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"historyComment\":{\"attributeName\":\"historyComment\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalTextDTO\",\"attributeMdDTO\":{\"id\":\"8mqqlcqjf08v64cxlmrhbzc7nsbglsgs000000000000MdAttributeLocalText\",\"system\":false,\"displayLabel\":\"Comment\",\"description\":\"User Friendly History Comment\",\"definingMdStruct\":\"com.runwaysdk.system.scheduler.JobHistoryHistoryComment\",\"name\":\"historyComment\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"zgq2gki6h1tjgd3e7ll6jyreu531zh7a8d1kfg7ldiaylha1aq1u3shemkhgxe2b\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalText\",\"structDTO\":{\"id\":\"zgq2gki6h1tjgd3e7ll6jyreu531zh7a8d1kfg7ldiaylha1aq1u3shemkhgxe2b\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.scheduler.JobHistoryHistoryComment\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"21maxir11z49qzny49vkherutvrk7xa700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"zgq2gki6h1tjgd3e7ll6jyreu531zh7a8d1kfg7ldiaylha1aq1u3shemkhgxe2b\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeTextDTO\",\"attributeMdDTO\":{\"id\":\"z98y5rsqloy7eqns94zsoaqem8jz8bq700000000000000000000000000000139\",\"system\":false,\"displayLabel\":\"Default Locale\",\"description\":\"Default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeText\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"91yplljj4tqs825r26p94jord3ucj2vt00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"8gbraozud2p9vnkm59t1p8z5rkksc6te00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"8d1kfg7ldiaylha1aq1u3shemkhgxe2b0000000000000000000MdLocalStruct\",\"displayLabel\":\"Comment\",\"description\":\"User Friendly History Comment\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"2dbd4gtevy68m9juh1qqa4dhgfi8ko7b00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"lastUpdatedBy\":{\"attributeName\":\"lastUpdatedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"zo1f8vpzfwo49l0bpv02c4370r89ogz400000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Last Updated By\",\"description\":\"User who last updated this object\",\"name\":\"lastUpdatedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"seq\":{\"attributeName\":\"seq\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"za3yz24x11lvox7bkk2l17eo2ind7nfq00000000000000000000000000000338\",\"system\":true,\"displayLabel\":\"Sequence\",\"description\":\"Sequence number is incremented every time this object is updated\",\"name\":\"seq\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false},\"owner\":{\"attributeName\":\"owner\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"t1hdn5jde23b08aclsyvwlwma7ijd84000000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Owner\",\"description\":\"A reference to the user who own this object\",\"name\":\"owner\",\"referencedMdBusiness\":\"com.runwaysdk.system.Actor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"retries\":{\"attributeName\":\"retries\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"ikgua2xn2s3hys5dhsvjcz9k5y41uul700000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Retries\",\"description\":\"\",\"name\":\"retries\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":false},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"createDate\":{\"attributeName\":\"createDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"xwh81hemts3cxodgwsla6b0bx63zuvfe00000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Create Date\",\"description\":\"Date and time this record was created\",\"name\":\"createDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false}},\"_toString\":\"New: JobHistory\",\"writable\":true,\"state\":\"\",\"modified\":true,\"id\":\"0inj2gvduplbfgopraudkeyq9lkkcrbv2ft54d1ik9d4qmj2ffopey6uwk000fvr\",\"_type\":\"com.runwaysdk.system.scheduler.JobHistory\",\"_typeMd\":{\"id\":\"2ft54d1ik9d4qmj2ffopey6uwk000fvr00000000000000000000000000000001\",\"displayLabel\":\"JobHistory\",\"description\":\"JobHistory\"},\"transitions\":[],\"newInstance\":true,\"lockedByCurrentUser\":false}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
    clearHistory : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.system.scheduler.JobHistory', methodName:'clearHistory', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    }
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.scheduler.JobHistoryQueryDTO', {
  Extends : 'com.runwaysdk.business.BusinessQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.scheduler.JobHistory'
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
