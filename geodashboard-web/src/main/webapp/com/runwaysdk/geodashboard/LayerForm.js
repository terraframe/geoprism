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
 
  var LayerForm = Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.LayerForm', {
    Extends : com.runwaysdk.ui.Component,  
    IsAbstract : true,    
    Constants : {
      LAYER_MODAL : '#modal01'
    },
    Instance : {
      initialize : function(map, mapId){
        this._map = map;
        this._mapId = mapId;
      },
      
      _onApplySuccess : {
        IsAbstract : true,            
      },        
      
      /**
       * Closes the overlay with the layer/style CRUD.
       * 
       */
      _closeLayerModal : function(){
//        this.getImpl().modal('hide').html('');
      },

      getImpl : function(){
        return $(LayerForm.LAYER_MODAL);
      },
      
      
      /**
       * Renders the layer creation/edit form
       * 
       * @html
       */
      _displayLayerForm : function(html, layer){
        
        // clear all previous color picker dom elements
        $(".colpick.colpick_full.colpick_full_ns").remove();
        
        // Show the white background modal.
        var modal = this.getImpl().first();
        modal.html(html);
                
        
        // IMPORTANT: This line must be run last otherwise the user will see javascript loading and modifying the DOM.
        //            It is better to finish all of the DOM modification before showing the modal to the user
        modal.modal('show');
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
            that._onApplySuccess(htmlOrJson, response);
          },
          onFailure : function(e){
            that.handleException(e);
          }
        }, this.getImpl()[0]);
        
        params['mapId'] = this._mapId;
        
        // Custom conversion to turn the checkboxes into boolean true/false
//        params['style.enableLabel'] = params['style.enableLabel'].length > 0;
        
        // A hack to set the enableValue property which is required on DashboardLayer but is
        // not allowed for the reference layer form since ther is no 'value' to display
//        if(!params['style.enableValue']){
//          params['style.enableValue'] = false;
//        }
//        else{
//          params['style.enableValue'] = params['style.enableValue'].length > 0;
//        }
//        params['layer.displayInLegend'] = params['layer.displayInLegend'].length > 0;
        
        return request;
      }
    },
    Static : {
    }
  });
  
  var ThematicLayerForm = Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.ThematicLayerForm', {
    Extends : LayerForm,  
    Constants : {
        GEO_AGG_LEVEL_DD : "#agg-level-dd",
        GEO_AGG_METHOD_DD : "#agg-method-dd",
        GEO_AGG_HOLDER : "#agg-level-holder",
        GEO_TYPE_HOLDER : "#geom-type-holder",
        GEO_NODE_SELECT_EL : "#geonode-select"
    },
    Instance : {
          
      initialize : function(map, mapId){
        this.$initialize(map, mapId);
        
        this._LayerController = com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerController;        
        this._LayerController.setCancelListener(Mojo.Util.bind(this, this._cancelLayerListener));
        this._LayerController.setApplyWithStyleListener(Mojo.Util.bind(this, this._applyWithStyleListener));       
      },
      
      _displayLayerForm : function(html, layer) {
    	  var that = this;
      	  this.$_displayLayerForm(html, layer);
      },
      
      
      edit : function(id, $compile, $scope) {
        var that = this;
        that.$compile = $compile;
        that.$scope = $scope;
        
        var layer = this._map.getLayer(id);
        that._layer = layer;
      
        this._LayerController.edit(new com.runwaysdk.geodashboard.StandbyClientRequest({
          onSuccess : function(html){
            that._displayLayerForm(html, that._layer);
            
            // compiling the new html for Angular
            that.$compile($("#DashboardLayer-mainDiv")[0])($scope);
          },
          onFailure : function(e){
            that.handleException(e);
          }
        }, this.getImpl()[0]), id);  
      },
      
      open : function(thematicAttributeId, $compile, $scope) {
        var that = this;
        that.$compile = $compile;
        that.$scope = $scope;
          
        var request = new com.runwaysdk.geodashboard.StandbyClientRequest({
          onSuccess : function(html){
            that._displayLayerForm(html, "");
            
            // compiling the new html for Angular
            that.$compile($("#DashboardLayer-mainDiv")[0])($scope);
          },
          onFailure : function(e){
            that._closeLayerModal();
            that.handleException(e);
          }
        }, this.getImpl()[0]);  
          
        this._LayerController.newThematicInstance(request, thematicAttributeId, this._mapId);      
      },
      
      
      /**
       * 
       * @params
       */
      // TODO: remove me
      _cancelLayerListener : function(params){
      },
      
      // TODO: remove me
      _onApplySuccess : function(htmlOrJson, response) {
      },
      
      /**
       * Called when a user submits (creates/updates) a layer with styles.
       * 
       * @params
       */
      _applyWithStyleListener : function(params){
        
//        var layer = this._map.getLayer(params["layer.componentId"]);
//        var mdAttribute = (layer != null) ? layer.mdAttributeId : this._thematicAttributeId;
//        
//        params['layer.mdAttribute'] = mdAttribute;        
//        params['state'] = JSON.stringify(this._map.getModel());
//        
//        if($("#tab005categoriespolygon").is(":visible")){
//          var catStyleArr = this._updateCategoriesJSON("#categories-polygon-input");
//          params['style.categoryPolygonStyles'] = JSON.stringify(catStyleArr);
//        }
//        
//        if($("#tab007categoriespoint").is(":visible")){
//            var catStyleArr = this._updateCategoriesJSON("#categories-point-input");
//            params['style.categoryPointStyles'] = JSON.stringify(catStyleArr);
//          }
//        
//        // Check for existense of dynamic settings which may not exist 
//        if(params['style.bubbleContinuousSize']){
//          params['style.bubbleContinuousSize'] = params['style.bubbleContinuousSize'].length > 0;
//        }
//        
//        var secondaryAttribute = $("#secondaryAttribute").val();
//        
//        if(secondaryAttribute != null && secondaryAttribute.length > 0) {
//          var secondaryCategories = this._getSecondaryAttriubteCategories();
//          var secondaryAggregationType = $("#secondaryAggregation").val();
//            
//          if(secondaryCategories != null && secondaryCategories.length > 0){
//            params['style.secondaryAttribute'] = secondaryAttribute;
//            params['style.secondaryAggregationType'] = secondaryAggregationType;
//            params['style.secondaryCategories'] = JSON.stringify(secondaryCategories);
//          }          
//        }
//        else {
//          params['style.secondaryAttribute'] = '';
//          params['style.secondaryAggregationType'] = '';
//          params['style.secondaryCategories'] = '';
//        }
//        
//        // Get the strategy information
//        var strategy = $(ThematicLayerForm.GEO_AGG_LEVEL_DD).find(":selected");
//        var strategyType = strategy.data('type');
//        var strategyValue = strategy.val();
//        
//        params["strategy.isNew"] = "true";
//        params["#strategy.actualType"] = strategyType;
//        
//        if(strategyType == "com.runwaysdk.geodashboard.gis.persist.UniversalAggregationStrategy") {
//          params["strategy.universal"] = strategyValue;
//        }
//        
//        var request = this.$_applyWithStyleListener(params);
//        
//        return request;
      },
      
      
      /**
       * Scrapes categories on the layer creation/edit form
       * 
       * @inputId - The id for the <input> element used to store categories data
       */
      // TODO:  This will be replaced by scraping the model in angular
      _updateCategoriesJSON : function(inputId) {
//        var styleArr = new Object();
//        styleArr.catLiElems = []; // create an empty array to prevent system errors if the ontolgy tree doesnt render leaving catLiElems undefined
//        
//        for(var i=0; i<this._categoryWidgets.length; i++){
//          if(this._categoryWidgets[i]._storeId === inputId){
//            styleArr.catLiElems = this._categoryWidgets[i].getCategories();
//           
//            // set the hidden input element in the layer creation/edit form 
//            $(this._categoryWidgets[i]._storeId).data("categoriesstore", encodeURIComponent(JSON.stringify(styleArr)));
//          }
//        }
//           
//        return  styleArr;
      }
    }
  });
  
  var ReferenceLayerForm = Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.ReferenceLayerForm', {
    Extends : LayerForm,  
      
    Instance : {
            
      initialize : function(map, mapId){
        this.$initialize(map, mapId);
      
        this._ReferenceLayerController = com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerController;
        this._ReferenceLayerController.setCancelListener(Mojo.Util.bind(this, this._cancelReferenceLayerListener));
        this._ReferenceLayerController.setApplyWithStyleListener(Mojo.Util.bind(this, this._applyWithStyleListener)); 
      },
      
      _onApplySuccess : function(htmlOrJson, response) {
        if (response.isJSON(htmlOrJson, response)) {
          this._closeLayerModal();
              
          var returnedLayerJSON = JSON.parse( htmlOrJson);
          var jsonObj = {};
          jsonObj["refLayers"] = [Mojo.Util.toObject(htmlOrJson)];
              
          this._map.handleLayerEvent(jsonObj);
              
          this.handleMessages(response);
        }
        else if (response.isHTML()) {
          // we got html back, meaning there was an error
              
          this._displayLayerForm(htmlOrJson);
          this._addLayerFormControls();
              
          this.getImpl().animate({scrollTop:$('.heading').offset().top}, 'fast'); // Scroll to the top, so we can see the error
        }        
      },
      
      /**
       * 
       * @params
       */
      _cancelReferenceLayerListener : function(params){
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
          com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayer.unlock(request, id);
        }
      },
      
      /**
       * Edit form for reference layers  
       * 
       * @param e
       */
      edit : function(id){
        var that = this;
      
        // edit the layer
        var request = new com.runwaysdk.geodashboard.StandbyClientRequest({
          onSuccess : function(html){
            that._displayLayerForm(html);
            that._addLayerFormControls();
          },
          onFailure : function(e){
            that.handleException(e);
          }
        }, this.getImpl()[0]);  
      
        this._ReferenceLayerController.edit(request, id);
      },
      
      open : function(universalId){
        var that = this;
      
        var request = new com.runwaysdk.geodashboard.StandbyClientRequest({
          onSuccess : function(html){
            that._displayLayerForm(html);
            that._addLayerFormControls();
                  
          },
          onFailure : function(e){
            that._closeLayerModal();
            that.handleException(e);
          }
        }, this.getImpl()[0]);  
              
        this._ReferenceLayerController.newReferenceInstance(request, universalId, this._mapId);        
      }
    }
  });
  
})();
  
  
