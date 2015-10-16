/*
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
(function(){  

  var ClassFramework = Mojo.Meta;
  var Widget = com.runwaysdk.ui.factory.runway.Widget;
  var InstanceQueryDataSource = com.runwaysdk.ui.datatable.datasource.InstanceQueryDataSource;
  var GenericDataTable = com.runwaysdk.ui.factory.generic.datatable.DataTable;
  var FormEntry = com.runwaysdk.geodashboard.FormEntry;
  var Form = com.runwaysdk.geodashboard.Form;
  
  var defaultQueryType = "com.runwaysdk.geodashboard.Dashboard";
  
  var dashboardTableName = "com.runwaysdk.ui.userstable.DashboardTable";
  
  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage(dashboardTableName, {
    "newDashboard" : "New",
    "editDashboard" : "Edit",
    "deleteDashboard" : "Delete",
    "dialogEditDashboardTitle" : "Edit Dashboard",
    "dialogViewDashboardTitle" : "View Dashboard",
    "submit" : "Submit",
    "cancel" : "Cancel",
    "close" : "Close",
    "dialogNewDashboardTitle" : "New Dashboard",
    "dialogDeleteDashboardTitle" : "Confirm Delete",
    "delete" : "Delete",
    "edit" : "Edit",
    "deleteText" : "Are you sure you want to delete dashboard [{0}]?"
  });
  
  var DashboardFormEvent = ClassFramework.newClass('com.runwaysdk.ui.userstable.DashboardFormBuilderEvent', {
    Constants : {
      APPLY_SUCCESS : 0,
      APPLY_FAILURE : 1,
      CANCEL_SUCCESS : 2,
      CANCEL_FAILURE : 3,
      ON_EDIT : 4
    },
    Instance : {
      initialize : function(eventType, user, container) {
        this._eventType = eventType;
        this._user = user;
        this._container = container;
      },
      
      getEventType : function(){
        return this._eventType;
      },
      
      getDashboard : function(){
      return this._user;
      },
      
      getContainer : function(){
      return this._container;
      }
    }
  });
  
  ClassFramework.newClass('com.runwaysdk.ui.userstable.DashboardFormBuilder', {      
      
    Instance : {
       
      initialize : function(factory, dashboard, info) {
        this._factory = factory;
        this._dashboard = dashboard;
        this._info = info;
        this._listeners = [];
      },
      
      addListener : function (listener)
      {
        this._listeners.push(listener);
      },
      
      getListeners : function()
      {
        return this._listeners;  
      },
      
      fireEvent : function(eventType, dashboard, container)
      {
        for(var i = 0; i < this._listeners.length; i++) {
          this._listeners[i].handleEvent(new DashboardFormEvent(eventType, dashboard, container));
        }  
      },
      
      getFactory : function()
      {
        return this._factory;  
      },
      
      localize : function (key)
      {
        return com.runwaysdk.Localize.localize(dashboardTableName, key);      
      },
      
      render : function(container, readOnly) {      
        var form = this._build(readOnly);
        var that = this;
      
        container.appendContent(form);
      
        if(this._dashboard.isWritable())
        {
          if(!readOnly)
          {          
            var submitCallback = function() {
              // Disable the buttons to prevent multiple submits
              document.getElementById('dashboard-submit').disabled = true;
              document.getElementById('dashboard-cancel').disabled = true;
        
              // Clear any existing error messages
              form.removeErrorMessages();
        
              var values = form.getValues();
         
              that._populateComponent(values);
        
              var applyCallback = new com.runwaysdk.geodashboard.StandbyClientRequest({
                onSuccess : function(dashboard) {
                  container.close();
                
                  that.fireEvent(DashboardFormEvent.APPLY_SUCCESS, dashboard, container);
                },
                onFailure : function(ex) {
                  form.handleException(ex);
            
                  document.getElementById('dashboard-submit').disabled = false;
                  document.getElementById('dashboard-cancel').disabled = false;
                 
                  that.fireEvent(DashboardFormEvent.APPLY_FAILURE, null, container);
                }
              }, container.getParentNode());

              that._dashboard.apply(applyCallback);
            };
      
            var cancelCallback = function() {
              if(!that._dashboard.isNewInstance())
              {                
                var unlockCallback = new com.runwaysdk.geodashboard.StandbyClientRequest({
                  onSuccess : function(dashboard) {
                    container.close();
                  
                    that.fireEvent(DashboardFormEvent.CANCEL_SUCCESS, dashboard, container);
                  },
                  onFailure : function(ex) {
                    form.handleException(ex);
                  
                    that.fireEvent(DashboardFormEvent.CANCEL_FAILURE, null, container);
                  }
                }, container.getParentNode());
          
                that._dashboard.unlock(unlockCallback);  
              }
              else
              {
                container.close();
              }
            };
      
            container.addButton(that.localize("submit"), submitCallback, null, {id:'dashboard-submit', class:'btn btn-primary'});
            container.addButton(that.localize("cancel"), cancelCallback, null, {id:'dashboard-cancel', class:'btn'});            
          }
          else
          {
            var editCallback = function() {
              that.fireEvent(DashboardFormEvent.ON_EDIT, that._dashboard, container);              
            };
          
            container.addButton(that.localize("edit"), editCallback, null, {id:'dashboard-edit', class:'btn btn-primary'});
          }
        }
        else if(this._dashboard.isReadable())
        {
          container.addButton(that.localize("close"), function(){container.close();}, null, {id:'dashboard-cancel', class:'btn btn-primary'});            
        }
      },
      
      _build : function(readOnly) {
        var that = this;
        var form = new Form();
          
        // Build the admin role section
        form.appendElement(this._newHeader(this.localize('dashboardInfo')));
          
        if(!readOnly && this._dashboard.isNameWritable())
        {
          var nameInput = FormEntry.newInput('text', 'name', {attributes:{type:'text', id:'name'}});
          nameInput.setValue(this._info.name);
          
          form.addFormEntry(this._dashboard.getNameMd(), nameInput);          
        }
        else if(this._dashboard.isFirstNameReadable())
        {
          var label = this._dashboard.getNameMd().getDisplayLabel();  
          
          var entry = new com.runwaysdk.geodashboard.ReadEntry('name', label, this._info.name);
          form.addEntry(entry);                  
        }
          
        if(!readOnly && this._dashboard.isDisplayLabelWritable())
        {
          var displayLabelInput = FormEntry.newInput('text', 'displayLabel', {attributes:{type:'text', id:'displayLabel'}});
          displayLabelInput.setValue(this._info.label);
          
          form.addFormEntry(this._dashboard.getDisplayLabelMd(), displayLabelInput);          
        }
        else if(this._dashboard.isDisplayLabelReadable())
        {
          var label = this._dashboard.getDisplayLabelMd().getDisplayLabel();  
          
          var entry = new com.runwaysdk.geodashboard.ReadEntry('displayLabel', label, this._info.label);
          form.addEntry(entry);                  
        }
        
        if(!readOnly && this._dashboard.isCountryWritable())
        {          
          var md = this._dashboard.getCountryMd();
          var entry = new com.runwaysdk.geodashboard.ListFormEntry('country', md.getDisplayLabel(), this._info.countries);
          
          form.addEntry(entry); 
        }
        else if(this._dashboard.isCountryReadable())
        {
          var label = this._dashboard.getCountryMd().getCountry();  
          
          $.each( this._info.countries, function( index, option ) {
            if(option.checked) {
              var entry = new com.runwaysdk.geodashboard.ReadEntry('country', label, option.displayLabel);
              form.addEntry(entry);            
            }
          });          
        }
        
        if(!readOnly && this._dashboard.isRemovableWritable())
        {
          var md = this._dashboard.getRemovableMd();
          var options = [
            {displayLabel:md.getPositiveDisplayLabel(), value:true, checked:this._dashboard.getRemovable()},
            {displayLabel:md.getNegativeDisplayLabel(), value:false, checked:!this._dashboard.getRemovable()}
          ];
          
          var entry = new com.runwaysdk.geodashboard.RadioFormEntry('removable', md.getDisplayLabel(), options);
            
          form.addEntry(entry); 
        }
        else if(this._dashboard.isRemovableReadable())
        {
          var label = this._dashboard.getRemovableMd().getRemovable();  
          
          var entry = new com.runwaysdk.geodashboard.ReadEntry('removable', label, this._info.removable);
          form.addEntry(entry);                  
        }
                          
        return form;
      },
        
      _newHeader : function(displayLabel) {
        // Build the admin role section
        var header = this.getFactory().newElement('h2');
        header.setInnerHTML(displayLabel);

        return header;
      },
                    
      _populateComponent : function (values) {
        if(this._dashboard.isNameWritable())
        {          
          this._dashboard.setName(values.get("name"));
        }
            
        if(this._dashboard.isDisplayLabelWritable())
        {       
          this._dashboard.getDisplayLabel().setLocalizedValue(values.get("displayLabel"));                            
        }
                
        if(this._dashboard.isCountryWritable())
        {
          var country = values.get("country");
          
          this._dashboard.setCountry(country);
        }
            
        if(this._dashboard.isRemovableWritable())
        {
          var removable = values.get("removable");
          
          this._dashboard.setRemovable((removable[0] == 'true'));
        }
      }
    }
  });
  
  ClassFramework.newClass('com.runwaysdk.ui.userstable.DashboardFormConfiguator', {
    Instance : {
      get : function(factory, dashboard, info) {
        return new com.runwaysdk.ui.userstable.DashboardFormBuilder(factory, dashboard, info);
      },
        
      getJSON : function(callback, dashboard)
      {        
        dashboard.getJSON(callback);
      }
    }    
  });
  
  ClassFramework.newClass('com.runwaysdk.ui.userstable.DashboardForm', {
      
    Extends : Widget,
      
    Instance : {
        
      initialize : function(cfg) {
        cfg = cfg || {};
        this._config = cfg;        
        this._footer = null;
                  
        this.$initialize("div");
        
        this._configuator = (cfg.configuator || new com.runwaysdk.ui.userstable.DashboardFormConfiguator());
      },
      getFactory : function ()
      {
        return com.runwaysdk.ui.Manager.getFactory();
      },
      close : function ()
      {
        // Do nothing
      },
      addButton : function (label, callback, context, config)
      {
        var type = 'reset';
    
        if(this._footer === null) {
          this._footer = this.getFactory().newElement('footer');
          this._footer.setAttribute('class', 'bottom-bar');
          
          this._form.appendChild(this._footer);
          
          type = 'submit';          
        }
        
        var button = this.getFactory().newElement('input', {'value':label, 'type':type});
        button.setAttribute('class', config['class']);
        button.setAttribute('id', config['id']);
        button.addEventListener('click', function (e) {
          e.preventDefault();
          
          callback(e);
        });
          
        this._footer.appendChild(button);
      },
      appendContent : function(form) 
      {
        this._form = form;
        
        this.appendChild(form);
      },
      getParentNode : function()
      {
        return this.getRawEl();  
      },
      reload : function(dashboard, readOnly)
      {
        this.setInnerHTML('');
        this._footer = null;
        
        var builder = this._configuator.get(this.getFactory(), dashboard, null);
        builder.render(this, readOnly);
        builder.addListener(this);                      
      },
      handleEvent : function(e) {
        var that = this;
        var user = e.getDashboard();
        
        if(e.getEventType() === DashboardFormEvent.ON_EDIT) {
          var lockCallback = new com.runwaysdk.geodashboard.StandbyClientRequest({
            onSuccess : function(user) {
              that.reload(user, false);
            },
            onFailure : function(ex) {
              that.handleException(ex);
            }
          }, e.getContainer().getRawEl());
              
          user.lock(lockCallback);
        }
        else if (e.getEventType() === DashboardFormEvent.APPLY_SUCCESS || e.getEventType() === DashboardFormEvent.CANCEL_SUCCESS) {
          this.reload(user, true);          
        }
      },
      render : function(parent) {

        var Structure = com.runwaysdk.structure;
        var tq = new Structure.TaskQueue();
        var that = this;
        var user = null;
            
        // First load the user
        tq.addTask(new Structure.TaskIF({
          start : function(){
            
            var callback = new Mojo.ClientRequest({
              onSuccess : function(dto) {
                user = dto;
                tq.next();
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);               
              }
            }); 
                    
            com.runwaysdk.geodashboard.GeodashboardDashboard.getCurrentDashboard(callback);
          }
        }));
          
        // Build the form
        tq.addTask(new Structure.TaskIF({
          start : function(){
            var builder = that._configuator.get(that.getFactory(), user, null);
            builder.render(that, true);
            builder.addListener(that);
              
            that.$render(parent);        

            if (jcf != null && jcf.customForms != null) {
              jcf.customForms.replaceAll();
            }        
          }
        }));
          
        tq.start();
      }
    }
  });
  
  var dashboardTable = ClassFramework.newClass(dashboardTableName, {
    
    Extends : Widget,
    
    Instance : {
      
      initialize : function(cfg) {
        
        cfg = cfg || {};
        cfg.queryType = cfg.queryType || defaultQueryType;
        this._config = cfg;        
        this._configuator = (cfg.configuator || new com.runwaysdk.ui.userstable.DashboardFormConfiguator());
                
        this.$initialize("div");        
      },
      
      _onEditDashboard : function(e) {
        var target = e.getTarget();
        var id = target.id;        
        var mainDiv = document.getElementById("dashboardTable");
        var rowNumber = id.replace('table-edit-', '');
          
        var user = this._table.getDataSource().getResultsQueryDTO().getResultSet()[rowNumber];
        
        var callback = new com.runwaysdk.geodashboard.StandbyClientRequest({
          that : this,
          onSuccess : function(dto) {
            this.that._buildDialog(this.that.localize("dialogEditDashboardTitle"), dto);        
          },
          onFailure : function(ex) {
            this.that.handleException(ex);
          } 
        }, mainDiv);        
        
        if(user.isWritable())
        {
          user.lock(callback);
        }
        else if(user.isReadable())
        {
          this._buildDialog(this.localize("dialogViewDashboardTitle"), user);        
        }
      },
      
      _onNewDashboard : function(e) {        
        var dashboard = new com.runwaysdk.geodashboard.Dashboard();
      
        if(dashboard.isWritable())
        {
          this._buildDialog(this.localize("dialogNewDashboardTitle"), dashboard);
        }
        else
        {
          var dialog = this.getFactory().newDialog(com.runwaysdk.Localize.get("rError", "Error"), {modal: true});
          dialog.appendContent(this.localize("notAllowed"));
          dialog.addButton(com.runwaysdk.Localize.get("rOk", "Ok"), function(){dialog.close();});
          dialog.render();
        }
      },     
      
      _buildDialog : function(label, dashboard) {
        
        var id = "dialog-" + (dashboard.isNewInstance() ? 'new' : dashboard.getId());
        var element = document.getElementById(id);
                    
        if(element == null)
        {      
          var Structure = com.runwaysdk.structure;
          var tq = new Structure.TaskQueue();
          var that = this;
          var types = {};
          var info = null;
          
          // First load the dashboard json
          tq.addTask(new Structure.TaskIF({
            start : function(){
          
              var callback = new Mojo.ClientRequest({
                onSuccess : function(json) {
                  info = JSON.parse(json);
                  
                  tq.next();
                },
                onFailure : function(ex) {
                  tq.stop();
                  that.handleException(ex);               
                }
              }); 

              that._configuator.getJSON(callback, dashboard);
            }
          }));
                     
          // Last build the dialog form
          tq.addTask(new Structure.TaskIF({
            start : function(){
              var fac = that.getFactory();
              var table = that._table;
        
              var dialog = fac.newDialog(label, {minHeight:250, maxHeight:$(window).height() - 75, minWidth:730, resizable: false});   
              dialog.setId(id);
            
              var builder = that._configuator.get(fac, dashboard, info);
              builder.addListener({
                handleEvent : function(event) {
                  if(event.getEventType() === DashboardFormEvent.APPLY_SUCCESS) {
                    table.refresh();
                  }
                }              
              });
              builder.render(dialog, false);
        
              dialog.render();   
        
              if (jcf != null && jcf.customForms != null) {
                jcf.customForms.replaceAll();
              }
            }
          }));
        
          tq.start();
        }
      },     
      
      _onDeleteDashboard : function(e) {
        var fac = this.getFactory();
        var table = this._table;
        
        var target = e.getTarget();
        var id = target.id;        
        var rowNumber = id.replace('table-delete-', '');
        
        var dashboard = table.getDataSource().getResultsQueryDTO().getResultSet()[rowNumber];
        
        var dialog = fac.newDialog(this.localize("dialogDeleteDashboardTitle"));
        dialog.appendContent(this.localize("deleteText").replace("{0}", dashboard.getName()));
        
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
            var mainDiv = document.getElementById("dashboardTable");
            
            var removeCallback = new com.runwaysdk.geodashboard.StandbyClientRequest({
              onSuccess : function() {
                table.refresh();
              },
              onFailure : function(ex) {
                tq.stop();
                that.handleException(ex);
              }
            }, mainDiv);
            
            dashboard.remove(removeCallback);
          }
        }));
        
        tq.start();
      },
      
      _makeButtons : function() {
        var newDashboard = this.getFactory().newButton(this.localize("newDashboard"), Mojo.Util.bind(this, this._onNewDashboard));
        newDashboard.setAttribute('class', 'btn btn-primary');
        
        this.appendChild(newDashboard);
      },
      
      _clickHandler : function(e) {        
        var target = e.getTarget();
        var klass = target.getAttribute('class');
        
        if(klass.indexOf('table-edit') !== -1)
        {
          e.preventDefault();
            
          this._onEditDashboard(e);
        }
        else if (klass.indexOf('table-delete') !== -1)
        {
          e.preventDefault();
          
          this._onDeleteDashboard(e);
        }      
      },
      
      render : function(parent) {
        this._makeButtons();
        
        // Build the columns from the read permissions
        var dashboard = new com.runwaysdk.geodashboard.Dashboard();
        var columns = [];        

        columns.push({header: this.localize("delete"), customFormatter: Mojo.Util.bind(this, function(result, rowNumber){
            return '<a href="#" class="fa fa-times ico-remove table-delete" id="table-delete-' + rowNumber + '"></a>';
          })
        });
        
        columns.push({header: this.localize("edit"), customFormatter: Mojo.Util.bind(this, function(result, rowNumber){
            return '<a href="#" class="fa fa-pencil ico-edit table-edit" id="table-edit-' + rowNumber + '"></a>';
          })
        });
                
        if(dashboard.isNameReadable())
        {          
          columns.push({queryAttr: "name"});        
        }
          
        if(dashboard.isDisplayLabelReadable())
        {
          columns.push({queryAttr: "displayLabel"});        
        }
                  
        if(dashboard.isRemovableReadable())
        {
          columns.push({queryAttr: "removable"});        
        }
        
        this._config.dataSource = new InstanceQueryDataSource({
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
  
  return dashboardTable;
  
})();