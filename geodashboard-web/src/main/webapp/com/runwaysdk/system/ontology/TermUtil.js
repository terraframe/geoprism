Mojo.Meta.newClass('com.runwaysdk.system.ontology.TermUtil', {
  Extends : 'com.runwaysdk.business.UtilDTO',
  Constants : 
  {
    ID : 'id',
    CLASS : 'com.runwaysdk.system.ontology.TermUtil'
  },
  Instance: 
  {
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"id\":\"74myx2rtom6jovue1a7mcya0grkx8bmg8bbiw3k7jp50losl7wfjmyoi7obo4e5p\",\"readable\":true,\"_type\":\"com.runwaysdk.system.ontology.TermUtil\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"r76uo7biair2jwqr9653wqbqfma6o1ep00000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":false,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"74myx2rtom6jovue1a7mcya0grkx8bmg8bbiw3k7jp50losl7wfjmyoi7obo4e5p\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true}},\"dto_type\":\"com.runwaysdk.business.UtilDTO\",\"_toString\":\"New: Term Util\",\"_typeMd\":{\"id\":\"8bbiw3k7jp50losl7wfjmyoi7obo4e5p20080226NM0000000000000000000003\",\"displayLabel\":\"Term Util\",\"description\":\"\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
    addAndRemoveLink : function(clientRequest, childId, oldParentId, oldRelType, newParentId, newRelType)
    {
      var metadata = {className:'com.runwaysdk.system.ontology.TermUtil', methodName:'addAndRemoveLink', declaredTypes: ["java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    removeLink : function(clientRequest, childId, parentId, relationshipType)
    {
      var metadata = {className:'com.runwaysdk.system.ontology.TermUtil', methodName:'removeLink', declaredTypes: ["java.lang.String", "java.lang.String", "java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getDirectDescendants : function(clientRequest, termId, relationshipType)
    {
      var metadata = {className:'com.runwaysdk.system.ontology.TermUtil', methodName:'getDirectDescendants', declaredTypes: ["java.lang.String", "[Ljava.lang.String;"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    exportTerm : function(clientRequest, outputStream, parentId, exportParent, format)
    {
      var metadata = {className:'com.runwaysdk.system.ontology.TermUtil', methodName:'exportTerm', declaredTypes: ["java.io.OutputStream", "java.lang.String", "java.lang.Boolean", "com.runwaysdk.system.ontology.io.TermFileFormat"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getAllAncestors : function(clientRequest, termId, relationshipType)
    {
      var metadata = {className:'com.runwaysdk.system.ontology.TermUtil', methodName:'getAllAncestors', declaredTypes: ["java.lang.String", "[Ljava.lang.String;"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getAllDescendants : function(clientRequest, termId, relationshipType)
    {
      var metadata = {className:'com.runwaysdk.system.ontology.TermUtil', methodName:'getAllDescendants', declaredTypes: ["java.lang.String", "[Ljava.lang.String;"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    addLink : function(clientRequest, childId, parentId, relationshipType)
    {
      var metadata = {className:'com.runwaysdk.system.ontology.TermUtil', methodName:'addLink', declaredTypes: ["java.lang.String", "java.lang.String", "java.lang.String"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    },
    getDirectAncestors : function(clientRequest, termId, relationshipType)
    {
      var metadata = {className:'com.runwaysdk.system.ontology.TermUtil', methodName:'getDirectAncestors', declaredTypes: ["java.lang.String", "[Ljava.lang.String;"]};
      Mojo.$.com.runwaysdk.Facade.invokeMethod(clientRequest, metadata, null, [].splice.call(arguments, 1));
    }
  }
});
