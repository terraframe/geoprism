Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController', {
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController'
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
    _listeners : {'cancel':null,'create':null,'delete':null,'edit':null,'export':null,'newInstance':null,'update':null,'view':null,'viewAll':null,'viewExport':null,'viewPage':null},
    setListener : function(listener)
    {
      var listeners = Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners;
      for(var action in listeners)
      {
        listeners[action] = listener;
      }
    },
    setCancelListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['cancel'] = listener;
    },
    removeCancelListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['cancel'] = null;
    },
    _notifyCancelListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['cancel']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['cancel'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['cancelMap'](clientRequest, params);
      }
    },
    cancel : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.cancel.mojax', clientRequest, params);
    },
    cancelMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.cancel.mojax', clientRequest, paramString);
    },
    setCreateListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['create'] = listener;
    },
    removeCreateListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['create'] = null;
    },
    _notifyCreateListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['create']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['create'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['createMap'](clientRequest, params);
      }
    },
    create : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.create.mojax', clientRequest, params);
    },
    createMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.create.mojax', clientRequest, paramString);
    },
    setDeleteListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['delete'] = listener;
    },
    removeDeleteListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['delete'] = null;
    },
    _notifyDeleteListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['delete']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['delete'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['deleteMap'](clientRequest, params);
      }
    },
    delete : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.delete.mojax', clientRequest, params);
    },
    deleteMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.delete.mojax', clientRequest, paramString);
    },
    setEditListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['edit'] = listener;
    },
    removeEditListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['edit'] = null;
    },
    _notifyEditListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['edit']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['edit'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['editMap'](clientRequest, params);
      }
    },
    edit : function(clientRequest, id)
    {
      var params = {'id':id}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.edit.mojax', clientRequest, params);
    },
    editMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.edit.mojax', clientRequest, paramString);
    },
    setExportListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['export'] = listener;
    },
    removeExportListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['export'] = null;
    },
    _notifyExportListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['export']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['export'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['exportMap'](clientRequest, params);
      }
    },
    export : function(clientRequest, dto, parentTerm, downloadToken)
    {
      var params = {'dto':dto,'parentTerm':parentTerm,'downloadToken':downloadToken}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.export.mojax', clientRequest, params);
    },
    exportMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.export.mojax', clientRequest, paramString);
    },
    setNewInstanceListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['newInstance'] = listener;
    },
    removeNewInstanceListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['newInstance'] = null;
    },
    _notifyNewInstanceListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['newInstance']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['newInstance'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['newInstanceMap'](clientRequest, params);
      }
    },
    newInstance : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.newInstance.mojax', clientRequest, params);
    },
    newInstanceMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.newInstance.mojax', clientRequest, paramString);
    },
    setUpdateListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['update'] = listener;
    },
    removeUpdateListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['update'] = null;
    },
    _notifyUpdateListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['update']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['update'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['updateMap'](clientRequest, params);
      }
    },
    update : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.update.mojax', clientRequest, params);
    },
    updateMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.update.mojax', clientRequest, paramString);
    },
    setViewListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['view'] = listener;
    },
    removeViewListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['view'] = null;
    },
    _notifyViewListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['view']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['view'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewMap'](clientRequest, params);
      }
    },
    view : function(clientRequest, id)
    {
      var params = {'id':id}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.view.mojax', clientRequest, params);
    },
    viewMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.view.mojax', clientRequest, paramString);
    },
    setViewAllListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['viewAll'] = listener;
    },
    removeViewAllListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['viewAll'] = null;
    },
    _notifyViewAllListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['viewAll']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['viewAll'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewAllMap'](clientRequest, params);
      }
    },
    viewAll : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.viewAll.mojax', clientRequest, params);
    },
    viewAllMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.viewAll.mojax', clientRequest, paramString);
    },
    setViewExportListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['viewExport'] = listener;
    },
    removeViewExportListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['viewExport'] = null;
    },
    _notifyViewExportListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['viewExport']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['viewExport'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewExportMap'](clientRequest, params);
      }
    },
    viewExport : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.viewExport.mojax', clientRequest, params);
    },
    viewExportMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.viewExport.mojax', clientRequest, paramString);
    },
    setViewPageListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['viewPage'] = listener;
    },
    removeViewPageListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['viewPage'] = null;
    },
    _notifyViewPageListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['viewPage']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController._listeners['viewPage'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewPageMap'](clientRequest, params);
      }
    },
    viewPage : function(clientRequest, sortAttribute, isAscending, pageSize, pageNumber)
    {
      var params = {'sortAttribute':sortAttribute,'isAscending':isAscending,'pageSize':pageSize,'pageNumber':pageNumber}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.viewPage.mojax', clientRequest, params);
    },
    viewPageMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.GeoEntityExportMenuController.viewPage.mojax', clientRequest, paramString);
    }
  }
});
