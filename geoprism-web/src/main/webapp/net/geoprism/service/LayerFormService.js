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
  /*
   * 
   * 
   * THEMATIC LAYER SERVICE
   * 
   * 
   */
  function LayerFormService(runwayService) {
    var service = {};
    
    service.createRequest = function(onSuccess, onFailure){
      var request = new Mojo.ClientRequest({
        onSuccess : onSuccess,
        onFailure : function(e) {
          if(onFailure != null) {
            onFailure(e);            	
          }
          else {
            GDB.ExceptionHandler.handleException(e);            	
          }
        }
      });
        
      return request;
    }
    
    
    service.createStandbyRequest = function(elementId, onSuccess, onFailure){
      var el = $(elementId);
        
      if(el.length > 0) {      
        var request = new GDB.StandbyClientRequest({
          onSuccess : onSuccess,
          onFailure : function(e){
            if(onFailure != null) {
              onFailure(e);            
            }
            else {
              GDB.ExceptionHandler.handleException(e);            
            }
          }
        }, elementId);
          
        return request;        
      }
        
      return service.createRequest(onSuccess, onFailure);
    }
    
    service.getCategoryValues = function(categories, categoryType) {
      var array = [];
                             
      for(var i = 0; i < categories.catLiElems.length; i++) {
        var category = categories.catLiElems[i];
                 
        // Only send back categories which have values
        if(category.val != null && (typeof category.val !== 'string' || (category.val.length > 0 || category.rangeAllMin))) {        	
          var object = {};
          angular.copy(category, object);
                 
          object.otherEnabled = categories.otherEnabled;
                   
          array.push(object);            
        }
      }
        
      if(categories.otherEnabled) {
        array.push(categories.other);
      }
               
      return {catLiElems:array};
    }
    
    service.apply = function(layer, style, dynamicDataModel, categoryWidget, state, element, onSuccess, onFailure) {
      // Populate the layer
      runwayService.populate(service.layerDTO, layer);      

      // Populate the style
      runwayService.populate(service.styleDTO, style);  
      
      var categoryType = dynamicDataModel.thematicAttributeDataType;

      // Update the style model to include the point and polygon category values
      service.styleDTO.setCategoryPointStyles(JSON.stringify(service.getCategoryValues(categoryWidget.basicPointCatOptionsObj, categoryType)));
      service.styleDTO.setCategoryPolygonStyles(JSON.stringify(service.getCategoryValues(categoryWidget.polygonCatOptionsObj, categoryType)));
           
      // Update the secondary attribute values       
      service.styleDTO.clearSecondaryAggregationType();
      
      if(style.secondaryAggregation.attribute.id != 'NONE'){    	  
        var secondaryCategoryType = style.secondaryAggregation.attribute.categoryType;    	  
    	  
        service.styleDTO.setValue('secondaryAttribute', style.secondaryAggregation.attribute.mdAttributeId);
        service.styleDTO.addSecondaryAggregationType(style.secondaryAggregation.method.value);          
        service.styleDTO.setSecondaryCategories(JSON.stringify(service.getCategoryValues(style.secondaryAggregation, secondaryCategoryType).catLiElems));
      }
      else {
        service.styleDTO.setValue('secondaryAttribute', '');
        service.styleDTO.setSecondaryCategories('[]');
      }
      
      // Create a new strategy DTO: It replaces the current DTO
      var strategy = dynamicDataModel.aggregationStrategyMap[dynamicDataModel.aggregationStrategy];
      var strategyDTO = null;
      
      if(strategy.type.indexOf("UniversalAggregationStrategy")  > -1){
        strategyDTO = new net.geoprism.dashboard.UniversalAggregationStrategy();
      }
      else if(strategy.type.indexOf("GeometryAggregationStrategy")  > -1){
        strategyDTO = new net.geoprism.dashboard.GeometryAggregationStrategy();
      }
        
      // the universal property will be skipped for geometry agg in the populate method because that property
      // doesn't exist on the server object.
      runwayService.populate(strategyDTO, {
        "id": strategy.id, 
        "universal": strategy.value
      });
        
      var request = service.createStandbyRequest(element, onSuccess, onFailure);
      
      service.layerDTO.applyWithStyleAndStrategy(request, service.styleDTO, state.mapId, strategyDTO, state);
    }      
      
    service.unlock = function(layer, element, onSuccess, onFailure) {
      var success = function() {
        service.layerDTO = null;
        service.styleDTO = null;
          
        onSuccess();        
      }

      if(service.layerDTO == null || service.layerDTO.isNewInstance()) {
        success();
      }
      else {
        var request = service.createStandbyRequest(element, success, onFailure);

        service.layerDTO.unlock(request);
      }        
    }
      
    /**
     * Convert response from the server into the objects that
     * the front-end controller is expecting.
     **/
    service.createObjects = function(response, isNew) {
      service.layerDTO = com.runwaysdk.DTOUtil.convertToType(response.layerDTO);
      service.styleDTO = com.runwaysdk.DTOUtil.convertToType(response.styleDTO);
      
      var options = response.options;
      
      /**
       * Dymanic model properties may be changed throughout the user experience. 
       * They are kept out of other models to keep those models replicating server models more closely.
       */
      var dynamicDataModel = {
        aggregationStrategyOptions : [],
        aggregationStrategyMap : {},        
        aggregationStrategy : '', // using dynamicDataModel because the json representation is different from the actual model
        aggregationMethods : [], 
        aggregationMap : {},
        layerTypeNames : [],
        layerTypeLabels : [],
        secondaryAttributes : [],
        secondaryAggregationMethods : [], 
        isOntologyAttribute : false,
        isTextAttribute : false,
        ontologyNodes : [],
        termType : '',
        relationshipType : '',
        thematicAttributeDataType : '',
        availableFonts : [],
        geoNodes : []
      };
      
      dynamicDataModel.aggregationMethods = options.aggregations;
      dynamicDataModel.nodeAggregationStrategiesLookup = options.aggegationStrategies;
      dynamicDataModel.aggregationStrategyOptions = options.aggegationStrategies[0].aggregationStrategies;
      dynamicDataModel.layerTypeNames = options.layerTypeNames;
      dynamicDataModel.layerTypeLabels = options.layerTypeLabels;
      dynamicDataModel.pointTypes = options.pointTypes;
      dynamicDataModel.secondaryAttributes = options.secondaryAttributes;
      dynamicDataModel.aggregationMap = JSON.parse(options.aggregationMap);
        
      dynamicDataModel.thematicAttributeDataType = options.attributeDataType;
      dynamicDataModel.isOntologyAttribute = options.attributeType.isOntologyAttribute;
      dynamicDataModel.isTextAttribute = options.attributeType.isTextAttribute;
        
      dynamicDataModel.availableFonts = service.processFonts(options.fonts);
      dynamicDataModel.geoNodes = options.geoNodes;
        
      if(options.attributeType.isOntologyAttribute){
        dynamicDataModel.ontologyNodes = options.attributeType.nodes;
        dynamicDataModel.optionCount = options.attributeType.optionCount;
        dynamicDataModel.termType = options.attributeType.termType;
        dynamicDataModel.relationshipType = options.attributeType.relationshipType;
      }
      
      // These properties should be set in setLayerState for existing layers (editing a layer)
      if(isNew){
        dynamicDataModel.aggregationStrategy = options.aggegationStrategies[0].aggregationStrategies[0].value;
      }
      else {
        dynamicDataModel.aggregationStrategy = response.layer.aggregationStrategy.value;
      }
      
      // For faster lookup create a map of the aggregation strategy options
      for(var i=0; i < dynamicDataModel.aggregationStrategyOptions.length; i++){
        var strategy = dynamicDataModel.aggregationStrategyOptions[i];
        
        dynamicDataModel.aggregationStrategyMap[strategy.value] = strategy;
      }

      /**
       * Model for a thematic layer
       */
      var layer = {
        newInstance : true,
        nameLabel : '',
        displayInLegend : true,
        mdAttribute : '',
        layerType : '',
        geoNode : null,
        aggregationType : '',
        attributeType : '',  // not on the dto
        aggregationMethod : '', // not on the dto
        aggregationAttribute : '',  // not on the dto
        attributeLabel : '',  // not on the dto
      };

      layer.newInstance = service.layerDTO.isNewInstance();
      layer.nameLabel = response.layer.layerName;
      layer.mdAttribute = response.layer.mdAttributeId;
      layer.attributeLabel = response.layer.attributeLabel;
      layer.layerType = response.layer.featureStrategy;
      layer.displayInLegend = response.layer.inLegend;
      
      // These properties should be set in setLayerState for existing layers (editing a layer)
      if(isNew){
        layer.aggregationMethod = dynamicDataModel.aggregationMethods[0];
        layer.aggregationType = dynamicDataModel.aggregationMethods[0].method;
        layer.geoNode = options.geoNodes[0].id;
      } 
      else {
        layer.aggregationMethod = response.layer.aggregationMethod;
        layer.aggregationType = aggregationMethod = response.layer.aggregationMethod;
        layer.geoNode = response.layer.geoNodeId;
      }
      
      /**
       * Model for a thematic style of a thematic layer
       */
      var style = {
        basicPointSize : 10,
        enableLabel : true,
        enableValue : true,
        labelColor : '#000000',
        labelFont : '', 
        labelHalo : '#ffffff',
        labelHaloWidth : 2,
        labelSize : 12,
        lineOpacity : 0.9,
        lineStroke : '#000000',
        lineStrokeCap : '',
        lineStrokeWidth : 2,
        pointFill : '#00bfff',
        pointOpacity : 0.9,
        pointRotation : '',
        pointStroke : '#000000',
        pointStrokeOpacity : 0.9,
        pointStrokeWidth : 1,
        pointWellKnownName : 'CIRCLE',
        polygonFill : '#00bfff',
        polygonFillOpacity : 0.9,
        polygonStroke : '#000000',
        polygonStrokeOpacity : 0.9,
        polygonStrokeWidth : 1,
        valueColor : '#000000',
        valueFont : '',
        valueHalo : '#ffffff',
        valueHaloWidth : 2,
        valueSize : '12',
        bubbleContinuousSize : true,
        bubbleFill : '#00bfff',
        bubbleMaxSize : 70,
        bubbleMinSize : 20,
        bubbleOpacity : 0.9,
        bubbleRotation : '',
        bubbleSize : 10,
        bubbleStroke : '#000000',
        bubbleStrokeOpacity : 0.9,
        bubbleStrokeWidth : 1,
        bubbleWellKnownName : 'CIRCLE',
        categoryPointFillOpacity : 0.9,
        categoryPointSize : 10,
        categoryPointStroke : '#000000',
        categoryPointStrokeOpacity : 0.9,
        categoryPointStrokeWidth : 1,
        categoryPointStyles : '',
        categoryPointWellKnownName : 'CIRCLE',
        categoryPolygonFillOpacity : 0.9,
        categoryPolygonStroke : '#000000',
        categoryPolygonStrokeOpacity : 0.9,
        categoryPolygonStrokeWidth : 1,
        categoryPolygonStyles : '',
        gradientPointFillOpacity : 0.9,
        gradientPointMaxFill : '#505050',
        gradientPointMinFill : '#ffffff',
        gradientPointSize : 10,
        gradientPointStroke : '#000000',
        gradientPointStrokeOpacity : 0.9,
        gradientPointStrokeWidth : 1,
        gradientPointWellKnownName : 'CIRCLE',
        gradientPolygonFillOpacity : 0.9,
        gradientPolygonMaxFill : '#505050',
        gradientPolygonMinFill : '#ffffff',
        gradientPolygonStroke : '#000000',
        gradientPolygonStrokeOpacity : 0.9,
        gradientPolygonStrokeWidth : 1,
        secondaryAggregation : {
          attribute : {label : 'None', id : 'NONE'},
          method : {},
          otherEnabled : false,
          rangeCategoriesEnabled : false,
          other : {"val":"","color":"#737678","isOntologyCat":false,"isRangeCat":false,"otherEnabled":false,"otherCat":true,"enableIcon":false,"geomType":"POINT"},
          catLiElems : [
                  {"val":"","color":"#000000","isOntologyCat":false,"isRangeCat":false,"otherEnabled":false,"otherCat":false,"enableIcon":false,"geomType":"POINT"},
                  {"val":"","color":"#000000","isOntologyCat":false,"isRangeCat":false,"otherEnabled":false,"otherCat":false,"enableIcon":false,"geomType":"POINT"},
                  {"val":"","color":"#000000","isOntologyCat":false,"isRangeCat":false,"otherEnabled":false,"otherCat":false,"enableIcon":false,"geomType":"POINT"},
                  {"val":"","color":"#000000","isOntologyCat":false,"isRangeCat":false,"otherEnabled":false,"otherCat":false,"enableIcon":false,"geomType":"POINT"},
                  {"val":"","color":"#000000","isOntologyCat":false,"isRangeCat":false,"otherEnabled":false,"otherCat":false,"enableIcon":false,"geomType":"POINT"}]        
          
        },
        styleCondition : '',
        numBubbleSizeCategories : 5,
        numGradientPointCategories : 5,
        numGradientPolygonCategories : 5
      };
      
      // Populate the values of style from the server DTO
      runwayService.populateObject(style, service.styleDTO);
        
      // WKN values come from the server lower cased
      // but the UI model expects them to be upper cased values
      style.pointWellKnownName = style.pointWellKnownName.toUpperCase();
      style.bubbleWellKnownName = style.bubbleWellKnownName.toUpperCase();
      style.categoryPointWellKnownName = style.categoryPointWellKnownName.toUpperCase();
      style.gradientPointWellKnownName = style.gradientPointWellKnownName.toUpperCase();

      // Ensure that the current font is actually available on the system
      // If not default to the first option
      if(!service.isValidFont(style.labelFont, dynamicDataModel.availableFonts)) {
        style.labelFont = dynamicDataModel.availableFonts[0];
      }
      
      if(!service.isValidFont(style.valueFont, dynamicDataModel.availableFonts)) {
        style.valueFont = dynamicDataModel.availableFonts[0];          
      }
      
      /**
       * Model for category widgets 
       */
      var categoryWidget = {
        basicPointCatOptionsObj : {
          otherEnabled : true,      
          rangeCategoriesEnabled : false,
          other : {"val":"","color":"#737678","isOntologyCat":false,"isRangeCat":false,"rangeAllMin":false,"rangeAllMax":false,"otherEnabled":true,"otherCat":true,"enableIcon":false,"geomType":"POINT"},
          catLiElems : [
                  {"val":"","color":"#1b9e77","isOntologyCat":false,"isRangeCat":false,"rangeAllMin":false,"rangeAllMax":false,"otherEnabled":true,"otherCat":false,"enableIcon":false,"geomType":"POINT"},
                  {"val":"","color":"#d95f02","isOntologyCat":false,"isRangeCat":false,"rangeAllMin":false,"rangeAllMax":false,"otherEnabled":true,"otherCat":false,"enableIcon":false,"geomType":"POINT"},
                  {"val":"","color":"#7570b3","isOntologyCat":false,"isRangeCat":false,"rangeAllMin":false,"rangeAllMax":false,"otherEnabled":true,"otherCat":false,"enableIcon":false,"geomType":"POINT"},
                  {"val":"","color":"#e7298a","isOntologyCat":false,"isRangeCat":false,"rangeAllMin":false,"rangeAllMax":false,"otherEnabled":true,"otherCat":false,"enableIcon":false,"geomType":"POINT"},
                  {"val":"","color":"#66a61e","isOntologyCat":false,"isRangeCat":false,"rangeAllMin":false,"rangeAllMax":false,"otherEnabled":true,"otherCat":false,"enableIcon":false,"geomType":"POINT"}
          ]
        },
        polygonCatOptionsObj : {
          otherEnabled : true,
          rangeCategoriesEnabled : false,
          other : {"val":"","color":"#737678","isOntologyCat":false,"isRangeCat":false,"otherEnabled":true,"otherCat":true,"geomType":"POLYGON"},
          catLiElems:[
                  {"val":"","color":"#1b9e77","isOntologyCat":false,"isRangeCat":false,"rangeAllMin":false,"rangeAllMax":false,"otherEnabled":true,"otherCat":false,"geomType":"POLYGON"},
                  {"val":"","color":"#d95f02","isOntologyCat":false,"isRangeCat":false,"rangeAllMin":false,"rangeAllMax":false,"otherEnabled":true,"otherCat":false,"geomType":"POLYGON"},
                  {"val":"","color":"#7570b3","isOntologyCat":false,"isRangeCat":false,"rangeAllMin":false,"rangeAllMax":false,"otherEnabled":true,"otherCat":false,"geomType":"POLYGON"},
                  {"val":"","color":"#e7298a","isOntologyCat":false,"isRangeCat":false,"rangeAllMin":false,"rangeAllMax":false,"otherEnabled":true,"otherCat":false,"geomType":"POLYGON"},
                  {"val":"","color":"#66a61e","isOntologyCat":false,"isRangeCat":false,"rangeAllMin":false,"rangeAllMax":false,"otherEnabled":true,"otherCat":false,"geomType":"POLYGON"}
          ]
        }
      };
      
      // Update the point and polygon category model
      service.loadCategoryValues(categoryWidget.polygonCatOptionsObj, service.styleDTO.getCategoryPolygonStyles(), dynamicDataModel.thematicAttributeDataType);
      service.loadCategoryValues(categoryWidget.basicPointCatOptionsObj, service.styleDTO.getCategoryPointStyles(), dynamicDataModel.thematicAttributeDataType);
      style.secondaryAggregation = service.loadSecondaryAggregation(dynamicDataModel, service.styleDTO);
      
      return {layer:layer, style:style, categoryWidget:categoryWidget, dynamicDataModel:dynamicDataModel};    
    }
      
    service.loadSecondaryAggregation = function(dynamicDataModel, styleDTO) {
      var aggregation = {
        attribute : {label : 'None', id : 'NONE'},
        method : {},
        otherEnabled : false,
        rangeCategoriesEnabled : false,
        other : {"val":"","color":"#737678","isOntologyCat":false,"isRangeCat":false,"rangeAllMin":false,"rangeAllMax":false,"otherEnabled":true,"otherCat":true,"enableIcon":false,"geomType":"POINT"},
        catLiElems : [
          {"val":"","color":"#000000","isOntologyCat":false,"isRangeCat":false,"rangeAllMin":false,"rangeAllMax":false,"otherEnabled":true,"otherCat":false,"enableIcon":false,"geomType":"POINT"},
          {"val":"","color":"#000000","isOntologyCat":false,"isRangeCat":false,"rangeAllMin":false,"rangeAllMax":false,"otherEnabled":true,"otherCat":false,"enableIcon":false,"geomType":"POINT"},
          {"val":"","color":"#000000","isOntologyCat":false,"isRangeCat":false,"rangeAllMin":false,"rangeAllMax":false,"otherEnabled":true,"otherCat":false,"enableIcon":false,"geomType":"POINT"},
          {"val":"","color":"#000000","isOntologyCat":false,"isRangeCat":false,"rangeAllMin":false,"rangeAllMax":false,"otherEnabled":true,"otherCat":false,"enableIcon":false,"geomType":"POINT"},
          {"val":"","color":"#000000","isOntologyCat":false,"isRangeCat":false,"rangeAllMin":false,"rangeAllMax":false,"otherEnabled":true,"otherCat":false,"enableIcon":false,"geomType":"POINT"}          
        ]
      };
      
      var mdAttributeId = styleDTO.getValue('secondaryAttribute');
      
      if(mdAttributeId != null && mdAttributeId != '') {
          
        // Load the attribute value
        for(var i = 0; i < dynamicDataModel.secondaryAttributes.length; i++) {
          var attribute = dynamicDataModel.secondaryAttributes[i];
            
          if(attribute.mdAttributeId == mdAttributeId) {
            aggregation.attribute = attribute;  
          }
        }

        // Load the method value
        var aggregationType = styleDTO.getSecondaryAggregationType()[0].name();      
        var options = dynamicDataModel.aggregationMap[aggregation.attribute.type];
        
        dynamicDataModel.secondaryAggregationMethods = options;
          
        for(var i = 0; i < dynamicDataModel.secondaryAggregationMethods.length; i++) {
          var method = dynamicDataModel.secondaryAggregationMethods[i];
            
          if(method.value == aggregationType) {
            aggregation.method = method;  
          }
        }
        
        var categoryType = aggregation.attribute.categoryType;
        
        // Load the categories
        service.loadCategoryValues(aggregation, styleDTO.getSecondaryCategories(), categoryType);
      }
        
      return aggregation;
    }
     
    /**
     * Copies categories to the model from a json object (usually a server response)
     * 
     */
    service.loadCategoryValues = function(model, json, categoryType) {
      var otherEnabledFlag = true;
      var rangeCategoriesEnabledFlag = false;
      
      if(json != null && json != '') {
        var categories = JSON.parse(json);
        
        if($.isArray(categories)) {
          categories = {catLiElems:categories};
        }
        
        for(var i = 0; i < categories.catLiElems.length; i++) {
        	 var category = categories.catLiElems[i];
        	 
            // all categories should have the same flag values so a single occurance should describe all
            if(!category.otherEnabled){
          	  otherEnabledFlag = false;
            }
            
            // all categories should have the same flag values so a single occurance should describe all
            if(category.isRangeCat){
          	  rangeCategoriesEnabledFlag = true;
            }
        }
            
        for(var i = 0; i < categories.catLiElems.length; i++) {
          var category = categories.catLiElems[i];
          
          if(!category.isRangeCat && rangeCategoriesEnabledFlag){
        	  category.isRangeCat = true;
          }
              
          if(!category.otherCat) {
            if(i < model.catLiElems.length ) {            	
              model.catLiElems[i] = category;                          	
            }
            else {
              model.catLiElems.push(category);
            }
          }
          else {
            model.other = category;
          }
        }          
      }
      
      // update all the model categories to account for empty categories that are not updated from the back-end
      for(var i = 0; i < model.catLiElems.length; i++) {
          var modelCat = model.catLiElems[i];
          modelCat.isRangeCat = rangeCategoriesEnabledFlag;
      }
      
      // keep model updates outside of a loop to prevent angular from un-needed processing
      model.rangeCategoriesEnabled = rangeCategoriesEnabledFlag;
      model.otherEnabled = otherEnabledFlag;
    }
    
      
    /**
     * Fonts from Java come in as simple json array. We need to add id's to each entry for Angular.
     * 
     * @param fonts : Array of font names as strings
     */
    service.processFonts = function(fonts){
      var formattedFonts = [];
      for(var i=0; i<fonts.length; i++){
        formattedFonts.push(fonts[i].replace(/\+/g, " "));
      }
      return formattedFonts; 
    };
      
    service.edit = function(layerId, element, onSuccess, onFailure) {
      var success = function(response) {
        var model = service.createObjects(response, false);
              
        onSuccess(model);
      }
            
      var request = service.createStandbyRequest(element, success, onFailure);
            
      net.geoprism.dashboard.layer.DashboardThematicLayerController.edit(request, layerId);    
    }
      
    service.newInstance = function(mdAttributeId, mapId, element, onSuccess, onFailure) {
      var success = function(response) {
        var model = service.createObjects(response, true);
          
        onSuccess(model);
      }
        
      var request = service.createStandbyRequest(element, success, onFailure);
        
      net.geoprism.dashboard.layer.DashboardThematicLayerController.newThematicInstance(request, mdAttributeId, mapId);
    }
      
    service.isValidFont = function(font, options) {
      if(font != null && font.length > 0) {
        for(var i = 0; i < options.length; i++) {
          if(font === options[i]) {
            return true;
          } 
        }      
      }
      return false;
    }
    
    service.categoryAutoCompleteService = function(mdAttribute, geoNodeId, universalId, aggregationVal, text, limit, conditions, onSuccess, onFailure){
      var request = service.createRequest(onSuccess, onFailure);
      net.geoprism.dashboard.Dashboard.getCategoryInputSuggestions(request, mdAttribute, geoNodeId, universalId, aggregationVal, text, limit, conditions);
    }
    
    return service;
  }
  
  /*
   * 
   * 
   * REFERENCE LAYER SERVICE
   * 
   * 
   */  
  function ReferenceLayerFormService(runwayService) {
    var service = {};
      
    service.createRequest = function(onSuccess, onFailure){
      var request = new Mojo.ClientRequest({
        onSuccess : onSuccess,
        onFailure : function(e) {
          if(onFailure != null) {
            onFailure(e);              
          }
          else {
            GDB.ExceptionHandler.handleException(e);              
          }
        }
      });
          
      return request;
    }
      
      
    service.createStandbyRequest = function(elementId, onSuccess, onFailure){
      var el = $(elementId);
          
      if(el.length > 0) {        
        var request = new GDB.StandbyClientRequest({
          onSuccess : onSuccess,
          onFailure : function(e){
            if(onFailure != null) {
              onFailure(e);              
            }
            else {
              GDB.ExceptionHandler.handleException(e);              
            }
          }
        }, elementId);
            
        return request;        
      }
          
      return service.createRequest(onSuccess, onFailure);
    }
    
    service.apply = function(layer, style, state, element, onSuccess, onFailure) {
      // Populate the layer
      runwayService.populate(service.layerDTO, layer);      
      service.layerDTO.setValue('universal', layer.universalId);

      // Populate the style
      runwayService.populate(service.styleDTO, style);  
      
      var request = service.createStandbyRequest(element, onSuccess, onFailure);
      
      service.layerDTO.applyWithStyle(request, service.styleDTO, state.mapId, state);
    }      
    
    service.unlock = function(layer, element, onSuccess, onFailure) {
      var success = function() {
        service.layerDTO = null;
        service.styleDTO = null;
        
        onSuccess();      	  
      }

      if(service.layerDTO == null || service.layerDTO.isNewInstance()) {
    	success();
      }
      else {
        var request = service.createStandbyRequest(element, success, onFailure);

        service.layerDTO.unlock(request);
      }        
    }
    
    service.createObjects = function(response) {
      service.layerDTO = com.runwaysdk.DTOUtil.convertToType(response.layerDTO);
      service.styleDTO = com.runwaysdk.DTOUtil.convertToType(response.styleDTO);
        
      var layer = {};
      layer.newInstance = service.layerDTO.isNewInstance();
	  layer.nameLabel = response.layer.layerName;
      layer.universalId = response.layer.universalId;
      layer.layerType = response.layer.featureStrategy;
      layer.displayInLegend = response.layer.inLegend;

      // Set default values for the style
      var style = {};
      style.labelFont = 'Arial';
      style.enableLabel = true;
      style.labelSize = 12;
      style.labelColor = '#E0A4E0';
      style.labelHalo = '#000';
      style.labelHaloWidth = 2;
      style.pointFill = '#A9DEA4';
      style.pointOpacity = 0.75;
      style.pointStroke = '#000';
      style.pointStrokeWidth = 2;
      style.pointStrokeOpacity = 0.65;
      style.basicPointSize = 20;
      style.pointWellKnownName = 'CIRCLE';
      style.polygonFill = '#A9DEA4';
      style.polygonFillOpacity = 0.90;
      style.polygonStroke = '#000';
      style.polygonStrokeWidth = 5;
      style.polygonStrokeOpacity = 0.65;
        
      runwayService.populateObject(style, service.styleDTO);
      
      // pointWellKnownName values come from the server lower cased
      // but the UI model expects them to be upper cased values
      style.pointWellKnownName = style.pointWellKnownName.toUpperCase();

      // Ensure that the current font is actually available on the system
      // If not default to the first option
      if(!service.isValidFont(style.labelFont, response.options.availableFonts)) {
        style.labelFont = response.options.availableFonts[0];
      }
        
      return {layer:layer, style:style, dynamicDataModel:response.options};    
    }
    
    service.edit = function(layerId, element, onSuccess, onFailure) {
      var success = function(response) {
        var model = service.createObjects(response);
            
        onSuccess(model);
      }
          
      var request = service.createStandbyRequest(element, success, onFailure);
          
      net.geoprism.dashboard.layer.DashboardReferenceLayerController.edit(request, layerId);    	
    }
    
    service.newInstance = function(mapId, element, onSuccess, onFailure) {
      var success = function(response) {
    	var model = service.createObjects(response);
    	
    	// Ticket #237 - Reference layer should defaut to polygon
    	model.layer.layerType = 'BASICPOLYGON';
    	model.layer.universalId = model.dynamicDataModel.universals[0].value;
    	model.layer.name = model.dynamicDataModel.universals[0].label;
        
        onSuccess(model);
      }
      
      var request = service.createStandbyRequest(element, success, onFailure);
      
      net.geoprism.dashboard.layer.DashboardReferenceLayerController.newReferenceInstance(request, '', mapId);
    }
    
    service.isValidFont = function(font, options) {
      if(font != null && font.length > 0) {
        for(var i = 0; i < options.length; i++) {
          if(font === options[i]) {
            return true;
          } 
        }      
      }
      return false;
    }
      
    return service;
  }
	  
  
  
  angular.module("layer-form-service", ["runway-service"]);
  angular.module('layer-form-service')
    .factory('referenceLayerFormService', ReferenceLayerFormService)
    .factory('layerFormService', LayerFormService);
})();