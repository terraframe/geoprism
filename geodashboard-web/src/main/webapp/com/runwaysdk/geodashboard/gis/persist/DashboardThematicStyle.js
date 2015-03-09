Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.persist.DashboardStyle', {
  Extends : 'com.runwaysdk.business.BusinessDTO',
  Constants : 
  {
    CREATEDATE : 'createDate',
    CREATEDBY : 'createdBy',
    ENABLELABEL : 'enableLabel',
    ENABLEVALUE : 'enableValue',
    ENTITYDOMAIN : 'entityDomain',
    ID : 'id',
    KEYNAME : 'keyName',
    LABELCOLOR : 'labelColor',
    LABELFONT : 'labelFont',
    LABELHALO : 'labelHalo',
    LABELHALOWIDTH : 'labelHaloWidth',
    LABELSIZE : 'labelSize',
    LASTUPDATEDATE : 'lastUpdateDate',
    LASTUPDATEDBY : 'lastUpdatedBy',
    LINEOPACITY : 'lineOpacity',
    LINESTROKE : 'lineStroke',
    LINESTROKECAP : 'lineStrokeCap',
    LINESTROKEWIDTH : 'lineStrokeWidth',
    LOCKEDBY : 'lockedBy',
    NAME : 'name',
    OWNER : 'owner',
    POINTFILL : 'pointFill',
    POINTOPACITY : 'pointOpacity',
    POINTROTATION : 'pointRotation',
    POINTSIZE : 'pointSize',
    POINTSTROKE : 'pointStroke',
    POINTSTROKEOPACITY : 'pointStrokeOpacity',
    POINTSTROKEWIDTH : 'pointStrokeWidth',
    POINTWELLKNOWNNAME : 'pointWellKnownName',
    POLYGONFILL : 'polygonFill',
    POLYGONFILLOPACITY : 'polygonFillOpacity',
    POLYGONSTROKE : 'polygonStroke',
    POLYGONSTROKEOPACITY : 'polygonStrokeOpacity',
    POLYGONSTROKEWIDTH : 'polygonStrokeWidth',
    SEQ : 'seq',
    SITEMASTER : 'siteMaster',
    TYPE : 'type',
    VALUECOLOR : 'valueColor',
    VALUEFONT : 'valueFont',
    VALUEHALO : 'valueHalo',
    VALUEHALOWIDTH : 'valueHaloWidth',
    VALUESIZE : 'valueSize',
    CLASS : 'com.runwaysdk.geodashboard.gis.persist.DashboardStyle'
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
    getEnableLabel : function()
    {
      return this.getAttributeDTO('enableLabel').getValue();
    },
    setEnableLabel : function(value)
    {
      var attributeDTO = this.getAttributeDTO('enableLabel');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isEnableLabelReadable : function()
    {
      return this.getAttributeDTO('enableLabel').isReadable();
    },
    isEnableLabelWritable : function()
    {
      return this.getAttributeDTO('enableLabel').isWritable();
    },
    isEnableLabelModified : function()
    {
      return this.getAttributeDTO('enableLabel').isModified();
    },
    getEnableLabelMd : function()
    {
      return this.getAttributeDTO('enableLabel').getAttributeMdDTO();
    },
    getEnableValue : function()
    {
      return this.getAttributeDTO('enableValue').getValue();
    },
    setEnableValue : function(value)
    {
      var attributeDTO = this.getAttributeDTO('enableValue');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isEnableValueReadable : function()
    {
      return this.getAttributeDTO('enableValue').isReadable();
    },
    isEnableValueWritable : function()
    {
      return this.getAttributeDTO('enableValue').isWritable();
    },
    isEnableValueModified : function()
    {
      return this.getAttributeDTO('enableValue').isModified();
    },
    getEnableValueMd : function()
    {
      return this.getAttributeDTO('enableValue').getAttributeMdDTO();
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
    getLabelColor : function()
    {
      return this.getAttributeDTO('labelColor').getValue();
    },
    setLabelColor : function(value)
    {
      var attributeDTO = this.getAttributeDTO('labelColor');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isLabelColorReadable : function()
    {
      return this.getAttributeDTO('labelColor').isReadable();
    },
    isLabelColorWritable : function()
    {
      return this.getAttributeDTO('labelColor').isWritable();
    },
    isLabelColorModified : function()
    {
      return this.getAttributeDTO('labelColor').isModified();
    },
    getLabelColorMd : function()
    {
      return this.getAttributeDTO('labelColor').getAttributeMdDTO();
    },
    getLabelFont : function()
    {
      return this.getAttributeDTO('labelFont').getValue();
    },
    setLabelFont : function(value)
    {
      var attributeDTO = this.getAttributeDTO('labelFont');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isLabelFontReadable : function()
    {
      return this.getAttributeDTO('labelFont').isReadable();
    },
    isLabelFontWritable : function()
    {
      return this.getAttributeDTO('labelFont').isWritable();
    },
    isLabelFontModified : function()
    {
      return this.getAttributeDTO('labelFont').isModified();
    },
    getLabelFontMd : function()
    {
      return this.getAttributeDTO('labelFont').getAttributeMdDTO();
    },
    getLabelHalo : function()
    {
      return this.getAttributeDTO('labelHalo').getValue();
    },
    setLabelHalo : function(value)
    {
      var attributeDTO = this.getAttributeDTO('labelHalo');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isLabelHaloReadable : function()
    {
      return this.getAttributeDTO('labelHalo').isReadable();
    },
    isLabelHaloWritable : function()
    {
      return this.getAttributeDTO('labelHalo').isWritable();
    },
    isLabelHaloModified : function()
    {
      return this.getAttributeDTO('labelHalo').isModified();
    },
    getLabelHaloMd : function()
    {
      return this.getAttributeDTO('labelHalo').getAttributeMdDTO();
    },
    getLabelHaloWidth : function()
    {
      return this.getAttributeDTO('labelHaloWidth').getValue();
    },
    setLabelHaloWidth : function(value)
    {
      var attributeDTO = this.getAttributeDTO('labelHaloWidth');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isLabelHaloWidthReadable : function()
    {
      return this.getAttributeDTO('labelHaloWidth').isReadable();
    },
    isLabelHaloWidthWritable : function()
    {
      return this.getAttributeDTO('labelHaloWidth').isWritable();
    },
    isLabelHaloWidthModified : function()
    {
      return this.getAttributeDTO('labelHaloWidth').isModified();
    },
    getLabelHaloWidthMd : function()
    {
      return this.getAttributeDTO('labelHaloWidth').getAttributeMdDTO();
    },
    getLabelSize : function()
    {
      return this.getAttributeDTO('labelSize').getValue();
    },
    setLabelSize : function(value)
    {
      var attributeDTO = this.getAttributeDTO('labelSize');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isLabelSizeReadable : function()
    {
      return this.getAttributeDTO('labelSize').isReadable();
    },
    isLabelSizeWritable : function()
    {
      return this.getAttributeDTO('labelSize').isWritable();
    },
    isLabelSizeModified : function()
    {
      return this.getAttributeDTO('labelSize').isModified();
    },
    getLabelSizeMd : function()
    {
      return this.getAttributeDTO('labelSize').getAttributeMdDTO();
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
    getLineOpacity : function()
    {
      return this.getAttributeDTO('lineOpacity').getValue();
    },
    setLineOpacity : function(value)
    {
      var attributeDTO = this.getAttributeDTO('lineOpacity');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isLineOpacityReadable : function()
    {
      return this.getAttributeDTO('lineOpacity').isReadable();
    },
    isLineOpacityWritable : function()
    {
      return this.getAttributeDTO('lineOpacity').isWritable();
    },
    isLineOpacityModified : function()
    {
      return this.getAttributeDTO('lineOpacity').isModified();
    },
    getLineOpacityMd : function()
    {
      return this.getAttributeDTO('lineOpacity').getAttributeMdDTO();
    },
    getLineStroke : function()
    {
      return this.getAttributeDTO('lineStroke').getValue();
    },
    setLineStroke : function(value)
    {
      var attributeDTO = this.getAttributeDTO('lineStroke');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isLineStrokeReadable : function()
    {
      return this.getAttributeDTO('lineStroke').isReadable();
    },
    isLineStrokeWritable : function()
    {
      return this.getAttributeDTO('lineStroke').isWritable();
    },
    isLineStrokeModified : function()
    {
      return this.getAttributeDTO('lineStroke').isModified();
    },
    getLineStrokeMd : function()
    {
      return this.getAttributeDTO('lineStroke').getAttributeMdDTO();
    },
    getLineStrokeCap : function()
    {
      return this.getAttributeDTO('lineStrokeCap').getValue();
    },
    setLineStrokeCap : function(value)
    {
      var attributeDTO = this.getAttributeDTO('lineStrokeCap');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isLineStrokeCapReadable : function()
    {
      return this.getAttributeDTO('lineStrokeCap').isReadable();
    },
    isLineStrokeCapWritable : function()
    {
      return this.getAttributeDTO('lineStrokeCap').isWritable();
    },
    isLineStrokeCapModified : function()
    {
      return this.getAttributeDTO('lineStrokeCap').isModified();
    },
    getLineStrokeCapMd : function()
    {
      return this.getAttributeDTO('lineStrokeCap').getAttributeMdDTO();
    },
    getLineStrokeWidth : function()
    {
      return this.getAttributeDTO('lineStrokeWidth').getValue();
    },
    setLineStrokeWidth : function(value)
    {
      var attributeDTO = this.getAttributeDTO('lineStrokeWidth');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isLineStrokeWidthReadable : function()
    {
      return this.getAttributeDTO('lineStrokeWidth').isReadable();
    },
    isLineStrokeWidthWritable : function()
    {
      return this.getAttributeDTO('lineStrokeWidth').isWritable();
    },
    isLineStrokeWidthModified : function()
    {
      return this.getAttributeDTO('lineStrokeWidth').isModified();
    },
    getLineStrokeWidthMd : function()
    {
      return this.getAttributeDTO('lineStrokeWidth').getAttributeMdDTO();
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
    getPointFill : function()
    {
      return this.getAttributeDTO('pointFill').getValue();
    },
    setPointFill : function(value)
    {
      var attributeDTO = this.getAttributeDTO('pointFill');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPointFillReadable : function()
    {
      return this.getAttributeDTO('pointFill').isReadable();
    },
    isPointFillWritable : function()
    {
      return this.getAttributeDTO('pointFill').isWritable();
    },
    isPointFillModified : function()
    {
      return this.getAttributeDTO('pointFill').isModified();
    },
    getPointFillMd : function()
    {
      return this.getAttributeDTO('pointFill').getAttributeMdDTO();
    },
    getPointOpacity : function()
    {
      return this.getAttributeDTO('pointOpacity').getValue();
    },
    setPointOpacity : function(value)
    {
      var attributeDTO = this.getAttributeDTO('pointOpacity');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPointOpacityReadable : function()
    {
      return this.getAttributeDTO('pointOpacity').isReadable();
    },
    isPointOpacityWritable : function()
    {
      return this.getAttributeDTO('pointOpacity').isWritable();
    },
    isPointOpacityModified : function()
    {
      return this.getAttributeDTO('pointOpacity').isModified();
    },
    getPointOpacityMd : function()
    {
      return this.getAttributeDTO('pointOpacity').getAttributeMdDTO();
    },
    getPointRotation : function()
    {
      return this.getAttributeDTO('pointRotation').getValue();
    },
    setPointRotation : function(value)
    {
      var attributeDTO = this.getAttributeDTO('pointRotation');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPointRotationReadable : function()
    {
      return this.getAttributeDTO('pointRotation').isReadable();
    },
    isPointRotationWritable : function()
    {
      return this.getAttributeDTO('pointRotation').isWritable();
    },
    isPointRotationModified : function()
    {
      return this.getAttributeDTO('pointRotation').isModified();
    },
    getPointRotationMd : function()
    {
      return this.getAttributeDTO('pointRotation').getAttributeMdDTO();
    },
    getPointSize : function()
    {
      return this.getAttributeDTO('pointSize').getValue();
    },
    setPointSize : function(value)
    {
      var attributeDTO = this.getAttributeDTO('pointSize');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPointSizeReadable : function()
    {
      return this.getAttributeDTO('pointSize').isReadable();
    },
    isPointSizeWritable : function()
    {
      return this.getAttributeDTO('pointSize').isWritable();
    },
    isPointSizeModified : function()
    {
      return this.getAttributeDTO('pointSize').isModified();
    },
    getPointSizeMd : function()
    {
      return this.getAttributeDTO('pointSize').getAttributeMdDTO();
    },
    getPointStroke : function()
    {
      return this.getAttributeDTO('pointStroke').getValue();
    },
    setPointStroke : function(value)
    {
      var attributeDTO = this.getAttributeDTO('pointStroke');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPointStrokeReadable : function()
    {
      return this.getAttributeDTO('pointStroke').isReadable();
    },
    isPointStrokeWritable : function()
    {
      return this.getAttributeDTO('pointStroke').isWritable();
    },
    isPointStrokeModified : function()
    {
      return this.getAttributeDTO('pointStroke').isModified();
    },
    getPointStrokeMd : function()
    {
      return this.getAttributeDTO('pointStroke').getAttributeMdDTO();
    },
    getPointStrokeOpacity : function()
    {
      return this.getAttributeDTO('pointStrokeOpacity').getValue();
    },
    setPointStrokeOpacity : function(value)
    {
      var attributeDTO = this.getAttributeDTO('pointStrokeOpacity');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPointStrokeOpacityReadable : function()
    {
      return this.getAttributeDTO('pointStrokeOpacity').isReadable();
    },
    isPointStrokeOpacityWritable : function()
    {
      return this.getAttributeDTO('pointStrokeOpacity').isWritable();
    },
    isPointStrokeOpacityModified : function()
    {
      return this.getAttributeDTO('pointStrokeOpacity').isModified();
    },
    getPointStrokeOpacityMd : function()
    {
      return this.getAttributeDTO('pointStrokeOpacity').getAttributeMdDTO();
    },
    getPointStrokeWidth : function()
    {
      return this.getAttributeDTO('pointStrokeWidth').getValue();
    },
    setPointStrokeWidth : function(value)
    {
      var attributeDTO = this.getAttributeDTO('pointStrokeWidth');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPointStrokeWidthReadable : function()
    {
      return this.getAttributeDTO('pointStrokeWidth').isReadable();
    },
    isPointStrokeWidthWritable : function()
    {
      return this.getAttributeDTO('pointStrokeWidth').isWritable();
    },
    isPointStrokeWidthModified : function()
    {
      return this.getAttributeDTO('pointStrokeWidth').isModified();
    },
    getPointStrokeWidthMd : function()
    {
      return this.getAttributeDTO('pointStrokeWidth').getAttributeMdDTO();
    },
    getPointWellKnownName : function()
    {
      return this.getAttributeDTO('pointWellKnownName').getValue();
    },
    setPointWellKnownName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('pointWellKnownName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPointWellKnownNameReadable : function()
    {
      return this.getAttributeDTO('pointWellKnownName').isReadable();
    },
    isPointWellKnownNameWritable : function()
    {
      return this.getAttributeDTO('pointWellKnownName').isWritable();
    },
    isPointWellKnownNameModified : function()
    {
      return this.getAttributeDTO('pointWellKnownName').isModified();
    },
    getPointWellKnownNameMd : function()
    {
      return this.getAttributeDTO('pointWellKnownName').getAttributeMdDTO();
    },
    getPolygonFill : function()
    {
      return this.getAttributeDTO('polygonFill').getValue();
    },
    setPolygonFill : function(value)
    {
      var attributeDTO = this.getAttributeDTO('polygonFill');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPolygonFillReadable : function()
    {
      return this.getAttributeDTO('polygonFill').isReadable();
    },
    isPolygonFillWritable : function()
    {
      return this.getAttributeDTO('polygonFill').isWritable();
    },
    isPolygonFillModified : function()
    {
      return this.getAttributeDTO('polygonFill').isModified();
    },
    getPolygonFillMd : function()
    {
      return this.getAttributeDTO('polygonFill').getAttributeMdDTO();
    },
    getPolygonFillOpacity : function()
    {
      return this.getAttributeDTO('polygonFillOpacity').getValue();
    },
    setPolygonFillOpacity : function(value)
    {
      var attributeDTO = this.getAttributeDTO('polygonFillOpacity');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPolygonFillOpacityReadable : function()
    {
      return this.getAttributeDTO('polygonFillOpacity').isReadable();
    },
    isPolygonFillOpacityWritable : function()
    {
      return this.getAttributeDTO('polygonFillOpacity').isWritable();
    },
    isPolygonFillOpacityModified : function()
    {
      return this.getAttributeDTO('polygonFillOpacity').isModified();
    },
    getPolygonFillOpacityMd : function()
    {
      return this.getAttributeDTO('polygonFillOpacity').getAttributeMdDTO();
    },
    getPolygonStroke : function()
    {
      return this.getAttributeDTO('polygonStroke').getValue();
    },
    setPolygonStroke : function(value)
    {
      var attributeDTO = this.getAttributeDTO('polygonStroke');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPolygonStrokeReadable : function()
    {
      return this.getAttributeDTO('polygonStroke').isReadable();
    },
    isPolygonStrokeWritable : function()
    {
      return this.getAttributeDTO('polygonStroke').isWritable();
    },
    isPolygonStrokeModified : function()
    {
      return this.getAttributeDTO('polygonStroke').isModified();
    },
    getPolygonStrokeMd : function()
    {
      return this.getAttributeDTO('polygonStroke').getAttributeMdDTO();
    },
    getPolygonStrokeOpacity : function()
    {
      return this.getAttributeDTO('polygonStrokeOpacity').getValue();
    },
    setPolygonStrokeOpacity : function(value)
    {
      var attributeDTO = this.getAttributeDTO('polygonStrokeOpacity');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPolygonStrokeOpacityReadable : function()
    {
      return this.getAttributeDTO('polygonStrokeOpacity').isReadable();
    },
    isPolygonStrokeOpacityWritable : function()
    {
      return this.getAttributeDTO('polygonStrokeOpacity').isWritable();
    },
    isPolygonStrokeOpacityModified : function()
    {
      return this.getAttributeDTO('polygonStrokeOpacity').isModified();
    },
    getPolygonStrokeOpacityMd : function()
    {
      return this.getAttributeDTO('polygonStrokeOpacity').getAttributeMdDTO();
    },
    getPolygonStrokeWidth : function()
    {
      return this.getAttributeDTO('polygonStrokeWidth').getValue();
    },
    setPolygonStrokeWidth : function(value)
    {
      var attributeDTO = this.getAttributeDTO('polygonStrokeWidth');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPolygonStrokeWidthReadable : function()
    {
      return this.getAttributeDTO('polygonStrokeWidth').isReadable();
    },
    isPolygonStrokeWidthWritable : function()
    {
      return this.getAttributeDTO('polygonStrokeWidth').isWritable();
    },
    isPolygonStrokeWidthModified : function()
    {
      return this.getAttributeDTO('polygonStrokeWidth').isModified();
    },
    getPolygonStrokeWidthMd : function()
    {
      return this.getAttributeDTO('polygonStrokeWidth').getAttributeMdDTO();
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
    getValueColor : function()
    {
      return this.getAttributeDTO('valueColor').getValue();
    },
    setValueColor : function(value)
    {
      var attributeDTO = this.getAttributeDTO('valueColor');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isValueColorReadable : function()
    {
      return this.getAttributeDTO('valueColor').isReadable();
    },
    isValueColorWritable : function()
    {
      return this.getAttributeDTO('valueColor').isWritable();
    },
    isValueColorModified : function()
    {
      return this.getAttributeDTO('valueColor').isModified();
    },
    getValueColorMd : function()
    {
      return this.getAttributeDTO('valueColor').getAttributeMdDTO();
    },
    getValueFont : function()
    {
      return this.getAttributeDTO('valueFont').getValue();
    },
    setValueFont : function(value)
    {
      var attributeDTO = this.getAttributeDTO('valueFont');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isValueFontReadable : function()
    {
      return this.getAttributeDTO('valueFont').isReadable();
    },
    isValueFontWritable : function()
    {
      return this.getAttributeDTO('valueFont').isWritable();
    },
    isValueFontModified : function()
    {
      return this.getAttributeDTO('valueFont').isModified();
    },
    getValueFontMd : function()
    {
      return this.getAttributeDTO('valueFont').getAttributeMdDTO();
    },
    getValueHalo : function()
    {
      return this.getAttributeDTO('valueHalo').getValue();
    },
    setValueHalo : function(value)
    {
      var attributeDTO = this.getAttributeDTO('valueHalo');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isValueHaloReadable : function()
    {
      return this.getAttributeDTO('valueHalo').isReadable();
    },
    isValueHaloWritable : function()
    {
      return this.getAttributeDTO('valueHalo').isWritable();
    },
    isValueHaloModified : function()
    {
      return this.getAttributeDTO('valueHalo').isModified();
    },
    getValueHaloMd : function()
    {
      return this.getAttributeDTO('valueHalo').getAttributeMdDTO();
    },
    getValueHaloWidth : function()
    {
      return this.getAttributeDTO('valueHaloWidth').getValue();
    },
    setValueHaloWidth : function(value)
    {
      var attributeDTO = this.getAttributeDTO('valueHaloWidth');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isValueHaloWidthReadable : function()
    {
      return this.getAttributeDTO('valueHaloWidth').isReadable();
    },
    isValueHaloWidthWritable : function()
    {
      return this.getAttributeDTO('valueHaloWidth').isWritable();
    },
    isValueHaloWidthModified : function()
    {
      return this.getAttributeDTO('valueHaloWidth').isModified();
    },
    getValueHaloWidthMd : function()
    {
      return this.getAttributeDTO('valueHaloWidth').getAttributeMdDTO();
    },
    getValueSize : function()
    {
      return this.getAttributeDTO('valueSize').getValue();
    },
    setValueSize : function(value)
    {
      var attributeDTO = this.getAttributeDTO('valueSize');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isValueSizeReadable : function()
    {
      return this.getAttributeDTO('valueSize').isReadable();
    },
    isValueSizeWritable : function()
    {
      return this.getAttributeDTO('valueSize').isWritable();
    },
    isValueSizeModified : function()
    {
      return this.getAttributeDTO('valueSize').isModified();
    },
    getValueSizeMd : function()
    {
      return this.getAttributeDTO('valueSize').getAttributeMdDTO();
    },
    getAllContainingLayer : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParents(clientRequest, this.getId(), 'com.runwaysdk.geodashboard.gis.persist.HasStyle');
    },
    getAllContainingLayerRelationships : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.getParentRelationships(clientRequest, this.getId(), 'com.runwaysdk.geodashboard.gis.persist.HasStyle');
    },
    addContainingLayer : function(clientRequest, parent)
    {
      var parentId = (parent instanceof Object) ? parent.getId() : parent;
      Mojo.$.com.runwaysdk.Facade.addParent(clientRequest, parentId, this.getId(), 'com.runwaysdk.geodashboard.gis.persist.HasStyle');
    },
    removeContainingLayer : function(clientRequest, relationship)
    {
      var relationshipId = (relationship instanceof Object) ? relationship.getId() : relationship;
      Mojo.$.com.runwaysdk.Facade.deleteParent(clientRequest, relationshipId);
    },
    removeAllContainingLayer : function(clientRequest)
    {
      Mojo.$.com.runwaysdk.Facade.deleteParents(clientRequest, this.getId(), 'com.runwaysdk.geodashboard.gis.persist.HasStyle');
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"readable\":true,\"dto_type\":\"com.runwaysdk.business.BusinessDTO\",\"attributeMap\":{\"valueHaloWidth\":{\"attributeName\":\"valueHaloWidth\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"ihuz2gkwo2mkqwzz77ebysuqtdz1vg1b00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Halo Width\",\"description\":\"\",\"name\":\"valueHaloWidth\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"lockedBy\":{\"attributeName\":\"lockedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"itjj7ucnyhl9vpbicr3bd7l4pdxieyhe00000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Locked By\",\"description\":\"User who has a write lock on this object\",\"name\":\"lockedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.Users\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"labelColor\":{\"attributeName\":\"labelColor\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ieapeu3itftstvo7x7ouhorfo4j7s1gd00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Color\",\"description\":\"\",\"name\":\"labelColor\",\"immutable\":false,\"required\":true,\"size\":7},\"writable\":true,\"value\":\"#000000\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"pointSize\":{\"attributeName\":\"pointSize\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"i8wv8urhufy6hvxaeqwgomzo85dk0fmy00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Size\",\"description\":\"\",\"name\":\"pointSize\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":6,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"valueSize\":{\"attributeName\":\"valueSize\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"i683xk5f1yo07trcbq9vdmnn4ntmh6uq00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Size\",\"description\":\"\",\"name\":\"valueSize\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":12,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"valueHalo\":{\"attributeName\":\"valueHalo\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"iegdwb66x0nwrzsnrr5uv1mc0wmw7lw900000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Halo\",\"description\":\"\",\"name\":\"valueHalo\",\"immutable\":false,\"required\":true,\"size\":10},\"writable\":true,\"value\":\"#FFFFFF\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"pointWellKnownName\":{\"attributeName\":\"pointWellKnownName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ikz5shix771j3ox70vhc8deufkgmbh5t00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Well Known Name\",\"description\":\"\",\"name\":\"pointWellKnownName\",\"immutable\":false,\"required\":true,\"size\":50},\"writable\":true,\"value\":\"circle\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"entityDomain\":{\"attributeName\":\"entityDomain\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"iizkjhj5gd7icwona3w90n6bffkl94y300000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Entity Domain\",\"description\":\"The Domain of this entity\",\"name\":\"entityDomain\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdDomain\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"enableLabel\":{\"attributeName\":\"enableLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"iad8hyz8gcckgkm13enegeqhm6wxgfzd00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Display Label\",\"negativeDisplayLabel\":\"\",\"description\":\"\",\"name\":\"enableLabel\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"type\":{\"attributeName\":\"type\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"icaiuroye6hgvc6c8ae8xlogunteygdw00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Type\",\"description\":\"Fully qualified type of this object\",\"name\":\"type\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"com.runwaysdk.geodashboard.gis.persist.DashboardStyle\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"polygonStroke\":{\"attributeName\":\"polygonStroke\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i7jodr32q1j7p7hr768pynfhi81lf2es00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Color\",\"description\":\"\",\"name\":\"polygonStroke\",\"immutable\":false,\"required\":false,\"size\":7},\"writable\":true,\"value\":\"#000000\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"pointStrokeWidth\":{\"attributeName\":\"pointStrokeWidth\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"iky0grdy1s3v6v7g9nx6zxlrbwam8waw00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Width\",\"description\":\"\",\"name\":\"pointStrokeWidth\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":2,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"polygonFillOpacity\":{\"attributeName\":\"polygonFillOpacity\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDoubleDTO\",\"attributeMdDTO\":{\"id\":\"i5dkqo2wqkgmibtbftg4m2tjhmoxfubf00000000000000000000000000000170\",\"system\":false,\"totalLength\":3,\"displayLabel\":\"Opacity\",\"decimalLength\":2,\"description\":\"\",\"name\":\"polygonFillOpacity\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":0.9,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDouble\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"itkhqq476ozd7vxz71i3n5eezg257jmr00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"xtpb6f3kqvuigrowp7bsfyd7x8rn2kb3i20j00wk3hlxgxr9gqp4ql2mqgb2nvc2\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"pointFill\":{\"attributeName\":\"pointFill\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i35q7g1p21c194ion6wvzh63qdc3i08300000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Color\",\"description\":\"\",\"name\":\"pointFill\",\"immutable\":false,\"required\":true,\"size\":7},\"writable\":true,\"value\":\"#00bfff\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i6hl7qufjh3ecixvkv6ed4kqyr6sgym200000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i291hcwvy4fj5p1z2jzft09g0qj10zwn00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"polygonFill\":{\"attributeName\":\"polygonFill\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ioubnhqkqd1rpyg73ezsequlsmdekzfb00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Color\",\"description\":\"\",\"name\":\"polygonFill\",\"immutable\":false,\"required\":true,\"size\":7},\"writable\":true,\"value\":\"#00bfff\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"pointOpacity\":{\"attributeName\":\"pointOpacity\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDoubleDTO\",\"attributeMdDTO\":{\"id\":\"ibace8x7fnl63e2h7vbodtnpv7vhocf800000000000000000000000000000170\",\"system\":false,\"totalLength\":3,\"displayLabel\":\"Opacity\",\"decimalLength\":2,\"description\":\"\",\"name\":\"pointOpacity\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":0.9,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDouble\",\"modified\":false},\"pointRotation\":{\"attributeName\":\"pointRotation\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"i2655u6dwk1ss1mb9zju2yfetkfgdana00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Point Rotation\",\"description\":\"\",\"name\":\"pointRotation\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":false},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"labelSize\":{\"attributeName\":\"labelSize\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"ii71nbgd7906j975omr4aa23s4hww58500000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Size\",\"description\":\"\",\"name\":\"labelSize\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":12,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"name\":{\"attributeName\":\"name\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"io7968jrldoynd0kdiv4adpto624ay8z00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Name\",\"description\":\"\",\"name\":\"name\",\"immutable\":false,\"required\":false,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"createDate\":{\"attributeName\":\"createDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"iezm55bemreoatoj00chyeqp0axs9ftj00000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Create Date\",\"description\":\"Date and time this record was created\",\"name\":\"createDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"polygonStrokeOpacity\":{\"attributeName\":\"polygonStrokeOpacity\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDoubleDTO\",\"attributeMdDTO\":{\"id\":\"i1lebay6d5v7jqqq1tset8tztvmj4smt00000000000000000000000000000170\",\"system\":false,\"totalLength\":3,\"displayLabel\":\"Opacity\",\"decimalLength\":2,\"description\":\"\",\"name\":\"polygonStrokeOpacity\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":0.9,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDouble\",\"modified\":false},\"labelHaloWidth\":{\"attributeName\":\"labelHaloWidth\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"id7vo98in69auc1cjl2ibjpobrctuz9f00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Halo Width\",\"description\":\"\",\"name\":\"labelHaloWidth\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"labelHalo\":{\"attributeName\":\"labelHalo\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ikn1rzbxh10sjecg0ux6dj7q12j3d2iw00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Halo\",\"description\":\"\",\"name\":\"labelHalo\",\"immutable\":false,\"required\":true,\"size\":7},\"writable\":true,\"value\":\"#FFFFFF\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"lineStrokeCap\":{\"attributeName\":\"lineStrokeCap\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ie7rmx840k2qq3dbbc6x3vltdvu2tv8k00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Line Stroke Cap\",\"description\":\"\",\"name\":\"lineStrokeCap\",\"immutable\":false,\"required\":false,\"size\":6},\"writable\":true,\"value\":\"butt\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"enableValue\":{\"attributeName\":\"enableValue\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"ir0aa12gqwz71vtkimkdu239nxf9asxs00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Display Value\",\"negativeDisplayLabel\":\"\",\"description\":\"\",\"name\":\"enableValue\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"lineOpacity\":{\"attributeName\":\"lineOpacity\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDoubleDTO\",\"attributeMdDTO\":{\"id\":\"im64x92qmumto2qvw4i2tvbpnf21o2el00000000000000000000000000000170\",\"system\":false,\"totalLength\":3,\"displayLabel\":\"Opacity\",\"decimalLength\":2,\"description\":\"\",\"name\":\"lineOpacity\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":0.9,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDouble\",\"modified\":false},\"lineStroke\":{\"attributeName\":\"lineStroke\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ijf7jermt0qyhql26k6g56ql5kegpjq100000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Color\",\"description\":\"\",\"name\":\"lineStroke\",\"immutable\":false,\"required\":true,\"size\":7},\"writable\":true,\"value\":\"#000000\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"lastUpdateDate\":{\"attributeName\":\"lastUpdateDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"i4wc3qgg3c23gjcfy3ykhzwoazy49ptn00000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Last Update Date\",\"description\":\"Date and time this record was last updated\",\"name\":\"lastUpdateDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"pointStroke\":{\"attributeName\":\"pointStroke\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ihqtvhx0vvkwzs8m90f0j1yqtn7n6vnx00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Color\",\"description\":\"\",\"name\":\"pointStroke\",\"immutable\":false,\"required\":false,\"size\":7},\"writable\":true,\"value\":\"#000000\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"polygonStrokeWidth\":{\"attributeName\":\"polygonStrokeWidth\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"ien9x4p3l7qfb4z24t4tobtu3bk9fk9k00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Width\",\"description\":\"\",\"name\":\"polygonStrokeWidth\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":2,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"createdBy\":{\"attributeName\":\"createdBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"ibskt0wgz3dahzxidbwkve9qkeqna5s200000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Created By\",\"description\":\"User that created this object\",\"name\":\"createdBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"lastUpdatedBy\":{\"attributeName\":\"lastUpdatedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"iriz4tomrlrbq3kc51a2fgd9e8akhvvb00000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Last Updated By\",\"description\":\"User who last updated this object\",\"name\":\"lastUpdatedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"lineStrokeWidth\":{\"attributeName\":\"lineStrokeWidth\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"i8ej0ii8hqhggnh9dodr73t8emddnar000000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Width\",\"description\":\"\",\"name\":\"lineStrokeWidth\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":3,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"seq\":{\"attributeName\":\"seq\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"ibspcqp2ki7nj856zgkzmwk2qiljbhur00000000000000000000000000000338\",\"system\":true,\"displayLabel\":\"Sequence\",\"description\":\"Sequence number is incremented every time this object is updated\",\"name\":\"seq\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false},\"owner\":{\"attributeName\":\"owner\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"itratvulvkrexf3tygbi7iukqkmzwe6v00000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Owner\",\"description\":\"A reference to the user who own this object\",\"name\":\"owner\",\"referencedMdBusiness\":\"com.runwaysdk.system.Actor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"valueFont\":{\"attributeName\":\"valueFont\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ik970i1w87usfeql5kejk75dv3fuaank00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Font\",\"description\":\"\",\"name\":\"valueFont\",\"immutable\":false,\"required\":true,\"size\":50},\"writable\":true,\"value\":\"Arial\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"valueColor\":{\"attributeName\":\"valueColor\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i55u7ntik8tndrdvfklhbrzqvclf6pkn00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Color\",\"description\":\"\",\"name\":\"valueColor\",\"immutable\":false,\"required\":true,\"size\":10},\"writable\":true,\"value\":\"#000000\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"labelFont\":{\"attributeName\":\"labelFont\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i8djfbatg5y3rwuevd7hhw4pxdy4k1r400000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Font\",\"description\":\"\",\"name\":\"labelFont\",\"immutable\":false,\"required\":true,\"size\":50},\"writable\":true,\"value\":\"Arial\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"pointStrokeOpacity\":{\"attributeName\":\"pointStrokeOpacity\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDoubleDTO\",\"attributeMdDTO\":{\"id\":\"i635vg70wnvsn8zqd2pyreavsa21dqh300000000000000000000000000000170\",\"system\":false,\"totalLength\":2,\"displayLabel\":\"Opacity\",\"decimalLength\":2,\"description\":\"\",\"name\":\"pointStrokeOpacity\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":0.7,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDouble\",\"modified\":false}},\"_toString\":\"New: Style\",\"writable\":true,\"state\":\"\",\"modified\":true,\"id\":\"xtpb6f3kqvuigrowp7bsfyd7x8rn2kb3i20j00wk3hlxgxr9gqp4ql2mqgb2nvc2\",\"_type\":\"com.runwaysdk.geodashboard.gis.persist.DashboardStyle\",\"_typeMd\":{\"id\":\"i20j00wk3hlxgxr9gqp4ql2mqgb2nvc200000000000000000000000000000001\",\"displayLabel\":\"Style\",\"description\":\"\"},\"transitions\":[],\"newInstance\":true,\"lockedByCurrentUser\":false}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
    getSortedFonts : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.gis.persist.DashboardStyle', methodName:'getSortedFonts', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getSortedAggregations : function(clientRequest, thematicAttributeId)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.gis.persist.DashboardStyle', methodName:'getSortedAggregations', declaredTypes: ["java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    }
  }
});
Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.persist.DashboardStyleQueryDTO', {
  Extends : 'com.runwaysdk.business.BusinessQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.gis.persist.DashboardStyle'
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
Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle', {
  Extends : 'com.runwaysdk.geodashboard.gis.persist.DashboardStyle',
  Constants : 
  {
    AGGREGATIONTYPE : 'aggregationType',
    MDATTRIBUTE : 'mdAttribute',
    POINTFIXEDSIZE : 'pointFixedSize',
    POINTMAXSIZE : 'pointMaxSize',
    POINTMINSIZE : 'pointMinSize',
    POLYGONMAXFILL : 'polygonMaxFill',
    POLYGONMINFILL : 'polygonMinFill',
    STYLECATEGORY1 : 'styleCategory1',
    STYLECATEGORY2 : 'styleCategory2',
    STYLECATEGORY3 : 'styleCategory3',
    STYLECATEGORY4 : 'styleCategory4',
    STYLECATEGORY5 : 'styleCategory5',
    STYLECATEGORYFILL1 : 'styleCategoryFill1',
    STYLECATEGORYFILL2 : 'styleCategoryFill2',
    STYLECATEGORYFILL3 : 'styleCategoryFill3',
    STYLECATEGORYFILL4 : 'styleCategoryFill4',
    STYLECATEGORYFILL5 : 'styleCategoryFill5',
    STYLECONDITION : 'styleCondition',
    CLASS : 'com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle'
  },
  Instance: 
  {
    getAggregationType : function()
    {
      var attributeDTO = this.getAttributeDTO('aggregationType');
      var names = attributeDTO.getEnumNames();
      var enums = [];
      for(var i=0; i<names.length; i++)
      {
        enums.push(Mojo.$.com.runwaysdk.geodashboard.gis.persist.AllAggregationType[names[i]]);
      }
      return enums;
    },
    removeAggregationType : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('aggregationType');
      attributeDTO.remove(enumValue);
      this.setModified(true);
    },
    clearAggregationType : function()
    {
      var attributeDTO = this.getAttributeDTO('aggregationType');
      attributeDTO.clear();
      this.setModified(true);
    },
    addAggregationType : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('aggregationType');
      attributeDTO.add(enumValue);
      this.setModified(true);
    },
    isAggregationTypeReadable : function()
    {
      return this.getAttributeDTO('aggregationType').isReadable();
    },
    isAggregationTypeWritable : function()
    {
      return this.getAttributeDTO('aggregationType').isWritable();
    },
    isAggregationTypeModified : function()
    {
      return this.getAttributeDTO('aggregationType').isModified();
    },
    getAggregationTypeMd : function()
    {
      return this.getAttributeDTO('aggregationType').getAttributeMdDTO();
    },
    getMdAttribute : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('mdAttribute');
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
    setMdAttribute : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('mdAttribute');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isMdAttributeReadable : function()
    {
      return this.getAttributeDTO('mdAttribute').isReadable();
    },
    isMdAttributeWritable : function()
    {
      return this.getAttributeDTO('mdAttribute').isWritable();
    },
    isMdAttributeModified : function()
    {
      return this.getAttributeDTO('mdAttribute').isModified();
    },
    getMdAttributeMd : function()
    {
      return this.getAttributeDTO('mdAttribute').getAttributeMdDTO();
    },
    getPointFixedSize : function()
    {
      return this.getAttributeDTO('pointFixedSize').getValue();
    },
    setPointFixedSize : function(value)
    {
      var attributeDTO = this.getAttributeDTO('pointFixedSize');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPointFixedSizeReadable : function()
    {
      return this.getAttributeDTO('pointFixedSize').isReadable();
    },
    isPointFixedSizeWritable : function()
    {
      return this.getAttributeDTO('pointFixedSize').isWritable();
    },
    isPointFixedSizeModified : function()
    {
      return this.getAttributeDTO('pointFixedSize').isModified();
    },
    getPointFixedSizeMd : function()
    {
      return this.getAttributeDTO('pointFixedSize').getAttributeMdDTO();
    },
    getPointMaxSize : function()
    {
      return this.getAttributeDTO('pointMaxSize').getValue();
    },
    setPointMaxSize : function(value)
    {
      var attributeDTO = this.getAttributeDTO('pointMaxSize');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPointMaxSizeReadable : function()
    {
      return this.getAttributeDTO('pointMaxSize').isReadable();
    },
    isPointMaxSizeWritable : function()
    {
      return this.getAttributeDTO('pointMaxSize').isWritable();
    },
    isPointMaxSizeModified : function()
    {
      return this.getAttributeDTO('pointMaxSize').isModified();
    },
    getPointMaxSizeMd : function()
    {
      return this.getAttributeDTO('pointMaxSize').getAttributeMdDTO();
    },
    getPointMinSize : function()
    {
      return this.getAttributeDTO('pointMinSize').getValue();
    },
    setPointMinSize : function(value)
    {
      var attributeDTO = this.getAttributeDTO('pointMinSize');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPointMinSizeReadable : function()
    {
      return this.getAttributeDTO('pointMinSize').isReadable();
    },
    isPointMinSizeWritable : function()
    {
      return this.getAttributeDTO('pointMinSize').isWritable();
    },
    isPointMinSizeModified : function()
    {
      return this.getAttributeDTO('pointMinSize').isModified();
    },
    getPointMinSizeMd : function()
    {
      return this.getAttributeDTO('pointMinSize').getAttributeMdDTO();
    },
    getPolygonMaxFill : function()
    {
      return this.getAttributeDTO('polygonMaxFill').getValue();
    },
    setPolygonMaxFill : function(value)
    {
      var attributeDTO = this.getAttributeDTO('polygonMaxFill');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPolygonMaxFillReadable : function()
    {
      return this.getAttributeDTO('polygonMaxFill').isReadable();
    },
    isPolygonMaxFillWritable : function()
    {
      return this.getAttributeDTO('polygonMaxFill').isWritable();
    },
    isPolygonMaxFillModified : function()
    {
      return this.getAttributeDTO('polygonMaxFill').isModified();
    },
    getPolygonMaxFillMd : function()
    {
      return this.getAttributeDTO('polygonMaxFill').getAttributeMdDTO();
    },
    getPolygonMinFill : function()
    {
      return this.getAttributeDTO('polygonMinFill').getValue();
    },
    setPolygonMinFill : function(value)
    {
      var attributeDTO = this.getAttributeDTO('polygonMinFill');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPolygonMinFillReadable : function()
    {
      return this.getAttributeDTO('polygonMinFill').isReadable();
    },
    isPolygonMinFillWritable : function()
    {
      return this.getAttributeDTO('polygonMinFill').isWritable();
    },
    isPolygonMinFillModified : function()
    {
      return this.getAttributeDTO('polygonMinFill').isModified();
    },
    getPolygonMinFillMd : function()
    {
      return this.getAttributeDTO('polygonMinFill').getAttributeMdDTO();
    },
    getStyleCategory1 : function()
    {
      return this.getAttributeDTO('styleCategory1').getValue();
    },
    setStyleCategory1 : function(value)
    {
      var attributeDTO = this.getAttributeDTO('styleCategory1');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isStyleCategory1Readable : function()
    {
      return this.getAttributeDTO('styleCategory1').isReadable();
    },
    isStyleCategory1Writable : function()
    {
      return this.getAttributeDTO('styleCategory1').isWritable();
    },
    isStyleCategory1Modified : function()
    {
      return this.getAttributeDTO('styleCategory1').isModified();
    },
    getStyleCategory1Md : function()
    {
      return this.getAttributeDTO('styleCategory1').getAttributeMdDTO();
    },
    getStyleCategory2 : function()
    {
      return this.getAttributeDTO('styleCategory2').getValue();
    },
    setStyleCategory2 : function(value)
    {
      var attributeDTO = this.getAttributeDTO('styleCategory2');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isStyleCategory2Readable : function()
    {
      return this.getAttributeDTO('styleCategory2').isReadable();
    },
    isStyleCategory2Writable : function()
    {
      return this.getAttributeDTO('styleCategory2').isWritable();
    },
    isStyleCategory2Modified : function()
    {
      return this.getAttributeDTO('styleCategory2').isModified();
    },
    getStyleCategory2Md : function()
    {
      return this.getAttributeDTO('styleCategory2').getAttributeMdDTO();
    },
    getStyleCategory3 : function()
    {
      return this.getAttributeDTO('styleCategory3').getValue();
    },
    setStyleCategory3 : function(value)
    {
      var attributeDTO = this.getAttributeDTO('styleCategory3');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isStyleCategory3Readable : function()
    {
      return this.getAttributeDTO('styleCategory3').isReadable();
    },
    isStyleCategory3Writable : function()
    {
      return this.getAttributeDTO('styleCategory3').isWritable();
    },
    isStyleCategory3Modified : function()
    {
      return this.getAttributeDTO('styleCategory3').isModified();
    },
    getStyleCategory3Md : function()
    {
      return this.getAttributeDTO('styleCategory3').getAttributeMdDTO();
    },
    getStyleCategory4 : function()
    {
      return this.getAttributeDTO('styleCategory4').getValue();
    },
    setStyleCategory4 : function(value)
    {
      var attributeDTO = this.getAttributeDTO('styleCategory4');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isStyleCategory4Readable : function()
    {
      return this.getAttributeDTO('styleCategory4').isReadable();
    },
    isStyleCategory4Writable : function()
    {
      return this.getAttributeDTO('styleCategory4').isWritable();
    },
    isStyleCategory4Modified : function()
    {
      return this.getAttributeDTO('styleCategory4').isModified();
    },
    getStyleCategory4Md : function()
    {
      return this.getAttributeDTO('styleCategory4').getAttributeMdDTO();
    },
    getStyleCategory5 : function()
    {
      return this.getAttributeDTO('styleCategory5').getValue();
    },
    setStyleCategory5 : function(value)
    {
      var attributeDTO = this.getAttributeDTO('styleCategory5');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isStyleCategory5Readable : function()
    {
      return this.getAttributeDTO('styleCategory5').isReadable();
    },
    isStyleCategory5Writable : function()
    {
      return this.getAttributeDTO('styleCategory5').isWritable();
    },
    isStyleCategory5Modified : function()
    {
      return this.getAttributeDTO('styleCategory5').isModified();
    },
    getStyleCategory5Md : function()
    {
      return this.getAttributeDTO('styleCategory5').getAttributeMdDTO();
    },
    getStyleCategoryFill1 : function()
    {
      return this.getAttributeDTO('styleCategoryFill1').getValue();
    },
    setStyleCategoryFill1 : function(value)
    {
      var attributeDTO = this.getAttributeDTO('styleCategoryFill1');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isStyleCategoryFill1Readable : function()
    {
      return this.getAttributeDTO('styleCategoryFill1').isReadable();
    },
    isStyleCategoryFill1Writable : function()
    {
      return this.getAttributeDTO('styleCategoryFill1').isWritable();
    },
    isStyleCategoryFill1Modified : function()
    {
      return this.getAttributeDTO('styleCategoryFill1').isModified();
    },
    getStyleCategoryFill1Md : function()
    {
      return this.getAttributeDTO('styleCategoryFill1').getAttributeMdDTO();
    },
    getStyleCategoryFill2 : function()
    {
      return this.getAttributeDTO('styleCategoryFill2').getValue();
    },
    setStyleCategoryFill2 : function(value)
    {
      var attributeDTO = this.getAttributeDTO('styleCategoryFill2');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isStyleCategoryFill2Readable : function()
    {
      return this.getAttributeDTO('styleCategoryFill2').isReadable();
    },
    isStyleCategoryFill2Writable : function()
    {
      return this.getAttributeDTO('styleCategoryFill2').isWritable();
    },
    isStyleCategoryFill2Modified : function()
    {
      return this.getAttributeDTO('styleCategoryFill2').isModified();
    },
    getStyleCategoryFill2Md : function()
    {
      return this.getAttributeDTO('styleCategoryFill2').getAttributeMdDTO();
    },
    getStyleCategoryFill3 : function()
    {
      return this.getAttributeDTO('styleCategoryFill3').getValue();
    },
    setStyleCategoryFill3 : function(value)
    {
      var attributeDTO = this.getAttributeDTO('styleCategoryFill3');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isStyleCategoryFill3Readable : function()
    {
      return this.getAttributeDTO('styleCategoryFill3').isReadable();
    },
    isStyleCategoryFill3Writable : function()
    {
      return this.getAttributeDTO('styleCategoryFill3').isWritable();
    },
    isStyleCategoryFill3Modified : function()
    {
      return this.getAttributeDTO('styleCategoryFill3').isModified();
    },
    getStyleCategoryFill3Md : function()
    {
      return this.getAttributeDTO('styleCategoryFill3').getAttributeMdDTO();
    },
    getStyleCategoryFill4 : function()
    {
      return this.getAttributeDTO('styleCategoryFill4').getValue();
    },
    setStyleCategoryFill4 : function(value)
    {
      var attributeDTO = this.getAttributeDTO('styleCategoryFill4');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isStyleCategoryFill4Readable : function()
    {
      return this.getAttributeDTO('styleCategoryFill4').isReadable();
    },
    isStyleCategoryFill4Writable : function()
    {
      return this.getAttributeDTO('styleCategoryFill4').isWritable();
    },
    isStyleCategoryFill4Modified : function()
    {
      return this.getAttributeDTO('styleCategoryFill4').isModified();
    },
    getStyleCategoryFill4Md : function()
    {
      return this.getAttributeDTO('styleCategoryFill4').getAttributeMdDTO();
    },
    getStyleCategoryFill5 : function()
    {
      return this.getAttributeDTO('styleCategoryFill5').getValue();
    },
    setStyleCategoryFill5 : function(value)
    {
      var attributeDTO = this.getAttributeDTO('styleCategoryFill5');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isStyleCategoryFill5Readable : function()
    {
      return this.getAttributeDTO('styleCategoryFill5').isReadable();
    },
    isStyleCategoryFill5Writable : function()
    {
      return this.getAttributeDTO('styleCategoryFill5').isWritable();
    },
    isStyleCategoryFill5Modified : function()
    {
      return this.getAttributeDTO('styleCategoryFill5').isModified();
    },
    getStyleCategoryFill5Md : function()
    {
      return this.getAttributeDTO('styleCategoryFill5').getAttributeMdDTO();
    },
    getStyleCondition : function(clientRequest)
    {
      var attributeDTO = this.getAttributeDTO('styleCondition');
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
    setStyleCondition : function(ref)
    {
      var attributeDTO = this.getAttributeDTO('styleCondition');
      attributeDTO.setValue(Mojo.Util.isObject(ref) ? ref.getId() : ref);
      this.setModified(true);
    },
    isStyleConditionReadable : function()
    {
      return this.getAttributeDTO('styleCondition').isReadable();
    },
    isStyleConditionWritable : function()
    {
      return this.getAttributeDTO('styleCondition').isWritable();
    },
    isStyleConditionModified : function()
    {
      return this.getAttributeDTO('styleCondition').isModified();
    },
    getStyleConditionMd : function()
    {
      return this.getAttributeDTO('styleCondition').getAttributeMdDTO();
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"readable\":true,\"dto_type\":\"com.runwaysdk.business.BusinessDTO\",\"attributeMap\":{\"lockedBy\":{\"attributeName\":\"lockedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"itjj7ucnyhl9vpbicr3bd7l4pdxieyhe00000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Locked By\",\"description\":\"User who has a write lock on this object\",\"name\":\"lockedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.Users\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"pointSize\":{\"attributeName\":\"pointSize\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"i8wv8urhufy6hvxaeqwgomzo85dk0fmy00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Size\",\"description\":\"\",\"name\":\"pointSize\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":6,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"valueSize\":{\"attributeName\":\"valueSize\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"i683xk5f1yo07trcbq9vdmnn4ntmh6uq00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Size\",\"description\":\"\",\"name\":\"valueSize\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":12,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"aggregationType\":{\"attributeName\":\"aggregationType\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"ib97qsxf0rjvwcofk1jmng46urabkzcq00000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.geodashboard.gis.persist.AllAggregationType\",\"system\":false,\"displayLabel\":\"Aggregation Type\",\"description\":\"\",\"name\":\"aggregationType\",\"immutable\":false,\"enumNames\":{\"MAX\":\"Max\",\"MIN\":\"Min\",\"MINORITY\":\"Minority\",\"SUM\":\"Sum\",\"DISTRIBUTION\":\"Distribution\",\"MAJORITY\":\"Majority\",\"AVG\":\"Avg\"},\"_selectMultiple\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"enumNames\":[],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":false},\"enableLabel\":{\"attributeName\":\"enableLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"iad8hyz8gcckgkm13enegeqhm6wxgfzd00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Display Label\",\"negativeDisplayLabel\":\"\",\"description\":\"\",\"name\":\"enableLabel\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"entityDomain\":{\"attributeName\":\"entityDomain\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"iizkjhj5gd7icwona3w90n6bffkl94y300000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Entity Domain\",\"description\":\"The Domain of this entity\",\"name\":\"entityDomain\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdDomain\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"type\":{\"attributeName\":\"type\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"icaiuroye6hgvc6c8ae8xlogunteygdw00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Type\",\"description\":\"Fully qualified type of this object\",\"name\":\"type\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"styleCategoryFill2\":{\"attributeName\":\"styleCategoryFill2\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i5gdbm0dyg6o2ibdmqjztq0ksj2mndho00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Category Fill for the 2nd category\",\"description\":\"\",\"name\":\"styleCategoryFill2\",\"immutable\":false,\"required\":false,\"size\":150},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"styleCategoryFill3\":{\"attributeName\":\"styleCategoryFill3\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"itanukf9lsmac32bbfsx3ksclxrkg9eh00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Category Fill for the 3rd category\",\"description\":\"\",\"name\":\"styleCategoryFill3\",\"immutable\":false,\"required\":false,\"size\":150},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"styleCategoryFill4\":{\"attributeName\":\"styleCategoryFill4\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"iecpidnga4nrn5q6k05b8ay9jt15c0r600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Category Fill for the 4th category\",\"description\":\"\",\"name\":\"styleCategoryFill4\",\"immutable\":false,\"required\":false,\"size\":150},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"pointStrokeWidth\":{\"attributeName\":\"pointStrokeWidth\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"iky0grdy1s3v6v7g9nx6zxlrbwam8waw00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Width\",\"description\":\"\",\"name\":\"pointStrokeWidth\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":2,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"styleCategoryFill5\":{\"attributeName\":\"styleCategoryFill5\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"il21tlpkxgyegz1fz6xfi53pcs8csz4400000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Category Fill for the 5th category\",\"description\":\"\",\"name\":\"styleCategoryFill5\",\"immutable\":false,\"required\":false,\"size\":150},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"styleCategory3\":{\"attributeName\":\"styleCategory3\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"idbrk1ymzljnk1oa4x2ywwkzdsau3a5a00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Category Value for the 3rd category\",\"description\":\"\",\"name\":\"styleCategory3\",\"immutable\":false,\"required\":false,\"size\":150},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"pointFill\":{\"attributeName\":\"pointFill\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i35q7g1p21c194ion6wvzh63qdc3i08300000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Color\",\"description\":\"\",\"name\":\"pointFill\",\"immutable\":false,\"required\":true,\"size\":7},\"writable\":true,\"value\":\"#00bfff\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"styleCategory4\":{\"attributeName\":\"styleCategory4\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i5xxgzpz8v5efb8nfwqk5f9vwvcxslua00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Category Value for the 4th category\",\"description\":\"\",\"name\":\"styleCategory4\",\"immutable\":false,\"required\":false,\"size\":150},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"pointMinSize\":{\"attributeName\":\"pointMinSize\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"i7af1drnagcdew2gsjaus89oloi0w3nl00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Min\",\"description\":\"\",\"name\":\"pointMinSize\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":1,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"styleCategory5\":{\"attributeName\":\"styleCategory5\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i6t8kbita2nj8ns7otfxhzsj8vfdfmbp00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Category Value for the 5th category\",\"description\":\"\",\"name\":\"styleCategory5\",\"immutable\":false,\"required\":false,\"size\":150},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"pointOpacity\":{\"attributeName\":\"pointOpacity\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDoubleDTO\",\"attributeMdDTO\":{\"id\":\"ibace8x7fnl63e2h7vbodtnpv7vhocf800000000000000000000000000000170\",\"system\":false,\"totalLength\":3,\"displayLabel\":\"Opacity\",\"decimalLength\":2,\"description\":\"\",\"name\":\"pointOpacity\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":0.9,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDouble\",\"modified\":false},\"pointRotation\":{\"attributeName\":\"pointRotation\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"i2655u6dwk1ss1mb9zju2yfetkfgdana00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Point Rotation\",\"description\":\"\",\"name\":\"pointRotation\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":false},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i291hcwvy4fj5p1z2jzft09g0qj10zwn00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"styleCategory1\":{\"attributeName\":\"styleCategory1\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"is3m855iea3jjqljggbgp1mk61odbbyj00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Category Value for the 1st category\",\"description\":\"\",\"name\":\"styleCategory1\",\"immutable\":false,\"required\":false,\"size\":150},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"styleCategory2\":{\"attributeName\":\"styleCategory2\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i6zwz78e4knm0xjuiy8vwaeehr79vngx00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Category Value for the 2nd category\",\"description\":\"\",\"name\":\"styleCategory2\",\"immutable\":false,\"required\":false,\"size\":150},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"polygonStrokeOpacity\":{\"attributeName\":\"polygonStrokeOpacity\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDoubleDTO\",\"attributeMdDTO\":{\"id\":\"i1lebay6d5v7jqqq1tset8tztvmj4smt00000000000000000000000000000170\",\"system\":false,\"totalLength\":3,\"displayLabel\":\"Opacity\",\"decimalLength\":2,\"description\":\"\",\"name\":\"polygonStrokeOpacity\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":0.9,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDouble\",\"modified\":false},\"labelHalo\":{\"attributeName\":\"labelHalo\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ikn1rzbxh10sjecg0ux6dj7q12j3d2iw00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Halo\",\"description\":\"\",\"name\":\"labelHalo\",\"immutable\":false,\"required\":true,\"size\":7},\"writable\":true,\"value\":\"#FFFFFF\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"lineOpacity\":{\"attributeName\":\"lineOpacity\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDoubleDTO\",\"attributeMdDTO\":{\"id\":\"im64x92qmumto2qvw4i2tvbpnf21o2el00000000000000000000000000000170\",\"system\":false,\"totalLength\":3,\"displayLabel\":\"Opacity\",\"decimalLength\":2,\"description\":\"\",\"name\":\"lineOpacity\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":0.9,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDouble\",\"modified\":false},\"createdBy\":{\"attributeName\":\"createdBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"ibskt0wgz3dahzxidbwkve9qkeqna5s200000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Created By\",\"description\":\"User that created this object\",\"name\":\"createdBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":true,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"lineStrokeWidth\":{\"attributeName\":\"lineStrokeWidth\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"i8ej0ii8hqhggnh9dodr73t8emddnar000000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Width\",\"description\":\"\",\"name\":\"lineStrokeWidth\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":3,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"owner\":{\"attributeName\":\"owner\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"itratvulvkrexf3tygbi7iukqkmzwe6v00000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"Owner\",\"description\":\"A reference to the user who own this object\",\"name\":\"owner\",\"referencedMdBusiness\":\"com.runwaysdk.system.Actor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"valueFont\":{\"attributeName\":\"valueFont\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ik970i1w87usfeql5kejk75dv3fuaank00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Font\",\"description\":\"\",\"name\":\"valueFont\",\"immutable\":false,\"required\":true,\"size\":50},\"writable\":true,\"value\":\"Arial\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"labelFont\":{\"attributeName\":\"labelFont\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i8djfbatg5y3rwuevd7hhw4pxdy4k1r400000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Font\",\"description\":\"\",\"name\":\"labelFont\",\"immutable\":false,\"required\":true,\"size\":50},\"writable\":true,\"value\":\"Arial\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"pointStrokeOpacity\":{\"attributeName\":\"pointStrokeOpacity\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDoubleDTO\",\"attributeMdDTO\":{\"id\":\"i635vg70wnvsn8zqd2pyreavsa21dqh300000000000000000000000000000170\",\"system\":false,\"totalLength\":2,\"displayLabel\":\"Opacity\",\"decimalLength\":2,\"description\":\"\",\"name\":\"pointStrokeOpacity\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":0.7,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDouble\",\"modified\":false},\"valueHaloWidth\":{\"attributeName\":\"valueHaloWidth\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"ihuz2gkwo2mkqwzz77ebysuqtdz1vg1b00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Halo Width\",\"description\":\"\",\"name\":\"valueHaloWidth\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"mdAttribute\":{\"attributeName\":\"mdAttribute\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"i8pbvoi6ejugkiso3sudfmwp88iix6oe00000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"\",\"description\":\"\",\"name\":\"mdAttribute\",\"referencedMdBusiness\":\"com.runwaysdk.system.metadata.MdAttribute\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"styleCondition\":{\"attributeName\":\"styleCondition\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"i0xjk8veam612mu47i59oyq5d0s9pbbi00000000000000000000000000000186\",\"system\":false,\"displayLabel\":\"\",\"description\":\"\",\"name\":\"styleCondition\",\"referencedMdBusiness\":\"com.runwaysdk.geodashboard.gis.persist.condition.DashboardCondition\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"labelColor\":{\"attributeName\":\"labelColor\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ieapeu3itftstvo7x7ouhorfo4j7s1gd00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Color\",\"description\":\"\",\"name\":\"labelColor\",\"immutable\":false,\"required\":true,\"size\":7},\"writable\":true,\"value\":\"#000000\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"valueHalo\":{\"attributeName\":\"valueHalo\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"iegdwb66x0nwrzsnrr5uv1mc0wmw7lw900000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Halo\",\"description\":\"\",\"name\":\"valueHalo\",\"immutable\":false,\"required\":true,\"size\":10},\"writable\":true,\"value\":\"#FFFFFF\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"pointWellKnownName\":{\"attributeName\":\"pointWellKnownName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ikz5shix771j3ox70vhc8deufkgmbh5t00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Well Known Name\",\"description\":\"\",\"name\":\"pointWellKnownName\",\"immutable\":false,\"required\":true,\"size\":50},\"writable\":true,\"value\":\"circle\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"polygonStroke\":{\"attributeName\":\"polygonStroke\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i7jodr32q1j7p7hr768pynfhi81lf2es00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Color\",\"description\":\"\",\"name\":\"polygonStroke\",\"immutable\":false,\"required\":false,\"size\":7},\"writable\":true,\"value\":\"#000000\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"polygonFillOpacity\":{\"attributeName\":\"polygonFillOpacity\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDoubleDTO\",\"attributeMdDTO\":{\"id\":\"i5dkqo2wqkgmibtbftg4m2tjhmoxfubf00000000000000000000000000000170\",\"system\":false,\"totalLength\":3,\"displayLabel\":\"Opacity\",\"decimalLength\":2,\"description\":\"\",\"name\":\"polygonFillOpacity\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":0.9,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDouble\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"itkhqq476ozd7vxz71i3n5eezg257jmr00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"24voq4ed5hzs10jd51d6ck11h064yizaioe1zceeim2lf04jhi20ohu6eob0gi0i\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i6hl7qufjh3ecixvkv6ed4kqyr6sgym200000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"polygonFill\":{\"attributeName\":\"polygonFill\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ioubnhqkqd1rpyg73ezsequlsmdekzfb00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Color\",\"description\":\"\",\"name\":\"polygonFill\",\"immutable\":false,\"required\":true,\"size\":7},\"writable\":true,\"value\":\"#00bfff\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"polygonMinFill\":{\"attributeName\":\"polygonMinFill\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ia30difz76ozwr30w0gv2odmkzw80nb600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Min\",\"description\":\"\",\"name\":\"polygonMinFill\",\"immutable\":false,\"required\":true,\"size\":7},\"writable\":true,\"value\":\"#ffffff\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"name\":{\"attributeName\":\"name\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"io7968jrldoynd0kdiv4adpto624ay8z00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Name\",\"description\":\"\",\"name\":\"name\",\"immutable\":false,\"required\":false,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"labelSize\":{\"attributeName\":\"labelSize\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"ii71nbgd7906j975omr4aa23s4hww58500000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Size\",\"description\":\"\",\"name\":\"labelSize\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":12,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"createDate\":{\"attributeName\":\"createDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"iezm55bemreoatoj00chyeqp0axs9ftj00000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Create Date\",\"description\":\"Date and time this record was created\",\"name\":\"createDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"pointFixedSize\":{\"attributeName\":\"pointFixedSize\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"is59o8qeb3g5f9r3mq7exmoklomzl21p00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Fixed Radius\",\"description\":\"\",\"name\":\"pointFixedSize\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":5,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"labelHaloWidth\":{\"attributeName\":\"labelHaloWidth\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"id7vo98in69auc1cjl2ibjpobrctuz9f00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Halo Width\",\"description\":\"\",\"name\":\"labelHaloWidth\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"lineStrokeCap\":{\"attributeName\":\"lineStrokeCap\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ie7rmx840k2qq3dbbc6x3vltdvu2tv8k00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Line Stroke Cap\",\"description\":\"\",\"name\":\"lineStrokeCap\",\"immutable\":false,\"required\":false,\"size\":6},\"writable\":true,\"value\":\"butt\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"enableValue\":{\"attributeName\":\"enableValue\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"ir0aa12gqwz71vtkimkdu239nxf9asxs00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Display Value\",\"negativeDisplayLabel\":\"\",\"description\":\"\",\"name\":\"enableValue\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"lineStroke\":{\"attributeName\":\"lineStroke\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ijf7jermt0qyhql26k6g56ql5kegpjq100000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Color\",\"description\":\"\",\"name\":\"lineStroke\",\"immutable\":false,\"required\":true,\"size\":7},\"writable\":true,\"value\":\"#000000\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"lastUpdateDate\":{\"attributeName\":\"lastUpdateDate\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeDateTimeDTO\",\"attributeMdDTO\":{\"id\":\"i4wc3qgg3c23gjcfy3ykhzwoazy49ptn00000000000000000000000000000341\",\"system\":true,\"displayLabel\":\"Last Update Date\",\"description\":\"Date and time this record was last updated\",\"name\":\"lastUpdateDate\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeDateTime\",\"modified\":false},\"pointStroke\":{\"attributeName\":\"pointStroke\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ihqtvhx0vvkwzs8m90f0j1yqtn7n6vnx00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Color\",\"description\":\"\",\"name\":\"pointStroke\",\"immutable\":false,\"required\":false,\"size\":7},\"writable\":true,\"value\":\"#000000\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"pointMaxSize\":{\"attributeName\":\"pointMaxSize\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"i55vib1meqe6v2w50w8nolsu3qjf2ucv00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Max\",\"description\":\"\",\"name\":\"pointMaxSize\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":10,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"polygonStrokeWidth\":{\"attributeName\":\"polygonStrokeWidth\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"ien9x4p3l7qfb4z24t4tobtu3bk9fk9k00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Width\",\"description\":\"\",\"name\":\"polygonStrokeWidth\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":2,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"polygonMaxFill\":{\"attributeName\":\"polygonMaxFill\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i2kpxjzqbkvppa3c9br1p4mbx53hp2kc00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Max\",\"description\":\"\",\"name\":\"polygonMaxFill\",\"immutable\":false,\"required\":true,\"size\":7},\"writable\":true,\"value\":\"#505050\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"lastUpdatedBy\":{\"attributeName\":\"lastUpdatedBy\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeReferenceDTO\",\"attributeMdDTO\":{\"id\":\"iriz4tomrlrbq3kc51a2fgd9e8akhvvb00000000000000000000000000000186\",\"system\":true,\"displayLabel\":\"Last Updated By\",\"description\":\"User who last updated this object\",\"name\":\"lastUpdatedBy\",\"referencedMdBusiness\":\"com.runwaysdk.system.SingleActor\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeReference\",\"modified\":false},\"seq\":{\"attributeName\":\"seq\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeLongDTO\",\"attributeMdDTO\":{\"id\":\"ibspcqp2ki7nj856zgkzmwk2qiljbhur00000000000000000000000000000338\",\"system\":true,\"displayLabel\":\"Sequence\",\"description\":\"Sequence number is incremented every time this object is updated\",\"name\":\"seq\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeLong\",\"modified\":false},\"valueColor\":{\"attributeName\":\"valueColor\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i55u7ntik8tndrdvfklhbrzqvclf6pkn00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Color\",\"description\":\"\",\"name\":\"valueColor\",\"immutable\":false,\"required\":true,\"size\":10},\"writable\":true,\"value\":\"#000000\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"styleCategoryFill1\":{\"attributeName\":\"styleCategoryFill1\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"iajgddgkebgj3d81u4a774d9b82w551n00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Category Fill for the 1st category\",\"description\":\"\",\"name\":\"styleCategoryFill1\",\"immutable\":false,\"required\":false,\"size\":150},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"_toString\":\"New: Thematic Attribute\",\"writable\":true,\"state\":\"\",\"modified\":true,\"id\":\"24voq4ed5hzs10jd51d6ck11h064yizaioe1zceeim2lf04jhi20ohu6eob0gi0i\",\"_type\":\"com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle\",\"_typeMd\":{\"id\":\"ioe1zceeim2lf04jhi20ohu6eob0gi0i00000000000000000000000000000001\",\"displayLabel\":\"Thematic Attribute\",\"description\":\"\"},\"transitions\":[],\"newInstance\":true,\"lockedByCurrentUser\":false}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
    getSortedFonts : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle', methodName:'getSortedFonts', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getSortedAggregations : function(clientRequest, thematicAttributeId)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle', methodName:'getSortedAggregations', declaredTypes: ["java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    }
  }
});
Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyleQueryDTO', {
  Extends : 'com.runwaysdk.geodashboard.gis.persist.DashboardStyleQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle'
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
