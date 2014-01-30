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

//define(["../../../../ClassFramework", "../../runway/widget/Widget"], function(ClassFramework, Widget){
(function(){

  var ClassFramework = Mojo.Meta;
  var Widget = com.runwaysdk.ui.factory.runway.Widget;
  
  var RW = ClassFramework.alias(Mojo.RW_PACKAGE + "*");
  var UI = ClassFramework.alias(Mojo.UI_PACKAGE + "*");
  
  var Row = ClassFramework.newClass("com.runwaysdk.ui.factory.generic.datatable.Row", {
    
    Extends : Widget,
    
    Instance : {
      initialize : function(config) {
        if (config) {
          this.$initialize(config.el || "tr");
          
          this.resizable = config.resizable || true;
          this.draggable = config.draggable || true;
          this.isHeader = config.isHeader || false;
          this.rowNumber = config.rowNumber;
          this.parentTable = config.parentTable;
          
          if (config.data) {
            this.addData(config.data);
          }
//          if (config.rowNumber == null) {
//            var siblings = this.getParent().getChildNodes();
//            
//            for (var i = 0; i < siblings.length; ++i) {
//              
//            }
//          }
        }
        else {
          this.$initialize("tr");
        }
        
        if (this.isHeader) {
          this.addClassName("headerrow");
        }
        else {
          if (this.rowNumber % 2 == 0) 
            this.addClassName("evenrow");
          else 
            this.addClassName("oddrow");
        }
        
        this._rowData = [];
      },
  
      addData : function(data)
      {
        var td;
        if (this.isHeader) {
          td = this.getFactory().newElement("th");
        }
        else {
          td = this.getFactory().newElement("td");
        }
        
        this._rowData.push(td);
        
        if (Mojo.Util.isObject(data)) {
          td.appendChild(data);
        }
        else {
          td.appendInnerHTML(data);
        }
        
        this.appendChild(td);
        
        return td;
      },
      
      getRowNumber : function() {
        return this.rowNumber;
      },
      
      getParentTable : function() {
        return this.parentTable;
      },
      
      getCell : function(num) {
        return this.getFactory().wrapElement(this._rowData[num-1]);
      },
      
      toggleSelected : function() {
        if (this.isSelected()) {
          this.setSelected(false);
        }
        else {
          this.setSelected(true);
        }
      },
      
      setSelected : function(bool) {
        if (bool && !this.isSelected()) {
          this.addClassName("row_selected");
        }
        else if (!bool && this.isSelected()) {
          this.removeClassName("row_selected");
        }
      },
      
      isSelected : function() {
        return this.hasClassName("row_selected");
      },
      
      // FIXME : is the speed enhancement worth it?
      // This is a psuedo-private method intended to only be used by the column class
      // in order to optimize the column's setStyle method. This should not be called by end users.
      _getRawCell : function(num) {
        return this._rowData[num-1];
      }
    }
  });
  
  return Row;
  
})();
