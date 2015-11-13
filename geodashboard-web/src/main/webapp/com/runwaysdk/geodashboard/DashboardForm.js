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
  
  var DashboardForm = Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.DashboardForm', {
    Extends : com.runwaysdk.ui.Component,  
    IsAbstract : false,    
    Constants : {
      DASHBOARD_MODAL : '#dashboard-dialog'
    },
    Instance : {
      initialize : function(controller, dashboardId){
        this._controller = controller;
        this._dashboardId = dashboardId;
        
        this._DashboardController = com.runwaysdk.geodashboard.DashboardController;
      },
      
      _addDashboardUsersHTML : function() {
          
        var usersJSON = $("#add-dashboard-users-container").data("dashboarduserjson");
          var users = com.runwaysdk.geodashboard.gis.CategoryWidget.getValueFromHTML(usersJSON);
          var html = '<div class="holder"><div class="row-holder">';
          
          if(users.length > 0){
              for(var i=0; i<users.length; i++){
                var user = users[i];
                var userId = user.id;
                var checked = "";
                
                if(user.hasAccess){
                  checked = "checked";
                }
                
                var chk = '<div class="check-block">' +
                '<input id="'+userId+'" class="add-user-checkbox" type="checkbox" '+checked+'></input>' +
                '<label for="'+userId+'">'+ user.firstName + " " + user.lastName +'</label>' +
                '</div>';
                    
                html += chk;
              }
          }
          else{
            html += '<p class="dialog-msg">'+ com.runwaysdk.Localize.localize("dashboard", "noUsersMsg") +'</p>';
          }
          
          html += '</div></div>';
          
          // Set the html
          $( "#add-dashboard-users-container" ).html(html);
          jcf.customForms.replaceAll($( "#add-dashboard-users-container" )[0]);
        },
      
      _displayDashboardCloneForm : function(html) {
        var that = this;
        
        // Remove the internal form div if it exists
        $( "#dashboard-dialog" ).remove();
          
        // Set the html of the dialog
        $( "#clone-container" ).html(html);
          
        // Show the dialog
        $( "#dashboard-dialog" ).dialog({
          resizable: false,
          height:200,
          width:730,
          modal: true,
          buttons: [{
            text : com.runwaysdk.Localize.localize("dashboard", "Ok", "Ok"),
            "class": 'btn btn-primary',
            click : function() {
              var request = new com.runwaysdk.geodashboard.StandbyClientRequest({
                onSuccess : function(dashboard){
                  that._controller.addDashboard(dashboard.getId(), dashboard.getDisplayLabel().getLocalizedValue());
                },
                onFailure : function(e){
                  $( "#dashboard-dialog" ).dialog( "close" );
                    
                  that.handleException(e);
                }
              }, $("#dashboard-dialog").parent().get(0));
                
              var dashboardId = $('#clone-dashboard-id').val();
              var label = $('#clone-label').val();
                
              if(label != null && label.length > 0)
              {
                com.runwaysdk.geodashboard.Dashboard.clone(request, dashboardId, label);                    
              }
              else
              {
                var msg = com.runwaysdk.Localize.localize("dashboard", "Required");
                
                $('#clone-label-error').html(msg);        
                $('#clone-label-field-row').addClass('field-error');                    
              }
            }
          },
          {
            text : com.runwaysdk.Localize.localize("dashboard", "Cancel", "Cancel"),
            "class": 'btn btn-default',
            click : function() {
               $( this ).dialog( "close" );
            }
          }]
        });
      },
      
      /*
       * Renders the dashboard options edit form 
       * 
       * @html - html returned from the controller
       */
      _displayDashboardEditForm : function(html) {
        var that = this;
        
        // Remove the internal form div if it exists
          $(this.getImpl()).remove();
          
          // Set the html of the dialog
          $( "#dashboard-edit-container" ).html(html);
          jcf.customForms.replaceAll($( "#dashboard-edit-container" )[0]);
          
          // Show the dialog
          $(this.getImpl()).dialog({
            resizable: false,
            height:300,
            width:730,
            modal: true,
            buttons: [{
              text : com.runwaysdk.Localize.localize("dashboard", "Ok", "Ok"),
              "class": 'btn btn-primary',
              click : function() {
                var dashboardId = $('#dashboard-id').val();
                var label = $('#dashboard-label').val();
                var request = new com.runwaysdk.geodashboard.StandbyClientRequest({
                  onSuccess : function(dashboard){
                    
                    var label = $('#dashboard-label').val();
                    $("#dashboard-dropdown").find(".active").text(label);
                    
                    jcf.customForms.refreshElement($('#dashboard-dropdown').get(0));
                    
                    $(that.getImpl()).dialog( "close" );
                  },
                  onFailure : function(e){
                    $(that.getImpl()).dialog( "close" );
                    that.handleException(e);
                  }
                }, $(that.getImpl()).parent().get(0));
                
                var userIds = that._getDashboardUserPrivilegeAssignments();
                
                if(label != null && label.length > 0)
                {
                  com.runwaysdk.geodashboard.Dashboard.applyWithOptions(request, dashboardId, userIds, label);                    
                }
                else
                {
                  var msg = com.runwaysdk.Localize.localize("dashboard", "Required");
                  
                  $('#dashboard-label-error').html(msg);        
                  $('#dashboard-label-field-row').addClass('field-error');                    
                }
              }
            },
            {
              text : com.runwaysdk.Localize.localize("dashboard", "Cancel", "Cancel"),
              "class": 'btn btn-default',
              click : function() {
                 $( this ).dialog( "close" );
              }
            }]
          });
      },
      
      /*
       * Scrape dashboard user privilege settings from the dashboard options form
       */
      _getDashboardUserPrivilegeAssignments : function() {
          var inputs = $("#add-dashboard-users-container input");
          var userIds = [];
          for(var i=0; i<inputs.length; i++){
            var input = inputs[i];
            var userObj = {};
            if(input.checked){
              userObj[input.id] = true;
            }
            else{
              userObj[input.id] = false;
            }
            userIds.push(userObj);
          }
          
          return userIds;
      },
      
      edit : function(id) {
          var that = this;
          
           var request = new Mojo.ClientRequest({
              onSuccess : function(html){   
                that._displayDashboardEditForm(html);
                that._addDashboardUsersHTML();
              },
              onFailure : function(e){
                that.handleException(e);
              }
            });
            
            this._DashboardController.edit(request, this._dashboardId);  
        },
        
        open : function() {
          var that = this;
            
          var request = new Mojo.ClientRequest({
            onSuccess : function(html){   
              that._displayDashboardCloneForm(html);
            },
            onFailure : function(e){
              that.handleException(e);
            }
          });
            
          this._DashboardController.newClone(request, this._dashboardId);    
        },
      
      /**
       * Closes the overlay with the layer/style CRUD.
       * 
       */
      _closeDashboardModal : function(){
        this.getImpl().modal('hide').html('');
      },
      
      
      getImpl : function(){
        return $(DashboardForm.DASHBOARD_MODAL);
      }
    },
    Static : {
      /**
       * Convert an RGB or RGBA string in the form RBG(255,255,255) to #ffffff
       * 
       */
      rgb2hex : function(rgb) {
        if(rgb != null) {
            
          if (/^#[0-9A-F]{6}$/i.test(rgb)){
            return rgb;
          }

          var rgbMatch = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
          if(rgbMatch){
            function hex(x) {
              return ("0" + parseInt(x).toString(16)).slice(-2);
            }
            return "#" + hex(rgbMatch[1]) + hex(rgbMatch[2]) + hex(rgbMatch[3]);
          }
            
          var rgbaMatch = rgb.match(/^rgba?[\s+]?\([\s+]?(\d+)[\s+]?,[\s+]?(\d+)[\s+]?,[\s+]?(\d+)[\s+]?/i);
          if(rgbaMatch){
            return (rgbaMatch && rgbaMatch.length === 4) ? "#" +
               ("0" + parseInt(rgbaMatch[1],10).toString(16)).slice(-2) +
               ("0" + parseInt(rgbaMatch[2],10).toString(16)).slice(-2) +
               ("0" + parseInt(rgbaMatch[3],10).toString(16)).slice(-2) : '';
          }
        }
      },
    }
  });
  
})();
  
  
