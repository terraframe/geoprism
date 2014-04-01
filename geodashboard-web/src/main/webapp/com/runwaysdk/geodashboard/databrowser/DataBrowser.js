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
  
  var dataBrowserName = "com.runwaysdk.geodashboard.databrowser.DataBrowser";
  
  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage(dataBrowserName, {
    tableTitle : "Records",
    treeTitle : "System Types",
    noTypeSelected : "No type selected."
  });
  
  var dataBrowser = ClassFramework.newClass(dataBrowserName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(config) {
        
        config = config || {};
        this.requireParameter("types", config.types, "array");
        
        var defaultConfig = {
          el: "div",
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
        
        this.addClassName("geodashboard-databrowser");
        
      },
      
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
      
      _onSelectTreeNode : function(event) {
        var selectedNode = event.node;
        
        if (this._table != null) {
          this._table.destroy();
          this._table = null;
        }
        
        if (selectedNode == null) {
          this._tableHolder.setInnerHTML(this.localize("noTypeSelected"));
        }
        else {
          var type = selectedNode.type;
          var tableCfg = Mojo.Util.clone(this._config);
          var tableEl = this.getFactory().newElement("table");
          this._tableHolder.setInnerHTML("");
          
          tableCfg.el = tableEl;
          tableCfg.dataSource = new InstanceQueryDataSource({
            className: type,
            readColumnsFromMetadata: true,
            columns: [
//                      { queryAttr : "locale", customFormatter :  }
                      ]
          });
          
          this._table = new GenericDataTable(tableCfg);
          this._table.render(this._tableHolder);
        }
      },
      
      __pushTypeAndAllChildren : function(type, typeArray, data) {
        // 1) Push the type
        var node = {label: type.getDisplayLabel(), id: type.getTypeId(), type: type.getTypeName(), children:[]};
        data.push(node);
        
        // 2) Loop through the typeArray, searching for children and pushing them under this node.
        for (var i = 0; i < typeArray.length; ++i) {
          var childType = typeArray[i];
          
          if (childType.getParentTypeId() === type.getTypeId()) {
            this.__pushTypeAndAllChildren(childType, typeArray, node.children);
          }
        }
      },
      
      _makeTree : function() {
        this._treeSection = this.getFactory().newElement("div");
        this._treeSection.addClassName("geodashboard-databrowser-treesection");
        this.appendChild(this._treeSection);
        
        this._treeTitle = this.getFactory().newElement("span", {innerHTML : this.localize("treeTitle")});
        this._treeTitle.addClassName("geodashboard-databrowser-treetitle");
        this._treeSection.appendChild(this._treeTitle);
        
        this._treeEl = this.getFactory().newElement("div");
        this._treeEl.addClassName("geodashboard-databrowser-tree");
        this._treeSection.appendChild(this._treeEl);

        var that = this;
        
        var metadataTypeArray = this._config.types;
        
        that._config.data = [];
        for (var i = 0; i < metadataTypeArray.length; ++i) {
          var type = metadataTypeArray[i];
          
          if (type.getParentTypeId() === "ROOT") {
            that.__pushTypeAndAllChildren(type, metadataTypeArray, that._config.data);
          }
        }
        
        // Instantiate JQTree
        that._tree = $(that._treeEl.getRawEl()).tree(that._config);
        that._tree.bind(
          'tree.select',
          Mojo.Util.bind(that, that._onSelectTreeNode)
        );
      },
      
      _makeTableSection : function() {
        this._tableSection = this.getFactory().newElement("div");
        this._tableSection.addClassName("geodashboard-databrowser-tablesection");
        this.appendChild(this._tableSection);
        
        this._tableTitle = this.getFactory().newElement("span", {innerHTML: this.localize("tableTitle")});
        this._tableTitle.addClassName("geodashboard-databrowser-tabletitle");
        this._tableSection.appendChild(this._tableTitle);
        
        this._tableHolder = this.getFactory().newElement("div", {innerHTML: this.localize("noTypeSelected")});
        this._tableHolder.addClassName("geodashboard-databrowser-table");
        this._tableSection.appendChild(this._tableHolder);
      },
      
      render : function(parent) {
        this._makeTree();
        this._makeTableSection();
        
        this.$render(parent);
      }
      
    }
    
  });
  
  return dataBrowser;
  
})();
