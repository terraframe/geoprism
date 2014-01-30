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

//define(["../../ClassFramework", "../../Util", "../RunwaySDK_UI", "../factory/runway/widget/Widget", "../factory/runway/form/Form", "jquery-cron"], function(ClassFramework, Util, UI, Widget, Forms) {
(function(){  

  var ClassFramework = Mojo.Meta;
  var Util = Mojo.Util;
  var Widget = com.runwaysdk.ui.factory.runway.Widget;
  var Forms = Mojo.Meta.alias(Mojo.RW_PACKAGE+"*");
  
  var pack = "com.runwaysdk.ui.scheduler.";
  
  var CronPicker = ClassFramework.newClass(pack+'CronPicker', {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(cronStr, config) {
        
        this.$initialize("div");
        
        this._cronStr = cronStr || this._cronStr || null;
        this._enabled = false;
        this._config = config || {};
      },
      
      getImpl : function() {
        return this._impl;
      },
      
      _onClickEnable : function() {
        this._enableRadio.getRawEl().checked = true;
        this._disableRadio.getRawEl().checked = false;
        this._cron.setStyle("display", "inline");
        this._enabled = true;
      },
      
      _onClickDisable : function() {
        this._disableRadio.getRawEl().checked = true;
        this._enableRadio.getRawEl().checked = false;
        this._cron.setStyle("display", "none");
        this._enabled = false;
      },
      
      getCronString : function() {
        if (this._enabled) {
          return this._impl.cron("value");
        }
        else {
          return null;
        }
      },
      
      setCronString : function(cronStr) {
        if (cronStr === "") {
          cronStr = null;
        }
        
        if (this.isRendered()) {
          this._impl.cron("value", cronStr);
        }
        else {
          this._cronStr = cronStr;
        }
      },
      
      render : function(p) {
        var enableDiv = this.getFactory().newElement("div", null, {display: "inline"});
        enableDiv.addEventListener("click", {handleEvent: Mojo.Util.bind(this, this._onClickEnable)});
        this.appendChild(enableDiv);
        var enableLabel = this.getFactory().newElement("label", {innerHTML: "enabled"});
        enableDiv.appendChild(enableLabel);
        this._enableRadio = this.getFactory().newElement("input", {type: "radio", name: "enabled", value: "enabled"});
        this._enableRadio.addEventListener("click", {handleEvent: Mojo.Util.bind(this, this._onClickEnable)});
        enableDiv.appendChild(this._enableRadio);
        
        var disableDiv = this.getFactory().newElement("div", null, {display: "inline"});
        disableDiv.addEventListener("click", {handleEvent: Mojo.Util.bind(this, this._onClickDisable)});
        this.appendChild(disableDiv);
        var disableLabel = this.getFactory().newElement("label", {innerHTML: "disabled"}, {"padding-left": "30px"});
        disableDiv.appendChild(disableLabel);
        this._disableRadio = this.getFactory().newElement("input", {type: "radio", name: "enabled", value: "disabled"});
        disableDiv.appendChild(this._disableRadio);
        
        this.appendChild(this.getFactory().newElement("br"));
        
        this._cron = this.getFactory().newElement("div");
        this.appendChild(this._cron);
        
        this.$render(p);
        
        if (this._cronStr == null) {
          this._onClickDisable();
        }
        else {
          this._onClickEnable();
          this._config.initial = this._cronStr;
        }
        
        this._impl = $(this._cron.getRawEl()).cron(this._config);
      }
    }
  });
  
  var CronInput = Mojo.Meta.newClass(pack+'CronInput', {
    Extends : Forms.FormInput,
    Instance : {
      initialize : function(name, config)
      {
        config = config || {};
        
        this._impl = new CronPicker(config.cronStr);
        
        this.$initialize(this._impl, null, name, config);
      },
      accept : function(visitor)
      {
        visitor.visitDefaultInput(this);
      },
      getValue : function()
      {
        return this._impl.getCronString();
      },
      setValue : function(val) {
        this._impl.setCronString(val);
      },
      render: function(p) {
        this._impl.render(p);
      }
    }
  });
  
  return Mojo.Meta.alias(pack+"*");
})();
  