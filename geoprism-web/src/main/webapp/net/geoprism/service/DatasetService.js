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
    var _datasetConfiguration = {};
    
    service.uploadSpreadsheet = function(connection, file) {
    	
      var request = runwayService.createConnectionRequest(connection);
      
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
    
    service.setDatasetConfiguration = function(config) {
    	this._datasetConfiguration = config;
    }
    
    service.getDatasetConfiguration = function() {
    	return this._datasetConfiguration;
    }
    
    service.importData = function(connection, configuration) {
      var request = runwayService.createConnectionRequest(connection);
      
      net.geoprism.DataUploaderController.importData(request, JSON.stringify(configuration));
    }
    
    service.cancelImport = function(connection, configuration) {
      var request = runwayService.createConnectionRequest(connection);
          
      net.geoprism.DataUploaderController.cancelImport(request, JSON.stringify(configuration));      
    }
    
    service.getSavedConfiguration = function(connection, id, sheetName) {
      var request = runwayService.createConnectionRequest(connection);
            
      net.geoprism.DataUploaderController.getSavedConfiguration(request, id, sheetName);
    }
    
    service.createGeoEntitySynonym = function(connection, entityId, label) {
      var request = runwayService.createConnectionRequest(connection);
        
      net.geoprism.DataUploaderController.createGeoEntitySynonym(request, entityId, label);
    }
    
    service.createGeoEntity = function(connection, parentId, universalId, label) {
      var request = runwayService.createConnectionRequest(connection);
      
      net.geoprism.DataUploaderController.createGeoEntity(request, parentId, universalId, label);
    }    
    

    service.addLocationExclusion = function(locationExclusionObj) {
    	var config = this.getDatasetConfiguration();
     	if(config.locationExclusions){
     		config.locationExclusions.push(locationExclusionObj);
    	}
    	else{
    		config.locationExclusions = [locationExclusionObj];
    	}
    }

    service.deleteGeoEntity = function(connection, entityId) {
      var request = runwayService.createConnectionRequest(connection);
      
      net.geoprism.DataUploaderController.deleteGeoEntity(request, entityId);
    }    
    
    service.deleteGeoEntitySynonym = function(connection, synonymId) {
      var request = runwayService.createConnectionRequest(connection);
      
      net.geoprism.DataUploaderController.deleteGeoEntitySynonym(request, synonymId);
    }    
    
    service.getAll = function(connection) {
      var request = runwayService.createConnectionRequest(connection);
    
      net.geoprism.DataSetController.getAll(request);
    }
    
    service.remove = function(connection, id) {
      var request = runwayService.createConnectionRequest(connection);
      
      net.geoprism.DataSetController.remove(request, id);
    }
    
    service.edit = function(connection, id) {
    	var request = runwayService.createConnectionRequest(connection);
    
    	net.geoprism.DataSetController.edit(request, id);
    }
    
    service.applyDatasetUpdate = function(connection, dataset) {
    	var request = runwayService.createConnectionRequest(connection);
    
    	net.geoprism.DataSetController.applyDatasetUpdate(request, JSON.stringify(dataset));
    }
    
    service.getGeoEntitySuggestions = function(connection, parentId, universalId, text, limit) {
      var request = runwayService.createConnectionRequest(connection);
    
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
      var problemResStep = {"label": "6", "page":"GEO-VALIDATION"}; 
      
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
    return service;  
  }
  
  angular.module("dataset-service", ["runway-service"]);
  angular.module("dataset-service")
    .factory('datasetService', DatasetService)
})();
