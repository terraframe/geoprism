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
  
  var universalTreeName = "com.runwaysdk.geodashboard.ontology.UniversalTree";
  var TermTree = com.runwaysdk.geodashboard.ontology.TermTree;
  
  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage(universalTreeName, {
    "newCountry" : "New Country",
    "deleteDescribe" : "Are you sure you want to delete '${termLabel}' and all GeoEntities that may reference it?",
    "isANode" : "Subtypes",
    "createIsA" : "Create Subtype",
    "refreshIsA" : "Refresh Subtypes"
  });
  
  /**
   * Adds a 'new country' button, changes some default term tree language and delegates to the term tree for all the heavy lifting.
   */
  var universalTree = ClassFramework.newClass(universalTreeName, {
    
    Extends : TermTree,
    
    Instance : {
      
      initialize : function(config) {
        
        config = config || {};
        this._config = config;
        
        this.$initialize(config);
        
        this._wrapperDiv = this.getFactory().newElement("div");
        
      },
      
      /**
       * Removes the term and all children terms from the caches.
       */
      __dropAll : function(termId) {
        var that = this;
        var nodes = this.__getNodesById(termId);
        
        // Children of universals are appended to the root node
        // The children are repeated under the copies, so we only want to append one set of the children to the root node.
        var node = nodes[0];
        var children = this.getChildren(node);
        var len = children.length;
        for (var i = 0; i < len; ++i) {
          var child = children[i];
          var childId = that.__getRunwayIdFromNode(child);
          
          this.__dropAll(childId);
          
          this.parentRelationshipCache.removeRecordMatchingId(childId, termId, that);
        }
        
        delete this.termCache[termId];
      },
      
      // @Override
      refreshTreeAfterDeleteTerm : function(termId) {
        var nodes = this.__getNodesById(termId);
        var $tree = this.getImpl();
        var shouldRefresh = !($tree.jstree("is_loaded", nodes[0]) && ($tree.jstree("get_children_dom", nodes[0]).length <= 0));
        
        this.parentRelationshipCache.removeAll(termId);
        
        this.__dropAll(termId);
        
        for (var i = 0; i < nodes.length; ++i) {
          $tree.jstree(
            'delete_node',
            nodes[i]
          );
        }
        
        if (shouldRefresh) {
          // We don't have the relationship id of the new relationship between the children and the root node. 
          this.refreshTerm(this.rootTermId);
        }
      },
      
      // @Override
      _check_callback : function(operation, node, node_parent, node_position, more) {
        if (operation === "move_node") {
          // You can't drag isANodeContainers.
          if (node.data != null && node.data.isIsANodeContainer) {
            return false;
          }
        }
        
        return true;
      },
      
      // @Override
      __findInsertIndex : function(label, newParent) {
        var children = this.getChildren(newParent);
        
        children.sort(function(nodeA,nodeB){
          if (nodeA.id === newParent.data.isANode) {
            return -1;
          }
          else if (nodeB.id === newParent.data.isANode) {
            return 1;
          }
          
          return nodeA.text.localeCompare(nodeB.text);
        });
       
        var i = 0;
        if (newParent.data != null && newParent.data.isANode != null) {
          i = 1;
        }
        
        for (; i < children.length; ++i) {
          console.log("comparing " + children[i].text + " with " + label + " results in " + children[i].text.localeCompare(label));
          if (children[i].text.localeCompare(label) >= 0) {
            break;
          }
        }
        
        console.log("returning index " + i + " for label " + label);
        
        return i;
      },
      
      _getContainerNode : function(parentNode, relType) {
        if (relType === "com.runwaysdk.system.gis.geo.IsARelationship") {
          return parentNode.data.isANode;
        }
        
        return parentNode;
      },
      
      _getRelationshipForNode : function(movedNode, newParent, oldRel) {
        if (newParent.data.isIsANodeContainer) {
          return "com.runwaysdk.system.gis.geo.IsARelationship";
        }
        
        return "com.runwaysdk.system.gis.geo.AllowedIn";
      },
      
      appendAdditionalData : function(jsonArray, parentNode, isAChildren) {
        if (parentNode.id === "#" || (parentNode.data != null && parentNode.data.isIsANodeContainer)) { return; }
        
        var isANode = {
          text: this.localize("isANode"),
          id: Mojo.Util.generateId(), state: {opened: false},
          data: { runwayId: this.__getRunwayIdFromNode(parentNode), isIsANodeContainer: true },
          children: isAChildren
        };
        
        parentNode.data.isANode = isANode.id;
        
        jsonArray.splice(0, 0, isANode);
      },
      
      /**
       * is binded to tree.contextmenu, called when the user right clicks on a node.
       */
      // @Override
      __onNodeRightClick : function(event, object) {
        var node = object.node;
        var parentId = this.getParentRunwayId(node);
        var term = this.termCache[this.__getRunwayIdFromNode(node)];
        
        if (this._cm != null && !this._cm.isDestroyed()) {
          this._cm.destroy();
        }
        
        if (node.data != null && node.data.isIsANodeContainer) {
          this._cm = this.getFactory().newContextMenu(node);
          
          // They right clicked on an isA container node. Display the context menu for isA containers.
          var create = this._cm.addItem(this.localize("createIsA"), "add", Mojo.Util.bind(this, this.__onIsACreateClick));
          var refresh = this._cm.addItem(this.localize("refreshIsA"), "refresh", Mojo.Util.bind(this, this.__onContextRefreshClick));
          
          this._cm.render();
        }
        else {
          this.$__onNodeRightClick(event, object);
        }
      },
      
      __onIsACreateClick : function(contextMenu, contextMenuItem, mouseEvent) {
        var targetNode = contextMenu.getTarget();
        var targetId = this.__getRunwayIdFromNode(targetNode);
        this.createTerm(targetId, targetNode, "com.runwaysdk.system.gis.geo.IsARelationship");
      },
      
//      __treeWantsData : function(treeThisRef, parent, jsTreeCallback) {
//        var that = this;
//        
//        var parentId = this.__getRunwayIdFromNode(parent);
//        
//        if (parent.data != null && parent.data.isIsANodeContainer) {
//          var oldRel = this._config.relationshipType;
//          this._config.relationshipType = "com.runwaysdk.system.gis.geo.IsARelationship";
//          this.$__treeWantsData(treeThisRef, parent, jsTreeCallback);
//          this._config.relationshipType = oldRel;
//        }
//        else {
//          this.$__treeWantsData(treeThisRef, parent, jsTreeCallback);
//        }
//      },
      
      _onClickNewCountry : function() {
        this.createTerm(this.rootTermId, {id:"#"});
      },
      
      createCountryButton : function() {
        var but = this.getFactory().newButton(this.localize("newCountry"), Mojo.Util.bind(this, this._onClickNewCountry));
        
        but.addClassName("btn btn-primary");
        but.setStyle("margin-bottom", "20px");
        
        this._wrapperDiv.appendChild(but);
      },
      
      destroy : function() {
        
        if (!this.isDestroyed()) {
          this.$destory();
          this._wrapperDiv.destory();
        }
        
      },
      
      render : function(parent) {
        this.createCountryButton();
        
        this._wrapperDiv.render(parent);
        
        this.$render(this._wrapperDiv);
      }
    }
  });
  
  return universalTree;
  
})();