Mojo.Meta.newClass('com.runwaysdk.system.metadata.MdWebGroupController', {
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.metadata.MdWebGroupController'
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
      var listeners = Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners;
      for(var action in listeners)
      {
        listeners[action] = listener;
      }
    },
    setCancelListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['cancel'] = listener;
    },
    removeCancelListener : function()
    {
      Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['cancel'] = null;
    },
    _notifyCancelListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['cancel']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['cancel'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['cancelMap'](clientRequest, params);
      }
    },
    cancel : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.MdWebGroupController.cancel.mojax', clientRequest, params);
    },
    cancelMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.MdWebGroupController.cancel.mojax', clientRequest, paramString);
    },
    setCreateListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['create'] = listener;
    },
    removeCreateListener : function()
    {
      Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['create'] = null;
    },
    _notifyCreateListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['create']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['create'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['createMap'](clientRequest, params);
      }
    },
    create : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.MdWebGroupController.create.mojax', clientRequest, params);
    },
    createMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.MdWebGroupController.create.mojax', clientRequest, paramString);
    },
    setDeleteListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['delete'] = listener;
    },
    removeDeleteListener : function()
    {
      Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['delete'] = null;
    },
    _notifyDeleteListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['delete']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['delete'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['deleteMap'](clientRequest, params);
      }
    },
    delete : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.MdWebGroupController.delete.mojax', clientRequest, params);
    },
    deleteMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.MdWebGroupController.delete.mojax', clientRequest, paramString);
    },
    setEditListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['edit'] = listener;
    },
    removeEditListener : function()
    {
      Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['edit'] = null;
    },
    _notifyEditListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['edit']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['edit'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['editMap'](clientRequest, params);
      }
    },
    edit : function(clientRequest, id)
    {
      var params = {'id':id}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.MdWebGroupController.edit.mojax', clientRequest, params);
    },
    editMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.MdWebGroupController.edit.mojax', clientRequest, paramString);
    },
    setNewInstanceListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['newInstance'] = listener;
    },
    removeNewInstanceListener : function()
    {
      Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['newInstance'] = null;
    },
    _notifyNewInstanceListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['newInstance']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['newInstance'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['newInstanceMap'](clientRequest, params);
      }
    },
    newInstance : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.MdWebGroupController.newInstance.mojax', clientRequest, params);
    },
    newInstanceMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.MdWebGroupController.newInstance.mojax', clientRequest, paramString);
    },
    setUpdateListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['update'] = listener;
    },
    removeUpdateListener : function()
    {
      Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['update'] = null;
    },
    _notifyUpdateListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['update']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['update'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['updateMap'](clientRequest, params);
      }
    },
    update : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.MdWebGroupController.update.mojax', clientRequest, params);
    },
    updateMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.MdWebGroupController.update.mojax', clientRequest, paramString);
    },
    setViewListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['view'] = listener;
    },
    removeViewListener : function()
    {
      Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['view'] = null;
    },
    _notifyViewListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['view']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['view'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewMap'](clientRequest, params);
      }
    },
    view : function(clientRequest, id)
    {
      var params = {'id':id}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.MdWebGroupController.view.mojax', clientRequest, params);
    },
    viewMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.MdWebGroupController.view.mojax', clientRequest, paramString);
    },
    setViewAllListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['viewAll'] = listener;
    },
    removeViewAllListener : function()
    {
      Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['viewAll'] = null;
    },
    _notifyViewAllListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['viewAll']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['viewAll'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewAllMap'](clientRequest, params);
      }
    },
    viewAll : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.MdWebGroupController.viewAll.mojax', clientRequest, params);
    },
    viewAllMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.MdWebGroupController.viewAll.mojax', clientRequest, paramString);
    },
    setViewPageListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['viewPage'] = listener;
    },
    removeViewPageListener : function()
    {
      Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['viewPage'] = null;
    },
    _notifyViewPageListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['viewPage']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.metadata.MdWebGroupController._listeners['viewPage'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewPageMap'](clientRequest, params);
      }
    },
    viewPage : function(clientRequest, sortAttribute, isAscending, pageSize, pageNumber)
    {
      var params = {'sortAttribute':sortAttribute,'isAscending':isAscending,'pageSize':pageSize,'pageNumber':pageNumber}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.MdWebGroupController.viewPage.mojax', clientRequest, params);
    },
    viewPageMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.metadata.MdWebGroupController.viewPage.mojax', clientRequest, paramString);
    }
  }
});
