(function(){
  
  var Component = com.runwaysdk.ui.Component;  
  var DynamicMapName = GDB.Constants.GIS_PACKAGE+'DynamicMap';
  
  /**
  * LANGUAGE
  */
  com.runwaysdk.Localize.defineLanguage(DynamicMapName, {
   "googleStreets" : "Google Streets",
   "googleSatellite" : "Google Satellite",
   "googleHybrid" : "Google Hybrid",
   "googleTerrain" : "Google Terrain",
   "osmBasic" : "Open Street Map",
   "location" : "Location",
   "aggregationMethod" : "Aggregation Method", 
   "aggregateValue" : "Value"
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
        this._map = null;
        
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
        
        var overlayBound = Mojo.Util.bind(this, this._overlayHandler);
        var refBound = Mojo.Util.bind(this, this._referenceHandler);
        overlayLayerContainer.on('click', 'a', overlayBound);
        referenceLayerContainer.on('click', 'a', refBound);
        
        var dashboardBound = Mojo.Util.bind(this, this._dashboardClickHandler);
        $(".gdb-dashboard").on("click", dashboardBound); 
        
        // Handler for the clone dashboard button
        $("#clone-dashboard").on("click", Mojo.Util.bind(this, this._dashboardCloneHandler));        
        
        this._LayerController = com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerController;
        this._ReferenceLayerController = com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerController;
        this._DashboardController = com.runwaysdk.geodashboard.DashboardController;
        this._ReportController = com.runwaysdk.geodashboard.report.ReportItemController;
        
        // set controller listeners
        this._LayerController.setCancelListener(Mojo.Util.bind(this, this._cancelLayerListener));
        this._LayerController.setApplyWithStyleListener(Mojo.Util.bind(this, this._applyWithStyleListener));       
        this._ReferenceLayerController.setCancelListener(Mojo.Util.bind(this, this._cancelReferenceLayerListener));
        this._ReferenceLayerController.setApplyWithStyleListener(Mojo.Util.bind(this, this._applyWithStyleListener)); 
        this._DashboardController.setCancelListener(Mojo.Util.bind(this, this._cancelDashboardListener));
        this._DashboardController.setCreateListener(Mojo.Util.bind(this, this._applyDashboardListener));
        
        overlayLayerContainer.sortable({
          update: Mojo.Util.bind(this, this._overlayLayerSortUpdate)
        });
        overlayLayerContainer.disableSelection();      
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
        
        this._renderReport('', this._criteria);
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
      
      _getSecondaryAttriubteCategories : function() {
        if($("#secondary-tree").length > 0) {
          var categories = this._getTermCategoryFromTree('secondary-tree');
            
          return categories;
        }
        else if ($('#secondary-choice-color').length > 0) {
          var categories = this._getCategoryFromList("#secondary-choice-color", false);
          
          return categories;
        }
          
        return null;
      },
      
      _getTermCategoryFromTree : function (elementId) {
        
        if($("#" + elementId).length > 0) {
          var categories = [];
          
          var elements = $("#" + elementId).find(".ontology-category-color-icon");
          
          $.each(elements, function( index, element ) {
            var rwId = element.dataset.rwid;
              
            // filters out the jqTree 'phantom' elements which are duplicates of the elements we are after                    
            if(rwId.indexOf("PHANTOM") === -1){
                        
              var category = new Object();
              category.id = element.dataset.rwid; 
              category.val = element.parentElement.previousSibling.textContent;
              category.color = GDB.gis.DynamicMap.prototype.rgb2hex($(element).css("background-color"));
              category.isOntologyCat = true;
                        
              categories.push(category);
            }
          });          
                
          return categories;
        }
        
        return null;        
      },
      
      _getCategoryFromList : function(parentElementId, checkOther) {
        var elements = $(parentElementId).find(".category-container");
        var categories = [];        
        
        for(var i=0; i< elements.length; i++){
          var catInputElem = $(elements[i]).find(".category-input");
          var catColorElem = $(elements[i]).find(".cat-color-selector");
          var catColor = GDB.gis.DynamicMap.prototype.rgb2hex($(catColorElem).css("background-color"));
          var catVal = catInputElem.val();
            
          // parse the formatted number to the format of the data so the SLD can apply categories by this value
          if(catInputElem.data("type") == "number" ) {
            var thisNum = this._parser(catVal);
              if($.isNumeric(thisNum)) {
                catVal = thisNum;                
              }
          }
            
          // Filter out categories with no input values
          if(catVal !== ""){
            var category = new Object();
            category.val = catVal;
            category.color = catColor;
            category.isOntologyCat = false;
            
            if(checkOther) {
             category.otherEnabled = $("#f53").prop("checked");
             
             if(catInputElem[0].id === "cat-other"){
                 category.otherCat = true;
               }
               else{
                 category.otherCat = false;
               }
            }
            else {
              category.otherEnabled = false;
              category.otherCat = false;
            }
                 
            categories.push(category);
          }
        }  
        
          
    return categories;

      },
      
      
      /**
       * Scrapes categories on the layer creation/edit form
       * 
       */
       _updateCategoriesJSON : function() {
         var styleArr = new Object();
         styleArr.catLiElems = [];
         
         if($("#ontology-tree").length > 0) {
           styleArr.catLiElems = this._getTermCategoryFromTree('ontology-tree');
         }
         else {           
           styleArr.catLiElems = this._getCategoryFromList('#category-colors-container', true);
         }
         
         // set the hidden input element in the layer creation/edit form 
         $("#categories-input").val(JSON.stringify(styleArr));
         
         return  styleArr;
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
	          
	          var view = this._setLayerViewObj(layer);
	          
	          var oldLayer = this._layerCache.get(view.getLayerId());
	          if (oldLayer != null) {
	            view.leafletLayer = oldLayer.leafletLayer;
	          }
	          this._layerCache.put(view.getLayerId(), view);        
	        }
    	}
        
        // Build reference layer objects and populate the cache
        if(json.refLayers){
	        for (var r = 0; r < json.refLayers.length; ++r) {
	        	var refLayer = json.refLayers[r];
	        	
	        	// Construct the layer view objects
	        	var refView = this._setLayerViewObj(refLayer);
		        var oldLayer = this._refLayerCache.get(refView.universalId);
		        if (oldLayer != null) {
		          refView.leafletLayer = oldLayer.leafletLayer;
		        }
		        
		        this._refLayerCache.put(refView.universalId, refView);
	        }
	        
	        if (json.bbox != null) {
	          this._bBox = json.bbox;
	        }
        }
      },
      
      /**
       * Constructs a layer object from json returned from the server.
       * The json does not type objects so we construct the appropriate type.
       * 
       * @layer - DashboardLayer representation
       */
      _setLayerViewObj : function(layer) {
    	  var view;
    	  
    	  if(layer.layerType === "THEMATICLAYER"){
	          view = new com.runwaysdk.geodashboard.gis.persist.DashboardLayerView();
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
    	  }
    	  else if(layer.layerType === "REFERENCELAYER"){
    		  var uniId = layer.uniId;
    		  
    		  view = new com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerView();
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
	          view.setLayerIsActive(true);
	          
	          view.layerType = layer.layerType;
	          view.universalId = uniId;
	          
	          if(layer.styles){
	        	view.style = layer.styles[0]; 
	          }
	          else{
	        	view.style = null;
	          }
    	  }
    	  else if(layer.layerType === "REFERENCEJSON"){
  	          var displayName = layer.properties.uniDispLabel;  
	          var uniId = layer.properties.uniId;
	          var exists = layer.properties.refLayerExists;
	          
    		  view = new com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerView();
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
    	  }
    	  
          return view;
      },
           
      /**
       * Creates a new Leaflet map. If one already exists the existing one will be cleaned up and removed.
       * 
       */
      _renderMap : function() {
        if (this._map != null) {
          $('#'+DynamicMap.BASE_LAYER_CONTAINER).html('');
          this._map.remove();
          $('#'+this._mapDivId).html('');
        }
        
        this._map = new L.Map(this._mapDivId, {zoomAnimation: false, zoomControl: true});
        
        // Add attribution to the map
        this._map.attributionControl.setPrefix("");
        //this._map.attributionControl.addAttribution("TerraFrame | GeoDashboard");
        
        var mapClickHandlerBound = Mojo.Util.bind(this, this._mapClickHandler);
        this._map.on("click", mapClickHandlerBound);
      },
      
      /**
       * Creates a new BIRT report. If one already exists the existing one will be cleaned up and removed.
       * 
       * @gedId
       * @criteria
       * 
       */
      _renderReport : function(geoId, criteria) {
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
          configuration.parameters.push({'name' : 'category', 'value' : geoId});
          configuration.parameters.push({'name' : 'criteria', 'value' : JSON.stringify(criteria)});
          configuration.parameters.push({'name' : 'width', 'value' : widthPt});
          configuration.parameters.push({'name' : 'height', 'value' : heightPt});
        
          this._ReportController.run(request, this._dashboardId, JSON.stringify(configuration));		
    	}
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
        // Handle points (2 coord sets) & polygons (4 coord sets)
        if (this._bBox.length === 2){
          var center = L.latLng(this._bBox[1], this._bBox[0]);
          this._map.setView(center, 9);
        }
        else if (this._bBox.length === 4){
          var swLatLng = L.latLng(this._bBox[1], this._bBox[0]);
          var neLatLng = L.latLng(this._bBox[3], this._bBox[2]);            
          var bounds = L.latLngBounds(swLatLng, neLatLng);   

          this._map.fitBounds(bounds);
        }
      },
      
      
      /**
       * Adds leaflet layers to the map and builds the base layer checkboxes
       * 
       */
      _renderBaseLayers : function() {
        this._baseLayers.clear();
        
        try {
          var base = this._getBaseLayers();                                
          this._map.addLayer(base[0]);
          
          this._renderBaseLayerSwitcher(base);
        }
        catch (ex) {
          // If we don't have internet, rendering the base layer will fail.
        }
      },
      
      
      /**
       * Redraws the HTML representing the user-defined layers and adds the layers to Leaflet.
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
	        	html += '<a href="#" data-id="'+id+'" data-universalid="'+layer.universalId+'" class="ico-remove">remove</a>';
	        	html += '<a href="#" data-id="'+id+'" data-universalid="'+layer.universalId+'" class="ico-edit">edit</a>';
	        	html += '<a href="#" data-universalid="'+layer.universalId+'" class="referenceLayer ico-enable" style="display:none;">enable</a>';
	        }
	        else if(this._editable) {              
 	        	html += '<a href="#" data-id="'+id+'" data-universalid="'+layer.universalId+'" class="ico-remove" style="display:none;">remove</a>';
	        	html += '<a href="#" data-id="'+id+'" data-universalid="'+layer.universalId+'" class="ico-edit" style="display:none;">edit</a>';
	        	html += '<a href="#" data-universalid="'+layer.universalId+'" class="referenceLayer ico-enable">enable</a>';
	        }
	        html += '<a href="#" data-id="'+id+'" class="ico-control">control</a></div>';
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
            html += '<div class="cell"><a href="#" data-id="'+layer.getLayerId()+'" class="ico-remove">remove</a>';
            html += '<a href="#" data-id="'+layer.getLayerId()+'" class="ico-edit">edit</a>';
            html += '<a href="#" data-id="'+layer.getLayerId()+'" class="ico-control">control</a></div>';
          }
          
          html += '</div>';
        }
        
        // 2) Render the HTML we just generated.
        container.html(html);
        
        // 3) Add checkboxes and register click events
        for(var i = 0; i < layers.length; i++){
          var layer = layers[i];
          
          var chexd = layer.checked === false || layer.checked === true ? layer.checked : true;
          
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
       * Adds all layers in the layerCache to leaflet, in the proper ordering.
       * 
       * @param boolean removeExisting Optional, if unspecified all existing layers will be removed first. If set to false, only layers that leaflet
       *   does not already know about will be added.
       */
      _addUserLayersToMap : function(removeExisting) {
        var layers = this._layerCache.values();
        var refLayers = this._refLayerCache.values();
        
        // Remove any already rendered layers from the leaflet map
        if (removeExisting === true) {
          for (var i = 0; i < layers.length; i++) {
            var layer = layers[i];
            
            if (layer.leafletLayer != null) {
              this._map.removeLayer(layer.leafletLayer);
            }
          }
          
          for (var i = 0; i < refLayers.length; i++) {
              var layer = refLayers[i];
              
              if (layer.leafletLayer && layer.leafletLayer != null) {
                this._map.removeLayer(layer.leafletLayer);
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
	            var geoserverName = this._workspace + ":" + viewName;
	            
	              var mapBounds = this._map.getBounds();
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
	          
	              this._map.addLayer(leafletLayer);
	              layer.leafletLayer = leafletLayer;
	          }
          }
        }
        
        //
        // Add thematic layers to leaflet map
        //
        for (var i = 0; i < layers.length; i++) {
          var layer = layers[i];
          var viewName = layer.getViewName();
          var geoserverName = this._workspace + ":" + viewName;
          if (layer.getLayerIsActive() === true && (removeExisting !== false || (removeExisting === false && layer.leafletLayer == null))) {
	          if(layer.getFeatureStrategy() === "BUBBLE"){
		            var leafletLayer = new L.NonTiledLayer.WMS(window.location.origin+"/geoserver/wms/", {
		                layers: geoserverName,
		                format: 'image/png',
		                transparent: true,
		                styles: layer.getSldName() || "" 
		              });
	            
		            this._map.addLayer(leafletLayer);
		            layer.leafletLayer = leafletLayer;
	          }
	          else{
	              // This tiling format (tileLayer) is the preferred way to render wms due to performance gains. 
	              // However, since bubbles are clipped by the differences between tiles we must render bubble layers as a single tile. 
	              // We should revisit this in the future to determine if bubbles can be supported in a tileLayer
	              var mapBounds = this._map.getBounds();
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
	          
	              this._map.addLayer(leafletLayer);
	              layer.leafletLayer = leafletLayer;
		      }
          }
        }
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
            
            // At this point, the layers are already ordered properly in the HTML. All we need to do is inform leaflet of the new ordering.
            that._addUserLayersToMap(true);
          },
          onFailure : function(e) {
            that.handleException(e);
            
            // The server failed to reorder the layers. We need to redraw the HTML to reset the layer ordering, but we don't need to update leaflet because that ordering is still correct.
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
          
        var that = this;
          
        var request = new Mojo.ClientRequest({
          onSuccess : function(html){     
          // Remove the internal form div if it exists
            $( "#clone-dialog" ).remove();
            
            // Set the html of the dialog
            $( "#clone-container" ).html(html);
            
            // Show the dialog
            $( "#clone-dialog" ).dialog({
              resizable: false,
              height:200,
              width:730,
              modal: true,
              buttons: [{
                text : com.runwaysdk.Localize.localize("dashboard", "Ok", "Ok"),
                "class": 'btn btn-primary',
                click : function() {
                  var createRequest = new com.runwaysdk.geodashboard.StandbyClientRequest({
                    onSuccess : function(dashboard){
                      $( "#clone-dialog" ).dialog( "close" );
                    	
                      window.location = "?dashboard=" + dashboard.getId();
                    },
                    onFailure : function(e){
                      $( "#clone-dialog" ).dialog( "close" );
                      
                      that.handleException(e);
                    }
                  }, $("#clone-dialog").parent().get(0));
                  
                  var dashboardId = $('#clone-dashboard-id').val();
                  var label = $('#clone-label').val();
                  
                  if(label != null && label.length > 0)
                  {
                    com.runwaysdk.geodashboard.Dashboard.clone(createRequest, dashboardId, label);                    
                  }
                  else
                  {
                    var msg = com.runwaysdk.Localize.localize("dashboard", "Required");
                    
                    $('#clone-label-error').html(msg);        
                    $('#clone-label-field-row').addClass('field-error');                    
                  }
                }
              },
              {
                text : com.runwaysdk.Localize.localize("dashboard", "Cancel", "Cancel"),
                "class": 'btn btn-default',
                click : function() {
                   $( this ).dialog( "close" );
                }
              }]
            });
          },
          onFailure : function(e){
            that.handleException(e);
          }
        });
        
        this._DashboardController.newClone(request, this._dashboardId);
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
      _overlayHandler : function(e){       
        var that = this;     
        var el = $(e.currentTarget);
        
        if(el.hasClass('ico-edit')) {
          // edit the layer
          var id = el.data('id');
          this._LayerController.edit(new Mojo.ClientRequest({
            onSuccess : function(html){
              that._displayLayerForm(html);
              that._addLayerFormControls();
            },
            onFailure : function(e){
              that.handleException(e);
            }
          }), id);
          
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
      _referenceHandler : function(e){       
        var that = this;     
        var el = $(e.currentTarget);
        
        if(el.hasClass('ico-edit')) {
          // edit the layer
          var id = el.data('id');
          this._ReferenceLayerController.edit(new Mojo.ClientRequest({
            onSuccess : function(html){
              that._displayLayerForm(html);
              that._addLayerFormControls();
            },
            onFailure : function(e){
              that.handleException(e);
            }
          }), id);
          
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
       * Performs the identify request when a user clicks on the map
       * 
       * This is a first version and will likely need to be rewritten using custom
       * backend logic (i.e. mdmethod/sql) to allow for more flexibility
       * 
       * @param id
       */
      _mapClickHandler : function(e) {
        
        var layers = this._layerCache.$values().reverse();
        
        if(layers.length > 0) {
          
          // Construct a GetFeatureInfo request URL given a point        
          var point = this._map.latLngToContainerPoint(e.latlng, this._map.getZoom());
          var size = this._map.getSize();        
          var mapBbox = this._map.getBounds().toBBoxString();
          var map = this._map;
          var layerMap = new Object();
          var layerStringList = '';
          var aggregationAttr = '';
          var popup = L.popup().setLatLng(e.latlng);
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
        
        var requestURL = window.location.origin+"/geoserver/" + this._workspace +"/wms?" +
          "REQUEST=GetFeatureInfo" +
          "&INFO_FORMAT=application/json" +
          "&EXCEPTIONS=APPLICATION/VND.OGC.SE_XML" +
          "&SERVICE=WMS" +
          "&SRS="+DynamicMap.SRID +
          "&VERSION=1.1.1" +
          "&height=" + size.y +
          "&width=" + size.x +
          "&X="+ point.x +
          "&Y="+ point.y +
          "&BBOX="+ mapBbox +
          "&LAYERS=geodashboard:"+ layerStringList +
          "&QUERY_LAYERS=geodashboard:"+ layerStringList +
          "&TYPENAME=geodashboard:"+ layerStringList;
      
        DynamicMap.that = this;
          $.ajax({
              url: requestURL,
              context: document.body 
          }).done(function(json) {
            var popupContent = '';
            
            // The getfeatureinfo request will return only 1 feature
            for(var i = 0; i < json.features.length; i++){
              var featureLayer = json.features[i];
              var featureProperties = featureLayer.properties;
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
              html += '<th>'+DynamicMap.that.localize("location")+'</th>';  
              html += '<th>'+DynamicMap.that.localize("aggregationMethod")+'</th>'; 
              html += '<th>'+DynamicMap.that.localize("aggregateValue")+'</th>'; 
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
                that._currGeoId = currGeoId;
              
                that._renderReport(that._currGeoId, that._criteria);
              }
            
            }
            
            if(popupContent.length > 0){
              popup.setContent(popupContent).openOn(map);
            }
          });
        }
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
	        	this._map.closePopup();
	        	
	    		var toRemove = this._layerCache.get(id);
	    		// remove layer from our cache
	            this._layerCache.remove(id);
	            // remove the layer from the map and UI
	            el.parent().parent().remove();
	            
	            // remove the actual layer from the map
	        	if(toRemove.leafletLayer){
	        		this._map.removeLayer(toRemove.leafletLayer);
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
	    		
	            // remove the leaflet map layer from the map
	        	if(toDisable.leafletLayer){
	        		this._map.removeLayer(toDisable.leafletLayer);
	        	}
	    		
	            // change the ui back to disabled
	        	$(el).hide();
	        	$(el).parent().find(".ico-edit").hide();
	        	$(el).parent().find(".ico-enable").show();
	        	$(el).parent().parent().find(".check").removeClass("checked").hide();
	    		//this._drawReferenceLayersHTML(); 
	        	
	        	$('*[data-parentlayerid="'+ $(el).data("id") +'"]').remove();
	    	}
      },
      
      
      /**
       * Closes the overlay with the layer/style CRUD.
       * 
       */
      _closeLayerModal : function(){
        $(DynamicMap.LAYER_MODAL).modal('hide').html('');
      },
      
      
      /**
       * Called when a user submits (creates/updates) a layer with styles.
       * 
       * @params
       */
      _applyWithStyleListener : function(params){
        
        var that = this;
        
        var request = new com.runwaysdk.geodashboard.StandbyClientRequest({
          onSuccess : function(htmlOrJson, response){
            if (response.isJSON()) {
              that._closeLayerModal();
              
              var layerType;
              var returnedLayerJSON = JSON.parse( htmlOrJson);
              if(returnedLayerJSON.layerType === "REFERENCELAYER"){
            	  layerType = "refLayers";
              }
              else{
            	  layerType = "layers";
              }
              var jsonObj = {};
              jsonObj[layerType] = [Mojo.Util.toObject(htmlOrJson)];
              
              that._updateCacheFromJSONResponse(jsonObj);
              
              // Redraw the HTML
              if(returnedLayerJSON.layerType === "REFERENCELAYER"){
            	  that._drawReferenceLayersHTML();
              }
              else{
            	  that._drawUserLayersHTML();
            	  
              	  // Close any info window popups if they exist
              	  that._map.closePopup();
              }
              
              // Update leaflet
              that._addUserLayersToMap(true);            
              
              // TODO : Push this somewhere as a default handler.
              that.handleMessages(response);
            }
            else if (response.isHTML()) {
              // we got html back, meaning there was an error
              
              that._displayLayerForm(htmlOrJson);
              that._addLayerFormControls();
              $(DynamicMap.LAYER_MODAL).animate({scrollTop:$('.heading').offset().top}, 'fast'); // Scroll to the top, so we can see the error
            }
          },
          onFailure : function(e){
            that.handleException(e);
          }
        }, $(DynamicMap.LAYER_MODAL)[0]);
        
        var layer = this._layerCache.get(params["layer.componentId"]);
        var mdAttribute = null;
        if (layer != null) 
        {
          mdAttribute = layer.getValue('mdAttribute');
        }
        else 
        {
          mdAttribute = this._currentAttributeId;
        }
        
        params['mapId'] = this._mapId;
        params['layer.mdAttribute'] = mdAttribute;
        
        // Custom conversion to turn the checkboxes into boolean true/false
        params['style.enableLabel'] = params['style.enableLabel'].length > 0;
        
        // A hack to set the enableValue property which is required on DashboardLayer but is
        // not allowed for the reference layer form since ther is no 'value' to display
        if(!params['style.enableValue']){
        	params['style.enableValue'] = false;
        }
        else{
        	params['style.enableValue'] = params['style.enableValue'].length > 0;
        }
        params['layer.displayInLegend'] = params['layer.displayInLegend'].length > 0;
        
        // Check for existense of dynamic settings which may not exist 
        if(params['style.bubbleContinuousSize']){
          params['style.bubbleContinuousSize'] = params['style.bubbleContinuousSize'].length > 0;
        }
        
        if($("#f79").is(":visible")){
          params['style.pointFixedSize'] = params['style.pointFixedSize'];
          params['style.pointFixed'] = true;
        }
        
        var conditions = this._getConditionsFromCriteria(this._criteria);
        
        $.each(conditions, function( index, condition ) {
          params["conditions_" + index + ".comparisonValue"] = condition.getValue('comparisonValue');
          params["conditions_" + index + ".isNew"] = "true";
          params["#conditions_" + index + ".actualType"] = condition.getType();
          
          if(condition instanceof com.runwaysdk.geodashboard.gis.persist.condition.DashboardAttributeCondition) {
            params["conditions_" + index + ".definingMdAttribute"] = condition.getValue('definingMdAttribute');  
          }
        });      
        
        if($("#tab004categories").is(":visible")){
          var catStyleArr = this._updateCategoriesJSON();
          params['style.styleCategories'] = JSON.stringify(catStyleArr);
        }
        
        
        var secondaryAttribute = $("#secondaryAttribute").val();
        
        if(secondaryAttribute != null && secondaryAttribute.length > 0) {
          var secondaryCategories = this._getSecondaryAttriubteCategories();
          var secondaryAggregationType = $("#secondaryAggregation").val();
            
          if(secondaryCategories != null && secondaryCategories.length > 0){
            params['style.secondaryAttribute'] = secondaryAttribute;
            params['style.secondaryAggregationType'] = secondaryAggregationType;
            params['style.secondaryCategories'] = JSON.stringify(secondaryCategories);
          }          
        }
        else {
          params['style.secondaryAttribute'] = '';
          params['style.secondaryAggregationType'] = '';
          params['style.secondaryCategories'] = '';
        }
          
        
        return request;
      },
      

      _getCategoryType : function() {
        var categoryType = null;
        
        $.each($('.category-input'), function(index, element) {
          categoryType = $(element).data("type");
        });
        
        return categoryType;
      },
      
      
      /**
       * 
       * @params
       */
      _cancelLayerListener : function(params){
        var that = this;
        
        if(params['layer.isNew'] === 'true')
        {
          this._closeLayerModal();
        }
        else
        {
          var that = this;
          var request = new Mojo.ClientRequest({
            onSuccess : function(params){
              that._closeLayerModal();
            },
            onFailure : function(e){
              that.handleException(e);
            }
          });
          
          //return request;
          var id = params['layer.componentId'];
          com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.unlock(request, id);
        }
      },
      
      
      /**
       * 
       * @params
       */
      _cancelReferenceLayerListener : function(params){
        var that = this;
        
        if(params['layer.isNew'] === 'true')
        {
          this._closeLayerModal();
        }
        else
        {
          var that = this;
          var request = new Mojo.ClientRequest({
            onSuccess : function(params){
              that._closeLayerModal();
            },
            onFailure : function(e){
              that.handleException(e);
            }
          });
          
          //return request;
          var id = params['layer.componentId'];
          com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayer.unlock(request, id);
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
       * Return all allowable base maps.
       * 
       */
      _getBaseLayers : function(){
        
        // the SATELLITE layer has all 22 zoom level, so we add it first to
        // become the internal base layer that determines the zoom levels of the
        // map.
        
        //var gsat = new L.Google('SATELLITE');         
        var gphy = new L.Google('TERRAIN');       
        var gmap = new L.Google('ROADMAP');       
        var ghyb = new L.Google('HYBRID');
        
        var osm = new L.TileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'); 
        osm._gdbcustomtype = 'OSM';
        
        var base = [osm, gmap, ghyb, gphy];
        
        return base;
      },
      
      
      /**
       * Renders each base layer as a checkable option in
       * the layer switcher.
       * 
       * @base - array of leaflet basemap layer objects
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
            var label = '';        
            if(b._type === 'ROADMAP'){
              label = this.localize("googleStreets");
            }
            else if(b._type === 'SATELLITE'){
              label = this.localize("googleSatellite");
            }
            else if(b._type === 'TERRAIN'){
               label = this.localize("googleTerrain"); 
            }
            else if(b._type === 'HYBRID'){
              label = this.localize("googleHybrid");
            }
            else if(b._gdbcustomtype === 'OSM'){
              label = this.localize("osmBasic");
            }
            
            com.runwaysdk.event.Registry.getInstance().removeAllEventListeners(id);
            var checkbox = this.getFactory().newCheckBox({checked: false, classes: ["row-form", "jcf-class-check", "chk-area"]});
            checkbox.setId(id);
            if(i === 0){
              checkbox.setChecked(checkbox);
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
              this._map.removeLayer(otherBaselayer);
            }
            else {
              var newBaselayer = this._baseLayers.get(targetId);
              this._map.addLayer(newBaselayer);
              
              // The osm tileLayer isnt set at the bottom by default so this sets it as so
              if(newBaselayer._gdbcustomtype === "OSM"){
              newBaselayer.bringToBack();
              }
            }
          }
        }
        else {
          var unchecklayer = this._baseLayers.get(targetId);
          this._map.removeLayer(unchecklayer);
        }
      },
      
      
      /**
       * Toggles the overlay layers of the map.
       * 
       * @e
       */
      _toggleOverlayLayer : function(e){
    	  
    	// Close any info window popups if they exist
    	this._map.closePopup();
    	  
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
          this._map.removeLayer(layer.leafletLayer);          
          if(layer.getDisplayInLegend())
          {
            layer.layerLegend.hide()
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
          this._map.removeLayer(layer.leafletLayer);          
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

            that._map.setView(new L.LatLng(lonlat[1], lonlat[0]), 13);
            
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
       * Hooks the auto-complete functionality to the category field input fields
       * 
       * 
       */
      _addCategoryAutoComplete : function(parent, aggregationId){
        
        var that = this;
        
        // Hook up the auto-complete for category input options new layers
        // existing layers have a seperate autocomplete hookup that queries 
        // the layer database view directly. 
        $(parent).find('.category-input').each(function(){
          var mdAttribute = $(this).data('mdattributeid');  
          var categoryType = $(this).data('type');
          
          $(this).autocomplete({
            source: function( request, response ) {
              var req = new Mojo.ClientRequest({
                onSuccess : function(results){
                  
                  // We need to localize the results for numbers
                  if(categoryType == 'number') {
                    for(var i = 0; i < results.length; i++) {
                      var number = parseFloat(results[i]);
                      var localized = that._formatter(number);
                      
                      results[i] = localized;
                    }
                  }
                  
                  response( results );
                },
                onFailure : function(e){
                  that.handleException(e);
                }
              });
              
              // values are scraped from hidden input elements on the layer create form
              var universalId = $("#f58").val();
              var aggregationVal = $(aggregationId).val();
              var criteria = that._reloadCriteria();
              var conditions = that._getConditionsFromCriteria(criteria);
              
              com.runwaysdk.geodashboard.Dashboard.getCategoryInputSuggestions(req, mdAttribute, universalId, aggregationVal, request.term, 10, conditions);
            },
            minLength: 1
          });
        });
      },
      
      _renderSecondaryAggregation : function(type) {
        
        var options = this._aggregationMap[type];
      
        if(options == null) {
          options = [];
        }
      
        var html = '<select id="secondaryAggregation" class="method-slect" name="secondaryAggregation">';
        
        for(var i = 0; i < options.length; i++) {
          var option = options[i];
          
          html += '<option value="' + option.value + '">' + option.label + '</option>';
        }
        
        html += '</select>';
        
        $("#secondary-aggregation-container").html(html);
        
        // Set the saved value
        var value = $('#secondaryAggregationValue').val();        
        $("#secondaryAggregation").val(value);
      },
      
      _renderSecondaryCategoryGroup : function(mdAttributeId, type) {
        $('#secondary-content').hide();
        
        this._renderSecondaryAggregation(type);
        
        var categoryType = this._getCategoryType(type);
        
        var html =
          '<div class="color-section">' +
            '<div class="heading-list">' +
              '<span>' + com.runwaysdk.Localize.localize("DashboardLayer.form", "category") + '</span>' +
              '<span>' + com.runwaysdk.Localize.localize("DashboardLayer.form", "color") + '</span>' +
              '<span></span>'+
            '</div>' +
            '<div class="category-block">' +
              '<div class="panel-group choice-color category-group">' +
                '<div class="panel">' +
                  '<div id="secondary-choice-color" class="panel-collapse">' +
                    '<ul class="color-list">' +
                      this._getSecondaryCategoryHTML(1, mdAttributeId, categoryType) +
                      this._getSecondaryCategoryHTML(2, mdAttributeId, categoryType) +
                      this._getSecondaryCategoryHTML(3, mdAttributeId, categoryType) +
                      this._getSecondaryCategoryHTML(4, mdAttributeId, categoryType) +
                      this._getSecondaryCategoryHTML(5, mdAttributeId, categoryType) +
                    '</ul>' +
                  '</div>' +                              
                '</div>' +              
              '</div>' +            
            '</div>' +
          '</div>';          
                 
        $('#secondary-cateogries').html(html);
        
        this._addCategoryAutoComplete('#secondary-content',"#secondaryAggregation");
        
        this._setupCategoryColorPicker($('#secondary-content').find('.color-holder'));
        
        jcf.customForms.replaceAll($('#secondary-content').get(0));
        
        $('#secondary-content').show();        
      },
      
      _getSecondaryCategoryHTML : function(index, mdAttributeId, categoryType) {
        var fillLabel =  com.runwaysdk.Localize.localize("DashboardThematicLayer.form", "fill");  
                
        var html = '<li>' +
          '<div class="category-container">' +
            '<div class="text category-input-container">' +
              '<input id="secondaryCat' + index + '" data-mdattributeid="' + mdAttributeId + '" data-type="' + categoryType+ '" class="category-input" name="" type="text" value="" placeholder="' + fillLabel +'" autocomplete="on" >' +
            '</div>' +
            '<div class="cell">' +
              '<div class="color-holder">' +
                '<a href="#" class="color-choice">' +
                  '<span id="secondaryCat' + index + '-color-selector" class="ico cat-color-selector" style="background:#000000">icon</span>' +
                  '<span class="arrow">arrow</span>' +
                '</a>' +
              '</div>' +
            '</div>' +
          '</div>' +
        '</li>';
          
        return html;        
      },
      
      _renderSecondaryTermTree : function(mdAttributeId, type) {
        $('#secondary-content').hide();
        
        this._renderSecondaryAggregation(type);
        
        var html =
         '<div class="color-section">' +
           '<div class="heading-list">' +
             '<span>' + com.runwaysdk.Localize.localize("DashboardLayer.form", "category") + '</span>' +
             '<span>' + com.runwaysdk.Localize.localize("DashboardLayer.form", "color") + '</span>' +
             '<span></span>'+
           '</div>' +              
           '<div class="category-block">' +
             '<div class="ontology-category-input-container">' +
             '<div id="secondary-tree"></div>' +
           '</div>' +
         '</div>';
            
        $('#secondary-cateogries').html(html);

        // Get the term roots and setup the tree widget
        var that = this;
        var req = new Mojo.ClientRequest({
          onSuccess : function(results){
            var nodes = JSON.parse(results);
            var rootTerms = [];
                
            for(var i = 0; i < nodes.length; i++) {
              var id = nodes[i].id;              
              rootTerms.push({termId : id});
            }
                
            that._renderCategoryTermTree("#secondary-tree", rootTerms, '#secondaryCategories', nodes);
            
            $('#secondary-content').show();
          },
          onFailure : function(e){
            that.handleException(e);
          }
        });
        
        jcf.customForms.replaceAll($('#secondary-content').get(0));
            
        com.runwaysdk.geodashboard.Dashboard.getClassifierTree(req, mdAttributeId);            
      },
      
      _handleSecondaryChange : function(e){
        var option = e.target.selectedOptions[0];
        var mdAttributeId = option.value;
        var type = $(option).data("type");
        
        if(mdAttributeId == '') {
          $('#secondary-content').hide();
        }
        else if(type == 'com.runwaysdk.system.metadata.MdAttributeTerm') {
          this._renderSecondaryTermTree(mdAttributeId, type);
        }
        else {
          this._renderSecondaryCategoryGroup(mdAttributeId, type);
        }
      },
      
      _getCategoryType : function(type) {
        if(type == 'com.runwaysdk.system.metadata.MdAttributeDouble' || type == 'com.runwaysdk.system.metadata.MdAttributeInteger') {
          return 'number';
        }
        else if(type == 'com.runwaysdk.system.metadata.MdAttributeDate') {
          return 'date';
        }
        
        return 'text';
      },
      
      _addLayerFormControls : function(){
        
        $("#secondary-select-box").change(Mojo.Util.bind(this, this._handleSecondaryChange));
        
        if($("#ontology-tree").length > 0){
          this._renderLayerTermTree();
          attachCategoryColorPickers();
        }
        else if($(".category-input").length > 0){
          this._addCategoryAutoComplete('#category-colors-container', '#f59');
          this._loadExistingCategories("#categories-input", "cat", "#ontology-tree", true);
            
          // category 'other' option
          $("#f53").change(function() {
            if($(this).is(":checked")) {
              $("#cat-other").parent().parent().show();
            }
            else{
              $("#cat-other").parent().parent().hide();
            }     
          });
          
          attachCategoryColorPickers();
        }
        
        function attachCategoryColorPickers(){
          // ontology category layer type colors
          $(".category-color-icon").colpick({
            submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
            onChange:function(hsb,hex,rgb,el,bySetColor) {
              $(el).css('background','#'+hex);
              $(el).find('.color-input').attr('value', '#'+hex);
            }
          });
        }
        
        // Load the secondary values
        var secondaryAttribute = $("#secondaryAttribute").val();
        
        if(secondaryAttribute != null && secondaryAttribute.length > 0) {

          var type = $('#secondaryAttribute').find(":selected").data('type');
          
          if(type == 'com.runwaysdk.system.metadata.MdAttributeTerm') {          
          
            this._renderSecondaryTermTree(secondaryAttribute, type);
          }
          else {
            this._renderSecondaryCategoryGroup(secondaryAttribute, type);
          
            this._loadExistingCategories("#secondaryCategories", "secondaryCat", "#secondary-tree", false);

          }
        }
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
         * Called when a user submits a new dashboard.
         * 
         */
        _applyDashboardListener : function(){
          
          var that = this;
          
          var request = new com.runwaysdk.geodashboard.StandbyClientRequest({
            onSuccess : function(html, response){
              if (response.isJSON()) {
                that._closeDashboardModal();
                
                window.location = "?dashboard=" + html.getId();
              }
              else if (response.isHTML()) {
                // we got html back, meaning there was an error
                that._displayDashboardForm(html);
              }
            },
            onFailure : function(e){
              that.handleException(e);
            }
          }, $(DynamicMap.DASHBOARD_MODAL)[0]);
                   
          return request;
        },
        
        /**
         * Closes the new dashboard CRUD.
         * 
         */
        _closeDashboardModal : function(){
          $(DynamicMap.DASHBOARD_MODAL).modal('hide').html('');
        },
      
      /**
       * Gets the html for and calls the layer creation/edit form 
       * 
       * @e 
       */
      _openLayerForAttribute : function(e){
        e.preventDefault();      
        
        var el = $(e.currentTarget);
        var attrId = el.data('id');
        this._currentAttributeId = attrId;

        var that = this;
        
        var request = new Mojo.ClientRequest({
          onSuccess : function(html){
            that._displayLayerForm(html);
            that._addLayerFormControls();
            
            //
            // See form.jsp in DashboardLayer for category color pickers event listener configuration
            //
          },
          onFailure : function(e){
            that._closeLayerModal();
            that.handleException(e);
          }
        });
        
        this._LayerController.newThematicInstance(request, this._currentAttributeId, this._mapId);
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

          var request = new Mojo.ClientRequest({
            onSuccess : function(html){
              that._displayLayerForm(html);
              that._addLayerFormControls();
              
            },
            onFailure : function(e){
              that._closeLayerModal();
              that.handleException(e);
            }
          });
          
          this._ReferenceLayerController.newReferenceInstance(request, universalId, this._mapId);
        },
        
      	
      /**
       * Renders the layer creation/edit form
       * 
       * @html
       */
      _displayLayerForm : function(html){
        
        var that = this;
        
        // clear all previous color picker dom elements
        $(".colpick.colpick_full.colpick_full_ns").remove();
        
        // Show the white background modal.
        var modal = $(DynamicMap.LAYER_MODAL).first();
        modal.html(html);
        
        jcf.customForms.replaceAll(modal[0]);
        
        // Add layer styling event listeners
        this._selectColor();
        this._selectLayerType();
        
        // Move reusable cells to active cell holder
        var activeTab = $("#layer-type-styler-container").children(".tab-pane.active")[0].id;
        if (activeTab === "tab001basic") {
          this._attachDynamicCells($("#gdb-reusable-basic-stroke-cell-holder"), $("#gdb-reusable-basic-fill-cell-holder"));
        }
        else if (activeTab === "tab003gradient") {
          this._attachDynamicCells($("#gdb-reusable-gradient-stroke-cell-holder"), $("#gdb-reusable-gradient-fill-cell-holder"));
        }
        else if (activeTab === "tab004categories") {
          this._attachDynamicCells($("#gdb-reusable-categories-stroke-cell-holder"), $("#gdb-reusable-categories-fill-cell-holder"));
          
          // Hide the reusable input cells that don't apply to categories
          var polyFillOpacity = $("#gdb-reusable-cell-polygonFillOpacity");
          polyFillOpacity.hide();
        }
        
        // Attach listeners
        $('a[data-toggle="tab"]').on('shown.bs.tab', Mojo.Util.bind(this, this._onLayerTypeTabChange));
     
        // Attach event listeners for the universal (geo) aggregation dropdown.
        $("#f58").change(function(){ 
          if($("#f58 option:selected").hasClass("universal-leaf")){
            // Hide the attribute aggregation dropdown because aggregations are irrelevant at this level of universal
            $("#f59").parent().parent().hide();
          }
          else{
            $("#f59").parent().parent().show();
          }
        });
        
        // Localize any existing number cateogry values
        $.each($('.category-input'), function() {
          var value = $(this).val();
          if(value != null && value.length > 0) {
            var categoryType = $(this).data("type");
            if(categoryType == "number") {
              var number = parseFloat(value);
              var localized = that._formatter(number);
              
              $(this).val(localized);
            }
          }
        });    
        
        // IMPORTANT: This line must be run last otherwise the user will see javascript loading and modifying the DOM.
        //            It is better to finish all of the DOM modification before showing the modal to the user
        modal.modal('show');
      },
      
      /**
       * Renders the term tree on the layer form for ontology categories
       * 
       */
      _renderLayerTermTree : function() {
        
        var elementId = "#ontology-tree";
        var storeId = "#categories-input";
        var rootsJSON = $("#ontology-tree").data("roots");
        var rootTerms = rootsJSON.roots;
        
        this._renderCategoryTermTree(elementId,rootTerms, storeId);
      },
        
      _renderCategoryTermTree : function(elementId, rootTerms, storeId, nodes) {
        var that = this;
        
        var tree = new com.runwaysdk.geodashboard.ontology.OntologyTree({
          termType : "com.runwaysdk.geodashboard.ontology.Classifier",
          relationshipTypes : [ "com.runwaysdk.geodashboard.ontology.ClassifierIsARelationship" ],
          rootTerms : rootTerms,
          editable : false,
          slide : false,
          selectable : false,
          onCreateLi: function(node, $li) {
            var nodeId = node.id;

            var catColor = that._getCategoryColor(nodeId, elementId, storeId);
          
            var thisLi = $.parseHTML(
              '<a href="#" class="color-choice" style="float:right; width:20px; height:20px; padding: 0px; margin-right:15px; border:none;">' +
                '<span data-rwId="'+ nodeId +'" class="ico ontology-category-color-icon" style="background:'+catColor+'; border:1px solid #ccc; width:20px; height:20px; float:right; cursor:pointer;">icon</span>' +
              '</a>');

            // Add the color icon for category ontology nodes              
            $li.find('> div').append(thisLi)
              
            // ontology category layer type colors
            $(thisLi).find("span").colpick({
              submit: 0,  // removes the "ok" button which allows verification of selection and memory for last color
              onChange: function(hsb,hex,rgb,el,bySetColor) {
                var hexStr = '#'+hex;
                $(el).css('background', hexStr);
                $(el).next(".color-input").attr('value', hexStr);                                  
              }
            });
          },
          /* checkable: true, */
          crud: {
            create: { // This configuration gets merged into the jquery create dialog.
              height: 320
            },
            update: {
              height: 320
            }
          }
        });
        tree.render(elementId, nodes);
      },
      
      /**
       * Gets the hex color code for an ontology category based on the node id 
       * 
       * @paarm nodeId - id of the node in the ui to get the color for.
       */
      _getCategoryColor : function(nodeId, elementId, storeId) {
          var catsJSONObj = $(storeId).data("categoriesstore");
          
          if(catsJSONObj){

            var catsJSONArr = catsJSONObj.catLiElems;              
            if(catsJSONArr == null && Array.isArray(catsJSONObj)) {
              catsJSONArr = catsJSONObj;
            }          

            if($(elementId).length > 0){
              for(var i=0; i<catsJSONArr.length; i++){
                var cat = catsJSONArr[i];
                var catId = cat.id;
                
                if(catId === nodeId){
                  return cat.color;
                }
              }
            }
          }
          // if no match is found return the default
          return "#00bfff";
      },
      
      
      /**
       * Adds the value and color setting for saved categories on the layer create/edit form.
       * The categories data is appended to the #categories-input element as json.
       * 
       */
      _loadExistingCategories : function(storeElement, prefix, treeElement, checkOther) {
        
        var catsJSONObj = $(storeElement).data("categoriesstore");
        
        if(catsJSONObj){
          var catsJSONArr = catsJSONObj.catLiElems;
          
          if(catsJSONArr == null && Array.isArray(catsJSONObj)) {
            catsJSONArr = catsJSONObj;
          }          
          
          if($(treeElement).length === 0)
          {
            var catInputId;
            var catOtherEnabled = true;
            
            for(var i=0; i<catsJSONArr.length; i++){
              var cat = catsJSONArr[i];
              
              // Controlling for 'other' category 
              if(cat.otherCat){
                catInputId = prefix + "-other";
              }
              else{
                catInputId = prefix + (i+1);
              }
              
              var catColorSelectorId = catInputId + "-color-selector";
              $("#"+catInputId).val(cat.val);
              $("#"+catColorSelectorId).css("background", cat.color);
              catOtherEnabled = cat.otherEnabled;
            }
            
            // Simulate a checkbox click to turn off the checkbox if the 'other' option is disabled
            // The 'other' option is checked by default making this a valid sequence
            if(checkOther && !catOtherEnabled){
              $("#f53").click();
              $("#" + prefix + "-other").parent().parent().hide();
            }
          }
        }
      },
      
      
      _attachDynamicCells : function(strokeCellHolder, fillCellHolder) {
        if(strokeCellHolder != null) {        
          var polyStroke = $("#gdb-reusable-cell-polygonStroke");
          strokeCellHolder.append(polyStroke);
          polyStroke.show();
        
          var polyStrokeWidth = $("#gdb-reusable-cell-polygonStrokeWidth");
          strokeCellHolder.append(polyStrokeWidth);
          polyStrokeWidth.show();
        
          var polyStrokeOpacity = $("#gdb-reusable-cell-polygonStrokeOpacity");
          strokeCellHolder.append(polyStrokeOpacity);
          polyStrokeOpacity.show();
        }
        
        if(fillCellHolder != null) {
          var polyFillOpacity = $("#gdb-reusable-cell-polygonFillOpacity");
          fillCellHolder.append(polyFillOpacity);
          polyFillOpacity.show();
        }
      },          
      
      _onLayerTypeTabChange : function(e) {
        var activeTab = e.target;
        
        var type = activeTab.dataset["gdbTabType"];
        
        if (type === "BASIC") {
          this._attachDynamicCells($("#gdb-reusable-basic-stroke-cell-holder"), $("#gdb-reusable-basic-fill-cell-holder"));
          $("#tab001basic").show();
        }
        else if (type === "BUBBLE") {
          $("#tab002bubble").show();
        }
        else if (type === "GRADIENT") {
          this._attachDynamicCells($("#gdb-reusable-gradient-stroke-cell-holder"), $("#gdb-reusable-gradient-fill-cell-holder"));
          $("#tab003gradient").show();
        }
        else if (type === "CATEGORY") {
          $("#tab004categories").show();
          this._attachDynamicCells($("#gdb-reusable-categories-stroke-cell-holder"), null);
        }
        else if (type === "FIXEDBUBBLE") {
            $("#tab005bubble").show();
            this._attachDynamicCells($("#gdb-reusable-categories-stroke-cell-holder"), $("#gdb-reusable-categories-fill-cell-holder"));
        }
      },
      
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
      
      _setupCategoryColorPicker : function(elements) {
        
        // color dropdown buttons
        elements.colpick({
          submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
          onShow:function(colPickObj) {
            var currColor = GDB.gis.DynamicMap.prototype.rgb2hex($(this).find(".ico").css("background-color"));
            $(this).colpickSetColor(currColor,false);
          },
          onChange:function(hsb,hex,rgb,el,bySetColor) {
            $(el).find(".ico").css('background','#'+hex);
            $(el).find('.color-input').attr('value', '#'+hex);
          }
        }); 
      },
      
      /**
       * Handles the selection of colors from the color picker 
       * 
       * 
       */
      _selectColor : function(){
        var that = this;
        
        this._setupCategoryColorPicker($('.color-holder'));
        
        // category layer type colors
        $("#category-colors-container").find('.icon-color').colpick({
          submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
          onChange:function(hsb,hex,rgb,el,bySetColor) {
            $(el).css('background','#'+hex);
            $(el).find('.color-input').attr('value', '#'+hex);          
          }
        });
        
        // ontology category layer type colors
        $(".ontology-category-color-icon").colpick({
          submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
          onChange:function(hsb,hex,rgb,el,bySetColor) {
            $(el).css('background','#'+hex);
            $(el).next(".color-input").attr('value', '#'+hex);
          }
        });
        
      },
      
      /**
       * Handles the selection of layer type representation in the layer create/edit form
       * i.e. basic, bubble, gradient, category
       * 
       */
      _selectLayerType : function(){
        var layerType = com.runwaysdk.geodashboard.gis.persist.DashboardLayer.LAYERTYPE;
        $('input:radio[name="layer.'+layerType+'"]').change(function(){   
              
              var targetRadio = $(this);
              
              // hide all the styling options
              $.each($('.tab-pane'), function(){
                if($(this).is(":visible")){
                  $(this).hide(); 
                }
              });
            
              // add the relevant styling options for the layer type
              if (targetRadio.attr("id") === "radio1") {
                  $("#tab001").show();
              }
              else if (targetRadio.attr("id") === "radio2") {
                  $("#tab002").show();
              }
              else if (targetRadio.attr("id") === "radio3") {
                  $("#tab003").show();
              }
              else if (targetRadio.attr("id") === "radio4") {
                  $("#tab004").show();
              }
        });
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
        that._map.closePopup();
        
        // Validate there are no existing errors
        var errorCount = $('.gdb-attr-filter.field-error').length
        
        if(errorCount > 0) {
          var message = com.runwaysdk.Localize.localize("filter", "error");
          
          that._renderMessage(message);
        }
        else {
          this._criteria = this._reloadCriteria();
          var conditions = this._getConditionsFromCriteria(this._criteria);
            
          var clientRequest = new Mojo.ClientRequest({
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
          });
            
          com.runwaysdk.geodashboard.gis.persist.DashboardMap.updateConditions(clientRequest, this._mapId, conditions);
          
          this._currGeoId = '';
            
          this._renderReport(this._currGeoId, this._criteria);
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
      
      _setReportPanelHeight : function (height) {
        var current = $("#reporticng-container").height();
        var toolbar = $("#report-toolbar").height();        
        
        // Minimize
        if(current > height)
        {
          var difference = (current - height);
          
          $("#reporticng-container").animate({ bottom: "-=" + difference + "px" }, 1000, function(){
            
            $("#reporticng-container").css("bottom", "0px");                                                  
            $("#report-viewport").height(height-toolbar);
            $("#reporticng-container").height(height);
          });          
        }
        // Maximize
        else if (current < height){
          var difference = (height - current);
          
          $("#reporticng-container").height(height);
          $("#report-viewport").height(height-toolbar);
          $("#reporticng-container").css("bottom", "-" + difference + "px");
              
          $("#reporticng-container").animate({ bottom: "+=" + difference + "px" }, 1000, null);
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
        $('a.apply-filters-button').on('click', Mojo.Util.bind(this, this._onClickApplyFilters));
        $('a.save-filters-button').on('click', Mojo.Util.bind(this, this._onClickSaveFilters));
        $('a.save-global-filters-button').on('click', Mojo.Util.bind(this, this._onClickSaveGlobalFilters));
        
        // Reporting events
        $('.report-export').on('click', Mojo.Util.bind(this, this._onClickExportReport));        
        $('#report-upload').on('click', Mojo.Util.bind(this, this._onClickUploadReport));
        
        // Max
        $('#report-max').on('click', function(){
          var height = $("#mapDivId").height();
            
          that._setReportPanelHeight(height);
        });
        
        // Split
        $('#report-split').on('click', function(){
          var height = Math.floor($("#mapDivId").height() / 2);
              
          that._setReportPanelHeight(height);
        });
        
        // Min
        $('#report-min').on('click', function(){
          that._setReportPanelHeight(30);
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
          if (!that._validateNumeric(this.value)) {            
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
              
            com.runwaysdk.geodashboard.Dashboard.getGeoEntitySuggestions(req, request.term, 10);
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
