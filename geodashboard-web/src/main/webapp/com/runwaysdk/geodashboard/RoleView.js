Mojo.Meta.newClass('com.runwaysdk.geodashboard.RoleView', {
  Extends : 'com.runwaysdk.business.ViewDTO',
  Constants : 
  {
    ASSIGNED : 'assigned',
    DISPLAYLABEL : 'displayLabel',
    GROUPNAME : 'groupName',
    ID : 'id',
    ROLEID : 'roleId',
    CLASS : 'com.runwaysdk.geodashboard.RoleView'
  },
  Instance: 
  {
    getAssigned : function()
    {
      return this.getAttributeDTO('assigned').getValue();
    },
    setAssigned : function(value)
    {
      var attributeDTO = this.getAttributeDTO('assigned');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isAssignedReadable : function()
    {
      return this.getAttributeDTO('assigned').isReadable();
    },
    isAssignedWritable : function()
    {
      return this.getAttributeDTO('assigned').isWritable();
    },
    isAssignedModified : function()
    {
      return this.getAttributeDTO('assigned').isModified();
    },
    getAssignedMd : function()
    {
      return this.getAttributeDTO('assigned').getAttributeMdDTO();
    },
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
    getGroupName : function()
    {
      return this.getAttributeDTO('groupName').getValue();
    },
    setGroupName : function(value)
    {
      var attributeDTO = this.getAttributeDTO('groupName');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isGroupNameReadable : function()
    {
      return this.getAttributeDTO('groupName').isReadable();
    },
    isGroupNameWritable : function()
    {
      return this.getAttributeDTO('groupName').isWritable();
    },
    isGroupNameModified : function()
    {
      return this.getAttributeDTO('groupName').isModified();
    },
    getGroupNameMd : function()
    {
      return this.getAttributeDTO('groupName').getAttributeMdDTO();
    },
    getRoleId : function()
    {
      return this.getAttributeDTO('roleId').getValue();
    },
    setRoleId : function(value)
    {
      var attributeDTO = this.getAttributeDTO('roleId');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isRoleIdReadable : function()
    {
      return this.getAttributeDTO('roleId').isReadable();
    },
    isRoleIdWritable : function()
    {
      return this.getAttributeDTO('roleId').isWritable();
    },
    isRoleIdModified : function()
    {
      return this.getAttributeDTO('roleId').isModified();
    },
    getRoleIdMd : function()
    {
      return this.getAttributeDTO('roleId').getAttributeMdDTO();
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"id\":\"r7m4qupkht38ootmysktpxp93d89gv5jiqve9jfjn2nb6l0rf2rggn9zayi8kwki\",\"readable\":true,\"_type\":\"com.runwaysdk.geodashboard.RoleView\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"i506oafdmnludeyr9bnhc8wbx7orderh00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"r7m4qupkht38ootmysktpxp93d89gv5jiqve9jfjn2nb6l0rf2rggn9zayi8kwki\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"groupName\":{\"attributeName\":\"groupName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeTextDTO\",\"attributeMdDTO\":{\"id\":\"ikh9bsvfm7j1a33upxsgeu8b9rafanz600000000000000000000000000000139\",\"system\":false,\"displayLabel\":\"Group name\",\"description\":\"\",\"name\":\"groupName\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeText\",\"modified\":false},\"displayLabel\":{\"attributeName\":\"displayLabel\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeTextDTO\",\"attributeMdDTO\":{\"id\":\"i5yrtfimmbo8wxdi3tj3upss51knal8600000000000000000000000000000139\",\"system\":false,\"displayLabel\":\"Display label\",\"description\":\"\",\"name\":\"displayLabel\",\"immutable\":false,\"required\":false},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeText\",\"modified\":false},\"assigned\":{\"attributeName\":\"assigned\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeBooleanDTO\",\"attributeMdDTO\":{\"id\":\"im0yjfe965n0ufrem26lpguuosr5pyzl00000000000000000000000000000194\",\"system\":false,\"displayLabel\":\"Is assigned\",\"negativeDisplayLabel\":\"\",\"description\":\"Flag denoting if the current user is assigned to the role\",\"name\":\"assigned\",\"immutable\":false,\"required\":false,\"positiveDisplayLabel\":\"\"},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeBoolean\",\"modified\":false},\"roleId\":{\"attributeName\":\"roleId\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"inyossj4loefq73cxxchvanfn5e6kdkr00000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Role id\",\"description\":\"\",\"name\":\"roleId\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.ViewDTO\",\"_toString\":\"New: Role view\",\"_typeMd\":{\"id\":\"iqve9jfjn2nb6l0rf2rggn9zayi8kwki20071129NM0000000000000000000005\",\"displayLabel\":\"Role view\",\"description\":\"\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
    getRoles : function(clientRequest, user)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.RoleView', methodName:'getRoles', declaredTypes: ["com.runwaysdk.geodashboard.GeodashboardUser"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getDashboardRoles : function(clientRequest, user)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.RoleView', methodName:'getDashboardRoles', declaredTypes: ["com.runwaysdk.geodashboard.GeodashboardUser"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getAdminRoles : function(clientRequest, user)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.RoleView', methodName:'getAdminRoles', declaredTypes: ["com.runwaysdk.geodashboard.GeodashboardUser"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    }
  }
});
Mojo.Meta.newClass('com.runwaysdk.geodashboard.RoleViewQueryDTO', {
  Extends : 'com.runwaysdk.business.ViewQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.RoleView'
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
