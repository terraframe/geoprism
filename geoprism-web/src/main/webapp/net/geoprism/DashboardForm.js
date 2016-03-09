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
  
  var DashboardForm = Mojo.Meta.newClass('net.geoprism.dashboardrdForm', {
    Extends : com.runwaysdk.ui.Component,  
    IsAbstract : false,    
    Constants : {
      DASHBOARD_MODAL : '#dashboard-dialog'
    },
    Instance : {
      initialize : function(controller, dashboardId){
        this._controller = controller;
        this._dashboardId = dashboardId;
        
        this._DashboardController = net.geoprism.dashboard.DashboardController;
      },
      
      _addDashboardUsersHTML : function() {
          
        var usersJSON = $("#add-dashboard-users-container").data("dashboarduserjson");
        
          var users = net.geoprism.dashboard.CategoryWidget.getValueFromHTML(usersJSON);
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
      
        _addMappableClassesHTML : function() {
          
          var typesJSON = $("#add-dashboard-type-container").data("classes");
          var types = net.geoprism.dasnet.geoprism.dashboardFromHTML(typesJSON);
          var html = '<div class="holder"><div class="row-holder">';
          
          if(types.length > 0){
            for(var i=0; i<types.length; i++){
              var type = types[i];
              var id = type.id;
              var checked = "";
              
              if(type.selected){
                checked = "checked";
              }
              
              var chk = '<div class="check-block">' +
              '<input id="'+id+'" class="add-user-checkbox" type="checkbox" '+checked+'></input>' +
              '<label for="'+id+'">'+ type.label +'</label>' +
              '</div>';
              
              html += chk;
            }
          }
          else{
            html += '<p class="dialog-msg">'+ com.runwaysdk.Localize.localize("dashboard", "noMappableTypesMsg") +'</p>';
          }
          
          html += '</div></div>';
          
          // Set the html
          $( "#add-dashboard-type-container" ).html(html);
          jcf.customForms.replaceAll($( "#add-dashboard-type-container" )[0]);
        },
        
      _displayDashboardCloneForm : function(html) {
        var that = this;
        that._dashboardFocusAreaLabel = this._controller.dashboards[this._dashboardId].focusAreas;
        that._dashboardDescription = this._controller.dashboards[this._dashboardId].description;
        
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
              var request = new net.geoprism.StandbyClientRequest({
                onSuccess : function(dashboard){
                	that._controller.refreshDashboard({
                		id : dashboard.getId(), 
                		label : dashboard.getDisplayLabel().getLocalizedValue(), 
              		  	description : that._dashboardDescription, 
              		  	focusAreas : that._dashboardFocusAreaLabel,
              		  	isLastDashboard : true
                		})
                  
                  $( "#dashboard-dialog" ).dialog( "close" );
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
                net.geoprism.dashboard.Dashboard.clone(request, dashboardId, label);                    
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
                var request = new net.geoprism.StandbyClientRequest({
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
                var types = that._getDashboardTypes();
                
                if(label != null && label.length > 0)
                {
                  var options = JSON.stringify({"label" : label, "userIds" : userIds, "types" : types});
                	
                  net.geoprism.dashboard.Dashboard.applyWithOptions(request, dashboardId, options);                    
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
      
      /*
       * Scrape dashboard types dashboard options form
       */
      _getDashboardTypes : function() {
        var inputs = $("#add-dashboard-type-container input");
        var types = [];
        
        for(var i=0; i<inputs.length; i++){
          var input = inputs[i];
            
          types.push({'checked' : input.checked, 'id' : input.id});
        }
        
        return types;
      },
      
      edit : function(id) {
          var that = this;
          
           var request = new Mojo.ClientRequest({
              onSuccess : function(html){   
                that._displayDashboardEditForm(html);
                that._addDashboardUsersHTML();
                that._addMappableClassesHTML();
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
  
  /**
   * LANGUAGE
   */
   com.runwaysdk.Localize.defineLanguage('net.geoprism.dashboard.Tnet.geoprism.dashboardbmit",
    "cancel" : "Cancel",
    "title"  : "Configure type attributes"
   });

  Mojo.Meta.newClass('net.geoprism.dashboard.TypeForm'net.geoprism.dashboardk.ui.Component,  
    Constants : {
      DASHBOARD_MODAL : '#type-dialog'
    },
    Instance : {
      initialize : function(dashboardId, mdClassId){
        this._dashboardId = dashboardId;
        this._mdClassId = mdClassId;
      },
      getFactory : function ()
      {
        return com.runwaysdk.ui.Manager.getFactory();
      },
      setDialog : function(dialog) {
        this._dialog = dialog;
      },      
      setForm : function(form) {
        this._form = form;
      },      
      render : function() {
        var that = this;

        var element = document.getElementById('type-dialog');
                          
        if(element == null)
        {                
          var request = new Mojo.ClientRequest({
            onSuccess : function(json) {
              var attributes = JSON.parse(json);
                
              var fac = that.getFactory();
              
              var dialog = fac.newDialog(that.localize("title"), {minHeight:250, maxHeight:$(window).height() - 75, minWidth:730, resizable: false});   
              dialog.setId('type-dialog');
                
              var form = new net.geoprism.Form();
              
              var options = [];
              
              for (var i = 0; i < attributes.length; ++i) {
                var attribute = attributes[i];
                var option = {displayLabel:attribute.label, value:attribute.id, checked:attribute.selected}; 
                       
                options.push(option);
              }
              
              var entry = new net.geoprism.CheckboxFormEntry('types', 'Attributes',options);
              form.addEntry(entry);
              
              dialog.appendContent(form);
              dialog.addButton(that.localize("submit"), Mojo.Util.bind(that, that._handleSubmit), null, {id:'dashboard-submit', 'class':'btn btn-primary type-button'});
              dialog.addButton(that.localize("cancel"), Mojo.Util.bind(that, that._handleCancel), null, {id:'dashboard-cancel', 'class':'btn type-button'});            
                            
              that.setDialog(dialog);
              that.setForm(form);
              
              dialog.render();   
              
              if (jcf != null && jcf.customForms != null) {
                jcf.customForms.replaceAll();
              }
            },
            onFailure : function(ex) {
              that.handleException(ex);               
            }
          }); 
            
            
          net.geoprism.MappableClass.getAttributesAsJSON(request, that._dashboardId, that._mdClassId);
        }
      },
      _handleSubmit : function(e) {
        var that = this;

        // Disable the buttons to prevent multiple submits
    	$('.type-button').prop("disabled",true);    	  
          
        // Clear any existing error messages
        this._form.removeErrorMessages();
          
        var values = this._form.getValues();
          
        var request = new net.geoprism.StandbyClientRequest({
          onSuccess : function(dashboard) {
            that._dialog.close();
          },
          onFailure : function(ex) {
            that._form.handleException(ex);
            
            $('.type-button').prop("disabled",false);    	  
          }
        }, this._dialog.getParentNode());

//        that._dashboard.apply(applyCallback);
        this._dialog.close();        
      },
      _handleCancel : function(e) {
        this._dialog.close();
      }
    }
  });
})();
  
  
