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
 * RunwaySDK Javascript Core library.
 * 
 * @author Terraframe
 */

//define(["./log4js", "./ClassFramework", "./Util", "./Structure", "./RunwaySDK_Inspector"], function(Log4js, ClassFramework, Util, Structure){
(function(){
  
  var ClassFramework = Mojo.Meta;
  var Util = Mojo.Util;
  var Structure = Mojo.Meta.alias(Mojo.STRUCTURE_PACKAGE + "*");
  var Exception = com.runwaysdk.Exception;

  var Base = ClassFramework.getBaseClass();
  
  var FeatureSet = Mojo.Meta.newClass(Mojo.ROOT_PACKAGE+'FeatureSet', {
    IsSingleton : true,
    Instance : {
      initialize : function()
      {
        var impl = Mojo.GLOBAL.document.implementation;
        this._dom2Events = impl.hasFeature('Events', '2.0');
        this._dom3Events = impl.hasFeature('Events', '3.0');
        
        if(!this._dom2Events && !this._dom3Events)
        {
          throw new Exception('Neither DOM Level 2 nor 3 Events are supported.');
        }
      },
      supportsDOM2Events : function() { return this._dom2Events; },
      supportsDOM3Events : function() { return this._dom3Events; }
    }
  });

  /**
   * All classes that extend Base implement this Interface such that they can
   * register event listeners and dispatch events.
   */
  var EventTarget = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'EventTarget', {
    Instance : {
      addEventListener : function(type, listener, obj, context, capture){},
      removeEventListener : function(type, listener, useCapture){},
      dispatchEvent : function(evt){},
      removeEventListeners : function(target, type){},
      removeAllEventListeners : function(target){},
      hasEventListener : function(target, type, listener, useCapture){}
    }
  });

  // Manually add the EventTarget interface to Base, which must take place
  // after the initial bootstrapping (since EventTarget extends Base). This
  // breaks encapsulation and should only be done internally.
  Base.getMetaClass()._interfaces.push(EventTarget);
  Base.getMetaClass()._enforceInterfaceMethods();
  
// FIXME iterate over different object types
// Look at prototype 1.6 and
// http://closure-library.googlecode.com/svn/trunk/closure/goog/docs/closure_goog_iter_iter.js.source.html
// for more iter methods and integrated iteration
Mojo.Meta.newClass('Mojo.Iter', {

  Instance : {
  
    initialize : function()
    {
      // todo take in arr/obj/iterable and wrap it to allow iteration functions
    }
  },
  
  Static : {
  
    filter : function(obj, func, context)
    {
      var bound = Mojo.Util.bind((context || this), func);
      var filtered = [];
      Mojo.Iter.forEach(obj, function(item, ind){
        
        if(bound(item, ind))
        {
          filtered.push(item);
        }
      });
      
      return filtered;
    },
    
    forEach : function(obj, func, context)
    {
      var bound = Mojo.Util.bind((context || this), func);
      if(Mojo.Util.isNumber(obj))
      {
        for(var i=0; i<obj; i++)
        {
          bound(i); 
        }
      }
      else if(Mojo.Util.isArray(obj))
      {
        for(var i=0; i<obj.length; i++)
        {
          var item = obj[i];
          bound(item, i);
        }
      }
      else if(Mojo.Util.isObject(obj))
      {
        var keys = Mojo.Util.getKeys(obj);
        for(var i=0; i<keys.length; i++)
        {
          var key = keys[i];
          bound(obj[key], key);
        }
      }
      else if('length' in obj)
      {
        for(var i=0; i<obj.length; i++)
        {
          bound(obj[i], i);
        }
      }
      else
      {
        throw Error('The object cannot be iterated over.');
      }
    },
    
    map : function(obj, func, context)
    {
      var bound = Mojo.Util.bind((context || this), func);
      var mapped = [];
      Mojo.Iter.forEach(obj, function(item, ind){
        
        mapped.push(bound(item, ind));
        
      });
      
      return mapped;
    }
  }
});

/**
 * Class that serializes basic objects with optional key/value overriding.
 * Functions and the __context variables are ignored.
 */
var StandardSerializer = Mojo.Meta.newClass(Mojo.ROOT_PACKAGE+'StandardSerializer',{
  
  Instance : {
    initialize : function(source, override)
    {
      source = source || {};
      this._destination = {};
      
      // Copy the non-function properties to the destination object.
      // This will also remove any infinite recursion via toJSON()
      // declarations on the original source object.
      for(var i in source)
      {
        if(!Mojo.Util.isFunction(source[i]) && i !== "__context")
        {
          this._destination[i] = source[i];
        }
      }
      this._override = override || null;
    },
    toJSON : function(key)
    {
      var ssRef = this;
      var replacer = function(key, value)
      {
        if(ssRef._override !== null && key in ssRef._override)
        {
          return ssRef._override[key];
        }
        else
        {
          return value;
        }
      };
      
      if (key == null) {
        return Mojo.Util.toJSON(this._destination, replacer);
      }
      else {
        return this._destination;
      }
    }
  }
});

var EventListener = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'EventListener', {
  Instance : {
    handleEvent : function(evt){}
  }
});

var EventException = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'EventException', {
  Extends : Mojo.ROOT_PACKAGE+'Exception',
  Constants : {
    UNSPECIFIED_EVENT_TYPE_ERR : 0
  },
  Instance : {
    initialize : function(code, message)
    {
      this.$initialize(message);
      this._code = code;
    },
    getCode : function() { return this._code; }
  }
});

var EventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'EventIF', {
  Constants : {
    CAPTURING_PHASE : 1,
    AT_TARGET : 2,
    BUBBLING_PHASE: 3
  },
  Instance : {
    getType : function(){},
    getTarget : function(){},
    getCurrentTarget : function(){},
    getEventPhase : function(){},
    getBubbles : function(){},
    getCancelable : function(){},
    getTimeStamp : function(){},
    stopPropagation : function(){},
    stopImmediatePropagation : function(){},
    preventDefault : function(){},
    getPreventDefault : function(){},
    initEvent : function(eventType, canBubble, cancelable){}
  }
});

var CustomEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'CustomEventIF', {
  Instance : {
    getDetail : function(){},
    initCustomEvent : function(eventType, canBubble, cancelable, detail){}
  }
});

var HTMLEventsIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'HTMLEventsIF', {
  Extends : EventIF,
  Instance : {

  }
});

var UIEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'UIEventIF', {
  Extends : EventIF,
  Instance : {
    getView : function(){},
    getDetail : function(){},
    initUIEvent : function(type, canBubble, cancelable, view, detail){}
  }
});

var FocusEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'FocusEventIF', {
  Extends : UIEventIF,
  Instance : {
    getRelatedTarget : function(){},
    initFocusEvent : function(eventType, canBubble, cancelable, view, detail, relatedTarget){}
  }
});

var MouseEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'MouseEventIF', {
  Extends : UIEventIF,
  Instance : {
    getScreenX : function(){},
    getScreenY : function(){},
    getClientX : function(){},
    getClientY : function(){},
    getCtrlKey : function(){},
    getShiftKey : function(){},
    getAltKey : function(){},
    getMetaKey : function(){},
    getButton : function(){},
    getButtons : function(){},
    getRelatedTarget : function(){},
    getModifierState : function(keyArg){},
    initMouseEvent : function(type, canBubble, cancelable, view, detail, 
      screenX, screenY, clientX, clientY, ctrlKey, 
      altKey, shiftKey, metaKey, button, relatedTarget){}
  }
});

var DragEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'DragEventIF', {
  Extends : MouseEventIF,
  Instance : {
    getDataTransfer : function(){},
    initDragEvent : function(typeArg, canBubbleArg, cancelableArg, dummyArg, detailArg, screenXArg, screenYArg, 
      clientXArg, clientYArg, ctrlKeyArg, altKeyArg, shiftKeyArg, metaKeyArg, buttonArg, relatedTargetArg, dataTransferArg){}
  }
});

var TextEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'TextEventIF', {
  Extends : UIEventIF,
  Constants : {
    DOM_INPUT_METHOD_UNKNOWN       : 0x00,
    DOM_INPUT_METHOD_KEYBOARD      : 0x01,
    DOM_INPUT_METHOD_PASTE         : 0x02,
    DOM_INPUT_METHOD_DROP          : 0x03,
    DOM_INPUT_METHOD_IME           : 0x04,
    DOM_INPUT_METHOD_OPTION        : 0x05,
    DOM_INPUT_METHOD_HANDWRITING   : 0x06,
    DOM_INPUT_METHOD_VOICE         : 0x07,
    DOM_INPUT_METHOD_MULTIMODAL    : 0x08,
    DOM_INPUT_METHOD_SCRIPT        : 0x09
  },
  Instance : {
    getData : function(){},
    getInputMethod : function(){},
    getLocale : function(){},
    initTextEvent : function(type, canBubble, cancelable, view, data, input, locale){}
  }
});

var WheelEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'WheelEventIF', {
  Extends : MouseEventIF,
  Constants : {
    DOM_DELTA_PIXEL : 0x01,
    DOM_DELTA_LINE : 0x02,
    DOM_DELTA_PAGE: 0x03
  },
  Instance : {
    getDeltaX : function(){},
    getDeltaY : function(){},
    getDeltaZ : function(){},
    getDeltaMode : function(){},
    initWheelEvent : function(type, canBubble, cancelable, view, detail, 
      screenX, screenY, clientX, clientY, ctrlKey, 
      altKey, shiftKey, metaKey, button, relatedTarget, modifierList, deltaX, deltaY, deltaZ, deltaMode){}
  }
});

var KeyboardEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'KeyboardEventIF', {
  Extends : UIEventIF,
  Constants : {
    DOM_KEY_LOCATION_STANDARD : 0x00,
    DOM_KEY_LOCATION_LEFT : 0x01,
    DOM_KEY_LOCATION_RIGHT : 0x02,
    DOM_KEY_LOCATION_NUMPAD : 0x03,
    DOM_KEY_LOCATION_MOBILE : 0x04,
    DOM_KEY_LOCATION_JOYSTICK : 0x05
  },
  Instance : {
    getChar : function(){},
    getKey : function(){},
    getKeyCode : function(){},
    getLocation : function(){},
    getCtrlKey : function(){},
    getShiftKey : function(){},
    getAltKey : function(){},
    getMetaKey : function(){},
    getRepeat : function(){},
    getModifierState : function(key){},
    getLocale : function(){},
    initKeyboardEvent : function(type, canBubble, cancelable, view, key, locationArg, modifiersList, repeat, locale){}
  }
});

var CompositionEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'CompositionEventIF', {
  Extends : UIEventIF,
  Instance : {
    getData : function(){},
    getLocale : function(){},
    initUIEvent : function(type, canBubble, cancelable, view, data, locale){}
  }
});

var DocumentEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'DocumentEventIF', {
  Instance : {
    createEvent : function(eventType){},
    canDispatch : function(namespaceURI, eventType){}
  }
});

var DocumentEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'DocumentEvent', {
  Implements: DocumentEventIF,
  IsSingleton: true,
  Instance: {
    initialize : function()
    {
    },
    createEvent : function(eventType)
    {
      eventType = Mojo.Util.isFunction(eventType) ? eventType.getMetaClass().getQualifiedName() : eventType;

      var event = null;
      // look for a DOM type match and use an Event wrapper
      var eventDef = EventUtil.DOM_EVENTS[eventType];
      if(eventDef)
      {
        event = new eventDef.eventInterface(eventType);
      }
      // look for a custom event
      else if(Mojo.Meta.classExists(eventType))
      {
        event = Mojo.Meta.newInstance(eventType);
      }
      else
      {
        throw new Mojo.$.com.runwaysdk.Exception("The event [" + eventType + "] could not be created because it is not a recognized DOM event.");
      }

      return event;
    },
    canDispatch : function(namespaceURI, eventType)
    {
      // TODO implement
    }
  }
});

/**
 * Marker interface that an event must implement if it wants to allow
 * the global registration of listeners.
 */
var GlobalEvent = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'GlobalEvent', {});

var AbstractEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'AbstractEvent', {
  IsAbstract: true,
  Implements: EventIF,
  Instance : {
    initialize : function()
    {
      this.$initialize.apply(this);
      this._preventDefault = false;
    },
    preventDefault : function()
    {
      if(this.getCancelable())
      {
        this._preventDefault = true;
      } 
    },
    getPreventDefault : function()
    {
      return this._preventDefault;
    },
  }
});

// FIXME publish event variables to adhere to the spec
var Event = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'Event', {
  Extends: AbstractEvent,
  Instance : {
    initialize : function(evt)
    {
      this.$initialize();
      
      // Wrap a new event object or an existing one passed into this constructor
      if(Mojo.Util.isString(evt))
      {
        if (document.createEvent) // W3C compatible browsers
        {
          var eventInterface = this.getEventInterface();
          this._evt = document.createEvent(eventInterface);
        }
        else if (document.createEventObject) // IE family
        {
          this._evt = document.createEventObject();
          this._evt.type = evt;
        }
      }
      else if (evt == null) {
        this._evt = window.event;
      }
      else
      {
        this._evt = evt;
      }
    },
    getEventInterface : function()
    {
      return 'Event';
    },
    getEvent : function()
    {
      return this._evt;
    },
    getType : function()
    {
      return this._evt.type;
    },
    getTarget : function()
    {
      var target = this._evt.target;
      
      if (target != null && target.___runwaysdk_wrapper != null) {
        return target.___runwaysdk_wrapper;
      }
      
      return target;
    },
    getCurrentTarget : function()
    {
      var target = this._evt.currentTarget;
      
      if (target != null && target.___runwaysdk_wrapper != null) {
        return target.___runwaysdk_wrapper;
      }
      
      return target;
    },
    getEventPhase : function()
    {
      return this._evt.eventPhase;    
    },
    getBubbles : function()
    {
      return this._evt.bubbles;    
    },
    getCancelable : function()
    {
      return this._evt.cancelable;    
    },
    getTimeStamp : function()
    {
      return this._evt.timeStamp;    
    },
    stopPropagation : function()
    {
      this._evt.stopPropagation();    
    },
    stopImmediatePropagation : function()
    {
      // FIXME check for support before invoking
      this._evt.stopImmediatePropagation();
    },
    preventDefault : function()
    {
      this.$preventDefault();
      this._evt.preventDefault();    
    },
    
    initEvent : function(eventType, canBubble, cancelable)
    {
      this._evt.initEvent(eventType, canBubble, cancelable);
    }
  }
});

var HTMLEvents = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'HTMLEvents', {
  Extends : Event,
  Implements: HTMLEventsIF,
  Instance : {
    initialize : function(event)
    {
      this.$initialize(event);
    }, 
    getEventInterface : function()
    {
      return 'HTMLEvents';
    },
    initEvent : function(type, canBubble, cancelable)
    {
      this.$initEvent.apply(this, arguments);
    },
  }
});

var UIEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'UIEvent', {
  Extends: Event,
  Implements: UIEventIF,
  Instance : {
    initialize : function(event)
    {
      this.$initialize(event);
    },
    getEventInterface : function()
    {
      return 'UIEvent';
    },
    getView : function()
    {
      return this.getEvent().view;
    },
    getDetail : function()
    {
      return this.getEvent().detail;
    },
    initEvent : function(type, canBubble, cancelable)
    {
      this.initUIEvent.apply(this, arguments);
    },
    initUIEvent : function(type, canBubble, cancelable, view, detail)
    {
      this.getEvent().initUIEvent(type, canBubble, cancelable, view, detail);
    }
  }
});

var TextEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'TextEvent', {
  Extends : UIEvent,
  Implements : TextEventIF,
  Instance : {
    initialize : function(event)
    {
      this.$initialize(event);
    },
    getEventInterface : function()
    {
      return 'TextEvent';
    },
    getData : function()
    {
      return this.getEvent().data;
    },
    getInputMethod : function()
    {
      return this.getEvent().inputMethod;
    },
    getLocale : function()
    {
      return this.getEvent().locale;
    },
    initTextEvent : function(type, canBubble, cancelable, view, data, input, locale)
    {
      this.getEvent().initTextEvent(type, canBubble, cancelable, view, data, input, locale);
    }
  }
});

var CompositionEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'CompositionEvent', {
  Extends : UIEvent,
  Implements : CompositionEventIF,
  Instance : {
    initialize : function(event)
    {
      this.$initialize(event);
    },
    getEventInterface : function()
    {
      return 'CompositionEvent';
    },
    getData : function()
    {
      return this.getEvent().data;
    },
    getLocale : function()
    {
      return this.getEvent().locale;
    },
    initCompositionEvent : function(type, canBubble, cancelable, view, data, locale)
    {
      this.getEvent().initCompositionEvent(type, canBubble, cancelable, view, data, locale);
    }
  }
});

var FocusEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'FocusEvent', {
  Extends : UIEvent,
  Implements : FocusEventIF,
  Instance : {
    initialize : function(event)
    {
      this.$initialize(event);
    },
    getEventInterface : function()
    {
      return FeatureSet.getInstance().supportsDOM3Events() ? 
        'FocusEvent' : this.$getEventInterface();
    },
    getRelatedTarget : function()
    {
      return this.getEvent().relatedTarget;
    },
    initFocusEvent : function(eventType, canBubble, cancelable, view, detail, relatedTarget)
    {
      this.getEvent().initFocusEvent(eventType, canBubble, cancelable, view, detail, relatedTarget);
    }
  }
});

var MouseEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'MouseEvent', {
  Extends: UIEvent,
  Implements: MouseEventIF,
  Instance : {
    initialize : function(event)
    {
      this.$initialize(event);
    },
    getEventInterface : function()
    {
      return 'MouseEvent';
    },
    getScreenX : function()
    {
      return this.getEvent().screenX;
    },
    getScreenY : function()
    {
      return this.getEvent().screenY;
    },
    getClientX : function()
    {
      return this.getEvent().clientX;
    },
    getClientY : function()
    {
      return this.getEvent().clientY;
    },
    getCtrlKey : function()
    {
      return this.getEvent().ctrlKey;
    },
    getShiftKey : function()
    {
      return this.getEvent().shiftKey;
    },
    getAltKey : function()
    {
      return this.getEvent().altKey;
    },
    getMetaKey : function()
    {
      return this.getEvent().metaKey;
    },
    getButton : function()
    {
      return this.getEvent().button;
    },
    getButtons : function()
    {
      return this.getEvent().buttons;
    },
    getModifierState : function(keyArg)
    {
      // FIXME DOM3 only
      return this.getEvent().getModifierState(keyArg);
    },
    getRelatedTarget : function()
    {
      return this.getEvent().relatedTarget;
    },
    initEvent : function(type, canBubble, cancelable)
    {
      this.initMouseEvent.apply(this, arguments);
    },
    initMouseEvent : function(type, canBubble, cancelable, view, detail, 
      screenX, screenY, clientX, clientY, ctrlKey, 
      altKey, shiftKey, metaKey, button, relatedTarget)
    {
      this.getEvent().initMouseEvent(type, canBubble, cancelable, view, detail, 
      screenX, screenY, clientX, clientY, ctrlKey, 
      altKey, shiftKey, metaKey, button, relatedTarget);
    },
    isRightClick : function() {
      var isRightMB;
      var e = this.getEvent();

      if ("which" in e)  // Gecko (Firefox), WebKit (Safari/Chrome) & Opera
          isRightMB = e.which == 3; 
      else if ("button" in e)  // IE, Opera 
          isRightMB = e.button == 2;
      
      return isRightMB;
    }
  }
});

var DragEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'DragEvent', {
  Extends : MouseEvent,
  Implements : DragEventIF,
  Instance : {
    initialize : function(event)
    {
      this.$initialize(event);
    },
    getDataTransfer : function()
    {
      return this.getEvent().dataTransfer; // TODO normalize into wrapper class
    },
    initDragEvent : function(typeArg, canBubbleArg, cancelableArg, dummyArg, detailArg, screenXArg, screenYArg, 
      clientXArg, clientYArg, ctrlKeyArg, altKeyArg, shiftKeyArg, metaKeyArg, buttonArg, relatedTargetArg, dataTransferArg){
      this.getEvent().initDragEvent(typeArg, canBubbleArg, cancelableArg, dummyArg, detailArg, screenXArg, screenYArg, 
      clientXArg, clientYArg, ctrlKeyArg, altKeyArg, shiftKeyArg, metaKeyArg, buttonArg, relatedTargetArg, dataTransferArg);
    }  
  }
});

var WheelEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'WheelEvent', {
  Extends : MouseEvent,
  Implements : WheelEventIF,
  Instance : {
    initialize : function(event)
    {
      this.$initialize(event);
    },
    getEventInterface : function()
    {
      return 'WheelEvent';
    },
    getDeltaX : function()
    {
      return this.deltaX;
    },
    getDeltaY : function()
    {
      return this.deltaY;
    },
    getDeltaZ : function()
    {
      return this.deltaZ;
    },
    getDeltaMode : function()
    {
      return this.deltaMode;
    },
    initWheelEvent : function(type, canBubble, cancelable, view, detail, 
      screenX, screenY, clientX, clientY, ctrlKey, 
      altKey, shiftKey, metaKey, button, relatedTarget, modifierList, deltaX, deltaY, deltaZ, deltaMode)
    {
      this.initWheelEvent(type, canBubble, cancelable, view, detail, 
      screenX, screenY, clientX, clientY, ctrlKey, 
      altKey, shiftKey, metaKey, button, relatedTarget, modifierList, deltaX, deltaY, deltaZ, deltaMode);
    }
  }
});

var KeyboardEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'KeyboardEvent', {
  Extends: UIEvent,
  Implements: KeyboardEventIF,
  Instance : {
    initialize : function(event)
    {
      this.$initialize(event);
    },
    getEventInterface : function()
    {
      return FeatureSet.getInstance().supportsDOM3Events() ? 
        'KeyboardEvent' : this.$getEventInterface();
    },
    getChar : function()
    {
      return this.getEvent()['char'];
    },
    getKey : function()
    {
      return this.getEvent().key;
    },
    getKeyCode : function()
    {
      return this.getEvent().keyCode;
    },
    getLocation : function()
    {
      return this.getEvent().location;
    },
    getCtrlKey : function()
    {
      return this.getEvent().ctrlKey;
    },
    getShiftKey : function()
    {
      return this.getEvent().shiftKey;
    },
    getAltKey : function()
    {
      return this.getEvent().altKey;
    },
    getMetaKey : function()
    {
      return this.getEvent().metaKey;
    },
    getRepeat : function()
    {
      return this.getEvent().repeat;
    },
    getModifierState : function(key)
    {
      return this.getEvent().modifierState;
    },
    getLocale : function()
    {
      return this.getEvent().locale;
    },
    initEvent : function(type, canBubble, cancelable)
    {
      this.initKeyboardEvent.apply(this, arguments);
    },
    initKeyboardEvent : function(type, canBubble, cancelable, view, key, locationArg, modifiersList, repeat)
    {
      this.getEvent().initKeyboardEvent(type, canBubble, cancelable, view, key, locationArg, modifiersList, repeat);
    }
  }
});

var CustomEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'CustomEvent', {
  Extends: AbstractEvent,
  Implements: CustomEventIF,
  IsAbstract : true,
  Instance : {
    initialize : function(config)
    {
      this.$initialize();
      config = config || {};
      
      /*
       * These fields are defined as public in the event specification, but it
       * is recommended that the accessor methods are invoked instead.
       */
      this.type = this.getMetaClass().getQualifiedName();
      this.target = null;
      this.currentTarget = null;
      this.bubbles = config.bubbles || true;
      this.cancelable = config.cancelable || true;
      this.timeStamp = new Date().getTime();
      this.eventPhase = EventIF.AT_TARGET;
      this._preventDefault = false;
      this._detail = null;
      this._stopped = false;
      this._immediateStopped = false;
    },
    getEventInterface : function()
    {
      return 'CustomEvent';
    },
    defaultAction : function()
    {
      // Do nothing as the default implementation. Subclasses may override this.
    },
    getType : function()
    {
      return this.type;
    },
    getTarget : function()
    {
      return this.target;
    },
    _setTarget : function(target)
    {
      this.target = target;
    },
    _setCurrentTarget : function(currentTarget)
    {
      this.currentTarget = currentTarget;
    },
    getCurrentTarget : function()
    {
      return this.currentTarget;
    },
    _setEventPhase : function(phase)
    {
      this.eventPhase = phase;
    },
    getEventPhase : function()
    {
      return this.eventPhase;   
    },
    getBubbles : function()
    {
      return this.bubbles; 
    },
    getCancelable : function()
    {
      return this.cancelable; 
    },
    getTimeStamp : function()
    {
      return this.timeStamp;  
    },
    stopPropagation : function()
    {
      this._stopped = true;
    },
    getStopPropagation : function()
    {
      return this._stopped;
    },
    stopImmediatePropagation : function()
    {
      this._immediateStopped = true;
    },
    getStopImmediatePropagation : function()
    {
      return this._immediateStopped;
    },
    initEvent : function(eventType, canBubble, cancelable)
    {
      var type = Mojo.Util.isFunction(eventType) ? eventType.getMetaClass().getQualifiedName() : eventType;
      if(type !== this.type)
      {
        throw new Exception('Cannot change the type of a custom error from ['+this.type+'] to ['+type+']');
      }
      
      this.bubbles = canBubble;
      this.cancelable = cancelable;
    },
    initCustomEvent : function(eventType, canBubble, cancelable, detail)
    {
      this.initEvent(eventType, canBubble, cancelable);
      this._detail = detail;
    },
    getDetail : function()
    {
      return this._detail;
    }
  }
});

var EventUtil = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'EventUtil', {

  IsAbstract : true,

  Static : {
    
    addEventListener: function(oTarget, sEvent, oListener, bUseCapture)
    {
      oTarget = this._validateTarget(oTarget);
    
      if (oTarget.addEventListener)
      {
        oTarget.addEventListener(sEvent, oListener, bUseCapture);
      }
      else if (oTarget.attachEvent) // IE
      {
        // Make it so that the handler is passed an event object with a proper event.target
        var wrapper = function() { var event = window.event; event.target = window.event.srcElement; oListener(event); };
        oTarget.attachEvent("on" + sEvent, wrapper);
      }
      else
      {
        oTarget["on" + sEvent] = oListener;
      }
    },
    
    removeEventListener: function(oTarget, sEvent, oListener, bUseCapture)
    {
      oTarget = this._validateTarget(oTarget);
    
      if (oTarget.removeEventListener)
      {
        oTarget.removeEventListener(sEvent, oListener, bUseCapture);
      }
      else if (oTarget.detachEvent) // IE
      {
        oTarget.detachEvent(sEvent, oListener);
      }
      else
      {
        oTarget["on" + sEvent] = null;
      }
    },
    
    /**
     * Factory method to create any type of event.
     */
    createEvent: function(eventType)
    {
      props = props || {};
      
      var event = null;
      
      // look for a DOM type match and use an Event wrapper to initialize the properties
      var eventDef = this.EVENTS[eventType];
      if(eventDef)
      {
        if (document.createEvent) // W3C compatible browsers
        {
          event = document.createEvent(eventDef.eventGroup);
        }
        else if (document.createEventObject) // IE family
        {
          event = document.createEventObject();
        }
      }
      else
      {
        throw new Mojo.$.com.runwaysdk.Exception("The event [" + eventType + "] could not be created because it is not a recognized DOM event.");
      }

      return event;
    },
    
    dispatchEvent: function(target, event)
    {
      target = this._validateTarget(target);
    
      if (target === window && !Mojo.Util.isFunction(target.dispatchEvent) && Mojo.Util.isFunction(document.dispatchEvent))
      {
        // Safari3 doesn't have window.dispatchEvent()
        target = document;
      }
      else if (target === document && !Mojo.Util.isFunction(document.documentElement.fireEvent))
      {
        // IE6,IE7 thinks window==document and doesn't have window.fireEvent()
        // IE6,IE7 cannot properly call document.fireEvent()
        target = document.documentElement;
      }
    
      if (Mojo.Util.isFunction(target.dispatchEvent)) // W3C compatible browsers
      {
        return target.dispatchEvent(event);
      }
      else if (Mojo.Util.isFunction(target.fireEvent)) // IE family
      {
        return target.fireEvent("on" + event.type, event);
      }
      else
      {
        throw new Mojo.$.com.runwaysdk.Exception("Unable to dispatch event [" + event + "] on target [" + target +
          "], no usable methods were found on the target to dispatch the event. (Either you are using some unsupported browser, or the target does not support events)");
      }
    },
    
    _validateTarget: function(target)
    {
      target = Mojo.Util.isString(target) ? document.getElementById(target) : target;
      if(target == null)
      {
        throw new Mojo.$.com.runwaysdk.Exception("Mojo.Event.addListener: Unable to find a target by the id of '" + sOldTarget + "'.");
      }

      return target;
    },
    
    DEPRECATED_EVENTS : {
      // TODO should we white or black list deprecation?
    },
    
    // FIXME add default args ... maybe use LinkedHashMap for ordering and overrides
    DOM_EVENTS : {
    
      // FormEvent
      change : {eventInterface : HTMLEvents},
    
      // UIEvent
      abort : {eventInterface : UIEvent},
      load : {eventInterface : UIEvent},
      unload : {eventInterface : UIEvent},
      select : {eventInterface : UIEvent},
      error : {eventInterface : UIEvent},
      resize : {eventInterface : UIEvent},
      scroll : {eventInterface : UIEvent},
      
      // FocusEvent
      blur : {eventInterface : FocusEvent},
      focus : {eventInterface : FocusEvent},
      focusin : {eventInterface : FocusEvent},
      focusout : {eventInterface : FocusEvent},
      
      // MouseEvent
      click : {eventInterface : MouseEvent},
      dblclick : {eventInterface : MouseEvent},
      mousedown : {eventInterface : MouseEvent},
      mouseenter : {eventInterface : MouseEvent},
      mouseleave : {eventInterface : MouseEvent},
      mousemove : {eventInterface : MouseEvent},
      mouseout : {eventInterface : MouseEvent},
      mouseover : {eventInterface : MouseEvent},
      mouseup : {eventInterface : MouseEvent},
      contextmenu : {eventInterface : MouseEvent},
      
      // WheelEvent
      wheel : {eventInterface : WheelEvent},
      
      // DragEvent
      dragstart : {eventInterface : DragEvent},
      drag : {eventInterface : DragEvent},
      dragend : {eventInterface : DragEvent},
      dragenter : {eventInterface : DragEvent},
      dragover : {eventInterface : DragEvent},
      dragleave : {eventInterface : DragEvent},
      drop : {eventInterface : DragEvent},
      
      // KeyboardEvent
      keydown : {eventInterface : KeyboardEvent},
      keyup : {eventInterface : KeyboardEvent},
      keypress : {eventInterface : KeyboardEvent}, // FIXME deprecated in DOM3 (use textInput)
      
      // TextEvent
      textinput : {eventInterface : TextEvent},
      
      // CompositionEvent
      compositionstart : {eventInterface : CompositionEvent},
      compositionend : {eventInterface : CompositionEvent},
      compositionupdate : {eventInterface : CompositionEvent}
    }
  }
});

/**
 * Wrapper class to store the data for an instance of an EventListener
 */
var ListenerEntry = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'ListenerEntry', {
  Instance : {
    initialize : function(type, listener, wrapper, obj, context, capture)
    {
      this._type = type;
      this._listener = listener;
      this._wrapper = wrapper;
      this._object = obj;
      this._context = context;
      this._capture = capture;
    },
    getType : function() { return this._type; },
    getListener : function() { return this._listener; },
    getWrapper : function() { return this._wrapper; },
    getObject : function() { return this._object; },
    getContext : function() { return this._context; },
    captures : function() { return this._capture; },
    matches : function(type, listener, capture)
    {
      return this._type === type && this._listener === listener && this._capture === capture;
    }
  }
});

var Registry = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'Registry', {
  IsSingleton : true,
  Instance : {
    initialize : function()
    {
      this._listeners = new Structure.HashMap();
      this._globalListeners = new Structure.HashMap();
      this._domPrefix = /^dom.*/i;
    },
    
    isDispatching : function()
    {
      return this._dispatching;
    },
    
    dispatchEvent : function(evt)
    {
      try
      {
        var listeners = this._listeners.get(evt.getCurrentTarget());
        if(listeners === null)
        {
          return; // no listeners for the current event target
        }
        
        for(var i=0, len=listeners.length; i<len; i++)
        {
          if(evt.getStopImmediatePropagation())
          {
            return;
          }
          
          // FIXME match on event phase
          var listener = listeners[i];
          if(listener.getType() === evt.getType())
          {
            var wrapper = listener.getWrapper();
            var context = listener.getContext() || evt.getCurrentTarget();
            var obj = listener.getObject();
            var handler = Mojo.Util.isObject(wrapper) ? wrapper.handleEvent : wrapper;
            
            try
            {
              var args = [evt];
              if(!Mojo.Util.isUndefined(obj))
              {
                args.push(obj);
              }
              
              handler.apply(context, args);
            }
            catch(e)
            {
              if ( !(e instanceof com.runwaysdk.Exception) ) {
                e = new com.runwaysdk.Exception(e);
              }
              
              // Invoke an error handler if one exists, but an error within any event listener
              // SHOULD NOT disrupt futher listener processing according to the spec.
              // FIXME wrap with EventException and store event as instance var
              if(Mojo.Util.isFunction(wrapper.handleError))
              {
                wrapper.handleError.call(context, e);
              }
            }
          }
        }
      }
      catch (e)
      {
        // FIXME allow for Event-level error handling
        //http://www.w3.org/TR/DOM-Level-2-Events/events.html#Events-EventException
        //http://www.findmeat.org/tutorials/javascript/x946556.htm
      }
    },
    
    _wrapDOMListener : function(type, listener, obj, context, capture)
    {
      return Mojo.Util.bind(context, function(evt){
          
          // FIXME normalize
          var eventInterface = EventUtil.DOM_EVENTS[evt.type].eventInterface;
          var event = new eventInterface(evt);
          
          var retval = true;
          if (Mojo.Util.isObject(listener)) 
          {
           retval = listener.handleEvent.call(context, event, obj);
          }
          else 
          {
            retval = listener.call(this, event, obj);
          }
          
          if ( retval !== true && retval !== undefined ) {
            if ( (event.result = retval) === false ) {
              event.preventDefault();
              event.stopPropagation();
            }
          }
        });
    },
    
    addEventListener : function(target, type, listener, obj, context, capture)
    {
      if (type == null) {
        throw new com.runwaysdk.Exception("Unable to listen to a null event.");
      }
      
      if(Mojo.Util.isObject(listener) && Mojo.Util.isFunction(listener.handleEvent))
      {
        if(context && context !== listener)
        {
          var msg = 'An event listener object cannot have a different context than the listener itself.';
          throw new Mojo.$.com.runwaysdk.Exception(msg);
        }
        else
        {
          context = listener;
        }
      }
      else if(!Mojo.Util.isFunction(listener))
      {
        var msg = 'An event listener must either be an object, instance of ['+EventListener.getMetaClass().getQualifiedName()+'], or object that defines a handleEvent(evt) function.';
        throw new Mojo.$.com.runwaysdk.Exception(msg);
      }
      
      // DOM and custom events are wrapped differently
      var wrapper;
      if(Mojo.Meta.classExists(type))
      {
        // The spec states that custom events cannot start with the DOM prefix
        if(this._domPrefix.test(type))
        {
          var msg = "Custom events, such as ["+type+"] cannot start with the prefix 'DOM' as required by the event specification.";
          throw new Mojo.$.com.runwaysdk.Exception(msg);
        }
      
        // custom event
        wrapper = listener;
        if(capture)
        {
          var msg = "Custom events, such as ["+type+"], cannot be listened for in capture mode. Listen globally instead.";
          throw new Mojo.$.com.runwaysdk.Exception(msg);
        }
        
      }
      else
      {
        wrapper = this._wrapDOMListener(type, listener, obj, context, capture);
        
        // FIXME Justin
        if (com.runwaysdk.ui.ElementProviderIF.getMetaClass().isInstance(target))
        {
          EventUtil.addEventListener(target.getEl().getRawEl(), type, wrapper, capture);
        }
        else if (com.runwaysdk.ui.ElementIF.getMetaClass().isInstance(target))
        {
          EventUtil.addEventListener(target.getRawEl(), type, wrapper, capture);
        }
        else if (target === document) {
          EventUtil.addEventListener(target, type, wrapper, capture);
        }
        else
        {
          var msg = "Cannot add event listener on type ["+target+"].";
          throw new Mojo.$.com.runwaysdk.Exception(msg);
        }
      }
      
      capture = capture || false;
    
      var listenerObj = new ListenerEntry(type, listener, wrapper, obj, context, capture);
      if(this._listeners.containsKey(target))
      {
        // discard duplicates
        var listeners = this._listeners.get(target);
        for(var i=0, len=listeners.length; i<len; i++)
        {
          var entry = listeners[i];
          if(type === entry.getType() && listener === entry.getListener())
          {
            return;
          }
        }
        listeners.push(listenerObj);
      }
      else
      {
        this._listeners.put(target, [listenerObj]);
      }
    },
    
    removeEventListener : function(target, type, listener, capture)
    {
      capture = capture || false;
      
      if(this._listeners.containsKey(target))
      {
        var listeners = this._listeners.get(target);
        if(listeners !== null)
        {
          for(var i=0; i<listeners.length; i++)
          {
            var l = listeners[i];
            if(l.matches(type, listener, capture))
            {
              listeners.splice(i,1);
              
              if(!Mojo.Meta.classExists(type))
              {
                EventUtil.removeEventListener(target, type, l.getWrapper(), l.captures());
              }
              
              return;
            }
          }
        }
      }
    },
    
    removeEventListeners : function(target, type)
    {
      var listeners = this._listeners.get(target);
      if(listeners !== null)
      {
        for(var i=0, len=listeners.length; i<len; i++)
        {
          var l = listeners[i];
          var filtered = [];
          if(l.matches(type, l.getListener(), l.captures()))
          {
             
            if(!Mojo.Meta.classExists(type))
            {
              EventUtil.removeEventListener(target, type, l.getWrapper(), l.captures());
            }
          }
          else
          {
            filtered.push(l);
          }
        }
        
        this._listeners.put(target, filtered);
      }
    },
    
    removeAllEventListeners : function(target)
    {
      var listeners = this._listeners.get(target);
      if(listeners !== null)
      {
        for(var i=0, len=listeners.length; i<len; i++)
        {
          var l = listeners[i];
             
          if(!Mojo.Meta.classExists(l.getType()))
          {
            EventUtil.removeEventListener(target, l.getType(), l.getWrapper(), l.captures());
          }
        }
        
        this._listeners.remove(target);
      }
    },
    
    hasEventListener : function(target, type, listener, capture)
    {
      capture = capture || false;
      
      var listeners = this._listeners.get(target);
      if(listeners !== null)
      {
        for(var i=0, len=listeners.length; i<len; i++)
        {
          var l = listeners[i];
          if(l.matches(type, listener, capture))
          {
            return true;
          }
        }
      }
      
      return false;      
    }
  }
});

var DestroyEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'DestroyEvent', {
  Extends : Mojo.$.com.runwaysdk.event.CustomEvent,
  Instance : {
    initialize : function(object)
    {
      this.$initialize();
      this._object = object;
    },
    getObject : function() { return this._object; },
  }
});


var TaskIF = Mojo.Meta.newInterface(Mojo.STRUCTURE_PACKAGE+'TaskIF', {
  Instance : {
    /**
     * Starts this task.
     * 
     * @param taskQueue The TaskQueue instance that is managing this TaskIF.
     */
    start : function(taskQueue){}
  }
});

var TaskListenerIF = Mojo.Meta.newInterface(Mojo.STRUCTURE_PACKAGE+'TaskListenerIF', {
  Instance : {
    /**
     * Called when a TaskIF is started.
     * 
     * @param taskQueue The TaskQueue instance that is managing this TaskIF.
     */
    onStart : function(taskQueue){},
    /**
     * Called when a TaskIF is finished (success case)
     * 
     * @param taskQueue The TaskQueue instance that is managing this TaskIF.
     */
    onFinish : function(taskQueue){},
    /**
     * Called when a TaskIF is stopped (error case)
     * 
     * @param taskQueue The TaskQueue instance that is managing this TaskIF.
     * @param e An optional object, usually an Error instance, that can be used for error handling, logging, or debugging.
     */
    onStop : function(taskQueue, e){}  
  }
});

var TaskListener = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+"TaskListener", {
  Implements: TaskListenerIF,
  
  Instance : {
    onStart : function(){},
    onFinish : function(){},
    onStop : function(){}
  }
});

var TaskQueue = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'TaskQueue', {
  Extends : Structure.AbstractCollection,
  Implements : TaskIF, 
  Instance : {
    
    initialize : function()
    {
      this._remaining = [];
      this._listeners = [];
      this._currentTask = null;
      this._currentListeners = null;
      this._completed = [];
      this._processing = false;
      this._stopped = false;
      this._locked = false;
      this._queueListeners = [];
      this._taskQueue = null;
    },
    
    /**
     * Locks this instance such that no modifications can take place
     * while it processes.
     */
    lock : function(){
      this._locked = true;
    },
    
    /**
     * Unlocks this instance such that modifications can take place
     * while it processes.
     */
    unlock : function(){
      this._locked = false;
    },
    
    /**
     * Checks if this instance is locked.
     * 
     * @return Returns true if locked or false otherwise.
     */
    isLocked : function(){
      return this._locked;
    },
    
    /**
     * Adds a TaskListenerIF object, which will have its handler methods
     * invoked when this TaskQueue starts, finishes, or aborts.
     * 
     * @taskListenerIFs An object that implements the methods of TaskListenerIF.
     * This parameter can be any number of TaskListenerIF objects
     * (i.e., TaskQueue.addTaskQueueListener(taskListenerIFs*))
     */
    addTaskQueueListener : function(taskListenerIF){
      // Cannot add new listeners if the queue is locked
      if(this.isLocked()){
        throw new Exception('Cannot invoke TaskQueue.addTaskQueueListener() while the TaskQueue is locked');
      }
      
      this.addEventListener(TQStartEvent, {handleEvent: taskListenerIF.onStart});
      this.addEventListener(TQStopEvent, {handleEvent: taskListenerIF.onStop});
      this.addEventListener(TQFinishEvent, {handleEvent: taskListenerIF.onFinish});
    },
    
    /**
     * Adds a TaskListenerIF object, which will have its handler methods
     * invoked for each TaskIF object that starts, finishes, or aborts.
     * 
     * @taskListenerIFs An object that implements the methods of TaskListenerIF.
     * This parameter can be any number of TaskListenerIF objects
     * (i.e., TaskQueue.addTaskListener(taskListenerIFs*)).
     */
    addTaskListener : function(taskListenerIF){
    
      // Cannot add new listeners if the queue is locked
      if(this.isLocked()){
        throw new Exception('Cannot invoke TaskQueue.addTaskListener() while the TaskQueue is locked');
      }
      
      this.addEventListener(TStartEvent, {handleEvent: taskListenerIF.onStart});
      this.addEventListener(TStopEvent, {handleEvent: taskListenerIF.onStop});
      this.addEventListener(TFinishEvent, {handleEvent: taskListenerIF.onFinish});
    },
    
    /**
     * Adds a TaskIF object to the end of this TaskQueue.
     * 
     * @param taskIFs An object that implements the methods of TaskIF.
     * This parameter can be any number of TaskIF objects 
     * (i.e., TaskQueue.addTask(task1*))
     */
    addTask : function(taskIFs){
    
      // Cannot add new tasks if the queue is locked
      if(this.isLocked()){
        throw new Exception('Cannot invoke TaskQueue.addTask() while the TaskQueue is locked');
      }
    
      this._remaining = this._remaining.concat(Array.prototype.splice.call(arguments, 0, arguments.length));
    },
    
    /**
     * Returns an array of all tasks that remain in the queue.
     * 
     * @return An array, in queue order, of the remaining TaskIF objects.
     */
    getRemainingTasks : function(){
      return this._remaining;
    },
    
    /**
     * Returns the current TaskIF object that is being processed. This method
     * returns null if TaskQueue.start() has not been called or if the queue
     * has been successfully processed.
     * 
     * @return The current TaskIF that is being processed.
     */
    getCurrentTask : function(){
      return this._currentTask;
    },
    
    /**
     * Returns an array of all tasks that have completed.
     * 
     * @return An array, in queue order, of the completed TaskIF objects.
     */
    getCompletedTasks : function(){
      return this._completed;
    },
    
    /**
     * Checks if this TaskQueue is currently processing tasks.
     * 
     * @return Returns true if this instance is processing or false otherwise.
     */
    isProcessing : function(){
      return this._processing;
    },
    
    /**
     * Checks if this TaskQueue has had its processing stopped.
     * 
     * @return Returns true if this instance has been stopped or false otherwise.
     */
    isStopped : function(){
      return this._stopped;
    },
    
    /**
     * Aborts the processing of the queue. This method has no effect if called more
     * than once.
     * 
     * @param e An optional object, usually an Error instance, that will be passed to TaskListenerIF.onAbort(e).
     */
    stop : function(e){
      
      if(this._stopped){
        return;
      }
      
      // error checking
      if(!this._processing){
        throw new Exception('Cannot invoke TaskQueue.stop() if processing has not started.');
      }
      
      this._stopped = true;
      this._processing = false;
  
      // notify the listeners
      this.dispatchEvent(new TQStopEvent(this));
      this.dispatchEvent(new TStopEvent(this));
      
      this.removeAllEventListeners();
    },
    
    /**
     * Starts the processing of the tasks in queue order.
     * 
     * @param taskQueue An optional "owner" TaskQueue that is running this instance as a TaskIF.
     */
    start : function(taskQueue){
      this._processing = true;
      this._taskQueue = taskQueue;
      this.dispatchEvent(new TQStartEvent(this));
      this.next();
    },
    
    /**
     * Returns the next TaskIF object in the queue.
     * 
     * @return Returns the next TaskIF object in the queue or null if it is empty.
     */
    peek : function(){
      return this.hasNext() ? this._remaining[0] : null;
    },
    
    /**
     * Checks if this TaskQueue has another TaskIF object to process.
     * 
     * @return Returns true if there is another task to process or false otherwise.
     */
    hasNext : function(){
      return this._remaining.length > 0;
    },
    
    /**
     * The method that MUST be invoked by TaskIF instances to signal this TaskQueue that
     * processing can continue.
     */
    next : function(){
    
      // error checking
      if(this._stopped){
        throw new Exception('Cannot invoke TaskQueue.next() because the processing was stopped.');
      }
      else if(!this._processing){
        throw new Exception('Cannot invoke TaskQueue.next() because the queue is no longer processing.');
      }
    
      if(this._currentTask !== null){
        this._completed.push(this._currentTask);
        this.dispatchEvent(new TFinishEvent(this));
      }
    
      if(this.hasNext()){
        this._currentTask = this._remaining.shift();
        this.dispatchEvent(new TStartEvent(this));
        var args = [this].concat(Array.prototype.splice.call(arguments, 0, arguments.length));
        this._currentTask.start.apply(this, args);
      }
      else {
        // finished!
        this._processing = false;
        this._currentTask = null;
        this.dispatchEvent(new TQFinishEvent(this));
        
        this.removeAllEventListeners();
        
        // notify the owning TaskQueue (if this is a composite) that this instance is done processing
        if(this._taskQueue && Mojo.Util.isFunction(this._taskQueue.next)){
          this._taskQueue.next();
        }
      }
    },
    
    /**
     * String representation of this TaskQueue object and its current state.
     */
    toString : function(){
      return '[TaskQueue] processing: '+this.isProcessing()+', remaining: '+this.getRemainingTasks().length+', completed: '+this.getCompletedTasks().length;
    }  
  }
});

// TaskQueue Custom Events:
var TQBaseEvent = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'TQBaseEvent', {
  Extends : Mojo.$.com.runwaysdk.event.CustomEvent,
  Instance : {
    initialize : function(tq) {
      this.$initialize();
      this._tq = tq;
    },
    getTaskQueue : function() {
      return this._tq;
    }
  }
});
var TQStartEvent = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'TQStartEvent', {
  Extends : TQBaseEvent,
  Instance : {
    initialize : function(tq)
    {
      this.$initialize(tq);
    }
  }
});
var TQStopEvent = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'TQStopEvent', {
  Extends : TQBaseEvent,
  Instance : {
    initialize : function(tq)
    {
      this.$initialize(tq);
    }
  }
});
var TQFinishEvent = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'TQFinishEvent', {
  Extends : TQBaseEvent,
  Instance : {
    initialize : function(tq)
    {
      this.$initialize(tq);
    }
  }
});
var TStartEvent = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'TStartEvent', {
  Extends : TQBaseEvent,
  Instance : {
    initialize : function(tq)
    {
      this.$initialize(tq);
    }
  }
});
var TStopEvent = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'TStopEvent', {
  Extends : TQBaseEvent,
  Instance : {
    initialize : function(tq)
    {
      this.$initialize(tq);
    }
  }
});
var TFinishEvent = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'TFinishEvent', {
  Extends : TQBaseEvent,
  Instance : {
    initialize : function(tq)
    {
      this.$initialize(tq);
    }
  }
});

var ClientRequestSuccessEvent = Mojo.Meta.newClass(Mojo.ROOT_PACKAGE+'ClientRequestSuccessEvent', {
  Extends : com.runwaysdk.event.CustomEvent,
  Instance : {
    initialize : function(rv) {
      this.$initialize();
      this._rv = rv;
    },
    getReturnValue : function() {
      return this._rv;
    }
  }
});
var ClientRequestFailureEvent = Mojo.Meta.newClass(Mojo.ROOT_PACKAGE+'ClientRequestFailureEvent', {
  Extends : com.runwaysdk.event.CustomEvent,
  Instance : {
    initialize : function(ex, exType) {
      this.$initialize();
      this._ex = ex;
      this._exType = exType;
    },
    getException : function() {
      return this._ex;
    },
    getExceptionType : function() {
      return this._exType;
    }
  }
});

Mojo.Meta.newClass('Mojo.ClientRequest', {

  Instance : {
  
    initialize : function(handler){

      if (handler != null) {
        Mojo.Util.copy(handler, this);
      }

      this._warnings = [];
      this._information = [];
      this._transport = null;
    },
    
    addOnSuccessListener : function(listener) {
      this.addEventListener(ClientRequestSuccessEvent, {handleEvent: listener});
    },
    
    addOnFailureListener : function(listener) {
      this.addEventListener(ClientRequestFailureEvent, {handleEvent: listener});
    },
    
    performOnSuccess : function(retVal) {
      if(Mojo.Util.isFunction(this.onSuccess))
      {
        this.onSuccess(retVal);
      }
      this.dispatchEvent(new ClientRequestSuccessEvent(retVal));
    },
    
    performOnFailure : function(ex, exType) {
      if(Mojo.Util.isString(exType) && Mojo.Util.isFunction(this['on'+exType]))
      {
        this['on'+exType](ex);
      }
      // no match ... use the default handler
      else if(Mojo.Util.isFunction(this.onFailure))
      {
        this.onFailure(ex);
      }
      this.dispatchEvent(new ClientRequestFailureEvent(ex, exType));
    },
    
    getMessages : function() { return this._warnings.concat(this._information); },
    
    setWarnings : function(warnings) { this._warnings = warnings; },
    
    getWarnings : function() { return this._warnings; },
    
    setInformation : function(information) { this._information = information; },
    
    getInformation : function() { return this._information; },
    
    getTransport : function() { return this._transport; },
    
    setTransport : function(transport) { this._transport = transport; }
  }
});

var ClientSession = Mojo.Meta.newClass('Mojo.ClientSession', {

  IsSingleton : true,
  
  Instance : {
    initialize : function()
    {
      this._nativeParsingEnabled = true;
      
      // FIXME use constants for the keys
      this._ajaxOptions ={
          'method':'post',
          'contentType':'application/x-www-form-urlencoded',
          'encoding':'UTF-8',
          'asynchronous':true,
          'successRange':[200,299]
      };
      
      this._baseEndpoint = (Mojo.GLOBAL.location.protocol + "//" + Mojo.GLOBAL.location.host  +'/'+ Mojo.GLOBAL.location.pathname.split( '/' )[1] +'/');
    }
  },
  
  Static : {
    
    isNativeParsingEnabled : function() { return Mojo.ClientSession.getInstance()._nativeParsingEnabled; },
    
    setNativeParsingEnabled : function(enabled){ Mojo.ClientSession.getInstance()._nativeParsingEnabled = enabled; },

    getBaseEndpoint : function() { return Mojo.ClientSession.getInstance()._baseEndpoint; },
    
    setBaseEndpoint : function(baseEndpoint) { Mojo.ClientSession.getInstance()._baseEndpoint = baseEndpoint; },
    
    getAjaxOptions : function() { return Mojo.Util.copy(Mojo.ClientSession.getInstance()._ajaxOptions, {}); },
    
    setAjaxOptions : function(defaultOptions) { Mojo.Util.copy(defaultOptions, Mojo.ClientSession.getInstance()._ajaxOptions); }
  }
});

var AjaxRequest = Mojo.Meta.newClass(Mojo.ROOT_PACKAGE+'AjaxRequest', {

  Instance : {
    
    initialize: function (url, parameters, options)
    {
      this._url = url;
      this._xhr = this._xhrFactory();
      
      // encode the parameters if given a map
      this.paramStr = '';
      if(Mojo.Util.isObject(parameters))
      {
        var paramArray = [];
        for(var i in parameters)
        {
          paramArray.push(encodeURIComponent(i)+'='+encodeURIComponent(parameters[i]));
        }
        this.paramStr = paramArray.join('&');
      }
      else if (parameters != null)
      {
        this.paramStr = parameters.toString();
      }
      
      this.options = {};
      Mojo.Util.copy(Mojo.ClientSession.getAjaxOptions(), this.options);
      Mojo.Util.copy(options, this.options);
    },
    
    _xhrFactory : function()
    {
      try
      {
        // Firefox, Opera 8.0+, Safari
        return new XMLHttpRequest();
      }
      catch (e)
      {
        // Internet Explorer
        try
        {
          return new ActiveXObject("Msxml2.XMLHTTP");
        }
        catch (e)
        {
          try
          {
            return new ActiveXObject("Microsoft.XMLHTTP");
          }
          catch (e)
          {
            var message = "The browser does not support Ajax";
            throw new Exception(message);
          }
        }
      }
    },
    
    apply: function ()
    {
      this._send();
      
      try
      {      
        var bound = Mojo.Util.bind(this, this._onReadyStateChange);
        if(this.options.method.toLowerCase() === 'post')
        {
          this._xhr.open(this.options.method, this._url, this.options.asynchronous);
          this._xhr.onreadystatechange = bound;
          this._xhr.setRequestHeader("Content-type", this.options.contentType + "; charset="+this.options.encoding);
//          this._xhr.setRequestHeader("Content-length", this.paramStr.length);
//          this._xhr.setRequestHeader("Connection", "close");
  
          this._xhr.send(this.paramStr);
        }
        else
        {
          this._xhr.open(this.options.method, this._url+"?"+this.paramStr, this.options.asynchronous);
          this._xhr.onreadystatechange = bound;
          
          this._xhr.send(null);
        }
      }
      catch(e)
      {
        // FIXME add error handling for non-server exceptions
        this._complete();
      }
    },
    
    _send : function()
    {
      if (Mojo.Util.isFunction(this.options.onSend))
      {
        this.options.onSend(this);
      }
    },
    
    _complete : function()
    {
      if (Mojo.Util.isFunction(this.options.onComplete))
      {
        this.options.onComplete(this);
      }
    },
    
    _success : function()
    {
      if (Mojo.Util.isFunction(this.options.onSuccess))
      {
        this.options.onSuccess(this);
      }
    },
    
    _failure : function()
    {
      if (Mojo.Util.isFunction(this.options.onFailure))
      {
        this.options.onFailure(this);
      }
    },
    
    _onReadyStateChange : function()
    {
      if(this._xhr.readyState == 4)
      {
        this._complete();
        
        if(this._xhr.status >= this.options.successRange[0]
          && this._xhr.status <= this.options.successRange[1])
        {
          this._success();
        }
        else
        {
          this._failure();
        }
      }
    }
  }
});


/**
 * Class that formats and parses numeric instances. This class can be instantiated directly
 * but it is recommended that the static NumberFormat.getInstance() is called to return a
 * shared instance that has been initialized for the application.
 */
var NumberFormat = Mojo.Meta.newClass(Mojo.ROOT_PACKAGE+'NumberFormat', {
  Instance : {
    initialize : function(groupingSeparator, decimalSeparator, posPrefix, posSuffix, negPrefix, negSuffix)
    {
      this._posPrefix = posPrefix;
      this._posSuffix = posSuffix;

      this._negPrefix = negPrefix;
      this._negSuffix = negSuffix;
      
      // Escape the separator in case it is a regex character (e.g., period as a wildcard).
      // Not all regex characters are known, but it should be safe to escape them all
      this._groupingSeparator = groupingSeparator;
      this._groupingRegex = new RegExp('\\'+this._groupingSeparator, 'g');
      
      this._decimalSeparator = decimalSeparator;
      
      // Set default digit lengths
      this._minIntegerDigits = 1;
      this._maxIntegerDigits = 40;
      this._minFractionDigits = 2;
      this._maxFractionDigits = 2;
    },
    
    parse : function(value, integerOnly) {
      
      if(value == null)
      {
        return null;
      }
      
      if(Mojo.Util.isNumber(value))
      {
        // the value is already a number, so there's nothing to parse.
        return value;
      }
      
      var toParse = Mojo.Util.isString(value) ? value : value.toString();
      
      // final object check in case toString() returned a null or undefined
      if(toParse != null)
      {
        var isNegative = ((this._negPrefix != '' && toParse.indexOf(this._negPrefix) != -1) 
            || (this._negSuffix != '' && toParse.indexOf(this._negSuffix) != -1));
      
        // Remove all suffix and prefix values
        var temp = new String(toParse);
        temp = temp.replace(this._posPrefix, "");
        temp = temp.replace(this._posSuffix, "");
        temp = temp.replace(this._negPrefix, "");
        temp = temp.replace(this._negSuffix, "");
      
        // make sure to remove the grouping separator so the 
        // number isn't truncated (e.g., parseFloat('1,234') === 1).
        temp = temp.replace(this._groupingRegex, '');
        
        // Convert the decimal point because javascript parsing
        // expects an english-based format (e.g., 123.45).
        temp = temp.replace(this._decimalSeparator, ".");
        
        var number;
        if(integerOnly || temp.indexOf(".") == -1)
        {
          number = parseInt(temp);
        }
        else
        {
          number = parseFloat(temp);
        }
        
        if(isNegative) {
          number = number * -1;
        }
      
        return number;
      }
      else
      {
        // the given value was not a number or an object representative of a number
        return NaN;
      }
    },
    
    format : function(number) {
      
      if(Mojo.Util.isString())
      {
        // the number is already a string. Nothing to format.
        return number;
      }
      
      var isNegative = (number < 0);
      var isInteger = number % 1 === 0;
      
      var postiveNumber = (isNegative ? -1 * number : number);
      
      // if the number is an integer or even decimal to not show fractional digits
      // FIXME this needs to be flagged in a better way because an application might
      // want to show 123.00 instead of 123 because of significant digits.
      var value = postiveNumber.toFixed((isInteger ? 0 : this._maxFractionDigits));
      
      
      var core = value.replace(".", this._decimalSeparator);
      
      if(isNegative)
      {
        return this._negPrefix + core + this._negSuffix;
      }
      else
      {
        return this._posPrefix + core + this._posSuffix;
      }
    },
    
    getDecimalSeparator : function() {
      return this._decimalSeparator;
    },
    
    getGroupingSeparator : function() {
      return this._groupingSeparator;
    },
    
    getMaxIntegerDigits : function() {
      return this._maxIntegerDigits;
    },
    
    getMinIntegerDigits : function() {
      return this._minIntegerDigits;
    },
    
    getMaxFractionDigits : function() {
      return this._maxFractionDigits;
    },
    
    getMinFractionDigits : function() {
      return this._minFractionDigits;
    },
    
    setMaxIntegerDigits : function(maxIntegerDigits) {
      this._maxIntegerDigits = maxIntegerDigits;
    },
    
    setMinIntegerDigits : function(minIntegerDigits) {
      this._minIntegerDigits = minIntegerDigits;
    },
    
    setMaxFractionDigits : function(maxFractionDigits) {
      this._maxFractionDigits = maxFractionDigits;
    },
    
    setMinFractionDigits : function(minFractionDigits) {
      this._minFractionDigits = minFractionDigits;
    }
  },
  Static : {
    
    applicationInstance : null,
    
    /**
     * Initializes this applications instance of NumberFormat.
     */
    initializeInstance : function(groupingSeparator, decimalSeparator, posPrefix, posSuffix, negPrefix, negSuffix){
      NumberFormat.applicationInstance = new NumberFormat(groupingSeparator, decimalSeparator, posPrefix, posSuffix, negPrefix, negSuffix);
    },
    
    /**
     * Returns shared instance across this application. If 
     */
    getInstance : function(){
      
      if(NumberFormat.applicationInstance == null){
        // create a parser with english locale settings
        NumberFormat.applicationInstance = new NumberFormat(',', '.', '', '', '-', '');
      }
      
      return NumberFormat.applicationInstance;
    }
  }
});

var Localize = Mojo.Meta.newClass(Mojo.ROOT_PACKAGE+'Localize', {   
  IsSingleton : true,
   
  Instance : {
    initialize : function(obj)
    {
      this.$initialize();
      this._map = new Mojo.$.com.runwaysdk.structure.HashMap(obj);
    },
   
    get : function(key)
    {
      return this._map.get(key);
    },
   
    put : function(key, value)
    {
      return this._map.put(key, value);
    },
   
    putAll : function(obj)
    {
      this._map.putAll(obj);
    }
  },   
   
  Static : {
    get : function(key, defaultValue)
    { 
      var text = Localize.getInstance().get(key)
      
      if(text !== null && text !== undefined)
      {
        return text;
      }
      
      if(defaultValue !== null && defaultValue !== undefined)
      {
        return defaultValue;
      }
      
      return "???" + key + "???";
    },
    
    put : function(key, value)
    {
      return Localize.getInstance().put(key, value);
    },
   
    putAll : function(obj)
    {
     return Localize.getInstance().putAll(obj);
    },
    
    defineLanguage : function(className, map) {
      var newMap = {};
      
      for (var key in map) {
        if(map.hasOwnProperty(key)){
          newMap[className + "." + key] = map[key];
        }
      }
      
      return Localize.putAll(newMap);
    }
  }
});

})();