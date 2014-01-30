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

//define(["../../../../ClassFramework", "../../../../Util", "../../runway/widget/Widget"], function(ClassFramework, Util, Widget){
(function(){

  var ClassFramework = Mojo.Meta;
  var Util = Mojo.Util;
  var Widget = com.runwaysdk.ui.factory.runway.Widget;
  
  var RW = ClassFramework.alias(Mojo.RW_PACKAGE + "*");
  var UI = ClassFramework.alias(Mojo.UI_PACKAGE + "*");
  
  var Column = ClassFramework.newClass("com.runwaysdk.ui.factory.generic.datatable.Column", {
    
    Extends : Widget,
    
    Instance : {
      initialize : function(config) {
        if (Util.isValid(config)) {
          this.$initialize(config.el || "col");
          
          this.sortable = config.sortable || true;
          this.resizable = config.resizable || true;
          this.draggable = config.draggable || true;
          this.columnNumber = config.columnNumber;
          this.parentTable = config.parentTable;
        }
        else {
          this.$initialize("col");
        }
      },
      
//      setStyle : function(k,v) {
//        var rowNumber = 1;
//        var row = this.parentTable.getRow(1);
//        
//        while(Mojo.Util.isValid(row)) {
//          UI.DOMFacade.setStyle(row._getRawCell(this.columnNumber), k, v);
//          row = this.parentTable.getRow(++rowNumber);
//        }
//      },
      
      getColumnNumber : function() {
        return this.columnNumber;
      },
      
      getParentTable : function() {
        return this.parentTable;
      },
    }
  });
  
  return Column;
  
})();
