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

//define(["./Factory"], function(){
(function(){

/**
 * @class com.runwaysdk.ui.jquery.Tree A wrapper around JQuery widget jqTree.
 * 
 * @constructs
 * @param obj
 *   @param String obj.nodeId The id of the div defined in html, specifying the location for the tree. The id is prefixed with #.
 *   @param Object obj.data Optional, a properly formatted data object as documented by jqTree.
 *   @param Boolean obj.dragDrop Optional, set to true to enable drag drop, false to disable. Default is false.
 */
var tree = Mojo.Meta.newClass(Mojo.JQUERY_PACKAGE + 'Tree', {
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
      
      var data = {};
      if (obj.data != null) {
        data = obj.data;
      }
      
      var dragDrop = false;
      if (obj.dragDrop != null) {
        dragDrop = obj.dragDrop;
      }
      
      // Create the jqTree
      $(function() {
        $(obj.nodeId).tree({
            data: data,
            dragAndDrop: dragDrop,
            useContextMenu: false,
            onCreateLi: function(node, $li) {
              com.runwaysdk.ui.DOMFacade.getChildren($li[0])[0].__runwayid = node.id;
            }
        });
      });
      
      this.nodeId = obj.nodeId;
      var $tree = $(this.nodeId);
      
      $tree.bind(
          'tree.open',
          Mojo.Util.bind(this, this.__onNodeOpen)
      );
      $tree.bind(
          'tree.select',
          Mojo.Util.bind(this, this.__onNodeSelect)
      );
      $tree.bind(
          'tree.move',
          Mojo.Util.bind(this, this.__onNodeMove)
      );
      $tree.bind(
          'tree.contextmenu',
          Mojo.Util.bind(this, this.__onNodeRightClick)
      );
      
      this.selectCallbacks = [];
      this.deselectCallbacks = [];
      
      this.openCallbacks = [];
      this.moveCallbacks = [];
      this.rightClickCallbacks = [];
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
     * Adds a child to the tree.
     * 
     * @param com.runwaysdk.business.TermDTO or String (id) child The child term that will be added to the tree.
     * @param com.runwaysdk.business.TermDTO or String (id) parent The parent term that the child will be appended under.
     * @param String relationshipType The relationship type that the child will be appended with. The relationship type must extend com.runwaysdk.business.TermRelationship.
     */
    addChild : function(child, parent, relationshipType, callback) {
      this.__assertPrereqs();
      this.__assertRequire("child", child);
      this.__assertRequire("parent", parent);
      this.__assertRequire("relationshipType", relationshipType);
      
      var $thisTree = $(this.nodeId);
      
      var parentNode = $thisTree.tree('getNodeById', parentId);
      if (parentNode == null || parentNode == undefined) {
        $thisTree.tree(
          'appendNode',
          {
              label: childId,
              id: childId
          }
        );
      }
      else {
        $thisTree.tree(
          'appendNode',
          {
              label: childId,
              id: childId
          },
          parentNode
        );
      }
    },
    
    removeNode : function(node) {
      this.__assertPrereqs();
      this.__assertRequire("node", node);
      
      var $thisTree = $(this.nodeId);
      
      $thisTree.tree(
        'removeNode',
        $thisTree.tree('getNodeById', node)
      );
    },
    
    /**
     * Internal, is binded to tree.contextmenu, called when the user right clicks on a node.
     */
    __onNodeRightClick : function(e) {
      // invoke our listeners
      for (i = 0; i < this.rightClickCallbacks.length; ++i) {
        this.rightClickCallbacks[i](e.node);
      }
    },
    
    /**
     * Internal, is binded to jqtree's node move event. You can prevent the move by calling event.preventDefault()
     */
    __onNodeMove : function(event) {
      var movedNode = event.move_info.moved_node;
      var targetNode = event.move_info.target_node;
      var previousParent = event.move_info.previous_parent;
      
      // invoke our listeners
      for (i = 0; i < this.moveCallbacks.length; ++i) {
        this.moveCallbacks[i](node);
      }
    },
    
    /**
     * Internal, Is binded to jqtree's node select (and deselect) event and invokes our term select listeners.
     */
    __onNodeSelect : function(e) {
      if (e.node) {
        // node was selected
        // invoke our listeners
        for (i = 0; i < that.selectCallbacks.length; ++i) {
          that.selectCallbacks[i](e.node);
        }
      }
      else {
        // event.node is null
        // a node was deselected
        // e.previous_node contains the deselected node
        var node = e.previous_node;
        
        // invoke our listeners
        for (i = 0; i < this.deselectCallbacks.length; ++i) {
          that.deselectCallbacks[i](node);
        }
      }
    },
    
    /**
     * Internal, is binded to jqtree's node open event.
     */
    __onNodeOpen : function(e) {
      var node = e.node;
      
      
    },
    
    /**
     * Internal method, do not call.
     */
    __handleException : function(ex) {
      var dialog = this.getFactory().newDialog("Error", {modal: true});
      dialog.appendContent(ex.getLocalizedMessage() || ex.getMessage() || ex.getDeveloperMessage());
      dialog.addButton("Ok", function(){dialog.close();});
      dialog.render();
    },
    
    /**
     * Internal method, do not call.
     */
    __assertPrereqs : function() {
      if (this.rootTermId == null || this.rootTermId == undefined) {
        var ex = new com.runwaysdk.Exception("You must call setRootTerm first before you can use this method.");
        this.__handleException(ex);
      }
    },
    
    /**
     * Internal method, do not call.
     */
    __assertRequire : function(name, value) {
      if (value == null || value == undefined) {
        var ex = new com.runwaysdk.Exception("Parameter [" + name + "] is required.");
        this.__handleException(ex);
      }
    }
  }
});

})();
