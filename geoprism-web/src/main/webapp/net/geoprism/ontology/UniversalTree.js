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
  
  var universalTreeName = "net.geoprism.ontology.UniversalTree";
  var TermTree = net.geoprism.ontology.TermTree;
  
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
        
        config.exportMenuType = "net.geoprism.ontology.UniversalExportMenu";
        
        this.$initialize(config);
        
        this._wrapperDiv = this.getFactory().newElement("div");
        
        this._subtypeMap = new net.geoprism.ontology.NodeMap(this);
      },
      
      removeNode : function(node) {          
        // Recursively delete all descendant nodes
        if (node.children) {
          for (var i=node.children.length-1; i >= 0; i--) {
            var child = node.children[i];
            
            this.removeNode(child);
          }
        }
            
        // Remove thte node form the tree
        this.getImpl().tree("removeNode", node);
          
        // Remove the node from the term-node mapping
        this._nodeMap.removeNodeMapping(node.runwayId, node.id);
          
        // Remove the node from the term-subtype mapping
        if(node.data != null && node.data.isSubtypeContainer) {
          this._subtypeMap.removeNodeMapping(node.runwayId, node.id);          
        }
      },      
      
      /**
       * Removes the term and all children terms from the caches.
       */
      __dropAll : function(termId) {
        var that = this;
        var nodes = this.__getNodesById(termId);
        
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
        var nodes = this.__getNodesById(termId);
        var $tree = this.getImpl();
        var shouldRefresh = !(this.hasLoadedNode(nodes[0]) && (nodes[0].children.length <= 0));
        
        this.parentRelationshipCache.removeAll(termId);
        
        this.__dropAll(termId);
        
        for (var i = 0; i < nodes.length; ++i) {
          this.removeNode(nodes[i]);
        }
        
        if (shouldRefresh) {
          // We don't have the relationship id of the new relationship between the children and the root node. 
          var rootTermId = this._getRootTermId();          
          
          this.refreshTerm(rootTermId);
        }
      },
            
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

      _handleMove : function(movedNode, targetNode, previousParent, relDTO, nodes) {
        if(targetNode.data != null && targetNode.data.isSubtypeContainer) {
          // We need to refresh all of the subtype nodes        
          var subtypeNodes = this._subtypeMap.getNodes(targetNode.runwayId);
          
          this.$_handleMove(movedNode, targetNode, previousParent, relDTO, subtypeNodes)
        }
        else {
          this.$_handleMove(movedNode, targetNode, previousParent, relDTO, nodes)          
        }
      },
      
      _handleCopy : function(movedNode, targetNode, relDTO, nodes) {
        if(targetNode.data != null && targetNode.data.isSubtypeContainer) {
          // We need to refresh all of the subtype nodes        
          var subtypeNodes = this._subtypeMap.getNodes(targetNode.runwayId);
          
          this.$_handleCopy(movedNode, targetNode, relDTO, subtypeNodes)
        }
        else {
          this.$_handleCopy(movedNode, targetNode, relDTO, nodes)          
        }
      },
      
      _handleCreateTerm : function (parentId, parentNodes, responseObj) {
        var nodes = this.$_handleCreateTerm(parentId, parentNodes, responseObj);
        
        // Create the subtype nodes
        for (var i = 0; i < nodes.length; ++i) {
          this._createSubtypeNode(nodes[i]);
        }
      },
      
      _createSubtypeNode : function(parent) {
        var id = Mojo.Util.generateId();
          
        parent.data = parent.data || {};
        parent.data.isANode = id;
          
        var config = {dragAndDrop: false, label: this.localize("isANode"), runwayId:parent.runwayId, data: { isSubtypeContainer: true }}
        var node = this.__createTreeNode(id, parent, true, config);
            
        this._subtypeMap.addNodeMapping(parent.runwayId, node.id);
        
        return node;
      },
      
      
      /**
       * is binded to tree.contextmenu, called when the user right clicks on a node.
       */
      // @Override
      _createNodeRightClickMenu : function(event) {
        var that = this;
        var node = event.node;
          
        if (node.data != null && node.data.isSubtypeContainer) {
          var items = [];
          items.push({label:this.localize("createIsA"), id:"add", handler:Mojo.Util.bind(this, this.__onIsACreateClick)});
          items.push({label:this.localize("refreshIsA"), id:"refresh", handler:Mojo.Util.bind(this, this.__onContextRefreshClick)});
          
          return items;
        }
        else {
          return this.$_createNodeRightClickMenu(event);
        }        
      },
      
      __onIsACreateClick : function(contextMenu, contextMenuItem, mouseEvent) {
        var targetNode = contextMenu.getTarget();
        var targetId = this.__getRunwayIdFromNode(targetNode);

        var nodes = this._subtypeMap.getNodes(targetId);
        
        this.createTerm(targetId, "com.runwaysdk.system.gis.geo.IsARelationship", nodes);
      },
      
      /**
       * OVERRIDE : Adds special logic for handling Subtypes.
       * 
       * Fetches all the term's children from the server, removes all children of the provided nodes,
       * and then re-populates the child nodes based on the TermAndRel objects received from the server.
       * 
       * @param termId 
       *     Id of the term to refresh
       * @param callback
       *     Map of additional callback functions for request success and failure
       * @param nodes
       *     List of nodes to update with the refreshed term data.  If this is null then
       *     all nodes corresponding to the termId will be refreshed. 
       */
      refreshTerm : function(termId, callback, nodes) {
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

            if(nodes == null) {
              nodes = that.__getNodesById(termId);              
            }
            
            // Remove existing children
            for (var iNode = 0; iNode < nodes.length; ++iNode) {
              var node = nodes[iNode];
              node.hasFetched = true;
              
              var children = node.children.slice(0,node.children.length); // slice is used here to avoid concurrent modification, screwing up the loop.
              for (var i=0; i < children.length; i++) {
                that.removeNode(children[i]);
              }
            }
            
            // Create a node for every term we got from the server.
            for (var iNode = 0; iNode < nodes.length; ++iNode) {
              var node = nodes[iNode];
              var runwayId = that.__getRunwayIdFromNode(node);
              var id = Mojo.Util.generateId();
              
              var subtypeNode = null;
              
              // Create a subtype node for all nodes expect the hidden root node
              if(runwayId != null) {
                subtypeNode = that._createSubtypeNode(node);
              }
              
              for (var i = 0; i < termAndRels.length; ++i) {
                var childId = termAndRels[i].getTerm().getId();
                
                var parentRecord = {parentId: termId, relId: termAndRels[i].getRelationshipId(), relType: termAndRels[i].getRelationshipType()};
                that.parentRelationshipCache.put(childId, parentRecord);
                
                that.termCache[childId] = termAndRels[i].getTerm();
                
                if (termAndRels[i].getRelationshipType() === "com.runwaysdk.system.gis.geo.IsARelationship")
                {
                  if(subtypeNode != null) {
                    that.__createTreeNode(childId, subtypeNode);                    
                  }
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
      
      _getRootTermId : function() {
        var rootTermId = this.rootTermConfigs.keySet()[0];
        
        return rootTermId;        
      },
      
      _onClickNewCountry : function() {
        var rootTermId = this._getRootTermId();
      
        this.createTerm(rootTermId);
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
