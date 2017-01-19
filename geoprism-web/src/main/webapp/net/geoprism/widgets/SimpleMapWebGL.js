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
  function SimpleMapWebGLController($scope, $rootScope, localizationService, webGLMapService, locationService, $sce, $compile) {
	 var controller = this;
	  
	 $scope.renderBase = true;
	 $scope.baseLayers = [];
	 $scope.contextStyle = {fill:"rgba(0, 0, 0, 0.25)", strokeColor:"rgba(0, 0, 0, 0.75)", strokeWidth:5, radius:7};
	 $scope.targetStyle = {fill:"rgba(255, 0, 0, 0.75)", strokeColor:"rgba(255, 0, 0, 0.75)", strokeWidth:3, radius:7};
	 $scope.sharedGeoData = {};
	 
	 
	 controller.init = function() {
		 
		var hoverCallback = function(featureId){
	        $scope.$emit('hoverChange', {
	              id : featureId
	        });
	        $scope.$apply();
		}
		
		var featureClickCallback = function(feature, map){
			$scope.$emit('locationFocus', {
	              id : feature.properties.id
	        });
	        $scope.$apply();
		}
		
		//
		// Setting up empty layers to be populated later
		//
		// NOTE: We can't move this code into the map factory because it requires the event listeners which reference the scope.
		//
		var emptyGeoJSON = {"type":"FeatureCollection","totalFeatures":0,"features":[],"crs":{"type":"name","properties":{"name":"urn:ogc:def:crs:EPSG::4326"}}};
		
		controller.addVectorLayer(emptyGeoJSON, "context-multipolygon", $scope.contextStyle, "CONTEXT", 2);
    controller.addVectorLayer(emptyGeoJSON, "context-point", $scope.contextStyle, "CONTEXT", 2);
    
    controller.addVectorLayer(emptyGeoJSON, "target-multipolygon", $scope.targetStyle, "TARGET", 2);
    controller.addVectorLayer(emptyGeoJSON, "target-point", $scope.targetStyle, "TARGET", 2);

		controller.addVectorHoverEvents(hoverCallback, ["target-point", "context-point", "target-multipolygon", "context-multipolygon"]);
		controller.addVectorClickEvents(featureClickCallback, ["target-point", "context-point", "target-multipolygon", "context-multipolygon"]);
		//
		// end
		//
	 }
	 
	 
	 controller.baseLayerPanelMouseOut = function() {
		 $scope.showBaseLayerPanel = false;
	 }
	 
	 controller.toggleBaseLayer = function(targetLayer) {
		 $scope.baseLayers.forEach(function(lyr){
			 if(lyr.layerId === targetLayer.layerId){
				 webGLMapService.toggleBaseLayer(lyr, $scope.activeBaseLayer);
				 $scope.activeBaseLayer = targetLayer;
			 }
		 });
	 }
	 
	 controller.removeVectorData = function() {
		 webGLMapService.removeAllVectorLayers();
	 }
	 
	 controller.getMapData = function(callback, data, workspace) {
		 webGLMapService.getGeoJSONData(callback, data, workspace);
	 }
	 
	 controller.addVectorClickEvents = function(featureClickCallback, layersArr) {
		 webGLMapService.addVectorClickEvents(featureClickCallback, layersArr);
	 }
	 
	 controller.addVectorHoverEvents = function(hoverCallback, layersArr) {
		 webGLMapService.addVectorHoverEvents(hoverCallback, layersArr);
	 }
	 
	 controller.zoomToLayersExtent = function(layersArr) {
		 webGLMapService.zoomToLayersExtent(layersArr);
	 }
	 
	 controller.zoomToExtentOfFeatures = function(featureGeoIds) {
		 webGLMapService.zoomToExtentOfFeatures(featureGeoIds);
	 }
	 
	 controller.focusOnFeature = function(feature) {
		 webGLMapService.focusOnFeature(feature);
	 }
	 
	 controller.focusOffFeature = function(feature) {
		 webGLMapService.focusOffFeature(feature);
	 }
	  
	 controller.addVectorLayer = function(layerGeoJSON, layerName, styleObj, type, stackingIndex) {
		 webGLMapService.addVectorLayer(layerGeoJSON, layerName, styleObj, type, stackingIndex);
	 }
	 
	 controller.updateVectorLayer = function(layerGeoJSON, layerName, styleObj, type, stackingIndex) {
		 webGLMapService.updateVectorLayer(layerGeoJSON, layerName, styleObj, type, stackingIndex);
	 }
	 
	  
	 controller.refreshBaseLayer = function() {
	      if($scope.baseLayers.length > 0) {
	        for(var i = 0; i < $scope.baseLayers.length; i++) {
	          var layer = $scope.baseLayers[i];
	          
	          if(layer.isActive) {
	            webGLMapService.showLayer(layer, 0);          
	          }
	          else {
	            webGLMapService.hideLayer(layer);        
	          }
	        }
	      }
	  }
	 
	 controller.refreshInteractiveLayers = function(triggeringEvent) {
		  if(!isEmptyJSONObject($scope.sharedGeoData)){
			  var data = $scope.sharedGeoData;
			
			  var targetCallback = function(data) {
				  var geomType;
				  for(var i=0; i<data.features.length; i++){
		    		var feature = data.features[i];
		    		feature.properties.isHoverable = true;
		    		feature.properties.isClickable = true;
		    		geomType = feature.geometry.type.toLowerCase();
				  }
				  
				  controller.updateVectorLayer(data, "target-" + geomType, $scope.targetStyle, "TARGET", 2);
			  }
			  
			  data.layers.forEach(function(layer){
				  controller.getMapData(targetCallback, layer, layer.workspace);
			  });
		  }
	 }
	 
	 
	 controller.refreshWithContextLayer = function(triggeringEvent) {
		  if(!isEmptyJSONObject($scope.sharedGeoData)){
			  var data = $scope.sharedGeoData;
			
			  var contextCallback = function(data) {
				  for(var i=0; i<data.features.length; i++){
	  	    		var feature = data.features[i];
	  	    		feature.properties.isHoverable = false;
	  	    		feature.properties.isClickable = false;
	  			  }
				  
				  controller.updateVectorLayer(data, "context-point", $scope.contextStyle, "CONTEXT", 1);
	    		controller.zoomToLayersExtent(["context-point"]);
			  }
			  
			  var targetCallback = function(data) {
				  for(var i=0; i<data.features.length; i++){
		    		var feature = data.features[i];
		    		feature.properties.isHoverable = true;
		    		feature.properties.isClickable = true;
				  }
				  
				  controller.updateVectorLayer(data, "target-point", $scope.targetStyle, "TARGET", 2);
		    	controller.zoomToLayersExtent(["target-point"]);
			  }
			  
			  
			  //controller.removeVectorData();
			  
			  // get context geo data
			  controller.getMapData(contextCallback, data.layers[0], data.workspace);
			  
			  // get target geo data
			  controller.getMapData(targetCallback, data.layers[1], data.workspace);
		  }
	 }
	 
      function isEmptyJSONObject(obj) {
	    for(var prop in obj) {
	        if(obj.hasOwnProperty(prop))
	            return false;
	    }

	    return true && JSON.stringify(obj) === JSON.stringify({});
	  }
      
      
      
      $scope.$on('listHoverOver', function(event, data){
    	  controller.focusOnFeature(data);
      });
      
      $scope.$on('listHoverOff', function(event, data){
    	  controller.focusOffFeature(data);
      });
      
      $scope.$on('listItemClick', function(event, data){
    	  controller.zoomToExtentOfFeatures(data.geoIds)
      });
      
      
      // Recieve shared data from parent controller based on user selection of target location
      $scope.$on('sharedGeoData', function(event, data) {
    	  if(!isEmptyJSONObject(data)){
    		  
    		  $scope.sharedGeoData = data;
    		  
    		  controller.refreshWithContextLayer('sharedGeoData');
    	  }
    	  else if(!isEmptyJSONObject($scope.sharedGeoData)) {
    		  controller.refreshWithContextLayer('sharedGeoData');
    	  }
      });
      
      
      $scope.$on("layerChange", function(event, data){
    	  $scope.sharedGeoData = data;
    	  
    	  if($scope.includeContextLayer){
    		  controller.refreshWithContextLayer('layerChange');
    	  }
    	  else{
    		  controller.refreshInteractiveLayers('layerChange');
    	  }
      });
      
      
//      $scope.$watch("activeBaseLayer", function(newVal, oldVal) {
//    	  if(newVal){
//    		  console.log("watched")
//    	  }
//      });

      
      controller.init();
  }
  
  
  function SimpleMapWebGL() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/widgets/simple-map-webgl.jsp',
      scope: {
    	  includeContextLayer:'=includeContextLayer',
    	  baseMapType:"@baseMapType"
      },
      controller : SimpleMapWebGLController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs, ctrl) {
      }
    }   
  }
  
  
  
  angular.module("simple-map-webgl", ["styled-inputs", "localization-service", "webgl-map-service", "location-service"]);
  angular.module("simple-map-webgl")
   .controller('simpleMapWebglController', SimpleMapWebGLController)
   .directive('simpleMapWebgl', SimpleMapWebGL)
})();
