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
  
/**
 * @class com.runwaysdk.ui.jquery.Tree A wrapper around JQuery widget jqTree to allow for integration with Term objects.
 * 
 * @constructs
 * @param obj
 *   @param String obj.nodeId The id of the div defined in html, specifying the location for the tree. The id is prefixed with #.
 *   @param Object obj.data Optional, a properly formatted data object as documented by jqTree.
 *   @param Boolean obj.dragDrop Optional, set to true to enable drag drop, false to disable. Default is false.
 */
var tree = Mojo.Meta.newClass('com.runwaysdk.ui.ontology.TermTree', {
  
  Extends : Widget,
  
  Instance : {
    
    initialize : function(config){
      
      config = config || {};
      config.el = config.nodeId || "div";
      
      this.$initialize(config.el);
      
      config.dragAndDrop = config.dragDrop;
      config.data = config.data || {};
      
      this._config = config;
      
      this.termCache = {};
      
      // jqtree assumes that id's are unique. For our purposes the id may map to multiple nodes.
      this.duplicateNum = {};
      
      
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
    setRootTerm : function(rootTerm, callback) {
      this.__assertRequire("rootTerm", rootTerm);
      
      if (!this.isRendered()) {
        var ex = new com.runwaysdk.Exception("The tree must be rendered before you can use this method.");
        this.handleException(ex);
        return;
      }
      
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
      
      var onSuccess = callback == null ? null : callback.onSuccess;
      var onFailure = callback == null ? null : callback.onFailure;
      
      this.__createTreeNode(this.rootTermId, null, onSuccess, onFailure);
    },
    
    /**
     * Registers a function to on term select.
     * 
     * @param Function callback A function with argument 'term', the selected term. 
     */
    registerOnTermSelect : function(callback) {
      this.__assertRequire("callback", callback);
      
      this.selectCallbacks.push(callback);
    },
    
    /**
     * Registers a function to on term deselect.
     * 
     * @param Function callback A function with argument 'term', the deselected term. 
     */
    registerOnTermDeselect : function(callback) {
      this.__assertRequire("callback", callback);
      
      this.deselectCallbacks.push(callback);
    },
    
    /**
     * Adds a child term to the tree under parent with the given relationship.
     * 
     * @param com.runwaysdk.business.TermDTO or String (id) child The child term that will be added to the tree.
     * @param com.runwaysdk.business.TermDTO or String (id) parent The parent term that the child will be appended under.
     * @param String relationshipType The relationship type that the child will be appended with. The relationship type must extend com.runwaysdk.business.TermRelationship.
     * @param Object callback A callback object with onSuccess and onFailure methods.
     */
    addChild : function(child, parent, relationshipType, callback) {
      this.__assertPrereqs();
      this.__assertRequire("child", child);
      this.__assertRequire("parent", parent);
      this.__assertRequire("relationshipType", relationshipType);
      this.__assertRequire("callback", callback);
      
      var childId = (child instanceof Object) ? child.getId() : child;
      var parentId = (parent instanceof Object) ? parent.getId() : parent;
      
      var $thisTree = $(this.getRawEl());
      var that = this;
      
      var parentNodes = this.__getNodesById(parentId);
      if (parentNodes == null || parentNodes == undefined) {
        var ex = new com.runwaysdk.Exception("The provided parent [" + parentId + "] does not exist in this tree.");
        this.handleException(ex);
        return;
      }
      
      var hisCallback = callback;
      var myCallback = {
        onSuccess : function(relDTO) {
        	var applyCallback = {
        	    onSuccess : function(appliedRelDTO) {
        	      var parentRecord = {parentId: parentId, relId: appliedRelDTO.getId(), relType: appliedRelDTO.getType()};
                that.parentRelationshipCache.put(childId, parentRecord);
        	      
                var fetchNodeCallback = {
                  onSuccess : function() {
                    for (var i = 0; i < parentNodes.length; ++i) {
                      that.__createTreeNode(childId, parentNodes[i]);
                    }
                    
                    hisCallback.onSuccess(appliedRelDTO);
                  },
                  onFailure : function(err) {
                    hisCallback.onFailure(err);
                  }
                }
                that.__getTermFromId(childId, fetchNodeCallback); // We're calling this here to force caching the node because createTreeNode will need it.
        	    },
        	    
        	    onFailure : function(obj) {
        	      hisCallback.onFailure(obj);
        	    }
        	}
        	Mojo.Util.copy(new Mojo.ClientRequest(applyCallback), applyCallback);
        	
        	relDTO.apply(applyCallback);
        },
        
        onFailure : function(obj) {
          hisCallback.onFailure(obj);
        }
      };
      Mojo.Util.copy(new Mojo.ClientRequest(myCallback), myCallback);
      
      com.runwaysdk.Facade.addChild(myCallback, parentId, childId, relationshipType);
    },
    
    /**
     * Removes the term and all its children from the tree and notifies the server to remove the relationship in the database.
     * 
     * @returns
     *    @onFailure com.runwaysdk.Exception Fails synchronously if the term is not mapped to a parent record in the parentRelationshipCache.  
     *    @onFailure com.runwaysdk.Exception Fails synchronously if the term is the root node.
     * 
     * @param com.runwaysdk.business.TermDTO or String (Id) term The term to remove from the tree.
     * @param com.runwaysdk.business.TermDTO or String (Id) parent The term's parent, to resolve ambiguities that arise when a term has multiple parents.
     * @param String relationshipType The relationship type that the term has with its parent.
     * @param Object callback A callback object with onSuccess and onFailure methods.
     */
    removeTerm : function(term, parent, callback) {
      this.__assertPrereqs();
      this.__assertRequire("term", term);
      this.__assertRequire("parent", parent);
      this.__assertRequire("callback", callback);
      
      var termId = (term instanceof Object) ? term.getId() : term;
      var parentId = (parent instanceof Object) ? parent.getId() : parent;
      
      if (termId === this.rootTermId) {
        var ex = new com.runwaysdk.Exception("You cannot delete the root node.");
        callback.onFailure(ex);
        return;
      }
      
      var parentRecord = this.parentRelationshipCache.getRecordWithParentId(termId, parentId, this);
      
      var that = this;
      
      var $thisTree = $(this.getRawEl());
      
      var hisCallback = callback;
      var deleteCallback = {
        onSuccess : function() {
          var nodes = that.__getNodesById(termId);
          for (var i = 0; i < nodes.length; ++i) {
            $thisTree.tree(
              'removeNode',
              nodes[i]
            );
          }
          
          that.parentRelationshipCache.removeAll(termId);
          that.termCache[termId] = null;
          
//          if (that.curSelected.id === termId) {
//            that.curSelected = null;
//          }
          
          hisCallback.onSuccess();
        },
        
        onFailure : function(err) {
          hisCallback.onFailure(err);
          return;
        }
      }
      Mojo.Util.copy(new Mojo.ClientRequest(deleteCallback), deleteCallback);
      
      com.runwaysdk.Facade.deleteChild(deleteCallback, parentRecord.relId);
    },
    
    /**
     * Internal, is binded to context menu option Refresh.
     */
    __onContextRefreshClick : function(contextMenu, contextMenuItem, mouseEvent) {
      var targetNode = contextMenu.getTarget();
      
      targetNode.hasFetched = null;
      
      // Node open will refresh.
      this.__onNodeOpen({node: targetNode});
    },
    
    /**
     * Internal, is binded to context menu option Create. 
     */
    __onContextCreateClick : function(contextMenu, contextMenuItem, mouseEvent) {
      var targetNode = contextMenu.getTarget();
      var targetId = this.__getRunwayIdFromNode(targetNode);
      
      var dialog = this.getFactory().newDialog("Create Term", {modal: true});
      
      
      // Form
      var form = this.getFactory().newForm("Create Form");
      
      var fNameTextInput = this.getFactory().newFormControl('text', 'displayLabel');
      form.addEntry("Display Label", fNameTextInput);
      
      dialog.appendContent(form);
      
      var that = this;
      
      var submitCallback = function() {
        var values = form.accept(that.getFactory().newFormControl('FormVisitor'));
        dialog.close();
        
        var applyCallback = {
          onSuccess : function(term) {
            var labelCallback = {
              onSuccess : function() {
                term.getDisplayLabel().localizedValue = values.get("displayLabel");
                
                var addChildCallback = {
                  onSuccess : function(relat) {
                    that.parentRelationshipCache.put(term.getId(), {parentId: targetId, relId: relat.getId(), relType: relat.getType()});
                  },
                  
                  onFailure : function(err) {
                    that.handleException(err);
                    return;
                  }
                };
                
                that.addChild(term, targetId, "com.runwaysdk.jstest.business.ontology.Sequential", addChildCallback);
                that.termCache[term.getId()] = term;
              },
              onFailure : function() {
                
              }
            };
            Mojo.Util.copy(new Mojo.ClientRequest(labelCallback), labelCallback);
            
            var displayLabel = term.getDisplayLabel();
            displayLabel.setValue("defaultLocale", values.get("displayLabel"));
            displayLabel.apply(labelCallback);
          },
          
          onFailure : function(err) {
            that.handleException(err);
            return;
          }
        };
        Mojo.Util.copy(new Mojo.ClientRequest(applyCallback), applyCallback);
        
        // FIXME : Don't hardcode this
        var al = new com.runwaysdk.jstest.business.ontology.Alphabet();
        
        al.apply(applyCallback);
      };
      dialog.addButton("Submit", submitCallback);
      
      var cancelCallback = function() {
        dialog.close();
      };
      dialog.addButton("Cancel", cancelCallback);
      
      dialog.render();
    },
    
    /**
     * Internal, is binded to context menu option Edit. 
     */
    __onContextEditClick : function(contextMenu, contextMenuItem, mouseEvent) {
      var node = contextMenu.getTarget();
      var termId = this.__getRunwayIdFromNode(node);
      
      var dialog = this.getFactory().newDialog("Edit Term", {modal: true});
      
      // Form
      var form = this.getFactory().newForm("Edit Form");
      
      var fNameTextInput = this.getFactory().newFormControl('text', 'displayLabel');
      form.addEntry("Display Label", fNameTextInput);
      
      dialog.appendContent(form);
      
      var that = this;
      
      var submitCallback = function() {
        var values = form.accept(that.getFactory().newFormControl('FormVisitor'));
        dialog.close();
        
        var getTermCallback = {
          onSuccess : function(term) {
            var lockCallback = {
              onSuccess : function(relat) {
                term.getDisplayLabel().setValue("defaultLocale", values.get("displayLabel"));
                
                var applyCallback = {
                  onSuccess : function(displayLabel) {
                    term.getDisplayLabel().localizedValue = values.get("displayLabel");
                    
                    var nodes = that.__getNodesById(term.getId());
                    for (var i = 0; i < nodes.length; ++i) {
                      $(that.getRawEl()).tree("updateNode", nodes[i], {label: values.get("displayLabel")});
                    }
                  },
                  onFailure : function(err) {
                    that.handleException(err);
                    return;
                  }
                }
                Mojo.Util.copy(new Mojo.ClientRequest(applyCallback), applyCallback);
//                term.apply(applyCallback);
                term.getDisplayLabel().apply(applyCallback);
              },
              
              onFailure : function(err) {
                that.handleException(err);
                return;
              }
            };
            Mojo.Util.copy(new Mojo.ClientRequest(lockCallback), lockCallback);
            
            term.lock(lockCallback);
          },
          
          onFailure : function(err) {
            that.handleException(err);
            return;
          }
        };
        
        that.__getTermFromId(termId, getTermCallback);
      };
      dialog.addButton("Submit", submitCallback);
      
      var cancelCallback = function() {
        dialog.close();
      };
      dialog.addButton("Cancel", cancelCallback);
      
      dialog.render();
    },
    
    /**
     * Internal, is binded to context menu option Delete. 
     */
    __onContextDeleteClick : function(contextMenu, contextMenuItem, mouseEvent) {
      var node = contextMenu.getTarget();
      var termId = this.__getRunwayIdFromNode(node);
      var parentId = this.__getRunwayIdFromNode(node.parent);
      var that = this;
      
      if (termId === this.rootTermId) {
        var ex = new com.runwaysdk.Exception("You cannot delete the root node.");
        this.handleException(ex);
        return;
      }
      
      var parentRecords = this.parentRelationshipCache.get(termId, this);
      
      var deleteCallback = {
        onSuccess : function() {
          // Intentionally empty
        },
        onFailure : function(err) {
          that.handleException(err);
          return;
        }
      }
      var deleteHandler = function() {
        that.removeTerm(termId, parentId, deleteCallback);
        dialog.close();
      };
      var performDeleteRelHandler = function() {
        var deleteRelCallback = {
          onSuccess : function() {
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
        that.termCache[termId].removeChildTerm(deleteRelCallback, parentRecord.relId);
        
        dialog.close();
      };
      var cancelHandler = function() {
        dialog.close();
      };
      
      if (parentRecords.length > 1) {
        var dialog = this.getFactory().newDialog("Delete Term", {modal: true, width: 600, height: 300});
        dialog.appendContent("This term has more than one parent. Do you want to delete the term:<br/>");
        dialog.appendContent(termId + "<br/><br/>");
        dialog.appendContent("all of its children, and the associated relationships or " +
            "do you want to only remove the relationship between the term and this particular parent?")
        dialog.addButton("Delete Term And Relationships", deleteHandler);
        dialog.addButton("Delete Relationship", performDeleteRelHandler);
        dialog.addButton("Cancel", cancelHandler);
        dialog.render();
      }
      else {
        var dialog = this.getFactory().newDialog("Delete Term", {modal: true, width: 550, height: 250});
        dialog.appendContent("Are you sure you want to delete the term:<br/>");
        dialog.appendContent(termId + "<br/><br/>");
        dialog.appendContent("and all of its children?")
        dialog.addButton("Delete Term", deleteHandler);
        dialog.addButton("Cancel", cancelHandler);
        dialog.render();
      }
    },
    
    /**
     * Internal, is binded to tree.contextmenu, called when the user right clicks on a node.
     */
    __onNodeRightClick : function(e) {
      var cm = this.getFactory().newContextMenu(e.node);
      cm.addItem("Create", "add", Mojo.Util.bind(this, this.__onContextCreateClick));
      cm.addItem("Edit", "edit", Mojo.Util.bind(this, this.__onContextEditClick));
      cm.addItem("Delete", "delete", Mojo.Util.bind(this, this.__onContextDeleteClick));
      cm.addItem("Refresh", "refresh", Mojo.Util.bind(this, this.__onContextRefreshClick));
      cm.render();
    },
    
    /**
     * Internal, is binded to jqtree's node move event.
     */
    __onNodeMove : function(event) {
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
            
            var nodes = that.__getNodesById(targetNodeId);
            for (var i = 0; i<nodes.length; ++i) {
              that.__createTreeNode(movedNodeId, nodes[i]);
            }
          },
          onFailure : function(ex) {
            that.handleException(ex);
          }
        };
        Mojo.Util.copy(new Mojo.ClientRequest(moveBizCallback), moveBizCallback);
        
        var parentRecord = this.parentRelationshipCache.getRecordWithParentId(movedNodeId, previousParentId, that);
        com.runwaysdk.Facade.moveBusiness(moveBizCallback, targetNodeId, movedNodeId, parentRecord.relId, parentRecord.relType);
      };
      
      // User clicks Copy on context menu //
      var copyHandler = function(mouseEvent, contextMenu) {
        
        var addChildCallback = {
          onSuccess : function(relDTO1) {
            var applyCallback = {
              onSuccess : function(relDTO2) {
                that.parentRelationshipCache.put(movedNodeId, {parentId: targetNodeId, relId: relDTO2.getId(), relType: relDTO2.getType()});
                
                var nodes = that.__getNodesById(targetNodeId);
                for (var i = 0; i<nodes.length; ++i) {
                  that.__createTreeNode(movedNodeId, nodes[i]);
                }
              },
              onFailure : function(err) {
                that.handleException(err);
              }
            }
            Mojo.Util.copy(new Mojo.ClientRequest(applyCallback), applyCallback);
            
            relDTO1.apply(applyCallback);
          },
          onFailure : function(ex) {
            that.handleException(ex);
          }
        };
        Mojo.Util.copy(new Mojo.ClientRequest(addChildCallback), addChildCallback);
        
        var parentRecord = this.parentRelationshipCache.getRecordWithParentId(movedNodeId, this.__getRunwayIdFromNode(movedNode.parent), that);
        com.runwaysdk.Facade.addChild(addChildCallback, targetNodeId, movedNodeId, parentRecord.relType);
      };
      
      var cm = this.getFactory().newContextMenu({childId: movedNodeId, parentId: targetNodeId});
      cm.addItem("Move", "add", Mojo.Util.bind(this, moveHandler));
      cm.addItem("Copy", "paste", Mojo.Util.bind(this, copyHandler));
      cm.render();
      
      event.preventDefault()
    },
    
    /**
     * Internal, Is binded to jqtree's node select (and deselect) event.
     */
    __onNodeSelect : function(e) {
      
    },
    
    /**
     * Internal, is binded to jqtree's node open event and loads new nodes from the server with a getChildren request, if necessary.
     */
    __onNodeOpen : function(e) {
      var node = e.node;
      var nodeId = this.__getRunwayIdFromNode(node);
      var that = this;
      
      if (node.hasFetched == null || node.hasFetched == undefined) {
        
        var callback = {
          onSuccess : function(termAndRels) {
            // Remove existing children
            var children = node.children.slice(0,node.children.length); // slice is used here to avoid concurrent modification, screwing up the loop.
            for (var i=0; i < children.length; i++) {
              $(that.getRawEl()).tree("removeNode", children[i]);
            }
            
            for (var i = 0; i < termAndRels.length; ++i) {
              var $tree = $(that.getRawEl());
              var termId = termAndRels[i].getTerm().getId();
              
              var parentRecord = {parentId: nodeId, relId: termAndRels[i].getRelationshipId(), relType: termAndRels[i].getRelationshipType()};
              that.parentRelationshipCache.put(termId, parentRecord);
               
              that.termCache[termId] = termAndRels[i].getTerm();
              
              that.__createTreeNode(termId, node);
            }
             
            var nodes = that.__getNodesById(nodeId);
            for (var i = 0; i < nodes.length; ++i) {
              if (nodes[i].phantomChild != null) {
                $(that.getRawEl()).tree("removeNode", nodes[i].phantomChild);
              }
            }
            
            node.hasFetched = true;
          },
          
          onFailure : function(err) {
            that.handleException(err);
            return;
          }
        };
        Mojo.Util.copy(new Mojo.ClientRequest(callback), callback);
        
        com.runwaysdk.Facade.getTermAllChildren(callback, nodeId, 0, 0);
      }
    },
    
    /**
     * Internal method, do not call.
     */
    __assertPrereqs : function() {
      if (!this.isRendered()) {
        var ex = new com.runwaysdk.Exception("The tree must be rendered before you can use this method.");
        this.handleException(ex);
      }
      if (this.rootTermId == null || this.rootTermId == undefined) {
        var ex = new com.runwaysdk.Exception("You must call setRootTerm first before you can use this method.");
        this.handleException(ex);
      }
    },
    
    __assertRequire : function(name, value) {
      try {
        Mojo.Util.requireParameter(name, value);
      }
      catch(ex) {
        this.handleException(ex);
      }
    },
    
    /**
     * Internal, attempts to find the node in the cache, if the node does not exist in the cache it will request it from the server.
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
        hisCallback.onSuccess(term);
      }
    },
    
    /**
     * jqTree assumes that node id's are unique. If a term has multiple parents this isn't the case. Use this method to get a jqtree node from a term id.
     * 
     * @returns jqtreeNode[] or null
     */
    __getNodesById : function(nodeId) {
      if (this.duplicateNum[nodeId] != null) {
        $thisTree = $(this.getRawEl());
        
        var duplicateNum = this.duplicateNum[nodeId];
        var nodes = [];
        
        for (var i = 0; i < duplicateNum; ++i) {
          var node = $thisTree.tree("getNodeById", nodeId + "_" + i);
          
//          if (node == null) {
//            var ex = new com.runwaysdk.Exception("Expected duplicate node of index " + i + ".");
//            this.handleException(ex);
//            return;
//          }
          
          if (node != null) {
            nodes.push(node);
          }
        }
        
        return nodes;
      }
      else {
        var retVal = $(this.getRawEl()).tree("getNodeById", nodeId + "_0");
        return retVal == null ? null : [retVal];
      }
    },
    
    /**
     * Internal, creates a new jqTree node and appends it to the tree. This method will request the term from the server, to get the display label, if the term is not in the cache.
     */
    __createTreeNode : function(childId, parentNode, theirOnSuccess, theirOnFailure) {
      var that = this;
      
      this.__getTermFromId(childId, {
        onSuccess : function(childTerm) {
          var $thisTree = $(that.getRawEl());
          
          var duplicateTerm = $thisTree.tree("getNodeById", childId + "_0");
          
          var duplicateIndex = that.duplicateNum[childId] == null ? (duplicateTerm == null ? 0 : 1) : that.duplicateNum[childId];
          
          var idStr = childId + "_" + duplicateIndex;
          
          var displayLabel = childTerm.getDisplayLabel().getLocalizedValue();
          if (displayLabel == "" || displayLabel == null) {
            displayLabel = childTerm.getId();
          }
          
          var node = null;
          if (parentNode == null || parentNode == undefined) {
            node = $thisTree.tree(
              'appendNode',
              {
                  label: displayLabel,
                  id: idStr
              }
            );
          }
          else {
            node = $thisTree.tree(
              'appendNode',
              {
                  label: displayLabel,
                  id: idStr
              },
              parentNode
            );
            var phantom = $thisTree.tree(
              'appendNode',
              {
                  label: "",
                  id: idStr + "_PHANTOM",
                  phantom: true
              },
              node
            );
            node.phantomChild = phantom;
          }
          
          if (duplicateTerm != null) {
            that.duplicateNum[childId] == null ? that.duplicateNum[childId] = 1 : true;
            that.duplicateNum[childId] = that.duplicateNum[childId] + 1;
          }
          
          if (Mojo.Util.isFunction(theirOnSuccess)) {
            theirOnSuccess(childTerm);
          }
        },
        onFailure : function(err) {
          if (!Mojo.Util.isFunction(theirOnFailure)) {
            that.handleException(err);
            return;
          }
          else {
            theirOnFailure(err);
          }
        }
      });
    },
    
    __getRunwayIdFromNode : function(node) {
      return node.id.substr(0, node.id.indexOf("_"));
    },
    
    render : function(parent) {
      
      this.$render(parent);
      
      // Create the jqTree
      var $tree = $(this.getRawEl()).tree(this._config);
      
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
          Mojo.Util.bind(this, this.__onNodeRightClick)
      );
    }
    
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
//      this.__assertRequire("term", term);
//      this.__assertRequire("callback", callback);
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
 * @class com.runwaysdk.ui.ontology.ParentRelationshipCache A parent relationship cache that maps a childId to known parent records. This class is used internally only.
 */
var ParentRelationshipCache = Mojo.Meta.newClass('com.runwaysdk.ui.ontology.ParentRelationshipCache', {
  
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
     * Removes all parents in the cache for the given term id. 
     */
    removeAll : function(childId) {
      this.cache[childId] = [];
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
      
      var ex = new com.runwaysdk.Exception("Unable to find a matching record to remove with childId[" + childId + "] and parentId[" + parentId + "].");
      treeInst.handleException(ex);
      return;
    },
    
    /**
     * @returns parentRecord[]
     */
    get : function(childId, treeInst) {
      var got = this.cache[childId];
      
      if (treeInst != null && childId === treeInst.rootTermId) {
        var ex = new com.runwaysdk.Exception("That operation is invalid on the root node.");
        treeInst.handleException(ex);
        return;
      }
      
      if (treeInst != null && (got == null || got == undefined)) {
        var ex = new com.runwaysdk.Exception("The term [" + childId + "] is not mapped to a parent record in the parentRelationshipCache.");
        treeInst.handleException(ex);
        return;
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
      
      var ex = new com.runwaysdk.Exception("The ParentRelationshipCache is faulty, unable to find parent with id [" + parentId + "] in the cache. The child term in question is [" + childId + "] and that term has [" + parentRecords.length + "] parents in the cache.");
      treeInst.handleException(ex);
      return;
    },
    
    render : function(parent) {
      
    }
  }
});

})();
