Mojo.Meta.newClass('com.runwaysdk.system.scheduler.JobHistoryView', {
  Extends : 'com.runwaysdk.business.ViewDTO',
  Constants : 
  {
    CREATEDATE : 'createDate',
    CRONEXPRESSION : 'cronExpression',
    DESCRIPTION : 'description',
    ENDTIME : 'endTime',
    HISTORYCOMMENT : 'historyComment',
    HISTORYINFORMATION : 'historyInformation',
    ID : 'id',
    JOBID : 'jobId',
    JOBOPERATION : 'jobOperation',
    LASTRUN : 'lastRun',
    MAXRETRIES : 'maxRetries',
    RETRIES : 'retries',
    STARTTIME : 'startTime',
    STATUS : 'status',
    STATUSLABEL : 'statusLabel',
    TIMEOUT : 'timeout',
    WORKPROGRESS : 'workProgress',
    WORKTOTAL : 'workTotal',
    CLASS : 'com.runwaysdk.system.scheduler.JobHistoryView'
  },
  Instance: 
  {
    getCreateDate : function()
    {
      return this.getAttributeDTO('createDate').getValue();
    },
    setCreateDate : function(value)
    {
      var attributeDTO = this.getAttributeDTO('createDate');
      attributeDTO.setValue(value);
      this.setModified(true);
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
    getStatusLabel : function()
    {
      return this.getAttributeDTO('statusLabel').getValue();
    },
    setStatusLabel : function(value)
    {
      var attributeDTO = this.getAttributeDTO('statusLabel');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isStatusLabelReadable : function()
    {
      return this.getAttributeDTO('statusLabel').isReadable();
    },
    isStatusLabelWritable : function()
    {
      return this.getAttributeDTO('statusLabel').isWritable();
    },
    isStatusLabelModified : function()
    {
      return this.getAttributeDTO('statusLabel').isModified();
    },
    getStatusLabelMd : function()
    {
      return this.getAttributeDTO('statusLabel').getAttributeMdDTO();
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
      if(obj == null)
      {
        var json = '{\"id\":\"xyiyvqwt3n15esuen0hxaerwcks8ow2w0myp064p1wj846sqldssh6511t6pniro\",\"readable\":true,\"_type\":\"com.runwaysdk.system.scheduler.JobHistoryView\",\"attributeMap\":{\"maxRetries\":{\"attributeName\":\"maxRetries\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"8na0btlywjutz45nx8gv3olebeytm508NM200811060000000000000000000001\",\"system\":false,\"displayLabel\":\"Max Retries\",\"description\":\"Max Retries\",\"name\":\"maxRetries\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"jobId\":{\"attributeName\":\"jobId\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"zmeyz4kauc5ewldnw2rai6phwxymqrlxNM200811060000000000000000000001\",\"system\":false,\"displayLabel\":\"Job Id\",\"description\":\"Job Id\",\"name\":\"jobId\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"status\":{\"attributeName\":\"status\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"i66rcoefcoba9jmmcicr2mnr9pk196ogNM200811060000000000000000000001\",\"referencedMdEnumeration\":\"com.runwaysdk.system.scheduler.AllJobStatus\",\"system\":false,\"displayLabel\":\"Status\",\"description\":\"\",\"name\":\"status\",\"immutable\":false,\"enumNames\":{\"SUCCESS\":\"Success\",\"STOPPED\":\"Stopped\",\"RUNNING\":\"Running\",\"FAILURE\":\"Failure\",\"CANCELED\":\"Canceled\"},\"_selectMultiple\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"enumNames\":[],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":false},\"historyInformation\":{\"attributeName\":\"historyInformation\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalTextDTO\",\"attributeMdDTO\":{\"id\":\"yz90dqgrt1qrc91091b1n5h96095nqvgNM200811060000000000000000000001\",\"system\":false,\"displayLabel\":\"History Information\",\"description\":\"History Information with Data and Results\",\"definingMdStruct\":\"com.runwaysdk.system.scheduler.JobHistoryHistoryInformation\",\"name\":\"historyInformation\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"zak1nq89kjua1pec0vofw3xj5k7reysbqp07ac4ckrfbmoon7a84mpl6a6yzn16l\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalText\",\"structDTO\":{\"id\":\"zak1nq89kjua1pec0vofw3xj5k7reysbqp07ac4ckrfbmoon7a84mpl6a6yzn16l\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.scheduler.JobHistoryHistoryInformation\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"r4llx3xvoclq434ouccd9gkojqzbfdxx00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"zak1nq89kjua1pec0vofw3xj5k7reysbqp07ac4ckrfbmoon7a84mpl6a6yzn16l\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeTextDTO\",\"attributeMdDTO\":{\"id\":\"07lmuuipe34rkv3tsz1nin7m730cxbnn00000000000000000000000000000139\",\"system\":false,\"displayLabel\":\"Default Locale\",\"description\":\"Default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeText\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"szm3q72o75pm8zppby7cxncyzewltf3h00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"8ikz5kkigqmy6x52y9uf9qbvzb4o5yo700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"qp07ac4ckrfbmoon7a84mpl6a6yzn16l0000000000000000000MdLocalStruct\",\"displayLabel\":\"History Information\",\"description\":\"History Information with Data and Results\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"lastRun\":{\"attributeName\":\"lastRun\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"swre5xjop338e962i5ib4h1mszfv5vz2NM200811060000000000000000000001\",\"system\":false,\"displayLabel\":\"Last Run\",\"description\":\"Last Run\",\"name\":\"lastRun\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"endTime\":{\"attributeName\":\"endTime\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"ijh1mxd78u7d268p6zgaxvzk6ml8elbuNM200811060000000000000000000001\",\"system\":false,\"displayLabel\":\"End Time\",\"description\":\"\",\"name\":\"endTime\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"zoasw1eofdepioet5axepotlpx1us7ap00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"xyiyvqwt3n15esuen0hxaerwcks8ow2w0myp064p1wj846sqldssh6511t6pniro\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"startTime\":{\"attributeName\":\"startTime\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"iqkrd4cb5350ndtijqcskqch64j3xwr4NM200811060000000000000000000001\",\"system\":false,\"displayLabel\":\"Last Run\",\"description\":\"\",\"name\":\"startTime\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"workProgress\":{\"attributeName\":\"workProgress\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"if4zwbs2ra46dsogs74uaycezzdkwq5wNM200811060000000000000000000001\",\"system\":false,\"displayLabel\":\"work progress\",\"description\":\"\",\"name\":\"workProgress\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":false},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"jobOperation\":{\"attributeName\":\"jobOperation\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"xmyba32zssnf7up3kx7bp6t9qmim8ns6NM200811060000000000000000000001\",\"referencedMdEnumeration\":\"com.runwaysdk.system.scheduler.AllJobOperation\",\"system\":false,\"displayLabel\":\"Job Operation\",\"description\":\"The current Job Operation called on the Job.\",\"name\":\"jobOperation\",\"immutable\":false,\"enumNames\":{\"RESUME\":\"Resume\",\"START\":\"Start\",\"STOP\":\"Stop\",\"PAUSE\":\"Pause\",\"CANCEL\":\"Cancel\"},\"_selectMultiple\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"enumNames\":[],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":false},\"historyComment\":{\"attributeName\":\"historyComment\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalTextDTO\",\"attributeMdDTO\":{\"id\":\"21n24bkvwwuxbyc45cfka1o0bxiwj3y3NM200811060000000000000000000001\",\"system\":false,\"displayLabel\":\"Comment\",\"description\":\"User Friendly History Comment\",\"definingMdStruct\":\"com.runwaysdk.system.scheduler.JobHistoryHistoryComment\",\"name\":\"historyComment\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"74waobw52p4pr93f6l3ef773ortcugsa8d1kfg7ldiaylha1aq1u3shemkhgxe2b\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalText\",\"structDTO\":{\"id\":\"74waobw52p4pr93f6l3ef773ortcugsa8d1kfg7ldiaylha1aq1u3shemkhgxe2b\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.scheduler.JobHistoryHistoryComment\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"21maxir11z49qzny49vkherutvrk7xa700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"74waobw52p4pr93f6l3ef773ortcugsa8d1kfg7ldiaylha1aq1u3shemkhgxe2b\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeTextDTO\",\"attributeMdDTO\":{\"id\":\"z98y5rsqloy7eqns94zsoaqem8jz8bq700000000000000000000000000000139\",\"system\":false,\"displayLabel\":\"Default Locale\",\"description\":\"Default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeText\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"91yplljj4tqs825r26p94jord3ucj2vt00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"8gbraozud2p9vnkm59t1p8z5rkksc6te00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"8d1kfg7ldiaylha1aq1u3shemkhgxe2b0000000000000000000MdLocalStruct\",\"displayLabel\":\"Comment\",\"description\":\"User Friendly History Comment\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"description\":{\"attributeName\":\"description\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"6ykcapzdzqhx7qeapv9ciwc2fo7q2zr7NM200811060000000000000000000001\",\"system\":false,\"displayLabel\":\"Description\",\"description\":\"Description\",\"definingMdStruct\":\"com.runwaysdk.system.scheduler.ExecutableJobDescription\",\"name\":\"description\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"xpirlvbyhcgku9yti8n9dwnct37q4dufqqhfctwvbkzyaocsqynj2u6lejwo2a9m\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"xpirlvbyhcgku9yti8n9dwnct37q4dufqqhfctwvbkzyaocsqynj2u6lejwo2a9m\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.scheduler.ExecutableJobDescription\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"1vmu1sa2yrpmmwa8epcpd60bxokmoo3k00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"xpirlvbyhcgku9yti8n9dwnct37q4dufqqhfctwvbkzyaocsqynj2u6lejwo2a9m\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"7g1wn6zc531fojdpokckb89zdvw5x4mj00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Default Locale\",\"description\":\"Default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":4000},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"qomak9t0la8icbh1mfkvolf24ysqwcd700000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"1od0069zdhpc0eu18i03nrt7gyly5has00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"qqhfctwvbkzyaocsqynj2u6lejwo2a9m0000000000000000000MdLocalStruct\",\"displayLabel\":\"Description\",\"description\":\"Description\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"retries\":{\"attributeName\":\"retries\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"ilxzlo4yzxzb95woxxre8tn82bltstwwNM200811060000000000000000000001\",\"system\":false,\"displayLabel\":\"Retries\",\"description\":\"\",\"name\":\"retries\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":false},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"workTotal\":{\"attributeName\":\"workTotal\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"t22w82favs8yozfivgny4j1p5syvezjzNM200811060000000000000000000001\",\"system\":false,\"displayLabel\":\"Work Total\",\"description\":\"Work Total\",\"name\":\"workTotal\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"statusLabel\":{\"attributeName\":\"statusLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"iaaix6dqfvdx4diiwivpmyus0g53x9a000000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Status\",\"description\":\"\",\"name\":\"statusLabel\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"createDate\":{\"attributeName\":\"createDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"y1frsxjzdkx48k2d8lwl4tjpdey2uu19NM200811060000000000000000000001\",\"system\":false,\"displayLabel\":\"Create Date\",\"description\":\"Date and time this record was created\",\"name\":\"createDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"cronExpression\":{\"attributeName\":\"cronExpression\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0maqqvve8aevjetl715xeqt5we2mk45xNM200811060000000000000000000001\",\"system\":false,\"displayLabel\":\"Cron Expression\",\"description\":\"Cron Expression\",\"name\":\"cronExpression\",\"immutable\":false,\"required\":false,\"size\":60},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"timeout\":{\"attributeName\":\"timeout\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"xba95e4gixxkgyabnl5jriughuq5ef7tNM200811060000000000000000000001\",\"system\":false,\"displayLabel\":\"Timeout\",\"description\":\"Timeout\",\"name\":\"timeout\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.ViewDTO\",\"_toString\":\"New: Job history view\",\"_typeMd\":{\"id\":\"0myp064p1wj846sqldssh6511t6pniro20071129NM0000000000000000000005\",\"displayLabel\":\"Job history view\",\"description\":\"\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
    getJobHistories : function(clientRequest, sortAttribute, isAscending, pageSize, pageNumber)
    {
      var metadata = {className:'com.runwaysdk.system.scheduler.JobHistoryView', methodName:'getJobHistories', declaredTypes: ["java.lang.String", "java.lang.Boolean", "java.lang.Integer", "java.lang.Integer"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    }
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.scheduler.JobHistoryViewQueryDTO', {
  Extends : 'com.runwaysdk.business.ViewQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.scheduler.JobHistoryView'
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
