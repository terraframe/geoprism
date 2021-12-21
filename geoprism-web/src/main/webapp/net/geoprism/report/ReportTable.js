/*
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
//define(["../../ClassFramework", "../../Util", "../RunwaySDK_UI", "../factory/runway/widget/Widget", "../datatable/datasource/ViewQueryDataSource"], function(ClassFramework, Util, UI, Widget, ViewQueryDataSource) {
(function(){  

  var ClassFramework = Mojo.Meta;
  var Widget = com.runwaysdk.ui.factory.runway.Widget;
  var ViewQueryDataSource = com.runwaysdk.ui.datatable.datasource.ViewQueryDataSource;
  var GenericDataTable = com.runwaysdk.ui.factory.generic.datatable.DataTable;
  
  var defaultQueryType = "net.geoprism.report.ReportItemView";
  
  var tableClassName = "net.geoprism.report.ReportTable";
  
  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage(tableClassName, {
    "newReport" : "New",
    "editReport" : "Edit",
    "deleteReport" : "Delete",
    "dialogEditTitle" : "Edit report",
    "dialogViewTitle" : "View report",
    "submit" : "Submit",
    "cancel" : "Cancel",
    "close" : "Close",
    "dialogNewTitle" : "New report",
    "dialogDeleteTitle" : "Confirm Delete",
    "delete" : "Delete",
    "edit" : "Edit",
    "deleteText" : "Are you sure you want to delete the report ",
  });
  
  var reportsTable = ClassFramework.newClass(tableClassName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(cfg) {
        
        cfg = cfg || {};
        cfg.queryType = cfg.queryType || defaultQueryType;
        this._config = cfg;        
                
        this.$initialize("div");        
      },
      
      _onEdit : function(e) {
        var target = e.getTarget();
        var id = target.id;        
        var rowNumber = id.replace('table-edit-', '');
            
        var reportView = this._table.getDataSource().getResultsQueryDTO().getResultSet()[rowNumber];    	  
        var id = reportView.getReportId();
        
        var that = this;
        
        var config = {
          type: 'net.geoprism.report.ReportItem',
          action: "update",
          viewAction: "edit",
          viewParams: {id: id},          
          width: 600,
          onSuccess : function(dto) {
            that._table.refresh();
          },
          onFailure : function(e) {
            that.handleException(e);
          }
        };
          
        new com.runwaysdk.ui.RunwayControllerFormDialog(config).render();    	  
      },
      
      _onNew: function(e) {
        var that = this;
          
        var config = {
          type: 'net.geoprism.report.ReportItem',
          action: "create",
          viewAction: "newInstance",
          width: 600,
          onSuccess : function(dto) {
              that._table.refresh();
            },
            onFailure : function(e) {
              that.handleException(e);
            }
          };
          
          new com.runwaysdk.ui.RunwayControllerFormDialog(config).render();    	  
      },     
            
      _onDelete: function(e) {
        var fac = this.getFactory();
        var table = this._table;
        
        var target = e.getTarget();
        var id = target.id;        
        var rowNumber = id.replace('table-delete-', '');
        
        var report = table.getDataSource().getResultsQueryDTO().getResultSet()[rowNumber];
        
        var dialog = fac.newDialog(this.localize("dialogDeleteTitle"));
        dialog.appendContent(this.localize("deleteText") + report.getReportLabel() + "?");
        
        var Structure = com.runwaysdk.structure;
        var tq = new Structure.TaskQueue();
        var that = this;
        
        tq.addTask(new Structure.TaskIF({
          start : function(){
            dialog.addButton(that.localize("delete"), function() { tq.next(); }, null, {class:'btn btn-primary'});
            
            var cancelCallback = function() {
              dialog.close();
              tq.stop();
            };

            dialog.addButton(that.localize("cancel"), cancelCallback, null, {class:'btn'});            
            dialog.render();
          }
        }));
        
        tq.addTask(new Structure.TaskIF({
          start : function(){
            dialog.close();
            var mainDiv = document.getElementById("reportTable");
            
            var removeCallback = new net.geoprism.StandbyClientRequest({
              onSuccess : function() {
                table.refresh();
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);
              }
            }, mainDiv);
            
            report.remove(removeCallback);
          }
        }));
        
        tq.start();
      },
      
      _makeButtons : function() {
        var newReport = this.getFactory().newButton(this.localize("newReport"), Mojo.Util.bind(this, this._onNew));
        newReport.setAttribute('class', 'btn btn-primary');
        
        this.appendChild(newReport);
      },
      
      _clickHandler : function(e) {        
        var target = e.getTarget();
        var klass = target.getAttribute('class');
        
        if(klass.indexOf('table-edit') !== -1)
        {
          e.preventDefault();
            
          this._onEdit(e);
        }
        else if (klass.indexOf('table-delete') !== -1)
        {
          e.preventDefault();
          
          this._onDelete(e);
        }      
      },
      
      render : function(parent) {
        this._makeButtons();
        
        // Build the columns from the read permissions
        var report = new net.geoprism.report.ReportItemView();
        var columns = [];        
        
        columns.push({header: this.localize("edit"), customFormatter: Mojo.Util.bind(this, function(result, rowNumber){
            return '<a href="#" class="ico-edit table-edit" id="table-edit-' + rowNumber + '">' + this.localize("edit") +'</a>';
          })
        });
        
        columns.push({header: this.localize("delete"), customFormatter: Mojo.Util.bind(this, function(result, rowNumber){
            return '<a href="#" class="ico-remove table-delete" id="table-delete-' + rowNumber + '">' + this.localize("delete") +'</a>';
          })
        });
        
        if(report.isReportNameReadable())
        {          
          columns.push({queryAttr: "reportName"});        
        }
          
        if(report.isReportLabelReadable())
        {
          columns.push({queryAttr: "reportLabel"});        
        }
        
        if(report.isDashboardLabelReadable())
        {
          columns.push({queryAttr: "dashboardLabel"});        
        }
        
        this._config.dataSource = new ViewQueryDataSource({
          className: this._config.queryType,
          columns: columns
        });
        
        this._config.selectableRows = false;
        
        // Overwrite the column definitions for the edit and delete columns
        this._config.aoColumnDefs = [
          { "bSortable": false, "aTargets": [ 0 ], "sClass": "center", "sTitle":"", "sWidth":"25px" },
          { "bSortable": false, "aTargets": [ 1 ], "sClass": "center", "sTitle":"", "sWidth":"25px" }
        ];
        
        // Remove the search control from the table
        this._config.sDom = '<"top"i>rt<"bottom"lp><"clear">';
        
        this._table = new GenericDataTable(this._config);
        this._table.addEventListener('click', Mojo.Util.bind(this, this._clickHandler));
        this.appendChild(this._table);
        
        this.$render(parent);        
      }      
    }
    
  });
  
  return reportsTable;
  
})();
