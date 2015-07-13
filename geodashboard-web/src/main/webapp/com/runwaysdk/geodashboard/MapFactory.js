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
	
  var MapWidgetName = GDB.Constants.GIS_PACKAGE+'MapWidget';
  
  com.runwaysdk.Localize.defineLanguage(MapWidgetName, {
	   "googleStreets" : "Google Streets",
	   "googleSatellite" : "Google Satellite",
	   "googleHybrid" : "Google Hybrid",
	   "googleTerrain" : "Google Terrain",
	   "osmBasic" : "Open Street Map",
	   "location" : "Location",
	   "aggregationMethod" : "Aggregation Method", 
	   "aggregateValue" : "Value",
	   "mqAerial" : "MapQuest Satellite",
	   "mqHybrid" : "MapQuest Hybrid"
  });
	  
  var MapWidget = Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.MapWidget', {
	Extends : com.runwaysdk.ui.Component,  
    IsAbstract : true,
    Constants : {
    	SRID : "EPSG:4326"
    },    
    Instance : {
      
      setMap : {
            IsAbstract : true
      },
      
      getMap : {
        IsAbstract : true
      },
      
      getCenter : {
          IsAbstract : true
      },
       
      getZoomLevel : {
          IsAbstract : true
      },
      
      getMapElementId : {
    	  IsAbstract : true
      },
      
      getMapConfig : {
    	  IsAbstract : true
      },
      
      getEnableClickEvents : {
    	  IsAbstract : true
      },
      
      getCallingThisRef : {
    	  IsAbstract : true
      },
      
      setCallingThisRef : {
    	  IsAbstract : true
      },
      
      getImpl : {
    	  IsAbstract : true
      },
      
      getCurrentBounds : {
    	  IsAbstract : true
      },
      
      /**
       * Returns the full extent of the map data
       * 
       * <public> - called externally
       */
      getBounds : function() {
    	  var bounds = this._bounds;
    	  
    	  // Constructing a standard format
    	  var boundsFormatted = {
    			  _southWest : {lat : bounds[0], lng : bounds[1]},
    			  _northEast : {lat: bounds[2], lng : bounds[3]}
    			  };
    	  
    	  return boundsFormatted;
      },
      
      /**
       * Set the full extent of the map data 
       * 
       * Expects: [southwest long, southwest lat, northeast long, northeast lat]
       * 
       * <private> - internal method
       */
      setBounds : function(bounds) {
    	  this._bounds = bounds;
      },
      
      configureMap : {
    	  IsAbstract : true
      },
      
      showLayer : {
    	  IsAbstract : true
      },
      
      hideLayer : {
    	  IsAbstract : true
      },
      
      _mapClickHandler : {
    	  IsAbstract : true
      },
      
      createUserLayers : {
    	  IsAbstract : true
      },
      
      createBaseLayers : {
    	  IsAbstract : true
      },
      
      createReferenceLayers : {
    	  IsAbstract : true
      },
      
      setView : {
    	  IsAbstract : true
      },
      
      showClickPopup : {
    	  IsAbstract : true
      },
      
      removeClickPopup : {
    	  IsAbstract : true
      },
      
      renderMap : {
    	  IsAbstract : true
      }
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
        this.callingThisRef = callingThisRef;
        this._config = {zoomAnimation: true, zoomControl: true, attributionControl: true};
        
        this.renderMap(callingThisRef);
      },
      
      /**
       * <private> - internal method
       */
      setMap : function(map) {
    	  this._map = map;
      },
      
      /**
       * <public> - called externally
       */
      getMap : function() {
    	  return this._map;
      },
      
      /**
       * <private> - internal method
       */
      getCenter : function() {
    	  return this._center;
      },
      
      /**
       * <private> - internal method
       */
      getZoomLevel : function() {
    	  return this.zoomLevel;
      },
      
      /**
       * <private> - internal method
       */
      getMapElementId : function() {
    	  return this._elementId;
      },
      
      /**
       * <private> - internal method
       */
      getMapConfig : function() {
    	  return this._configuration;
      },
      
      /**
       * <private> - internal method
       */
      getEnableClickEvents : function() {
    	  return this.enableClickEvents;
      },
      
      /**
       * <private> - internal method
       */
      getCallingThisRef : function() {
    	  return this.callingThisRef;
      },
      
      /**
       * <private> - internal method
       */
      setCallingThisRef : function(thisRef) {
    	  this.callingThisRef = thisRef;
      },
      
      /**
       * <private> - internal method
       */
      getImpl : function() {        
          return $(this._elementId);
      },    
      
      /**
       * <private> - internal method
       */
      getMapSize : function() {
    	  var map = this.getMap();
    	  var mapSize = map.getSize();  
    	  var mapSizeFormatted = {"x": mapSize.x, "y": mapSize.y};
    	  return mapSizeFormatted;
      },
      
      /**
       * Return bounds as an object
       */
      getCurrentBounds : function() {
    	  var map = this.getMap();
    	  var bounds = map.getBounds();
          
    	  // Constructing a standard format
    	  var boundsFormatted = {
    			  _southWest : {lat : bounds._southWest.lat, lng : bounds._southWest.lng},
    			  _northEast : {lat: bounds._northEast.lat, lng : bounds._northEast.lng}
    			  };
    	  
    	  return boundsFormatted;
      },
      
      /**
       * Return bounds as a string 
       * 
       * <private> - internal method
       */
      getCurrentBoundsAsString : function() {
    	  var bounds = this.getCurrentBounds();
    	  var boundsStr = [bounds._southWest.lng, bounds._southWest.lat, bounds._northEast.lng, bounds._northEast.lat].toString();
    	  return boundsStr;
      },
      
      /**
       * <private> - internal method
       */
      configureMap : function() {
    	  var map = this.getMap();
    	  map.attributionControl.setPrefix('');
      },
      
      /**
       * Get point position relative to map size and zoom level
       * 
       * @latLng - array of coord point in form [lat, lng]
       * 
       * <private> - internal method
       */
      coordToMapPoint : function(latLng) {
    	  var map = this.getMap();
    	  var latLngPt = new L.LatLng(latLng[0], latLng[1]);
    	  
    	  return map.latLngToContainerPoint(latLngPt, map.getZoom());
      },
      
      /**
       * Display the layer on the map
       * 
       * @layer - layer object
       * @stackIndex - integer value z-index of the layer 
       * 
       * <public> - called externally
       */
      showLayer : function(layer) {
    	  var map = this.getMap();
    	  map.addLayer(layer);
    	  
    	  // The OSM tileLayer isn't set at the bottom by default so this sets it as so
          if(layer._gdbcustomtype === "OSM"){
            map.attributionControl.setPrefix('');
            layer.bringToBack();
          }
      },
      
      /**
       * Hide the layer on the map
       * 
       * @layer - layer object
       * 
       * <public> - called externally
       */
      hideLayer : function(layer) {
    	  var map = this.getMap();
    	  map.removeLayer(layer);
      },
      
      /**
       * Create and return an array of all base layer objects.
       * 
       * <public> - called externally
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
       * 
       * <public> - called externally
       */
      createUserLayers : function(layers, geoserverWorkspace, removeExisting) {
    	  // Remove any already rendered layers from the leaflet map
          if (removeExisting === true) {
            for (var i = 0; i < layers.length; i++) {
              var layer = layers[i];
              
              if (layer.wmsLayerObj != null) {
                this.hideLayer(layer.wmsLayerObj);
              }
            }
          }
          
          
          //
          // Add thematic layers to leaflet map
          //
          for (var i = 0; i < layers.length; i++) {
            var layer = layers[i];
            if (layer.getLayerIsActive() === true && (removeExisting !== false || (removeExisting === false && layer.wmsLayerObj == null))) {
            	this.constructLayerObj(layer, geoserverWorkspace);
            }
          }
      },
      
      
      /**
       * Create and return an array of all reference layer objects.
       * 
       * <public> - called externally
       */
      createReferenceLayers : function(refLayers, geoserverWorkspace, removeExisting) {
    	    // Remove any already rendered layers from the leaflet map
          if (removeExisting === true) {
            for (var i = 0; i < refLayers.length; i++) {
                var layer = refLayers[i];
                
                if (layer.wmsLayerObj && layer.wmsLayerObj != null) {
                  this.hideLayer(layer.wmsLayerObj);
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
              this.constructLayerObj(layer, geoserverWorkspace);
            }
          }
      },
      
      
      /**
       * Build the wms layer object
       * 
       * @layer - layer object
       * @geoserverWorkspace = name of the geoserver workspace
       * 
       * <private> - internal method
       */
      constructLayerObj : function(layer, geoserverWorkspace){
    	  var viewName = layer.getViewName();
          var geoserverName = geoserverWorkspace + ":" + viewName;
    	  
          // This tiling format (tileLayer) is the preferred way to render wms due to performance gains but 
          // REQUIRES THAT META TILING SIZE BE SET TO A LARGE VALUE (I.E. 20) TO REDUCE BUBBLE CHOPPING.
          // We could get slightly better performance by setting tiled: false for non-bubble layers but 
          // this is currently unnecessary addition of code for relatively small performance gain.
            var mapBounds = this.getCurrentBounds();
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
            layer.wmsLayerObj = leafletLayer;
      },
      
      /**
       * Sets the bounds and zoom level of the map
       * 
       * @bounds - JSON array of coordinates {southwest lat, southwest long, northeast lat, northeast long}
       * @zoomLevel - integer zoom level
       * 
       * <public> - called externally
       */
      setView : function(bounds, zoomLevel) {
    	  var map = this.getMap();
    	  
    	  if(!zoomLevel){
    		  zoomLevel = 9;
    	  }
    	  
          // Handle points (2 coord sets) & polygons (4 coord sets)
          if (bounds.length === 2){
            var center = L.latLng(bounds[1], bounds[0]);
            
            map.setView(center, zoomLevel);
          }
          else if (bounds.length === 4){
            var swLatLng = L.latLng(bounds[1], bounds[0]);
            var neLatLng = L.latLng(bounds[3], bounds[2]);            
            var lBounds = L.latLngBounds(swLatLng, neLatLng);   

            map.fitBounds(lBounds);
          }
      },
      
      /**
       * <private> - internal method
       */
      removeStaleMapFragments : function() {
    	  var map = this.getMap();
          map.remove();
          
    	  $('#'+this.getMapElementId()).html('');
      },
      
      
      /**
       * Performs the identify request when a user clicks on the map
       * 
       * @param id
       * 
       * <private> - internal method
       */
      _mapClickHandler : function(e) {
    	  
    	var callingThisRef = this.getCallingThisRef();
        
    	// The 'this' is a this reference from the calling code to get access to the layer cache
        var layers = callingThisRef._layerCache.$values().reverse();
        
        if(layers.length > 0) {
          
          // Construct a GetFeatureInfo request URL given a point        
          var point = this.coordToMapPoint([ e.latlng.lat, e.latlng.lng ]);
          var size = this.getMapSize();        
          var mapBbox = this.getCurrentBoundsAsString();
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
                attributeValue = callingThisRef._formatter(attributeValue);
              }
              else if(!isNaN(Date.parse(attributeValue.substring(0, attributeValue.length - 1)))){
                var slicedAttr = attributeValue.substring(0, attributeValue.length - 1);
                var parsedAttr = $.datepicker.parseDate('yy-mm-dd', slicedAttr);
                attributeValue = callingThisRef._formatDate(parsedAttr);
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
                callingThisRef._renderReport(layer.getLayerId(), callingThisRef._currGeoId, callingThisRef._criteria);
              }            
            }
            
            if(popupContent.length > 0){
              that.showClickPopup(popupContent, [ e.latlng.lat, e.latlng.lng ]);
            }
          });
        }
      },
      
      /**
       * Open a popup on the map
       * 
       * <private> - internal method
       */
      showClickPopup : function(content, latLng) {
    	  var map = this.getMap();
    	  var popup = L.popup().setLatLng(new L.LatLng(latLng[0], latLng[1]));
    	  popup.setContent(content).openOn(map);
      },
      
      /**
       * Close all popups on the map
       * 
       * <public> - called externally
       */
      removeClickPopup : function() {
    	  var map = this.getMap();
    	  map.closePopup();
      },
      
      
      /**
       * Instantiate a new map
       * 
       * @callingThisRef - the this refernce from calling code which is needed to gain access to DynamicMap scope
       * 
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
      }
    },
    Static : {
    	// Static methods
      }
  });
  
  
  
  var OpenLayersMap = Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.OpenLayersMap', {
	    Extends : MapWidget,  
	    IsAbstract : false,
	    Constants : {
	    	
	    },   
	    Instance : {
	      initialize : function(elementId, center, zoomLevel, enableClickEvents, callingThisRef){
	        this._center = center || [0,0];
	        this._zoomLevel = zoomLevel || 2;
	        this._elementId = elementId;    
	        this._map = null;
	        this.enableClickEvents = enableClickEvents;
	        this.callingThisRef = callingThisRef;
	        
	        this._bounds = null;
	        this._config = {zoomAnimation: true, zoomControl: true, attributionControl: true};
	        
	        this.renderMap(callingThisRef);
	      },
	      
	      /**
	       * <public> - called externally
	       */
	      getElementId : function() {
	    	  return this._elementId;
	      },
	      
	      /**
	       * <private> - internal method
	       */
	      setMap : function(map) {
	    	  this._map = map;
	      },
	      
	      /**
	       * <public> - called externally
	       */
	      getMap : function() {
	    	  return this._map;
	      },
	      
	      /**
	       * <private> - internal method
	       */
	      getCenter : function() {
	    	  return this._center;
	      },
	      
	      /**
	       * <private> - internal method
	       */
	      getZoomLevel : function() {
	    	  return this._zoomLevel;
	      },
	      
	      /**
	       * <private> - internal method
	       */
	      getMapElementId : function() {
	    	  return this._elementId;
	      },
	      
	      /**
	       * <private> - internal method
	       */
	      getMapConfig : function() {
	    	  return this._configuration;
	      },
	      
	      /**
	       * <private> - internal method
	       */
	      getEnableClickEvents : function() {
	    	  return this.enableClickEvents;
	      },
	      
	      /**
	       * <private> - internal method
	       */
	      getCallingThisRef : function() {
	    	  return this.callingThisRef;
	      },
	      
	      /**
	       * <private> - internal method
	       */
	      setCallingThisRef : function(thisRef) {
	    	  this.callingThisRef = thisRef;
	      },
	      
	      /**
	       * <private> - internal method
	       */
	      getImpl : function() {        
	          return $(this._elementId);
	      },    
	      
	      /**
	       * <private> - internal method
	       */
	      getMapSize : function() {
	    	  var map = this.getMap();
	    	  var mapSize = map.getSize();  
	    	  var mapSizeFormatted = {"x": mapSize[0], "y": mapSize[1]};
	    	  return mapSizeFormatted;
	      },
	      
	      
	      /**
	       * Return bounds as an object
	       * 
	       * <private> - internal method
	       */
	      getCurrentBounds : function() {
	    	  var map = this.getMap();
	    	  var bounds = map.getView().calculateExtent(map.getSize());
	    	  var boundsTransformed = ol.extent.applyTransform(bounds, ol.proj.getTransform('EPSG:3857', MapWidget.SRID));
	          
	    	  // Constructing a standard format
	    	  var boundsFormatted = {
	    			  _southWest : {lat : boundsTransformed[0], lng : boundsTransformed[1]},
	    			  _northEast : {lat: boundsTransformed[2], lng : boundsTransformed[3]}
	    			  };
	    	  
	    	  return boundsFormatted;
	      },
	      
	      /**
	       * Return bounds as a string. The order of the bounds is required by OpenLayers 3
	       *  =  [sw lat, sw long, ne lat, ne long]
	       * 
	       * <private> - internal method
	       */
	      getCurrentBoundsAsString : function() {
	    	  var bounds = this.getCurrentBounds();
	    	  var boundsStr = [bounds._southWest.lat, bounds._southWest.lng, bounds._northEast.lat, bounds._northEast.lng].toString();
	    	  return boundsStr;
	      },
	      
	      /**
	       * Set basic properties of the map
	       * 
	       * <private> - internal method
	       */
	      configureMap : function() {
	    	  var map = this.getMap();
	    	  
	    	  map.getViewport().style.cursor = "-webkit-grab";
	    	  map.on('pointerdrag', function(evt) {
	    	      map.getViewport().style.cursor = "-webkit-grabbing";
	    	  });

	    	  map.on('pointerup', function(evt) {
	    	      map.getViewport().style.cursor = "-webkit-grab";
	    	  });
	    	  
	    	  //TODO: set attribution and other properties
	    	  
	      },
	      
	      /**
	       * Get point position relative to map size and zoom level
	       * 
	       * @latLng - array of coord point in form [lat, lng]
	       */
	      coordToMapPoint : function(latLng) {
	    	  // TODO: add openlayers code
	      },
	      
	      /**
	       * Display the layer on the map
	       * 
	       * @layer - layer object
	       * @stackIndex - integer value z-index of the layer 
	       * 
	       * <public> - called externally
	       * 
	       * TODO: bubble chopping is worse with openlayers. fix it
	       */
	      showLayer : function(layer, stackIndex) {
	    	  var map = this.getMap();
	    	  
	    	  if(stackIndex !== null && stackIndex >= 0){
	    		  map.getLayers().insertAt(stackIndex, layer);
	    	  }
	    	  else{
	    		  // will add the layer to the top of all other layers
	    		  map.addLayer(layer);
	    	  }
	      },
	      
	      /**
	       * Hide the layer on the map
	       * 
	       * @layer - layer object
	       * 
	       * <public> - called externally
	       */
	      hideLayer : function(layer) {
	    	  var map = this.getMap();
	    	  map.removeLayer(layer);
	      },
	      
	      /**
	       * Create and return an array of all base layer objects.
	       * 
	       * <public> - called externally
	       */
	      createBaseLayers : function(){
	        
	    	  // A hack to use google maps :(
//	    	  var gmap = new google.maps.Map(document.getElementById('mapDivId'), {
//	    		  center: new google.maps.LatLng(0, 0),
//	    		  zoom: this.getZoomLevel(),
//	    		  disableDefaultUI: true,
//	    		  keyboardShortcuts: false,
//	    		  draggable: true,
//	    		  disableDoubleClickZoom: false,
//	    		  scrollwheel: true,
//	    		  streetViewControl: false
//	    		});
//	        gmap._gdbCustomLabel = this.localize("googleStreets");
//	        
//	        gmap.controls[google.maps.ControlPosition.TOP_LEFT].push(olmap);
//	        
//	        var map = this.getMap();
//	        var view = map.getView();
//	        
//	        view.on('change:center', function() {
//	          var center = ol.proj.transform(view.getCenter(),
//	              'EPSG:3857', 'EPSG:4326');
//	          gmap.setCenter(new google.maps.LatLng(center[1], center[0]));
//	        });
//	        view.on('change:resolution', function() {
//	          gmap.setZoom(view.getZoom());
//	        });
	        
	    	  
	    	// TODO: Set min/max zoom levels or on zoom behavior to account for mapquest not displaying 
	    	 // at low zoom levels
	        var osm = new ol.layer.Tile({ 
	        	source: new ol.source.OSM({ 
  					  attributions: [ 
  					                  new ol.Attribution({
  					                	  html: 'All maps © ' + '<a href="http://www.openstreetmap.org/">OpenStreetMap</a>'
  					                  }),
  					                  ol.source.OSM.ATTRIBUTION
  					                ]
  				  })
  		  	});
	        osm._gdbcustomtype = 'OSM';
	        osm._gdbCustomLabel = this.localize("osmBasic");
	        
	        var mqAerial = new ol.layer.Tile({ 
	        	style: 'Aerial',
	        	visible: true,
	        	source: new ol.source.MapQuest({
	        		  layer: 'sat'
  				  })
  		  	});
	        mqAerial._gdbCustomLabel = this.localize("mqAerial");
	        
	        var mqHybrid = new ol.layer.Group({
	            style: 'AerialWithLabels',
	            visible: true,
	            layers: [
	              new ol.layer.Tile({
	                source: new ol.source.MapQuest({layer: 'sat'})
	              }),
	              new ol.layer.Tile({
	                source: new ol.source.MapQuest({layer: 'hyb'})
	              })
	            ]
	          });
	        mqHybrid._gdbCustomLabel = this.localize("mqHybrid");
	        
	        var base = [osm, mqAerial, mqHybrid];
	        
	        return base;
	      },
	      
	      
	      /**
	       * Create and return an array of all user defined layer objects.
	       * 
	       * @layers - array of layer objects in the order of desired stacking order (i.e. position 0 = bottom)
	       * @geoserverWorkspace - the geoserver workspace name
	       * @removeExisting - bool flag for if existing layers should be removed
	       * 
	       * <public> - called externally
	       */
	      createUserLayers : function(layers, geoserverWorkspace, removeExisting) {
	    	  // Remove any already rendered layers from the leaflet map
	          if (removeExisting === true) {
	            for (var i = 0; i < layers.length; i++) {
	              var layer = layers[i];
	              
	              if (layer.wmsLayerObj != null) {
	                this.hideLayer(layer.wmsLayerObj);
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
	            if (layer.getLayerIsActive() === true && (removeExisting !== false || (removeExisting === false && layer.wmsLayerObj == null))) {
	            	this.constructLayerObj(layer, geoserverWorkspace);
	            }
	          }
	      },
	      
	      
	      /**
	       * Create and return an array of all reference layer objects.
	       * 
	       * @refLayers - array of reference layer objects in the order of desired stacking order (i.e. position 0 = bottom)
	       * @geoserverWorkspace - the geoserver workspace name
	       * @removeExisting - bool flag for if existing layers should be removed
	       * 
	       * <public> - called externally
	       */
	      createReferenceLayers : function(refLayers, geoserverWorkspace, removeExisting) {
	    	  
	    	  // Remove any already rendered layers from the leaflet map
	          if (removeExisting === true) {
	            for (var i = 0; i < refLayers.length; i++) {
	                var layer = refLayers[i];
	                
	                if (layer.wmsLayerObj && layer.wmsLayerObj != null) {
	                  this.hideLayer(layer.wmsLayerObj);
	                }
	              }
	          }
	          
	          
	          //
	          // Add reference layers to the map
	          //
	          for (var i = 0; i < refLayers.length; i++) {
	            var layer = refLayers[i];
	            
	            // Since REFERENCEJSON layers are basic placeholders for actual mappable layers we will make sure none of them
	            // get through here.
	            if(layer.layerType !== "REFERENCEJSON"){
	              if (layer.getLayerIsActive() === true && (removeExisting !== false || (removeExisting === false && layer.wmsLayerObj == null))) {
	            	  this.constructLayerObj(layer, geoserverWorkspace);
	              }
	            }
	          }
	      },
	      
	      /**
	       * Build the wms layer object
	       * 
	       * @layer - layer object
	       * @geoserverWorkspace = name of the geoserver workspace
	       * 
	       * <private> - internal method
	       */
	      constructLayerObj : function(layer, geoserverWorkspace){
	    	  
	    	  // This tiling format (tileLayer) is the preferred way to render wms due to performance gains but 
              // REQUIRES THAT META TILING SIZE BE SET TO A LARGE VALUE (I.E. 20) TO REDUCE BUBBLE CHOPPING.
              // We could get slightly better performance by setting tiled: false for non-bubble layers but 
              // this is currently unnecessary addition of code for relatively small performance gain.
	    	  var viewName = layer.getViewName();
              var geoserverName = geoserverWorkspace + ":" + viewName;
              
              var mapBounds = this.getCurrentBounds();
                
              var wmsLayer = new ol.layer.Tile({
			    		source: new ol.source.TileWMS(({
			    			url: window.location.origin+"/geoserver/wms/",
			    			params: {
			    				'LAYERS': geoserverName, 
			    				'TILED': true,
			    				'STYLES': layer.getSldName() || "",
			    				'TILESIZE': 256,
			    				'FORMAT': 'image/png',
			    				'TILESORIGIN': mapBounds._southWest.lng + "," + mapBounds._southWest.lat
			    			},
			    			serverType: 'geoserver'
			    		})),
			    		visible: true
			  	});
	            
	            this.showLayer(wmsLayer, null);
	            layer.wmsLayerObj = wmsLayer;
	      },
	      
	      /**
	       * Sets the bounds and zoom level of the map
	       * 
	       * @bounds - <OPTIONAL> array of coordinates [southwest long, southwest lat, northeast long, northeast lat]
	       * @center - <OPTIONAL> array of coordintes [lat, long]
	       * @zoomLevel - <OPTIONAL> integer zoom level
	       * 
	       * <public> - called externally
	       */
	      setView : function(bounds, center, zoomLevel) {
	    	  var map = this.getMap();
	    	  
	    	  if(zoomLevel){
	    		  map.getView().setZoom(zoomLevel);
	    	  }
	    	  if(center){
	    		  map.getView().setCenter(center, MapWidget.SRID, 'EPSG:3857');
	    	  }
	    	  if(bounds){
	    	  
		          // Handle points (2 coord sets) & polygons (4 coord sets)
		          if (bounds.length === 2){
		        	// TODO: decide if this should be set as bounds  
		            //this.setBounds(bounds[0], bounds[1]);
		            
		        	// TODO: test 2 coordinate bounds input
		            map.getView().setCenter(bounds, MapWidget.SRID, 'EPSG:3857');
		          }
		          else if (bounds.length === 4){
		        	// OpenLayers 3 standard format = [minx, miny, maxx, maxy]
		            var bounds = [bounds[1], bounds[0], bounds[3], bounds[2]];
		            this.setBounds(bounds);
		          
			    	var fromattedBounds = this.getBounds();
			    	var bBox = [
		                         fromattedBounds._southWest.lng, 
		                         fromattedBounds._southWest.lat, 
		                         fromattedBounds._northEast.lng, 
		                         fromattedBounds._northEast.lat
		                         ];
			    	var areaExtent = ol.extent.applyTransform(bBox, ol.proj.getTransform(MapWidget.SRID, 'EPSG:3857'));
			    	map.getView().fit(areaExtent, map.getSize());
		          };
	    	  };
	      },
	      
	      /**
	       * <private> - internal method
	       */
	      removeStaleMapFragments : function() {
	    	  var map = this.getMap();
	          map.remove();
	          
	    	  $('#'+this.getMapElementId()).html('');
	      },
	      
	      
	      /**
	       * Performs the identify request when a user clicks on the map
	       * 
	       * @param id
	       * 
	       * <private> - internal method
	       */
	      _mapClickHandler : function(e) {
	    	
	    	// remove any exiting popups
	    	this.removeClickPopup();
	    	
	      	var callingThisRef = this.getCallingThisRef();
	      	var point = e.pixel;
	          
	      	// The 'this' is a this reference from the calling code to get access to the layer cache
	        var layers = callingThisRef._layerCache.$values().reverse();
	          
	          if(layers.length > 0) {
	            
	            // Construct a GetFeatureInfo request URL given a point        
//	            var point = this.coordToMapPoint([ e.latlng.lat, e.latlng.lng ]);
	            var size = this.getMapSize();        
	            var mapBbox = this.getCurrentBoundsAsString();
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
	            "&X="+ parseInt(point[0]) +
	            "&Y="+ parseInt(point[1]) +
	            "&BBOX="+ mapBbox +
	            "&LAYERS=geodashboard:"+ layerStringList +
	            "&QUERY_LAYERS=geodashboard:"+ layerStringList +
	            "&TYPENAME=geodashboard:"+ layerStringList;
	        
	            OpenLayersMap.that = this;
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
	                  attributeValue = callingThisRef._formatter(attributeValue);
	                }
	                else if(!isNaN(Date.parse(attributeValue.substring(0, attributeValue.length - 1)))){
	                  var slicedAttr = attributeValue.substring(0, attributeValue.length - 1);
	                  var parsedAttr = $.datepicker.parseDate('yy-mm-dd', slicedAttr);
	                  attributeValue = callingThisRef._formatDate(parsedAttr);
	                }
	                
	                popupContent += '<div id="popup" class="ol-popup"><a href="#" id="popup-closer" class="ol-popup-closer"></a><h3 class="popup-heading">'+layerDisplayName+'</h3>';
	                
	                var html = '';
	                html += '<table class="table">';
	                html += '<thead class="popup-table-heading">';
	                html += '<tr>'; 
	                html += '<th>'+OpenLayersMap.that.localize("location")+'</th>';  
	                html += '<th>'+OpenLayersMap.that.localize("aggregationMethod")+'</th>'; 
	                html += '<th>'+OpenLayersMap.that.localize("aggregateValue")+'</th>'; 
	                html += '</tr>';  
	                html += '</thead>';
	                html += '<tbody>';  
	                html += '<tr>'; 
	                html += '<td>'+ featureDisplayName +'</td>';  
	                html += '<td>' + aggregationMethod + '</td>'; 
	                html += '<td>' + attributeValue + '</td>';  
	                html += '</tr>';  
	                html += '</tbody>';  
	                html += '</table></div>';  
	                        
	                popupContent += html;
	                
	                var currGeoId = featureLayer.properties.geoid;
	                if(currGeoId != null)
	                {                 
	              	callingThisRef._currGeoId = currGeoId;
	                  callingThisRef._renderReport(layer.getLayerId(), callingThisRef._currGeoId, callingThisRef._criteria);
	                }            
	              }
	              
	              if(popupContent.length > 0){
	                that.showClickPopup(popupContent, e.coordinate);
	              }
	            });
	          }
	        
	    	  
	      },
	      
	      /**
	       * Open a popup on the map
	       * 
	       * <private> - internal method
	       */
	      showClickPopup : function(content, latLng) {
	    	  var that = this;
	    	  var map = this.getMap();
	    	  var popup = $.parseHTML(content)[0];
	    	    
	    	  var overlay = new ol.Overlay({
	    	        element:popup,
	    	        autoPan: true
	    	  });
	    	  
	    	  map.addOverlay(overlay);
	    	  overlay.setPosition(latLng);
	    	  
	    	  $("#popup-closer").click(function(e){
	    		  that.removeClickPopup();
	    	  });
	      },
	      
	      /**
	       * Close all popups on the map
	       * 
	       * <public> - called externally
	       */
	      removeClickPopup : function() {
	    	  var map = this.getMap();
	    	  var overlays = map.getOverlays();
	    	  overlays.clear();
	      },
	      
	      
	      /**
	       * Instantiate a new map
	       * 
	       * @callingThisRef - the this refernce from calling code which is needed to gain access to DynamicMap scope
	       * 
	       * <private> - internal method
	       */
	      renderMap : function(callingThisRef) {
	    	  if(this.getMap() != null){
	    		  this.removeStaleMapFragments();
	    	  }
	    	  
	    	  var map = new ol.Map({
	    		  layers: [ ], // base maps will be added later
	    		  //controls: ol.control.defaults({ attribution: true }).extend([attribution]),
	    		  target: this.getMapElementId(),
	    		  view: new ol.View({ 
	    			  center: this.getCenter(), 
	    			  zoom: this.getZoomLevel()
//	    			  projection: MapWidget.SRID
	    			  })
	    	  });
	    	  
	    	  this.setMap(map);
	    	  this.configureMap();
	    	  
	    	  
	    	  if(this.getEnableClickEvents()){
	    		  this.setCallingThisRef(callingThisRef);
	    		  
	    		  var mapClickHandlerBound = Mojo.Util.bind(this, this._mapClickHandler);
	    		  map.on("click", mapClickHandlerBound);
	    	  }
	      }
	    },
	    Static : {
	    	// Static methods
	      }
	  });
  
})();
  
  
