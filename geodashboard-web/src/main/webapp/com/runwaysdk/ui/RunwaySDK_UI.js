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
 * RunwaySDK Javascript UI library.
 * 
 * @author Terraframe
 */

//define(["../RunwaySDK_Core"], function(){
(function(){

Mojo.UI_PACKAGE = Mojo.ROOT_PACKAGE+'ui.';
Mojo.FACTORY_PACKAGE = Mojo.UI_PACKAGE+'factory.';
Mojo.FORM_PACKAGE = {
  FORM: Mojo.ROOT_PACKAGE+'form.',
  WEB: Mojo.ROOT_PACKAGE+'form.web.',
  FIELD: Mojo.ROOT_PACKAGE+'form.web.field.',
  METADATA: Mojo.ROOT_PACKAGE+'form.web.metadata.',
  CONDITION: Mojo.ROOT_PACKAGE+'form.web.condition.'
};

// FIXME use module shortcut from RunwaySDK.js to access these (e.g., Mojo.EVENT)
var EVENT = Mojo.Meta.alias(Mojo.ROOT_PACKAGE+'event.*', {});
var STRUCT = Mojo.Meta.alias(Mojo.ROOT_PACKAGE+'structure.*', {});

var Manager = Mojo.Meta.newClass(Mojo.UI_PACKAGE+'Manager', {
  
  IsSingleton : true,
  
  Static : {
    setFactory : function(factory)
    {
      Manager.getInstance().setFactory(factory);
    },
    getFactory : function(factory)
    {
      return Manager.getInstance().getFactory(factory);
    },
    addFactory : function(key, factoryClassRef) {
      return Manager.getInstance().addFactory(key, factoryClassRef);
    },
    getAvailableFactories : function() {
      return Manager.getInstance().getAvailableFactories();
    },
    onRegisterFactory : function(fn) {
      Manager.getInstance().onRegisterFactory(fn);
    },
    getFactoryName : function() {
      return Manager.getInstance().getFactoryName();
    }
  },
  
  Instance : {
    initialize : function()
    {
      this._factories = {};
      this._listeners = [];
    },
    setFactory : function(key)
    {
      if (!Mojo.Util.isObject(this._factories[key]))
      {
        throw new com.runwaysdk.Exception('The provided factory name ['+key+'] is not defined.');
      }
      
      this._factory = this._factories[key];
      this._factoryName = key;
    },
    getAvailableFactories : function() {
      var keys = [];
      for (var key in this._factories) {
        if (this._factories.hasOwnProperty(key)) {
          keys.push(key);
        }
      }
      return keys;
    },
    getFactory : function(name)
    {
      if (name == null || name == undefined) {
        return this._factory;
      }
      else {
        return this._factories[name];
      }
    },
    getFactoryName : function() {
      return this._factoryName;
    },
    addFactory : function(key, factoryClassRef) {
      this._factories[key] = factoryClassRef.getInstance();
      
      for (var i = 0; i < this._listeners.length; ++i) {
        this._listeners[i](key);
      }
    },
    onRegisterFactory : function(fn) {
      this._listeners.push(fn);
    }
  }
});

/**
 * Abstract Factory to create a set of related UI widgets from distinct toolkits.
 * Its a Widget Factory that Javascript frameworks implement.
 */
var AbstractComponentFactoryIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+'AbstractComponentFactoryIF', {
  
  Instance : {
    // The El parameter can be:
    // A string : if the string has a # at the beginning then it finds the element via the proceeding ID
    //            if the string does not have a # at the beginning then it creates a new element with the string
    // An HTMLElementIF, in which case it does nothing and returns el
    // An instance of an element of the underlying implementation, in which case it wraps it and returns a HTMLElementIF wrapper
    // A raw DOM element, in which case it wraps it and returns a HTMLElementIF wrapper.
    newElement : function(el, attributes, styles){},
    
    newDocumentFragment : function(el){},
    
    newDialog : function(title, config){},
    
    // The El parameter can be:
    // A string : if the string has a # at the beginning then it finds the element via the proceeding ID
    //            if the string does not have a # at the beginning then it creates a new element with the string
    // An HTMLElementIF
    // An instance of an element of the underlying button implementation
    // A raw DOM element of possible tag type: button, input (type = button), a, span, div
    // Null, in which case it automatically creates a button tag
    newButton : function(label, handler, el){},
    
    newForm : function(name, config){},
    
    newFormControl : function(type, config){},
    
    newDataTable : function(config){},
    
    newColumn : function(config){},
    
    newRecord : function(config){},
    
    newList : function(config){},
    
    newListItem : function(config){},
    
    makeDraggable : function(elProvider, config){},
    
    makeDroppable : function(elProvider, config){}
  }
});

var ComponentIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+'ComponentIF', {
  Instance : {
    getId : function(){},
    setId : function(id){},
    getParent : function(){},
    appendChild : function(child){},
    insertBefore : function(newChild, refChild){},
    /**
     * Returns an array of Component child objects in the order
     * in which they were appended via appendChild().
     */
//    getChildren : function(){},
//    getChild : function(id){},
//    hasChild : function(child){},
    removeChild : function(child){},
    replaceChild : function(newChild, oldChild){},
    render : function(){},
    isRendered : function(){},
    destroy : function(){}
  }
});

/**
 * Component
 * Essentially a UI base class for both adapters to extend as well as Runway implementations.
 */
var Component = Mojo.Meta.newClass(Mojo.UI_PACKAGE+'Component',{
  Implements: [ComponentIF],
  IsAbstract : true,
  Instance : {
    initialize : function(id)
    {
      id = id || this._generateId();
      this.setId(id);
      this._parent = null;
      this._rendered = false;
      this._isDestroyed = false;
    },
    getManager: function()
    {
      return Manager.getInstance();
    },
    getFactory : function()
    {
      return this.getManager().getFactory();
    },
    setParent : function(parent)
    {
      if (Mojo.Util.isUndefined(parent)) {
        throw new com.runwaysdk.Exception("The argument to Component.setParent cannot be undefined.");
      }
      
      // Parent is allowed to be null
      if (parent !== null && !Util.isComponentIF(parent)) {
        throw new com.runwaysdk.Exception("The argument to Component.setParent must implement ComponentIF.");
      }
      
      this._parent = parent;
    },
    getParent : function()
    {
      return this._parent;
    },
    getId : function()
    {
      return this._id;
    },
    getHashCode : function()
    {
      return this.getId();
    },
    equals : function(obj)
    {
      var eq = this.$equals(obj);
      if(eq)
      {
        return true;
      }
      else if(obj instanceof Mojo.$.com.runwaysdk.Base
        && this.getId() === obj.getId())
      {
        return true;
      }
      
      return this === obj;
    },
    setId : function(id)
    {
      this._id = id;
    },
    _generateId : function()
    {
      return this.getMetaClass().getName()+'_'+Mojo.Util.generateId(16);
    },
    render : function()
    {
      this._rendered = true;
    },
    isRendered : function()
    {
      return this._rendered;
    },
    _setRendered : function(rendered)
    {
      this._rendered = rendered;
    },
    replaceChild : function(newChild, oldChild){
      if (!Util.isComponentIF(newChild) || !Util.isComponentIF(oldChild)) {
        throw new com.runwaysdk.Exception("The arguments must implement ComponentIF.");
      }
      
      return oldChild;
    },
    appendChild : function(child)
    {
      if (!Util.isComponentIF(child)) {
        throw new com.runwaysdk.Exception("The argument to Component.appendChild must implement ComponentIF.");
      }
      
      child.setParent(this);
      return child;
    },
    insertBefore : function(newChild, refChild) {
      if (!Util.isComponentIF(newChild) || !Util.isComponentIF(refChild)) {
        throw new com.runwaysdk.Exception("The arguments must implement ComponentIF.");
      }

      newChild.setParent(this);
      return newChild;
    },
    removeChild : function(child)
    {
      if (!Util.isComponentIF(child)) {
        throw new com.runwaysdk.Exception("The argument to Component.removeChild must implement ComponentIF.");
      }
      
      child.setParent(null);
      return child;
    },
    destroy : function()
    {
      if(this._isDestroyed){
        throw new com.runwaysdk.Exception('Cannot call Component.destroy() more than once on ['+this+'].');
      }
      else {
        this.$destroy();
        this._isDestroyed = true;
      }
    },
    isDestroyed : function() {
      return this._isDestroyed;
    },
    
    addDestroyEventListener : function(fnListener) {
      this.addEventListener(com.runwaysdk.event.DestroyEvent, {handleEvent: fnListener});
    },
    
    handleException : function(ex, throwIt) {
      try {
        var msg = ex.getMessage();
        
        if (ex instanceof com.runwaysdk.ProblemExceptionDTO && msg == null) {
          var problems = ex.getProblems();
          
          msg = com.runwaysdk.Localize.get("problems", "Problems processing request:\n");
          
          for (var i = 0; i < problems.length; ++i) {
            msg = msg + problems[i].getMessage() + "\n";
          }
        }
        
        var dialog = this.getFactory().newDialog(com.runwaysdk.Localize.get("rError", "Error"), {modal: true});
        dialog.appendContent(msg);
        dialog.addButton(com.runwaysdk.Localize.get("rOk", "Ok"), function(){dialog.close();});
        dialog.render();
        
        if (throwIt) {
          throw ex;
        }
      }
      catch(e2) {
        throw ex;
      }
    },
    
    localize : function(key, defaultValue) {
      return com.runwaysdk.Localize.get(this.getMetaClass().getQualifiedName() + "." + key, defaultValue);
    },
    
    requireParameter : function(name, value) {
      Mojo.Util.requireParameter(name, value);
    },
    
    /**
     * Dispatches the given event. Note that custom events do not support
     * a capturing phase.
     */
    dispatchEvent : function(evt)
    {
      if(evt.getEventPhase() === EVENT.EventIF.AT_TARGET)
      {
        evt._setTarget(this);
      }
      
      evt._setCurrentTarget(this);
      
      // dispatch the event for all listeners of this object (the current target)
      Mojo.$.com.runwaysdk.event.Registry.getInstance().dispatchEvent(evt);
      
      // simulate bubbling by dispatching the event on this object's parent
      if(evt.getBubbles() && !evt.getStopPropagation() && this.getParent() != null)
      {
        evt._setEventPhase(EVENT.EventIF.BUBBLING_PHASE);
        this.getParent().dispatchEvent(evt);
      }
      
      if(evt.getTarget().equals(this) && !evt.getPreventDefault())
      {
        evt.defaultAction();
      }
    }
  }
});

var Composite = Mojo.Meta.newClass(Mojo.UI_PACKAGE+'Composite', {
  IsAbstract : true,
  Extends : Component,
  Instance : {
    initialize : function(id)
    {
      this.$initialize.apply(this, arguments);
      this._components = new STRUCT.LinkedHashMap();
    },
    setComponents : function(components) {
      if (components instanceof STRUCT.LinkedHashMap) {
        this._components = components;
      }
      else {
        this._components = new STRUCT.LinkedHashMap(components);
      }
      
      var children = this.getChildren();
      for (var i = 0; i < children.length; ++i) {
        children[i].setParent(this);
      }
    },
    /**
     * Sets the link between the parent and child and adds the child to the
     * underlying composite structure.
     */
    appendChild : function(child)
    {
      if (!(child instanceof Component)) {
        child = Util.toElement(child);
      }
      
      this._components.put(child.getId(), child);
      
      return this.$appendChild(child);
    },
    insertBefore : function(newChild, refChild) {
      this._components.insert(newChild.getId(), newChild, refChild.getId());
      return this.$insertBefore(newChild, refChild);
    },
    replaceChild : function(newChild, oldChild){
      this._components.replace(newChild.getId(), newChild, oldChild);
      return this.$replaceChild(newChild, oldChild)
    },
    /**
     * Checks if the given ComponentIF or id string is a valid child
     * of this component.
     */
    hasChild : function(child){
      return this._components.containsKey(child);
    },
    getChild : function(id){
      return this._components.get(id);
    },
    /**
     * Returns an array of all child components in the order in which they
     * were appended to this component.
     */
    getChildren : function(){
      return this._components.values();
    },
    removeChild : function(child)
    {
      this._components.remove(child.getId());
      return this.$removeChild(child);
    },
    render : function(parent) {
      if (!this.isRendered()) {
        this.$render();
        
        var components = this._components.values();
        for(var i=0; i<components.length; i++)
        {
          components[i].render(this);
        }
      }
    },
    destroy : function()
    {
      if(!this.isDestroyed()){
        var components = this._components.values();
        for(var i=0; i<components.length; i++)
        {
          components[i].destroy();
        }
        this._components = null;
      }
      
      this.$destroy();
    }
  }
});

/*
 * UI bridge Interfaces
 */

var NodeIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+'NodeIF', {
  Extends : ComponentIF,
  
  Constants : {
    ELEMENT_NODE : 1,
    ATTRIBUTE_NODE : 2,
    TEXT_NODE : 3,
    CDATA_SECTION_NODE : 4,
    ENTITY_REFERENCE_NODE : 5,
    ENTITY_NODE : 6,
    PROCESSING_INSTRUCTION_NODE : 7,
    COMMENT_NODE : 8,
    DOCUMENT_NODE : 9,
    DOCUMENT_TYPE_NODE : 10,
    DOCUMENT_FRAGMENT_NODE : 11,
    NOTATION_NODE : 12  
  },
  Instance : {
    // DOM Methods
    appendChild : function(newChild){},
    cloneNode : function(deep){},
    hasAttributes : function(){},
    hasChildNodes : function(){},
    insertBefore : function(newChild, refChild){},
    isSupported : function(feature, version){},

    removeChild : function(oldChild){},
    replaceChild : function(newChild, oldChild){},
    
    // Accessors for DOM attributes (not the attr kind)
    getNodeName : function(){},
    getNodeValue : function(){},
    getNodeType : function(){},
    getParentNode : function(){},
    getChildNodes : function(){},
    getLastChild : function(){},
    getPreviousSibling : function(){},
    getNextSibling : function(){},
    getOwnerDocument : function(){},
    
    // Runway Methods
    getRawNode : function(){},
    getImpl : function(){}    // Returns NodeIF
  }
});

var AttrIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+'AttrIF', {
  Extends : NodeIF,
  Instance : {
    getName : function(){},
    setValue : function(value){},
    getValue : function(){},
    getOwnerElement : function(){},
  }
});

var ElementIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+'ElementIF', {
  Extends : ComponentIF,
  Instance : {
    getAttribute : function(name){},
    setAttribute : function(name, value){},
    setAttributes : function(attrs){},
    removeAttribute : function(name){},
    hasAttribute : function(attribute){},
    appendChild : function(child){},
    removeChild : function(child){},
    replaceChild : function(oldChild, newChild){},
    addEventListener : function(oTarget, sEvent, oListener, bUseCapture){},
    removeEventListener : function(oTarget, sEvent, oListener, bUseCapture){},
    getImpl : function(name){},
    normalize : function(){},
  }
});

var HTMLElementIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+'HTMLElementIF', {
  Extends: ElementIF,
  Instance : {
    setInnerHTML:function(html){},
    appendInnerHTML:function(html){},
    getInnerHTML:function(){},
    hasClassName:function(c){},
    addClassName:function(c){},
    addClassNames:function(obj){},
    removeClassName:function(c){},
    setStyle:function(property, value){},
    setStyles:function(styles){},
    getStyle:function(property){},
    getParent:function(){},
    getElementsByClassName : function(className, tag){},
    getRawEl : function(){},
  }
});

var ButtonProviderIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+'ButtonProviderIF', {
  Instance : {
    getButtons : function(){}
  }
});

var ButtonIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+'ButtonIF', {
  Instance : {
    
  }
});

var DialogIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+'DialogIF', {
  Extends : ComponentIF,
  
  Instance : {
    setTitle : function(HtmlElement_Or_Text){},
    setInnerHTML : function(HtmlElement_Or_Text){},
    appendInnerHTML : function(HtmlElement_Or_Text){},
    addButton : function(label, handler, isDefault){},
    removeButton : function(buttonIF){},
    getButton : function(integer){},
    getButtons : function(){},
    getHeader : function(){},
    getFooter : function(){},
    show : function(){},
    hide : function(){}
  }
});

var ListIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+'ListIF', {
  Extends : ComponentIF,
  
  Instance : {
    addItem : function(item){},
    removeItem : function(item){},
    clearItems : function(){},
    hasItem : function(item){},
    getItem : function(num_or_str){},
    insertBefore : function(newItem, refItem){},
    replaceItem : function(newItem, oldItem){},
    getAllItems : function(){},
    size : function(){}
  }
});

var ListItemIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+'ListItemIF', {
  Extends : ComponentIF,
  
  Instance : {
    getData:function(){},
    hasLI:function(HTMLElementIF_or_RawDOM){}
  }
});

/**
 * Simple interface that marks the any implementing class as operating on an underlying
 * ElementIF object. This interface is useful for classes that perform complex operations and
 * decorate Elements yet still want to expose the underlying ElementIF to DOM operations.
 */
var ElementProviderIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+'ElementProviderIF', {
  Instance : {
    getEl : function(){},
    getContentEl : function(){}
  }
});

/**
 * @deprecated
 * This class is now completely deprecated. Use Widget (from the runway factory) instead.
 */
var WidgetBase = Mojo.Meta.newClass(Mojo.UI_PACKAGE+'WidgetBase',{
  Implements: ElementProviderIF,
  Extends: Composite,
  IsAbstract : true,
  Instance : {
    initialize : function(id)
    {
      this.$initialize(id);
    },
    appendChild : function(child){
      if(ElementProviderIF.getMetaClass().isInstance(child)){
        this.getContentEl().appendChild(child);
      }
      
      this.$appendChild(child);
    },
    removeChild : function(child){
      if(ElementProviderIF.getMetaClass().isInstance(child)){
        this.getContentEl().removeChild(child);
      }
      
      this.$removeChild(child);
    },
    /**
     * Makes the final Widget DOM structure connect
     * to the live DOM Tree. Subclasses may override this
     * to do custom rendering that delegates to YUI, for example.
     */
    _render : function(parent){
      // perform the final append to the DOM
      parent = Util.toElement(parent || DOMFacade.getBody());
      parent.appendChild(this.getEl());    
    },
    render : function(parent){
    
      // Subclasses may have their own rendering flow, so if the widget
      // is marked as rendered don't do anything except super to the 
      // parent render method.
      // FIXME clean this up
      if(!this.isRendered()){
        var el = this.getEl();
        var cEl = this.getContentEl();
        
        // don't append the content el to the bounding el
        // if they are the same Element
        if(!el.equals(cEl)){
          el.appendChild(cEl);
        }

      }
      this._render(parent);
      this.$render(parent);
    },
    destroy : function(){
      this.$destroy();
      
      // remove primary element event handlers
      var el = this.getEl();
      var cEl = this.getContentEl();
      if(!cEl.equals(el)){
        cEl.destroy(); 
      }
      
      el.destroy();
    },
    getImpl : {
      IsAbstract : true
    }
  }
});

/**
 * @deprecated
 * This class is now completely deprecated. Factories are no longer required to implement their own HTMLElements.
 */
var HTMLElementBase = Mojo.Meta.newClass(Mojo.UI_PACKAGE+'HTMLElementBase',{
  Implements: [HTMLElementIF, ElementProviderIF],
  Extends: Component,
  IsAbstract : true,
  Instance : {
    initialize : function(el, attributes, styles)
    {
      this.$initialize();  //_generateId() will take care of this object's id
      this.setAttributes(attributes);
      this.setStyles(styles);
    },
    setId : function(id)
    {
      DOMFacade.setAttribute(this.getRawEl(), 'id', id);
      this.$setId(id);
    },
    getId : function()
    {
      var id = DOMFacade.getAttribute(this.getRawEl(), 'id');
      var wId = this.$getId();
      if ((this.$getId() !== id) && (Mojo.Util.isValid(id)) && (id !== ""))
      {
        this.$setId(id);
      }
      return id;
    },
    /**
     * Returns the underlying DOM Element instance.
     */
    equals : function(el)
    {
      if (this.$equals(el))
      {
        return true;
      }
      else if (Util.isRawElement(el))
      {
        if (this.getId() === el.id)
        {
          return true;
        }
      }
      return false;
    },
    getAttribute : function(name)
    {
      if (!this.hasAttribute(name)) {
        return null;
      }
      return this.getImpl().getAttribute(name);
    },
    setAttribute : function(name, value)
    {
      this.getImpl().setAttribute(name, String(value));
    },
    setAttributes : function(attrs)
    {
      for (var key in attrs) {
        this.setAttribute(key, attrs[key]);
      }
    },
    removeAttribute : function(name)
    {
      return this.getImpl().removeAttribute(name);
    },
    hasAttribute : function(name) {
      return this.getImpl().hasAttribute(name);
    },
    addClassName : function(name)
    {
      return this.getImpl().addClassName(name);
    },
    addClassNames : function(obj) {
      for (key in obj) {
        this.addClassName(obj[key]);
      }
    },
    hasClassName : function(name)
    {
      return this.getImpl().hasClassName(name);
    },
    removeClassName : function(name)
    {
      return this.getImpl().removeClassName(name);
    },
    setInnerHTML : function(html)
    {
      return this.getImpl().setInnerHTML(html);
    },
    getInnerHTML : function()
    {
      return this.getImpl().getInnerHTML();
    },
    appendInnerHTML : function(html)
    {
      return this.setInnerHTML(this.getInnerHTML() + html);
    },
    getChild : function(id){
      return this.getFactory().newElement(id);
    },
    hasChild : function(child){
      var id = Util.isComponentIF(child) ? child.getId() : child;
      return DOMFacade.getElementById(id) !== null;
    },
    replaceChild : function(newChild, oldChild){
      newChild = Util.toRawElement(newChild);
      oldChild = Util.toRawElement(oldChild);
      this.getImpl().replaceChild(newChild, oldChild);
      return oldChild;
    },
    appendChild : function(newChild)
    {
      if (Util.isComponentIF(newChild))
      {
        this.$appendChild(newChild);
      }
      var newChildRaw = Util.toRawElement(newChild);
      this.getImpl().appendChild(newChildRaw);
      return newChild;
    },
    insertBefore : function(newChild, refChild) {
      if (Util.isComponentIF(newChild))
      {
        this.$insertBefore(newChild, refChild);
      }
      var newChildRaw = Util.toRawElement(newChild);
      refChild = Util.toRawElement(refChild);
      this.getImpl().insertBefore(newChildRaw, refChild);
      return newChild;
    },
    removeChild : function(oldChild)
    {
      if (Util.isComponentIF(oldChild))
      {
        this.$removeChild(oldChild);
      }
      oldChild = Util.toRawElement(oldChild);
      this.getImpl().removeChild(oldChild);
      return oldChild;
    },
    setStyle : function(property, value)
    {
      return this.getImpl().setStyle(property, value);
    },
    setStyles : function(styles)
    {
      for (var key in styles) {
        this.setStyle(key, styles[key]);
      }
    },
    getStyle : function(property)
    {
      return this.getImpl().getStyle(property);
    },
    getElementsByClassName : function(className, tag)
    {
      return this.getImpl().getElementsByClassName(className, tag);
    },
    getImpl : function()
    {
      return this._impl;
    },
    dispatchEvent : function(evt)
    {
      // FIXME Justin - use Registry
      // dispatch the event for all listeners of this object (the current target)
      if (evt instanceof com.runwaysdk.event.Event) 
      {
        return com.runwaysdk.event.EventUtil.dispatchEvent(this.getRawEl(), evt.getEvent());
      }
      else {
        return this.$dispatchEvent(evt);
      }
    },
    getEl : function() {
      return this;
    },
    getContentEl : function() {
      return this.getEl();
    },
    normalize : function() {
      this.getImpl().normalize();
    },
    cloneNode : function(deep){
      return this.getImpl().cloneNode(deep);
    },
    render : function(parent) {
      this.$render();
      
      if (parent == null) {
        parent = DOMFacade.getBody();
      }
      else {
        parent = Util.toElement(parent);
      }
      parent.appendChild(this);
    }
  }
});

/**
 * Helper class to access and modify the dom in a safe, cross-browser manner.
 */
var cursorX;
var cursorY;
document.onmousemove = function(e){
    cursorX = e.pageX;
    cursorY = e.pageY;
};
var DOMFacade = Mojo.Meta.newClass(Mojo.UI_PACKAGE+'DOMFacade', {
  
  Static : {
    
    /**
     * The provided function, func, will be executed after the page is loaded.
     * 
     * @param func The function to execute.
     */
    execOnPageLoad : function(func) {
      if(window.attachEvent) {
        window.attachEvent('onload', func);
      } else {
          if(window.onload) {
              var curronload = window.onload;
              var newonload = function() {
                  curronload();
                  func();
              };
              window.onload = newonload;
          } else {
              window.onload = func;
          }
      }
    },
    
    getMousePos : function() {
      return {x: cursorX + "px", y: cursorY + "px"};
    },
  
    convertNodeTypeToString : function(nodeType) {
      if (nodeType === 1) {
        return "Element";
      }
      else {
        throw new com.runwaysdk.Exception("Unsupported");
      }
    },
    
    createDocumentFragment : function()
    {
      return document.createDocumentFragment();
    },
  
    createElement : function(type, attributes, styles)
    {
      var el = document.createElement(type);
     
      DOMFacade.updateElement(el, attributes, styles);
      
      return el;
    },
    
    updateElement : function(el, attributes, styles)
    {
      el = Util.toRawElement(el);
  
      if(Mojo.Util.isObject(attributes))
      {
        for(var attr in attributes)
        {
          DOMFacade.setAttribute(el, attr, attributes[attr]);
        }
      }
      
      if(Mojo.Util.isObject(styles))
      {
        for(var style in styles)
        {
          DOMFacade.setStyle(el, style, styles[style]);
        }
      }
    },
    
    getTextContent : function(el)
    {
    // FIXME null check may not be sufficient
      return el.textContent != null ? el.textContent : el.innerText;
    },
    
    setTextContent : function(el, text)
    {
    // FIXME null check may not be sufficient
      if(el.textContent != null)
      {
        el.textContent = text;
      }
      else
      {
        el.innerText = text;
      }
    },
    createAttribute : function(name, value)
    {
      var newAttr = document.createAttribute(name);
      if (Mojo.Util.isValid(value))
      {
        newAttr.nodeValue = value;
      }
      return newAttr;
    },
    
    setAttribute : function(el, key, value)
    {
      // Note: The implementation of the className functions is dependent upon the implementation of the get/set attribute functions!
      // Good ol DOM. If we implement get/set attribute as bracket notation, then we get/set the classname via the string 'className'
      // However, if we implement it via the get/set attribute function then we get/set the classname via the string 'class'.
      // Since we're using the actual function here, we want to normalize any 'className' strings to 'class'. They shouldn't have to
      // know how we actually set the class attribute.
      if (key == "className") {
        key = "class";
      }
      else if (key === "innerHTML") {
        DOMFacade.setInnerHTML(el, value);
        return;
      }
      
      // they're trying to use on-something event handling. Quick! Stop them!
      //if (Mojo.IS_DEBUG_LEVEL) {
        if ((Mojo.Util.isFunction(value) || Mojo.Util.isString(value)) && key.search(/on/i) == 0) // TODO: instanceof check with HtmlElement
          throw new com.runwaysdk.Exception("DOM Level 0 event registration is discouraged.");
      //}
      
      el = Util.toRawElement(el);
      
      if (Mojo.Util.isFunction(el.setAttribute)) {
        el.setAttribute(key, String(value));
      }
      else {
        if (key == "class") {
          key = "className";
        }
        el[key] = value;
      }
    },
    
    setId : function(el, id) {
      DOMFacade.setAttribute(el, "id", id);
    },
    
    getId : function(el) {
      return DOMFacade.getAttribute(el, "id");
    },
    
    setAttributes : function(el, obj) {
      if (obj == null) { return; }
      
      throw new com.ruwnaysdk.Exception("Not implemented.");
    },
    
    getAttribute : function(el, key)
    {
      // Note: The implementation of the className functions is dependent upon the implementation of the get/set attribute functions!
      // Good ol DOM. If we implement get/set attribute as bracket notation, then we get/set the classname via the string 'className'
      // However, if we implement it via the get/set attribute function then we get/set the classname via the string 'class'.
      // Since we're using the actual function here, we want to normalize any 'className' strings to 'class'. They shouldn't have to
      // know how we actually set the class attribute.
      if (key == "className")
        key = "class";
      
      el = Util.toRawElement(el);
      
      if (Mojo.Util.isFunction(el.getAttribute)) {
        return el.getAttribute(key);
      }
      else {
        if (key == "class") {
          key = "className";
        }
        el[key] = value;
      }
    },
    
    removeAttribute : function(el, attribute) {
      el = Util.toRawElement(el);
      
      if (this.hasAttribute(el, attribute)) {
        this.setAttribute(el, attribute, null); // el.removeAttribute doesn't always work solely by itself (even in chrome!)
        return el.removeAttribute(attribute);
      }
    },
    
    hasAttribute : function(el, attribute) {
      el = Util.toRawElement(el);
      
      if (Mojo.Util.isFunction(el.hasAttribute)) {
        return el.hasAttribute(attribute);
      }
      else { // IE7
        return !Mojo.Util.isUndefined(el[attribute]);
      }
    },
    
    addClassName : function(el, c)
    {
      if (!this.hasClassName(el, c)) {
        var curClass = this.getAttribute(el, "class");
        
        if (curClass == null) {
          return this.setAttribute(el, "class", c);
        }
        else {
          return this.setAttribute(el, "class", curClass + " " + c);
        }
      }
    },
    
    getClassName : function(el)
    {
      return this.getAttribute(el, "class");
    },
    
    addClassNames : function(el, obj)
    {
      for (var k in obj)
      {
        this.addClassName(el, obj[k]);  
      }
    },
    
    getChildren : function(el) {
      el = Util.toRawElement(el);
      
      return Array.prototype.slice.call(el.children);
    },
    
    getParent : function(el) {
      var rawParent = Util.toRawElement(el).parentNode;
      
      if (rawParent === document) {
        return this.getDocument();
      }
      else if (rawParent != null) {
        return Manager.getFactory().newElement(rawParent);
      }
      else {
        return null;
      }
    },
    
    hasClassName : function(el, c)
    {
      var reg = new RegExp( "(^|\\s+)" + c + "(\\s+|$)" );
      return reg.test( this.getClassName(el) );
    },
    
    removeClassName : function(el, c)
    {
      el = Util.toRawElement(el);
      c = Mojo.Util.trim(c);
      
      if (this.hasClassName(el, c))
      {
          var reg = new RegExp( "(^|\\s+)" + c + "(\\s+|$)" );
          el.className = el.className.replace(reg,'');
      }
    },
    
    setStyle : function(el, key, value)
    {
      el = Util.toRawElement(el);
      return el.style[key] = value;
    },
    
    setStyles : function(el, obj)
    {
      if (obj == null) { return; }
      
      return new com.runwaysdk.Exception("Not implemented");
    },
    
    getStyle : function(el, key)
    {
      el = Util.toRawElement(el);
      return el.style[key];
    },
    
    setInnerHTML : function(el, html) {
      el = Util.toRawElement(el);
      el.innerHTML = html;
    },
    
    appendInnerHTML : function(el, html) {
      el = Util.toRawElement(el);
      el.innerHTML += html;
    },
    
    getInnerHTML : function(el) {
      el = Util.toRawElement(el);
      return el.innerHTML;
    },
    
    getElementById : function(id) {
      // FIXME : is this cross-browser?
      return document.getElementById(id);
    },
    
    getText : function(el)
    {
      el = Util.toRawElement(el);
      return el.textContent != null ? el.textContent : el.innerText;
    },
    
    getBody : function() {
      return Manager.getFactory().newElement(DOMFacade.getRawBody());
    },
    
    getRawBody : function()
    {
      return document.body; // FIXME make cross-browser and iframe compatible
    },
    
    setText : function(el, text)
    {
      el = Util.toRawElement(el);
      if(el.textContent != null)
      {
        el.textContent = text;
      }
      else
      {
        el.innerText = text;
      }
    },
    
    /**
     * Sets the position (top and left styles) of the provided element.
     * 
     * @param Element e An element to set the position of.
     * @param String x The x coordinate (left), with trailing units (px, for example).
     * @param String y The y coordinate (top), with trailing units (px, for example).
     */
    setPos : function(e, x, y) {
      e = Util.toRawElement(e);
      
      this.setStyle(e, "left", x);
      this.setStyle(e, "top", y);
    },
    
    /**
     * This code adapted from jquery, it will find the absolute position of even nested nodes.
     */
    getPos : function(e)
    {
      e = Util.toRawElement(e);
      
      var left = 0;
      var top = 0;
      var l;
      var box;
      
      try {
          box = e.getBoundingClientRect();
      } catch(e) {}
      if (!box) {
        return null;
      }
  
      // Make sure we're not dealing with a disconnected DOM node
      var doc = e.ownerDocument;
      if (doc == null) {
        return null;
      }
      var docElem = doc.documentElement;
      
      var body = doc.body,
          win = window,
          clientTop = docElem.clientTop || body.clientTop || 0,
          clientLeft = docElem.clientLeft || body.clientLeft || 0,
          scrollTop = win.pageYOffset || jQuery.support.boxModel && docElem.scrollTop || body.scrollTop,
          scrollLeft = win.pageXOffset || jQuery.support.boxModel && docElem.scrollLeft || body.scrollLeft,
          top = box.top + scrollTop - clientTop,
          left = box.left + scrollLeft - clientLeft;
  
      return { x:left, y:top };
    },
    
    getSize : function(e)
    {
      e = Util.toRawElement(e);
      
      var w;
      var h;
      
      if (Mojo.Util.isFunction(e.offsetWidth) && (w = e.offsetWidth()) != 0 && w != null)
      {
        h = e.offsetHeight();
      }
      else if (Mojo.Util.isNumber(e.offsetWidth) && (w = e.offsetWidth) != 0 && w != null)
      {
        h = e.offsetHeight;
      }
      else
      {
        w = parseInt(DOMFacade.getStyle(e,"width"));
        h = parseInt(DOMFacade.getStyle(e,"height"));
      }
      
      return { x:w, y:h };
    },
    
    getHeight : function(e)
    {
      e = Util.toRawElement(e);
      
      var h;
      
      if (Mojo.Util.isFunction(e.offsetHeight) && (h = e.offsetHeight()) != 0 && h != null)
      {
      }
      else if (Mojo.Util.isNumber(e.offsetHeight) && (h = e.offsetHeight) != 0 && h != null)
      {
      }
      else
      {
        h = parseInt(DOMFacade.getStyle(e,"height"));
      }
      
      return h;
    },
    
    getWidth : function(e)
    {
      e = Util.toRawElement(e);
      
      var w;
      
      if (Mojo.Util.isFunction(e.offsetWidth) && (w = e.offsetWidth()) != 0 && w != null)
      {
      }
      else if (Mojo.Util.isNumber(e.offsetWidth) && (w = e.offsetWidth) != 0 && w != null)
      {
      }
      else
      {
        w = parseInt(DOMFacade.getStyle(e,"width"));
      }
      
      return w;
    },
    
    setSize : function(e,w,h)
    {
      DOMFacade.setWidth(e,w);
      DOMFacade.setHeight(e,h);
    },
    
    setHeight : function(e,h)
    {
      if (Mojo.Util.isNumber(h))
        h = h + "px";
        
      DOMFacade.setStyle(e, "height", h);
    },
    
    setWidth : function(e,w)
    {
      if (Mojo.Util.isNumber(w))
        w = w + "px";
        
      DOMFacade.setStyle(e, "width", w);
    },
    
    getDocument : function() {
      return Document.getInstance();
    }
  
  }
});

var Document = Mojo.Meta.newClass(Mojo.UI_PACKAGE+"Document", {
  
  IsSingleton : true,
  
  Instance : {
    
    addEventListener : function(type, listener, obj, context, capture) {
      com.runwaysdk.event.Registry.getInstance().addEventListener(document, type, listener, obj, context, capture);
    },
    
    removeEventListener : function(type, listener, obj, context, capture) {
      com.runwaysdk.event.Registry.getInstance().removeEventListener(document, type, listener, obj, context, capture);
    },
    
    dispatchEvent : function(evt)
    {
      if(evt.getEventPhase() === EVENT.EventIF.AT_TARGET)
      {
        evt._setTarget(this);
      }
      
      evt._setCurrentTarget(this);
      
      // dispatch the event for all listeners of this object (the current target)
      Mojo.$.com.runwaysdk.event.Registry.getInstance().dispatchEvent(evt);
      
      if(!evt.getPreventDefault())
      {
        evt.defaultAction();
      }
    },
    
    getParent : function() {
      return null;
    }
    
  }
});

var Util = Mojo.Meta.newClass(Mojo.UI_PACKAGE+"Util", {
  
  IsAbstract : true,
  
  Static : {
    
    isElement : function(o) {
      return ElementIF.getMetaClass().isInstance(o);
    },
    
    isRawElement : function(o) {
      return o instanceof Mojo.GLOBAL.Element || o instanceof Mojo.GLOBAL.DocumentFragment;
    },
    
    isRawAttr : function(o) {
      return o instanceof Mojo.GLOBAL.Attr;
    },
    
    isAttr : function(o)
    {
      return AttrIF.getMetaClass().isInstance(o);
    },
    
    isComponentIF : function(o) {
      return ComponentIF.getMetaClass().isInstance(o);
    },
    
    toElement : function(el, dontWrap) { // a passed in string must be an element node name (not an id!)
      if (!Mojo.Util.isValid(el))
        return el;
      else if ( Mojo.Util.isString(el) || this.isRawElement(el) ) {
        if (dontWrap) {
          throw new com.runwaysdk.Exception("A raw DOM element was potentially passed to a method on a wrapped element. If your intention is to wrap a raw DOM element or to create a new wrapped element you should use the factory's createElement method.");
        }
        else {
          return Manager.getFactory().newElement(el);
        }
      }
      else if (this.isElement(el)) 
        return el;
      else if (ElementProviderIF.getMetaClass().isInstance(el)) 
        return el.getEl();
      else 
        throw new com.runwaysdk.Exception("Unable to convert object " + el + " to an element. (Did you implement the ElementProviderIF on your widget?)");
    },
    
    toRawElement : function(el) // a passed in string must be an element node name (not an id!)
    {
      if (!Mojo.Util.isValid(el))
      return el;
      else if (Mojo.Util.isString(el))
        return DOMFacade.createElement(el);
      else if (this.isRawElement(el))
        return el;
      else if (this.isElement(el))
        return el.getRawEl();
      else if (ElementProviderIF.getMetaClass().isInstance(el))
        return el.getEl().getRawEl();
      else
        throw new com.runwaysdk.Exception("Unable to convert object " + el + " to a raw element. (Did you implement the ElementProviderIF on your widget?)");
    },
    
    stringToRawElement : function(str) {
      if (Mojo.Util.isString(str)) {
        // Does the string begin with a # sign? If so, fetch the element from ID.
        if (str.charAt(0) == "#") {
          return DOMFacade.getElementById( str.slice(1) );
        }
        else {
          // Else treat it as a type string for a new element
          return DOMFacade.createElement(str);
        }
      }
      else {
        return str;
      }
    },
  
    getMousePos : function(ev)
    {
      if (ev.pageX || ev.pageY)
      {
        return { x: ev.pageX, y: ev.pageY };
      }
      
      return {
               x: ev.clientX + document.body.scrollLeft - document.body.clientLeft,
               y: ev.clientY + document.body.scrollTop - document.body.clientTop
      };
    },
    
    isWithinBounds : function( pos, x, y, w, h ) // calcs if pos{x,y} is within the rectangle defined by x,y,w,h
    {
      if (pos.x > x && pos.y > y && pos.x < x + w && pos.y < y + h)
        return true;
      return false;
    },
    
    isInsideElement : function( pos, el ) // calcs if pos is inside the element
    {
      var ePos = DOMFacade.getPos(el);
      var size = DOMFacade.getSize(el);
      return this.isWithinBounds( pos, ePos.x, ePos.y, size.x, size.y );
    },
    
    getScreenSize : function()
    {
      var h = 512;
      var w = 512;
      
      if (self.innerHeight)
      {
        w = self.innerWidth;
        h = self.innerHeight;
      }
      else if (document.documentElement && document.documentElement.clientHeight)
      {
        w = document.documentElement.clientWidth;
        h = document.documentElement.clientHeight;
      }
      else if (document.body)
      {
        w = document.body.clientWidth;
        h = document.body.clientHeight;
      }
      
      return { x:w, y:h };
    },
    
    getScrH : function()
    {
      return Util.getScreenSize().y;
    },
    
    getScrW : function()
    {
      return Util.getScreenSize().x;
    },
    
    // Prevents users from highlighting (selecting) the element
    disableSelection : function(el)
    {
      // this function is currently needed for (runway) drag drop
      el = this.toRawElement(el);
      
      var event = new com.runwaysdk.event.Event("selectstart");
      event.preventDefault();
      
      var listener = new com.runwaysdk.event.EventListener({
        handleEvent : function(e)
        {
          return false;
        }
      });
      
      el.addEventListener(event, listener);

      el.unselectable = "on";
      el.style.MozUserSelect = "none";
    },
    
    max : function(a, b)
    {
      if (a > b)
        return a;
      
      return b;
    }
  }

});

var DataTableIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+"DataTableIF", {
  Instance : {
    getColumnSet : function(){}, // ColumnSetIF
    getRecordSet : function(){}, // RecordSetIF
  }
});

var ColumnSetIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+"ColumnSetIF", {
  Instance : {
    /**
     * @param columnIF a ColumnIF or ColumnIF[] object.
     */
    addColumn : function(columnIF){},
    
    /**
     * @param columnIF a ColumnIF or ColumnIF[] object.
     */    
    removeColumn : function(columnIF){}
  }
});

var RecordSetIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+"RecordSetIF", {
  Instance : {
  
    /**
     * @param recordIF a RecordIF or RecordIF[] object.
     */
    addRecord : function(recordIF){},
    
    /**
     * @param RecordIF a ColumnIF or RecordIF[] object.
     */    
    removeRecord : function(recordIF){}
  }
});

var ColumnIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+"ColumnIF", {
  Instance : {
    getKey : function(){},
    getLabel : function(){}
  }
});

var RecordIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+"RecordIF", {
  Instance : {
    getValue : function(key){} // String
  }
});

var Pagination = Mojo.Meta.newClass(Mojo.UI_PACKAGE+'Pagination', {
  Constants : {
    DEFAULT_MAX_DISPLAY_PAGES : 10
  },
  Instance : {
    initialize : function(pageNumber, pageSize, count){
      this._pageNumber = pageNumber;
      this._pageSize = pageSize;
      this._count = count;
      this._pages = [];
      this._displayPages = this.constructor.DEFAULT_MAX_DISPLAY_PAGES;
      this.calculate();
    },
    calculate : function()
    {
      // can't paginate an empty result set
      if (this._count === 0)
      {
        return;
      }
  
      // Calculate the number of links to display
      if(this._pageSize == 0 || this._pageNumber == 0)
      {
        this._pageSize = this._count;
        this._pageNumber = 1;
      }
  
      var totalPages = parseInt(Math.ceil(this._count / this._pageSize ));
  
      var l = Math.max(this._pageNumber - 4, 1);
      var u = Math.min(this._pageNumber + 4, totalPages);
      var lowerBound = Math.max(1, Math.min(this._pageNumber-4, u-this._displayPages));
      var upperBound = Math.min(Math.max(this._pageNumber+4, l+this._displayPages), totalPages);
  
      if (lowerBound != 1)
      {
        // Generate the first page
        this._pages.push(new Page(false, 1));
  
        // Generate the marker page
        if (lowerBound != 2)
        {
          var page = new Page();
          page.markLeft();
          this._pages.push(page);
        }
      }
  
      for (var i = lowerBound; i <= upperBound; i++)
      {
        this._pages.push(new Page( ( this._pageNumber == i ), i));
      }
  
      if (upperBound != totalPages)
      {
        // Generate marker page
        if (upperBound != totalPages - 1)
        {
          var page = new Page();
          page.markRight();
          this._pages.push(page);
        }
  
        // Generate last page
        this._pages.push(new Page(false, totalPages));
      }
    },
    getPages : function()
    {
      return this._pages;
    }
  }
});

var Page = Mojo.Meta.newClass(Mojo.UI_PACKAGE+'Page', {
  Instance : {
    initialize : function(isCurrent, pageNumber){
      this._isCurrent = Mojo.Util.isBoolean(isCurrent) ? isCurrent : false;
      this._pageNumber = Mojo.Util.isNumber(pageNumber) ? pageNumber : 0;
      this._isLeft = false;
      this._isRight = false;
    },
    markLeft : function() { this._isLeft = true; },
  
    markRight : function() { this._isRight = true; },
  
    isLeft : function() { return this._isLeft; },
  
    isRight : function() { return this._isRight; },
  
    isCurrentPage : function() { return this._isCurrent; },
  
    getPageNumber : function() { return this._pageNumber; },

  }
});

var LayoutIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+'LayoutIF', {
  Extends : ElementProviderIF,
  Instance : {
    render : function(){}
  }
});

var DragIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+'DragIF', {
  Instance: {
    getDragDelegate : function(){},
    getDragConfig : function(){}, // a selector is required to be specified here
    
    // The following methods are optional if extending from DragBase:
    getDragData : function(){},
    getDragTarget : function(HTMLElementIF){},
    onDragStart : function(dragStartEvent){},
    onDrag : function(dragEvent){},
    onDragEnd : function(dragEndEvent){}
  }
});
var DropIF = Mojo.Meta.newInterface(Mojo.UI_PACKAGE+'DropIF', {
  Instance: {
    getDropDelegate : function(){},
    getDropConfig : function(){}, // a selector is required to be specified here
    
    // The following methods are optional if extending from DropBase:
    dispatchDropHitEvent : function(){},
    getDropTarget : function(HTMLElementIF){},
    onCanDrop : function(){},
    onDropEnter : function(){},
    onDropExit : function(){},
    onDropHit : function(){},
    onDropOver : function(){}
  }
});

var AddItemEvent = Mojo.Meta.newClass(Mojo.UI_PACKAGE+'AddItemEvent', {
  Extends : EVENT.CustomEvent,
  Instance : {
    initialize : function(listItem)
    {
      this.$initialize();
      this._listItem = listItem;
    },
    getListItem : function() { return this._listItem; },
  }
});
var RemoveItemEvent = Mojo.Meta.newClass(Mojo.UI_PACKAGE+'RemoveItemEvent', {
  Extends : EVENT.CustomEvent,
  Instance : {
    initialize : function(listItem)
    {
      this.$initialize();
      this._listItem = listItem;
    },
    getListItem : function() { return this._listItem; },
  }
});

/**
 * Visitor to traverse the field structures of a FormObject.
 */
var FormObjectVisitorIF = Mojo.Meta.newInterface(Mojo.FORM_PACKAGE.FORM+'FormObjectVisitorIF', {
  Instance : {
    visitFormObject : function(formObject){},
    visitCharacter : function(webCharacter){},
    visitText : function(webText){},
    visitDate : function(webDate){},
    visitInteger : function(webInteger){},
    visitLong : function(webLong){},
    visitDouble : function(webDouble){},
    visitDecimal : function(webDecimal){},
    visitFloat : function(webFloat){},
    visitBreak : function(webBreak){},
    visitHeader : function(webHeader){},
    visitComment : function(webComment){},
    visitReference : function(webReference){},
    visitGeo : function(webGeo){},
    visitSingleTerm : function(webSingleTerm){},
    visitMultipleTerm : function(webMultipleTerm){},
    visitGroup : function(webGroup){},
    visitSingleTermGrid : function(webSingleTerm){},
    visitCharacterCondition : function(characterCondition){},
    visitLongCondition : function(longCondition){},
    visitDoubleCondition : function(doubleCondition){},
    visitDateCondition : function(dateCondition){},
    visitAndFieldCondition : function(andCondition){}
  }
});

var VisitableIF = Mojo.Meta.newInterface(Mojo.FORM_PACKAGE.FORM+'VisitableIF', {
  Instance : {
    /**
     * @param visitor FormObjectVisitorIF
     */
    accept : function(visitor){}
  }
});

var FormMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'FormMd', {
  IsAbstract : true,
  Instance : {
    initialize : function(obj){
      this.$initialize();
      this._formName = obj.formName;
      this._formMdClass = obj.formMdClass;
      this._description = obj.description;
      this._displayLabel = obj.displayLabel;
      this._id = obj.id;
    },
    getId : function(){ return this._id; },
    getDisplayLabel : function() { return this._displayLabel; },
    getDescription : function() { return this._description; },
    getFormName : function(){ return this._formName; },
    getFormMdClass : function(){ return this._formMdClass; },
    toJSON : function(objKey)
    {
      var map = {};
      var keys = Mojo.Util.getKeys(this);
      for(var i=0, len=keys.length; i<len; i++)
      {
        var key = keys[i];
        if(key.indexOf('_') === 0 && key.indexOf('__') !== 0)
        {
          var newKey = key.substr(1, key.length);
          map[newKey] = this[key];
        }
        else
        {
          map[key] = this[key];
        }      
      }
    
      return new com.runwaysdk.StandardSerializer(map).toJSON(key);
    }
  }
});

var WebFormMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebFormMd', {
  Extends: FormMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var FormObject = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FORM+'FormObject', {
  IsAbstract: true,
  Implements : VisitableIF,
  Instance: {
    initialize : function(obj) {

      // default values
      this._formMd = new WebFormMd(obj.formMd);
      this._fields = new com.runwaysdk.structure.LinkedHashMap();
      this._id = obj.id;
      this._dataId = obj.dataId;
      this._newInstance = obj.newInstance;
      this._readable = obj.readable;
      this._writable = obj.writable;
      this._type = obj.type;
      
      var fields = obj.fields;
      for(var i=0, len=fields.length; i<len; i++)
      {
        var fieldObj = fields[i];
        var field = Mojo.Meta.newInstance(fieldObj.js_class, fieldObj);
        this._fields.put(field.getFieldName(), field);
      }
    },
    getFormName : function() { return this.getMd().getFormName(); },
    getHashCode : function(){ return this._id; },
    getId: function(){ return this._id; },
    getDataId : function(){ return this._dataId; },
    isNewInstance: function(){ return this._newInstance; },
    getMd: function(){ return this._formMd; },
    getType : function(){ return this._type; },
    isReadable : function(){ return this._readable; },
    isWritable : function(){ return this._writable; },
    getValue : function(field){ return this._fields[field].getValue(); },
    getFieldMap : function(){ return this._fields; },
    getFields : function(){ return this._fields.values(); },
    getFieldNames : function(){ return this._fields.keySet(); },
    toJSON : function(objKey)
    {
      var map = {};
      var keys = Mojo.Util.getKeys(this);
      for(var i=0, len=keys.length; i<len; i++)
      {
        var key = keys[i];
        if(key.indexOf('_') === 0 && key.indexOf('__') !== 0)
        {
          var newKey = key.substr(1, key.length);
          if(key === '_fields')
          {
            map[newKey] = this._fields.values();
          }
          else
          {
            map[newKey] = this[key];
          }
        }
        else
        {
          map[key] = this[key];
        }
      }
    
      return new com.runwaysdk.StandardSerializer(map).toJSON(key);
    }
  }
});

var WebFormObject = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.WEB+'WebFormObject', {
  Extends: FormObject,
  Instance: {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){

      var fields = this.getFields();
      for(var i=0; i<fields.length; i++)
      {
        fields[i].accept(visitor);
      }
      
      visitor.visitFormObject(this);
    }
  },
  Static : {
    parseFromJSON : function(json){
      var obj = Mojo.Util.toObject(json);
      return new WebFormObject(obj);
    }
  }
});

var FieldIF = Mojo.Meta.newInterface(Mojo.FORM_PACKAGE.FIELD+'FieldIF', {
  Instance : {
    getType : function(){},
    getFieldMd : function(){},
    getValue : function(){},
    getFieldName : function(){},
    isWritable : function(){},
    isReadable : function(){},
    isModified : function(){}
  }
});

var WebField = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebField', {
  Implements: [FieldIF, VisitableIF],
  IsAbstract : true,
  Instance : {
    initialize : function(obj){
      this.$initialize();
      
      var fieldMd = obj.fieldMd;
      this._fieldMd = Mojo.Meta.newInstance(fieldMd.js_class, fieldMd);
      var conditionObj = obj.condition;
      
      if (conditionObj) {
        this._condition = Mojo.Meta.newInstance(conditionObj.js_class, conditionObj);
      }
      else {
        this._condition = null;
      }
      
      this._writable = obj.writable;
      this._readable = obj.readable;
      this._value = obj.value;
      this._modified = obj.modified;
      this._type = obj.type;
    },
    getType : function(){ return this._type; },
    getFieldMd : function(){ return this._fieldMd; },
    getValue : function(){ return this._value; },
    setValue : function(value){ this._value = value; },
    getFieldName : function(){ return this.getFieldMd().getFieldName(); },
    isWritable : function(){ return this._writable; },
    isReadable : function(){ return this._readable; },
    isModified : function(){ return this._modified; },
    getCondition : function(){ return this._condition; },
    toJSON : function(objKey)
    {
      var map = {};
      var keys = Mojo.Util.getKeys(this);
      for(var i=0, len=keys.length; i<len; i++)
      {
        var key = keys[i];
        if(key.indexOf('_') === 0 && key.indexOf('__') !== 0)
        {
          var newKey = key.substr(1, key.length);
          map[newKey] = this[key];
        }
        else
        {
          map[key] = this[key];
        }      
      }
    
      return new com.runwaysdk.StandardSerializer(map).toJSON(key);
    }
  }
});

var WebAttribute = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebAttribute', {
  Extends : WebField,
  IsAbstract : true,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var WebPrimitive = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebPrimitive', {
  Extends : WebAttribute,
  IsAbstract : true,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var WebBoolean = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebBoolean', {
  Extends : WebPrimitive,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){
      visitor.visitBoolean(this);
    }
  }
});
var WebReference = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebReference', {
  Extends : WebAttribute,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
      this._referenceDisplay = obj.referenceDisplay || '';
    },
    getReferenceDisplay : function() { return this._referenceDisplay; },
    accept : function(visitor){
      visitor.visitReference(this);
    }
  }
});

var WebGeo = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebGeo', {
  Extends : WebAttribute,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
      this._geoField = Mojo.Util.toObject(obj.geoField);
    },
    getGeoField : function(){ return this._geoField; },
    accept : function(visitor){
      visitor.visitGeo(this);
    }
  }
});

var WebSingleTerm = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebSingleTerm', {
  Extends : WebAttribute,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){
      visitor.visitSingleTerm(this);
    }
  }
});

var WebMultipleTerm = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebMultipleTerm', {
  Extends : WebAttribute,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
      var termsArr = obj.terms;
      this._termIds = new com.runwaysdk.structure.HashMap(obj.terms);
    },
    accept : function(visitor){
      visitor.visitMultipleTerm(this);
    },
    addTerm : function(termId, display){
      this._termIds.put(termId, display || null);
    },
    getTerm : function(termId){
      return this._termIds.get(termId);
    },
    removeTerm : function(termId){
      this._termIds.remove(termId);
    },
    clearTerms : function(){
      this._termIds.clear();
    },
    getTermIds : function(){
      return this._termIds.keySet();
    }
  }
});

var WebSingleTermGrid = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebSingleTermGrid', {
  Extends : WebAttribute,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
      this._gridExec = obj.grid;
      this._grid = null;
    },
    getGridExecutable : function() { return this._gridExec; },
    setGrid : function(grid){ this._grid = grid; },
    getGrid : function(){ return this._grid; },
    accept : function(visitor){
      visitor.visitSingleTermGrid(this);
    },
    toJSON : function(key)
    {
      var map = {};
      var keys = Mojo.Util.getKeys(this);
      for(var i=0, len=keys.length; i<len; i++)
      {
        var key = keys[i];
        if(key === '_gridExec' || key === '_grid')
        {
          continue;
        }
        else if(key.indexOf('_') === 0 && key.indexOf('__') !== 0)
        {
          var newKey = key.substr(1, key.length);
          map[newKey] = this[key];
        }
        else
        {
          map[key] = this[key];
        }      
      }
    
      return new com.runwaysdk.StandardSerializer(map).toJSON(key);
    }
  }
});

var WebCharacter = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebCharacter', {
  Extends : WebPrimitive,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){
      visitor.visitCharacter(this);
    }
  }
});

var WebText = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebText', {
  Extends : WebPrimitive,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){
      visitor.visitText(this);
    }
  }
});

var WebNumber = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebNumber', {
  IsAbstract : true,
  Extends : WebPrimitive,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var WebInteger = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebInteger', {
  Extends : WebNumber,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){
      visitor.visitInteger(this);
    }
  }
});

var WebLong = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebLong', {
  Extends : WebNumber,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){
      visitor.visitLong(this);
    }
  }
});

var WebDec = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebDec', {
  IsAbstract : true,
  Extends : WebNumber,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var WebDecimal = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebDecimal', {
  Extends : WebDec,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){
      visitor.visitDecimal(this);
    }
  }
});

var WebDouble = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebDouble', {
  Extends : WebDec,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){
      visitor.visitDouble(this);
    }
  }
});

var WebFloat = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebFloat', {
  Extends : WebDec,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){
      visitor.visitFloat(this);
    }
  }
});

var WebMoment = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebMoment', {
  IsAbstract : true,
  Extends : WebPrimitive,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var WebDate = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebDate', {
  Extends : WebMoment,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){
      visitor.visitDate(this);
    }
  }
});

var WebDateTime = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebDateTime', {
  Extends : WebMoment,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){
      visitor.visitDateTime(this);
    }
  }
});

var WebTime = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebTime', {
  Extends : WebMoment,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){
      visitor.visitTime(this);
    }
  }
});

var WebHeader = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebHeader', {
  Extends : WebField,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){
      visitor.visitHeader(this);
    }
  }
});

var WebBreak = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebBreak', {
  Extends : WebField,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){
      visitor.visitBreak(this);
    }
  }
});

var WebComment = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.FIELD+'WebComment', {
  Extends : WebField,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){
      visitor.visitComment(this);
    }
  }
});

var FieldMdIF = Mojo.Meta.newInterface(Mojo.FORM_PACKAGE.FIELD+'FieldMdIF', {
  Instance : {
    getDefiningMdForm : function(){},
    getFieldName : function(){},
    getFieldOrder : function(){},
    getDisplayLabel : function(){},
    getDescription : function(){},
    getId : function(){},
    isRequired : function(){}
  }
});

var WebFieldMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebFieldMd', {
  Implements : FieldMdIF,
  IsAbstract : true,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
      this._definingMdForm = obj.definingMdForm;
      this._displayLabel = obj.displayLabel;
      this._description = obj.description;
      this._fieldName = obj.fieldName;
      this._fieldOrder = obj.fieldOrder;
      this._id = obj.id;
      this._required = obj.required;
    },
    getDefiningMdForm : function(){ return this._definingMdForm; },
    getFieldName : function(){ return this._fieldName; },
    getFieldOrder : function(){ return this._fieldOrder; },
    getDisplayLabel : function(){ return this._displayLabel; },
    getDescription : function(){ return this._description; },
    getId : function(){ return this._id; },
    isRequired : function(){ return this._required; },
    toJSON : function(objKey)
    {
      var map = {};
      var keys = Mojo.Util.getKeys(this);
      for(var i=0, len=keys.length; i<len; i++)
      {
        var key = keys[i];
        if(key.indexOf('_') === 0 && key.indexOf('__') !== 0)
        {
          var newKey = key.substr(1, key.length);
          map[newKey] = this[key];
        }
        else
        {
          map[key] = this[key];
        }      
      }
    
      return new com.runwaysdk.StandardSerializer(map).toJSON(key);
    }
  }
});

var WebAttributeMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebAttributeMd', {
  Extends : WebFieldMd,
  IsAbstract : true,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
      this._definingMdAttribute = obj.definingMdAttribute;
      this._definingAttribute = obj.definingAttribute;
      this._definingClass = obj.definingClass;
    },
    getDefiningMdAttribute : function(){ return this._definingMdAttribute; },
    getDefiningAttribute : function(){ return this._definingAttribute; },
    getDefiningClass : function(){ return this._definingClass; }
  }
});

var WebPrimitiveMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebPrimitiveMd', {
  Extends : WebAttributeMd,
  IsAbstract : true,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var WebBooleanMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebBooleanMd', {
  Extends : WebPrimitiveMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
      this._positiveDisplayLabel = obj.positiveDisplayLabel;
      this._negativeDisplayLabel = obj.negativeDisplayLabel;
    },
    getPositiveDisplayLabel : function(){ return this._positiveDisplayLabel; },
    getNegativeDisplayLabel : function(){ return this._negativeDisplayLabel; }
  }
});

var WebReferenceMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebReferenceMd', {
  Extends : WebAttributeMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
      this._referencedMdBusiness = obj.referencedMdBusiness;
      this._referencedDisplayLabel = obj.referencedDisplayLabel;
    },
    getReferencedMdBusiness : function(){ return this._referencedMdBusiness; },
    getReferencedDisplayLabel : function(){ return this._referencedDisplayLabel; }
  }
});

var WebGeoMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebGeoMd', {
  Extends : WebAttributeMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
      this._geoDisplayLabel = obj.geoDisplayLabel;
    },
    getGeoDisplayLabel : function(){ return this._geoDisplayLabel; }
  }
});

var WebSingleTermMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebSingleTermMd', {
  Extends : WebAttributeMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
      this._termDisplayLabel = obj.termDisplayLabel;
    },
    getTermDisplayLabel : function(){ return this._termDisplayLabel; }
  }
});

var WebMultipleTermMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebMultipleTermMd', {
  Extends : WebAttributeMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var WebSingleTermGridMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebSingleTermGridMd', {
  Extends : WebAttributeMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});


var WebCharacterMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebCharacterMd', {
  Extends : WebPrimitiveMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
      this._maxLength = obj.maxLength;
      this._displayLength = obj.displayLength;
    },
    getDisplayLength : function(){ return this._displayLength; },
    getMaxLength : function(){ return this._maxLength; }
  }
});

var WebTextMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebTextMd', {
  Extends : WebPrimitiveMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
      this._width = obj.width;
      this._height = obj.height;
    },
    getWidth : function(){ return this._width; },
    getHeight : function(){ return this._height; }
  }
});

var WebNumberMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebNumberMd', {
  IsAbstract : true,
  Extends : WebPrimitiveMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
      this._startRange = obj.startRange;
      this._endRange = obj.endRange;
    },
    getStartRange : function() { return this._startRange; },
    getEndRange : function() { return this._endRange; }
  }
});

var WebIntegerMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebIntegerMd', {
  Extends : WebNumberMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var WebLongMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebLongMd', {
  Extends : WebNumberMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var WebDecMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebDecMd', {
  Extends : WebNumberMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
      this._decPrecision = obj.decPrecision;
      this._decScale = obj.decScale;
    },
    getDecPrecision : function() { return this._decPrecision; },
    getDecScale : function() { return this._decScale; }
  }
});

var WebDecimalMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebDecimalMd', {
  Extends : WebDecMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var WebDoubleMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebDoubleMd', {
  Extends : WebDecMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var WebFloatMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebFloatMd', {
  Extends : WebDecMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var WebMomentMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebMomentMd', {
  IsAbstract : true,
  Extends : WebPrimitiveMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var WebDateMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebDateMd', {
  Extends : WebMomentMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
      this._beforeTodayExclusive = obj.beforeTodayExclusive;
      this._beforeTodayInclusive = obj.beforeTodayInclusive;
      this._afterTodayExclusive = obj.afterTodayExclusive;
      this._afterTodayInclusive = obj.afterTodayInclusive;
      this._startDate = obj.stardddtDate;
      this._endDate = obj.endDate;
    },
    getBeforeTodayExclusive : function() { return this._beforeTodayExclusive; },
    getBeforeTodayInclusive : function() { return this._beforeTodayInclusive; },
    getAfterTodayExclusive : function() { return this._afterTodayExclusive; },
    getAfterTodayInclusive : function() { return this._afterTodayInclusive; },
    getStartDate : function() { return this._startDate; },
    getEndDate : function() { return this._endDate; }
  }
});

var WebDateTimeMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebDateTimeMd', {
  Extends : WebMomentMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var WebTimeMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebTimeMd', {
  Extends : WebMomentMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var WebHeaderMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebHeaderMd', {
  Extends : WebFieldMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var WebBreakMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebBreakMd', {
  Extends : WebFieldMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var WebCommentMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'WebCommentMd', {
  Extends : WebFieldMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var ConditionMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'ConditionMd', {
  IsAbstract : true,
  Instance : {
    initialize : function(obj){
      this.$initialize();
      this._id = obj.id;
      this._referencingMdField = obj.referencingMdField;
      this._referencingMdForm = obj.referencingMdForm;
    },
    getId : function() { return this._id; },
    getReferencingMdField : function() { return this._referencingMdField; },
    getReferencingMdForm : function() { return this._referencingMdForm; },
    toJSON : function(objKey)
    {
      var map = {};
      var keys = Mojo.Util.getKeys(this);
      for(var i=0, len=keys.length; i<len; i++)
      {
        var key = keys[i];
        if(key.indexOf('_') === 0 && key.indexOf('__') !== 0)
        {
          var newKey = key.substr(1, key.length);
          map[newKey] = this[key];
        }
        else
        {
          map[key] = this[key];
        }      
      }
    
      return new com.runwaysdk.StandardSerializer(map).toJSON(key);
    }
  }
});

var BasicConditionMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'BasicConditionMd', {
  Extends : ConditionMd,
  IsAbstract : true,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var CharacterConditionMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'CharacterConditionMd', {
  Extends : BasicConditionMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var DateConditionMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'DateConditionMd', {
  Extends : BasicConditionMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var DoubleConditionMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'DoubleConditionMd', {
  Extends : BasicConditionMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var LongConditionMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'LongConditionMd', {
  Extends : BasicConditionMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var CompositeFieldConditionMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'CompositeFieldConditionMd', {
  Extends : ConditionMd,
  IsAbstract : true,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var AndFieldConditionMd = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.METADATA+'AndFieldConditionMd', {
  Extends : CompositeFieldConditionMd,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

var Condition = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.CONDITION+'Condition', {
  Implements : VisitableIF,
  IsAbstract : true,
  Instance : {
    initialize : function(obj){
      this.$initialize();
      
      var conditionMd = obj.conditionMd;
      if(obj.conditionMd){
        this._conditionMd = Mojo.Meta.newInstance(conditionMd.js_class, conditionMd);
      }
      
			this._operation = obj.operation;
			this._definingMdField = obj.definingMdField;
			this._value = obj.value;
    },
    getConditionMd : function() { return this._conditionMd; },
    getOperation : function(){ return this._operation; },
    getValue : function(){ return this._value; },
    _setValue : function(value) { this._value = value; },
    getDefiningMdField : function(){ return this._definingMdField; },
    isTrue : { IsAbstract : true },
    toJSON : function(objKey)
    {
      var map = {};
      var keys = Mojo.Util.getKeys(this);
      for(var i=0, len=keys.length; i<len; i++)
      {
        var key = keys[i];
        if(key.indexOf('_') === 0 && key.indexOf('__') !== 0)
        {
          var newKey = key.substr(1, key.length);
          map[newKey] = this[key];
        }
        else
        {
          map[key] = this[key];
        }      
      }
    
      return new com.runwaysdk.StandardSerializer(map).toJSON(key);
    }
  }
});

var CompositeFieldCondition = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.CONDITION+'CompositeFieldCondition',{
	IsAbstract : true,
	Extends : Condition,
	Instance : {
		initialize : function(obj) {
			this.$initialize(obj);
			
			var firstConditionObj = obj.firstCondition;
			var secondConditionObj = obj.secondCondition;
			
      this._firstCondition = Mojo.Meta.newInstance(firstConditionObj.js_class, firstConditionObj);
      this._secondCondition = Mojo.Meta.newInstance(secondConditionObj.js_class, secondConditionObj);
		},
		getFirstCondition : function(){ return this._firstCondition; },
		getSecondCondition : function(){ return this._secondCondition; }
	}
});

var AndFieldCondition = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.CONDITION+'AndFieldCondition',{
	Extends : CompositeFieldCondition,
	Instance : {
		initialize : function(obj) {
			this.$initialize(obj);
		},
		accept : function(visitor){
		  visitor.visitAndFieldCondition(this);
		},
		isTrue : function(){
		  return this.getFirstCondition().isTrue() && this.getSecondCondition().isTrue();
		}
	}
});

var BasicCondition = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.CONDITION+'BasicCondition', {
	IsAbstract : true,
	Extends : Condition,
	Instance : {
		initialize : function(obj) {
			this.$initialize(obj);
			this._value = obj.value;
			this._isTrue = false; // has the condition criterion been met?
		},
    _setTrue : function(isTrue) { this._isTrue = isTrue; },
    isTrue : function() { return this._isTrue; }
	}
});

var CharacterCondition = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.CONDITION+'CharacterCondition', {
  Extends : BasicCondition,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){
      visitor.visitCharacterCondition(this);
    },
		evaluate : function(changedValue){
      var value = this.getValue();
      var op = this.getOperation();
      var isTrue = false;
      switch (op) {
        case 'EQ':
          isTrue = changedValue === value;
          break;
        case 'NEQ':
          isTrue = changedValue !== value;
          break;
        default:
          isTrue = false;
      }
      
      this._setTrue(isTrue);      
    }
  }
});

var DateCondition = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.CONDITION+'DateCondition', {
  Extends : BasicCondition,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){
      visitor.visitDateCondition(this);
    },
    evaluate : function(changedValue){
      var value = MDSS.Calendar.parseDate(this.getValue());
      changedValue = MDSS.Calendar.parseDate(changedValue);
			
      var op = this.getOperation();
      var isTrue = false;
      if (changedValue !== "")
      {
        switch (op) {
          case 'EQ':
            isTrue = changedValue.equals(value);
            break;
          case 'GT':
            isTrue = changedValue.isAfter(value); 
            break;
          case 'GTE':
            isTrue = (changedValue.isAfter(value) || changedValue.equalsIgnoreTime(value)); 
            break;
          case 'LT':
            isTrue = changedValue.isBefore(value); 
            break;
          case 'LTE':
            isTrue = (changedValue.isBefore(value) || changedValue.equalsIgnoreTime(value)); 
            break;
          case 'NEQ':
            isTrue = !changedValue.equalsIgnoreTime(value);
            break;
          default:
            isTrue = false;
        }
      }
      
      this._setTrue(isTrue);      
    }
  }
});

var DoubleCondition = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.CONDITION+'DoubleCondition', {
  Extends : BasicCondition,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){
      visitor.visitDoubleCondition(this);
    },
		evaluate : function(changedValue){
      var value = parseFloat(this.getValue());
			changedValue = parseFloat(changedValue);
      var op = this.getOperation();
      var isTrue = false;
      switch (op) {
        case 'EQ':
          isTrue = changedValue == value;
          break;
        case 'GT':
          isTrue = changedValue > value;
          break;
        case 'GTE':
          isTrue = changedValue >= value;
          break;
        case 'LT':
          isTrue = changedValue < value;
          break;
        case 'LTE':
          isTrue = changedValue <= value;
          break;
        case 'NEQ':
          isTrue = changedValue != value;
          break;
        default:
          isTrue = false;
      }
      
      this._setTrue(isTrue);      
    }
  }
});

var LongCondition = Mojo.Meta.newClass(Mojo.FORM_PACKAGE.CONDITION+'LongCondition', {
  Extends : BasicCondition,
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    },
    accept : function(visitor){
      visitor.visitLongCondition(this);
    },
	  evaluate : function(changedValue){
      var value = parseInt(this.getValue());
			changedValue = parseInt(changedValue);
      var op = this.getOperation();
      var isTrue = false;
      switch (op) {
        case 'EQ':
          isTrue = changedValue == value;
          break;
        case 'GT':
          isTrue = changedValue > value;
          break;
        case 'GTE':
          isTrue = changedValue >= value;
          break;
        case 'LT':
          isTrue = changedValue < value;
          break;
        case 'LTE':
          isTrue = changedValue <= value;
          break;
        case 'NEQ':
          isTrue = changedValue != value;
          break;
        default:
          isTrue = false;
      }
      
      this._setTrue(isTrue);      
    }	
  }
});

})();