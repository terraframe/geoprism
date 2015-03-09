Mojo.Meta.newClass('com.runwaysdk.geodashboard.report.PairView', {
  Extends : 'com.runwaysdk.business.ViewDTO',
  Constants : 
  {
    ID : 'id',
    LABEL : 'label',
    VALUE : 'value',
    CLASS : 'com.runwaysdk.geodashboard.report.PairView'
  },
  Instance: 
  {
    getLabel : function()
    {
      return this.getAttributeDTO('label').getValue();
    },
    setLabel : function(value)
    {
      var attributeDTO = this.getAttributeDTO('label');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isLabelReadable : function()
    {
      return this.getAttributeDTO('label').isReadable();
    },
    isLabelWritable : function()
    {
      return this.getAttributeDTO('label').isWritable();
    },
    isLabelModified : function()
    {
      return this.getAttributeDTO('label').isModified();
    },
    getLabelMd : function()
    {
      return this.getAttributeDTO('label').getAttributeMdDTO();
    },
    getValue : function()
    {
      return this.getAttributeDTO('value').getValue();
    },
    setValue : function(value)
    {
      var attributeDTO = this.getAttributeDTO('value');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isValueReadable : function()
    {
      return this.getAttributeDTO('value').isReadable();
    },
    isValueWritable : function()
    {
      return this.getAttributeDTO('value').isWritable();
    },
    isValueModified : function()
    {
      return this.getAttributeDTO('value').isModified();
    },
    getValueMd : function()
    {
      return this.getAttributeDTO('value').getAttributeMdDTO();
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"id\":\"ql9myhyr6ejd7htrs3n8brj8npqxbwdxigdwvibw9ebalzuz127vynyb2m681r8w\",\"readable\":true,\"_type\":\"com.runwaysdk.geodashboard.report.PairView\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"il92ivmhi5up88nc690ppuj8h6w3q7ih00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"ql9myhyr6ejd7htrs3n8brj8npqxbwdxigdwvibw9ebalzuz127vynyb2m681r8w\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"value\":{\"attributeName\":\"value\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeTextDTO\",\"attributeMdDTO\":{\"id\":\"itc2ml0yrcre2djgofbdodzxxhtepg5700000000000000000000000000000139\",\"system\":false,\"displayLabel\":\"Value\",\"description\":\"\",\"name\":\"value\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeText\",\"modified\":false},\"label\":{\"attributeName\":\"label\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeTextDTO\",\"attributeMdDTO\":{\"id\":\"ibehinnzpeli8vkqx6od7x4s8vi8bnbo00000000000000000000000000000139\",\"system\":false,\"displayLabel\":\"Label\",\"description\":\"\",\"name\":\"label\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeText\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.ViewDTO\",\"_toString\":\"New: Report item\",\"_typeMd\":{\"id\":\"igdwvibw9ebalzuz127vynyb2m681r8w20071129NM0000000000000000000005\",\"displayLabel\":\"Report item\",\"description\":\"\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.geodashboard.report.PairViewQueryDTO', {
  Extends : 'com.runwaysdk.business.ViewQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.report.PairView'
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
