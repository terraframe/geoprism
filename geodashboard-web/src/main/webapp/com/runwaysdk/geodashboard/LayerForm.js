(function(){
  var CategoryWidget = Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.CategoryWidget', {
    IsAbstract : true,
    Constants : {
      OTHER : 'other'
    },    
    Instance : {
      
      render : {
        IsAbstract : true
      },
      getCategories:  {
        IsAbstract : true
      },
      attachCategoryColorPickers : function (){
        // ontology category layer type colors
        $(".cat-color-selector").colpick({
          submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
          onShow:function(colPickObj){
        	  var that = this;
              $(LayerForm.LAYER_MODAL).scroll(function(){  
              	var colorPicker = $(".colpick.colpick_full.colpick_full_ns:visible");
              	var colPick = $(that);
              	var diff = colPick.offset().top + colPick.height() + 2; 
              	var diffStr = diff.toString() + "px";
              	colorPicker.css({ top: diffStr });
              });
          },
          onChange:function(hsb,hex,rgb,el,bySetColor) {
            $(el).css('background','#'+hex);
            $(el).find('.color-input').attr('value', '#'+hex);
          }
        });
      }
    }
  });
  
  var CategoryTreeWidget = Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.CategoryListWidget', {
    Extends : CategoryWidget,    
    Instance : {
      initialize : function(map, elementId, storeId, checkOther, prefix, mdAttributeId, categoryType, aggregationId){
        this._map = map;
        this._elementId = elementId;
        this._storeId = storeId;        
        this._checkOther = checkOther;    
        this._prefix = prefix;
        this._mdAttributeId = mdAttributeId;
        this._categoryType = categoryType;
        this._aggregationId = aggregationId;
        
        this._parser = Globalize.numberParser();
        this._formatter = Globalize.numberFormatter();
      },
      getImpl : function() {        
          return $(this._elementId);
      },      
      setCheckOther : function(checkOther) {
        this._checkOther = checkOther;  
      },
      
      _getCategoryHTML : function(index) {
            	  
        var autocomplete = 'on';
        var fillLabel =  com.runwaysdk.Localize.localize("DashboardThematicLayer.form", "catInputPlaceholder");  
        var disabled = "";
        var color = "#000000";
        var value = "";
        var id = this._prefix + '-' + index;
        
        if(index == CategoryWidget.OTHER) {
          autocomplete = 'off';
          fillLabel =  com.runwaysdk.Localize.localize("DashboardThematicLayer.form", "Other");
          disabled = "disabled";
          value = "other";
          color = "#737678";
        }       
                  
        var html = '<li>' +
          '<div class="category-container">' +
            '<div class="text category-input-container">' +
              '<input id="' + id  + '" data-mdattributeid="' + this._mdAttributeId + '" data-type="' + this._categoryType + '" class="category-input" name="" type="text" value="' + value + '" placeholder="' + fillLabel +'" autocomplete="' + autocomplete + '" ' + disabled + ' >' +
            '</div>' +
            '<div class="cell">' +
              '<div class="color-holder">' +
                '<a href="#" class="color-choice">' +
                  '<span id="' + id  + '-color-selector" class="ico cat-color-selector" style="background:' + color +'">icon</span>' +
                  '<span class="arrow">arrow</span>' +
                '</a>' +
              '</div>' +
            '</div>' +
          '</div>' +
        '</li>';
            
        return html;        
      },
        
      
      /**
       * Adds the value and color setting for saved categories on the layer create/edit form.
       * The categories data is appended to the #categories-input element as json.
       * 
       */
      render : function() {
        
        var html = '<ul class="color-list">';
        html += this._getCategoryHTML(1);
        html += this._getCategoryHTML(2);
        html += this._getCategoryHTML(3);
        html += this._getCategoryHTML(4);
        html += this._getCategoryHTML(5);

        if(this._checkOther) {
          html += this._getCategoryHTML(CategoryWidget.OTHER);
        }
        html += '</ul>';
      
        this.getImpl().html(html);
        
        // Load the existing data 
        this._addCategoryAutoComplete();        
        this._loadExistingCategories();
        
        this.attachCategoryColorPickers();
      },
      
      /**
       * Hooks the auto-complete functionality to the category field input fields
       */
      _addCategoryAutoComplete : function(){
        
        var that = this;
        
        // Hook up the auto-complete for category input options new layers
        // existing layers have a seperate autocomplete hookup that queries 
        // the layer database view directly. 
        this.getImpl().find('.category-input').each(function(){
          
          var mdAttribute = $(this).data('mdattributeid');  
          var categoryType = $(this).data('type');
          
          $(this).autocomplete({
            source: function( request, response ) {
              var req = new Mojo.ClientRequest({
                onSuccess : function(results){
                  
                  // We need to localize the results for numbers
                  if(categoryType == 'number') {
                    for(var i = 0; i < results.length; i++) {
                      var number = parseFloat(results[i]);
                      var localized = that._formatter(number);
                      
                      results[i] = localized;
                    }
                  }
                  
                  response( results );
                },
                onFailure : function(e){
                  that._map.handleException(e);
                }
              });
              
              // values are scraped from hidden input elements on the layer create form
              var universalId = $(ThematicLayerForm.GEO_AGG_LEVEL_DD).val();
              var geoNodeId = $(ThematicLayerForm.GEO_NODE_SELECT_EL).val();
              var aggregationVal = $(that._aggregationId).val();
              var conditions = that._map.getCurrentConditions();
              
              com.runwaysdk.geodashboard.Dashboard.getCategoryInputSuggestions(req, mdAttribute, geoNodeId, universalId, aggregationVal, request.term, 10, conditions);
            },
            minLength: 1
          });
        });
      },
      _loadExistingCategories : function() {
        var catsJSONObj = $(this._storeId).data("categoriesstore");
          
        if(catsJSONObj){
          catsJSONObj = JSON.parse(decodeURIComponent(catsJSONObj));
            
          var catsJSONArr = catsJSONObj.catLiElems;
            
          if(catsJSONArr == null && Array.isArray(catsJSONObj)) {
            catsJSONArr = catsJSONObj;
          }          
            
          var catInputId;
          var catOtherEnabled = true;
              
          for(var i=0; i<catsJSONArr.length; i++){
            var cat = catsJSONArr[i];
                
            // Controlling for 'other' category 
            if(cat.otherCat){
              catInputId = this._prefix + "-" + CategoryWidget.OTHER;
            }
            else{
              catInputId = this._prefix + "-" + (i+1);
            }
                
            var catColorSelectorId = catInputId + "-color-selector";
            $("#"+catInputId).val(cat.val);
            $("#"+catColorSelectorId).css("background", cat.color);
            catOtherEnabled = cat.otherEnabled;
          }
              
          // Simulate a checkbox click to turn off the checkbox if the 'other' option is disabled
          // The 'other' option is checked by default making this a valid sequence
          if(this._checkOther && !catOtherEnabled){
            $(".other-option-check-box").click();
            $("#" + this._prefix + "-" + CategoryWidget.OTHER).parent().parent().hide();
          }
        }
      },        
      getCategories: function () {    
        var elements = this.getImpl().find(".category-container");
        var categories = [];        
            
        for(var i=0; i< elements.length; i++){
          var catInputElem = $(elements[i]).find(".category-input");
          var catColorElem = $(elements[i]).find(".cat-color-selector");
          var catColor = LayerForm.rgb2hex($(catColorElem).css("background-color"));
          var catVal = catInputElem.val();
                
          // parse the formatted number to the format of the data so the SLD can apply categories by this value
          if(catInputElem.data("type") == "number" ) {
            var thisNum = this._parser(catVal);
              
            if($.isNumeric(thisNum)) {
              catVal = thisNum;                
            }
          }
                
          // Filter out categories with no input values
          if(catVal !== ""){
            var category = new Object();
            category.val = catVal;
            category.color = catColor;
            category.isOntologyCat = false;
                
            if(this._checkOther) {
              category.otherEnabled = $(".other-option-check-box").prop("checked");
                 
              if(catInputElem[0].id === "cat-other"){
                category.otherCat = true;
              }
              else{
                category.otherCat = false;
              }
            }
            else {
              category.otherEnabled = false;
              category.otherCat = false;
            }
                     
            categories.push(category);
          }
        }  
                      
        return categories;
      },
    }
  });
  
  var CategoryTreeWidget = Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.CategoryTreeWidget', {
    Extends : CategoryWidget,    
    Instance : {
      initialize : function(elementId, storeId){
        this._elementId = elementId;
        this._storeId = storeId;
        
        // Default values for the category tree widget
        if(this._elementId == null) {
          this._elementId = "#ontology-tree";
        }
        
        if(this._storeId == null ) {
          this._storeId = "#categories-input";
        }
      },
      
      getImpl : function() {        
        return $(this._elementId);
      },
      
      /**
       * Renders the term tree on the layer form for ontology categories
       * 
       */        
      render : function(rootTerms, nodes) {
        
        if(rootTerms == null) {
          var rootsJSON = this.getImpl().data("roots");
          rootTerms = rootsJSON.roots;        
        }
        
        var that = this;
        
        var tree = new com.runwaysdk.geodashboard.ontology.OntologyTree({
          termType : "com.runwaysdk.geodashboard.ontology.Classifier",
          relationshipTypes : [ "com.runwaysdk.geodashboard.ontology.ClassifierIsARelationship" ],
          rootTerms : rootTerms,
          editable : false,
          slide : false,
          selectable : false,
          onCreateLi: function(node, $li) {
            if(!node.phantom) {
              var termId = node.runwayId;

              var catColor = that._getCategoryColor(termId);
          
              var thisLi = $.parseHTML(
                '<a href="#" class="color-choice" style="float:right; width:20px; height:20px; padding: 0px; margin-right:15px; border:none;">' +
                  '<span data-rwId="'+ termId +'" class="ico ontology-category-color-icon" style="background:'+catColor+'; border:1px solid #ccc; width:20px; height:20px; float:right; cursor:pointer;">icon</span>' +
                '</a>');

              // Add the color icon for category ontology nodes              
              $li.find('> div').append(thisLi);
              
            // ontology category layer type colors
            $(thisLi).find("span").colpick({
              submit: 0,  // removes the "ok" button which allows verification of selection and memory for last color
              onShow:function(colPickObj) {
            	var that = this;
                var currColor = LayerForm.rgb2hex($(this).css("background-color"));
                $(this).colpickSetColor(currColor,false);

                $(LayerForm.LAYER_MODAL).scroll(function(){  
                	var colorPicker = $(".colpick.colpick_full.colpick_full_ns:visible");
                	var colPick = $(that);
                	var diff = colPick.offset().top + colPick.height() + 2; 
                	var diffStr = diff.toString() + "px";
                	colorPicker.css({ top: diffStr });
                });
              },
              onChange: function(hsb,hex,rgb,el,bySetColor) {
                var hexStr = '#'+hex;
                $(el).css('background', hexStr);
                $(el).next(".color-input").attr('value', hexStr);                                  
              },
              /* checkable: true, */
              crud: {
                create: { // This configuration gets merged into the jquery create dialog.
                  height: 320
                },
                update: {
                  height: 320
                }
              }
            });
           }
          }
        });
        tree.render(this._elementId, nodes);
        
        // Set the color of the 'other' category selector icon
        $("#cat-other-color-selector").css('background', this._getCategoryColor(""));
        
        this.attachCategoryColorPickers();
      },
      
      /**
       * Gets the hex color code for an ontology category based on the term id 
       * 
       * @paarm nodeId - id of the node in the ui to get the color for.
       */
      _getCategoryColor : function(termId) {
        var catsJSONObj = $(this._storeId).data("categoriesstore");
          
        if(catsJSONObj){
          catsJSONObj = JSON.parse(decodeURIComponent(catsJSONObj));
            
          var catsJSONArr = catsJSONObj.catLiElems;
          
          if(catsJSONArr == null && Array.isArray(catsJSONObj)) {
            catsJSONArr = catsJSONObj;
          }          

          if(this.getImpl().length > 0){
            for(var i=0; i<catsJSONArr.length; i++){
              var cat = catsJSONArr[i];
              var catId = cat.id;
               
              if(catId === termId){
                return cat.color;
              }
              else if(catId === "cat-other-color-selector"){
                return cat.color;
              }
            }
          }
        }
          // if no match is found return the default
          return "#00bfff";
      },
      
      getCategories: function () {
          
        if(this.getImpl().length > 0) {
          var categories = [];
              
          var elements = this.getImpl().find(".ontology-category-color-icon");
              
          $.each(elements, function( index, element ) {
            var rwId = element.dataset.rwid;
                  
            var category = new Object();
            category.id = element.dataset.rwid; 
            category.val = element.parentElement.previousSibling.textContent;
            category.color = LayerForm.rgb2hex($(element).css("background-color"));
            category.isOntologyCat = true;
                            
            categories.push(category);
          });
              
          // Get the other category for the a layer create/edit form term tree
          if(this.getImpl().next().attr('id') === "other-cat-container"){
            var otherCat = new Object();
            otherCat.id = "cat-other-color-selector"; 
            otherCat.val = "Other";
            otherCat.color = LayerForm.rgb2hex($("#cat-other-color-selector").css("background-color"));
            otherCat.isOntologyCat = false;
            otherCat.otherCat = true;
            otherCat.otherEnabled =  $(".other-option-check-box").prop("checked");
                            
            categories.push(otherCat);
          }
                    
          return categories;
        }
            
        return null;        
      },
    }
  });
 
  var LayerForm = Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.LayerForm', {
    Extends : com.runwaysdk.ui.Component,  
    IsAbstract : true,    
    Constants : {
      LAYER_MODAL : '#modal01'
    },
    Instance : {
      initialize : function(map, mapId){
        this._map = map;
        this._mapId = mapId;
      },
      
      _onApplySuccess : {
        IsAbstract : true,            
      },        
      
      /**
       * Closes the overlay with the layer/style CRUD.
       * 
       */
      _closeLayerModal : function(){
        this.getImpl().modal('hide').html('');
      },

      getImpl : function(){
        return $(LayerForm.LAYER_MODAL);
      },
      
      /**
       * Adding font-family style property to dropdown options for better usability.
       * Adding this method was necessary because the ux js code does not account for style properties in dropdowns
       * 
       */
      _injectFontStylesForDropdown : function(){
        var convertedOptions = $(".select-options.drop-font-select").find("ul").children();
        var selectedOption = $(".select-font-select.select-area").find(".center");
        selectedOption.css("font-family", selectedOption.text());
        
        for(var i=0; i<convertedOptions.length; i++){
          var targetSpan = $(convertedOptions[i]).find("span");
          
          if(targetSpan.text().length > 0){
            targetSpan.css("font-family", targetSpan.text());
          }
        }
      },
      
      _addLayerFormControls : function(){
        // Scroll selector dropdown options on page scroll
        this.getImpl().scroll(function(){         
          var drops = $(".select-options");
          for(var i=0; i<drops.length; i++){
            var drop = $(drops[i]);
                
            if(!drop.hasClass("options-hidden")){
              var dropSelector = $(".select-active");
              var diff = dropSelector.offset().top + dropSelector.height() + 2; 
              var diffStr = diff.toString() + "px";
              drop.css({ top: diffStr });
            }
          }
          
        });
              
        this._injectFontStylesForDropdown();
      },          
      
      _setupCategoryColorPicker : function(elements) {
          
        // color dropdown buttons
        elements.colpick({
          submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
          onShow:function(colPickObj) {
        	var that = this;
            var currColor = LayerForm.rgb2hex($(this).find(".ico").css("background-color"));
            $(this).colpickSetColor(currColor,false);
            
            $(LayerForm.LAYER_MODAL).scroll(function(){  
            	var colorPicker = $(".colpick.colpick_full.colpick_full_ns:visible");
            	var colPick = $(that);
            	var diff = colPick.offset().top + colPick.height() + 2; 
            	var diffStr = diff.toString() + "px";
            	colorPicker.css({ top: diffStr });
            });
            
          },
          onChange:function(hsb,hex,rgb,el,bySetColor) {
            $(el).find(".ico").css('background','#'+hex);
            $(el).find('.color-input').attr('value', '#'+hex);
          }
        }); 
      },       
      
      _onLayerTypeTabChange : function(e) {
        var activeTab = e.target;
          
        var type = activeTab.dataset["gdbTabType"];
          
        if (type === "BASICPOLYGON") {
          $("#tab003basicpolygon").show();
        }
        else if (type === "BASICPOINT") {
            $("#tab001basicpoint").show();
        }
        else if (type === "BUBBLE") {
          $("#tab002bubble").show();
        }
        else if (type === "GRADIENTPOLYGON") {
          $("#tab004gradientpolygon").show();
        }
        else if (type === "CATEGORYPOLYGON") {
          $("#tab005categoriespolygon").show();
        }
      },
        
      /**
       * Handles the selection of colors from the color picker 
       * 
       * 
       */
      _selectColor : function(){
        var that = this;
        
        this._setupCategoryColorPicker($('.color-holder'));
        
        // category layer type colors
        $("#category-colors-container").find('.icon-color').colpick({
          submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
          onShow:function(colPickObj){
        	  var that = this;
              $(LayerForm.LAYER_MODAL).scroll(function(){  
              	var colorPicker = $(".colpick.colpick_full.colpick_full_ns:visible");
              	var colPick = $(that);
              	var diff = colPick.offset().top + colPick.height() + 2; 
              	var diffStr = diff.toString() + "px";
              	colorPicker.css({ top: diffStr });
              });
          },
          onChange:function(hsb,hex,rgb,el,bySetColor) {
            $(el).css('background','#'+hex);
            $(el).find('.color-input').attr('value', '#'+hex);          
          }
        });
        
        // ontology category layer type colors
        $(".ontology-category-color-icon").colpick({
          submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
          onShow:function(colPickObj){
        	  var that = this;
              $(LayerForm.LAYER_MODAL).scroll(function(){  
              	var colorPicker = $(".colpick.colpick_full.colpick_full_ns:visible");
              	var colPick = $(that);
              	var diff = colPick.offset().top + colPick.height() + 2; 
              	var diffStr = diff.toString() + "px";
              	colorPicker.css({ top: diffStr });
              });
          },
          onChange:function(hsb,hex,rgb,el,bySetColor) {
            $(el).css('background','#'+hex);
            $(el).next(".color-input").attr('value', '#'+hex);
          }
        });        
      },   
      
      /**
       * Handles the selection of layer type representation in the layer create/edit form
       * i.e. basic polygon, basic point, bubble, gradient, category, etc...
       * 
       */
      _selectLayerType : function(){
      
        var layerType = com.runwaysdk.geodashboard.gis.persist.DashboardLayer.LAYERTYPE;
        
        $('input:radio[name="layer.'+layerType+'"]').change(function(){   
              
          // hide all the styling options
          $.each($('.tab-pane'), function(){
            if($(this).is(":visible")){
              $(this).hide(); 
            }
          });
        });
      },
      
      
      /**
       * Renders the layer creation/edit form
       * 
       * @html
       */
      _displayLayerForm : function(html, layer){
        
        var that = this;
        
        // clear all previous color picker dom elements
        $(".colpick.colpick_full.colpick_full_ns").remove();
        
        // Show the white background modal.
        var modal = this.getImpl().first();
        modal.html(html);
                
        jcf.customForms.replaceAll(modal[0]);
        
        // Add layer styling event listeners
        this._selectColor();
        this._selectLayerType();
        
        // Move reusable cells to active cell holder
        var activeTab = $("#layer-type-styler-container").children(".tab-pane.active")[0].id;
        if (activeTab === "tab003basicpolygon") {
        	$("#tab003basicpolygon").show();
        }
        else if (activeTab === "tab001basicpoint") {
        	$("#tab001basicpoint").show();
        }
        else if (activeTab === "tab004gradientpolygon") {
        	$("#tab004gradientpolygon").show();
        }
        else if (activeTab === "tab005categoriespolygon") {
        	$("#tab005categoriespolygon").show();
        }
        
        // Attach listeners
        $('a[data-toggle="tab"]').on('shown.bs.tab', Mojo.Util.bind(this, this._onLayerTypeTabChange));
     
        // Attach event listeners for the universal (geo) aggregation dropdown.
        $(ThematicLayerForm.GEO_AGG_LEVEL_DD).change(function(){ 
          if($(ThematicLayerForm.GEO_AGG_LEVEL_DD+"option:selected").hasClass("universal-leaf")){
            // Hide the attribute aggregation dropdown because aggregations are irrelevant at this level of universal
            $(ThematicLayerForm.GEO_AGG_METHOD_DD).parent().parent().hide();
          }
          else{
            $(ThematicLayerForm.GEO_AGG_METHOD_DD).parent().parent().show();
          }
        });
        
        
        // Localize any existing number cateogry values
        $.each($('.category-input'), function() {
          var value = $(this).val();
          if(value != null && value.length > 0) {
            var categoryType = $(this).data("type");
            if(categoryType == "number") {
              var number = parseFloat(value);
              var localized = that._map.getFormatter()(number);
              
              $(this).val(localized);
            }
          }
        });    
        
        // IMPORTANT: This line must be run last otherwise the user will see javascript loading and modifying the DOM.
        //            It is better to finish all of the DOM modification before showing the modal to the user
        modal.modal('show');
      },

      /**
       * Called when a user submits (creates/updates) a layer with styles.
       * 
       * @params
       */
      _applyWithStyleListener : function(params){
        
        var that = this;
        
        var request = new com.runwaysdk.geodashboard.StandbyClientRequest({
          onSuccess : function(htmlOrJson, response){
            that._onApplySuccess(htmlOrJson, response);
          },
          onFailure : function(e){
            that.handleException(e);
          }
        }, this.getImpl()[0]);
        
        params['mapId'] = this._mapId;
        
        // Custom conversion to turn the checkboxes into boolean true/false
        params['style.enableLabel'] = params['style.enableLabel'].length > 0;
        
        // A hack to set the enableValue property which is required on DashboardLayer but is
        // not allowed for the reference layer form since ther is no 'value' to display
        if(!params['style.enableValue']){
          params['style.enableValue'] = false;
        }
        else{
          params['style.enableValue'] = params['style.enableValue'].length > 0;
        }
        params['layer.displayInLegend'] = params['layer.displayInLegend'].length > 0;
        
        return request;
      }
    },
    Static : {
      /**
       * Convert an RGB or RGBA string in the form RBG(255,255,255) to #ffffff
       * 
       */
      rgb2hex : function(rgb) {
        if(rgb != null) {
            
          if (/^#[0-9A-F]{6}$/i.test(rgb)){
            return rgb;
          }

          var rgbMatch = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
          if(rgbMatch){
            function hex(x) {
              return ("0" + parseInt(x).toString(16)).slice(-2);
            }
            return "#" + hex(rgbMatch[1]) + hex(rgbMatch[2]) + hex(rgbMatch[3]);
          }
            
          var rgbaMatch = rgb.match(/^rgba?[\s+]?\([\s+]?(\d+)[\s+]?,[\s+]?(\d+)[\s+]?,[\s+]?(\d+)[\s+]?/i);
          if(rgbaMatch){
            return (rgbaMatch && rgbaMatch.length === 4) ? "#" +
               ("0" + parseInt(rgbaMatch[1],10).toString(16)).slice(-2) +
               ("0" + parseInt(rgbaMatch[2],10).toString(16)).slice(-2) +
               ("0" + parseInt(rgbaMatch[3],10).toString(16)).slice(-2) : '';
          }
        }
      },
    }
  });
  
  var ThematicLayerForm = Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.ThematicLayerForm', {
    Extends : LayerForm,  
    Constants : {
        GEO_AGG_LEVEL_DD : "#agg-level-dd",
        GEO_AGG_METHOD_DD : "#agg-method-dd",
        GEO_AGG_HOLDER : "#agg-level-holder",
        GEO_TYPE_HOLDER : "#geom-type-holder",
        GEO_NODE_SELECT_EL : "#geonode-select"
    },
    Instance : {
          
      initialize : function(map, mapId){
        this.$initialize(map, mapId);
        
        this._LayerController = com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerController;        
        this._LayerController.setCancelListener(Mojo.Util.bind(this, this._cancelLayerListener));
        this._LayerController.setApplyWithStyleListener(Mojo.Util.bind(this, this._applyWithStyleListener));       
      },
      
      _displayLayerForm : function(html, layer) {
    	var that = this;
    	this.$_displayLayerForm(html, layer);
    	
        // Attach event listeners for the universal (geo) aggregation dropdown.
        this._setGeoAggEventListeners();
        
        // Populate the Aggregation Options based on the default selected GeoNode
        this._getGeographicAggregationOptions(layer);
        
        $(ThematicLayerForm.GEO_NODE_SELECT_EL).change(function(){ 
        	that._getGeographicAggregationOptions(layer);
        });
        
        this._thematicAttributeId = $("#categories-input").data('mdattributeid');
      },
      
      edit : function(id) {
        var that = this;
        
        var layer = this._map._layerCache.get(id);
        that._layer = layer;
      
        this._LayerController.edit(new Mojo.ClientRequest({
          onSuccess : function(html){
            that._displayLayerForm(html, that._layer);
            that._addLayerFormControls();
          },
          onFailure : function(e){
            that.handleException(e);
          }
        }), id);  
      },
      
      open : function(thematicAttributeId) {
        var that = this;
          
        var request = new Mojo.ClientRequest({
          onSuccess : function(html){
            that._displayLayerForm(html, "");
            that._addLayerFormControls();              
          },
          onFailure : function(e){
            that._closeLayerModal();
            that.handleException(e);
          }
        });
          
        this._LayerController.newThematicInstance(request, thematicAttributeId, this._mapId);      
      },
      
      
      /**
       * 
       * @params
       */
      _cancelLayerListener : function(params){
        var that = this;
        
        if(params['layer.isNew'] === 'true')
        {
          this._closeLayerModal();
        }
        else
        {
          var that = this;
          var request = new Mojo.ClientRequest({
            onSuccess : function(params){
              that._closeLayerModal();
            },
            onFailure : function(e){
              that.handleException(e);
            }
          });
          
          //return request;
          var id = params['layer.componentId'];
          com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.unlock(request, id);
        }
      },
      
      _onApplySuccess : function(htmlOrJson, response) {
        if (response.isJSON()) {
          this._closeLayerModal();
              
          var jsonObj = {};
          jsonObj["layers"] = [Mojo.Util.toObject(htmlOrJson)];
              
          this._map.handleLayerEvent(jsonObj);
              
          // TODO : Push this somewhere as a default handler.
          this.handleMessages(response);
        }
        else if (response.isHTML()) {
          // we got html back, meaning there was an error
              
          this._displayLayerForm(htmlOrJson, null);
          this._addLayerFormControls();
              
          this.getImpl().animate({scrollTop:$('.heading').offset().top}, 'fast'); // Scroll to the top, so we can see the error
        }
      },
      
      /**
       * Called when a user submits (creates/updates) a layer with styles.
       * 
       * @params
       */
      _applyWithStyleListener : function(params){
        
        var layer = this._map.getLayer(params["layer.componentId"]);
        var mdAttribute = (layer != null) ? layer.getValue('mdAttribute') : this._thematicAttributeId;
        
        params['layer.mdAttribute'] = mdAttribute;
        
        var conditions = this._map.getConditions();
        
        $.each(conditions, function( index, condition ) {
          params["conditions_" + index + ".comparisonValue"] = condition.getValue('comparisonValue');
          params["conditions_" + index + ".isNew"] = "true";
          params["#conditions_" + index + ".actualType"] = condition.getType();
          
          if(condition instanceof com.runwaysdk.geodashboard.gis.persist.condition.DashboardAttributeCondition) {
            params["conditions_" + index + ".definingMdAttribute"] = condition.getValue('definingMdAttribute');  
          }
        });      
        
        if($("#tab005categoriespolygon").is(":visible")){
          var catStyleArr = this._updateCategoriesJSON();
          params['style.categoryPolygonStyles'] = JSON.stringify(catStyleArr);
        }
        
        // Check for existense of dynamic settings which may not exist 
        if(params['style.bubbleContinuousSize']){
          params['style.bubbleContinuousSize'] = params['style.bubbleContinuousSize'].length > 0;
        }
        
        var secondaryAttribute = $("#secondaryAttribute").val();
        
        if(secondaryAttribute != null && secondaryAttribute.length > 0) {
          var secondaryCategories = this._getSecondaryAttriubteCategories();
          var secondaryAggregationType = $("#secondaryAggregation").val();
            
          if(secondaryCategories != null && secondaryCategories.length > 0){
            params['style.secondaryAttribute'] = secondaryAttribute;
            params['style.secondaryAggregationType'] = secondaryAggregationType;
            params['style.secondaryCategories'] = JSON.stringify(secondaryCategories);
          }          
        }
        else {
          params['style.secondaryAttribute'] = '';
          params['style.secondaryAggregationType'] = '';
          params['style.secondaryCategories'] = '';
        }
        
        // Get the strategy information
        var strategy = $(ThematicLayerForm.GEO_AGG_LEVEL_DD).find(":selected");
        var strategyType = strategy.data('type');
        var strategyValue = strategy.val();
        
        params["strategy.isNew"] = "true";
        params["#strategy.actualType"] = strategyType;
        
        if(strategyType == "com.runwaysdk.geodashboard.gis.persist.UniversalAggregationStrategy") {
          params["strategy.universal"] = strategyValue;
        }
        
        var request = this.$_applyWithStyleListener(params);
        
        return request;
      },
      
      
      /**
       * 
       * @layer - the layer object representing the layer the form is targeting 
       * 
       */
      _getGeographicAggregationOptions : function(layer){
    	  var that = this;
    	  
    	  // To set the option on a saved layer when initiating an edit session we get the
    	  // universal id OR geometry aggregation value id depending on the aggregation type of
    	  // the saved layer. 
    	  if(layer){
	    	  var universalOrGeomId = layer.aggregationStrategy.value;
	    	  if(universalOrGeomId){
	    		  that._selectedOption = universalOrGeomId;
	    	  }
	    	  else{
	    		  that._selectedOption = null;
	    	  }
    	  }
    	  var selectedVal = $(ThematicLayerForm.GEO_NODE_SELECT_EL+" option:selected").val();
    	  
      	  var clientRequest = new Mojo.ClientRequest({
            onSuccess : function(data) {
            	that._setGeographicAggregationOptions(data, that._selectedOption);
            },
            onFailure : function(e) {
              that.handleException(e);
            }
          });
          
          com.runwaysdk.geodashboard.gis.persist.AggregationStrategyView.getAggregationStrategies(clientRequest, selectedVal);
      },
      
      /**
       * 
       * @selectedVal - selected dropdown option element 
       * 
       */
      _getPossibleGeometryTypes : function(){
    	  var that = this;
    	  var selectedVal = $(ThematicLayerForm.GEO_NODE_SELECT_EL+" option:selected").val();
    	  var geomTypes;
    	  
      	  var clientRequest = new Mojo.ClientRequest({
            onSuccess : function(data) {
            	geomTypes = data;
            	console.log('in')
            	return geomTypes
            },
            onFailure : function(e) {
              that.handleException(e);
            }
          });
          
          com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.getGeoNodeGeometryTypesJSON(clientRequest, selectedVal);
          
          console.log("out")
          return geomTypes;
      },
      
      _setGeoAggEventListeners : function(){
    	  var that = this;
    	  
          // Attach event listeners for the universal (geo) aggregation dropdown.
          $(ThematicLayerForm.GEO_AGG_LEVEL_DD).change(function(){ 
            if($(ThematicLayerForm.GEO_AGG_LEVEL_DD+" option:selected").hasClass("universal-leaf")){
              // Hide the attribute aggregation dropdown because aggregations are irrelevant at this level of universal
              $(ThematicLayerForm.GEO_AGG_METHOD_DD).parent().parent().hide();
            }
            else{
              $(ThematicLayerForm.GEO_AGG_METHOD_DD).parent().parent().show();
            }
            
            var selectedOption = $(ThematicLayerForm.GEO_AGG_LEVEL_DD).find(":selected");
      	  	that._setLayerTypeOptions(selectedOption);
          });
      },
      
      /**
       * Populate the geographic aggregation dropdown
       * 
       * @aggregations - JSON representing geo aggregation levels
       */
      _setGeographicAggregationOptions : function(aggregations, selectedOption) {
    	  var selected = "";
    	  
    	  // Get the original name value from the originally rendered dropdown because this
    	  // data was already passed from server to client in the jsp
    	  var layerTypes = $(ThematicLayerForm.GEO_TYPE_HOLDER).data("layertypes");
    	  
    	  var html = '<select id="'+ThematicLayerForm.GEO_AGG_LEVEL_DD.replace("#", "")+'" class="method-select" data-layertypes="'+layerTypes+'" >';
    	  for(var i=0; i<aggregations.length; i++){
    		  var agg = aggregations[i];
    		  if(selectedOption === agg.getValue()){
    			  selected = "selected";
    		  }
    		  else if(i === 0){
    			  selected = "selected";
    		  }
    		  else{
    			  selected = "";
    		  }
    		  html += '<option value="' + agg.getValue() + '" data-type="'+agg.getAggregationType()+'" data-geomTypes="'+agg.getAvailableGeometryTypes()+'" '+selected+'>' + agg.getDisplayLabel() + '</option>';
    	  }
    	  html += '</select>';
          
          $(ThematicLayerForm.GEO_AGG_LEVEL_DD).parent().html(html);

    	  jcf.customForms.replaceAll($(ThematicLayerForm.GEO_AGG_LEVEL_DD).parent().get(0));
    	  
    	  $(ThematicLayerForm.GEO_AGG_HOLDER).show();
    	  
    	  var selectedOption = $(ThematicLayerForm.GEO_AGG_LEVEL_DD).find(":selected");
    	  this._setLayerTypeOptions(selectedOption);
    	  
    	  this._setGeoAggEventListeners();
      },
      
      
      /**
       * Populate the layer type block based on the selection of the geonode and geo aggregation level dropdown
       * 
       * @selectedOption - selected option from the geographic aggregation level dropdown
       */
      _setLayerTypeOptions : function(selectedOption) {
    	  var type = selectedOption.data('type');
   		  var layerTypes = $(ThematicLayerForm.GEO_TYPE_HOLDER).data("layertypes");
		  var layerTypesJSON = JSON.parse(decodeURIComponent(layerTypes));
    	  
    	  if(type === "com.runwaysdk.geodashboard.gis.persist.UniversalAggregationStrategy"){
    		  for(var i=0; i<layerTypesJSON.length; i++){
    			  var lType = layerTypesJSON[i];
    			  
    			  $("." + lType).show();
    		  }
    	  }
    	  else if (type === "com.runwaysdk.geodashboard.gis.persist.GeometryAggregationStrategy"){
    		  for(var i=0; i<layerTypesJSON.length; i++){
    			  var lType = layerTypesJSON[i];
    			  
    			  $("." + lType).hide();
    		  }
    		  
    		  var geomTypes = JSON.parse(decodeURIComponent(selectedOption.data("geomtypes")));
    		  
    		  for(var i=0; i<geomTypes.length; i++){
    			  var geomType = geomTypes[i];
    			  if(geomType === "geoPoint"){
    				  $(".BUBBLE").show();
    				  $(".BASICPOINT").show();
    			  }
    			  else if(geomType === "geoMultiPolygon"){
    				  $(".BASICPOLYGON").show();
    				  $(".CATEGORYPOLYGON").show();
    	    		  $(".GRADIENTPOLYGON").show();
    			  }
    			  else{
    				  //TODO: this needs to be an else if (geomType === "some geom type") which isnt defined yet
    			  }
    		  }
    	  }
    	  
    	  $(ThematicLayerForm.GEO_TYPE_HOLDER).show();
      },

      
      _addLayerFormControls : function(){
          
        $("#secondary-select-box").change(Mojo.Util.bind(this, this._handleSecondaryChange));
          
        if($("#ontology-tree").length > 0){
          
          this._categoryWidget = new CategoryTreeWidget("#ontology-tree", "#categories-input");
          this._categoryWidget.render();
          
          // category 'other' option
          $(".other-option-check-box").change(function() {
            if($(this).is(":checked")) {
              $("#other-cat-container").show();
            }
            else{
              $("#other-cat-container").hide();
            }     
          });
            
          // Simulate a checkbox click to turn off the checkbox if the 'other' option is disabled
          // The 'other' option is checked by default making this a valid sequence
          var otherEnabled = true;
          
          var catStore = $("#categories-input").data("categoriesstore");
          
          if(catStore.length > 0){
            var catJSON = JSON.parse(decodeURIComponent(catStore)).catLiElems;
            
            for(var i=0; i<catJSON.length; i++){
              var cat = catJSON[i];
              
              if(cat.id === "cat-other-color-selector"){
                otherEnabled = cat.otherEnabled;
              }
            }
          }
            
          if(!otherEnabled){
            $(".other-option-check-box").click();
          }
        }
        else if($(".category-input").length > 0){
          var categoryType = $("#categories-input").data('type');
          
          this._categoryWidget  = new com.runwaysdk.geodashboard.gis.CategoryListWidget(this._map, "#choice-color01", "#categories-input", true, "cat", this._thematicAttributeId, categoryType, ThematicLayerForm.GEO_AGG_METHOD_DD);
          this._categoryWidget.render();
          
          // category 'other' option
          $(".other-option-check-box").change(function() {
            if($(this).is(":checked")) {
              $("#cat-other").parent().parent().show();
            }
            else{
              $("#cat-other").parent().parent().hide();
            }     
          });
           
          attachCategoryColorPickers();
        }
          
        function attachCategoryColorPickers(){
          // ontology category layer type colors
          $(".category-color-icon").colpick({
            submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
            onShow:function(colPickObj){
            	var that = this;
                $(LayerForm.LAYER_MODAL).scroll(function(){  
                	var colorPicker = $(".colpick.colpick_full.colpick_full_ns:visible");
                	var colPick = $(that);
                	var diff = colPick.offset().top + colPick.height() + 2; 
                	var diffStr = diff.toString() + "px";
                	colorPicker.css({ top: diffStr });
                });
            },
            onChange:function(hsb,hex,rgb,el,bySetColor) {
              $(el).css('background','#'+hex);
              $(el).find('.color-input').attr('value', '#'+hex);
            }
          });
        }
          
        // Load the secondary values
        var secondaryAttribute = $("#secondaryAttribute").val();
          
        if(secondaryAttribute != null && secondaryAttribute.length > 0) {
          var type = $('#secondaryAttribute').find(":selected").data('type');
          if(type == 'com.runwaysdk.system.metadata.MdAttributeTerm') {          
            this._renderSecondaryTermTree(secondaryAttribute, type);
          }
          else {
            this._renderSecondaryCategoryGroup(secondaryAttribute, type);
            this._loadExistingCategories("#secondaryCategories", "secondaryCat", "#secondary-tree", false);
          }
        }
          
        // Scroll selector dropdown options on page scroll
        $(LayerForm.LAYER_MODAL).scroll(function(){         
          var drops = $(".select-options");
          
          for(var i=0; i<drops.length; i++){
            var drop = $(drops[i]);
            
            if(!drop.hasClass("options-hidden")){
              var dropSelector = $(".select-active");
              var diff = dropSelector.offset().top + dropSelector.height() + 2; 
              var diffStr = diff.toString() + "px";
              drop.css({ top: diffStr });
            }
          }
        });
          
        this._injectFontStylesForDropdown();
      },
      
      /**
       * Handles the selection of colors from the color picker 
       * 
       * 
       */
      _selectColor : function(){
        var that = this;
        
        this._setupCategoryColorPicker($('.color-holder'));
        
        // category layer type colors
        $("#category-colors-container").find('.icon-color').colpick({
          submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
          onShow:function(colPickObj){
        	  var that = this;
              $(LayerForm.LAYER_MODAL).scroll(function(){  
              	var colorPicker = $(".colpick.colpick_full.colpick_full_ns:visible");
              	var colPick = $(that);
              	var diff = colPick.offset().top + colPick.height() + 2; 
              	var diffStr = diff.toString() + "px";
              	colorPicker.css({ top: diffStr });
              });
          },
          onChange:function(hsb,hex,rgb,el,bySetColor) {
            $(el).css('background','#'+hex);
            $(el).find('.color-input').attr('value', '#'+hex);          
          }
        });
        
        // ontology category layer type colors
        $(".ontology-category-color-icon").colpick({
          submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
          onShow:function(colPickObj){
        	  var that = this;
              $(LayerForm.LAYER_MODAL).scroll(function(){  
              	var colorPicker = $(".colpick.colpick_full.colpick_full_ns:visible");
              	var colPick = $(that);
              	var diff = colPick.offset().top + colPick.height() + 2; 
              	var diffStr = diff.toString() + "px";
              	colorPicker.css({ top: diffStr });
              });
          },
          onChange:function(hsb,hex,rgb,el,bySetColor) {
            $(el).css('background','#'+hex);
            $(el).next(".color-input").attr('value', '#'+hex);
          }
        });        
      },   

      /**
       * Handles the selection of layer type representation in the layer create/edit form
       * i.e. basic point, basic polygon, bubble, gradient, category, etc...
       * 
       */
      _selectLayerType : function(){
      
        var layerType = com.runwaysdk.geodashboard.gis.persist.DashboardLayer.LAYERTYPE;
        
        $('input:radio[name="layer.'+layerType+'"]').change(function(){   
              
          // hide all the styling options
          $.each($('.tab-pane'), function(){
            if($(this).is(":visible")){
              $(this).hide(); 
            }
          });
        });
      },
      
      /**
       * Hooks the auto-complete functionality to the category field input fields
       * 
       * 
       */
      _addCategoryAutoComplete : function(parent, aggregationId){
        
        var that = this;
        
        // Hook up the auto-complete for category input options 
        $(parent).find('.category-input').each(function(){
          var mdAttribute = $(this).data('mdattributeid');  
          var categoryType = $(this).data('type');
          
          $(this).autocomplete({
            source: function( request, response ) {
              var req = new Mojo.ClientRequest({
                onSuccess : function(results){
                  
                  // We need to localize the results for numbers
                  if(categoryType == 'number') {
                    for(var i = 0; i < results.length; i++) {
                      var number = parseFloat(results[i]);
                      var localized = that._formatter(number);
                      
                      results[i] = localized;
                    }
                  }
                  
                  response( results );
                },
                onFailure : function(e){
                  that.handleException(e);
                }
              });
              
              // values are scraped from hidden input elements on the layer create form
              var universalId = $(ThematicLayerForm.GEO_AGG_LEVEL_DD).val();
              var aggregationVal = $(aggregationId).val();
              var criteria = that._reloadCriteria();
              var conditions = that._getConditionsFromCriteria(criteria);
              
              com.runwaysdk.geodashboard.Dashboard.getCategoryInputSuggestions(req, mdAttribute, universalId, aggregationVal, request.term, 10, conditions);
            },
            minLength: 1
          });

          this._setupCategoryColorPicker($("#choice-color01").find('.color-holder'));
        })
          
        // Load the secondary values
        var secondaryAttribute = $("#secondaryAttribute").val();
          
        if(secondaryAttribute != null && secondaryAttribute.length > 0) {
        
          var type = $('#secondaryAttribute').find(":selected").data('type');
            
          if(type == 'com.runwaysdk.system.metadata.MdAttributeTerm') {          
           
            this._renderSecondaryTermTree(secondaryAttribute, type);
          }
          else {
            this._renderSecondaryCategoryGroup(secondaryAttribute, type);
          }
        }
        
        this.$_addLayerFormControls();          
      },
      

      _renderSecondaryAggregation : function(type) {
        
        var options = this._map.getAggregationMap()[type];
      
        if(options == null) {
          options = [];
        }
      
        var html = '<select id="secondaryAggregation" class="method-select" name="secondaryAggregation">';
        
        for(var i = 0; i < options.length; i++) {
          var option = options[i];
          
          html += '<option value="' + option.value + '">' + option.label + '</option>';
        }
        
        html += '</select>';
        
        $("#secondary-aggregation-container").html(html);
        
        // Set the saved value
        var value = $('#secondaryAggregationValue').val();        
        $("#secondaryAggregation").val(value);
      },
      
      _renderSecondaryCategoryGroup : function(mdAttributeId, type) {
        $('#secondary-content').hide();
        
        this._renderSecondaryAggregation(type);
        
        var categoryType = this._getCategoryType(type);
        
        var html =
          '<div class="color-section">' +
            '<div class="heading-list">' +
              '<span>' + com.runwaysdk.Localize.localize("DashboardLayer.form", "category") + '</span>' +
              '<span>' + com.runwaysdk.Localize.localize("DashboardLayer.form", "color") + '</span>' +
              '<span></span>'+
            '</div>' +
            '<div class="category-block">' +
              '<div class="panel-group choice-color category-group">' +
                '<div class="panel">' +
                  '<div id="secondary-choice-color" class="panel-collapse">' +
                  '</div>' +                              
                '</div>' +              
              '</div>' +            
            '</div>' +
          '</div>';          
                 
        $('#secondary-cateogries').html(html);
        
        
        this._secondaryWidget  = new com.runwaysdk.geodashboard.gis.CategoryListWidget(this._map, "#secondary-choice-color", "#secondaryCategories", false, "secondary", mdAttributeId, categoryType, "#secondaryAggregation");
        this._secondaryWidget.render();
        
        this._setupCategoryColorPicker($('#secondary-content').find('.color-holder'));
        
        jcf.customForms.replaceAll($('#secondary-content').get(0));
        
        $('#secondary-content').show();        
      },
      
      _renderSecondaryTermTree : function(mdAttributeId, type) {
        $('#secondary-content').hide();
        
        this._renderSecondaryAggregation(type);
        
        var html =
         '<div class="color-section">' +
           '<div class="heading-list">' +
             '<span>' + com.runwaysdk.Localize.localize("DashboardLayer.form", "category") + '</span>' +
             '<span>' + com.runwaysdk.Localize.localize("DashboardLayer.form", "color") + '</span>' +
             '<span></span>'+
           '</div>' +              
           '<div class="category-block">' +
             '<div class="ontology-category-input-container">' +
             '<div id="secondary-tree"></div>' +
           '</div>' +
         '</div>';
            
        $('#secondary-cateogries').html(html);

        // Get the term roots and setup the tree widget
        var that = this;
        var req = new Mojo.ClientRequest({
          onSuccess : function(results){
            var nodes = JSON.parse(results);
            var rootTerms = [];
                
            for(var i = 0; i < nodes.length; i++) {
              var id = nodes[i].id;              
              rootTerms.push({termId : id});
            }
            
            that._secondaryWidget = new CategoryTreeWidget("#secondary-tree", '#secondaryCategories');
            that._secondaryWidget.render(rootTerms, nodes);
            
            $('#secondary-content').show();
          },
          onFailure : function(e){
            that.handleException(e);
          }
        });
        
        jcf.customForms.replaceAll($('#secondary-content').get(0));
            
        com.runwaysdk.geodashboard.Dashboard.getClassifierTree(req, mdAttributeId);            
      },
      
      _handleSecondaryChange : function(e){
        var option = e.target.selectedOptions[0];
        var mdAttributeId = option.value;
        var type = $(option).data("type");
        
        if(mdAttributeId == '') {
          $('#secondary-content').hide();
        }
        else if(type == 'com.runwaysdk.system.metadata.MdAttributeTerm') {
          this._renderSecondaryTermTree(mdAttributeId, type);
        }
        else {
          this._renderSecondaryCategoryGroup(mdAttributeId, type);
        }
      },
      
      _getCategoryType : function(type) {
        if(type == 'com.runwaysdk.system.metadata.MdAttributeDouble' || type == 'com.runwaysdk.system.metadata.MdAttributeInteger') {
          return 'number';
        }
        else if(type == 'com.runwaysdk.system.metadata.MdAttributeDate') {
          return 'date';
        }
        
        return 'text';
      },
      
      _getSecondaryAttriubteCategories : function() {
        if(this._secondaryWidget != null) {
          var categories = this._secondaryWidget.getCategories();
              
          return categories;
        }
            
        return null;
      },
      
      /**
       * Scrapes categories on the layer creation/edit form
       * 
       */
      _updateCategoriesJSON : function() {
        var styleArr = new Object();
        styleArr.catLiElems = this._categoryWidget.getCategories();
           
        // set the hidden input element in the layer creation/edit form 
        $("#categories-input").data("categoriesstore", encodeURIComponent(JSON.stringify(styleArr)));
           
        return  styleArr;
      }
    }
  });
  
  var ReferenceLayerForm = Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.ReferenceLayerForm', {
    Extends : LayerForm,  
      
    Instance : {
            
      initialize : function(map, mapId){
        this.$initialize(map, mapId);
      
        this._ReferenceLayerController = com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerController;
        this._ReferenceLayerController.setCancelListener(Mojo.Util.bind(this, this._cancelReferenceLayerListener));
        this._ReferenceLayerController.setApplyWithStyleListener(Mojo.Util.bind(this, this._applyWithStyleListener)); 
      },
      
      _onApplySuccess : function(htmlOrJson, response) {
        if (response.isJSON(htmlOrJson, response)) {
          this._closeLayerModal();
              
          var returnedLayerJSON = JSON.parse( htmlOrJson);
          var jsonObj = {};
          jsonObj["refLayers"] = [Mojo.Util.toObject(htmlOrJson)];
              
          this._map.handleReferenceLayerEvent(jsonObj)
              
          this.handleMessages(response);
        }
        else if (response.isHTML()) {
          // we got html back, meaning there was an error
              
          this._displayLayerForm(htmlOrJson);
          this._addLayerFormControls();
              
          this.getImpl().animate({scrollTop:$('.heading').offset().top}, 'fast'); // Scroll to the top, so we can see the error
        }        
      },
      
      /**
       * 
       * @params
       */
      _cancelReferenceLayerListener : function(params){
        var that = this;
        
        if(params['layer.isNew'] === 'true')
        {
          this._closeLayerModal();
        }
        else
        {
          var that = this;
          var request = new Mojo.ClientRequest({
            onSuccess : function(params){
              that._closeLayerModal();
            },
            onFailure : function(e){
              that.handleException(e);
            }
          });
          
          //return request;
          var id = params['layer.componentId'];
          com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayer.unlock(request, id);
        }
      },
      
      /**
       * Edit form for reference layers  
       * 
       * @param e
       */
      edit : function(id){
        var that = this;
      
        // edit the layer
        var request = new Mojo.ClientRequest({
          onSuccess : function(html){
            that._displayLayerForm(html);
            that._addLayerFormControls();
          },
          onFailure : function(e){
            that.handleException(e);
          }
        });
      
        this._ReferenceLayerController.edit(request, id);
      },
      
      open : function(universalId){
        var that = this;
      
        var request = new Mojo.ClientRequest({
          onSuccess : function(html){
            that._displayLayerForm(html);
            that._addLayerFormControls();
                  
          },
          onFailure : function(e){
            that._closeLayerModal();
            that.handleException(e);
          }
        });
              
        this._ReferenceLayerController.newReferenceInstance(request, universalId, this._mapId);        
      }
    }
  });
  
})();
  
  
