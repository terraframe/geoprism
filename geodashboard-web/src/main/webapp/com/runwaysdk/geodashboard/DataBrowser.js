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
      
      initialize : function(config) {
        
        config = config || {};
//        this.requireParameter("types", config.types, "array");
        
        var defaultConfig = {
          el: "div",
          data: [
                {label: "com.runwaysdk.system.Users"},
                {label: "com.runwaysdk.system.gis.geo.GeoEntity"},
                {label: "com.runwaysdk.system.gis.geo.Universal"},
                {label: "com.runwaysdk.geodashboard.GeodashboardUser"}
          ],
          dragAndDrop: true,
          selectable: true,
          crud: {
            create: {
              width: 650,
              height: 300
            },
            update: {
              width: 650,
              height: 300
            }
          }
        };
        this._config = Mojo.Util.deepMerge(defaultConfig, config);
        
        this.$initialize(this._config.el);
        
      },
      
//      _onSelectChange : function(event) {
//        var newType = this._select.getRawEl().options[this._select.getRawEl().selectedIndex].text;
//        
//        this._table.destroy();
//        
//        this._config.dataSource = new InstanceQueryDataSource({
//          className: newType,
//          readColumnsFromMetadata: true
//        });
//        
//        this._table = new GenericDataTable(this._config);
//        this._table.render(this);
//      },
//      
//      _makeButtons : function() {
//        this._select = this.getFactory().newElement("select");
//        var rawIn = this._select.getRawEl();
//        for (var i = 0; i < this._config.types.length; ++i) {
//          this._select.appendChild(this.getFactory().newElement("option", {innerHTML: this._config.types[i], value: this._config.types[i]}));
//        }
//        rawIn.selectedIndex = 0;
//        this._select.addEventListener("change", Mojo.Util.bind(this, this._onSelectChange));
//        this.appendChild(this._select);
//        
//        var newUser = this.getFactory().newButton(this.localize("newUser"), Mojo.Util.bind(this, this._onNewUser));
//        this.appendChild(newUser);
//        
//        var editUser = this.getFactory().newButton(this.localize("editUser"), Mojo.Util.bind(this, this._onEditUser));
//        this.appendChild(editUser);
//        
//        var deleteUser = this.getFactory().newButton(this.localize("deleteUser"), Mojo.Util.bind(this, this._onDeleteUser));
//        this.appendChild(deleteUser);
//      },
      
      _onClickTreeNode : function(event) {
        // The clicked node is 'event.node'
        var type = event.node.name;
        
        this._table.destroy();
        
        this._config.dataSource = new InstanceQueryDataSource({
          className: type,
          readColumnsFromMetadata: true
        });
        
        this._table = new GenericDataTable(this._config);
        this._table.render(this);
      },
      
      _makeTree : function() {
        this._treeEl = this.getFactory().newElement("div");
        this.appendChild(this._treeEl);
        this._tree = $(this._treeEl.getRawEl()).tree(this._config);
        
        this._tree.bind(
          'tree.click',
          Mojo.Util.bind(this, this._onClickTreeNode)
        );
      },
      
      render : function(parent) {
//        this._makeButtons();
        
        this._makeTree();
        
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
