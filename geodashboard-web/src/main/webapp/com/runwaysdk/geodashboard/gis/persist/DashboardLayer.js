Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.persist.DashboardLayer', {
  Extends : 'com.runwaysdk.business.BusinessDTO',
  Constants : 
  {
    BBOXINCLUDED : 'BBoxIncluded',
    ACTIVEBYDEFAULT : 'activeByDefault',
    CREATEDATE : 'createDate',
    CREATEDBY : 'createdBy',
    DASHBOARDLEGEND : 'dashboardLegend',
    DASHBOARDMAP : 'dashboardMap',
    DISPLAYINLEGEND : 'displayInLegend',
    ENTITYDOMAIN : 'entityDomain',
    GEOENTITY : 'geoEntity',
    ID : 'id',
    KEYNAME : 'keyName',
    LASTPUBLISHDATE : 'lastPublishDate',
    LASTUPDATEDATE : 'lastUpdateDate',
    LASTUPDATEDBY : 'lastUpdatedBy',
    LAYERENABLED : 'layerEnabled',
    LAYERTYPE : 'layerType',
    LOCKEDBY : 'lockedBy',
    NAME : 'name',
    OWNER : 'owner',
    SEQ : 'seq',
    SITEMASTER : 'siteMaster',
    TYPE : 'type',
    UNIVERSAL : 'universal',
    VIEWNAME : 'viewName',
    VIRTUAL : 'virtual',
    CLASS : 'com.runwaysdk.geodashboard.gis.persist.DashboardLayer'
  },
  Instance: 
  {
    getBBoxIncluded : function()
    {
      return this.getAttributeDTO('BBoxIncluded').getValue();
    },
    setBBoxIncluded : function(value)
    {
      var attributeDTO = this.getAttributeDTO('BBoxIncluded');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isBBoxIncludedReadable : function()
    {
      return this.getAttributeDTO('BBoxIncluded').isReadable();
    },
    isBBoxIncludedWritable : function()
    {
      return this.getAttributeDTO('BBoxIncluded').isWritable();
    },
    isBBoxIncludedModified : function()
    {
      return this.getAttributeDTO('BBoxIncluded').isModified();
    },
    getBBoxIncludedMd : function()
    {
      return this.getAttributeDTO('BBoxIncluded').getAttributeMdDTO();
    },
    getActiveByDefault : function()
    {
      return this.getAttributeDTO('activeByDefault').getValue();
    },
    setActiveByDefault : function(value)
    {
      var attributeDTO = this.getAttributeDTO('activeByDefault');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isActiveByDefaultReadable : function()
    {
      return this.getAttributeDTO('activeByDefault').isReadable();
    },
    isActiveByDefaultWritable : function()
    {
      return this.getAttributeDTO('activeByDefault').isWritable();
    },
    isActiveByDefaultModified : function()
    {
      return this.getAttributeDTO('activeByDefault').isModified();
    },
    getActiveByDefaultMd : function()
    {
      return this.getAttributeDTO('activeByDefault').getAttributeMdDTO();
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
    getDashboardLegend : function()
    {
      if(Mojo.Meta.classExists('com.runwaysdk.geodashboard.gis.persist.DashboardLegend'))
      {
        var structDTO = this.getAttributeDTO('dashboardLegend').getStructDTO();
        if(structDTO == null)
        {
          return null;
        }
        else if(structDTO instanceof Mojo.$.com.runwaysdk.geodashboard.gis.persist.DashboardLegend)
        {
          return structDTO;
        }
        else
        {
          structDTO = new Mojo.$.com.runwaysdk.geodashboard.gis.persist.DashboardLegend(structDTO);
          this.getAttributeDTO('dashboardLegend').setStructDTO(structDTO);
          return structDTO;
        }
      }
      else
      {
        throw new Mojo.$.com.runwaysdk.Exception('Must import type com.runwaysdk.geodashboard.gis.persist.DashboardLegend');
      }
    },
    isDashboardLegendReadable : function()
    {
      return this.getAttributeDTO('dashboardLegend').isReadable();
    },
    isDashboardLegendWritable : function()
    {
      return this.getAttributeDTO('dashboardLegend').isWritable();
    },
    isDashboardLegendModified : function()
    {
      return this.getAttributeDTO('dashboardLegend').isModified();
    },
    getDashboardLegendMd : function()
    {
      return this.getAttributeDTO('dashboardLegend').getAttributeMdDTO();
    },
    getDashboardMap : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('dashboardMap');
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
    setDashboardMap : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('dashboardMap');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isDashboardMapReadable : function()
    {
      return this.getAttributeDTO('dashboardMap').isReadable();
    },
    isDashboardMapWritable : function()
    {
      return this.getAttributeDTO('dashboardMap').isWritable();
    },
    isDashboardMapModified : function()
    {
      return this.getAttributeDTO('dashboardMap').isModified();
    },
    getDashboardMapMd : function()
    {
      return this.getAttributeDTO('dashboardMap').getAttributeMdDTO();
    },
    getDisplayInLegend : function()
    {
      return this.getAttributeDTO('displayInLegend').getValue();
    },
    setDisplayInLegend : function(value)
    {
      var attributeDTO = this.getAttributeDTO('displayInLegend');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isDisplayInLegendReadable : function()
    {
      return this.getAttributeDTO('displayInLegend').isReadable();
    },
    isDisplayInLegendWritable : function()
    {
      return this.getAttributeDTO('displayInLegend').isWritable();
    },
    isDisplayInLegendModified : function()
    {
      return this.getAttributeDTO('displayInLegend').isModified();
    },
    getDisplayInLegendMd : function()
    {
      return this.getAttributeDTO('displayInLegend').getAttributeMdDTO();
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
    getGeoEntity : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('geoEntity');
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
    setGeoEntity : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('geoEntity');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isGeoEntityReadable : function()
    {
      return this.getAttributeDTO('geoEntity').isReadable();
    },
    isGeoEntityWritable : function()
    {
      return this.getAttributeDTO('geoEntity').isWritable();
    },
    isGeoEntityModified : function()
    {
      return this.getAttributeDTO('geoEntity').isModified();
    },
    getGeoEntityMd : function()
    {
      return this.getAttributeDTO('geoEntity').getAttributeMdDTO();
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
    getLastPublishDate : function()
    {
      return this.getAttributeDTO('lastPublishDate').getValue();
    },
    setLastPublishDate : function(value)
    {
      var attributeDTO = this.getAttributeDTO('lastPublishDate');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isLastPublishDateReadable : function()
    {
      return this.getAttributeDTO('lastPublishDate').isReadable();
    },
    isLastPublishDateWritable : function()
    {
      return this.getAttributeDTO('lastPublishDate').isWritable();
    },
    isLastPublishDateModified : function()
    {
      return this.getAttributeDTO('lastPublishDate').isModified();
    },
    getLastPublishDateMd : function()
    {
      return this.getAttributeDTO('lastPublishDate').getAttributeMdDTO();
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
    getLayerEnabled : function()
    {
      return this.getAttributeDTO('layerEnabled').getValue();
    },
    setLayerEnabled : function(value)
    {
      var attributeDTO = this.getAttributeDTO('layerEnabled');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isLayerEnabledReadable : function()
    {
      return this.getAttributeDTO('layerEnabled').isReadable();
    },
    isLayerEnabledWritable : function()
    {
      return this.getAttributeDTO('layerEnabled').isWritable();
    },
    isLayerEnabledModified : function()
    {
      return this.getAttributeDTO('layerEnabled').isModified();
    },
    getLayerEnabledMd : function()
    {
      return this.getAttributeDTO('layerEnabled').getAttributeMdDTO();
    },
    getLayerType : function()
    {
      var attributeDTO = this.getAttributeDTO('layerType');
      var names = attributeDTO.getEnumNames();
      var enums = [];
      for(var i=0; i<names.length; i++)
      {
        enums.push(Mojo.$.com.runwaysdk.geodashboard.gis.persist.AllLayerType[names[i]]);
      }
      return enums;
    },
    removeLayerType : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('layerType');
      attributeDTO.remove(enumValue);
      this.setModified(true);
    },
    clearLayerType : function()
    {
      var attributeDTO = this.getAttributeDTO('layerType');
      attributeDTO.clear();
      this.setModified(true);
    },
    addLayerType : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('layerType');
      attributeDTO.add(enumValue);
      this.setModified(true);
    },
    isLayerTypeReadable : function()
    {
      return this.getAttributeDTO('layerType').isReadable();
    },
    isLayerTypeWritable : function()
    {
      return this.getAttributeDTO('layerType').isWritable();
    },
    isLayerTypeModified : function()
    {
      return this.getAttributeDTO('layerType').isModified();
    },
    getLayerTypeMd : function()
    {
      return this.getAttributeDTO('layerType').getAttributeMdDTO();
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
    getName : function()
    {
      return this.getAttributeDTO('name').getValue();
    },
    setName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('name');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isNameReadable : function()
    {
      return this.getAttributeDTO('name').isReadable();
    },
    isNameWritable : function()
    {
      return this.getAttributeDTO('name').isWritable();
    },
    isNameModified : function()
    {
      return this.getAttributeDTO('name').isModified();
    },
    getNameMd : function()
    {
      return this.getAttributeDTO('name').getAttributeMdDTO();
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
    getUniversal : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('universal');
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
    setUniversal : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('universal');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isUniversalReadable : function()
    {
      return this.getAttributeDTO('universal').isReadable();
    },
    isUniversalWritable : function()
    {
      return this.getAttributeDTO('universal').isWritable();
    },
    isUniversalModified : function()
    {
      return this.getAttributeDTO('universal').isModified();
    },
    getUniversalMd : function()
    {
      return this.getAttributeDTO('universal').getAttributeMdDTO();
    },
    getViewName : function()
    {
      return this.getAttributeDTO('viewName').getValue();
    },
    setViewName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('viewName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isViewNameReadable : function()
    {
      return this.getAttributeDTO('viewName').isReadable();
    },
    isViewNameWritable : function()
    {
      return this.getAttributeDTO('viewName').isWritable();
    },
    isViewNameModified : function()
    {
      return this.getAttributeDTO('viewName').isModified();
    },
    getViewNameMd : function()
    {
      return this.getAttributeDTO('viewName').getAttributeMdDTO();
    },
    getVirtual : function()
    {
      return this.getAttributeDTO('virtual').getValue();
    },
    setVirtual : function(value)
    {
      var attributeDTO = this.getAttributeDTO('virtual');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isVirtualReadable : function()
    {
      return this.getAttributeDTO('virtual').isReadable();
    },
    isVirtualWritable : function()
    {
      return this.getAttributeDTO('virtual').isWritable();
    },
    isVirtualModified : function()
    {
      return this.getAttributeDTO('virtual').isModified();
    },
    getVirtualMd : function()
    {
      return this.getAttributeDTO('virtual').getAttributeMdDTO();
    },
    updateLegend : function(clientRequest, legendXPosition, legendYPosition, groupedInLegend)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.gis.persist.DashboardLayer', methodName:'updateLegend', declaredTypes: ["java.lang.Integer", "java.lang.Integer", "java.lang.Boolean"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, this, [].splice.call(arguments, 1));
    },
    applyWithStyle : function(clientRequest, style, mapId, conditions)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.gis.persist.DashboardLayer', methodName:'applyWithStyle', declaredTypes: ["com.runwaysdk.geodashboard.gis.persist.DashboardStyle", "java.lang.String", "[Lcom.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, this, [].splice.call(arguments, 1));
    },
    getAllContainingMap : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParents(clientRequest, this.getId(), 'com.runwaysdk.geodashboard.gis.persist.HasLayer');
    },
    getAllContainingMapRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParentRelationships(clientRequest, this.getId(), 'com.runwaysdk.geodashboard.gis.persist.HasLayer');
    },
    addContainingMap : function(clientRequest, parent)
    {
      var parentId = (parent instanceof Object) ? parent.getId() : parent;
      Mojo.$.com.runwaysdk.Facade.addParent(clientRequest, parentId, this.getId(), 'com.runwaysdk.geodashboard.gis.persist.HasLayer');
    },
    removeContainingMap : function(clientRequest, relationship)
    {
      var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteParent(clientRequest, relationshipId);
    },
    removeAllContainingMap : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteParents(clientRequest, this.getId(), 'com.runwaysdk.geodashboard.gis.persist.HasLayer');
    },
    getAllHasStyle : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildren(clientRequest, this.getId(), 'com.runwaysdk.geodashboard.gis.persist.HasStyle');
    },
    getAllHasStyleRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getChildRelationships(clientRequest, this.getId(), 'com.runwaysdk.geodashboard.gis.persist.HasStyle');
    },
    addHasStyle : function(clientRequest, child)
    {
      var childId = (child instanceof Object) ? child.getId() : child;
      Mojo.$.com.runwaysdk.Facade.addChild(clientRequest, this.getId(), childId, 'com.runwaysdk.geodashboard.gis.persist.HasStyle');
    },
    removeHasStyle : function(clientRequest, relationship)
    {
        var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteChild(clientRequest, relationshipId);
    },
    removeAllHasStyle : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteChildren(clientRequest, this.getId(), 'com.runwaysdk.geodashboard.gis.persist.HasStyle');
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"readable\":true,\"dto_type\":\"com.runwaysdk.business.BusinessDTO\",\"attributeMap\":{\"virtual\":{\"attributeName\":\"virtual\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"ik8yxyj0xdsqgt4tevfzivdocbihq15y00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Virtual\",\"negativeDisplayLabel\":\"\",\"description\":\"Sets each ruleset in a separate feature type if true\",\"name\":\"virtual\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"\"},\"writable\":true,\"value\":false,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"lockedBy\":{\"attributeName\":\"lockedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"i6nq07rt5hehwrfnxsbdosuoiv6tc67100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Locked By\",\"description\":\"User who has a write lock on this object\",\"name\":\"lockedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.Users\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"layerType\":{\"attributeName\":\"layerType\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"ibgg1l8acp57dq0qmd4u0zqrnl13vjp000000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.geodashboard.gis.persist.AllLayerType\",\"system\":false,\"displayLabel\":\"Layer Type\",\"description\":\"\",\"name\":\"layerType\",\"immutable\":false,\"enumNames\":{\"CATEGORY\":\"Category\",\"BUBBLE\":\"Bubble\",\"GRADIENT\":\"Gradient\",\"BASIC\":\"Basic\"},\"_selectMultiple\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"enumNames\":[],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":false},\"BBoxIncluded\":{\"attributeName\":\"BBoxIncluded\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"ia5oykboy28d18ks3mp7wx53pbxxwd6f00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"BBox Inluded\",\"negativeDisplayLabel\":\"\",\"description\":\"\",\"name\":\"BBoxIncluded\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"entityDomain\":{\"attributeName\":\"entityDomain\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"i54yd1ol74osietrd7h45wam9lbw44jf00000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Entity Domain\",\"description\":\"The Domain of this entity\",\"name\":\"entityDomain\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdDomain\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"type\":{\"attributeName\":\"type\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"inrpq4ctestlfulac24yrlcm20ikj7gp00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Type\",\"description\":\"Fully qualified type of this object\",\"name\":\"type\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"com.runwaysdk.geodashboard.gis.persist.DashboardLayer\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ikni2t4jqm43wuvfs4ztb9zu7tdih98r00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"73zm550z7b0cmrjftl6q3x0noevz3gqjiabc3jsr7ev1b7wep3qdkx816ca8mkfi\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ijh60wko0ry500tx19ogm4uk5m9ajn2900000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"innm7vrw0dx17375wg3uwk3ny3mqfmbi00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"lastPublishDate\":{\"attributeName\":\"lastPublishDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"irvbcjw3pjnruvnbqrbhezbssicdl3cd00000000000000000000000000000341\",\"system\":false,\"displayLabel\":\"LastPublishDate\",\"description\":\"Last Date The Layer Was Published (To GeoServer)\",\"name\":\"lastPublishDate\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"layerEnabled\":{\"attributeName\":\"layerEnabled\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"iq8gmo9epehi9yjndks2b5jei74o2w6f00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Layer Enabled\",\"negativeDisplayLabel\":\"\",\"description\":\"\",\"name\":\"layerEnabled\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"name\":{\"attributeName\":\"name\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ii0a5npfo33ag2m85eawur7t84k1a1p800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Name\",\"description\":\"\",\"name\":\"name\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"dashboardMap\":{\"attributeName\":\"dashboardMap\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"ij5jo03mrkj6arj16tp31aemukhr74lm00000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"dashboardMap\",\"description\":\"\",\"name\":\"dashboardMap\",\"referencedMdBusiness\":\"com.runwaysdk.geodashboard.gis.persist.DashboardMap\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"universal\":{\"attributeName\":\"universal\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"i1ee2csnxwznbbhy849u0gafftxizg9200000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Universal\",\"description\":\"\",\"name\":\"universal\",\"referencedMdBusiness\":\"com.runwaysdk.system.gis.geo.Universal\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"createDate\":{\"attributeName\":\"createDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"i1balbrmuccr55mg0yllq8rqpfz374t900000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Create Date\",\"description\":\"Date and time this record was created\",\"name\":\"createDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"dashboardLegend\":{\"attributeName\":\"dashboardLegend\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeStructDTO\",\"attributeMdDTO\":{\"id\":\"i4we0gl0ma1btkju4mlfzla6exait0wg00000000000000000000000000000308\",\"system\":false,\"displayLabel\":\"\",\"description\":\"\",\"definingMdStruct\":\"com.runwaysdk.geodashboard.gis.persist.DashboardLegend\",\"name\":\"dashboardLegend\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"8p77fok7m6vuwjvma2ohxlekzfr3uegsi5dmh42i1kau82i87tc9pjpg7s03iihl\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeStruct\",\"structDTO\":{\"id\":\"8p77fok7m6vuwjvma2ohxlekzfr3uegsi5dmh42i1kau82i87tc9pjpg7s03iihl\",\"readable\":true,\"_type\":\"com.runwaysdk.geodashboard.gis.persist.DashboardLegend\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ic9lk5838j7lc0wpfd16hy2e9qha6mcf00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"8p77fok7m6vuwjvma2ohxlekzfr3uegsi5dmh42i1kau82i87tc9pjpg7s03iihl\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"legendYPosition\":{\"attributeName\":\"legendYPosition\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"il0ghr56cczjylwrge46ursu4i36921y00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Legend Y Position\",\"description\":\"\",\"name\":\"legendYPosition\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":false},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"legendXPosition\":{\"attributeName\":\"legendXPosition\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"ii6zrnrbgw86x870hgrbwo6ipqxker3000000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Legend X Position\",\"description\":\"\",\"name\":\"legendXPosition\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":false},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"iltpot1kqe2u8i8fmnb7zx0m4mwetkfv00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"igr7ycr5hpj4uvqoyfbxnh5n79yg65ev00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"groupedInLegend\":{\"attributeName\":\"groupedInLegend\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"i144yxue1ypcue0wocimowfiblh1d6xb00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Grouped In Legend\",\"negativeDisplayLabel\":\"\",\"description\":\"\",\"name\":\"groupedInLegend\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.StructDTO\",\"_toString\":\"New: Legend\",\"_typeMd\":{\"id\":\"i5dmh42i1kau82i87tc9pjpg7s03iihl00000000000000000000000000000979\",\"displayLabel\":\"Legend\",\"description\":\"\"},\"writable\":true,\"newInstance\":true,\"modified\":true},\"modified\":true},\"activeByDefault\":{\"attributeName\":\"activeByDefault\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"i7k8bt41znm6fy3mx1shko84m44og4e500000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Layer Is Active By Default\",\"negativeDisplayLabel\":\"\",\"description\":\"\",\"name\":\"activeByDefault\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"lastUpdateDate\":{\"attributeName\":\"lastUpdateDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"ia2m2vj968w70oam1llg6mharrid96h900000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Last Update Date\",\"description\":\"Date and time this record was last updated\",\"name\":\"lastUpdateDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"geoEntity\":{\"attributeName\":\"geoEntity\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"i4wmie016ryj2amgq85i9jfdlhooymaj00000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"GeoEntity\",\"description\":\"\",\"name\":\"geoEntity\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"viewName\":{\"attributeName\":\"viewName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ico88xqb55pe4lxjeixxaf5o1h3xf2al00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"View Name\",\"description\":\"The name of the DB view this layer maps to.\",\"name\":\"viewName\",\"immutable\":false,\"required\":false,\"size\":64},\"writable\":true,\"value\":\"lv_yw0sy6e2ww\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"createdBy\":{\"attributeName\":\"createdBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"igfltw3tugf3oebyms0xyspsr1sk76ty00000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Created By\",\"description\":\"User that created this object\",\"name\":\"createdBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"displayInLegend\":{\"attributeName\":\"displayInLegend\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"iiqqifh3p71uq3str65ngs6ptfk80ew100000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Display In Legend\",\"negativeDisplayLabel\":\"\",\"description\":\"\",\"name\":\"displayInLegend\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"lastUpdatedBy\":{\"attributeName\":\"lastUpdatedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"iqkutmpyjhhffi0vy4ajzb6n6ps5fvw100000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Last Updated By\",\"description\":\"User who last updated this object\",\"name\":\"lastUpdatedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"owner\":{\"attributeName\":\"owner\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"ietv1pc5hpficdoemzcnght1h8v7lgmp00000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Owner\",\"description\":\"A reference to the user who own this object\",\"name\":\"owner\",\"referencedMdBusiness\":\"com.runwaysdk.system.Actor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"seq\":{\"attributeName\":\"seq\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"ikywsg36ytluw67fenjoik6znpazbgp300000000000000000000000000000338\",\"system\":true,\"displayLabel\":\"Sequence\",\"description\":\"Sequence number is incremented every time this object is updated\",\"name\":\"seq\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false}},\"_toString\":\"New: Layer\",\"writable\":true,\"state\":\"\",\"modified\":true,\"id\":\"73zm550z7b0cmrjftl6q3x0noevz3gqjiabc3jsr7ev1b7wep3qdkx816ca8mkfi\",\"_type\":\"com.runwaysdk.geodashboard.gis.persist.DashboardLayer\",\"_typeMd\":{\"id\":\"iabc3jsr7ev1b7wep3qdkx816ca8mkfi00000000000000000000000000000001\",\"displayLabel\":\"Layer\",\"description\":\"\"},\"transitions\":[],\"newInstance\":true,\"lockedByCurrentUser\":false}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
    updateLegend : function(clientRequest, id, legendXPosition, legendYPosition, groupedInLegend)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.gis.persist.DashboardLayer', methodName:'updateLegend', declaredTypes: ["java.lang.String", "java.lang.Integer", "java.lang.Integer", "java.lang.Boolean"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    applyWithStyle : function(clientRequest, id, style, mapId, conditions)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.gis.persist.DashboardLayer', methodName:'applyWithStyle', declaredTypes: ["java.lang.String", "com.runwaysdk.geodashboard.gis.persist.DashboardStyle", "java.lang.String", "[Lcom.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition;"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getSortedUniversals : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.gis.persist.DashboardLayer', methodName:'getSortedUniversals', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    }
  }
});
Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.persist.DashboardLayerQueryDTO', {
  Extends : 'com.runwaysdk.business.BusinessQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.gis.persist.DashboardLayer'
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
