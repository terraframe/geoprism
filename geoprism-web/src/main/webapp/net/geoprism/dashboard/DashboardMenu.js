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

  function DashboardMenuController($scope, $timeout, dashboardService, localizationService, widgetService) {
    var controller = this;
    controller.show = null;   
    controller.isInExistingRow = true;
    
    controller.getDashboards = function() {
      var onSuccess = function(json) {
        $timeout(function() {
          var response = json.data;
          
          controller.ids = [];
          controller.dashboards = {};
          controller.editDashboard = response.editDashboard;
          controller.isInExistingRow = (response.dashboards.length > 0);
          
          for(var i = 0; i < response.dashboards.length; i++) {
            var dashboard = response.dashboards[i];
            
            controller.ids.push(dashboard.dashboardId);
            controller.dashboards[dashboard.dashboardId] = dashboard;  
            
            if(i === response.dashboards.length - 1){
              controller.dashboards[dashboard.dashboardId].isLastDashboard = true;
              if((i+1) % 3 === 0){
                controller.isInExistingRow = false;
              }
            }
          }
            
          // $scope.$apply();
        }, 0);
      };
      
      var onFailure = function(json) {
        console.log("Err: Could not get available dashboards");
      };
              
      dashboardService.getAvailableDashboardsAsJSON('', onSuccess, onFailure);          
    }
    
    controller.newDashboard = function() {
      $scope.$emit('newDashboard', {element : '#container'});
    }
    
    controller.edit = function(dashboardId) {
      $scope.$emit('editDashboard', {dashboardId : dashboardId, element : '#container'});
    }
    
    controller.cloneDashboard = function(dashboardId) {
      $scope.$emit('cloneDashboard', {dashboardId : dashboardId, element : '#container'});      
    }
    
    controller.remove = function(dashboardId) {
      var title = localizationService.localize("dashboardViewer", "deleteDashboardDialog");
        
      var message = localizationService.localize("dashboardViewer", "deleteDashboardContent");
      message = message.replace('{0}', controller.dashboards[dashboardId].label);
        
      var buttons = [];
      buttons.push({
        label : localizationService.localize("dashboardViewer", "delete"),
        config : {class:'btn btn-primary'},
        callback : function(){
          var onSuccess = function() {
            // Remove the dashboard from the array
            var index = undefined;
                  
            for(var i = 0; i < controller.ids.length; i++) {
              var id = controller.ids[i];
                      
              if(id == dashboardId) {
                index = i;
              }              
            }
                  
            if(index != null) {
              controller.ids.splice(index, 1);              
              delete controller.dashboards[dashboardId];
                    
              controller.toggleCreateDashboardIcon();
            }
                  
            // $scope.$apply();
          };          
                
          dashboardService.removeDashboard(dashboardId, "#container", onSuccess);
        }
      });
      buttons.push({
        label : localizationService.localize("dashboardViewer", "cancel"),
        config : {class:'btn'},
      });
        
      widgetService.createDialog(title, message, buttons);
    }
    
    controller.refreshDashboard = function(dashboard) {
      for(var key in controller.dashboards){
        if (controller.dashboards.hasOwnProperty(key)) {
          var db = controller.dashboards[key];
          if(db.isLastDashboard){
            db.isLastDashboard = false;
          }
        }
      }
      
      var newDashboard = {
          dashboardId:dashboard.oid, 
          label:dashboard.label, 
          description:dashboard.description, 
//          focusAreasAsString:dashboard.focusAreas.toString(),
          isLastDashboard:true};
      var oldDashboard = controller.dashboards[dashboard.oid];
      
      if(oldDashboard != null) {      
        angular.copy(newDashboard, oldDashboard);                    
      }
      else {
        controller.dashboards[dashboard.oid] = newDashboard;  
        controller.ids.push(dashboard.oid);        
      }     
      
      controller.toggleCreateDashboardIcon();
      
//      // $scope.$apply();
    }
    
    controller.toggleCreateDashboardIcon = function(){
        // handling the toggle of create new dashboard icon when outside of an existing row
        if(controller.ids.length % 3 === 0){
          controller.isInExistingRow = false;
        }
        else if((controller.ids.length - 1) % 3 === 0){
           controller.isInExistingRow = true;
        }
        else if((controller.ids.length + 1) % 3 === 0){
         controller.isInExistingRow = true;
      }
        
        // maintain the last dashboard status flag
        var i = 0;
        for(var key in controller.dashboards){
        if (controller.dashboards.hasOwnProperty(key)) {
          var db = controller.dashboards[key];
          if(db.isLastDashboard){
            db.isLastDashboard = false;
          }
          
          if(i === Object.keys(controller.dashboards).length -1){
            db.isLastDashboard = true;
          }
          
          i++;
        }
      }
        
//        // $scope.$apply();
    }
    
    $scope.$on('dashboardChange', function(event, data){
      controller.refreshDashboard(data.dashboard);
      
      event.stopPropagation();            
    });
    
    controller.getDashboards();  
  }

  angular.module("dashboard-menu", ["dashboard-service", "widget-service", "localization-service", "dashboard-builder", "data-uploader", "dashboard-clone-form"]);
  angular.module("dashboard-menu")
   .controller('DashboardMenuController', DashboardMenuController)
})();