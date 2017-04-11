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
  function SimpleMapWebGLController($scope, $rootScope, localizationService, webGLMapService, locationService, $sce, $compile) {
   var controller = this;
    
   $scope.renderBase = true;
   $scope.baseLayers = [];
   $scope.contextStyle = {fill:"rgba(0, 0, 0, 0.25)", strokeColor:"rgba(0, 0, 0, 0.75)", strokeWidth:5, radius:7};
   $scope.targetStyle = {fill:"rgba(161, 202, 241, 0.80)", strokeColor:"rgba(102, 102, 102, 0.80)", strokeWidth:2, radius:7};
   $scope.featureStyle = {fill:"rgba(37, 202, 125, 0.9)", strokeColor:"rgba(0, 0, 0, 0.75)", strokeWidth:5, radius:7};
   $scope.featurePointStyle = {fill:"rgba(37, 202, 125, 0.9)", strokeColor:"rgba(0, 0, 0, 0.75)", strokeWidth:3, radius:7};
   $scope.sharedGeoData = {};
   
   
   controller.init = function() {
     
   }
   
  controller.hoverCallback = function(featureId){
      
      var isMobile = false; //initiate as false
      // device detection
      if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|ipad|iris|kindle|Android|Silk|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(navigator.userAgent) 
          || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(navigator.userAgent.substr(0,4))){
        
        $scope.$emit('locationFocus', {
                  id : featureId
            });
            $scope.$apply();
        
      }
      else{
            $scope.$emit('hoverChange', {
                  id : featureId
            });
            $scope.$apply();
      }
    }
    
    controller.featureClickCallback = function(feature, map){
      $scope.$emit('locationFocus', {
                id : feature.properties.id
          });
          $scope.$apply();
    }
   
   
   controller.baseLayerPanelMouseOut = function() {
     $scope.showBaseLayerPanel = false;
   }
   
   controller.toggleBaseLayer = function(targetLayer) {
     $scope.baseLayers.forEach(function(lyr){
       if(lyr.layerId === targetLayer.layerId){
         webGLMapService.toggleBaseLayer(lyr, $scope.activeBaseLayer);
         $scope.activeBaseLayer = targetLayer;
       }
     });
   }
   
   controller.removeVectorData = function() {
     webGLMapService.removeAllVectorLayers();
   }
   
   controller.getMapData = function(callback, data, workspace) {
     webGLMapService.getGeoJSONData(callback, data, workspace);
   }
   
   controller.addVectorClickEvents = function(featureClickCallback, layersArr) {
     webGLMapService.addVectorClickEvents(featureClickCallback, layersArr);
   }
   
   controller.addVectorHoverEvents = function(hoverCallback, layersArr) {
     webGLMapService.addVectorHoverEvents(hoverCallback, layersArr);
   }
   
   controller.zoomToLayersExtent = function(layersArr) {
     webGLMapService.zoomToLayersExtent(layersArr);
   }
   
   controller.zoomToExtentOfFeatures = function(featureGeoIds) {
     webGLMapService.zoomToExtentOfFeatures(featureGeoIds);
   }
   
   controller.zoomToExtent = function(bounds) {
     webGLMapService.zoomToExtent(bounds);
   }
   
   controller.focusOnFeature = function(feature) {
     webGLMapService.focusOnFeature(feature);
   }
   
   controller.focusOffFeature = function(feature) {
     webGLMapService.focusOffFeature(feature);
   }
    
   controller.updateVectorLayer = function(source, layers) {
     webGLMapService.updateVectorLayer(source, layers);
   }
   
    
   controller.refreshBaseLayer = function() {
        if($scope.baseLayers.length > 0) {
          for(var i = 0; i < $scope.baseLayers.length; i++) {
            var layer = $scope.baseLayers[i];
            
            if(layer.isActive) {
              webGLMapService.showLayer(layer, 0);          
            }
            else {
              webGLMapService.hideLayer(layer);        
            }
          }
        }
    }
   
   // TODO: This has legacy code. verify that IF this is being used it's doing as expected. 
   // Otherwise remove it.
   controller.refreshInteractiveLayers = function(triggeringEvent) {
     console.log("SimpleMapWebGL::RefreshInteractiveLayers")
      if(!isEmptyJSONObject($scope.sharedGeoData)){
        var data = $scope.sharedGeoData;
        
        var geomType;
        for(var l=0; l<data.features.length; l++){
          var layer = data.features[l];
          for(var i=0; i<layer.features.length; i++){
            var feature = layer.features[i];
            feature.properties.isHoverable = true;
            feature.properties.isClickable = true;
            
            // TODO: Remove this temp demo code
              if(feature.properties.displayLabel.startsWith("ES")){
                feature.properties.height = 0;
                feature.properties.base = 0;
                feature.properties.featureType = "boundary";
              }
              else{
                feature.properties.height = Math.round(Math.random() * 50);
                feature.properties.base = 0;
                feature.properties.featureType = "building";
              }
              
            geomType = feature.geometry.type.toLowerCase();
          }
          
          if(!geomType){
            geomType = "multipolygon";
          }
          controller.updateVectorLayer(layer, "target-" + geomType, $scope.targetStyle, "TARGET", 2);
        }
      }
   }
   
   
   controller.refreshWithContextLayer = function(triggeringEvent) {
        var source = $scope.sharedGeoData[0];
        
        var bboxObj;
        if(source && source.bbox){
          var bboxArr = JSON.parse(source.bbox);
          bboxObj= {sw : new mapboxgl.LngLat(bboxArr[0], bboxArr[1]), ne : new mapboxgl.LngLat(bboxArr[2], bboxArr[3])};
        }
          
        var layers = [{
          name: "context-multipolygon",
          style: $scope.contextStyle,
          layer: "context",
          type: "CONTEXT",
          index: 1,
          is3d: false                  
        },{
          name: "target-multipolygon",
          style: $scope.targetStyle,
          layer: "target",
          type: "TARGET",
          index: 2,
          is3d: true,
          bbox: bboxObj
        },{
          name: "feature-multipolygon",
          style: $scope.featureStyle,
          layer: "feature-polygon",
          type: "FEATURE",
          index: 3,
          is3d: false
        },{
          name: "feature-line",
          style: $scope.featureStyle,
          layer: "feature-line",
          type: "FEATURE",
          index: 4,
          is3d: false
        },{
          name: "feature-point",
          style: $scope.featurePointStyle,
          layer: "feature-point",
          type: "FEATURE",
          index: 5,
          is3d: false
        }];
          
        controller.updateVectorLayer(source, layers);  
        controller.zoomToLayersExtent([layers[1]]);
        
        controller.addVectorHoverEvents(controller.hoverCallback, ["target-multipolygon", "context-multipolygon"]);
        controller.addVectorClickEvents(controller.featureClickCallback, ["target-multipolygon", "context-multipolygon"]);
     }
   
     function isEmptyJSONObject(obj) {
      for(var prop in obj) {
          if(obj.hasOwnProperty(prop))
              return false;
      }

      return true && JSON.stringify(obj) === JSON.stringify({});
    }
      
      
      
      $scope.$on('listHoverOver', function(event, data){
        controller.focusOnFeature(data);
      });
      
      $scope.$on('listHoverOff', function(event, data){
        controller.focusOffFeature(data);
      });
      
      $scope.$on('listItemClick', function(event, data){
        controller.zoomToExtentOfFeatures(data.entities)
      });
      
      
      // Recieve shared data from parent controller based on user selection of target location
      $scope.$on('sharedGeoData', function(event, data) {
        if(!isEmptyJSONObject(data)){
          
          $scope.sharedGeoData = data;
          
          controller.refreshWithContextLayer('sharedGeoData');
        }
        else if(!isEmptyJSONObject($scope.sharedGeoData)) {
          controller.refreshWithContextLayer('sharedGeoData');
        }
      });
      
      
      $scope.$on("layerChange", function(event, data){
        $scope.sharedGeoData = data;
        
        if($scope.includeContextLayer){
          controller.refreshWithContextLayer('layerChange');
        }
        else{
          controller.refreshInteractiveLayers('layerChange');
        }
      });
      
      
      $scope.$on('init', function(event, data) {
    	  if(data.defaultExtent){
    		  controller.zoomToExtent(data.defaultExtent);
    	  }
      });
      
      controller.init();
  }
  
  
  function SimpleMapWebGL() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/widgets/simple-map-webgl.jsp',
      scope: {
        includeContextLayer:'=includeContextLayer',
        baseMapType:"@baseMapType"
      },
      controller : SimpleMapWebGLController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs, ctrl) {
      }
    }   
  }
  
  
  
  angular.module("simple-map-webgl", ["styled-inputs", "localization-service", "webgl-map-service", "location-service"]);
  angular.module("simple-map-webgl")
   .controller('simpleMapWebglController', SimpleMapWebGLController)
   .directive('simpleMapWebgl', SimpleMapWebGL)
})();
