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

  function ColorPicker() {
    return {
      restrict: 'A',
      scope: {
        model:'=',
      },      
      link: function (scope, element, attrs, ctrl) {
        // Hook up the color picker
        $(element).colpick({
          submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
          onShow:function(colPickObj){
            var that = this;
            
            // Set the current value of the color picker
            $(this).colpickSetColor(scope.model,false);
              
            $(attrs.element).scroll(function(){  
              var colorPicker = $(".colpick.colpick_full.colpick_full_ns:visible");
              var colPick = $(that);
              var diff = colPick.offset().top + colPick.height() + 2; 
              var diffStr = diff.toString() + "px";
                
              colorPicker.css({ top: diffStr });
            });
          },
          onChange:function(hsb,hex,rgb,el,bySetColor) {
            var value = '#'+hex;
            
            if(scope.model != value) {
              scope.model = value;
              scope.$apply();
            }
          }
        });
      }
    }    
  }
  
  function StyleCategoryList($timeout) {
    return {
      restrict: 'E',
      replace: true,
      templateUrl : '/partial/layer/style-category-list.jsp',    
      scope: {
        categories : '=',
        autoComplete : '&',
        showOther : '@'
      },
      link: function (scope, element, attrs) {
        if(scope.showOther == null) {
          scope.showOther = 'true';
        }
      }
    };    
  };
  
  function StyleCategoryOntologyController($scope) {
    var controller = this;
    
    /**
     * Scrape an ontology tree for the style settings
     */
    controller.getOntologyCategories = function (treeEl) {
        
      if($(treeEl).length > 0) {
        var categories = [];
              
        var elements = $(treeEl).find(".ontology-category-color-icon");
              
        $.each(elements, function( index, element ) {
          var category = {};
          category.id = element.dataset.rwid; 
          category.val = element.parentElement.previousSibling.textContent;
          category.color = controller.rgb2hex($(element).css("background-color"));
          category.isOntologyCat = true;
                            
          categories.push(category);
        });
                                  
        return categories;
      }
            
      return null;        
    };
  
    /**
     * Gets the hex color code for an ontology category based on the term id 
     * 
     * @param nodeId - id of the node in the ui to get the color for.
     */
    controller.getCategoryColor = function(termId) {
        
      if($scope.categories){          
        var categories = $scope.categories.catLiElems;

        if(categories != null) {
          if(categories.length > 0){
            for(var i=0; i < categories.length; i++){
              var category = categories[i];
              var catId = category.id;
             
              if(category.id === termId){
                return category.color;
              }
            }
          }          
        }
      }
      
      // if no match is found return the default
      return "#00bfff";
    };
    
    /**
     * Converts rgb or rgba to hex equivilent.
     * 
     * @param rgb or rgba 
     */
    controller.rgb2hex = function(rgb) {
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
  }

  
  function StyleCategoryOntology($timeout) {
    return {
      restrict: 'E',
      replace: true,
      templateUrl : '/partial/layer/style-category-ontology.jsp',    
      scope: {
      nodes : '&',
        categories : '=',
        showOther : '@'        
      },
      controller : StyleCategoryOntologyController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
        if(scope.showOther == null) {
          scope.showOther = 'true';
        }
        
        // Hook up the ontology-tree
        var treeElement = $(element).find('.ontology-tree').first()[0];        
        
        var nodes = scope.nodes();
        
        var rootTerms = [];
        
        for(var i = 0; i < nodes.length; i++) {
          var id = nodes[i].id;              
          rootTerms.push({termId : id});
        }
        
        var tree = new com.runwaysdk.geodashboard.ontology.OntologyTree({
          termType : "com.runwaysdk.geodashboard.ontology.Classifier",
          relationshipTypes : [ "com.runwaysdk.geodashboard.ontology.ClassifierIsARelationship" ],
          rootTerms : rootTerms,
          editable : false,
          slide : false,
          selectable : false,
          onCreateLi: function(node, $li) {
            if(!node.phantom) {
              var termId = node.runwayId;
                        
              // Load the value from the model
              var catColor = ctrl.getCategoryColor(termId);
                    
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
                    var currColor = ctrl.rgb2hex($(this).css("background-color"));
                    $(this).colpickSetColor(currColor,false);
                                  
                    // Move the color picker widget if the page is scrolled
                    $('#modal01').scroll(function(){  
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
                  },
                  onHide:function(el) {
                    scope.categories.catLiElems = ctrl.getOntologyCategories(treeElement);
                        
                    scope.$apply();
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
        }); // end tree
        
        tree.render(treeElement, nodes);
      }
    };    
  }; 
  
  function StyleController($scope) {
    var controller = this;
    
    controller.getFormattedInt = function(n){
      return Math.round(n*100)
    };
  }
     
  function StyleBasicFill($timeout) {
    return {
      restrict: 'E',
      replace: true,
      templateUrl : '/partial/layer/style-basic-fill.jsp',    
      scope: {
        fill:'=',
        opacity:'='
      },
      controller : StyleController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
      }
    };    
  };
  
  function StyleGradientFill($timeout) {
    return {
      restrict: 'E',
      replace: true,
      templateUrl : '/partial/layer/style-gradient-fill.jsp',    
      scope: {
        minFill:'=',
        maxFill:'=',
        opacity:'='
      },
      controller : StyleController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
        scope.clazz = {};
          
        if (attrs['class']) {
          scope.clazz = attrs['class'];        
        }        
      }
    };    
  };
  
  function StyleStroke($timeout) {
    return {
      restrict: 'E',
      replace: true,
      templateUrl : '/partial/layer/style-stroke.jsp',    
      scope: {
        stroke:'=',
        strokeWidth:'=',
        strokeOpacity:'='
      },
      controller : StyleController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
        if(attrs['class'] != null) {
          scope.divClass = attrs['class'];
        }
      }
    };    
  };
  
  
  
   function LayerNameInput($timeout) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: '/partial/layer/dashboard-layer-form-name.jsp',    
        scope: true,
        link: function (scope, element, attrs) {
          element.ready(function(){
            $timeout(function(){
              jcf.customForms.replaceAll(element[0]);
              $(element).show();
            }, 100);          
          });
        }
      };    
   };
  
  function LayerLabel($timeout) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl : '/partial/layer/dashboard-layer-form-label.jsp',    
        scope: true,
        link: function (scope, element, attrs, ctrl) {
          element.ready(function(){
            $timeout(function(){
              jcf.customForms.replaceAll(element[0]);
              $(element).show();
            }, 100);          
          });
        }
      };    
   };
   
   function LayerGeoNode($timeout) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: '/partial/layer/dashboard-layer-form-geonode.jsp',    
        scope: true,
        link: function (scope, element, attrs) {
          element.ready(function(){
            $timeout(function(){
              jcf.customForms.replaceAll(element[0]);
              $(element).show();
            }, 100);          
          });
        }
      };    
   };
   
   function LayerAggregationController($scope) {
     var controller = this;
     
     controller.showAggregationMethods = function() {
       var strategy = $scope.getCurrentAggregationStrategy();
         
       return (strategy != null && strategy.type === 'com.runwaysdk.geodashboard.gis.persist.UniversalAggregationStrategy');
     }
   }   
   
   function LayerAggregation($timeout) {
     return {
       restrict: 'E',
       replace: true,
       templateUrl: '/partial/layer/dashboard-layer-form-aggregation.jsp',    
       scope: true,
       controller : LayerAggregationController,
       controllerAs : 'ctrl',
       link: function (scope, element, attrs) {
         element.ready(function(){
           $timeout(function(){
             jcf.customForms.replaceAll(element[0]);
             $(element).show();
           }, 100);          
         });               
       }
     };    
   };
  
   
  function LayerTypes($timeout) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: '/partial/layer/dashboard-layer-form-layer-types.jsp',    
        scope: true,
        link: function (scope, element, attrs) {
          element.ready(function(){
            $timeout(function(){
              jcf.customForms.replaceAll(element[0]);
              $(element).show();
            }, 100);          
          });
        }
      };    
  };
  
  
  function LayerTypesSelectionDirective($timeout) {
    return function(scope, element, attrs) {
      // This is needed to process the ng-repeat html
      // It processes just the layer type selector widget
      // Timeout is a bit of a hack to make sure all angular based html is finished
      $timeout(function(){ 
         jcf.customForms.replaceAll(element[0]);
      }, 100);
    };
  };
  
  function BasicPoint($timeout) {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/layer/basic-point.jsp',    
      scope: true,
      link: function (scope, element, attrs) {
        element.ready(function(){
          $timeout(function(){
            jcf.customForms.replaceAll(element[0]);
//            $(element).show();
          }, 100);          
        });
      }
    };    
  };
  
  function BasicPolygon($timeout) {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/layer/basic-polygon.jsp',    
      scope: true,
      link: function (scope, element, attrs) {
        element.ready(function(){
          $timeout(function(){
            jcf.customForms.replaceAll(element[0]);
//            $(element).show();
          }, 100);          
        });
      }
    };    
  };
  
  function LayerTypesStyleController($scope, $timeout, $compile, layerFormService, localizationService) {
    var controller = this;  
    controller.ready = true;
    
    // TODO Change this to use the localizationService
    controller._formatter = Globalize.numberFormatter();
    controller._parser = Globalize.numberParser();
    
    
    /**
     * Setter for dynamic secondary aggregation methods which are updated on
     * selection of secondary attributes by the user.
     * 
     * @param type : thematic attribute type <string>
     */
    controller.setSecondaryAggregationMethods = function(type) {
      var options = $scope.categoryWidget.aggregationMap[type];
      
      $scope.dynamicDataModel.secondaryAggregationMethods = options;
      $scope.styleModel.secondaryAggregation.method = options[0];
    };
    
    controller.onAttributeChange = function() {
      controller.ready = false;
    	
      var attribute = $scope.styleModel.secondaryAggregation.attribute;
    	
      if(attribute.id != 'NONE') {
    	
        $('#secondary-aggregation-container').html("");
        
        // Reset the aggregation categories
        $scope.styleModel.secondaryAggregation.otherEnabled = false;
        $scope.styleModel.secondaryAggregation.other = {"val":"","color":"#737678","isOntologyCat":false,"otherEnabled":true,"otherCat":true};
        $scope.styleModel.secondaryAggregation.catLiElems = [
          {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
          {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
          {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
          {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
          {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false}          
        ];        
            
        controller.setSecondaryAggregationMethods(attribute.type);

        // Rebuild the secondary aggregations
        angular.element(document.getElementById('secondary-aggregation-container')).append($compile(
          '<select style="display:none" id="secondaryAggregation" class="method-select" name="secondaryAggregation"'+ 
            'ng-model="styleModel.secondaryAggregation.method"'+
            'ng-options="agg as agg.label for agg in dynamicDataModel.secondaryAggregationMethods track by agg.value">'+
          '</select>'
        )($scope));
                  
                  
        $timeout(function(){ 
          jcf.customForms.replaceAll($('#secondary-aggregation-container').get(0));
                    
          // ui hack to hide the select list while the UI process manipulates dom elements. 
          $("#secondaryAggregation").show();
          
          controller.ready = true;          
        }, 50);    	  
      }
    }
    
    /**
     * Test if the selected secondary attribute is valid (not null or empty string)
     */
    controller.secondaryAttributeIsValid = function(){
      var selection = $scope.styleModel.secondaryAggregation.attribute;
      
      if(selection && selection.id != "NONE" ){
        return true;
      }
      
      return false;
    };
    
    controller.isSecondaryAttributeOntology = function() {
      return ($scope.styleModel.secondaryAggregation.attribute.type == 'com.runwaysdk.system.metadata.MdAttributeTerm');
    }
    
    controller.categoryAutocomplete = function(mdAttribute, geoNodeId, universalId, aggregationVal, categoryType, request, response ) {
      
      var conditions = $scope.dashboard.getCompressedState(); 
      var limit = 10;
            
      var onSuccess = function(results){
        // We need to localize the results for numbers
        if(categoryType == 'number') {
          for(var i = 0; i < results.length; i++) {
            var number = parseFloat(results[i]);
            var localized = controller._formatter(number);
            results[i] = localized;
          }
        }
            
        response( results );
      };
          
      var onFailure = function(e){
        console.log(e);
      };
       
      var text = request.term;
      
      if(categoryType == 'number') {
        var parsed = controller._parser(text);
        
        if($.isNumeric(parsed)) {
          text = parsed;
        }
      }
        
      layerFormService.categoryAutoCompleteService(mdAttribute, geoNodeId, universalId, aggregationVal, text, limit, conditions, onSuccess, onFailure );
    };
    
    
    /**
     * Handle the ajax request/response for basic category auto complete.
     * 
     * @param request : JQuery signature var
     * @param response : JQuery signature var
     */
    controller.basicCategoryAutocompleteSource = function( request, response ) {      
      var mdAttribute = $scope.layerModel.mdAttribute;  
      var universalId = $scope.getCurrentAggregationStrategy().value;
      var geoNodeId = $scope.layerModel.geoNode;
      var aggregationVal = $scope.layerModel.aggregationType;
      var categoryType = $scope.dynamicDataModel.thematicAttributeDataType;
      
      controller.categoryAutocomplete(mdAttribute, geoNodeId, universalId, aggregationVal, categoryType, request, response );
    };    

    /**
     * Handle the ajax request/response for basic category auto complete.
     * 
     * @param request : JQuery signature var
     * @param response : JQuery signature var
     */
    controller.secondaryCategoryAutocompleteSource = function( request, response ) {      
      var mdAttribute = $scope.styleModel.secondaryAggregation.attribute.mdAttributeId;  
      var universalId = $scope.getCurrentAggregationStrategy().value;
      var geoNodeId = $scope.layerModel.geoNode;
      var aggregationVal = $scope.styleModel.secondaryAggregation.method.value;
      var categoryType = $scope.styleModel.secondaryAggregation.attribute.categoryType;
      
      controller.categoryAutocomplete(mdAttribute, geoNodeId, universalId, aggregationVal, categoryType, request, response );
    };     
  }       
  
  function LayerTypesStyle($timeout) {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/layer/dashboard-layer-form-layer-types-styling.jsp',    
      scope: true,
      controller : LayerTypesStyleController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs) {
        
        // Timeout is needed to ensure the tree elements exist on the dom to append the trees to
        $timeout(function(){
          // Format the select elements
          jcf.customForms.replaceAll(element[0]);
          
          $(element).show();
        }, 500);        
      }
    };    
  };
  
  
   function LegendOptions($timeout) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: '/partial/layer/dashboard-layer-form-legend-option.jsp',    
        scope: true,
        link: function (scope, element, attrs) {
          element.ready(function(){
            $timeout(function(){
              jcf.customForms.replaceAll(element[0]);
              $(element).show();
            }, 100);          
          });        	
        }
      };    
   };
   
   
   function FormActionButtons($timeout) {
        return {
          restrict: 'E',
          replace: true,
          templateUrl: '/partial/layer/dashboard-layer-form-action-buttons.jsp',    
          scope: true,
          link: function (scope, element, attrs) {
            element.ready(function(){
              $timeout(function(){
                jcf.customForms.replaceAll(element[0]);
                $(element).show();
              }, 100);          
            });        	  
          }
        };    
     };
     
  
   /**
    * Directive attached to all basic category input elements to hook the actual 
    * autocomplete action.
    */
   function CategoryAutoComplete($timeout) {
    return {
      restrict: "A",
      scope:{
        source : "&",
        ngModel : '='
      },
      require : 'ngModel',
      link: function (scope, element, attr, ngModel) {
        
        $timeout(function(){
          
              $(element).autocomplete({
                source: scope.source(),
                minLength: 1,
                select : function(event, ui) {
                  ngModel.$setViewValue(ui.item.value);
                }
              });
        }, 500); 
      }
    };
  };
  
  var DashboardThematicLayerFormController = function($scope, $timeout, $compile, layerFormService, localizationService) {
    var controller = this;
      
    /** 
     * Initialization Function 
     */
    $scope.init = function(layerId, newInstance, geoNodeId, mdAttributeId, mapId) {
      $scope.newInstance = (newInstance === 'true');
      $scope.dynamicDataModel.newInstance = (newInstance === 'true');
      $scope.layerModel.id = layerId;
      $scope.layerModel.mdAttribute = mdAttributeId;
      $scope.layerModel.geoNode = geoNodeId;
      $scope.mapId;
        
      if($scope.newInstance){
        controller.loadLayerOptions($scope.layerModel.mdAttribute);
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
    $scope.showModal = true; 
    $scope.errors = [];
      
      
    /**
     * Dymanic model properties may be changed throughout the user experience. 
     * They are kept out of other models to keep those models replicating server models more closely.
     */
    $scope.dynamicDataModel = {
      newInstance : true,
      aggregationStrategyOptions : [],
      aggregationStrategy : '', // using dynamicDataModel because the json representation is different from the actual model
      aggregationMethods : [], 
      layerTypeNames : [],
      layerTypeLabels : [],
      secondaryAttributes : [],
      secondaryAggregationMethods : [], 
      isOntologyAttribute : false,
      isTextAttribute : false,
      ontologyNodes : [],
      termType : '',
      relationshipType : '',
      thematicAttributeDataType : '',
      availableFonts : []      
    };

      
    /**
     * Model for a thematic layer
     */
    $scope.layerModel = {
      viewName : '',
//      sldName : '', // not needed on the client because geoserver uses the sld set on the server based on layer submission
      name : '',
      id : '',
      displayInLegend : true,
//      legendXPosition : 0,  // not on the dto
//      legendYPosition : 0,  // not on the dto
//      groupedInLegend : true,  // not on the dto
      mdAttribute : '',
      attributeType : '',  // not on the dto
      aggregationMethod : '', // not on the dto
      aggregationAttribute : '',  // not on the dto
      layerType : '',
      attributeLabel : '',  // not on the dto
      geoNode : '',
      aggregationStrategy : '', // should always be null since we are passing in new aggregation with applyWithStyle
      aggregationType : '',
//      styles : ''  // not on the dto
    };
    
    
    /**
     * Model for a thematic style of a thematic layer
     */
    $scope.styleModel = {
      basicPointSize : 10,
      enableLabel : true,
      enableValue : true,
      id : '',
      labelColor : '#000000',
      labelFont : '', 
      labelHalo : '#ffffff',
      labelHaloWidth : 2,
      labelSize : 12,
      lineOpacity : 0.9,
      lineStroke : '#000000',
      lineStrokeCap : '',
      lineStrokeWidth : 2,
      name : '',
      pointFill : '#00bfff',
      pointOpacity : 0.9,
      pointRotation : '',
      pointStroke : '#000000',
      pointStrokeOpacity : 0.9,
      pointStrokeWidth : 1,
      pointWellKnownName : 'CIRCLE',
      polygonFill : '#00bfff',
      polygonFillOpacity : 0.9,
      polygonStroke : '#000000',
      polygonStrokeOpacity : 0.9,
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
      bubbleOpacity : 0.9,
      bubbleRotation : '',
      bubbleSize : 10,
      bubbleStroke : '#000000',
      bubbleStrokeOpacity : 0.9,
      bubbleStrokeWidth : 1,
      bubbleWellKnownName : 'CIRCLE',
      categoryPointFillOpacity : 0.9,
      categoryPointSize : 10,
      categoryPointStroke : '#000000',
      categoryPointStrokeOpacity : 0.9,
      categoryPointStrokeWidth : 1,
      categoryPointStyles : '',
      categoryPointWellKnownName : 'CIRCLE',
      categoryPolygonFillOpacity : 0.9,
      categoryPolygonStroke : '#000000',
      categoryPolygonStrokeOpacity : 0.9,
      categoryPolygonStrokeWidth : 1,
      categoryPolygonStyles : '',
      gradientPointFillOpacity : 0.9,
      gradientPointMaxFill : '#505050',
      gradientPointMinFill : '#ffffff',
      gradientPointSize : 10,
      gradientPointStroke : '#000000',
      gradientPointStrokeOpacity : 0.9,
      gradientPointStrokeWidth : 1,
      gradientPointWellKnownName : 'CIRCLE',
      gradientPolygonFillOpacity : 0.9,
      gradientPolygonMaxFill : '#505050',
      gradientPolygonMinFill : '#ffffff',
      gradientPolygonStroke : '#000000',
      gradientPolygonStrokeOpacity : 0.9,
      gradientPolygonStrokeWidth : 1,
      secondaryAggregation : {
        attribute : {label : 'None', id : 'NONE'},
        method : {},
        otherEnabled : false,
        other : {"val":"","color":"#737678","isOntologyCat":false,"otherEnabled":false,"otherCat":true},
        catLiElems : [
                {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":false,"otherCat":false},
                {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":false,"otherCat":false},
                {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":false,"otherCat":false},
                {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":false,"otherCat":false},
                {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":false,"otherCat":false}]        
        
      },
      styleCondition : ''
    };
    
    
    /**
     * Model for category widgets 
     */
    $scope.categoryWidget = {
      basicPointCatOptionsObj : {
        otherEnabled : true,      
        other : {"val":"","color":"#737678","isOntologyCat":false,"otherEnabled":true,"otherCat":true},
        catLiElems : [
                {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
                {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
                {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
                {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
                {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false}
        ]
      },
      polygonCatOptionsObj : {
        otherEnabled : true,
        other : {"val":"","color":"#737678","isOntologyCat":false,"otherEnabled":true,"otherCat":true},
        catLiElems:[
                {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
                {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
                {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
                {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
                {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false}
        ]
      }
    };
    
    $scope.setEnableValue = function(val){
      $scope.styleModel.enableValue = val;
    }; 
      
    $scope.setEnableLabel = function(val){
      $scope.styleModel.enableLabel = val;
    }; 
      
    $scope.setInLegend = function(val){
      $scope.layerModel.inLegend = val;
    };
      
    $scope.setPointCategories = function(catObjects){
      $scope.styleModel.categoryPointStyles = {catLiElems:catObjects};
    };
      
    $scope.setPolygonCategories = function(catObjects){
      $scope.styleModel.categoryPolygonStyles = {catLiElems:catObjects};
    };
      
    $scope.setAggregationStrategyOptions = function(aggregations){
      $scope.dynamicDataModel.aggregationStrategyOptions = aggregations;
    };
        
    $scope.setValueFont = function(val){
      $scope.styleModel.valueFont = val;
    };
        
    $scope.getFormattedInt = function(n){
      return Math.round(n*100)
    };
        
        
    $scope.persist = function() {
      var onSuccess = function(response) {      
        $scope.closeLayerModal();
             
        var jsonObj = {};
        jsonObj["layers"] = [Mojo.Util.toObject(response)];
            
        $scope.dashboard.handleLayerEvent(jsonObj);
      };    
             
      var onFailure = function(e){
        $scope.errors = [];
        $scope.errors.push(e.message);
               
        $scope.$apply();
      };
             
             
      var categoryType = $scope.dynamicDataModel.thematicAttributeDataType;

      // Update the style model to include the point and polygon category values
      $scope.styleModel.categoryPointStyles = JSON.stringify(controller.getCategoryValues($scope.categoryWidget.basicPointCatOptionsObj, categoryType));
      $scope.styleModel.categoryPolygonStyles = JSON.stringify(controller.getCategoryValues($scope.categoryWidget.polygonCatOptionsObj, categoryType));
           
      // Update the secondary attribute values       
      if($scope.styleModel.secondaryAggregation.id != 'NONE'){    	  
        var secondaryCategoryType = $scope.styleModel.secondaryAggregation.attribute.categoryType;    	  
    	  
        $scope.styleModel.secondaryAttribute = $scope.styleModel.secondaryAggregation.attribute.mdAttributeId;                   
        $scope.styleModel.secondaryAggregationType = $scope.styleModel.secondaryAggregation.method.value;          
        $scope.styleModel.secondaryCategories = JSON.stringify(controller.getCategoryValues($scope.styleModel.secondaryAggregation, secondaryCategoryType).catLiElems);
      }
      else {
        $scope.styleModel.secondaryAttribute = '';                   
        $scope.styleModel.secondaryAggregationType = '';          
        $scope.styleModel.secondaryCategories = '[]';         
      }
           
      layerFormService.applyWithStyle($scope.layerModel, $scope.styleModel, $scope.dynamicDataModel, $scope.dashboard.getCompressedState(), '#modal01', onSuccess, onFailure);
    };
    
    
         
       /**
         * Action on cancel of layer form
         */
    $scope.cancel = function() {
      var onSuccess = function() {  
        $scope.closeLayerModal();
       }
               
      layerFormService.unlock($scope.layerModel.id, onSuccess);
     };
         
         
    /**
     * Close the form html
     */
     $scope.closeLayerModal = function() {
       $($scope.FORM_CONSTANTS.LAYER_MODAL).modal('hide').html('');
    };
         
    
    /**
     * Request for existing layer data
     * 
     */
    controller.loadLayerState = function() {
          
      var onSuccess = function(json){
        var state = JSON.parse(json);
        var opts = JSON.parse(state.optionsJSON);            
            
        controller.setLayerOptions(opts, false);  
        controller.setLayerState(state);
                      
        $scope.$apply();
      };
            
      layerFormService.getThematicLayerJSON($scope.layerModel.id, onSuccess);
    };
      
      
    /**
     * Request for new layer data 
     * 
     * @param attributeId : Id of the thematic attribute that the layer will be mapped against.
     */
    controller.loadLayerOptions = function(attributeId) {
      var onSuccess = function(json){
        var opts = JSON.parse(json);
              
        controller.setLayerOptions(opts, true);
            
        $scope.$apply();
      };
            
      layerFormService.getThematicLayerOptionsJSON(attributeId, $scope.dashboard.getDashboardId(), onSuccess);
    };
      
      
    /**
     * Set up the initial models for new layers
     * 
     * @param options : the JSON object from the server describing a new layer, style, and supporting data.
     */
    controller.setLayerOptions = function(options, isNew) {
        
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
        
      $scope.dynamicDataModel.availableFonts = $scope.processFonts(options.fonts);
      $scope.geoNodes = options.geoNodes;
        
      if(options.attributeType.isOntologyAttribute){
        $scope.dynamicDataModel.ontologyNodes = options.attributeType.nodes;
        $scope.dynamicDataModel.termType = options.attributeType.termType;
        $scope.dynamicDataModel.relationshipType = options.attributeType.relationshipType;
      }
        
      $scope.setGeographicAggregationOptions($scope.dynamicDataModel.aggregationStrategyOptions, $scope.dynamicDataModel.aggregationStrategy);
        
      $scope.categoryWidget.aggregationMap = JSON.parse(options.aggregationMap);
      
      // These properties should be set in setLayerState for existing layers (editing a layer)
      if(isNew){
        $scope.dynamicDataModel.aggregationStrategy = options.aggegationStrategies[0].aggregationStrategies[0].value;
          
        $scope.layerModel.layerType = options.layerTypeNames[0];
        $scope.layerModel.aggregationMethod = options.aggregations[0];
        $scope.layerModel.aggregationType = aggregationMethod = $scope.dynamicDataModel.aggregationMethods[0].method;
        $scope.layerModel.geoNode = $scope.layerModel.geoNode || $scope.geoNodes[0].id; // TODO: remove geoNode from init function... this is set in the init function but may be null if new layer
        $scope.styleModel.labelFont = $scope.dynamicDataModel.availableFonts[0]; 
        $scope.styleModel.valueFont = $scope.dynamicDataModel.availableFonts[0];  
        $scope.layerModel.displayInLegend = true;          
      }
    };
      
      
    /**
     * Set the model state for editing existing layers
     */
    controller.setLayerState = function(state) {
        
      $scope.dynamicDataModel.aggregationStrategy = state.aggregationStrategy.value;
        
      $scope.layerModel.name = state.layerName;
      $scope.layerModel.layerType = state.featureStrategy;
      $scope.layerModel.aggregationMethod = state.aggregationMethod;
      $scope.layerModel.aggregationType = aggregationMethod = state.aggregationMethod;
      $scope.layerModel.geoNode = state.geoNodeId;
      $scope.layerModel.displayInLegend = state.inLegend;

      var style = state.styles[0];
      $scope.styleModel = style;
                
      // Update the point and polygon category model
      controller.loadCategoryValues($scope.categoryWidget.polygonCatOptionsObj, style.categoryPolygonStyles, $scope.dynamicDataModel.thematicAttributeDataType);
      controller.loadCategoryValues($scope.categoryWidget.basicPointCatOptionsObj, style.categoryPointStyles, $scope.dynamicDataModel.thematicAttributeDataType);
      controller.loadSecondaryAggregation();
    };
      
    controller.loadSecondaryAggregation = function() {
      var aggregation = {
        attribute : {label : 'None', id : 'NONE'},
        method : {},
        otherEnabled : false,
        other : {"val":"","color":"#737678","isOntologyCat":false,"otherEnabled":true,"otherCat":true},
        catLiElems : [
          {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
          {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
          {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
          {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false},
          {"val":"","color":"#000000","isOntologyCat":false,"otherEnabled":true,"otherCat":false}          
        ]
      };
      
      var mdAttributeId = $scope.styleModel.secondaryAttribute;      
      
      if($scope.styleModel.secondaryAttribute != null && $scope.styleModel.secondaryAttribute != '') {
          
        // Load the attribute value
        for(var i = 0; i < $scope.dynamicDataModel.secondaryAttributes.length; i++) {
          var attribute = $scope.dynamicDataModel.secondaryAttributes[i];
            
          if(attribute.mdAttributeId == mdAttributeId) {
            aggregation.attribute = attribute;  
          }
        }

        // Load the method value
        var aggregationType = $scope.styleModel.secondaryAggregationType[0];      
        var options = $scope.categoryWidget.aggregationMap[aggregation.attribute.type];
        
        $scope.dynamicDataModel.secondaryAggregationMethods = options;
          
        for(var i = 0; i < $scope.dynamicDataModel.secondaryAggregationMethods.length; i++) {
          var method = $scope.dynamicDataModel.secondaryAggregationMethods[i];
            
          if(method.value == aggregationType) {
            aggregation.method = method;  
          }
        }
        
        var categoryType = aggregation.attribute.categoryType;
        
        // Load the categories
        controller.loadCategoryValues(aggregation, $scope.styleModel.secondaryCategories, categoryType);
      }
        
      $scope.styleModel.secondaryAggregation = aggregation;
    }
      
    controller.loadCategoryValues = function(model, json, categoryType) {
      var _formatter = Globalize.numberFormatter();
        
      if(json != null && json != '') {
        var categories = JSON.parse(json);
        
        if($.isArray(categories)) {
          categories = {catLiElems:categories};
        }
            
        for(var i = 0; i < categories.catLiElems.length; i++) {
          var category = categories.catLiElems[i];
              
          if(!category.otherCat) {
            if(categoryType == 'number') {
              category.val = _formatter(category.val);  
            }
                
            if(i < model.catLiElems.length ) {            	
              model.catLiElems[i] = category;                          	
            }
            else {
              model.catLiElems.push(category);
            }
          }
          else {
            model.other = category;
          }
              
          model.otherEnabled = category.otherEnabled;
        }          
      }
    }
    
    controller.getCategoryValues = function(categories, categoryType) {
      var _parser = Globalize.numberParser();
             
      var array = [];
                           
      for(var i = 0; i < categories.catLiElems.length; i++) {
        var category = categories.catLiElems[i];
               
        // Only send back categories which have values
        if(category.val != null && category.val.length > 0) {
          var object = {};
          angular.copy(category, object);
               
          object.otherEnabled = categories.otherEnabled;
                 
          if(categoryType == 'number') {
            object.val = _parser(object.val);  
          }
                 
          array.push(object);            
        }
      }
      
      if(categories.otherEnabled) {
        array.push(categories.other);
      }
             
      return {catLiElems:array};
    }
      
    /**
     * Fonts from Java come in as simple json array. We need to add id's to each entry for Angular.
     * 
     * @param fonts : Array of font names as strings
     */
      $scope.processFonts = function(fonts){
        var formattedFonts = [];
        for(var i=0; i<fonts.length; i++){
          formattedFonts.push(fonts[i].replace(/\+/g, " "));
        }
        return formattedFonts; 
      };      
        
            
      /**
       * Toggle the layer type selection UI based on user actions (model change)
       */
      $scope.$watch("layerModel.layerType", function(newValue, oldValue) {
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
      
      
      $scope.$watch("styleModel.labelFont", function(newValue, oldValue) {
        $scope.setValueFont(newValue);
      });
      
      /*
       * When the aggregation strategy is changed the list of possible
       * geoNodes needs to be updated.
       */
      $scope.$watch("layerModel.geoNode", function(newValue, oldValue) {
        if(newValue != null && newValue.length > 0) {        
          $scope.getGeographicAggregationOptions(newValue);
        }
      }); 
      
      $scope.$watch("dynamicDataModel.aggregationStrategy", function(newValue, oldValue) {
        if(newValue != null && newValue.length > 0) {
          $scope._setLayerTypeOptions(newValue);
        }
      });
      
      
      /**
       * Dropdown elements are non-standard UI elements (<div>s) that are not fixed to a position on the form. 
       * This helps keep the in place.
       * 
       * TODO: Call this method
       */
      $scope.attachDropdownScrollControls = function(){
            // Scroll selector dropdown options on page scroll
          $($scope.FORM_CONSTANTS.LAYER_MODAL).scroll(function(){         
            var drops = $(".select-options");
            
            for(var i=0; i<drops.length; i++){
              var drop = $(drops[i]);
              
              if(!drop.hasClass("options-hidden")){
                var dropSelector = $(".select-active");
                var diff = dropSelector.offset().top + dropSelector.height() + 2; 
                var diffStr = diff.toString() + "px";
                drop.css({ top: diffStr });
              }
            }
          });
      };

      
        /**
         * Sets up the geographic aggregation options (universals) based on a lookup.
         * 
         * @param layer : the layer object representing the layer the form is targeting 
         */
      $scope.getGeographicAggregationOptions = function(geoNodeId){
        $timeout(function(){              
              var geoNodes = $scope.dynamicDataModel.nodeAggregationStrategiesLookup;
              
              if(geoNodes != null) {
                  for(var i=0; i<geoNodes.length; i++){
                    var geoNode = geoNodes[i];
                    
                    if(geoNode.nodeId === geoNodeId){
                      var strategies = geoNode.aggregationStrategies;
                      
                      // Set options on the model
                      $scope.setGeographicAggregationOptions(strategies);
                    }
                  }              
              }
          
        }, 10);
      };
      
      
        /**
         * Populate the geographic aggregation dropdown
         * 
         * @aggregations - JSON representing geo aggregation levels
         * @selectedOption - selected option <object> from the geographic aggregation level dropdown
         */
         $scope.setGeographicAggregationOptions = function(aggregations) {
           $timeout(function(){
             // Re-sets the options property on the model           
             $scope.setAggregationStrategyOptions(aggregations);
             $scope.$apply();             
           }, 1);
         };
         
         
         $scope.getAggregationStrategy = function(value) {
             var options = $scope.dynamicDataModel.aggregationStrategyOptions;
             
             for(var i=0; i < options.length; i++){
               var strategy = options[i];
                 
               if(strategy.value === value){
                 return strategy;
               }
             }
               
             return null;           
         }
         
         $scope.getCurrentAggregationStrategy = function() {
           var value = $scope.dynamicDataModel.aggregationStrategy;
           
           return $scope.getAggregationStrategy(value);
         }
         
         /**
          * Populate the layer type block based on the selection of the geonode and geo aggregation level dropdown
          * 
          * @selectedOption - selected option <object> from the geographic aggregation level dropdown
          */
         $scope._setLayerTypeOptions = function(value) {
           
           var strategy = $scope.getCurrentAggregationStrategy();
           
           var type = strategy.type;
           
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
             
             var geomTypes = strategy.geomTypes;
             
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
  };
  
  
  /*
   * 
   * Reference Layer
   * 
   */
  function ReferenceLayerController($scope, $rootScope, referenceLayerFormService) {
    
      
    /*
     * Controller functions
     */
    var controller = this;
    
    controller.clear = function() {
      /*
       * Clear all model objects
       */     	
      $scope.show = false;          
      $scope.errors = [];
      $scope.state = null;

      // Objects loaded from the server
      $scope.layerModel = null;
      $scope.styleModel = null;
      $scope.dynamicDataModel = null;
    }
    
    controller.apply = function() {
      var onSuccess = function(layer) {    	  
        controller.clear();            
        $scope.$apply();
        
        if(layer != null) {
          var map = {};
          map.refLayers = [JSON.parse(layer)];
        	
          $scope.$emit('layerChange', {map:map});
        }
      }
      
      var onFailure = function(e){
        $scope.errors = [];
        $scope.errors.push(e.message);
                 
        $scope.$apply();
      };
      
      // Clear all the errors
      $scope.errors = [];
      
      referenceLayerFormService.apply($scope.layerModel, $scope.styleModel, $scope.state, '#reference-modal', onSuccess, onFailure);      
    }
    
    controller.cancel = function() {
      var onSuccess = function(response) {
        controller.clear();            
        $scope.$apply();
      }
      
      referenceLayerFormService.unlock($scope.layer, '#reference-modal', onSuccess);      
    }
    
    controller.load = function(response) {
      $scope.layerModel = response.layer;
      $scope.styleModel = response.style;
      $scope.dynamicDataModel = response.dynamicDataModel;
    }
    
    controller.newInstance = function(universalId, mapId) {
      var onSuccess = function(response) {
        controller.load(response);
            
        $scope.show = true;
        $scope.$apply();
      }
          
      referenceLayerFormService.newInstance(universalId, mapId, '#mapDivId', onSuccess);
    }
    
    controller.edit = function(layerId, mapId) {
      var onSuccess = function(response) {
        controller.load(response);
            
        $scope.show = true;
        $scope.$apply();
      }
      
      referenceLayerFormService.edit(layerId, '#mapDivId', onSuccess);
    }          
    
    /*
     * Listen for reference layer events
     */ 
    $rootScope.$on('editReferenceLayer', function(event, data) {
      var layerId = data.layerId;
      var mapId = data.mapId;
      var state = data.state;
      
      $scope.state = state;
      
      controller.edit(layerId, mapId);
    });    
    
    $rootScope.$on('newReferenceLayer', function(event, data) {
      var universalId = data.universalId;
      var mapId = data.mapId;
      var state = data.state;
      
      $scope.state = state;
      
      controller.newInstance(universalId, mapId);
    });
    
    /*
     * Model initialization
     */ 
    controller.clear();
  }
  
  function ReferenceLayer($timeout) {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/layer/reference-layer.jsp',    
      scope: {
    	  
      },
      controller : ReferenceLayerController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs) {
      }
    };    
   };  
  
  
   
  angular.module("dashboard-layer-form", ["layer-form-service", "dashboard", "styled-inputs"]);
  angular.module("dashboard-layer-form")
    .controller('LayerFormController', ['$scope', '$timeout', '$compile', 'layerFormService', DashboardThematicLayerFormController])
    .directive('colorPicker', ColorPicker)
    .directive('styleCategoryList', StyleCategoryList)
    .directive('styleCategoryOntology', StyleCategoryOntology)
    .directive('styleBasicFill', StyleBasicFill)
    .directive('styleGradientFill', StyleGradientFill)
    .directive('styleStroke', StyleStroke)
    .directive('layerNameInput', LayerNameInput)
    .directive('layerLabel', LayerLabel)
    .directive('layerGeoNode', LayerGeoNode)
    .directive('layerAggregation', LayerAggregation)
    .directive('layerTypes', LayerTypes)
    .directive('layerTypesSelectionDirective', LayerTypesSelectionDirective)
    .directive('basicPoint', BasicPoint)
    .directive('basicPolygon', BasicPolygon)
    .directive('layerTypesStyle', LayerTypesStyle)
    .directive('categoryAutoComplete', CategoryAutoComplete)
    .directive('legendOptions', LegendOptions)
    .directive('formActionButtons', FormActionButtons)
    .directive('referenceLayer', ReferenceLayer);
})();