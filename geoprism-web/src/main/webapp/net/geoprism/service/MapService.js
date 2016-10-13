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
    
    service.setView = function(bounds, center, zoomLevel){
      service.map.setView(bounds, center, zoomLevel);    
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
    
    /////// NEW Services /////
    
    service.addVectorLayer = function(layer, styleObj, type, stackingIndex) {
    	service.map.addVectorLayer(layer, styleObj, type, stackingIndex);
    }
    
    service.zoomToVectorDataExtent = function() {
    	service.map.zoomToVectorDataExtent();
    }
    
    service.addVectorHoverEvents = function() {
    	service.map.addVectorHoverEvents();
    }
    
    service.addVectorClickEvents = function() {
    	service.map.addVectorClickEvents();
    }
    
    service.removeAllVectorLayers = function() {
    	service.map.removeAllVectorLayers();
    }
    
    service.enableEdits = function() {
    	service.map.enableEdits();
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
    
    ///////// SERVICES BELOW THIS ARE TEMPORARY ///////
    ///
    ///
    
    service.getMockJSONMapLayersParent = function(isHoverable, isClickable){
    	var data = {"type":"FeatureCollection","totalFeatures":1,"features":[
    	        {"type":"Feature","id":"USA-CO","properties":{"fips":"08","name":"Colorado"},"geometry":{"type":"Polygon","coordinates":[[[-107.919731,41.003906],[-105.728954,40.998429],[-104.053011,41.003906],[-102.053927,41.003906],[-102.053927,40.001626],[-102.042974,36.994786],[-103.001438,37.000263],[-104.337812,36.994786],[-106.868158,36.994786],[-107.421329,37.000263],[-109.042503,37.000263],[-109.042503,38.166851],[-109.058934,38.27639],[-109.053457,39.125316],[-109.04798,40.998429],[-107.919731,41.003906]]]}}]
    			}
    	
    	for(var i=0; i<data.features.length; i++){
    		var feature = data.features[i];
    		feature.properties.isHoverable = isHoverable;
    		feature.properties.isClickable = isClickable;
    	}
    	
    	return data;
    }
    
    service.getMockJSONMapLayersPolygons = function(isHoverable, isClickable){
    	var data = {"type":"FeatureCollection","properties":{"kind":"state","state":"CO"},"features":[
				{"type":"Feature","properties":{"kind":"county","name":"Boulder","state":"CO"},"geometry":{"type":"MultiPolygon","coordinates":[[[[-105.3401,40.2590],[-105.0553,40.2645],[-105.0553,40.0016],[-105.0553,39.9140],[-105.0553,39.9140],[-105.4003,39.9140],[-105.4387,39.9359],[-105.6742,39.9304],[-105.6906,40.0126],[-105.6413,40.0345],[-105.6304,40.1166],[-105.6797,40.1878],[-105.6523,40.2590]]]]}},
				{"type":"Feature","properties":{"kind":"county","name":"Jefferson","state":"CO"},"geometry":{"type":"MultiPolygon","coordinates":[[[[-105.0553,39.9140],[-105.0553,39.9140],[-105.0553,39.7935],[-105.0553,39.6675],[-105.0827,39.6675],[-105.0553,39.6511],[-105.1101,39.6292],[-105.0553,39.6237],[-105.0498,39.5635],[-105.1374,39.4704],[-105.1210,39.4320],[-105.1703,39.4046],[-105.2196,39.2622],[-105.3291,39.1308],[-105.3291,39.1308],[-105.4003,39.1308],[-105.4003,39.5635],[-105.4003,39.7497],[-105.4003,39.9140]]]]}}
				]}
    	
    	for(var i=0; i<data.features.length; i++){
    		var feature = data.features[i];
    		feature.properties.isHoverable = isHoverable;
    		feature.properties.isClickable = isClickable;
    	}
    	
    	return data;
    }
    
    service.getMockJSONMapLayersPoints = function(isHoverable, isClickable){
     	var data = {"type":"FeatureCollection","totalFeatures":1,"features":[
       	          {"type":"Feature","id":"1","properties":{"fips":"08","name":"test1"},"geometry":{"type":"Point","coordinates":[-107.919731,41.003906]}},
       			  {"type":"Feature","id":"2","properties":{"fips":"08","name":"test2"},"geometry":{"type":"Point","coordinates":[-105.0553,39.9140]}}
       	       ]}
     	
    	for(var i=0; i<data.features.length; i++){
    		var feature = data.features[i];
    		feature.properties.isHoverable = isHoverable;
    		feature.properties.isClickable = isClickable;
    	}
    	
    	return data;
    }
    
    return service;
  }
  
  angular.module("map-service", []);
  angular.module('map-service')
    .factory('mapService', MapService);
})();