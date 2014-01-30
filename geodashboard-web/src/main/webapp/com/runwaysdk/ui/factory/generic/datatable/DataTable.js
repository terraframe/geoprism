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

//define(["../../../../../ClassFramework", "../../../../../Util", "./DataSourceFactory", "./BaseServerDataSource", "./Events"], function(ClassFramework, Util, DataSourceFactory, BaseServerDataSource, Events) {
(function(){  

  var ClassFramework = Mojo.Meta;
  var Util = Mojo.Util;
  var Widget = com.runwaysdk.ui.factory.runway.Widget;
  
  var dataTable = ClassFramework.newClass('com.runwaysdk.ui.factory.generic.datatable.DataTable', {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(cfg)
      {
        // ! This class implements functionality for config.selectableRows (see render). !
        
        this.$initialize("table");
        
        cfg.el = this;
        this._config = cfg;
        
        this._impl = this.getFactory().newDataTable(cfg);
        
        this._selectedRow = null;
      },
      
      getImpl : function() {
        return this._impl;
      },
      
      addRow : function(rowData) {
        this.getImpl().addRow(rowData);
      },
      
      getNumberOfRows : function() {
        return this.getImpl().getNumberOfRows();
      },
      
      addNewRowEventListener : function(fnListener) {
        this.getImpl().addNewRowEventListener(fnListener);
      },
      
      addNewHeaderRowEventListener : function(fnListener) {
        this.getImpl().addNewHeaderRowEventListener(fnListener);
      },
      
      deleteRow : function(rowNum) {
        this.getImpl().deleteRow(rowNum);
      },
      
      getDataSource : function() {
        return this.getImpl().getDataSource();
      },
      
      getImpl : function() {
        return this._impl;
      },
      
      refresh : function(callback) {
        this.getImpl().refresh(callback);
      },
      
      updateRow : function(rowData, rowNum) {
        this.getImpl().updateRow(rowData, rowNum);
      },
      
      _onNewRowEvent : function(newRowEvent) {
        var row = newRowEvent.getRow();
        
        row.addEventListener("click", Mojo.Util.bind(this, function() {
          if (this._selectedRow === row) {
            row.setSelected(false);
            this._selectedRow = null;
            return;
          }
          else if (this._selectedRow != null) {
            this._selectedRow.setSelected(false);
          }
          
          row.setSelected(true);
          this._selectedRow = row;
        }));
      },
      
      getSelectedRow : function() {
        return this._selectedRow;
      },
      
      render : function(parent) {
        
        if (this._config.selectableRows) {
          this.addNewRowEventListener(Mojo.Util.bind(this, this._onNewRowEvent));
        }
        
        this.getImpl().render(parent);
      }
    }
  });
  
})();