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

  function CategoryIconService(runwayService) {
    var service = {};
    
    service.create = function(connection, file, label) {
    	
      var request = runwayService.createConnectionRequest(connection);
      
      var params = new FormData();
      params.append('file', file);
      params.append('label', label)

      /*
       * IMPORTANT: This method cannot be invoked through the generated javascript
       * controller because you can't pass in a FormData to the method.  Thus, we
       * are invoking it directly through the Facade.  FormData is required for
       * submitting file objects through javascript.
       */
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('net.geoprism.dashboard.layer.CategoryIconController.create.mojax', request, params);
    }
    
    service.getAll = function(connection) {
      var request = runwayService.createConnectionRequest(connection);
    
      net.geoprism.dashboard.layer.CategoryIconController.getAll(request);
    }
    
    service.remove = function(connection, id) {
      var request = runwayService.createConnectionRequest(connection);
      
      net.geoprism.dashboard.layer.CategoryIconController.remove(request, id);
    }
    
    return service;  
  }
  
  angular.module("category-icon-service", ["runway-service"]);
  angular.module("category-icon-service")
    .factory('categoryIconService', CategoryIconService)
})();
