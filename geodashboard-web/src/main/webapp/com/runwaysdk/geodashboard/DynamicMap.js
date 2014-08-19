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
   "osmBasic" : "Open Street Map"
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
      FROM_DATE : 'from-field'
    },
    
    Instance : {
      
      /**
       * Constructor
       * @param mapDivId DOM id of the map div.
       */
      initialize : function(mapDivId, mapId){
        this.$initialize();
        
        var overlayLayerContainer = $('#'+DynamicMap.OVERLAY_LAYER_CONTAINER);
        
        this._googleEnabled = Mojo.Util.isObject(Mojo.GLOBAL.google);
        
        this._mapDivId = mapDivId;
        this._mapId = mapId;
        
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
        
        var legendBound = Mojo.Util.bind(this, this._legendClickHandler);
        $(".legend-opener").on('click', legendBound);
        
        $("#legend-list").sortable({
            update: Mojo.Util.bind(this, this._legendSortUpdate)
        });
        
        this._LayerController = com.runwaysdk.geodashboard.gis.persist.DashboardLayerController;
        this._DashboardController = com.runwaysdk.geodashboard.DashboardController;
        
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
      },
      
      /**
       * Requests all map data again from the server (bounds, persisted layers, etc...) and updates our internal caches.
       */
      _updateCachedData : function(fnSuccess) {
        var that = this;
        
        com.runwaysdk.geodashboard.gis.persist.DashboardMap.getMapJSON(
          new Mojo.ClientRequest({
            onSuccess : function(json){
              var jsonObj = Mojo.Util.toObject(json);
              
              that._updateCacheFromJSONResponse(jsonObj);
              
              fnSuccess();
            },
            onFailure : function(e){
              that.handleException(e);
            }
          })
          , this._mapId, '{testKey:"TestValue"}');
      },
      
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
      
      
      _renderBaseLayers : function() {
        this._baseLayers.clear();
        
        var base = this._getBaseLayers();                                
        this._map.addLayer(base[0]); 
        this._renderBaseLayerSwitcher(base);
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
    	  
    	  $("#legend-list").html('');
    	  
          var layers = this._layerCache.values();
          
          for(var i = layers.length-1; i >= 0; i--){
            var layer = layers[i];
            var displayName = layer.getLayerName() || "N/A";
            var geoserverName = DynamicMap.GEOSERVER_WORKSPACE + ":" + layer.getViewName();
            
            var html = '';
  			html += '<li class="legend-item">';
  			html += '<img src="'+window.location.origin+'/geoserver/wms?REQUEST=GetLegendGraphic&amp;VERSION=1.0.0&amp;FORMAT=image/png&amp;WIDTH=25&amp;HEIGHT=25&amp;LEGEND_OPTIONS=bgColor:0x302822;fontAntiAliasing:true;fontColor:0x515796;fontSize:12;fontStyle:bold;&amp;LAYER='+geoserverName+'&amp;SCALE=700000" alt="">'+ displayName;
  			html += '</li>';
  			
  			$("#legend-list").append(html);
          }
      },
      
      /**
       * Build HTML for user defined overlays
       */
      _drawUserLayersHMTL : function(htmlInfo) {
        var container = $('#'+DynamicMap.OVERLAY_LAYER_CONTAINER);
        var onCheckHandler = Mojo.Util.bind(this, this._toggleOverlayLayer);
        var html = '';
        var layers = this._layerCache.values();
        
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
        var that = this;     
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
//              that._addUserLayersToMap(false); // We can't only add the new layer here because the logic is different for create/update and we can't know at this point which one we're at
              that._addUserLayersToMap();            
              
              // TODO : Push this somewhere as a default handler.
              that.handleMessages(response);
            }
            else if (response.isHTML()) {
              // we got html back, meaning there was an error
              that._displayLayerForm(htmlOrJson);
              $('#modal01').animate({scrollTop:$('.heading').offset().top}, 'fast'); // Scroll to the top, so we can see the error
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
          
          params["condition.comparisonValue"] = textValue;
          params["condition.isNew"] = "true";
          params["#condition.actualType"] = condition;
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
        }
        else {
          this._map.removeLayer(layer.leafletLayer);
        }
      },
      
      /**
       * Removes and re-adds all overlay leaflet layer objects that are checked 
       * to maintain the order of the layers in the sidebar
       * 
       */
//      _addSortedOverlays : function(){
//        var layers = []; 
//        
//          // Function for keeping sort order
//          function sortByKey(array, key) {
//            return array.sort(function (a, b) {
//              var x = a[key];
//              var y = b[key];
//              return ((x < y) ? -1 : ((x > y) ? 1 : 0));
//            });
//          }
//          
//          var checkboxLayerIds = this._getActiveOverlayOrderedArrayIds();
//          for (var i = 0; i < checkboxLayerIds.length; i++) {
//            var layer = this._overlayLayers.get(checkboxLayerIds[i]);
//            
//            // Remove all existing layers to re-add in order later
//            this._map.removeLayer(layer);
//            
//            // add to a sorting array
//            zIndex = $.inArray(checkboxLayerIds[i], checkboxLayerIds);
//            layers.push({
//              "z-index": zIndex,
//              "layer": layer
//            });
//          }
//          
//          // Sort layers array by z-index
//          var orderedLayers = sortByKey(layers, "z-index");
//          
//          // Loop through ordered layers array and add to map in correct order
//          that = this;
//          $.each(orderedLayers, function () {
//            that._map.addLayer(that._overlayLayers.get(this.layer.id));
//          });
//      },
      
      /**
       * Disables the search functionality if google can't be loaded
       */
      _disableAutoComplete : function(){
        $('#'+DynamicMap.GEOCODE).attr('disabled', 'disabled');
      },
      
      /**
       * Hooks the auto-complete functionality to the Dashboard.
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
        
        this._LayerController.newInstance(request);
        
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
        
        // Attach listeners
        $('a[data-toggle="tab"]').on('shown.bs.tab', Mojo.Util.bind(this, this._onLayerTypeTabChange));
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
        
        if (type === "basic") {
          this._attachDynamicCells($("#gdb-reusable-basic-stroke-cell-holder"), $("#gdb-reusable-basic-fill-cell-holder"));
          $("#tab001basic").show();
        }
        else if (type === "bubble") {
          $("#tab002bubble").show();
        }
        else if (type === "gradient") {
          this._attachDynamicCells($("#gdb-reusable-gradient-stroke-cell-holder"), $("#gdb-reusable-gradient-fill-cell-holder"));
          $("#tab003gradient").show();
        }
        else if (type === "categories") {
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
      _legendClickHandler : function(e){
          
          $("#legend-items-container").slideToggle("slow");
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
        
        if(this._googleEnabled){
          this._addAutoComplete();
        }
        else {
          
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
