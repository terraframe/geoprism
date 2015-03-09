Mojo.Meta.newClass('com.runwaysdk.geodashboard.report.ReportParameter', {
  Extends : 'com.runwaysdk.business.ViewDTO',
  Constants : 
  {
    ID : 'id',
    PARAMETERNAME : 'parameterName',
    PARAMETERVALUE : 'parameterValue',
    CLASS : 'com.runwaysdk.geodashboard.report.ReportParameter'
  },
  Instance: 
  {
    getParameterName : function()
    {
      return this.getAttributeDTO('parameterName').getValue();
    },
    setParameterName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('parameterName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isParameterNameReadable : function()
    {
      return this.getAttributeDTO('parameterName').isReadable();
    },
    isParameterNameWritable : function()
    {
      return this.getAttributeDTO('parameterName').isWritable();
    },
    isParameterNameModified : function()
    {
      return this.getAttributeDTO('parameterName').isModified();
    },
    getParameterNameMd : function()
    {
      return this.getAttributeDTO('parameterName').getAttributeMdDTO();
    },
    getParameterValue : function()
    {
      return this.getAttributeDTO('parameterValue').getValue();
    },
    setParameterValue : function(value)
    {
      var attributeDTO = this.getAttributeDTO('parameterValue');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isParameterValueReadable : function()
    {
      return this.getAttributeDTO('parameterValue').isReadable();
    },
    isParameterValueWritable : function()
    {
      return this.getAttributeDTO('parameterValue').isWritable();
    },
    isParameterValueModified : function()
    {
      return this.getAttributeDTO('parameterValue').isModified();
    },
    getParameterValueMd : function()
    {
      return this.getAttributeDTO('parameterValue').getAttributeMdDTO();
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"id\":\"8cuv8e09xgrba1ak9xss3alnhmzxtmxoisyd7ar3pzyaii1saafbx434fd6x88i5\",\"readable\":true,\"_type\":\"com.runwaysdk.geodashboard.report.ReportParameter\",\"attributeMap\":{\"parameterValue\":{\"attributeName\":\"parameterValue\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeTextDTO\",\"attributeMdDTO\":{\"id\":\"i5c2evemdk1ft1nhv0uenepsmz1ktgr500000000000000000000000000000139\",\"system\":false,\"displayLabel\":\"Parameter value\",\"description\":\"\",\"name\":\"parameterValue\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeText\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ica6jhg38xgc0ixf1yb2ggf4bwvfq2iq00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"8cuv8e09xgrba1ak9xss3alnhmzxtmxoisyd7ar3pzyaii1saafbx434fd6x88i5\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"parameterName\":{\"attributeName\":\"parameterName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeTextDTO\",\"attributeMdDTO\":{\"id\":\"ik84eqmojz5qgfqux7cow74uavit9k2800000000000000000000000000000139\",\"system\":false,\"displayLabel\":\"Parameter name\",\"description\":\"\",\"name\":\"parameterName\",\"immutable\":false,\"required\":true},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeText\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.ViewDTO\",\"_toString\":\"New: Report parameter\",\"_typeMd\":{\"id\":\"isyd7ar3pzyaii1saafbx434fd6x88i520071129NM0000000000000000000005\",\"displayLabel\":\"Report parameter\",\"description\":\"\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.geodashboard.report.ReportParameterQueryDTO', {
  Extends : 'com.runwaysdk.business.ViewQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.report.ReportParameter'
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
