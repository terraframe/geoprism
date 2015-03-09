Mojo.Meta.newClass('com.runwaysdk.geodashboard.MdAttributeView', {
  Extends : 'com.runwaysdk.business.ViewDTO',
  Constants : 
  {
    ATTRIBUTENAME : 'attributeName',
    ATTRIBUTETYPE : 'attributeType',
    DISPLAYLABEL : 'displayLabel',
    ID : 'id',
    MDATTRIBUTEID : 'mdAttributeId',
    MDCLASSID : 'mdClassId',
    CLASS : 'com.runwaysdk.geodashboard.MdAttributeView'
  },
  Instance: 
  {
    getAttributeName : function()
    {
      return this.getAttributeDTO('attributeName').getValue();
    },
    setAttributeName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('attributeName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isAttributeNameReadable : function()
    {
      return this.getAttributeDTO('attributeName').isReadable();
    },
    isAttributeNameWritable : function()
    {
      return this.getAttributeDTO('attributeName').isWritable();
    },
    isAttributeNameModified : function()
    {
      return this.getAttributeDTO('attributeName').isModified();
    },
    getAttributeNameMd : function()
    {
      return this.getAttributeDTO('attributeName').getAttributeMdDTO();
    },
    getAttributeType : function()
    {
      return this.getAttributeDTO('attributeType').getValue();
    },
    setAttributeType : function(value)
    {
      var attributeDTO = this.getAttributeDTO('attributeType');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isAttributeTypeReadable : function()
    {
      return this.getAttributeDTO('attributeType').isReadable();
    },
    isAttributeTypeWritable : function()
    {
      return this.getAttributeDTO('attributeType').isWritable();
    },
    isAttributeTypeModified : function()
    {
      return this.getAttributeDTO('attributeType').isModified();
    },
    getAttributeTypeMd : function()
    {
      return this.getAttributeDTO('attributeType').getAttributeMdDTO();
    },
    getDisplayLabel : function()
    {
      return this.getAttributeDTO('displayLabel').getValue();
    },
    setDisplayLabel : function(value)
    {
      var attributeDTO = this.getAttributeDTO('displayLabel');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isDisplayLabelReadable : function()
    {
      return this.getAttributeDTO('displayLabel').isReadable();
    },
    isDisplayLabelWritable : function()
    {
      return this.getAttributeDTO('displayLabel').isWritable();
    },
    isDisplayLabelModified : function()
    {
      return this.getAttributeDTO('displayLabel').isModified();
    },
    getDisplayLabelMd : function()
    {
      return this.getAttributeDTO('displayLabel').getAttributeMdDTO();
    },
    getMdAttributeId : function()
    {
      return this.getAttributeDTO('mdAttributeId').getValue();
    },
    setMdAttributeId : function(value)
    {
      var attributeDTO = this.getAttributeDTO('mdAttributeId');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isMdAttributeIdReadable : function()
    {
      return this.getAttributeDTO('mdAttributeId').isReadable();
    },
    isMdAttributeIdWritable : function()
    {
      return this.getAttributeDTO('mdAttributeId').isWritable();
    },
    isMdAttributeIdModified : function()
    {
      return this.getAttributeDTO('mdAttributeId').isModified();
    },
    getMdAttributeIdMd : function()
    {
      return this.getAttributeDTO('mdAttributeId').getAttributeMdDTO();
    },
    getMdClassId : function()
    {
      return this.getAttributeDTO('mdClassId').getValue();
    },
    setMdClassId : function(value)
    {
      var attributeDTO = this.getAttributeDTO('mdClassId');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isMdClassIdReadable : function()
    {
      return this.getAttributeDTO('mdClassId').isReadable();
    },
    isMdClassIdWritable : function()
    {
      return this.getAttributeDTO('mdClassId').isWritable();
    },
    isMdClassIdModified : function()
    {
      return this.getAttributeDTO('mdClassId').isModified();
    },
    getMdClassIdMd : function()
    {
      return this.getAttributeDTO('mdClassId').getAttributeMdDTO();
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"id\":\"8gdf2rqhqt5uzo80rl67o0n2xe2j27ayiorlrp8khr9fimd7z4l5nf285l2qjxta\",\"readable\":true,\"_type\":\"com.runwaysdk.geodashboard.MdAttributeView\",\"attributeMap\":{\"mdAttributeId\":{\"attributeName\":\"mdAttributeId\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ini8bdt6kqorergpi07wxthxlxiexngf00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"MdAttribute \",\"description\":\"\",\"name\":\"mdAttributeId\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"in5bi7w8gf7dwkwi38f5576xsn16ts9f00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"8gdf2rqhqt5uzo80rl67o0n2xe2j27ayiorlrp8khr9fimd7z4l5nf285l2qjxta\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"attributeName\":{\"attributeName\":\"attributeName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i6lidf6jwbgnoar6sltyxvw9cjxptycf00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Attribute Name\",\"description\":\"\",\"name\":\"attributeName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"displayLabel\":{\"attributeName\":\"displayLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"iq8kcwvk51dx0qxi3dtpoqlmu1c8bvd900000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Display Label\",\"description\":\"\",\"name\":\"displayLabel\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"attributeType\":{\"attributeName\":\"attributeType\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ickcrpfdn2dr7ruj1g9fx035vexhcgfp00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Attribute Type\",\"description\":\"\",\"name\":\"attributeType\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"mdClassId\":{\"attributeName\":\"mdClassId\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"imxvnotmizmi6f6wgoj84zvh4j8t0t4100000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"MdClass \",\"description\":\"\",\"name\":\"mdClassId\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.ViewDTO\",\"_toString\":\"New: MdAttribute View\",\"_typeMd\":{\"id\":\"iorlrp8khr9fimd7z4l5nf285l2qjxta20071129NM0000000000000000000005\",\"displayLabel\":\"MdAttribute View\",\"description\":\"\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.geodashboard.MdAttributeViewQueryDTO', {
  Extends : 'com.runwaysdk.business.ViewQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.MdAttributeView'
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
