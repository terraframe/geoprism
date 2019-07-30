/*
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
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
    
    Static : {
     isJson : function(str) {
          try {
              JSON.parse(str);
          } catch (e) {
              return false;
          }
          return true;
      }
    },
    
    Instance : {
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
