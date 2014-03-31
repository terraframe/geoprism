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
    noTypeSelected : "No type selected."
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
//                {label: "com.runwaysdk.system.Users", children: [{label: "com.runwaysdk.geodashboard.GeodashboardUser"}]},
//                {label: "com.runwaysdk.system.gis.geo.GeoEntity"},
//                {label: "com.runwaysdk.system.gis.geo.Universal"}
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
            readColumnsFromMetadata: true
          });
          
          this._table = new GenericDataTable(tableCfg);
          this._table.render(this._tableHolder);
        }
      },
      
      _getTreeData : function() {
        
      },
      
      _makeTree : function() {
        this._treeEl = this.getFactory().newElement("div");
        this._treeEl.addClassName("geodashboard-databrowser-tree");
        this.appendChild(this._treeEl);

        var that = this;
        
        var cr = new com.runwaysdk.geodashboard.StandbyClientRequest({onSuccess: function(metadataTypeArray){
          
          var idHashmap = {};
          
          // Loop over it once and put the objects we're creating into the idHashmap.
          for (var i = 0; i < metadataTypeArray.length; ++i) {
            var metadataType = metadataTypeArray[i];
            
            var node = {label: metadataType.getDisplayLabel(), id: metadataType.getTypeId(), type: metadataType.getTypeName(), children:[]};
            
            var parentId = metadataType.getParentId();
            if (parentId === "ROOT") {
              if (idHashmap[metadataType.getTypeId()] == null) {
                idHashmap[metadataType.getTypeId()] = node;
              }
            }
            else {
              var parentNode = idHashmap[metadataType.getParentId()];
              if (parentNode != null) {
                parentNode.children.push(node);
              }
              else {
                var didFind = false;
                
                // Dereference parents
                for (var j = 0; j < metadataTypeArray.length; ++j) {
                  var parentMdType = metadataTypeArray[j];
                  
                  if (parentMdType.getTypeId() === metadataType.getParentId()) {
                    var parentNode = idHashmap[parentMdType.getTypeId()];
                    if (parentNode == null) {
                      idHashmap[parentMdType.getTypeId()] = {label: parentMdType.getDisplayLabel(), id: parentMdType.getTypeId(), type: parentMdType.getTypeName(), children:[]};
                    }
                    
                    parentNode.children.push(node);
                    
                    didFind = true;
                  }
                }
                
                if (!didFind) {
                  throw new com.runwaysdk.Exception("The node (retrieved from the server) [" + node + "] contains a reference to a parent that does not exist.");
                }
              }
            }
          }
          
          that._config.data = [];
          // Loop over it again and transfer the data from the hashmap into an array so that we preserve ordering.
          for (var i = 0; i < metadataTypeArray.length; ++i) {
            if (metadataTypeArray[i].getParentId() === "ROOT") {
              that._config.data.push(idHashmap[metadataTypeArray[i].getTypeId()]);
            }
          }
          
          // Instantiate JQTree
          that._tree = $(that._treeEl.getRawEl()).tree(that._config);
          that._tree.bind(
            'tree.select',
            Mojo.Util.bind(that, that._onSelectTreeNode)
          );
          
        }, onFailure: Util.bind(that, that.handleException)}, that._treeEl);
        
        com.runwaysdk.geodashboard.databrowser.DataBrowserUtil.getTypes(cr);
      },
      
      _makeTableHolder : function() {
        this._tableHolder = this.getFactory().newElement("div", {innerHTML: this.localize("noTypeSelected")});
        this._tableHolder.addClassName("geodashboard-databrowser-table");
        this.appendChild(this._tableHolder);
      },
      
      render : function(parent) {
        this._makeTree();
        this._makeTableHolder();
        
        this.$render(parent);
      }
      
    }
    
  });
  
  return dataBrowser;
  
})();
