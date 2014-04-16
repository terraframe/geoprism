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

//define(["../../ClassFramework", "../../Util", "../RunwaySDK_UI", "../factory/runway/widget/Widget", "../datatable/datasource/InstanceQueryDataSource", "./CronPicker", "prettycron"], function(ClassFramework, Util, UI, Widget, InstanceQueryDataSource, CronPicker) {
(function(){

  var ClassFramework = Mojo.Meta;
  var Util = Mojo.Util;
  var Widget = com.runwaysdk.ui.factory.runway.Widget;
  var InstanceQueryDataSource = com.runwaysdk.ui.datatable.datasource.InstanceQueryDataSource;
  var FormEntry = com.runwaysdk.geodashboard.FormEntry;
  var Form = com.runwaysdk.geodashboard.Form;
  
  // In miliseconds
  var JOBS_POLLING_INTERVAL = 1000;
  var HISTORY_POLLING_INTERVAL = 6000;

  var JOB_QUERY_TYPE = "com.runwaysdk.system.scheduler.ExecutableJob";
  
  var schedulerName = 'com.runwaysdk.geodashboard.scheduler.Scheduler';
  var jobTableName = 'com.runwaysdk.geodashboard.scheduler.JobTable';
  var jobHistoryTableName = "com.runwaysdk.geodashboard.scheduler.JobHistoryTable";
  
  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage(schedulerName, {
    "jobs" : "Jobs",
    "history" : "History",
  });
    
  com.runwaysdk.Localize.defineLanguage(jobTableName, {
    "editJobTitle" : "Edit Job",
    "scheduledRun" : "Scheduled Run",
    "submit" : "Submit",
    "cancel" : "Cancel",
    "never" : "Never",
    "progress" : "Progress",
    
    // The metadata for MdMethods is not included in the Javascript query results, which is why we have to hardcode these values here (for now at least).
    "start" : "Start",
    "stop" : "Stop",
    "pause" : "Pause",
    "resume" : "Resume",
    
    // When we refactor the Job metadata from flags into a status enum these will be removed.
    "stopped" : "Stopped",
    "status" : "Status",
    "sSortAscending" : ": activate to sort column ascending",
    "sSortDescending" : ": activate to sort column descending",
    "sFirst" : "First",
    "sLast" : "Last",
    "sNext" : "Next",
    "sPrevious" : "Previous",
    "sEmptyTable" : "No data available in table",
    "sInfo" : "Showing _START_ to _END_ of _TOTAL_ entries",
    "sInfoEmpty" : "Showing 0 to 0 of 0 entries",
    "sInfoFiltered" : "(filtered from _MAX_ total entries)",
    "sLengthMenu" : "Show _MENU_ entries",
    "sLoadingRecords" : "Loading...",
    "sProcessing" : "Processing...",
    "sSearch" : "Search:",
    "sZeroRecords" : "No matching records found"    
  });
  
  com.runwaysdk.Localize.defineLanguage(jobHistoryTableName, {
    "duration" : "Duration",
    "problems" : "Problems",
    "seconds" : "seconds",
    "sSortAscending" : ": activate to sort column ascending",
    "sSortDescending" : ": activate to sort column descending",
    "sFirst" : "First",
    "sLast" : "Last",
    "sNext" : "Next",
    "sPrevious" : "Previous",
    "sEmptyTable" : "No data available in table",
    "sInfo" : "Showing _START_ to _END_ of _TOTAL_ entries",
    "sInfoEmpty" : "Showing 0 to 0 of 0 entries",
    "sInfoFiltered" : "(filtered from _MAX_ total entries)",
    "sLengthMenu" : "Show _MENU_ entries",
    "sLoadingRecords" : "Loading...",
    "sProcessing" : "Processing...",
    "sSearch" : "Search:",
    "sZeroRecords" : "No matching records found"    
  });
  
  var JobTable = ClassFramework.newClass(jobTableName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(config) {
        
        this.$initialize("table");
        
        this._config = config;
        
      },
      
      _onClickStartJob : function(contextMenu, contextMenuItem, mouseEvent) {
        var row = contextMenu.getTarget();
        var table = row.getParentTable();
        var jobDTO = table.getDataSource().getResultsQueryDTO().getResultSet()[row.getRowNumber()];
        var that = this;
        
        jobDTO.start(new Mojo.ClientRequest({
          onSuccess : function() {
            
          },
          onFailure : function(ex) {
            that.handleException(ex);
          }
        }));
      },
      
      _onClickStopJob : function(contextMenu, contextMenuItem, mouseEvent) {
        var row = contextMenu.getTarget();
        var table = row.getParentTable();
        var resultsQueryDTO = table.getDataSource().getResultsQueryDTO();
        var jobDTO = resultsQueryDTO.getResultSet()[row.getRowNumber()];
        var that = this;
        
        jobDTO.stop(new Mojo.ClientRequest({
          onSuccess : function() {
            
          },
          onFailure : function(ex) {
            that.handleException(ex);
          }
        }));
      },
      
      _onClickPauseJob : function(contextMenu, contextMenuItem, mouseEvent) {
        var row = contextMenu.getTarget();
        var table = row.getParentTable();
        var jobDTO = table.getDataSource().getResultsQueryDTO().getResultSet()[row.getRowNumber()];
        var that = this;
        
        jobDTO.pause(new Mojo.ClientRequest({
          onSuccess : function() {
            
          },
          onFailure : function(ex) {
            that.handleException(ex);
          }
        }));
      },
      
      _onClickResumeJob : function(contextMenu, contextMenuItem, mouseEvent) {
        var row = contextMenu.getTarget();
        var table = row.getParentTable();
        var jobDTO = table.getDataSource().getResultsQueryDTO().getResultSet()[row.getRowNumber()];
        var that = this;
        
        jobDTO.resume(new Mojo.ClientRequest({
          onSuccess : function() {
            
          },
          onFailure : function(ex) {
            that.handleException(ex);
          }
        }));
      },
      
      _openContextMenu : function(row, event) {
        
        var jobMetadata = this._table.getDataSource().getMetadataQueryDTO();
        var statusRowNum = 3;
        
        // Create Runway's Context Menu
        var cm = this.getFactory().newContextMenu(row);
        var start = cm.addItem(this.localize("start"), "add", Mojo.Util.bind(this, this._onClickStartJob));
        var stop = cm.addItem(this.localize("stop"), "delete", Mojo.Util.bind(this, this._onClickStopJob));
        var pause = cm.addItem(this.localize("pause"), "edit", Mojo.Util.bind(this, this._onClickPauseJob));
        var resume = cm.addItem(this.localize("resume"), "refresh", Mojo.Util.bind(this, this._onClickResumeJob));
        
        var completed = jobMetadata.getAttributeDTO("completed").getAttributeMdDTO().getDisplayLabel();
        var stopped = this.localize("stopped");
        var canceled = jobMetadata.getAttributeDTO("canceled").getAttributeMdDTO().getDisplayLabel();
        var running = jobMetadata.getAttributeDTO("running").getAttributeMdDTO().getDisplayLabel();
        var paused = jobMetadata.getAttributeDTO("paused").getAttributeMdDTO().getDisplayLabel();
        
        var status = row.getChildren()[statusRowNum].getInnerHTML();
        if (status === completed || status === stopped || status === canceled) {
          stop.setEnabled(false);
          pause.setEnabled(false);
          resume.setEnabled(false);
        }
        else if (status === running) {
          start.setEnabled(false);
          resume.setEnabled(false);
        }
        else if (status === paused) {
          start.setEnabled(false);
          pause.setEnabled(false);
        }
        
        cm.render();
        
        row.addClassName("row_selected");
        cm.addDestroyEventListener(function() {
          row.removeClassName("row_selected");
        });
        
        return false; // Prevents default (displaying the browser context menu)
      },
      
      _openEditMenu : function(row, job) {
        var dialog = this.getFactory().newDialog(this.localize("editJobTitle"), {minWidth:730});
        
        row.addClassName("row_selected");
        dialog.addDestroyEventListener(function() {
          row.removeClassName("row_selected");
        });

        var form = new Form();
                    
        if(job.isDescriptionWritable())
        {
          var descriptionInput = FormEntry.newInput('textarea', 'description', {attributes:{type:'text', id:'description'}});
          descriptionInput.setValue(job.getDescription().getLocalizedValue());
          form.addFormEntry(job.getDescriptionMd(), descriptionInput);          
        }
        else if(job.isDescriptionReadable())
        {
          var label = job.getDescriptionMd().getDisplayLabel();        
          var entry = new com.runwaysdk.geodashboard.ReadEntry('description', label, job ? job.getLocalizedValue() : "");
          form.addEntry(entry);                  
        }
        
        if(job.isCronExpressionWritable())
        {
          var entry = new com.runwaysdk.geodashboard.CronEntry("cron");
          entry.setValue(job.getCronExpression());
          
          form.addEntry(entry);
        }
        
        dialog.appendContent(form);
        
        var that = this;
        
        var handleSubmit = Mojo.Util.bind(this, function(){
          var values = form.getValues();
            
          job.getDescription().localizedValue = values.get("description");
          job.setCronExpression(values.get("cron"));
            
          var applyCallback = new Mojo.ClientRequest({
            onSuccess : function() {
              dialog.close();
            },
            onFailure : function(ex) {
              that.handleException(ex);
            }
          });
            
          job.apply(applyCallback);
        });
        
        
        var handleCancel = Mojo.Util.bind(this, function () {       
          var unlockCallback = new Mojo.ClientRequest({
            onSuccess : function(user) {
              dialog.close();
            },
            onFailure : function(ex) {
              that.handleException(ex);
            }
          });
    
          job.unlock(unlockCallback);  
        });
        
                
        dialog.addButton(that.localize("submit"), handleSubmit, null, {class:'btn btn-primary'});
        dialog.addButton(that.localize("cancel"), handleCancel, null, {class:'btn'});                        
        dialog.render();
        
        if (jcf != null && jcf.customForms != null) {
          jcf.customForms.replaceAll();
        }
            
        return false;
      },

      _lockRow : function (row, event) {
        var table = row.getParentTable();
        var job = table.getDataSource().getResultsQueryDTO().getResultSet()[row.getRowNumber()];        
      
        var lockCallback = new Mojo.ClientRequest({
          that : this,
          onSuccess : function(ret) {
            this.that._openEditMenu(row, ret);
          },
          onFailure : function(ex) {
            this.that.handleException(ex);
          }
        });
        
        job.lock(lockCallback);
      },      
      
      _onNewRowEvent : function(newRowEvent) {
        var row = newRowEvent.getRow();
        
        var onContextMenu =  Mojo.Util.bind(this, function(event) {
          this._openContextMenu(row, event);
          
          return false;
        });

        var onClick =  Mojo.Util.bind(this, function(event) {
          this._lockRow(row, event);

          return false;
        });
                
        row.addEventListener("click", onClick);
        row.addEventListener("contextmenu", onContextMenu);
      },
      
      formatStatus : function(jobDTO) {
        
        var jobMetadata = this._table.getDataSource().getMetadataQueryDTO();
        
        var completed = jobMetadata.getAttributeDTO("completed").getAttributeMdDTO().getDisplayLabel();
        var stopped = this.localize("stopped");
        var canceled = jobMetadata.getAttributeDTO("canceled").getAttributeMdDTO().getDisplayLabel();
        var running = jobMetadata.getAttributeDTO("running").getAttributeMdDTO().getDisplayLabel();
        var paused = jobMetadata.getAttributeDTO("paused").getAttributeMdDTO().getDisplayLabel();
        
        if (jobDTO.getRunning()) { return running; }
        if (jobDTO.getCompleted()) { return completed; }
        if (jobDTO.getPaused()) { return paused; }
        if (jobDTO.getCanceled()) { return canceled; }
          
        return stopped;
      },
      
      formatProgress : function(jobDTO) {
        if (jobDTO.getWorkTotal() == null || jobDTO.getWorkProgress() == null || jobDTO.getWorkTotal() == 0) {
          return null;
        }
        
        return ((jobDTO.getWorkProgress() / jobDTO.getWorkTotal()) * 100) + "%";
      },
      
      formatScheduledRun : function(jobDTO) {
        var cronStr = jobDTO.getCronExpression();
        
        if (cronStr == null || cronStr === "") {
          return this.localize("never");
        }
        else {
          return prettyCron.toString(cronStr);
        }
      },
      
      render : function(parent) {
        
        var ds = new InstanceQueryDataSource({
          className: JOB_QUERY_TYPE,
          columns: [
            { queryAttr: "jobId" },
            { queryAttr: "description",  customFormatter: function(jobDTO){ return jobDTO.getDescription().getLocalizedValue(); } },
            { header: this.localize("progress"), customFormatter: Mojo.Util.bind(this, this.formatProgress) },
            { header: this.localize("status"), customFormatter: Mojo.Util.bind(this, this.formatStatus) },
//            { header: this.localize("scheduledRun"), customFormatter: function(job) {return job.getCronExpression();}}
            { header: this.localize("scheduledRun"), customFormatter: function(job) {
                return com.runwaysdk.geodashboard.CronUtil.cronToHumanReadable(job.getCronExpression());
              }
            }
          ]
        });
        
        // Create the DataTable impl
        this._config.el = this;
        this._config.dataSource = ds;
        this._config.sDom = '<"top"i>rt<"bottom"lp><"clear">';
        
        // Localize the datatable widget
        this._config.oLanguage = {
          oAria: {
            sSortAscending: this.localize("sSortAscending"),
            sSortDescending: this.localize("sSortDescending")
          },
          oPaginate: {
            sFirst: this.localize("sFirst"),
            sLast: this.localize("sLast"),
            sNext: this.localize("sNext"),
            sPrevious: this.localize("sPrevious")
          },
          sEmptyTable: this.localize("sEmptyTable"),
          sInfo: this.localize("sInfo"),
          sInfoEmpty: this.localize("sInfoEmpty"),
          sInfoFiltered: this.localize("sInfoFiltered"),
          sLengthMenu: this.localize("sLengthMenu"),
          sLoadingRecords: this.localize("sLoadingRecords"),
          sProcessing: this.localize("sProcessing"),
          sSearch: this.localize("sSearch"),
          sZeroRecords: this.localize("sZeroRecords")
        };        
        
        
        this._table = this.getFactory().newDataTable(this._config);
        
        this._table.addNewRowEventListener(Mojo.Util.bind(this, this._onNewRowEvent));
        
        this._table.render(parent);
        
        var that = this;
        this._pollingRequest = new com.runwaysdk.ui.PollingRequest({
          callback: {
            onSuccess: function(data) {
              
            },
            onFailure: function(ex) {
              that.handleException(ex);
            }
          },
          performRequest : function(callback) {
            that._table.refresh(callback);
          },
          pollingInterval : JOBS_POLLING_INTERVAL
        });
        
        this._pollingRequest.enable();
      },
      
      getPollingRequest : function() {
        return this._pollingRequest;
      },
      
      destroy : function() {
        this.$destroy();
        this._pollingRequest.destroy();
      }
    }    
  });

  var JobHistoryTable = ClassFramework.newClass(jobHistoryTableName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(config) {
        
        this.$initialize("table");
        
        this._config = config;
        
      },
      
      getPollingRequest : function() {
        return this._pollingRequest;
      },
      
      render : function(parent) {
        var that = this;
        
        var ds = new com.runwaysdk.ui.datatable.datasource.MdMethodDataSource({
          method : function(clientRequest) {
            com.runwaysdk.system.scheduler.JobHistoryView.getJobHistories(clientRequest, this.getSortAttr(), this.isAscending(), this.getPageSize(), this.getPageNumber());
          },
          columns : [
                     {queryAttr: "lastRun"},
                     {queryAttr: "jobId"},
                     {header: this.localize("duration"), customFormatter: function(view) { return ((view.getEndTime() - view.getStartTime()) / 1000) + " " + that.localize("seconds") + "."; }},
                     {queryAttr: "description"},
                     {header: this.localize("problems"), customFormatter : function(view) {
                       // This may be a workaround to a bug in runway, the value isn't getting set to the localized value.
                       return view.getAttributeDTO('historyInformation').getValue();
//                       return view.getHistoryInformation().getLocalizedValue();
                     }}
                    ]
        });
        
        // Create the DataTable impl
        this._config["iDisplayLength"] = 5;
        this._config.el = this;
        this._config.dataSource = ds;
        this._config.sDom = '<"top"i>rt<"bottom"lp><"clear">';
        // Localize the datatable widget
        
        this._table = this.getFactory().newDataTable(this._config);
        this._table.render(parent);
        
        this._pollingRequest = new com.runwaysdk.ui.PollingRequest({
          callback: {
            onSuccess: function(data) {
              
            },
            onFailure: function(ex) {
              that.handleException(ex);
            }
          },
          performRequest : function(callback) {
            that._table.refresh(callback);
          },
          pollingInterval : HISTORY_POLLING_INTERVAL
        });
      }
    }
  });
  
  var scheduler = ClassFramework.newClass(schedulerName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(config) {
        this._tabPanel = this.getFactory().newTabPanel();
        
        this.$initialize(this._tabPanel);
        
        this._config = config || {};
        this._config.language = this._config.language || {};
        Util.merge(com.runwaysdk.Localize.getLanguage(schedulerName), this._config.language);
        
        this._jobTable = new JobTable(this._config);
        this._tabPanel.addPanel(this.localize("jobs"), this._jobTable);
        
        this._historyTable = new JobHistoryTable(this._config);
        this._tabPanel.addPanel(this.localize("history"), this._historyTable);
        
        this._tabPanel.addSwitchPanelEventListener(Mojo.Util.bind(this, this.onSwitchPanel));
      },
      
      onSwitchPanel : function(switchPanelEvent) {
        var panel = switchPanelEvent.getPanel();
        
        if (panel.getPanelNumber() === 0) { // Jobs
          this._jobTable.getPollingRequest().enable();
          this._historyTable.getPollingRequest().disable();
        }
        else if (panel.getPanelNumber() === 1) { // History
          this._jobTable.getPollingRequest().disable();
          this._historyTable.getPollingRequest().enable();
        }
      },
      
      render : function(parent) {
        this._tabPanel.render(parent);
      }
    }
  });
    
  return scheduler;
  
})();
