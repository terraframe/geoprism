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
//define(["jquery-ui", "./Factory", "../runway/widget/Widget",], function(){
(function(){

  var RUNWAY_UI = Mojo.Meta.alias("com.runwaysdk.ui.*");
  var RW = Mojo.Meta.alias(Mojo.RW_PACKAGE + "*");
  
  var TabPanel = Mojo.Meta.newClass(Mojo.JQUERY_PACKAGE+'tabpanel.TabPanel', {
    
    Extends : RW.Widget,
    
    Instance: {
      initialize : function(config) {
        config = config || {};
        
        this.$initialize(config.el || "div");
        
        this._ul = this.getFactory().newElement("ul");
        this.appendChild(this._ul);
        
        this._panelCount = 0;
      },
      
      addSwitchPanelEventListener : function(fnListener) {
        this.addEventListener(SwitchPanelEvent, {handleEvent: fnListener});
      },
      
      addPanel : function(title, content) {
        var panel = new Panel(this._panelCount, title, content);
        this.appendChild(panel);
        
        var that = this;
        var onPanelClick = function(mouseEvent) {
          that.dispatchEvent(new SwitchPanelEvent(panel));
        };
        
        panel.getTitleLi().addEventListener("click", onPanelClick);
        this._ul.appendChild(panel.getTitleLi());
        
        this._panelCount++;
      },
      
      render : function(parent) {
        this.$render(parent);
        $(this.getRawEl()).tabs();
      }
    }
  });
  
  var Panel = Mojo.Meta.newClass(Mojo.JQUERY_PACKAGE+"tabpanel.Panel", {
    
    Extends : RW.Widget,
    
    Instance : {
      initialize : function(panelNumber, title, content) {
        this.$initialize("div");
        
        this._panelNumber = panelNumber;
        
        if (Mojo.Util.isString(content)) {
          this.setInnerHTML(content);
        }
        else {
          this.appendChild(content);
        }
        
        this._li = this.getFactory().newElement("li");
        this._li.appendChild(this.getFactory().newElement("a", {href: "#" + this.getId(), innerHTML: title}))
      },
      
      getPanelNumber : function() {
        return this._panelNumber;
      },
      
      getTitleLi : function() {
        return this._li;
      },
      
      render : function(parent) {
        this.$render(parent);
      }
    }
    
  });
  
  var SwitchPanelEvent = Mojo.Meta.newClass(Mojo.JQUERY_PACKAGE+"tabpanel.SwitchPanelEvent", {
    
    Extends : com.runwaysdk.event.CustomEvent,
    
    Instance : {
      initialize : function(panel) {
        this.$initialize();
        this._panel = panel;
      },
      
      getPanel : function() {
        return this._panel;
      }
    }
    
  });
  
  return TabPanel;
  
})();
