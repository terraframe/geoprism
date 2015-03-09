Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.persist.DashboardLegend', {
  Extends : 'com.runwaysdk.business.StructDTO',
  Constants : 
  {
    GROUPEDINLEGEND : 'groupedInLegend',
    ID : 'id',
    KEYNAME : 'keyName',
    LEGENDXPOSITION : 'legendXPosition',
    LEGENDYPOSITION : 'legendYPosition',
    SITEMASTER : 'siteMaster',
    CLASS : 'com.runwaysdk.geodashboard.gis.persist.DashboardLegend'
  },
  Instance: 
  {
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
      if(obj == null)
      {
        var json = '{\"id\":\"08ojqsrhkodgba05gebqbzml8tt1rthhi5dmh42i1kau82i87tc9pjpg7s03iihl\",\"readable\":true,\"_type\":\"com.runwaysdk.geodashboard.gis.persist.DashboardLegend\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ic9lk5838j7lc0wpfd16hy2e9qha6mcf00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"08ojqsrhkodgba05gebqbzml8tt1rthhi5dmh42i1kau82i87tc9pjpg7s03iihl\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"legendYPosition\":{\"attributeName\":\"legendYPosition\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"il0ghr56cczjylwrge46ursu4i36921y00000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Legend Y Position\",\"description\":\"\",\"name\":\"legendYPosition\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":false},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"legendXPosition\":{\"attributeName\":\"legendXPosition\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"ii6zrnrbgw86x870hgrbwo6ipqxker3000000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Legend X Position\",\"description\":\"\",\"name\":\"legendXPosition\",\"immutable\":false,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":false,\"_rejectNegative\":false},\"writable\":true,\"value\":0,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"iltpot1kqe2u8i8fmnb7zx0m4mwetkfv00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"igr7ycr5hpj4uvqoyfbxnh5n79yg65ev00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"groupedInLegend\":{\"attributeName\":\"groupedInLegend\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"i144yxue1ypcue0wocimowfiblh1d6xb00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Grouped In Legend\",\"negativeDisplayLabel\":\"\",\"description\":\"\",\"name\":\"groupedInLegend\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"\"},\"writable\":true,\"value\":true,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.StructDTO\",\"_toString\":\"New: Legend\",\"_typeMd\":{\"id\":\"i5dmh42i1kau82i87tc9pjpg7s03iihl00000000000000000000000000000979\",\"displayLabel\":\"Legend\",\"description\":\"\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.persist.DashboardLegendQueryDTO', {
  Extends : 'com.runwaysdk.business.StructQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.gis.persist.DashboardLegend'
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
