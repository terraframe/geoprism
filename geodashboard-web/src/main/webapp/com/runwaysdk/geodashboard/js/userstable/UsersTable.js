/*
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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

//define(["../../ClassFramework", "../../Util", "../RunwaySDK_UI", "../factory/runway/widget/Widget", "../datatable/datasource/InstanceQueryDataSource"], function(ClassFramework, Util, UI, Widget, InstanceQueryDataSource) {
(function(){  

  var ClassFramework = Mojo.Meta;
  var Widget = com.runwaysdk.ui.factory.runway.Widget;
  var InstanceQueryDataSource = com.runwaysdk.ui.datatable.datasource.InstanceQueryDataSource;
  var GenericDataTable = com.runwaysdk.ui.factory.generic.datatable.DataTable;
  var FormEntry = com.runwaysdk.geodashboard.FormEntry;
  var Form = com.runwaysdk.geodashboard.Form;
  
  var defaultQueryType = "com.runwaysdk.geodashboard.GeodashboardUser";
  
  var usersTableName = "com.runwaysdk.ui.userstable.UsersTable";
  
  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage(usersTableName, {
    "newUser" : "New",
    "editUser" : "Edit",
    "deleteUser" : "Delete",
    "dialogEditUserTitle" : "Edit User",
    "submit" : "Submit",
    "cancel" : "Cancel",
    "dialogNewUserTitle" : "New User",
    "dialogDeleteUserTitle" : "Confirm Delete",
    "delete" : "Delete",
    "edit" : "Edit",
    "deleteText" : "Are you sure you want to delete user ",
    "confirmPassword" : "Confirm password",
    "confirmRequired" : "Password confirmation is required",
    "passwordMismatch" : "Passwords do not match",
    "invalidEmail" : "Invalid email address format",
    "admin" : "Admin"
  });
  
  var usersTable = ClassFramework.newClass(usersTableName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(cfg) {
        
        cfg = cfg || {};
        cfg.queryType = cfg.queryType || defaultQueryType;
        this._config = cfg;
        
        this.$initialize("div");
        
      },
      
      _makeNewOrEditForm : function(user, metadataDTO) {
        var form = new Form();
        form.addClassName('submit-form');
        
        if(user.isFirstNameWritable())
        {
          var firstNameInput = form.newInput('text', 'firstName', {attributes:{type:'text', id:'firstName'}});
          firstNameInput.setValue(user ? user.getFirstName() : "");
          form.addEntry(new FormEntry(metadataDTO.getAttributeDTO("firstName").getAttributeMdDTO().getDisplayLabel(), firstNameInput));          
        }
        
        if(user.isLastNameWritable())
        {
          var lastNameInput = form.newInput('text', 'lastName', {attributes:{type:'text', id:'lastName'}});
          lastNameInput.setValue(user ? user.getLastName() : "");
          form.addEntry(new FormEntry(metadataDTO.getAttributeDTO("lastName").getAttributeMdDTO().getDisplayLabel(), lastNameInput));          
        }
        
        if(user.isPhoneNumberWritable())
        {
          var phoneNumberInput = form.newInput('text', 'phoneNumber', {attributes:{type:'text', id:'phoneNumber'}});
          phoneNumberInput.setValue(user ? user.getPhoneNumber() : "");
          form.addEntry(new FormEntry(metadataDTO.getAttributeDTO("phoneNumber").getAttributeMdDTO().getDisplayLabel(), phoneNumberInput));          
        }
        
        if(user.isEmailWritable())
        {
          var emailInput = form.newInput('text', 'email', {attributes:{type:'text', id:'email'}});
          emailInput.setValue(user ? user.getEmail() : "");
          form.addEntry(new FormEntry(metadataDTO.getAttributeDTO("email").getAttributeMdDTO().getDisplayLabel(), emailInput));  
          
          var validateEmail = Mojo.Util.bind(this, function()
          {
            if(emailInput.getValue() !== '')
            {
              document.getElementById('user-submit').disabled = true;

              if(!this._isValidEmail(emailInput.getValue()))              
              {
                form.getEntry(emailInput.getId()).addInlineError(this.localize('invalidEmail'));          
              }
              else
              {
                form.getEntry(emailInput.getId()).removeInlineError();
                document.getElementById('user-submit').disabled = false;              
              }
            }
          });
          
          emailInput.addEventListener('blur', validateEmail);
        }
                
        if(user.isUsernameWritable())
        {          
          var usernameInput = form.newInput('text', 'username', {attributes:{type:'text', id:'username'}});
          usernameInput.setValue(user ? user.getUsername() : "");
          form.addEntry(new FormEntry(metadataDTO.getAttributeDTO("username").getAttributeMdDTO().getDisplayLabel(), usernameInput));
        }
        
        if(user.isPasswordWritable())
        {         
          var passwordInput = form.newInput('text', 'password', {attributes:{type:'password', id:'password'}});
          form.addEntry(new FormEntry(metadataDTO.getAttributeDTO("password").getAttributeMdDTO().getDisplayLabel(), passwordInput));
          
          var confirmInput = form.newInput('text', 'confirm', {attributes:{type:'password', id:'confirm'}});
          form.addEntry(new FormEntry(this.localize("confirmPassword"), confirmInput));
          
          var validatePassword = Mojo.Util.bind(this, function()
          {
            if(passwordInput.getValue() !== '')
            {
              document.getElementById('user-submit').disabled = true;              
              
              if (confirmInput.getValue() === '')
              {
                form.getEntry(confirmInput.getId()).addInlineError(this.localize('confirmRequired'));                
              }
              else
              {
                form.getEntry(confirmInput.getId()).removeInlineError();
                  
                if(passwordInput.getValue() !== confirmInput.getValue())
                {
                  form.getEntry(passwordInput.getId()).addInlineError(this.localize('passwordMismatch'));                
                }
                else
                {
                  form.getEntry(passwordInput.getId()).removeInlineError();
                  
                  document.getElementById('user-submit').disabled = false;                              
                }               
              }
            }
          });

          passwordInput.addEventListener('blur', validatePassword);
          confirmInput.addEventListener('blur', validatePassword);
        }
                        
        return form;
      },
      
      _isValidEmail : function(email) { 
        var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(email);
      },
            
      _populateComponent : function (user, values) {
        if(user.isUsernameWritable())
        {          
          user.setUsername(values.get("username"));
        }
          
        if(user.isPasswordWritable())
        {       
          user.setPassword(values.get("password"));                            
        }
          
        if(user.isFirstNameWritable())
        {
          user.setFirstName(values.get("firstName"));                            
        }
          
        if(user.isLastNameWritable())
        {
          user.setLastName(values.get("lastName"));
        }
          
        if(user.isPhoneNumberWritable())
        {
          user.setPhoneNumber(values.get("phoneNumber"));
        }
        
        if(user.isEmailWritable())
        {
          user.setEmail(values.get("email"));
        }                   
      },
      
      _onEditUser : function(e) {
        var target = e.getTarget();
        var id = target.id;        
        var rowNumber = id.replace('table-edit-', '');
          
        var user = this._table.getDataSource().getResultsQueryDTO().getResultSet()[rowNumber];
        
        var callback = new Mojo.ClientRequest({
          that : this,
          onSuccess : function(dto) {
            this.that._buildDialog(this.that.localize("dialogEditUserTitle"), dto);        
          },
          onFailure : function(ex) {
            this.that.handleException(ex);
          } 
        });        
        
        user.lock(callback);
      },

      
      _onNewUser : function(e) {        
        this._buildDialog(this.localize("dialogNewUserTitle"), new com.runwaysdk.geodashboard.GeodashboardUser());      
      },     
      
      _buildDialog : function(label, user) {
        var fac = this.getFactory();
        var table = this._table;
        var metadataDTO = table.getDataSource().getMetadataQueryDTO();
        
        var dialog = fac.newDialog(label, {minHeight:520, minWidth:730});        
        var form = this._makeNewOrEditForm(user, metadataDTO);
        dialog.appendContent(form);
        
        var that = this;  
        
        var submitCallback = function() {
          // Disable the buttons to prevent multiple submits
          document.getElementById('user-submit').disabled = true;
          document.getElementById('user-cancel').disabled = true;
          
          // Clear any existing error messages
          form.removeErrorMessages();
          
          var values = form.getValues();
          
          that._populateComponent(user, values);
          
          var applyCallback = new Mojo.ClientRequest({
            onSuccess : function() {
              dialog.close();                
              table.refresh();
            },
            onFailure : function(ex) {
              form.handleException(ex);
              
              document.getElementById('user-submit').disabled = false;
              document.getElementById('user-cancel').disabled = false;              
            }
          });
          
          user.apply(applyCallback);            
        };
        
        
        var cancelCallback = function() {
          dialog.close();
        };
        
        dialog.addButton(that.localize("submit"), submitCallback, null, {id:'user-submit', class:'btn btn-primary'});
        dialog.addButton(that.localize("cancel"), cancelCallback, null, {id:'user-cancel', class:'btn'});
        
        dialog.render();        
      },     
      
      _onDeleteUser : function(e) {
        var fac = this.getFactory();
        var table = this._table;
        
        var target = e.getTarget();
        var id = target.id;        
        var rowNumber = id.replace('table-delete-', '');
        
        var user = table.getDataSource().getResultsQueryDTO().getResultSet()[rowNumber];
        
        var dialog = fac.newDialog(this.localize("dialogDeleteUserTitle"));
        dialog.appendContent(this.localize("deleteText") + user.getUsername() + "?");
        
        var Structure = com.runwaysdk.structure;
        var tq = new Structure.TaskQueue();
        var that = this;
        
        tq.addTask(new Structure.TaskIF({
          start : function(){
            dialog.addButton(that.localize("delete"), function() { tq.next(); }, null, {class:'btn btn-primary'});
            
            var cancelCallback = function() {
              dialog.close();
              tq.stop();
            };

            dialog.addButton(that.localize("cancel"), cancelCallback, null, {class:'btn'});            
            dialog.render();
          }
        }));
        
        tq.addTask(new Structure.TaskIF({
          start : function(){
            dialog.close();
            
            var removeCallback = new com.runwaysdk.geodashboard.BlockingClientRequest({
              onSuccess : function() {
                table.refresh();
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);
              }
            });
            
            user.remove(removeCallback);
          }
        }));
        
        tq.start();
      },
      
      _makeButtons : function() {
        var newUser = this.getFactory().newButton(this.localize("newUser"), Mojo.Util.bind(this, this._onNewUser));
        newUser.setAttribute('class', 'btn btn-primary');
        
        this.appendChild(newUser);
      },
      
      _clickHandler : function(e) {        
        var target = e.getTarget();
        var klass = target.getAttribute('class');
        
        if(klass.contains('table-edit'))
        {
          e.preventDefault();
            
          this._onEditUser(e);
        }
        else if (klass.contains('table-delete'))
        {
          e.preventDefault();
          
          this._onDeleteUser(e);
        }      
      },
      
      render : function(parent) {
        this._makeButtons();
        
        // Build the columns from the read permissions
        var user = new com.runwaysdk.geodashboard.GeodashboardUser();
        var columns = [];        
        
        columns.push({header: this.localize("edit"), customFormatter: Mojo.Util.bind(this, function(result, rowNumber){
            return '<a href="#" class="ico-edit table-edit" id="table-edit-' + rowNumber + '">' + this.localize("edit") +'</a>';
          })
        });
        
        columns.push({header: this.localize("delete"), customFormatter: Mojo.Util.bind(this, function(result, rowNumber){
            return '<a href="#" class="ico-remove table-delete" id="table-delete-' + rowNumber + '">' + this.localize("delete") +'</a>';
          })
        });
        
        if(user.isUsernameReadable())
        {          
          columns.push({queryAttr: "username"});        
        }
          
        if(user.isFirstNameReadable())
        {
          columns.push({queryAttr: "firstName"});        
        }
          
        if(user.isLastNameReadable())
        {
          columns.push({queryAttr: "lastName"});        
        }
          
        if(user.isPhoneNumberReadable())
        {
          columns.push({queryAttr: "phoneNumber"});        
        }
        
        if(user.isEmailReadable())
        {
          columns.push({queryAttr: "email"});        
        }                   
                
        this._config.dataSource = new InstanceQueryDataSource({
          className: this._config.queryType,
          columns: columns
        });
        
        this._config.selectableRows = true;
        
        // Overwrite the column definitions for the edit and delete columns
        this._config.aoColumnDefs = [
          { "bSortable": false, "aTargets": [ 0 ], "sClass": "center", "sTitle":"" },
          { "bSortable": false, "aTargets": [ 1 ], "sClass": "center", "sTitle":"" }
        ];
        
        // Remove the search control from the table
        this._config.sDom = '<"top"i>rt<"bottom"lp><"clear">';
        
        this._table = new GenericDataTable(this._config);
        this._table.addEventListener('click', Mojo.Util.bind(this, this._clickHandler));
        this.appendChild(this._table);
        
        this.$render(parent);
        
      }      
    }
    
  });
  
  return usersTable;
  
})();
