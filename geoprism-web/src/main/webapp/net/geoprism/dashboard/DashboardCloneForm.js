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
  function CloneFormController($scope, $rootScope, dashboardService) {
    var controller = this;
    
    controller.submit = function() {
      var onSuccess = function(dashboard) {
        $scope.$emit('dashboardChange', {dashboard : dashboard});
        
        controller.clear();
        
        $scope.$apply();
      };
      
      var onFailure = function(e){
        $scope.errors = [];
        $scope.errors.push(e.localizedMessage);
                   
        $scope.$apply();
      };
      
      $scope.errors = [];
      
      dashboardService.cloneDashboard($scope.dashboard.id, $scope.dashboard.name, '#clone-modal', onSuccess, onFailure);
    }
    
    controller.cancel = function() {
      controller.clear();      
    }
    
    controller.clear = function() {
      $scope.dashboard = undefined;
      $scope.errors = undefined;
      $scope.show = undefined;      
    }
    
    controller.load = function(dashboard) {
      $scope.dashboard = dashboard;
      $scope.dashboard.name = "";
      
      $scope.errors = [];
      $scope.show = true;
    }
    
    controller.newClone = function(dashboardId, elementId) {
      var onSuccess = function(response) {        
        controller.load(response.dashboard);
        
        $scope.$apply();
      };
      
      dashboardService.newClone(dashboardId, elementId, onSuccess);
    }
    
    $rootScope.$on('cloneDashboard', function(event, data) {
      var dashboardId = data.dashboardId;
      var elementId = data.elementId;

      controller.newClone(dashboardId, elementId);
    });
  }
  
  function CloneForm() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/clone-form.jsp',
      scope: {},
      controller : CloneFormController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
      }
    }    
  }
  
  angular.module("dashboard-clone-form", ["dashboard-service", "styled-inputs"]);
  angular.module('dashboard-clone-form')
    .directive('cloneForm', CloneForm);
})();