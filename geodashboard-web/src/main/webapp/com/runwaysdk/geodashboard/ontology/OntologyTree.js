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
  
  var ontologyTreeName = "com.runwaysdk.geodashboard.ontology.OntologyTree";
  var TermTree = com.runwaysdk.geodashboard.ontology.TermTree;
  
  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage(ontologyTreeName, {
    "newCountry" : "New Country",
    "deleteDescribe" : "Are you sure you want to delete '${termLabel}' and all GeoEntities that may reference it?",
    "isANode" : "Subtypes",
    "createIsA" : "Create Subtype",
    "refreshIsA" : "Refresh Subtypes"
  });
  
  /**
   * Adds a 'new country' button, changes some default term tree language and delegates to the term tree for all the heavy lifting.
   */
  var universalTree = ClassFramework.newClass(ontologyTreeName, {
    
    Extends : TermTree,
    
    Instance : {
      
      initialize : function(config) {
        
        config = config || {};
        this._config = config;
        
        config.exportMenuType = "com.runwaysdk.geodashboard.gis.ClassifierExportMenu";
        
        this.$initialize(config);
        
        this._wrapperDiv = this.getFactory().newElement("div");
        
      },
      
      /**
       * Called when the user right clicks on a node.
       * This override will stop the user from deleting root nodes.
       */
      // @Override
      __onNodeRightClick : function(event, object) {
        var $tree = this.getImpl();
        var node = object.node;
        
        if (this._cm != null && !this._cm.isDestroyed()) {
          this._cm.destroy();
        }
        
        this._cm = this.getFactory().newContextMenu(node);
        var create = this._cm.addItem(this.localize("create"), "add", Mojo.Util.bind(this, this.__onContextCreateClick));
        var update = this._cm.addItem(this.localize("update"), "edit", Mojo.Util.bind(this, this.__onContextEditClick));
        var del = this._cm.addItem(this.localize("delete"), "delete", Mojo.Util.bind(this, this.__onContextDeleteClick));
        var refresh = this._cm.addItem(this.localize("refresh"), "refresh", Mojo.Util.bind(this, this.__onContextRefreshClick));
        var cmiExport = this._cm.addItem(this.localize("export"), "export", Mojo.Util.bind(this, this.__onContextExportClick));
        
        if (this.busyNodes.contains(node.id)) {
          create.setEnabled(false);
          update.setEnabled(false);
          del.setEnabled(false);
          refresh.setEnabled(false);
          cmiExport.setEnabled(false);
        }
        
        // You can't delete the built in ontology nodes (IDE ontologys, geodashboard ontologies, etc)
        if (this.getParentRunwayId(node) === this.rootTermId) {
          del.setEnabled(false);
        }
        
        this._cm.render();
        
        this._cm.addDestroyEventListener(function() {
          $tree.jstree("deselect_node", node);
        });
      },
      
      // @Override
      _check_callback : function(operation, node, node_parent, node_position, more) {
        if (operation === "move_node") {
          // You can't move the built in ontology nodes (IDE ontologies, geodashboard ontologies, etc)
          if (this.getParentRunwayId(node) === this.rootTermId) {
            return false;
          }
          
          // You can't move nodes onto the root level (Only a developer can create a root level node.)
          if (this.__getRunwayIdFromNode(node_parent) === this.rootTermId) {
            return false;
          } 
        }
        
        return true;
      },
      
      destroy : function() {
        
        if (!this.isDestroyed()) {
          this.$destory();
          this._wrapperDiv.destory();
        }
        
      },
      
      render : function(parent) {
        this._wrapperDiv.render(parent);
        
        this.$render(this._wrapperDiv);
      }
    }
  });
  
  return universalTree;
  
})();
