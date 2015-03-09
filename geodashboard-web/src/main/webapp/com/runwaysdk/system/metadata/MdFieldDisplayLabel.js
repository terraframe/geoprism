Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdFieldDisplayLabel', {
  Extends : 'com.runwaysdk.business.LocalStructDTO',
  Constants : 
  {
    DEFAULTLOCALE : 'defaultLocale',
    ID : 'id',
    KEYNAME : 'keyName',
    SITEMASTER : 'siteMaster',
    CLASS : 'com.runwaysdk.system.metadata.MdFieldDisplayLabel'
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
        var json = '{\"id\":\"qpwdyecdukoy5e8l7nyqlyzst5g1bhys8ual4o5g20fv0rl7dqgfe54tghwhmx8j\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MdFieldDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"8l0caneb4yc0jcp4afyqxxqguez7lrrn00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"qpwdyecdukoy5e8l7nyqlyzst5g1bhys8ual4o5g20fv0rl7dqgfe54tghwhmx8j\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"1xrbu9pfblsbac5ch4bnsr46hn7perqb00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Default Locale\",\"description\":\"Default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":4000},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"xrqfoimyo52v3154hyn09bjg2cqgc2d000000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0lj953aur2l7u5hpkvhzk8z12pf080jc00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"8ual4o5g20fv0rl7dqgfe54tghwhmx8j0000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"The display label of the form\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdFieldDisplayLabelQueryDTO', {
  Extends : 'com.runwaysdk.business.LocalStructQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdFieldDisplayLabel'
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
