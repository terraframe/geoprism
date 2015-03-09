Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition', {
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
    PARENTCONDITION : 'parentCondition',
    ROOTCONDITION : 'rootCondition',
    SEQ : 'seq',
    SITEMASTER : 'siteMaster',
    TYPE : 'type',
    CLASS : 'com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition'
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
    getParentCondition : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('parentCondition');
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
    setParentCondition : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('parentCondition');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isParentConditionReadable : function()
    {
      return this.getAttributeDTO('parentCondition').isReadable();
    },
    isParentConditionWritable : function()
    {
      return this.getAttributeDTO('parentCondition').isWritable();
    },
    isParentConditionModified : function()
    {
      return this.getAttributeDTO('parentCondition').isModified();
    },
    getParentConditionMd : function()
    {
      return this.getAttributeDTO('parentCondition').getAttributeMdDTO();
    },
    getRootCondition : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('rootCondition');
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
    setRootCondition : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('rootCondition');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isRootConditionReadable : function()
    {
      return this.getAttributeDTO('rootCondition').isReadable();
    },
    isRootConditionWritable : function()
    {
      return this.getAttributeDTO('rootCondition').isWritable();
    },
    isRootConditionModified : function()
    {
      return this.getAttributeDTO('rootCondition').isModified();
    },
    getRootConditionMd : function()
    {
      return this.getAttributeDTO('rootCondition').getAttributeMdDTO();
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
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionQueryDTO', {
  Extends : 'com.runwaysdk.business.BusinessQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition'
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
Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.persist.condition.DashboardAttributeCondition', {
  Extends : 'com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition',
  IsAbstract : true,
  Constants : 
  {
    DEFININGMDATTRIBUTE : 'definingMdAttribute',
    CLASS : 'com.runwaysdk.geodashboard.gis.persist.condition.DashboardAttributeCondition'
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
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.persist.condition.DashboardAttributeConditionQueryDTO', {
  Extends : 'com.runwaysdk.geodashboard.gis.persist.condition.DashboardConditionQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.gis.persist.condition.DashboardAttributeCondition'
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
Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.persist.condition.DashboardComposite', {
  Extends : 'com.runwaysdk.geodashboard.gis.persist.condition.DashboardAttributeCondition',
  IsAbstract : true,
  Constants : 
  {
    LEFTCONDITION : 'leftCondition',
    RIGHTCONDITION : 'rightCondition',
    CLASS : 'com.runwaysdk.geodashboard.gis.persist.condition.DashboardComposite'
  },
  Instance: 
  {
    getLeftCondition : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('leftCondition');
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
    setLeftCondition : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('leftCondition');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isLeftConditionReadable : function()
    {
      return this.getAttributeDTO('leftCondition').isReadable();
    },
    isLeftConditionWritable : function()
    {
      return this.getAttributeDTO('leftCondition').isWritable();
    },
    isLeftConditionModified : function()
    {
      return this.getAttributeDTO('leftCondition').isModified();
    },
    getLeftConditionMd : function()
    {
      return this.getAttributeDTO('leftCondition').getAttributeMdDTO();
    },
    getRightCondition : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('rightCondition');
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
    setRightCondition : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('rightCondition');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isRightConditionReadable : function()
    {
      return this.getAttributeDTO('rightCondition').isReadable();
    },
    isRightConditionWritable : function()
    {
      return this.getAttributeDTO('rightCondition').isWritable();
    },
    isRightConditionModified : function()
    {
      return this.getAttributeDTO('rightCondition').isModified();
    },
    getRightConditionMd : function()
    {
      return this.getAttributeDTO('rightCondition').getAttributeMdDTO();
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
Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.persist.condition.DashboardCompositeQueryDTO', {
  Extends : 'com.runwaysdk.geodashboard.gis.persist.condition.DashboardAttributeConditionQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.gis.persist.condition.DashboardComposite'
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
