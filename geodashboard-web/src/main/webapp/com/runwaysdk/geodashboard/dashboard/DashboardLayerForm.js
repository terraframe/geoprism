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
	
//    $scope.apply = function() {
//        var request = new Mojo.ClientRequest({
//          onSuccess : function(dto){
//            $scope.dto = new com.runwaysdk.geodashboard.GeodashboardUser();              
//            $scope.$apply();
//            
//            $scope.refresh();
//          },
//          onFailure : function(e){
//            alert(e);
//          }
//        });
//        
//        populateFromJSON($scope.dto, $scope.model);
//        
//        $scope.dto.apply(request);
//      }
	
	 function layerNameInput() {
	    return {
	      restrict: 'E',
	      replace: true,
	      templateUrl: '/partial/dashboard/dashboard-layer-form-name.jsp',    
	      scope: {
	      },
	      link: function (scope, element, attrs) {
	    	  // format the partial
	    	  jcf.customForms.replaceAll(element[0]);
	      }
	    }    
	 }
	 
	function layerLabel() {
	    return {
	      restrict: 'E',
	      replace: true,
	      templateUrl: '/partial/dashboard/dashboard-layer-form-label.jsp',    
	      scope: true,
	      link: function (scope, element, attrs) {
	    	  // format the partial
//	    	  jcf.customForms.replaceAll(element[0]);
	    	  
	    	  //
	    	  // Setting up click events for checkboxes manually to hook custom ui elements to the angular model binding.
	    	  //
	    	  var enableValEl = document.getElementById("f51").previousSibling;
	    	  enableValEl.onclick = function() { 
	    		  var theEl = angular.element($(this).next()[0]);
	    		  if($(this).hasClass("chk-checked")){
	    			  theEl.triggerHandler('input'); // triggers the angular change event on the hidden input
	    			  scope.setEnableValue(false); // manually set the model to false because the change event hasn't occured yet
	    		  }
	    		  else{
	    			  theEl.triggerHandler('input'); // triggers the angular change event on the hidden input
	    			  scope.setEnableValue(true); // manually set the model to true because the change event hasn't occured yet
	    		  }
	    	  }
	    	  
	    	  //
	    	  // Setting up click events for checkboxes manually to hook the custom ui elements to the angular model binding.
	    	  //
	    	  var enableLabelEl = document.getElementById("f94").previousSibling;
	    	  enableLabelEl.onclick = function() { 
	    		  var theEl = angular.element($(this).next()[0]);
	    		  if($(this).hasClass("chk-checked")){
	    			  theEl.triggerHandler('input'); // triggers the angular change event on the hidden input
	    			  scope.setEnableLabel(false); // manually set the model to false because the change event hasn't occured yet
	    		  }
	    		  else{
	    			  theEl.triggerHandler('input'); // triggers the angular change event on the hidden input
	    			  scope.setEnableLabel(true); // manually set the model to true because the change event hasn't occured yet
	    		  }
	    	  }
	      }
	    }    
	}
	
	
	 function layerGeoNode() {
		    return {
		      restrict: 'E',
		      replace: true,
		      templateUrl: '/partial/dashboard/dashboard-layer-form-geonode.jsp',    
		      scope: true,
		      link: function (scope, element, attrs) {
		    	  // format the partial
//		    	  jcf.customForms.replaceAll(element[0]);
		      }
		    }    
		 }
	 
	 function layerAggregation() {
	    return {
	      restrict: 'E',
	      replace: true,
	      templateUrl: '/partial/dashboard/dashboard-layer-form-aggregation.jsp',    
	      scope: true,
	      link: function (scope, element, attrs) {
	    	  // format the partial
//		    	  jcf.customForms.replaceAll(element[0]);
	    	  
	    	  var geoNodeSelectId = "#geonode-select";
	          
	          // Populate the Aggregation Options based on the default selected GeoNode
	    	  scope.getGeographicAggregationOptions(scope.thematicLayerModel.aggregationStrategy, scope.thematicLayerModel.geoNodeId);
	    	  $(geoNodeSelectId).change(function(){ 
	    		  scope.getGeographicAggregationOptions(layer);
	    	  });
	      }
	    }    
	}
	
	var DashboardThematicLayerFormController = function($scope, $compile, layerFormService) {
		var controller = this;
		var geoNodeSelectId = "#geonode-select";
   	  	var geoTypeHolder = "#geom-type-holder";
   	  	var geoAggLevelSelectId = "#agg-level-dd";
   	  	var geoAggLevelHolder = "#agg-level-holder";
   	  	var geoAggMethodSelectId = "#agg-method-dd";
		
   	  	
		 /* Getting the $compile method reference for use with later functions  */
	    controller.$compile = $compile;
	    controller.$scope = $scope;
	    
		/* Initialization Function */
	    $scope.init = function(layerId, newInstance, availableFonts, geoNodes, geoNodeId, aggregations) {
	    	$scope.layerId = layerId;
	    	$scope.newInstance = Boolean(newInstance);
	    	$scope.availableFonts = processFonts(JSON.parse(decodeURIComponent(availableFonts)));
	    	$scope.geoNodes = JSON.parse(decodeURIComponent(geoNodes));
	    	
	    	controller.layerId = layerId;
	    	
	    	if(!newInstance){
	    	  $scope.loadExisting(controller.newInstance);
	    	  controller.loadLayerState()
	    	}
 
	      
		    /* Default State */ 
		    $scope.layerId = '';
		    $scope.newInstance = true; 
		    
		    // Dymanic model properties may be changed based on runway based ajax requests 
		    // They are kept out of other models to keep those models replicating server models more closely
		    $scope.dynamicDataModel = {
		    	aggregationLevelOptions : [],
		    	aggregationMethods : JSON.parse(decodeURIComponent(aggregations))
		    };

			$scope.thematicLayerModel = {
				viewName : '',
				sldName : '',
				layerName : '',
				layerId : '',
				inLegend : true,
				legendXPosition : 0,
				legendYPosition : 0,
				groupedInLegen : true,
				featureStrategy : '',
				mdAttributeId : '',
				attributeType : '',
				aggregationMethod : $scope.dynamicDataModel.aggregationMethods[0], 
				aggregationAttribute : '',
				layerType : '',
				attributeLabel : '',
				geoNodeId : geoNodeId.trim().length > 0 ? geoNodeId : $scope.geoNodes[0].id,
				aggregationStrategy : $scope.dynamicDataModel.aggregationLevelOptions[0],
				styles : ''
			};
			
			$scope.thematicStyleModel = {
				basicPointSize : '',
				enableLabel : true,
				enableValue : true,
				id : '',
				labelColor : '#000000;',
				availableFonts : $scope.availableFonts[0],
				labelFont : '', 
				labelHalo : '#ffffff',
				labelHaloWidth : '2',
				labelSize : "12",
				lineOpacity : '',
				lineStroke : '',
				lineStrokeCap : '',
				lineStrokeWidth : '',
				name : '',
				pointFill : '',
				pointOpacity : '',
				pointRotation : '',
				pointStroke : '',
				pointStrokeOpacity : '',
				pointStrokeWidth : '',
				pointWellKnownName : '',
				polygonFill : '',
				polygonFillOpacity : '',
				polygonStroke : '',
				polygonStrokeOpacity : '',
				polygonStrokeWidth : '',
				type : '',
				valueColor : '',
				valueFont : '',
				valueHalo : '',
				valueHaloWidth : '',
				valueSize : '',
				bubbleContinuousSize : '',
				bubbleFill : '',
				bubbleMaxSize : '',
				bubbleMinSize : '',
				bubbleOpacity : '',
				bubbleRotation : '',
				bubbleSize : '',
				bubbleStroke : '',
				bubbleStrokeOpacity : '',
				bubbleStrokeWidth : '',
				bubbleWellKnownName : '',
				categoryPointFillOpacity : '',
				categoryPointSize : '',
				categoryPointStroke : '',
				categoryPointStrokeOpacity : '',
				categoryPointStrokeWidth : '',
				categoryPointStyles : '',
				categoryPointWellKnownName : '',
				categoryPolygonFillOpacity : '',
				categoryPolygonStroke : '',
				categoryPolygonStrokeOpacity : '',
				categoryPolygonStrokeWidth : '',
				categoryPolygonStyles : '',
				gradientPointFillOpacity : '',
				gradientPointMaxFill : '',
				gradientPointMinFill : '',
				gradientPointSize : '',
				gradientPointStroke : '',
				gradientPointStrokeOpacity : '',
				gradientPointStrokeWidth : '',
				gradientPointWellKnownName : '',
				gradientPolygonFillOpacity : '',
				gradientPolygonMaxFill : '',
				gradientPolygonMinFill : '',
				gradientPolygonStroke : '',
				gradientPolygonStrokeOpacity : '',
				gradientPolygonStrokeWidth : '',
				secondaryAggregationType : '',
				secondaryAttribute : '',
				secondaryCategories : '',
				styleCondition : ''
			};
		
	    }
	    
	    controller.loadLayerState = function() {
	        
	        /* Clear the current state */
//	        controller.model = {
//	          location : {label:'',value:''},
//	          editDashboard : false,
//	          editData : false,
//	          types : [],
//	          mapId : ''
//	        };

	        
	        var onSuccess = function(json){
	          var state = JSON.parse(json);
	            
//	          controller.setLayerState(state);
	        };
	          
	        layerFormService.getThematicLayerJSON(controller.layerId, onSuccess);
	    }
	    
	    
	    controller.setLayerState = function(state) {
//	        $timeout(function() {
//	          controller.model = state;
//	          controller.renderBase = true;
//
//	          // Initialize the default base map
//	          var layerSourceType = controller.model.activeBaseMap["LAYER_SOURCE_TYPE"];
//	              
//	          for(var i = 0; i < controller.baseLayers.length; i++) {
//	            var layer = controller.baseLayers[i];
//	                  
//	            if(layer.layerType == layerSourceType) {
//	              layer.isActive = true;
//	            }
//	            else {
//	              layer.isActive = false;            
//	            }
//	          }            
//
//	          $scope.$apply();
//	                
//	          controller.refresh();
//	        }, 5);
	      }
	    
		  
    	// Fonts from Java come in as simple json array. We need to add id's to each entry for Angular.
		// It's important to note the obvious, that these id's are completely generic.
    	function processFonts(fonts){
    		var formattedFonts = [];
    		for(var i=0; i<fonts.length; i++){
    			formattedFonts.push({ "id" : i, "value" : fonts[i].replace(/\+/g, " ") });
    		}
    		return formattedFonts; 
    	}
	    
    	
	    // Aggregation strategy watcher
	    $scope.$watch("thematicLayerModel.aggregationStrategy", function(newValue, oldValue) {
	        console.log("watching ", " : ", "new val = ", newValue, " old val = ", oldValue)
            if($(geoAggLevelSelectId+" option:selected").hasClass("universal-leaf")){
                // Hide the attribute aggregation dropdown because aggregations are irrelevant at this level of universal
                $(geoAggMethodSelectId).parent().parent().hide();
              }
              else{
                $(geoAggMethodSelectId).parent().parent().show();
              }
              
              var selectedOption = $(geoAggLevelSelectId).find(":selected");
              if(selectedOption){
            	  _setLayerTypeOptions(selectedOption);
              }
	    });
	    
	    $scope.$watch("thematicLayerModel.aggregationMethod", function(newValue, oldValue) {
	        console.log("watching ", " : ", "new val = ", newValue, " old val = ", oldValue)
	    });
	    
	    
	    
	    $scope.setEnableValue = function(val){
	        $scope.thematicStyleModel.enableValue = val;
	    }; 
	    
	    $scope.setEnableLabel = function(val){
	        $scope.thematicStyleModel.enableLabel = val;
	    }; 
	    
	    
  	  	/**
         * 
         * @layer - the layer object representing the layer the form is targeting 
         * 
         */
	    $scope.getGeographicAggregationOptions = function(aggregationStrategy, geoNodeId){
            var that = this;
            
            // To set the option on a saved layer when initiating an edit session we get the
            // universal id OR geometry aggregation value id depending on the aggregation type of
            // the saved layer. 
            if(aggregationStrategy){
                that._selectedOption = aggregationStrategy;
            }
            else{
                that._selectedOption = null;
            }
            
            var clientRequest = new Mojo.ClientRequest({
                onSuccess : function(data) {
                  $scope.setGeographicAggregationOptions(data, that._selectedOption);
                },
                onFailure : function(e) {
                  //that.handleException(e);
                  // TODO: handle exception
                	console.log(e)
                  
                }
            });
            com.runwaysdk.geodashboard.gis.persist.AggregationStrategyView.getAggregationStrategies(clientRequest, geoNodeId);
	    }
	    
        /**
         * Populate the geographic aggregation dropdown
         * 
         * @aggregations - JSON representing geo aggregation levels
         */
         $scope.setGeographicAggregationOptions = function(aggregations, selectedOption) {
	             var selected = "";
	             
	             // Get the original name value from the originally rendered dropdown because this
	             // data was already passed from server to client in the jsp
	             var layerTypes = $(geoTypeHolder).data("layertypes");
	             
	             var allAggs = [];
	             for(var i=0; i<aggregations.length; i++){
	               var agg = aggregations[i];
	               if(selectedOption === agg.getValue()){
	                 selected = "selected";
	               }
	               else if(i === 0){
	                 selected = "selected";
	               }
	               else{
	                 selected = "";
	               }
	               var thisAgg = {"value":agg.getValue(), "type":agg.getAggregationType(), "geomTypes":agg.getAvailableGeometryTypes(), "displayLabel":agg.getDisplayLabel()}
	               allAggs.push(thisAgg)
	             }
	             
	             // Re-sets the options property on the model
	             $scope.dynamicDataModel.aggregationLevelOptions = allAggs;

//	             jcf.customForms.replaceAll($(geoAggLevelSelectId).parent().get(0));
	             
	             $(geoAggLevelHolder).show();
	             
	             var selectedOption = $(geoAggLevelSelectId).find(":selected");
	             _setLayerTypeOptions(selectedOption);
        }
         
         /**
          * Populate the layer type block based on the selection of the geonode and geo aggregation level dropdown
          * 
          * @selectedOption - selected option from the geographic aggregation level dropdown
          */
         function _setLayerTypeOptions(selectedOption) {
           var type = selectedOption.data('type');
           var layerTypes = $(geoTypeHolder).data("layertypes");
           var layerTypesJSON = getValueFromHTML(layerTypes);
           
           if(type === "com.runwaysdk.geodashboard.gis.persist.UniversalAggregationStrategy"){
             for(var i=0; i<layerTypesJSON.length; i++){
               var lType = layerTypesJSON[i];
               
               $("." + lType).show();
             }
           }
           else if (type === "com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy"){
             for(var i=0; i<layerTypesJSON.length; i++){
               var lType = layerTypesJSON[i];
               
               $("." + lType).hide();
             }
             
             var geomTypes = getValueFromHTML(selectedOption.data("geomtypes"));
             
             for(var i=0; i<geomTypes.length; i++){
               var geomType = geomTypes[i];
               if(geomType === "geoPoint"){
                 $(".BUBBLE").show();
                 $(".BASICPOINT").show();
                 $(".GRADIENTPOINT").show();
                 $(".CATEGORYPOINT").show();
               }
               else if(geomType === "geoMultiPolygon"){
                 $(".BASICPOLYGON").show();
                 $(".CATEGORYPOLYGON").show();
                   $(".GRADIENTPOLYGON").show();
               }
               else{
                 //TODO: this needs to be an else if (geomType === "some line geom type") which isnt defined yet
               }
             }
           }
           
           $(geoTypeHolder).show();
         }
         
         
         function getValueFromHTML(html)
         {
           if(typeof html === 'string' || html instanceof String) {
             return JSON.parse(decodeURIComponent(html));                
           }
         
           // The html variable has already been converted into an object
           // Nothing else is required.
           return html;
         }
	}
	 
	angular.module("dashboard-layer-form", ["dashboard", "layer-form-service"]);
	angular.module("dashboard-layer-form")
		.controller('LayerFormController', ['$scope', '$compile', 'layerFormService', DashboardThematicLayerFormController])
		.directive('layerNameInput', layerNameInput)
		.directive('layerLabel', layerLabel)
		.directive('layerGeoNode', layerGeoNode)
		.directive('layerAggregation', layerAggregation)
		.filter('range', function() {
		  return function(input, min, max) {
		    min = parseInt(min); 
		    max = parseInt(max);
		    for (var i=min; i<max; i++)
		      input.push(i);
		    return input;
		  };
		})
		  
	
	
})();