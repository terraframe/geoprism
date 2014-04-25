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
  
  var geoentityTreeName = "com.runwaysdk.geodashboard.ontology.GeoEntityTree";
  var TermTree = com.runwaysdk.geodashboard.ontology.TermTree;
  
  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage(geoentityTreeName, {
    "deleteDescribe" : "Are you sure you want to delete '${termLabel}' and all of its children?",
    "viewSynonyms" : "View Synonyms",
    "hideSynonyms" : "Hide Synonyms",
    "createSynonym" : "Create Synonym",
    "updateSynonym" : "Update Synonym",
    "deleteSynonym" : "Delete Synonym",
    "refreshSynonym" : "Refresh Synonyms"
  });
  
  /**
   * 
   */
  var geoentityTree = ClassFramework.newClass(geoentityTreeName, {
    
    Extends : TermTree,
    
    Instance : {
      
      initialize : function(config) {
        config = config || {};
        
        this.$initialize(config);
      },
      
      /**
       * is binded to tree.contextmenu, called when the user right clicks on a node.
       * This override will disable some context menu options that don't make sense.
       */
      // @Override
      __onNodeRightClick : function(event, object) {
        var node = object.node;
        var parentId = this.getParentRunwayId(node);
        var term = this.termCache[this.__getRunwayIdFromNode(node)];
        
        if (this._cm != null && !this._cm.isDestroyed()) {
          this._cm.destroy();
        }
        
        this._cm = this.getFactory().newContextMenu(node);
        
        if (node.data != null && node.data.isSynonymContainer) {
          // They right clicked on a synonym container node. Display the context menu for synonym containers.
          var create = this._cm.addItem(this.localize("createSynonym"), "add", Mojo.Util.bind(this, this.__onCreateSynonym));
          var refresh = this._cm.addItem(this.localize("refreshSynonym"), "refresh", Mojo.Util.bind(this, this.__onRefreshSynonym));
        }
        else if (node.data != null && node.data.isSynonym) {
          // Display the synonym node context menu.
          var delSyn = this._cm.addItem(this.localize("updateSynonym"), "edit", Mojo.Util.bind(this, this.__onUpdateSynonym));
          var update = this._cm.addItem(this.localize("deleteSynonym"), "delete", Mojo.Util.bind(this, this.__onDeleteSynonym));
        }
        else {
          // Display the standard context menu but disable some options based on GeoEntity rules.
          
          var create = this._cm.addItem(this.localize("create"), "add", Mojo.Util.bind(this, this.__onContextCreateClick));
          if (term.canCreateChildren === false) {
            create.setEnabled(false);
          }
          
          var update = this._cm.addItem(this.localize("update"), "edit", Mojo.Util.bind(this, this.__onContextEditClick));
          
          var del = this._cm.addItem(this.localize("delete"), "delete", Mojo.Util.bind(this, this.__onContextDeleteClick));
          if (parentId === this.rootTermId) {
            del.setEnabled(false);
          }
          
          var refresh = this._cm.addItem(this.localize("refresh"), "refresh", Mojo.Util.bind(this, this.__onContextRefreshClick));
          
          var synonyms = null;
          if (node.synonymNode != null) {
            synonyms = this._cm.addItem(this.localize("hideSynonyms"), "synonyms", Mojo.Util.bind(this, this.__onHideSynonym));
          }
          else {
            synonyms = this._cm.addItem(this.localize("viewSynonyms"), "synonyms", Mojo.Util.bind(this, this.__onContextViewSynonymsClick));
          }
          
          if (this.busyNodes.contains(node)) {
            create.setEnabled(false);
            update.setEnabled(false);
            del.setEnabled(false);
            refresh.setEnabled(false);
            
            synonyms.setEnabled(false);
          }
        }
        
        this._cm.render();
      },
      __onCreateSynonym : function(contextMenu, contextMenuItem, mouseEvent) {
        var that = this;
        var targetNode = contextMenu.getTarget();
        var parentId = this.__getRunwayIdFromNode(targetNode);
        
        new com.runwaysdk.ui.RunwayControllerFormDialog({
          type: "com.runwaysdk.system.gis.geo.Synonym",
          action: "create",
          actionParams: {geoId: parentId},
          width: 730,
          height: 200,
          onSuccess : function(tnr) {
            var syn = tnr.getTerm();
            that.termCache[syn.getId()] = syn;
//            that.parentRelationshipCache.put(syn.getId(), {parentId: parentId, relId: relId, relType: relType});
            that.__createTreeNode(syn.getId(), targetNode, false, null, null, {isSynonym: true});
            
            that._impl.jstree("open_node", targetNode);
          },
          onFailure : function(e) {
            that.handleException(e);
          }
        }).render();
      },
      __onHideSynonym : function(contextMenu, contextMenuItem, mouseEvent) {
        var targetNode = contextMenu.getTarget();
        
        this._impl.jstree("delete_node", targetNode.synonymNode);
        targetNode.synonymNode = null;
      },
      __onRefreshSynonym : function(contextMenu, contextMenuItem, mouseEvent) {
        var targetNode = contextMenu.getTarget();
        
        this._impl.jstree("load_node", targetNode);
      },
      __onUpdateSynonym : function(contextMenu, contextMenuItem, mouseEvent) {
        var that = this;
        var targetNode = contextMenu.getTarget();
        var targetId = this.__getRunwayIdFromNode(targetNode);
        
        this.setNodeBusy(targetNode, true);
        
        new com.runwaysdk.ui.RunwayControllerFormDialog({
          type: "com.runwaysdk.system.gis.geo.Synonym",
          action: "update",
          viewParams: {id: targetId},
          width: 730,
          height: 200,
          onSuccess : function(syn) {
            that.termCache[syn.getId()] = syn;
            
            var nodes = that.__getNodesById(syn.getId());
            for (var i = 0; i < nodes.length; ++i) {
              that._impl.jstree("rename_node", nodes[i], that._getTermDisplayLabel(syn));
            }
            
            that.setNodeBusy(targetNode, false);
          },
          onFailure : function(e) {
            that.handleException(e);
          }
        }).render();
      },
      __onDeleteSynonym : function(contextMenu, contextMenuItem, mouseEvent) {
        var that = this;
        var targetNode = contextMenu.getTarget();
        var dto = this.termCache[this.__getRunwayIdFromNode(targetNode)];
        
        this.setNodeBusy(targetNode, true);
        
        new com.runwaysdk.ui.RunwayControllerFormDialog({
          type: "com.runwaysdk.system.gis.geo.Synonym",
          action: "delete",
          dto: dto,
          onSuccess : function(response) {
            that._impl.jstree("delete_node", targetNode);
            that.setNodeBusy(targetNode, false);
          },
          onFailure : function(e) {
            that.handleException(e);
          }
        }).render();
      },
      /**
       * Creates the "Synonyms" node and then loads the synonyms under it.
       */
      __onContextViewSynonymsClick : function(contextMenu, contextMenuItem, mouseEvent) {
        var targetNode = contextMenu.getTarget();
        var that = this;
        
        if (targetNode.data != null && targetNode.data.isSynonymContainer) { return; }
        
        var createSynonymNode = function(){
          var onOpen = function(justOpened) {
            justOpened.dontClobberChildren = true;
            
            var afterCreateNode = function(justCreated) {
              targetNode.synonymNode = justCreated;
              that.setNodeBusy(targetNode, false);
              
              // Open the target node
              that._impl.jstree("open_node", targetNode, false);
              
              // Load the synonyms
              that._impl.jstree("load_node", justCreated, function(justLoaded){
                that._impl.jstree("open_node", justLoaded, false);
              });
            };
            
            console.log("Creating a synonym with runwayId: " + that.__getRunwayIdFromNode(targetNode));
            
            // Create a synonyms node under the target node
            var idStr = targetNode.id + "_" + Mojo.Util.generateId();
            that._impl.jstree(
              'create_node',
              targetNode,
              { state:{opened: false}, children:false, text: "Synonyms", data: {runwayId: that.__getRunwayIdFromNode(targetNode), isSynonymContainer: true}, id: idStr },
              0,
              afterCreateNode, true
            );
          };
          
          that._impl.jstree("open_node", targetNode, onOpen, false);
        };
        
        that.setNodeBusy(targetNode, true);
        
        if (!this._impl.jstree("is_loaded", targetNode)) {
          this._impl.jstree("load_node", targetNode, createSynonymNode);
        }
        else {
          createSynonymNode();
        }
      },
      
      __treeWantsData : function(treeThisRef, parent, jsTreeCallback) {
        var that = this;
        
        var parentId = this.__getRunwayIdFromNode(parent);
        
        if (parent.data != null && parent.data.isSynonymContainer) {
          
          var cr = new Mojo.ClientRequest({
            onSuccess: function(response){
              var tnrs = Mojo.Util.toObject(response).returnValue;
              
              var json = [];
              for (var i = 0; i < tnrs.length; ++i) {
                var tnr = com.runwaysdk.DTOUtil.convertToType(tnrs[i]);
                var term = tnr.getTerm();
                var termId = term.getId();
                
                var parentRecord = {parentId: parentId, relId: tnr.getRelationshipId(), relType: tnr.getRelationshipType()};
                that.parentRelationshipCache.put(termId, parentRecord);
                that.termCache[termId] = term;
                
                var treeNode = {
                    text: that._getTermDisplayLabel(term),
                    id: termId, state: {opened: true}, children: false,
                    data: { runwayId: termId, isSynonym: true }
//                    li_attr: {'class' : relType} // Add the relationshipType to the li's class
                };
                json.push(treeNode);
              }
              
              jsTreeCallback.call(treeThisRef, json);
              
              if (json.length === 0) {
                that._impl.jstree("redraw_node", parent, false);
              }
            },
            onFailure : function(ex) {
              jsTreeCallback.call(this, []);
              that.handleException(ex);
              return;
            }
          });
          
          Mojo.Util.invokeControllerAction("com.runwaysdk.system.gis.geo.Synonym", "getDirectDescendants", {parentId: parentId}, cr);
        }
        else {
          this.$__treeWantsData(treeThisRef, parent, jsTreeCallback);
        }
      },
      
      // @Override
      /**
       * This override prints the universal display label along with the geo entity display label.
       */
      _getTermDisplayLabel : function(term) {
        var displayLabel = this.$_getTermDisplayLabel(term);
        
        if (term.universalDisplayLabel != null && term.universalDisplayLabel !== displayLabel) {
          displayLabel = displayLabel + " [" + term.universalDisplayLabel + "]";
        }
        
        return displayLabel;
      },
      
      _makeGeoEntityFromView : function(view) {
        var term = new com.runwaysdk.system.gis.geo.GeoEntity();
        term.getDisplayLabel().setLocalizedValue(view.getGeoEntityDisplayLabel());
        term.id = view.getGeoEntityId();
        term.universalDisplayLabel = view.getUniversalDisplayLabel();
        term.canCreateChildren = view.getCanCreateChildren();
        term.newInstance = false;
        return term;
      },
      
      __responseToTerm : function(view) {
        return this._makeGeoEntityFromView(view);
      },
      
      __responseToTNR : function(geoEntView) {
        var term = this._makeGeoEntityFromView(geoEntView);
        var relId = geoEntView.getRelationshipId();
        var relType = geoEntView.getRelationshipType();
        
        return new com.runwaysdk.business.ontology.TermAndRel({
          term: term,
          relType: relType,
          relId: relId,
          dto_type: "com.runwaysdk.business.ontology.TermAndRel"
        });
      },
      
      render : function(parent) {
        
        this.$render(parent);
        
      }
    }
  });
  
  return geoentityTree;
  
})();