Mojo.Meta.newClass('com.runwaysdk.geodashboard.localization.LocalizationFacade', {
  Extends : 'com.runwaysdk.business.UtilDTO',
  Constants : 
  {
    ID : 'id',
    CLASS : 'com.runwaysdk.geodashboard.localization.LocalizationFacade'
  },
  Instance: 
  {
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"id\":\"swc2shsu2s3plo2c1ihodnng5x1131t0i32oa66z1542ks6xrvdmpq9o3n0iw2jr\",\"readable\":true,\"_type\":\"com.runwaysdk.geodashboard.localization.LocalizationFacade\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"ipjbxoi02mhn0o1uc1jynkehoks3mr9m00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"swc2shsu2s3plo2c1ihodnng5x1131t0i32oa66z1542ks6xrvdmpq9o3n0iw2jr\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true}},\"dto_type\":\"com.runwaysdk.business.UtilDTO\",\"_toString\":\"New: Localization facade\",\"_typeMd\":{\"id\":\"i32oa66z1542ks6xrvdmpq9o3n0iw2jr20080226NM0000000000000000000003\",\"displayLabel\":\"Localization facade\",\"description\":\"\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
    getFromBundles : function(clientRequest, key)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.localization.LocalizationFacade', methodName:'getFromBundles', declaredTypes: ["java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getConfigurationJSON : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.localization.LocalizationFacade', methodName:'getConfigurationJSON', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getCLDRLocaleName : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.localization.LocalizationFacade', methodName:'getCLDRLocaleName', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getCalendarLocale : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.localization.LocalizationFacade', methodName:'getCalendarLocale', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getJSON : function(clientRequest)
    {
      var metadata = {className:'com.runwaysdk.geodashboard.localization.LocalizationFacade', methodName:'getJSON', declaredTypes: []};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    }
  }
});
