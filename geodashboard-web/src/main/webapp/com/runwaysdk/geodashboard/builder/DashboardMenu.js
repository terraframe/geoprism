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

  function DashboardMenuController($scope, $timeout, dashboardService) {
    var controller = this;
    controller.show = null;    
    
    controller.getDashboards = function() {
      var onSuccess = function(json) {
        $timeout(function() {
          var response = JSON.parse(json);
          
          controller.ids = [];
          controller.dashboards = {};
          controller.editDashboard = response.editDashboard;
          
          for(var i = 0; i < response.dashboards.length; i++) {
            var dashboard = response.dashboards[i];
            
            controller.ids.push(dashboard.dashboardId);
            controller.dashboards[dashboard.dashboardId] = dashboard;            
          }
            
          $scope.$apply();
        }, 0);
      };
              
      dashboardService.getAvailableDashboardsAsJSON('', onSuccess);          
    }
    
    controller.account = function() {
      var dialog = com.runwaysdk.ui.Manager.getFactory().newDialog(com.runwaysdk.Localize.get("accountSettings", "Account Settings"), {modal: true, width: 600});
      dialog.addButton(com.runwaysdk.Localize.get("rOk", "Ok"), function(){
        dialog.close();
      }, null, {primary: true});
      dialog.setStyle("z-index", 2001);
      dialog.render();    
        
      var ut = new com.runwaysdk.ui.userstable.UserForm();  
      ut.render("#"+dialog.getContentEl().getId());
    }
    
    controller.newDashboard = function() {
      controller.show = 'NEW';      
    }
    
    controller.edit = function(dashboardId) {
      controller.show = dashboardId;
    }
    
    controller.remove = function(dashboardId) {
      var that = this;
      var fac = com.runwaysdk.ui.Manager.getFactory();
      
      var message = com.runwaysdk.Localize.localize("dashboardViewer", "deleteDashboardContent", "Are you sure you want to delete dashboard [{0}]?");
      message = message.replace('{0}', controller.dashboards[dashboardId].label);
          
      var dialog = fac.newDialog(com.runwaysdk.Localize.localize("dashboardViewer", "deleteDashboardDialog", "Delete dashboard"));
      dialog.appendContent(message);
          
      var Structure = com.runwaysdk.structure;
      var tq = new Structure.TaskQueue();
          
      tq.addTask(new Structure.TaskIF({
        start : function(){            
         var cancelCallback = function() {
            dialog.close();
            tq.stop();
          };

          dialog.addButton(com.runwaysdk.Localize.localize("dashboardViewer", "delete", "Delete"), function() { tq.next(); }, null, {class:'btn btn-primary'});
          dialog.addButton(com.runwaysdk.Localize.localize("dashboardViewer", "cancel", "Cancel"), cancelCallback, null, {class:'btn'});            
          dialog.render();
        }
      }));
          
      tq.addTask(new Structure.TaskIF({
        start : function(){
          dialog.close();
            
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
            }
            
            $scope.$apply();
          };          
          
          dashboardService.removeDashboard(dashboardId, "#container", onSuccess);
        }
      }));
          
      tq.start();             
    }
    
    controller.refreshDashboard = function(dashboard) {
      
      var newDashboard = {dashboardId:dashboard.id, label:dashboard.label};
      var oldDashboard = controller.dashboards[dashboard.id];
      
      if(oldDashboard != null) {    	
        angular.copy(newDashboard, oldDashboard);                	  
      }
      else {
        controller.dashboards[dashboard.id] = newDashboard;  
        controller.ids.push(dashboard.id);        
      }     
      
      $scope.$apply();
    }
    
    controller.getDashboards();	
  }

  angular.module("dashboard-menu", ["dashboard-service", "dashboard-builder"]);
  angular.module("dashboard-menu")
   .controller('DashboardMenuController', DashboardMenuController)
})();