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
    "deleteDescribe" : "Are you sure you want to delete '${termLabel}'?",
    "export" : "Export"
  });
  
  var tree = Mojo.Meta.newClass('com.runwaysdk.geodashboard.ontology.TermNodeLabel', {
    Instance : {
      initialize : function(label) {
        this._label = label;
      },      
      getLocalizedValue : function() {
        return this._label;
      },
      getValue : function() {
        return this._label;
      }
    }    
  });
  
  var tree = Mojo.Meta.newClass('com.runwaysdk.geodashboard.ontology.TermNode', {
    Instance : {
      initialize : function(id, label) {
        this._id = id;
        this._label = new com.runwaysdk.geodashboard.ontology.TermNodeLabel(label);
      },
        
      getId : function() {
        return this._id;
      },
        
      getDisplayLabel : function() {
        return this._label;
      }
    }    
  });
  
  /**
   * @class com.runwaysdk.geodashboard.ontology.TermTree A wrapper around JQuery widget jqTree to allow for integration with Term objects.
   * 
   * @constructs
   * @param Object config
   *   @param String   config.termType The typename of the term this tree will act upon.
   *   @param String   config.relationshipTypes The relationships this tree will act upon.
   *   @param Object[] config.rootTerms An array of objects. Each object contains:
   *     @param Boolean config.rootTerms[i].selectable This is the boolean flag defined on the AttributeRoot and it denotes whether or not to display the root in the tree.
   *     @param TermDTO config.rootTerms[i].term Optional The root Term. If selectable is true then its recommended that you pass the DTO instead of the id (it avoids an extra unnecessary ajax request later).
   *     @param String config.rootTerms[i].termId Optional The id of the root Term. If you don't provide the term then you must provide the termId. Its recommended to use this if you know that selectable is false. If you're unsure then give us the DTO.
   */
  var tree = Mojo.Meta.newClass(termTreeName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(config) {
        
        config = config || {};
        this.requireParameter("termType", config.termType, "string");
        this.requireParameter("relationshipTypes", config.relationshipTypes, "array");
        this.requireParameter("rootTerms", config.rootTerms, "array");
        this.requireParameter("exportMenuType", config.exportMenuType, "string");
        
        var defaultConfig = {
          el: "div",
          data: [], // This parameter is required for jqTree, otherwise it tries to load data from a url.
          dragAndDrop: true,
          selectable: true,
          checkable: false,
          editable: false,
          onCanMove: this.canMove,
          onCanMoveTo: this.canMoveTo,
          autoOpen: false,
          openFolderDelay: 5000,
          crud: {
            create: {
              width: 730,
              height: 320
            },
            update: {
              width: 730,
              height: 320
            }
          }
        };
        this._config = Mojo.Util.deepMerge(defaultConfig, config);
        
        // Add checkboxes
        if (this._config.checkable && this._config.onCreateLi == null) {
          this._config.onCreateLi = Mojo.Util.bind(this, this.__onCreateLi);
        }
        
        this.$initialize(this._config.el, this._config);
        
        this.termCache = {};
        
        // jqtree assumes that id's are unique. For our purposes the id may map to multiple nodes.
        // This map maps the runwayId = [ generatedId's ]
        this._nodeMap = new com.runwaysdk.structure.HashMap();
        
        this.parentRelationshipCache = new ParentRelationshipCache();
        
        this.selectCallbacks = [];
        this.deselectCallbacks = [];
      },
      
      _addNodeMapping : function(termId, nodeId) {
        if (this._nodeMap.get(termId) == null) {
          this._nodeMap.put(termId, []);
        }
        
        this._nodeMap.get(termId).push(nodeId);
      },
      
      _removeNodeMapping : function(termId) {
        this._nodeMap.remove(termId);
      },
      
      /*
       * Controls whether a node can be moved
       */
      canMove : function(node)
      {
        // restrict the ability to move the root node
        if(! node.parent.parent){
          return false;
        }
        else{
          return true;
        }
      },
      
      
      /*
       *  Controls whether a node can be moved to a specific parent.
       *  Allow all by default
       */
      canMoveTo : function(moved_node, target_node, position)
      {
        return true;
      },
      
      
      /**
       * Loops over all the roots and loads either the root itself or the root's children into the tree (depending on rootTermConfig.selectable)
       */
      refreshRoots : function()
      {
        var that = this;
        this.rootTermConfigs = new com.runwaysdk.structure.HashMap();
        
        if (this._config.rootTerms.length === 0) { throw new com.runwaysdk.Exception("Invalid TermTree configuration. You must provide at least one root term."); }
        
        for (var i = 0; i < this._config.rootTerms.length; ++i)
        {
          var rootTerm = this._config.rootTerms[i];
          if (Mojo.Util.isObject(rootTerm))
          {
            var term = rootTerm.term;
          
            if (term instanceof com.runwaysdk.business.TermDTO || term instanceof com.runwaysdk.geodashboard.ontology.TermNode)
            {
              rootTerm.termId = term.getId();
              this.rootTermConfigs.put(rootTerm.termId, rootTerm);
              this.termCache[term.getId()] = term;
              
              if (rootTerm.selectable)
              {
                that.__createTreeNode(rootTerm.termId, null, false);
                that.refreshTerm(rootTerm.termId);
              }
              else
              {
                that.refreshTerm(rootTerm.termId);
              }
            }
            else if (Mojo.Util.isString(rootTerm.termId))
            {
              // We have to have selectable terms in the cache because we need their display label.
              if (rootTerm.selectable)
              {
                // Request node from server
                var myCallback = {
                  onSuccess : function(term) {
                    that.termCache[term.getId()] = term;
                    rootTerm.term = term;
                    that.rootTermConfigs.put(rootTerm.termId, rootTerm);
                    that.__createTreeNode(rootTerm.termId, null, false);
                    that.refreshTerm(term.getId());
                  },
                  
                  onFailure : function(err) {
                    that.handleException(err);
                  }
                };
                Mojo.Util.copy(new Mojo.ClientRequest(myCallback), myCallback);
                
                com.runwaysdk.Facade.get(myCallback, rootTerm.termId);
              }
              else
              {
                that.rootTermConfigs.put(rootTerm.termId, rootTerm);
                that.refreshTerm(rootTerm.termId);
              }
            }
            else
            {
              throw new com.runwaysdk.Exception("Invalid TermTree configuration. config.rootTerms[" + i + "] must provide either a TermDTO or a term id.");
            }
          }
          else
          {
            throw new com.runwaysdk.Exception("Invalid TermTree configuration. config.rootTerms[" + i + "] must be an object.");
          }
        }
      },
      
      /**
       * Returns an array containing the id of every term in the tree which has been checked.
       */
      getCheckedTerms : function(rootNode, appendArray) {
        appendArray = appendArray || [];
        rootNode = rootNode || $(this.getRawEl()).tree("getTree");
        
        for (var i = 0; i < rootNode.children.length; ++i) {
          var child = rootNode.children[i];
          
          if (!child.phantom && child.checkBox != null) {
            
            // If a node is checked then we do not need to check its children nodes, because they are implicitly checked
            if (child.checkBox.isChecked()) {
              var termId = this.__getRunwayIdFromNode(child);
              
              appendArray.push(termId);
            }
            else {
              this.getCheckedTerms(child, appendArray);              
            }            
          }
        }
        
        return appendArray;
      },
      
      setCheckedTerms : function(termIds) {
        for(var i = 0; i< termIds.length; i++) {          
          var termId = termIds[i];          
          var nodes = this.__getNodesById(termId);
          
          if(nodes != null) {            
            for(var j = 0; j < nodes.length; j++) {
              var node = nodes[j];
              node.skipCheckParent = false;
              node.checkBox.setChecked(true);
              
              this.__setCheck(nodes[j], true);
            }          
          }
        }
      },
      
      __setCheck : function (node, checked) {
        if (!node.skipCheckChildren) {
          this.doForNodeAndAllChildren(node, function(childNode) {
            if (childNode != node) {
              childNode.skipCheckParent = true;
              childNode.checkBox.setChecked(checked);
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
      
      __onCheck : function(event) {
        var checkBox = event.getCheckBox();
        var checked = checkBox.isChecked();
        var node = checkBox.node;
        
        this.__setCheck(node, checked);
      },
      
      __onCreateLi : function(node, $li) {
        if (!node.phantom) {
          var fac = this.getFactory();
          
          var title = fac.newElement($li.find('.jqtree-title')[0]).getParent();
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
          
          title.insertBefore(checkBox, title.getChildren()[0]);
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
          $thisTree.tree('removeNode',nodes[i]);
        }
        
        delete this.termCache[termId];
      },
      
      /**
       * Notifies the server to delete the term and then updates the tree by removing the node.
       */
      deleteTerm : function(termId, parent) {
        this.requireParameter("termId", termId, "string");
        
        var term = this.termCache[termId]; 
        
        var that = this;
        
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
        
        this.doForTermAndAllChildren(termId, function(node){that.setNodeBusy(node, true);});
        
        Mojo.Util.invokeControllerAction(this._config.termType, "delete", {dto: term}, deleteCallback);
      },
      
      /**
       * Callback handler for a successful creation of a new term.  This method may be overwritten
       * in sub classes to provide more functionality.
       */
      _handleCreateTerm : function (parentId, parentNodes, responseObj) {
        var termAndRel = this.__responseToTNR(responseObj);
        var term = termAndRel.getTerm();
        var relId = termAndRel.getRelationshipId();
        var relType = termAndRel.getRelationshipType();
          
        this.parentRelationshipCache.put(term.getId(), {parentId: parentId, relId: relId, relType: relType});
        this.termCache[term.getId()] = term;
          
        for (var i = 0; i < parentNodes.length; ++i) {
          this.__createTreeNode(term.getId(), parentNodes[i], true);
        }
      },
      
      createTerm : function(parentId, relType, parentNodes) {
        this.requireParameter("parentId", parentId, "string");
        
        if(parentNodes == null) {
          parentNodes = this.__getNodesById(parentId);
        }
        
        var that = this;                
        
        if (parentNodes == null) {
          var ex = new com.runwaysdk.Exception("The provided parent [" + parentId + "] does not exist in this tree.");
          this.handleException(ex);
          return;
        }
        
        if (relType == null) {
          relType = this._config.relationshipTypes[0];
        }
        
        var config = {
          type: this._config.termType,
          viewParams: {parentId: parentId, relationshipType: relType},
          action: "create",
          actionParams: {parentId: parentId, relationshipType: relType},
          onSuccess : function(responseObj) {
            that._handleCreateTerm(parentId, parentNodes, responseObj);                        
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
        
        this.refreshTerm(this.__getRunwayIdFromNode(targetNode));
      },
      
      /**
       * is binded to context menu option Export.
       */
      __onContextExportClick : function(contextMenu, contextMenuItem, mouseEvent) {
        var targetNode = contextMenu.getTarget();
        
        this.exportTerm(this.__getRunwayIdFromNode(targetNode));
      },
      
      /**
       * @param parentTerm String The Runway Id of the term to export.
       */
      exportTerm : function(parentTerm) {
        var that = this;
        
        var newType = eval("new " + this._config.exportMenuType + " ()");
        var title = newType.getMd().getDisplayLabel();
        
        var config = {
          type: this._config.exportMenuType,
          title: title,
          viewParams: {},
          action: "export",
          actionParams: {parentTerm: parentTerm},
          width: 560,
          onSuccess : function(responseObj) {
          },
          onFailure : function(e) {
            that.handleException(e);
          }
        };
        
        new com.runwaysdk.ui.RunwayControllerFormDialogDownloader(config).render();
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
       * Callback handler for a successful update of a term.  This method may be overwritten
       * in sub classes to provide additional functionality.
       */
      _handleUpdateTerm : function (oldTermId, responseObj) {
        var term = this.__responseToTerm(responseObj);
        var label = this._getTermDisplayLabel(term);
        var newTermId = term.getId();
        
        this.setTermBusy(oldTermId, false);
          
        // Update the node's Id from the id we've received from the server (since if the ids are deterministic it may have changed)
        var nodes = this.__getNodesById(oldTermId);        
        
        this._removeNodeMapping(oldTermId);
        this.termCache[oldTermId] = null;
        
        for (var i = 0; i < nodes.length; ++i) {
          this._addNodeMapping(newTermId, node.id);
          
          this.getImpl().tree("updateNode", nodes[i], {label:label, runwayId:term.getId()});
        }
          
        this.termCache[newTermId] = term;        
      },
      
      /**
       * is binded to context menu option Edit. 
       */
      __onContextEditClick : function(contextMenu, contextMenuItem, mouseEvent) {
        var node = contextMenu.getTarget();
        var termId = this.__getRunwayIdFromNode(node);
        var that = this;
        var parentId = this.__getRunwayIdFromNode(node.parent);
        
        that.setTermBusy(termId, true);
        
        var config = {
          type: this._config.termType,
          viewParams: {id: termId, parentId: parentId, relationshipType: ""},
          action: "update",
          actionParams: {parentId: parentId, relationshipType: ""},
          onSuccess : function(responseObj) {
            that._handleUpdateTerm(termId, responseObj);
          },
          onFailure : function(e) {
            that.setTermBusy(termId, false);
            that.handleException(e);
          },
          onCancel : function() {
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
        var relType = this._getRelationshipForNode(node, node.parent);
        var that = this;
        var dialog = null;
        
        if (this.rootTermConfigs.containsKey(termId)) {
          var ex = new com.runwaysdk.Exception("You cannot delete a root node.");
          this.handleException(ex);
          return;
        }
        
        var parentRecords = this.parentRelationshipCache.get(termId, this);
        
        var deleteHandler = function() {
          that.deleteTerm(termId, parentId);
          dialog.close();
        };
        var performDeleteRelHandler = function() {
          var parentRecord = that.parentRelationshipCache.getRecordWithParentIdAndRelType(termId, parentId, relType);
          
          var deleteRelCallback = {
            onSuccess : function() {
              var nodes = that.__getNodesById(termId);
              for (var i = 0; i < nodes.length; ++i) {
                if (that.__getRunwayIdFromNode(nodes[i].parent) == parentId && relType === that._getRelationshipForNode(nodes[i], nodes[i].parent)) {
                  $(that.getRawEl()).tree("removeNode", nodes[i]);
                }
              }
              that.parentRelationshipCache.removeRecordMatchingRelId(termId, parentRecord.relId);
            },
            onFailure : function(err) {
              that.handleException(err);
              return;
            }
          }
          Mojo.Util.copy(new Mojo.ClientRequest(deleteRelCallback), deleteRelCallback);
          
          com.runwaysdk.system.ontology.TermUtil.removeLink(deleteRelCallback, termId, parentId, relType);
          
          dialog.close();
        };
        var cancelHandler = function() {
          dialog.close();
        };
        
        var term = this.termCache[termId];
//        this.__getTermFromId(termId, {
//          onSuccess: function(term) {
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
//          },
//          onFailure: function(e) {
//            that.handleException(e);
//          }
//        });
      },
      
      getImpl : function()
      {
        return $(this.getRawEl());
      },
      
      _createNodeRightClickMenu : function(event) {
        var items = [];
        items.push({label:this.localize("create"), id:"add", handler:Mojo.Util.bind(this, this.__onContextCreateClick)});
        items.push({label:this.localize("update"), id:"edit", handler:Mojo.Util.bind(this, this.__onContextEditClick)});        
        items.push({label:this.localize("delete"), id:"delete", handler:Mojo.Util.bind(this, this.__onContextDeleteClick)});        
        items.push({label:this.localize("refresh"), id:"refresh", handler:Mojo.Util.bind(this, this.__onContextRefreshClick)});  
        items.push({label:this.localize("export"), id:"export", handler:Mojo.Util.bind(this, this.__onContextExportClick)});
          
        return items;
      },
      
      /**
       * is binded to tree.contextmenu, called when the user right clicks on a node.
       */
      __onNodeRightClick : function(e) {
        var $tree = $(this.getRawEl());
        $tree.tree('selectNode', e.node);
        
        var items = this._createNodeRightClickMenu(e);
        
        var cm = this.getFactory().newContextMenu(e.node);
        
        for(var i = 0; i < items.length; i++) {
          var item = items[i];
          
          var menuItem = cm.addItem(item.label, item.id, item.handler);
          
          if (e.node.termBusy || item.enabled === false) {
            menuItem.setEnabled(false);
          }
        }
        
        cm.render();
        
        cm.addDestroyEventListener(function() {
          $tree.tree("selectNode", null);
        });
      },
      
      isNodeBusy : function(node)
      {
        return node.termBusy;
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
            el.find(".jqtree-toggler").css("color", "transparent"); // hide the arrow toggle when the busy spinner is present
          }
          else {
            node.termBusy = false;
            el.removeClass("jqtree-loading");
            el.find(".jqtree-toggler").css("color", "black");
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
      
      doForTerm : function(termId, fnDo) {
        var nodes = this.__getNodesById(termId);
        
        for (var i = 0; i < nodes.length; ++i) {
          fnDo(nodes[i]);
        }
      },
      
      doForTermAndImmediateChildren : function(termId, fnDo) {
        var nodes = this.__getNodesById(termId);
        
        for (var i = 0; i < nodes.length; ++i) {
          var node = nodes[i];
          fnDo(node);
          
          for (var i = 0; i < node.children.length; ++i) {
            var child = node.children[i];
            
            if (!child.phantom) {
              fnDo(child);
            }
          }
        }
      },
      
      _createNodeMoveMenu : function(event) {
        
        var $thisTree = $(this.getRawEl());
        var movedNode = event.move_info.moved_node;
        var targetNode = event.move_info.target_node;
        var previousParent = event.move_info.previous_parent;
        var previousParentId = this.__getRunwayIdFromNode(previousParent);
              
        var movedNodeId = this.__getRunwayIdFromNode(movedNode);
        var targetNodeId = this.__getRunwayIdFromNode(targetNode);        
                    
        var that = this;
          
        // User clicks Move on context menu //
        var moveHandler = function(mouseEvent, contextMenu) {
            
          var oldRelType = this._getRelationshipForNode(movedNode, previousParent);
          var parentRecord = this.parentRelationshipCache.getRecordWithParentIdAndRelType(movedNodeId, previousParentId, oldRelType);
          var relType = this._getRelationshipForNode(movedNode, targetNode);
            
          var moveBizCallback = new Mojo.ClientRequest({
            onSuccess : function(relDTO) {
              // Remove nodes from old relationship.              
              that.parentRelationshipCache.removeRecordMatchingRelId(movedNodeId, parentRecord.relId);
                
              var nodes = that.__getNodesById(movedNodeId);
              for (var i = 0; i<nodes.length; ++i) {
                $thisTree.tree(
                  'removeNode',
                  nodes[i]
                );
              }

              if(targetNode.hasFetched) {                
                // Create nodes that represent the new relationship
                that.parentRelationshipCache.put(movedNodeId, {parentId: targetNodeId, relId: relDTO.getId(), relType: relDTO.getType()});
                  
                var nodes = that.__getNodesById(targetNodeId);
                for (var i = 0; i<nodes.length; ++i) {
                  that.__createTreeNode(movedNodeId, nodes[i]);
                }
              }
              else {
                // Refresh the node
                var termId = that.__getRunwayIdFromNode(targetNode);
                    
                that.refreshTerm(termId, null, [targetNode]);
              }
            },
            onFailure : function(ex) {
              that.doForNodeAndAllChildren(movedNode, function(node){that.setNodeBusy(node, false);});
              that.handleException(ex);
            }
          });
          
          this.doForNodeAndAllChildren(movedNode, function(node){that.setNodeBusy(node, true);});
            
          com.runwaysdk.system.ontology.TermUtil.addAndRemoveLink(moveBizCallback, movedNodeId, previousParentId, parentRecord.relType, targetNodeId, relType);
        };
          
        // User clicks Copy on context menu //
        var copyHandler = function(mouseEvent, contextMenu) {
            
          var addChildCallback = new Mojo.ClientRequest({
            onSuccess : function(relDTO) {
              that.setNodeBusy(movedNode, false);
              
              if(targetNode.hasFetched) {                
                that.parentRelationshipCache.put(movedNodeId, {parentId: targetNodeId, relId: relDTO.getId(), relType: relDTO.getType()});
                
                var nodes = that.__getNodesById(targetNodeId);
                for (var i = 0; i<nodes.length; ++i) {
                  that.__createTreeNode(movedNodeId, nodes[i]);
                }
              }
              else {
                // Refresh the node
                var termId = that.__getRunwayIdFromNode(targetNode);
                  
                that.refreshTerm(termId, null, [targetNode]);
              }
            },
            onFailure : function(ex) {
              that.setNodeBusy(movedNode, false);
              that.handleException(ex);
            }
          });
            
          that.setNodeBusy(movedNode, true);            
            
          var relType = that._getRelationshipForNode(movedNode, targetNode);
          com.runwaysdk.system.ontology.TermUtil.addLink(addChildCallback, movedNodeId, targetNodeId, relType);
        };
          
        var items = [];
        items.push({label:this.localize("move"), image:"add", handler:Mojo.Util.bind(this, moveHandler)});
        items.push({label:this.localize("copy"), image:"paste", handler:Mojo.Util.bind(this, copyHandler)});
        
        return items;
      },
      
      /**
       * is binded to jqtree's node move event.s
       */
      __onNodeMove : function(event) {
        var movedNode = event.move_info.moved_node;
        var targetNode = event.move_info.target_node;
              
        var movedNodeId = this.__getRunwayIdFromNode(movedNode);
        var targetNodeId = this.__getRunwayIdFromNode(targetNode);

        if (this.rootTermConfigs.containsKey(movedNodeId)) {
          event.preventDefault();
              
          var ex = new com.runwaysdk.Exception("You cannot move a root node.");
          return;
        }
        
        var items = this._createNodeMoveMenu(event);
      
        var cm = this.getFactory().newContextMenu({childId: movedNodeId, parentId: targetNodeId});
        
        for(var i = 0; i < items.length; i++) {
          var item = items[i];
          
          cm.addItem(item.label, item.image, item.handler);
        }
        
        cm.render();
        
        event.preventDefault();
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
        
        if (node.hasFetched == null || node.hasFetched == undefined || node.hasFetched == false) {
          this.refreshTerm(nodeId, null, [node]);
        }
      },
      
      // Subclasses of TermTree can override this if they're returning a view. (Like GeoEntity). Just convert the view to a TNR.
      __responseToTNR : function(responseObj) {
        return responseObj;
      },
      
      __responseToTerm : function(responseObj) {
        return responseObj;
      },
      
      // Provided for overrides
      refreshTermHookin : function(parentNode) {
        
      },
      
      /**
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
                $(that.getRawEl()).tree("removeNode", children[i]);
              }
            }
            
            // Create a node for every term we got from the server.
            for (var i = 0; i < termAndRels.length; ++i) {
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
      
      isRootTermId : function(termId) {      
        if(termId != null) {
          return this.rootTermConfigs.containsKey(termId);        
        }
        
        return true;
      },
      
      /**
       * Returns all of the tree nodes which correspond to a term.  A term with multiple parents 
       * will appear in the tree multiple times.  Thus it a term may have multiple tree nodes.
       * 
       * @returns jqtreeNode[] or null
       */
      __getNodesById : function(termId) {
        
        if (this.rootTermConfigs.containsKey(termId)) {
          var config = this.rootTermConfigs.get(termId);
          
          if (!config.selectable)
          {
            return [this.getImpl().tree("getTree")];
          }
        }
        
        var tree = $(this.getRawEl());

        if (this._nodeMap.get(termId) != null) {
          
          var nodeIds = this._nodeMap.get(termId);
          var nodes = [];
          
          for (var i = 0; i < nodeIds.length; ++i) {
            var node = tree.tree("getNodeById", nodeIds[i]);
                        
            if (node != null) {
              nodes.push(node);
            }
          }
          
          return nodes;
        }
        
        return null;
      },
      
      _getTermDisplayLabel : function(term) {
        var displayLabel = term.getDisplayLabel().getLocalizedValue();
        if (displayLabel == "" || displayLabel == null) {
          displayLabel = term.getId();
        }
        
        return displayLabel;
      },
      
      /**
       * Returns true if the children of the node have been loaded.
       */
      hasLoadedNode : function(node)
      {
        return node.hasFetched;
      },
      
      _getRelationshipForNode : function(movedNode, newParent) {
        return this._config.relationshipTypes[0];
      },
      
      _createNodes : function (parentNode, nodes) {
       
        for(var i = 0; i < nodes.length; i++) {
          var node = nodes[i];

          if(this.__getNodesById(node.id) == null) {
            
            // Create the cached term
            var term = new com.runwaysdk.geodashboard.ontology.TermNode(node.id, node.label);
          
            // Populate the caches
            this.termCache[term.getId()] = term;
          
            if(parentNode == null) { 
            
              // Create the cached root term
              var rootTerm = {term:term, termId: term.getId(), selectable:true};
              this.rootTermConfigs.put(term.getId(), rootTerm);
            }
            
            if(node.parentRecord != null) {              
              this.parentRelationshipCache.put(node.id, node.parentRecord);
            }
            
            this.__createTreeNode(term.getId(), parentNode, node.fetched, false, true);
          }
          else if(node.parentRecord != null){
            // This might be an existing node with multiple parents
            // Thus we need to check if the node has already been added
            // to this parent.  If not we need to create a new tree ndoe
            // for this relationship.
            
            if(!this.parentRelationshipCache.contains(node.id, node.parentRecord)) {
              this.parentRelationshipCache.put(node.id, node.parentRecord);          
                
              this.__createTreeNode(node.id, parentNode, node.fetched, false, true);
            }
          }
                    
          var treeNodes = this.__getNodesById(node.id);
          
          for(var j = 0; j < treeNodes.length; j++) {
            var treeNode = treeNodes[j];

            if(node.children.length > 0) {              
              this._createNodes(treeNodes[j], node.children);
              
              treeNode.hasFetched = true;              
              
              if(treeNode.phantomChild != null) {
                $(this.getRawEl()).tree("removeNode", treeNode.phantomChild); 
                
                treeNode.phantomChild == null;
              }
            }
          }
        }
      },
      
      /**
       * creates a new jqTree node and appends it to the tree. This method will request the term from the server, to get the display label, if the term is not in the cache.
       */
      __createTreeNode : function(childId, parentNode, hasFetched, config, hide) {
        var that = this;
        var tree = $(that.getRawEl());
        var childTerm = this.termCache[childId];
                    
        var nodeId = Mojo.Util.generateId();
            
        var defaultConfig = {
          id: nodeId,
          runwayId: childId
        };
            
        config = Mojo.Util.deepMerge(defaultConfig, config || {});
            
        if (config.label == null) {
          config.label = that._getTermDisplayLabel(childTerm);
        }
            
        var node = null;
        
        if (parentNode == null || parentNode == undefined) {
          node = tree.tree('appendNode',config);
        }
        else {
          node = tree.tree('appendNode',config,parentNode);
              
          if (!hasFetched) {
            var phantom = tree.tree('appendNode',{
              label: "",
              id: nodeId + "_PHANTOM",
              phantom: true,
              runwayId: childId
            }, node);
            
            node.phantomChild = phantom;
          }
              
          if(hide == null || !hide ) {                  
            tree.tree("openNode", parentNode);                
          }
        }
            
        node.hasFetched = hasFetched;
        
        this._addNodeMapping(childId, node.id);
        
        return node;
      },
      
      __getRunwayIdFromNode : function(node) {
        if (node.parent == null) {
          // We were passed jqTree's internal root node.
          for (var i = 0; i < this.rootTermConfigs.keySet(); ++i)
          {
            if (!rootTermConfigs[i].selectable)
            {
              return rootTermConfigs.termId;
            }
          }
          
          // Throw an error here instead?
          return null;
        }
        
        return node.runwayId;
      },
      
      render : function(parent, nodes) {
        var that = this;
        
        this.$render(parent);
        
        // Create the jqTree
        var $tree = $(this.getRawEl()).tree(this._config);
        
        $tree.bind(
            'tree.open',
            Mojo.Util.bind(this, this.__onNodeOpen)
        );
        
        if (this._config.editable)
        {
          this._boundedRightClick = Mojo.Util.bind(this, this.__onNodeRightClick);
          
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
        }
        
        if(nodes == null) {
          this.refreshRoots();          
        }
        else {
          this.rootTermConfigs = new com.runwaysdk.structure.HashMap();        
          
          this._createNodes(null, nodes);
        }
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
   * @class com.runwaysdk.geodashboard.ontology.ParentRelationshipCache A parent relationship cache that maps a childId to known parent records.
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
       * Updates all references in the cache from oldId to newId. 
       */
      updateTermId : function(oldId, newId) {
        var oldParents = this.get(oldId);
        this.removeAll(oldId);
        
        this.putAll(newId, oldParents);
        
        for(childId in this.cache) {
          if(this.cache.hasOwnProperty(childId)) {
            var parentRecordArray = this.cache[childId];
            
            for (var i = 0; i < parentRecordArray.length; ++i) {
              var parentRecord = parentRecordArray[i];
              if (parentRecord.parentId === oldId) {
                parentRecord.parentId = newId;
              }
            }
          }
        }
      },
      
      /**
       * @param childId A Term's RunwayId
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
      
      contains : function(childId, record) {
        if(this.cache[childId] != null) {
          var records = this.cache[childId];
          
          for(var i = 0; i < records.length; i++) {
            if(records[i].relId == record.relId) {
              return true;
            }
          }
        } 
      
        return false;
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
      removeRecordMatchingId : function(childId, parentId) {
        var records = this.get(childId);
        
        for (var i = 0; i < records.length; ++i) {
          if (records[i].parentId === parentId) {
            records.splice(i, 1);
            return;
          }
        }
        
        throw new com.runwaysdk.Exception("Unable to find a matching record to remove with childId[" + childId + "] and parentId[" + parentId + "].");
      },
      
      removeRecordMatchingIdAndRelType : function(childId, parentId, relType) {
        var records = this.get(childId);
        
        for (var i = 0; i < records.length; ++i) {
          if (records[i].parentId === parentId && records[i].relType === relType) {
            records.splice(i, 1);
            return;
          }
        }
        
        throw new com.runwaysdk.Exception("Unable to find a matching record to remove with childId[" + childId + "] and parentId[" + parentId + "].");
      },
      
      removeRecordMatchingRelId : function(childId, relId) {
        var records = this.get(childId);
        
        for (var i = 0; i < records.length; ++i) {
          if (records[i].relId === relId) {
            records.splice(i, 1);
            return;
          }
        }
        
        throw new com.runwaysdk.Exception("Unable to find a matching record to remove with childId[" + childId + "] and parentId[" + parentId + "].");
      },
      
      /**
       * @returns parentRecord[]
       */
      get : function(childId) {
        var got = this.cache[childId];
        
        if (got == null) {
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
      
      /**
       * @returns parentRecord
       */
      getRecordWithParentIdAndRelType : function(childId, parentId, relType) {
        var parentRecords = this.get(childId);
        
        for (var i = 0; i < parentRecords.length; ++i) {
          if (parentId === parentRecords[i].parentId && relType === parentRecords[i].relType) {
            return parentRecords[i];
          }
        }
        
        throw new com.runwaysdk.Exception("The ParentRelationshipCache is faulty, unable to find parent with id [" + parentId + "] and relType [" + relType + "] in the cache. The child term in question is [" + childId + "] and that term has [" + parentRecords.length + "] parents in the cache.");
      },
      
      putAll : function(childId, records) {
        for (var i = 0; i < records.length; ++i) {
          this.put(childId, records[i]);
        }
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
