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
  function NamePage() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/data-uploader/name-page.jsp',
      scope: true,
      link: function (scope, element, attrs) {
      }
    }   
  }  
  
  function AttributesPageController($scope) {
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
      
    controller.accept = function(field) {
      field.accepted = true;
    }
    
    controller.initialize();
  }
  
  function AttributesPage() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/data-uploader/attributes-page.jsp',
      scope: true,
      controller : AttributesPageController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs) {
      }
    }   
  }
  
  function LocationPageController($scope, $rootScope) {
    var controller = this;
        
    controller.initialize = function() {
      $scope.count = 0;

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
          if($scope.locationFields[field.universal]  == null) {
            $scope.locationFields[field.universal] = [];
          }
            
          $scope.locationFields[field.universal].push(field);            
        }
      }
      
      controller.newAttribute();
    }
    
    controller.getNextLocationField = function() {
      for(var i = ($scope.universals.length - 1); i >= 0; i--) {
        var universal = $scope.universals[i];
        
        if($scope.locationFields[universal.value] != null) {
          var fields = $scope.locationFields[universal.value];
          
          for(var j = 0; j < fields.length; j++) {
            var field = fields[j];
            
            if(!field.selected) {            	
              return field;
            }
          }
        }        
      }  
      
      return null;
    }
    
    controller.edit = function(attribute) {
      $scope.attribute = angular.copy(attribute);
    }
      
    controller.remove = function(attribute) {
      if($scope.sheet.attributes.values[attribute.id] != null) {
              
        delete $scope.sheet.attributes.values[attribute.id];        
        $scope.sheet.attributes.ids.splice( $.inArray(attribute.id, $scope.sheet.attributes.ids), 1 );
        
        // Update the field.selected status
        controller.setFieldSelected();
      }
    }
    
    controller.newAttribute = function() {
      if($scope.attribute != null) {      
        if($scope.attribute.id == -1) {
          $scope.attribute.id = ($scope.count++);      
          $scope.sheet.attributes.ids.push($scope.attribute.id);
          $scope.sheet.attributes.values[$scope.attribute.id] = {};              
        }     
        
        var attribute = $scope.sheet.attributes.values[$scope.attribute.id];      
        angular.copy($scope.attribute, attribute);              
        
        // Update the field.selected status
        controller.setFieldSelected();
      }
      
      var field = controller.getNextLocationField();      
      
      if(field != null) {
        $scope.attribute = {
          label : field.label,
          fields : {},
          id : -1
        };

        controller.addField(field);
      
        controller.setUniversalOptions(field);
      }
      else {
        $scope.attribute = null;
      }
    }
    
    controller.addField = function(field) {
      $scope.attribute.fields[field.universal] = field.label;
    }
    
    controller.setUniversalOptions = function(field) {
      $scope.universalOptions = [];
      var valid = true;
      
      for(var i = 0; i < $scope.universals.length; i++) {
        var universal = $scope.universals[i];
              
        if(universal.value == field.universal) {
          valid = false;              
        }
        else if(valid && $scope.locationFields[universal.value] != null) {
          $scope.universalOptions.push(universal);
        }
      }
    }
    
    controller.setFieldSelected = function() {
      for(var i = 0; i < $scope.sheet.fields.length; i++) {     
    	var field = $scope.sheet.fields[i]
    	
    	if(field.type == 'LOCATION') {
          field.selected = controller.isSelected(field);    		
    	}
        else {
          field.selected = false;                
        }
      }          
    }
    
    controller.isSelected = function(field) {
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
          var attribute = $scope.sheet.attributes.values[id];          
            
          if(attribute.label == label) {
            count++;
          }            
        }
              
        if(count > 0) {
          return false;
        }
      }  
            
      return true;
    }
      
    $scope.$watch('attribute.fields', function(){
      var length = Object.keys($scope.attribute.fields).length;
      var valid = (length > 1);
        
//      controller.attributeForm.$setValidity("size",  valid);
    }, true);   
    
    // Initialize the scope
    controller.initialize();
  }
  
  function LocationPage() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/data-uploader/location-page.jsp',
      scope: true,
      controller : LocationPageController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs) {
      }
    }   
  }
  
  function CoordinatePageController($scope) {
    var controller = this;
    
    controller.initialize = function() {        
      $scope.count = 0;
      $scope.coordinate = {
        label : "",
        latitude : "",
        longitude : "",
        featureLabel : "",
        location : "",
        featureId : "",
        id : -1
      };      
      
      $scope.latitudes = [];
      $scope.longitudes = [];
      $scope.featureLabels = [];
      $scope.locations = [];
      $scope.featureIds = [];
      
      for(var i = 0; i < $scope.sheet.fields.length; i++) {
        var field = $scope.sheet.fields[i];
        
        if(field.type == 'LATITUDE') {
          $scope.latitudes.push(field);          
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
      
      if($scope.sheet.attributes != null) {
        for(var i = 0; i < $scope.sheet.attributes.ids.length; i++) {
          var id = $scope.sheet.attributes.ids[i];
          var attribute = $scope.sheet.attributes.values[id];          
        
          $scope.locations.push({
            label : attribute.label
          });
        }
      }
    }
    
    controller.isBasic = function(field) {
      return (field.type == 'TEXT' || field.type == 'LONG' || field.type == 'DOUBLE');	
    }
    
    controller.edit = function(coordinate) {
      $scope.coordinate = angular.copy(coordinate);
    }
    
    controller.remove = function(coordinate) {
      if($scope.sheet.coordinates.values[coordinate.id] != null) {
        
        delete $scope.sheet.coordinates.values[coordinate.id];        
        $scope.sheet.coordinates.ids.splice( $.inArray(coordinate.id, $scope.sheet.coordinates.ids), 1 );
        
        // Update the field.selected status
        controller.setFieldSelected();
      }
    }
    
    controller.newCoordinate = function() {
      if($scope.coordinate.id == -1) {
        $scope.coordinate.id = ($scope.count++);      
        $scope.sheet.coordinates.ids.push($scope.coordinate.id);
        $scope.sheet.coordinates.values[$scope.coordinate.id] = {};              
      }     
      
      var coordinate = $scope.sheet.coordinates.values[$scope.coordinate.id];      
      angular.copy($scope.coordinate, coordinate);              
      
      $scope.coordinate = {
        label : "",
        latitude : "",
        longitude : "",
        featureLabel : "",
        location : "",
        featureId : "",
        id : -1
      };  
      
      // Update the field.selected status
      controller.setFieldSelected();
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
        
        if(count > 0) {
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
      templateUrl: '/partial/data-uploader/coordinate-page.jsp',
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
          }
        }
      }
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
      templateUrl: '/partial/data-uploader/summary-page.jsp',
      scope: true,
      controller : SummaryPageController,
      controllerAs : 'ctrl',            
      link: function (scope, element, attrs) {
      }
    }   
  }  

  function UploaderDialogController($scope, $rootScope, dataService) {
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
    }

    controller.setCountry = function(country) {
      $scope.sheet.country = country;
    }
    
    controller.persist = function() {
      var onSuccess = function(result) {
        controller.clear();
          
        $scope.$emit('closeUploader', {});
      }
        
      var onFailure = function(e){
        $scope.errors.push(e.message);
      };             

      // Reset the file Errors
      $scope.errors = [];
      
      dataService.importData($scope.configuration, '#uploader-div', onSuccess, onFailure);      
    }
    
    controller.cancel = function() {
      controller.clear();
    
      $scope.$emit('closeUploader', {});                
    }
    
    controller.load = function(information, options) {
      $scope.options = options;
      
      $scope.configuration = information;
      $scope.sheet = $scope.configuration.sheets[0];
      $scope.sheet.attributes = {ids:[], values : {}};    
      $scope.sheet.coordinates = {ids:[], values : {}};    
      $scope.show = true;
      
      $scope.page = {
        snapshots : [],     
        current : 'INITIAL',
      };
      
      $scope.$apply();
    }
    
    controller.next = function() {
      // State machine
      if($scope.page.current == 'INITIAL') {
        // Go to fields page  
        $scope.page.current = 'FIELDS';      
        
        var snapshot = {
          page : 'INITIAL',
          sheet : angular.copy($scope.sheet)        
        };
        $scope.page.snapshots.push(snapshot);
        
        $scope.$broadcast('nextPage', {});
      }
      else if($scope.page.current == 'FIELDS') {
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
        
        var snapshot = {
          page : 'FIELDS',
          sheet : angular.copy($scope.sheet)        
        };
        $scope.page.snapshots.push(snapshot);
      }
      else if($scope.page.current == 'LOCATION') {
        if (controller.hasCoordinateField()) {
          // Go to coordinate page
          $scope.page.current = 'COORDINATE';      
        }
        else {
          // Go to summary page
          $scope.page.current = 'SUMMARY';      
        }
        
        var snapshot = {
          page : 'LOCATION',
          sheet : angular.copy($scope.sheet)        
        };        
        $scope.page.snapshots.push(snapshot);
      }
      else if($scope.page.current == 'COORDINATE') {
        // Go to summary page
        $scope.page.current = 'SUMMARY';      
        
        var snapshot = {
          page : 'COORDINATE',
          sheet : angular.copy($scope.sheet)        
        };        
        $scope.page.snapshots.push(snapshot);
      }
    }
    
    controller.prev = function() {
      if($scope.page.snapshots.length > 0) {          
        $scope.$broadcast('pagePrev', {});
        
        var snapshot = $scope.page.snapshots.pop();
    	  
        $scope.page.current = snapshot.page;          
        $scope.sheet = snapshot.sheet;
      }        
    }
    
    controller.hasLocationField = function() {
      for(var i = 0; i < $scope.sheet.fields.length; i++) {     
        var field = $scope.sheet.fields[i]
          
        if(field.type == 'LOCATION') {
          return true;
        }
      }        
      
      return false;
    }
    
    controller.hasCoordinateField = function() {
      for(var i = 0; i < $scope.sheet.fields.length; i++) {     
        var field = $scope.sheet.fields[i]
              
        if(field.type == 'LONGITUDE' || field.type == 'LATITUDE' ) {
          return true;
        }
      }        
          
      return false;    	
    }
    
    $rootScope.$on('dataUpload', function(event, data){
      if(data.information != null && data.information.sheets.length > 0) {
        controller.load(data.information, data.options);
      }
    });    
  } 
  
  function UploaderDialog() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/data-uploader/uploader-dialog.jsp',
      scope: {
      },
      controller : UploaderDialogController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
      }
    }   
  }  
  
  function ValidateUnique() {
    return {
      restrict: "A",
      scope : {
        validator : '&'  
      },
      require: 'ngModel',
      link: function (scope, element, attr, ngModel) {
      
        element.bind('blur', function (e) {
          var unique = scope.validator()(element.val());
          ngModel.$setValidity('unique', unique);
          
          scope.$apply();
        });            
      }
    };
  };  
  
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
  
  
  angular.module("data-uploader", ["data-service", "styled-inputs"]);
  angular.module("data-uploader")
   .directive('attributesPage', AttributesPage)
   .directive('namePage', NamePage)
   .directive('locationPage', LocationPage)
   .directive('coordinatePage', CoordinatePage)
   .directive('summaryPage', SummaryPage)
   .directive('validateUnique', ValidateUnique)
   .directive('validateAccepted', ValidateAccepted)
   .directive('uploaderDialog', UploaderDialog);
})();
