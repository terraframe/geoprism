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
          checkable: false,
          
          crud: {
            create: {
              width: 730,
              height: 320
            },
            update: {
              width: 730,
              height: 320
            }
          },
          
          jsTree: {
            "plugins" : ["dnd", "crrm", "ui", "contextmenu"],
            "core" : {
              data : this.__preserveThisBind(this, this.__treeWantsData),
              check_callback: true,
              "load_open" : true,
              "themes" : {
                "icons": false
//                "variant": "large"
              }
            },
            "dnd" : {
              drop_check : function(){alert("drop check");}
            },
            "crrm" : {
              "move" : {
                "check_move" : function(){alert("drop check");}
              }
            }
          }
        };
        this._config = Mojo.Util.deepMerge(defaultConfig, config);
        
        if (this._config.checkable) {
          this._config.jsTree.plugins.push("checkbox");
        }
        
        // Add checkboxes
        if (this._config.checkable && this._config.onCreateLi == null) {
           this._config.onCreateLi = Mojo.Util.bind(this, this.__onCreateLi);
        }
        
        this.$initialize(this._config.el, this._config);
        
        this.__setRootTerm(config.rootTerm);
        
        this.termCache = {};
        
        // jqtree assumes that id's are unique. For our purposes the id may map to multiple nodes.
        // This map maps the runwayId = [ generatedId's ]
        this.duplicateMap = new com.runwaysdk.structure.HashMap();
        this.genToRunway = {}; // Maps key = generatedId, value = runwayId
        
        this.parentRelationshipCache = new ParentRelationshipCache();
        
        this.selectCallbacks = [];
        this.deselectCallbacks = [];
        this.busyNodes = new com.runwaysdk.structure.HashSet();
      },
      
      __preserveThisBind : function(thisRef, func){
        var args = [].splice.call(arguments, 2, arguments.length);
        return function(){
          var callArgs = args.concat([].splice.call(arguments, 0, arguments.length));
          callArgs.splice(0,0,this);
          
          var retval = func.apply(thisRef, callArgs);
          return retval;
        };
      },
      
      getCheckedTerms : function(rootNode, appendArray) {
        appendArray = appendArray || [];
        rootNode = rootNode || this.getImpl().tree("getTree");
        
        for (var i = 0; i < rootNode.children.length; ++i) {
          var child = rootNode.children[i];
          
          if (child.checkBox != null) {
            if (child.checkBox.isChecked()) {
              appendArray.push(this.__getRunwayIdFromNode(child));
            }
            
            this.getCheckedTerms(child, appendArray);
          }
        }
        
        return appendArray;
      },
      
      __onCheck : function(event) {
        var checkBox = event.getCheckBox();
        var node = checkBox.node;
        var termId = this.__getRunwayIdFromNode(node);
        
        if (!node.skipCheckChildren) {
          this.doForNodeAndAllChildren(node, function(childNode) {
            if (childNode != node) {
              childNode.skipCheckParent = true;
              childNode.checkBox.setChecked(checkBox.isChecked());
              childNode.skipCheckParent = false;
            }
          });
        }
        
        if (node.parent != null && node.parent.checkBox != null && !node.skipCheckParent) {
          var checkedCount = 0;
          var partialChecked = 0;
          
          var siblings = node.parent.children;
          for (var i = 0; i < siblings.length; ++i) {
            if (siblings[i].checkBox.isChecked()) {
              checkedCount++;
            }
            else if (siblings[i].checkBox.isPartialChecked()) {
              partialChecked++;
            }
          }
          
          if (node.parent.children.length === checkedCount) {
            node.parent.checkBox.setChecked(true);
          }
          else {
            if ((checkedCount + partialChecked) > 0) {
              node.parent.skipCheckChildren = true;
              node.parent.checkBox.setChecked("partial");
              node.parent.skipCheckChildren = false;
            }
            else {
              node.parent.skipCheckChildren = true;
              node.parent.checkBox.setChecked(false);
              node.parent.skipCheckChildren = false;
            }
          }
        }
        
        if (node.checkBox.hasClassName("partialcheck")) {
          var checkedCount = 0;
          
          for (var i = 0; i < node.children.length; ++i) {
            if (node.children[i].checkBox.isChecked() || node.children[i].checkBox.isPartialChecked()) {
              checkedCount++;
            }
          }
          
          if (checkedCount == 0) {
            node.checkBox.removeClassName("partialcheck");
          }
        }
      },
      
      __onCreateLi : function(node, $li) {
        var fac = this.getFactory();
        
        var li = fac.newElement($li[0]);
        var title = li.getChildren()[1];
        var checkBox = node.checkBox;
        if (checkBox == null) {
          checkBox = fac.newCheckBox({classes: ["jqtree-checkbox"]});
          
          if (node.parent != null && node.parent.checkBox != null && node.parent.checkBox.isChecked()) {
            checkBox.setChecked(true);
          }
          
          checkBox.addOnCheckListener(Mojo.Util.bind(this, this.__onCheck));
          checkBox.render();
          
          node.checkBox = checkBox;
          checkBox.node = node;
        }
        
        li.insertBefore(checkBox, title);
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
        var $thisTree = this.getImpl();
        
        // TODO: Remove children from the parent relationship cache.
//        var node = nodes[0];
//        var children = nodes[0].children;
//        for (var i = 0; i < children.length; ++i) {
//          
//        }
        
        $thisTree.jstree(
          'delete_node',
          nodes
        );
        
        delete this.termCache[termId];
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
        var $thisTree = this.getImpl();
        
        var deleteCallback = new Mojo.ClientRequest({
          onSuccess : function(retval) {
            that.doForTermAndAllChildren(termId, function(node){that.setNodeBusy(node, false);});
            that.refreshTreeAfterDeleteTerm(termId);
          },
          
          onFailure : function(err) {
            that.doForTermAndAllChildren(termId, function(node){that.setNodeBusy(node, false);});
            that.handleException(err);
            return;
          }
        });
        
        this.doForTermAndAllChildren(termId, function(node){that.setNodeBusy(node, true)});
        
        Mojo.Util.invokeControllerAction(this._config.termType, "delete", {dto: term}, deleteCallback);
      },
      
      createTerm : function(parentId, targetNode) {
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
          onSuccess : function(responseObj) {
            var termAndRel = that.__responseToTNR(responseObj);
            var term = termAndRel.getTerm();
            var relId = termAndRel.getRelationshipId();
            var relType = termAndRel.getRelationshipType();
            
            that.parentRelationshipCache.put(term.getId(), {parentId: parentId, relId: relId, relType: relType});
            that.termCache[term.getId()] = term;
            
            if (targetNode != null) {
              var $tree = that.getImpl();
              for (var i = 0; i < parentNodes.length; ++i) {
                var node = parentNodes[i];
                if ($tree.jstree("is_loaded", node)) {
                  if (targetNode.id === node.id) {
                    $tree.jstree("open_node", node, function(node2){
                      return function(){
                        that.__createTreeNode(term.getId(), node2, true);
                        $tree.jstree("open_node", node2, false);
                      };
                    }(node));
                  }
                  else {
                    that.__createTreeNode(term.getId(), node, true);
                  }
                }
                else if (targetNode.id === node.id) {
                  that.setNodeBusy(node, true);
                  $tree.jstree("load_node", node);
                  $tree.jstree("open_node", node, function(node2){
                    return function(){ that.setNodeBusy(node2, false); };
                  }(node));
                }
              }
            }
            else {
              that._impl.jstree("open_node", targetNode);
              that.refreshTerm(parentId);
            }
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
        var that = this;
        
        this._impl.jstree("open_node", targetNode, function(){
          that.refreshTerm(that.__getRunwayIdFromNode(targetNode));
        });
      },
      
      /**
       * is binded to context menu option Create. 
       */
      __onContextCreateClick : function(contextMenu, contextMenuItem, mouseEvent) {
        var targetNode = contextMenu.getTarget();
        var targetId = this.__getRunwayIdFromNode(targetNode);
        this.createTerm(targetId, targetNode);
      },
      
      /**
       * is binded to context menu option Edit. 
       */
      __onContextEditClick : function(contextMenu, contextMenuItem, mouseEvent) {
        var node = contextMenu.getTarget();
        var termId = this.__getRunwayIdFromNode(node);
        var that = this;
        var parentId = this.getParentRunwayId(node);
        var $tree = this.getImpl();
        
        var config = {
          type: this._config.termType,
          viewParams: {id: termId, parentId: parentId, relationshipType: ""},
          action: "update",
          actionParams: {parentId: parentId, relationshipType: ""},
          onSuccess : function(responseObj) {
            var term = that.__responseToTerm(responseObj);
            that.termCache[term.getId()] = term;
            
            var nodes = that.__getNodesById(term.getId());
            for (var i = 0; i < nodes.length; ++i) {
              $tree.jstree("rename_node", nodes[i], that._getTermDisplayLabel(term));
            }
            
            that.setTermBusy(termId, false);
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
//        var parentId = this.__getRunwayIdFromNode(node.parent);
        var parentId = this.getParentRunwayId(node);
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
              var nodes = that.__getNodesById(termId);
              for (var i = 0; i < nodes.length; ++i) {
                if (that.getParentRunwayId(nodes[i]) == parentId) {
                  that.getImpl().jstree("delete_node", nodes[i]);
                }
              }
              that.parentRelationshipCache.removeRecordMatchingId(termId, parentId, that);
              
              // Children of universals are appended to the root node, so refresh the root node.
//              that.refreshTerm(that.rootTermId);
            },
            onFailure : function(err) {
              that.handleException(err);
              return;
            }
          };
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
            var termLabel = that._getTermDisplayLabel(term);
            
            var deleteLabel = that.localize("delete") + " " + termMdLabel;
            
            if (parentRecords.length > 1) {
              var deleteMultiParentDescribe = that.localize("deleteMultiParentDescribe").replace("${termMdLabel}", termMdLabel).replace("${termMdLabel}", termMdLabel).replace("${termLabel}", termLabel);
              deleteMultiParentDescribe = deleteMultiParentDescribe.replace("${parentLabel}", that._getTermDisplayLabel(that.termCache[parentId]));
              
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
      __onNodeRightClick : function(event, object) {
        var $tree = this.getImpl();
        
        var node = object.node;
//        var term = this.termCache[this.__getRunwayIdFromNode(node)];
        
        if (this._cm != null && !this._cm.isDestroyed()) {
          this._cm.destroy();
        }
        
        this._cm = this.getFactory().newContextMenu(node);
        var create = this._cm.addItem(this.localize("create"), "add", Mojo.Util.bind(this, this.__onContextCreateClick));
        var update = this._cm.addItem(this.localize("update"), "edit", Mojo.Util.bind(this, this.__onContextEditClick));
        var del = this._cm.addItem(this.localize("delete"), "delete", Mojo.Util.bind(this, this.__onContextDeleteClick));
        var refresh = this._cm.addItem(this.localize("refresh"), "refresh", Mojo.Util.bind(this, this.__onContextRefreshClick));
        
        if (this.busyNodes.contains(node.id)) {
          create.setEnabled(false);
          update.setEnabled(false);
          del.setEnabled(false);
          refresh.setEnabled(false);
        }
        
        this._cm.render();
        
        this._cm.addDestroyEventListener(function() {
          $tree.jstree("deselect_node", node);
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
      
      getParentId : function(node) {
        var parent = this.getImpl().jstree("get_parent", node);
        
        if (parent === node.id) {
          return "#";
        }
        
        return parent;
      },
      
      getParentRunwayId : function(node) {
        var id = this.getParentId(node);
        var node = this.getImpl().jstree("get_node", id);
        return this.__getRunwayIdFromNode(node);
      },
      
      setNodeBusy : function(node, bool) {
        
        if (node === "#") { return; }
        
        if (bool) {
          this.busyNodes.add(node.id);
        }
        else {
          this.busyNodes.remove(node.id);
        }
        
//        if (node.parent == null) {
//          if (bool) {
//            this._busydiv = this.getFactory().newElement("div");
//            this._busydiv.addClassName("jqtree-node-busy");
//            this.insertBefore(this._busydiv, this.getChildren()[0]);
//            return;
//          }
//          else {
//            if (this._busydiv.getParent() != null) {
//              this._busydiv.destroy();
//            }
//          }
//        }
//        else {
//          var el = $(node.element);
//          
//          if (bool) {
//            node.termBusy = true;
//            el.addClass("jqtree-loading");
//          }
//          else {
//            node.termBusy = false;
//            el.removeClass("jqtree-loading");
//          }
//        }
      },
      
      /**
       * Invokes the function fnDo for the provided node, and recursively all children of that node.
       * 
       * @param node
       * @param fnDo
       */
      doForNodeAndAllChildren : function(node, fnDo) {
        fnDo.call(this, node);
        
        var children = this.getImpl().jstree("get_children_dom", node);
        for (var i = 0; i < children.length; ++i) {
          var child = this.getImpl().jstree("get_node", node.children[i], false);
          
          this.doForNodeAndAllChildren(child, fnDo);
        }
      },
      
      doForTermAndAllChildren : function(termId, fnDo) {
        var nodes = this.__getNodesById(termId);
        
        for (var i = 0; i < nodes.length; ++i) {
          this.doForNodeAndAllChildren(nodes[i], fnDo);
        }
      },
      
      doForTerm : function(termId, fnDo) {
        var nodes = this.__getNodesById(termId);
        
        for (var i = 0; i < nodes.length; ++i) {
          fnDo.call(this, nodes[i]);
        }
      },
      
      doForTermAndImmediateChildren : function(termId, fnDo) {
        var nodes = this.__getNodesById(termId);
        var $tree = this.getImpl();
        
        for (var i = 0; i < nodes.length; ++i) {
          var node = nodes[i];
          fnDo.call(this, node);
          
          var children = $tree.jstree("get_children_dom", node);
          for (var i = 0; i < children.length; ++i) {
            var child = children[i];
            
            fnDo.call(this, child);
          }
        }
      },
      
      __findInsertIndex : function(label, newParent) {
        var index = 0;
        var $tree = this.getImpl();
        
        var children = this.getChildren(newParent);
        
//        children.sort(function(a,b){
//          var nodeA = $tree.jstree("get_node", a);
//          var nodeB = $tree.jstree("get_node", b);
//          return nodeA.text.localeCompare(nodeB.text);
//        });
        
        
        for (var i = 0; i < children.length; ++i) {
          if (children[i].text.localeCompare(label) > 0) {
            break;
          }
          else {
            index++;
          }
        }
        
        return index;
      },
      
      __copyNodeToParent : function(node, parent) {
        var that = this;
        
        this.__createTreeNode(this.__getRunwayIdFromNode(node), parent, false, function(newCopiedNode){
          var children = that.getChildren(node);
          var len = children.length;
          for (var i = 0; i < len; ++i) {
            that.__copyNodeToParent(children[i], newCopiedNode);
          }
        }, true);
        
        this._impl.jstree("open_node", parent);
      },
      
      /**
       * is binded to jqtree's node move event.s
       */
      __onNodeMove : function(jqEvent, treeEvent) {
        if (this._isMoving || this._isMoving2) { return; }
        
        var $thisTree = this.getImpl();
        var movedNode = treeEvent.node;
        var targetNode = $thisTree.jstree("get_node", treeEvent.parent);
        var previousParent = $thisTree.jstree("get_node", treeEvent.old_parent);
        var previousParentId = this.__getRunwayIdFromNode(previousParent);
        
        var movedNodeId = this.__getRunwayIdFromNode(movedNode);
        var targetNodeId = this.__getRunwayIdFromNode(targetNode);
        
        var that = this;
        
        // Really lame jsTree.. the move has already happend and there's no way to prevent it, so we're going to roll it back right here and then perform it later manually.
        var index = this.__findInsertIndex(movedNode.text, previousParent);
        this._isMoving = true;
        $thisTree.jstree("move_node", movedNode, previousParent, index, false, true);
        this._isMoving = false;
        
        if (this.busyNodes.contains(movedNode)) {
          return false;
        }
        
        // User clicks Move on context menu //
        var moveHandler = function(mouseEvent, contextMenu) {
          
          var moveBizCallback = {
            onSuccess : function(relDTO) {
              that.parentRelationshipCache.removeRecordMatchingId(movedNodeId, previousParentId, that);
              that.parentRelationshipCache.put(movedNodeId, {parentId: targetNodeId, relId: relDTO.getId(), relType: relDTO.getType()});
              
              that.doForNodeAndAllChildren(movedNode, function(node){that.setNodeBusy(node, false);});
              
              that._isMoving2 = true;
              var ind = that.__findInsertIndex(movedNode.text, targetNode);
              $thisTree.jstree("move_node", movedNode, targetNode, ind, function(){
                $thisTree.jstree("open_node", targetNode, false);
              }, true);
              that._isMoving2 = false;
              
              // Remove nodes from old relationship.
//              var nodes = that.__getNodesById(movedNodeId); // We actually don't want to delete copies here, only move the node we've selected.
//              $thisTree.jstree(
//                'delete_node',
//                movedNode
//              );
              
              // Create nodes that represent the new relationship
//              nodes = that.__getNodesById(targetNodeId);
//              for (var i = 0; i<nodes.length; ++i) {
//                that.__createTreeNode(movedNodeId, nodes[i]);
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
                that.__copyNodeToParent(movedNode, nodes[i]);
              }
//              that._impl.jstree("load_node", targetNode, function(){
//                that._impl.jstree("open_node", targetNode);
//              });
            },
            onFailure : function(ex) {
              that.setNodeBusy(movedNode, false);
              that.handleException(ex);
            }
          };
          Mojo.Util.copy(new Mojo.ClientRequest(addChildCallback), addChildCallback);
          
          that.setNodeBusy(movedNode, true);
          
          var parentRecord = this.parentRelationshipCache.getRecordWithParentId(movedNodeId, this.getParentRunwayId(movedNode), that);
          
          // The oldRelId is null which means that this actually does a copy.
          com.runwaysdk.Facade.moveBusiness(addChildCallback, targetNodeId, movedNodeId, null, parentRecord.relType);
        };
        
        var cm = this.getFactory().newContextMenu({childId: movedNodeId, parentId: targetNodeId});
        cm.addItem(this.localize("move"), "add", Mojo.Util.bind(this, moveHandler));
        cm.addItem(this.localize("copy"), "paste", Mojo.Util.bind(this, copyHandler));
        cm.render();
        
//        jqEvent.preventDefault();
        return false;
      },
      
      /**
       * Fetches all the term's children from the server, drops all children of the node, and then repopulates the child nodes based on the TermAndRel objects receieved from the server.
       */
      refreshTerm : function(termId) {
        var that = this;
        var id = termId;
        
        var $tree = this.getImpl();
        
        var nodeArray = this.__getNodesById(termId);
        
        var len = nodeArray.length;
        for (var i = 0; i < len; ++i) {
          if ($tree.jstree("is_open", nodeArray[i]) || $("#"+nodeArray[i].id).hasClass("jstree-leaf")) {
            nodeArray[i].synonymNode = null;
            this.setNodeBusy(nodeArray[i], true);
            $tree.jstree("load_node", nodeArray[i], function(node){
              return function(){ that.setNodeBusy(node, false); };
            }(nodeArray[i]));
          }
        }
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
      __getNodesById : function(runwayId) {
        if (runwayId === this.rootTermId) {
          return ["#"];
        }
        
        var $tree = this.getImpl();
        
        if (this.duplicateMap.get(runwayId) != null) {
          var duplicates = this.duplicateMap.get(runwayId);
          var nodes = [];
          
          for (var i = 0; i < duplicates.length; ++i) {
            var node = $tree.jstree("get_node", duplicates[i], false);
            
//            if (node == null || node === false) {
//              var ex = new com.runwaysdk.Exception("Expected duplicate node of index " + i + ".");
//              throw ex;
//            }
            
            if (node != null && node !== false) {
              nodes.push(node);
            }
          }
          
          return nodes;
        }
        else {
          var retVal = $tree.jstree("get_node", runwayId, false);
          
          if (retVal == false) {
            throw new com.runwaysdk.Exception("A node with id '" + runwayId + "' does not exist in this tree.");
          }
          
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
       * Retrieves the term with id termId from the termCache and then creates its representation in the tree.
       */
      __createTreeNode : function(termId, parentNode, isNodeClosed, callback, dontSetNodeChildren, appendData) {
        var that = this;
        
        var term = this.termCache[termId];
        if (term == null) {
          throw new com.runwaysdk.Exception("Term with id '" + termId + "' not found.");
        }
        
        var $thisTree = that.getImpl();
        
        var idStr = that.__getUniqueIdForTerm(termId);
        
        var displayLabel = that._getTermDisplayLabel(term);
        
        if (parentNode == null || parentNode == "#") {
          parentNode = $thisTree.jstree("get_node", "#");
        }
        
        var data = {runwayId: termId};
        if (appendData != null) {
          Mojo.Util.merge(appendData, data);
        }
        
        var node = { state:{opened: !isNodeClosed}, text: displayLabel, id: idStr, data: data };
        
        if (dontSetNodeChildren !== true) {
          node.children = isNodeClosed;
        }
        
        // God damn it jsTree you buggy piece of shit
        if (parentNode.id === "#") {
          parentNode.parents = [];
        }
        
        node = $thisTree.jstree(
          'create_node',
          parentNode,
          node,
          this.__findInsertIndex(displayLabel, parentNode),
          callback, false
        );
        
        return node;
      },
      
      getChildren : function(nodeId) {
        var $tree = this.getImpl();
        var nodeChildren = [];
        var domChildren = $tree.jstree("get_children_dom", nodeId);
        var len = domChildren.length;
        for (var i = 0; i < len; ++i) {
          var node = $tree.jstree("get_node", domChildren[i]);
          
          if (node != null && node != false) {
            nodeChildren.push(node);
          }
        }
        return nodeChildren;
      },
      
      __getRunwayIdFromNode : function(node) {
        if (node === "#" || node.id === "#") {
          return this.rootTermId;
        }
        
        if (node.data.runwayId != null) {
          return node.data.runwayId;
        }
        
        throw new com.runwaysdk.Exception();
      },
      
      // Subclasses of TermTree can override this if they're returning a view. (Like GeoEntity). Just convert the view to a TNR.
      __responseToTNR : function(responseObj) {
        return responseObj;
      },
      
      __responseToTerm : function(responseObj) {
        return responseObj;
      },
      
      __getUniqueIdForTerm : function(termId) {
        var $tree = this.getImpl();
        var duplicateTerm = $tree.jstree("get_node", termId, false);
        var duplicates = this.duplicateMap.get(termId);
        var idStr = termId;
        if ( (duplicateTerm != null && duplicateTerm != false) || duplicates != null ) {
          if (duplicates == null) {
            this.duplicateMap.put(termId, [termId]);
          }
          idStr = Mojo.Util.generateId();
          this.duplicateMap.get(termId).push(idStr);
        }
        this.genToRunway[idStr] = termId;
        
        return idStr;
      },
      
      /**
       * Fixes jsTree's broken/buggy recursive children structure which exists on all nodes and doesn't get updated properly.
       * 
       * @param node Must be the root tree node.
       * @param children_deep Do not provide this internal parameter used only for recursive calls.
       */
//      __fixNodeChildren : function(node, children_deep) {
//        children_deep = children_deep || [];
//        var $tree = this.getImpl();
//        
//        var children_shallow = [];
//        var children = this.getChildren(node);
//        for (var i = 0; i < children.length; ++i) {
//          children_deep.push(children[i].id);
//          var childDeep = [];
//          this.__fixNodeChildren($tree.jstree("get_node", children[i]), childDeep);
//          children_deep = children_deep.concat(childDeep);
//          children_shallow.push(children[i].id);
//        }
//        
//        node.children = children_shallow;
//        node.children_d = children_deep;
//      },
      
      getRelationships : function() {
        
      },
      
      __treeWantsData : function(treeThisRef, parent, jsTreeCallback) {
        var that = this;
        
        var parentNodeId = parent.id;
        var parentTermId = this.__getRunwayIdFromNode(parent);
        var parentTerm = this.termCache[parentTermId];
        
        var callback = new Mojo.ClientRequest({
          onSuccess : function(responseText) {
            var json = Mojo.Util.getObject(responseText);
            var objArray = com.runwaysdk.DTOUtil.convertToType(json.returnValue);
            var termAndRels = [];
            for (var i = 0; i < objArray.length; ++i) {
              termAndRels.push(that.__responseToTNR(objArray[i]));
            }
            var $tree = that.getImpl();
            
            // Delete all existing children
            var children = $tree.jstree("get_children_dom", parent);
            for (var i = 0; i < children.length; ++i) {
              var child = children[i];
              if (child.id != "") {
                var runwayId = that.genToRunway[child.id];
                
                if (runwayId != null) {
                  var duplicates = that.duplicateMap.get(runwayId);
                  if (duplicates != null) {
                    var indexOf = duplicates.indexOf(child.id);
                    if (indexOf > -1) {
                      duplicates.splice(indexOf, 1);
                      delete that.genToRunway[child.id];
                    }
                  }
                }
              }
            }
            
            // Create a json object representing our TermAndRel to pass to jstree.
            var json = [];
            for (var i = 0; i < termAndRels.length; ++i) {
              var tnr = termAndRels[i];
              var termId = termAndRels[i].getTerm().getId();
              
              var parentRecord = {parentId: parentTermId, relId: termAndRels[i].getRelationshipId(), relType: termAndRels[i].getRelationshipType()};
              that.parentRelationshipCache.put(termId, parentRecord);
              
              var term = termAndRels[i].getTerm();
              that.termCache[termId] = term;
              
              // Generate a unique id for the node.
              var idStr = that.__getUniqueIdForTerm(termId);
              
              var relType = Mojo.Util.replaceAll(tnr.getRelationshipType(), ".", "-");
              
//              if (i > 3) {
//                relType = "com-runwaysdk-system-gis-geo-IsA";
//              }
              
              var treeNode = {
                  text: that._getTermDisplayLabel(term),
                  id: idStr, state: {opened: false}, children: true,
                  data: { runwayId: termId },
                  li_attr: {'class' : relType} // Add the relationshipType to the li's class
              };
              json.push(treeNode);
            }
            
            jsTreeCallback.call(treeThisRef, json);
            
            // There's no "onCreateLi" event for jsTree, so invoke our function that creates checkboxes
//            var children = $tree.jstree("get_children_dom", parent);
//            for (var i = 0; i < children.length; ++i) {
//              var child = children[i];
//              if (child.id != "") {
//                that.__onCreateLi(child, $("#"+child.id));
//              }
//            }
            
            // This code is to fix a bug in jstree.
            if (json.length === 0) {
//              var parentId = that.getParentId(that.getImpl().jstree("get_node", parentNodeId));
              
//              var node = $tree.jstree("get_node", parentNodeId);
              
//              if (!node.dontClobberChildren) {
//                node.parent = parentId;
//                node.parents = [parentId];
//                node.children = [];
//                node.children_d = [];
                $tree.jstree("redraw_node", parentNodeId, false);
//              }
            }
          },
          
          onFailure : function(err) {
            jsTreeCallback.call(this, []);
            that.handleException(err);
            return;
          }
        });
        
        Mojo.Util.invokeControllerAction(this._config.termType, "getDirectDescendants", {parentId: parentTermId, relationshipTypes: [this._config.relationshipType], pageNum: 0, pageSize: 0}, callback);
      },
      
      getImpl : function() {
        return this._impl;
      },
      
      render : function(parent) {
        
        var that = this;
        
        this.$render(parent);
        
        // Create jsTree
        this._impl = $(this.getRawEl()).jstree(this._config.jsTree);
        
        this._boundedRightClick = Mojo.Util.bind(this, this.__onNodeRightClick);
        
//        this._impl.on(
//            'select_node.jstree',
//            function(event, object) {
//              that._boundedRightClick(event, object);
//            }
//        );
        
        this._impl.on(
            'show_contextmenu.jstree',
            that._boundedRightClick
        );
        
        this._impl.on(
            'move_node.jstree',
            Mojo.Util.bind(this, this.__onNodeMove)
        );
      }
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
      },
      
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
      }
    }
  });

})();
