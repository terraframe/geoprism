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
  
  var ontologyTreeName = "net.geoprism.ontology.OntologyTree";
  var TermTree = net.geoprism.ontology.TermTree;
  
  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage(ontologyTreeName, {
    "deleteDescribe" : "Are you sure you want to delete '${termLabel}' and all of its children?",
  });
  
  /**
   * Adds a 'new country' button, changes some default term tree language and delegates to the term tree for all the heavy lifting.
   */
  var universalTree = ClassFramework.newClass(ontologyTreeName, {
    
    Extends : TermTree,
    
    Instance : {
      
      initialize : function(config) {
        
        config = config || {};
        this._config = config;
        
        config.exportMenuType = "net.geoprism.ontologyerExportMenu";
        
        this.$initialize(config);
        
        this._wrapperDiv = this.getFactory().newElement("div");
        
      },
      
      _createNodeRightClickMenu : function(event) {
        var node = event.node;    	  
        var parentId = this.__getRunwayIdFromNode(node.parent);    	  
    	  
        // Display the standard context menu        
        var items = this.$_createNodeRightClickMenu(event);
        
        // Disable the delete functionality if the node represents a root node.
        for(var i = 0; i < items.length; i++) {
          var item = items[i];
          
          if (item.id == 'delete' && this.isRootTermId(parentId)) {
            item.enabled = false;
          }
        }
        
        return items;
      },
      
      destroy : function() {
        
        if (!this.isDestroyed()) {
          this.$destory();
          this._wrapperDiv.destory();
        }
        
      },
      
      render : function(parent, nodes) {
        this._wrapperDiv.render(parent);
        
        this.$render(this._wrapperDiv, nodes);
      }
    }
  });
  
  return universalTree;
  
})();
