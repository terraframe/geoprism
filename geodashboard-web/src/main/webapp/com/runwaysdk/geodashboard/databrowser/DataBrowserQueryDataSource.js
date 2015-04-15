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
  var queryDataSource = ClassFramework.newClass('com.runwaysdk.geodashboard.databrowser.DataBrowserQueryDataSource', {
    
    Extends : ServerDataSource,
    
    Instance : {
      initialize : function(config) {
        this.$initialize(config);
        
        Util.requireParameter("className", config.className);
        
        if (config.columns == null && !config.readColumnsFromMetadata) {
          throw new com.runwaysdk.Exception("InstanceQueryDataSource has not been configured correctly. Either a columns array of objects or a boolean flag readColumnsFromMetadata (set to true) must be present.");
        }
        
        this._config = config;
        
        this._type = config.className;
        this._metadataQueryDTO = null;
        this._resultsQueryDTO = null;
        this._requestEvent = null;
        this._attributeNames = [];
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
        
        // 1. Fetch the QueryDTO with metadata for the DataSchema
        taskQueue.addTask(new STRUCT.TaskIF({
          start : function(){
            that._getQueryDTO(myCallback);
          }
        }));
        
        // 2. Calculate what the column headers are for display in the table.
        taskQueue.addTask(new STRUCT.TaskIF({
          start : function(){
            var colArr = [];
            
            // They didn't tell us what attrs to use, just use everything that makes sense.
            if (that._config.readColumnsFromMetadata) {
              var names = that._metadataQueryDTO.getAttributeNames();
              var RefDTO = com.runwaysdk.transport.attributes.AttributeReferenceDTO;
              var StructDTO = com.runwaysdk.transport.attributes.AttributeStructDTO;
              
              that._config.columns = that._config.columns || [];
              for(var i=0; i< names.length; i++)
              {
                var name = names[i];
                var attrDTO = that._metadataQueryDTO.getAttributeDTO(name);
                
                // TODO Custom filter needed. Should only allow primitives based on IF
                if(!(attrDTO instanceof RefDTO) && !(attrDTO instanceof StructDTO)
                  && !attrDTO.getAttributeMdDTO().isSystem() && name !== 'keyName'
                  && attrDTO.isReadable()) {
                  var type = attrDTO.getType();
                  
                  if(that._config.formatters != null && that._config.formatters[type] != null) {
                    var formatter = that._config.formatters[type];
                    
                    that._config.columns.push({queryAttr:name, customFormatter: formatter});                    
                  }
                  else {
                    that._config.columns.push({queryAttr:name});                    
                  }
                }
              }
            }
            
            for (var i = 0; i < that._config.columns.length; ++i) {
              var header = that._config.columns[i].header;
              var queryAttr = that._config.columns[i].queryAttr;
              
              if (header != null) {
                colArr.push(that._config.columns[i].header);
              }
              else if (queryAttr != null) {
                var attrDTO = that._metadataQueryDTO.getAttributeDTO(queryAttr);
                
                if (attrDTO == null) {
                  var ex = new com.runwaysdk.Exception("The type '" + that._type + "' has no attribute named '" + queryAttr + "'.");
                  callback.onFailure(ex);
                  return;
                }
                
                var label = attrDTO.getAttributeMdDTO().getDisplayLabel();
                colArr.push(label);
              }
              else {
                var ex = new com.runwaysdk.Exception("Configuration error, all column objects must provide either a header or a queryAttr or both.");
                callback.onFailure(ex);
                return;
              }
            }
            
            if (colArr.length == 0) {
              throw new com.runwaysdk.Exception("At least one column is required.");
            }
            
            // Sort the columns
            if (that._config.readColumnsFromMetadata) {
              for (var i = 0; i < colArr.length; ++i) {
                that._config.columns[i].displayLabel = colArr[i];
              }
              
              colArr.sort(function(a,b){
                return a.localeCompare(b);
              });
              
              that._config.columns.sort(function(a,b){
                return a.displayLabel.localeCompare(b.displayLabel);
              });
            }
            
            that.setColumns(colArr);
            taskQueue.next();
          }
        }));
        
        // 3. Fetch required classes if they're not loaded.
        taskQueue.addTask(new STRUCT.TaskIF({
          start : function() {
            that._loadClass(callback);
          }
        }));
        
        taskQueue.start();
      },
      
      // OVERRIDE
      performRequest: function(callback) {
        
        this.beforePerformRequest(callback);
        
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
        
        if (!ClassFramework.classExists(this._type)) {
          var thisDS = this;
          var clientRequest = new Mojo.ClientRequest({
            onSuccess : function(){
              callback.onSuccess();
            },
            onFailure : function(e){
              callback.onFailure(e);
            }
          });
          
          var types = [this._type];
                    
          com.runwaysdk.Facade.importTypes(clientRequest, types, {autoEval : true});
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
          
          var attributeDTO = this._metadataQueryDTO.getAttributeDTO(sortAttribute);
          
          if((attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO) || (attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeLocalTextDTO))
          {
            this._metadataQueryDTO.addStructOrderBy(sortAttribute, 'LOCALIZE', this.isAscending() ? 'asc' : 'desc');                      
          }
          else if(attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeStructDTO)
          {
            this._metadataQueryDTO.addStructOrderBy(sortAttribute, 'id', this.isAscending() ? 'asc' : 'desc');
          }
          else if ((attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeMomentDTO) || (attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeBooleanDTO) || (attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeCharacterDTO) || (attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeTextDTO)  || (attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeNumberDTO))
          {
            this._metadataQueryDTO.addOrderBy(sortAttribute, this.isAscending() ? 'asc' : 'desc');          
          }
          else
          {
            console.log("ERROR: Unable to sort by non-primitive attribute [" + sortAttribute + "].");
          }
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
            if (queryAttr != null) {
              
              var attrDTO = result.getAttributeDTO(queryAttr);
              
              if (attrDTO instanceof com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO) {
                value = attrDTO.getStructDTO()._toString || "";
              }
              else {
                value = attrDTO.getValue(); 
              }
            }
            
            if (customFormatter != null) {
              value = customFormatter(value);
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