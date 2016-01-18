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
      templateUrl: '/partial/data-uploader/data-set-name-page.jsp',
      scope: true,
      link: function (scope, element, attrs) {
      }
    }   
  }  
  
  function AttributesPage() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/data-uploader/data-set-attributes-page.jsp',
      scope: true,
      link: function (scope, element, attrs) {
      }
    }   
  }	
	
  function UploaderDialogController($scope, $rootScope) {
    var controller = this;
    
    controller.clear = function() {
      // Flag indicating if the modal and all of its elements should be destroyed
      $scope.show = false;
        
      // Flag indicating if the modal is waiting from a response from the server
      $scope.busy = false;   
      
      // List of errors
      $scope.errors = [];
      
      // Stack of state snapshots captured when the user clicks next
      $scope.snapshots = [];
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
      $scope.currentSheet = 0;
      
      $scope.sheet = sheets[$scope.currentSheet];      
      $scope.show = true;

      $scope.currentPage = 1;
      $scope.pageCount = 2;
      
      $scope.$apply();
    }
    
    controller.next = function() {
      // Snapshot the current state
      $scope.snapshots.push($scope.sheet);
    	
      $scope.currentPage = $scope.currentPage + 1;
    }
    
    controller.prev = function() {
      $scope.currentPage = $scope.currentPage - 1;
      
      // Revert back to a previous snapshot
      $scope.sheet = $scope.snapshots.pop();
    }
    
    $rootScope.$on('dataUpload', function(event, data){
      if(data.sheets != null && data.sheets.length > 0) {
        controller.load(data.sheets, data.options);
      }
    });
    
    controller.clear();
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
  
  angular.module("data-uploader", ["data-service"]);
  angular.module("data-uploader")
   .directive('attributesPage', AttributesPage)
   .directive('namePage', NamePage)
   .directive('uploaderDialog', UploaderDialog);
})();
