Mojo.Meta.newClass('com.runwaysdk.geodashboard.report.ReportItem', {
  Extends : 'com.runwaysdk.business.BusinessDTO',
  Constants : 
  {
    CACHEDOCUMENT : 'cacheDocument',
    CREATEDATE : 'createDate',
    CREATEDBY : 'createdBy',
    DASHBOARD : 'dashboard',
    DESIGN : 'design',
    DOCUMENT : 'document',
    ENTITYDOMAIN : 'entityDomain',
    ID : 'id',
    KEYNAME : 'keyName',
    LASTUPDATEDATE : 'lastUpdateDate',
    LASTUPDATEDBY : 'lastUpdatedBy',
    LOCKEDBY : 'lockedBy',
    OWNER : 'owner',
    REPORTLABEL : 'reportLabel',
    REPORTNAME : 'reportName',
    SEQ : 'seq',
    SITEMASTER : 'siteMaster',
    TYPE : 'type',
    CLASS : 'com.runwaysdk.geodashboard.report.ReportItem'
  },
  Instance: 
  {
    getCacheDocument : function()
    {
      return this.getAttributeDTO('cacheDocument').getValue();
    },
    setCacheDocument : function(value)
    {
      var attributeDTO = this.getAttributeDTO('cacheDocument');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isCacheDocumentReadable : function()
    {
      return this.getAttributeDTO('cacheDocument').isReadable();
    },
    isCacheDocumentWritable : function()
    {
      return this.getAttributeDTO('cacheDocument').isWritable();
    },
    isCacheDocumentModified : function()
    {
      return this.getAttributeDTO('cacheDocument').isModified();
    },
    getCacheDocumentMd : function()
    {
      return this.getAttributeDTO('cacheDocument').getAttributeMdDTO();
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
    getDashboard : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('dashboard');
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
    setDashboard : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('dashboard');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isDashboardReadable : function()
    {
      return this.getAttributeDTO('dashboard').isReadable();
    },
    isDashboardWritable : function()
    {
      return this.getAttributeDTO('dashboard').isWritable();
    },
    isDashboardModified : function()
    {
      return this.getAttributeDTO('dashboard').isModified();
    },
    getDashboardMd : function()
    {
      return this.getAttributeDTO('dashboard').getAttributeMdDTO();
    },
    getDesign : function()
    {
      return this.getAttributeDTO('design').getValue();
    },
    setDesign : function(value)
    {
      var attributeDTO = this.getAttributeDTO('design');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isDesignReadable : function()
    {
      return this.getAttributeDTO('design').isReadable();
    },
    isDesignWritable : function()
    {
      return this.getAttributeDTO('design').isWritable();
    },
    isDesignModified : function()
    {
      return this.getAttributeDTO('design').isModified();
    },
    getDesignMd : function()
    {
      return this.getAttributeDTO('design').getAttributeMdDTO();
    },
    getDocument : function()
    {
      return this.getAttributeDTO('document').getValue();
    },
    setDocument : function(value)
    {
      var attributeDTO = this.getAttributeDTO('document');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isDocumentReadable : function()
    {
      return this.getAttributeDTO('document').isReadable();
    },
    isDocumentWritable : function()
    {
      return this.getAttributeDTO('document').isWritable();
    },
    isDocumentModified : function()
    {
      return this.getAttributeDTO('document').isModified();
    },
    getDocumentMd : function()
    {
      return this.getAttributeDTO('document').getAttributeMdDTO();
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
    getReportLabel : function()
    {
      if(Mojo.Meta.classExists('com.runwaysdk.geodashboard.report.ReportItemReportLabel'))
      {
        var structDTO = this.getAttributeDTO('reportLabel').getStructDTO();
        if(structDTO == null)
        {
          return null;
        }
        else if(structDTO instanceof Mojo.$.com.runwaysdk.geodashboard.report.ReportItemReportLabel)
        {
          return structDTO;
        }
        else
        {
          structDTO = new Mojo.$.com.runwaysdk.geodashboard.report.ReportItemReportLabel(structDTO);
          this.getAttributeDTO('reportLabel').setStructDTO(structDTO);
          return structDTO;
        }
      }
      else
      {
        throw new Mojo.$.com.runwaysdk.Exception('Must import type com.runwaysdk.geodashboard.report.ReportItemReportLabel');
      }
    },
    isReportLabelReadable : function()
    {
      return this.getAttributeDTO('reportLabel').isReadable();
    },
    isReportLabelWritable : function()
    {
      return this.getAttributeDTO('reportLabel').isWritable();
    },
    isReportLabelModified : function()
    {
      return this.getAttributeDTO('reportLabel').isModified();
    },
    getReportLabelMd : function()
    {
      return this.getAttributeDTO('reportLabel').getAttributeMdDTO();
    },
    getReportName : function()
    {
      return this.getAttributeDTO('reportName').getValue();
    },
    setReportName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('reportName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isReportNameReadable : function()
    {
      return this.getAttributeDTO('reportName').isReadable();
    },
    isReportNameWritable : function()
    {
      return this.getAttributeDTO('reportName').isWritable();
    },
    isReportNameModified : function()
    {
      return this.getAttributeDTO('reportName').isModified();
    },
    getReportNameMd : function()
    {
      return this.getAttributeDTO('reportName').getAttributeMdDTO();
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
    getDocumentAsStream : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItem', methodName:'getDocumentAsStream', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, this, [].splice.call(arguments, 1));
    },
    getURL : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItem', methodName:'getURL', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, this, [].splice.call(arguments, 1));
    },
    getDesignAsStream : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItem', methodName:'getDesignAsStream', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, this, [].splice.call(arguments, 1));
    },
    applyWithFile : function(clientRequest, fileStream)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItem', methodName:'applyWithFile', declaredTypes: ["java.io.InputStream"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, this, [].splice.call(arguments, 1));
    },
    getParameterDefinitions : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItem', methodName:'getParameterDefinitions', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, this, [].splice.call(arguments, 1));
    },
    render : function(clientRequest, outputStream, parameters, baseURL, reportURL)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItem', methodName:'render', declaredTypes: ["java.io.OutputStream", "[Lcom.runwaysdk.geodashboard.report.ReportParameter;", "java.lang.String", "java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, this, [].splice.call(arguments, 1));
    },
    validatePermissions : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItem', methodName:'validatePermissions', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, this, [].splice.call(arguments, 1));
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"readable\":true,\"dto_type\":\"com.runwaysdk.business.BusinessDTO\",\"attributeMap\":{\"lockedBy\":{\"attributeName\":\"lockedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"ikllsiomudk8rwmsd54e7eollluu96f700000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Locked By\",\"description\":\"User who has a write lock on this object\",\"name\":\"lockedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.Users\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"entityDomain\":{\"attributeName\":\"entityDomain\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"ig8x3xywbrbyehoncumhaavx5u0wskm500000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Entity Domain\",\"description\":\"The Domain of this entity\",\"name\":\"entityDomain\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdDomain\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"type\":{\"attributeName\":\"type\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ikezdq51kbx1jur5pd2pjm7pog9x84o600000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Type\",\"description\":\"Fully qualified type of this object\",\"name\":\"type\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"com.runwaysdk.geodashboard.report.ReportItem\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"lastUpdateDate\":{\"attributeName\":\"lastUpdateDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"id8x3jfzdlns9bw1czpbt7o158xju13o00000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Last Update Date\",\"description\":\"Date and time this record was last updated\",\"name\":\"lastUpdateDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"reportLabel\":{\"attributeName\":\"reportLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO\",\"attributeMdDTO\":{\"id\":\"iq9uk4pxiibgsbaerbup9js3rk7asr5b0000000MdAttributeLocalCharacter\",\"system\":false,\"displayLabel\":\"Report label\",\"description\":\"\",\"definingMdStruct\":\"com.runwaysdk.geodashboard.report.ReportItemReportLabel\",\"name\":\"reportLabel\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"09i5xtvlw90ck8kqvc7o0zrk1z83kxrnigg5w6xy81n6pm393gqv7mirpny5x4q9\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeLocalCharacter\",\"structDTO\":{\"id\":\"09i5xtvlw90ck8kqvc7o0zrk1z83kxrnigg5w6xy81n6pm393gqv7mirpny5x4q9\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.geodashboard.report.ReportItemReportLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ite8f32jsgwuakpxi1fckcgmsl69linj00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"09i5xtvlw90ck8kqvc7o0zrk1z83kxrnigg5w6xy81n6pm393gqv7mirpny5x4q9\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i8toauekdllt90idn04phywirr7dtvel00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Default Locale\",\"description\":\"Default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":4000},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i5nxb4pa32lqoyv4v7k3wp4u8a813uy600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"iqzdty738clqjehru3jgg2tcr14u9zeq00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"igg5w6xy81n6pm393gqv7mirpny5x4q90000000000000000000MdLocalStruct\",\"displayLabel\":\"Report label\",\"description\":\"\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"icvupxlyy6qy0czkun5y88799gsoydu300000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"z8ndd529w3cpbpvf35vu7t3tiyscwbnyi765ed3lfee6pq6zk8z6bifoxnsyoxv0\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"document\":{\"attributeName\":\"document\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeFileDTO\",\"attributeMdDTO\":{\"id\":\"i9ln4bcmi36qaeaijw49ohw15wsw5gr1NM200708150000000000000000000003\",\"system\":false,\"displayLabel\":\"Report document\",\"description\":\"\",\"name\":\"document\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeFile\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"iav6y2pa8bjq7nyxwu2uk232m7fuq33x00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"createdBy\":{\"attributeName\":\"createdBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"it7wgd7q5h53dobg8jhcqj4nyr7ixi6z00000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Created By\",\"description\":\"User that created this object\",\"name\":\"createdBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"in880i37jf0f18uwhf6ydjcpj96150s200000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"lastUpdatedBy\":{\"attributeName\":\"lastUpdatedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"i7j863ro268267ejd4bolgji33iizkae00000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Last Updated By\",\"description\":\"User who last updated this object\",\"name\":\"lastUpdatedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"seq\":{\"attributeName\":\"seq\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"i057we9w9s9t4ehrvk9o8y0d1ctu6fd600000000000000000000000000000338\",\"system\":true,\"displayLabel\":\"Sequence\",\"description\":\"Sequence number is incremented every time this object is updated\",\"name\":\"seq\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false},\"owner\":{\"attributeName\":\"owner\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"ikz72myd34xn9o5p7ac56qdjbxa9dg1h00000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Owner\",\"description\":\"A reference to the user who own this object\",\"name\":\"owner\",\"referencedMdBusiness\":\"com.runwaysdk.system.Actor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"cacheDocument\":{\"attributeName\":\"cacheDocument\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"ihcgibmtjr5ekeeol6mgu4vjrh7267yp00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Use report document?\",\"negativeDisplayLabel\":\"No\",\"description\":\"\",\"name\":\"cacheDocument\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"Yes\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"createDate\":{\"attributeName\":\"createDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"ihhkxk44mzxj8g3t7je2y9rody8jk2kw00000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Create Date\",\"description\":\"Date and time this record was created\",\"name\":\"createDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"dashboard\":{\"attributeName\":\"dashboard\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"ihqjxbpg8pj5nj0oe0q725yoklrrycnz00000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Dashboard\",\"description\":\"\",\"name\":\"dashboard\",\"referencedMdBusiness\":\"com.runwaysdk.geodashboard.Dashboard\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"reportName\":{\"attributeName\":\"reportName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i2i1gq333nkifqme4ign7ymu9jqhfern00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Report name\",\"description\":\"\",\"name\":\"reportName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"design\":{\"attributeName\":\"design\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeFileDTO\",\"attributeMdDTO\":{\"id\":\"i3om180hbnxwho6cp19ifus54vs767ccNM200708150000000000000000000003\",\"system\":false,\"displayLabel\":\"Report design\",\"description\":\"\",\"name\":\"design\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeFile\",\"modified\":false}},\"_toString\":\"\",\"writable\":true,\"state\":\"\",\"modified\":true,\"id\":\"z8ndd529w3cpbpvf35vu7t3tiyscwbnyi765ed3lfee6pq6zk8z6bifoxnsyoxv0\",\"_type\":\"com.runwaysdk.geodashboard.report.ReportItem\",\"_typeMd\":{\"id\":\"i765ed3lfee6pq6zk8z6bifoxnsyoxv000000000000000000000000000000001\",\"displayLabel\":\"Report item\",\"description\":\"\"},\"transitions\":[],\"newInstance\":true,\"lockedByCurrentUser\":false}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
    getDocumentAsStream : function(clientRequest, id)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItem', methodName:'getDocumentAsStream', declaredTypes: ["java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getURL : function(clientRequest, id)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItem', methodName:'getURL', declaredTypes: ["java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getDesignAsStream : function(clientRequest, id)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItem', methodName:'getDesignAsStream', declaredTypes: ["java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getQueriesForReporting : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItem', methodName:'getQueriesForReporting', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    applyWithFile : function(clientRequest, id, fileStream)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItem', methodName:'applyWithFile', declaredTypes: ["java.lang.String", "java.io.InputStream"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getReportItemForDashboard : function(clientRequest, dashboardId)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItem', methodName:'getReportItemForDashboard', declaredTypes: ["java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getParameterDefinitions : function(clientRequest, id)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItem', methodName:'getParameterDefinitions', declaredTypes: ["java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    render : function(clientRequest, id, outputStream, parameters, baseURL, reportURL)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItem', methodName:'render', declaredTypes: ["java.lang.String", "java.io.OutputStream", "[Lcom.runwaysdk.geodashboard.report.ReportParameter;", "java.lang.String", "java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getValuesForReporting : function(clientRequest, queryId, category, criteria, aggregation)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItem', methodName:'getValuesForReporting', declaredTypes: ["java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getMetadataForReporting : function(clientRequest, queryId, category, criteria, aggregation)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItem', methodName:'getMetadataForReporting', declaredTypes: ["java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    validatePermissions : function(clientRequest, id)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItem', methodName:'validatePermissions', declaredTypes: ["java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getSupportedAggregation : function(clientRequest, queryId)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItem', methodName:'getSupportedAggregation', declaredTypes: ["java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    }
  }
});
Mojo.Meta.newClass('com.runwaysdk.geodashboard.report.ReportItemQueryDTO', {
  Extends : 'com.runwaysdk.business.BusinessQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.report.ReportItem'
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
