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
	    	  
	          $("#label-text-color").colpick({
	              submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
	              onShow:function(colPickObj){
	            	  var that = this;
	                  $('#modal01').scroll(function(){  
	                    var colorPicker = $(".colpick.colpick_full.colpick_full_ns:visible");
	                    var colPick = $(that);
	                    var diff = colPick.offset().top + colPick.height() + 2; 
	                    var diffStr = diff.toString() + "px";
	                    colorPicker.css({ top: diffStr });
	                  });
	              },
	              onChange:function(hsb,hex,rgb,el,bySetColor) {
	            	$(el).find(".ico").css('background','#'+hex);
	            	scope.thematicStyleModel.labelColor = '#'+hex;
	              },
	              onHide:function(el) {
	            	 scope.$apply();
	              }
	           });
	          
	          $("#label-halo-color").colpick({
	              submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
	              onShow:function(colPickObj){
	            	  var that = this;
	                  $('#modal01').scroll(function(){  
	                    var colorPicker = $(".colpick.colpick_full.colpick_full_ns:visible");
	                    var colPick = $(that);
	                    var diff = colPick.offset().top + colPick.height() + 2; 
	                    var diffStr = diff.toString() + "px";
	                    colorPicker.css({ top: diffStr });
	                  });
	              },
	              onChange:function(hsb,hex,rgb,el,bySetColor) {
	            	$(el).find(".ico").css('background','#'+hex);
	            	scope.thematicStyleModel.labelHalo = '#'+hex;
	              },
	              onHide:function(el) {
	            	 scope.$apply();
	              }
	           });
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
	 
	 function layerAggregation(layerFormService) {
	    return {
	      restrict: 'E',
	      replace: true,
	      templateUrl: '/partial/dashboard/dashboard-layer-form-aggregation.jsp',    
	      scope: true,
	      link: function (scope, element, attrs) {
	    	  // format the partial
		      //jcf.customForms.replaceAll(element[0]);
	    	  
	    	  var geoNodeSelectId = "#geonode-select";
	    	  var geoAggLevelId = "#agg-level-dd";
	    	  var aggMedthodId = "#agg-method-dd";
	          
	    	  $(geoNodeSelectId).change(function(){ 
	    		  scope.getGeographicAggregationOptions(scope.thematicLayerModel.aggregationStrategy, scope.thematicLayerModel.geoNodeId);
	    	  });
	    	  
	    	  
	    	  $(geoAggLevelId).change(function(e){ 
	    		  // Hide aggregation dropdown when mapping against raw geometries (cant aggregate geometries)
	    		  var type = layerFormService.getAggregationStrategyType(scope, e.currentTarget.selectedOptions[0].value);
	              if(type === "com.runwaysdk.geodashboard.gis.persist.UniversalAggregationStrategy"){
	            	  $(aggMedthodId).parent().parent().show();
	              }
	              else if (type === "com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy"){
	            	  $(aggMedthodId).parent().parent().hide();
	              }
	    	  });
	      }
	    }    
	}
	
	 
	function layerTypes() {
	    return {
	      restrict: 'E',
	      replace: true,
	      templateUrl: '/partial/dashboard/dashboard-layer-form-layer-types.jsp',    
	      scope: true,
	      link: function (scope, element, attrs) {
	    	  // format the partial
		      jcf.customForms.replaceAll(element[0]);
	      }
	    }    
	}
	
	function layerTypesSelectionDirective() {
		  return function(scope, element, attrs) {
			  // This is needed to process the ng-repeat html
			  // It processes just the layer type selector widget
			  // Timeout is a bit of a hack to make sure all angular based html is finished
			  setTimeout(function(){ 
				  jcf.customForms.replaceAll(element[0]);
			  }, 100);
		  };
	}
	
	function layerTypesStyle() {
	    return {
	      restrict: 'E',
	      replace: true,
	      templateUrl: '/partial/dashboard/dashboard-layer-form-layer-types-styling.jsp',    
	      scope: true,
	      link: function (scope, element, attrs) {
	    	  // format the partial
//		      jcf.customForms.replaceAll(element[0]);
	    	  
	    	  //TODO: coppied from layer form but needs to be converted to angular
//	    	  $("#secondary-select-box").change(Mojo.Util.bind(this, this._handleSecondaryChange));
	      }
	    }    
	}
	
	
	var DashboardThematicLayerFormController = function($scope, $timeout, $compile, layerFormService) {
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
	    $scope.init = function(layerId, newInstance, geoNodeId, mdAttributeId, mapId) {
	    	// TODO: possibly remove mapId
	    	$scope.layerId = layerId;
	    	$scope.newInstance = Boolean(newInstance);
	    	$scope.thematicLayerModel.mdAttributeId = mdAttributeId;
	    	$scope.thematicLayerModel.geoNodeId = geoNodeId;
	    	
	    	if(newInstance){
	    		controller.loadLayerOptions($scope.thematicLayerModel.mdAttributeId);
	    	}
	    	else{
	    	  controller.loadLayerState();
	    	}
	    }
 
	      
	    /* Default State */ 
	    $scope.layerId = '';
	    $scope.newInstance = true; 
	    $scope.availableFonts = [];
	    
	    // Dymanic model properties may be changed based on runway based ajax requests 
	    // They are kept out of other models to keep those models replicating server models more closely
	    $scope.dynamicDataModel = {
	    	aggregationStrategyOptions : [],
	    	aggregationMethods : [], 
	    	layerTypeNams : [],
	    	layerTypeLabels : [],
	    	secondaryAttributes : [],
	    	isOntologyAttribute : false,
	    	isTextAttribute : false,
	    	roots : [],
	    	selectableMap : {},
	    	termType : '',
	    	relationshipType : ''
	    };

		$scope.thematicLayerModel = {
			viewName : '',
			sldName : '',
			layerName : '',
			layerId : '',
			inLegend : true,
			legendXPosition : 0,
			legendYPosition : 0,
			groupedInLegend : true,
			featureStrategy : '',
			mdAttributeId : '',
			attributeType : '',
			aggregationMethod : '', 
			aggregationAttribute : '',
			layerType : '',
			attributeLabel : '',
			geoNodeId : '',
			aggregationStrategy : '',
			styles : ''
		};
		
		$scope.thematicStyleModel = {
			basicPointSize : 10,
			enableLabel : true,
			enableValue : true,
			id : '',
			labelColor : '#000000;',
			labelFont : '', 
			labelHalo : '#ffffff',
			labelHaloWidth : 2,
			labelSize : 12,
			lineOpacity : '',
			lineStroke : '',
			lineStrokeCap : '',
			lineStrokeWidth : '',
			name : '',
			pointFill : '#00bfff',
			pointOpacity : 90,
			pointRotation : '',
			pointStroke : '#000000;',
			pointStrokeOpacity : 90,
			pointStrokeWidth : 1,
			pointWellKnownName : 'CIRCLE',
			polygonFill : '#00bfff',
			polygonFillOpacity : 90,
			polygonStroke : '#000000',
			polygonStrokeOpacity : 90,
			polygonStrokeWidth : 1,
			type : '',
			valueColor : '#000000',
			valueFont : '',
			valueHalo : '#ffffff',
			valueHaloWidth : 2,
			valueSize : '12',
			bubbleContinuousSize : true,
			bubbleFill : '#00bfff',
			bubbleMaxSize : 70,
			bubbleMinSize : 20,
			bubbleOpacity : 90,
			bubbleRotation : '',
			bubbleSize : 10,
			bubbleStroke : '#000000',
			bubbleStrokeOpacity : 90,
			bubbleStrokeWidth : 1,
			bubbleWellKnownName : 'CIRCLE',
			categoryPointFillOpacity : 90,
			categoryPointSize : 10,
			categoryPointStroke : '#000000',
			categoryPointStrokeOpacity : 90,
			categoryPointStrokeWidth : 1,
			categoryPointStyles : '',
			categoryPointWellKnownName : 'CIRCLE',
			categoryPolygonFillOpacity : 90,
			categoryPolygonStroke : '#000000',
			categoryPolygonStrokeOpacity : 90,
			categoryPolygonStrokeWidth : 1,
			categoryPolygonStyles : '',
			gradientPointFillOpacity : 90,
			gradientPointMaxFill : '#505050',
			gradientPointMinFill : '#ffffff',
			gradientPointSize : 10,
			gradientPointStroke : '#000000',
			gradientPointStrokeOpacity : 90,
			gradientPointStrokeWidth : 1,
			gradientPointWellKnownName : 'CIRCLE',
			gradientPolygonFillOpacity : 90,
			gradientPolygonMaxFill : '#505050',
			gradientPolygonMinFill : '#ffffff',
			gradientPolygonStroke : '#000000',
			gradientPolygonStrokeOpacity : 90,
			gradientPolygonStrokeWidth : 1,
			secondaryAggregationType : '',
			secondaryAttribute : '',
			secondaryCategories : '',
			styleCondition : ''
		};
		
	    
	    controller.loadLayerState = function() {
	        
	        var onSuccess = function(json){
	          var state = JSON.parse(json);
	            
//	          controller.setLayerState(state);
	        };
	          
	        layerFormService.getThematicLayerJSON($scope.layerId, onSuccess);
	    }
	    
	    controller.loadLayerOptions = function(attributeId) {
	        
	        var onSuccess = function(json){
	          var opts = JSON.parse(json);
	            
	          controller.setLayerOptions(opts);
	        };
	          
	        layerFormService.getThematicLayerOptionsJSON(attributeId, $scope.dashboard.getDashboardId(), onSuccess);
	    }
	    
	    controller.setLayerOptions = function(options) {
	    		$scope.dynamicDataModel.aggregationMethods = options.aggregations;
	    		$scope.dynamicDataModel.nodeAggregationStrategiesLookup = options.aggegationStrategies;
	    		$scope.dynamicDataModel.aggregationStrategyOptions = options.aggegationStrategies[0].aggregationStrategies;
	    		$scope.dynamicDataModel.layerTypeNames = options.layerTypeNames;
	    		$scope.dynamicDataModel.layerTypeLabels = options.layerTypeLabels;
	    		$scope.dynamicDataModel.pointTypes = options.pointTypes;
	    		$scope.dynamicDataModel.secondaryAttributes = options.secondaryAttributes;
	    		
	    		$scope.dynamicDataModel.isOntologyAttribute = options.attributeType.isOntologyAttribute;
	    		$scope.dynamicDataModel.isTextAttribute = options.attributeType.isTextAttribute;
	    		
	    		if(options.attributeType.isOntologyAttribute){
		    		$scope.dynamicDataModel.roots = options.attributeType.roots;
		    		$scope.dynamicDataModel.selectableMap = options.attributeType.selectableMap;
		    		$scope.dynamicDataModel.termType = options.attributeType.termType;
		    		$scope.dynamicDataModel.relationshipType = options.attributeType.relationshipType;
	    		}
	    	
	    		$scope.thematicLayerModel.layerType = options.layerTypeNames[0];
	    		$scope.thematicLayerModel.aggregationMethod = options.aggregations[0];
	    		$scope.thematicLayerModel.aggregationStrategy = options.aggegationStrategies[0].aggregationStrategies[0];
	    		$scope.thematicLayerModel.pointWellKnownName = options.pointTypes[0];
	    		
	    		$scope.availableFonts = processFonts(options.fonts);
	    		$scope.geoNodes = options.geoNodes;
	    		
	    		$scope.thematicStyleModel.labelFont = $scope.availableFonts[0];
	    		
	    		$scope.thematicLayerModel.geoNodeId = $scope.thematicLayerModel.geoNodeId || $scope.geoNodes[0]; // this is set in the init function but may be null if new layer
	    		$scope.setGeographicAggregationOptions($scope.dynamicDataModel.aggregationStrategyOptions, $scope.thematicLayerModel.aggregationStrategy)
	    		
	    		$scope.$apply();
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
	    
    	
    	$scope.secondaryAttributeIsValid = function(){
  	    	var selection = $scope.thematicStyleModel.secondaryAttribute;
  	    	if(selection && selection.id.length > 0 && selection.type.length > 0 && selection.label.length > 0 ){
  	    		return true;
  	    	}
  	    	return false;
  	    }
    	  
    	
	    // Aggregation strategy watcher
	    $scope.$watch("thematicLayerModel.aggregationStrategy", function(newValue, oldValue) {
	        //console.log("watching agg strategy", " : ", "new val = ", newValue, " old val = ", oldValue)
            if($(geoAggLevelSelectId+" option:selected").hasClass("universal-leaf")){
                // Hide the attribute aggregation dropdown because aggregations are irrelevant at this level of universal
                $(geoAggMethodSelectId).parent().parent().hide();
              }
              else{
                $(geoAggMethodSelectId).parent().parent().show();
              }
	    });
	    
	    $scope.$watch("thematicLayerModel.layerType", function(newValue, oldValue) {
	        console.log("watching agg method", " : ", "new val = ", newValue, " old val = ", oldValue)
	        if(newValue !== ""){
	        	$(".tab-pane").removeClass("active");
	        	
	        	if(newValue === "BASICPOINT"){
	        		$("#tab001basicpoint").addClass("active")
	        	}
	        	else if(newValue === "GRADIENTPOINT"){
	        		$("#tab006gradientpoint").addClass("active")
	        	}
	        	else if(newValue === "CATEGORYPOINT"){
	        		$("#tab007categoriespoint").addClass("active")
	        	}
	        	else if(newValue === "BUBBLE"){
	        		$("#tab002bubble").addClass("active")
	        	}
	        	else if(newValue === "BASICPOLYGON"){
	        		$("#tab006gradientpoint").addClass("active")
	        	}
	        	else if(newValue === "GRADIENTPOLYGON"){
	        		$("#tab004gradientpolygon").addClass("active")
	        	}
	        	else if(newValue === "CATEGORYPOLYGON"){
	        		$("#tab005categoriespolygon").addClass("active")
	        	}
	        }
	    });
	    
	    
	    $scope.$watch("thematicStyleModel.secondaryAttribute", function(newValue, oldValue) {
	        console.log("watching secondary attr", " : ", "new val = ", newValue, " old val = ", oldValue)
	        
//	       function _handleSecondaryChange(e){
//	            var option = e.target.selectedOptions[0];
//	            var mdAttributeId = option.value;
//	            var type = $(option).data("type"); // TODO: UPDATE from layerFormService getAggregationStrategyType
//	            
//	            if(mdAttributeId == '') {
//	              $('#secondary-content').hide();
//	            }
//	            else if(type == 'com.runwaysdk.system.metadata.MdAttributeTerm') {
//	              this._renderSecondaryTermTree(mdAttributeId, type);
//	            }
//	            else {
//	              this._renderSecondaryCategoryGroup(mdAttributeId, type);
//	            }
//	          }
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
            var selectedOption = null;
            
            if(aggregationStrategy){
                selectedOption = aggregationStrategy;
            }
            
            var nodeAggLookup = $scope.dynamicDataModel.nodeAggregationStrategiesLookup;
            for(var i=0; i<nodeAggLookup.length; i++){
            	var thisNode = nodeAggLookup[i];
            	if(thisNode.nodeId === geoNodeId){
            		$scope.setGeographicAggregationOptions(thisNode.aggregationStrategies, selectedOption);
            	}
            }
	    }
	    
        /**
         * Populate the geographic aggregation dropdown
         * 
         * @aggregations - JSON representing geo aggregation levels
         */
         $scope.setGeographicAggregationOptions = function(aggregations, selectedOption) {
//	             var selected = "";
//	             for(var i=0; i<aggregations.length; i++){
//	               var agg = aggregations[i];
//	               if(selectedOption.aggStrategyId === agg.aggStrategyId){
//	                 selected = "selected";
//	               }
//	               else if(i === 0){
//	                 selected = "selected";
//	               }
//	               else{
//	                 selected = "";
//	               }
//	               agg._isSelected = true
//	             }

	             // Re-sets the options property on the model
	             $scope.dynamicDataModel.aggregationStrategyOptions = aggregations;
	             
	             $scope.$apply();
	             
	             //jcf.customForms.replaceAll($(geoAggLevelSelectId).parent().get(0));
	             
//	             $(geoAggLevelHolder).show();
	             
	             _setLayerTypeOptions($scope.thematicLayerModel.aggregationStrategy);
        }
         
         /**
          * Populate the layer type block based on the selection of the geonode and geo aggregation level dropdown
          * 
          * @selectedOption - selected option from the geographic aggregation level dropdown
          */
         function _setLayerTypeOptions(selectedOption) {
           var type = selectedOption.aggStrategyType;
           // TODO: Move this data into the model
           var layerTypesJSON = $scope.dynamicDataModel.layerTypeNames;
           
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
             
             // TODO: replace data attribute with model property
             var geomTypes = selectedOption.aggStrategyGeomTypes;
             
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
		.controller('LayerFormController', ['$scope', '$timeout', '$compile', 'layerFormService', DashboardThematicLayerFormController])
		.directive('layerNameInput', layerNameInput)
		.directive('layerLabel', layerLabel)
		.directive('layerGeoNode', layerGeoNode)
		.directive('layerAggregation', layerAggregation)
		.directive('layerTypes', layerTypes)
		.directive('layerTypesSelectionDirective', layerTypesSelectionDirective)
		.directive('layerTypesStyle', layerTypesStyle)
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