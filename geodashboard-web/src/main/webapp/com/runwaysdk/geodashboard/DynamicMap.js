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
        
        // Default criteria for filtering
        this._conditionMap = {'conditions' : [], 'criteria' : []};
        this._currGeoId = '';
        
        // Number localization setup
        this._parser = Globalize.numberParser();
        this._formatter = Globalize.numberFormatter();
        
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
        
        // Handler for the clone dashboard button
        $("#clone-dashboard").on("click", Mojo.Util.bind(this, this._dashboardCloneHandler));        
        
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
       * 
       */
      fullRefresh : function() {
        var that = this;
        
        that._renderMap();
        
        this._updateCachedData(function(){
          that._configureMap();
          that._renderBaseLayers();
          that._renderUserLayers();
        });
        
        that._renderReport('', that._conditionMap['criteria']);
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
      * Scrapes color icons from ontology categories on the layer creation/edit form
      * 
      */
      _updateOntologyCategoriesJSON : function() {
        
          var ontTreeStyleArr = new Object();
          ontTreeStyleArr.catLiElems = [];
        
        var allElem = $(".ontology-category-color-icon");
        for(var e=0; e<allElem.length; e++){
          
          var rwId = allElem[e].dataset.rwid;
          // filters out the jqTree 'phantom' elements which are duplicates of the elements we are after
          if(rwId.indexOf("PHANTOM") === -1){
            var theElem = allElem[e];
            var theColor = rgb2hex($(theElem).css("background-color"));
            
              var liObj = new Object();
                liObj.id = theElem.dataset.rwid;
                liObj.color = theColor;
                ontTreeStyleArr.catLiElems.push(liObj);                   
          }
        }
        
        // set the hidden input element in the layer creation/edit form 
        $("#ontology-categories-input").val(JSON.stringify(ontTreeStyleArr));
        
        // javascript and jquery return css color values as rgb.  
        // We want hex in this case
        function rgb2hex(rgb) {
            if (/^#[0-9A-F]{6}$/i.test(rgb)) return rgb;

            rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
            function hex(x) {
                return ("0" + parseInt(x).toString(16)).slice(-2);
            }
            return "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
        }

        return  ontTreeStyleArr;
      },
      
      /**
       * Creates "view" objects for each layer defined by the dashboardlayerview.java class
       * and builds the initial layer cache
       * 
       * @json - json obj returned from the server that contains map data and properties for map construction
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
       * 
       * @gedId
       * @criteria
       * 
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
      },
      
      _exportReport : function(geoId, criteria, format) {
           
        var configuration = {};
        configuration.parameters = [];
        configuration.parameters.push({'name' : 'category', 'value' : geoId});
        configuration.parameters.push({'name' : 'criteria', 'value' : JSON.stringify(criteria)});
        configuration.parameters.push({'name' : 'format', 'value' : format});
        
        var url = 'com.runwaysdk.geodashboard.report.ReportItemController.run.mojo';
        url += '?' + encodeURIComponent("report") + "=" + encodeURIComponent(this._dashboardId);          
        url += '&' + encodeURIComponent("configuration") + "=" + encodeURIComponent(JSON.stringify(configuration));          
        
        window.location.href = url;
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
        this._drawUserLayersHMTL();
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
       * 
       */
      _Legend : function(layerId, displayName, geoserverName, legendXPosition, legendYPosition, groupedInLegend) {
        this.legendId = "legend_" + layerId;
        this.displayName = displayName;
        this.geoserverName = geoserverName;
        this.legendXPosition = legendXPosition;
        this.legendYPosition = legendYPosition;
        this.groupedInLegend = groupedInLegend;
        this.create = function(){
              if(this.groupedInLegend){
                  // Remove any old grouped legend items before creating new updated items
                  $(".legend-item[data-parentlayerid='"+layerId+"']").remove();
                  
                  var html = '';
                html += '<li class="legend-item legend-grouped" data-parentLayerId="'+layerId+'">';
                html += '<img class="legend-image" src="'+window.location.origin+'/geoserver/wms?REQUEST=GetLegendGraphic&amp;VERSION=1.0.0&amp;FORMAT=image/png&amp;WIDTH=25&amp;HEIGHT=25&amp;LEGEND_OPTIONS=bgColor:0x302822;fontName:Arial;fontAntiAliasing:true;fontColor:0xececec;fontSize:11;fontStyle:bold;&amp;LAYER='+ this.geoserverName+'" alt="">'+ this.displayName;
                html += '</li>';
              
                  $("#legend-list-group").append(html);  
                  this.checkGroupedBackground();
              }
              else
              {
                  // remove associated legend
                  $("#"+this.legendId).remove();
                  
                  var html = '';              
                  html += '<div class="info-box legend-container legend-snapable" id="'+ this.legendId +'" data-parentLayerId="'+ layerId +'" style="top:'+ this.legendYPosition +'px; left:'+ this.legendXPosition +'px;">';
                  html += '<div id="legend-items-container"><ul id="legend-list">';
                html += '<li class="legend-item" data-parentLayerId="'+layerId+'">';
                html += '<img class="legend-image" src="'+window.location.origin+'/geoserver/wms?REQUEST=GetLegendGraphic&amp;VERSION=1.0.0&amp;FORMAT=image/png&amp;WIDTH=25&amp;HEIGHT=25&amp;LEGEND_OPTIONS=bgColor:0x302822;fontName:Arial;fontAntiAliasing:true;fontColor:0xececec;fontSize:11;fontStyle:bold;&amp;LAYER='+ this.geoserverName +'" alt="">'+ this.displayName;
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
            $('*[data-parentlayerid="'+layerId+'"]').show();
                this.checkGroupedBackground();
          }
          else{
            $("#"+this.legendId).show();
          }
        };
        this.hide = function(){
          if(this.groupedInLegend){
            $('*[data-parentlayerid="'+layerId+'"]').hide();
          }
          else{
            $("#"+this.legendId).hide();
          }
          
              this.checkGroupedBackground();
        };
        this.unGroup = function(li){
            var legendPos = $("#legend-container-group").position();
            var legendXPosition = legendPos.left;
            var legendYPosition = legendPos.top;
            var liYPosition = legendYPosition;
            var liXPosition = legendXPosition - 50;
              
            html = '';
            html += '<div class="info-box legend-container legend-snapable" id="'+this.legendId+'" data-parentLayerId="'+layerId+'" style="top:'+liYPosition+'px; left:'+liXPosition+'px;">';
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
          
          this.checkGroupedBackground();
          
        };
        this.checkGroupedBackground = function(){
              // Styling (visually hiding) the legend when all items are removed from legend group
          var containsVisibleElement = false;
          $.each($("#legend-list-group li"), function(k,v){
            if($(v).is(":visible"))
            {
              containsVisibleElement = true;
            }
          });
              if(!containsVisibleElement && $("#legend-container-group").hasClass("legend-container-group-active"))
              {
                $("#legend-container-group").removeClass("legend-container-group-active");
              }
              else if(containsVisibleElement && !$("#legend-container-group").hasClass("legend-container-group-active"))
              {
                $("#legend-container-group").addClass("legend-container-group-active");
              }
        };
      },
      
      
      /**
       * Draws legend entries based on the layer cache
       * 
       */
      _drawLegendItems : function() {
        var that = this;
        var layers = this._layerCache.values();
        
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
            var displayInLegend = layer.getDisplayInLegend();

            if(displayInLegend)
            {
                var layerId = layer.getLayerId();
                var displayName = layer.getLayerName() || "N/A";
                var geoserverName = DynamicMap.GEOSERVER_WORKSPACE + ":" + layer.getViewName();
                var legendXPosition = layer.getLegendXPosition();
                var legendYPosition = layer.getLegendYPosition();
                var groupedInLegend = layer.getGroupedInLegend();
                
                var legendObj = new this._Legend(
                    layerId, 
                    displayName, 
                    geoserverName, 
                    legendXPosition, 
                    legendYPosition, 
                    groupedInLegend
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
          }
          
          //
          // Control for adding un-grouped legend items to the legend items container
          //
          $("#legend-container-group").droppable({
              tolerance: "touch",
              drop: function( event, ui ) {
                var groupedInLegend = true;
                var relatedLayerId = $(ui.draggable).data('parentlayerid');
                var parentLayer = that._layerCache.get(relatedLayerId);
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
            
            // prevent legend containers from being created twice. 
            if($("#"+legendId).length === 0){
                var parentLayer = that._layerCache.get(parentLayerId);
                parentLayer.layerLegend.unGroup(li);
                parentLayer.layerLegend.checkGroupedBackground();
                
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
       * @htmlInfo
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
        
        // Remove any already rendered layers from the leaflet map
        if (removeExisting === true) {
          for (var i = 0; i < layers.length; i++) {
            var layer = layers[i];
            
            if (layer.leafletLayer != null) {
              this._map.removeLayer(layer.leafletLayer);
            }
          }
        }
        
        // Add all our layers from the layerCache to leaflet map
        for (var i = 0; i < layers.length; i++) {
          var layer = layers[i];
          
          if (layer.getLayerIsActive() === true && (removeExisting !== false || (removeExisting === false && layer.leafletLayer == null))) {
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
            that._addUserLayersToMap(true);
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
              width:400,
              modal: true,
              buttons: [{
                text : com.runwaysdk.Localize.localize("dashboard", "Ok", "Ok"),
                click : function() {
                  var createRequest = new Mojo.ClientRequest({
                    onSuccess : function(dashboard){
                      window.location = "?dashboard=" + dashboard.getId();
                    },
                    onFailure : function(e){
                      that.handleException(e);
                    }
                  });
                  
                  var dashboardId = $('#clone-dashboard-id').val();
                  var label = $('#clone-label').val();
                  
                  if(label != null && label.length > 0)
                  {
                    com.runwaysdk.geodashboard.Dashboard.clone(createRequest, dashboardId, label);                    
                  }
                  else
                  {
                    var msg = com.runwaysdk.Localize.localize("dashboard", "Required");                    
                    
                    var dialog = com.runwaysdk.ui.Manager.getFactory().newDialog(com.runwaysdk.Localize.get("rError", "Error"), {modal: true});
                    dialog.appendContent(message);
                    dialog.addButton(com.runwaysdk.Localize.get("rOk", "Ok"), function(){dialog.close();}, null, {primary: true});
                    dialog.setStyle("z-index", 2001);
                    dialog.render();    
                  }
                }
              },
              {
                text : com.runwaysdk.Localize.localize("dashboard", "Cancel", "Cancel"),
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
              that._addCategoryAutoComplete();
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
              var currAttributeVal = currLayer.properties[aggregationAttr];
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
            	that._currGeoId = currGeoId;
            	
                that._renderReport(that._currGeoId, that._conditionMap['criteria']);
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
       * @el
       * @id
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
       * @params
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
              that._addUserLayersToMap(true);            
              
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
        if (layer != null) 
        {
          mdAttribute = layer.style.mdAttribute;
        }
        else 
        {
          mdAttribute = this._currentAttributeId;
        }
        
        params['mapId'] = this._mapId;
        params['style.mdAttribute'] = mdAttribute;
        
        // Custom conversion to turn the checkboxes into boolean true/false
        params['style.enableLabel'] = params['style.enableLabel'].length > 0;
        params['style.enableValue'] = params['style.enableValue'].length > 0;
        params['layer.displayInLegend'] = params['layer.displayInLegend'].length > 0;
        
        var conditions = this._conditionMap['conditions'];
        
        $.each(conditions, function( index, condition ) {
          params["conditions_" + index + ".comparisonValue"] = condition.getComparisonValue();
          params["conditions_" + index + ".definingMdAttribute"] = condition.getValue('definingMdAttribute');
          params["conditions_" + index + ".isNew"] = "true";
          params["#conditions_" + index + ".actualType"] = condition.getType();
        });      
        
        
        // Normalize any localized number category values
        if(this._getCategoryType() == "number" ) {
          var attributes = ['style.styleCategory1', 'style.styleCategory2', 'style.styleCategory3', 'style.styleCategory4', 'style.styleCategory5'];
          
          $.each(attributes, function(index, attribute) {
            if(params[attribute] != null && params[attribute].length > 0) {
              var number = that._parser(params[attribute])
              
              if($.isNumeric(number)) {
                params[attribute] = number;                
              }
            } 
          });
        }
        
        var  ontTreeStyleArr = this._updateOntologyCategoriesJSON();
        params['style.styleOntologyCategoryies'] = JSON.stringify(ontTreeStyleArr);
        
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
       * A similar functionality is implemented for creating new layers with categories in the
       * _openLayerForAttribute method. The difference being that this method queries the existing 
       * database view which the layer is built on while the other queries the database from the Dashboard
       * class to determine the possible values for a layer. 
       * 
       * This method is a little more acurate because the database view includes any filters
       * that might have been applied to the layer.
       */
      _addCategoryAutoComplete : function(){
        
        var that = this;
        
        // Hook up the auto-complete for category input options new layers
        // existing layers have a seperate autocomplete hookup that queries 
        // the layer database view directly. 
        $('.category-input').each(function(){
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
              var aggregationVal = $("#f59").val();
              
              com.runwaysdk.geodashboard.Dashboard.getCategoryInputSuggestions(req, mdAttribute, universalId, aggregationVal, request.term, 10);
            },
            minLength: 1
          });
        });
        
        
//        var clientRequest = new Mojo.ClientRequest({
//          onSuccess : function(jsonCatData) {
//          
//            var data = JSON.parse(jsonCatData);
//          
//            that._autocomplete = $('.category-input').autocomplete({
//                  minLength: 1,
//                  autoFocus: true,
//                  select : function(value, data){          
//                    
//                  // Set the field to this value
//                    
//                    that._suggestionCoords.clear();
//                  },
//                  source: function(request, response){
//                  
//                    var result = $.grep(data, function (el) {
//                          return el.indexOf(request.term) === 0;
//                      });
//                      response(result);     
//                  },
//                  change: function (e, ui) {
//                      if (!(0 || ui.item)) e.target.value = "";
//                  }
//                  
//                });
//        },
//        onFailure : function(e) {
//          that.handleException(e);
//        }
//      });
//        
//        com.runwaysdk.geodashboard.gis.persist.DashboardLayer.getThematicAttributeCategories(clientRequest, layerId);
        
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
            
            that._addCategoryAutoComplete();
            
//            // Hook up the auto-complete for category input options new layers
//            // existing layers have a seperate autocomplete hookup that queries 
//            // the layer database view directly. 
//            $('.category-input').each(function(){
//              var mdAttribute = $(this).data('mdattributeid');  
//              var categoryType = $(this).data('type');
//              
//              $(this).autocomplete({
//                source: function( request, response ) {
//                  var req = new Mojo.ClientRequest({
//                    onSuccess : function(results){
//                      
//                      // We need to localize the results for numbers
//                      if(categoryType == 'number') {
//                        for(var i = 0; i < results.length; i++) {
//                          var number = parseFloat(results[i]);
//                          var localized = that._formatter(number);
//                          
//                          results[i] = localized;
//                        }
//                      }
//                      
//                      response( results );
//                    },
//                    onFailure : function(e){
//                      that.handleException(e);
//                    }
//                  });
//                  
//                  // values are scraped from hidden input elements on the layer create form
//                  var universalId = $("#f58").val();
//                  var aggregationVal = $("#f59").val();
//                  
//                  com.runwaysdk.geodashboard.Dashboard.getCategoryInputSuggestions(req, mdAttribute, universalId, aggregationVal, request.term, 10);
//                },
//                minLength: 1
//              });
//            });
            
            // ontology category layer type colors
            $(".category-color-icon").colpick({
              submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
              onChange:function(hsb,hex,rgb,el,bySetColor) {
                $(el).css('background','#'+hex);
                $(el).find('.color-input').attr('value', '#'+hex);
              }
            });
            
            //
            // See form.jsp in DashboardLayer for category color pickers event listener configuration
            //
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
        else if (type === "CATEGORY") {
          $("#tab004categories").show();
          this._attachDynamicCells($("#gdb-reusable-categories-stroke-cell-holder"), $("#gdb-reusable-categories-fill-cell-holder"));
        }
      },
      
      /**
       * Handles the selection of colors from the color picker 
       * 
       * 
       */
      _selectColor : function(){
        
        // color dropdown buttons
        $('.color-holder').colpick({
          submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
          onChange:function(hsb,hex,rgb,el,bySetColor) {
            $(el).find(".ico").css('background','#'+hex);
            $(el).find('.color-input').attr('value', '#'+hex);
          }
        }); 
        
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
      
      _buildConditionMap : function () {
        var that = this;
        var conditions = [];
        var criteria = [];
          
        $( "input.gdb-attr-filter" ).each(function( index ) {
          var textValue = $(this).val();
              
          // Ensure there is a value to filter against
          if (textValue != null && textValue !== "") {
            if($(this).hasClass('filter-number')) {              
              // Add number criterias
              var mdAttribute = $(this).attr('id').replace('filter-number-', '');   
                
              var select = $("#filter-opts-" + mdAttribute).val();
                
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

              var number = that._parser( textValue );

              attrCond.setComparisonValue(number);
              attrCond.setDefiningMdAttribute(mdAttribute);
                
              conditions.push(attrCond);
              criteria.push({'type':'ATTRIBUTE_CONDITION', 'mdAttribute':mdAttribute, 'operation':select, 'value':number});            
            }
            else if($(this).hasClass('filter-char')) {              
              // Add character criterias
              var mdAttribute = $(this).attr('id').replace('filter-char-', '');   
              
              var select = $("#filter-opts-" + mdAttribute).val();
              
              var attrCond = null;
              
              if (select === "eq") {
                attrCond = new com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqual();
              }
              else if(select === "neq") {
                attrCond = new com.runwaysdk.geodashboard.gis.persist.condition.DashboardNotEqual();
              }
              
              attrCond.setComparisonValue(textValue);
              attrCond.setDefiningMdAttribute(mdAttribute);
              
              conditions.push(attrCond);
              criteria.push({'type':'ATTRIBUTE_CONDITION', 'mdAttribute':mdAttribute, 'operation':select, 'value':textValue});            
            }
            else if($(this).hasClass('filter-term')) {              
              // Add term criterias
              var mdAttribute = $(this).attr('id').replace('filter-term-', '');
              var id = $('#filter-hidden-' + mdAttribute).val(); 
              
              if(id != null)
              {
                var attrCond = new com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqual();             
                attrCond.setComparisonValue(id);
                attrCond.setDefiningMdAttribute(mdAttribute);
                
                conditions.push(attrCond);
                criteria.push({'type':'ATTRIBUTE_CONDITION', 'mdAttribute':mdAttribute, 'operation':'eq', 'value':id});            
              }
            }
            else if($(this).hasClass('filter-date')) {
              // Add the date criteria
              if($(this).hasClass('checkin')) {
                var mdAttribute = $(this).attr('id').replace('filter-from-', '');                 
                var date = $(this).datepicker('getDate')
                
                // Normalize the incoming date to a standard format regardless of the locale
                var value =  $.datepicker.formatDate('dd/mm/yy', date);

                var attrCond = new com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThanOrEqual();
                attrCond.setComparisonValue(value);
                attrCond.setDefiningMdAttribute(mdAttribute);
                      
                conditions.push(attrCond);
                criteria.push({'type':'ATTRIBUTE_CONDITION', 'mdAttribute':mdAttribute, 'operation':'ge', 'value':value});
              }
              else if($(this).hasClass('checkout')) {
                var mdAttribute = $(this).attr('id').replace('filter-to-', '');   
                var date = $(this).datepicker('getDate')
                
                // Normalize the incoming date to a standard format regardless of the locale                
                var value =  $.datepicker.formatDate('dd/mm/yy', date);
                    
                var attrCond = new com.runwaysdk.geodashboard.gis.persist.condition.DashboardLessThanOrEqual();
                attrCond.setComparisonValue(value);
                attrCond.setDefiningMdAttribute(mdAttribute);
                    
                conditions.push(attrCond);
                criteria.push({'type':'ATTRIBUTE_CONDITION', 'mdAttribute':mdAttribute, 'operation':'le', 'value':value});
              }                          
            }
          }          
        });
        
        // Add the boolean
        $( '.jcf-class-filter-boolean.rad-checked' ).each(function( index ) {          
          var input = $(this).siblings().first();
            var value = input.attr('value');
            
            var mdAttribute = input.attr('name').replace('filter-', '');   
            
            var attrCond = new com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqual();
            attrCond.setComparisonValue(value);
            attrCond.setDefiningMdAttribute(mdAttribute);
            
            conditions.push(attrCond);
            criteria.push({'type':'ATTRIBUTE_CONDITION', 'mdAttribute':mdAttribute, 'operation':'eq', 'value':value});                
        });
        
        // Add the geo entity filter
        var location = $('#filter-geo-hidden').val();
        
        if(location != null && location.length > 0)
        {
          var condition = new com.runwaysdk.geodashboard.gis.persist.condition.LocationCondition();
          condition.setComparisonValue(location);
            
          conditions.push(condition);          
          criteria.push({'type':'LOCATION_CONDITION', 'operation':'eq', 'value':location});                          
        }
        
        return {'conditions' : conditions, 'criteria' : criteria};
      },
      
      _onClickExportReport : function(e) {
        $( "#report-menu" ).hide();
        
      	var format = $(e.target).data('format');

        var criteria = this._conditionMap['criteria']; 
        
        this._exportReport(this._currGeoId, criteria, format);
      },
      
      _onClickApplyFilters : function(e) {
        var that = this;
        
        // Validate there are no existing errors
        var errorCount = $('.gdb-attr-filter.field-error').length
        
        if(errorCount > 0) {
          var message = com.runwaysdk.Localize.localize("filter", "error");
          
          var dialog = com.runwaysdk.ui.Manager.getFactory().newDialog(com.runwaysdk.Localize.get("rError", "Error"), {modal: true});
          dialog.appendContent(message);
          dialog.addButton(com.runwaysdk.Localize.get("rOk", "Ok"), function(){dialog.close();}, null, {primary: true});
          dialog.setStyle("z-index", 2001);
          dialog.render();          
        }
        else {
          this._conditionMap = this._buildConditionMap();
            
          var criteria = this._conditionMap['criteria']; 
          var conditions = this._conditionMap['conditions'];
                    
          var clientRequest = new Mojo.ClientRequest({
            onSuccess : function(json, calledObj, response) {
              var jsonObj = Mojo.Util.toObject(json);
               
              that._updateCacheFromJSONResponse(jsonObj);
                
              that._addUserLayersToMap(true);
                
              that._drawLegendItems()
                
              // TODO : Push this somewhere as a default handler.
              that.handleMessages(response);
            },
            onFailure : function(e) {
              that.handleException(e);
            }
          });
            
          com.runwaysdk.geodashboard.gis.persist.DashboardMap.updateConditions(clientRequest, this._mapId, conditions);
          
          this._currGeoId = '';
            
          this._renderReport(this._currGeoId, criteria);
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
      
      /**
       * Renders the mapping widget, performing a full refresh.
       */
      render : function(){
        var that = this;
        
        this.fullRefresh();
        
        // Make sure all openers for each attribute have a click event
        $('a.attributeLayer').on('click', Mojo.Util.bind(this, this._openLayerForAttribute));
        $('a.new-dashboard-btn').on('click', Mojo.Util.bind(this, this._openNewDashboardForm));
        $('a.apply-filters-button').on('click', Mojo.Util.bind(this, this._onClickApplyFilters));
        
        // Events for exporting a report
        $('.report-export').on('click', Mojo.Util.bind(this, this._onClickExportReport));        
        $('#report-menu-button').on('click', function(){$( "#report-menu" ).toggle();});        
        
        // Render the menu
        $( "#report-menu" ).menu();
        $( "#report-menu" ).hide();
        
        
        if(this._googleEnabled){
          this._addAutoComplete();
        }    
        
        //
        // Chart panel slide toggle behavior
        //
        $("#reporting-toggle-button").on('click', function(){
          var reportContentHeight = parseInt($("#report-viewport").css("height"));
          if($("#reporticng-container").hasClass("report-panel-closed"))
          {
            $("#reporticng-container").animate({ bottom: "+="+ reportContentHeight +"px" }, 1000, function() {
                  $("#reporticng-container").removeClass("report-panel-closed");
             });
          }
          else
          {
            $("#reporticng-container").animate({ bottom: "-="+ reportContentHeight +"px" }, 1000, function() {
                  $("#reporticng-container").addClass("report-panel-closed");
             });
          }
        });
        
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
          
          var mdAttribute = $(this).attr('id').replace('filter-char-', '');   
          
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
              
              com.runwaysdk.geodashboard.Dashboard.getTextSuggestions(req, mdAttribute, request.term, 10);
            },
            minLength: 2
          });
        });
        
        // Hook up the filter auto-complete for term attributes
        $('.filter-term').each(function(){
          var mdAttribute = $(this).attr('id').replace('filter-term-', '');   
          
          $(this).autocomplete({
            source: function( request, response ) {
              var req = new Mojo.ClientRequest({
                onSuccess : function(classifiers){
                  var results = [];
                  
                  $.each(classifiers, function( index, classifier ) {
                    var label = classifier.getDisplayLabel().getLocalizedValue();
                    var id = classifier.getId();
                    
                    results.push({'label':label, 'value':label, 'id':id});
                  });
                  
                  response( results );
                },
                onFailure : function(e){
                  that.handleException(e);
                }
              });
              
              com.runwaysdk.geodashboard.Dashboard.getClassifierSuggestions(req, mdAttribute, request.term, 10);
            },
            select: function(event, ui) {
                $('#filter-hidden-' + mdAttribute ).val(ui.item.id);
            },           
            minLength: 2
          });
        });
        
        // Hook up the filter auto-complete for the global location filter
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
        });
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
