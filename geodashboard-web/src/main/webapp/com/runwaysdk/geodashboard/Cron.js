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
  var Widget = com.runwaysdk.ui.factory.runway.Widget;
  var Forms = Mojo.Meta.alias(Mojo.RW_PACKAGE+"*");
  
  var pack = "com.runwaysdk.geodashboard.";
  var widgetName = pack+'CronPicker';
  
  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage(widgetName, {
    "disabled" : "Disabled",
    "enabled" : "Enabled",
    "never" : "Never",
    "everyMinute" : "Every ${minute}",
    "everyHour" : "Every ${hour} at ${actualMinute} minutes past the hour.",
    "everyDay" : "Every ${day} at ${actualHour}:${actualMinute}.",
    "everyWeek" : "Every ${week} on ${actualDayOfWeek} at ${actualHour}:${actualMinute}.",
    "everyMonth" : "Every ${month} on the ${actualDay} at ${actualHour}:${actualMinute}.",
        
    "minute" : "minute",
    "hour" : "hour",
    "day" : "day",
    "week" : "week",
    "month" : "month",
        
    "sunday" : "Sunday",
    "monday" : "Monday",
    "tuesday" : "Tuesday",
    "wednesday" : "Wednesday",
    "thursday" : "Thursday",
    "friday" : "Friday",
    "saturday" : "Saturday"    
  });

  var regexMapper = [
    {regex: /\* \* \* \* \*/, name: "everyMinute"},
    {regex: /\d+ \* \* \* \*/, name: "everyHour"},
    {regex: /\d+ \d+ \* \* \*/, name: "everyDay"},
    {regex: /\d+ \d+ \* \* \d+/, name: "everyWeek"},
    {regex: /\d+ \d+ \d+ \* \*/, name: "everyMonth"}
  ];
    
  Mojo.Meta.newClass(pack+"CronUtil", {
    
    IsSingleton: true,
    
    Static : {
      getEveryStrFromCron : function(cronStr) {                            
        for (var i = 0; i < regexMapper.length; ++i) {
          if (cronStr.match(regexMapper[i].regex)) {
            return regexMapper[i].name;
          }
        }
        
        return "everyMinute";
      },
      
      cronToHumanReadable : function(cronStr) {
        if (cronStr == null || cronStr == "") {
          return com.runwaysdk.Localize.localize(widgetName, "never");
        }
        
        var everyStrName = com.runwaysdk.geodashboard.CronUtil.getEveryStrFromCron(cronStr);
        
        if (everyStrName == null)
        {
        return cronStr;
        }
        
        var everyStr = com.runwaysdk.Localize.localize(widgetName,everyStrName);        
        var cronStrParts = cronStr.split(" ");
        
        if (cronStrParts[0].toString().length === 1) {
          cronStrParts[0] = "0" + cronStrParts[0].toString();
        }
        
        if (everyStrName == "everyMinute") {
          return everyStr.replace("${minute}", com.runwaysdk.Localize.localize(widgetName, "minute"));
        }
        else if (everyStrName == "everyHour") {
          return everyStr.replace("${hour}", com.runwaysdk.Localize.localize(widgetName, "hour")).replace("${actualMinute}", cronStrParts[0]);
        }
        else if (everyStrName == "everyDay") {
          return everyStr.replace("${day}", com.runwaysdk.Localize.localize(widgetName, "day")).replace("${actualHour}", cronStrParts[1]).replace("${actualMinute}", cronStrParts[0]);
        }
        else if (everyStrName == "everyWeek") {
          var actualWeek = com.runwaysdk.geodashboard.CronUtil.convertDayOfWeekNumberToLocalizedWeek(cronStrParts[4]);
          return everyStr.replace("${week}", com.runwaysdk.Localize.localize(widgetName, "week")).replace("${actualDayOfWeek}", actualWeek).replace("${actualHour}", cronStrParts[1]).replace("${actualMinute}", cronStrParts[0]);
        }
        else if (everyStrName == "everyMonth") {
          var day = this.formatDayValue(cronStrParts[2]);
          return everyStr.replace("${month}", com.runwaysdk.Localize.localize(widgetName, "month")).replace("${actualDay}", day).replace("${actualHour}", cronStrParts[1]).replace("${actualMinute}", cronStrParts[0]);
        }
        else {
          return cronStr;
        }
      },
      
      formatDayValue : function(index) {
        index = parseInt(index,10);
        
        if (index === 2 || index === 22) {
          return index + "nd";
        }
        else if (index === 3 || index === 23) {
          return index + "rd";
        }
        else if (index === 1 || index === 21 || index === 31) {
          return index + "st";
        }
        
        return index + "th";
      },
      
      convertDayOfWeekNumberToLocalizedWeek : function(weekNum) {
        if (weekNum == 0) {
          return com.runwaysdk.Localize.localize(widgetName, "sunday");
        }
        else if (weekNum == 1) {
          return com.runwaysdk.Localize.localize(widgetName, "monday");
        }
        else if (weekNum == 2) {
          return com.runwaysdk.Localize.localize(widgetName, "tuesday");
        }
        else if (weekNum == 3) {
          return com.runwaysdk.Localize.localize(widgetName, "wednesday");
        }
        else if (weekNum == 4) {
          return com.runwaysdk.Localize.localize(widgetName, "thursday");
        }
        else if (weekNum == 5) {
          return com.runwaysdk.Localize.localize(widgetName, "friday");
        }
        else if (weekNum == 6) {
          return com.runwaysdk.Localize.localize(widgetName, "saturday");
        }
      }
    }
    
  });    
  
  var CronPicker = ClassFramework.newClass(widgetName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(cronStr, config) {
        
        this.$initialize("div");
        
        this._cronStr = cronStr || null;
        this._enabled = false;
        this._config = config || {};
        
        var fac = this.getFactory();
        
        this._rangePicker = fac.newElement("select");
        this._rangePicker.appendChild(fac.newElement("option", {value: "everyMinute", innerHTML: this.localize("minute")}));
        this._rangePicker.appendChild(fac.newElement("option", {value: "everyHour", innerHTML: this.localize("hour")}));
        this._rangePicker.appendChild(fac.newElement("option", {value: "everyDay", innerHTML: this.localize("day")}));
        this._rangePicker.appendChild(fac.newElement("option", {value: "everyWeek", innerHTML: this.localize("week")}));
        this._rangePicker.appendChild(fac.newElement("option", {value: "everyMonth", innerHTML: this.localize("month")}));
        this._rangePicker.setValue(com.runwaysdk.geodashboard.CronUtil.getEveryStrFromCron(this.getCronString() || "* * * * *"));
        
        this._minutePicker = this._generateNumberPicker(0, 59, function(index){if (index < 10) {return "0"+index;} return index; });
        this._hourPicker = this._generateNumberPicker(0, 23);
        this._dayOfWeekPicker = this._generateNumberPicker(0, 6, function(index){return com.runwaysdk.geodashboard.CronUtil.convertDayOfWeekNumberToLocalizedWeek(index);});
        this._dayOfWeekPicker.setValue(0);
        this._dayPicker = this._generateNumberPicker(1, 31, com.runwaysdk.geodashboard.CronUtil.formatDayValue);
        this._dayPicker.setValue(1);
      },
      
      _generateNumberPicker : function(startNum, endNum, formatter) {
        var fac = this.getFactory();
        
        var picker = fac.newElement("select");
        for (var i = startNum; i <= endNum; ++i) {
          var localized = i;
          if (formatter != null) {
            localized = formatter(i);
          }
          
          picker.appendChild(fac.newElement("option", {value: i, innerHTML: localized}));
        }
        return picker;
      },
      
      getImpl : function() {
        return this._impl;
      },
      
      _onClickEnable : function() {
        this._enableRadio.getRawEl().checked = true;
        this._disableRadio.getRawEl().checked = false;
        this._cron.setStyle("display", "inline");
        this._enabled = true;
        
        if (this._cronStr == null) {
          this._cronStr = "* * * * *";
          this._rangePicker.setValue("everyMinute");
        }
        this._writeCronHtml();
      },
      
      _onClickDisable : function() {
        this._disableRadio.getRawEl().checked = true;
        this._enableRadio.getRawEl().checked = false;
        this._cron.setStyle("display", "none");
        this._enabled = false;
        this._cronStr = null;
      },
      
      getCronString : function() {
        return this._cronStr;
      },
      
      setCronString : function(cronStr) {
        if (cronStr === "") {
          cronStr = null;
        }
        
        this._cronStr = cronStr;
        if (cronStr != null) {
          this._rangePicker.setValue(com.runwaysdk.geodashboard.CronUtil.getEveryStrFromCron(this._cronStr));
        }
        
        if (this.isRendered()) {
          if (this._cronStr == null) {
            this._onClickDisable();
          }
          else {
            this._onClickEnable();
          }
          
          this._writeCronHtml();
        }
      },
      
      _writeHtml : function() {
        // Write Enable Radio Button
        var enableDiv = this.getFactory().newElement("div", null, {display: "inline"});
        enableDiv.addEventListener("click", {handleEvent: Mojo.Util.bind(this, this._onClickEnable)});
        this.appendChild(enableDiv);
        var enableLabel = this.getFactory().newElement("label", {innerHTML: this.localize("enabled")});
        enableDiv.appendChild(enableLabel);
        this._enableRadio = this.getFactory().newElement("input", {type: "radio", name: "enabled", value: "enabled"});
        this._enableRadio.addEventListener("click", {handleEvent: Mojo.Util.bind(this, this._onClickEnable)});
        enableDiv.appendChild(this._enableRadio);
        
        // Write Disable Radio Button
        var disableDiv = this.getFactory().newElement("div", null, {display: "inline"});
        disableDiv.addEventListener("click", {handleEvent: Mojo.Util.bind(this, this._onClickDisable)});
        this.appendChild(disableDiv);
        var disableLabel = this.getFactory().newElement("label", {innerHTML: this.localize("disabled")}, {"padding-left": "30px"});
        disableDiv.appendChild(disableLabel);
        this._disableRadio = this.getFactory().newElement("input", {type: "radio", name: "enabled", value: "disabled"});
        disableDiv.appendChild(this._disableRadio);
        
        this.appendChild(this.getFactory().newElement("br"));
        
        // Create the div that holds the cron picker html and either show or hide it based on if we're enabled or diasbled.
        this._cron = this.getFactory().newElement("div");
        this.appendChild(this._cron);
        
        if (this._cronStr == null) {
          this._onClickDisable();
        }
        else {
          this._onClickEnable();
        }
        
        this._writeCronHtml();
      },
      
      _calcCronStr : function(minute, hour, dayNum, month, dayOfTheWeek) {
        var everyStrName = this._rangePicker.getValue();
        
        if (everyStrName === "everyMinute") {
          minute = null;
          hour = null;
          dayNum = null;
          dayOfTheWeek = null;
          month = null;
        }
        else if (everyStrName === "everyHour") {
          minute = minute || 0;
          hour = null;
          dayNum = null;
          dayOfTheWeek = null;
          month = null;
        }
        else if (everyStrName == "everyDay") {
          minute = minute || 0;
          hour = hour || 0;
          dayNum = null;
          dayOfTheWeek = null;
          month = null;
        }
        else if (everyStrName == "everyWeek") {
          minute = minute || 0;
          hour = hour || 0;
          dayOfTheWeek = dayOfTheWeek || 0;
          dayNum = null;
          month = null;
        }
        else if (everyStrName === "everyMonth") {
          minute = minute || 0;
          hour = hour || 0;
          dayNum = dayNum || 1;
          month = null;
          dayOfTheWeek = null;
        }
        
        minute = minute == null ? "*" : minute;
        hour = hour == null ? "*" : hour;
        dayNum = dayNum == null ? "*" : dayNum;
        month = month == null ? "*" : month;
        dayOfTheWeek = dayOfTheWeek == null ? "*" : dayOfTheWeek;
        
        this._cronStr = minute + " " + hour + " " + dayNum + " " + month + " " + dayOfTheWeek;
        this._writeCronHtml();
      },
      
      _writeCronHtml : function() {
        if (this.getCronString() != null) {
          this._cron.setInnerHTML("");
          
          var everyStrName = this._rangePicker.getValue();
          var everyStr = this.localize(everyStrName);
          
          var cronStrParts = this.getCronString().split(" ");
          
          if (everyStrName === "everyMinute") {
            this._cron.setInnerHTML(everyStr.replace("${minute}", this._rangePicker.getOuterHTML()));
            
            this._rangePicker = this._cron.getChildWithId(this._rangePicker.getId());
            this._rangePicker.setValue("everyMinute");
            this._rangePicker.addEventListener("change", Mojo.Util.bind(this, function(){this._calcCronStr();}));
          }
          else if (everyStrName === "everyHour") {
            this._rangePicker.setValue("everyHour");
            this._cron.setInnerHTML(everyStr.replace("${hour}", this._rangePicker.getOuterHTML()).replace("${actualMinute}", this._minutePicker.getOuterHTML()));
            
            this._rangePicker = this._cron.getChildWithId(this._rangePicker.getId());
            this._rangePicker.setValue("everyHour");
            
            this._minutePicker = this._cron.getChildWithId(this._minutePicker.getId());
            this._minutePicker.setValue(cronStrParts[0]);
            
            var changeFunc = Mojo.Util.bind(this, function(){this._calcCronStr(this._minutePicker.getValue());});
            this._minutePicker.addEventListener("change", changeFunc);
            this._rangePicker.addEventListener("change", changeFunc);
          }
          else if (everyStrName == "everyDay") {
            this._rangePicker.setValue("everyDay");
            
            var cronHTML = everyStr.replace("${day}", this._rangePicker.getOuterHTML());
            cronHTML = cronHTML.replace("${actualHour}", this._hourPicker.getOuterHTML());
            cronHTML = cronHTML.replace("${actualMinute}", this._minutePicker.getOuterHTML());
            this._cron.setInnerHTML(cronHTML);
            
            this._rangePicker = this._cron.getChildWithId(this._rangePicker.getId());
            this._rangePicker.setValue("everyDay");
            
            this._minutePicker = this._cron.getChildWithId(this._minutePicker.getId());
            this._minutePicker.setValue(cronStrParts[0]);
            
            this._hourPicker = this._cron.getChildWithId(this._hourPicker.getId());
            this._hourPicker.setValue(cronStrParts[1]);
            
            var changeFunc = Mojo.Util.bind(this, function(){this._calcCronStr(this._minutePicker.getValue(), this._hourPicker.getValue());});
            this._minutePicker.addEventListener("change", changeFunc);
            this._hourPicker.addEventListener("change", changeFunc);
            this._rangePicker.addEventListener("change", changeFunc);
          }
          else if (everyStrName == "everyWeek") {
            this._rangePicker.setValue("everyWeek");
            
            var cronHTML = everyStr.replace("${week}", this._rangePicker.getOuterHTML());
            cronHTML = cronHTML.replace("${actualMinute}", this._minutePicker.getOuterHTML());
            cronHTML = cronHTML.replace("${actualHour}", this._hourPicker.getOuterHTML());
            cronHTML = cronHTML.replace("${actualDayOfWeek}", this._dayOfWeekPicker.getOuterHTML());
            this._cron.setInnerHTML(cronHTML);
            
            this._rangePicker = this._cron.getChildWithId(this._rangePicker.getId());
            this._rangePicker.setValue("everyWeek");
            
            this._minutePicker = this._cron.getChildWithId(this._minutePicker.getId());
            this._minutePicker.setValue(cronStrParts[0]);
            
            this._hourPicker = this._cron.getChildWithId(this._hourPicker.getId());
            this._hourPicker.setValue(cronStrParts[1]);
            
            this._dayOfWeekPicker = this._cron.getChildWithId(this._dayOfWeekPicker.getId());
            this._dayOfWeekPicker.setValue(cronStrParts[4]);
            
            var changeFunc = Mojo.Util.bind(this, function(){this._calcCronStr(this._minutePicker.getValue(), this._hourPicker.getValue(), null, null, this._dayOfWeekPicker.getValue());});
            this._minutePicker.addEventListener("change", changeFunc);
            this._hourPicker.addEventListener("change", changeFunc);
            this._dayOfWeekPicker.addEventListener("change", changeFunc);
            this._rangePicker.addEventListener("change", changeFunc);
          }
          else if (everyStrName == "everyMonth") {
            this._rangePicker.setValue("everyMonth");
            
            var cronHTML = everyStr.replace("${month}", this._rangePicker.getOuterHTML());
            cronHTML = cronHTML.replace("${actualMinute}", this._minutePicker.getOuterHTML());
            cronHTML = cronHTML.replace("${actualHour}", this._hourPicker.getOuterHTML());
            cronHTML = cronHTML.replace("${actualDay}", this._dayPicker.getOuterHTML());
            this._cron.setInnerHTML(cronHTML);
            
            this._rangePicker = this._cron.getChildWithId(this._rangePicker.getId());
            this._rangePicker.setValue("everyMonth");
            
            this._minutePicker = this._cron.getChildWithId(this._minutePicker.getId());
            this._minutePicker.setValue(cronStrParts[0]);
            
            this._hourPicker = this._cron.getChildWithId(this._hourPicker.getId());
            this._hourPicker.setValue(cronStrParts[1]);
            
            this._dayPicker = this._cron.getChildWithId(this._dayPicker.getId());
            this._dayPicker.setValue(cronStrParts[2]);
            
            var changeFunc = Mojo.Util.bind(this, function(){this._calcCronStr(this._minutePicker.getValue(), this._hourPicker.getValue(), this._dayPicker.getValue(), null, null);});
            this._minutePicker.addEventListener("change", changeFunc);
            this._hourPicker.addEventListener("change", changeFunc);
            this._dayPicker.addEventListener("change", changeFunc);
            this._rangePicker.addEventListener("change", changeFunc);
          }
        }
      },
      
      render : function(p) {
        this._writeHtml();
        
        this.$render(p);
        
//        this._impl = $(this._cron.getRawEl()).cron(this._config);
      }
    }
  });
  
  Mojo.Meta.newClass(pack+'CronInput', {
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
  