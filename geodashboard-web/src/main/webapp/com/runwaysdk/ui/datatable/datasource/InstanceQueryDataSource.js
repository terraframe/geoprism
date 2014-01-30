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

//define(["../../../ClassFramework", "../../../Util", "../../factory/generic/datatable/datasource/ServerDataSource"], function(ClassFramework, Util, ServerDataSource) {
(function(){

  var ClassFramework = Mojo.Meta;
  var Util = Mojo.Util;
  var ServerDataSource = com.runwaysdk.ui.factory.generic.datatable.datasource.ServerDataSource;
  
  var RW = ClassFramework.alias(Mojo.RW_PACKAGE + "*");
  var UI = ClassFramework.alias(Mojo.UI_PACKAGE + "*");
  var STRUCT = ClassFramework.alias(Mojo.STRUCTURE_PACKAGE + "*");
  
  /**
   * @class com.runwaysdk.ui.datatable.InstanceQueryDataSource A data source for data tables that reads instance data from a query dto.
   * 
   * @constructs
   * @param obj
   *   @param com.runwaysdk.business.BusinessQueryDTO queryDTO The query dto to display data from.
   */
  var queryDataSource = ClassFramework.newClass('com.runwaysdk.ui.datatable.datasource.InstanceQueryDataSource', {
    
    Extends : ServerDataSource,
    
    Instance : {
      initialize : function(config) {
        this.$initialize(config);
        
        Util.requireParameter("className [QueryDataSource]", config.className);
        Util.requireParameter("columns [QueryDataSource]", config.columns);
        
        this._config = config;
        
        this._type = config.className;
        this._metadataQueryDTO = null;
        this._resultsQueryDTO = null;
        this._requestEvent = null;
        this._attributeNames = [];
        
        // FOR TESTING ERR0RS ONLY
//        this._requests = 0;
      },
      
      getSortAttr : function() {
        return this._config.columns[this.getSortColumn()].queryAttr;
      },
      
      // OVERRIDE
      initialSetup : function(callback) {
        
        this.$initialSetup();
        
        var taskQueue = new STRUCT.TaskQueue();
        var that = this;
        
        var myCallback = {
          onSuccess : function(paramQueryDTO) {
            taskQueue.next();
          },
          onFailure: callback.onFailure
        };
        
        // 1. Fetch required classes if they're not loaded.
        taskQueue.addTask(new STRUCT.TaskIF({
          start : function(){
            that._loadClass(myCallback);
          }
        }));
        
        // 2. Fetch the QueryDTO with metadata for the DataSchema
        taskQueue.addTask(new STRUCT.TaskIF({
          start : function(){
            that._getQueryDTO(myCallback);
          }
        }));
        
        taskQueue.addTask(new STRUCT.TaskIF({
          start : function(){
            // 3. Calculate what the column headers are for display in the table.
            var colArr = [];
            
            for (var i = 0; i < that._config.columns.length; ++i) {
              var header = that._config.columns[i].header;
              var queryAttr = that._config.columns[i].queryAttr;
              
              if (header != null) {
                colArr.push(that._config.columns[i].header);
              }
              else if (queryAttr != null) {
                var attrDTO = that._metadataQueryDTO.getAttributeDTO(queryAttr);
                
                if (attrDTO == null) {
                  var ex = new com.runwaysdk.Exception("[InstanceQueryDataSource] The type '" + that._type + "' has no attribute named '" + queryAttr + "'.");
                  callback.onFailure(ex);
                  return;
                }
                
                colArr.push(attrDTO.getAttributeMdDTO().getDisplayLabel());
              }
              else {
                var ex = new com.runwaysdk.Exception("[InstanceQueryDataSource] Configuration error, all column objects must provide either a header or a queryAttr or both.");
                callback.onFailure(ex);
                return;
              }
            }
            
            that.setColumns(colArr);
            callback.onSuccess();
          }
        }));
        
        taskQueue.start();
      },
      
      // OVERRIDE
      performRequest: function(callback) {
        
        this.beforePerformRequest(callback);
        
//        // FOR TESTING ERR0RS ONLY
//        this._requests++;
//        if (this._requests > 5) {
//          callback.onFailure(new com.runwaysdk.Exception("Requests exceeded 5."));
//          return;
//        }
        
        var thisDS = this;
        
        var taskQueue = new STRUCT.TaskQueue();
        
        var myCallback = {
          onSuccess : function() {
            taskQueue.next();
          },
          onFailure: callback.onFailure
        };
        
        // 1. Perform the query and get a result set
        taskQueue.addTask(new STRUCT.TaskIF({
          start : function() {
            thisDS._performQuery(myCallback);
          }
        }));
        
        // 2. Finalize the request and invoke the callback
        taskQueue.addTask(new STRUCT.TaskIF({
          start : function() {
            var json = thisDS._finalizeRequest();
            
            thisDS.afterPerformRequest(json);
            
            callback.onSuccess(json);
          }
        }));
        
        taskQueue.start();
      },
      
      _loadClass : function(callback) {
        
        if(!ClassFramework.classExists(this._type)) {
          var ex = new com.runwaysdk.Exception('Operation to lazy load DataSource type ['+this._type+'] is not supported.');
          callback.onFailure(ex);
          
//          var thisDS = this;
//          var clientRequest = new Mojo.ClientRequest({
//            onSuccess : function(){
//              thisDS._taskQueue.next();
//            },
//            onFailure : function(e){
//              callback.onFailure(e);
//            }
//          });
//          
//          com.runwaysdk.Facade.importTypes(clientRequest, [this._type], {autoEval : true});
        }
        else {
          callback.onSuccess();
        }
      },

      _getQueryDTO : function(callback) {
        var thisDS = this;
        
        var clientRequest = new Mojo.ClientRequest({
          onSuccess : function(queryDTO) {
            thisDS._metadataQueryDTO = queryDTO;
            callback.onSuccess(queryDTO);
          },
          onFailure : function(e) {
            callback.onFailure(e);
          }
        });
        
        com.runwaysdk.Facade.getQuery(clientRequest, this._type);
      },
      
      _performQuery : function(callback) {
      
        var thisDS = this;
        
        var clientRequest = new Mojo.ClientRequest({
          onSuccess : function(queryDTO){
            thisDS._resultsQueryDTO = queryDTO;
            callback.onSuccess(queryDTO);
          },
          onFailure : function(e){
            callback.onFailure(e);
          }
        });
        
        // setup the query parameters
        this._metadataQueryDTO.setCountEnabled(true);
        this._metadataQueryDTO.setPageSize(this._pageSize);
        this._metadataQueryDTO.setPageNumber(this._pageNumber);
        
        var sortAttribute = this.getSortAttr();
        if(Mojo.Util.isString(sortAttribute)) {
          this._metadataQueryDTO.clearOrderByList();
          this._metadataQueryDTO.addOrderBy(sortAttribute, this.isAscending() ? 'asc' : 'desc');
        }
        
        com.runwaysdk.Facade.queryEntities(clientRequest, this._metadataQueryDTO);
      },
      
      _finalizeRequest : function() {
        
        var thisDS = this;
        
        // convert each DTO into an object literal
        var json = [];
        var resultSet = this._resultsQueryDTO.getResultSet();   
        for(var i=0; i<resultSet.length; i++)
        {
          var result = resultSet[i];
          
          var obj = [];
          
          for(var j = 0; j < thisDS._config.columns.length; j++) {
            var queryAttr = thisDS._config.columns[j].queryAttr;
            var customFormatter = thisDS._config.columns[j].customFormatter;
            
            var value = "";
            if (customFormatter != null) {
              value = customFormatter(result);
            }
            else if (queryAttr != null) {
              
              if (queryAttr === "displayLabel") {
                value = result.getDisplayLabel().getLocalizedValue();
              }
              else {
                value = result.getAttributeDTO(queryAttr).getValue();
              }
            }
            
            value = value != null ? value : '';
            obj.push(value);
          }
          
          json.push(obj);
        }
        
        this.setTotalResults(this._resultsQueryDTO.getCount());
        
        return json;
      },
      
      getResultsQueryDTO : function() {
        return this._resultsQueryDTO;
      },
      
      getMetadataQueryDTO : function() {
        return this._metadataQueryDTO;
      },
      
      // OVERRIDE
      reset : function() {
        this.$reset();
        
        if(this._resultsQueryDTO !== null)
        {
          this._resultsQueryDTO.clearOrderByList();
        }
      }
    }
  });
  
  return queryDataSource;
})();