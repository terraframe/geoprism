Mojo.Meta.newClass('com.runwaysdk.system.Address', {
  Extends : 'com.runwaysdk.business.StructDTO',
  Constants : 
  {
    CITY : 'city',
    ID : 'id',
    KEYNAME : 'keyName',
    POSTALCODE : 'postalCode',
    PRIMARYADDRESS : 'primaryAddress',
    SECONDARYADDRESS : 'secondaryAddress',
    SITEMASTER : 'siteMaster',
    ZIPCODE : 'zipCode',
    CLASS : 'com.runwaysdk.system.Address'
  },
  Instance: 
  {
    getCity : function()
    {
      return this.getAttributeDTO('city').getValue();
    },
    setCity : function(value)
    {
      var attributeDTO = this.getAttributeDTO('city');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isCityReadable : function()
    {
      return this.getAttributeDTO('city').isReadable();
    },
    isCityWritable : function()
    {
      return this.getAttributeDTO('city').isWritable();
    },
    isCityModified : function()
    {
      return this.getAttributeDTO('city').isModified();
    },
    getCityMd : function()
    {
      return this.getAttributeDTO('city').getAttributeMdDTO();
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
    getPostalCode : function()
    {
      var attributeDTO = this.getAttributeDTO('postalCode');
      var names = attributeDTO.getEnumNames();
      var enums = [];
      for(var i=0; i<names.length; i++)
      {
        enums.push(Mojo.$.com.runwaysdk.system.AllPostalCodes[names[i]]);
      }
      return enums;
    },
    removePostalCode : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('postalCode');
      attributeDTO.remove(enumValue);
      this.setModified(true);
    },
    clearPostalCode : function()
    {
      var attributeDTO = this.getAttributeDTO('postalCode');
      attributeDTO.clear();
      this.setModified(true);
    },
    addPostalCode : function(enumValue)
    {
      var attributeDTO = this.getAttributeDTO('postalCode');
      attributeDTO.add(enumValue);
      this.setModified(true);
    },
    isPostalCodeReadable : function()
    {
      return this.getAttributeDTO('postalCode').isReadable();
    },
    isPostalCodeWritable : function()
    {
      return this.getAttributeDTO('postalCode').isWritable();
    },
    isPostalCodeModified : function()
    {
      return this.getAttributeDTO('postalCode').isModified();
    },
    getPostalCodeMd : function()
    {
      return this.getAttributeDTO('postalCode').getAttributeMdDTO();
    },
    getPrimaryAddress : function()
    {
      return this.getAttributeDTO('primaryAddress').getValue();
    },
    setPrimaryAddress : function(value)
    {
      var attributeDTO = this.getAttributeDTO('primaryAddress');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isPrimaryAddressReadable : function()
    {
      return this.getAttributeDTO('primaryAddress').isReadable();
    },
    isPrimaryAddressWritable : function()
    {
      return this.getAttributeDTO('primaryAddress').isWritable();
    },
    isPrimaryAddressModified : function()
    {
      return this.getAttributeDTO('primaryAddress').isModified();
    },
    getPrimaryAddressMd : function()
    {
      return this.getAttributeDTO('primaryAddress').getAttributeMdDTO();
    },
    getSecondaryAddress : function()
    {
      return this.getAttributeDTO('secondaryAddress').getValue();
    },
    setSecondaryAddress : function(value)
    {
      var attributeDTO = this.getAttributeDTO('secondaryAddress');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isSecondaryAddressReadable : function()
    {
      return this.getAttributeDTO('secondaryAddress').isReadable();
    },
    isSecondaryAddressWritable : function()
    {
      return this.getAttributeDTO('secondaryAddress').isWritable();
    },
    isSecondaryAddressModified : function()
    {
      return this.getAttributeDTO('secondaryAddress').isModified();
    },
    getSecondaryAddressMd : function()
    {
      return this.getAttributeDTO('secondaryAddress').getAttributeMdDTO();
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
    getZipCode : function()
    {
      return this.getAttributeDTO('zipCode').getValue();
    },
    setZipCode : function(value)
    {
      var attributeDTO = this.getAttributeDTO('zipCode');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isZipCodeReadable : function()
    {
      return this.getAttributeDTO('zipCode').isReadable();
    },
    isZipCodeWritable : function()
    {
      return this.getAttributeDTO('zipCode').isWritable();
    },
    isZipCodeModified : function()
    {
      return this.getAttributeDTO('zipCode').isModified();
    },
    getZipCodeMd : function()
    {
      return this.getAttributeDTO('zipCode').getAttributeMdDTO();
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"id\":\"zgj5k7zjtd8tqojq7xsj1fx77bjvn1kl20080403NM0000000000000000000085\",\"readable\":true,\"_type\":\"com.runwaysdk.system.Address\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080403NM000000000000000000007600000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"zgj5k7zjtd8tqojq7xsj1fx77bjvn1kl20080403NM0000000000000000000085\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"postalCode\":{\"attributeName\":\"postalCode\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeEnumerationDTO\",\"attributeMdDTO\":{\"id\":\"20080403NM000000000000000000008200000000000000000000000000000216\",\"referencedMdEnumeration\":\"com.runwaysdk.system.AllPostalCodes\",\"system\":false,\"displayLabel\":\"Postal Code\",\"description\":\"State of residence\",\"name\":\"postalCode\",\"immutable\":false,\"enumNames\":{\"VT\":\"VERMONT\",\"RI\":\"RHODE_ISLAND\",\"HI\":\"HAWAII\",\"VI\":\"VIRGIN_ISLANDS\",\"MH\":\"MARSHALL_ISLANDS\",\"ME\":\"MAINE\",\"VA\":\"VIRGINIA\",\"MI\":\"MICHIGAN\",\"ID\":\"IDAHO\",\"DE\":\"DELAWARE\",\"IA\":\"IOWA\",\"MD\":\"MARYLAND\",\"MA\":\"MASSACHUSETTS\",\"AS\":\"AMERICAN_SAMOA\",\"AR\":\"ARKANSAS\",\"IL\":\"ILLINOIS\",\"UT\":\"UTAH\",\"IN\":\"INDIANA\",\"MN\":\"MINNESOTA\",\"MP\":\"NORTHERN_MARIANA_ISLANDS\",\"AZ\":\"ARIZONA\",\"MO\":\"MISSOURI\",\"MT\":\"MONTANA\",\"MS\":\"MISSISSIPPI\",\"NH\":\"NEW_HAMPSHIRE\",\"PW\":\"PALAU\",\"NJ\":\"NEW_JERSEY\",\"PR\":\"PUERTO_RICO\",\"NM\":\"NEW_MEXICO\",\"AK\":\"ALASKA\",\"TX\":\"TEXAS\",\"AL\":\"ALABAMA\",\"NC\":\"NORTH_CAROLINA\",\"ND\":\"NORTH_DAKOTA\",\"NE\":\"NEBRASKA\",\"NY\":\"NEW_YORK\",\"GA\":\"GEORGIA\",\"NV\":\"NEVADA\",\"TN\":\"TENNESSEE\",\"CA\":\"CALIFORNIA\",\"OK\":\"OKLAHOMA\",\"OH\":\"OHIO\",\"WY\":\"WYOMING\",\"FM\":\"FEDERATED_STATES_OF_MICRONESIA\",\"FL\":\"FLORIDA\",\"SD\":\"SOUTH_DAKOTA\",\"SC\":\"SOUTH_CAROLINA\",\"CT\":\"CONNECTICUT\",\"WV\":\"WEST_VIRGINIA\",\"DC\":\"DISTRICT_OF_COLUMBIA\",\"KY\":\"KENTUCKY\",\"WI\":\"WISCONSIN\",\"KS\":\"KANSAS\",\"OR\":\"OREGON\",\"LA\":\"LOUISIANA\",\"GU\":\"GUAM\",\"WA\":\"WASHINGTON\",\"CO\":\"COLORADO\",\"PA\":\"PENNSYLVANIA\"},\"_selectMultiple\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"enumNames\":[],\"type\":\"com.runwaysdk.system.metadata.MdAttributeEnumeration\",\"modified\":false},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080403NM000000000000000000007900000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080403NM000000000000000000007700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"secondaryAddress\":{\"attributeName\":\"secondaryAddress\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080403NM000000000000000000008000000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Secondary Address\",\"description\":\"Secondary residencial address\",\"name\":\"secondaryAddress\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"zipCode\":{\"attributeName\":\"zipCode\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080403NM000000000000000000007800000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Zip Code\",\"description\":\"Zip code of residence\",\"name\":\"zipCode\",\"immutable\":false,\"required\":true,\"size\":5},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"primaryAddress\":{\"attributeName\":\"primaryAddress\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080403NM000000000000000000008100000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Primary Address\",\"description\":\"Primary residencial address\",\"name\":\"primaryAddress\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"city\":{\"attributeName\":\"city\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080403NM000000000000000000007500000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"City\",\"description\":\"City of residence\",\"name\":\"city\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.StructDTO\",\"_toString\":\"New: Address\",\"_typeMd\":{\"id\":\"20080403NM000000000000000000008500000000000000000000000000000979\",\"displayLabel\":\"Address\",\"description\":\"Physical address\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.AddressQueryDTO', {
  Extends : 'com.runwaysdk.business.StructQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.Address'
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
