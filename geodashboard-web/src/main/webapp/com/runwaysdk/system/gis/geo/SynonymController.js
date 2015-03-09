Mojo.Meta.newClass('com.runwaysdk.system.gis.geo.SynonymController', {
  Constants : 
  {
    CLASS : 'com.runwaysdk.system.gis.geo.SynonymController'
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
      var listeners = Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners;
      for(var action in listeners)
      {
        listeners[action] = listener;
      }
    },
    setCancelListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['cancel'] = listener;
    },
    removeCancelListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['cancel'] = null;
    },
    _notifyCancelListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['cancel']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['cancel'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['cancelMap'](clientRequest, params);
      }
    },
    cancel : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.cancel.mojax', clientRequest, params);
    },
    cancelMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.cancel.mojax', clientRequest, paramString);
    },
    setCreateListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['create'] = listener;
    },
    removeCreateListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['create'] = null;
    },
    _notifyCreateListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['create']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['create'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['createMap'](clientRequest, params);
      }
    },
    create : function(clientRequest, dto, geoId)
    {
      var params = {'dto':dto,'geoId':geoId}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.create.mojax', clientRequest, params);
    },
    createMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.create.mojax', clientRequest, paramString);
    },
    setDeleteListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['delete'] = listener;
    },
    removeDeleteListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['delete'] = null;
    },
    _notifyDeleteListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['delete']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['delete'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['deleteMap'](clientRequest, params);
      }
    },
    delete : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.delete.mojax', clientRequest, params);
    },
    deleteMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.delete.mojax', clientRequest, paramString);
    },
    setEditListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['edit'] = listener;
    },
    removeEditListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['edit'] = null;
    },
    _notifyEditListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['edit']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['edit'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['editMap'](clientRequest, params);
      }
    },
    edit : function(clientRequest, id)
    {
      var params = {'id':id}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.edit.mojax', clientRequest, params);
    },
    editMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.edit.mojax', clientRequest, paramString);
    },
    setGetDirectDescendantsListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['getDirectDescendants'] = listener;
    },
    removeGetDirectDescendantsListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['getDirectDescendants'] = null;
    },
    _notifyGetDirectDescendantsListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['getDirectDescendants']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['getDirectDescendants'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['getDirectDescendantsMap'](clientRequest, params);
      }
    },
    getDirectDescendants : function(clientRequest, parentId)
    {
      var params = {'parentId':parentId}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.getDirectDescendants.mojax', clientRequest, params);
    },
    getDirectDescendantsMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.getDirectDescendants.mojax', clientRequest, paramString);
    },
    setNewInstanceListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['newInstance'] = listener;
    },
    removeNewInstanceListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['newInstance'] = null;
    },
    _notifyNewInstanceListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['newInstance']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['newInstance'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['newInstanceMap'](clientRequest, params);
      }
    },
    newInstance : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.newInstance.mojax', clientRequest, params);
    },
    newInstanceMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.newInstance.mojax', clientRequest, paramString);
    },
    setUpdateListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['update'] = listener;
    },
    removeUpdateListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['update'] = null;
    },
    _notifyUpdateListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['update']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['update'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['updateMap'](clientRequest, params);
      }
    },
    update : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.update.mojax', clientRequest, params);
    },
    updateMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.update.mojax', clientRequest, paramString);
    },
    setViewListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['view'] = listener;
    },
    removeViewListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['view'] = null;
    },
    _notifyViewListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['view']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['view'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewMap'](clientRequest, params);
      }
    },
    view : function(clientRequest, id)
    {
      var params = {'id':id}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.view.mojax', clientRequest, params);
    },
    viewMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.view.mojax', clientRequest, paramString);
    },
    setViewAllListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['viewAll'] = listener;
    },
    removeViewAllListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['viewAll'] = null;
    },
    _notifyViewAllListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['viewAll']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['viewAll'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewAllMap'](clientRequest, params);
      }
    },
    viewAll : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.viewAll.mojax', clientRequest, params);
    },
    viewAllMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.viewAll.mojax', clientRequest, paramString);
    },
    setViewCreateListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['viewCreate'] = listener;
    },
    removeViewCreateListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['viewCreate'] = null;
    },
    _notifyViewCreateListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['viewCreate']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['viewCreate'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewCreateMap'](clientRequest, params);
      }
    },
    viewCreate : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.viewCreate.mojax', clientRequest, params);
    },
    viewCreateMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.viewCreate.mojax', clientRequest, paramString);
    },
    setViewPageListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['viewPage'] = listener;
    },
    removeViewPageListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['viewPage'] = null;
    },
    _notifyViewPageListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['viewPage']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['viewPage'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewPageMap'](clientRequest, params);
      }
    },
    viewPage : function(clientRequest, sortAttribute, isAscending, pageSize, pageNumber)
    {
      var params = {'sortAttribute':sortAttribute,'isAscending':isAscending,'pageSize':pageSize,'pageNumber':pageNumber}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.viewPage.mojax', clientRequest, params);
    },
    viewPageMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.viewPage.mojax', clientRequest, paramString);
    },
    setViewUpdateListener : function(listener)
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['viewUpdate'] = listener;
    },
    removeViewUpdateListener : function()
    {
      Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['viewUpdate'] = null;
    },
    _notifyViewUpdateListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['viewUpdate']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.system.gis.geo.SynonymController._listeners['viewUpdate'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewUpdateMap'](clientRequest, params);
      }
    },
    viewUpdate : function(clientRequest, id)
    {
      var params = {'id':id}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.viewUpdate.mojax', clientRequest, params);
    },
    viewUpdateMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.system.gis.geo.SynonymController.viewUpdate.mojax', clientRequest, paramString);
    }
  }
});
