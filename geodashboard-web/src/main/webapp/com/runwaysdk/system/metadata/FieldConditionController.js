Mojo.Meta.newClass('com.runwaysdk.system.metadata.FieldConditionController', {
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.FieldConditionController'
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
    _listeners : {'cancel':null,'create':null,'delete':null,'edit':null,'update':null,'view':null,'viewAll':null,'viewPage':null},
    setListener : function(listener)
    {
      var listeners = Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners;
      for(var action in listeners)
      {
        listeners[action] = listener;
      }
    },
    setCancelListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['cancel'] = listener;
    },
    removeCancelListener : function()
    {
      Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['cancel'] = null;
    },
    _notifyCancelListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['cancel']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['cancel'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['cancelMap'](clientRequest, params);
      }
    },
    cancel : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.FieldConditionController.cancel.mojax', clientRequest, params);
    },
    cancelMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.FieldConditionController.cancel.mojax', clientRequest, paramString);
    },
    setCreateListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['create'] = listener;
    },
    removeCreateListener : function()
    {
      Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['create'] = null;
    },
    _notifyCreateListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['create']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['create'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['createMap'](clientRequest, params);
      }
    },
    create : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.FieldConditionController.create.mojax', clientRequest, params);
    },
    createMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.FieldConditionController.create.mojax', clientRequest, paramString);
    },
    setDeleteListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['delete'] = listener;
    },
    removeDeleteListener : function()
    {
      Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['delete'] = null;
    },
    _notifyDeleteListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['delete']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['delete'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['deleteMap'](clientRequest, params);
      }
    },
    delete : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.FieldConditionController.delete.mojax', clientRequest, params);
    },
    deleteMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.FieldConditionController.delete.mojax', clientRequest, paramString);
    },
    setEditListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['edit'] = listener;
    },
    removeEditListener : function()
    {
      Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['edit'] = null;
    },
    _notifyEditListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['edit']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['edit'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['editMap'](clientRequest, params);
      }
    },
    edit : function(clientRequest, id)
    {
      var params = {'id':id}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.FieldConditionController.edit.mojax', clientRequest, params);
    },
    editMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.FieldConditionController.edit.mojax', clientRequest, paramString);
    },
    setUpdateListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['update'] = listener;
    },
    removeUpdateListener : function()
    {
      Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['update'] = null;
    },
    _notifyUpdateListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['update']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['update'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['updateMap'](clientRequest, params);
      }
    },
    update : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.FieldConditionController.update.mojax', clientRequest, params);
    },
    updateMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.FieldConditionController.update.mojax', clientRequest, paramString);
    },
    setViewListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['view'] = listener;
    },
    removeViewListener : function()
    {
      Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['view'] = null;
    },
    _notifyViewListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['view']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['view'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewMap'](clientRequest, params);
      }
    },
    view : function(clientRequest, id)
    {
      var params = {'id':id}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.FieldConditionController.view.mojax', clientRequest, params);
    },
    viewMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.FieldConditionController.view.mojax', clientRequest, paramString);
    },
    setViewAllListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['viewAll'] = listener;
    },
    removeViewAllListener : function()
    {
      Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['viewAll'] = null;
    },
    _notifyViewAllListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['viewAll']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['viewAll'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewAllMap'](clientRequest, params);
      }
    },
    viewAll : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.FieldConditionController.viewAll.mojax', clientRequest, params);
    },
    viewAllMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.FieldConditionController.viewAll.mojax', clientRequest, paramString);
    },
    setViewPageListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['viewPage'] = listener;
    },
    removeViewPageListener : function()
    {
      Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['viewPage'] = null;
    },
    _notifyViewPageListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['viewPage']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.metadata.FieldConditionController._listeners['viewPage'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewPageMap'](clientRequest, params);
      }
    },
    viewPage : function(clientRequest, sortAttribute, isAscending, pageSize, pageNumber)
    {
      var params = {'sortAttribute':sortAttribute,'isAscending':isAscending,'pageSize':pageSize,'pageNumber':pageNumber}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.FieldConditionController.viewPage.mojax', clientRequest, params);
    },
    viewPageMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.FieldConditionController.viewPage.mojax', clientRequest, paramString);
    }
  }
});
