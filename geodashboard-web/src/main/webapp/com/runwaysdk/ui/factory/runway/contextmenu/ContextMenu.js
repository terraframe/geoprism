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
//define(["../list/List"], function() {
(function(){

var RW = Mojo.Meta.alias(Mojo.RW_PACKAGE + "*");
var UI = Mojo.Meta.alias(Mojo.UI_PACKAGE + "*");

var ContextMenu = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'ContextMenu', {
  
  Extends : RW.List,
  
//  Implements : [],
  
  Instance : {
    initialize : function(target, pos) {
      this.$initialize();
      
      this.setTarget(target);
      
      if (pos == null) {
        pos = UI.DOMFacade.getMousePos();
      }
      this.setPos(pos.x, pos.y);
      
      this.__bindedOnDocumentClickListener = Mojo.Util.bind(this, this.onDocumentClickListener);
      UI.DOMFacade.getDocument().addEventListener("mousedown", this.__bindedOnDocumentClickListener);
    },
    onDocumentClickListener : function(mouseEvent) {
      if (!this.hasLI(mouseEvent.getTarget())){
        this.close();
      }
    },
    // OVERRIDE from List
    _makeListItem : function(labelOrItem, icon, handler, config) {
      if (labelOrItem instanceof ContextMenuItem) {
        return labelOrItem;
      }
      else if (Mojo.Util.isString(labelOrItem))
      {
        return new ContextMenuItem(this, labelOrItem, icon, handler, config);
      }
      else
      {
        throw new com.runwaysdk.Exception("Invalid parameter. Expected either a string (a label for the new ContextMenuItem), or an existing ContextMenuItem.");
      }
    },
    // OVERRIDE from List
    addItem : function(label, icon, handler, config) {
      item = this._makeListItem(label, icon, handler, config);
      this.appendChild(item);
      this.dispatchEvent(new UI.AddItemEvent(item));
      return item;
    },
    getTarget : function() {
      return this._target;
    },
    setTarget : function(target) {
      this._target = target;
//      this._target = UI.Util.toElement(target);
    },
    show : function()
    {
      this.setStyle("display", "inline");
    },
    hide : function()
    {
      this.setStyle("display", "none");
    },
    close : function() {
      this.destroy();
    },
    destroy : function() {
      this.$destroy();
      
      // The super will not remove this event listener because it is bound to document, not the context menu instance.
      UI.DOMFacade.getDocument().removeEventListener("mousedown", this.__bindedOnDocumentClickListener);
    }
  }
});

var ContextMenuItem = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'ContextMenuItem', {
  
  Extends : RW.ListItem,
  
  Instance : {
    initialize : function(contextMenu, label, icon, handler, config) {
      this.$initialize();
      
      this._handler = handler;
      this._contextMenu = contextMenu;
      
      this.setInnerHTML(label);
      this.addClassName("icon-" + icon);
      
      this.addEventListener("click", Mojo.Util.bind(this, this.onClickListener));
      this.addEventListener("mouseover", Mojo.Util.bind(this, this.onMouseOverListener));
      this.addEventListener("mouseout", Mojo.Util.bind(this, this.onMouseOutListener));
    },
    onMouseOverListener : function(e) {
      if (this.isEnabled()) {
        this.addClassName("hover");
      }
    },
    onMouseOutListener : function(e) {
      if (this.isEnabled()) {
        this.removeClassName("hover");
      }
    },
    onClickListener : function(e)
    {
      if (this.isEnabled()) {
        this._handler(this._contextMenu, this, e);
        this._contextMenu.close();
      }
    },
    setEnabled : function(bool) {
      if (bool) {
        this.removeClassName("disabled");
      }
      else {
        this.addClassName("disabled");
      }
    },
    isEnabled : function() {
      return !(this.hasClassName("disabled"));
    }
    
  }
});

})();