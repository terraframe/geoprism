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
  function ReportPanelController($scope, dashboardService) {
    var controller = this;
    controller.state = 'min';
    
    controller.canEdit = function() {
      return dashboardService.canEdit();
    }
    
    controller.collapse = function() {
      var height = $("#mapDivId").height();
    	
      if(controller.state === 'split'){
  	    controller.setReportPanelHeight(0, false);
        controller.state = 'min';
      }
      else if(controller.state === 'max'){
        var splitHeight = Math.floor(height / 2);
        
        controller.setReportPanelHeight(splitHeight, true);
        controller.state = 'split';
      }
    }
    
    controller.expand = function() {
      var height = $("#mapDivId").height();
    	
      if(controller.state === 'min'){
        var splitHeight = Math.floor(height / 2);
        
        controller.setReportPanelHeight(splitHeight, false);
        controller.state = 'split';
      }
      else if(controller.state === 'split'){
        var reportToolbarHeight = 30;
        
        controller.setReportPanelHeight(height + reportToolbarHeight, true);        
        controller.state = 'max';
      }    	
    }
    
    controller.setReportPanelHeight = function (height, flipButton) {
      var current = $("#reporticng-container").height();
      var toolbar = $("#report-toolbar").height();        
        
      // Minimize
      if(current > height)
      {
        var difference = (current - height);
          
        $("#reporticng-container").animate({ bottom: "-=" + difference + "px" }, 1000, function(){
            
          if(flipButton){
            $("#report-toggle-container").toggleClass("maxed");
          }
            
          $("#reporticng-container").css("bottom", "0px");                                                  
          $("#report-viewport").height(height-toolbar);
          $("#reporticng-container").height(height);
        });     
          
        // animate the loading spinner
        $(".standby-overlay").animate({top: "+=" + difference + "px"}, 1000);
      }
      // Maximize
      else if (current < height){
        var difference = (height - current);
          
        $("#reporticng-container").css("bottom", "-" + difference + "px");
        $("#reporticng-container").height(height);
        $("#report-viewport").height(height-toolbar);
              
        $("#reporticng-container").animate({bottom: "+=" + difference + "px"}, 1000, function() {
          if(flipButton){
            $("#report-toggle-container").toggleClass("maxed");
          }
        });
          
        // animate the loading spinner
        $(".standby-overlay").animate({top: "-=" + difference + "px"}, 1000);
        $(".standby-overlay").css("height", current + difference);
      }          
    }
    
    controller.upload = function(e) {
      var dashboardId = dashboardService.getDashboard().getDashboardId();
      
      var config = {
        type: 'com.runwaysdk.geodashboard.report.ReportItem',
        action: "update",
        viewAction: "edit",
        viewParams: {id: dashboardId},          
        width: 600,
        onSuccess : function(dto) {
          $("#report-export-container").show();
          
          $scope.hasReport = true;
        },
        onFailure : function(e) {
          GDB.ExceptionHandler.handleException(e);
        },
        onCancel : function(e) {
          var request = new Mojo.ClientRequest({
            onSuccess : function () {
              // Close the dialog ??
            },
            onFailure : function(e) {
              GDB.ExceptionHandler.handleException(e);
            }
          });
            
          com.runwaysdk.geodashboard.report.ReportItem.unlockByDashboard(request, dashboardId);
        }
      };
            
      new com.runwaysdk.ui.RunwayControllerFormDialog(config).render();
    }    
    
    controller.exportReport = function(format){
      var dashboard = dashboardService.getDashboard();
      dashboard.exportReport(format);      
    }
  }
  
  function ReportPanel() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/report-panel.jsp',
      scope: {
        hasReport:'=',
      },
      controller : ReportPanelController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
      }
    }    
  }
  
  angular.module("report-panel", ["dashboard-service"]);
  angular.module('report-panel')
    .directive('reportPanel', ReportPanel);
})();