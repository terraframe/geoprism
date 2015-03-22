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
        
        config.exportMenuType = "com.runwaysdk.geodashboard.gis.UniversalExportMenu";
        
        this.$initialize(config);
        
        this._wrapperDiv = this.getFactory().newElement("div");
        
      },
      
      /**
       * Removes the term and all children terms from the caches.
       */
      __dropAll : function(termId) {
        var that = this;
        var nodes = this.getNodesById(termId);
        
        // Children of universals are appended to the root node
        // The children are repeated under the copies, so its okay to grab node[0]
        var node = nodes[0];
        var children = node.children;
        var len = children.length;
        for (var i = 0; i < len; ++i) {
          var child = children[i];
          var childId = that.__getRunwayIdFromNode(child);
          
          // This child is an isAContainer. Drop all of its children. 
          if (childId === termId) {
            var isAChildren = child.children;
            var isALen = isAChildren.length;
            for (var isai = 0; isai < isALen; ++isai) {
              var isAChild = isAChildren[isai];
              var isAId = that.__getRunwayIdFromNode(isAChild);
              
              var relType = this._getRelationshipForNode(isAChild, child);
              this.parentRelationshipCache.removeRecordMatchingIdAndRelType(isAId, childId, relType);
              
              this.__dropAll(isAId);
            }
          }
          else {
            this.__dropAll(childId);
            
            var relType = this._getRelationshipForNode(child, node);
            this.parentRelationshipCache.removeRecordMatchingIdAndRelType(childId, termId, relType);
          }
        }
        
        delete this.termCache[termId];
      },
      
      // @Override
      refreshTreeAfterDeleteTerm : function(termId) {
        var nodes = this.getNodesById(termId);
        var $tree = this.getImpl();
        var shouldRefresh = !(that.hasLoadedNode(nodes[0]) && (nodes[0].children.length <= 0));
        
        this.parentRelationshipCache.removeAll(termId);
        
        this.__dropAll(termId);
        
        for (var i = 0; i < nodes.length; ++i) {
          $tree.tree(
            'removeNode',
            nodes[i]
          );
        }
        
        if (shouldRefresh) {
          // We don't have the relationship id of the new relationship between the children and the root node. 
          this.refreshTerm(this.rootTermId);
        }
      },
      
      // @Override
//      _check_callback : function(operation, node, node_parent, node_position, more) {
//        if (operation === "move_node") {
//          // You can't drag isANodeContainers.
//          if (node.data != null && node.data.isSubtypeContainer) {
//            return false;
//          }
//        }
//        
//        return true;
//      },
      
      // @Override
//      __findInsertIndex : function(label, newParent) {
//        var children = this.getChildren(newParent);
//        
//        children.sort(function(nodeA,nodeB){
//          if (newParent.data != null && nodeA.id === newParent.data.isANode) {
//            return -1;
//          }
//          else if (newParent.data != null && nodeB.id === newParent.data.isANode) {
//            return 1;
//          }
//          
//          return nodeA.text.localeCompare(nodeB.text);
//        });
//       
//        var i = 0;
//        if (newParent.data != null && newParent.data.isANode != null) {
//          i = 1;
//        }
//        
//        for (; i < children.length; ++i) {
//          if (children[i].text.localeCompare(label) >= 0) {
//            break;
//          }
//        }
//        
//        return i;
//      },
      
      _getContainerNode : function(parentNode, relType) {
        if (relType === "com.runwaysdk.system.gis.geo.IsARelationship") {
          return parentNode.data.isANode;
        }
        
        return parentNode;
      },
      
      _getRelationshipForNode : function(movedNode, newParent, oldRel) {
        if (newParent.data != null && newParent.data.isSubtypeContainer) {
          return "com.runwaysdk.system.gis.geo.IsARelationship";
        }
        
        return "com.runwaysdk.system.gis.geo.AllowedIn";
      },
      
      /**
       * Override
       * 
       * Adds the isanode container to all nodes.
       */
      refreshTermHookin : function(parentNode) {
        if (parentNode.id === "#" || (parentNode.data != null && parentNode.data.isSubtypeContainer)) { return; }
      },
      
      /**
       * is binded to tree.contextmenu, called when the user right clicks on a node.
       */
      // @Override
      __onNodeRightClick : function(event, object) {
        var that = this;
        var node = event.node;
        
        if (this._cm != null && !this._cm.isDestroyed()) {
          this._cm.destroy();
        }
        
        that.getImpl().tree('selectNode', node);
        
        if (node.data != null && node.data.isSubtypeContainer) {
          this._cm = this.getFactory().newContextMenu(node);
          
          // They right clicked on an isA container node. Display the context menu for isA containers.
          var create = this._cm.addItem(this.localize("createIsA"), "add", Mojo.Util.bind(this, this.__onIsACreateClick));
          var refresh = this._cm.addItem(this.localize("refreshIsA"), "refresh", Mojo.Util.bind(this, this.__onContextRefreshClick));
          
          this._cm.render();
          
          this._cm.addDestroyEventListener(function() {
            that.getImpl().tree("selectNode", null);
          });
        }
        else {
          this.$__onNodeRightClick(event, object);
        }
      },
      
      __onIsACreateClick : function(contextMenu, contextMenuItem, mouseEvent) {
        var targetNode = contextMenu.getTarget();
        var targetId = this.__getRunwayIdFromNode(targetNode);
        this.createTerm(targetId, "com.runwaysdk.system.gis.geo.IsARelationship", targetNode);
      },
      
      /**
       * Override
       * 
       * Adds special logic for handling Subtypes.
       */
      refreshTerm : function(termId, callback) {
        var that = this;
        
        this.setTermBusy(termId, true);
        
        var myCallback = new Mojo.ClientRequest({
          onSuccess : function(responseText) {
            var json = Mojo.Util.getObject(responseText);
            var objArray = com.runwaysdk.DTOUtil.convertToType(json.returnValue);
            var termAndRels = [];
            for (var i = 0; i < objArray.length; ++i) {
              termAndRels.push(that.__responseToTNR(objArray[i]));
            }
            
            var nodes = that.getNodesById(termId);
            
            // Remove existing children
            for (var iNode = 0; iNode < nodes.length; ++iNode) {
              var node = nodes[iNode];
              node.hasFetched = true;
              var children = node.children.slice(0,node.children.length); // slice is used here to avoid concurrent modification, screwing up the loop.
              for (var i=0; i < children.length; i++) {
                $(that.getRawEl()).tree("removeNode", children[i]);
              }
            }
            
            // Create a node for every term we got from the server.
            for (var iNode = 0; iNode < nodes.length; ++iNode) {
              var node = nodes[iNode];
              var id = Mojo.Util.generateId();
              
              node.data = node.data || {};
              node.data.isANode = id;
              
              var synNode = that.__createTreeNode(id, node, true, {label: that.localize("isANode"), runwayId: that.__getRunwayIdFromNode(node), data: { isSubtypeContainer: true }});
              
              for (var i = 0; i < termAndRels.length; ++i) {
                var childId = termAndRels[i].getTerm().getId();
                
                var parentRecord = {parentId: termId, relId: termAndRels[i].getRelationshipId(), relType: termAndRels[i].getRelationshipType()};
                that.parentRelationshipCache.put(childId, parentRecord);
                
                that.termCache[childId] = termAndRels[i].getTerm();
                
                if (termAndRels[i].getRelationshipType() === "com.runwaysdk.system.gis.geo.IsARelationship")
                {
                  that.__createTreeNode(childId, synNode);
                }
                else
                {
                  that.__createTreeNode(childId, node);
                }
              }
            }
            
            that.setTermBusy(termId, false);
            
            if (callback != null && Mojo.Util.isFunction(callback.onSuccess))
            {
              callback.onSuccess();
            }
          },
          
          onFailure : function(err) {
            that.setTermBusy(termId, false);
            
            if (callback != null && Mojo.Util.isFunction(callback.onFailure))
            {
              callback.onFailure();
            }
            else
            {
              that.handleException(err);
            }
            
            return;
          }
        });
        
        Mojo.Util.invokeControllerAction(this._config.termType, "getDirectDescendants", {parentId: termId, relationshipTypes: this._config.relationshipTypes, pageNum: 0, pageSize: 0}, myCallback);
      },
      
      _onClickNewCountry : function() {
        this.createTerm(this.rootTermId);
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
