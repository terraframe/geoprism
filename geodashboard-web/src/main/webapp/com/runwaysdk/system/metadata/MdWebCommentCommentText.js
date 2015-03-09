Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdWebCommentCommentText', {
  Extends : 'com.runwaysdk.business.LocalStructDTO',
  Constants : 
  {
    DEFAULTLOCALE : 'defaultLocale',
    ID : 'id',
    KEYNAME : 'keyName',
    SITEMASTER : 'siteMaster',
    CLASS : 'com.runwaysdk.system.metadata.MdWebCommentCommentText'
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
        var json = '{\"id\":\"6td4n8s3ckagqo3ib8c4h6d8pmht20t3xt0vdqqydrwlpyfum9s1a92ty1xkwz56\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.system.metadata.MdWebCommentCommentText\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0dtfqb82x5irqygxkxmbkb7ucu8okcry00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"6td4n8s3ckagqo3ib8c4h6d8pmht20t3xt0vdqqydrwlpyfum9s1a92ty1xkwz56\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeTextDTO\",\"attributeMdDTO\":{\"id\":\"0ng65fuurni6vkcbhfxvf6dkv5h7bmoj00000000000000000000000000000139\",\"system\":false,\"displayLabel\":\"Default Locale\",\"description\":\"Default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeText\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"1u13ih33mclp3npeq420fikkup9ir1zw00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"1sl3daffqmzufuau9xlnqispyqg13vtb00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"xt0vdqqydrwlpyfum9s1a92ty1xkwz560000000000000000000MdLocalStruct\",\"displayLabel\":\"Comment Text\",\"description\":\"The text data for the comment.\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdWebCommentCommentTextQueryDTO', {
  Extends : 'com.runwaysdk.business.LocalStructQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdWebCommentCommentText'
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
