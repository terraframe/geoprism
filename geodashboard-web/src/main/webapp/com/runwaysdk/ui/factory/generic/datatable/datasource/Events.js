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

//define(["../../../../../ClassFramework", "../../../../../Util", "../../../../RunwaySDK_UI"], function(ClassFramework, Util, UI) {
(function(){  

  var ClassFramework = Mojo.Meta;
  var Util = Mojo.Util;
  
  var pack = "com.runwaysdk.ui.factory.generic.datatable.datasource.events."; 
  
  var formatResponseEvent = ClassFramework.newClass(pack+'FormatResponseEvent', {
    
    Extends : com.runwaysdk.event.CustomEvent,
    
    Instance : {
      
      initialize : function(formattedResponse, unformattedResponse) {
        this.$initialize();
        
        this._formattedResponse = formattedResponse;
        this._unformattedResponse = unformattedResponse;
      },
      
      getFormattedResponse : function() {
        return this._formattedResponse;
      },
      
      getUnformattedResponse : function() {
        return this._unformattedResponse;
      }
      
    }
    
  });
  
  var beforePerformRequest = ClassFramework.newClass(pack+'BeforePerformRequestEvent', {
    
    Extends : com.runwaysdk.event.CustomEvent,
    
    Instance : {
      
      initialize : function(callback) {
        this.$initialize();
        
        this._callback = callback;
      },
      
      getCallback : function() {
        return this._callback;
      }
      
    }
    
  });
  
  var afterPerformRequest = ClassFramework.newClass(pack+'AfterPerformRequestEvent', {
    
    Extends : com.runwaysdk.event.CustomEvent,
    
    Instance : {
      
      initialize : function(response) {
        this.$initialize();
        
        this._response = response;
      },
      
      getResponse : function() {
        return this._response;
      }
      
    }
    
  });
  
  var setPageNumber = ClassFramework.newClass(pack+'SetPageNumberEvent', {
    
    Extends : com.runwaysdk.event.CustomEvent,
    
    Instance : {
      
      initialize : function(pageNumber) {
        this.$initialize();
        
        this._pageNum = pageNumber;
      },
      
      getPageNumber : function() {
        return this._pageNum;
      }
      
    }
    
  });
  
  var setPageSize = ClassFramework.newClass(pack+'SetPageSizeEvent', {
    
    Extends : com.runwaysdk.event.CustomEvent,
    
    Instance : {
      
      initialize : function(pageSize) {
        this.$initialize();
        
        this._pageSize = pageSize;
      },
      
      getPageSize : function() {
        return this._pageSize;
      }
      
    }
    
  });
  
  return Mojo.Meta.alias(pack+"*");
  
})();
