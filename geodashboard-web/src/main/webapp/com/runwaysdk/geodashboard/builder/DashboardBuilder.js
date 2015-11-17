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

  function BuilderDialogController($scope, $timeout, builderService) {
    var controller = this;
    
    controller.cancel = function() {
      var onSuccess = function() {      
        $scope.show = null;
          
        controller.fields = null;
        controller.dashboard = null;
        
        $scope.$apply();
      }
        
      builderService.unlock(controller.dashboard, onSuccess);
    }
    
    controller.persist = function() {
      var onSuccess = function(result) {      
        $scope.show = null;          
        controller.fields = null;    
        controller.dashboard = null;  
                
        $scope.$apply();

        var dashboard = JSON.parse(result);

        $scope.callback.refresh(dashboard);
      }    
      
      builderService.applyWithOptions(controller.dashboard, onSuccess);
    }
    
    controller.load = function() {
    
      var dashboardId = ($scope.show != null && $scope.show != 'NEW' ? $scope.show : null);
        
      var onSuccess = function(result) {
        controller.fields = result.fields;    
        controller.dashboard = result.object;
        
        $scope.$apply();
      }
               
      builderService.loadDashboard(dashboardId, onSuccess);    
    }
  }
  
  function BuilderDialog() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/builder/builder-dialog.jsp',
      scope: {
        show:'=',
        callback:'='
      },
      controller : BuilderDialogController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
        ctrl.load();
      }
    }    
  }
  
  function TextField() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/builder/text-field.jsp',
      require: '^form',      
      scope: {
        field:'=',
        model:'='
      },
      link: function (scope, element, attrs, form) {
        scope.form = form;  
      }
    }    
  }
  
  function SelectFieldController($scope, $timeout) {
    var controller = this;

    controller.init = function(element) {
//  $timeout(function(){
//    jcf.customForms.replaceAll(element[0]);      
//  }, 50, false);
    }    
  }
  
  function SelectField() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/builder/select-field.jsp',
      scope: {
        field:'=',
        model:'='
      },
      require: ['^form', 'selectField'],            
      controller : SelectFieldController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrls) {
        scope.form = ctrls[0];
    
        element.ready(function(){
          ctrls[1].init(element);        
        });
      }
    }    
  }
  
  function TypeAttributeController($scope) {
    $scope.$watch('attribute.selected', function() {
      if($scope.attribute.selected && !$scope.type.value){
        $scope.type.value = true;          
      }
    }, true);
        
    $scope.$watch('type.value', function() {
      if(!$scope.type.value && $scope.attribute.selected){
        $scope.attribute.selected = false;          
      }
    }, true);              
  }
  
  function TypeAttribute() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/builder/type-attribute.jsp',
      scope: {
        attribute:'=',
        type:'='
      },
      controller : TypeAttributeController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
      }
    }    
  }
  
  function ModalDialog() {
    return {
      restrict: 'E',
      scope: {
      },
      replace: true, // Replace with the template below
      transclude: true, // we want to insert custom content inside the directive
      templateUrl: '/partial/dialog/modal-dialog.jsp',
      link: function(scope, element, attrs) {
        scope.dialogStyle = {};
        
        if (attrs.width) {
          scope.dialogStyle.width = attrs.width;        
        }
        
        if (attrs.height) {
          scope.dialogStyle.height = attrs.height;        
        }
        
        scope.hideModal = function() {
          scope.show = false;
        };
      }
    };
  }


  angular.module("dashboard-builder", ["builder-service"]);
  angular.module("dashboard-builder")
   .directive('textField', TextField)
   .directive('selectField', SelectField)
   .directive('modalDialog', ModalDialog)
   .directive('typeAttribute', TypeAttribute)
   .directive('builderDialog', BuilderDialog);
})();