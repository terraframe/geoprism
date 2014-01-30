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
//define(["../../../../ClassFramework", "./datasource/DataSourceFactory", "./../../generic/datatable/Column", "./../../generic/datatable/Row", "../../generic/datatable/datasource/DataSourceIF", "../widget/Widget"], function(ClassFramework, DataSourceFactory, Column, Row, DataSourceIF){
(function(){

  var ClassFramework = Mojo.Meta;
  
  
  var RW = ClassFramework.alias(Mojo.RW_PACKAGE + "*");
  var UI = ClassFramework.alias(Mojo.UI_PACKAGE + "*");
  
  var DataTable = ClassFramework.newClass(Mojo.RW_PACKAGE+'datatable.DataTable', {
    
    Extends : RW.Widget,
    
    Instance : {
      
      initialize : function(config)
      {
        config = config || {};
        config.title = config.title || "";
        config.border = config.border || "1";
        config.el = config.el || "div";
        
        this.$initialize(config.el);
        
        // Create the "Show [select] entries" portion.
        this._pageLenDiv = this.getFactory().newElement("div");
        this._pageLenDiv.addClassName("pageLenDiv");
        this.appendChild(this._pageLenDiv);
        var label = this.getFactory().newElement("label");
        this._pageLenDiv.appendChild(label);
        var span1 = this.getFactory().newElement("span");
        span1.setInnerHTML("Show ");
        label.appendChild(span1);
        var select = this.getFactory().newElement("select");
        var option10 = this.getFactory().newElement("option");
        option10.setAttribute("selected", "selected");
        option10.setInnerHTML("10");
        select.appendChild(option10);
        var option25 = this.getFactory().newElement("option");
        option25.setInnerHTML("25");
        select.appendChild(option25);
        var option50 = this.getFactory().newElement("option");
        option50.setInnerHTML("50");
        select.appendChild(option50);
        var option100 = this.getFactory().newElement("option");
        option100.setInnerHTML("100");
        select.appendChild(option100);
        label.appendChild(select);
        var span2 = this.getFactory().newElement("span");
        span2.setInnerHTML(" entries");
        label.appendChild(span2);
        
        // Create the search bar
        this._searchBar = this.getFactory().newElement("div");
        this._searchBar.addClassName("searchBar");
        this.appendChild(this._searchBar);
        var label = this.getFactory().newElement("label");
        label.setInnerHTML("Search: ");
        this._searchBar.appendChild(label);
        var textBox = this.getFactory().newElement("input");
        textBox.setAttribute("type", "text");
        this._searchBar.appendChild(textBox);
        
        // Create elements for table
        this._table = this.getFactory().newElement("table", {border: config.border}, config.styles);
        this.appendChild(this._table);
        
        var caption = this.getFactory().newElement("caption");
        caption.setInnerHTML(config.title);
        this._table.appendChild(caption);
        
        this._thead = this.getFactory().newElement("thead");
        this._table.appendChild(this._thead);
        this._headerRow = new Row({isHeader:true});
        this._thead.appendChild(this._headerRow);
        
        this._tBody = this.getFactory().newElement("tbody");
        this._table.appendChild(this._tBody);
        
        
        // Create the "showing x to y of z entries" text
        this._showingLabel = this.getFactory().newElement("label");
        this._showingLabel.addClassName("showingLabel");
        this._showingLabel.setInnerHTML("Showing 0 to 0 of 0 entries.");
        this.appendChild(this._showingLabel);
        
        // Create the Previous and Next buttons
        this._pageButtons  = this.getFactory().newElement("div");
        this._pageButtons.addClassName("pageButtons");
        this.appendChild(this._pageButtons);
        var previous = this.getFactory().newElement("a");
        previous.addClassName("pagePrevious");
        previous.addClassName("disabled");
        previous.setInnerHTML("Previous");
        this._pageButtons.appendChild(previous);
        var next = this.getFactory().newElement("a");
        next.addClassName("pageNext");
        next.addClassName("disabled");
        next.setInnerHTML("Next");
        this._pageButtons.appendChild(next);
        
        this._rows = [];
        this._columns = [];
        
        if (DataSourceIF.getMetaClass().isInstance(config.dataSource)) {
          this._dataSource = config.dataSource;
        }
        else {
          config.dataSource.dataTable = this;
          this._dataSource = DataSourceFactory.newDataSource(config.dataSource);
        }
      },
      
      addHeader: function(inner) // can take a header primitive or a collection of header primitives
      {
        if (Mojo.Util.isObject(inner) || Mojo.Util.isArray(inner)) {
          for (key in inner) {
            this.addHeader(inner[key]);
          }
        }
        else {
          this._headerRow.addData(inner);
        }
      },
      
      /** This function only adds the col element, it doesn't actually modify the content of the
       * table at all.
       * @param {Object} columnNumber : The number representing which column it is in the table.
       */
      addColumn : function(columnNum) {
        var column = new Column({columnNumber: columnNum, parentTable: this});
        this._table.appendChild(column);
        this._columns.push(column);
      },
      
      addRow : function(rowData, repeatNumber, rowNum) // if repeatNumber is 2 this will add the same row twice (if its 1 or 0 or null it does nothing)
      {
        if ( !(Mojo.Util.isObject(rowData) || Mojo.Util.isArray(rowData)) ) { // rowData is a primative (or null)
          var row = new Row({rowNumber: this._rows.length+1, parentTable: this});
          this._rows.push(row);
          this._tBody.appendChild(row);
          
          return row;
        }
        else if ( !(Mojo.Util.isObject(rowData[0]) || Mojo.Util.isArray(rowData[0])) ) { // rowData is an array of primitives (most common use case)
          var row = new Row({rowNumber: this._rows.length+1, parentTable: this});
          this._rows.push(row);
          this._tBody.appendChild(row);
          
          for (var k in rowData) {
            row.addData(rowData[k]);
          }
          
          if (Mojo.Util.isNumber(repeatNumber)) {
            repeatNumber--;
            
            for (var i = 0; i < repeatNumber; i++) {
              this.addRow(rowData);
            }
          }
          
          return row;
        }
        else { // rowData is an array of arrays (return an array of rows)
          var retArr = [];
          
          for (var k in rowData) {
            retArr.push( this.addRow(rowData[k]) );
          }
          
          return retArr;
        }
      },
      
      deleteRow : function(rowNumber, deleteNumber) // This function works exactly like array.splice
      {
        var row;
        deleteNumber = deleteNumber || 1;
        var rowIndex = rowNumber - 1;
        var exitCondition = rowIndex + deleteNumber;
        
        for (; rowIndex < exitCondition; ++rowIndex) {
          row = this._rows[rowIndex];
          if (row == undefined) {
            deleteNumber = rowIndex - (rowNumber-1);
            break;
          }
          
          this._tBody.removeChild(row);
        }
        
        this._rows.splice(rowNumber-1, deleteNumber);
      },
      
      getRow : function(rowNumber)
      {
        if (rowNumber == 0)
          return this.getHeaderRow();
        
        if (Mojo.Util.isValid(rowNumber))
          return this._rows[rowNumber-1];
        else
          return this._rows;
      },
      
      getHeaderRow : function() {
        return this._headerRow;
      },
      
      getRows : function() {
        return this.getRow();
      },
      
      getColumn : function(columnNumber) {
        if (Mojo.Util.isValid(columnNumber))
          return this._columns[columnNumber-1];
        else
          return this._columns;
      },
      
      getColumns : function() {
        return this.getColumn();
      },
      
      getNumberOfRows : function() {
        return this._rows.length;
      },
      
      render : function(p) {
        this.$render(p);
        
        this.__acceptArray(this._dataSource.getColumns(), this._dataSource.getData());
      },
      
      __acceptArray : function(columns, array)
      {
        this.addHeader(columns);
        
        var v;
        for (var k = 0; k < array.length; ++k)
        {
          this.addRow( array[k] );
        }
        
        for (var k = 0; k < columns.length; ++k) {
          this.addColumn(k+1);
        }
        
        this._data = array;
      }
      
    }
    
  });
  
  return DataTable;

})();
