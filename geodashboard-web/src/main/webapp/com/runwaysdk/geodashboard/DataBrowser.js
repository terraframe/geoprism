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
  
  var dataBrowserName = "com.runwaysdk.geodashboard.DataBrowser";
  
  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage(dataBrowserName, {
    
  });
  
  var dataBrowser = ClassFramework.newClass(dataBrowserName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(cfg) {
        
        cfg = cfg || {};
        this.requireParameter("types", cfg.types, "array");
        this._config = cfg;
        
        this.$initialize("div");
        
      },
      
      _makeNewOrEditForm : function(usersDTO, metadataDTO) {
        var form = this.getFactory().newForm();
        
        var usernameInput = form.newInput('text', 'username');
        usernameInput.setValue(usersDTO ? usersDTO.getUsername() : "");
        form.addEntry(metadataDTO.getAttributeDTO("username").getAttributeMdDTO().getDisplayLabel(), usernameInput);
        
        var passwordInput = form.newInput('text', 'password');
        passwordInput.setValue(usersDTO ? usersDTO.getPassword() : "");
        form.addEntry(metadataDTO.getAttributeDTO("password").getAttributeMdDTO().getDisplayLabel(), passwordInput);
        
        var localeInput = form.newInput('select', 'locale');
        localeInput.addOption("locale1", "locale1", true);
        localeInput.addOption("locale2", "locale2", false);
        localeInput.addOption("locale3", "locale3", false);
        form.addEntry(metadataDTO.getAttributeDTO("locale").getAttributeMdDTO().getDisplayLabel(), localeInput);
        
        var inactiveInput = form.newInput('text', 'inactive');
        inactiveInput.setValue(usersDTO ? usersDTO.getInactive().toString() : "");
        form.addEntry(metadataDTO.getAttributeDTO("inactive").getAttributeMdDTO().getDisplayLabel(), inactiveInput);
        
        var sessionLimitInput = form.newInput('text', 'sessionLimit');
        sessionLimitInput.setValue(usersDTO ? usersDTO.getSessionLimit() : "");
        form.addEntry(metadataDTO.getAttributeDTO("sessionLimit").getAttributeMdDTO().getDisplayLabel(), sessionLimitInput);
        
        return form;
      },
      
      _onClickEdit : function(mouseEvent) {
        
      },
      
      _onClickNew : function(mouseEvent) {
        
      },
      
      _onClickDelete : function(mouseEvent) {
      },
      
      _onSelectChange : function(event) {
        var newType = this._select.getRawEl().options[this._select.getRawEl().selectedIndex].text;
        
        this._table.destroy();
        
        this._config.dataSource = new InstanceQueryDataSource({
          className: newType,
          readColumnsFromMetadata: true
        });
        
        this._table = new GenericDataTable(this._config);
        this._table.render(this);
      },
      
      _makeButtons : function() {
        this._select = this.getFactory().newElement("select");
        var rawIn = this._select.getRawEl();
        for (var i = 0; i < this._config.types.length; ++i) {
          this._select.appendChild(this.getFactory().newElement("option", {innerHTML: this._config.types[i], value: this._config.types[i]}));
        }
        rawIn.selectedIndex = 0;
        this._select.addEventListener("change", Mojo.Util.bind(this, this._onSelectChange));
        this.appendChild(this._select);
        
//        var newUser = this.getFactory().newButton(this.localize("newUser"), Mojo.Util.bind(this, this._onNewUser));
//        this.appendChild(newUser);
//        
//        var editUser = this.getFactory().newButton(this.localize("editUser"), Mojo.Util.bind(this, this._onEditUser));
//        this.appendChild(editUser);
//        
//        var deleteUser = this.getFactory().newButton(this.localize("deleteUser"), Mojo.Util.bind(this, this._onDeleteUser));
//        this.appendChild(deleteUser);
      },
      
      render : function(parent) {
        this._makeButtons();
        
        this._config.dataSource = new InstanceQueryDataSource({
          className: this._config.types[0],
          readColumnsFromMetadata: true
        });
        
        this._config.selectableRows = false;
        
        this._table = new GenericDataTable(this._config);
        this.appendChild(this._table);
        
        this.$render(parent);
      }
      
    }
    
  });
  
  return dataBrowser;
  
})();
