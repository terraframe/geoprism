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

define(["../../../../../ClassFramework", "./ArrayDataSource", "./ServerDataSource"], function(ClassFramework, ArrayDataSource, ServerDataSource) {
  
  var RW = ClassFramework.alias(Mojo.RW_PACKAGE + "*");
  var UI = ClassFramework.alias(Mojo.UI_PACKAGE + "*");
  
  var dataSourceFactory = ClassFramework.newClass(Mojo.RW_PACKAGE+'datatable.datasource.DataSourceFactory', {
    
    IsSingleton : true,
    
    Static : {
      
      newDataSource : function(initObj) {
        
        if (initObj.type === "Array") {
          return new ArrayDataSource(initObj);
        }
        else if (initObj.type === "Server") {
          return new ServerDataSource(initObj);
        }
        else {
          throw new com.runwaysdk.Exception("The provided data source type '" + initObj.type + "' is invalid.");
        }
        
      }
  
    }
  
  });
  
  return dataSourceFactory;
  
});
