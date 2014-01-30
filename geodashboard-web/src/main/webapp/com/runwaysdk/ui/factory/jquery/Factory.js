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
/**
 * RunwaySDK Javascript UI Adapter for YUI2.
 * 
 * @author Terraframe
 */
//define(["../runway/runway"], function(){
(function(){

  var RUNWAY_UI = Mojo.Meta.alias(Mojo.UI_PACKAGE + "*");
  Mojo.JQUERY_PACKAGE = Mojo.FACTORY_PACKAGE+'jquery.';
  
  var Factory = Mojo.Meta.newClass(Mojo.JQUERY_PACKAGE+'Factory', {
    
    IsSingleton : true,
    
    Implements : RUNWAY_UI.AbstractComponentFactoryIF,
    
    Instance: {
      newElement: function(el, attributes, styles) {
        if (RUNWAY_UI.Util.isElement(el)) {
          return el;
        }
        else {
//          return new JQ_UI.HTMLElement(el, attributes, styles);
          return RUNWAY_UI.Manager.getFactory("Runway").newElement(el, attributes, styles);
        }
      },
      newDocumentFragment : function(el){
        throw new com.runwaysdk.Exception('Not implemented');
      },
      newDialog : function (title, config) {
        return new com.runwaysdk.ui.factory.jquery.Dialog(title, config);
      },
      newButton : function(label, handler, context) {
        //return new Button(label, handler, el);
        return RUNWAY_UI.Manager.getFactory("Runway").newButton(label, handler, context);
      },
      newList : function (title, config, items) {
        return RUNWAY_UI.Manager.getFactory("Runway").newList(title, config, items);
      },
      newListItem : function(data){
        return RUNWAY_UI.Manager.getFactory("Runway").newListItem(data);
      },
      newDataTable: function(cfg){
        return new com.runwaysdk.ui.factory.jquery.datatable.DataTable(cfg);
      },
      newColumn : function(config){
        return RUNWAY_UI.Manager.getFactory("Runway").newColumn(config);
      },
      newRecord : function(config){
        return RUNWAY_UI.Manager.getFactory("Runway").newRecord(config);
      },
      makeDraggable : function(elProvider, config) {
        RUNWAY_UI.Manager.getFactory("Runway").makeDraggable(elProvider, config);
      },
      makeDroppable : function(elProvider, config) {
        throw new com.runwaysdk.Exception('Not implemented');
      },
      newForm : function(name, config){
        return RUNWAY_UI.Manager.getFactory("Runway").newForm(name, config);
      },
      newFormControl : function(type, config){
        return RUNWAY_UI.Manager.getFactory("Runway").newFormControl(type, config);
      },
      newTree : function(config) {
        return new com.runwaysdk.ui.factory.jquery.Tree(config);
      },
      newContextMenu : function(config) {
        return new RUNWAY_UI.Manager.getFactory("Runway").newContextMenu(config);
      },
      newTabPanel : function(config) {
        return new com.runwaysdk.ui.factory.jquery.tabpanel.TabPanel(config);
      }
    }
  });
  RUNWAY_UI.Manager.addFactory("JQuery", Factory);
  
})();
