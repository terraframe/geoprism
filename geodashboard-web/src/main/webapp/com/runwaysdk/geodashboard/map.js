(function(){
  
  var DynamicMap = Mojo.Meta.newClass(GDB.Constants.GIS_PACKAGE+'DynamicMap', {
    
    Constants : {
      BASE_LAYER_CONTAINER : 'baseLayerContainer',
      GEOCODE : 'geocode',
      GEOCODE_LABEL : 'geocodeLabel'
    },
    
    Instance : {
      
      /**
       * Constructor
       * @param mapId DOM id of the map div.
       */
      initialize : function(mapId){
        this.$initialize();
        
        this._mapId = mapId;
        this._map = null;
        
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
        
        var base = [gmap, gsat, gphy, ghyb];
        
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
          
          html += '<div class="row-form">';
          html += '<input id="'+id+'" class="check" type="checkbox" '+checked+'>';
          html += '<label for="'+id+'">'+b._type+'</label>';
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
       * Renders the map using Leaflet.
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
        //that._autocomplete.show();
        
        //$('#'+DynamicMap.GEOCODE).on('keypress', Mojo.Util.bind(this, this._geocodeHandler));
        
        this._map = new L.Map('map', {
			center: new L.LatLng(39.79, -464.97), 
			zoom: 9,
			zoomAnimation: false,
			zoomControl: true
		});

//////        this._map.addControl(new OpenLayers.Control.MousePosition());
        L.control.mousePosition().addTo(this._map);
        
        // Render the div for base layers 
        var base = this.getBaseLayers();

        this._renderBaseLayerSwitcher(base);

        this._map.addLayer(base[0]);   
        
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

