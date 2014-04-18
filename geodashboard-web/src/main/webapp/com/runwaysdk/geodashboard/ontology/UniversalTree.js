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
  
  var universalTreeName = "com.runwaysdk.geodashboard.ontology.UniversalTree";
  var TermTree = com.runwaysdk.geodashboard.ontology.TermTree;
  
  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage(universalTreeName, {
    "newCountry" : "New Country",
    "deleteDescribe" : "Are you sure you want to delete '${termLabel}' and all GeoEntities that may reference it?"
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
        
        this.$initialize(config);
        
        this._wrapperDiv = this.getFactory().newElement("div");
        
      },
      
      /**
       * Removes the term and all children terms from the caches.
       */
      __dropAll : function(termId) {
        var that = this;
        var nodes = this.__getNodesById(termId);
        
        // Children of universals are appended to the root node
        // The children are repeated under the copies, so we only want to append one set of the children to the root node.
        var node = nodes[0];
        var children = this.getChildren(node);
        var len = children.length;
        for (var i = 0; i < len; ++i) {
          var child = children[i];
          var childId = that.__getRunwayIdFromNode(child);
          
          this.__dropAll(childId);
          
          this.parentRelationshipCache.removeRecordMatchingId(childId, termId, that);
        }
        
        delete this.termCache[termId];
      },
      
      // @Override
      refreshTreeAfterDeleteTerm : function(termId) {
        var nodes = this.__getNodesById(termId);
        var $tree = this.getImpl();
        var shouldRefresh = !($tree.jstree("is_loaded", nodes[0]) && ($tree.jstree("get_children_dom", nodes[0]).length <= 0));
        
        this.parentRelationshipCache.removeAll(termId);
        
        this.__dropAll(termId);
        
        for (var i = 0; i < nodes.length; ++i) {
          $tree.jstree(
            'delete_node',
            nodes[i]
          );
        }
        
        if (shouldRefresh) {
          // We don't have the relationship id of the new relationship between the children and the root node. 
          this.refreshTerm(this.rootTermId);
        }
      },
      
      _onClickNewCountry : function() {
        this.createTerm(this.rootTermId, {id:"#"});
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