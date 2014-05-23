(function(){
  
  var DynamicMap = Mojo.Meta.newClass(GDB.Constants.GIS_PACKAGE+'DynamicMap', {
    
    Constants : {
      BASE_LAYER_CONTAINER : 'baseLayerContainer',
      OVERLAY_LAYER_CONTAINER : 'overlayLayerContainer',
      GEOSERVER_WORKSPACE : 'geodashboard',
      GEOCODE : 'geocode',
      GEOCODE_LABEL : 'geocodeLabel'
    },
    
    Instance : {
      
      /**
       * Constructor
       * @param mapDivId DOM id of the map div.
       */
      initialize : function(mapDivId, mapId){
        this.$initialize();
        
        this._mapDivId = mapDivId;
        this._mapId = mapId;
        this._map = new L.Map(this._mapDivId,{zoomAnimation: false,zoomControl: true});
        
        this._defaultOverlay = null;
        this._defaultOverlayId = null;
        this._currentOverlay = null;
        this._overlayLayers = new com.runwaysdk.structure.HashMap();     
        
        // The current base map (only one at a time is allowed)
        this._defaultBase = null;
        this._defaultBaseId = null;
        this._currentBase = null;
        this._baseLayers = new com.runwaysdk.structure.HashMap();
        this._suggestionCoords = new com.runwaysdk.structure.HashMap();
        
        this._autocomplete = null;
        this._responseCallback = null;
      },
      
      /**
       * Return all allowable base maps.
       */
      getBaseLayers : function(){
        
        // the SATELLITE layer has all 22 zoom level, so we add it first to
        // become the internal base layer that determines the zoom levels of the
        // map.
        var gsat = new L.Google('SATELLITE');        	
        var gphy = new L.Google('TERRAIN');       
        var gmap = new L.Google('ROADMAP');       
        var ghyb = new L.Google('HYBRID');
        
        // need to set Zindex on this so it remains underneath other layers
        var osm = new L.TileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'); 
        osm._gdbcustomtype = 'OSM';
        
        var base = [gmap, gsat, ghyb, gphy, osm];
        
        return base;
      },
      
      /**
       * Renders each base layer as a checkable option in
       * the layer switcher.
       */
      _renderBaseLayerSwitcher : function(base){
        
        // Create the HTML for each row (base layer representation).
        var html = '';
        var ids = [];
        for(var i=0; i<base.length; i++){
          
          var id = 'base_layer_'+i;
          
          var b = base[i];
          b.id = id;
          
          var checked = '';
          if(i === 0){
            this._currentBase = this._defaultBase = base[0];
            checked = 'checked="checked"';
          }
          
          ids.push(id);
          
          // Assigning better display labels.
          var label = '';
          if(b._type === 'ROADMAP'){
        	  label = 'Google Streets';
          }
          else if(b._type === 'SATELLITE'){
        	  label = 'Google Satellite';
          }
          else if(b._type === 'TERRAIN'){
        	  label = 'Google Terrain';
          }
          else if(b._type === 'HYBRID'){
        	  label = 'Google Hybrid';
          }
          else if(b._gdbcustomtype === 'OSM'){
        	  label = 'Open Street Map';
          }

          html += '<div class="row-form">';
          html += '<input id="'+id+'" class="check" type="checkbox" '+checked+'>';
          html += '<label for="'+id+'">'+ label +'</label>';
          html += '</div>';
          
          this._baseLayers.put(id, b);
        }

        // combine the rows into new HTML that goes in to the layer switcher
        var rows = $(html);

        var container = $('#'+DynamicMap.BASE_LAYER_CONTAINER);
        var el = container[0];
        
        container.append(rows);
        
        jcf.customForms.replaceAll(el);
        
        // add event handlers to manage the actual check/uncheck process
        for(var i=0; i<ids.length; i++){
          var id = ids[i];
          var check = $('#'+id);
          
          var handler = Mojo.Util.bind(this, this._selectBaseLayer);
          check.on('change', this._baseLayers.get(id), handler);
        }
        
      },
      
      /**
       * Changes the base layer of the map.
       * 
       * @param e
       */
      _selectBaseLayer : function(e){
    	  				
        var changed = e.currentTarget;
        var changedId = changed.id;
        var changedLayer = this._baseLayers.get(changedId);
        
        var ids = this._baseLayers.keySet();
        
        var newBaseLayer = null;
        if(changed.checked){
          // uncheck other base layers without firing the event (to avoid infinte event looping)
          for(var i=0; i<ids.length; i++){
            
            var id = ids[i];
            if(id === changedId){
              newBaseLayer = changedLayer;
            }
            else{
              var uncheck = document.getElementById(id);
              uncheck.checked = false;
              jcf.customForms.refreshElement(uncheck);
              
              var layer = this._baseLayers.get(id);
              this._map.removeLayer(layer);             	
            }
          }
        }
        else {
          
          // because a base is required, make sure to keep the default base checked
          var base = document.getElementById(this._defaultBase.id);
          base.checked = true;
          jcf.customForms.refreshElement(base);
          
          // reset to the base layer
          changedLayer.setVisibility(false);
          newBaseLayer = this._defaultBase;
        }
        
        
        // refresh the map with the base layer (unless it's already the base).
        // Because we're using allOverlays = true, we must set the base layer by 
        // lowering the index to 0 (instead of setBaseLayer()).
//        newBaseLayer.setVisibility(true);
        this._map.addLayer(newBaseLayer);
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
                  
                  var lon = row.geometry.location.e;
                  var lat = row.geometry.location.d;
                  that._suggestionCoords.put(row.formatted_address, [lon, lat]);
                }
                response(suggestions);
              }
            });      
          },
        });
//        that._autocomplete.show();
        
        //$('#'+DynamicMap.GEOCODE).on('keypress', Mojo.Util.bind(this, this._geocodeHandler));

        com.runwaysdk.geodashboard.gis.persist.DashboardMap.getMapJSON(
    		new Mojo.ClientRequest({
        		onSuccess : function(json){
        			var jsonObj = Mojo.Util.toObject(json);
        			var jsonBbox = jsonObj.bbox; 
        			var jsonLayers = jsonObj.layers;
        			
        			if(jsonBbox.length < 4){
        				// Incomplete BBox array.  Must have 4 coordinate sets.
        				// Set to a basic default 
        				that._map.setView(L.LatLng(39.79, -464.97), 9); 
        			}
        			else {
        				var swLatLng = L.latLng(jsonBbox[1], jsonBbox[0]);
        				var neLatLng = L.latLng(jsonBbox[3], jsonBbox[2]);
        		  
        				var bounds = L.latLngBounds(swLatLng, neLatLng);
        		  
        			//// This is now intantiated in the initialize function
//        			that._map = new L.Map(that._mapDivId, {
//        				center: new L.LatLng(39.79, -464.97), 
//        				zoom: 9,
//        				zoomAnimation: false,
//        				zoomControl: true
//        			});
        	        
        				that._map.fitBounds(bounds);
        			}
        	        
        	        that._map.attributionControl.setPrefix('');
        	        that._map.attributionControl.addAttribution("TerraFrame | GeoDashboard");
        	        
        	        L.control.mousePosition().addTo(that._map);
        	        
        	        // Add Base Layers
        	        var base = that.getBaseLayers();       	               	        
        	        that._map.addLayer(base[0]); 
        	        that._renderBaseLayerSwitcher(base);
        	        
        	        // Add Overlays
        	        //// This needs:
        	        ////	to add custom style param
        	        ////	to have more confidence that geoserver services exist
        	        // Create the HTML for each row (base layer representation).
        	        var html = '';
        	        var ids = [];
        	        for(var i = 0; i < jsonLayers.length; i++){
        	        	var viewName = jsonObj.layers[i].viewName
        	        	var displayName = jsonObj.layers[i].layerName
        	        	var geoserverName = DynamicMap.GEOSERVER_WORKSPACE + ":" + viewName;
        	        	
        	        	var layer = L.tileLayer.wms(window.location.origin+"/geoserver/wms/", {
        	        		layers: geoserverName,
        	        		format: 'image/png',
        	        		transparent: true
        				});        			

        	            var id = 'overlay_layer_'+i;
        	            
        	            var b = layer;
        	            b.id = id;
        	            
        	            var checked = '';
        	            
        	            // This is completely unneeded but makes sure a single layer is rendered on the map.  
        	            // It often helps new users to see an overlay in action on initial map load.
        	            if(i === 0){
        	              this._currentOverlay = this._defaultOverlay = layer;
        	              checked = 'checked="checked"';
        	              that._map.addLayer(layer);
        	            }
        	            
        	            ids.push(id);
        	            
        	            html += '<div class="row-form">';
        	            html += '<input id="'+id+'" class="check" type="checkbox" '+checked+'>';
        	            html += '<label for="'+id+'">'+displayName+'</label>';
        	            html += '<div class="cell"><a href="#" class="ico-remove">remove</a><a href="#" class="ico-edit">edit</a><a href="#" class="ico-control">control</a></div>';
        	            html += '</div>';
        	            
        	            that._overlayLayers.put(id, b);
        	          }

        	          // combine the rows into new HTML that goes in to the layer switcher
        	          var rows = $(html);

        	          var container = $('#'+DynamicMap.OVERLAY_LAYER_CONTAINER);
        	          var el = container[0];
        	          
        	          container.append(rows);
        	          
        	          jcf.customForms.replaceAll(el);
        	          
        	          // add event handlers to manage the actual check/uncheck process
        	          for(var i=0; i<ids.length; i++){
        	            var id = ids[i];
        	            var check = $('#'+id);
        	            
        	            var handler = Mojo.Util.bind(that, that._selectOverlayLayer);
        	            check.on('change', that._overlayLayers.get(id), handler);
        	          }	       
        		}
    		})
    		,"yynpw8x3rmgs50lfay9s5j3co7ged60szojgdxrfm5h1jt5d5afsrgoboiuxxb5d");   
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

