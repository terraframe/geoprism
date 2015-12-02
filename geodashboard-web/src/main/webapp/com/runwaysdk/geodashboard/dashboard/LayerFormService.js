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
  
  function LayerFormService(runwayService) {
    var service = {};
    service.thematicLayerDTO = new com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer();
    service.thematicStyleDTO = new com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle();
    
    service.createRequest = function(onSuccess, onFailure){
        var request = new Mojo.ClientRequest({
          onSuccess : onSuccess,
          onFailure : function(e) {
            GDB.ExceptionHandler.handleException(e);
                      
            if(onFailure != null) {
              onFailure(e);
            }
          }
        });
        
        return request;
    }
    
    
    service.createStandbyRequest = function(elementId, onSuccess, onFailure){
        var el = $(elementId);
        
        if(el.length > 0) {    	  
          var request = new GDB.StandbyClientRequest({
            onSuccess : onSuccess,
            onFailure : function(e){
              GDB.ExceptionHandler.handleException(e);
              
              if(onFailure != null) {
                onFailure(e);
              }
            }
          }, elementId);
          
          return request;        
        }
        
        return service.createRequest(onSuccess, onFailure);
    }
    
    
    service.getThematicLayerJSON = function(layerId, onSuccess, onFailure) {
        var request = service.createRequest(onSuccess, onFailure);
            
        com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.getJSON(request, layerId);
    }
    
    service.getThematicLayerOptionsJSON = function(attributeId, dashboardId, onSuccess, onFailure) {
    	var request = service.createRequest(onSuccess, onFailure);
        
        com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.getOptionsJSON(request, attributeId, dashboardId);
    }
    
    
    
    
    
    
    service.applyWithStyle = function(thematicLayerModel, thematicStyleModel, dynamicDataModel, state, element, onSuccess, onFailure) {
    	 var request = runwayService.createStandbyRequest(element, onSuccess, onFailure);
    	 
    	 if(dynamicDataModel.aggregationStrategy.type.indexOf("UniversalAggregationStrategy")  > -1){
    		 service.aggregationStrategyDTO = new com.runwaysdk.geodashboard.gis.persist.UniversalAggregationStrategy();
    	 }
    	 else if(dynamicDataModel.aggregationStrategy.type.indexOf("GeometryAggregationStrategy")  > -1){
    		 service.aggregationStrategyDTO = new com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy();
    	 }			

         runwayService.populate(service.thematicLayerDTO, thematicLayerModel);
         runwayService.populate(service.thematicStyleDTO, thematicStyleModel);
         
         // the universal property will be skipped for geometry agg in the populate method because that property
         // doesn't exist on the server object.
         runwayService.populate(service.aggregationStrategyDTO, {
        	 "id": dynamicDataModel.aggregationStrategy.id, 
        	 "universal": dynamicDataModel.aggregationStrategy.value
         	}
         );

         service.thematicLayerDTO.applyWithStyleAndStrategy(request, service.thematicStyleDTO, state.mapId, service.aggregationStrategyDTO, state);
    }
    
    
    service.unlock = function(object, onSuccess, onFailure) {
        if(service.thematicLayerDTO == null || service.thematicLayerDTO.isNewInstance()) {
          onSuccess();  
        }
        else {
          var request = runwayService.createRequest(onSuccess, onFailure);
          
          service.thematicLayerDTO.unlock(request);  
        }
    }
    
    
    
    
    
    
    service.categoryAutoCompleteService = function(mdAttribute, geoNodeId, universalId, aggregationVal, text, limit, conditions, onSuccess, onFailure){
    	var request = service.createRequest(onSuccess, onFailure);
		com.runwaysdk.geodashboard.Dashboard.getCategoryInputSuggestions(request, mdAttribute, geoNodeId, universalId, aggregationVal, text, limit, conditions);
    }
    
    return service;
  }
  
  angular.module("layer-form-service", ["runway-service"]);
  angular.module('layer-form-service')
    .factory('layerFormService', LayerFormService);
})();