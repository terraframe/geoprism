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

  var Util = Mojo.Util;
  var ClassFramework = Mojo.Meta;
  var Widget = com.runwaysdk.ui.factory.runway.Widget;
  var GenericDataTable = com.runwaysdk.ui.factory.generic.datatable.DataTable;
  
  var dataBrowserName = "net.geoprism.data.browser.DataBrowser";
  
  var Form = ClassFramework.newClass('net.geoprism.IFrameResponseFormDialogDownloader', {
    Extends : com.runwaysdk.ui.RunwayControllerFormDialogDownloader,
    Instance : {
        
      initialize : function(config, iframeId, contentId) {
        this.$initialize(config);
        
        this._iframeId = iframeId;
        this._contentId = contentId;
      },
      _onClickSubmit : function() {
        // Reset the result HTML
        $('#' + this._iframeId).contents().find("body").html('');
        
        this.$_onClickSubmit();                  
      },
      _onActionSuccess : function(type) {
        var result = $('#' + this._iframeId).contents().find("#" + this._contentId);
        var message = result.html();
        
        if(result.hasClass('error')) {
          this.$_onFailure(message);          
        }
        else {
          this.$_onActionSuccess(type);
        }
      },      
      
    }
  });
  
  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage(dataBrowserName, {
    'tableTitle' : "Records",
    'treeTitle' : "System Types",
    'noTypeSelected' : "No type selected.",
    'export' : "Template",
    'import' : "Import",
    'message' : 'Message',
    'success' : 'Upload complete',
    'dialogDeleteDataTitle' : 'Delete current data?',
    'deleteData' : 'Are you sure you want to delete all of the selected data?',
    'delete' : 'Delete',
    'cancel' : 'Cancel'
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
      
      _onContextTreeNode : function(event) {
        var node = event.node;          
        var type = node.type;
        
        var items = [];
        items.push({label:this.localize("export"), id:"export", handler:Mojo.Util.bind(this, this._onContextExportClick, type)});
        items.push({label:this.localize("import"), id:"import", handler:Mojo.Util.bind(this, this._onContextImportClick, type)});
        
        if(this._config.editData) {
          items.push({label:this.localize("delete"), id:"import", handler:Mojo.Util.bind(this, this._onContextDeleteClick, type)});          
        }
        
        var cm = this.getFactory().newContextMenu(node);
        
        for(var i = 0; i < items.length; i++) {
          var item = items[i];
          
          var menuItem = cm.addItem(item.label, item.id, item.handler);
          
          if (item.enabled === false) {
            menuItem.setEnabled(false);
          }
        }
        
        cm.render();
      },
      
      _onContextExportClick : function(type) {
        var that = this;
          
        var title = this.localize("export")
          
        var config = {
          type: 'net.geoprism.data.importer.Excel',
          title: title,
          viewParams: {},
          action: "exportExcelFile",
          viewAction: "exportExcelForm",
          actionParams: {type: type},
          width: 560,
          onSuccess : function(responseObj) {
          },
          onFailure : function(e) {
            that.handleException(e);
          }
        };
          
        new com.runwaysdk.ui.RunwayControllerFormDialogDownloader(config).render();               
      },      
      _onContextImportClick : function(type) {
        var that = this;
        
        var title = this.localize("import")
        
        var config = {
          type: 'net.geoprism.data.importer.Excel',
          title: title,
          viewParams: {},
          action: "importExcelFile",
          viewAction: "excelImportForm",
          actionParams: {},
          width: 560,
          onSuccess : function() {
            that._table.refresh();            
          },
          onFailure : function(e) {
            that.handleException(e);            
          }
        };
        
        new Form(config, 'result_iframe', 'upload_result').render();  
      },
      _onContextDeleteClick : function(type) {
        var that = this;
        
        var fac = this.getFactory();
        
        var dialog = fac.newDialog(this.localize("dialogDeleteDataTitle"));
        dialog.appendContent(this.localize("deleteData"));
        
        var Structure = com.runwaysdk.structure;
        var tq = new Structure.TaskQueue();
        var that = this;
        
        tq.addTask(new Structure.TaskIF({
          start : function(){            
            var cancelCallback = function() {
              dialog.close();
              tq.stop();
            };

            dialog.addButton(that.localize("delete"), function() { tq.next(); }, null, {class:'btn btn-primary'});
            dialog.addButton(that.localize("cancel"), cancelCallback, null, {class:'btn'});            
            dialog.render();
          }
        }));
        
        tq.addTask(new Structure.TaskIF({
          start : function(){
            dialog.close();
            
            var request = new net.geoprism.StandbyClientRequest({
              onSuccess : function() {
                that._table.refresh();
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);
              }
            }, '#databrowser');
            
            net.geoprism.data.browser.DataBrowserUtil.deleteData(request, type)
          }
        }));
        
        tq.start();
      },
      handleException : function(e) {
        if($.type( e ) === "string") {
          this.handleErrorMessage(e);
        }
        else {
          this.handleErrorMessage(e.getLocalizedMessage());
        }
      },
      handleErrorMessage : function(message) {
        var dialog = this.getFactory().newDialog(com.runwaysdk.Localize.get("rError", "Error"), {modal: true});
        dialog.appendContent(message);
        dialog.addButton(com.runwaysdk.Localize.get("rOk", "Ok"), function(){dialog.close();});
        dialog.render();        
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
          tableCfg.dataSource = new net.geoprism.data.browser.DataBrowserQueryDataSource({
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
        that._tree.bind(
          'tree.contextmenu',
          Mojo.Util.bind(that, that._onContextTreeNode)
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
