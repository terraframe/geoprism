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
  var STRUCT = ClassFramework.alias(Mojo.STRUCTURE_PACKAGE + "*");

  
  var geoentityTreeName = "com.runwaysdk.geodashboard.ontology.GeoEntityTree";
  var TermTree = com.runwaysdk.geodashboard.ontology.TermTree;
  
  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage(geoentityTreeName, {
    "deleteDescribe" : "Are you sure you want to delete '${termLabel}' and all of its children?",
    "synonymsNode" : "Synonyms",
    "viewSynonyms" : "View Synonyms",
    "hideSynonyms" : "Hide Synonyms",
    "createSynonym" : "Create Synonym",
    "updateSynonym" : "Update Synonym",
    "deleteSynonym" : "Delete Synonym",
    "refreshSynonyms" : "Refresh Synonyms",
    "emptyMessage" : "No data to display, create a Universal first.",
    "merge" : "Merge",
    "mergeConfirmation" : "Are you sure you want to merge these to entities?",
    "ok" : "Ok",
    "cancel" : "Cancel",
    "mergeTitle" : "Merge confirmation",
    "accept" : "Confirm as new location"
  });
  
  Mojo.Meta.newClass('com.runwaysdk.geodashboard.ontology.RefreshQueue', {
    Instance : {
      initialize : function(tree, ids) {
        this._tree = tree;
        this._ids = ids;
        this._index = 0;
      },      
      onSuccess : function() {
        if(this._index < this._ids.length) {
          this._tree.refreshTerm(this._ids[this._index], this);
          
          this._index = this._index + 1;
        }        
      },
      start : function() {
        this.onSuccess();
      }
    }    
  });  
  
  /**
   * 
   */
  var geoentityTree = ClassFramework.newClass(geoentityTreeName, {
    
    Extends : TermTree,
    
    Instance : {
      
      initialize : function(config) {
        config = config || {};
        
        config.exportMenuType = "com.runwaysdk.geodashboard.gis.GeoEntityExportMenu";
        
        this.$initialize(config);
        
        this._wrapperDiv = this.getFactory().newElement("div");
      },

      /**
       * is binded to tree.contextmenu, called when the user right clicks on a node.
       * This override will disable some context menu options that don't make sense.
       */
      // @Override
      _createNodeRightClickMenu : function(event) {
        var that = this;
        var node = event.node;
        var parentId = this.__getRunwayIdFromNode(node.parent);
        var term = this.termCache[this.__getRunwayIdFromNode(node)];
        
        if (node.data != null && node.data.isSynonymContainer) {
          // They right clicked on a synonym container node. Display the context menu for synonym containers.
          var items = [];
          items.push({label:this.localize("createSynonym"), id:"add", handler:Mojo.Util.bind(this, this.__onCreateSynonym)});          
          items.push({label:this.localize("refreshSynonyms"), id:"refresh", handler:Mojo.Util.bind(this, this.__onRefreshSynonym)});          
          
          return items;
        }
        else if (node.data != null && node.data.isSynonym) {
          var items = [];
          items.push({label:this.localize("updateSynonym"), id:"edit", handler:Mojo.Util.bind(this, this.__onUpdateSynonym)});          
          items.push({label:this.localize("deleteSynonym"), id:"delete", handler:Mojo.Util.bind(this, this.__onDeleteSynonym)});          
          
          return items;
        }
        else {
          // Display the standard context menu        
          var items = this.$_createNodeRightClickMenu(event);
        
          if (node.data == null) {
            node.data = {};
          }
          
          if (node.data.synonymNode != null) {
            items.push({label:this.localize("hideSynonyms"), id:"synonyms", handler:Mojo.Util.bind(this, this.__onHideSynonym)});          
          }
          else {
            items.push({label:this.localize("viewSynonyms"), id:"synonyms", handler:Mojo.Util.bind(this, this.__onContextViewSynonymsClick)});          
          }
        
          items.push({label:this.localize("export"), id:"export", handler:Mojo.Util.bind(this, this.__onContextExportClick)}); 
          
          // For unmatched nodes we need to give the option to accept the node          
          var problem = $(".geoent-problem-error-li[data-entity='"+term.getId()+"'][data-problem='UNMATCHED']");
            
          if(problem.length > 0) {
            var problemId = problem.data('id');
            
            items.push({label:this.localize("accept"), id:"accept", handler:function(){that.deleteProblem(problemId);}}); 
          }
          
          // Disable some options based on GeoEntity rules.
          for(var i = 0; i < items.length; i++) {
            var item = items[i];
            
            if (item.id == 'add' && term.canCreateChildren === false) {
              item.enabled = false;
            }
            
            if (item.id == 'delete' && this.isRootTermId(parentId)) {
              item.enabled = false;
            }
          }
          
          return items;
        }
      },
      
      deleteProblem : function(problemId) {
        var that = this;
        
        var request = new Mojo.ClientRequest({
          onSuccess : function() {
            that.refreshEntityProblems();
          },
          onFailure : function(ex) {
            that.handleException(ex);
          }
        });
                  
        com.runwaysdk.geodashboard.GeoEntityUtil.deleteEntityProblem(request, problemId);
      },
      
      __onCreateSynonym : function(contextMenu, contextMenuItem, mouseEvent) {
        var that = this;
        var targetNode = contextMenu.getTarget();
        var parentId = this.__getRunwayIdFromNode(targetNode.parent);
        
        new com.runwaysdk.ui.RunwayControllerFormDialog({
          type: "com.runwaysdk.system.gis.geo.Synonym",
          action: "create",
          actionParams: {geoId: parentId},
          width: 730,
          height: 200,
          onSuccess : function(tnr) {
            var syn = tnr.getTerm();
            that.termCache[syn.getId()] = syn;
            that.parentRelationshipCache.put(syn.getId(), {parentId: parentId, relId: tnr.getRelationshipId(), relType: tnr.getRelationshipType()});
            that.__createTreeNode(syn.getId(), targetNode, true, {data: {isSynonym: true}});
          },
          onFailure : function(e) {
            that.handleException(e);
          }
        }).render();
      },
      __onHideSynonym : function(contextMenu, contextMenuItem, mouseEvent) {
        var targetNode = contextMenu.getTarget();
        
        this.getImpl().tree("removeNode", targetNode.data.synonymNode);
        targetNode.data.synonymNode = null;
      },
      __onRefreshSynonym : function(contextMenu, contextMenuItem, mouseEvent) {
        var targetNode = contextMenu.getTarget();
        
        this.refreshTerm(this.__getRunwayIdFromNode(targetNode));
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
            
            var nodes = that.__getNodesById(targetId);
            for (var i = 0; i < nodes.length; ++i) {
              $(that.getRawEl()).tree("updateNode", nodes[i], {label: that._getTermDisplayLabel(syn), id: syn.getId(), runwayId: syn.getId()});
            }
            
            that.setNodeBusy(targetNode, false);
          },
          onFailure : function(e) {
            that.setNodeBusy(targetNode, false);
            that.handleException(e);
          },
          onCancel : function() {
            that.setNodeBusy(targetNode, false);
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
            that.getImpl().tree("removeNode", targetNode);
            that.setNodeBusy(targetNode, false);
          },
          onFailure : function(e) {
            that.setNodeBusy(targetNode, false);
            that.handleException(e);
          },
          onCancel : function() {
            that.setNodeBusy(targetNode, false);
          }
        }).render();
      },
      /**
       * Creates the "Synonyms" node and then loads the synonyms under it.
       */
      __onContextViewSynonymsClick : function(contextMenu, contextMenuItem, mouseEvent) {
        var targetNode = contextMenu.getTarget();
        var that = this;
        var runwayId = that.__getRunwayIdFromNode(targetNode);
        
        if (targetNode.data != null && targetNode.data.isSynonymContainer) { return; }
        
        var createSynonymNode = function(){
          targetNode.dontClobberChildren = true;
          
          var synId = targetNode.id + "_" + Mojo.Util.generateId();
          
          // Create a synonyms node under the target node
          var justCreated = that.__createTreeNode(synId, targetNode, true, {label: that.localize("synonymsNode"), data: {isSynonymContainer: true}});
          
          targetNode.data.synonymNode = justCreated;
          that.setNodeBusy(targetNode, false);
          
          // Load the synonyms
          that.refreshTerm(synId);
        };
        
        that.setNodeBusy(targetNode, true);
        
        if (!that.hasLoadedNode(targetNode)) {
          that.refreshTerm(runwayId, {
            onSuccess : function() {
              createSynonymNode();
            },
            onFailure : function() {
              that.setNodeBusy(targetNode, false);
            }
          });
        }
        else {
          createSynonymNode();
        }
      },
      
      /**
       * Override
       * 
       */
      _createNodeMoveMenu : function(event) {

        var $thisTree = $(this.getRawEl());
        var movedNode = event.move_info.moved_node;
        var targetNode = event.move_info.target_node;
                
        var movedNodeId = this.__getRunwayIdFromNode(movedNode);
        var targetNodeId = this.__getRunwayIdFromNode(targetNode);        
                      
        var that = this;
            
        // User clicks merge on context menu //
        var mergeHandler = function(mouseEvent, contextMenu) {

          var dialog = that.getFactory().newDialog(that.localize("mergeTitle"), {modal: false, width: 350, height: 200, resizable: false});

          var okHandler = Mojo.Util.bind(this, function() {
            var request = new Mojo.ClientRequest({
              onSuccess : function(idsToUpdate) {
            	var queue = new com.runwaysdk.geodashboard.ontology.RefreshQueue(that, idsToUpdate);
                queue.start();
                    
                that.doForNodeAndAllChildren(movedNode, function(node){that.setNodeBusy(node, false);}); 
                
                that.refreshEntityProblems();
              },
              onFailure : function(ex) {
                that.doForNodeAndAllChildren(movedNode, function(node){that.setNodeBusy(node, false);});
                that.handleException(ex);
              }
            });
            
            dialog.close();
                  
            this.doForNodeAndAllChildren(movedNode, function(node){that.setNodeBusy(node, true);});
                
            com.runwaysdk.geodashboard.GeoEntityUtil.makeSynonym(request, movedNodeId, targetNodeId);            
          });          
          
          dialog.appendContent(that.localize("mergeConfirmation"));
          dialog.addButton(that.localize("ok"), okHandler, null, {"class": "btn btn-primary"});
          dialog.addButton(that.localize("cancel"), function(){dialog.close();}, null, {"class": "btn"});
          dialog.render();
        };
      
      
        var items = this.$_createNodeMoveMenu(event);
        items.push({label:this.localize("merge"), image:"merge", handler:Mojo.Util.bind(this, mergeHandler)});
        
        return items;        
      },
      
      /**
       * Override
       * 
       */
      canMove : function(node)
      {
        if(! node.parent.parent){
          return false;
        }
        else if(node.data != null && (node.data.isSynonym || node.data.isSynonymContainer)) {
          return false;
        }
        else{
          return true;
        }
      },
      
      
      /**
       * Override
       * 
       */
      canMoveTo : function(moved_node, target_node, position)
      {
        if (target_node.data != null && (target_node.data.isSynonym || target_node.data.isSynonymContainer)) {
          return false;
        }
        else{
          return true;
        }
      },
      
      focusTerm : function(termId) {
        var that = this;
        var id = termId;
          
        var nodes = that.__getNodesById(termId);
          
        if(nodes == null) {
          // Load all of the nodes going up the tree from termId
          var request = new Mojo.ClientRequest({
            onSuccess: function(response){
              var nodes = JSON.parse(response);

              that._createNodes(null, nodes);
              
              that.focusTerm(termId);              
            }, 
            onFailure : function(ex) {
              that.handleException(ex);
            }              
          });
          
          com.runwaysdk.geodashboard.GeoEntityUtil.getGeoEntityTree(request, termId);
        }
        else {
          for(var i = 0; i < nodes.length; i++) {
            var node = nodes[i];
            this.getImpl().tree('selectNode', node, true);      
            
            if(i === nodes.length - 1){
              var escape = false;
              var height = 0;
              var currentElem = $(node.element);
              while(escape !== true){
                if(currentElem.parent().hasClass("jqtree_common") && currentElem.parent().hasClass("jqtree-folder") && !currentElem.parent().hasClass("jqtree-selected")){
                  height += currentElem.parent().position().top;
                  currentElem = currentElem.parent();
                  escape = true; // escape at the first container jqtree-folder
                }
                else if(currentElem.parent().hasClass("jqtree_common")){
                  // pass containers if they aren't jqtree-folder's
                  currentElem = currentElem.parent();
                }
                else{
                  // just in case the html changes in the jqtree lib we will make sure the loop can be escaped
                  escape = true;
                }
              }
              $('.pageContent').animate({
                  scrollTop: height + $('.pageContent').scrollTop() - ($('.pageContent')[0].getBoundingClientRect().height / 2)
              }, 500);
            }
          }
        }        
      },
      
      /**
       * OVERRIDE :  Adds a special case handler for synonyms.
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
        var id = termId;
        
        this.setTermBusy(termId, true);
        
        if(nodes == null) {
          nodes = that.__getNodesById(termId);              
        }
        var parent = nodes[0];
        
        // Special case for synonyms.
        if (parent.data != null && parent.data.isSynonymContainer) {
          var parentId = this.__getRunwayIdFromNode(parent.parent);
          
          var cr = new Mojo.ClientRequest({
            onSuccess: function(response){
              var json = Mojo.Util.getObject(response);
              var tnrs = com.runwaysdk.DTOUtil.convertToType(json.returnValue);
              
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
              for (var i = 0; i < tnrs.length; ++i) {
                var childId = tnrs[i].getTerm().getId();
                
                var parentRecord = {parentId: termId, relId: tnrs[i].getRelationshipId(), relType: tnrs[i].getRelationshipType()};
                that.parentRelationshipCache.put(childId, parentRecord);
                
                that.termCache[childId] = tnrs[i].getTerm();
                
                for (var iNode = 0; iNode < nodes.length; ++iNode) {
                  var node = nodes[iNode];
                  that.__createTreeNode(childId, node, false, {data: {isSynonym: true}});
                }
              }
              
              that.setTermBusy(termId, false);
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
          this.$refreshTerm(termId, callback, nodes);
        }
      },   
      
      _handleUpdateTerm : function (termId, responseObj) {
        this.$_handleUpdateTerm(termId, responseObj);
        
        this.refreshEntityProblems();
      },
      
      _handleCreateTerm : function (parentId, parentNode, responseObj) {
        this.$_handleCreateTerm(parentId, parentNode, responseObj);
        
        this.refreshEntityProblems();
      },
      
      refreshTreeAfterDeleteTerm : function(termId) {
        this.$refreshTreeAfterDeleteTerm(termId);
        
        this.refreshEntityProblems();
      },
      
      refreshEntityProblems : function() {
        var that = this;
      
        var request = new Mojo.ClientRequest({
          onSuccess: function(views){
            
            var html = '';
          
            for(var i = 0; i < views.length; i++) {
              var view = views[i];
              
              html += '<li class="geoent-problem-error-li" data-id="' + view.getConcreteId() + '" data-entity="' + view.getGeoId() + '" data-problem="' + view.getProblemName() + '">';
              html += '  <a href="#" class="fa fa-times-circle geoent-problem-msg-icon geoent-problem-error">';
              html += '    <p class="geoent-problem-msg">' + view.getProblem() + '</p>';
              html += '  </a>';
              html += '</li>';      
            }
            
            if(html.length > 0){
              $("#problems-list").html(html);
            }
            else{
              $("#problem-panel-noissue-msg").show();
            }
            
            $(".geoent-problem-error-li").click(function(e){
              var entityId = $(e.currentTarget).data("entity");
                
              that.focusTerm(entityId);
            });
          },
          onFailure : function(ex) {
            that.handleException(ex);
          }
        });
        
        com.runwaysdk.geodashboard.GeoEntityUtil.getAllProblems(request)
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
      
      _onClickExportAll : function() {
        this.exportTerm(this.rootTermId);
      },
      
//      createExportAllButton : function() {
//        var but = this.getFactory().newButton(this.localize("exportAll"), Mojo.Util.bind(this, this._onClickExportAll));
//        
//        but.addClassName("btn btn-primary");
//        but.setStyle("margin-bottom", "20px");
//        
//        this._wrapperDiv.appendChild(but);
//      },
      
      render : function(parent) {
        
//        this.createExportAllButton();
        
        this._wrapperDiv.render(parent);
        
        this.$render(this._wrapperDiv);
        
      }
    }
  });
  
  return geoentityTree;
  
})();
