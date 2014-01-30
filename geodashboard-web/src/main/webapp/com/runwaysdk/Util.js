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

//define(["./errorcatch"], function() {

window.Mojo = window.Mojo || {};

Mojo.Util = (function(){
  
  var Util = {
    ISO8601_REGEX : "^([0-9]{4})-([0-9]{2})-([0-9]{2})T([0-9]{2}):([0-9]{2}):([0-9]{2})([-+])([0-9]{2})([0-9]{2})$",
    
    // general purpose empty function
    emptyFunction : function(){},
    
    // reference to global object (e.g., window)
    GLOBAL : (function(){ return this; })(),
    
    // toString constants used for type checking
    IS_OBJECT_TO_STRING : Object.prototype.toString.call({}),
    IS_ARRAY_TO_STRING : Object.prototype.toString.call([]),
    IS_FUNCTION_TO_STRING : Object.prototype.toString.call(function(){}),
    IS_DATE_TO_STRING : Object.prototype.toString.call(new Date()),
    IS_STRING_TO_STRING : Object.prototype.toString.call(''),
    IS_NUMBER_TO_STRING : Object.prototype.toString.call(0),
    IS_BOOLEAN_TO_STRING : Object.prototype.toString.call(true),
    
    isObject : function(o)
    {
      return  o != null && Object.prototype.toString.call(o) === Util.IS_OBJECT_TO_STRING;
    },

    isArray : function(o)
    { 
      return o != null && Object.prototype.toString.call(o) === Util.IS_ARRAY_TO_STRING;
    },

    isFunction : function(o)
    {
      return o != null && Object.prototype.toString.call(o) === Util.IS_FUNCTION_TO_STRING;
    },

    isDate : function(o)
    {
      return o != null && Object.prototype.toString.call(o) === Util.IS_DATE_TO_STRING;
    },

    isString : function(o)
    {
      return o != null && Object.prototype.toString.call(o) === Util.IS_STRING_TO_STRING;
    },

    isNumber : function(o)
    {
      return o != null && Object.prototype.toString.call(o) === Util.IS_NUMBER_TO_STRING;
    },
    
    isBoolean : function(o)
    {
      return o != null && Object.prototype.toString.call(o) === Util.IS_BOOLEAN_TO_STRING;
    },
    
    isUndefined : function(o)
    {
      return typeof o === 'undefined';
    },
    
    isElement : function(o) {
      return Util.isValid(o) && o instanceof Util.GLOBAL.Element;
    },
    
    isValid : function(o)
    {
      return o != null;
    },
    
    getKeys : function(obj, hasOwnProp)
    {
      var keys = [];
      for(var i in obj)
      {
        if(!hasOwnProp || obj.hasOwnProperty(i))
        {
          keys.push(i);
        }
      }
  
      return keys;
    },
    
    getValues : function(obj, hasOwnProp)
    {
      var values = [];
      for(var i in obj)
      {
        if(!hasOwnProp || obj.hasOwnProperty(i))
        {
          values.push(obj[i]);
        }
      }
  
      return values;
    },
    
    copy : function(source, dest, hasOwnProp)
    {
      if(Util.isObject(source))
      {
        for(var i in source)
        {
          if(!hasOwnProp || source.hasOwnProperty(i))
          {
            dest[i] = source[i];
          }
        }
      }
      
      return dest;
    },
    
    generateId : function(idSize)
    {
      var result = '';
      idSize = idSize || 32;
      for(var i=0; i<idSize; i++)
      {
        result += Math.floor(Math.random()*16).toString(16);
      } 
      return result;
    },
    
    bind : function(thisRef, func)
    {
      if (!Util.isFunction(func))
      {
        throw new com.runwaysdk.Exception("Unable to bind,  the second parameter is not a function.");
      }
    
      var args = [].splice.call(arguments, 2, arguments.length);
      return function(){
        var retval = func.apply(thisRef, args.concat([].splice.call(arguments, 0, arguments.length)));
        return retval;
      };
    },
    
    curry : function(func)
    {
      var args = [].splice.call(arguments, 1, arguments.length);
      return function(){
        return func.apply(this, args.concat([].splice.call(arguments, 0, arguments.length)));
      };
    },
    
    /**
   * Extracts all script tag contents and returns a string of executable code
   * that can be evaluated.
   */
    extractScripts : function(html)
    {
      var scripts = html.match(/<script\b[^>]*>[\s\S]*?<\/script>/img);
      var executables = [];
      if(scripts != null)
      {
        for(var i=0; i<scripts.length; i++)
        {
          var scriptM = scripts[i].match(/<script\b[^>]*>([\s\S]*?)<\/script>/im);
          executables.push(scriptM[1]);
        }
      }
  
      return executables.join('');
    },
  
    /**
   * Removes all scripts from the HTML and returns a string of the processed
   * HTML.
   */
    removeScripts : function(html)
    {
      return html.replace(/<script\b[^>]*>[\s\S]*?<\/script>/img, '');
    },
    
    // TODO give credit to
    // http://blog.stevenlevithan.com/archives/faster-trim-javascript
    trim : function(str)
    {
      var str = str.replace(/^\s\s*/, '');
      var ws = /\s/;
      var i = str.length;
      while (ws.test(str.charAt(--i)));
      return str.slice(0, i + 1);
    },
    
    memoize : function(func, context)
    {
      func.memoCache = {};
    
      return function() {
        
        var args = [].splice.call(arguments, 0, arguments.length);
        if(func.memoCache[args])
        {
          return func.memoCache[args];
        }
        else
        {
          func.memoCache[args] = func.apply(context || this, args); 
          return func.memoCache[args];
        }
      };
    },
    
    debounce : function(func, threshold, context, enforceWait)
    {
      var timeout = null;
      var isExec = null;
  
      return function(){
  
        if(timeout !== null || enforceWait && isExec)
        {
          return;
        }
      
        timeout = setTimeout(function(){
          clearTimeout(timeout);
          timeout = null;          
        }, threshold || 500);
  
        isExec = true;
        func.apply(context || this, arguments);
        isExec = false;
      };
    },
    
    setISO8601 : function (date, string, ignoreTimezone)
    {
      var regexp = new RegExp(Util.ISO8601_REGEX);
  
      if(!Util.isString(string) || string === '' || !regexp.test(string))
      {
        return false;
      }
            
      var d = string.match(regexp);
  
      var offset = 0;
      var tempDate = new Date(d[1], 0, 1);
  
      if (d[2]) { tempDate.setMonth(d[2] - 1); }
      if (d[3]) { tempDate.setDate(d[3]); }
      if (d[4]) { tempDate.setHours(d[4]); }
      if (d[5]) { tempDate.setMinutes(d[5]); }
      if (d[6]) { tempDate.setSeconds(d[6]); }
      if (d[8]) {
          offset = (Number(d[8]) * 60) + Number(d[9]);
          offset *= ((d[7] == '-') ? 1 : -1);
      }
      
      var time = Number(tempDate);
  
      if(ignoreTimezone !== true)
      {
        offset -= tempDate.getTimezoneOffset();
        time += (offset * 60 * 1000);
      }
      
      date.setTime(Number(time));
      
      return true;
    },
  
    toISO8601 : function (date, ignoreTimezone)
    {
      /*
     * ISO8601 format: Complete date plus hours, minutes, seconds and a
     * decimal fraction of a second YYYY-MM-DDThh:mm:ssZ (eg
     * 1997-07-16T19:20:30.45-0100)
     */
      var format = 6;
      var offset = date.getTimezoneOffset()/60;
      
      var tempDate = date;
         
      var zeropad = function (num) {
      var value = (num < 0 ? num * -1 : num);
      
        return (value < 10 ? '0' + value : value);
      };
  
      var str = "";
  
      // Set YYYY
      str += tempDate.getFullYear();
      // Set MM
      str += "-" + zeropad(tempDate.getMonth() + 1);
      // Set DD
      str += "-" + zeropad(tempDate.getDate());
      // Set Thh:mm
      str += "T" + zeropad(tempDate.getHours()) + ":" + zeropad(tempDate.getMinutes());
      // Set ss
      str += ":" + zeropad(tempDate.getSeconds());        
      // Set TZD
      
      if(!ignoreTimezone)
      {
        str += (offset > 0 ? '-' : '+') + zeropad(offset) + '00';
      }
      
      return str;
    },
    
    /**
     * Tests the input objects for equality, calling Base.equals() if
     * one of the objects extends the com.runwaysdk.Base class. Otherwise,
     * an identity check is performed using ===. This method also treats
     * undefined and null as equal.
     */
    equals : function(obj1, obj2)
    {
      if(obj1 instanceof Base)
      {
        return obj1.equals(obj2);
      }
      else if(obj1 == null && obj2 == null) // null/undefined check
      {
        return true;
      }
      else
      {
        return obj1 === obj2;
      }
    },
    
    /**
   * This JSON object is based on the reference code provided by Douglas
   * Crockford. The original, commented source is located at
   * http://json.org/json2.js.
   */    
    JSON : (function(){
  
        var cx = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
            escapable = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
            gap,
            indent,
            meta = {    // table of character substitutions
                '\b': '\\b',
                '\t': '\\t',
                '\n': '\\n',
                '\f': '\\f',
                '\r': '\\r',
                '"' : '\\"',
                '\\': '\\\\'
            },
            rep;
  
  
        function quote(string) {
  
            escapable.lastIndex = 0;
            return escapable.test(string) ?
                '"' + string.replace(escapable, function (a) {
                    var c = meta[a];
                    return Util.isString(c) ? c :
                        '\\u' + ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
                }) + '"' :
                '"' + string + '"';
        }
  
        function f(n) {
          // Format integers to have at least two digits.
          return n < 10 ? '0' + n : n;
        }
  
        // Normally we wouldn't modify the prototype of a native object
        // but the spec defines the following behavior for Date serialization.
        if (typeof Date.prototype.toJSON !== 'function') {
  
          Date.prototype.toJSON = function (key) {
  
              return isFinite(this.valueOf()) ?
                     this.getUTCFullYear()   + '-' +
                   f(this.getUTCMonth() + 1) + '-' +
                   f(this.getUTCDate())      + 'T' +
                   f(this.getUTCHours())     + ':' +
                   f(this.getUTCMinutes())   + ':' +
                   f(this.getUTCSeconds())   + 'Z' : null;
          };
        }
  
  
        function str(key, holder) {
  
            var i,          // The loop counter.
                k,          // The member key.
                v,          // The member value.
                length,
                mind = gap,
                partial,
                value = holder[key];
  
            if(typeof value === 'function')
            {
              return undefined;
            }
            
            //var isClass = value instanceof Base;
            
            if (value && typeof value === 'object' &&
                typeof value.toJSON === 'function') {
              value = value.toJSON(key);
            }
  
            if (typeof rep === 'function') {
                value = rep.call(holder, key, value);
            }
            
            // Special case: if this is a Runway classes then return
            // because it has already been serialized.
            /*
            if(isClass)
            {
              return value;
            }
            */
            
            switch (typeof value) {
            
            case 'string':
                return quote(value);
  
            case 'number':
                return isFinite(value) ? String(value) : 'null';
  
            case 'boolean':
            case 'null':
  
                return String(value);
  
            case 'object':
  
                if (!value) {
                    return 'null';
                }
                
                gap += indent;
                partial = [];
  
                if (Util.isArray(value)) {
  
                    length = value.length;
                    for (i = 0; i < length; i += 1) {
                        partial[i] = str(i, value) || 'null';
                    }
  
                    v = partial.length === 0 ? '[]' :
                        gap ? '[\n' + gap +
                                partial.join(',\n' + gap) + '\n' +
                                    mind + ']' :
                              '[' + partial.join(',') + ']';
                    gap = mind;
                    return v;
                }
  
                if (rep && typeof rep === 'object') {
                    length = rep.length;
                    for (i = 0; i < length; i += 1) {
                        k = rep[i];
                        if (typeof k === 'string') {
                            v = str(k, value);
                            if (v) {
                                partial.push(quote(k) + (gap ? ': ' : ':') + v);
                            }
                        }
                    }
                } else {
  
                    for (k in value) {
                        if (Object.hasOwnProperty.call(value, k)) {
                            v = str(k, value);
                            if (v) {
                                partial.push(quote(k) + (gap ? ': ' : ':') + v);
                            }
                        }
                    }
                }
  
                v = partial.length === 0 ? '{}' :
                    gap ? '{\n' + gap + partial.join(',\n' + gap) + '\n' +
                            mind + '}' : '{' + partial.join(',') + '}';
                gap = mind;
                return v;
            }
        }
  
        return {
        
            stringify : function (value, replacer, space) {
  
                var i;
                gap = '';
                indent = '';
  
                if (typeof space === 'number') {
                    for (i = 0; i < space; i += 1) {
                        indent += ' ';
                    }
  
                } else if (typeof space === 'string') {
                    indent = space;
                }
  
                rep = replacer;
                if (replacer && typeof replacer !== 'function' &&
                        (typeof replacer !== 'object' ||
                         typeof replacer.length !== 'number')) {
                    throw new Exception('Util.getJSON');
                }
  
                return str('', {'': value});
            },
  
  
            parse : function (text, reviver) {
  
                var j;
  
                function walk(holder, key) {
  
                    var k, v, value = holder[key];
                    if (value && typeof value === 'object') {
                        for (k in value) {
                            if (Object.hasOwnProperty.call(value, k)) {
                                v = walk(value, k);
                                if (v !== undefined) {
                                    value[k] = v;
                                } else {
                                    delete value[k];
                                }
                            }
                        }
                    }
                    return reviver.call(holder, key, value);
                }
  
                text = String(text);
                cx.lastIndex = 0;
                if (cx.test(text)) {
                    text = text.replace(cx, function (a) {
                        return '\\u' +
                            ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
                    });
                }
  
                if (/^[\],:{}\s]*$/.
                  test(text.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, '@').
                  replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']').
                  replace(/(?:^|:|,)(?:\s*\[)+/g, ''))) {
  
                    j = eval('(' + text + ')');
  
                    return typeof reviver === 'function' ?
                        walk({'': j}, '') : j;
                }
  
                throw new Exception('Util.getObject');
            }
            
        };
  
    })(),
    
    merge : function(source, dest, hasOwnProp)
    {
      if(Util.isObject(source))
      {
        for(var i in source)
        {
          if((!hasOwnProp || source.hasOwnProperty(i)) && !(i in dest))
          {
            dest[i] = source[i];
          }
        }
      }
      
      return dest;
    },
  
    toObject : function(json, reviver)
    {
      if (Util.isString(json))
      {
        var useNativeParsing = Mojo.ClientSession.isNativeParsingEnabled();
  
        if (useNativeParsing && Mojo.SUPPORTS_NATIVE_PARSING)
        {
          return JSON.parse(json, reviver);
        }
        else
        {
          return Util.JSON.parse(json, reviver);
        }
      }
      else
      {
        return json;
      }
    },
    
    // alias for toObject()
    getObject : function(json, reviver)
    {
      return Util.toObject(json, reviver);
    },
  
    toJSON : function(obj, replacer)
    {
      var useNativeParsing = Mojo.ClientSession.isNativeParsingEnabled();
      
      // Use the browser's toJSON if it exists
      if (useNativeParsing && Mojo.SUPPORTS_NATIVE_PARSING)
      {
        return JSON.stringify(obj, replacer); 
      }
      else
      {
        // Otherwise use Runway's
        return Util.JSON.stringify(obj, replacer);
      }      
    },
    
    // alias for toJSON()
    getJSON : function(obj, replacer)
    {
      return Util.toJSON(obj, replacer);
    },
    
    
    // FIXME : this doesn't belong here
    collectFormValues : function(formId)
    {
      var keyValues = {};
      function collect(elements)
      {
        for(var i=0; i<elements.length; i++)
        {
          var el = elements[i];
          if(el.disabled)
          {
            continue;
          }
  
          var name = el.name;
  
          var nodeName = el.nodeName.toLowerCase();
          switch(nodeName)
          {
            case 'select':
              var values = [];
              var options = el.options;
              for(var j=0; j<options.length; j++)
              {
                var option = options[j];
                if(option.selected)
                  values.push(option.value);
              }
              keyValues[name] = values;
              break;
            case 'textarea':
              keyValues[name] = el.value;
              break;
            case 'input':
              var type = el.type.toLowerCase();
              switch(type)
              {
                case 'radio':
                  if(el.checked)
                    keyValues[name] = el.value;
                  break;
                case 'checkbox':
                  if(!keyValues[name])
                    keyValues[name] = [];
  
                  if(el.checked)
                    keyValues[name].push(el.value);
                  break;
                default:
                  keyValues[name] = el.value;
              }
              break;
          }
        }
      }
  
      var form = Util.isString(formId) ? document.getElementById(formId) : formId;
      collect(form.getElementsByTagName('input'));
      collect(form.getElementsByTagName('select'));
      collect(form.getElementsByTagName('textarea'));
      
      // FIXME use form.elements[] instead and remove inner function
  
      return keyValues;
    },
  
    convertMapToQueryString : function(map)
    {
      if(map == null)
      {
        return '';
      }
  
      var params = [];
      for(var key in map)
      {
        var entry = map[key];
        if(Util.isArray(entry))
        {
          for(var i=0; i<entry.length; i++)
          {
            params.push(encodeURIComponent(key) + "[]=" + encodeURIComponent(entry[i]));
          }
        }
        else
        {
          params.push(encodeURIComponent(key) + "=" + encodeURIComponent(entry));
        }
      }
  
      var queryString = params.join("&");
      return queryString;
    },
    
    requireParameter : function(name, value) {
      if (value == null || value == undefined) {
        var ex = new com.runwaysdk.Exception("Parameter [" + name + "] is required.");
        this.__handleException(ex);
      }
    }
  };
  
  return Util;
})();
