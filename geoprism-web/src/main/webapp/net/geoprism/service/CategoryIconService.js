/*
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
(function(){

  function CategoryIconService($http, runwayService) {
    var service = {};
    
    service.create = function(connection, file, label) {
      
      var data = new FormData();
      data.append('file', file);
      data.append('label', label)

      /*
       * IMPORTANT: This method cannot be invoked through the generated javascript
       * controller because you can't pass in a FormData to the method.  Thus, we
       * are invoking it directly through the Facade.  FormData is required for
       * submitting file objects through javascript.
       */
//      Mojo.$.com.runwaysdk.Facade._controllerWrapper('net.geoprism.dashboard.layer.CategoryIconController.create.mojax', request, params);
      
      var req = {
        method: 'POST',
        url: '/iconimage/create',
        headers: {
          'Content-Type': undefined
        },
        data: data,
        transformRequest: angular.identity        
      }
      
      runwayService.execute(req, connection);      
    }
    
    service.apply = function(connection, id, file, label) {
      
      var data = new FormData();
      data.append('id', id);
      data.append('label', label)
      
      if(file.fileReference !== "NONE"){
    	  data.append('file', file);
      }

      /*
       * IMPORTANT: This method cannot be invoked through the generated javascript
       * controller because you can't pass in a FormData to the method.  Thus, we
       * are invoking it directly through the Facade.  FormData is required for
       * submitting file objects through javascript.
       */
//        Mojo.$.com.runwaysdk.Facade._controllerWrapper('net.geoprism.dashboard.layer.CategoryIconController.apply.mojax', request, params);
      
      var req = {
        method: 'POST',
        url: '/iconimage/apply',
        headers: {
          'Content-Type': undefined
        },
        data: data,
        transformRequest: angular.identity        
      }      
      
      runwayService.execute(req, connection);
    }    
    
    service.getAll = function(connection) {
//      var request = runwayService.createConnectionRequest(connection);
//    
//      net.geoprism.dashboard.layer.CategoryIconController.getAll(request);
      
      var req = {
        method: 'GET',
        url: '/iconimage/getAll'
      }      
      
      runwayService.execute(req, connection);      
    }
    
    service.remove = function(connection, id) {
//      var request = runwayService.createConnectionRequest(connection);
//      
//      net.geoprism.dashboard.layer.CategoryIconController.remove(request, id);
      
      var req = {
        method: 'POST',
        url: '/iconimage/remove',
        data : {id : id}
      }      
      
      runwayService.execute(req, connection);      
    }
    
    service.edit = function(connection, id) {
//      var request = runwayService.createConnectionRequest(connection);
//      
//      net.geoprism.dashboard.layer.CategoryIconController.edit(request, id);
      
      var req = {
        method: 'POST',
        url: '/iconimage/edit',
        data : {id : id}
      }      
            
      runwayService.execute(req, connection);      
    }
    
    service.getImage = function(connection, id) {      
//      var request = runwayService.createConnectionRequest(connection);
//        
//      net.geoprism.dashboard.layer.CategoryIconController.getCategoryIconImage(request, id);
      
      var req = {
        method: 'GET',
        url: '/iconimage/getCategoryIconImage',
        params: {id : id}
      }      
                  
      runwayService.execute(req, connection);      
    }
    
    service.getDefaultIconModel = function() {
      return {enableIcon:false, icon:"", iconSize:50};
    }
    
    return service;  
  }
  
  angular.module("category-icon-service", ["runway-service"]);
  angular.module("category-icon-service")
    .factory('categoryIconService', CategoryIconService)
})();
