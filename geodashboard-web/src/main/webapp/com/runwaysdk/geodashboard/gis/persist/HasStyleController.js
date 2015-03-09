Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.persist.HasStyleController', {
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.gis.persist.HasStyleController'
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
    _listeners : {'cancel':null,'childQuery':null,'create':null,'delete':null,'edit':null,'newInstance':null,'newRelationship':null,'parentQuery':null,'update':null,'view':null,'viewAll':null,'viewPage':null},
    setListener : function(listener)
    {
      var listeners = Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners;
      for(var action in listeners)
      {
        listeners[action] = listener;
      }
    },
    setCancelListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['cancel'] = listener;
    },
    removeCancelListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['cancel'] = null;
    },
    _notifyCancelListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['cancel']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['cancel'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['cancelMap'](clientRequest, params);
      }
    },
    cancel : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.cancel.mojax', clientRequest, params);
    },
    cancelMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.cancel.mojax', clientRequest, paramString);
    },
    setChildQueryListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['childQuery'] = listener;
    },
    removeChildQueryListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['childQuery'] = null;
    },
    _notifyChildQueryListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['childQuery']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['childQuery'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['childQueryMap'](clientRequest, params);
      }
    },
    childQuery : function(clientRequest, childId)
    {
      var params = {'childId':childId}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.childQuery.mojax', clientRequest, params);
    },
    childQueryMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.childQuery.mojax', clientRequest, paramString);
    },
    setCreateListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['create'] = listener;
    },
    removeCreateListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['create'] = null;
    },
    _notifyCreateListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['create']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['create'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['createMap'](clientRequest, params);
      }
    },
    create : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.create.mojax', clientRequest, params);
    },
    createMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.create.mojax', clientRequest, paramString);
    },
    setDeleteListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['delete'] = listener;
    },
    removeDeleteListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['delete'] = null;
    },
    _notifyDeleteListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['delete']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['delete'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['deleteMap'](clientRequest, params);
      }
    },
    delete : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.delete.mojax', clientRequest, params);
    },
    deleteMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.delete.mojax', clientRequest, paramString);
    },
    setEditListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['edit'] = listener;
    },
    removeEditListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['edit'] = null;
    },
    _notifyEditListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['edit']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['edit'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['editMap'](clientRequest, params);
      }
    },
    edit : function(clientRequest, id)
    {
      var params = {'id':id}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.edit.mojax', clientRequest, params);
    },
    editMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.edit.mojax', clientRequest, paramString);
    },
    setNewInstanceListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['newInstance'] = listener;
    },
    removeNewInstanceListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['newInstance'] = null;
    },
    _notifyNewInstanceListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['newInstance']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['newInstance'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['newInstanceMap'](clientRequest, params);
      }
    },
    newInstance : function(clientRequest, parentId, childId)
    {
      var params = {'parentId':parentId,'childId':childId}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.newInstance.mojax', clientRequest, params);
    },
    newInstanceMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.newInstance.mojax', clientRequest, paramString);
    },
    setNewRelationshipListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['newRelationship'] = listener;
    },
    removeNewRelationshipListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['newRelationship'] = null;
    },
    _notifyNewRelationshipListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['newRelationship']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['newRelationship'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['newRelationshipMap'](clientRequest, params);
      }
    },
    newRelationship : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.newRelationship.mojax', clientRequest, params);
    },
    newRelationshipMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.newRelationship.mojax', clientRequest, paramString);
    },
    setParentQueryListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['parentQuery'] = listener;
    },
    removeParentQueryListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['parentQuery'] = null;
    },
    _notifyParentQueryListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['parentQuery']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['parentQuery'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['parentQueryMap'](clientRequest, params);
      }
    },
    parentQuery : function(clientRequest, parentId)
    {
      var params = {'parentId':parentId}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.parentQuery.mojax', clientRequest, params);
    },
    parentQueryMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.parentQuery.mojax', clientRequest, paramString);
    },
    setUpdateListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['update'] = listener;
    },
    removeUpdateListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['update'] = null;
    },
    _notifyUpdateListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['update']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['update'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['updateMap'](clientRequest, params);
      }
    },
    update : function(clientRequest, dto)
    {
      var params = {'dto':dto}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.update.mojax', clientRequest, params);
    },
    updateMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.update.mojax', clientRequest, paramString);
    },
    setViewListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['view'] = listener;
    },
    removeViewListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['view'] = null;
    },
    _notifyViewListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['view']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['view'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewMap'](clientRequest, params);
      }
    },
    view : function(clientRequest, id)
    {
      var params = {'id':id}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.view.mojax', clientRequest, params);
    },
    viewMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.view.mojax', clientRequest, paramString);
    },
    setViewAllListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['viewAll'] = listener;
    },
    removeViewAllListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['viewAll'] = null;
    },
    _notifyViewAllListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['viewAll']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['viewAll'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewAllMap'](clientRequest, params);
      }
    },
    viewAll : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.viewAll.mojax', clientRequest, params);
    },
    viewAllMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.viewAll.mojax', clientRequest, paramString);
    },
    setViewPageListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['viewPage'] = listener;
    },
    removeViewPageListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['viewPage'] = null;
    },
    _notifyViewPageListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['viewPage']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.gis.persist.HasStyleController._listeners['viewPage'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['viewPageMap'](clientRequest, params);
      }
    },
    viewPage : function(clientRequest, sortAttribute, isAscending, pageSize, pageNumber)
    {
      var params = {'sortAttribute':sortAttribute,'isAscending':isAscending,'pageSize':pageSize,'pageNumber':pageNumber}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.viewPage.mojax', clientRequest, params);
    },
    viewPageMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.gis.persist.HasStyleController.viewPage.mojax', clientRequest, paramString);
    }
  }
});
