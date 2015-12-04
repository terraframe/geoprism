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
    

    service.apply = function(request, state) {
    	 service.thematicLayerDTO.applyWithStyleAndStrategy(request, service.thematicStyleDTO, state.mapId, service.aggregationStrategyDTO, state);
    }
    
    service.getCurrentAggregationStrategy = function(dynamicDataModel) {
      var options = dynamicDataModel.aggregationStrategyOptions;
   	  var value = dynamicDataModel.aggregationStrategy;
        
      for(var i=0; i < options.length; i++){
        var strategy = options[i];
            
        if(strategy.value === value){
          return strategy;
        }
      }
          
      return null;        	 
    }
    
    service.setAggregationStrategy = function(newInstance, dynamicDataModel) {
    	
    	var strategy = service.getCurrentAggregationStrategy(dynamicDataModel);
		 
		if(strategy.type.indexOf("UniversalAggregationStrategy")  > -1){
	   		 service.aggregationStrategyDTO = new com.runwaysdk.geodashboard.gis.persist.UniversalAggregationStrategy();
	   	}
	   	else if(strategy.type.indexOf("GeometryAggregationStrategy")  > -1){
	   		 service.aggregationStrategyDTO = new com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy();
	   	}	
	        
        // the universal property will be skipped for geometry agg in the populate method because that property
        // doesn't exist on the server object.
        runwayService.populate(service.aggregationStrategyDTO, {
       	 	"id": strategy.id, 
       	 	"universal": strategy.value
        });
    };
    
    
    service.applyWithStyle = function(thematicLayerModel, thematicStyleModel, dynamicDataModel, state, element, onSuccess, onFailure) {
    	 
    	 var request = runwayService.createStandbyRequest(element, onSuccess, onFailure);
    	 if(dynamicDataModel.newInstance){
    	    service.thematicLayerDTO = new com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer();
    		service.thematicStyleDTO = new com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle();
    			
    		 service.setAggregationStrategy(dynamicDataModel.newInstance, dynamicDataModel);
    		 
	         runwayService.populate(service.thematicLayerDTO, thematicLayerModel);
	         runwayService.populate(service.thematicStyleDTO, thematicStyleModel);
	         
	         service.apply(request, state);
    	 }
    	 else{
    		 var layerSuccess = function(response) {    
    			service.thematicLayerDTO = response;
    			
    			// get/store saved layers agg strategy
	    		var existingLayerAggStrat = response.attributeMap.aggregationStrategy.value;
	    		
	    		// populate the dto
	   	    	runwayService.populate(service.thematicLayerDTO, thematicLayerModel);
	   	    	
	   	    	// add the agg strategy back to the dto after it has been populated from the model
	   	    	service.thematicLayerDTO.setValue('aggregationStrategy', existingLayerAggStrat);
	    		   
   	    		service.setAggregationStrategy(dynamicDataModel.newInstance, dynamicDataModel);
 	   	    	
	   	    	var styleSuccess = function(response) {      
	 	   	    	service.thematicStyleDTO = response;
	 	   	    	runwayService.populate(service.thematicStyleDTO, thematicStyleModel);
	 	   	    	
 	   	    		// IMPORTANT: We need all of the previous ajax requests to finish before applying
 	   	    		service.apply(request, state);
	   	    	};  
	     		var styleFailure = function(response) { console.log(response); }; 
	     		var styleRequest = runwayService.createRequest(styleSuccess, styleFailure);
	    		com.runwaysdk.geodashboard.gis.persist.DashboardThematicStyle.get(styleRequest, thematicStyleModel.id);
	   	     };  
    		 var layerFailure = function(response) { console.log(response); }; 
    		 var layerRequest = runwayService.createRequest(layerSuccess, layerFailure);
    		 com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.get(layerRequest, thematicLayerModel.id);
    	 }
    	 
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