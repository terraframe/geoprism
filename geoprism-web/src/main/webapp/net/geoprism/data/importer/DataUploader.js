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
(function(){
  function CategoryValidationProblemController($scope, datasetService, categoryService) {
    var controller = this;
    $scope.problem.synonym = null;
    
    controller.getClassifierSuggestions = function( request, response ) {
      var limit = 20;
      
      var connection = {
          onSuccess : function(resultSet){
            var results = [];
            
            $.each(resultSet, function( index, result ) {
              results.push({'label':result.label, 'value':result.label, 'id':result.value});
            });
            
            response( results );
          },
          onFailure : function(e){
//            console.log(e);
          }
      };
      
      var text = request.term;
      
      datasetService.getClassifierSuggestions(connection, $scope.problem.mdAttributeId, text, limit);
    }
    
    controller.setSynonym = function(value) {
      controller.problemForm.$setValidity("synonym-length",  ($scope.problem.synonym != null));      
    }
    
    controller.createSynonym = function() {
      var connection = {
        elementId : '#uploader-overlay',
        onSuccess : function(response){
          $scope.problem.resolved = true;
          $scope.problem.action = {
            name : 'SYNONYM',
            synonymId : response.synonymId,
            label : response.label
          };
            
//          $scope.$apply();
        },
        onFailure : function(e){
          $scope.errors = [];
          $scope.errors.push(e.localizedMessage);
            
//          $scope.$apply();
        }        
      };
      
      $scope.errors = undefined;
      
      datasetService.createClassifierSynonym(connection, $scope.problem.synonym, $scope.problem.label);
    }
    
    
    controller.createOption = function() {
      var connection = {
        elementId : '#uploader-overlay',
        onSuccess : function(response){
          $scope.problem.resolved = true;
          $scope.problem.action = {
            name : 'OPTION',
            optionId : response.id
          };
      
          $scope.$apply();
        },
        onFailure : function(e){
          $scope.errors = [];
          $scope.errors.push(e.localizedMessage);
      
          $scope.$apply();
        }      
      };
        
      $scope.errors = undefined;
      
      var option = {
        parentId : $scope.problem.categoryId,
        label : $scope.problem.label
      };
      
      categoryService.createOption(connection, JSON.stringify(option));
    }

    
    controller.ignoreValue = function() {
      $scope.problem.resolved = true;
      
      $scope.problem.action = {
        name : 'IGNORE'
      };
      
      var mdAttributeId = $scope.problem.mdAttributeId;
      
      var config = datasetService.getDatasetConfiguration();
      
      if(!config.categoryExclusion){
        config.categoryExclusion = {};
      }
      
      if(!config.categoryExclusion[mdAttributeId]) {
        config.categoryExclusion[mdAttributeId] = [];
      }
      
      config.categoryExclusion[mdAttributeId].push($scope.problem.label);
    }
    
    controller.removeExclusion = function() {
      
      var mdAttributeId = $scope.problem.mdAttributeId;
      var label = $scope.problem.label;
      
      var config = datasetService.getDatasetConfiguration();
      if(config.categoryExclusion && config.categoryExclusion[mdAttributeId]){          
        config.categoryExclusion[mdAttributeId] = $.grep(config.categoryExclusion[mdAttributeId], function(value) {
          return value != label;
        });        
      }
      
      if(config.categoryExclusion[mdAttributeId].length === 0) {
        delete config.categoryExclusion[mdAttributeId];
      }
    }    
    
    controller.undoAction = function() {
      var locationLabel = $scope.problem.label;
      var universal = $scope.problem.universalId;
      
      if($scope.problem.resolved) {
        
        var action = $scope.problem.action;
        
        if(action.name == 'IGNORE'){
          $scope.problem.resolved = false;
          
          controller.removeExclusion();
        }
        else if (action.name === 'SYNONYM'){
          var connection = {
            elementId : '#uploader-overlay',
            onSuccess : function(response){
              $scope.problem.resolved = false;
              $scope.problem.synonym = null;
                      
              controller.problemForm.$setValidity("synonym-length",  ($scope.problem.synonym != null));      
                      
              // $scope.$apply();
            },
            onFailure : function(e){
              $scope.errors = [];
              $scope.errors.push(e.localizedMessage);
                      
              // $scope.$apply();
            }      
          };
          
          datasetService.deleteClassifierSynonym(connection, action.synonymId);                    
        }
        else {
          var connection = {
            elementId : '#uploader-overlay',
            onSuccess : function(response){
              $scope.problem.resolved = false;
              $scope.problem.optionId = null;
                              
               $scope.$apply();
            },
            onFailure : function(e){
              $scope.errors = [];
              $scope.errors.push(e.localizedMessage);
                              
              $scope.$apply();
            }      
          };
                  
          categoryService.deleteOption(connection, action.optionId);
        }
      }
    }
  }
  
  function CategoryValidationProblem($timeout) {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/data-uploader/category-validation-problem.jsp',
      scope: {
        problem : '=',
        options : '='
      },
      controller : CategoryValidationProblemController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs, ctrl) {
        $timeout(function(){
          ctrl.problemForm.$setValidity("synonym-length",  false);        
        }, 0);
      }
    }   
  }
  
  
  function CategoryValidationPageController($scope) {
    var controller = this;
    
    controller.hasProblems = function() {
      for(var i = 0; i < $scope.problems.categories.length; i++) {
        if(!$scope.problems.categories[i].resolved) {
          return true;
        }
      }
      
      return false;
    }
    
    $scope.$watch('problems', function(){
      $scope.form.$setValidity("size",  (!controller.hasProblems()));
    }, true);

    // Remove the global validation on the form
    $scope.$on('pagePrev', function(event, data){
      $scope.form.$setValidity("size",  true);
    });       
  }
  
  function CategoryValidationPage() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/data-uploader/category-validation-page.jsp',
      scope: true,
      controller : CategoryValidationPageController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs) {
      }
    }   
  }  
  
  function GeoValidationProblemController($scope, datasetService, runwayService) {
    var controller = this;
    $scope.problem.synonym = null;
    
    controller.getGeoEntitySuggestions = function( request, response ) {
      var limit = 20;
      
      var connection = {
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
        }
      };
      
      var text = request.term;
        
      datasetService.getGeoEntitySuggestions(connection, $scope.problem.parentId, $scope.problem.universalId, text, limit);
    }
    
    controller.setSynonym = function(value) {
      $scope.problem.synonym = value;
      
      controller.problemForm.$setValidity("synonym-length",  ($scope.problem.synonym != null));      
    }
    
    controller.createSynonym = function() {
      var connection = {
        elementId : '#uploader-overlay',
        onSuccess : function(response){
          $scope.problem.resolved = true;
          $scope.problem.action = {
            name : 'SYNONYM',
            synonymId : response.synonymId,
            label : response.label,
            ancestors : response.ancestors            
          };
         
          // $scope.$apply();
        },
        onFailure : function(e){
         $scope.errors = [];
         $scope.errors.push(e.localizedMessage);
         
         // $scope.$apply();
        }        
      };
      
      $scope.errors = undefined;
      
      datasetService.createGeoEntitySynonym(connection, $scope.problem.synonym, $scope.problem.label);
    }
    
    controller.createEntity = function() {
      var connection = {
        elementId : '#uploader-overlay',
        onSuccess : function(response){
          $scope.problem.resolved = true;
          $scope.problem.action = {
            name : 'ENTITY',
            entityId : response.entityId
          };
      
          // $scope.$apply();
        },
        onFailure : function(e){
          $scope.errors = [];
          $scope.errors.push(e.localizedMessage);
      
          // $scope.$apply();
        }      
      };
        
      $scope.errors = undefined;
      
      datasetService.createGeoEntity(connection, $scope.problem.parentId, $scope.problem.universalId, $scope.problem.label);
    }
    
    controller.findLocObjIndex = function(locationExclusions, exclusionId){
      for(var i=0; i<locationExclusions.length; i++){
        var exclusion = locationExclusions[i];
        
        if(exclusion.id == exclusionId){
          return i;
        }
      }
      
      return -1;
    }

    controller.removeLocationExclusion = function(exclusionId) {
      var config = datasetService.getDatasetConfiguration();

      if(config.locationExclusions){         
         
        var index = controller.findLocObjIndex(config.locationExclusions, exclusionId);
         
        if (index > -1) {
          config.locationExclusions.splice(index, 1);
       }
      }
    }
    
    controller.ignoreDataAtLocation = function() {
      var locationLabel = $scope.problem.label;
      var universal = $scope.problem.universalId;
      var id = runwayService.generateId();
      
      $scope.problem.resolved = true;
      
      $scope.problem.action = {
        name : 'IGNOREATLOCATION',
        label : locationLabel,
        id : id
      };
      
      var exclusion = {id:id, "universal":universal, "locationLabel":locationLabel, "parentId":$scope.problem.parentId};
      
      var config = datasetService.getDatasetConfiguration();
      
      if(config.locationExclusions){
        config.locationExclusions.push(exclusion);
      }
      else{
        config.locationExclusions = [exclusion];
      }
    }    

    controller.undoAction = function() {
      var locationLabel = $scope.problem.label;
      var universal = $scope.problem.universalId;
      
      if($scope.problem.resolved) {
        
        var connection = {
          elementId : '#uploader-overlay',
          onSuccess : function(response){
            $scope.problem.resolved = false;
            $scope.problem.synonym = null;
            
            controller.problemForm.$setValidity("synonym-length",  ($scope.problem.synonym != null));      
            
            // $scope.$apply();
          },
          onFailure : function(e){
            $scope.errors = [];
            $scope.errors.push(e.localizedMessage);
                
            // $scope.$apply();
          }      
        };
      
        var action = $scope.problem.action;
        
        if(action.name == 'ENTITY')  {
          datasetService.deleteGeoEntity(connection, action.entityId);          
        }
        else if(action.name == 'IGNOREATLOCATION'){
          $scope.problem.resolved = false;
          controller.removeLocationExclusion(action.id);
        }
        else {
          datasetService.deleteGeoEntitySynonym(connection, action.synonymId);                    
        }
      }
    }
  }
  
  function GeoValidationProblem($timeout) {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/data-uploader/geo-validation-problem.jsp',
      scope: {
        problem : '=' 
      },
      controller : GeoValidationProblemController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs, ctrl) {
        $timeout(function(){
          ctrl.problemForm.$setValidity("synonym-length",  false);        
        }, 0);
      }
    }   
  }
  
  function GeoValidationPageController($scope, datasetService) {
    var controller = this;
    
    controller.hasProblems = function() {
      if($scope.problems.locations != null) {      
        for(var i = 0; i < $scope.problems.locations.length; i++) {
          if(!$scope.problems.locations[i].resolved) {
            return true;
          }
        }
      }
      
      return false;
    }
    
    $scope.$watch('problems', function(){
      $scope.form.$setValidity("size",  (!controller.hasProblems()));
    }, true);

    // Remove the global validation on the form
    $scope.$on('pagePrev', function(event, data){
      $scope.form.$setValidity("size",  true);
    });       
  }
  
  function GeoValidationPage() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/data-uploader/geo-validation-page.jsp',
      scope: true,
      controller : GeoValidationPageController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs) {
      }
    }   
  }
  
  function MatchPageController($scope, datasetService) {
    var controller = this;
    
    controller.select = function(match, replace) {
      var connection = {
        elementId : '#uploader-div',
        onSuccess : function(result) {
          var sheet = result.datasets;
          sheet.replaceExisting = replace;
      
          $scope.$emit('loadConfiguration', {sheet: sheet});
      
          // $scope.$apply();
        }          
      };
      
      datasetService.getSavedConfiguration(connection, match.id, $scope.sheet.name);      
    }
  }
  
  function MatchPage() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/data-uploader/match-page.jsp',
      scope: true,
      controller : MatchPageController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs) {
      }
    }   
  }
  
  
  function MatchInitialPageController($scope, datasetService) {
    var controller = this;
    /**
     * @param targetPage <optional> 
     * @param leavingPage <optional> 
     */
    controller.next = function(targetPage, leavingPage) {
      // emiting to UploaderDialogController
      $scope.$emit('nextPage', {targetPage:targetPage, leavingPage:leavingPage});
    }
  }
  
  function MatchInitialPage() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/data-uploader/match-initial-page.jsp',
      scope: true,
      controller : MatchInitialPageController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs) {
      }
    }   
  }
  
  
  function BeginningInfoPageController($scope, datasetService) {
    var controller = this;
    
    controller.length = function() {
      var config = datasetService.getDatasetConfiguration();
      return config.sheets.length;
    }
  }
 
  
  function BeginningInfoPage() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/data-uploader/beginning-info-page.jsp',
      scope: true,
      controller : BeginningInfoPageController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs) {
      }
    }   
  }
  
  function NamePageController($scope, datasetService) {
    var controller = this;
    
    controller.isUniqueLabel = function(label, ngModel, scope) {
      var connection = {
        onSuccess : function() {
          ngModel.$setValidity('unique', true);       
          // $scope.$apply();
        },
        onFailure : function(e){
          ngModel.$setValidity('unique', false);          
          // $scope.$apply();
        }
      };
      
      if(label != null && label != '') {
        datasetService.validateDatasetName(connection, label, '');
      }
    }
  }
  
  function NamePage() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/data-uploader/name-page.jsp',
      scope: true,
      controller : NamePageController,
      controllerAs : 'ctrl',        
      link: function (scope, element, attrs) {
      }
    }   
  } 
  
  function AttributesPageController($scope, datasetService) {
    var controller = this;
    
    controller.initialize = function() {
      // Initialize the universal options
      if($scope.options != null) {
        var countries = $scope.options.countries;
              
        for(var i = 0; i < countries.length; i++) {
          var country = countries[i];
                 
          if(country.value == $scope.sheet.country) {
            $scope.universals = country.options;
          }
        }
      }
      
      $scope.hasLocation = false;
      
      $scope.longitudeFields = {};
      $scope.latitudeFields = {};      
      $scope.textFields = {};
      
      for(var i = 0; i < $scope.sheet.fields.length; i++) {
    	var sheetField = $scope.sheet.fields[i];
        controller.accept(sheetField);
      }
    }
    
    controller.initializeField = function(field) {
      if(field.categoryLabel == null) {
        field.categoryLabel = field.label;
      }
    }
  
    controller.isUniqueLabel = function(label) {
      if($scope.sheet != null) {
        var count = 0;
          
        for(var i = 0; i < $scope.sheet.fields.length; i++) {
          var field = $scope.sheet.fields[i];
            
          if(field.label == label) {
            count++;
          }            
        }
          
        if(count > 1) {
          return false;
        }
      }  
        
      return true;
    }
        
    controller.isUniqueCategory = function(label, ngModel, scope) {
      
      // First: Ensure the category label is unique with respect to the local categories
      var valid = true;
      
      if($scope.sheet != null) {
        var count = 0;
            
        for(var i = 0; i < $scope.sheet.fields.length; i++) {
          var field = $scope.sheet.fields[i];
              
          if(field.type == 'CATEGORY' && field.root == null && field.categoryLabel == label) {
            count++;
          }            
        }
            
        if(count > 1) {
          valid = false;
        }
      }      
      
      // Second: Ensure the category label is unique with respect to the remote categories
      if(valid) {
        var connection = {
          onSuccess : function() {
            ngModel.$setValidity('unique', true);       
            // $scope.$apply();
          },
          onFailure : function(e){
            ngModel.$setValidity('unique', false);          
            // $scope.$apply();
          }
        };
        
        if(label != null && label != '') {      
          datasetService.validateCategoryName(connection, label, '');
        }        
      }
      else {
        ngModel.$setValidity('unique', false);          
        scope.$apply();      
      }
      
    }
    
    controller.accept = function(field) {
      field.accepted = true;
      
      if(field.type === "IGNORE"){
    	  
      }
      
      if(field.type === "LOCATION" || field.type === 'COORDINATE'){
        controller.setLocationSelected(field.type);
      }
      else{
        controller.setLocationSelected([]);
      }
      
      if(field.type === "LATITUDE") {
        $scope.latitudeFields[field.name] = field;
      }
      else {
        delete $scope.latitudeFields[field.name];      
      }
      
      if(field.type === "LONGITUDE") {
        $scope.longitudeFields[field.name] = field;      
      }
      else {
        delete $scope.longitudeFields[field.name];
      }
      
      if(field.type === "TEXT") {
        $scope.textFields[field.name] = field;      
      }
      else {
        delete $scope.textFields[field.name];
      }
      
      if(field.type === "CATEGORY") {
    	  controller.setCategorySelected(field.type);
      }
      
      var matched = (Object.keys($scope.latitudeFields).length == Object.keys($scope.longitudeFields).length);
      $scope.form.$setValidity("coordinate", matched);
      
      if(Object.keys($scope.latitudeFields).length > 0 || Object.keys($scope.longitudeFields).length > 0) {
        $scope.form.$setValidity("coordinateText", (Object.keys($scope.textFields).length > 0));        
      }
      else {
        $scope.form.$setValidity("coordinateText", true);        
      }
    }
    
    controller.setLocationSelected = function(locationType) {
      var locationTypeArr = [];
      
      if(locationType && locationType.length > 0){
        locationTypeArr.push(locationType);
        
        for(var i=0; i<$scope.sheet.fields.length; i++){
          var field = $scope.sheet.fields[i];
          
          if(((field.type === "LOCATION" || field.type === "LONGITUDE" || field.type === "LATITUDE") && field.type !== locationType) && locationTypeArr.indexOf(field.type) === -1 ){
            locationTypeArr.push(field.type);
          }
        }
      }
      else{
        for(var i=0; i<$scope.sheet.fields.length; i++){
          var field = $scope.sheet.fields[i];
          
          if(field.type === "LOCATION" && locationTypeArr.indexOf(field.type) === -1){
            locationTypeArr.push(field.type);
          }
          else if(field.type === "LONGITUDE" || field.type === "LATITUDE"){
	          for(var c=0; c<$scope.sheet.fields.length; c++){
	    		  var field2 = $scope.sheet.fields[c];
	    		  if(field2.type !== field.type && field2.type === "LATITUDE" || field2.type === "LONGITUDE"){
	    			  if(locationTypeArr.indexOf(field.type) === -1){
	    				  locationTypeArr.push(field.type);
		    		  }
	    		  }
	    	  }
          }
          
        }
      }
      
      $scope.$emit('hasSpecialTypeEvent', {type:"LOCATION", typeConstant:locationTypeArr});
    }
    
    controller.setCategorySelected = function(category) {
    	$scope.$emit('hasSpecialTypeEvent', {type:"CATEGORY", typeConstant:category});
    }
    
    controller.initialize();
    
  }
  
  function AttributesPage() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/data-uploader/attributes-page.jsp',
      scope: true,
      controller : AttributesPageController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs) {
      }
    }   
  }
  
  function LocationPageController($scope, runwayService) {
    var controller = this;
    
    controller.initialize = function() {
      // Initialize the universal options
      var countries = $scope.options.countries;
            
      for(var i = 0; i < countries.length; i++) {
        var country = countries[i];
               
        if(country.value == $scope.sheet.country) {
          $scope.universals = country.options;
        }
      }
        
      // Create a map of possible location fields
      $scope.locationFields = {};
        
      for(var j = 0; j < $scope.sheet.fields.length; j++) {
        var field = $scope.sheet.fields[j];
            
        if(field.type == 'LOCATION') {
          if($scope.locationFields[field.universal] == null) {
            $scope.locationFields[field.universal] = [];
          }
            
          $scope.locationFields[field.universal].push(field);          
        }
      }
      
      controller.setLocationFieldAutoAssignment();
    }
    
    
    /**
     * Gets the lowest unassigned location fields in across the entire universal hierarchy.
     */
    controller.getLowestUnassignedLocationFields = function() {
      var unassignedLowestFields = [];
      
      for(var i = ($scope.universals.length - 1); i >= 0; i--) {
        var universal = $scope.universals[i];
        
        if($scope.locationFields[universal.value] != null) {
          var fields = $scope.locationFields[universal.value];
          
          for(var j = 0; j < fields.length; j++) {
            var field = fields[j];
            
            if(!field.assigned) {              
              unassignedLowestFields.push({field:field, universal:universal});
            }
          }
          
          if(unassignedLowestFields.length > 0){
            return unassignedLowestFields;
          }
        }        
      }  
      
      return unassignedLowestFields;
    }
    
    
    /**
     * Gets all unassigned location fields at a given universal level
     * 
     * @universalId - The universal level at which to search for unassigned fields
     */
    controller.getUnassignedLocationFields = function(universalId) {
      if($scope.locationFields.hasOwnProperty(universalId)){
        var fields = $scope.locationFields[universalId];
        var unassignedFields = [];
    
        for(var j = 0; j < fields.length; j++) {
          var field = fields[j];
          
          if(!field.assigned) {              
            unassignedFields.push(field);
          }
        }
            
        return unassignedFields;
      }
      
      return [];
    } 
    
    
    /**
     * Gets the next location moving from low to high in the universal hierarchy. 
     * Valid returns include another field at the same universal level or a higher universal level.
     * 
     */
    controller.getNextLocationField = function() {
        for(var i = ($scope.universals.length - 1); i >= 0; i--) {
            var universal = $scope.universals[i];
            
            if($scope.locationFields[universal.value] != null) {
              var fields = $scope.locationFields[universal.value];
              
              for(var j = 0; j < fields.length; j++) {
                var field = fields[j];
                
                if(!field.assigned) {              
                  return {field:field, universal:universal};
                }
              }
            }        
          }  
          
          return null;
    } 
    
    
    controller.edit = function(attribute) {
      // This should only be hit if trying to edit when an existing edit session is in place. 
      if($scope.attribute && $scope.sheet.attributes.values[$scope.attribute.id]){
    	  if($scope.sheet.attributes.values[$scope.attribute.id].editing){
    		  $scope.sheet.attributes.values[$scope.attribute.id].editing = false;
    	  }
    	  
    	  // all fields that are in the current attribute (ui widget) should be set back to assigned
    	  // which creates a save type behavior for the current state of the attribute.
    	  controller.setFieldAssigned();
      }
      
      $scope.attribute = angular.copy(attribute);
      
      $scope.sheet.attributes.values[$scope.attribute.id].editing = true;
      
      controller.unassignLocationFields();
      
      var locFieldsSelectedButNotYetAssigned = controller.getLocationFieldsSelectedInWidget();
      controller.refreshUnassignedFields(locFieldsSelectedButNotYetAssigned);
            
      var fieldLabel = $scope.attribute.fields[attribute.universal];      
      var field = controller.getField(fieldLabel);

      // EXCLUDEed fields should be skipped
      if(field){
    	  controller.setUniversalOptions(field);  
      }
    }
    
    
    /**
     * Sets all $scope.locationFields to false if the location field is part of $scope.attribute.fields.
     */
    controller.unassignLocationFields = function(){
    	for (var key in $scope.attribute.fields) {
    		if ($scope.attribute.fields.hasOwnProperty(key)) {
    			var attributeFieldLabel = $scope.attribute.fields[key];
    			var locFields = $scope.locationFields[key];
    			for(var i=0; i<locFields.length; i++){
          	    	var locField = locFields[i];
          	    	if(locField.label === attributeFieldLabel){
          	    		locField.assigned = false;
          	    	}
    			}
    		}
    	}
    }
    
    
    controller.getLocationFieldsSelectedInWidget = function() {
        var unassignedFields = [];
        for (var key in $scope.attribute.fields) {
      	  if ($scope.attribute.fields.hasOwnProperty(key)) {
      		var attributeFieldLabel = $scope.attribute.fields[key];
      	    var locFields = $scope.locationFields[key];
      	    
      	    for(var i=0; i<locFields.length; i++){
      	    	var locField = locFields[i];
      	    	// making sure to only add fields that are part of $scope.attribute
      	    	if(locField.label === attributeFieldLabel && !locField.assigned){
      	    		unassignedFields.push(locField.label);
      	    	}
      	    }
      	  }
        }
        
        return unassignedFields;
    }
    
    
    /**
     * Gets a field from sheet.fields by label
     * 
     * @label = label of a field 
     */
    controller.getField = function(label) {
      for(var j = 0; j < $scope.sheet.fields.length; j++) {
        var field = $scope.sheet.fields[j];
                
        if(field.label === label) {
          return field;
        }      
      }
      
      return null;
    }
    
    
    /**
     * Populate the unassigned field array from sheet.fields
     * 
     * @selectedFields - fields that are to be excluded from the unassignedFields array. Typically this is because they are set in the 
     *            location field attribute by the user in the UI and that location card has not yet been set. Keeping these fields out
     *            of the unassignedFields array ensures the fields aren't displayed as unassigned in the UI.
     */
    controller.refreshUnassignedFields = function(selectedFields) {
      $scope.unassignedFields = [];
      
      if($scope.attribute != null) {
        for(var i = 0; i < $scope.sheet.fields.length; i++) {     
          var field = $scope.sheet.fields[i];
              
          if(field.type == 'LOCATION' && !field.assigned && field.name != $scope.attribute.label && selectedFields.indexOf(field.name) === -1 ) {
            $scope.unassignedFields.push(field);
          }
        }                          
      }
    }
    
    /**
     * Remove attribute from the sheet 
     * 
     * @attribute - attribute to remove
     */
    controller.remove = function(attribute) {
      if($scope.sheet.attributes.values[attribute.id]) {
              
        delete $scope.sheet.attributes.values[attribute.id];        
        $scope.sheet.attributes.ids.splice( $.inArray(attribute.id, $scope.sheet.attributes.ids), 1 );
        
        controller.setFieldAssigned();
        
        if(!$scope.attribute) {
          controller.newAttribute();
        } 
        else {
          controller.refreshUnassignedFields([]);
        }
      }
    }
    
    
    /**
     * Create a new attribute from a location field in the source data
     * 
     * @attribute - location field that is being constructed. 
     *         It corresponds to a configurable location card on the UI where the user can set the location.
     */
    controller.newAttribute = function() {
      
	    if($scope.attribute) {      
	        if($scope.attribute.id === -1) {
	          $scope.attribute.id = runwayService.generateId();
	          $scope.sheet.attributes.ids.push($scope.attribute.id);
	          $scope.sheet.attributes.values[$scope.attribute.id] = {};              
	        }     
	        
	        //controller.handleExcludedFields();
	        
	        var attributeInSheet = $scope.sheet.attributes.values[$scope.attribute.id];    
	        attributeInSheet.editing = false;
	        
	        // copy the properties from the attribute (cofigurable widget in the ui) to sheet.attributes
	        // $scope.attribute is coppied to --> attributeInSheet
	        angular.copy($scope.attribute, attributeInSheet);            
	        
	        // Set the assigned prop in sheet.fields if an attribute stored in sheet.attributes.values
	        // also references the field.  This means that a saved attribute has claimed that field.
	        controller.setFieldAssigned();
	    }
        
        var nextLocation = controller.getNextLocationField();      
        
        // This typically passes when a user manually sets an attribute and 
        // there are more location fields yet to be set
        if(nextLocation) {
          var field = nextLocation.field;
          var universal = nextLocation.universal;
          
          $scope.attribute = {
            label : field.label,
            universal : universal.value,
            fields : {},
            id : -1
          };

          controller.addField(field);
          controller.setUniversalOptions(field);
          controller.refreshUnassignedFields([]);
        }
        else {
          controller.refreshUnassignedFields([]);
          $scope.attribute = null;
        }
    }
    
    //TODO: remove this if not needed
//    controller.handleExcludedFields = function() {
//    	var attributeFieldsToDelete = [];
//    	for (var key in $scope.attribute.fields) {
//    		if ($scope.attribute.fields.hasOwnProperty(key)) {
//    			var attributeFieldLabel = $scope.attribute.fields[key];
//    			if(attributeFieldLabel === "EXCLUDE"){
//    				attributeFieldsToDelete.push(key);
//    			}
//    		}
//    	}
//    	
//    	for(var i=0; i<attributeFieldsToDelete.length; i++){
//    		var field = attributeFieldsToDelete[i];
//    		
//    		if(field){
//    			delete $scope.attribute.fields[field];
//    		}
//    	}
//    }
    
    
    /**
     * Try to auto build the location field if there is only one field option per universal
     * 
     * Rules:
     *  1. Attempt to auto-assign context fields if there is ONLY one lowest level universal
     *  2. Auto-assign a single context field only if there is a single option
     *  
     */
    controller.setLocationFieldAutoAssignment = function() {
      
      var lowestLevelUnassignedLocationFields = controller.getLowestUnassignedLocationFields();  
      var lowestLevelUnassignedUniversal;
      if(lowestLevelUnassignedLocationFields.length > 0){
        lowestLevelUnassignedUniversal = lowestLevelUnassignedLocationFields[0].universal.value;
      }
            
        if(lowestLevelUnassignedLocationFields.length === 1){
            for(var j = 0; j < lowestLevelUnassignedLocationFields.length; j++) {
                var field = lowestLevelUnassignedLocationFields[j].field;
                
              // construct the initial model for a location field
              $scope.attribute = {
                label : field.label,
                name : field.name,
                universal : field.universal,
                fields : {},
                id : -1
              };
      
              // add the targetLocationField.field (remember, it's from the source data) 
              // to the new location field (i.e. attribute)
              controller.addField(field);
              
              // sets all valid universal options (excluding the current universal) for this location field
              controller.setUniversalOptions(field); 
              
              // There is only one or no universal options (i.e. context locations) so just set the field
              // to save a click for the user
              if($scope.universalOptions.length < 1){
                // calling newAttribute() is safe because there are no other location fields so the 
                // location attribute will just be set to null.
                controller.newAttribute(); 
              }
              else{
                // Attempting auto-assignment of context fields
                controller.constructContextFieldsForAttribute(field);
              }
            }
        }
        // There are more than one lowest level un-assigned fields so lets not assume we can guess context fields.
        else if(lowestLevelUnassignedLocationFields.length > 1){
          for(var j = 0; j < lowestLevelUnassignedLocationFields.length; j++) {
                var field = lowestLevelUnassignedLocationFields[j].field;
                
              // construct the initial model for a location field
              $scope.attribute = {
                label : field.label,
                name : field.name,
                universal : field.universal,
                fields : {},
                id : -1
              };
              
              // add the targetLocationField.field (remember, it's from the source data) 
              // to the new location field (i.e. attribute)
              controller.addField(field);
            
              // sets all valid universal options (excluding the current universal) for this location field
              controller.setUniversalOptions(field);
              
              controller.refreshUnassignedFields([field.name]);
              
              break;
          }
        }
    }
    
    
    /**
     * Construct all possible context fields for a given target field.
     * NOTE: there is an assumption that this will only be called for a lowest level univeral field
     * 
     * @field - the target field to which context fields would be built from. This is typically a field
     *       in the lowest level universal of a sheet.
     */
    controller.constructContextFieldsForAttribute = function(field) {
      var unassignedLocationFieldsForTargetFieldUniversal = controller.getUnassignedLocationFields(field.universal); 
      
        if($scope.universalOptions.length > 0 && unassignedLocationFieldsForTargetFieldUniversal.length === 1){
          var fieldsSetToUniversalOptions = [];
          
          for(var i=$scope.universalOptions.length; i--;){
            var universalOption = $scope.universalOptions[i];
              var unassignedLocationFieldsForThisUniversal = controller.getUnassignedLocationFields(universalOption.value); 
              // Set the field ONLY if there is a single option per universal
              if(unassignedLocationFieldsForThisUniversal.length === 1){
                controller.addField(unassignedLocationFieldsForThisUniversal[0]);
                fieldsSetToUniversalOptions.push(unassignedLocationFieldsForThisUniversal[0].name);
              }
          }
          
          // set the location attribute only if all the universal options have been set automatically
          // i.e. The # of universal options must match the # of fields set
          if(fieldsSetToUniversalOptions.length === $scope.universalOptions.length){
            controller.newAttribute();
          }
          
          controller.refreshUnassignedFields(fieldsSetToUniversalOptions);
        }
    }
    
    controller.addField = function(field) {
      $scope.attribute.fields[field.universal] = field.label;
    }
    
    /**
     * Sets the valid universal options for a given field.
     * 
     * Valid options are:
     *   1.  universals that are assigned by the user in the source data 
     *   2.  universals with fields that are not yet assigned
     */
    controller.setUniversalOptions = function(field) {
      $scope.universalOptions = [];
      var valid = true;
      
      for(var i = 0; i < $scope.universals.length; i++) {
        var universal = $scope.universals[i];
        var unassignedFieldsForThisUniversal = controller.getUnassignedLocationFields(universal.value);
              
        if(universal.value == field.universal) {
          valid = false;              
        }
        else if(valid && unassignedFieldsForThisUniversal.length > 0) {
          $scope.universalOptions.push(universal);
        }
      }
    }
    
    
    /**
     * Sets all sheet.fields in a sheet to isAssigned = true if the field is assigned to a sheet attribute
     * and is a location field.
     * 
     */
    controller.setFieldAssigned = function() {
      for(var i = 0; i < $scope.sheet.fields.length; i++) {     
        var field = $scope.sheet.fields[i]
      
        if(field.type == 'LOCATION') {
          field.assigned = controller.isAssigned(field);          
        }
        else {
          field.assigned = false;                
        }
      }          
    }
    
    
    /**
     * Check if a field is assigned to a sheet attribute 
     * 
     * @field - the field to check
     * 
     * isAssigned = true if the sheet attribute fields has a field with a corresponding field label
     */
    controller.isAssigned = function(field) {
      for(var i = 0; i < $scope.sheet.attributes.ids.length; i++) {
        var id = $scope.sheet.attributes.ids[i];
        var attribute = $scope.sheet.attributes.values[id];
                  
        for (var key in attribute.fields) {
          if (attribute.fields.hasOwnProperty(key)) {
            if(attribute.fields[key] == field.label) {
              return true;
            }
          }
        }
      }
      
      return false;
    }
    
    controller.isUniqueLabel = function(label) {
      if($scope.sheet != null) {
        var count = 0;
             
        for(var i = 0; i < $scope.sheet.fields.length; i++) {
          var field = $scope.sheet.fields[i];
                
          if(field.type != 'LOCATION' && field.label == label) {
            count++;
          }            
        }
          
        for(var i = 0; i < $scope.sheet.attributes.ids.length; i++) {
          var id = $scope.sheet.attributes.ids[i];
          var sheetAttribute = $scope.sheet.attributes.values[id];          
            
          if(sheetAttribute.label === label && !sheetAttribute.editing) {
            count++;
          }   
          if($scope.attribute && $scope.attribute.label === sheetAttribute.label && !sheetAttribute.editing){
        	  count++;
          }
        }
              
        if(count > 0) {
          return false;
        }
      }  
            
      return true;
    }
      
    $scope.$watch('attribute', function(){
      $scope.form.$setValidity("size",  ($scope.attribute == null));
    }, true);   
    

    // Remove the global validation on the form
    $scope.$on('pagePrev', function(event, data){
      $scope.form.$setValidity("size",  true);
    });   
    
    controller.change = function(selectedFields){
      var selectedFieldsArr = [];
      for (var key in selectedFields) {
          if (selectedFields.hasOwnProperty(key) && selectedFields[key] !== "EXCLUDE") {
            selectedFieldsArr.push(selectedFields[key]);
          }
        }
      controller.refreshUnassignedFields(selectedFieldsArr);
    }
    
    // Initialize the scope
    controller.initialize();
  }
  
  function LocationPage() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/data-uploader/location-page.jsp',
      scope: true,
      controller : LocationPageController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs) {
      }
    }   
  }
  
  function CoordinatePageController($scope, localizationService) {
    var controller = this;
    
    controller.initialize = function() {        
      $scope.longitudes = [];
      $scope.featureLabels = [];
      $scope.locations = [];
      $scope.featureIds = [];
      
      var countries = $scope.options.countries;
      
      for(var i = 0; i < countries.length; i++) {
        var country = countries[i];
               
        if(country.value == $scope.sheet.country) {
          $scope.universals = country.options;
          $scope.labels = {};
          
          for(var j = 0; j < country.options.length; j++) {
            var universal = country.options[j];
            
            $scope.labels[universal.value] = universal.label;
          }
        }
      }      
      
      for(var i = 0; i < $scope.sheet.fields.length; i++) {
        var field = $scope.sheet.fields[i];
        
        if(field.type == 'LATITUDE') {
          
          if(!controller.hasCoordinateField(field)) {
            var coordinate = {
              label : "",
              latitude : field.label,
              longitude : controller.getSuggestedLongitude(field),
              featureLabel : "",
              location : "",
              featureId : "",
              id : field.label
            };
            
            $scope.sheet.coordinates.ids.push(coordinate.id);
            $scope.sheet.coordinates.values[coordinate.id] = coordinate;                          
          }
        }
        else if(field.type == 'LONGITUDE') {
          $scope.longitudes.push(field);          
        }
        else if(field.type == 'TEXT') {
          $scope.featureLabels.push(field);          
        }
        else if(controller.isBasic(field)) {
          $scope.featureIds.push(field);          
        }
      }
      
      /*
       * If there is only 1 longitude field then set that value
       * automatically and don't give the user a drop-down that
       * they need to select from
       */
      if($scope.longitudes.length == 1) {
        for(var i = 0; i < $scope.sheet.coordinates.ids.length; i++) {
          var id = $scope.sheet.coordinates.ids[i];
          var coordinate = $scope.sheet.coordinates.values[id];          
          
          coordinate.longitude = $scope.longitudes[0].label;
        }
      }
      
      if($scope.sheet.attributes != null) {
        for(var i = 0; i < $scope.sheet.attributes.ids.length; i++) {
          var id = $scope.sheet.attributes.ids[i];
          var attribute = $scope.sheet.attributes.values[id];          
        
          $scope.locations.push({
            label : attribute.label,
            universal : attribute.universal
          });
        }
      }
    }
    
    controller.getSuggestedLongitude = function(targetField) {
       var fields = $scope.sheet.fields;
       var trackingPosition = null;
       var mostLikelyLongitudeField = null;
       
    for(var i=0; i<fields.length; i++){
      var field = fields[i];
      if(field.type === "LATITUDE" && field.name === targetField.label){
        trackingPosition = field.fieldPosition;
      }
      else if(field.type === "LONGITUDE"){
        // if fields are located next to each other in the source data (spreadsheet)
        if(field.fieldPosition === trackingPosition + 1 || field.fieldPosition === trackingPosition - 1){
          return field.name;
        }
        else if(targetField.label.toLowerCase().replace(localizationService.localize("dataUploader", "attributeLatAbbreviation").toLowerCase(), localizationService.localize("dataUploader", "attributeLongAbbreviation").toLowerCase()) === field.label.toLowerCase() ||
            targetField.label.toLowerCase().replace(localizationService.localize("dataUploader", "attributeLatitudeName").toLowerCase(), localizationService.localize("dataUploader", "attributeLongitudeName").toLowerCase()) === field.label.toLowerCase() ){
          return field.name;
        }
      }
    }
    
    return false;
    }
    
    controller.hasCoordinateField = function(field) {
      for(var i = 0; i < $scope.sheet.coordinates.ids.length; i++) {
        var id = $scope.sheet.coordinates.ids[i];
        
        if(id === field.label) {
          return true;
        }
      }
      
      return false;
    }
    
    controller.isBasic = function(field) {
      return (field.type == 'TEXT' || field.type == 'LONG' || field.type == 'DOUBLE');  
    }
        
    controller.isUniqueLabel = function(label) {
      if($scope.sheet != null) {
        var count = 0;
        
        for(var i = 0; i < $scope.sheet.fields.length; i++) {
          var field = $scope.sheet.fields[i];
          
          if(field.label == label) {
            count++;
          }            
        }
        
        if($scope.sheet.attributes != null) {
          for(var i = 0; i < $scope.sheet.attributes.ids.length; i++) {
            var id = $scope.sheet.attributes.ids[i];
            var attribute = $scope.sheet.attributes.values[id];          
            
            if(attribute.label == label) {
              count++;
            }            
          }          
        }
        
        for(var i = 0; i < $scope.sheet.coordinates.ids.length; i++) {
          var id = $scope.sheet.coordinates.ids[i];
          var coordinate = $scope.sheet.coordinates.values[id];          
          
          if(coordinate.label == label) {
            count++;
          }            
        }
        
        if(count > 1) {
          return false;
        }
      }  
      
      return true;
    }
    
    // Initialize the scope
    controller.initialize();
  }
  
  function CoordinatePage() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/data-uploader/coordinate-page.jsp',
      scope: true,
      controller : CoordinatePageController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs) {
      }
    }   
  }
  
  function SummaryPageController($scope) {
    var controller = this;
    
    controller.initialize = function() {
      // Initialize the universal options
      if($scope.options != null) {
        var countries = $scope.options.countries;
          
        for(var i = 0; i < countries.length; i++) {
          var country = countries[i];
                   
          if(country.value == $scope.sheet.country) {
            $scope.universals = country.options;
            $scope.labels = {};
              
            for(var j = 0; j < country.options.length; j++) {
              var universal = country.options[j];
                
              $scope.labels[universal.value] = universal.label;
            }
          }
        }      
      }
    }
    
    controller.hasFieldType = function(type) {
      var fields = $scope.sheet.fields;
      for(var i=0; i<fields.length; i++){
        var field = fields[i];
        if(field.type.toLowerCase() === type.toLowerCase()){
          return true;
        }
      }
      
      return false;
    }
    
    controller.isValid = function(field) {
      return !(field.type == 'LOCATION' || field.type == 'LONGITUDE' || field.type == 'LATITUDE' || field.type == 'IGNORE'  || field.type == ''); 
    }
    
    controller.initialize();
  }
  
  function SummaryPage() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/data-uploader/summary-page.jsp',
      scope: true,
      controller : SummaryPageController,
      controllerAs : 'ctrl',            
      link: function (scope, element, attrs) {
      }
    }   
  }  

  function UploaderDialogController($scope, $rootScope, datasetService, localizationService, widgetService) {
    var controller = this;
    
    // Flag indicating if the modal and all of its elements should be destroyed
    $scope.show = false;    
    
    controller.clear = function() {
      // Flag indicating if the modal and all of its elements should be destroyed
      $scope.show = false;
        
      // Flag indicating if the modal is waiting from a response from the server
      $scope.busy = false;   
      
      // List of errors
      $scope.errors = undefined;
      
      // Stack of state snapshots captured when the user clicks next
      $scope.page = undefined;
      
      $scope.options = undefined;      
      $scope.sheet = undefined;      
      $scope.universals = undefined;
      
      $scope.currentStep = 0;
      $scope.locationType = []; 
    }

    controller.setCountry = function(country) {
      $scope.sheet.country = country;
    }
    
    controller.persist = function() {
      var connection = {
        elementId : '#uploader-overlay',
        onSuccess : function(result) {
          if(result.success) {          
            controller.clear();
          
            $scope.$emit('datasetChange', {datasets:result.datasets, finished : true});          
          }
          else {          
            
            if(controller.hasLocationField() && controller.hasCoordinateField()) {
              $scope.currentStep = 5;
            }
            else if(controller.hasLocationField() || controller.hasCoordinateField()) {
              $scope.currentStep = 4;
            }
            else{
              $scope.currentStep = 3;
            }

            
            if( !result.problems.locations || result.problems.locations.length > 0) {
              $scope.page.current = 'GEO-VALIDATION';
            }
            else {
              $scope.page.current = 'CATEGORY-VALIDATION';
            }
            
            $scope.page.snapshots = [];            
            $scope.sheets = result.sheets;
            $scope.sheet = $scope.sheets[0];
            $scope.problems = result.problems;
          
            $scope.$emit('datasetChange', {datasets:result.datasets, finished : false});
          }
         
          // $scope.$apply();
        },
        onFailure : function(e){
          $scope.errors.push(e.localizedMessage);
         
          // $scope.$apply();
        }
      };
      // Reset the file Errors
      
      var config = datasetService.getDatasetConfiguration();
      config.sheets[0] = $scope.sheet;
      
      $scope.errors = [];
      
      datasetService.importData(connection, config);      
    }
    
    controller.cancel = function() {
      var connection = {
        elementId : '#uploader-overlay',
        onSuccess : function(result) {
          controller.clear();
         
          $scope.$emit('datasetChange', {finished : true});                
         
          // $scope.$apply();
        },
        onFailure : function(e){
          $scope.errors.push(e.localizedMessage);
         
          // $scope.$apply();
        }             
      };
      // Reset the file Errors
      $scope.errors = [];
          
      datasetService.cancelImport(connection, datasetService.getDatasetConfiguration());
    }
    
    controller.load = function(information, options, classifiers) {
      $scope.options = options;
      $scope.classifiers = classifiers;
      
      datasetService.setDatasetConfiguration(information);
      var config = datasetService.getDatasetConfiguration();
      
      $scope.sheet = config.sheets[0];
      $scope.sheet.attributes = {ids:[], values : {}};    
      $scope.sheet.coordinates = {ids:[], values : {}};    
      $scope.errors = [];      
      $scope.show = true;
      $scope.locationType = []; 
      $scope.updateExistingDataset = false;
      
      $scope.userSteps = datasetService.getUploaderSteps([]);
      
      $scope.page = {
        snapshots : [],     
        current : 'BEGINNING-INFO',
      };
      
      if($scope.sheet.matches.length > 0) {
        $scope.page.current = 'MATCH-INITIAL';
      }
      
      // $scope.$apply();
    }
    
    controller.setAttributeDefaults = function() {
      var types = [];
      var fields = $scope.sheet.fields;
      for(var i=0; i<fields.length; i++){
        var field = fields[i];
        
        if(field.columnType === "NUMBER"){
          if(field.label.toLowerCase() === localizationService.localize("dataUploader", "attributeLatAbbreviation").toLowerCase() || field.label.toLowerCase().includes(localizationService.localize("dataUploader", "attributeLatitudeName").toLowerCase()) ){
            field.type = 'LATITUDE' 
              
            if(types.indexOf("COORDINATE") === -1){
              types.push("COORDINATE");
            }
          }
          else if(field.label.toLowerCase() === localizationService.localize("dataUploader", "attributeLngAbbreviation").toLowerCase() || field.label.toLowerCase() === localizationService.localize("dataUploader", "attributeLongAbbreviation").toLowerCase() || field.label.toLowerCase().includes(localizationService.localize("dataUploader", "attributeLongitudeName").toLowerCase()) ){
            field.type = 'LONGITUDE';
            
            if(types.indexOf("COORDINATE") === -1){
              types.push("COORDINATE");
            }
          }
        }
      }
      
      return types;
    }
    
    /**
     * @param targetPage <optional> 
     * @param leavingPage <optional> 
     */
    controller.next = function(targetPage, leavingPage) {
      $scope.pageDirection = "NEXT";
      
      if(targetPage && leavingPage){
        $scope.page.current = targetPage
          
            var snapshot = {
                page : leavingPage,
                sheet : angular.copy($scope.sheet)        
            };
            $scope.page.snapshots.push(snapshot);
      }
      else{
        // Linear logic
        if($scope.page.current == 'MATCH-INITIAL') {
          $scope.page.current = 'MATCH'
            
                var snapshot = {
                    page : 'MATCH-INITIAL',
                    sheet : angular.copy($scope.sheet)        
                };
                $scope.page.snapshots.push(snapshot);
        }
        else if($scope.page.current == 'MATCH') {
          $scope.page.current = 'BEGINNING-INFO';      
          
          var snapshot = {
              page : 'MATCH',
              sheet : angular.copy($scope.sheet)        
          };
          $scope.page.snapshots.push(snapshot);
        }
        else if($scope.page.current == 'BEGINNING-INFO') {
          $scope.page.current = 'INITIAL'; 
          controller.incrementStep($scope.page.current);
          $scope.currentStep = -1;
        }
        else if($scope.page.current === 'INITIAL') {
          // Go to fields page  
          $scope.page.current = 'FIELDS';      
          
          var stepTypes = controller.setAttributeDefaults();
          
          var snapshot = {
            page : 'INITIAL',
            sheet : angular.copy($scope.sheet)        
          };
          $scope.page.snapshots.push(snapshot);
          
          // re-set the step indicator since the snapshot re-sets the attributes page
          $scope.locationType = []; 
          $scope.userSteps = datasetService.getUploaderSteps(stepTypes);
          controller.incrementStep($scope.page.current);
        }
        else if($scope.page.current === 'FIELDS') {
          if(controller.hasLocationField()) {
            // Go to location attribute page
            $scope.page.current = 'LOCATION';      
          }
          else if (controller.hasCoordinateField()) {
            // Go to coordinate page
            $scope.page.current = 'COORDINATE';   
          }
          else {
            // Go to summary page
            $scope.page.current = 'SUMMARY'; 
          }
          
          controller.incrementStep($scope.page.current);
          
          var snapshot = {
            page : 'FIELDS',
            sheet : angular.copy($scope.sheet)        
          };
          $scope.page.snapshots.push(snapshot);
        }
        else if($scope.page.current === 'LOCATION') {
          if (controller.hasCoordinateField()) {
            // Go to coordinate page
            $scope.page.current = 'COORDINATE'; 
          }
          else {
            // Go to summary page
            $scope.page.current = 'SUMMARY';    
          }
          
          controller.incrementStep($scope.page.current);
          
          var snapshot = {
            page : 'LOCATION',
            sheet : angular.copy($scope.sheet)        
          };        
          $scope.page.snapshots.push(snapshot);
        }
        else if($scope.page.current === 'COORDINATE') {
          // Go to summary page
          $scope.page.current = 'SUMMARY';  
          
          controller.incrementStep($scope.page.current);
          
          var snapshot = {
            page : 'COORDINATE',
            sheet : angular.copy($scope.sheet)        
          };        
          $scope.page.snapshots.push(snapshot);
        }
        else if($scope.page.current === 'GEO-VALIDATION') {
          
          $scope.page.current = 'CATEGORY-VALIDATION';  
          
          controller.incrementStep($scope.page.current);
          
          var snapshot = {
            page : 'GEO-VALIDATION',
            sheet : angular.copy($scope.sheet)        
          };        

          $scope.page.snapshots.push(snapshot);
        }
      }
    }
    
    controller.incrementStep = function(targetPage) {
    	  if(targetPage === 'MATCH-INITIAL') {
    		  $scope.currentStep = -1;
          }
          else if(targetPage == 'MATCH') {
        	  $scope.currentStep = -1;
          }
          else if(targetPage == 'BEGINNING-INFO') {
            $scope.currentStep = -1;
          }
          else if(targetPage == 'INITIAL') {
            $scope.currentStep = 1;        
          }
          else if(targetPage === 'FIELDS') {
            $scope.currentStep = 2; 
          }
          else if(targetPage === 'LOCATION') {
        	  $scope.currentStep = 3;
          }
          else if(targetPage === 'COORDINATE') {
            if(controller.hasLocationField()) {
              $scope.currentStep = 4;
            }
            else{
              $scope.currentStep = 3;
            }
          }
          else if(targetPage === 'GEO-VALIDATION') {
            if(controller.hasLocationField() && controller.hasCoordinateField()) {
            	$scope.currentStep = 5;
            }
            else if (controller.hasLocationField()){
            	$scope.currentStep = 4;
            }
            else if (controller.hasCoordinateField()) {
            	$scope.currentStep = 4;
            }
            else {
            	$scope.currentStep = 3;
            }
          }
    }
    
    controller.prev = function() {
      $scope.pageDirection = "PREVIOUS";
      if($scope.page.current === 'MATCH' || $scope.page.current === "SUMMARY" || $scope.page.current === "BEGINNING-INFO" || $scope.page.current === "CATEGORY-VALIDATION") {
        controller.handlePrev();        
      }
      else {
        var title = localizationService.localize("dataUploader", "prevDialogTitle");
        
        var message = localizationService.localize("dataUploader", "prevDialogContent");
        
        var buttons = [];
        buttons.push({
          label : localizationService.localize("dataUploader", "ok"),
          config : {class:'btn btn-primary'},
          callback : function(){
            controller.handlePrev();
            
            // $scope.$apply();
          }
        });
        buttons.push({
          label : localizationService.localize("dataUploader", "cancel"),
          config : {class:'btn'},
        });
        
        widgetService.createDialog(title, message, buttons);      
      }
    }
    
    controller.isReady = function() {
      var current = $scope.page.current;
      
      return (current === 'SUMMARY' || current === 'CATEGORY-VALIDATION' || (current === 'GEO-VALIDATION' && $scope.problems.categories !== null && $scope.problems.categories.length === 0));
    }
    
    controller.hasNextPage = function() {
      var current = $scope.page.current;
      
      if(current == 'GEO-VALIDATION') {
        return ($scope.problems.categories !== null && $scope.problems.categories.length > 0);
      }
      
      return (current !== 'MATCH-INITIAL' && current !== 'SUMMARY' && current !== 'MATCH' && current !== 'CATEGORY-VALIDATION');
    }
    
    controller.handlePrev = function() {
      if($scope.page.snapshots.length > 0) { 
        if($scope.page.current === 'INITIAL') {
          $scope.page.current = 'BEGINNING-INFO'; 
        }
        else{
          $scope.$broadcast('pagePrev', {});
          
          var snapshot = $scope.page.snapshots.pop();
          
          $scope.page.current = snapshot.page;          
          $scope.sheet = snapshot.sheet;
          
          $scope.updateExistingDataset = false;
        }
      }
      
      
      if($scope.page.current === "SUMMARY" || $scope.page.current === 'CATEGORY-VALIDATION'){
        var stepCt = 4;
        if (!controller.hasCoordinateField()) {
          stepCt = stepCt - 1;
        }
        
        if(!controller.hasLocationField()) {
          stepCt = stepCt - 1;
        }
        
        if($scope.page.current === 'CATEGORY-VALIDATION') {
        	stepCt = stepCt - 1;
        }
        
        $scope.currentStep = stepCt;
      }
      else if($scope.page.current === "COORDINATE"){
        var stepCt = 3;
        if(!controller.hasLocationField()) {
          stepCt = stepCt - 1;
        }
        
        $scope.currentStep = stepCt;
      }
      else if($scope.page.current === "LOCATION"){
        $scope.currentStep = 2;
      }
      else if($scope.page.current === "FIELDS"){
        $scope.currentStep = 1;
      }
      else if($scope.page.current === "INITIAL"){
        $scope.currentStep = 0;
      }      
    }
    
    controller.hasLocationField = function() {
      for(var i = 0; i < $scope.sheet.fields.length; i++) {     
        var field = $scope.sheet.fields[i];
          
        if(field.type == 'LOCATION') {
          return true;
        }
      }        
      
      return false;
    }
    
    controller.hasCoordinateField = function() {
      for(var i = 0; i < $scope.sheet.fields.length; i++) {     
        var field = $scope.sheet.fields[i];
              
        if(field.type == 'LONGITUDE' || field.type == 'LATITUDE' ) {
          return true;
        }
      }        
          
      return false;      
    }
    
    $rootScope.$on('dataUpload', function(event, data){
      if(data.information != null && data.information.sheets.length > 0) {
        controller.load(data.information, data.options, data.classifiers);
      }
    });
    
    $scope.$on('loadConfiguration', function(event, data){
      // Go to summary page
      $scope.page.current = 'SUMMARY';  
      $scope.updateExistingDataset = true;
        
      var snapshot = {
        page : 'MATCH',
        sheet : angular.copy($scope.sheet)        
      };        
      $scope.page.snapshots.push(snapshot);

      angular.copy(data.sheet, $scope.sheet);
    });
    
    // AttributePageController emit's event when user toggles attribute types between location and non-location types
//    $scope.$on('hasLocationEvent', function(event, locationType) { 
//      $scope.locationType = locationType;
//      
//      if(locationType.length === 1 && locationType[0] === "LOCATION"){
//        $scope.userSteps = datasetService.getUploaderSteps(["LOCATION"]);
//      }
//      else if(locationType.length === 1 && locationType[0] === "LONGITUDE" || locationType[0] === "LATITUDE"){
//        $scope.userSteps = datasetService.getUploaderSteps(["COORDINATE"]);
//      }
//      else if(locationType.length > 1 && locationType.indexOf("LOCATION") !== -1 && (locationType.indexOf("LATITUDE") !== -1 || locationType.indexOf("LONGITUDE")) ){
//        $scope.userSteps = datasetService.getUploaderSteps(["LOCATION", "COORDINATE"]);
//      }
//      else{
//        $scope.userSteps = datasetService.getUploaderSteps([]);
//      }
//    });
    
    $scope.$on('hasSpecialTypeEvent', function(event, type) { 
    	var stepsConfigArr = [];
    	
    	if(type.type === "LOCATION"){
    		var locationType = type.typeConstant;
    		$scope.locationType = locationType;
    	      
    	    if(locationType.length === 1 && locationType[0] === "LOCATION"){
    	        stepsConfigArr.push("LOCATION");
    	    }
    	    else if(locationType.length === 1 && locationType[0] === "LONGITUDE" || locationType[0] === "LATITUDE"){
    	        stepsConfigArr.push("COORDINATE");
    	    }
    	    else if(locationType.length > 1 && locationType.indexOf("LOCATION") !== -1 && (locationType.indexOf("LATITUDE") !== -1 || locationType.indexOf("LONGITUDE")) ){
    	        stepsConfigArr.push("LOCATION");
    	        stepsConfigArr.push("COORDINATE");
    	    }
    	    
    		for(var i=0; i<$scope.sheet.fields.length; i++){
      		  var field = $scope.sheet.fields[i];
      		
      		  if(field.type === "CATEGORY" && stepsConfigArr.indexOf("CATEGORY") === -1){
      			  stepsConfigArr.push("CATEGORY");
      	      }
      		}
    	}
    	else if(type.type === "CATEGORY"){
    		stepsConfigArr.push("CATEGORY");
    		
    		for(var i=0; i<$scope.sheet.fields.length; i++){
    		  var field = $scope.sheet.fields[i];
    		
    		  if(field.type === "LOCATION" && stepsConfigArr.indexOf("LOCATION") === -1){
    			  stepsConfigArr.push("LOCATION");
    	      }
    	      else if(field.type === "LATITUDE" || field.type === "LONGITUDE"){
    	    	  for(var c=0; c<$scope.sheet.fields.length; c++){
    	    		  var field2 = $scope.sheet.fields[c];
    	    		  if(field2.type !== field.type && field2.type === "LATITUDE" || field2.type === "LONGITUDE"){
    	    			  if(stepsConfigArr.indexOf("COORDINATE") === -1){
        	    			  stepsConfigArr.push("COORDINATE");
        	    		  }
    	    		  }
    	    	  }
    	      }
    		}
    	}

    	$scope.userSteps = datasetService.getUploaderSteps(stepsConfigArr);
    });
    
    $scope.$on('nextPage', function(event, args) {
      // emitted from MatchInitialPageController
      controller.next(args.targetPage, args.leavingPage);
    });
  } 
  
  function UploaderDialog() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/data-uploader/uploader-dialog.jsp',
      scope: {
      },
      controller : UploaderDialogController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
      }
    }   
  }  
  
  function SynonymActionController($scope) {
    var controller = this;
      
    controller.init = function() {
      $scope.show = false;
    }
      
    controller.toggle = function() {
      $scope.show = !$scope.show;
    }
      
    controller.init();
  }
    
  function SynonymAction() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/data-uploader/synonym-action.jsp',
      scope: {
        action : '='
      },
      controller : SynonymActionController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs) {
      }
    }   
  } 
  
  function ValidateAccepted() {
    return {
      restrict: "A",
      scope : {
        field : '='  
      },
      require: 'ngModel',
      link: function (scope, element, attr, ngModel) {
//        scope.$watch('field.accepted', function() {
//          ngModel.$setValidity('accepted', scope.field.accepted);        
//        });
      }
    };
  };  
  
  
  angular.module("data-uploader", ["styled-inputs", "dataset-service", "category-service", "localization-service", "widget-service", "runway-service", "ngAnimate" ]);
  angular.module("data-uploader")
   .directive('attributesPage', AttributesPage)
   .directive('matchInitialPage', MatchInitialPage)
   .directive('matchPage', MatchPage)
   .directive('geoValidationPage', GeoValidationPage)
   .directive('geoValidationProblem', GeoValidationProblem)
   .directive('categoryValidationPage', CategoryValidationPage)
   .directive('categoryValidationProblem', CategoryValidationProblem)
   .directive('beginningInfoPage', BeginningInfoPage)
   .directive('namePage', NamePage)
   .directive('locationPage', LocationPage)
   .directive('coordinatePage', CoordinatePage)
   .directive('summaryPage', SummaryPage)
   .directive('validateAccepted', ValidateAccepted)
   .directive('synonymAction', SynonymAction)
   .directive('uploaderDialog', UploaderDialog);
})();
