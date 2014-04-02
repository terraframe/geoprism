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

//define(["../../factory/runway/contextmenu/ContextMenu", "../../factory/runway/widget/Widget", "jquery-tree"], function(ContextMenu, Widget){
(function(){
  
  var ContextMenu = com.runwaysdk.ui.factory.runway.ContextMenu;
  var Widget = com.runwaysdk.ui.factory.runway.Widget;

  var RW = Mojo.Meta.alias(Mojo.RW_PACKAGE + "*");
  var UI = Mojo.Meta.alias(Mojo.UI_PACKAGE + "*");
  
  var termTreeName = 'com.runwaysdk.geodashboard.ontology.TermTree';
  
  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage(termTreeName, {
    "move" : "Move",
    "copy" : "Copy",
    "create" : "Create",
    "update" : "Update",
    "delete" : "Delete",
    "refresh" : "Refresh",
    "deleteMultiParentDescribe" : "This ${termMdLabel} has more than one parent. Do you want to delete '${termLabel}' everywhere, or do you want to only remove the relationship with '${parentLabel}'?",
    "deleteTermAndRels" : "Delete '${termLabel}' everywhere",
    "deleteRel" : "Delete only this '${termLabel}'",
    "cancel" : "Cancel",
    "deleteDescribe" : "Are you sure you want to delete '${termLabel}'?"
  });
  
  /**
   * @class com.runwaysdk.geodashboard.ontology.TermTree A wrapper around JQuery widget jqTree to allow for integration with Term objects.
   * 
   * @constructs
   * @param obj
   *   @param String obj.nodeId The id of the div defined in html, specifying the location for the tree. The id is prefixed with #.
   *   @param Object obj.data Optional, a properly formatted data object as documented by jqTree.
   *   @param Boolean obj.dragDrop Optional, set to true to enable drag drop, false to disable. Default is false.
   */
  var tree = Mojo.Meta.newClass(termTreeName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(config) {
        
        config = config || {};
        this.requireParameter("termType", config.termType, "string");
        this.requireParameter("relationshipType", config.relationshipType, "string");
        this.requireParameter("rootTerm", config.rootTerm, "string");
        
        var defaultConfig = {
          el: "div",
          data: [], // This parameter is required for jqTree, otherwise it tries to load data from a url.
          dragAndDrop: true,
          selectable: true,
          checkable: true,
          crud: {
            create: {
              width: 680,
              height: 300
            },
            update: {
              width: 680,
              height: 300
            }
          }
        };
        this._config = Mojo.Util.deepMerge(defaultConfig, config);
        
        // Add checkboxes
        if (this._config.checkable && this._config.onCreateLi == null) {
           this._config.onCreateLi = function(node, $li) {
            if (!node.phantom) {
              $li.find('.jqtree-title').before('<input class="jqtree-checkbox" id="check" type="checkbox">');
              jcf.customForms.replaceAll();
            }
          };
        }
        
        this.$initialize(this._config.el, this._config);
        
        this.__setRootTerm(config.rootTerm);
        
        this.termCache = {};
        
        // jqtree assumes that id's are unique. For our purposes the id may map to multiple nodes.
        // This map maps the runwayId = [ generatedId's ]
        this.duplicateMap = {};
        
        this.parentRelationshipCache = new ParentRelationshipCache();
        
        this.selectCallbacks = [];
        this.deselectCallbacks = [];
      },
      
      /**
       * Sets the root term for the tree. The root term must be set before the tree can be used.
       * 
       * @param com.runwaysdk.business.TermDTO or String (Id) rootTerm The root term of the tree.
       * @param Object callback Optional, a callback object with onSuccess and onFailure methods.
       */
      __setRootTerm : function(rootTerm, callback) {
        this.requireParameter("rootTerm", rootTerm);
        
        if (rootTerm instanceof com.runwaysdk.business.TermDTO) {
          this.rootTermId = rootTerm.getId();
          this.termCache[this.rootTermId] = rootTerm;
        }
        else if (Mojo.Util.isString(rootTerm)) {
          this.rootTermId = rootTerm;
        }
        else {
          var ex = "Root term must be of type com.runwaysdk.business.TermDTO or String (an id).";
          throw new com.runwaysdk.Exception(ex);
        }
      },
      
      /**
       * Registers a function to on term select.
       * 
       * @param Function callback A function with argument 'term', the selected term. 
       */
      registerOnTermSelect : function(callback) {
        this.requireParameter("callback", callback);
        
        this.selectCallbacks.push(callback);
      },
      
      /**
       * Registers a function to on term deselect.
       * 
       * @param Function callback A function with argument 'term', the deselected term. 
       */
      registerOnTermDeselect : function(callback) {
        this.requireParameter("callback", callback);
        
        this.deselectCallbacks.push(callback);
      },
      
      /**
       * GeoEntities and Universals handle this differently.
       * 
       * @param termId
       */
      refreshTreeAfterDeleteTerm : function(termId) {
        // This method is overridden in UniversalTree, so this logic here is for GeoEntityTree and assumes that all children are also deleted.
        
        var nodes = this.__getNodesById(termId);
        var $thisTree = $(this.getRawEl());
        
        // TODO: Remove children from the parent relationship cache.
//        var node = nodes[0];
//        var children = nodes[0].children;
//        for (var i = 0; i < children.length; ++i) {
//          
//        }
        
        for (var i = 0; i < nodes.length; ++i) {
          $thisTree.tree(
            'removeNode',
            nodes[i]
          );
        }
        
        delete this.termCache[termId];
        
        if (this._config.checkable) {
          jcf.customForms.replaceAll();
        }
      },
      
      /**
       * Notifies the server to delete the term and then updates the tree by removing the node.
       */
      deleteTerm : function(termId, parent) {
        this.requireParameter("termId", termId, "string");
        this.requireParameter("parent", parent);
        
        var term = this.termCache[termId]; 
        var parentId = (parent instanceof Object) ? parent.getId() : parent;
        
        var parentRecord = this.parentRelationshipCache.getRecordWithParentId(termId, parentId, this);
        
        var that = this;
        var $thisTree = $(this.getRawEl());
        
        var deleteCallback = new Mojo.ClientRequest({
          onSuccess : function(retval) {
            that.refreshTreeAfterDeleteTerm(termId);
          },
          
          onFailure : function(err) {
            that.doForTermAndAllChildren(termId, function(node){that.setNodeBusy(node, false)});
            that.handleException(err);
            return;
          }
        });
        
        this.doForTermAndAllChildren(termId, function(node){that.setNodeBusy(node, true)});
        
        Mojo.Util.invokeControllerAction(this._config.termType, "delete", {dto: term}, deleteCallback);
      },
      
      createTerm : function(parentId) {
        this.requireParameter("parentId", parentId, "string");
        var that = this;
        
        var parentNodes = this.__getNodesById(parentId);
        if (parentNodes == null || parentNodes == undefined) {
          var ex = new com.runwaysdk.Exception("The provided parent [" + parentId + "] does not exist in this tree.");
          this.handleException(ex);
          return;
        }
        
        var config = {
          type: this._config.termType,
          viewParams: {parentId: parentId, relationshipType: this._config.relationshipType},
          action: "create",
          actionParams: {parentId: parentId, relationshipType: this._config.relationshipType},
          onSuccess : function(termAndRel) {
            var term = termAndRel.getTerm();
            var relId = termAndRel.getRelationshipId();
            var relType = termAndRel.getRelationshipType();
            
            that.parentRelationshipCache.put(term.getId(), {parentId: parentId, relId: relId, relType: relType});
            that.termCache[term.getId()] = term;
            
            var $thisTree = $(that.getRawEl());
            for (var i = 0; i < parentNodes.length; ++i) {
              var node = that.__createTreeNode(term.getId(), parentNodes[i], true);
              
              $thisTree.tree("openNode", node);
            }
            
//            if (that._config.checkable) {
//              jcf.customForms.replaceAll();
//            }
          },
          onFailure : function(e) {
            that.handleException(e);
          }
        };
        Mojo.Util.merge(this._config.crud.create, config);
        
        new com.runwaysdk.ui.RunwayControllerFormDialog(config).render();
      },
      
      /**
       * is binded to context menu option Refresh.
       */
      __onContextRefreshClick : function(contextMenu, contextMenuItem, mouseEvent) {
        var targetNode = contextMenu.getTarget();
        
        targetNode.hasFetched = null;
        
        // Node open will refresh.
        this.__onNodeOpen({node: targetNode});
      },
      
      /**
       * is binded to context menu option Create. 
       */
      __onContextCreateClick : function(contextMenu, contextMenuItem, mouseEvent) {
        var targetNode = contextMenu.getTarget();
        var targetId = this.__getRunwayIdFromNode(targetNode);
        this.createTerm(targetId);
      },
      
      /**
       * is binded to context menu option Edit. 
       */
      __onContextEditClick : function(contextMenu, contextMenuItem, mouseEvent) {
        var node = contextMenu.getTarget();
        var termId = this.__getRunwayIdFromNode(node);
        var that = this;
        var parentId = this.__getRunwayIdFromNode(node.parent);
        
        var config = {
          type: this._config.termType,
          viewParams: {parentId: parentId, relationshipType: ""},
          action: "update",
          id: termId,
          onSuccess : function(term) {
            that.termCache[term.getId()] = term;
            
            var nodes = that.__getNodesById(term.getId());
            for (var i = 0; i < nodes.length; ++i) {
              $(that.getRawEl()).tree("updateNode", nodes[i], {label: that._getTermDisplayLabel(term)});
            }
            
            that.setTermBusy(termId, false);
            
            if (that._config.checkable) {
              jcf.customForms.replaceAll();
            }
          },
          onFailure : function(e) {
            that.setTermBusy(termId, false);
            that.handleException(e);
          },
          onClickSubmit : function() {
            that.setTermBusy(termId, true);
          },
          onViewSuccess : function(html) {
            that.setTermBusy(termId, false);
          }
        };
        Mojo.Util.merge(this._config.crud.update, config);
        
        new com.runwaysdk.ui.RunwayControllerFormDialog(config).render();
      },
      
      /**
       * is binded to context menu option Delete. 
       */
      __onContextDeleteClick : function(contextMenu, contextMenuItem, mouseEvent) {
        var node = contextMenu.getTarget();
        var termId = this.__getRunwayIdFromNode(node);
        var parentId = this.__getRunwayIdFromNode(node.parent);
        var that = this;
        var dialog = null;
        
        if (termId === this.rootTermId) {
          var ex = new com.runwaysdk.Exception("You cannot delete the root node.");
          this.handleException(ex);
          return;
        }
        
        var parentRecords = this.parentRelationshipCache.get(termId, this);
        
        var deleteHandler = function() {
          that.deleteTerm(termId, parentId);
          dialog.close();
        };
        var performDeleteRelHandler = function() {
          var deleteRelCallback = {
            onSuccess : function() {
              // Children of universals are appended to the root node, so refresh the root node.
              that.refreshTerm(that.rootTermId);
              
              var nodes = that.__getNodesById(termId);
              for (var i = 0; i < nodes.length; ++i) {
                if (that.__getRunwayIdFromNode(nodes[i].parent) == parentId) {
                  $(that.getRawEl()).tree("removeNode", nodes[i]);
                }
              }
              that.parentRelationshipCache.removeRecordMatchingId(termId, parentId, that);
            },
            onFailure : function(err) {
              that.handleException(err);
              return;
            }
          }
          Mojo.Util.copy(new Mojo.ClientRequest(deleteRelCallback), deleteRelCallback);
          
          var parentRecord = that.parentRelationshipCache.getRecordWithParentId(termId, parentId, this);
//          that.termCache[termId].removeChildTerm(deleteRelCallback, parentRecord.relId);
          com.runwaysdk.Facade.deleteChild(deleteRelCallback, parentRecord.relId);
          
          dialog.close();
        };
        var cancelHandler = function() {
          dialog.close();
        };
        
        this.__getTermFromId(termId, {
          onSuccess: function(term) {
            var newType = eval("new " + that._config.termType + "()");
            var termMdLabel = newType.getMd().getDisplayLabel();
            var termLabel = term.getDisplayLabel().getLocalizedValue();
            
            var deleteLabel = that.localize("delete") + " " + termMdLabel;
            
            if (parentRecords.length > 1) {
              var deleteMultiParentDescribe = that.localize("deleteMultiParentDescribe").replace("${termMdLabel}", termMdLabel).replace("${termMdLabel}", termMdLabel).replace("${termLabel}", termLabel);
              deleteMultiParentDescribe = deleteMultiParentDescribe.replace("${parentLabel}", that.termCache[parentId].getDisplayLabel().getLocalizedValue());
              
              dialog = that.getFactory().newDialog(deleteLabel, {modal: false, width: 650, height: 300, resizable: false});
              dialog.appendContent(deleteMultiParentDescribe);
              dialog.addButton(that.localize("deleteTermAndRels").replace("${termLabel}", termLabel), deleteHandler, null, {"class": "btn btn-primary"});
              dialog.addButton(that.localize("deleteRel").replace("${termLabel}", termLabel), performDeleteRelHandler, null, {"class": "btn btn-primary"});
              dialog.addButton(that.localize("cancel"), cancelHandler, null, {"class": "btn"});
              dialog.render();
            }
            else {
              dialog = that.getFactory().newDialog(deleteLabel, {modal: false, width: 485, height: 200, resizable: false});
              dialog.appendContent(that.localize("deleteDescribe").replace("${termLabel}", termLabel));
              dialog.addButton(deleteLabel, deleteHandler, null, {"class": "btn btn-primary"});
              dialog.addButton(that.localize("cancel"), cancelHandler, null, {"class": "btn"});
              dialog.render();
            }
          },
          onFailure: function(e) {
            that.handleException(e);
          }
        });
      },
      
      /**
       * is binded to tree.contextmenu, called when the user right clicks on a node.
       */
      __onNodeRightClick : function(e) {
        var $tree = $(this.getRawEl());
        $tree.tree('selectNode', e.node);
        
        var cm = this.getFactory().newContextMenu(e.node);
        var create = cm.addItem(this.localize("create"), "add", Mojo.Util.bind(this, this.__onContextCreateClick));
        var update = cm.addItem(this.localize("update"), "edit", Mojo.Util.bind(this, this.__onContextEditClick));
        var del = cm.addItem(this.localize("delete"), "delete", Mojo.Util.bind(this, this.__onContextDeleteClick));
        var refresh = cm.addItem(this.localize("refresh"), "refresh", Mojo.Util.bind(this, this.__onContextRefreshClick));
        
        if (e.node.termBusy) {
          create.setEnabled(false);
          update.setEnabled(false);
          del.setEnabled(false);
          refresh.setEnabled(false);
        }
        
        cm.render();
        
        cm.addDestroyEventListener(function() {
          $tree.tree("selectNode", null);
        });
      },
      
      /**
       * Finds all nodes associated with the given term id and sets them to busy, adding a busy indicator.
       * 
       * @param termId
       * @param bool
       */
      setTermBusy : function(termId, bool) {
        var nodes = this.__getNodesById(termId);

        for (var i = 0; i<nodes.length; ++i) {
          var node = nodes[i];
          
          this.setNodeBusy(node, bool);
        }
      },
      
      setNodeBusy : function(node, bool) {
        if (node.parent == null) {
          if (bool) {
            this._busydiv = this.getFactory().newElement("div");
            this._busydiv.addClassName("jqtree-node-busy");
            this.insertBefore(this._busydiv, this.getChildren()[0]);
            return;
          }
          else {
            if (this._busydiv.getParent() != null) {
              this._busydiv.destroy();
            }
          }
        }
        else {
          var el = $(node.element);
          
          if (bool) {
            node.termBusy = true;
            el.addClass("jqtree-loading");
          }
          else {
            node.termBusy = false;
            el.removeClass("jqtree-loading");
          }
        }
      },
      
      /**
       * Invokes the function fnDo for the provided node, and recursively all children of that node.
       * 
       * @param node
       * @param fnDo
       */
      doForNodeAndAllChildren : function(node, fnDo) {
        fnDo(node);
        
        for (var i = 0; i < node.children.length; ++i) {
          var child = node.children[i];
          
          if (!child.phantom) {
            if (child.children.length > 0) {
              this.doForNodeAndAllChildren(child, fnDo);
            }
            else {
              fnDo(child);
            }
          }
        }
      },
      
      doForTermAndAllChildren : function(termId, fnDo) {
        var nodes = this.__getNodesById(termId);
        
        for (var i = 0; i < nodes.length; ++i) {
          this.doForNodeAndAllChildren(nodes[i], fnDo);
        }
      },
      
      /**
       * is binded to jqtree's node move event.s
       */
      __onNodeMove : function(event) {
        var $thisTree = $(this.getRawEl());
        var movedNode = event.move_info.moved_node;
        var targetNode = event.move_info.target_node;
        var previousParent = event.move_info.previous_parent;
        var previousParentId = this.__getRunwayIdFromNode(previousParent);
        
        var movedNodeId = this.__getRunwayIdFromNode(movedNode);
        var targetNodeId = this.__getRunwayIdFromNode(targetNode);
        
        if (movedNodeId == this.rootTermId) {
          event.preventDefault();
          var ex = new com.runwaysdk.Exception("You cannot move the root node.");
          return;
        }
        
        var that = this;
        
        // User clicks Move on context menu //
        var moveHandler = function(mouseEvent, contextMenu) {
          
          var moveBizCallback = {
            onSuccess : function(relDTO) {
              that.parentRelationshipCache.removeRecordMatchingId(movedNodeId, previousParentId, that);
              that.parentRelationshipCache.put(movedNodeId, {parentId: targetNodeId, relId: relDTO.getId(), relType: relDTO.getType()});
              
              // Remove nodes from old relationship.
              var nodes = that.__getNodesById(movedNodeId);
              for (var i = 0; i<nodes.length; ++i) {
                $thisTree.tree(
                  'removeNode',
                  nodes[i]
                );
              }
              
              // Create nodes that represent the new relationship
              nodes = that.__getNodesById(targetNodeId);
              for (var i = 0; i<nodes.length; ++i) {
                that.__createTreeNode(movedNodeId, nodes[i]);
              }
              
//              if (that._config.checkable) {
//                jcf.customForms.replaceAll();
//              }
            },
            onFailure : function(ex) {
              that.doForNodeAndAllChildren(movedNode, function(node){that.setNodeBusy(node, false);});
              that.handleException(ex);
            }
          };
          Mojo.Util.copy(new Mojo.ClientRequest(moveBizCallback), moveBizCallback);
          
          this.doForNodeAndAllChildren(movedNode, function(node){that.setNodeBusy(node, true);});
          
          var parentRecord = this.parentRelationshipCache.getRecordWithParentId(movedNodeId, previousParentId, that);
          com.runwaysdk.Facade.moveBusiness(moveBizCallback, targetNodeId, movedNodeId, parentRecord.relId, parentRecord.relType);
        };
        
        // User clicks Copy on context menu //
        var copyHandler = function(mouseEvent, contextMenu) {
          
          var addChildCallback = {
            onSuccess : function(relDTO) {
              that.setNodeBusy(movedNode, false);
              
              that.parentRelationshipCache.put(movedNodeId, {parentId: targetNodeId, relId: relDTO.getId(), relType: relDTO.getType()});
              
              var nodes = that.__getNodesById(targetNodeId);
              for (var i = 0; i<nodes.length; ++i) {
                that.__createTreeNode(movedNodeId, nodes[i]);
              }
              
//              if (that._config.checkable) {
//                jcf.customForms.replaceAll();
//              }
            },
            onFailure : function(ex) {
              that.setNodeBusy(movedNode, false);
              that.handleException(ex);
            }
          };
          Mojo.Util.copy(new Mojo.ClientRequest(addChildCallback), addChildCallback);
          
          that.setNodeBusy(movedNode, true);
          
          var parentRecord = this.parentRelationshipCache.getRecordWithParentId(movedNodeId, this.__getRunwayIdFromNode(movedNode.parent), that);
          
          // The oldRelId is null which means that this actually does a copy.
          com.runwaysdk.Facade.moveBusiness(addChildCallback, targetNodeId, movedNodeId, null, parentRecord.relType);
        };
        
        var cm = this.getFactory().newContextMenu({childId: movedNodeId, parentId: targetNodeId});
        cm.addItem(this.localize("move"), "add", Mojo.Util.bind(this, moveHandler));
        cm.addItem(this.localize("copy"), "paste", Mojo.Util.bind(this, copyHandler));
        cm.render();
        
        event.preventDefault()
      },
      
      /**
       * Is binded to jqtree's node select (and deselect) event.
       */
      __onNodeSelect : function(e) {
        
      },
      
      /**
       * Is binded to jqtree's node open event and loads new nodes from the server with a getChildren request, if necessary.
       */
      __onNodeOpen : function(e) {
        var node = e.node;
        var nodeId = this.__getRunwayIdFromNode(node);
        var that = this;
        
        if (node.hasFetched == null || node.hasFetched == undefined) {
          this.refreshTerm(nodeId);
        }
      },
      
      /**
       * Fetches all the term's children from the server, drops all children of the node, and then repopulates the child nodes based on the TermAndRel objects receieved from the server.
       */
      refreshTerm : function(termId) {
        var that = this;
        var id = termId;
        
        this.setTermBusy(termId, true);
        
        var callback = new Mojo.ClientRequest({
          onSuccess : function(responseText) {
            var json = Mojo.Util.getObject(responseText);
            var termAndRels = com.runwaysdk.DTOUtil.convertToType(json.returnValue);
            
            var nodes = that.__getNodesById(termId);
            
            // Remove existing children
            for (var iNode = 0; iNode < nodes.length; ++iNode) {
              var node = nodes[iNode];
              var children = node.children.slice(0,node.children.length); // slice is used here to avoid concurrent modification, screwing up the loop.
              for (var i=0; i < children.length; i++) {
                $(that.getRawEl()).tree("removeNode", children[i]);
              }
            }
            
            // Create a node for every term we got from the server.
            for (var i = 0; i < termAndRels.length; ++i) {
              var $tree = $(that.getRawEl());
              var childId = termAndRels[i].getTerm().getId();
              
              var parentRecord = {parentId: termId, relId: termAndRels[i].getRelationshipId(), relType: termAndRels[i].getRelationshipType()};
              that.parentRelationshipCache.put(childId, parentRecord);
               
              that.termCache[childId] = termAndRels[i].getTerm();
              
              for (var iNode = 0; iNode < nodes.length; ++iNode) {
                var node = nodes[iNode];
                that.__createTreeNode(childId, node);
              }
            }
            
            that.setTermBusy(termId, false);
            
            if (that._config.checkable) {
              jcf.customForms.replaceAll();
            }
          },
          
          onFailure : function(err) {
            that.setTermBusy(termId, false);
            that.handleException(err);
            return;
          }
        });
        
        Mojo.Util.invokeControllerAction(this._config.termType, "getAllChildren", {parentId: termId, pageNum: 0, pageSize: 0}, callback);
      },
      
      /**
       * attempts to find the node in the cache, if the node does not exist in the cache it will request it from the server.
       * 
       * @returns
       *   @onSuccess com.runwaysdk.business.TermDTO term The requested term.
       * 
       * @param String termId The id of the requested term.
       * @param Object callback A callback object with onSuccess and onFailure methods.
       */
      __getTermFromId : function(termId, hisCallback) {
        var term = this.termCache[termId];
        
        var that = this;
        
        if (term == null) {
          // Request node from server
          var myCallback = {
            onSuccess : function(obj) {
              term = obj;
              that.termCache[term.getId()] = term;
              
              hisCallback.onSuccess(term);
            },
            
            onFailure : function(obj) {
              hisCallback.onFailure(obj);
            }
          };
          Mojo.Util.copy(new Mojo.ClientRequest(myCallback), myCallback);
          
          com.runwaysdk.Facade.get(myCallback, termId);
        }
        else {
          return hisCallback.onSuccess(term);
        }
      },
      
      /**
       * jqTree assumes that node id's are unique. If a term has multiple parents this isn't the case. Use this method to get a jqtree node from a term id.
       * 
       * @returns jqtreeNode[] or null
       */
      __getNodesById : function(nodeId) {
        if (nodeId === this.rootTermId) {
          return [$(this.getRawEl()).tree("getTree")];
        } 
        
        if (this.duplicateMap[nodeId] != null) {
          $thisTree = $(this.getRawEl());
          
          var duplicates = this.duplicateMap[nodeId];
          var nodes = [];
          
          for (var i = 0; i < duplicates.length; ++i) {
            var node = $thisTree.tree("getNodeById", duplicates[i]);
            
//            if (node == null) {
//              var ex = new com.runwaysdk.Exception("Expected duplicate node of index " + i + ".");
//              this.handleException(ex, true);
//              return;
//            }
            
            if (node != null) {
              nodes.push(node);
            }
          }
          
          return nodes;
        }
        else {
          var retVal = $(this.getRawEl()).tree("getNodeById", nodeId);
          return retVal == null ? null : [retVal];
        }
      },
      
      _getTermDisplayLabel : function(term) {
        var displayLabel = term.getDisplayLabel().getLocalizedValue();
        if (displayLabel == "" || displayLabel == null) {
          displayLabel = term.getId();
        }
        
        return displayLabel;
      },
      
      /**
       * creates a new jqTree node and appends it to the tree. This method will request the term from the server, to get the display label, if the term is not in the cache.
       */
      __createTreeNode : function(childId, parentNode, hasFetched) {
        var that = this;
        
        return this.__getTermFromId(childId, {
          onSuccess : function(childTerm) {
            var $thisTree = $(that.getRawEl());
            
            var duplicateTerm = $thisTree.tree("getNodeById", childId);
            
            var idStr = childId;
            if (duplicateTerm != null) {
              if (that.duplicateMap[childId] == null) {
                that.duplicateMap[childId] = [childId];
              }
              idStr = Mojo.Util.generateId();
              that.duplicateMap[childId].push(idStr);
            }
            
            var displayLabel = that._getTermDisplayLabel(childTerm);
            
            var node = null;
            if (parentNode == null || parentNode == undefined) {
              node = $thisTree.tree(
                'appendNode',
                {
                    label: displayLabel,
                    id: idStr,
                    runwayId: childId
                }
              );
            }
            else {
              node = $thisTree.tree(
                'appendNode',
                {
                    label: displayLabel,
                    id: idStr,
                    runwayId: childId
                },
                parentNode
              );
              
              if (!hasFetched) {
                var phantom = $thisTree.tree(
                  'appendNode',
                  {
                      label: "",
                      id: idStr + "_PHANTOM",
                      phantom: true,
                      runwayId: childId
                  },
                  node
                );
                node.phantomChild = phantom;
              }
              else {
                node.hasFetched = true;
              }
              
              $thisTree.tree("openNode", parentNode);
            }
            
            if (that._config.checkable) {
              jcf.customForms.replaceAll();
            }
            
            return node;
          },
          onFailure : function(err) {
            that.handleException(err);
            return;
          }
        });
      },
      
      __getRunwayIdFromNode : function(node) {
        if (node.parent == null) {
          // We were passed the root node. Since we didn't create the root node (jqtree did) we can't set an id on it.
          return this.rootTermId;
        }
        
        return node.runwayId;
      },
      
      render : function(parent) {
        
        var that = this;
        
        this.$render(parent);
        
        // Create the jqTree
        var $tree = $(this.getRawEl()).tree(this._config);
        
        this._boundedRightClick = Mojo.Util.bind(this, this.__onNodeRightClick)
        
        $tree.bind(
            'tree.open',
            Mojo.Util.bind(this, this.__onNodeOpen)
        );
        $tree.bind(
            'tree.move',
            Mojo.Util.bind(this, this.__onNodeMove)
        );
        $tree.bind(
            'tree.contextmenu',
            function(event) {
              that._boundedRightClick(event);
              event.preventDefault(); // This stops nodes from being selected when clicked on (which currently has no use)
            }
        );
        $tree.bind(
          'tree.click',
          function(event) {
            that._boundedRightClick(event);
            event.preventDefault(); // This stops nodes from being selected when clicked on (which currently has no use)
          }
        );
        
        this.refreshTerm(this.rootTermId);
      },
      
      /**
       * Returns the relationships that the term has with its parent. The relationships may be cached and the method may return
       * synchronously. The cache may or may not contain all relationships the term has with its parent.
       * 
       * @param com.runwaysdk.business.TermDTO or String (Id) term The term to remove from the tree.
       * @param Object callback A callback object with onSuccess and onFailure methods.
       * @returns com.runwaysdk.business.TermRelationship[] The relationships.
       */
  //    getRelationshipsWithParent : function(term, callback) {
  //      this.__assertPrereqs();
  //      this.requireParameter("term", term);
  //      this.requireParameter("callback", callback);
  //      
  //      var termId = (term instanceof Object) ? term.getId() : term;
  //      
  //      var that = this;
  //      
  //      var hisCallback = callback;
  //      var myCallback = {
  //        onSuccess : function(relationships) {
  //          hisCallback(relationships);
  //        },
  //        
  //        onFailure : function(err) {
  //          hisCallback.onFailure(err);
  //          return;
  //        }
  //      };
  //      Mojo.Util.copy(new Mojo.ClientRequest(myCallback), myCallback);
  //      
  //      if (this.parentRelationshipCache[termId] != null && this.parentRelationshipCache != undefined) {
  //        myCallback.onSuccess([this.parentRelationshipCache[termId]]);
  //      }
  //      else {
  //        com.runwaysdk.Facade.getParentRelationships(myCallback, termId, relationshipType);
  //      }
  //    },
    }
  });
  
  /**
   * @class com.runwaysdk.geodashboard.ontology.ParentRelationshipCache A parent relationship cache that maps a childId to known parent records. This class is used internally only.
   */
  var ParentRelationshipCache = Mojo.Meta.newClass('com.runwaysdk.geodashboard.ontology.ParentRelationshipCache', {
    
    IsAbstract : false,
    
    Instance : {
  
      /**
       * This cache maps a childId to an array of parents, where each parent is represented by a parentRecord.
       * 
       * A parentRecord is a javascript object : {parentId, relId, relType}
       * 
       */
      initialize : function()
      {
        // Map<childId, record[]>
        this.cache = {};
      },
      
      /**
       * @param childId
       * @param parentRecord
       */
      put : function(childId, record) {
        var cacheRecordArray = this.cache[childId] ? this.cache[childId] : [];
        
        // If the record is already in the cache, update it and return.
        for (var i = 0; i < cacheRecordArray.length; ++i) {
          if (cacheRecordArray[i].parentId === record.parentId && cacheRecordArray[i].relType === record.relType) {
            this.cache[childId][i].relId = record.relId;
            return;
          }
        }
        
        // else add the new record to the cache
        if (this.cache[childId] == null || this.cache[childId] == undefined) {
          this.cache[childId] = [];
        }
        
        this.cache[childId].push(record);
      },
      
      /**
       * Removes all records from the cache.
       */
      dump : function() {
        this.cache = {};
      },
      
      /**
       * Removes all parents in the cache for the given term id.
       */
      removeAll : function(termId) {
        this.cache[termId] = [];
        
        // Remove all children
//        for (var childId in this.cache) {
//          if (this.cache.hasOwnProperty(childId)) {
//            var records = this.cache[childId];
//            
//            for (var i = 0; i < records.length; ++i) {
//              if (records[i].parentId === termId) {
//                records[i].splice(i, 1);
//              }
//            }
//            
//            if (records.length === 0) {
//              delete this.cache[childId];
//            }
//          }
//        }
      },
      
      /**
       * Removes the specified parentRecord from the parentRecord[] matching the term id and the parent id.
       */
      removeRecordMatchingId : function(childId, parentId, treeInst) {
        var records = this.get(childId, treeInst);
        
        for (var i = 0; i < records.length; ++i) {
          if (records[i].parentId === parentId) {
            records.splice(i, 1);
            return;
          }
        }
        
        throw new com.runwaysdk.Exception("Unable to find a matching record to remove with childId[" + childId + "] and parentId[" + parentId + "].");
      },
      
      /**
       * @returns parentRecord[]
       */
      get : function(childId, treeInst) {
        var got = this.cache[childId];
        
        if (treeInst != null && (got == null || got == undefined)) {
          throw new com.runwaysdk.Exception("The term [" + childId + "] is not mapped to a parent record in the parentRelationshipCache.");
        }
        
        return this.cache[childId] ? this.cache[childId] : [];
      },
      
      /**
       * @returns parentRecord
       */
      getRecordWithParentId : function(childId, parentId, treeInst) {
        var parentRecords = this.get(childId, treeInst);
        
        for (var i = 0; i < parentRecords.length; ++i) {
          if (parentId === parentRecords[i].parentId) {
            return parentRecords[i];
          }
        }
        
        throw new com.runwaysdk.Exception("The ParentRelationshipCache is faulty, unable to find parent with id [" + parentId + "] in the cache. The child term in question is [" + childId + "] and that term has [" + parentRecords.length + "] parents in the cache.");
      }
    }
  });

})();
