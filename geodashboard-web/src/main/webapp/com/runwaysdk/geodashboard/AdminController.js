Mojo.Meta.newClass('com.runwaysdk.geodashboard.AdminController', {
  Constants : 
  {
    CLASS : 'com.runwaysdk.geodashboard.AdminController'
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
    _listeners : {'account':null,'databrowser':null,'geoentity':null,'ontologies':null,'roles':null,'scheduler':null,'universal':null,'users':null},
    setListener : function(listener)
    {
      var listeners = Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners;
      for(var action in listeners)
      {
        listeners[action] = listener;
      }
    },
    setAccountListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['account'] = listener;
    },
    removeAccountListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['account'] = null;
    },
    _notifyAccountListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['account']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['account'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['accountMap'](clientRequest, params);
      }
    },
    account : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.AdminController.account.mojax', clientRequest, params);
    },
    accountMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.AdminController.account.mojax', clientRequest, paramString);
    },
    setDatabrowserListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['databrowser'] = listener;
    },
    removeDatabrowserListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['databrowser'] = null;
    },
    _notifyDatabrowserListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['databrowser']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['databrowser'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['databrowserMap'](clientRequest, params);
      }
    },
    databrowser : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.AdminController.databrowser.mojax', clientRequest, params);
    },
    databrowserMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.AdminController.databrowser.mojax', clientRequest, paramString);
    },
    setGeoentityListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['geoentity'] = listener;
    },
    removeGeoentityListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['geoentity'] = null;
    },
    _notifyGeoentityListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['geoentity']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['geoentity'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['geoentityMap'](clientRequest, params);
      }
    },
    geoentity : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.AdminController.geoentity.mojax', clientRequest, params);
    },
    geoentityMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.AdminController.geoentity.mojax', clientRequest, paramString);
    },
    setOntologiesListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['ontologies'] = listener;
    },
    removeOntologiesListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['ontologies'] = null;
    },
    _notifyOntologiesListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['ontologies']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['ontologies'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['ontologiesMap'](clientRequest, params);
      }
    },
    ontologies : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.AdminController.ontologies.mojax', clientRequest, params);
    },
    ontologiesMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.AdminController.ontologies.mojax', clientRequest, paramString);
    },
    setRolesListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['roles'] = listener;
    },
    removeRolesListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['roles'] = null;
    },
    _notifyRolesListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['roles']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['roles'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['rolesMap'](clientRequest, params);
      }
    },
    roles : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.AdminController.roles.mojax', clientRequest, params);
    },
    rolesMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.AdminController.roles.mojax', clientRequest, paramString);
    },
    setSchedulerListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['scheduler'] = listener;
    },
    removeSchedulerListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['scheduler'] = null;
    },
    _notifySchedulerListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['scheduler']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['scheduler'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['schedulerMap'](clientRequest, params);
      }
    },
    scheduler : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.AdminController.scheduler.mojax', clientRequest, params);
    },
    schedulerMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.AdminController.scheduler.mojax', clientRequest, paramString);
    },
    setUniversalListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['universal'] = listener;
    },
    removeUniversalListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['universal'] = null;
    },
    _notifyUniversalListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['universal']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['universal'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['universalMap'](clientRequest, params);
      }
    },
    universal : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.AdminController.universal.mojax', clientRequest, params);
    },
    universalMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.AdminController.universal.mojax', clientRequest, paramString);
    },
    setUsersListener : function(listener)
    {
      Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['users'] = listener;
    },
    removeUsersListener : function()
    {
      Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['users'] = null;
    },
    _notifyUsersListener : function(params, action, actionId)
    {
      if(Mojo.Util.isFunction(Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['users']))
      {
        var clientRequest = Mojo.$.com.runwaysdk.geodashboard.AdminController._listeners['users'](params, action, actionId);
      }
      if(clientRequest != null)
      {
        this['usersMap'](clientRequest, params);
      }
    },
    users : function(clientRequest)
    {
      var params = {}
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.AdminController.users.mojax', clientRequest, params);
    },
    usersMap : function(clientRequest, params)
    {
      var paramString = Mojo.Util.convertMapToQueryString(params);
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('com.runwaysdk.geodashboard.AdminController.users.mojax', clientRequest, paramString);
    }
  }
});
