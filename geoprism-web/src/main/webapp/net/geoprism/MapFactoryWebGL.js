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
          
          this.hoverPolygonStyle = {fill:"white", opacity:0.35, stroke:"rgba(255, 255, 0, 0.75)", strokeWidth:3 }
      	  this.hoverPointStyle = {fill:"white", opacity:0.35, radius:10, stroke:"rgba(255, 255, 0, 0.75)", strokeWidth:3 };
          
      	  this.editFeatureStyle = {fill:"rgba(255, 0, 0, 1)", stroke:"rgba(255, 0, 0, 1)", strokeWidth:3, radius:10 };
      	  
      	  this.LAYERS_LIST = ["target-point", "context-point", "target-multipolygon", "context-multipolygon"];
          
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
          
			    map.on("data.updated", function(data){
        		// TODO: remove hard coded layer names
        		if( ! map.isEasing()){
        			console.log("zoom zoom zoom!")
        			that.zoomToLayersExtent(that.LAYERS_LIST);
        		}
        		
//          		console.log(e, " - data loaded")
          });
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
        
        
        addVectorLayer : function(layerAsGeoJSON, layerName, styleObj, type, stackingIndex) {
        	var map = this.getMap();
        	var that = this;
        	
        	
            map.on('load', function () {
            	
            	// add the source which stores the data
          	    map.addSource(layerName, { 
          	    	type: 'geojson', 
          	    	data: layerAsGeoJSON
          	    });
          	  
          	    if (layerName.indexOf("point") != -1){
	          	    // add the main layer
    		     	    map.addLayer({
      			 	    	"id": layerName,
    			 	        "source": layerName,
    			 	        "type": "circle",
    			 	        "paint": {
    			 	            "circle-radius": styleObj.radius,
    			 	            "circle-color": styleObj.fill
    			 	        }
    		     	    });
    		     	    
    		     	    // add labels
    		     	    map.addLayer({
      			 	    	"id": layerName + "-label",
    			 	        "source": layerName,
    			 	        "type": "symbol",
    			 	        "paint": {
    			 	            "text-color": "black",
    			 	            "text-halo-color": "#fff",
    		                    "text-halo-width": 2
    			 	        },
    			 	        "layout": {
    	         	            "text-field": "{displaylabel}",
    	         	            "text-font": ["Open Sans Semibold", "Arial Unicode MS Bold"],
    	         	            "text-offset": [0, 0.6],
    	         	            "text-anchor": "top",
    	         	            "text-size": 12
    	         	        }
      			 	    });
    		     	    
    		     	    // This layer is displayed when they hover over the feature
    		     	    map.addLayer({
    		     	        "id": layerName + "-hover",
    		     	        "source": layerName,
    			 	        "type": "circle",
    			 	        "paint": {
    			 	            "circle-radius": styleObj.radius,
    			 	            "circle-color": that.getHoverPointStyle().fill,
    			 	            "circle-opacity": that.getHoverPointStyle().opacity
    			 	        },
    		     	        "filter": ["==", "name", ""] // hide all features in the layer
    		     	     });
          	    }
          	    else if (layerName.indexOf("multipolygon") != -1){
          	    	map.addLayer({
      			 	    	"id": layerName,
      			 	        "source": layerName,
      			 	        "type": "fill",
      			 	        "paint": {
      			 	            "fill-color": styleObj.fill,
      			 	            "fill-outline-color": "black",
      			 	            //"fill-stroke-width": 5,
      			 	            //"fill-stroke-color": "#000000"
      			 	        }
      			 	    });
              	    	
          	      // add labels
                  map.addLayer({
      			 	    	"id": layerName + "-label",
      			 	        "source": layerName,
      			 	        "type": "symbol",
      			 	        "paint": {
      			 	            "text-color": "black",
      			 	            "text-halo-color": "#fff",
      		                    "text-halo-width": 2
      			 	        },
      			 	        "layout": {
      	         	            "text-field": "{displaylabel}",
      	         	            "text-font": ["Open Sans Semibold", "Arial Unicode MS Bold"],
      	         	            "text-anchor": "center",
      	         	            "text-size": 12
      	         	        }
      			 	    });
      		     	  
    		     	    // This layer is displayed when they hover over the feature
    		     	    map.addLayer({
  		     	        "id": layerName + "-hover",
  		     	        "source": layerName,
    			 	        "type": "fill",
    			 	        "paint": {
    			 	            "fill-color": that.getHoverPolygonStyle().fill,
    			 	            "fill-opacity": that.getHoverPolygonStyle().opacity
    			 	        },
  		     	        "filter": ["==", "name", ""] // hide all features in the layer
  		     	      });
    		     	   
    		     	   // This layer is displayed when they click on the feature
    		     	   map.addLayer({
                   "id": layerName + "-select",
                   "source": layerName,
                   "type": "fill",
                   "paint": {
                       "fill-color": that.selectPolygonStyle.fill,
                       "fill-opacity": that.selectPolygonStyle.opacity
                   },
                   "filter": ["==", "name", ""] // hide all features in the layer
                 });
          	    }
          	    
          	    if (that._updateVectorLayersAfterLoading != null)
          	    {
          	      for (var i = 0; i < that._updateVectorLayersAfterLoading.length; ++i)
          	      {
          	        that._updateVectorLayersAfterLoading[i]();
          	      }
          	    }
          	    that._areLayersLoaded = true;
            });
            
            
            // Won't do anything unless layers are populate with data containing features
            map.on("source.load", function(e){
            	if(e.source.id === layerName){
            		that.zoomToLayersExtent([layerName])
            	}
            });
   	    
        },
        
        
        updateVectorLayer : function(layerAsGeoJSON, layerName, styleObj, type, stackingIndex, skipMapLoadedCheck) {
        	var map = this.getMap();
        	var that = this;
        	
            if (that._areLayersLoaded == null && skipMapLoadedCheck == null && !map.loaded())
      	    {
        	  if (this._updateVectorLayersAfterLoading == null)
      	      {
        	    this._updateVectorLayersAfterLoading = [];
      	      }
        	  
        	  this._updateVectorLayersAfterLoading.push(function(){
                that.updateVectorLayer(layerAsGeoJSON, layerName, styleObj, type, stackingIndex, true);
              });
        	  
        	  return;
      	    }
        	
        	var layer = map.getLayer(layerName);
			
    		if (layer) {
          	  var layerSourceName = layer.source;
          	  map.getSource(layerSourceName).setData(layerAsGeoJSON);
    		}
			
    		map.once('data', function () {
    		  map.fire("data.updated", true);
    		});
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
	        		targetLayers.forEach(function(targetLayer){
	        			
	        			var layerSourceName = targetLayer.source;
	        			
	    	        	// TODO: replace _data with map.querySourceFeatures(layerSourceName);
//	    	        	var layerSourceData = map.querySourceFeatures(layerSourceName);
	    	        	var layerSourceData = map.getSource(layerSourceName)._data;
	    	        	
	    	        	if(layerSourceData.features.length > 0){
	    	         		for(var i=0; i<layerSourceData.features.length; i++){
	    	        			var targetFeature = layerSourceData.features[i];
	    	        			var featureProps = targetFeature.properties;
	    	        			if((feature.id && feature.id === featureProps.id) || 
	    	        			  (feature.geoId && feature.geoId.length > 0 && that.arrayContainsString(feature.geoId, featureProps.geoid))){
		        				
	    	        				// control for styling of different geometry types
	    	        				if(targetFeature.geometry){
	    	        				  if(targetFeature.geometry.type.toLowerCase() === "multipolygon"){
	    			        	    	map.setFilter("target-multipolygon-hover", ["==", "id", ""]);
	    		        	    	  }
	    	        				  else if(targetFeature.geometry.type.toLowerCase() === "point"){
	    			        	    	map.setFilter("target-point-hover", ["==", "id", ""]);
	    		        	    	  }
	    	        				}
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
	        			
	        			var layerSourceName = targetLayer.source;
	        			
	    	        	// TODO: replace _data with map.querySourceFeatures(layerSourceName);
//	        			var layerSourceData = map.querySourceFeatures(layerSourceName);
	        			var layerSourceData = map.getSource(layerSourceName)._data;
	    	        	
	    	        	if(layerSourceData.features.length > 0){
	    	         		for(var i=0; i<layerSourceData.features.length; i++){
	    	        			var targetFeature = layerSourceData.features[i];
	    	        			var featureProps = targetFeature.properties;
	    	        			if((feature.id && feature.id === featureProps.id) || 
	    	        			   (feature.geoId && feature.geoId.length > 0 && that.arrayContainsString(feature.geoId, featureProps.geoid))){
	    	            	    		
		            	    		// control for styling of different geometry types
	    	        				if(targetFeature.geometry){
	    		        	    	  if(targetFeature.geometry.type.toLowerCase() === "multipolygon" || targetFeature.geometry.type.toLowerCase() === "polygon"){
	    			        	    	map.setFilter("target-multipolygon-hover", ["==", "id", targetFeature.properties.id]);
	    		        	    	  }
	    		        	    	  else if(targetFeature.geometry.type.toLowerCase() === "point"){
	    			        	    	map.setFilter("target-point-hover", ["==", "id", targetFeature.properties.id]);
	    		        	    	  }
	    	        				}
	    	            	        selectedFeatures.push(targetFeature);
	    	        			}
	    	        		}
	    	        	}
	        			
	//	        		var features = targetLayer.getSource().getFeatures();
	//	        		for(var i=0; i<features.length; i++){
	//	        			var targetFeature = features[i];
	//	        			var featureProps = targetFeature.getProperties();
	//	        			if((feature.id && feature.id === featureProps.id) || 
	//	        			   (feature.geoIds && feature.geoIds.length > 0 && that.arrayContainsString(feature.geoIds, featureProps.geoid))){
	//	        				
	//	            	    	// control for styling of different geometry types
	//	            	    	if(targetFeature.getGeometry() instanceof ol.geom.MultiPolygon || targetFeature.getGeometry() instanceof ol.geom.Polygon){
	//	            	    		targetFeature.setStyle(that.getHoverPolygonStyle());
	//	            	    	}
	//	            	    	else if(targetFeature.getGeometry() instanceof ol.geom.MultiPoint || targetFeature.getGeometry() instanceof ol.geom.Point){
	//	            	    		targetFeature.setStyle(that.getHoverPointStyle());
	//	            	    	}
	//	            	        selectedFeatures.push(targetFeature);
	//	        			}
	//	        		}
	        		});
	        	}
        	}
        },
        
        selectFeature : function(feature) {
          var map = this.getMap();
          
          if(feature.layer.type === "fill"){
            map.setFilter(feature.layer.source + "-select", ["==", "id", feature.properties.id]);
          }
        },
        
        unselectFeature : function(feature) {
          var map = this.getMap();
          
          if(feature.layer.type === "fill"){
            map.setFilter(feature.layer.source + "-select", ["==", "id", ""]);
          }
        },
        
        startEditingFeature : function(featureId) {
          var map = this.getMap();
          
          this._editingControl = new MapboxDraw({
            controls: {
              point: false, line_string: true, polygon: true, trash: true, combine_features: true, uncombine_features: true
            }
          });
          map.addControl(this._editingControl);
          
          var features = map.querySourceFeatures("target-multipolygon", {
            filter: ['==', 'id', featureId]
          });
          
          for (var i = 0; i < features.length; ++i)
          {
            this._editingControl.add(features[i]);
          }
          
          map.setFilter("target-multipolygon", ["!=", "id", featureId]);
          
//          this._editingControl.changeMode("draw_polygon");
//          console.log(this._editingControl.getMode())
        },
        
        stopEditing : function() {
          var map = this.getMap();
          
          map.removeControl(this._editingControl);
        },
        
        getVectorLayersByTypeProp : function(type) {
        	var map = this.getMap();
        	var layersArr = [];
        	var layersList = this.LAYERS_LIST;
        	
        	if(layersList.length > 0){
        		layersList.forEach(function(layerName){
        			var layer = map.getLayer(layerName);
        			
        			if(layer){
        				layersArr.push(layer);
        			}
        		});
        	}
        	
        	
//        	var layers = this.getAllVectorLayers();
//        	for(var i=0; i<layers.length; i++){
//        		var layer = layers[i];
//      			var layerProps = layer.getProperties()
//      			if(layerProps.hasOwnProperty("type") && layerProps.type === type){
//      				layersArr.push(layer);
//      			}
//        	}
        	
        	return layersArr;
        },
        
        removeAllVectorLayers : function() {
        	var map = this.getMap();
        	var vecLayers = this.getAllVectorLayers();
        	
        	for(var i=0; i<vecLayers.length; i++){
        		map.removeLayer(vecLayers[i]);
        	}
        },
        
        getAllVectorLayers : function() {
        	var map = this.getMap();
        	var layersArr = [];
        	var layersList = this.LAYERS_LIST;
        	
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
        		layersArr.forEach(function(layerName){
        			var layer = map.getLayer(layerName);
        			
        			if(layer){
	    	        	var layerSourceName = layer.source;
	    	        	
	    	        	// TODO: replace _data with map.querySourceFeatures(layerSourceName);
	    	        	var layerSourceData = map.getSource(layerSourceName)._data;
	    	        	
	    	        	if(layerSourceData.features.length > 0){
	    	        		var bbox = turf.extent(layerSourceData);
	    	        		bounds.extend(
	    	        				new mapboxgl.LngLatBounds(bbox)
	    	        		);
	    	        	}
        			}
        		});
	        	
        		// check if bounds is an empty json object
        		if(Object.keys(bounds).length > 0){
        			if(bounds.getSouth() === bounds.getNorth() && bounds.getWest() === bounds.getEast()){
//	        			map.easeTo({
//	        		        center: [ bounds.getEast(), bounds.getNorth() ]
//	        		    });
        				
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
	        			map.fitBounds(bounds, {padding:10});
	        		}
        		}
        	}
        },
        
        
        zoomToExtentOfFeatures : function(featureGeoIds) {
        	var map = this.getMap();
        	var that = this;
        	var layersArr = this.getAllVectorLayers();
        	var fullExt = null;
        	
        	
        	var bounds = new mapboxgl.LngLatBounds();
        	
        	if(layersArr.length > 0){
        		layersArr.forEach(function(layer){
        			
        			if(layer){
	    	        	var layerSourceName = layer.source;
	    	        	
	    	        	// TODO: replace _data with map.querySourceFeatures(layerSourceName);
	    	        	var layerSourceData = map.getSource(layerSourceName)._data;
	    	        	
	    	        	if(layerSourceData.features.length > 0){
	    	        		layerSourceData.features.forEach(function(f){
	    	        			if(that.arrayContainsString(featureGeoIds, f.properties.geoid)){
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
//	        			map.easeTo({
//	        		        center: [ bounds.getEast(), bounds.getNorth() ]
//	        		    });
        				
        				var pt = {
        						  "type": "Feature",
        						  "properties": {},
        						  "geometry": {
        						    "type": "Point",
        						    "coordinates": [bounds.getEast(), bounds.getNorth()]
        						  }
        						};
        				
        				var buffered = turf.buffer(pt, 10, "miles");
        				var bufferedBounds = turf.extent(buffered);
        				map.fitBounds(bufferedBounds, {padding:0});
	        		}
	        		else{
	        			map.fitBounds(bounds, {padding:100});
	        		}
        		}
        	}
        	
//        	for(var i=0; i<layers.length; i++){
//        		var layer = layers[i];
//    			var features = layer.getSource().getFeatures();
//    			features.forEach(function(feature){
//    				if(that.arrayContainsString(featureGeoIds, feature.getProperties().geoid)){
//    					var featureExt = feature.getGeometry().getExtent();
//    					if(fullExt){
//            				if(featureExt[0] < fullExt[0]){
//            					fullExt[0] = featureExt[0];
//            				}
//            				if(featureExt[1] < fullExt[1]){
//            					fullExt[1] = featureExt[1];
//            				}
//            				if(featureExt[2] > fullExt[2]){
//            					fullExt[2] = featureExt[2];
//            				}
//            				if(featureExt[3] > fullExt[3]){
//            					fullExt[3] = featureExt[3];
//            				}
//            			}
//            			else{
//            				fullExt = featureExt;
//            			}
//    				}
//    			});
//        	}
//        	
//        	this.zoomToExtent(fullExt);
        },
        
        
        addVectorClickEvents : function(featureClickCallback, layersArr) {
        	var map = this.getMap();
        	var that = this;
        	
        	map.on('click', function(e) {
    		  
        		var features = map.queryRenderedFeatures(e.point, { layers: that.LAYERS_LIST });
        		
        		if(features.length){
        	    	var feature = features[0]; // only take the 1st feature
        	    	
        	    	if(feature.properties.isClickable){
        	    		featureClickCallback(feature, map);
        	    	}
        		}
        	}); 
        },
        
        
        addVectorHoverEvents : function(hoverCallback, layersArr) {
        	var map = this.getMap();
        	var that = this;
        	var selectedFeatures = [];
        	var popup;
        	var originalCursor = map.getCanvas().style.cursor;
        	
        	//TODO: Do we need to wrap this in 'load' event or should we use a different event
        	map.on('load', function () {
	        	map.on('mousemove', function(e) {
	        		var features = map.queryRenderedFeatures(e.point, { layers: that.LAYERS_LIST });
	        	    
	        	    if(features.length){
	        	    	var feature = features[0]; // only take the 1st feature
	        	    	
	        	    	if(popup){
	        	    		popup.remove();
	        	    	}
	        	    	
	                	popup = new mapboxgl.Popup({closeOnClick: true})
	                    	.setLngLat([e.lngLat.lng, e.lngLat.lat])
	                    	.setHTML(feature.properties.displaylabel)
	                    	.addTo(map);
	                	
	                	
	        	    	if(feature.properties.isClickable){
	        	    		map.getCanvas().style.cursor = 'pointer';
	        	    	}
	        	    	else{
	        	    		map.getCanvas().style.cursor = originalCursor;
	        	    	}
	        	    	
	        	    	if(selectedFeatures.length > 0){
        	    			map.setFilter("context-point-hover", ["==", "id", ""]);
        	    			map.setFilter("context-multipolygon-hover", ["==", "id", ""]);
        	    			map.setFilter("target-point-hover", ["==", "id", ""]);
        	    			map.setFilter("target-multipolygon-hover", ["==", "id", ""]);
        	    			selectedFeatures = [];
        	    		}
	        	    	
	        	    	
	        	    	// control for styling of different geometry types
	        	    	// 'fill' === polygon
	        	    	if(feature.layer.type === "fill"){
		        	    	map.setFilter("context-multipolygon-hover", ["==", "id", feature.properties.id]);
                    map.setFilter("target-multipolygon-hover", ["==", "id", feature.properties.id]);

	        	    	}
	        	    	else if(feature.layer.type === "circle"){
	        	    	  map.setFilter("context-point-hover", ["==", "id", feature.properties.id]);
	                  map.setFilter("target-point-hover", ["==", "id", feature.properties.id]);
	        	    	}
	        	    	
	        	        selectedFeatures.push(feature);
	        	        
	        	        hoverCallback(feature.properties.id);
	        	    }
	        	    else{
	        	    	map.getCanvas().style.cursor = originalCursor;
	        	    	
	        	    	map.setFilter("context-point-hover", ["==", "id", ""]);
	        	    	map.setFilter("context-multipolygon-hover", ["==", "id", ""]);
	        	    	map.setFilter("target-point-hover", ["==", "id", ""]);
                  map.setFilter("target-multipolygon-hover", ["==", "id", ""]);
	        	    	
	        	    	if(selectedFeatures.length > 0){
	        	    		hoverCallback(null);
	        	    		selectedFeatures = [];
	        	    	}
	        	    	
	        	    	if(popup){
	        	    		popup.remove();
	        	    	}
	        	    }
	    		});
	        	
	            map.on("mouseout", function() {
	                map.setFilter("context-point-hover", ["==", "id", ""]);
	                map.setFilter("target-point-hover", ["==", "id", ""]);
	            });
        	});
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
              style: 'mapbox://styles/mapbox/satellite-v8'
          });
          
        	
          this.setMap(map);
          this.configureMap();     
          
        }
      },
      Static : {
        // Static methods
      }
    });
  
})();
  
  
