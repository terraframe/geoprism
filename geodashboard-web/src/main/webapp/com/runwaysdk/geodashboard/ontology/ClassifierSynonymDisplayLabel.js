Mojo.Meta.newClass('com.runwaysdk.geodashboard.ontology.ClassifierSynonymDisplayLabel', {
  Extends : 'com.runwaysdk.business.LocalStructDTO',
  Constants : 
  {
    DEFAULTLOCALE : 'defaultLocale',
    ID : 'id',
    KEYNAME : 'keyName',
    SITEMASTER : 'siteMaster',
    CLASS : 'com.runwaysdk.geodashboard.ontology.ClassifierSynonymDisplayLabel'
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
        var json = '{\"id\":\"0nkxax1b7sgix3firejimpmwp78on7vwi856vlpsycqj4xthd0mpoptiqr1q0efx\",\"localizedValue\":\"\",\"readable\":true,\"_type\":\"com.runwaysdk.geodashboard.ontology.ClassifierSynonymDisplayLabel\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"iqn1ppxzulmdqkuvq2qcdelhvg0gj53r00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"0nkxax1b7sgix3firejimpmwp78on7vwi856vlpsycqj4xthd0mpoptiqr1q0efx\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"defaultLocale\":{\"attributeName\":\"defaultLocale\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"iltuyi9j63mwhbk6efwea9nnd0zi7u2q00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Default Locale\",\"description\":\"Default locale\",\"name\":\"defaultLocale\",\"immutable\":false,\"required\":false,\"size\":4000},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i9acu7lb6l1i3a9e6h6mimt7qw56ps1500000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i1qc106cxm2o6bpvrmqluegwbblefbim00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.LocalStructDTO\",\"_toString\":\"\",\"_typeMd\":{\"id\":\"i856vlpsycqj4xthd0mpoptiqr1q0efx0000000000000000000MdLocalStruct\",\"displayLabel\":\"Display Label\",\"description\":\"\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.geodashboard.ontology.ClassifierSynonymDisplayLabelQueryDTO', {
  Extends : 'com.runwaysdk.business.LocalStructQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.ontology.ClassifierSynonymDisplayLabel'
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
