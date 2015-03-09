Mojo.Meta.newClass('com.runwaysdk.system.gis.geo.GeoEntityView', {
  Extends : 'com.runwaysdk.business.ViewDTO',
  Constants : 
  {
    CANCREATECHILDREN : 'canCreateChildren',
    GEOENTITYDISPLAYLABEL : 'geoEntityDisplayLabel',
    GEOENTITYID : 'geoEntityId',
    ID : 'id',
    RELATIONSHIPID : 'relationshipId',
    RELATIONSHIPTYPE : 'relationshipType',
    UNIVERSALDISPLAYLABEL : 'universalDisplayLabel',
    CLASS : 'com.runwaysdk.system.gis.geo.GeoEntityView'
  },
  Instance: 
  {
    getCanCreateChildren : function()
    {
      return this.getAttributeDTO('canCreateChildren').getValue();
    },
    setCanCreateChildren : function(value)
    {
      var attributeDTO = this.getAttributeDTO('canCreateChildren');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isCanCreateChildrenReadable : function()
    {
      return this.getAttributeDTO('canCreateChildren').isReadable();
    },
    isCanCreateChildrenWritable : function()
    {
      return this.getAttributeDTO('canCreateChildren').isWritable();
    },
    isCanCreateChildrenModified : function()
    {
      return this.getAttributeDTO('canCreateChildren').isModified();
    },
    getCanCreateChildrenMd : function()
    {
      return this.getAttributeDTO('canCreateChildren').getAttributeMdDTO();
    },
    getGeoEntityDisplayLabel : function()
    {
      return this.getAttributeDTO('geoEntityDisplayLabel').getValue();
    },
    setGeoEntityDisplayLabel : function(value)
    {
      var attributeDTO = this.getAttributeDTO('geoEntityDisplayLabel');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isGeoEntityDisplayLabelReadable : function()
    {
      return this.getAttributeDTO('geoEntityDisplayLabel').isReadable();
    },
    isGeoEntityDisplayLabelWritable : function()
    {
      return this.getAttributeDTO('geoEntityDisplayLabel').isWritable();
    },
    isGeoEntityDisplayLabelModified : function()
    {
      return this.getAttributeDTO('geoEntityDisplayLabel').isModified();
    },
    getGeoEntityDisplayLabelMd : function()
    {
      return this.getAttributeDTO('geoEntityDisplayLabel').getAttributeMdDTO();
    },
    getGeoEntityId : function()
    {
      return this.getAttributeDTO('geoEntityId').getValue();
    },
    setGeoEntityId : function(value)
    {
      var attributeDTO = this.getAttributeDTO('geoEntityId');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isGeoEntityIdReadable : function()
    {
      return this.getAttributeDTO('geoEntityId').isReadable();
    },
    isGeoEntityIdWritable : function()
    {
      return this.getAttributeDTO('geoEntityId').isWritable();
    },
    isGeoEntityIdModified : function()
    {
      return this.getAttributeDTO('geoEntityId').isModified();
    },
    getGeoEntityIdMd : function()
    {
      return this.getAttributeDTO('geoEntityId').getAttributeMdDTO();
    },
    getRelationshipId : function()
    {
      return this.getAttributeDTO('relationshipId').getValue();
    },
    setRelationshipId : function(value)
    {
      var attributeDTO = this.getAttributeDTO('relationshipId');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isRelationshipIdReadable : function()
    {
      return this.getAttributeDTO('relationshipId').isReadable();
    },
    isRelationshipIdWritable : function()
    {
      return this.getAttributeDTO('relationshipId').isWritable();
    },
    isRelationshipIdModified : function()
    {
      return this.getAttributeDTO('relationshipId').isModified();
    },
    getRelationshipIdMd : function()
    {
      return this.getAttributeDTO('relationshipId').getAttributeMdDTO();
    },
    getRelationshipType : function()
    {
      return this.getAttributeDTO('relationshipType').getValue();
    },
    setRelationshipType : function(value)
    {
      var attributeDTO = this.getAttributeDTO('relationshipType');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isRelationshipTypeReadable : function()
    {
      return this.getAttributeDTO('relationshipType').isReadable();
    },
    isRelationshipTypeWritable : function()
    {
      return this.getAttributeDTO('relationshipType').isWritable();
    },
    isRelationshipTypeModified : function()
    {
      return this.getAttributeDTO('relationshipType').isModified();
    },
    getRelationshipTypeMd : function()
    {
      return this.getAttributeDTO('relationshipType').getAttributeMdDTO();
    },
    getUniversalDisplayLabel : function()
    {
      return this.getAttributeDTO('universalDisplayLabel').getValue();
    },
    setUniversalDisplayLabel : function(value)
    {
      var attributeDTO = this.getAttributeDTO('universalDisplayLabel');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isUniversalDisplayLabelReadable : function()
    {
      return this.getAttributeDTO('universalDisplayLabel').isReadable();
    },
    isUniversalDisplayLabelWritable : function()
    {
      return this.getAttributeDTO('universalDisplayLabel').isWritable();
    },
    isUniversalDisplayLabelModified : function()
    {
      return this.getAttributeDTO('universalDisplayLabel').isModified();
    },
    getUniversalDisplayLabelMd : function()
    {
      return this.getAttributeDTO('universalDisplayLabel').getAttributeMdDTO();
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"id\":\"z9vzvcfp7m7m8apmeizikbqwoom5084mi0odsvngntgx0aw2iljmb3q6gqaw4fie\",\"readable\":true,\"_type\":\"com.runwaysdk.system.gis.geo.GeoEntityView\",\"attributeMap\":{\"universalDisplayLabel\":{\"attributeName\":\"universalDisplayLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"imu3ng3t4iu77w2pvwfomho6v7ufgd7d00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"universalDisplayLabel\",\"description\":\"\",\"name\":\"universalDisplayLabel\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i1vvgwq7kwwzmn8covipn1xp5h9h95fs00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"z9vzvcfp7m7m8apmeizikbqwoom5084mi0odsvngntgx0aw2iljmb3q6gqaw4fie\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"relationshipType\":{\"attributeName\":\"relationshipType\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i2rsxwc2kou42gcnazfa91acilo57w0v00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"relationshipType\",\"description\":\"\",\"name\":\"relationshipType\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"relationshipId\":{\"attributeName\":\"relationshipId\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i3925d5gwu4d1tzbiba91rltte76cn2d00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"relationshipId\",\"description\":\"\",\"name\":\"relationshipId\",\"immutable\":false,\"required\":false,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"canCreateChildren\":{\"attributeName\":\"canCreateChildren\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"i4vi9famynkcjs506xsm77tvr4lrpmbe00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"canCreateChildren\",\"negativeDisplayLabel\":\"\",\"description\":\"\",\"name\":\"canCreateChildren\",\"immutable\":false,\"required\":true,\"positiveDisplayLabel\":\"\"},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"geoEntityId\":{\"attributeName\":\"geoEntityId\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"imhvzg8qi7tf7eywpdiaypwg39gyzr0j00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"geoEntityId\",\"description\":\"\",\"name\":\"geoEntityId\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"geoEntityDisplayLabel\":{\"attributeName\":\"geoEntityDisplayLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"im9nge2sfpm1mablh31dtxflxlf62v1r00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"geoEntityDisplayLabel\",\"description\":\"\",\"name\":\"geoEntityDisplayLabel\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.ViewDTO\",\"_toString\":\"New: GeoEntity View\",\"_typeMd\":{\"id\":\"i0odsvngntgx0aw2iljmb3q6gqaw4fie20071129NM0000000000000000000005\",\"displayLabel\":\"GeoEntity View\",\"description\":\"\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.gis.geo.GeoEntityViewQueryDTO', {
  Extends : 'com.runwaysdk.business.ViewQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.gis.geo.GeoEntityView'
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
