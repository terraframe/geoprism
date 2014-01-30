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

//define(["../../../../../ClassFramework", "../../../../../Util", "./DataSourceFactory", "./BaseServerDataSource", "./Events"], function(ClassFramework, Util, DataSourceFactory, BaseServerDataSource, Events) {
(function(){  

  var ClassFramework = Mojo.Meta;
  var Util = Mojo.Util;
  var DataSourceFactory = com.runwaysdk.ui.factory.generic.datatable.datasource.DataSourceFactory; 
  var BaseServerDataSource = com.runwaysdk.ui.factory.generic.datatable.datasource.BaseServerDataSource;
  var Events = com.runwaysdk.ui.factory.generic.datatable.datasource.Events;
  
  var RW = ClassFramework.alias(Mojo.RW_PACKAGE + "*");
  var UI = ClassFramework.alias(Mojo.UI_PACKAGE + "*");
  
  var serverDataSource = ClassFramework.newClass('com.runwaysdk.ui.factory.generic.datatable.datasource.ServerDataSource', {
    
    IsAbstract : true,
    
    Extends : BaseServerDataSource,
    
    Instance : {
      
      initialize : function(cfg)
      {
        cfg = cfg || {};
        
        this.$initialize(cfg);
        
        cfg.type = "Server";
        cfg.genericDataSource = this;
        
        this._impl = DataSourceFactory.newDataSource(cfg);
      },
      
      getImpl : function() {
        return this._impl;
      },
      
      formatResponse : function(response) {
        response = this._impl.formatResponse(response);
        return this.$formatResponse(response);
      },
      
      getConfig : function() {
        return this._impl.getConfig();
      },
      
      getColumns : function(callback) {
        return this._impl.getColumns() || this.$getColumns(callback);
      },
      
      setPageNumber : function(pageNum) {
        this.$setPageNumber(pageNum);
        this._impl.setPageNumber(pageNum);
      },
      
      setPageSize : function(size) {
        this.$setPageSize(size);
        this._impl.setPageSize(size);
      },
      
      setColumns : function(cols) {
        this.$setColumns(cols);
        this._impl.setColumns(cols);
      },
      
      setTotalResults : function(total) {
        this.$setTotalResults(total);
        this._impl.setTotalResults(total);
      }
      
    }
  });
  
  return serverDataSource;
  
})();
