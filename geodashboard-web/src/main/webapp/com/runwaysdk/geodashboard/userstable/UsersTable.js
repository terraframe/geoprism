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
    "dialogViewUserTitle" : "View User",
    "submit" : "Submit",
    "cancel" : "Cancel",
    "close" : "Close",
    "dialogNewUserTitle" : "New User",
    "dialogDeleteUserTitle" : "Confirm Delete",
    "delete" : "Delete",
    "edit" : "Edit",
    "deleteText" : "Are you sure you want to delete user ",
    "confirmPassword" : "Confirm password",
    "confirmRequired" : "Password confirmation is required",
    "passwordMismatch" : "Passwords do not match",
    "invalidEmail" : "Invalid email address format",
    "admin" : "Admin",
    "adminRoleHeader" : "System roles",
    "dashboardRoleHeader" : "Dashbaord roles",
    "accountInfo" : "Account information",
    "userInfo" : "User information",
    "allow" : "allow",
    "notAllowed" : "Not allowed to create a new user",
    "sSortAscending" : ": activate to sort column ascending",
    "sSortDescending" : ": activate to sort column descending",
    "sFirst" : "First",
    "sLast" : "Last",
    "sNext" : "Next",
    "sPrevious" : "Previous",
    "sEmptyTable" : "No data available in table",
    "sInfo" : "Showing _START_ to _END_ of _TOTAL_ entries",
    "sInfoEmpty" : "Showing 0 to 0 of 0 entries",
    "sInfoFiltered" : "(filtered from _MAX_ total entries)",
    "sLengthMenu" : "Show _MENU_ entries",
    "sLoadingRecords" : "Loading...",
    "sProcessing" : "Processing...",
    "sSearch" : "Search:",
    "sZeroRecords" : "No matching records found"
  });
  
  var usersTable = ClassFramework.newClass(usersTableName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(cfg) {
        
        cfg = cfg || {};
        cfg.queryType = cfg.queryType || defaultQueryType;
        this._config = cfg;        
        this._roleMd = new com.runwaysdk.system.Roles();
                
        this.$initialize("div");        
      },
      
      _makeNewOrEditForm : function(user, metadataDTO, adminRoles, dashboardRoles) {
        var form = new Form();
        form.addClassName('submit-form');
        
        // Build the admin role section
        form.appendElement(this._newHeader(this.localize('userInfo')));
        
        if(user.isFirstNameWritable())
        {
          var firstNameInput = FormEntry.newInput('text', 'firstName', {attributes:{type:'text', id:'firstName'}});
          firstNameInput.setValue(user ? user.getFirstName() : "");
          form.addFormEntry(metadataDTO.getAttributeDTO("firstName").getAttributeMdDTO(), firstNameInput);          
        }
        else if(user.isFirstNameReadable())
        {
          var label = metadataDTO.getAttributeDTO("firstName").getAttributeMdDTO().getDisplayLabel();        
          var entry = new com.runwaysdk.geodashboard.ReadEntry('firstName', label, user ? user.getFirstName() : "");
          form.addEntry(entry);                  
        }
        
        if(user.isLastNameWritable())
        {
          var lastNameInput = FormEntry.newInput('text', 'lastName', {attributes:{type:'text', id:'lastName'}});
          lastNameInput.setValue(user ? user.getLastName() : "");
          form.addFormEntry(metadataDTO.getAttributeDTO("lastName").getAttributeMdDTO(), lastNameInput);          
        }
        else if(user.isLastNameReadable())
        {
          var label = metadataDTO.getAttributeDTO("lastName").getAttributeMdDTO().getDisplayLabel();        
          var entry = new com.runwaysdk.geodashboard.ReadEntry('lastName', label, user ? user.getLastName() : "");
          form.addEntry(entry);                  
        }
        
        if(user.isPhoneNumberWritable())
        {
          var phoneNumberInput = FormEntry.newInput('text', 'phoneNumber', {attributes:{type:'text', id:'phoneNumber'}});
          phoneNumberInput.setValue(user ? user.getPhoneNumber() : "");
          form.addFormEntry(metadataDTO.getAttributeDTO("phoneNumber").getAttributeMdDTO(), phoneNumberInput);
        }
        else if(user.isPhoneNumberReadable())
        {
          var label = metadataDTO.getAttributeDTO("phoneNumber").getAttributeMdDTO().getDisplayLabel();        
          var entry = new com.runwaysdk.geodashboard.ReadEntry('phoneNumber', label, user ? user.getPhoneNumber() : "");
          form.addEntry(entry);                  
        }
        
        if(user.isEmailWritable())
        {
          var emailInput = FormEntry.newInput('text', 'email', {attributes:{type:'text', id:'email'}});
          emailInput.setValue(user ? user.getEmail() : "");
          form.addFormEntry(metadataDTO.getAttributeDTO("email").getAttributeMdDTO(), emailInput);
          
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
        else if(user.isEmailReadable())
        {
          var label = metadataDTO.getAttributeDTO("email").getAttributeMdDTO().getDisplayLabel();        
          var entry = new com.runwaysdk.geodashboard.ReadEntry('email', label, user ? user.getEmail() : "");
          form.addEntry(entry);                  
        }

        
        // Build the admin role section
        form.appendElement(this._newHeader(this.localize('accountInfo')));
                
        if(user.isUsernameWritable())
        {          
          var usernameInput = FormEntry.newInput('text', 'username', {attributes:{type:'text', id:'username'}});
          usernameInput.setValue(user ? user.getUsername() : "");
          
          form.addFormEntry(metadataDTO.getAttributeDTO("username").getAttributeMdDTO(), usernameInput);
        }
        else if(user.isUsernameReadable())
        {
          var label = metadataDTO.getAttributeDTO("username").getAttributeMdDTO().getDisplayLabel();        
          var entry = new com.runwaysdk.geodashboard.ReadEntry('username', label, user ? user.getUsername() : "");
          form.addEntry(entry);                  
        }
        
        if(user.isPasswordWritable())
        {         
          var passwordInput = FormEntry.newInput('text', 'password', {attributes:{type:'password', id:'password'}});
          form.addFormEntry(metadataDTO.getAttributeDTO("password").getAttributeMdDTO(), passwordInput);
          
          var confirmInput = FormEntry.newInput('text', 'confirm', {attributes:{type:'password', id:'confirm'}});
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
                
        // Check if the user has role permssions
        if(this._roleMd.isWritable())
        {
          // Build the admin role section
          form.appendElement(this._newHeader(this.localize('adminRoleHeader')));
        
          for (var i = 0; i < adminRoles.length; ++i) {
            var role = adminRoles[i];
            var options = [{displayLabel:this.localize('allow'), value:role.getRoleId(), checked:role.getAssigned()}]; 
          
            var entry = new com.runwaysdk.geodashboard.CheckboxFormEntry('role_' + role.getRoleId(), role.getDisplayLabel(), options);
            form.addEntry(entry);
          }  
        
          // Build the dashboard role section
          form.appendElement(this._newHeader(this.localize('dashboardRoleHeader')));

          for (var i = 0; i < dashboardRoles.length; ++i) {
            var role = dashboardRoles[i];
            var options = [{displayLabel:this.localize('allow'), value:role.getRoleId(), checked:role.getAssigned()}]; 
            
            var entry = new com.runwaysdk.geodashboard.CheckboxFormEntry('role_' + role.getRoleId(), role.getDisplayLabel(), options);
            form.addEntry(entry);
          }  
        }
                
        return form;
      },
      
      _newHeader : function(displayLabel) {
        // Build the admin role section
        var header = this.getFactory().newElement('h2');
        header.setInnerHTML(displayLabel);

        return header;
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
      
      _getRoles : function (values)
      {
        var roles = [];
        var keys = values.keySet();
    
        for (var i = 0; i < keys.length; i++)
        {
          if(keys[i].indexOf('role_') !== -1)
          {
            var value = values.get(keys[i]);
            
            if(value.length > 0)
            {
              roles.push(value[0]);
            }
          }
        }
        
        return roles;
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
        
        if(user.isWritable())
        {
          user.lock(callback);
        }
        else if(user.isReadable())
        {
          this._buildDialog(this.localize("dialogViewUserTitle"), user);        
        }
      },

      
      _onNewUser : function(e) {        
        var user = new com.runwaysdk.geodashboard.GeodashboardUser();
      
        if(user.isWritable())
        {
          this._buildDialog(this.localize("dialogNewUserTitle"), user);
        }
        else
        {
          var dialog = this.getFactory().newDialog(com.runwaysdk.Localize.get("rError", "Error"), {modal: true});
          dialog.appendContent(this.localize("notAllowed"));
          dialog.addButton(com.runwaysdk.Localize.get("rOk", "Ok"), function(){dialog.close();});
          dialog.render();
        }
      },     
      
      _buildDialog : function(label, user) {
      
        var Structure = com.runwaysdk.structure;
        var tq = new Structure.TaskQueue();
        var that = this;
        var adminRoles = [];
        var dashboardRoles = [];
          
        // First load the users admin role information
        tq.addTask(new Structure.TaskIF({
          start : function(){
          
            var callback = new Mojo.ClientRequest({
              onSuccess : function(roles) {
                for(var i = 0; i < roles.length; i++) {
                  adminRoles.push(roles[i]);                  
                }
                
                tq.next();
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);               
              }
            }); 
                  
            com.runwaysdk.geodashboard.RoleView.getAdminRoles(callback, user);
          }
        }));
        
        // Second load the users dashboard role information
        tq.addTask(new Structure.TaskIF({
          start : function(){
            
            var callback = new Mojo.ClientRequest({
              onSuccess : function(roles) {
                for(var i = 0; i < roles.length; i++) {
                  dashboardRoles.push(roles[i]);                  
                }
                  
                tq.next();
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);               
              }
            }); 
                    
            com.runwaysdk.geodashboard.RoleView.getDashboardRoles(callback, user);
          }
        }));
                     
        // Last build the dialog form
        tq.addTask(new Structure.TaskIF({
          start : function(){
            var fac = that.getFactory();
            var table = that._table;
            var metadataDTO = table.getDataSource().getMetadataQueryDTO();
        
            var dialog = fac.newDialog(label, {minHeight:520, minWidth:730});        
            var form = that._makeNewOrEditForm(user, metadataDTO, adminRoles, dashboardRoles);
            dialog.appendContent(form);
        
            if(user.isWritable())
            {
              var submitCallback = function() {
                // Disable the buttons to prevent multiple submits
                document.getElementById('user-submit').disabled = true;
                document.getElementById('user-cancel').disabled = true;
          
                // Clear any existing error messages
                form.removeErrorMessages();
          
                var values = form.getValues();
          
                var roles = that._getRoles(values);
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
          
                if(that._roleMd.isWritable()) {
                  user.applyWithRoles(applyCallback, roles);            
                }
                else {
                  user.apply(applyCallback);
                }
              };
        
        
              var cancelCallback = function() {
                if(!user.isNewInstance())
                {                
                  var unlockCallback = new Mojo.ClientRequest({
                    onSuccess : function() {
                      dialog.close();                
                    },
                    onFailure : function(ex) {
                      form.handleException(ex);
                    }
                  });
            
                  user.unlock(unlockCallback);  
                }
                else
                {
                  dialog.close();
                }
              };
        
              dialog.addButton(that.localize("submit"), submitCallback, null, {id:'user-submit', class:'btn btn-primary'});
              dialog.addButton(that.localize("cancel"), cancelCallback, null, {id:'user-cancel', class:'btn'});            
            }
            else if(user.isReadable())
            {
              dialog.addButton(that.localize("close"), function(){dialog.close()}, null, {id:'user-cancel', class:'btn btn-primary'});            
            }
        
            dialog.render();   
        
            if (jcf != null && jcf.customForms != null) {
              jcf.customForms.replaceAll();
            }
          }
        }));
        
        tq.start();
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
        
        if(klass.indexOf('table-edit') !== -1)
        {
          e.preventDefault();
            
          this._onEditUser(e);
        }
        else if (klass.indexOf('table-delete') !== -1)
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

        // Localize the datatable widget
        this._config.oLanguage = {
          oAria: {
            sSortAscending: this.localize("sSortAscending"),
            sSortDescending: this.localize("sSortDescending")
          },
          oPaginate: {
            sFirst: this.localize("sFirst"),
            sLast: this.localize("sLast"),
            sNext: this.localize("sNext"),
            sPrevious: this.localize("sPrevious")
          },
          sEmptyTable: this.localize("sEmptyTable"),
          sInfo: this.localize("sInfo"),
          sInfoEmpty: this.localize("sInfoEmpty"),
          sInfoFiltered: this.localize("sInfoFiltered"),
          sLengthMenu: this.localize("sLengthMenu"),
          sLoadingRecords: this.localize("sLoadingRecords"),
          sProcessing: this.localize("sProcessing"),
          sSearch: this.localize("sSearch"),
          sZeroRecords: this.localize("sZeroRecords")
        };        
        
        this._table = new GenericDataTable(this._config);
        this._table.addEventListener('click', Mojo.Util.bind(this, this._clickHandler));
        this.appendChild(this._table);
        
        this.$render(parent);
        
      }      
    }
    
  });
  
  return usersTable;
  
})();
