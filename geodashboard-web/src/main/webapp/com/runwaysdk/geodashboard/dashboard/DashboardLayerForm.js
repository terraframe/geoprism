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
        scope: {
          layerModel : '=',
          disabled : '&'
        },
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
            
            scope.$emit('ready', {});            
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
      var options = $scope.dynamicDataModel.aggregationMap[type];
      
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
        
      layerFormService.categoryAutoCompleteService(mdAttribute, geoNodeId, universalId, aggregationVal, text, limit, $scope.state, onSuccess, onFailure );
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
       scope: {
         persist : '&',
         cancel : '&'
       },
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
  
  /*
   * 
   * 
   * THEMATIC LAYER
   * 
   * 
   */  
  function ThematicLayerController($scope, $rootScope, $timeout, layerFormService) {
    /**
     * Constants for UI elements 
     */
    $scope.FORM_CONSTANTS = {
      LAYER_MODAL : "#modal01",
      GEO_TYPE_HOLDER : "#geom-type-holder",
    };
    
    var controller = this;
    
    controller.clear = function() {
      /**
       * Layer agnostic properties
       */	
      $scope.show = false;          
      $scope.errors = [];
      $scope.state = null;

      // Objects loaded from the server
      $scope.layerModel = null;
      $scope.styleModel = null;
      $scope.categoryWidget = null;
      $scope.dynamicDataModel = null;
    }
    
    controller.load = function(response) {
      $scope.dynamicDataModel = response.dynamicDataModel;    	
      $scope.layerModel = response.layer;
      $scope.styleModel = response.style;
      $scope.categoryWidget = response.categoryWidget;
    }    
      
    $scope.setPointCategories = function(catObjects){
      $scope.styleModel.categoryPointStyles = {catLiElems:catObjects};
    };
      
    $scope.setPolygonCategories = function(catObjects){
      $scope.styleModel.categoryPolygonStyles = {catLiElems:catObjects};
    };
      
    $scope.setAggregationStrategyOptions = function(aggregations){
      $scope.dynamicDataModel.aggregationStrategyOptions = aggregations;
    };
                
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
           return $scope.dynamicDataModel.aggregationStrategyMap[value];           
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

    controller.persist = function() {
      var onSuccess = function(layer) {
        controller.clear();    
        $scope.$apply();

        if(layer != null) {
          var map = {};
          map["layers"] = [JSON.parse(layer)];
                  
          $scope.$emit('layerChange', {map:map});    
        }
      };    
               
      var onFailure = function(e){
        $scope.errors = [];
        $scope.errors.push(e.message);
                 
        $scope.$apply();
      };             
               
      // Clear all the errors
      $scope.errors = [];
             
      layerFormService.apply($scope.layerModel, $scope.styleModel, $scope.dynamicDataModel, $scope.categoryWidget, $scope.state, '#modal01', onSuccess, onFailure);
    };
           
    /**
     * Action on cancel of layer form
     */
    controller.cancel = function() {
      var onSuccess = function() {  
        controller.clear();            
        $scope.$apply();
      }
                 
      layerFormService.unlock($scope.layerModel, '#thematic-modal', onSuccess);
    };
    
         
    controller.newInstance = function(mdAttributeId, mapId) {
      var onSuccess = function(response) {
        controller.load(response);
                  
        $scope.show = true;
        $scope.$apply();
      }
                 
      layerFormService.newInstance(mdAttributeId, mapId, '#mapDivId', onSuccess);
      
    }
           
    controller.edit = function(layerId) {
      var onSuccess = function(response) {
        controller.load(response);
                   
        $scope.show = true;
        $scope.$apply();
      }
             
      layerFormService.edit(layerId, '#mapDivId', onSuccess);
    } 
    
    
    
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
    
    /*
     * Listen for layer events
     */ 
    $rootScope.$on('editThematicLayer', function(event, data) {
      var layerId = data.layerId;
      var state = data.state;
      
      $scope.state = state;
      
      controller.edit(layerId);
    });    
    
    $rootScope.$on('newThematicLayer', function(event, data) {
      var mdAttributeId = data.mdAttributeId;
      var mapId = data.mapId;
      var state = data.state;
      
      $scope.state = state;
      
      controller.newInstance(mdAttributeId, mapId);
    }); 
        
    /*
     * Model initialization
     */ 
    controller.clear();    
  }

  
  function ThematicLayer() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/layer/thematic-layer.jsp',    
      scope: {
      
      },
      controller : ThematicLayerController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs) {
      }
    };    
  }; 
  
  
  /*
   * 
   * 
   * REFERENCE LAYER
   * 
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
      $scope.show = false;  // Flag if the modal should be processed
      $scope.ready = false;
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
    
    controller.edit = function(layerId) {
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
      var state = data.state;
      
      $scope.state = state;
      
      controller.edit(layerId);
    });    
    
    $rootScope.$on('newReferenceLayer', function(event, data) {
      var universalId = data.universalId;
      var mapId = data.mapId;
      var state = data.state;
      
      $scope.state = state;
      
      controller.newInstance(universalId, mapId);
    });
    
    
    $scope.$on('ready', function(event, data) {
      $scope.ready = true;
      
      event.stopPropagation();
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
    .directive('thematicLayer', ThematicLayer)
    .directive('referenceLayer', ReferenceLayer);
})();