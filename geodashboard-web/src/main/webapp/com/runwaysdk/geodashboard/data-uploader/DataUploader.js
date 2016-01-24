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
        
    // Initialize the scope
    $scope.count = 0;
    $scope.attribute = {
      label : "Test",
      fields : {},
      id : -1
    };      
    $scope.sheet.attributes = {ids:[], values : {}};  
    
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
    
    controller.toggleField = function(universal, field) {
      if($scope.attribute.fields[universal.value] != null && $scope.attribute.fields[universal.value].name == field.name){
        delete $scope.attribute.fields[universal.value];
      }
      else {        
        $scope.attribute.fields[universal.value] = field;      
      }
    }
      
    controller.newAttribute = function() {
      if($scope.attribute.id == -1) {
        $scope.attribute.id = ($scope.count++);      
        $scope.sheet.attributes.ids.push($scope.attribute.id);
        $scope.sheet.attributes.values[$scope.attribute.id] = {};              
      }     
      
      var attribute = $scope.sheet.attributes.values[$scope.attribute.id];      
      angular.copy($scope.attribute, attribute);              
        
      $scope.attribute = {
        label : "",
        fields : {},
        id : -1
      };  
      
      // Update the field.selected status
      controller.setFieldSelected();
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
            if(attribute.fields[key].name == field.name) {
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
                
          if(field.label == label) {
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
        
      controller.attributeForm.$setValidity("size",  valid);
    }, true);    
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

  function UploaderDialogController($scope, $rootScope) {
    var controller = this;
    
    // Flag indicating if the modal and all of its elements should be destroyed
    $scope.show = false;    
    
    controller.clear = function() {
      // Flag indicating if the modal and all of its elements should be destroyed
      $scope.show = false;
        
      // Flag indicating if the modal is waiting from a response from the server
      $scope.busy = false;   
      
      // List of errors
      $scope.errors = null;
      
      // Stack of state snapshots captured when the user clicks next
      $scope.snapshots = null;      
      $scope.currentPage = null;
      $scope.pageCount = null;

      
      $scope.options = null;      
      $scope.sheet = null;      
      $scope.universals = null;
    }

    controller.setCountry = function(country) {
      $scope.sheet.country = country;
    }
        
    controller.updateUniversalOptions = function() {
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
    
    controller.persist = function() {
      controller.clear();
    
      $scope.$emit('closeUploader', {});                    
    }
    
    controller.cancel = function() {
      controller.clear();
    
      $scope.$emit('closeUploader', {});                
    }
    
    controller.load = function(sheets, options) {
      $scope.options = options;
      
      $scope.sheet = sheets[0];      
      $scope.show = true;

      $scope.currentPage = 1;
      $scope.pageCount = 3;
      
      $scope.snapshots = [];
      
      $scope.$apply();
    }
    
    controller.next = function() {
      if($scope.currentPage < $scope.pageCount) {
        $scope.$broadcast('pageNext', {});                
    	  
        // Snapshot the current state
        $scope.snapshots.push(angular.copy($scope.sheet));
        
        $scope.currentPage = $scope.currentPage + 1;        
      }
    }
    
    controller.prev = function() {
      if($scope.currentPage > 1) {          
        $scope.$broadcast('pagePrev', {});
    	  
        $scope.currentPage = $scope.currentPage - 1;
          
        // Revert back to a previous snapshot
        $scope.sheet = $scope.snapshots.pop();
      }        
    }
    
    $rootScope.$on('dataUpload', function(event, data){
      if(data.sheets != null && data.sheets.length > 0) {
        controller.load(data.sheets, data.options);
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
   .directive('validateUnique', ValidateUnique)
   .directive('validateAccepted', ValidateAccepted)
   .directive('uploaderDialog', UploaderDialog);
})();
