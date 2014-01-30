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
  var schedulerPackage = Mojo.Meta.alias("com.runwaysdk.ui.scheduler.*");
  
  // In miliseconds
  var JOBS_POLLING_INTERVAL = 1000;
  var HISTORY_POLLING_INTERVAL = 6000;

  var JOB_QUERY_TYPE = "com.runwaysdk.system.scheduler.ExecutableJob";
  var HISTORY_QUERY_TYPE = "com.runwaysdk.system.scheduler.JobHistory";
  
  var schedulerName = 'com.runwaysdk.ui.scheduler.Scheduler';
  var jobTableName = 'com.runwaysdk.ui.scheduler.JobTable';
  var jobHistoryTableName = "com.runwaysdk.ui.scheduler.JobHistoryTable";
  
  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage(schedulerName, {
    "jobs" : "Jobs",
    "history" : "History"
  });
  com.runwaysdk.Localize.defineLanguage(jobTableName, {
    "start" : "Start",
    "stop" : "Stop",
    "pause" : "Pause",
    "resume" : "Resume",
    "completed" : "Completed",
    "running" : "Running",
    "paused" : "Paused",
    "canceled" : "Canceled",
    "editJobTitle" : "Edit Job",
    "scheduledRun" : "Scheduled Run",
    "description" : "Description",
    "submit" : "Submit",
    "cancel" : "Cancel",
    "never" : "Never",
    "progress" : "Progress",
    "status" : "Status"
  });
  com.runwaysdk.Localize.defineLanguage(jobHistoryTableName, {
    "duration" : "Duration",
    "problems" : "Problems",
    "seconds" : "seconds"
  });
  
  var scheduler = ClassFramework.newClass(schedulerName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(cfg) {
        this._tabPanel = this.getFactory().newTabPanel();
        
        this.$initialize(this._tabPanel);
        
        this._jobTable = new JobTable(cfg);
        this._tabPanel.addPanel(this.localize("jobs"), this._jobTable);
        
        this._historyTable = new JobHistoryTable();
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
  
  var JobTable = ClassFramework.newClass(jobTableName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(cfg) {
        
        this.$initialize("table");
        
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
        var jobDTO = table.getDataSource().getResultsQueryDTO().getResultSet()[row.getRowNumber()];
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
      
      _openContextMenu : function(mouseEvent) {
        var fac = this.getFactory();
        var row = mouseEvent.getTarget().getParent();
        var statusRowNum = 3;
        
        var cm = fac.newContextMenu(row);
        var start = cm.addItem(this.localize("start"), "add", Mojo.Util.bind(this, this._onClickStartJob));
        var stop = cm.addItem(this.localize("stop"), "delete", Mojo.Util.bind(this, this._onClickStopJob));
        var pause = cm.addItem(this.localize("pause"), "edit", Mojo.Util.bind(this, this._onClickPauseJob));
        var resume = cm.addItem(this.localize("resume"), "refresh", Mojo.Util.bind(this, this._onClickResumeJob));
        
        var status = row.getChildren()[statusRowNum].getInnerHTML();
        if (status === this.localize("completed") || status === this.localize("stopped") || status === this.localize("canceled")) {
          stop.setEnabled(false);
          pause.setEnabled(false);
          resume.setEnabled(false);
        }
        else if (status === this.localize("running")) {
          start.setEnabled(false);
          resume.setEnabled(false);
        }
        else if (status === this.localize("paused")) {
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
      
      _openEditMenu : function(mouseEvent) {
        var fac = this.getFactory();
        var row = mouseEvent.getTarget().getParent();
        var table = row.getParentTable();
        var jobDTO = table.getDataSource().getResultsQueryDTO().getResultSet()[row.getRowNumber()];
        
        var dialog = fac.newDialog(this.localize("editJobTitle"), {width: "500px"});
        
        row.addClassName("row_selected");
        dialog.addDestroyEventListener(function() {
          row.removeClassName("row_selected");
        });
        
        var form = this.getFactory().newForm();
        
        var descriptionInput = this.getFactory().newFormControl('textarea', 'description');
        descriptionInput.setValue(jobDTO.getDescription().getLocalizedValue());
        form.addEntry(this.localize("description"), descriptionInput);
        
        var cronInput = new schedulerPackage.CronInput("cron");
        cronInput.setValue(jobDTO.getCronExpression());
        form.addEntry(this.localize("scheduledRun"), cronInput);
        
        dialog.appendContent(form);
        
        var Structure = com.runwaysdk.structure;
        var tq = new Structure.TaskQueue();
        
        var that = this;
        
        tq.addTask(new Structure.TaskIF({
          start : Mojo.Util.bind(this, function(){
            dialog.addButton(this.localize("submit"), function() { tq.next(); });
            
            var cancelCallback = function() {
              dialog.close();
              tq.stop();
            };
            dialog.addButton(this.localize("cancel"), cancelCallback);
            
            dialog.render();
          })
        }));
        
        tq.addTask(new Structure.TaskIF({
          start : Mojo.Util.bind(this, function(){
            dialog.close();
            
            var lockCallback = new Mojo.ClientRequest({
              onSuccess : function(retJobDTO) {
                jobDTO = retJobDTO;
                tq.next();
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);
              }
            });
            
            com.runwaysdk.Facade.lock(lockCallback, jobDTO.getId());
          })
        }));
        
        tq.addTask(new Structure.TaskIF({
          start : Mojo.Util.bind(this, function(){
            var values = form.accept(fac.newFormControl('FormVisitor'));
            
            jobDTO.getDescription().localizedValue = values.get("description");
            jobDTO.setCronExpression(values.get("cron"));
            
            var applyCallback = new Mojo.ClientRequest({
              onSuccess : function() {
                // Intentionally empty
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);
              }
            });
            
            jobDTO.apply(applyCallback);
          })
        }));
        
        tq.start();
        
        return false;
      },
      
      _onNewRowEvent : function(newRowEvent) {
        var fac = this.getFactory();
        var row = newRowEvent.getRow();
        
        var progressRowNum = 2;
        
        if (row.getRowNumber() == progressRowNum) {
          // TODO create a progress bar widget
        }
        
        row.addEventListener("dblclick", Mojo.Util.bind(this, this._openEditMenu));
        row.addEventListener("contextmenu", Mojo.Util.bind(this, this._openContextMenu));
      },
      
      formatStatus : function(jobDTO) {
        if (jobDTO.getRunning()) { return this.localize("running"); }
        if (jobDTO.getCompleted()) { this.localize("completed"); }
        if (jobDTO.getPaused()) { return this.localize("paused"); }
        if (jobDTO.getCanceled()) { return this.localize("canceled"); }
          
        return this.localize("stopped", "Stopped");
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
            { header: this.localize("status"), customFormatter: Mojo.Util.bind(this, this.formatStatus) }
//            { header: "Scheduled Run", customFormatter: this.formatScheduledRun }
          ]
        });
        
        this._table = this.getFactory().newDataTable({
          el : this,
          dataSource : ds
        });
        
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
      
      initialize : function(cfg) {
        this.$initialize("table");
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
                     {header: that.localize("duration"), customFormatter: function(view) { return ((view.getEndTime() - view.getStartTime()) / 1000) + " " + that.localize("seconds") + "."; }},
                     {queryAttr: "description"},
                     {header: that.localize("problems"), customFormatter : function(view) {
                       // This may be a workaround to a bug in runway, the value isn't getting set to the localized value.
                       return view.getAttributeDTO('historyInformation').getValue();
//                       return view.getHistoryInformation().getLocalizedValue();
                     }}
                    ]
        });
        
        this._table = this.getFactory().newDataTable({
          el : this,
          dataSource : ds,
          "iDisplayLength": 5
        });
        
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
  
  return scheduler;
  
})();
