Mojo.Meta.newClass('com.runwaysdk.system.PhoneNumber', {
  Extends : 'com.runwaysdk.business.StructDTO',
  Constants : 
  {
    AREACODE : 'areaCode',
    EXTENSION : 'extension',
    ID : 'id',
    KEYNAME : 'keyName',
    PREFIX : 'prefix',
    SITEMASTER : 'siteMaster',
    SUFFIX : 'suffix',
    CLASS : 'com.runwaysdk.system.PhoneNumber'
  },
  Instance: 
  {
    getAreaCode : function()
    {
      return this.getAttributeDTO('areaCode').getValue();
    },
    setAreaCode : function(value)
    {
      var attributeDTO = this.getAttributeDTO('areaCode');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isAreaCodeReadable : function()
    {
      return this.getAttributeDTO('areaCode').isReadable();
    },
    isAreaCodeWritable : function()
    {
      return this.getAttributeDTO('areaCode').isWritable();
    },
    isAreaCodeModified : function()
    {
      return this.getAttributeDTO('areaCode').isModified();
    },
    getAreaCodeMd : function()
    {
      return this.getAttributeDTO('areaCode').getAttributeMdDTO();
    },
    getExtension : function()
    {
      return this.getAttributeDTO('extension').getValue();
    },
    setExtension : function(value)
    {
      var attributeDTO = this.getAttributeDTO('extension');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isExtensionReadable : function()
    {
      return this.getAttributeDTO('extension').isReadable();
    },
    isExtensionWritable : function()
    {
      return this.getAttributeDTO('extension').isWritable();
    },
    isExtensionModified : function()
    {
      return this.getAttributeDTO('extension').isModified();
    },
    getExtensionMd : function()
    {
      return this.getAttributeDTO('extension').getAttributeMdDTO();
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
    getPrefix : function()
    {
      return this.getAttributeDTO('prefix').getValue();
    },
    setPrefix : function(value)
    {
      var attributeDTO = this.getAttributeDTO('prefix');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPrefixReadable : function()
    {
      return this.getAttributeDTO('prefix').isReadable();
    },
    isPrefixWritable : function()
    {
      return this.getAttributeDTO('prefix').isWritable();
    },
    isPrefixModified : function()
    {
      return this.getAttributeDTO('prefix').isModified();
    },
    getPrefixMd : function()
    {
      return this.getAttributeDTO('prefix').getAttributeMdDTO();
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
    getSuffix : function()
    {
      return this.getAttributeDTO('suffix').getValue();
    },
    setSuffix : function(value)
    {
      var attributeDTO = this.getAttributeDTO('suffix');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isSuffixReadable : function()
    {
      return this.getAttributeDTO('suffix').isReadable();
    },
    isSuffixWritable : function()
    {
      return this.getAttributeDTO('suffix').isWritable();
    },
    isSuffixModified : function()
    {
      return this.getAttributeDTO('suffix').isModified();
    },
    getSuffixMd : function()
    {
      return this.getAttributeDTO('suffix').getAttributeMdDTO();
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"id\":\"1ycuko7jmlfef58m0z69ap63ff27aav300000000000000000000000000000318\",\"readable\":true,\"_type\":\"com.runwaysdk.system.PhoneNumber\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000032600000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"1ycuko7jmlfef58m0z69ap63ff27aav300000000000000000000000000000318\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"extension\":{\"attributeName\":\"extension\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000032800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Extension\",\"description\":\"Phone number extension\",\"name\":\"extension\",\"immutable\":false,\"required\":false,\"size\":8},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080227NM000000000000000000003500000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"JS20070104000000000000000000120600000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"areaCode\":{\"attributeName\":\"areaCode\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000032000000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Area Code\",\"description\":\"Phone number area code\",\"name\":\"areaCode\",\"immutable\":false,\"required\":true,\"size\":3},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"prefix\":{\"attributeName\":\"prefix\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000032200000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"prefix\",\"description\":\"Phone number prefix\",\"name\":\"prefix\",\"immutable\":false,\"required\":true,\"size\":3},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"suffix\":{\"attributeName\":\"suffix\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000032400000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Suffix\",\"description\":\"Phone number suffix\",\"name\":\"suffix\",\"immutable\":false,\"required\":true,\"size\":4},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.StructDTO\",\"_toString\":\"New: Phone Number\",\"_typeMd\":{\"id\":\"0000000000000000000000000000031800000000000000000000000000000979\",\"displayLabel\":\"Phone Number\",\"description\":\"US Phone Number with area code, prefix, and sufix\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.PhoneNumberQueryDTO', {
  Extends : 'com.runwaysdk.business.StructQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.PhoneNumber'
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
