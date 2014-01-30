/*
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * RunwaySDK Javascript DTO library.
 * 
 * @author Terraframe
 */
//define(["./RunwaySDK_Core"], function(){
(function(){

// set up some package constants.
Mojo.JSON_ENDPOINT = 'Mojo/JSONControllerServlet';
Mojo.ATTRIBUTE_DTO_PACKAGE = Mojo.ROOT_PACKAGE+'transport.attributes.';
Mojo.MD_DTO_PACKAGE = Mojo.ROOT_PACKAGE+'transport.metadata.';
Mojo.BUSINESS_PACKAGE = Mojo.ROOT_PACKAGE+'business.';
Mojo.ATTRIBUTE_PROBLEM_PACKAGE = Mojo.ROOT_PACKAGE+'dataaccess.attributes.';

var RunwayRequest = Mojo.Meta.newClass(Mojo.ROOT_PACKAGE+'RunwayRequest', {

  Extends : Mojo.$.com.runwaysdk.AjaxRequest,

  Instance : {

    initialize: function(endpoint, clientRequest, parameters, isController)
    {
      var url = Mojo.ClientSession.getBaseEndpoint()+endpoint;
      this.$initialize(url, parameters, clientRequest.options);
    
      this.clientRequest = clientRequest;
      this.isController = isController || false;
      
      this.clientRequest.setTransport(this._xhr);
    },
    
    _getResponseText : function()
    {
      return this._xhr.responseText;
    },

    _send : function()
    {
      if (Mojo.Util.isFunction(this.clientRequest.onSend))
      {
        this.clientRequest.onSend();
      }
    },
    
    _complete : function()
    {
      if (Mojo.Util.isFunction(this.clientRequest.onComplete))
      {
        this.clientRequest.onComplete();
      }
    },
    
    _success : function()
    {
      var responseText = this._getResponseText();
      var obj = null;
      if(!this.isController)
      {
        var json = Mojo.Util.getObject(responseText);
        obj = DTOUtil.convertToType(json.returnValue);
 
        // add warnings/information to the ClientRequest
        if(Mojo.Util.isArray(json.warnings) && json.warnings.length > 0)
        {
          this.clientRequest.setWarnings(DTOUtil.convertToType(json.warnings));
        }
 
        if(Mojo.Util.isArray(json.information) && json.information.length > 0)
        {
          this.clientRequest.setInformation(DTOUtil.convertToType(json.information));
        }
      }
      else
      {
        obj = responseText;
      }

      this.clientRequest.performOnSuccess(obj);
    },

    _failure : function()
    {
      var responseText = this._getResponseText();
      var e = null;
      
      if (responseText === "" && this._xhr.status === 0) {
    	  
        var e = new com.runwaysdk.Exception(com.runwaysdk.Localize.get("rNoServer", "Unable to communicate with server."));
        this.clientRequest.performOnFailure(e);
        return;
      }
      
      var exceptionType = null;
      try
      {
        var obj = Mojo.Util.getObject(responseText);

        if('dto_type' in obj && obj.dto_type === Mojo.ROOT_PACKAGE+'RunwayExceptionDTO')
        {
          exceptionType = obj.wrappedException;
          e = DTOUtil.convertToType(obj);
        }
        else if('dto_type' in obj && obj.dto_type === Mojo.ROOT_PACKAGE+'ProblemExceptionDTO')
        {
          exceptionType = obj.dto_type;
          e = DTOUtil.convertToType(obj);
        }
        else if('_type' in obj)
        {
          exceptionType = obj._type;
          if(Mojo.Meta.classExists(obj._type))
          {
            e = DTOUtil.convertToType(obj);
          }
          else
          {
            e = new com.runwaysdk.Exception(obj);
          }
        }
        else
        {
          e = new com.runwaysdk.Exception(obj);
        }

        if(Mojo.Util.isString(exceptionType) && exceptionType.length > 0)
        {
          var exNameInd = exceptionType.lastIndexOf('.');
          if(exNameInd !== -1)
          {
            exceptionType = exceptionType.substr(exNameInd + 1);
          }
        }
      }
      catch(e1)
      {
        // Intentionally empty
      }
      
      if (e == null) {
        var e = new com.runwaysdk.Exception(responseText);
        this.clientRequest.performOnFailure(e);
      }
      else {
        this.clientRequest.performOnFailure(e, exceptionType);
      }
    }
  }
});

var DTOUtil = Mojo.Meta.newClass(Mojo.ROOT_PACKAGE+'DTOUtil', {

  IsAbstract : true,

  Static : {
    convertToType : function(value)
    {
      // void/null returns
      if(value == null)
      {
        return null;
      }
      else if(Mojo.Util.isObject(value))
      {
        // MdEnumeration Items
        if('enumType' in value && 'enumName' in value)
        {
          return Mojo.Meta.findClass(value.enumType)[value.enumName];
        }
        // special case for ValueObjectDTOs, which define both a dto_type and _type
        else if('dto_type' in value && value.dto_type === Mojo.BUSINESS_PACKAGE+'ValueObjectDTO')
        {
          return Mojo.Meta.newInstance(value.dto_type, value);
        }
        // messages may be type-safe or generic
        else if('dto_type' in value && (value.dto_type === Mojo.BUSINESS_PACKAGE+'WarningDTO' || value.dto_type === Mojo.BUSINESS_PACKAGE+'InformationDTO'))
        {
          if(Mojo.Meta.classExists(value._type))
          {
            return Mojo.Meta.newInstance(value._type, value);
          }
          else
          {
            return Mojo.Meta.newInstance(value.dto_type, value);
          }
        }
        else if('_type' in value && value._type != '')
        {
          // generated dtos
          return Mojo.Meta.newInstance(value._type, value);
        }
        else if('dto_type' in value && value.dto_type != '')
        {
          // hard-coded dtos
          return Mojo.Meta.newInstance(value.dto_type, value);
        }
        else
        {
          return value;
        }
      }
      else if(Mojo.Util.isArray(value))
      {
        for(var i=0; i<value.length; i++)
        {
          value[i] = DTOUtil.convertToType(value[i]);
        }

        return value;
      }
      else
      {
        return value;
      }
    }
  }
});

var Facade = Mojo.Meta.newClass(Mojo.ROOT_PACKAGE+'Facade', {

  IsAbstract : true,

  Static : {

  /**
   * Import Types.
   * 
   * @param clientRequest
   * @param types
   * @param options
   */
    importTypes : function(clientRequest, types, options)
    {
      if(Mojo.Util.isString(types))
      {
        types = [types];
      }
  
      var json = Mojo.Util.getJSON(types);
  
      var params = {
        'method' : 'importTypes',
        'types' : json};
  
      var onSuccessRef = clientRequest.onSuccess;
      var importCallback = function(jsSource)
      {
        if(Mojo.Util.isObject(options))
        {
          if(options.autoEval)
          {
            eval(jsSource); // FIXME make secure
          }
          else if('appendTo' in options)
          {
            var script = document.createElement("script");
            script.type = "text/javascript";
  
            try
            {
              script.appendChild(document.createTextNode(jsSource));
            }
            catch(e)
            {
              script.text = jsSource; // IE
            }
  
            var appendTo = options.appendTo;
            var parentEl = Mojo.Util.isString(appendTo) ? document.getElementById(appendTo) : appendTo;
            parentEl.appendChild(script);
          }
        }
  
        if(Mojo.Util.isFunction(onSuccessRef))
        {
          onSuccessRef.call(clientRequest, jsSource);
        }
      };
      clientRequest.onSuccess = importCallback;
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * Wrapper for generated Facade methods.
   */
    _methodWrapper : function(clientRequest, params)
    {
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * Wrapper for generated controller methods
   */
    _controllerWrapper : function(endpoint, clientRequest, params)
    {
      if(Mojo.Util.isObject(params))
      {
        var suffix = '.mofo';
        if(endpoint.indexOf(suffix, endpoint.length - suffix.length) !== -1)
        {
          params = {"com.runwaysdk.mofoObject":Mojo.Util.getJSON(params)};
        }
        else
        {
          params = {"com.runwaysdk.mojaxObject":Mojo.Util.getJSON(params)};
        }
      }
  
      new RunwayRequest(endpoint, clientRequest, params, true).apply();
    },
  
    /**
   * Login
   */
    login : function (clientRequest, username, password)
    {
      var params = {
        'method' : 'login',
        'username' : username,
        'password' : password};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * LoginUser
   */
    loginUser : function (clientRequest, username, password)
    {
      var params = {
        'method' : 'loginUser',
        'username' : username,
        'password' : password};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * ChangeLogin
   */
    changeLogin : function (clientRequest, username, password)
    {
      var params = {
        'method' : 'changeLogin',
        'username' : username,
        'password' : password};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * LoginAnonymous
   */
    loginAnonymous : function (clientRequest)
    {
      var params = {
        'method' : 'loginAnonymous'
      };
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * Logout
   */
    logout : function (clientRequest)
    {
      var params = {
        'method' : 'logout'};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * NewBusiness
   */
    newBusiness : function (clientRequest, type)
    {
      var params = {
        'method' : 'newBusiness',
        'type' : type};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * CreateSessionComponent
   */
    createSessionComponent : function(clientRequest, sessionDTO)
    {
      // convert the BusinessDTO into a JSON String
      var json = Mojo.Util.getJSON(sessionDTO);
  
      var params = {
        'method' : 'createSessionComponent',
        'sessionDTO' : json};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * CreateBusiness
   */
    createBusiness : function (clientRequest, businessDTO)
    {
      // convert the BusinessDTO into a JSON String
      var json = Mojo.Util.getJSON(businessDTO);
  
      var params = {
        'method' : 'createBusiness',
        'businessDTO' : json};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * NewStruct
   */
    newStruct : function (clientRequest, type)
    {
      var params = {
        'method' : 'newStruct',
        'type' : type};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * NewEntity
   */
    newEntity : function (clientRequest, type)
    {
      var params = {
        'method' : 'newEntity',
        'type' : type};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * CreateStruct
   */
    createStruct : function (clientRequest, structDTO)
    {
      // convert the StructDTO into a JSON String
      var json = Mojo.Util.getJSON(structDTO);
  
      var params = {
        'method' : 'createStruct',
        'structDTO' : json};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * CreateRelationship
   */
    createRelationship : function (clientRequest, relationshipDTO)
    {
      // convert the RelationshipDTO into a JSON String
      var json = Mojo.Util.getJSON(relationshipDTO);
  
      var params = {
        'method' : 'createRelationship',
        'relationshipDTO' : json};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * CheckAdminScreenAccess
   */
    checkAdminScreenAccess : function (clientRequest)
    {
      var params = {
        'method' : 'checkAdminScreenAccess'
      };
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * Update
   */
    update : function (clientRequest, mutableDTO)
    {
      // convert the MutableDTO into a JSON String
      var json = Mojo.Util.getJSON(mutableDTO);
  
      var params = {
        'method' : 'update',
        'mutableDTO' : json};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * DeleteBusiness
   */
    deleteEntity : function (clientRequest, id)
    {
      var params = {
        'method' : 'delete',
        'id' : id};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * GetInstance
   */
    get : function (clientRequest, id)
    {
      var params = {
        'method' : 'get',
        'id' : id};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    getUser : function(clientRequest)
    {
      var params = {
        'method' : 'getUser'
      };
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * GetQuery
   */
    getQuery : function(clientRequest, type)
    {
      var params = {
        'method' : 'getQuery',
        'type' : type
      };
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * AddChild
   * 
   * @returns com.runwaysdk.business.RelationshipDTO
   */
    addChild : function(clientRequest, parentId, childId, relationshipType)
    {
      var params = {
        'method' : 'addChild',
        'parentId' : parentId,
        'childId' : childId,
        'relationshipType' : relationshipType};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * AddParent
   */
    addParent : function(clientRequest, parentId, childId, relationshipType)
    {
      var params = {
        'method' : 'addParent',
        'parentId' : parentId,
        'childId' : childId,
        'relationshipType' : relationshipType};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * DeleteChild
   */
    deleteChild : function(clientRequest, relationshipId)
    {
      var params = {
        'method' : 'deleteChild',
        'relationshipId' : relationshipId};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * DeleteParent
   */
    deleteParent : function(clientRequest, relationshipId)
    {
      var params = {
        'method' : 'deleteParent',
        'relationshipId' : relationshipId};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * DeleteChildren
   */
    deleteChildren : function(clientRequest, parentId, relationshipType)
    {
      var params = {
        'method' : 'deleteChildren',
        'parentId' : parentId,
        'relationshipType' : relationshipType};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * DeleteParents
   */
    deleteParents : function(clientRequest, childId, relationshipType)
    {
      var params = {
        'method' : 'deleteParents',
        'childId' : childId,
        'relationshipType' : relationshipType};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
    
    /**
   * getChildren
   */
    getChildren : function(clientRequest, parentId, relationshipType)
    {
      var params = {
        'method' : 'getChildren',
        'parentId' : parentId,
        'relationshipType' : relationshipType};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * GetChildRelationships
   */
    getChildRelationships : function(clientRequest, parentId, relationshipType)
    {
      var params = {
        'method' : 'getChildRelationships',
        'parentId' : parentId,
        'relationshipType' : relationshipType};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * GetParentRelationship
   */
    getParentRelationships : function(clientRequest, childId, relationshipType)
    {
      var params = {
        'method' : 'getParentRelationships',
        'childId' : childId,
        'relationshipType' : relationshipType};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
  
    /**
   * getParents
   */
    getParents : function(clientRequest, childId, relationshipType)
    {
      var params = {
        'method' : 'getParents',
        'childId' : childId,
        'relationshipType' : relationshipType};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
  
    /**
   * Lock
   */
    lock : function(clientRequest, id)
    {
      var params = {
        'method' : 'lock',
        'id' : id};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * Unlock
   */
    unlock : function(clientRequest, id)
    {
      var params = {
        'method' : 'unlock',
        'id' : id};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * GrantTypePermission
   */
    grantTypePermission : function(clientRequest, actorId, mdTypeId, operationNames)
    {
      if(!Mojo.Util.isArray(operationNames))
      {
        operationNames = [operationNames];
      }
  
      var operationNamesJSON = Mojo.Util.getJSON(operationNames);
  
      var params = {
        'method' : 'grantTypePermission',
        'actorId' : actorId,
        'operationNames' : operationNamesJSON,
        'mdTypeId' : mdTypeId};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * GrantStatePermission
   */
    grantStatePermission : function(clientRequest, actorId, stateId, operationNames)
    {
      if(!Mojo.Util.isArray(operationNames))
      {
        operationNames = [operationNames];
      }
  
      var operationNamesJSON = Mojo.Util.getJSON(operationNames);
  
      var params = {
        'method' : 'grantStatePermission',
        'actorId' : actorId,
        'operationNames' : operationNamesJSON,
        'stateId' : stateId};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * GrantAttributePermission
   */
    grantAttributePermission : function(clientRequest, actorId, mdAttributeId, operationNames)
    {
      if(!Mojo.Util.isArray(operationNames))
      {
        operationNames = [operationNames];
      }
  
      var operationNamesJSON = Mojo.Util.getJSON(operationNames);
  
      var params = {
        'method' : 'grantAttributePermission',
        'actorId' : actorId,
        'operationNames' : operationNamesJSON,
        'mdAttributeId' : mdAttributeId};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * GrantAttributeStatePermission
   */
    grantAttributeStatePermission : function(clientRequest, actorId, mdAttributeId, stateId, operationNames)
    {
      if(!Mojo.Util.isArray(operationNames))
      {
        operationNames = [operationNames];
      }
  
      var operationNamesJSON = Mojo.Util.getJSON(operationNames);
  
      var params = {
        'method' : 'grantAttributeStatePermission',
        'actorId' : actorId,
        'operationNames' : operationNamesJSON,
        'mdAttributeId' : mdAttributeId,
        'stateId' : stateId};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * PromoteObject
   */
    promoteObject : function(callback, businessJSON, transitionName)
    {
       var params = {
        'method' : 'promoteObject',
        'businessDTO' : businessJSON,
        'transitionName' : transitionName};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * RevokeTypePermission
   */
    revokeTypePermission : function(clientRequest, actorId, mdTypeId, operationNames)
    {
      if(!Mojo.Util.isArray(operationNames))
      {
        operationNames = [operationNames];
      }
  
      var operationNamesJSON = Mojo.Util.getJSON(operationNames);
  
      var params = {
        'method' : 'revokeTypePermission',
        'actorId' : actorId,
        'operationNames' : operationNamesJSON,
        'mdTypeId' : mdTypeId};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * RevokeStatePermission
   */
    revokeStatePermission : function(clientRequest, actorId, stateId, operationNames)
    {
      if(!Mojo.Util.isArray(operationNames))
      {
        operationNames = [operationNames];
      }
  
      var operationNamesJSON = Mojo.Util.getJSON(operationNames);
  
      var params = {
        'method' : 'revokeStatePermission',
        'actorId' : actorId,
        'operationNames' : operationNamesJSON,
        'stateId' : stateId};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * RevokeAttributePermission
   */
    revokeAttributePermission : function(clientRequest, actorId, mdAttributeId, operationNames)
    {
      if(!Mojo.Util.isArray(operationNames))
      {
        operationNames = [operationNames];
      }
  
      var operationNamesJSON = Mojo.Util.getJSON(operationNames);
  
      var params = {
        'method' : 'revokeAttributePermission',
        'actorId' : actorId,
        'operationNames' : operationNamesJSON,
        'mdAttributeId' : mdAttributeId};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * RevokeAttributeStatePermission
   */
    revokeAttributeStatePermission : function(clientRequest, actorId, mdAttributeId, stateId, operationNames)
    {
      if(!Mojo.Util.isArray(operationNames))
      {
        operationNames = [operationNames];
      }
  
      var operationNamesJSON = Mojo.Util.getJSON(operationNames);
  
      var params = {
        'method' : 'revokeAttributeStatePermission',
        'actorId' : actorId,
        'operationNames' : operationNamesJSON,
        'mdAttributeId' : mdAttributeId,
        'stateId' : stateId};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * InvokeMethod
   */
    invokeMethod : function(clientRequest, metadata, mutableDTO, parameters)
    {
      var mutableDTOJSON = Mojo.Util.getJSON(mutableDTO);
      var metadataJSON = Mojo.Util.getJSON(metadata);
      var parametersJSON = Mojo.Util.getJSON(parameters);
  
      var params = {
        'method' : 'invokeMethod',
        'mutableDTO' : mutableDTOJSON,
        'metadata' : metadataJSON,
        'parameters' : parametersJSON};
  
      // specific callback to invokeMethod()
      var onSuccessRef = clientRequest.onSuccess;
      var invokeCallback = function(objArray)
      {
        var returnObject = objArray[0];
        var calledObject = objArray[1];
  
        if(Mojo.Util.isFunction(onSuccessRef))
          onSuccessRef.call(clientRequest, returnObject, calledObject);
      };
      clientRequest.onSuccess = invokeCallback;
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * GetEnumeration
   */
    getEnumeration : function(clientRequest, enumType, enumName)
    {
      var params = {
        'method' : 'getEnumeration',
        'enumType' : enumType,
        'enumName' : enumName};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * GetEnumerations
   */
    getEnumerations : function(clientRequest, enumType, enumNames)
    {
      var enumNamesJSON = Mojo.Util.getJSON(enumNames);
  
      var params = {
        'method' : 'getEnumerations',
        'enumType' : enumType,
        'enumNames' : enumNamesJSON};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * GetAllEnumerations
   */
    getAllEnumerations : function(clientRequest, enumType)
    {
      var params = {
        'method' : 'getAllEnumerations',
        'enumType' : enumType};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * QueryBusinesses
   */
    queryBusinesses : function(clientRequest, queryDTO)
    {
      queryDTO.clearAttributes();
      var json = Mojo.Util.getJSON(queryDTO);
  
      var params = {
        'method' : 'queryBusinesses',
        'queryDTO' : json};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    groovyValueQuery : function(clientRequest, queryDTO)
    {
      queryDTO.clearAttributes();
      queryDTO.clearResultSet();
      var json = Mojo.Util.getJSON(queryDTO);
  
      var params = {
        'method' : 'groovyValueQuery',
        'queryDTO' : json
      };
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * QueryStructs
   */
    queryStructs : function(clientRequest, queryDTO)
    {
      queryDTO.clearAttributes();
      queryDTO.clearResultSet();
      var json = Mojo.Util.getJSON(queryDTO);
  
      var params = {
        'method' : 'queryStructs',
        'queryDTO' : json};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * QueryEntities
   */
    queryEntities : function(clientRequest, queryDTO)
    {
//      queryDTO.clearAttributes();
      queryDTO.clearResultSet();
      var json = Mojo.Util.getJSON(queryDTO);
  
      var params = {
        'method' : 'queryEntities',
        'queryDTO' : json};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
  
    /**
   * QueryRelationships
   */
    queryRelationships : function(clientRequest, queryDTO)
    {
      queryDTO.clearAttributes();
      queryDTO.clearResultSet();
      var json = Mojo.Util.getJSON(queryDTO);
  
      var params = {
        'method' : 'queryRelationships',
        'queryDTO' : json};
  
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
    
    /**
     * getTermAllChildren
     * 
     * @returns com.runwaysdk.business.ontology.TermAndRel[]
     */
    getTermAllChildren : function(clientRequest, parentId, pageNum, pageSize)
    {
      var params = {
        'method' : 'getTermAllChildren',
        'parentId' : parentId,
        'pageNum' : pageNum,
        'pageSize' : pageSize};
      
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
    
    /**
     * moveBusiness
     * 
     * @returns com.runwaysdk.business.RelationshipDTO
     */
    moveBusiness : function(clientRequest, newParentId, childId, oldRelationshipId, newRelationshipType)
    {
      var params = {
        'method' : 'moveBusiness',
        'parentId' : newParentId,
        'childId' : childId,
        'relationshipId' : oldRelationshipId,
        'relationshipType' : newRelationshipType};
      
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    }
  }
});

var serializableIF = Mojo.Meta.newInterface(Mojo.ROOT_PACKAGE+'Serializable', {
  
  Instance : {
    toJSON : {
      Method : function(key) {},
      EnforceArity : true
    }
  }
});


/**
 * ComponentQueryDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'ComponentQueryDTO', {

  Implements : serializableIF,
  
  IsAbstract : true,
  
  Instance : {

    initialize : function(obj)
    {
      this.dto_type = obj.dto_type;
      this.queryType = obj.queryType;
      this.pageSize = obj.pageSize;
      this.pageNumber = obj.pageNumber;
      this.count = obj.count;
      this.countEnabled = obj.countEnabled;
      this.groovyQuery = obj.groovyQuery;

      for(var attributeName in obj.definedAttributes)
      {
        var attribute = obj.definedAttributes[attributeName];
        var attributeDTO = Mojo.Meta.newInstance(attribute.dtoType, attribute);
        obj.definedAttributes[attributeName] = attributeDTO;
      }
      
      this.definedAttributes = obj.definedAttributes;  // keep ref for structs

      // now convert the result set
      this.resultSet = [];
      for(var i=0; i<obj.resultSet.length; i++)
      {
        var result = obj.resultSet[i];
        this.resultSet.push(DTOUtil.convertToType(result));
      }
    },
  
    getType : function()
    {
      return this.queryType;
    },
  
    setPageNumber : function(pageNumber)
    {
      this.pageNumber = pageNumber;
    },
  
    setPageSize : function(pageSize)
    {
      this.pageSize = pageSize;
    },
  
    getPageNumber : function()
    {
      return this.pageNumber;
    },
  
    getPageSize : function()
    {
      return this.pageSize;
    },
  
    getAttributeDTO : function(attrName)
    {
      return this.definedAttributes[attrName];
    },
  
    getAttributeNames : function()
    {
      return Mojo.Util.getKeys(this.definedAttributes);
    },
  
    clearAttributes : function()
    {
      this.definedAttributes = {};
    },
  
    getResultSet : function()
    {
      return this.resultSet;
    },
  
    clearResultSet : function()
    {
      this.resultSet = [];
    },
  
    getCount : function() { return this.count; },
  
    setCountEnabled : function(countEnabled) {this.countEnabled = countEnabled; },
  
    isCountEnabled : function() { return this.countEnabled; },
    
    toJSON : function(key)
    {
      return new com.runwaysdk.StandardSerializer(this).toJSON(key);
    }
  
  }
});

/**
 * ValueQueryDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'ValueQueryDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'ComponentQueryDTO',
  
  Instance : {

    initialize : function(obj)
    {
      if(typeof obj === 'string')
      {
        // set defaults here because this object can be instantiated client-side
        this.type = '';
        this.resultSet = [];
        this.definedAttributes = {};
        this.groovyQuery = obj;
        this.pageSize = 0;
        this.pageNumber = 0;
        this.count = 0;
        this.countEnabled = false;
  
        this.dto_type = Mojo.BUSINESS_PACKAGE+'ValueQueryDTO';
      }
      else
      {
        this.$initialize(obj);
      }
    },
  
    getGroovyQuery : function() { return this.groovyQuery; }
  
  }
});

/**
 * ClassQueryDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'ClassQueryDTO', {

  IsAbstract : true,
  
  Extends : Mojo.BUSINESS_PACKAGE+'ComponentQueryDTO',
  
  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this.classes = {};
    },
  
    getClassTypes : function()
    {
      return Mojo.Util.getKeys(this.classes);
    },
  
    getSuperClasses : function(classType)
    {
      return this.classes[classType];
    }
  
  }
});

/**
 * EntityQueryDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'EntityQueryDTO', {

  IsAbstract : true,
  
  Extends : Mojo.BUSINESS_PACKAGE+'ClassQueryDTO',
  
  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this.conditions = {};
      for(var i=0; i<obj.conditions.length; i++)
      {
        var conditionJSON = obj.conditions[i];

        var attribute = conditionJSON.attribute;
        var condition = conditionJSON.condition;
        var value = conditionJSON.value;

        var key = attribute+condition+value;
        
        var klass = Mojo.Meta.findClass(Mojo.BUSINESS_PACKAGE+'Condition');
        this.conditions[key] = new klass(attribute, condition, value);
      }

      this.orderByList = [];
      for(var i=0; i<obj.orderByList.length; i++)
      {
        var orderByJSON = obj.orderByList[i];

        var orderBy;
        if(orderByJSON.length == 3) // StructOrderBy
        {
          var attributeStruct = orderByJSON.attributeStruct;
          var attribute = orderByJSON.attribute;
          var order = orderByJSON.order;

          var klass = Mojo.Meta.findClass(Mojo.BUSINESS_PACKAGE+'StructOrderBy');
          orderBy = new klass(attributeStruct, attribute, order);
        }
        else
        {
          var attribute = orderByJSON.attribute;
          var order = orderByJSON.order;

          var klass = Mojo.Meta.findClass(Mojo.BUSINESS_PACKAGE+'OrderBy');
          orderBy = new klass(attributeStruct, attribute, order);
        }
        
        this.orderByList.push(orderBy);
      }
    },
  
    addCondition : function(attribute, condition, value)
    {
      var key = attribute+condition+value;
      
      var klass = Mojo.Meta.findClass(Mojo.BUSINESS_PACKAGE+'Condition');
      var conditionObj = new klass(attribute, condition, value);
      this.conditions[key] = conditionObj;
    },
  
    clearConditions : function()
    {
      this.conditions = {};
    },
  
    getConditions : function()
    {
      return Mojo.Util.getValues(this.conditions);
    },
  
    addOrderBy : function(attribute, order)
    {
      var klass = Mojo.Meta.findClass(Mojo.BUSINESS_PACKAGE+'OrderBy');
      var orderBy = new klass(attribute, order);
      this.orderByList.push(orderBy);
    },
  
    getOrderByList : function()
    {
      return this.orderByList;
    },
  
    clearOrderByList : function()
    {
      this.orderByList = [];
    }
  
  }
});

// OrderBy
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'OrderBy', {

  Instance : {

    initialize : function(attribute, order)
    {
      this.attribute = attribute;
      this.order = order;
    },
  
    getAttribute : function() { return this.attribute; },
  
    getOrder : function() { return this.order; },
    
    toJSON : function(key)
    {
      return new Mojo.$.com.runwaysdk.StandardSerializer(this).toJSON(key);
    }
  
  }
});

// Condition
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'Condition', {

  Instance : {

    initialize : function(attribute, condition, value)
    {
      this.attribute = attribute;
      this.condition = condition;
      this.value = value;
    },
  
    getAttribute : function() { return this.attribute; },
  
    getCondition : function() { return this.condition; },
  
    getValue : function() { return this.value; },
    
    toJSON : function(key)
    {
      return new Mojo.$.com.runwaysdk.StandardSerializer(this).toJSON(key);
    }
  
  }
});

/**
 * ElementQueryDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'ElementQueryDTO', {

  IsAbstract : true,
  
  Extends : Mojo.BUSINESS_PACKAGE+'EntityQueryDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this._isAbstract = obj._isAbstract;
    },
  
    isAbstract : function(){ return this._isAbstract; },
  
    addStructOrderBy : function(structAttribute, attribute, order)
    {
      var klass = Mojo.Meta.findClass(Mojo.BUSINESS_PACKAGE+'StructOrderBy');
      var orderBy = new klass(structAttribute, attribute, order);
      this.orderByList.push(orderBy);
    }
  
  }
});

// StructOrderBy
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'StructOrderBy', {

  Extends : Mojo.BUSINESS_PACKAGE+'OrderBy',
  
  Instance : {

    initialize : function(attributeStruct, attribute, order)
    {
      this.$initialize(attribute, order);
    
      this.attributeStruct = attributeStruct;
    },
  
    getAttributeStruct: function() { return this.attributeStruct; },
  
    getAttribute : function() { return this.attribute; },
  
    getOrder : function() { return this.order; }
  
  }
});

/**
 * StructQueryDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'StructQueryDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'EntityQueryDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

/**
 * BusinessQueryDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'BusinessQueryDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'ElementQueryDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
  
      // load MdRelationship information for child and parent
      this.typeInMdRelationshipAsChildList = [];
      this.typeInMdRelationshipAsParentList = [];
      
      for(var i=0; i<obj.typeInMdRelationshipAsChildList.length; i++)
      {
        var asChild = obj.typeInMdRelationshipAsChildList[i];
        var asChildObj = Mojo.Meta.newInstance(Mojo.BUSINESS_PACKAGE+'TypeInMdRelationshipAsChild', asChild);
        this.typeInMdRelationshipAsChildList.push(asChildObj);
      }
 
      for(var i=0; i<obj.typeInMdRelationshipAsParentList.length; i++)
      {
        var asParent = obj.typeInMdRelationshipAsParentList[i];
        var asParentObj = Mojo.Meta.newInstance(Mojo.BUSINESS_PACKAGE+'TypeInMdRelationshipAsParent', asParent);
        this.typeInMdRelationshipAsParentList.push(asParentObj);
      }
    },
  
    getTypeInMdRelationshipAsChildList : function() { return this.typeInMdRelationshipAsChildList; },
  
    getTypeInMdRelationshipAsParentList : function() { return this.typeInMdRelationshipAsParentList; }
  
  }
});

// TypeInMdRelationshipAsChild
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'TypeInMdRelationshipAsChild', {

  Instance : {

    initialize : function(obj)
    {
      this.childDisplayLabel = obj.childDisplayLabel;
      this.relationshipType = obj.relationshipType;
    },
  
    getChildDisplayLabel : function() { return this.childDisplayLabel; },
  
    getRelationshipType : function() { return this.relationshipType; },
    
    toJSON : function(key)
    {
      return new Mojo.$.com.runwaysdk.StandardSerializer(this).toJSON(key);
    }
  
  }
});

// TypeInMdRelationshipAsParent
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'TypeInMdRelationshipAsParent', {

  Instance : {

    initialize : function(obj)
    {
      this.parentDisplayLabel = obj.parentDisplayLabel;
      this.relationshipType = obj.relationshipType;
    },
  
    getParentDisplayLabel : function() { return this.parentDisplayLabel; },
  
    getRelationshipType : function() { return this.relationshipType; },
    
    toJSON : function(key)
    {
      return new Mojo.$.com.runwaysdk.StandardSerializer(this).toJSON(key);
    }
  
  }
});

/**
 * RelationshipQueryDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'RelationshipQueryDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'ElementQueryDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this.parentMdBusiness = obj.parentMdBusiness;
      this.childMdBusiness = obj.childMdBusiness;
    },
  
    getParentMdBusiness : function() { return this.parentMdBusiness; },
  
    getChildMdBusiness : function() { return this.childMdBusiness; }
  
  }
});

Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'ViewQueryDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'ComponentQueryDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

/**
 * ComponentDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'ComponentDTO', {

  Implements : serializableIF,
  
  IsAbstract : true,

  Instance : {

    initialize : function(obj)
    {
      this.dto_type = obj.dto_type;
      
      this.id = obj.id;

      this._type = obj._type;
      
      if(Mojo.Util.isObject(obj._typeMd))
      {
        this._typeMd = Mojo.Meta.newInstance(Mojo.MD_DTO_PACKAGE+'TypeMd', obj._typeMd);
      }
      else
      {
        this._typeMd = null;
      }

      this._toString = obj._toString;
      this.readable = obj.readable;

      for(var attributeName in obj.attributeMap)
      {
        var attribute = obj.attributeMap[attributeName];
        var attributeDTO = Mojo.Meta.newInstance(attribute.dtoType, attribute);
        obj.attributeMap[attributeName] = attributeDTO;
      }
      this.attributeMap = obj.attributeMap; // keep reference for structs
    },
  
    getType : function() { return this._type; },
  
    getTypeMd : function() { return this._typeMd; },
  
    getId : function() { return this.id; },
    
    getHashCode : function() { return this.getId(); },
  
    getIdMd : function() { return this.getAttributeDTO('id').getAttributeMdDTO(); },
  
    getAttributeDTO : function(attributeName)
    {
      return this.attributeMap[attributeName];
    },
  
    getMd : function() { return this._typeMd; },
  
    toString : function() { return this._toString; },
  
    isReadable : function() { return this.readable; },
  
    setValue : function(attributeName, value) { this.getAttributeDTO(attributeName).setValue(value); },
  
    getValue : function(attributeName) { return this.getAttributeDTO(attributeName).getValue(); },
    
    getAttributeNames : function()
    {
      return Mojo.Util.getKeys(this.attributeMap, true);
    },
    
    toJSON : function(key)
    {
      return new Mojo.$.com.runwaysdk.StandardSerializer(this).toJSON(key);
    }
  }
});

Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'MutableDTO', {

  IsAbstract : true,
  
  Extends : Mojo.BUSINESS_PACKAGE+'ComponentDTO',

  Instance : {

    initialize : function(obj)
    {
    
      // Generate a new id per instance instead of using the id
      // of the cached JSON (to avoid all new instances having the same id),
      // and preserve the id of the metadata type.
      if(obj.newInstance)
      {
        obj.id = Mojo.Util.generateId()+obj.id.substring(32);    
      }
      
      this.$initialize(obj);
    
  
      this.writable = obj.writable;
      this.modified = obj.modified;
      this.newInstance = obj.newInstance;
    },
    
    isWritable : function() {return this.writable; },
  
    setModified : function(modified) { this.modified = modified; },
  
    isModified : function() { return this.modified; },
  
    isNewInstance : function() { return this.newInstance; }
  
  },
  
  Static : {
  
    get : function(clientRequest, id)
    {
      Facade.get(clientRequest, id);
    }
  
  }
});

/**
 * ValueObjectDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'ValueObjectDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'ComponentDTO',

  Instance : {

    initialize : function (obj)
    {
      this.$initialize(obj);
    }
  
  }
});

/**
 * TransientDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'TransientDTO', {

  IsAbstract : true,
  
  Extends : Mojo.BUSINESS_PACKAGE+'MutableDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
    }
    
  }
});

/**
 * LocalizableDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'LocalizableDTO', {

  IsAbstract : true,
  
  Extends : Mojo.BUSINESS_PACKAGE+'TransientDTO',

  Instance : {

    initialize : function (obj)
    {
      this.$initialize(obj);
    }
  
  }
});

/**
 * ExceptionDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'ExceptionDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'LocalizableDTO',

  Instance : {

    initialize : function (obj)
    {
      this.$initialize(obj);
    }
  
  }
});

/**
 * NotificationDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'NotificationDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'LocalizableDTO',
  
  IsAbstract : true,

  Instance : {

    initialize : function (obj)
    {
      this.$initialize(obj);
    }
  
  }
});

/**
 * MessageDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'MessageDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'NotificationDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this.localizedMessage = obj.localizedMessage;
     
    },
  
    getMessage : function() { return this.localizedMessage; }
  
  }
});

/**
 * InformationDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'InformationDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'MessageDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

/**
 * WarningDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'WarningDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'MessageDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

/**
 * ProblemDTO (super-type of all generated problems)
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'ProblemDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'NotificationDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this.localizedMessage = obj.localizedMessage;
      this.developerMessage = obj.developerMessage;
    },
  
    getLocalizedMessage : function() { return this.localizedMessage; },
  
    getMessage : function() { return this.localizedMessage; },
  
    getDeveloperMessage : function() { return this.developerMessage; }
  
  }
});

/**
 * Super-type of all hard-coded exceptions.
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'MojoProblemDTO', {

  Instance : {

    initialize : function(obj)
    {
      this.localizedMessage = obj.localizedMessage;
      this.developerMessage = obj.developerMessage;
    },
  
    getLocalizedMessage : function() { return this.localizedMessage; },
  
    getMessage : function() { return this.localizedMessage; },
  
    getDeveloperMessage : function() { return this.developerMessage; }
  
  }
});

/**
 * AttributeProblemDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'AttributeProblemDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'MojoProblemDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this.componentId = obj.componentId;
      this.definingType = obj.definingType;
      this.definingTypeDisplayLabel = obj.definingTypeDisplayLabel;
      this.attributeName = obj.attributeName;
      this.attributeId = obj.attributeId;
      this.attributeDisplayLabel = obj.attributeDisplayLabel;
    },
  
    getComponentId : function() { return this.componentId; },
  
    getDefiningType : function() { return this.definingType; },
  
    getDefiningTypeDisplayLabel : function() { return this.definingTypeDisplayLabel; },
  
    getAttributeName : function() { return this.attributeName; },
  
    getAttributeId : function() { return this.attributeId; },
  
    getAttributeDisplayLabel : function() { return this.attributeDisplayLabel; }
  
  }
});

/**
 * EmptyValueProblemDTO
 */
Mojo.Meta.newClass(Mojo.ATTRIBUTE_PROBLEM_PACKAGE+'EmptyValueProblemDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'AttributeProblemDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

/**
 * ImmutableAttributeProblemDTO
 */
Mojo.Meta.newClass(Mojo.ATTRIBUTE_PROBLEM_PACKAGE+'ImmutableAttributeProblemDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'AttributeProblemDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

/**
 * SystemAttributeProblemDTO
 */
Mojo.Meta.newClass(Mojo.ATTRIBUTE_PROBLEM_PACKAGE+'SystemAttributeProblemDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'AttributeProblemDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

/**
 * SmartExceptionDTO
 * 
 * (delegates to an ExceptionDTO)
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'SmartExceptionDTO', {

  Extends : Mojo.ROOT_PACKAGE+'Exception',

  Instance : {

    initialize : function (obj)
    {
      this.$initialize(obj);
  
      this.ex = Mojo.Meta.newInstance(Mojo.BUSINESS_PACKAGE+'ExceptionDTO', obj);
    },
  
    getAttributeDTO : function(attributeName){ return this.ex.getAttributeDTO(attributeName); },
  
    getId : function() { return this.ex.getId(); },
  
    getIdMd : function() { return this.ex.getIdMd(); },
  
    getMd : function() { return this.ex.getMd(); },
  
    getType : function() { return this.ex.getType(); },
  
    getTypeMd : function() { return this.ex.getTypeMd(); },
  
    isModified : function() { return this.ex.isModified(); },
  
    isNewInstance : function() { return this.ex.isNewInstance(); },
  
    isReadable : function() { return this.ex.isReadable(); },
  
    isWritable : function() { return this.ex.isWritable(); },
  
    setModified : function(modified) { return this.ex.setModified(modified); }
  
  }
});


/**
 * RunwayExceptioneptionDTO
 * 
 * (for hard-coded exceptions)
 */
Mojo.Meta.newClass(Mojo.ROOT_PACKAGE+'RunwayExceptionDTO', {

  Extends : Mojo.ROOT_PACKAGE+'Exception',

  Instance : {

    initialize : function (obj)
    {
      this.$initialize(obj);
  
      if(Mojo.Util.isString(obj.wrappedException))
      {
        this.wrappedException = obj.wrappedException;
      }
      else
      {
      // final resort. Only the localized message was received
        this.wrappedException = null;
      }
    },
  
    getWrappedException : function() { return this.wrappedException; }
  
  }
});

/**
 * ProblemExceptionDTO
 */
Mojo.Meta.newClass(Mojo.ROOT_PACKAGE+'ProblemExceptionDTO', {
  
  Extends : Mojo.ROOT_PACKAGE+'Exception',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this.problemList = [];
      for(var i=0; i<obj.problemList.length; i++)
      {
        var problemJSON = obj.problemList[i];

        var problem = null;
        if('_type' in problemJSON && Mojo.Meta.classExists(problemJSON._type))
        {
          problem = Mojo.Meta.newInstance(problemJSON._type, problemJSON);
        }
        else if('dto_type' in problemJSON && problemJSON.dto_type === Mojo.BUSINESS_PACKAGE+'AttributeProblemDTO')
        {
          problem = Mojo.Meta.newInstance(Mojo.BUSINESS_PACKAGE+'AttributeProblemDTO', problemJSON);
        }
        else if('dto_type' in problemJSON && problemJSON.dto_type === Mojo.ATTRIBUTE_PROBLEM_PACKAGE+'EmptyValueProblemDTO')
        {
          problem = Mojo.Meta.newInstance(Mojo.ATTRIBUTE_PROBLEM_PACKAGE+'EmptyValueProblemDTO', problemJSON);
        }
        else
        {
          problem = Mojo.Meta.newInstance(Mojo.BUSINESS_PACKAGE+'ProblemDTO', problemJSON);
        }

        this.problemList[i] = problem;
      }
    },
  
    getProblems : function()
    {
      return this.problemList;
    }
  
  }
});

/**
 * SessionDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'SessionDTO', {

  IsAbstract : true,
  
  Extends : Mojo.BUSINESS_PACKAGE+'TransientDTO',

  Instance : {

    initialize : function (obj)
    {
      this.$initialize(obj);
    },
  
    apply : function(clientRequest)
    {
      if(this.isWritable())
      {
        if(this.isNewInstance())
          Facade.createSessionComponent(clientRequest, this);
        else
          Facade.update(clientRequest, this);
      }
    }
  
  }
});

/**
 * UtilDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'UtilDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'SessionDTO',

  Instance : {

    initialize : function (obj)
    {
      this.$initialize(obj);
    }
  
  }
});

/**
 * ViewDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'ViewDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'SessionDTO',

  Instance : {

    initialize : function (obj)
    {
      this.$initialize(obj);
    }
  
  }
});

/**
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'EntityDTO', {

  IsAbstract : true,
  
  Extends : Mojo.BUSINESS_PACKAGE+'MutableDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
    },
  
    remove : function(clientRequest)
    {
      Facade.deleteEntity(clientRequest, this.getId());
    }
  
  }
});

/**
 * ElementDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'ElementDTO', {

  IsAbstract : true,
  
  Extends : Mojo.BUSINESS_PACKAGE+'EntityDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this.lockedByCurrentUser = obj.lockedByCurrentUser;
    },
  
    lock : function(clientRequest)
    {
      Facade.lock(clientRequest, this.getId());
    },
  
    unlock : function(clientRequest)
    {
      Facade.unlock(clientRequest, this.getId());
    },
  
    isLockedByCurrentUser : function() { return this.lockedByCurrentUser; }
  
  },
  
  Static : {

    lock : function(clientRequest, id)
    {
      Facade.lock(clientRequest, id);
    },
  
    unlock : function(clientRequest, id)
    {
      Facade.unlock(clientRequest, id);
    }
  
  }
});


/*
 * BusinessDTO definition
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'BusinessDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'ElementDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this.state = obj.state;
      this.transitions = obj.transitions;
    },
  
    getState : function() { return this.state; },
  
    getTransitions : function() { return this.transitions; },
  
    apply : function(clientRequest)
    {
      if(this.isWritable())
      {
        if(this.isNewInstance())
          Facade.createBusiness(clientRequest, this);
        else
          Facade.update(clientRequest, this);
      }
    }
  
  }
});

/**
 * EnumerationDTO.
 * 
 * Parent class of generated MdEnumerations.
 */
Mojo.Meta.newClass(Mojo.ROOT_PACKAGE+'EnumerationDTO', {

  IsAbstract : true,
  
  Instance : {

    initialize : function(obj)
    {
      this.dto_type = obj.dto_type;
      this.enumType = obj.enumType;
      this._name = obj._name;
      this.displayLabel = obj.displayLabel;
    },
  
    name : function () { return this._name; },
  
    getDisplayLabel : function() { return this.displayLabel; },
    
    toJSON : function(key)
    {
      return new Mojo.$.com.runwaysdk.StandardSerializer(this).toJSON(key);
    }
  
  }
});

/*
 * RelationshipDTO definition
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'RelationshipDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'ElementDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this._typeMd = Mojo.Meta.newInstance(Mojo.MD_DTO_PACKAGE+'RelationshipMd', obj._relationshipMd);
      this.parentId = obj.parentId;
      this.childId = obj.childId;
    },
  
    getParentId : function() { return this.parentId; },
  
    getChildId : function() { return this.childId; },
  
    apply : function(clientRequest)
    {
      if(this.isWritable())
      {
        if(this.isNewInstance())
          Facade.createRelationship(clientRequest, this);
        else
          Facade.update(clientRequest, this);
      }
    },
  
    getParent : function(clientRequest)
    {
      Facade.get(clientRequest, this.getParentId());
    },
  
    getChild : function(clientRequest)
    {
      Facade.get(clientRequest, this.getChildId());
    }

  }
});

/**
 * StructDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'StructDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'EntityDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
    },
  
    apply : function(clientRequest)
    {
      if(this.isWritable())
      {
        if(this.isNewInstance())
          Facade.createStruct(clientRequest, this);
        else
          Facade.update(clientRequest, this);
      }
    }
  
  }
});

/**
 * LocalStructDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'LocalStructDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'StructDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
      
      this.localizedValue = obj.localizedValue;
    },
    
    getLocalizedValue : function() {
      return this.localizedValue;
    }
  
  }
});

/**
 * LocalStructQueryDTO
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'LocalStructQueryDTO', {

  Extends : Mojo.BUSINESS_PACKAGE+'StructQueryDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

/*
 * Attribute definitions
 */

// attribute
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDTO', {

  IsAbstract : true,

  Instance : {
  
    initialize : function(obj)
    {
      this.attributeName = obj.attributeName;
      this.type = obj.type;
      this.dtoType = obj.dtoType;
      this.value = obj.value;
      this.readable = obj.readable;
      this.writable = obj.writable;
      this.modified = obj.modified;
      
      // instantiate the MdDTO version of this attribute
      var mdDtoType = this.getMetaClass().getName().replace('DTO', 'MdDTO');
      var mdKlass = Mojo.Meta.findClass(Mojo.MD_DTO_PACKAGE+ mdDtoType);

      if(!mdKlass)
      {
        mdKlass = Mojo.Meta.findClass("com.runwaysdk.gis.transport.metadata."+ mdDtoType);
      }
      this.attributeMdDTO = new mdKlass(obj.attributeMdDTO);
    },
  
    getName : function() { return this.attributeName; },
  
    getValue : function() { return this.value; },
    
    getType : function() { return this.type; },
  
    setValue : function(value)
    {
      if(this.isWritable())
      {
        // null and undefined should be null
        this.value = value != null ? value : null;
        this.setModified(true);
      }
    },
  
    isReadable : function() { return this.readable; },
  
    isWritable : function() { return this.writable; },
  
    isModified : function() { return this.modified; },
  
    setModified : function(modified) { this.modified = modified; },
  
    getAttributeMdDTO : function() { return this.attributeMdDTO; },
    
    toJSON : function(key)
    {
      return new Mojo.$.com.runwaysdk.StandardSerializer(this).toJSON(key);
    }
    
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeMdDTO', {

  IsAbstract : true,

  Instance : {

    initialize : function(obj)
    {
      this.displayLabel = obj.displayLabel;
      this.description = obj.description;
      this.required = obj.required;
      this.immutable = obj.immutable;
      this.columnName = obj.columnName;
      this.id = obj.id;
      this.system = obj.system;
      this.name = obj.name;
    },
  
    getDisplayLabel : function() { return this.displayLabel; },
  
    getDescription : function() { return this.description; },
  
    isRequired : function() { return this.required; },
  
    isImmutable : function() { return this.immutable; },
  
    getId : function() { return this.id; },
  
    isSystem : function() { return this.system; },
  
    getName : function() { return this.name; },
  
    getColumnName : function() { return this.columnName; },
    
    toJSON : function() { return undefined; }
  
  }
});

// number
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeNumberDTO', {

  IsAbstract : true,

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDTO',
  
  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
    }
    
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeNumberMdDTO', {

  IsAbstract : true,
  
  Extends : Mojo.MD_DTO_PACKAGE+'AttributeMdDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this._rejectZero = obj._rejectZero;
      this._rejectNegative = obj._rejectNegative;
      this._rejectPositive = obj._rejectPositive;
  
    },
  
    rejectZero : function() { return this._rejectZero; },
  
    rejectNegative : function() { return this._rejectNegative; },
  
    rejectPositive : function() { return this._rejectPositive; }
  
  }
});

// integer
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeIntegerDTO', {

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeNumberDTO',
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeIntegerMdDTO', {

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeNumberMdDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

// long
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeLongDTO', {

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeNumberDTO',
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeLongMdDTO', {

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeNumberMdDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

// dec
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDecDTO', {

  IsAbstract : true,

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeNumberDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeDecMdDTO', {

  IsAbstract : true,

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeNumberMdDTO',
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this.totalLength = obj.totalLength;
      this.decimalLength = obj.decimalLength;
    },
  
    getTotalLength : function() { return this.totalLength; },
  
    getDecimalLength : function() { return this.decimalLength; }
  
  }
});

// decimal
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDecimalDTO', {

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDecDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeDecimalMdDTO', {

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeDecMdDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

// double
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDoubleDTO', {

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDecDTO',
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeDoubleMdDTO', {

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeDecMdDTO',
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

// float
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeFloatDTO', {

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDecDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeFloatMdDTO', {

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeDecMdDTO',
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

// text
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeTextDTO', {

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDTO',
  
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeTextMdDTO',        {

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeMdDTO',
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

// clob
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeClobDTO', {
  
  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDTO',
  
  
  Instance : {
  
  initialize : function(obj)
  {
  this.$initialize(obj);
  }

}
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeClobMdDTO',        {
  
  Extends : Mojo.MD_DTO_PACKAGE+'AttributeMdDTO',
  
  Instance : {
  
  initialize : function(obj)
  {
  this.$initialize(obj);
  }

}
});

// local text
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeLocalTextDTO', {
  
  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDTO',
  
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
      this.structDTO = obj.structDTO;
    },
    getStructDTO : function(){ return this.structDTO; },
    setStructDTO : function(structDTO){ this.structDTO = structDTO; }
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeLocalTextMdDTO',        {
  
  Extends : Mojo.MD_DTO_PACKAGE+'AttributeMdDTO',
  
  Instance : {
  
  initialize : function(obj)
  {
    this.$initialize(obj);
  }

}
});

// character
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeCharacterDTO', {

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeCharacterMdDTO', {

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeMdDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this.size = obj.size;
    },
  
    getSize : function() { return this.size; }
  
  }
});

// local character
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeLocalCharacterDTO', {

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
      this.structDTO = obj.structDTO;
    },
    getStructDTO : function(){ return this.structDTO; },
    setStructDTO : function(structDTO){ this.structDTO = structDTO; }
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeLocalCharacterMdDTO', {

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeMdDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this.size = obj.size;
    },
  
    getSize : function() { return this.size; }
  
  }
});

// boolean
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeBooleanDTO', {

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDTO',
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeBooleanMdDTO', {

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeMdDTO',
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
      
      this._positiveDisplayLabel = obj.positiveDisplayLabel;
      this._negativeDisplayLabel = obj.negativeDisplayLabel;
    },
    
    getPositiveDisplayLabel : function() { return this._positiveDisplayLabel; },
  
    getNegativeDisplayLabel : function() { return this._negativeDisplayLabel; }
  
  }
});

// struct
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeStructDTO', {

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDTO',
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this.structDTO = obj.structDTO;
    },
  
    getStructDTO : function()
    {
      return this.structDTO;
    },
    
    /**
   * Should only be called when setting the type-safe version of the struct.
   */
    setStructDTO : function(structDTO)
    {
      this.structDTO = structDTO;
    }
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeStructMdDTO', {

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeMdDTO',
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this.definingMdStruct = obj.definingMdStruct;
    },
  
    getDefiningMdStruct : function() { return this.definingMdStruct; }
  
  }
});

// moment
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeMomentDTO', {

  IsAbstract : true,

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDTO',

  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this._ignoreTimezone = false;
      
      // set internal value as a date
      if(this.value != null && this.value !== '')
      {
        var date = new Date();
        var ignoreTimezone = this.getIgnoreTimezone();          
        Mojo.Util.setISO8601(date, this.value, ignoreTimezone);
 
        this.value = date;
      }
      else
      {
        this.value = null;
      }
    },
    
    setIgnoreTimezone : function(ignoreTimezone)
    {
      this._ignoreTimezone = ignoreTimezone;
    },
    
    getIgnoreTimezone : function()
    {
      return this._ignoreTimezone;
    },
  
    setValue : function(value)
    {
      if(this.isWritable())
      {
        if(value != null)
        {
          if(Mojo.Util.isDate(value))
          {
            this.value = value;          
          }
          else
          {
            var date = new Date();
            
            var ignoreTimezone = this.getIgnoreTimezone();
            Mojo.Util.setISO8601(date, value, ignoreTimezone);
  
            this.value = date;
          }
        }
        else
        {
          this.value = null;
        }
  
        this.setModified(true);
      }
    },
    
    toJSON : function(key)
    {
      var date = this.getValue();
      if(Mojo.Util.isDate(date))
      {
        date = Mojo.Util.toISO8601(date, this.getIgnoreTimezone());
      }
      
      var override = {value:date};
      return new Mojo.$.com.runwaysdk.StandardSerializer(this, override).toJSON(key);
    }
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeMomentMdDTO', {

  IsAbstract : true,

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeMdDTO',
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

// datetime
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDateTimeDTO', {

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeMomentDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeDateTimeMdDTO', {

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeMomentMdDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

// date
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDateDTO', {

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeMomentDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
      
      this.setIgnoreTimezone(true);
    }
  
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeDateMdDTO', {

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeMomentMdDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

// time
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeTimeDTO', {

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeMomentDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeTimeMdDTO', {

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeMomentMdDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

// reference object
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeReferenceDTO', {

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeReferenceMdDTO', {

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeMdDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this.referencedMdBusiness = obj.referencedMdBusiness;
    },
  
    getReferencedMdBusiness : function() { return this.referencedMdBusiness; }
  
  }
});

// enumeration
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeEnumerationDTO', {

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
  
      // javascript doesn't have a set, so use a hash with key == value.
      this.enumNames = {};
      for(var i=0; i<obj.enumNames.length; i++)
      {
        var enumName = obj.enumNames[i];
        this.enumNames[enumName] = enumName;
      }
    },
  
    add : function(item)
    {
      if(this.isWritable())
      {
        if(!this.getAttributeMdDTO().selectMultiple())
        {
          this.clear();
        }
  
        var enumName = Mojo.Util.isObject(item) ? item.name() : item;
        this.enumNames[enumName] = enumName;
      }
    },
  
    remove : function(item)
    {
      if(this.isWritable())
      {
        var enumName = Mojo.Util.isObject(item) ? item.name() : item;
        delete this.enumNames[enumName];
      }
    },
  
    clear : function()
    {
      if(this.isWritable())
      {
        this.enumNames = {};
      }
    },
  
    getEnumNames : function()
    {
      return Mojo.Util.getKeys(this.enumNames);
    },
  
    getEnumValues : function(clientRequest)
    {
      var enumType = this.getAttributeMdDTO().getReferencedMdEnumeration();
      var names = Mojo.Util.getKeys(this.enumNames);
      Facade.getEnumerations(clientRequest, enumType, names);
    }
  
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeEnumerationMdDTO', {

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeMdDTO',
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this._selectMultiple = obj._selectMultiple;
      this.referencedMdEnumeration = obj.referencedMdEnumeration;
 
      this.enumNames = {}; // key/value = name/display label
      Mojo.Util.copy(obj.enumNames, this.enumNames);
    }, 
  
    getReferencedMdEnumeration : function() { return this.referencedMdEnumeration; },
  
    selectMultiple : function() { return this._selectMultiple; },
  
    getEnumNames : function()
    {
      return Mojo.Util.getKeys(this.enumNames);
    },
  
    getEnumLabels : function()
    {
      return Mojo.Util.getValues(this.enumNames);
    },
  
    getEnumDisplayLabel : function(enumName)
    {
      return this.enumNames[enumName];
    },
  
    getEnumItems : function()
    {
      var copy = {};
      Mojo.Util.copy(this.enumNames, cop);
      return copy;
    }
  
  }
});

//multi reference
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeMultiReferenceDTO', {
        
        Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDTO',
        
          Instance : {
                    
            initialize : function(obj)
            {
              this.$initialize(obj);
                  
              // javascript doesn't have a set, so use a hash with key == value.
              this.itemIds = {};
                      
              for(var i=0; i<obj.itemIds.length; i++)
              {
                var itemId = obj.itemIds[i];
                this.itemIds[itemId] = itemId;
              }
            },
                  
            add : function(item)
            {
              if(this.isWritable())
              {
                var itemId = Mojo.Util.isObject(item) ? item.getId() : item;
                this.itemIds[itemId] = itemId;
              }
            },
                  
            remove : function(item)
            {
              if(this.isWritable())
              {
                var itemId = Mojo.Util.isObject(item) ? item.getId() : item;
                delete this.itemIds[itemId];
              }
            },
                  
            clear : function()
            {
              if(this.isWritable())
              {
                this.itemIds = {};
              }
            },
                  
            getItemIds : function()
            {
              return Mojo.Util.getKeys(this.itemIds);
            }                  
        }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeMultiReferenceMdDTO', {
        
        Extends : Mojo.MD_DTO_PACKAGE+'AttributeMdDTO',
        
        Instance : {
                    
                    initialize : function(obj)
                    {
                      this.$initialize(obj);
                  
                      this.referencedMdBusiness = obj.referencedMdBusiness;
                    },
                  
                    getReferencedMdBusiness : function() { return this.referencedMdBusiness; }
        }
});

// multi term
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeMultiTermDTO', {
        
        Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeMultiReferenceDTO',
        
        Instance : {
                
                initialize : function(obj)
                {
                        this.$initialize(obj);
                }
        }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeMultiTermMdDTO', {
        
        Extends : Mojo.MD_DTO_PACKAGE+'AttributeMultiReferenceMdDTO',
        
        Instance : {
                
                initialize : function(obj)
                {
                        this.$initialize(obj);
                },
        }                
});

// encryption
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeEncryptionDTO', {

  IsAbstract : true,

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeEncryptionMdDTO', {

  IsAbstract : true,

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeMdDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this.encryptionMethod = obj.encryptionMethod;
    },
  
    getEncryptionMethod : function() { return this.encryptionMethod; }
  
  }
});

// hash
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeHashDTO', {

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeEncryptionDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeHashMdDTO', {

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeEncryptionMdDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

// symmetric
Mojo.Meta.newClass(Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeSymmetricDTO', {

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeEncryptionDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'AttributeSymmetricMdDTO', {

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeEncryptionMdDTO',
  
  Instance : {
    
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
    
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'TypeMd', {

  Instance : {

    initialize : function(obj)
    {
      this.displayLabel = obj.displayLabel;
      this.description = obj.description;
      this.id = obj.id;
    },
  
    getDisplayLabel : function() {return this.displayLabel;},
  
    getDescription : function() {return this.description;},
  
    getId : function() {return this.id;},
    
    toJSON : function() { return undefined; }
  
  }
});

Mojo.Meta.newClass(Mojo.MD_DTO_PACKAGE+'RelationshipMd', {

  Extends : Mojo.MD_DTO_PACKAGE+'TypeMd',
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
  
      this.parentMdBusiness = obj.parentMdBusiness;
      this.childMdBusiness = obj.childMdBusiness;
    },
  
    getParentMdBusiness : function() { return this.parentMdBusiness; },
  
    getChildMdBusiness  : function() { return this.childMdBusiness; }
  
  }
});

/*
 * TermDTO definition
 */
Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'TermDTO', {

  IsAbstract : true,
	
  Extends : Mojo.BUSINESS_PACKAGE+'BusinessDTO',

  Instance : {

    initialize : function(obj)
    {
      this.$initialize(obj);
    },
    
    addChild : function(child, relationshipType) {
       var params = {
              'method' : 'addChild',
              'parentId' : this.getId(),
              'childId' : child.getId(),
              'relationshipType' : relationshipType};
      
       new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    },
    
    getParents : function(relationshipType) {
      var params = {
            'method' : 'getParents',
            'childId' : this.getId(),
            'relationshipType' : relationshipType};
     
      new RunwayRequest(Mojo.JSON_ENDPOINT, clientRequest, params).apply();
    }
  
  }
});

Mojo.Meta.newClass(Mojo.BUSINESS_PACKAGE+'TermRelationshipDTO', {

	  IsAbstract : true,
	  
	  Extends : Mojo.BUSINESS_PACKAGE+'RelationshipDTO',

	  Instance : {

	    initialize : function(obj)
	    {
	      this.$initialize(obj);
	    }
	  
	  }
});

Mojo.Meta.newClass('com.runwaysdk.business.ontology.TermAndRel', {
  
  IsAbstract : false,
  
  Instance : {

    initialize : function(obj)
    {
      this._term = DTOUtil.convertToType(obj.term);
      this._relType = obj.relType;
      this._relId = obj.relId;
      this._dto_type = obj.dto_type;
    },
    
    getTerm : function() {
      return this._term;
    },
    
    getRelationshipType : function() {
      return this._relType;
    },
    
    getRelationshipId : function() {
      return this._relId;
    },
    
    getType : function() {
      return this._dto_type;
    }
    
  }
});

})();