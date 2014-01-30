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
 * RunwaySDK Javascript UI library.
 * 
 * @author Terraframe
 */
//define(["../widget/Widget"], function(){
(function(){

var RW = Mojo.Meta.alias(Mojo.RW_PACKAGE + "*");
var UI = Mojo.Meta.alias(Mojo.UI_PACKAGE + "*");

var Button = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'Button', {
  
  Extends : RW.Widget,
  
  Implements : [UI.ElementProviderIF, UI.ButtonIF],

  Instance : {
    initialize : function(label, handler) {
      this.$initialize("button", {type: "button"}); // FIXME: I don't really think the type is actually working or doing anything here
      this.setInnerHTML(label);
      
      // Register onclick event listener
      var listener = new com.runwaysdk.event.EventListener({handleEvent : handler});
      this.addEventListener('click', listener);
    }
  }
});

})();