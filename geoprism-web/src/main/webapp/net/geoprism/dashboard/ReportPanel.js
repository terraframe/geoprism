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
  function FileDirective(){
    return {
      scope: {
        file: '&'
      },
      link: function (scope, el, attrs) {
        el.bind('change', function (event) {
          var file = event.target.files[0];
          scope.file()(file);
          scope.$apply();
        });
      }
    };
  };

  function ReportDialogController($scope, $rootScope, dashboardService) {
    var controller = this;
    
    controller.clear = function() {
      // Flag indicating if the modal and all of its elements should be destroyed
      $scope.show = false;      
      $scope.file = null;      
      $scope.report = null;
      $scope.errors = [];
    };
    
    controller.cancel = function() {
      if(!$scope.report.newInstance) {
        
        var onSuccess = function() {      
          controller.clear();        
        };
        
        var onFailure = function(e){
          $scope.errors.push(e.localizedMessage);
        };             
        
        $scope.errors = [];
        
        dashboardService.unlockReport($scope.report.oid, '#report-form', onSuccess);
      }
      else {
        controller.clear();                
      }
    };
    
    controller.persist = function() {
      if($scope.file != null) {      
        var onSuccess = function(response) {      
          controller.clear();
          
          $scope.$emit('reportUpdate', response.data)
        };
        
        var onFailure = function(e){
          $scope.errors.push(e.localizedMessage);
        };             
        
        $scope.errors = [];
            
        dashboardService.uploadReport($scope.report.dashboardId, $scope.file, '#report-form', onSuccess, onFailure);      
      }
    };
    
    controller.init = function(report) {
      controller.clear();
      
      $scope.report = report;
      $scope.show = true;
    }
    
    controller.onFileSelect = function(file) {      
      $scope.file = file ? file : undefined;
    }
    
    $rootScope.$on('editReport', function($event, data){
      controller.init(data.report)  
    });
    
    // Initialize an empty controller
    controller.clear();
  }
  
  function ReportDialog() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/dashboard/report-dialog.jsp',
      scope: {
      },
      controller : ReportDialogController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
      }
    }    
  }
  
  function ReportPanelController($scope, $rootScope, dashboardService) {
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
            
      var onSuccess = function(response) {      
        var report = response.data;
      
        $scope.$emit('editReport', {report:report});      
      };
          
      dashboardService.editReport(dashboardId, '#container', onSuccess);      
    }    
    
    controller.exportReport = function(format){
      $scope.$emit('exportReport', {format:format});
    }
    
    controller.remove = function() {
      var dashboardId = dashboardService.getDashboard().getDashboardId();
      
      var onSuccess = function(){
        $scope.hasReport = false;          
        // $scope.$apply();        
      };
      
      dashboardService.removeReport(dashboardId, "#report-viewport", onSuccess);
    }
    
    $rootScope.$on('reportUpdate', function($event, data){
      $scope.hasReport = true;    	
    });
  }
  
  function ReportPanel() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/dashboard/report-panel.jsp',
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
    .directive('file', FileDirective)
    .directive('reportDialog', ReportDialog)
    .directive('reportPanel', ReportPanel);
})();