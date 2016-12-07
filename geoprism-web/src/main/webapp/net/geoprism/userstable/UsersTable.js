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
//define(["../../ClassFramework", "../../Util", "../RunwaySDK_UI", "../factory/runway/widget/Widget", "../datatable/datasource/InstanceQueryDataSource"], function(ClassFramework, Util, UI, Widget, InstanceQueryDataSource) {
(function(){  

  var ClassFramework = Mojo.Meta;
  var Widget = com.runwaysdk.ui.factory.runway.Widget;
  var InstanceQueryDataSource = com.runwaysdk.ui.datatable.datasource.InstanceQueryDataSource;
  var GenericDataTable = com.runwaysdk.ui.factory.generic.datatable.DataTable;
  var FormEntry = net.geoprism.FormEntry;
  var Form = net.geoprism.Form;
  
  var defaultQueryType = "net.geoprism.GeoprismUser";
  
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
    "accountInfo" : "Account information",
    "userInfo" : "User information",
    "allow" : "allow",
    "notAllowed" : "Not allowed to create a new user",
  });
  
  var UserFormEvent = ClassFramework.newClass('com.runwaysdk.ui.userstable.UserFormBuilderEvent', {
    Constants : {
      APPLY_SUCCESS : 0,
      APPLY_FAILURE : 1,
      CANCEL_SUCCESS : 2,
      CANCEL_FAILURE : 3,
      ON_EDIT : 4
    },
    Instance : {
      initialize : function(eventType, user, container) {
        this._eventType = eventType;
        this._user = user;
        this._container = container;
      },
      
      getEventType : function(){
        return this._eventType;
      },
      
      getUser : function(){
    	return this._user;
      },
      
      getContainer : function(){
    	return this._container;
      }
    }
  });
  
  ClassFramework.newClass('com.runwaysdk.ui.userstable.UserFormBuilder', {      
      
    Instance : {
       
      initialize : function(factory, user, rolesMap) {
        this._factory = factory;
        this._user = user;
        this._roleMd = new com.runwaysdk.system.Roles();
        this._rolesMap = rolesMap;
        this._listeners = [];
        this._hasRoles = (this._roleMd.isWritable() && rolesMap != null);
      },
      
      addListener : function (listener)
      {
        this._listeners.push(listener);
      },
      
      getListeners : function()
      {
        return this._listeners;  
      },
      
      fireEvent : function(eventType, user, container)
      {
        for(var i = 0; i < this._listeners.length; i++) {
          this._listeners[i].handleEvent(new UserFormEvent(eventType, user, container));
        }  
      },
      
      getFactory : function()
      {
        return this._factory;  
      },
      
      localize : function (key)
      {
        return com.runwaysdk.Localize.localize(usersTableName, key);      
      },
      
      render : function(container, readOnly) {      
        var form = this._build(readOnly);
        var that = this;
      
        container.appendContent(form);
      
        if(this._user.isWritable())
        {
          if(!readOnly)
          {          
            var submitCallback = function() {
              // Disable the buttons to prevent multiple submits
              document.getElementById('user-submit').disabled = true;
              document.getElementById('user-cancel').disabled = true;
        
              // Clear any existing error messages
              form.removeErrorMessages();
        
              var values = form.getValues();
         
              var roles = that._getRoles(values);
              that._populateComponent(values);
        
              var applyCallback = new net.geoprism.StandbyClientRequest({
                onSuccess : function(user) {
                  container.close();
                
                  that.fireEvent(UserFormEvent.APPLY_SUCCESS, user, container);
                },
                onFailure : function(ex) {
                  form.handleException(ex);
            
                  document.getElementById('user-submit').disabled = false;
                  document.getElementById('user-cancel').disabled = false;
                 
                  that.fireEvent(UserFormEvent.APPLY_FAILURE, null, container);
                }
              }, container.getParentNode());

              if(that._hasRoles) {
                that._user.applyWithRoles(applyCallback, roles);            
              }
              else {
                that._user.apply(applyCallback);
              }
            };
      
            var cancelCallback = function() {
              if(!that._user.isNewInstance())
              {                
                var unlockCallback = new net.geoprism.StandbyClientRequest({
                  onSuccess : function(user) {
                    container.close();
                  
                    that.fireEvent(UserFormEvent.CANCEL_SUCCESS, user, container);
                  },
                  onFailure : function(ex) {
                    form.handleException(ex);
                  
                    that.fireEvent(UserFormEvent.CANCEL_FAILURE, null, container);
                  }
                }, container.getParentNode());
          
                that._user.unlock(unlockCallback);  
              }
              else
              {
                container.close();
              }
            };
      
            container.addButton(that.localize("submit"), submitCallback, null, {id:'user-submit', class:'btn btn-primary'});
            container.addButton(that.localize("cancel"), cancelCallback, null, {id:'user-cancel', class:'btn'});            
          }
          else
          {
            var editCallback = function() {
              that.fireEvent(UserFormEvent.ON_EDIT, that._user, container);            	
            };
          
            container.addButton(that.localize("edit"), editCallback, null, {id:'user-edit', class:'btn btn-primary'});
          }
        }
        else if(this._user.isReadable())
        {
          container.addButton(that.localize("close"), function(){container.close();}, null, {id:'user-cancel', class:'btn btn-primary'});            
        }
      },
      
      _build : function(readOnly) {
        var form = new Form();
          
        // Build the admin role section
        form.appendElement(this._newHeader(this.localize('userInfo')));
        
        if(this._user.getType() === 'net.geoprism.account.ExternalProfile') {
            if(this._user.isDisplayNameReadable())
            {
              var label = this._user.getDisplayNameMd().getDisplayLabel();        
              var entry = new net.geoprism.ReadEntry('displayName', label, this._user ? this._user.getDisplayName() : "");
              form.addEntry(entry);                  
            }        	
        }
        else {
            if(!readOnly && this._user.isFirstNameWritable())
            {
              var firstNameInput = FormEntry.newInput('text', 'firstName', {attributes:{type:'text', id:'firstName'}});
              firstNameInput.setValue(this._user ? this._user.getFirstName() : "");
              form.addFormEntry(this._user.getFirstNameMd(), firstNameInput);          
            }
            else if(this._user.isFirstNameReadable())
            {
              var label = this._user.getFirstNameMd().getDisplayLabel();        
              var entry = new net.geoprism.ReadEntry('firstName', label, this._user ? this._user.getFirstName() : "");
              form.addEntry(entry);                  
            }
              
            if(!readOnly && this._user.isLastNameWritable())
            {
              var lastNameInput = FormEntry.newInput('text', 'lastName', {attributes:{type:'text', id:'lastName'}});
              lastNameInput.setValue(this._user ? this._user.getLastName() : "");
              form.addFormEntry(this._user.getLastNameMd(), lastNameInput);          
            }
            else if(this._user.isLastNameReadable())
            {
              var label = this._user.getLastNameMd().getDisplayLabel();        
              var entry = new net.geoprism.ReadEntry('lastName', label, this._user ? this._user.getLastName() : "");
              form.addEntry(entry);                  
            }
              
            if(!readOnly && this._user.isPhoneNumberWritable())
            {
              var phoneNumberInput = FormEntry.newInput('text', 'phoneNumber', {attributes:{type:'text', id:'phoneNumber'}});
              phoneNumberInput.setValue(this._user ? this._user.getPhoneNumber() : "");
              form.addFormEntry(this._user.getPhoneNumberMd(), phoneNumberInput);
            }
            else if(this._user.isPhoneNumberReadable())
            {
              var label = this._user.getPhoneNumberMd().getDisplayLabel();        
              var entry = new net.geoprism.ReadEntry('phoneNumber', label, this._user ? this._user.getPhoneNumber() : "");
              form.addEntry(entry);                  
            }
              
            if(!readOnly && this._user.isEmailWritable())
            {
              var emailInput = FormEntry.newInput('text', 'email', {attributes:{type:'text', id:'email'}});
              emailInput.setValue(this._user ? this._user.getEmail() : "");
              form.addFormEntry(this._user.getEmailMd(), emailInput);
                
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
            else if(this._user.isEmailReadable())
            {
              var label = this._user.getEmailMd().getDisplayLabel();        
              var entry = new net.geoprism.ReadEntry('email', label, this._user ? this._user.getEmail() : "");
              form.addEntry(entry);                  
            }
              
            // Build the admin role section
            form.appendElement(this._newHeader(this.localize('accountInfo')));
                      
            if(!readOnly && this._user.isUsernameWritable())
            {
              var usernameInput = FormEntry.newInput('text', 'username', {attributes:{type:'text', id:'username'}});
              usernameInput.setValue(this._user ? this._user.getUsername() : "");
                
              form.addFormEntry(this._user.getUsernameMd(), usernameInput);
            }
            else if(this._user.isUsernameReadable())
            {
              var label = this._user.getUsernameMd().getDisplayLabel();        
              var entry = new net.geoprism.ReadEntry('username', label, this._user ? this._user.getUsername() : "");
              form.addEntry(entry);                  
            }
            
            if(!readOnly && this._user.isPasswordWritable())
            {         
              var passwordInput = FormEntry.newInput('text', 'password', {attributes:{type:'password', id:'password'}});
              form.addFormEntry(this._user.getPasswordMd(), passwordInput);
                
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
        }
                            
        // Check if the this._user has role permssions
        if(!readOnly && this._hasRoles)
        {
          this.buildRoles(form);	        	
        }
                  
        return form;
      },
      
      buildRoles : function(form) {
        for (var k in this._rolesMap)
        {
          if (this._rolesMap.hasOwnProperty(k))
          {
            var roles = this._rolesMap[k];
              
            // Build the admin role section
            form.appendElement(this._newHeader(this.localize(k)));
                
            for (var i = 0; i < roles.length; ++i) {
              var role = roles[i];
              var options = [{displayLabel:this.localize('allow'), value:role.getRoleId(), checked:role.getAssigned()}]; 
                  
              var entry = new net.geoprism.CheckboxFormEntry('role_' + role.getRoleId(), role.getDisplayLabel(), options);
              form.addEntry(entry);
            }
          }
        }    	  
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
                    
      _populateComponent : function (values) {
        if(this._user.isUsernameWritable())
        {          
          this._user.setUsername(values.get("username"));
        }
            
        if(this._user.isPasswordWritable())
        {       
          this._user.setPassword(values.get("password"));                            
        }
            
        if(this._user.isFirstNameWritable())
        {
          this._user.setFirstName(values.get("firstName"));                            
        }
            
        if(this._user.isLastNameWritable())
        {
          this._user.setLastName(values.get("lastName"));
        }
            
        if(this._user.isPhoneNumberWritable())
        {
          this._user.setPhoneNumber(values.get("phoneNumber"));
        }
          
        if(this._user.isEmailWritable())
        {
          this._user.setEmail(values.get("email"));
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
      }      
    }
  });
  
  ClassFramework.newClass('com.runwaysdk.ui.userstable.UserFormConfiguator', {
	  Instance : {
		get : function(factory, user, rolesMap) {
		  return new com.runwaysdk.ui.userstable.UserFormBuilder(factory, user, rolesMap);
        },
        
        getRoles : function(callback, user)
        {
          net.geoprism.RoleView.getRoles(callback, user);
        }
	  }	  
  });
  
  ClassFramework.newClass('com.runwaysdk.ui.userstable.UserForm', {
      
    Extends : Widget,
      
    Instance : {
        
      initialize : function(cfg) {
        cfg = cfg || {};
        this._config = cfg;        
        this._footer = null;
                  
        this.$initialize("div");
        
        this._configuator = (cfg.configuator || new com.runwaysdk.ui.userstable.UserFormConfiguator());
      },
      getFactory : function ()
      {
        return com.runwaysdk.ui.Manager.getFactory();
      },
      close : function ()
      {
        // Do nothing
      },
      addButton : function (label, callback, context, config)
      {
        var type = 'reset';
    
        if(this._footer === null) {
          this._footer = this.getFactory().newElement('footer');
          this._footer.setAttribute('class', 'bottom-bar');
          
          this._form.appendChild(this._footer);
          
          type = 'submit';          
        }
        
        var button = this.getFactory().newElement('input', {'value':label, 'type':type});
        button.setAttribute('class', config['class']);
        button.setAttribute('id', config['id']);
        button.addEventListener('click', function (e) {
          e.preventDefault();
          
          callback(e);
        });
          
        this._footer.appendChild(button);
      },
      appendContent : function(form) 
      {
        this._form = form;
        
        this.appendChild(form);
      },
      getParentNode : function()
      {
    	  return this.getRawEl();  
      },
      reload : function(user, readOnly)
      {
    	this.setInnerHTML('');
    	this._footer = null;
    	  
        var builder = this._configuator.get(this.getFactory(), user, null);
        builder.render(this, readOnly);
        builder.addListener(this);            	    	  
      },
      handleEvent : function(e) {
    	var that = this;
    	var user = e.getUser();
    	  
        if(e.getEventType() === UserFormEvent.ON_EDIT) {
          var lockCallback = new net.geoprism.StandbyClientRequest({
            onSuccess : function(user) {
              that.reload(user, false);
            },
            onFailure : function(ex) {
              that.handleException(ex);
            }
          }, e.getContainer().getRawEl());
              
          user.lock(lockCallback);
        }
        else if (e.getEventType() === UserFormEvent.APPLY_SUCCESS || e.getEventType() === UserFormEvent.CANCEL_SUCCESS) {
          this.reload(user, true);        	
        }
      },
      render : function(parent) {

        var Structure = com.runwaysdk.structure;
        var tq = new Structure.TaskQueue();
        var that = this;
        var user = null;
            
        // First load the user
        tq.addTask(new Structure.TaskIF({
          start : function(){
            
            var callback = new Mojo.ClientRequest({
              onSuccess : function(dto) {
                user = dto;
                tq.next();
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);               
              }
            }); 
                    
            net.geoprism.GeoprismUser.getCurrentUser(callback);
          }
        }));
          
        // Build the form
        tq.addTask(new Structure.TaskIF({
          start : function(){
            var builder = that._configuator.get(that.getFactory(), user, null);
            builder.render(that, true);
            builder.addListener(that);
              
            that.$render(parent);        

            if (jcf != null && jcf.customForms != null) {
              jcf.customForms.replaceAll();
            }        
          }
        }));
          
        tq.start();
      }
    }
  });
  
  var usersTable = ClassFramework.newClass(usersTableName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(cfg) {
        
        cfg = cfg || {};
        cfg.queryType = cfg.queryType || defaultQueryType;
        this._config = cfg;        
        this._roleMd = new com.runwaysdk.system.Roles();
        this._configuator = (cfg.configuator || new com.runwaysdk.ui.userstable.UserFormConfiguator());
                
        this.$initialize("div");        
      },
      
      _onEditUser : function(e) {
        var target = e.getTarget();
        var id = target.id;        
        var mainDiv = document.getElementById("usersTable");
        var rowNumber = id.replace('table-edit-', '');
          
        var user = this._table.getDataSource().getResultsQueryDTO().getResultSet()[rowNumber];
        
        var callback = new net.geoprism.StandbyClientRequest({
          that : this,
          onSuccess : function(dto) {
            this.that._buildDialog(this.that.localize("dialogEditUserTitle"), dto);        
          },
          onFailure : function(ex) {
            this.that.handleException(ex);
          } 
        }, mainDiv);        
        
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
        var user = new net.geoprism.GeoprismUser();
      
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
    	  
      	var id = "dialog-" + (user.isNewInstance() ? 'new' : user.getId());
    	var element = document.getElementById(id);
                    
    	if(element == null)
        {      
          var Structure = com.runwaysdk.structure;
          var tq = new Structure.TaskQueue();
          var that = this;
          var rolesMap = {};
          
          // First load the users admin role information
          tq.addTask(new Structure.TaskIF({
            start : function(){
          
              var callback = new Mojo.ClientRequest({
                onSuccess : function(roles) {
                  for(var i = 0; i < roles.length; i++)
                  {
                    var groupName = roles[i].getGroupName();
                  
                    if(rolesMap[groupName] == null)
                    {
                      rolesMap[groupName] = [];
                    }
                  
                    rolesMap[groupName].push(roles[i]);                  
                  }
                
                  tq.next();
                },
                onFailure : function(ex) {
                  tq.stop();
                  that.handleException(ex);               
                }
              }); 

              that._configuator.getRoles(callback, user);
            }
          }));
                     
          // Last build the dialog form
          tq.addTask(new Structure.TaskIF({
            start : function(){
              var fac = that.getFactory();
              var table = that._table;
        
              var dialog = fac.newDialog(label, {minHeight:250, maxHeight:$(window).height() - 75, minWidth:730, resizable: false});   
              dialog.setId(id);
            
              var builder = that._configuator.get(fac, user, rolesMap);
              builder.addListener({
                handleEvent : function(event) {
                  if(event.getEventType() === UserFormEvent.APPLY_SUCCESS) {
                    table.refresh();
                  }
                }              
              });
              builder.render(dialog, false);
        
              dialog.render();   
        
              if (jcf != null && jcf.customForms != null) {
                jcf.customForms.replaceAll();
              }
            }
          }));
        
          tq.start();
        }
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
            var mainDiv = document.getElementById("usersTable");
            
            var removeCallback = new net.geoprism.StandbyClientRequest({
              onSuccess : function() {
                table.refresh();
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);
              }
            }, mainDiv);
            
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
        var user = new net.geoprism.GeoprismUser();
        var columns = [];        

        columns.push({header: this.localize("delete"), customFormatter: Mojo.Util.bind(this, function(result, rowNumber){
            return '<a href="#" class="fa fa-times ico-remove table-delete" id="table-delete-' + rowNumber + '"></a>';
          })
        });
        
        columns.push({header: this.localize("edit"), customFormatter: Mojo.Util.bind(this, function(result, rowNumber){
            return '<a href="#" class="fa fa-pencil ico-edit table-edit" id="table-edit-' + rowNumber + '"></a>';
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
        
        this._config.selectableRows = false;
        
        // Overwrite the column definitions for the edit and delete columns
        this._config.aoColumnDefs = [
          { "bSortable": false, "aTargets": [ 0 ], "sClass": "center", "sTitle":"", "sWidth":"25px" },
          { "bSortable": false, "aTargets": [ 1 ], "sClass": "center", "sTitle":"", "sWidth":"25px" }
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
