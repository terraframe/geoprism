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
	
  function DashboardPanelController($scope, $timeout, dashboardService) {
    var controller = this;
    controller.expanded = true;
    
    controller.toggle = function() {
      var speed = 500;
        
      if(controller.expanded){
        $("#dashboardMetadata").animate({right: "-=300"}, speed, function() {
          controller.expanded = false;
          
          $scope.$apply();
        });
          
        // Report Panel background
        $("#report-viewport").animate({marginRight: "0px"}, speed);
          
        // Repprt panel toolbar
        $("#report-toolbar").animate({marginRight: "0px"}, speed);
      }
      else{
        $("#dashboardMetadata").animate({right: "+=300"}, speed, function() {
          controller.expanded = true;

          $scope.$apply();          
        });
          
        // Report Panel background
        $("#report-viewport").animate({marginRight: "300px"}, speed);
        
        // Repprt panel toolbar
        $("#report-toolbar").animate({marginRight: "300px"},speed);
      }
    }
  }
	  
  function DashboardPanel() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/dashboard-panel.jsp',
      scope: {
        dashboard:'='
      },
      controller : DashboardPanelController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
      }
    }    
  }  
	
  angular.module("dashboard-panel", ["dashboard-service", "dashboard-accordion"]);
  angular.module('dashboard-panel')
    .directive('dashboardPanel', DashboardPanel);  
})();