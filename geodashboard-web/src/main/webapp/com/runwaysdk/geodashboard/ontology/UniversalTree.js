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
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(config) {
        
        config = config || {};
        config.language = config.language || {};
        Util.merge(com.runwaysdk.Localize.getLanguage(universalTreeName), config.language); // Override the term tree's language with our language.
        this._config = config;
        
        this._termTree = new com.runwaysdk.geodashboard.ontology.TermTree(config);
        
        this.$initialize("div");
      },
      
      _onClickNewCountry : function() {
        this._termTree.createTerm(this._termTree.rootTermId);
      },
      
      render : function(parent) {
        
        var createCountry = this.getFactory().newButton(this.localize("newCountry"), Mojo.Util.bind(this, this._onClickNewCountry));
        this.appendChild(createCountry);
        
        this.appendChild(this._termTree);
        
        this.$render(parent);
        
      }
    }
  });
  
  return universalTree;
  
})();