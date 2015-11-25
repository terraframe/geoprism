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
	
	 function LayerNameInput() {
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
	    };    
	 };
	 
	 
	function LayerLabel() {
	    return {
	      restrict: 'E',
	      replace: true,
	      templateUrl: '/partial/dashboard/dashboard-layer-form-label.jsp',    
	      scope: true,
	      link: function (scope, element, attrs) {
	    	  // format the partial
//	    	  jcf.customForms.replaceAll(element[0]);
	    	  
	    	  // set style of font labels in the dropdown to the font they represent
	    	  setTimeout(function(){ 
	    		  scope._injectFontStylesForDropdown();
	    	  }, 100);
	    
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
	    };    
	 };
	
	
	 function LayerGeoNode() {
	    return {
	      restrict: 'E',
	      replace: true,
	      templateUrl: '/partial/dashboard/dashboard-layer-form-geonode.jsp',    
	      scope: true,
	      link: function (scope, element, attrs) {
	    	  // format the partial
//		    	  jcf.customForms.replaceAll(element[0]);
	      }
	    };    
	 };
	 
	 
	 function LayerAggregation(layerFormService) {
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
	    		  var type = scope.getAggregationStrategyType(e.currentTarget.selectedOptions[0].value);
	              if(type === "com.runwaysdk.geodashboard.gis.persist.UniversalAggregationStrategy"){
	            	  $(aggMedthodId).parent().parent().show();
	              }
	              else if (type === "com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy"){
	            	  $(aggMedthodId).parent().parent().hide();
	              }
	    	  });
	      }
	    }    
	};
	
	 
	function LayerTypes() {
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
	};
	
	
	function LayerTypesSelectionDirective() {
		  return function(scope, element, attrs) {
			  // This is needed to process the ng-repeat html
			  // It processes just the layer type selector widget
			  // Timeout is a bit of a hack to make sure all angular based html is finished
			  setTimeout(function(){ 
				  jcf.customForms.replaceAll(element[0]);
			  }, 100);
		  };
	};
	
	
	function LayerTypesStyle() {
	    return {
	      restrict: 'E',
	      replace: true,
	      templateUrl: '/partial/dashboard/dashboard-layer-form-layer-types-styling.jsp',    
	      scope: true,
	      link: function (scope, element, attrs) {
	    	  // format the partial
//		      jcf.customForms.replaceAll(element[0]);
		      
		      // Timeout is needed to ensure the tree elements exist on the dom to append the trees to
		      setTimeout(function(){
		    	  if(scope.dynamicDataModel.isOntologyAttribute){
			    	  var layerTypes = scope.dynamicDataModel.layerTypeNames;
			    	  for(var i=0; i<layerTypes.length; i++){
			    		  var type = layerTypes[i];
			    		  var targetEl = null;
			    		  // Set ontology category tree on all category elements
			    		  if(type === 'CATEGORYPOINT' || type === 'CATEGORYPOLYGON'){
			    			  
				    		  if(type === 'CATEGORYPOINT'){
						          targetEl = scope.FORM_CONSTANTS.POINT_ONTOLOGY_TREE_ID;
				    		  }
				    		  else if(type === 'CATEGORYPOLYGON'){
				    			  targetEl = scope.FORM_CONSTANTS.POLYGON_ONTOLOGY_TREE_ID;
				    		  }
				    		  
				    		  var tree = new com.runwaysdk.geodashboard.ontology.OntologyTree({
					              termType : "com.runwaysdk.geodashboard.ontology.Classifier",
					              relationshipTypes : [ "com.runwaysdk.geodashboard.ontology.ClassifierIsARelationship" ],
					              rootTerms : scope.dynamicDataModel.roots.roots,
					              editable : false,
					              slide : false,
					              selectable : false,
					              onCreateLi: function(node, $li) {
					                if(!node.phantom) {
					                	var termId = node.runwayId;
					                  	var catColor = scope._getCategoryColor(termId);
					              
					                  	var thisLi = $.parseHTML(
					                    '<a href="#" class="color-choice" style="float:right; width:20px; height:20px; padding: 0px; margin-right:15px; border:none;">' +
					                      '<span data-rwId="'+ termId +'" class="ico ontology-category-color-icon" style="background:'+catColor+'; border:1px solid #ccc; width:20px; height:20px; float:right; cursor:pointer;">icon</span>' +
					                    '</a>');
		
					                  	// Add the color icon for category ontology nodes              
					                  	$li.find('> div').append(thisLi);
					                  
						                // ontology category layer type colors
						                $(thisLi).find("span").colpick({
						                  submit: 0,  // removes the "ok" button which allows verification of selection and memory for last color
						                  onShow:function(colPickObj) {
						                  var that = this;
						                    var currColor = scope.rgb2hex($(this).css("background-color"));
						                    $(this).colpickSetColor(currColor,false);
						                    
						                    // Move the color picker widget if the page is scrolled
						                    $(scope.FORM_CONSTANTS.LAYER_MODAL).scroll(function(){  
						                      var colorPicker = $(".colpick.colpick_full.colpick_full_ns:visible");
						                      var colPick = $(that);
						                      var diff = colPick.offset().top + colPick.height() + 2; 
						                      var diffStr = diff.toString() + "px";
						                      colorPicker.css({ top: diffStr });
						                    });
						                    
						                  },
						                  onChange: function(hsb,hex,rgb,el,bySetColor) {
						                    var hexStr = '#'+hex;
						                    $(el).css('background', hexStr);
						                    $(el).next(".color-input").attr('value', hexStr);    
						                    
						                    // TODO: Determine when to update the model for updated tree colors
						                    //scope.updateAllOntologyCategryModels()
						                  },
						                  /* checkable: true, */
						                  crud: {
						                    create: { // This configuration gets merged into the jquery create dialog.
						                      height: 320
						                    },
						                    update: {
						                      height: 320
						                    }
						                  }
						                });
					                }
					              }
				    		  });
				    		  
				    		  tree.render(targetEl, null);
			    		  }
			    	  	}
		    	  
			          	function attachOtherCategoryColorPicker(){
			              // ontology category layer type colors
			              $(".ontology-other-color-icon").colpick({
			                submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
			                onShow:function(colPickObj){
			                  var that = this;
			                    $(scope.FORM_CONSTANTS.LAYER_MODAL).scroll(function(){  
			                      var colorPicker = $(".colpick.colpick_full.colpick_full_ns:visible");
			                      var colPick = $(that);
			                      var diff = colPick.offset().top + colPick.height() + 2; 
			                      var diffStr = diff.toString() + "px";
			                      colorPicker.css({ top: diffStr });
			                    });
			                },
			                onChange:function(hsb,hex,rgb,el,bySetColor) {
			                  $(el).css('background','#'+hex);
			                  $(el).find('.color-input').attr('value', '#'+hex);
			                }
			              });
			            }
			          	
			          	attachOtherCategoryColorPicker();
		    	  }
		    	  else{
		    		  scope._setupCategoryColorPicker($(element[0]).find(".color-holder"));
		    	  }
		          	
		      }, 500);
	      }
	    }    
	};
	
	
	function CategoryAutoComplete(layerFormService) {
		return {
			restrict: "A",
			link: function (scope, element, attr, ctrl) {
				
				setTimeout(function(){
				    
			    	  $(element[0]).autocomplete({
			    		  source: scope.basicCategoryAutocompleteSource,
			    		  minLength: 1
			    	  });
				}, 500); 
			}
		};
	};
	
	
	var DashboardThematicLayerFormController = function($scope, $timeout, $compile, layerFormService) {
		var controller = this; 
   	  	
		 /**
		  * Getting the $compile method reference for use with later functions  
		  */
	    controller.$compile = $compile;
	    controller.$scope = $scope;
	    
		/** 
		 * Initialization Function 
		 */
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
	    };
	    
	      
	    /**
	     * Constants for UI elements 
	     */
	    $scope.FORM_CONSTANTS = {
	    	POLYGON_CATEGORY_STORE : "#categories-polygon-input",
	    	POINT_CATEGORY_STORE : "#categories-point-input",
	    	LAYER_MODAL : "#modal01",
	        POINT_ONTOLOGY_TREE_ID : "#points-ontology-tree",
	        POLYGON_ONTOLOGY_TREE_ID : "#polygon-ontology-tree",
	        GEONODE_SELECT_ID : "#geonode-select",
	        GEO_TYPE_HOLDER : "#geom-type-holder",
	        GEO_AGG_STRATEGY_SELECT_ID : "#agg-level-dd",
	        GEO_AGG_STRATEGY_HOLDER_ID : "#agg-level-holder",
	        GEO_AGG_METHOD_SELECT_ID : "#agg-method-dd"
	    };
	    
	    
	    /**
	     * Layer agnostic properties
	     */
	    $scope.newInstance = true; 
	    $scope.availableFonts = [];
	    
	    
	    /**
	     * Dymanic model properties may be changed throughout the user experience. 
	     * They are kept out of other models to keep those models replicating server models more closely.
	     */
	    $scope.dynamicDataModel = {
	    	aggregationStrategyOptions : [],
	    	aggregationMethods : [], 
	    	layerTypeNames : [],
	    	layerTypeLabels : [],
	    	secondaryAttributes : [],
	    	secondaryAggregationMethods : [], 
	    	isOntologyAttribute : false,
	    	isTextAttribute : false,
	    	roots : [],
	    	selectableMap : {},
	    	termType : '',
	    	relationshipType : '',
	    	thematicAttributeDataType : '', 
	    	polyCategoriesStore : {catLiElems:[]},
	    	pointCategoriesStore : {catLiElems:[]}
	    };

	    
	    /**
	     * Model for a thematic layer
	     */
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
		
		
		/**
		 * Model for a thematic style of a thematic layer
		 */
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
			secondaryCategories : [], // TODO: hook this up
			styleCondition : ''
		};
		
		
		/**
		 * Model for category widgets 
		 */
		$scope.categoryWidget = {
			widgetType : '',
			ontPointOtherOptionSelected : true,
			pointCatOtherOptionSelected : true,
			pointCatOtherOptionColor : '#737678',
			aggregationMap : {},
			basicPointCatOptionsObj : {
				"catLiElems":[
				              	{"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
				              	{"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
				              	{"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
				              	{"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
				              	{"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
				              	{"val":"other","color":"#737678","isOntologyCat":false,"otherEnabled":true,"otherCat":true}
				              ]
			},
			ontPolygonOtherOptionSelected : true,
			polygonCatOtherOptionSelected : true,
			polygonCatOtherOptionColor : '#737678',
			polygonCatOptionsObj : {
				"catLiElems":[
				              	{"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
				              	{"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
				              	{"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
				              	{"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
				              	{"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
				              	{"val":"other","color":"#737678","isOntologyCat":false,"otherEnabled":true,"otherCat":true}
				              ]
			},
			secondaryCatOptionsObj : {
				"catLiElems":[
				              	{"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
				              	{"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
				              	{"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
				              	{"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
				              	{"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
				              	{"val":"other","color":"#737678","isOntologyCat":false,"otherEnabled":true,"otherCat":true}
				              ]
			}
		};
	    
		
	    /**
	     * Request for existing layer data 
	     * 
	     */
	    controller.loadLayerState = function() {
	        
	        var onSuccess = function(json){
	          var state = JSON.parse(json);
	            
//	          controller.setLayerState(state);
	        };
	          
	        layerFormService.getThematicLayerJSON($scope.layerId, onSuccess);
	    };
	    
	    
	    /**
	     * Request for new layer data 
	     * 
	     * @param attributeId : Id of the thematic attribute that the layer will be mapped against.
	     */
	    controller.loadLayerOptions = function(attributeId) {
	        
	        var onSuccess = function(json){
	          var opts = JSON.parse(json);
	            
	          controller.setLayerOptions(opts);
	        };
	          
	        layerFormService.getThematicLayerOptionsJSON(attributeId, $scope.dashboard.getDashboardId(), onSuccess);
	    };
	    
	    
	    /**
	     * Set up the initial models for new layers
	     * 
	     * @param options : the JSON object from the server describing a new layer, style, and supporting data.
	     */
	    controller.setLayerOptions = function(options) {
	    		$scope.dynamicDataModel.aggregationMethods = options.aggregations;
	    		$scope.dynamicDataModel.nodeAggregationStrategiesLookup = options.aggegationStrategies;
	    		$scope.dynamicDataModel.aggregationStrategyOptions = options.aggegationStrategies[0].aggregationStrategies;
	    		$scope.dynamicDataModel.layerTypeNames = options.layerTypeNames;
	    		$scope.dynamicDataModel.layerTypeLabels = options.layerTypeLabels;
	    		$scope.dynamicDataModel.pointTypes = options.pointTypes;
	    		$scope.dynamicDataModel.secondaryAttributes = options.secondaryAttributes;
	    		
	    		$scope.dynamicDataModel.thematicAttributeDataType = options.attributeDataType;
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
	    		$scope.thematicLayerModel.secondaryAttributes = options.secondaryAttributes[0];  //TODO: review if this actaully should be set
	    		
	    		$scope.availableFonts = $scope.processFonts(options.fonts);
	    		$scope.geoNodes = options.geoNodes;
	    		
	    		$scope.thematicStyleModel.labelFont = $scope.availableFonts[0];
	    		
	    		$scope.thematicLayerModel.geoNodeId = $scope.thematicLayerModel.geoNodeId || $scope.geoNodes[0]; // this is set in the init function but may be null if new layer
	    		$scope.setGeographicAggregationOptions($scope.dynamicDataModel.aggregationStrategyOptions, $scope.thematicLayerModel.aggregationStrategy)
	    		
	    		$scope.categoryWidget.aggregationMap = JSON.parse(options.aggregationMap); // TODO: this might need the aggregation value/id for persisting to the database
	    		$scope.$apply();
	    };
	    
	    
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
	    };
	    
	    
		/**
		 * Fonts from Java come in as simple json array. We need to add id's to each entry for Angular.
		 * It's important to note the obvious, that these id's are completely generic.
		 * 
		 * @param fonts : Array of font names as strings
		 */
    	$scope.processFonts = function(fonts){
    		var formattedFonts = [];
    		for(var i=0; i<fonts.length; i++){
    			formattedFonts.push({ "id" : i, "value" : fonts[i].replace(/\+/g, " ") });
    		}
    		return formattedFonts; 
    	};
    	
    	
    	 /**
         * Adding font-family style property to dropdown options for better usability.
         * Adding this method was necessary because the ux js code does not account for style properties in dropdowns
         * 
         */
    	$scope._injectFontStylesForDropdown = function(){
          var convertedOptions = $(".select-options.drop-font-select").find("ul").children();
          var selectedOption = $(".select-font-select.select-area").find(".center");
          selectedOption.css("font-family", selectedOption.text());
          
          for(var i=0; i<convertedOptions.length; i++){
            var targetSpan = $(convertedOptions[i]).find("span");
            
            if(targetSpan.text().length > 0){
              targetSpan.css("font-family", targetSpan.text());
            }
          }
        };
	    
        
        /**
         * Get the aggregation type from the model based on an aggregation Value
         * 
         * @param aggStratVal : aggregation strategy value (not id)
         */ 
        $scope.getAggregationStrategyType = function(aggStratVal) {
        	var aggStratOpts = $scope.dynamicDataModel.aggregationStrategyOptions;
        	for(var i=0; i<aggStratOpts.length; i++){
        		var aggStrat = aggStratOpts[i];
        		if(aggStrat.aggStrategyValue === aggStratVal){
        			return aggStrat.aggStrategyType;
        		}
        	}
        	return
        };
    	
        
        /**
         * Test if the selected secondary attribute is valid (not null or empty string)
         */
    	$scope.secondaryAttributeIsValid = function(){
  	    	var selection = $scope.thematicStyleModel.secondaryAttribute;
  	    	if(selection && selection.id.length > 0 && selection.type.length > 0 && selection.label.length > 0 ){
  	    		return true;
  	    	}
  	    	return false;
  	    };
  	    
    	
    	/**
         * Gets the hex color code for an ontology category based on the term id 
         * 
         * @param nodeId - id of the node in the ui to get the color for.
         */
        $scope._getCategoryColor = function(termId) {
          var catsJSONObj = $scope.dynamicDataModel.pointCategoriesStore;
            
          if(catsJSONObj){          
            var catsJSONArr = catsJSONObj.catLiElems;
            
            if(catsJSONArr == null && Array.isArray(catsJSONObj)) {
              catsJSONArr = catsJSONObj;
            }          

            if(catsJSONArr != null) {            
              if(catsJSONArr.length > 0){
                for(var i=0; i<catsJSONArr.length; i++){
                  var cat = catsJSONArr[i];
                  var catId = cat.id;
                 
                  if(catId === termId){
                    return cat.color;
                  }
                  else if(catId === "cat-other-color-selector"){
                    return cat.color;
                  }
                  else if(catId === "cat-point-other-color-selector"){
                    return cat.color;
                  }
                }
              }          
            }
          }
          // if no match is found return the default
          return "#00bfff";
        };
        
        
        // TODO: Test and setup
        $scope._setupCategoryColorPicker = function(elements) {
            
            // color dropdown buttons
            elements.colpick({
              submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
              onShow:function(colPickObj) {
              var that = this;
              var icoEl;
              var currColor;
              
              if($(this).find(".ico")){
                currColor = $scope.rgb2hex($(this).find(".ico").css("background-color"));
              }
              else if($(this).hasClass("ico")){
                currColor = $scope.rgb2hex($(this).css("background-color"));
              }
              
                $(this).colpickSetColor(currColor,false);
                
                $($scope.FORM_CONSTANTS.LAYER_MODAL).scroll(function(){  
                  var colorPicker = $(".colpick.colpick_full.colpick_full_ns:visible");
                  var colPick = $(that);
                  var diff = colPick.offset().top + colPick.height() + 2; 
                  var diffStr = diff.toString() + "px";
                  colorPicker.css({ top: diffStr });
                });
                
              },
              onChange:function(hsb,hex,rgb,el,bySetColor) {
                $(el).find(".ico").css('background','#'+hex);
                $(el).find('.color-input').attr('value', '#'+hex);
              }
            }); 
        };
        
    	
        /**
         * Scrape an ontology tree for the style settings
         */
    	$scope.getOntologyCategories = function (treeEl) {
            
            if($(treeEl).length > 0) {
              var categories = [];
                  
              var elements = $(treeEl).find(".ontology-category-color-icon");
                  
              $.each(elements, function( index, element ) {
                var category = new Object();
                category.id = element.dataset.rwid; 
                category.val = element.parentElement.previousSibling.textContent;
                category.color = $scope.rgb2hex($(element).css("background-color"));
                category.isOntologyCat = true;
                                
                categories.push(category);
              });
                  
              // Get the other category for the a layer create/edit form term tree
              var thisId = $(treeEl).next().attr('id');
              if(thisId === "other-cat-point-container"){
                var otherCat = new Object();
                otherCat.id = "cat-point-other-color-selector"; 
                otherCat.val = "Other";
                otherCat.color = $scope.rgb2hex($("#cat-point-other-color-selector").css("background-color"));
                otherCat.isOntologyCat = false;
                otherCat.otherCat = true;
                otherCat.otherEnabled =  $("#ont-cat-point-other-option").prop("checked");
                categories.push(otherCat);
              }
              else if(thisId === "other-cat-poly-container"){
                var otherCat = new Object();
                  otherCat.id = "cat-other-color-selector"; 
                  otherCat.val = "Other";
                  otherCat.color = $scope.rgb2hex($("#cat-other-color-selector").css("background-color"));
                  otherCat.isOntologyCat = false;
                  otherCat.otherCat = true;
                  otherCat.otherEnabled =  $("#ont-cat-poly-other-option").prop("checked");
                  categories.push(otherCat);
              }
                        
              return categories;
            }
                
            return null;        
        };
    	
    	
    	/**
         * Scrape basic categories for values and style settings
         */
    	$scope.getBasicCategories = function () {    
          var elements = this.getImpl().find(".category-container");
          var categories = [];        
              
          for(var i=0; i< elements.length; i++){
            var catInputElem = $(elements[i]).find(".category-input");
            var catColorElem = $(elements[i]).find(".cat-color-selector");
            var catColor = LayerForm.rgb2hex($(catColorElem).css("background-color"));
            var catVal = catInputElem.val();
                  
            // parse the formatted number to the format of the data so the SLD can apply categories by this value
            if(catInputElem.data("type") == "number" ) {
              var thisNum = this._parser(catVal);
                
              if($.isNumeric(thisNum)) {
                catVal = thisNum;                
              }
            }
                  
            // Filter out categories with no input values
            if(catVal !== ""){
              var category = new Object();
              category.val = catVal;
              category.color = catColor;
              category.isOntologyCat = false;
                  
              if(this._checkOther) {
                if(this.getGeomType() === "polygon"){
                  category.otherEnabled = $("#basic-cat-poly-other-option").prop("checked");
                }
                else if(this.getGeomType() === "point"){
                  category.otherEnabled = $("#basic-cat-point-other-option").prop("checked");
                }
                
                if(catInputElem[0].id === this.getOtherCatInputId()){
                  category.otherCat = true;
                }
                else{
                  category.otherCat = false;
                }
              }
              else {
                category.otherEnabled = false;
                category.otherCat = false;
              }
                       
              categories.push(category);
            }
          }  
                        
          return categories;
        };
    	
    	
    	/**
         * Add existing categories to the ui
         * TODO: hook this up and test it. 
         */
    	$scope._loadExistingBasicCategories = function() {
          var catsJSONObj = $(this._storeId).data("categoriesstore");  // TODO: get this data from the model
            
          if(catsJSONObj){
            catsJSONObj = CategoryWidget.getValueFromHTML(catsJSONObj);
            var catsJSONArr = catsJSONObj.catLiElems;
              
            if(catsJSONArr == null && Array.isArray(catsJSONObj)) {
              catsJSONArr = catsJSONObj;
            }          
                        
            if(catsJSONArr != null) {
              
              var catInputId;
              var catOtherEnabled = true;
            
              for(var i=0; i<catsJSONArr.length; i++){
                var cat = catsJSONArr[i];
                  
                // Controlling for 'other' category 
                if(cat.otherCat){
                  catInputId = this.getOtherCatInputId();
                }
                else{
                  catInputId = this._prefix + "-" + (i+1);
                }
                  
                var catColorSelectorId = catInputId + "-color-selector";
              
                var value = cat.val;            
                var categoryType = $("#"+catInputId).data("type");
              
                // Localize any existing number category values
                if(!cat.otherCat && categoryType == "number") {
                  var number = parseFloat(value);
                  var localized = this._formatter(number);
                  
                  value = localized;
                }            
              
                $("#"+catInputId).val(value);
                $("#"+catColorSelectorId).css("background", cat.color);
                catOtherEnabled = cat.otherEnabled;
              }
                
              // Simulate a checkbox click to turn off the checkbox if the 'other' option is disabled
              // The 'other' option is checked by default making this a valid sequence
              if(this._checkOther && !catOtherEnabled){
              
              if(this.getGeomType() === "polygon"){
                  $("#basic-cat-poly-other-option").click();
                }
                else if(this.getGeomType() === "point"){
                  $("#basic-cat-point-other-option").click();
                }
                $("#" + catInputId).parent().parent().hide();
              }
            }
          }
        };
        
    	
    	$scope.basicCategoryAutocompleteSource = function( request, response ) {
    	   var _parser = Globalize.numberParser();
    	   var _formatter = Globalize.numberFormatter();
    			
		   var mdAttribute = $scope.thematicLayerModel.mdAttributeId;  
		   var categoryType = $scope.dynamicDataModel.thematicAttributeDataType;
	       var universalId = $scope.thematicLayerModel.aggregationStrategy.aggStrategyValue;
	       var geoNodeId = $scope.thematicLayerModel.geoNodeId.id;
	       var aggregationVal = $scope.thematicLayerModel.aggregationMethod.method;
	       var conditions = $scope.dashboard.getCompressedState(); 
	       var limit = 10;
		          
	       var onSuccess = function(results){
                // We need to localize the results for numbers
                if(categoryType == 'number') {
                  for(var i = 0; i < results.length; i++) {
                    var number = parseFloat(results[i]);
                    var localized = _formatter(number);
                    results[i] = localized;
                  }
                }
                
                response( results );
           };
              
           var onFailure = function(e){
        	   GDB.ExceptionHandler.handleException(e.message);
           };
           
           var text = request.term;
 			  
           layerFormService.categoryAutoCompleteService(mdAttribute, geoNodeId, universalId, aggregationVal, text, limit, conditions, onSuccess, onFailure );
    	};
    	
    	
	    // Aggregation strategy watcher
	    $scope.$watch("thematicLayerModel.aggregationStrategy", function(newValue, oldValue) {
	        //console.log("watching agg strategy", " : ", "new val = ", newValue, " old val = ", oldValue)
            if($($scope.FORM_CONSTANTS.GEO_AGG_STRATEGY_SELECT_ID+" option:selected").hasClass("universal-leaf")){
                // Hide the attribute aggregation dropdown because aggregations are irrelevant at this level of universal
                $($scope.FORM_CONSTANTS.GEO_AGG_METHOD_SELECT_ID).parent().parent().hide();
              }
              else{
                $($scope.FORM_CONSTANTS.GEO_AGG_METHOD_SELECT_ID).parent().parent().show();
              }
	    });
	    
	    
	    /**
	     * Toggle the layer type selection UI based on user actions (model change)
	     */
	    $scope.$watch("thematicLayerModel.layerType", function(newValue, oldValue) {
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
	        		$("#tab003basicpolygon").addClass("active")
	        	}
	        	else if(newValue === "GRADIENTPOLYGON"){
	        		$("#tab004gradientpolygon").addClass("active")
	        	}
	        	else if(newValue === "CATEGORYPOLYGON"){
	        		$("#tab005categoriespolygon").addClass("active")
	        	}
	        }
	    });
	    
	    
	    $scope._renderSecondaryCategoryGroup = function(mdAttributeId, type) {
	        $scope.setSecondaryAggregationMethods(type);
	        
//	        jcf.customForms.replaceAll($('#secondary-content').get(0));
	    };
	    
	    
	    /**
	     * Render the ontology tree for secondary categories
	     */
	    $scope._renderSecondaryTermTree = function(mdAttributeId, type) {
	        
	        $scope.setSecondaryAggregationMethods(type);
	        
	        // Get the term roots and setup the tree widget
	        var req = new Mojo.ClientRequest({
	          onSuccess : function(results){
	            var nodes = JSON.parse(results);
	            var rootTerms = [];
	                
	            $("#secondary-tree").html("");
	            
	            for(var i = 0; i < nodes.length; i++) {
	              var id = nodes[i].id;              
	              rootTerms.push({termId : id});
	            }
	            
	            var _secondaryWidget = new com.runwaysdk.geodashboard.ontology.OntologyTree({
		              termType : "com.runwaysdk.geodashboard.ontology.Classifier",
		              relationshipTypes : [ "com.runwaysdk.geodashboard.ontology.ClassifierIsARelationship" ],
		              rootTerms : rootTerms,
		              editable : false,
		              slide : false,
		              selectable : false,
		              onCreateLi: function(node, $li) {
		                if(!node.phantom) {
		                	var termId = node.runwayId;
		                  	var catColor = $scope._getCategoryColor(termId);
		              
		                  	var thisLi = $.parseHTML(
		                    '<a href="#" class="color-choice" style="float:right; width:20px; height:20px; padding: 0px; margin-right:15px; border:none;">' +
		                      '<span data-rwId="'+ termId +'" class="ico ontology-category-color-icon" style="background:'+catColor+'; border:1px solid #ccc; width:20px; height:20px; float:right; cursor:pointer;">icon</span>' +
		                    '</a>');

		                  	// Add the color icon for category ontology nodes              
		                  	$li.find('> div').append(thisLi);
		                  
			                // ontology category layer type colors
			                $(thisLi).find("span").colpick({
			                  submit: 0,  // removes the "ok" button which allows verification of selection and memory for last color
			                  onShow:function(colPickObj) {
			                	var that = this;
			                    var currColor = $scope.rgb2hex($(this).css("background-color"));
			                    $(this).colpickSetColor(currColor,false);
			                    
			                    // Move the color picker widget if the page is scrolled
			                    $($scope.FORM_CONSTANTS.LAYER_MODAL).scroll(function(){  
			                      var colorPicker = $(".colpick.colpick_full.colpick_full_ns:visible");
			                      var colPick = $(that);
			                      var diff = colPick.offset().top + colPick.height() + 2; 
			                      var diffStr = diff.toString() + "px";
			                      colorPicker.css({ top: diffStr });
			                    });
			                    
			                  },
			                  onChange: function(hsb,hex,rgb,el,bySetColor) {
			                    var hexStr = '#'+hex;
			                    $(el).css('background', hexStr);
			                    $(el).next(".color-input").attr('value', hexStr);    
			                    
			                    // TODO: Determine when to update the model for updated tree colors
			                    //scope.updateAllOntologyCategryModels()
			                  },
			                  /* checkable: true, */
			                  crud: {
			                    create: { // This configuration gets merged into the jquery create dialog.
			                      height: 320
			                    },
			                    update: {
			                      height: 320
			                    }
			                  }
			                });
		                }
		              }
	    		  });
	    		  
	            _secondaryWidget.render("#secondary-tree", nodes);
	          },
	          onFailure : function(e){
	        	  GDB.ExceptionHandler.handleException(e.message);
	          }
	        });
	        
//	        jcf.customForms.replaceAll($('#secondary-content').get(0));
	            
	        com.runwaysdk.geodashboard.Dashboard.getClassifierTree(req, mdAttributeId);            
	    };
	    
	    
	    /**
	     * Setter for dynamic secondary aggregation methods which are updated on
	     * selection of secondary attributes by the user.
	     */
	    $scope.setSecondaryAggregationMethods = function(type) {
	    	var options = $scope.categoryWidget.aggregationMap[type];
	    	$scope.dynamicDataModel.secondaryAggregationMethods = options;
	    	//jcf.customForms.replaceAll($('#secondary-aggregation-container').get(0));
	    };
	    
	    
	    /**
	     * Render secondary category widgets based on user selection of a secondary attribute.
	     */
	    $scope.$watch("thematicStyleModel.secondaryAttribute", function(newValue, oldValue) {
	        if(newValue){
	        	var mdAttributeId = newValue.mdAttributeId;
	        	if(newValue.type){
	        		var type = newValue.type;
		            if(type == 'com.runwaysdk.system.metadata.MdAttributeTerm') {
		            	$scope._renderSecondaryTermTree(mdAttributeId, type);
		            }
		            else {
		            	$scope._renderSecondaryCategoryGroup(mdAttributeId, type);
		            }
	        	}
	        }
	    });
	    
	    
	    $scope.setEnableValue = function(val){
	        $scope.thematicStyleModel.enableValue = val;
	    }; 
	    
	    $scope.setEnableLabel = function(val){
	        $scope.thematicStyleModel.enableLabel = val;
	    }; 
	    
	    $scope.setPointCategoriesStore = function(catObjects){
	    	$scope.dynamicDataModel.pointCategoriesStore = {catLiElems:catObjects};
	    };
	    
	    $scope.setPolygonCategoriesStore = function(catObjects){
	    	$scope.dynamicDataModel.polyCategoriesStore = {catLiElems:catObjects};
	    };
	    
	    
	    /**
	     * Scrapes the category point and category polygon ontology trees for style settings.
	     */
	    $scope.updateAllOntologyCategryModels = function(){
	    	var layerTypes = $scope.dynamicDataModel.layerTypeNames;
	    	for(var i=0; i<layerTypes.length; i++){
	    		var type = layerTypes[i];
	    		if(type === 'CATEGORYPOINT'){
	    			var scrapedCategoryEls = $scope.getOntologyCategories($scope.FORM_CONSTANTS.POINT_ONTOLOGY_TREE_ID);
	    			$scope.setPointCategoriesStore(scrapedCategoryEls);
	    		}
	    		else if(type === 'CATEGORYPOLYGON'){
	    			var scrapedCategoryEls = $scope.getOntologyCategories($scope.FORM_CONSTANTS.POLYGON_ONTOLOGY_TREE_ID);
	    			$scope.setPolygonCategoriesStore(scrapedCategoryEls);
	    		}
	    	}
	    };
	    
	    
	    /**
	     * Converts rgb or rgba to hex equivilent.
	     * 
	     * @param rgb or rgba 
	     */
	    $scope.rgb2hex = function(rgb) {
	        if(rgb != null) {
	            
	            if (/^#[0-9A-F]{6}$/i.test(rgb)){
	              return rgb;
	            }

	            var rgbMatch = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
	            if(rgbMatch){
	              function hex(x) {
	                return ("0" + parseInt(x).toString(16)).slice(-2);
	              }
	              return "#" + hex(rgbMatch[1]) + hex(rgbMatch[2]) + hex(rgbMatch[3]);
	            }
	              
	            var rgbaMatch = rgb.match(/^rgba?[\s+]?\([\s+]?(\d+)[\s+]?,[\s+]?(\d+)[\s+]?,[\s+]?(\d+)[\s+]?/i);
	            if(rgbaMatch){
	              return (rgbaMatch && rgbaMatch.length === 4) ? "#" +
	                 ("0" + parseInt(rgbaMatch[1],10).toString(16)).slice(-2) +
	                 ("0" + parseInt(rgbaMatch[2],10).toString(16)).slice(-2) +
	                 ("0" + parseInt(rgbaMatch[3],10).toString(16)).slice(-2) : '';
	            }
	         }
	    };
	    
	    
  	  	/**
         * Sets up the geographic aggregation options (universals) based on a lookup.
         * 
         * @param layer : the layer object representing the layer the form is targeting 
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
            		// Set options on the model
            		$scope.setGeographicAggregationOptions(thisNode.aggregationStrategies, selectedOption);
            	}
            }
	    };
	    
	    
        /**
         * Populate the geographic aggregation dropdown
         * 
         * @aggregations - JSON representing geo aggregation levels
         */
         $scope.setGeographicAggregationOptions = function(aggregations, selectedOption) {
             // Re-sets the options property on the model
             $scope.dynamicDataModel.aggregationStrategyOptions = aggregations;
             
             $scope.$apply();
             
             //jcf.customForms.replaceAll($(geoAggLevelSelectId).parent().get(0));
             
             $scope._setLayerTypeOptions($scope.thematicLayerModel.aggregationStrategy);
         };
         
         
         /**
          * Populate the layer type block based on the selection of the geonode and geo aggregation level dropdown
          * 
          * @selectedOption - selected option from the geographic aggregation level dropdown
          */
         $scope._setLayerTypeOptions = function(selectedOption) {
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
           
           $($scope.FORM_CONSTANTS.GEO_TYPE_HOLDER).show();
         };
         
         
         /**
          * Utility function for converting encoded json back to json (de-coding)
          * 
          * @param html : encoded json
          */
         function getValueFromHTML(html)
         {
           if(typeof html === 'string' || html instanceof String) {
             return JSON.parse(decodeURIComponent(html));                
           }
         
           // The html variable has already been converted into an object
           // Nothing else is required.
           return html;
         }
	};
	
	 
	angular.module("dashboard-layer-form", ["dashboard", "layer-form-service"]);
	angular.module("dashboard-layer-form")
		.controller('LayerFormController', ['$scope', '$timeout', '$compile', 'layerFormService', DashboardThematicLayerFormController])
		.directive('layerNameInput', LayerNameInput)
		.directive('layerLabel', LayerLabel)
		.directive('layerGeoNode', LayerGeoNode)
		.directive('layerAggregation', LayerAggregation)
		.directive('layerTypes', LayerTypes)
		.directive('layerTypesSelectionDirective', LayerTypesSelectionDirective)
		.directive('layerTypesStyle', LayerTypesStyle)
		.directive('categoryAutoComplete', CategoryAutoComplete)
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