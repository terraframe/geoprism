Mojo.Meta.newClass('com.runwaysdk.geodashboard.report.ReportItemView', {
  Extends : 'com.runwaysdk.business.ViewDTO',
  Constants : 
  {
    DASHBOARDLABEL : 'dashboardLabel',
    ID : 'id',
    REPORTID : 'reportId',
    REPORTLABEL : 'reportLabel',
    REPORTNAME : 'reportName',
    CLASS : 'com.runwaysdk.geodashboard.report.ReportItemView'
  },
  Instance: 
  {
    getDashboardLabel : function()
    {
      return this.getAttributeDTO('dashboardLabel').getValue();
    },
    setDashboardLabel : function(value)
    {
      var attributeDTO = this.getAttributeDTO('dashboardLabel');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isDashboardLabelReadable : function()
    {
      return this.getAttributeDTO('dashboardLabel').isReadable();
    },
    isDashboardLabelWritable : function()
    {
      return this.getAttributeDTO('dashboardLabel').isWritable();
    },
    isDashboardLabelModified : function()
    {
      return this.getAttributeDTO('dashboardLabel').isModified();
    },
    getDashboardLabelMd : function()
    {
      return this.getAttributeDTO('dashboardLabel').getAttributeMdDTO();
    },
    getReportId : function()
    {
      return this.getAttributeDTO('reportId').getValue();
    },
    setReportId : function(value)
    {
      var attributeDTO = this.getAttributeDTO('reportId');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isReportIdReadable : function()
    {
      return this.getAttributeDTO('reportId').isReadable();
    },
    isReportIdWritable : function()
    {
      return this.getAttributeDTO('reportId').isWritable();
    },
    isReportIdModified : function()
    {
      return this.getAttributeDTO('reportId').isModified();
    },
    getReportIdMd : function()
    {
      return this.getAttributeDTO('reportId').getAttributeMdDTO();
    },
    getReportLabel : function()
    {
      return this.getAttributeDTO('reportLabel').getValue();
    },
    setReportLabel : function(value)
    {
      var attributeDTO = this.getAttributeDTO('reportLabel');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isReportLabelReadable : function()
    {
      return this.getAttributeDTO('reportLabel').isReadable();
    },
    isReportLabelWritable : function()
    {
      return this.getAttributeDTO('reportLabel').isWritable();
    },
    isReportLabelModified : function()
    {
      return this.getAttributeDTO('reportLabel').isModified();
    },
    getReportLabelMd : function()
    {
      return this.getAttributeDTO('reportLabel').getAttributeMdDTO();
    },
    getReportName : function()
    {
      return this.getAttributeDTO('reportName').getValue();
    },
    setReportName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('reportName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isReportNameReadable : function()
    {
      return this.getAttributeDTO('reportName').isReadable();
    },
    isReportNameWritable : function()
    {
      return this.getAttributeDTO('reportName').isWritable();
    },
    isReportNameModified : function()
    {
      return this.getAttributeDTO('reportName').isModified();
    },
    getReportNameMd : function()
    {
      return this.getAttributeDTO('reportName').getAttributeMdDTO();
    },
    remove : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItemView', methodName:'remove', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, this, [].splice.call(arguments, 1));
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"id\":\"zg44ke50bewbwu2mcza90hty6ntk9neuib72fnr27t4zu7bol8jtanwbha24r414\",\"readable\":true,\"_type\":\"com.runwaysdk.geodashboard.report.ReportItemView\",\"attributeMap\":{\"reportLabel\":{\"attributeName\":\"reportLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeTextDTO\",\"attributeMdDTO\":{\"id\":\"i9r4wdhgfdlso7wg3cskale3x1jj0enb00000000000000000000000000000139\",\"system\":false,\"displayLabel\":\"Report label\",\"description\":\"\",\"name\":\"reportLabel\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeText\",\"modified\":false},\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i0mzymh7x6uoorxv5j21561lrb0h4qwf00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"zg44ke50bewbwu2mcza90hty6ntk9neuib72fnr27t4zu7bol8jtanwbha24r414\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"reportName\":{\"attributeName\":\"reportName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeTextDTO\",\"attributeMdDTO\":{\"id\":\"i100yt9bhl32kqtogc3ux0cfwpmu4jqk00000000000000000000000000000139\",\"system\":false,\"displayLabel\":\"Report name\",\"description\":\"\",\"name\":\"reportName\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeText\",\"modified\":false},\"reportId\":{\"attributeName\":\"reportId\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeTextDTO\",\"attributeMdDTO\":{\"id\":\"it8te7xlvil6flfe9a11nrdnttc3qfz200000000000000000000000000000139\",\"system\":false,\"displayLabel\":\"Report id\",\"description\":\"\",\"name\":\"reportId\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeText\",\"modified\":false},\"dashboardLabel\":{\"attributeName\":\"dashboardLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeTextDTO\",\"attributeMdDTO\":{\"id\":\"i5rpvlf9rkk5u40ts5zf4zitwm82geoi00000000000000000000000000000139\",\"system\":false,\"displayLabel\":\"Dashboard label\",\"description\":\"\",\"name\":\"dashboardLabel\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeText\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.ViewDTO\",\"_toString\":\"New: Report item\",\"_typeMd\":{\"id\":\"ib72fnr27t4zu7bol8jtanwbha24r41420071129NM0000000000000000000005\",\"displayLabel\":\"Report item\",\"description\":\"\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
    remove : function(clientRequest, id)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.report.ReportItemView', methodName:'remove', declaredTypes: ["java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    }
  }
});
Mojo.Meta.newClass('com.runwaysdk.geodashboard.report.ReportItemViewQueryDTO', {
  Extends : 'com.runwaysdk.business.ViewQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.report.ReportItemView'
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
