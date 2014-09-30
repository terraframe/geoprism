(function(){    
  // Jquery autocomplete code

  (function($) {
    $.widget( "custom.combobox", {
              _create : function()
              {
                this.wrapper = $("<span>").addClass("custom-combobox")
                    .insertAfter(this.element);
                this.element.hide();
                this._createAutocomplete();
              },
              _createAutocomplete : function()
              {
                var selected = this.element.children(":selected"), value = selected
                    .val() ? selected.text() : "";
                this.input = $("<input>")
                    .appendTo(this.wrapper)
                    .val(value)
                    .attr("title", "")
                    .addClass("custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left")
                    .autocomplete(
                    {
                      delay : 0,
                      minLength : 0,
                      source : $.proxy(this, "_source")
                    })
                    .tooltip(
                    {
                      tooltipClass : "ui-state-highlight"
                    });
                this._on(this.input,
                {
                  autocompleteselect : function(event, ui)
                  {
                    ui.item.option.selected = true;
                    this._trigger("select", event,
                    {
                      item : ui.item.option
                    });                    
                  },
                  autocompletechange : function(event, ui)
                  {
                    this._trigger("change", event, {value : this.input.val()});
                  },
                });
              },
              _source : function(request, response)
              {
                var matcher = new RegExp($.ui.autocomplete
                    .escapeRegex(request.term), "i");
                response(this.element.children("option").map(function()
                {
                  var text = $(this).text();
                  if (this.value && (!request.term || matcher.test(text)))
                    return { label : text, value : text, option : this };
                }));
              },
              _destroy : function()
              {
                this.wrapper.remove();
                this.element.show();
              }
            });
  })(jQuery); 
 
  var ClassFramework = Mojo.Meta;
  var Widget = com.runwaysdk.ui.factory.runway.Widget;  
  
  Mojo.Meta.newClass('MDSS.report.FormList', {
    Extends : Widget,
    Instance : {
      initialize : function()
      {
        this.$initialize("fieldset");
        this._entries = new com.runwaysdk.structure.LinkedHashMap();
      
        this._section = com.runwaysdk.ui.Manager.getFactory().newElement('section');
        this._section.setAttribute('class', 'form-container');
      
        this.appendChild(this._section);
      },
      getManager: function()
      {
        return com.runwaysdk.ui.Manager.getInstance();
      },
      getFactory : function()
      {
        return this.getManager().getFactory();
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
        this._entries.put(formEntry.getId(), formEntry);
      
        formEntry.setParent(this);
        formEntry.append(this._section);        
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

  Mojo.Meta.newClass('MDSS.report.Form', {
    Extends : Widget,
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
        
        this._formList = new MDSS.report.FormList();
        this.appendChild(this._formList);
        
        this._buttons = this.getFactory().newElement('div', {id:'parameters-buttons'});        
        this._formList.appendElement(this._buttons);        
      },
      addEntry : function(entry)
      {
        this._formList.addEntry(entry);
      },
      getEntries : function()
      {
        return this._formList.getEntries();
      },
      appendElement : function(child)
      {
        this._formList.appendElement(child);
      },
      createButton : function(id, label, primary, callback)
      {
        var button = this.getFactory().newElement('button', {id:id, type:'button'});
        button.addClassName('btn');
        
        if(primary)
        {
          button.addClassName('btn-primary');                  
        }
        
        button.setInnerHTML(label);

        YAHOO.util.Event.on(button, 'click', callback);  
        
        this._buttons.appendChild(button);
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
      refresh : function()
      {
        var entries = this.getEntries().values();
        
        for (var i = 0; i < entries.length; ++i) {
          entries[i].updateView();
        }  
      },
      removeErrorMessages : function ()
      {    
        var entries = this.getEntries().values();
          
        for (var i = 0; i < entries.length; ++i)
        {
          entries[i].removeInlineError();
        }      
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
              msg = MDSS.localize("problems");    
              
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


  Mojo.Meta.newClass('MDSS.report.AbstractFormEntry', {
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
      updateModel : {
        IsAbstract : true
      },
      updateView : {
        IsAbstract : true
      },      
      append : {
        IsAbstract : true
      },      
      hasError : {
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
        else if(obj instanceof Mojo.$.com.runwaysdk.Base && this.getId() === obj.getId())
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
      }        
    }
  }); 

  Mojo.Meta.newClass('MDSS.report.FormTextEntry', {
    Extends : MDSS.report.AbstractFormEntry,  
    Instance : {
      initialize : function(model)
      {
        this.$initialize();
   
        this._model = model;
                  
        this._div = this.getFactory().newElement('div');
        this._div.setAttribute('class', 'field-row clearfix');
        
        this._widget = new com.runwaysdk.ui.factory.runway.TextInput(model.getName(), {id:model.getId()});
        
        // Hook up the page navigation inputs
        $(this._widget.getRawEl()).on('change', Mojo.Util.bind(this, this.updateModel));   
        
        this._label = new com.runwaysdk.ui.factory.runway.Label(model.getPromptText(), this._widget.getId());
        this._label.setAttribute('for', this._model.getName());
                  
        this._error = this.getFactory().newElement('div', {class:"error-message", id:this._widget.getId() + "-error"});
          
        this._div.appendChild(this._label);
        this._div.appendChild(this._widget);
        this._div.appendChild(this._error);        
                 
        this.setId(this._widget.getId());
      },
      hasError : function()
      {
        return (this._error.getInnerHTML() != '');
      },
      append : function(container)
      {
        container.appendChild(this._div);
      },
      updateModel : function ()
      {
        try
        {
          this._model.setValue(this._widget.getValue());   
          
          this.removeInlineError();
        }
        catch(e)
        {
          this.addInlineError(e);
        }
      },
      updateView : function ()
      {
        try
        {
          var value = this._model.getValue();
          
          if(value === undefined)
          {
            this._widget.setValue('');            
          }
          else
          {
            this._widget.setValue(value);                        
          }
          
          this._model.validate();
          
          this.removeInlineError();
        }
        catch(e)
        {
          this.addInlineError(e);
        }
        
      },
      removeInlineError : function ()
      {
        this._error.setInnerHTML('');              
        this._widget.removeClassName('field-error');        
      },    
      addInlineError : function (e) {
        try
        {
          this._error.setInnerHTML(e.getLocalizedMessage());        
          this._widget.addClassName('field-error');
        }
        catch(e1)
        {
          this._error.setInnerHTML(e);        
          this._widget.addClassName('field-error');
        }                
      }
    }
  });  

  Mojo.Meta.newClass('MDSS.report.SelectEntry', {
    Extends : MDSS.report.AbstractFormEntry,  
    Instance : {
      initialize : function(model)
      {
        this.$initialize();
        
        this._model = model;
                  
        this._div = this.getFactory().newElement('div');
        this._div.setAttribute('class', 'field-row clearfix');
        
        this._widget = new com.runwaysdk.ui.factory.runway.Select(model.getName(), {id:model.getId()});
        
        if(!model.allowNewValues())
        {
          this._widget.setAttribute('multiple', model.isMultiple());
          this._widget.setAttribute('size', 3);
        }
        
        var options = this._model.getOptions();
        
        for (var i = 0; i < options.length; i++) {
          var option = this.getFactory().newElement('option');
          option.setAttribute('value', options[i].value);
          option.setInnerHTML(options[i].label);
          
          this._widget.appendChild(option)
        }          
                  
        this._label = new com.runwaysdk.ui.factory.runway.Label(model.getPromptText(), this._widget.getId());
        this._label.setAttribute('for', this._model.getName());
                  
        this._error = this.getFactory().newElement('div', {class:"error-message", id:this._widget.getId() + "-error"});
          
        this._div.appendChild(this._label);
        this._div.appendChild(this._widget);
        this._div.appendChild(this._error); 
        
        // Hook up the page navigation inputs and make a combo box
        if(model.allowNewValues())
        {
          $(this._widget.getRawEl()).combobox({ 
            select : Mojo.Util.bind(this, function (event, ui) { 
              this.setValues(ui.item);
            }),
            change : Mojo.Util.bind(this, function (event, ui) { 
              this.setValues(ui);
            })
          }).val(this._model.getValue());
        }
        else
        {
          $(this._widget.getRawEl()).on('change', Mojo.Util.bind(this, this.updateModel));          
        }
                 
        this.setId(this._widget.getId());
      },
      hasError : function()
      {
        return (this._error.getInnerHTML() != '');
      },
      append : function(container)
      {
        container.appendChild(this._div);
      },
      updateModel : function ()
      {
        return this.setValues(this._widget.getValues());          
      },
      setValues : function (values)
      {        
        var array = [];
        
        if(Mojo.Util.isArray(values))
        {          
          for(var i = 0; i < values.length; i++)
          {
            array.push(values[i].value);
          }
        }
        else
        {
          array = values.value;
        }
                
        try
        {
          this._model.setValue(array);   
          
          this.removeInlineError();
        }
        catch(e)
        {
          this.addInlineError(e);
        }
      },
      updateView : function ()
      {
        try
        {
          var value = this._model.getValue();
          
          $(this._widget.getRawEl()).val(value);
          
          if(this._model.allowNewValues())
          {
            $(this._widget.getRawEl()).parent().find('.custom-combobox-input').val(value);
          }
          
          this._model.validate();
          
          this.removeInlineError();
        }
        catch(e)
        {
          this.addInlineError(e);
        }        
      },
      removeInlineError : function ()
      {
        this._error.setInnerHTML('');              
        this._widget.removeClassName('field-error');        
      },    
      addInlineError : function (e) {
        try
        {
          this._error.setInnerHTML(e.getLocalizedMessage());        
          this._widget.addClassName('field-error');
        }
        catch(e1)
        {
          this._error.setInnerHTML(e);        
          this._widget.addClassName('field-error');
        }        
      }
    }
  });  
  
  Mojo.Meta.newClass('MDSS.report.AbstractGroupEntry', {
    Extends : MDSS.report.AbstractFormEntry,  
    Instance : {
      initialize : function(model)
      {
        this.$initialize();
        
        this._model = model;
        
        this._div = this.getFactory().newElement('div');
        this._div.setAttribute('class', 'field-row clearfix');
                
        this._label = new com.runwaysdk.ui.factory.runway.Label(model.getPromptText(), this._model.getName());
        this._label.setAttribute('for', this._model.getName());
        
        this._innerDiv = this.getFactory().newElement('div');
        this._innerDiv.setAttribute('class', 'checks-frame');
        
        var options = this._model.getOptions();
        
        for (var i = 0; i < options.length; ++i) {
          var option = options[i];
          
          var input = this._buildOption(this._model.getName(), option.value);
          
          var label = new com.runwaysdk.ui.factory.runway.Label(option.label);
          label.setAttribute('for', option.value);

          this._innerDiv.appendChild(input);   
          this._innerDiv.appendChild(label);
        }  
        
        $(this._innerDiv.getRawEl()).on('change', Mojo.Util.bind(this, this.updateModel));   
      
        this._error = this.getFactory().newElement('div', {class:"error-message", id:this._model.getName() + "-error"});
       
        this._div.appendChild(this._label);
        this._div.appendChild(this._innerDiv);
        this._div.appendChild(this._error);   
        
        this.setId(this._model.getName());
      },
      hasError : function()
      {
        return (this._error.getInnerHTML() != '');
      },
      append : function(container)
      {
        container.appendChild(this._div);
      },
      updateModel : function ()
      {
        try
        {
//          var val = [];
//          $(':checkbox:checked').each(function(i){
//            val[i] = $(this).val();
//          });
          
          var value = $("input[name ='" + this._model.getName() + "']:checked").val();
          
          this._model.setValue(value);   
          
          this.removeInlineError();
        }
        catch(e)
        {
          this.addInlineError(e);
        }
      },
      updateView : function ()
      {
        try
        {
          var value = this._model.getValue();
          var selector = "input[name='" + this._model.getName() + "'][value='" + value + "']";
          
          $(selector).attr('checked', true);
          
          this._model.validate();
          
          this.removeInlineError();
        }
        catch(e)
        {
          this.addInlineError(e);
        }        
      },
      removeInlineError : function ()
      {
        this._error.setInnerHTML('');              
        this._innerDiv.removeClassName('field-error');        
      },    
      addInlineError : function (e) {
        try
        {
          this._error.setInnerHTML(e.getLocalizedMessage());        
          this._innerDiv.addClassName('field-error');          
        }
        catch(e1)
        {
          this._error.setInnerHTML(e);        
          this._innerDiv.addClassName('field-error');          
        }
      }
    }
  });  
  
  
  Mojo.Meta.newClass('MDSS.report.RadioEntry', {
    Extends : MDSS.report.AbstractGroupEntry,  
    Instance : {
      initialize : function(model)
      {
        this.$initialize(model);
      },
      _buildOption : function(name, value)
      {
        var input = this.getFactory().newElement('input', {type:'radio', name:name});
        input.setAttribute('value', value);
        
        return input;
      }
    }
  });  
  
  Mojo.Meta.newClass('MDSS.report.CheckboxEntry', {
    Extends : MDSS.report.AbstractGroupEntry,  
    Instance : {
      initialize : function(model)
      {
        this.$initialize(model);
      },
      _buildOption : function(name, value)
      {
        var input = this.getFactory().newElement('input', {type:'checkbox', name:name});
        input.setAttribute('value', value);
        
        return input;
      }
    }
  });
  
  Mojo.Meta.newClass('MDSS.report.ReportParameter', {
    Constants : {
      AUTO_SUGGEST : 4,
      CHECK_BOX : 3,
      LIST_BOX : 1,
      RADIO_BUTTON : 2,
      TEXT_BOX : 0,
      SELECTION_LIST_DYNAMIC : 1,
      SELECTION_LIST_NONE : 0,
      SELECTION_LIST_STATIC : 2,
      TYPE_ANY : 0,
      TYPE_BOOLEAN : 5,
      TYPE_DATE : 7,
      TYPE_DATE_TIME : 4,
      TYPE_DECIMAL : 3,
      TYPE_FLOAT : 2,
      TYPE_INTEGER : 6,
      TYPE_STRING : 1,
      TYPE_TIME : 8
    },
    Instance : {
      initialize : function(json) {
        this._isHidden = json.isHidden;
        this._dataType = json.dataType;
        this._isRequired = json.isRequired;
        this._name = json.name;
        this._type = json.type;
        this._defaultValue = json.defaultValue;
        this._options = json.options;
        this._displayName = json.displayName;
        this._promptText = json.promptText;
        this._scalarParameterType = json.scalarParameterType;
        this._value = this.getParameterByName(this._name);
        this._allowNewValues = json.allowNewValues;
        
        if(this._value == null || this._value == '')
        {
          this._value = this._defaultValue;
        }
        
        if(this.isMultiple())
        {
          try
          {
            this._value = JSON.parse(this._value);            
          }
          catch(e)
          {
            this._value = this._defaultValue;
          }
        }
                
        if((this._options == null || this._options.length == 0) && this._dataType == MDSS.report.ReportParameter.TYPE_BOOLEAN)
        {
          this._options = [{label:MDSS.localize('True'), value:'True'}, {label:MDSS.localize('False'), value:'False'}];
        }
      },
      isHidden : function()
      {
        return this._isHidden;
      },
      allowNewValues : function()
      {
        return this._allowNewValues;
      },
      getDisplayName : function()
      {
        return this._displayName;
      },
      getPromptText : function()
      {
        return this._promptText;
      },
      setValue : function(value) {
        this._value = value;
        
        this.validate();
      },
      getValue : function() {
        return this._value; 
      },
      getName : function() {
        return this._name;
      },
      getId : function() {
        return this._name + '_id';
      },
      getType : function() {
        return this._type;
      },
      getDataType : function() {
        return this._dataType;
      },
      getOptions : function() {
        return this._options;
      },
      isMultiple : function() {
        return (this._scalarParameterType === 'multi-value');
      },
      isRequired : function() {
        return this._isRequired;
      },
      getParameterByName : function (name) {
        // http://stackoverflow.com/questions/901115/how-can-i-get-query-string-values-in-javascript
        name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
        
        return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
      },
      formatForUrl : function()
      {
        if(Mojo.Util.isArray(this.getValue()))
        {
          return encodeURIComponent(this.getName()) + "=" + encodeURIComponent(JSON.stringify(this.getValue()));          
        }
        else
        {
          return encodeURIComponent(this.getName()) + "=" + encodeURIComponent(this.getValue());          
        }
      },
      validate : function(value) {
        if(value === undefined)
        {
          value = this._value;  
        }
        
        if(value != null && Mojo.Util.isArray(value))
        {
          for(var i = 0; i < value.length; i++)
          {
            this.validate(value[i]);
          }
        }        
        else if ((value == null || value.length == 0) && this.isRequired())
        {
          throw new com.runwaysdk.Exception(MDSS.localize("value.required"));
        }
        else if (value != null && value.length > 0)
        {
          if(this._dataType == MDSS.report.ReportParameter.TYPE_FLOAT || this._dataType == MDSS.report.ReportParameter.TYPE_DECIMAL)
          {
            // Validate the value represents a valid float or decimal
            var re = /^-{0,1}\d+(\.\d+){0,1}$/;
            if(!re.test(value))
            {
              throw new com.runwaysdk.Exception(MDSS.localize("invalid_birt_rational"));              
            }
          }
          else if(this._dataType == MDSS.report.ReportParameter.TYPE_INTEGER)
          {
            // Validate the value represents a valid whole number
            //var re = new RegExp('-{0,1}\d+');
            var re = /^-{0,1}\d+$/;
          
            if(!re.test(value))
            {
              throw new com.runwaysdk.Exception(MDSS.localize("invalid_birt_integer"));              
            }
          }        
          else if(this._dataType == MDSS.report.ReportParameter.TYPE_TIME)
          {
            // Validate the value represents a valid time (hh:mm:ss)            
            var re = /^\d{2}:\d{2}:\d{2}$/;
            if(!re.test(value))
            {
              throw new com.runwaysdk.Exception(MDSS.localize("invalid_birt_time"));
            }          
          }        
          else if(this._dataType == MDSS.report.ReportParameter.TYPE_DATE)
          {
            // Validate the value represents a valid date (yyyy-MM-dd)
            var re = /^\d{4}-\d{2}-\d{2}$/;
            if(!re.test(value))
            {
              throw new com.runwaysdk.Exception(MDSS.localize("invalid_birt_date"));
            }          
          }        
          else if(this._dataType == MDSS.report.ReportParameter.TYPE_DATE_TIME)
          {
            // Validate the value represents a valid date time (yyyy-MM-dd HH:mm:ss.SSS)
            var re = /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}.*$/;
            if(!re.test(value))
            {
              throw new com.runwaysdk.Exception(MDSS.localize("invalid_birt_date_time"));
            }          
          
          }
          else if(this._dataType == MDSS.report.ReportParameter.TYPE_BOOLEAN)
          {
            // Validate the value represents a valid date time (False, True)
            if(!(value == 'True' || value == 'False'))
            {
              throw new com.runwaysdk.Exception(MDSS.localize("invalid_birt_boolean"));
            }          
          }
        }        
      }
    }
  });
    
  Mojo.Meta.newClass('MDSS.report.ReportPage', {
    Instance: {
      initialize : function(id, pageNumber, pageCount, required, cached) {
        this._id = id;
        this._pageNumber = pageNumber;
        this._pageCount = pageCount;
        this._required = required;
        this._cached = cached;
        
        this._rendered = false;
        this._parameters = null;            
        
        this._panel = com.runwaysdk.ui.Manager.getFactory().newElement(document.getElementById('panel-content'));

        // Set up the event handlers for menu clicks
        $('#parameters').on('click', Mojo.Util.bind(this, this._handleParametersClick));             
        $('.export').on('click', Mojo.Util.bind(this, this._handleExportSelect));                     
        $('#menu-button').on('click', function(){$( "#menu" ).toggle();});                     
        
        // Hook up page navigation buttons
        $('#gotoPage').on('change', Mojo.Util.bind(this, this._handlePageClick));                     
        $('#last-page').on('click', Mojo.Util.bind(this, this._handleLastPage));                     
        $('#next-page').on('click', Mojo.Util.bind(this, this._handleNextPage));                     
        $('#prev-page').on('click', Mojo.Util.bind(this, this._handlePrevPage));                     
        $('#first-page').on('click', Mojo.Util.bind(this, this._handleFirstPage));                     
        
        // Render the menu
        $( "#menu" ).menu();
        $( "#menu" ).hide();
      },
      getFactory : function()
      {
        return com.runwaysdk.ui.Manager.getFactory();
      },
      init : function()
      {
        var request = new MDSS.Request({
          that : this,
          onSuccess : function(json) {
            this.that._setParameters(json);
            
            if(this.that._required)
            {
              this.that.showParameterForm();              
            }
          },
          onFailure : function(e) {
            alert(e.getLocalizedMessage());
          }
        });
        
        if(this._required) {
          $("#navigationBar").hide();
        }
            
        dss.vector.solutions.report.ReportItem.getParameterDefinitions(request, this._id);    
      },
      _setParameters : function(json)
      {
        this._parameters = [];
        
        var array = JSON.parse(json);
            
        var len = array.length;
        for (var i = 0; i < len; i++) {
          var parameter = new MDSS.report.ReportParameter(array[i]);
          this._parameters.push(parameter);
        }                        
      },
      _handleParametersClick : function()
      {
        if(this._cached === true)
        {
          alert(MDSS.localize('cached.parameter.error'));          
        }
        else
        {
          if(this._parameters == null)
          {
            var request = new MDSS.Request({
              that : this,
              onSuccess : function(json) {
                this.that._setParameters(json);
                
                this.that.showParameterForm();
              },
              onFailure : function(e) {
                alert(e.getLocalizedMessage());
              }
            });
            
            dss.vector.solutions.report.ReportItem.getParameterDefinitions(request, this._id);              
          }
          else
          {
            this.showParameterForm();
          }                      
        }
      },
      _buildParameterForm : function(readOnly) {
        var form = new MDSS.report.Form();
        
        var header = this.getFactory().newElement('h2');
        header.setInnerHTML(MDSS.localize('parameters'));
        
        form.appendElement(header);
      
        var len = this._parameters.length;
        for (var i = 0; i < len; i++) {
          var parameter = this._parameters[i];
          
          if(!parameter.isHidden())
          {
            if(parameter.getType() == MDSS.report.ReportParameter.AUTO || parameter.getType() == MDSS.report.ReportParameter.TEXT_BOX)
            {
              var entry = new MDSS.report.FormTextEntry(parameter);
            
              form.addEntry(entry);                      
            }
            else if(parameter.getType() == MDSS.report.ReportParameter.LIST_BOX)
            {
              var entry = new MDSS.report.SelectEntry(parameter);
            
              form.addEntry(entry);                      
            }
            else if(parameter.getType() == MDSS.report.ReportParameter.RADIO_BUTTON)
            {
              var entry = new MDSS.report.RadioEntry(parameter);
            
              form.addEntry(entry);
            }
            else if(parameter.getDataType() == MDSS.report.ReportParameter.TYPE_BOOLEAN &&  parameter.getType() == MDSS.report.ReportParameter.CHECK_BOX)
            {
              var entry = new MDSS.report.RadioEntry(parameter);
              
              form.addEntry(entry);
            }       
            else if(parameter.getType() == MDSS.report.ReportParameter.CHECK_BOX)
            {
              var entry = new MDSS.report.CheckboxEntry(parameter);
            
              form.addEntry(entry);
            }       
          }
        } 
        
        form.createButton("run_report", MDSS.localize("run_report"), true, Mojo.Util.bind(this, this._run));
        
        if(!this._required)
        {
          form.createButton("close", MDSS.localize("close"), false, Mojo.Util.bind(this, this._close));          
        }
        
        return form;
      },    
      showParameterForm : function() {  
        $( "#menu" ).hide();
        
        if(!this._rendered)
        {          
          var form = this._buildParameterForm();

          this._panel.appendChild(form);
          
          //$( "#panel" ).resizable({ handles: "n" });
        }
        
        $("#panel").slideToggle("slow");

        if(!this._rendered)
        {          
          form.refresh();
        }
        
        this._rendered = true;
      },
      _close : function(){
        $("#panel").slideToggle("slow");
      },
      _getUrl : function()
      {
        var url = "dss.vector.solutions.report.ReportController.run.mojo?report=" + this._id;
        
        var len = this._parameters.length;
        for (var i = 0; i < len; i++) {
          var parameter = this._parameters[i];
        
          url += "&" + parameter.formatForUrl();
        } 
        
        return url;
      },
      _run : function(){        
        
        try
        {
          var len = this._parameters.length;
          
          for (var i = 0; i < len; i++) {
            this._parameters[i].validate();
          }                        
          
          window.location.href = this._getUrl();          
        }
        catch(e)
        {
          alert(MDSS.localize('paramters.error'));
        }
      },        
      _handleFirstPage : function() {
        this._gotoPage(1);
      },      
      _handlePrevPage : function() {
        var pageNumber = this._pageNumber > 1 ? (this._pageNumber - 1) : 1;
        
        this._gotoPage(pageNumber);
      },      
      _handleNextPage : function() {
        var pageNumber = this._pageNumber < this._pageCount ? (this._pageNumber + 1) : this._pageCount;
        
        this._gotoPage(pageNumber);
      },      
      _handleLastPage : function() {
        this._gotoPage(this._pageCount);
      },      
      _handlePageClick : function() {
        var pageNumber = parseInt($('#gotoPage').val());
        
        this._gotoPage(pageNumber);
      },      
      _gotoPage : function(pageNumber) {
        if(Mojo.Util.isNumber(pageNumber) && pageNumber >= 1 && pageNumber <= this._pageCount)
        {
          var url = this._getUrl() + "&" + encodeURIComponent("pageNumber") + "=" + encodeURIComponent(pageNumber);
          
          window.location.href = url;
        }
        else
        {
          alert(MDSS.localize('Invalid_page_input'));
        }        
      },
      _handleExportSelect : function(e) {                
        $( "#menu" ).hide();
        
        var selected = e.target.id;
        
        if(selected != null && selected != "")
        {
          var url = this._getUrl() + "&" + encodeURIComponent("format") + "=" + encodeURIComponent(selected);
          
          window.location.href = url;
        }
      }
    }
  });
})();

