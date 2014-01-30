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

//define(["../../../../../ClassFramework", "../../../../../Util", "./DataSourceFactory", "./DataSourceIF", "./Events"], function(ClassFramework, Util, DataSourceFactory, DataSourceIF, Events) {
(function(){  

  var ClassFramework = Mojo.Meta;
  var Util = Mojo.Util;
  var DataSourceFactory = com.runwaysdk.ui.factory.generic.datatable.datasource.DataSourceFactory;
  var DataSourceIF = com.runwaysdk.ui.factory.generic.datatable.datasource.DataSourceIF;
  var Events = Mojo.Meta.alias("com.runwaysdk.ui.factory.generic.datatable.datasource.events.*");
  
  var RW = ClassFramework.alias(Mojo.RW_PACKAGE + "*");
  var UI = ClassFramework.alias(Mojo.UI_PACKAGE + "*");
  
  var baseServerDataSource = ClassFramework.newClass('com.runwaysdk.ui.factory.generic.datatable.datasource.BaseServerDataSource', {
    
    Extends : UI.Component,
    
    IsAbstract : true,
    
    Implements : DataSourceIF,
    
    Instance : {
      
      initialize : function(cfg)
      {
        cfg = cfg || {};
        
        this._columns = cfg.columns;
        
        this._pageNumber = 1;
        this._pageSize = 20;
        this._totalResults = 0;
        this._sortColumn = 0;
        this._ascending = false;
        
        this._performRequestListeners = [];
        this._setPageNumListeners = [];
        this._setPageSizeListeners = [];
        
        this._isInitialized = false;
      },
      
      initialSetup : function(callback) {
        this._isInitialized = true;
        
        if (callback != null) {
          callback.onSuccess();
        }
      },
      
      getData : function(callback) {
        if (this._isInitialized === false) {
          throw new com.runwaysdk.Exception(com.runwaysdk.Localize.get("rInitalSetup", "The data source must perform initial setup first. This is accomplished by invoking 'initialSetup' on the data source before invoking any other methods."));
        }
        
        var that = this;
        var myCallback = {
          onSuccess: function(response) {
            that._unformattedResponse = response;
            response = that.formatResponse(response);
            callback.onSuccess(response);
          },
          onFailure: function(ex) {
            callback.onFailure(ex);
          }
        }
        this.performRequest(myCallback);
      },
      
      formatResponse : function(formattedResponse) {
        this.dispatchEvent(new Events.FormatResponseEvent(formattedResponse, this._unformattedResponse));
        return formattedResponse;
      },
      
      beforePerformRequest : function(callback) {
        this.dispatchEvent(new Events.BeforePerformRequestEvent(callback));
      },
      
      afterPerformRequest : function(response) {
        this.dispatchEvent(new Events.AfterPerformRequestEvent(response));
      },
      
      addFormatResponseEventListener : function(fnListener) {
        this.addEventListener(Events.FormatResponseEvent, {handleEvent: fnListener});
      },
      
      addBeforePerformRequestEventListener : function(fnListener) {
        this.addEventListener(Events.BeforePerformRequestEvent, {handleEvent: fnListener});
      },
      
      addAfterPerformRequestEventListener : function(fnListener) {
        this.addEventListener(Events.AfterPerformRequestEvent, {handleEvent: fnListener});
      },
      
      addSetPageNumberEventListener : function(fnListener) {
        this.addEventListener(Events.SetPageNumberEvent, {handleEvent: fnListener});
      },
      
      addSetPageSizeEventListener : function(fnListener) {
        this.addEventListener(Events.SetPageSizeEvent, {handleEvent: fnListener});
      },
      
      reset : function() {
        this._pageNumber = 1;
        this._pageSize = 20;
        this._sortColumn = null;
        this._ascending = true;
      },
      
      setTotalResults : function(iTotal) {
        this._totalResults = iTotal;
      },
      
      getTotalResults : function() {
        return this._totalResults;
      },
  
      setPageNumber : function(pageNumber) {
        this._pageNumber = pageNumber;
        
        this.dispatchEvent(new Events.SetPageNumberEvent(pageNumber));
      },
      
      setPageSize : function(pageSize) {
        this._pageSize = pageSize;
        
        this.dispatchEvent(new Events.SetPageSizeEvent(pageSize));
      },
      
      getPageSize : function() {
        return this._pageSize;
      },
      
      getPageNumber : function() {
        return this._pageNumber;
      },
      
      getSortColumn : function() {
        return this._sortColumn;
      },
      
      setSortColumn : function(sortColumn) {
        // TODO accept multiple attributes for priority sorting
        this._sortColumn = sortColumn;
      },
      
      toggleAscending : function() {
        this.setAscending(!this.isAscending());
      },
      
      setAscending : function(ascending){
        this._ascending = ascending;
      },
      
      isAscending : function(){
        return this._ascending;
      },
      
      getColumns : function() {
        return this._columns;
      },
      
      setColumns : function(cols) {
        this._columns = cols;
      }
      
    }
  });
  
  return baseServerDataSource;
  
})();
