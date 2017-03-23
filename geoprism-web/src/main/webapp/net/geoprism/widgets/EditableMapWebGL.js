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
(function() {
  function EditableMapWebGLController($scope, $rootScope, localizationService, webGLMapService, locationService, $sce, $compile) {
    var controller = this;

    $scope.renderBase = true;
    $scope.baseLayers = [];
    $scope.contextStyle = {
      fill : "rgba(0, 0, 0, 0.25)",
      strokeColor : "rgba(0, 0, 0, 0.75)",
      strokeWidth : 5,
      radius : 7
    };
    $scope.targetStyle = {
      fill : "rgba(161, 202, 241, 0.75)",
      strokeColor : "rgba(255, 0, 0, 0.75)",
      strokeWidth : 3,
      radius : 7
    };
    $scope.sharedGeoData = {};


    controller.init = function() {

//      var emptyGeoJSON = {
//        "type" : "FeatureCollection",
//        "totalFeatures" : 0,
//        "features" : [],
//        "crs" : {
//          "type" : "name",
//          "properties" : {
//            "name" : "urn:ogc:def:crs:EPSG::4326"
//          }
//        }
//      };

      // Create editing control
      var map = controller.getWebGLMap();
      controller._editingControl = new MapboxDraw({
        controls : {
          point : false,
          line_string : false,
          polygon : true,
          trash : true,
          combine_features : false,
          uncombine_features : false
        }
      });
      map.addControl(controller._editingControl);
      
      // Define the GeoprismEditingControl
      controller._geoprismEditingControl = new net.geoprism.gis.GeoprismEditingControl(controller);
      map.addControl(controller._geoprismEditingControl);
      
      controller._isEditing = false;
      
      controller._updatedGeos = {};
      map.on("draw.update", controller.onDrawUpdate);
    }

    controller.getWebGLMap = function() {
      return webGLMapService.getWebGlMap();
    }

    controller.baseLayerPanelMouseOut = function() {
      $scope.showBaseLayerPanel = false;
    }

    controller.toggleBaseLayer = function(targetLayer) {
      $scope.baseLayers.forEach(function(lyr) {
        if (lyr.layerId === targetLayer.layerId) {
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

    controller.addNewPointControl = function(feature, saveCallback) {
      mapService.addNewPointControl(feature, saveCallback);
    }

    controller.zoomToLayersExtent = function(layersArr) {
//      webGLMapService.zoomToLayersExtent(layersArr);
    }

    controller.zoomToExtentOfFeatures = function(featureGeoIds) {
//      webGLMapService.zoomToExtentOfFeatures(featureGeoIds);
    }

    controller.selectFeature = function(feature) {
      if (controller._isEditing) { return; }
      
      webGLMapService.selectFeature(feature);
    }

    controller.unselectFeature = function(feature) {
      webGLMapService.unselectFeature(feature);
    }

    controller.focusOnFeature = function(feature) {
      webGLMapService.focusOnFeature(feature);
    }

    controller.focusOffFeature = function(feature) {
      webGLMapService.focusOffFeature(feature);
    }

    controller.addVectorLayer = function(layerGeoJSON, layerName, styleObj, type, stackingIndex) {
      webGLMapService.addVectorLayer(layerGeoJSON, layerName, styleObj, type, stackingIndex);
    }

    controller.updateVectorLayer = function(source, layers) {
      webGLMapService.updateVectorLayer(source, layers);
    }
    
    controller.startNewGeoSession = function() {
      if (controller._isEditing) { controller.cancelEditing(); return; }
      
      var map = controller.getWebGLMap();
      
      this.unselectFeature(null);
      
      // After all the hard stuff has been done, now we can enable editing controls
      controller._geoprismEditingControl.startNewGeoSession();
 
      controller._isEditing = true;
    }
    
    controller.cancelNewGeoSession = function() {
      if (!controller._isEditing) { return; }
      
      var map = controller.getWebGLMap();
      
      controller._geoprismEditingControl.stopNewGeoSession();

      controller._editingControl.deleteAll();
      
      controller._isEditing = false;
    }
    
    // The first thing that happens when they click the edit button. We need to get proper geometries (not simplified) and lock all the GeoEntities
    controller.openEditingSession = function() {
      if (controller._isEditing) { controller.cancelEditing(); return; }
      
      var config = $scope.sharedGeoData[0].config;
      config.x = 0;
      config.y = 0;
      config.z = 0;
      
      var connection = {
        elementId : '#innerFrameHtml',
        onSuccess : function(geom) {
          controller.startEditingFeatures(geom);
        },
        onFailure : function(error) {
          // TODO : Proper error handling
        }
      };
      locationService.openEditingSession(connection, config);
    }

    controller.startEditingFeatures = function(geometries) {
      var map = controller.getWebGLMap();
      
      this.unselectFeature(null);
        
      // The draw plugin has a bug in it that throws a warning 'crs is deprecated' as an error.
      geometries.crs = undefined;
      
      this._editingControl.add(geometries);
        
      // After all the hard stuff has been done, now we can enable editing controls
      controller._geoprismEditingControl.startEditing();
 
      map.setFilter("target-multipolygon", [ "==", "id", "" ]);
      
      controller._isEditing = true;
    }

    controller.cancelEditing = function() {
      if (!controller._isEditing) { return; }
      
      var config = $scope.sharedGeoData[0].config;
      
      var connection = {
        elementId : '#innerFrameHtml',
        onSuccess : function() {
          var map = controller.getWebGLMap();
          
          controller._geoprismEditingControl.stopEditing();

          map.setFilter("target-multipolygon", [ "!=", "id", "" ]);
          controller._editingControl.deleteAll();
          
          controller._isEditing = false;
        },
        onFailure : function(error) {
          // TODO : Proper error handling
          console.log(error);
        }
      };
      locationService.cancelEditingSession(connection, config);
    }
    
    controller.saveNewGeoSession = function() {
      if (!controller._isEditing) { return; }
      
      var featureCollection = this._editingControl.getAll();
      
      // Convert to WKT
      var geojson = null;
      var features = featureCollection.features;
      if (features.length > 1)
      {
        geojson = {
          "type": "MultiPolygon",
          "coordinates": []
        };
        
        for (var i = 0; i < features.length; ++i)
        {
          geojson.coordinates.push(features[i].geometry.coordinates);
        }
      }
      else if (features.length === 1)
      {
        geojson = features[0];
      }
      else
      {
        controller.cancelNewGeoSession();
        return;
      }
      
      var _wkt = wellknown.stringify(geojson);
      
      $scope.$emit('locationEditNew', {
        wkt: _wkt,
        afterApply: function(){
          location.reload();
        }
      });
    }
    
    controller.onDrawUpdate = function(event) {
      var feats = event.features;
      
      for (var i = 0; i < feats.length; ++i)
      {
        var feat = feats[i];
        
        controller._updatedGeos[feat.id] = true;
      }
    };
    
    controller.saveEditing = function() {
      if (!controller._isEditing) { return; }
      
      var map = controller.getWebGLMap();
      
      var featureCollection = this._editingControl.getAll();
      
      // Filter out features that haven't been updated
      var updatedFeatureCollection = {type:"FeatureCollection", features:[]};
      var updatedFeatures = updatedFeatureCollection.features;
      
      var feats = featureCollection.features;
      for (var i = 0; i < feats.length; ++i)
      {
        var feat = feats[i];
        
        if (controller._updatedGeos.hasOwnProperty(feat.id))
        {
          updatedFeatures.push(feat);
        }
        else
        {
          updatedFeatures.push({
            id: feat.id,
            type: "unlock"
          })
        }
      }
      controller._updatedGeos = {};
      
      var connection = {
        elementId : '#innerFrameHtml',
        onSuccess : function(data) {
          controller._isEditing = false;
          
          controller._geoprismEditingControl.stopEditing();
          
          map.setFilter("target-multipolygon", [ "!=", "id", "" ]);
          
          // TODO: It might be better to update the rendered polygons purely clientside, but that would require converting from whatever
          //   format the draw plugin exports into something that the mapbox gl plugin knows how to handle. This may be easy? I don't know.
          //   At least this way if the editing persists in some weird way its immediately obvious to the user, even if its slower.
          controller._editingControl.deleteAll();
          $scope.$emit('locationReloadCurrent');
        },
        onFailure : function(error) {
          // TODO : Proper error handling
          console.log(error);
        }
      };
      locationService.applyGeometries(connection, updatedFeatureCollection);
    };

    controller.refreshBaseLayer = function() {
      if ($scope.baseLayers.length > 0) {
        for (var i = 0; i < $scope.baseLayers.length; i++) {
          var layer = $scope.baseLayers[i];

          if (layer.isActive) {
            webGLMapService.showLayer(layer, 0);
          } else {
            webGLMapService.hideLayer(layer);
          }
        }
      }
    }

    controller.refreshInteractiveLayers = function(triggeringEvent) {
      if (!isEmptyJSONObject($scope.sharedGeoData)) {
        var data = $scope.sharedGeoData[0];
          
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
          is3d: false            
        }];
          
        controller.updateVectorLayer(data, layers);
      }
    }

    controller.refreshWithContextLayer = function(triggeringEvent) {
      console.log("EditableMapWebGL::refreshWithContextLayer");
      
      if (!isEmptyJSONObject($scope.sharedGeoData)) {
        var data = $scope.sharedGeoData[0];
          
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
          is3d: false            
        }];
        

        var hoverCallback = function(featureId) {
          if (!controller._isEditing)
          {
            $scope.$emit('hoverChange', {
              id : featureId
            });
            $scope.$apply();
            
            return true;
          }
          
          return false;
        }

        var featureClickCallback = function(feature, map) {
          if (!controller._isEditing)
          {
            $scope.$emit('locationFocus', {
              id : feature.properties.id
            });
            $scope.$apply();
            
            return true;
          }
          
          return false;
        }
        
        controller.updateVectorLayer(data, layers);
        
        controller.addVectorHoverEvents(hoverCallback, "target-multipolygon");
        controller.addVectorClickEvents(featureClickCallback, "target-multipolygon");
      }
    }

    function isEmptyJSONObject(obj) {
      for ( var prop in obj) {
        if (obj.hasOwnProperty(prop))
          return false;
      }

      return true && JSON.stringify(obj) === JSON.stringify({});
    }

    $scope.$on('listHoverOver', function(event, data) {
      if (!controller._isEditing)
      {
        controller.focusOnFeature(data);
      }
    });

    $scope.$on('listHoverOff', function(event, data) {
      if (!controller._isEditing)
      {
        controller.focusOffFeature(data);
      }
    });

    $scope.$on('listItemClick', function(event, data) {
      if (!controller._isEditing)
      {
        controller.zoomToExtentOfFeatures(data.geoIds)
      }
    });

    $scope.$on('editLocation', function(event, data) {
      controller.openEditingSession(data.id);
    });
    
    $scope.$on('cancelEditLocation', function(event, data) {
      controller.cancelEditing();
    });

    // Recieve shared data from parent controller based on user selection of
    // target location
    console.log("EditableMapWebGL::on sharedGeoData");
    $scope.$on('sharedGeoData', function(event, data) {
      console.log("EditableMapWebGL::sharedGeoData");
      
      if (!isEmptyJSONObject(data)) {

        $scope.sharedGeoData = data;

        controller.refreshWithContextLayer('sharedGeoData');
      } else if (!isEmptyJSONObject($scope.sharedGeoData)) {
        controller.refreshWithContextLayer('sharedGeoData');
      }
    });

    $scope.$on("layerChange", function(event, data) {
      $scope.sharedGeoData = data;

      if ($scope.includeContextLayer) {
        controller.refreshWithContextLayer('layerChange');
      } else {
        controller.refreshInteractiveLayers('layerChange');
      }
    });

    // $scope.$watch("activeBaseLayer", function(newVal, oldVal) {
    // if(newVal){
    // console.log("watched")
    // }
    // });

    controller.init();
  }
  
  Mojo.Meta.newClass('net.geoprism.gis.GeoprismEditingControl', {
    Extends : com.runwaysdk.ui.Component,  
    IsAbstract : false,
    Instance : {
     
      initialize : function(controller) {
        this._controller = controller;
      },
      
      onAdd : function(map) {
        var that = this;
        this._map = map;
        
        this._container = $(document.createElement('div'));
        this._container.addClass('mapboxgl-ctrl-group mapboxgl-ctrl');

        this._bEdit = $(document.createElement("button"));
        this._bEdit.addClass('fa fa-pencil-square-o');
        this._bEdit.css("color", "black");
        this._bEdit.css("font-size", "18px");
        this._bEdit.click(function() {
          that._controller.openEditingSession(null);
        });
        this._container.append(this._bEdit);
        
        this._bNew = $(document.createElement("button"));
        this._bNew.addClass('fa fa-plus-square-o');
        this._bNew.css("color", "black");
        this._bNew.css("font-size", "18px");
        this._bNew.click(function() {
          that._controller.startNewGeoSession();
        });
        this._container.append(this._bNew);
        
        this._bSave = $(document.createElement("button"));
        this._bSave.addClass('fa fa-floppy-o');
        this._bSave.css("color", "black");
        this._bSave.css("display", "none");
        this._bSave.css("font-size", "16px");
        this._bSave.click(function() {
          that.onClickSave();
        });
        this._container.append(this._bSave);
        
        this._bCancel = $(document.createElement("button"));
        this._bCancel.addClass('fa fa-ban');
        this._bCancel.css("color", "black");
        this._bCancel.css("display", "none");
        this._bCancel.css("font-size", "16px");
        this._bCancel.click(function() {
          that.onClickCancel();
        });
        this._container.append(this._bCancel);
        
        this.stopEditing();
        
        return this._container[0];
      },
      
      onRemove : function() {
        this._container.parentNode.removeChild(this._container);
        this._map = undefined;
      },
      
      onClickSave : function() {
        if (this._isNewGeoSession)
        {
          this._controller.saveNewGeoSession();
        }
        else if (this._controller._isEditing)
        {
          this._controller.saveEditing();
        }
      },
      
      onClickCancel : function() {
        if (this._isNewGeoSession)
        {
          this._controller.cancelNewGeoSession();
        }
        else if (this._controller._isEditing)
        {
          this._controller.cancelEditing();
        }
      },
      
      startNewGeoSession: function() {
        this._bEdit.css("display", "none");
        this._bNew.css("display", "none");
        this._bSave.css("display", "block");
        this._bCancel.css("display", "block");
        
        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_trash").removeAttr("style");
//        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_line").removeAttr("style");
        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_polygon").removeAttr("style");
//        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_combine").removeAttr("style");
//        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_uncombine").removeAttr("style");
        
        this._isNewGeoSession = true;
      },
      
      stopNewGeoSession: function() {
        this._bEdit.css("display", "block");
        this._bNew.css("display", "block");
        this._bSave.css("display", "none");
        this._bCancel.css("display", "none");
        
        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_trash").css("display", "none");
//        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_line").css("display", "none");
        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_polygon").css("display", "none");
//        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_combine").css("display", "none");
//        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_uncombine").css("display", "none");
        
        this._isNewGeoSession = false;
      },
      
      startEditing: function() {
        this._bEdit.css("display", "none");
        this._bNew.css("display", "none");
        this._bSave.css("display", "block");
        this._bCancel.css("display", "block");
        
        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_trash").removeAttr("style");
//        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_line").css("display", "none");
        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_polygon").css("display", "none");
//        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_combine").css("display", "none");
//        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_uncombine").css("display", "none");
      },
      
      stopEditing : function() {
        this._bEdit.css("display", "block");
        this._bNew.css("display", "block");
        this._bSave.css("display", "none");
        this._bCancel.css("display", "none");
        
        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_trash").css("display", "none");
//        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_line").css("display", "none");
        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_polygon").css("display", "none");
//        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_combine").css("display", "none");
//        $(".mapbox-gl-draw_ctrl-draw-btn.mapbox-gl-draw_uncombine").css("display", "none");
      }
    }
  });

  function EditableMapWebGL() {
    return {
      restrict : 'E',
      replace : true,
      templateUrl : com.runwaysdk.__applicationContextPath
          + '/partial/widgets/editable-map-webgl.jsp',
      scope : {
        includeContextLayer : '=includeContextLayer',
        baseMapType : "@baseMapType"
      },
      controller : EditableMapWebGLController,
      controllerAs : 'ctrl',
      link : function(scope, element, attrs, ctrl) {
      }
    }
  }
  
  angular.module("editable-map-webgl", [ "styled-inputs", "localization-service", "webgl-map-service", "location-service" ]);
  angular.module("editable-map-webgl").controller('editableMapWebglController', EditableMapWebGLController)
  	.directive('editableMapWebgl', EditableMapWebGL)
})();
