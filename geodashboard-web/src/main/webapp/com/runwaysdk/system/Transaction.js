Mojo.Meta.newClass('com.runwaysdk.system.Transaction', {
  Extends : 'com.runwaysdk.business.StructDTO',
  Constants : 
  {
    ACTION : 'action',
    DATAOBJECTID : 'dataObjectID',
    ID : 'id',
    KEYNAME : 'keyName',
    SITEMASTER : 'siteMaster',
    TRANSACTIONID : 'transactionID',
    CLASS : 'com.runwaysdk.system.Transaction'
  },
  Instance: 
  {
    getAction : function()
    {
      return this.getAttributeDTO('action').getValue();
    },
    setAction : function(value)
    {
      var attributeDTO = this.getAttributeDTO('action');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isActionReadable : function()
    {
      return this.getAttributeDTO('action').isReadable();
    },
    isActionWritable : function()
    {
      return this.getAttributeDTO('action').isWritable();
    },
    isActionModified : function()
    {
      return this.getAttributeDTO('action').isModified();
    },
    getActionMd : function()
    {
      return this.getAttributeDTO('action').getAttributeMdDTO();
    },
    getDataObjectID : function()
    {
      return this.getAttributeDTO('dataObjectID').getValue();
    },
    setDataObjectID : function(value)
    {
      var attributeDTO = this.getAttributeDTO('dataObjectID');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isDataObjectIDReadable : function()
    {
      return this.getAttributeDTO('dataObjectID').isReadable();
    },
    isDataObjectIDWritable : function()
    {
      return this.getAttributeDTO('dataObjectID').isWritable();
    },
    isDataObjectIDModified : function()
    {
      return this.getAttributeDTO('dataObjectID').isModified();
    },
    getDataObjectIDMd : function()
    {
      return this.getAttributeDTO('dataObjectID').getAttributeMdDTO();
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
    getTransactionID : function()
    {
      return this.getAttributeDTO('transactionID').getValue();
    },
    setTransactionID : function(value)
    {
      var attributeDTO = this.getAttributeDTO('transactionID');
      attributeDTO.setValue(value);
      this.setModified(true);
    },
    isTransactionIDReadable : function()
    {
      return this.getAttributeDTO('transactionID').isReadable();
    },
    isTransactionIDWritable : function()
    {
      return this.getAttributeDTO('transactionID').isWritable();
    },
    isTransactionIDModified : function()
    {
      return this.getAttributeDTO('transactionID').isModified();
    },
    getTransactionIDMd : function()
    {
      return this.getAttributeDTO('transactionID').getAttributeMdDTO();
    },
    initialize : function(obj)
    {
      if(obj == null)
      {
        var json = '{\"id\":\"sc1ho8anc0wn2yfnnj39s66a0enkamjw00000000000000000000000000000376\",\"readable\":true,\"_type\":\"com.runwaysdk.system.Transaction\",\"attributeMap\":{\"id\":{\"attributeName\":\"id\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000037700000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"ID\",\"description\":\"ID of this object\",\"name\":\"id\",\"immutable\":true,\"required\":true,\"size\":64},\"writable\":true,\"value\":\"sc1ho8anc0wn2yfnnj39s66a0enkamjw00000000000000000000000000000376\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":true},\"keyName\":{\"attributeName\":\"keyName\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"20080227NM000000000000000000003600000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Key Name\",\"description\":\"Key which uniquely identifies each instance of this type heirarchy.\",\"name\":\"keyName\",\"immutable\":false,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"siteMaster\":{\"attributeName\":\"siteMaster\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"JS20070104000000000000000000120900000000000000000000000000000138\",\"system\":true,\"displayLabel\":\"Site Master\",\"description\":\"The name of the site responsible for this object\",\"name\":\"siteMaster\",\"immutable\":true,\"required\":true,\"size\":255},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"action\":{\"attributeName\":\"action\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000038000000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Action\",\"description\":\"The action taken by this DataObject in this transaction (Create, Update, or Delete)\",\"name\":\"action\",\"immutable\":true,\"required\":true,\"size\":6},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false},\"transactionID\":{\"attributeName\":\"transactionID\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeIntegerDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000037800000000000000000000000000000332\",\"system\":false,\"displayLabel\":\"Transaction ID\",\"description\":\"The ID of the transaction this action took place in\",\"name\":\"transactionID\",\"immutable\":true,\"_rejectPositive\":false,\"_rejectZero\":false,\"required\":true,\"_rejectNegative\":true},\"writable\":true,\"value\":null,\"type\":\"com.runwaysdk.system.metadata.MdAttributeInteger\",\"modified\":false},\"dataObjectID\":{\"attributeName\":\"dataObjectID\",\"readable\":true,\"dtoType\":\"com.runwaysdk.transport.attributes.AttributeCharacterDTO\",\"attributeMdDTO\":{\"id\":\"0000000000000000000000000000037900000000000000000000000000000138\",\"system\":false,\"displayLabel\":\"Date Object ID\",\"description\":\"The ID of the DataObject modified in this transaction\",\"name\":\"dataObjectID\",\"immutable\":true,\"required\":true,\"size\":32},\"writable\":true,\"value\":\"\",\"type\":\"com.runwaysdk.system.metadata.MdAttributeCharacter\",\"modified\":false}},\"dto_type\":\"com.runwaysdk.business.StructDTO\",\"_toString\":\"New: Transaction Records\",\"_typeMd\":{\"id\":\"0000000000000000000000000000037600000000000000000000000000000979\",\"displayLabel\":\"Transaction Records\",\"description\":\"Records transactions on a write server so other mirrors can reference it and remain current\"},\"writable\":true,\"newInstance\":true,\"modified\":true}';
        obj = Mojo.Util.getObject(json);
      }
      this.$initialize(obj);
    }
  },
  Static: 
  {
  }
});
Mojo.Meta.newClass('com.runwaysdk.system.TransactionQueryDTO', {
  Extends : 'com.runwaysdk.business.StructQueryDTO',
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.Transaction'
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
