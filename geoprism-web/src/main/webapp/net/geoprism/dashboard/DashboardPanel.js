/*
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
(function(){
	
  function DashboardPanelController($scope, $timeout, widgetService) {
    var controller = this;
    controller.expanded = true;
    
    controller.toggle = function() {
      var speed = 500;
        
      if(controller.expanded){
        widgetService.animate("#dashboardMetadata", {right: "-=300"}, speed, function(){
          controller.expanded = false;
          // $scope.$apply();
        });

        // Report Panel background
        widgetService.animate("#report-viewport", {marginRight: "0px"}, speed);
        
        // Repprt panel toolbar
        widgetService.animate("#report-toolbar", {marginRight: "0px"}, speed);
      }
      else{    	  
        widgetService.animate("#dashboardMetadata", {right: "+=300"}, speed, function(){
          controller.expanded = true;
          // $scope.$apply();
        });

        // Report Panel background
        widgetService.animate("#report-viewport", {marginRight: "300px"}, speed);
            
        // Repprt panel toolbar
        widgetService.animate("#report-toolbar", {marginRight: "300px"}, speed);
      }
    }
  }
	  
  function DashboardPanel() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/dashboard/dashboard-panel.jsp',
      scope: {
        dashboard:'='
      },
      controller : DashboardPanelController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
      }
    }    
  }  
	
  angular.module("dashboard-panel", ["widget-service", "dashboard-accordion"]);
  angular.module('dashboard-panel')
    .directive('dashboardPanel', DashboardPanel);  
})();