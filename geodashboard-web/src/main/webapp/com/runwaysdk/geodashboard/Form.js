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
//define(["../../ClassFramework", "../../Util", "../RunwaySDK_UI", "../factory/runway/widget/Widget", "../datatable/datasource/InstanceQueryDataSource"], function(ClassFramework, Util, UI, Widget, InstanceQueryDataSource) {
(function(){  
  var Widget = com.runwaysdk.ui.factory.runway.Widget;
  var Visitable = com.runwaysdk.ui.factory.runway.Visitable;
  
  Mojo.Meta.newClass('com.runwaysdk.geodashboard.AbstractFormEntry', {
    IsAbstract : true,
    Instance : {
      initialize : function()
      {
        this._parent = null;
        this._rendered = false;
        this._isDestroyed = false;
      },  
      removeInlineError : {
        IsAbstract : true
      },    
      addInlineError : {
        IsAbstract : true
      },
      hasError : {
        IsAbstract : true
      },
      accept : {
        IsAbstract : true
      },
      getManager: function()
      {
        return com.runwaysdk.ui.Manager.getInstance();
      },
      getFactory : function()
      {
        return this.getManager().getFactory();
      },      
      setParent : function(parent)
      {
        if (Mojo.Util.isUndefined(parent)) {
          throw new com.runwaysdk.Exception("The argument to Component.setParent cannot be undefined.");
        }
        
        // Parent is allowed to be null
        if (parent !== null && !com.runwaysdk.ui.Util.isComponentIF(parent)) {
          throw new com.runwaysdk.Exception("The argument to Component.setParent must implement ComponentIF.");
        }
        
        this._parent = parent;
      },
      getParent : function()
      {
        return this._parent;
      },
      getId : function()
      {
        return this._id;
      },
      getHashCode : function()
      {
        return this.getId();
      },
      equals : function(obj)
      {
        var eq = this.$equals(obj);
          
        if(eq)
        {
          return true;
        }
        else if(obj instanceof Mojo.$.com.runwaysdk.Base
          && this.getId() === obj.getId())
        {
          return true;
        }
        
        return this === obj;
      },
      setId : function(id)
      {
        this._id = id;
      },
      _generateId : function()
      {
        return this.getMetaClass().getName()+'_'+Mojo.Util.generateId(16);
      },
      render : function()
      {
        this._rendered = true;
      },
      isRendered : function()
      {
        return this._rendered;
      },
      _setRendered : function(rendered)
      {
        this._rendered = rendered;
      },
      addValidator : function(validator, message) {
        
      }
    }
  });
  
  Mojo.Meta.newClass('com.runwaysdk.geodashboard.ListFormEntry', {
    Extends : com.runwaysdk.geodashboard.AbstractFormEntry,
    Instance : {
      initialize : function(id, displayLabel, options)
      {
        this.$initialize();
        
        this._inputs = [];
        
        this._div = this.getFactory().newElement('div');
        this._div.setAttribute('class', 'field-row clearfix');
          
        var label = new com.runwaysdk.ui.factory.runway.Label(displayLabel);
        label.setAttribute('for', id);
        
        this._widget = this.getFactory().newElement('select');
        this._widget.setAttribute('id', id);
        
        for (var i = 0; i < options.length; ++i) {
          var option = options[i];
          
          var element = this.getFactory().newElement('option');
          element.setAttribute('value', option.value);
          element.setInnerHTML(option.displayLabel);
              
          if(option.checked)
          {
            element.setAttribute('selected', true);          
          }
              
          this._widget.appendChild(element);
        }

        var innerDiv = this.getFactory().newElement('div');
        innerDiv.setAttribute('class', 'select-holder');
       
        innerDiv.appendChild(this._widget);
          
        this._error = this.getFactory().newElement('div', {class:"error-message", id:id + "-error"});
         
        this._div.appendChild(label);
        this._div.appendChild(innerDiv);
        this._div.appendChild(this._error);   
          
        this.setId(id);
      },
      getName : function()
      {
        return this.getId();
      },
      getDiv : function()
      {
        return this._div;
      },
      hasError : function() 
      {
        return this._error.getInnerHTML() != '';
      },      
      removeInlineError : function ()
      {
        this._error.setInnerHTML('');              
        this._div.removeClassName('field-error');        
      },    
      addInlineError : function (msg) {
        this._error.setInnerHTML(msg);        
        this._div.addClassName('field-error');
      },
      getValues : function() {
        return $(this._widget.getRawEl()).val();
      },      
      accept : function(visitor) {
        visitor.visitSelect(this);      
      },
      render : function() {
        this.$render();
        
        jcf.customForms.replaceAll(this._div.getRawEl());        
      }      
    }
  });  
  
  Mojo.Meta.newClass('com.runwaysdk.geodashboard.AbstractGroupFormEntry', {
    Extends : com.runwaysdk.geodashboard.AbstractFormEntry,
    Instance : {
      initialize : function(id, displayLabel, options)
      {
        this.$initialize();
      
        this._inputs = [];
      
        this._div = this.getFactory().newElement('div');
        this._div.setAttribute('class', 'field-row clearfix');
                
        var _span = this.getFactory().newElement('span');
        _span.setAttribute('class', 'label-text');
        _span.setInnerHTML(displayLabel);
        
        var _innerDiv = this.getFactory().newElement('div');
        _innerDiv.setAttribute('class', 'checks-frame');
        
        for (var i = 0; i < options.length; ++i) {
          var option = options[i];
          
          var input = this._buildOptionInput(option);
          
          if(option.checked)
          {
            input.setAttribute('checked', true);          
          }
          
          var label = new com.runwaysdk.ui.factory.runway.Label(option.displayLabel);
          label.setAttribute('for', option.value);

          _innerDiv.appendChild(input);   
          _innerDiv.appendChild(label);
          
          this._inputs.push(input);
        }  
      
        this._error = this.getFactory().newElement('div', {class:"error-message", id:id + "-error"});
       
        this._div.appendChild(_span);
        this._div.appendChild(_innerDiv);
        this._div.appendChild(this._error);   
        
        this.setId(id);
      },
      _buildOptionInput : function (option)
      {
        IsAbstract : true;
      },      
      getName : function()
      {
        return this.getId();
      },
      getDiv : function()
      {
        return this._div;
      },
      hasError : function() 
      {
        return this._error.getInnerHTML() != '';
      },      
      removeInlineError : function ()
      {
        this._error.setInnerHTML('');              
        this._div.removeClassName('field-error');        
      },    
      addInlineError : function (msg) {
        this._error.setInnerHTML(msg);        
        this._div.addClassName('field-error');
      },
      getValues : function() {
        var selectedOptions = [];
        
        for (var i = 0; i < this._inputs.length; i++)
        {
          if (this._inputs[i].getRawEl().checked)
          {
            selectedOptions.push(this._inputs[i].getValue());
          }
        }
        return selectedOptions;
      },      
      accept : function(visitor) {
        visitor.visitSelect(this);      
      }           
    }
  });  

  Mojo.Meta.newClass('com.runwaysdk.geodashboard.CheckboxFormEntry', {
    Extends : com.runwaysdk.geodashboard.AbstractGroupFormEntry,
    Instance : {
      initialize : function(id, displayLabel, options)
      {
        this.$initialize(id, displayLabel, options);
      },
      _buildOptionInput : function (option)
      {
        var input = this.getFactory().newElement('input', {id:option.value, type:'checkbox'});
        input.setAttribute('value', option.value);

        return input;        
      },
    }
  });  
  
  Mojo.Meta.newClass('com.runwaysdk.geodashboard.RadioFormEntry', {
    Extends : com.runwaysdk.geodashboard.AbstractGroupFormEntry,
    Instance : {
      initialize : function(id, displayLabel, options)
      {
        this.$initialize(id, displayLabel, options);
      },
      _buildOptionInput : function (option)
      {
        var input = this.getFactory().newElement('input', {id:option.value, type:'radio', name:this.getId()});
        input.setAttribute('value', option.value);
        
        return input;        
      },
    }
  });
  
  Mojo.Meta.newClass('com.runwaysdk.geodashboard.DateFormEntry', {
    Extends : com.runwaysdk.geodashboard.AbstractFormEntry,  
    Instance : {
      initialize : function(attributeMd, name, value)
      {
        this.$initialize();
                
        var label = attributeMd.getDisplayLabel();
        
        if(attributeMd.isRequired()) {
          label += " *";
        }        
        
        this._div = this.getFactory().newElement('div');
        this._div.setAttribute('class', 'field-row clearfix form-block');
        
        this._span = this.getFactory().newElement('span');
        this._span.setAttribute('class', 'data-text');

        this._widget = new com.runwaysdk.ui.factory.runway.TextInput(name, {attributes:{type:'text'}});
        this._widget.setValue(value);

//        this._widget.addClassName('hasDatepicker');
//        this._widget.addClassName('date-field');
        
//        this._a = this.getFactory().newElement('a');
//        this._a.addClassName('datapicker-opener');
//        this._a.addClassName('date-field-opener');
//        this._a.setAttribute('href', '#');
        
        this._span.appendChild(this._widget);
//        this._span.appendChild(this._a);
        
        this._label = new com.runwaysdk.ui.factory.runway.Label(label, this._widget.getId());
        this._label.setAttribute('for', this._widget.getName());
        
        this._error = this.getFactory().newElement('div', {class:"error-message", id:this._widget.getId() + "-error"});
        
        this._div.appendChild(this._label);
        this._div.appendChild(this._span);
        this._div.appendChild(this._error);   
        
        this.setId(this._widget.getId());
      },
      render : function() {
//        var that = this;
//        
//        if(!this.isRendered()) {
//          // Setup the date picker
//          $('.date-field').datepicker($.datepicker.regional.local);
//          $('.date-field').datepicker("option", {
//            showOtherMonths: true,
//            selectOtherMonths: true
//          });
//            
//          $('.date-field-opener').click(function(e) {
//            e.preventDefault();
//            $(this).prev().datepicker('show');
//          });          
//        }
        
        this.$render();
      },
      getDiv : function()
      {
        return this._div;
      },
      addValidator : function(validator, message) {
        var that = this;
        
        $(this._widget.getRawEl()).keyup(function (event) {
          var value = $(this).val();
                
          if (validator(value)) {            
            that.addInlineError(message);
          }
          else {
            that.removeInlineError();
          }
        });        
      },      
      getLabel : function()
      {
        return this._label;
      },
      getWidget : function()
      {
        return this._widget;
      },
      hasError : function() 
      {
        return this._error.getInnerHTML() != '';
      },      
      removeInlineError : function ()
      {
        this._error.setInnerHTML('');              
        this._div.removeClassName('field-error');        
      },    
      addInlineError : function (msg) {
        this._error.setInnerHTML(msg);        
        this._div.addClassName('field-error');
      },
      accept : function(visitor) {
        this._widget.accept(visitor);
      }     
    }
  });  
  
  Mojo.Meta.newClass('com.runwaysdk.geodashboard.TermEntry', {
    IsAbstract : true,    
    Extends : com.runwaysdk.geodashboard.AbstractFormEntry,  
    Instance : {
      initialize : function(attributeMd, value)
      {
        this.$initialize();
        
        var name = attributeMd.getName();
        var displayLabel = attributeMd.getDisplayLabel();
        
        if(attributeMd.isRequired()) {
          displayLabel += " *";
        }

        this._attributeMd = attributeMd;
        this._div = this.getFactory().newElement('div');
        this._div.setAttribute('class', 'field-row clearfix form-block');
        
        this._widget = new com.runwaysdk.ui.factory.runway.TextInput(name + '-auto', {attributes:{type:'text'}});
        this._widget.setValue('');
        
        this._hidden = new com.runwaysdk.ui.factory.runway.HiddenInput(name, {attributes:{type:'hidden'}});
        this._hidden.setValue(value);
        
        this._label = new com.runwaysdk.ui.factory.runway.Label(displayLabel, this._widget.getId());
        this._label.setAttribute('for', this._widget.getName());
        
        this._error = this.getFactory().newElement('div', {class:"error-message", id:this._widget.getId() + "-error"});
        
        this._div.appendChild(this._label);
        this._div.appendChild(this._widget);
        this._div.appendChild(this._hidden);
        this._div.appendChild(this._error);   
        
        this.setId(this._widget.getId());
      },
      _loadTerm : function(termId) {
        IsAbstract : true
      },
      _getSuggestions : function(request, response ) {
        IsAbstract : true
      },      
      render : function() {
        var that = this;
        var id = this._hidden.getValue();
      
        // Load the data for the existing value
        if(id != null && id.length > 0) {
          this._loadTerm(id);
        }
        
        // Setup the auto-complete
        $(this._widget.getRawEl()).autocomplete({
          source: function( request, response ) {
            that._getSuggestions(request, response);
          },
          select: function(event, ui) {
            $(that._hidden.getRawEl() ).val(ui.item.id);
          }, 
          change : function(event, ui) {
            var value = $(that._widget.getRawEl()).val();
            
            if(value == null || value == '') {                
              $(that._hidden.getRawEl()).val('');
            }
          },
          minLength: 2
        });        
      },
      getDiv : function()
      {
        return this._div;
      },
      getLabel : function()
      {
        return this._label;
      },
      getWidget : function()
      {
        return this._widget;
      },
      removeInlineError : function ()
      {
        this._error.setInnerHTML('');              
        this._div.removeClassName('field-error');        
      },    
      addInlineError : function (msg) {
        this._error.setInnerHTML(msg);        
        this._div.addClassName('field-error');
      },
      hasError : function() 
      {
        return this._error.getInnerHTML() != '';
      },      
      accept : function(visitor) {
        this._hidden.accept(visitor);
      }     
    }
  });  
  
  Mojo.Meta.newClass('com.runwaysdk.geodashboard.ClassifierEntry', {
    Extends : com.runwaysdk.geodashboard.TermEntry,  
    Instance : {
      initialize : function(attributeMd, value)
      {
        this.$initialize(attributeMd, value);
      },
      _loadTerm : function(termId) {
        var that = this;
      
        var request = new Mojo.ClientRequest({
          onSuccess : function(classifier){
            that._widget.setValue(classifier.getDisplayLabel().getLocalizedValue());
          },
          onFailure : function(ex){
            that.addInlineError(ex.getLocalizedMessage());
          }
        });
                
        com.runwaysdk.geodashboard.ontology.Classifier.get(request, termId);         
      },
      _getSuggestions : function(request, response) {
        var that = this;
      
        var req = new Mojo.ClientRequest({
          onSuccess : function(resultSet){
                                                      
            var results = [];
                    
            $.each(resultSet, function( index, classifier ) {
              var label = classifier.getDisplayLabel().getLocalizedValue()
              var id = classifier.getId();
                      
              results.push({'label':label, 'value':label, 'id':id});
            });
                    
            response( results );
          },
          onFailure : function(e){
            that.handleException(e);
          }
        });

        com.runwaysdk.geodashboard.Dashboard.getClassifierSuggestions(req, that._attributeMd.getId(), request.term, 10);        
      }
    }
  });
  
  Mojo.Meta.newClass('com.runwaysdk.geodashboard.GeoEntityEntry', {
    Extends : com.runwaysdk.geodashboard.TermEntry,  
    Instance : {
      initialize : function(attributeMd, value, dashboardId)
      {
        this.$initialize(attributeMd, value);
        
        this._dashboardId = dashboardId;
      },
      _loadTerm : function(termId) {
        var that = this;
        
        var request = new Mojo.ClientRequest({
          onSuccess : function(label){
            that._widget.setValue(label);
          },
          onFailure : function(ex){
            that.addInlineError(ex.getLocalizedMessage());
          }
        });
        
        com.runwaysdk.geodashboard.GeoEntityUtil.getEntityLabel(request, termId);         
      },
      _getSuggestions : function(request, response) {
        var that = this;
        var req = new Mojo.ClientRequest({
          onSuccess : function(query){
            var resultSet = query.getResultSet()
                                    
            var results = [];
                  
            $.each(resultSet, function( index, result ) {
              var label = result.getValue('displayLabel');
              var id = result.getValue('id');
                    
              results.push({'label':label, 'value':label, 'id':id});
            });
                  
            response( results );
          },
          onFailure : function(e){
            that.addInlineError(ex.getLocalizedMessage());
          }
        });
        
        com.runwaysdk.geodashboard.Dashboard.getGeoEntitySuggestions(req, that._dashboardId, request.term, 10);
      }
    }
  });
  
  Mojo.Meta.newClass('com.runwaysdk.geodashboard.FormEntry', {
    Extends : com.runwaysdk.geodashboard.AbstractFormEntry,  
    Instance : {
      initialize : function(displayLabel, widget)
      {
        this.$initialize();
      
        this._widget = widget;
                
        this._div = this.getFactory().newElement('div');
        this._div.setAttribute('class', 'field-row clearfix');
        
        this._label = new com.runwaysdk.ui.factory.runway.Label(displayLabel, this._widget.getId());
        this._label.setAttribute('for', this._widget.getName());
        
        this._error = this.getFactory().newElement('div', {class:"error-message", id:this._widget.getId() + "-error"});
        
        this._div.appendChild(this._label);
        this._div.appendChild(this._widget);
        this._div.appendChild(this._error);        
                
        this.setId(widget.getId());
      },
      getDiv : function()
      {
        return this._div;
      },
      getLabel : function()
      {
        return this._label;
      },
      getWidget : function()
      {
        return this._widget;
      },
      removeInlineError : function ()
      {
        this._error.setInnerHTML('');              
        this._div.removeClassName('field-error');        
      },    
      addInlineError : function (msg) {
        this._error.setInnerHTML(msg);        
        this._div.addClassName('field-error');
      },
      hasError : function() 
      {
        return this._error.getInnerHTML() != '';
      },      
      accept : function(visitor) {
        this._widget.accept(visitor);
      },
      addValidator : function(validator, message) {
        var that = this;
      
        $(this._widget.getRawEl()).keyup(function (event) {
          var value = $(this).val();
              
          if (validator(value)) {            
            that.addInlineError(message);
          }
          else {
            that.removeInlineError();
          }
        });        
      }
    },
    Static : {
      newInput : function(type, name, config) {
        if (type === "text")
        {
          return new com.runwaysdk.ui.factory.runway.TextInput(name, config);
        }
        else if (type === "textarea")
        {
          return new com.runwaysdk.ui.factory.runway.TextArea(name, config);
        }
        else if (type === "hidden")
        {
          return new com.runwaysdk.ui.factory.runway.HiddenInput(name, config);
        }
        else if (type === "select")
        {
          return new com.runwaysdk.ui.factory.runway.Select(name, config);
        }
        else
        {
          throw new com.runwaysdk.Exception("Input type ["+type+"] not implemented");
        }
      },     
    }
  });  
  
  Mojo.Meta.newClass('com.runwaysdk.geodashboard.ReadEntry', {
    Extends : com.runwaysdk.geodashboard.AbstractFormEntry,  
    Instance : {
      initialize : function(id, displayLabel, value)
      {
        this.$initialize();
        
        this._value = value;
        this._name = name;
        
        this._div = this.getFactory().newElement('div');
        this._div.setAttribute('class', 'field-row clearfix');
        
        this._label = new com.runwaysdk.ui.factory.runway.Label(displayLabel, id);
        this._label.setAttribute('for', id);
        
        this._error = this.getFactory().newElement('div', {class:"error-message", id:id + "-error"});
        
        this._div.appendChild(this._label);
        this._div.appendChild(new com.runwaysdk.ui.factory.runway.Label(value, id));
        this._div.appendChild(this._error);        
        
        this.setId(id);
      },
      getName : function()
      {
        return this._name;  
      },
      getValue : function()
      {
        return this._value;  
      },
      getLabel : function()
      {
        return this._label;
      },      
      getDiv : function()
      {
        return this._div;
      },
      removeInlineError : function ()
      {
        this._error.setInnerHTML('');              
        this._div.removeClassName('field-error');        
      },    
      addInlineError : function (msg) {
        this._error.setInnerHTML(msg);        
        this._div.addClassName('field-error');
      },
      hasError : function() 
      {
        return this._error.getInnerHTML() != '';
      },      
      accept : function(visitor) {
        visitor.visitDefaultInput(this);
      }     
    }
  });  
  
  Mojo.Meta.newClass('com.runwaysdk.geodashboard.ReadTextAreaEntry', {
    Extends : com.runwaysdk.geodashboard.AbstractFormEntry,  
    Instance : {
      initialize : function(id, displayLabel, value, config)
      {
        this.$initialize();
          
        this._value = value;
          
        this._div = this.getFactory().newElement('div');
        this._div.setAttribute('class', 'field-row clearfix');
          
        this._label = new com.runwaysdk.ui.factory.runway.Label(displayLabel, id);
        this._label.setAttribute('for', id);
        
        this._text = new com.runwaysdk.ui.factory.runway.TextArea(id);
        this._text.setAttribute('disabled', true);
        this._text.setValue(value);
          
        this._error = this.getFactory().newElement('div', {class:"error-message", id:id + "-error"});
          
        this._div.appendChild(this._label);
        this._div.appendChild(this._text);
        this._div.appendChild(this._error);        
          
        this.setId(id);
      },
      getName : function()
      {
        return this._name;  
      },
      getValue : function()
      {
        return this._value;  
      },
      getLabel : function()
      {
        return this._label;
      },      
      getDiv : function()
      {
        return this._div;
      },
      removeInlineError : function ()
      {
        this._error.setInnerHTML('');              
        this._div.removeClassName('field-error');        
      },    
      addInlineError : function (msg) {
        this._error.setInnerHTML(msg);        
        this._div.addClassName('field-error');
      },
      hasError : function() 
      {
        return this._error.getInnerHTML() != '';
      },      
      accept : function(visitor) {
        visitor.visitDefaultInput(this);
      }     
    }
  });  
    
  
  
  var FormList = Mojo.Meta.newClass('com.runwaysdk.geodashboard.FormList', {
    Extends : Widget,
    Instance : {
      initialize : function()
      {
        this.$initialize("fieldset");
        this._entries = new com.runwaysdk.structure.LinkedHashMap();
      
        this._section = this.getFactory().newElement('section');
        this._section.setAttribute('class', 'form-container');
      
        this.appendChild(this._section);
      },
      getChildren : function()
      {
        return this._entries.values();
      },
      getChild : function(id)
      {
        return this.getEl().getChild(id);
      },
      hasChild : function(child)
      {
        return this.getEl().hasChild(child);
      },
      addEntry : function(formEntry)
      {
        formEntry.setParent(this);
        this._entries.put(formEntry.getId(), formEntry);
      
        this._section.appendChild(formEntry.getDiv());
      },
      appendElement : function(child)
      {
        this._section.appendChild(child);        
      },
      getEntries : function()
      {
        return this._entries;
      }
    }
  }); 

  Mojo.Meta.newClass('com.runwaysdk.geodashboard.Form', {
    Extends : Widget,
    Implements : Visitable,
    Instance : {
      initialize : function(config)
      {
        config = config || {};
        config.name = config.name || "";
        config.action = config.action || "";
        config.method = config.method || "POST";
        this._config = config;
        
        this.$initialize("form");
        this.addClassName('submit-form');
        
        this.setName(this._config.name);
        this.setAction(this._config.action);
        this.setMethod(this._config.method);
        
        this._formList = new FormList();
        this.appendChild(this._formList);
      },
      /**
       * Convenience method, invokes accept with the default visitor.
       * 
       * @returns map
       */
      getValues : function() {
        return this.accept(new com.runwaysdk.ui.factory.runway.FormVisitor());
      },
      getAction : function() {
        return this.getRawEl().action;
      },
      setAction : function(action) {
        this.getRawEl().action = action;
      },
      getMethod : function() {
        return this.getRawEl().method;
      },
      setMethod : function(method) {
        this.getRawEl().method = method;
      },
      getName : function() {
        return this.getRawEl().name;
      },
      setName : function(method) {
        this.getRawEl().name = name;
      }, 
      newVisitor : function(type, config) {
        if (type == "FormVisitor") {
          return new FormVisitor(config);
        }
        else if (type == "ConsoleFormVisitor") {
          return new ConsoleFormVisitor(config);
        }
        else
        {
          throw new com.runwaysdk.Exception("Input type ["+type+"] not implemented");
        }
      },
      addEntry : function(entry)
      {
        this._formList.addEntry(entry);
      },
      addFormEntry : function(attributeMdDTO, input) {
        var label = attributeMdDTO.getDisplayLabel();
         
        if(attributeMdDTO.isRequired()) {
          label += " *";
        }
        
        var entry = new com.runwaysdk.geodashboard.FormEntry(label, input);
        this.addEntry(entry);        
        
        return entry;
      },      
      getEntries : function()
      {
        return this._formList.getEntries();
      },
      appendElement : function(child)
      {
        this._formList.appendElement(child);
      },
      _generateFormId : function() {
        return this.getId()+'_RW_Form';
      },
      getChildren : function()
      {
        var children = this.$getChildren();
        var formListChildren = this._formList.getChildren();
        children = children.concat(formListChildren);
        return children;
      },
      render : function() {
        var entries = this.getEntries().values();
          
        for (var i = 0; i < entries.length; ++i) {
          entries[i].render();
        }          
      },
      accept : function(visitor)
      {
        visitor.visitForm(this);
        
        var entries = this.getEntries().values();
        
        for (var i = 0; i < entries.length; ++i) {
          entries[i].accept(visitor);
        }  
        
        return visitor.finishAndReturn();
      },
      getEntry : function (entryId)
      {
        var entries = this.getEntries().values();
        
        for (var i = 0; i < entries.length; ++i) {
          if(entries[i].getId() === entryId)
          {
            return entries[i];
          }
        }  
        
        return null;
      },
      removeErrorMessages : function ()
      {    
        var entries = this.getEntries().values();
          
        for (var i = 0; i < entries.length; ++i)
        {
          entries[i].removeInlineError();
        }      
      },
      hasError : function() {
        var entries = this.getEntries().values();
        
        var hasError = false;
          
        for (var i = 0; i < entries.length; ++i)
        {
          hasError = (hasError || entries[i].hasError());
        }  
        
        return hasError;
      },
      handleException : function(ex, throwIt)
      {
        var msg = null;
        
        try {          
          if (ex instanceof com.runwaysdk.ProblemExceptionDTO && msg == null) {
            var problems = ex.getProblems();
            var globalMessages = [];
              
            for (var i = 0; i < problems.length; i++) {
              var problem = problems[i];
              var attributeName = problem.getAttributeName();
              
              var entry = this.getEntry(attributeName);
              
              if(entry != null)
              {
                entry.addInlineError(problems[i].getLocalizedMessage());
              }
              else
              {
                globalMessages.push(problems[i].getLocalizedMessage());            
              }
            }
            
            if(globalMessages.length > 0) {
              msg = com.runwaysdk.Localize.get("problems", "Problems processing request:\n");    
              
              for (var i = 0; i < globalMessages.length; i++) {
                msg += globalMessages[i] + "\n";
              }
            }
          }
          else {
            msg = ex.getLocalizedMessage();          
          }
            
          if(msg != null)
          {
            var dialog = this.getFactory().newDialog(com.runwaysdk.Localize.get("rError", "Error"), {modal: true});
            dialog.appendContent(msg);
            dialog.addButton(com.runwaysdk.Localize.get("rOk", "Ok"), function(){dialog.close();});
            dialog.render();
          }
            
          if (throwIt) {
            throw ex;
          }
        }
        catch(e2) {
          throw ex;
        }
      }    
    }
  });  
})();
 
