Mojo.Meta.newClass('com.runwaysdk.geodashboard.databrowser.MetadataType', {
  Extends : 'com.runwaysdk.business.ViewDTO',
  Constants : 
  {
    DISPLAYLABEL : 'displayLabel',
    ID : 'id',
    PARENTTYPEID : 'parentTypeId',
    TYPEID : 'typeId',
    TYPENAME : 'typeName',
    TYPEPACKAGE : 'typePackage',
    CLASS : 'com.runwaysdk.geodashboard.databrowser.MetadataType'
  },
  Instance: 
  {
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
    getParentTypeId : function()
    {
      return this.getAttributeDTO('parentTypeId').getValue();
    },
    setParentTypeId : function(value)
    {
      var attributeDTO = this.getAttributeDTO('parentTypeId');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isParentTypeIdReadable : function()
    {
      return this.getAttributeDTO('parentTypeId').isReadable();
    },
    isParentTypeIdWritable : function()
    {
      return this.getAttributeDTO('parentTypeId').isWritable();
    },
    isParentTypeIdModified : function()
    {
      return this.getAttributeDTO('parentTypeId').isModified();
    },
    getParentTypeIdMd : function()
    {
      return this.getAttributeDTO('parentTypeId').getAttributeMdDTO();
    },
    getTypeId : function()
    {
      return this.getAttributeDTO('typeId').getValue();
    },
    setTypeId : function(value)
    {
      var attributeDTO = this.getAttributeDTO('typeId');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isTypeIdReadable : function()
    {
      return this.getAttributeDTO('typeId').isReadable();
    },
    isTypeIdWritable : function()
    {
      return this.getAttributeDTO('typeId').isWritable();
    },
    isTypeIdModified : function()
    {
      return this.getAttributeDTO('typeId').isModified();
    },
    getTypeIdMd : function()
    {
      return this.getAttributeDTO('typeId').getAttributeMdDTO();
    },
    getTypeName : function()
    {
      return this.getAttributeDTO('typeName').getValue();
    },
    setTypeName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('typeName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isTypeNameReadable : function()
    {
      return this.getAttributeDTO('typeName').isReadable();
    },
    isTypeNameWritable : function()
    {
      return this.getAttributeDTO('typeName').isWritable();
    },
    isTypeNameModified : function()
    {
      return this.getAttributeDTO('typeName').isModified();
    },
    getTypeNameMd : function()
    {
      return this.getAttributeDTO('typeName').getAttributeMdDTO();
    },
    getTypePackage : function()
    {
      return this.getAttributeDTO('typePackage').getValue();
    },
    setTypePackage : function(value)
    {
      var attributeDTO = this.getAttributeDTO('typePackage');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isTypePackageReadable : function()
    {
      return this.getAttributeDTO('typePackage').isReadable();
    },
    isTypePackageWritable : function()
    {
      return this.getAttributeDTO('typePackage').isWritable();
    },
    isTypePackageModified : function()
    {
      return this.getAttributeDTO('typePackage').isModified();
    },
    getTypePackageMd : function()
    {
      return this.getAttributeDTO('typePackage').getAttributeMdDTO();
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"id\":\"24j5aw7tiqearkr9hiyxrppk2eny8zpmik55r2v2bbaah8mpawpvdeff75b8kr82\",\"readable\":true,\"_type\":\"com.runwaysdk.geodashboard.databrowser.MetadataType\",\"attributeMap\":{\"typeName\":{\"attributeName\":\"typeName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"io3valos0zar6y37zozjpb5o02wu8u5p00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"\",\"description\":\"\",\"name\":\"typeName\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"itfn5h58ed5rsz48j3qozmhemiphqdwm00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"24j5aw7tiqearkr9hiyxrppk2eny8zpmik55r2v2bbaah8mpawpvdeff75b8kr82\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"displayLabel\":{\"attributeName\":\"displayLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i1r718u1jzv9vnk1gu4nyx6dybv4audi00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"\",\"description\":\"\",\"name\":\"displayLabel\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"parentTypeId\":{\"attributeName\":\"parentTypeId\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i20r9j2umqr0wyudh639xh9pu9iv4kk900000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"\",\"description\":\"\",\"name\":\"parentTypeId\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"typePackage\":{\"attributeName\":\"typePackage\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i1xt3fvgkpcl6gca0jr0avj7n38371tk00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"\",\"description\":\"\",\"name\":\"typePackage\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"typeId\":{\"attributeName\":\"typeId\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i9bnw256yedv437tuskhrb9wcxmr16e100000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"\",\"description\":\"\",\"name\":\"typeId\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.ViewDTO\",\"_toString\":\"New: \",\"_typeMd\":{\"id\":\"ik55r2v2bbaah8mpawpvdeff75b8kr8220071129NM0000000000000000000005\",\"displayLabel\":\"\",\"description\":\"\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.geodashboard.databrowser.MetadataTypeQueryDTO', {
  Extends : 'com.runwaysdk.business.ViewQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.databrowser.MetadataType'
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
