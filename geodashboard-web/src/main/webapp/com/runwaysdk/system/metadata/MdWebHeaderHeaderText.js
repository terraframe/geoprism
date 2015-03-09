Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdWebHeaderHeaderText', {
  Extends : 'com.runwaysdk.business.LocalStructDTO',
  Constants : 
  {
    DEFAULTLOCALE : 'defaultLocale',
    ID : 'id',
    KEYNAME : 'keyName',
    SITEMASTER : 'siteMaster',
    CLASS : 'com.runwaysdk.system.metadata.MdWebHeaderHeaderText'
  },
  Instance: 
  {
    getDefaultLocale : function()
    {
      return this.getAttributeDTO('defaultLocale').getValue();
    },
    setDefaultLocale : function(value)
    {
      var attributeDTO = this.getAttributeDTO('defaultLocale');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isDefaultLocaleReadable : function()
    {
      return this.getAttributeDTO('defaultLocale').isReadable();
    },
    isDefaultLocaleWritable : function()
    {
      return this.getAttributeDTO('defaultLocale').isWritable();
    },
    isDefaultLocaleModified : function()
    {
      return this.getAttributeDTO('defaultLocale').isModified();
    },
    getDefaultLocaleMd : function()
    {
      return this.getAttributeDTO('defaultLocale').getAttributeMdDTO();
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
        var json = '{\"id\":\"xh36jboexqsjqd2866pbu2q1hf3xljur7ghgjxwrdkz9m3kwz9bcw2d3ydxrmcf9\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MdWebHeaderHeaderText\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"70scuy1scrtx0ei5etozup15krffu2y000000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"xh36jboexqsjqd2866pbu2q1hf3xljur7ghgjxwrdkz9m3kwz9bcw2d3ydxrmcf9\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"r5xgf1ccjw6hx36dkk0f3ixzvli7l5nh00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Default Locale\",\"description\":\"Default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":4000},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"743xlrc2qbj7vu6oyaux4taixm7hq4tl00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0639f0bmeoia2ezq8wt9pvjd2w6eywlo00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"7ghgjxwrdkz9m3kwz9bcw2d3ydxrmcf90000000000000000000MdLocalStruct\",\"displayLabel\":\"Header Text\",\"description\":\"The text data for the header.\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdWebHeaderHeaderTextQueryDTO', {
  Extends : 'com.runwaysdk.business.LocalStructQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdWebHeaderHeaderText'
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
