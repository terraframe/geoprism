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

var RUNWAY_UI = Mojo.Meta.alias("com.runwaysdk.ui.*");
Mojo.YUI2_PACKAGE = Mojo.UI_PACKAGE+'YUI2.';
var STRUCT = Mojo.Meta.alias(Mojo.ROOT_PACKAGE+'structure.*', {});

var Factory = Mojo.Meta.newClass(Mojo.YUI2_PACKAGE+'Factory', {
  
  IsSingleton : true,
  
  Implements : RUNWAY_UI.AbstractComponentFactoryIF,
  
  Instance: {
    newElement: function(el, attributes, styles) {
      /*
      if (RUNWAY_UI.Util.isElement(el)) {
        return el;
      }
      else {
        return new HTMLElement(el, attributes, styles);
      }
      */
      throw new com.runwaysdk.Exception('Not implemented');
    },
    newDocumentFragment : function(el){
      throw new com.runwaysdk.Exception('Not implemented');
    },
    newDialog : function (title, config) {
      return new Dialog(title, config);
//    	throw new com.runwaysdk.Exception("Not implemented");
    },
    newButton : function(label, handler, el) {
      //return new Button(label, handler, el);
      throw new com.runwaysdk.Exception('Not implemented');
    },
    newDataTable: function(){
      throw new com.runwaysdk.Exception('Not implemented');
      //return new com.runwaysdk.ui.DataTable();
    },
    newList : function (title, config, items) {
      throw new com.runwaysdk.Exception('Not implemented');
    },
    newListItem : function(data){
      throw new com.runwaysdk.Exception('Not implemented');
    },
    newDataTable : function (type) {
      throw new com.runwaysdk.Exception('Not implemented');
    },
    newColumn : function(config){
      throw new com.runwaysdk.Exception('Not implemented');
    },
    newRecord : function(obj){
      throw new com.runwaysdk.Exception('Not implemented');
    },
    makeDraggable : function(elProvider) {
      throw new com.runwaysdk.Exception('Not implemented');
    },
    makeDroppable : function(elProvider) {
      throw new com.runwaysdk.Exception('Not implemented');
    },
    newForm : function(name, config){
      throw new com.runwaysdk.Exception('Not implemented');
    },
    newFormControl : function(type, config){
      throw new com.runwaysdk.Exception('Not implemented');
    },
  }
});
//RUNWAY_UI.Manager.addFactory("YUI2", Factory);

/**
 * Base class of YUI2 wrapped widgets that contains YUI boilerplate code and
 * lifecycle management.
 */
var YUI2WidgetBase = Mojo.Meta.newClass(Mojo.YUI2_PACKAGE+'YUI2WidgetBase',{
  Extends : RUNWAY_UI.WidgetBase,
  IsAbstract : true,
  Instance : {
    initialize : function(id){
      this.$initialize(id);
    },
    destroy : function(){
      this.$destroy();
      
      var impl = this.getImpl();
      if(this._definesYUI2Destroy()){
        impl.destroy();
      }
    },
    show : function()
    {
      this.getImpl().show();
    },
    hide : function()
    {
      this.getImpl().hide();
    },
    /**
     * Checks if this instance delegates to a YUI2 class that defines destroy().
     * This is required per widget because YUI2 has no common base class that defines destroy().
     */
    _definesYUI2Destroy : {
      IsAbstract :true
    }
  }
});

/*
var Button = Mojo.Meta.newClass(Mojo.YUI2_PACKAGE+'Button',{
  
  Extends : YUI2WidgetBase,
  
  Implements : RUNWAY_UI.ButtonIF,
  
  Instance : {
    initialize: function(label, handler, el) {
      
      // FIXME : is there a better way to do this without a million if checks?
//      if (el == null) {
//        el = "input";
//      }
//      
//      if (el instanceof YAHOO.widget.Button) {
//        this._impl = el;
//      }
//      else if (el instanceof HTMLElement) {
//        this._impl = new YAHOO.widget.Button(el.getRawEl()); // FIXME : test this
//      }
//      else {
//        el = RUNWAY_UI.Util.stringToRawElement(el);
//        
//        if (el.nodeName.toUpperCase() === "BUTTON") {
//          throw new com.runwaysdk.Exception("No buttons allowed. YUI hates buttons.");
//        }
//        else if (el.nodeName.toUpperCase() === "INPUT") {
//          el.type = "button";
//        }
//        
//        this._impl = new YAHOO.widget.Button(el);
//      }
//      
//      this._impl.createButtonElement("input");
//      this._impl.render();
//      this._impl.cfg.queueProperty("label", label);
//      this._impl.cfg.queueProperty("onclick", handler);
      
     
      this._impl = new com.runwaysdk.ui.Button(label, handler);
     
      this.$initialize();
    },
    _definesYUI2Destroy : function() {
      //return this.getImpl() instanceof YAHOO.widget.Module;
      return true;
    },
    getImpl : function() {
      return this._impl;
    },
    getEl : function() {
      //return this.getFactory().newElement( this.getImpl()._button ); // FIXME : test this
      return this.getImpl().getEl();
    },
    getContentEl : function() {
      return this.getEl();
    },
    render : function(p) {
      // FIXME : This doesn't get called. Should this.getImpl().render() be called in base class?
      this.getImpl().render(p);
      this.$render(p);
    }
  }
});
*/

var Dialog = Mojo.Meta.newClass(Mojo.YUI2_PACKAGE+'Dialog', {
  
  Extends : YUI2WidgetBase,
  
  Implements : [RUNWAY_UI.DialogIF, RUNWAY_UI.ButtonProviderIF],
  
  Instance: {
    initialize : function(title, config) {
      config = config || {};
      
      this.$initialize(config.id);
      
      // Intelligent Config Defaults:
      config.width = config.width || "30em";
      config.fixedcenter = config.fixedcenter || true;
      config.visible = config.visible || true;
      config.constraintoviewport = config.constraintoviewport || true;
      
      // YUI2 doesn't know how to properly set a height, so lets do it for them
      if (config.height != null) {
        var height = config.height;
        delete config.height;
      }
      
      this._impl = new YAHOO.widget.SimpleDialog(this._generateDialogId(), config);
      
      this.setTitle(title);
      
      if (height != null) {
        this.getContentEl().setStyle("height", height);
      }
      
      this._buttons = new STRUCT.LinkedHashMap();
    },
    _generateDialogId : function() {
      return this.getId()+'_YUI2_Dialog';
    },
    _definesYUI2Destroy : function(){
      return this.getImpl() instanceof YAHOO.widget.Module;
    },
    getImpl : function() {
      return this._impl;
    },
    getEl : function() {
      return this.getFactory().newElement( this.getImpl().element );
    },
    close : function() {
      this.destroy();
    },
    getContentEl : function(){
      // The dialog's body is lazily created, so force its creation
      // if it does not exist as a public variable.
      var impl = this.getImpl();
      if(!impl.body) {
        impl.setBody('');
      }
      return this.getFactory().newElement(impl.body);
    },
    getHeader : function() {
      // The dialog's header is lazily created, so force its creation
      // if it does not exist as a public variable.
      var impl = this.getImpl();
      if(!impl.header) {
        impl.setHeader('');
      }
      return this.getFactory().newElement(impl.header);
    },
    getFooter : function() {
      // The dialog's footer is lazily created, so force its creation
      // if it does not exist as a public variable.
      var impl = this.getImpl();
      if(!impl.footer) {
        impl.setFooter('');
      }
      return this.getFactory().newElement(impl.footer);
    },
    setTitle : function (title) {
      this.getImpl().setHeader(title);
    },
    appendContent : function(content){
      if(Mojo.Util.isString(content)){
        this.setInnerHTML(content);
      }
      else {
        this.appendChild(content);
      }
    },
    appendChild : function(child) {
      this.getContentEl().appendChild(child);
    },
    setInnerHTML : function(str){
      this.getContentEl().setInnerHTML(str);
    },
    appendInnerHTML : function(str){
      this.getContentEl().appendInnerHTML(str);
    },
    addButton: function(label, handler, context) {
      var foot = this.getFooter();
      
      var buttonIF;
      if (com.runwaysdk.ui.ButtonIF.getMetaClass().isInstance(label)) {
        buttonIF = label;
      }
      else {
        buttonIF = this.getFactory().newButton(label, handler, context);
      }
      
      // FIXME : use layout manager
      foot.appendChild(buttonIF);
      
      return buttonIF;
      
      /*
      if (this.isRendered()) {
        this._buttons.put(config.label, {text: config.label || "", handler: config.handler, isDefault: config.isDefault || false});
        
        this.getImpl().cfg.setProperty("buttons", this._buttons.values());
        //this.getImpl().render();
      }
      else {
        this._buttons.put(config.label, {text: config.label || "", handler: config.handler, isDefault: config.isDefault || false});
        this.getImpl().cfg.setProperty("buttons", this._buttons.values());
        var buttons = this.getImpl().getButtons();
        throw new com.runwaysdk.Exception("Break me!");
      }
      */
    },
    removeButton : function() {
      
    },
    getButton : function() {
      
    },
    getButtons : function()
    {
      return this._buttons.values();
    },
		setClose : function(bool)
		{
			if (bool === true || bool === false)
			{
				this.getImpl().cfg.setProperty('close',bool);
			}
		},
    _render : function(parent){
    
      // delegate to YUI's render and mark the Dialog as rendered
      // to avoid the parent class performing redundant or erroneous 
      // render logic.
      parent = RUNWAY_UI.Util.toRawElement(parent || RUNWAY_UI.DOMFacade.getBody());
      this.getImpl().render(parent);
      this._setRendered(true);      
    },
    render : function(parent) {
      if (this._buttons.values().length > 0) {
        this.getImpl().cfg.queueProperty("buttons", this._buttons.values());
      }
      
      this._render(parent);
      this.$render(parent);
    }
  }
});

/*
var HTMLElement = Mojo.Meta.newClass(Mojo.YUI2_PACKAGE+'HTMLElement', {
  
    Extends : RUNWAY_UI.HTMLElementBase,
    
    Instance : {
      initialize : function(el, attributes, styles)
      {
        if (el instanceof YAHOO.util.Element) {
          this._impl = el;
        }
        else {
          el = RUNWAY_UI.Util.stringToRawElement(el);
          this._impl = new YAHOO.util.Element(el);
        }
        
        this.$initialize(el);
      },
      getAttribute : function(name)
      {
        //return this.getImpl().get(name);
      },
      setAttribute : function(name, value)
      {
        //this.getImpl().set(name, value);
      },
      addClassName : function(name)
      {
        this.getImpl().addClass(name);
      },
      hasClassName : function(name)
      {
        return this.getImpl().hasClass(name);
      },
      removeClassName : function(name)
      {
        this.getImpl().removeClass(name);
      },
      removeAttribute : function(attribute)
      {
        //this.setAttribute(attribute, null);
        RUNWAY_UI.DOMFacade.removeAttribute(this.getRawEl(), attribute);
      },
      getElementsByClassName : function(className, tag)
      {
        return this.getRawEl().getElementsByClassName(className, tag);
      },
      getRawEl : function()
      {
        return this.getAttribute("element");
      }
    }
});
*/

})();
