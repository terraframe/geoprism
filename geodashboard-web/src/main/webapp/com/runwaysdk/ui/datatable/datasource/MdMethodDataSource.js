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
   * @class com.runwaysdk.ui.datatable.datasource.MdMethodDataSource A data source for data tables that performs a request on an MdMethod and provides the populates the table
   *    from the result. This is especially helpful when working with views.
   * 
   * @constructs
   * @param obj
   *   @param com.runwaysdk.business.BusinessQueryDTO queryDTO The query dto to display data from.
   */
  var methodDataSource = ClassFramework.newClass('com.runwaysdk.ui.datatable.datasource.MdMethodDataSource', {
    
    Extends : ServerDataSource,
    
    Instance : {
      initialize : function(config) {
        
        this.$initialize(config); // Supering is important
        
        this._config = config;
      },
      // OVERRIDE
      initialSetup : function(callback) {
        
        this.$initialSetup(); // Supering is important
        
        this._doQuery(callback);
      },
      // OVERRIDE
      performRequest: function(callback) {
        this._doQuery(callback);
      },
      
      getSortAttr : function() {
        return this._config.columns[this.getSortColumn()].queryAttr;
      },
      
      _doQuery : function(callback) {
        var that = this;
        
        this._config.method.call(this, new Mojo.ClientRequest({
          onSuccess : function(view) {
            callback.onSuccess(that._onQuerySuccess(view));
          },
          onFailure : function(ex) {
            that.handleException(ex);
            callback.onFailure(ex);
          }
        }));
      },
      
      _onQuerySuccess : function(view) {
        var resultSet = view.getResultSet();
        
        var retVal = [];
        var colArr = [];
        
        // calculate column headers
        for (var i=0; i < this._config.columns.length; ++i) {
          var header = this._config.columns[i].header;
          var queryAttr = this._config.columns[i].queryAttr;
          
          if (header != null) {
            colArr.push(this._config.columns[i].header);
          }
          else if (queryAttr != null) {
            var attrDTO = view.getAttributeDTO(queryAttr);
            if (attrDTO == null) {
              var ex = new com.runwaysdk.Exception("[MdMethodDataSource] The type '" + this._type + "' has no attribute named '" + queryAttr + "'.");
              callback.onFailure(ex);
              return;
            }
            colArr.push(attrDTO.getAttributeMdDTO().getDisplayLabel());
          }
          else {
            var ex = new com.runwaysdk.Exception("[MdMethodDataSource] Configuration error, all column objects must provide either a header or a queryAttr or both.");
            callback.onFailure(ex);
            return;
          }
        }
        this.setColumns(colArr);
        
        // Build an array of [row][columnData] from the result set
        for(var i=0; i<resultSet.length; i++)
        {
          var result = resultSet[i];
          
          var row = [];
          for (var j=0; j < this._config.columns.length; ++j) {
            var queryAttr = this._config.columns[j].queryAttr;
            var customFormatter = this._config.columns[j].customFormatter;
            
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
            row.push(value);
          }
          
          retVal.push(row);
        }
        
        this.setTotalResults(view.getCount());
        
        return retVal;
      }
    }
  });
})();
