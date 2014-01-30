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
  
  var Dialog = Mojo.Meta.newClass(Mojo.JQUERY_PACKAGE+'Dialog', {
    
    Extends : RW.Widget,
    
    Implements : [RUNWAY_UI.DialogIF, RUNWAY_UI.ButtonProviderIF],
    
    Instance: {
      initialize : function(title, config) {
        config = config || {};
        config.title = title;
        config.buttons = config.buttons || {};
        config.close = Mojo.Util.bind(this, function(){
          if (config.destroyOnExit && !this._isHide) {
            this.destroy();
          }
        });
        this._config = config;
        
        this.$initialize(config.el);
        
        this._buttons = new com.runwaysdk.structure.HashSet();
      },
      getImpl : function() {
        return this._impl;
      },
      getHeader : function() {
        throw new com.runwaysdk.Exception("Not implemented");
      },
      getFooter : function() {
        throw new com.runwaysdk.Exception("Not implemented");
      },
      setTitle : function (title) {
        this.getImpl().dialog("option", "title", title);
      },
      appendContent : function(content) {
        if (Mojo.Util.isString(content)) {
          this.appendInnerHTML(content);
        }
        else {
          this.appendChild(content);
        }
      },
      setContent : function(content) {
        if (Mojo.Util.isString(content)) {
          this.setInnerHTML(content);
        }
        else {
          this.removeAllChildren();
          this.appendChild(content);
        }
      },
      addButton: function(label, handler, context) {
        this._config.buttons[label] = handler;
        
        if (this.isRendered()) {
          var buttons = this.getImpl().dialog("option", "buttons"); // getter
          buttons[label] = handler;
          this.getImpl().dialog("option", "buttons", buttons); // setter
        }
      },
      removeButton : function() {
        
      },
      getButton : function() {
        
      },
      getButtons : function()
      {
        return this.getImpl().dialog("option", "buttons");
      },
      close : function() {
        this.destroy();
      },
      show : function() {
        $(this.getImpl()).dialog('open');
      },
      hide : function() {
        this._isHide = true;
        $(this.getImpl()).dialog("close");
        this._isHide = false;
      },
      render : function(parent) {
        this.$render(parent);
        
        this._impl = $(this.getRawEl()).dialog(this._config);
      },
      destroy : function() {
        var parent = this.getParent();
        
        this.$destroy();
        parent.destroy();
        
        if (this._config.modal) {
          $(".ui-widget-overlay.ui-front").remove();
        }
      }
    }
  });

})();
