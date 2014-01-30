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

  var Util = Mojo.Util;
  var ClassFramework = Mojo.Meta;
  var Widget = com.runwaysdk.ui.factory.runway.Widget;
  var InstanceQueryDataSource = com.runwaysdk.ui.datatable.datasource.InstanceQueryDataSource;
  var GenericDataTable = com.runwaysdk.ui.factory.generic.datatable.DataTable;
  
  var queryType = "com.runwaysdk.system.Users";
  
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
    "deleteText" : "Are you sure you want to delete user "
  });
  
  var usersTable = ClassFramework.newClass(usersTableName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(cfg) {
        
        cfg = cfg || {};
        this._config = cfg;
        
        this.$initialize("div");
        
      },
      
      _makeNewOrEditForm : function(usersDTO, metadataDTO) {
        var form = this.getFactory().newForm();
        
        var usernameInput = this.getFactory().newFormControl('text', 'username');
        usernameInput.setValue(usersDTO ? usersDTO.getUsername() : "");
        form.addEntry(metadataDTO.getAttributeDTO("username").getAttributeMdDTO().getDisplayLabel(), usernameInput);
        
        var passwordInput = this.getFactory().newFormControl('text', 'password');
        passwordInput.setValue(usersDTO ? usersDTO.getPassword() : "");
        form.addEntry(metadataDTO.getAttributeDTO("password").getAttributeMdDTO().getDisplayLabel(), passwordInput);
        
//        var localeInput = this.getFactory().newFormControl('select', 'locale');
//        localeInput.addOption("locale1", "locale1", true);
//        localeInput.addOption("locale2", "locale2", false);
//        localeInput.addOption("locale3", "locale3", false);
//        form.addEntry(metadataDTO.getAttributeDTO("locale").getAttributeMdDTO().getDisplayLabel(), localeInput);
        
        var inactiveInput = this.getFactory().newFormControl('text', 'inactive');
        inactiveInput.setValue(usersDTO ? usersDTO.getInactive().toString() : "");
        form.addEntry(metadataDTO.getAttributeDTO("inactive").getAttributeMdDTO().getDisplayLabel(), inactiveInput);
        
        var sessionLimitInput = this.getFactory().newFormControl('text', 'sessionLimit');
        sessionLimitInput.setValue(usersDTO ? usersDTO.getSessionLimit() : "");
        form.addEntry(metadataDTO.getAttributeDTO("sessionLimit").getAttributeMdDTO().getDisplayLabel(), sessionLimitInput);
        
        return form;
      },
      
      _onEditUser : function(mouseEvent) {
        var fac = this.getFactory();
        var table = this._table;
        var row = this._table.getSelectedRow();
        var usersDTO = table.getDataSource().getResultsQueryDTO().getResultSet()[row.getRowNumber()];
        var metadataDTO = table.getDataSource().getMetadataQueryDTO();
        
        var dialog = fac.newDialog(this.localize("dialogEditUserTitle"));
        var form = this._makeNewOrEditForm(usersDTO, metadataDTO);
        dialog.appendContent(form);
        
        var Structure = com.runwaysdk.structure;
        var tq = new Structure.TaskQueue();
        var that = this;
        
        tq.addTask(new Structure.TaskIF({
          start : function(){
            dialog.addButton(that.localize("submit"), function() { tq.next(); });
            
            var cancelCallback = function() {
              dialog.close();
              tq.stop();
            };
            dialog.addButton(that.localize("cancel"), cancelCallback);
            
            dialog.render();
          }
        }));
        
        tq.addTask(new Structure.TaskIF({
          start : function(){
            var values = form.accept(fac.newFormControl('FormVisitor'));
            dialog.close();
            
            usersDTO.setUsername(values.get("username"));
            usersDTO.setPassword(values.get("password"));
//              usersDTO.setLocale()
            usersDTO.setInactive(values.get("inactive"));
            usersDTO.setSessionLimit(values.get("sessionLimit"));
            
            var lockCallback = new Mojo.ClientRequest({
              onSuccess : function() {
                tq.next();
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);
              }
            });
            
            usersDTO.lock(lockCallback);
          }
        }));
        
        tq.addTask(new Structure.TaskIF({
          start : function(){
            var applyCallback = new Mojo.ClientRequest({
              onSuccess : function() {
                table.refresh();
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);
              }
            });
            
            usersDTO.apply(applyCallback);
          }
        }));
        
        tq.start();
      },
      
      _onNewUser : function(mouseEvent) {
        var fac = this.getFactory();
        var table = this._table;
        var metadataDTO = table.getDataSource().getMetadataQueryDTO();
        
        var dialog = fac.newDialog(this.localize("dialogNewUserTitle"));
        var form = this._makeNewOrEditForm(null, metadataDTO);
        dialog.appendContent(form);
        
        var Structure = com.runwaysdk.structure;
        var tq = new Structure.TaskQueue();
        var that = this;
        
        var usersDTO = null;
        
        tq.addTask(new Structure.TaskIF({
          start : function(){
            dialog.addButton(that.localize("submit"), function() { tq.next(); });
            
            var cancelCallback = function() {
              dialog.close();
              tq.stop();
            };
            dialog.addButton(that.localize("cancel"), cancelCallback);
            
            dialog.render();
          }
        }));
        
        tq.addTask(new Structure.TaskIF({
          start : function(){
            var values = form.accept(fac.newFormControl('FormVisitor'));
            dialog.close();
            
            usersDTO = eval("new " + queryType + "();");
            usersDTO.setUsername(values.get("username"));
            usersDTO.setPassword(values.get("password"));
//              usersDTO.setLocale()
            usersDTO.setInactive(values.get("inactive"));
            usersDTO.setSessionLimit(values.get("sessionLimit"));
            
            var applyCallback = new Mojo.ClientRequest({
              onSuccess : function() {
                table.refresh();
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);
              }
            });
            
            usersDTO.apply(applyCallback);
          }
        }));
        
        tq.start();
      },
      
      _onDeleteUser : function(mouseEvent) {
        var fac = this.getFactory();
        var table = this._table;
        var row = this._table.getSelectedRow();
        var usersDTO = table.getDataSource().getResultsQueryDTO().getResultSet()[row.getRowNumber()];
        var metadataDTO = table.getDataSource().getMetadataQueryDTO();
        
        var dialog = fac.newDialog(this.localize("dialogDeleteUserTitle"));
        dialog.appendContent(this.localize("deleteText") + usersDTO.getUsername() + "?");
        
        var Structure = com.runwaysdk.structure;
        var tq = new Structure.TaskQueue();
        var that = this;
        
        tq.addTask(new Structure.TaskIF({
          start : function(){
            dialog.addButton(that.localize("delete"), function() { tq.next(); });
            
            var cancelCallback = function() {
              dialog.close();
              tq.stop();
            };
            dialog.addButton(that.localize("cancel"), cancelCallback);
            
            dialog.render();
          }
        }));
        
        tq.addTask(new Structure.TaskIF({
          start : function(){
            dialog.close();
            
            var removeCallback = new Mojo.ClientRequest({
              onSuccess : function() {
                table.refresh();
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);
              }
            });
            
            usersDTO.remove(removeCallback);
          }
        }));
        
        tq.start();
      },
      
      _localeFormatter : function(userDTO) {
        return String(userDTO.getLocale());
      },
      
      _makeButtons : function() {
        var newUser = this.getFactory().newButton(this.localize("newUser"), Mojo.Util.bind(this, this._onNewUser));
        this.appendChild(newUser);
        
        var editUser = this.getFactory().newButton(this.localize("editUser"), Mojo.Util.bind(this, this._onEditUser));
        this.appendChild(editUser);
        
        var deleteUser = this.getFactory().newButton(this.localize("deleteUser"), Mojo.Util.bind(this, this._onDeleteUser));
        this.appendChild(deleteUser);
      },
      
      render : function(parent) {
        this._makeButtons();
        
        this._config.dataSource = new InstanceQueryDataSource({
          className: queryType,
          columns: [
            {queryAttr: "username"},
            {queryAttr: "sessionLimit"},
            {queryAttr: "inactive"},
            {queryAttr: "locale", customFormatter: this._localeFormatter}
          ]
        });
        
        this._config.selectableRows = true;
        
        this._table = new GenericDataTable(this._config);
        this.appendChild(this._table);
        
        // TODO : Polling
        
        this.$render(parent);
      }
      
    }
    
  });
  
  return usersTable;
  
})();
