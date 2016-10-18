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
  function EditableMapController($scope, $rootScope, localizationService, mapService, locationService) {
	 var controller = this;
	  
	 $scope.editWidgetEnabled = false;
	 $scope.renderBase = true;
	 $scope.baseLayers = [];
	 $scope.contextStyle = {fill:"rgba(0, 0, 0, 0.25)", strokeColor:"rgba(0, 0, 0, 0.5)", strokeWidth:5, radius:7};
	 $scope.targetStyle = {fill:"rgba(255, 0, 0, 0.1)", strokeColor:"rgba(255, 0, 0, 0.5)", strokeWidth:3, radius:7};
	 $scope.newFeatureGeom = null;
	 $scope.editFeature = null;
	 $scope.sharedGeoData = {};
	 
	 $scope.saveCallback = function(editFeature, isNew) {
		  if(isNew){
			  $scope.newFeatureGeom = editFeature;
			  $scope.$apply();
		  }
		  else{
			  $scope.editFeature = editFeature;
			  $scope.$apply();
		  }
	 }
	 
	 
	 controller.init = function() {
		 
		$scope.baseLayers = mapService.createBaseLayers();
		
		// Override default basemap from MapConfig with property passed from directive HTML
		// Any option passed in must also be included in MapConfig or it will not be available to add. 
		if($scope.baseMapType){
			$scope.baseLayers.forEach(function(lyr){
				if(lyr.layerType.toUpperCase() === $scope.baseMapType.toUpperCase()){
					lyr.isActive = true;
				}
				else{
					lyr.isActive = false;
				}
			});
		}
		 
		controller.refreshBaseLayer();
		
		
		var hoverCallback = function(featureId){
	        $scope.$emit('hoverChange', {
	              id : featureId
	        });
	        $scope.$apply();
		}
		
		var featureClickCallback = function(feature, map) {
			  if(map.getProperties().editFeatures && map.getProperties().editFeatures.getArray().length === 0){
				  controller.addNewPointControl(feature, $scope.saveCallback);
			  }
		}
        
		controller.addVectorHoverEvents(hoverCallback);
		controller.addVectorClickEvents(featureClickCallback);
	 }
	 
	 controller.enableEdits = function(saveCallback) {
		 mapService.enableEdits(saveCallback);
		 $scope.editWidgetEnabled = true;
	 }
	 
	 controller.disableEdits = function() {
		 mapService.disableEdits();
	 }
	 
	 controller.removeVectorData = function() {
		 mapService.removeAllVectorLayers();
	 }
	 
	 controller.getMapData = function(callback, data, workspace) {
		 mapService.getGeoJSONData(callback, data, workspace);
	 }
	 
	 controller.addNewPointControl = function(feature, saveCallback) {
		 mapService.addNewPointControl(feature, saveCallback);
	 }
	 
	 controller.addVectorClickEvents = function(featureClickCallback) {
		 mapService.addVectorClickEvents(featureClickCallback);
	 }
	 
	 controller.addVectorHoverEvents = function(hoverCallback) {
		 mapService.addVectorHoverEvents(hoverCallback);
	 }
	 
	 controller.zoomToVectorDataExtent = function() {
		 mapService.zoomToVectorDataExtent();
	 }
	 
	 controller.focusOnFeature = function(feature) {
		 mapService.focusOnFeature(feature);
	 }
	 
	 controller.focusOffFeature = function(feature) {
		 mapService.focusOffFeature(feature);
	 }
	  
	 controller.addVectorLayer = function(layerGeoJSON, styleObj, type, stackingIndex) {
		 mapService.addVectorLayer(layerGeoJSON, styleObj, type, stackingIndex);
	 }
	 
	 controller.closeEditSession = function(saveCallback) {
		 mapService.closeEditSession(saveCallback);
	 }
	  
	  
	 controller.refreshBaseLayer = function() {
	      if($scope.baseLayers.length > 0) {
	        for(var i = 0; i < $scope.baseLayers.length; i++) {
	          var layer = $scope.baseLayers[i];
	          
	          if(layer.isActive) {
	            mapService.showLayer(layer, 0);          
	          }
	          else {
	            mapService.hideLayer(layer);        
	          }
	        }
	      }
	  }
	 
	 
	 controller.refreshWithContextLayer = function(triggeringEvent) {
		  if(!isEmptyJSONObject($scope.sharedGeoData)){
			  var data = $scope.sharedGeoData;
			  var targetIsClickable = canEnableEditToolbar(data);
			
			  var contextCallback = function(data) {
				  for(var i=0; i<data.features.length; i++){
	  	    		var feature = data.features[i];
	  	    		feature.properties.isHoverable = false;
	  	    		feature.properties.isClickable = false;
	  			  }
				  
				  controller.addVectorLayer(data, $scope.contextStyle, "CONTEXT", 1);
	    		  controller.zoomToVectorDataExtent();
			  }
			  
			  var targetCallback = function(data) {
				  for(var i=0; i<data.features.length; i++){
		    		var feature = data.features[i];
		    		feature.properties.isHoverable = true;
		    		feature.properties.isClickable = targetIsClickable;
				  }
				  
				  controller.addVectorLayer(data, $scope.targetStyle, "TARGET", 2);
		    	  controller.zoomToVectorDataExtent();
			  }
			  
			  
			  controller.removeVectorData();
			  
			  // get context geo data
			  controller.getMapData(contextCallback, data.layers[0], data.workspace);
			  
			  // get target geo data
			  controller.getMapData(targetCallback, data.layers[1], data.workspace);

			  
			  if(canEnableEditToolbar(data) && !$scope.editWidgetEnabled){
				controller.enableEdits(controller.saveCallback);
			  }
			  else if($scope.editWidgetEnabled){
				// locationChange is requesting simple refresh of the map layers after an edit
				// else will be true if the universal level is not appropriate for edits and it is now a 
				// feature edit based refresh
				if(triggeringEvent === "locationChange"){
					controller.closeEditSession(controller.saveCallback);
				}
				else{
					controller.disableEdits();
					$scope.editWidgetEnabled = false;
				}
			  }		 
			  else if(triggeringEvent === "locationChange"){
				  controller.closeEditSession(controller.saveCallback);
			  }
		  }
	 }
	 
	 function canEnableEditToolbar(data) {
		 if($scope.enableEdits 
				 && data.layers.length > 1
				 && data.layers[1].layerType === "POINT" 
				 && data.layers[0].layerType === "POLYGON"
			     ){
			 return true;
		 }
		 
		 return false;
	 }
	  
      function isEmptyJSONObject(obj) {
	    for(var prop in obj) {
	        if(obj.hasOwnProperty(prop))
	            return false;
	    }

	    return true && JSON.stringify(obj) === JSON.stringify({});
	  }
      
      
      //
      // START Events emitted from LocationManagement.js
      //
      $scope.$watch("newFeatureGeom", function(newVal, oldVal) {
    	  $scope.$emit('locationEdit', {
	        wkt : newVal,
	        universal : {
	            value : $scope.sharedGeoData.universal,
	            options : $scope.sharedGeoData.universals
	        },
	        parent : $scope.sharedGeoData.entity
	      });
      });
      
      $scope.$watch("editFeature", function(newVal, oldVal) {
    	  if(newVal){
    		  var editFeatureProps = newVal.getProperties()
	    	  $scope.$emit('locationLock', {
		        wkt : editFeatureProps.wktGeom,
		        entityId : editFeatureProps.id 
		      });
    	  }
      });
      
      $scope.$on('listHoverOver', function(event, data){
    	  controller.focusOnFeature(data);
      });
      
      $scope.$on('listHoverOff', function(event, data){
    	  controller.focusOffFeature(data);
      });
      
      $rootScope.$on("locationChange", function(event, data){
    	  //
    	  // IMPORTANT: this event should only be called from a success callback of an entity create or update
    	  //
		  controller.refreshWithContextLayer("locationChange");
      })
      
      // Recieve shared data from LocationManagement controller based on user selection of target location
      $scope.$on('sharedGeoData', function(event, data) {
    	  if(!isEmptyJSONObject(data)){
    		  
    		  $scope.sharedGeoData = data;
    		  
    		  controller.refreshWithContextLayer('sharedGeoData');
    	  }
    	  else if(!isEmptyJSONObject($scope.sharedGeoData)) {
    		  controller.refreshWithContextLayer('sharedGeoData');
    	  }
      });
      //
      // END events emitted from LocationMangement.js
      //
      
      
      controller.init();
  }
  
  
  function EditableMap() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/widgets/editable-map.jsp',
      scope: {
    	  enableEdits:'=enableEdits',
    	  includeContextLayer:'=includeContextLayer', 
    	  baseMapType:'@baseMapType'
      },
      controller : EditableMapController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs, ctrl) {
      }
    }   
  }
  
  
  
  angular.module("editable-map", ["styled-inputs", "localization-service", "map-service", "location-service"]);
  angular.module("editable-map")
   .controller('EditableMapController', EditableMapController)
   .directive('editableMap', EditableMap)
})();
