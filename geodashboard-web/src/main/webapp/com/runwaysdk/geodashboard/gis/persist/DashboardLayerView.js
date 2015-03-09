Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.persist.DashboardLayerView', {
  Extends : 'com.runwaysdk.business.ViewDTO',
  Constants : 
  {
    ACTIVEBYDEFAULT : 'activeByDefault',
    AGGREGATIONATTRIBUTE : 'aggregationAttribute',
    AGGREGATIONMETHOD : 'aggregationMethod',
    DISPLAYINLEGEND : 'displayInLegend',
    GROUPEDINLEGEND : 'groupedInLegend',
    ID : 'id',
    LAYERID : 'layerId',
    LAYERISACTIVE : 'layerIsActive',
    LAYERNAME : 'layerName',
    LEGENDXPOSITION : 'legendXPosition',
    LEGENDYPOSITION : 'legendYPosition',
    SLDNAME : 'sldName',
    VIEWNAME : 'viewName',
    CLASS : 'com.runwaysdk.geodashboard.gis.persist.DashboardLayerView'
  },
  Instance: 
  {
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
    getAggregationAttribute : function()
    {
      return this.getAttributeDTO('aggregationAttribute').getValue();
    },
    setAggregationAttribute : function(value)
    {
      var attributeDTO = this.getAttributeDTO('aggregationAttribute');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isAggregationAttributeReadable : function()
    {
      return this.getAttributeDTO('aggregationAttribute').isReadable();
    },
    isAggregationAttributeWritable : function()
    {
      return this.getAttributeDTO('aggregationAttribute').isWritable();
    },
    isAggregationAttributeModified : function()
    {
      return this.getAttributeDTO('aggregationAttribute').isModified();
    },
    getAggregationAttributeMd : function()
    {
      return this.getAttributeDTO('aggregationAttribute').getAttributeMdDTO();
    },
    getAggregationMethod : function()
    {
      return this.getAttributeDTO('aggregationMethod').getValue();
    },
    setAggregationMethod : function(value)
    {
      var attributeDTO = this.getAttributeDTO('aggregationMethod');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isAggregationMethodReadable : function()
    {
      return this.getAttributeDTO('aggregationMethod').isReadable();
    },
    isAggregationMethodWritable : function()
    {
      return this.getAttributeDTO('aggregationMethod').isWritable();
    },
    isAggregationMethodModified : function()
    {
      return this.getAttributeDTO('aggregationMethod').isModified();
    },
    getAggregationMethodMd : function()
    {
      return this.getAttributeDTO('aggregationMethod').getAttributeMdDTO();
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
    getGroupedInLegend : function()
    {
      return this.getAttributeDTO('groupedInLegend').getValue();
    },
    setGroupedInLegend : function(value)
    {
      var attributeDTO = this.getAttributeDTO('groupedInLegend');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isGroupedInLegendReadable : function()
    {
      return this.getAttributeDTO('groupedInLegend').isReadable();
    },
    isGroupedInLegendWritable : function()
    {
      return this.getAttributeDTO('groupedInLegend').isWritable();
    },
    isGroupedInLegendModified : function()
    {
      return this.getAttributeDTO('groupedInLegend').isModified();
    },
    getGroupedInLegendMd : function()
    {
      return this.getAttributeDTO('groupedInLegend').getAttributeMdDTO();
    },
    getLayerId : function()
    {
      return this.getAttributeDTO('layerId').getValue();
    },
    setLayerId : function(value)
    {
      var attributeDTO = this.getAttributeDTO('layerId');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isLayerIdReadable : function()
    {
      return this.getAttributeDTO('layerId').isReadable();
    },
    isLayerIdWritable : function()
    {
      return this.getAttributeDTO('layerId').isWritable();
    },
    isLayerIdModified : function()
    {
      return this.getAttributeDTO('layerId').isModified();
    },
    getLayerIdMd : function()
    {
      return this.getAttributeDTO('layerId').getAttributeMdDTO();
    },
    getLayerIsActive : function()
    {
      return this.getAttributeDTO('layerIsActive').getValue();
    },
    setLayerIsActive : function(value)
    {
      var attributeDTO = this.getAttributeDTO('layerIsActive');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isLayerIsActiveReadable : function()
    {
      return this.getAttributeDTO('layerIsActive').isReadable();
    },
    isLayerIsActiveWritable : function()
    {
      return this.getAttributeDTO('layerIsActive').isWritable();
    },
    isLayerIsActiveModified : function()
    {
      return this.getAttributeDTO('layerIsActive').isModified();
    },
    getLayerIsActiveMd : function()
    {
      return this.getAttributeDTO('layerIsActive').getAttributeMdDTO();
    },
    getLayerName : function()
    {
      return this.getAttributeDTO('layerName').getValue();
    },
    setLayerName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('layerName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isLayerNameReadable : function()
    {
      return this.getAttributeDTO('layerName').isReadable();
    },
    isLayerNameWritable : function()
    {
      return this.getAttributeDTO('layerName').isWritable();
    },
    isLayerNameModified : function()
    {
      return this.getAttributeDTO('layerName').isModified();
    },
    getLayerNameMd : function()
    {
      return this.getAttributeDTO('layerName').getAttributeMdDTO();
    },
    getLegendXPosition : function()
    {
      return this.getAttributeDTO('legendXPosition').getValue();
    },
    setLegendXPosition : function(value)
    {
      var attributeDTO = this.getAttributeDTO('legendXPosition');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isLegendXPositionReadable : function()
    {
      return this.getAttributeDTO('legendXPosition').isReadable();
    },
    isLegendXPositionWritable : function()
    {
      return this.getAttributeDTO('legendXPosition').isWritable();
    },
    isLegendXPositionModified : function()
    {
      return this.getAttributeDTO('legendXPosition').isModified();
    },
    getLegendXPositionMd : function()
    {
      return this.getAttributeDTO('legendXPosition').getAttributeMdDTO();
    },
    getLegendYPosition : function()
    {
      return this.getAttributeDTO('legendYPosition').getValue();
    },
    setLegendYPosition : function(value)
    {
      var attributeDTO = this.getAttributeDTO('legendYPosition');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isLegendYPositionReadable : function()
    {
      return this.getAttributeDTO('legendYPosition').isReadable();
    },
    isLegendYPositionWritable : function()
    {
      return this.getAttributeDTO('legendYPosition').isWritable();
    },
    isLegendYPositionModified : function()
    {
      return this.getAttributeDTO('legendYPosition').isModified();
    },
    getLegendYPositionMd : function()
    {
      return this.getAttributeDTO('legendYPosition').getAttributeMdDTO();
    },
    getSldName : function()
    {
      return this.getAttributeDTO('sldName').getValue();
    },
    setSldName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('sldName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isSldNameReadable : function()
    {
      return this.getAttributeDTO('sldName').isReadable();
    },
    isSldNameWritable : function()
    {
      return this.getAttributeDTO('sldName').isWritable();
    },
    isSldNameModified : function()
    {
      return this.getAttributeDTO('sldName').isModified();
    },
    getSldNameMd : function()
    {
      return this.getAttributeDTO('sldName').getAttributeMdDTO();
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
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"id\":\"6v7dglsn08o35hk9p7y3c8ctzw8kmj42irx89bti064i57eiik1o29sjjn8f6n8t\",\"readable\":true,\"_type\":\"com.runwaysdk.geodashboard.gis.persist.DashboardLayerView\",\"attributeMap\":{\"legendXPosition\":{\"attributeName\":\"legendXPosition\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"il1w7x0k92it27yjc7hr63e7i2p3e5pnNM200811060000000000000000000001\",\"system\":false,\"displayLabel\":\"Legend X Position\",\"description\":\"\",\"name\":\"legendXPosition\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":false},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"legendYPosition\":{\"attributeName\":\"legendYPosition\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"int8pd47ck75s0d58xna8vf8slchf3vtNM200811060000000000000000000001\",\"system\":false,\"displayLabel\":\"Legend Y Position\",\"description\":\"\",\"name\":\"legendYPosition\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":false},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"aggregationMethod\":{\"attributeName\":\"aggregationMethod\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"idanvwlsl8vxol66acsfv68uxt2s3mys00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Aggregation Method\",\"description\":\"\",\"name\":\"aggregationMethod\",\"immutable\":false,\"required\":true,\"size\":10},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"groupedInLegend\":{\"attributeName\":\"groupedInLegend\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"i0y8f446qibj0o18b9c0ewd0pii9cje1NM200811060000000000000000000001\",\"system\":false,\"displayLabel\":\"Grouped In Legend\",\"negativeDisplayLabel\":\"\",\"description\":\"\",\"name\":\"groupedInLegend\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"layerName\":{\"attributeName\":\"layerName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i45xjfffv3kuonlb4qdizmtjfn7kc9g400000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"layerName\",\"description\":\"\",\"name\":\"layerName\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"activeByDefault\":{\"attributeName\":\"activeByDefault\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"it75suk015wk7a9luebg89hkg4c3jhc4NM200811060000000000000000000001\",\"system\":false,\"displayLabel\":\"Layer Is Active By Default\",\"negativeDisplayLabel\":\"\",\"description\":\"\",\"name\":\"activeByDefault\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"layerId\":{\"attributeName\":\"layerId\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"iezi8f4nnvwuiooltqfphm9jnq9qztqs00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"layerId\",\"description\":\"\",\"name\":\"layerId\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i81eov26qvn2qii05qwj4iooat0ecri700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"6v7dglsn08o35hk9p7y3c8ctzw8kmj42irx89bti064i57eiik1o29sjjn8f6n8t\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"layerIsActive\":{\"attributeName\":\"layerIsActive\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"i9djs9c92mwzunlkexjkj0dyhdob678300000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Layer Is Active\",\"negativeDisplayLabel\":\"\",\"description\":\"\",\"name\":\"layerIsActive\",\"immutable\":false,\"required\":false,\"positiveDisplayLabel\":\"\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"aggregationAttribute\":{\"attributeName\":\"aggregationAttribute\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"iet17i21ua73vo55k6qosy0bcy3qv06j00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Aggregation Attribute\",\"description\":\"\",\"name\":\"aggregationAttribute\",\"immutable\":false,\"required\":true,\"size\":50},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"viewName\":{\"attributeName\":\"viewName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ibz488a1p7n08isobrcxe15svgtbtkcr00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"viewName\",\"description\":\"\",\"name\":\"viewName\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"displayInLegend\":{\"attributeName\":\"displayInLegend\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"i3yjl6nfdrhmpodi9lwga19gtujbqmzfNM200811060000000000000000000001\",\"system\":false,\"displayLabel\":\"Display In Legend\",\"negativeDisplayLabel\":\"\",\"description\":\"\",\"name\":\"displayInLegend\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"sldName\":{\"attributeName\":\"sldName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i7txr1no2lw54p10zhoreot37nnyjp3l00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"sldName\",\"description\":\"\",\"name\":\"sldName\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.ViewDTO\",\"_toString\":\"New: Dashboard Layer View\",\"_typeMd\":{\"id\":\"irx89bti064i57eiik1o29sjjn8f6n8t20071129NM0000000000000000000005\",\"displayLabel\":\"Dashboard Layer View\",\"description\":\"\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.persist.DashboardLayerViewQueryDTO', {
  Extends : 'com.runwaysdk.business.ViewQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.gis.persist.DashboardLayerView'
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
