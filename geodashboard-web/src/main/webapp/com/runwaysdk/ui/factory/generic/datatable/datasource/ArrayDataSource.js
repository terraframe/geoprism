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

//define(["../../../../../ClassFramework", "../../../../../Util", "./DataSourceFactory", "./DataSourceIF"], function(ClassFramework, Util, DataSourceFactory, DataSourceIF) {
(function(){  

  var ClassFramework = Mojo.Meta;
  var Util = Mojo.Util;
  var DataSourceFactory = com.runwaysdk.ui.factory.generic.datatable.datasource.DataSourceFactory;
  var DataSourceIF = com.runwaysdk.ui.factory.generic.datatable.datasource.DataSourceIF;
  
  var RW = ClassFramework.alias(Mojo.RW_PACKAGE + "*");
  var UI = ClassFramework.alias(Mojo.UI_PACKAGE + "*");
  
  var arrayDataSource = ClassFramework.newClass('com.runwaysdk.ui.factory.generic.datatable.datasource.ArrayDataSource', {
    
    Implements : DataSourceIF,
    
    Instance : {
      
      initialize : function(cfg)
      {
        Util.requireParameter("columns (ArrayDataSource)", cfg.columns);
        Util.requireParameter("data (ArrayDataSource)", cfg.data);
        
        cfg.type = "Array";
        
        this._impl = DataSourceFactory.newDataSource(cfg);
      },
      
      getConfig : function() {
        return this._impl.getConfig();
      },
      
      getColumns : function(callback) {
        return this._impl.getColumns();
      },
      
      setColumns : function(cols) {
        this._impl.setColumns(cols);
      },
      
      getData : function(callback) {
        return this._impl.getData();
      }
      
    }
  });
  
  return arrayDataSource;
  
})();
