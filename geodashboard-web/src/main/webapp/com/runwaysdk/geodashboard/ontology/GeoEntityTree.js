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
    "deleteDescribe" : "Are you sure you want to delete '${termLabel}' and all of its children?"
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
      __onNodeRightClick : function(e) {
        var parentId = this.__getRunwayIdFromNode(e.node.parent);
        var term = this.termCache[this.__getRunwayIdFromNode(e.node)];
        
        var cm = this.getFactory().newContextMenu(e.node);
        
        var create = cm.addItem(this.localize("create"), "add", Mojo.Util.bind(this, this.__onContextCreateClick));
        if (term.canCreateChildren === false) {
          create.setEnabled(false);
        }
        
        cm.addItem(this.localize("update"), "edit", Mojo.Util.bind(this, this.__onContextEditClick));
        
        var del = cm.addItem(this.localize("delete"), "delete", Mojo.Util.bind(this, this.__onContextDeleteClick));
        if (parentId === this.rootTermId) {
          del.setEnabled(false);
        }
        
        cm.addItem(this.localize("refresh"), "refresh", Mojo.Util.bind(this, this.__onContextRefreshClick));
        
        cm.render();
      },
      
      /**
       * We're overriding this method because the GeoEntityController returns a GeoEntityView, but the TermTree is coded to accept a term.
       */
      __onContextEditClick : function(contextMenu, contextMenuItem, mouseEvent) {
        var node = contextMenu.getTarget();
        var termId = this.__getRunwayIdFromNode(node);
        var that = this;
        var parentId = this.__getRunwayIdFromNode(node.parent);
        
        var config = {
          type: this._config.termType,
          viewParams: {parentId: parentId},
          action: "update",
          id: termId,
          onSuccess : function(view) {
            var term = that._makeGeoEntityFromView(view);
            
            that.termCache[term.getId()] = term;
            
            var nodes = that.__getNodesById(term.getId());
            for (var i = 0; i < nodes.length; ++i) {
              $(that.getRawEl()).tree("updateNode", nodes[i], {label: that._getTermDisplayLabel(term)});
            }
          },
          onFailure : function(e) {
            that.handleException(e);
          }
        };
        Mojo.Util.merge(this._config.crud.update, config);
        
        new com.runwaysdk.ui.RunwayControllerFormDialog(config).render();
      },
      
      // @Override
      /**
       * This override prints the universal display label along with the geo entity display label.
       */
      _getTermDisplayLabel : function(term) {
        var displayLabel = this.$_getTermDisplayLabel(term);
        
        if (term.universalDisplayLabel != null && term.universalDisplayLabel !== displayLabel) {
          displayLabel = "[" + term.universalDisplayLabel + "] " + displayLabel;
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
      
      /**
       * We're overriding this method because the GeoEntityController returns a GeoEntityView, but the TermTree is coded to accept a TermAndRel.
       */
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
          onSuccess : function(view) {
            var term = that._makeGeoEntityFromView(view);
            var relId = view.getRelationshipId();
            var relType = view.getRelationshipType();
            
            that.parentRelationshipCache.put(term.getId(), {parentId: parentId, relId: relId, relType: relType});
            that.termCache[term.getId()] = term;
            
            var $thisTree = $(that.getRawEl());
            for (var i = 0; i < parentNodes.length; ++i) {
              var node = that.__createTreeNode(term.getId(), parentNodes[i]);
              $thisTree.tree("openNode", node);
            }
          },
          onFailure : function(e) {
            that.handleException(e);
          }
        };
        Mojo.Util.merge(this._config.crud.create, config);
        
        new com.runwaysdk.ui.RunwayControllerFormDialog(config).render();
      },
      
      // @Override
      /**
       * We're overriding this method because the GeoEntityController returns a GeoEntityView, but the TermTree is coded to accept a TermAndRel.
       */
      refreshTerm : function(termId) {
        var that = this;
        var id = termId;
        
        var callback = new Mojo.ClientRequest({
          onSuccess : function(responseText) {
            var json = Mojo.Util.getObject(responseText);
            var geoEntityViews = com.runwaysdk.DTOUtil.convertToType(json.returnValue);
            
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
            for (var i = 0; i < geoEntityViews.length; ++i) {
              var $tree = $(that.getRawEl());
              var view = geoEntityViews[i];
              var childId = view.getGeoEntityId();
              
              var parentRecord = {parentId: termId, relId: view.getRelationshipId(), relType: view.getRelationshipType()};
              that.parentRelationshipCache.put(childId, parentRecord);
               
              var term = that._makeGeoEntityFromView(view);
              
              that.termCache[childId] = term;
              
              for (var iNode = 0; iNode < nodes.length; ++iNode) {
                var node = nodes[iNode];
                that.__createTreeNode(childId, node);
                node.hasFetched = true;
              }
            }
          },
          
          onFailure : function(err) {
            that.handleException(err);
            return;
          }
        });
        
        Mojo.Util.invokeControllerAction(this._config.termType, "getAllChildren", {parentId: termId, pageNum: 0, pageSize: 0}, callback);
      },
      
      // @Override
      refreshTreeAfterDeleteTerm : function(termId) {
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
      },
      
      render : function(parent) {
        
        this.$render(parent);
        
      }
    }
  });
  
  return geoentityTree;
  
})();