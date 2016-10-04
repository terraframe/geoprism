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
    
    service.setDatasetConfiguration = function(config) {
      this._datasetConfiguration = config;
    }
      
    service.getDatasetConfiguration = function() {
      return this._datasetConfiguration;
    }
    
    service.uploadSpreadsheet = function(connection, file) {
      
      var data = new FormData();
      data.append('file', file);
      
      var req = {
        method: 'POST',
        url: '/uploader/getAttributeInformation',
        headers: {
          'Content-Type': undefined
        },
        data: data,
        transformRequest: angular.identity        
      }
            
      runwayService.execute(req, connection);            
    }
    
    service.importData = function(connection, configuration) {
//      var request = runwayService.createConnectionRequest(connection);
//      net.geoprism.DataUploaderController.importData(request, JSON.stringify(configuration));
      
      var req = {
        method: 'POST',
        url: '/uploader/importData',
        data : {configuration : configuration }
      }
      
      runwayService.execute(req, connection);
    }
    
    service.cancelImport = function(connection, configuration) {
//      var request = runwayService.createConnectionRequest(connection);          
//      net.geoprism.DataUploaderController.cancelImport(request, JSON.stringify(configuration));
    
      var req = {
        method: 'POST',
        url: '/uploader/cancelImport',
        data : {configuration : configuration }
      }
              
      runwayService.execute(req, connection);    
    }
    
    service.getSavedConfiguration = function(connection, id, sheetName) {
//      var request = runwayService.createConnectionRequest(connection);            
//      net.geoprism.DataUploaderController.getSavedConfiguration(request, id, sheetName);
            
      var req = {
        method: 'POST',
        url: '/uploader/getSavedConfiguration',
        data : {
          id : id,
          sheetName : sheetName
        }
      }
              
      runwayService.execute(req, connection);      
    }
    
    service.createGeoEntitySynonym = function(connection, entityId, label) {
//      var request = runwayService.createConnectionRequest(connection);        
//      net.geoprism.DataUploaderController.createGeoEntitySynonym(request, entityId, label);
      
        
      var req = {
        method: 'POST',
        url: '/uploader/createGeoEntitySynonym',
        data : {
          entityId : entityId,
          label : label
        }
      }
                
      runwayService.execute(req, connection);            
    }
    
    service.createClassifierSynonym = function(connection, classifierId, label) {
//      var request = runwayService.createConnectionRequest(connection);      
//      net.geoprism.DataUploaderController.createClassifierSynonym(request, classifierId, label);
    
      var req = {
        method: 'POST',
        url: '/uploader/createClassifierSynonym',
        data : {
          classifierId : classifierId,
          label : label
        }
      }
                        
      runwayService.execute(req, connection);    
    }
    
    service.createGeoEntity = function(connection, parentId, universalId, label) {
//      var request = runwayService.createConnectionRequest(connection);      
//      net.geoprism.DataUploaderController.createGeoEntity(request, parentId, universalId, label);
      
      var req = {
        method: 'POST',
        url: '/uploader/createGeoEntity',
        data : {
          parentId : parentId,
          universalId : universalId,
          label : label
        }
      }
                            
      runwayService.execute(req, connection);          
    }    

    service.deleteGeoEntity = function(connection, entityId) {
//      var request = runwayService.createConnectionRequest(connection);      
//      net.geoprism.DataUploaderController.deleteGeoEntity(request, entityId);
    
        
      var req = {
        method: 'POST',
        url: '/uploader/deleteGeoEntity',
        data : {
          entityId : entityId
        }
      }
                              
      runwayService.execute(req, connection);
    }    
    
    service.deleteGeoEntitySynonym = function(connection, synonymId) {
//      var request = runwayService.createConnectionRequest(connection);      
//      net.geoprism.DataUploaderController.deleteGeoEntitySynonym(request, synonymId);
    
      var req = {
        method: 'POST',
        url: '/uploader/deleteGeoEntitySynonym',
        data : {
          synonymId : synonymId
        }
      }
      
      runwayService.execute(req, connection);
    }
    
    service.deleteClassifierSynonym = function(connection, synonymId) {
//      var request = runwayService.createConnectionRequest(connection);      
//      net.geoprism.DataUploaderController.deleteClassifierSynonym(request, synonymId);    
    
      var req = {
        method: 'POST',
        url: '/uploader/deleteClassifierSynonym',
        data : {
          synonymId : synonymId
        }
      }
      
      runwayService.execute(req, connection);
    }    
    
    service.getClassifierSuggestions = function(connection, mdAttributeId, text, limit) {
//      var request = runwayService.createConnectionRequest(connection);        
//      net.geoprism.DataUploaderController.getClassifierSuggestions(request, mdAttributeId, text, limit);
        
      var req = {
        method: 'GET',
        url: '/uploader/getClassifierSuggestions',
        params : {
          mdAttributeId : mdAttributeId,
          text : text,
          limit : limit          
        }
      }
      
      runwayService.execute(req, connection);      
    }
      
    service.validateDatasetName = function(connection, name, id) {
//      var request = runwayService.createConnectionRequest(connection);          
//      net.geoprism.DataUploaderController.validateDatasetName(request, label, id);      
      
      var req = {
        method: 'GET',
        url: '/uploader/validateDatasetName',
        params : {
          name : name,
          id : id          
        }
      }
              
      runwayService.execute(req, connection);
    }
      
    service.validateCategoryName = function(connection, name, id) {
//      var request = runwayService.createConnectionRequest(connection);      
//      net.geoprism.DataUploaderController.validateCategoryName(request, label, id);
      
      var req = {
        method: 'GET',
        url: '/uploader/validateCategoryName',
        params : {
          name : name,
          id : id          
        }
      }
                      
      runwayService.execute(req, connection);      
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
    
    service.cancel = function(connection, id) {
      var request = runwayService.createConnectionRequest(connection);
      
      net.geoprism.DataSetController.cancel(request, id);
    }
    
    service.applyDatasetUpdate = function(connection, dataset) {
      var request = runwayService.createConnectionRequest(connection);
    
      net.geoprism.DataSetController.applyDatasetUpdate(request, JSON.stringify(dataset));
    }
    
    service.apply = function(connection, dataset) {
      var request = runwayService.createConnectionRequest(connection);
      
      net.geoprism.DataSetController.apply(request, JSON.stringify(dataset));
    }
    
    service.getGeoEntitySuggestions = function(connection, parentId, universalId, text, limit) {
      var request = runwayService.createConnectionRequest(connection);
    
      net.geoprism.ontology.GeoEntityUtil.getGeoEntitySuggestions(request, parentId, universalId, text, limit);
    }
    
    //
    // Used to structure the data uploader steps widget
    // @config - Configuration array containing additional steps. Can be an empty array.
    //
    service.getUploaderSteps = function(config){
      var basicSteps = [
        {"label": "1", "page":"INITIAL"},
        {"label": "2", "page":"FIELDS"},
        {"label": "3", "page":"SUMMARY"}];
      
      var locationStep = {"label": "4", "page":"LOCATION"};
      var coordinateStep = {"label": "5", "page":"COORDINATE"};
      var geoProblemResStep = {"label": "6", "page":"GEO-VALIDATION"}; 
      var categoryProblemResStep = {"label": "7", "page":"CATEGORY-VALIDATION"}; 
      
      if(config.indexOf("LOCATION") > -1 && config.indexOf("COORDINATE") > -1 && config.indexOf("CATEGORY") > -1){
        basicSteps.splice(2, 0, locationStep, coordinateStep);
        basicSteps.splice(5, 0, geoProblemResStep);
        basicSteps.splice(6, 0, categoryProblemResStep);
      }
      else if(config.indexOf("LOCATION") === -1 && config.indexOf("COORDINATE") > -1 && config.indexOf("CATEGORY") > -1){
      basicSteps.splice(2, 0, coordinateStep);
        basicSteps.splice(4, 0, geoProblemResStep);
        basicSteps.splice(5, 0, categoryProblemResStep);
      }
      else if(config.indexOf("LOCATION") > -1 && config.indexOf("COORDINATE") === -1 && config.indexOf("CATEGORY") > -1){
      basicSteps.splice(2, 0, locationStep);
        basicSteps.splice(4, 0, geoProblemResStep);
        basicSteps.splice(5, 0, categoryProblemResStep);
      }
      else if(config.indexOf("LOCATION") > -1 && config.indexOf("COORDINATE") > -1 && config.indexOf("CATEGORY") === -1){
        basicSteps.splice(2, 0, locationStep, coordinateStep);
        basicSteps.splice(5, 0, geoProblemResStep);
      }
      else if(config.indexOf("LOCATION") > -1 ){
        basicSteps.splice(2, 0, locationStep);
        basicSteps.splice(4, 0, geoProblemResStep);
      }
      else if(config.indexOf("COORDINATE") > -1){
        basicSteps.splice(2, 0, coordinateStep);
        basicSteps.splice(4, 0, geoProblemResStep);
      }
      else if(config.indexOf("CATEGORY") > -1){
        basicSteps.splice(3, 0, categoryProblemResStep);
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
