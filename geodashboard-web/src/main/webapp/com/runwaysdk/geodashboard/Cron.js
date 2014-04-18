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
    "everyHour" : "Every ${hour} at ${actualMinute} minutes past the hour",
    "everyDay" : "Every ${day} at ${actualHour}:${actualMinute}",
    "everyWeek" : "Every ${week} on ${actualDayOfWeek} at ${actualHour}:${actualMinute}",
    "everyMonth" : "Every ${month} on the ${actualDay} at ${actualHour}:${actualMinute}",
        
    "minute" : "minute",
    "hour" : "hour",
    "day" : "day",
    "week" : "week",
    "month" : "month",
    "dow" : "day of week",
    "period" : "Period",
        
    "sunday" : "Sunday",
    "monday" : "Monday",
    "tuesday" : "Tuesday",
    "wednesday" : "Wednesday",
    "thursday" : "Thursday",
    "friday" : "Friday",
    "saturday" : "Saturday",
    
    "scheduledRun" : "Scheduled Run"
  });

  Mojo.Meta.newClass(pack+"CronUtil", {
    
    IsSingleton: true,
    
    Static : {
      getPeriodFromValue : function(value)
      {
        var regexMapper = [
          {regex: /0 \* \* \* \* \?/, name: com.runwaysdk.geodashboard.CronEntry.EVERY_MINUTE},
          {regex: /0 \d+ \* \* \* \?/, name: com.runwaysdk.geodashboard.CronEntry.EVERY_HOUR},
          {regex: /0 \d+ \d+ \* \* \?/, name: com.runwaysdk.geodashboard.CronEntry.EVERY_DAY},
          {regex: /0 \d+ \d+ \? \* ./, name: com.runwaysdk.geodashboard.CronEntry.EVERY_WEEK},
          {regex: /0 \d+ \d+ \d+ \* \?/, name: com.runwaysdk.geodashboard.CronEntry.EVERY_MONTH}
        ];
        
        for (var i = 0; i < regexMapper.length; ++i)
        {
          if (value.match(regexMapper[i].regex))
          {
            return regexMapper[i].name;
          }
        }
            
        return com.runwaysdk.geodashboard.CronEntry.EVERY_MINUTE;
      },
      cronToHumanReadable : function(cronStr) {
        if (cronStr == null || cronStr == "") {
          //return com.runwaysdk.Localize.localize(widgetName, "never");
          return 'never';
        }
        
        var period = com.runwaysdk.geodashboard.CronUtil.getPeriodFromValue(cronStr);
        
        if (period == null)
        {
          return cronStr;
        }
        
        var label = com.runwaysdk.Localize.localize(widgetName,period);        
        var cronStrParts = cronStr.split(" ");
        
        var minute = cronStrParts[com.runwaysdk.geodashboard.CronEntry.MINUTES];          
        var hour = cronStrParts[com.runwaysdk.geodashboard.CronEntry.HOURS];                    
        var dow = cronStrParts[com.runwaysdk.geodashboard.CronEntry.DOW];
        var day = cronStrParts[com.runwaysdk.geodashboard.CronEntry.DOM];
        
        if (minute.length === 1) {
          minute = "0" + minute;
        }
        
        if (period === com.runwaysdk.geodashboard.CronEntry.EVERY_MINUTE) {
          return label.replace("${minute}", com.runwaysdk.Localize.localize(widgetName, "minute"));
        }
        else if (period === com.runwaysdk.geodashboard.CronEntry.EVERY_HOUR) {
          return label.replace("${hour}", com.runwaysdk.Localize.localize(widgetName, "hour")).replace("${actualMinute}", minute);
        }
        else if (period === com.runwaysdk.geodashboard.CronEntry.EVERY_DAY) {
          return label.replace("${day}", com.runwaysdk.Localize.localize(widgetName, "day")).replace("${actualHour}", hour).replace("${actualMinute}", minute);
        }
        else if (period === com.runwaysdk.geodashboard.CronEntry.EVERY_WEEK) {          
          var actualWeek = com.runwaysdk.geodashboard.CronUtil.convertDayOfWeekNumberToLocalizedWeek(dow);
          return label.replace("${week}", com.runwaysdk.Localize.localize(widgetName, "week")).replace("${actualDayOfWeek}", actualWeek).replace("${actualHour}", hour).replace("${actualMinute}", minute);
        }
        else if (period === com.runwaysdk.geodashboard.CronEntry.EVERY_MONTH) {
          var actualDay = com.runwaysdk.geodashboard.CronUtil.formatDayValue(day);
          return label.replace("${month}", com.runwaysdk.Localize.localize(widgetName, "month")).replace("${actualDay}", actualDay).replace("${actualHour}", hour).replace("${actualMinute}", minute);
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
  
  Mojo.Meta.newClass(pack+'CronInput', {
    Instance : {
      initialize : function(div, input)
      {
        this._div = div;
        this._input = input;
      },
      getValue : function()
      {
        return this._input.getValue();
      },
      setValue : function(value)
      {
        this._input.setValue(value);  
      },
      hide : function()
      {
        this._div.setStyle("display", "none");
      },
      show : function()
      {
        this._div.setStyle("display", "inline");
      },
      getDiv : function()
      {
        return this._div;
      },
      getInput : function()
      {
        return this._input;
      }
    }    
  });
  
  Mojo.Meta.newClass(pack+'CronEntry', {
    Extends : com.runwaysdk.geodashboard.AbstractFormEntry,
    Constants: {
      SECONDS : 0,
      MINUTES : 1,
      HOURS : 2,
      DOM : 3,
      MONTH : 4,
      DOW : 5,
      SUNDAY : 1,
      MONDAY : 2,
      TUESDAY : 3,
      WEDNESDAY : 4,
      THURSDAY : 5,
      FRIDAY : 6,
      SATURDAY : 7,
      EVERY_MINUTE : "everyMinute",
      EVERY_HOUR : "everyHour",
      EVERY_DAY : "everyDay",
      EVERY_WEEK : "everyWeek",
      EVERY_MONTH : "everyMonth"
    },    
    Instance : {
      initialize : function(id)
      {
        this.$initialize();
        
        this._enabled = false;
        
        this._section = this.getFactory().newElement('section');
        this._section.setAttribute('class', 'form-container');
        
        
        var scheduledRunDiv = this.getFactory().newElement('div');
        scheduledRunDiv.setAttribute('class', 'field-row clearfix');
                  
        var scheduledRunSpan = this.getFactory().newElement('span');
        scheduledRunSpan.setAttribute('class', 'label-text');
        scheduledRunSpan.setInnerHTML(this.localize('scheduledRun'));
          
        var scheduledRunInnerDiv = this.getFactory().newElement('div');
        scheduledRunInnerDiv.setAttribute('class', 'checks-frame');
        
        var enableDiv = this.getFactory().newElement('span');
        enableDiv.addEventListener("click", {handleEvent: Mojo.Util.bind(this, this._onClickEnable)});
        
        
        this._enableRadio = this.getFactory().newElement("input", {type: "radio", name: "scheduledRun", value: "enabled"});

        var enableLabel = new com.runwaysdk.ui.factory.runway.Label(this.localize('enabled'));
        enableDiv.appendChild(this._enableRadio);
        enableLabel.setAttribute('for', "enabled");

        this._disableRadio = this.getFactory().newElement("input", {type: "radio", name: "scheduledRun", value: "disabled"});
        
        var disableDiv = this.getFactory().newElement('span');
        disableDiv.appendChild(this._disableRadio);
        disableDiv.addEventListener("click", {handleEvent: Mojo.Util.bind(this, this._onClickDisable)});
        
        var disableLabel = new com.runwaysdk.ui.factory.runway.Label(this.localize('disabled'));
        disableLabel.setAttribute('for', "disabled");
        
        scheduledRunInnerDiv.appendChild(enableDiv);   
        scheduledRunInnerDiv.appendChild(enableLabel);
        scheduledRunInnerDiv.appendChild(disableDiv);   
        scheduledRunInnerDiv.appendChild(disableLabel);

        this.scheduledRunError = this.getFactory().newElement('div', {class:"error-message", id:id + "-error"});
        
        scheduledRunDiv.appendChild(scheduledRunSpan);
        scheduledRunDiv.appendChild(scheduledRunInnerDiv);
        scheduledRunDiv.appendChild(this.scheduledRunError);        
                        
        var periodOptions = [
          {value: com.runwaysdk.geodashboard.CronEntry.EVERY_MINUTE, label: this.localize("minute")},
          {value: com.runwaysdk.geodashboard.CronEntry.EVERY_HOUR, label: this.localize("hour")},
          {value: com.runwaysdk.geodashboard.CronEntry.EVERY_DAY, label: this.localize("day")},
          {value: com.runwaysdk.geodashboard.CronEntry.EVERY_WEEK, label: this.localize("week")},          
          {value: com.runwaysdk.geodashboard.CronEntry.EVERY_MONTH, label: this.localize("month")}
        ];
        
        this._period = this._generateSelectList('period', periodOptions);        
        this._minute = this._generateNumberPicker('minute', 0, 59, function(index){if (index < 10) {return "0"+index;} return index; });
        this._hour = this._generateNumberPicker('hour', 0, 23);
        this._dow = this._generateNumberPicker('dow', 0, 6, function(index){return com.runwaysdk.geodashboard.CronUtil.convertDayOfWeekNumberToLocalizedWeek(index);});
        this._day = this._generateNumberPicker('day', 1, 31, com.runwaysdk.geodashboard.CronUtil.formatDayValue);
        
        this._cronDiv = this.getFactory().newElement('div');        
        this._cronDiv.appendChild(this._period.getDiv());
        this._cronDiv.appendChild(this._minute.getDiv());
        this._cronDiv.appendChild(this._hour.getDiv());
        this._cronDiv.appendChild(this._dow.getDiv());
        this._cronDiv.appendChild(this._day.getDiv());
        this._cronDiv.addEventListener("change", Mojo.Util.bind(this, this._calcCronValue));
        
        this._section.appendChild(scheduledRunDiv);
        this._section.appendChild(this._cronDiv);
        this.setId(id);        
      },
      _onClickEnable : function() {
        this.setValue(com.runwaysdk.geodashboard.CronEntry.EVERY_MINUTE);
      },        
      _onClickDisable : function() {
        this.setValue(null);
      },
      _generateNumberPicker : function(id, startNum, endNum, formatter)
      {
        var options = [];
          
        for (var i = startNum; i <= endNum; ++i) {
          var localized = i;
          if (formatter != null) {
            localized = formatter(i);
          }
            
          options.push({value: i, label: localized});
        }
          
        return this._generateSelectList(id, options);
      },      
      _generateSelectList : function (id, options)
      {
        var div = this.getFactory().newElement('div');
        div.setAttribute('class', 'field-row clearfix');
                    
        var label = this.getFactory().newElement('label');
        label.setAttribute('for', id);
        label.setInnerHTML(this.localize(id));
            
        var select = this.getFactory().newElement('select');
        select.setAttribute('id', id);
        select.setStyle('width', '269px');
          
        for(var i = 0; i < options.length; i++)
        {
          select.appendChild(this.getFactory().newElement("option", {value: options[i].value, innerHTML: options[i].label}));
        }
          
        div.appendChild(label);
        div.appendChild(select);

        return new com.runwaysdk.geodashboard.CronInput(div, select);
      },      
      _calcCronValue : function()
      {
        var period = this._period.getValue();
        var minute = this._minute.getValue();
        var hour = this._hour.getValue();
        var dayOfTheWeek = this._dow.getValue();
        var dayNum = this._day.getValue();
        var month = null; 
          
        if (period === com.runwaysdk.geodashboard.CronEntry.EVERY_MINUTE) {
          minute = null;
          hour = null;
          dayNum = null;
          dayOfTheWeek = null;
          month = null;
        }
        else if (period === com.runwaysdk.geodashboard.CronEntry.EVERY_HOUR) {
          minute = minute || 0;
          hour = null;
          dayNum = null;
          dayOfTheWeek = null;
          month = null;
        }
        else if (period == com.runwaysdk.geodashboard.CronEntry.EVERY_DAY) {
          minute = minute || 0;
          hour = hour || 0;
          dayNum = null;
          dayOfTheWeek = null;
          month = null;
        }
        else if (period == com.runwaysdk.geodashboard.CronEntry.EVERY_WEEK) {
          minute = minute || 0;
          hour = hour || 0;
          dayOfTheWeek = dayOfTheWeek || 0;
          dayNum = null;
          month = null;
        }
        else if (period === com.runwaysdk.geodashboard.CronEntry.EVERY_MONTH) {
          minute = minute || 0;
          hour = hour || 0;
          dayNum = dayNum || 1;
          month = null;
          dayOfTheWeek = null;
        }
        
        minute = minute == null ? "*" : minute;
        hour = hour == null ? "*" : hour;
        dayNum = dayNum == null ? (dayOfTheWeek == null ? "*" : "?") : dayNum;
        month = month == null ? "*" : month;
        dayOfTheWeek = dayOfTheWeek == null ? "?" : dayOfTheWeek;
          
        this.setValue("0 " + minute + " " + hour + " " + dayNum + " " + month + " " + dayOfTheWeek);
      },
      
      localize : function (key)
      {
        return com.runwaysdk.Localize.localize(widgetName, key);
      },
      getDiv : function()
      {
        return this._section;
      },
      accept : function(visitor)
      {
        visitor.visitDefaultInput(this);
      },
      getValue : function()
      {
        return this._value;
      },
      setValue : function(value)
      {
        this._value = value;
        
        this._period.hide();          
        this._minute.hide();
        this._hour.hide();
        this._dow.hide();
        this._day.hide();     

        if (this._value !== null && this._value !== "")
        {        
          var cronStrParts = this.getValue().split(" ");
          var periodValue = com.runwaysdk.geodashboard.CronUtil.getPeriodFromValue(this._value);
          
          this._period.setValue(periodValue);
          this._period.show();
                        
          if (periodValue === com.runwaysdk.geodashboard.CronEntry.EVERY_HOUR) {
            this._minute.setValue(cronStrParts[com.runwaysdk.geodashboard.CronEntry.MINUTES]);
            this._minute.show();
          }
          else if (periodValue === com.runwaysdk.geodashboard.CronEntry.EVERY_DAY) {
            this._minute.setValue(cronStrParts[com.runwaysdk.geodashboard.CronEntry.MINUTES]);
            this._minute.show();
            
            this._hour.setValue(cronStrParts[com.runwaysdk.geodashboard.CronEntry.HOURS]);
            this._hour.show();
          }
          else if (periodValue === com.runwaysdk.geodashboard.CronEntry.EVERY_WEEK) {
            this._minute.setValue(cronStrParts[com.runwaysdk.geodashboard.CronEntry.MINUTES]);
            this._minute.show();
            
            this._hour.setValue(cronStrParts[com.runwaysdk.geodashboard.CronEntry.HOURS]);
            this._hour.show();
            
            this._dow.setValue(cronStrParts[com.runwaysdk.geodashboard.CronEntry.DOW]);
            this._dow.show();
          }
          else if (periodValue === com.runwaysdk.geodashboard.CronEntry.EVERY_MONTH) {
            this._minute.setValue(cronStrParts[com.runwaysdk.geodashboard.CronEntry.MINUTES]);
            this._minute.show();
            
            this._hour.setValue(cronStrParts[com.runwaysdk.geodashboard.CronEntry.HOURS]);
            this._hour.show();
            
            this._day.setValue(cronStrParts[com.runwaysdk.geodashboard.CronEntry.DOM]);
            this._day.show();
          }
          
          this._enableRadio.getRawEl().checked = true;
          this._disableRadio.getRawEl().checked = false;
          this._cronDiv.setStyle("display", "inline");
          this._enabled = true;
        }
        else
        {            
          this._cronDiv.setStyle("display", "none");
          
          this._disableRadio.getRawEl().checked = true;
          this._enableRadio.getRawEl().checked = false;
          this._enabled = false;          
        }        
      },
      getName : function()
      {
        return this.getId();
      },
      removeInlineError : function ()
      {
      },    
      addInlineError : function (msg) {
      }
    }
  });
  
  return Mojo.Meta.alias(pack+"*");
})();
  