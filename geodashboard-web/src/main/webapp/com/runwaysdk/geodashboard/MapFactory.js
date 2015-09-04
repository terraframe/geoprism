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
     "mqHybrid" : "MapQuest Hybrid",
     "attributionELTooltip" : "Map Attribution",
     "editFeature" : "Edit feature"
  });
    
  var MapWidget = Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.MapWidget', {
  Extends : com.runwaysdk.ui.Component,  
    IsAbstract : true,
    Constants : {
      DATASRID : "EPSG:4326",
      MAPSRID : "EPSG:3857"
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
      
      getDynamicMap : {
        IsAbstract : true
      },
      
      setDynamicMap : {
        IsAbstract : true
      },
      
      getImpl : {
        IsAbstract : true
      },
      
      getCurrentBounds : {
        IsAbstract : true
      },
      
      /**
       * Returns the full extent of the map data in the data projection
       * 
       * <public> - called externally
       */
      getBounds : function() {
        var bounds = this._bounds;
        
        // Constructing a standard format
        var boundsFormatted = {};
        
        
        // Handle points (2 coord sets) & polygons (4 coord sets)
        // Point occurances are rare and would mean the data includes only a single point or overlapping points
          if (bounds.length === 2){
            boundsFormatted = {
              lat : bounds[1], lng : bounds[0]
            };
          }
          else if (bounds.length === 4){
            boundsFormatted = {
                _southWest : {lat : bounds[1], lng : bounds[0]},
                _northEast : {lat: bounds[3], lng : bounds[2]}
            };
          }
        
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
      initialize : function(elementId, center, zoomLevel, enableClickEvents, dynamicMap){
        this._center = center;
        this._zoomLevel = zoomLevel;
        this._elementId = elementId;    
        this._map = null;
        this.enableClickEvents = enableClickEvents;
        this._dynamicMap = dynamicMap;
        this._config = {zoomAnimation: true, zoomControl: true, attributionControl: true};
        
        this.renderMap(dynamicMap);
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
      getDynamicMap : function() {
        return this._dynamicMap;
      },
      
      /**
       * <private> - internal method
       */
      setDynamicMap : function(thisRef) {
        this._dynamicMap = thisRef;
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
        
        var dynamicMap = this.getDynamicMap();
        
        // The 'this' is a this reference from the calling code to get access to the layer cache
        var layers = dynamicMap.getCachedLayers().reverse();
        
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
        
        var requestURL = window.location.origin+"/geoserver/" + dynamicMap._workspace +"/wms?" +
          "REQUEST=GetFeatureInfo" +
          "&INFO_FORMAT=application/json" +
          "&EXCEPTIONS=APPLICATION/VND.OGC.SE_XML" +
          "&SERVICE=WMS" +
          "&SRS="+MapWidget.DATASRID +
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
              var geoId = featureLayer.properties.geoid;
              
              var layer = layerMap[featureLayerId];
              var layerDisplayName = layer.getLayerName();
              var aggregationMethod = layer.getAggregationMethod();
              var attributeName = layer.getAggregationAttribute().toLowerCase();
              
              var attributeValue = featureLayer.properties[attributeName];                            
              var featureDisplayName = featureLayer.properties.displaylabel;
              
              if(typeof attributeValue === 'number'){
                attributeValue = dynamicMap._formatter(attributeValue);
              }
              else if(!isNaN(Date.parse(attributeValue.substring(0, attributeValue.length - 1)))){
                var slicedAttr = attributeValue.substring(0, attributeValue.length - 1);
                var parsedAttr = $.datepicker.parseDate('yy-mm-dd', slicedAttr);
                attributeValue = dynamicMap._formatDate(parsedAttr);
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
              
              if(dynamicMap.canEditData() && geoId != null && (layer.aggregationStrategy.type == 'GeometryAggregationStrategy' || layer.aggregationStrategy.type == 'GEOMETRY') ) {
                  html += '<tr>'; 
                  html += '<td colspan="3"><a class="edit-feature" data-layerid="' + layer.getLayerId() + '" data-geoid="' + geoId + '">' + that.localize("editFeature") + '</a></td>';  
                  html += '</tr>';  
                }              
              
              html += '</tbody>';  
              html += '</table>';  
                      
              popupContent += html;
              
              if(geoId != null)
              {                 
                dynamicMap.setCurrGeoId(currGeoId);
                dynamicMap._renderReport(layer.getLayerId(), geoId, dynamicMap._criteria);
              }            
            }
            
            if(popupContent.length > 0){
              that.showClickPopup(popupContent, [ e.latlng.lat, e.latlng.lng ]);
              
              
              // Hook-up the edit click event
              $(".edit-feature").click(function(e){
                var layerId = $(this).data("layerid");
                var geoId = $(this).data("geoid");
                
                dynamicMap.editFeature(layerId, geoId);
              });              
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
       * @dynamicMap - the this refernce from calling code which is needed to gain access to DynamicMap scope
       * 
       */
      renderMap : function(dynamicMap) {
        if(this.getMap() != null){
          this.removeStaleMapFragments();
        }
        var map = new L.Map(this.getMapElementId(), this.getMapConfig());
        this.setMap(map);
        this.configureMap();
        
        
        if(this.getEnableClickEvents()){
          this.setDynamicMap(dynamicMap);
          
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
        initialize : function(elementId, center, zoomLevel, enableClickEvents, dynamicMap){
          this._center = center || [0,0];
          this._zoomLevel = zoomLevel || 2;
          this._elementId = elementId;    
          this._map = null;
          this.enableClickEvents = enableClickEvents;
          this._dynamicMap = dynamicMap;
          
          this._bounds = null;
          this._config = {zoomAnimation: true, zoomControl: true, attributionControl: true};
          
          this.renderMap(dynamicMap);
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
        getDynamicMap : function() {
          return this._dynamicMap;
        },
        
        /**
         * <private> - internal method
         */
        setDynamicMap : function(thisRef) {
          this._dynamicMap = thisRef;
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
         * Return bounds as an object in the map projection (epsg:3857 by default)
         * 
         * @epsgCode - target projection for the bounds
         * 
         * <private> - internal method
         */
        getCurrentBounds : function(epsgCode) {
          var map = this.getMap();
          var mapBounds = map.getView().calculateExtent(map.getSize());
          var bounds = {};
            
          if(epsgCode !== MapWidget.MAPSRID){
            bounds = ol.extent.applyTransform(mapBounds, ol.proj.getTransform(MapWidget.MAPSRID, epsgCode));
          }
          else{
            bounds = mapBounds;
          }
          
          // Constructing a standard format
          var boundsFormatted = {
              _southWest : {lng : bounds[0], lat : bounds[1]},
              _northEast : {lng: bounds[2], lat : bounds[3]}
              };
          
          return boundsFormatted;
        },
        
        /**
         * Return bounds as a string. The order of the bounds is required by OpenLayers 3
         *  =  [sw lat, sw long, ne lat, ne long]
         *  
         * @epsgCode - target projection for the bounds
         * 
         * <private> - internal method
         */
        getCurrentBoundsAsString : function(epsgCode) {
          var bounds = this.getCurrentBounds(epsgCode);
          var boundsStr = [bounds._southWest.lng, bounds._southWest.lat, bounds._northEast.lng, bounds._northEast.lat].toString();
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
	        var baseObjArr = [];
	        var baseMapsArr = MapConfig._BASEMAPS;
	        for(var i=0; i<baseMapsArr.length; i++){
	        	var base = baseMapsArr[i];
	        	if(base.LAYER_TYPE.toLowerCase() === "tile"){
	        		var baseObj = new ol.layer.Tile(
	        				{visible: base.VISIBLE},
	        				base.CUSTOM_TYPE_OPTIONS
	        			);
	        		
	        		if(base.LAYER_SOURCE_TYPE.toLowerCase() === "osm"){
	        			
	        			baseObj.setSource( 
	        				new ol.source.OSM(base.LAYER_SOURCE_OPTIONS)
	        			);
	        		}
	        		else if(base.LAYER_SOURCE_TYPE.toLowerCase() === "mapquest"){
	        			baseObj.setSource( 
	        				new ol.source.MapQuest(base.LAYER_SOURCE_OPTIONS)
	        			);
	        		}
	        		
        			baseObj._gdbisdefault = base.DEFAULT;
        			baseObj._gdbcustomtype = base.LAYER_SOURCE_TYPE;
            		baseObj._gdbCustomLabel = this.localize(base.LOCLIZATION_KEY);
            		
	        		baseObjArr.push(baseObj);
	        	}
	        	else if(base.LAYER_TYPE.toLowerCase() === "group"){
	        		var layersArr = [];
	        		
	        		var baseObj = new ol.layer.Group(
	        				{visible: base.VISIBLE},
	        				{isdefault: base.DEFAULT},
	        				base.CUSTOM_TYPE_OPTIONS);
	        		
	        		for(var gi=0; gi<base.GROUP_LAYERS.length; gi++){
	        			var layer = base.GROUP_LAYERS[gi];
	        			
	        			if(layer.LAYER_TYPE.toLowerCase() === "tile"){
	        				var layerObj =  new ol.layer.Tile(layer.LAYER_TYPE_OPTIONS);
	        				
	        				if(layer.LAYER_SOURCE_TYPE.toLowerCase() === "mapquest"){
	        					layerObj.setSource(new ol.source.MapQuest(layer.LAYER_SOURCE_OPTIONS));
	        				}
	        				
	        				layersArr.push(layerObj);
	        			}
	        		}
	        		
	        		baseObj.setLayers(new ol.Collection(layersArr));
	        		
	        		baseObj._gdbisdefault = base.DEFAULT;
	        		baseObj._gdbcustomtype = base.LAYER_SOURCE_TYPE;
	        		baseObj._gdbCustomLabel = this.localize(base.LOCLIZATION_KEY);
	        		
	        		baseObjArr.push(baseObj);
	        	}
        }
        	
        	
        // TODO: Set min/max zoom levels or on zoom behavior to account for mapquest not displaying 
         // at low zoom levels
          
          return baseObjArr;
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
              
              
              //
              //  Implementation of meta-tiling is not working. Until a fix is found we will use single tile requests
              //
//              var mapBounds = this.getCurrentBounds(MapWidget.DATASRID);
//              var mapSWOrigin = [mapBounds._southWest.lng, mapBounds._southWest.lat].toString();
//                
//              var fullExtent = this.getBounds();
//              var ext = [fullExtent._southWest.lng, fullExtent._southWest.lat, fullExtent._northEast.lng, fullExtent._northEast.lat];
//              var projectionExtent = [-20026376.39, -20048966.10, 20026376.39, 20048966.10]; //epsg:3857 extent
//              var wmsLayer = new ol.layer.Tile({
//                  extent: projectionExtent, 
//                  preload: true,
//            source: new ol.source.TileWMS({
//              url: window.location.origin+"/geoserver/wms/",
//              params: {
//                'LAYERS': geoserverName, 
//                'VERSION': '1.3',
//                'CRS': MapWidget.MAPSRID,
//                'BBOX': ext,
//                'TILED': true,
//                'STYLES': layer.getSldName() || "",
//                'TILESIZE': 256,
//                'FORMAT': 'image/png',
//                'TILESORIGIN': mapSWOrigin
//              },
//              serverType: 'geoserver'
//            }),
//            visible: true
//        });
              //
              // end of meta-tiling block
              //
              
              // Single Tile format
              var wmsLayer = new ol.layer.Image({
            source: new ol.source.ImageWMS({
              url: window.location.origin+"/geoserver/wms/",
              params: {
                'LAYERS': geoserverName, 
                'TILED': true,
                'STYLES': layer.getSldName() || "",
                'FORMAT': 'image/png'
              },
              serverType: 'geoserver'
            }),
            visible: true
        });
              
              this.showLayer(wmsLayer, null);
              layer.wmsLayerObj = wmsLayer;
        },
        
        /**
         * Sets the bounds and zoom level of the map. 
         * @bounds can be set independanty.
         * @center & @zoomLevel must be set together
         * 
         * @bounds - <OPTIONAL> array of coordinates [southwest long, southwest lat, northeast long, northeast lat]
         * @center - <OPTIONAL> array of coordintes [long, lat]
         * @zoomLevel - <OPTIONAL> integer zoom level
         * 
         * <public> - called externally
         */
        setView : function(bounds, center, zoomLevel) {
          var map = this.getMap();
          
          if(center && zoomLevel){
            map.getView().setCenter(center, MapWidget.DATASRID, MapWidget.MAPSRID);
            map.getView().setZoom(zoomLevel);
          }
          else if(bounds){
              // Handle points (2 coord sets) & polygons (4 coord sets)
              if (bounds.length === 2){
                this.setBounds(bounds[0], bounds[1]);
                
                map.getView().setCenter(bounds, MapWidget.DATASRID, MapWidget.MAPSRID);
              }
              else if (bounds.length === 4){
              // OpenLayers 3 standard format = [minx, miny, maxx, maxy]
                this.setBounds([bounds[0], bounds[1], bounds[2], bounds[3]]);
              
            var fromattedBounds = this.getBounds();
            var bBox = [
                             fromattedBounds._southWest.lng, 
                             fromattedBounds._southWest.lat, 
                             fromattedBounds._northEast.lng, 
                             fromattedBounds._northEast.lat
                             ];
            var areaExtent = ol.extent.applyTransform(bBox, ol.proj.getTransform(MapWidget.DATASRID, MapWidget.MAPSRID));
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
        
          var dynamicMap = this.getDynamicMap();
          var point = e.pixel;
            
          // The 'this' is a this reference from the calling code to get access to the layer cache
          var layers = dynamicMap.getCachedLayers().reverse();
            
          if(layers.length > 0) {
              
            // Construct a GetFeatureInfo request URL given a point        
            var size = this.getMapSize();        
            var mapBbox = this.getCurrentBoundsAsString(MapWidget.DATASRID);
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
            
            var requestURL = window.location.origin+"/geoserver/" + dynamicMap._workspace +"/wms?" +
              "REQUEST=GetFeatureInfo" +
              "&INFO_FORMAT=application/json" +
              "&EXCEPTIONS=APPLICATION/VND.OGC.SE_XML" +
              "&SERVICE=WMS" +
              "&SRS="+MapWidget.DATASRID +
              "&VERSION=1.1.1" +
              "&height=" + size.y +
              "&width=" + size.x +
              "&X="+ parseInt(point[0]) +
              "&Y="+ parseInt(point[1]) +
              "&BBOX="+ mapBbox +
              "&LAYERS=geodashboard:"+ layerStringList +
              "&QUERY_LAYERS=geodashboard:"+ layerStringList +
              "&TYPENAME=geodashboard:"+ layerStringList;
          
              var that = this;
              
              $.ajax({
                  url: requestURL,
                  context: document.body 
              }).done(function(json) {
                var popupContent = '';
                
                if(json.features != null) {
                  // The getfeatureinfo request will return only 1 feature
                  for(var i = 0; i < json.features.length; i++){
                    var featureLayer = json.features[i];
                    var featureLayerIdReturn = featureLayer.id;
                    var featureLayerId = featureLayerIdReturn.substring(0, featureLayerIdReturn.indexOf('.'));
                    var geoId = featureLayer.properties.geoid;
                      
                    var layer = layerMap[featureLayerId];
                    var layerDisplayName = layer.getLayerName();
                    var aggregationMethod = layer.getAggregationMethod();
                    var attributeName = layer.getAggregationAttribute().toLowerCase();
                      
                    var attributeValue = featureLayer.properties[attributeName];                            
                    var featureDisplayName = featureLayer.properties.displaylabel;

                      
                    if(typeof attributeValue === 'number'){
                      attributeValue = dynamicMap._formatter(attributeValue);
                    }
                    else if(attributeValue != null && !isNaN(Date.parse(attributeValue.substring(0, attributeValue.length - 1)))){
                      var slicedAttr = attributeValue.substring(0, attributeValue.length - 1);
                      var parsedAttr = $.datepicker.parseDate('yy-mm-dd', slicedAttr);
                      attributeValue = dynamicMap._formatDate(parsedAttr);
                    }
                      
                    popupContent += '<div id="popup" class="ol-popup"><a href="#" id="popup-closer" class="ol-popup-closer"></a><h3 class="popup-heading">'+layerDisplayName+'</h3>';
                      
                    var html = '';
                    html += '<table class="table">';
                    html += '<thead class="popup-table-heading">';
                    html += '<tr>'; 
                    html += '<th>'+that.localize("location")+'</th>';  
                    html += '<th>'+that.localize("aggregationMethod")+'</th>'; 
                    html += '<th>'+that.localize("aggregateValue")+'</th>'; 
                    html += '</tr>';  
                    html += '</thead>';
                    html += '<tbody>';  
                    html += '<tr>'; 
                    html += '<td>'+ (featureDisplayName == null ? '' : featureDisplayName) +'</td>';  
                    html += '<td>' + (aggregationMethod == null ? '' : aggregationMethod) + '</td>'; 
                    html += '<td>' + (attributeValue == null ? '' : attributeValue) + '</td>';  
                    html += '</tr>';  
                      
                    if(dynamicMap.canEditData() && geoId != null && (layer.aggregationStrategy.type == 'GeometryAggregationStrategy' || layer.aggregationStrategy.type == 'GEOMETRY') ) {
                      html += '<tr>'; 
                      html += '<td colspan="3"><a class="edit-feature" data-layerid="' + layer.getLayerId() + '" data-geoid="' + geoId + '">' + that.localize("editFeature") + '</a></td>';  
                      html += '</tr>';  
                    }
                      
                    html += '</tbody>';  
                    html += '</table></div>';  
                              
                    popupContent += html;
                      
                    if(geoId != null)
                    {                 
                      dynamicMap.setCurrGeoId(geoId);
                      dynamicMap._renderReport(layer.getLayerId(), geoId, dynamicMap._criteria);
                    }            
                  }                  
                }
                
                if(popupContent.length > 0){
                  that.showClickPopup(popupContent, e.coordinate);
                  
                  // Hook-up the edit click event
                  $(".edit-feature").click(function(e){
                    var layerId = $(this).data("layerid");
                    var geoId = $(this).data("geoid");
                    
                    dynamicMap.editFeature(layerId, geoId);
                  });
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
         * @dynamicMap - the this refernce from calling code which is needed to gain access to DynamicMap scope
         * 
         * <private> - internal method
         */
        renderMap : function(dynamicMap) {
          if(this.getMap() != null){
            this.removeStaleMapFragments();
          }
          
          var attribution = new ol.control.Attribution({
            collapsible: true,
            tipLabel: this.localize("attributionELTooltip")
          });
          
          var map = new ol.Map({
            layers: [ ], // base maps will be added later
            controls: ol.control.defaults({ attribution: false }).extend([attribution]),
            target: this.getMapElementId(),
            loadTilesWhileInteracting: true,
            loadTilesWhileAnimating: true,
            view: new ol.View({ 
              center: this.getCenter(), 
              zoom: this.getZoomLevel()
            })
          });
          
          this.setMap(map);
          this.configureMap();
          
          if(this.getEnableClickEvents()){
            this.setDynamicMap(dynamicMap);
            
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
  
  
