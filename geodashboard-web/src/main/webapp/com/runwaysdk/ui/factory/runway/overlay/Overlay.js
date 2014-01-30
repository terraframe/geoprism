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

//define(["../widget/Widget"], function(){
(function(){

var RW = Mojo.Meta.alias(Mojo.RW_PACKAGE + "*");
var UI = Mojo.Meta.alias(Mojo.UI_PACKAGE + "*");

var Overlay = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'Overlay', {
  
  Extends : RW.Widget,
  
  Instance : {
    
    initialize: function(el, isModal)
    {
      this.$initialize(el);
      
      if (isModal)
      {
        this._Dimmer = this.getFactory().newElement("div");
        this._Dimmer.addClassNames(["com-runwaysdk-ui-factory-runway-Widget","overlay-dimmer"]);
        UI.DOMFacade.getBody().appendChild(this._Dimmer);
      }
      this._isModal = isModal;
    },
    
    show : function()
    {
      this.setStyle("display", "inline");
      
      if (this._Dimmer) {
        this._Dimmer.setStyle("display", "inline")
      }
    },
    
    hide : function()
    {
      this.setStyle("display", "none");
      
      if (this._isModal) {
        this._Dimmer.setStyle("display", "none")
      }
    },
    
    close : function() {
      this.destroy();
    },
    
    setVisible : function(bool) {
      if (bool) {
        if (!this.isRendered()) 
          this.render();
        
        this.setStyle("visibility", "visible");
      }
      else if (this.isRendered()) {
        this.setStyle("visibility", "hidden");
      }
    },
    
    bringToFront : function()
    {
      OverlayManager.bringToFront(this);
    },
    
    manageZIndex : function() {
      return true;
    },
    
    render : function(parent)
    {
      if (this.manageZIndex()) {
        if (this._isModal)
        {
          this._Dimmer.render(parent);
          OverlayManager.appendOverlay(this._Dimmer);
        }
        
        this.$render(parent);
        OverlayManager.appendOverlay(this);
      }
      else {
        this.$render(parent);
      }
    },
    destroy : function()
    {
      if (this._isModal) {
        OverlayManager.removeOverlay(this._Dimmer);
        this._Dimmer.destroy();
      }
      OverlayManager.removeOverlay(this);
      
      this.$destroy();
    }
    
  }
});

var OverlayManager = Mojo.Meta.newClass(Mojo.RW_PACKAGE+"OverlayManager", {

  IsSingleton : true,

  Instance : {
    
    initialize: function()
    {
      this.$initialize();
      
      this._topIndex = 1;
      this._bottomIndex = -1;
      
      this._set = new com.runwaysdk.structure.HashSet();
    },
    
    appendOverlay : function(overlay)
    {
      if (!this.isManaged(overlay))
      {
        this._set.add(overlay);
        this.bringToFront(overlay);
      }
    },
    
    removeOverlay : function(overlay)
    {
      if (this.isManaged(overlay))
      {
        this._set.remove(overlay);
        this.bringToIndex(overlay, 0);
      }
    },
    
    isManaged : function(obj)
    {
      return this._set.contains(obj);
    },
    
    getOverlays : function()
    {
      return this._set;
    },
    
    bringToFront : function(el)
    {
      //if (this.isManaged(el))
      //{
        this.bringToIndex(el, this._topIndex)
      
        this._topIndex++;
     //}
    },
    
    bringToBack : function(el)
    {
      //if (this.isManaged(el))
      //{
        this.bringToIndex(el, this._bottomIndex)
      
        this._bottomIndex--;
      //}
    },
    
    // Private:
    bringToIndex : function(el, index)
    {
      UI.DOMFacade.setStyle(el, "z-index", index);
      UI.DOMFacade.setStyle(el, "zIndex", index);
    }
    
  },
  
  Static : {
    
    appendOverlay : function(el)
    {
      OverlayManager.getInstance().appendOverlay(el);
    },
    
    removeOverlay : function(el)
    {
      OverlayManager.getInstance().removeOverlay(el);
    },
    
    bringToFront : function(el)
    {
      OverlayManager.getInstance().bringToFront(el);
    },
    
    bringToBack : function(el)
    {
      OverlayManager.getInstance().bringToBack(el);
    },
    
    getOverlays : function()
    {
      return OverlayManager.getInstance().getOverlays();
    }
    
  }

});

})();
