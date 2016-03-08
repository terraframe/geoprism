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
    service.map = new net.geoprismersMap("mapDivId", null, null);
    
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
    
    return service;
  }
  
  angular.module("map-service", []);
  angular.module('map-service')
    .factory('mapService', MapService);
})();