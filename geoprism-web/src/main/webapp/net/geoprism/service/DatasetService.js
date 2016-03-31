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

  function DatasetService(runwayService) {
    var service = {};
    
    service.uploadSpreadsheet = function(file, element, onSuccess, onFailure) {
      var success = function(response) {
        var result = JSON.parse(response);
    	
        onSuccess(result);
      };
      
      var request = runwayService.createStandbyRequest(element, success, onFailure);
      
      var params = new FormData();
      params.append('file', file);

      /*
       * IMPORTANT: This method cannot be invoked through the generated javascript
       * controller because you can't pass in a FormData to the method.  Thus, we
       * are invoking it directly through the Facade.  FormData is required for
       * submitting file objects through javascript.
       */
      Mojo.$.com.runwaysdk.Facade._controllerWrapper('net.geoprism.DataUploaderController.getAttributeInformation.mojax', request, params);
    }
    
    service.importData = function(configuration, element, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(element, onSuccess, onFailure);
      
      net.geoprism.DataUploaderController.importData(request, JSON.stringify(configuration));
    }
    
    service.cancelImport = function(configuration, element, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(element, onSuccess, onFailure);
          
      net.geoprism.DataUploaderController.cancelImport(request, JSON.stringify(configuration));      
    }
    
    service.getSavedConfiguration = function(id, sheetName, element, onSuccess, onFailure) {
      var success = function(response) {
        var result = JSON.parse(response);
            
        onSuccess(result);
      };
          
      var request = runwayService.createStandbyRequest(element, success, onFailure);
            
      net.geoprism.DataUploaderController.getSavedConfiguration(request, id, sheetName);
    }
    
    service.createGeoEntitySynonym = function(entityId, label, element, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(element, onSuccess, onFailure);
        
      net.geoprism.DataUploaderController.createGeoEntitySynonym(request, entityId, label);
    }
    
    service.createGeoEntity = function(parentId, universalId, label, element, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(element, onSuccess, onFailure);
      
      net.geoprism.DataUploaderController.createGeoEntity(request, parentId, universalId, label);
    }    
    
    service.getAll = function(onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);
    
      net.geoprism.DataSetController.getAll(request);
    }
    
    service.remove = function(id, element, success, onFailure) {
      var request = runwayService.createStandbyRequest(element, success, onFailure);
      
      net.geoprism.DataSetController.remove(request, id);
    }
    
    service.getGeoEntitySuggestions = function(parentId, universalId, text, limit, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);
    
      net.geoprism.ontology.GeoEntityUtil.getGeoEntitySuggestions(request, parentId, universalId, text, limit);
    }
    
    //
    // Used to structure the data uploader steps widget
    // config - Configuration array containing additional steps. Can be an empty array.
    //
    service.getUploaderSteps = function(config){
      var basicSteps = [
        {"label": "1", "page":"INITIAL"},
        {"label": "2", "page":"FIELDS"},
        {"label": "3", "page":"SUMMARY"}];
      
      var locationStep = {"label": "4", "page":"LOCATION"};
      var coordinateStep = {"label": "5", "page":"COORDINATE"};
      var problemResStep = {"label": "6"}; 
      
      if(config.indexOf("LOCATION") !== -1 && config.indexOf("COORDINATE") !== -1){
        basicSteps.splice(2, 0, locationStep, coordinateStep);
        basicSteps.splice(5, 0, problemResStep);
      }
      else if(config.indexOf("LOCATION") !== -1){
        basicSteps.splice(2, 0, locationStep);
        basicSteps.splice(4, 0, problemResStep);
      }
      else if(config.indexOf("COORDINATE") !== -1){
        basicSteps.splice(2, 0, coordinateStep);
        basicSteps.splice(4, 0, problemResStep);
      }

      return basicSteps;
    }

	// List of runway class dependencies which must be loaded from the server
    return runwayService.decorateService(service, ['net.geoprism.DataUploaderController', 'net.geoprism.DataSetController', 'net.geoprism.ontology.GeoEntityUtil']);  
  }
  
  angular.module("dataset-service", ["runway-service"]);
  angular.module("dataset-service")
    .factory('datasetService', DatasetService)
})();
