Mojo.Meta.newClass('com.runwaysdk.geodashboard.databrowser.DataBrowserUtil', {
  Extends : 'com.runwaysdk.business.UtilDTO',
  Constants : 
  {
    ID : 'id',
    CLASS : 'com.runwaysdk.geodashboard.databrowser.DataBrowserUtil'
  },
  Instance: 
  {
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"id\":\"seux768xp48x679ht0xnnz9k7k5zkkgwi5dfzuol7gc4agjkql6krkysvtlqhkp0\",\"readable\":true,\"_type\":\"com.runwaysdk.geodashboard.databrowser.DataBrowserUtil\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"icc2v44g9f72qfp1xipq7usktia5oz6m00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"seux768xp48x679ht0xnnz9k7k5zkkgwi5dfzuol7gc4agjkql6krkysvtlqhkp0\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true}},\"dto_type\":\"com.runwaysdk.business.UtilDTO\",\"_toString\":\"New: \",\"_typeMd\":{\"id\":\"i5dfzuol7gc4agjkql6krkysvtlqhkp020080226NM0000000000000000000003\",\"displayLabel\":\"\",\"description\":\"\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
    getDefaultTypes : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.databrowser.DataBrowserUtil', methodName:'getDefaultTypes', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getTypes : function(clientRequest, packages, types)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.databrowser.DataBrowserUtil', methodName:'getTypes', declaredTypes: ["[Ljava.lang.String;", "[Ljava.lang.String;"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    }
  }
});
