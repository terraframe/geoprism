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
   "aggregateValue" : "Aggregation Value"
  });
  
  var DynamicMap = Mojo.Meta.newClass(GDB.Constants.GIS_PACKAGE+'DynamicMap', {
    
  Extends : Component,
    
    Constants : {
      BASE_LAYER_CONTAINER : 'baseLayerContainer',
      OVERLAY_LAYER_CONTAINER : 'overlayLayerContainer',
      GEOSERVER_WORKSPACE : 'geodashboard',
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
        
        this._googleEnabled = Mojo.Util.isObject(Mojo.GLOBAL.google);
        
        this._config = config;
        this._mapDivId = config.mapDivId;
        this._mapId = config.mapId;
        this._dashboardId = config.dashboardId;
        this._criteria = []; // Default criteria for filtering
        
        this._defaultOverlay = null;
        this._currentOverlay = null;
        this._map = null;
        
        // LinkedHashMap<runwayId, DashboardLayerView>
        this._layerCache = new com.runwaysdk.structure.LinkedHashMap();
        
        // The current base map (only one at a time is allowed)
        this._defaultBase = null;
        this._currentBase = null;
        this._baseLayers = new com.runwaysdk.structure.HashMap();
        
        this._suggestionCoords = new com.runwaysdk.structure.HashMap();      
        this._autocomplete = null;
        this._responseCallback = null;
        this._bBox = null;
        
        var bound = Mojo.Util.bind(this, this._overlayHandler);
        overlayLayerContainer.on('click', 'a', bound);
        
        var dashboardBound = Mojo.Util.bind(this, this._dashboardClickHandler);
        $(".gdb-dashboard").on("click", dashboardBound); 
        
        this._LayerController = com.runwaysdk.geodashboard.gis.persist.DashboardLayerController;
        this._DashboardController = com.runwaysdk.geodashboard.DashboardController;
        this._ReportController = com.runwaysdk.geodashboard.report.ReportItemController;
        
        // set controller listeners
        this._LayerController.setCancelListener(Mojo.Util.bind(this, this._cancelLayerListener));
        this._LayerController.setApplyWithStyleListener(Mojo.Util.bind(this, this._applyWithStyleListener));       
        this._DashboardController.setCancelListener(Mojo.Util.bind(this, this._cancelDashboardListener));
        this._DashboardController.setCreateListener(Mojo.Util.bind(this, this._applyDashboardListener));
        
        overlayLayerContainer.sortable({
          update: Mojo.Util.bind(this, this._overlayLayerSortUpdate)
        });
        overlayLayerContainer.disableSelection();      
      },
      
      /**
       * Retrieves new data from the server and then refreshes everything on the map with the new data.
       */
      fullRefresh : function() {
        var that = this;
        
        that._renderMap();
        
        this._updateCachedData(function(){
          that._configureMap();
          that._renderBaseLayers();
          that._renderUserLayers();
        });
        
        that._renderReport('', that._criteria);
      },
      
      /**
       * Requests all map data again from the server (bounds, persisted layers, etc...) and updates our internal caches.
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
       */
      _updateCacheFromJSONResponse : function(json) {
        // TODO : For now, we can't actually return an aggregate view (Runway doesn't support them yet), so we're returning JSON.
        //          Since we're using JSON, we have to create DashboardLayerView objects here.
        for (var i = 0; i < json.layers.length; ++i) {
          var layer = json.layers[i];
          
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
          view.setLayerIsActive(true);
          view.setAggregationMethod(layer.aggregationMethod);
          view.setAggregationAttribute(layer.aggregationAttribute);
          
          view.style = layer.styles[0];
          
          var oldLayer = this._layerCache.get(layer.layerId);
          if (oldLayer != null) {
            view.leafletLayer = oldLayer.leafletLayer;
          }
          this._layerCache.put(layer.layerId, view);
        }
        
        if (json.bbox != null) {
          this._bBox = json.bbox;
        }
      },
           
      /**
       * Creates a new Leaflet map. If one already exists the existing one will be cleaned up and removed.
       */
      _renderMap : function() {
        if (this._map != null) {
          $('#'+DynamicMap.BASE_LAYER_CONTAINER).html('');
          this._map.remove();
          $('#'+this._mapDivId).html('');
        }
        
        this._map = new L.Map(this._mapDivId, {zoomAnimation: false, zoomControl: true});
        
        // Add attribution to the map
        this._map.attributionControl.setPrefix('');
        //this._map.attributionControl.addAttribution("TerraFrame | GeoDashboard");
        
        // Hide mouse position coordinate display when outside of map
        this._map.on('mouseover', function(e) {
          $(".leaflet-control-mouseposition.leaflet-control").show();
        });
        
        this._map.on('mouseout', function(e) {
          $(".leaflet-control-mouseposition.leaflet-control").hide();
        });
        
        L.control.mousePosition({emptyString:"",position:"bottomleft",prefix:"Lat: ",separator:" Long: "}).addTo(this._map);
        
        var mapClickHandlerBound = Mojo.Util.bind(this, this._mapClickHandler);
        this._map.on("click", mapClickHandlerBound);
      },
      
      /**
       * Creates a new BIRT report. If one already exists the existing one will be cleaned up and removed.
       */
      _renderReport : function(geoId, criteria) {
        var request = new com.runwaysdk.geodashboard.StandbyClientRequest({
          that : this,
          onSuccess : function(html){
            this.that._displayReport(html);
          },
          onFailure : function(e){
            this.that.handleException(e);
          }
        }, $( "#report-content" )[0]);
        
        this._ReportController.run(request, this._dashboardId, geoId, JSON.stringify(criteria));        
      },
      
      _displayReport : function(html) {
        $( "#report-content" ).html(html);
      },      
            
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
        this._drawUserLayersHMTL();
        this._addUserLayersToMap();
      },
      
      /**
       * Draws legend entries based on the layer cache
       * 
       */
      _drawLegendItems : function() {
        
        $(".legend-container").remove();            
        
          var layers = this._layerCache.values();
          
          for(var i = layers.length-1; i >= 0; i--){
          var layer = layers[i];
            
          var displayInLegend = layer.getDisplayInLegend();

          if(displayInLegend){
              var layerId = layer.getLayerId();
              var legendId = "legend_" + layerId;
              var displayName = layer.getLayerName() || "N/A";
              var geoserverName = DynamicMap.GEOSERVER_WORKSPACE + ":" + layer.getViewName();
              var legendXPosition = layer.getLegendXPosition();
              var legendYPosition = layer.getLegendYPosition();
              var groupedInLegend = layer.getGroupedInLegend();
              
              if(groupedInLegend){
                
                // Remove any old grouped legend items before creating new updated items
                var oldLegendItem = $(".legend-item[data-parentlayerid='"+layerId+"']");
                oldLegendItem.remove();
                
                var html = '';
            html += '<li class="legend-item legend-grouped" data-parentLayerId="'+layerId+'">';
            html += '<img class="legend-image" src="'+window.location.origin+'/geoserver/wms?REQUEST=GetLegendGraphic&amp;VERSION=1.0.0&amp;FORMAT=image/png&amp;WIDTH=25&amp;HEIGHT=25&amp;LEGEND_OPTIONS=bgColor:0x302822;fontName:Arial;fontAntiAliasing:true;fontColor:0xececec;fontSize:11;fontStyle:bold;&amp;LAYER='+geoserverName+'" alt="">'+ displayName;
            html += '</li>';
            
                  // Styling the legend when items are added to an empty legend group
                  if($("#legend-list-group li").length === 0){
                    $("#legend-container-group").css("background", "rgba(0, 0, 0, 0.8)");
                    $("#legend-container-group").css("border", "solid black 1px");
                  }
                  
            $("#legend-list-group").append(html);  
            
              }
              else{
                
                // remove associated legend
                //// legend id's are set as the 'legend_' + layer id @ legend creation
                $("#"+legendId).remove();
                
                // weak control to set the legend in a better position than upper left when the legend is initially created
                if(legendXPosition == 0 && legendYPosition == 0){
                  legendXPosition += 300;
                  legendYPosition += 100;
                }
                
                var html = '';              
              html += '<div class="info-box legend-container legend-snapable" id="'+legendId+'" data-parentLayerId="'+layerId+'" style="top:'+legendYPosition+'px; left:'+legendXPosition+'px;">';
              html += '<div id="legend-items-container"><ul id="legend-list">';
              
            html += '<li class="legend-item" data-parentLayerId="'+layerId+'">';
            html += '<img class="legend-image" src="'+window.location.origin+'/geoserver/wms?REQUEST=GetLegendGraphic&amp;VERSION=1.0.0&amp;FORMAT=image/png&amp;WIDTH=25&amp;HEIGHT=25&amp;LEGEND_OPTIONS=bgColor:0x302822;fontName:Arial;fontAntiAliasing:true;fontColor:0xececec;fontSize:11;fontStyle:bold;&amp;LAYER='+geoserverName+'" alt="">'+ displayName;
            html += '</li>';
            
            html += '</ul></div></div>';
                
                $(".pageContent").append(html);  
                
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
          }
          }
          
          var that = this;
          $("#legend-container-group").droppable({
            tolerance: "touch",
            drop: function( event, ui ) {
              var draggedListItem = ui.draggable.children().children().children();
              var draggedLegendContainer = ui.draggable;
              var groupedInLegend = true;
              var relatedLayerId = $(ui.draggable).data('parentlayerid');
              var x = 0;
              var y = 0;
              
                  var clientRequest = new Mojo.ClientRequest({
                      onSuccess : function() {
                        // Update the layer object in the layer cache with the new legend position
                        var relatedLayer = that._layerCache.get(relatedLayerId);
                        relatedLayer.setLegendXPosition(x);
                        relatedLayer.setLegendYPosition(y);
                        relatedLayer.setGroupedInLegend(groupedInLegend);
                      },
                      onFailure : function(e) {
                        that.handleException(e);
                       
                      }
                  });
                  
                  // Persist legend position to the db
                  com.runwaysdk.geodashboard.gis.persist.DashboardLayer.updateLegend(clientRequest, relatedLayerId, x, y, groupedInLegend);
                  
                  // Styling the legend when items are added to an empty legend group
                  if($("#legend-list-group li").length === 0){
                    $("#legend-container-group").css("background", "rgba(0, 0, 0, 0.8)");
                    $("#legend-container-group").css("border", "solid black 1px");
                  }
                  
              draggedListItem.appendTo("#legend-list-group");
              draggedListItem.addClass("legend-grouped");
              draggedLegendContainer.remove();
            }
          });
          
          // Click and drag handlers  
          var legendDragStopDragBound = Mojo.Util.bind(this, this._legendDragStopHandler);
          
          $(".legend-container").on('dragstop', legendDragStopDragBound);         
          $("#legend-list-group").on("click", ".legend-item", function(e) {
            var li = $(this);
          var parentLayerId = li.data("parentlayerid");
          var legendId = "legend_" + parentLayerId;
          
          // prevent legend containers from being created twice. 
          if($("#"+legendId).length === 0){
            var legendPos = $("#legend-container-group").position();
            var legendXPosition = legendPos.left;
            var legendYPosition = legendPos.top;
            var liYPosition = legendYPosition;
            var liXPosition = legendXPosition - 50;
            
            html = '';
            html += '<div class="info-box legend-container legend-snapable" id="'+legendId+'" data-parentLayerId="'+parentLayerId+'" style="top:'+liYPosition+'px; left:'+liXPosition+'px;">';
              html += '<div id="legend-items-container"><ul id="legend-list">';
              html += '</ul></div></div>';
              
              li.removeClass("legend-grouped");
              
              $(".pageContent").append(html);
              $("#"+legendId).children().children().append(li);
              
                  // Styling (visually hiding) the legend when all items are removed from legend group
                  if($("#legend-list-group li").length === 0){
                    $("#legend-container-group").css("background", "none");
                    $("#legend-container-group").css("border", "none");
                  }
              
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
       * Build HTML for user defined overlays
       * 
       */
      _drawUserLayersHMTL : function(htmlInfo) {
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
          html += '<div class="cell"><a href="#" data-id="'+layer.getLayerId()+'" class="ico-remove">remove</a>';
          html += '<a href="#" data-id="'+layer.getLayerId()+'" class="ico-edit">edit</a>';
          html += '<a href="#" data-id="'+layer.getLayerId()+'" class="ico-control">control</a></div>';
          html += '</div>';
        }
        
        // 2) Render the HTML we just generated.
        container.html(html);
//        jcf.customForms.replaceAll(container[0]);
        
        // 3) Add checkboxes and register click events
        for(var i = 0; i < layers.length; i++){
          var layer = layers[i];
          
          var chexd = layer.checked === false || layer.checked === true ? layer.checked : true;
          
          com.runwaysdk.event.Registry.getInstance().removeAllEventListeners(layer.getLayerId());
          var checkbox = this.getFactory().newCheckBox({el: "#"+layer.getLayerId(), data: {runwayId: layer.getLayerId()}, checked: chexd, classes: ["check"]});
          checkbox.addOnCheckListener(onCheckHandler);
          checkbox.render();
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
        
        // Remove any already rendered layers
        if (removeExisting !== false) {
          for (var i = 0; i < layers.length; i++) {
            var layer = layers[i];
            
            if (layer.leafletLayer != null) {
              this._map.removeLayer(layer.leafletLayer);
            }
          }
        }
        
        // Add all our layers from the layerCache to leaflet.
        for (var i = 0; i < layers.length; i++) {
          var layer = layers[i];
          
          if (layer.checked !== false && (removeExisting !== false || (removeExisting === false && layer.leafletLayer == null))) {
            var viewName = layer.getViewName();
            var displayName = layer.getLayerName() || "N/A";
            var geoserverName = DynamicMap.GEOSERVER_WORKSPACE + ":" + viewName;
            var mapBounds = this._map.getBounds();
            var mapSWOrigin = [mapBounds._southWest.lat, mapBounds._southWest.lng];
              
            var leafletLayer = L.tileLayer.wms(window.location.origin+"/geoserver/wms/", {
              layers: geoserverName,
              format: 'image/png',
              transparent: true,
              tiled: true,
              tilesorigin: mapSWOrigin,
              styles: layer.getSldName() || "" // This should be enabled we wire up the interface or set up a better test process
            });
            this._map.addLayer(leafletLayer);
            
            layer.leafletLayer = leafletLayer;
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
            that._addUserLayersToMap();
          },
          onFailure : function(e) {
            that.handleException(e);
            
            // The server failed to reorder the layers. We need to redraw the HTML to reset the layer ordering, but we don't need to update leaflet because that ordering is still correct.
            that._drawUserLayersHMTL();
          }
        });
        
        com.runwaysdk.geodashboard.gis.persist.DashboardMap.orderLayers(clientRequest, this._mapId, layerIds);
      },
      
      /**
       * Handler for when the user clicks on a dashboard on the dropdown.
       */
      _dashboardClickHandler : function(e) {

        var el = $(e.currentTarget);
        
        var dashboardId = el[0].id;
        
        window.location = "?dashboard=" + dashboardId;
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
       * Performs the identify request when a user clicks on the map
       * 
       * This is a first version and will likely need to be rewritten using custom
       * backend logic (i.e. mdmethod/sql) to allow for more flexibility
       * 
       * @param id
       */
      _mapClickHandler : function(e) {
        
        // Construct a GetFeatureInfo request URL given a point
        var point = this._map.latLngToContainerPoint(e.latlng, this._map.getZoom());
        var size = this._map.getSize();
        var layers = this._layerCache.$values().reverse();
        var mapBbox = this._map.getBounds().toBBoxString();
        var map = this._map;
        var layerNameMap = new Object();
        var layerAggMap = new Object();
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
          
          if(layer.getLayerIsActive()){
              var layerId = layer.attributeMap.viewName.value;
              layerNameMap[layerId] = layer.getLayerName();
              layerAggMap[layerId] = layer.getAggregationMethod();
              if(firstAdded){
                layerStringList += "," + layerId;
              }
              else{
                layerStringList += layerId;
                // we currently only map against a single attribute for a map.  
                // if we allow multiple attributes mapped in a map the assignment of
                // aggregationAttr will need to be refactored to associate a layers
                // identify request with the appropriate aggregationAttr for that layer
                aggregationAttr = layer.getAggregationAttribute().toLowerCase();
                firstAdded = true;
              }
          }
        }
        
      var requestURL = window.location.origin+"/geoserver/"+DynamicMap.GEOSERVER_WORKSPACE+"/wms?" +
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
        "&TYPENAME=geodashboard:"+ layerStringList +
        "&propertyName=displaylabel,geoid,"+ aggregationAttr;
      
      DynamicMap.that = this;
         $.ajax({
          url: requestURL,
          context: document.body 
        }).done(function(json) {
          var popupContent = '';
          // The getfeatureinfo request will return only 1 feature
            for(var i = 0; i<json.features.length; i++){
              var currLayer = json.features[i];
              var currLayerIdReturn = currLayer.id;
              var currLayerId = currLayerIdReturn.substring(0, currLayerIdReturn.indexOf('.'));
              var currLayerDisplayName = layerNameMap[currLayerId];
              var currFeatureDisplayName = currLayer.properties.displaylabel;
              var currAttributeVal = currLayer.properties.numberofunits;
              var currAggMethod = layerAggMap[currLayerId];
              
              
              popupContent += '<h3 class="popup-heading">'+currLayerDisplayName+'</h3>';
              
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
              html += '<td>'+ currFeatureDisplayName +'</td>';  
              html += '<td>' + currAggMethod + '</td>'; 
              html += '<td>' + currAttributeVal + '</td>';  
              html += '</tr>';  
              html += '</tbody>';  
              html += '</table>';  
        
              
//              popupContent += '<h4 class="popup-heading">'+currLayerDisplayName+'</h4>';
//                popupContent += '<p class="popup-content">'+ currFeatureDisplayName + " "+ currAggMethod + " " +currAttributeVal+'</p>';
              
              popupContent += html;
              
              var currGeoId = currLayer.properties.geoid;
              
              if(currGeoId != null)
              {            	   
            	  that._renderReport(currGeoId, that._criteria);
              }
            
            }
            
            if(popupContent.length > 0){
              popup.setContent(popupContent).openOn(map);
            }
        });
      },
      
      /**
       * Removes the Layer with the given object id (Runway Id)
       * from all caches, the sidebar, and the map itself.
       * 
       * @param id
       */
      _removeLayer : function(el, id) {
        
        var toRemove = this._layerCache.get(id);
        
        // remove layer from our cache
        this._layerCache.remove(id);
        
        // remove the actual layer from the map
        this._map.removeLayer(toRemove.leafletLayer);
        
        // remove the layer from the map and UI
        el.parent().parent().remove();
        
        // Remove associated legend and legend container
        //// legend id's are set as the 'legend_' + layer id @ legend creation
        $("#legend_"+id).remove();
        $("li[data-parentlayerid='"+id+"']").remove();
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
       * @param params
       */
      _applyWithStyleListener : function(params){
        
        var that = this;
        
        var request = new com.runwaysdk.geodashboard.StandbyClientRequest({
          onSuccess : function(htmlOrJson, response){
            if (response.isJSON()) {
              that._closeLayerModal();
              
              that._updateCacheFromJSONResponse({layers: [Mojo.Util.toObject(htmlOrJson)]});
              
              // Redraw the HTML and update leaflet.
              that._drawUserLayersHMTL();
              that._addUserLayersToMap();            
              
              // TODO : Push this somewhere as a default handler.
              that.handleMessages(response);
            }
            else if (response.isHTML()) {
              // we got html back, meaning there was an error
              that._displayLayerForm(htmlOrJson);
              $(DynamicMap.LAYER_MODAL).animate({scrollTop:$('.heading').offset().top}, 'fast'); // Scroll to the top, so we can see the error
            }
          },
          onFailure : function(e){
            that.handleException(e);
          }
        }, $(DynamicMap.LAYER_MODAL)[0]);
        
        var layer = this._layerCache.get(params["layer.componentId"]);
        var mdAttribute = null;
        if (layer != null) {
          mdAttribute = layer.style.mdAttribute;
        }
        else {
          mdAttribute = this._currentAttributeId;
        }
        
        params['mapId'] = this._mapId;
        params['style.mdAttribute'] = mdAttribute;
        
        // Custom conversion to turn the checkboxes into boolean true/false
        params['style.enableLabel'] = params['style.enableLabel'].length > 0;
        params['style.enableValue'] = params['style.enableValue'].length > 0;
        params['layer.displayInLegend'] = params['layer.displayInLegend'].length > 0;
        
        // Include attribute condition filtering (i.e. sales unit is greater than 50)
        var select = $("select.gdb-attr-filter." + mdAttribute).val();
        var textValue = $("input.gdb-attr-filter." + mdAttribute).val();
        if (textValue != null && textValue !== "") {
          var condition = null;
          if (select === "gt") {
            condition = "com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThan";
          }
          else if (select === "ge") {
            condition = "com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThanOrEqual";
          }
          else if (select === "lt") {
            condition = "com.runwaysdk.geodashboard.gis.persist.condition.DashboardLessThan";
          }
          else if (select === "le") {
            condition = "com.runwaysdk.geodashboard.gis.persist.condition.DashboardLessThanOrEqual";
          }
          
          params["conditions_0.comparisonValue"] = textValue;
          params["conditions_0.isNew"] = "true";
          params["#conditions_0.actualType"] = condition;
        }
        
        return request;
      },
      
      /**
       * 
       * @param params
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
          com.runwaysdk.geodashboard.gis.persist.DashboardLayer.unlock(request, id);
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
        var gsat = new L.Google('SATELLITE');         
        var gphy = new L.Google('TERRAIN');       
        var gmap = new L.Google('ROADMAP');       
        var ghyb = new L.Google('HYBRID');
        
        var osm = new L.TileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'); 
        osm._gdbcustomtype = 'OSM';
        
        var base = [gmap, gsat, ghyb, gphy, osm];
        
        return base;
      },
      
      /**
       * Renders each base layer as a checkable option in
       * the layer switcher.
       * 
       * @param base - array of leaflet basemap layer objects
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
       * @param checkbox - Checkbox object
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
       * @param e
       */
      _toggleOverlayLayer : function(e){
        var cbox = e.getCheckBox();
        var checked = cbox.isChecked();
        var layer = this._layerCache.get(cbox.getData().runwayId);
        
        layer.checked = checked;
        
        if (checked) {
          this._addUserLayersToMap();
          layer.setLayerIsActive(true);
        }
        else {
          this._map.removeLayer(layer.leafletLayer);
          layer.setLayerIsActive(false);
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
          modal.modal('show');
          modal.html(html);
          
          eval(Mojo.Util.extractScripts(html));
          
          jcf.customForms.replaceAll(modal[0]);
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
          },
          onFailure : function(e){
            that._closeLayerModal();
            that.handleException(e);
          }
        });
        
        this._LayerController.newThematicInstance(request, this._currentAttributeId);
        
      },
      
      /**
       * Renders the layer creation/edit form
       * 
       * @html
       */
      _displayLayerForm : function(html){
        
        // clear all previous color picker dom elements
        $(".colpick.colpick_full.colpick_full_ns").remove();
        
        // Show the white background modal.
        var modal = $(DynamicMap.LAYER_MODAL).first();
        modal.modal('show');
        modal.html(html);
        
        eval(Mojo.Util.extractScripts(html));
        
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
        else if (activeTab === "tab004category") {
          this._showCategoryTab();
        }
        
        // Attach listeners
        $('a[data-toggle="tab"]').on('shown.bs.tab', Mojo.Util.bind(this, this._onLayerTypeTabChange));
      },
      
      _showCategoryTab : function() {
        // Hide the reusable input cells that don't apply to categories
        var polyStroke = $("#gdb-reusable-cell-polygonStroke");
        polyStroke.hide();
        
        var polyStrokeWidth = $("#gdb-reusable-cell-polygonStrokeWidth");
        polyStrokeWidth.hide();
        
        var polyStrokeOpacity = $("#gdb-reusable-cell-polygonStrokeOpacity");
        polyStrokeOpacity.hide();
        
        var polyFillOpacity = $("#gdb-reusable-cell-polygonFillOpacity");
        polyFillOpacity.hide();
        
        // Display the tree picker
        this._layerCategoriesTree = new com.runwaysdk.geodashboard.ontology.LayerCategoriesTree(this._config.layerCategoriesTree);
        this._layerCategoriesTree.render("#category-colors-container");
      },
      
      _attachDynamicCells : function(strokeCellHolder, fillCellHolder) {
        var polyStroke = $("#gdb-reusable-cell-polygonStroke");
        strokeCellHolder.append(polyStroke);
        polyStroke.show();
        
        var polyStrokeWidth = $("#gdb-reusable-cell-polygonStrokeWidth");
        strokeCellHolder.append(polyStrokeWidth);
        polyStrokeWidth.show();
        
        var polyStrokeOpacity = $("#gdb-reusable-cell-polygonStrokeOpacity");
        strokeCellHolder.append(polyStrokeOpacity);
        polyStrokeOpacity.show();
        
        var polyFillOpacity = $("#gdb-reusable-cell-polygonFillOpacity");
        fillCellHolder.append(polyFillOpacity);
        polyFillOpacity.show();
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
        else if (type === "CATGORIES") {
          $("#tab004categories").show();
        }
      },
      
      /**
       * Handles the selection of colors from the color picker 
       * 
       * 
       */
      _selectColor : function(){
        
        // color dropdown buttons
        var total1 = $('.color-holder').colpick({
          submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
          onChange:function(hsb,hex,rgb,el,bySetColor) {
            $(el).find(".ico").css('background','#'+hex);
            $(el).find('.color-input').attr('value', '#'+hex);
          }
        }); 
        
        // category layer type colors
        var total2 = $("#category-colors-container").find('.icon-color').colpick({
        submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
        onChange:function(hsb,hex,rgb,el,bySetColor) {
          $(el).css('background','#'+hex);
          $(el).find('.color-input').attr('value', '#'+hex);
        }
        });
      },
      
      /**
       * Handles the selection of layer type representation  
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
       * Hide / show the legend
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
        
        var clientRequest = new Mojo.ClientRequest({
          onSuccess : function() {
            // Update the layer object in the layer cache with the new legend position
            var relatedLayer = that._layerCache.get(relatedLayerId);
            relatedLayer.setLegendXPosition(x);
            relatedLayer.setLegendYPosition(y);
            relatedLayer.setGroupedInLegend(groupedInLegend);
          },
          onFailure : function(e) {
            that.handleException(e);
           
          }
        });
        
        // Persist legend position to the db
        com.runwaysdk.geodashboard.gis.persist.DashboardLayer.updateLegend(clientRequest, relatedLayerId, x, y, groupedInLegend);
      },
      
      _onClickApplyFilters : function(e) {
        var that = this;
        
        // Get the attribute filter conditions (And its shortened criteria reprensentation for reporting)
        var layers = this._layerCache.values();
        var conditions = [];
        this._criteria = [];
        
        
        for(var i = layers.length-1; i >= 0; i--) {
          var layer = layers[i];

          var mdAttribute = layer.style.mdAttribute;
          
          var select = $("select.gdb-attr-filter." + mdAttribute).val();
          var textValue = $("input.gdb-attr-filter." + mdAttribute).val();
          if (textValue != null && textValue !== "") {
            var attrCond = null;
            if (select === "gt") {
              attrCond = new com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThan();
            }
            else if (select === "ge") {
              attrCond = new com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThanOrEqual();
            }
            else if (select === "lt") {
              attrCond = new com.runwaysdk.geodashboard.gis.persist.condition.DashboardLessThan();
            }
            else if (select === "le") {
              attrCond = new com.runwaysdk.geodashboard.gis.persist.condition.DashboardLessThanOrEqual();
            }
            
            attrCond.setComparisonValue(textValue);
            
            conditions.push(attrCond);
            this._criteria.push({'mdAttribute':mdAttribute, 'operation':select, 'value':textValue});
          }
        }
        
        var clientRequest = new Mojo.ClientRequest({
          onSuccess : function(json, calledObj, response) {
            var jsonObj = Mojo.Util.toObject(json);
            
            that._updateCacheFromJSONResponse(jsonObj);
            
            that._addUserLayersToMap();
            
            // TODO : Push this somewhere as a default handler.
            that.handleMessages(response);
          },
          onFailure : function(e) {
            that.handleException(e);
          }
        });
        
        com.runwaysdk.geodashboard.gis.persist.DashboardMap.updateConditions(clientRequest, this._mapId, conditions);
        
        this._renderReport('', this._criteria);
      },
      
      /**
       * Callback for sorting legend items
       */
      _legendSortUpdate : function(event, ui){
         // No action needed at this time. This is simply a ui feature.
      },
      
      /**
       * Renders the mapping widget, performing a full refresh.
       */
      render : function(){
        this.fullRefresh();
        
        // Make sure all openers for each attribute have a click event
        $('a.attributeLayer').on('click', Mojo.Util.bind(this, this._openLayerForAttribute));
        $('a.new-dashboard-btn').on('click', Mojo.Util.bind(this, this._openNewDashboardForm));
        $('a.apply-filters-button').on('click', Mojo.Util.bind(this, this._onClickApplyFilters));
        
        if(this._googleEnabled){
          this._addAutoComplete();
        }        
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
