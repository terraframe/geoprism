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
  var STRUCT = ClassFramework.alias(Mojo.STRUCTURE_PACKAGE + "*");

  
  var geoentityTreeName = "net.geoprism.ontology.GeoEntityTree";
  var TermTree = net.geoprism.ontology.TermTree;
  
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
    "accept" : "Confirm as new location",
    "noproblems-msg" : "There are no known geoentity issues"
    	
  });
  
  Mojo.Meta.newClass('net.geoprism.ontology.RefreshQueue', {
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
        this._tree.refreshEntityProblems(this);        
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
        
        config.exportMenuType = "net.geoprism.ontology.GeoEntityExportMenu";
        config.onCreateLi = Mojo.Util.bind(this, this.__onCreateLi);
        
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
          
          // For unmatched nodes we need to give the option to accept the node          
          var problem = $(".geoent-problem-error-li[data-entity='"+term.getId()+"'][data-problem='UNMATCHED']");
            
          if(problem.length > 0) {
            var problemId = problem.data('id');
            var termId = problem.data('entity');
            
            items.push({label:this.localize("accept"), id:"accept", handler:function(){that.deleteProblem(termId, problemId);}}); 
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
      
      deleteProblem : function(termId, problemId) {
        var that = this;
        
        var request = new Mojo.ClientRequest({
          onSuccess : function() {
            that.refreshTermProblems(termId);

            $("#tree").find('[data-problemid="'+problemId+'"]').remove();
          },
          onFailure : function(ex) {
            that.handleException(ex);
          }
        });
                  
        net.geoprism.ontology.GeoEntityUtil.deleteEntityProblem(request, problemId);
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
                var queue = new net.geoprism.ontology.RefreshQueue(that, idsToUpdate);
                queue.start();
                    
                that.doForNodeAndAllChildren(movedNode, function(node){that.setNodeBusy(node, false);}); 
              },
              onFailure : function(ex) {
                that.doForNodeAndAllChildren(movedNode, function(node){that.setNodeBusy(node, false);});
                that.handleException(ex);
              }
            });
            
            dialog.close();
                  
            this.doForNodeAndAllChildren(movedNode, function(node){that.setNodeBusy(node, true);});
                
            net.geoprism.ontology.GeoEntityUtil.makeSynonym(request, movedNodeId, targetNodeId);            
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
      _canMove : function(node)
      {
        if(node.data != null && (node.data.isSynonym || node.data.isSynonymContainer)) {
          return false;
        }
        
        return this.$_canMove(node);
      },
      
      
      /**
       * Override
       * 
       */
      _canMoveTo : function(moved_node, target_node, position)
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
          
          net.geoprism.ontology.GeoEntityUtil.getGeoEntityTree(request, termId);
        }
        else {
          for(var i = 0; i < nodes.length; i++) {
            var node = nodes[i];
            this.getImpl().tree('openNode', node, true);      
            
            if(i === nodes.length - 1){
              this.getImpl().tree('selectNode', node, true);     
              
              this.getImpl().tree('scrollToNode', node);
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
      
      __createTreeNode : function(childId, parentNode, hasFetched, config, hide) {
       
        var problemId = "";
        var hasProblem = this.hasProblem(childId);
        if(hasProblem){
          problemId = $(".geoent-problem-error-li[data-entity='"+childId+"']").data("id");
        }
        
        if(config){
          config.hasProblem = hasProblem;
          config.problemId = problemId
        }
        else{
          var config = {};
          config.hasProblem = hasProblem;
          config.problemId = problemId
        }
       
        var node = this.$__createTreeNode(childId, parentNode, hasFetched, config, hide);

      },
      
      _refreshProblems : function(termId) {
        var nodes = this.__getNodesById(termId);
        var hasProblem = this.hasProblem(termId);
        
        for(var i = 0; i < nodes.length; i++) {
          var node = nodes[i];
          
          this._toggleProblem(node, hasProblem);
        }
      },
      
      __onCreateLi : function(node, $li) {
        if (!node.phantom) {
          if(node.hasProblem){
            var msg = "";
            var msgEls = $("#problems-list").find('[data-entity="'+node.runwayId+'"]');
            for(var i=0; i<msgEls.length; i++){
              var msgEl = msgEls[i];
              msg +=  i+1 + "." + "&nbsp;&nbsp;" + $(msgEl).find(".geoent-problem-msg").text(); // gets the message from the problems panel
              if(i < msgEls.length - 1){
                msg += "<br/><br/>";
              }
            }
            $li.find("span").parent().append("<i data-problemid='"+ node.problemId +"' data-entity='"+ node.runwayId +"' class='fa fa-times-circle geoent-problem-msg-icon geoent-problem-error'></i>");
            $li.find("i").tooltip({
              items: "i",
              content: msg,
              tooltipClass: "geoentity-problem-tooltip"
            });
          }
        }
      },
      
      _toggleProblem : function(node, hasProblem) {
        if(hasProblem) {
          // Indicate that the node has a problem
          this.getImpl().tree('updateNode', node, {hasProblem:true} );
          
          node.problem = true;
        }
        else if(!hasProblem) {
          // Indicate that the node no longer has a problem
          $(node.element).find("i").hide();
          this.getImpl().tree('updateNode', node, {hasProblem:false} );
          
          node.problem = false;
        }        
      },
      
      hasProblem : function(termId) {
        var problem = $(".geoent-problem-error-li[data-entity='"+termId+"']");
          
        return (problem.length > 0);
      },
      
      /**
       * OVERRIDE
       */
      _handleUpdateTerm : function (termId, responseObj) {
        var that = this;
          
        var callback = {};
        callback.onSuccess = function(){
          that.$_handleUpdateTerm(termId, responseObj);
          
          that._refreshProblems(termId);          
        };        
          
        this.refreshEntityProblems(callback);
      },

      /**
       * OVERRIDE
       */
      _handleCreateTerm : function (parentId, parentNodes, responseObj) {
        var that = this;
        
        var callback = {};
        callback.onSuccess = function(){
          that.$_handleCreateTerm(parentId, parentNodes, responseObj);
        };        
        
        this.refreshEntityProblems(callback);
      },

      /**
       * OVERRIDE
       */
      refreshTreeAfterDeleteTerm : function(termId) {
        var that = this;
        
        var callback = {};
        callback.onSuccess = function(){
          that.$refreshTreeAfterDeleteTerm(termId);
        };        
        
        this.refreshEntityProblems(callback);
      },
      
      /*
       * Refreshes all of the problems list and all of the nodes corresponding to a term
       */
      refreshTermProblems : function(termId) {
        var that = this;
      
        var callback = {};
        callback.onSuccess = function(){
          that._refreshProblems(termId);
        };
        
        this.refreshEntityProblems(callback);  
      },
      
      refreshEntityProblems : function(callback) {
        var that = this;
      
        var request = new Mojo.ClientRequest({
          onSuccess: function(views){
            that.displayEntityProblems(views);  
            
            if(callback != null && Mojo.Util.isFunction(callback.onSuccess)) {
              callback.onSuccess(views);
            }
          },
          onFailure : function(ex) {
            that.handleException(ex);
            
            if(callback != null && Mojo.Util.isFunction(callback.onFailure)) {
              callback.onFailure(ex);
            }
          }
        });
        
        net.geoprism.ontology.GeoEntityUtil.getAllProblems(request);
      },
      
      /**
       * Removes and re-creates the problems panel
       */
      displayEntityProblems : function(views) {
        var that = this;
        var html = '';
          
        for(var i = 0; i < views.length; i++) {
          var view = views[i];
            
          html += '<li class="geoent-problem-error-li" data-id="' + view.getConcreteId() + '" data-entity="' + view.getGeoId() + '" data-problem="' + view.getProblemName() + '">';
          html += '  <a href="#" class="fa fa-times-circle geoent-problem-msg-icon geoent-problem-error">';
          html += '    <p class="geoent-problem-msg">' + view.getProblem() + '</p>';
          html += '  </a>';
          html += '</li>';      
        }
          
        if(html.length == 0){
          html = '<h4 id="problem-panel-noissue-msg">' + this.localize("noproblems-msg") + '</h4>';
        }
        
        $("#problems-list").html(html);
          
        $(".geoent-problem-error-li").click(function(e){
          var entityId = $(e.currentTarget).data("entity");
              
          that.focusTerm(entityId);
        });        
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
      
      render : function(parent, views) {
        if(views != null) {
          this.displayEntityProblems(views);         
        }
        
        this._wrapperDiv.render(parent);
        
        this.$render(this._wrapperDiv);        
      }
    }
  });
  
  return geoentityTree;
  
})();
