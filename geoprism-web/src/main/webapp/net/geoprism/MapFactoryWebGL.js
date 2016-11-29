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
          
          this.hoverPolygonStyle = {fill:"yellow", stroke:"rgba(255, 255, 0, 0.75)", strokeWidth:3 }
      	  this.hoverPointStyle = {fill:"yellow", radius:10, stroke:"rgba(255, 255, 0, 0.75)", strokeWidth:3 };
      	  this.editFeatureStyle = {fill:"rgba(255, 0, 0, 1)", stroke:"rgba(255, 0, 0, 1)", strokeWidth:3, radius:10 };
          
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
	     	    
	     	    // add the hover layer
	     	    map.addLayer({
	     	        "id": layerName + "-hover",
	     	        "source": layerName,
		 	        "type": "circle",
		 	        "paint": {
		 	            "circle-radius": styleObj.radius,
		 	            "circle-color": that.getHoverPointStyle().fill
		 	        },
	     	        "filter": ["==", "name", ""]
	     	     });
	     	    
	     	   that.zoomToLayersExtent([layerName])
            });
   	    
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
	    	        	var layerSourceData = map.getSource(layerSourceName)._data;
	    	        	
	    	        	layerSourceData.features.forEach(function(feature) {
	    	        	    bounds.extend(feature.geometry.coordinates);
	    	        	});
        			}
        		});
	        	
	        	map.fitBounds(bounds);
        	}
        },
        
        
        addVectorClickEvents : function(featureClickCallback) {
        	var map = this.getMap();
        	var that = this;
        	
        	map.on('click', function(e) {
    		  
        		var features = map.queryRenderedFeatures(e.point, { layers: ['point'] });
        		
        		if(features.length){
        	    	var feature = features[0]; // only take the 1st feature
        	    	
        	    	if(feature.properties.isClickable){
        	    		featureClickCallback(feature, map);
        	    	}
        		}
        	}); 
        },
        
        
        addVectorHoverEvents : function(hoverCallback) {
        	var map = this.getMap();
        	var that = this;
        	var selectedFeatures = [];
        	var popup;
        	var originalCursor = map.getCanvas().style.cursor;
        	
        	//TODO: Do we need to wrap this in 'load' event or should we use a different event
        	map.on('load', function () {
	        	map.on('mousemove', function(e) {
	        		var features = map.queryRenderedFeatures(e.point, { layers: ["point"] });
	        	    
	        	    if(features.length){
	        	    	var feature = features[0]; // only take the 1st feature
	        	    	map.setFilter("point-hover", ["==", "id", feature.properties.id]);
	        	    	
	        	    	if(popup){
	        	    		popup.remove();
	        	    	}
	        	    	
	                	popup = new mapboxgl.Popup({closeOnClick: true})
	                    .setLngLat([e.lngLat.lng, e.lngLat.lat])
	                    .setHTML(features[0].properties.displaylabel)
	                    .addTo(map);
	                	
	                	
	        	    	if(feature.properties.isClickable){
	        	    		map.getCanvas().style.cursor = 'pointer';
	        	    	}
	        	    	else{
	        	    		map.getCanvas().style.cursor = originalCursor;
	        	    	}
	        	    	
	        	    	
	        	    	// control for styling of different geometry types
	        	    	// 'fill' === polygon
	        	    	if(feature.layer.type === "fill"){
	//        	    		feature.setStyle(that.getHoverPolygonStyle());
	        	    		console.log("polygon hover")
	        	    	}
	        	    	else if(feature.layer.type === "circle"){
	//        	    		feature.setStyle(that.getHoverPointStyle());
	        	    		console.log("point hover")
	        	    	}
	        	    	
	        	        selectedFeatures.push(feature);
	        	        
	        	        hoverCallback(feature.properties.id);
	        	    }
	        	    else{
	        	    	map.getCanvas().style.cursor = originalCursor;
	        	    	
	        	    	map.setFilter("point-hover", ["==", "id", ""]);
	        	    	
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
	                map.setFilter("point-hover", ["==", "id", ""]);
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
  
  
