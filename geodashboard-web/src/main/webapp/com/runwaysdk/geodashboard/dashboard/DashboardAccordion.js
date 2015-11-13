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
  var parser = Globalize.numberParser();
  var formatter = Globalize.numberFormatter();  
  
  function LocationFilterController($scope, dashboardService) {
    var controller = this;
    
    controller.source = function( request, response ) {
      var onSuccess = function(query){
        var resultSet = query.getResultSet()
                                    
        var results = [];
                  
        $.each(resultSet, function( index, result ) {
          var label = result.getValue('displayLabel');
          var id = result.getValue('id');
                    
          results.push({'label':label, 'value':label, 'id':id});
        });
                  
        response( results );
      };
                      
      dashboardService.getGeoEntitySuggestions($scope.dashboardId, request.term, 10, onSuccess);
    }
  }
  
  function LocationFilter() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/location-filter.jsp',      
      scope: {
        filter:'=',
        dashboardId:'='
      },
      controller : LocationFilterController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
        var input = $(element).find(".filter-geo");
        
        input.autocomplete({
          source: ctrl.source,
          select: function(event, ui) {
            scope.filter.value = ui.item.id;
            scope.filter.label = ui.item.label;
          }, 
          change : function(event, ui) {
            var value = input.val();
              
            if(value == null || value == '') {                
              scope.filter.value= '';
            }
          },
          minLength: 2
        });
        
        scope.$watch('filter', function(){
          if(scope.filter.value != null && scope.filter.value != '') {
            input.val(scope.filter.label);          
          }
        }, true);
      }
    }    
  }  
  
  function TypeAccordion() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/dashboard-accordion.jsp',      
      scope: {
        types:'=',
        newLayer:'&'
      },
      link: function (scope, element, attrs) {
      }
    }    
  }
  
  function AccordionAttributeController($scope, $timeout, dashboardService) {
    var controller = this;
    
    controller.canEdit = function() {
      return dashboardService.canEdit();
    }
    
    controller.newLayer = function(mdAttributeId) {
      dashboardService.getDashboard().newLayer(mdAttributeId);
    }
    
    controller.expand = function(element) {
      /*
       * There is a timing issue between when the angular finishes process
       * the ng-href and ng-attr attributes.  Thus we need to delay the
       * acutal opening of the attribute block.
       */  
      $timeout(function(){
        $(element).find(".opener-link").click();
      }, 1000);
    }
  }
  
  function AccordionAttribute() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/accordion-attribute.jsp', 
      transclude: true,
      scope: {
        attribute:'=',
        identifier:'@'
      },
      controller : AccordionAttributeController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
      
        // Don't collapse the element if there are filtering values            
        element.ready(function(){        
          if(!$.isEmptyObject(scope.attribute.filter)) {
            ctrl.expand(element);
          }      
        });
      }
    }    
  }
  
  function AttributeTypeController($scope, $timeout) {
    var controller = this;

    controller.init = function(element) {
      $timeout(function(){
        jcf.customForms.replaceAll(element[0]);      
      }, 500, false);
    }  
  }
  
  function NumberType() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/number-type.jsp',      
      require: ['^form', 'numberType'],
      controller : AttributeTypeController,
      controllerAs : 'ctrl',
      scope: {
        attribute:'=',
        whole:'=',
      },
      link: function (scope, element, attrs, ctrls) {
        scope.form = ctrls[0];

        ctrls[1].init(element);
      }
    }    
  }
  
  function DateType($parse) {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/date-type.jsp',      
      require: ['^form'],
      scope: {
        attribute:'='
      },
      link: function (scope, element, attrs, ctrl) {
        scope.form = ctrl;
        scope.attribute.filter.type = "DATE_CONDITION";
        
        /* Hook up the jquery datepicker*/
        var checkin = $(element).find('.checkin');
        var checkout = $(element).find('.checkout');
        
        $(element).find('.datapicker-opener').on('click', function(e){
          e.preventDefault();
          
          $(this).prev().datepicker('show');
        });

        var startDate = false;
        var endDate = false;
        
        checkin.datepicker($.datepicker.regional.local);
        checkin.datepicker("option", {
          showOtherMonths: true,
          selectOtherMonths: true,
          onSelect: function(dateText, inst){
            startDate = new Date(dateText);
            
            scope.attribute.filter.startDate = dateText;
            scope.$apply();
          },
          onClose: function( selectedDate ) {
            checkout.datepicker( "option", "minDate", selectedDate );
          },
          beforeShowDay: function(date){
            return [true, startDate && ((date.getTime() == startDate.getTime()) || (endDate && date >= startDate && date <= endDate)) ? "dp-highlight" : ""];
          }
        });
        
        checkout.datepicker($.datepicker.regional.local);
        checkout.datepicker("option", {
          showOtherMonths: true,
          selectOtherMonths: true,
          onSelect: function(dateText, inst){
            endDate = new Date(dateText);
            
            scope.attribute.filter.endDate = dateText;
            scope.$apply();
          },
          onClose: function( selectedDate ) {
            checkin.datepicker( "option", "maxDate", selectedDate );
          },
          beforeShowDay: function(date){
            return [true, startDate && ((date.getTime() == startDate.getTime()) || (endDate && date >= startDate && date <= endDate)) ? "dp-highlight" : ""];
          }
        });            
      }
    }    
  }

  function CharacterType() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/character-type.jsp',      
      require: ['^form', 'characterType'],      
      scope: {
        attribute:'='
      },
      controller : AttributeTypeController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrls) {
        scope.form = ctrls[0];
        
        ctrls[1].init(element);
      }
    }    
  }
  
  function OntologyTypeController($scope, dashboardService) {
    var controller = this;
    
    controller.renderTree = function(element) {
      
      var onSuccess = function(results){
        var nodes = JSON.parse(results);
        var rootTerms = [];
              
        for(var i = 0; i < nodes.length; i++) {
          rootTerms.push({termId : nodes[i].id});
        }
              
        var tree = new com.runwaysdk.geodashboard.ontology.OntologyTree({
          termType : "com.runwaysdk.geodashboard.ontology.Classifier" ,
          relationshipTypes : [ "com.runwaysdk.geodashboard.ontology.ClassifierIsARelationship" ],
          rootTerms : rootTerms,
          editable : false,
          slide : false,
          selectable : false,
          checkable : true
        });
        tree.onCheck(function(node){
          $scope.attribute.filter.value = tree.getCheckedTerms();
          $scope.$apply();
        });
            
        tree.render(element, nodes);
            
        // Load saved values
        if($scope.attribute.filter.value != null)
        {
          tree.setCheckedTerms($scope.attribute.filter.value);  
        }
      }
          
      var mdAttributeId = $scope.attribute.mdAttributeId;

      dashboardService.getClassifierTree(mdAttributeId, onSuccess);      
    }    
  }
  
  function OntologyType() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/ontology-type.jsp',
      controller : OntologyTypeController,
      controllerAs : 'ctrl',
      scope: {
        attribute:'='
      },
      require: ['^form', 'ontologyType'],      
      link: function (scope, element, attrs, ctrls) {
        scope.form = ctrls[0];
        scope.attribute.filter.type = "CLASSIFIER_CONDITION";
        
        element.ready(function(){
          ctrls[1].renderTree(element[0]);
        });
      }
    }    
  }
  
  function BooleanType() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/boolean-type.jsp',
      require: '^form',
      scope: {
        attribute:'='
      },
      link: function (scope, element, attrs, ctrl) {
        scope.form = ctrl;
        
        element.ready(function(){
          jcf.customForms.replaceAll(element[0]);
        });        
      }
    }    
  }
  
  function IntegerOnly() {
    return {
      restrict: 'A',
      require: 'ngModel',
      link: function (scope, element, attrs, ngModel) {
        ngModel.$parsers.push(function(value) {
          if(value != null) {            
            //convert data from view format to model format
            var number = parser( value );
            return number;
          }
          
          return value;
        });

        ngModel.$formatters.push(function(value) {
          if(value != null) {
            var number = value;
            
            if(typeof number === 'string') {
              number = parseInt(value);
            }
            
            //convert data from model format to view format
            return formatter(number);
          }
            
          return value;
        });
        
        ngModel.$validators.integer = function(modelValue, viewValue) {
          if (ngModel.$isEmpty(viewValue)) {
            // consider empty models to be valid
            return true;
          }
            
          var number = parser( viewValue );
          var valid = ($.isNumeric(number) && Math.floor(number) == number);
          
          return valid;        
        }
      }
    }    
  }
  
  function NumberOnly() {
    return {
      restrict: 'A',
      require: 'ngModel',
      link: function (scope, element, attrs, ngModel) {
      
        ngModel.$parsers.push(function(value) {
          if(value != null) {          
            //convert data from view format to model format
            var number = parser( value );
            return number;
          }
              
          return value;
        });

        ngModel.$formatters.push(function(value) {
          if(value != null) {
            var number = value;
                
            if(typeof number === 'string') {
              number = parseInt(value);
            }
                
            //convert data from model format to view format
            return formatter(number);
          }
                
          return value;
        });
      
        ngModel.$validators.integer = function(modelValue, viewValue) {
          if (ngModel.$isEmpty(viewValue)) {
            // consider empty models to be valid
            return true;
          }
          
          var number = parser( viewValue );
          
          return $.isNumeric(number);        
        }
      }
    }    
  }
  
  angular.module("dashboard-accordion", ["dashboard-service"]);
  angular.module('dashboard-accordion')
  .directive('locationFilter', LocationFilter)
  .directive('typeAccordion', TypeAccordion)
  .directive('accordionAttribute', AccordionAttribute)
  .directive('numberType', NumberType)
  .directive('dateType', DateType)
  .directive('characterType', CharacterType)
  .directive('ontologyType', OntologyType)
  .directive('booleanType', BooleanType)
  .directive('numberOnly', NumberOnly)
  .directive('integerOnly', IntegerOnly);
})();