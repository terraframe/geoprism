Mojo.Meta.newClass('com.runwaysdk.system.gis.geo.UniversalController', {
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.gis.geo.UniversalController'
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
    _listeners : {'cancel':null,'create':null,'delete':null,'edit':null,'getDirectDescendants':null,'newInstance':null,'update':null,'view':null,'viewAll':null,'viewCreate':null,'viewPage':null,'viewUpdate':null},
    setListener : function(listener)
    {
      var listeners = Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners;
      for(var action in listeners)
      {
        listeners[action] = listener;
      }
    },
    setCancelListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['cancel'] = listener;
    },
    removeCancelListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['cancel'] = null;
    },
    _notifyCancelListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['cancel']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['cancel'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['cancelMap'](clientRequest, params);
      }
    },
    cancel : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.cancel.mojax', clientRequest, params);
    },
    cancelMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.cancel.mojax', clientRequest, paramString);
    },
    setCreateListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['create'] = listener;
    },
    removeCreateListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['create'] = null;
    },
    _notifyCreateListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['create']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['create'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['createMap'](clientRequest, params);
      }
    },
    create : function(clientRequest, dto, parentId, relationshipType)
    {
      var params = {'dto':dto,'parentId':parentId,'relationshipType':relationshipType}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.create.mojax', clientRequest, params);
    },
    createMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.create.mojax', clientRequest, paramString);
    },
    setDeleteListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['delete'] = listener;
    },
    removeDeleteListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['delete'] = null;
    },
    _notifyDeleteListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['delete']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['delete'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['deleteMap'](clientRequest, params);
      }
    },
    delete : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.delete.mojax', clientRequest, params);
    },
    deleteMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.delete.mojax', clientRequest, paramString);
    },
    setEditListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['edit'] = listener;
    },
    removeEditListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['edit'] = null;
    },
    _notifyEditListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['edit']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['edit'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['editMap'](clientRequest, params);
      }
    },
    edit : function(clientRequest, id)
    {
      var params = {'id':id}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.edit.mojax', clientRequest, params);
    },
    editMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.edit.mojax', clientRequest, paramString);
    },
    setGetDirectDescendantsListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['getDirectDescendants'] = listener;
    },
    removeGetDirectDescendantsListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['getDirectDescendants'] = null;
    },
    _notifyGetDirectDescendantsListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['getDirectDescendants']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['getDirectDescendants'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['getDirectDescendantsMap'](clientRequest, params);
      }
    },
    getDirectDescendants : function(clientRequest, parentId, relationshipTypes, pageNum, pageSize)
    {
      var params = {'parentId':parentId,'relationshipTypes':relationshipTypes,'pageNum':pageNum,'pageSize':pageSize}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.getDirectDescendants.mojax', clientRequest, params);
    },
    getDirectDescendantsMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.getDirectDescendants.mojax', clientRequest, paramString);
    },
    setNewInstanceListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['newInstance'] = listener;
    },
    removeNewInstanceListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['newInstance'] = null;
    },
    _notifyNewInstanceListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['newInstance']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['newInstance'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['newInstanceMap'](clientRequest, params);
      }
    },
    newInstance : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.newInstance.mojax', clientRequest, params);
    },
    newInstanceMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.newInstance.mojax', clientRequest, paramString);
    },
    setUpdateListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['update'] = listener;
    },
    removeUpdateListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['update'] = null;
    },
    _notifyUpdateListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['update']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['update'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['updateMap'](clientRequest, params);
      }
    },
    update : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.update.mojax', clientRequest, params);
    },
    updateMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.update.mojax', clientRequest, paramString);
    },
    setViewListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['view'] = listener;
    },
    removeViewListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['view'] = null;
    },
    _notifyViewListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['view']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['view'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewMap'](clientRequest, params);
      }
    },
    view : function(clientRequest, id)
    {
      var params = {'id':id}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.view.mojax', clientRequest, params);
    },
    viewMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.view.mojax', clientRequest, paramString);
    },
    setViewAllListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['viewAll'] = listener;
    },
    removeViewAllListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['viewAll'] = null;
    },
    _notifyViewAllListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['viewAll']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['viewAll'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewAllMap'](clientRequest, params);
      }
    },
    viewAll : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.viewAll.mojax', clientRequest, params);
    },
    viewAllMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.viewAll.mojax', clientRequest, paramString);
    },
    setViewCreateListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['viewCreate'] = listener;
    },
    removeViewCreateListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['viewCreate'] = null;
    },
    _notifyViewCreateListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['viewCreate']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['viewCreate'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewCreateMap'](clientRequest, params);
      }
    },
    viewCreate : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.viewCreate.mojax', clientRequest, params);
    },
    viewCreateMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.viewCreate.mojax', clientRequest, paramString);
    },
    setViewPageListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['viewPage'] = listener;
    },
    removeViewPageListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['viewPage'] = null;
    },
    _notifyViewPageListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['viewPage']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['viewPage'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewPageMap'](clientRequest, params);
      }
    },
    viewPage : function(clientRequest, sortAttribute, isAscending, pageSize, pageNumber)
    {
      var params = {'sortAttribute':sortAttribute,'isAscending':isAscending,'pageSize':pageSize,'pageNumber':pageNumber}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.viewPage.mojax', clientRequest, params);
    },
    viewPageMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.viewPage.mojax', clientRequest, paramString);
    },
    setViewUpdateListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['viewUpdate'] = listener;
    },
    removeViewUpdateListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['viewUpdate'] = null;
    },
    _notifyViewUpdateListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['viewUpdate']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.UniversalController._listeners['viewUpdate'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewUpdateMap'](clientRequest, params);
      }
    },
    viewUpdate : function(clientRequest, id)
    {
      var params = {'id':id}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.viewUpdate.mojax', clientRequest, params);
    },
    viewUpdateMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.UniversalController.viewUpdate.mojax', clientRequest, paramString);
    }
  }
});
