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
     "editFeature" : "Edit feature",
     "bingAerial" : "Bing Satellite",
     "googleSatellite" : "Google Satellite",
     "googleStreets" : "Google Streets",
     "googleTerrain" : "Google Terrain"
  });
    
  var MapWidget = Mojo.Meta.newClass('net.geoprism.gis.MapWidget', {
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
      
      getHoverPointStyle : function() {
        return this.hoverPointStyle;
      },
      
      getHoverPolygonStyle : function() {
        return this.hoverPolygonStyle;
      },
      
      getEditFeatureStyle : function() {
        return this.editFeatureStyle;
      },
      
      addVectorHoverEvents : {
        IsAbstract : true
      },
      
      addVectorClickEvents : {
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
      
      renderMap : {
        IsAbstract : true
      }
    }
  });
  
  var WebGLMap = Mojo.Meta.newClass('net.geoprism.gis.WebGLMap', {
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
          
          this.selectPolygonStyle = {fill:"rgba(0, 48, 143, 1)", opacity:0.5 }
          
          this.hoverPolygonStyle = {fill:"rgba(163, 19, 19, .9)", opacity:0.35, stroke:"rgba(255, 255, 0, 0.75)", strokeWidth:3 }
          this.hoverPointStyle = {fill:"rgba(163, 19, 19, .9)", opacity:0.50, radius:10, stroke:"rgba(255, 255, 0, 0.75)", strokeWidth:3 };
          
          this.editFeatureStyle = {fill:"rgba(255, 0, 0, 1)", stroke:"rgba(255, 0, 0, 1)", strokeWidth:3, radius:10 };
          
          this.LAYERS_LIST = ["target-multipolygon", "context-multipolygon"];
          
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
         * Set basic properties of the map
         * 
         * <private> - internal method
         */
        configureMap : function() {
          var map = this.getMap();
          var that = this;
          
      
          // add scale bar
          map.addControl(new mapboxgl.ScaleControl({position: 'bottom-left', unit: 'imperial'}));
          map.addControl(new mapboxgl.NavigationControl({showCompass:false}), 'top-right');
        },
        
        // TODO: convert to webgl
        getAllVectorLayers : function() {
          var map = this.getMap();
          var layers = map.getLayers().getArray();
          var allVectorLayers = [];
          
          for(var i=0; i<layers.length; i++){
            var layer = layers[i];
            if(layer instanceof ol.layer.Vector){
              allVectorLayers.push(layer);
            }
          }
          
          return allVectorLayers;
        },
        
        _addVectorLayer : function(source, layers) {
          console.log("reached _addVectorLayer");
          var map = this.getMap();
          var that = this;
          
          var protocol= window.location.protocol;
          var host = window.location.host;
          
          map.addSource(source.name, { 
            type: 'vector',
            tiles: [protocol + '//' + host + com.runwaysdk.__applicationContextPath + '/location/data?x={x}&y={y}&z={z}&config=' + encodeURIComponent(JSON.stringify(source.config))]
          });
                
          for(var i = 0; i < layers.length; i++) {
            var layer = layers[i];
            
            var layerName = layer.name;
            var styleObj = layer.style;
            
            // add the main layer
            map.addLayer({
              "id": layerName + "-point",
              "source": source.name,
              "source-layer": layer.layer,                     
              "type": "circle",
              "paint": {
                  "circle-radius": styleObj.radius,
                  "circle-color": styleObj.fill,
                  "circle-stroke-width": styleObj.strokeWidth,
                  "circle-stroke-color": styleObj.strokeColor
              },
              'layout': {
                'visibility' : layer.geomType.indexOf("POINT") !== -1 ? 'visible' : 'none'
              }              
            });
            
            // add labels
            map.addLayer({
              "id": layerName + "-point" + "-label",
              "source": source.name,
              "source-layer": layer.layer,                     
              "type": "symbol",
              "paint": {
                  "text-color": "black",
                  "text-halo-color": "#fff",
                   "text-halo-width": 2
              },
              "layout": {
                      "text-field": "{displayLabel}",
                      "text-font": ["Open Sans Semibold", "Arial Unicode MS Bold"],
                      "text-offset": [0, 0.6],
                      "text-anchor": "top",
                      "text-size": 12,
                      'visibility' : layer.geomType.indexOf("POINT") !== -1 ? 'visible' : 'none'
                  }
            });
            
            // This layer is displayed when they hover over the feature
            map.addLayer({
              "id": layerName + "-point" + "-hover",
              "source": source.name,
              "source-layer": layer.layer,                     
              "type": "circle",
              "paint": {
                  "circle-radius": styleObj.radius,
                  "circle-color": that.getHoverPointStyle().fill,
                  "circle-opacity": that.getHoverPointStyle().opacity
              },
              'layout': {
                  'visibility' : layer.geomType.indexOf("POINT") !== -1 ? 'visible' : 'none'
              },
              "filter": ["==", "name", ""] // hide all features in the layer
             });
            
            
                if (layerName.indexOf("line") !== -1){
                	
                	// add the main layer
                	map.addLayer({
                		"id": layerName,
                		"source": source.name,
                		"source-layer": layer.layer,                     
                		"type": "line",
                		"layout": {
                	      "line-join": "round",
                          "line-cap": "round"
                        },
                        "paint": {
                          "line-color": styleObj.fill,
                          "line-width": styleObj.strokeWidth
                        }
                    });
                	
                	// add labels
                	map.addLayer({
                		"id": layerName + "-label",
                		"source": source.name,
                		"source-layer": layer.layer,                     
                		"type": "symbol",
                		"symbol-placement":"line",
                		"paint": {
                			"text-color": "black",
                			"text-halo-color": "#fff",
                			"text-halo-width": 2
                		},
                		"layout": {
                			"text-field": "{displayLabel}",
                			"text-font": ["Open Sans Semibold", "Arial Unicode MS Bold"],
                			"text-offset": [0, 0.6],
                			"text-anchor": "top",
                			"text-size": 12
                		}
                	});                	
                }
                else if (layerName.indexOf("polygon") !== -1){
                  
                  if(layer.is3d){
                  
                    var polygons3DStyle = {
                     "id": layerName,
                     "source": source.name,
                     "source-layer": layer.layer,                     
                     "type": "fill-extrusion",
                     "paint": {
//                       'fill-extrusion-color': styleObj.fill,
                       'fill-extrusion-color': {
                         "property": "featureType",
                         "type": "categorical",
                         "stops": [
                             ["boundary", "#7c7871"],
                             ["building", "#3b475b"],
                         ]
                       },
                       'fill-extrusion-height': {
                        'type': 'identity',
                        'property': 'height'
                       },
                       'fill-extrusion-base': {
                          'type': 'identity',
                          'property': 'base'
                       },
//                       'fill-extrusion-height': layer.height,
//                       'fill-extrusion-base': layer.base,
                       'fill-extrusion-opacity': .9
                     },
                    'layout': {
                        'visibility' : layer.geomType.indexOf("POLYGON") !== -1 ? 'visible' : 'none'
                     }                                  
                 }
                    
                    
                    map.addLayer(polygons3DStyle);
                    
                    // This layer is displayed when they hover over the feature
                   map.addLayer({
                     "id": layerName + "-hover",
                     "source": source.name,
                     "source-layer": layer.layer,                     
                     "type": "fill-extrusion",
                     "paint": {
                       "fill-extrusion-color": that.getHoverPolygonStyle().fill,
                       'fill-extrusion-height': {
                           'type': 'identity',
                           'property': 'height'
                          },
                          'fill-extrusion-base': {
                             'type': 'identity',
                             'property': 'base'
                          },
                     },
                    'fill-extrusion-opacity': .5,
                    'layout': {
                        'visibility' : layer.geomType.indexOf("POLYGON") !== -1 ? 'visible' : 'none'
                     },                                  
                     "filter": ["==", "name", ""] // hide all features in the layer
                     });
                  }
                  else{
                    var polygonSimpleStyle = {
                      "id": layerName,
                      "source": source.name,
                      "source-layer": layer.layer,                     
                      "type": "fill",
                      "paint": {
                           "fill-color": styleObj.fill,
                           "fill-outline-color": "black"
                           //"fill-stroke-width": 5,
                           //"fill-stroke-color": "#000000"
                       },
                      'layout': {
                         'visibility' : layer.geomType.indexOf("POLYGON") !== -1 ? 'visible' : 'none'
                       }                                  
                    
                   }
                    
                    map.addLayer(polygonSimpleStyle);
                    
                  // This layer is displayed when they hover over the feature
                   map.addLayer({
                     "id": layerName + "-hover",
                     "source": source.name,
                     "source-layer": layer.layer,                     
                     "type": "fill",
                     "paint": {
                       "fill-color": that.getHoverPolygonStyle().fill,
                       "fill-opacity": that.getHoverPolygonStyle().opacity
                     },
                     'layout': {
                         'visibility' : layer.geomType.indexOf("POLYGON") !== -1 ? 'visible' : 'none'
                      },                     
                     "filter": ["==", "name", ""] // hide all features in the layer
                     });
                  }
                  
                  
                      
                    // add labels
                    map.addLayer({
                      "id": layerName + "-label",
                      "source": source.name,
                      "source-layer": layer.layer,                     
                      "type": "symbol",
                      "paint": {
                           "text-color": "black",
                           "text-halo-color": "#fff",
                           "text-halo-width": 2
                       },
                       "layout": {
                               "text-field": "{displayLabel}",
                               "text-font": ["Open Sans Semibold", "Arial Unicode MS Bold"],
                               "text-anchor": "center",
                               "text-size": 12,
                               "symbol-spacing": 10000,
                               'visibility' : layer.geomType.indexOf("POLYGON") !== -1 ? 'visible' : 'none'                               
                           }
                   });
                }
          }
        },
          
        addVectorLayer : function(source, layers) {
          var map = this.getMap();
          var that = this;
          
          if(map.loaded()) {
            this._addVectorLayer(source, layers);            
          }
          else {
//            map.on('load', function () {
//              that._addVectorLayer(source, layers);              
//            });            
        	
        	// map.loaded() and also the event callback don't work properly
        	// https://github.com/mapbox/mapbox-gl-directions/issues/111
        	var that = this;
    	    function waitMapLoaded() {
			  if(!that.getMap()._loaded) {
				console.log(that.getMap());
			    window.setTimeout(waitMapLoaded, 100);
			  } else {
		        that._addVectorLayer(source, layers);
			  }
    		}
    	    waitMapLoaded();
          }
        },        
        
        updateVectorLayer : function(source, layers) {
          var map = this.getMap();
          var that = this;

          var mSource = map.getSource(source.name);
          
          if(mSource) {
            var protocol= window.location.protocol;
            var host = window.location.host;
            
            map.removeSource(source.name);
            map.addSource(source.name, { 
              type: 'vector', 
              tiles: [protocol + '//' + host + com.runwaysdk.__applicationContextPath + '/location/data?x={x}&y={y}&z={z}&config=' + encodeURIComponent(JSON.stringify(source.config))]
            });
            
            for(var i = 0; i < layers.length; i++) {
            	var layer = layers[i];
            	
            	if(layer.geomType.indexOf("POINT") !== -1) {
            		map.setLayoutProperty(layer.name + "-point", 'visibility', 'visible');            	            		
            		map.setLayoutProperty(layer.name + "-point-label", 'visibility', 'visible');            	            		
            		map.setLayoutProperty(layer.name + "-point-hover", 'visibility', 'visible');            	            		
            		map.setLayoutProperty(layer.name, 'visibility', 'none');            	            		
            		map.setLayoutProperty(layer.name + "-label", 'visibility', 'none');            	            		
            		map.setLayoutProperty(layer.name + "-hover", 'visibility', 'none');            	            		
            	}
            	else {
            		map.setLayoutProperty(layer.name + "-point", 'visibility', 'none');            	            		
            		map.setLayoutProperty(layer.name + "-point-label", 'visibility', 'none');            	            		
            		map.setLayoutProperty(layer.name + "-point-hover", 'visibility', 'none');            	            		
            		map.setLayoutProperty(layer.name, 'visibility', 'visible');            	            		
            		map.setLayoutProperty(layer.name + "-label", 'visibility', 'visible');            	            		
            		map.setLayoutProperty(layer.name + "-hover", 'visibility', 'visible');            	            		
            	}
            }
          }
          else {
            this.addVectorLayer(source, layers);
          }
        },
        
        
        arrayContainsString : function(array, str) {
          for(var i=0; i<array.length; i++){
            if(array[i] === str){
              return true;
            }
          }
          
          return false;
        },
        
        /**
         * For hover events
         */
        focusOffFeature : function(feature) {
          var map = this.getMap();
          if( ! map.isEasing()){
            var that = this;
            var selectedFeatures = [];
            var targetLayers = this.getVectorLayersByTypeProp("TARGET");
            
            
            if(targetLayers.length > 0){
              targetLayers.forEach(function(targetLayer)
              {
                var features = map.querySourceFeatures(targetLayer.source, {
                  sourceLayer: targetLayer.sourceLayer
                });
                
                if (features !=null & features.length > 0)
                {
                  for (var i=0; i < features.length; ++i)
                  {
                    var loopFeat = features[i];
                    var props = loopFeat.properties;
                    
                    if ( (feature.id && feature.id === props.id) ||
                          (feature.geoId && feature.geoId.length > 0 && feature.geoId === props.geoId) ||
                          (feature.geoIds && feature.geoIds.length > 0 && that.arrayContainsString(feature.geoIds, props.geoId)))
                    {
                      // control for styling of different geometry types
                      if(loopFeat.geometry)
                      {
                        if(loopFeat.geometry.type.toLowerCase() === "multipolygon" || loopFeat.geometry.type.toLowerCase() === "polygon")
                        {
                          map.setFilter(targetLayer.sourceLayer.toLowerCase() + "-multipolygon-hover", ["==", "id", ""]);
                        }
                        else if(loopFeat.geometry.type.toLowerCase() === "point")
                        {
//                        map.setFilter("target-point-hover", ["==", "id", ""]);
                        }
                      }
                      selectedFeatures.push(loopFeat);
                    }
                  }
                }
                
              });
            }
          }
        },
        
        /**
         * For hover events
         */
        focusOnFeature : function(feature) {
          var map = this.getMap();
          if( ! map.isEasing()){ 
            var that = this;
            var selectedFeatures = [];
            var targetLayers = this.getVectorLayersByTypeProp("TARGET");
            if(targetLayers.length > 0){
              targetLayers.forEach(function(targetLayer){
                
                var features = map.querySourceFeatures(targetLayer.source, {
                  sourceLayer: targetLayer.sourceLayer
                });
                
                if (features !=null & features.length > 0)
                {
                  for (var i=0; i < features.length; ++i)
                  {
                    var loopFeat = features[i];
                    var props = loopFeat.properties;
                    
                    if (  (feature.geoId && feature.geoId.length > 0 && feature.geoId === props.geoId) ||
                          (feature.geoIds && feature.geoIds.length > 0 && that.arrayContainsString(feature.geoIds, props.geoId)))
                    {
                      // control for styling of different geometry types
                      if(loopFeat.geometry)
                      {
                        if(loopFeat.geometry.type.toLowerCase() === "multipolygon" || loopFeat.geometry.type.toLowerCase() === "polygon")
                        {
                          map.setFilter(targetLayer.sourceLayer.toLowerCase() + "-multipolygon-hover", ["==", "id", loopFeat.properties.oid]);
                        }
                        else if(loopFeat.geometry.type.toLowerCase() === "point")
                        {
//                        map.setFilter("target-point-hover", ["==", "id", loopFeat.properties.oid]);
                        }
                      }
                      selectedFeatures.push(loopFeat);
                    }
                  }
                }            
              });
            }
          }
        },
        
        selectFeature : function(feature) {
        	//TODO: fix this or remove it
//          var map = this.getMap();
//          
//          if(feature.layer.type === "fill"){
//            map.setFilter(feature.layer.source + "-select", ["==", "id", feature.properties.id]);
//          }
        },
        
        unselectFeature : function(feature) {
        	//TODO: fix this or remove it
//          var map = this.getMap();
//          
//          if (feature == null)
//          {
//            map.setFilter("target-multipolygon-select", ["==", "id", ""]);
//          }
//          else if(feature.layer.type === "fill"){
//            map.setFilter(feature.layer.source + "-select", ["==", "id", ""]);
//          }
        },
        
        getVectorLayersByTypeProp : function(type) {
          var map = this.getMap();
          var layersArr = [];
          var layersList = this.LAYERS_LIST;
          
          if(layersList.length > 0){
            layersList.forEach(function(layerName){
              var layer = map.getLayer(layerName);
              
//              if(layer && layer.sourceLayer.toUpperCase() === type.toUpperCase()){
              if(layer){
                layersArr.push(layer);
              }
            });
          }
          
          return layersArr;
        },
        
        removeAllVectorLayers : function() {
          var map = this.getMap();
          var vecLayers = this.getAllVectorLayers();
          
          for(var i=0; i<vecLayers.length; i++){
            map.removeLayer(vecLayers[i]);
          }
        },
        
        getAllVectorLayers : function(layers) {
          var map = this.getMap();
          var layersArr = [];
          var layersList = this.LAYERS_LIST;
          if (layers != null)
          {
            layersList = layers;
          }
          
          if(layersList.length > 0){
            layersList.forEach(function(layerName){
              var layer = map.getLayer(layerName);
              
              if(layer){
                layersArr.push(layer);
              }
            });
          }
          
          return layersArr;
        },
        
        
        /**
         * <private> - internal method
         */
        removeStaleMapFragments : function() {
          var map = this.getMap();
          map.remove();
            
          $('#'+this.getMapElementId()).html('');
        },  
        
        
        zoomToLayersExtent : function(layersArr) {
          var map = this.getMap();
          var bounds = new mapboxgl.LngLatBounds();
          
          if(layersArr.length > 0){
            layersArr.forEach(function(refLayer){
            	if(refLayer.bbox && refLayer.bbox.sw && refLayer.bbox.ne){
            		bounds.extend(
            			new mapboxgl.LngLatBounds(refLayer.bbox.sw, refLayer.bbox.ne)
            		);
            	}
            });
            
            // check if bounds is an empty json object
            if(Object.keys(bounds).length > 0){
              if(bounds.getSouth() === bounds.getNorth() && bounds.getWest() === bounds.getEast()){
                
                var pt = {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                      "type": "Point",
                      "coordinates": [bounds.getEast(), bounds.getNorth()]
                    }
                  };
              
                var buffered = turf.buffer(pt, 1, "miles");
                var bufferedBounds = turf.extent(buffered);
                map.fitBounds(bufferedBounds, {padding:0});
              }
              else{
            	// TODO: map.loaded() ALWAYS returns false.  I'm leaving this check here for future
            	// versions that hopefully work. 
            	// the || property is to make up for the map.loaded() failing flag. 
            	if(map.loaded() || map.getLayer(layersArr[0].name)){
            		map.fitBounds(bounds, {padding:10});
            	}
            	else{
            		map.on('load', function () {
            			map.fitBounds(bounds, {padding:10});
            		})
            	}
              }
            }
          }
        },
        
        
        zoomToExtentOfFeatures : function(entities, layers) {
          var map = this.getMap();
          var that = this;
          var layersArr = this.getAllVectorLayers(layers);
          var fullExt = null;
          
          var bounds = new mapboxgl.LngLatBounds();
          var featureGeoIds = [];
          var featureIds = [];
          entities.forEach(function(ent){
            if (typeof ent === 'string' || ent instanceof String)
            {
              featureIds.push(ent);
            }
            else
            {
              featureGeoIds.push(ent.geoId);
            }
          })
          
          if(layersArr.length > 0){
            layersArr.forEach(function(layer){
              
              if(layer){
                  var layerSourceName = layer.source;
                  
                  var layerSourceData = map.querySourceFeatures(layerSourceName, {
                      sourceLayer: layer.sourceLayer
                  });
                  
                  if(layerSourceData && layerSourceData.length > 0){
                    layerSourceData.forEach(function(f){
                      if(that.arrayContainsString(featureGeoIds, f.properties.geoId) || that.arrayContainsString(featureIds, f.properties.featureId)){
                        var bbox = turf.extent(f);
                          bounds.extend([bbox[0], bbox[1]], [bbox[2], bbox[3]]);
                      }
                    });
                  }
              }
            });
            
            // check if bounds is an empty json object
            if(Object.keys(bounds).length > 0){
              if(bounds.getSouth() === bounds.getNorth() && bounds.getWest() === bounds.getEast()){
                
                var pt = {
                      "type": "Feature",
                      "properties": {},
                      "geometry": {
                        "type": "Point",
                        "coordinates": [bounds.getEast(), bounds.getNorth()]
                      }
                    };
                
                var buffered = turf.buffer(pt, .5, "miles");
                var bufferedBounds = turf.extent(buffered);
                map.fitBounds(bufferedBounds, {padding:0});
              }
              else{
                map.fitBounds(bounds, {padding:50});
              }
            }
          }
        },
        
        
        zoomToExtent : function(bounds) {
        	var map = this.getMap();
        	
        	if(typeof bounds === "string" && net.geoprism.gis.WebGLMap.isJSON(bounds) ){
        		bounds = JSON.parse(bounds);
        	}
        	
        	// convert to 3857
//        	if(bounds.srid === "4326"){
//        		var swProj = proj4("EPSG:4326", "EPSG:3857", [bounds.sw.lng, bounds.sw.lat]);
//        		var neProj = proj4("EPSG:4326", "EPSG:3857", [bounds.ne.lng, bounds.ne.lat]);
//        		var sw = new mapboxgl.LngLat(swProj[0], swProj[1]);
//        		var ne = new mapboxgl.LngLat(neProj[0], neProj[1]);
//        	}
//        	else{
//        		var sw = new mapboxgl.LngLat(bounds.sw.lng, bounds.sw.lat);
//        		var ne = new mapboxgl.LngLat(bounds.ne.lng, bounds.ne.lat);
//        	}
        	
        	if(bounds.sw && bounds.ne){
	        	var sw = new mapboxgl.LngLat(Number(bounds.sw.lng), Number(bounds.sw.lat));
	    		var ne = new mapboxgl.LngLat(Number(bounds.ne.lng), Number(bounds.ne.lat));
	    		
	        	var bbox = new mapboxgl.LngLatBounds(sw, ne);
	        	
	        	if(map.loaded()){
	        		map.fitBounds(bbox, {padding:50, linear:true});
	        	}
	        	else{
	        		map.on('load', function () {
	        			map.fitBounds(bbox, {padding:50, linear:true});
	        		});
	        	}
        	}
        },
        
        
        addVectorClickEvents : function(featureClickCallback, layerz)
        {
          this._clicks = this._clicks || {};
          if (this._clicks.hasOwnProperty(layerz)) { return; }
          this._clicks[layerz] = true;
          
          var map = this.getMap();
          var that = this;
          
          map.on('click', function(e) {
          
            var features = map.queryRenderedFeatures(e.point, { layers: layerz });
            
            if(features.length && features.length > 0){
              var feature = features[0]; // 1st item = top most feature according to mapbox-gl api
                
              if(feature.properties.isClickable || feature.properties.isClickable === "true"){
                var shouldDoDefault = featureClickCallback(feature, map);
                if (shouldDoDefault === false) { return; }
              }
            }
          }); 
        },
        
        
        addVectorHoverEvents : function(hoverCallback, layerz)
        {
          this._hovers = this._hovers || {};
          if (this._hovers.hasOwnProperty(layerz)) { return; }
          this._hovers[layerz] = true;
          
          var map = this.getMap();
          var that = this;
          var selectedFeatures = [];
          var popup;
          var originalCursor = map.getCanvas().style.cursor;
          
          //TODO: Do we need to wrap this in 'load' event or should we use a different event
//          map.on('load', function () {
            map.on('mousemove', that.throttle(function(e) {
              if(typeof map.getLayer('target-multipolygon-hover') === 'undefined') {
                return; // This function throws errors if it runs too early.
              }
              
              var features = map.queryRenderedFeatures(e.point, { layers: layerz  });
                
              if(features && features.length){
                var feature = features[0]; // only take the 1st feature
                
                var shouldDoDefault = hoverCallback(feature.id);
                if (shouldDoDefault === false) { return; }
                
                if(popup){
                  popup.remove();
                }
                
                // TODO: This popup code is slow
                popup = new mapboxgl.Popup({closeOnClick: true})
                    .setLngLat([e.lngLat.lng, e.lngLat.lat])
                    .setHTML(feature.properties.displayLabel)
                    .addTo(map);
                  
                  
                if(feature.properties.isClickable || feature.properties.isClickable === "true"){
                  map.getCanvas().style.cursor = 'pointer';
                }
                else{
                  map.getCanvas().style.cursor = originalCursor;
                }
                
                if(selectedFeatures.length > 0){
//                  map.setFilter("target-point-hover", ["==", "id", ""]);
                  map.setFilter("target-multipolygon-hover", ["==", "id", ""]);
                  selectedFeatures = [];
                }
                
                
                // control for styling of different geometry types
                // 'fill' === polygon
                if(feature.layer.type === "fill"){
                  map.setFilter("target-multipolygon-hover", ["==", "id", feature.properties.oid]);
                }
                else if(feature.layer.type === "fill-extrusion"){
                  // currently disabled because extruded features over flat features gets covered by highlight
                  //map.setFilter("target-multipolygon-hover", ["==", "id", feature.properties.id]);
                }
                else if(feature.layer.type === "circle"){
//                  map.setFilter("target-point-hover", ["==", "id", feature.properties.id]);
                }
                
                  selectedFeatures.push(feature);
                  
                  hoverCallback(feature.properties.oid);
              }
              else{
                map.getCanvas().style.cursor = originalCursor;
                
//                map.setFilter("target-point-hover", ["==", "id", ""]);
                    map.setFilter("target-multipolygon-hover", ["==", "id", ""]);
                
                if(selectedFeatures.length > 0){
                  hoverCallback(null);
                  selectedFeatures = [];
                }
                
                if(popup){
                  popup.remove();
                }
              }
              
          }, 70, that));
            
//              map.on("mouseout", function() {
//                  map.setFilter("target-point-hover", ["==", "id", ""]);
//              });
            
//          });
        },
        
        
        /**
         * Taken from: https://github.com/mapbox/mapbox-gl-draw/blob/v0.4.0/src/util.js
         *   per the advice of: https://github.com/mapbox/mapbox-gl-js/issues/2225
         * 
         * Create a version of `fn` that only fires once every `time` millseconds.
         *
         * @param {Function} fn the function to be throttled
         * @param {number} time millseconds required between function calls
         * @param {*} context the value of `this` with which the function is called
         * @returns {Function} debounced function
         * @private
         */

        throttle : function(fn, time, context) {
          var lock, args, wrapperFn, later;

          later = function () {
            // reset lock and call if queued
            lock = false;
            if (args) {
              wrapperFn.apply(context, args);
              args = false;
            }
          };

          wrapperFn = function () {
            if (lock) {
              // called too soon, queue to call later
              args = arguments;

            } else {
              // call and lock until later
              fn.apply(context, arguments);
              setTimeout(later, time);
              lock = true;
            }
          };

          return wrapperFn;
        },
        
        
        /**
         * Instantiate a new map
         * 
         * <private> - internal method
         */
        renderMap : function() {
          if(this.getMap() != null){
            this.removeStaleMapFragments();
          }
          
          // TODO: replace accessToken with TerraFrame or customer token
          mapboxgl.accessToken = 'pk.eyJ1IjoianVzdGlubGV3aXMiLCJhIjoiY2l0YnlpdWRkMDlkNjJ5bzZuMTR3MHZ3YyJ9.Ad0fQd8onRSYR9QZP6VyUw';
          var map = new mapboxgl.Map({
              container: 'mapDivId',
//              minZoom: 2,
//              maxBounds: [[-180,-90],[180,90]],
              style: 'mapbox://styles/mapbox/satellite-v8'
          });
          
//          map.on("zoomend", function(e){
//        	  console.log(map.getZoom())
//        	  console.log(JSON.stringify(map.getBounds()))
//          });
          
        	
          this.setMap(map);
          this.configureMap();     
          
        }
      },
      Static : {
        // Static methods
    	  
    	  isJSON(data) {
    		  var ret = true;
    		  try {
    			  JSON.parse(data);
    		  } 
    		  catch(e) {
    			  ret = false;
    		  }
    		  
    		  return ret;
    	  }
      }
    });
  
})();
  
  
