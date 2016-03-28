// Ol3-Google-Maps. See https://github.com/mapgears/ol3-google-maps/
// License: https://github.com/mapgears/ol3-google-maps/blob/master/LICENSE
// Version: v0.4

(function (root, factory) {
  if (typeof exports === "object") {
    module.exports = factory(root.ol);
  } else if (typeof define === "function" && define.amd) {
    define(['ol'], factory);
  } else {
    root.olgm = factory(root.ol);
  }
}(this, function (ol) {
  var OL3GOOGLEMAPS = {};
  var goog = this.goog = {};
this.CLOSURE_NO_DEPS = true;
// Copyright 2006 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Bootstrap for the Google JS Library (Closure).
 *
 * In uncompiled mode base.js will write out Closure's deps file, unless the
 * global <code>CLOSURE_NO_DEPS</code> is set to true.  This allows projects to
 * include their own deps file(s) from different locations.
 *
 *
 * @provideGoog
 */


/**
 * @define {boolean} Overridden to true by the compiler when --closure_pass
 *     or --mark_as_compiled is specified.
 */
var COMPILED = false;


/**
 * Base namespace for the Closure library.  Checks to see goog is already
 * defined in the current scope before assigning to prevent clobbering if
 * base.js is loaded more than once.
 *
 * @const
 */
var goog = goog || {};


/**
 * Reference to the global context.  In most cases this will be 'window'.
 */
goog.global = this;


/**
 * A hook for overriding the define values in uncompiled mode.
 *
 * In uncompiled mode, {@code CLOSURE_UNCOMPILED_DEFINES} may be defined before
 * loading base.js.  If a key is defined in {@code CLOSURE_UNCOMPILED_DEFINES},
 * {@code goog.define} will use the value instead of the default value.  This
 * allows flags to be overwritten without compilation (this is normally
 * accomplished with the compiler's "define" flag).
 *
 * Example:
 * <pre>
 *   var CLOSURE_UNCOMPILED_DEFINES = {'goog.DEBUG': false};
 * </pre>
 *
 * @type {Object.<string, (string|number|boolean)>|undefined}
 */
goog.global.CLOSURE_UNCOMPILED_DEFINES;


/**
 * A hook for overriding the define values in uncompiled or compiled mode,
 * like CLOSURE_UNCOMPILED_DEFINES but effective in compiled code.  In
 * uncompiled code CLOSURE_UNCOMPILED_DEFINES takes precedence.
 *
 * Also unlike CLOSURE_UNCOMPILED_DEFINES the values must be number, boolean or
 * string literals or the compiler will emit an error.
 *
 * While any @define value may be set, only those set with goog.define will be
 * effective for uncompiled code.
 *
 * Example:
 * <pre>
 *   var CLOSURE_DEFINES = {'goog.DEBUG': false};
 * </pre>
 *
 * @type {Object.<string, (string|number|boolean)>|undefined}
 */
goog.global.CLOSURE_DEFINES;


/**
 * Returns true if the specified value is not undefined.
 * WARNING: Do not use this to test if an object has a property. Use the in
 * operator instead.
 *
 * @param {?} val Variable to test.
 * @return {boolean} Whether variable is defined.
 */
goog.isDef = function(val) {
  // void 0 always evaluates to undefined and hence we do not need to depend on
  // the definition of the global variable named 'undefined'.
  return val !== void 0;
};


/**
 * Builds an object structure for the provided namespace path, ensuring that
 * names that already exist are not overwritten. For example:
 * "a.b.c" -> a = {};a.b={};a.b.c={};
 * Used by goog.provide and goog.exportSymbol.
 * @param {string} name name of the object that this file defines.
 * @param {*=} opt_object the object to expose at the end of the path.
 * @param {Object=} opt_objectToExportTo The object to add the path to; default
 *     is |goog.global|.
 * @private
 */
goog.exportPath_ = function(name, opt_object, opt_objectToExportTo) {
  var parts = name.split('.');
  var cur = opt_objectToExportTo || goog.global;

  // Internet Explorer exhibits strange behavior when throwing errors from
  // methods externed in this manner.  See the testExportSymbolExceptions in
  // base_test.html for an example.
  if (!(parts[0] in cur) && cur.execScript) {
    cur.execScript('var ' + parts[0]);
  }

  // Certain browsers cannot parse code in the form for((a in b); c;);
  // This pattern is produced by the JSCompiler when it collapses the
  // statement above into the conditional loop below. To prevent this from
  // happening, use a for-loop and reserve the init logic as below.

  // Parentheses added to eliminate strict JS warning in Firefox.
  for (var part; parts.length && (part = parts.shift());) {
    if (!parts.length && goog.isDef(opt_object)) {
      // last part and we have an object; use it
      cur[part] = opt_object;
    } else if (cur[part]) {
      cur = cur[part];
    } else {
      cur = cur[part] = {};
    }
  }
};


/**
 * Defines a named value. In uncompiled mode, the value is retreived from
 * CLOSURE_DEFINES or CLOSURE_UNCOMPILED_DEFINES if the object is defined and
 * has the property specified, and otherwise used the defined defaultValue.
 * When compiled, the default can be overridden using compiler command-line
 * options.
 *
 * @param {string} name The distinguished name to provide.
 * @param {string|number|boolean} defaultValue
 */
goog.define = function(name, defaultValue) {
  var value = defaultValue;
  if (!COMPILED) {
    if (goog.global.CLOSURE_UNCOMPILED_DEFINES &&
        Object.prototype.hasOwnProperty.call(
            goog.global.CLOSURE_UNCOMPILED_DEFINES, name)) {
      value = goog.global.CLOSURE_UNCOMPILED_DEFINES[name];
    } else if (goog.global.CLOSURE_DEFINES &&
        Object.prototype.hasOwnProperty.call(
            goog.global.CLOSURE_DEFINES, name)) {
      value = goog.global.CLOSURE_DEFINES[name];
    }
  }
  goog.exportPath_(name, value);
};


/**
 * @define {boolean} DEBUG is provided as a convenience so that debugging code
 * that should not be included in a production js_binary can be easily stripped
 * by specifying --define goog.DEBUG=false to the JSCompiler. For example, most
 * toString() methods should be declared inside an "if (goog.DEBUG)" conditional
 * because they are generally used for debugging purposes and it is difficult
 * for the JSCompiler to statically determine whether they are used.
 */
goog.DEBUG = true;


/**
 * @define {string} LOCALE defines the locale being used for compilation. It is
 * used to select locale specific data to be compiled in js binary. BUILD rule
 * can specify this value by "--define goog.LOCALE=<locale_name>" as JSCompiler
 * option.
 *
 * Take into account that the locale code format is important. You should use
 * the canonical Unicode format with hyphen as a delimiter. Language must be
 * lowercase, Language Script - Capitalized, Region - UPPERCASE.
 * There are few examples: pt-BR, en, en-US, sr-Latin-BO, zh-Hans-CN.
 *
 * See more info about locale codes here:
 * http://www.unicode.org/reports/tr35/#Unicode_Language_and_Locale_Identifiers
 *
 * For language codes you should use values defined by ISO 693-1. See it here
 * http://www.w3.org/WAI/ER/IG/ert/iso639.htm. There is only one exception from
 * this rule: the Hebrew language. For legacy reasons the old code (iw) should
 * be used instead of the new code (he), see http://wiki/Main/IIISynonyms.
 */
goog.define('goog.LOCALE', 'en');  // default to en


/**
 * @define {boolean} Whether this code is running on trusted sites.
 *
 * On untrusted sites, several native functions can be defined or overridden by
 * external libraries like Prototype, Datejs, and JQuery and setting this flag
 * to false forces closure to use its own implementations when possible.
 *
 * If your JavaScript can be loaded by a third party site and you are wary about
 * relying on non-standard implementations, specify
 * "--define goog.TRUSTED_SITE=false" to the JSCompiler.
 */
goog.define('goog.TRUSTED_SITE', true);


/**
 * @define {boolean} Whether a project is expected to be running in strict mode.
 *
 * This define can be used to trigger alternate implementations compatible with
 * running in EcmaScript Strict mode or warn about unavailable functionality.
 * See https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Functions_and_function_scope/Strict_mode
 */
goog.define('goog.STRICT_MODE_COMPATIBLE', false);


/**
 * Creates object stubs for a namespace.  The presence of one or more
 * goog.provide() calls indicate that the file defines the given
 * objects/namespaces.  Provided objects must not be null or undefined.
 * Build tools also scan for provide/require statements
 * to discern dependencies, build dependency files (see deps.js), etc.
 * @see goog.require
 * @param {string} name Namespace provided by this file in the form
 *     "goog.package.part".
 */
goog.provide = function(name) {
  if (!COMPILED) {
    // Ensure that the same namespace isn't provided twice. This is intended
    // to teach new developers that 'goog.provide' is effectively a variable
    // declaration. And when JSCompiler transforms goog.provide into a real
    // variable declaration, the compiled JS should work the same as the raw
    // JS--even when the raw JS uses goog.provide incorrectly.
    if (goog.isProvided_(name)) {
      throw Error('Namespace "' + name + '" already declared.');
    }
    delete goog.implicitNamespaces_[name];

    var namespace = name;
    while ((namespace = namespace.substring(0, namespace.lastIndexOf('.')))) {
      if (goog.getObjectByName(namespace)) {
        break;
      }
      goog.implicitNamespaces_[namespace] = true;
    }
  }

  goog.exportPath_(name);
};


/**
 * Marks that the current file should only be used for testing, and never for
 * live code in production.
 *
 * In the case of unit tests, the message may optionally be an exact namespace
 * for the test (e.g. 'goog.stringTest'). The linter will then ignore the extra
 * provide (if not explicitly defined in the code).
 *
 * @param {string=} opt_message Optional message to add to the error that's
 *     raised when used in production code.
 */
goog.setTestOnly = function(opt_message) {
  if (COMPILED && !goog.DEBUG) {
    opt_message = opt_message || '';
    throw Error('Importing test-only code into non-debug environment' +
                (opt_message ? ': ' + opt_message : '.'));
  }
};


/**
 * Forward declares a symbol. This is an indication to the compiler that the
 * symbol may be used in the source yet is not required and may not be provided
 * in compilation.
 *
 * The most common usage of forward declaration is code that takes a type as a
 * function parameter but does not need to require it. By forward declaring
 * instead of requiring, no hard dependency is made, and (if not required
 * elsewhere) the namespace may never be required and thus, not be pulled
 * into the JavaScript binary. If it is required elsewhere, it will be type
 * checked as normal.
 *
 *
 * @param {string} name The namespace to forward declare in the form of
 *     "goog.package.part".
 */
goog.forwardDeclare = function(name) {};


if (!COMPILED) {

  /**
   * Check if the given name has been goog.provided. This will return false for
   * names that are available only as implicit namespaces.
   * @param {string} name name of the object to look for.
   * @return {boolean} Whether the name has been provided.
   * @private
   */
  goog.isProvided_ = function(name) {
    return !goog.implicitNamespaces_[name] &&
        goog.isDefAndNotNull(goog.getObjectByName(name));
  };

  /**
   * Namespaces implicitly defined by goog.provide. For example,
   * goog.provide('goog.events.Event') implicitly declares that 'goog' and
   * 'goog.events' must be namespaces.
   *
   * @type {Object}
   * @private
   */
  goog.implicitNamespaces_ = {};
}


/**
 * Returns an object based on its fully qualified external name.  The object
 * is not found if null or undefined.  If you are using a compilation pass that
 * renames property names beware that using this function will not find renamed
 * properties.
 *
 * @param {string} name The fully qualified name.
 * @param {Object=} opt_obj The object within which to look; default is
 *     |goog.global|.
 * @return {?} The value (object or primitive) or, if not found, null.
 */
goog.getObjectByName = function(name, opt_obj) {
  var parts = name.split('.');
  var cur = opt_obj || goog.global;
  for (var part; part = parts.shift(); ) {
    if (goog.isDefAndNotNull(cur[part])) {
      cur = cur[part];
    } else {
      return null;
    }
  }
  return cur;
};


/**
 * Globalizes a whole namespace, such as goog or goog.lang.
 *
 * @param {Object} obj The namespace to globalize.
 * @param {Object=} opt_global The object to add the properties to.
 * @deprecated Properties may be explicitly exported to the global scope, but
 *     this should no longer be done in bulk.
 */
goog.globalize = function(obj, opt_global) {
  var global = opt_global || goog.global;
  for (var x in obj) {
    global[x] = obj[x];
  }
};


/**
 * Adds a dependency from a file to the files it requires.
 * @param {string} relPath The path to the js file.
 * @param {Array} provides An array of strings with the names of the objects
 *                         this file provides.
 * @param {Array} requires An array of strings with the names of the objects
 *                         this file requires.
 */
goog.addDependency = function(relPath, provides, requires) {
  if (goog.DEPENDENCIES_ENABLED) {
    var provide, require;
    var path = relPath.replace(/\\/g, '/');
    var deps = goog.dependencies_;
    for (var i = 0; provide = provides[i]; i++) {
      deps.nameToPath[provide] = path;
      if (!(path in deps.pathToNames)) {
        deps.pathToNames[path] = {};
      }
      deps.pathToNames[path][provide] = true;
    }
    for (var j = 0; require = requires[j]; j++) {
      if (!(path in deps.requires)) {
        deps.requires[path] = {};
      }
      deps.requires[path][require] = true;
    }
  }
};




// NOTE(nnaze): The debug DOM loader was included in base.js as an original way
// to do "debug-mode" development.  The dependency system can sometimes be
// confusing, as can the debug DOM loader's asynchronous nature.
//
// With the DOM loader, a call to goog.require() is not blocking -- the script
// will not load until some point after the current script.  If a namespace is
// needed at runtime, it needs to be defined in a previous script, or loaded via
// require() with its registered dependencies.
// User-defined namespaces may need their own deps file.  See http://go/js_deps,
// http://go/genjsdeps, or, externally, DepsWriter.
// https://developers.google.com/closure/library/docs/depswriter
//
// Because of legacy clients, the DOM loader can't be easily removed from
// base.js.  Work is being done to make it disableable or replaceable for
// different environments (DOM-less JavaScript interpreters like Rhino or V8,
// for example). See bootstrap/ for more information.


/**
 * @define {boolean} Whether to enable the debug loader.
 *
 * If enabled, a call to goog.require() will attempt to load the namespace by
 * appending a script tag to the DOM (if the namespace has been registered).
 *
 * If disabled, goog.require() will simply assert that the namespace has been
 * provided (and depend on the fact that some outside tool correctly ordered
 * the script).
 */
goog.define('goog.ENABLE_DEBUG_LOADER', true);


/**
 * Implements a system for the dynamic resolution of dependencies that works in
 * parallel with the BUILD system. Note that all calls to goog.require will be
 * stripped by the JSCompiler when the --closure_pass option is used.
 * @see goog.provide
 * @param {string} name Namespace to include (as was given in goog.provide()) in
 *     the form "goog.package.part".
 */
goog.require = function(name) {

  // If the object already exists we do not need do do anything.
  // TODO(arv): If we start to support require based on file name this has to
  //            change.
  // TODO(arv): If we allow goog.foo.* this has to change.
  // TODO(arv): If we implement dynamic load after page load we should probably
  //            not remove this code for the compiled output.
  if (!COMPILED) {
    if (goog.isProvided_(name)) {
      return;
    }

    if (goog.ENABLE_DEBUG_LOADER) {
      var path = goog.getPathFromDeps_(name);
      if (path) {
        goog.included_[path] = true;
        goog.writeScripts_();
        return;
      }
    }

    var errorMessage = 'goog.require could not find: ' + name;
    if (goog.global.console) {
      goog.global.console['error'](errorMessage);
    }


      throw Error(errorMessage);

  }
};


/**
 * Path for included scripts.
 * @type {string}
 */
goog.basePath = '';


/**
 * A hook for overriding the base path.
 * @type {string|undefined}
 */
goog.global.CLOSURE_BASE_PATH;


/**
 * Whether to write out Closure's deps file. By default, the deps are written.
 * @type {boolean|undefined}
 */
goog.global.CLOSURE_NO_DEPS;


/**
 * A function to import a single script. This is meant to be overridden when
 * Closure is being run in non-HTML contexts, such as web workers. It's defined
 * in the global scope so that it can be set before base.js is loaded, which
 * allows deps.js to be imported properly.
 *
 * The function is passed the script source, which is a relative URI. It should
 * return true if the script was imported, false otherwise.
 * @type {(function(string): boolean)|undefined}
 */
goog.global.CLOSURE_IMPORT_SCRIPT;


/**
 * Null function used for default values of callbacks, etc.
 * @return {void} Nothing.
 */
goog.nullFunction = function() {};


/**
 * The identity function. Returns its first argument.
 *
 * @param {*=} opt_returnValue The single value that will be returned.
 * @param {...*} var_args Optional trailing arguments. These are ignored.
 * @return {?} The first argument. We can't know the type -- just pass it along
 *      without type.
 * @deprecated Use goog.functions.identity instead.
 */
goog.identityFunction = function(opt_returnValue, var_args) {
  return opt_returnValue;
};


/**
 * When defining a class Foo with an abstract method bar(), you can do:
 * Foo.prototype.bar = goog.abstractMethod
 *
 * Now if a subclass of Foo fails to override bar(), an error will be thrown
 * when bar() is invoked.
 *
 * Note: This does not take the name of the function to override as an argument
 * because that would make it more difficult to obfuscate our JavaScript code.
 *
 * @type {!Function}
 * @throws {Error} when invoked to indicate the method should be overridden.
 */
goog.abstractMethod = function() {
  throw Error('unimplemented abstract method');
};


/**
 * Adds a {@code getInstance} static method that always returns the same
 * instance object.
 * @param {!Function} ctor The constructor for the class to add the static
 *     method to.
 */
goog.addSingletonGetter = function(ctor) {
  ctor.getInstance = function() {
    if (ctor.instance_) {
      return ctor.instance_;
    }
    if (goog.DEBUG) {
      // NOTE: JSCompiler can't optimize away Array#push.
      goog.instantiatedSingletons_[goog.instantiatedSingletons_.length] = ctor;
    }
    return ctor.instance_ = new ctor;
  };
};


/**
 * All singleton classes that have been instantiated, for testing. Don't read
 * it directly, use the {@code goog.testing.singleton} module. The compiler
 * removes this variable if unused.
 * @type {!Array.<!Function>}
 * @private
 */
goog.instantiatedSingletons_ = [];


/**
 * True if goog.dependencies_ is available.
 * @const {boolean}
 */
goog.DEPENDENCIES_ENABLED = !COMPILED && goog.ENABLE_DEBUG_LOADER;


if (goog.DEPENDENCIES_ENABLED) {
  /**
   * Object used to keep track of urls that have already been added. This record
   * allows the prevention of circular dependencies.
   * @type {Object}
   * @private
   */
  goog.included_ = {};


  /**
   * This object is used to keep track of dependencies and other data that is
   * used for loading scripts.
   * @private
   * @type {Object}
   */
  goog.dependencies_ = {
    pathToNames: {}, // 1 to many
    nameToPath: {}, // 1 to 1
    requires: {}, // 1 to many
    // Used when resolving dependencies to prevent us from visiting file twice.
    visited: {},
    written: {} // Used to keep track of script files we have written.
  };


  /**
   * Tries to detect whether is in the context of an HTML document.
   * @return {boolean} True if it looks like HTML document.
   * @private
   */
  goog.inHtmlDocument_ = function() {
    var doc = goog.global.document;
    return typeof doc != 'undefined' &&
           'write' in doc;  // XULDocument misses write.
  };


  /**
   * Tries to detect the base path of base.js script that bootstraps Closure.
   * @private
   */
  goog.findBasePath_ = function() {
    if (goog.global.CLOSURE_BASE_PATH) {
      goog.basePath = goog.global.CLOSURE_BASE_PATH;
      return;
    } else if (!goog.inHtmlDocument_()) {
      return;
    }
    var doc = goog.global.document;
    var scripts = doc.getElementsByTagName('script');
    // Search backwards since the current script is in almost all cases the one
    // that has base.js.
    for (var i = scripts.length - 1; i >= 0; --i) {
      var src = scripts[i].src;
      var qmark = src.lastIndexOf('?');
      var l = qmark == -1 ? src.length : qmark;
      if (src.substr(l - 7, 7) == 'base.js') {
        goog.basePath = src.substr(0, l - 7);
        return;
      }
    }
  };


  /**
   * Imports a script if, and only if, that script hasn't already been imported.
   * (Must be called at execution time)
   * @param {string} src Script source.
   * @private
   */
  goog.importScript_ = function(src) {
    var importScript = goog.global.CLOSURE_IMPORT_SCRIPT ||
        goog.writeScriptTag_;
    if (!goog.dependencies_.written[src] && importScript(src)) {
      goog.dependencies_.written[src] = true;
    }
  };


  /**
   * The default implementation of the import function. Writes a script tag to
   * import the script.
   *
   * @param {string} src The script source.
   * @return {boolean} True if the script was imported, false otherwise.
   * @private
   */
  goog.writeScriptTag_ = function(src) {
    if (goog.inHtmlDocument_()) {
      var doc = goog.global.document;

      // If the user tries to require a new symbol after document load,
      // something has gone terribly wrong. Doing a document.write would
      // wipe out the page.
      if (doc.readyState == 'complete') {
        // Certain test frameworks load base.js multiple times, which tries
        // to write deps.js each time. If that happens, just fail silently.
        // These frameworks wipe the page between each load of base.js, so this
        // is OK.
        var isDeps = /\bdeps.js$/.test(src);
        if (isDeps) {
          return false;
        } else {
          throw Error('Cannot write "' + src + '" after document load');
        }
      }

      doc.write(
          '<script type="text/javascript" src="' + src + '"></' + 'script>');
      return true;
    } else {
      return false;
    }
  };


  /**
   * Resolves dependencies based on the dependencies added using addDependency
   * and calls importScript_ in the correct order.
   * @private
   */
  goog.writeScripts_ = function() {
    // The scripts we need to write this time.
    var scripts = [];
    var seenScript = {};
    var deps = goog.dependencies_;

    function visitNode(path) {
      if (path in deps.written) {
        return;
      }

      // We have already visited this one. We can get here if we have cyclic
      // dependencies.
      if (path in deps.visited) {
        if (!(path in seenScript)) {
          seenScript[path] = true;
          scripts.push(path);
        }
        return;
      }

      deps.visited[path] = true;

      if (path in deps.requires) {
        for (var requireName in deps.requires[path]) {
          // If the required name is defined, we assume that it was already
          // bootstrapped by other means.
          if (!goog.isProvided_(requireName)) {
            if (requireName in deps.nameToPath) {
              visitNode(deps.nameToPath[requireName]);
            } else {
              throw Error('Undefined nameToPath for ' + requireName);
            }
          }
        }
      }

      if (!(path in seenScript)) {
        seenScript[path] = true;
        scripts.push(path);
      }
    }

    for (var path in goog.included_) {
      if (!deps.written[path]) {
        visitNode(path);
      }
    }

    for (var i = 0; i < scripts.length; i++) {
      if (scripts[i]) {
        goog.importScript_(goog.basePath + scripts[i]);
      } else {
        throw Error('Undefined script input');
      }
    }
  };


  /**
   * Looks at the dependency rules and tries to determine the script file that
   * fulfills a particular rule.
   * @param {string} rule In the form goog.namespace.Class or project.script.
   * @return {?string} Url corresponding to the rule, or null.
   * @private
   */
  goog.getPathFromDeps_ = function(rule) {
    if (rule in goog.dependencies_.nameToPath) {
      return goog.dependencies_.nameToPath[rule];
    } else {
      return null;
    }
  };

  goog.findBasePath_();

  // Allow projects to manage the deps files themselves.
  if (!goog.global.CLOSURE_NO_DEPS) {
    goog.importScript_(goog.basePath + 'deps.js');
  }
}



//==============================================================================
// Language Enhancements
//==============================================================================


/**
 * This is a "fixed" version of the typeof operator.  It differs from the typeof
 * operator in such a way that null returns 'null' and arrays return 'array'.
 * @param {*} value The value to get the type of.
 * @return {string} The name of the type.
 */
goog.typeOf = function(value) {
  var s = typeof value;
  if (s == 'object') {
    if (value) {
      // Check these first, so we can avoid calling Object.prototype.toString if
      // possible.
      //
      // IE improperly marshals tyepof across execution contexts, but a
      // cross-context object will still return false for "instanceof Object".
      if (value instanceof Array) {
        return 'array';
      } else if (value instanceof Object) {
        return s;
      }

      // HACK: In order to use an Object prototype method on the arbitrary
      //   value, the compiler requires the value be cast to type Object,
      //   even though the ECMA spec explicitly allows it.
      var className = Object.prototype.toString.call(
          /** @type {Object} */ (value));
      // In Firefox 3.6, attempting to access iframe window objects' length
      // property throws an NS_ERROR_FAILURE, so we need to special-case it
      // here.
      if (className == '[object Window]') {
        return 'object';
      }

      // We cannot always use constructor == Array or instanceof Array because
      // different frames have different Array objects. In IE6, if the iframe
      // where the array was created is destroyed, the array loses its
      // prototype. Then dereferencing val.splice here throws an exception, so
      // we can't use goog.isFunction. Calling typeof directly returns 'unknown'
      // so that will work. In this case, this function will return false and
      // most array functions will still work because the array is still
      // array-like (supports length and []) even though it has lost its
      // prototype.
      // Mark Miller noticed that Object.prototype.toString
      // allows access to the unforgeable [[Class]] property.
      //  15.2.4.2 Object.prototype.toString ( )
      //  When the toString method is called, the following steps are taken:
      //      1. Get the [[Class]] property of this object.
      //      2. Compute a string value by concatenating the three strings
      //         "[object ", Result(1), and "]".
      //      3. Return Result(2).
      // and this behavior survives the destruction of the execution context.
      if ((className == '[object Array]' ||
           // In IE all non value types are wrapped as objects across window
           // boundaries (not iframe though) so we have to do object detection
           // for this edge case.
           typeof value.length == 'number' &&
           typeof value.splice != 'undefined' &&
           typeof value.propertyIsEnumerable != 'undefined' &&
           !value.propertyIsEnumerable('splice')

          )) {
        return 'array';
      }
      // HACK: There is still an array case that fails.
      //     function ArrayImpostor() {}
      //     ArrayImpostor.prototype = [];
      //     var impostor = new ArrayImpostor;
      // this can be fixed by getting rid of the fast path
      // (value instanceof Array) and solely relying on
      // (value && Object.prototype.toString.vall(value) === '[object Array]')
      // but that would require many more function calls and is not warranted
      // unless closure code is receiving objects from untrusted sources.

      // IE in cross-window calls does not correctly marshal the function type
      // (it appears just as an object) so we cannot use just typeof val ==
      // 'function'. However, if the object has a call property, it is a
      // function.
      if ((className == '[object Function]' ||
          typeof value.call != 'undefined' &&
          typeof value.propertyIsEnumerable != 'undefined' &&
          !value.propertyIsEnumerable('call'))) {
        return 'function';
      }

    } else {
      return 'null';
    }

  } else if (s == 'function' && typeof value.call == 'undefined') {
    // In Safari typeof nodeList returns 'function', and on Firefox typeof
    // behaves similarly for HTML{Applet,Embed,Object}, Elements and RegExps. We
    // would like to return object for those and we can detect an invalid
    // function by making sure that the function object has a call method.
    return 'object';
  }
  return s;
};


/**
 * Returns true if the specified value is null.
 * @param {?} val Variable to test.
 * @return {boolean} Whether variable is null.
 */
goog.isNull = function(val) {
  return val === null;
};


/**
 * Returns true if the specified value is defined and not null.
 * @param {?} val Variable to test.
 * @return {boolean} Whether variable is defined and not null.
 */
goog.isDefAndNotNull = function(val) {
  // Note that undefined == null.
  return val != null;
};


/**
 * Returns true if the specified value is an array.
 * @param {?} val Variable to test.
 * @return {boolean} Whether variable is an array.
 */
goog.isArray = function(val) {
  return goog.typeOf(val) == 'array';
};


/**
 * Returns true if the object looks like an array. To qualify as array like
 * the value needs to be either a NodeList or an object with a Number length
 * property.
 * @param {?} val Variable to test.
 * @return {boolean} Whether variable is an array.
 */
goog.isArrayLike = function(val) {
  var type = goog.typeOf(val);
  return type == 'array' || type == 'object' && typeof val.length == 'number';
};


/**
 * Returns true if the object looks like a Date. To qualify as Date-like the
 * value needs to be an object and have a getFullYear() function.
 * @param {?} val Variable to test.
 * @return {boolean} Whether variable is a like a Date.
 */
goog.isDateLike = function(val) {
  return goog.isObject(val) && typeof val.getFullYear == 'function';
};


/**
 * Returns true if the specified value is a string.
 * @param {?} val Variable to test.
 * @return {boolean} Whether variable is a string.
 */
goog.isString = function(val) {
  return typeof val == 'string';
};


/**
 * Returns true if the specified value is a boolean.
 * @param {?} val Variable to test.
 * @return {boolean} Whether variable is boolean.
 */
goog.isBoolean = function(val) {
  return typeof val == 'boolean';
};


/**
 * Returns true if the specified value is a number.
 * @param {?} val Variable to test.
 * @return {boolean} Whether variable is a number.
 */
goog.isNumber = function(val) {
  return typeof val == 'number';
};


/**
 * Returns true if the specified value is a function.
 * @param {?} val Variable to test.
 * @return {boolean} Whether variable is a function.
 */
goog.isFunction = function(val) {
  return goog.typeOf(val) == 'function';
};


/**
 * Returns true if the specified value is an object.  This includes arrays and
 * functions.
 * @param {?} val Variable to test.
 * @return {boolean} Whether variable is an object.
 */
goog.isObject = function(val) {
  var type = typeof val;
  return type == 'object' && val != null || type == 'function';
  // return Object(val) === val also works, but is slower, especially if val is
  // not an object.
};


/**
 * Gets a unique ID for an object. This mutates the object so that further calls
 * with the same object as a parameter returns the same value. The unique ID is
 * guaranteed to be unique across the current session amongst objects that are
 * passed into {@code getUid}. There is no guarantee that the ID is unique or
 * consistent across sessions. It is unsafe to generate unique ID for function
 * prototypes.
 *
 * @param {Object} obj The object to get the unique ID for.
 * @return {number} The unique ID for the object.
 */
goog.getUid = function(obj) {
  // TODO(arv): Make the type stricter, do not accept null.

  // In Opera window.hasOwnProperty exists but always returns false so we avoid
  // using it. As a consequence the unique ID generated for BaseClass.prototype
  // and SubClass.prototype will be the same.
  return obj[goog.UID_PROPERTY_] ||
      (obj[goog.UID_PROPERTY_] = ++goog.uidCounter_);
};


/**
 * Whether the given object is alreay assigned a unique ID.
 *
 * This does not modify the object.
 *
 * @param {Object} obj The object to check.
 * @return {boolean} Whether there an assigned unique id for the object.
 */
goog.hasUid = function(obj) {
  return !!obj[goog.UID_PROPERTY_];
};


/**
 * Removes the unique ID from an object. This is useful if the object was
 * previously mutated using {@code goog.getUid} in which case the mutation is
 * undone.
 * @param {Object} obj The object to remove the unique ID field from.
 */
goog.removeUid = function(obj) {
  // TODO(arv): Make the type stricter, do not accept null.

  // In IE, DOM nodes are not instances of Object and throw an exception if we
  // try to delete.  Instead we try to use removeAttribute.
  if ('removeAttribute' in obj) {
    obj.removeAttribute(goog.UID_PROPERTY_);
  }
  /** @preserveTry */
  try {
    delete obj[goog.UID_PROPERTY_];
  } catch (ex) {
  }
};


/**
 * Name for unique ID property. Initialized in a way to help avoid collisions
 * with other closure JavaScript on the same page.
 * @type {string}
 * @private
 */
goog.UID_PROPERTY_ = 'closure_uid_' + ((Math.random() * 1e9) >>> 0);


/**
 * Counter for UID.
 * @type {number}
 * @private
 */
goog.uidCounter_ = 0;


/**
 * Adds a hash code field to an object. The hash code is unique for the
 * given object.
 * @param {Object} obj The object to get the hash code for.
 * @return {number} The hash code for the object.
 * @deprecated Use goog.getUid instead.
 */
goog.getHashCode = goog.getUid;


/**
 * Removes the hash code field from an object.
 * @param {Object} obj The object to remove the field from.
 * @deprecated Use goog.removeUid instead.
 */
goog.removeHashCode = goog.removeUid;


/**
 * Clones a value. The input may be an Object, Array, or basic type. Objects and
 * arrays will be cloned recursively.
 *
 * WARNINGS:
 * <code>goog.cloneObject</code> does not detect reference loops. Objects that
 * refer to themselves will cause infinite recursion.
 *
 * <code>goog.cloneObject</code> is unaware of unique identifiers, and copies
 * UIDs created by <code>getUid</code> into cloned results.
 *
 * @param {*} obj The value to clone.
 * @return {*} A clone of the input value.
 * @deprecated goog.cloneObject is unsafe. Prefer the goog.object methods.
 */
goog.cloneObject = function(obj) {
  var type = goog.typeOf(obj);
  if (type == 'object' || type == 'array') {
    if (obj.clone) {
      return obj.clone();
    }
    var clone = type == 'array' ? [] : {};
    for (var key in obj) {
      clone[key] = goog.cloneObject(obj[key]);
    }
    return clone;
  }

  return obj;
};


/**
 * A native implementation of goog.bind.
 * @param {Function} fn A function to partially apply.
 * @param {Object|undefined} selfObj Specifies the object which this should
 *     point to when the function is run.
 * @param {...*} var_args Additional arguments that are partially applied to the
 *     function.
 * @return {!Function} A partially-applied form of the function bind() was
 *     invoked as a method of.
 * @private
 * @suppress {deprecated} The compiler thinks that Function.prototype.bind is
 *     deprecated because some people have declared a pure-JS version.
 *     Only the pure-JS version is truly deprecated.
 */
goog.bindNative_ = function(fn, selfObj, var_args) {
  return /** @type {!Function} */ (fn.call.apply(fn.bind, arguments));
};


/**
 * A pure-JS implementation of goog.bind.
 * @param {Function} fn A function to partially apply.
 * @param {Object|undefined} selfObj Specifies the object which this should
 *     point to when the function is run.
 * @param {...*} var_args Additional arguments that are partially applied to the
 *     function.
 * @return {!Function} A partially-applied form of the function bind() was
 *     invoked as a method of.
 * @private
 */
goog.bindJs_ = function(fn, selfObj, var_args) {
  if (!fn) {
    throw new Error();
  }

  if (arguments.length > 2) {
    var boundArgs = Array.prototype.slice.call(arguments, 2);
    return function() {
      // Prepend the bound arguments to the current arguments.
      var newArgs = Array.prototype.slice.call(arguments);
      Array.prototype.unshift.apply(newArgs, boundArgs);
      return fn.apply(selfObj, newArgs);
    };

  } else {
    return function() {
      return fn.apply(selfObj, arguments);
    };
  }
};


/**
 * Partially applies this function to a particular 'this object' and zero or
 * more arguments. The result is a new function with some arguments of the first
 * function pre-filled and the value of this 'pre-specified'.
 *
 * Remaining arguments specified at call-time are appended to the pre-specified
 * ones.
 *
 * Also see: {@link #partial}.
 *
 * Usage:
 * <pre>var barMethBound = bind(myFunction, myObj, 'arg1', 'arg2');
 * barMethBound('arg3', 'arg4');</pre>
 *
 * @param {?function(this:T, ...)} fn A function to partially apply.
 * @param {T} selfObj Specifies the object which this should point to when the
 *     function is run.
 * @param {...*} var_args Additional arguments that are partially applied to the
 *     function.
 * @return {!Function} A partially-applied form of the function bind() was
 *     invoked as a method of.
 * @template T
 * @suppress {deprecated} See above.
 */
goog.bind = function(fn, selfObj, var_args) {
  // TODO(nicksantos): narrow the type signature.
  if (Function.prototype.bind &&
      // NOTE(nicksantos): Somebody pulled base.js into the default Chrome
      // extension environment. This means that for Chrome extensions, they get
      // the implementation of Function.prototype.bind that calls goog.bind
      // instead of the native one. Even worse, we don't want to introduce a
      // circular dependency between goog.bind and Function.prototype.bind, so
      // we have to hack this to make sure it works correctly.
      Function.prototype.bind.toString().indexOf('native code') != -1) {
    goog.bind = goog.bindNative_;
  } else {
    goog.bind = goog.bindJs_;
  }
  return goog.bind.apply(null, arguments);
};


/**
 * Like bind(), except that a 'this object' is not required. Useful when the
 * target function is already bound.
 *
 * Usage:
 * var g = partial(f, arg1, arg2);
 * g(arg3, arg4);
 *
 * @param {Function} fn A function to partially apply.
 * @param {...*} var_args Additional arguments that are partially applied to fn.
 * @return {!Function} A partially-applied form of the function bind() was
 *     invoked as a method of.
 */
goog.partial = function(fn, var_args) {
  var args = Array.prototype.slice.call(arguments, 1);
  return function() {
    // Clone the array (with slice()) and append additional arguments
    // to the existing arguments.
    var newArgs = args.slice();
    newArgs.push.apply(newArgs, arguments);
    return fn.apply(this, newArgs);
  };
};


/**
 * Copies all the members of a source object to a target object. This method
 * does not work on all browsers for all objects that contain keys such as
 * toString or hasOwnProperty. Use goog.object.extend for this purpose.
 * @param {Object} target Target.
 * @param {Object} source Source.
 */
goog.mixin = function(target, source) {
  for (var x in source) {
    target[x] = source[x];
  }

  // For IE7 or lower, the for-in-loop does not contain any properties that are
  // not enumerable on the prototype object (for example, isPrototypeOf from
  // Object.prototype) but also it will not include 'replace' on objects that
  // extend String and change 'replace' (not that it is common for anyone to
  // extend anything except Object).
};


/**
 * @return {number} An integer value representing the number of milliseconds
 *     between midnight, January 1, 1970 and the current time.
 */
goog.now = (goog.TRUSTED_SITE && Date.now) || (function() {
  // Unary plus operator converts its operand to a number which in the case of
  // a date is done by calling getTime().
  return +new Date();
});


/**
 * Evals JavaScript in the global scope.  In IE this uses execScript, other
 * browsers use goog.global.eval. If goog.global.eval does not evaluate in the
 * global scope (for example, in Safari), appends a script tag instead.
 * Throws an exception if neither execScript or eval is defined.
 * @param {string} script JavaScript string.
 */
goog.globalEval = function(script) {
  if (goog.global.execScript) {
    goog.global.execScript(script, 'JavaScript');
  } else if (goog.global.eval) {
    // Test to see if eval works
    if (goog.evalWorksForGlobals_ == null) {
      goog.global.eval('var _et_ = 1;');
      if (typeof goog.global['_et_'] != 'undefined') {
        delete goog.global['_et_'];
        goog.evalWorksForGlobals_ = true;
      } else {
        goog.evalWorksForGlobals_ = false;
      }
    }

    if (goog.evalWorksForGlobals_) {
      goog.global.eval(script);
    } else {
      var doc = goog.global.document;
      var scriptElt = doc.createElement('script');
      scriptElt.type = 'text/javascript';
      scriptElt.defer = false;
      // Note(user): can't use .innerHTML since "t('<test>')" will fail and
      // .text doesn't work in Safari 2.  Therefore we append a text node.
      scriptElt.appendChild(doc.createTextNode(script));
      doc.body.appendChild(scriptElt);
      doc.body.removeChild(scriptElt);
    }
  } else {
    throw Error('goog.globalEval not available');
  }
};


/**
 * Indicates whether or not we can call 'eval' directly to eval code in the
 * global scope. Set to a Boolean by the first call to goog.globalEval (which
 * empirically tests whether eval works for globals). @see goog.globalEval
 * @type {?boolean}
 * @private
 */
goog.evalWorksForGlobals_ = null;


/**
 * Optional map of CSS class names to obfuscated names used with
 * goog.getCssName().
 * @type {Object|undefined}
 * @private
 * @see goog.setCssNameMapping
 */
goog.cssNameMapping_;


/**
 * Optional obfuscation style for CSS class names. Should be set to either
 * 'BY_WHOLE' or 'BY_PART' if defined.
 * @type {string|undefined}
 * @private
 * @see goog.setCssNameMapping
 */
goog.cssNameMappingStyle_;


/**
 * Handles strings that are intended to be used as CSS class names.
 *
 * This function works in tandem with @see goog.setCssNameMapping.
 *
 * Without any mapping set, the arguments are simple joined with a hyphen and
 * passed through unaltered.
 *
 * When there is a mapping, there are two possible styles in which these
 * mappings are used. In the BY_PART style, each part (i.e. in between hyphens)
 * of the passed in css name is rewritten according to the map. In the BY_WHOLE
 * style, the full css name is looked up in the map directly. If a rewrite is
 * not specified by the map, the compiler will output a warning.
 *
 * When the mapping is passed to the compiler, it will replace calls to
 * goog.getCssName with the strings from the mapping, e.g.
 *     var x = goog.getCssName('foo');
 *     var y = goog.getCssName(this.baseClass, 'active');
 *  becomes:
 *     var x= 'foo';
 *     var y = this.baseClass + '-active';
 *
 * If one argument is passed it will be processed, if two are passed only the
 * modifier will be processed, as it is assumed the first argument was generated
 * as a result of calling goog.getCssName.
 *
 * @param {string} className The class name.
 * @param {string=} opt_modifier A modifier to be appended to the class name.
 * @return {string} The class name or the concatenation of the class name and
 *     the modifier.
 */
goog.getCssName = function(className, opt_modifier) {
  var getMapping = function(cssName) {
    return goog.cssNameMapping_[cssName] || cssName;
  };

  var renameByParts = function(cssName) {
    // Remap all the parts individually.
    var parts = cssName.split('-');
    var mapped = [];
    for (var i = 0; i < parts.length; i++) {
      mapped.push(getMapping(parts[i]));
    }
    return mapped.join('-');
  };

  var rename;
  if (goog.cssNameMapping_) {
    rename = goog.cssNameMappingStyle_ == 'BY_WHOLE' ?
        getMapping : renameByParts;
  } else {
    rename = function(a) {
      return a;
    };
  }

  if (opt_modifier) {
    return className + '-' + rename(opt_modifier);
  } else {
    return rename(className);
  }
};


/**
 * Sets the map to check when returning a value from goog.getCssName(). Example:
 * <pre>
 * goog.setCssNameMapping({
 *   "goog": "a",
 *   "disabled": "b",
 * });
 *
 * var x = goog.getCssName('goog');
 * // The following evaluates to: "a a-b".
 * goog.getCssName('goog') + ' ' + goog.getCssName(x, 'disabled')
 * </pre>
 * When declared as a map of string literals to string literals, the JSCompiler
 * will replace all calls to goog.getCssName() using the supplied map if the
 * --closure_pass flag is set.
 *
 * @param {!Object} mapping A map of strings to strings where keys are possible
 *     arguments to goog.getCssName() and values are the corresponding values
 *     that should be returned.
 * @param {string=} opt_style The style of css name mapping. There are two valid
 *     options: 'BY_PART', and 'BY_WHOLE'.
 * @see goog.getCssName for a description.
 */
goog.setCssNameMapping = function(mapping, opt_style) {
  goog.cssNameMapping_ = mapping;
  goog.cssNameMappingStyle_ = opt_style;
};


/**
 * To use CSS renaming in compiled mode, one of the input files should have a
 * call to goog.setCssNameMapping() with an object literal that the JSCompiler
 * can extract and use to replace all calls to goog.getCssName(). In uncompiled
 * mode, JavaScript code should be loaded before this base.js file that declares
 * a global variable, CLOSURE_CSS_NAME_MAPPING, which is used below. This is
 * to ensure that the mapping is loaded before any calls to goog.getCssName()
 * are made in uncompiled mode.
 *
 * A hook for overriding the CSS name mapping.
 * @type {Object|undefined}
 */
goog.global.CLOSURE_CSS_NAME_MAPPING;


if (!COMPILED && goog.global.CLOSURE_CSS_NAME_MAPPING) {
  // This does not call goog.setCssNameMapping() because the JSCompiler
  // requires that goog.setCssNameMapping() be called with an object literal.
  goog.cssNameMapping_ = goog.global.CLOSURE_CSS_NAME_MAPPING;
}


/**
 * Gets a localized message.
 *
 * This function is a compiler primitive. If you give the compiler a localized
 * message bundle, it will replace the string at compile-time with a localized
 * version, and expand goog.getMsg call to a concatenated string.
 *
 * Messages must be initialized in the form:
 * <code>
 * var MSG_NAME = goog.getMsg('Hello {$placeholder}', {'placeholder': 'world'});
 * </code>
 *
 * @param {string} str Translatable string, places holders in the form {$foo}.
 * @param {Object=} opt_values Map of place holder name to value.
 * @return {string} message with placeholders filled.
 */
goog.getMsg = function(str, opt_values) {
  if (opt_values) {
    str = str.replace(/\{\$([^}]+)}/g, function(match, key) {
      return key in opt_values ? opt_values[key] : match;
    });
  }
  return str;
};


/**
 * Gets a localized message. If the message does not have a translation, gives a
 * fallback message.
 *
 * This is useful when introducing a new message that has not yet been
 * translated into all languages.
 *
 * This function is a compiler primitive. Must be used in the form:
 * <code>var x = goog.getMsgWithFallback(MSG_A, MSG_B);</code>
 * where MSG_A and MSG_B were initialized with goog.getMsg.
 *
 * @param {string} a The preferred message.
 * @param {string} b The fallback message.
 * @return {string} The best translated message.
 */
goog.getMsgWithFallback = function(a, b) {
  return a;
};


/**
 * Exposes an unobfuscated global namespace path for the given object.
 * Note that fields of the exported object *will* be obfuscated, unless they are
 * exported in turn via this function or goog.exportProperty.
 *
 * Also handy for making public items that are defined in anonymous closures.
 *
 * ex. goog.exportSymbol('public.path.Foo', Foo);
 *
 * ex. goog.exportSymbol('public.path.Foo.staticFunction', Foo.staticFunction);
 *     public.path.Foo.staticFunction();
 *
 * ex. goog.exportSymbol('public.path.Foo.prototype.myMethod',
 *                       Foo.prototype.myMethod);
 *     new public.path.Foo().myMethod();
 *
 * @param {string} publicPath Unobfuscated name to export.
 * @param {*} object Object the name should point to.
 * @param {Object=} opt_objectToExportTo The object to add the path to; default
 *     is goog.global.
 */
goog.exportSymbol = function(publicPath, object, opt_objectToExportTo) {
  goog.exportPath_(publicPath, object, opt_objectToExportTo);
};


/**
 * Exports a property unobfuscated into the object's namespace.
 * ex. goog.exportProperty(Foo, 'staticFunction', Foo.staticFunction);
 * ex. goog.exportProperty(Foo.prototype, 'myMethod', Foo.prototype.myMethod);
 * @param {Object} object Object whose static property is being exported.
 * @param {string} publicName Unobfuscated name to export.
 * @param {*} symbol Object the name should point to.
 */
goog.exportProperty = function(object, publicName, symbol) {
  object[publicName] = symbol;
};


/**
 * Inherit the prototype methods from one constructor into another.
 *
 * Usage:
 * <pre>
 * function ParentClass(a, b) { }
 * ParentClass.prototype.foo = function(a) { };
 *
 * function ChildClass(a, b, c) {
 *   ChildClass.base(this, 'constructor', a, b);
 * }
 * goog.inherits(ChildClass, ParentClass);
 *
 * var child = new ChildClass('a', 'b', 'see');
 * child.foo(); // This works.
 * </pre>
 *
 * @param {Function} childCtor Child class.
 * @param {Function} parentCtor Parent class.
 */
goog.inherits = function(childCtor, parentCtor) {
  /** @constructor */
  function tempCtor() {};
  tempCtor.prototype = parentCtor.prototype;
  childCtor.superClass_ = parentCtor.prototype;
  childCtor.prototype = new tempCtor();
  /** @override */
  childCtor.prototype.constructor = childCtor;

  /**
   * Calls superclass constructor/method.
   *
   * This function is only available if you use goog.inherits to
   * express inheritance relationships between classes.
   *
   * NOTE: This is a replacement for goog.base and for superClass_
   * property defined in childCtor.
   *
   * @param {!Object} me Should always be "this".
   * @param {string} methodName The method name to call. Calling
   *     superclass constructor can be done with the special string
   *     'constructor'.
   * @param {...*} var_args The arguments to pass to superclass
   *     method/constructor.
   * @return {*} The return value of the superclass method/constructor.
   */
  childCtor.base = function(me, methodName, var_args) {
    var args = Array.prototype.slice.call(arguments, 2);
    return parentCtor.prototype[methodName].apply(me, args);
  };
};


/**
 * Call up to the superclass.
 *
 * If this is called from a constructor, then this calls the superclass
 * constructor with arguments 1-N.
 *
 * If this is called from a prototype method, then you must pass the name of the
 * method as the second argument to this function. If you do not, you will get a
 * runtime error. This calls the superclass' method with arguments 2-N.
 *
 * This function only works if you use goog.inherits to express inheritance
 * relationships between your classes.
 *
 * This function is a compiler primitive. At compile-time, the compiler will do
 * macro expansion to remove a lot of the extra overhead that this function
 * introduces. The compiler will also enforce a lot of the assumptions that this
 * function makes, and treat it as a compiler error if you break them.
 *
 * @param {!Object} me Should always be "this".
 * @param {*=} opt_methodName The method name if calling a super method.
 * @param {...*} var_args The rest of the arguments.
 * @return {*} The return value of the superclass method.
 * @suppress {es5Strict} This method can not be used in strict mode, but
 *     all Closure Library consumers must depend on this file.
 */
goog.base = function(me, opt_methodName, var_args) {
  var caller = arguments.callee.caller;

  if (goog.STRICT_MODE_COMPATIBLE || (goog.DEBUG && !caller)) {
    throw Error('arguments.caller not defined.  goog.base() cannot be used ' +
                'with strict mode code. See ' +
                'http://www.ecma-international.org/ecma-262/5.1/#sec-C');
  }

  if (caller.superClass_) {
    // This is a constructor. Call the superclass constructor.
    return caller.superClass_.constructor.apply(
        me, Array.prototype.slice.call(arguments, 1));
  }

  var args = Array.prototype.slice.call(arguments, 2);
  var foundCaller = false;
  for (var ctor = me.constructor;
       ctor; ctor = ctor.superClass_ && ctor.superClass_.constructor) {
    if (ctor.prototype[opt_methodName] === caller) {
      foundCaller = true;
    } else if (foundCaller) {
      return ctor.prototype[opt_methodName].apply(me, args);
    }
  }

  // If we did not find the caller in the prototype chain, then one of two
  // things happened:
  // 1) The caller is an instance method.
  // 2) This method was not called by the right caller.
  if (me[opt_methodName] === caller) {
    return me.constructor.prototype[opt_methodName].apply(me, args);
  } else {
    throw Error(
        'goog.base called from a method of one name ' +
        'to a method of a different name');
  }
};


/**
 * Allow for aliasing within scope functions.  This function exists for
 * uncompiled code - in compiled code the calls will be inlined and the aliases
 * applied.  In uncompiled code the function is simply run since the aliases as
 * written are valid JavaScript.
 * @param {function()} fn Function to call.  This function can contain aliases
 *     to namespaces (e.g. "var dom = goog.dom") or classes
 *     (e.g. "var Timer = goog.Timer").
 */
goog.scope = function(fn) {
  fn.call(goog.global);
};


/*
 * To support uncompiled, strict mode bundles that use eval to divide source
 * like so:
 *    eval('someSource;//# sourceUrl sourcefile.js');
 * We need to export the globally defined symbols "goog" and "COMPILED".
 * Exporting "goog" breaks the compiler optimizations, so we required that
 * be defined externally.
 * NOTE: We don't use goog.exportSymbol here because we don't want to trigger
 * extern generation when that compiler option is enabled.
 */
if (!COMPILED) {
  goog.global['COMPILED'] = COMPILED;
}



goog.provide('olgm.Abstract');



/**
 * Abstract class for most classes of this library, which receives a reference
 * to the `google.maps.Map` and `ol.Map` objects an behave accordingly with
 * them.
 *
 * @param {!ol.Map} ol3map
 * @param {!google.maps.Map} gmap
 * @constructor
 */
olgm.Abstract = function(ol3map, gmap) {

  /**
   * @type {!ol.Map}
   * @protected
   */
  this.ol3map = ol3map;

  /**
   * @type {!google.maps.Map}
   * @protected
   */
  this.gmap = gmap;

};

// Copyright 2009 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Provides a base class for custom Error objects such that the
 * stack is correctly maintained.
 *
 * You should never need to throw goog.debug.Error(msg) directly, Error(msg) is
 * sufficient.
 *
 */

goog.provide('goog.debug.Error');



/**
 * Base class for custom error objects.
 * @param {*=} opt_msg The message associated with the error.
 * @constructor
 * @extends {Error}
 */
goog.debug.Error = function(opt_msg) {

  // Attempt to ensure there is a stack trace.
  if (Error.captureStackTrace) {
    Error.captureStackTrace(this, goog.debug.Error);
  } else {
    var stack = new Error().stack;
    if (stack) {
      this.stack = stack;
    }
  }

  if (opt_msg) {
    this.message = String(opt_msg);
  }
};
goog.inherits(goog.debug.Error, Error);


/** @override */
goog.debug.Error.prototype.name = 'CustomError';

// Copyright 2006 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Definition of goog.dom.NodeType.
 */

goog.provide('goog.dom.NodeType');


/**
 * Constants for the nodeType attribute in the Node interface.
 *
 * These constants match those specified in the Node interface. These are
 * usually present on the Node object in recent browsers, but not in older
 * browsers (specifically, early IEs) and thus are given here.
 *
 * In some browsers (early IEs), these are not defined on the Node object,
 * so they are provided here.
 *
 * See http://www.w3.org/TR/DOM-Level-2-Core/core.html#ID-1950641247
 * @enum {number}
 */
goog.dom.NodeType = {
  ELEMENT: 1,
  ATTRIBUTE: 2,
  TEXT: 3,
  CDATA_SECTION: 4,
  ENTITY_REFERENCE: 5,
  ENTITY: 6,
  PROCESSING_INSTRUCTION: 7,
  COMMENT: 8,
  DOCUMENT: 9,
  DOCUMENT_TYPE: 10,
  DOCUMENT_FRAGMENT: 11,
  NOTATION: 12
};

// Copyright 2006 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Utilities for string manipulation.
 */


/**
 * Namespace for string utilities
 */
goog.provide('goog.string');
goog.provide('goog.string.Unicode');


/**
 * @define {boolean} Enables HTML escaping of lowercase letter "e" which helps
 * with detection of double-escaping as this letter is frequently used.
 */
goog.define('goog.string.DETECT_DOUBLE_ESCAPING', false);


/**
 * Common Unicode string characters.
 * @enum {string}
 */
goog.string.Unicode = {
  NBSP: '\xa0'
};


/**
 * Fast prefix-checker.
 * @param {string} str The string to check.
 * @param {string} prefix A string to look for at the start of {@code str}.
 * @return {boolean} True if {@code str} begins with {@code prefix}.
 */
goog.string.startsWith = function(str, prefix) {
  return str.lastIndexOf(prefix, 0) == 0;
};


/**
 * Fast suffix-checker.
 * @param {string} str The string to check.
 * @param {string} suffix A string to look for at the end of {@code str}.
 * @return {boolean} True if {@code str} ends with {@code suffix}.
 */
goog.string.endsWith = function(str, suffix) {
  var l = str.length - suffix.length;
  return l >= 0 && str.indexOf(suffix, l) == l;
};


/**
 * Case-insensitive prefix-checker.
 * @param {string} str The string to check.
 * @param {string} prefix  A string to look for at the end of {@code str}.
 * @return {boolean} True if {@code str} begins with {@code prefix} (ignoring
 *     case).
 */
goog.string.caseInsensitiveStartsWith = function(str, prefix) {
  return goog.string.caseInsensitiveCompare(
      prefix, str.substr(0, prefix.length)) == 0;
};


/**
 * Case-insensitive suffix-checker.
 * @param {string} str The string to check.
 * @param {string} suffix A string to look for at the end of {@code str}.
 * @return {boolean} True if {@code str} ends with {@code suffix} (ignoring
 *     case).
 */
goog.string.caseInsensitiveEndsWith = function(str, suffix) {
  return goog.string.caseInsensitiveCompare(
      suffix, str.substr(str.length - suffix.length, suffix.length)) == 0;
};


/**
 * Case-insensitive equality checker.
 * @param {string} str1 First string to check.
 * @param {string} str2 Second string to check.
 * @return {boolean} True if {@code str1} and {@code str2} are the same string,
 *     ignoring case.
 */
goog.string.caseInsensitiveEquals = function(str1, str2) {
  return str1.toLowerCase() == str2.toLowerCase();
};


/**
 * Does simple python-style string substitution.
 * subs("foo%s hot%s", "bar", "dog") becomes "foobar hotdog".
 * @param {string} str The string containing the pattern.
 * @param {...*} var_args The items to substitute into the pattern.
 * @return {string} A copy of {@code str} in which each occurrence of
 *     {@code %s} has been replaced an argument from {@code var_args}.
 */
goog.string.subs = function(str, var_args) {
  var splitParts = str.split('%s');
  var returnString = '';

  var subsArguments = Array.prototype.slice.call(arguments, 1);
  while (subsArguments.length &&
         // Replace up to the last split part. We are inserting in the
         // positions between split parts.
         splitParts.length > 1) {
    returnString += splitParts.shift() + subsArguments.shift();
  }

  return returnString + splitParts.join('%s'); // Join unused '%s'
};


/**
 * Converts multiple whitespace chars (spaces, non-breaking-spaces, new lines
 * and tabs) to a single space, and strips leading and trailing whitespace.
 * @param {string} str Input string.
 * @return {string} A copy of {@code str} with collapsed whitespace.
 */
goog.string.collapseWhitespace = function(str) {
  // Since IE doesn't include non-breaking-space (0xa0) in their \s character
  // class (as required by section 7.2 of the ECMAScript spec), we explicitly
  // include it in the regexp to enforce consistent cross-browser behavior.
  return str.replace(/[\s\xa0]+/g, ' ').replace(/^\s+|\s+$/g, '');
};


/**
 * Checks if a string is empty or contains only whitespaces.
 * @param {string} str The string to check.
 * @return {boolean} True if {@code str} is empty or whitespace only.
 */
goog.string.isEmpty = function(str) {
  // testing length == 0 first is actually slower in all browsers (about the
  // same in Opera).
  // Since IE doesn't include non-breaking-space (0xa0) in their \s character
  // class (as required by section 7.2 of the ECMAScript spec), we explicitly
  // include it in the regexp to enforce consistent cross-browser behavior.
  return /^[\s\xa0]*$/.test(str);
};


/**
 * Checks if a string is null, undefined, empty or contains only whitespaces.
 * @param {*} str The string to check.
 * @return {boolean} True if{@code str} is null, undefined, empty, or
 *     whitespace only.
 */
goog.string.isEmptySafe = function(str) {
  return goog.string.isEmpty(goog.string.makeSafe(str));
};


/**
 * Checks if a string is all breaking whitespace.
 * @param {string} str The string to check.
 * @return {boolean} Whether the string is all breaking whitespace.
 */
goog.string.isBreakingWhitespace = function(str) {
  return !/[^\t\n\r ]/.test(str);
};


/**
 * Checks if a string contains all letters.
 * @param {string} str string to check.
 * @return {boolean} True if {@code str} consists entirely of letters.
 */
goog.string.isAlpha = function(str) {
  return !/[^a-zA-Z]/.test(str);
};


/**
 * Checks if a string contains only numbers.
 * @param {*} str string to check. If not a string, it will be
 *     casted to one.
 * @return {boolean} True if {@code str} is numeric.
 */
goog.string.isNumeric = function(str) {
  return !/[^0-9]/.test(str);
};


/**
 * Checks if a string contains only numbers or letters.
 * @param {string} str string to check.
 * @return {boolean} True if {@code str} is alphanumeric.
 */
goog.string.isAlphaNumeric = function(str) {
  return !/[^a-zA-Z0-9]/.test(str);
};


/**
 * Checks if a character is a space character.
 * @param {string} ch Character to check.
 * @return {boolean} True if {code ch} is a space.
 */
goog.string.isSpace = function(ch) {
  return ch == ' ';
};


/**
 * Checks if a character is a valid unicode character.
 * @param {string} ch Character to check.
 * @return {boolean} True if {code ch} is a valid unicode character.
 */
goog.string.isUnicodeChar = function(ch) {
  return ch.length == 1 && ch >= ' ' && ch <= '~' ||
         ch >= '\u0080' && ch <= '\uFFFD';
};


/**
 * Takes a string and replaces newlines with a space. Multiple lines are
 * replaced with a single space.
 * @param {string} str The string from which to strip newlines.
 * @return {string} A copy of {@code str} stripped of newlines.
 */
goog.string.stripNewlines = function(str) {
  return str.replace(/(\r\n|\r|\n)+/g, ' ');
};


/**
 * Replaces Windows and Mac new lines with unix style: \r or \r\n with \n.
 * @param {string} str The string to in which to canonicalize newlines.
 * @return {string} {@code str} A copy of {@code} with canonicalized newlines.
 */
goog.string.canonicalizeNewlines = function(str) {
  return str.replace(/(\r\n|\r|\n)/g, '\n');
};


/**
 * Normalizes whitespace in a string, replacing all whitespace chars with
 * a space.
 * @param {string} str The string in which to normalize whitespace.
 * @return {string} A copy of {@code str} with all whitespace normalized.
 */
goog.string.normalizeWhitespace = function(str) {
  return str.replace(/\xa0|\s/g, ' ');
};


/**
 * Normalizes spaces in a string, replacing all consecutive spaces and tabs
 * with a single space. Replaces non-breaking space with a space.
 * @param {string} str The string in which to normalize spaces.
 * @return {string} A copy of {@code str} with all consecutive spaces and tabs
 *    replaced with a single space.
 */
goog.string.normalizeSpaces = function(str) {
  return str.replace(/\xa0|[ \t]+/g, ' ');
};


/**
 * Removes the breaking spaces from the left and right of the string and
 * collapses the sequences of breaking spaces in the middle into single spaces.
 * The original and the result strings render the same way in HTML.
 * @param {string} str A string in which to collapse spaces.
 * @return {string} Copy of the string with normalized breaking spaces.
 */
goog.string.collapseBreakingSpaces = function(str) {
  return str.replace(/[\t\r\n ]+/g, ' ').replace(
      /^[\t\r\n ]+|[\t\r\n ]+$/g, '');
};


/**
 * Trims white spaces to the left and right of a string.
 * @param {string} str The string to trim.
 * @return {string} A trimmed copy of {@code str}.
 */
goog.string.trim = function(str) {
  // Since IE doesn't include non-breaking-space (0xa0) in their \s character
  // class (as required by section 7.2 of the ECMAScript spec), we explicitly
  // include it in the regexp to enforce consistent cross-browser behavior.
  return str.replace(/^[\s\xa0]+|[\s\xa0]+$/g, '');
};


/**
 * Trims whitespaces at the left end of a string.
 * @param {string} str The string to left trim.
 * @return {string} A trimmed copy of {@code str}.
 */
goog.string.trimLeft = function(str) {
  // Since IE doesn't include non-breaking-space (0xa0) in their \s character
  // class (as required by section 7.2 of the ECMAScript spec), we explicitly
  // include it in the regexp to enforce consistent cross-browser behavior.
  return str.replace(/^[\s\xa0]+/, '');
};


/**
 * Trims whitespaces at the right end of a string.
 * @param {string} str The string to right trim.
 * @return {string} A trimmed copy of {@code str}.
 */
goog.string.trimRight = function(str) {
  // Since IE doesn't include non-breaking-space (0xa0) in their \s character
  // class (as required by section 7.2 of the ECMAScript spec), we explicitly
  // include it in the regexp to enforce consistent cross-browser behavior.
  return str.replace(/[\s\xa0]+$/, '');
};


/**
 * A string comparator that ignores case.
 * -1 = str1 less than str2
 *  0 = str1 equals str2
 *  1 = str1 greater than str2
 *
 * @param {string} str1 The string to compare.
 * @param {string} str2 The string to compare {@code str1} to.
 * @return {number} The comparator result, as described above.
 */
goog.string.caseInsensitiveCompare = function(str1, str2) {
  var test1 = String(str1).toLowerCase();
  var test2 = String(str2).toLowerCase();

  if (test1 < test2) {
    return -1;
  } else if (test1 == test2) {
    return 0;
  } else {
    return 1;
  }
};


/**
 * Regular expression used for splitting a string into substrings of fractional
 * numbers, integers, and non-numeric characters.
 * @type {RegExp}
 * @private
 */
goog.string.numerateCompareRegExp_ = /(\.\d+)|(\d+)|(\D+)/g;


/**
 * String comparison function that handles numbers in a way humans might expect.
 * Using this function, the string "File 2.jpg" sorts before "File 10.jpg". The
 * comparison is mostly case-insensitive, though strings that are identical
 * except for case are sorted with the upper-case strings before lower-case.
 *
 * This comparison function is significantly slower (about 500x) than either
 * the default or the case-insensitive compare. It should not be used in
 * time-critical code, but should be fast enough to sort several hundred short
 * strings (like filenames) with a reasonable delay.
 *
 * @param {string} str1 The string to compare in a numerically sensitive way.
 * @param {string} str2 The string to compare {@code str1} to.
 * @return {number} less than 0 if str1 < str2, 0 if str1 == str2, greater than
 *     0 if str1 > str2.
 */
goog.string.numerateCompare = function(str1, str2) {
  if (str1 == str2) {
    return 0;
  }
  if (!str1) {
    return -1;
  }
  if (!str2) {
    return 1;
  }

  // Using match to split the entire string ahead of time turns out to be faster
  // for most inputs than using RegExp.exec or iterating over each character.
  var tokens1 = str1.toLowerCase().match(goog.string.numerateCompareRegExp_);
  var tokens2 = str2.toLowerCase().match(goog.string.numerateCompareRegExp_);

  var count = Math.min(tokens1.length, tokens2.length);

  for (var i = 0; i < count; i++) {
    var a = tokens1[i];
    var b = tokens2[i];

    // Compare pairs of tokens, returning if one token sorts before the other.
    if (a != b) {

      // Only if both tokens are integers is a special comparison required.
      // Decimal numbers are sorted as strings (e.g., '.09' < '.1').
      var num1 = parseInt(a, 10);
      if (!isNaN(num1)) {
        var num2 = parseInt(b, 10);
        if (!isNaN(num2) && num1 - num2) {
          return num1 - num2;
        }
      }
      return a < b ? -1 : 1;
    }
  }

  // If one string is a substring of the other, the shorter string sorts first.
  if (tokens1.length != tokens2.length) {
    return tokens1.length - tokens2.length;
  }

  // The two strings must be equivalent except for case (perfect equality is
  // tested at the head of the function.) Revert to default ASCII-betical string
  // comparison to stablize the sort.
  return str1 < str2 ? -1 : 1;
};


/**
 * URL-encodes a string
 * @param {*} str The string to url-encode.
 * @return {string} An encoded copy of {@code str} that is safe for urls.
 *     Note that '#', ':', and other characters used to delimit portions
 *     of URLs *will* be encoded.
 */
goog.string.urlEncode = function(str) {
  return encodeURIComponent(String(str));
};


/**
 * URL-decodes the string. We need to specially handle '+'s because
 * the javascript library doesn't convert them to spaces.
 * @param {string} str The string to url decode.
 * @return {string} The decoded {@code str}.
 */
goog.string.urlDecode = function(str) {
  return decodeURIComponent(str.replace(/\+/g, ' '));
};


/**
 * Converts \n to <br>s or <br />s.
 * @param {string} str The string in which to convert newlines.
 * @param {boolean=} opt_xml Whether to use XML compatible tags.
 * @return {string} A copy of {@code str} with converted newlines.
 */
goog.string.newLineToBr = function(str, opt_xml) {
  return str.replace(/(\r\n|\r|\n)/g, opt_xml ? '<br />' : '<br>');
};


/**
 * Escapes double quote '"' and single quote '\'' characters in addition to
 * '&', '<', and '>' so that a string can be included in an HTML tag attribute
 * value within double or single quotes.
 *
 * It should be noted that > doesn't need to be escaped for the HTML or XML to
 * be valid, but it has been decided to escape it for consistency with other
 * implementations.
 *
 * With goog.string.DETECT_DOUBLE_ESCAPING, this function escapes also the
 * lowercase letter "e".
 *
 * NOTE(user):
 * HtmlEscape is often called during the generation of large blocks of HTML.
 * Using statics for the regular expressions and strings is an optimization
 * that can more than half the amount of time IE spends in this function for
 * large apps, since strings and regexes both contribute to GC allocations.
 *
 * Testing for the presence of a character before escaping increases the number
 * of function calls, but actually provides a speed increase for the average
 * case -- since the average case often doesn't require the escaping of all 4
 * characters and indexOf() is much cheaper than replace().
 * The worst case does suffer slightly from the additional calls, therefore the
 * opt_isLikelyToContainHtmlChars option has been included for situations
 * where all 4 HTML entities are very likely to be present and need escaping.
 *
 * Some benchmarks (times tended to fluctuate +-0.05ms):
 *                                     FireFox                     IE6
 * (no chars / average (mix of cases) / all 4 chars)
 * no checks                     0.13 / 0.22 / 0.22         0.23 / 0.53 / 0.80
 * indexOf                       0.08 / 0.17 / 0.26         0.22 / 0.54 / 0.84
 * indexOf + re test             0.07 / 0.17 / 0.28         0.19 / 0.50 / 0.85
 *
 * An additional advantage of checking if replace actually needs to be called
 * is a reduction in the number of object allocations, so as the size of the
 * application grows the difference between the various methods would increase.
 *
 * @param {string} str string to be escaped.
 * @param {boolean=} opt_isLikelyToContainHtmlChars Don't perform a check to see
 *     if the character needs replacing - use this option if you expect each of
 *     the characters to appear often. Leave false if you expect few html
 *     characters to occur in your strings, such as if you are escaping HTML.
 * @return {string} An escaped copy of {@code str}.
 */
goog.string.htmlEscape = function(str, opt_isLikelyToContainHtmlChars) {

  if (opt_isLikelyToContainHtmlChars) {
    str = str.replace(goog.string.AMP_RE_, '&amp;')
          .replace(goog.string.LT_RE_, '&lt;')
          .replace(goog.string.GT_RE_, '&gt;')
          .replace(goog.string.QUOT_RE_, '&quot;')
          .replace(goog.string.SINGLE_QUOTE_RE_, '&#39;')
          .replace(goog.string.NULL_RE_, '&#0;');
    if (goog.string.DETECT_DOUBLE_ESCAPING) {
      str = str.replace(goog.string.E_RE_, '&#101;');
    }
    return str;

  } else {
    // quick test helps in the case when there are no chars to replace, in
    // worst case this makes barely a difference to the time taken
    if (!goog.string.ALL_RE_.test(str)) return str;

    // str.indexOf is faster than regex.test in this case
    if (str.indexOf('&') != -1) {
      str = str.replace(goog.string.AMP_RE_, '&amp;');
    }
    if (str.indexOf('<') != -1) {
      str = str.replace(goog.string.LT_RE_, '&lt;');
    }
    if (str.indexOf('>') != -1) {
      str = str.replace(goog.string.GT_RE_, '&gt;');
    }
    if (str.indexOf('"') != -1) {
      str = str.replace(goog.string.QUOT_RE_, '&quot;');
    }
    if (str.indexOf('\'') != -1) {
      str = str.replace(goog.string.SINGLE_QUOTE_RE_, '&#39;');
    }
    if (str.indexOf('\x00') != -1) {
      str = str.replace(goog.string.NULL_RE_, '&#0;');
    }
    if (goog.string.DETECT_DOUBLE_ESCAPING && str.indexOf('e') != -1) {
      str = str.replace(goog.string.E_RE_, '&#101;');
    }
    return str;
  }
};


/**
 * Regular expression that matches an ampersand, for use in escaping.
 * @const {!RegExp}
 * @private
 */
goog.string.AMP_RE_ = /&/g;


/**
 * Regular expression that matches a less than sign, for use in escaping.
 * @const {!RegExp}
 * @private
 */
goog.string.LT_RE_ = /</g;


/**
 * Regular expression that matches a greater than sign, for use in escaping.
 * @const {!RegExp}
 * @private
 */
goog.string.GT_RE_ = />/g;


/**
 * Regular expression that matches a double quote, for use in escaping.
 * @const {!RegExp}
 * @private
 */
goog.string.QUOT_RE_ = /"/g;


/**
 * Regular expression that matches a single quote, for use in escaping.
 * @const {!RegExp}
 * @private
 */
goog.string.SINGLE_QUOTE_RE_ = /'/g;


/**
 * Regular expression that matches null character, for use in escaping.
 * @const {!RegExp}
 * @private
 */
goog.string.NULL_RE_ = /\x00/g;


/**
 * Regular expression that matches a lowercase letter "e", for use in escaping.
 * @const {!RegExp}
 * @private
 */
goog.string.E_RE_ = /e/g;


/**
 * Regular expression that matches any character that needs to be escaped.
 * @const {!RegExp}
 * @private
 */
goog.string.ALL_RE_ = (goog.string.DETECT_DOUBLE_ESCAPING ?
    /[\x00&<>"'e]/ :
    /[\x00&<>"']/);


/**
 * Unescapes an HTML string.
 *
 * @param {string} str The string to unescape.
 * @return {string} An unescaped copy of {@code str}.
 */
goog.string.unescapeEntities = function(str) {
  if (goog.string.contains(str, '&')) {
    // We are careful not to use a DOM if we do not have one. We use the []
    // notation so that the JSCompiler will not complain about these objects and
    // fields in the case where we have no DOM.
    if ('document' in goog.global) {
      return goog.string.unescapeEntitiesUsingDom_(str);
    } else {
      // Fall back on pure XML entities
      return goog.string.unescapePureXmlEntities_(str);
    }
  }
  return str;
};


/**
 * Unescapes a HTML string using the provided document.
 *
 * @param {string} str The string to unescape.
 * @param {!Document} document A document to use in escaping the string.
 * @return {string} An unescaped copy of {@code str}.
 */
goog.string.unescapeEntitiesWithDocument = function(str, document) {
  if (goog.string.contains(str, '&')) {
    return goog.string.unescapeEntitiesUsingDom_(str, document);
  }
  return str;
};


/**
 * Unescapes an HTML string using a DOM to resolve non-XML, non-numeric
 * entities. This function is XSS-safe and whitespace-preserving.
 * @private
 * @param {string} str The string to unescape.
 * @param {Document=} opt_document An optional document to use for creating
 *     elements. If this is not specified then the default window.document
 *     will be used.
 * @return {string} The unescaped {@code str} string.
 */
goog.string.unescapeEntitiesUsingDom_ = function(str, opt_document) {
  var seen = {'&amp;': '&', '&lt;': '<', '&gt;': '>', '&quot;': '"'};
  var div;
  if (opt_document) {
    div = opt_document.createElement('div');
  } else {
    div = goog.global.document.createElement('div');
  }
  // Match as many valid entity characters as possible. If the actual entity
  // happens to be shorter, it will still work as innerHTML will return the
  // trailing characters unchanged. Since the entity characters do not include
  // open angle bracket, there is no chance of XSS from the innerHTML use.
  // Since no whitespace is passed to innerHTML, whitespace is preserved.
  return str.replace(goog.string.HTML_ENTITY_PATTERN_, function(s, entity) {
    // Check for cached entity.
    var value = seen[s];
    if (value) {
      return value;
    }
    // Check for numeric entity.
    if (entity.charAt(0) == '#') {
      // Prefix with 0 so that hex entities (e.g. &#x10) parse as hex numbers.
      var n = Number('0' + entity.substr(1));
      if (!isNaN(n)) {
        value = String.fromCharCode(n);
      }
    }
    // Fall back to innerHTML otherwise.
    if (!value) {
      // Append a non-entity character to avoid a bug in Webkit that parses
      // an invalid entity at the end of innerHTML text as the empty string.
      div.innerHTML = s + ' ';
      // Then remove the trailing character from the result.
      value = div.firstChild.nodeValue.slice(0, -1);
    }
    // Cache and return.
    return seen[s] = value;
  });
};


/**
 * Unescapes XML entities.
 * @private
 * @param {string} str The string to unescape.
 * @return {string} An unescaped copy of {@code str}.
 */
goog.string.unescapePureXmlEntities_ = function(str) {
  return str.replace(/&([^;]+);/g, function(s, entity) {
    switch (entity) {
      case 'amp':
        return '&';
      case 'lt':
        return '<';
      case 'gt':
        return '>';
      case 'quot':
        return '"';
      default:
        if (entity.charAt(0) == '#') {
          // Prefix with 0 so that hex entities (e.g. &#x10) parse as hex.
          var n = Number('0' + entity.substr(1));
          if (!isNaN(n)) {
            return String.fromCharCode(n);
          }
        }
        // For invalid entities we just return the entity
        return s;
    }
  });
};


/**
 * Regular expression that matches an HTML entity.
 * See also HTML5: Tokenization / Tokenizing character references.
 * @private
 * @type {!RegExp}
 */
goog.string.HTML_ENTITY_PATTERN_ = /&([^;\s<&]+);?/g;


/**
 * Do escaping of whitespace to preserve spatial formatting. We use character
 * entity #160 to make it safer for xml.
 * @param {string} str The string in which to escape whitespace.
 * @param {boolean=} opt_xml Whether to use XML compatible tags.
 * @return {string} An escaped copy of {@code str}.
 */
goog.string.whitespaceEscape = function(str, opt_xml) {
  // This doesn't use goog.string.preserveSpaces for backwards compatibility.
  return goog.string.newLineToBr(str.replace(/  /g, ' &#160;'), opt_xml);
};


/**
 * Preserve spaces that would be otherwise collapsed in HTML by replacing them
 * with non-breaking space Unicode characters.
 * @param {string} str The string in which to preserve whitespace.
 * @return {string} A copy of {@code str} with preserved whitespace.
 */
goog.string.preserveSpaces = function(str) {
  return str.replace(/(^|[\n ]) /g, '$1' + goog.string.Unicode.NBSP);
};


/**
 * Strip quote characters around a string.  The second argument is a string of
 * characters to treat as quotes.  This can be a single character or a string of
 * multiple character and in that case each of those are treated as possible
 * quote characters. For example:
 *
 * <pre>
 * goog.string.stripQuotes('"abc"', '"`') --> 'abc'
 * goog.string.stripQuotes('`abc`', '"`') --> 'abc'
 * </pre>
 *
 * @param {string} str The string to strip.
 * @param {string} quoteChars The quote characters to strip.
 * @return {string} A copy of {@code str} without the quotes.
 */
goog.string.stripQuotes = function(str, quoteChars) {
  var length = quoteChars.length;
  for (var i = 0; i < length; i++) {
    var quoteChar = length == 1 ? quoteChars : quoteChars.charAt(i);
    if (str.charAt(0) == quoteChar && str.charAt(str.length - 1) == quoteChar) {
      return str.substring(1, str.length - 1);
    }
  }
  return str;
};


/**
 * Truncates a string to a certain length and adds '...' if necessary.  The
 * length also accounts for the ellipsis, so a maximum length of 10 and a string
 * 'Hello World!' produces 'Hello W...'.
 * @param {string} str The string to truncate.
 * @param {number} chars Max number of characters.
 * @param {boolean=} opt_protectEscapedCharacters Whether to protect escaped
 *     characters from being cut off in the middle.
 * @return {string} The truncated {@code str} string.
 */
goog.string.truncate = function(str, chars, opt_protectEscapedCharacters) {
  if (opt_protectEscapedCharacters) {
    str = goog.string.unescapeEntities(str);
  }

  if (str.length > chars) {
    str = str.substring(0, chars - 3) + '...';
  }

  if (opt_protectEscapedCharacters) {
    str = goog.string.htmlEscape(str);
  }

  return str;
};


/**
 * Truncate a string in the middle, adding "..." if necessary,
 * and favoring the beginning of the string.
 * @param {string} str The string to truncate the middle of.
 * @param {number} chars Max number of characters.
 * @param {boolean=} opt_protectEscapedCharacters Whether to protect escaped
 *     characters from being cutoff in the middle.
 * @param {number=} opt_trailingChars Optional number of trailing characters to
 *     leave at the end of the string, instead of truncating as close to the
 *     middle as possible.
 * @return {string} A truncated copy of {@code str}.
 */
goog.string.truncateMiddle = function(str, chars,
    opt_protectEscapedCharacters, opt_trailingChars) {
  if (opt_protectEscapedCharacters) {
    str = goog.string.unescapeEntities(str);
  }

  if (opt_trailingChars && str.length > chars) {
    if (opt_trailingChars > chars) {
      opt_trailingChars = chars;
    }
    var endPoint = str.length - opt_trailingChars;
    var startPoint = chars - opt_trailingChars;
    str = str.substring(0, startPoint) + '...' + str.substring(endPoint);
  } else if (str.length > chars) {
    // Favor the beginning of the string:
    var half = Math.floor(chars / 2);
    var endPos = str.length - half;
    half += chars % 2;
    str = str.substring(0, half) + '...' + str.substring(endPos);
  }

  if (opt_protectEscapedCharacters) {
    str = goog.string.htmlEscape(str);
  }

  return str;
};


/**
 * Special chars that need to be escaped for goog.string.quote.
 * @private
 * @type {Object}
 */
goog.string.specialEscapeChars_ = {
  '\0': '\\0',
  '\b': '\\b',
  '\f': '\\f',
  '\n': '\\n',
  '\r': '\\r',
  '\t': '\\t',
  '\x0B': '\\x0B', // '\v' is not supported in JScript
  '"': '\\"',
  '\\': '\\\\'
};


/**
 * Character mappings used internally for goog.string.escapeChar.
 * @private
 * @type {Object}
 */
goog.string.jsEscapeCache_ = {
  '\'': '\\\''
};


/**
 * Encloses a string in double quotes and escapes characters so that the
 * string is a valid JS string.
 * @param {string} s The string to quote.
 * @return {string} A copy of {@code s} surrounded by double quotes.
 */
goog.string.quote = function(s) {
  s = String(s);
  if (s.quote) {
    return s.quote();
  } else {
    var sb = ['"'];
    for (var i = 0; i < s.length; i++) {
      var ch = s.charAt(i);
      var cc = ch.charCodeAt(0);
      sb[i + 1] = goog.string.specialEscapeChars_[ch] ||
          ((cc > 31 && cc < 127) ? ch : goog.string.escapeChar(ch));
    }
    sb.push('"');
    return sb.join('');
  }
};


/**
 * Takes a string and returns the escaped string for that character.
 * @param {string} str The string to escape.
 * @return {string} An escaped string representing {@code str}.
 */
goog.string.escapeString = function(str) {
  var sb = [];
  for (var i = 0; i < str.length; i++) {
    sb[i] = goog.string.escapeChar(str.charAt(i));
  }
  return sb.join('');
};


/**
 * Takes a character and returns the escaped string for that character. For
 * example escapeChar(String.fromCharCode(15)) -> "\\x0E".
 * @param {string} c The character to escape.
 * @return {string} An escaped string representing {@code c}.
 */
goog.string.escapeChar = function(c) {
  if (c in goog.string.jsEscapeCache_) {
    return goog.string.jsEscapeCache_[c];
  }

  if (c in goog.string.specialEscapeChars_) {
    return goog.string.jsEscapeCache_[c] = goog.string.specialEscapeChars_[c];
  }

  var rv = c;
  var cc = c.charCodeAt(0);
  if (cc > 31 && cc < 127) {
    rv = c;
  } else {
    // tab is 9 but handled above
    if (cc < 256) {
      rv = '\\x';
      if (cc < 16 || cc > 256) {
        rv += '0';
      }
    } else {
      rv = '\\u';
      if (cc < 4096) { // \u1000
        rv += '0';
      }
    }
    rv += cc.toString(16).toUpperCase();
  }

  return goog.string.jsEscapeCache_[c] = rv;
};


/**
 * Takes a string and creates a map (Object) in which the keys are the
 * characters in the string. The value for the key is set to true. You can
 * then use goog.object.map or goog.array.map to change the values.
 * @param {string} s The string to build the map from.
 * @return {!Object} The map of characters used.
 */
// TODO(arv): It seems like we should have a generic goog.array.toMap. But do
//            we want a dependency on goog.array in goog.string?
goog.string.toMap = function(s) {
  var rv = {};
  for (var i = 0; i < s.length; i++) {
    rv[s.charAt(i)] = true;
  }
  return rv;
};


/**
 * Determines whether a string contains a substring.
 * @param {string} str The string to search.
 * @param {string} subString The substring to search for.
 * @return {boolean} Whether {@code str} contains {@code subString}.
 */
goog.string.contains = function(str, subString) {
  return str.indexOf(subString) != -1;
};


/**
 * Determines whether a string contains a substring, ignoring case.
 * @param {string} str The string to search.
 * @param {string} subString The substring to search for.
 * @return {boolean} Whether {@code str} contains {@code subString}.
 */
goog.string.caseInsensitiveContains = function(str, subString) {
  return goog.string.contains(str.toLowerCase(), subString.toLowerCase());
};


/**
 * Returns the non-overlapping occurrences of ss in s.
 * If either s or ss evalutes to false, then returns zero.
 * @param {string} s The string to look in.
 * @param {string} ss The string to look for.
 * @return {number} Number of occurrences of ss in s.
 */
goog.string.countOf = function(s, ss) {
  return s && ss ? s.split(ss).length - 1 : 0;
};


/**
 * Removes a substring of a specified length at a specific
 * index in a string.
 * @param {string} s The base string from which to remove.
 * @param {number} index The index at which to remove the substring.
 * @param {number} stringLength The length of the substring to remove.
 * @return {string} A copy of {@code s} with the substring removed or the full
 *     string if nothing is removed or the input is invalid.
 */
goog.string.removeAt = function(s, index, stringLength) {
  var resultStr = s;
  // If the index is greater or equal to 0 then remove substring
  if (index >= 0 && index < s.length && stringLength > 0) {
    resultStr = s.substr(0, index) +
        s.substr(index + stringLength, s.length - index - stringLength);
  }
  return resultStr;
};


/**
 *  Removes the first occurrence of a substring from a string.
 *  @param {string} s The base string from which to remove.
 *  @param {string} ss The string to remove.
 *  @return {string} A copy of {@code s} with {@code ss} removed or the full
 *      string if nothing is removed.
 */
goog.string.remove = function(s, ss) {
  var re = new RegExp(goog.string.regExpEscape(ss), '');
  return s.replace(re, '');
};


/**
 *  Removes all occurrences of a substring from a string.
 *  @param {string} s The base string from which to remove.
 *  @param {string} ss The string to remove.
 *  @return {string} A copy of {@code s} with {@code ss} removed or the full
 *      string if nothing is removed.
 */
goog.string.removeAll = function(s, ss) {
  var re = new RegExp(goog.string.regExpEscape(ss), 'g');
  return s.replace(re, '');
};


/**
 * Escapes characters in the string that are not safe to use in a RegExp.
 * @param {*} s The string to escape. If not a string, it will be casted
 *     to one.
 * @return {string} A RegExp safe, escaped copy of {@code s}.
 */
goog.string.regExpEscape = function(s) {
  return String(s).replace(/([-()\[\]{}+?*.$\^|,:#<!\\])/g, '\\$1').
      replace(/\x08/g, '\\x08');
};


/**
 * Repeats a string n times.
 * @param {string} string The string to repeat.
 * @param {number} length The number of times to repeat.
 * @return {string} A string containing {@code length} repetitions of
 *     {@code string}.
 */
goog.string.repeat = function(string, length) {
  return new Array(length + 1).join(string);
};


/**
 * Pads number to given length and optionally rounds it to a given precision.
 * For example:
 * <pre>padNumber(1.25, 2, 3) -> '01.250'
 * padNumber(1.25, 2) -> '01.25'
 * padNumber(1.25, 2, 1) -> '01.3'
 * padNumber(1.25, 0) -> '1.25'</pre>
 *
 * @param {number} num The number to pad.
 * @param {number} length The desired length.
 * @param {number=} opt_precision The desired precision.
 * @return {string} {@code num} as a string with the given options.
 */
goog.string.padNumber = function(num, length, opt_precision) {
  var s = goog.isDef(opt_precision) ? num.toFixed(opt_precision) : String(num);
  var index = s.indexOf('.');
  if (index == -1) {
    index = s.length;
  }
  return goog.string.repeat('0', Math.max(0, length - index)) + s;
};


/**
 * Returns a string representation of the given object, with
 * null and undefined being returned as the empty string.
 *
 * @param {*} obj The object to convert.
 * @return {string} A string representation of the {@code obj}.
 */
goog.string.makeSafe = function(obj) {
  return obj == null ? '' : String(obj);
};


/**
 * Concatenates string expressions. This is useful
 * since some browsers are very inefficient when it comes to using plus to
 * concat strings. Be careful when using null and undefined here since
 * these will not be included in the result. If you need to represent these
 * be sure to cast the argument to a String first.
 * For example:
 * <pre>buildString('a', 'b', 'c', 'd') -> 'abcd'
 * buildString(null, undefined) -> ''
 * </pre>
 * @param {...*} var_args A list of strings to concatenate. If not a string,
 *     it will be casted to one.
 * @return {string} The concatenation of {@code var_args}.
 */
goog.string.buildString = function(var_args) {
  return Array.prototype.join.call(arguments, '');
};


/**
 * Returns a string with at least 64-bits of randomness.
 *
 * Doesn't trust Javascript's random function entirely. Uses a combination of
 * random and current timestamp, and then encodes the string in base-36 to
 * make it shorter.
 *
 * @return {string} A random string, e.g. sn1s7vb4gcic.
 */
goog.string.getRandomString = function() {
  var x = 2147483648;
  return Math.floor(Math.random() * x).toString(36) +
         Math.abs(Math.floor(Math.random() * x) ^ goog.now()).toString(36);
};


/**
 * Compares two version numbers.
 *
 * @param {string|number} version1 Version of first item.
 * @param {string|number} version2 Version of second item.
 *
 * @return {number}  1 if {@code version1} is higher.
 *                   0 if arguments are equal.
 *                  -1 if {@code version2} is higher.
 */
goog.string.compareVersions = function(version1, version2) {
  var order = 0;
  // Trim leading and trailing whitespace and split the versions into
  // subversions.
  var v1Subs = goog.string.trim(String(version1)).split('.');
  var v2Subs = goog.string.trim(String(version2)).split('.');
  var subCount = Math.max(v1Subs.length, v2Subs.length);

  // Iterate over the subversions, as long as they appear to be equivalent.
  for (var subIdx = 0; order == 0 && subIdx < subCount; subIdx++) {
    var v1Sub = v1Subs[subIdx] || '';
    var v2Sub = v2Subs[subIdx] || '';

    // Split the subversions into pairs of numbers and qualifiers (like 'b').
    // Two different RegExp objects are needed because they are both using
    // the 'g' flag.
    var v1CompParser = new RegExp('(\\d*)(\\D*)', 'g');
    var v2CompParser = new RegExp('(\\d*)(\\D*)', 'g');
    do {
      var v1Comp = v1CompParser.exec(v1Sub) || ['', '', ''];
      var v2Comp = v2CompParser.exec(v2Sub) || ['', '', ''];
      // Break if there are no more matches.
      if (v1Comp[0].length == 0 && v2Comp[0].length == 0) {
        break;
      }

      // Parse the numeric part of the subversion. A missing number is
      // equivalent to 0.
      var v1CompNum = v1Comp[1].length == 0 ? 0 : parseInt(v1Comp[1], 10);
      var v2CompNum = v2Comp[1].length == 0 ? 0 : parseInt(v2Comp[1], 10);

      // Compare the subversion components. The number has the highest
      // precedence. Next, if the numbers are equal, a subversion without any
      // qualifier is always higher than a subversion with any qualifier. Next,
      // the qualifiers are compared as strings.
      order = goog.string.compareElements_(v1CompNum, v2CompNum) ||
          goog.string.compareElements_(v1Comp[2].length == 0,
              v2Comp[2].length == 0) ||
          goog.string.compareElements_(v1Comp[2], v2Comp[2]);
      // Stop as soon as an inequality is discovered.
    } while (order == 0);
  }

  return order;
};


/**
 * Compares elements of a version number.
 *
 * @param {string|number|boolean} left An element from a version number.
 * @param {string|number|boolean} right An element from a version number.
 *
 * @return {number}  1 if {@code left} is higher.
 *                   0 if arguments are equal.
 *                  -1 if {@code right} is higher.
 * @private
 */
goog.string.compareElements_ = function(left, right) {
  if (left < right) {
    return -1;
  } else if (left > right) {
    return 1;
  }
  return 0;
};


/**
 * Maximum value of #goog.string.hashCode, exclusive. 2^32.
 * @type {number}
 * @private
 */
goog.string.HASHCODE_MAX_ = 0x100000000;


/**
 * String hash function similar to java.lang.String.hashCode().
 * The hash code for a string is computed as
 * s[0] * 31 ^ (n - 1) + s[1] * 31 ^ (n - 2) + ... + s[n - 1],
 * where s[i] is the ith character of the string and n is the length of
 * the string. We mod the result to make it between 0 (inclusive) and 2^32
 * (exclusive).
 * @param {string} str A string.
 * @return {number} Hash value for {@code str}, between 0 (inclusive) and 2^32
 *  (exclusive). The empty string returns 0.
 */
goog.string.hashCode = function(str) {
  var result = 0;
  for (var i = 0; i < str.length; ++i) {
    result = 31 * result + str.charCodeAt(i);
    // Normalize to 4 byte range, 0 ... 2^32.
    result %= goog.string.HASHCODE_MAX_;
  }
  return result;
};


/**
 * The most recent unique ID. |0 is equivalent to Math.floor in this case.
 * @type {number}
 * @private
 */
goog.string.uniqueStringCounter_ = Math.random() * 0x80000000 | 0;


/**
 * Generates and returns a string which is unique in the current document.
 * This is useful, for example, to create unique IDs for DOM elements.
 * @return {string} A unique id.
 */
goog.string.createUniqueString = function() {
  return 'goog_' + goog.string.uniqueStringCounter_++;
};


/**
 * Converts the supplied string to a number, which may be Infinity or NaN.
 * This function strips whitespace: (toNumber(' 123') === 123)
 * This function accepts scientific notation: (toNumber('1e1') === 10)
 *
 * This is better than Javascript's built-in conversions because, sadly:
 *     (Number(' ') === 0) and (parseFloat('123a') === 123)
 *
 * @param {string} str The string to convert.
 * @return {number} The number the supplied string represents, or NaN.
 */
goog.string.toNumber = function(str) {
  var num = Number(str);
  if (num == 0 && goog.string.isEmpty(str)) {
    return NaN;
  }
  return num;
};


/**
 * Returns whether the given string is lower camel case (e.g. "isFooBar").
 *
 * Note that this assumes the string is entirely letters.
 * @see http://en.wikipedia.org/wiki/CamelCase#Variations_and_synonyms
 *
 * @param {string} str String to test.
 * @return {boolean} Whether the string is lower camel case.
 */
goog.string.isLowerCamelCase = function(str) {
  return /^[a-z]+([A-Z][a-z]*)*$/.test(str);
};


/**
 * Returns whether the given string is upper camel case (e.g. "FooBarBaz").
 *
 * Note that this assumes the string is entirely letters.
 * @see http://en.wikipedia.org/wiki/CamelCase#Variations_and_synonyms
 *
 * @param {string} str String to test.
 * @return {boolean} Whether the string is upper camel case.
 */
goog.string.isUpperCamelCase = function(str) {
  return /^([A-Z][a-z]*)+$/.test(str);
};


/**
 * Converts a string from selector-case to camelCase (e.g. from
 * "multi-part-string" to "multiPartString"), useful for converting
 * CSS selectors and HTML dataset keys to their equivalent JS properties.
 * @param {string} str The string in selector-case form.
 * @return {string} The string in camelCase form.
 */
goog.string.toCamelCase = function(str) {
  return String(str).replace(/\-([a-z])/g, function(all, match) {
    return match.toUpperCase();
  });
};


/**
 * Converts a string from camelCase to selector-case (e.g. from
 * "multiPartString" to "multi-part-string"), useful for converting JS
 * style and dataset properties to equivalent CSS selectors and HTML keys.
 * @param {string} str The string in camelCase form.
 * @return {string} The string in selector-case form.
 */
goog.string.toSelectorCase = function(str) {
  return String(str).replace(/([A-Z])/g, '-$1').toLowerCase();
};


/**
 * Converts a string into TitleCase. First character of the string is always
 * capitalized in addition to the first letter of every subsequent word.
 * Words are delimited by one or more whitespaces by default. Custom delimiters
 * can optionally be specified to replace the default, which doesn't preserve
 * whitespace delimiters and instead must be explicitly included if needed.
 *
 * Default delimiter => " ":
 *    goog.string.toTitleCase('oneTwoThree')    => 'OneTwoThree'
 *    goog.string.toTitleCase('one two three')  => 'One Two Three'
 *    goog.string.toTitleCase('  one   two   ') => '  One   Two   '
 *    goog.string.toTitleCase('one_two_three')  => 'One_two_three'
 *    goog.string.toTitleCase('one-two-three')  => 'One-two-three'
 *
 * Custom delimiter => "_-.":
 *    goog.string.toTitleCase('oneTwoThree', '_-.')       => 'OneTwoThree'
 *    goog.string.toTitleCase('one two three', '_-.')     => 'One two three'
 *    goog.string.toTitleCase('  one   two   ', '_-.')    => '  one   two   '
 *    goog.string.toTitleCase('one_two_three', '_-.')     => 'One_Two_Three'
 *    goog.string.toTitleCase('one-two-three', '_-.')     => 'One-Two-Three'
 *    goog.string.toTitleCase('one...two...three', '_-.') => 'One...Two...Three'
 *    goog.string.toTitleCase('one. two. three', '_-.')   => 'One. two. three'
 *    goog.string.toTitleCase('one-two.three', '_-.')     => 'One-Two.Three'
 *
 * @param {string} str String value in camelCase form.
 * @param {string=} opt_delimiters Custom delimiter character set used to
 *      distinguish words in the string value. Each character represents a
 *      single delimiter. When provided, default whitespace delimiter is
 *      overridden and must be explicitly included if needed.
 * @return {string} String value in TitleCase form.
 */
goog.string.toTitleCase = function(str, opt_delimiters) {
  var delimiters = goog.isString(opt_delimiters) ?
      goog.string.regExpEscape(opt_delimiters) : '\\s';

  // For IE8, we need to prevent using an empty character set. Otherwise,
  // incorrect matching will occur.
  delimiters = delimiters ? '|[' + delimiters + ']+' : '';

  var regexp = new RegExp('(^' + delimiters + ')([a-z])', 'g');
  return str.replace(regexp, function(all, p1, p2) {
    return p1 + p2.toUpperCase();
  });
};


/**
 * Parse a string in decimal or hexidecimal ('0xFFFF') form.
 *
 * To parse a particular radix, please use parseInt(string, radix) directly. See
 * https://developer.mozilla.org/en/JavaScript/Reference/Global_Objects/parseInt
 *
 * This is a wrapper for the built-in parseInt function that will only parse
 * numbers as base 10 or base 16.  Some JS implementations assume strings
 * starting with "0" are intended to be octal. ES3 allowed but discouraged
 * this behavior. ES5 forbids it.  This function emulates the ES5 behavior.
 *
 * For more information, see Mozilla JS Reference: http://goo.gl/8RiFj
 *
 * @param {string|number|null|undefined} value The value to be parsed.
 * @return {number} The number, parsed. If the string failed to parse, this
 *     will be NaN.
 */
goog.string.parseInt = function(value) {
  // Force finite numbers to strings.
  if (isFinite(value)) {
    value = String(value);
  }

  if (goog.isString(value)) {
    // If the string starts with '0x' or '-0x', parse as hex.
    return /^\s*-?0x/i.test(value) ?
        parseInt(value, 16) : parseInt(value, 10);
  }

  return NaN;
};


/**
 * Splits a string on a separator a limited number of times.
 *
 * This implementation is more similar to Python or Java, where the limit
 * parameter specifies the maximum number of splits rather than truncating
 * the number of results.
 *
 * See http://docs.python.org/2/library/stdtypes.html#str.split
 * See JavaDoc: http://goo.gl/F2AsY
 * See Mozilla reference: http://goo.gl/dZdZs
 *
 * @param {string} str String to split.
 * @param {string} separator The separator.
 * @param {number} limit The limit to the number of splits. The resulting array
 *     will have a maximum length of limit+1.  Negative numbers are the same
 *     as zero.
 * @return {!Array.<string>} The string, split.
 */

goog.string.splitLimit = function(str, separator, limit) {
  var parts = str.split(separator);
  var returnVal = [];

  // Only continue doing this while we haven't hit the limit and we have
  // parts left.
  while (limit > 0 && parts.length) {
    returnVal.push(parts.shift());
    limit--;
  }

  // If there are remaining parts, append them to the end.
  if (parts.length) {
    returnVal.push(parts.join(separator));
  }

  return returnVal;
};


// Copyright 2008 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Utilities to check the preconditions, postconditions and
 * invariants runtime.
 *
 * Methods in this package should be given special treatment by the compiler
 * for type-inference. For example, <code>goog.asserts.assert(foo)</code>
 * will restrict <code>foo</code> to a truthy value.
 *
 * The compiler has an option to disable asserts. So code like:
 * <code>
 * var x = goog.asserts.assert(foo()); goog.asserts.assert(bar());
 * </code>
 * will be transformed into:
 * <code>
 * var x = foo();
 * </code>
 * The compiler will leave in foo() (because its return value is used),
 * but it will remove bar() because it assumes it does not have side-effects.
 *
 */

goog.provide('goog.asserts');
goog.provide('goog.asserts.AssertionError');

goog.require('goog.debug.Error');
goog.require('goog.dom.NodeType');
goog.require('goog.string');


/**
 * @define {boolean} Whether to strip out asserts or to leave them in.
 */
goog.define('goog.asserts.ENABLE_ASSERTS', goog.DEBUG);



/**
 * Error object for failed assertions.
 * @param {string} messagePattern The pattern that was used to form message.
 * @param {!Array.<*>} messageArgs The items to substitute into the pattern.
 * @constructor
 * @extends {goog.debug.Error}
 * @final
 */
goog.asserts.AssertionError = function(messagePattern, messageArgs) {
  messageArgs.unshift(messagePattern);
  goog.debug.Error.call(this, goog.string.subs.apply(null, messageArgs));
  // Remove the messagePattern afterwards to avoid permenantly modifying the
  // passed in array.
  messageArgs.shift();

  /**
   * The message pattern used to format the error message. Error handlers can
   * use this to uniquely identify the assertion.
   * @type {string}
   */
  this.messagePattern = messagePattern;
};
goog.inherits(goog.asserts.AssertionError, goog.debug.Error);


/** @override */
goog.asserts.AssertionError.prototype.name = 'AssertionError';


/**
 * The default error handler.
 * @param {!goog.asserts.AssertionError} e The exception to be handled.
 */
goog.asserts.DEFAULT_ERROR_HANDLER = function(e) { throw e; };


/**
 * The handler responsible for throwing or logging assertion errors.
 * @private {function(!goog.asserts.AssertionError)}
 */
goog.asserts.errorHandler_ = goog.asserts.DEFAULT_ERROR_HANDLER;


/**
 * Throws an exception with the given message and "Assertion failed" prefixed
 * onto it.
 * @param {string} defaultMessage The message to use if givenMessage is empty.
 * @param {Array.<*>} defaultArgs The substitution arguments for defaultMessage.
 * @param {string|undefined} givenMessage Message supplied by the caller.
 * @param {Array.<*>} givenArgs The substitution arguments for givenMessage.
 * @throws {goog.asserts.AssertionError} When the value is not a number.
 * @private
 */
goog.asserts.doAssertFailure_ =
    function(defaultMessage, defaultArgs, givenMessage, givenArgs) {
  var message = 'Assertion failed';
  if (givenMessage) {
    message += ': ' + givenMessage;
    var args = givenArgs;
  } else if (defaultMessage) {
    message += ': ' + defaultMessage;
    args = defaultArgs;
  }
  // The '' + works around an Opera 10 bug in the unit tests. Without it,
  // a stack trace is added to var message above. With this, a stack trace is
  // not added until this line (it causes the extra garbage to be added after
  // the assertion message instead of in the middle of it).
  var e = new goog.asserts.AssertionError('' + message, args || []);
  goog.asserts.errorHandler_(e);
};


/**
 * Sets a custom error handler that can be used to customize the behavior of
 * assertion failures, for example by turning all assertion failures into log
 * messages.
 * @param {function(goog.asserts.AssertionError)} errorHandler
 */
goog.asserts.setErrorHandler = function(errorHandler) {
  if (goog.asserts.ENABLE_ASSERTS) {
    goog.asserts.errorHandler_ = errorHandler;
  }
};


/**
 * Checks if the condition evaluates to true if goog.asserts.ENABLE_ASSERTS is
 * true.
 * @template T
 * @param {T} condition The condition to check.
 * @param {string=} opt_message Error message in case of failure.
 * @param {...*} var_args The items to substitute into the failure message.
 * @return {T} The value of the condition.
 * @throws {goog.asserts.AssertionError} When the condition evaluates to false.
 */
goog.asserts.assert = function(condition, opt_message, var_args) {
  if (goog.asserts.ENABLE_ASSERTS && !condition) {
    goog.asserts.doAssertFailure_('', null, opt_message,
        Array.prototype.slice.call(arguments, 2));
  }
  return condition;
};


/**
 * Fails if goog.asserts.ENABLE_ASSERTS is true. This function is useful in case
 * when we want to add a check in the unreachable area like switch-case
 * statement:
 *
 * <pre>
 *  switch(type) {
 *    case FOO: doSomething(); break;
 *    case BAR: doSomethingElse(); break;
 *    default: goog.assert.fail('Unrecognized type: ' + type);
 *      // We have only 2 types - "default:" section is unreachable code.
 *  }
 * </pre>
 *
 * @param {string=} opt_message Error message in case of failure.
 * @param {...*} var_args The items to substitute into the failure message.
 * @throws {goog.asserts.AssertionError} Failure.
 */
goog.asserts.fail = function(opt_message, var_args) {
  if (goog.asserts.ENABLE_ASSERTS) {
    goog.asserts.errorHandler_(new goog.asserts.AssertionError(
        'Failure' + (opt_message ? ': ' + opt_message : ''),
        Array.prototype.slice.call(arguments, 1)));
  }
};


/**
 * Checks if the value is a number if goog.asserts.ENABLE_ASSERTS is true.
 * @param {*} value The value to check.
 * @param {string=} opt_message Error message in case of failure.
 * @param {...*} var_args The items to substitute into the failure message.
 * @return {number} The value, guaranteed to be a number when asserts enabled.
 * @throws {goog.asserts.AssertionError} When the value is not a number.
 */
goog.asserts.assertNumber = function(value, opt_message, var_args) {
  if (goog.asserts.ENABLE_ASSERTS && !goog.isNumber(value)) {
    goog.asserts.doAssertFailure_('Expected number but got %s: %s.',
        [goog.typeOf(value), value], opt_message,
        Array.prototype.slice.call(arguments, 2));
  }
  return /** @type {number} */ (value);
};


/**
 * Checks if the value is a string if goog.asserts.ENABLE_ASSERTS is true.
 * @param {*} value The value to check.
 * @param {string=} opt_message Error message in case of failure.
 * @param {...*} var_args The items to substitute into the failure message.
 * @return {string} The value, guaranteed to be a string when asserts enabled.
 * @throws {goog.asserts.AssertionError} When the value is not a string.
 */
goog.asserts.assertString = function(value, opt_message, var_args) {
  if (goog.asserts.ENABLE_ASSERTS && !goog.isString(value)) {
    goog.asserts.doAssertFailure_('Expected string but got %s: %s.',
        [goog.typeOf(value), value], opt_message,
        Array.prototype.slice.call(arguments, 2));
  }
  return /** @type {string} */ (value);
};


/**
 * Checks if the value is a function if goog.asserts.ENABLE_ASSERTS is true.
 * @param {*} value The value to check.
 * @param {string=} opt_message Error message in case of failure.
 * @param {...*} var_args The items to substitute into the failure message.
 * @return {!Function} The value, guaranteed to be a function when asserts
 *     enabled.
 * @throws {goog.asserts.AssertionError} When the value is not a function.
 */
goog.asserts.assertFunction = function(value, opt_message, var_args) {
  if (goog.asserts.ENABLE_ASSERTS && !goog.isFunction(value)) {
    goog.asserts.doAssertFailure_('Expected function but got %s: %s.',
        [goog.typeOf(value), value], opt_message,
        Array.prototype.slice.call(arguments, 2));
  }
  return /** @type {!Function} */ (value);
};


/**
 * Checks if the value is an Object if goog.asserts.ENABLE_ASSERTS is true.
 * @param {*} value The value to check.
 * @param {string=} opt_message Error message in case of failure.
 * @param {...*} var_args The items to substitute into the failure message.
 * @return {!Object} The value, guaranteed to be a non-null object.
 * @throws {goog.asserts.AssertionError} When the value is not an object.
 */
goog.asserts.assertObject = function(value, opt_message, var_args) {
  if (goog.asserts.ENABLE_ASSERTS && !goog.isObject(value)) {
    goog.asserts.doAssertFailure_('Expected object but got %s: %s.',
        [goog.typeOf(value), value],
        opt_message, Array.prototype.slice.call(arguments, 2));
  }
  return /** @type {!Object} */ (value);
};


/**
 * Checks if the value is an Array if goog.asserts.ENABLE_ASSERTS is true.
 * @param {*} value The value to check.
 * @param {string=} opt_message Error message in case of failure.
 * @param {...*} var_args The items to substitute into the failure message.
 * @return {!Array} The value, guaranteed to be a non-null array.
 * @throws {goog.asserts.AssertionError} When the value is not an array.
 */
goog.asserts.assertArray = function(value, opt_message, var_args) {
  if (goog.asserts.ENABLE_ASSERTS && !goog.isArray(value)) {
    goog.asserts.doAssertFailure_('Expected array but got %s: %s.',
        [goog.typeOf(value), value], opt_message,
        Array.prototype.slice.call(arguments, 2));
  }
  return /** @type {!Array} */ (value);
};


/**
 * Checks if the value is a boolean if goog.asserts.ENABLE_ASSERTS is true.
 * @param {*} value The value to check.
 * @param {string=} opt_message Error message in case of failure.
 * @param {...*} var_args The items to substitute into the failure message.
 * @return {boolean} The value, guaranteed to be a boolean when asserts are
 *     enabled.
 * @throws {goog.asserts.AssertionError} When the value is not a boolean.
 */
goog.asserts.assertBoolean = function(value, opt_message, var_args) {
  if (goog.asserts.ENABLE_ASSERTS && !goog.isBoolean(value)) {
    goog.asserts.doAssertFailure_('Expected boolean but got %s: %s.',
        [goog.typeOf(value), value], opt_message,
        Array.prototype.slice.call(arguments, 2));
  }
  return /** @type {boolean} */ (value);
};


/**
 * Checks if the value is a DOM Element if goog.asserts.ENABLE_ASSERTS is true.
 * @param {*} value The value to check.
 * @param {string=} opt_message Error message in case of failure.
 * @param {...*} var_args The items to substitute into the failure message.
 * @return {!Element} The value, likely to be a DOM Element when asserts are
 *     enabled.
 * @throws {goog.asserts.AssertionError} When the value is not a boolean.
 */
goog.asserts.assertElement = function(value, opt_message, var_args) {
  if (goog.asserts.ENABLE_ASSERTS && (!goog.isObject(value) ||
      value.nodeType != goog.dom.NodeType.ELEMENT)) {
    goog.asserts.doAssertFailure_('Expected Element but got %s: %s.',
        [goog.typeOf(value), value], opt_message,
        Array.prototype.slice.call(arguments, 2));
  }
  return /** @type {!Element} */ (value);
};


/**
 * Checks if the value is an instance of the user-defined type if
 * goog.asserts.ENABLE_ASSERTS is true.
 *
 * The compiler may tighten the type returned by this function.
 *
 * @param {*} value The value to check.
 * @param {function(new: T, ...)} type A user-defined constructor.
 * @param {string=} opt_message Error message in case of failure.
 * @param {...*} var_args The items to substitute into the failure message.
 * @throws {goog.asserts.AssertionError} When the value is not an instance of
 *     type.
 * @return {!T}
 * @template T
 */
goog.asserts.assertInstanceof = function(value, type, opt_message, var_args) {
  if (goog.asserts.ENABLE_ASSERTS && !(value instanceof type)) {
    goog.asserts.doAssertFailure_('instanceof check failed.', null,
        opt_message, Array.prototype.slice.call(arguments, 3));
  }
  return value;
};


/**
 * Checks that no enumerable keys are present in Object.prototype. Such keys
 * would break most code that use {@code for (var ... in ...)} loops.
 */
goog.asserts.assertObjectPrototypeIsIntact = function() {
  for (var key in Object.prototype) {
    goog.asserts.fail(key + ' should not be enumerable in Object.prototype.');
  }
};

goog.provide('olgm');


/**
 * @type {!Array.<number>}
 */
olgm.RESOLUTIONS = [
  156543.03390625,
  78271.516953125,
  39135.7584765625,
  19567.87923828125,
  9783.939619140625,
  4891.9698095703125,
  2445.9849047851562,
  1222.9924523925781,
  611.4962261962891,
  305.74811309814453,
  152.87405654907226,
  76.43702827453613,
  38.218514137268066,
  19.109257068634033,
  9.554628534317017,
  4.777314267158508,
  2.388657133579254,
  1.194328566789627,
  0.5971642833948135,
  0.298582141697,
  0.14929107084,
  0.07464553542,
  0.03732276771
];


/**
 * @param {ol.geom.Geometry} geometry
 * @return {ol.Coordinate}
 */
olgm.getCenterOf = function(geometry) {

  var center = null;

  if (geometry instanceof ol.geom.Point) {
    center = geometry.getCoordinates();
  } else if (geometry instanceof ol.geom.Polygon) {
    center = geometry.getInteriorPoint().getCoordinates();
  } else {
    center = ol.extent.getCenter(geometry.getExtent());
  }

  return center;
};


/**
 * @param {string|Array.<number>|CanvasGradient|CanvasPattern} color
 * @return {string}
 */
olgm.getColor = function(color) {

  var out = '';
  var rgba = null;

  if (typeof color === 'string') {
    // is string
    if (olgm.stringStartsWith(color, 'rgba')) {
      rgba = olgm.parseRGBA(color);
    } else {
      out = color;
    }
  } else if (Array.isArray(color)) {
    rgba = color;
  }

  // Todo - support CanvasGradient and CanvasPattern

  if (rgba !== null) {
    out = ['rgb(', rgba[0], ',', rgba[1], ',', rgba[2], ')'].join('');
  }

  return out;
};


/**
 * @param {string|Array.<number>|CanvasGradient|CanvasPattern} color
 * @return {?number}
 */
olgm.getColorOpacity = function(color) {

  var opacity = null;
  var rgba = null;

  if (typeof color === 'string') {
    // is string
    if (olgm.stringStartsWith(color, 'rgba')) {
      rgba = olgm.parseRGBA(color);
    }
  } else if (Array.isArray(color)) {
    rgba = color;
  }

  // Todo - support CanvasGradient and CanvasPattern

  if (rgba && rgba[3] !== undefined) {
    opacity = +rgba[3];
  }

  return opacity;
};


/**
 * Get the style from the specified object.
 * @param {ol.style.Style|ol.style.StyleFunction|ol.layer.Vector|ol.Feature} object

 * @return {?ol.style.Style}
 */
olgm.getStyleOf = function(object) {

  var style = null;

  if (object instanceof ol.style.Style) {
    style = object;
  } else if (object instanceof ol.layer.Vector ||
             object instanceof ol.Feature) {
    style = object.getStyle();
    if (style && style instanceof Function) {
      style = style()[0]; // todo - support multiple styles ?
    }
  } else if (object instanceof Function) {
    style = object()[0]; // todo - support multiple styles ?
  }

  return style;
};


/**
 * @param {number} resolution
 * @return {?number}
 */
olgm.getZoomFromResolution = function(resolution) {

  var found = null;
  var precision = 1000;
  var res = Math.round(resolution * precision) / precision;

  for (var z = 0, len = olgm.RESOLUTIONS.length; z < len; z++) {
    if (res == Math.round(olgm.RESOLUTIONS[z] * precision) / precision) {
      found = z;
      break;
    }
  }

  return found;
};


/**
 * Source: http://stackoverflow.com/questions/7543818/\
 *     regex-javascript-to-match-both-rgb-and-rgba
 * @param {string} rgbaString
 * @return {?Array.<number>}
 */
olgm.parseRGBA = function(rgbaString) {
  var rgba = null;
  var regex = /^rgba?\((\d+),\s*(\d+),\s*(\d+)(?:,\s*(\d+(?:\.\d+)?))?\)$/;
  var matches = rgbaString.match(regex);
  if (matches && matches.length) {
    rgba = [
      +matches[1],
      +matches[2],
      +matches[3],
      +matches[4]
    ];
  }
  return rgba;
};


/**
 * @param {string} string
 * @param {string} needle
 * @return {boolean}
 */
olgm.stringStartsWith = function(string, needle) {
  return (string.indexOf(needle) === 0);
};


/**
 * @param {Array.<ol.events.Key|Array.<ol.events.Key>>} listenerKeys
 * @param {Array.<goog.events.Key>=} opt_googListenerKeys
 */
olgm.unlistenAllByKey = function(listenerKeys, opt_googListenerKeys) {
  listenerKeys.forEach(ol.Observable.unByKey);
  listenerKeys.length = 0;
  if (opt_googListenerKeys) {
    opt_googListenerKeys.forEach(goog.events.unlistenByKey);
    opt_googListenerKeys.length = 0;
  }
};

/**
 * The following file was borrowed from the MapLabel project, which original
 * source code is available at:
 *
 *     http://google-maps-utility-library-v3.googlecode.com/svn/trunk/maplabel
 *
 * Here's a copy of its license:
 *
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @fileoverview Map Label.
 *
 * @author lukem@google.com (Luke Mahe)
 * @author cbro@google.com (Chris Broadfoot)
 *
 * Integration to OL3-Google-Maps:
 * @author adube@mapgears.com (Alexandre Dubé)
 */

goog.provide('olgm.gm.MapLabel');



/**
 * Creates a new Map Label
 * @constructor
 * @extends {google.maps.OverlayView}
 * @param {Object.<string, *>=} opt_options Optional properties to set.
 * @api
 */
olgm.gm.MapLabel = function(opt_options) {
  this.set('font', 'normal 10px sans-serif');
  this.set('textAlign', 'center');
  this.set('textBaseline', 'middle');

  this.set('zIndex', 1e3);

  this.setValues(opt_options);
};
if (window.google && window.google.maps) {
  goog.inherits(olgm.gm.MapLabel, google.maps.OverlayView);
}


/**
 * @type {boolean}
 * @private
 */
olgm.gm.MapLabel.prototype.drawn_ = false;


/**
 * @type {number}
 * @private
 */
olgm.gm.MapLabel.prototype.height_ = 0;


/**
 * @type {number}
 * @private
 */
olgm.gm.MapLabel.prototype.width_ = 0;


/**
 * Note: mark as `@api` to make the minimized version include this method.
 * @param {string} prop
 * @api
 */
olgm.gm.MapLabel.prototype.changed = function(prop) {
  switch (prop) {
    case 'fontColor':
    case 'fontFamily':
    case 'fontSize':
    case 'fontWeight':
    case 'strokeColor':
    case 'strokeWeight':
    case 'text':
    case 'textAlign':
    case 'textBaseline':
      this.drawCanvas_();
      break;
    case 'maxZoom':
    case 'minZoom':
    case 'offsetX':
    case 'offsetY':
    case 'position':
      this.draw();
      break;
  }
};


/**
 * Draws the label to the canvas 2d context.
 * @private
 */
olgm.gm.MapLabel.prototype.drawCanvas_ = function() {
  var canvas = this.canvas_;
  if (!canvas) return;

  var style = canvas.style;

  var fillStyle = this.get('fontColor');
  if (!fillStyle) {
    return;
  }

  style.zIndex = /** @type {number} */ (this.get('zIndex'));

  var ctx = canvas.getContext('2d');
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  ctx.textBaseline = this.get('textBaseline');
  ctx.strokeStyle = this.get('strokeColor');
  ctx.fillStyle = fillStyle;
  ctx.font = this.get('font');
  ctx.textAlign = this.get('textAlign');

  var text = this.get('text');
  if (text) {
    var x = canvas.width / 2;
    var y = canvas.height / 2;

    var strokeWeight = Number(this.get('strokeWeight'));
    if (strokeWeight) {
      ctx.lineWidth = strokeWeight;
      ctx.strokeText(text, x, y);
    }

    ctx.fillText(text, x, y);
  }
};


/**
 * Note: mark as `@api` to make the minimized version include this method.
 * @api
 */
olgm.gm.MapLabel.prototype.onAdd = function() {
  var canvas = this.canvas_ = document.createElement('canvas');
  var style = canvas.style;
  style.position = 'absolute';

  var ctx = canvas.getContext('2d');
  ctx.lineJoin = 'round';

  this.drawCanvas_();

  var panes = this.getPanes();
  if (panes) {
    panes.markerLayer.appendChild(canvas);
  }
};


/**
 * Note: mark as `@api` to make the minimized version include this method.
 * @api
 */
olgm.gm.MapLabel.prototype.draw = function() {

  if (this.drawn_) {
    this.redraw_();
    return;
  }

  var canvas = this.canvas_;
  if (!canvas) {
    // onAdd has not been called yet.
    return;
  }

  var ctx = canvas.getContext('2d');
  var height = ctx.canvas.height;
  var width = ctx.canvas.width;
  this.width_ = width;
  this.height_ = height;

  if (!this.redraw_()) {
    return;
  }

  this.drawn_ = true;
};


/**
 * Note: mark as `@api` to make the minimized version include this method.
 * @return {boolean}
 * @private
 */
olgm.gm.MapLabel.prototype.redraw_ = function() {
  var latLng = /** @type {google.maps.LatLng} */ (this.get('position'));
  if (!latLng) {
    return false;
  }

  var projection = this.getProjection();
  if (!projection) {
    // The map projection is not ready yet so do nothing
    return false;
  }

  var pos = projection.fromLatLngToDivPixel(latLng);
  var height = this.height_;
  var width = this.width_;

  var offsetX = this.get('offsetX') || 0;
  var offsetY = this.get('offsetY') || 0;
  var style = this.canvas_.style;
  style['top'] = (pos.y - (height / 2) + offsetY) + 'px';
  style['left'] = (pos.x - (width / 2) + offsetX) + 'px';

  style['visibility'] = this.getVisible_();

  return true;
};


/**
 * Get the visibility of the label.
 * @private
 * @return {string} blank string if visible, 'hidden' if invisible.
 */
olgm.gm.MapLabel.prototype.getVisible_ = function() {
  var minZoom = /** @type {number} */ (this.get('minZoom'));
  var maxZoom = /** @type {number} */ (this.get('maxZoom'));

  if (minZoom === undefined && maxZoom === undefined) {
    return '';
  }

  var map = this.getMap();
  if (!map) {
    return '';
  }

  var mapZoom = map.getZoom();
  if (mapZoom < minZoom || mapZoom > maxZoom) {
    return 'hidden';
  }
  return '';
};


/**
 * Note: mark as `@api` to make the minimized version include this method.
 * @api
 */
olgm.gm.MapLabel.prototype.onRemove = function() {
  var canvas = this.canvas_;
  if (canvas && canvas.parentNode) {
    canvas.parentNode.removeChild(canvas);
  }
};

goog.provide('olgm.gm');

goog.require('goog.asserts');
goog.require('olgm');
goog.require('olgm.gm.MapLabel');


// === Data ===


/**
 * Create a Google Maps feature using an OpenLayers one.
 * @param {ol.Feature} feature
 * @param {ol.Map=} opt_ol3map For reprojection purpose. If undefined, then
 *     `EPSG:3857` is used.
 * @return {google.maps.Data.Feature}
 */
olgm.gm.createFeature = function(feature, opt_ol3map) {
  var geometry = feature.getGeometry();
  goog.asserts.assertInstanceof(geometry, ol.geom.Geometry);
  var gmapGeometry = olgm.gm.createFeatureGeometry(geometry, opt_ol3map);
  return new google.maps.Data.Feature({
    geometry: gmapGeometry
  });
};


/**
 * Create a Google Maps geometry using an OpenLayers one.
 * @param {ol.geom.Geometry} geometry
 * @param {ol.Map=} opt_ol3map For reprojection purpose. If undefined, then
 *     `EPSG:3857` is used.
 * @return {google.maps.Data.Geometry|google.maps.LatLng|google.maps.LatLng}
 */
olgm.gm.createFeatureGeometry = function(geometry, opt_ol3map) {

  var gmapGeometry = null;

  if (geometry instanceof ol.geom.Point) {
    gmapGeometry = olgm.gm.createLatLng(geometry, opt_ol3map);
  } else if (geometry instanceof ol.geom.LineString ||
             geometry instanceof ol.geom.Polygon) {
    gmapGeometry = olgm.gm.createGeometry(geometry, opt_ol3map);
  }

  goog.asserts.assert(gmapGeometry !== null,
      'GoogleMaps geometry should not be null');

  return gmapGeometry;
};


/**
 * Create a Google Maps LatLng object using an OpenLayers Point.
 * @param {ol.geom.Point|ol.Coordinate} object
 * @param {ol.Map=} opt_ol3map For reprojection purpose. If undefined, then
 *     `EPSG:3857` is used.
 * @return {google.maps.LatLng}
 */
olgm.gm.createLatLng = function(object, opt_ol3map) {
  var inProj = (opt_ol3map !== undefined) ?
      opt_ol3map.getView().getProjection() : 'EPSG:3857';
  var coordinates;
  if (object instanceof ol.geom.Point) {
    coordinates = object.getCoordinates();
  } else {
    coordinates = object;
  }
  var lonLatCoords = ol.proj.transform(coordinates, inProj, 'EPSG:4326');
  return new google.maps.LatLng(lonLatCoords[1], lonLatCoords[0]);
};


/**
 * Create a Google Maps LineString or Polygon object using an OpenLayers one.
 * @param {ol.geom.LineString|ol.geom.Polygon} geometry
 * @param {ol.Map=} opt_ol3map For reprojection purpose. If undefined, then
 *     `EPSG:3857` is used.
 * @return {google.maps.Data.LineString|google.maps.Data.Polygon}
 */
olgm.gm.createGeometry = function(geometry, opt_ol3map) {
  var inProj = (opt_ol3map !== undefined) ?
      opt_ol3map.getView().getProjection() : 'EPSG:3857';

  var latLngs = [];
  var lonLatCoords;

  var coordinates;
  if (geometry instanceof ol.geom.LineString) {
    coordinates = geometry.getCoordinates();
  } else {
    coordinates = geometry.getCoordinates()[0];
  }

  for (var i = 0, len = coordinates.length; i < len; i++) {
    lonLatCoords = ol.proj.transform(coordinates[i], inProj, 'EPSG:4326');
    latLngs.push(new google.maps.LatLng(lonLatCoords[1], lonLatCoords[0]));
  }

  var gmapGeometry = null;
  if (geometry instanceof ol.geom.LineString) {
    gmapGeometry = new google.maps.Data.LineString(latLngs);
  } else {
    gmapGeometry = new google.maps.Data.Polygon([latLngs]);
  }

  return gmapGeometry;
};


// === Style ===


/**
 * Create a Google Maps data style options from an OpenLayers object.
 * @param {ol.style.Style|ol.style.StyleFunction|ol.layer.Vector|ol.Feature} object
 * @param {number=} opt_index
 * @return {?google.maps.Data.StyleOptions}
 */
olgm.gm.createStyle = function(object, opt_index) {
  var gmStyle = null;
  var style = olgm.getStyleOf(object);
  if (style) {
    gmStyle = olgm.gm.createStyleInternal(style, opt_index);
  }
  return gmStyle;
};


/**
 * Create a Google Maps data style options from an OpenLayers style object.
 * @param {ol.style.Style} style
 * @param {number=} opt_index
 * @return {google.maps.Data.StyleOptions}
 */
olgm.gm.createStyleInternal = function(style, opt_index) {

  var gmStyle = /** @type {google.maps.Data.StyleOptions} */ ({});

  // strokeColor
  // strokeOpacity
  // strokeWeight
  var stroke = style.getStroke();
  if (stroke) {
    var strokeColor = stroke.getColor();
    if (strokeColor) {
      gmStyle['strokeColor'] = olgm.getColor(strokeColor);
      var strokeOpacity = olgm.getColorOpacity(strokeColor);
      if (strokeOpacity !== null) {
        gmStyle['strokeOpacity'] = strokeOpacity;
      }
    }

    var strokeWidth = stroke.getWidth();
    if (strokeWidth) {
      gmStyle['strokeWeight'] = strokeWidth;
    }
  }

  // fillColor
  // fillOpacity
  var fill = style.getFill();
  if (fill) {
    var fillColor = fill.getColor();
    if (fillColor) {
      gmStyle['fillColor'] = olgm.getColor(fillColor);
      var fillOpacity = olgm.getColorOpacity(fillColor);
      if (fillOpacity !== null) {
        gmStyle['fillOpacity'] = fillOpacity;
      }
    }
  }

  var image = style.getImage();
  if (image) {
    var gmIcon = {};
    var gmSymbol = {};

    if (image instanceof ol.style.Circle) {
      // --- ol.style.Circle ---
      gmSymbol['path'] = google.maps.SymbolPath.CIRCLE;

      var imageStroke = image.getStroke();
      if (imageStroke) {
        var imageStrokeColor = imageStroke.getColor();
        if (imageStrokeColor) {
          gmSymbol['strokeColor'] = olgm.getColor(imageStrokeColor);
        }

        gmSymbol['strokeWeight'] = imageStroke.getWidth();
      }

      var imageFill = image.getFill();
      if (imageFill) {
        var imageFillColor = imageFill.getColor();
        if (imageFillColor) {
          gmSymbol['fillColor'] = olgm.getColor(imageFillColor);

          var imageFillOpacity = olgm.getColorOpacity(imageFillColor);
          if (imageFillOpacity !== null) {
            gmSymbol['fillOpacity'] = imageFillOpacity;
          } else {
            // Google Maps default fill opacity of images is `0`. In ol3,
            // it's `1`.
            gmSymbol['fillOpacity'] = 1;
          }
        }
      }

      var imageRadius = image.getRadius();
      if (imageRadius) {
        gmSymbol['scale'] = imageRadius;
      }
    } else if (image instanceof ol.style.Icon) {
      // --- ol.style.Icon ---

      var imageSrc = image.getSrc();
      if (imageSrc) {
        gmSymbol['url'] = imageSrc;
      }

      var imageScale = image.getScale();

      var imageAnchor = image.getAnchor();
      if (imageAnchor) {
        if (imageScale !== undefined) {
          gmSymbol['anchor'] = new google.maps.Point(
              imageAnchor[0] * imageScale, imageAnchor[1] * imageScale);
        } else {
          gmSymbol['anchor'] = new google.maps.Point(
              imageAnchor[0], imageAnchor[1]);
        }
      }

      var imageOrigin = image.getOrigin();
      if (imageOrigin) {
        gmSymbol['origin'] = new google.maps.Point(
            imageOrigin[0], imageOrigin[1]);
      }

      var imageSize = image.getSize();
      if (imageSize) {
        gmSymbol['size'] = new google.maps.Size(imageSize[0], imageSize[1]);

        if (imageScale !== undefined) {
          gmSymbol['scaledSize'] = new google.maps.Size(
              imageSize[0] * imageScale, imageSize[1] * imageScale);
        }
      }

      // NOTE - google.maps.Icon does not support opacity
    }

    if (Object.keys(gmIcon).length) {
      gmStyle['icon'] = /** @type {google.maps.Icon} */ (gmIcon);
    } else if (Object.keys(gmSymbol).length) {
      gmStyle['icon'] = /** @type {google.maps.Symbol} */ (gmSymbol);
    }
  }

  // if, at this very last point, there aren't any style options that have
  // been set, then tell Google Maps to render the feature invisible because
  // we're dealing with an empty `ol.style.Style` object.
  if (Object.keys(/** @type {!Object} */ (gmStyle)).length === 0) {
    gmStyle['visible'] = false;
  } else if (opt_index !== undefined) {
    var zIndex = opt_index * 2;
    gmStyle['zIndex'] = zIndex;
  }

  return gmStyle;
};


// === Label ===


/**
 * Create a MapLabel object from a text style and Lat/Lng location.
 * @param {ol.style.Text} textStyle
 * @param {google.maps.LatLng} latLng
 * @param {number} index
 * @return {olgm.gm.MapLabel}
 */
olgm.gm.createLabel = function(textStyle, latLng, index) {

  var labelOptions = {
    align: 'center',
    position: latLng,
    zIndex: index * 2 + 1
  };

  var text = textStyle.getText();
  if (text) {
    labelOptions['text'] = text;
  }

  var font = textStyle.getFont();
  if (font) {
    labelOptions['font'] = font;
  }

  var fill = textStyle.getFill();
  if (fill) {
    var fillColor = fill.getColor();
    if (fillColor) {
      labelOptions['fontColor'] = fillColor;
    }
  }

  var stroke = textStyle.getStroke();
  if (stroke) {
    var strokeColor = stroke.getColor();
    if (strokeColor) {
      labelOptions['strokeColor'] = strokeColor;
    }

    var strokeWidth = stroke.getWidth();
    if (strokeWidth) {
      labelOptions['strokeWeight'] = strokeWidth;
    }
  }

  var offsetX = textStyle.getOffsetX();
  if (offsetX) {
    labelOptions['offsetX'] = offsetX;
  }

  var offsetY = textStyle.getOffsetY();
  if (offsetY) {
    labelOptions['offsetY'] = offsetY;
  }

  var textAlign = textStyle.getTextAlign();
  if (textAlign) {
    labelOptions['textAlign'] = textAlign;
  }

  var textBaseline = textStyle.getTextBaseline();
  if (textBaseline) {
    labelOptions['textBaseline'] = textBaseline;
  }

  return new olgm.gm.MapLabel(labelOptions);
};

goog.provide('olgm.herald.Herald');

goog.require('olgm');
goog.require('olgm.Abstract');



/**
 * Abstract class for all heralds. When activated, an herald synchronizes
 * something from the OpenLayers map to the Google Maps one. When deactivated,
 * it stops doing so.
 *
 * @param {!ol.Map} ol3map
 * @param {!google.maps.Map} gmap
 * @constructor
 * @extends {olgm.Abstract}
 */
olgm.herald.Herald = function(ol3map, gmap) {

  /**
   * @type {Array.<ol.events.Key|Array.<ol.events.Key>>}
   * @protected
   */
  this.listenerKeys = [];

  /**
   * @type {Array.<goog.events.Key>}
   * @protected
   */
  this.googListenerKeys = [];

  goog.base(this, ol3map, gmap);
};
goog.inherits(olgm.herald.Herald, olgm.Abstract);


/**
 * Register all event listeners.
 */
olgm.herald.Herald.prototype.activate = function() {};


/**
 * Unregister all event listeners.
 */
olgm.herald.Herald.prototype.deactivate = function() {
  olgm.unlistenAllByKey(this.listenerKeys, this.googListenerKeys);
};

goog.provide('olgm.herald.Feature');

goog.require('goog.asserts');
goog.require('olgm');
goog.require('olgm.gm');
goog.require('olgm.herald.Herald');



/**
 * The Feature Herald is responsible of synchronizing a single ol3 vector
 * feature to a gmap feature. Here's what synchronized within the feature:
 *
 * - its geometry
 * - its style
 *
 * @param {!ol.Map} ol3map
 * @param {!google.maps.Map} gmap
 * @param {ol.Feature} feature
 * @param {!google.maps.Data} data
 * @param {number} index
 * @constructor
 * @extends {olgm.herald.Herald}
 */
olgm.herald.Feature = function(ol3map, gmap, feature, data, index) {

  /**
   * @type {ol.Feature}
   * @private
   */
  this.feature_ = feature;

  /**
   * @type {!google.maps.Data}
   * @private
   */
  this.data_ = data;

  /**
   * @type {number}
   * @private
   */
  this.index_ = index;

  goog.base(this, ol3map, gmap);

};
goog.inherits(olgm.herald.Feature, olgm.herald.Herald);


/**
 * @type {google.maps.Data.Feature}
 * @private
 */
olgm.herald.Feature.prototype.gmapFeature_ = null;


/**
 * @type {olgm.gm.MapLabel}
 * @private
 */
olgm.herald.Feature.prototype.label_ = null;


/**
 * @inheritDoc
 */
olgm.herald.Feature.prototype.activate = function() {
  goog.base(this, 'activate');

  var geometry = this.feature_.getGeometry();
  goog.asserts.assertInstanceof(geometry, ol.geom.Geometry);

  // create gmap feature
  this.gmapFeature_ = olgm.gm.createFeature(this.feature_);
  this.data_.add(this.gmapFeature_);

  // override style if a style is defined at the feature level
  var gmStyle = olgm.gm.createStyle(this.feature_, this.index_);
  if (gmStyle) {
    this.data_.overrideStyle(this.gmapFeature_, gmStyle);
  }

  // if the feature has text style, add a map label to gmap
  var style = olgm.getStyleOf(this.feature_);
  if (style) {
    var text = style.getText();
    if (text) {
      var latLng = olgm.gm.createLatLng(olgm.getCenterOf(geometry));
      this.label_ = olgm.gm.createLabel(text, latLng, this.index_);
      this.label_.setMap(this.gmap);
    }
  }

  // event listeners (todo)
  var keys = this.listenerKeys;
  this.geometryChangeKey_ = geometry.on('change',
                                        this.handleGeometryChange_,
                                        this);
  keys.push(this.geometryChangeKey_);
  keys.push(this.feature_.on(
      'change:' + this.feature_.getGeometryName(),
      this.handleGeometryReplace_, this
      ));
};


/**
 * @inheritDoc
 */
olgm.herald.Feature.prototype.deactivate = function() {

  // remove gmap feature
  this.data_.remove(this.gmapFeature_);
  this.gmapFeature_ = null;

  // remove label
  if (this.label_) {
    this.label_.setMap(null);
    this.label_ = null;
  }

  goog.base(this, 'deactivate');
};


/**
 * @private
 */
olgm.herald.Feature.prototype.handleGeometryChange_ = function() {
  var geometry = this.feature_.getGeometry();
  goog.asserts.assertInstanceof(geometry, ol.geom.Geometry);
  this.gmapFeature_.setGeometry(olgm.gm.createFeatureGeometry(geometry));

  if (this.label_) {
    var latLng = olgm.gm.createLatLng(olgm.getCenterOf(geometry));
    this.label_.set('position', latLng);
  }
};


/**
 * @private
 */
olgm.herald.Feature.prototype.handleGeometryReplace_ = function() {
  var keys = this.listenerKeys;
  ol.Observable.unByKey(this.geometryChangeKey_);
  var index = keys.indexOf(this.geometryChangeKey_);
  keys.splice(index, 1);

  this.geometryChangeKey_ = this.feature_.getGeometry().on('change',
      this.handleGeometryChange_,
      this);
  keys.push(this.geometryChangeKey_);
  this.handleGeometryChange_();
};

goog.provide('olgm.herald.VectorSource');

goog.require('goog.asserts');
goog.require('olgm.herald.Feature');
goog.require('olgm.herald.Herald');



/**
 * The VectorSource Herald is responsible of sychronizing the features from
 * an ol3 vector source. The existing features in addition of those that are
 * added and removed are all managed. Each existing or added feature is bound
 * to a `olgm.herald.Feature` object. It gets unbound when removed.
 *
 * @param {!ol.Map} ol3map
 * @param {!google.maps.Map} gmap
 * @param {!ol.source.Vector} source
 * @param {!google.maps.Data} data
 * @constructor
 * @extends {olgm.herald.Herald}
 */
olgm.herald.VectorSource = function(ol3map, gmap, source, data) {

  /**
   * @type {Array.<ol.Feature>}
   * @private
   */
  this.features_ = [];

  /**
   * @type {Array.<olgm.herald.VectorSource.Cache>}
   * @private
   */
  this.cache_ = [];

  /**
   * @type {!google.maps.Data}
   * @private
   */
  this.data_ = data;

  /**
   * @type {ol.source.Vector}
   * @private
   */
  this.source_ = source;

  goog.base(this, ol3map, gmap);
};
goog.inherits(olgm.herald.VectorSource, olgm.herald.Herald);


/**
 * @inheritDoc
 */
olgm.herald.VectorSource.prototype.activate = function() {
  goog.base(this, 'activate');

  // watch existing features...
  this.source_.getFeatures().forEach(this.watchFeature_, this);

  // event listeners
  var keys = this.listenerKeys;
  keys.push(this.source_.on('addfeature', this.handleAddFeature_, this));
  keys.push(this.source_.on('removefeature', this.handleRemoveFeature_, this));
};


/**
 * @inheritDoc
 */
olgm.herald.VectorSource.prototype.deactivate = function() {
  // unwatch existing features...
  this.source_.getFeatures().forEach(this.unwatchFeature_, this);

  goog.base(this, 'deactivate');
};


/**
 * @param {ol.source.VectorEvent} event
 * @private
 */
olgm.herald.VectorSource.prototype.handleAddFeature_ = function(event) {
  var feature = event.feature;
  goog.asserts.assertInstanceof(feature, ol.Feature);
  this.watchFeature_(feature);
};


/**
 * @param {ol.source.VectorEvent} event
 * @private
 */
olgm.herald.VectorSource.prototype.handleRemoveFeature_ = function(event) {
  var feature = event.feature;
  goog.asserts.assertInstanceof(feature, ol.Feature);
  this.unwatchFeature_(feature);
};


/**
 * @param {ol.Feature} feature
 * @private
 */
olgm.herald.VectorSource.prototype.watchFeature_ = function(feature) {

  var ol3map = this.ol3map;
  var gmap = this.gmap;
  var data = this.data_;

  // push to features (internal)
  this.features_.push(feature);

  var index = this.features_.indexOf(feature);

  // create and activate feature herald
  var herald = new olgm.herald.Feature(ol3map, gmap, feature, data, index);
  herald.activate();

  // push to cache
  this.cache_.push({
    feature: feature,
    herald: herald
  });
};


/**
 * @param {ol.Feature} feature
 * @private
 */
olgm.herald.VectorSource.prototype.unwatchFeature_ = function(feature) {
  var index = this.features_.indexOf(feature);
  if (index !== -1) {
    // remove from features (internal)
    this.features_.splice(index, 1);
    // deactivate feature herald
    this.cache_[index].herald.deactivate();
    // remove from cache
    this.cache_.splice(index, 1);
  }
};


/**
 * @typedef {{
 *   feature: (ol.Feature),
 *   herald: (olgm.herald.Feature)
 * }}
 */
olgm.herald.VectorSource.Cache;

// Copyright 2010 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview A global registry for entry points into a program,
 * so that they can be instrumented. Each module should register their
 * entry points with this registry. Designed to be compiled out
 * if no instrumentation is requested.
 *
 * Entry points may be registered before or after a call to
 * goog.debug.entryPointRegistry.monitorAll. If an entry point is registered
 * later, the existing monitor will instrument the new entry point.
 *
 * @author nicksantos@google.com (Nick Santos)
 */

goog.provide('goog.debug.EntryPointMonitor');
goog.provide('goog.debug.entryPointRegistry');

goog.require('goog.asserts');



/**
 * @interface
 */
goog.debug.EntryPointMonitor = function() {};


/**
 * Instruments a function.
 *
 * @param {!Function} fn A function to instrument.
 * @return {!Function} The instrumented function.
 */
goog.debug.EntryPointMonitor.prototype.wrap;


/**
 * Try to remove an instrumentation wrapper created by this monitor.
 * If the function passed to unwrap is not a wrapper created by this
 * monitor, then we will do nothing.
 *
 * Notice that some wrappers may not be unwrappable. For example, if other
 * monitors have applied their own wrappers, then it will be impossible to
 * unwrap them because their wrappers will have captured our wrapper.
 *
 * So it is important that entry points are unwrapped in the reverse
 * order that they were wrapped.
 *
 * @param {!Function} fn A function to unwrap.
 * @return {!Function} The unwrapped function, or {@code fn} if it was not
 *     a wrapped function created by this monitor.
 */
goog.debug.EntryPointMonitor.prototype.unwrap;


/**
 * An array of entry point callbacks.
 * @type {!Array.<function(!Function)>}
 * @private
 */
goog.debug.entryPointRegistry.refList_ = [];


/**
 * Monitors that should wrap all the entry points.
 * @type {!Array.<!goog.debug.EntryPointMonitor>}
 * @private
 */
goog.debug.entryPointRegistry.monitors_ = [];


/**
 * Whether goog.debug.entryPointRegistry.monitorAll has ever been called.
 * Checking this allows the compiler to optimize out the registrations.
 * @type {boolean}
 * @private
 */
goog.debug.entryPointRegistry.monitorsMayExist_ = false;


/**
 * Register an entry point with this module.
 *
 * The entry point will be instrumented when a monitor is passed to
 * goog.debug.entryPointRegistry.monitorAll. If this has already occurred, the
 * entry point is instrumented immediately.
 *
 * @param {function(!Function)} callback A callback function which is called
 *     with a transforming function to instrument the entry point. The callback
 *     is responsible for wrapping the relevant entry point with the
 *     transforming function.
 */
goog.debug.entryPointRegistry.register = function(callback) {
  // Don't use push(), so that this can be compiled out.
  goog.debug.entryPointRegistry.refList_[
      goog.debug.entryPointRegistry.refList_.length] = callback;
  // If no one calls monitorAll, this can be compiled out.
  if (goog.debug.entryPointRegistry.monitorsMayExist_) {
    var monitors = goog.debug.entryPointRegistry.monitors_;
    for (var i = 0; i < monitors.length; i++) {
      callback(goog.bind(monitors[i].wrap, monitors[i]));
    }
  }
};


/**
 * Configures a monitor to wrap all entry points.
 *
 * Entry points that have already been registered are immediately wrapped by
 * the monitor. When an entry point is registered in the future, it will also
 * be wrapped by the monitor when it is registered.
 *
 * @param {!goog.debug.EntryPointMonitor} monitor An entry point monitor.
 */
goog.debug.entryPointRegistry.monitorAll = function(monitor) {
  goog.debug.entryPointRegistry.monitorsMayExist_ = true;
  var transformer = goog.bind(monitor.wrap, monitor);
  for (var i = 0; i < goog.debug.entryPointRegistry.refList_.length; i++) {
    goog.debug.entryPointRegistry.refList_[i](transformer);
  }
  goog.debug.entryPointRegistry.monitors_.push(monitor);
};


/**
 * Try to unmonitor all the entry points that have already been registered. If
 * an entry point is registered in the future, it will not be wrapped by the
 * monitor when it is registered. Note that this may fail if the entry points
 * have additional wrapping.
 *
 * @param {!goog.debug.EntryPointMonitor} monitor The last monitor to wrap
 *     the entry points.
 * @throws {Error} If the monitor is not the most recently configured monitor.
 */
goog.debug.entryPointRegistry.unmonitorAllIfPossible = function(monitor) {
  var monitors = goog.debug.entryPointRegistry.monitors_;
  goog.asserts.assert(monitor == monitors[monitors.length - 1],
      'Only the most recent monitor can be unwrapped.');
  var transformer = goog.bind(monitor.unwrap, monitor);
  for (var i = 0; i < goog.debug.entryPointRegistry.refList_.length; i++) {
    goog.debug.entryPointRegistry.refList_[i](transformer);
  }
  monitors.length--;
};

// Copyright 2006 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Utilities for manipulating arrays.
 *
 */


goog.provide('goog.array');
goog.provide('goog.array.ArrayLike');

goog.require('goog.asserts');


/**
 * @define {boolean} NATIVE_ARRAY_PROTOTYPES indicates whether the code should
 * rely on Array.prototype functions, if available.
 *
 * The Array.prototype functions can be defined by external libraries like
 * Prototype and setting this flag to false forces closure to use its own
 * goog.array implementation.
 *
 * If your javascript can be loaded by a third party site and you are wary about
 * relying on the prototype functions, specify
 * "--define goog.NATIVE_ARRAY_PROTOTYPES=false" to the JSCompiler.
 *
 * Setting goog.TRUSTED_SITE to false will automatically set
 * NATIVE_ARRAY_PROTOTYPES to false.
 */
goog.define('goog.NATIVE_ARRAY_PROTOTYPES', goog.TRUSTED_SITE);


/**
 * @define {boolean} If true, JSCompiler will use the native implementation of
 * array functions where appropriate (e.g., {@code Array#filter}) and remove the
 * unused pure JS implementation.
 */
goog.define('goog.array.ASSUME_NATIVE_FUNCTIONS', false);


/**
 * @typedef {Array|NodeList|Arguments|{length: number}}
 */
goog.array.ArrayLike;


/**
 * Returns the last element in an array without removing it.
 * Same as goog.array.last.
 * @param {Array.<T>|goog.array.ArrayLike} array The array.
 * @return {T} Last item in array.
 * @template T
 */
goog.array.peek = function(array) {
  return array[array.length - 1];
};


/**
 * Returns the last element in an array without removing it.
 * Same as goog.array.peek.
 * @param {Array.<T>|goog.array.ArrayLike} array The array.
 * @return {T} Last item in array.
 * @template T
 */
goog.array.last = goog.array.peek;


/**
 * Reference to the original {@code Array.prototype}.
 * @private
 */
goog.array.ARRAY_PROTOTYPE_ = Array.prototype;


// NOTE(arv): Since most of the array functions are generic it allows you to
// pass an array-like object. Strings have a length and are considered array-
// like. However, the 'in' operator does not work on strings so we cannot just
// use the array path even if the browser supports indexing into strings. We
// therefore end up splitting the string.


/**
 * Returns the index of the first element of an array with a specified value, or
 * -1 if the element is not present in the array.
 *
 * See {@link http://tinyurl.com/developer-mozilla-org-array-indexof}
 *
 * @param {Array.<T>|goog.array.ArrayLike} arr The array to be searched.
 * @param {T} obj The object for which we are searching.
 * @param {number=} opt_fromIndex The index at which to start the search. If
 *     omitted the search starts at index 0.
 * @return {number} The index of the first matching array element.
 * @template T
 */
goog.array.indexOf = goog.NATIVE_ARRAY_PROTOTYPES &&
                     (goog.array.ASSUME_NATIVE_FUNCTIONS ||
                      goog.array.ARRAY_PROTOTYPE_.indexOf) ?
    function(arr, obj, opt_fromIndex) {
      goog.asserts.assert(arr.length != null);

      return goog.array.ARRAY_PROTOTYPE_.indexOf.call(arr, obj, opt_fromIndex);
    } :
    function(arr, obj, opt_fromIndex) {
      var fromIndex = opt_fromIndex == null ?
          0 : (opt_fromIndex < 0 ?
               Math.max(0, arr.length + opt_fromIndex) : opt_fromIndex);

      if (goog.isString(arr)) {
        // Array.prototype.indexOf uses === so only strings should be found.
        if (!goog.isString(obj) || obj.length != 1) {
          return -1;
        }
        return arr.indexOf(obj, fromIndex);
      }

      for (var i = fromIndex; i < arr.length; i++) {
        if (i in arr && arr[i] === obj)
          return i;
      }
      return -1;
    };


/**
 * Returns the index of the last element of an array with a specified value, or
 * -1 if the element is not present in the array.
 *
 * See {@link http://tinyurl.com/developer-mozilla-org-array-lastindexof}
 *
 * @param {!Array.<T>|!goog.array.ArrayLike} arr The array to be searched.
 * @param {T} obj The object for which we are searching.
 * @param {?number=} opt_fromIndex The index at which to start the search. If
 *     omitted the search starts at the end of the array.
 * @return {number} The index of the last matching array element.
 * @template T
 */
goog.array.lastIndexOf = goog.NATIVE_ARRAY_PROTOTYPES &&
                         (goog.array.ASSUME_NATIVE_FUNCTIONS ||
                          goog.array.ARRAY_PROTOTYPE_.lastIndexOf) ?
    function(arr, obj, opt_fromIndex) {
      goog.asserts.assert(arr.length != null);

      // Firefox treats undefined and null as 0 in the fromIndex argument which
      // leads it to always return -1
      var fromIndex = opt_fromIndex == null ? arr.length - 1 : opt_fromIndex;
      return goog.array.ARRAY_PROTOTYPE_.lastIndexOf.call(arr, obj, fromIndex);
    } :
    function(arr, obj, opt_fromIndex) {
      var fromIndex = opt_fromIndex == null ? arr.length - 1 : opt_fromIndex;

      if (fromIndex < 0) {
        fromIndex = Math.max(0, arr.length + fromIndex);
      }

      if (goog.isString(arr)) {
        // Array.prototype.lastIndexOf uses === so only strings should be found.
        if (!goog.isString(obj) || obj.length != 1) {
          return -1;
        }
        return arr.lastIndexOf(obj, fromIndex);
      }

      for (var i = fromIndex; i >= 0; i--) {
        if (i in arr && arr[i] === obj)
          return i;
      }
      return -1;
    };


/**
 * Calls a function for each element in an array. Skips holes in the array.
 * See {@link http://tinyurl.com/developer-mozilla-org-array-foreach}
 *
 * @param {Array.<T>|goog.array.ArrayLike} arr Array or array like object over
 *     which to iterate.
 * @param {?function(this: S, T, number, ?): ?} f The function to call for every
 *     element. This function takes 3 arguments (the element, the index and the
 *     array). The return value is ignored.
 * @param {S=} opt_obj The object to be used as the value of 'this' within f.
 * @template T,S
 */
goog.array.forEach = goog.NATIVE_ARRAY_PROTOTYPES &&
                     (goog.array.ASSUME_NATIVE_FUNCTIONS ||
                      goog.array.ARRAY_PROTOTYPE_.forEach) ?
    function(arr, f, opt_obj) {
      goog.asserts.assert(arr.length != null);

      goog.array.ARRAY_PROTOTYPE_.forEach.call(arr, f, opt_obj);
    } :
    function(arr, f, opt_obj) {
      var l = arr.length;  // must be fixed during loop... see docs
      var arr2 = goog.isString(arr) ? arr.split('') : arr;
      for (var i = 0; i < l; i++) {
        if (i in arr2) {
          f.call(opt_obj, arr2[i], i, arr);
        }
      }
    };


/**
 * Calls a function for each element in an array, starting from the last
 * element rather than the first.
 *
 * @param {Array.<T>|goog.array.ArrayLike} arr Array or array
 *     like object over which to iterate.
 * @param {?function(this: S, T, number, ?): ?} f The function to call for every
 *     element. This function
 *     takes 3 arguments (the element, the index and the array). The return
 *     value is ignored.
 * @param {S=} opt_obj The object to be used as the value of 'this'
 *     within f.
 * @template T,S
 */
goog.array.forEachRight = function(arr, f, opt_obj) {
  var l = arr.length;  // must be fixed during loop... see docs
  var arr2 = goog.isString(arr) ? arr.split('') : arr;
  for (var i = l - 1; i >= 0; --i) {
    if (i in arr2) {
      f.call(opt_obj, arr2[i], i, arr);
    }
  }
};


/**
 * Calls a function for each element in an array, and if the function returns
 * true adds the element to a new array.
 *
 * See {@link http://tinyurl.com/developer-mozilla-org-array-filter}
 *
 * @param {Array.<T>|goog.array.ArrayLike} arr Array or array
 *     like object over which to iterate.
 * @param {?function(this:S, T, number, ?):boolean} f The function to call for
 *     every element. This function
 *     takes 3 arguments (the element, the index and the array) and must
 *     return a Boolean. If the return value is true the element is added to the
 *     result array. If it is false the element is not included.
 * @param {S=} opt_obj The object to be used as the value of 'this'
 *     within f.
 * @return {!Array.<T>} a new array in which only elements that passed the test
 *     are present.
 * @template T,S
 */
goog.array.filter = goog.NATIVE_ARRAY_PROTOTYPES &&
                    (goog.array.ASSUME_NATIVE_FUNCTIONS ||
                     goog.array.ARRAY_PROTOTYPE_.filter) ?
    function(arr, f, opt_obj) {
      goog.asserts.assert(arr.length != null);

      return goog.array.ARRAY_PROTOTYPE_.filter.call(arr, f, opt_obj);
    } :
    function(arr, f, opt_obj) {
      var l = arr.length;  // must be fixed during loop... see docs
      var res = [];
      var resLength = 0;
      var arr2 = goog.isString(arr) ? arr.split('') : arr;
      for (var i = 0; i < l; i++) {
        if (i in arr2) {
          var val = arr2[i];  // in case f mutates arr2
          if (f.call(opt_obj, val, i, arr)) {
            res[resLength++] = val;
          }
        }
      }
      return res;
    };


/**
 * Calls a function for each element in an array and inserts the result into a
 * new array.
 *
 * See {@link http://tinyurl.com/developer-mozilla-org-array-map}
 *
 * @param {Array.<VALUE>|goog.array.ArrayLike} arr Array or array like object
 *     over which to iterate.
 * @param {function(this:THIS, VALUE, number, ?): RESULT} f The function to call
 *     for every element. This function takes 3 arguments (the element,
 *     the index and the array) and should return something. The result will be
 *     inserted into a new array.
 * @param {THIS=} opt_obj The object to be used as the value of 'this' within f.
 * @return {!Array.<RESULT>} a new array with the results from f.
 * @template THIS, VALUE, RESULT
 */
goog.array.map = goog.NATIVE_ARRAY_PROTOTYPES &&
                 (goog.array.ASSUME_NATIVE_FUNCTIONS ||
                  goog.array.ARRAY_PROTOTYPE_.map) ?
    function(arr, f, opt_obj) {
      goog.asserts.assert(arr.length != null);

      return goog.array.ARRAY_PROTOTYPE_.map.call(arr, f, opt_obj);
    } :
    function(arr, f, opt_obj) {
      var l = arr.length;  // must be fixed during loop... see docs
      var res = new Array(l);
      var arr2 = goog.isString(arr) ? arr.split('') : arr;
      for (var i = 0; i < l; i++) {
        if (i in arr2) {
          res[i] = f.call(opt_obj, arr2[i], i, arr);
        }
      }
      return res;
    };


/**
 * Passes every element of an array into a function and accumulates the result.
 *
 * See {@link http://tinyurl.com/developer-mozilla-org-array-reduce}
 *
 * For example:
 * var a = [1, 2, 3, 4];
 * goog.array.reduce(a, function(r, v, i, arr) {return r + v;}, 0);
 * returns 10
 *
 * @param {Array.<T>|goog.array.ArrayLike} arr Array or array
 *     like object over which to iterate.
 * @param {?function(this:S, R, T, number, ?) : R} f The function to call for
 *     every element. This function
 *     takes 4 arguments (the function's previous result or the initial value,
 *     the value of the current array element, the current array index, and the
 *     array itself)
 *     function(previousValue, currentValue, index, array).
 * @param {?} val The initial value to pass into the function on the first call.
 * @param {S=} opt_obj  The object to be used as the value of 'this'
 *     within f.
 * @return {R} Result of evaluating f repeatedly across the values of the array.
 * @template T,S,R
 */
goog.array.reduce = goog.NATIVE_ARRAY_PROTOTYPES &&
                    (goog.array.ASSUME_NATIVE_FUNCTIONS ||
                     goog.array.ARRAY_PROTOTYPE_.reduce) ?
    function(arr, f, val, opt_obj) {
      goog.asserts.assert(arr.length != null);
      if (opt_obj) {
        f = goog.bind(f, opt_obj);
      }
      return goog.array.ARRAY_PROTOTYPE_.reduce.call(arr, f, val);
    } :
    function(arr, f, val, opt_obj) {
      var rval = val;
      goog.array.forEach(arr, function(val, index) {
        rval = f.call(opt_obj, rval, val, index, arr);
      });
      return rval;
    };


/**
 * Passes every element of an array into a function and accumulates the result,
 * starting from the last element and working towards the first.
 *
 * See {@link http://tinyurl.com/developer-mozilla-org-array-reduceright}
 *
 * For example:
 * var a = ['a', 'b', 'c'];
 * goog.array.reduceRight(a, function(r, v, i, arr) {return r + v;}, '');
 * returns 'cba'
 *
 * @param {Array.<T>|goog.array.ArrayLike} arr Array or array
 *     like object over which to iterate.
 * @param {?function(this:S, R, T, number, ?) : R} f The function to call for
 *     every element. This function
 *     takes 4 arguments (the function's previous result or the initial value,
 *     the value of the current array element, the current array index, and the
 *     array itself)
 *     function(previousValue, currentValue, index, array).
 * @param {?} val The initial value to pass into the function on the first call.
 * @param {S=} opt_obj The object to be used as the value of 'this'
 *     within f.
 * @return {R} Object returned as a result of evaluating f repeatedly across the
 *     values of the array.
 * @template T,S,R
 */
goog.array.reduceRight = goog.NATIVE_ARRAY_PROTOTYPES &&
                         (goog.array.ASSUME_NATIVE_FUNCTIONS ||
                          goog.array.ARRAY_PROTOTYPE_.reduceRight) ?
    function(arr, f, val, opt_obj) {
      goog.asserts.assert(arr.length != null);
      if (opt_obj) {
        f = goog.bind(f, opt_obj);
      }
      return goog.array.ARRAY_PROTOTYPE_.reduceRight.call(arr, f, val);
    } :
    function(arr, f, val, opt_obj) {
      var rval = val;
      goog.array.forEachRight(arr, function(val, index) {
        rval = f.call(opt_obj, rval, val, index, arr);
      });
      return rval;
    };


/**
 * Calls f for each element of an array. If any call returns true, some()
 * returns true (without checking the remaining elements). If all calls
 * return false, some() returns false.
 *
 * See {@link http://tinyurl.com/developer-mozilla-org-array-some}
 *
 * @param {Array.<T>|goog.array.ArrayLike} arr Array or array
 *     like object over which to iterate.
 * @param {?function(this:S, T, number, ?) : boolean} f The function to call for
 *     for every element. This function takes 3 arguments (the element, the
 *     index and the array) and should return a boolean.
 * @param {S=} opt_obj  The object to be used as the value of 'this'
 *     within f.
 * @return {boolean} true if any element passes the test.
 * @template T,S
 */
goog.array.some = goog.NATIVE_ARRAY_PROTOTYPES &&
                  (goog.array.ASSUME_NATIVE_FUNCTIONS ||
                   goog.array.ARRAY_PROTOTYPE_.some) ?
    function(arr, f, opt_obj) {
      goog.asserts.assert(arr.length != null);

      return goog.array.ARRAY_PROTOTYPE_.some.call(arr, f, opt_obj);
    } :
    function(arr, f, opt_obj) {
      var l = arr.length;  // must be fixed during loop... see docs
      var arr2 = goog.isString(arr) ? arr.split('') : arr;
      for (var i = 0; i < l; i++) {
        if (i in arr2 && f.call(opt_obj, arr2[i], i, arr)) {
          return true;
        }
      }
      return false;
    };


/**
 * Call f for each element of an array. If all calls return true, every()
 * returns true. If any call returns false, every() returns false and
 * does not continue to check the remaining elements.
 *
 * See {@link http://tinyurl.com/developer-mozilla-org-array-every}
 *
 * @param {Array.<T>|goog.array.ArrayLike} arr Array or array
 *     like object over which to iterate.
 * @param {?function(this:S, T, number, ?) : boolean} f The function to call for
 *     for every element. This function takes 3 arguments (the element, the
 *     index and the array) and should return a boolean.
 * @param {S=} opt_obj The object to be used as the value of 'this'
 *     within f.
 * @return {boolean} false if any element fails the test.
 * @template T,S
 */
goog.array.every = goog.NATIVE_ARRAY_PROTOTYPES &&
                   (goog.array.ASSUME_NATIVE_FUNCTIONS ||
                    goog.array.ARRAY_PROTOTYPE_.every) ?
    function(arr, f, opt_obj) {
      goog.asserts.assert(arr.length != null);

      return goog.array.ARRAY_PROTOTYPE_.every.call(arr, f, opt_obj);
    } :
    function(arr, f, opt_obj) {
      var l = arr.length;  // must be fixed during loop... see docs
      var arr2 = goog.isString(arr) ? arr.split('') : arr;
      for (var i = 0; i < l; i++) {
        if (i in arr2 && !f.call(opt_obj, arr2[i], i, arr)) {
          return false;
        }
      }
      return true;
    };


/**
 * Counts the array elements that fulfill the predicate, i.e. for which the
 * callback function returns true. Skips holes in the array.
 *
 * @param {!(Array.<T>|goog.array.ArrayLike)} arr Array or array like object
 *     over which to iterate.
 * @param {function(this: S, T, number, ?): boolean} f The function to call for
 *     every element. Takes 3 arguments (the element, the index and the array).
 * @param {S=} opt_obj The object to be used as the value of 'this' within f.
 * @return {number} The number of the matching elements.
 * @template T,S
 */
goog.array.count = function(arr, f, opt_obj) {
  var count = 0;
  goog.array.forEach(arr, function(element, index, arr) {
    if (f.call(opt_obj, element, index, arr)) {
      ++count;
    }
  }, opt_obj);
  return count;
};


/**
 * Search an array for the first element that satisfies a given condition and
 * return that element.
 * @param {Array.<T>|goog.array.ArrayLike} arr Array or array
 *     like object over which to iterate.
 * @param {?function(this:S, T, number, ?) : boolean} f The function to call
 *     for every element. This function takes 3 arguments (the element, the
 *     index and the array) and should return a boolean.
 * @param {S=} opt_obj An optional "this" context for the function.
 * @return {?T} The first array element that passes the test, or null if no
 *     element is found.
 * @template T,S
 */
goog.array.find = function(arr, f, opt_obj) {
  var i = goog.array.findIndex(arr, f, opt_obj);
  return i < 0 ? null : goog.isString(arr) ? arr.charAt(i) : arr[i];
};


/**
 * Search an array for the first element that satisfies a given condition and
 * return its index.
 * @param {Array.<T>|goog.array.ArrayLike} arr Array or array
 *     like object over which to iterate.
 * @param {?function(this:S, T, number, ?) : boolean} f The function to call for
 *     every element. This function
 *     takes 3 arguments (the element, the index and the array) and should
 *     return a boolean.
 * @param {S=} opt_obj An optional "this" context for the function.
 * @return {number} The index of the first array element that passes the test,
 *     or -1 if no element is found.
 * @template T,S
 */
goog.array.findIndex = function(arr, f, opt_obj) {
  var l = arr.length;  // must be fixed during loop... see docs
  var arr2 = goog.isString(arr) ? arr.split('') : arr;
  for (var i = 0; i < l; i++) {
    if (i in arr2 && f.call(opt_obj, arr2[i], i, arr)) {
      return i;
    }
  }
  return -1;
};


/**
 * Search an array (in reverse order) for the last element that satisfies a
 * given condition and return that element.
 * @param {Array.<T>|goog.array.ArrayLike} arr Array or array
 *     like object over which to iterate.
 * @param {?function(this:S, T, number, ?) : boolean} f The function to call
 *     for every element. This function
 *     takes 3 arguments (the element, the index and the array) and should
 *     return a boolean.
 * @param {S=} opt_obj An optional "this" context for the function.
 * @return {?T} The last array element that passes the test, or null if no
 *     element is found.
 * @template T,S
 */
goog.array.findRight = function(arr, f, opt_obj) {
  var i = goog.array.findIndexRight(arr, f, opt_obj);
  return i < 0 ? null : goog.isString(arr) ? arr.charAt(i) : arr[i];
};


/**
 * Search an array (in reverse order) for the last element that satisfies a
 * given condition and return its index.
 * @param {Array.<T>|goog.array.ArrayLike} arr Array or array
 *     like object over which to iterate.
 * @param {?function(this:S, T, number, ?) : boolean} f The function to call
 *     for every element. This function
 *     takes 3 arguments (the element, the index and the array) and should
 *     return a boolean.
 * @param {Object=} opt_obj An optional "this" context for the function.
 * @return {number} The index of the last array element that passes the test,
 *     or -1 if no element is found.
 * @template T,S
 */
goog.array.findIndexRight = function(arr, f, opt_obj) {
  var l = arr.length;  // must be fixed during loop... see docs
  var arr2 = goog.isString(arr) ? arr.split('') : arr;
  for (var i = l - 1; i >= 0; i--) {
    if (i in arr2 && f.call(opt_obj, arr2[i], i, arr)) {
      return i;
    }
  }
  return -1;
};


/**
 * Whether the array contains the given object.
 * @param {goog.array.ArrayLike} arr The array to test for the presence of the
 *     element.
 * @param {*} obj The object for which to test.
 * @return {boolean} true if obj is present.
 */
goog.array.contains = function(arr, obj) {
  return goog.array.indexOf(arr, obj) >= 0;
};


/**
 * Whether the array is empty.
 * @param {goog.array.ArrayLike} arr The array to test.
 * @return {boolean} true if empty.
 */
goog.array.isEmpty = function(arr) {
  return arr.length == 0;
};


/**
 * Clears the array.
 * @param {goog.array.ArrayLike} arr Array or array like object to clear.
 */
goog.array.clear = function(arr) {
  // For non real arrays we don't have the magic length so we delete the
  // indices.
  if (!goog.isArray(arr)) {
    for (var i = arr.length - 1; i >= 0; i--) {
      delete arr[i];
    }
  }
  arr.length = 0;
};


/**
 * Pushes an item into an array, if it's not already in the array.
 * @param {Array.<T>} arr Array into which to insert the item.
 * @param {T} obj Value to add.
 * @template T
 */
goog.array.insert = function(arr, obj) {
  if (!goog.array.contains(arr, obj)) {
    arr.push(obj);
  }
};


/**
 * Inserts an object at the given index of the array.
 * @param {goog.array.ArrayLike} arr The array to modify.
 * @param {*} obj The object to insert.
 * @param {number=} opt_i The index at which to insert the object. If omitted,
 *      treated as 0. A negative index is counted from the end of the array.
 */
goog.array.insertAt = function(arr, obj, opt_i) {
  goog.array.splice(arr, opt_i, 0, obj);
};


/**
 * Inserts at the given index of the array, all elements of another array.
 * @param {goog.array.ArrayLike} arr The array to modify.
 * @param {goog.array.ArrayLike} elementsToAdd The array of elements to add.
 * @param {number=} opt_i The index at which to insert the object. If omitted,
 *      treated as 0. A negative index is counted from the end of the array.
 */
goog.array.insertArrayAt = function(arr, elementsToAdd, opt_i) {
  goog.partial(goog.array.splice, arr, opt_i, 0).apply(null, elementsToAdd);
};


/**
 * Inserts an object into an array before a specified object.
 * @param {Array.<T>} arr The array to modify.
 * @param {T} obj The object to insert.
 * @param {T=} opt_obj2 The object before which obj should be inserted. If obj2
 *     is omitted or not found, obj is inserted at the end of the array.
 * @template T
 */
goog.array.insertBefore = function(arr, obj, opt_obj2) {
  var i;
  if (arguments.length == 2 || (i = goog.array.indexOf(arr, opt_obj2)) < 0) {
    arr.push(obj);
  } else {
    goog.array.insertAt(arr, obj, i);
  }
};


/**
 * Removes the first occurrence of a particular value from an array.
 * @param {Array.<T>|goog.array.ArrayLike} arr Array from which to remove
 *     value.
 * @param {T} obj Object to remove.
 * @return {boolean} True if an element was removed.
 * @template T
 */
goog.array.remove = function(arr, obj) {
  var i = goog.array.indexOf(arr, obj);
  var rv;
  if ((rv = i >= 0)) {
    goog.array.removeAt(arr, i);
  }
  return rv;
};


/**
 * Removes from an array the element at index i
 * @param {goog.array.ArrayLike} arr Array or array like object from which to
 *     remove value.
 * @param {number} i The index to remove.
 * @return {boolean} True if an element was removed.
 */
goog.array.removeAt = function(arr, i) {
  goog.asserts.assert(arr.length != null);

  // use generic form of splice
  // splice returns the removed items and if successful the length of that
  // will be 1
  return goog.array.ARRAY_PROTOTYPE_.splice.call(arr, i, 1).length == 1;
};


/**
 * Removes the first value that satisfies the given condition.
 * @param {Array.<T>|goog.array.ArrayLike} arr Array or array
 *     like object over which to iterate.
 * @param {?function(this:S, T, number, ?) : boolean} f The function to call
 *     for every element. This function
 *     takes 3 arguments (the element, the index and the array) and should
 *     return a boolean.
 * @param {S=} opt_obj An optional "this" context for the function.
 * @return {boolean} True if an element was removed.
 * @template T,S
 */
goog.array.removeIf = function(arr, f, opt_obj) {
  var i = goog.array.findIndex(arr, f, opt_obj);
  if (i >= 0) {
    goog.array.removeAt(arr, i);
    return true;
  }
  return false;
};


/**
 * Returns a new array that is the result of joining the arguments.  If arrays
 * are passed then their items are added, however, if non-arrays are passed they
 * will be added to the return array as is.
 *
 * Note that ArrayLike objects will be added as is, rather than having their
 * items added.
 *
 * goog.array.concat([1, 2], [3, 4]) -> [1, 2, 3, 4]
 * goog.array.concat(0, [1, 2]) -> [0, 1, 2]
 * goog.array.concat([1, 2], null) -> [1, 2, null]
 *
 * There is bug in all current versions of IE (6, 7 and 8) where arrays created
 * in an iframe become corrupted soon (not immediately) after the iframe is
 * destroyed. This is common if loading data via goog.net.IframeIo, for example.
 * This corruption only affects the concat method which will start throwing
 * Catastrophic Errors (#-2147418113).
 *
 * See http://endoflow.com/scratch/corrupted-arrays.html for a test case.
 *
 * Internally goog.array should use this, so that all methods will continue to
 * work on these broken array objects.
 *
 * @param {...*} var_args Items to concatenate.  Arrays will have each item
 *     added, while primitives and objects will be added as is.
 * @return {!Array} The new resultant array.
 */
goog.array.concat = function(var_args) {
  return goog.array.ARRAY_PROTOTYPE_.concat.apply(
      goog.array.ARRAY_PROTOTYPE_, arguments);
};


/**
 * Returns a new array that contains the contents of all the arrays passed.
 * @param {...!Array.<T>} var_args
 * @return {!Array.<T>}
 * @template T
 */
goog.array.join = function(var_args) {
  return goog.array.ARRAY_PROTOTYPE_.concat.apply(
      goog.array.ARRAY_PROTOTYPE_, arguments);
};


/**
 * Converts an object to an array.
 * @param {Array.<T>|goog.array.ArrayLike} object  The object to convert to an
 *     array.
 * @return {!Array.<T>} The object converted into an array. If object has a
 *     length property, every property indexed with a non-negative number
 *     less than length will be included in the result. If object does not
 *     have a length property, an empty array will be returned.
 * @template T
 */
goog.array.toArray = function(object) {
  var length = object.length;

  // If length is not a number the following it false. This case is kept for
  // backwards compatibility since there are callers that pass objects that are
  // not array like.
  if (length > 0) {
    var rv = new Array(length);
    for (var i = 0; i < length; i++) {
      rv[i] = object[i];
    }
    return rv;
  }
  return [];
};


/**
 * Does a shallow copy of an array.
 * @param {Array.<T>|goog.array.ArrayLike} arr  Array or array-like object to
 *     clone.
 * @return {!Array.<T>} Clone of the input array.
 * @template T
 */
goog.array.clone = goog.array.toArray;


/**
 * Extends an array with another array, element, or "array like" object.
 * This function operates 'in-place', it does not create a new Array.
 *
 * Example:
 * var a = [];
 * goog.array.extend(a, [0, 1]);
 * a; // [0, 1]
 * goog.array.extend(a, 2);
 * a; // [0, 1, 2]
 *
 * @param {Array.<VALUE>} arr1  The array to modify.
 * @param {...(Array.<VALUE>|VALUE)} var_args The elements or arrays of elements
 *     to add to arr1.
 * @template VALUE
 */
goog.array.extend = function(arr1, var_args) {
  for (var i = 1; i < arguments.length; i++) {
    var arr2 = arguments[i];
    // If we have an Array or an Arguments object we can just call push
    // directly.
    var isArrayLike;
    if (goog.isArray(arr2) ||
        // Detect Arguments. ES5 says that the [[Class]] of an Arguments object
        // is "Arguments" but only V8 and JSC/Safari gets this right. We instead
        // detect Arguments by checking for array like and presence of "callee".
        (isArrayLike = goog.isArrayLike(arr2)) &&
            // The getter for callee throws an exception in strict mode
            // according to section 10.6 in ES5 so check for presence instead.
            Object.prototype.hasOwnProperty.call(arr2, 'callee')) {
      arr1.push.apply(arr1, arr2);
    } else if (isArrayLike) {
      // Otherwise loop over arr2 to prevent copying the object.
      var len1 = arr1.length;
      var len2 = arr2.length;
      for (var j = 0; j < len2; j++) {
        arr1[len1 + j] = arr2[j];
      }
    } else {
      arr1.push(arr2);
    }
  }
};


/**
 * Adds or removes elements from an array. This is a generic version of Array
 * splice. This means that it might work on other objects similar to arrays,
 * such as the arguments object.
 *
 * @param {Array.<T>|goog.array.ArrayLike} arr The array to modify.
 * @param {number|undefined} index The index at which to start changing the
 *     array. If not defined, treated as 0.
 * @param {number} howMany How many elements to remove (0 means no removal. A
 *     value below 0 is treated as zero and so is any other non number. Numbers
 *     are floored).
 * @param {...T} var_args Optional, additional elements to insert into the
 *     array.
 * @return {!Array.<T>} the removed elements.
 * @template T
 */
goog.array.splice = function(arr, index, howMany, var_args) {
  goog.asserts.assert(arr.length != null);

  return goog.array.ARRAY_PROTOTYPE_.splice.apply(
      arr, goog.array.slice(arguments, 1));
};


/**
 * Returns a new array from a segment of an array. This is a generic version of
 * Array slice. This means that it might work on other objects similar to
 * arrays, such as the arguments object.
 *
 * @param {Array.<T>|goog.array.ArrayLike} arr The array from
 * which to copy a segment.
 * @param {number} start The index of the first element to copy.
 * @param {number=} opt_end The index after the last element to copy.
 * @return {!Array.<T>} A new array containing the specified segment of the
 *     original array.
 * @template T
 */
goog.array.slice = function(arr, start, opt_end) {
  goog.asserts.assert(arr.length != null);

  // passing 1 arg to slice is not the same as passing 2 where the second is
  // null or undefined (in that case the second argument is treated as 0).
  // we could use slice on the arguments object and then use apply instead of
  // testing the length
  if (arguments.length <= 2) {
    return goog.array.ARRAY_PROTOTYPE_.slice.call(arr, start);
  } else {
    return goog.array.ARRAY_PROTOTYPE_.slice.call(arr, start, opt_end);
  }
};


/**
 * Removes all duplicates from an array (retaining only the first
 * occurrence of each array element).  This function modifies the
 * array in place and doesn't change the order of the non-duplicate items.
 *
 * For objects, duplicates are identified as having the same unique ID as
 * defined by {@link goog.getUid}.
 *
 * Alternatively you can specify a custom hash function that returns a unique
 * value for each item in the array it should consider unique.
 *
 * Runtime: N,
 * Worstcase space: 2N (no dupes)
 *
 * @param {Array.<T>|goog.array.ArrayLike} arr The array from which to remove
 *     duplicates.
 * @param {Array=} opt_rv An optional array in which to return the results,
 *     instead of performing the removal inplace.  If specified, the original
 *     array will remain unchanged.
 * @param {function(T):string=} opt_hashFn An optional function to use to
 *     apply to every item in the array. This function should return a unique
 *     value for each item in the array it should consider unique.
 * @template T
 */
goog.array.removeDuplicates = function(arr, opt_rv, opt_hashFn) {
  var returnArray = opt_rv || arr;
  var defaultHashFn = function(item) {
    // Prefix each type with a single character representing the type to
    // prevent conflicting keys (e.g. true and 'true').
    return goog.isObject(current) ? 'o' + goog.getUid(current) :
        (typeof current).charAt(0) + current;
  };
  var hashFn = opt_hashFn || defaultHashFn;

  var seen = {}, cursorInsert = 0, cursorRead = 0;
  while (cursorRead < arr.length) {
    var current = arr[cursorRead++];
    var key = hashFn(current);
    if (!Object.prototype.hasOwnProperty.call(seen, key)) {
      seen[key] = true;
      returnArray[cursorInsert++] = current;
    }
  }
  returnArray.length = cursorInsert;
};


/**
 * Searches the specified array for the specified target using the binary
 * search algorithm.  If no opt_compareFn is specified, elements are compared
 * using <code>goog.array.defaultCompare</code>, which compares the elements
 * using the built in < and > operators.  This will produce the expected
 * behavior for homogeneous arrays of String(s) and Number(s). The array
 * specified <b>must</b> be sorted in ascending order (as defined by the
 * comparison function).  If the array is not sorted, results are undefined.
 * If the array contains multiple instances of the specified target value, any
 * of these instances may be found.
 *
 * Runtime: O(log n)
 *
 * @param {Array.<VALUE>|goog.array.ArrayLike} arr The array to be searched.
 * @param {TARGET} target The sought value.
 * @param {function(TARGET, VALUE): number=} opt_compareFn Optional comparison
 *     function by which the array is ordered. Should take 2 arguments to
 *     compare, and return a negative number, zero, or a positive number
 *     depending on whether the first argument is less than, equal to, or
 *     greater than the second.
 * @return {number} Lowest index of the target value if found, otherwise
 *     (-(insertion point) - 1). The insertion point is where the value should
 *     be inserted into arr to preserve the sorted property.  Return value >= 0
 *     iff target is found.
 * @template TARGET, VALUE
 */
goog.array.binarySearch = function(arr, target, opt_compareFn) {
  return goog.array.binarySearch_(arr,
      opt_compareFn || goog.array.defaultCompare, false /* isEvaluator */,
      target);
};


/**
 * Selects an index in the specified array using the binary search algorithm.
 * The evaluator receives an element and determines whether the desired index
 * is before, at, or after it.  The evaluator must be consistent (formally,
 * goog.array.map(goog.array.map(arr, evaluator, opt_obj), goog.math.sign)
 * must be monotonically non-increasing).
 *
 * Runtime: O(log n)
 *
 * @param {Array.<VALUE>|goog.array.ArrayLike} arr The array to be searched.
 * @param {function(this:THIS, VALUE, number, ?): number} evaluator
 *     Evaluator function that receives 3 arguments (the element, the index and
 *     the array). Should return a negative number, zero, or a positive number
 *     depending on whether the desired index is before, at, or after the
 *     element passed to it.
 * @param {THIS=} opt_obj The object to be used as the value of 'this'
 *     within evaluator.
 * @return {number} Index of the leftmost element matched by the evaluator, if
 *     such exists; otherwise (-(insertion point) - 1). The insertion point is
 *     the index of the first element for which the evaluator returns negative,
 *     or arr.length if no such element exists. The return value is non-negative
 *     iff a match is found.
 * @template THIS, VALUE
 */
goog.array.binarySelect = function(arr, evaluator, opt_obj) {
  return goog.array.binarySearch_(arr, evaluator, true /* isEvaluator */,
      undefined /* opt_target */, opt_obj);
};


/**
 * Implementation of a binary search algorithm which knows how to use both
 * comparison functions and evaluators. If an evaluator is provided, will call
 * the evaluator with the given optional data object, conforming to the
 * interface defined in binarySelect. Otherwise, if a comparison function is
 * provided, will call the comparison function against the given data object.
 *
 * This implementation purposefully does not use goog.bind or goog.partial for
 * performance reasons.
 *
 * Runtime: O(log n)
 *
 * @param {Array.<VALUE>|goog.array.ArrayLike} arr The array to be searched.
 * @param {function(TARGET, VALUE): number|
 *         function(this:THIS, VALUE, number, ?): number} compareFn Either an
 *     evaluator or a comparison function, as defined by binarySearch
 *     and binarySelect above.
 * @param {boolean} isEvaluator Whether the function is an evaluator or a
 *     comparison function.
 * @param {TARGET=} opt_target If the function is a comparison function, then
 *     this is the target to binary search for.
 * @param {THIS=} opt_selfObj If the function is an evaluator, this is an
  *    optional this object for the evaluator.
 * @return {number} Lowest index of the target value if found, otherwise
 *     (-(insertion point) - 1). The insertion point is where the value should
 *     be inserted into arr to preserve the sorted property.  Return value >= 0
 *     iff target is found.
 * @template THIS, VALUE, TARGET
 * @private
 */
goog.array.binarySearch_ = function(arr, compareFn, isEvaluator, opt_target,
    opt_selfObj) {
  var left = 0;  // inclusive
  var right = arr.length;  // exclusive
  var found;
  while (left < right) {
    var middle = (left + right) >> 1;
    var compareResult;
    if (isEvaluator) {
      compareResult = compareFn.call(opt_selfObj, arr[middle], middle, arr);
    } else {
      compareResult = compareFn(opt_target, arr[middle]);
    }
    if (compareResult > 0) {
      left = middle + 1;
    } else {
      right = middle;
      // We are looking for the lowest index so we can't return immediately.
      found = !compareResult;
    }
  }
  // left is the index if found, or the insertion point otherwise.
  // ~left is a shorthand for -left - 1.
  return found ? left : ~left;
};


/**
 * Sorts the specified array into ascending order.  If no opt_compareFn is
 * specified, elements are compared using
 * <code>goog.array.defaultCompare</code>, which compares the elements using
 * the built in < and > operators.  This will produce the expected behavior
 * for homogeneous arrays of String(s) and Number(s), unlike the native sort,
 * but will give unpredictable results for heterogenous lists of strings and
 * numbers with different numbers of digits.
 *
 * This sort is not guaranteed to be stable.
 *
 * Runtime: Same as <code>Array.prototype.sort</code>
 *
 * @param {Array.<T>} arr The array to be sorted.
 * @param {?function(T,T):number=} opt_compareFn Optional comparison
 *     function by which the
 *     array is to be ordered. Should take 2 arguments to compare, and return a
 *     negative number, zero, or a positive number depending on whether the
 *     first argument is less than, equal to, or greater than the second.
 * @template T
 */
goog.array.sort = function(arr, opt_compareFn) {
  // TODO(arv): Update type annotation since null is not accepted.
  arr.sort(opt_compareFn || goog.array.defaultCompare);
};


/**
 * Sorts the specified array into ascending order in a stable way.  If no
 * opt_compareFn is specified, elements are compared using
 * <code>goog.array.defaultCompare</code>, which compares the elements using
 * the built in < and > operators.  This will produce the expected behavior
 * for homogeneous arrays of String(s) and Number(s).
 *
 * Runtime: Same as <code>Array.prototype.sort</code>, plus an additional
 * O(n) overhead of copying the array twice.
 *
 * @param {Array.<T>} arr The array to be sorted.
 * @param {?function(T, T): number=} opt_compareFn Optional comparison function
 *     by which the array is to be ordered. Should take 2 arguments to compare,
 *     and return a negative number, zero, or a positive number depending on
 *     whether the first argument is less than, equal to, or greater than the
 *     second.
 * @template T
 */
goog.array.stableSort = function(arr, opt_compareFn) {
  for (var i = 0; i < arr.length; i++) {
    arr[i] = {index: i, value: arr[i]};
  }
  var valueCompareFn = opt_compareFn || goog.array.defaultCompare;
  function stableCompareFn(obj1, obj2) {
    return valueCompareFn(obj1.value, obj2.value) || obj1.index - obj2.index;
  };
  goog.array.sort(arr, stableCompareFn);
  for (var i = 0; i < arr.length; i++) {
    arr[i] = arr[i].value;
  }
};


/**
 * Sorts an array of objects by the specified object key and compare
 * function. If no compare function is provided, the key values are
 * compared in ascending order using <code>goog.array.defaultCompare</code>.
 * This won't work for keys that get renamed by the compiler. So use
 * {'foo': 1, 'bar': 2} rather than {foo: 1, bar: 2}.
 * @param {Array.<Object>} arr An array of objects to sort.
 * @param {string} key The object key to sort by.
 * @param {Function=} opt_compareFn The function to use to compare key
 *     values.
 */
goog.array.sortObjectsByKey = function(arr, key, opt_compareFn) {
  var compare = opt_compareFn || goog.array.defaultCompare;
  goog.array.sort(arr, function(a, b) {
    return compare(a[key], b[key]);
  });
};


/**
 * Tells if the array is sorted.
 * @param {!Array.<T>} arr The array.
 * @param {?function(T,T):number=} opt_compareFn Function to compare the
 *     array elements.
 *     Should take 2 arguments to compare, and return a negative number, zero,
 *     or a positive number depending on whether the first argument is less
 *     than, equal to, or greater than the second.
 * @param {boolean=} opt_strict If true no equal elements are allowed.
 * @return {boolean} Whether the array is sorted.
 * @template T
 */
goog.array.isSorted = function(arr, opt_compareFn, opt_strict) {
  var compare = opt_compareFn || goog.array.defaultCompare;
  for (var i = 1; i < arr.length; i++) {
    var compareResult = compare(arr[i - 1], arr[i]);
    if (compareResult > 0 || compareResult == 0 && opt_strict) {
      return false;
    }
  }
  return true;
};


/**
 * Compares two arrays for equality. Two arrays are considered equal if they
 * have the same length and their corresponding elements are equal according to
 * the comparison function.
 *
 * @param {goog.array.ArrayLike} arr1 The first array to compare.
 * @param {goog.array.ArrayLike} arr2 The second array to compare.
 * @param {Function=} opt_equalsFn Optional comparison function.
 *     Should take 2 arguments to compare, and return true if the arguments
 *     are equal. Defaults to {@link goog.array.defaultCompareEquality} which
 *     compares the elements using the built-in '===' operator.
 * @return {boolean} Whether the two arrays are equal.
 */
goog.array.equals = function(arr1, arr2, opt_equalsFn) {
  if (!goog.isArrayLike(arr1) || !goog.isArrayLike(arr2) ||
      arr1.length != arr2.length) {
    return false;
  }
  var l = arr1.length;
  var equalsFn = opt_equalsFn || goog.array.defaultCompareEquality;
  for (var i = 0; i < l; i++) {
    if (!equalsFn(arr1[i], arr2[i])) {
      return false;
    }
  }
  return true;
};


/**
 * 3-way array compare function.
 * @param {!Array.<VALUE>|!goog.array.ArrayLike} arr1 The first array to
 *     compare.
 * @param {!Array.<VALUE>|!goog.array.ArrayLike} arr2 The second array to
 *     compare.
 * @param {function(VALUE, VALUE): number=} opt_compareFn Optional comparison
 *     function by which the array is to be ordered. Should take 2 arguments to
 *     compare, and return a negative number, zero, or a positive number
 *     depending on whether the first argument is less than, equal to, or
 *     greater than the second.
 * @return {number} Negative number, zero, or a positive number depending on
 *     whether the first argument is less than, equal to, or greater than the
 *     second.
 * @template VALUE
 */
goog.array.compare3 = function(arr1, arr2, opt_compareFn) {
  var compare = opt_compareFn || goog.array.defaultCompare;
  var l = Math.min(arr1.length, arr2.length);
  for (var i = 0; i < l; i++) {
    var result = compare(arr1[i], arr2[i]);
    if (result != 0) {
      return result;
    }
  }
  return goog.array.defaultCompare(arr1.length, arr2.length);
};


/**
 * Compares its two arguments for order, using the built in < and >
 * operators.
 * @param {VALUE} a The first object to be compared.
 * @param {VALUE} b The second object to be compared.
 * @return {number} A negative number, zero, or a positive number as the first
 *     argument is less than, equal to, or greater than the second.
 * @template VALUE
 */
goog.array.defaultCompare = function(a, b) {
  return a > b ? 1 : a < b ? -1 : 0;
};


/**
 * Compares its two arguments for equality, using the built in === operator.
 * @param {*} a The first object to compare.
 * @param {*} b The second object to compare.
 * @return {boolean} True if the two arguments are equal, false otherwise.
 */
goog.array.defaultCompareEquality = function(a, b) {
  return a === b;
};


/**
 * Inserts a value into a sorted array. The array is not modified if the
 * value is already present.
 * @param {Array.<VALUE>|goog.array.ArrayLike} array The array to modify.
 * @param {VALUE} value The object to insert.
 * @param {function(VALUE, VALUE): number=} opt_compareFn Optional comparison
 *     function by which the array is ordered. Should take 2 arguments to
 *     compare, and return a negative number, zero, or a positive number
 *     depending on whether the first argument is less than, equal to, or
 *     greater than the second.
 * @return {boolean} True if an element was inserted.
 * @template VALUE
 */
goog.array.binaryInsert = function(array, value, opt_compareFn) {
  var index = goog.array.binarySearch(array, value, opt_compareFn);
  if (index < 0) {
    goog.array.insertAt(array, value, -(index + 1));
    return true;
  }
  return false;
};


/**
 * Removes a value from a sorted array.
 * @param {!Array.<VALUE>|!goog.array.ArrayLike} array The array to modify.
 * @param {VALUE} value The object to remove.
 * @param {function(VALUE, VALUE): number=} opt_compareFn Optional comparison
 *     function by which the array is ordered. Should take 2 arguments to
 *     compare, and return a negative number, zero, or a positive number
 *     depending on whether the first argument is less than, equal to, or
 *     greater than the second.
 * @return {boolean} True if an element was removed.
 * @template VALUE
 */
goog.array.binaryRemove = function(array, value, opt_compareFn) {
  var index = goog.array.binarySearch(array, value, opt_compareFn);
  return (index >= 0) ? goog.array.removeAt(array, index) : false;
};


/**
 * Splits an array into disjoint buckets according to a splitting function.
 * @param {Array.<T>} array The array.
 * @param {function(this:S, T,number,Array.<T>):?} sorter Function to call for
 *     every element.  This takes 3 arguments (the element, the index and the
 *     array) and must return a valid object key (a string, number, etc), or
 *     undefined, if that object should not be placed in a bucket.
 * @param {S=} opt_obj The object to be used as the value of 'this' within
 *     sorter.
 * @return {!Object} An object, with keys being all of the unique return values
 *     of sorter, and values being arrays containing the items for
 *     which the splitter returned that key.
 * @template T,S
 */
goog.array.bucket = function(array, sorter, opt_obj) {
  var buckets = {};

  for (var i = 0; i < array.length; i++) {
    var value = array[i];
    var key = sorter.call(opt_obj, value, i, array);
    if (goog.isDef(key)) {
      // Push the value to the right bucket, creating it if necessary.
      var bucket = buckets[key] || (buckets[key] = []);
      bucket.push(value);
    }
  }

  return buckets;
};


/**
 * Creates a new object built from the provided array and the key-generation
 * function.
 * @param {Array.<T>|goog.array.ArrayLike} arr Array or array like object over
 *     which to iterate whose elements will be the values in the new object.
 * @param {?function(this:S, T, number, ?) : string} keyFunc The function to
 *     call for every element. This function takes 3 arguments (the element, the
 *     index and the array) and should return a string that will be used as the
 *     key for the element in the new object. If the function returns the same
 *     key for more than one element, the value for that key is
 *     implementation-defined.
 * @param {S=} opt_obj The object to be used as the value of 'this'
 *     within keyFunc.
 * @return {!Object.<T>} The new object.
 * @template T,S
 */
goog.array.toObject = function(arr, keyFunc, opt_obj) {
  var ret = {};
  goog.array.forEach(arr, function(element, index) {
    ret[keyFunc.call(opt_obj, element, index, arr)] = element;
  });
  return ret;
};


/**
 * Creates a range of numbers in an arithmetic progression.
 *
 * Range takes 1, 2, or 3 arguments:
 * <pre>
 * range(5) is the same as range(0, 5, 1) and produces [0, 1, 2, 3, 4]
 * range(2, 5) is the same as range(2, 5, 1) and produces [2, 3, 4]
 * range(-2, -5, -1) produces [-2, -3, -4]
 * range(-2, -5, 1) produces [], since stepping by 1 wouldn't ever reach -5.
 * </pre>
 *
 * @param {number} startOrEnd The starting value of the range if an end argument
 *     is provided. Otherwise, the start value is 0, and this is the end value.
 * @param {number=} opt_end The optional end value of the range.
 * @param {number=} opt_step The step size between range values. Defaults to 1
 *     if opt_step is undefined or 0.
 * @return {!Array.<number>} An array of numbers for the requested range. May be
 *     an empty array if adding the step would not converge toward the end
 *     value.
 */
goog.array.range = function(startOrEnd, opt_end, opt_step) {
  var array = [];
  var start = 0;
  var end = startOrEnd;
  var step = opt_step || 1;
  if (opt_end !== undefined) {
    start = startOrEnd;
    end = opt_end;
  }

  if (step * (end - start) < 0) {
    // Sign mismatch: start + step will never reach the end value.
    return [];
  }

  if (step > 0) {
    for (var i = start; i < end; i += step) {
      array.push(i);
    }
  } else {
    for (var i = start; i > end; i += step) {
      array.push(i);
    }
  }
  return array;
};


/**
 * Returns an array consisting of the given value repeated N times.
 *
 * @param {VALUE} value The value to repeat.
 * @param {number} n The repeat count.
 * @return {!Array.<VALUE>} An array with the repeated value.
 * @template VALUE
 */
goog.array.repeat = function(value, n) {
  var array = [];
  for (var i = 0; i < n; i++) {
    array[i] = value;
  }
  return array;
};


/**
 * Returns an array consisting of every argument with all arrays
 * expanded in-place recursively.
 *
 * @param {...*} var_args The values to flatten.
 * @return {!Array} An array containing the flattened values.
 */
goog.array.flatten = function(var_args) {
  var result = [];
  for (var i = 0; i < arguments.length; i++) {
    var element = arguments[i];
    if (goog.isArray(element)) {
      result.push.apply(result, goog.array.flatten.apply(null, element));
    } else {
      result.push(element);
    }
  }
  return result;
};


/**
 * Rotates an array in-place. After calling this method, the element at
 * index i will be the element previously at index (i - n) %
 * array.length, for all values of i between 0 and array.length - 1,
 * inclusive.
 *
 * For example, suppose list comprises [t, a, n, k, s]. After invoking
 * rotate(array, 1) (or rotate(array, -4)), array will comprise [s, t, a, n, k].
 *
 * @param {!Array.<T>} array The array to rotate.
 * @param {number} n The amount to rotate.
 * @return {!Array.<T>} The array.
 * @template T
 */
goog.array.rotate = function(array, n) {
  goog.asserts.assert(array.length != null);

  if (array.length) {
    n %= array.length;
    if (n > 0) {
      goog.array.ARRAY_PROTOTYPE_.unshift.apply(array, array.splice(-n, n));
    } else if (n < 0) {
      goog.array.ARRAY_PROTOTYPE_.push.apply(array, array.splice(0, -n));
    }
  }
  return array;
};


/**
 * Moves one item of an array to a new position keeping the order of the rest
 * of the items. Example use case: keeping a list of JavaScript objects
 * synchronized with the corresponding list of DOM elements after one of the
 * elements has been dragged to a new position.
 * @param {!(Array|Arguments|{length:number})} arr The array to modify.
 * @param {number} fromIndex Index of the item to move between 0 and
 *     {@code arr.length - 1}.
 * @param {number} toIndex Target index between 0 and {@code arr.length - 1}.
 */
goog.array.moveItem = function(arr, fromIndex, toIndex) {
  goog.asserts.assert(fromIndex >= 0 && fromIndex < arr.length);
  goog.asserts.assert(toIndex >= 0 && toIndex < arr.length);
  // Remove 1 item at fromIndex.
  var removedItems = goog.array.ARRAY_PROTOTYPE_.splice.call(arr, fromIndex, 1);
  // Insert the removed item at toIndex.
  goog.array.ARRAY_PROTOTYPE_.splice.call(arr, toIndex, 0, removedItems[0]);
  // We don't use goog.array.insertAt and goog.array.removeAt, because they're
  // significantly slower than splice.
};


/**
 * Creates a new array for which the element at position i is an array of the
 * ith element of the provided arrays.  The returned array will only be as long
 * as the shortest array provided; additional values are ignored.  For example,
 * the result of zipping [1, 2] and [3, 4, 5] is [[1,3], [2, 4]].
 *
 * This is similar to the zip() function in Python.  See {@link
 * http://docs.python.org/library/functions.html#zip}
 *
 * @param {...!goog.array.ArrayLike} var_args Arrays to be combined.
 * @return {!Array.<!Array>} A new array of arrays created from provided arrays.
 */
goog.array.zip = function(var_args) {
  if (!arguments.length) {
    return [];
  }
  var result = [];
  for (var i = 0; true; i++) {
    var value = [];
    for (var j = 0; j < arguments.length; j++) {
      var arr = arguments[j];
      // If i is larger than the array length, this is the shortest array.
      if (i >= arr.length) {
        return result;
      }
      value.push(arr[i]);
    }
    result.push(value);
  }
};


/**
 * Shuffles the values in the specified array using the Fisher-Yates in-place
 * shuffle (also known as the Knuth Shuffle). By default, calls Math.random()
 * and so resets the state of that random number generator. Similarly, may reset
 * the state of the any other specified random number generator.
 *
 * Runtime: O(n)
 *
 * @param {!Array} arr The array to be shuffled.
 * @param {function():number=} opt_randFn Optional random function to use for
 *     shuffling.
 *     Takes no arguments, and returns a random number on the interval [0, 1).
 *     Defaults to Math.random() using JavaScript's built-in Math library.
 */
goog.array.shuffle = function(arr, opt_randFn) {
  var randFn = opt_randFn || Math.random;

  for (var i = arr.length - 1; i > 0; i--) {
    // Choose a random array index in [0, i] (inclusive with i).
    var j = Math.floor(randFn() * (i + 1));

    var tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
  }
};

// Copyright 2013 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Utilities used by goog.labs.userAgent tools. These functions
 * should not be used outside of goog.labs.userAgent.*.
 *
 * @visibility {//closure/goog/bin/sizetests:__pkg__}
 * @visibility {//closure/goog/dom:__subpackages__}
 * @visibility {//closure/goog/style:__pkg__}
 * @visibility {//closure/goog/testing:__pkg__}
 * @visibility {//closure/goog/useragent:__subpackages__}
 * @visibility {//testing/puppet/modules:__pkg__} *
 *
 * @author nnaze@google.com (Nathan Naze)
 */

goog.provide('goog.labs.userAgent.util');

goog.require('goog.string');


/**
 * Gets the native userAgent string from navigator if it exists.
 * If navigator or navigator.userAgent string is missing, returns an empty
 * string.
 * @return {string}
 * @private
 */
goog.labs.userAgent.util.getNativeUserAgentString_ = function() {
  var navigator = goog.labs.userAgent.util.getNavigator_();
  if (navigator) {
    var userAgent = navigator.userAgent;
    if (userAgent) {
      return userAgent;
    }
  }
  return '';
};


/**
 * Getter for the native navigator.
 * This is a separate function so it can be stubbed out in testing.
 * @return {Navigator}
 * @private
 */
goog.labs.userAgent.util.getNavigator_ = function() {
  return goog.global.navigator;
};


/**
 * A possible override for applications which wish to not check
 * navigator.userAgent but use a specified value for detection instead.
 * @private {string}
 */
goog.labs.userAgent.util.userAgent_ =
    goog.labs.userAgent.util.getNativeUserAgentString_();


/**
 * Applications may override browser detection on the built in
 * navigator.userAgent object by setting this string. Set to null to use the
 * browser object instead.
 * @param {?string=} opt_userAgent The User-Agent override.
 */
goog.labs.userAgent.util.setUserAgent = function(opt_userAgent) {
  goog.labs.userAgent.util.userAgent_ = opt_userAgent ||
      goog.labs.userAgent.util.getNativeUserAgentString_();
};


/**
 * @return {string} The user agent string.
 */
goog.labs.userAgent.util.getUserAgent = function() {
  return goog.labs.userAgent.util.userAgent_;
};


/**
 * @param {string} str
 * @return {boolean} Whether the user agent contains the given string, ignoring
 *     case.
 */
goog.labs.userAgent.util.matchUserAgent = function(str) {
  var userAgent = goog.labs.userAgent.util.getUserAgent();
  return goog.string.contains(userAgent, str);
};


/**
 * @param {string} str
 * @return {boolean} Whether the user agent contains the given string.
 */
goog.labs.userAgent.util.matchUserAgentIgnoreCase = function(str) {
  var userAgent = goog.labs.userAgent.util.getUserAgent();
  return goog.string.caseInsensitiveContains(userAgent, str);
};


/**
 * Parses the user agent into tuples for each section.
 * @param {string} userAgent
 * @return {!Array.<!Array.<string>>} Tuples of key, version, and the contents
 *     of the parenthetical.
 */
goog.labs.userAgent.util.extractVersionTuples = function(userAgent) {
  // Matches each section of a user agent string.
  // Example UA:
  // Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; en-us)
  // AppleWebKit/531.21.10 (KHTML, like Gecko) Mobile/7B405
  // This has three version tuples: Mozilla, AppleWebKit, and Mobile.

  var versionRegExp = new RegExp(
      // Key. Note that a key may have a space.
      // (i.e. 'Mobile Safari' in 'Mobile Safari/5.0')
      '(\\w[\\w ]+)' +

      '/' +                // slash
      '([^\\s]+)' +        // version (i.e. '5.0b')
      '\\s*' +             // whitespace
      '(?:\\((.*?)\\))?',  // parenthetical info. parentheses not matched.
      'g');

  var data = [];
  var match;

  // Iterate and collect the version tuples.  Each iteration will be the
  // next regex match.
  while (match = versionRegExp.exec(userAgent)) {
    data.push([
      match[1],  // key
      match[2],  // value
      // || undefined as this is not undefined in IE7 and IE8
      match[3] || undefined  // info
    ]);
  }

  return data;
};


// Copyright 2013 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Closure user agent detection (Browser).
 * @see <a href="http://www.useragentstring.com/">User agent strings</a>
 * For more information on rendering engine, platform, or device see the other
 * sub-namespaces in goog.labs.userAgent, goog.labs.userAgent.platform,
 * goog.labs.userAgent.device respectively.)
 *
 */

goog.provide('goog.labs.userAgent.browser');

goog.require('goog.array');
goog.require('goog.asserts');
goog.require('goog.labs.userAgent.util');
goog.require('goog.string');


/**
 * @return {boolean} Whether the user's browser is Opera.
 * @private
 */
goog.labs.userAgent.browser.matchOpera_ = function() {
  return goog.labs.userAgent.util.matchUserAgent('Opera') ||
      goog.labs.userAgent.util.matchUserAgent('OPR');
};


/**
 * @return {boolean} Whether the user's browser is IE.
 * @private
 */
goog.labs.userAgent.browser.matchIE_ = function() {
  return goog.labs.userAgent.util.matchUserAgent('Trident') ||
      goog.labs.userAgent.util.matchUserAgent('MSIE');
};


/**
 * @return {boolean} Whether the user's browser is Firefox.
 * @private
 */
goog.labs.userAgent.browser.matchFirefox_ = function() {
  return goog.labs.userAgent.util.matchUserAgent('Firefox');
};


/**
 * @return {boolean} Whether the user's browser is Safari.
 * @private
 */
goog.labs.userAgent.browser.matchSafari_ = function() {
  return goog.labs.userAgent.util.matchUserAgent('Safari') &&
      !goog.labs.userAgent.util.matchUserAgent('Chrome') &&
      !goog.labs.userAgent.util.matchUserAgent('CriOS') &&
      !goog.labs.userAgent.util.matchUserAgent('Android');
};


/**
 * @return {boolean} Whether the user's browser is Chrome.
 * @private
 */
goog.labs.userAgent.browser.matchChrome_ = function() {
  return goog.labs.userAgent.util.matchUserAgent('Chrome') ||
      goog.labs.userAgent.util.matchUserAgent('CriOS');
};


/**
 * @return {boolean} Whether the user's browser is the Android browser.
 * @private
 */
goog.labs.userAgent.browser.matchAndroidBrowser_ = function() {
  return goog.labs.userAgent.util.matchUserAgent('Android') &&
      !goog.labs.userAgent.util.matchUserAgent('Chrome') &&
      !goog.labs.userAgent.util.matchUserAgent('CriOS');
};


/**
 * @return {boolean} Whether the user's browser is Opera.
 */
goog.labs.userAgent.browser.isOpera = goog.labs.userAgent.browser.matchOpera_;


/**
 * @return {boolean} Whether the user's browser is IE.
 */
goog.labs.userAgent.browser.isIE = goog.labs.userAgent.browser.matchIE_;


/**
 * @return {boolean} Whether the user's browser is Firefox.
 */
goog.labs.userAgent.browser.isFirefox =
    goog.labs.userAgent.browser.matchFirefox_;


/**
 * @return {boolean} Whether the user's browser is Safari.
 */
goog.labs.userAgent.browser.isSafari =
    goog.labs.userAgent.browser.matchSafari_;


/**
 * @return {boolean} Whether the user's browser is Chrome.
 */
goog.labs.userAgent.browser.isChrome =
    goog.labs.userAgent.browser.matchChrome_;


/**
 * @return {boolean} Whether the user's browser is the Android browser.
 */
goog.labs.userAgent.browser.isAndroidBrowser =
    goog.labs.userAgent.browser.matchAndroidBrowser_;


/**
 * For more information, see:
 * http://docs.aws.amazon.com/silk/latest/developerguide/user-agent.html
 * @return {boolean} Whether the user's browser is Silk.
 */
goog.labs.userAgent.browser.isSilk = function() {
  return goog.labs.userAgent.util.matchUserAgent('Silk');
};


/**
 * @return {string} The browser version or empty string if version cannot be
 *     determined. Note that for Internet Explorer, this returns the version of
 *     the browser, not the version of the rendering engine. (IE 8 in
 *     compatibility mode will return 8.0 rather than 7.0. To determine the
 *     rendering engine version, look at document.documentMode instead. See
 *     http://msdn.microsoft.com/en-us/library/cc196988(v=vs.85).aspx for more
 *     details.)
 */
goog.labs.userAgent.browser.getVersion = function() {
  var userAgentString = goog.labs.userAgent.util.getUserAgent();
  // Special case IE since IE's version is inside the parenthesis and
  // without the '/'.
  if (goog.labs.userAgent.browser.isIE()) {
    return goog.labs.userAgent.browser.getIEVersion_(userAgentString);
  }

  if (goog.labs.userAgent.browser.isOpera()) {
    return goog.labs.userAgent.browser.getOperaVersion_(userAgentString);
  }

  var versionTuples =
      goog.labs.userAgent.util.extractVersionTuples(userAgentString);
  return goog.labs.userAgent.browser.getVersionFromTuples_(versionTuples);
};


/**
 * @param {string|number} version The version to check.
 * @return {boolean} Whether the browser version is higher or the same as the
 *     given version.
 */
goog.labs.userAgent.browser.isVersionOrHigher = function(version) {
  return goog.string.compareVersions(goog.labs.userAgent.browser.getVersion(),
                                     version) >= 0;
};


/**
 * Determines IE version. More information:
 * http://msdn.microsoft.com/en-us/library/ie/bg182625(v=vs.85).aspx#uaString
 * http://msdn.microsoft.com/en-us/library/hh869301(v=vs.85).aspx
 * http://blogs.msdn.com/b/ie/archive/2010/03/23/introducing-ie9-s-user-agent-string.aspx
 * http://blogs.msdn.com/b/ie/archive/2009/01/09/the-internet-explorer-8-user-agent-string-updated-edition.aspx
 *
 * @param {string} userAgent the User-Agent.
 * @return {string}
 * @private
 */
goog.labs.userAgent.browser.getIEVersion_ = function(userAgent) {
  // IE11 may identify itself as MSIE 9.0 or MSIE 10.0 due to an IE 11 upgrade
  // bug. Example UA:
  // Mozilla/5.0 (MSIE 9.0; Windows NT 6.1; WOW64; Trident/7.0; rv:11.0)
  // like Gecko.
  // See http://www.whatismybrowser.com/developers/unknown-user-agent-fragments.
  var rv = /rv: *([\d\.]*)/.exec(userAgent);
  if (rv && rv[1]) {
    return rv[1];
  }

  var version = '';
  var msie = /MSIE +([\d\.]+)/.exec(userAgent);
  if (msie && msie[1]) {
    // IE in compatibility mode usually identifies itself as MSIE 7.0; in this
    // case, use the Trident version to determine the version of IE. For more
    // details, see the links above.
    var tridentVersion = /Trident\/(\d.\d)/.exec(userAgent);
    if (msie[1] == '7.0') {
      if (tridentVersion && tridentVersion[1]) {
        switch (tridentVersion[1]) {
          case '4.0':
            version = '8.0';
            break;
          case '5.0':
            version = '9.0';
            break;
          case '6.0':
            version = '10.0';
            break;
          case '7.0':
            version = '11.0';
            break;
        }
      } else {
        version = '7.0';
      }
    } else {
      version = msie[1];
    }
  }
  return version;
};


/**
 * Determines Opera version. More information:
 * http://my.opera.com/ODIN/blog/2013/07/15/opera-user-agent-strings-opera-15-and-beyond
 *
 * @param {string} userAgent The User-Agent.
 * @return {string}
 * @private
 */
goog.labs.userAgent.browser.getOperaVersion_ = function(userAgent) {
  var versionTuples =
      goog.labs.userAgent.util.extractVersionTuples(userAgent);
  var lastTuple = goog.array.peek(versionTuples);
  if (lastTuple[0] == 'OPR' && lastTuple[1]) {
    return lastTuple[1];
  }

  return goog.labs.userAgent.browser.getVersionFromTuples_(versionTuples);
};


/**
 * Nearly all User-Agents start with Mozilla/N.0. This looks at the second tuple
 * for the actual browser version number.
 * @param {!Array.<!Array.<string>>} versionTuples
 * @return {string} The version or empty string if it cannot be determined.
 * @private
 */
goog.labs.userAgent.browser.getVersionFromTuples_ = function(versionTuples) {
  // versionTuples[2] (The first X/Y tuple after the parenthesis) contains the
  // browser version number.
  goog.asserts.assert(versionTuples.length > 2,
      'Couldn\'t extract version tuple from user agent string');
  return versionTuples[2] && versionTuples[2][1] ? versionTuples[2][1] : '';
};

// Copyright 2013 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Closure user agent detection.
 * @see http://en.wikipedia.org/wiki/User_agent
 * For more information on browser brand, platform, or device see the other
 * sub-namespaces in goog.labs.userAgent (browser, platform, and device).
 *
 */

goog.provide('goog.labs.userAgent.engine');

goog.require('goog.array');
goog.require('goog.labs.userAgent.util');
goog.require('goog.string');


/**
 * @return {boolean} Whether the rendering engine is Presto.
 */
goog.labs.userAgent.engine.isPresto = function() {
  return goog.labs.userAgent.util.matchUserAgent('Presto');
};


/**
 * @return {boolean} Whether the rendering engine is Trident.
 */
goog.labs.userAgent.engine.isTrident = function() {
  // IE only started including the Trident token in IE8.
  return goog.labs.userAgent.util.matchUserAgent('Trident') ||
      goog.labs.userAgent.util.matchUserAgent('MSIE');
};


/**
 * @return {boolean} Whether the rendering engine is WebKit.
 */
goog.labs.userAgent.engine.isWebKit = function() {
  return goog.labs.userAgent.util.matchUserAgentIgnoreCase('WebKit');
};


/**
 * @return {boolean} Whether the rendering engine is Gecko.
 */
goog.labs.userAgent.engine.isGecko = function() {
  return goog.labs.userAgent.util.matchUserAgent('Gecko') &&
      !goog.labs.userAgent.engine.isWebKit() &&
      !goog.labs.userAgent.engine.isTrident();
};


/**
 * @return {string} The rendering engine's version or empty string if version
 *     can't be determined.
 */
goog.labs.userAgent.engine.getVersion = function() {
  var userAgentString = goog.labs.userAgent.util.getUserAgent();
  if (userAgentString) {
    var tuples = goog.labs.userAgent.util.extractVersionTuples(
        userAgentString);

    var engineTuple = tuples[1];
    if (engineTuple) {
      // In Gecko, the version string is either in the browser info or the
      // Firefox version.  See Gecko user agent string reference:
      // http://goo.gl/mULqa
      if (engineTuple[0] == 'Gecko') {
        return goog.labs.userAgent.engine.getVersionForKey_(
            tuples, 'Firefox');
      }

      return engineTuple[1];
    }

    // IE has only one version identifier, and the Trident version is
    // specified in the parenthetical.
    var browserTuple = tuples[0];
    var info;
    if (browserTuple && (info = browserTuple[2])) {
      var match = /Trident\/([^\s;]+)/.exec(info);
      if (match) {
        return match[1];
      }
    }
  }
  return '';
};


/**
 * @param {string|number} version The version to check.
 * @return {boolean} Whether the rendering engine version is higher or the same
 *     as the given version.
 */
goog.labs.userAgent.engine.isVersionOrHigher = function(version) {
  return goog.string.compareVersions(goog.labs.userAgent.engine.getVersion(),
                                     version) >= 0;
};


/**
 * @param {!Array.<!Array.<string>>} tuples Version tuples.
 * @param {string} key The key to look for.
 * @return {string} The version string of the given key, if present.
 *     Otherwise, the empty string.
 * @private
 */
goog.labs.userAgent.engine.getVersionForKey_ = function(tuples, key) {
  // TODO(nnaze): Move to util if useful elsewhere.

  var pair = goog.array.find(tuples, function(pair) {
    return key == pair[0];
  });

  return pair && pair[1] || '';
};

// Copyright 2006 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Rendering engine detection.
 * @see <a href="http://www.useragentstring.com/">User agent strings</a>
 * For information on the browser brand (such as Safari versus Chrome), see
 * goog.userAgent.product.
 * @see ../demos/useragent.html
 */

goog.provide('goog.userAgent');

goog.require('goog.labs.userAgent.browser');
goog.require('goog.labs.userAgent.engine');
goog.require('goog.labs.userAgent.util');
goog.require('goog.string');


/**
 * @define {boolean} Whether we know at compile-time that the browser is IE.
 */
goog.define('goog.userAgent.ASSUME_IE', false);


/**
 * @define {boolean} Whether we know at compile-time that the browser is GECKO.
 */
goog.define('goog.userAgent.ASSUME_GECKO', false);


/**
 * @define {boolean} Whether we know at compile-time that the browser is WEBKIT.
 */
goog.define('goog.userAgent.ASSUME_WEBKIT', false);


/**
 * @define {boolean} Whether we know at compile-time that the browser is a
 *     mobile device running WebKit e.g. iPhone or Android.
 */
goog.define('goog.userAgent.ASSUME_MOBILE_WEBKIT', false);


/**
 * @define {boolean} Whether we know at compile-time that the browser is OPERA.
 */
goog.define('goog.userAgent.ASSUME_OPERA', false);


/**
 * @define {boolean} Whether the
 *     {@code goog.userAgent.isVersionOrHigher}
 *     function will return true for any version.
 */
goog.define('goog.userAgent.ASSUME_ANY_VERSION', false);


/**
 * Whether we know the browser engine at compile-time.
 * @type {boolean}
 * @private
 */
goog.userAgent.BROWSER_KNOWN_ =
    goog.userAgent.ASSUME_IE ||
    goog.userAgent.ASSUME_GECKO ||
    goog.userAgent.ASSUME_MOBILE_WEBKIT ||
    goog.userAgent.ASSUME_WEBKIT ||
    goog.userAgent.ASSUME_OPERA;


/**
 * Returns the userAgent string for the current browser.
 *
 * @return {string} The userAgent string.
 */
goog.userAgent.getUserAgentString = function() {
  return goog.labs.userAgent.util.getUserAgent();
};


/**
 * TODO(nnaze): Change type to "Navigator" and update compilation targets.
 * @return {Object} The native navigator object.
 */
goog.userAgent.getNavigator = function() {
  // Need a local navigator reference instead of using the global one,
  // to avoid the rare case where they reference different objects.
  // (in a WorkerPool, for example).
  return goog.global['navigator'] || null;
};


/**
 * Whether the user agent is Opera.
 * @type {boolean}
 */
goog.userAgent.OPERA = goog.userAgent.BROWSER_KNOWN_ ?
    goog.userAgent.ASSUME_OPERA :
    goog.labs.userAgent.browser.isOpera();


/**
 * Whether the user agent is Internet Explorer.
 * @type {boolean}
 */
goog.userAgent.IE = goog.userAgent.BROWSER_KNOWN_ ?
    goog.userAgent.ASSUME_IE :
    goog.labs.userAgent.browser.isIE();


/**
 * Whether the user agent is Gecko. Gecko is the rendering engine used by
 * Mozilla, Firefox, and others.
 * @type {boolean}
 */
goog.userAgent.GECKO = goog.userAgent.BROWSER_KNOWN_ ?
    goog.userAgent.ASSUME_GECKO :
    goog.labs.userAgent.engine.isGecko();


/**
 * Whether the user agent is WebKit. WebKit is the rendering engine that
 * Safari, Android and others use.
 * @type {boolean}
 */
goog.userAgent.WEBKIT = goog.userAgent.BROWSER_KNOWN_ ?
    goog.userAgent.ASSUME_WEBKIT || goog.userAgent.ASSUME_MOBILE_WEBKIT :
    goog.labs.userAgent.engine.isWebKit();


/**
 * Whether the user agent is running on a mobile device.
 *
 * This is a separate function so that the logic can be tested.
 *
 * TODO(nnaze): Investigate swapping in goog.labs.userAgent.device.isMobile().
 *
 * @return {boolean} Whether the user agent is running on a mobile device.
 * @private
 */
goog.userAgent.isMobile_ = function() {
  return goog.userAgent.WEBKIT &&
         goog.labs.userAgent.util.matchUserAgent('Mobile');
};


/**
 * Whether the user agent is running on a mobile device.
 *
 * TODO(nnaze): Consider deprecating MOBILE when labs.userAgent
 *   is promoted as the gecko/webkit logic is likely inaccurate.
 *
 * @type {boolean}
 */
goog.userAgent.MOBILE = goog.userAgent.ASSUME_MOBILE_WEBKIT ||
                        goog.userAgent.isMobile_();


/**
 * Used while transitioning code to use WEBKIT instead.
 * @type {boolean}
 * @deprecated Use {@link goog.userAgent.product.SAFARI} instead.
 * TODO(nicksantos): Delete this from goog.userAgent.
 */
goog.userAgent.SAFARI = goog.userAgent.WEBKIT;


/**
 * @return {string} the platform (operating system) the user agent is running
 *     on. Default to empty string because navigator.platform may not be defined
 *     (on Rhino, for example).
 * @private
 */
goog.userAgent.determinePlatform_ = function() {
  var navigator = goog.userAgent.getNavigator();
  return navigator && navigator.platform || '';
};


/**
 * The platform (operating system) the user agent is running on. Default to
 * empty string because navigator.platform may not be defined (on Rhino, for
 * example).
 * @type {string}
 */
goog.userAgent.PLATFORM = goog.userAgent.determinePlatform_();


/**
 * @define {boolean} Whether the user agent is running on a Macintosh operating
 *     system.
 */
goog.define('goog.userAgent.ASSUME_MAC', false);


/**
 * @define {boolean} Whether the user agent is running on a Windows operating
 *     system.
 */
goog.define('goog.userAgent.ASSUME_WINDOWS', false);


/**
 * @define {boolean} Whether the user agent is running on a Linux operating
 *     system.
 */
goog.define('goog.userAgent.ASSUME_LINUX', false);


/**
 * @define {boolean} Whether the user agent is running on a X11 windowing
 *     system.
 */
goog.define('goog.userAgent.ASSUME_X11', false);


/**
 * @define {boolean} Whether the user agent is running on Android.
 */
goog.define('goog.userAgent.ASSUME_ANDROID', false);


/**
 * @define {boolean} Whether the user agent is running on an iPhone.
 */
goog.define('goog.userAgent.ASSUME_IPHONE', false);


/**
 * @define {boolean} Whether the user agent is running on an iPad.
 */
goog.define('goog.userAgent.ASSUME_IPAD', false);


/**
 * @type {boolean}
 * @private
 */
goog.userAgent.PLATFORM_KNOWN_ =
    goog.userAgent.ASSUME_MAC ||
    goog.userAgent.ASSUME_WINDOWS ||
    goog.userAgent.ASSUME_LINUX ||
    goog.userAgent.ASSUME_X11 ||
    goog.userAgent.ASSUME_ANDROID ||
    goog.userAgent.ASSUME_IPHONE ||
    goog.userAgent.ASSUME_IPAD;


/**
 * Initialize the goog.userAgent constants that define which platform the user
 * agent is running on.
 * @private
 */
goog.userAgent.initPlatform_ = function() {
  /**
   * Whether the user agent is running on a Macintosh operating system.
   * @type {boolean}
   * @private
   */
  goog.userAgent.detectedMac_ = goog.string.contains(goog.userAgent.PLATFORM,
      'Mac');

  /**
   * Whether the user agent is running on a Windows operating system.
   * @type {boolean}
   * @private
   */
  goog.userAgent.detectedWindows_ = goog.string.contains(
      goog.userAgent.PLATFORM, 'Win');

  /**
   * Whether the user agent is running on a Linux operating system.
   * @type {boolean}
   * @private
   */
  goog.userAgent.detectedLinux_ = goog.string.contains(goog.userAgent.PLATFORM,
      'Linux');

  /**
   * Whether the user agent is running on a X11 windowing system.
   * @type {boolean}
   * @private
   */
  goog.userAgent.detectedX11_ = !!goog.userAgent.getNavigator() &&
      goog.string.contains(goog.userAgent.getNavigator()['appVersion'] || '',
          'X11');

  // Need user agent string for Android/IOS detection
  var ua = goog.userAgent.getUserAgentString();

  /**
   * Whether the user agent is running on Android.
   * @type {boolean}
   * @private
   */
  goog.userAgent.detectedAndroid_ = !!ua &&
      goog.string.contains(ua, 'Android');

  /**
   * Whether the user agent is running on an iPhone.
   * @type {boolean}
   * @private
   */
  goog.userAgent.detectedIPhone_ = !!ua && goog.string.contains(ua, 'iPhone');

  /**
   * Whether the user agent is running on an iPad.
   * @type {boolean}
   * @private
   */
  goog.userAgent.detectedIPad_ = !!ua && goog.string.contains(ua, 'iPad');
};


if (!goog.userAgent.PLATFORM_KNOWN_) {
  goog.userAgent.initPlatform_();
}


/**
 * Whether the user agent is running on a Macintosh operating system.
 * @type {boolean}
 */
goog.userAgent.MAC = goog.userAgent.PLATFORM_KNOWN_ ?
    goog.userAgent.ASSUME_MAC : goog.userAgent.detectedMac_;


/**
 * Whether the user agent is running on a Windows operating system.
 * @type {boolean}
 */
goog.userAgent.WINDOWS = goog.userAgent.PLATFORM_KNOWN_ ?
    goog.userAgent.ASSUME_WINDOWS : goog.userAgent.detectedWindows_;


/**
 * Whether the user agent is running on a Linux operating system.
 * @type {boolean}
 */
goog.userAgent.LINUX = goog.userAgent.PLATFORM_KNOWN_ ?
    goog.userAgent.ASSUME_LINUX : goog.userAgent.detectedLinux_;


/**
 * Whether the user agent is running on a X11 windowing system.
 * @type {boolean}
 */
goog.userAgent.X11 = goog.userAgent.PLATFORM_KNOWN_ ?
    goog.userAgent.ASSUME_X11 : goog.userAgent.detectedX11_;


/**
 * Whether the user agent is running on Android.
 * @type {boolean}
 */
goog.userAgent.ANDROID = goog.userAgent.PLATFORM_KNOWN_ ?
    goog.userAgent.ASSUME_ANDROID : goog.userAgent.detectedAndroid_;


/**
 * Whether the user agent is running on an iPhone.
 * @type {boolean}
 */
goog.userAgent.IPHONE = goog.userAgent.PLATFORM_KNOWN_ ?
    goog.userAgent.ASSUME_IPHONE : goog.userAgent.detectedIPhone_;


/**
 * Whether the user agent is running on an iPad.
 * @type {boolean}
 */
goog.userAgent.IPAD = goog.userAgent.PLATFORM_KNOWN_ ?
    goog.userAgent.ASSUME_IPAD : goog.userAgent.detectedIPad_;


/**
 * @return {string} The string that describes the version number of the user
 *     agent.
 * @private
 */
goog.userAgent.determineVersion_ = function() {
  // All browsers have different ways to detect the version and they all have
  // different naming schemes.

  // version is a string rather than a number because it may contain 'b', 'a',
  // and so on.
  var version = '', re;

  if (goog.userAgent.OPERA && goog.global['opera']) {
    var operaVersion = goog.global['opera'].version;
    return goog.isFunction(operaVersion) ? operaVersion() : operaVersion;
  }

  if (goog.userAgent.GECKO) {
    re = /rv\:([^\);]+)(\)|;)/;
  } else if (goog.userAgent.IE) {
    re = /\b(?:MSIE|rv)[: ]([^\);]+)(\)|;)/;
  } else if (goog.userAgent.WEBKIT) {
    // WebKit/125.4
    re = /WebKit\/(\S+)/;
  }

  if (re) {
    var arr = re.exec(goog.userAgent.getUserAgentString());
    version = arr ? arr[1] : '';
  }

  if (goog.userAgent.IE) {
    // IE9 can be in document mode 9 but be reporting an inconsistent user agent
    // version.  If it is identifying as a version lower than 9 we take the
    // documentMode as the version instead.  IE8 has similar behavior.
    // It is recommended to set the X-UA-Compatible header to ensure that IE9
    // uses documentMode 9.
    var docMode = goog.userAgent.getDocumentMode_();
    if (docMode > parseFloat(version)) {
      return String(docMode);
    }
  }

  return version;
};


/**
 * @return {number|undefined} Returns the document mode (for testing).
 * @private
 */
goog.userAgent.getDocumentMode_ = function() {
  // NOTE(user): goog.userAgent may be used in context where there is no DOM.
  var doc = goog.global['document'];
  return doc ? doc['documentMode'] : undefined;
};


/**
 * The version of the user agent. This is a string because it might contain
 * 'b' (as in beta) as well as multiple dots.
 * @type {string}
 */
goog.userAgent.VERSION = goog.userAgent.determineVersion_();


/**
 * Compares two version numbers.
 *
 * @param {string} v1 Version of first item.
 * @param {string} v2 Version of second item.
 *
 * @return {number}  1 if first argument is higher
 *                   0 if arguments are equal
 *                  -1 if second argument is higher.
 * @deprecated Use goog.string.compareVersions.
 */
goog.userAgent.compare = function(v1, v2) {
  return goog.string.compareVersions(v1, v2);
};


/**
 * Cache for {@link goog.userAgent.isVersionOrHigher}.
 * Calls to compareVersions are surprisingly expensive and, as a browser's
 * version number is unlikely to change during a session, we cache the results.
 * @const
 * @private
 */
goog.userAgent.isVersionOrHigherCache_ = {};


/**
 * Whether the user agent version is higher or the same as the given version.
 * NOTE: When checking the version numbers for Firefox and Safari, be sure to
 * use the engine's version, not the browser's version number.  For example,
 * Firefox 3.0 corresponds to Gecko 1.9 and Safari 3.0 to Webkit 522.11.
 * Opera and Internet Explorer versions match the product release number.<br>
 * @see <a href="http://en.wikipedia.org/wiki/Safari_version_history">
 *     Webkit</a>
 * @see <a href="http://en.wikipedia.org/wiki/Gecko_engine">Gecko</a>
 *
 * @param {string|number} version The version to check.
 * @return {boolean} Whether the user agent version is higher or the same as
 *     the given version.
 */
goog.userAgent.isVersionOrHigher = function(version) {
  return goog.userAgent.ASSUME_ANY_VERSION ||
      goog.userAgent.isVersionOrHigherCache_[version] ||
      (goog.userAgent.isVersionOrHigherCache_[version] =
          goog.string.compareVersions(goog.userAgent.VERSION, version) >= 0);
};


/**
 * Deprecated alias to {@code goog.userAgent.isVersionOrHigher}.
 * @param {string|number} version The version to check.
 * @return {boolean} Whether the user agent version is higher or the same as
 *     the given version.
 * @deprecated Use goog.userAgent.isVersionOrHigher().
 */
goog.userAgent.isVersion = goog.userAgent.isVersionOrHigher;


/**
 * Whether the IE effective document mode is higher or the same as the given
 * document mode version.
 * NOTE: Only for IE, return false for another browser.
 *
 * @param {number} documentMode The document mode version to check.
 * @return {boolean} Whether the IE effective document mode is higher or the
 *     same as the given version.
 */
goog.userAgent.isDocumentModeOrHigher = function(documentMode) {
  return goog.userAgent.IE && goog.userAgent.DOCUMENT_MODE >= documentMode;
};


/**
 * Deprecated alias to {@code goog.userAgent.isDocumentModeOrHigher}.
 * @param {number} version The version to check.
 * @return {boolean} Whether the IE effective document mode is higher or the
 *      same as the given version.
 * @deprecated Use goog.userAgent.isDocumentModeOrHigher().
 */
goog.userAgent.isDocumentMode = goog.userAgent.isDocumentModeOrHigher;


/**
 * For IE version < 7, documentMode is undefined, so attempt to use the
 * CSS1Compat property to see if we are in standards mode. If we are in
 * standards mode, treat the browser version as the document mode. Otherwise,
 * IE is emulating version 5.
 * @type {number|undefined}
 * @const
 */
goog.userAgent.DOCUMENT_MODE = (function() {
  var doc = goog.global['document'];
  if (!doc || !goog.userAgent.IE) {
    return undefined;
  }
  var mode = goog.userAgent.getDocumentMode_();
  return mode || (doc['compatMode'] == 'CSS1Compat' ?
      parseInt(goog.userAgent.VERSION, 10) : 5);
})();

// Copyright 2010 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Browser capability checks for the events package.
 *
 */


goog.provide('goog.events.BrowserFeature');

goog.require('goog.userAgent');


/**
 * Enum of browser capabilities.
 * @enum {boolean}
 */
goog.events.BrowserFeature = {
  /**
   * Whether the button attribute of the event is W3C compliant.  False in
   * Internet Explorer prior to version 9; document-version dependent.
   */
  HAS_W3C_BUTTON: !goog.userAgent.IE ||
      goog.userAgent.isDocumentModeOrHigher(9),

  /**
   * Whether the browser supports full W3C event model.
   */
  HAS_W3C_EVENT_SUPPORT: !goog.userAgent.IE ||
      goog.userAgent.isDocumentModeOrHigher(9),

  /**
   * To prevent default in IE7-8 for certain keydown events we need set the
   * keyCode to -1.
   */
  SET_KEY_CODE_TO_PREVENT_DEFAULT: goog.userAgent.IE &&
      !goog.userAgent.isVersionOrHigher('9'),

  /**
   * Whether the {@code navigator.onLine} property is supported.
   */
  HAS_NAVIGATOR_ONLINE_PROPERTY: !goog.userAgent.WEBKIT ||
      goog.userAgent.isVersionOrHigher('528'),

  /**
   * Whether HTML5 network online/offline events are supported.
   */
  HAS_HTML5_NETWORK_EVENT_SUPPORT:
      goog.userAgent.GECKO && goog.userAgent.isVersionOrHigher('1.9b') ||
      goog.userAgent.IE && goog.userAgent.isVersionOrHigher('8') ||
      goog.userAgent.OPERA && goog.userAgent.isVersionOrHigher('9.5') ||
      goog.userAgent.WEBKIT && goog.userAgent.isVersionOrHigher('528'),

  /**
   * Whether HTML5 network events fire on document.body, or otherwise the
   * window.
   */
  HTML5_NETWORK_EVENTS_FIRE_ON_BODY:
      goog.userAgent.GECKO && !goog.userAgent.isVersionOrHigher('8') ||
      goog.userAgent.IE && !goog.userAgent.isVersionOrHigher('9'),

  /**
   * Whether touch is enabled in the browser.
   */
  TOUCH_ENABLED:
      ('ontouchstart' in goog.global ||
          !!(goog.global['document'] &&
             document.documentElement &&
             'ontouchstart' in document.documentElement) ||
          // IE10 uses non-standard touch events, so it has a different check.
          !!(goog.global['navigator'] &&
              goog.global['navigator']['msMaxTouchPoints']))
};

// Copyright 2011 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Definition of the disposable interface.  A disposable object
 * has a dispose method to to clean up references and resources.
 * @author nnaze@google.com (Nathan Naze)
 */


goog.provide('goog.disposable.IDisposable');



/**
 * Interface for a disposable object.  If a instance requires cleanup
 * (references COM objects, DOM notes, or other disposable objects), it should
 * implement this interface (it may subclass goog.Disposable).
 * @interface
 */
goog.disposable.IDisposable = function() {};


/**
 * Disposes of the object and its resources.
 * @return {void} Nothing.
 */
goog.disposable.IDisposable.prototype.dispose = goog.abstractMethod;


/**
 * @return {boolean} Whether the object has been disposed of.
 */
goog.disposable.IDisposable.prototype.isDisposed = goog.abstractMethod;

// Copyright 2005 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Implements the disposable interface. The dispose method is used
 * to clean up references and resources.
 * @author arv@google.com (Erik Arvidsson)
 */


goog.provide('goog.Disposable');
/** @suppress {extraProvide} */
goog.provide('goog.dispose');
/** @suppress {extraProvide} */
goog.provide('goog.disposeAll');

goog.require('goog.disposable.IDisposable');



/**
 * Class that provides the basic implementation for disposable objects. If your
 * class holds one or more references to COM objects, DOM nodes, or other
 * disposable objects, it should extend this class or implement the disposable
 * interface (defined in goog.disposable.IDisposable).
 * @constructor
 * @implements {goog.disposable.IDisposable}
 */
goog.Disposable = function() {
  if (goog.Disposable.MONITORING_MODE != goog.Disposable.MonitoringMode.OFF) {
    if (goog.Disposable.INCLUDE_STACK_ON_CREATION) {
      this.creationStack = new Error().stack;
    }
    goog.Disposable.instances_[goog.getUid(this)] = this;
  }
};


/**
 * @enum {number} Different monitoring modes for Disposable.
 */
goog.Disposable.MonitoringMode = {
  /**
   * No monitoring.
   */
  OFF: 0,
  /**
   * Creating and disposing the goog.Disposable instances is monitored. All
   * disposable objects need to call the {@code goog.Disposable} base
   * constructor. The PERMANENT mode must be switched on before creating any
   * goog.Disposable instances.
   */
  PERMANENT: 1,
  /**
   * INTERACTIVE mode can be switched on and off on the fly without producing
   * errors. It also doesn't warn if the disposable objects don't call the
   * {@code goog.Disposable} base constructor.
   */
  INTERACTIVE: 2
};


/**
 * @define {number} The monitoring mode of the goog.Disposable
 *     instances. Default is OFF. Switching on the monitoring is only
 *     recommended for debugging because it has a significant impact on
 *     performance and memory usage. If switched off, the monitoring code
 *     compiles down to 0 bytes.
 */
goog.define('goog.Disposable.MONITORING_MODE', 0);


/**
 * @define {boolean} Whether to attach creation stack to each created disposable
 *     instance; This is only relevant for when MonitoringMode != OFF.
 */
goog.define('goog.Disposable.INCLUDE_STACK_ON_CREATION', true);


/**
 * Maps the unique ID of every undisposed {@code goog.Disposable} object to
 * the object itself.
 * @type {!Object.<number, !goog.Disposable>}
 * @private
 */
goog.Disposable.instances_ = {};


/**
 * @return {!Array.<!goog.Disposable>} All {@code goog.Disposable} objects that
 *     haven't been disposed of.
 */
goog.Disposable.getUndisposedObjects = function() {
  var ret = [];
  for (var id in goog.Disposable.instances_) {
    if (goog.Disposable.instances_.hasOwnProperty(id)) {
      ret.push(goog.Disposable.instances_[Number(id)]);
    }
  }
  return ret;
};


/**
 * Clears the registry of undisposed objects but doesn't dispose of them.
 */
goog.Disposable.clearUndisposedObjects = function() {
  goog.Disposable.instances_ = {};
};


/**
 * Whether the object has been disposed of.
 * @type {boolean}
 * @private
 */
goog.Disposable.prototype.disposed_ = false;


/**
 * Callbacks to invoke when this object is disposed.
 * @type {Array.<!Function>}
 * @private
 */
goog.Disposable.prototype.onDisposeCallbacks_;


/**
 * If monitoring the goog.Disposable instances is enabled, stores the creation
 * stack trace of the Disposable instance.
 * @type {string}
 */
goog.Disposable.prototype.creationStack;


/**
 * @return {boolean} Whether the object has been disposed of.
 * @override
 */
goog.Disposable.prototype.isDisposed = function() {
  return this.disposed_;
};


/**
 * @return {boolean} Whether the object has been disposed of.
 * @deprecated Use {@link #isDisposed} instead.
 */
goog.Disposable.prototype.getDisposed = goog.Disposable.prototype.isDisposed;


/**
 * Disposes of the object. If the object hasn't already been disposed of, calls
 * {@link #disposeInternal}. Classes that extend {@code goog.Disposable} should
 * override {@link #disposeInternal} in order to delete references to COM
 * objects, DOM nodes, and other disposable objects. Reentrant.
 *
 * @return {void} Nothing.
 * @override
 */
goog.Disposable.prototype.dispose = function() {
  if (!this.disposed_) {
    // Set disposed_ to true first, in case during the chain of disposal this
    // gets disposed recursively.
    this.disposed_ = true;
    this.disposeInternal();
    if (goog.Disposable.MONITORING_MODE != goog.Disposable.MonitoringMode.OFF) {
      var uid = goog.getUid(this);
      if (goog.Disposable.MONITORING_MODE ==
          goog.Disposable.MonitoringMode.PERMANENT &&
          !goog.Disposable.instances_.hasOwnProperty(uid)) {
        throw Error(this + ' did not call the goog.Disposable base ' +
            'constructor or was disposed of after a clearUndisposedObjects ' +
            'call');
      }
      delete goog.Disposable.instances_[uid];
    }
  }
};


/**
 * Associates a disposable object with this object so that they will be disposed
 * together.
 * @param {goog.disposable.IDisposable} disposable that will be disposed when
 *     this object is disposed.
 */
goog.Disposable.prototype.registerDisposable = function(disposable) {
  this.addOnDisposeCallback(goog.partial(goog.dispose, disposable));
};


/**
 * Invokes a callback function when this object is disposed. Callbacks are
 * invoked in the order in which they were added.
 * @param {function(this:T):?} callback The callback function.
 * @param {T=} opt_scope An optional scope to call the callback in.
 * @template T
 */
goog.Disposable.prototype.addOnDisposeCallback = function(callback, opt_scope) {
  if (!this.onDisposeCallbacks_) {
    this.onDisposeCallbacks_ = [];
  }

  this.onDisposeCallbacks_.push(
      goog.isDef(opt_scope) ? goog.bind(callback, opt_scope) : callback);
};


/**
 * Deletes or nulls out any references to COM objects, DOM nodes, or other
 * disposable objects. Classes that extend {@code goog.Disposable} should
 * override this method.
 * Not reentrant. To avoid calling it twice, it must only be called from the
 * subclass' {@code disposeInternal} method. Everywhere else the public
 * {@code dispose} method must be used.
 * For example:
 * <pre>
 *   mypackage.MyClass = function() {
 *     mypackage.MyClass.base(this, 'constructor');
 *     // Constructor logic specific to MyClass.
 *     ...
 *   };
 *   goog.inherits(mypackage.MyClass, goog.Disposable);
 *
 *   mypackage.MyClass.prototype.disposeInternal = function() {
 *     // Dispose logic specific to MyClass.
 *     ...
 *     // Call superclass's disposeInternal at the end of the subclass's, like
 *     // in C++, to avoid hard-to-catch issues.
 *     mypackage.MyClass.base(this, 'disposeInternal');
 *   };
 * </pre>
 * @protected
 */
goog.Disposable.prototype.disposeInternal = function() {
  if (this.onDisposeCallbacks_) {
    while (this.onDisposeCallbacks_.length) {
      this.onDisposeCallbacks_.shift()();
    }
  }
};


/**
 * Returns True if we can verify the object is disposed.
 * Calls {@code isDisposed} on the argument if it supports it.  If obj
 * is not an object with an isDisposed() method, return false.
 * @param {*} obj The object to investigate.
 * @return {boolean} True if we can verify the object is disposed.
 */
goog.Disposable.isDisposed = function(obj) {
  if (obj && typeof obj.isDisposed == 'function') {
    return obj.isDisposed();
  }
  return false;
};


/**
 * Calls {@code dispose} on the argument if it supports it. If obj is not an
 *     object with a dispose() method, this is a no-op.
 * @param {*} obj The object to dispose of.
 */
goog.dispose = function(obj) {
  if (obj && typeof obj.dispose == 'function') {
    obj.dispose();
  }
};


/**
 * Calls {@code dispose} on each member of the list that supports it. (If the
 * member is an ArrayLike, then {@code goog.disposeAll()} will be called
 * recursively on each of its members.) If the member is not an object with a
 * {@code dispose()} method, then it is ignored.
 * @param {...*} var_args The list.
 */
goog.disposeAll = function(var_args) {
  for (var i = 0, len = arguments.length; i < len; ++i) {
    var disposable = arguments[i];
    if (goog.isArrayLike(disposable)) {
      goog.disposeAll.apply(null, disposable);
    } else {
      goog.dispose(disposable);
    }
  }
};

// Copyright 2013 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

goog.provide('goog.events.EventId');



/**
 * A templated class that is used when registering for events. Typical usage:
 * <code>
 *   /** @type {goog.events.EventId.<MyEventObj>}
 *   var myEventId = new goog.events.EventId(
 *       goog.events.getUniqueId(('someEvent'));
 *
 *   // No need to cast or declare here since the compiler knows the correct
 *   // type of 'evt' (MyEventObj).
 *   something.listen(myEventId, function(evt) {});
 * </code>
 *
 * @param {string} eventId
 * @template T
 * @constructor
 * @struct
 * @final
 */
goog.events.EventId = function(eventId) {
  /** @const */ this.id = eventId;
};


/**
 * @override
 */
goog.events.EventId.prototype.toString = function() {
  return this.id;
};

// Copyright 2005 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview A base class for event objects.
 *
 */


goog.provide('goog.events.Event');
goog.provide('goog.events.EventLike');

/**
 * goog.events.Event no longer depends on goog.Disposable. Keep requiring
 * goog.Disposable here to not break projects which assume this dependency.
 * @suppress {extraRequire}
 */
goog.require('goog.Disposable');
goog.require('goog.events.EventId');


/**
 * A typedef for event like objects that are dispatchable via the
 * goog.events.dispatchEvent function. strings are treated as the type for a
 * goog.events.Event. Objects are treated as an extension of a new
 * goog.events.Event with the type property of the object being used as the type
 * of the Event.
 * @typedef {string|Object|goog.events.Event|goog.events.EventId}
 */
goog.events.EventLike;



/**
 * A base class for event objects, so that they can support preventDefault and
 * stopPropagation.
 *
 * @param {string|!goog.events.EventId} type Event Type.
 * @param {Object=} opt_target Reference to the object that is the target of
 *     this event. It has to implement the {@code EventTarget} interface
 *     declared at {@link http://developer.mozilla.org/en/DOM/EventTarget}.
 * @constructor
 */
goog.events.Event = function(type, opt_target) {
  /**
   * Event type.
   * @type {string}
   */
  this.type = type instanceof goog.events.EventId ? String(type) : type;

  /**
   * TODO(user): The type should probably be
   * EventTarget|goog.events.EventTarget.
   *
   * Target of the event.
   * @type {Object|undefined}
   */
  this.target = opt_target;

  /**
   * Object that had the listener attached.
   * @type {Object|undefined}
   */
  this.currentTarget = this.target;

  /**
   * Whether to cancel the event in internal capture/bubble processing for IE.
   * @type {boolean}
   * @public
   * @suppress {underscore|visibility} Technically public, but referencing this
   *     outside this package is strongly discouraged.
   */
  this.propagationStopped_ = false;

  /**
   * Whether the default action has been prevented.
   * This is a property to match the W3C specification at
   * {@link http://www.w3.org/TR/DOM-Level-3-Events/
   * #events-event-type-defaultPrevented}.
   * Must be treated as read-only outside the class.
   * @type {boolean}
   */
  this.defaultPrevented = false;

  /**
   * Return value for in internal capture/bubble processing for IE.
   * @type {boolean}
   * @public
   * @suppress {underscore|visibility} Technically public, but referencing this
   *     outside this package is strongly discouraged.
   */
  this.returnValue_ = true;
};


/**
 * For backwards compatibility (goog.events.Event used to inherit
 * goog.Disposable).
 * @deprecated Events don't need to be disposed.
 */
goog.events.Event.prototype.disposeInternal = function() {
};


/**
 * For backwards compatibility (goog.events.Event used to inherit
 * goog.Disposable).
 * @deprecated Events don't need to be disposed.
 */
goog.events.Event.prototype.dispose = function() {
};


/**
 * Stops event propagation.
 */
goog.events.Event.prototype.stopPropagation = function() {
  this.propagationStopped_ = true;
};


/**
 * Prevents the default action, for example a link redirecting to a url.
 */
goog.events.Event.prototype.preventDefault = function() {
  this.defaultPrevented = true;
  this.returnValue_ = false;
};


/**
 * Stops the propagation of the event. It is equivalent to
 * {@code e.stopPropagation()}, but can be used as the callback argument of
 * {@link goog.events.listen} without declaring another function.
 * @param {!goog.events.Event} e An event.
 */
goog.events.Event.stopPropagation = function(e) {
  e.stopPropagation();
};


/**
 * Prevents the default action. It is equivalent to
 * {@code e.preventDefault()}, but can be used as the callback argument of
 * {@link goog.events.listen} without declaring another function.
 * @param {!goog.events.Event} e An event.
 */
goog.events.Event.preventDefault = function(e) {
  e.preventDefault();
};

// Copyright 2010 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Event Types.
 *
 * @author arv@google.com (Erik Arvidsson)
 * @author mirkov@google.com (Mirko Visontai)
 */


goog.provide('goog.events.EventType');

goog.require('goog.userAgent');


/**
 * Returns a prefixed event name for the current browser.
 * @param {string} eventName The name of the event.
 * @return {string} The prefixed event name.
 * @suppress {missingRequire|missingProvide}
 * @private
 */
goog.events.getVendorPrefixedName_ = function(eventName) {
  return goog.userAgent.WEBKIT ? 'webkit' + eventName :
      (goog.userAgent.OPERA ? 'o' + eventName.toLowerCase() :
          eventName.toLowerCase());
};


/**
 * Constants for event names.
 * @enum {string}
 */
goog.events.EventType = {
  // Mouse events
  CLICK: 'click',
  RIGHTCLICK: 'rightclick',
  DBLCLICK: 'dblclick',
  MOUSEDOWN: 'mousedown',
  MOUSEUP: 'mouseup',
  MOUSEOVER: 'mouseover',
  MOUSEOUT: 'mouseout',
  MOUSEMOVE: 'mousemove',
  MOUSEENTER: 'mouseenter',
  MOUSELEAVE: 'mouseleave',
  // Select start is non-standard.
  // See http://msdn.microsoft.com/en-us/library/ie/ms536969(v=vs.85).aspx.
  SELECTSTART: 'selectstart', // IE, Safari, Chrome

  // Key events
  KEYPRESS: 'keypress',
  KEYDOWN: 'keydown',
  KEYUP: 'keyup',

  // Focus
  BLUR: 'blur',
  FOCUS: 'focus',
  DEACTIVATE: 'deactivate', // IE only
  // NOTE: The following two events are not stable in cross-browser usage.
  //     WebKit and Opera implement DOMFocusIn/Out.
  //     IE implements focusin/out.
  //     Gecko implements neither see bug at
  //     https://bugzilla.mozilla.org/show_bug.cgi?id=396927.
  // The DOM Events Level 3 Draft deprecates DOMFocusIn in favor of focusin:
  //     http://dev.w3.org/2006/webapi/DOM-Level-3-Events/html/DOM3-Events.html
  // You can use FOCUS in Capture phase until implementations converge.
  FOCUSIN: goog.userAgent.IE ? 'focusin' : 'DOMFocusIn',
  FOCUSOUT: goog.userAgent.IE ? 'focusout' : 'DOMFocusOut',

  // Forms
  CHANGE: 'change',
  SELECT: 'select',
  SUBMIT: 'submit',
  INPUT: 'input',
  PROPERTYCHANGE: 'propertychange', // IE only

  // Drag and drop
  DRAGSTART: 'dragstart',
  DRAG: 'drag',
  DRAGENTER: 'dragenter',
  DRAGOVER: 'dragover',
  DRAGLEAVE: 'dragleave',
  DROP: 'drop',
  DRAGEND: 'dragend',

  // WebKit touch events.
  TOUCHSTART: 'touchstart',
  TOUCHMOVE: 'touchmove',
  TOUCHEND: 'touchend',
  TOUCHCANCEL: 'touchcancel',

  // Misc
  BEFOREUNLOAD: 'beforeunload',
  CONSOLEMESSAGE: 'consolemessage',
  CONTEXTMENU: 'contextmenu',
  DOMCONTENTLOADED: 'DOMContentLoaded',
  ERROR: 'error',
  HELP: 'help',
  LOAD: 'load',
  LOSECAPTURE: 'losecapture',
  ORIENTATIONCHANGE: 'orientationchange',
  READYSTATECHANGE: 'readystatechange',
  RESIZE: 'resize',
  SCROLL: 'scroll',
  UNLOAD: 'unload',

  // HTML 5 History events
  // See http://www.w3.org/TR/html5/history.html#event-definitions
  HASHCHANGE: 'hashchange',
  PAGEHIDE: 'pagehide',
  PAGESHOW: 'pageshow',
  POPSTATE: 'popstate',

  // Copy and Paste
  // Support is limited. Make sure it works on your favorite browser
  // before using.
  // http://www.quirksmode.org/dom/events/cutcopypaste.html
  COPY: 'copy',
  PASTE: 'paste',
  CUT: 'cut',
  BEFORECOPY: 'beforecopy',
  BEFORECUT: 'beforecut',
  BEFOREPASTE: 'beforepaste',

  // HTML5 online/offline events.
  // http://www.w3.org/TR/offline-webapps/#related
  ONLINE: 'online',
  OFFLINE: 'offline',

  // HTML 5 worker events
  MESSAGE: 'message',
  CONNECT: 'connect',

  // CSS animation events.
  /** @suppress {missingRequire} */
  ANIMATIONSTART: goog.events.getVendorPrefixedName_('AnimationStart'),
  /** @suppress {missingRequire} */
  ANIMATIONEND: goog.events.getVendorPrefixedName_('AnimationEnd'),
  /** @suppress {missingRequire} */
  ANIMATIONITERATION: goog.events.getVendorPrefixedName_('AnimationIteration'),

  // CSS transition events. Based on the browser support described at:
  // https://developer.mozilla.org/en/css/css_transitions#Browser_compatibility
  /** @suppress {missingRequire} */
  TRANSITIONEND: goog.events.getVendorPrefixedName_('TransitionEnd'),

  // W3C Pointer Events
  // http://www.w3.org/TR/pointerevents/
  POINTERDOWN: 'pointerdown',
  POINTERUP: 'pointerup',
  POINTERCANCEL: 'pointercancel',
  POINTERMOVE: 'pointermove',
  POINTEROVER: 'pointerover',
  POINTEROUT: 'pointerout',
  POINTERENTER: 'pointerenter',
  POINTERLEAVE: 'pointerleave',
  GOTPOINTERCAPTURE: 'gotpointercapture',
  LOSTPOINTERCAPTURE: 'lostpointercapture',

  // IE specific events.
  // See http://msdn.microsoft.com/en-us/library/ie/hh772103(v=vs.85).aspx
  // Note: these events will be supplanted in IE11.
  MSGESTURECHANGE: 'MSGestureChange',
  MSGESTUREEND: 'MSGestureEnd',
  MSGESTUREHOLD: 'MSGestureHold',
  MSGESTURESTART: 'MSGestureStart',
  MSGESTURETAP: 'MSGestureTap',
  MSGOTPOINTERCAPTURE: 'MSGotPointerCapture',
  MSINERTIASTART: 'MSInertiaStart',
  MSLOSTPOINTERCAPTURE: 'MSLostPointerCapture',
  MSPOINTERCANCEL: 'MSPointerCancel',
  MSPOINTERDOWN: 'MSPointerDown',
  MSPOINTERENTER: 'MSPointerEnter',
  MSPOINTERHOVER: 'MSPointerHover',
  MSPOINTERLEAVE: 'MSPointerLeave',
  MSPOINTERMOVE: 'MSPointerMove',
  MSPOINTEROUT: 'MSPointerOut',
  MSPOINTEROVER: 'MSPointerOver',
  MSPOINTERUP: 'MSPointerUp',

  // Native IMEs/input tools events.
  TEXTINPUT: 'textinput',
  COMPOSITIONSTART: 'compositionstart',
  COMPOSITIONUPDATE: 'compositionupdate',
  COMPOSITIONEND: 'compositionend',

  // Webview tag events
  // See http://developer.chrome.com/dev/apps/webview_tag.html
  EXIT: 'exit',
  LOADABORT: 'loadabort',
  LOADCOMMIT: 'loadcommit',
  LOADREDIRECT: 'loadredirect',
  LOADSTART: 'loadstart',
  LOADSTOP: 'loadstop',
  RESPONSIVE: 'responsive',
  SIZECHANGED: 'sizechanged',
  UNRESPONSIVE: 'unresponsive',

  // HTML5 Page Visibility API.  See details at
  // {@code goog.labs.dom.PageVisibilityMonitor}.
  VISIBILITYCHANGE: 'visibilitychange',

  // LocalStorage event.
  STORAGE: 'storage',

  // DOM Level 2 mutation events (deprecated).
  DOMSUBTREEMODIFIED: 'DOMSubtreeModified',
  DOMNODEINSERTED: 'DOMNodeInserted',
  DOMNODEREMOVED: 'DOMNodeRemoved',
  DOMNODEREMOVEDFROMDOCUMENT: 'DOMNodeRemovedFromDocument',
  DOMNODEINSERTEDINTODOCUMENT: 'DOMNodeInsertedIntoDocument',
  DOMATTRMODIFIED: 'DOMAttrModified',
  DOMCHARACTERDATAMODIFIED: 'DOMCharacterDataModified'
};

// Copyright 2009 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Useful compiler idioms.
 *
 */

goog.provide('goog.reflect');


/**
 * Syntax for object literal casts.
 * @see http://go/jscompiler-renaming
 * @see http://code.google.com/p/closure-compiler/wiki/
 *      ExperimentalTypeBasedPropertyRenaming
 *
 * Use this if you have an object literal whose keys need to have the same names
 * as the properties of some class even after they are renamed by the compiler.
 *
 * @param {!Function} type Type to cast to.
 * @param {Object} object Object literal to cast.
 * @return {Object} The object literal.
 */
goog.reflect.object = function(type, object) {
  return object;
};


/**
 * To assert to the compiler that an operation is needed when it would
 * otherwise be stripped. For example:
 * <code>
 *     // Force a layout
 *     goog.reflect.sinkValue(dialog.offsetHeight);
 * </code>
 * @type {!Function}
 */
goog.reflect.sinkValue = function(x) {
  goog.reflect.sinkValue[' '](x);
  return x;
};


/**
 * The compiler should optimize this function away iff no one ever uses
 * goog.reflect.sinkValue.
 */
goog.reflect.sinkValue[' '] = goog.nullFunction;


/**
 * Check if a property can be accessed without throwing an exception.
 * @param {Object} obj The owner of the property.
 * @param {string} prop The property name.
 * @return {boolean} Whether the property is accessible. Will also return true
 *     if obj is null.
 */
goog.reflect.canAccessProperty = function(obj, prop) {
  /** @preserveTry */
  try {
    goog.reflect.sinkValue(obj[prop]);
    return true;
  } catch (e) {}
  return false;
};

// Copyright 2005 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview A patched, standardized event object for browser events.
 *
 * <pre>
 * The patched event object contains the following members:
 * - type           {string}    Event type, e.g. 'click'
 * - target         {Object}    The element that actually triggered the event
 * - currentTarget  {Object}    The element the listener is attached to
 * - relatedTarget  {Object}    For mouseover and mouseout, the previous object
 * - offsetX        {number}    X-coordinate relative to target
 * - offsetY        {number}    Y-coordinate relative to target
 * - clientX        {number}    X-coordinate relative to viewport
 * - clientY        {number}    Y-coordinate relative to viewport
 * - screenX        {number}    X-coordinate relative to the edge of the screen
 * - screenY        {number}    Y-coordinate relative to the edge of the screen
 * - button         {number}    Mouse button. Use isButton() to test.
 * - keyCode        {number}    Key-code
 * - ctrlKey        {boolean}   Was ctrl key depressed
 * - altKey         {boolean}   Was alt key depressed
 * - shiftKey       {boolean}   Was shift key depressed
 * - metaKey        {boolean}   Was meta key depressed
 * - defaultPrevented {boolean} Whether the default action has been prevented
 * - state          {Object}    History state object
 *
 * NOTE: The keyCode member contains the raw browser keyCode. For normalized
 * key and character code use {@link goog.events.KeyHandler}.
 * </pre>
 *
 */

goog.provide('goog.events.BrowserEvent');
goog.provide('goog.events.BrowserEvent.MouseButton');

goog.require('goog.events.BrowserFeature');
goog.require('goog.events.Event');
goog.require('goog.events.EventType');
goog.require('goog.reflect');
goog.require('goog.userAgent');



/**
 * Accepts a browser event object and creates a patched, cross browser event
 * object.
 * The content of this object will not be initialized if no event object is
 * provided. If this is the case, init() needs to be invoked separately.
 * @param {Event=} opt_e Browser event object.
 * @param {EventTarget=} opt_currentTarget Current target for event.
 * @constructor
 * @extends {goog.events.Event}
 */
goog.events.BrowserEvent = function(opt_e, opt_currentTarget) {
  goog.events.BrowserEvent.base(this, 'constructor', opt_e ? opt_e.type : '');

  /**
   * Target that fired the event.
   * @override
   * @type {Node}
   */
  this.target = null;

  /**
   * Node that had the listener attached.
   * @override
   * @type {Node|undefined}
   */
  this.currentTarget = null;

  /**
   * For mouseover and mouseout events, the related object for the event.
   * @type {Node}
   */
  this.relatedTarget = null;

  /**
   * X-coordinate relative to target.
   * @type {number}
   */
  this.offsetX = 0;

  /**
   * Y-coordinate relative to target.
   * @type {number}
   */
  this.offsetY = 0;

  /**
   * X-coordinate relative to the window.
   * @type {number}
   */
  this.clientX = 0;

  /**
   * Y-coordinate relative to the window.
   * @type {number}
   */
  this.clientY = 0;

  /**
   * X-coordinate relative to the monitor.
   * @type {number}
   */
  this.screenX = 0;

  /**
   * Y-coordinate relative to the monitor.
   * @type {number}
   */
  this.screenY = 0;

  /**
   * Which mouse button was pressed.
   * @type {number}
   */
  this.button = 0;

  /**
   * Keycode of key press.
   * @type {number}
   */
  this.keyCode = 0;

  /**
   * Keycode of key press.
   * @type {number}
   */
  this.charCode = 0;

  /**
   * Whether control was pressed at time of event.
   * @type {boolean}
   */
  this.ctrlKey = false;

  /**
   * Whether alt was pressed at time of event.
   * @type {boolean}
   */
  this.altKey = false;

  /**
   * Whether shift was pressed at time of event.
   * @type {boolean}
   */
  this.shiftKey = false;

  /**
   * Whether the meta key was pressed at time of event.
   * @type {boolean}
   */
  this.metaKey = false;

  /**
   * History state object, only set for PopState events where it's a copy of the
   * state object provided to pushState or replaceState.
   * @type {Object}
   */
  this.state = null;

  /**
   * Whether the default platform modifier key was pressed at time of event.
   * (This is control for all platforms except Mac, where it's Meta.)
   * @type {boolean}
   */
  this.platformModifierKey = false;

  /**
   * The browser event object.
   * @private {Event}
   */
  this.event_ = null;

  if (opt_e) {
    this.init(opt_e, opt_currentTarget);
  }
};
goog.inherits(goog.events.BrowserEvent, goog.events.Event);


/**
 * Normalized button constants for the mouse.
 * @enum {number}
 */
goog.events.BrowserEvent.MouseButton = {
  LEFT: 0,
  MIDDLE: 1,
  RIGHT: 2
};


/**
 * Static data for mapping mouse buttons.
 * @type {!Array.<number>}
 */
goog.events.BrowserEvent.IEButtonMap = [
  1, // LEFT
  4, // MIDDLE
  2  // RIGHT
];


/**
 * Accepts a browser event object and creates a patched, cross browser event
 * object.
 * @param {Event} e Browser event object.
 * @param {EventTarget=} opt_currentTarget Current target for event.
 */
goog.events.BrowserEvent.prototype.init = function(e, opt_currentTarget) {
  var type = this.type = e.type;

  // TODO(nicksantos): Change this.target to type EventTarget.
  this.target = /** @type {Node} */ (e.target) || e.srcElement;

  // TODO(nicksantos): Change this.currentTarget to type EventTarget.
  this.currentTarget = /** @type {Node} */ (opt_currentTarget);

  var relatedTarget = /** @type {Node} */ (e.relatedTarget);
  if (relatedTarget) {
    // There's a bug in FireFox where sometimes, relatedTarget will be a
    // chrome element, and accessing any property of it will get a permission
    // denied exception. See:
    // https://bugzilla.mozilla.org/show_bug.cgi?id=497780
    if (goog.userAgent.GECKO) {
      if (!goog.reflect.canAccessProperty(relatedTarget, 'nodeName')) {
        relatedTarget = null;
      }
    }
    // TODO(arv): Use goog.events.EventType when it has been refactored into its
    // own file.
  } else if (type == goog.events.EventType.MOUSEOVER) {
    relatedTarget = e.fromElement;
  } else if (type == goog.events.EventType.MOUSEOUT) {
    relatedTarget = e.toElement;
  }

  this.relatedTarget = relatedTarget;

  // Webkit emits a lame warning whenever layerX/layerY is accessed.
  // http://code.google.com/p/chromium/issues/detail?id=101733
  this.offsetX = (goog.userAgent.WEBKIT || e.offsetX !== undefined) ?
      e.offsetX : e.layerX;
  this.offsetY = (goog.userAgent.WEBKIT || e.offsetY !== undefined) ?
      e.offsetY : e.layerY;

  this.clientX = e.clientX !== undefined ? e.clientX : e.pageX;
  this.clientY = e.clientY !== undefined ? e.clientY : e.pageY;
  this.screenX = e.screenX || 0;
  this.screenY = e.screenY || 0;

  this.button = e.button;

  this.keyCode = e.keyCode || 0;
  this.charCode = e.charCode || (type == 'keypress' ? e.keyCode : 0);
  this.ctrlKey = e.ctrlKey;
  this.altKey = e.altKey;
  this.shiftKey = e.shiftKey;
  this.metaKey = e.metaKey;
  this.platformModifierKey = goog.userAgent.MAC ? e.metaKey : e.ctrlKey;
  this.state = e.state;
  this.event_ = e;
  if (e.defaultPrevented) {
    this.preventDefault();
  }
};


/**
 * Tests to see which button was pressed during the event. This is really only
 * useful in IE and Gecko browsers. And in IE, it's only useful for
 * mousedown/mouseup events, because click only fires for the left mouse button.
 *
 * Safari 2 only reports the left button being clicked, and uses the value '1'
 * instead of 0. Opera only reports a mousedown event for the middle button, and
 * no mouse events for the right button. Opera has default behavior for left and
 * middle click that can only be overridden via a configuration setting.
 *
 * There's a nice table of this mess at http://www.unixpapa.com/js/mouse.html.
 *
 * @param {goog.events.BrowserEvent.MouseButton} button The button
 *     to test for.
 * @return {boolean} True if button was pressed.
 */
goog.events.BrowserEvent.prototype.isButton = function(button) {
  if (!goog.events.BrowserFeature.HAS_W3C_BUTTON) {
    if (this.type == 'click') {
      return button == goog.events.BrowserEvent.MouseButton.LEFT;
    } else {
      return !!(this.event_.button &
          goog.events.BrowserEvent.IEButtonMap[button]);
    }
  } else {
    return this.event_.button == button;
  }
};


/**
 * Whether this has an "action"-producing mouse button.
 *
 * By definition, this includes left-click on windows/linux, and left-click
 * without the ctrl key on Macs.
 *
 * @return {boolean} The result.
 */
goog.events.BrowserEvent.prototype.isMouseActionButton = function() {
  // Webkit does not ctrl+click to be a right-click, so we
  // normalize it to behave like Gecko and Opera.
  return this.isButton(goog.events.BrowserEvent.MouseButton.LEFT) &&
      !(goog.userAgent.WEBKIT && goog.userAgent.MAC && this.ctrlKey);
};


/**
 * @override
 */
goog.events.BrowserEvent.prototype.stopPropagation = function() {
  goog.events.BrowserEvent.superClass_.stopPropagation.call(this);
  if (this.event_.stopPropagation) {
    this.event_.stopPropagation();
  } else {
    this.event_.cancelBubble = true;
  }
};


/**
 * @override
 */
goog.events.BrowserEvent.prototype.preventDefault = function() {
  goog.events.BrowserEvent.superClass_.preventDefault.call(this);
  var be = this.event_;
  if (!be.preventDefault) {
    be.returnValue = false;
    if (goog.events.BrowserFeature.SET_KEY_CODE_TO_PREVENT_DEFAULT) {
      /** @preserveTry */
      try {
        // Most keys can be prevented using returnValue. Some special keys
        // require setting the keyCode to -1 as well:
        //
        // In IE7:
        // F3, F5, F10, F11, Ctrl+P, Crtl+O, Ctrl+F (these are taken from IE6)
        //
        // In IE8:
        // Ctrl+P, Crtl+O, Ctrl+F (F1-F12 cannot be stopped through the event)
        //
        // We therefore do this for all function keys as well as when Ctrl key
        // is pressed.
        var VK_F1 = 112;
        var VK_F12 = 123;
        if (be.ctrlKey || be.keyCode >= VK_F1 && be.keyCode <= VK_F12) {
          be.keyCode = -1;
        }
      } catch (ex) {
        // IE throws an 'access denied' exception when trying to change
        // keyCode in some situations (e.g. srcElement is input[type=file],
        // or srcElement is an anchor tag rewritten by parent's innerHTML).
        // Do nothing in this case.
      }
    }
  } else {
    be.preventDefault();
  }
};


/**
 * @return {Event} The underlying browser event object.
 */
goog.events.BrowserEvent.prototype.getBrowserEvent = function() {
  return this.event_;
};


/** @override */
goog.events.BrowserEvent.prototype.disposeInternal = function() {
};

// Copyright 2012 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview An interface for a listenable JavaScript object.
 */

goog.provide('goog.events.Listenable');
goog.provide('goog.events.ListenableKey');

/** @suppress {extraRequire} */
goog.require('goog.events.EventId');



/**
 * A listenable interface. A listenable is an object with the ability
 * to dispatch/broadcast events to "event listeners" registered via
 * listen/listenOnce.
 *
 * The interface allows for an event propagation mechanism similar
 * to one offered by native browser event targets, such as
 * capture/bubble mechanism, stopping propagation, and preventing
 * default actions. Capture/bubble mechanism depends on the ancestor
 * tree constructed via {@code #getParentEventTarget}; this tree
 * must be directed acyclic graph. The meaning of default action(s)
 * in preventDefault is specific to a particular use case.
 *
 * Implementations that do not support capture/bubble or can not have
 * a parent listenable can simply not implement any ability to set the
 * parent listenable (and have {@code #getParentEventTarget} return
 * null).
 *
 * Implementation of this class can be used with or independently from
 * goog.events.
 *
 * Implementation must call {@code #addImplementation(implClass)}.
 *
 * @interface
 * @see goog.events
 * @see http://www.w3.org/TR/DOM-Level-2-Events/events.html
 */
goog.events.Listenable = function() {};


/**
 * An expando property to indicate that an object implements
 * goog.events.Listenable.
 *
 * See addImplementation/isImplementedBy.
 *
 * @type {string}
 * @const
 */
goog.events.Listenable.IMPLEMENTED_BY_PROP =
    'closure_listenable_' + ((Math.random() * 1e6) | 0);


/**
 * Marks a given class (constructor) as an implementation of
 * Listenable, do that we can query that fact at runtime. The class
 * must have already implemented the interface.
 * @param {!Function} cls The class constructor. The corresponding
 *     class must have already implemented the interface.
 */
goog.events.Listenable.addImplementation = function(cls) {
  cls.prototype[goog.events.Listenable.IMPLEMENTED_BY_PROP] = true;
};


/**
 * @param {Object} obj The object to check.
 * @return {boolean} Whether a given instance implements Listenable. The
 *     class/superclass of the instance must call addImplementation.
 */
goog.events.Listenable.isImplementedBy = function(obj) {
  return !!(obj && obj[goog.events.Listenable.IMPLEMENTED_BY_PROP]);
};


/**
 * Adds an event listener. A listener can only be added once to an
 * object and if it is added again the key for the listener is
 * returned. Note that if the existing listener is a one-off listener
 * (registered via listenOnce), it will no longer be a one-off
 * listener after a call to listen().
 *
 * @param {string|!goog.events.EventId.<EVENTOBJ>} type The event type id.
 * @param {function(this:SCOPE, EVENTOBJ):(boolean|undefined)} listener Callback
 *     method.
 * @param {boolean=} opt_useCapture Whether to fire in capture phase
 *     (defaults to false).
 * @param {SCOPE=} opt_listenerScope Object in whose scope to call the
 *     listener.
 * @return {goog.events.ListenableKey} Unique key for the listener.
 * @template SCOPE,EVENTOBJ
 */
goog.events.Listenable.prototype.listen;


/**
 * Adds an event listener that is removed automatically after the
 * listener fired once.
 *
 * If an existing listener already exists, listenOnce will do
 * nothing. In particular, if the listener was previously registered
 * via listen(), listenOnce() will not turn the listener into a
 * one-off listener. Similarly, if there is already an existing
 * one-off listener, listenOnce does not modify the listeners (it is
 * still a once listener).
 *
 * @param {string|!goog.events.EventId.<EVENTOBJ>} type The event type id.
 * @param {function(this:SCOPE, EVENTOBJ):(boolean|undefined)} listener Callback
 *     method.
 * @param {boolean=} opt_useCapture Whether to fire in capture phase
 *     (defaults to false).
 * @param {SCOPE=} opt_listenerScope Object in whose scope to call the
 *     listener.
 * @return {goog.events.ListenableKey} Unique key for the listener.
 * @template SCOPE,EVENTOBJ
 */
goog.events.Listenable.prototype.listenOnce;


/**
 * Removes an event listener which was added with listen() or listenOnce().
 *
 * @param {string|!goog.events.EventId.<EVENTOBJ>} type The event type id.
 * @param {function(this:SCOPE, EVENTOBJ):(boolean|undefined)} listener Callback
 *     method.
 * @param {boolean=} opt_useCapture Whether to fire in capture phase
 *     (defaults to false).
 * @param {SCOPE=} opt_listenerScope Object in whose scope to call
 *     the listener.
 * @return {boolean} Whether any listener was removed.
 * @template SCOPE,EVENTOBJ
 */
goog.events.Listenable.prototype.unlisten;


/**
 * Removes an event listener which was added with listen() by the key
 * returned by listen().
 *
 * @param {goog.events.ListenableKey} key The key returned by
 *     listen() or listenOnce().
 * @return {boolean} Whether any listener was removed.
 */
goog.events.Listenable.prototype.unlistenByKey;


/**
 * Dispatches an event (or event like object) and calls all listeners
 * listening for events of this type. The type of the event is decided by the
 * type property on the event object.
 *
 * If any of the listeners returns false OR calls preventDefault then this
 * function will return false.  If one of the capture listeners calls
 * stopPropagation, then the bubble listeners won't fire.
 *
 * @param {goog.events.EventLike} e Event object.
 * @return {boolean} If anyone called preventDefault on the event object (or
 *     if any of the listeners returns false) this will also return false.
 */
goog.events.Listenable.prototype.dispatchEvent;


/**
 * Removes all listeners from this listenable. If type is specified,
 * it will only remove listeners of the particular type. otherwise all
 * registered listeners will be removed.
 *
 * @param {string=} opt_type Type of event to remove, default is to
 *     remove all types.
 * @return {number} Number of listeners removed.
 */
goog.events.Listenable.prototype.removeAllListeners;


/**
 * Returns the parent of this event target to use for capture/bubble
 * mechanism.
 *
 * NOTE(user): The name reflects the original implementation of
 * custom event target ({@code goog.events.EventTarget}). We decided
 * that changing the name is not worth it.
 *
 * @return {goog.events.Listenable} The parent EventTarget or null if
 *     there is no parent.
 */
goog.events.Listenable.prototype.getParentEventTarget;


/**
 * Fires all registered listeners in this listenable for the given
 * type and capture mode, passing them the given eventObject. This
 * does not perform actual capture/bubble. Only implementors of the
 * interface should be using this.
 *
 * @param {string|!goog.events.EventId.<EVENTOBJ>} type The type of the
 *     listeners to fire.
 * @param {boolean} capture The capture mode of the listeners to fire.
 * @param {EVENTOBJ} eventObject The event object to fire.
 * @return {boolean} Whether all listeners succeeded without
 *     attempting to prevent default behavior. If any listener returns
 *     false or called goog.events.Event#preventDefault, this returns
 *     false.
 * @template EVENTOBJ
 */
goog.events.Listenable.prototype.fireListeners;


/**
 * Gets all listeners in this listenable for the given type and
 * capture mode.
 *
 * @param {string|!goog.events.EventId} type The type of the listeners to fire.
 * @param {boolean} capture The capture mode of the listeners to fire.
 * @return {!Array.<goog.events.ListenableKey>} An array of registered
 *     listeners.
 * @template EVENTOBJ
 */
goog.events.Listenable.prototype.getListeners;


/**
 * Gets the goog.events.ListenableKey for the event or null if no such
 * listener is in use.
 *
 * @param {string|!goog.events.EventId.<EVENTOBJ>} type The name of the event
 *     without the 'on' prefix.
 * @param {function(this:SCOPE, EVENTOBJ):(boolean|undefined)} listener The
 *     listener function to get.
 * @param {boolean} capture Whether the listener is a capturing listener.
 * @param {SCOPE=} opt_listenerScope Object in whose scope to call the
 *     listener.
 * @return {goog.events.ListenableKey} the found listener or null if not found.
 * @template SCOPE,EVENTOBJ
 */
goog.events.Listenable.prototype.getListener;


/**
 * Whether there is any active listeners matching the specified
 * signature. If either the type or capture parameters are
 * unspecified, the function will match on the remaining criteria.
 *
 * @param {string|!goog.events.EventId.<EVENTOBJ>=} opt_type Event type.
 * @param {boolean=} opt_capture Whether to check for capture or bubble
 *     listeners.
 * @return {boolean} Whether there is any active listeners matching
 *     the requested type and/or capture phase.
 * @template EVENTOBJ
 */
goog.events.Listenable.prototype.hasListener;



/**
 * An interface that describes a single registered listener.
 * @interface
 */
goog.events.ListenableKey = function() {};


/**
 * Counter used to create a unique key
 * @type {number}
 * @private
 */
goog.events.ListenableKey.counter_ = 0;


/**
 * Reserves a key to be used for ListenableKey#key field.
 * @return {number} A number to be used to fill ListenableKey#key
 *     field.
 */
goog.events.ListenableKey.reserveKey = function() {
  return ++goog.events.ListenableKey.counter_;
};


/**
 * The source event target.
 * @type {!(Object|goog.events.Listenable|goog.events.EventTarget)}
 */
goog.events.ListenableKey.prototype.src;


/**
 * The event type the listener is listening to.
 * @type {string}
 */
goog.events.ListenableKey.prototype.type;


/**
 * The listener function.
 * @type {function(?):?|{handleEvent:function(?):?}|null}
 */
goog.events.ListenableKey.prototype.listener;


/**
 * Whether the listener works on capture phase.
 * @type {boolean}
 */
goog.events.ListenableKey.prototype.capture;


/**
 * The 'this' object for the listener function's scope.
 * @type {Object}
 */
goog.events.ListenableKey.prototype.handler;


/**
 * A globally unique number to identify the key.
 * @type {number}
 */
goog.events.ListenableKey.prototype.key;

// Copyright 2005 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Listener object.
 * @see ../demos/events.html
 */

goog.provide('goog.events.Listener');

goog.require('goog.events.ListenableKey');



/**
 * Simple class that stores information about a listener
 * @param {!Function} listener Callback function.
 * @param {Function} proxy Wrapper for the listener that patches the event.
 * @param {EventTarget|goog.events.Listenable} src Source object for
 *     the event.
 * @param {string} type Event type.
 * @param {boolean} capture Whether in capture or bubble phase.
 * @param {Object=} opt_handler Object in whose context to execute the callback.
 * @implements {goog.events.ListenableKey}
 * @constructor
 */
goog.events.Listener = function(
    listener, proxy, src, type, capture, opt_handler) {
  if (goog.events.Listener.ENABLE_MONITORING) {
    this.creationStack = new Error().stack;
  }

  /**
   * Callback function.
   * @type {Function}
   */
  this.listener = listener;

  /**
   * A wrapper over the original listener. This is used solely to
   * handle native browser events (it is used to simulate the capture
   * phase and to patch the event object).
   * @type {Function}
   */
  this.proxy = proxy;

  /**
   * Object or node that callback is listening to
   * @type {EventTarget|goog.events.Listenable}
   */
  this.src = src;

  /**
   * The event type.
   * @const {string}
   */
  this.type = type;

  /**
   * Whether the listener is being called in the capture or bubble phase
   * @const {boolean}
   */
  this.capture = !!capture;

  /**
   * Optional object whose context to execute the listener in
   * @type {Object|undefined}
   */
  this.handler = opt_handler;

  /**
   * The key of the listener.
   * @const {number}
   * @override
   */
  this.key = goog.events.ListenableKey.reserveKey();

  /**
   * Whether to remove the listener after it has been called.
   * @type {boolean}
   */
  this.callOnce = false;

  /**
   * Whether the listener has been removed.
   * @type {boolean}
   */
  this.removed = false;
};


/**
 * @define {boolean} Whether to enable the monitoring of the
 *     goog.events.Listener instances. Switching on the monitoring is only
 *     recommended for debugging because it has a significant impact on
 *     performance and memory usage. If switched off, the monitoring code
 *     compiles down to 0 bytes.
 */
goog.define('goog.events.Listener.ENABLE_MONITORING', false);


/**
 * If monitoring the goog.events.Listener instances is enabled, stores the
 * creation stack trace of the Disposable instance.
 * @type {string}
 */
goog.events.Listener.prototype.creationStack;


/**
 * Marks this listener as removed. This also remove references held by
 * this listener object (such as listener and event source).
 */
goog.events.Listener.prototype.markAsRemoved = function() {
  this.removed = true;
  this.listener = null;
  this.proxy = null;
  this.src = null;
  this.handler = null;
};

// Copyright 2006 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview Utilities for manipulating objects/maps/hashes.
 */

goog.provide('goog.object');


/**
 * Calls a function for each element in an object/map/hash.
 *
 * @param {Object.<K,V>} obj The object over which to iterate.
 * @param {function(this:T,V,?,Object.<K,V>):?} f The function to call
 *     for every element. This function takes 3 arguments (the element, the
 *     index and the object) and the return value is ignored.
 * @param {T=} opt_obj This is used as the 'this' object within f.
 * @template T,K,V
 */
goog.object.forEach = function(obj, f, opt_obj) {
  for (var key in obj) {
    f.call(opt_obj, obj[key], key, obj);
  }
};


/**
 * Calls a function for each element in an object/map/hash. If that call returns
 * true, adds the element to a new object.
 *
 * @param {Object.<K,V>} obj The object over which to iterate.
 * @param {function(this:T,V,?,Object.<K,V>):boolean} f The function to call
 *     for every element. This
 *     function takes 3 arguments (the element, the index and the object)
 *     and should return a boolean. If the return value is true the
 *     element is added to the result object. If it is false the
 *     element is not included.
 * @param {T=} opt_obj This is used as the 'this' object within f.
 * @return {!Object.<K,V>} a new object in which only elements that passed the
 *     test are present.
 * @template T,K,V
 */
goog.object.filter = function(obj, f, opt_obj) {
  var res = {};
  for (var key in obj) {
    if (f.call(opt_obj, obj[key], key, obj)) {
      res[key] = obj[key];
    }
  }
  return res;
};


/**
 * For every element in an object/map/hash calls a function and inserts the
 * result into a new object.
 *
 * @param {Object.<K,V>} obj The object over which to iterate.
 * @param {function(this:T,V,?,Object.<K,V>):R} f The function to call
 *     for every element. This function
 *     takes 3 arguments (the element, the index and the object)
 *     and should return something. The result will be inserted
 *     into a new object.
 * @param {T=} opt_obj This is used as the 'this' object within f.
 * @return {!Object.<K,R>} a new object with the results from f.
 * @template T,K,V,R
 */
goog.object.map = function(obj, f, opt_obj) {
  var res = {};
  for (var key in obj) {
    res[key] = f.call(opt_obj, obj[key], key, obj);
  }
  return res;
};


/**
 * Calls a function for each element in an object/map/hash. If any
 * call returns true, returns true (without checking the rest). If
 * all calls return false, returns false.
 *
 * @param {Object.<K,V>} obj The object to check.
 * @param {function(this:T,V,?,Object.<K,V>):boolean} f The function to
 *     call for every element. This function
 *     takes 3 arguments (the element, the index and the object) and should
 *     return a boolean.
 * @param {T=} opt_obj This is used as the 'this' object within f.
 * @return {boolean} true if any element passes the test.
 * @template T,K,V
 */
goog.object.some = function(obj, f, opt_obj) {
  for (var key in obj) {
    if (f.call(opt_obj, obj[key], key, obj)) {
      return true;
    }
  }
  return false;
};


/**
 * Calls a function for each element in an object/map/hash. If
 * all calls return true, returns true. If any call returns false, returns
 * false at this point and does not continue to check the remaining elements.
 *
 * @param {Object.<K,V>} obj The object to check.
 * @param {?function(this:T,V,?,Object.<K,V>):boolean} f The function to
 *     call for every element. This function
 *     takes 3 arguments (the element, the index and the object) and should
 *     return a boolean.
 * @param {T=} opt_obj This is used as the 'this' object within f.
 * @return {boolean} false if any element fails the test.
 * @template T,K,V
 */
goog.object.every = function(obj, f, opt_obj) {
  for (var key in obj) {
    if (!f.call(opt_obj, obj[key], key, obj)) {
      return false;
    }
  }
  return true;
};


/**
 * Returns the number of key-value pairs in the object map.
 *
 * @param {Object} obj The object for which to get the number of key-value
 *     pairs.
 * @return {number} The number of key-value pairs in the object map.
 */
goog.object.getCount = function(obj) {
  // JS1.5 has __count__ but it has been deprecated so it raises a warning...
  // in other words do not use. Also __count__ only includes the fields on the
  // actual object and not in the prototype chain.
  var rv = 0;
  for (var key in obj) {
    rv++;
  }
  return rv;
};


/**
 * Returns one key from the object map, if any exists.
 * For map literals the returned key will be the first one in most of the
 * browsers (a know exception is Konqueror).
 *
 * @param {Object} obj The object to pick a key from.
 * @return {string|undefined} The key or undefined if the object is empty.
 */
goog.object.getAnyKey = function(obj) {
  for (var key in obj) {
    return key;
  }
};


/**
 * Returns one value from the object map, if any exists.
 * For map literals the returned value will be the first one in most of the
 * browsers (a know exception is Konqueror).
 *
 * @param {Object.<K,V>} obj The object to pick a value from.
 * @return {V|undefined} The value or undefined if the object is empty.
 * @template K,V
 */
goog.object.getAnyValue = function(obj) {
  for (var key in obj) {
    return obj[key];
  }
};


/**
 * Whether the object/hash/map contains the given object as a value.
 * An alias for goog.object.containsValue(obj, val).
 *
 * @param {Object.<K,V>} obj The object in which to look for val.
 * @param {V} val The object for which to check.
 * @return {boolean} true if val is present.
 * @template K,V
 */
goog.object.contains = function(obj, val) {
  return goog.object.containsValue(obj, val);
};


/**
 * Returns the values of the object/map/hash.
 *
 * @param {Object.<K,V>} obj The object from which to get the values.
 * @return {!Array.<V>} The values in the object/map/hash.
 * @template K,V
 */
goog.object.getValues = function(obj) {
  var res = [];
  var i = 0;
  for (var key in obj) {
    res[i++] = obj[key];
  }
  return res;
};


/**
 * Returns the keys of the object/map/hash.
 *
 * @param {Object} obj The object from which to get the keys.
 * @return {!Array.<string>} Array of property keys.
 */
goog.object.getKeys = function(obj) {
  var res = [];
  var i = 0;
  for (var key in obj) {
    res[i++] = key;
  }
  return res;
};


/**
 * Get a value from an object multiple levels deep.  This is useful for
 * pulling values from deeply nested objects, such as JSON responses.
 * Example usage: getValueByKeys(jsonObj, 'foo', 'entries', 3)
 *
 * @param {!Object} obj An object to get the value from.  Can be array-like.
 * @param {...(string|number|!Array.<number|string>)} var_args A number of keys
 *     (as strings, or numbers, for array-like objects).  Can also be
 *     specified as a single array of keys.
 * @return {*} The resulting value.  If, at any point, the value for a key
 *     is undefined, returns undefined.
 */
goog.object.getValueByKeys = function(obj, var_args) {
  var isArrayLike = goog.isArrayLike(var_args);
  var keys = isArrayLike ? var_args : arguments;

  // Start with the 2nd parameter for the variable parameters syntax.
  for (var i = isArrayLike ? 0 : 1; i < keys.length; i++) {
    obj = obj[keys[i]];
    if (!goog.isDef(obj)) {
      break;
    }
  }

  return obj;
};


/**
 * Whether the object/map/hash contains the given key.
 *
 * @param {Object} obj The object in which to look for key.
 * @param {*} key The key for which to check.
 * @return {boolean} true If the map contains the key.
 */
goog.object.containsKey = function(obj, key) {
  return key in obj;
};


/**
 * Whether the object/map/hash contains the given value. This is O(n).
 *
 * @param {Object.<K,V>} obj The object in which to look for val.
 * @param {V} val The value for which to check.
 * @return {boolean} true If the map contains the value.
 * @template K,V
 */
goog.object.containsValue = function(obj, val) {
  for (var key in obj) {
    if (obj[key] == val) {
      return true;
    }
  }
  return false;
};


/**
 * Searches an object for an element that satisfies the given condition and
 * returns its key.
 * @param {Object.<K,V>} obj The object to search in.
 * @param {function(this:T,V,string,Object.<K,V>):boolean} f The
 *      function to call for every element. Takes 3 arguments (the value,
 *     the key and the object) and should return a boolean.
 * @param {T=} opt_this An optional "this" context for the function.
 * @return {string|undefined} The key of an element for which the function
 *     returns true or undefined if no such element is found.
 * @template T,K,V
 */
goog.object.findKey = function(obj, f, opt_this) {
  for (var key in obj) {
    if (f.call(opt_this, obj[key], key, obj)) {
      return key;
    }
  }
  return undefined;
};


/**
 * Searches an object for an element that satisfies the given condition and
 * returns its value.
 * @param {Object.<K,V>} obj The object to search in.
 * @param {function(this:T,V,string,Object.<K,V>):boolean} f The function
 *     to call for every element. Takes 3 arguments (the value, the key
 *     and the object) and should return a boolean.
 * @param {T=} opt_this An optional "this" context for the function.
 * @return {V} The value of an element for which the function returns true or
 *     undefined if no such element is found.
 * @template T,K,V
 */
goog.object.findValue = function(obj, f, opt_this) {
  var key = goog.object.findKey(obj, f, opt_this);
  return key && obj[key];
};


/**
 * Whether the object/map/hash is empty.
 *
 * @param {Object} obj The object to test.
 * @return {boolean} true if obj is empty.
 */
goog.object.isEmpty = function(obj) {
  for (var key in obj) {
    return false;
  }
  return true;
};


/**
 * Removes all key value pairs from the object/map/hash.
 *
 * @param {Object} obj The object to clear.
 */
goog.object.clear = function(obj) {
  for (var i in obj) {
    delete obj[i];
  }
};


/**
 * Removes a key-value pair based on the key.
 *
 * @param {Object} obj The object from which to remove the key.
 * @param {*} key The key to remove.
 * @return {boolean} Whether an element was removed.
 */
goog.object.remove = function(obj, key) {
  var rv;
  if ((rv = key in obj)) {
    delete obj[key];
  }
  return rv;
};


/**
 * Adds a key-value pair to the object. Throws an exception if the key is
 * already in use. Use set if you want to change an existing pair.
 *
 * @param {Object.<K,V>} obj The object to which to add the key-value pair.
 * @param {string} key The key to add.
 * @param {V} val The value to add.
 * @template K,V
 */
goog.object.add = function(obj, key, val) {
  if (key in obj) {
    throw Error('The object already contains the key "' + key + '"');
  }
  goog.object.set(obj, key, val);
};


/**
 * Returns the value for the given key.
 *
 * @param {Object.<K,V>} obj The object from which to get the value.
 * @param {string} key The key for which to get the value.
 * @param {R=} opt_val The value to return if no item is found for the given
 *     key (default is undefined).
 * @return {V|R|undefined} The value for the given key.
 * @template K,V,R
 */
goog.object.get = function(obj, key, opt_val) {
  if (key in obj) {
    return obj[key];
  }
  return opt_val;
};


/**
 * Adds a key-value pair to the object/map/hash.
 *
 * @param {Object.<K,V>} obj The object to which to add the key-value pair.
 * @param {string} key The key to add.
 * @param {V} value The value to add.
 * @template K,V
 */
goog.object.set = function(obj, key, value) {
  obj[key] = value;
};


/**
 * Adds a key-value pair to the object/map/hash if it doesn't exist yet.
 *
 * @param {Object.<K,V>} obj The object to which to add the key-value pair.
 * @param {string} key The key to add.
 * @param {V} value The value to add if the key wasn't present.
 * @return {V} The value of the entry at the end of the function.
 * @template K,V
 */
goog.object.setIfUndefined = function(obj, key, value) {
  return key in obj ? obj[key] : (obj[key] = value);
};


/**
 * Does a flat clone of the object.
 *
 * @param {Object.<K,V>} obj Object to clone.
 * @return {!Object.<K,V>} Clone of the input object.
 * @template K,V
 */
goog.object.clone = function(obj) {
  // We cannot use the prototype trick because a lot of methods depend on where
  // the actual key is set.

  var res = {};
  for (var key in obj) {
    res[key] = obj[key];
  }
  return res;
  // We could also use goog.mixin but I wanted this to be independent from that.
};


/**
 * Clones a value. The input may be an Object, Array, or basic type. Objects and
 * arrays will be cloned recursively.
 *
 * WARNINGS:
 * <code>goog.object.unsafeClone</code> does not detect reference loops. Objects
 * that refer to themselves will cause infinite recursion.
 *
 * <code>goog.object.unsafeClone</code> is unaware of unique identifiers, and
 * copies UIDs created by <code>getUid</code> into cloned results.
 *
 * @param {*} obj The value to clone.
 * @return {*} A clone of the input value.
 */
goog.object.unsafeClone = function(obj) {
  var type = goog.typeOf(obj);
  if (type == 'object' || type == 'array') {
    if (obj.clone) {
      return obj.clone();
    }
    var clone = type == 'array' ? [] : {};
    for (var key in obj) {
      clone[key] = goog.object.unsafeClone(obj[key]);
    }
    return clone;
  }

  return obj;
};


/**
 * Returns a new object in which all the keys and values are interchanged
 * (keys become values and values become keys). If multiple keys map to the
 * same value, the chosen transposed value is implementation-dependent.
 *
 * @param {Object} obj The object to transpose.
 * @return {!Object} The transposed object.
 */
goog.object.transpose = function(obj) {
  var transposed = {};
  for (var key in obj) {
    transposed[obj[key]] = key;
  }
  return transposed;
};


/**
 * The names of the fields that are defined on Object.prototype.
 * @type {Array.<string>}
 * @private
 */
goog.object.PROTOTYPE_FIELDS_ = [
  'constructor',
  'hasOwnProperty',
  'isPrototypeOf',
  'propertyIsEnumerable',
  'toLocaleString',
  'toString',
  'valueOf'
];


/**
 * Extends an object with another object.
 * This operates 'in-place'; it does not create a new Object.
 *
 * Example:
 * var o = {};
 * goog.object.extend(o, {a: 0, b: 1});
 * o; // {a: 0, b: 1}
 * goog.object.extend(o, {b: 2, c: 3});
 * o; // {a: 0, b: 2, c: 3}
 *
 * @param {Object} target The object to modify. Existing properties will be
 *     overwritten if they are also present in one of the objects in
 *     {@code var_args}.
 * @param {...Object} var_args The objects from which values will be copied.
 */
goog.object.extend = function(target, var_args) {
  var key, source;
  for (var i = 1; i < arguments.length; i++) {
    source = arguments[i];
    for (key in source) {
      target[key] = source[key];
    }

    // For IE the for-in-loop does not contain any properties that are not
    // enumerable on the prototype object (for example isPrototypeOf from
    // Object.prototype) and it will also not include 'replace' on objects that
    // extend String and change 'replace' (not that it is common for anyone to
    // extend anything except Object).

    for (var j = 0; j < goog.object.PROTOTYPE_FIELDS_.length; j++) {
      key = goog.object.PROTOTYPE_FIELDS_[j];
      if (Object.prototype.hasOwnProperty.call(source, key)) {
        target[key] = source[key];
      }
    }
  }
};


/**
 * Creates a new object built from the key-value pairs provided as arguments.
 * @param {...*} var_args If only one argument is provided and it is an array
 *     then this is used as the arguments,  otherwise even arguments are used as
 *     the property names and odd arguments are used as the property values.
 * @return {!Object} The new object.
 * @throws {Error} If there are uneven number of arguments or there is only one
 *     non array argument.
 */
goog.object.create = function(var_args) {
  var argLength = arguments.length;
  if (argLength == 1 && goog.isArray(arguments[0])) {
    return goog.object.create.apply(null, arguments[0]);
  }

  if (argLength % 2) {
    throw Error('Uneven number of arguments');
  }

  var rv = {};
  for (var i = 0; i < argLength; i += 2) {
    rv[arguments[i]] = arguments[i + 1];
  }
  return rv;
};


/**
 * Creates a new object where the property names come from the arguments but
 * the value is always set to true
 * @param {...*} var_args If only one argument is provided and it is an array
 *     then this is used as the arguments,  otherwise the arguments are used
 *     as the property names.
 * @return {!Object} The new object.
 */
goog.object.createSet = function(var_args) {
  var argLength = arguments.length;
  if (argLength == 1 && goog.isArray(arguments[0])) {
    return goog.object.createSet.apply(null, arguments[0]);
  }

  var rv = {};
  for (var i = 0; i < argLength; i++) {
    rv[arguments[i]] = true;
  }
  return rv;
};


/**
 * Creates an immutable view of the underlying object, if the browser
 * supports immutable objects.
 *
 * In default mode, writes to this view will fail silently. In strict mode,
 * they will throw an error.
 *
 * @param {!Object.<K,V>} obj An object.
 * @return {!Object.<K,V>} An immutable view of that object, or the
 *     original object if this browser does not support immutables.
 * @template K,V
 */
goog.object.createImmutableView = function(obj) {
  var result = obj;
  if (Object.isFrozen && !Object.isFrozen(obj)) {
    result = Object.create(obj);
    Object.freeze(result);
  }
  return result;
};


/**
 * @param {!Object} obj An object.
 * @return {boolean} Whether this is an immutable view of the object.
 */
goog.object.isImmutableView = function(obj) {
  return !!Object.isFrozen && Object.isFrozen(obj);
};

// Copyright 2013 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview A map of listeners that provides utility functions to
 * deal with listeners on an event target. Used by
 * {@code goog.events.EventTarget}.
 *
 * WARNING: Do not use this class from outside goog.events package.
 *
 * @visibility {//closure/goog/bin/sizetests:__pkg__}
 * @visibility {//closure/goog/events:__pkg__}
 * @visibility {//closure/goog/labs/events:__pkg__}
 */

goog.provide('goog.events.ListenerMap');

goog.require('goog.array');
goog.require('goog.events.Listener');
goog.require('goog.object');



/**
 * Creates a new listener map.
 * @param {EventTarget|goog.events.Listenable} src The src object.
 * @constructor
 * @final
 */
goog.events.ListenerMap = function(src) {
  /** @type {EventTarget|goog.events.Listenable} */
  this.src = src;

  /**
   * Maps of event type to an array of listeners.
   * @type {Object.<string, !Array.<!goog.events.Listener>>}
   */
  this.listeners = {};

  /**
   * The count of types in this map that have registered listeners.
   * @private {number}
   */
  this.typeCount_ = 0;
};


/**
 * @return {number} The count of event types in this map that actually
 *     have registered listeners.
 */
goog.events.ListenerMap.prototype.getTypeCount = function() {
  return this.typeCount_;
};


/**
 * @return {number} Total number of registered listeners.
 */
goog.events.ListenerMap.prototype.getListenerCount = function() {
  var count = 0;
  for (var type in this.listeners) {
    count += this.listeners[type].length;
  }
  return count;
};


/**
 * Adds an event listener. A listener can only be added once to an
 * object and if it is added again the key for the listener is
 * returned.
 *
 * Note that a one-off listener will not change an existing listener,
 * if any. On the other hand a normal listener will change existing
 * one-off listener to become a normal listener.
 *
 * @param {string|!goog.events.EventId} type The listener event type.
 * @param {!Function} listener This listener callback method.
 * @param {boolean} callOnce Whether the listener is a one-off
 *     listener.
 * @param {boolean=} opt_useCapture The capture mode of the listener.
 * @param {Object=} opt_listenerScope Object in whose scope to call the
 *     listener.
 * @return {goog.events.ListenableKey} Unique key for the listener.
 */
goog.events.ListenerMap.prototype.add = function(
    type, listener, callOnce, opt_useCapture, opt_listenerScope) {
  var typeStr = type.toString();
  var listenerArray = this.listeners[typeStr];
  if (!listenerArray) {
    listenerArray = this.listeners[typeStr] = [];
    this.typeCount_++;
  }

  var listenerObj;
  var index = goog.events.ListenerMap.findListenerIndex_(
      listenerArray, listener, opt_useCapture, opt_listenerScope);
  if (index > -1) {
    listenerObj = listenerArray[index];
    if (!callOnce) {
      // Ensure that, if there is an existing callOnce listener, it is no
      // longer a callOnce listener.
      listenerObj.callOnce = false;
    }
  } else {
    listenerObj = new goog.events.Listener(
        listener, null, this.src, typeStr, !!opt_useCapture, opt_listenerScope);
    listenerObj.callOnce = callOnce;
    listenerArray.push(listenerObj);
  }
  return listenerObj;
};


/**
 * Removes a matching listener.
 * @param {string|!goog.events.EventId} type The listener event type.
 * @param {!Function} listener This listener callback method.
 * @param {boolean=} opt_useCapture The capture mode of the listener.
 * @param {Object=} opt_listenerScope Object in whose scope to call the
 *     listener.
 * @return {boolean} Whether any listener was removed.
 */
goog.events.ListenerMap.prototype.remove = function(
    type, listener, opt_useCapture, opt_listenerScope) {
  var typeStr = type.toString();
  if (!(typeStr in this.listeners)) {
    return false;
  }

  var listenerArray = this.listeners[typeStr];
  var index = goog.events.ListenerMap.findListenerIndex_(
      listenerArray, listener, opt_useCapture, opt_listenerScope);
  if (index > -1) {
    var listenerObj = listenerArray[index];
    listenerObj.markAsRemoved();
    goog.array.removeAt(listenerArray, index);
    if (listenerArray.length == 0) {
      delete this.listeners[typeStr];
      this.typeCount_--;
    }
    return true;
  }
  return false;
};


/**
 * Removes the given listener object.
 * @param {goog.events.ListenableKey} listener The listener to remove.
 * @return {boolean} Whether the listener is removed.
 */
goog.events.ListenerMap.prototype.removeByKey = function(listener) {
  var type = listener.type;
  if (!(type in this.listeners)) {
    return false;
  }

  var removed = goog.array.remove(this.listeners[type], listener);
  if (removed) {
    listener.markAsRemoved();
    if (this.listeners[type].length == 0) {
      delete this.listeners[type];
      this.typeCount_--;
    }
  }
  return removed;
};


/**
 * Removes all listeners from this map. If opt_type is provided, only
 * listeners that match the given type are removed.
 * @param {string|!goog.events.EventId=} opt_type Type of event to remove.
 * @return {number} Number of listeners removed.
 */
goog.events.ListenerMap.prototype.removeAll = function(opt_type) {
  var typeStr = opt_type && opt_type.toString();
  var count = 0;
  for (var type in this.listeners) {
    if (!typeStr || type == typeStr) {
      var listenerArray = this.listeners[type];
      for (var i = 0; i < listenerArray.length; i++) {
        ++count;
        listenerArray[i].markAsRemoved();
      }
      delete this.listeners[type];
      this.typeCount_--;
    }
  }
  return count;
};


/**
 * Gets all listeners that match the given type and capture mode. The
 * returned array is a copy (but the listener objects are not).
 * @param {string|!goog.events.EventId} type The type of the listeners
 *     to retrieve.
 * @param {boolean} capture The capture mode of the listeners to retrieve.
 * @return {!Array.<goog.events.ListenableKey>} An array of matching
 *     listeners.
 */
goog.events.ListenerMap.prototype.getListeners = function(type, capture) {
  var listenerArray = this.listeners[type.toString()];
  var rv = [];
  if (listenerArray) {
    for (var i = 0; i < listenerArray.length; ++i) {
      var listenerObj = listenerArray[i];
      if (listenerObj.capture == capture) {
        rv.push(listenerObj);
      }
    }
  }
  return rv;
};


/**
 * Gets the goog.events.ListenableKey for the event or null if no such
 * listener is in use.
 *
 * @param {string|!goog.events.EventId} type The type of the listener
 *     to retrieve.
 * @param {!Function} listener The listener function to get.
 * @param {boolean} capture Whether the listener is a capturing listener.
 * @param {Object=} opt_listenerScope Object in whose scope to call the
 *     listener.
 * @return {goog.events.ListenableKey} the found listener or null if not found.
 */
goog.events.ListenerMap.prototype.getListener = function(
    type, listener, capture, opt_listenerScope) {
  var listenerArray = this.listeners[type.toString()];
  var i = -1;
  if (listenerArray) {
    i = goog.events.ListenerMap.findListenerIndex_(
        listenerArray, listener, capture, opt_listenerScope);
  }
  return i > -1 ? listenerArray[i] : null;
};


/**
 * Whether there is a matching listener. If either the type or capture
 * parameters are unspecified, the function will match on the
 * remaining criteria.
 *
 * @param {string|!goog.events.EventId=} opt_type The type of the listener.
 * @param {boolean=} opt_capture The capture mode of the listener.
 * @return {boolean} Whether there is an active listener matching
 *     the requested type and/or capture phase.
 */
goog.events.ListenerMap.prototype.hasListener = function(
    opt_type, opt_capture) {
  var hasType = goog.isDef(opt_type);
  var typeStr = hasType ? opt_type.toString() : '';
  var hasCapture = goog.isDef(opt_capture);

  return goog.object.some(
      this.listeners, function(listenerArray, type) {
        for (var i = 0; i < listenerArray.length; ++i) {
          if ((!hasType || listenerArray[i].type == typeStr) &&
              (!hasCapture || listenerArray[i].capture == opt_capture)) {
            return true;
          }
        }

        return false;
      });
};


/**
 * Finds the index of a matching goog.events.Listener in the given
 * listenerArray.
 * @param {!Array.<!goog.events.Listener>} listenerArray Array of listener.
 * @param {!Function} listener The listener function.
 * @param {boolean=} opt_useCapture The capture flag for the listener.
 * @param {Object=} opt_listenerScope The listener scope.
 * @return {number} The index of the matching listener within the
 *     listenerArray.
 * @private
 */
goog.events.ListenerMap.findListenerIndex_ = function(
    listenerArray, listener, opt_useCapture, opt_listenerScope) {
  for (var i = 0; i < listenerArray.length; ++i) {
    var listenerObj = listenerArray[i];
    if (!listenerObj.removed &&
        listenerObj.listener == listener &&
        listenerObj.capture == !!opt_useCapture &&
        listenerObj.handler == opt_listenerScope) {
      return i;
    }
  }
  return -1;
};

// Copyright 2005 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * @fileoverview An event manager for both native browser event
 * targets and custom JavaScript event targets
 * ({@code goog.events.Listenable}). This provides an abstraction
 * over browsers' event systems.
 *
 * It also provides a simulation of W3C event model's capture phase in
 * Internet Explorer (IE 8 and below). Caveat: the simulation does not
 * interact well with listeners registered directly on the elements
 * (bypassing goog.events) or even with listeners registered via
 * goog.events in a separate JS binary. In these cases, we provide
 * no ordering guarantees.
 *
 * The listeners will receive a "patched" event object. Such event object
 * contains normalized values for certain event properties that differs in
 * different browsers.
 *
 * Example usage:
 * <pre>
 * goog.events.listen(myNode, 'click', function(e) { alert('woo') });
 * goog.events.listen(myNode, 'mouseover', mouseHandler, true);
 * goog.events.unlisten(myNode, 'mouseover', mouseHandler, true);
 * goog.events.removeAll(myNode);
 * </pre>
 *
 *                                            in IE and event object patching]
 *
 * @see ../demos/events.html
 * @see ../demos/event-propagation.html
 * @see ../demos/stopevent.html
 */

// IMPLEMENTATION NOTES:
// goog.events stores an auxiliary data structure on each EventTarget
// source being listened on. This allows us to take advantage of GC,
// having the data structure GC'd when the EventTarget is GC'd. This
// GC behavior is equivalent to using W3C DOM Events directly.

goog.provide('goog.events');
goog.provide('goog.events.CaptureSimulationMode');
goog.provide('goog.events.Key');
goog.provide('goog.events.ListenableType');

goog.require('goog.asserts');
goog.require('goog.debug.entryPointRegistry');
goog.require('goog.events.BrowserEvent');
goog.require('goog.events.BrowserFeature');
goog.require('goog.events.Listenable');
goog.require('goog.events.ListenerMap');


/**
 * @typedef {number|goog.events.ListenableKey}
 */
goog.events.Key;


/**
 * @typedef {EventTarget|goog.events.Listenable}
 */
goog.events.ListenableType;


/**
 * Container for storing event listeners and their proxies
 *
 * TODO(user): Remove this when all external usage is
 * purged. goog.events no longer use goog.events.listeners_ for
 * anything meaningful.
 *
 * @private {!Object.<goog.events.ListenableKey>}
 */
goog.events.listeners_ = {};


/**
 * Property name on a native event target for the listener map
 * associated with the event target.
 * @const
 * @private
 */
goog.events.LISTENER_MAP_PROP_ = 'closure_lm_' + ((Math.random() * 1e6) | 0);


/**
 * String used to prepend to IE event types.
 * @const
 * @private
 */
goog.events.onString_ = 'on';


/**
 * Map of computed "on<eventname>" strings for IE event types. Caching
 * this removes an extra object allocation in goog.events.listen which
 * improves IE6 performance.
 * @const
 * @dict
 * @private
 */
goog.events.onStringMap_ = {};


/**
 * @enum {number} Different capture simulation mode for IE8-.
 */
goog.events.CaptureSimulationMode = {
  /**
   * Does not perform capture simulation. Will asserts in IE8- when you
   * add capture listeners.
   */
  OFF_AND_FAIL: 0,

  /**
   * Does not perform capture simulation, silently ignore capture
   * listeners.
   */
  OFF_AND_SILENT: 1,

  /**
   * Performs capture simulation.
   */
  ON: 2
};


/**
 * @define {number} The capture simulation mode for IE8-. By default,
 *     this is ON.
 */
goog.define('goog.events.CAPTURE_SIMULATION_MODE', 2);


/**
 * Estimated count of total native listeners.
 * @private {number}
 */
goog.events.listenerCountEstimate_ = 0;


/**
 * Adds an event listener for a specific event on a native event
 * target (such as a DOM element) or an object that has implemented
 * {@link goog.events.Listenable}. A listener can only be added once
 * to an object and if it is added again the key for the listener is
 * returned. Note that if the existing listener is a one-off listener
 * (registered via listenOnce), it will no longer be a one-off
 * listener after a call to listen().
 *
 * @param {EventTarget|goog.events.Listenable} src The node to listen
 *     to events on.
 * @param {string|Array.<string>|
 *     !goog.events.EventId.<EVENTOBJ>|!Array.<!goog.events.EventId.<EVENTOBJ>>}
 *     type Event type or array of event types.
 * @param {function(this:T, EVENTOBJ):?|{handleEvent:function(?):?}|null}
 *     listener Callback method, or an object with a handleEvent function.
 *     WARNING: passing an Object is now softly deprecated.
 * @param {boolean=} opt_capt Whether to fire in capture phase (defaults to
 *     false).
 * @param {T=} opt_handler Element in whose scope to call the listener.
 * @return {goog.events.Key} Unique key for the listener.
 * @template T,EVENTOBJ
 */
goog.events.listen = function(src, type, listener, opt_capt, opt_handler) {
  if (goog.isArray(type)) {
    for (var i = 0; i < type.length; i++) {
      goog.events.listen(src, type[i], listener, opt_capt, opt_handler);
    }
    return null;
  }

  listener = goog.events.wrapListener(listener);
  if (goog.events.Listenable.isImplementedBy(src)) {
    return src.listen(
        /** @type {string|!goog.events.EventId} */ (type),
        listener, opt_capt, opt_handler);
  } else {
    return goog.events.listen_(
        /** @type {EventTarget} */ (src),
        /** @type {string|!goog.events.EventId} */ (type),
        listener, /* callOnce */ false, opt_capt, opt_handler);
  }
};


/**
 * Adds an event listener for a specific event on a native event
 * target. A listener can only be added once to an object and if it
 * is added again the key for the listener is returned.
 *
 * Note that a one-off listener will not change an existing listener,
 * if any. On the other hand a normal listener will change existing
 * one-off listener to become a normal listener.
 *
 * @param {EventTarget} src The node to listen to events on.
 * @param {string|!goog.events.EventId} type Event type.
 * @param {!Function} listener Callback function.
 * @param {boolean} callOnce Whether the listener is a one-off
 *     listener or otherwise.
 * @param {boolean=} opt_capt Whether to fire in capture phase (defaults to
 *     false).
 * @param {Object=} opt_handler Element in whose scope to call the listener.
 * @return {goog.events.ListenableKey} Unique key for the listener.
 * @private
 */
goog.events.listen_ = function(
    src, type, listener, callOnce, opt_capt, opt_handler) {
  if (!type) {
    throw Error('Invalid event type');
  }

  var capture = !!opt_capt;
  if (capture && !goog.events.BrowserFeature.HAS_W3C_EVENT_SUPPORT) {
    if (goog.events.CAPTURE_SIMULATION_MODE ==
        goog.events.CaptureSimulationMode.OFF_AND_FAIL) {
      goog.asserts.fail('Can not register capture listener in IE8-.');
      return null;
    } else if (goog.events.CAPTURE_SIMULATION_MODE ==
        goog.events.CaptureSimulationMode.OFF_AND_SILENT) {
      return null;
    }
  }

  var listenerMap = goog.events.getListenerMap_(src);
  if (!listenerMap) {
    src[goog.events.LISTENER_MAP_PROP_] = listenerMap =
        new goog.events.ListenerMap(src);
  }

  var listenerObj = listenerMap.add(
      type, listener, callOnce, opt_capt, opt_handler);

  // If the listenerObj already has a proxy, it has been set up
  // previously. We simply return.
  if (listenerObj.proxy) {
    return listenerObj;
  }

  var proxy = goog.events.getProxy();
  listenerObj.proxy = proxy;

  proxy.src = src;
  proxy.listener = listenerObj;

  // Attach the proxy through the browser's API
  if (src.addEventListener) {
    src.addEventListener(type.toString(), proxy, capture);
  } else {
    // The else above used to be else if (src.attachEvent) and then there was
    // another else statement that threw an exception warning the developer
    // they made a mistake. This resulted in an extra object allocation in IE6
    // due to a wrapper object that had to be implemented around the element
    // and so was removed.
    src.attachEvent(goog.events.getOnString_(type.toString()), proxy);
  }

  goog.events.listenerCountEstimate_++;
  return listenerObj;
};


/**
 * Helper function for returning a proxy function.
 * @return {!Function} A new or reused function object.
 */
goog.events.getProxy = function() {
  var proxyCallbackFunction = goog.events.handleBrowserEvent_;
  // Use a local var f to prevent one allocation.
  var f = goog.events.BrowserFeature.HAS_W3C_EVENT_SUPPORT ?
      function(eventObject) {
        return proxyCallbackFunction.call(f.src, f.listener, eventObject);
      } :
      function(eventObject) {
        var v = proxyCallbackFunction.call(f.src, f.listener, eventObject);
        // NOTE(user): In IE, we hack in a capture phase. However, if
        // there is inline event handler which tries to prevent default (for
        // example <a href="..." onclick="return false">...</a>) in a
        // descendant element, the prevent default will be overridden
        // by this listener if this listener were to return true. Hence, we
        // return undefined.
        if (!v) return v;
      };
  return f;
};


/**
 * Adds an event listener for a specific event on a native event
 * target (such as a DOM element) or an object that has implemented
 * {@link goog.events.Listenable}. After the event has fired the event
 * listener is removed from the target.
 *
 * If an existing listener already exists, listenOnce will do
 * nothing. In particular, if the listener was previously registered
 * via listen(), listenOnce() will not turn the listener into a
 * one-off listener. Similarly, if there is already an existing
 * one-off listener, listenOnce does not modify the listeners (it is
 * still a once listener).
 *
 * @param {EventTarget|goog.events.Listenable} src The node to listen
 *     to events on.
 * @param {string|Array.<string>|
 *     !goog.events.EventId.<EVENTOBJ>|!Array.<!goog.events.EventId.<EVENTOBJ>>}
 *     type Event type or array of event types.
 * @param {function(this:T, EVENTOBJ):?|{handleEvent:function(?):?}|null}
 *     listener Callback method.
 * @param {boolean=} opt_capt Fire in capture phase?.
 * @param {T=} opt_handler Element in whose scope to call the listener.
 * @return {goog.events.Key} Unique key for the listener.
 * @template T,EVENTOBJ
 */
goog.events.listenOnce = function(src, type, listener, opt_capt, opt_handler) {
  if (goog.isArray(type)) {
    for (var i = 0; i < type.length; i++) {
      goog.events.listenOnce(src, type[i], listener, opt_capt, opt_handler);
    }
    return null;
  }

  listener = goog.events.wrapListener(listener);
  if (goog.events.Listenable.isImplementedBy(src)) {
    return src.listenOnce(
        /** @type {string|!goog.events.EventId} */ (type),
        listener, opt_capt, opt_handler);
  } else {
    return goog.events.listen_(
        /** @type {EventTarget} */ (src),
        /** @type {string|!goog.events.EventId} */ (type),
        listener, /* callOnce */ true, opt_capt, opt_handler);
  }
};


/**
 * Adds an event listener with a specific event wrapper on a DOM Node or an
 * object that has implemented {@link goog.events.Listenable}. A listener can
 * only be added once to an object.
 *
 * @param {EventTarget|goog.events.Listenable} src The target to
 *     listen to events on.
 * @param {goog.events.EventWrapper} wrapper Event wrapper to use.
 * @param {function(this:T, ?):?|{handleEvent:function(?):?}|null} listener
 *     Callback method, or an object with a handleEvent function.
 * @param {boolean=} opt_capt Whether to fire in capture phase (defaults to
 *     false).
 * @param {T=} opt_handler Element in whose scope to call the listener.
 * @template T
 */
goog.events.listenWithWrapper = function(src, wrapper, listener, opt_capt,
    opt_handler) {
  wrapper.listen(src, listener, opt_capt, opt_handler);
};


/**
 * Removes an event listener which was added with listen().
 *
 * @param {EventTarget|goog.events.Listenable} src The target to stop
 *     listening to events on.
 * @param {string|Array.<string>|
 *     !goog.events.EventId.<EVENTOBJ>|!Array.<!goog.events.EventId.<EVENTOBJ>>}
 *     type Event type or array of event types to unlisten to.
 * @param {function(?):?|{handleEvent:function(?):?}|null} listener The
 *     listener function to remove.
 * @param {boolean=} opt_capt In DOM-compliant browsers, this determines
 *     whether the listener is fired during the capture or bubble phase of the
 *     event.
 * @param {Object=} opt_handler Element in whose scope to call the listener.
 * @return {?boolean} indicating whether the listener was there to remove.
 * @template EVENTOBJ
 */
goog.events.unlisten = function(src, type, listener, opt_capt, opt_handler) {
  if (goog.isArray(type)) {
    for (var i = 0; i < type.length; i++) {
      goog.events.unlisten(src, type[i], listener, opt_capt, opt_handler);
    }
    return null;
  }

  listener = goog.events.wrapListener(listener);
  if (goog.events.Listenable.isImplementedBy(src)) {
    return src.unlisten(
        /** @type {string|!goog.events.EventId} */ (type),
        listener, opt_capt, opt_handler);
  }

  if (!src) {
    // TODO(user): We should tighten the API to only accept
    // non-null objects, or add an assertion here.
    return false;
  }

  var capture = !!opt_capt;
  var listenerMap = goog.events.getListenerMap_(
      /** @type {EventTarget} */ (src));
  if (listenerMap) {
    var listenerObj = listenerMap.getListener(
        /** @type {string|!goog.events.EventId} */ (type),
        listener, capture, opt_handler);
    if (listenerObj) {
      return goog.events.unlistenByKey(listenerObj);
    }
  }

  return false;
};


/**
 * Removes an event listener which was added with listen() by the key
 * returned by listen().
 *
 * @param {goog.events.Key} key The key returned by listen() for this
 *     event listener.
 * @return {boolean} indicating whether the listener was there to remove.
 */
goog.events.unlistenByKey = function(key) {
  // TODO(user): Remove this check when tests that rely on this
  // are fixed.
  if (goog.isNumber(key)) {
    return false;
  }

  var listener = /** @type {goog.events.ListenableKey} */ (key);
  if (!listener || listener.removed) {
    return false;
  }

  var src = listener.src;
  if (goog.events.Listenable.isImplementedBy(src)) {
    return src.unlistenByKey(listener);
  }

  var type = listener.type;
  var proxy = listener.proxy;
  if (src.removeEventListener) {
    src.removeEventListener(type, proxy, listener.capture);
  } else if (src.detachEvent) {
    src.detachEvent(goog.events.getOnString_(type), proxy);
  }
  goog.events.listenerCountEstimate_--;

  var listenerMap = goog.events.getListenerMap_(
      /** @type {EventTarget} */ (src));
  // TODO(user): Try to remove this conditional and execute the
  // first branch always. This should be safe.
  if (listenerMap) {
    listenerMap.removeByKey(listener);
    if (listenerMap.getTypeCount() == 0) {
      // Null the src, just because this is simple to do (and useful
      // for IE <= 7).
      listenerMap.src = null;
      // We don't use delete here because IE does not allow delete
      // on a window object.
      src[goog.events.LISTENER_MAP_PROP_] = null;
    }
  } else {
    listener.markAsRemoved();
  }

  return true;
};


/**
 * Removes an event listener which was added with listenWithWrapper().
 *
 * @param {EventTarget|goog.events.Listenable} src The target to stop
 *     listening to events on.
 * @param {goog.events.EventWrapper} wrapper Event wrapper to use.
 * @param {function(?):?|{handleEvent:function(?):?}|null} listener The
 *     listener function to remove.
 * @param {boolean=} opt_capt In DOM-compliant browsers, this determines
 *     whether the listener is fired during the capture or bubble phase of the
 *     event.
 * @param {Object=} opt_handler Element in whose scope to call the listener.
 */
goog.events.unlistenWithWrapper = function(src, wrapper, listener, opt_capt,
    opt_handler) {
  wrapper.unlisten(src, listener, opt_capt, opt_handler);
};


/**
 * Removes all listeners from an object. You can also optionally
 * remove listeners of a particular type.
 *
 * @param {Object|undefined} obj Object to remove listeners from. Must be an
 *     EventTarget or a goog.events.Listenable.
 * @param {string|!goog.events.EventId=} opt_type Type of event to remove.
 *     Default is all types.
 * @return {number} Number of listeners removed.
 */
goog.events.removeAll = function(obj, opt_type) {
  // TODO(user): Change the type of obj to
  // (!EventTarget|!goog.events.Listenable).

  if (!obj) {
    return 0;
  }

  if (goog.events.Listenable.isImplementedBy(obj)) {
    return obj.removeAllListeners(opt_type);
  }

  var listenerMap = goog.events.getListenerMap_(
      /** @type {EventTarget} */ (obj));
  if (!listenerMap) {
    return 0;
  }

  var count = 0;
  var typeStr = opt_type && opt_type.toString();
  for (var type in listenerMap.listeners) {
    if (!typeStr || type == typeStr) {
      // Clone so that we don't need to worry about unlistenByKey
      // changing the content of the ListenerMap.
      var listeners = listenerMap.listeners[type].concat();
      for (var i = 0; i < listeners.length; ++i) {
        if (goog.events.unlistenByKey(listeners[i])) {
          ++count;
        }
      }
    }
  }
  return count;
};


/**
 * Removes all native listeners registered via goog.events. Native
 * listeners are listeners on native browser objects (such as DOM
 * elements). In particular, goog.events.Listenable and
 * goog.events.EventTarget listeners will NOT be removed.
 * @return {number} Number of listeners removed.
 * @deprecated This doesn't do anything, now that Closure no longer
 * stores a central listener registry.
 */
goog.events.removeAllNativeListeners = function() {
  goog.events.listenerCountEstimate_ = 0;
  return 0;
};


/**
 * Gets the listeners for a given object, type and capture phase.
 *
 * @param {Object} obj Object to get listeners for.
 * @param {string|!goog.events.EventId} type Event type.
 * @param {boolean} capture Capture phase?.
 * @return {Array.<goog.events.Listener>} Array of listener objects.
 */
goog.events.getListeners = function(obj, type, capture) {
  if (goog.events.Listenable.isImplementedBy(obj)) {
    return obj.getListeners(type, capture);
  } else {
    if (!obj) {
      // TODO(user): We should tighten the API to accept
      // !EventTarget|goog.events.Listenable, and add an assertion here.
      return [];
    }

    var listenerMap = goog.events.getListenerMap_(
        /** @type {EventTarget} */ (obj));
    return listenerMap ? listenerMap.getListeners(type, capture) : [];
  }
};


/**
 * Gets the goog.events.Listener for the event or null if no such listener is
 * in use.
 *
 * @param {EventTarget|goog.events.Listenable} src The target from
 *     which to get listeners.
 * @param {?string|!goog.events.EventId.<EVENTOBJ>} type The type of the event.
 * @param {function(EVENTOBJ):?|{handleEvent:function(?):?}|null} listener The
 *     listener function to get.
 * @param {boolean=} opt_capt In DOM-compliant browsers, this determines
 *                            whether the listener is fired during the
 *                            capture or bubble phase of the event.
 * @param {Object=} opt_handler Element in whose scope to call the listener.
 * @return {goog.events.ListenableKey} the found listener or null if not found.
 * @template EVENTOBJ
 */
goog.events.getListener = function(src, type, listener, opt_capt, opt_handler) {
  // TODO(user): Change type from ?string to string, or add assertion.
  type = /** @type {string} */ (type);
  listener = goog.events.wrapListener(listener);
  var capture = !!opt_capt;
  if (goog.events.Listenable.isImplementedBy(src)) {
    return src.getListener(type, listener, capture, opt_handler);
  }

  if (!src) {
    // TODO(user): We should tighten the API to only accept
    // non-null objects, or add an assertion here.
    return null;
  }

  var listenerMap = goog.events.getListenerMap_(
      /** @type {EventTarget} */ (src));
  if (listenerMap) {
    return listenerMap.getListener(type, listener, capture, opt_handler);
  }
  return null;
};


/**
 * Returns whether an event target has any active listeners matching the
 * specified signature. If either the type or capture parameters are
 * unspecified, the function will match on the remaining criteria.
 *
 * @param {EventTarget|goog.events.Listenable} obj Target to get
 *     listeners for.
 * @param {string|!goog.events.EventId=} opt_type Event type.
 * @param {boolean=} opt_capture Whether to check for capture or bubble-phase
 *     listeners.
 * @return {boolean} Whether an event target has one or more listeners matching
 *     the requested type and/or capture phase.
 */
goog.events.hasListener = function(obj, opt_type, opt_capture) {
  if (goog.events.Listenable.isImplementedBy(obj)) {
    return obj.hasListener(opt_type, opt_capture);
  }

  var listenerMap = goog.events.getListenerMap_(
      /** @type {EventTarget} */ (obj));
  return !!listenerMap && listenerMap.hasListener(opt_type, opt_capture);
};


/**
 * Provides a nice string showing the normalized event objects public members
 * @param {Object} e Event Object.
 * @return {string} String of the public members of the normalized event object.
 */
goog.events.expose = function(e) {
  var str = [];
  for (var key in e) {
    if (e[key] && e[key].id) {
      str.push(key + ' = ' + e[key] + ' (' + e[key].id + ')');
    } else {
      str.push(key + ' = ' + e[key]);
    }
  }
  return str.join('\n');
};


/**
 * Returns a string with on prepended to the specified type. This is used for IE
 * which expects "on" to be prepended. This function caches the string in order
 * to avoid extra allocations in steady state.
 * @param {string} type Event type.
 * @return {string} The type string with 'on' prepended.
 * @private
 */
goog.events.getOnString_ = function(type) {
  if (type in goog.events.onStringMap_) {
    return goog.events.onStringMap_[type];
  }
  return goog.events.onStringMap_[type] = goog.events.onString_ + type;
};


/**
 * Fires an object's listeners of a particular type and phase
 *
 * @param {Object} obj Object whose listeners to call.
 * @param {string|!goog.events.EventId} type Event type.
 * @param {boolean} capture Which event phase.
 * @param {Object} eventObject Event object to be passed to listener.
 * @return {boolean} True if all listeners returned true else false.
 */
goog.events.fireListeners = function(obj, type, capture, eventObject) {
  if (goog.events.Listenable.isImplementedBy(obj)) {
    return obj.fireListeners(type, capture, eventObject);
  }

  return goog.events.fireListeners_(obj, type, capture, eventObject);
};


/**
 * Fires an object's listeners of a particular type and phase.
 * @param {Object} obj Object whose listeners to call.
 * @param {string|!goog.events.EventId} type Event type.
 * @param {boolean} capture Which event phase.
 * @param {Object} eventObject Event object to be passed to listener.
 * @return {boolean} True if all listeners returned true else false.
 * @private
 */
goog.events.fireListeners_ = function(obj, type, capture, eventObject) {
  var retval = 1;

  var listenerMap = goog.events.getListenerMap_(
      /** @type {EventTarget} */ (obj));
  if (listenerMap) {
    // TODO(user): Original code avoids array creation when there
    // is no listener, so we do the same. If this optimization turns
    // out to be not required, we can replace this with
    // listenerMap.getListeners(type, capture) instead, which is simpler.
    var listenerArray = listenerMap.listeners[type.toString()];
    if (listenerArray) {
      listenerArray = listenerArray.concat();
      for (var i = 0; i < listenerArray.length; i++) {
        var listener = listenerArray[i];
        // We might not have a listener if the listener was removed.
        if (listener && listener.capture == capture && !listener.removed) {
          retval &=
              goog.events.fireListener(listener, eventObject) !== false;
        }
      }
    }
  }
  return Boolean(retval);
};


/**
 * Fires a listener with a set of arguments
 *
 * @param {goog.events.Listener} listener The listener object to call.
 * @param {Object} eventObject The event object to pass to the listener.
 * @return {boolean} Result of listener.
 */
goog.events.fireListener = function(listener, eventObject) {
  var listenerFn = listener.listener;
  var listenerHandler = listener.handler || listener.src;

  if (listener.callOnce) {
    goog.events.unlistenByKey(listener);
  }
  return listenerFn.call(listenerHandler, eventObject);
};


/**
 * Gets the total number of listeners currently in the system.
 * @return {number} Number of listeners.
 * @deprecated This returns estimated count, now that Closure no longer
 * stores a central listener registry. We still return an estimation
 * to keep existing listener-related tests passing. In the near future,
 * this function will be removed.
 */
goog.events.getTotalListenerCount = function() {
  return goog.events.listenerCountEstimate_;
};


/**
 * Dispatches an event (or event like object) and calls all listeners
 * listening for events of this type. The type of the event is decided by the
 * type property on the event object.
 *
 * If any of the listeners returns false OR calls preventDefault then this
 * function will return false.  If one of the capture listeners calls
 * stopPropagation, then the bubble listeners won't fire.
 *
 * @param {goog.events.Listenable} src The event target.
 * @param {goog.events.EventLike} e Event object.
 * @return {boolean} If anyone called preventDefault on the event object (or
 *     if any of the handlers returns false) this will also return false.
 *     If there are no handlers, or if all handlers return true, this returns
 *     true.
 */
goog.events.dispatchEvent = function(src, e) {
  goog.asserts.assert(
      goog.events.Listenable.isImplementedBy(src),
      'Can not use goog.events.dispatchEvent with ' +
      'non-goog.events.Listenable instance.');
  return src.dispatchEvent(e);
};


/**
 * Installs exception protection for the browser event entry point using the
 * given error handler.
 *
 * @param {goog.debug.ErrorHandler} errorHandler Error handler with which to
 *     protect the entry point.
 */
goog.events.protectBrowserEventEntryPoint = function(errorHandler) {
  goog.events.handleBrowserEvent_ = errorHandler.protectEntryPoint(
      goog.events.handleBrowserEvent_);
};


/**
 * Handles an event and dispatches it to the correct listeners. This
 * function is a proxy for the real listener the user specified.
 *
 * @param {goog.events.Listener} listener The listener object.
 * @param {Event=} opt_evt Optional event object that gets passed in via the
 *     native event handlers.
 * @return {boolean} Result of the event handler.
 * @this {EventTarget} The object or Element that fired the event.
 * @private
 */
goog.events.handleBrowserEvent_ = function(listener, opt_evt) {
  if (listener.removed) {
    return true;
  }

  // Synthesize event propagation if the browser does not support W3C
  // event model.
  if (!goog.events.BrowserFeature.HAS_W3C_EVENT_SUPPORT) {
    var ieEvent = opt_evt ||
        /** @type {Event} */ (goog.getObjectByName('window.event'));
    var evt = new goog.events.BrowserEvent(ieEvent, this);
    var retval = true;

    if (goog.events.CAPTURE_SIMULATION_MODE ==
            goog.events.CaptureSimulationMode.ON) {
      // If we have not marked this event yet, we should perform capture
      // simulation.
      if (!goog.events.isMarkedIeEvent_(ieEvent)) {
        goog.events.markIeEvent_(ieEvent);

        var ancestors = [];
        for (var parent = evt.currentTarget; parent;
             parent = parent.parentNode) {
          ancestors.push(parent);
        }

        // Fire capture listeners.
        var type = listener.type;
        for (var i = ancestors.length - 1; !evt.propagationStopped_ && i >= 0;
             i--) {
          evt.currentTarget = ancestors[i];
          retval &= goog.events.fireListeners_(ancestors[i], type, true, evt);
        }

        // Fire bubble listeners.
        //
        // We can technically rely on IE to perform bubble event
        // propagation. However, it turns out that IE fires events in
        // opposite order of attachEvent registration, which broke
        // some code and tests that rely on the order. (While W3C DOM
        // Level 2 Events TR leaves the event ordering unspecified,
        // modern browsers and W3C DOM Level 3 Events Working Draft
        // actually specify the order as the registration order.)
        for (var i = 0; !evt.propagationStopped_ && i < ancestors.length; i++) {
          evt.currentTarget = ancestors[i];
          retval &= goog.events.fireListeners_(ancestors[i], type, false, evt);
        }
      }
    } else {
      retval = goog.events.fireListener(listener, evt);
    }
    return retval;
  }

  // Otherwise, simply fire the listener.
  return goog.events.fireListener(
      listener, new goog.events.BrowserEvent(opt_evt, this));
};


/**
 * This is used to mark the IE event object so we do not do the Closure pass
 * twice for a bubbling event.
 * @param {Event} e The IE browser event.
 * @private
 */
goog.events.markIeEvent_ = function(e) {
  // Only the keyCode and the returnValue can be changed. We use keyCode for
  // non keyboard events.
  // event.returnValue is a bit more tricky. It is undefined by default. A
  // boolean false prevents the default action. In a window.onbeforeunload and
  // the returnValue is non undefined it will be alerted. However, we will only
  // modify the returnValue for keyboard events. We can get a problem if non
  // closure events sets the keyCode or the returnValue

  var useReturnValue = false;

  if (e.keyCode == 0) {
    // We cannot change the keyCode in case that srcElement is input[type=file].
    // We could test that that is the case but that would allocate 3 objects.
    // If we use try/catch we will only allocate extra objects in the case of a
    // failure.
    /** @preserveTry */
    try {
      e.keyCode = -1;
      return;
    } catch (ex) {
      useReturnValue = true;
    }
  }

  if (useReturnValue ||
      /** @type {boolean|undefined} */ (e.returnValue) == undefined) {
    e.returnValue = true;
  }
};


/**
 * This is used to check if an IE event has already been handled by the Closure
 * system so we do not do the Closure pass twice for a bubbling event.
 * @param {Event} e  The IE browser event.
 * @return {boolean} True if the event object has been marked.
 * @private
 */
goog.events.isMarkedIeEvent_ = function(e) {
  return e.keyCode < 0 || e.returnValue != undefined;
};


/**
 * Counter to create unique event ids.
 * @private {number}
 */
goog.events.uniqueIdCounter_ = 0;


/**
 * Creates a unique event id.
 *
 * @param {string} identifier The identifier.
 * @return {string} A unique identifier.
 * @idGenerator
 */
goog.events.getUniqueId = function(identifier) {
  return identifier + '_' + goog.events.uniqueIdCounter_++;
};


/**
 * @param {EventTarget} src The source object.
 * @return {goog.events.ListenerMap} A listener map for the given
 *     source object, or null if none exists.
 * @private
 */
goog.events.getListenerMap_ = function(src) {
  var listenerMap = src[goog.events.LISTENER_MAP_PROP_];
  // IE serializes the property as well (e.g. when serializing outer
  // HTML). So we must check that the value is of the correct type.
  return listenerMap instanceof goog.events.ListenerMap ? listenerMap : null;
};


/**
 * Expando property for listener function wrapper for Object with
 * handleEvent.
 * @const
 * @private
 */
goog.events.LISTENER_WRAPPER_PROP_ = '__closure_events_fn_' +
    ((Math.random() * 1e9) >>> 0);


/**
 * @param {Object|Function} listener The listener function or an
 *     object that contains handleEvent method.
 * @return {!Function} Either the original function or a function that
 *     calls obj.handleEvent. If the same listener is passed to this
 *     function more than once, the same function is guaranteed to be
 *     returned.
 */
goog.events.wrapListener = function(listener) {
  goog.asserts.assert(listener, 'Listener can not be null.');

  if (goog.isFunction(listener)) {
    return listener;
  }

  goog.asserts.assert(
      listener.handleEvent, 'An object listener must have handleEvent method.');
  return listener[goog.events.LISTENER_WRAPPER_PROP_] ||
      (listener[goog.events.LISTENER_WRAPPER_PROP_] = function(e) {
        return listener.handleEvent(e);
      });
};


// Register the browser event handler as an entry point, so that
// it can be monitored for exception handling, etc.
goog.debug.entryPointRegistry.register(
    /**
     * @param {function(!Function): !Function} transformer The transforming
     *     function.
     */
    function(transformer) {
      goog.events.handleBrowserEvent_ = transformer(
          goog.events.handleBrowserEvent_);
    });

goog.provide('olgm.herald.View');

goog.require('goog.asserts');
goog.require('goog.events');
goog.require('goog.events.EventType');
goog.require('olgm');
goog.require('olgm.herald.Herald');



/**
 * The View Herald is responsible of synchronizing the view (center/zoom)
 * of boths maps together. The ol3 map view is the master here, i.e. changes
 * from the ol3 map view are given to the gmap map, but not vice-versa.
 *
 * When the browser window gets resized, the gmap map is also updated.
 *
 * @param {!ol.Map} ol3map
 * @param {!google.maps.Map} gmap
 * @constructor
 * @extends {olgm.herald.Herald}
 */
olgm.herald.View = function(ol3map, gmap) {
  goog.base(this, ol3map, gmap);
};
goog.inherits(olgm.herald.View, olgm.herald.Herald);


/**
 * On window resize, the GoogleMaps map gets recentered. To avoid doing this
 * too often, a timeout is set.
 * @type {?number}
 * @private
 */
olgm.herald.View.prototype.windowResizeTimerId_ = null;


/**
 * @inheritDoc
 */
olgm.herald.View.prototype.activate = function() {
  goog.base(this, 'activate');

  var view = this.ol3map.getView();
  var keys = this.listenerKeys;

  // listen to center change
  keys.push(view.on('change:center', this.setCenter, this));

  // listen to resolution change
  keys.push(view.on('change:resolution', this.setZoom, this));

  // listen to browser window resize
  this.googListenerKeys.push(goog.events.listen(
      window,
      goog.events.EventType.RESIZE,
      this.handleWindowResize_,
      false,
      this));

  this.setCenter();
  this.setZoom();
};


/**
 * @inheritDoc
 */
olgm.herald.View.prototype.deactivate = function() {
  goog.base(this, 'deactivate');
};


/**
 * Recenter the gmap map at the ol3 map center location.
 */
olgm.herald.View.prototype.setCenter = function() {
  var view = this.ol3map.getView();
  var projection = view.getProjection();
  var center = view.getCenter();
  if (goog.isArray(center)) {
    center = ol.proj.transform(center, projection, 'EPSG:4326');
    goog.asserts.assertArray(center);
    this.gmap.setCenter(new google.maps.LatLng(center[1], center[0]));
  }
};


/**
 * Set the gmap map zoom level at the ol3 map view zoom level.
 */
olgm.herald.View.prototype.setZoom = function() {
  var resolution = this.ol3map.getView().getResolution();
  if (goog.isNumber(resolution)) {
    var zoom = olgm.getZoomFromResolution(resolution);
    if (zoom !== null) {
      this.gmap.setZoom(zoom);
    }
  }
};


/**
 * Called when the browser window is resized. Set the center of the GoogleMaps
 * map after a slight delay.
 * @private
 */
olgm.herald.View.prototype.handleWindowResize_ = function() {
  if (!goog.isNull(this.windowResizeTimerId_)) {
    goog.global.clearTimeout(this.windowResizeTimerId_);
  }
  this.windowResizeTimerId_ = window.setTimeout(
      goog.bind(this.setCenterAfterResize_, this),
      100);
};


/**
 * Called after the browser window got resized, after a small delay.
 * Set the center of the GoogleMaps map and reset the timeout.
 * @private
 */
olgm.herald.View.prototype.setCenterAfterResize_ = function() {
  this.setCenter();
  this.windowResizeTimerId_ = null;
};

goog.provide('olgm.layer.Google');



/**
 * An ol3 layer object serving the purpose of being added to the ol3 map
 * as dummy layer. The `mapTypeId` defines which Google Maps map type the
 * layer represents.
 *
 * @param {!olgmx.layer.GoogleOptions=} opt_options Options.
 * @constructor
 * @extends {ol.layer.Group}
 * @api
 */
olgm.layer.Google = function(opt_options) {

  var options = opt_options !== undefined ? opt_options : {};

  goog.base(this, /** @type {olx.layer.GroupOptions} */ (options));

  /**
   * @type {google.maps.MapTypeId.<(number|string)>|string}
   * @private
   */
  this.mapTypeId_ = options.mapTypeId !== undefined ? options.mapTypeId :
      google.maps.MapTypeId.ROADMAP;

  /**
   * @type {?Array.<google.maps.MapTypeStyle>}
   * @private
   */
  this.styles_ = options.styles ? options.styles : null;

};
goog.inherits(olgm.layer.Google, ol.layer.Group);


/**
 * @return {google.maps.MapTypeId.<(number|string)>|string}
 * @api
 */
olgm.layer.Google.prototype.getMapTypeId = function() {
  return this.mapTypeId_;
};


/**
 * @return {?Array.<google.maps.MapTypeStyle>}
 */
olgm.layer.Google.prototype.getStyles = function() {
  return this.styles_;
};

goog.provide('olgm.herald.Layers');

goog.require('goog.asserts');
goog.require('olgm');
goog.require('olgm.gm');
goog.require('olgm.herald.Herald');
goog.require('olgm.herald.VectorSource');
goog.require('olgm.herald.View');
goog.require('olgm.layer.Google');



/**
 * The Layers Herald is responsible of synchronizing the layers from the
 * OpenLayers map to the Google Maps one. It listens to layers added and
 * removed, and also takes care of existing layers when activated.
 *
 * It is also responsible of the activation and deactivation of the
 * Google Maps map. When activated, it is rendered in the OpenLayers map
 * target element, and the OpenLayers map is put inside the Google Maps map
 * as a control that takes 100% of the size. The original state is restored
 * when deactivated.
 *
 * The supported layers are:
 *
 * `olgm.layer.Google`
 * -------------------
 *     When a google layer is added, the process of enabling the
 *     Google Maps  map is activated (if it is the first and if it's visible).
 *     If there is an existing and visible `olgm.layer.Google` in the map,
 *     then the top-most is used to define the map type id Google Maps has to
 *     switch to. **Limitation** The Google Maps map is always below the
 *     OpenLayers map, which means that the other OpenLayers layers are always
 *     on top of Google Maps.
 *
 * `ol.layer.Vector`
 * -----------------
 *     When a vector layers is added, a `olgm.herald.VectorSource` is created
 *     to manage its `ol.source.Vector`. The layer is immediately rendered
 *     fully transparent, making the interactions still possible over it
 *     while being invisible.
 *
 * @param {!ol.Map} ol3map
 * @param {!google.maps.Map} gmap
 * @param {boolean} watchVector
 * @constructor
 * @extends {olgm.herald.Herald}
 */
olgm.herald.Layers = function(ol3map, gmap, watchVector) {

  /**
   * @type {Array.<olgm.layer.Google>}
   * @private
   */
  this.googleLayers_ = [];

  /**
   * @type {Array.<olgm.herald.Layers.GoogleLayerCache>}
   * @private
   */
  this.googleCache_ = [];

  /**
   * @type {Array.<olgm.herald.Layers.VectorLayerCache>}
   * @private
   */
  this.vectorCache_ = [];

  /**
   * @type {Array.<ol.layer.Vector>}
   * @private
   */
  this.vectorLayers_ = [];

  /**
   * @type {olgm.herald.View}
   * @private
   */
  this.viewHerald_ = new olgm.herald.View(ol3map, gmap);

  /**
   * @type {boolean}
   * @private
   */
  this.watchVector_ = watchVector;


  // === Elements  === //

  /**
   * @type {Node}
   * @private
   */
  this.gmapEl_ = gmap.getDiv();

  /**
   * @type {Element}
   * @private
   */
  this.ol3mapEl_ = ol3map.getViewport();

  /**
   * @type {Element}
   * @private
   */
  this.targetEl_ = ol3map.getTargetElement();


  goog.base(this, ol3map, gmap);


  // some controls, like the ol.control.ZoomSlider, require the map div
  // to have a size. While activating Google Maps, the size of the ol3 map
  // becomes moot. The code below fixes that.
  var center = this.ol3map.getView().getCenter();
  if (!center) {
    this.ol3map.getView().once('change:center', function() {
      this.ol3map.once('postrender', function() {
        this.ol3mapIsRenderered_ = true;
        this.toggleGoogleMaps_();
      }, this);
      this.toggleGoogleMaps_();
    }, this);
  } else {
    this.ol3map.once('postrender', function() {
      this.ol3mapIsRenderered_ = true;
      this.toggleGoogleMaps_();
    }, this);
  }
};
goog.inherits(olgm.herald.Layers, olgm.herald.Herald);


/**
 * Flag that determines whether the GoogleMaps map is currently active, i.e.
 * is currently shown and has the OpenLayers map added as one of its control.
 * @type {boolean}
 * @private
 */
olgm.herald.Layers.prototype.googleMapsIsActive_ = false;


/**
 * @type {boolean}
 * @private
 */
olgm.herald.Layers.prototype.ol3mapIsRenderered_ = false;


/**
 * @inheritDoc
 */
olgm.herald.Layers.prototype.activate = function() {
  goog.base(this, 'activate');

  var layers = this.ol3map.getLayers();

  // watch existing layers
  layers.forEach(this.watchLayer_, this);

  // event listeners
  var keys = this.listenerKeys;
  keys.push(layers.on('add', this.handleLayersAdd_, this));
  keys.push(layers.on('remove', this.handleLayersRemove_, this));
};


/**
 * @inheritDoc
 */
olgm.herald.Layers.prototype.deactivate = function() {
  // unwatch existing layers
  this.ol3map.getLayers().forEach(this.unwatchLayer_, this);

  goog.base(this, 'deactivate');
};


/**
 * @return {boolean}
 */
olgm.herald.Layers.prototype.getGoogleMapsActive = function() {
  return this.googleMapsIsActive_;
};


/**
 * Callback method fired when a new layer is added to the map.
 * @param {ol.CollectionEvent} event Collection event.
 * @private
 */
olgm.herald.Layers.prototype.handleLayersAdd_ = function(event) {
  var layer = event.element;
  goog.asserts.assertInstanceof(layer, ol.layer.Base);
  this.watchLayer_(layer);
};


/**
 * Callback method fired when a layer is removed from the map.
 * @param {ol.CollectionEvent} event Collection event.
 * @private
 */
olgm.herald.Layers.prototype.handleLayersRemove_ = function(event) {
  var layer = event.element;
  goog.asserts.assertInstanceof(layer, ol.layer.Base);
  this.unwatchLayer_(layer);
};


/**
 * Watch the layer
 * @param {ol.layer.Base} layer
 * @private
 */
olgm.herald.Layers.prototype.watchLayer_ = function(layer) {
  if (layer instanceof olgm.layer.Google) {
    this.watchGoogleLayer_(layer);
  } else if (layer instanceof ol.layer.Vector && this.watchVector_) {
    this.watchVectorLayer_(layer);
  }
};


/**
 * Watch the google layer
 * @param {olgm.layer.Google} layer
 * @private
 */
olgm.herald.Layers.prototype.watchGoogleLayer_ = function(layer) {
  this.googleLayers_.push(layer);
  this.googleCache_.push(/** @type {olgm.herald.Layers.GoogleLayerCache} */ ({
    layer: layer,
    listenerKeys: [
      layer.on('change:visible', this.toggleGoogleMaps_, this)
    ]
  }));
  this.toggleGoogleMaps_();
};


/**
 * Watch the vector layer
 * @param {ol.layer.Vector} layer
 * @private
 */
olgm.herald.Layers.prototype.watchVectorLayer_ = function(layer) {

  var ol3map = this.ol3map;
  var gmap = this.gmap;

  // a source is required to work with this layer
  var source = layer.getSource();
  if (!source) {
    return;
  }

  this.vectorLayers_.push(layer);

  // Data
  var data = new google.maps.Data({
    'map': gmap
  });

  // Style
  var gmStyle = olgm.gm.createStyle(layer);
  if (gmStyle) {
    data.setStyle(gmStyle);
  }

  // herald
  var herald = new olgm.herald.VectorSource(ol3map, gmap, source, data);

  // opacity
  var opacity = layer.getOpacity();

  var cacheItem = /** {@type olgm.herald.Layers.VectorLayerCache} */ ({
    data: data,
    herald: herald,
    layer: layer,
    listenerKeys: [],
    opacity: opacity
  });

  cacheItem.listenerKeys.push(layer.on('change:visible',
      this.handleVectorLayerVisibleChange_.bind(this, cacheItem), this));

  this.activateVectorLayerCacheItem_(cacheItem);

  this.vectorCache_.push(cacheItem);
};


/**
 * Unwatch the layer
 * @param {ol.layer.Base} layer
 * @private
 */
olgm.herald.Layers.prototype.unwatchLayer_ = function(layer) {
  if (layer instanceof olgm.layer.Google) {
    this.unwatchGoogleLayer_(layer);
  } else if (layer instanceof ol.layer.Vector && this.watchVector_) {
    this.unwatchVectorLayer_(layer);
  }
};


/**
 * Unwatch the google layer
 * @param {olgm.layer.Google} layer
 * @private
 */
olgm.herald.Layers.prototype.unwatchGoogleLayer_ = function(layer) {
  var index = this.googleLayers_.indexOf(layer);
  if (index !== -1) {
    this.googleLayers_.splice(index, 1);

    var cacheItem = this.googleCache_[index];
    olgm.unlistenAllByKey(cacheItem.listenerKeys);

    this.googleCache_.splice(index, 1);

    this.toggleGoogleMaps_();
  }
};


/**
 * Unwatch the vector layer
 * @param {ol.layer.Vector} layer
 * @private
 */
olgm.herald.Layers.prototype.unwatchVectorLayer_ = function(layer) {
  var index = this.vectorLayers_.indexOf(layer);
  if (index !== -1) {
    this.vectorLayers_.splice(index, 1);

    var cacheItem = this.vectorCache_[index];
    olgm.unlistenAllByKey(cacheItem.listenerKeys);

    // data - unset
    cacheItem.data.setMap(null);

    // herald
    cacheItem.herald.deactivate();

    // opacity
    layer.setOpacity(cacheItem.opacity);

    this.vectorCache_.splice(index, 1);
  }
};


/**
 * Activates the GoogleMaps map, i.e. put it in the ol3 map target and put
 * the ol3 map inside the gmap controls.
 * @private
 */
olgm.herald.Layers.prototype.activateGoogleMaps_ = function() {

  var center = this.ol3map.getView().getCenter();
  if (this.googleMapsIsActive_ || !this.ol3mapIsRenderered_ || !center) {
    return;
  }

  this.targetEl_.removeChild(this.ol3mapEl_);
  this.targetEl_.appendChild(this.gmapEl_);
  this.gmap.controls[google.maps.ControlPosition.TOP_LEFT].push(
      this.ol3mapEl_);

  this.viewHerald_.activate();

  // the map div of GoogleMaps doesn't like being tossed aroud. The line
  // below fixes the UI issue of wrong size of the tiles of GoogleMaps
  google.maps.event.trigger(this.gmap, 'resize');

  // it's also possible that the google maps map is not exactly at the
  // correct location. Fix this manually here
  this.viewHerald_.setCenter();
  this.viewHerald_.setZoom();

  this.googleMapsIsActive_ = true;

  // activate all cache items
  this.vectorCache_.forEach(this.activateVectorLayerCacheItem_, this);
};


/**
 * Deactivates the GoogleMaps map, i.e. put the ol3 map back in its target
 * and remove the gmap map.
 * @private
 */
olgm.herald.Layers.prototype.deactivateGoogleMaps_ = function() {

  if (!this.googleMapsIsActive_) {
    return;
  }

  this.gmap.controls[google.maps.ControlPosition.TOP_LEFT].removeAt(0);
  this.targetEl_.removeChild(this.gmapEl_);
  this.targetEl_.appendChild(this.ol3mapEl_);

  this.viewHerald_.deactivate();

  this.ol3mapEl_.style.position = 'relative';

  // deactivate all cache items
  this.vectorCache_.forEach(this.deactivateVectorLayerCacheItem_, this);

  this.googleMapsIsActive_ = false;
};


/**
 * This method takes care of activating or deactivating the GoogleMaps map.
 * It is activated if at least one visible Google layer is currently in the
 * ol3 map (and vice-versa for deactivation). The top-most layer is used
 * to determine that. It is also used to change the GoogleMaps mapTypeId
 * accordingly too to fit the top-most ol3 Google layer.
 * @private
 */
olgm.herald.Layers.prototype.toggleGoogleMaps_ = function() {

  var found = null;

  // find top-most Google layer
  this.ol3map.getLayers().getArray().slice(0).reverse().every(
      function(layer) {
        if (layer instanceof olgm.layer.Google &&
            layer.getVisible() &&
            this.googleLayers_.indexOf(layer) !== -1) {
          found = layer;
          return false;
        } else {
          return true;
        }
      },
      this);

  if (found) {
    // set mapTypeId
    this.gmap.setMapTypeId(found.getMapTypeId());
    // set styles
    var styles = found.getStyles();
    if (styles) {
      this.gmap.setOptions({'styles': styles});
    } else {
      this.gmap.setOptions({'styles': null});
    }

    // activate
    this.activateGoogleMaps_();
  } else {
    // deactivate
    this.deactivateGoogleMaps_();
  }
};


/**
 * Activates a vector layer cache item, i.e. activate its herald and
 * render the layer invisible. Will only do so if the layer is visible.
 * @param {olgm.herald.Layers.VectorLayerCache} cacheItem
 * @private
 */
olgm.herald.Layers.prototype.activateVectorLayerCacheItem_ = function(
    cacheItem) {
  var layer = cacheItem.layer;
  var visible = layer.getVisible();
  if (visible && this.googleMapsIsActive_) {
    cacheItem.herald.activate();
    cacheItem.layer.setOpacity(0);
  }
};


/**
 * Deactivates a vector layer cache item, i.e. deactivate its herald and
 * restore the layer opacity.
 * @param {olgm.herald.Layers.VectorLayerCache} cacheItem
 * @private
 */
olgm.herald.Layers.prototype.deactivateVectorLayerCacheItem_ = function(
    cacheItem) {
  cacheItem.herald.deactivate();
  cacheItem.layer.setOpacity(cacheItem.opacity);
};


/**
 * @param {olgm.herald.Layers.VectorLayerCache} cacheItem
 * @private
 */
olgm.herald.Layers.prototype.handleVectorLayerVisibleChange_ = function(
    cacheItem) {
  var layer = cacheItem.layer;
  var visible = layer.getVisible();
  if (visible) {
    this.activateVectorLayerCacheItem_(cacheItem);
  } else {
    this.deactivateVectorLayerCacheItem_(cacheItem);
  }
};


/**
 * @typedef {{
 *   layer: (olgm.layer.Google),
 *   listenerKeys: (Array.<ol.events.Key|Array.<ol.events.Key>>)
 * }}
 */
olgm.herald.Layers.GoogleLayerCache;


/**
 * @typedef {{
 *   data: (google.maps.Data),
 *   herald: (olgm.herald.VectorSource),
 *   layer: (ol.layer.Vector),
 *   listenerKeys: (Array.<ol.events.Key|Array.<ol.events.Key>>),
 *   opacity: (number)
 * }}
 */
olgm.herald.Layers.VectorLayerCache;

goog.provide('olgm.interaction');


/**
 * Set of interactions that are recommended to include in ol3 maps used
 * by this library. It basically is the same as the default interactions
 * included in such maps by default, with the exclusion of some that are
 * not compatible with this library.
 *
 * Here's the one that are excluded and why:
 * * `ol.interaction.DragRotate` - Google Maps doesn't support the same kind
 *   of rotation that OL3 does.
 * * `ol.interaction.PinchRotate` - Google Maps doesn't support the same kind
 *   of rotation that OL3 does.
 * * `ol.interaction.DragPan` - The one created by OL3 includes kinetic
 *   animation while panning, which causes animation not being syncrhonized
 *   with Google Maps. It is excluded, but also replace by one that doesn't
 *   have kinetic enabled.
 *
 * @param {olx.interaction.DefaultsOptions=} opt_options Defaults options.
 * @return {ol.Collection.<ol.interaction.Interaction>} A collection of
 * interactions to be used with the ol.Map constructor's interactions option.
 * @api stable
 */
olgm.interaction.defaults = function(opt_options) {

  var options = (opt_options !== undefined) ? opt_options : {};

  options['altShiftDragRotate'] = false;
  options['dragPan'] = false;
  options['pinchRotate'] = false;

  return ol.interaction.defaults(options).extend([
    new ol.interaction.DragPan()
  ]);

};

goog.provide('olgm.OLGoogleMaps');

goog.require('goog.asserts');
goog.require('olgm.Abstract');
goog.require('olgm.herald.Layers');
goog.require('olgm.herald.View');



/**
 * The main component of this library. It binds an existing OpenLayers map to
 * a Google Maps map it creates through the use of `herald` objects. Each
 * herald is responsible of synchronizing something from the OpenLayers map
 * to the Google Maps one, which makes OpenLayers the master source of
 * interactions. This allows the development of applications without having
 * to worry about writing code that uses the Google Maps API.
 *
 * Here's an architecture overview of what the different heralds, where they
 * are created and on what they act:
 *
 *
 * olgm.OLGoogleMaps <-- ol.Map
 *  |
 *  |__ olgm.herald.Layers <-- ol.Collection.<ol.layer.Base>
 *       |                      |
 *       |                      |__olgm.layer.Google
 *       |                      |
 *       |                      |__ol.layer.Vector
 *       |
 *       |__olgm.herald.View <-- ol.View
 *       |
 *       |__olgm.herald.VectorSource <-- ol.source.Vector
 *          |
 *          |__olgm.herald.Feature <-- ol.Feature
 *
 *
 *
 * @param {!olgmx.OLGoogleMapsOptions} options Options.
 * @constructor
 * @extends {olgm.Abstract}
 * @api
 */
olgm.OLGoogleMaps = function(options) {

  /**
   * @type {Array.<olgm.herald.Herald>}
   * @private
   */
  this.heralds_ = [];

  var gmapEl = document.createElement('div');
  gmapEl.style.height = 'inherit';
  gmapEl.style.width = 'inherit';

  var gmap = new google.maps.Map(gmapEl, {
    disableDefaultUI: true,
    disableDoubleClickZoom: true,
    draggable: false,
    keyboardShortcuts: false,
    mapTypeId: google.maps.MapTypeId.ROADMAP,
    scrollwheel: false,
    streetViewControl: false
  });

  goog.base(this, options.map, gmap);

  var watchVector = options.watchVector !== undefined ?
      options.watchVector : true;

  /**
   * @type {olgm.herald.Layers}
   * @private
   */
  this.layersHerald_ = new olgm.herald.Layers(
      this.ol3map, this.gmap, watchVector);
  this.heralds_.push(this.layersHerald_);
};
goog.inherits(olgm.OLGoogleMaps, olgm.Abstract);


/**
 * @type {boolean}
 * @private
 */
olgm.OLGoogleMaps.prototype.active_ = false;


/**
 * @api
 */
olgm.OLGoogleMaps.prototype.activate = function() {

  if (this.active_) {
    return;
  }

  // activate heralds
  for (var i = 0, len = this.heralds_.length; i < len; i++) {
    this.heralds_[i].activate();
  }

  this.active_ = true;
};


/**
 * @api
 */
olgm.OLGoogleMaps.prototype.deactivate = function() {

  if (!this.active_) {
    return;
  }

  // deactivate heralds
  for (var i = 0, len = this.heralds_.length; i < len; i++) {
    this.heralds_[i].deactivate();
  }

  this.active_ = false;
};


/**
 * @return {boolean}
 * @api
 */
olgm.OLGoogleMaps.prototype.getGoogleMapsActive = function() {
  return this.active_ && this.layersHerald_.getGoogleMapsActive();
};


/**
 * @return {google.maps.Map}
 * @api
 */
olgm.OLGoogleMaps.prototype.getGoogleMapsMap = function() {
  return this.gmap;
};


/**
 * @api
 */
olgm.OLGoogleMaps.prototype.toggle = function() {
  if (this.active_) {
    this.deactivate();
  } else {
    this.activate();
  }
};

// Copyright 2009 The Closure Library Authors.
// All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// This file has been auto-generated by GenJsDeps, please do not edit.

goog.addDependency('demos/editor/equationeditor.js', ['goog.demos.editor.EquationEditor'], ['goog.ui.equation.EquationEditorDialog']);
goog.addDependency('demos/editor/helloworld.js', ['goog.demos.editor.HelloWorld'], ['goog.dom', 'goog.dom.TagName', 'goog.editor.Plugin']);
goog.addDependency('demos/editor/helloworlddialog.js', ['goog.demos.editor.HelloWorldDialog', 'goog.demos.editor.HelloWorldDialog.OkEvent'], ['goog.dom.TagName', 'goog.events.Event', 'goog.string', 'goog.ui.editor.AbstractDialog', 'goog.ui.editor.AbstractDialog.Builder', 'goog.ui.editor.AbstractDialog.EventType']);
goog.addDependency('demos/editor/helloworlddialogplugin.js', ['goog.demos.editor.HelloWorldDialogPlugin', 'goog.demos.editor.HelloWorldDialogPlugin.Command'], ['goog.demos.editor.HelloWorldDialog', 'goog.dom.TagName', 'goog.editor.plugins.AbstractDialogPlugin', 'goog.editor.range', 'goog.functions', 'goog.ui.editor.AbstractDialog.EventType']);

// Copyright 2014 The Closure Library Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS-IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// This file has been auto-generated by GenJsDeps, please do not edit.

goog.addDependency('../../third_party/closure/goog/caja/string/html/htmlparser.js', ['goog.string.html.HtmlParser', 'goog.string.html.HtmlParser.EFlags', 'goog.string.html.HtmlParser.Elements', 'goog.string.html.HtmlParser.Entities', 'goog.string.html.HtmlSaxHandler'], []);
goog.addDependency('../../third_party/closure/goog/caja/string/html/htmlsanitizer.js', ['goog.string.html.HtmlSanitizer', 'goog.string.html.HtmlSanitizer.AttributeType', 'goog.string.html.HtmlSanitizer.Attributes', 'goog.string.html.htmlSanitize'], ['goog.string.StringBuffer', 'goog.string.html.HtmlParser', 'goog.string.html.HtmlParser.EFlags', 'goog.string.html.HtmlParser.Elements', 'goog.string.html.HtmlSaxHandler']);
goog.addDependency('../../third_party/closure/goog/dojo/dom/query.js', ['goog.dom.query'], ['goog.array', 'goog.dom', 'goog.functions', 'goog.string', 'goog.userAgent']);
goog.addDependency('../../third_party/closure/goog/jpeg_encoder/jpeg_encoder_basic.js', ['goog.crypt.JpegEncoder'], ['goog.crypt.base64']);
goog.addDependency('../../third_party/closure/goog/loremipsum/text/loremipsum.js', ['goog.text.LoremIpsum'], ['goog.array', 'goog.math', 'goog.string', 'goog.structs.Map', 'goog.structs.Set']);
goog.addDependency('../../third_party/closure/goog/mochikit/async/deferred.js', ['goog.async.Deferred', 'goog.async.Deferred.AlreadyCalledError', 'goog.async.Deferred.CanceledError'], ['goog.Promise', 'goog.Thenable', 'goog.array', 'goog.asserts', 'goog.debug.Error']);
goog.addDependency('../../third_party/closure/goog/mochikit/async/deferredlist.js', ['goog.async.DeferredList'], ['goog.async.Deferred']);
goog.addDependency('../../third_party/closure/goog/osapi/osapi.js', ['goog.osapi'], []);
goog.addDependency('../../third_party/closure/goog/svgpan/svgpan.js', ['svgpan.SvgPan'], ['goog.Disposable', 'goog.events', 'goog.events.EventType', 'goog.events.MouseWheelHandler']);
goog.addDependency('a11y/aria/announcer.js', ['goog.a11y.aria.Announcer'], ['goog.Disposable', 'goog.a11y.aria', 'goog.a11y.aria.LivePriority', 'goog.a11y.aria.State', 'goog.dom', 'goog.object']);
goog.addDependency('a11y/aria/announcer_test.js', ['goog.a11y.aria.AnnouncerTest'], ['goog.a11y.aria', 'goog.a11y.aria.Announcer', 'goog.a11y.aria.LivePriority', 'goog.a11y.aria.State', 'goog.array', 'goog.dom', 'goog.dom.iframe', 'goog.testing.jsunit']);
goog.addDependency('a11y/aria/aria.js', ['goog.a11y.aria'], ['goog.a11y.aria.Role', 'goog.a11y.aria.State', 'goog.a11y.aria.datatables', 'goog.array', 'goog.asserts', 'goog.dom', 'goog.dom.TagName', 'goog.object']);
goog.addDependency('a11y/aria/aria_test.js', ['goog.a11y.ariaTest'], ['goog.a11y.aria', 'goog.a11y.aria.Role', 'goog.a11y.aria.State', 'goog.dom', 'goog.dom.TagName', 'goog.testing.jsunit']);
goog.addDependency('a11y/aria/attributes.js', ['goog.a11y.aria.AutoCompleteValues', 'goog.a11y.aria.CheckedValues', 'goog.a11y.aria.DropEffectValues', 'goog.a11y.aria.ExpandedValues', 'goog.a11y.aria.GrabbedValues', 'goog.a11y.aria.InvalidValues', 'goog.a11y.aria.LivePriority', 'goog.a11y.aria.OrientationValues', 'goog.a11y.aria.PressedValues', 'goog.a11y.aria.RelevantValues', 'goog.a11y.aria.SelectedValues', 'goog.a11y.aria.SortValues', 'goog.a11y.aria.State'], []);
goog.addDependency('a11y/aria/datatables.js', ['goog.a11y.aria.datatables'], ['goog.a11y.aria.State', 'goog.object']);
goog.addDependency('a11y/aria/roles.js', ['goog.a11y.aria.Role'], []);
goog.addDependency('array/array.js', ['goog.array', 'goog.array.ArrayLike'], ['goog.asserts']);
goog.addDependency('array/array_test.js', ['goog.arrayTest'], ['goog.array', 'goog.dom', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('asserts/asserts.js', ['goog.asserts', 'goog.asserts.AssertionError'], ['goog.debug.Error', 'goog.dom.NodeType', 'goog.string']);
goog.addDependency('asserts/asserts_test.js', ['goog.assertsTest'], ['goog.asserts', 'goog.asserts.AssertionError', 'goog.dom', 'goog.string', 'goog.testing.jsunit']);
goog.addDependency('async/animationdelay.js', ['goog.async.AnimationDelay'], ['goog.Disposable', 'goog.events', 'goog.functions']);
goog.addDependency('async/animationdelay_test.js', ['goog.async.AnimationDelayTest'], ['goog.async.AnimationDelay', 'goog.testing.AsyncTestCase', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('async/conditionaldelay.js', ['goog.async.ConditionalDelay'], ['goog.Disposable', 'goog.async.Delay']);
goog.addDependency('async/conditionaldelay_test.js', ['goog.async.ConditionalDelayTest'], ['goog.async.ConditionalDelay', 'goog.testing.MockClock', 'goog.testing.jsunit']);
goog.addDependency('async/delay.js', ['goog.Delay', 'goog.async.Delay'], ['goog.Disposable', 'goog.Timer']);
goog.addDependency('async/delay_test.js', ['goog.async.DelayTest'], ['goog.async.Delay', 'goog.testing.MockClock', 'goog.testing.jsunit']);
goog.addDependency('async/nexttick.js', ['goog.async.nextTick', 'goog.async.throwException'], ['goog.debug.entryPointRegistry', 'goog.functions']);
goog.addDependency('async/nexttick_test.js', ['goog.async.nextTickTest'], ['goog.async.nextTick', 'goog.debug.ErrorHandler', 'goog.debug.entryPointRegistry', 'goog.testing.AsyncTestCase', 'goog.testing.MockClock', 'goog.testing.jsunit']);
goog.addDependency('async/run.js', ['goog.async.run'], ['goog.async.nextTick', 'goog.async.throwException', 'goog.testing.watchers']);
goog.addDependency('async/run_test.js', ['goog.async.runTest'], ['goog.async.run', 'goog.testing.MockClock', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('async/throttle.js', ['goog.Throttle', 'goog.async.Throttle'], ['goog.Disposable', 'goog.Timer']);
goog.addDependency('async/throttle_test.js', ['goog.async.ThrottleTest'], ['goog.async.Throttle', 'goog.testing.MockClock', 'goog.testing.jsunit']);
goog.addDependency('base.js', ['goog'], []);
goog.addDependency('base_test.js', ['an.existing.path', 'dup.base', 'far.out', 'goog.baseTest', 'goog.explicit', 'goog.implicit.explicit', 'goog.test', 'goog.test.name', 'goog.test.name.space', 'goog.xy', 'goog.xy.z', 'ns', 'testDep.bar'], ['goog.Timer', 'goog.functions', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.testing.recordFunction', 'goog.userAgent']);
goog.addDependency('color/alpha.js', ['goog.color.alpha'], ['goog.color']);
goog.addDependency('color/alpha_test.js', ['goog.color.alphaTest'], ['goog.array', 'goog.color', 'goog.color.alpha', 'goog.testing.jsunit']);
goog.addDependency('color/color.js', ['goog.color', 'goog.color.Hsl', 'goog.color.Hsv', 'goog.color.Rgb'], ['goog.color.names', 'goog.math']);
goog.addDependency('color/color_test.js', ['goog.colorTest'], ['goog.array', 'goog.color', 'goog.color.names', 'goog.testing.jsunit']);
goog.addDependency('color/names.js', ['goog.color.names'], []);
goog.addDependency('crypt/aes.js', ['goog.crypt.Aes'], ['goog.asserts', 'goog.crypt.BlockCipher']);
goog.addDependency('crypt/aes_test.js', ['goog.crypt.AesTest'], ['goog.crypt', 'goog.crypt.Aes', 'goog.testing.jsunit']);
goog.addDependency('crypt/arc4.js', ['goog.crypt.Arc4'], ['goog.asserts']);
goog.addDependency('crypt/arc4_test.js', ['goog.crypt.Arc4Test'], ['goog.array', 'goog.crypt.Arc4', 'goog.testing.jsunit']);
goog.addDependency('crypt/base64.js', ['goog.crypt.base64'], ['goog.crypt', 'goog.userAgent']);
goog.addDependency('crypt/base64_test.js', ['goog.crypt.base64Test'], ['goog.crypt', 'goog.crypt.base64', 'goog.testing.jsunit']);
goog.addDependency('crypt/basen.js', ['goog.crypt.baseN'], []);
goog.addDependency('crypt/basen_test.js', ['goog.crypt.baseNTest'], ['goog.crypt.baseN', 'goog.testing.jsunit']);
goog.addDependency('crypt/blobhasher.js', ['goog.crypt.BlobHasher', 'goog.crypt.BlobHasher.EventType'], ['goog.asserts', 'goog.crypt', 'goog.crypt.Hash', 'goog.events.EventTarget', 'goog.fs', 'goog.log']);
goog.addDependency('crypt/blobhasher_test.js', ['goog.crypt.BlobHasherTest'], ['goog.crypt', 'goog.crypt.BlobHasher', 'goog.crypt.Md5', 'goog.events', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit']);
goog.addDependency('crypt/blockcipher.js', ['goog.crypt.BlockCipher'], []);
goog.addDependency('crypt/bytestring_perf.js', ['goog.crypt.byteArrayToStringPerf'], ['goog.array', 'goog.dom', 'goog.testing.PerformanceTable']);
goog.addDependency('crypt/cbc.js', ['goog.crypt.Cbc'], ['goog.array', 'goog.asserts', 'goog.crypt']);
goog.addDependency('crypt/cbc_test.js', ['goog.crypt.CbcTest'], ['goog.crypt', 'goog.crypt.Aes', 'goog.crypt.Cbc', 'goog.testing.jsunit']);
goog.addDependency('crypt/crypt.js', ['goog.crypt'], ['goog.array', 'goog.asserts']);
goog.addDependency('crypt/crypt_test.js', ['goog.cryptTest'], ['goog.crypt', 'goog.string', 'goog.testing.jsunit']);
goog.addDependency('crypt/hash.js', ['goog.crypt.Hash'], []);
goog.addDependency('crypt/hash32.js', ['goog.crypt.hash32'], ['goog.crypt']);
goog.addDependency('crypt/hash32_test.js', ['goog.crypt.hash32Test'], ['goog.crypt.hash32', 'goog.testing.TestCase', 'goog.testing.jsunit']);
goog.addDependency('crypt/hashtester.js', ['goog.crypt.hashTester'], ['goog.array', 'goog.crypt', 'goog.testing.PerformanceTable', 'goog.testing.PseudoRandom', 'goog.testing.asserts']);
goog.addDependency('crypt/hmac.js', ['goog.crypt.Hmac'], ['goog.crypt.Hash']);
goog.addDependency('crypt/hmac_test.js', ['goog.crypt.HmacTest'], ['goog.crypt.Hmac', 'goog.crypt.Sha1', 'goog.crypt.hashTester', 'goog.testing.jsunit']);
goog.addDependency('crypt/md5.js', ['goog.crypt.Md5'], ['goog.crypt.Hash']);
goog.addDependency('crypt/md5_test.js', ['goog.crypt.Md5Test'], ['goog.crypt', 'goog.crypt.Md5', 'goog.crypt.hashTester', 'goog.testing.jsunit']);
goog.addDependency('crypt/pbkdf2.js', ['goog.crypt.pbkdf2'], ['goog.asserts', 'goog.crypt', 'goog.crypt.Hmac', 'goog.crypt.Sha1']);
goog.addDependency('crypt/pbkdf2_test.js', ['goog.crypt.pbkdf2Test'], ['goog.crypt', 'goog.crypt.pbkdf2', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('crypt/sha1.js', ['goog.crypt.Sha1'], ['goog.crypt.Hash']);
goog.addDependency('crypt/sha1_test.js', ['goog.crypt.Sha1Test'], ['goog.crypt', 'goog.crypt.Sha1', 'goog.crypt.hashTester', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('crypt/sha2.js', ['goog.crypt.Sha2'], ['goog.array', 'goog.asserts', 'goog.crypt.Hash']);
goog.addDependency('crypt/sha224.js', ['goog.crypt.Sha224'], ['goog.crypt.Sha2']);
goog.addDependency('crypt/sha224_test.js', ['goog.crypt.Sha224Test'], ['goog.crypt', 'goog.crypt.Sha224', 'goog.crypt.hashTester', 'goog.testing.jsunit']);
goog.addDependency('crypt/sha256.js', ['goog.crypt.Sha256'], ['goog.crypt.Sha2']);
goog.addDependency('crypt/sha256_test.js', ['goog.crypt.Sha256Test'], ['goog.crypt', 'goog.crypt.Sha256', 'goog.crypt.hashTester', 'goog.testing.jsunit']);
goog.addDependency('crypt/sha2_64bit.js', ['goog.crypt.Sha2_64bit'], ['goog.array', 'goog.asserts', 'goog.crypt.Hash', 'goog.math.Long']);
goog.addDependency('crypt/sha2_64bit_test.js', ['goog.crypt.Sha2_64bit_test'], ['goog.array', 'goog.crypt', 'goog.crypt.Sha384', 'goog.crypt.Sha512', 'goog.crypt.Sha512_256', 'goog.crypt.hashTester', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('crypt/sha384.js', ['goog.crypt.Sha384'], ['goog.crypt.Sha2_64bit']);
goog.addDependency('crypt/sha512.js', ['goog.crypt.Sha512'], ['goog.crypt.Sha2_64bit']);
goog.addDependency('crypt/sha512_256.js', ['goog.crypt.Sha512_256'], ['goog.crypt.Sha2_64bit']);
goog.addDependency('cssom/cssom.js', ['goog.cssom', 'goog.cssom.CssRuleType'], ['goog.array', 'goog.dom']);
goog.addDependency('cssom/cssom_test.js', ['goog.cssomTest'], ['goog.array', 'goog.cssom', 'goog.cssom.CssRuleType', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('cssom/iframe/style.js', ['goog.cssom.iframe.style'], ['goog.asserts', 'goog.cssom', 'goog.dom', 'goog.dom.NodeType', 'goog.dom.TagName', 'goog.dom.classlist', 'goog.string', 'goog.style', 'goog.userAgent']);
goog.addDependency('cssom/iframe/style_test.js', ['goog.cssom.iframe.styleTest'], ['goog.cssom', 'goog.cssom.iframe.style', 'goog.dom', 'goog.dom.DomHelper', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('datasource/datamanager.js', ['goog.ds.DataManager'], ['goog.ds.BasicNodeList', 'goog.ds.DataNode', 'goog.ds.Expr', 'goog.object', 'goog.string', 'goog.structs', 'goog.structs.Map']);
goog.addDependency('datasource/datasource.js', ['goog.ds.BaseDataNode', 'goog.ds.BasicNodeList', 'goog.ds.DataNode', 'goog.ds.DataNodeList', 'goog.ds.EmptyNodeList', 'goog.ds.LoadState', 'goog.ds.SortedNodeList', 'goog.ds.Util', 'goog.ds.logger'], ['goog.array', 'goog.log']);
goog.addDependency('datasource/datasource_test.js', ['goog.ds.JsDataSourceTest'], ['goog.dom.xml', 'goog.ds.DataManager', 'goog.ds.JsDataSource', 'goog.ds.SortedNodeList', 'goog.ds.XmlDataSource', 'goog.testing.jsunit']);
goog.addDependency('datasource/expr.js', ['goog.ds.Expr'], ['goog.ds.BasicNodeList', 'goog.ds.EmptyNodeList', 'goog.string']);
goog.addDependency('datasource/expr_test.js', ['goog.ds.ExprTest'], ['goog.ds.DataManager', 'goog.ds.Expr', 'goog.ds.JsDataSource', 'goog.testing.jsunit']);
goog.addDependency('datasource/fastdatanode.js', ['goog.ds.AbstractFastDataNode', 'goog.ds.FastDataNode', 'goog.ds.FastListNode', 'goog.ds.PrimitiveFastDataNode'], ['goog.ds.DataManager', 'goog.ds.EmptyNodeList', 'goog.string']);
goog.addDependency('datasource/fastdatanode_test.js', ['goog.ds.FastDataNodeTest'], ['goog.array', 'goog.ds.DataManager', 'goog.ds.Expr', 'goog.ds.FastDataNode', 'goog.testing.jsunit']);
goog.addDependency('datasource/jsdatasource.js', ['goog.ds.JsDataSource', 'goog.ds.JsPropertyDataSource'], ['goog.ds.BaseDataNode', 'goog.ds.BasicNodeList', 'goog.ds.DataManager', 'goog.ds.EmptyNodeList', 'goog.ds.LoadState']);
goog.addDependency('datasource/jsondatasource.js', ['goog.ds.JsonDataSource'], ['goog.Uri', 'goog.dom', 'goog.ds.DataManager', 'goog.ds.JsDataSource', 'goog.ds.LoadState', 'goog.ds.logger']);
goog.addDependency('datasource/jsxmlhttpdatasource.js', ['goog.ds.JsXmlHttpDataSource'], ['goog.Uri', 'goog.ds.DataManager', 'goog.ds.FastDataNode', 'goog.ds.LoadState', 'goog.ds.logger', 'goog.events', 'goog.log', 'goog.net.EventType', 'goog.net.XhrIo']);
goog.addDependency('datasource/jsxmlhttpdatasource_test.js', ['goog.ds.JsXmlHttpDataSourceTest'], ['goog.ds.JsXmlHttpDataSource', 'goog.testing.TestQueue', 'goog.testing.jsunit', 'goog.testing.net.XhrIo']);
goog.addDependency('datasource/xmldatasource.js', ['goog.ds.XmlDataSource', 'goog.ds.XmlHttpDataSource'], ['goog.Uri', 'goog.dom.NodeType', 'goog.dom.xml', 'goog.ds.BasicNodeList', 'goog.ds.DataManager', 'goog.ds.LoadState', 'goog.ds.logger', 'goog.net.XhrIo', 'goog.string']);
goog.addDependency('date/date.js', ['goog.date', 'goog.date.Date', 'goog.date.DateTime', 'goog.date.Interval', 'goog.date.month', 'goog.date.weekDay'], ['goog.asserts', 'goog.date.DateLike', 'goog.i18n.DateTimeSymbols', 'goog.string']);
goog.addDependency('date/date_test.js', ['goog.dateTest'], ['goog.array', 'goog.date', 'goog.date.Date', 'goog.date.DateTime', 'goog.date.Interval', 'goog.date.month', 'goog.date.weekDay', 'goog.i18n.DateTimeSymbols', 'goog.testing.ExpectedFailures', 'goog.testing.jsunit', 'goog.userAgent', 'goog.userAgent.platform', 'goog.userAgent.product', 'goog.userAgent.product.isVersion']);
goog.addDependency('date/datelike.js', ['goog.date.DateLike'], []);
goog.addDependency('date/daterange.js', ['goog.date.DateRange', 'goog.date.DateRange.Iterator', 'goog.date.DateRange.StandardDateRangeKeys'], ['goog.date.Date', 'goog.date.Interval', 'goog.iter.Iterator', 'goog.iter.StopIteration']);
goog.addDependency('date/daterange_test.js', ['goog.date.DateRangeTest'], ['goog.date.Date', 'goog.date.DateRange', 'goog.date.Interval', 'goog.i18n.DateTimeSymbols', 'goog.testing.jsunit']);
goog.addDependency('date/duration.js', ['goog.date.duration'], ['goog.i18n.DateTimeFormat', 'goog.i18n.MessageFormat']);
goog.addDependency('date/duration_test.js', ['goog.date.durationTest'], ['goog.date.duration', 'goog.i18n.DateTimeFormat', 'goog.i18n.DateTimeSymbols', 'goog.i18n.DateTimeSymbols_bn', 'goog.i18n.DateTimeSymbols_en', 'goog.i18n.DateTimeSymbols_fa', 'goog.testing.jsunit']);
goog.addDependency('date/relative.js', ['goog.date.relative', 'goog.date.relative.TimeDeltaFormatter', 'goog.date.relative.Unit'], ['goog.i18n.DateTimeFormat']);
goog.addDependency('date/relative_test.js', ['goog.date.relativeTest'], ['goog.date.DateTime', 'goog.date.relative', 'goog.i18n.DateTimeFormat', 'goog.testing.jsunit']);
goog.addDependency('date/relativewithplurals.js', ['goog.date.relativeWithPlurals'], ['goog.date.relative', 'goog.date.relative.Unit', 'goog.i18n.MessageFormat']);
goog.addDependency('date/relativewithplurals_test.js', ['goog.date.relativeWithPluralsTest'], ['goog.date.relative', 'goog.date.relativeTest', 'goog.date.relativeWithPlurals', 'goog.i18n.DateTimeFormat', 'goog.i18n.DateTimeSymbols', 'goog.i18n.DateTimeSymbols_bn', 'goog.i18n.DateTimeSymbols_en', 'goog.i18n.DateTimeSymbols_fa', 'goog.i18n.NumberFormatSymbols', 'goog.i18n.NumberFormatSymbols_bn', 'goog.i18n.NumberFormatSymbols_en', 'goog.i18n.NumberFormatSymbols_fa']);
goog.addDependency('date/utcdatetime.js', ['goog.date.UtcDateTime'], ['goog.date', 'goog.date.Date', 'goog.date.DateTime', 'goog.date.Interval']);
goog.addDependency('date/utcdatetime_test.js', ['goog.date.UtcDateTimeTest'], ['goog.date.Interval', 'goog.date.UtcDateTime', 'goog.date.month', 'goog.date.weekDay', 'goog.testing.jsunit']);
goog.addDependency('db/cursor.js', ['goog.db.Cursor'], ['goog.async.Deferred', 'goog.db.Error', 'goog.debug', 'goog.events.EventTarget']);
goog.addDependency('db/db.js', ['goog.db'], ['goog.async.Deferred', 'goog.db.Error', 'goog.db.IndexedDb', 'goog.db.Transaction']);
goog.addDependency('db/db_test.js', ['goog.dbTest'], ['goog.Disposable', 'goog.array', 'goog.async.Deferred', 'goog.async.DeferredList', 'goog.db', 'goog.db.Cursor', 'goog.db.Error', 'goog.db.IndexedDb', 'goog.db.KeyRange', 'goog.db.Transaction', 'goog.events', 'goog.object', 'goog.testing.AsyncTestCase', 'goog.testing.PropertyReplacer', 'goog.testing.asserts', 'goog.testing.jsunit', 'goog.userAgent.product', 'goog.userAgent.product.isVersion']);
goog.addDependency('db/error.js', ['goog.db.Error', 'goog.db.Error.ErrorCode', 'goog.db.Error.ErrorName', 'goog.db.Error.VersionChangeBlockedError'], ['goog.debug.Error']);
goog.addDependency('db/index.js', ['goog.db.Index'], ['goog.async.Deferred', 'goog.db.Cursor', 'goog.db.Error', 'goog.debug']);
goog.addDependency('db/indexeddb.js', ['goog.db.IndexedDb'], ['goog.async.Deferred', 'goog.db.Error', 'goog.db.ObjectStore', 'goog.db.Transaction', 'goog.events.Event', 'goog.events.EventHandler', 'goog.events.EventTarget']);
goog.addDependency('db/keyrange.js', ['goog.db.KeyRange'], []);
goog.addDependency('db/objectstore.js', ['goog.db.ObjectStore'], ['goog.async.Deferred', 'goog.db.Cursor', 'goog.db.Error', 'goog.db.Index', 'goog.debug', 'goog.events']);
goog.addDependency('db/old_db_test.js', ['goog.oldDbTest'], ['goog.async.Deferred', 'goog.db', 'goog.db.Cursor', 'goog.db.Error', 'goog.db.IndexedDb', 'goog.db.KeyRange', 'goog.db.Transaction', 'goog.events', 'goog.testing.AsyncTestCase', 'goog.testing.asserts', 'goog.testing.jsunit', 'goog.userAgent.product', 'goog.userAgent.product.isVersion']);
goog.addDependency('db/transaction.js', ['goog.db.Transaction', 'goog.db.Transaction.TransactionMode'], ['goog.async.Deferred', 'goog.db.Error', 'goog.db.ObjectStore', 'goog.events.EventHandler', 'goog.events.EventTarget']);
goog.addDependency('debug/console.js', ['goog.debug.Console'], ['goog.debug.LogManager', 'goog.debug.Logger.Level', 'goog.debug.TextFormatter']);
goog.addDependency('debug/console_test.js', ['goog.debug.ConsoleTest'], ['goog.debug.Console', 'goog.debug.LogRecord', 'goog.debug.Logger', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('debug/debug.js', ['goog.debug'], ['goog.array', 'goog.string', 'goog.structs.Set', 'goog.userAgent']);
goog.addDependency('debug/debug_test.js', ['goog.debugTest'], ['goog.debug', 'goog.structs.Set', 'goog.testing.jsunit']);
goog.addDependency('debug/debugwindow.js', ['goog.debug.DebugWindow'], ['goog.debug.HtmlFormatter', 'goog.debug.LogManager', 'goog.debug.Logger', 'goog.structs.CircularBuffer', 'goog.userAgent']);
goog.addDependency('debug/debugwindow_test.js', ['goog.debug.DebugWindowTest'], ['goog.debug.DebugWindow', 'goog.testing.jsunit']);
goog.addDependency('debug/devcss/devcss.js', ['goog.debug.DevCss', 'goog.debug.DevCss.UserAgent'], ['goog.asserts', 'goog.cssom', 'goog.dom.classlist', 'goog.events', 'goog.events.EventType', 'goog.string', 'goog.userAgent']);
goog.addDependency('debug/devcss/devcss_test.js', ['goog.debug.DevCssTest'], ['goog.debug.DevCss', 'goog.style', 'goog.testing.jsunit']);
goog.addDependency('debug/devcss/devcssrunner.js', ['goog.debug.devCssRunner'], ['goog.debug.DevCss']);
goog.addDependency('debug/divconsole.js', ['goog.debug.DivConsole'], ['goog.debug.HtmlFormatter', 'goog.debug.LogManager', 'goog.style']);
goog.addDependency('debug/enhanceerror_test.js', ['goog.debugEnhanceErrorTest'], ['goog.debug', 'goog.testing.jsunit']);
goog.addDependency('debug/entrypointregistry.js', ['goog.debug.EntryPointMonitor', 'goog.debug.entryPointRegistry'], ['goog.asserts']);
goog.addDependency('debug/entrypointregistry_test.js', ['goog.debug.entryPointRegistryTest'], ['goog.debug.ErrorHandler', 'goog.debug.entryPointRegistry', 'goog.testing.jsunit']);
goog.addDependency('debug/error.js', ['goog.debug.Error'], []);
goog.addDependency('debug/error_test.js', ['goog.debug.ErrorTest'], ['goog.debug.Error', 'goog.testing.ExpectedFailures', 'goog.testing.jsunit', 'goog.userAgent', 'goog.userAgent.product']);
goog.addDependency('debug/errorhandler.js', ['goog.debug.ErrorHandler', 'goog.debug.ErrorHandler.ProtectedFunctionError'], ['goog.asserts', 'goog.debug', 'goog.debug.EntryPointMonitor', 'goog.debug.Trace']);
goog.addDependency('debug/errorhandler_async_test.js', ['goog.debug.ErrorHandlerAsyncTest'], ['goog.debug.ErrorHandler', 'goog.testing.AsyncTestCase', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('debug/errorhandler_test.js', ['goog.debug.ErrorHandlerTest'], ['goog.debug.ErrorHandler', 'goog.testing.MockControl', 'goog.testing.jsunit']);
goog.addDependency('debug/errorhandlerweakdep.js', ['goog.debug.errorHandlerWeakDep'], []);
goog.addDependency('debug/errorreporter.js', ['goog.debug.ErrorReporter', 'goog.debug.ErrorReporter.ExceptionEvent'], ['goog.asserts', 'goog.debug', 'goog.debug.ErrorHandler', 'goog.debug.entryPointRegistry', 'goog.events', 'goog.events.Event', 'goog.events.EventTarget', 'goog.log', 'goog.net.XhrIo', 'goog.object', 'goog.string', 'goog.uri.utils', 'goog.userAgent']);
goog.addDependency('debug/errorreporter_test.js', ['goog.debug.ErrorReporterTest'], ['goog.debug.ErrorReporter', 'goog.events', 'goog.functions', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('debug/fancywindow.js', ['goog.debug.FancyWindow'], ['goog.array', 'goog.debug.DebugWindow', 'goog.debug.LogManager', 'goog.debug.Logger', 'goog.dom.DomHelper', 'goog.dom.safe', 'goog.html.SafeHtml', 'goog.object', 'goog.string', 'goog.userAgent']);
goog.addDependency('debug/formatter.js', ['goog.debug.Formatter', 'goog.debug.HtmlFormatter', 'goog.debug.TextFormatter'], ['goog.debug.RelativeTimeProvider', 'goog.string']);
goog.addDependency('debug/fpsdisplay.js', ['goog.debug.FpsDisplay'], ['goog.asserts', 'goog.async.AnimationDelay', 'goog.ui.Component']);
goog.addDependency('debug/fpsdisplay_test.js', ['goog.debug.FpsDisplayTest'], ['goog.debug.FpsDisplay', 'goog.testing.AsyncTestCase', 'goog.testing.jsunit']);
goog.addDependency('debug/gcdiagnostics.js', ['goog.debug.GcDiagnostics'], ['goog.debug.Trace', 'goog.log', 'goog.userAgent']);
goog.addDependency('debug/logbuffer.js', ['goog.debug.LogBuffer'], ['goog.asserts', 'goog.debug.LogRecord']);
goog.addDependency('debug/logbuffer_test.js', ['goog.debug.LogBufferTest'], ['goog.debug.LogBuffer', 'goog.debug.Logger', 'goog.testing.jsunit']);
goog.addDependency('debug/logger.js', ['goog.debug.LogManager', 'goog.debug.Loggable', 'goog.debug.Logger', 'goog.debug.Logger.Level'], ['goog.array', 'goog.asserts', 'goog.debug', 'goog.debug.LogBuffer', 'goog.debug.LogRecord']);
goog.addDependency('debug/logger_test.js', ['goog.debug.LoggerTest'], ['goog.debug.LogManager', 'goog.debug.Logger', 'goog.testing.jsunit']);
goog.addDependency('debug/logrecord.js', ['goog.debug.LogRecord'], []);
goog.addDependency('debug/logrecordserializer.js', ['goog.debug.logRecordSerializer'], ['goog.debug.LogRecord', 'goog.debug.Logger.Level', 'goog.json', 'goog.object']);
goog.addDependency('debug/logrecordserializer_test.js', ['goog.debug.logRecordSerializerTest'], ['goog.debug.LogRecord', 'goog.debug.Logger', 'goog.debug.logRecordSerializer', 'goog.testing.jsunit']);
goog.addDependency('debug/relativetimeprovider.js', ['goog.debug.RelativeTimeProvider'], []);
goog.addDependency('debug/tracer.js', ['goog.debug.Trace'], ['goog.array', 'goog.iter', 'goog.log', 'goog.structs.Map', 'goog.structs.SimplePool']);
goog.addDependency('debug/tracer_test.js', ['goog.debug.TraceTest'], ['goog.debug.Trace', 'goog.testing.jsunit']);
goog.addDependency('defineclass_test.js', ['goog.defineClassTest'], ['goog.testing.jsunit']);
goog.addDependency('disposable/disposable.js', ['goog.Disposable', 'goog.dispose', 'goog.disposeAll'], ['goog.disposable.IDisposable']);
goog.addDependency('disposable/disposable_test.js', ['goog.DisposableTest'], ['goog.Disposable', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('disposable/idisposable.js', ['goog.disposable.IDisposable'], []);
goog.addDependency('dom/abstractmultirange.js', ['goog.dom.AbstractMultiRange'], ['goog.array', 'goog.dom', 'goog.dom.AbstractRange']);
goog.addDependency('dom/abstractrange.js', ['goog.dom.AbstractRange', 'goog.dom.RangeIterator', 'goog.dom.RangeType'], ['goog.dom', 'goog.dom.NodeType', 'goog.dom.SavedCaretRange', 'goog.dom.TagIterator', 'goog.userAgent']);
goog.addDependency('dom/abstractrange_test.js', ['goog.dom.AbstractRangeTest'], ['goog.dom', 'goog.dom.AbstractRange', 'goog.dom.Range', 'goog.testing.jsunit']);
goog.addDependency('dom/animationframe/animationframe.js', ['goog.dom.animationFrame', 'goog.dom.animationFrame.Spec', 'goog.dom.animationFrame.State'], ['goog.dom.animationFrame.polyfill']);
goog.addDependency('dom/animationframe/polyfill.js', ['goog.dom.animationFrame.polyfill'], []);
goog.addDependency('dom/annotate.js', ['goog.dom.annotate', 'goog.dom.annotate.AnnotateFn'], ['goog.array', 'goog.asserts', 'goog.dom', 'goog.dom.NodeType', 'goog.dom.safe', 'goog.html.SafeHtml']);
goog.addDependency('dom/annotate_test.js', ['goog.dom.annotateTest'], ['goog.dom', 'goog.dom.annotate', 'goog.html.SafeHtml', 'goog.testing.jsunit']);
goog.addDependency('dom/browserfeature.js', ['goog.dom.BrowserFeature'], ['goog.userAgent']);
goog.addDependency('dom/browserrange/abstractrange.js', ['goog.dom.browserrange.AbstractRange'], ['goog.array', 'goog.asserts', 'goog.dom', 'goog.dom.NodeType', 'goog.dom.RangeEndpoint', 'goog.dom.TagName', 'goog.dom.TextRangeIterator', 'goog.iter', 'goog.math.Coordinate', 'goog.string', 'goog.string.StringBuffer', 'goog.userAgent']);
goog.addDependency('dom/browserrange/browserrange.js', ['goog.dom.browserrange', 'goog.dom.browserrange.Error'], ['goog.dom', 'goog.dom.NodeType', 'goog.dom.browserrange.GeckoRange', 'goog.dom.browserrange.IeRange', 'goog.dom.browserrange.OperaRange', 'goog.dom.browserrange.W3cRange', 'goog.dom.browserrange.WebKitRange', 'goog.userAgent']);
goog.addDependency('dom/browserrange/browserrange_test.js', ['goog.dom.browserrangeTest'], ['goog.dom', 'goog.dom.NodeType', 'goog.dom.Range', 'goog.dom.RangeEndpoint', 'goog.dom.TagName', 'goog.dom.browserrange', 'goog.testing.dom', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('dom/browserrange/geckorange.js', ['goog.dom.browserrange.GeckoRange'], ['goog.dom.browserrange.W3cRange']);
goog.addDependency('dom/browserrange/ierange.js', ['goog.dom.browserrange.IeRange'], ['goog.array', 'goog.dom', 'goog.dom.NodeType', 'goog.dom.RangeEndpoint', 'goog.dom.TagName', 'goog.dom.browserrange.AbstractRange', 'goog.log', 'goog.string']);
goog.addDependency('dom/browserrange/operarange.js', ['goog.dom.browserrange.OperaRange'], ['goog.dom.browserrange.W3cRange']);
goog.addDependency('dom/browserrange/w3crange.js', ['goog.dom.browserrange.W3cRange'], ['goog.dom', 'goog.dom.NodeType', 'goog.dom.RangeEndpoint', 'goog.dom.browserrange.AbstractRange', 'goog.string']);
goog.addDependency('dom/browserrange/webkitrange.js', ['goog.dom.browserrange.WebKitRange'], ['goog.dom.RangeEndpoint', 'goog.dom.browserrange.W3cRange', 'goog.userAgent']);
goog.addDependency('dom/bufferedviewportsizemonitor.js', ['goog.dom.BufferedViewportSizeMonitor'], ['goog.asserts', 'goog.async.Delay', 'goog.events', 'goog.events.EventTarget', 'goog.events.EventType']);
goog.addDependency('dom/bufferedviewportsizemonitor_test.js', ['goog.dom.BufferedViewportSizeMonitorTest'], ['goog.dom.BufferedViewportSizeMonitor', 'goog.dom.ViewportSizeMonitor', 'goog.events', 'goog.events.EventType', 'goog.math.Size', 'goog.testing.MockClock', 'goog.testing.events', 'goog.testing.events.Event', 'goog.testing.jsunit']);
goog.addDependency('dom/classes.js', ['goog.dom.classes'], ['goog.array']);
goog.addDependency('dom/classes_test.js', ['goog.dom.classes_test'], ['goog.dom', 'goog.dom.classes', 'goog.testing.jsunit']);
goog.addDependency('dom/classlist.js', ['goog.dom.classlist'], ['goog.array']);
goog.addDependency('dom/classlist_test.js', ['goog.dom.classlist_test'], ['goog.dom', 'goog.dom.classlist', 'goog.testing.ExpectedFailures', 'goog.testing.jsunit', 'goog.userAgent.product']);
goog.addDependency('dom/controlrange.js', ['goog.dom.ControlRange', 'goog.dom.ControlRangeIterator'], ['goog.array', 'goog.dom', 'goog.dom.AbstractMultiRange', 'goog.dom.AbstractRange', 'goog.dom.RangeIterator', 'goog.dom.RangeType', 'goog.dom.SavedRange', 'goog.dom.TagWalkType', 'goog.dom.TextRange', 'goog.iter.StopIteration', 'goog.userAgent']);
goog.addDependency('dom/controlrange_test.js', ['goog.dom.ControlRangeTest'], ['goog.dom', 'goog.dom.ControlRange', 'goog.dom.RangeType', 'goog.dom.TagName', 'goog.dom.TextRange', 'goog.testing.dom', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('dom/dataset.js', ['goog.dom.dataset'], ['goog.string']);
goog.addDependency('dom/dataset_test.js', ['goog.dom.datasetTest'], ['goog.dom', 'goog.dom.dataset', 'goog.testing.jsunit']);
goog.addDependency('dom/dom.js', ['goog.dom', 'goog.dom.Appendable', 'goog.dom.DomHelper'], ['goog.array', 'goog.asserts', 'goog.dom.BrowserFeature', 'goog.dom.NodeType', 'goog.dom.TagName', 'goog.dom.classes', 'goog.functions', 'goog.math.Coordinate', 'goog.math.Size', 'goog.object', 'goog.string', 'goog.userAgent']);
goog.addDependency('dom/dom_test.js', ['goog.dom.dom_test'], ['goog.dom', 'goog.dom.BrowserFeature', 'goog.dom.DomHelper', 'goog.dom.NodeType', 'goog.dom.TagName', 'goog.functions', 'goog.object', 'goog.string.Unicode', 'goog.testing.PropertyReplacer', 'goog.testing.asserts', 'goog.userAgent', 'goog.userAgent.product', 'goog.userAgent.product.isVersion']);
goog.addDependency('dom/fontsizemonitor.js', ['goog.dom.FontSizeMonitor', 'goog.dom.FontSizeMonitor.EventType'], ['goog.dom', 'goog.events', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.userAgent']);
goog.addDependency('dom/fontsizemonitor_test.js', ['goog.dom.FontSizeMonitorTest'], ['goog.dom', 'goog.dom.FontSizeMonitor', 'goog.events', 'goog.events.Event', 'goog.testing.PropertyReplacer', 'goog.testing.events', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('dom/forms.js', ['goog.dom.forms'], ['goog.structs.Map']);
goog.addDependency('dom/forms_test.js', ['goog.dom.formsTest'], ['goog.dom', 'goog.dom.forms', 'goog.testing.jsunit']);
goog.addDependency('dom/fullscreen.js', ['goog.dom.fullscreen', 'goog.dom.fullscreen.EventType'], ['goog.dom', 'goog.userAgent']);
goog.addDependency('dom/iframe.js', ['goog.dom.iframe'], ['goog.dom', 'goog.userAgent']);
goog.addDependency('dom/iframe_test.js', ['goog.dom.iframeTest'], ['goog.dom', 'goog.dom.iframe', 'goog.testing.jsunit']);
goog.addDependency('dom/iter.js', ['goog.dom.iter.AncestorIterator', 'goog.dom.iter.ChildIterator', 'goog.dom.iter.SiblingIterator'], ['goog.iter.Iterator', 'goog.iter.StopIteration']);
goog.addDependency('dom/iter_test.js', ['goog.dom.iterTest'], ['goog.dom', 'goog.dom.NodeType', 'goog.dom.iter.AncestorIterator', 'goog.dom.iter.ChildIterator', 'goog.dom.iter.SiblingIterator', 'goog.testing.dom', 'goog.testing.jsunit']);
goog.addDependency('dom/multirange.js', ['goog.dom.MultiRange', 'goog.dom.MultiRangeIterator'], ['goog.array', 'goog.dom.AbstractMultiRange', 'goog.dom.AbstractRange', 'goog.dom.RangeIterator', 'goog.dom.RangeType', 'goog.dom.SavedRange', 'goog.dom.TextRange', 'goog.iter.StopIteration', 'goog.log']);
goog.addDependency('dom/multirange_test.js', ['goog.dom.MultiRangeTest'], ['goog.dom', 'goog.dom.MultiRange', 'goog.dom.Range', 'goog.iter', 'goog.testing.jsunit']);
goog.addDependency('dom/nodeiterator.js', ['goog.dom.NodeIterator'], ['goog.dom.TagIterator']);
goog.addDependency('dom/nodeiterator_test.js', ['goog.dom.NodeIteratorTest'], ['goog.dom', 'goog.dom.NodeIterator', 'goog.testing.dom', 'goog.testing.jsunit']);
goog.addDependency('dom/nodeoffset.js', ['goog.dom.NodeOffset'], ['goog.Disposable', 'goog.dom.TagName']);
goog.addDependency('dom/nodeoffset_test.js', ['goog.dom.NodeOffsetTest'], ['goog.dom', 'goog.dom.NodeOffset', 'goog.dom.NodeType', 'goog.dom.TagName', 'goog.testing.jsunit']);
goog.addDependency('dom/nodetype.js', ['goog.dom.NodeType'], []);
goog.addDependency('dom/pattern/abstractpattern.js', ['goog.dom.pattern.AbstractPattern'], ['goog.dom.pattern.MatchType']);
goog.addDependency('dom/pattern/allchildren.js', ['goog.dom.pattern.AllChildren'], ['goog.dom.pattern.AbstractPattern', 'goog.dom.pattern.MatchType']);
goog.addDependency('dom/pattern/callback/callback.js', ['goog.dom.pattern.callback'], ['goog.dom', 'goog.dom.TagWalkType', 'goog.iter']);
goog.addDependency('dom/pattern/callback/counter.js', ['goog.dom.pattern.callback.Counter'], []);
goog.addDependency('dom/pattern/callback/test.js', ['goog.dom.pattern.callback.Test'], ['goog.iter.StopIteration']);
goog.addDependency('dom/pattern/childmatches.js', ['goog.dom.pattern.ChildMatches'], ['goog.dom.pattern.AllChildren', 'goog.dom.pattern.MatchType']);
goog.addDependency('dom/pattern/endtag.js', ['goog.dom.pattern.EndTag'], ['goog.dom.TagWalkType', 'goog.dom.pattern.Tag']);
goog.addDependency('dom/pattern/fulltag.js', ['goog.dom.pattern.FullTag'], ['goog.dom.pattern.MatchType', 'goog.dom.pattern.StartTag', 'goog.dom.pattern.Tag']);
goog.addDependency('dom/pattern/matcher.js', ['goog.dom.pattern.Matcher'], ['goog.dom.TagIterator', 'goog.dom.pattern.MatchType', 'goog.iter']);
goog.addDependency('dom/pattern/matcher_test.js', ['goog.dom.pattern.matcherTest'], ['goog.dom', 'goog.dom.pattern.EndTag', 'goog.dom.pattern.FullTag', 'goog.dom.pattern.Matcher', 'goog.dom.pattern.Repeat', 'goog.dom.pattern.Sequence', 'goog.dom.pattern.StartTag', 'goog.dom.pattern.callback.Counter', 'goog.dom.pattern.callback.Test', 'goog.iter.StopIteration', 'goog.testing.jsunit']);
goog.addDependency('dom/pattern/nodetype.js', ['goog.dom.pattern.NodeType'], ['goog.dom.pattern.AbstractPattern', 'goog.dom.pattern.MatchType']);
goog.addDependency('dom/pattern/pattern.js', ['goog.dom.pattern', 'goog.dom.pattern.MatchType'], []);
goog.addDependency('dom/pattern/pattern_test.js', ['goog.dom.patternTest'], ['goog.dom', 'goog.dom.NodeType', 'goog.dom.TagWalkType', 'goog.dom.pattern.AllChildren', 'goog.dom.pattern.ChildMatches', 'goog.dom.pattern.EndTag', 'goog.dom.pattern.FullTag', 'goog.dom.pattern.MatchType', 'goog.dom.pattern.NodeType', 'goog.dom.pattern.Repeat', 'goog.dom.pattern.Sequence', 'goog.dom.pattern.StartTag', 'goog.dom.pattern.Text', 'goog.testing.jsunit']);
goog.addDependency('dom/pattern/repeat.js', ['goog.dom.pattern.Repeat'], ['goog.dom.NodeType', 'goog.dom.pattern.AbstractPattern', 'goog.dom.pattern.MatchType']);
goog.addDependency('dom/pattern/sequence.js', ['goog.dom.pattern.Sequence'], ['goog.dom.NodeType', 'goog.dom.pattern.AbstractPattern', 'goog.dom.pattern.MatchType']);
goog.addDependency('dom/pattern/starttag.js', ['goog.dom.pattern.StartTag'], ['goog.dom.TagWalkType', 'goog.dom.pattern.Tag']);
goog.addDependency('dom/pattern/tag.js', ['goog.dom.pattern.Tag'], ['goog.dom.pattern', 'goog.dom.pattern.AbstractPattern', 'goog.dom.pattern.MatchType', 'goog.object']);
goog.addDependency('dom/pattern/text.js', ['goog.dom.pattern.Text'], ['goog.dom.NodeType', 'goog.dom.pattern', 'goog.dom.pattern.AbstractPattern', 'goog.dom.pattern.MatchType']);
goog.addDependency('dom/range.js', ['goog.dom.Range'], ['goog.dom', 'goog.dom.AbstractRange', 'goog.dom.ControlRange', 'goog.dom.MultiRange', 'goog.dom.NodeType', 'goog.dom.TextRange', 'goog.userAgent']);
goog.addDependency('dom/range_test.js', ['goog.dom.RangeTest'], ['goog.dom', 'goog.dom.NodeType', 'goog.dom.Range', 'goog.dom.RangeType', 'goog.dom.TagName', 'goog.dom.TextRange', 'goog.dom.browserrange', 'goog.testing.dom', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('dom/rangeendpoint.js', ['goog.dom.RangeEndpoint'], []);
goog.addDependency('dom/safe.js', ['goog.dom.safe'], ['goog.html.SafeHtml', 'goog.html.SafeUrl']);
goog.addDependency('dom/safe_test.js', ['goog.dom.safeTest'], ['goog.dom.safe', 'goog.html.SafeUrl', 'goog.html.testing', 'goog.string.Const', 'goog.testing.jsunit']);
goog.addDependency('dom/savedcaretrange.js', ['goog.dom.SavedCaretRange'], ['goog.array', 'goog.dom', 'goog.dom.SavedRange', 'goog.dom.TagName', 'goog.string']);
goog.addDependency('dom/savedcaretrange_test.js', ['goog.dom.SavedCaretRangeTest'], ['goog.dom', 'goog.dom.Range', 'goog.dom.SavedCaretRange', 'goog.testing.dom', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('dom/savedrange.js', ['goog.dom.SavedRange'], ['goog.Disposable', 'goog.log']);
goog.addDependency('dom/savedrange_test.js', ['goog.dom.SavedRangeTest'], ['goog.dom', 'goog.dom.Range', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('dom/selection.js', ['goog.dom.selection'], ['goog.string', 'goog.userAgent']);
goog.addDependency('dom/selection_test.js', ['goog.dom.selectionTest'], ['goog.dom', 'goog.dom.selection', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('dom/tagiterator.js', ['goog.dom.TagIterator', 'goog.dom.TagWalkType'], ['goog.dom', 'goog.dom.NodeType', 'goog.iter.Iterator', 'goog.iter.StopIteration']);
goog.addDependency('dom/tagiterator_test.js', ['goog.dom.TagIteratorTest'], ['goog.dom', 'goog.dom.TagIterator', 'goog.dom.TagWalkType', 'goog.iter', 'goog.iter.StopIteration', 'goog.testing.dom', 'goog.testing.jsunit']);
goog.addDependency('dom/tagname.js', ['goog.dom.TagName'], []);
goog.addDependency('dom/tagname_test.js', ['goog.dom.TagNameTest'], ['goog.dom.TagName', 'goog.object', 'goog.testing.jsunit']);
goog.addDependency('dom/tags.js', ['goog.dom.tags'], ['goog.object']);
goog.addDependency('dom/tags_test.js', ['goog.dom.tagsTest'], ['goog.dom.tags', 'goog.testing.jsunit']);
goog.addDependency('dom/textrange.js', ['goog.dom.TextRange'], ['goog.array', 'goog.dom', 'goog.dom.AbstractRange', 'goog.dom.RangeType', 'goog.dom.SavedRange', 'goog.dom.TagName', 'goog.dom.TextRangeIterator', 'goog.dom.browserrange', 'goog.string', 'goog.userAgent']);
goog.addDependency('dom/textrange_test.js', ['goog.dom.TextRangeTest'], ['goog.dom', 'goog.dom.ControlRange', 'goog.dom.Range', 'goog.dom.TextRange', 'goog.math.Coordinate', 'goog.style', 'goog.testing.ExpectedFailures', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('dom/textrangeiterator.js', ['goog.dom.TextRangeIterator'], ['goog.array', 'goog.dom.NodeType', 'goog.dom.RangeIterator', 'goog.dom.TagName', 'goog.iter.StopIteration']);
goog.addDependency('dom/textrangeiterator_test.js', ['goog.dom.TextRangeIteratorTest'], ['goog.dom', 'goog.dom.TagName', 'goog.dom.TextRangeIterator', 'goog.iter.StopIteration', 'goog.testing.dom', 'goog.testing.jsunit']);
goog.addDependency('dom/vendor.js', ['goog.dom.vendor'], ['goog.string', 'goog.userAgent']);
goog.addDependency('dom/vendor_test.js', ['goog.dom.vendorTest'], ['goog.array', 'goog.dom.vendor', 'goog.labs.userAgent.util', 'goog.testing.MockUserAgent', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.userAgent', 'goog.userAgentTestUtil']);
goog.addDependency('dom/viewportsizemonitor.js', ['goog.dom.ViewportSizeMonitor'], ['goog.dom', 'goog.events', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.math.Size']);
goog.addDependency('dom/viewportsizemonitor_test.js', ['goog.dom.ViewportSizeMonitorTest'], ['goog.dom.ViewportSizeMonitor', 'goog.events', 'goog.events.Event', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.math.Size', 'goog.testing.MockClock', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit']);
goog.addDependency('dom/xml.js', ['goog.dom.xml'], ['goog.dom', 'goog.dom.NodeType']);
goog.addDependency('dom/xml_test.js', ['goog.dom.xmlTest'], ['goog.dom.xml', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('editor/browserfeature.js', ['goog.editor.BrowserFeature'], ['goog.editor.defines', 'goog.userAgent', 'goog.userAgent.product', 'goog.userAgent.product.isVersion']);
goog.addDependency('editor/browserfeature_test.js', ['goog.editor.BrowserFeatureTest'], ['goog.dom', 'goog.dom.Range', 'goog.editor.BrowserFeature', 'goog.testing.ExpectedFailures', 'goog.testing.jsunit']);
goog.addDependency('editor/clicktoeditwrapper.js', ['goog.editor.ClickToEditWrapper'], ['goog.Disposable', 'goog.asserts', 'goog.dom', 'goog.dom.Range', 'goog.dom.TagName', 'goog.editor.BrowserFeature', 'goog.editor.Command', 'goog.editor.Field.EventType', 'goog.editor.range', 'goog.events.BrowserEvent.MouseButton', 'goog.events.EventHandler', 'goog.events.EventType', 'goog.log']);
goog.addDependency('editor/clicktoeditwrapper_test.js', ['goog.editor.ClickToEditWrapperTest'], ['goog.dom', 'goog.dom.Range', 'goog.editor.ClickToEditWrapper', 'goog.editor.SeamlessField', 'goog.testing.MockClock', 'goog.testing.events', 'goog.testing.jsunit', 'goog.userAgent', 'goog.userAgent.product']);
goog.addDependency('editor/command.js', ['goog.editor.Command'], []);
goog.addDependency('editor/contenteditablefield.js', ['goog.editor.ContentEditableField'], ['goog.asserts', 'goog.editor.Field', 'goog.log']);
goog.addDependency('editor/contenteditablefield_test.js', ['goog.editor.ContentEditableFieldTest'], ['goog.dom', 'goog.editor.ContentEditableField', 'goog.editor.field_test', 'goog.testing.jsunit']);
goog.addDependency('editor/defines.js', ['goog.editor.defines'], []);
goog.addDependency('editor/field.js', ['goog.editor.Field', 'goog.editor.Field.EventType'], ['goog.a11y.aria', 'goog.a11y.aria.Role', 'goog.array', 'goog.asserts', 'goog.async.Delay', 'goog.dom', 'goog.dom.Range', 'goog.dom.TagName', 'goog.editor.BrowserFeature', 'goog.editor.Command', 'goog.editor.Plugin', 'goog.editor.icontent', 'goog.editor.icontent.FieldFormatInfo', 'goog.editor.icontent.FieldStyleInfo', 'goog.editor.node', 'goog.editor.range', 'goog.events', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.functions', 'goog.log', 'goog.log.Level', 'goog.string', 'goog.string.Unicode', 'goog.style', 'goog.userAgent', 'goog.userAgent.product']);
goog.addDependency('editor/field_test.js', ['goog.editor.field_test'], ['goog.dom', 'goog.dom.Range', 'goog.editor.BrowserFeature', 'goog.editor.Field', 'goog.editor.Plugin', 'goog.editor.range', 'goog.events', 'goog.events.BrowserEvent', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.functions', 'goog.testing.LooseMock', 'goog.testing.MockClock', 'goog.testing.dom', 'goog.testing.events', 'goog.testing.events.Event', 'goog.testing.recordFunction', 'goog.userAgent']);
goog.addDependency('editor/focus.js', ['goog.editor.focus'], ['goog.dom.selection']);
goog.addDependency('editor/focus_test.js', ['goog.editor.focusTest'], ['goog.dom.selection', 'goog.editor.BrowserFeature', 'goog.editor.focus', 'goog.testing.jsunit']);
goog.addDependency('editor/icontent.js', ['goog.editor.icontent', 'goog.editor.icontent.FieldFormatInfo', 'goog.editor.icontent.FieldStyleInfo'], ['goog.editor.BrowserFeature', 'goog.style', 'goog.userAgent']);
goog.addDependency('editor/icontent_test.js', ['goog.editor.icontentTest'], ['goog.dom', 'goog.editor.BrowserFeature', 'goog.editor.icontent', 'goog.editor.icontent.FieldFormatInfo', 'goog.editor.icontent.FieldStyleInfo', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('editor/link.js', ['goog.editor.Link'], ['goog.array', 'goog.dom', 'goog.dom.NodeType', 'goog.dom.Range', 'goog.dom.TagName', 'goog.editor.BrowserFeature', 'goog.editor.Command', 'goog.editor.node', 'goog.editor.range', 'goog.string', 'goog.string.Unicode', 'goog.uri.utils', 'goog.uri.utils.ComponentIndex']);
goog.addDependency('editor/link_test.js', ['goog.editor.LinkTest'], ['goog.dom', 'goog.dom.NodeType', 'goog.dom.Range', 'goog.dom.TagName', 'goog.editor.Link', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('editor/node.js', ['goog.editor.node'], ['goog.dom', 'goog.dom.NodeType', 'goog.dom.TagName', 'goog.dom.iter.ChildIterator', 'goog.dom.iter.SiblingIterator', 'goog.iter', 'goog.object', 'goog.string', 'goog.string.Unicode', 'goog.userAgent']);
goog.addDependency('editor/node_test.js', ['goog.editor.nodeTest'], ['goog.array', 'goog.dom', 'goog.dom.NodeType', 'goog.dom.TagName', 'goog.editor.node', 'goog.style', 'goog.testing.ExpectedFailures', 'goog.testing.dom', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('editor/plugin.js', ['goog.editor.Plugin'], ['goog.editor.Command', 'goog.events.EventTarget', 'goog.functions', 'goog.log', 'goog.object', 'goog.reflect']);
goog.addDependency('editor/plugin_test.js', ['goog.editor.PluginTest'], ['goog.editor.Field', 'goog.editor.Plugin', 'goog.functions', 'goog.testing.StrictMock', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('editor/plugins/abstractbubbleplugin.js', ['goog.editor.plugins.AbstractBubblePlugin'], ['goog.dom', 'goog.dom.NodeType', 'goog.dom.Range', 'goog.dom.TagName', 'goog.editor.Plugin', 'goog.editor.style', 'goog.events', 'goog.events.EventHandler', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.events.actionEventWrapper', 'goog.functions', 'goog.string.Unicode', 'goog.ui.Component', 'goog.ui.editor.Bubble', 'goog.userAgent']);
goog.addDependency('editor/plugins/abstractbubbleplugin_test.js', ['goog.editor.plugins.AbstractBubblePluginTest'], ['goog.dom', 'goog.editor.plugins.AbstractBubblePlugin', 'goog.events.BrowserEvent', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.functions', 'goog.style', 'goog.testing.editor.FieldMock', 'goog.testing.editor.TestHelper', 'goog.testing.events', 'goog.testing.events.Event', 'goog.testing.jsunit', 'goog.ui.editor.Bubble', 'goog.userAgent']);
goog.addDependency('editor/plugins/abstractdialogplugin.js', ['goog.editor.plugins.AbstractDialogPlugin', 'goog.editor.plugins.AbstractDialogPlugin.EventType'], ['goog.dom', 'goog.dom.Range', 'goog.editor.Field.EventType', 'goog.editor.Plugin', 'goog.editor.range', 'goog.events', 'goog.ui.editor.AbstractDialog.EventType']);
goog.addDependency('editor/plugins/abstractdialogplugin_test.js', ['goog.editor.plugins.AbstractDialogPluginTest'], ['goog.dom.SavedRange', 'goog.editor.Field', 'goog.editor.plugins.AbstractDialogPlugin', 'goog.events.Event', 'goog.events.EventHandler', 'goog.functions', 'goog.testing.MockClock', 'goog.testing.MockControl', 'goog.testing.PropertyReplacer', 'goog.testing.editor.FieldMock', 'goog.testing.editor.TestHelper', 'goog.testing.events', 'goog.testing.jsunit', 'goog.testing.mockmatchers.ArgumentMatcher', 'goog.ui.editor.AbstractDialog', 'goog.userAgent']);
goog.addDependency('editor/plugins/abstracttabhandler.js', ['goog.editor.plugins.AbstractTabHandler'], ['goog.editor.Plugin', 'goog.events.KeyCodes']);
goog.addDependency('editor/plugins/abstracttabhandler_test.js', ['goog.editor.plugins.AbstractTabHandlerTest'], ['goog.editor.Field', 'goog.editor.plugins.AbstractTabHandler', 'goog.events.BrowserEvent', 'goog.events.KeyCodes', 'goog.testing.StrictMock', 'goog.testing.editor.FieldMock', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('editor/plugins/basictextformatter.js', ['goog.editor.plugins.BasicTextFormatter', 'goog.editor.plugins.BasicTextFormatter.COMMAND'], ['goog.array', 'goog.dom', 'goog.dom.NodeType', 'goog.dom.Range', 'goog.dom.TagName', 'goog.editor.BrowserFeature', 'goog.editor.Command', 'goog.editor.Link', 'goog.editor.Plugin', 'goog.editor.node', 'goog.editor.range', 'goog.editor.style', 'goog.iter', 'goog.iter.StopIteration', 'goog.log', 'goog.object', 'goog.string', 'goog.string.Unicode', 'goog.style', 'goog.ui.editor.messages', 'goog.userAgent']);
goog.addDependency('editor/plugins/basictextformatter_test.js', ['goog.editor.plugins.BasicTextFormatterTest'], ['goog.dom', 'goog.dom.Range', 'goog.dom.TagName', 'goog.editor.BrowserFeature', 'goog.editor.Command', 'goog.editor.Field', 'goog.editor.Plugin', 'goog.editor.plugins.BasicTextFormatter', 'goog.object', 'goog.style', 'goog.testing.ExpectedFailures', 'goog.testing.LooseMock', 'goog.testing.PropertyReplacer', 'goog.testing.editor.FieldMock', 'goog.testing.editor.TestHelper', 'goog.testing.jsunit', 'goog.testing.mockmatchers', 'goog.userAgent']);
goog.addDependency('editor/plugins/blockquote.js', ['goog.editor.plugins.Blockquote'], ['goog.dom', 'goog.dom.NodeType', 'goog.dom.TagName', 'goog.dom.classlist', 'goog.editor.BrowserFeature', 'goog.editor.Command', 'goog.editor.Plugin', 'goog.editor.node', 'goog.functions', 'goog.log']);
goog.addDependency('editor/plugins/blockquote_test.js', ['goog.editor.plugins.BlockquoteTest'], ['goog.dom', 'goog.dom.Range', 'goog.dom.TagName', 'goog.editor.plugins.Blockquote', 'goog.testing.editor.FieldMock', 'goog.testing.editor.TestHelper', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('editor/plugins/emoticons.js', ['goog.editor.plugins.Emoticons'], ['goog.dom.TagName', 'goog.editor.Plugin', 'goog.editor.range', 'goog.functions', 'goog.ui.emoji.Emoji', 'goog.userAgent']);
goog.addDependency('editor/plugins/emoticons_test.js', ['goog.editor.plugins.EmoticonsTest'], ['goog.Uri', 'goog.array', 'goog.dom', 'goog.dom.TagName', 'goog.editor.Field', 'goog.editor.plugins.Emoticons', 'goog.testing.jsunit', 'goog.ui.emoji.Emoji', 'goog.userAgent']);
goog.addDependency('editor/plugins/enterhandler.js', ['goog.editor.plugins.EnterHandler'], ['goog.dom', 'goog.dom.NodeOffset', 'goog.dom.NodeType', 'goog.dom.Range', 'goog.dom.TagName', 'goog.editor.BrowserFeature', 'goog.editor.Plugin', 'goog.editor.node', 'goog.editor.plugins.Blockquote', 'goog.editor.range', 'goog.editor.style', 'goog.events.KeyCodes', 'goog.functions', 'goog.object', 'goog.string', 'goog.userAgent']);
goog.addDependency('editor/plugins/enterhandler_test.js', ['goog.editor.plugins.EnterHandlerTest'], ['goog.dom', 'goog.dom.NodeType', 'goog.dom.Range', 'goog.dom.TagName', 'goog.editor.BrowserFeature', 'goog.editor.Field', 'goog.editor.Plugin', 'goog.editor.plugins.Blockquote', 'goog.editor.plugins.EnterHandler', 'goog.editor.range', 'goog.events', 'goog.events.KeyCodes', 'goog.testing.ExpectedFailures', 'goog.testing.MockClock', 'goog.testing.dom', 'goog.testing.editor.TestHelper', 'goog.testing.events', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('editor/plugins/equationeditorbubble.js', ['goog.editor.plugins.equation.EquationBubble'], ['goog.dom', 'goog.dom.TagName', 'goog.editor.Command', 'goog.editor.plugins.AbstractBubblePlugin', 'goog.string.Unicode', 'goog.ui.editor.Bubble', 'goog.ui.equation.ImageRenderer']);
goog.addDependency('editor/plugins/equationeditorplugin.js', ['goog.editor.plugins.EquationEditorPlugin'], ['goog.dom', 'goog.editor.Command', 'goog.editor.plugins.AbstractDialogPlugin', 'goog.editor.range', 'goog.events', 'goog.events.EventType', 'goog.functions', 'goog.log', 'goog.ui.editor.AbstractDialog', 'goog.ui.editor.EquationEditorDialog', 'goog.ui.equation.ImageRenderer', 'goog.ui.equation.PaletteManager']);
goog.addDependency('editor/plugins/equationeditorplugin_test.js', ['goog.editor.plugins.EquationEditorPluginTest'], ['goog.dom', 'goog.dom.DomHelper', 'goog.dom.TagName', 'goog.editor.plugins.EquationEditorPlugin', 'goog.testing.MockControl', 'goog.testing.MockRange', 'goog.testing.PropertyReplacer', 'goog.testing.editor.FieldMock', 'goog.testing.jsunit', 'goog.testing.mockmatchers.ArgumentMatcher', 'goog.ui.editor.EquationEditorOkEvent']);
goog.addDependency('editor/plugins/firststrong.js', ['goog.editor.plugins.FirstStrong'], ['goog.dom.NodeType', 'goog.dom.TagIterator', 'goog.dom.TagName', 'goog.editor.Command', 'goog.editor.Plugin', 'goog.editor.node', 'goog.editor.range', 'goog.i18n.bidi', 'goog.i18n.uChar', 'goog.iter', 'goog.userAgent']);
goog.addDependency('editor/plugins/firststrong_test.js', ['goog.editor.plugins.FirstStrongTest'], ['goog.dom.Range', 'goog.editor.Command', 'goog.editor.Field', 'goog.editor.plugins.FirstStrong', 'goog.editor.range', 'goog.events.KeyCodes', 'goog.testing.editor.TestHelper', 'goog.testing.events', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('editor/plugins/headerformatter.js', ['goog.editor.plugins.HeaderFormatter'], ['goog.editor.Command', 'goog.editor.Plugin', 'goog.userAgent']);
goog.addDependency('editor/plugins/headerformatter_test.js', ['goog.editor.plugins.HeaderFormatterTest'], ['goog.dom', 'goog.editor.Command', 'goog.editor.plugins.BasicTextFormatter', 'goog.editor.plugins.HeaderFormatter', 'goog.events.BrowserEvent', 'goog.testing.LooseMock', 'goog.testing.editor.FieldMock', 'goog.testing.editor.TestHelper', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('editor/plugins/linkbubble.js', ['goog.editor.plugins.LinkBubble', 'goog.editor.plugins.LinkBubble.Action'], ['goog.array', 'goog.dom', 'goog.dom.TagName', 'goog.editor.Command', 'goog.editor.Link', 'goog.editor.plugins.AbstractBubblePlugin', 'goog.editor.range', 'goog.functions', 'goog.string', 'goog.style', 'goog.ui.editor.messages', 'goog.uri.utils', 'goog.window']);
goog.addDependency('editor/plugins/linkbubble_test.js', ['goog.editor.plugins.LinkBubbleTest'], ['goog.dom', 'goog.dom.Range', 'goog.dom.TagName', 'goog.editor.Command', 'goog.editor.Link', 'goog.editor.plugins.LinkBubble', 'goog.events.BrowserEvent', 'goog.events.Event', 'goog.events.EventType', 'goog.string', 'goog.style', 'goog.testing.FunctionMock', 'goog.testing.PropertyReplacer', 'goog.testing.editor.FieldMock', 'goog.testing.editor.TestHelper', 'goog.testing.events', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('editor/plugins/linkdialogplugin.js', ['goog.editor.plugins.LinkDialogPlugin'], ['goog.array', 'goog.dom', 'goog.editor.Command', 'goog.editor.plugins.AbstractDialogPlugin', 'goog.events.EventHandler', 'goog.functions', 'goog.ui.editor.AbstractDialog.EventType', 'goog.ui.editor.LinkDialog', 'goog.ui.editor.LinkDialog.EventType', 'goog.ui.editor.LinkDialog.OkEvent', 'goog.uri.utils']);
goog.addDependency('editor/plugins/linkdialogplugin_test.js', ['goog.ui.editor.LinkDialogTest'], ['goog.dom', 'goog.dom.DomHelper', 'goog.dom.NodeType', 'goog.dom.TagName', 'goog.editor.BrowserFeature', 'goog.editor.Command', 'goog.editor.Field', 'goog.editor.Link', 'goog.editor.plugins.LinkDialogPlugin', 'goog.string', 'goog.string.Unicode', 'goog.testing.MockControl', 'goog.testing.editor.FieldMock', 'goog.testing.editor.TestHelper', 'goog.testing.editor.dom', 'goog.testing.events', 'goog.testing.jsunit', 'goog.testing.mockmatchers', 'goog.ui.editor.AbstractDialog', 'goog.ui.editor.LinkDialog', 'goog.userAgent']);
goog.addDependency('editor/plugins/linkshortcutplugin.js', ['goog.editor.plugins.LinkShortcutPlugin'], ['goog.editor.Command', 'goog.editor.Link', 'goog.editor.Plugin', 'goog.string']);
goog.addDependency('editor/plugins/linkshortcutplugin_test.js', ['goog.editor.plugins.LinkShortcutPluginTest'], ['goog.dom', 'goog.editor.Field', 'goog.editor.plugins.BasicTextFormatter', 'goog.editor.plugins.LinkBubble', 'goog.editor.plugins.LinkShortcutPlugin', 'goog.events.KeyCodes', 'goog.testing.PropertyReplacer', 'goog.testing.dom', 'goog.testing.events', 'goog.testing.jsunit']);
goog.addDependency('editor/plugins/listtabhandler.js', ['goog.editor.plugins.ListTabHandler'], ['goog.dom.TagName', 'goog.editor.Command', 'goog.editor.plugins.AbstractTabHandler']);
goog.addDependency('editor/plugins/listtabhandler_test.js', ['goog.editor.plugins.ListTabHandlerTest'], ['goog.dom', 'goog.editor.Command', 'goog.editor.plugins.ListTabHandler', 'goog.events.BrowserEvent', 'goog.events.KeyCodes', 'goog.functions', 'goog.testing.StrictMock', 'goog.testing.editor.FieldMock', 'goog.testing.editor.TestHelper', 'goog.testing.jsunit']);
goog.addDependency('editor/plugins/loremipsum.js', ['goog.editor.plugins.LoremIpsum'], ['goog.asserts', 'goog.dom', 'goog.editor.Command', 'goog.editor.Plugin', 'goog.editor.node', 'goog.functions']);
goog.addDependency('editor/plugins/loremipsum_test.js', ['goog.editor.plugins.LoremIpsumTest'], ['goog.dom', 'goog.editor.Command', 'goog.editor.Field', 'goog.editor.plugins.LoremIpsum', 'goog.string.Unicode', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('editor/plugins/removeformatting.js', ['goog.editor.plugins.RemoveFormatting'], ['goog.dom', 'goog.dom.NodeType', 'goog.dom.Range', 'goog.dom.TagName', 'goog.editor.BrowserFeature', 'goog.editor.Plugin', 'goog.editor.node', 'goog.editor.range', 'goog.string', 'goog.userAgent']);
goog.addDependency('editor/plugins/removeformatting_test.js', ['goog.editor.plugins.RemoveFormattingTest'], ['goog.dom', 'goog.dom.Range', 'goog.dom.TagName', 'goog.editor.BrowserFeature', 'goog.editor.plugins.RemoveFormatting', 'goog.string', 'goog.testing.ExpectedFailures', 'goog.testing.dom', 'goog.testing.editor.FieldMock', 'goog.testing.editor.TestHelper', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('editor/plugins/spacestabhandler.js', ['goog.editor.plugins.SpacesTabHandler'], ['goog.dom', 'goog.dom.TagName', 'goog.editor.plugins.AbstractTabHandler', 'goog.editor.range']);
goog.addDependency('editor/plugins/spacestabhandler_test.js', ['goog.editor.plugins.SpacesTabHandlerTest'], ['goog.dom', 'goog.dom.Range', 'goog.editor.plugins.SpacesTabHandler', 'goog.events.BrowserEvent', 'goog.events.KeyCodes', 'goog.functions', 'goog.testing.StrictMock', 'goog.testing.editor.FieldMock', 'goog.testing.editor.TestHelper', 'goog.testing.jsunit']);
goog.addDependency('editor/plugins/tableeditor.js', ['goog.editor.plugins.TableEditor'], ['goog.array', 'goog.dom', 'goog.dom.TagName', 'goog.editor.Plugin', 'goog.editor.Table', 'goog.editor.node', 'goog.editor.range', 'goog.object']);
goog.addDependency('editor/plugins/tableeditor_test.js', ['goog.editor.plugins.TableEditorTest'], ['goog.dom', 'goog.dom.Range', 'goog.editor.plugins.TableEditor', 'goog.object', 'goog.string', 'goog.testing.ExpectedFailures', 'goog.testing.JsUnitException', 'goog.testing.editor.FieldMock', 'goog.testing.editor.TestHelper', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('editor/plugins/tagonenterhandler.js', ['goog.editor.plugins.TagOnEnterHandler'], ['goog.dom', 'goog.dom.NodeType', 'goog.dom.Range', 'goog.dom.TagName', 'goog.editor.Command', 'goog.editor.node', 'goog.editor.plugins.EnterHandler', 'goog.editor.range', 'goog.editor.style', 'goog.events.KeyCodes', 'goog.string', 'goog.style', 'goog.userAgent']);
goog.addDependency('editor/plugins/tagonenterhandler_test.js', ['goog.editor.plugins.TagOnEnterHandlerTest'], ['goog.dom', 'goog.dom.NodeType', 'goog.dom.Range', 'goog.dom.TagName', 'goog.editor.BrowserFeature', 'goog.editor.Field', 'goog.editor.Plugin', 'goog.editor.plugins.TagOnEnterHandler', 'goog.events.KeyCodes', 'goog.string.Unicode', 'goog.testing.dom', 'goog.testing.editor.TestHelper', 'goog.testing.events', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('editor/plugins/undoredo.js', ['goog.editor.plugins.UndoRedo'], ['goog.dom', 'goog.dom.NodeOffset', 'goog.dom.Range', 'goog.editor.BrowserFeature', 'goog.editor.Command', 'goog.editor.Field.EventType', 'goog.editor.Plugin', 'goog.editor.node', 'goog.editor.plugins.UndoRedoManager', 'goog.editor.plugins.UndoRedoState', 'goog.events', 'goog.events.EventHandler', 'goog.log']);
goog.addDependency('editor/plugins/undoredo_test.js', ['goog.editor.plugins.UndoRedoTest'], ['goog.array', 'goog.dom', 'goog.dom.browserrange', 'goog.editor.Field', 'goog.editor.plugins.LoremIpsum', 'goog.editor.plugins.UndoRedo', 'goog.events', 'goog.functions', 'goog.testing.MockClock', 'goog.testing.PropertyReplacer', 'goog.testing.StrictMock', 'goog.testing.jsunit']);
goog.addDependency('editor/plugins/undoredomanager.js', ['goog.editor.plugins.UndoRedoManager', 'goog.editor.plugins.UndoRedoManager.EventType'], ['goog.editor.plugins.UndoRedoState', 'goog.events.EventTarget']);
goog.addDependency('editor/plugins/undoredomanager_test.js', ['goog.editor.plugins.UndoRedoManagerTest'], ['goog.editor.plugins.UndoRedoManager', 'goog.editor.plugins.UndoRedoState', 'goog.events', 'goog.testing.StrictMock', 'goog.testing.jsunit']);
goog.addDependency('editor/plugins/undoredostate.js', ['goog.editor.plugins.UndoRedoState'], ['goog.events.EventTarget']);
goog.addDependency('editor/plugins/undoredostate_test.js', ['goog.editor.plugins.UndoRedoStateTest'], ['goog.editor.plugins.UndoRedoState', 'goog.testing.jsunit']);
goog.addDependency('editor/range.js', ['goog.editor.range', 'goog.editor.range.Point'], ['goog.array', 'goog.dom', 'goog.dom.NodeType', 'goog.dom.Range', 'goog.dom.RangeEndpoint', 'goog.dom.SavedCaretRange', 'goog.editor.node', 'goog.editor.style', 'goog.iter', 'goog.userAgent']);
goog.addDependency('editor/range_test.js', ['goog.editor.rangeTest'], ['goog.dom', 'goog.dom.Range', 'goog.dom.TagName', 'goog.editor.range', 'goog.editor.range.Point', 'goog.string', 'goog.testing.dom', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('editor/seamlessfield.js', ['goog.editor.SeamlessField'], ['goog.cssom.iframe.style', 'goog.dom', 'goog.dom.Range', 'goog.dom.TagName', 'goog.editor.BrowserFeature', 'goog.editor.Field', 'goog.editor.icontent', 'goog.editor.icontent.FieldFormatInfo', 'goog.editor.icontent.FieldStyleInfo', 'goog.editor.node', 'goog.events', 'goog.events.EventType', 'goog.log', 'goog.style']);
goog.addDependency('editor/seamlessfield_test.js', ['goog.editor.seamlessfield_test'], ['goog.dom', 'goog.dom.DomHelper', 'goog.dom.Range', 'goog.editor.BrowserFeature', 'goog.editor.Field', 'goog.editor.SeamlessField', 'goog.events', 'goog.functions', 'goog.style', 'goog.testing.MockClock', 'goog.testing.MockRange', 'goog.testing.jsunit']);
goog.addDependency('editor/style.js', ['goog.editor.style'], ['goog.dom', 'goog.dom.NodeType', 'goog.editor.BrowserFeature', 'goog.events.EventType', 'goog.object', 'goog.style', 'goog.userAgent']);
goog.addDependency('editor/style_test.js', ['goog.editor.styleTest'], ['goog.dom', 'goog.dom.TagName', 'goog.editor.BrowserFeature', 'goog.editor.style', 'goog.events.EventHandler', 'goog.events.EventType', 'goog.style', 'goog.testing.LooseMock', 'goog.testing.jsunit', 'goog.testing.mockmatchers']);
goog.addDependency('editor/table.js', ['goog.editor.Table', 'goog.editor.TableCell', 'goog.editor.TableRow'], ['goog.dom', 'goog.dom.DomHelper', 'goog.dom.NodeType', 'goog.dom.TagName', 'goog.log', 'goog.string.Unicode', 'goog.style']);
goog.addDependency('editor/table_test.js', ['goog.editor.TableTest'], ['goog.dom', 'goog.editor.Table', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('events/actioneventwrapper.js', ['goog.events.actionEventWrapper'], ['goog.a11y.aria', 'goog.a11y.aria.Role', 'goog.events', 'goog.events.EventHandler', 'goog.events.EventType', 'goog.events.EventWrapper', 'goog.events.KeyCodes', 'goog.userAgent']);
goog.addDependency('events/actioneventwrapper_test.js', ['goog.events.actionEventWrapperTest'], ['goog.a11y.aria', 'goog.a11y.aria.Role', 'goog.events', 'goog.events.EventHandler', 'goog.events.KeyCodes', 'goog.events.actionEventWrapper', 'goog.testing.events', 'goog.testing.jsunit']);
goog.addDependency('events/actionhandler.js', ['goog.events.ActionEvent', 'goog.events.ActionHandler', 'goog.events.ActionHandler.EventType', 'goog.events.BeforeActionEvent'], ['goog.events', 'goog.events.BrowserEvent', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.userAgent']);
goog.addDependency('events/actionhandler_test.js', ['goog.events.ActionHandlerTest'], ['goog.dom', 'goog.events', 'goog.events.ActionHandler', 'goog.testing.events', 'goog.testing.jsunit']);
goog.addDependency('events/browserevent.js', ['goog.events.BrowserEvent', 'goog.events.BrowserEvent.MouseButton'], ['goog.events.BrowserFeature', 'goog.events.Event', 'goog.events.EventType', 'goog.reflect', 'goog.userAgent']);
goog.addDependency('events/browserevent_test.js', ['goog.events.BrowserEventTest'], ['goog.events.BrowserEvent', 'goog.events.BrowserFeature', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('events/browserfeature.js', ['goog.events.BrowserFeature'], ['goog.userAgent']);
goog.addDependency('events/event.js', ['goog.events.Event', 'goog.events.EventLike'], ['goog.Disposable', 'goog.events.EventId']);
goog.addDependency('events/event_test.js', ['goog.events.EventTest'], ['goog.events.Event', 'goog.events.EventId', 'goog.events.EventTarget', 'goog.testing.jsunit']);
goog.addDependency('events/eventhandler.js', ['goog.events.EventHandler'], ['goog.Disposable', 'goog.events', 'goog.object']);
goog.addDependency('events/eventhandler_test.js', ['goog.events.EventHandlerTest'], ['goog.events', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('events/eventid.js', ['goog.events.EventId'], []);
goog.addDependency('events/events.js', ['goog.events', 'goog.events.CaptureSimulationMode', 'goog.events.Key', 'goog.events.ListenableType'], ['goog.asserts', 'goog.debug.entryPointRegistry', 'goog.events.BrowserEvent', 'goog.events.BrowserFeature', 'goog.events.Listenable', 'goog.events.ListenerMap']);
goog.addDependency('events/events_test.js', ['goog.eventsTest'], ['goog.asserts.AssertionError', 'goog.debug.EntryPointMonitor', 'goog.debug.ErrorHandler', 'goog.debug.entryPointRegistry', 'goog.dom', 'goog.events', 'goog.events.BrowserFeature', 'goog.events.CaptureSimulationMode', 'goog.events.Event', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.events.Listener', 'goog.functions', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('events/eventtarget.js', ['goog.events.EventTarget'], ['goog.Disposable', 'goog.asserts', 'goog.events', 'goog.events.Event', 'goog.events.Listenable', 'goog.events.ListenerMap', 'goog.object']);
goog.addDependency('events/eventtarget_test.js', ['goog.events.EventTargetTest'], ['goog.events.EventTarget', 'goog.events.Listenable', 'goog.events.eventTargetTester', 'goog.events.eventTargetTester.KeyType', 'goog.events.eventTargetTester.UnlistenReturnType', 'goog.testing.jsunit']);
goog.addDependency('events/eventtarget_via_googevents_test.js', ['goog.events.EventTargetGoogEventsTest'], ['goog.events', 'goog.events.EventTarget', 'goog.events.eventTargetTester', 'goog.events.eventTargetTester.KeyType', 'goog.events.eventTargetTester.UnlistenReturnType', 'goog.object', 'goog.testing', 'goog.testing.jsunit']);
goog.addDependency('events/eventtarget_via_w3cinterface_test.js', ['goog.events.EventTargetW3CTest'], ['goog.events.EventTarget', 'goog.events.eventTargetTester', 'goog.events.eventTargetTester.KeyType', 'goog.events.eventTargetTester.UnlistenReturnType', 'goog.testing.jsunit']);
goog.addDependency('events/eventtargettester.js', ['goog.events.eventTargetTester', 'goog.events.eventTargetTester.KeyType', 'goog.events.eventTargetTester.UnlistenReturnType'], ['goog.array', 'goog.events', 'goog.events.Event', 'goog.events.EventTarget', 'goog.testing.asserts', 'goog.testing.recordFunction']);
goog.addDependency('events/eventtype.js', ['goog.events.EventType'], ['goog.userAgent']);
goog.addDependency('events/eventwrapper.js', ['goog.events.EventWrapper'], []);
goog.addDependency('events/filedrophandler.js', ['goog.events.FileDropHandler', 'goog.events.FileDropHandler.EventType'], ['goog.array', 'goog.dom', 'goog.events', 'goog.events.BrowserEvent', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.log']);
goog.addDependency('events/filedrophandler_test.js', ['goog.events.FileDropHandlerTest'], ['goog.events', 'goog.events.BrowserEvent', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.events.FileDropHandler', 'goog.testing.jsunit']);
goog.addDependency('events/focushandler.js', ['goog.events.FocusHandler', 'goog.events.FocusHandler.EventType'], ['goog.events', 'goog.events.BrowserEvent', 'goog.events.EventTarget', 'goog.userAgent']);
goog.addDependency('events/imehandler.js', ['goog.events.ImeHandler', 'goog.events.ImeHandler.Event', 'goog.events.ImeHandler.EventType'], ['goog.events.Event', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.userAgent']);
goog.addDependency('events/imehandler_test.js', ['goog.events.ImeHandlerTest'], ['goog.array', 'goog.dom', 'goog.events', 'goog.events.ImeHandler', 'goog.events.KeyCodes', 'goog.object', 'goog.string', 'goog.testing.PropertyReplacer', 'goog.testing.events', 'goog.testing.events.Event', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('events/inputhandler.js', ['goog.events.InputHandler', 'goog.events.InputHandler.EventType'], ['goog.Timer', 'goog.dom', 'goog.events.BrowserEvent', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.events.KeyCodes', 'goog.userAgent']);
goog.addDependency('events/inputhandler_test.js', ['goog.events.InputHandlerTest'], ['goog.dom', 'goog.events.EventHandler', 'goog.events.EventType', 'goog.events.InputHandler', 'goog.events.KeyCodes', 'goog.testing.events', 'goog.testing.events.Event', 'goog.testing.jsunit', 'goog.testing.recordFunction', 'goog.userAgent']);
goog.addDependency('events/keycodes.js', ['goog.events.KeyCodes'], ['goog.userAgent']);
goog.addDependency('events/keycodes_test.js', ['goog.events.KeyCodesTest'], ['goog.events.BrowserEvent', 'goog.events.KeyCodes', 'goog.object', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('events/keyhandler.js', ['goog.events.KeyEvent', 'goog.events.KeyHandler', 'goog.events.KeyHandler.EventType'], ['goog.events', 'goog.events.BrowserEvent', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.userAgent']);
goog.addDependency('events/keyhandler_test.js', ['goog.events.KeyEventTest'], ['goog.dom', 'goog.events', 'goog.events.BrowserEvent', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.events.KeyHandler', 'goog.testing.events', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('events/keynames.js', ['goog.events.KeyNames'], []);
goog.addDependency('events/listenable.js', ['goog.events.Listenable', 'goog.events.ListenableKey'], ['goog.events.EventId']);
goog.addDependency('events/listenable_test.js', ['goog.events.ListenableTest'], ['goog.events.Listenable', 'goog.testing.jsunit']);
goog.addDependency('events/listener.js', ['goog.events.Listener'], ['goog.events.ListenableKey']);
goog.addDependency('events/listenermap.js', ['goog.events.ListenerMap'], ['goog.array', 'goog.events.Listener', 'goog.object']);
goog.addDependency('events/listenermap_test.js', ['goog.events.ListenerMapTest'], ['goog.dispose', 'goog.events', 'goog.events.EventId', 'goog.events.EventTarget', 'goog.events.ListenerMap', 'goog.testing.jsunit']);
goog.addDependency('events/mousewheelhandler.js', ['goog.events.MouseWheelEvent', 'goog.events.MouseWheelHandler', 'goog.events.MouseWheelHandler.EventType'], ['goog.dom', 'goog.events', 'goog.events.BrowserEvent', 'goog.events.EventTarget', 'goog.math', 'goog.style', 'goog.userAgent']);
goog.addDependency('events/mousewheelhandler_test.js', ['goog.events.MouseWheelHandlerTest'], ['goog.dom', 'goog.events', 'goog.events.BrowserEvent', 'goog.events.MouseWheelEvent', 'goog.events.MouseWheelHandler', 'goog.functions', 'goog.string', 'goog.testing.PropertyReplacer', 'goog.testing.events', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('events/onlinehandler.js', ['goog.events.OnlineHandler', 'goog.events.OnlineHandler.EventType'], ['goog.Timer', 'goog.events.BrowserFeature', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.net.NetworkStatusMonitor', 'goog.userAgent']);
goog.addDependency('events/onlinelistener_test.js', ['goog.events.OnlineHandlerTest'], ['goog.events', 'goog.events.BrowserFeature', 'goog.events.Event', 'goog.events.EventHandler', 'goog.events.OnlineHandler', 'goog.net.NetworkStatusMonitor', 'goog.testing.MockClock', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('events/pastehandler.js', ['goog.events.PasteHandler', 'goog.events.PasteHandler.EventType', 'goog.events.PasteHandler.State'], ['goog.Timer', 'goog.async.ConditionalDelay', 'goog.events.BrowserEvent', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.log', 'goog.userAgent']);
goog.addDependency('events/pastehandler_test.js', ['goog.events.PasteHandlerTest'], ['goog.dom', 'goog.events', 'goog.events.BrowserEvent', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.events.PasteHandler', 'goog.testing.MockClock', 'goog.testing.MockUserAgent', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('format/emailaddress.js', ['goog.format.EmailAddress'], ['goog.string']);
goog.addDependency('format/emailaddress_test.js', ['goog.format.EmailAddressTest'], ['goog.array', 'goog.format.EmailAddress', 'goog.testing.jsunit']);
goog.addDependency('format/format.js', ['goog.format'], ['goog.i18n.GraphemeBreak', 'goog.string', 'goog.userAgent']);
goog.addDependency('format/format_test.js', ['goog.formatTest'], ['goog.dom', 'goog.format', 'goog.string', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit']);
goog.addDependency('format/htmlprettyprinter.js', ['goog.format.HtmlPrettyPrinter', 'goog.format.HtmlPrettyPrinter.Buffer'], ['goog.object', 'goog.string.StringBuffer']);
goog.addDependency('format/htmlprettyprinter_test.js', ['goog.format.HtmlPrettyPrinterTest'], ['goog.format.HtmlPrettyPrinter', 'goog.testing.MockClock', 'goog.testing.jsunit']);
goog.addDependency('format/internationalizedemailaddress.js', ['goog.format.InternationalizedEmailAddress'], ['goog.format.EmailAddress']);
goog.addDependency('format/internationalizedemailaddress_test.js', ['goog.format.InternationalizedEmailAddressTest'], ['goog.array', 'goog.format.InternationalizedEmailAddress', 'goog.testing.jsunit']);
goog.addDependency('format/jsonprettyprinter.js', ['goog.format.JsonPrettyPrinter', 'goog.format.JsonPrettyPrinter.HtmlDelimiters', 'goog.format.JsonPrettyPrinter.TextDelimiters'], ['goog.json', 'goog.json.Serializer', 'goog.string', 'goog.string.StringBuffer', 'goog.string.format']);
goog.addDependency('format/jsonprettyprinter_test.js', ['goog.format.JsonPrettyPrinterTest'], ['goog.format.JsonPrettyPrinter', 'goog.testing.jsunit']);
goog.addDependency('fs/entry.js', ['goog.fs.DirectoryEntry', 'goog.fs.DirectoryEntry.Behavior', 'goog.fs.Entry', 'goog.fs.FileEntry'], []);
goog.addDependency('fs/entryimpl.js', ['goog.fs.DirectoryEntryImpl', 'goog.fs.EntryImpl', 'goog.fs.FileEntryImpl'], ['goog.array', 'goog.async.Deferred', 'goog.fs.DirectoryEntry', 'goog.fs.Entry', 'goog.fs.Error', 'goog.fs.FileEntry', 'goog.fs.FileWriter', 'goog.functions', 'goog.string']);
goog.addDependency('fs/error.js', ['goog.fs.Error', 'goog.fs.Error.ErrorCode'], ['goog.debug.Error', 'goog.object', 'goog.string']);
goog.addDependency('fs/filereader.js', ['goog.fs.FileReader', 'goog.fs.FileReader.EventType', 'goog.fs.FileReader.ReadyState'], ['goog.async.Deferred', 'goog.events.EventTarget', 'goog.fs.Error', 'goog.fs.ProgressEvent']);
goog.addDependency('fs/filesaver.js', ['goog.fs.FileSaver', 'goog.fs.FileSaver.EventType', 'goog.fs.FileSaver.ProgressEvent', 'goog.fs.FileSaver.ReadyState'], ['goog.events.EventTarget', 'goog.fs.Error', 'goog.fs.ProgressEvent']);
goog.addDependency('fs/filesystem.js', ['goog.fs.FileSystem'], []);
goog.addDependency('fs/filesystemimpl.js', ['goog.fs.FileSystemImpl'], ['goog.fs.DirectoryEntryImpl', 'goog.fs.FileSystem']);
goog.addDependency('fs/filewriter.js', ['goog.fs.FileWriter'], ['goog.fs.Error', 'goog.fs.FileSaver']);
goog.addDependency('fs/fs.js', ['goog.fs'], ['goog.array', 'goog.async.Deferred', 'goog.fs.Error', 'goog.fs.FileReader', 'goog.fs.FileSystemImpl', 'goog.userAgent']);
goog.addDependency('fs/fs_test.js', ['goog.fsTest'], ['goog.array', 'goog.async.Deferred', 'goog.async.DeferredList', 'goog.dom', 'goog.events', 'goog.fs', 'goog.fs.DirectoryEntry', 'goog.fs.Error', 'goog.fs.FileReader', 'goog.fs.FileSaver', 'goog.string', 'goog.testing.AsyncTestCase', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit']);
goog.addDependency('fs/progressevent.js', ['goog.fs.ProgressEvent'], ['goog.events.Event']);
goog.addDependency('functions/functions.js', ['goog.functions'], []);
goog.addDependency('functions/functions_test.js', ['goog.functionsTest'], ['goog.functions', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('fx/abstractdragdrop.js', ['goog.fx.AbstractDragDrop', 'goog.fx.AbstractDragDrop.EventType', 'goog.fx.DragDropEvent', 'goog.fx.DragDropItem'], ['goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.events', 'goog.events.Event', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.fx.Dragger', 'goog.math.Box', 'goog.math.Coordinate', 'goog.style']);
goog.addDependency('fx/abstractdragdrop_test.js', ['goog.fx.AbstractDragDropTest'], ['goog.array', 'goog.events.EventType', 'goog.functions', 'goog.fx.AbstractDragDrop', 'goog.fx.DragDropItem', 'goog.math.Box', 'goog.math.Coordinate', 'goog.style', 'goog.testing.events', 'goog.testing.jsunit']);
goog.addDependency('fx/anim/anim.js', ['goog.fx.anim', 'goog.fx.anim.Animated'], ['goog.async.AnimationDelay', 'goog.async.Delay', 'goog.object']);
goog.addDependency('fx/anim/anim_test.js', ['goog.fx.animTest'], ['goog.async.AnimationDelay', 'goog.async.Delay', 'goog.events', 'goog.functions', 'goog.fx.Animation', 'goog.fx.anim', 'goog.object', 'goog.testing.MockClock', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.testing.recordFunction', 'goog.userAgent']);
goog.addDependency('fx/animation.js', ['goog.fx.Animation', 'goog.fx.Animation.EventType', 'goog.fx.Animation.State', 'goog.fx.AnimationEvent'], ['goog.array', 'goog.events.Event', 'goog.fx.Transition', 'goog.fx.Transition.EventType', 'goog.fx.TransitionBase.State', 'goog.fx.anim', 'goog.fx.anim.Animated']);
goog.addDependency('fx/animation_test.js', ['goog.fx.AnimationTest'], ['goog.events', 'goog.fx.Animation', 'goog.testing.MockClock', 'goog.testing.jsunit']);
goog.addDependency('fx/animationqueue.js', ['goog.fx.AnimationParallelQueue', 'goog.fx.AnimationQueue', 'goog.fx.AnimationSerialQueue'], ['goog.array', 'goog.asserts', 'goog.events.EventHandler', 'goog.fx.Transition.EventType', 'goog.fx.TransitionBase', 'goog.fx.TransitionBase.State']);
goog.addDependency('fx/animationqueue_test.js', ['goog.fx.AnimationQueueTest'], ['goog.events', 'goog.fx.Animation', 'goog.fx.AnimationParallelQueue', 'goog.fx.AnimationSerialQueue', 'goog.fx.Transition', 'goog.fx.anim', 'goog.testing.MockClock', 'goog.testing.jsunit']);
goog.addDependency('fx/css3/fx.js', ['goog.fx.css3'], ['goog.fx.css3.Transition']);
goog.addDependency('fx/css3/transition.js', ['goog.fx.css3.Transition'], ['goog.Timer', 'goog.fx.TransitionBase', 'goog.style', 'goog.style.transition']);
goog.addDependency('fx/css3/transition_test.js', ['goog.fx.css3.TransitionTest'], ['goog.dispose', 'goog.dom', 'goog.events', 'goog.fx.Transition', 'goog.fx.css3.Transition', 'goog.style.transition', 'goog.testing.MockClock', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('fx/cssspriteanimation.js', ['goog.fx.CssSpriteAnimation'], ['goog.fx.Animation']);
goog.addDependency('fx/cssspriteanimation_test.js', ['goog.fx.CssSpriteAnimationTest'], ['goog.fx.CssSpriteAnimation', 'goog.math.Box', 'goog.math.Size', 'goog.testing.MockClock', 'goog.testing.jsunit']);
goog.addDependency('fx/dom.js', ['goog.fx.dom', 'goog.fx.dom.BgColorTransform', 'goog.fx.dom.ColorTransform', 'goog.fx.dom.Fade', 'goog.fx.dom.FadeIn', 'goog.fx.dom.FadeInAndShow', 'goog.fx.dom.FadeOut', 'goog.fx.dom.FadeOutAndHide', 'goog.fx.dom.PredefinedEffect', 'goog.fx.dom.Resize', 'goog.fx.dom.ResizeHeight', 'goog.fx.dom.ResizeWidth', 'goog.fx.dom.Scroll', 'goog.fx.dom.Slide', 'goog.fx.dom.SlideFrom', 'goog.fx.dom.Swipe'], ['goog.color', 'goog.events', 'goog.fx.Animation', 'goog.fx.Transition', 'goog.style', 'goog.style.bidi']);
goog.addDependency('fx/dragdrop.js', ['goog.fx.DragDrop'], ['goog.fx.AbstractDragDrop', 'goog.fx.DragDropItem']);
goog.addDependency('fx/dragdropgroup.js', ['goog.fx.DragDropGroup'], ['goog.dom', 'goog.fx.AbstractDragDrop', 'goog.fx.DragDropItem']);
goog.addDependency('fx/dragdropgroup_test.js', ['goog.fx.DragDropGroupTest'], ['goog.events', 'goog.fx.DragDropGroup', 'goog.testing.jsunit']);
goog.addDependency('fx/dragger.js', ['goog.fx.DragEvent', 'goog.fx.Dragger', 'goog.fx.Dragger.EventType'], ['goog.dom', 'goog.events', 'goog.events.Event', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.math.Coordinate', 'goog.math.Rect', 'goog.style', 'goog.style.bidi', 'goog.userAgent']);
goog.addDependency('fx/dragger_test.js', ['goog.fx.DraggerTest'], ['goog.dom', 'goog.events', 'goog.events.BrowserEvent', 'goog.events.Event', 'goog.events.EventType', 'goog.fx.Dragger', 'goog.math.Rect', 'goog.style.bidi', 'goog.testing.StrictMock', 'goog.testing.events', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('fx/draglistgroup.js', ['goog.fx.DragListDirection', 'goog.fx.DragListGroup', 'goog.fx.DragListGroup.EventType', 'goog.fx.DragListGroupEvent'], ['goog.array', 'goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.events', 'goog.events.Event', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.fx.Dragger', 'goog.math.Coordinate', 'goog.string', 'goog.style']);
goog.addDependency('fx/draglistgroup_test.js', ['goog.fx.DragListGroupTest'], ['goog.array', 'goog.dom', 'goog.dom.classlist', 'goog.events', 'goog.events.BrowserEvent', 'goog.events.BrowserFeature', 'goog.events.Event', 'goog.events.EventType', 'goog.fx.DragEvent', 'goog.fx.DragListDirection', 'goog.fx.DragListGroup', 'goog.fx.Dragger', 'goog.math.Coordinate', 'goog.object', 'goog.testing.events', 'goog.testing.jsunit']);
goog.addDependency('fx/dragscrollsupport.js', ['goog.fx.DragScrollSupport'], ['goog.Disposable', 'goog.Timer', 'goog.dom', 'goog.events.EventHandler', 'goog.events.EventType', 'goog.math.Coordinate', 'goog.style']);
goog.addDependency('fx/dragscrollsupport_test.js', ['goog.fx.DragScrollSupportTest'], ['goog.fx.DragScrollSupport', 'goog.math.Coordinate', 'goog.testing.MockClock', 'goog.testing.events', 'goog.testing.jsunit']);
goog.addDependency('fx/easing.js', ['goog.fx.easing'], []);
goog.addDependency('fx/easing_test.js', ['goog.fx.easingTest'], ['goog.fx.easing', 'goog.testing.jsunit']);
goog.addDependency('fx/fx.js', ['goog.fx'], ['goog.asserts', 'goog.fx.Animation', 'goog.fx.Animation.EventType', 'goog.fx.Animation.State', 'goog.fx.AnimationEvent', 'goog.fx.Transition.EventType', 'goog.fx.easing']);
goog.addDependency('fx/fx_test.js', ['goog.fxTest'], ['goog.fx.Animation', 'goog.object', 'goog.testing.MockClock', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit']);
goog.addDependency('fx/transition.js', ['goog.fx.Transition', 'goog.fx.Transition.EventType'], []);
goog.addDependency('fx/transitionbase.js', ['goog.fx.TransitionBase', 'goog.fx.TransitionBase.State'], ['goog.events.EventTarget', 'goog.fx.Transition', 'goog.fx.Transition.EventType']);
goog.addDependency('graphics/abstractgraphics.js', ['goog.graphics.AbstractGraphics'], ['goog.dom', 'goog.graphics.Path', 'goog.math.Coordinate', 'goog.math.Size', 'goog.style', 'goog.ui.Component']);
goog.addDependency('graphics/affinetransform.js', ['goog.graphics.AffineTransform'], ['goog.math']);
goog.addDependency('graphics/canvaselement.js', ['goog.graphics.CanvasEllipseElement', 'goog.graphics.CanvasGroupElement', 'goog.graphics.CanvasImageElement', 'goog.graphics.CanvasPathElement', 'goog.graphics.CanvasRectElement', 'goog.graphics.CanvasTextElement'], ['goog.array', 'goog.dom', 'goog.dom.TagName', 'goog.graphics.EllipseElement', 'goog.graphics.GroupElement', 'goog.graphics.ImageElement', 'goog.graphics.Path', 'goog.graphics.PathElement', 'goog.graphics.RectElement', 'goog.graphics.TextElement', 'goog.math', 'goog.string']);
goog.addDependency('graphics/canvasgraphics.js', ['goog.graphics.CanvasGraphics'], ['goog.events.EventType', 'goog.graphics.AbstractGraphics', 'goog.graphics.CanvasEllipseElement', 'goog.graphics.CanvasGroupElement', 'goog.graphics.CanvasImageElement', 'goog.graphics.CanvasPathElement', 'goog.graphics.CanvasRectElement', 'goog.graphics.CanvasTextElement', 'goog.graphics.SolidFill', 'goog.math.Size', 'goog.style']);
goog.addDependency('graphics/element.js', ['goog.graphics.Element'], ['goog.asserts', 'goog.events', 'goog.events.EventTarget', 'goog.events.Listenable', 'goog.graphics.AffineTransform', 'goog.math']);
goog.addDependency('graphics/ellipseelement.js', ['goog.graphics.EllipseElement'], ['goog.graphics.StrokeAndFillElement']);
goog.addDependency('graphics/ext/coordinates.js', ['goog.graphics.ext.coordinates'], ['goog.string']);
goog.addDependency('graphics/ext/element.js', ['goog.graphics.ext.Element'], ['goog.events.EventTarget', 'goog.functions', 'goog.graphics.ext.coordinates']);
goog.addDependency('graphics/ext/ellipse.js', ['goog.graphics.ext.Ellipse'], ['goog.graphics.ext.StrokeAndFillElement']);
goog.addDependency('graphics/ext/ext.js', ['goog.graphics.ext'], ['goog.graphics.ext.Ellipse', 'goog.graphics.ext.Graphics', 'goog.graphics.ext.Group', 'goog.graphics.ext.Image', 'goog.graphics.ext.Rectangle', 'goog.graphics.ext.Shape', 'goog.graphics.ext.coordinates']);
goog.addDependency('graphics/ext/graphics.js', ['goog.graphics.ext.Graphics'], ['goog.events.EventType', 'goog.graphics.ext.Group']);
goog.addDependency('graphics/ext/group.js', ['goog.graphics.ext.Group'], ['goog.array', 'goog.graphics.ext.Element']);
goog.addDependency('graphics/ext/image.js', ['goog.graphics.ext.Image'], ['goog.graphics.ext.Element']);
goog.addDependency('graphics/ext/path.js', ['goog.graphics.ext.Path'], ['goog.graphics.AffineTransform', 'goog.graphics.Path', 'goog.math', 'goog.math.Rect']);
goog.addDependency('graphics/ext/rectangle.js', ['goog.graphics.ext.Rectangle'], ['goog.graphics.ext.StrokeAndFillElement']);
goog.addDependency('graphics/ext/shape.js', ['goog.graphics.ext.Shape'], ['goog.graphics.ext.Path', 'goog.graphics.ext.StrokeAndFillElement', 'goog.math.Rect']);
goog.addDependency('graphics/ext/strokeandfillelement.js', ['goog.graphics.ext.StrokeAndFillElement'], ['goog.graphics.ext.Element']);
goog.addDependency('graphics/fill.js', ['goog.graphics.Fill'], []);
goog.addDependency('graphics/font.js', ['goog.graphics.Font'], []);
goog.addDependency('graphics/graphics.js', ['goog.graphics'], ['goog.dom', 'goog.graphics.CanvasGraphics', 'goog.graphics.SvgGraphics', 'goog.graphics.VmlGraphics', 'goog.userAgent']);
goog.addDependency('graphics/groupelement.js', ['goog.graphics.GroupElement'], ['goog.graphics.Element']);
goog.addDependency('graphics/imageelement.js', ['goog.graphics.ImageElement'], ['goog.graphics.Element']);
goog.addDependency('graphics/lineargradient.js', ['goog.graphics.LinearGradient'], ['goog.asserts', 'goog.graphics.Fill']);
goog.addDependency('graphics/path.js', ['goog.graphics.Path', 'goog.graphics.Path.Segment'], ['goog.array', 'goog.math']);
goog.addDependency('graphics/pathelement.js', ['goog.graphics.PathElement'], ['goog.graphics.StrokeAndFillElement']);
goog.addDependency('graphics/paths.js', ['goog.graphics.paths'], ['goog.graphics.Path', 'goog.math.Coordinate']);
goog.addDependency('graphics/rectelement.js', ['goog.graphics.RectElement'], ['goog.graphics.StrokeAndFillElement']);
goog.addDependency('graphics/solidfill.js', ['goog.graphics.SolidFill'], ['goog.graphics.Fill']);
goog.addDependency('graphics/stroke.js', ['goog.graphics.Stroke'], []);
goog.addDependency('graphics/strokeandfillelement.js', ['goog.graphics.StrokeAndFillElement'], ['goog.graphics.Element']);
goog.addDependency('graphics/svgelement.js', ['goog.graphics.SvgEllipseElement', 'goog.graphics.SvgGroupElement', 'goog.graphics.SvgImageElement', 'goog.graphics.SvgPathElement', 'goog.graphics.SvgRectElement', 'goog.graphics.SvgTextElement'], ['goog.dom', 'goog.graphics.EllipseElement', 'goog.graphics.GroupElement', 'goog.graphics.ImageElement', 'goog.graphics.PathElement', 'goog.graphics.RectElement', 'goog.graphics.TextElement']);
goog.addDependency('graphics/svggraphics.js', ['goog.graphics.SvgGraphics'], ['goog.Timer', 'goog.dom', 'goog.events.EventHandler', 'goog.events.EventType', 'goog.graphics.AbstractGraphics', 'goog.graphics.LinearGradient', 'goog.graphics.Path', 'goog.graphics.SolidFill', 'goog.graphics.Stroke', 'goog.graphics.SvgEllipseElement', 'goog.graphics.SvgGroupElement', 'goog.graphics.SvgImageElement', 'goog.graphics.SvgPathElement', 'goog.graphics.SvgRectElement', 'goog.graphics.SvgTextElement', 'goog.math', 'goog.math.Size', 'goog.style', 'goog.userAgent']);
goog.addDependency('graphics/textelement.js', ['goog.graphics.TextElement'], ['goog.graphics.StrokeAndFillElement']);
goog.addDependency('graphics/vmlelement.js', ['goog.graphics.VmlEllipseElement', 'goog.graphics.VmlGroupElement', 'goog.graphics.VmlImageElement', 'goog.graphics.VmlPathElement', 'goog.graphics.VmlRectElement', 'goog.graphics.VmlTextElement'], ['goog.dom', 'goog.graphics.EllipseElement', 'goog.graphics.GroupElement', 'goog.graphics.ImageElement', 'goog.graphics.PathElement', 'goog.graphics.RectElement', 'goog.graphics.TextElement']);
goog.addDependency('graphics/vmlgraphics.js', ['goog.graphics.VmlGraphics'], ['goog.array', 'goog.events', 'goog.events.EventHandler', 'goog.events.EventType', 'goog.graphics.AbstractGraphics', 'goog.graphics.LinearGradient', 'goog.graphics.Path', 'goog.graphics.SolidFill', 'goog.graphics.VmlEllipseElement', 'goog.graphics.VmlGroupElement', 'goog.graphics.VmlImageElement', 'goog.graphics.VmlPathElement', 'goog.graphics.VmlRectElement', 'goog.graphics.VmlTextElement', 'goog.math', 'goog.math.Size', 'goog.string', 'goog.style']);
goog.addDependency('history/event.js', ['goog.history.Event'], ['goog.events.Event', 'goog.history.EventType']);
goog.addDependency('history/eventtype.js', ['goog.history.EventType'], []);
goog.addDependency('history/history.js', ['goog.History', 'goog.History.Event', 'goog.History.EventType'], ['goog.Timer', 'goog.dom', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.history.Event', 'goog.history.EventType', 'goog.memoize', 'goog.string', 'goog.userAgent']);
goog.addDependency('history/history_test.js', ['goog.HistoryTest'], ['goog.History', 'goog.dispose', 'goog.dom', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('history/html5history.js', ['goog.history.Html5History', 'goog.history.Html5History.TokenTransformer'], ['goog.asserts', 'goog.events', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.history.Event', 'goog.history.EventType']);
goog.addDependency('history/html5history_test.js', ['goog.history.Html5HistoryTest'], ['goog.history.Html5History', 'goog.testing.MockControl', 'goog.testing.jsunit', 'goog.testing.mockmatchers']);
goog.addDependency('html/legacyconversions.js', ['goog.html.legacyconversions'], ['goog.html.SafeHtml', 'goog.html.SafeUrl', 'goog.html.TrustedResourceUrl']);
goog.addDependency('html/legacyconversions_test.js', ['goog.html.legacyconversionsTest'], ['goog.html.SafeHtml', 'goog.html.SafeUrl', 'goog.html.TrustedResourceUrl', 'goog.html.legacyconversions', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit']);
goog.addDependency('html/safehtml.js', ['goog.html.SafeHtml'], ['goog.array', 'goog.asserts', 'goog.dom.tags', 'goog.html.SafeStyle', 'goog.html.SafeUrl', 'goog.i18n.bidi.Dir', 'goog.i18n.bidi.DirectionalString', 'goog.object', 'goog.string', 'goog.string.Const', 'goog.string.TypedString']);
goog.addDependency('html/safehtml_test.js', ['goog.html.safeHtmlTest'], ['goog.html.SafeHtml', 'goog.html.SafeStyle', 'goog.html.SafeUrl', 'goog.html.testing', 'goog.i18n.bidi.Dir', 'goog.string.Const', 'goog.testing.jsunit']);
goog.addDependency('html/safestyle.js', ['goog.html.SafeStyle'], ['goog.array', 'goog.asserts', 'goog.string', 'goog.string.Const', 'goog.string.TypedString']);
goog.addDependency('html/safestyle_test.js', ['goog.html.safeStyleTest'], ['goog.html.SafeStyle', 'goog.string.Const', 'goog.testing.jsunit']);
goog.addDependency('html/safeurl.js', ['goog.html.SafeUrl'], ['goog.asserts', 'goog.i18n.bidi.Dir', 'goog.i18n.bidi.DirectionalString', 'goog.string.Const', 'goog.string.TypedString']);
goog.addDependency('html/safeurl_test.js', ['goog.html.safeUrlTest'], ['goog.html.SafeUrl', 'goog.i18n.bidi.Dir', 'goog.string.Const', 'goog.testing.jsunit']);
goog.addDependency('html/testing.js', ['goog.html.testing'], ['goog.html.SafeHtml', 'goog.html.SafeStyle', 'goog.html.SafeUrl', 'goog.html.TrustedResourceUrl']);
goog.addDependency('html/trustedresourceurl.js', ['goog.html.TrustedResourceUrl'], ['goog.asserts', 'goog.i18n.bidi.Dir', 'goog.i18n.bidi.DirectionalString', 'goog.string.Const', 'goog.string.TypedString']);
goog.addDependency('html/trustedresourceurl_test.js', ['goog.html.trustedResourceUrlTest'], ['goog.html.TrustedResourceUrl', 'goog.i18n.bidi.Dir', 'goog.string.Const', 'goog.testing.jsunit']);
goog.addDependency('html/uncheckedconversions.js', ['goog.html.uncheckedconversions'], ['goog.asserts', 'goog.html.SafeHtml', 'goog.html.SafeStyle', 'goog.html.SafeUrl', 'goog.html.TrustedResourceUrl', 'goog.string', 'goog.string.Const']);
goog.addDependency('html/uncheckedconversions_test.js', ['goog.html.uncheckedconversionsTest'], ['goog.html.SafeHtml', 'goog.html.SafeUrl', 'goog.html.TrustedResourceUrl', 'goog.html.uncheckedconversions', 'goog.i18n.bidi.Dir', 'goog.string.Const', 'goog.testing.jsunit']);
goog.addDependency('html/utils.js', ['goog.html.utils'], ['goog.string']);
goog.addDependency('html/utils_test.js', ['goog.html.UtilsTest'], ['goog.array', 'goog.dom.TagName', 'goog.html.utils', 'goog.object', 'goog.testing.jsunit']);
goog.addDependency('i18n/bidi.js', ['goog.i18n.bidi', 'goog.i18n.bidi.Dir', 'goog.i18n.bidi.DirectionalString', 'goog.i18n.bidi.Format'], []);
goog.addDependency('i18n/bidi_test.js', ['goog.i18n.bidiTest'], ['goog.i18n.bidi', 'goog.i18n.bidi.Dir', 'goog.testing.jsunit']);
goog.addDependency('i18n/bidiformatter.js', ['goog.i18n.BidiFormatter'], ['goog.html.SafeHtml', 'goog.html.legacyconversions', 'goog.i18n.bidi', 'goog.i18n.bidi.Dir', 'goog.i18n.bidi.Format']);
goog.addDependency('i18n/bidiformatter_test.js', ['goog.i18n.BidiFormatterTest'], ['goog.html.SafeHtml', 'goog.i18n.BidiFormatter', 'goog.i18n.bidi.Dir', 'goog.i18n.bidi.Format', 'goog.testing.jsunit']);
goog.addDependency('i18n/charlistdecompressor.js', ['goog.i18n.CharListDecompressor'], ['goog.array', 'goog.i18n.uChar']);
goog.addDependency('i18n/charlistdecompressor_test.js', ['goog.i18n.CharListDecompressorTest'], ['goog.i18n.CharListDecompressor', 'goog.testing.jsunit']);
goog.addDependency('i18n/charpickerdata.js', ['goog.i18n.CharPickerData'], []);
goog.addDependency('i18n/collation.js', ['goog.i18n.collation'], []);
goog.addDependency('i18n/collation_test.js', ['goog.i18n.collationTest'], ['goog.i18n.collation', 'goog.testing.ExpectedFailures', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('i18n/compactnumberformatsymbols.js', ['goog.i18n.CompactNumberFormatSymbols', 'goog.i18n.CompactNumberFormatSymbols_af', 'goog.i18n.CompactNumberFormatSymbols_af_ZA', 'goog.i18n.CompactNumberFormatSymbols_am', 'goog.i18n.CompactNumberFormatSymbols_am_ET', 'goog.i18n.CompactNumberFormatSymbols_ar', 'goog.i18n.CompactNumberFormatSymbols_ar_001', 'goog.i18n.CompactNumberFormatSymbols_az', 'goog.i18n.CompactNumberFormatSymbols_az_Cyrl_AZ', 'goog.i18n.CompactNumberFormatSymbols_az_Latn_AZ', 'goog.i18n.CompactNumberFormatSymbols_bg', 'goog.i18n.CompactNumberFormatSymbols_bg_BG', 'goog.i18n.CompactNumberFormatSymbols_bn', 'goog.i18n.CompactNumberFormatSymbols_bn_BD', 'goog.i18n.CompactNumberFormatSymbols_br', 'goog.i18n.CompactNumberFormatSymbols_br_FR', 'goog.i18n.CompactNumberFormatSymbols_ca', 'goog.i18n.CompactNumberFormatSymbols_ca_AD', 'goog.i18n.CompactNumberFormatSymbols_ca_ES', 'goog.i18n.CompactNumberFormatSymbols_ca_ES_VALENCIA', 'goog.i18n.CompactNumberFormatSymbols_ca_FR', 'goog.i18n.CompactNumberFormatSymbols_ca_IT', 'goog.i18n.CompactNumberFormatSymbols_chr', 'goog.i18n.CompactNumberFormatSymbols_chr_US', 'goog.i18n.CompactNumberFormatSymbols_cs', 'goog.i18n.CompactNumberFormatSymbols_cs_CZ', 'goog.i18n.CompactNumberFormatSymbols_cy', 'goog.i18n.CompactNumberFormatSymbols_cy_GB', 'goog.i18n.CompactNumberFormatSymbols_da', 'goog.i18n.CompactNumberFormatSymbols_da_DK', 'goog.i18n.CompactNumberFormatSymbols_da_GL', 'goog.i18n.CompactNumberFormatSymbols_de', 'goog.i18n.CompactNumberFormatSymbols_de_AT', 'goog.i18n.CompactNumberFormatSymbols_de_BE', 'goog.i18n.CompactNumberFormatSymbols_de_CH', 'goog.i18n.CompactNumberFormatSymbols_de_DE', 'goog.i18n.CompactNumberFormatSymbols_de_LU', 'goog.i18n.CompactNumberFormatSymbols_el', 'goog.i18n.CompactNumberFormatSymbols_el_GR', 'goog.i18n.CompactNumberFormatSymbols_en', 'goog.i18n.CompactNumberFormatSymbols_en_001', 'goog.i18n.CompactNumberFormatSymbols_en_AS', 'goog.i18n.CompactNumberFormatSymbols_en_AU', 'goog.i18n.CompactNumberFormatSymbols_en_DG', 'goog.i18n.CompactNumberFormatSymbols_en_FM', 'goog.i18n.CompactNumberFormatSymbols_en_GB', 'goog.i18n.CompactNumberFormatSymbols_en_GU', 'goog.i18n.CompactNumberFormatSymbols_en_IE', 'goog.i18n.CompactNumberFormatSymbols_en_IN', 'goog.i18n.CompactNumberFormatSymbols_en_IO', 'goog.i18n.CompactNumberFormatSymbols_en_MH', 'goog.i18n.CompactNumberFormatSymbols_en_MP', 'goog.i18n.CompactNumberFormatSymbols_en_PR', 'goog.i18n.CompactNumberFormatSymbols_en_PW', 'goog.i18n.CompactNumberFormatSymbols_en_SG', 'goog.i18n.CompactNumberFormatSymbols_en_TC', 'goog.i18n.CompactNumberFormatSymbols_en_UM', 'goog.i18n.CompactNumberFormatSymbols_en_US', 'goog.i18n.CompactNumberFormatSymbols_en_VG', 'goog.i18n.CompactNumberFormatSymbols_en_VI', 'goog.i18n.CompactNumberFormatSymbols_en_ZA', 'goog.i18n.CompactNumberFormatSymbols_en_ZW', 'goog.i18n.CompactNumberFormatSymbols_es', 'goog.i18n.CompactNumberFormatSymbols_es_419', 'goog.i18n.CompactNumberFormatSymbols_es_EA', 'goog.i18n.CompactNumberFormatSymbols_es_ES', 'goog.i18n.CompactNumberFormatSymbols_es_IC', 'goog.i18n.CompactNumberFormatSymbols_et', 'goog.i18n.CompactNumberFormatSymbols_et_EE', 'goog.i18n.CompactNumberFormatSymbols_eu', 'goog.i18n.CompactNumberFormatSymbols_eu_ES', 'goog.i18n.CompactNumberFormatSymbols_fa', 'goog.i18n.CompactNumberFormatSymbols_fa_IR', 'goog.i18n.CompactNumberFormatSymbols_fi', 'goog.i18n.CompactNumberFormatSymbols_fi_FI', 'goog.i18n.CompactNumberFormatSymbols_fil', 'goog.i18n.CompactNumberFormatSymbols_fil_PH', 'goog.i18n.CompactNumberFormatSymbols_fr', 'goog.i18n.CompactNumberFormatSymbols_fr_BL', 'goog.i18n.CompactNumberFormatSymbols_fr_CA', 'goog.i18n.CompactNumberFormatSymbols_fr_FR', 'goog.i18n.CompactNumberFormatSymbols_fr_GF', 'goog.i18n.CompactNumberFormatSymbols_fr_GP', 'goog.i18n.CompactNumberFormatSymbols_fr_MC', 'goog.i18n.CompactNumberFormatSymbols_fr_MF', 'goog.i18n.CompactNumberFormatSymbols_fr_MQ', 'goog.i18n.CompactNumberFormatSymbols_fr_PM', 'goog.i18n.CompactNumberFormatSymbols_fr_RE', 'goog.i18n.CompactNumberFormatSymbols_fr_YT', 'goog.i18n.CompactNumberFormatSymbols_gl', 'goog.i18n.CompactNumberFormatSymbols_gl_ES', 'goog.i18n.CompactNumberFormatSymbols_gsw', 'goog.i18n.CompactNumberFormatSymbols_gsw_CH', 'goog.i18n.CompactNumberFormatSymbols_gsw_LI', 'goog.i18n.CompactNumberFormatSymbols_gu', 'goog.i18n.CompactNumberFormatSymbols_gu_IN', 'goog.i18n.CompactNumberFormatSymbols_haw', 'goog.i18n.CompactNumberFormatSymbols_haw_US', 'goog.i18n.CompactNumberFormatSymbols_he', 'goog.i18n.CompactNumberFormatSymbols_he_IL', 'goog.i18n.CompactNumberFormatSymbols_hi', 'goog.i18n.CompactNumberFormatSymbols_hi_IN', 'goog.i18n.CompactNumberFormatSymbols_hr', 'goog.i18n.CompactNumberFormatSymbols_hr_HR', 'goog.i18n.CompactNumberFormatSymbols_hu', 'goog.i18n.CompactNumberFormatSymbols_hu_HU', 'goog.i18n.CompactNumberFormatSymbols_hy', 'goog.i18n.CompactNumberFormatSymbols_hy_AM', 'goog.i18n.CompactNumberFormatSymbols_id', 'goog.i18n.CompactNumberFormatSymbols_id_ID', 'goog.i18n.CompactNumberFormatSymbols_in', 'goog.i18n.CompactNumberFormatSymbols_is', 'goog.i18n.CompactNumberFormatSymbols_is_IS', 'goog.i18n.CompactNumberFormatSymbols_it', 'goog.i18n.CompactNumberFormatSymbols_it_IT', 'goog.i18n.CompactNumberFormatSymbols_it_SM', 'goog.i18n.CompactNumberFormatSymbols_iw', 'goog.i18n.CompactNumberFormatSymbols_ja', 'goog.i18n.CompactNumberFormatSymbols_ja_JP', 'goog.i18n.CompactNumberFormatSymbols_ka', 'goog.i18n.CompactNumberFormatSymbols_ka_GE', 'goog.i18n.CompactNumberFormatSymbols_kk', 'goog.i18n.CompactNumberFormatSymbols_kk_Cyrl_KZ', 'goog.i18n.CompactNumberFormatSymbols_km', 'goog.i18n.CompactNumberFormatSymbols_km_KH', 'goog.i18n.CompactNumberFormatSymbols_kn', 'goog.i18n.CompactNumberFormatSymbols_kn_IN', 'goog.i18n.CompactNumberFormatSymbols_ko', 'goog.i18n.CompactNumberFormatSymbols_ko_KR', 'goog.i18n.CompactNumberFormatSymbols_ky', 'goog.i18n.CompactNumberFormatSymbols_ky_Cyrl_KG', 'goog.i18n.CompactNumberFormatSymbols_ln', 'goog.i18n.CompactNumberFormatSymbols_ln_CD', 'goog.i18n.CompactNumberFormatSymbols_lo', 'goog.i18n.CompactNumberFormatSymbols_lo_LA', 'goog.i18n.CompactNumberFormatSymbols_lt', 'goog.i18n.CompactNumberFormatSymbols_lt_LT', 'goog.i18n.CompactNumberFormatSymbols_lv', 'goog.i18n.CompactNumberFormatSymbols_lv_LV', 'goog.i18n.CompactNumberFormatSymbols_mk', 'goog.i18n.CompactNumberFormatSymbols_mk_MK', 'goog.i18n.CompactNumberFormatSymbols_ml', 'goog.i18n.CompactNumberFormatSymbols_ml_IN', 'goog.i18n.CompactNumberFormatSymbols_mn', 'goog.i18n.CompactNumberFormatSymbols_mn_Cyrl_MN', 'goog.i18n.CompactNumberFormatSymbols_mr', 'goog.i18n.CompactNumberFormatSymbols_mr_IN', 'goog.i18n.CompactNumberFormatSymbols_ms', 'goog.i18n.CompactNumberFormatSymbols_ms_Latn_MY', 'goog.i18n.CompactNumberFormatSymbols_mt', 'goog.i18n.CompactNumberFormatSymbols_mt_MT', 'goog.i18n.CompactNumberFormatSymbols_my', 'goog.i18n.CompactNumberFormatSymbols_my_MM', 'goog.i18n.CompactNumberFormatSymbols_nb', 'goog.i18n.CompactNumberFormatSymbols_nb_NO', 'goog.i18n.CompactNumberFormatSymbols_nb_SJ', 'goog.i18n.CompactNumberFormatSymbols_ne', 'goog.i18n.CompactNumberFormatSymbols_ne_NP', 'goog.i18n.CompactNumberFormatSymbols_nl', 'goog.i18n.CompactNumberFormatSymbols_nl_NL', 'goog.i18n.CompactNumberFormatSymbols_no', 'goog.i18n.CompactNumberFormatSymbols_no_NO', 'goog.i18n.CompactNumberFormatSymbols_or', 'goog.i18n.CompactNumberFormatSymbols_or_IN', 'goog.i18n.CompactNumberFormatSymbols_pa', 'goog.i18n.CompactNumberFormatSymbols_pa_Guru_IN', 'goog.i18n.CompactNumberFormatSymbols_pl', 'goog.i18n.CompactNumberFormatSymbols_pl_PL', 'goog.i18n.CompactNumberFormatSymbols_pt', 'goog.i18n.CompactNumberFormatSymbols_pt_BR', 'goog.i18n.CompactNumberFormatSymbols_pt_PT', 'goog.i18n.CompactNumberFormatSymbols_ro', 'goog.i18n.CompactNumberFormatSymbols_ro_RO', 'goog.i18n.CompactNumberFormatSymbols_ru', 'goog.i18n.CompactNumberFormatSymbols_ru_RU', 'goog.i18n.CompactNumberFormatSymbols_si', 'goog.i18n.CompactNumberFormatSymbols_si_LK', 'goog.i18n.CompactNumberFormatSymbols_sk', 'goog.i18n.CompactNumberFormatSymbols_sk_SK', 'goog.i18n.CompactNumberFormatSymbols_sl', 'goog.i18n.CompactNumberFormatSymbols_sl_SI', 'goog.i18n.CompactNumberFormatSymbols_sq', 'goog.i18n.CompactNumberFormatSymbols_sq_AL', 'goog.i18n.CompactNumberFormatSymbols_sr', 'goog.i18n.CompactNumberFormatSymbols_sr_Cyrl_RS', 'goog.i18n.CompactNumberFormatSymbols_sv', 'goog.i18n.CompactNumberFormatSymbols_sv_SE', 'goog.i18n.CompactNumberFormatSymbols_sw', 'goog.i18n.CompactNumberFormatSymbols_sw_TZ', 'goog.i18n.CompactNumberFormatSymbols_ta', 'goog.i18n.CompactNumberFormatSymbols_ta_IN', 'goog.i18n.CompactNumberFormatSymbols_te', 'goog.i18n.CompactNumberFormatSymbols_te_IN', 'goog.i18n.CompactNumberFormatSymbols_th', 'goog.i18n.CompactNumberFormatSymbols_th_TH', 'goog.i18n.CompactNumberFormatSymbols_tl', 'goog.i18n.CompactNumberFormatSymbols_tr', 'goog.i18n.CompactNumberFormatSymbols_tr_TR', 'goog.i18n.CompactNumberFormatSymbols_uk', 'goog.i18n.CompactNumberFormatSymbols_uk_UA', 'goog.i18n.CompactNumberFormatSymbols_ur', 'goog.i18n.CompactNumberFormatSymbols_ur_PK', 'goog.i18n.CompactNumberFormatSymbols_uz', 'goog.i18n.CompactNumberFormatSymbols_uz_Latn_UZ', 'goog.i18n.CompactNumberFormatSymbols_vi', 'goog.i18n.CompactNumberFormatSymbols_vi_VN', 'goog.i18n.CompactNumberFormatSymbols_zh', 'goog.i18n.CompactNumberFormatSymbols_zh_CN', 'goog.i18n.CompactNumberFormatSymbols_zh_HK', 'goog.i18n.CompactNumberFormatSymbols_zh_Hans_CN', 'goog.i18n.CompactNumberFormatSymbols_zh_TW', 'goog.i18n.CompactNumberFormatSymbols_zu', 'goog.i18n.CompactNumberFormatSymbols_zu_ZA'], []);
goog.addDependency('i18n/compactnumberformatsymbols_ext.js', ['goog.i18n.CompactNumberFormatSymbolsExt', 'goog.i18n.CompactNumberFormatSymbols_aa', 'goog.i18n.CompactNumberFormatSymbols_aa_DJ', 'goog.i18n.CompactNumberFormatSymbols_aa_ER', 'goog.i18n.CompactNumberFormatSymbols_aa_ET', 'goog.i18n.CompactNumberFormatSymbols_af_NA', 'goog.i18n.CompactNumberFormatSymbols_agq', 'goog.i18n.CompactNumberFormatSymbols_agq_CM', 'goog.i18n.CompactNumberFormatSymbols_ak', 'goog.i18n.CompactNumberFormatSymbols_ak_GH', 'goog.i18n.CompactNumberFormatSymbols_ar_AE', 'goog.i18n.CompactNumberFormatSymbols_ar_BH', 'goog.i18n.CompactNumberFormatSymbols_ar_DJ', 'goog.i18n.CompactNumberFormatSymbols_ar_DZ', 'goog.i18n.CompactNumberFormatSymbols_ar_EG', 'goog.i18n.CompactNumberFormatSymbols_ar_EH', 'goog.i18n.CompactNumberFormatSymbols_ar_ER', 'goog.i18n.CompactNumberFormatSymbols_ar_IL', 'goog.i18n.CompactNumberFormatSymbols_ar_IQ', 'goog.i18n.CompactNumberFormatSymbols_ar_JO', 'goog.i18n.CompactNumberFormatSymbols_ar_KM', 'goog.i18n.CompactNumberFormatSymbols_ar_KW', 'goog.i18n.CompactNumberFormatSymbols_ar_LB', 'goog.i18n.CompactNumberFormatSymbols_ar_LY', 'goog.i18n.CompactNumberFormatSymbols_ar_MA', 'goog.i18n.CompactNumberFormatSymbols_ar_MR', 'goog.i18n.CompactNumberFormatSymbols_ar_OM', 'goog.i18n.CompactNumberFormatSymbols_ar_PS', 'goog.i18n.CompactNumberFormatSymbols_ar_QA', 'goog.i18n.CompactNumberFormatSymbols_ar_SA', 'goog.i18n.CompactNumberFormatSymbols_ar_SD', 'goog.i18n.CompactNumberFormatSymbols_ar_SO', 'goog.i18n.CompactNumberFormatSymbols_ar_SS', 'goog.i18n.CompactNumberFormatSymbols_ar_SY', 'goog.i18n.CompactNumberFormatSymbols_ar_TD', 'goog.i18n.CompactNumberFormatSymbols_ar_TN', 'goog.i18n.CompactNumberFormatSymbols_ar_YE', 'goog.i18n.CompactNumberFormatSymbols_as', 'goog.i18n.CompactNumberFormatSymbols_as_IN', 'goog.i18n.CompactNumberFormatSymbols_asa', 'goog.i18n.CompactNumberFormatSymbols_asa_TZ', 'goog.i18n.CompactNumberFormatSymbols_ast', 'goog.i18n.CompactNumberFormatSymbols_ast_ES', 'goog.i18n.CompactNumberFormatSymbols_az_Cyrl', 'goog.i18n.CompactNumberFormatSymbols_az_Latn', 'goog.i18n.CompactNumberFormatSymbols_bas', 'goog.i18n.CompactNumberFormatSymbols_bas_CM', 'goog.i18n.CompactNumberFormatSymbols_be', 'goog.i18n.CompactNumberFormatSymbols_be_BY', 'goog.i18n.CompactNumberFormatSymbols_bem', 'goog.i18n.CompactNumberFormatSymbols_bem_ZM', 'goog.i18n.CompactNumberFormatSymbols_bez', 'goog.i18n.CompactNumberFormatSymbols_bez_TZ', 'goog.i18n.CompactNumberFormatSymbols_bm', 'goog.i18n.CompactNumberFormatSymbols_bm_ML', 'goog.i18n.CompactNumberFormatSymbols_bn_IN', 'goog.i18n.CompactNumberFormatSymbols_bo', 'goog.i18n.CompactNumberFormatSymbols_bo_CN', 'goog.i18n.CompactNumberFormatSymbols_bo_IN', 'goog.i18n.CompactNumberFormatSymbols_brx', 'goog.i18n.CompactNumberFormatSymbols_brx_IN', 'goog.i18n.CompactNumberFormatSymbols_bs', 'goog.i18n.CompactNumberFormatSymbols_bs_Cyrl', 'goog.i18n.CompactNumberFormatSymbols_bs_Cyrl_BA', 'goog.i18n.CompactNumberFormatSymbols_bs_Latn', 'goog.i18n.CompactNumberFormatSymbols_bs_Latn_BA', 'goog.i18n.CompactNumberFormatSymbols_byn', 'goog.i18n.CompactNumberFormatSymbols_byn_ER', 'goog.i18n.CompactNumberFormatSymbols_cgg', 'goog.i18n.CompactNumberFormatSymbols_cgg_UG', 'goog.i18n.CompactNumberFormatSymbols_ckb', 'goog.i18n.CompactNumberFormatSymbols_ckb_Arab', 'goog.i18n.CompactNumberFormatSymbols_ckb_Arab_IQ', 'goog.i18n.CompactNumberFormatSymbols_ckb_Arab_IR', 'goog.i18n.CompactNumberFormatSymbols_ckb_IQ', 'goog.i18n.CompactNumberFormatSymbols_ckb_IR', 'goog.i18n.CompactNumberFormatSymbols_ckb_Latn', 'goog.i18n.CompactNumberFormatSymbols_ckb_Latn_IQ', 'goog.i18n.CompactNumberFormatSymbols_dav', 'goog.i18n.CompactNumberFormatSymbols_dav_KE', 'goog.i18n.CompactNumberFormatSymbols_de_LI', 'goog.i18n.CompactNumberFormatSymbols_dje', 'goog.i18n.CompactNumberFormatSymbols_dje_NE', 'goog.i18n.CompactNumberFormatSymbols_dua', 'goog.i18n.CompactNumberFormatSymbols_dua_CM', 'goog.i18n.CompactNumberFormatSymbols_dyo', 'goog.i18n.CompactNumberFormatSymbols_dyo_SN', 'goog.i18n.CompactNumberFormatSymbols_dz', 'goog.i18n.CompactNumberFormatSymbols_dz_BT', 'goog.i18n.CompactNumberFormatSymbols_ebu', 'goog.i18n.CompactNumberFormatSymbols_ebu_KE', 'goog.i18n.CompactNumberFormatSymbols_ee', 'goog.i18n.CompactNumberFormatSymbols_ee_GH', 'goog.i18n.CompactNumberFormatSymbols_ee_TG', 'goog.i18n.CompactNumberFormatSymbols_el_CY', 'goog.i18n.CompactNumberFormatSymbols_en_150', 'goog.i18n.CompactNumberFormatSymbols_en_AG', 'goog.i18n.CompactNumberFormatSymbols_en_AI', 'goog.i18n.CompactNumberFormatSymbols_en_BB', 'goog.i18n.CompactNumberFormatSymbols_en_BE', 'goog.i18n.CompactNumberFormatSymbols_en_BM', 'goog.i18n.CompactNumberFormatSymbols_en_BS', 'goog.i18n.CompactNumberFormatSymbols_en_BW', 'goog.i18n.CompactNumberFormatSymbols_en_BZ', 'goog.i18n.CompactNumberFormatSymbols_en_CA', 'goog.i18n.CompactNumberFormatSymbols_en_CC', 'goog.i18n.CompactNumberFormatSymbols_en_CK', 'goog.i18n.CompactNumberFormatSymbols_en_CM', 'goog.i18n.CompactNumberFormatSymbols_en_CX', 'goog.i18n.CompactNumberFormatSymbols_en_DM', 'goog.i18n.CompactNumberFormatSymbols_en_ER', 'goog.i18n.CompactNumberFormatSymbols_en_FJ', 'goog.i18n.CompactNumberFormatSymbols_en_FK', 'goog.i18n.CompactNumberFormatSymbols_en_GD', 'goog.i18n.CompactNumberFormatSymbols_en_GG', 'goog.i18n.CompactNumberFormatSymbols_en_GH', 'goog.i18n.CompactNumberFormatSymbols_en_GI', 'goog.i18n.CompactNumberFormatSymbols_en_GM', 'goog.i18n.CompactNumberFormatSymbols_en_GY', 'goog.i18n.CompactNumberFormatSymbols_en_HK', 'goog.i18n.CompactNumberFormatSymbols_en_IM', 'goog.i18n.CompactNumberFormatSymbols_en_JE', 'goog.i18n.CompactNumberFormatSymbols_en_JM', 'goog.i18n.CompactNumberFormatSymbols_en_KE', 'goog.i18n.CompactNumberFormatSymbols_en_KI', 'goog.i18n.CompactNumberFormatSymbols_en_KN', 'goog.i18n.CompactNumberFormatSymbols_en_KY', 'goog.i18n.CompactNumberFormatSymbols_en_LC', 'goog.i18n.CompactNumberFormatSymbols_en_LR', 'goog.i18n.CompactNumberFormatSymbols_en_LS', 'goog.i18n.CompactNumberFormatSymbols_en_MG', 'goog.i18n.CompactNumberFormatSymbols_en_MO', 'goog.i18n.CompactNumberFormatSymbols_en_MS', 'goog.i18n.CompactNumberFormatSymbols_en_MT', 'goog.i18n.CompactNumberFormatSymbols_en_MU', 'goog.i18n.CompactNumberFormatSymbols_en_MW', 'goog.i18n.CompactNumberFormatSymbols_en_NA', 'goog.i18n.CompactNumberFormatSymbols_en_NF', 'goog.i18n.CompactNumberFormatSymbols_en_NG', 'goog.i18n.CompactNumberFormatSymbols_en_NR', 'goog.i18n.CompactNumberFormatSymbols_en_NU', 'goog.i18n.CompactNumberFormatSymbols_en_NZ', 'goog.i18n.CompactNumberFormatSymbols_en_PG', 'goog.i18n.CompactNumberFormatSymbols_en_PH', 'goog.i18n.CompactNumberFormatSymbols_en_PK', 'goog.i18n.CompactNumberFormatSymbols_en_PN', 'goog.i18n.CompactNumberFormatSymbols_en_RW', 'goog.i18n.CompactNumberFormatSymbols_en_SB', 'goog.i18n.CompactNumberFormatSymbols_en_SC', 'goog.i18n.CompactNumberFormatSymbols_en_SD', 'goog.i18n.CompactNumberFormatSymbols_en_SH', 'goog.i18n.CompactNumberFormatSymbols_en_SL', 'goog.i18n.CompactNumberFormatSymbols_en_SS', 'goog.i18n.CompactNumberFormatSymbols_en_SX', 'goog.i18n.CompactNumberFormatSymbols_en_SZ', 'goog.i18n.CompactNumberFormatSymbols_en_TK', 'goog.i18n.CompactNumberFormatSymbols_en_TO', 'goog.i18n.CompactNumberFormatSymbols_en_TT', 'goog.i18n.CompactNumberFormatSymbols_en_TV', 'goog.i18n.CompactNumberFormatSymbols_en_TZ', 'goog.i18n.CompactNumberFormatSymbols_en_UG', 'goog.i18n.CompactNumberFormatSymbols_en_VC', 'goog.i18n.CompactNumberFormatSymbols_en_VU', 'goog.i18n.CompactNumberFormatSymbols_en_WS', 'goog.i18n.CompactNumberFormatSymbols_en_ZM', 'goog.i18n.CompactNumberFormatSymbols_eo', 'goog.i18n.CompactNumberFormatSymbols_eo_001', 'goog.i18n.CompactNumberFormatSymbols_es_AR', 'goog.i18n.CompactNumberFormatSymbols_es_BO', 'goog.i18n.CompactNumberFormatSymbols_es_CL', 'goog.i18n.CompactNumberFormatSymbols_es_CO', 'goog.i18n.CompactNumberFormatSymbols_es_CR', 'goog.i18n.CompactNumberFormatSymbols_es_CU', 'goog.i18n.CompactNumberFormatSymbols_es_DO', 'goog.i18n.CompactNumberFormatSymbols_es_EC', 'goog.i18n.CompactNumberFormatSymbols_es_GQ', 'goog.i18n.CompactNumberFormatSymbols_es_GT', 'goog.i18n.CompactNumberFormatSymbols_es_HN', 'goog.i18n.CompactNumberFormatSymbols_es_MX', 'goog.i18n.CompactNumberFormatSymbols_es_NI', 'goog.i18n.CompactNumberFormatSymbols_es_PA', 'goog.i18n.CompactNumberFormatSymbols_es_PE', 'goog.i18n.CompactNumberFormatSymbols_es_PH', 'goog.i18n.CompactNumberFormatSymbols_es_PR', 'goog.i18n.CompactNumberFormatSymbols_es_PY', 'goog.i18n.CompactNumberFormatSymbols_es_SV', 'goog.i18n.CompactNumberFormatSymbols_es_US', 'goog.i18n.CompactNumberFormatSymbols_es_UY', 'goog.i18n.CompactNumberFormatSymbols_es_VE', 'goog.i18n.CompactNumberFormatSymbols_ewo', 'goog.i18n.CompactNumberFormatSymbols_ewo_CM', 'goog.i18n.CompactNumberFormatSymbols_fa_AF', 'goog.i18n.CompactNumberFormatSymbols_ff', 'goog.i18n.CompactNumberFormatSymbols_ff_CM', 'goog.i18n.CompactNumberFormatSymbols_ff_GN', 'goog.i18n.CompactNumberFormatSymbols_ff_MR', 'goog.i18n.CompactNumberFormatSymbols_ff_SN', 'goog.i18n.CompactNumberFormatSymbols_fo', 'goog.i18n.CompactNumberFormatSymbols_fo_FO', 'goog.i18n.CompactNumberFormatSymbols_fr_BE', 'goog.i18n.CompactNumberFormatSymbols_fr_BF', 'goog.i18n.CompactNumberFormatSymbols_fr_BI', 'goog.i18n.CompactNumberFormatSymbols_fr_BJ', 'goog.i18n.CompactNumberFormatSymbols_fr_CD', 'goog.i18n.CompactNumberFormatSymbols_fr_CF', 'goog.i18n.CompactNumberFormatSymbols_fr_CG', 'goog.i18n.CompactNumberFormatSymbols_fr_CH', 'goog.i18n.CompactNumberFormatSymbols_fr_CI', 'goog.i18n.CompactNumberFormatSymbols_fr_CM', 'goog.i18n.CompactNumberFormatSymbols_fr_DJ', 'goog.i18n.CompactNumberFormatSymbols_fr_DZ', 'goog.i18n.CompactNumberFormatSymbols_fr_GA', 'goog.i18n.CompactNumberFormatSymbols_fr_GN', 'goog.i18n.CompactNumberFormatSymbols_fr_GQ', 'goog.i18n.CompactNumberFormatSymbols_fr_HT', 'goog.i18n.CompactNumberFormatSymbols_fr_KM', 'goog.i18n.CompactNumberFormatSymbols_fr_LU', 'goog.i18n.CompactNumberFormatSymbols_fr_MA', 'goog.i18n.CompactNumberFormatSymbols_fr_MG', 'goog.i18n.CompactNumberFormatSymbols_fr_ML', 'goog.i18n.CompactNumberFormatSymbols_fr_MR', 'goog.i18n.CompactNumberFormatSymbols_fr_MU', 'goog.i18n.CompactNumberFormatSymbols_fr_NC', 'goog.i18n.CompactNumberFormatSymbols_fr_NE', 'goog.i18n.CompactNumberFormatSymbols_fr_PF', 'goog.i18n.CompactNumberFormatSymbols_fr_RW', 'goog.i18n.CompactNumberFormatSymbols_fr_SC', 'goog.i18n.CompactNumberFormatSymbols_fr_SN', 'goog.i18n.CompactNumberFormatSymbols_fr_SY', 'goog.i18n.CompactNumberFormatSymbols_fr_TD', 'goog.i18n.CompactNumberFormatSymbols_fr_TG', 'goog.i18n.CompactNumberFormatSymbols_fr_TN', 'goog.i18n.CompactNumberFormatSymbols_fr_VU', 'goog.i18n.CompactNumberFormatSymbols_fr_WF', 'goog.i18n.CompactNumberFormatSymbols_fur', 'goog.i18n.CompactNumberFormatSymbols_fur_IT', 'goog.i18n.CompactNumberFormatSymbols_fy', 'goog.i18n.CompactNumberFormatSymbols_fy_NL', 'goog.i18n.CompactNumberFormatSymbols_ga', 'goog.i18n.CompactNumberFormatSymbols_ga_IE', 'goog.i18n.CompactNumberFormatSymbols_gd', 'goog.i18n.CompactNumberFormatSymbols_gd_GB', 'goog.i18n.CompactNumberFormatSymbols_guz', 'goog.i18n.CompactNumberFormatSymbols_guz_KE', 'goog.i18n.CompactNumberFormatSymbols_gv', 'goog.i18n.CompactNumberFormatSymbols_gv_IM', 'goog.i18n.CompactNumberFormatSymbols_ha', 'goog.i18n.CompactNumberFormatSymbols_ha_Latn', 'goog.i18n.CompactNumberFormatSymbols_ha_Latn_GH', 'goog.i18n.CompactNumberFormatSymbols_ha_Latn_NE', 'goog.i18n.CompactNumberFormatSymbols_ha_Latn_NG', 'goog.i18n.CompactNumberFormatSymbols_hr_BA', 'goog.i18n.CompactNumberFormatSymbols_ia', 'goog.i18n.CompactNumberFormatSymbols_ia_FR', 'goog.i18n.CompactNumberFormatSymbols_ig', 'goog.i18n.CompactNumberFormatSymbols_ig_NG', 'goog.i18n.CompactNumberFormatSymbols_ii', 'goog.i18n.CompactNumberFormatSymbols_ii_CN', 'goog.i18n.CompactNumberFormatSymbols_it_CH', 'goog.i18n.CompactNumberFormatSymbols_jgo', 'goog.i18n.CompactNumberFormatSymbols_jgo_CM', 'goog.i18n.CompactNumberFormatSymbols_jmc', 'goog.i18n.CompactNumberFormatSymbols_jmc_TZ', 'goog.i18n.CompactNumberFormatSymbols_kab', 'goog.i18n.CompactNumberFormatSymbols_kab_DZ', 'goog.i18n.CompactNumberFormatSymbols_kam', 'goog.i18n.CompactNumberFormatSymbols_kam_KE', 'goog.i18n.CompactNumberFormatSymbols_kde', 'goog.i18n.CompactNumberFormatSymbols_kde_TZ', 'goog.i18n.CompactNumberFormatSymbols_kea', 'goog.i18n.CompactNumberFormatSymbols_kea_CV', 'goog.i18n.CompactNumberFormatSymbols_khq', 'goog.i18n.CompactNumberFormatSymbols_khq_ML', 'goog.i18n.CompactNumberFormatSymbols_ki', 'goog.i18n.CompactNumberFormatSymbols_ki_KE', 'goog.i18n.CompactNumberFormatSymbols_kk_Cyrl', 'goog.i18n.CompactNumberFormatSymbols_kkj', 'goog.i18n.CompactNumberFormatSymbols_kkj_CM', 'goog.i18n.CompactNumberFormatSymbols_kl', 'goog.i18n.CompactNumberFormatSymbols_kl_GL', 'goog.i18n.CompactNumberFormatSymbols_kln', 'goog.i18n.CompactNumberFormatSymbols_kln_KE', 'goog.i18n.CompactNumberFormatSymbols_ko_KP', 'goog.i18n.CompactNumberFormatSymbols_kok', 'goog.i18n.CompactNumberFormatSymbols_kok_IN', 'goog.i18n.CompactNumberFormatSymbols_ks', 'goog.i18n.CompactNumberFormatSymbols_ks_Arab', 'goog.i18n.CompactNumberFormatSymbols_ks_Arab_IN', 'goog.i18n.CompactNumberFormatSymbols_ksb', 'goog.i18n.CompactNumberFormatSymbols_ksb_TZ', 'goog.i18n.CompactNumberFormatSymbols_ksf', 'goog.i18n.CompactNumberFormatSymbols_ksf_CM', 'goog.i18n.CompactNumberFormatSymbols_ksh', 'goog.i18n.CompactNumberFormatSymbols_ksh_DE', 'goog.i18n.CompactNumberFormatSymbols_kw', 'goog.i18n.CompactNumberFormatSymbols_kw_GB', 'goog.i18n.CompactNumberFormatSymbols_ky_Cyrl', 'goog.i18n.CompactNumberFormatSymbols_lag', 'goog.i18n.CompactNumberFormatSymbols_lag_TZ', 'goog.i18n.CompactNumberFormatSymbols_lg', 'goog.i18n.CompactNumberFormatSymbols_lg_UG', 'goog.i18n.CompactNumberFormatSymbols_lkt', 'goog.i18n.CompactNumberFormatSymbols_lkt_US', 'goog.i18n.CompactNumberFormatSymbols_ln_AO', 'goog.i18n.CompactNumberFormatSymbols_ln_CF', 'goog.i18n.CompactNumberFormatSymbols_ln_CG', 'goog.i18n.CompactNumberFormatSymbols_lu', 'goog.i18n.CompactNumberFormatSymbols_lu_CD', 'goog.i18n.CompactNumberFormatSymbols_luo', 'goog.i18n.CompactNumberFormatSymbols_luo_KE', 'goog.i18n.CompactNumberFormatSymbols_luy', 'goog.i18n.CompactNumberFormatSymbols_luy_KE', 'goog.i18n.CompactNumberFormatSymbols_mas', 'goog.i18n.CompactNumberFormatSymbols_mas_KE', 'goog.i18n.CompactNumberFormatSymbols_mas_TZ', 'goog.i18n.CompactNumberFormatSymbols_mer', 'goog.i18n.CompactNumberFormatSymbols_mer_KE', 'goog.i18n.CompactNumberFormatSymbols_mfe', 'goog.i18n.CompactNumberFormatSymbols_mfe_MU', 'goog.i18n.CompactNumberFormatSymbols_mg', 'goog.i18n.CompactNumberFormatSymbols_mg_MG', 'goog.i18n.CompactNumberFormatSymbols_mgh', 'goog.i18n.CompactNumberFormatSymbols_mgh_MZ', 'goog.i18n.CompactNumberFormatSymbols_mgo', 'goog.i18n.CompactNumberFormatSymbols_mgo_CM', 'goog.i18n.CompactNumberFormatSymbols_mn_Cyrl', 'goog.i18n.CompactNumberFormatSymbols_ms_Latn', 'goog.i18n.CompactNumberFormatSymbols_ms_Latn_BN', 'goog.i18n.CompactNumberFormatSymbols_ms_Latn_SG', 'goog.i18n.CompactNumberFormatSymbols_mua', 'goog.i18n.CompactNumberFormatSymbols_mua_CM', 'goog.i18n.CompactNumberFormatSymbols_naq', 'goog.i18n.CompactNumberFormatSymbols_naq_NA', 'goog.i18n.CompactNumberFormatSymbols_nd', 'goog.i18n.CompactNumberFormatSymbols_nd_ZW', 'goog.i18n.CompactNumberFormatSymbols_ne_IN', 'goog.i18n.CompactNumberFormatSymbols_nl_AW', 'goog.i18n.CompactNumberFormatSymbols_nl_BE', 'goog.i18n.CompactNumberFormatSymbols_nl_BQ', 'goog.i18n.CompactNumberFormatSymbols_nl_CW', 'goog.i18n.CompactNumberFormatSymbols_nl_SR', 'goog.i18n.CompactNumberFormatSymbols_nl_SX', 'goog.i18n.CompactNumberFormatSymbols_nmg', 'goog.i18n.CompactNumberFormatSymbols_nmg_CM', 'goog.i18n.CompactNumberFormatSymbols_nn', 'goog.i18n.CompactNumberFormatSymbols_nn_NO', 'goog.i18n.CompactNumberFormatSymbols_nnh', 'goog.i18n.CompactNumberFormatSymbols_nnh_CM', 'goog.i18n.CompactNumberFormatSymbols_nr', 'goog.i18n.CompactNumberFormatSymbols_nr_ZA', 'goog.i18n.CompactNumberFormatSymbols_nso', 'goog.i18n.CompactNumberFormatSymbols_nso_ZA', 'goog.i18n.CompactNumberFormatSymbols_nus', 'goog.i18n.CompactNumberFormatSymbols_nus_SD', 'goog.i18n.CompactNumberFormatSymbols_nyn', 'goog.i18n.CompactNumberFormatSymbols_nyn_UG', 'goog.i18n.CompactNumberFormatSymbols_om', 'goog.i18n.CompactNumberFormatSymbols_om_ET', 'goog.i18n.CompactNumberFormatSymbols_om_KE', 'goog.i18n.CompactNumberFormatSymbols_os', 'goog.i18n.CompactNumberFormatSymbols_os_GE', 'goog.i18n.CompactNumberFormatSymbols_os_RU', 'goog.i18n.CompactNumberFormatSymbols_pa_Arab', 'goog.i18n.CompactNumberFormatSymbols_pa_Arab_PK', 'goog.i18n.CompactNumberFormatSymbols_pa_Guru', 'goog.i18n.CompactNumberFormatSymbols_ps', 'goog.i18n.CompactNumberFormatSymbols_ps_AF', 'goog.i18n.CompactNumberFormatSymbols_pt_AO', 'goog.i18n.CompactNumberFormatSymbols_pt_CV', 'goog.i18n.CompactNumberFormatSymbols_pt_GW', 'goog.i18n.CompactNumberFormatSymbols_pt_MO', 'goog.i18n.CompactNumberFormatSymbols_pt_MZ', 'goog.i18n.CompactNumberFormatSymbols_pt_ST', 'goog.i18n.CompactNumberFormatSymbols_pt_TL', 'goog.i18n.CompactNumberFormatSymbols_rm', 'goog.i18n.CompactNumberFormatSymbols_rm_CH', 'goog.i18n.CompactNumberFormatSymbols_rn', 'goog.i18n.CompactNumberFormatSymbols_rn_BI', 'goog.i18n.CompactNumberFormatSymbols_ro_MD', 'goog.i18n.CompactNumberFormatSymbols_rof', 'goog.i18n.CompactNumberFormatSymbols_rof_TZ', 'goog.i18n.CompactNumberFormatSymbols_ru_BY', 'goog.i18n.CompactNumberFormatSymbols_ru_KG', 'goog.i18n.CompactNumberFormatSymbols_ru_KZ', 'goog.i18n.CompactNumberFormatSymbols_ru_MD', 'goog.i18n.CompactNumberFormatSymbols_ru_UA', 'goog.i18n.CompactNumberFormatSymbols_rw', 'goog.i18n.CompactNumberFormatSymbols_rw_RW', 'goog.i18n.CompactNumberFormatSymbols_rwk', 'goog.i18n.CompactNumberFormatSymbols_rwk_TZ', 'goog.i18n.CompactNumberFormatSymbols_sah', 'goog.i18n.CompactNumberFormatSymbols_sah_RU', 'goog.i18n.CompactNumberFormatSymbols_saq', 'goog.i18n.CompactNumberFormatSymbols_saq_KE', 'goog.i18n.CompactNumberFormatSymbols_sbp', 'goog.i18n.CompactNumberFormatSymbols_sbp_TZ', 'goog.i18n.CompactNumberFormatSymbols_se', 'goog.i18n.CompactNumberFormatSymbols_se_FI', 'goog.i18n.CompactNumberFormatSymbols_se_NO', 'goog.i18n.CompactNumberFormatSymbols_seh', 'goog.i18n.CompactNumberFormatSymbols_seh_MZ', 'goog.i18n.CompactNumberFormatSymbols_ses', 'goog.i18n.CompactNumberFormatSymbols_ses_ML', 'goog.i18n.CompactNumberFormatSymbols_sg', 'goog.i18n.CompactNumberFormatSymbols_sg_CF', 'goog.i18n.CompactNumberFormatSymbols_shi', 'goog.i18n.CompactNumberFormatSymbols_shi_Latn', 'goog.i18n.CompactNumberFormatSymbols_shi_Latn_MA', 'goog.i18n.CompactNumberFormatSymbols_shi_Tfng', 'goog.i18n.CompactNumberFormatSymbols_shi_Tfng_MA', 'goog.i18n.CompactNumberFormatSymbols_sn', 'goog.i18n.CompactNumberFormatSymbols_sn_ZW', 'goog.i18n.CompactNumberFormatSymbols_so', 'goog.i18n.CompactNumberFormatSymbols_so_DJ', 'goog.i18n.CompactNumberFormatSymbols_so_ET', 'goog.i18n.CompactNumberFormatSymbols_so_KE', 'goog.i18n.CompactNumberFormatSymbols_so_SO', 'goog.i18n.CompactNumberFormatSymbols_sq_MK', 'goog.i18n.CompactNumberFormatSymbols_sq_XK', 'goog.i18n.CompactNumberFormatSymbols_sr_Cyrl', 'goog.i18n.CompactNumberFormatSymbols_sr_Cyrl_BA', 'goog.i18n.CompactNumberFormatSymbols_sr_Cyrl_ME', 'goog.i18n.CompactNumberFormatSymbols_sr_Cyrl_XK', 'goog.i18n.CompactNumberFormatSymbols_sr_Latn', 'goog.i18n.CompactNumberFormatSymbols_sr_Latn_BA', 'goog.i18n.CompactNumberFormatSymbols_sr_Latn_ME', 'goog.i18n.CompactNumberFormatSymbols_sr_Latn_RS', 'goog.i18n.CompactNumberFormatSymbols_sr_Latn_XK', 'goog.i18n.CompactNumberFormatSymbols_ss', 'goog.i18n.CompactNumberFormatSymbols_ss_SZ', 'goog.i18n.CompactNumberFormatSymbols_ss_ZA', 'goog.i18n.CompactNumberFormatSymbols_ssy', 'goog.i18n.CompactNumberFormatSymbols_ssy_ER', 'goog.i18n.CompactNumberFormatSymbols_st', 'goog.i18n.CompactNumberFormatSymbols_st_LS', 'goog.i18n.CompactNumberFormatSymbols_st_ZA', 'goog.i18n.CompactNumberFormatSymbols_sv_AX', 'goog.i18n.CompactNumberFormatSymbols_sv_FI', 'goog.i18n.CompactNumberFormatSymbols_sw_KE', 'goog.i18n.CompactNumberFormatSymbols_sw_UG', 'goog.i18n.CompactNumberFormatSymbols_swc', 'goog.i18n.CompactNumberFormatSymbols_swc_CD', 'goog.i18n.CompactNumberFormatSymbols_ta_LK', 'goog.i18n.CompactNumberFormatSymbols_ta_MY', 'goog.i18n.CompactNumberFormatSymbols_ta_SG', 'goog.i18n.CompactNumberFormatSymbols_teo', 'goog.i18n.CompactNumberFormatSymbols_teo_KE', 'goog.i18n.CompactNumberFormatSymbols_teo_UG', 'goog.i18n.CompactNumberFormatSymbols_tg', 'goog.i18n.CompactNumberFormatSymbols_tg_Cyrl', 'goog.i18n.CompactNumberFormatSymbols_tg_Cyrl_TJ', 'goog.i18n.CompactNumberFormatSymbols_ti', 'goog.i18n.CompactNumberFormatSymbols_ti_ER', 'goog.i18n.CompactNumberFormatSymbols_ti_ET', 'goog.i18n.CompactNumberFormatSymbols_tig', 'goog.i18n.CompactNumberFormatSymbols_tig_ER', 'goog.i18n.CompactNumberFormatSymbols_tn', 'goog.i18n.CompactNumberFormatSymbols_tn_BW', 'goog.i18n.CompactNumberFormatSymbols_tn_ZA', 'goog.i18n.CompactNumberFormatSymbols_to', 'goog.i18n.CompactNumberFormatSymbols_to_TO', 'goog.i18n.CompactNumberFormatSymbols_tr_CY', 'goog.i18n.CompactNumberFormatSymbols_ts', 'goog.i18n.CompactNumberFormatSymbols_ts_ZA', 'goog.i18n.CompactNumberFormatSymbols_twq', 'goog.i18n.CompactNumberFormatSymbols_twq_NE', 'goog.i18n.CompactNumberFormatSymbols_tzm', 'goog.i18n.CompactNumberFormatSymbols_tzm_Latn', 'goog.i18n.CompactNumberFormatSymbols_tzm_Latn_MA', 'goog.i18n.CompactNumberFormatSymbols_ug', 'goog.i18n.CompactNumberFormatSymbols_ug_Arab', 'goog.i18n.CompactNumberFormatSymbols_ug_Arab_CN', 'goog.i18n.CompactNumberFormatSymbols_ur_IN', 'goog.i18n.CompactNumberFormatSymbols_uz_Arab', 'goog.i18n.CompactNumberFormatSymbols_uz_Arab_AF', 'goog.i18n.CompactNumberFormatSymbols_uz_Cyrl', 'goog.i18n.CompactNumberFormatSymbols_uz_Cyrl_UZ', 'goog.i18n.CompactNumberFormatSymbols_uz_Latn', 'goog.i18n.CompactNumberFormatSymbols_vai', 'goog.i18n.CompactNumberFormatSymbols_vai_Latn', 'goog.i18n.CompactNumberFormatSymbols_vai_Latn_LR', 'goog.i18n.CompactNumberFormatSymbols_vai_Vaii', 'goog.i18n.CompactNumberFormatSymbols_vai_Vaii_LR', 'goog.i18n.CompactNumberFormatSymbols_ve', 'goog.i18n.CompactNumberFormatSymbols_ve_ZA', 'goog.i18n.CompactNumberFormatSymbols_vo', 'goog.i18n.CompactNumberFormatSymbols_vo_001', 'goog.i18n.CompactNumberFormatSymbols_vun', 'goog.i18n.CompactNumberFormatSymbols_vun_TZ', 'goog.i18n.CompactNumberFormatSymbols_wae', 'goog.i18n.CompactNumberFormatSymbols_wae_CH', 'goog.i18n.CompactNumberFormatSymbols_wal', 'goog.i18n.CompactNumberFormatSymbols_wal_ET', 'goog.i18n.CompactNumberFormatSymbols_xh', 'goog.i18n.CompactNumberFormatSymbols_xh_ZA', 'goog.i18n.CompactNumberFormatSymbols_xog', 'goog.i18n.CompactNumberFormatSymbols_xog_UG', 'goog.i18n.CompactNumberFormatSymbols_yav', 'goog.i18n.CompactNumberFormatSymbols_yav_CM', 'goog.i18n.CompactNumberFormatSymbols_yo', 'goog.i18n.CompactNumberFormatSymbols_yo_BJ', 'goog.i18n.CompactNumberFormatSymbols_yo_NG', 'goog.i18n.CompactNumberFormatSymbols_zgh', 'goog.i18n.CompactNumberFormatSymbols_zgh_MA', 'goog.i18n.CompactNumberFormatSymbols_zh_Hans', 'goog.i18n.CompactNumberFormatSymbols_zh_Hans_HK', 'goog.i18n.CompactNumberFormatSymbols_zh_Hans_MO', 'goog.i18n.CompactNumberFormatSymbols_zh_Hans_SG', 'goog.i18n.CompactNumberFormatSymbols_zh_Hant', 'goog.i18n.CompactNumberFormatSymbols_zh_Hant_HK', 'goog.i18n.CompactNumberFormatSymbols_zh_Hant_MO', 'goog.i18n.CompactNumberFormatSymbols_zh_Hant_TW'], []);
goog.addDependency('i18n/currency.js', ['goog.i18n.currency', 'goog.i18n.currency.CurrencyInfo', 'goog.i18n.currency.CurrencyInfoTier2'], []);
goog.addDependency('i18n/currency_test.js', ['goog.i18n.currencyTest'], ['goog.i18n.NumberFormat', 'goog.i18n.currency', 'goog.i18n.currency.CurrencyInfo', 'goog.object', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit']);
goog.addDependency('i18n/currencycodemap.js', ['goog.i18n.currencyCodeMap', 'goog.i18n.currencyCodeMapTier2'], []);
goog.addDependency('i18n/datetimeformat.js', ['goog.i18n.DateTimeFormat', 'goog.i18n.DateTimeFormat.Format'], ['goog.asserts', 'goog.date', 'goog.i18n.DateTimeSymbols', 'goog.i18n.TimeZone', 'goog.string']);
goog.addDependency('i18n/datetimeformat_test.js', ['goog.i18n.DateTimeFormatTest'], ['goog.date.Date', 'goog.date.DateTime', 'goog.i18n.DateTimeFormat', 'goog.i18n.DateTimePatterns', 'goog.i18n.DateTimePatterns_de', 'goog.i18n.DateTimePatterns_en', 'goog.i18n.DateTimePatterns_fa', 'goog.i18n.DateTimePatterns_fr', 'goog.i18n.DateTimePatterns_ja', 'goog.i18n.DateTimePatterns_sv', 'goog.i18n.DateTimeSymbols', 'goog.i18n.DateTimeSymbols_ar_AE', 'goog.i18n.DateTimeSymbols_ar_SA', 'goog.i18n.DateTimeSymbols_bn_BD', 'goog.i18n.DateTimeSymbols_de', 'goog.i18n.DateTimeSymbols_en', 'goog.i18n.DateTimeSymbols_en_GB', 'goog.i18n.DateTimeSymbols_en_IE', 'goog.i18n.DateTimeSymbols_en_IN', 'goog.i18n.DateTimeSymbols_en_US', 'goog.i18n.DateTimeSymbols_fa', 'goog.i18n.DateTimeSymbols_fr', 'goog.i18n.DateTimeSymbols_fr_DJ', 'goog.i18n.DateTimeSymbols_he_IL', 'goog.i18n.DateTimeSymbols_ja', 'goog.i18n.DateTimeSymbols_ro_RO', 'goog.i18n.DateTimeSymbols_sv', 'goog.i18n.TimeZone', 'goog.testing.jsunit']);
goog.addDependency('i18n/datetimeparse.js', ['goog.i18n.DateTimeParse'], ['goog.date', 'goog.i18n.DateTimeFormat', 'goog.i18n.DateTimeSymbols']);
goog.addDependency('i18n/datetimeparse_test.js', ['goog.i18n.DateTimeParseTest'], ['goog.date.Date', 'goog.i18n.DateTimeFormat', 'goog.i18n.DateTimeParse', 'goog.i18n.DateTimeSymbols', 'goog.i18n.DateTimeSymbols_en', 'goog.i18n.DateTimeSymbols_fa', 'goog.i18n.DateTimeSymbols_fr', 'goog.i18n.DateTimeSymbols_pl', 'goog.i18n.DateTimeSymbols_zh', 'goog.testing.ExpectedFailures', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('i18n/datetimepatterns.js', ['goog.i18n.DateTimePatterns', 'goog.i18n.DateTimePatterns_af', 'goog.i18n.DateTimePatterns_am', 'goog.i18n.DateTimePatterns_ar', 'goog.i18n.DateTimePatterns_az', 'goog.i18n.DateTimePatterns_bg', 'goog.i18n.DateTimePatterns_bn', 'goog.i18n.DateTimePatterns_br', 'goog.i18n.DateTimePatterns_ca', 'goog.i18n.DateTimePatterns_chr', 'goog.i18n.DateTimePatterns_cs', 'goog.i18n.DateTimePatterns_cy', 'goog.i18n.DateTimePatterns_da', 'goog.i18n.DateTimePatterns_de', 'goog.i18n.DateTimePatterns_de_AT', 'goog.i18n.DateTimePatterns_de_CH', 'goog.i18n.DateTimePatterns_el', 'goog.i18n.DateTimePatterns_en', 'goog.i18n.DateTimePatterns_en_AU', 'goog.i18n.DateTimePatterns_en_GB', 'goog.i18n.DateTimePatterns_en_IE', 'goog.i18n.DateTimePatterns_en_IN', 'goog.i18n.DateTimePatterns_en_SG', 'goog.i18n.DateTimePatterns_en_US', 'goog.i18n.DateTimePatterns_en_ZA', 'goog.i18n.DateTimePatterns_es', 'goog.i18n.DateTimePatterns_es_419', 'goog.i18n.DateTimePatterns_es_ES', 'goog.i18n.DateTimePatterns_et', 'goog.i18n.DateTimePatterns_eu', 'goog.i18n.DateTimePatterns_fa', 'goog.i18n.DateTimePatterns_fi', 'goog.i18n.DateTimePatterns_fil', 'goog.i18n.DateTimePatterns_fr', 'goog.i18n.DateTimePatterns_fr_CA', 'goog.i18n.DateTimePatterns_gl', 'goog.i18n.DateTimePatterns_gsw', 'goog.i18n.DateTimePatterns_gu', 'goog.i18n.DateTimePatterns_haw', 'goog.i18n.DateTimePatterns_he', 'goog.i18n.DateTimePatterns_hi', 'goog.i18n.DateTimePatterns_hr', 'goog.i18n.DateTimePatterns_hu', 'goog.i18n.DateTimePatterns_hy', 'goog.i18n.DateTimePatterns_id', 'goog.i18n.DateTimePatterns_in', 'goog.i18n.DateTimePatterns_is', 'goog.i18n.DateTimePatterns_it', 'goog.i18n.DateTimePatterns_iw', 'goog.i18n.DateTimePatterns_ja', 'goog.i18n.DateTimePatterns_ka', 'goog.i18n.DateTimePatterns_kk', 'goog.i18n.DateTimePatterns_km', 'goog.i18n.DateTimePatterns_kn', 'goog.i18n.DateTimePatterns_ko', 'goog.i18n.DateTimePatterns_ky', 'goog.i18n.DateTimePatterns_ln', 'goog.i18n.DateTimePatterns_lo', 'goog.i18n.DateTimePatterns_lt', 'goog.i18n.DateTimePatterns_lv', 'goog.i18n.DateTimePatterns_mk', 'goog.i18n.DateTimePatterns_ml', 'goog.i18n.DateTimePatterns_mn', 'goog.i18n.DateTimePatterns_mo', 'goog.i18n.DateTimePatterns_mr', 'goog.i18n.DateTimePatterns_ms', 'goog.i18n.DateTimePatterns_mt', 'goog.i18n.DateTimePatterns_my', 'goog.i18n.DateTimePatterns_nb', 'goog.i18n.DateTimePatterns_ne', 'goog.i18n.DateTimePatterns_nl', 'goog.i18n.DateTimePatterns_no', 'goog.i18n.DateTimePatterns_no_NO', 'goog.i18n.DateTimePatterns_or', 'goog.i18n.DateTimePatterns_pa', 'goog.i18n.DateTimePatterns_pl', 'goog.i18n.DateTimePatterns_pt', 'goog.i18n.DateTimePatterns_pt_BR', 'goog.i18n.DateTimePatterns_pt_PT', 'goog.i18n.DateTimePatterns_ro', 'goog.i18n.DateTimePatterns_ru', 'goog.i18n.DateTimePatterns_sh', 'goog.i18n.DateTimePatterns_si', 'goog.i18n.DateTimePatterns_sk', 'goog.i18n.DateTimePatterns_sl', 'goog.i18n.DateTimePatterns_sq', 'goog.i18n.DateTimePatterns_sr', 'goog.i18n.DateTimePatterns_sv', 'goog.i18n.DateTimePatterns_sw', 'goog.i18n.DateTimePatterns_ta', 'goog.i18n.DateTimePatterns_te', 'goog.i18n.DateTimePatterns_th', 'goog.i18n.DateTimePatterns_tl', 'goog.i18n.DateTimePatterns_tr', 'goog.i18n.DateTimePatterns_uk', 'goog.i18n.DateTimePatterns_ur', 'goog.i18n.DateTimePatterns_uz', 'goog.i18n.DateTimePatterns_vi', 'goog.i18n.DateTimePatterns_zh', 'goog.i18n.DateTimePatterns_zh_CN', 'goog.i18n.DateTimePatterns_zh_HK', 'goog.i18n.DateTimePatterns_zh_TW', 'goog.i18n.DateTimePatterns_zu'], []);
goog.addDependency('i18n/datetimepatternsext.js', ['goog.i18n.DateTimePatternsExt', 'goog.i18n.DateTimePatterns_af_NA', 'goog.i18n.DateTimePatterns_af_ZA', 'goog.i18n.DateTimePatterns_agq', 'goog.i18n.DateTimePatterns_agq_CM', 'goog.i18n.DateTimePatterns_ak', 'goog.i18n.DateTimePatterns_ak_GH', 'goog.i18n.DateTimePatterns_am_ET', 'goog.i18n.DateTimePatterns_ar_001', 'goog.i18n.DateTimePatterns_ar_AE', 'goog.i18n.DateTimePatterns_ar_BH', 'goog.i18n.DateTimePatterns_ar_DJ', 'goog.i18n.DateTimePatterns_ar_DZ', 'goog.i18n.DateTimePatterns_ar_EG', 'goog.i18n.DateTimePatterns_ar_EH', 'goog.i18n.DateTimePatterns_ar_ER', 'goog.i18n.DateTimePatterns_ar_IL', 'goog.i18n.DateTimePatterns_ar_IQ', 'goog.i18n.DateTimePatterns_ar_JO', 'goog.i18n.DateTimePatterns_ar_KM', 'goog.i18n.DateTimePatterns_ar_KW', 'goog.i18n.DateTimePatterns_ar_LB', 'goog.i18n.DateTimePatterns_ar_LY', 'goog.i18n.DateTimePatterns_ar_MA', 'goog.i18n.DateTimePatterns_ar_MR', 'goog.i18n.DateTimePatterns_ar_OM', 'goog.i18n.DateTimePatterns_ar_PS', 'goog.i18n.DateTimePatterns_ar_QA', 'goog.i18n.DateTimePatterns_ar_SA', 'goog.i18n.DateTimePatterns_ar_SD', 'goog.i18n.DateTimePatterns_ar_SO', 'goog.i18n.DateTimePatterns_ar_SS', 'goog.i18n.DateTimePatterns_ar_SY', 'goog.i18n.DateTimePatterns_ar_TD', 'goog.i18n.DateTimePatterns_ar_TN', 'goog.i18n.DateTimePatterns_ar_YE', 'goog.i18n.DateTimePatterns_as', 'goog.i18n.DateTimePatterns_as_IN', 'goog.i18n.DateTimePatterns_asa', 'goog.i18n.DateTimePatterns_asa_TZ', 'goog.i18n.DateTimePatterns_az_Cyrl', 'goog.i18n.DateTimePatterns_az_Cyrl_AZ', 'goog.i18n.DateTimePatterns_az_Latn', 'goog.i18n.DateTimePatterns_az_Latn_AZ', 'goog.i18n.DateTimePatterns_bas', 'goog.i18n.DateTimePatterns_bas_CM', 'goog.i18n.DateTimePatterns_be', 'goog.i18n.DateTimePatterns_be_BY', 'goog.i18n.DateTimePatterns_bem', 'goog.i18n.DateTimePatterns_bem_ZM', 'goog.i18n.DateTimePatterns_bez', 'goog.i18n.DateTimePatterns_bez_TZ', 'goog.i18n.DateTimePatterns_bg_BG', 'goog.i18n.DateTimePatterns_bm', 'goog.i18n.DateTimePatterns_bm_ML', 'goog.i18n.DateTimePatterns_bn_BD', 'goog.i18n.DateTimePatterns_bn_IN', 'goog.i18n.DateTimePatterns_bo', 'goog.i18n.DateTimePatterns_bo_CN', 'goog.i18n.DateTimePatterns_bo_IN', 'goog.i18n.DateTimePatterns_br_FR', 'goog.i18n.DateTimePatterns_brx', 'goog.i18n.DateTimePatterns_brx_IN', 'goog.i18n.DateTimePatterns_bs', 'goog.i18n.DateTimePatterns_bs_Cyrl', 'goog.i18n.DateTimePatterns_bs_Cyrl_BA', 'goog.i18n.DateTimePatterns_bs_Latn', 'goog.i18n.DateTimePatterns_bs_Latn_BA', 'goog.i18n.DateTimePatterns_ca_AD', 'goog.i18n.DateTimePatterns_ca_ES', 'goog.i18n.DateTimePatterns_ca_FR', 'goog.i18n.DateTimePatterns_ca_IT', 'goog.i18n.DateTimePatterns_cgg', 'goog.i18n.DateTimePatterns_cgg_UG', 'goog.i18n.DateTimePatterns_chr_US', 'goog.i18n.DateTimePatterns_cs_CZ', 'goog.i18n.DateTimePatterns_cy_GB', 'goog.i18n.DateTimePatterns_da_DK', 'goog.i18n.DateTimePatterns_da_GL', 'goog.i18n.DateTimePatterns_dav', 'goog.i18n.DateTimePatterns_dav_KE', 'goog.i18n.DateTimePatterns_de_BE', 'goog.i18n.DateTimePatterns_de_DE', 'goog.i18n.DateTimePatterns_de_LI', 'goog.i18n.DateTimePatterns_de_LU', 'goog.i18n.DateTimePatterns_dje', 'goog.i18n.DateTimePatterns_dje_NE', 'goog.i18n.DateTimePatterns_dua', 'goog.i18n.DateTimePatterns_dua_CM', 'goog.i18n.DateTimePatterns_dyo', 'goog.i18n.DateTimePatterns_dyo_SN', 'goog.i18n.DateTimePatterns_dz', 'goog.i18n.DateTimePatterns_dz_BT', 'goog.i18n.DateTimePatterns_ebu', 'goog.i18n.DateTimePatterns_ebu_KE', 'goog.i18n.DateTimePatterns_ee', 'goog.i18n.DateTimePatterns_ee_GH', 'goog.i18n.DateTimePatterns_ee_TG', 'goog.i18n.DateTimePatterns_el_CY', 'goog.i18n.DateTimePatterns_el_GR', 'goog.i18n.DateTimePatterns_en_001', 'goog.i18n.DateTimePatterns_en_150', 'goog.i18n.DateTimePatterns_en_AG', 'goog.i18n.DateTimePatterns_en_AI', 'goog.i18n.DateTimePatterns_en_AS', 'goog.i18n.DateTimePatterns_en_BB', 'goog.i18n.DateTimePatterns_en_BE', 'goog.i18n.DateTimePatterns_en_BM', 'goog.i18n.DateTimePatterns_en_BS', 'goog.i18n.DateTimePatterns_en_BW', 'goog.i18n.DateTimePatterns_en_BZ', 'goog.i18n.DateTimePatterns_en_CA', 'goog.i18n.DateTimePatterns_en_CC', 'goog.i18n.DateTimePatterns_en_CK', 'goog.i18n.DateTimePatterns_en_CM', 'goog.i18n.DateTimePatterns_en_CX', 'goog.i18n.DateTimePatterns_en_DG', 'goog.i18n.DateTimePatterns_en_DM', 'goog.i18n.DateTimePatterns_en_ER', 'goog.i18n.DateTimePatterns_en_FJ', 'goog.i18n.DateTimePatterns_en_FK', 'goog.i18n.DateTimePatterns_en_FM', 'goog.i18n.DateTimePatterns_en_GD', 'goog.i18n.DateTimePatterns_en_GG', 'goog.i18n.DateTimePatterns_en_GH', 'goog.i18n.DateTimePatterns_en_GI', 'goog.i18n.DateTimePatterns_en_GM', 'goog.i18n.DateTimePatterns_en_GU', 'goog.i18n.DateTimePatterns_en_GY', 'goog.i18n.DateTimePatterns_en_HK', 'goog.i18n.DateTimePatterns_en_IM', 'goog.i18n.DateTimePatterns_en_IO', 'goog.i18n.DateTimePatterns_en_JE', 'goog.i18n.DateTimePatterns_en_JM', 'goog.i18n.DateTimePatterns_en_KE', 'goog.i18n.DateTimePatterns_en_KI', 'goog.i18n.DateTimePatterns_en_KN', 'goog.i18n.DateTimePatterns_en_KY', 'goog.i18n.DateTimePatterns_en_LC', 'goog.i18n.DateTimePatterns_en_LR', 'goog.i18n.DateTimePatterns_en_LS', 'goog.i18n.DateTimePatterns_en_MG', 'goog.i18n.DateTimePatterns_en_MH', 'goog.i18n.DateTimePatterns_en_MO', 'goog.i18n.DateTimePatterns_en_MP', 'goog.i18n.DateTimePatterns_en_MS', 'goog.i18n.DateTimePatterns_en_MT', 'goog.i18n.DateTimePatterns_en_MU', 'goog.i18n.DateTimePatterns_en_MW', 'goog.i18n.DateTimePatterns_en_NA', 'goog.i18n.DateTimePatterns_en_NF', 'goog.i18n.DateTimePatterns_en_NG', 'goog.i18n.DateTimePatterns_en_NR', 'goog.i18n.DateTimePatterns_en_NU', 'goog.i18n.DateTimePatterns_en_NZ', 'goog.i18n.DateTimePatterns_en_PG', 'goog.i18n.DateTimePatterns_en_PH', 'goog.i18n.DateTimePatterns_en_PK', 'goog.i18n.DateTimePatterns_en_PN', 'goog.i18n.DateTimePatterns_en_PR', 'goog.i18n.DateTimePatterns_en_PW', 'goog.i18n.DateTimePatterns_en_RW', 'goog.i18n.DateTimePatterns_en_SB', 'goog.i18n.DateTimePatterns_en_SC', 'goog.i18n.DateTimePatterns_en_SD', 'goog.i18n.DateTimePatterns_en_SH', 'goog.i18n.DateTimePatterns_en_SL', 'goog.i18n.DateTimePatterns_en_SS', 'goog.i18n.DateTimePatterns_en_SX', 'goog.i18n.DateTimePatterns_en_SZ', 'goog.i18n.DateTimePatterns_en_TC', 'goog.i18n.DateTimePatterns_en_TK', 'goog.i18n.DateTimePatterns_en_TO', 'goog.i18n.DateTimePatterns_en_TT', 'goog.i18n.DateTimePatterns_en_TV', 'goog.i18n.DateTimePatterns_en_TZ', 'goog.i18n.DateTimePatterns_en_UG', 'goog.i18n.DateTimePatterns_en_UM', 'goog.i18n.DateTimePatterns_en_US_POSIX', 'goog.i18n.DateTimePatterns_en_VC', 'goog.i18n.DateTimePatterns_en_VG', 'goog.i18n.DateTimePatterns_en_VI', 'goog.i18n.DateTimePatterns_en_VU', 'goog.i18n.DateTimePatterns_en_WS', 'goog.i18n.DateTimePatterns_en_ZM', 'goog.i18n.DateTimePatterns_en_ZW', 'goog.i18n.DateTimePatterns_eo', 'goog.i18n.DateTimePatterns_es_AR', 'goog.i18n.DateTimePatterns_es_BO', 'goog.i18n.DateTimePatterns_es_CL', 'goog.i18n.DateTimePatterns_es_CO', 'goog.i18n.DateTimePatterns_es_CR', 'goog.i18n.DateTimePatterns_es_CU', 'goog.i18n.DateTimePatterns_es_DO', 'goog.i18n.DateTimePatterns_es_EA', 'goog.i18n.DateTimePatterns_es_EC', 'goog.i18n.DateTimePatterns_es_GQ', 'goog.i18n.DateTimePatterns_es_GT', 'goog.i18n.DateTimePatterns_es_HN', 'goog.i18n.DateTimePatterns_es_IC', 'goog.i18n.DateTimePatterns_es_MX', 'goog.i18n.DateTimePatterns_es_NI', 'goog.i18n.DateTimePatterns_es_PA', 'goog.i18n.DateTimePatterns_es_PE', 'goog.i18n.DateTimePatterns_es_PH', 'goog.i18n.DateTimePatterns_es_PR', 'goog.i18n.DateTimePatterns_es_PY', 'goog.i18n.DateTimePatterns_es_SV', 'goog.i18n.DateTimePatterns_es_US', 'goog.i18n.DateTimePatterns_es_UY', 'goog.i18n.DateTimePatterns_es_VE', 'goog.i18n.DateTimePatterns_et_EE', 'goog.i18n.DateTimePatterns_eu_ES', 'goog.i18n.DateTimePatterns_ewo', 'goog.i18n.DateTimePatterns_ewo_CM', 'goog.i18n.DateTimePatterns_fa_AF', 'goog.i18n.DateTimePatterns_fa_IR', 'goog.i18n.DateTimePatterns_ff', 'goog.i18n.DateTimePatterns_ff_SN', 'goog.i18n.DateTimePatterns_fi_FI', 'goog.i18n.DateTimePatterns_fil_PH', 'goog.i18n.DateTimePatterns_fo', 'goog.i18n.DateTimePatterns_fo_FO', 'goog.i18n.DateTimePatterns_fr_BE', 'goog.i18n.DateTimePatterns_fr_BF', 'goog.i18n.DateTimePatterns_fr_BI', 'goog.i18n.DateTimePatterns_fr_BJ', 'goog.i18n.DateTimePatterns_fr_BL', 'goog.i18n.DateTimePatterns_fr_CD', 'goog.i18n.DateTimePatterns_fr_CF', 'goog.i18n.DateTimePatterns_fr_CG', 'goog.i18n.DateTimePatterns_fr_CH', 'goog.i18n.DateTimePatterns_fr_CI', 'goog.i18n.DateTimePatterns_fr_CM', 'goog.i18n.DateTimePatterns_fr_DJ', 'goog.i18n.DateTimePatterns_fr_DZ', 'goog.i18n.DateTimePatterns_fr_FR', 'goog.i18n.DateTimePatterns_fr_GA', 'goog.i18n.DateTimePatterns_fr_GF', 'goog.i18n.DateTimePatterns_fr_GN', 'goog.i18n.DateTimePatterns_fr_GP', 'goog.i18n.DateTimePatterns_fr_GQ', 'goog.i18n.DateTimePatterns_fr_HT', 'goog.i18n.DateTimePatterns_fr_KM', 'goog.i18n.DateTimePatterns_fr_LU', 'goog.i18n.DateTimePatterns_fr_MA', 'goog.i18n.DateTimePatterns_fr_MC', 'goog.i18n.DateTimePatterns_fr_MF', 'goog.i18n.DateTimePatterns_fr_MG', 'goog.i18n.DateTimePatterns_fr_ML', 'goog.i18n.DateTimePatterns_fr_MQ', 'goog.i18n.DateTimePatterns_fr_MR', 'goog.i18n.DateTimePatterns_fr_MU', 'goog.i18n.DateTimePatterns_fr_NC', 'goog.i18n.DateTimePatterns_fr_NE', 'goog.i18n.DateTimePatterns_fr_PF', 'goog.i18n.DateTimePatterns_fr_PM', 'goog.i18n.DateTimePatterns_fr_RE', 'goog.i18n.DateTimePatterns_fr_RW', 'goog.i18n.DateTimePatterns_fr_SC', 'goog.i18n.DateTimePatterns_fr_SN', 'goog.i18n.DateTimePatterns_fr_SY', 'goog.i18n.DateTimePatterns_fr_TD', 'goog.i18n.DateTimePatterns_fr_TG', 'goog.i18n.DateTimePatterns_fr_TN', 'goog.i18n.DateTimePatterns_fr_VU', 'goog.i18n.DateTimePatterns_fr_WF', 'goog.i18n.DateTimePatterns_fr_YT', 'goog.i18n.DateTimePatterns_ga', 'goog.i18n.DateTimePatterns_ga_IE', 'goog.i18n.DateTimePatterns_gl_ES', 'goog.i18n.DateTimePatterns_gsw_CH', 'goog.i18n.DateTimePatterns_gsw_LI', 'goog.i18n.DateTimePatterns_gu_IN', 'goog.i18n.DateTimePatterns_guz', 'goog.i18n.DateTimePatterns_guz_KE', 'goog.i18n.DateTimePatterns_gv', 'goog.i18n.DateTimePatterns_gv_IM', 'goog.i18n.DateTimePatterns_ha', 'goog.i18n.DateTimePatterns_ha_Latn', 'goog.i18n.DateTimePatterns_ha_Latn_GH', 'goog.i18n.DateTimePatterns_ha_Latn_NE', 'goog.i18n.DateTimePatterns_ha_Latn_NG', 'goog.i18n.DateTimePatterns_haw_US', 'goog.i18n.DateTimePatterns_he_IL', 'goog.i18n.DateTimePatterns_hi_IN', 'goog.i18n.DateTimePatterns_hr_BA', 'goog.i18n.DateTimePatterns_hr_HR', 'goog.i18n.DateTimePatterns_hu_HU', 'goog.i18n.DateTimePatterns_hy_AM', 'goog.i18n.DateTimePatterns_id_ID', 'goog.i18n.DateTimePatterns_ig', 'goog.i18n.DateTimePatterns_ig_NG', 'goog.i18n.DateTimePatterns_ii', 'goog.i18n.DateTimePatterns_ii_CN', 'goog.i18n.DateTimePatterns_is_IS', 'goog.i18n.DateTimePatterns_it_CH', 'goog.i18n.DateTimePatterns_it_IT', 'goog.i18n.DateTimePatterns_it_SM', 'goog.i18n.DateTimePatterns_ja_JP', 'goog.i18n.DateTimePatterns_jgo', 'goog.i18n.DateTimePatterns_jgo_CM', 'goog.i18n.DateTimePatterns_jmc', 'goog.i18n.DateTimePatterns_jmc_TZ', 'goog.i18n.DateTimePatterns_ka_GE', 'goog.i18n.DateTimePatterns_kab', 'goog.i18n.DateTimePatterns_kab_DZ', 'goog.i18n.DateTimePatterns_kam', 'goog.i18n.DateTimePatterns_kam_KE', 'goog.i18n.DateTimePatterns_kde', 'goog.i18n.DateTimePatterns_kde_TZ', 'goog.i18n.DateTimePatterns_kea', 'goog.i18n.DateTimePatterns_kea_CV', 'goog.i18n.DateTimePatterns_khq', 'goog.i18n.DateTimePatterns_khq_ML', 'goog.i18n.DateTimePatterns_ki', 'goog.i18n.DateTimePatterns_ki_KE', 'goog.i18n.DateTimePatterns_kk_Cyrl', 'goog.i18n.DateTimePatterns_kk_Cyrl_KZ', 'goog.i18n.DateTimePatterns_kkj', 'goog.i18n.DateTimePatterns_kkj_CM', 'goog.i18n.DateTimePatterns_kl', 'goog.i18n.DateTimePatterns_kl_GL', 'goog.i18n.DateTimePatterns_kln', 'goog.i18n.DateTimePatterns_kln_KE', 'goog.i18n.DateTimePatterns_km_KH', 'goog.i18n.DateTimePatterns_kn_IN', 'goog.i18n.DateTimePatterns_ko_KP', 'goog.i18n.DateTimePatterns_ko_KR', 'goog.i18n.DateTimePatterns_kok', 'goog.i18n.DateTimePatterns_kok_IN', 'goog.i18n.DateTimePatterns_ks', 'goog.i18n.DateTimePatterns_ks_Arab', 'goog.i18n.DateTimePatterns_ks_Arab_IN', 'goog.i18n.DateTimePatterns_ksb', 'goog.i18n.DateTimePatterns_ksb_TZ', 'goog.i18n.DateTimePatterns_ksf', 'goog.i18n.DateTimePatterns_ksf_CM', 'goog.i18n.DateTimePatterns_kw', 'goog.i18n.DateTimePatterns_kw_GB', 'goog.i18n.DateTimePatterns_ky_Cyrl', 'goog.i18n.DateTimePatterns_ky_Cyrl_KG', 'goog.i18n.DateTimePatterns_lag', 'goog.i18n.DateTimePatterns_lag_TZ', 'goog.i18n.DateTimePatterns_lg', 'goog.i18n.DateTimePatterns_lg_UG', 'goog.i18n.DateTimePatterns_lkt', 'goog.i18n.DateTimePatterns_lkt_US', 'goog.i18n.DateTimePatterns_ln_AO', 'goog.i18n.DateTimePatterns_ln_CD', 'goog.i18n.DateTimePatterns_ln_CF', 'goog.i18n.DateTimePatterns_ln_CG', 'goog.i18n.DateTimePatterns_lo_LA', 'goog.i18n.DateTimePatterns_lt_LT', 'goog.i18n.DateTimePatterns_lu', 'goog.i18n.DateTimePatterns_lu_CD', 'goog.i18n.DateTimePatterns_luo', 'goog.i18n.DateTimePatterns_luo_KE', 'goog.i18n.DateTimePatterns_luy', 'goog.i18n.DateTimePatterns_luy_KE', 'goog.i18n.DateTimePatterns_lv_LV', 'goog.i18n.DateTimePatterns_mas', 'goog.i18n.DateTimePatterns_mas_KE', 'goog.i18n.DateTimePatterns_mas_TZ', 'goog.i18n.DateTimePatterns_mer', 'goog.i18n.DateTimePatterns_mer_KE', 'goog.i18n.DateTimePatterns_mfe', 'goog.i18n.DateTimePatterns_mfe_MU', 'goog.i18n.DateTimePatterns_mg', 'goog.i18n.DateTimePatterns_mg_MG', 'goog.i18n.DateTimePatterns_mgh', 'goog.i18n.DateTimePatterns_mgh_MZ', 'goog.i18n.DateTimePatterns_mgo', 'goog.i18n.DateTimePatterns_mgo_CM', 'goog.i18n.DateTimePatterns_mk_MK', 'goog.i18n.DateTimePatterns_ml_IN', 'goog.i18n.DateTimePatterns_mn_Cyrl', 'goog.i18n.DateTimePatterns_mn_Cyrl_MN', 'goog.i18n.DateTimePatterns_mr_IN', 'goog.i18n.DateTimePatterns_ms_Latn', 'goog.i18n.DateTimePatterns_ms_Latn_BN', 'goog.i18n.DateTimePatterns_ms_Latn_MY', 'goog.i18n.DateTimePatterns_ms_Latn_SG', 'goog.i18n.DateTimePatterns_mt_MT', 'goog.i18n.DateTimePatterns_mua', 'goog.i18n.DateTimePatterns_mua_CM', 'goog.i18n.DateTimePatterns_my_MM', 'goog.i18n.DateTimePatterns_naq', 'goog.i18n.DateTimePatterns_naq_NA', 'goog.i18n.DateTimePatterns_nb_NO', 'goog.i18n.DateTimePatterns_nb_SJ', 'goog.i18n.DateTimePatterns_nd', 'goog.i18n.DateTimePatterns_nd_ZW', 'goog.i18n.DateTimePatterns_ne_IN', 'goog.i18n.DateTimePatterns_ne_NP', 'goog.i18n.DateTimePatterns_nl_AW', 'goog.i18n.DateTimePatterns_nl_BE', 'goog.i18n.DateTimePatterns_nl_BQ', 'goog.i18n.DateTimePatterns_nl_CW', 'goog.i18n.DateTimePatterns_nl_NL', 'goog.i18n.DateTimePatterns_nl_SR', 'goog.i18n.DateTimePatterns_nl_SX', 'goog.i18n.DateTimePatterns_nmg', 'goog.i18n.DateTimePatterns_nmg_CM', 'goog.i18n.DateTimePatterns_nn', 'goog.i18n.DateTimePatterns_nn_NO', 'goog.i18n.DateTimePatterns_nnh', 'goog.i18n.DateTimePatterns_nnh_CM', 'goog.i18n.DateTimePatterns_nus', 'goog.i18n.DateTimePatterns_nus_SD', 'goog.i18n.DateTimePatterns_nyn', 'goog.i18n.DateTimePatterns_nyn_UG', 'goog.i18n.DateTimePatterns_om', 'goog.i18n.DateTimePatterns_om_ET', 'goog.i18n.DateTimePatterns_om_KE', 'goog.i18n.DateTimePatterns_or_IN', 'goog.i18n.DateTimePatterns_pa_Arab', 'goog.i18n.DateTimePatterns_pa_Arab_PK', 'goog.i18n.DateTimePatterns_pa_Guru', 'goog.i18n.DateTimePatterns_pa_Guru_IN', 'goog.i18n.DateTimePatterns_pl_PL', 'goog.i18n.DateTimePatterns_ps', 'goog.i18n.DateTimePatterns_ps_AF', 'goog.i18n.DateTimePatterns_pt_AO', 'goog.i18n.DateTimePatterns_pt_CV', 'goog.i18n.DateTimePatterns_pt_GW', 'goog.i18n.DateTimePatterns_pt_MO', 'goog.i18n.DateTimePatterns_pt_MZ', 'goog.i18n.DateTimePatterns_pt_ST', 'goog.i18n.DateTimePatterns_pt_TL', 'goog.i18n.DateTimePatterns_rm', 'goog.i18n.DateTimePatterns_rm_CH', 'goog.i18n.DateTimePatterns_rn', 'goog.i18n.DateTimePatterns_rn_BI', 'goog.i18n.DateTimePatterns_ro_MD', 'goog.i18n.DateTimePatterns_ro_RO', 'goog.i18n.DateTimePatterns_rof', 'goog.i18n.DateTimePatterns_rof_TZ', 'goog.i18n.DateTimePatterns_ru_BY', 'goog.i18n.DateTimePatterns_ru_KG', 'goog.i18n.DateTimePatterns_ru_KZ', 'goog.i18n.DateTimePatterns_ru_MD', 'goog.i18n.DateTimePatterns_ru_RU', 'goog.i18n.DateTimePatterns_ru_UA', 'goog.i18n.DateTimePatterns_rw', 'goog.i18n.DateTimePatterns_rw_RW', 'goog.i18n.DateTimePatterns_rwk', 'goog.i18n.DateTimePatterns_rwk_TZ', 'goog.i18n.DateTimePatterns_saq', 'goog.i18n.DateTimePatterns_saq_KE', 'goog.i18n.DateTimePatterns_sbp', 'goog.i18n.DateTimePatterns_sbp_TZ', 'goog.i18n.DateTimePatterns_seh', 'goog.i18n.DateTimePatterns_seh_MZ', 'goog.i18n.DateTimePatterns_ses', 'goog.i18n.DateTimePatterns_ses_ML', 'goog.i18n.DateTimePatterns_sg', 'goog.i18n.DateTimePatterns_sg_CF', 'goog.i18n.DateTimePatterns_shi', 'goog.i18n.DateTimePatterns_shi_Latn', 'goog.i18n.DateTimePatterns_shi_Latn_MA', 'goog.i18n.DateTimePatterns_shi_Tfng', 'goog.i18n.DateTimePatterns_shi_Tfng_MA', 'goog.i18n.DateTimePatterns_si_LK', 'goog.i18n.DateTimePatterns_sk_SK', 'goog.i18n.DateTimePatterns_sl_SI', 'goog.i18n.DateTimePatterns_sn', 'goog.i18n.DateTimePatterns_sn_ZW', 'goog.i18n.DateTimePatterns_so', 'goog.i18n.DateTimePatterns_so_DJ', 'goog.i18n.DateTimePatterns_so_ET', 'goog.i18n.DateTimePatterns_so_KE', 'goog.i18n.DateTimePatterns_so_SO', 'goog.i18n.DateTimePatterns_sq_AL', 'goog.i18n.DateTimePatterns_sq_MK', 'goog.i18n.DateTimePatterns_sq_XK', 'goog.i18n.DateTimePatterns_sr_Cyrl', 'goog.i18n.DateTimePatterns_sr_Cyrl_BA', 'goog.i18n.DateTimePatterns_sr_Cyrl_ME', 'goog.i18n.DateTimePatterns_sr_Cyrl_RS', 'goog.i18n.DateTimePatterns_sr_Cyrl_XK', 'goog.i18n.DateTimePatterns_sr_Latn', 'goog.i18n.DateTimePatterns_sr_Latn_BA', 'goog.i18n.DateTimePatterns_sr_Latn_ME', 'goog.i18n.DateTimePatterns_sr_Latn_RS', 'goog.i18n.DateTimePatterns_sr_Latn_XK', 'goog.i18n.DateTimePatterns_sv_AX', 'goog.i18n.DateTimePatterns_sv_FI', 'goog.i18n.DateTimePatterns_sv_SE', 'goog.i18n.DateTimePatterns_sw_KE', 'goog.i18n.DateTimePatterns_sw_TZ', 'goog.i18n.DateTimePatterns_sw_UG', 'goog.i18n.DateTimePatterns_swc', 'goog.i18n.DateTimePatterns_swc_CD', 'goog.i18n.DateTimePatterns_ta_IN', 'goog.i18n.DateTimePatterns_ta_LK', 'goog.i18n.DateTimePatterns_ta_MY', 'goog.i18n.DateTimePatterns_ta_SG', 'goog.i18n.DateTimePatterns_te_IN', 'goog.i18n.DateTimePatterns_teo', 'goog.i18n.DateTimePatterns_teo_KE', 'goog.i18n.DateTimePatterns_teo_UG', 'goog.i18n.DateTimePatterns_th_TH', 'goog.i18n.DateTimePatterns_ti', 'goog.i18n.DateTimePatterns_ti_ER', 'goog.i18n.DateTimePatterns_ti_ET', 'goog.i18n.DateTimePatterns_to', 'goog.i18n.DateTimePatterns_to_TO', 'goog.i18n.DateTimePatterns_tr_CY', 'goog.i18n.DateTimePatterns_tr_TR', 'goog.i18n.DateTimePatterns_twq', 'goog.i18n.DateTimePatterns_twq_NE', 'goog.i18n.DateTimePatterns_tzm', 'goog.i18n.DateTimePatterns_tzm_Latn', 'goog.i18n.DateTimePatterns_tzm_Latn_MA', 'goog.i18n.DateTimePatterns_ug', 'goog.i18n.DateTimePatterns_ug_Arab', 'goog.i18n.DateTimePatterns_ug_Arab_CN', 'goog.i18n.DateTimePatterns_uk_UA', 'goog.i18n.DateTimePatterns_ur_IN', 'goog.i18n.DateTimePatterns_ur_PK', 'goog.i18n.DateTimePatterns_uz_Arab', 'goog.i18n.DateTimePatterns_uz_Arab_AF', 'goog.i18n.DateTimePatterns_uz_Cyrl', 'goog.i18n.DateTimePatterns_uz_Cyrl_UZ', 'goog.i18n.DateTimePatterns_uz_Latn', 'goog.i18n.DateTimePatterns_uz_Latn_UZ', 'goog.i18n.DateTimePatterns_vai', 'goog.i18n.DateTimePatterns_vai_Latn', 'goog.i18n.DateTimePatterns_vai_Latn_LR', 'goog.i18n.DateTimePatterns_vai_Vaii', 'goog.i18n.DateTimePatterns_vai_Vaii_LR', 'goog.i18n.DateTimePatterns_vi_VN', 'goog.i18n.DateTimePatterns_vun', 'goog.i18n.DateTimePatterns_vun_TZ', 'goog.i18n.DateTimePatterns_xog', 'goog.i18n.DateTimePatterns_xog_UG', 'goog.i18n.DateTimePatterns_yav', 'goog.i18n.DateTimePatterns_yav_CM', 'goog.i18n.DateTimePatterns_yo', 'goog.i18n.DateTimePatterns_yo_BJ', 'goog.i18n.DateTimePatterns_yo_NG', 'goog.i18n.DateTimePatterns_zgh', 'goog.i18n.DateTimePatterns_zgh_MA', 'goog.i18n.DateTimePatterns_zh_Hans', 'goog.i18n.DateTimePatterns_zh_Hans_CN', 'goog.i18n.DateTimePatterns_zh_Hans_HK', 'goog.i18n.DateTimePatterns_zh_Hans_MO', 'goog.i18n.DateTimePatterns_zh_Hans_SG', 'goog.i18n.DateTimePatterns_zh_Hant', 'goog.i18n.DateTimePatterns_zh_Hant_HK', 'goog.i18n.DateTimePatterns_zh_Hant_MO', 'goog.i18n.DateTimePatterns_zh_Hant_TW', 'goog.i18n.DateTimePatterns_zu_ZA'], ['goog.i18n.DateTimePatterns']);
goog.addDependency('i18n/datetimesymbols.js', ['goog.i18n.DateTimeSymbols', 'goog.i18n.DateTimeSymbols_af', 'goog.i18n.DateTimeSymbols_am', 'goog.i18n.DateTimeSymbols_ar', 'goog.i18n.DateTimeSymbols_az', 'goog.i18n.DateTimeSymbols_bg', 'goog.i18n.DateTimeSymbols_bn', 'goog.i18n.DateTimeSymbols_br', 'goog.i18n.DateTimeSymbols_ca', 'goog.i18n.DateTimeSymbols_chr', 'goog.i18n.DateTimeSymbols_cs', 'goog.i18n.DateTimeSymbols_cy', 'goog.i18n.DateTimeSymbols_da', 'goog.i18n.DateTimeSymbols_de', 'goog.i18n.DateTimeSymbols_de_AT', 'goog.i18n.DateTimeSymbols_de_CH', 'goog.i18n.DateTimeSymbols_el', 'goog.i18n.DateTimeSymbols_en', 'goog.i18n.DateTimeSymbols_en_AU', 'goog.i18n.DateTimeSymbols_en_GB', 'goog.i18n.DateTimeSymbols_en_IE', 'goog.i18n.DateTimeSymbols_en_IN', 'goog.i18n.DateTimeSymbols_en_ISO', 'goog.i18n.DateTimeSymbols_en_SG', 'goog.i18n.DateTimeSymbols_en_US', 'goog.i18n.DateTimeSymbols_en_ZA', 'goog.i18n.DateTimeSymbols_es', 'goog.i18n.DateTimeSymbols_es_419', 'goog.i18n.DateTimeSymbols_es_ES', 'goog.i18n.DateTimeSymbols_et', 'goog.i18n.DateTimeSymbols_eu', 'goog.i18n.DateTimeSymbols_fa', 'goog.i18n.DateTimeSymbols_fi', 'goog.i18n.DateTimeSymbols_fil', 'goog.i18n.DateTimeSymbols_fr', 'goog.i18n.DateTimeSymbols_fr_CA', 'goog.i18n.DateTimeSymbols_gl', 'goog.i18n.DateTimeSymbols_gsw', 'goog.i18n.DateTimeSymbols_gu', 'goog.i18n.DateTimeSymbols_haw', 'goog.i18n.DateTimeSymbols_he', 'goog.i18n.DateTimeSymbols_hi', 'goog.i18n.DateTimeSymbols_hr', 'goog.i18n.DateTimeSymbols_hu', 'goog.i18n.DateTimeSymbols_hy', 'goog.i18n.DateTimeSymbols_id', 'goog.i18n.DateTimeSymbols_in', 'goog.i18n.DateTimeSymbols_is', 'goog.i18n.DateTimeSymbols_it', 'goog.i18n.DateTimeSymbols_iw', 'goog.i18n.DateTimeSymbols_ja', 'goog.i18n.DateTimeSymbols_ka', 'goog.i18n.DateTimeSymbols_kk', 'goog.i18n.DateTimeSymbols_km', 'goog.i18n.DateTimeSymbols_kn', 'goog.i18n.DateTimeSymbols_ko', 'goog.i18n.DateTimeSymbols_ky', 'goog.i18n.DateTimeSymbols_ln', 'goog.i18n.DateTimeSymbols_lo', 'goog.i18n.DateTimeSymbols_lt', 'goog.i18n.DateTimeSymbols_lv', 'goog.i18n.DateTimeSymbols_mk', 'goog.i18n.DateTimeSymbols_ml', 'goog.i18n.DateTimeSymbols_mn', 'goog.i18n.DateTimeSymbols_mr', 'goog.i18n.DateTimeSymbols_ms', 'goog.i18n.DateTimeSymbols_mt', 'goog.i18n.DateTimeSymbols_my', 'goog.i18n.DateTimeSymbols_nb', 'goog.i18n.DateTimeSymbols_ne', 'goog.i18n.DateTimeSymbols_nl', 'goog.i18n.DateTimeSymbols_no', 'goog.i18n.DateTimeSymbols_no_NO', 'goog.i18n.DateTimeSymbols_or', 'goog.i18n.DateTimeSymbols_pa', 'goog.i18n.DateTimeSymbols_pl', 'goog.i18n.DateTimeSymbols_pt', 'goog.i18n.DateTimeSymbols_pt_BR', 'goog.i18n.DateTimeSymbols_pt_PT', 'goog.i18n.DateTimeSymbols_ro', 'goog.i18n.DateTimeSymbols_ru', 'goog.i18n.DateTimeSymbols_si', 'goog.i18n.DateTimeSymbols_sk', 'goog.i18n.DateTimeSymbols_sl', 'goog.i18n.DateTimeSymbols_sq', 'goog.i18n.DateTimeSymbols_sr', 'goog.i18n.DateTimeSymbols_sv', 'goog.i18n.DateTimeSymbols_sw', 'goog.i18n.DateTimeSymbols_ta', 'goog.i18n.DateTimeSymbols_te', 'goog.i18n.DateTimeSymbols_th', 'goog.i18n.DateTimeSymbols_tl', 'goog.i18n.DateTimeSymbols_tr', 'goog.i18n.DateTimeSymbols_uk', 'goog.i18n.DateTimeSymbols_ur', 'goog.i18n.DateTimeSymbols_uz', 'goog.i18n.DateTimeSymbols_vi', 'goog.i18n.DateTimeSymbols_zh', 'goog.i18n.DateTimeSymbols_zh_CN', 'goog.i18n.DateTimeSymbols_zh_HK', 'goog.i18n.DateTimeSymbols_zh_TW', 'goog.i18n.DateTimeSymbols_zu'], []);
goog.addDependency('i18n/datetimesymbolsext.js', ['goog.i18n.DateTimeSymbolsExt', 'goog.i18n.DateTimeSymbols_aa', 'goog.i18n.DateTimeSymbols_aa_DJ', 'goog.i18n.DateTimeSymbols_aa_ER', 'goog.i18n.DateTimeSymbols_aa_ET', 'goog.i18n.DateTimeSymbols_af_NA', 'goog.i18n.DateTimeSymbols_af_ZA', 'goog.i18n.DateTimeSymbols_agq', 'goog.i18n.DateTimeSymbols_agq_CM', 'goog.i18n.DateTimeSymbols_ak', 'goog.i18n.DateTimeSymbols_ak_GH', 'goog.i18n.DateTimeSymbols_am_ET', 'goog.i18n.DateTimeSymbols_ar_001', 'goog.i18n.DateTimeSymbols_ar_AE', 'goog.i18n.DateTimeSymbols_ar_BH', 'goog.i18n.DateTimeSymbols_ar_DJ', 'goog.i18n.DateTimeSymbols_ar_DZ', 'goog.i18n.DateTimeSymbols_ar_EG', 'goog.i18n.DateTimeSymbols_ar_EH', 'goog.i18n.DateTimeSymbols_ar_ER', 'goog.i18n.DateTimeSymbols_ar_IL', 'goog.i18n.DateTimeSymbols_ar_IQ', 'goog.i18n.DateTimeSymbols_ar_JO', 'goog.i18n.DateTimeSymbols_ar_KM', 'goog.i18n.DateTimeSymbols_ar_KW', 'goog.i18n.DateTimeSymbols_ar_LB', 'goog.i18n.DateTimeSymbols_ar_LY', 'goog.i18n.DateTimeSymbols_ar_MA', 'goog.i18n.DateTimeSymbols_ar_MR', 'goog.i18n.DateTimeSymbols_ar_OM', 'goog.i18n.DateTimeSymbols_ar_PS', 'goog.i18n.DateTimeSymbols_ar_QA', 'goog.i18n.DateTimeSymbols_ar_SA', 'goog.i18n.DateTimeSymbols_ar_SD', 'goog.i18n.DateTimeSymbols_ar_SO', 'goog.i18n.DateTimeSymbols_ar_SS', 'goog.i18n.DateTimeSymbols_ar_SY', 'goog.i18n.DateTimeSymbols_ar_TD', 'goog.i18n.DateTimeSymbols_ar_TN', 'goog.i18n.DateTimeSymbols_ar_YE', 'goog.i18n.DateTimeSymbols_as', 'goog.i18n.DateTimeSymbols_as_IN', 'goog.i18n.DateTimeSymbols_asa', 'goog.i18n.DateTimeSymbols_asa_TZ', 'goog.i18n.DateTimeSymbols_ast', 'goog.i18n.DateTimeSymbols_ast_ES', 'goog.i18n.DateTimeSymbols_az_Cyrl', 'goog.i18n.DateTimeSymbols_az_Cyrl_AZ', 'goog.i18n.DateTimeSymbols_az_Latn', 'goog.i18n.DateTimeSymbols_az_Latn_AZ', 'goog.i18n.DateTimeSymbols_bas', 'goog.i18n.DateTimeSymbols_bas_CM', 'goog.i18n.DateTimeSymbols_be', 'goog.i18n.DateTimeSymbols_be_BY', 'goog.i18n.DateTimeSymbols_bem', 'goog.i18n.DateTimeSymbols_bem_ZM', 'goog.i18n.DateTimeSymbols_bez', 'goog.i18n.DateTimeSymbols_bez_TZ', 'goog.i18n.DateTimeSymbols_bg_BG', 'goog.i18n.DateTimeSymbols_bm', 'goog.i18n.DateTimeSymbols_bm_ML', 'goog.i18n.DateTimeSymbols_bn_BD', 'goog.i18n.DateTimeSymbols_bn_IN', 'goog.i18n.DateTimeSymbols_bo', 'goog.i18n.DateTimeSymbols_bo_CN', 'goog.i18n.DateTimeSymbols_bo_IN', 'goog.i18n.DateTimeSymbols_br_FR', 'goog.i18n.DateTimeSymbols_brx', 'goog.i18n.DateTimeSymbols_brx_IN', 'goog.i18n.DateTimeSymbols_bs', 'goog.i18n.DateTimeSymbols_bs_Cyrl', 'goog.i18n.DateTimeSymbols_bs_Cyrl_BA', 'goog.i18n.DateTimeSymbols_bs_Latn', 'goog.i18n.DateTimeSymbols_bs_Latn_BA', 'goog.i18n.DateTimeSymbols_byn', 'goog.i18n.DateTimeSymbols_byn_ER', 'goog.i18n.DateTimeSymbols_ca_AD', 'goog.i18n.DateTimeSymbols_ca_ES', 'goog.i18n.DateTimeSymbols_ca_ES_VALENCIA', 'goog.i18n.DateTimeSymbols_ca_FR', 'goog.i18n.DateTimeSymbols_ca_IT', 'goog.i18n.DateTimeSymbols_cgg', 'goog.i18n.DateTimeSymbols_cgg_UG', 'goog.i18n.DateTimeSymbols_chr_US', 'goog.i18n.DateTimeSymbols_ckb', 'goog.i18n.DateTimeSymbols_ckb_Arab', 'goog.i18n.DateTimeSymbols_ckb_Arab_IQ', 'goog.i18n.DateTimeSymbols_ckb_Arab_IR', 'goog.i18n.DateTimeSymbols_ckb_IQ', 'goog.i18n.DateTimeSymbols_ckb_IR', 'goog.i18n.DateTimeSymbols_ckb_Latn', 'goog.i18n.DateTimeSymbols_ckb_Latn_IQ', 'goog.i18n.DateTimeSymbols_cs_CZ', 'goog.i18n.DateTimeSymbols_cy_GB', 'goog.i18n.DateTimeSymbols_da_DK', 'goog.i18n.DateTimeSymbols_da_GL', 'goog.i18n.DateTimeSymbols_dav', 'goog.i18n.DateTimeSymbols_dav_KE', 'goog.i18n.DateTimeSymbols_de_BE', 'goog.i18n.DateTimeSymbols_de_DE', 'goog.i18n.DateTimeSymbols_de_LI', 'goog.i18n.DateTimeSymbols_de_LU', 'goog.i18n.DateTimeSymbols_dje', 'goog.i18n.DateTimeSymbols_dje_NE', 'goog.i18n.DateTimeSymbols_dua', 'goog.i18n.DateTimeSymbols_dua_CM', 'goog.i18n.DateTimeSymbols_dyo', 'goog.i18n.DateTimeSymbols_dyo_SN', 'goog.i18n.DateTimeSymbols_dz', 'goog.i18n.DateTimeSymbols_dz_BT', 'goog.i18n.DateTimeSymbols_ebu', 'goog.i18n.DateTimeSymbols_ebu_KE', 'goog.i18n.DateTimeSymbols_ee', 'goog.i18n.DateTimeSymbols_ee_GH', 'goog.i18n.DateTimeSymbols_ee_TG', 'goog.i18n.DateTimeSymbols_el_CY', 'goog.i18n.DateTimeSymbols_el_GR', 'goog.i18n.DateTimeSymbols_en_001', 'goog.i18n.DateTimeSymbols_en_150', 'goog.i18n.DateTimeSymbols_en_AG', 'goog.i18n.DateTimeSymbols_en_AI', 'goog.i18n.DateTimeSymbols_en_AS', 'goog.i18n.DateTimeSymbols_en_BB', 'goog.i18n.DateTimeSymbols_en_BE', 'goog.i18n.DateTimeSymbols_en_BM', 'goog.i18n.DateTimeSymbols_en_BS', 'goog.i18n.DateTimeSymbols_en_BW', 'goog.i18n.DateTimeSymbols_en_BZ', 'goog.i18n.DateTimeSymbols_en_CA', 'goog.i18n.DateTimeSymbols_en_CC', 'goog.i18n.DateTimeSymbols_en_CK', 'goog.i18n.DateTimeSymbols_en_CM', 'goog.i18n.DateTimeSymbols_en_CX', 'goog.i18n.DateTimeSymbols_en_DG', 'goog.i18n.DateTimeSymbols_en_DM', 'goog.i18n.DateTimeSymbols_en_ER', 'goog.i18n.DateTimeSymbols_en_FJ', 'goog.i18n.DateTimeSymbols_en_FK', 'goog.i18n.DateTimeSymbols_en_FM', 'goog.i18n.DateTimeSymbols_en_GD', 'goog.i18n.DateTimeSymbols_en_GG', 'goog.i18n.DateTimeSymbols_en_GH', 'goog.i18n.DateTimeSymbols_en_GI', 'goog.i18n.DateTimeSymbols_en_GM', 'goog.i18n.DateTimeSymbols_en_GU', 'goog.i18n.DateTimeSymbols_en_GY', 'goog.i18n.DateTimeSymbols_en_HK', 'goog.i18n.DateTimeSymbols_en_IM', 'goog.i18n.DateTimeSymbols_en_IO', 'goog.i18n.DateTimeSymbols_en_JE', 'goog.i18n.DateTimeSymbols_en_JM', 'goog.i18n.DateTimeSymbols_en_KE', 'goog.i18n.DateTimeSymbols_en_KI', 'goog.i18n.DateTimeSymbols_en_KN', 'goog.i18n.DateTimeSymbols_en_KY', 'goog.i18n.DateTimeSymbols_en_LC', 'goog.i18n.DateTimeSymbols_en_LR', 'goog.i18n.DateTimeSymbols_en_LS', 'goog.i18n.DateTimeSymbols_en_MG', 'goog.i18n.DateTimeSymbols_en_MH', 'goog.i18n.DateTimeSymbols_en_MO', 'goog.i18n.DateTimeSymbols_en_MP', 'goog.i18n.DateTimeSymbols_en_MS', 'goog.i18n.DateTimeSymbols_en_MT', 'goog.i18n.DateTimeSymbols_en_MU', 'goog.i18n.DateTimeSymbols_en_MW', 'goog.i18n.DateTimeSymbols_en_NA', 'goog.i18n.DateTimeSymbols_en_NF', 'goog.i18n.DateTimeSymbols_en_NG', 'goog.i18n.DateTimeSymbols_en_NR', 'goog.i18n.DateTimeSymbols_en_NU', 'goog.i18n.DateTimeSymbols_en_NZ', 'goog.i18n.DateTimeSymbols_en_PG', 'goog.i18n.DateTimeSymbols_en_PH', 'goog.i18n.DateTimeSymbols_en_PK', 'goog.i18n.DateTimeSymbols_en_PN', 'goog.i18n.DateTimeSymbols_en_PR', 'goog.i18n.DateTimeSymbols_en_PW', 'goog.i18n.DateTimeSymbols_en_RW', 'goog.i18n.DateTimeSymbols_en_SB', 'goog.i18n.DateTimeSymbols_en_SC', 'goog.i18n.DateTimeSymbols_en_SD', 'goog.i18n.DateTimeSymbols_en_SH', 'goog.i18n.DateTimeSymbols_en_SL', 'goog.i18n.DateTimeSymbols_en_SS', 'goog.i18n.DateTimeSymbols_en_SX', 'goog.i18n.DateTimeSymbols_en_SZ', 'goog.i18n.DateTimeSymbols_en_TC', 'goog.i18n.DateTimeSymbols_en_TK', 'goog.i18n.DateTimeSymbols_en_TO', 'goog.i18n.DateTimeSymbols_en_TT', 'goog.i18n.DateTimeSymbols_en_TV', 'goog.i18n.DateTimeSymbols_en_TZ', 'goog.i18n.DateTimeSymbols_en_UG', 'goog.i18n.DateTimeSymbols_en_UM', 'goog.i18n.DateTimeSymbols_en_VC', 'goog.i18n.DateTimeSymbols_en_VG', 'goog.i18n.DateTimeSymbols_en_VI', 'goog.i18n.DateTimeSymbols_en_VU', 'goog.i18n.DateTimeSymbols_en_WS', 'goog.i18n.DateTimeSymbols_en_ZM', 'goog.i18n.DateTimeSymbols_en_ZW', 'goog.i18n.DateTimeSymbols_eo', 'goog.i18n.DateTimeSymbols_eo_001', 'goog.i18n.DateTimeSymbols_es_AR', 'goog.i18n.DateTimeSymbols_es_BO', 'goog.i18n.DateTimeSymbols_es_CL', 'goog.i18n.DateTimeSymbols_es_CO', 'goog.i18n.DateTimeSymbols_es_CR', 'goog.i18n.DateTimeSymbols_es_CU', 'goog.i18n.DateTimeSymbols_es_DO', 'goog.i18n.DateTimeSymbols_es_EA', 'goog.i18n.DateTimeSymbols_es_EC', 'goog.i18n.DateTimeSymbols_es_GQ', 'goog.i18n.DateTimeSymbols_es_GT', 'goog.i18n.DateTimeSymbols_es_HN', 'goog.i18n.DateTimeSymbols_es_IC', 'goog.i18n.DateTimeSymbols_es_MX', 'goog.i18n.DateTimeSymbols_es_NI', 'goog.i18n.DateTimeSymbols_es_PA', 'goog.i18n.DateTimeSymbols_es_PE', 'goog.i18n.DateTimeSymbols_es_PH', 'goog.i18n.DateTimeSymbols_es_PR', 'goog.i18n.DateTimeSymbols_es_PY', 'goog.i18n.DateTimeSymbols_es_SV', 'goog.i18n.DateTimeSymbols_es_US', 'goog.i18n.DateTimeSymbols_es_UY', 'goog.i18n.DateTimeSymbols_es_VE', 'goog.i18n.DateTimeSymbols_et_EE', 'goog.i18n.DateTimeSymbols_eu_ES', 'goog.i18n.DateTimeSymbols_ewo', 'goog.i18n.DateTimeSymbols_ewo_CM', 'goog.i18n.DateTimeSymbols_fa_AF', 'goog.i18n.DateTimeSymbols_fa_IR', 'goog.i18n.DateTimeSymbols_ff', 'goog.i18n.DateTimeSymbols_ff_CM', 'goog.i18n.DateTimeSymbols_ff_GN', 'goog.i18n.DateTimeSymbols_ff_MR', 'goog.i18n.DateTimeSymbols_ff_SN', 'goog.i18n.DateTimeSymbols_fi_FI', 'goog.i18n.DateTimeSymbols_fil_PH', 'goog.i18n.DateTimeSymbols_fo', 'goog.i18n.DateTimeSymbols_fo_FO', 'goog.i18n.DateTimeSymbols_fr_BE', 'goog.i18n.DateTimeSymbols_fr_BF', 'goog.i18n.DateTimeSymbols_fr_BI', 'goog.i18n.DateTimeSymbols_fr_BJ', 'goog.i18n.DateTimeSymbols_fr_BL', 'goog.i18n.DateTimeSymbols_fr_CD', 'goog.i18n.DateTimeSymbols_fr_CF', 'goog.i18n.DateTimeSymbols_fr_CG', 'goog.i18n.DateTimeSymbols_fr_CH', 'goog.i18n.DateTimeSymbols_fr_CI', 'goog.i18n.DateTimeSymbols_fr_CM', 'goog.i18n.DateTimeSymbols_fr_DJ', 'goog.i18n.DateTimeSymbols_fr_DZ', 'goog.i18n.DateTimeSymbols_fr_FR', 'goog.i18n.DateTimeSymbols_fr_GA', 'goog.i18n.DateTimeSymbols_fr_GF', 'goog.i18n.DateTimeSymbols_fr_GN', 'goog.i18n.DateTimeSymbols_fr_GP', 'goog.i18n.DateTimeSymbols_fr_GQ', 'goog.i18n.DateTimeSymbols_fr_HT', 'goog.i18n.DateTimeSymbols_fr_KM', 'goog.i18n.DateTimeSymbols_fr_LU', 'goog.i18n.DateTimeSymbols_fr_MA', 'goog.i18n.DateTimeSymbols_fr_MC', 'goog.i18n.DateTimeSymbols_fr_MF', 'goog.i18n.DateTimeSymbols_fr_MG', 'goog.i18n.DateTimeSymbols_fr_ML', 'goog.i18n.DateTimeSymbols_fr_MQ', 'goog.i18n.DateTimeSymbols_fr_MR', 'goog.i18n.DateTimeSymbols_fr_MU', 'goog.i18n.DateTimeSymbols_fr_NC', 'goog.i18n.DateTimeSymbols_fr_NE', 'goog.i18n.DateTimeSymbols_fr_PF', 'goog.i18n.DateTimeSymbols_fr_PM', 'goog.i18n.DateTimeSymbols_fr_RE', 'goog.i18n.DateTimeSymbols_fr_RW', 'goog.i18n.DateTimeSymbols_fr_SC', 'goog.i18n.DateTimeSymbols_fr_SN', 'goog.i18n.DateTimeSymbols_fr_SY', 'goog.i18n.DateTimeSymbols_fr_TD', 'goog.i18n.DateTimeSymbols_fr_TG', 'goog.i18n.DateTimeSymbols_fr_TN', 'goog.i18n.DateTimeSymbols_fr_VU', 'goog.i18n.DateTimeSymbols_fr_WF', 'goog.i18n.DateTimeSymbols_fr_YT', 'goog.i18n.DateTimeSymbols_fur', 'goog.i18n.DateTimeSymbols_fur_IT', 'goog.i18n.DateTimeSymbols_fy', 'goog.i18n.DateTimeSymbols_fy_NL', 'goog.i18n.DateTimeSymbols_ga', 'goog.i18n.DateTimeSymbols_ga_IE', 'goog.i18n.DateTimeSymbols_gd', 'goog.i18n.DateTimeSymbols_gd_GB', 'goog.i18n.DateTimeSymbols_gl_ES', 'goog.i18n.DateTimeSymbols_gsw_CH', 'goog.i18n.DateTimeSymbols_gsw_LI', 'goog.i18n.DateTimeSymbols_gu_IN', 'goog.i18n.DateTimeSymbols_guz', 'goog.i18n.DateTimeSymbols_guz_KE', 'goog.i18n.DateTimeSymbols_gv', 'goog.i18n.DateTimeSymbols_gv_IM', 'goog.i18n.DateTimeSymbols_ha', 'goog.i18n.DateTimeSymbols_ha_Latn', 'goog.i18n.DateTimeSymbols_ha_Latn_GH', 'goog.i18n.DateTimeSymbols_ha_Latn_NE', 'goog.i18n.DateTimeSymbols_ha_Latn_NG', 'goog.i18n.DateTimeSymbols_haw_US', 'goog.i18n.DateTimeSymbols_he_IL', 'goog.i18n.DateTimeSymbols_hi_IN', 'goog.i18n.DateTimeSymbols_hr_BA', 'goog.i18n.DateTimeSymbols_hr_HR', 'goog.i18n.DateTimeSymbols_hu_HU', 'goog.i18n.DateTimeSymbols_hy_AM', 'goog.i18n.DateTimeSymbols_ia', 'goog.i18n.DateTimeSymbols_ia_FR', 'goog.i18n.DateTimeSymbols_id_ID', 'goog.i18n.DateTimeSymbols_ig', 'goog.i18n.DateTimeSymbols_ig_NG', 'goog.i18n.DateTimeSymbols_ii', 'goog.i18n.DateTimeSymbols_ii_CN', 'goog.i18n.DateTimeSymbols_is_IS', 'goog.i18n.DateTimeSymbols_it_CH', 'goog.i18n.DateTimeSymbols_it_IT', 'goog.i18n.DateTimeSymbols_it_SM', 'goog.i18n.DateTimeSymbols_ja_JP', 'goog.i18n.DateTimeSymbols_jgo', 'goog.i18n.DateTimeSymbols_jgo_CM', 'goog.i18n.DateTimeSymbols_jmc', 'goog.i18n.DateTimeSymbols_jmc_TZ', 'goog.i18n.DateTimeSymbols_ka_GE', 'goog.i18n.DateTimeSymbols_kab', 'goog.i18n.DateTimeSymbols_kab_DZ', 'goog.i18n.DateTimeSymbols_kam', 'goog.i18n.DateTimeSymbols_kam_KE', 'goog.i18n.DateTimeSymbols_kde', 'goog.i18n.DateTimeSymbols_kde_TZ', 'goog.i18n.DateTimeSymbols_kea', 'goog.i18n.DateTimeSymbols_kea_CV', 'goog.i18n.DateTimeSymbols_khq', 'goog.i18n.DateTimeSymbols_khq_ML', 'goog.i18n.DateTimeSymbols_ki', 'goog.i18n.DateTimeSymbols_ki_KE', 'goog.i18n.DateTimeSymbols_kk_Cyrl', 'goog.i18n.DateTimeSymbols_kk_Cyrl_KZ', 'goog.i18n.DateTimeSymbols_kkj', 'goog.i18n.DateTimeSymbols_kkj_CM', 'goog.i18n.DateTimeSymbols_kl', 'goog.i18n.DateTimeSymbols_kl_GL', 'goog.i18n.DateTimeSymbols_kln', 'goog.i18n.DateTimeSymbols_kln_KE', 'goog.i18n.DateTimeSymbols_km_KH', 'goog.i18n.DateTimeSymbols_kn_IN', 'goog.i18n.DateTimeSymbols_ko_KP', 'goog.i18n.DateTimeSymbols_ko_KR', 'goog.i18n.DateTimeSymbols_kok', 'goog.i18n.DateTimeSymbols_kok_IN', 'goog.i18n.DateTimeSymbols_ks', 'goog.i18n.DateTimeSymbols_ks_Arab', 'goog.i18n.DateTimeSymbols_ks_Arab_IN', 'goog.i18n.DateTimeSymbols_ksb', 'goog.i18n.DateTimeSymbols_ksb_TZ', 'goog.i18n.DateTimeSymbols_ksf', 'goog.i18n.DateTimeSymbols_ksf_CM', 'goog.i18n.DateTimeSymbols_ksh', 'goog.i18n.DateTimeSymbols_ksh_DE', 'goog.i18n.DateTimeSymbols_kw', 'goog.i18n.DateTimeSymbols_kw_GB', 'goog.i18n.DateTimeSymbols_ky_Cyrl', 'goog.i18n.DateTimeSymbols_ky_Cyrl_KG', 'goog.i18n.DateTimeSymbols_lag', 'goog.i18n.DateTimeSymbols_lag_TZ', 'goog.i18n.DateTimeSymbols_lg', 'goog.i18n.DateTimeSymbols_lg_UG', 'goog.i18n.DateTimeSymbols_lkt', 'goog.i18n.DateTimeSymbols_lkt_US', 'goog.i18n.DateTimeSymbols_ln_AO', 'goog.i18n.DateTimeSymbols_ln_CD', 'goog.i18n.DateTimeSymbols_ln_CF', 'goog.i18n.DateTimeSymbols_ln_CG', 'goog.i18n.DateTimeSymbols_lo_LA', 'goog.i18n.DateTimeSymbols_lt_LT', 'goog.i18n.DateTimeSymbols_lu', 'goog.i18n.DateTimeSymbols_lu_CD', 'goog.i18n.DateTimeSymbols_luo', 'goog.i18n.DateTimeSymbols_luo_KE', 'goog.i18n.DateTimeSymbols_luy', 'goog.i18n.DateTimeSymbols_luy_KE', 'goog.i18n.DateTimeSymbols_lv_LV', 'goog.i18n.DateTimeSymbols_mas', 'goog.i18n.DateTimeSymbols_mas_KE', 'goog.i18n.DateTimeSymbols_mas_TZ', 'goog.i18n.DateTimeSymbols_mer', 'goog.i18n.DateTimeSymbols_mer_KE', 'goog.i18n.DateTimeSymbols_mfe', 'goog.i18n.DateTimeSymbols_mfe_MU', 'goog.i18n.DateTimeSymbols_mg', 'goog.i18n.DateTimeSymbols_mg_MG', 'goog.i18n.DateTimeSymbols_mgh', 'goog.i18n.DateTimeSymbols_mgh_MZ', 'goog.i18n.DateTimeSymbols_mgo', 'goog.i18n.DateTimeSymbols_mgo_CM', 'goog.i18n.DateTimeSymbols_mk_MK', 'goog.i18n.DateTimeSymbols_ml_IN', 'goog.i18n.DateTimeSymbols_mn_Cyrl', 'goog.i18n.DateTimeSymbols_mn_Cyrl_MN', 'goog.i18n.DateTimeSymbols_mr_IN', 'goog.i18n.DateTimeSymbols_ms_Latn', 'goog.i18n.DateTimeSymbols_ms_Latn_BN', 'goog.i18n.DateTimeSymbols_ms_Latn_MY', 'goog.i18n.DateTimeSymbols_ms_Latn_SG', 'goog.i18n.DateTimeSymbols_mt_MT', 'goog.i18n.DateTimeSymbols_mua', 'goog.i18n.DateTimeSymbols_mua_CM', 'goog.i18n.DateTimeSymbols_my_MM', 'goog.i18n.DateTimeSymbols_naq', 'goog.i18n.DateTimeSymbols_naq_NA', 'goog.i18n.DateTimeSymbols_nb_NO', 'goog.i18n.DateTimeSymbols_nb_SJ', 'goog.i18n.DateTimeSymbols_nd', 'goog.i18n.DateTimeSymbols_nd_ZW', 'goog.i18n.DateTimeSymbols_ne_IN', 'goog.i18n.DateTimeSymbols_ne_NP', 'goog.i18n.DateTimeSymbols_nl_AW', 'goog.i18n.DateTimeSymbols_nl_BE', 'goog.i18n.DateTimeSymbols_nl_BQ', 'goog.i18n.DateTimeSymbols_nl_CW', 'goog.i18n.DateTimeSymbols_nl_NL', 'goog.i18n.DateTimeSymbols_nl_SR', 'goog.i18n.DateTimeSymbols_nl_SX', 'goog.i18n.DateTimeSymbols_nmg', 'goog.i18n.DateTimeSymbols_nmg_CM', 'goog.i18n.DateTimeSymbols_nn', 'goog.i18n.DateTimeSymbols_nn_NO', 'goog.i18n.DateTimeSymbols_nnh', 'goog.i18n.DateTimeSymbols_nnh_CM', 'goog.i18n.DateTimeSymbols_nr', 'goog.i18n.DateTimeSymbols_nr_ZA', 'goog.i18n.DateTimeSymbols_nso', 'goog.i18n.DateTimeSymbols_nso_ZA', 'goog.i18n.DateTimeSymbols_nus', 'goog.i18n.DateTimeSymbols_nus_SD', 'goog.i18n.DateTimeSymbols_nyn', 'goog.i18n.DateTimeSymbols_nyn_UG', 'goog.i18n.DateTimeSymbols_om', 'goog.i18n.DateTimeSymbols_om_ET', 'goog.i18n.DateTimeSymbols_om_KE', 'goog.i18n.DateTimeSymbols_or_IN', 'goog.i18n.DateTimeSymbols_os', 'goog.i18n.DateTimeSymbols_os_GE', 'goog.i18n.DateTimeSymbols_os_RU', 'goog.i18n.DateTimeSymbols_pa_Arab', 'goog.i18n.DateTimeSymbols_pa_Arab_PK', 'goog.i18n.DateTimeSymbols_pa_Guru', 'goog.i18n.DateTimeSymbols_pa_Guru_IN', 'goog.i18n.DateTimeSymbols_pl_PL', 'goog.i18n.DateTimeSymbols_ps', 'goog.i18n.DateTimeSymbols_ps_AF', 'goog.i18n.DateTimeSymbols_pt_AO', 'goog.i18n.DateTimeSymbols_pt_CV', 'goog.i18n.DateTimeSymbols_pt_GW', 'goog.i18n.DateTimeSymbols_pt_MO', 'goog.i18n.DateTimeSymbols_pt_MZ', 'goog.i18n.DateTimeSymbols_pt_ST', 'goog.i18n.DateTimeSymbols_pt_TL', 'goog.i18n.DateTimeSymbols_rm', 'goog.i18n.DateTimeSymbols_rm_CH', 'goog.i18n.DateTimeSymbols_rn', 'goog.i18n.DateTimeSymbols_rn_BI', 'goog.i18n.DateTimeSymbols_ro_MD', 'goog.i18n.DateTimeSymbols_ro_RO', 'goog.i18n.DateTimeSymbols_rof', 'goog.i18n.DateTimeSymbols_rof_TZ', 'goog.i18n.DateTimeSymbols_ru_BY', 'goog.i18n.DateTimeSymbols_ru_KG', 'goog.i18n.DateTimeSymbols_ru_KZ', 'goog.i18n.DateTimeSymbols_ru_MD', 'goog.i18n.DateTimeSymbols_ru_RU', 'goog.i18n.DateTimeSymbols_ru_UA', 'goog.i18n.DateTimeSymbols_rw', 'goog.i18n.DateTimeSymbols_rw_RW', 'goog.i18n.DateTimeSymbols_rwk', 'goog.i18n.DateTimeSymbols_rwk_TZ', 'goog.i18n.DateTimeSymbols_sah', 'goog.i18n.DateTimeSymbols_sah_RU', 'goog.i18n.DateTimeSymbols_saq', 'goog.i18n.DateTimeSymbols_saq_KE', 'goog.i18n.DateTimeSymbols_sbp', 'goog.i18n.DateTimeSymbols_sbp_TZ', 'goog.i18n.DateTimeSymbols_se', 'goog.i18n.DateTimeSymbols_se_FI', 'goog.i18n.DateTimeSymbols_se_NO', 'goog.i18n.DateTimeSymbols_seh', 'goog.i18n.DateTimeSymbols_seh_MZ', 'goog.i18n.DateTimeSymbols_ses', 'goog.i18n.DateTimeSymbols_ses_ML', 'goog.i18n.DateTimeSymbols_sg', 'goog.i18n.DateTimeSymbols_sg_CF', 'goog.i18n.DateTimeSymbols_shi', 'goog.i18n.DateTimeSymbols_shi_Latn', 'goog.i18n.DateTimeSymbols_shi_Latn_MA', 'goog.i18n.DateTimeSymbols_shi_Tfng', 'goog.i18n.DateTimeSymbols_shi_Tfng_MA', 'goog.i18n.DateTimeSymbols_si_LK', 'goog.i18n.DateTimeSymbols_sk_SK', 'goog.i18n.DateTimeSymbols_sl_SI', 'goog.i18n.DateTimeSymbols_sn', 'goog.i18n.DateTimeSymbols_sn_ZW', 'goog.i18n.DateTimeSymbols_so', 'goog.i18n.DateTimeSymbols_so_DJ', 'goog.i18n.DateTimeSymbols_so_ET', 'goog.i18n.DateTimeSymbols_so_KE', 'goog.i18n.DateTimeSymbols_so_SO', 'goog.i18n.DateTimeSymbols_sq_AL', 'goog.i18n.DateTimeSymbols_sq_MK', 'goog.i18n.DateTimeSymbols_sq_XK', 'goog.i18n.DateTimeSymbols_sr_Cyrl', 'goog.i18n.DateTimeSymbols_sr_Cyrl_BA', 'goog.i18n.DateTimeSymbols_sr_Cyrl_ME', 'goog.i18n.DateTimeSymbols_sr_Cyrl_RS', 'goog.i18n.DateTimeSymbols_sr_Cyrl_XK', 'goog.i18n.DateTimeSymbols_sr_Latn', 'goog.i18n.DateTimeSymbols_sr_Latn_BA', 'goog.i18n.DateTimeSymbols_sr_Latn_ME', 'goog.i18n.DateTimeSymbols_sr_Latn_RS', 'goog.i18n.DateTimeSymbols_sr_Latn_XK', 'goog.i18n.DateTimeSymbols_ss', 'goog.i18n.DateTimeSymbols_ss_SZ', 'goog.i18n.DateTimeSymbols_ss_ZA', 'goog.i18n.DateTimeSymbols_ssy', 'goog.i18n.DateTimeSymbols_ssy_ER', 'goog.i18n.DateTimeSymbols_st', 'goog.i18n.DateTimeSymbols_st_LS', 'goog.i18n.DateTimeSymbols_st_ZA', 'goog.i18n.DateTimeSymbols_sv_AX', 'goog.i18n.DateTimeSymbols_sv_FI', 'goog.i18n.DateTimeSymbols_sv_SE', 'goog.i18n.DateTimeSymbols_sw_KE', 'goog.i18n.DateTimeSymbols_sw_TZ', 'goog.i18n.DateTimeSymbols_sw_UG', 'goog.i18n.DateTimeSymbols_swc', 'goog.i18n.DateTimeSymbols_swc_CD', 'goog.i18n.DateTimeSymbols_ta_IN', 'goog.i18n.DateTimeSymbols_ta_LK', 'goog.i18n.DateTimeSymbols_ta_MY', 'goog.i18n.DateTimeSymbols_ta_SG', 'goog.i18n.DateTimeSymbols_te_IN', 'goog.i18n.DateTimeSymbols_teo', 'goog.i18n.DateTimeSymbols_teo_KE', 'goog.i18n.DateTimeSymbols_teo_UG', 'goog.i18n.DateTimeSymbols_tg', 'goog.i18n.DateTimeSymbols_tg_Cyrl', 'goog.i18n.DateTimeSymbols_tg_Cyrl_TJ', 'goog.i18n.DateTimeSymbols_th_TH', 'goog.i18n.DateTimeSymbols_ti', 'goog.i18n.DateTimeSymbols_ti_ER', 'goog.i18n.DateTimeSymbols_ti_ET', 'goog.i18n.DateTimeSymbols_tig', 'goog.i18n.DateTimeSymbols_tig_ER', 'goog.i18n.DateTimeSymbols_tn', 'goog.i18n.DateTimeSymbols_tn_BW', 'goog.i18n.DateTimeSymbols_tn_ZA', 'goog.i18n.DateTimeSymbols_to', 'goog.i18n.DateTimeSymbols_to_TO', 'goog.i18n.DateTimeSymbols_tr_CY', 'goog.i18n.DateTimeSymbols_tr_TR', 'goog.i18n.DateTimeSymbols_ts', 'goog.i18n.DateTimeSymbols_ts_ZA', 'goog.i18n.DateTimeSymbols_twq', 'goog.i18n.DateTimeSymbols_twq_NE', 'goog.i18n.DateTimeSymbols_tzm', 'goog.i18n.DateTimeSymbols_tzm_Latn', 'goog.i18n.DateTimeSymbols_tzm_Latn_MA', 'goog.i18n.DateTimeSymbols_ug', 'goog.i18n.DateTimeSymbols_ug_Arab', 'goog.i18n.DateTimeSymbols_ug_Arab_CN', 'goog.i18n.DateTimeSymbols_uk_UA', 'goog.i18n.DateTimeSymbols_ur_IN', 'goog.i18n.DateTimeSymbols_ur_PK', 'goog.i18n.DateTimeSymbols_uz_Arab', 'goog.i18n.DateTimeSymbols_uz_Arab_AF', 'goog.i18n.DateTimeSymbols_uz_Cyrl', 'goog.i18n.DateTimeSymbols_uz_Cyrl_UZ', 'goog.i18n.DateTimeSymbols_uz_Latn', 'goog.i18n.DateTimeSymbols_uz_Latn_UZ', 'goog.i18n.DateTimeSymbols_vai', 'goog.i18n.DateTimeSymbols_vai_Latn', 'goog.i18n.DateTimeSymbols_vai_Latn_LR', 'goog.i18n.DateTimeSymbols_vai_Vaii', 'goog.i18n.DateTimeSymbols_vai_Vaii_LR', 'goog.i18n.DateTimeSymbols_ve', 'goog.i18n.DateTimeSymbols_ve_ZA', 'goog.i18n.DateTimeSymbols_vi_VN', 'goog.i18n.DateTimeSymbols_vo', 'goog.i18n.DateTimeSymbols_vo_001', 'goog.i18n.DateTimeSymbols_vun', 'goog.i18n.DateTimeSymbols_vun_TZ', 'goog.i18n.DateTimeSymbols_wae', 'goog.i18n.DateTimeSymbols_wae_CH', 'goog.i18n.DateTimeSymbols_wal', 'goog.i18n.DateTimeSymbols_wal_ET', 'goog.i18n.DateTimeSymbols_xh', 'goog.i18n.DateTimeSymbols_xh_ZA', 'goog.i18n.DateTimeSymbols_xog', 'goog.i18n.DateTimeSymbols_xog_UG', 'goog.i18n.DateTimeSymbols_yav', 'goog.i18n.DateTimeSymbols_yav_CM', 'goog.i18n.DateTimeSymbols_yo', 'goog.i18n.DateTimeSymbols_yo_BJ', 'goog.i18n.DateTimeSymbols_yo_NG', 'goog.i18n.DateTimeSymbols_zgh', 'goog.i18n.DateTimeSymbols_zgh_MA', 'goog.i18n.DateTimeSymbols_zh_Hans', 'goog.i18n.DateTimeSymbols_zh_Hans_CN', 'goog.i18n.DateTimeSymbols_zh_Hans_HK', 'goog.i18n.DateTimeSymbols_zh_Hans_MO', 'goog.i18n.DateTimeSymbols_zh_Hans_SG', 'goog.i18n.DateTimeSymbols_zh_Hant', 'goog.i18n.DateTimeSymbols_zh_Hant_HK', 'goog.i18n.DateTimeSymbols_zh_Hant_MO', 'goog.i18n.DateTimeSymbols_zh_Hant_TW', 'goog.i18n.DateTimeSymbols_zu_ZA'], ['goog.i18n.DateTimeSymbols']);
goog.addDependency('i18n/graphemebreak.js', ['goog.i18n.GraphemeBreak'], ['goog.structs.InversionMap']);
goog.addDependency('i18n/graphemebreak_test.js', ['goog.i18n.GraphemeBreakTest'], ['goog.i18n.GraphemeBreak', 'goog.testing.jsunit']);
goog.addDependency('i18n/messageformat.js', ['goog.i18n.MessageFormat'], ['goog.asserts', 'goog.i18n.NumberFormat', 'goog.i18n.ordinalRules', 'goog.i18n.pluralRules']);
goog.addDependency('i18n/messageformat_test.js', ['goog.i18n.MessageFormatTest'], ['goog.i18n.MessageFormat', 'goog.i18n.NumberFormatSymbols_hr', 'goog.i18n.pluralRules', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit']);
goog.addDependency('i18n/mime.js', ['goog.i18n.mime', 'goog.i18n.mime.encode'], ['goog.array']);
goog.addDependency('i18n/mime_test.js', ['goog.i18n.mime.encodeTest'], ['goog.i18n.mime.encode', 'goog.testing.jsunit']);
goog.addDependency('i18n/numberformat.js', ['goog.i18n.NumberFormat', 'goog.i18n.NumberFormat.CurrencyStyle', 'goog.i18n.NumberFormat.Format'], ['goog.asserts', 'goog.i18n.CompactNumberFormatSymbols', 'goog.i18n.NumberFormatSymbols', 'goog.i18n.currency', 'goog.math']);
goog.addDependency('i18n/numberformat_test.js', ['goog.i18n.NumberFormatTest'], ['goog.i18n.CompactNumberFormatSymbols', 'goog.i18n.CompactNumberFormatSymbols_de', 'goog.i18n.CompactNumberFormatSymbols_en', 'goog.i18n.CompactNumberFormatSymbols_fr', 'goog.i18n.NumberFormat', 'goog.i18n.NumberFormatSymbols', 'goog.i18n.NumberFormatSymbols_de', 'goog.i18n.NumberFormatSymbols_en', 'goog.i18n.NumberFormatSymbols_fr', 'goog.i18n.NumberFormatSymbols_pl', 'goog.i18n.NumberFormatSymbols_ro', 'goog.testing.ExpectedFailures', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.userAgent', 'goog.userAgent.product', 'goog.userAgent.product.isVersion']);
goog.addDependency('i18n/numberformatsymbols.js', ['goog.i18n.NumberFormatSymbols', 'goog.i18n.NumberFormatSymbols_af', 'goog.i18n.NumberFormatSymbols_af_ZA', 'goog.i18n.NumberFormatSymbols_am', 'goog.i18n.NumberFormatSymbols_am_ET', 'goog.i18n.NumberFormatSymbols_ar', 'goog.i18n.NumberFormatSymbols_ar_001', 'goog.i18n.NumberFormatSymbols_az', 'goog.i18n.NumberFormatSymbols_az_Cyrl_AZ', 'goog.i18n.NumberFormatSymbols_az_Latn_AZ', 'goog.i18n.NumberFormatSymbols_bg', 'goog.i18n.NumberFormatSymbols_bg_BG', 'goog.i18n.NumberFormatSymbols_bn', 'goog.i18n.NumberFormatSymbols_bn_BD', 'goog.i18n.NumberFormatSymbols_br', 'goog.i18n.NumberFormatSymbols_br_FR', 'goog.i18n.NumberFormatSymbols_ca', 'goog.i18n.NumberFormatSymbols_ca_AD', 'goog.i18n.NumberFormatSymbols_ca_ES', 'goog.i18n.NumberFormatSymbols_ca_ES_VALENCIA', 'goog.i18n.NumberFormatSymbols_ca_FR', 'goog.i18n.NumberFormatSymbols_ca_IT', 'goog.i18n.NumberFormatSymbols_chr', 'goog.i18n.NumberFormatSymbols_chr_US', 'goog.i18n.NumberFormatSymbols_cs', 'goog.i18n.NumberFormatSymbols_cs_CZ', 'goog.i18n.NumberFormatSymbols_cy', 'goog.i18n.NumberFormatSymbols_cy_GB', 'goog.i18n.NumberFormatSymbols_da', 'goog.i18n.NumberFormatSymbols_da_DK', 'goog.i18n.NumberFormatSymbols_da_GL', 'goog.i18n.NumberFormatSymbols_de', 'goog.i18n.NumberFormatSymbols_de_AT', 'goog.i18n.NumberFormatSymbols_de_BE', 'goog.i18n.NumberFormatSymbols_de_CH', 'goog.i18n.NumberFormatSymbols_de_DE', 'goog.i18n.NumberFormatSymbols_de_LU', 'goog.i18n.NumberFormatSymbols_el', 'goog.i18n.NumberFormatSymbols_el_GR', 'goog.i18n.NumberFormatSymbols_en', 'goog.i18n.NumberFormatSymbols_en_001', 'goog.i18n.NumberFormatSymbols_en_AS', 'goog.i18n.NumberFormatSymbols_en_AU', 'goog.i18n.NumberFormatSymbols_en_DG', 'goog.i18n.NumberFormatSymbols_en_FM', 'goog.i18n.NumberFormatSymbols_en_GB', 'goog.i18n.NumberFormatSymbols_en_GU', 'goog.i18n.NumberFormatSymbols_en_IE', 'goog.i18n.NumberFormatSymbols_en_IN', 'goog.i18n.NumberFormatSymbols_en_IO', 'goog.i18n.NumberFormatSymbols_en_MH', 'goog.i18n.NumberFormatSymbols_en_MP', 'goog.i18n.NumberFormatSymbols_en_PR', 'goog.i18n.NumberFormatSymbols_en_PW', 'goog.i18n.NumberFormatSymbols_en_SG', 'goog.i18n.NumberFormatSymbols_en_TC', 'goog.i18n.NumberFormatSymbols_en_UM', 'goog.i18n.NumberFormatSymbols_en_US', 'goog.i18n.NumberFormatSymbols_en_VG', 'goog.i18n.NumberFormatSymbols_en_VI', 'goog.i18n.NumberFormatSymbols_en_ZA', 'goog.i18n.NumberFormatSymbols_en_ZW', 'goog.i18n.NumberFormatSymbols_es', 'goog.i18n.NumberFormatSymbols_es_419', 'goog.i18n.NumberFormatSymbols_es_EA', 'goog.i18n.NumberFormatSymbols_es_ES', 'goog.i18n.NumberFormatSymbols_es_IC', 'goog.i18n.NumberFormatSymbols_et', 'goog.i18n.NumberFormatSymbols_et_EE', 'goog.i18n.NumberFormatSymbols_eu', 'goog.i18n.NumberFormatSymbols_eu_ES', 'goog.i18n.NumberFormatSymbols_fa', 'goog.i18n.NumberFormatSymbols_fa_IR', 'goog.i18n.NumberFormatSymbols_fi', 'goog.i18n.NumberFormatSymbols_fi_FI', 'goog.i18n.NumberFormatSymbols_fil', 'goog.i18n.NumberFormatSymbols_fil_PH', 'goog.i18n.NumberFormatSymbols_fr', 'goog.i18n.NumberFormatSymbols_fr_BL', 'goog.i18n.NumberFormatSymbols_fr_CA', 'goog.i18n.NumberFormatSymbols_fr_FR', 'goog.i18n.NumberFormatSymbols_fr_GF', 'goog.i18n.NumberFormatSymbols_fr_GP', 'goog.i18n.NumberFormatSymbols_fr_MC', 'goog.i18n.NumberFormatSymbols_fr_MF', 'goog.i18n.NumberFormatSymbols_fr_MQ', 'goog.i18n.NumberFormatSymbols_fr_PM', 'goog.i18n.NumberFormatSymbols_fr_RE', 'goog.i18n.NumberFormatSymbols_fr_YT', 'goog.i18n.NumberFormatSymbols_gl', 'goog.i18n.NumberFormatSymbols_gl_ES', 'goog.i18n.NumberFormatSymbols_gsw', 'goog.i18n.NumberFormatSymbols_gsw_CH', 'goog.i18n.NumberFormatSymbols_gsw_LI', 'goog.i18n.NumberFormatSymbols_gu', 'goog.i18n.NumberFormatSymbols_gu_IN', 'goog.i18n.NumberFormatSymbols_haw', 'goog.i18n.NumberFormatSymbols_haw_US', 'goog.i18n.NumberFormatSymbols_he', 'goog.i18n.NumberFormatSymbols_he_IL', 'goog.i18n.NumberFormatSymbols_hi', 'goog.i18n.NumberFormatSymbols_hi_IN', 'goog.i18n.NumberFormatSymbols_hr', 'goog.i18n.NumberFormatSymbols_hr_HR', 'goog.i18n.NumberFormatSymbols_hu', 'goog.i18n.NumberFormatSymbols_hu_HU', 'goog.i18n.NumberFormatSymbols_hy', 'goog.i18n.NumberFormatSymbols_hy_AM', 'goog.i18n.NumberFormatSymbols_id', 'goog.i18n.NumberFormatSymbols_id_ID', 'goog.i18n.NumberFormatSymbols_in', 'goog.i18n.NumberFormatSymbols_is', 'goog.i18n.NumberFormatSymbols_is_IS', 'goog.i18n.NumberFormatSymbols_it', 'goog.i18n.NumberFormatSymbols_it_IT', 'goog.i18n.NumberFormatSymbols_it_SM', 'goog.i18n.NumberFormatSymbols_iw', 'goog.i18n.NumberFormatSymbols_ja', 'goog.i18n.NumberFormatSymbols_ja_JP', 'goog.i18n.NumberFormatSymbols_ka', 'goog.i18n.NumberFormatSymbols_ka_GE', 'goog.i18n.NumberFormatSymbols_kk', 'goog.i18n.NumberFormatSymbols_kk_Cyrl_KZ', 'goog.i18n.NumberFormatSymbols_km', 'goog.i18n.NumberFormatSymbols_km_KH', 'goog.i18n.NumberFormatSymbols_kn', 'goog.i18n.NumberFormatSymbols_kn_IN', 'goog.i18n.NumberFormatSymbols_ko', 'goog.i18n.NumberFormatSymbols_ko_KR', 'goog.i18n.NumberFormatSymbols_ky', 'goog.i18n.NumberFormatSymbols_ky_Cyrl_KG', 'goog.i18n.NumberFormatSymbols_ln', 'goog.i18n.NumberFormatSymbols_ln_CD', 'goog.i18n.NumberFormatSymbols_lo', 'goog.i18n.NumberFormatSymbols_lo_LA', 'goog.i18n.NumberFormatSymbols_lt', 'goog.i18n.NumberFormatSymbols_lt_LT', 'goog.i18n.NumberFormatSymbols_lv', 'goog.i18n.NumberFormatSymbols_lv_LV', 'goog.i18n.NumberFormatSymbols_mk', 'goog.i18n.NumberFormatSymbols_mk_MK', 'goog.i18n.NumberFormatSymbols_ml', 'goog.i18n.NumberFormatSymbols_ml_IN', 'goog.i18n.NumberFormatSymbols_mn', 'goog.i18n.NumberFormatSymbols_mn_Cyrl_MN', 'goog.i18n.NumberFormatSymbols_mr', 'goog.i18n.NumberFormatSymbols_mr_IN', 'goog.i18n.NumberFormatSymbols_ms', 'goog.i18n.NumberFormatSymbols_ms_Latn_MY', 'goog.i18n.NumberFormatSymbols_mt', 'goog.i18n.NumberFormatSymbols_mt_MT', 'goog.i18n.NumberFormatSymbols_my', 'goog.i18n.NumberFormatSymbols_my_MM', 'goog.i18n.NumberFormatSymbols_nb', 'goog.i18n.NumberFormatSymbols_nb_NO', 'goog.i18n.NumberFormatSymbols_nb_SJ', 'goog.i18n.NumberFormatSymbols_ne', 'goog.i18n.NumberFormatSymbols_ne_NP', 'goog.i18n.NumberFormatSymbols_nl', 'goog.i18n.NumberFormatSymbols_nl_NL', 'goog.i18n.NumberFormatSymbols_no', 'goog.i18n.NumberFormatSymbols_no_NO', 'goog.i18n.NumberFormatSymbols_or', 'goog.i18n.NumberFormatSymbols_or_IN', 'goog.i18n.NumberFormatSymbols_pa', 'goog.i18n.NumberFormatSymbols_pa_Guru_IN', 'goog.i18n.NumberFormatSymbols_pl', 'goog.i18n.NumberFormatSymbols_pl_PL', 'goog.i18n.NumberFormatSymbols_pt', 'goog.i18n.NumberFormatSymbols_pt_BR', 'goog.i18n.NumberFormatSymbols_pt_PT', 'goog.i18n.NumberFormatSymbols_ro', 'goog.i18n.NumberFormatSymbols_ro_RO', 'goog.i18n.NumberFormatSymbols_ru', 'goog.i18n.NumberFormatSymbols_ru_RU', 'goog.i18n.NumberFormatSymbols_si', 'goog.i18n.NumberFormatSymbols_si_LK', 'goog.i18n.NumberFormatSymbols_sk', 'goog.i18n.NumberFormatSymbols_sk_SK', 'goog.i18n.NumberFormatSymbols_sl', 'goog.i18n.NumberFormatSymbols_sl_SI', 'goog.i18n.NumberFormatSymbols_sq', 'goog.i18n.NumberFormatSymbols_sq_AL', 'goog.i18n.NumberFormatSymbols_sr', 'goog.i18n.NumberFormatSymbols_sr_Cyrl_RS', 'goog.i18n.NumberFormatSymbols_sv', 'goog.i18n.NumberFormatSymbols_sv_SE', 'goog.i18n.NumberFormatSymbols_sw', 'goog.i18n.NumberFormatSymbols_sw_TZ', 'goog.i18n.NumberFormatSymbols_ta', 'goog.i18n.NumberFormatSymbols_ta_IN', 'goog.i18n.NumberFormatSymbols_te', 'goog.i18n.NumberFormatSymbols_te_IN', 'goog.i18n.NumberFormatSymbols_th', 'goog.i18n.NumberFormatSymbols_th_TH', 'goog.i18n.NumberFormatSymbols_tl', 'goog.i18n.NumberFormatSymbols_tr', 'goog.i18n.NumberFormatSymbols_tr_TR', 'goog.i18n.NumberFormatSymbols_uk', 'goog.i18n.NumberFormatSymbols_uk_UA', 'goog.i18n.NumberFormatSymbols_ur', 'goog.i18n.NumberFormatSymbols_ur_PK', 'goog.i18n.NumberFormatSymbols_uz', 'goog.i18n.NumberFormatSymbols_uz_Latn_UZ', 'goog.i18n.NumberFormatSymbols_vi', 'goog.i18n.NumberFormatSymbols_vi_VN', 'goog.i18n.NumberFormatSymbols_zh', 'goog.i18n.NumberFormatSymbols_zh_CN', 'goog.i18n.NumberFormatSymbols_zh_HK', 'goog.i18n.NumberFormatSymbols_zh_Hans_CN', 'goog.i18n.NumberFormatSymbols_zh_TW', 'goog.i18n.NumberFormatSymbols_zu', 'goog.i18n.NumberFormatSymbols_zu_ZA'], []);
goog.addDependency('i18n/numberformatsymbolsext.js', ['goog.i18n.NumberFormatSymbolsExt', 'goog.i18n.NumberFormatSymbols_aa', 'goog.i18n.NumberFormatSymbols_aa_DJ', 'goog.i18n.NumberFormatSymbols_aa_ER', 'goog.i18n.NumberFormatSymbols_aa_ET', 'goog.i18n.NumberFormatSymbols_af_NA', 'goog.i18n.NumberFormatSymbols_agq', 'goog.i18n.NumberFormatSymbols_agq_CM', 'goog.i18n.NumberFormatSymbols_ak', 'goog.i18n.NumberFormatSymbols_ak_GH', 'goog.i18n.NumberFormatSymbols_ar_AE', 'goog.i18n.NumberFormatSymbols_ar_BH', 'goog.i18n.NumberFormatSymbols_ar_DJ', 'goog.i18n.NumberFormatSymbols_ar_DZ', 'goog.i18n.NumberFormatSymbols_ar_EG', 'goog.i18n.NumberFormatSymbols_ar_EH', 'goog.i18n.NumberFormatSymbols_ar_ER', 'goog.i18n.NumberFormatSymbols_ar_IL', 'goog.i18n.NumberFormatSymbols_ar_IQ', 'goog.i18n.NumberFormatSymbols_ar_JO', 'goog.i18n.NumberFormatSymbols_ar_KM', 'goog.i18n.NumberFormatSymbols_ar_KW', 'goog.i18n.NumberFormatSymbols_ar_LB', 'goog.i18n.NumberFormatSymbols_ar_LY', 'goog.i18n.NumberFormatSymbols_ar_MA', 'goog.i18n.NumberFormatSymbols_ar_MR', 'goog.i18n.NumberFormatSymbols_ar_OM', 'goog.i18n.NumberFormatSymbols_ar_PS', 'goog.i18n.NumberFormatSymbols_ar_QA', 'goog.i18n.NumberFormatSymbols_ar_SA', 'goog.i18n.NumberFormatSymbols_ar_SD', 'goog.i18n.NumberFormatSymbols_ar_SO', 'goog.i18n.NumberFormatSymbols_ar_SS', 'goog.i18n.NumberFormatSymbols_ar_SY', 'goog.i18n.NumberFormatSymbols_ar_TD', 'goog.i18n.NumberFormatSymbols_ar_TN', 'goog.i18n.NumberFormatSymbols_ar_YE', 'goog.i18n.NumberFormatSymbols_as', 'goog.i18n.NumberFormatSymbols_as_IN', 'goog.i18n.NumberFormatSymbols_asa', 'goog.i18n.NumberFormatSymbols_asa_TZ', 'goog.i18n.NumberFormatSymbols_ast', 'goog.i18n.NumberFormatSymbols_ast_ES', 'goog.i18n.NumberFormatSymbols_az_Cyrl', 'goog.i18n.NumberFormatSymbols_az_Latn', 'goog.i18n.NumberFormatSymbols_bas', 'goog.i18n.NumberFormatSymbols_bas_CM', 'goog.i18n.NumberFormatSymbols_be', 'goog.i18n.NumberFormatSymbols_be_BY', 'goog.i18n.NumberFormatSymbols_bem', 'goog.i18n.NumberFormatSymbols_bem_ZM', 'goog.i18n.NumberFormatSymbols_bez', 'goog.i18n.NumberFormatSymbols_bez_TZ', 'goog.i18n.NumberFormatSymbols_bm', 'goog.i18n.NumberFormatSymbols_bm_ML', 'goog.i18n.NumberFormatSymbols_bn_IN', 'goog.i18n.NumberFormatSymbols_bo', 'goog.i18n.NumberFormatSymbols_bo_CN', 'goog.i18n.NumberFormatSymbols_bo_IN', 'goog.i18n.NumberFormatSymbols_brx', 'goog.i18n.NumberFormatSymbols_brx_IN', 'goog.i18n.NumberFormatSymbols_bs', 'goog.i18n.NumberFormatSymbols_bs_Cyrl', 'goog.i18n.NumberFormatSymbols_bs_Cyrl_BA', 'goog.i18n.NumberFormatSymbols_bs_Latn', 'goog.i18n.NumberFormatSymbols_bs_Latn_BA', 'goog.i18n.NumberFormatSymbols_byn', 'goog.i18n.NumberFormatSymbols_byn_ER', 'goog.i18n.NumberFormatSymbols_cgg', 'goog.i18n.NumberFormatSymbols_cgg_UG', 'goog.i18n.NumberFormatSymbols_ckb', 'goog.i18n.NumberFormatSymbols_ckb_Arab', 'goog.i18n.NumberFormatSymbols_ckb_Arab_IQ', 'goog.i18n.NumberFormatSymbols_ckb_Arab_IR', 'goog.i18n.NumberFormatSymbols_ckb_IQ', 'goog.i18n.NumberFormatSymbols_ckb_IR', 'goog.i18n.NumberFormatSymbols_ckb_Latn', 'goog.i18n.NumberFormatSymbols_ckb_Latn_IQ', 'goog.i18n.NumberFormatSymbols_dav', 'goog.i18n.NumberFormatSymbols_dav_KE', 'goog.i18n.NumberFormatSymbols_de_LI', 'goog.i18n.NumberFormatSymbols_dje', 'goog.i18n.NumberFormatSymbols_dje_NE', 'goog.i18n.NumberFormatSymbols_dua', 'goog.i18n.NumberFormatSymbols_dua_CM', 'goog.i18n.NumberFormatSymbols_dyo', 'goog.i18n.NumberFormatSymbols_dyo_SN', 'goog.i18n.NumberFormatSymbols_dz', 'goog.i18n.NumberFormatSymbols_dz_BT', 'goog.i18n.NumberFormatSymbols_ebu', 'goog.i18n.NumberFormatSymbols_ebu_KE', 'goog.i18n.NumberFormatSymbols_ee', 'goog.i18n.NumberFormatSymbols_ee_GH', 'goog.i18n.NumberFormatSymbols_ee_TG', 'goog.i18n.NumberFormatSymbols_el_CY', 'goog.i18n.NumberFormatSymbols_en_150', 'goog.i18n.NumberFormatSymbols_en_AG', 'goog.i18n.NumberFormatSymbols_en_AI', 'goog.i18n.NumberFormatSymbols_en_BB', 'goog.i18n.NumberFormatSymbols_en_BE', 'goog.i18n.NumberFormatSymbols_en_BM', 'goog.i18n.NumberFormatSymbols_en_BS', 'goog.i18n.NumberFormatSymbols_en_BW', 'goog.i18n.NumberFormatSymbols_en_BZ', 'goog.i18n.NumberFormatSymbols_en_CA', 'goog.i18n.NumberFormatSymbols_en_CC', 'goog.i18n.NumberFormatSymbols_en_CK', 'goog.i18n.NumberFormatSymbols_en_CM', 'goog.i18n.NumberFormatSymbols_en_CX', 'goog.i18n.NumberFormatSymbols_en_DM', 'goog.i18n.NumberFormatSymbols_en_ER', 'goog.i18n.NumberFormatSymbols_en_FJ', 'goog.i18n.NumberFormatSymbols_en_FK', 'goog.i18n.NumberFormatSymbols_en_GD', 'goog.i18n.NumberFormatSymbols_en_GG', 'goog.i18n.NumberFormatSymbols_en_GH', 'goog.i18n.NumberFormatSymbols_en_GI', 'goog.i18n.NumberFormatSymbols_en_GM', 'goog.i18n.NumberFormatSymbols_en_GY', 'goog.i18n.NumberFormatSymbols_en_HK', 'goog.i18n.NumberFormatSymbols_en_IM', 'goog.i18n.NumberFormatSymbols_en_JE', 'goog.i18n.NumberFormatSymbols_en_JM', 'goog.i18n.NumberFormatSymbols_en_KE', 'goog.i18n.NumberFormatSymbols_en_KI', 'goog.i18n.NumberFormatSymbols_en_KN', 'goog.i18n.NumberFormatSymbols_en_KY', 'goog.i18n.NumberFormatSymbols_en_LC', 'goog.i18n.NumberFormatSymbols_en_LR', 'goog.i18n.NumberFormatSymbols_en_LS', 'goog.i18n.NumberFormatSymbols_en_MG', 'goog.i18n.NumberFormatSymbols_en_MO', 'goog.i18n.NumberFormatSymbols_en_MS', 'goog.i18n.NumberFormatSymbols_en_MT', 'goog.i18n.NumberFormatSymbols_en_MU', 'goog.i18n.NumberFormatSymbols_en_MW', 'goog.i18n.NumberFormatSymbols_en_NA', 'goog.i18n.NumberFormatSymbols_en_NF', 'goog.i18n.NumberFormatSymbols_en_NG', 'goog.i18n.NumberFormatSymbols_en_NR', 'goog.i18n.NumberFormatSymbols_en_NU', 'goog.i18n.NumberFormatSymbols_en_NZ', 'goog.i18n.NumberFormatSymbols_en_PG', 'goog.i18n.NumberFormatSymbols_en_PH', 'goog.i18n.NumberFormatSymbols_en_PK', 'goog.i18n.NumberFormatSymbols_en_PN', 'goog.i18n.NumberFormatSymbols_en_RW', 'goog.i18n.NumberFormatSymbols_en_SB', 'goog.i18n.NumberFormatSymbols_en_SC', 'goog.i18n.NumberFormatSymbols_en_SD', 'goog.i18n.NumberFormatSymbols_en_SH', 'goog.i18n.NumberFormatSymbols_en_SL', 'goog.i18n.NumberFormatSymbols_en_SS', 'goog.i18n.NumberFormatSymbols_en_SX', 'goog.i18n.NumberFormatSymbols_en_SZ', 'goog.i18n.NumberFormatSymbols_en_TK', 'goog.i18n.NumberFormatSymbols_en_TO', 'goog.i18n.NumberFormatSymbols_en_TT', 'goog.i18n.NumberFormatSymbols_en_TV', 'goog.i18n.NumberFormatSymbols_en_TZ', 'goog.i18n.NumberFormatSymbols_en_UG', 'goog.i18n.NumberFormatSymbols_en_VC', 'goog.i18n.NumberFormatSymbols_en_VU', 'goog.i18n.NumberFormatSymbols_en_WS', 'goog.i18n.NumberFormatSymbols_en_ZM', 'goog.i18n.NumberFormatSymbols_eo', 'goog.i18n.NumberFormatSymbols_eo_001', 'goog.i18n.NumberFormatSymbols_es_AR', 'goog.i18n.NumberFormatSymbols_es_BO', 'goog.i18n.NumberFormatSymbols_es_CL', 'goog.i18n.NumberFormatSymbols_es_CO', 'goog.i18n.NumberFormatSymbols_es_CR', 'goog.i18n.NumberFormatSymbols_es_CU', 'goog.i18n.NumberFormatSymbols_es_DO', 'goog.i18n.NumberFormatSymbols_es_EC', 'goog.i18n.NumberFormatSymbols_es_GQ', 'goog.i18n.NumberFormatSymbols_es_GT', 'goog.i18n.NumberFormatSymbols_es_HN', 'goog.i18n.NumberFormatSymbols_es_MX', 'goog.i18n.NumberFormatSymbols_es_NI', 'goog.i18n.NumberFormatSymbols_es_PA', 'goog.i18n.NumberFormatSymbols_es_PE', 'goog.i18n.NumberFormatSymbols_es_PH', 'goog.i18n.NumberFormatSymbols_es_PR', 'goog.i18n.NumberFormatSymbols_es_PY', 'goog.i18n.NumberFormatSymbols_es_SV', 'goog.i18n.NumberFormatSymbols_es_US', 'goog.i18n.NumberFormatSymbols_es_UY', 'goog.i18n.NumberFormatSymbols_es_VE', 'goog.i18n.NumberFormatSymbols_ewo', 'goog.i18n.NumberFormatSymbols_ewo_CM', 'goog.i18n.NumberFormatSymbols_fa_AF', 'goog.i18n.NumberFormatSymbols_ff', 'goog.i18n.NumberFormatSymbols_ff_CM', 'goog.i18n.NumberFormatSymbols_ff_GN', 'goog.i18n.NumberFormatSymbols_ff_MR', 'goog.i18n.NumberFormatSymbols_ff_SN', 'goog.i18n.NumberFormatSymbols_fo', 'goog.i18n.NumberFormatSymbols_fo_FO', 'goog.i18n.NumberFormatSymbols_fr_BE', 'goog.i18n.NumberFormatSymbols_fr_BF', 'goog.i18n.NumberFormatSymbols_fr_BI', 'goog.i18n.NumberFormatSymbols_fr_BJ', 'goog.i18n.NumberFormatSymbols_fr_CD', 'goog.i18n.NumberFormatSymbols_fr_CF', 'goog.i18n.NumberFormatSymbols_fr_CG', 'goog.i18n.NumberFormatSymbols_fr_CH', 'goog.i18n.NumberFormatSymbols_fr_CI', 'goog.i18n.NumberFormatSymbols_fr_CM', 'goog.i18n.NumberFormatSymbols_fr_DJ', 'goog.i18n.NumberFormatSymbols_fr_DZ', 'goog.i18n.NumberFormatSymbols_fr_GA', 'goog.i18n.NumberFormatSymbols_fr_GN', 'goog.i18n.NumberFormatSymbols_fr_GQ', 'goog.i18n.NumberFormatSymbols_fr_HT', 'goog.i18n.NumberFormatSymbols_fr_KM', 'goog.i18n.NumberFormatSymbols_fr_LU', 'goog.i18n.NumberFormatSymbols_fr_MA', 'goog.i18n.NumberFormatSymbols_fr_MG', 'goog.i18n.NumberFormatSymbols_fr_ML', 'goog.i18n.NumberFormatSymbols_fr_MR', 'goog.i18n.NumberFormatSymbols_fr_MU', 'goog.i18n.NumberFormatSymbols_fr_NC', 'goog.i18n.NumberFormatSymbols_fr_NE', 'goog.i18n.NumberFormatSymbols_fr_PF', 'goog.i18n.NumberFormatSymbols_fr_RW', 'goog.i18n.NumberFormatSymbols_fr_SC', 'goog.i18n.NumberFormatSymbols_fr_SN', 'goog.i18n.NumberFormatSymbols_fr_SY', 'goog.i18n.NumberFormatSymbols_fr_TD', 'goog.i18n.NumberFormatSymbols_fr_TG', 'goog.i18n.NumberFormatSymbols_fr_TN', 'goog.i18n.NumberFormatSymbols_fr_VU', 'goog.i18n.NumberFormatSymbols_fr_WF', 'goog.i18n.NumberFormatSymbols_fur', 'goog.i18n.NumberFormatSymbols_fur_IT', 'goog.i18n.NumberFormatSymbols_fy', 'goog.i18n.NumberFormatSymbols_fy_NL', 'goog.i18n.NumberFormatSymbols_ga', 'goog.i18n.NumberFormatSymbols_ga_IE', 'goog.i18n.NumberFormatSymbols_gd', 'goog.i18n.NumberFormatSymbols_gd_GB', 'goog.i18n.NumberFormatSymbols_guz', 'goog.i18n.NumberFormatSymbols_guz_KE', 'goog.i18n.NumberFormatSymbols_gv', 'goog.i18n.NumberFormatSymbols_gv_IM', 'goog.i18n.NumberFormatSymbols_ha', 'goog.i18n.NumberFormatSymbols_ha_Latn', 'goog.i18n.NumberFormatSymbols_ha_Latn_GH', 'goog.i18n.NumberFormatSymbols_ha_Latn_NE', 'goog.i18n.NumberFormatSymbols_ha_Latn_NG', 'goog.i18n.NumberFormatSymbols_hr_BA', 'goog.i18n.NumberFormatSymbols_ia', 'goog.i18n.NumberFormatSymbols_ia_FR', 'goog.i18n.NumberFormatSymbols_ig', 'goog.i18n.NumberFormatSymbols_ig_NG', 'goog.i18n.NumberFormatSymbols_ii', 'goog.i18n.NumberFormatSymbols_ii_CN', 'goog.i18n.NumberFormatSymbols_it_CH', 'goog.i18n.NumberFormatSymbols_jgo', 'goog.i18n.NumberFormatSymbols_jgo_CM', 'goog.i18n.NumberFormatSymbols_jmc', 'goog.i18n.NumberFormatSymbols_jmc_TZ', 'goog.i18n.NumberFormatSymbols_kab', 'goog.i18n.NumberFormatSymbols_kab_DZ', 'goog.i18n.NumberFormatSymbols_kam', 'goog.i18n.NumberFormatSymbols_kam_KE', 'goog.i18n.NumberFormatSymbols_kde', 'goog.i18n.NumberFormatSymbols_kde_TZ', 'goog.i18n.NumberFormatSymbols_kea', 'goog.i18n.NumberFormatSymbols_kea_CV', 'goog.i18n.NumberFormatSymbols_khq', 'goog.i18n.NumberFormatSymbols_khq_ML', 'goog.i18n.NumberFormatSymbols_ki', 'goog.i18n.NumberFormatSymbols_ki_KE', 'goog.i18n.NumberFormatSymbols_kk_Cyrl', 'goog.i18n.NumberFormatSymbols_kkj', 'goog.i18n.NumberFormatSymbols_kkj_CM', 'goog.i18n.NumberFormatSymbols_kl', 'goog.i18n.NumberFormatSymbols_kl_GL', 'goog.i18n.NumberFormatSymbols_kln', 'goog.i18n.NumberFormatSymbols_kln_KE', 'goog.i18n.NumberFormatSymbols_ko_KP', 'goog.i18n.NumberFormatSymbols_kok', 'goog.i18n.NumberFormatSymbols_kok_IN', 'goog.i18n.NumberFormatSymbols_ks', 'goog.i18n.NumberFormatSymbols_ks_Arab', 'goog.i18n.NumberFormatSymbols_ks_Arab_IN', 'goog.i18n.NumberFormatSymbols_ksb', 'goog.i18n.NumberFormatSymbols_ksb_TZ', 'goog.i18n.NumberFormatSymbols_ksf', 'goog.i18n.NumberFormatSymbols_ksf_CM', 'goog.i18n.NumberFormatSymbols_ksh', 'goog.i18n.NumberFormatSymbols_ksh_DE', 'goog.i18n.NumberFormatSymbols_kw', 'goog.i18n.NumberFormatSymbols_kw_GB', 'goog.i18n.NumberFormatSymbols_ky_Cyrl', 'goog.i18n.NumberFormatSymbols_lag', 'goog.i18n.NumberFormatSymbols_lag_TZ', 'goog.i18n.NumberFormatSymbols_lg', 'goog.i18n.NumberFormatSymbols_lg_UG', 'goog.i18n.NumberFormatSymbols_lkt', 'goog.i18n.NumberFormatSymbols_lkt_US', 'goog.i18n.NumberFormatSymbols_ln_AO', 'goog.i18n.NumberFormatSymbols_ln_CF', 'goog.i18n.NumberFormatSymbols_ln_CG', 'goog.i18n.NumberFormatSymbols_lu', 'goog.i18n.NumberFormatSymbols_lu_CD', 'goog.i18n.NumberFormatSymbols_luo', 'goog.i18n.NumberFormatSymbols_luo_KE', 'goog.i18n.NumberFormatSymbols_luy', 'goog.i18n.NumberFormatSymbols_luy_KE', 'goog.i18n.NumberFormatSymbols_mas', 'goog.i18n.NumberFormatSymbols_mas_KE', 'goog.i18n.NumberFormatSymbols_mas_TZ', 'goog.i18n.NumberFormatSymbols_mer', 'goog.i18n.NumberFormatSymbols_mer_KE', 'goog.i18n.NumberFormatSymbols_mfe', 'goog.i18n.NumberFormatSymbols_mfe_MU', 'goog.i18n.NumberFormatSymbols_mg', 'goog.i18n.NumberFormatSymbols_mg_MG', 'goog.i18n.NumberFormatSymbols_mgh', 'goog.i18n.NumberFormatSymbols_mgh_MZ', 'goog.i18n.NumberFormatSymbols_mgo', 'goog.i18n.NumberFormatSymbols_mgo_CM', 'goog.i18n.NumberFormatSymbols_mn_Cyrl', 'goog.i18n.NumberFormatSymbols_ms_Latn', 'goog.i18n.NumberFormatSymbols_ms_Latn_BN', 'goog.i18n.NumberFormatSymbols_ms_Latn_SG', 'goog.i18n.NumberFormatSymbols_mua', 'goog.i18n.NumberFormatSymbols_mua_CM', 'goog.i18n.NumberFormatSymbols_naq', 'goog.i18n.NumberFormatSymbols_naq_NA', 'goog.i18n.NumberFormatSymbols_nd', 'goog.i18n.NumberFormatSymbols_nd_ZW', 'goog.i18n.NumberFormatSymbols_ne_IN', 'goog.i18n.NumberFormatSymbols_nl_AW', 'goog.i18n.NumberFormatSymbols_nl_BE', 'goog.i18n.NumberFormatSymbols_nl_BQ', 'goog.i18n.NumberFormatSymbols_nl_CW', 'goog.i18n.NumberFormatSymbols_nl_SR', 'goog.i18n.NumberFormatSymbols_nl_SX', 'goog.i18n.NumberFormatSymbols_nmg', 'goog.i18n.NumberFormatSymbols_nmg_CM', 'goog.i18n.NumberFormatSymbols_nn', 'goog.i18n.NumberFormatSymbols_nn_NO', 'goog.i18n.NumberFormatSymbols_nnh', 'goog.i18n.NumberFormatSymbols_nnh_CM', 'goog.i18n.NumberFormatSymbols_nr', 'goog.i18n.NumberFormatSymbols_nr_ZA', 'goog.i18n.NumberFormatSymbols_nso', 'goog.i18n.NumberFormatSymbols_nso_ZA', 'goog.i18n.NumberFormatSymbols_nus', 'goog.i18n.NumberFormatSymbols_nus_SD', 'goog.i18n.NumberFormatSymbols_nyn', 'goog.i18n.NumberFormatSymbols_nyn_UG', 'goog.i18n.NumberFormatSymbols_om', 'goog.i18n.NumberFormatSymbols_om_ET', 'goog.i18n.NumberFormatSymbols_om_KE', 'goog.i18n.NumberFormatSymbols_os', 'goog.i18n.NumberFormatSymbols_os_GE', 'goog.i18n.NumberFormatSymbols_os_RU', 'goog.i18n.NumberFormatSymbols_pa_Arab', 'goog.i18n.NumberFormatSymbols_pa_Arab_PK', 'goog.i18n.NumberFormatSymbols_pa_Guru', 'goog.i18n.NumberFormatSymbols_ps', 'goog.i18n.NumberFormatSymbols_ps_AF', 'goog.i18n.NumberFormatSymbols_pt_AO', 'goog.i18n.NumberFormatSymbols_pt_CV', 'goog.i18n.NumberFormatSymbols_pt_GW', 'goog.i18n.NumberFormatSymbols_pt_MO', 'goog.i18n.NumberFormatSymbols_pt_MZ', 'goog.i18n.NumberFormatSymbols_pt_ST', 'goog.i18n.NumberFormatSymbols_pt_TL', 'goog.i18n.NumberFormatSymbols_rm', 'goog.i18n.NumberFormatSymbols_rm_CH', 'goog.i18n.NumberFormatSymbols_rn', 'goog.i18n.NumberFormatSymbols_rn_BI', 'goog.i18n.NumberFormatSymbols_ro_MD', 'goog.i18n.NumberFormatSymbols_rof', 'goog.i18n.NumberFormatSymbols_rof_TZ', 'goog.i18n.NumberFormatSymbols_ru_BY', 'goog.i18n.NumberFormatSymbols_ru_KG', 'goog.i18n.NumberFormatSymbols_ru_KZ', 'goog.i18n.NumberFormatSymbols_ru_MD', 'goog.i18n.NumberFormatSymbols_ru_UA', 'goog.i18n.NumberFormatSymbols_rw', 'goog.i18n.NumberFormatSymbols_rw_RW', 'goog.i18n.NumberFormatSymbols_rwk', 'goog.i18n.NumberFormatSymbols_rwk_TZ', 'goog.i18n.NumberFormatSymbols_sah', 'goog.i18n.NumberFormatSymbols_sah_RU', 'goog.i18n.NumberFormatSymbols_saq', 'goog.i18n.NumberFormatSymbols_saq_KE', 'goog.i18n.NumberFormatSymbols_sbp', 'goog.i18n.NumberFormatSymbols_sbp_TZ', 'goog.i18n.NumberFormatSymbols_se', 'goog.i18n.NumberFormatSymbols_se_FI', 'goog.i18n.NumberFormatSymbols_se_NO', 'goog.i18n.NumberFormatSymbols_seh', 'goog.i18n.NumberFormatSymbols_seh_MZ', 'goog.i18n.NumberFormatSymbols_ses', 'goog.i18n.NumberFormatSymbols_ses_ML', 'goog.i18n.NumberFormatSymbols_sg', 'goog.i18n.NumberFormatSymbols_sg_CF', 'goog.i18n.NumberFormatSymbols_shi', 'goog.i18n.NumberFormatSymbols_shi_Latn', 'goog.i18n.NumberFormatSymbols_shi_Latn_MA', 'goog.i18n.NumberFormatSymbols_shi_Tfng', 'goog.i18n.NumberFormatSymbols_shi_Tfng_MA', 'goog.i18n.NumberFormatSymbols_sn', 'goog.i18n.NumberFormatSymbols_sn_ZW', 'goog.i18n.NumberFormatSymbols_so', 'goog.i18n.NumberFormatSymbols_so_DJ', 'goog.i18n.NumberFormatSymbols_so_ET', 'goog.i18n.NumberFormatSymbols_so_KE', 'goog.i18n.NumberFormatSymbols_so_SO', 'goog.i18n.NumberFormatSymbols_sq_MK', 'goog.i18n.NumberFormatSymbols_sq_XK', 'goog.i18n.NumberFormatSymbols_sr_Cyrl', 'goog.i18n.NumberFormatSymbols_sr_Cyrl_BA', 'goog.i18n.NumberFormatSymbols_sr_Cyrl_ME', 'goog.i18n.NumberFormatSymbols_sr_Cyrl_XK', 'goog.i18n.NumberFormatSymbols_sr_Latn', 'goog.i18n.NumberFormatSymbols_sr_Latn_BA', 'goog.i18n.NumberFormatSymbols_sr_Latn_ME', 'goog.i18n.NumberFormatSymbols_sr_Latn_RS', 'goog.i18n.NumberFormatSymbols_sr_Latn_XK', 'goog.i18n.NumberFormatSymbols_ss', 'goog.i18n.NumberFormatSymbols_ss_SZ', 'goog.i18n.NumberFormatSymbols_ss_ZA', 'goog.i18n.NumberFormatSymbols_ssy', 'goog.i18n.NumberFormatSymbols_ssy_ER', 'goog.i18n.NumberFormatSymbols_st', 'goog.i18n.NumberFormatSymbols_st_LS', 'goog.i18n.NumberFormatSymbols_st_ZA', 'goog.i18n.NumberFormatSymbols_sv_AX', 'goog.i18n.NumberFormatSymbols_sv_FI', 'goog.i18n.NumberFormatSymbols_sw_KE', 'goog.i18n.NumberFormatSymbols_sw_UG', 'goog.i18n.NumberFormatSymbols_swc', 'goog.i18n.NumberFormatSymbols_swc_CD', 'goog.i18n.NumberFormatSymbols_ta_LK', 'goog.i18n.NumberFormatSymbols_ta_MY', 'goog.i18n.NumberFormatSymbols_ta_SG', 'goog.i18n.NumberFormatSymbols_teo', 'goog.i18n.NumberFormatSymbols_teo_KE', 'goog.i18n.NumberFormatSymbols_teo_UG', 'goog.i18n.NumberFormatSymbols_tg', 'goog.i18n.NumberFormatSymbols_tg_Cyrl', 'goog.i18n.NumberFormatSymbols_tg_Cyrl_TJ', 'goog.i18n.NumberFormatSymbols_ti', 'goog.i18n.NumberFormatSymbols_ti_ER', 'goog.i18n.NumberFormatSymbols_ti_ET', 'goog.i18n.NumberFormatSymbols_tig', 'goog.i18n.NumberFormatSymbols_tig_ER', 'goog.i18n.NumberFormatSymbols_tn', 'goog.i18n.NumberFormatSymbols_tn_BW', 'goog.i18n.NumberFormatSymbols_tn_ZA', 'goog.i18n.NumberFormatSymbols_to', 'goog.i18n.NumberFormatSymbols_to_TO', 'goog.i18n.NumberFormatSymbols_tr_CY', 'goog.i18n.NumberFormatSymbols_ts', 'goog.i18n.NumberFormatSymbols_ts_ZA', 'goog.i18n.NumberFormatSymbols_twq', 'goog.i18n.NumberFormatSymbols_twq_NE', 'goog.i18n.NumberFormatSymbols_tzm', 'goog.i18n.NumberFormatSymbols_tzm_Latn', 'goog.i18n.NumberFormatSymbols_tzm_Latn_MA', 'goog.i18n.NumberFormatSymbols_ug', 'goog.i18n.NumberFormatSymbols_ug_Arab', 'goog.i18n.NumberFormatSymbols_ug_Arab_CN', 'goog.i18n.NumberFormatSymbols_ur_IN', 'goog.i18n.NumberFormatSymbols_uz_Arab', 'goog.i18n.NumberFormatSymbols_uz_Arab_AF', 'goog.i18n.NumberFormatSymbols_uz_Cyrl', 'goog.i18n.NumberFormatSymbols_uz_Cyrl_UZ', 'goog.i18n.NumberFormatSymbols_uz_Latn', 'goog.i18n.NumberFormatSymbols_vai', 'goog.i18n.NumberFormatSymbols_vai_Latn', 'goog.i18n.NumberFormatSymbols_vai_Latn_LR', 'goog.i18n.NumberFormatSymbols_vai_Vaii', 'goog.i18n.NumberFormatSymbols_vai_Vaii_LR', 'goog.i18n.NumberFormatSymbols_ve', 'goog.i18n.NumberFormatSymbols_ve_ZA', 'goog.i18n.NumberFormatSymbols_vo', 'goog.i18n.NumberFormatSymbols_vo_001', 'goog.i18n.NumberFormatSymbols_vun', 'goog.i18n.NumberFormatSymbols_vun_TZ', 'goog.i18n.NumberFormatSymbols_wae', 'goog.i18n.NumberFormatSymbols_wae_CH', 'goog.i18n.NumberFormatSymbols_wal', 'goog.i18n.NumberFormatSymbols_wal_ET', 'goog.i18n.NumberFormatSymbols_xh', 'goog.i18n.NumberFormatSymbols_xh_ZA', 'goog.i18n.NumberFormatSymbols_xog', 'goog.i18n.NumberFormatSymbols_xog_UG', 'goog.i18n.NumberFormatSymbols_yav', 'goog.i18n.NumberFormatSymbols_yav_CM', 'goog.i18n.NumberFormatSymbols_yo', 'goog.i18n.NumberFormatSymbols_yo_BJ', 'goog.i18n.NumberFormatSymbols_yo_NG', 'goog.i18n.NumberFormatSymbols_zgh', 'goog.i18n.NumberFormatSymbols_zgh_MA', 'goog.i18n.NumberFormatSymbols_zh_Hans', 'goog.i18n.NumberFormatSymbols_zh_Hans_HK', 'goog.i18n.NumberFormatSymbols_zh_Hans_MO', 'goog.i18n.NumberFormatSymbols_zh_Hans_SG', 'goog.i18n.NumberFormatSymbols_zh_Hant', 'goog.i18n.NumberFormatSymbols_zh_Hant_HK', 'goog.i18n.NumberFormatSymbols_zh_Hant_MO', 'goog.i18n.NumberFormatSymbols_zh_Hant_TW'], ['goog.i18n.NumberFormatSymbols']);
goog.addDependency('i18n/ordinalrules.js', ['goog.i18n.ordinalRules'], []);
goog.addDependency('i18n/pluralrules.js', ['goog.i18n.pluralRules'], []);
goog.addDependency('i18n/pluralrules_test.js', ['goog.i18n.pluralRulesTest'], ['goog.i18n.pluralRules', 'goog.testing.jsunit']);
goog.addDependency('i18n/timezone.js', ['goog.i18n.TimeZone'], ['goog.array', 'goog.date.DateLike', 'goog.string']);
goog.addDependency('i18n/timezone_test.js', ['goog.i18n.TimeZoneTest'], ['goog.i18n.TimeZone', 'goog.testing.jsunit']);
goog.addDependency('i18n/uchar.js', ['goog.i18n.uChar'], []);
goog.addDependency('i18n/uchar/localnamefetcher.js', ['goog.i18n.uChar.LocalNameFetcher'], ['goog.i18n.uChar', 'goog.i18n.uChar.NameFetcher', 'goog.log']);
goog.addDependency('i18n/uchar/localnamefetcher_test.js', ['goog.i18n.uChar.LocalNameFetcherTest'], ['goog.i18n.uChar.LocalNameFetcher', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('i18n/uchar/namefetcher.js', ['goog.i18n.uChar.NameFetcher'], []);
goog.addDependency('i18n/uchar/remotenamefetcher.js', ['goog.i18n.uChar.RemoteNameFetcher'], ['goog.Disposable', 'goog.Uri', 'goog.i18n.uChar', 'goog.i18n.uChar.NameFetcher', 'goog.log', 'goog.net.XhrIo', 'goog.structs.Map']);
goog.addDependency('i18n/uchar/remotenamefetcher_test.js', ['goog.i18n.uChar.RemoteNameFetcherTest'], ['goog.i18n.uChar.RemoteNameFetcher', 'goog.net.XhrIo', 'goog.testing.jsunit', 'goog.testing.net.XhrIo', 'goog.testing.recordFunction']);
goog.addDependency('i18n/uchar_test.js', ['goog.i18n.uCharTest'], ['goog.i18n.uChar', 'goog.testing.jsunit']);
goog.addDependency('iter/iter.js', ['goog.iter', 'goog.iter.Iterable', 'goog.iter.Iterator', 'goog.iter.StopIteration'], ['goog.array', 'goog.asserts', 'goog.functions', 'goog.math']);
goog.addDependency('iter/iter_test.js', ['goog.iterTest'], ['goog.iter', 'goog.iter.Iterator', 'goog.iter.StopIteration', 'goog.testing.jsunit']);
goog.addDependency('json/evaljsonprocessor.js', ['goog.json.EvalJsonProcessor'], ['goog.json', 'goog.json.Processor', 'goog.json.Serializer']);
goog.addDependency('json/hybrid.js', ['goog.json.hybrid'], ['goog.asserts', 'goog.json']);
goog.addDependency('json/hybrid_test.js', ['goog.json.hybridTest'], ['goog.json', 'goog.json.hybrid', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.testing.recordFunction', 'goog.userAgent']);
goog.addDependency('json/hybridjsonprocessor.js', ['goog.json.HybridJsonProcessor'], ['goog.json.Processor', 'goog.json.hybrid']);
goog.addDependency('json/hybridjsonprocessor_test.js', ['goog.json.HybridJsonProcessorTest'], ['goog.json.HybridJsonProcessor', 'goog.json.hybrid', 'goog.testing.jsunit']);
goog.addDependency('json/json.js', ['goog.json', 'goog.json.Replacer', 'goog.json.Reviver', 'goog.json.Serializer'], []);
goog.addDependency('json/json_perf.js', ['goog.jsonPerf'], ['goog.dom', 'goog.json', 'goog.math', 'goog.string', 'goog.testing.PerformanceTable', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit']);
goog.addDependency('json/json_test.js', ['goog.jsonTest'], ['goog.functions', 'goog.json', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('json/nativejsonprocessor.js', ['goog.json.NativeJsonProcessor'], ['goog.asserts', 'goog.json', 'goog.json.Processor']);
goog.addDependency('json/processor.js', ['goog.json.Processor'], ['goog.string.Parser', 'goog.string.Stringifier']);
goog.addDependency('json/processor_test.js', ['goog.json.processorTest'], ['goog.json.EvalJsonProcessor', 'goog.json.NativeJsonProcessor', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('labs/dom/pagevisibilitymonitor.js', ['goog.labs.dom.PageVisibilityEvent', 'goog.labs.dom.PageVisibilityMonitor', 'goog.labs.dom.PageVisibilityState'], ['goog.dom', 'goog.dom.vendor', 'goog.events', 'goog.events.Event', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.memoize']);
goog.addDependency('labs/dom/pagevisibilitymonitor_test.js', ['goog.labs.dom.PageVisibilityMonitorTest'], ['goog.events', 'goog.functions', 'goog.labs.dom.PageVisibilityMonitor', 'goog.testing.PropertyReplacer', 'goog.testing.events', 'goog.testing.events.Event', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('labs/events/nondisposableeventtarget.js', ['goog.labs.events.NonDisposableEventTarget'], ['goog.array', 'goog.asserts', 'goog.events.Event', 'goog.events.Listenable', 'goog.events.ListenerMap', 'goog.object']);
goog.addDependency('labs/events/nondisposableeventtarget_test.js', ['goog.labs.events.NonDisposableEventTargetTest'], ['goog.events.Listenable', 'goog.events.eventTargetTester', 'goog.events.eventTargetTester.KeyType', 'goog.events.eventTargetTester.UnlistenReturnType', 'goog.labs.events.NonDisposableEventTarget', 'goog.testing.jsunit']);
goog.addDependency('labs/events/nondisposableeventtarget_via_googevents_test.js', ['goog.labs.events.NonDisposableEventTargetGoogEventsTest'], ['goog.events', 'goog.events.eventTargetTester', 'goog.events.eventTargetTester.KeyType', 'goog.events.eventTargetTester.UnlistenReturnType', 'goog.labs.events.NonDisposableEventTarget', 'goog.object', 'goog.testing', 'goog.testing.jsunit']);
goog.addDependency('labs/events/touch.js', ['goog.labs.events.touch', 'goog.labs.events.touch.TouchData'], ['goog.array', 'goog.asserts', 'goog.events.EventType', 'goog.string']);
goog.addDependency('labs/events/touch_test.js', ['goog.labs.events.touchTest'], ['goog.labs.events.touch', 'goog.testing.jsunit']);
goog.addDependency('labs/format/csv.js', ['goog.labs.format.csv', 'goog.labs.format.csv.ParseError', 'goog.labs.format.csv.Token'], ['goog.array', 'goog.asserts', 'goog.debug.Error', 'goog.object', 'goog.string', 'goog.string.newlines']);
goog.addDependency('labs/format/csv_test.js', ['goog.labs.format.csvTest'], ['goog.labs.format.csv', 'goog.labs.format.csv.ParseError', 'goog.object', 'goog.testing.asserts', 'goog.testing.jsunit']);
goog.addDependency('labs/html/attribute_rewriter.js', ['goog.labs.html.AttributeRewriter', 'goog.labs.html.AttributeValue', 'goog.labs.html.attributeRewriterPresubmitWorkaround'], []);
goog.addDependency('labs/html/sanitizer.js', ['goog.labs.html.Sanitizer'], ['goog.asserts', 'goog.html.SafeUrl', 'goog.labs.html.attributeRewriterPresubmitWorkaround', 'goog.labs.html.scrubber', 'goog.object', 'goog.string']);
goog.addDependency('labs/html/sanitizer_test.js', ['goog.labs.html.SanitizerTest'], ['goog.html.SafeUrl', 'goog.labs.html.Sanitizer', 'goog.string', 'goog.string.Const', 'goog.testing.jsunit']);
goog.addDependency('labs/html/scrubber.js', ['goog.labs.html.scrubber'], ['goog.array', 'goog.dom.tags', 'goog.labs.html.attributeRewriterPresubmitWorkaround', 'goog.string']);
goog.addDependency('labs/html/scrubber_test.js', ['goog.html.ScrubberTest'], ['goog.labs.html.scrubber', 'goog.object', 'goog.string', 'goog.testing.jsunit']);
goog.addDependency('labs/i18n/listformat.js', ['goog.labs.i18n.GenderInfo', 'goog.labs.i18n.GenderInfo.Gender', 'goog.labs.i18n.ListFormat'], ['goog.asserts', 'goog.labs.i18n.ListFormatSymbols']);
goog.addDependency('labs/i18n/listformat_test.js', ['goog.labs.i18n.ListFormatTest'], ['goog.labs.i18n.GenderInfo', 'goog.labs.i18n.ListFormat', 'goog.labs.i18n.ListFormatSymbols', 'goog.labs.i18n.ListFormatSymbols_el', 'goog.labs.i18n.ListFormatSymbols_en', 'goog.labs.i18n.ListFormatSymbols_fr', 'goog.labs.i18n.ListFormatSymbols_ml', 'goog.labs.i18n.ListFormatSymbols_zu', 'goog.testing.jsunit']);
goog.addDependency('labs/i18n/listsymbols.js', ['goog.labs.i18n.ListFormatSymbols', 'goog.labs.i18n.ListFormatSymbols_af', 'goog.labs.i18n.ListFormatSymbols_am', 'goog.labs.i18n.ListFormatSymbols_ar', 'goog.labs.i18n.ListFormatSymbols_az', 'goog.labs.i18n.ListFormatSymbols_bg', 'goog.labs.i18n.ListFormatSymbols_bn', 'goog.labs.i18n.ListFormatSymbols_br', 'goog.labs.i18n.ListFormatSymbols_ca', 'goog.labs.i18n.ListFormatSymbols_chr', 'goog.labs.i18n.ListFormatSymbols_cs', 'goog.labs.i18n.ListFormatSymbols_cy', 'goog.labs.i18n.ListFormatSymbols_da', 'goog.labs.i18n.ListFormatSymbols_de', 'goog.labs.i18n.ListFormatSymbols_de_AT', 'goog.labs.i18n.ListFormatSymbols_de_CH', 'goog.labs.i18n.ListFormatSymbols_el', 'goog.labs.i18n.ListFormatSymbols_en', 'goog.labs.i18n.ListFormatSymbols_en_AU', 'goog.labs.i18n.ListFormatSymbols_en_GB', 'goog.labs.i18n.ListFormatSymbols_en_IE', 'goog.labs.i18n.ListFormatSymbols_en_IN', 'goog.labs.i18n.ListFormatSymbols_en_ISO', 'goog.labs.i18n.ListFormatSymbols_en_SG', 'goog.labs.i18n.ListFormatSymbols_en_US', 'goog.labs.i18n.ListFormatSymbols_en_ZA', 'goog.labs.i18n.ListFormatSymbols_es', 'goog.labs.i18n.ListFormatSymbols_es_419', 'goog.labs.i18n.ListFormatSymbols_es_ES', 'goog.labs.i18n.ListFormatSymbols_et', 'goog.labs.i18n.ListFormatSymbols_eu', 'goog.labs.i18n.ListFormatSymbols_fa', 'goog.labs.i18n.ListFormatSymbols_fi', 'goog.labs.i18n.ListFormatSymbols_fil', 'goog.labs.i18n.ListFormatSymbols_fr', 'goog.labs.i18n.ListFormatSymbols_fr_CA', 'goog.labs.i18n.ListFormatSymbols_gl', 'goog.labs.i18n.ListFormatSymbols_gsw', 'goog.labs.i18n.ListFormatSymbols_gu', 'goog.labs.i18n.ListFormatSymbols_haw', 'goog.labs.i18n.ListFormatSymbols_he', 'goog.labs.i18n.ListFormatSymbols_hi', 'goog.labs.i18n.ListFormatSymbols_hr', 'goog.labs.i18n.ListFormatSymbols_hu', 'goog.labs.i18n.ListFormatSymbols_hy', 'goog.labs.i18n.ListFormatSymbols_id', 'goog.labs.i18n.ListFormatSymbols_in', 'goog.labs.i18n.ListFormatSymbols_is', 'goog.labs.i18n.ListFormatSymbols_it', 'goog.labs.i18n.ListFormatSymbols_iw', 'goog.labs.i18n.ListFormatSymbols_ja', 'goog.labs.i18n.ListFormatSymbols_ka', 'goog.labs.i18n.ListFormatSymbols_kk', 'goog.labs.i18n.ListFormatSymbols_km', 'goog.labs.i18n.ListFormatSymbols_kn', 'goog.labs.i18n.ListFormatSymbols_ko', 'goog.labs.i18n.ListFormatSymbols_ky', 'goog.labs.i18n.ListFormatSymbols_ln', 'goog.labs.i18n.ListFormatSymbols_lo', 'goog.labs.i18n.ListFormatSymbols_lt', 'goog.labs.i18n.ListFormatSymbols_lv', 'goog.labs.i18n.ListFormatSymbols_mk', 'goog.labs.i18n.ListFormatSymbols_ml', 'goog.labs.i18n.ListFormatSymbols_mn', 'goog.labs.i18n.ListFormatSymbols_mo', 'goog.labs.i18n.ListFormatSymbols_mr', 'goog.labs.i18n.ListFormatSymbols_ms', 'goog.labs.i18n.ListFormatSymbols_mt', 'goog.labs.i18n.ListFormatSymbols_my', 'goog.labs.i18n.ListFormatSymbols_nb', 'goog.labs.i18n.ListFormatSymbols_ne', 'goog.labs.i18n.ListFormatSymbols_nl', 'goog.labs.i18n.ListFormatSymbols_no', 'goog.labs.i18n.ListFormatSymbols_no_NO', 'goog.labs.i18n.ListFormatSymbols_or', 'goog.labs.i18n.ListFormatSymbols_pa', 'goog.labs.i18n.ListFormatSymbols_pl', 'goog.labs.i18n.ListFormatSymbols_pt', 'goog.labs.i18n.ListFormatSymbols_pt_BR', 'goog.labs.i18n.ListFormatSymbols_pt_PT', 'goog.labs.i18n.ListFormatSymbols_ro', 'goog.labs.i18n.ListFormatSymbols_ru', 'goog.labs.i18n.ListFormatSymbols_sh', 'goog.labs.i18n.ListFormatSymbols_si', 'goog.labs.i18n.ListFormatSymbols_sk', 'goog.labs.i18n.ListFormatSymbols_sl', 'goog.labs.i18n.ListFormatSymbols_sq', 'goog.labs.i18n.ListFormatSymbols_sr', 'goog.labs.i18n.ListFormatSymbols_sv', 'goog.labs.i18n.ListFormatSymbols_sw', 'goog.labs.i18n.ListFormatSymbols_ta', 'goog.labs.i18n.ListFormatSymbols_te', 'goog.labs.i18n.ListFormatSymbols_th', 'goog.labs.i18n.ListFormatSymbols_tl', 'goog.labs.i18n.ListFormatSymbols_tr', 'goog.labs.i18n.ListFormatSymbols_uk', 'goog.labs.i18n.ListFormatSymbols_ur', 'goog.labs.i18n.ListFormatSymbols_uz', 'goog.labs.i18n.ListFormatSymbols_vi', 'goog.labs.i18n.ListFormatSymbols_zh', 'goog.labs.i18n.ListFormatSymbols_zh_CN', 'goog.labs.i18n.ListFormatSymbols_zh_HK', 'goog.labs.i18n.ListFormatSymbols_zh_TW', 'goog.labs.i18n.ListFormatSymbols_zu'], []);
goog.addDependency('labs/i18n/listsymbolsext.js', ['goog.labs.i18n.ListFormatSymbolsExt', 'goog.labs.i18n.ListFormatSymbols_af_NA', 'goog.labs.i18n.ListFormatSymbols_af_ZA', 'goog.labs.i18n.ListFormatSymbols_agq', 'goog.labs.i18n.ListFormatSymbols_agq_CM', 'goog.labs.i18n.ListFormatSymbols_ak', 'goog.labs.i18n.ListFormatSymbols_ak_GH', 'goog.labs.i18n.ListFormatSymbols_am_ET', 'goog.labs.i18n.ListFormatSymbols_ar_001', 'goog.labs.i18n.ListFormatSymbols_ar_AE', 'goog.labs.i18n.ListFormatSymbols_ar_BH', 'goog.labs.i18n.ListFormatSymbols_ar_DJ', 'goog.labs.i18n.ListFormatSymbols_ar_DZ', 'goog.labs.i18n.ListFormatSymbols_ar_EG', 'goog.labs.i18n.ListFormatSymbols_ar_EH', 'goog.labs.i18n.ListFormatSymbols_ar_ER', 'goog.labs.i18n.ListFormatSymbols_ar_IL', 'goog.labs.i18n.ListFormatSymbols_ar_IQ', 'goog.labs.i18n.ListFormatSymbols_ar_JO', 'goog.labs.i18n.ListFormatSymbols_ar_KM', 'goog.labs.i18n.ListFormatSymbols_ar_KW', 'goog.labs.i18n.ListFormatSymbols_ar_LB', 'goog.labs.i18n.ListFormatSymbols_ar_LY', 'goog.labs.i18n.ListFormatSymbols_ar_MA', 'goog.labs.i18n.ListFormatSymbols_ar_MR', 'goog.labs.i18n.ListFormatSymbols_ar_OM', 'goog.labs.i18n.ListFormatSymbols_ar_PS', 'goog.labs.i18n.ListFormatSymbols_ar_QA', 'goog.labs.i18n.ListFormatSymbols_ar_SA', 'goog.labs.i18n.ListFormatSymbols_ar_SD', 'goog.labs.i18n.ListFormatSymbols_ar_SO', 'goog.labs.i18n.ListFormatSymbols_ar_SS', 'goog.labs.i18n.ListFormatSymbols_ar_SY', 'goog.labs.i18n.ListFormatSymbols_ar_TD', 'goog.labs.i18n.ListFormatSymbols_ar_TN', 'goog.labs.i18n.ListFormatSymbols_ar_YE', 'goog.labs.i18n.ListFormatSymbols_as', 'goog.labs.i18n.ListFormatSymbols_as_IN', 'goog.labs.i18n.ListFormatSymbols_asa', 'goog.labs.i18n.ListFormatSymbols_asa_TZ', 'goog.labs.i18n.ListFormatSymbols_az_Cyrl', 'goog.labs.i18n.ListFormatSymbols_az_Cyrl_AZ', 'goog.labs.i18n.ListFormatSymbols_az_Latn', 'goog.labs.i18n.ListFormatSymbols_az_Latn_AZ', 'goog.labs.i18n.ListFormatSymbols_bas', 'goog.labs.i18n.ListFormatSymbols_bas_CM', 'goog.labs.i18n.ListFormatSymbols_be', 'goog.labs.i18n.ListFormatSymbols_be_BY', 'goog.labs.i18n.ListFormatSymbols_bem', 'goog.labs.i18n.ListFormatSymbols_bem_ZM', 'goog.labs.i18n.ListFormatSymbols_bez', 'goog.labs.i18n.ListFormatSymbols_bez_TZ', 'goog.labs.i18n.ListFormatSymbols_bg_BG', 'goog.labs.i18n.ListFormatSymbols_bm', 'goog.labs.i18n.ListFormatSymbols_bm_ML', 'goog.labs.i18n.ListFormatSymbols_bn_BD', 'goog.labs.i18n.ListFormatSymbols_bn_IN', 'goog.labs.i18n.ListFormatSymbols_bo', 'goog.labs.i18n.ListFormatSymbols_bo_CN', 'goog.labs.i18n.ListFormatSymbols_bo_IN', 'goog.labs.i18n.ListFormatSymbols_br_FR', 'goog.labs.i18n.ListFormatSymbols_brx', 'goog.labs.i18n.ListFormatSymbols_brx_IN', 'goog.labs.i18n.ListFormatSymbols_bs', 'goog.labs.i18n.ListFormatSymbols_bs_Cyrl', 'goog.labs.i18n.ListFormatSymbols_bs_Cyrl_BA', 'goog.labs.i18n.ListFormatSymbols_bs_Latn', 'goog.labs.i18n.ListFormatSymbols_bs_Latn_BA', 'goog.labs.i18n.ListFormatSymbols_ca_AD', 'goog.labs.i18n.ListFormatSymbols_ca_ES', 'goog.labs.i18n.ListFormatSymbols_ca_FR', 'goog.labs.i18n.ListFormatSymbols_ca_IT', 'goog.labs.i18n.ListFormatSymbols_cgg', 'goog.labs.i18n.ListFormatSymbols_cgg_UG', 'goog.labs.i18n.ListFormatSymbols_chr_US', 'goog.labs.i18n.ListFormatSymbols_cs_CZ', 'goog.labs.i18n.ListFormatSymbols_cy_GB', 'goog.labs.i18n.ListFormatSymbols_da_DK', 'goog.labs.i18n.ListFormatSymbols_da_GL', 'goog.labs.i18n.ListFormatSymbols_dav', 'goog.labs.i18n.ListFormatSymbols_dav_KE', 'goog.labs.i18n.ListFormatSymbols_de_BE', 'goog.labs.i18n.ListFormatSymbols_de_DE', 'goog.labs.i18n.ListFormatSymbols_de_LI', 'goog.labs.i18n.ListFormatSymbols_de_LU', 'goog.labs.i18n.ListFormatSymbols_dje', 'goog.labs.i18n.ListFormatSymbols_dje_NE', 'goog.labs.i18n.ListFormatSymbols_dua', 'goog.labs.i18n.ListFormatSymbols_dua_CM', 'goog.labs.i18n.ListFormatSymbols_dyo', 'goog.labs.i18n.ListFormatSymbols_dyo_SN', 'goog.labs.i18n.ListFormatSymbols_dz', 'goog.labs.i18n.ListFormatSymbols_dz_BT', 'goog.labs.i18n.ListFormatSymbols_ebu', 'goog.labs.i18n.ListFormatSymbols_ebu_KE', 'goog.labs.i18n.ListFormatSymbols_ee', 'goog.labs.i18n.ListFormatSymbols_ee_GH', 'goog.labs.i18n.ListFormatSymbols_ee_TG', 'goog.labs.i18n.ListFormatSymbols_el_CY', 'goog.labs.i18n.ListFormatSymbols_el_GR', 'goog.labs.i18n.ListFormatSymbols_en_001', 'goog.labs.i18n.ListFormatSymbols_en_150', 'goog.labs.i18n.ListFormatSymbols_en_AG', 'goog.labs.i18n.ListFormatSymbols_en_AI', 'goog.labs.i18n.ListFormatSymbols_en_AS', 'goog.labs.i18n.ListFormatSymbols_en_BB', 'goog.labs.i18n.ListFormatSymbols_en_BE', 'goog.labs.i18n.ListFormatSymbols_en_BM', 'goog.labs.i18n.ListFormatSymbols_en_BS', 'goog.labs.i18n.ListFormatSymbols_en_BW', 'goog.labs.i18n.ListFormatSymbols_en_BZ', 'goog.labs.i18n.ListFormatSymbols_en_CA', 'goog.labs.i18n.ListFormatSymbols_en_CC', 'goog.labs.i18n.ListFormatSymbols_en_CK', 'goog.labs.i18n.ListFormatSymbols_en_CM', 'goog.labs.i18n.ListFormatSymbols_en_CX', 'goog.labs.i18n.ListFormatSymbols_en_DG', 'goog.labs.i18n.ListFormatSymbols_en_DM', 'goog.labs.i18n.ListFormatSymbols_en_ER', 'goog.labs.i18n.ListFormatSymbols_en_FJ', 'goog.labs.i18n.ListFormatSymbols_en_FK', 'goog.labs.i18n.ListFormatSymbols_en_FM', 'goog.labs.i18n.ListFormatSymbols_en_GD', 'goog.labs.i18n.ListFormatSymbols_en_GG', 'goog.labs.i18n.ListFormatSymbols_en_GH', 'goog.labs.i18n.ListFormatSymbols_en_GI', 'goog.labs.i18n.ListFormatSymbols_en_GM', 'goog.labs.i18n.ListFormatSymbols_en_GU', 'goog.labs.i18n.ListFormatSymbols_en_GY', 'goog.labs.i18n.ListFormatSymbols_en_HK', 'goog.labs.i18n.ListFormatSymbols_en_IM', 'goog.labs.i18n.ListFormatSymbols_en_IO', 'goog.labs.i18n.ListFormatSymbols_en_JE', 'goog.labs.i18n.ListFormatSymbols_en_JM', 'goog.labs.i18n.ListFormatSymbols_en_KE', 'goog.labs.i18n.ListFormatSymbols_en_KI', 'goog.labs.i18n.ListFormatSymbols_en_KN', 'goog.labs.i18n.ListFormatSymbols_en_KY', 'goog.labs.i18n.ListFormatSymbols_en_LC', 'goog.labs.i18n.ListFormatSymbols_en_LR', 'goog.labs.i18n.ListFormatSymbols_en_LS', 'goog.labs.i18n.ListFormatSymbols_en_MG', 'goog.labs.i18n.ListFormatSymbols_en_MH', 'goog.labs.i18n.ListFormatSymbols_en_MO', 'goog.labs.i18n.ListFormatSymbols_en_MP', 'goog.labs.i18n.ListFormatSymbols_en_MS', 'goog.labs.i18n.ListFormatSymbols_en_MT', 'goog.labs.i18n.ListFormatSymbols_en_MU', 'goog.labs.i18n.ListFormatSymbols_en_MW', 'goog.labs.i18n.ListFormatSymbols_en_NA', 'goog.labs.i18n.ListFormatSymbols_en_NF', 'goog.labs.i18n.ListFormatSymbols_en_NG', 'goog.labs.i18n.ListFormatSymbols_en_NR', 'goog.labs.i18n.ListFormatSymbols_en_NU', 'goog.labs.i18n.ListFormatSymbols_en_NZ', 'goog.labs.i18n.ListFormatSymbols_en_PG', 'goog.labs.i18n.ListFormatSymbols_en_PH', 'goog.labs.i18n.ListFormatSymbols_en_PK', 'goog.labs.i18n.ListFormatSymbols_en_PN', 'goog.labs.i18n.ListFormatSymbols_en_PR', 'goog.labs.i18n.ListFormatSymbols_en_PW', 'goog.labs.i18n.ListFormatSymbols_en_RW', 'goog.labs.i18n.ListFormatSymbols_en_SB', 'goog.labs.i18n.ListFormatSymbols_en_SC', 'goog.labs.i18n.ListFormatSymbols_en_SD', 'goog.labs.i18n.ListFormatSymbols_en_SH', 'goog.labs.i18n.ListFormatSymbols_en_SL', 'goog.labs.i18n.ListFormatSymbols_en_SS', 'goog.labs.i18n.ListFormatSymbols_en_SX', 'goog.labs.i18n.ListFormatSymbols_en_SZ', 'goog.labs.i18n.ListFormatSymbols_en_TC', 'goog.labs.i18n.ListFormatSymbols_en_TK', 'goog.labs.i18n.ListFormatSymbols_en_TO', 'goog.labs.i18n.ListFormatSymbols_en_TT', 'goog.labs.i18n.ListFormatSymbols_en_TV', 'goog.labs.i18n.ListFormatSymbols_en_TZ', 'goog.labs.i18n.ListFormatSymbols_en_UG', 'goog.labs.i18n.ListFormatSymbols_en_UM', 'goog.labs.i18n.ListFormatSymbols_en_US_POSIX', 'goog.labs.i18n.ListFormatSymbols_en_VC', 'goog.labs.i18n.ListFormatSymbols_en_VG', 'goog.labs.i18n.ListFormatSymbols_en_VI', 'goog.labs.i18n.ListFormatSymbols_en_VU', 'goog.labs.i18n.ListFormatSymbols_en_WS', 'goog.labs.i18n.ListFormatSymbols_en_ZM', 'goog.labs.i18n.ListFormatSymbols_en_ZW', 'goog.labs.i18n.ListFormatSymbols_eo', 'goog.labs.i18n.ListFormatSymbols_es_AR', 'goog.labs.i18n.ListFormatSymbols_es_BO', 'goog.labs.i18n.ListFormatSymbols_es_CL', 'goog.labs.i18n.ListFormatSymbols_es_CO', 'goog.labs.i18n.ListFormatSymbols_es_CR', 'goog.labs.i18n.ListFormatSymbols_es_CU', 'goog.labs.i18n.ListFormatSymbols_es_DO', 'goog.labs.i18n.ListFormatSymbols_es_EA', 'goog.labs.i18n.ListFormatSymbols_es_EC', 'goog.labs.i18n.ListFormatSymbols_es_GQ', 'goog.labs.i18n.ListFormatSymbols_es_GT', 'goog.labs.i18n.ListFormatSymbols_es_HN', 'goog.labs.i18n.ListFormatSymbols_es_IC', 'goog.labs.i18n.ListFormatSymbols_es_MX', 'goog.labs.i18n.ListFormatSymbols_es_NI', 'goog.labs.i18n.ListFormatSymbols_es_PA', 'goog.labs.i18n.ListFormatSymbols_es_PE', 'goog.labs.i18n.ListFormatSymbols_es_PH', 'goog.labs.i18n.ListFormatSymbols_es_PR', 'goog.labs.i18n.ListFormatSymbols_es_PY', 'goog.labs.i18n.ListFormatSymbols_es_SV', 'goog.labs.i18n.ListFormatSymbols_es_US', 'goog.labs.i18n.ListFormatSymbols_es_UY', 'goog.labs.i18n.ListFormatSymbols_es_VE', 'goog.labs.i18n.ListFormatSymbols_et_EE', 'goog.labs.i18n.ListFormatSymbols_eu_ES', 'goog.labs.i18n.ListFormatSymbols_ewo', 'goog.labs.i18n.ListFormatSymbols_ewo_CM', 'goog.labs.i18n.ListFormatSymbols_fa_AF', 'goog.labs.i18n.ListFormatSymbols_fa_IR', 'goog.labs.i18n.ListFormatSymbols_ff', 'goog.labs.i18n.ListFormatSymbols_ff_SN', 'goog.labs.i18n.ListFormatSymbols_fi_FI', 'goog.labs.i18n.ListFormatSymbols_fil_PH', 'goog.labs.i18n.ListFormatSymbols_fo', 'goog.labs.i18n.ListFormatSymbols_fo_FO', 'goog.labs.i18n.ListFormatSymbols_fr_BE', 'goog.labs.i18n.ListFormatSymbols_fr_BF', 'goog.labs.i18n.ListFormatSymbols_fr_BI', 'goog.labs.i18n.ListFormatSymbols_fr_BJ', 'goog.labs.i18n.ListFormatSymbols_fr_BL', 'goog.labs.i18n.ListFormatSymbols_fr_CD', 'goog.labs.i18n.ListFormatSymbols_fr_CF', 'goog.labs.i18n.ListFormatSymbols_fr_CG', 'goog.labs.i18n.ListFormatSymbols_fr_CH', 'goog.labs.i18n.ListFormatSymbols_fr_CI', 'goog.labs.i18n.ListFormatSymbols_fr_CM', 'goog.labs.i18n.ListFormatSymbols_fr_DJ', 'goog.labs.i18n.ListFormatSymbols_fr_DZ', 'goog.labs.i18n.ListFormatSymbols_fr_FR', 'goog.labs.i18n.ListFormatSymbols_fr_GA', 'goog.labs.i18n.ListFormatSymbols_fr_GF', 'goog.labs.i18n.ListFormatSymbols_fr_GN', 'goog.labs.i18n.ListFormatSymbols_fr_GP', 'goog.labs.i18n.ListFormatSymbols_fr_GQ', 'goog.labs.i18n.ListFormatSymbols_fr_HT', 'goog.labs.i18n.ListFormatSymbols_fr_KM', 'goog.labs.i18n.ListFormatSymbols_fr_LU', 'goog.labs.i18n.ListFormatSymbols_fr_MA', 'goog.labs.i18n.ListFormatSymbols_fr_MC', 'goog.labs.i18n.ListFormatSymbols_fr_MF', 'goog.labs.i18n.ListFormatSymbols_fr_MG', 'goog.labs.i18n.ListFormatSymbols_fr_ML', 'goog.labs.i18n.ListFormatSymbols_fr_MQ', 'goog.labs.i18n.ListFormatSymbols_fr_MR', 'goog.labs.i18n.ListFormatSymbols_fr_MU', 'goog.labs.i18n.ListFormatSymbols_fr_NC', 'goog.labs.i18n.ListFormatSymbols_fr_NE', 'goog.labs.i18n.ListFormatSymbols_fr_PF', 'goog.labs.i18n.ListFormatSymbols_fr_PM', 'goog.labs.i18n.ListFormatSymbols_fr_RE', 'goog.labs.i18n.ListFormatSymbols_fr_RW', 'goog.labs.i18n.ListFormatSymbols_fr_SC', 'goog.labs.i18n.ListFormatSymbols_fr_SN', 'goog.labs.i18n.ListFormatSymbols_fr_SY', 'goog.labs.i18n.ListFormatSymbols_fr_TD', 'goog.labs.i18n.ListFormatSymbols_fr_TG', 'goog.labs.i18n.ListFormatSymbols_fr_TN', 'goog.labs.i18n.ListFormatSymbols_fr_VU', 'goog.labs.i18n.ListFormatSymbols_fr_WF', 'goog.labs.i18n.ListFormatSymbols_fr_YT', 'goog.labs.i18n.ListFormatSymbols_ga', 'goog.labs.i18n.ListFormatSymbols_ga_IE', 'goog.labs.i18n.ListFormatSymbols_gl_ES', 'goog.labs.i18n.ListFormatSymbols_gsw_CH', 'goog.labs.i18n.ListFormatSymbols_gsw_LI', 'goog.labs.i18n.ListFormatSymbols_gu_IN', 'goog.labs.i18n.ListFormatSymbols_guz', 'goog.labs.i18n.ListFormatSymbols_guz_KE', 'goog.labs.i18n.ListFormatSymbols_gv', 'goog.labs.i18n.ListFormatSymbols_gv_IM', 'goog.labs.i18n.ListFormatSymbols_ha', 'goog.labs.i18n.ListFormatSymbols_ha_Latn', 'goog.labs.i18n.ListFormatSymbols_ha_Latn_GH', 'goog.labs.i18n.ListFormatSymbols_ha_Latn_NE', 'goog.labs.i18n.ListFormatSymbols_ha_Latn_NG', 'goog.labs.i18n.ListFormatSymbols_haw_US', 'goog.labs.i18n.ListFormatSymbols_he_IL', 'goog.labs.i18n.ListFormatSymbols_hi_IN', 'goog.labs.i18n.ListFormatSymbols_hr_BA', 'goog.labs.i18n.ListFormatSymbols_hr_HR', 'goog.labs.i18n.ListFormatSymbols_hu_HU', 'goog.labs.i18n.ListFormatSymbols_hy_AM', 'goog.labs.i18n.ListFormatSymbols_id_ID', 'goog.labs.i18n.ListFormatSymbols_ig', 'goog.labs.i18n.ListFormatSymbols_ig_NG', 'goog.labs.i18n.ListFormatSymbols_ii', 'goog.labs.i18n.ListFormatSymbols_ii_CN', 'goog.labs.i18n.ListFormatSymbols_is_IS', 'goog.labs.i18n.ListFormatSymbols_it_CH', 'goog.labs.i18n.ListFormatSymbols_it_IT', 'goog.labs.i18n.ListFormatSymbols_it_SM', 'goog.labs.i18n.ListFormatSymbols_ja_JP', 'goog.labs.i18n.ListFormatSymbols_jgo', 'goog.labs.i18n.ListFormatSymbols_jgo_CM', 'goog.labs.i18n.ListFormatSymbols_jmc', 'goog.labs.i18n.ListFormatSymbols_jmc_TZ', 'goog.labs.i18n.ListFormatSymbols_ka_GE', 'goog.labs.i18n.ListFormatSymbols_kab', 'goog.labs.i18n.ListFormatSymbols_kab_DZ', 'goog.labs.i18n.ListFormatSymbols_kam', 'goog.labs.i18n.ListFormatSymbols_kam_KE', 'goog.labs.i18n.ListFormatSymbols_kde', 'goog.labs.i18n.ListFormatSymbols_kde_TZ', 'goog.labs.i18n.ListFormatSymbols_kea', 'goog.labs.i18n.ListFormatSymbols_kea_CV', 'goog.labs.i18n.ListFormatSymbols_khq', 'goog.labs.i18n.ListFormatSymbols_khq_ML', 'goog.labs.i18n.ListFormatSymbols_ki', 'goog.labs.i18n.ListFormatSymbols_ki_KE', 'goog.labs.i18n.ListFormatSymbols_kk_Cyrl', 'goog.labs.i18n.ListFormatSymbols_kk_Cyrl_KZ', 'goog.labs.i18n.ListFormatSymbols_kkj', 'goog.labs.i18n.ListFormatSymbols_kkj_CM', 'goog.labs.i18n.ListFormatSymbols_kl', 'goog.labs.i18n.ListFormatSymbols_kl_GL', 'goog.labs.i18n.ListFormatSymbols_kln', 'goog.labs.i18n.ListFormatSymbols_kln_KE', 'goog.labs.i18n.ListFormatSymbols_km_KH', 'goog.labs.i18n.ListFormatSymbols_kn_IN', 'goog.labs.i18n.ListFormatSymbols_ko_KP', 'goog.labs.i18n.ListFormatSymbols_ko_KR', 'goog.labs.i18n.ListFormatSymbols_kok', 'goog.labs.i18n.ListFormatSymbols_kok_IN', 'goog.labs.i18n.ListFormatSymbols_ks', 'goog.labs.i18n.ListFormatSymbols_ks_Arab', 'goog.labs.i18n.ListFormatSymbols_ks_Arab_IN', 'goog.labs.i18n.ListFormatSymbols_ksb', 'goog.labs.i18n.ListFormatSymbols_ksb_TZ', 'goog.labs.i18n.ListFormatSymbols_ksf', 'goog.labs.i18n.ListFormatSymbols_ksf_CM', 'goog.labs.i18n.ListFormatSymbols_kw', 'goog.labs.i18n.ListFormatSymbols_kw_GB', 'goog.labs.i18n.ListFormatSymbols_ky_Cyrl', 'goog.labs.i18n.ListFormatSymbols_ky_Cyrl_KG', 'goog.labs.i18n.ListFormatSymbols_lag', 'goog.labs.i18n.ListFormatSymbols_lag_TZ', 'goog.labs.i18n.ListFormatSymbols_lg', 'goog.labs.i18n.ListFormatSymbols_lg_UG', 'goog.labs.i18n.ListFormatSymbols_lkt', 'goog.labs.i18n.ListFormatSymbols_lkt_US', 'goog.labs.i18n.ListFormatSymbols_ln_AO', 'goog.labs.i18n.ListFormatSymbols_ln_CD', 'goog.labs.i18n.ListFormatSymbols_ln_CF', 'goog.labs.i18n.ListFormatSymbols_ln_CG', 'goog.labs.i18n.ListFormatSymbols_lo_LA', 'goog.labs.i18n.ListFormatSymbols_lt_LT', 'goog.labs.i18n.ListFormatSymbols_lu', 'goog.labs.i18n.ListFormatSymbols_lu_CD', 'goog.labs.i18n.ListFormatSymbols_luo', 'goog.labs.i18n.ListFormatSymbols_luo_KE', 'goog.labs.i18n.ListFormatSymbols_luy', 'goog.labs.i18n.ListFormatSymbols_luy_KE', 'goog.labs.i18n.ListFormatSymbols_lv_LV', 'goog.labs.i18n.ListFormatSymbols_mas', 'goog.labs.i18n.ListFormatSymbols_mas_KE', 'goog.labs.i18n.ListFormatSymbols_mas_TZ', 'goog.labs.i18n.ListFormatSymbols_mer', 'goog.labs.i18n.ListFormatSymbols_mer_KE', 'goog.labs.i18n.ListFormatSymbols_mfe', 'goog.labs.i18n.ListFormatSymbols_mfe_MU', 'goog.labs.i18n.ListFormatSymbols_mg', 'goog.labs.i18n.ListFormatSymbols_mg_MG', 'goog.labs.i18n.ListFormatSymbols_mgh', 'goog.labs.i18n.ListFormatSymbols_mgh_MZ', 'goog.labs.i18n.ListFormatSymbols_mgo', 'goog.labs.i18n.ListFormatSymbols_mgo_CM', 'goog.labs.i18n.ListFormatSymbols_mk_MK', 'goog.labs.i18n.ListFormatSymbols_ml_IN', 'goog.labs.i18n.ListFormatSymbols_mn_Cyrl', 'goog.labs.i18n.ListFormatSymbols_mn_Cyrl_MN', 'goog.labs.i18n.ListFormatSymbols_mr_IN', 'goog.labs.i18n.ListFormatSymbols_ms_Latn', 'goog.labs.i18n.ListFormatSymbols_ms_Latn_BN', 'goog.labs.i18n.ListFormatSymbols_ms_Latn_MY', 'goog.labs.i18n.ListFormatSymbols_ms_Latn_SG', 'goog.labs.i18n.ListFormatSymbols_mt_MT', 'goog.labs.i18n.ListFormatSymbols_mua', 'goog.labs.i18n.ListFormatSymbols_mua_CM', 'goog.labs.i18n.ListFormatSymbols_my_MM', 'goog.labs.i18n.ListFormatSymbols_naq', 'goog.labs.i18n.ListFormatSymbols_naq_NA', 'goog.labs.i18n.ListFormatSymbols_nb_NO', 'goog.labs.i18n.ListFormatSymbols_nb_SJ', 'goog.labs.i18n.ListFormatSymbols_nd', 'goog.labs.i18n.ListFormatSymbols_nd_ZW', 'goog.labs.i18n.ListFormatSymbols_ne_IN', 'goog.labs.i18n.ListFormatSymbols_ne_NP', 'goog.labs.i18n.ListFormatSymbols_nl_AW', 'goog.labs.i18n.ListFormatSymbols_nl_BE', 'goog.labs.i18n.ListFormatSymbols_nl_BQ', 'goog.labs.i18n.ListFormatSymbols_nl_CW', 'goog.labs.i18n.ListFormatSymbols_nl_NL', 'goog.labs.i18n.ListFormatSymbols_nl_SR', 'goog.labs.i18n.ListFormatSymbols_nl_SX', 'goog.labs.i18n.ListFormatSymbols_nmg', 'goog.labs.i18n.ListFormatSymbols_nmg_CM', 'goog.labs.i18n.ListFormatSymbols_nn', 'goog.labs.i18n.ListFormatSymbols_nn_NO', 'goog.labs.i18n.ListFormatSymbols_nnh', 'goog.labs.i18n.ListFormatSymbols_nnh_CM', 'goog.labs.i18n.ListFormatSymbols_nus', 'goog.labs.i18n.ListFormatSymbols_nus_SD', 'goog.labs.i18n.ListFormatSymbols_nyn', 'goog.labs.i18n.ListFormatSymbols_nyn_UG', 'goog.labs.i18n.ListFormatSymbols_om', 'goog.labs.i18n.ListFormatSymbols_om_ET', 'goog.labs.i18n.ListFormatSymbols_om_KE', 'goog.labs.i18n.ListFormatSymbols_or_IN', 'goog.labs.i18n.ListFormatSymbols_pa_Arab', 'goog.labs.i18n.ListFormatSymbols_pa_Arab_PK', 'goog.labs.i18n.ListFormatSymbols_pa_Guru', 'goog.labs.i18n.ListFormatSymbols_pa_Guru_IN', 'goog.labs.i18n.ListFormatSymbols_pl_PL', 'goog.labs.i18n.ListFormatSymbols_ps', 'goog.labs.i18n.ListFormatSymbols_ps_AF', 'goog.labs.i18n.ListFormatSymbols_pt_AO', 'goog.labs.i18n.ListFormatSymbols_pt_CV', 'goog.labs.i18n.ListFormatSymbols_pt_GW', 'goog.labs.i18n.ListFormatSymbols_pt_MO', 'goog.labs.i18n.ListFormatSymbols_pt_MZ', 'goog.labs.i18n.ListFormatSymbols_pt_ST', 'goog.labs.i18n.ListFormatSymbols_pt_TL', 'goog.labs.i18n.ListFormatSymbols_rm', 'goog.labs.i18n.ListFormatSymbols_rm_CH', 'goog.labs.i18n.ListFormatSymbols_rn', 'goog.labs.i18n.ListFormatSymbols_rn_BI', 'goog.labs.i18n.ListFormatSymbols_ro_MD', 'goog.labs.i18n.ListFormatSymbols_ro_RO', 'goog.labs.i18n.ListFormatSymbols_rof', 'goog.labs.i18n.ListFormatSymbols_rof_TZ', 'goog.labs.i18n.ListFormatSymbols_ru_BY', 'goog.labs.i18n.ListFormatSymbols_ru_KG', 'goog.labs.i18n.ListFormatSymbols_ru_KZ', 'goog.labs.i18n.ListFormatSymbols_ru_MD', 'goog.labs.i18n.ListFormatSymbols_ru_RU', 'goog.labs.i18n.ListFormatSymbols_ru_UA', 'goog.labs.i18n.ListFormatSymbols_rw', 'goog.labs.i18n.ListFormatSymbols_rw_RW', 'goog.labs.i18n.ListFormatSymbols_rwk', 'goog.labs.i18n.ListFormatSymbols_rwk_TZ', 'goog.labs.i18n.ListFormatSymbols_saq', 'goog.labs.i18n.ListFormatSymbols_saq_KE', 'goog.labs.i18n.ListFormatSymbols_sbp', 'goog.labs.i18n.ListFormatSymbols_sbp_TZ', 'goog.labs.i18n.ListFormatSymbols_seh', 'goog.labs.i18n.ListFormatSymbols_seh_MZ', 'goog.labs.i18n.ListFormatSymbols_ses', 'goog.labs.i18n.ListFormatSymbols_ses_ML', 'goog.labs.i18n.ListFormatSymbols_sg', 'goog.labs.i18n.ListFormatSymbols_sg_CF', 'goog.labs.i18n.ListFormatSymbols_shi', 'goog.labs.i18n.ListFormatSymbols_shi_Latn', 'goog.labs.i18n.ListFormatSymbols_shi_Latn_MA', 'goog.labs.i18n.ListFormatSymbols_shi_Tfng', 'goog.labs.i18n.ListFormatSymbols_shi_Tfng_MA', 'goog.labs.i18n.ListFormatSymbols_si_LK', 'goog.labs.i18n.ListFormatSymbols_sk_SK', 'goog.labs.i18n.ListFormatSymbols_sl_SI', 'goog.labs.i18n.ListFormatSymbols_sn', 'goog.labs.i18n.ListFormatSymbols_sn_ZW', 'goog.labs.i18n.ListFormatSymbols_so', 'goog.labs.i18n.ListFormatSymbols_so_DJ', 'goog.labs.i18n.ListFormatSymbols_so_ET', 'goog.labs.i18n.ListFormatSymbols_so_KE', 'goog.labs.i18n.ListFormatSymbols_so_SO', 'goog.labs.i18n.ListFormatSymbols_sq_AL', 'goog.labs.i18n.ListFormatSymbols_sq_MK', 'goog.labs.i18n.ListFormatSymbols_sq_XK', 'goog.labs.i18n.ListFormatSymbols_sr_Cyrl', 'goog.labs.i18n.ListFormatSymbols_sr_Cyrl_BA', 'goog.labs.i18n.ListFormatSymbols_sr_Cyrl_ME', 'goog.labs.i18n.ListFormatSymbols_sr_Cyrl_RS', 'goog.labs.i18n.ListFormatSymbols_sr_Cyrl_XK', 'goog.labs.i18n.ListFormatSymbols_sr_Latn', 'goog.labs.i18n.ListFormatSymbols_sr_Latn_BA', 'goog.labs.i18n.ListFormatSymbols_sr_Latn_ME', 'goog.labs.i18n.ListFormatSymbols_sr_Latn_RS', 'goog.labs.i18n.ListFormatSymbols_sr_Latn_XK', 'goog.labs.i18n.ListFormatSymbols_sv_AX', 'goog.labs.i18n.ListFormatSymbols_sv_FI', 'goog.labs.i18n.ListFormatSymbols_sv_SE', 'goog.labs.i18n.ListFormatSymbols_sw_KE', 'goog.labs.i18n.ListFormatSymbols_sw_TZ', 'goog.labs.i18n.ListFormatSymbols_sw_UG', 'goog.labs.i18n.ListFormatSymbols_swc', 'goog.labs.i18n.ListFormatSymbols_swc_CD', 'goog.labs.i18n.ListFormatSymbols_ta_IN', 'goog.labs.i18n.ListFormatSymbols_ta_LK', 'goog.labs.i18n.ListFormatSymbols_ta_MY', 'goog.labs.i18n.ListFormatSymbols_ta_SG', 'goog.labs.i18n.ListFormatSymbols_te_IN', 'goog.labs.i18n.ListFormatSymbols_teo', 'goog.labs.i18n.ListFormatSymbols_teo_KE', 'goog.labs.i18n.ListFormatSymbols_teo_UG', 'goog.labs.i18n.ListFormatSymbols_th_TH', 'goog.labs.i18n.ListFormatSymbols_ti', 'goog.labs.i18n.ListFormatSymbols_ti_ER', 'goog.labs.i18n.ListFormatSymbols_ti_ET', 'goog.labs.i18n.ListFormatSymbols_to', 'goog.labs.i18n.ListFormatSymbols_to_TO', 'goog.labs.i18n.ListFormatSymbols_tr_CY', 'goog.labs.i18n.ListFormatSymbols_tr_TR', 'goog.labs.i18n.ListFormatSymbols_twq', 'goog.labs.i18n.ListFormatSymbols_twq_NE', 'goog.labs.i18n.ListFormatSymbols_tzm', 'goog.labs.i18n.ListFormatSymbols_tzm_Latn', 'goog.labs.i18n.ListFormatSymbols_tzm_Latn_MA', 'goog.labs.i18n.ListFormatSymbols_ug', 'goog.labs.i18n.ListFormatSymbols_ug_Arab', 'goog.labs.i18n.ListFormatSymbols_ug_Arab_CN', 'goog.labs.i18n.ListFormatSymbols_uk_UA', 'goog.labs.i18n.ListFormatSymbols_ur_IN', 'goog.labs.i18n.ListFormatSymbols_ur_PK', 'goog.labs.i18n.ListFormatSymbols_uz_Arab', 'goog.labs.i18n.ListFormatSymbols_uz_Arab_AF', 'goog.labs.i18n.ListFormatSymbols_uz_Cyrl', 'goog.labs.i18n.ListFormatSymbols_uz_Cyrl_UZ', 'goog.labs.i18n.ListFormatSymbols_uz_Latn', 'goog.labs.i18n.ListFormatSymbols_uz_Latn_UZ', 'goog.labs.i18n.ListFormatSymbols_vai', 'goog.labs.i18n.ListFormatSymbols_vai_Latn', 'goog.labs.i18n.ListFormatSymbols_vai_Latn_LR', 'goog.labs.i18n.ListFormatSymbols_vai_Vaii', 'goog.labs.i18n.ListFormatSymbols_vai_Vaii_LR', 'goog.labs.i18n.ListFormatSymbols_vi_VN', 'goog.labs.i18n.ListFormatSymbols_vun', 'goog.labs.i18n.ListFormatSymbols_vun_TZ', 'goog.labs.i18n.ListFormatSymbols_xog', 'goog.labs.i18n.ListFormatSymbols_xog_UG', 'goog.labs.i18n.ListFormatSymbols_yav', 'goog.labs.i18n.ListFormatSymbols_yav_CM', 'goog.labs.i18n.ListFormatSymbols_yo', 'goog.labs.i18n.ListFormatSymbols_yo_BJ', 'goog.labs.i18n.ListFormatSymbols_yo_NG', 'goog.labs.i18n.ListFormatSymbols_zgh', 'goog.labs.i18n.ListFormatSymbols_zgh_MA', 'goog.labs.i18n.ListFormatSymbols_zh_Hans', 'goog.labs.i18n.ListFormatSymbols_zh_Hans_CN', 'goog.labs.i18n.ListFormatSymbols_zh_Hans_HK', 'goog.labs.i18n.ListFormatSymbols_zh_Hans_MO', 'goog.labs.i18n.ListFormatSymbols_zh_Hans_SG', 'goog.labs.i18n.ListFormatSymbols_zh_Hant', 'goog.labs.i18n.ListFormatSymbols_zh_Hant_HK', 'goog.labs.i18n.ListFormatSymbols_zh_Hant_MO', 'goog.labs.i18n.ListFormatSymbols_zh_Hant_TW', 'goog.labs.i18n.ListFormatSymbols_zu_ZA'], ['goog.labs.i18n.ListFormatSymbols']);
goog.addDependency('labs/mock/mock.js', ['goog.labs.mock', 'goog.labs.mock.VerificationError'], ['goog.array', 'goog.asserts', 'goog.debug', 'goog.debug.Error', 'goog.functions', 'goog.object']);
goog.addDependency('labs/mock/mock_test.js', ['goog.labs.mockTest'], ['goog.array', 'goog.labs.mock', 'goog.labs.mock.VerificationError', 'goog.labs.testing.AnythingMatcher', 'goog.labs.testing.GreaterThanMatcher', 'goog.string', 'goog.testing.jsunit']);
goog.addDependency('labs/net/image.js', ['goog.labs.net.image'], ['goog.Promise', 'goog.events.EventHandler', 'goog.events.EventType', 'goog.net.EventType', 'goog.userAgent']);
goog.addDependency('labs/net/image_test.js', ['goog.labs.net.imageTest'], ['goog.events', 'goog.labs.net.image', 'goog.string', 'goog.testing.AsyncTestCase', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('labs/net/webchannel.js', ['goog.net.WebChannel'], ['goog.events', 'goog.events.Event']);
goog.addDependency('labs/net/webchannel/basetestchannel.js', ['goog.labs.net.webChannel.BaseTestChannel'], ['goog.labs.net.webChannel.Channel', 'goog.labs.net.webChannel.ChannelRequest', 'goog.labs.net.webChannel.requestStats', 'goog.labs.net.webChannel.requestStats.Stat']);
goog.addDependency('labs/net/webchannel/channel.js', ['goog.labs.net.webChannel.Channel'], []);
goog.addDependency('labs/net/webchannel/channelrequest.js', ['goog.labs.net.webChannel.ChannelRequest'], ['goog.Timer', 'goog.async.Throttle', 'goog.events.EventHandler', 'goog.labs.net.webChannel.requestStats', 'goog.labs.net.webChannel.requestStats.ServerReachability', 'goog.labs.net.webChannel.requestStats.Stat', 'goog.net.ErrorCode', 'goog.net.EventType', 'goog.net.XmlHttp', 'goog.object', 'goog.uri.utils.StandardQueryParam', 'goog.userAgent']);
goog.addDependency('labs/net/webchannel/channelrequest_test.js', ['goog.labs.net.webChannel.channelRequestTest'], ['goog.Uri', 'goog.functions', 'goog.labs.net.webChannel.ChannelRequest', 'goog.labs.net.webChannel.WebChannelDebug', 'goog.labs.net.webChannel.requestStats', 'goog.labs.net.webChannel.requestStats.ServerReachability', 'goog.testing.MockClock', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.testing.net.XhrIo', 'goog.testing.recordFunction']);
goog.addDependency('labs/net/webchannel/connectionstate.js', ['goog.labs.net.webChannel.ConnectionState'], []);
goog.addDependency('labs/net/webchannel/forwardchannelrequestpool.js', ['goog.labs.net.webChannel.ForwardChannelRequestPool'], ['goog.array', 'goog.string', 'goog.structs.Set']);
goog.addDependency('labs/net/webchannel/forwardchannelrequestpool_test.js', ['goog.labs.net.webChannel.forwardChannelRequestPoolTest'], ['goog.labs.net.webChannel.ChannelRequest', 'goog.labs.net.webChannel.ForwardChannelRequestPool', 'goog.testing.PropertyReplacer', 'goog.testing.asserts', 'goog.testing.jsunit']);
goog.addDependency('labs/net/webchannel/netutils.js', ['goog.labs.net.webChannel.netUtils'], ['goog.Uri', 'goog.labs.net.webChannel.WebChannelDebug']);
goog.addDependency('labs/net/webchannel/requeststats.js', ['goog.labs.net.webChannel.requestStats', 'goog.labs.net.webChannel.requestStats.Event', 'goog.labs.net.webChannel.requestStats.ServerReachability', 'goog.labs.net.webChannel.requestStats.ServerReachabilityEvent', 'goog.labs.net.webChannel.requestStats.Stat', 'goog.labs.net.webChannel.requestStats.StatEvent', 'goog.labs.net.webChannel.requestStats.TimingEvent'], ['goog.events.Event', 'goog.events.EventTarget']);
goog.addDependency('labs/net/webchannel/webchannelbase.js', ['goog.labs.net.webChannel.WebChannelBase'], ['goog.Uri', 'goog.array', 'goog.asserts', 'goog.debug.TextFormatter', 'goog.json', 'goog.labs.net.webChannel.BaseTestChannel', 'goog.labs.net.webChannel.Channel', 'goog.labs.net.webChannel.ChannelRequest', 'goog.labs.net.webChannel.ConnectionState', 'goog.labs.net.webChannel.ForwardChannelRequestPool', 'goog.labs.net.webChannel.WebChannelDebug', 'goog.labs.net.webChannel.Wire', 'goog.labs.net.webChannel.WireV8', 'goog.labs.net.webChannel.netUtils', 'goog.labs.net.webChannel.requestStats', 'goog.labs.net.webChannel.requestStats.Stat', 'goog.log', 'goog.net.XhrIo', 'goog.object', 'goog.string', 'goog.structs', 'goog.structs.CircularBuffer']);
goog.addDependency('labs/net/webchannel/webchannelbase_test.js', ['goog.labs.net.webChannel.webChannelBaseTest'], ['goog.Timer', 'goog.array', 'goog.dom', 'goog.functions', 'goog.json', 'goog.labs.net.webChannel.ChannelRequest', 'goog.labs.net.webChannel.ForwardChannelRequestPool', 'goog.labs.net.webChannel.WebChannelBase', 'goog.labs.net.webChannel.WebChannelBaseTransport', 'goog.labs.net.webChannel.WebChannelDebug', 'goog.labs.net.webChannel.Wire', 'goog.labs.net.webChannel.netUtils', 'goog.labs.net.webChannel.requestStats', 'goog.labs.net.webChannel.requestStats.Stat', 'goog.structs.Map', 'goog.testing.MockClock', 'goog.testing.PropertyReplacer', 'goog.testing.asserts', 'goog.testing.jsunit']);
goog.addDependency('labs/net/webchannel/webchannelbasetransport.js', ['goog.labs.net.webChannel.WebChannelBaseTransport'], ['goog.asserts', 'goog.events.EventTarget', 'goog.labs.net.webChannel.WebChannelBase', 'goog.log', 'goog.net.WebChannel', 'goog.net.WebChannelTransport', 'goog.string.path']);
goog.addDependency('labs/net/webchannel/webchannelbasetransport_test.js', ['goog.labs.net.webChannel.webChannelBaseTransportTest'], ['goog.events', 'goog.labs.net.webChannel.WebChannelBaseTransport', 'goog.net.WebChannel', 'goog.testing.jsunit']);
goog.addDependency('labs/net/webchannel/webchanneldebug.js', ['goog.labs.net.webChannel.WebChannelDebug'], ['goog.json', 'goog.log']);
goog.addDependency('labs/net/webchannel/wire.js', ['goog.labs.net.webChannel.Wire'], []);
goog.addDependency('labs/net/webchannel/wirev8.js', ['goog.labs.net.webChannel.WireV8'], ['goog.asserts', 'goog.json', 'goog.json.EvalJsonProcessor', 'goog.structs']);
goog.addDependency('labs/net/webchannel/wirev8_test.js', ['goog.labs.net.webChannel.WireV8Test'], ['goog.labs.net.webChannel.WireV8', 'goog.testing.jsunit']);
goog.addDependency('labs/net/webchanneltransport.js', ['goog.net.WebChannelTransport'], []);
goog.addDependency('labs/net/webchanneltransportfactory.js', ['goog.net.createWebChannelTransport'], ['goog.functions', 'goog.labs.net.webChannel.WebChannelBaseTransport']);
goog.addDependency('labs/net/xhr.js', ['goog.labs.net.xhr', 'goog.labs.net.xhr.Error', 'goog.labs.net.xhr.HttpError', 'goog.labs.net.xhr.Options', 'goog.labs.net.xhr.PostData', 'goog.labs.net.xhr.TimeoutError'], ['goog.Promise', 'goog.debug.Error', 'goog.json', 'goog.net.HttpStatus', 'goog.net.XmlHttp', 'goog.string', 'goog.uri.utils']);
goog.addDependency('labs/net/xhr_test.js', ['goog.labs.net.xhrTest'], ['goog.Promise', 'goog.labs.net.xhr', 'goog.net.XmlHttp', 'goog.string', 'goog.testing.AsyncTestCase', 'goog.testing.MockClock', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('labs/object/object.js', ['goog.labs.object'], []);
goog.addDependency('labs/object/object_test.js', ['goog.labs.objectTest'], ['goog.labs.object', 'goog.testing.jsunit']);
goog.addDependency('labs/promise/promise.js', ['goog.labs.Promise', 'goog.labs.Resolver'], ['goog.Promise', 'goog.Thenable', 'goog.promise.Resolver']);
goog.addDependency('labs/storage/boundedcollectablestorage.js', ['goog.labs.storage.BoundedCollectableStorage'], ['goog.array', 'goog.asserts', 'goog.iter', 'goog.storage.CollectableStorage', 'goog.storage.ErrorCode', 'goog.storage.ExpiringStorage']);
goog.addDependency('labs/storage/boundedcollectablestorage_test.js', ['goog.labs.storage.BoundedCollectableStorageTest'], ['goog.labs.storage.BoundedCollectableStorage', 'goog.storage.collectableStorageTester', 'goog.storage.storage_test', 'goog.testing.MockClock', 'goog.testing.jsunit', 'goog.testing.storage.FakeMechanism']);
goog.addDependency('labs/structs/map.js', ['goog.labs.structs.Map'], ['goog.array', 'goog.asserts', 'goog.labs.object', 'goog.object']);
goog.addDependency('labs/structs/map_perf.js', ['goog.labs.structs.mapPerf'], ['goog.dom', 'goog.labs.structs.Map', 'goog.structs.Map', 'goog.testing.PerformanceTable', 'goog.testing.jsunit']);
goog.addDependency('labs/structs/map_test.js', ['goog.labs.structs.MapTest'], ['goog.labs.structs.Map', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit']);
goog.addDependency('labs/structs/multimap.js', ['goog.labs.structs.Multimap'], ['goog.array', 'goog.labs.object', 'goog.labs.structs.Map']);
goog.addDependency('labs/structs/multimap_test.js', ['goog.labs.structs.MultimapTest'], ['goog.labs.structs.Map', 'goog.labs.structs.Multimap', 'goog.testing.jsunit']);
goog.addDependency('labs/style/pixeldensitymonitor.js', ['goog.labs.style.PixelDensityMonitor', 'goog.labs.style.PixelDensityMonitor.Density', 'goog.labs.style.PixelDensityMonitor.EventType'], ['goog.events', 'goog.events.EventTarget']);
goog.addDependency('labs/style/pixeldensitymonitor_test.js', ['goog.labs.style.PixelDensityMonitorTest'], ['goog.array', 'goog.dom.DomHelper', 'goog.events', 'goog.labs.style.PixelDensityMonitor', 'goog.testing.MockControl', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('labs/testing/assertthat.js', ['goog.labs.testing.MatcherError', 'goog.labs.testing.assertThat'], ['goog.asserts', 'goog.debug.Error', 'goog.labs.testing.Matcher']);
goog.addDependency('labs/testing/assertthat_test.js', ['goog.labs.testing.assertThatTest'], ['goog.labs.testing.MatcherError', 'goog.labs.testing.assertThat', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('labs/testing/decoratormatcher.js', ['goog.labs.testing.AnythingMatcher'], ['goog.labs.testing.Matcher']);
goog.addDependency('labs/testing/decoratormatcher_test.js', ['goog.labs.testing.decoratorMatcherTest'], ['goog.labs.testing.AnythingMatcher', 'goog.labs.testing.GreaterThanMatcher', 'goog.labs.testing.MatcherError', 'goog.labs.testing.assertThat', 'goog.testing.jsunit']);
goog.addDependency('labs/testing/dictionarymatcher.js', ['goog.labs.testing.HasEntriesMatcher', 'goog.labs.testing.HasEntryMatcher', 'goog.labs.testing.HasKeyMatcher', 'goog.labs.testing.HasValueMatcher'], ['goog.array', 'goog.asserts', 'goog.labs.testing.Matcher', 'goog.string']);
goog.addDependency('labs/testing/dictionarymatcher_test.js', ['goog.labs.testing.dictionaryMatcherTest'], ['goog.labs.testing.HasEntryMatcher', 'goog.labs.testing.MatcherError', 'goog.labs.testing.assertThat', 'goog.testing.jsunit']);
goog.addDependency('labs/testing/environment.js', ['goog.labs.testing.Environment'], ['goog.array', 'goog.testing.TestCase', 'goog.testing.jsunit']);
goog.addDependency('labs/testing/environment_test.js', ['goog.labs.testing.environmentTest'], ['goog.labs.testing.Environment', 'goog.testing.MockControl', 'goog.testing.TestCase', 'goog.testing.jsunit']);
goog.addDependency('labs/testing/environment_usage_test.js', ['goog.labs.testing.environmentUsageTest'], ['goog.labs.testing.Environment']);
goog.addDependency('labs/testing/logicmatcher.js', ['goog.labs.testing.AllOfMatcher', 'goog.labs.testing.AnyOfMatcher', 'goog.labs.testing.IsNotMatcher'], ['goog.array', 'goog.labs.testing.Matcher']);
goog.addDependency('labs/testing/logicmatcher_test.js', ['goog.labs.testing.logicMatcherTest'], ['goog.labs.testing.AllOfMatcher', 'goog.labs.testing.GreaterThanMatcher', 'goog.labs.testing.MatcherError', 'goog.labs.testing.assertThat', 'goog.testing.jsunit']);
goog.addDependency('labs/testing/matcher.js', ['goog.labs.testing.Matcher'], []);
goog.addDependency('labs/testing/numbermatcher.js', ['goog.labs.testing.CloseToMatcher', 'goog.labs.testing.EqualToMatcher', 'goog.labs.testing.GreaterThanEqualToMatcher', 'goog.labs.testing.GreaterThanMatcher', 'goog.labs.testing.LessThanEqualToMatcher', 'goog.labs.testing.LessThanMatcher'], ['goog.asserts', 'goog.labs.testing.Matcher']);
goog.addDependency('labs/testing/numbermatcher_test.js', ['goog.labs.testing.numberMatcherTest'], ['goog.labs.testing.LessThanMatcher', 'goog.labs.testing.MatcherError', 'goog.labs.testing.assertThat', 'goog.testing.jsunit']);
goog.addDependency('labs/testing/objectmatcher.js', ['goog.labs.testing.HasPropertyMatcher', 'goog.labs.testing.InstanceOfMatcher', 'goog.labs.testing.IsNullMatcher', 'goog.labs.testing.IsNullOrUndefinedMatcher', 'goog.labs.testing.IsUndefinedMatcher', 'goog.labs.testing.ObjectEqualsMatcher'], ['goog.labs.testing.Matcher', 'goog.string']);
goog.addDependency('labs/testing/objectmatcher_test.js', ['goog.labs.testing.objectMatcherTest'], ['goog.labs.testing.MatcherError', 'goog.labs.testing.ObjectEqualsMatcher', 'goog.labs.testing.assertThat', 'goog.testing.jsunit']);
goog.addDependency('labs/testing/stringmatcher.js', ['goog.labs.testing.ContainsStringMatcher', 'goog.labs.testing.EndsWithMatcher', 'goog.labs.testing.EqualToIgnoringCaseMatcher', 'goog.labs.testing.EqualToIgnoringWhitespaceMatcher', 'goog.labs.testing.EqualsMatcher', 'goog.labs.testing.RegexMatcher', 'goog.labs.testing.StartsWithMatcher', 'goog.labs.testing.StringContainsInOrderMatcher'], ['goog.asserts', 'goog.labs.testing.Matcher', 'goog.string']);
goog.addDependency('labs/testing/stringmatcher_test.js', ['goog.labs.testing.stringMatcherTest'], ['goog.labs.testing.MatcherError', 'goog.labs.testing.StringContainsInOrderMatcher', 'goog.labs.testing.assertThat', 'goog.testing.jsunit']);
goog.addDependency('labs/useragent/browser.js', ['goog.labs.userAgent.browser'], ['goog.array', 'goog.asserts', 'goog.labs.userAgent.util', 'goog.string']);
goog.addDependency('labs/useragent/browser_test.js', ['goog.labs.userAgent.browserTest'], ['goog.labs.userAgent.browser', 'goog.labs.userAgent.testAgents', 'goog.labs.userAgent.util', 'goog.testing.jsunit']);
goog.addDependency('labs/useragent/device.js', ['goog.labs.userAgent.device'], ['goog.labs.userAgent.util']);
goog.addDependency('labs/useragent/device_test.js', ['goog.labs.userAgent.deviceTest'], ['goog.labs.userAgent.device', 'goog.labs.userAgent.testAgents', 'goog.labs.userAgent.util', 'goog.testing.jsunit']);
goog.addDependency('labs/useragent/engine.js', ['goog.labs.userAgent.engine'], ['goog.array', 'goog.labs.userAgent.util', 'goog.string']);
goog.addDependency('labs/useragent/engine_test.js', ['goog.labs.userAgent.engineTest'], ['goog.labs.userAgent.engine', 'goog.labs.userAgent.testAgents', 'goog.labs.userAgent.util', 'goog.testing.jsunit']);
goog.addDependency('labs/useragent/platform.js', ['goog.labs.userAgent.platform'], ['goog.labs.userAgent.util', 'goog.string']);
goog.addDependency('labs/useragent/platform_test.js', ['goog.labs.userAgent.platformTest'], ['goog.labs.userAgent.platform', 'goog.labs.userAgent.testAgents', 'goog.labs.userAgent.util', 'goog.testing.jsunit']);
goog.addDependency('labs/useragent/test_agents.js', ['goog.labs.userAgent.testAgents'], []);
goog.addDependency('labs/useragent/util.js', ['goog.labs.userAgent.util'], ['goog.string']);
goog.addDependency('labs/useragent/util_test.js', ['goog.labs.userAgent.utilTest'], ['goog.functions', 'goog.labs.userAgent.testAgents', 'goog.labs.userAgent.util', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit']);
goog.addDependency('locale/countries.js', ['goog.locale.countries'], []);
goog.addDependency('locale/countrylanguagenames_test.js', ['goog.locale.countryLanguageNamesTest'], ['goog.locale', 'goog.testing.jsunit']);
goog.addDependency('locale/defaultlocalenameconstants.js', ['goog.locale.defaultLocaleNameConstants'], []);
goog.addDependency('locale/genericfontnames.js', ['goog.locale.genericFontNames'], []);
goog.addDependency('locale/genericfontnames_test.js', ['goog.locale.genericFontNamesTest'], ['goog.locale.genericFontNames', 'goog.testing.jsunit']);
goog.addDependency('locale/genericfontnamesdata.js', ['goog.locale.genericFontNamesData'], []);
goog.addDependency('locale/locale.js', ['goog.locale'], ['goog.locale.nativeNameConstants']);
goog.addDependency('locale/nativenameconstants.js', ['goog.locale.nativeNameConstants'], []);
goog.addDependency('locale/scriptToLanguages.js', ['goog.locale.scriptToLanguages'], ['goog.locale']);
goog.addDependency('locale/timezonedetection.js', ['goog.locale.timeZoneDetection'], ['goog.locale', 'goog.locale.TimeZoneFingerprint']);
goog.addDependency('locale/timezonedetection_test.js', ['goog.locale.timeZoneDetectionTest'], ['goog.locale.timeZoneDetection', 'goog.testing.jsunit']);
goog.addDependency('locale/timezonefingerprint.js', ['goog.locale.TimeZoneFingerprint'], []);
goog.addDependency('locale/timezonelist.js', ['goog.locale.TimeZoneList'], ['goog.locale']);
goog.addDependency('locale/timezonelist_test.js', ['goog.locale.TimeZoneListTest'], ['goog.locale', 'goog.locale.TimeZoneList', 'goog.testing.jsunit']);
goog.addDependency('log/log.js', ['goog.log', 'goog.log.Level', 'goog.log.LogRecord', 'goog.log.Logger'], ['goog.debug', 'goog.debug.LogManager', 'goog.debug.LogRecord', 'goog.debug.Logger']);
goog.addDependency('log/log_test.js', ['goog.logTest'], ['goog.debug.LogManager', 'goog.log', 'goog.log.Level', 'goog.testing.jsunit']);
goog.addDependency('math/affinetransform.js', ['goog.math.AffineTransform'], ['goog.math']);
goog.addDependency('math/affinetransform_test.js', ['goog.math.AffineTransformTest'], ['goog.array', 'goog.math', 'goog.math.AffineTransform', 'goog.testing.jsunit']);
goog.addDependency('math/bezier.js', ['goog.math.Bezier'], ['goog.math', 'goog.math.Coordinate']);
goog.addDependency('math/bezier_test.js', ['goog.math.BezierTest'], ['goog.math', 'goog.math.Bezier', 'goog.math.Coordinate', 'goog.testing.jsunit']);
goog.addDependency('math/box.js', ['goog.math.Box'], ['goog.math.Coordinate']);
goog.addDependency('math/box_test.js', ['goog.math.BoxTest'], ['goog.math.Box', 'goog.math.Coordinate', 'goog.testing.jsunit']);
goog.addDependency('math/coordinate.js', ['goog.math.Coordinate'], ['goog.math']);
goog.addDependency('math/coordinate3.js', ['goog.math.Coordinate3'], []);
goog.addDependency('math/coordinate3_test.js', ['goog.math.Coordinate3Test'], ['goog.math.Coordinate3', 'goog.testing.jsunit']);
goog.addDependency('math/coordinate_test.js', ['goog.math.CoordinateTest'], ['goog.math.Coordinate', 'goog.testing.jsunit']);
goog.addDependency('math/exponentialbackoff.js', ['goog.math.ExponentialBackoff'], ['goog.asserts']);
goog.addDependency('math/exponentialbackoff_test.js', ['goog.math.ExponentialBackoffTest'], ['goog.math.ExponentialBackoff', 'goog.testing.jsunit']);
goog.addDependency('math/integer.js', ['goog.math.Integer'], []);
goog.addDependency('math/integer_test.js', ['goog.math.IntegerTest'], ['goog.math.Integer', 'goog.testing.jsunit']);
goog.addDependency('math/interpolator/interpolator1.js', ['goog.math.interpolator.Interpolator1'], []);
goog.addDependency('math/interpolator/linear1.js', ['goog.math.interpolator.Linear1'], ['goog.array', 'goog.math', 'goog.math.interpolator.Interpolator1']);
goog.addDependency('math/interpolator/linear1_test.js', ['goog.math.interpolator.Linear1Test'], ['goog.math.interpolator.Linear1', 'goog.testing.jsunit']);
goog.addDependency('math/interpolator/pchip1.js', ['goog.math.interpolator.Pchip1'], ['goog.math', 'goog.math.interpolator.Spline1']);
goog.addDependency('math/interpolator/pchip1_test.js', ['goog.math.interpolator.Pchip1Test'], ['goog.math.interpolator.Pchip1', 'goog.testing.jsunit']);
goog.addDependency('math/interpolator/spline1.js', ['goog.math.interpolator.Spline1'], ['goog.array', 'goog.math', 'goog.math.interpolator.Interpolator1', 'goog.math.tdma']);
goog.addDependency('math/interpolator/spline1_test.js', ['goog.math.interpolator.Spline1Test'], ['goog.math.interpolator.Spline1', 'goog.testing.jsunit']);
goog.addDependency('math/line.js', ['goog.math.Line'], ['goog.math', 'goog.math.Coordinate']);
goog.addDependency('math/line_test.js', ['goog.math.LineTest'], ['goog.math.Coordinate', 'goog.math.Line', 'goog.testing.jsunit']);
goog.addDependency('math/long.js', ['goog.math.Long'], []);
goog.addDependency('math/long_test.js', ['goog.math.LongTest'], ['goog.math.Long', 'goog.testing.jsunit']);
goog.addDependency('math/math.js', ['goog.math'], ['goog.array', 'goog.asserts']);
goog.addDependency('math/math_test.js', ['goog.mathTest'], ['goog.math', 'goog.testing.jsunit']);
goog.addDependency('math/matrix.js', ['goog.math.Matrix'], ['goog.array', 'goog.math', 'goog.math.Size', 'goog.string']);
goog.addDependency('math/matrix_test.js', ['goog.math.MatrixTest'], ['goog.math.Matrix', 'goog.testing.jsunit']);
goog.addDependency('math/path.js', ['goog.math.Path', 'goog.math.Path.Segment'], ['goog.array', 'goog.math']);
goog.addDependency('math/path_test.js', ['goog.math.PathTest'], ['goog.array', 'goog.math.AffineTransform', 'goog.math.Path', 'goog.testing.jsunit']);
goog.addDependency('math/paths.js', ['goog.math.paths'], ['goog.math.Coordinate', 'goog.math.Path']);
goog.addDependency('math/paths_test.js', ['goog.math.pathsTest'], ['goog.math.Coordinate', 'goog.math.paths', 'goog.testing.jsunit']);
goog.addDependency('math/range.js', ['goog.math.Range'], ['goog.asserts']);
goog.addDependency('math/range_test.js', ['goog.math.RangeTest'], ['goog.math.Range', 'goog.testing.jsunit']);
goog.addDependency('math/rangeset.js', ['goog.math.RangeSet'], ['goog.array', 'goog.iter.Iterator', 'goog.iter.StopIteration', 'goog.math.Range']);
goog.addDependency('math/rangeset_test.js', ['goog.math.RangeSetTest'], ['goog.iter', 'goog.math.Range', 'goog.math.RangeSet', 'goog.testing.jsunit']);
goog.addDependency('math/rect.js', ['goog.math.Rect'], ['goog.math.Box', 'goog.math.Coordinate', 'goog.math.Size']);
goog.addDependency('math/rect_test.js', ['goog.math.RectTest'], ['goog.math.Box', 'goog.math.Coordinate', 'goog.math.Rect', 'goog.math.Size', 'goog.testing.jsunit']);
goog.addDependency('math/size.js', ['goog.math.Size'], []);
goog.addDependency('math/size_test.js', ['goog.math.SizeTest'], ['goog.math.Size', 'goog.testing.jsunit']);
goog.addDependency('math/tdma.js', ['goog.math.tdma'], []);
goog.addDependency('math/tdma_test.js', ['goog.math.tdmaTest'], ['goog.math.tdma', 'goog.testing.jsunit']);
goog.addDependency('math/vec2.js', ['goog.math.Vec2'], ['goog.math', 'goog.math.Coordinate']);
goog.addDependency('math/vec2_test.js', ['goog.math.Vec2Test'], ['goog.math.Vec2', 'goog.testing.jsunit']);
goog.addDependency('math/vec3.js', ['goog.math.Vec3'], ['goog.math', 'goog.math.Coordinate3']);
goog.addDependency('math/vec3_test.js', ['goog.math.Vec3Test'], ['goog.math.Coordinate3', 'goog.math.Vec3', 'goog.testing.jsunit']);
goog.addDependency('memoize/memoize.js', ['goog.memoize'], []);
goog.addDependency('memoize/memoize_test.js', ['goog.memoizeTest'], ['goog.memoize', 'goog.testing.jsunit']);
goog.addDependency('messaging/abstractchannel.js', ['goog.messaging.AbstractChannel'], ['goog.Disposable', 'goog.debug', 'goog.json', 'goog.log', 'goog.messaging.MessageChannel']);
goog.addDependency('messaging/abstractchannel_test.js', ['goog.messaging.AbstractChannelTest'], ['goog.messaging.AbstractChannel', 'goog.testing.MockControl', 'goog.testing.async.MockControl', 'goog.testing.jsunit']);
goog.addDependency('messaging/bufferedchannel.js', ['goog.messaging.BufferedChannel'], ['goog.Timer', 'goog.Uri', 'goog.debug.Error', 'goog.events', 'goog.log', 'goog.messaging.MessageChannel', 'goog.messaging.MultiChannel']);
goog.addDependency('messaging/bufferedchannel_test.js', ['goog.messaging.BufferedChannelTest'], ['goog.debug.Console', 'goog.dom', 'goog.log', 'goog.log.Level', 'goog.messaging.BufferedChannel', 'goog.testing.MockClock', 'goog.testing.MockControl', 'goog.testing.async.MockControl', 'goog.testing.jsunit', 'goog.testing.messaging.MockMessageChannel']);
goog.addDependency('messaging/deferredchannel.js', ['goog.messaging.DeferredChannel'], ['goog.Disposable', 'goog.async.Deferred', 'goog.messaging.MessageChannel']);
goog.addDependency('messaging/deferredchannel_test.js', ['goog.messaging.DeferredChannelTest'], ['goog.async.Deferred', 'goog.messaging.DeferredChannel', 'goog.testing.MockControl', 'goog.testing.async.MockControl', 'goog.testing.jsunit', 'goog.testing.messaging.MockMessageChannel']);
goog.addDependency('messaging/loggerclient.js', ['goog.messaging.LoggerClient'], ['goog.Disposable', 'goog.debug', 'goog.debug.LogManager', 'goog.debug.Logger']);
goog.addDependency('messaging/loggerclient_test.js', ['goog.messaging.LoggerClientTest'], ['goog.debug', 'goog.debug.Logger', 'goog.messaging.LoggerClient', 'goog.testing.MockControl', 'goog.testing.jsunit', 'goog.testing.messaging.MockMessageChannel']);
goog.addDependency('messaging/loggerserver.js', ['goog.messaging.LoggerServer'], ['goog.Disposable', 'goog.log']);
goog.addDependency('messaging/loggerserver_test.js', ['goog.messaging.LoggerServerTest'], ['goog.debug.LogManager', 'goog.debug.Logger', 'goog.log', 'goog.log.Level', 'goog.messaging.LoggerServer', 'goog.testing.MockControl', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.testing.messaging.MockMessageChannel']);
goog.addDependency('messaging/messagechannel.js', ['goog.messaging.MessageChannel'], []);
goog.addDependency('messaging/messaging.js', ['goog.messaging'], ['goog.messaging.MessageChannel']);
goog.addDependency('messaging/messaging_test.js', ['goog.testing.messaging.MockMessageChannelTest'], ['goog.messaging', 'goog.testing.MockControl', 'goog.testing.jsunit', 'goog.testing.messaging.MockMessageChannel']);
goog.addDependency('messaging/multichannel.js', ['goog.messaging.MultiChannel', 'goog.messaging.MultiChannel.VirtualChannel'], ['goog.Disposable', 'goog.events.EventHandler', 'goog.log', 'goog.messaging.MessageChannel', 'goog.object']);
goog.addDependency('messaging/multichannel_test.js', ['goog.messaging.MultiChannelTest'], ['goog.messaging.MultiChannel', 'goog.testing.MockControl', 'goog.testing.jsunit', 'goog.testing.messaging.MockMessageChannel', 'goog.testing.mockmatchers.IgnoreArgument']);
goog.addDependency('messaging/portcaller.js', ['goog.messaging.PortCaller'], ['goog.Disposable', 'goog.async.Deferred', 'goog.messaging.DeferredChannel', 'goog.messaging.PortChannel', 'goog.messaging.PortNetwork', 'goog.object']);
goog.addDependency('messaging/portcaller_test.js', ['goog.messaging.PortCallerTest'], ['goog.events.EventTarget', 'goog.messaging.PortCaller', 'goog.messaging.PortNetwork', 'goog.testing.MockControl', 'goog.testing.jsunit', 'goog.testing.messaging.MockMessageChannel']);
goog.addDependency('messaging/portchannel.js', ['goog.messaging.PortChannel'], ['goog.Timer', 'goog.array', 'goog.async.Deferred', 'goog.debug', 'goog.dom', 'goog.dom.DomHelper', 'goog.events', 'goog.events.EventType', 'goog.json', 'goog.log', 'goog.messaging.AbstractChannel', 'goog.messaging.DeferredChannel', 'goog.object', 'goog.string']);
goog.addDependency('messaging/portnetwork.js', ['goog.messaging.PortNetwork'], []);
goog.addDependency('messaging/portoperator.js', ['goog.messaging.PortOperator'], ['goog.Disposable', 'goog.asserts', 'goog.log', 'goog.messaging.PortChannel', 'goog.messaging.PortNetwork', 'goog.object']);
goog.addDependency('messaging/portoperator_test.js', ['goog.messaging.PortOperatorTest'], ['goog.messaging.PortNetwork', 'goog.messaging.PortOperator', 'goog.testing.MockControl', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.testing.messaging.MockMessageChannel', 'goog.testing.messaging.MockMessagePort']);
goog.addDependency('messaging/respondingchannel.js', ['goog.messaging.RespondingChannel'], ['goog.Disposable', 'goog.log', 'goog.messaging.MessageChannel', 'goog.messaging.MultiChannel', 'goog.messaging.MultiChannel.VirtualChannel']);
goog.addDependency('messaging/respondingchannel_test.js', ['goog.messaging.RespondingChannelTest'], ['goog.messaging.RespondingChannel', 'goog.testing.MockControl', 'goog.testing.jsunit', 'goog.testing.messaging.MockMessageChannel']);
goog.addDependency('messaging/testdata/portchannel_worker.js', ['goog.messaging.testdata.portchannel_worker'], ['goog.messaging.PortChannel']);
goog.addDependency('messaging/testdata/portnetwork_worker1.js', ['goog.messaging.testdata.portnetwork_worker1'], ['goog.messaging.PortCaller', 'goog.messaging.PortChannel']);
goog.addDependency('messaging/testdata/portnetwork_worker2.js', ['goog.messaging.testdata.portnetwork_worker2'], ['goog.messaging.PortCaller', 'goog.messaging.PortChannel']);
goog.addDependency('module/abstractmoduleloader.js', ['goog.module.AbstractModuleLoader'], []);
goog.addDependency('module/basemodule.js', ['goog.module.BaseModule'], ['goog.Disposable']);
goog.addDependency('module/loader.js', ['goog.module.Loader'], ['goog.Timer', 'goog.array', 'goog.dom', 'goog.object']);
goog.addDependency('module/module.js', ['goog.module'], ['goog.array', 'goog.module.Loader']);
goog.addDependency('module/moduleinfo.js', ['goog.module.ModuleInfo'], ['goog.Disposable', 'goog.functions', 'goog.module.BaseModule', 'goog.module.ModuleLoadCallback']);
goog.addDependency('module/moduleinfo_test.js', ['goog.module.ModuleInfoTest'], ['goog.module.BaseModule', 'goog.module.ModuleInfo', 'goog.testing.jsunit']);
goog.addDependency('module/moduleloadcallback.js', ['goog.module.ModuleLoadCallback'], ['goog.debug.entryPointRegistry', 'goog.debug.errorHandlerWeakDep']);
goog.addDependency('module/moduleloadcallback_test.js', ['goog.module.ModuleLoadCallbackTest'], ['goog.debug.ErrorHandler', 'goog.debug.entryPointRegistry', 'goog.functions', 'goog.module.ModuleLoadCallback', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('module/moduleloader.js', ['goog.module.ModuleLoader'], ['goog.Timer', 'goog.array', 'goog.events', 'goog.events.Event', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.log', 'goog.module.AbstractModuleLoader', 'goog.net.BulkLoader', 'goog.net.EventType', 'goog.net.jsloader', 'goog.userAgent.product']);
goog.addDependency('module/moduleloader_test.js', ['goog.module.ModuleLoaderTest'], ['goog.array', 'goog.dom', 'goog.functions', 'goog.module.ModuleLoader', 'goog.module.ModuleManager', 'goog.module.ModuleManager.CallbackType', 'goog.object', 'goog.testing.AsyncTestCase', 'goog.testing.PropertyReplacer', 'goog.testing.events.EventObserver', 'goog.testing.jsunit', 'goog.testing.recordFunction', 'goog.userAgent.product']);
goog.addDependency('module/modulemanager.js', ['goog.module.ModuleManager', 'goog.module.ModuleManager.CallbackType', 'goog.module.ModuleManager.FailureType'], ['goog.Disposable', 'goog.array', 'goog.asserts', 'goog.async.Deferred', 'goog.debug.Trace', 'goog.dispose', 'goog.log', 'goog.module.ModuleInfo', 'goog.module.ModuleLoadCallback', 'goog.object']);
goog.addDependency('module/modulemanager_test.js', ['goog.module.ModuleManagerTest'], ['goog.array', 'goog.functions', 'goog.module.BaseModule', 'goog.module.ModuleManager', 'goog.testing', 'goog.testing.MockClock', 'goog.testing.jsunit', 'goog.testing.recordFunction', 'goog.userAgent']);
goog.addDependency('module/testdata/modA_1.js', ['goog.module.testdata.modA_1'], []);
goog.addDependency('module/testdata/modA_2.js', ['goog.module.testdata.modA_2'], ['goog.module.ModuleManager']);
goog.addDependency('module/testdata/modB_1.js', ['goog.module.testdata.modB_1'], ['goog.module.ModuleManager']);
goog.addDependency('net/browserchannel.js', ['goog.net.BrowserChannel', 'goog.net.BrowserChannel.Error', 'goog.net.BrowserChannel.Event', 'goog.net.BrowserChannel.Handler', 'goog.net.BrowserChannel.LogSaver', 'goog.net.BrowserChannel.QueuedMap', 'goog.net.BrowserChannel.ServerReachability', 'goog.net.BrowserChannel.ServerReachabilityEvent', 'goog.net.BrowserChannel.Stat', 'goog.net.BrowserChannel.StatEvent', 'goog.net.BrowserChannel.State', 'goog.net.BrowserChannel.TimingEvent'], ['goog.Uri', 'goog.array', 'goog.asserts', 'goog.debug.TextFormatter', 'goog.events.Event', 'goog.events.EventTarget', 'goog.json', 'goog.json.EvalJsonProcessor', 'goog.log', 'goog.net.BrowserTestChannel', 'goog.net.ChannelDebug', 'goog.net.ChannelRequest', 'goog.net.XhrIo', 'goog.net.tmpnetwork', 'goog.object', 'goog.string', 'goog.structs', 'goog.structs.CircularBuffer']);
goog.addDependency('net/browserchannel_test.js', ['goog.net.BrowserChannelTest'], ['goog.Timer', 'goog.array', 'goog.dom', 'goog.functions', 'goog.json', 'goog.net.BrowserChannel', 'goog.net.ChannelDebug', 'goog.net.ChannelRequest', 'goog.net.tmpnetwork', 'goog.structs.Map', 'goog.testing.MockClock', 'goog.testing.PropertyReplacer', 'goog.testing.asserts', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('net/browsertestchannel.js', ['goog.net.BrowserTestChannel'], ['goog.json.EvalJsonProcessor', 'goog.net.ChannelRequest', 'goog.net.ChannelRequest.Error', 'goog.net.tmpnetwork', 'goog.string.Parser', 'goog.userAgent']);
goog.addDependency('net/bulkloader.js', ['goog.net.BulkLoader'], ['goog.events.EventHandler', 'goog.events.EventTarget', 'goog.log', 'goog.net.BulkLoaderHelper', 'goog.net.EventType', 'goog.net.XhrIo']);
goog.addDependency('net/bulkloader_test.js', ['goog.net.BulkLoaderTest'], ['goog.events.Event', 'goog.events.EventHandler', 'goog.net.BulkLoader', 'goog.net.EventType', 'goog.testing.MockClock', 'goog.testing.jsunit']);
goog.addDependency('net/bulkloaderhelper.js', ['goog.net.BulkLoaderHelper'], ['goog.Disposable', 'goog.log']);
goog.addDependency('net/channeldebug.js', ['goog.net.ChannelDebug'], ['goog.json', 'goog.log']);
goog.addDependency('net/channelrequest.js', ['goog.net.ChannelRequest', 'goog.net.ChannelRequest.Error'], ['goog.Timer', 'goog.async.Throttle', 'goog.events.EventHandler', 'goog.net.ErrorCode', 'goog.net.EventType', 'goog.net.XmlHttp', 'goog.object', 'goog.userAgent']);
goog.addDependency('net/channelrequest_test.js', ['goog.net.ChannelRequestTest'], ['goog.Uri', 'goog.functions', 'goog.net.BrowserChannel', 'goog.net.ChannelDebug', 'goog.net.ChannelRequest', 'goog.testing.MockClock', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.testing.net.XhrIo', 'goog.testing.recordFunction']);
goog.addDependency('net/cookies.js', ['goog.net.Cookies', 'goog.net.cookies'], []);
goog.addDependency('net/cookies_test.js', ['goog.net.cookiesTest'], ['goog.array', 'goog.net.cookies', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit']);
goog.addDependency('net/corsxmlhttpfactory.js', ['goog.net.CorsXmlHttpFactory', 'goog.net.IeCorsXhrAdapter'], ['goog.net.HttpStatus', 'goog.net.XhrLike', 'goog.net.XmlHttp', 'goog.net.XmlHttpFactory']);
goog.addDependency('net/corsxmlhttpfactory_test.js', ['goog.net.CorsXmlHttpFactoryTest'], ['goog.net.CorsXmlHttpFactory', 'goog.net.IeCorsXhrAdapter', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('net/crossdomainrpc.js', ['goog.net.CrossDomainRpc'], ['goog.Uri', 'goog.dom', 'goog.events', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.json', 'goog.log', 'goog.net.EventType', 'goog.net.HttpStatus', 'goog.string', 'goog.userAgent']);
goog.addDependency('net/crossdomainrpc_test.js', ['goog.net.CrossDomainRpcTest'], ['goog.log', 'goog.log.Level', 'goog.net.CrossDomainRpc', 'goog.testing.jsunit']);
goog.addDependency('net/errorcode.js', ['goog.net.ErrorCode'], []);
goog.addDependency('net/eventtype.js', ['goog.net.EventType'], []);
goog.addDependency('net/filedownloader.js', ['goog.net.FileDownloader', 'goog.net.FileDownloader.Error'], ['goog.Disposable', 'goog.asserts', 'goog.async.Deferred', 'goog.crypt.hash32', 'goog.debug.Error', 'goog.events', 'goog.events.EventHandler', 'goog.fs', 'goog.fs.DirectoryEntry', 'goog.fs.Error', 'goog.fs.FileSaver', 'goog.net.EventType', 'goog.net.XhrIo', 'goog.net.XhrIoPool', 'goog.object']);
goog.addDependency('net/filedownloader_test.js', ['goog.net.FileDownloaderTest'], ['goog.fs.Error', 'goog.net.ErrorCode', 'goog.net.FileDownloader', 'goog.net.XhrIo', 'goog.testing.AsyncTestCase', 'goog.testing.PropertyReplacer', 'goog.testing.fs', 'goog.testing.fs.FileSystem', 'goog.testing.jsunit', 'goog.testing.net.XhrIoPool']);
goog.addDependency('net/httpstatus.js', ['goog.net.HttpStatus'], []);
goog.addDependency('net/iframe_xhr_test.js', ['goog.net.iframeXhrTest'], ['goog.Timer', 'goog.debug.Console', 'goog.debug.LogManager', 'goog.debug.Logger', 'goog.events', 'goog.net.IframeIo', 'goog.net.XhrIo', 'goog.testing.AsyncTestCase', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('net/iframeio.js', ['goog.net.IframeIo', 'goog.net.IframeIo.IncrementalDataEvent'], ['goog.Timer', 'goog.Uri', 'goog.asserts', 'goog.debug', 'goog.dom', 'goog.events', 'goog.events.Event', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.json', 'goog.log', 'goog.log.Level', 'goog.net.ErrorCode', 'goog.net.EventType', 'goog.reflect', 'goog.string', 'goog.structs', 'goog.userAgent']);
goog.addDependency('net/iframeio_different_base_test.js', ['goog.net.iframeIoDifferentBaseTest'], ['goog.events', 'goog.net.EventType', 'goog.net.IframeIo', 'goog.testing.AsyncTestCase', 'goog.testing.jsunit']);
goog.addDependency('net/iframeio_test.js', ['goog.net.IframeIoTest'], ['goog.debug', 'goog.debug.DivConsole', 'goog.debug.LogManager', 'goog.dom', 'goog.events', 'goog.events.EventType', 'goog.log', 'goog.log.Level', 'goog.net.IframeIo', 'goog.testing.events', 'goog.testing.events.Event', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('net/iframeloadmonitor.js', ['goog.net.IframeLoadMonitor'], ['goog.dom', 'goog.events', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.userAgent']);
goog.addDependency('net/iframeloadmonitor_test.js', ['goog.net.IframeLoadMonitorTest'], ['goog.dom', 'goog.events', 'goog.net.IframeLoadMonitor', 'goog.testing.AsyncTestCase', 'goog.testing.jsunit']);
goog.addDependency('net/imageloader.js', ['goog.net.ImageLoader'], ['goog.array', 'goog.dom', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.net.EventType', 'goog.object', 'goog.userAgent']);
goog.addDependency('net/imageloader_test.js', ['goog.net.ImageLoaderTest'], ['goog.Timer', 'goog.array', 'goog.dispose', 'goog.events', 'goog.events.Event', 'goog.events.EventType', 'goog.net.EventType', 'goog.net.ImageLoader', 'goog.object', 'goog.string', 'goog.testing.AsyncTestCase', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('net/ipaddress.js', ['goog.net.IpAddress', 'goog.net.Ipv4Address', 'goog.net.Ipv6Address'], ['goog.array', 'goog.math.Integer', 'goog.object', 'goog.string']);
goog.addDependency('net/ipaddress_test.js', ['goog.net.IpAddressTest'], ['goog.math.Integer', 'goog.net.IpAddress', 'goog.net.Ipv4Address', 'goog.net.Ipv6Address', 'goog.testing.jsunit']);
goog.addDependency('net/jsloader.js', ['goog.net.jsloader', 'goog.net.jsloader.Error', 'goog.net.jsloader.ErrorCode', 'goog.net.jsloader.Options'], ['goog.array', 'goog.async.Deferred', 'goog.debug.Error', 'goog.dom', 'goog.dom.TagName']);
goog.addDependency('net/jsloader_test.js', ['goog.net.jsloaderTest'], ['goog.array', 'goog.dom', 'goog.net.jsloader', 'goog.net.jsloader.ErrorCode', 'goog.testing.AsyncTestCase', 'goog.testing.jsunit']);
goog.addDependency('net/jsonp.js', ['goog.net.Jsonp'], ['goog.Uri', 'goog.net.jsloader']);
goog.addDependency('net/jsonp_test.js', ['goog.net.JsonpTest'], ['goog.net.Jsonp', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.testing.recordFunction', 'goog.userAgent']);
goog.addDependency('net/mockiframeio.js', ['goog.net.MockIFrameIo'], ['goog.events.EventTarget', 'goog.json', 'goog.net.ErrorCode', 'goog.net.EventType', 'goog.net.IframeIo']);
goog.addDependency('net/multiiframeloadmonitor.js', ['goog.net.MultiIframeLoadMonitor'], ['goog.events', 'goog.net.IframeLoadMonitor']);
goog.addDependency('net/multiiframeloadmonitor_test.js', ['goog.net.MultiIframeLoadMonitorTest'], ['goog.dom', 'goog.net.IframeLoadMonitor', 'goog.net.MultiIframeLoadMonitor', 'goog.testing.AsyncTestCase', 'goog.testing.jsunit']);
goog.addDependency('net/networkstatusmonitor.js', ['goog.net.NetworkStatusMonitor'], ['goog.events.Listenable']);
goog.addDependency('net/networktester.js', ['goog.net.NetworkTester'], ['goog.Timer', 'goog.Uri', 'goog.log']);
goog.addDependency('net/networktester_test.js', ['goog.net.NetworkTesterTest'], ['goog.Uri', 'goog.net.NetworkTester', 'goog.testing.MockClock', 'goog.testing.jsunit']);
goog.addDependency('net/testdata/jsloader_test1.js', ['goog.net.testdata.jsloader_test1'], []);
goog.addDependency('net/testdata/jsloader_test2.js', ['goog.net.testdata.jsloader_test2'], []);
goog.addDependency('net/testdata/jsloader_test3.js', ['goog.net.testdata.jsloader_test3'], []);
goog.addDependency('net/testdata/jsloader_test4.js', ['goog.net.testdata.jsloader_test4'], []);
goog.addDependency('net/tmpnetwork.js', ['goog.net.tmpnetwork'], ['goog.Uri', 'goog.net.ChannelDebug']);
goog.addDependency('net/websocket.js', ['goog.net.WebSocket', 'goog.net.WebSocket.ErrorEvent', 'goog.net.WebSocket.EventType', 'goog.net.WebSocket.MessageEvent'], ['goog.Timer', 'goog.asserts', 'goog.debug.entryPointRegistry', 'goog.events', 'goog.events.Event', 'goog.events.EventTarget', 'goog.log']);
goog.addDependency('net/websocket_test.js', ['goog.net.WebSocketTest'], ['goog.debug.EntryPointMonitor', 'goog.debug.ErrorHandler', 'goog.debug.entryPointRegistry', 'goog.events', 'goog.functions', 'goog.net.WebSocket', 'goog.testing.MockClock', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('net/wrapperxmlhttpfactory.js', ['goog.net.WrapperXmlHttpFactory'], ['goog.net.XhrLike', 'goog.net.XmlHttpFactory']);
goog.addDependency('net/xhrio.js', ['goog.net.XhrIo', 'goog.net.XhrIo.ResponseType'], ['goog.Timer', 'goog.array', 'goog.debug.entryPointRegistry', 'goog.events.EventTarget', 'goog.json', 'goog.log', 'goog.net.ErrorCode', 'goog.net.EventType', 'goog.net.HttpStatus', 'goog.net.XmlHttp', 'goog.object', 'goog.string', 'goog.structs', 'goog.structs.Map', 'goog.uri.utils', 'goog.userAgent']);
goog.addDependency('net/xhrio_test.js', ['goog.net.XhrIoTest'], ['goog.Uri', 'goog.debug.EntryPointMonitor', 'goog.debug.ErrorHandler', 'goog.debug.entryPointRegistry', 'goog.events', 'goog.functions', 'goog.net.EventType', 'goog.net.WrapperXmlHttpFactory', 'goog.net.XhrIo', 'goog.net.XmlHttp', 'goog.object', 'goog.string', 'goog.testing.MockClock', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.testing.net.XhrIo', 'goog.testing.recordFunction']);
goog.addDependency('net/xhriopool.js', ['goog.net.XhrIoPool'], ['goog.net.XhrIo', 'goog.structs.PriorityPool']);
goog.addDependency('net/xhrlike.js', ['goog.net.XhrLike'], []);
goog.addDependency('net/xhrmanager.js', ['goog.net.XhrManager', 'goog.net.XhrManager.Event', 'goog.net.XhrManager.Request'], ['goog.events', 'goog.events.Event', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.net.ErrorCode', 'goog.net.EventType', 'goog.net.XhrIo', 'goog.net.XhrIoPool', 'goog.structs.Map']);
goog.addDependency('net/xhrmanager_test.js', ['goog.net.XhrManagerTest'], ['goog.events', 'goog.net.EventType', 'goog.net.XhrIo', 'goog.net.XhrManager', 'goog.testing.jsunit', 'goog.testing.net.XhrIoPool', 'goog.testing.recordFunction']);
goog.addDependency('net/xmlhttp.js', ['goog.net.DefaultXmlHttpFactory', 'goog.net.XmlHttp', 'goog.net.XmlHttp.OptionType', 'goog.net.XmlHttp.ReadyState', 'goog.net.XmlHttpDefines'], ['goog.asserts', 'goog.net.WrapperXmlHttpFactory', 'goog.net.XmlHttpFactory']);
goog.addDependency('net/xmlhttpfactory.js', ['goog.net.XmlHttpFactory'], ['goog.net.XhrLike']);
goog.addDependency('net/xpc/crosspagechannel.js', ['goog.net.xpc.CrossPageChannel'], ['goog.Uri', 'goog.async.Deferred', 'goog.async.Delay', 'goog.dispose', 'goog.dom', 'goog.events', 'goog.events.EventHandler', 'goog.events.EventType', 'goog.json', 'goog.log', 'goog.messaging.AbstractChannel', 'goog.net.xpc', 'goog.net.xpc.CfgFields', 'goog.net.xpc.ChannelStates', 'goog.net.xpc.CrossPageChannelRole', 'goog.net.xpc.DirectTransport', 'goog.net.xpc.FrameElementMethodTransport', 'goog.net.xpc.IframePollingTransport', 'goog.net.xpc.IframeRelayTransport', 'goog.net.xpc.NativeMessagingTransport', 'goog.net.xpc.NixTransport', 'goog.net.xpc.TransportTypes', 'goog.net.xpc.UriCfgFields', 'goog.string', 'goog.uri.utils', 'goog.userAgent']);
goog.addDependency('net/xpc/crosspagechannel_test.js', ['goog.net.xpc.CrossPageChannelTest'], ['goog.Disposable', 'goog.Uri', 'goog.async.Deferred', 'goog.dom', 'goog.log', 'goog.log.Level', 'goog.net.xpc', 'goog.net.xpc.CfgFields', 'goog.net.xpc.CrossPageChannel', 'goog.net.xpc.CrossPageChannelRole', 'goog.net.xpc.TransportTypes', 'goog.object', 'goog.testing.AsyncTestCase', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit']);
goog.addDependency('net/xpc/crosspagechannelrole.js', ['goog.net.xpc.CrossPageChannelRole'], []);
goog.addDependency('net/xpc/directtransport.js', ['goog.net.xpc.DirectTransport'], ['goog.Timer', 'goog.async.Deferred', 'goog.events.EventHandler', 'goog.log', 'goog.net.xpc', 'goog.net.xpc.CfgFields', 'goog.net.xpc.CrossPageChannelRole', 'goog.net.xpc.Transport', 'goog.net.xpc.TransportTypes', 'goog.object']);
goog.addDependency('net/xpc/directtransport_test.js', ['goog.net.xpc.DirectTransportTest'], ['goog.dom', 'goog.log', 'goog.log.Level', 'goog.net.xpc', 'goog.net.xpc.CfgFields', 'goog.net.xpc.CrossPageChannel', 'goog.net.xpc.CrossPageChannelRole', 'goog.net.xpc.TransportTypes', 'goog.testing.AsyncTestCase', 'goog.testing.jsunit']);
goog.addDependency('net/xpc/frameelementmethodtransport.js', ['goog.net.xpc.FrameElementMethodTransport'], ['goog.log', 'goog.net.xpc', 'goog.net.xpc.CrossPageChannelRole', 'goog.net.xpc.Transport', 'goog.net.xpc.TransportTypes']);
goog.addDependency('net/xpc/iframepollingtransport.js', ['goog.net.xpc.IframePollingTransport', 'goog.net.xpc.IframePollingTransport.Receiver', 'goog.net.xpc.IframePollingTransport.Sender'], ['goog.array', 'goog.dom', 'goog.log', 'goog.log.Level', 'goog.net.xpc', 'goog.net.xpc.CfgFields', 'goog.net.xpc.CrossPageChannelRole', 'goog.net.xpc.Transport', 'goog.net.xpc.TransportTypes', 'goog.userAgent']);
goog.addDependency('net/xpc/iframepollingtransport_test.js', ['goog.net.xpc.IframePollingTransportTest'], ['goog.Timer', 'goog.dom', 'goog.dom.TagName', 'goog.functions', 'goog.net.xpc.CfgFields', 'goog.net.xpc.CrossPageChannel', 'goog.net.xpc.CrossPageChannelRole', 'goog.net.xpc.TransportTypes', 'goog.object', 'goog.testing.MockClock', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('net/xpc/iframerelaytransport.js', ['goog.net.xpc.IframeRelayTransport'], ['goog.dom', 'goog.dom.safe', 'goog.events', 'goog.html.SafeHtml', 'goog.log', 'goog.log.Level', 'goog.net.xpc', 'goog.net.xpc.CfgFields', 'goog.net.xpc.Transport', 'goog.net.xpc.TransportTypes', 'goog.string', 'goog.string.Const', 'goog.userAgent']);
goog.addDependency('net/xpc/nativemessagingtransport.js', ['goog.net.xpc.NativeMessagingTransport'], ['goog.Timer', 'goog.asserts', 'goog.async.Deferred', 'goog.events', 'goog.events.EventHandler', 'goog.log', 'goog.net.xpc', 'goog.net.xpc.CrossPageChannelRole', 'goog.net.xpc.Transport', 'goog.net.xpc.TransportTypes']);
goog.addDependency('net/xpc/nativemessagingtransport_test.js', ['goog.net.xpc.NativeMessagingTransportTest'], ['goog.dom', 'goog.events', 'goog.net.xpc', 'goog.net.xpc.CfgFields', 'goog.net.xpc.CrossPageChannel', 'goog.net.xpc.CrossPageChannelRole', 'goog.net.xpc.NativeMessagingTransport', 'goog.testing.jsunit']);
goog.addDependency('net/xpc/nixtransport.js', ['goog.net.xpc.NixTransport'], ['goog.log', 'goog.net.xpc', 'goog.net.xpc.CfgFields', 'goog.net.xpc.CrossPageChannelRole', 'goog.net.xpc.Transport', 'goog.net.xpc.TransportTypes', 'goog.reflect']);
goog.addDependency('net/xpc/relay.js', ['goog.net.xpc.relay'], []);
goog.addDependency('net/xpc/transport.js', ['goog.net.xpc.Transport'], ['goog.Disposable', 'goog.dom', 'goog.net.xpc.TransportNames']);
goog.addDependency('net/xpc/xpc.js', ['goog.net.xpc', 'goog.net.xpc.CfgFields', 'goog.net.xpc.ChannelStates', 'goog.net.xpc.TransportNames', 'goog.net.xpc.TransportTypes', 'goog.net.xpc.UriCfgFields'], ['goog.log']);
goog.addDependency('object/object.js', ['goog.object'], []);
goog.addDependency('object/object_test.js', ['goog.objectTest'], ['goog.functions', 'goog.object', 'goog.testing.jsunit']);
goog.addDependency('positioning/absoluteposition.js', ['goog.positioning.AbsolutePosition'], ['goog.math.Box', 'goog.math.Coordinate', 'goog.math.Size', 'goog.positioning', 'goog.positioning.AbstractPosition']);
goog.addDependency('positioning/abstractposition.js', ['goog.positioning.AbstractPosition'], ['goog.math.Box', 'goog.math.Size', 'goog.positioning.Corner']);
goog.addDependency('positioning/anchoredposition.js', ['goog.positioning.AnchoredPosition'], ['goog.math.Box', 'goog.positioning', 'goog.positioning.AbstractPosition']);
goog.addDependency('positioning/anchoredposition_test.js', ['goog.positioning.AnchoredPositionTest'], ['goog.dom', 'goog.positioning.AnchoredPosition', 'goog.positioning.Corner', 'goog.positioning.Overflow', 'goog.style', 'goog.testing.jsunit']);
goog.addDependency('positioning/anchoredviewportposition.js', ['goog.positioning.AnchoredViewportPosition'], ['goog.math.Box', 'goog.positioning', 'goog.positioning.AnchoredPosition', 'goog.positioning.Corner', 'goog.positioning.Overflow', 'goog.positioning.OverflowStatus']);
goog.addDependency('positioning/anchoredviewportposition_test.js', ['goog.positioning.AnchoredViewportPositionTest'], ['goog.dom', 'goog.math.Box', 'goog.positioning.AnchoredViewportPosition', 'goog.positioning.Corner', 'goog.positioning.OverflowStatus', 'goog.style', 'goog.testing.jsunit']);
goog.addDependency('positioning/clientposition.js', ['goog.positioning.ClientPosition'], ['goog.asserts', 'goog.math.Box', 'goog.math.Coordinate', 'goog.math.Size', 'goog.positioning', 'goog.positioning.AbstractPosition', 'goog.style']);
goog.addDependency('positioning/clientposition_test.js', ['goog.positioning.clientPositionTest'], ['goog.dom', 'goog.positioning.ClientPosition', 'goog.style', 'goog.testing.jsunit']);
goog.addDependency('positioning/menuanchoredposition.js', ['goog.positioning.MenuAnchoredPosition'], ['goog.math.Box', 'goog.math.Size', 'goog.positioning', 'goog.positioning.AnchoredViewportPosition', 'goog.positioning.Corner', 'goog.positioning.Overflow']);
goog.addDependency('positioning/menuanchoredposition_test.js', ['goog.positioning.MenuAnchoredPositionTest'], ['goog.dom', 'goog.positioning.Corner', 'goog.positioning.MenuAnchoredPosition', 'goog.testing.jsunit']);
goog.addDependency('positioning/positioning.js', ['goog.positioning', 'goog.positioning.Corner', 'goog.positioning.CornerBit', 'goog.positioning.Overflow', 'goog.positioning.OverflowStatus'], ['goog.asserts', 'goog.dom', 'goog.dom.TagName', 'goog.math.Box', 'goog.math.Coordinate', 'goog.math.Size', 'goog.style', 'goog.style.bidi']);
goog.addDependency('positioning/positioning_test.js', ['goog.positioningTest'], ['goog.dom', 'goog.dom.DomHelper', 'goog.math.Box', 'goog.math.Coordinate', 'goog.math.Rect', 'goog.math.Size', 'goog.positioning', 'goog.positioning.Corner', 'goog.positioning.Overflow', 'goog.positioning.OverflowStatus', 'goog.style', 'goog.testing.ExpectedFailures', 'goog.testing.jsunit', 'goog.userAgent', 'goog.userAgent.product']);
goog.addDependency('positioning/viewportclientposition.js', ['goog.positioning.ViewportClientPosition'], ['goog.math.Box', 'goog.math.Coordinate', 'goog.math.Size', 'goog.positioning.ClientPosition']);
goog.addDependency('positioning/viewportclientposition_test.js', ['goog.positioning.ViewportClientPositionTest'], ['goog.dom', 'goog.positioning.Corner', 'goog.positioning.Overflow', 'goog.positioning.ViewportClientPosition', 'goog.style', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('positioning/viewportposition.js', ['goog.positioning.ViewportPosition'], ['goog.math.Box', 'goog.math.Coordinate', 'goog.math.Size', 'goog.positioning.AbstractPosition']);
goog.addDependency('promise/promise.js', ['goog.Promise'], ['goog.Thenable', 'goog.asserts', 'goog.async.run', 'goog.async.throwException', 'goog.debug.Error', 'goog.promise.Resolver']);
goog.addDependency('promise/promise_test.js', ['goog.PromiseTest'], ['goog.Promise', 'goog.Thenable', 'goog.functions', 'goog.testing.AsyncTestCase', 'goog.testing.MockClock', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('promise/resolver.js', ['goog.promise.Resolver'], []);
goog.addDependency('promise/testsuiteadapter.js', ['goog.promise.testSuiteAdapter'], ['goog.Promise']);
goog.addDependency('promise/thenable.js', ['goog.Thenable'], []);
goog.addDependency('proto/proto.js', ['goog.proto'], ['goog.proto.Serializer']);
goog.addDependency('proto/serializer.js', ['goog.proto.Serializer'], ['goog.json.Serializer', 'goog.string']);
goog.addDependency('proto/serializer_test.js', ['goog.protoTest'], ['goog.proto', 'goog.testing.jsunit']);
goog.addDependency('proto2/descriptor.js', ['goog.proto2.Descriptor', 'goog.proto2.Metadata'], ['goog.array', 'goog.asserts', 'goog.object', 'goog.string']);
goog.addDependency('proto2/descriptor_test.js', ['goog.proto2.DescriptorTest'], ['goog.proto2.Descriptor', 'goog.testing.jsunit']);
goog.addDependency('proto2/fielddescriptor.js', ['goog.proto2.FieldDescriptor'], ['goog.asserts', 'goog.string']);
goog.addDependency('proto2/fielddescriptor_test.js', ['goog.proto2.FieldDescriptorTest'], ['goog.proto2.FieldDescriptor', 'goog.proto2.Message', 'goog.testing.jsunit']);
goog.addDependency('proto2/lazydeserializer.js', ['goog.proto2.LazyDeserializer'], ['goog.asserts', 'goog.proto2.Message', 'goog.proto2.Serializer']);
goog.addDependency('proto2/message.js', ['goog.proto2.Message'], ['goog.asserts', 'goog.proto2.Descriptor', 'goog.proto2.FieldDescriptor']);
goog.addDependency('proto2/message_test.js', ['goog.proto2.MessageTest'], ['goog.testing.jsunit', 'proto2.TestAllTypes', 'proto2.TestAllTypes.NestedEnum', 'proto2.TestAllTypes.NestedMessage', 'proto2.TestAllTypes.OptionalGroup', 'proto2.TestAllTypes.RepeatedGroup']);
goog.addDependency('proto2/objectserializer.js', ['goog.proto2.ObjectSerializer'], ['goog.asserts', 'goog.proto2.Serializer', 'goog.string']);
goog.addDependency('proto2/objectserializer_test.js', ['goog.proto2.ObjectSerializerTest'], ['goog.proto2.ObjectSerializer', 'goog.proto2.Serializer', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'proto2.TestAllTypes']);
goog.addDependency('proto2/package_test.pb.js', ['someprotopackage.TestPackageTypes'], ['goog.proto2.Message', 'proto2.TestAllTypes']);
goog.addDependency('proto2/pbliteserializer.js', ['goog.proto2.PbLiteSerializer'], ['goog.asserts', 'goog.proto2.FieldDescriptor', 'goog.proto2.LazyDeserializer', 'goog.proto2.Serializer']);
goog.addDependency('proto2/pbliteserializer_test.js', ['goog.proto2.PbLiteSerializerTest'], ['goog.proto2.PbLiteSerializer', 'goog.testing.jsunit', 'proto2.TestAllTypes']);
goog.addDependency('proto2/proto_test.js', ['goog.proto2.messageTest'], ['goog.proto2.FieldDescriptor', 'goog.testing.jsunit', 'proto2.TestAllTypes', 'someprotopackage.TestPackageTypes']);
goog.addDependency('proto2/serializer.js', ['goog.proto2.Serializer'], ['goog.asserts', 'goog.proto2.FieldDescriptor', 'goog.proto2.Message']);
goog.addDependency('proto2/test.pb.js', ['proto2.TestAllTypes', 'proto2.TestAllTypes.NestedEnum', 'proto2.TestAllTypes.NestedMessage', 'proto2.TestAllTypes.OptionalGroup', 'proto2.TestAllTypes.RepeatedGroup', 'proto2.TestDefaultChild', 'proto2.TestDefaultParent'], ['goog.proto2.Message']);
goog.addDependency('proto2/textformatserializer.js', ['goog.proto2.TextFormatSerializer'], ['goog.array', 'goog.asserts', 'goog.json', 'goog.math', 'goog.object', 'goog.proto2.FieldDescriptor', 'goog.proto2.Message', 'goog.proto2.Serializer', 'goog.string']);
goog.addDependency('proto2/textformatserializer_test.js', ['goog.proto2.TextFormatSerializerTest'], ['goog.proto2.ObjectSerializer', 'goog.proto2.TextFormatSerializer', 'goog.testing.jsunit', 'proto2.TestAllTypes']);
goog.addDependency('proto2/util.js', ['goog.proto2.Util'], ['goog.asserts']);
goog.addDependency('pubsub/pubsub.js', ['goog.pubsub.PubSub'], ['goog.Disposable', 'goog.array']);
goog.addDependency('pubsub/pubsub_test.js', ['goog.pubsub.PubSubTest'], ['goog.array', 'goog.pubsub.PubSub', 'goog.testing.jsunit']);
goog.addDependency('pubsub/topicid.js', ['goog.pubsub.TopicId'], []);
goog.addDependency('pubsub/typedpubsub.js', ['goog.pubsub.TypedPubSub'], ['goog.Disposable', 'goog.pubsub.PubSub']);
goog.addDependency('pubsub/typedpubsub_test.js', ['goog.pubsub.TypedPubSubTest'], ['goog.array', 'goog.pubsub.TopicId', 'goog.pubsub.TypedPubSub', 'goog.testing.jsunit']);
goog.addDependency('reflect/reflect.js', ['goog.reflect'], []);
goog.addDependency('result/deferredadaptor.js', ['goog.result.DeferredAdaptor'], ['goog.async.Deferred', 'goog.result', 'goog.result.Result']);
goog.addDependency('result/dependentresult.js', ['goog.result.DependentResult'], ['goog.result.Result']);
goog.addDependency('result/result_interface.js', ['goog.result.Result'], ['goog.Thenable']);
goog.addDependency('result/resultutil.js', ['goog.result'], ['goog.array', 'goog.result.DependentResult', 'goog.result.Result', 'goog.result.SimpleResult']);
goog.addDependency('result/simpleresult.js', ['goog.result.SimpleResult', 'goog.result.SimpleResult.StateError'], ['goog.Promise', 'goog.Thenable', 'goog.debug.Error', 'goog.result.Result']);
goog.addDependency('soy/data.js', ['goog.soy.data', 'goog.soy.data.SanitizedContent', 'goog.soy.data.SanitizedContentKind'], ['goog.html.SafeHtml', 'goog.html.uncheckedconversions', 'goog.string.Const']);
goog.addDependency('soy/data_test.js', ['goog.soy.dataTest'], ['goog.html.SafeHtml', 'goog.soy.testHelper', 'goog.testing.jsunit']);
goog.addDependency('soy/renderer.js', ['goog.soy.InjectedDataSupplier', 'goog.soy.Renderer'], ['goog.asserts', 'goog.dom', 'goog.soy', 'goog.soy.data.SanitizedContent', 'goog.soy.data.SanitizedContentKind']);
goog.addDependency('soy/renderer_test.js', ['goog.soy.RendererTest'], ['goog.dom', 'goog.dom.NodeType', 'goog.dom.TagName', 'goog.html.SafeHtml', 'goog.i18n.bidi.Dir', 'goog.soy.Renderer', 'goog.soy.data.SanitizedContentKind', 'goog.soy.testHelper', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('soy/soy.js', ['goog.soy'], ['goog.asserts', 'goog.dom', 'goog.dom.NodeType', 'goog.dom.TagName', 'goog.soy.data.SanitizedContent', 'goog.soy.data.SanitizedContentKind', 'goog.string']);
goog.addDependency('soy/soy_test.js', ['goog.soyTest'], ['goog.dom', 'goog.dom.NodeType', 'goog.dom.TagName', 'goog.functions', 'goog.soy', 'goog.soy.testHelper', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit']);
goog.addDependency('soy/soy_testhelper.js', ['goog.soy.testHelper'], ['goog.dom', 'goog.dom.TagName', 'goog.i18n.bidi.Dir', 'goog.soy.data.SanitizedContent', 'goog.soy.data.SanitizedContentKind', 'goog.string', 'goog.userAgent']);
goog.addDependency('spell/spellcheck.js', ['goog.spell.SpellCheck', 'goog.spell.SpellCheck.WordChangedEvent'], ['goog.Timer', 'goog.events.EventTarget', 'goog.structs.Set']);
goog.addDependency('spell/spellcheck_test.js', ['goog.spell.SpellCheckTest'], ['goog.spell.SpellCheck', 'goog.testing.jsunit']);
goog.addDependency('stats/basicstat.js', ['goog.stats.BasicStat'], ['goog.array', 'goog.iter', 'goog.log', 'goog.object', 'goog.string.format', 'goog.structs.CircularBuffer']);
goog.addDependency('stats/basicstat_test.js', ['goog.stats.BasicStatTest'], ['goog.array', 'goog.stats.BasicStat', 'goog.string.format', 'goog.testing.PseudoRandom', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('storage/collectablestorage.js', ['goog.storage.CollectableStorage'], ['goog.array', 'goog.iter', 'goog.storage.ErrorCode', 'goog.storage.ExpiringStorage', 'goog.storage.RichStorage']);
goog.addDependency('storage/collectablestorage_test.js', ['goog.storage.CollectableStorageTest'], ['goog.storage.CollectableStorage', 'goog.storage.collectableStorageTester', 'goog.storage.storage_test', 'goog.testing.MockClock', 'goog.testing.jsunit', 'goog.testing.storage.FakeMechanism']);
goog.addDependency('storage/collectablestoragetester.js', ['goog.storage.collectableStorageTester'], ['goog.testing.asserts']);
goog.addDependency('storage/encryptedstorage.js', ['goog.storage.EncryptedStorage'], ['goog.crypt', 'goog.crypt.Arc4', 'goog.crypt.Sha1', 'goog.crypt.base64', 'goog.json', 'goog.json.Serializer', 'goog.storage.CollectableStorage', 'goog.storage.ErrorCode', 'goog.storage.RichStorage', 'goog.storage.RichStorage.Wrapper', 'goog.storage.mechanism.IterableMechanism']);
goog.addDependency('storage/encryptedstorage_test.js', ['goog.storage.EncryptedStorageTest'], ['goog.json', 'goog.storage.EncryptedStorage', 'goog.storage.ErrorCode', 'goog.storage.RichStorage', 'goog.storage.collectableStorageTester', 'goog.storage.storage_test', 'goog.testing.MockClock', 'goog.testing.PseudoRandom', 'goog.testing.jsunit', 'goog.testing.storage.FakeMechanism']);
goog.addDependency('storage/errorcode.js', ['goog.storage.ErrorCode'], []);
goog.addDependency('storage/expiringstorage.js', ['goog.storage.ExpiringStorage'], ['goog.storage.RichStorage', 'goog.storage.RichStorage.Wrapper', 'goog.storage.mechanism.Mechanism']);
goog.addDependency('storage/expiringstorage_test.js', ['goog.storage.ExpiringStorageTest'], ['goog.storage.ExpiringStorage', 'goog.storage.storage_test', 'goog.testing.MockClock', 'goog.testing.jsunit', 'goog.testing.storage.FakeMechanism']);
goog.addDependency('storage/mechanism/errorcode.js', ['goog.storage.mechanism.ErrorCode'], []);
goog.addDependency('storage/mechanism/errorhandlingmechanism.js', ['goog.storage.mechanism.ErrorHandlingMechanism'], ['goog.storage.mechanism.Mechanism']);
goog.addDependency('storage/mechanism/errorhandlingmechanism_test.js', ['goog.storage.mechanism.ErrorHandlingMechanismTest'], ['goog.storage.mechanism.ErrorHandlingMechanism', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('storage/mechanism/html5localstorage.js', ['goog.storage.mechanism.HTML5LocalStorage'], ['goog.storage.mechanism.HTML5WebStorage']);
goog.addDependency('storage/mechanism/html5localstorage_test.js', ['goog.storage.mechanism.HTML5LocalStorageTest'], ['goog.storage.mechanism.HTML5LocalStorage', 'goog.storage.mechanism.mechanismSeparationTester', 'goog.storage.mechanism.mechanismSharingTester', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('storage/mechanism/html5sessionstorage.js', ['goog.storage.mechanism.HTML5SessionStorage'], ['goog.storage.mechanism.HTML5WebStorage']);
goog.addDependency('storage/mechanism/html5sessionstorage_test.js', ['goog.storage.mechanism.HTML5SessionStorageTest'], ['goog.storage.mechanism.HTML5SessionStorage', 'goog.storage.mechanism.mechanismSeparationTester', 'goog.storage.mechanism.mechanismSharingTester', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('storage/mechanism/html5webstorage.js', ['goog.storage.mechanism.HTML5WebStorage'], ['goog.asserts', 'goog.iter.Iterator', 'goog.iter.StopIteration', 'goog.storage.mechanism.ErrorCode', 'goog.storage.mechanism.IterableMechanism']);
goog.addDependency('storage/mechanism/html5webstorage_test.js', ['goog.storage.mechanism.HTML5MockStorage', 'goog.storage.mechanism.HTML5WebStorageTest', 'goog.storage.mechanism.MockThrowableStorage'], ['goog.storage.mechanism.ErrorCode', 'goog.storage.mechanism.HTML5WebStorage', 'goog.testing.jsunit']);
goog.addDependency('storage/mechanism/ieuserdata.js', ['goog.storage.mechanism.IEUserData'], ['goog.asserts', 'goog.iter.Iterator', 'goog.iter.StopIteration', 'goog.storage.mechanism.ErrorCode', 'goog.storage.mechanism.IterableMechanism', 'goog.structs.Map', 'goog.userAgent']);
goog.addDependency('storage/mechanism/ieuserdata_test.js', ['goog.storage.mechanism.IEUserDataTest'], ['goog.storage.mechanism.IEUserData', 'goog.storage.mechanism.mechanismSeparationTester', 'goog.storage.mechanism.mechanismSharingTester', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('storage/mechanism/iterablemechanism.js', ['goog.storage.mechanism.IterableMechanism'], ['goog.array', 'goog.asserts', 'goog.iter', 'goog.iter.Iterator', 'goog.storage.mechanism.Mechanism']);
goog.addDependency('storage/mechanism/iterablemechanismtester.js', ['goog.storage.mechanism.iterableMechanismTester'], ['goog.iter.Iterator', 'goog.storage.mechanism.IterableMechanism', 'goog.testing.asserts']);
goog.addDependency('storage/mechanism/mechanism.js', ['goog.storage.mechanism.Mechanism'], []);
goog.addDependency('storage/mechanism/mechanismfactory.js', ['goog.storage.mechanism.mechanismfactory'], ['goog.storage.mechanism.HTML5LocalStorage', 'goog.storage.mechanism.HTML5SessionStorage', 'goog.storage.mechanism.IEUserData', 'goog.storage.mechanism.IterableMechanism', 'goog.storage.mechanism.PrefixedMechanism']);
goog.addDependency('storage/mechanism/mechanismfactory_test.js', ['goog.storage.mechanism.mechanismfactoryTest'], ['goog.storage.mechanism.mechanismfactory', 'goog.testing.jsunit']);
goog.addDependency('storage/mechanism/mechanismseparationtester.js', ['goog.storage.mechanism.mechanismSeparationTester'], ['goog.iter.Iterator', 'goog.storage.mechanism.IterableMechanism', 'goog.testing.asserts']);
goog.addDependency('storage/mechanism/mechanismsharingtester.js', ['goog.storage.mechanism.mechanismSharingTester'], ['goog.iter.Iterator', 'goog.storage.mechanism.IterableMechanism', 'goog.testing.asserts']);
goog.addDependency('storage/mechanism/mechanismtester.js', ['goog.storage.mechanism.mechanismTester'], ['goog.storage.mechanism.ErrorCode', 'goog.storage.mechanism.HTML5LocalStorage', 'goog.storage.mechanism.Mechanism', 'goog.testing.asserts', 'goog.userAgent.product', 'goog.userAgent.product.isVersion']);
goog.addDependency('storage/mechanism/prefixedmechanism.js', ['goog.storage.mechanism.PrefixedMechanism'], ['goog.iter.Iterator', 'goog.storage.mechanism.IterableMechanism']);
goog.addDependency('storage/mechanism/prefixedmechanism_test.js', ['goog.storage.mechanism.PrefixedMechanismTest'], ['goog.storage.mechanism.HTML5LocalStorage', 'goog.storage.mechanism.PrefixedMechanism', 'goog.storage.mechanism.mechanismSeparationTester', 'goog.storage.mechanism.mechanismSharingTester', 'goog.testing.jsunit']);
goog.addDependency('storage/richstorage.js', ['goog.storage.RichStorage', 'goog.storage.RichStorage.Wrapper'], ['goog.storage.ErrorCode', 'goog.storage.Storage', 'goog.storage.mechanism.Mechanism']);
goog.addDependency('storage/richstorage_test.js', ['goog.storage.RichStorageTest'], ['goog.storage.ErrorCode', 'goog.storage.RichStorage', 'goog.storage.storage_test', 'goog.testing.jsunit', 'goog.testing.storage.FakeMechanism']);
goog.addDependency('storage/storage.js', ['goog.storage.Storage'], ['goog.json', 'goog.storage.ErrorCode']);
goog.addDependency('storage/storage_test.js', ['goog.storage.storage_test'], ['goog.storage.Storage', 'goog.structs.Map', 'goog.testing.asserts']);
goog.addDependency('string/const.js', ['goog.string.Const'], ['goog.asserts', 'goog.string.TypedString']);
goog.addDependency('string/const_test.js', ['goog.string.constTest'], ['goog.string.Const', 'goog.testing.jsunit']);
goog.addDependency('string/linkify.js', ['goog.string.linkify'], ['goog.string']);
goog.addDependency('string/linkify_test.js', ['goog.string.linkifyTest'], ['goog.string', 'goog.string.linkify', 'goog.testing.dom', 'goog.testing.jsunit']);
goog.addDependency('string/newlines.js', ['goog.string.newlines', 'goog.string.newlines.Line'], ['goog.array']);
goog.addDependency('string/newlines_test.js', ['goog.string.newlinesTest'], ['goog.string.newlines', 'goog.testing.jsunit']);
goog.addDependency('string/parser.js', ['goog.string.Parser'], []);
goog.addDependency('string/path.js', ['goog.string.path'], ['goog.array', 'goog.string']);
goog.addDependency('string/path_test.js', ['goog.string.pathTest'], ['goog.string.path', 'goog.testing.jsunit']);
goog.addDependency('string/string.js', ['goog.string', 'goog.string.Unicode'], []);
goog.addDependency('string/string_test.js', ['goog.stringTest'], ['goog.functions', 'goog.object', 'goog.string', 'goog.string.Unicode', 'goog.testing.MockControl', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit']);
goog.addDependency('string/stringbuffer.js', ['goog.string.StringBuffer'], []);
goog.addDependency('string/stringbuffer_test.js', ['goog.string.StringBufferTest'], ['goog.string.StringBuffer', 'goog.testing.jsunit']);
goog.addDependency('string/stringformat.js', ['goog.string.format'], ['goog.string']);
goog.addDependency('string/stringformat_test.js', ['goog.string.formatTest'], ['goog.string.format', 'goog.testing.jsunit']);
goog.addDependency('string/stringifier.js', ['goog.string.Stringifier'], []);
goog.addDependency('string/typedstring.js', ['goog.string.TypedString'], []);
goog.addDependency('structs/avltree.js', ['goog.structs.AvlTree', 'goog.structs.AvlTree.Node'], ['goog.structs.Collection']);
goog.addDependency('structs/avltree_test.js', ['goog.structs.AvlTreeTest'], ['goog.array', 'goog.structs.AvlTree', 'goog.testing.jsunit']);
goog.addDependency('structs/circularbuffer.js', ['goog.structs.CircularBuffer'], []);
goog.addDependency('structs/circularbuffer_test.js', ['goog.structs.CircularBufferTest'], ['goog.structs.CircularBuffer', 'goog.testing.jsunit']);
goog.addDependency('structs/collection.js', ['goog.structs.Collection'], []);
goog.addDependency('structs/collection_test.js', ['goog.structs.CollectionTest'], ['goog.structs.AvlTree', 'goog.structs.Set', 'goog.testing.jsunit']);
goog.addDependency('structs/heap.js', ['goog.structs.Heap'], ['goog.array', 'goog.object', 'goog.structs.Node']);
goog.addDependency('structs/heap_test.js', ['goog.structs.HeapTest'], ['goog.structs', 'goog.structs.Heap', 'goog.testing.jsunit']);
goog.addDependency('structs/inversionmap.js', ['goog.structs.InversionMap'], ['goog.array']);
goog.addDependency('structs/inversionmap_test.js', ['goog.structs.InversionMapTest'], ['goog.structs.InversionMap', 'goog.testing.jsunit']);
goog.addDependency('structs/linkedmap.js', ['goog.structs.LinkedMap'], ['goog.structs.Map']);
goog.addDependency('structs/linkedmap_test.js', ['goog.structs.LinkedMapTest'], ['goog.structs.LinkedMap', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('structs/map.js', ['goog.structs.Map'], ['goog.iter.Iterator', 'goog.iter.StopIteration', 'goog.object']);
goog.addDependency('structs/map_test.js', ['goog.structs.MapTest'], ['goog.iter', 'goog.structs', 'goog.structs.Map', 'goog.testing.jsunit']);
goog.addDependency('structs/node.js', ['goog.structs.Node'], []);
goog.addDependency('structs/pool.js', ['goog.structs.Pool'], ['goog.Disposable', 'goog.structs.Queue', 'goog.structs.Set']);
goog.addDependency('structs/pool_test.js', ['goog.structs.PoolTest'], ['goog.structs.Pool', 'goog.testing.MockClock', 'goog.testing.jsunit']);
goog.addDependency('structs/prioritypool.js', ['goog.structs.PriorityPool'], ['goog.structs.Pool', 'goog.structs.PriorityQueue']);
goog.addDependency('structs/prioritypool_test.js', ['goog.structs.PriorityPoolTest'], ['goog.structs.PriorityPool', 'goog.testing.MockClock', 'goog.testing.jsunit']);
goog.addDependency('structs/priorityqueue.js', ['goog.structs.PriorityQueue'], ['goog.structs.Heap']);
goog.addDependency('structs/priorityqueue_test.js', ['goog.structs.PriorityQueueTest'], ['goog.structs', 'goog.structs.PriorityQueue', 'goog.testing.jsunit']);
goog.addDependency('structs/quadtree.js', ['goog.structs.QuadTree', 'goog.structs.QuadTree.Node', 'goog.structs.QuadTree.Point'], ['goog.math.Coordinate']);
goog.addDependency('structs/quadtree_test.js', ['goog.structs.QuadTreeTest'], ['goog.structs', 'goog.structs.QuadTree', 'goog.testing.jsunit']);
goog.addDependency('structs/queue.js', ['goog.structs.Queue'], ['goog.array']);
goog.addDependency('structs/queue_test.js', ['goog.structs.QueueTest'], ['goog.structs.Queue', 'goog.testing.jsunit']);
goog.addDependency('structs/set.js', ['goog.structs.Set'], ['goog.structs', 'goog.structs.Collection', 'goog.structs.Map']);
goog.addDependency('structs/set_test.js', ['goog.structs.SetTest'], ['goog.iter', 'goog.structs', 'goog.structs.Set', 'goog.testing.jsunit']);
goog.addDependency('structs/simplepool.js', ['goog.structs.SimplePool'], ['goog.Disposable']);
goog.addDependency('structs/stringset.js', ['goog.structs.StringSet'], ['goog.asserts', 'goog.iter']);
goog.addDependency('structs/stringset_test.js', ['goog.structs.StringSetTest'], ['goog.array', 'goog.iter', 'goog.structs.StringSet', 'goog.testing.asserts', 'goog.testing.jsunit']);
goog.addDependency('structs/structs.js', ['goog.structs'], ['goog.array', 'goog.object']);
goog.addDependency('structs/structs_test.js', ['goog.structsTest'], ['goog.array', 'goog.structs', 'goog.structs.Map', 'goog.structs.Set', 'goog.testing.jsunit']);
goog.addDependency('structs/treenode.js', ['goog.structs.TreeNode'], ['goog.array', 'goog.asserts', 'goog.structs.Node']);
goog.addDependency('structs/treenode_test.js', ['goog.structs.TreeNodeTest'], ['goog.structs.TreeNode', 'goog.testing.jsunit']);
goog.addDependency('structs/trie.js', ['goog.structs.Trie'], ['goog.object', 'goog.structs']);
goog.addDependency('structs/trie_test.js', ['goog.structs.TrieTest'], ['goog.object', 'goog.structs', 'goog.structs.Trie', 'goog.testing.jsunit']);
goog.addDependency('style/bidi.js', ['goog.style.bidi'], ['goog.dom', 'goog.style', 'goog.userAgent']);
goog.addDependency('style/bidi_test.js', ['goog.style.bidiTest'], ['goog.dom', 'goog.style', 'goog.style.bidi', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('style/cursor.js', ['goog.style.cursor'], ['goog.userAgent']);
goog.addDependency('style/cursor_test.js', ['goog.style.cursorTest'], ['goog.style.cursor', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('style/style.js', ['goog.style'], ['goog.array', 'goog.asserts', 'goog.dom', 'goog.dom.NodeType', 'goog.dom.vendor', 'goog.math.Box', 'goog.math.Coordinate', 'goog.math.Rect', 'goog.math.Size', 'goog.object', 'goog.string', 'goog.userAgent']);
goog.addDependency('style/style_test.js', ['goog.style_test'], ['goog.array', 'goog.color', 'goog.dom', 'goog.events.BrowserEvent', 'goog.labs.userAgent.util', 'goog.math.Box', 'goog.math.Coordinate', 'goog.math.Rect', 'goog.math.Size', 'goog.object', 'goog.string', 'goog.style', 'goog.testing.ExpectedFailures', 'goog.testing.MockUserAgent', 'goog.testing.asserts', 'goog.testing.jsunit', 'goog.userAgent', 'goog.userAgent.product', 'goog.userAgent.product.isVersion', 'goog.userAgentTestUtil', 'goog.userAgentTestUtil.UserAgents']);
goog.addDependency('style/style_webkit_scrollbars_test.js', ['goog.style.webkitScrollbarsTest'], ['goog.asserts', 'goog.style', 'goog.styleScrollbarTester', 'goog.testing.ExpectedFailures', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('style/stylescrollbartester.js', ['goog.styleScrollbarTester'], ['goog.dom', 'goog.style', 'goog.testing.asserts']);
goog.addDependency('style/transform.js', ['goog.style.transform'], ['goog.functions', 'goog.math.Coordinate', 'goog.style', 'goog.userAgent', 'goog.userAgent.product.isVersion']);
goog.addDependency('style/transform_test.js', ['goog.style.transformTest'], ['goog.dom', 'goog.style.transform', 'goog.testing.jsunit', 'goog.userAgent', 'goog.userAgent.product.isVersion']);
goog.addDependency('style/transition.js', ['goog.style.transition', 'goog.style.transition.Css3Property'], ['goog.array', 'goog.asserts', 'goog.dom.safe', 'goog.dom.vendor', 'goog.functions', 'goog.html.SafeHtml', 'goog.style', 'goog.userAgent']);
goog.addDependency('style/transition_test.js', ['goog.style.transitionTest'], ['goog.style', 'goog.style.transition', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('testing/asserts.js', ['goog.testing.JsUnitException', 'goog.testing.asserts'], ['goog.testing.stacktrace']);
goog.addDependency('testing/asserts_test.js', ['goog.testing.assertsTest'], ['goog.array', 'goog.dom', 'goog.iter.Iterator', 'goog.iter.StopIteration', 'goog.structs.Map', 'goog.structs.Set', 'goog.testing.asserts', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('testing/async/mockcontrol.js', ['goog.testing.async.MockControl'], ['goog.asserts', 'goog.async.Deferred', 'goog.debug', 'goog.testing.asserts', 'goog.testing.mockmatchers.IgnoreArgument']);
goog.addDependency('testing/async/mockcontrol_test.js', ['goog.testing.async.MockControlTest'], ['goog.async.Deferred', 'goog.testing.MockControl', 'goog.testing.asserts', 'goog.testing.async.MockControl', 'goog.testing.jsunit']);
goog.addDependency('testing/asynctestcase.js', ['goog.testing.AsyncTestCase', 'goog.testing.AsyncTestCase.ControlBreakingException'], ['goog.testing.TestCase', 'goog.testing.TestCase.Test', 'goog.testing.asserts']);
goog.addDependency('testing/asynctestcase_async_test.js', ['goog.testing.AsyncTestCaseAsyncTest'], ['goog.testing.AsyncTestCase', 'goog.testing.jsunit']);
goog.addDependency('testing/asynctestcase_noasync_test.js', ['goog.testing.AsyncTestCaseSyncTest'], ['goog.testing.AsyncTestCase', 'goog.testing.jsunit']);
goog.addDependency('testing/asynctestcase_test.js', ['goog.testing.AsyncTestCaseTest'], ['goog.debug.Error', 'goog.testing.AsyncTestCase', 'goog.testing.asserts', 'goog.testing.jsunit']);
goog.addDependency('testing/benchmark.js', ['goog.testing.benchmark'], ['goog.dom', 'goog.dom.TagName', 'goog.testing.PerformanceTable', 'goog.testing.PerformanceTimer', 'goog.testing.TestCase']);
goog.addDependency('testing/continuationtestcase.js', ['goog.testing.ContinuationTestCase', 'goog.testing.ContinuationTestCase.Step', 'goog.testing.ContinuationTestCase.Test'], ['goog.array', 'goog.events.EventHandler', 'goog.testing.TestCase', 'goog.testing.TestCase.Test', 'goog.testing.asserts']);
goog.addDependency('testing/continuationtestcase_test.js', ['goog.testing.ContinuationTestCaseTest'], ['goog.events', 'goog.events.EventTarget', 'goog.testing.ContinuationTestCase', 'goog.testing.MockClock', 'goog.testing.PropertyReplacer', 'goog.testing.TestCase', 'goog.testing.jsunit']);
goog.addDependency('testing/deferredtestcase.js', ['goog.testing.DeferredTestCase'], ['goog.async.Deferred', 'goog.testing.AsyncTestCase', 'goog.testing.TestCase']);
goog.addDependency('testing/deferredtestcase_test.js', ['goog.testing.DeferredTestCaseTest'], ['goog.async.Deferred', 'goog.testing.DeferredTestCase', 'goog.testing.TestCase', 'goog.testing.TestRunner', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('testing/dom.js', ['goog.testing.dom'], ['goog.array', 'goog.asserts', 'goog.dom', 'goog.dom.NodeIterator', 'goog.dom.NodeType', 'goog.dom.TagIterator', 'goog.dom.TagName', 'goog.dom.classlist', 'goog.iter', 'goog.object', 'goog.string', 'goog.style', 'goog.testing.asserts', 'goog.userAgent']);
goog.addDependency('testing/dom_test.js', ['goog.testing.domTest'], ['goog.dom', 'goog.dom.TagName', 'goog.testing.dom', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('testing/editor/dom.js', ['goog.testing.editor.dom'], ['goog.dom.NodeType', 'goog.dom.TagIterator', 'goog.dom.TagWalkType', 'goog.iter', 'goog.string', 'goog.testing.asserts']);
goog.addDependency('testing/editor/dom_test.js', ['goog.testing.editor.domTest'], ['goog.dom', 'goog.dom.TagName', 'goog.functions', 'goog.testing.editor.dom', 'goog.testing.jsunit']);
goog.addDependency('testing/editor/fieldmock.js', ['goog.testing.editor.FieldMock'], ['goog.dom', 'goog.dom.Range', 'goog.editor.Field', 'goog.testing.LooseMock', 'goog.testing.mockmatchers']);
goog.addDependency('testing/editor/testhelper.js', ['goog.testing.editor.TestHelper'], ['goog.Disposable', 'goog.dom', 'goog.dom.Range', 'goog.editor.BrowserFeature', 'goog.editor.node', 'goog.editor.plugins.AbstractBubblePlugin', 'goog.testing.dom']);
goog.addDependency('testing/editor/testhelper_test.js', ['goog.testing.editor.TestHelperTest'], ['goog.dom', 'goog.dom.TagName', 'goog.editor.node', 'goog.testing.editor.TestHelper', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('testing/events/eventobserver.js', ['goog.testing.events.EventObserver'], ['goog.array']);
goog.addDependency('testing/events/eventobserver_test.js', ['goog.testing.events.EventObserverTest'], ['goog.array', 'goog.events', 'goog.events.Event', 'goog.events.EventTarget', 'goog.testing.events.EventObserver', 'goog.testing.jsunit']);
goog.addDependency('testing/events/events.js', ['goog.testing.events', 'goog.testing.events.Event'], ['goog.Disposable', 'goog.asserts', 'goog.dom.NodeType', 'goog.events', 'goog.events.BrowserEvent', 'goog.events.BrowserFeature', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.object', 'goog.style', 'goog.userAgent']);
goog.addDependency('testing/events/events_test.js', ['goog.testing.eventsTest'], ['goog.array', 'goog.dom', 'goog.events', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.math.Coordinate', 'goog.string', 'goog.style', 'goog.testing.PropertyReplacer', 'goog.testing.events', 'goog.testing.jsunit', 'goog.testing.recordFunction', 'goog.userAgent']);
goog.addDependency('testing/events/matchers.js', ['goog.testing.events.EventMatcher'], ['goog.events.Event', 'goog.testing.mockmatchers.ArgumentMatcher']);
goog.addDependency('testing/events/matchers_test.js', ['goog.testing.events.EventMatcherTest'], ['goog.events.Event', 'goog.testing.events.EventMatcher', 'goog.testing.jsunit']);
goog.addDependency('testing/events/onlinehandler.js', ['goog.testing.events.OnlineHandler'], ['goog.events.EventTarget', 'goog.net.NetworkStatusMonitor']);
goog.addDependency('testing/events/onlinehandler_test.js', ['goog.testing.events.OnlineHandlerTest'], ['goog.events', 'goog.net.NetworkStatusMonitor', 'goog.testing.events.EventObserver', 'goog.testing.events.OnlineHandler', 'goog.testing.jsunit']);
goog.addDependency('testing/expectedfailures.js', ['goog.testing.ExpectedFailures'], ['goog.debug.DivConsole', 'goog.dom', 'goog.dom.TagName', 'goog.events', 'goog.events.EventType', 'goog.log', 'goog.style', 'goog.testing.JsUnitException', 'goog.testing.TestCase', 'goog.testing.asserts']);
goog.addDependency('testing/expectedfailures_test.js', ['goog.testing.ExpectedFailuresTest'], ['goog.debug.Logger', 'goog.testing.ExpectedFailures', 'goog.testing.JsUnitException', 'goog.testing.jsunit']);
goog.addDependency('testing/fs/blob.js', ['goog.testing.fs.Blob'], ['goog.crypt.base64']);
goog.addDependency('testing/fs/blob_test.js', ['goog.testing.fs.BlobTest'], ['goog.testing.fs.Blob', 'goog.testing.jsunit']);
goog.addDependency('testing/fs/directoryentry_test.js', ['goog.testing.fs.DirectoryEntryTest'], ['goog.array', 'goog.fs.DirectoryEntry', 'goog.fs.Error', 'goog.testing.AsyncTestCase', 'goog.testing.MockClock', 'goog.testing.fs.FileSystem', 'goog.testing.jsunit']);
goog.addDependency('testing/fs/entry.js', ['goog.testing.fs.DirectoryEntry', 'goog.testing.fs.Entry', 'goog.testing.fs.FileEntry'], ['goog.Timer', 'goog.array', 'goog.asserts', 'goog.async.Deferred', 'goog.fs.DirectoryEntry', 'goog.fs.DirectoryEntryImpl', 'goog.fs.Entry', 'goog.fs.Error', 'goog.fs.FileEntry', 'goog.functions', 'goog.object', 'goog.string', 'goog.testing.fs.File', 'goog.testing.fs.FileWriter']);
goog.addDependency('testing/fs/entry_test.js', ['goog.testing.fs.EntryTest'], ['goog.fs.DirectoryEntry', 'goog.fs.Error', 'goog.testing.AsyncTestCase', 'goog.testing.MockClock', 'goog.testing.fs.FileSystem', 'goog.testing.jsunit']);
goog.addDependency('testing/fs/file.js', ['goog.testing.fs.File'], ['goog.testing.fs.Blob']);
goog.addDependency('testing/fs/fileentry_test.js', ['goog.testing.fs.FileEntryTest'], ['goog.testing.AsyncTestCase', 'goog.testing.MockClock', 'goog.testing.fs.FileEntry', 'goog.testing.fs.FileSystem', 'goog.testing.jsunit']);
goog.addDependency('testing/fs/filereader.js', ['goog.testing.fs.FileReader'], ['goog.Timer', 'goog.events.EventTarget', 'goog.fs.Error', 'goog.fs.FileReader', 'goog.testing.fs.ProgressEvent']);
goog.addDependency('testing/fs/filereader_test.js', ['goog.testing.fs.FileReaderTest'], ['goog.Timer', 'goog.async.Deferred', 'goog.events', 'goog.fs.Error', 'goog.fs.FileReader', 'goog.fs.FileSaver', 'goog.testing.AsyncTestCase', 'goog.testing.fs.FileReader', 'goog.testing.fs.FileSystem', 'goog.testing.jsunit']);
goog.addDependency('testing/fs/filesystem.js', ['goog.testing.fs.FileSystem'], ['goog.fs.FileSystem', 'goog.testing.fs.DirectoryEntry']);
goog.addDependency('testing/fs/filewriter.js', ['goog.testing.fs.FileWriter'], ['goog.Timer', 'goog.events.EventTarget', 'goog.fs.Error', 'goog.fs.FileSaver', 'goog.string', 'goog.testing.fs.ProgressEvent']);
goog.addDependency('testing/fs/filewriter_test.js', ['goog.testing.fs.FileWriterTest'], ['goog.async.Deferred', 'goog.events', 'goog.fs.Error', 'goog.fs.FileSaver', 'goog.testing.AsyncTestCase', 'goog.testing.MockClock', 'goog.testing.fs.Blob', 'goog.testing.fs.FileSystem', 'goog.testing.jsunit']);
goog.addDependency('testing/fs/fs.js', ['goog.testing.fs'], ['goog.Timer', 'goog.array', 'goog.async.Deferred', 'goog.fs', 'goog.testing.fs.Blob', 'goog.testing.fs.FileSystem']);
goog.addDependency('testing/fs/fs_test.js', ['goog.testing.fsTest'], ['goog.testing.AsyncTestCase', 'goog.testing.fs', 'goog.testing.fs.Blob', 'goog.testing.jsunit']);
goog.addDependency('testing/fs/integration_test.js', ['goog.testing.fs.integrationTest'], ['goog.async.Deferred', 'goog.async.DeferredList', 'goog.events', 'goog.fs', 'goog.fs.DirectoryEntry', 'goog.fs.Error', 'goog.fs.FileSaver', 'goog.testing.AsyncTestCase', 'goog.testing.PropertyReplacer', 'goog.testing.fs', 'goog.testing.jsunit']);
goog.addDependency('testing/fs/progressevent.js', ['goog.testing.fs.ProgressEvent'], ['goog.events.Event']);
goog.addDependency('testing/functionmock.js', ['goog.testing', 'goog.testing.FunctionMock', 'goog.testing.GlobalFunctionMock', 'goog.testing.MethodMock'], ['goog.object', 'goog.testing.LooseMock', 'goog.testing.Mock', 'goog.testing.MockInterface', 'goog.testing.PropertyReplacer', 'goog.testing.StrictMock']);
goog.addDependency('testing/functionmock_test.js', ['goog.testing.FunctionMockTest'], ['goog.array', 'goog.string', 'goog.testing', 'goog.testing.FunctionMock', 'goog.testing.Mock', 'goog.testing.StrictMock', 'goog.testing.asserts', 'goog.testing.jsunit', 'goog.testing.mockmatchers']);
goog.addDependency('testing/graphics.js', ['goog.testing.graphics'], ['goog.graphics.Path.Segment', 'goog.testing.asserts']);
goog.addDependency('testing/i18n/asserts.js', ['goog.testing.i18n.asserts'], ['goog.testing.jsunit']);
goog.addDependency('testing/i18n/asserts_test.js', ['goog.testing.i18n.assertsTest'], ['goog.testing.ExpectedFailures', 'goog.testing.i18n.asserts']);
goog.addDependency('testing/jsunit.js', ['goog.testing.jsunit'], ['goog.testing.TestCase', 'goog.testing.TestRunner']);
goog.addDependency('testing/loosemock.js', ['goog.testing.LooseExpectationCollection', 'goog.testing.LooseMock'], ['goog.array', 'goog.structs.Map', 'goog.testing.Mock']);
goog.addDependency('testing/loosemock_test.js', ['goog.testing.LooseMockTest'], ['goog.testing.LooseMock', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.testing.mockmatchers']);
goog.addDependency('testing/messaging/mockmessagechannel.js', ['goog.testing.messaging.MockMessageChannel'], ['goog.messaging.AbstractChannel', 'goog.testing.asserts']);
goog.addDependency('testing/messaging/mockmessageevent.js', ['goog.testing.messaging.MockMessageEvent'], ['goog.events.BrowserEvent', 'goog.events.EventType', 'goog.testing.events']);
goog.addDependency('testing/messaging/mockmessageport.js', ['goog.testing.messaging.MockMessagePort'], ['goog.events.EventTarget']);
goog.addDependency('testing/messaging/mockportnetwork.js', ['goog.testing.messaging.MockPortNetwork'], ['goog.messaging.PortNetwork', 'goog.testing.messaging.MockMessageChannel']);
goog.addDependency('testing/mock.js', ['goog.testing.Mock', 'goog.testing.MockExpectation'], ['goog.array', 'goog.object', 'goog.testing.JsUnitException', 'goog.testing.MockInterface', 'goog.testing.mockmatchers']);
goog.addDependency('testing/mock_test.js', ['goog.testing.MockTest'], ['goog.array', 'goog.testing', 'goog.testing.Mock', 'goog.testing.MockControl', 'goog.testing.MockExpectation', 'goog.testing.jsunit']);
goog.addDependency('testing/mockclassfactory.js', ['goog.testing.MockClassFactory', 'goog.testing.MockClassRecord'], ['goog.array', 'goog.object', 'goog.testing.LooseMock', 'goog.testing.StrictMock', 'goog.testing.TestCase', 'goog.testing.mockmatchers']);
goog.addDependency('testing/mockclassfactory_test.js', ['fake.BaseClass', 'fake.ChildClass', 'goog.testing.MockClassFactoryTest'], ['goog.testing', 'goog.testing.MockClassFactory', 'goog.testing.jsunit']);
goog.addDependency('testing/mockclock.js', ['goog.testing.MockClock'], ['goog.Disposable', 'goog.testing.PropertyReplacer', 'goog.testing.events', 'goog.testing.events.Event', 'goog.testing.watchers']);
goog.addDependency('testing/mockclock_test.js', ['goog.testing.MockClockTest'], ['goog.events', 'goog.functions', 'goog.testing.MockClock', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.testing.recordFunction']);
goog.addDependency('testing/mockcontrol.js', ['goog.testing.MockControl'], ['goog.array', 'goog.testing', 'goog.testing.LooseMock', 'goog.testing.StrictMock']);
goog.addDependency('testing/mockcontrol_test.js', ['goog.testing.MockControlTest'], ['goog.testing.Mock', 'goog.testing.MockControl', 'goog.testing.jsunit']);
goog.addDependency('testing/mockinterface.js', ['goog.testing.MockInterface'], []);
goog.addDependency('testing/mockmatchers.js', ['goog.testing.mockmatchers', 'goog.testing.mockmatchers.ArgumentMatcher', 'goog.testing.mockmatchers.IgnoreArgument', 'goog.testing.mockmatchers.InstanceOf', 'goog.testing.mockmatchers.ObjectEquals', 'goog.testing.mockmatchers.RegexpMatch', 'goog.testing.mockmatchers.SaveArgument', 'goog.testing.mockmatchers.TypeOf'], ['goog.array', 'goog.dom', 'goog.testing.asserts']);
goog.addDependency('testing/mockmatchers_test.js', ['goog.testing.mockmatchersTest'], ['goog.dom', 'goog.testing.jsunit', 'goog.testing.mockmatchers', 'goog.testing.mockmatchers.ArgumentMatcher']);
goog.addDependency('testing/mockrandom.js', ['goog.testing.MockRandom'], ['goog.Disposable']);
goog.addDependency('testing/mockrandom_test.js', ['goog.testing.MockRandomTest'], ['goog.testing.MockRandom', 'goog.testing.jsunit']);
goog.addDependency('testing/mockrange.js', ['goog.testing.MockRange'], ['goog.dom.AbstractRange', 'goog.testing.LooseMock']);
goog.addDependency('testing/mockrange_test.js', ['goog.testing.MockRangeTest'], ['goog.testing.MockRange', 'goog.testing.jsunit']);
goog.addDependency('testing/mockstorage.js', ['goog.testing.MockStorage'], ['goog.structs.Map']);
goog.addDependency('testing/mockstorage_test.js', ['goog.testing.MockStorageTest'], ['goog.testing.MockStorage', 'goog.testing.jsunit']);
goog.addDependency('testing/mockuseragent.js', ['goog.testing.MockUserAgent'], ['goog.Disposable', 'goog.labs.userAgent.util', 'goog.testing.PropertyReplacer', 'goog.userAgent']);
goog.addDependency('testing/mockuseragent_test.js', ['goog.testing.MockUserAgentTest'], ['goog.dispose', 'goog.testing.MockUserAgent', 'goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('testing/multitestrunner.js', ['goog.testing.MultiTestRunner', 'goog.testing.MultiTestRunner.TestFrame'], ['goog.Timer', 'goog.array', 'goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.events.EventHandler', 'goog.functions', 'goog.string', 'goog.ui.Component', 'goog.ui.ServerChart', 'goog.ui.TableSorter']);
goog.addDependency('testing/net/xhrio.js', ['goog.testing.net.XhrIo'], ['goog.array', 'goog.dom.xml', 'goog.events', 'goog.events.EventTarget', 'goog.json', 'goog.net.ErrorCode', 'goog.net.EventType', 'goog.net.HttpStatus', 'goog.net.XhrIo', 'goog.net.XmlHttp', 'goog.object', 'goog.structs.Map']);
goog.addDependency('testing/net/xhrio_test.js', ['goog.testing.net.XhrIoTest'], ['goog.dom.xml', 'goog.events', 'goog.events.Event', 'goog.net.ErrorCode', 'goog.net.EventType', 'goog.net.XmlHttp', 'goog.object', 'goog.testing.MockControl', 'goog.testing.asserts', 'goog.testing.jsunit', 'goog.testing.mockmatchers.InstanceOf', 'goog.testing.net.XhrIo']);
goog.addDependency('testing/net/xhriopool.js', ['goog.testing.net.XhrIoPool'], ['goog.net.XhrIoPool', 'goog.testing.net.XhrIo']);
goog.addDependency('testing/objectpropertystring.js', ['goog.testing.ObjectPropertyString'], []);
goog.addDependency('testing/performancetable.js', ['goog.testing.PerformanceTable'], ['goog.dom', 'goog.testing.PerformanceTimer']);
goog.addDependency('testing/performancetimer.js', ['goog.testing.PerformanceTimer', 'goog.testing.PerformanceTimer.Task'], ['goog.array', 'goog.async.Deferred', 'goog.math']);
goog.addDependency('testing/performancetimer_test.js', ['goog.testing.PerformanceTimerTest'], ['goog.async.Deferred', 'goog.dom', 'goog.math', 'goog.testing.MockClock', 'goog.testing.PerformanceTimer', 'goog.testing.jsunit']);
goog.addDependency('testing/propertyreplacer.js', ['goog.testing.PropertyReplacer'], ['goog.testing.ObjectPropertyString', 'goog.userAgent']);
goog.addDependency('testing/propertyreplacer_test.js', ['goog.testing.PropertyReplacerTest'], ['goog.testing.PropertyReplacer', 'goog.testing.asserts', 'goog.testing.jsunit']);
goog.addDependency('testing/proto2/proto2.js', ['goog.testing.proto2'], ['goog.proto2.Message', 'goog.proto2.ObjectSerializer', 'goog.testing.asserts']);
goog.addDependency('testing/proto2/proto2_test.js', ['goog.testing.proto2Test'], ['goog.testing.jsunit', 'goog.testing.proto2', 'proto2.TestAllTypes']);
goog.addDependency('testing/pseudorandom.js', ['goog.testing.PseudoRandom'], ['goog.Disposable']);
goog.addDependency('testing/pseudorandom_test.js', ['goog.testing.PseudoRandomTest'], ['goog.testing.PseudoRandom', 'goog.testing.jsunit']);
goog.addDependency('testing/recordfunction.js', ['goog.testing.FunctionCall', 'goog.testing.recordConstructor', 'goog.testing.recordFunction'], ['goog.testing.asserts']);
goog.addDependency('testing/recordfunction_test.js', ['goog.testing.recordFunctionTest'], ['goog.functions', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.testing.recordConstructor', 'goog.testing.recordFunction']);
goog.addDependency('testing/shardingtestcase.js', ['goog.testing.ShardingTestCase'], ['goog.asserts', 'goog.testing.TestCase']);
goog.addDependency('testing/shardingtestcase_test.js', ['goog.testing.ShardingTestCaseTest'], ['goog.testing.ShardingTestCase', 'goog.testing.TestCase', 'goog.testing.asserts', 'goog.testing.jsunit']);
goog.addDependency('testing/singleton.js', ['goog.testing.singleton'], []);
goog.addDependency('testing/singleton_test.js', ['goog.testing.singletonTest'], ['goog.testing.asserts', 'goog.testing.jsunit', 'goog.testing.singleton']);
goog.addDependency('testing/stacktrace.js', ['goog.testing.stacktrace', 'goog.testing.stacktrace.Frame'], []);
goog.addDependency('testing/stacktrace_test.js', ['goog.testing.stacktraceTest'], ['goog.functions', 'goog.string', 'goog.testing.ExpectedFailures', 'goog.testing.PropertyReplacer', 'goog.testing.StrictMock', 'goog.testing.asserts', 'goog.testing.jsunit', 'goog.testing.stacktrace', 'goog.testing.stacktrace.Frame', 'goog.userAgent']);
goog.addDependency('testing/storage/fakemechanism.js', ['goog.testing.storage.FakeMechanism'], ['goog.storage.mechanism.IterableMechanism', 'goog.structs.Map']);
goog.addDependency('testing/strictmock.js', ['goog.testing.StrictMock'], ['goog.array', 'goog.testing.Mock']);
goog.addDependency('testing/strictmock_test.js', ['goog.testing.StrictMockTest'], ['goog.testing.StrictMock', 'goog.testing.jsunit']);
goog.addDependency('testing/style/layoutasserts.js', ['goog.testing.style.layoutasserts'], ['goog.style', 'goog.testing.asserts', 'goog.testing.style']);
goog.addDependency('testing/style/layoutasserts_test.js', ['goog.testing.style.layoutassertsTest'], ['goog.dom', 'goog.style', 'goog.testing.jsunit', 'goog.testing.style.layoutasserts']);
goog.addDependency('testing/style/style.js', ['goog.testing.style'], ['goog.dom', 'goog.math.Rect', 'goog.style']);
goog.addDependency('testing/style/style_test.js', ['goog.testing.styleTest'], ['goog.dom', 'goog.style', 'goog.testing.jsunit', 'goog.testing.style']);
goog.addDependency('testing/testcase.js', ['goog.testing.TestCase', 'goog.testing.TestCase.Error', 'goog.testing.TestCase.Order', 'goog.testing.TestCase.Result', 'goog.testing.TestCase.Test'], ['goog.object', 'goog.testing.asserts', 'goog.testing.stacktrace']);
goog.addDependency('testing/testqueue.js', ['goog.testing.TestQueue'], []);
goog.addDependency('testing/testrunner.js', ['goog.testing.TestRunner'], ['goog.testing.TestCase']);
goog.addDependency('testing/ui/rendererasserts.js', ['goog.testing.ui.rendererasserts'], ['goog.testing.asserts']);
goog.addDependency('testing/ui/rendererasserts_test.js', ['goog.testing.ui.rendererassertsTest'], ['goog.testing.asserts', 'goog.testing.jsunit', 'goog.testing.ui.rendererasserts', 'goog.ui.ControlRenderer']);
goog.addDependency('testing/ui/rendererharness.js', ['goog.testing.ui.RendererHarness'], ['goog.Disposable', 'goog.dom.NodeType', 'goog.testing.asserts', 'goog.testing.dom']);
goog.addDependency('testing/ui/style.js', ['goog.testing.ui.style'], ['goog.array', 'goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.testing.asserts']);
goog.addDependency('testing/ui/style_test.js', ['goog.testing.ui.styleTest'], ['goog.dom', 'goog.testing.jsunit', 'goog.testing.ui.style']);
goog.addDependency('testing/watchers.js', ['goog.testing.watchers'], []);
goog.addDependency('timer/timer.js', ['goog.Timer'], ['goog.events.EventTarget']);
goog.addDependency('timer/timer_test.js', ['goog.TimerTest'], ['goog.Timer', 'goog.events', 'goog.testing.MockClock', 'goog.testing.jsunit']);
goog.addDependency('tweak/entries.js', ['goog.tweak.BaseEntry', 'goog.tweak.BasePrimitiveSetting', 'goog.tweak.BaseSetting', 'goog.tweak.BooleanGroup', 'goog.tweak.BooleanInGroupSetting', 'goog.tweak.BooleanSetting', 'goog.tweak.ButtonAction', 'goog.tweak.NumericSetting', 'goog.tweak.StringSetting'], ['goog.array', 'goog.asserts', 'goog.log', 'goog.object']);
goog.addDependency('tweak/entries_test.js', ['goog.tweak.BaseEntryTest'], ['goog.testing.MockControl', 'goog.testing.jsunit', 'goog.tweak.testhelpers']);
goog.addDependency('tweak/registry.js', ['goog.tweak.Registry'], ['goog.asserts', 'goog.log', 'goog.object', 'goog.string', 'goog.tweak.BaseEntry', 'goog.uri.utils']);
goog.addDependency('tweak/registry_test.js', ['goog.tweak.RegistryTest'], ['goog.asserts.AssertionError', 'goog.testing.jsunit', 'goog.tweak', 'goog.tweak.testhelpers']);
goog.addDependency('tweak/testhelpers.js', ['goog.tweak.testhelpers'], ['goog.tweak', 'goog.tweak.BooleanGroup', 'goog.tweak.BooleanInGroupSetting', 'goog.tweak.BooleanSetting', 'goog.tweak.ButtonAction', 'goog.tweak.NumericSetting', 'goog.tweak.Registry', 'goog.tweak.StringSetting']);
goog.addDependency('tweak/tweak.js', ['goog.tweak', 'goog.tweak.ConfigParams'], ['goog.asserts', 'goog.tweak.BaseSetting', 'goog.tweak.BooleanGroup', 'goog.tweak.BooleanInGroupSetting', 'goog.tweak.BooleanSetting', 'goog.tweak.ButtonAction', 'goog.tweak.NumericSetting', 'goog.tweak.Registry', 'goog.tweak.StringSetting']);
goog.addDependency('tweak/tweakui.js', ['goog.tweak.EntriesPanel', 'goog.tweak.TweakUi'], ['goog.array', 'goog.asserts', 'goog.dom.DomHelper', 'goog.object', 'goog.style', 'goog.tweak', 'goog.ui.Zippy', 'goog.userAgent']);
goog.addDependency('tweak/tweakui_test.js', ['goog.tweak.TweakUiTest'], ['goog.dom', 'goog.string', 'goog.testing.jsunit', 'goog.tweak', 'goog.tweak.TweakUi', 'goog.tweak.testhelpers']);
goog.addDependency('ui/abstractspellchecker.js', ['goog.ui.AbstractSpellChecker', 'goog.ui.AbstractSpellChecker.AsyncResult'], ['goog.a11y.aria', 'goog.array', 'goog.asserts', 'goog.dom', 'goog.dom.NodeType', 'goog.dom.classlist', 'goog.dom.selection', 'goog.events', 'goog.events.Event', 'goog.events.EventType', 'goog.math.Coordinate', 'goog.spell.SpellCheck', 'goog.structs.Set', 'goog.style', 'goog.ui.Component', 'goog.ui.MenuItem', 'goog.ui.MenuSeparator', 'goog.ui.PopupMenu']);
goog.addDependency('ui/ac/ac.js', ['goog.ui.ac'], ['goog.ui.ac.ArrayMatcher', 'goog.ui.ac.AutoComplete', 'goog.ui.ac.InputHandler', 'goog.ui.ac.Renderer']);
goog.addDependency('ui/ac/arraymatcher.js', ['goog.ui.ac.ArrayMatcher'], ['goog.string']);
goog.addDependency('ui/ac/autocomplete.js', ['goog.ui.ac.AutoComplete', 'goog.ui.ac.AutoComplete.EventType'], ['goog.array', 'goog.asserts', 'goog.events', 'goog.events.EventTarget', 'goog.object']);
goog.addDependency('ui/ac/cachingmatcher.js', ['goog.ui.ac.CachingMatcher'], ['goog.array', 'goog.async.Throttle', 'goog.ui.ac.ArrayMatcher', 'goog.ui.ac.RenderOptions']);
goog.addDependency('ui/ac/inputhandler.js', ['goog.ui.ac.InputHandler'], ['goog.Disposable', 'goog.Timer', 'goog.a11y.aria', 'goog.dom', 'goog.dom.selection', 'goog.events.EventHandler', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.events.KeyHandler', 'goog.string', 'goog.userAgent', 'goog.userAgent.product']);
goog.addDependency('ui/ac/remote.js', ['goog.ui.ac.Remote'], ['goog.ui.ac.AutoComplete', 'goog.ui.ac.InputHandler', 'goog.ui.ac.RemoteArrayMatcher', 'goog.ui.ac.Renderer']);
goog.addDependency('ui/ac/remotearraymatcher.js', ['goog.ui.ac.RemoteArrayMatcher'], ['goog.Disposable', 'goog.Uri', 'goog.events', 'goog.json', 'goog.net.EventType', 'goog.net.XhrIo']);
goog.addDependency('ui/ac/renderer.js', ['goog.ui.ac.Renderer', 'goog.ui.ac.Renderer.CustomRenderer'], ['goog.a11y.aria', 'goog.a11y.aria.Role', 'goog.a11y.aria.State', 'goog.array', 'goog.asserts', 'goog.dispose', 'goog.dom', 'goog.dom.NodeType', 'goog.dom.classlist', 'goog.events', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.fx.dom.FadeInAndShow', 'goog.fx.dom.FadeOutAndHide', 'goog.positioning', 'goog.positioning.Corner', 'goog.positioning.Overflow', 'goog.string', 'goog.style', 'goog.ui.IdGenerator', 'goog.ui.ac.AutoComplete']);
goog.addDependency('ui/ac/renderoptions.js', ['goog.ui.ac.RenderOptions'], []);
goog.addDependency('ui/ac/richinputhandler.js', ['goog.ui.ac.RichInputHandler'], ['goog.ui.ac.InputHandler']);
goog.addDependency('ui/ac/richremote.js', ['goog.ui.ac.RichRemote'], ['goog.ui.ac.AutoComplete', 'goog.ui.ac.Remote', 'goog.ui.ac.Renderer', 'goog.ui.ac.RichInputHandler', 'goog.ui.ac.RichRemoteArrayMatcher']);
goog.addDependency('ui/ac/richremotearraymatcher.js', ['goog.ui.ac.RichRemoteArrayMatcher'], ['goog.json', 'goog.ui.ac.RemoteArrayMatcher']);
goog.addDependency('ui/activitymonitor.js', ['goog.ui.ActivityMonitor'], ['goog.array', 'goog.asserts', 'goog.dom', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.events.EventType']);
goog.addDependency('ui/advancedtooltip.js', ['goog.ui.AdvancedTooltip'], ['goog.events', 'goog.events.EventType', 'goog.math.Box', 'goog.math.Coordinate', 'goog.style', 'goog.ui.Tooltip', 'goog.userAgent']);
goog.addDependency('ui/animatedzippy.js', ['goog.ui.AnimatedZippy'], ['goog.dom', 'goog.events', 'goog.fx.Animation', 'goog.fx.Transition', 'goog.fx.easing', 'goog.ui.Zippy', 'goog.ui.ZippyEvent']);
goog.addDependency('ui/attachablemenu.js', ['goog.ui.AttachableMenu'], ['goog.a11y.aria', 'goog.a11y.aria.State', 'goog.array', 'goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.events.Event', 'goog.events.KeyCodes', 'goog.string', 'goog.style', 'goog.ui.ItemEvent', 'goog.ui.MenuBase', 'goog.ui.PopupBase', 'goog.userAgent']);
goog.addDependency('ui/bidiinput.js', ['goog.ui.BidiInput'], ['goog.dom', 'goog.events', 'goog.events.InputHandler', 'goog.i18n.bidi', 'goog.ui.Component']);
goog.addDependency('ui/bubble.js', ['goog.ui.Bubble'], ['goog.Timer', 'goog.events', 'goog.events.EventType', 'goog.math.Box', 'goog.positioning', 'goog.positioning.AbsolutePosition', 'goog.positioning.AnchoredPosition', 'goog.positioning.Corner', 'goog.positioning.CornerBit', 'goog.style', 'goog.ui.Component', 'goog.ui.Popup']);
goog.addDependency('ui/button.js', ['goog.ui.Button', 'goog.ui.Button.Side'], ['goog.events.EventType', 'goog.events.KeyCodes', 'goog.events.KeyHandler', 'goog.ui.ButtonRenderer', 'goog.ui.ButtonSide', 'goog.ui.Component', 'goog.ui.Control', 'goog.ui.NativeButtonRenderer', 'goog.ui.registry']);
goog.addDependency('ui/buttonrenderer.js', ['goog.ui.ButtonRenderer'], ['goog.a11y.aria', 'goog.a11y.aria.Role', 'goog.a11y.aria.State', 'goog.asserts', 'goog.ui.ButtonSide', 'goog.ui.Component', 'goog.ui.ControlRenderer']);
goog.addDependency('ui/buttonside.js', ['goog.ui.ButtonSide'], []);
goog.addDependency('ui/charcounter.js', ['goog.ui.CharCounter', 'goog.ui.CharCounter.Display'], ['goog.dom', 'goog.events', 'goog.events.EventTarget', 'goog.events.InputHandler']);
goog.addDependency('ui/charpicker.js', ['goog.ui.CharPicker'], ['goog.a11y.aria', 'goog.a11y.aria.State', 'goog.array', 'goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.events', 'goog.events.Event', 'goog.events.EventHandler', 'goog.events.EventType', 'goog.events.InputHandler', 'goog.events.KeyCodes', 'goog.events.KeyHandler', 'goog.i18n.CharListDecompressor', 'goog.i18n.uChar', 'goog.structs.Set', 'goog.style', 'goog.ui.Button', 'goog.ui.Component', 'goog.ui.ContainerScroller', 'goog.ui.FlatButtonRenderer', 'goog.ui.HoverCard', 'goog.ui.LabelInput', 'goog.ui.Menu', 'goog.ui.MenuButton', 'goog.ui.MenuItem', 'goog.ui.Tooltip']);
goog.addDependency('ui/checkbox.js', ['goog.ui.Checkbox', 'goog.ui.Checkbox.State'], ['goog.a11y.aria', 'goog.a11y.aria.State', 'goog.asserts', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.ui.CheckboxRenderer', 'goog.ui.Component.EventType', 'goog.ui.Component.State', 'goog.ui.Control', 'goog.ui.registry']);
goog.addDependency('ui/checkboxmenuitem.js', ['goog.ui.CheckBoxMenuItem'], ['goog.ui.MenuItem', 'goog.ui.registry']);
goog.addDependency('ui/checkboxrenderer.js', ['goog.ui.CheckboxRenderer'], ['goog.a11y.aria', 'goog.a11y.aria.Role', 'goog.a11y.aria.State', 'goog.array', 'goog.asserts', 'goog.dom.classlist', 'goog.object', 'goog.ui.ControlRenderer']);
goog.addDependency('ui/colorbutton.js', ['goog.ui.ColorButton'], ['goog.ui.Button', 'goog.ui.ColorButtonRenderer', 'goog.ui.registry']);
goog.addDependency('ui/colorbuttonrenderer.js', ['goog.ui.ColorButtonRenderer'], ['goog.asserts', 'goog.dom.classlist', 'goog.functions', 'goog.ui.ColorMenuButtonRenderer']);
goog.addDependency('ui/colormenubutton.js', ['goog.ui.ColorMenuButton'], ['goog.array', 'goog.object', 'goog.ui.ColorMenuButtonRenderer', 'goog.ui.ColorPalette', 'goog.ui.Component', 'goog.ui.Menu', 'goog.ui.MenuButton', 'goog.ui.registry']);
goog.addDependency('ui/colormenubuttonrenderer.js', ['goog.ui.ColorMenuButtonRenderer'], ['goog.asserts', 'goog.color', 'goog.dom.classlist', 'goog.ui.MenuButtonRenderer', 'goog.userAgent']);
goog.addDependency('ui/colorpalette.js', ['goog.ui.ColorPalette'], ['goog.array', 'goog.color', 'goog.style', 'goog.ui.Palette', 'goog.ui.PaletteRenderer']);
goog.addDependency('ui/colorpicker.js', ['goog.ui.ColorPicker', 'goog.ui.ColorPicker.EventType'], ['goog.ui.ColorPalette', 'goog.ui.Component']);
goog.addDependency('ui/colorsplitbehavior.js', ['goog.ui.ColorSplitBehavior'], ['goog.ui.ColorMenuButton', 'goog.ui.SplitBehavior']);
goog.addDependency('ui/combobox.js', ['goog.ui.ComboBox', 'goog.ui.ComboBoxItem'], ['goog.Timer', 'goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.events.EventType', 'goog.events.InputHandler', 'goog.events.KeyCodes', 'goog.events.KeyHandler', 'goog.log', 'goog.positioning.Corner', 'goog.positioning.MenuAnchoredPosition', 'goog.string', 'goog.style', 'goog.ui.Component', 'goog.ui.ItemEvent', 'goog.ui.LabelInput', 'goog.ui.Menu', 'goog.ui.MenuItem', 'goog.ui.MenuSeparator', 'goog.ui.registry', 'goog.userAgent']);
goog.addDependency('ui/component.js', ['goog.ui.Component', 'goog.ui.Component.Error', 'goog.ui.Component.EventType', 'goog.ui.Component.State'], ['goog.array', 'goog.asserts', 'goog.dom', 'goog.dom.NodeType', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.object', 'goog.style', 'goog.ui.IdGenerator']);
goog.addDependency('ui/container.js', ['goog.ui.Container', 'goog.ui.Container.EventType', 'goog.ui.Container.Orientation'], ['goog.a11y.aria', 'goog.a11y.aria.State', 'goog.asserts', 'goog.dom', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.events.KeyHandler', 'goog.object', 'goog.style', 'goog.ui.Component', 'goog.ui.ContainerRenderer', 'goog.ui.Control']);
goog.addDependency('ui/containerrenderer.js', ['goog.ui.ContainerRenderer'], ['goog.a11y.aria', 'goog.array', 'goog.asserts', 'goog.dom.NodeType', 'goog.dom.classlist', 'goog.string', 'goog.style', 'goog.ui.registry', 'goog.userAgent']);
goog.addDependency('ui/containerscroller.js', ['goog.ui.ContainerScroller'], ['goog.Disposable', 'goog.Timer', 'goog.events.EventHandler', 'goog.style', 'goog.ui.Component', 'goog.ui.Container']);
goog.addDependency('ui/control.js', ['goog.ui.Control'], ['goog.array', 'goog.dom', 'goog.events.Event', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.events.KeyHandler', 'goog.string', 'goog.ui.Component', 'goog.ui.ControlContent', 'goog.ui.ControlRenderer', 'goog.ui.decorate', 'goog.ui.registry', 'goog.userAgent']);
goog.addDependency('ui/controlcontent.js', ['goog.ui.ControlContent'], []);
goog.addDependency('ui/controlrenderer.js', ['goog.ui.ControlRenderer'], ['goog.a11y.aria', 'goog.a11y.aria.Role', 'goog.a11y.aria.State', 'goog.array', 'goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.object', 'goog.string', 'goog.style', 'goog.ui.Component', 'goog.userAgent']);
goog.addDependency('ui/cookieeditor.js', ['goog.ui.CookieEditor'], ['goog.asserts', 'goog.dom', 'goog.dom.TagName', 'goog.events.EventType', 'goog.net.cookies', 'goog.string', 'goog.style', 'goog.ui.Component']);
goog.addDependency('ui/css3buttonrenderer.js', ['goog.ui.Css3ButtonRenderer'], ['goog.asserts', 'goog.dom.TagName', 'goog.dom.classlist', 'goog.ui.Button', 'goog.ui.ButtonRenderer', 'goog.ui.Component', 'goog.ui.INLINE_BLOCK_CLASSNAME', 'goog.ui.registry']);
goog.addDependency('ui/css3menubuttonrenderer.js', ['goog.ui.Css3MenuButtonRenderer'], ['goog.dom', 'goog.dom.TagName', 'goog.ui.INLINE_BLOCK_CLASSNAME', 'goog.ui.MenuButton', 'goog.ui.MenuButtonRenderer', 'goog.ui.registry']);
goog.addDependency('ui/cssnames.js', ['goog.ui.INLINE_BLOCK_CLASSNAME'], []);
goog.addDependency('ui/custombutton.js', ['goog.ui.CustomButton'], ['goog.ui.Button', 'goog.ui.CustomButtonRenderer', 'goog.ui.registry']);
goog.addDependency('ui/custombuttonrenderer.js', ['goog.ui.CustomButtonRenderer'], ['goog.a11y.aria.Role', 'goog.asserts', 'goog.dom.NodeType', 'goog.dom.classlist', 'goog.string', 'goog.ui.ButtonRenderer', 'goog.ui.INLINE_BLOCK_CLASSNAME']);
goog.addDependency('ui/customcolorpalette.js', ['goog.ui.CustomColorPalette'], ['goog.color', 'goog.dom', 'goog.dom.classlist', 'goog.ui.ColorPalette', 'goog.ui.Component']);
goog.addDependency('ui/datepicker.js', ['goog.ui.DatePicker', 'goog.ui.DatePicker.Events', 'goog.ui.DatePickerEvent'], ['goog.a11y.aria', 'goog.asserts', 'goog.date.Date', 'goog.date.DateRange', 'goog.date.Interval', 'goog.dom', 'goog.dom.NodeType', 'goog.dom.classlist', 'goog.events.Event', 'goog.events.EventType', 'goog.events.KeyHandler', 'goog.i18n.DateTimeFormat', 'goog.i18n.DateTimePatterns', 'goog.i18n.DateTimeSymbols', 'goog.style', 'goog.ui.Component', 'goog.ui.DefaultDatePickerRenderer', 'goog.ui.IdGenerator']);
goog.addDependency('ui/datepickerrenderer.js', ['goog.ui.DatePickerRenderer'], []);
goog.addDependency('ui/decorate.js', ['goog.ui.decorate'], ['goog.ui.registry']);
goog.addDependency('ui/defaultdatepickerrenderer.js', ['goog.ui.DefaultDatePickerRenderer'], ['goog.dom', 'goog.dom.TagName', 'goog.ui.DatePickerRenderer']);
goog.addDependency('ui/dialog.js', ['goog.ui.Dialog', 'goog.ui.Dialog.ButtonSet', 'goog.ui.Dialog.ButtonSet.DefaultButtons', 'goog.ui.Dialog.DefaultButtonCaptions', 'goog.ui.Dialog.DefaultButtonKeys', 'goog.ui.Dialog.Event', 'goog.ui.Dialog.EventType'], ['goog.a11y.aria', 'goog.a11y.aria.Role', 'goog.a11y.aria.State', 'goog.asserts', 'goog.dom', 'goog.dom.NodeType', 'goog.dom.TagName', 'goog.dom.classlist', 'goog.dom.safe', 'goog.events', 'goog.events.Event', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.fx.Dragger', 'goog.html.SafeHtml', 'goog.html.legacyconversions', 'goog.math.Rect', 'goog.string', 'goog.structs.Map', 'goog.style', 'goog.ui.ModalPopup']);
goog.addDependency('ui/dimensionpicker.js', ['goog.ui.DimensionPicker'], ['goog.events.EventType', 'goog.events.KeyCodes', 'goog.math.Size', 'goog.ui.Component', 'goog.ui.Control', 'goog.ui.DimensionPickerRenderer', 'goog.ui.registry']);
goog.addDependency('ui/dimensionpickerrenderer.js', ['goog.ui.DimensionPickerRenderer'], ['goog.a11y.aria.Announcer', 'goog.a11y.aria.LivePriority', 'goog.dom', 'goog.dom.TagName', 'goog.i18n.bidi', 'goog.style', 'goog.ui.ControlRenderer', 'goog.userAgent']);
goog.addDependency('ui/dragdropdetector.js', ['goog.ui.DragDropDetector', 'goog.ui.DragDropDetector.EventType', 'goog.ui.DragDropDetector.ImageDropEvent', 'goog.ui.DragDropDetector.LinkDropEvent'], ['goog.dom', 'goog.dom.TagName', 'goog.events.Event', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.math.Coordinate', 'goog.string', 'goog.style', 'goog.userAgent']);
goog.addDependency('ui/drilldownrow.js', ['goog.ui.DrilldownRow'], ['goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.ui.Component']);
goog.addDependency('ui/editor/abstractdialog.js', ['goog.ui.editor.AbstractDialog', 'goog.ui.editor.AbstractDialog.Builder', 'goog.ui.editor.AbstractDialog.EventType'], ['goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.events.EventTarget', 'goog.string', 'goog.ui.Dialog']);
goog.addDependency('ui/editor/bubble.js', ['goog.ui.editor.Bubble'], ['goog.asserts', 'goog.dom', 'goog.dom.TagName', 'goog.dom.ViewportSizeMonitor', 'goog.dom.classlist', 'goog.editor.style', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.functions', 'goog.log', 'goog.math.Box', 'goog.object', 'goog.positioning', 'goog.positioning.Corner', 'goog.positioning.Overflow', 'goog.positioning.OverflowStatus', 'goog.string', 'goog.style', 'goog.ui.Component', 'goog.ui.PopupBase', 'goog.userAgent']);
goog.addDependency('ui/editor/defaulttoolbar.js', ['goog.ui.editor.ButtonDescriptor', 'goog.ui.editor.DefaultToolbar'], ['goog.asserts', 'goog.dom', 'goog.dom.TagName', 'goog.dom.classlist', 'goog.editor.Command', 'goog.style', 'goog.ui.editor.ToolbarFactory', 'goog.ui.editor.messages', 'goog.userAgent']);
goog.addDependency('ui/editor/equationeditordialog.js', ['goog.ui.editor.EquationEditorDialog'], ['goog.editor.Command', 'goog.ui.Dialog', 'goog.ui.editor.AbstractDialog', 'goog.ui.editor.EquationEditorOkEvent', 'goog.ui.equation.TexEditor']);
goog.addDependency('ui/editor/equationeditorokevent.js', ['goog.ui.editor.EquationEditorOkEvent'], ['goog.events.Event', 'goog.ui.editor.AbstractDialog']);
goog.addDependency('ui/editor/linkdialog.js', ['goog.ui.editor.LinkDialog', 'goog.ui.editor.LinkDialog.BeforeTestLinkEvent', 'goog.ui.editor.LinkDialog.EventType', 'goog.ui.editor.LinkDialog.OkEvent'], ['goog.dom', 'goog.dom.TagName', 'goog.dom.safe', 'goog.editor.BrowserFeature', 'goog.editor.Link', 'goog.editor.focus', 'goog.editor.node', 'goog.events.Event', 'goog.events.EventHandler', 'goog.events.EventType', 'goog.events.InputHandler', 'goog.html.SafeHtml', 'goog.string', 'goog.string.Unicode', 'goog.style', 'goog.ui.Button', 'goog.ui.Component', 'goog.ui.LinkButtonRenderer', 'goog.ui.editor.AbstractDialog', 'goog.ui.editor.TabPane', 'goog.ui.editor.messages', 'goog.userAgent', 'goog.window']);
goog.addDependency('ui/editor/messages.js', ['goog.ui.editor.messages'], ['goog.html.uncheckedconversions', 'goog.string.Const']);
goog.addDependency('ui/editor/tabpane.js', ['goog.ui.editor.TabPane'], ['goog.asserts', 'goog.dom.TagName', 'goog.dom.classlist', 'goog.events.EventHandler', 'goog.events.EventType', 'goog.style', 'goog.ui.Component', 'goog.ui.Control', 'goog.ui.Tab', 'goog.ui.TabBar']);
goog.addDependency('ui/editor/toolbarcontroller.js', ['goog.ui.editor.ToolbarController'], ['goog.editor.Field', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.ui.Component']);
goog.addDependency('ui/editor/toolbarfactory.js', ['goog.ui.editor.ToolbarFactory'], ['goog.array', 'goog.dom', 'goog.dom.TagName', 'goog.string', 'goog.string.Unicode', 'goog.style', 'goog.ui.Component', 'goog.ui.Container', 'goog.ui.Option', 'goog.ui.Toolbar', 'goog.ui.ToolbarButton', 'goog.ui.ToolbarColorMenuButton', 'goog.ui.ToolbarMenuButton', 'goog.ui.ToolbarRenderer', 'goog.ui.ToolbarSelect', 'goog.userAgent']);
goog.addDependency('ui/emoji/emoji.js', ['goog.ui.emoji.Emoji'], []);
goog.addDependency('ui/emoji/emojipalette.js', ['goog.ui.emoji.EmojiPalette'], ['goog.events.EventType', 'goog.net.ImageLoader', 'goog.ui.Palette', 'goog.ui.emoji.Emoji', 'goog.ui.emoji.EmojiPaletteRenderer']);
goog.addDependency('ui/emoji/emojipaletterenderer.js', ['goog.ui.emoji.EmojiPaletteRenderer'], ['goog.a11y.aria', 'goog.asserts', 'goog.dom.NodeType', 'goog.dom.classlist', 'goog.style', 'goog.ui.PaletteRenderer', 'goog.ui.emoji.Emoji']);
goog.addDependency('ui/emoji/emojipicker.js', ['goog.ui.emoji.EmojiPicker'], ['goog.log', 'goog.style', 'goog.ui.Component', 'goog.ui.TabPane', 'goog.ui.emoji.Emoji', 'goog.ui.emoji.EmojiPalette', 'goog.ui.emoji.EmojiPaletteRenderer', 'goog.ui.emoji.ProgressiveEmojiPaletteRenderer']);
goog.addDependency('ui/emoji/popupemojipicker.js', ['goog.ui.emoji.PopupEmojiPicker'], ['goog.events.EventType', 'goog.positioning.AnchoredPosition', 'goog.positioning.Corner', 'goog.ui.Component', 'goog.ui.Popup', 'goog.ui.emoji.EmojiPicker']);
goog.addDependency('ui/emoji/progressiveemojipaletterenderer.js', ['goog.ui.emoji.ProgressiveEmojiPaletteRenderer'], ['goog.style', 'goog.ui.emoji.EmojiPaletteRenderer']);
goog.addDependency('ui/emoji/spriteinfo.js', ['goog.ui.emoji.SpriteInfo'], []);
goog.addDependency('ui/equation/arrowpalette.js', ['goog.ui.equation.ArrowPalette'], ['goog.math.Size', 'goog.ui.equation.Palette']);
goog.addDependency('ui/equation/changeevent.js', ['goog.ui.equation.ChangeEvent'], ['goog.events.Event']);
goog.addDependency('ui/equation/comparisonpalette.js', ['goog.ui.equation.ComparisonPalette'], ['goog.math.Size', 'goog.ui.equation.Palette']);
goog.addDependency('ui/equation/editorpane.js', ['goog.ui.equation.EditorPane'], ['goog.style', 'goog.ui.Component']);
goog.addDependency('ui/equation/equationeditor.js', ['goog.ui.equation.EquationEditor'], ['goog.events', 'goog.ui.Component', 'goog.ui.TabBar', 'goog.ui.equation.ImageRenderer', 'goog.ui.equation.TexPane']);
goog.addDependency('ui/equation/equationeditordialog.js', ['goog.ui.equation.EquationEditorDialog'], ['goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.ui.Dialog', 'goog.ui.equation.EquationEditor', 'goog.ui.equation.PaletteManager', 'goog.ui.equation.TexEditor']);
goog.addDependency('ui/equation/greekpalette.js', ['goog.ui.equation.GreekPalette'], ['goog.math.Size', 'goog.ui.equation.Palette']);
goog.addDependency('ui/equation/imagerenderer.js', ['goog.ui.equation.ImageRenderer'], ['goog.asserts', 'goog.dom.TagName', 'goog.dom.classlist', 'goog.string', 'goog.uri.utils']);
goog.addDependency('ui/equation/mathpalette.js', ['goog.ui.equation.MathPalette'], ['goog.math.Size', 'goog.ui.equation.Palette']);
goog.addDependency('ui/equation/menupalette.js', ['goog.ui.equation.MenuPalette', 'goog.ui.equation.MenuPaletteRenderer'], ['goog.math.Size', 'goog.ui.PaletteRenderer', 'goog.ui.equation.Palette', 'goog.ui.equation.PaletteRenderer']);
goog.addDependency('ui/equation/palette.js', ['goog.ui.equation.Palette', 'goog.ui.equation.PaletteEvent', 'goog.ui.equation.PaletteRenderer'], ['goog.dom', 'goog.dom.TagName', 'goog.events.Event', 'goog.ui.Palette', 'goog.ui.PaletteRenderer']);
goog.addDependency('ui/equation/palettemanager.js', ['goog.ui.equation.PaletteManager'], ['goog.Timer', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.ui.equation.ArrowPalette', 'goog.ui.equation.ComparisonPalette', 'goog.ui.equation.GreekPalette', 'goog.ui.equation.MathPalette', 'goog.ui.equation.MenuPalette', 'goog.ui.equation.Palette', 'goog.ui.equation.SymbolPalette']);
goog.addDependency('ui/equation/symbolpalette.js', ['goog.ui.equation.SymbolPalette'], ['goog.math.Size', 'goog.ui.equation.Palette']);
goog.addDependency('ui/equation/texeditor.js', ['goog.ui.equation.TexEditor'], ['goog.ui.Component', 'goog.ui.equation.ImageRenderer', 'goog.ui.equation.TexPane']);
goog.addDependency('ui/equation/texpane.js', ['goog.ui.equation.TexPane'], ['goog.Timer', 'goog.dom', 'goog.dom.TagName', 'goog.dom.selection', 'goog.events', 'goog.events.EventType', 'goog.events.InputHandler', 'goog.style', 'goog.ui.equation.ChangeEvent', 'goog.ui.equation.EditorPane', 'goog.ui.equation.ImageRenderer', 'goog.ui.equation.Palette', 'goog.ui.equation.PaletteEvent']);
goog.addDependency('ui/filteredmenu.js', ['goog.ui.FilteredMenu'], ['goog.a11y.aria', 'goog.a11y.aria.AutoCompleteValues', 'goog.a11y.aria.State', 'goog.dom', 'goog.events', 'goog.events.EventType', 'goog.events.InputHandler', 'goog.events.KeyCodes', 'goog.object', 'goog.string', 'goog.style', 'goog.ui.Component', 'goog.ui.FilterObservingMenuItem', 'goog.ui.Menu', 'goog.ui.MenuItem', 'goog.userAgent']);
goog.addDependency('ui/filterobservingmenuitem.js', ['goog.ui.FilterObservingMenuItem'], ['goog.ui.FilterObservingMenuItemRenderer', 'goog.ui.MenuItem', 'goog.ui.registry']);
goog.addDependency('ui/filterobservingmenuitemrenderer.js', ['goog.ui.FilterObservingMenuItemRenderer'], ['goog.ui.MenuItemRenderer']);
goog.addDependency('ui/flatbuttonrenderer.js', ['goog.ui.FlatButtonRenderer'], ['goog.a11y.aria.Role', 'goog.asserts', 'goog.dom.classlist', 'goog.ui.Button', 'goog.ui.ButtonRenderer', 'goog.ui.INLINE_BLOCK_CLASSNAME', 'goog.ui.registry']);
goog.addDependency('ui/flatmenubuttonrenderer.js', ['goog.ui.FlatMenuButtonRenderer'], ['goog.dom', 'goog.style', 'goog.ui.FlatButtonRenderer', 'goog.ui.INLINE_BLOCK_CLASSNAME', 'goog.ui.Menu', 'goog.ui.MenuButton', 'goog.ui.MenuRenderer', 'goog.ui.registry']);
goog.addDependency('ui/formpost.js', ['goog.ui.FormPost'], ['goog.array', 'goog.dom.TagName', 'goog.dom.safe', 'goog.html.SafeHtml', 'goog.ui.Component']);
goog.addDependency('ui/gauge.js', ['goog.ui.Gauge', 'goog.ui.GaugeColoredRange'], ['goog.a11y.aria', 'goog.asserts', 'goog.events', 'goog.fx.Animation', 'goog.fx.Transition', 'goog.fx.easing', 'goog.graphics', 'goog.graphics.Font', 'goog.graphics.Path', 'goog.graphics.SolidFill', 'goog.math', 'goog.ui.Component', 'goog.ui.GaugeTheme']);
goog.addDependency('ui/gaugetheme.js', ['goog.ui.GaugeTheme'], ['goog.graphics.LinearGradient', 'goog.graphics.SolidFill', 'goog.graphics.Stroke']);
goog.addDependency('ui/hovercard.js', ['goog.ui.HoverCard', 'goog.ui.HoverCard.EventType', 'goog.ui.HoverCard.TriggerEvent'], ['goog.array', 'goog.dom', 'goog.events', 'goog.events.Event', 'goog.events.EventType', 'goog.ui.AdvancedTooltip', 'goog.ui.PopupBase', 'goog.ui.Tooltip']);
goog.addDependency('ui/hsvapalette.js', ['goog.ui.HsvaPalette'], ['goog.array', 'goog.color.alpha', 'goog.dom.TagName', 'goog.events', 'goog.events.EventType', 'goog.style', 'goog.ui.Component', 'goog.ui.HsvPalette']);
goog.addDependency('ui/hsvpalette.js', ['goog.ui.HsvPalette'], ['goog.color', 'goog.dom.TagName', 'goog.events', 'goog.events.EventType', 'goog.events.InputHandler', 'goog.style', 'goog.style.bidi', 'goog.ui.Component', 'goog.userAgent']);
goog.addDependency('ui/idgenerator.js', ['goog.ui.IdGenerator'], []);
goog.addDependency('ui/idletimer.js', ['goog.ui.IdleTimer'], ['goog.Timer', 'goog.events', 'goog.events.EventTarget', 'goog.structs.Set', 'goog.ui.ActivityMonitor']);
goog.addDependency('ui/iframemask.js', ['goog.ui.IframeMask'], ['goog.Disposable', 'goog.Timer', 'goog.dom', 'goog.dom.iframe', 'goog.events.EventHandler', 'goog.style']);
goog.addDependency('ui/imagelessbuttonrenderer.js', ['goog.ui.ImagelessButtonRenderer'], ['goog.dom.classlist', 'goog.ui.Button', 'goog.ui.Component', 'goog.ui.CustomButtonRenderer', 'goog.ui.INLINE_BLOCK_CLASSNAME', 'goog.ui.registry']);
goog.addDependency('ui/imagelessmenubuttonrenderer.js', ['goog.ui.ImagelessMenuButtonRenderer'], ['goog.dom', 'goog.dom.TagName', 'goog.dom.classlist', 'goog.ui.INLINE_BLOCK_CLASSNAME', 'goog.ui.MenuButton', 'goog.ui.MenuButtonRenderer', 'goog.ui.registry']);
goog.addDependency('ui/inputdatepicker.js', ['goog.ui.InputDatePicker'], ['goog.date.DateTime', 'goog.dom', 'goog.string', 'goog.ui.Component', 'goog.ui.DatePicker', 'goog.ui.PopupBase', 'goog.ui.PopupDatePicker']);
goog.addDependency('ui/itemevent.js', ['goog.ui.ItemEvent'], ['goog.events.Event']);
goog.addDependency('ui/keyboardshortcuthandler.js', ['goog.ui.KeyboardShortcutEvent', 'goog.ui.KeyboardShortcutHandler', 'goog.ui.KeyboardShortcutHandler.EventType'], ['goog.Timer', 'goog.events', 'goog.events.Event', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.events.KeyNames', 'goog.object', 'goog.userAgent']);
goog.addDependency('ui/labelinput.js', ['goog.ui.LabelInput'], ['goog.Timer', 'goog.a11y.aria', 'goog.a11y.aria.State', 'goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.events.EventHandler', 'goog.events.EventType', 'goog.ui.Component', 'goog.userAgent']);
goog.addDependency('ui/linkbuttonrenderer.js', ['goog.ui.LinkButtonRenderer'], ['goog.ui.Button', 'goog.ui.FlatButtonRenderer', 'goog.ui.registry']);
goog.addDependency('ui/media/flashobject.js', ['goog.ui.media.FlashObject', 'goog.ui.media.FlashObject.ScriptAccessLevel', 'goog.ui.media.FlashObject.Wmodes'], ['goog.asserts', 'goog.events.Event', 'goog.events.EventHandler', 'goog.events.EventType', 'goog.log', 'goog.object', 'goog.string', 'goog.structs.Map', 'goog.style', 'goog.ui.Component', 'goog.userAgent', 'goog.userAgent.flash']);
goog.addDependency('ui/media/flickr.js', ['goog.ui.media.FlickrSet', 'goog.ui.media.FlickrSetModel'], ['goog.ui.media.FlashObject', 'goog.ui.media.Media', 'goog.ui.media.MediaModel', 'goog.ui.media.MediaRenderer']);
goog.addDependency('ui/media/googlevideo.js', ['goog.ui.media.GoogleVideo', 'goog.ui.media.GoogleVideoModel'], ['goog.string', 'goog.ui.media.FlashObject', 'goog.ui.media.Media', 'goog.ui.media.MediaModel', 'goog.ui.media.MediaRenderer']);
goog.addDependency('ui/media/media.js', ['goog.ui.media.Media', 'goog.ui.media.MediaRenderer'], ['goog.style', 'goog.ui.Component', 'goog.ui.Control', 'goog.ui.ControlRenderer']);
goog.addDependency('ui/media/mediamodel.js', ['goog.ui.media.MediaModel', 'goog.ui.media.MediaModel.Category', 'goog.ui.media.MediaModel.Credit', 'goog.ui.media.MediaModel.Credit.Role', 'goog.ui.media.MediaModel.Credit.Scheme', 'goog.ui.media.MediaModel.Medium', 'goog.ui.media.MediaModel.MimeType', 'goog.ui.media.MediaModel.Player', 'goog.ui.media.MediaModel.SubTitle', 'goog.ui.media.MediaModel.Thumbnail'], ['goog.array']);
goog.addDependency('ui/media/mp3.js', ['goog.ui.media.Mp3'], ['goog.string', 'goog.ui.media.FlashObject', 'goog.ui.media.Media', 'goog.ui.media.MediaRenderer']);
goog.addDependency('ui/media/photo.js', ['goog.ui.media.Photo'], ['goog.ui.media.Media', 'goog.ui.media.MediaRenderer']);
goog.addDependency('ui/media/picasa.js', ['goog.ui.media.PicasaAlbum', 'goog.ui.media.PicasaAlbumModel'], ['goog.ui.media.FlashObject', 'goog.ui.media.Media', 'goog.ui.media.MediaModel', 'goog.ui.media.MediaRenderer']);
goog.addDependency('ui/media/vimeo.js', ['goog.ui.media.Vimeo', 'goog.ui.media.VimeoModel'], ['goog.string', 'goog.ui.media.FlashObject', 'goog.ui.media.Media', 'goog.ui.media.MediaModel', 'goog.ui.media.MediaRenderer']);
goog.addDependency('ui/media/youtube.js', ['goog.ui.media.Youtube', 'goog.ui.media.YoutubeModel'], ['goog.string', 'goog.ui.Component', 'goog.ui.media.FlashObject', 'goog.ui.media.Media', 'goog.ui.media.MediaModel', 'goog.ui.media.MediaRenderer']);
goog.addDependency('ui/menu.js', ['goog.ui.Menu', 'goog.ui.Menu.EventType'], ['goog.math.Coordinate', 'goog.string', 'goog.style', 'goog.ui.Component.EventType', 'goog.ui.Component.State', 'goog.ui.Container', 'goog.ui.Container.Orientation', 'goog.ui.MenuHeader', 'goog.ui.MenuItem', 'goog.ui.MenuRenderer', 'goog.ui.MenuSeparator']);
goog.addDependency('ui/menubar.js', ['goog.ui.menuBar'], ['goog.ui.Container', 'goog.ui.MenuBarRenderer']);
goog.addDependency('ui/menubardecorator.js', ['goog.ui.menuBarDecorator'], ['goog.ui.MenuBarRenderer', 'goog.ui.menuBar', 'goog.ui.registry']);
goog.addDependency('ui/menubarrenderer.js', ['goog.ui.MenuBarRenderer'], ['goog.a11y.aria.Role', 'goog.ui.Container', 'goog.ui.ContainerRenderer']);
goog.addDependency('ui/menubase.js', ['goog.ui.MenuBase'], ['goog.events.EventHandler', 'goog.events.EventType', 'goog.events.KeyHandler', 'goog.ui.Popup']);
goog.addDependency('ui/menubutton.js', ['goog.ui.MenuButton'], ['goog.Timer', 'goog.a11y.aria', 'goog.a11y.aria.State', 'goog.asserts', 'goog.dom', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.events.KeyHandler', 'goog.math.Box', 'goog.math.Rect', 'goog.positioning', 'goog.positioning.Corner', 'goog.positioning.MenuAnchoredPosition', 'goog.positioning.Overflow', 'goog.style', 'goog.ui.Button', 'goog.ui.Component', 'goog.ui.IdGenerator', 'goog.ui.Menu', 'goog.ui.MenuButtonRenderer', 'goog.ui.MenuRenderer', 'goog.ui.registry', 'goog.userAgent', 'goog.userAgent.product']);
goog.addDependency('ui/menubuttonrenderer.js', ['goog.ui.MenuButtonRenderer'], ['goog.dom', 'goog.style', 'goog.ui.CustomButtonRenderer', 'goog.ui.INLINE_BLOCK_CLASSNAME', 'goog.ui.Menu', 'goog.ui.MenuRenderer']);
goog.addDependency('ui/menuheader.js', ['goog.ui.MenuHeader'], ['goog.ui.Component', 'goog.ui.Control', 'goog.ui.MenuHeaderRenderer', 'goog.ui.registry']);
goog.addDependency('ui/menuheaderrenderer.js', ['goog.ui.MenuHeaderRenderer'], ['goog.ui.ControlRenderer']);
goog.addDependency('ui/menuitem.js', ['goog.ui.MenuItem'], ['goog.a11y.aria.Role', 'goog.array', 'goog.dom', 'goog.dom.classlist', 'goog.math.Coordinate', 'goog.string', 'goog.ui.Component', 'goog.ui.Control', 'goog.ui.MenuItemRenderer', 'goog.ui.registry']);
goog.addDependency('ui/menuitemrenderer.js', ['goog.ui.MenuItemRenderer'], ['goog.a11y.aria.Role', 'goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.ui.Component', 'goog.ui.ControlRenderer']);
goog.addDependency('ui/menurenderer.js', ['goog.ui.MenuRenderer'], ['goog.a11y.aria', 'goog.a11y.aria.Role', 'goog.a11y.aria.State', 'goog.asserts', 'goog.dom', 'goog.ui.ContainerRenderer', 'goog.ui.Separator']);
goog.addDependency('ui/menuseparator.js', ['goog.ui.MenuSeparator'], ['goog.ui.MenuSeparatorRenderer', 'goog.ui.Separator', 'goog.ui.registry']);
goog.addDependency('ui/menuseparatorrenderer.js', ['goog.ui.MenuSeparatorRenderer'], ['goog.dom', 'goog.dom.classlist', 'goog.ui.ControlContent', 'goog.ui.ControlRenderer']);
goog.addDependency('ui/mockactivitymonitor.js', ['goog.ui.MockActivityMonitor'], ['goog.events.EventType', 'goog.ui.ActivityMonitor']);
goog.addDependency('ui/mockactivitymonitor_test.js', ['goog.ui.MockActivityMonitorTest'], ['goog.events', 'goog.functions', 'goog.testing.jsunit', 'goog.testing.recordFunction', 'goog.ui.ActivityMonitor', 'goog.ui.MockActivityMonitor']);
goog.addDependency('ui/modalpopup.js', ['goog.ui.ModalPopup'], ['goog.Timer', 'goog.a11y.aria', 'goog.a11y.aria.State', 'goog.asserts', 'goog.dom', 'goog.dom.TagName', 'goog.dom.classlist', 'goog.dom.iframe', 'goog.events', 'goog.events.EventType', 'goog.events.FocusHandler', 'goog.fx.Transition', 'goog.string', 'goog.style', 'goog.ui.Component', 'goog.ui.PopupBase', 'goog.userAgent']);
goog.addDependency('ui/nativebuttonrenderer.js', ['goog.ui.NativeButtonRenderer'], ['goog.asserts', 'goog.dom.classlist', 'goog.events.EventType', 'goog.ui.ButtonRenderer', 'goog.ui.Component']);
goog.addDependency('ui/option.js', ['goog.ui.Option'], ['goog.ui.Component', 'goog.ui.MenuItem', 'goog.ui.registry']);
goog.addDependency('ui/palette.js', ['goog.ui.Palette'], ['goog.array', 'goog.dom', 'goog.events', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.math.Size', 'goog.ui.Component', 'goog.ui.Control', 'goog.ui.PaletteRenderer', 'goog.ui.SelectionModel']);
goog.addDependency('ui/paletterenderer.js', ['goog.ui.PaletteRenderer'], ['goog.a11y.aria', 'goog.a11y.aria.Role', 'goog.a11y.aria.State', 'goog.array', 'goog.asserts', 'goog.dom', 'goog.dom.NodeIterator', 'goog.dom.NodeType', 'goog.dom.TagName', 'goog.dom.classlist', 'goog.iter', 'goog.style', 'goog.ui.ControlRenderer', 'goog.userAgent']);
goog.addDependency('ui/plaintextspellchecker.js', ['goog.ui.PlainTextSpellChecker'], ['goog.Timer', 'goog.a11y.aria', 'goog.asserts', 'goog.dom', 'goog.events.EventHandler', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.events.KeyHandler', 'goog.spell.SpellCheck', 'goog.style', 'goog.ui.AbstractSpellChecker', 'goog.ui.Component', 'goog.userAgent']);
goog.addDependency('ui/popup.js', ['goog.ui.Popup', 'goog.ui.Popup.AbsolutePosition', 'goog.ui.Popup.AnchoredPosition', 'goog.ui.Popup.AnchoredViewPortPosition', 'goog.ui.Popup.ClientPosition', 'goog.ui.Popup.Corner', 'goog.ui.Popup.Overflow', 'goog.ui.Popup.ViewPortClientPosition', 'goog.ui.Popup.ViewPortPosition'], ['goog.math.Box', 'goog.positioning.AbsolutePosition', 'goog.positioning.AnchoredPosition', 'goog.positioning.AnchoredViewportPosition', 'goog.positioning.ClientPosition', 'goog.positioning.Corner', 'goog.positioning.Overflow', 'goog.positioning.ViewportClientPosition', 'goog.positioning.ViewportPosition', 'goog.style', 'goog.ui.PopupBase']);
goog.addDependency('ui/popupbase.js', ['goog.ui.PopupBase', 'goog.ui.PopupBase.EventType', 'goog.ui.PopupBase.Type'], ['goog.Timer', 'goog.array', 'goog.dom', 'goog.events', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.fx.Transition', 'goog.style', 'goog.userAgent']);
goog.addDependency('ui/popupcolorpicker.js', ['goog.ui.PopupColorPicker'], ['goog.asserts', 'goog.dom.classlist', 'goog.events.EventType', 'goog.positioning.AnchoredPosition', 'goog.positioning.Corner', 'goog.ui.ColorPicker', 'goog.ui.Component', 'goog.ui.Popup']);
goog.addDependency('ui/popupdatepicker.js', ['goog.ui.PopupDatePicker'], ['goog.events.EventType', 'goog.positioning.AnchoredPosition', 'goog.positioning.Corner', 'goog.positioning.Overflow', 'goog.style', 'goog.ui.Component', 'goog.ui.DatePicker', 'goog.ui.Popup', 'goog.ui.PopupBase']);
goog.addDependency('ui/popupmenu.js', ['goog.ui.PopupMenu'], ['goog.events.EventType', 'goog.positioning.AnchoredViewportPosition', 'goog.positioning.Corner', 'goog.positioning.MenuAnchoredPosition', 'goog.positioning.Overflow', 'goog.positioning.ViewportClientPosition', 'goog.structs.Map', 'goog.style', 'goog.ui.Component', 'goog.ui.Menu', 'goog.ui.PopupBase', 'goog.userAgent']);
goog.addDependency('ui/progressbar.js', ['goog.ui.ProgressBar', 'goog.ui.ProgressBar.Orientation'], ['goog.a11y.aria', 'goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.events', 'goog.events.EventType', 'goog.ui.Component', 'goog.ui.RangeModel', 'goog.userAgent']);
goog.addDependency('ui/prompt.js', ['goog.ui.Prompt'], ['goog.Timer', 'goog.dom', 'goog.events', 'goog.events.EventType', 'goog.functions', 'goog.html.SafeHtml', 'goog.html.legacyconversions', 'goog.ui.Component', 'goog.ui.Dialog', 'goog.userAgent']);
goog.addDependency('ui/rangemodel.js', ['goog.ui.RangeModel'], ['goog.events.EventTarget', 'goog.ui.Component']);
goog.addDependency('ui/ratings.js', ['goog.ui.Ratings', 'goog.ui.Ratings.EventType'], ['goog.a11y.aria', 'goog.a11y.aria.Role', 'goog.a11y.aria.State', 'goog.asserts', 'goog.dom.classlist', 'goog.events.EventType', 'goog.ui.Component']);
goog.addDependency('ui/registry.js', ['goog.ui.registry'], ['goog.asserts', 'goog.dom.classlist']);
goog.addDependency('ui/richtextspellchecker.js', ['goog.ui.RichTextSpellChecker'], ['goog.Timer', 'goog.asserts', 'goog.dom', 'goog.dom.NodeType', 'goog.dom.Range', 'goog.events.EventHandler', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.events.KeyHandler', 'goog.math.Coordinate', 'goog.spell.SpellCheck', 'goog.string.StringBuffer', 'goog.style', 'goog.ui.AbstractSpellChecker', 'goog.ui.Component', 'goog.ui.PopupMenu']);
goog.addDependency('ui/roundedpanel.js', ['goog.ui.BaseRoundedPanel', 'goog.ui.CssRoundedPanel', 'goog.ui.GraphicsRoundedPanel', 'goog.ui.RoundedPanel', 'goog.ui.RoundedPanel.Corner'], ['goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.graphics', 'goog.graphics.Path', 'goog.graphics.SolidFill', 'goog.graphics.Stroke', 'goog.math', 'goog.math.Coordinate', 'goog.style', 'goog.ui.Component', 'goog.userAgent']);
goog.addDependency('ui/roundedtabrenderer.js', ['goog.ui.RoundedTabRenderer'], ['goog.dom', 'goog.ui.Tab', 'goog.ui.TabBar', 'goog.ui.TabRenderer', 'goog.ui.registry']);
goog.addDependency('ui/scrollfloater.js', ['goog.ui.ScrollFloater', 'goog.ui.ScrollFloater.EventType'], ['goog.array', 'goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.events.EventType', 'goog.style', 'goog.ui.Component', 'goog.userAgent']);
goog.addDependency('ui/select.js', ['goog.ui.Select'], ['goog.a11y.aria', 'goog.a11y.aria.Role', 'goog.a11y.aria.State', 'goog.events.EventType', 'goog.ui.Component', 'goog.ui.IdGenerator', 'goog.ui.MenuButton', 'goog.ui.MenuItem', 'goog.ui.MenuRenderer', 'goog.ui.SelectionModel', 'goog.ui.registry']);
goog.addDependency('ui/selectionmenubutton.js', ['goog.ui.SelectionMenuButton', 'goog.ui.SelectionMenuButton.SelectionState'], ['goog.events.EventType', 'goog.style', 'goog.ui.Component', 'goog.ui.MenuButton', 'goog.ui.MenuItem', 'goog.ui.registry']);
goog.addDependency('ui/selectionmodel.js', ['goog.ui.SelectionModel'], ['goog.array', 'goog.events.EventTarget', 'goog.events.EventType']);
goog.addDependency('ui/separator.js', ['goog.ui.Separator'], ['goog.a11y.aria', 'goog.asserts', 'goog.ui.Component', 'goog.ui.Control', 'goog.ui.MenuSeparatorRenderer', 'goog.ui.registry']);
goog.addDependency('ui/serverchart.js', ['goog.ui.ServerChart', 'goog.ui.ServerChart.AxisDisplayType', 'goog.ui.ServerChart.ChartType', 'goog.ui.ServerChart.EncodingType', 'goog.ui.ServerChart.Event', 'goog.ui.ServerChart.LegendPosition', 'goog.ui.ServerChart.MaximumValue', 'goog.ui.ServerChart.MultiAxisAlignment', 'goog.ui.ServerChart.MultiAxisType', 'goog.ui.ServerChart.UriParam', 'goog.ui.ServerChart.UriTooLongEvent'], ['goog.Uri', 'goog.array', 'goog.asserts', 'goog.events.Event', 'goog.string', 'goog.ui.Component']);
goog.addDependency('ui/slider.js', ['goog.ui.Slider', 'goog.ui.Slider.Orientation'], ['goog.a11y.aria', 'goog.a11y.aria.Role', 'goog.dom', 'goog.ui.SliderBase']);
goog.addDependency('ui/sliderbase.js', ['goog.ui.SliderBase', 'goog.ui.SliderBase.AnimationFactory', 'goog.ui.SliderBase.Orientation'], ['goog.Timer', 'goog.a11y.aria', 'goog.a11y.aria.Role', 'goog.a11y.aria.State', 'goog.array', 'goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.events', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.events.KeyHandler', 'goog.events.MouseWheelHandler', 'goog.functions', 'goog.fx.AnimationParallelQueue', 'goog.fx.Dragger', 'goog.fx.Transition', 'goog.fx.dom.ResizeHeight', 'goog.fx.dom.ResizeWidth', 'goog.fx.dom.Slide', 'goog.math', 'goog.math.Coordinate', 'goog.style', 'goog.style.bidi', 'goog.ui.Component', 'goog.ui.RangeModel']);
goog.addDependency('ui/splitbehavior.js', ['goog.ui.SplitBehavior', 'goog.ui.SplitBehavior.DefaultHandlers'], ['goog.Disposable', 'goog.asserts', 'goog.dispose', 'goog.dom', 'goog.dom.NodeType', 'goog.dom.classlist', 'goog.events.EventHandler', 'goog.ui.ButtonSide', 'goog.ui.Component', 'goog.ui.decorate', 'goog.ui.registry']);
goog.addDependency('ui/splitpane.js', ['goog.ui.SplitPane', 'goog.ui.SplitPane.Orientation'], ['goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.events.EventType', 'goog.fx.Dragger', 'goog.math.Rect', 'goog.math.Size', 'goog.style', 'goog.ui.Component', 'goog.userAgent']);
goog.addDependency('ui/style/app/buttonrenderer.js', ['goog.ui.style.app.ButtonRenderer'], ['goog.dom.classlist', 'goog.ui.Button', 'goog.ui.CustomButtonRenderer', 'goog.ui.INLINE_BLOCK_CLASSNAME', 'goog.ui.registry']);
goog.addDependency('ui/style/app/menubuttonrenderer.js', ['goog.ui.style.app.MenuButtonRenderer'], ['goog.a11y.aria.Role', 'goog.array', 'goog.dom', 'goog.style', 'goog.ui.Menu', 'goog.ui.MenuRenderer', 'goog.ui.style.app.ButtonRenderer']);
goog.addDependency('ui/style/app/primaryactionbuttonrenderer.js', ['goog.ui.style.app.PrimaryActionButtonRenderer'], ['goog.ui.Button', 'goog.ui.registry', 'goog.ui.style.app.ButtonRenderer']);
goog.addDependency('ui/submenu.js', ['goog.ui.SubMenu'], ['goog.Timer', 'goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.events.KeyCodes', 'goog.positioning.AnchoredViewportPosition', 'goog.positioning.Corner', 'goog.style', 'goog.ui.Component', 'goog.ui.Menu', 'goog.ui.MenuItem', 'goog.ui.SubMenuRenderer', 'goog.ui.registry']);
goog.addDependency('ui/submenurenderer.js', ['goog.ui.SubMenuRenderer'], ['goog.a11y.aria', 'goog.a11y.aria.State', 'goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.style', 'goog.ui.Menu', 'goog.ui.MenuItemRenderer']);
goog.addDependency('ui/tab.js', ['goog.ui.Tab'], ['goog.ui.Component', 'goog.ui.Control', 'goog.ui.TabRenderer', 'goog.ui.registry']);
goog.addDependency('ui/tabbar.js', ['goog.ui.TabBar', 'goog.ui.TabBar.Location'], ['goog.ui.Component.EventType', 'goog.ui.Container', 'goog.ui.Container.Orientation', 'goog.ui.Tab', 'goog.ui.TabBarRenderer', 'goog.ui.registry']);
goog.addDependency('ui/tabbarrenderer.js', ['goog.ui.TabBarRenderer'], ['goog.a11y.aria.Role', 'goog.object', 'goog.ui.ContainerRenderer']);
goog.addDependency('ui/tablesorter.js', ['goog.ui.TableSorter', 'goog.ui.TableSorter.EventType'], ['goog.array', 'goog.dom', 'goog.dom.TagName', 'goog.dom.classlist', 'goog.events.EventType', 'goog.functions', 'goog.ui.Component']);
goog.addDependency('ui/tabpane.js', ['goog.ui.TabPane', 'goog.ui.TabPane.Events', 'goog.ui.TabPane.TabLocation', 'goog.ui.TabPane.TabPage', 'goog.ui.TabPaneEvent'], ['goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.events', 'goog.events.Event', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.style']);
goog.addDependency('ui/tabrenderer.js', ['goog.ui.TabRenderer'], ['goog.a11y.aria.Role', 'goog.ui.Component', 'goog.ui.ControlRenderer']);
goog.addDependency('ui/textarea.js', ['goog.ui.Textarea', 'goog.ui.Textarea.EventType'], ['goog.asserts', 'goog.dom', 'goog.dom.classlist', 'goog.events.EventType', 'goog.style', 'goog.ui.Control', 'goog.ui.TextareaRenderer', 'goog.userAgent']);
goog.addDependency('ui/textarearenderer.js', ['goog.ui.TextareaRenderer'], ['goog.dom.TagName', 'goog.ui.Component', 'goog.ui.ControlRenderer']);
goog.addDependency('ui/togglebutton.js', ['goog.ui.ToggleButton'], ['goog.ui.Button', 'goog.ui.Component', 'goog.ui.CustomButtonRenderer', 'goog.ui.registry']);
goog.addDependency('ui/toolbar.js', ['goog.ui.Toolbar'], ['goog.ui.Container', 'goog.ui.ToolbarRenderer']);
goog.addDependency('ui/toolbarbutton.js', ['goog.ui.ToolbarButton'], ['goog.ui.Button', 'goog.ui.ToolbarButtonRenderer', 'goog.ui.registry']);
goog.addDependency('ui/toolbarbuttonrenderer.js', ['goog.ui.ToolbarButtonRenderer'], ['goog.ui.CustomButtonRenderer']);
goog.addDependency('ui/toolbarcolormenubutton.js', ['goog.ui.ToolbarColorMenuButton'], ['goog.ui.ColorMenuButton', 'goog.ui.ToolbarColorMenuButtonRenderer', 'goog.ui.registry']);
goog.addDependency('ui/toolbarcolormenubuttonrenderer.js', ['goog.ui.ToolbarColorMenuButtonRenderer'], ['goog.asserts', 'goog.dom.classlist', 'goog.ui.ColorMenuButtonRenderer', 'goog.ui.MenuButtonRenderer', 'goog.ui.ToolbarMenuButtonRenderer']);
goog.addDependency('ui/toolbarmenubutton.js', ['goog.ui.ToolbarMenuButton'], ['goog.ui.MenuButton', 'goog.ui.ToolbarMenuButtonRenderer', 'goog.ui.registry']);
goog.addDependency('ui/toolbarmenubuttonrenderer.js', ['goog.ui.ToolbarMenuButtonRenderer'], ['goog.ui.MenuButtonRenderer']);
goog.addDependency('ui/toolbarrenderer.js', ['goog.ui.ToolbarRenderer'], ['goog.a11y.aria.Role', 'goog.ui.Container', 'goog.ui.ContainerRenderer', 'goog.ui.Separator', 'goog.ui.ToolbarSeparatorRenderer']);
goog.addDependency('ui/toolbarselect.js', ['goog.ui.ToolbarSelect'], ['goog.ui.Select', 'goog.ui.ToolbarMenuButtonRenderer', 'goog.ui.registry']);
goog.addDependency('ui/toolbarseparator.js', ['goog.ui.ToolbarSeparator'], ['goog.ui.Separator', 'goog.ui.ToolbarSeparatorRenderer', 'goog.ui.registry']);
goog.addDependency('ui/toolbarseparatorrenderer.js', ['goog.ui.ToolbarSeparatorRenderer'], ['goog.asserts', 'goog.dom.classlist', 'goog.ui.INLINE_BLOCK_CLASSNAME', 'goog.ui.MenuSeparatorRenderer']);
goog.addDependency('ui/toolbartogglebutton.js', ['goog.ui.ToolbarToggleButton'], ['goog.ui.ToggleButton', 'goog.ui.ToolbarButtonRenderer', 'goog.ui.registry']);
goog.addDependency('ui/tooltip.js', ['goog.ui.Tooltip', 'goog.ui.Tooltip.CursorTooltipPosition', 'goog.ui.Tooltip.ElementTooltipPosition', 'goog.ui.Tooltip.State'], ['goog.Timer', 'goog.array', 'goog.dom', 'goog.dom.safe', 'goog.events', 'goog.events.EventType', 'goog.html.legacyconversions', 'goog.math.Box', 'goog.math.Coordinate', 'goog.positioning', 'goog.positioning.AnchoredPosition', 'goog.positioning.Corner', 'goog.positioning.Overflow', 'goog.positioning.OverflowStatus', 'goog.positioning.ViewportPosition', 'goog.structs.Set', 'goog.style', 'goog.ui.Popup', 'goog.ui.PopupBase']);
goog.addDependency('ui/tree/basenode.js', ['goog.ui.tree.BaseNode', 'goog.ui.tree.BaseNode.EventType'], ['goog.Timer', 'goog.a11y.aria', 'goog.asserts', 'goog.dom.safe', 'goog.events.Event', 'goog.events.KeyCodes', 'goog.html.SafeHtml', 'goog.html.SafeStyle', 'goog.html.legacyconversions', 'goog.string', 'goog.string.StringBuffer', 'goog.style', 'goog.ui.Component']);
goog.addDependency('ui/tree/treecontrol.js', ['goog.ui.tree.TreeControl'], ['goog.a11y.aria', 'goog.asserts', 'goog.dom.classlist', 'goog.events.EventType', 'goog.events.FocusHandler', 'goog.events.KeyHandler', 'goog.html.SafeHtml', 'goog.log', 'goog.ui.tree.BaseNode', 'goog.ui.tree.TreeNode', 'goog.ui.tree.TypeAhead', 'goog.userAgent']);
goog.addDependency('ui/tree/treenode.js', ['goog.ui.tree.TreeNode'], ['goog.ui.tree.BaseNode']);
goog.addDependency('ui/tree/typeahead.js', ['goog.ui.tree.TypeAhead', 'goog.ui.tree.TypeAhead.Offset'], ['goog.array', 'goog.events.KeyCodes', 'goog.string', 'goog.structs.Trie']);
goog.addDependency('ui/tristatemenuitem.js', ['goog.ui.TriStateMenuItem', 'goog.ui.TriStateMenuItem.State'], ['goog.dom.classlist', 'goog.ui.Component', 'goog.ui.MenuItem', 'goog.ui.TriStateMenuItemRenderer', 'goog.ui.registry']);
goog.addDependency('ui/tristatemenuitemrenderer.js', ['goog.ui.TriStateMenuItemRenderer'], ['goog.asserts', 'goog.dom.classlist', 'goog.ui.MenuItemRenderer']);
goog.addDependency('ui/twothumbslider.js', ['goog.ui.TwoThumbSlider'], ['goog.a11y.aria', 'goog.a11y.aria.Role', 'goog.dom', 'goog.ui.SliderBase']);
goog.addDependency('ui/zippy.js', ['goog.ui.Zippy', 'goog.ui.Zippy.Events', 'goog.ui.ZippyEvent'], ['goog.a11y.aria', 'goog.a11y.aria.Role', 'goog.a11y.aria.State', 'goog.dom', 'goog.dom.classlist', 'goog.events.Event', 'goog.events.EventHandler', 'goog.events.EventTarget', 'goog.events.EventType', 'goog.events.KeyCodes', 'goog.style']);
goog.addDependency('uri/uri.js', ['goog.Uri', 'goog.Uri.QueryData'], ['goog.array', 'goog.string', 'goog.structs', 'goog.structs.Map', 'goog.uri.utils', 'goog.uri.utils.ComponentIndex', 'goog.uri.utils.StandardQueryParam']);
goog.addDependency('uri/uri_test.js', ['goog.UriTest'], ['goog.Uri', 'goog.testing.jsunit']);
goog.addDependency('uri/utils.js', ['goog.uri.utils', 'goog.uri.utils.ComponentIndex', 'goog.uri.utils.QueryArray', 'goog.uri.utils.QueryValue', 'goog.uri.utils.StandardQueryParam'], ['goog.asserts', 'goog.string', 'goog.userAgent']);
goog.addDependency('uri/utils_test.js', ['goog.uri.utilsTest'], ['goog.functions', 'goog.string', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.uri.utils']);
goog.addDependency('useragent/adobereader.js', ['goog.userAgent.adobeReader'], ['goog.string', 'goog.userAgent']);
goog.addDependency('useragent/adobereader_test.js', ['goog.userAgent.adobeReaderTest'], ['goog.testing.jsunit', 'goog.userAgent.adobeReader']);
goog.addDependency('useragent/flash.js', ['goog.userAgent.flash'], ['goog.string']);
goog.addDependency('useragent/flash_test.js', ['goog.userAgent.flashTest'], ['goog.testing.jsunit', 'goog.userAgent.flash']);
goog.addDependency('useragent/iphoto.js', ['goog.userAgent.iphoto'], ['goog.string', 'goog.userAgent']);
goog.addDependency('useragent/jscript.js', ['goog.userAgent.jscript'], ['goog.string']);
goog.addDependency('useragent/jscript_test.js', ['goog.userAgent.jscriptTest'], ['goog.testing.jsunit', 'goog.userAgent.jscript']);
goog.addDependency('useragent/keyboard.js', ['goog.userAgent.keyboard'], ['goog.userAgent', 'goog.userAgent.product']);
goog.addDependency('useragent/keyboard_test.js', ['goog.userAgent.keyboardTest'], ['goog.labs.userAgent.testAgents', 'goog.labs.userAgent.util', 'goog.testing.MockUserAgent', 'goog.testing.jsunit', 'goog.userAgent.keyboard', 'goog.userAgentTestUtil']);
goog.addDependency('useragent/picasa.js', ['goog.userAgent.picasa'], ['goog.string', 'goog.userAgent']);
goog.addDependency('useragent/platform.js', ['goog.userAgent.platform'], ['goog.userAgent']);
goog.addDependency('useragent/platform_test.js', ['goog.userAgent.platformTest'], ['goog.testing.MockUserAgent', 'goog.testing.jsunit', 'goog.userAgent', 'goog.userAgent.platform']);
goog.addDependency('useragent/product.js', ['goog.userAgent.product'], ['goog.userAgent']);
goog.addDependency('useragent/product_isversion.js', ['goog.userAgent.product.isVersion'], ['goog.userAgent.product']);
goog.addDependency('useragent/product_test.js', ['goog.userAgent.productTest'], ['goog.array', 'goog.labs.userAgent.util', 'goog.testing.MockUserAgent', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.userAgent', 'goog.userAgent.product', 'goog.userAgent.product.isVersion', 'goog.userAgentTestUtil']);
goog.addDependency('useragent/useragent.js', ['goog.userAgent'], ['goog.labs.userAgent.browser', 'goog.labs.userAgent.engine', 'goog.labs.userAgent.util', 'goog.string']);
goog.addDependency('useragent/useragent_quirks_test.js', ['goog.userAgentQuirksTest'], ['goog.testing.jsunit', 'goog.userAgent']);
goog.addDependency('useragent/useragent_test.js', ['goog.userAgentTest'], ['goog.array', 'goog.labs.userAgent.testAgents', 'goog.labs.userAgent.util', 'goog.testing.PropertyReplacer', 'goog.testing.jsunit', 'goog.userAgent', 'goog.userAgentTestUtil']);
goog.addDependency('useragent/useragenttestutil.js', ['goog.userAgentTestUtil', 'goog.userAgentTestUtil.UserAgents'], ['goog.labs.userAgent.browser', 'goog.labs.userAgent.engine', 'goog.userAgent', 'goog.userAgent.keyboard', 'goog.userAgent.platform', 'goog.userAgent.product', 'goog.userAgent.product.isVersion']);
goog.addDependency('vec/float32array.js', ['goog.vec.Float32Array'], []);
goog.addDependency('vec/float64array.js', ['goog.vec.Float64Array'], []);
goog.addDependency('vec/mat3.js', ['goog.vec.Mat3'], ['goog.vec']);
goog.addDependency('vec/mat3d.js', ['goog.vec.mat3d', 'goog.vec.mat3d.Type'], ['goog.vec']);
goog.addDependency('vec/mat3f.js', ['goog.vec.mat3f', 'goog.vec.mat3f.Type'], ['goog.vec']);
goog.addDependency('vec/mat4.js', ['goog.vec.Mat4'], ['goog.vec', 'goog.vec.Vec3', 'goog.vec.Vec4']);
goog.addDependency('vec/mat4d.js', ['goog.vec.mat4d', 'goog.vec.mat4d.Type'], ['goog.vec', 'goog.vec.vec3d', 'goog.vec.vec4d']);
goog.addDependency('vec/mat4f.js', ['goog.vec.mat4f', 'goog.vec.mat4f.Type'], ['goog.vec', 'goog.vec.vec3f', 'goog.vec.vec4f']);
goog.addDependency('vec/matrix3.js', ['goog.vec.Matrix3'], []);
goog.addDependency('vec/matrix4.js', ['goog.vec.Matrix4'], ['goog.vec', 'goog.vec.Vec3', 'goog.vec.Vec4']);
goog.addDependency('vec/quaternion.js', ['goog.vec.Quaternion'], ['goog.vec', 'goog.vec.Vec3', 'goog.vec.Vec4']);
goog.addDependency('vec/ray.js', ['goog.vec.Ray'], ['goog.vec.Vec3']);
goog.addDependency('vec/vec.js', ['goog.vec', 'goog.vec.AnyType', 'goog.vec.ArrayType', 'goog.vec.Float32', 'goog.vec.Float64', 'goog.vec.Number'], ['goog.vec.Float32Array', 'goog.vec.Float64Array']);
goog.addDependency('vec/vec2.js', ['goog.vec.Vec2'], ['goog.vec']);
goog.addDependency('vec/vec2d.js', ['goog.vec.vec2d', 'goog.vec.vec2d.Type'], ['goog.vec']);
goog.addDependency('vec/vec2f.js', ['goog.vec.vec2f', 'goog.vec.vec2f.Type'], ['goog.vec']);
goog.addDependency('vec/vec3.js', ['goog.vec.Vec3'], ['goog.vec']);
goog.addDependency('vec/vec3d.js', ['goog.vec.vec3d', 'goog.vec.vec3d.Type'], ['goog.vec']);
goog.addDependency('vec/vec3f.js', ['goog.vec.vec3f', 'goog.vec.vec3f.Type'], ['goog.vec']);
goog.addDependency('vec/vec4.js', ['goog.vec.Vec4'], ['goog.vec']);
goog.addDependency('vec/vec4d.js', ['goog.vec.vec4d', 'goog.vec.vec4d.Type'], ['goog.vec']);
goog.addDependency('vec/vec4f.js', ['goog.vec.vec4f', 'goog.vec.vec4f.Type'], ['goog.vec']);
goog.addDependency('webgl/webgl.js', ['goog.webgl'], []);
goog.addDependency('window/window.js', ['goog.window'], ['goog.string', 'goog.userAgent']);
goog.addDependency('window/window_test.js', ['goog.windowTest'], ['goog.dom', 'goog.events', 'goog.string', 'goog.testing.AsyncTestCase', 'goog.testing.jsunit', 'goog.window']);

/**
 * @fileoverview Custom exports file.
 * @suppress {checkVars}
 */

goog.require('olgm.OLGoogleMaps');
goog.require('olgm.gm.MapLabel');
goog.require('olgm.interaction');
goog.require('olgm.layer.Google');


goog.exportSymbol(
    'olgm.OLGoogleMaps',
    olgm.OLGoogleMaps,
    OL3GOOGLEMAPS);

goog.exportProperty(
    olgm.OLGoogleMaps.prototype,
    'activate',
    olgm.OLGoogleMaps.prototype.activate);

goog.exportProperty(
    olgm.OLGoogleMaps.prototype,
    'deactivate',
    olgm.OLGoogleMaps.prototype.deactivate);

goog.exportProperty(
    olgm.OLGoogleMaps.prototype,
    'getGoogleMapsActive',
    olgm.OLGoogleMaps.prototype.getGoogleMapsActive);

goog.exportProperty(
    olgm.OLGoogleMaps.prototype,
    'getGoogleMapsMap',
    olgm.OLGoogleMaps.prototype.getGoogleMapsMap);

goog.exportProperty(
    olgm.OLGoogleMaps.prototype,
    'toggle',
    olgm.OLGoogleMaps.prototype.toggle);

goog.exportSymbol(
    'olgm.layer.Google',
    olgm.layer.Google,
    OL3GOOGLEMAPS);

goog.exportProperty(
    olgm.layer.Google.prototype,
    'getMapTypeId',
    olgm.layer.Google.prototype.getMapTypeId);

goog.exportSymbol(
    'olgm.interaction.defaults',
    olgm.interaction.defaults,
    OL3GOOGLEMAPS);

goog.exportSymbol(
    'olgm.gm.MapLabel',
    olgm.gm.MapLabel,
    OL3GOOGLEMAPS);

goog.exportProperty(
    olgm.gm.MapLabel.prototype,
    'changed',
    olgm.gm.MapLabel.prototype.changed);

goog.exportProperty(
    olgm.gm.MapLabel.prototype,
    'onAdd',
    olgm.gm.MapLabel.prototype.onAdd);

goog.exportProperty(
    olgm.gm.MapLabel.prototype,
    'draw',
    olgm.gm.MapLabel.prototype.draw);

goog.exportProperty(
    olgm.gm.MapLabel.prototype,
    'onRemove',
    olgm.gm.MapLabel.prototype.onRemove);
OL3GOOGLEMAPS.olgm = olgm;

  return OL3GOOGLEMAPS.olgm;
}));
