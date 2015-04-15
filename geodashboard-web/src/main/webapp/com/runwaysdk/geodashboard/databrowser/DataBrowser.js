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
        
        this._pushedTypes = {};
        
      },
      
      _formatNumber : function(value) {
        if(this._numberFormatter == null) {
          this._numberFormatter = Globalize.numberFormatter();
        }
        
        if(value != null) {
          return this._numberFormatter(value);          
        }
        
        return null;
      },
      
      _formatDate : function(value) {
        
        if(this._dateFormatter == null) {
          this._dateFormatter = Globalize.dateFormatter({ date: "short" });                         
        }
        
        if(value != null) {
          return this._dateFormatter(value);          
        }
        
        return null;
      },
      
      _formatDateTime : function(value) {
        
        if(this._dateTimeFormatter == null) {
          this._dateTimeFormatter = Globalize.dateFormatter({ dateTime: "short" });                         
        }
        
        if(value != null) {        
          return this._dateTimeFormatter(value);
        }
        
        return null;          
      },
      
      _formatTime : function(value) {
        
        if(this._timeFormatter == null) {
          this._timeFormatter = Globalize.dateFormatter({ time: "short" });                         
        }
        
        if(value != null) {                
          return this._timeFormatter(value);
        }
      
        return null;
      },
      
      _onSelectTreeNode : function(event) {
        var selectedNode = event.node;
        
        if (this._table != null) {
          this._table.destroy();
          this._table = null;
        }
        
        if (selectedNode == null) {
          this._tableHolder.setInnerHTML(this.localize("noTypeSelected"));
          this._tableHolder.setStyle("text-align", "center");
        }
        else {
          var type = selectedNode.type;
          var tableCfg = Mojo.Util.clone(this._config);
          var tableEl = this.getFactory().newElement("table");
          this._tableHolder.setInnerHTML("");
          this._tableHolder.setStyle("text-align", "left");
          
          tableCfg.el = tableEl;
          tableCfg.dataSource = new com.runwaysdk.geodashboard.databrowser.DataBrowserQueryDataSource({
            className: type,
            readColumnsFromMetadata: true,
            formatters:{
              "com.runwaysdk.system.metadata.MdAttributeDate": Mojo.Util.bind(this, this._formatDate),
              "com.runwaysdk.system.metadata.MdAttributeDateTime": Mojo.Util.bind(this, this._formatDateTime),
              "com.runwaysdk.system.metadata.MdAttributeTime": Mojo.Util.bind(this, this._formatTime),
              "com.runwaysdk.system.metadata.MdAttributeFloat": Mojo.Util.bind(this, this._formatNumber),
              "com.runwaysdk.system.metadata.MdAttributeDouble": Mojo.Util.bind(this, this._formatNumber),
              "com.runwaysdk.system.metadata.MdAttributeDecimal": Mojo.Util.bind(this, this._formatNumber),
            },
            columns: []
          });
          
          // This config removes the search box.
          tableCfg.sDom = '<"top"i>rt<"bottom"lp><"clear">';
          
          this._table = new GenericDataTable(tableCfg);
          this._table.render(this._tableHolder);
        }
      },
      
      __pushTypeAndAllChildren : function(type, typeArray, data) {
        // 1) Push the type
        var node = {label: type.getDisplayLabel(), id: type.getTypeId(), type: type.getTypePackage() + "." + type.getTypeName(), children:[]};
        data.push(node);
        this._pushedTypes[type.getTypeId()] = node;
        
        // 2) Loop through the typeArray, searching for children and pushing them under this node.
        for (var i = 0; i < typeArray.length; ++i) {
          var childType = typeArray[i];
          
          if (childType.getParentTypeId() === type.getTypeId()) {
            this.__pushTypeAndAllChildren(childType, typeArray, node.children);
          }
        }
      },
      
      __recursiveSort : function(dataArray) {
        dataArray.sort(function(a,b){
          return a.label.localeCompare(b.label);
        });
        
        for (var i = 0; i < dataArray.length; ++i) {
          if (Mojo.Util.isArray(dataArray[i].children)) {
            this.__recursiveSort(dataArray[i].children);
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
        
        // Build an array of all the root types
        var rootTypes = [];
        for (var i = 0; i < metadataTypeArray.length; ++i) {
          var type = metadataTypeArray[i];
          
          var isRootType = true;
          for (var j = 0; j < metadataTypeArray.length; ++j) {
            var parentType = metadataTypeArray[j];
            
            if (type.getParentTypeId() === parentType.getTypeId()) {
              isRootType = false;
            }
          }
          
          if (isRootType) {
            rootTypes.push(type);
          }
        }
        
        // Push the root types, and all their children into the data array
        that._config.data = [];
        for (var i = 0; i < rootTypes.length; ++i) {
          that.__pushTypeAndAllChildren(rootTypes[i], metadataTypeArray, that._config.data);
        }
        
        // Sort it, baby
        that.__recursiveSort(that._config.data);
        
        // Instantiate JQTree with our data array
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
        this._tableHolder.setStyle("text-align", "center");
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
