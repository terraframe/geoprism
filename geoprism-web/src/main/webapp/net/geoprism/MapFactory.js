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
     "googleTerrain" : "Google Terrain",
     "editBtnTooltip": "Add a new location",
     "saveEditsBtnTooltip": "Save all your edits",
     "cancelEditsBtnTooltip": "Cancel any edits and close this edit session"
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
      
      getCurrentBounds : {
        IsAbstract : true
      },
      
      zoomToVectorDataExtent : {
    	  IsAbstract : true
      },
      
      addVectorHoverEvents : {
    	  IsAbstract : true
      },
      
      addVectorClickEvents : {
    	  IsAbstract : true
      },
      
      restoreOriginalFeatures : {
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
      
      addVectorLayer : {
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
      },
      
      clear : {
        IsAbstract : true
      },
      
      addOverlay : {
        IsAbstract : true
      },
        
      clearOverlays : {
        IsAbstract : true
      },
      
      getSaveCallback : {
        IsAbstract : true
      },
      
      setSaveCallback : {
        IsAbstract : true
      }
    }
  });
  
  var OpenLayersMap = Mojo.Meta.newClass('net.geoprism.gis.OpenLayersMap', {
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
          
          this._saveCallback = null;
          
      	  this.hoverPolygonStyle = new ol.style.Style({
    	    fill: new ol.style.Fill({
    	        color: 'rgba(255, 255, 0, 0.75)'
    	    }),
    	    stroke: new ol.style.Stroke({
    	        color: 'yellow'
    	    })
    	  });
    	
    	  this.hoverPointStyle = new ol.style.Style({
	          image: new ol.style.Circle({
	              radius: 7,
	              fill: new ol.style.Fill({ color: "rgba(255, 255, 0, 0.75)" }),
	              stroke: new ol.style.Stroke({ color: "yellow", width: 3 })
	            })
	      });
    	 
    	  this.editFeatureStyle = new ol.style.Style({
  		      fill: new ol.style.Fill({color: "rgba(255, 0, 0, 0.5)"}),
  		      stroke: new ol.style.Stroke({color: "rgba(255, 0, 0, 1)", width: 3}),
  		      image: new ol.style.Circle({
  		        radius: 9,
  		        fill: new ol.style.Fill({color: "rgba(255, 0, 0, 0.5)"}),
  		        stroke: new ol.style.Stroke({ color: "rgba(255, 0, 0, 1)", width: 3})
  		      })
    	  });
          
          this.renderMap();
        },
        
        getSaveCallback : function() {
        	return this._saveCallback;
        },
        
        setSaveCallback : function(callback) {
        	this._saveCallback = callback;
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
         */
        showLayer : function(layer, stackIndex) {
          var oLayer = this._cache[layer.key];
          
          if(oLayer != null && oLayer.showing == false) {
            var map = this.getMap();
            if(layer.layerType.toLowerCase() === "google"){
            	map.getLayers().insertAt(stackIndex, oLayer);
            }
            else if(stackIndex !== null && stackIndex >= 0){
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
        hideLayer : function(layer) {
          var oLayer = this._cache[layer.key];
          
          if (oLayer != null && oLayer.showing == true) {
            var map = this.getMap();
            map.removeLayer(oLayer);
            
            if(oLayer.removable) {
              delete this._cache[layer.layerId];            
            }
            else {
              oLayer.showing = false;  
            }
          }
        },
        
        addVectorLayer : function(layerAsGeoJSON, styleObj, type, stackingIndex) {
        	var map = this.getMap();
        	
        	var vectorSource = new ol.source.Vector({
        		features: (new ol.format.GeoJSON()).readFeatures(layerAsGeoJSON, { featureProjection: MapWidget.MAPSRID })
        	});
        	
        	// Assume all features in a layer are of the same geom type
        	var firstFeatureGeom = vectorSource.getFeatures()[0].getGeometry();
        	
        	var style = null;
        	if(firstFeatureGeom instanceof ol.geom.MultiPolygon || firstFeatureGeom instanceof ol.geom.Polygon){
	        	style = new ol.style.Style({
	    	        fill: new ol.style.Fill({ color: styleObj.fill }),
	    	        stroke: new ol.style.Stroke({
	    	          color: styleObj.strokeColor,
	    	          width: styleObj.strokeWidth
	    	        })
	    	    });
        	}
        	else if(firstFeatureGeom instanceof ol.geom.MultiPoint || firstFeatureGeom instanceof ol.geom.Point){
        		
        		style = new ol.style.Style({
    	          image: new ol.style.Circle({
    	              radius: styleObj.radius,
    	              fill: new ol.style.Fill({ color: styleObj.fill }),
    	              stroke: new ol.style.Stroke({ color: styleObj.strokeColor, width: styleObj.strokeWidth })
    	            })
    	        })
        	}

            // a vector layer to render the source
            var vectorLayer = new ol.layer.Vector({
              source: vectorSource,
              style: style,
              projection: MapWidget.MAPSRID
            });
            
            vectorLayer.setProperties({"type":type});
            map.getLayers().insertAt(stackingIndex, vectorLayer);
        },
        
        
        getVectorLayersByTypeProp : function(type) {
        	var map = this.getMap();
        	var layersArr = [];
        	var layers = this.getAllVectorLayers();
        	for(var i=0; i<layers.length; i++){
        		var layer = layers[i];
      			var layerProps = layer.getProperties()
      			if(layerProps.hasOwnProperty("type") && layerProps.type === type){
      				layersArr.push(layer);
      			}
        	}
        	
        	return layersArr;
        },
        
        
        addVectorClickEvents : function(featureClickCallback) {
        	var map = this.getMap();
        	var that = this;
        	
        	map.on('click', function(evt) {
    		  var feature = map.forEachFeatureAtPixel(evt.pixel,
    		    function(feature, layer) {
    			  if(feature.getProperties().isClickable){
    				  map.getOverlays().getArray()[0].setPosition(undefined);
    				  featureClickCallback(feature, map);
    			  }
    		    });
        	}); 
        	
        	
        	map.getViewport().addEventListener('contextmenu', function(evt) {
              evt.preventDefault();
              evt.stopPropagation();
              
      		  var feature = map.forEachFeatureAtPixel(map.getEventPixel(evt),
      		    function(feature, layer) {
      			  if(feature.getProperties().isClickable){
      				  map.getOverlays().getArray()[0].setPosition(undefined);
      				  featureClickCallback(feature, map);
      			  }
      		    });
          	}); 
        },
        
        
        createBasicFeaturePopup : function() {
        	var mapEl = document.getElementById(this.getElementId());
        	
        	var popup = document.createElement("div");
        	popup.className += " ol-popup";
        	popup.id = "popup";
        	
        	var popupHeading = document.createElement("h3");
        	popupHeading.className += "popup-heading";
        	var headingText = document.createTextNode("");
        	popupHeading.appendChild(headingText);
        	
        	var popupContent = document.createElement("div");
        	popupContent.id = "popup-content";
        	
        	mapEl.appendChild(popup);
        	popup.appendChild(popupHeading);
        	popup.appendChild(popupContent);
        	
        	return popup;
        },
        
        
        setPopupHeading : function(popupEl, headingStr){
        	var children = popupEl.children;
        	for(var i=0; i<children.length; i++){
        		var el = children[i];
        		var elClasses = el.classList;
        		for(var c=0; c<elClasses.length; c++){
        			var cls = elClasses[c];
        			if(cls && cls.indexOf("popup-heading") !== -1){
        				el.innerHTML = headingStr;
        				
        			}
        		};
        	};
        },
        
        
        addVectorHoverEvents : function(hoverCallback) {
        	var map = this.getMap();
        	var that = this;
        	var selectedFeatures = [];
        	
        	
        	var popup = this.createBasicFeaturePopup();
        	

	        /**
	         * Create an overlay to anchor the popup to the map.
	         */
	        var overlay = new ol.Overlay(/** @type {olx.OverlayOptions} */ ({
	          element: popup,
	          autoPan: true,
	          autoPanAnimation: {
	            duration: 250
	          }
	        }));


	        map.addOverlay(overlay);
        	
	        
        	var clearSelctedFeatures = function(){
        		if(selectedFeatures.length > 0){
    	    		for(var i=0; i<selectedFeatures.length; i++){
    	    			selectedFeatures[i].setStyle(null);
    	    		}
     	    		selectedFeatures = [];
     	        }
        	}
        	
        	
        	// a bit of a hack to restore original cursor css to take advantage of openlayers handling of 
        	// cursor settings for browser compatibility (i.e. -webkit-grab)
        	var originalCursor = $("#"+map.getTarget()).children(".ol-viewport").css("cursor");
        	var unHoverableFeatureCache = null;
        	map.on('pointermove', function (evt) {
        		if(evt.dragging) return;
        	    
        	    var feature = map.forEachFeatureAtPixel( evt.pixel, function(ft, l){return ft;} );
        	    
        	    if(map.getProperties().hasOwnProperty("gdbEditModeEnabled") && map.getProperties().gdbEditModeEnabled){
        	    	$("#"+map.getTarget()).children(".ol-viewport").css("cursor", "crosshair");
        	    }
        	    else if(feature && feature !== selectedFeatures[0] && feature.getProperties().isHoverable){
        	    	if(feature.getProperties().isClickable){
        	    		$("#"+map.getTarget()).children(".ol-viewport").css("cursor", "pointer");
        	    	}
        	    	else{
        	    		$("#"+map.getTarget()).children(".ol-viewport").css("cursor", originalCursor);
        	    	}
        	    	
        	    	// clear existing selected feature if transitioning directly to another feature.
        	    	// usually caused by overlapping features at the edge of one of the features.
        	    	clearSelctedFeatures();
        	    	
        	    	// control for styling of different geometry types
        	    	if(feature.getGeometry() instanceof ol.geom.MultiPolygon || feature.getGeometry() instanceof ol.geom.Polygon){
        	    		feature.setStyle(that.getHoverPolygonStyle());
        	    	}
        	    	else if(feature.getGeometry() instanceof ol.geom.MultiPoint || feature.getGeometry() instanceof ol.geom.Point){
        	    		feature.setStyle(that.getHoverPointStyle());
        	    	}
        	        selectedFeatures.push(feature);
        	        
        	        that.setPopupHeading(popup, feature.getProperties().displaylabel);
        	        
        	        overlay.setPosition(evt.coordinate);
        	        
        	        hoverCallback(feature.getProperties().id);
        	        
        	        unHoverableFeatureCache = null;
        	    } 
        	    else if(feature && feature === selectedFeatures[0]){
        	    	overlay.setPosition(evt.coordinate);
        	    	unHoverableFeatureCache = null;
        	    }
        	    else if(feature && feature !== selectedFeatures[0] && !feature.getProperties().isHoverable){
        	    	$("#"+map.getTarget()).children(".ol-viewport").css("cursor", originalCursor);
        	    	
        	    	// preventing hoverchange on every pixel diring scroll over context geographies
        	    	if(feature !== unHoverableFeatureCache){
        	    		unHoverableFeatureCache = feature;
        	    		if(selectedFeatures.length > 0){
        	    			hoverCallback(null);
        	    		}
        	    	}
        	    	
        	    	overlay.setPosition(undefined);
        	    	
        	    	clearSelctedFeatures();
        	    }
        	    else if(!feature) {
        	    	$("#"+map.getTarget()).children(".ol-viewport").css("cursor", originalCursor);
        	    	
        	    	if(selectedFeatures.length > 0){
        	    		hoverCallback(null);
        	    	}
        	    	
        	    	overlay.setPosition(undefined);
        	    	
        	    	clearSelctedFeatures();
        	    	
        	    	unHoverableFeatureCache = null;
        	    }
    		});
        },
        
        removeAllVectorLayers : function() {
        	var map = this.getMap();
        	var vecLayers = this.getAllVectorLayers();
        	
        	for(var i=0; i<vecLayers.length; i++){
        		map.removeLayer(vecLayers[i]);
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
         * Adding edit controls (buttons) to the map
         */
        enableEdits : function(saveCallback) {
        	this.setSaveCallback(saveCallback);
        	this.addEditComponents();
        	this.addNewPointControl(null);
        },
        
        /**
         * Removing edit controls (buttons) from the map
         */
        disableEdits : function() {
        	var map = this.getMap();
        	var mapControls = map.getControls().getArray();
    		var editModeCtrl = this.getCustomControl("addNewPointControl");
    		var saveEditsCtrl = this.getCustomControl("saveEditsControl");
    		var cancelEditsCtrl = this.getCustomControl("cancelEditsControl");
    		
    		this.closeEditSession();
        	
        	if(editModeCtrl !== null){
        		this.removeControl(editModeCtrl);
        	}
        	
        	if(saveEditsCtrl !== null && cancelEditsCtrl !== null){
        		this.removeControl(saveEditsCtrl);
        		this.removeControl(cancelEditsCtrl);
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
        
        
        focusOnFeature : function(feature) {
        	var map = this.getMap();
        	var that = this;
        	var selectedFeatures = [];
        	var targetLayers = this.getVectorLayersByTypeProp("TARGET");
        	if(targetLayers.length > 0){
        		targetLayers.forEach(function(targetLayer){
	        		var features = targetLayer.getSource().getFeatures();
	        		for(var i=0; i<features.length; i++){
	        			var targetFeature = features[i];
	        			var featureProps = targetFeature.getProperties();
	        			if((feature.id && feature.id === featureProps.id) || 
	        			   (feature.geoIds && feature.geoIds.length > 0 && that.arrayContainsString(feature.geoIds, featureProps.geoid))){
	        				
	            	    	// control for styling of different geometry types
	            	    	if(targetFeature.getGeometry() instanceof ol.geom.MultiPolygon || targetFeature.getGeometry() instanceof ol.geom.Polygon){
	            	    		targetFeature.setStyle(that.getHoverPolygonStyle());
	            	    	}
	            	    	else if(targetFeature.getGeometry() instanceof ol.geom.MultiPoint || targetFeature.getGeometry() instanceof ol.geom.Point){
	            	    		targetFeature.setStyle(that.getHoverPointStyle());
	            	    	}
	            	        selectedFeatures.push(targetFeature);
	        				
	//        				var geomType = targetFeature.getGeometry().getType();
	//        				if(geomType === "Point"){
	//        					var coords = targetFeature.getGeometry().getCoordinates();
	//        					coords = ol.proj.transform(coords, MapWidget.MAPSRID, MapWidget.DATASRID);
	//        					this.setView(null, coords, map.getView().getZoom());
	//        				}
	//        				else{
	//        					var extent = targetFeature.getGeometry().getExtent();
	//            				extent = ol.extent.applyTransform(extent, ol.proj.getTransform(MapWidget.MAPSRID, MapWidget.DATASRID));
	//        					this.setView(extent, null, null);
	//        				}
	        				
	        			}
	        		}
        		});
        	}
        },
        
        
        focusOffFeature : function(feature) {
        	var map = this.getMap();
        	var that = this;
        	var selectedFeatures = [];
        	var targetLayers = this.getVectorLayersByTypeProp("TARGET");
        	if(targetLayers.length > 0){
        		targetLayers.forEach(function(targetLayer){
	        		var features = targetLayer.getSource().getFeatures();
	        		for(var i=0; i<features.length; i++){
	        			var targetFeature = features[i];
	        			var featureProps = targetFeature.getProperties();
	        			if((feature.id && feature.id === featureProps.id) || (feature.geoIds && feature.geoIds.length > 0 && that.arrayContainsString(feature.geoIds, featureProps.geoid))){
	        				
	        				targetFeature.setStyle(null);
	        			}
	        		}
        		});
        	}
        },
        
        
        toggleBaseLayer : function(targetLayer, toggleOffLayer){
        	var map = this.getMap();
        	var that = this;
        	
        	this.hideLayer(toggleOffLayer);
        	this.showLayer(targetLayer, 0);
        },
        
        
        createBaseLayerControl : function(hoverCallback, hoverOffCallback){
        	var map = this.getMap();
        	
        	
        	/**
             * @constructor
             * @extends {ol.control.Control}
             * @param {Object=} opt_options Control options.
             */
            var baseLayerToggleControl = function(opt_options) {
              var options = opt_options || {};

              var button = document.createElement('button');
              button.className = 'base-map-btn fa fa-bars';
              
              button.addEventListener('mouseover', hoverCallback, false);
              button.addEventListener('mouseout', hoverOffCallback, false);


              var element = document.createElement('div');
              element.className = 'base-map-btn-wrapper ol-unselectable ol-control';
              element.appendChild(button);

              ol.control.Control.call(this, {
                element: element,
                target: options.target
              });
            };
            
            
            // extend
            ol.inherits(baseLayerToggleControl, ol.control.Control);
            
            var thisBaseLayerToggleControl = new baseLayerToggleControl();
            map.addControl(thisBaseLayerToggleControl);
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
            if(base.ENABLED === "true"){
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
	              else if(base.LAYER_SOURCE_TYPE.toLowerCase() === "bing"){
	            	baseObj.setSource( 
	            	  new ol.source.BingMaps(base.LAYER_SOURCE_OPTIONS)
	            	);
	              }
	              
	              baseObj._gdbisdefault = base.DEFAULT;
	              baseObj._gdbcustomtype = base.LAYER_SOURCE_TYPE;
	              baseObj._gdbCustomLabel = this.localize(base.LOCLIZATION_KEY);
	              baseObj.showing = false;
	              baseObj.removable = false;
	
	              // Add the baseObj to the layer cache
	              this._cache[i] = baseObj;
	                
	              var layer = {
	                layerId : i,
	                key : i,
	                isActive : (base.DEFAULT === 'true'),
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
	              baseObj.removable = false;
	
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
	            else if(base.LAYER_TYPE.toLowerCase() === "google"){
	            	var baseObj;
	            	
	            	if(base.LAYER_SOURCE_TYPE.toLowerCase() === "googlesatellite"){
		                baseObj = new olgm.layer.Google(
		                	{mapTypeId: google.maps.MapTypeId.SATELLITE},
		                    {visible: base.VISIBLE},
		                	{isdefault: base.DEFAULT},
		                    base.CUSTOM_TYPE_OPTIONS
		                 );       
	            	}
	            	else if(base.LAYER_SOURCE_TYPE.toLowerCase() === "googlestreets"){
		                baseObj = new olgm.layer.Google(
			                    {visible: base.VISIBLE},
			                	{isdefault: base.DEFAULT},
			                    base.CUSTOM_TYPE_OPTIONS
			                 );   
	            	}
	            	else if(base.LAYER_SOURCE_TYPE.toLowerCase() === "googleterrain"){
		                baseObj = new olgm.layer.Google(
		                		{mapTypeId: google.maps.MapTypeId.TERRAIN},
			                    {visible: base.VISIBLE},
			                	{isdefault: base.DEFAULT},
			                    base.CUSTOM_TYPE_OPTIONS
			                 );   
	            	}
	                
	               baseObj.showing = false;
	               baseObj.removable = false;
	
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
          oLayer.removable = true;

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
         * @dataSRID - <OPTIONAL> string defining SRID of the data (ex: 'EPSG:4326'). 
         * 
         * NOTE: dataSRID also operates like a flag only applying transforms if not null
         * 
         * <public> - called externally
         */
        setView : function(bounds, center, zoomLevel, dataSRID) {
          var map = this.getMap();
          
          if(center && zoomLevel){
        	if(dataSRID){
        		center = ol.proj.transform(center, dataSRID, MapWidget.MAPSRID);
        	}
            map.getView().setCenter(center);
            map.getView().setZoom(zoomLevel);
          }
          else if(bounds){
            // Handle points (2 coord sets) & polygons (4 coord sets)
            if (bounds.length === 2){
              this.setBounds(bounds[0], bounds[1]);
              
              // TODO: test set center with dataSRID. 
              if(!dataSRID){
            	dataSRID = MapWidget.DATASRID;
              }
              map.getView().setCenter(bounds, dataSRID, MapWidget.MAPSRID);
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
              
              if(dataSRID){
            	bBox = ol.extent.applyTransform(bBox, ol.proj.getTransform(dataSRID, MapWidget.MAPSRID));
          	  }
              map.getView().fit(bBox, map.getSize());
            };
          };
        },
        
        
        zoomToExtentOfFeatures : function(featureGeoIds) {
        	var map = this.getMap();
        	var that = this;
        	var layers = this.getAllVectorLayers();
        	var fullExt = null;
        	
        	for(var i=0; i<layers.length; i++){
        		var layer = layers[i];
    			var features = layer.getSource().getFeatures();
    			features.forEach(function(feature){
    				if(that.arrayContainsString(featureGeoIds, feature.getProperties().geoid)){
    					var featureExt = feature.getGeometry().getExtent();
    					if(fullExt){
            				if(featureExt[0] < fullExt[0]){
            					fullExt[0] = featureExt[0];
            				}
            				if(featureExt[1] < fullExt[1]){
            					fullExt[1] = featureExt[1];
            				}
            				if(featureExt[2] > fullExt[2]){
            					fullExt[2] = featureExt[2];
            				}
            				if(featureExt[3] > fullExt[3]){
            					fullExt[3] = featureExt[3];
            				}
            			}
            			else{
            				fullExt = featureExt;
            			}
    				}
    			});
        	}
        	
        	this.zoomToExtent(fullExt);
        },
        
        
        zoomToVectorDataExtent : function() {
        	var map = this.getMap();
        	var layers = this.getAllVectorLayers();
        	var fullExt = null;
        	
        	for(var i=0; i<layers.length; i++){
        		var layer = layers[i];
    			var layerExt = layer.getSource().getExtent();
    			if(fullExt){
    				if(layerExt[0] < fullExt[0]){
    					fullExt[0] = layerExt[0];
    				}
    				if(layerExt[1] < fullExt[1]){
    					fullExt[1] = layerExt[1];
    				}
    				if(layerExt[2] > fullExt[2]){
    					fullExt[2] = layerExt[2];
    				}
    				if(layerExt[3] > fullExt[3]){
    					fullExt[3] = layerExt[3];
    				}
    			}
    			else{
    				fullExt = layerExt;
    			}
        	}
        	
        	if(fullExt){
        		this.zoomToExtent(fullExt);
        	}
        	
        },
        
        zoomToExtent : function(extent) {
        	var map = this.getMap();
    		var singlePointCoords = null;
    		var vecLayers = this.getAllVectorLayers();
    		if(vecLayers.length === 1){
    			var layerFeatures = vecLayers[0].getSource().getFeatures();
    			if(layerFeatures.length === 1){
    				var theOnlyFeatureGeom = layerFeatures[0].getGeometry();
    				var theOnlyFeatureGeomType = theOnlyFeatureGeom.getType();
    				if(theOnlyFeatureGeomType === "Point"){
    					singlePointCoords = theOnlyFeatureGeom.getCoordinates();
    				}
    			}
    		}
    		
    		if(singlePointCoords){
    			this.setView(null, singlePointCoords, 12, null);
    		}
    		else{
    			map.getView().fit(extent, map.getSize());
    		}
        },
        
        
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
          var that = this;
          if(this.getMap() != null){
            this.removeStaleMapFragments();
          }
          
          var configControls = MapConfig._CONTROLS;
          var controls = [];
          configControls.forEach(function(ctrl){
        	  if(ctrl.TYPE.toUpperCase() === "ATTRIBUTION"){
        		  var attribution = new ol.control.Attribution({
        	            collapsible: ctrl.COLLAPSIBLE ? ctrl.COLLAPSIBLE : true,
        	            tipLabel: that.localize("attributionELTooltip")
        	      });
        		  controls.push(attribution);
        	  }
        	  else if(ctrl.TYPE.toUpperCase() === "SCALELINE"){
        		  var scaleLine = new ol.control.ScaleLine({
        			  className: ctrl.CLASSNAME, 
        			  target: document.getElementById(ctrl.TARGET),
        			  units: ctrl.UNITS ? ctrl.UNITS : "metric"
        		  });
        		  controls.push(scaleLine);
        	  }
          });
          
          
          var view = new ol.View({ 
              center: this.getCenter(), 
              zoom: this.getZoomLevel(),
              maxZoom: 20
          });
          
          var mapConfig = {
                layers: [], // base maps will be added later
                controls: ol.control.defaults({ attribution: false }).extend(controls),
                target: this.getMapElementId(),
                loadTilesWhileInteracting: true,
                loadTilesWhileAnimating: true,
                view: view
              }
          
          var googleEnabled = false;
          var baseMaps = MapConfig._BASEMAPS;
          for(var i=1; i<baseMaps.length; i++){
        	  var base = baseMaps[i];
        	  if(base.LAYER_TYPE.toLowerCase() === "google" && base.ENABLED === "true"){
        		  googleEnabled = true;
        		  mapConfig.interactions = olgm.interaction.defaults();
        		  break;
        	  }
          }
          
          var map = new ol.Map(mapConfig);
          
          if(googleEnabled){
        	  var olGM = new olgm.OLGoogleMaps({map: map});
        	  olGM.activate();
          }
        	
          this.setMap(map);
          this.configureMap();     
        },
        
        addEditComponents : function(targetFeature) {
        	var map = this.getMap();
        	var that = this;
        	
        	map.setProperties({"editFeatures":new ol.Collection()});
        	
	        var editFeatureOverlay = new ol.layer.Vector({
	          	  map: map,
	          	  source: new ol.source.Vector({
	          	    features: map.getProperties().editFeatures,
	          	    useSpatialIndex: false // optional, might improve performance
	          	  }),
	          	  style: that.getEditFeatureStyle(),
	          	  updateWhileAnimating: true, // optional, for instant visual feedback
	          	  updateWhileInteracting: true // optional, for instant visual feedback
	        });
	          
	        editFeatureOverlay.setProperties({"editLayer":true});
	        editFeatureOverlay.setMap(map);
	        map.setProperties({"editFeatureOverlay":editFeatureOverlay});
        },
        
        
        removeInteractions : function(interactions) {
        	var map = this.getMap();
        	
        	interactions.forEach(function(interaction){
        	  if(interaction){
        		  map.removeInteraction(interaction);
        	  }
          	});
        },
        
        
        addNewPointControl : function(targetFeature) {
        	var map = this.getMap();
        	var that = this;
        	var editBtnTooltip = this.localize("editBtnTooltip");
        	
        	
        	var addEditInteractions = function() {
        		var selectedDrawType = "Point";
        	    
        		var draw = new ol.interaction.Draw({
                  features: map.getProperties().editFeatures,
                  type: (selectedDrawType),
                  freehandCondition: function(event){
                	  return ol.events.condition.never(event);
                  }
                });
                
                // controlling for only having one line or Polygon being created at a time.
	            draw.on('drawstart', function (e) {
	                
	                var featureInEditCacheIsExistingGeoEntity = false;
	            	var existingEditFeature = null;
	            	var existingEditFeatures = map.getProperties().editFeatures;
                	if(existingEditFeatures.getArray().length === 2){
                		var existingEditFeature = existingEditFeatures.getArray()[0];
                		var existingEditFeatureProps = existingEditFeature.getProperties();
          	    		
          	    		if(existingEditFeatureProps.hasOwnProperty("isEditFeature") && existingEditFeatureProps.isEditFeature){
          	    			featureInEditCacheIsExistingGeoEntity = true;
          	    		}
                	}
                	
                	if(selectedDrawType !== 'Point') {
	                	map.getProperties().editFeatures.clear();  // implicit remove of last feature.
	                }
                	else if(featureInEditCacheIsExistingGeoEntity){
                		var mapProps = map.getProperties();
                		if(mapProps.hasOwnProperty("editFeatureOverlay")){
                			var editFeatureOverlay = mapProps.editFeatureOverlay;
                			var editFeaturesSource = editFeatureOverlay.getSource();
                			editFeaturesSource.removeFeature(editFeaturesSource.getFeatures()[1])
                		}
                	}
	            });
                
                // controlling for only having one point being created at a time. 
                draw.on('drawend', function(e) {
                	e.preventDefault();
                	
	                var featureInEditCacheIsExistingGeoEntity = false;
	            	var existingEditFeature = null;
	            	var existingEditFeatures = map.getProperties().editFeatures;
                	if(existingEditFeatures.getArray().length > 0){
                		var existingEditFeature = existingEditFeatures.getArray()[0];
                		var existingEditFeatureProps = existingEditFeature.getProperties();
          	    		
          	    		if(existingEditFeatureProps.hasOwnProperty("isEditFeature") && existingEditFeatureProps.isEditFeature){
          	    			featureInEditCacheIsExistingGeoEntity = true;
          	    		}
                	}
                	
                	if(selectedDrawType === "Point"){
	                	var existingEditFeatures = map.getProperties().editFeatures;
	                	if(existingEditFeatures.getArray().length > 0){
	                		if(!featureInEditCacheIsExistingGeoEntity){
		                		var mapProps = map.getProperties();
		                		if(mapProps.hasOwnProperty("editFeatureOverlay")){
		                			var editFeatureOverlay = mapProps.editFeatureOverlay;
		                			var editFeaturesSource = editFeatureOverlay.getSource();
		                			editFeaturesSource.removeFeature(editFeaturesSource.getFeatures()[0])
		                		}
	                		}
	                	}
                	}
                });
                
                var modify = new ol.interaction.Modify({
                    features: map.getProperties().editFeatures,
                    // the SHIFT key must be pressed to delete vertices, so
                    // that new vertices can be drawn at the same position
                    // of existing vertices
                    deleteCondition: function(event) {
                      return ol.events.condition.shiftKeyOnly(event) &&
                          ol.events.condition.singleClick(event);
                    }
                });
                
                
                map.addInteraction(modify);
                map.addInteraction(draw);
                map.setProperties({"gdbEditModeEnabled":true});
                
                that.addEditSessionControls(that.getSaveCallback());
            }
        	
        	
        	/**
             * @constructor
             * @extends {ol.control.Control}
             * @param {Object=} opt_options Control options.
             */
            var addNewPointControl = function(opt_options) {
              var options = opt_options || {};

              var button = document.createElement('button');
              button.className = 'enable-edit-mode-btn fa fa-map-marker';
              button.title = editBtnTooltip;
              
              function toggleInteraction() {
            	  var existingInteractions = map.getInteractions().getArray();
            	  var drawInteraction = null;
            	  var modifyInteraction = null
            	  for(var i=0; i<existingInteractions.length; i++){
            		  var interaction = existingInteractions[i];
            		  if(interaction instanceof ol.interaction.Draw){
            			  drawInteraction = interaction;
            		  }
            		  else if(interaction instanceof ol.interaction.Modify){
            			  modifyInteraction = interaction;
            		  }
            	  };
            	  
            	  if(drawInteraction !== null && modifyInteraction !== null){
            		  that.closeEditSession();
            	  }
            	  else{
            		  addEditInteractions();
            	  }
              }

              button.addEventListener('click', toggleInteraction, false);

              var element = document.createElement('div');
              element.className = 'enable-edit-mode-btn-wrapper ol-unselectable ol-control';
              element.appendChild(button);

              ol.control.Control.call(this, {
                element: element,
                target: options.target
              });
            };
            
            
            // extend
            ol.inherits(addNewPointControl, ol.control.Control);
            
            
            // if there is a target feature this should be an edit session opened for an existing feature. 
            // probably as a result of clicking on the feature in the map. 
            if(targetFeature){
            	var targetFeatureClone = targetFeature.clone();
            	// there should be only one returned during an edit session
            	var targetLayers = this.getVectorLayersByTypeProp("TARGET");
            	targetLayers[0].setProperties({"editFeatureCache":[targetFeature]});
            	targetLayers[0].getSource().removeFeature(targetFeature);
            	
            	targetFeatureClone.setProperties({"isEditFeature":true})
            	targetFeatureClone.setStyle(this.getEditFeatureStyle()); // styles are stored for each feature
        		map.getProperties().editFeatures.push(targetFeatureClone);
            	
            	// remove the create point control if editing an existing feature
        		var editModeCtrl = this.getCustomControl("addNewPointControl");
            	
            	if(editModeCtrl !== null){
            		this.removeControl(editModeCtrl);
            	}
            	
        		addEditInteractions();
        		this.moveEditSessionControls("UP");
        	}
            // else should only be true when entering a universal level that allows edits
            else{
            	var thisAddNewPointControl = new addNewPointControl();
            	thisAddNewPointControl.setProperties({"customControl":"addNewPointControl"});
                map.addControl(thisAddNewPointControl);
            }
        },
        
        
        addEditSessionControls : function() {
        	var map = this.getMap();
        	var that = this;
        	var saveEditsBtnTooltip = this.localize("saveEditsBtnTooltip");
        	var cancelEditsBtnTooltip = this.localize("cancelEditsBtnTooltip");
        	
        	
            /**
             * @constructor
             * @extends {ol.control.Control}
             * @param {Object=} opt_options Control options.
             */
            var saveEditsControl = function(opt_options) {
              var selectedDrawType = "Point"; // currently only supporting point
              var options = opt_options || {};

              var button = document.createElement('button');
              button.className = 'save-edits-btn fa fa-floppy-o';

              function saveAllFeatures() {
            	
      	    	var editFeatures = map.getProperties().editFeatures.getArray();
      	    	if(editFeatures.length === 1){
      	    		var editFeature = editFeatures[0];
      	    		var editFeatureProps = editFeature.getProperties();
      	    		var editGeom = editFeature.getGeometry().getCoordinates();
	          	    var transformedEditGeom = ol.proj.transform(editGeom, MapWidget.MAPSRID, MapWidget.DATASRID);
	          	    var editGeomWKT = "POINT("+ transformedEditGeom[0] + " " + transformedEditGeom[1] +")";
	          	    editFeature.setProperties({"wktGeom":editGeomWKT});
      	    		
	          	    var saveCallback = that.getSaveCallback();
	          	  
      	    		if(editFeatureProps.hasOwnProperty("isEditFeature") && editFeatureProps.isEditFeature){
      	    			saveCallback(editFeature, false);
      	    		}
      	    		else{
      	    			saveCallback(editGeomWKT, true);
      	    		}
      	    	}
              }
              

              button.addEventListener('click', saveAllFeatures, false);
              //button.addEventListener('touchstart', addInteraction, false);

              var element = document.createElement('div');
              element.className = 'save-edits-btn-wrapper ol-unselectable ol-control';
              element.appendChild(button);

              ol.control.Control.call(this, {
                element: element,
                target: options.target
              });
            };
            
            
            /**
             * @constructor
             * @extends {ol.control.Control}
             * @param {Object=} opt_options Control options.
             */
            var cancelEditsControl = function(opt_options) {
              var selectedDrawType = "Point"; // currently only supporting point
              var options = opt_options || {};

              var button = document.createElement('button');
              button.className = 'cancel-edits-btn fa fa-times';

              function restoreOriginalFeatures() {
            	// There should be only one returned during an edit session
            	var targetLayers = that.getVectorLayersByTypeProp("TARGET");
              	var targetLayerProps = targetLayers[0].getProperties();
              	if(targetLayerProps.hasOwnProperty("editFeatureCache")){
              		var originalEditFeature = targetLayerProps.editFeatureCache[0];
              		targetLayers[0].getSource().addFeature(originalEditFeature);
              	}
        	    
        	    that.closeEditSession();
              }
              

              button.addEventListener('click', restoreOriginalFeatures, false);

              var element = document.createElement('div');
              element.className = 'cancel-edits-btn-wrapper ol-unselectable ol-control';
              element.appendChild(button);

              ol.control.Control.call(this, {
                element: element,
                target: options.target
              });
            };
            
        	ol.inherits(saveEditsControl, ol.control.Control);
        	ol.inherits(cancelEditsControl, ol.control.Control);
        	
        	var thisSaveEditsControl = new saveEditsControl();
        	thisSaveEditsControl.setProperties({"customControl":"saveEditsControl"});
        	map.addControl(thisSaveEditsControl);
        	
        	var thisCancelEditsControl = new cancelEditsControl();
        	thisCancelEditsControl.setProperties({"customControl":"cancelEditsControl"});
        	map.addControl(thisCancelEditsControl);
        },
        
        
        restoreOriginalFeatures : function() {
        	// There should be only one returned during an edit session
        	var targetLayers = this.getVectorLayersByTypeProp("TARGET");
          	var targetLayerProps = targetLayers[0].getProperties();
          	if(targetLayerProps.hasOwnProperty("editFeatureCache")){
          		var originalEditFeature = targetLayerProps.editFeatureCache[0];
          		targetLayers[0].getSource().addFeature(originalEditFeature);
          	}
    	    
    	    this.closeEditSession();
        },
        
        
        /** 
         * Removing edit interactions (underlying edit functionality) from the map. 
         * 
         * This is separate from UI elements that might initiate an edit session.  
         * Interactions are the plumbing that performs the actual edits.
         */
        closeEditSession : function() {
        	var map = this.getMap();
        	
        	map.getProperties().editFeatures.clear();
        	
        	var existingInteractions = map.getInteractions().getArray();
      	    var drawInteraction = null;
      	    var modifyInteraction = null
      	    for(var i=0; i<existingInteractions.length; i++){
      		  var interaction = existingInteractions[i];
      		  if(interaction instanceof ol.interaction.Draw){
      			  drawInteraction = interaction;
      		  }
      		  else if(interaction instanceof ol.interaction.Modify){
      			  modifyInteraction = interaction;
      		  }
      	    };
        	
      	    this.removeInteractions([drawInteraction, modifyInteraction]);
      	    
      	    this.removeEditSessionControls();
      	    
      	    var newPointControlExists = this.getCustomControl("addNewPointControl");
      	    if(!newPointControlExists){
      	    	this.addNewPointControl();
      	    }
        	
        	map.setProperties({"gdbEditModeEnabled":false});
        },
        
        getCustomControl : function(controlName) {
        	var map = this.getMap();
        	var mapControls = map.getControls().getArray();
        	
        	for(var i=0; i<mapControls.length; i++){
        		var ctrl = mapControls[i];
        		var ctrlProps = ctrl.getProperties();
        		if(ctrlProps.hasOwnProperty("customControl")){
	        		if(ctrlProps.customControl === controlName){
	        			return ctrl;
	        		}
        		}
        	}
        	
        	return false;
        },
        
        
        moveEditSessionControls : function(direction) {
        	var map = this.getMap();
        	var mapControls = map.getControls().getArray();
        	var saveEditsCtrl = this.getCustomControl("saveEditsControl");
    		var cancelEditsCtrl = this.getCustomControl("cancelEditsControl");
    		
        	if(saveEditsCtrl !== null && cancelEditsCtrl !== null){
        		if(direction === "UP"){
        			saveEditsCtrl.element.classList.add("move-up");
        			cancelEditsCtrl.element.classList.add("move-up");
        		}
        		else if(direction === "DOWN"){
        			saveEditsCtrl.element.classList.remove("move-up");
        			cancelEditsCtrl.element.classList.remove("move-up");
        		}
        	}
        },
        
        
        removeEditSessionControls : function() {
        	var map = this.getMap();
        	var mapControls = map.getControls().getArray();
        	var saveEditsCtrl = this.getCustomControl("saveEditsControl");
    		var cancelEditsCtrl = this.getCustomControl("cancelEditsControl");
    		
        	if(saveEditsCtrl !== null && cancelEditsCtrl !== null){
        		this.removeControl(saveEditsCtrl);
        		this.removeControl(cancelEditsCtrl);
        	}
        },
        
        removeControl : function(control) {
        	var map = this.getMap();
        	map.removeControl(control);
        },
        
        zoomToFeatureExtent : function(featureJSON, workspace) {
        	var that = this;
        	var map = this.getMap();
        	
        	var callback = function(featureResponse){
	            var featureType = featureResponse.features[0].geometry.type.toLowerCase();
	            if(featureType === "multipolygon"){
	            	var featureGeom = new ol.geom.MultiPolygon(featureResponse.features[0].geometry.coordinates)
	                var featureGeomExtent = featureGeom.getExtent();
	                that.setView(featureGeomExtent, null, null, MapWidget.DATASRID);
	            }
	            else if(featureType === "point"){
	            	var featureGeom = new ol.geom.Point(featureResponse.features[0].geometry.coordinates);
	            	var featureGeomCenter = featureGeom.getCoordinates();
	            	that.setView(null, featureGeomCenter, 15, MapWidget.DATASRID);
	            }
        	}
        	
        	this.getWFSFeature(callback, featureJSON, workspace);
        },
        
        setClickHandler : function(handler) {
          var map = this.getMap();
          
          map.on('click', handler);
        },
        
        /**
         * Gets a WFS feature from a given layer
         * 
         * @param callback
         * @param featureJSON - json object defining the feature to return
         */
        getWFSFeature : function(callback, featureJSON, workspace) {
        	
        	var params = {
                    REQUEST:'GetFeature',
                    SERVICE:'WFS',
                    VERSION:'2.0.0',
                    TYPENAMES:workspace+":"+ featureJSON.layerViewName,
                    CQL_FILTER : "geoid='"+ featureJSON.geoId + "'",
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
                  
        },
        
        getFeatureInfo : function(workspace, layers, e, callback) {
          if(layers.length > 0) {
            var point = e.pixel;
            var coordinate = e.coordinate;

            var x = parseInt(point[0]);
            var y = parseInt(point[1]);
                        
            // Construct a GetFeatureInfo request URL given a point
            var size = this.getMapSize();
            var mapBbox = this.getCurrentBoundsAsString(MapWidget.MAPSRID);
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
              SRS:MapWidget.MAPSRID,              
              VERSION:'1.1.1',
              height:size.y,
              width:size.x,
              X:x,
              Y:y,
              BBOX:mapBbox,
              LAYERS:"geoprism:"+ layerStringList,
              QUERY_LAYERS:"geoprism:"+ layerStringList,
              TYPENAME:"geoprism:"+ layerStringList
              //PROPERTYNAME:"displaylabel,geoid," + layer.aggregationAttribute.toLowerCase()
            };
             
            var url = window.location.origin+"/geoserver/" + workspace +"/wms?" + $.param(params);
                  
            $.ajax({
              url: url,
              context: document.body 
            }).done(function(response) {
            
              if(response.features != null && response.features.length > 0) {
                  
                /* The response will return only 1 feature */
                var feature = response.features[0];
                var featureId = feature.id;
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
                  layerViewName : viewName,
                  aggregationMethod : aggregationMethod,
                  featureId : featureId,
                  featureDisplayName : featureDisplayName,
                  attributeValue : attributeValue,
                  geoId : geoId,
                  coordinate : coordinate,
                  featureGeom : feature.geometry,
                  layerId : layer.layerId,
                  aggregationStrategy : layer.aggregationStrategy
                };  
                  
                callback(info);
              }
            });
          }        
        },
        
        clear : function() {
          
          for (var key in this._cache) {
            if (this._cache.hasOwnProperty(key)) {
              var oLayer = this._cache[key];
              
              if (oLayer != null && oLayer.showing == true) {
                var map = this.getMap();
                map.removeLayer(oLayer);
                  
                if(oLayer.removable) {
                  delete this._cache[key];            
                }
                else {
                  oLayer.showing = false;  
                }
              }
            }
          }          
        },
        
        /**
         * Open a popup on the map
         * 
         * <private> - internal method
         */
        addOverlay : function(element, coordinate) {
          var that = this;
          var map = this.getMap();
            
          var overlay = new ol.Overlay({
            element:element,
            autoPan: true
          });
          
          map.addOverlay(overlay);
          overlay.setPosition(coordinate);
        },
        
        /**
         * Close all popups on the map
         * 
         * <public> - called externally
         */
        clearOverlays : function() {
          var map = this.getMap();
          var overlays = map.getOverlays();
          overlays.clear();
        }        
      },
      Static : {
        // Static methods
      }
    });
  
})();
  
  
