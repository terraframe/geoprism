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
  var parser = null;
  var formatter = null;
  
  var getParser = function() {
    if(parser == null) {
      parser = Globalize.numberParser();        
    }
  
    return parser;
  }
  
  var getFormatter = function() {
    if(formatter == null) {
      formatter = Globalize.numberFormatter();        
    }
    
    return formatter;
  }
  
  function DashboardController($scope) {
    var controller = this;
  
    controller.dashboardId = '';

    /* Getters */
    controller.getDashboardId = function() {
      return controller.dashboardId;
    }  

    /* Initialization Function */
    $scope.init = function(dashboardId) {
      controller.model = {
        location : {label:'',value:''},
        editDashboard : false,
        editData : false,
        types : [],
        dashboardId : dashboardId
      };
      controller.dashboardId = dashboardId;      
    }
  
    $scope.$watch(controller.getDashboardId(), function(newVal, oldVal){
      controller.load();    
    }, true);
    
    /* Controller Functions */
    controller.load = function() {
      var request = new Mojo.ClientRequest({
        onSuccess : function(json){
          controller.model = JSON.parse(json);
          
          $scope.$apply();
        },
        onFailure : function(e){
        }
      });
      
      com.runwaysdk.geodashboard.Dashboard.getJSON(request, controller.dashboardId);
    }
    
    /* Refresh Map Function */
    controller.refresh = function() {
      console.log(controller.model);
    }
    
    controller.save = function() {
      var request = new Mojo.ClientRequest({
        onSuccess : function() {
        },
        onFailure : function(e) {
          alert(e);
        }
      });
          
      com.runwaysdk.geodashboard.Dashboard.saveState(request, controller.dashboardId, controller.model);      
    }
  }
  
  function LocationFilter() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/location-filter.jsp',      
      scope: {
        filter:'=',
        dashboard:'='
      },
      link: function (scope, element, attrs) {
        var input = $(element).find(".filter-geo");

        if(scope.filter.value != null) {
          input.val(scope.filter.label);          
        }
        
        input.autocomplete({
          source: function( request, response ) {  
            var req = new Mojo.ClientRequest({
              onSuccess : function(query){
                var resultSet = query.getResultSet()
                                        
                var results = [];
                      
                $.each(resultSet, function( index, result ) {
                  var label = result.getValue('displayLabel');
                  var id = result.getValue('id');
                        
                  results.push({'label':label, 'value':label, 'id':id});
                });
                      
                response( results );
              },
              onFailure : function(e){
                console.log(e);
              }
            });
                  
            com.runwaysdk.geodashboard.Dashboard.getGeoEntitySuggestions(req, scope.dashboard, request.term, 10);
          },
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
        edittable:'='
      },
      link: function (scope, element, attrs) {
      }
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
      link: function (scope, element, attrs) {
    	  
        // This is a hack because of scoping issues with ng-repeat
        scope.edittable = scope.$parent.$parent.$parent.edittable;
    	  
        // Don't collapse the element if there are filtering values    	      	  
        if(!$.isEmptyObject(scope.attribute.filter)) {
        	element.ready(function(){
              $(element).find(".opener-link").click();
          })
        }    	  
      }
    }    
  }
  
  function NumberType() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/number-type.jsp',      
      require: '^form',
      scope: {
        attribute:'=',
        whole:'=',
      },
      link: function (scope, element, attrs, ctrl) {
        scope.form = ctrl;
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
        var ngModel = $parse(attrs.ngModel);
        
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
            
            scope.attribute.filter.startDate = startDate;
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
            
            scope.attribute.filter.endDate = endDate;
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
      require: '^form',      
      scope: {
        attribute:'='
      },
      link: function (scope, element, attrs, ctrl) {
        scope.form = ctrl;
      }
    }    
  }
  
  function OntologyType() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/ontology-type.jsp',
      require: '^form',
      scope: {
        attribute:'='
      },
      link: function (scope, element, attrs, ctrl) {
        scope.form = ctrl;
          
        var mdAttributeId = scope.attribute.mdAttributeId;
          
        // Get the term roots and setup the tree widget
        var req = new Mojo.ClientRequest({
          onSuccess : function(results){
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
            tree.render("#" + mdAttributeId, nodes);
            
            // Load saved values
            if(scope.attribute.value != null)
            {
              tree.setCheckedTerms(scope.attribute.value);  
            }
          },
          onFailure : function(e){
            console.log(e);
          }
        });
          
        com.runwaysdk.geodashboard.Dashboard.getClassifierTree(req, mdAttributeId);      
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
      }
    }    
  }
  
  function IntegerOnly() {
    return {
      restrict: 'A',
      require: 'ngModel',
      link: function (scope, element, attrs, ctrl) {
        ctrl.$validators.integer = function(modelValue, viewValue) {
          if (ctrl.$isEmpty(modelValue)) {
            // consider empty models to be valid
            return true;
          }
            
          var number = getParser()( modelValue );
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
      link: function (scope, element, attrs, ctrl) {
        ctrl.$validators.integer = function(modelValue, viewValue) {
          if (ctrl.$isEmpty(modelValue)) {
            // consider empty models to be valid
            return true;
          }
          
          var number = getParser()( modelValue );
          
          return $.isNumeric(number);        
        }
      }
    }    
  }
  
  angular.module("dashboard", []);
  angular
  .module('dashboard')
  .controller('DashboardController', DashboardController)
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