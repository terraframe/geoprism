(function(){
  
  var DynamicMap = Mojo.Meta.newClass(GDB.Constants.GIS_PACKAGE+'DynamicMap', {
    
    Constants : {
      BASE_LAYER_CONTAINER : 'baseLayerContainer'
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
        this._currentBase = null;
      },
      
      /**
       * Return all allowable base maps.
       */
      getBaseLayers : function(){
        
        // the SATELLITE layer has all 22 zoom level, so we add it first to
        // become the internal base layer that determines the zoom levels of the
        // map.
        var gsat = new OpenLayers.Layer.Google(
            "Google Satellite",
            {type: google.maps.MapTypeId.SATELLITE, numZoomLevels: 22}
        );
        var gphy = new OpenLayers.Layer.Google(
            "Google Physical",
            {type: google.maps.MapTypeId.TERRAIN, visibility: false}
        );
        var gmap = new OpenLayers.Layer.Google(
            "Google Streets", // the default
            {numZoomLevels: 20, visibility: false}
        );
        var ghyb = new OpenLayers.Layer.Google(
            "Google Hybrid",
            {type: google.maps.MapTypeId.HYBRID, numZoomLevels: 22, visibility: false}
        );
        
        var base = [gsat, gphy, gmap, ghyb];
        this._currentBase = base[0];
        
        return base;
      },
      
      /**
       * Renders the 
       */
      _renderBaseLayerSwitcher : function(base){
        
        var html = '';
        for(var i=0; i<base.length; i++){
          
          var b = base[i];
          
          var checked = i===0 ? 'checked="checked"' : '';
          var id = 'f'+i;

          html += '<div class="row-form">';
          html += '<input id="'+id+'" class="check" type="checkbox" '+checked+'>';
          html += '<label for="'+id+'">'+b.name+'</label>';
          html += '</div>';
        }

        var rows = $(html);
        $('#'+DynamicMap.BASE_LAYER_CONTAINER).append(rows);
      },
      
      /**
       * Renders the map using OpenLayers.
       */
      render : function(){
        
        this._map = new OpenLayers.Map(this._mapId, {allOverlays: true, theme: null});

        // Render the div for base layers (we do this custom because the OpenLayers default 
        // would be painful to implement in our styles)
        var base = this.getBaseLayers();
        
        this._renderBaseLayerSwitcher(base);
        
        
        this._map.addLayers(base);

        // Google.v3 uses EPSG:900913 as projection, so we have to
        // transform our coordinates
        this._map.setCenter(new OpenLayers.LonLat(10.2, 48.9).transform(
            new OpenLayers.Projection("EPSG:4326"),
            this._map.getProjectionObject()
        ), 5);        
        
      }
    }
    
  });
  
})();