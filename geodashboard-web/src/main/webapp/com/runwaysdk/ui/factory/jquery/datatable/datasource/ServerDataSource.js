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

//define(["../../../../../ClassFramework", "../../../../../Util", "../../../../RunwaySDK_UI", "../../../generic/datatable/datasource/BaseServerDataSource"], function(ClassFramework, Util, Runway_UI, BaseServerDataSource) {
(function(){

  var ClassFramework = Mojo.Meta;
  var Util = Mojo.Util;
  var BaseServerDataSource = com.runwaysdk.ui.factory.generic.datatable.datasource.BaseServerDataSource;
  
  var RW = ClassFramework.alias(Mojo.RW_PACKAGE + "*");
  var UI = ClassFramework.alias(Mojo.UI_PACKAGE + "*");
  var STRUCT = ClassFramework.alias(Mojo.STRUCTURE_PACKAGE + "*");
  
  var serverDataSource = ClassFramework.newClass(Mojo.JQUERY_PACKAGE+'datatable.datasource.ServerDataSource', {
    
    Extends : BaseServerDataSource,
    
    Instance : {
      initialize : function(config) {
        config = config || {};
        
        this.$initialize(config);
        
        this._config = config;
        
        this._genericDataSource = config.genericDataSource;
        this._iDisplayStart = 0;
      },
      
      getConfig : function() {
        
        return {
          "bProcessing": false,
          "bServerSide": true,
          "sAjaxSource": "",
          "fnServerData": Util.bind(this, this.__fnServerData),
          aoColumns: this.getColumns()
        };
      },
      
      setDataTablesCallback : function(callback) {
        var didInvoke = false;
        var thisDataSource = this;
        var hisCallback = callback;
        this._datatablesCallback = {
          onFailure: function(ex) {
            if (!didInvoke) {
              didInvoke = true;
              hisCallback.onFailure(ex);
            }
            else {
              thisDataSource.handleException(ex);
            }
          },
          onSuccess : function(data) {
            if (!didInvoke) {
              didInvoke = true;
              hisCallback.onSuccess(data);
            }
          }
        }
      },
      
      /**
       * This method is called when the datatables.net widget wants data from the server.
       */
      __fnServerData : function(sSource, aoData, fnCallback, oSettings) {
        var displayStart;
        var displayLen;
        var sEcho;
        for (var i = 0; i < aoData.length; ++i) {
          if (aoData[i].name === "iDisplayLength") {
            displayLen = aoData[i].value;
          }
          else if (aoData[i].name === "iDisplayStart") {
            displayStart = aoData[i].value;
          }
          else if (aoData[i].name === "sEcho") {
            sEcho = aoData[i].value;
          }
          
          if (displayStart != null && displayLen != null) { break; }
        }
        
        this._sSource = sSource;
        this._sEcho = sEcho;
        this._oSettings = oSettings;
        
        this._genericDataSource.setPageNumber(displayStart / displayLen + 1);
        this._genericDataSource.setPageSize(displayLen);
        this._genericDataSource.setSortColumn(oSettings.aaSorting[0][0]);
        this._genericDataSource.setAscending(oSettings.aaSorting[0][1] === "asc" ? true : false);
        
        
        var dtCallback = this._datatablesCallback;
        
        this._genericDataSource.getData({
          onSuccess : function(data) {
            fnCallback(data);
            
            if (dtCallback) {
              dtCallback.onSuccess(data);
            }
          },
          onFailure : dtCallback ? dtCallback.onFailure : this.handleException
        });
      },
      
      formatResponse : function(response) {
        var count = this.getTotalResults();
        
        return {
          sEcho : this._sEcho,
          iTotalRecords: count,
          iTotalDisplayRecords: count,
          aaData: response,
//          oSettings: this._oSettings
//          aoColumns: this.getColumns()
        };
      }
    }
  });
  
  return serverDataSource;
})();
