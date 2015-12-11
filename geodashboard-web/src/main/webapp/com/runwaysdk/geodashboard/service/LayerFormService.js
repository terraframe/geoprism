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
  /*
   * 
   * 
   * THEMATIC LAYER SERVICE
   * 
   * 
   */
  function LayerFormService(runwayService) {
    var service = {};
    
    service.createRequest = function(onSuccess, onFailure){
        var request = new Mojo.ClientRequest({
          onSuccess : onSuccess,
          onFailure : function(e) {
            if(onFailure != null) {
              onFailure(e);            	
            }
            else {
              GDB.ExceptionHandler.handleException(e);            	
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
              if(onFailure != null) {
                onFailure(e);            	
              }
              else {
                GDB.ExceptionHandler.handleException(e);            	
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
    	 
    	 var request = service.createStandbyRequest(element, onSuccess, onFailure);
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
  
  /*
   * 
   * 
   * REFERENCE LAYER SERVICE
   * 
   * 
   */  
  function ReferenceLayerFormService(runwayService) {
    var service = {};
      
    service.createRequest = function(onSuccess, onFailure){
      var request = new Mojo.ClientRequest({
        onSuccess : onSuccess,
        onFailure : function(e) {
          if(onFailure != null) {
            onFailure(e);              
          }
          else {
            GDB.ExceptionHandler.handleException(e);              
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
            if(onFailure != null) {
              onFailure(e);              
            }
            else {
              GDB.ExceptionHandler.handleException(e);              
            }
          }
        }, elementId);
            
        return request;        
      }
          
      return service.createRequest(onSuccess, onFailure);
    }
    
    service.apply = function(layer, style, state, element, onSuccess, onFailure) {
      // Populate the layer
      runwayService.populate(service.layerDTO, layer);      
      service.layerDTO.setValue('universal', layer.universalId);

      // Populate the style
      runwayService.populate(service.styleDTO, style);  
      
      var request = service.createStandbyRequest(element, onSuccess, onFailure);
      
      service.layerDTO.applyWithStyle(request, service.styleDTO, state.mapId, state);
    }      
    
    service.unlock = function(layer, element, onSuccess, onFailure) {
      var success = function() {
        service.layerDTO = null;
        service.styleDTO = null;
        
        onSuccess();      	  
      }

      if(service.layerDTO == null || service.layerDTO.isNewInstance()) {
    	success();
      }
      else {
        var request = service.createStandbyRequest(element, success, onFailure);

        service.layerDTO.unlock(request);
      }        
    }
    
    service.createObjects = function(response) {
      service.layerDTO = com.runwaysdk.DTOUtil.convertToType(response.layerDTO);
      service.styleDTO = com.runwaysdk.DTOUtil.convertToType(response.styleDTO);
        
      var layer = {};
      layer.name = response.layer.layerName;
      layer.universalId = response.layer.universalId;
      layer.layerType = response.layer.featureStrategy;
      layer.displayInLegend = response.layer.inLegend;

      // Set default values for the style
      var style = {};
      style.labelFont = 'Arial';
      style.enableLabel = true;
      style.labelSize = 12;
      style.labelColor = '#E0A4E0';
      style.labelHalo = '#000';
      style.labelHaloWidth = 2;
      style.pointFill = '#A9DEA4';
      style.pointOpacity = 0.75;
      style.pointStroke = '#000';
      style.pointStrokeWidth = 2;
      style.pointStrokeOpacity = 0.65;
      style.basicPointSize = 20;
      style.pointWellKnownName = 'CIRCLE';
      style.polygonFill = '#A9DEA4';
      style.polygonFillOpacity = 0.90;
      style.polygonStroke = '#000';
      style.polygonStrokeWidth = 5;
      style.polygonStrokeOpacity = 0.65;
        
      runwayService.populateObject(style, service.styleDTO);
      
      // pointWellKnownName values come from the server lower cased
      // but the UI model expects them to be upper cased values
      style.pointWellKnownName = style.pointWellKnownName.toUpperCase();

      // Ensure that the current font is actually available on the system
      // If not default to the first option
      if(!service.isValidFont(style.labelFont, response.options.availableFonts)) {
        style.labelFont = response.options.availableFonts[0];
      }
        
      return {layer:layer, style:style, dynamicDataModel:response.options};    
    }
    
    service.edit = function(layerId, element, onSuccess, onFailure) {
      var success = function(response) {
        var model = service.createObjects(response);
            
        onSuccess(model);
      }
          
      var request = service.createStandbyRequest(element, success, onFailure);
          
      com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerController.edit(request, layerId);    	
    }
    
    service.newInstance = function(universalId, mapId, element, onSuccess, onFailure) {
      var success = function(response) {
    	var model = service.createObjects(response);
        
        onSuccess(model);
      }
      
      var request = service.createStandbyRequest(element, success, onFailure);
      
      com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerController.newReferenceInstance(request, universalId, mapId);
    }
    
    service.isValidFont = function(font, options) {
      if(font != null && font.length > 0) {
        for(var i = 0; i < options.length; i++) {
          if(font === options[i]) {
            return true;
          } 
        }      
      }
      return false;
    }
      
    return service;
  }
	  
  
  
  angular.module("layer-form-service", ["runway-service"]);
  angular.module('layer-form-service')
    .factory('referenceLayerFormService', ReferenceLayerFormService)
    .factory('layerFormService', LayerFormService);
})();