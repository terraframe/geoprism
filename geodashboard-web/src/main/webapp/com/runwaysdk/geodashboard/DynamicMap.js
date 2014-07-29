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
        this._mdAttribute = null;
        this._map = new L.Map(this._mapDivId,{zoomAnimation: false,zoomControl: true});
        
        this._defaultOverlay = null;
        this._currentOverlay = null;
        
        // key => value = dom id => Layer
        this._overlayLayers = new com.runwaysdk.structure.HashMap();
        
        // key -> value = object => Layer
        this._layerCache = new com.runwaysdk.structure.HashMap();     
        
        // The current base map (only one at a time is allowed)
        this._defaultBase = null;
        this._currentBase = null;
        this._baseLayers = new com.runwaysdk.structure.HashMap();
        
        this._suggestionCoords = new com.runwaysdk.structure.HashMap();      
        this._autocomplete = null;
        this._responseCallback = null;
        
        this._rendered = false;
        
        var bound = Mojo.Util.bind(this, this._overlayHandler);
        overlayLayerContainer.on('click', 'a', bound);
        
        this._LayerController = com.runwaysdk.geodashboard.gis.persist.DashboardLayerController;
        
        // set controller listeners
        this._LayerController.setCancelListener(Mojo.Util.bind(this, this._cancelListener));
        this._LayerController.setApplyWithStyleListener(Mojo.Util.bind(this, this._applyWithStyleListener));
        
        
        overlayLayerContainer.sortable({
          update: Mojo.Util.bind(this, this._overlayLayerSortUpdate)
        });
        overlayLayerContainer.disableSelection();
      },
      
      _overlayLayerSortUpdate : function(event, ui) {
        var that = this;
        
        
        // Calculate an array of layer ids
        var layerIds = [];
        var layers = $("#overlayLayerContainer").find("input");
        for (var i = 0; i < layers.length; ++i) {
          var layer = $(layers[i]);
          layerIds.push(layer.data("id"));
        }
        layerIds.reverse();
        
        var clientRequest = new Mojo.ClientRequest({
          onSuccess : function(json){
            var jsonObj = Mojo.Util.toObject(json);
            that._refreshMapInternal(jsonObj);
          },
          onFailure : function(e) {
            that.handleException(e);
          }
        });
        
        com.runwaysdk.geodashboard.gis.persist.DashboardMap.orderLayers(clientRequest, this._mapId, layerIds);
      },
      
      _overlayHandler : function(e){
        
        var that = this;
        
        var el = $(e.currentTarget);
        if(el.hasClass('ico-edit')){
          
          // edit the layer
          var id = el.data('id');
          this._LayerController.edit(new Mojo.ClientRequest({
            onSuccess : function(html){
              that._displayLayerForm(html, true);
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
       * from all caches and the map itself.
       * 
       * @param id
       */
      _removeLayer : function(el, id){
        
        var toRemove = this._layerCache.get(id);
        
        // remove layer from both caches
        this._layerCache.remove(id);

        var name = toRemove.id;
        this._overlayLayers.remove(name);
        
        // remove the actual layer from the map        
        this._map.removeLayer(toRemove);
        
        // remove the layer from the map and UI
        // FIXME use selector
        el.parent().parent().remove();
        
        this._refreshMap();
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
        
        var request = new Mojo.ClientRequest({
          onSuccess : function(html){
            if(Mojo.Util.trim(html).length === 0){
              that._closeLayerModal();
              that._refreshMap();
            }
            else {
              // we got html back, meaning there was an error
              that._displayLayerForm(html, false);
            }
          },
          onFailure : function(e){
            that.handleException(e);
          }
        });
        
        params['mapId'] = this._mapId;
        params['style.mdAttribute'] = this._mdAttribute;
        
        // Custom conversion to turn the checkboxes into boolean true/false
        params['style.enableLabel'] = params['style.enableLabel'].length > 0;
        params['style.enableValue'] = params['style.enableValue'].length > 0;
        params['layer.displayInLegend'] = params['layer.displayInLegend'].length > 0;
        return request;
      },
      
      /**
       * 
       * @param params
       */
      _cancelListener : function(params){
        
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
       * Return all allowable base maps.
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
       */
      _renderBaseLayerSwitcher : function(base){
        
    	that = this;
        
        // Create the HTML for each row (base layer representation).
        var ids = [];
        baseLayers = new com.runwaysdk.structure.HashMap();
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
       * @param that - reference to this at method scope
       * @param targetCheckbox - target checkbox Checkbox object
      */
      _toggleBaseLayer : function(targetCheckbox){
    	  
    	target = targetCheckbox;
      	targetId = target.getId();
    	targetEl = document.getElementById(targetId);

    	var ids = baseLayers.keySet();   	  	   	
    	var isChecked = target.isChecked();
  	
  	  	if(isChecked){
  	  		for(var i=0; i<ids.length; i++){ 
  	  			if(ids[i] !== targetId){
  	  				$("#"+ids[i]).removeClass('checked');
  	  				var otherBaselayer = baseLayers.get(ids[i]);
			        that._map.removeLayer(otherBaselayer);  	  				
  	  			}
  	  			else{
  	  				var newBaselayer = baseLayers.get(targetId);
  	  				that._map.addLayer(newBaselayer);
  	  			}
  	  		}
  	  	}
  	  	else{
			var unchecklayer = baseLayers.get(targetId);
	        that._map.removeLayer(unchecklayer);
  	  	}
      },
      
      /**
       * Toggles the overlay layers of the map.
       * 
       * @param e
       */
      _selectOverlayLayer : function(e){
        
          var changed = e.currentTarget;
          var changedId = changed.id;
          var changedLayer = this._overlayLayers.get(changedId);
          var ids = this._overlayLayers.keySet();
          
          var newOverlayLayer = null;
          if(changed.checked){
            for(var i=0; i<ids.length; i++){
              var id = ids[i];
              if(id === changedId){
                newOverlayLayer = changedLayer;
                this._map.addLayer(newOverlayLayer);
              }
            }
          }
          else{
            for(var i=0; i<ids.length; i++){                
                  var id = ids[i];
                  if(id === changedId){
                    var uncheck = document.getElementById(id);
                    uncheck.checked = false;
                    jcf.customForms.refreshElement(uncheck);
                  
                    var removeLayer = this._overlayLayers.get(id);
                    this._map.removeLayer(removeLayer);
                  }
            }
          }
      },
      
      /**
       * Builds the autocomplete and renders the map using Leaflet.
       */
      render : function(){
        
        this._refreshMap();
        
        // Make sure all openers for each attribute have a click event
        $('a.attributeLayer').on('click', Mojo.Util.bind(this, this._openLayerForAttribute));
        
        if(this._googleEnabled){
          this._addAutoComplete();
        }
        else {
          
        }
      },
      
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
      
      _refreshMapInternal : function(jsonObj){
        
        // FIXME Lewis
        if(this._rendered){
          $('#'+DynamicMap.BASE_LAYER_CONTAINER).html('');
          $('#'+DynamicMap.OVERLAY_LAYER_CONTAINER).html('');
          this._baseLayers.clear();
          this._map.remove();
          $('#'+this._mapDivId).html('');
          this._map = new L.Map(this._mapDivId,{zoomAnimation: false,zoomControl: true});
        }
        
        var jsonBbox = jsonObj.bbox; 
        var jsonLayers = jsonObj.layers;
        
        // Handle points & polygons 
        if (jsonBbox.length === 2){
        	var center = L.latLng(jsonBbox[1], jsonBbox[0]);
        	this._map.setView(center, 9);
        }
        else if (jsonBbox.length === 4){
        		var swLatLng = L.latLng(jsonBbox[1], jsonBbox[0]);
        		var neLatLng = L.latLng(jsonBbox[3], jsonBbox[2]);            
        		var bounds = L.latLngBounds(swLatLng, neLatLng);   

        		this._map.fitBounds(bounds);
        }

        // Add attribution to the map
        this._map.attributionControl.setPrefix('');
        this._map.attributionControl.addAttribution("TerraFrame | GeoDashboard");

        // Hide mouse position coordinate display when outside of map
        this._map.on('mouseover', function(e) {
          $(".leaflet-control-mouseposition.leaflet-control").show();
        });

        this._map.on('mouseout', function(e) {
          $(".leaflet-control-mouseposition.leaflet-control").hide();
        });

        L.control.mousePosition({emptyString:"",position:"bottomleft",prefix:"Lat: ",separator:" Long: "}).addTo(this._map);

        // Add Base Layers to map and layer switcher panel
        var base = this._getBaseLayers();                                
        this._map.addLayer(base[0]); 
        this._renderBaseLayerSwitcher(base);

        var container = $('#'+DynamicMap.OVERLAY_LAYER_CONTAINER);
        
        //// Add associated Overlays
        // @viewName
        // @sldName - must be a valid style registered with geoserver (no .sld extension) or the default for that layer will be used.
        // @displayName
        // @geoserverName - must be include workspace and layername (ex: workspace:layer_name).         
        var html = '';
        var ids = [];
        // First create the html objects that represent the layers
        var layers = [];
        var htmlInfo = [];
        for(var i = 0; i < jsonLayers.length; i++){
          var layerObj = jsonLayers[i];
          
          var viewName = layerObj.viewName;
          var sldName = layerObj.sldName || "";  // This should be enabled we wire up the interface or set up a better test process
          var displayName = layerObj.layerName || "N/A";
          var geoserverName = DynamicMap.GEOSERVER_WORKSPACE + ":" + viewName;
          var layerId = layerObj.layerId;

          var layer = L.tileLayer.wms(window.location.origin+"/geoserver/wms/", {
            layers: geoserverName,
            format: 'image/png',
            transparent: true,
            styles: sldName
          });
          layers.push(layer);

          var id = 'overlay_layer_'+i;
          var b = layer;
          b.id = id;  
          ids.push(id);  
          this._overlayLayers.put(id, b);
          this._layerCache.put(layerId, b);

          // This if statement is completely unneeded but makes sure a single layer is rendered on the map.  
          // It often helps new users to see an overlay in action on initial map load.
          if(i === 0){
            this._currentOverlay = this._defaultOverlay = layer;
            //checked = 'checked="checked"';
          }
          
          this._map.addLayer(layers[i]);
          htmlInfo.push({id: id, layerId: layerId, displayName: displayName});
        }
        
        htmlInfo.reverse();
        for(var i = 0; i < htmlInfo.length; i++){
          var layerObj = htmlInfo[i];
          
          // Create the HTML for each row (base layer representation).
          var checked = 'checked="checked"';
          
          html += '<div class="row-form">';
          html += '<input data-id="'+layerObj.layerId+'" id="'+layerObj.id+'" class="check" type="checkbox" '+checked+'>';
          html += '<label for="'+layerObj.id+'">'+layerObj.displayName+'</label>';
          html += '<div class="cell"><a href="#" data-id="'+layerId+'" class="ico-remove">remove</a>';
          html += '<a href="#" data-id="'+layerObj.layerId+'" class="ico-edit">edit</a>';
          html += '<a href="#" data-id="'+layerObj.layerId+'" class="ico-control">control</a></div>';
          html += '</div>';
        }
        
        // combine the rows into new HTML that goes in to the layer switcher
        var rows = $(html);
        var el = container[0];

        container.append(rows);               
        jcf.customForms.replaceAll(el);

        // add event handlers to manage the actual check/uncheck process
        for(var i=0; i<ids.length; i++){
          var id = ids[i];
          var check = $('#'+id);                    
          var handler = Mojo.Util.bind(this, this._selectOverlayLayer);
          check.on('change', this._overlayLayers.get(id), handler);
        }
        
        this._rendered = true;       
      },
      
      _refreshMap : function(){
        
        var that = this;
        com.runwaysdk.geodashboard.gis.persist.DashboardMap.getMapJSON(
            new Mojo.ClientRequest({
                onSuccess : function(json){
                  var jsonObj = Mojo.Util.toObject(json);
                  that._refreshMapInternal(jsonObj);
                },
                onFailure : function(e){
                  that.handleException(e);
                }
            })
            , this._mapId, '{testKey:"TestValue"}');
      },
      
      _openLayerForAttribute : function(e){
        e.preventDefault();      
        
        var el = $(e.currentTarget);
        var attrId = el.data('id');
        this._mdAttribute = attrId;
        
        var that = this;
        
        var request = new Mojo.ClientRequest({
          onSuccess : function(html){
            that._displayLayerForm(html, false);
          },
          onFailure : function(e){
            that.handleException(e);
          }
        });
        
        this._LayerController.newInstance(request);
        
      },
      
      _displayLayerForm : function(html, forceShow){
    	  
        // clear all previous color picker dom elements
        $(".colpick.colpick_full.colpick_full_ns").remove();
        
        var modal = $(DynamicMap.LAYER_MODAL).first();       
        if(forceShow){
          modal.modal('show');
        }
        
        var exec = Mojo.Util.extractScripts(html);
        
        modal.html(html);
        jcf.customForms.replaceAll(modal[0]);
        
        
        // Add layer styling event listeners
        this._selectColor();
        this._selectLayerType();
        
        eval(exec);
        
        // Move reusable cells to active cell holder
        var activeTab = $("#layer-type-styler-container").children(".tab-pane.active")[0].id;
        if (activeTab === "tab001basic") {
          this.__attachDynamicCells($("#gdb-reusable-basic-stroke-cell-holder"), $("#gdb-reusable-basic-fill-cell-holder"));
        }
        else if (activeTab === "tab003gradient") {
          this.__attachDynamicCells($("#gdb-reusable-gradient-stroke-cell-holder"), $("#gdb-reusable-gradient-fill-cell-holder"));
        }
        
        // Attach listeners
        $('a[data-toggle="tab"]').on('shown.bs.tab', Mojo.Util.bind(this, this._onLayerTypeTabChange));
      },
      
      
      __attachDynamicCells : function(strokeCellHolder, fillCellHolder) {
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
          this.__attachDynamicCells($("#gdb-reusable-basic-stroke-cell-holder"), $("#gdb-reusable-basic-fill-cell-holder"));
          $("#tab001basic").show();
        }
        else if (type === "bubble") {
          $("#tab002bubble").show();
        }
        else if (type === "gradient") {
          this.__attachDynamicCells($("#gdb-reusable-gradient-stroke-cell-holder"), $("#gdb-reusable-gradient-fill-cell-holder"));
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
      }
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
