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
      var success = function(response) {
        var result = JSON.parse(response);
      	
        onSuccess(result);
      };
    	
      var request = runwayService.createStandbyRequest(element, success, onFailure);
      
      net.geoprism.DataUploaderController.importData(request, JSON.stringify(configuration));
    }
    
    service.cancelImport = function(configuration, element, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(element, onSuccess, onFailure);
          
      net.geoprism.DataUploaderController.cancelImport(request, JSON.stringify(configuration));    	
    }
    
    service.getAll = function(onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);
    
      net.geoprism.DataSetController.getAll(request);
    }
    
    
    service.getSavedConfiguration = function(id, sheetName, element, onSuccess, onFailure) {
      var success = function(response) {
        var result = JSON.parse(response);
          
         onSuccess(result);
      };
        
      var request = runwayService.createStandbyRequest(element, success, onFailure);
          
      net.geoprism.DataUploaderController.getSavedConfiguration(request, id, sheetName);
    }
    
    service.remove = function(id, element, success, onFailure) {
      var request = runwayService.createStandbyRequest(element, success, onFailure);
      
      net.geoprism.DataSetController.remove(request, id);
    }
    
    //
    // Used to structure the data uploader steps widget
    // config - Configuration array containing additional steps. Can be an empty array.
    //
    service.getUploaderSteps = function(config){
    	var basicSteps = [ {"label": "Name & Country", "page":"INITIAL"},
    			    		  {"label": "Field Configuration", "page":"FIELDS"},
    			    		  {"label": "Summary", "page":"SUMMARY"} ];
    	var locationStep = {"label": "Text Location Configuration", "page":"LOCATION"};
    	var coordinateStep = {"label": "Coordinate Configuration", "page":"COORDINATE"};
    	var problemResStep = {"label": "Problem Resolution"}; 
    	
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
    
    return service;  
  }
  
  angular.module("dataset-service", ["runway-service"]);
  angular.module("dataset-service")
    .factory('datasetService', DatasetService)
})();