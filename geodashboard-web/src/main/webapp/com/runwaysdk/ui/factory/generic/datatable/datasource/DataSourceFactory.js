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

//define(["../../../../../ClassFramework", "../../../jquery/datatable/datasource/DataSourceFactory", "../../../runway/datatable/datasource/DataSourceFactory"], function(ClassFramework, JQDSFac, RWDSFac) {
(function(){

  var ClassFramework = Mojo.Meta;
//  var JQDSFac = com.runwaysdk.ui.factory.jquery.datatable.datasource.DataSourceFactory;
//  var RWDSFac = null;
  
  var dataSourceFactory = ClassFramework.newClass('com.runwaysdk.ui.factory.generic.datatable.datasource.DataSourceFactory', {
    
    IsSingleton : true,
    
    Static : {
      
      newDataSource : function(initObj) {
        
        var UI = ClassFramework.alias(Mojo.UI_PACKAGE + "*");
        
        if (UI.Manager.getFactory().getMetaClass().getQualifiedName() === Mojo.RW_PACKAGE+"Factory") {
          return RWDSFac.newDataSource(initObj);
        }
        else if (UI.Manager.getFactory().getMetaClass().getQualifiedName() === Mojo.JQUERY_PACKAGE+"Factory") {
          return com.runwaysdk.ui.factory.jquery.datatable.datasource.DataSourceFactory.newDataSource(initObj);
        }
        else {
          throw new com.runwaysdk.Exception("DataSources are not supported for factory [" + UI.Manager.getFactoryName() + "].");
        }
        
      }
  
    }
  
  });
  
  return dataSourceFactory;
  
})();
