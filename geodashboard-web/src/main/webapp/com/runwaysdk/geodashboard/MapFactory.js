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
	
  var LeafletMapName = GDB.Constants.GIS_PACKAGE+'LeafletMap';
  
  com.runwaysdk.Localize.defineLanguage(LeafletMapName, {
	   "googleStreets" : "Google Streets",
	   "googleSatellite" : "Google Satellite",
	   "googleHybrid" : "Google Hybrid",
	   "googleTerrain" : "Google Terrain",
	   "osmBasic" : "Open Street Map",
	   "location" : "Location",
	   "aggregationMethod" : "Aggregation Method", 
	   "aggregateValue" : "Value"
  });
	  
  var MapWidget = Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.MapWidget', {
	Extends : com.runwaysdk.ui.Component,  
    IsAbstract : true,
    Constants : {
    	SRID : "EPSG:4326"
    },    
    Instance : {
      
//      render : {
//        IsAbstract : true
//      },
//      getCategories:  {
//        IsAbstract : true
//      },
//      SOMEABSTRACTMETHOD : function (){
// 
//      }
    }
  });
  
  var LeafletMap = Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.LeafletMap', {
    Extends : MapWidget,  
    IsAbstract : false,
    Constants : {
    	
    },   
    Instance : {
      initialize : function(elementId, center, zoomLevel, enableClickEvents, callingThisRef){
        this._center = center;
        this._zoomLevel = zoomLevel;
        this._elementId = elementId;    
        this._map = null;
        this.enableClickEvents = enableClickEvents;
        this._config = {zoomAnimation: true, zoomControl: true, attributionControl: true};
        this.callingThisRef = callingThisRef;
        
        this.renderMap(callingThisRef);
      },
      
      setMap : function(map) {
    	  this._map = map;
      },
      
      getMap : function() {
    	  return this._map;
      },
      
      getCenter : function() {
    	  return this._center;
      },
      
      getZoomLevel : function() {
    	  return this.zoomLevel;
      },
      
      getMapElementId : function() {
    	  return this._elementId;
      },
      
      getMapConfig : function() {
    	  return this._configuration;
      },
      
      getEnableClickEvents : function() {
    	  return this.enableClickEvents;
      },
      
      getCallingThisRef : function() {
    	  return this.callingThisRef;
      },
      
      setCallingThisRef : function(thisRef) {
    	  this.callingThisRef = thisRef;
      },
      
      getImpl : function() {        
          return $(this._elementId);
      },      
      
      configureMap : function() {
    	  var map = this.getMap();
    	  map.attributionControl.setPrefix('');
    	  
    	  if(this.getCenter() != null && this.getZoomLevel != null){
    		  this.setView(this.getCenter(), this.getZoomLevel);
    	  }
      },
      
      /*
       * Get point position relative to map size and zoom level
       * 
       * @latLng - array of coord point in form [lat, lng]
       */
      coordToMapPoint : function(latLng) {
    	  var map = this.getMap();
    	  var latLngPt = new L.LatLng(latLng[0], latLng[1]);
    	  
    	  return map.latLngToContainerPoint(latLngPt, map.getZoom());
      },
      
      getMapSize : function() {
    	  var map = this.getMap();
    	  var mapSize = map.getSize();  
    	  var mapSizeFormatted = {"x": mapSize.x, "y": mapSize.y};
    	  return mapSizeFormatted;
      },
      
      /*
       * Return bounds as an object
       */
      getBounds : function() {
    	  var map = this.getMap();
    	  var bounds = map.getBounds();
          
    	  // Constructing a standard format
    	  var boundsFormatted = {
    			  _southWest : {lat : bounds._southWest.lat, lng : bounds._southWest.lng},
    			  _northEast : {lat: bounds._northEast.lat, lng : bounds._northEast.lng}
    			  };
    	  
    	  return boundsFormatted;
      },
      
      /*
       * Return bounds as a string 
       */
      getBoundsAsString : function() {
    	  var bounds = this.getBounds();
    	  var boundsStr = [bounds._southWest.lng, bounds._southWest.lat, bounds._northEast.lng, bounds._northEast.lat].toString();
    	  return boundsStr;
      },
      
      showLayer : function(layer) {
    	  var map = this.getMap();
    	  map.addLayer(layer);
    	  
    	  // The OSM tileLayer isn't set at the bottom by default so this sets it as so
          if(layer._gdbcustomtype === "OSM"){
            map.attributionControl.setPrefix('');
            layer.bringToBack();
          }
      },
      
      hideLayer : function(layer) {
    	  var map = this.getMap();
    	  map.removeLayer(layer);
      },
      
      /**
       * Create and return an array of all base layer objects.
       * 
       */
      createBaseLayers : function(){
        
        // the SATELLITE layer has all 22 zoom level, so we add it first to
        // become the internal base layer that determines the zoom levels of the
        // map.
        
        //var gsat = new L.Google('SATELLITE');   
    	//gsat._gdbcustomlabel = this.localize("googleSatellite");
        var gphy = new L.Google('TERRAIN');     
        gphy._gdbCustomLabel = this.localize("googleTerrain"); 
        
        var gmap = new L.Google('ROADMAP');    
        gmap._gdbCustomLabel = this.localize("googleStreets");
        
        var ghyb = new L.Google('HYBRID');
        ghyb._gdbCustomLabel = this.localize("googleHybrid");
        
        var osm = new L.TileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
          attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors'
          }); 
        osm._gdbcustomtype = 'OSM';
        osm._gdbCustomLabel = this.localize("osmBasic");
        
        var base = [osm, gmap, ghyb, gphy];
        
        return base;
      },
      
      
      /**
       * Create and return an array of all user defined layer objects.
       */
      createUserLayers : function(layers, geoserverWorkspace, removeExisting) {
    	  // Remove any already rendered layers from the leaflet map
          if (removeExisting === true) {
            for (var i = 0; i < layers.length; i++) {
              var layer = layers[i];
              
              if (layer.leafletLayer != null) {
                this.hideLayer(layer.leafletLayer);
              }
            }
          }
          
          
          //
          // Add thematic layers to leaflet map
          //
          for (var i = 0; i < layers.length; i++) {
            var layer = layers[i];
            var viewName = layer.getViewName();
            var geoserverName = geoserverWorkspace + ":" + viewName;
            if (layer.getLayerIsActive() === true && (removeExisting !== false || (removeExisting === false && layer.leafletLayer == null))) {
                // This tiling format (tileLayer) is the preferred way to render wms due to performance gains but 
              // REQUIRES THAT META TILING SIZE BE SET TO A LARGE VALUE (I.E. 20) TO PREVENT BUBBLE CHOPPING.
              // We could get slightly better performance by setting tiled: false for non-bubble layers but 
              // this is currently unnecessary addition of code for relatively small performance gain.
                var mapBounds = this.getBounds();
                var mapSWOrigin = [mapBounds._southWest.lat, mapBounds._southWest.lng];
                var leafletLayer = L.tileLayer.wms(window.location.origin+"/geoserver/wms/", {
                  layers: geoserverName,
                  format: 'image/png',
                  transparent: true,
                  tiled: true,
                  tileSize: 256,
                  tilesorigin: mapSWOrigin,
                  styles: layer.getSldName() || "" 
                });
            
                this.showLayer(leafletLayer);
                layer.leafletLayer = leafletLayer;
            }
          }
      },
      
      
      /**
       * Create and return an array of all reference layer objects.
       */
      createReferenceLayers : function(refLayers, geoserverWorkspace, removeExisting) {
    	    // Remove any already rendered layers from the leaflet map
          if (removeExisting === true) {
            for (var i = 0; i < refLayers.length; i++) {
                var layer = refLayers[i];
                
                if (layer.leafletLayer && layer.leafletLayer != null) {
                  this.hideLayer(layer.leafletLayer);
                }
              }
          }
          
          //
          // Add reference layers to leaflet map
          //
          for (var i = 0; i < refLayers.length; i++) {
            var layer = refLayers[i];
            
            // Since REFERENCEJSON layers are basic placeholders for actual mappable layers we will make sure none of them
            // get through here.
            if(layer.layerType !== "REFERENCEJSON"){
              if (layer.getLayerIsActive() === true && (removeExisting !== false || (removeExisting === false && layer.leafletLayer == null))) {
                var viewName = layer.getViewName();
                var geoserverName = geoserverWorkspace + ":" + viewName;
                
                  var mapBounds = this.getBounds();
                  var mapSWOrigin = [mapBounds._southWest.lat, mapBounds._southWest.lng];
                  var leafletLayer = L.tileLayer.wms(window.location.origin+"/geoserver/wms/", {
                    layers: geoserverName,
                    format: 'image/png',
                    transparent: true,
                    tiled: true,
                    tileSize: 256,
                    tilesorigin: mapSWOrigin,
                    styles: layer.getSldName() || "" 
                  });
              
                  this.showLayer(leafletLayer);
                  layer.leafletLayer = leafletLayer;
              }
            }
          }
      },
      
      /**
       * Sets the bounds and zoom level of the map
       * 
       * @bounds - JSON array of coordinates {southwest lat, southwest long, northeast lat, northeast long}
       */
      setView : function(bounds, zoomLevel) {
    	  var map = this.getMap();
    	  
    	  if(!zoomLevel){
    		  zoomLevel = 9;
    	  }
    	  
          // Handle points (2 coord sets) & polygons (4 coord sets)
          if (bounds.length === 2){
            var center = L.latLng(bounds[1], bounds[0]);
            
            map.setView(center, 9);
          }
          else if (bounds.length === 4){
            var swLatLng = L.latLng(bounds[1], bounds[0]);
            var neLatLng = L.latLng(bounds[3], bounds[2]);            
            var lBounds = L.latLngBounds(swLatLng, neLatLng);   

            map.fitBounds(lBounds);
          }
      },
      
      removeStaleMapFragments : function() {
    	  var map = this.getMap();
          map.remove();
          
    	  $('#'+this.getMapElementId()).html('');
      },
      
      
      /**
       * Performs the identify request when a user clicks on the map
       * 
       * @param id
       */
      _mapClickHandler : function(e) {
    	  
    	var callingThisRef = this.getCallingThisRef();
        
    	// The 'this' is a this reference from the calling code to get access to the layer cache
        var layers = callingThisRef._layerCache.$values().reverse();
        
        if(layers.length > 0) {
          
          // Construct a GetFeatureInfo request URL given a point        
          var point = this.coordToMapPoint([ e.latlng.lat, e.latlng.lng ]);
          var size = this.getMapSize();        
          var mapBbox = this.getBoundsAsString();
          var map = this.getMap();
          var layerMap = new Object();
          var layerStringList = '';
          var that = this;
        
          // Build a string of layers to query against but geoserver will only return the 
          // first entry in the array if anything is found. Otherwise it will query the next layer
          // until something is found.
          var firstAdded = false;
          for (var i = 0; i < layers.length; i++) { 
            var layer = layers[i];
          
            // If the layer object is active (visible on the map)
            if(layer.getLayerIsActive()){
              var layerId = layer.attributeMap.viewName.value;              
              layerMap[layerId] = layer;              
                
              if(firstAdded){
                layerStringList += "," + layerId;
              }
              else{
                layerStringList += layerId;
                firstAdded = true;
              }
            }
          }
        
        var requestURL = window.location.origin+"/geoserver/" + callingThisRef._workspace +"/wms?" +
          "REQUEST=GetFeatureInfo" +
          "&INFO_FORMAT=application/json" +
          "&EXCEPTIONS=APPLICATION/VND.OGC.SE_XML" +
          "&SERVICE=WMS" +
          "&SRS="+MapWidget.SRID +
          "&VERSION=1.1.1" +
          "&height=" + size.y +
          "&width=" + size.x +
          "&X="+ point.x +
          "&Y="+ point.y +
          "&BBOX="+ mapBbox +
          "&LAYERS=geodashboard:"+ layerStringList +
          "&QUERY_LAYERS=geodashboard:"+ layerStringList +
          "&TYPENAME=geodashboard:"+ layerStringList;
      
          LeafletMap.that = this;
          $.ajax({
              url: requestURL,
              context: document.body 
          }).done(function(json) {
            var popupContent = '';
            
            // The getfeatureinfo request will return only 1 feature
            for(var i = 0; i < json.features.length; i++){
              var featureLayer = json.features[i];
              var featureLayerIdReturn = featureLayer.id;
              var featureLayerId = featureLayerIdReturn.substring(0, featureLayerIdReturn.indexOf('.'));
              
              var layer = layerMap[featureLayerId];
              var layerDisplayName = layer.getLayerName();
              var aggregationMethod = layer.getAggregationMethod();
              var attributeName = layer.getAggregationAttribute().toLowerCase();
              
              var attributeValue = featureLayer.properties[attributeName];                            
              var featureDisplayName = featureLayer.properties.displaylabel;
              
              if(typeof attributeValue === 'number'){
                attributeValue = that._formatter(attributeValue);
              }
              else if(!isNaN(Date.parse(attributeValue.substring(0, attributeValue.length - 1)))){
                var slicedAttr = attributeValue.substring(0, attributeValue.length - 1);
                var parsedAttr = $.datepicker.parseDate('yy-mm-dd', slicedAttr);
                attributeValue = that._formatDate(parsedAttr);
              }
              
              popupContent += '<h3 class="popup-heading">'+layerDisplayName+'</h3>';
              
              var html = '';
              html += '<table class="table">';
              html += '<thead class="popup-table-heading">';
              html += '<tr>'; 
              html += '<th>'+LeafletMap.that.localize("location")+'</th>';  
              html += '<th>'+LeafletMap.that.localize("aggregationMethod")+'</th>'; 
              html += '<th>'+LeafletMap.that.localize("aggregateValue")+'</th>'; 
              html += '</tr>';  
              html += '</thead>';
              html += '<tbody>';  
              html += '<tr>'; 
              html += '<td>'+ featureDisplayName +'</td>';  
              html += '<td>' + aggregationMethod + '</td>'; 
              html += '<td>' + attributeValue + '</td>';  
              html += '</tr>';  
              html += '</tbody>';  
              html += '</table>';  
                      
              popupContent += html;
              
              var currGeoId = featureLayer.properties.geoid;
              if(currGeoId != null)
              {                 
            	callingThisRef._currGeoId = currGeoId;
                callingThisRef._renderReport(layer.getLayerId(), that._currGeoId, that._criteria);
              }            
            }
            
            if(popupContent.length > 0){
              that.showClickPopup(popupContent, [ e.latlng.lat, e.latlng.lng ]);
            }
          });
        }
      },
      
      /*
       * Open a popup on the map
       */
      showClickPopup : function(content, latLng) {
    	  var map = this.getMap();
    	  var popup = L.popup().setLatLng(new L.LatLng(latLng[0], latLng[1]));
    	  popup.setContent(content).openOn(map);
      },
      
      /*
       * Close all popups on the map
       */
      removeClickPopup : function() {
    	  var map = this.getMap();
    	  map.closePopup();
      },
      
      
      /**
       * Instantiate a new map
       * 
       * @thisRef - the this refernce from calling code
       * 
       * TODO: Should newMap() be moved to initialize()?
       */
      renderMap : function(callingThisRef) {
    	  if(this.getMap() != null){
    		  this.removeStaleMapFragments();
    	  }
    	  var map = new L.Map(this.getMapElementId(), this.getMapConfig());
    	  this.setMap(map);
    	  this.configureMap();
    	  
    	  
    	  if(this.getEnableClickEvents()){
    		  this.setCallingThisRef(callingThisRef);
    		  
    		  var mapClickHandlerBound = Mojo.Util.bind(this, this._mapClickHandler);
    		  map.on("click", mapClickHandlerBound);
    	  }
    	  
//    	  return this.getMap(); //TODO: remove this?  Not sure it really needs to be returned in the end
      }
    },
    Static : {
        /**
         * Convert an RGB or RGBA string in the form RBG(255,255,255) to #ffffff
         * 
         */
        rgb2hex : function(rgb) {
          if(rgb != null) {
              
            if (/^#[0-9A-F]{6}$/i.test(rgb)){
              return rgb;
            }

            var rgbMatch = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
            if(rgbMatch){
              function hex(x) {
                return ("0" + parseInt(x).toString(16)).slice(-2);
              }
              return "#" + hex(rgbMatch[1]) + hex(rgbMatch[2]) + hex(rgbMatch[3]);
            }
              
            var rgbaMatch = rgb.match(/^rgba?[\s+]?\([\s+]?(\d+)[\s+]?,[\s+]?(\d+)[\s+]?,[\s+]?(\d+)[\s+]?/i);
            if(rgbaMatch){
              return (rgbaMatch && rgbaMatch.length === 4) ? "#" +
                 ("0" + parseInt(rgbaMatch[1],10).toString(16)).slice(-2) +
                 ("0" + parseInt(rgbaMatch[2],10).toString(16)).slice(-2) +
                 ("0" + parseInt(rgbaMatch[3],10).toString(16)).slice(-2) : '';
            }
          }
        },
      }
  });
  
})();
  
  
