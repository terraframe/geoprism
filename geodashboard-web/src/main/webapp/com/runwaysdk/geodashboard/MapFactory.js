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
      
      renderMap : {
        IsAbstract : true
      },
      
      setClickHandler : {
        IsAbstract : true
      },
      
      getFeatureInfo : {
        IsAbstract : true
      }
    }
  });
  
  
  var OpenLayersMap = Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.OpenLayersMap', {
      Extends : MapWidget,  
      IsAbstract : false,
      Constants : {
        
      },   
      Instance : {
        initialize : function(elementId, center, zoomLevel){
          this._center = center || [0,0];
          this._zoomLevel = zoomLevel || 2;
          this._elementId = elementId;    
          this._map = null;
          
          this._bounds = null;
          this._config = {zoomAnimation: true, zoomControl: true, attributionControl: true};
          this._cache = {};
          
          this.renderMap();
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
          var oLayer = this._cache[layer.key];
          
          if(oLayer != null && oLayer.showing == false) {
            var map = this.getMap();
            if(stackIndex !== null && stackIndex >= 0){
              map.getLayers().insertAt(stackIndex, oLayer);
            }
            else{
              // will add the layer to the top of all other layers
              map.addLayer(oLayer);
            }
            
            oLayer.showing = true;
          }
        },
        
        /**
         * Hide the layer on the map
         * 
         * @layer - layer object
         * 
         * <public> - called externally
         */
        hideLayer : function(layer, persist) {
          var oLayer = this._cache[layer.key];
          
          if (oLayer != null && oLayer.showing == true) {
            var map = this.getMap();
            map.removeLayer(oLayer);
            
            if(persist) {
              delete this._cache[layer.layerId];            
            }
            else {
              oLayer.showing = false;  
            }
          }
        },
        
        hideLayers : function(layers) {
          if(layers != null) {
            for(var i = 0; i < layers.length; i++) {
              var layer = layers[i];
                  
              this.hideLayer(layer, true);
            }          
          }
        },
        
        /**
         * Create and return an array of all base layer objects.
         * 
         * <public> - called externally
         */
        createBaseLayers : function(){
        
          var layers = [];
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
              baseObj.showing = false;

              // Add the baseObj to the layer cache
              this._cache[i] = baseObj;
                
              var layer = {
            layerId : i,
            key : i,
                isActive : false,
            layerType : base.LAYER_SOURCE_TYPE,
            layerLabel : this.localize(base.LOCLIZATION_KEY)
              };
              
              layers.push(layer);
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
              baseObj.showing = false;

              // Add the baseObj to the layer cache
              this._cache[i] = baseObj;              
              
              var layer = {
                layerId : i,
                key : i,
                isActive : false,
                layerType : base.LAYER_SOURCE_TYPE,
                layerLabel : this.localize(base.LOCLIZATION_KEY)
              };
                    
              layers.push(layer);
            }
          }
          
          return layers;
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
          // Remove any already rendered layers from the map
          if (removeExisting === true) {
            this.hideLayers(layers);            
          }
            
          for (var i = (layers.length-1); i >= 0; i--) {
            var layer = layers[i];
                
             var viewName = layer.viewName;
             var geoserverName = geoserverWorkspace + ":" + viewName;
              
             if (layer.isActive === true) {
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
        createReferenceLayers : function(layers, geoserverWorkspace, removeExisting) {
          
          // Remove any already rendered layers from the map
          if (removeExisting === true) {
            this.hideLayers(layers);
          }
            
          //
          // Add reference layers to the map
          //            
          for (var i = 0; i < layers.length; i++) {
            var layer = layers[i];
              
            // Since REFERENCEJSON layers are basic placeholders for actual mappable layers we will make sure none of them
            // get through here.            
            if(layer.layerType !== "REFERENCEJSON" && layer.isActive === true && layer.layerExists) {
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
          // This tiling format (tileLayer) is the preferred way to render wms due to performance gains but 
          // REQUIRES THAT META TILING SIZE BE SET TO A LARGE VALUE (I.E. 20) TO REDUCE BUBBLE CHOPPING.
          // We could get slightly better performance by setting tiled: false for non-bubble layers but 
          // this is currently unnecessary addition of code for relatively small performance gain.        
          var viewName = layer.viewName;
          var geoserverName = geoserverWorkspace + ":" + viewName;
                    
          // Single Tile format
          var oLayer = new ol.layer.Image({
            source: new ol.source.ImageWMS({
              url: window.location.origin+"/geoserver/wms/",
              params: {
                'LAYERS': geoserverName, 
                'TILED': true,
                'STYLES': layer.sldName || "",
                'FORMAT': 'image/png'
              },
              serverType: 'geoserver'
            }),
            visible: true
          });
          oLayer.showing = false;


          this._cache[layer.key] = oLayer;
          
          this.showLayer(layer, null);
              
              
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
         * Instantiate a new map
         * 
         * @controller - the this refernce from calling code which is needed to gain access to Controller scope
         * 
         * <private> - internal method
         */
        renderMap : function() {
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
        },
        
        setClickHandler : function(handler) {
          var map = this.getMap();
          
          map.on('click', handler);
        },
        
        getFeatureInfo : function(workspace, layers, point, callback) {
          if(layers.length > 0) {
            var x = parseInt(point[0]);
            var y = parseInt(point[1]);
            
            // Construct a GetFeatureInfo request URL given a point
            var size = this.getMapSize();
            var mapBbox = this.getCurrentBoundsAsString(MapWidget.DATASRID);
            var layerMap = {};

            var layerStringList = '';
                
             // Build a string of layers to query against but geoserver will only return the 
             // first entry in the array if anything is found. Otherwise it will query the next layer
             // until something is found.
            var firstAdded = false;
                
            for (var i = 0; i < layers.length; i++) { 
              var layer = layers[i];
              layerMap[layer.viewName] = layer;                           
                  
              // If the layer object is active (visible on the map)
              if(layer.isActive){
                var layerId = layer.viewName;
                        
                if(firstAdded){
                  layerStringList += "," + layerId;
                }
                else{
                  layerStringList += layerId;
                  firstAdded = true;
                }
              }
            }
            
            var params = {
              REQUEST:'GetFeatureInfo',
              INFO_FORMAT:'application/json',
              EXCEPTIONS:'APPLICATION/VND.OGC.SE_XML',
              SERVICE:'WMS',
              SRS:MapWidget.DATASRID,              
              VERSION:'1.1.1',
              height:size.y,
              width:size.x,
              X:x,
              Y:y,
              BBOX:mapBbox,
              LAYERS:"geodashboard:"+ layerStringList,
              QUERY_LAYERS:"geodashboard:"+ layerStringList,
              TYPENAME:"geodashboard:"+ layerStringList
            };
                
            var url = window.location.origin+"/geoserver/" + workspace +"/wms?" + $.param(params);
                  
            $.ajax({
              url: url,
              context: document.body 
            }).done(function(response) {
            
              if(response.features != null && response.features.length > 0) {
                  
                /* The response will return only 1 feature */
                var feature = response.features[0];
                var viewName = feature.id.substring(0, feature.id.indexOf('.'));
                
                var layer = layerMap[viewName];
                var layerDisplayName = layer.layerName;
                var aggregationMethod = layer.aggregationMethod;
                var attributeName = layer.aggregationAttribute.toLowerCase();
                        
                var featureDisplayName = feature.properties.displaylabel;
                var geoId = feature.properties.geoid;
                var attributeValue = feature.properties[attributeName];
              
                var info = {
                  layerDisplayName : layerDisplayName,
                  aggregationMethod : aggregationMethod,
                  featureDisplayName : featureDisplayName,
                  attributeValue : attributeValue,
                  geoId : geoId,
                  x : x,
                  y : y,
                  layerId : layer.layerId,
                  aggregationStrategy : layer.aggregationStrategy
                };  
                  
                callback(info);
              }
            });
          }        
        }
      },
      Static : {
        // Static methods
      }
    });
  
})();
  
  
