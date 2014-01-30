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

// the javascript onerror event is insufficient, it only logs some errors in some browsers...
// most notably, if you have an error inside of an event handler the onerror will not be called
// (also, errors from ajax requests and basically anything actually useful)
// the only "solution" is to wrap everything in try/catches. Unfortunately, the javascript error
// object (in the catch) doesn't give line numbers or source files that the error occurred in,
// so that doesn't help AT ALL for logging. (technically this is only partially true. Firefox will
// give line numbers and source file locations in the erorr object, but its the ONLY browser that
// does. Even Chrome won't give you that!)

//define(["./log4js"], function(Log4js){
(function(){

  var bind = function(thisRef, func) {
    var args = [].splice.call(arguments, 2, arguments.length);
    return function(){
      return func.apply(thisRef, args.concat([].splice.call(arguments, 0, arguments.length)));
    };
  };
  
  var logger = new Log4js.getLogger("Generic Runway Logger");
  logger.setLevel(Log4js.Level.ALL); // this should take a parameter from the clerver
  if ( (window.console && window.console.log) || window.opera )
  {
    logger.addAppender(new Log4js.BrowserConsoleAppender());
  }
  else {
    logger.addAppender(new Log4js.ConsoleAppender());
  }
  
  // Note that log4js clobbers the window.onerror with the last logger instantiated...
  // this is retarded, so I commented that line out of their source. If we ever upgrade
  // log4js versions, keep that in mind
  ErrorCatch = {};
  ErrorCatch.lastExceptionLogged = null;
  var oldWindowOnError = window.onerror;
  window.onerror = function(errorMsg, url, lineNumber) {
    try {
      if (oldWindowOnError != null) {
        oldWindowOnError(errorMsg, url, lineNumber);
      }
      
      if (ErrorCatch.lastExceptionLogged != null && (ErrorCatch.lastExceptionLogged.getMessage() === errorMsg || "Uncaught " + ErrorCatch.lastExceptionLogged.getMessage() === errorMsg)) {
        // This message has already been logged.
      }
      else if (com && com.runwaysdk && com.runwaysdk.Exception) {
        logger.windowError(errorMsg, url, lineNumber);
      }
    }
    catch (er) {
      logger.windowError(errorMsg, url, lineNumber);
    }
  }
  
  // TODO : ajaxify this biznitch
  //var ajaxAppender = new Log4js.AjaxAppender("./.jsp");
  //ajaxAppender.setThreshold(5);
  //logger.addAppender(ajaxAppender);
  
  if ( (window.console && window.console.log) || window.opera )
  {
    //logger.addAppender(new Log4js.BrowserConsoleAppender());
    
    // hook into console.log statements
    /*
    var old = window.console.log;
    window.console.log = function() {
      old.apply(window.console, arguments);
      logger.info.apply(logger, arguments);
    }
    
    // hook into console.warn statements
    var old2 = window.console.warn;
    window.console.warn = function() {
      old2.apply(window.console, arguments);
      logger.warn.apply(logger, arguments);
    }
    */
  }
  else {
    //logger.addAppender(new Log4js.ConsoleAppender());
  }

})();
