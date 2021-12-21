/*
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
(function(){
  
  function MapService() {
    var service = {};
    service.workspace = '';    
    service.map = new net.geoprism.gis.OpenLayersMap("mapDivId", null, null);
    
    service.setWorkspace = function(workspace) {
      service.workspace = workspace;
    };
      
    service.getWorkspace = function() {
      return service.workspace;
    };
  
    service.createBaseLayers = function() {
      return service.map.createBaseLayers();  
    }
  
    service.setClickHandler = function(handler) {
      service.map.setClickHandler(handler);      
    }

    service.clear = function() {
      service.map.clear();  
    }
    
    service.createReferenceLayers = function(layers) {
      service.map.createReferenceLayers(layers, service.workspace, true);
    }
    
    service.createUserLayers = function(layers) {
      service.map.createUserLayers(layers, service.workspace, true);
    }
    
    service.hideLayer = function(layer){
      service.map.hideLayer(layer);    
    }            

    service.hideLayers = function(layers){
      service.map.hideLayers(layers);
    }

    service.showLayer = function(layer, index) {
      service.map.showLayer(layer, index);    
    }    
    
    service.setView = function(bounds, center, zoomLevel, dataSRID){
      service.map.setView(bounds, center, zoomLevel, dataSRID);    
    }

    service.getCurrentBounds = function(srid) {
      return service.map.getCurrentBounds(srid);    
    }
    
    service.getFeatureInfo = function(layers, e, setFeatureInfo) {
      return service.map.getFeatureInfo(service.workspace, layers, e, setFeatureInfo);    
    }        

    service.addOverlay = function(element, coordinate) {
      service.map.addOverlay(element, coordinate);
    }

    service.clearOverlays = function() {
      service.map.clearOverlays();
    }
    
    service.zoomToFeatureExtent = function(feature){
    	// NOTE: this.getWorkspace() will only be set if on the dashboardViewer page
    	// because that is the only place we are currently passing the var to the client via jsp attributes
    	service.map.zoomToFeatureExtent(feature, this.getWorkspace());
    	this.clearOverlays(); // the popup doesn't shift appropriately to the new position so clear it
    }
    
    service.addVectorLayer = function(layer, styleObj, type, stackingIndex) {
    	service.map.addVectorLayer(layer, styleObj, type, stackingIndex);
    }
    
    service.zoomToVectorDataExtent = function() {
    	service.map.zoomToVectorDataExtent();
    }
    
    service.addVectorHoverEvents = function(hoverCallback) {
    	service.map.addVectorHoverEvents(hoverCallback);
    }
    
    service.addVectorClickEvents = function(featureClickCallback) {
    	service.map.addVectorClickEvents(featureClickCallback);
    }
    
    service.addNewPointControl = function(feature, saveCallback) {
    	service.map.addNewPointControl(feature, saveCallback);
    }
    
    service.removeAllVectorLayers = function() {
    	service.map.removeAllVectorLayers();
    }
    
    service.enableEdits = function(saveCallback) {
    	service.map.enableEdits(saveCallback);
    }
    
    service.disableEdits = function() {
    	service.map.disableEdits();
    }
    
    service.getGeoJSONData = function(callback, config, workspace) {
       	var params = {
                REQUEST:'GetFeature',
                SERVICE:'WFS',
                VERSION:'2.0.0',
                TYPENAMES:workspace +":"+ config.layerName,
//                CQL_FILTER : "geoid='"+ config.geoId + "'",
                //FEATUREID : featureJSON.featureId,  // We can't use featureid because our views don't include a dedicated primary key id
                outputFormat : 'application/json'
          };
	
          var url = window.location.origin+"/geoserver/" + workspace +"/wfs?" + $.param(params);
          
          $.ajax({
              url: url,
              context: document.body 
            }).done(function(response) {
              if(response.totalFeatures > 0) {
                callback(response);
              }
            });
    }
    
    service.closeEditSession = function(saveCallback){
    	service.map.closeEditSession(saveCallback);
    }
    
    service.focusOnFeature = function(feature) {
    	service.map.focusOnFeature(feature);
    }
    
    service.focusOffFeature = function(feature) {
    	service.map.focusOffFeature(feature);
    }
    
    service.zoomToExtentOfFeatures = function(featureGeoIds) {
    	service.map.zoomToExtentOfFeatures(featureGeoIds);
    }
    
    service.toggleBaseLayer = function(targetLayer, toggleOffLayer) {
    	service.map.toggleBaseLayer(targetLayer, toggleOffLayer);
    }
    
    service.addBaseMapControl = function(hoverCallback, hoverOffCallback) {
    	service.map.createBaseLayerControl(hoverCallback, hoverOffCallback);
    }
    
    service.restoreOriginalFeatures = function() {
    	service.map.restoreOriginalFeatures();
    }
    
    return service;
  }
  
  angular.module("map-service", []);
  angular.module('map-service')
    .factory('mapService', MapService);
})();