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

//define(["./log4js", "./Util", "./errorcatch"], function(Log4js, Util){
(function(){

  var Util = window.Mojo.Util;
  
  var rootPackage = 'com.runwaysdk.';
  
  var Mojo = {
    // protected namespace for all classes
    $ : {},
    
    // core constants
    ROOT_PACKAGE : rootPackage,
    STRUCTURE_PACKAGE : rootPackage+'structure.',
    EVENT_PACKAGE : rootPackage+'event.',
    
    META_CLASS_GETTER : 'getMetaClass',
    
    // general purpose empty function
    emptyFunction : function(){},
    
    // reference to global object (e.g., window)
    GLOBAL : (function(){ return this; })()
  };
  
  // Make Mojo visible to the global namespace // FIXME add until end in case of error
  Mojo.GLOBAL.Mojo = Mojo;
  
  Mojo.SUPPORTS_NATIVE_PARSING = Mojo.GLOBAL.JSON != null && Util.isFunction(Mojo.GLOBAL.JSON.parse) && Util.isFunction(Mojo.GLOBAL.JSON.stringify);
  
  var _isInitialized = false;
  var _classes = {};
  var _pseudoConstructor = function(){};
  var _native = []; // array of native bootstrapping classes
  
  var Base = null;
  
  var meta = {

    newInstance : function(type)
    {
      if (!Mojo.Meta.classExists(type)) 
      {
        throw new Exception("Unable to newInstance " + type + ". The specified class does not exist.");
      }
      
      var klass = _classes[type];
      var args = [].splice.call(arguments, 1, arguments.length);
      
      var obj = new klass(_pseudoConstructor);
      klass.prototype.initialize.apply(obj, args);
      
      return obj;
    },
    
    // FIXME if pattern matches explicitely on one class then return that class instead of an object
    // FIXME this doesn't return nested packages. For example, com.runwaysdk.ui.* won't include com.runwaysdk.ui.fatory.*
    alias : function(pattern, attachTo)
    {
      if (attachTo === Mojo.GLOBAL)
      {
       throw new Exception("Cannot alias classes to the global scope to avoid naming collisions.");  
      }
      
      if (pattern.match(/\*.+/))
      {
       throw new Exception("Invalid alias class specified: "+pattern);  
      }
      
      attachTo = attachTo || {};
      
      var r = '^'+pattern.replace(/\./g, '\\.').replace(/\*/g, '_?[A-Za-z0-9]+')+'$';
      var re = new RegExp(r);
      
      var classNames = Mojo.Meta.getClasses();
      for(var i=0; i<classNames.length; i++)
      {
        var className = classNames[i];
        if(re.test(className))
        {
          var klass = _classes[className];
          attachTo[klass.getMetaClass().getName()] = klass;
        }
      }
      
      return attachTo;
    },
    
    findClass : function(type, startingPackage)
    {
      if (startingPackage == null) {
        return _classes[type];
      }
      else {
        var parts = type.split(".");
        var ref = startingPackage;
      
        for (var i = 0; i < parts.length; i++) {
          if (ref != null) {
            ref = ref[parts[i]];
          }
          else {
            return null;
          }
        }
      
        return ref;
      }
    },
    
    classCount : function()
    {
      return Util.getKeys(_classes, true).length;
    },
    
    _buildPackage : function(packageName, alias)
    {
      if(packageName == null || packageName === '')
      {
        return alias;
      }
    
      var parts = packageName.split(".");

      var currentBuild = alias;
      for(var i=0; i<parts.length; i++)
      {
        var part = parts[i];

        if(!currentBuild[part])
        {
          currentBuild[part] = {};
        }

        currentBuild = currentBuild[part];
      }

      return currentBuild;      
    },
    
    dropClass : function(type)
    {
      // FIXME drop all subclasses from tree (maybe add destroy() method to MetaClass that drops its own children)
      // FIXME remove all aliases and shorthand
      if (!Mojo.Meta.classExists(type))
      {
        throw new Exception("Unable to dropClass " + type + ". The specified class does not exist.");
      }
      
      delete _classes[type];
      
      var parts = type.split(".");
      
      // Delete global reference
      var pack = Mojo.GLOBAL;
      for (var i = 0; i < parts.length-1; i++) {
        pack = pack[parts[i]];
      }
      delete pack[ parts[parts.length-1] ];
      
      // Delete Mojo.$ reference
      var pack = Mojo.$;
      for (var i = 0; i < parts.length-1; i++) {
        pack = pack[parts[i]];
      }
      delete pack[ parts[parts.length-1] ];
    },
    
    getClasses : function()
    {
      return Util.getKeys(_classes, true);
    },
    
    classExists : function(type)
    {
      return _classes.hasOwnProperty(type);
    },
    
    _makeSingleton : function(klass)
    {
      // block normal instantiation
      var sInitialize = klass.prototype.initialize;
      klass.prototype.initialize = function(){
     
        var message = "Cannot instantiate the singleton class ["+this.getMetaClass().getQualifiedName()+"]. " +
          "Use the static [getInstance()] method instead.";
        throw new Exception(message);        
      };
      
      klass.getInstance = (function(sInit){
        
        var instance = null;
        
        return function(){
          
          if(instance == null)
          {
            // TODO use something other than function swapping?
            var temp = klass.prototype.initialize;
            klass.prototype.initialize = sInit;
            instance = new klass();
            klass.prototype.initialize = temp;
          }
 
          return instance;      
        };
      })(sInitialize);
        
      return {name : 'getInstance', isStatic : true, isConstructor : false,
        method : klass.getInstance, klass: klass};  
    },
    
    _createConstructor : function()
    {
      return function(){
      
        if(_isInitialized && this.getMetaClass().isAbstract())
        {
          var msg = "Cannot instantiate the abstract class ["+this.getMetaClass().getQualifiedName()+"].";
          throw new Exception(msg);
        }
        
        this.__context = {}; // super context
        
        if(arguments.length === 1 && arguments[0] === _pseudoConstructor)
        {
          _pseudoConstructor(); // for "reflective" newInstance()
                      // calls.
        }
        else
        {
          this.initialize.apply(this, arguments);
        }
      };      
    },
    
    _addOverride : function(m)
    {
      return function(){
        // find the next different method on the superclass prototype
        // and execute it because it is the overridden super method.
        var current = this.__context[m] || this.constructor;
        var next = current.getMetaClass().getSuperClass();
        
        while(current.prototype[m] === next.prototype[m])
        {
          next = next.getMetaClass().getSuperClass();
        }
        
        this.__context[m] = next;
        
        var retObj = next.prototype[m].apply(this, arguments);
        
        this.__context[m] = current;
        
        return retObj;
      };    
    },
    
    _addMethod : function(klass, superClass, methodName, definition)
    {
      var isFunc = Util.isFunction(definition);
      var method = isFunc ? definition : definition.Method;
    
      // add instance method to the prototype
      klass.prototype[methodName] = method;
      
      // add override accessor if the parent class defines the same method
      if(superClass !== Object && Util.isFunction(superClass.prototype[methodName]))
      {
        var superName = '$'+methodName;
        klass.prototype[superName] = this._addOverride(methodName);
      }
      
      return {name : methodName, isStatic : false, isAbstract : (!isFunc && definition.IsAbstract),
          isConstructor : (methodName === 'initialize'), method : method, klass: klass,
          enforceArity: (definition.EnforceArity || false)};
    },
    
    _newType : function(metaRef, qualifiedName, def, isIF)
    {
      try {
      
        if (!Util.isString(qualifiedName) || qualifiedName.length === 0) {
          throw new Exception('The first parameter must be a valid qualified type name.');
        }
        else 
          if (def != null && !Util.isObject(def)) {
            throw new Exception('The second parameter must be a configuration object literal.');
          }
        
        // Set type defaults
        def = def ||
        {};
        
        var extendsClass = null;
        var constants = {};
        var instances = {};
        
        var statics = {};
        var isSingleton = false;
        var isAbstract = false;
        var interfaces = [];
        
        // override defaults and validate properties
        for (var prop in def) {
          if (def.hasOwnProperty(prop)) {
            switch (prop) {
              case 'Extends':
                if (def[prop] == null) {
                  var classOrIF = "class";
                  if (isIF) {
                    classOrIF = "interface";
                  }
                  throw new Exception('The ' + classOrIF + ' [' + qualifiedName + '] cannot extend a null or undefined ' + classOrIF + '.');
                }
                extendsClass = def[prop];
                break;
              case 'Constants':
                constants = def[prop];
                break;
              case 'Instance':
                instances = def[prop];
                break;
              case 'Static':
                if (isIF) {
                  throw new Exception('The interface [' + qualifiedName + '] cannot define static properties or methods.');
                }
                statics = def[prop];
                break;
              case 'IsSingleton':
                if (isIF) {
                  throw new Exception('The interface [' + qualifiedName + '] cannot be a singleton.');
                }
                isSingleton = def[prop];
                break;
              case 'IsAbstract':
                if (isIF) {
                  throw new Exception('The interface [' + qualifiedName + '] cannot be abstract.');
                }
                isAbstract = def[prop];
                break;
              case 'Implements':
                if (isIF) {
                  throw new Exception('The interface [' + qualifiedName + '] cannot implement other Interfaces.');
                }
                
                var ifs = def[prop];
                if (!Util.isArray(ifs)) {
                  ifs = [ifs];
                }
                
                for (var i = 0; i < ifs.length; i++) {
                  var IF = ifs[i];
                  var ifKlass = Util.isString(IF) ? _classes[IF] : IF;
                  if (ifKlass != null && ifKlass.getMetaClass().isInterface()) {
                    interfaces.push(ifKlass);
                  }
                  else {
                    throw new Exception('The class [' + qualifiedName + '] cannot implement the class [' + (ifKlass != null ? ifKlass.getQualifiedName() : null) + '].');
                  }
                }
                
                break;
              default:
                throw new Exception('The property [' + prop + '] on type [' + qualifiedName + '] is not recognized.');
            }
          }
        }
        
        var superClass;
        if (Util.isFunction(extendsClass)) {
          superClass = extendsClass;
        }
        else 
          if (Util.isString(extendsClass)) {
            superClass = _classes[extendsClass];
          }
          else {
            superClass = Base;
          }
        
        if (!superClass) {
          throw new Exception('The class [' + qualifiedName + '] does not extend a valid class.');
        }
        
        // attach the package/class to the alias
        var packageName;
        var className;
        if (/\./.test(qualifiedName)) {
          packageName = qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
          className = qualifiedName.substring(packageName.length + 1);
        }
        else {
          packageName = '';
          className = qualifiedName;
        }
        
        // make sure a constructor exists
        if (!isIF && !instances.initialize) {
          instances.initialize = function(){
          };
        }
        else 
          if (isIF && instances.initialize) {
            throw new Exception('The interface [' + qualifiedName + '] cannot define an initialize() constructor.');
          }
          else 
            if (isIF) {
              instances.initialize = function(obj){
                this.getMetaClass()._enforceAnonymousInnerClass(obj);
                Util.copy(obj, this);
              };
            }
        
        // wrap the constructor function
        var klass = metaRef._createConstructor();
        
        // add the namespace to the global object and Mojo.$
        var namespace = metaRef._buildPackage(packageName, Mojo.GLOBAL);
        namespace[className] = klass;
        
        namespace = metaRef._buildPackage(packageName, Mojo.$);
        namespace[className] = klass;
        
        _classes[qualifiedName] = klass;
        
        // temp function is used for inheritance instantiation, to
        // avoid calling actual class constructor
        var temp = function(){
        };
        temp.prototype = superClass.prototype;
        klass.prototype = new temp();
        
        // reset constructor to point to the class, such that
        // new A().constructor === A
        klass.prototype.constructor = klass;
        
        // config obj for MetaClass constructor
        var config = {
          packageName: packageName,
          className: className,
          klass: klass,
          superClass: superClass,
          instanceMethods: {},
          staticMethods: {},
          isAbstract: isAbstract,
          isInterface: isIF,
          qualifiedName: qualifiedName,
          isSingleton: isSingleton,
          interfaces: interfaces,
          constants: []
        };
        
        for (var m in instances) {
          var methodConfig = metaRef._addMethod(klass, superClass, m, instances[m]);
          config.instanceMethods[m] = methodConfig;
        }
        
        if (isSingleton) {
          var methodDef = this._makeSingleton(klass);
          config.staticMethods.getInstance = methodDef;
        }
        
        // add constants
        for (var c in constants) {
          config.constants.push({
            name: c,
            value: constants[c]
          });
        }
        
        // add static methods
        for (var m in statics) {
          if (Util.isFunction(statics[m])) {
            if (statics[m].IsAbstract) {
              throw new Exception("The method " + m + " defined on the class " + className + " cannot be both static and abstract.");
            }
            
            config.staticMethods[m] = {
              name: m,
              isStatic: true,
              isAbstract: false,
              isConstructor: false,
              method: statics[m],
              klass: klass,
              enforceArity: false
            };
          }
          else {
            // FIXME wrap static props in a Property class and have them
            // optionally
            // inherited (or use visibility modifiers?)
            klass[m] = statics[m];
          }
        }
        
        // attach the metadata Class
        if (_native !== null) {
          // MetaClass will be constructed later to complete bootstrapping
          klass.__metaClass = config;
          klass.prototype.__metaClass = config;
          _native.push(klass);
        }
        else {
          klass.__metaClass = new MetaClass(config);
          klass.prototype.__metaClass = klass.__metaClass;
        }
        
      } 
      catch (e) {
        if (this.classExists(qualifiedName)) {
          this.dropClass(qualifiedName);
        }
        throw e;
      }
      
      return klass;      
    },
    
    newInterface : function(qualifiedName, definition)
    {
      var metaRef = Mojo.Meta || this;
      return metaRef._newType(metaRef, qualifiedName, definition, true);
    },
    
    newClass : function(qualifiedName, definition)
    {
      var metaRef = Mojo.Meta || this;
      return metaRef._newType(metaRef, qualifiedName, definition, false);
    },
    
    extendsBase : function(obj)
    {
      if (Base == null) {
        return false;
      }
      
      return obj instanceof Base;
    },
    
    getBaseClass : function() {
      return Base;
    }
  };

  Base = meta.newClass(Mojo.ROOT_PACKAGE+'Base', {

    IsAbstract : true,
    
    Extends : Object,
  
    Instance : {
  
      initialize : function()
      {
        // Default Constructor
        this.__hashCode = null;
      },
    
      equals : function(obj)
      {
        return this === obj;
      },
      
      getHashCode : function()
      {
        if(this.__hashCode == null)
        {
          this.__hashCode = Mojo.Util.generateId(16);
        }
        
        return this.__hashCode;
      },
    
      clone : function()
      {
        var args = [this.getMetaClass().getQualifiedName()].concat(Array.prototype.splice.call(arguments, 0, arguments.length));
        return Mojo.Meta.newInstance.apply(this, args);
      },
      
      valueOf : function()
      {
        return this;
      },
      
      toString : function()
      {
        return '['+this.getMetaClass().getQualifiedName()+'] : ['+this.getHashCode()+']';
      },
      
      addEventListener : function(type, listener, obj, context, capture)
      {
        type = Util.isFunction(type) ? type.getMetaClass().getQualifiedName() : type;
        Mojo.$.com.runwaysdk.event.Registry.getInstance().addEventListener(this, type, listener, obj, context, capture);
      },
  
      removeEventListener : function(type, listener, capture)
      {
        type = Util.isFunction(type) ? type.getMetaClass().getQualifiedName() : type;
        Mojo.$.com.runwaysdk.event.Registry.getInstance().removeEventListener(this, type, listener, capture);
      },
      
      removeEventListeners : function(type)
      {
        type = Util.isFunction(type) ? type.getMetaClass().getQualifiedName() : type;
        Mojo.$.com.runwaysdk.event.Registry.getInstance().removeEventListeners(this, type);
      },
      
      removeAllEventListeners : function()
      {
        Mojo.$.com.runwaysdk.event.Registry.getInstance().removeAllEventListeners(this);
      },
      
      hasEventListener : function(type, listener, useCapture)
      {
        type = Util.isFunction(type) ? type.getMetaClass().getQualifiedName() : type;
        return Mojo.$.com.runwaysdk.event.Registry.getInstance().hasEventListener(this, type, listener, useCapture);
      },
  
      /**
       * Dispatches the given event. Note that custom events do not support
       * a capturing phase.
       */
      dispatchEvent : function(evt)
      {
        // FIXME handle dispatching DOM events?
      
        // set the target to this object if the event is just being dispatched
        if(evt.getEventPhase() === com.runwaysdk.event.EventIF.AT_TARGET)
        {
          evt._setTarget(this);
        }
        
        evt._setCurrentTarget(this);
        
        // dispatch the event for all listeners of this object (the current target)
        Mojo.$.com.runwaysdk.event.Registry.getInstance().dispatchEvent(evt);
        
        if(evt.getTarget().equals(this) && !evt.getPreventDefault())
        {
          evt.defaultAction();
        }
        
        return !evt.getPreventDefault();
      },
      
      destroy : function() {
        this.dispatchEvent(new com.runwaysdk.event.DestroyEvent(this));
        this.removeAllEventListeners();
      }
    }
  });
  
  var MetaClass = meta.newClass(Mojo.ROOT_PACKAGE+'MetaClass', {
  
    Instance : {
    
      initialize : function(config)
      {
        this._packageName = config.packageName;
        this._className = config.className;
        this._isAbstract = config.isAbstract;
        this._isInterface = config.isInterface;
        this._isSingleton = config.isSingleton;
        this._klass = config.klass;
        this._superClass = config.superClass;
        this._qualifiedName = config.qualifiedName;
        this._subclasses = {};
        this._interfaces = config.interfaces;
        
        var notBase = this._superClass !== Object;  
        
        this._addInstanceMethods(notBase, config.instanceMethods);
        this._addStaticMethods(notBase, config.staticMethods);      
        this._addConstants(notBase, config.constants);
        
        if(notBase)
        {
          this._superClass.getMetaClass()._addSubClass(this._qualifiedName, this._klass);
        }
        
        this._addMetaClassMethod();
        
        if(_isInitialized && !this._isInterface)
        {
          this._enforceInterfaceMethods();
        }
      },
      
      _addInstanceMethods : function(notBase, tInstances)
      {
        var mKlass = Method;
        
        this._instanceMethods = {};
        var abstractMethods = {};
        if(notBase)
        {
          // instance methods will be copied via prototype
          var pInstances = this._superClass.getMetaClass().getInstanceMethods(true);
          for(var i in pInstances)
          {
            var method = pInstances[i];
            this._instanceMethods[i] = method;
            
            if(method.isAbstract())
            {
              abstractMethods[i] = method;
            }
          }
        }
          
        for(var i in tInstances)
        {
          var definition = tInstances[i];
        
          // Check for a method override
          if(this._instanceMethods.hasOwnProperty(i))
          {
            var overridden = this._instanceMethods[i];
            definition.overrideKlass = definition.klass;
            definition.klass = overridden.getDefiningClass();
          }
          
          this._instanceMethods[i] = new mKlass(definition, this);
          
          if(i in abstractMethods)
          {
            delete abstractMethods[i]; // abstract method implemented!
          }
        }
        
        // Make sure all abstract methods are implemented
        if(!this._isAbstract)
        {
          var unimplemented = [];
          for(var i in abstractMethods)
          {
            if(abstractMethods.hasOwnProperty(i))
            {
              unimplemented.push(abstractMethods[i].getName());
            }
          }
          
          if(unimplemented.length > 0)
          {
            var msg = "The class ["+this._qualifiedName+"] must " + 
              "implement the abstract method(s) ["+unimplemented.join(', ')+"].";
            throw new Exception(msg);
          }
        }
      },
      
      _addStaticMethods : function(notBase, tStatics)
      {
        var mKlass = Method;
        
        this._staticMethods = {};
        
        if(notBase)
        {
          // static methods must be explicitly copied
          var pStatics = this._superClass.getMetaClass().getStaticMethods(true);
          for(var i in pStatics)
          {
            var mStatic = pStatics[i];
            this._staticMethods[mStatic.getName()] = mStatic;
            
            this._klass[mStatic.getName()] = mStatic.getMethod();
          }
        }
          
        for(var i in tStatics)
        {
          var definition = tStatics[i];
        
          if(this._staticMethods.hasOwnProperty(i))
          {
            var overridden = this._staticMethods[i];
            definition.overrideKlass = definition.klass;
            definition.klass = overridden.getDefiningClass();
          }
          
          // add the method metadata
          var method = new mKlass(definition, this);
          this._staticMethods[i] = method;
          
          // add the actual method to the class
          this._klass[i] = definition.method;
        }
      },
      
      _addConstants : function(notBase, constants)
      {
        this._constants = {};
        var cKlass = Constant;
        if(notBase)
        {
          var pConstants = this._superClass.getMetaClass().getConstants(true);
          for(var i in pConstants)
          {
            if(pConstants.hasOwnProperty(i))
            {
              this._constants[i] = pConstants[i];
            }
          }
        }
        
        for(var i=0; i<constants.length; i++)
        {
          var constObj = new cKlass(constants[i]);
          
          if(notBase && this._constants[constObj.getName()])
          {
          // FIXME remove _setOverride and use same methodology as instance/static
      // methods above
            constObj._setDefiningClass(this._constants[constObj.getName()].getDefiningClass());
            constObj._setOverrideClass(this._klass);
          }
          else
          {
            constObj._setDefiningClass(this._klass);
          }
          
          this._constants[constObj.getName()] = constObj;
          this._klass[constObj.getName()] = constObj.getValue();
        }
      },
      
      _addMetaClassMethod : function()
      {
        var mName = Mojo.META_CLASS_GETTER;
        
        // Each class constructor function and instance gets
        // a method to return this Class instance.
        this._klass[mName] = (function(metaClass){
          return function() {
            return metaClass;
          };
        })(this);
        
        this._klass.prototype[mName] = this._klass[mName];
        
        var baseClass = Base;
        this._instanceMethods[mName] = new Method({
          name : mName,
          isStatic : false,
          isAbstract : false, 
          isConstructor : false,
          method : this._klass[mName],
          klass: baseClass,
          overrideKlass : this._klass,
          enforceArity : false
        }, this);
        
       this._staticMethods[mName] = new Method({
          name : mName,
          isStatic : true,
          isAbstract : false, 
          isConstructor : false,
          method : this._klass[mName],
          klass: baseClass,
          overrideKlass : this._klass,
          enforceArity : false
        }, this);
      },
      
      _addSubClass : function(qualifiedName, klass)
      {
        this._subclasses[qualifiedName] = klass;
      },
      
      isSingleton : function()
      {
        return this._isSingleton;
      },
      
      isInterface : function()
      {
        return this._isInterface;
      },
      
      getInterfaces : function()
      {
        return this._interfaces;
      },
      
      getSubClasses : function(asMap)
      {
        if(asMap)
        {
          return this._subclasses;
        }
        else
        {
          var values = [];
          for(var i in this._subclasses)
          {
            values.push(this._subclasses[i]);
          }
          return values;
        }
      },
      
      getConstants : function(asMap)
      {
        if(asMap)
        {
          return this._constants;
        }
        else
        {
          var values = [];
          for(var i in this._constants)
          {
            values.push(this._constants[i]);
          }
          return values;
        }
      },
      
      hasConstant : function(name)
      {
        return this._constants[name] != null;
      },
      
      getConstant: function(name)
      {
        return this._constants[name];
      },
  
      getMethod : function(name)
      {
        // FIXME will not work with instance/static method of same name
        return this._instanceMethods[name] || this._staticMethods[name];
      },
      
      hasInstanceMethod : function(name)
      {
        return this._instanceMethods[name] != null;
      },
      
      getInstanceMethod : function(name)
      {
        return this._instanceMethods[name];
      },
      
      hasStaticMethod : function(name)
      {
        return this._staticMethods[name] != null;
      },
      
      getStaticMethod : function(name)
      {
        return this._staticMethods[name];
      },
      
      getInstanceMethods : function(asMap)
      {
        if(asMap)
        {
          return this._instanceMethods;
        }
        else
        {
          var arr = [];
          for(var i in this._instanceMethods)
          {
            if(true || this._instanceMethods.hasOwnProperty(i))
            {
              arr.push(this._instanceMethods[i]);
            } 
          }
          
          return arr;
        }
      },
      
      getStaticMethods : function(asMap)
      {
        if(asMap)
        {
          return this._staticMethods;
        }
        else
        {
          var arr = [];
          for(var i in this._staticMethods)
          {
            if(true || this._staticMethods.hasOwnProperty(i))
            {
              arr.push(this._staticMethods[i]);
            }
          }
          
          return arr;
        }
      },
      
      isAbstract : function()
      {
        return this._isAbstract;
      },
      
      getMethods : function()
      {
        return [].concat(this.getInstanceMethods(false), this.getStaticMethods(false));
      },
    
      getPackage : function()
      {
        return this._packageName;
      },
      
      getName : function()
      {
        return this._className;
      },
      
      getQualifiedName : function()
      {
        return this._qualifiedName;
      },
      
      _getClass : function(klass)
      {
        if(klass instanceof this.constructor)
        {
          return klass;
        }
        else if(Util.isFunction(klass) || klass instanceof Base)
        {
          return klass.getMetaClass();
        }
        else if(Util.isString(klass))
        {
          var foundClass = Mojo.Meta.findClass(klass);
          if(foundClass)
          {
            return foundClass.getMetaClass();
          }
          else
          {
            return null;
          }
        }
        else
        {
          return null;
        }
      },
      
      isSuperClassOf : function(klass)
      {
        var classObj = this._getClass(klass); 
        
        if(this === classObj)
        {
          return true;
        }
      
        var superClass = classObj.getSuperClass();
        while(superClass !== Object)
        {
          if(superClass.getMetaClass() === this)
          {
            return true;
          }
          
          superClass = superClass.getMetaClass().getSuperClass();
        }
        
        return false;
      },
      
      isSubClassOf : function(klass)
      {
        var classObj = this._getClass(klass); 
        return classObj.isSuperClassOf(this);
      },
      
      getFunction : function()
      {
        return this._klass;
      },
      
      _enforceAnonymousInnerClass : function(obj)
      {
        var IFs = [this._klass];
        var parentClass = this._superClass;
        while(parentClass.getMetaClass().isInterface())
        {
          var parentMeta = parentClass.getMetaClass();
          IFs.push(parentMeta.getFunction());
          parentClass = parentMeta.getSuperClass();
        }
        
        var toImplement = [];
        for(var i=0; i<IFs.length; i++)
        {
          toImplement = toImplement.concat(IFs[i].getMetaClass().getInstanceMethods());
        }
        
        var unimplemented = [];
        for(var i=0; i<toImplement.length; i++)
        {
          var method = toImplement[i];
          if(!method.getDefiningClass().getMetaClass().isInterface())
          {
            continue;
          }
          
          var name = method.getName();
          if(!(Util.isFunction(obj[name])) ||
              !obj.hasOwnProperty(name))
          {
            unimplemented.push(name);
          }
        }
        
        if(unimplemented.length > 0)
        {
          var msg = "The anonymous inner class ["+obj+"] must " + 
          "implement the interface method(s) ["+unimplemented.join(', ')+"].";
          throw new Exception(msg);
        }
      },
      
      _enforceInterfaceMethods : function()
      {
        var IFs = this.getInterfaces();
        var parentClass = this._superClass;
        while(parentClass !== Object)
        {
          var parentMeta = parentClass.getMetaClass();
          IFs = IFs.concat(parentMeta.getInterfaces());
          parentClass = parentMeta.getSuperClass();
        }
        
        var toImplement = [];
        for(var i=0; i<IFs.length; i++)
        {
          toImplement = toImplement.concat(IFs[i].getMetaClass().getInstanceMethods());
        }
        
        var unimplemented = [];
        for(var i=0; i<toImplement.length; i++)
        {
          var method = toImplement[i];
          if(!method.getDefiningClass().getMetaClass().isInterface())
          {
            continue;
          }
          
          var name = method.getName();
          var isDefined = (name in this._instanceMethods);
          if(!this._isAbstract && !isDefined)
          {
            unimplemented.push(name);
          }
          else if(isDefined && method.enforcesArity())
          {
            var implemented = this._instanceMethods[name];
            if(method.getArity() !== implemented.getArity())
            {
              var ifMethod = method.getDefiningClass().getMetaClass().getQualifiedName()+'.'+name;
              var msg = "The method ["+this._qualifiedName+"."+name+"] must " + 
              "define ["+method.getArity()+"] arguments as required by the interface method ["+ifMethod+"].";
              throw new Exception(msg);
            }
          }
        }
        
        if(unimplemented.length > 0)
        {
          var msg = "The class ["+this._qualifiedName+"] must " + 
          "implement the interface method(s) ["+unimplemented.join(', ')+"].";
          throw new Exception(msg);
        }
      },    
      
      doesImplement : function(IFinput)
      {
        if(this.isInterface() && this.isSubClassOf(IFinput))
        {
          // check for anonymous inner classes
          return true;
        }
        else if(!this.isInterface())
        {
          var klass = this._klass;
          while(klass !== Object)
          {
            var meta = klass.getMetaClass();
            var IFs = meta.getInterfaces();
            for(var i=0; i<IFs.length; i++)
            {
              var IF = IFs[i];
              if(IF.getMetaClass().isSubClassOf(IFinput))
              {
                return true;
              }
            }
            klass = meta.getSuperClass();
          }
          
          return false;
        }
        else
        {
          return false;
        }
      },
      
      isInstance : function(obj)
      {
        if(!Util.isObject(obj))
        {
          return false;
        }
        
        // cover regular classes and anonymous inner classes
        if(obj instanceof this._klass)
        {
          return true;
        }
  
        if(this.isInterface())
        {
          if(obj instanceof Base)
          {
            // compare the object's interfaces to *this* IF class.
            var klass = obj.constructor;
            while(klass !== Object)
            {
              var meta = klass.getMetaClass();
              var IFs = meta.getInterfaces();
              for(var i=0; i<IFs.length; i++)
              {
                var IF = IFs[i];
                if(IF.getMetaClass().isSubClassOf(this._klass))
                {
                  return true;
                }
              }
              klass = meta.getSuperClass();
            }
            
            return false;
          }
          else
          {
            return false;
          }
        }
        else
        {
          return false; // we don't know what this object is
        }
      },
      
      getSuperClass : function()
      {
        return this._superClass;
      },
      
      newInstance : function()
      {
        var args = [this.getQualifiedName()].concat([].splice.call(arguments, 0, arguments.length));
        return Mojo.Meta.newInstance.apply(this, args);
      },
      
      toString : function()
      {
        return '[MetaClass] ' + this.getQualifiedName();
      },
      
      toJSON : function ()
      {
        return undefined;
      },
      
      equals : function(obj) {
        if (this.$equals(obj)) {
          return true;
        }
        
        if (obj instanceof MetaClass && this.getQualifiedName() === obj.getQualifiedName()) {
          return true;
        }
        
        return false;
      }
    }
  });
  
  var Constant = meta.newClass(Mojo.ROOT_PACKAGE+"Constant", {
    
    Instance : {
    
      initialize : function(config)
      {
        this._name = config.name;
        this._value = config.value;
        this._klass = null;
        this._overrideKlass = null;
      },
      
      _setDefiningClass : function(klass)
      {
        this._klass = klass;
      },
      
      _setOverrideClass : function(klass)
      {
        this._overrideKlass = klass;
      },
      
      getName : function()
      {
        return this._name;
      },
      
      getValue : function()
      {
        return this._value;
      },
      
      getDefiningClass : function()
      {
        return this._klass;
      },
      
      isOverride : function()
      {
        return this._overrideKlass !== null;
      },
      
      getOverrideClass : function()
      {
        return this._overrideKlass;
      }
    }
    
  });
  
  var Method = meta.newClass(Mojo.ROOT_PACKAGE+'Method', {
  
    Instance : {
    
      initialize : function(config, metaClass)
      {
        this._name = config.name;
        this._isStatic = config.isStatic;
        this._isConstructor = config.isConstructor;
        this._klass = config.klass || null;
        this._overrideKlass = config.overrideKlass || null;
        this._isAbstract = config.isAbstract;
        this._enforceArity = config.enforceArity || false;
        
        if(_isInitialized && !metaClass.isAbstract()
           && this._isAbstract)
        {
          var msg = "The non-abstract class ["+metaClass.getQualifiedName()+"] cannot " + 
            "cannot declare the abstract method ["+this._name+"].";
          throw new Exception(msg);
        }
        
        if(this._isAbstract)
        {
          this._createAbstractMethod();
          this._arity = 0;
        }
        else
        {
          this._arity = config.method.length;
        }
      },
      
      _createAbstractMethod : function()
      {
        // Add the abstract method to always throw an error. This
        // will replace any method already on the prototype.
        this._klass.prototype[this._name] = (function(name){
          return function(){
  
            var definingClass = this.getMetaClass().getMethod(name).getDefiningClass().getMetaClass().getQualifiedName();
  
            var msg = "Cannot invoke the abstract method ["+name+"] on ["+definingClass+"].";
            throw new Exception(msg);
          };
        })(this._name);
      },
      
      isAbstract : function()
      {
        return this._isAbstract;
      },
      
      enforcesArity : function()
      {
        return this._enforceArity;
      },
      
      isConstructor : function()
      {
        return this._isConstructor;
      },
      
      getArity : function()
      {
        return this._arity;
      },
      
      isOverride : function()
      {
        return !this._isStatic && this._overrideKlass !== null;
      },
      
      isHiding : function()
      {
        return this._isStatic && this._overrideKlass !== null;
      },
      
      getOverrideClass : function()
      {
        return this._overrideKlass;
      },
      
      getMethod : function()
      {
        var klass = this._overrideKlass || this._klass;
        return this._isStatic ? klass[this._name] : klass.prototype[this._name];
      },
    
      getName : function()
      {
        return this._name;
      },
      
      isStatic : function()
      {
        return this._isStatic;
      },
      
      getDefiningClass : function()
      {
        return this._klass;
      },
      
      toString : function()
      {
        return '[Method] ' + this.getName();
      }
    }
    
  });
  
  // Finish bootstrapping the class system
  for(var i=0; i<_native.length; i++)
  {
    var bootstrapped = _native[i];
    
    // Convert the JSON config __metaClass into a MetaClass instance
    // and re-attach the metadata to the class definition.
    var cClass = new MetaClass(bootstrapped.__metaClass);
    bootstrapped.__metaClass = cClass;
    bootstrapped.prototype.__metaClass = cClass;
  }
  _native = null;
  
  // convert the Meta object to a class
  var metaProps = {};
  for(var i in meta)
  {
    if(meta.hasOwnProperty(i))
    {
      metaProps[i] = meta[i];
    }
  }
  
  var Meta = Mojo.Meta = metaProps.newClass('Mojo.Meta', {
    Static : metaProps  
  });
  meta = null;
  
  _isInitialized = true;
  
  /**
   * Exception
   * 
   * This is the actual exception that can be thrown and caught. There is no Mojo
   * Java counterpart, so we throw it in the root namespace.
   */
  //Create a logger for our exceptions
  var exLogger = new Log4js.getLogger("Runway Exception Constructor Logger");
  exLogger.setLevel(Log4js.Level.ALL); // this should take a parameter from the clerver
  if ( (window.console && window.console.log) || window.opera )
  {
    exLogger.addAppender(new Log4js.BrowserConsoleAppender());
  }
  else {
    exLogger.addAppender(new Log4js.ConsoleAppender());
  }
  var listeners = [];
  var Exception = Mojo.Meta.newClass(Mojo.ROOT_PACKAGE+'Exception', {
  
    Extends : Mojo.ROOT_PACKAGE+'Base',
    
    Static : {
      addEventListener : function(fn) {
        listeners.push(fn);
      }
    },
    
    Instance : {
  
      initialize : function ()
      {
        this._internalE = null;
        
        if(arguments.length == 1)
        {
          var arg = arguments[0];
          if(arg == null)
          {
            this.localizedMessage = null;
            this.developerMessage = null;
          }
          else if(Util.isString(arg))
          {
            this.localizedMessage = arg;
            this.developerMessage = null;
          }
          else if(arg instanceof Mojo.GLOBAL.Error)
          {
            this.localizedMessage = arg.message;
            this.developerMessage = null;
            this._internalE = arg;
          }
          else if(Util.isObject(arg)
            && 'localizedMessage' in arg
            && 'developerMessage' in arg)
          {
            this.localizedMessage = arg.localizedMessage;
            this.developerMessage = arg.developerMessage;
          }
          else
          {
            this.localizedMessage = null;
            this.developerMessage = null;
          }
        }
        else if(arguments.length === 2)
        {
          this.localizedMessage = arguments[0];
          this.developerMessage = arguments[1];
        }
        else
        {
          this.localizedMessage = null;
          this.developerMessage = null;
        }
        
        // Make the message public to conform with the Error API
        this.message = this.developerMessage || this.localizedMessage;
        
        
        this._stackTrace = [];
        var removeLines = false;
        if(this._internalE === null)
        {
          try {
            this._internalE = new Error(); // used to get a stacktrace
          }
          catch(e) {
            this._internalE = e;
          }
          
          removeLines = true;
        }
        
        // Thanks to http://www.eriwen.com/javascript/js-stack-trace/ for some ideas.
        if(Util.isString(this._internalE.stack) && this._internalE.stack !== "") // Mozilla + Chrome
        {
          // This converts the string into an array
          this._stackTrace = this._internalE.stack.split("\n");
        }
        else if (window.opera && this._internalE.message) { //Opera
          var lines = this._internalE.message.split('\n');
          for (var i=0, len=lines.length; i<len; i++) {
//            if (lines[i].match(/^\s*[A-Za-z0-9\-_\$]+\(/)) {
              var entry = lines[i];
              //Append next line also since it has the file info
              if (lines[i+1]) {
                entry += ' at ' + lines[i+1];
                i++;
              }
              this._stackTrace.push(entry);
//            }
          }
        }
        else if (arguments.callee != null && arguments.callee.caller != null && ((Mojo.Util.isString(arguments.callee.caller.name) && arguments.callee.caller.name !== "") || (arguments.callee.caller.toString().substring(arguments.callee.caller.toString().indexOf("function") + 8, arguments.callee.caller.toString().indexOf('')) || 'anonymous') !== "function")) {
          //IE and Safari
          var currentFunction = arguments.callee.caller;
          
          while (currentFunction) {
            var fn = currentFunction.toString();
            var fname = null;
            if (fn.name == null || fn.name === "") {
              fname = fn.substring(fn.indexOf("function") + 8, fn.indexOf('')) || 'anonymous';
            }
            else {
              fname = fn.name;
            }
            this._stackTrace.push(fname);
            currentFunction = currentFunction.caller;
          }
        }
        else
        {
          // Stack trace not supported.
          removeLines = false;
        }
        
        // Remove the message at the front, if it exists.
        if (this._stackTrace[0] === "Error") {
          this._stackTrace.shift();
        }
        
        // Remove the first two lines of the stack trace because they are the creation of this Exception.
        if (removeLines) {
          this._stackTrace.shift();
          this._stackTrace.shift();
        }
        
        this._stackTrace.splice(0, 0, this.message) // Add the message at the front
        
        exLogger.log(Log4js.Level.ERROR, this._stackTrace.join("\n"));
        
        // Used to prevent double logging of errors.
        ErrorCatch.lastExceptionLogged = this;
        
        for (var i = 0; i < listeners.length; ++i) {
          listeners[i](this);
        }
      },
      
      getLocalizedMessage : function() { return this.localizedMessage; },
      
      getMessage : function() { return this.localizedMessage; },
      
      getDeveloperMessage : function() { return this.developerMessage; },
      
      toString : function() { return this.localizedMessage; },
      
      /**
       * @returns array
       */
      getStackTrace : function()
      {
        return this._stackTrace;
      }
    
    }
  });
  
  // Define Util with our class framework for code that accesses it from the global scope.
  var Util = Mojo.Meta.newClass('Mojo.Util', {
    IsAbstract : true,
    Static : Util
  });
  
  return Meta;

})();