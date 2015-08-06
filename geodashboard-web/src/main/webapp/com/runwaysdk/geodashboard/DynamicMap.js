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
  
  var Component = com.runwaysdk.ui.Component;  
  var DynamicMapName = GDB.Constants.GIS_PACKAGE+'DynamicMap';
  
  /**
  * LANGUAGE
  */
  com.runwaysdk.Localize.defineLanguage(DynamicMapName, {
   "deleteLayerTooltip" : "Delete layer"
  });
    
  var LayerViewFactory = Mojo.Meta.newClass(GDB.Constants.GIS_PACKAGE+'LayerViewFactory', {
    Static : {
      /**
       * Constructs a layer object from json returned from the server.
       * The json does not type objects so we construct the appropriate type.
       * 
       * @layer - DashboardLayer representation
       */
      createLayerView : function(layer) {
          
          if(layer.layerType === "THEMATICLAYER"){
            var view = new com.runwaysdk.geodashboard.gis.persist.DashboardLayerView();
            view.setViewName(layer.viewName);
            view.setLayerId(layer.layerId);
            view.setSldName(layer.sldName);
            view.setLayerName(layer.layerName);
            view.setDisplayInLegend(layer.inLegend);
            view.setLegendXPosition(layer.legendXPosition);
            view.setLegendYPosition(layer.legendYPosition);
            view.setGroupedInLegend(layer.groupedInLegend);
            view.setActiveByDefault(true);
            view.setFeatureStrategy(layer.featureStrategy);
            
              // The $("#"+layer.layerId).length < 1 is a bit of a hack to account for the initial map load when the checkbox elements
              // may not be created yet.  The default is for all layers to be active on load so this is generally a safe assumption.
            if($("#"+layer.layerId).hasClass("checked") || $("#"+layer.layerId).length < 1){
              view.setLayerIsActive(true);
            }
            else{
              view.setLayerIsActive(false);
            }
            view.setAggregationMethod(layer.aggregationMethod);
            view.setAggregationAttribute(layer.aggregationAttribute);
            view.setMdAttribute(layer.mdAttributeId);
            view.setAttributeLabel(layer.attributeLabel);
            
            view.layerType = layer.layerType;              
            view.style = layer.styles[0];
            view.geoNodeId = layer.geoNodeId;
            view.aggregationStrategy = layer.aggregationStrategy;
            
            return view;
          }
          else if(layer.layerType === "REFERENCELAYER"){
            var uniId = layer.uniId;
            
            var view = new com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerView();
            view.setViewName(layer.viewName);
            view.setLayerId(layer.layerId);
            view.setSldName(layer.sldName);
            view.setLayerName(layer.layerName);
            view.setDisplayInLegend(layer.inLegend);
            view.setLegendXPosition(layer.legendXPosition);
            view.setLegendYPosition(layer.legendYPosition);
            view.setGroupedInLegend(layer.groupedInLegend);
            view.setActiveByDefault(false);
            view.setFeatureStrategy(layer.featureStrategy); // Reference layers should always be BASIC strategy
              
            view.layerExists = true;
              
            // The $("#"+layer.layerId).length < 1 is a bit of a hack to account for the initial map load when the checkbox elements
            // may not be created yet.  The default is for all layers to be active on load so this is generally a safe assumption.
            if($("#"+layer.layerId).hasClass("checked") || $("#"+layer.layerId).length < 1){
                view.setLayerIsActive(true);
            }
            else{
                view.setLayerIsActive(false);
            }
              
            view.layerType = layer.layerType;
            view.universalId = uniId;
              
            if(layer.styles){
              view.style = layer.styles[0]; 
            }
            else{
              view.style = null;
            }
            
            return view;
          }
          else if(layer.layerType === "REFERENCEJSON"){
            var displayName = layer.properties.uniDispLabel;  
            var uniId = layer.properties.uniId;
            var exists = layer.properties.refLayerExists;
              
            var view = new com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerView();
            view.setViewName(layer.viewName || null);
            view.setLayerId(uniId);  // IMPORTANT: additional logic depends on this being a universal id for REFERENCEJSON layers. 
            view.setSldName(layer.sldName || null);
            view.setLayerName(displayName);
            view.setDisplayInLegend(layer.inLegend || false); 
            view.setLegendXPosition(layer.legendXPosition || 0); // or default
            view.setLegendYPosition(layer.legendYPosition || 0); // or default
            view.setGroupedInLegend(layer.groupedInLegend || true); // or default
            view.setActiveByDefault(false);
            view.setFeatureStrategy("BASIC"); // Reference layers should always be BASIC strategy
              
            if(exists){
              view.layerExists = true;
            }
            else{
              view.layerExists = false;
            }
              
            view.universalId = uniId;
            view.layerType = "REFERENCEJSON";
              
            if(layer.styles){
              view.style = layer.styles[0] || null; 
            }
            else{
              view.style = null;
            }
            
            return view;
          }          
        },
   } 
  });
  
  var DynamicMap = Mojo.Meta.newClass(GDB.Constants.GIS_PACKAGE+'DynamicMap', {
    
    Extends : Component,
    
    Constants : {
      BASE_LAYER_CONTAINER : 'baseLayerContainer',
      OVERLAY_LAYER_CONTAINER : 'overlayLayerContainer',
      REFERENCE_LAYER_CONTAINER : 'refLayerContainer',
      GEOCODE : 'geocode',
      GEOCODE_LABEL : 'geocodeLabel',
      LAYER_MODAL : '#modal01',
      DASHBOARD_MODAL : "#dashboardModal01",
      TO_DATE : 'to-field',
      FROM_DATE : 'from-field',
      SRID : "EPSG:4326"
    },
    
    Instance : {
      
      /**
       * Constructor
       * @param mapDivId DOM id of the map div.
       */
      initialize : function(config){
        this.$initialize();
        
        var overlayLayerContainer = $('#'+DynamicMap.OVERLAY_LAYER_CONTAINER);
        var referenceLayerContainer = $('#'+DynamicMap.REFERENCE_LAYER_CONTAINER);
        
        this._googleEnabled = Mojo.Util.isObject(Mojo.GLOBAL.google);
        
        this._config = config;
        this._mapDivId = config.mapDivId;
        this._mapId = config.mapId;
        this._dashboardId = config.dashboardId;
        this._workspace = config.workspace;
        this._aggregationMap = config.aggregationMap;
        this._editable = config.editable;
        
        // Default criteria for filtering
        this._criteria = config.criteria;
        this._currGeoId = '';
        
        // Filter trees
        this._filterTrees = [];
        
        // Number localization setup
        this._parser = Globalize.numberParser();
        this._formatter = Globalize.numberFormatter();
        
        this._defaultOverlay = null;
        this._currentOverlay = null;
        this._mapFactory = null;
        
        this._layerCache = new com.runwaysdk.structure.LinkedHashMap();
        this._refLayerCache = new com.runwaysdk.structure.LinkedHashMap();
        
        // The current base map (only one at a time is allowed)
        this._defaultBase = null;
        this._currentBase = null;
        this._baseLayers = new com.runwaysdk.structure.HashMap();
        
        this._suggestionCoords = new com.runwaysdk.structure.HashMap();      
        this._autocomplete = null;
        this._responseCallback = null;
        this._bBox = null;
        this._reportPanelState = 'min';
        
        var overlayBound = Mojo.Util.bind(this, this._overlayEditHandler);
        var refBound = Mojo.Util.bind(this, this._referenceEditHandler);
        overlayLayerContainer.on('click', 'a', overlayBound);
        referenceLayerContainer.on('click', 'a', refBound);
        
        var dashboardBound = Mojo.Util.bind(this, this._dashboardClickHandler);
        $(".gdb-dashboard").on("click", dashboardBound); 
        
        // Handler for the clone dashboard button
        $("#clone-dashboard").on("click", Mojo.Util.bind(this, this._dashboardCloneHandler));        
        $("#dashboard-options-btn").on("click", Mojo.Util.bind(this, this._dashboardEditHandler));  
        
        this._DashboardMapController = com.runwaysdk.geodashboard.gis.persist.DashboardMapController;
        this._ReportController = com.runwaysdk.geodashboard.report.ReportItemController;
        
        overlayLayerContainer.sortable({
          update: Mojo.Util.bind(this, this._overlayLayerSortUpdate)
        });
        overlayLayerContainer.disableSelection();      
      },
      
      getLayer : function(layerId) {
        return this._layerCache.get(layerId);        
      },
      
      getAggregationMap : function() {
        return this._aggregationMap;  
      },
      
      getParser : function() {
        return this._parser;
      },
      
      getFormatter : function() {
        return this._formatter;
      },
      
      /**
       * Retrieves new data from the server and then refreshes everything on the map with the new data.
       * 
       */
      fullRefresh : function() {
        var that = this;
        
        this._renderMap();
        
        var clientRequest = new Mojo.ClientRequest({
          onSuccess : function(json, calledObj, response) {
            var jsonObj = Mojo.Util.toObject(json);
             
            that._updateCacheFromJSONResponse(jsonObj);
            that._configureMap();
            
            that._renderBaseLayers();
            that._renderUserLayers();
            
            that._drawLegendItems();
          },
          onFailure : function(e) {
            that.handleException(e);
          }
        });
        
        var conditions = this._getConditionsFromCriteria(this._criteria);
          
        com.runwaysdk.geodashboard.gis.persist.DashboardMap.updateConditions(clientRequest, this._mapId, conditions);
        
        this._renderReport('', '', this._criteria);
      },
      
      handleLayerEvent : function(jsonObj) {
        this._updateCacheFromJSONResponse(jsonObj);
        this._drawUserLayersHTML();
        this._mapFactory.removeClickPopup();
        this._addUserLayersToMap(true);    
      },
      
      handleReferenceLayerEvent : function(jsonObj) {
        this._updateCacheFromJSONResponse(jsonObj);
        this._drawReferenceLayersHTML();
        this._addUserLayersToMap(true);    
      },
      
      
      /**
       * Requests all map data again from the server (bounds, persisted layers, etc...) and updates our internal caches.
       * 
       * @fnSuccess - a function to be run on success of request for backend map configuration
       * 
       */
      _updateCachedData : function(fnSuccess) {
        var that = this;
        
        com.runwaysdk.geodashboard.gis.persist.DashboardMap.getMapJSON(
          new Mojo.ClientRequest({
            onSuccess : function(json, mapObj, response){
              var jsonObj = Mojo.Util.toObject(json);
              
              that._updateCacheFromJSONResponse(jsonObj);
              
              fnSuccess();
              
              // TODO : Push this somewhere as a default handler.
              that.handleMessages(response);
            },
            onFailure : function(e){
              that.handleException(e);
            }
          })
          , this._mapId, '{testKey:"TestValue"}');
      },
      
      /**
       * Creates "view" objects for each layer defined by the dashboardlayerview.java class
       * and builds the initial layer cache
       * 
       * @json - json obj returned from the server that contains map data and properties for map construction
       * 
       */
      _updateCacheFromJSONResponse : function(json) {
        // Create DashboardLayerView and/or DashboardReferenceLayerView objects to be used throughout the map life cycle 
        // to provide more formal structure for layer objects.
        
        if(json.layers){
        // Build thematic layer objects and populate the cache
          for (var i = 0; i < json.layers.length; ++i) {
            var layer = json.layers[i];
            
            var view = LayerViewFactory.createLayerView(layer);
            
            var oldLayer = this._layerCache.get(view.getLayerId());
            if (oldLayer != null) {
              view.wmsLayerObj = oldLayer.wmsLayerObj;
            }
            this._layerCache.put(view.getLayerId(), view);        
          }
        }
        
        // Build reference layer objects and populate the cache
        if(json.refLayers){
          for (var r = 0; r < json.refLayers.length; ++r) {
            var refLayer = json.refLayers[r];
            
            // Construct the layer view objects
            var refView = LayerViewFactory.createLayerView(refLayer);
            var oldLayer = this._refLayerCache.get(refView.universalId);
            if (oldLayer != null) {
              refView.wmsLayerObj = oldLayer.wmsLayerObj;
            }
            
            this._refLayerCache.put(refView.universalId, refView);
          }
        }
        
        if (json.bbox != null) {
            this._bBox = json.bbox;
        }
      },
                 
      /**
       * Creates a new map. If one already exists the existing one will be cleaned up and removed.
       * 
       */
      _renderMap : function() {
        var center = null;
        var zoomLevel = null;
        var enableClickEvents = true;
        this._mapFactory = new com.runwaysdk.geodashboard.gis.OpenLayersMap(this._mapDivId, center, zoomLevel, enableClickEvents, this);

        if (this._mapFactory.getMap() != null) {
            $('#'+DynamicMap.BASE_LAYER_CONTAINER).html('');
        }
      },
      
      /**
       * Creates a new BIRT report. If one already exists the existing one will be cleaned up and removed.
       * 
       * @gedId
       * @criteria
       * 
       */
      _renderReport : function(layerId, geoId, criteria) {
        if($( "#report-viewport" ).length > 0) {
          var request = new com.runwaysdk.geodashboard.StandbyClientRequest({
            that : this,
            onSuccess : function(html){
              this.that._displayReport(html);
            },
            onFailure : function(e){
              this.that.handleException(e);
            }
          }, $( "#report-viewport" )[0]);
        
          // Get the width of the reporting div, make sure to remove some pixels because of
          // the side bar and some padding. convert px to pt
          var widthPt = Math.round(($('#report-content').width() - 20) * 72 / 96);
          var heightPt = Math.round($('#report-content').height() * 72 / 96);
        
          var configuration = {};
          configuration.parameters = [];
          configuration.parameters.push({'name' : 'layerId', 'value' : layerId});
          configuration.parameters.push({'name' : 'category', 'value' : geoId});
          configuration.parameters.push({'name' : 'criteria', 'value' : JSON.stringify(criteria)});
          configuration.parameters.push({'name' : 'width', 'value' : widthPt});
          configuration.parameters.push({'name' : 'height', 'value' : heightPt});
        
          this._ReportController.run(request, this._dashboardId, JSON.stringify(configuration));    
        }
      },
      
      _exportMap : function() {
        
        var mapId = this._mapId;
        var outFileName = "GeoDashboard_Map";
        var outFileFormat = "png";
        var mapBounds = {};
        var mapExtent = this._mapFactory.getCurrentBounds(DynamicMap.SRID);
        mapBounds.left = mapExtent._southWest.lng;
        mapBounds.bottom = mapExtent._southWest.lat;
        mapBounds.right = mapExtent._northEast.lng;
        mapBounds.top = mapExtent._northEast.lat;
        mapBoundsStr = JSON.stringify(mapBounds);
      
        var mapSize = {};
        mapSize.width = $("#mapDivId").width();
        mapSize.height = $("#mapDivId").height();
        mapSizeStr = JSON.stringify(mapSize);
      
      
        var url = 'com.runwaysdk.geodashboard.gis.persist.DashboardMapController.exportMap.mojo';
        url += '?' + encodeURIComponent("mapId") + "=" + encodeURIComponent(mapId);          
        url += '&' + encodeURIComponent("outFileName") + "=" + encodeURIComponent(outFileName);   
        url += '&' + encodeURIComponent("outFileFormat") + "=" + encodeURIComponent(outFileFormat);  
        url += '&' + encodeURIComponent("mapBounds") + "=" + encodeURIComponent(mapBoundsStr);  
        url += '&' + encodeURIComponent("mapSize") + "=" + encodeURIComponent(mapSizeStr);  
          
        window.location.href = url;        
      },
      
      _exportReport : function(geoId, criteria, format) {
        
        var that = this;
        
        var request = new Mojo.ClientRequest({
          onSuccess : function (result) {
          
            if(result) {            
              var configuration = {};
              configuration.parameters = [];
              configuration.parameters.push({'name' : 'category', 'value' : geoId});
              configuration.parameters.push({'name' : 'criteria', 'value' : JSON.stringify(criteria)});
              configuration.parameters.push({'name' : 'format', 'value' : format});
                
              var url = 'com.runwaysdk.geodashboard.report.ReportItemController.run.mojo';
              url += '?' + encodeURIComponent("report") + "=" + encodeURIComponent(that._dashboardId);          
              url += '&' + encodeURIComponent("configuration") + "=" + encodeURIComponent(JSON.stringify(configuration));          
                
              window.location.href = url;
            }
            else {
              var msg = com.runwaysdk.Localize.localize("dashboard", "MissingReport");                    
              
              that._renderMessage(msg);
            }
          },
          onFailure : function (exception) {
            that.handleException(exception)
          }
          
        });
        
        com.runwaysdk.geodashboard.Dashboard.hasReport(request, this._dashboardId);
        
      },
      
      /**
       * Render the analytics report on screen
       * 
       * @html 
       */
      _displayReport : function(html) {
        $( "#report-content" ).html(html);
      },      
        
      
      /**
       * Set initial state of the map
       * 
       */
      _configureMap : function() {
    	  if(this._bBox.length === 2){
    		  this._mapFactory.setView(null, this._bBox, 5);
    	  }
    	  else if(this._bBox.length === 4){
    		  this._mapFactory.setView(this._bBox, null, null);
    	  }
      },
      
      
      /**
       * Adds layer objects to the map and builds the base layer checkboxes
       * 
       */
      _renderBaseLayers : function() {
        this._baseLayers.clear();
        
        try {
          var baseLayers = this._mapFactory.createBaseLayers();                                
          this._renderBaseLayerSwitcher(baseLayers);
        }
        catch (ex) {
          // If we don't have internet, rendering the base layer will fail.
        }
      },
      
      
      /**
       * Redraws the HTML representing the user-defined layers and adds the layers to the map.
       */
      _renderUserLayers : function() {
        this._drawUserLayersHTML();
        this._drawReferenceLayersHTML();
        this._addUserLayersToMap(true);
      },
      
      
      /**
       * Legend instance object
       * 
       * @larerId - Id of the layer the legend represents
       * @displayName - Label for the layer to be added to the legend
       * @geoserverName - name of the geoserver service for the layer
       * @legendXPosition - css based 'left' property
       * @legendYPosition - css based 'top' property
       * @groupedInLegend - boolean indicating if the legend item is grouped in the main legend or floating
       * @featureStrategy - strategy descriptor used to define the strategy for mapping layers (BASIC, BUBBLE, GRADIENT, CATEGORY)
       * 
       */
      _Legend : function(layer, displayName, geoserverName, legendXPosition, legendYPosition, groupedInLegend, featureStrategy, showFeatureLabels) {
        this.layerId = layer.getLayerId();
        this.legendId = "legend_" + this.layerId;
        this.displayName = displayName;
        this.geoserverName = geoserverName;
        this.legendXPosition = legendXPosition;
        this.legendYPosition = legendYPosition;
        this.groupedInLegend = groupedInLegend;
        this.featureStrategy = featureStrategy;
        this.showFeatureLabels = showFeatureLabels;
        this.create = function(){
            var src = window.location.origin; 
            src += '/geoserver/wms?REQUEST=GetLegendGraphic' + '&amp;';
            src += 'VERSION=1.0.0' + '&amp;';
            src += 'FORMAT=image/png&amp;WIDTH=25&amp;HEIGHT=25' + '&amp;';
            src += '&TRANSPARENT=true&LEGEND_OPTIONS=fontName:Arial;fontAntiAliasing:true;fontColor:0xececec;fontSize:11;fontStyle:bold;';
            
            // forcing labels for gradient for instances where only one feature is mapped which geoserver hides labels by default
            if(this.showFeatureLabels){
              src += 'forceLabels:on;';
            }
            src += '&amp;';
            src += 'LAYER='+ this.geoserverName;
            
            if(this.groupedInLegend){
                // Remove any old grouped legend items before creating new updated items
                $(".legend-item[data-parentlayerid='"+this.layerId+"']").remove();
                
                var html = '';
                if(layer.layerType === "REFERENCELAYER"){
                    html += '<li class="legend-item legend-grouped" data-parentLayerId="'+this.layerId+'" data-parentLayerType="'+layer.layerType+'" data-parentUniversalId="'+layer.universalId+'">';
                }
                else{
                  html += '<li class="legend-item legend-grouped" data-parentLayerId="'+this.layerId+'" data-parentLayerType="'+layer.layerType+'">';
                }
                html += '<img class="legend-image" src="'+ src +'" alt="">'+ this.displayName;
                html += '</li>';
              
                $("#legend-list-group").append(html);  
              }
              else
              {
                  // remove associated legend
                  $("#"+this.legendId).remove();
                  
                  var html = '';              
                  if(layer.layerType === "REFERENCELAYER"){
                      html += '<div class="info-box legend-container legend-snapable" id="'+ this.legendId +'" data-parentLayerId="'+ this.layerId +'" data-parentLayerType="'+layer.layerType+'" data-parentUniversalid="'+layer.universalId+'" style="top:'+ this.legendYPosition +'px; left:'+ this.legendXPosition +'px;">';
                  }
                  else{
                      html += '<div class="info-box legend-container legend-snapable" id="'+ this.legendId +'" data-parentLayerId="'+ this.layerId +'" data-parentLayerType="'+layer.layerType+'" style="top:'+ this.legendYPosition +'px; left:'+ this.legendXPosition +'px;">';
                  }
                  
                  html += '<div id="legend-items-container"><ul id="legend-list">';
                   if(layer.layerType === "REFERENCELAYER"){
                       html += '<li class="legend-item" data-parentLayerId="'+this.layerId+'" data-parentLayerType="'+layer.layerType+'" data-parentUniversalId="'+layer.universalId+'">';
                   }
                   else{
                     html += '<li class="legend-item" data-parentLayerId="'+this.layerId+'" data-parentLayerType="'+layer.layerType+'">';
                   }
                  html += '<img class="legend-image" src="'+ src +'" alt="">'+ this.displayName;
                  html += '</li>';
                  html += '</ul></div></div>';
                  
                  $(".pageContent").append(html);  
            }
        };
        this.show = function(){
          var containsVisibleElement = false;
          $.each($("#legend-list-group li"), function(k,v){
            if($(v).is(":visible"))
            {
              containsVisibleElement = true;
            }
          });
          
          if(this.groupedInLegend){
            $('*[data-parentlayerid="'+this.layerId+'"]').show();
          }
          else{
            $("#"+this.legendId).show();
          }
        };
        this.hide = function(){
          if(this.groupedInLegend){
            $('*[data-parentlayerid="'+this.layerId+'"]').hide();
          }
          else{
            $("#"+this.legendId).hide();
          }
        };
        this.unGroup = function(li){
            var legendPos = $("#legend-list-group").position();
            var legendXPosition = legendPos.left;
            var legendYPosition = legendPos.top;
            var liYPosition = legendYPosition;
            var liXPosition = legendXPosition + 50;
              
            html = '';
            if(layer.layerType === "REFERENCELAYER"){
                html += '<div class="info-box legend-container legend-snapable" id="'+ this.legendId +'" data-parentLayerId="'+ this.layerId +'" data-parentLayerType="'+layer.layerType+'" data-parentUniversalid="'+layer.universalId+'" style="top:'+liYPosition+'px; left:'+liXPosition+'px;">';
            }
            else{
                html += '<div class="info-box legend-container legend-snapable" id="'+ this.legendId +'" data-parentLayerId="'+ this.layerId +'" data-parentLayerType="'+layer.layerType+'" style="top:'+liYPosition+'px; left:'+liXPosition+'px;">';
            }
            html += '<div id="legend-items-container"><ul id="legend-list">';
            html += '</ul></div></div>';
              
              $(".pageContent").append(html);
              $("#"+this.legendId).children().children().append(li);
              
              li.removeClass("legend-grouped");
              this.groupedInLegend = false;
        };
        this.group = function(draggedElement){
            var draggedListItem = draggedElement.children().children().children();
            var draggedLegendContainer = draggedElement;
          
            draggedListItem.appendTo("#legend-list-group");
            draggedListItem.addClass("legend-grouped");
            this.groupedInLegend = true;
            draggedLegendContainer.remove();
            
            // if the page was rendered with no grouped legends (but any floating) the bootstrap will not 
            // assign the 'in' or 'collapse' classes to the div.  So we simulate the click event to open the 
            // panel which results in bootstrap adding the 'in' class to #collapse-legend
            if(!$("#collapse-legend").hasClass("in") && !$("#collapse-legend").hasClass("collapse") ){
              $("#legend-opener-button").click();
            }
            //else if the div is collapsed open it 
            else if($("#collapse-legend").hasClass("collapse")){
              $("#legend-opener-button").click();
            }
            
        };
      },
      
      
      _constructLegendItem : function(layer, legendDragStopDragBound) {
          var displayInLegend = layer.getDisplayInLegend();
          //TODO: these if checks for layer types are out dated
          if(displayInLegend)
          {
              var layerId = layer.getLayerId();
            if(layer.layerType === "REFERENCELAYER"){
              layerId = layer.universalId;
            }
            else{
              layerId = layer.getLayerId();
            }
              var displayName = layer.getLayerName() || "N/A";
              var geoserverName = this._workspace + ":" + layer.getViewName();
              var legendXPosition = layer.getLegendXPosition();
              var legendYPosition = layer.getLegendYPosition();
              var groupedInLegend = layer.getGroupedInLegend();
              var featureStrategy = layer.getFeatureStrategy();
              if(featureStrategy === "BASIC"){
                var showFeatureLabels = false;
              }
             else if(featureStrategy === "BUBBLE" && layer.attributeType === "BASIC"){
                // The label should be hidden when mapping bubbles against a text or term attribute.
                var showFeatureLabels = false;
              }
              else if(featureStrategy === "BUBBLE" && layer.style.bubbleContinuousSize && layer.attributeType !== "BASIC"){
                // The label should be displayed when mapping continuous size bubbles against anything other than a text or term attribute.
                var showFeatureLabels = true;
              }
              else{
                var showFeatureLabels = true;
              }
              
              var legendObj = new this._Legend(
                  layer, 
                  displayName, 
                  geoserverName, 
                  legendXPosition, 
                  legendYPosition, 
                  groupedInLegend,
                  featureStrategy,
                  showFeatureLabels
                  );
              
              // render the legend
              legendObj.create();
              
              // add legend object to the layer
              layer.layerLegend = legendObj;
              
              // Attache draggable event listener to the new element
              $("#"+legendObj.legendId).draggable({
                   containment: "body", 
                   snap: true, 
                   snap: ".legend-snapable", 
                   snapMode: "outer", 
                   snapTolerance: 5,
                   stack: ".legend-container"
              });
            $("#"+legendObj.legendId).on('dragstop', legendDragStopDragBound); 
            
            // hide the legend if the layer is un-checked
            // originally implemented to handle legends when filters are applied with un-checked layers
            if(layer.getLayerIsActive() === false){
              legendObj.hide();
            }
          }
      },
      
      
      /**
       * Draws legend entries based on the layer cache
       * 
       */
      _drawLegendItems : function() {
        var that = this;
        var layers = this._layerCache.values();
        var refLayers = this._refLayerCache.values();
        
          // Click and drag handlers  
          var legendDragStopDragBound = Mojo.Util.bind(this, this._legendDragStopHandler);
          $(".legend-container").on('dragstop', legendDragStopDragBound); 
        
        // Clear out any old legends (when map is updated)
        $(".legend-container").remove();    
        $(".legend-item.legend-grouped").remove();
        
        //
        // Add all legends to the map
        //
          for(var i = layers.length-1; i >= 0; i--){
            var layer = layers[i];
            this._constructLegendItem(layer, legendDragStopDragBound);
          }
          
          for(var r = refLayers.length-1; r >= 0; r--){
              var layer = refLayers[r];
              this._constructLegendItem(layer, legendDragStopDragBound);
          }
          
          //
          // Control for adding un-grouped legend items to the legend items container
          //
          $("#legend-collapse-container").droppable({
              tolerance: "touch",
              drop: function( event, ui ) {
                var groupedInLegend = true;
                var relatedLayerId = $(ui.draggable).data('parentlayerid');
                var parentLayer;
                if($(ui.draggable).data("parentlayertype") === "THEMATICLAYER"){
                  parentLayer = that._layerCache.get(relatedLayerId);
              }
              else if($(ui.draggable).data("parentlayertype") === "REFERENCELAYER"){
                parentLayer = that._refLayerCache.get($(ui.draggable).data("parentuniversalid"));
              }
                var x = 0;
                var y = 0;
                parentLayer.setLegendXPosition(x);
                parentLayer.setLegendYPosition(y);
                parentLayer.setGroupedInLegend(groupedInLegend);
                
                var clientRequest = new Mojo.ClientRequest({
                  onSuccess : function() {
                    // No action needed
                  },
                  onFailure : function(e) {
                    that.handleException(e);
                  }
                });
                    
                // Persist legend position to the db
                com.runwaysdk.geodashboard.gis.persist.DashboardLayer.updateLegend(clientRequest, relatedLayerId, x, y, groupedInLegend);
                parentLayer.layerLegend.group(ui.draggable);
              }
          });
          
          //
          // Control for removing (un grouping) grouped legend items from the main legend item container
          //
          $("#legend-list-group").on("click", ".legend-item", function(e) {
            var li = $(this);
            var parentLayerId = li.data("parentlayerid");
            var legendId = "legend_" + parentLayerId;
            var parentLayer;
            
            // prevent legend containers from being created twice. 
            if($("#"+legendId).length === 0){
              if(li.data("parentlayertype") === "THEMATICLAYER"){
                parentLayer = that._layerCache.get(parentLayerId);
              }
              else if(li.data("parentlayertype") === "REFERENCELAYER"){
                parentLayer = that._refLayerCache.get(li.data("parentuniversalid"));
              }
                parentLayer.layerLegend.unGroup(li);
                
                // Attache draggable event listener to the new element
                $("#"+legendId).draggable({
                    containment: "body", 
                    snap: true, 
                    snap: ".legend-snapable", 
                    snapMode: "outer", 
                    snapTolerance: 5,
                    stack: ".legend-container"
                });
                $("#"+legendId).on('dragstop', legendDragStopDragBound); 
            }
          });
      },
      
      
      /**
       * Build HTML for reference layers
       * 
       * @htmlInfo
       * 
       */
      _drawReferenceLayersHTML : function() {
        var container = $('#'+DynamicMap.REFERENCE_LAYER_CONTAINER);
        var onCheckHandler = Mojo.Util.bind(this, this._toggleReferenceLayer);
        var layers = this._refLayerCache.values();
        var html = '';
        
        // 1) Create the HTML for the layer.
        for(var i = layers.length-1; i >= 0; i--){
          var layer = layers[i];
          var displayName = layer.getLayerName();
          
          //
          // IMPORTANT: We are setting id to the layer id of the server generated DashboardReferenceLayer when possible.
          // The layer id will later be used to remove the layer from the ui and server when the layer is enabled in the ui. 
          // The universalId is used as the key and general uniquie id for the _refLayerCache hashmap. This is done because
          // when reference layers are disabled there is no valid layer id.  Using the universalId ensures consistency.
          //
          
          var id = layer.getLayerId();
            
          html += '<div class="row-form">';
          html += '<div id=' + id + ' data-universalid="'+layer.universalId+'"/>';
          html += '<label for="'+id+'">'+displayName+'</label>';
          html += '<div class="cell">';
          if(layer.layerExists && this._editable){
            html += '<a href="#" data-id="'+id+'" data-universalid="'+layer.universalId+'" class="fa fa-times ico-remove" title="'+com.runwaysdk.Localize.localize("dashboardViewer", "deleteLayerTooltip")+'"></a>';
            html += '<a href="#" data-id="'+id+'" data-universalid="'+layer.universalId+'" class="fa fa-pencil ico-edit" title="'+com.runwaysdk.Localize.localize("dashboardViewer", "editLayerTooltip")+'"></a>';
            html += '<a data-universalid="'+layer.universalId+'" class="fa fa-plus referenceLayer ico-enable" style="display:none;" title="'+com.runwaysdk.Localize.localize("dashboardViewer", "refLayerEnableTooltip")+'" ></a> ';
          }
          else if(this._editable) {              
            html += '<a href="#" data-id="'+id+'" data-universalid="'+layer.universalId+'" class="fa fa-times ico-remove" style="display:none;" title="'+com.runwaysdk.Localize.localize("dashboardViewer", "deleteLayerTooltip")+'"></a>';
            html += '<a href="#" data-id="'+id+'" data-universalid="'+layer.universalId+'" class="fa fa-pencil ico-edit title="'+com.runwaysdk.Localize.localize("dashboardViewer", "editLayerTooltip")+'"" style="display:none;"></a>';
            html += '<a data-universalid="'+layer.universalId+'" class="fa fa-plus referenceLayer ico-enable" title="'+com.runwaysdk.Localize.localize("dashboardViewer", "refLayerEnableTooltip")+'" ></a> ';
          }
          html += '</div>';
          html += '</div>';
        }
        
        // 2) Render the HTML we just generated.
        container.html(html);
        
        // 3) Add checkboxes and register click events
        for(var i = 0; i < layers.length; i++){
          var layer = layers[i];
          var chexd = false;
          var layerId = layer.getLayerId();
          var display = "none";
          
          // If the layer object is active (visible on the map) set the checkbox to checked
          if(layer.getLayerIsActive()){
            //var chexd = layer.checked === false || layer.checked === true ? layer.checked : true;
            chexd = true;
          }
          
          // If this reference layer is activated and mappable (exists) display the checkbox
          if(layer.layerExists){
            display = "";
          }
          
          com.runwaysdk.event.Registry.getInstance().removeAllEventListeners(layerId);
          var checkbox = this.getFactory().newCheckBox({el: "#"+layerId, data: {runwayId: layerId}, checked: chexd, classes: ["check"]});
          checkbox.addOnCheckListener(onCheckHandler);
          checkbox._node.style.display = display;
          checkbox.render();
        }        
        
        // TODO: Handle legends for saved/styled reference layers
        this._drawLegendItems();
      },
      
      
      /**
       * Build HTML for user defined overlays
       * 
       * @htmlInfo
       * 
       */
      _drawUserLayersHTML : function(htmlInfo) {
        var container = $('#'+DynamicMap.OVERLAY_LAYER_CONTAINER);
        var onCheckHandler = Mojo.Util.bind(this, this._toggleOverlayLayer);
        var layers = this._layerCache.values();
        var html = '';
        
        // 1) Create the HTML for the layer overlay.
        for(var i = layers.length-1; i >= 0; i--){
          var layer = layers[i];
          
          var displayName = layer.getLayerName() || "N/A";
          
          html += '<div class="row-form">';
          html += "<div id=" + layer.getLayerId() + "/>";
          html += '<label for="'+layer.getLayerId()+'">'+displayName+'</label>';
          
          if(this._editable) {
            //html += '<div class="cell"><a href="#" data-id="'+layer.getLayerId()+'" class="ico-remove" title="'+com.runwaysdk.Localize.localize("dashboardViewer", "deleteLayerTooltip")+'">remove</a>';
            //html += '<a href="#" data-id="'+layer.getLayerId()+'" class="ico-edit" title="'+com.runwaysdk.Localize.localize("dashboardViewer", "editLayerTooltip")+'">edit</a>';
            //html += '<a href="#" data-id="'+layer.getLayerId()+'" class="ico-control">control</a></div>';
            
              html += '<div class="cell"><a href="#" data-id="'+layer.getLayerId()+'" class="fa fa-times ico-remove" title="'+com.runwaysdk.Localize.localize("dashboardViewer", "deleteLayerTooltip")+'"></a>';
              html += '<a href="#" data-id="'+layer.getLayerId()+'" class="fa fa-pencil ico-edit" title="'+com.runwaysdk.Localize.localize("dashboardViewer", "editLayerTooltip")+'"></a>';
              html += '</div>';
          }
          
          html += '</div>';
        }
        
        // 2) Render the HTML we just generated.
        container.html(html);
        
        // 3) Add checkboxes and register click events
        for(var i = 0; i < layers.length; i++){
          var layer = layers[i];
          
          var chexd = layer.checked === false || layer.checked === true ? layer.checked : layer.getLayerIsActive();  //layerIsActive should be false if unchecked
          
          com.runwaysdk.event.Registry.getInstance().removeAllEventListeners(layer.getLayerId());
          var checkbox = this.getFactory().newCheckBox({el: "#"+layer.getLayerId(), data: {runwayId: layer.getLayerId()}, checked: chexd, classes: ["check"]});
          checkbox.addOnCheckListener(onCheckHandler);
          checkbox.render();
        }        
        
        // open the overlay panel if there are layers and it is collapsed
        if(layers.length > 0 && !$("#collapse-overlay").hasClass("in")){
          $("#overlay-opener-button").click();
        }
        
        this._drawLegendItems();
      },
      
      
      /**
       * Adds all layers in the layerCache to the map, in the proper ordering.
       * 
       * @param boolean removeExisting Optional, if unspecified all existing layers will be removed first. If set to false, only layers that are not
       * already added to the map will be added.
       */
      _addUserLayersToMap : function(removeExisting) {
        var layers = this._layerCache.values();
        var refLayers = this._refLayerCache.values();
        
        // Create reference layers before user layers to control stacking order.
        // This could be done in a better way by setting a type and potentially z-index on the layers
        this._mapFactory.createReferenceLayers(refLayers, this._workspace, removeExisting);
        this._mapFactory.createUserLayers(layers, this._workspace, removeExisting);
      },
      
      
      /**
       * Callback handler when layers are reordered which persists index order of layers to db and
       * 
       * @param event
       * @param ui
       */
      _overlayLayerSortUpdate : function(event, ui) {
        var that = this;
        
        // Calculate an array of layer ids
        var layerIds = [];
        var layers = $("#overlayLayerContainer").find(".com-runwaysdk-ui-factory-runway-checkbox-CheckBox");
        for (var i = 0; i < layers.length; ++i) {
          var layer = layers[i];
          layerIds.push(layer.id);
        }
        layerIds.reverse();
        
        var clientRequest = new Mojo.ClientRequest({
          onSuccess : function(json) {
            // Update the layer cache with the new layer ordering
            var reorderedLayerCache = new com.runwaysdk.structure.LinkedHashMap();
            for (var i = 0; i < layerIds.length; ++i) {
              reorderedLayerCache.put(layerIds[i], that._layerCache.get(layerIds[i]));
            }
            that._layerCache = reorderedLayerCache;
            
            // At this point, the layers are already ordered properly in the HTML. All we need to do is inform the map of the new ordering.
            that._addUserLayersToMap(true);
          },
          onFailure : function(e) {
            that.handleException(e);
            
            // The server failed to reorder the layers. We need to redraw the HTML to reset the layer ordering, but we don't need to update the map because that ordering is still correct.
            that._drawUserLayersHTML();
          }
        });
        
        com.runwaysdk.geodashboard.gis.persist.DashboardMap.orderLayers(clientRequest, this._mapId, layerIds);
      },
      
      
      /**
       * Handler for when the user clicks on a dashboard on the dropdown.
       * 
       * @e
       */
      _dashboardClickHandler : function(e) {

        var el = $(e.currentTarget);
        
        var dashboardId = el[0].id;
        
        window.location = "?dashboard=" + dashboardId;
      },
      
      
      /**
       * 
       * @e
       */
      _dashboardCloneHandler : function(e) {
        e.preventDefault();      
          
        var dashboardForm = new com.runwaysdk.geodashboard.gis.DashboardForm(this, this._dashboardId);
        dashboardForm.open();
      },
      
      /**
       * 
       * @e
       */
      _dashboardEditHandler : function(e) {
        e.preventDefault();      
          
        var dashboardForm = new com.runwaysdk.geodashboard.gis.DashboardForm(this, this._dashboardId);
        dashboardForm.edit(this._dashboardId);
        
      },
      
      _renderMessage : function(message) {
        var dialog = com.runwaysdk.ui.Manager.getFactory().newDialog(com.runwaysdk.Localize.get("rError", "Error"), {modal: true});
        dialog.appendContent(message);
        dialog.addButton(com.runwaysdk.Localize.get("rOk", "Ok"), function(){
          dialog.close();
        }, null, {primary: true});
        dialog.setStyle("z-index", 2001);
        dialog.render();            
      },
      
      /**
       * Opens the layer edit form for existing layers  
       * 
       * @param e
       */
      _overlayEditHandler : function(e){       
        var that = this;     
        var el = $(e.currentTarget);
        
        if(el.hasClass('ico-edit')) {
          // edit the layer
          var id = el.data('id');
          
          var form = new com.runwaysdk.geodashboard.gis.ThematicLayerForm(this, this._mapId);
          form.edit(id);
        }
        else if(el.hasClass('ico-remove')){         
          // delete the layer
          var id = el.data('id');
          com.runwaysdk.Facade.deleteEntity(new Mojo.ClientRequest({
            onSuccess : function(){
              that._removeLayer(el, id);
            },
            onFailure : function(e){
              that.handleException(e);
            }
          }), id);
        }
      },
      
      
      /**
       * Opens the layer edit form for reference layers  
       * 
       * @param e
       */
      _referenceEditHandler : function(e){       
        var that = this;     
        var el = $(e.currentTarget);
        
        if(el.hasClass('ico-edit')) {
          var id = el.data('id');  
            
          var form = new com.runwaysdk.geodashboard.gis.ReferenceLayerForm(this, this._mapId);
          form.edit(id);
        }
        else if(el.hasClass('ico-remove')){         
          // delete the layer
          var layerId = el.data('id');
          var universalId = el.data("universalid");
          
          com.runwaysdk.Facade.deleteEntity(new Mojo.ClientRequest({
            onSuccess : function(){
              that._removeLayer(el, universalId);
            },
            onFailure : function(e){
              that.handleException(e);
            }
          }), layerId);
        }
        else if(el.hasClass('ico-enable')) {
            // enable this reference layer
            
            this._openReferenceLayerForAttribute(e);
         }
      },
      
      
      /*
       * Format dates
       * 
       */
      _formatDate : function(value) {
          
          if(this._dateFormatter == null) {
            this._dateFormatter = Globalize.dateFormatter({ date: "short" });                         
          }
          
          if(value != null) {
            return this._dateFormatter(value);          
          }
          
          return null;
        },
        
        /*
         * Format date/times
         * 
         */
        _formatDateTime : function(value) {
          
          if(this._dateTimeFormatter == null) {
            this._dateTimeFormatter = Globalize.dateFormatter({ dateTime: "short" });                         
          }
          
          if(value != null) {        
            return this._dateTimeFormatter(value);
          }
          
          return null;          
        },
        
        /*
         * Format time
         * 
         */
        _formatTime : function(value) {
          
          if(this._timeFormatter == null) {
            this._timeFormatter = Globalize.dateFormatter({ time: "short" });                         
          }
          
          if(value != null) {                
            return this._timeFormatter(value);
          }
        
          return null;
        },

      
      /**
       * Removes the Layer with the given object id (Runway Id)
       * from all caches, the sidebar, and the map itself.
       * 
       * @el
       * @id - layer id
       */
      _removeLayer : function(el, id) {
        if(this._layerCache.get(id) !== null){
          
            // Close any info window popups if they exist
            this._mapFactory.removeClickPopup();
            
          var toRemove = this._layerCache.get(id);
          // remove layer from our cache
              this._layerCache.remove(id);
              // remove the layer from the map and UI
              el.parent().parent().remove();
              
              // remove the actual layer from the map
            if(toRemove.wmsLayerObj){
              this._mapFactory.hideLayer(toRemove.wmsLayerObj);
            }
            
            // Remove associated legend and legend container
            //// legend id's are set as the 'legend_' + layer id @ legend creation
            $("#legend_"+id).remove();
            $("li[data-parentlayerid='"+id+"']").remove();
        }
        else if(this._refLayerCache.get(id) !== null){
          toDisable = this._refLayerCache.get(id);
          // disable the layer object
          toDisable.setLayerIsActive(false);
          toDisable.layerExists = false;
          
              // remove the map layer from the map
            if(toDisable.wmsLayerObj){
              this._mapFactory.hideLayer(toDisable.wmsLayerObj);
            }
          
            // change the ui back to disabled
            $(el).hide();
            $(el).parent().find(".ico-edit").hide();
            $(el).parent().find(".ico-enable").show();
            $(el).parent().parent().find(".check").removeClass("checked").hide();
            
            $('*[data-parentlayerid="'+ $(el).data("id") +'"]').remove();
        }
      },
            
      /**
       * Cancel a dashboard creation crud form
       * 
       */
      _cancelDashboardListener : function(){        
          this._closeDashboardModal();
      },
      
      /**
       * Cancel a dashboard creation crud form
       * 
       */
      _cancelDashboardMapListener : function(){  
        
      },
      
      
      /**
       * Renders each base layer as a checkable option in
       * the layer switcher.
       * 
       * @base - array of basemap layer objects
       */
      _renderBaseLayerSwitcher : function(base){
        
        var that = this;
        
        // Create the HTML for each row (base layer representation).
        var ids = [];
        var baseLayers = this._baseLayers;
        for(var i=0; i<base.length; i++){
          
            var id = 'base_layer_'+i;
            
            var b = base[i];
            b.id = id;          
            ids.push(id);
            baseLayers.put(id, b);
            
            var checkboxContainer = this.getFactory().newElement("div", {"class" : "checkbox-container"});
            
            // Assigning better display labels.
            var label = b._gdbCustomLabel;        
            
            com.runwaysdk.event.Registry.getInstance().removeAllEventListeners(id);
            var checkbox = this.getFactory().newCheckBox({checked: false, classes: ["row-form", "jcf-class-check", "chk-area"]});
            checkbox.setId(id);
            if(i === 0){
              checkbox.setChecked(checkbox);
              this._mapFactory.showLayer(b, 0);
            }
            checkbox.addOnCheckListener(function(event){
              target = event.getCheckBox();   
              that._toggleBaseLayer(target);
            });
            checkboxContainer.appendChild(checkbox);            
            
            var labelObj = this.getFactory().newElement("label", {"for" : id, "class" : "checkbox-label"});
            labelObj.setInnerHTML(label);
                                                   
            checkboxContainer.appendChild(labelObj);
            checkboxContainer.render('#'+DynamicMap.BASE_LAYER_CONTAINER);  
        }
      },
      
      
      /**
       * Changes the base layer of the map.
       * 
       * @checkbox - Checkbox object
      */
      _toggleBaseLayer : function(checkBox) {
        var targetId = checkBox.getId();
        var ids = this._baseLayers.keySet();
        var isChecked = checkBox.isChecked();
      
        if (isChecked) {
          for (var i=0; i<ids.length; i++) { 
            if (ids[i] !== targetId) {
              $("#"+ids[i]).removeClass('checked');
              var otherBaselayer = this._baseLayers.get(ids[i]);
              this._mapFactory.hideLayer(otherBaselayer);
            }
            else {
              var newBaselayer = this._baseLayers.get(targetId);
              this._mapFactory.showLayer(newBaselayer, 0);
            }
          }
        }
        else {
          var unchecklayer = this._baseLayers.get(targetId);
          this._mapFactory.hideLayer(unchecklayer);
        }
      },
      
      
      /**
       * Toggles the overlay layers of the map.
       * 
       * @e
       */
      _toggleOverlayLayer : function(e){
        
    	  // Close any info window popups if they exist
    	  this._mapFactory.removeClickPopup();
        
        var cbox = e.getCheckBox();
        var checked = cbox.isChecked();
        var layer = this._layerCache.get(cbox.getData().runwayId);
        
        layer.checked = checked;
        
        if (checked) {
          layer.setLayerIsActive(true);
          this._addUserLayersToMap(true);       
          if(layer.getDisplayInLegend())
          {
            layer.layerLegend.show()
          }
        }
        else {
          layer.setLayerIsActive(false);
          this._mapFactory.hideLayer(layer.wmsLayerObj);          
          if(layer.getDisplayInLegend())
          {
            layer.layerLegend.hide();
          }
        }
      },
      
      
      /**
       * Toggles the overlay layers of the map.
       * 
       * @e
       */
      _toggleReferenceLayer : function(e){
        var cbox = e.getCheckBox();
        var checked = cbox.isChecked();
        var layer = this._refLayerCache.get(cbox._node.dataset.universalid);
        
        layer.checked = checked;
        
        if (checked) {
          layer.setLayerIsActive(true);
          this._addUserLayersToMap(true);       
          if(layer.getDisplayInLegend())
          {
            layer.layerLegend.show()
          }
        }
        else {
          layer.setLayerIsActive(false);
          this._mapFactory.hideLayer(layer.wmsLayerObj);          
          if(layer.getDisplayInLegend())
          {
            layer.layerLegend.hide()
          }
        }
      },
      
      
      /**
       * Disables the search functionality if google can't be loaded
       * 
       */
      _disableAutoComplete : function(){
        $('#'+DynamicMap.GEOCODE).attr('disabled', 'disabled');
      },
      
      /**
       * Hooks the auto-complete functionality to the Dashboard.
       * 
       */
      _addAutoComplete : function(){
        
        var that = this;
        
        this._autocomplete = $('#'+DynamicMap.GEOCODE).autocomplete({
          minLength: 2,
          select : function(value, data){          
            var loc = data.item.value;
            var lonlat = that._suggestionCoords.get(loc);
            var focusCoords = [ lonlat[1], lonlat[0] ];
            that._mapFactory.setView(focusCoords, 13);
            
            that._suggestionCoords.clear();
          },
          source: function(request, response){
          
            that._suggestionCoords.clear();
            
            var term = request.term;
            
            var geocoder = new google.maps.Geocoder();
            geocoder.geocode({ 'address': term }, function (results, status) {
              
              if (status == google.maps.GeocoderStatus.OK) {              
                var rows = Mojo.Util.isArray(results) ? results : [results];
                
                var suggestions = [];
                for(var i=0; i<rows.length; i++){                 
                  var row = rows[i];
                  
                  suggestions.push(row.formatted_address);
                  
                  var lon = row.geometry.location.A;
                  var lat = row.geometry.location.k;
                  that._suggestionCoords.put(row.formatted_address, [lon, lat]);
                }
                response(suggestions);
              }
            });      
          }
        });
      },
           
      /**
       * Gets the html for and calls the new dashboard creation form 
       * 
       * @e 
       */
      _openNewDashboardForm : function(e){
          e.preventDefault();               
          
          var that = this;
          
          var request = new Mojo.ClientRequest({
            onSuccess : function(html){
              that._displayDashboardForm(html);
            },
            onFailure : function(e){
              that._closeDashboardModal();
              that.handleException(e);
            }
          });

          this._DashboardController.newInstance(request);
        },
        
        /**
         * Opens a new tab for the dashboard currently active in the dashboards dropdown
         * 
         */
        _openNewDashboardTab : function(e){
          var url = "#";
          var win = window.open(url, '_blank');
          win.focus();
        },
        
        
        /**
         * Renders the dashboard creation form
         * 
         * @html
         */
        _displayDashboardForm : function(html){         
          
          // Show the white background modal.
          var modal = $(DynamicMap.DASHBOARD_MODAL).first();
          modal.html(html);
          
          jcf.customForms.replaceAll(modal[0]);
          
          modal.modal('show');
        },
        
        
      /**
       * Gets the html for and calls the layer creation/edit form 
       * 
       * @
       */
      _openLayerForAttribute : function(e){
        e.preventDefault();      
        
        var el = $(e.currentTarget);
        var attrId = el.data('id');
        
        var form = new com.runwaysdk.geodashboard.gis.ThematicLayerForm(this, this._mapId);
        form.open(attrId);
      },

      
      /**
       * Gets the html for and calls the layer creation/edit form 
       * 
       * @e 
       */
      _openReferenceLayerForAttribute : function(e){
        e.preventDefault();    
          
        var that = this;
          
        var el = $(e.currentTarget);
        var universalId = el.data('universalid');

        var form = new com.runwaysdk.geodashboard.gis.ReferenceLayerForm(this, this._mapId);
        form.open(universalId);
      },
        
        
      /**
       * Control stack order or legends
       */
      _legendClickDragHandler : function(e){
          
        var topZ = 0;
        $('.legend-container').each(function(){
          var thisZ = parseInt($(this).css('zIndex'), 10);
          if (thisZ > topZ){
            topZ = thisZ;
          }
        });
        $(e.currentTarget).css('zIndex', topZ+1);
      },
      
      /**
       * Persist legend position
       */
      _legendDragStopHandler : function(e){
        var that = this;
        var target = e.currentTarget;
        var relatedLayerId = $(e.currentTarget).data('parentlayerid');
        var newPosition = $(target).position();
        var x = newPosition.left;
        var y = newPosition.top;
        var groupedInLegend = true;
        
        if(!$(this).parent().is("#legend-list-group")){
          groupedInLegend = false;
        }
        
        // Update the layer object in the layer cache with the new legend position
        var relatedLayer;
        if($(target).data("parentlayertype") === "THEMATICLAYER"){
          relatedLayer = that._layerCache.get(relatedLayerId);
        }
        else if($(target).data("parentlayertype") === "REFERENCELAYER"){
          relatedLayer = that._refLayerCache.get($(target).data("parentuniversalid"));
        }
        
        relatedLayer.setLegendXPosition(x);
        relatedLayer.setLegendYPosition(y);
        relatedLayer.setGroupedInLegend(groupedInLegend);
        
        var clientRequest = new Mojo.ClientRequest({
          onSuccess : function() {
          // No action needed
          },
          onFailure : function(e) {
            that.handleException(e);
           
          }
        });
        
        // Persist legend position to the db
        com.runwaysdk.geodashboard.gis.persist.DashboardLayer.updateLegend(clientRequest, relatedLayerId, x, y, groupedInLegend);
      },
      
      /*
       * Scrape filters for values and meta information
       * 
       */
      _reloadCriteria : function () {
        var that = this;
        var criteria = [];
          
        $( "input.gdb-attr-filter" ).each(function( index ) {
          var textValue = $(this).val();
              
          // Ensure there is a value to filter against
          if (textValue != null && textValue !== "") {
            if($(this).hasClass('filter-number')) {              
              // Add number criterias
              var mdAttribute = $(this).data().mdattributeid;
                
              var select = $("#filter-opts-" + mdAttribute).val();

              var number = that._parser( textValue );

              criteria.push({'type':'ATTRIBUTE_CONDITION', 'mdAttribute':mdAttribute, 'operation':select, 'value':number});            
            }
            else if($(this).hasClass('filter-char')) {              
              // Add character criterias
              var mdAttribute = $(this).data().mdattributeid;
              
              var select = $("#filter-opts-" + mdAttribute).val();
              
              criteria.push({'type':'ATTRIBUTE_CONDITION', 'mdAttribute':mdAttribute, 'operation':select, 'value':textValue});            
            }
            else if($(this).hasClass('filter-date')) {
              // Add the date criteria
              if($(this).hasClass('checkin')) {
                var mdAttribute = $(this).data().mdattributeid;                 
                var date = $(this).datepicker('getDate')
                
                // Normalize the incoming date to a standard format regardless of the locale
                var value =  $.datepicker.formatDate('dd/mm/yy', date);

                criteria.push({'type':'ATTRIBUTE_CONDITION', 'mdAttribute':mdAttribute, 'operation':'ge', 'value':value});
              }
              else if($(this).hasClass('checkout')) {
                var mdAttribute = $(this).data().mdattributeid;
                var date = $(this).datepicker('getDate')
                
                // Normalize the incoming date to a standard format regardless of the locale                
                var value =  $.datepicker.formatDate('dd/mm/yy', date);
                
                criteria.push({'type':'ATTRIBUTE_CONDITION', 'mdAttribute':mdAttribute, 'operation':'le', 'value':value});
              }                          
            }
          }          
        });
        
        // Add boolean filters
        $( '.jcf-class-filter-boolean.rad-checked' ).each(function( index ) {          
          var input = $(this).siblings().first();
          var value = input.attr('value');
            
          var mdAttribute = input.attr('name').replace('filter-', '');   
            
          criteria.push({'type':'ATTRIBUTE_CONDITION', 'mdAttribute':mdAttribute, 'operation':'eq', 'value':value});                
        });
        
        // Add term filters
        for(var index = 0; index < this._filterTrees.length; index++) {
          var config = this._filterTrees[index];
          
          var mdAttribute = config.mdAttributeId;
          var terms = config.tree.getCheckedTerms();
          
          if(terms.length > 0) {
            var value = JSON.stringify(terms);
            
            criteria.push({'type':'CLASSIFIER_CONDITION', 'mdAttribute':mdAttribute, 'operation':'eq', 'value':value});
          }
        }
        
        // Add the geo entity filter
        var location = $('#filter-geo-hidden').val();
        
        if(location != null && location.length > 0)
        {
          criteria.push({'type':'LOCATION_CONDITION', 'operation':'eq', 'value':location});                          
        }
        
        return criteria;
      },
      
      /*
       * Returns cached conditions
       */  
      getConditions : function() {
        return this._getConditionsFromCriteria(this._criteria);  
      },
    
      /*
       * Returns the non cached conditions
       */
      getCurrentConditions : function() {
        var criteria = this._reloadCriteria();
        var conditions = this._getConditionsFromCriteria(criteria);
        
        return conditions;
      },
      
      /*
       * Construct Runway condition objects from filter criteria
       * 
       * @param criteria - array of objects that describe current filter settings
       * 
       */
      _getConditionsFromCriteria : function(criteria) {
         
        var conditions = [];
        
        for(var i = 0; i < criteria.length; i++) {
          
          var type = criteria[i].type;
          var operation = criteria[i].operation;
          var value = criteria[i].value;
          
          var condition = null;
            
          if(type == 'LOCATION_CONDITION') {
            condition = new com.runwaysdk.geodashboard.gis.persist.condition.LocationCondition();              
          }            
          else if(type == 'CLASSIFIER_CONDITION') {
            condition = new com.runwaysdk.geodashboard.gis.persist.condition.ClassifierCondition();
            condition.setDefiningMdAttribute(criteria[i].mdAttribute);              
          }            
          else if (type == 'ATTRIBUTE_CONDITION') {
              
            if (operation === "gt") {
              condition = new com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThan();
            }
            else if (operation === "ge") {
              condition = new com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThanOrEqual();
            }
            else if (operation === "lt") {
              condition = new com.runwaysdk.geodashboard.gis.persist.condition.DashboardLessThan();
            }
            else if (operation === "le") {
              condition = new com.runwaysdk.geodashboard.gis.persist.condition.DashboardLessThanOrEqual();
            }
            else if(operation === "neq") {
              condition = new com.runwaysdk.geodashboard.gis.persist.condition.DashboardNotEqual();
            }              
            else {
              condition = new com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqual();                
            }
              
            condition.setDefiningMdAttribute(criteria[i].mdAttribute);              
          }
            
          condition.setComparisonValue(value);
          
          conditions.push(condition);
        }     
        
        return conditions;
      },
      
      
      _onClickExportMap : function(e) {
        var format = $(e.target).data('format');  
        
        this._exportMap();
      },
      
      _onClickZoomMapToExtent : function(e) {
    	  this._configureMap();
      },      
      
      _onClickToggleLeftPanel : function(e) {
    	// TODO: change these classes to not be leaflet specific
        var target = $(e.target);
        var speed = 500;
        
        if(target.hasClass("expanded")){
          $("#control-form").animate({
              left: "-=236",
            },
            speed, 
            function() {
              target.removeClass("expanded");
              target.toggleClass("fa-angle-double-left fa-angle-double-right");
            }
           );
          
          // toggle the map zoom buttons
          $(".leaflet-control-zoom.leaflet-bar.leaflet-control").animate({
              left: "-=236",
          }, speed );
          
          $(".ol-zoom.ol-unselectable.ol-control").animate({
              left: "-=236",
          }, speed );
        }
        else{
          $("#control-form").animate({
              left: "+=236",
            }, 
            speed, 
            function() {
              target.addClass("expanded");
              target.toggleClass("fa-angle-double-right fa-angle-double-left");
            }
          );
          
          // toggle the map zoom buttons
          $(".leaflet-control-zoom.leaflet-bar.leaflet-control").animate({
              left: "+=236",
          }, speed );
          
          $(".ol-zoom.ol-unselectable.ol-control").animate({
              left: "+=236",
          }, speed );
        }
      },
      
      _onClickToggleRightPanel : function(e) {
        var target = $(e.target);
        var panel = $("#dashboardMetadata");
        var speed = 500;
        
        if(panel.hasClass("expanded")){
          $("#dashboardMetadata").animate({
              right: "-=300",
            },
            speed, 
            function() {
              panel.removeClass("expanded");
              target.toggleClass("fa-angle-double-right fa-angle-double-left");
            }
           );
          
          // Report Panel background
          $("#report-viewport").animate({
              marginRight: "0px"
          },
          speed
         );
          
          // Repprt panel toolbar
          $("#report-toolbar").animate({
              marginRight: "0px"
          },
          speed
         );
        }
        else{
          $("#dashboardMetadata").animate({
              right: "+=300",
            }, 
            speed, 
            function() {
            panel.addClass("expanded");
            target.toggleClass("fa-angle-double-left fa-angle-double-right");
            }
          );
          
          // Report Panel background
          $("#report-viewport").animate({
              marginRight: "300px"
          },
          speed
         );
        
          // Repprt panel toolbar
          $("#report-toolbar").animate({
                marginRight: "300px"
            },
            speed
        );
        }
      },
      
      _onClickExportReport : function(e) {
        var format = $(e.target).data('format');

        this._exportReport(this._currGeoId, this._criteria, format);
      },
      
      _onClickUploadReport : function(e) {
        var that = this;
          
        var config = {
          type: 'com.runwaysdk.geodashboard.report.ReportItem',
          action: "update",
          viewAction: "edit",
          viewParams: {id: this._dashboardId},          
          width: 600,
          onSuccess : function(dto) {
            $("#report-export-container").show();
          },
          onFailure : function(e) {
            that.handleException(e);
          },
          onCancel : function(e) {
            var request = new Mojo.ClientRequest({
              onSuccess : function () {
                // Close the dialog ??
              },
              onFailure : function(e) {
                that.handleException(e);
              }
            });
            
            com.runwaysdk.geodashboard.report.ReportItem.unlockByDashboard(request, that._dashboardId);
          }
        };
            
        new com.runwaysdk.ui.RunwayControllerFormDialog(config).render();
      },
      
      _onClickApplyFilters : function(e) {
        var that = this;
        
        // Close any info window popups if they exist
        that._mapFactory.removeClickPopup();
        
        // Validate there are no existing errors
        var errorCount = $('.gdb-attr-filter.field-error').length
        
        if(errorCount > 0) {
          var message = com.runwaysdk.Localize.localize("filter", "error");
          
          that._renderMessage(message);
        }
        else {
          this._criteria = this._reloadCriteria();
          var conditions = this._getConditionsFromCriteria(this._criteria);
          
          var clientRequest = new com.runwaysdk.geodashboard.StandbyClientRequest({
            onSuccess : function(json, calledObj, response) {
              var jsonObj = Mojo.Util.toObject(json);
               
              that._updateCacheFromJSONResponse(jsonObj);
                
              that._addUserLayersToMap(true);
                
              that._drawLegendItems();
                
              // TODO : Push this somewhere as a default handler.
              that.handleMessages(response);
            },
            onFailure : function(e) {
              that.handleException(e);
            }
          }, $(DynamicMap.DASHBOARD_MODAL)[0]);
            
          com.runwaysdk.geodashboard.gis.persist.DashboardMap.updateConditions(clientRequest, this._mapId, conditions);
          
          this._currGeoId = '';
            
          this._renderReport('', this._currGeoId, this._criteria);
        }
      },
      
      _onClickSaveFilters : function(e) {
        var that = this;
        
        // Validate there are no existing errors
        var errorCount = $('.gdb-attr-filter.field-error').length
        
        if(errorCount > 0) {
          var message = com.runwaysdk.Localize.localize("filter", "error");
          
          that._renderMessage(message);
        }
        else {
          var criteria = this._reloadCriteria();
          var conditions = this._getConditionsFromCriteria(criteria);          
          
          var clientRequest = new Mojo.ClientRequest({
            onSuccess : function() {
            },
            onFailure : function(e) {
              that.handleException(e);
            }
          });
          
          com.runwaysdk.geodashboard.Dashboard.applyConditions(clientRequest, this._dashboardId, conditions);
        }
      },
      
      _onClickSaveGlobalFilters : function(e) {
        var that = this;
          
        // Validate there are no existing errors
        var errorCount = $('.gdb-attr-filter.field-error').length
          
        if(errorCount > 0) {
          var message = com.runwaysdk.Localize.localize("filter", "error");
            
          that._renderMessage(message);
        }
        else {
          var criteria = this._reloadCriteria();
          var conditions = this._getConditionsFromCriteria(criteria);          
            
          var clientRequest = new Mojo.ClientRequest({
            onSuccess : function() {
            },
            onFailure : function(e) {
              that.handleException(e);
            }
          });
            
          com.runwaysdk.geodashboard.Dashboard.applyGlobalConditions(clientRequest, this._dashboardId, conditions);
        }
      },
      /**
       * Callback for sorting legend items
       */
      _legendSortUpdate : function(event, ui){
         // No action needed at this time. This is simply a ui feature.
      },
      
      _validateInteger : function (value) {
        var number = this._parser( value );
          
        return (value != '' && (!$.isNumeric(number) || Math.floor(number) != number));
      },
      
      _validateNumeric : function (value) {
        var number = this._parser( value );
        
        return (value != '' && !$.isNumeric(number));        
      },
      
      _setReportPanelHeight : function (height, flipButton) {
        var current = $("#reporticng-container").height();
        var toolbar = $("#report-toolbar").height();        
        
        // Minimize
        if(current > height)
        {
          var difference = (current - height);
          
          $("#reporticng-container").animate({ bottom: "-=" + difference + "px" }, 1000, function(){
            
          if(flipButton){
            $("#report-toggle-container").toggleClass("maxed");
          }
            
            $("#reporticng-container").css("bottom", "0px");                                                  
            $("#report-viewport").height(height-toolbar);
            $("#reporticng-container").height(height);
          });     
          
          // animate the loading spinner
          $(".standby-overlay").animate({top: "+=" + difference + "px"}, 1000);
        }
        // Maximize
        else if (current < height){
          var difference = (height - current);
          
          $("#reporticng-container").css("bottom", "-" + difference + "px");
          $("#reporticng-container").height(height);
          $("#report-viewport").height(height-toolbar);
              
          $("#reporticng-container").animate(
            {bottom: "+=" + difference + "px"}, 
            1000, function() {
            if(flipButton){
              $("#report-toggle-container").toggleClass("maxed");
            }
            }
           );
          
          // animate the loading spinner
          $(".standby-overlay").animate({top: "-=" + difference + "px"}, 1000);
          $(".standby-overlay").css("height", current + difference);
        }          
      },
      
      /**
       * Renders the mapping widget, performing a full refresh.
       */
      render : function(){
        var that = this;
        
        var conditions = {};
        
        if(this._criteria != null){
          for(var i = 0; i < this._criteria.length; i++) {
            var condition = this._criteria[i];
            
            if(condition.type == 'ATTRIBUTE_CONDITION' || condition.type == 'CLASSIFIER_CONDITION') {
              if(conditions["ATTRIBUTE_CONDITION"] == null) {
                conditions["ATTRIBUTE_CONDITION"] = {};              
              }
              
              if(conditions["ATTRIBUTE_CONDITION"][condition.mdAttribute] == null) {
                conditions["ATTRIBUTE_CONDITION"][condition.mdAttribute] = [];
              }
              
              conditions["ATTRIBUTE_CONDITION"][condition.mdAttribute].push(condition);
            }
            else if(condition.type == 'LOCATION_CONDITION') {
              conditions['LOCATION_CONDITION'] = condition;
            }
            
          }
        }
        
        // Make sure all openers for each attribute have a click event
        $('a.attributeLayer').on('click', Mojo.Util.bind(this, this._openLayerForAttribute));
        $('a.new-dashboard-btn').on('click', Mojo.Util.bind(this, this._openNewDashboardForm));
        $('.ico-new-dashboard-tab').on('click',  Mojo.Util.bind(this, this._openNewDashboardTab));
        $('a.apply-filters-button').on('click', Mojo.Util.bind(this, this._onClickApplyFilters));
        $('a.save-filters-button').on('click', Mojo.Util.bind(this, this._onClickSaveFilters));
        $('a.save-global-filters-button').on('click', Mojo.Util.bind(this, this._onClickSaveGlobalFilters));
        
        // Reporting events
        $('.report-export').on('click', Mojo.Util.bind(this, this._onClickExportReport));        
        $('#report-upload').on('click', Mojo.Util.bind(this, this._onClickUploadReport));
        
        $('#map-export-btn').on('click', Mojo.Util.bind(this, this._onClickExportMap)); 
        $('#map-zoom-to-extent-btn').on('click', Mojo.Util.bind(this, this._onClickZoomMapToExtent));
        
        $('#control-form-collapse-button').on('click', Mojo.Util.bind(this, this._onClickToggleLeftPanel));
        $('#data-panel-expand-toggle').on('click', Mojo.Util.bind(this, this._onClickToggleRightPanel));
        
        $('.report-height-toggle').on('click', function(e){
          var target = e.target;
          var height = $("#mapDivId").height();
          
          if(target.id === "report-expand-toggle"){
            if(that._reportPanelState === 'min'){
              var splitHeight = Math.floor(height / 2);
              that._setReportPanelHeight(splitHeight, false);
              $("#report-collapse-toggle").show();
              that._reportPanelState = 'split';
            }
              else if(that._reportPanelState === 'split'){
                var reportToolbarHeight = 30;
                    that._setReportPanelHeight(height + reportToolbarHeight, true);
                    that._reportPanelState = 'max';
                    $("#report-expand-toggle").hide();
              }
          }
          else{
            if(that._reportPanelState === 'split'){
              that._setReportPanelHeight(0, false);
              $("#report-collapse-toggle").hide();
              that._reportPanelState = 'min';
            }
              else if(that._reportPanelState === 'max'){
              var splitHeight = Math.floor(height / 2);
              that._setReportPanelHeight(splitHeight, true);
                    that._reportPanelState = 'split';
                    $("#report-expand-toggle").show();
              }
          }
        });
        
        // Render the menu
        $( "#report-menu" ).menu();
        
        if(this._googleEnabled){
          this._addAutoComplete();
        }    
        
        // Javascript to prevent input of non-number values in a number field
        $('.integers-only').keyup(function (event) {          
          if (that._validateInteger(this.value)) {            
            $(this).addClass('field-error');
          }
          else {            
            $(this).removeClass('field-error');
          }
        });      
        
        // Javascript to prevent input of non-number values in a number field
        $('.numbers-only').keyup(function () {
          if (that._validateNumeric(this.value)) {            
            $(this).addClass('field-error');
          }
          else {            
            $(this).removeClass('field-error');
          }
        });
        
        // Hook up the filter auto-complete for character attributes
        $('.filter-char').each(function(){
          
          var mdAttributeId = $(this).data().mdattributeid;
          
          $(this).autocomplete({
            source: function( request, response ) {
              var req = new Mojo.ClientRequest({
                onSuccess : function(results){
                  response( results );
                },
                onFailure : function(e){
                  that.handleException(e);
                }
              });
              
              com.runwaysdk.geodashboard.Dashboard.getTextSuggestions(req, mdAttributeId, request.term, 10);
            },
            minLength: 2
          });
          
          // Load the values
          if(conditions["ATTRIBUTE_CONDITION"] != null && conditions["ATTRIBUTE_CONDITION"][mdAttributeId] != null) {
            var condition = conditions["ATTRIBUTE_CONDITION"][mdAttributeId][0];
            
            $('#filter-opts-' + mdAttributeId).val(condition.operation);
            $(this).val(condition.value);
            
            if (jcf != null && jcf.customForms != null) {
              jcf.customForms.refreshElement($('#filter-opts-' + mdAttributeId).get(0));
            }
            
            // Open the filter input panel if a saved value is set for this filter block
            $(this).parent().parent().parent().parent().parent().parent().find(".opener-link").click();
          }          
        });
        
        // Hook up the filter auto-complete for term attributes and load saved values        
        $('.filter-term').each(function(){
          var mdAttributeId = $(this).attr('id');
          
          // Get the term roots and setup the tree widget
          var req = new Mojo.ClientRequest({
            onSuccess : function(results){
              var nodes = JSON.parse(results);
              var rootTerms = [];
              
              for(var i = 0; i < nodes.length; i++) {
                var id = nodes[i].id;              
                rootTerms.push({termId : id});
              }
              
              var tree = new com.runwaysdk.geodashboard.ontology.OntologyTree({
                termType : "com.runwaysdk.geodashboard.ontology.Classifier" ,
                relationshipTypes : [ "com.runwaysdk.geodashboard.ontology.ClassifierIsARelationship" ],
                rootTerms : rootTerms,
                editable : false,
                slide : false,
                selectable : false,
                checkable : true
              });
              
              tree.render("#" + mdAttributeId, nodes);
              
              // Load the values
              if(conditions["ATTRIBUTE_CONDITION"] != null && conditions["ATTRIBUTE_CONDITION"][mdAttributeId] != null) {
                var condition = conditions["ATTRIBUTE_CONDITION"][mdAttributeId][0];
                
                tree.setCheckedTerms(JSON.parse(condition.value));
                
                // Open the filter input panel if a saved value is set for this filter block
                $("#" + mdAttributeId).parent().parent().parent().parent().parent().find(".opener-link").click();
              }          
              
              that._filterTrees.push({
                "mdAttributeId" : mdAttributeId,
                "tree" : tree
              });
            },
            onFailure : function(e){
              that.handleException(e);
            }
          });
          
          com.runwaysdk.geodashboard.Dashboard.getClassifierTree(req, mdAttributeId);
        });
        
        // Hook up the filter auto-complete for the global location filter and load saved values        
        if(conditions["LOCATION_CONDITION"] != null) {
          var condition = conditions["LOCATION_CONDITION"];
          
          $('#filter-geo').val(condition.label);
          $('#filter-geo-hidden').val(condition.value);
        }
        
        $('#filter-geo').autocomplete({
          source: function( request, response ) {  
            var req = new Mojo.ClientRequest({
              onSuccess : function(query){
                var resultSet = query.getResultSet()
                                    
                var results = [];
                  
                $.each(resultSet, function( index, result ) {
                  var label = result.getValue('displayLabel');
                  var id = result.getValue('id');
                    
                  results.push({'label':label, 'value':label, 'id':id});
                });
                  
                response( results );
              },
              onFailure : function(e){
                that.handleException(e);
              }
            });
              
            com.runwaysdk.geodashboard.Dashboard.getGeoEntitySuggestions(req, that._dashboardId, request.term, 10);
          },
          select: function(event, ui) {
            $('#filter-geo-hidden' ).val(ui.item.id);
          }, 
          change : function(event, ui) {
            var value = $('#filter-geo').val();
          
            if(value == null || value == '') {                
              $('#filter-geo-hidden').val('');
            }
          },
          minLength: 2
        });
        
        // Hook up the mutual exclusive filter for boolean attributes
        $('.jcf-class-filter-boolean').each(function(){
          $(this).on('click', function(e) {
            var checked = $(this).hasClass( "rad-checked" );
            
            if(e.ctrlKey && checked)
            {
              $(this).removeClass( "rad-checked" );
            }
          });
          
          // Load saved boolean values
          var input = $(this).siblings().first();
          var inputValue = input.attr('value');
            
          var mdAttributeId = input.attr('name').replace('filter-', '');   
          
          if(conditions["ATTRIBUTE_CONDITION"] != null && conditions["ATTRIBUTE_CONDITION"][mdAttributeId] != null) {
            var condition = conditions["ATTRIBUTE_CONDITION"][mdAttributeId][0];
            var value = condition.value;
            
            
            if(inputValue ==  value) {
              $(this).addClass( "rad-checked" );
            }
          }          
        });
        
        // Load saved date values
        $('.filter-date').each(function(){
          var mdAttributeId = $(this).data().mdattributeid;
          var opened = false;
          
          if(conditions["ATTRIBUTE_CONDITION"] != null && conditions["ATTRIBUTE_CONDITION"][mdAttributeId] != null) {
            var array = conditions["ATTRIBUTE_CONDITION"][mdAttributeId];
            
            for(var i = 0; i < array.length; i++) {
              var condition = array[i];
              
              if($(this).hasClass('checkin') && condition.operation == 'ge') {
                
                // Normalize the incoming date to a standard format regardless of the locale
                var date =  $.datepicker.parseDate('dd/mm/yy', condition.value);                
                
                $(this).datepicker( "setDate", date );
              }
              else if($(this).hasClass('checkout') && condition.operation == 'le') {
                
                // Normalize the incoming date to a standard format regardless of the locale
                var date =  $.datepicker.parseDate('dd/mm/yy', condition.value);                
                
                $(this).datepicker( "setDate", date );
              }
              
              // Open the filter input panel if a saved value is set for this filter block
              if(!opened){
                $(this).parent().parent().parent().parent().parent().parent().parent().parent().find(".opener-link").click();
                opened = true;
              }
            }            
          }                
        });        
        
        // Load saved number values
        $('.filter-number').each(function(){
          var mdAttributeId = $(this).data().mdattributeid;
          var opened = false;
          
          if(conditions["ATTRIBUTE_CONDITION"] != null && conditions["ATTRIBUTE_CONDITION"][mdAttributeId] != null) {
            var array = conditions["ATTRIBUTE_CONDITION"][mdAttributeId];
            var condition = array[0];
            
            var value = that._formatter(parseFloat(condition.value));
            
            $(this).val(value);
            $('#filter-opts-' + mdAttributeId).val(condition.operation);
            
            if (jcf != null && jcf.customForms != null) {
              var element = $('#filter-opts-' + mdAttributeId).get(0);
              
              jcf.customForms.refreshElement(element);
            }
            
            // Open the filter input panel if a saved value is set for this filter block
            if(!opened){
              $(this).parent().parent().parent().parent().parent().parent().find(".opener-link").click();
              opened = true;
            }
          }                
        }); 
        
        
        this.fullRefresh();
      },
    }   
  });
  
  var DataType = Mojo.Meta.newClass(GDB.Constants.GIS_PACKAGE+'DataType', {
  
    Instance : {
      initialize: function(label){
        this.$initialize();
        
        this.label = label;
      }
    }
  });
  
  var AbstractAttribute = Mojo.Meta.newClass(GDB.Constants.GIS_PACKAGE+'AbstractAttribute', {
    
    Instance : {
      initialize: function(label){
        this.$initialize();
        
        this.label = label;
      }
    }
  });
  
})();
