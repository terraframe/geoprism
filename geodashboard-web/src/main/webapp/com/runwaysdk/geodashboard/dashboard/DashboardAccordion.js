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
      }
    }    
  }  
  
  function TypeAccordionController($scope, $timeout, dashboardService, jQueryService) {
    var controller = this;

    controller.move = function(element, event, ui) {
      if(dashboardService.canEdit()){

        var typeIds = [];
         
        var children = element.children();
          
        for (var i = 0; i < children.length; i++) {
          var typeId = $(children[i]).data('id');
          
          typeIds.push(typeId);
        }      
        
        var dashboardId = dashboardService.getDashboard().getDashboardId();
      
        dashboardService.setDataSetOrder(dashboardId, typeIds);
      }
    }
    
    controller.init = function(element) {
      // Add support for sorting the dashboard data sets    	
      if(element != null && dashboardService.canEdit()) {
        jQueryService.sortable(element, function(e, ui){
          controller.move(element, e, ui);
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
        newLayer:'&'
      },
      controller : TypeAccordionController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
        /* Hook-up drag and drop */        
        ctrl.init(element);
      }
    }    
  }
  
  function AttributePanelController($scope, dashboardService, jQueryService) {
    var controller = this;
    
    controller.move = function(element, event, ui) {
      if(dashboardService.canEdit()){

        var attributeIds = [];
             
        var children = element.children();
              
        for (var i = 0; i < children.length; i++) {
          var attributeId = $(children[i]).data('id');
              
          attributeIds.push(attributeId);
        }      
            
        var dashboardId = dashboardService.getDashboard().getDashboardId();
        var typeId = $scope.type.id;
          
        dashboardService.setDataSetAttributeOrder(dashboardId, typeId, attributeIds);
      }
    }
    
    controller.init = function(element) {
      if(element != null && dashboardService.canEdit()) {      
        jQueryService.sortable(element, function(e, ui){
          controller.move(element, e, ui);
        });        
      }
    }
  }
  
  function AttributePanel() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/attribute-panel.jsp',      
      scope: {
        type:'=',
        index:'='
      },
      controller : AttributePanelController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
        /* Hook-up drag and drop */        
        ctrl.init(element);
      }
    }    
  }
  
  function AccordionAttributeController($scope, $timeout, dashboardService) {
    var controller = this;
    
    controller.canEdit = function() {
      return dashboardService.canEdit();
    }
    
    controller.newLayer = function(mdAttributeId) {
      $scope.$emit('newThematicLayer', {mdAttributeId:mdAttributeId});
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
  
  function AccordionAttribute(dashboardService) {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/accordion-attribute.jsp', 
      scope: {
        attribute:'=',
        identifier:'@'
      },
      controller : AccordionAttributeController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
      
        // Don't collapse the element if there are filtering values            
        element.ready(function(){        
          if(!dashboardService.isEmptyFilter(scope.attribute.filter)) {
            ctrl.expand(element);
          }      
        });
      }
    }    
  }
  
  function AttributeTypeController($scope, $timeout) {
    var controller = this;

    controller.init = function(element) {
    }
    
    controller.getTextSuggestions = function(mdAttributeId, request, response ) {
        
      var limit = 10;
              
      var onSuccess = function(results){
              
        response( results );
      };
            
      var onFailure = function(e){
        console.log(e);
      };
         
      var text = request.term;
      
      dashboardService.getTextSuggestions(mdAttributeId, term, 10, onSuccess, onFailure);
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
  
  function CharacterTypeController($scope, $timeout, dashboardService) {
    var controller = this;

    controller.init = function(element) {
    }
    
    controller.getTextSuggestions = function(request, response ) {
        
      var limit = 10;
              
      var onSuccess = function(results){
              
        response( results );
      };
            
      var onFailure = function(e){
        console.log(e);
      };
         
      var term = request.term;
      var mdAttributeId = $scope.attribute.mdAttributeId;
      
      dashboardService.getTextSuggestions(mdAttributeId, term, 10, onSuccess, onFailure);
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
      controller : CharacterTypeController,
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
      }
    }    
  }
  
  
  angular.module("dashboard-accordion", ["dashboard-service", "jquery-service", "styled-inputs"]);
  angular.module('dashboard-accordion')
  .directive('locationFilter', LocationFilter)
  .directive('typeAccordion', TypeAccordion)
  .directive('attributePanel', AttributePanel)
  .directive('accordionAttribute', AccordionAttribute)
  .directive('numberType', NumberType)
  .directive('dateType', DateType)
  .directive('characterType', CharacterType)
  .directive('ontologyType', OntologyType)
  .directive('booleanType', BooleanType);
})();