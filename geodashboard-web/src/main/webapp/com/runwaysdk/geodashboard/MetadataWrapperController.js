Mojo.Meta.newClass('com.runwaysdk.geodashboard.MetadataWrapperController', {
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.MetadataWrapperController'
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
    _listeners : {'cancel':null,'create':null,'delete':null,'edit':null,'newInstance':null,'update':null,'view':null,'viewAll':null,'viewPage':null},
    setListener : function(listener)
    {
      var listeners = Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners;
      for(var action in listeners)
      {
        listeners[action] = listener;
      }
    },
    setCancelListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['cancel'] = listener;
    },
    removeCancelListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['cancel'] = null;
    },
    _notifyCancelListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['cancel']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['cancel'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['cancelMap'](clientRequest, params);
      }
    },
    cancel : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.MetadataWrapperController.cancel.mojax', clientRequest, params);
    },
    cancelMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.MetadataWrapperController.cancel.mojax', clientRequest, paramString);
    },
    setCreateListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['create'] = listener;
    },
    removeCreateListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['create'] = null;
    },
    _notifyCreateListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['create']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['create'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['createMap'](clientRequest, params);
      }
    },
    create : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.MetadataWrapperController.create.mojax', clientRequest, params);
    },
    createMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.MetadataWrapperController.create.mojax', clientRequest, paramString);
    },
    setDeleteListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['delete'] = listener;
    },
    removeDeleteListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['delete'] = null;
    },
    _notifyDeleteListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['delete']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['delete'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['deleteMap'](clientRequest, params);
      }
    },
    delete : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.MetadataWrapperController.delete.mojax', clientRequest, params);
    },
    deleteMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.MetadataWrapperController.delete.mojax', clientRequest, paramString);
    },
    setEditListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['edit'] = listener;
    },
    removeEditListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['edit'] = null;
    },
    _notifyEditListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['edit']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['edit'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['editMap'](clientRequest, params);
      }
    },
    edit : function(clientRequest, id)
    {
      var params = {'id':id}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.MetadataWrapperController.edit.mojax', clientRequest, params);
    },
    editMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.MetadataWrapperController.edit.mojax', clientRequest, paramString);
    },
    setNewInstanceListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['newInstance'] = listener;
    },
    removeNewInstanceListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['newInstance'] = null;
    },
    _notifyNewInstanceListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['newInstance']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['newInstance'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['newInstanceMap'](clientRequest, params);
      }
    },
    newInstance : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.MetadataWrapperController.newInstance.mojax', clientRequest, params);
    },
    newInstanceMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.MetadataWrapperController.newInstance.mojax', clientRequest, paramString);
    },
    setUpdateListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['update'] = listener;
    },
    removeUpdateListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['update'] = null;
    },
    _notifyUpdateListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['update']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['update'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['updateMap'](clientRequest, params);
      }
    },
    update : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.MetadataWrapperController.update.mojax', clientRequest, params);
    },
    updateMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.MetadataWrapperController.update.mojax', clientRequest, paramString);
    },
    setViewListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['view'] = listener;
    },
    removeViewListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['view'] = null;
    },
    _notifyViewListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['view']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['view'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewMap'](clientRequest, params);
      }
    },
    view : function(clientRequest, id)
    {
      var params = {'id':id}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.MetadataWrapperController.view.mojax', clientRequest, params);
    },
    viewMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.MetadataWrapperController.view.mojax', clientRequest, paramString);
    },
    setViewAllListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['viewAll'] = listener;
    },
    removeViewAllListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['viewAll'] = null;
    },
    _notifyViewAllListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['viewAll']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['viewAll'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewAllMap'](clientRequest, params);
      }
    },
    viewAll : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.MetadataWrapperController.viewAll.mojax', clientRequest, params);
    },
    viewAllMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.MetadataWrapperController.viewAll.mojax', clientRequest, paramString);
    },
    setViewPageListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['viewPage'] = listener;
    },
    removeViewPageListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['viewPage'] = null;
    },
    _notifyViewPageListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['viewPage']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.MetadataWrapperController._listeners['viewPage'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewPageMap'](clientRequest, params);
      }
    },
    viewPage : function(clientRequest, sortAttribute, isAscending, pageSize, pageNumber)
    {
      var params = {'sortAttribute':sortAttribute,'isAscending':isAscending,'pageSize':pageSize,'pageNumber':pageNumber}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.MetadataWrapperController.viewPage.mojax', clientRequest, params);
    },
    viewPageMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.MetadataWrapperController.viewPage.mojax', clientRequest, paramString);
    }
  }
});
