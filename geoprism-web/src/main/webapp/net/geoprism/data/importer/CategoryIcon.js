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
  function CategoryIconController($scope, $timeout, categoryIconService, localizationService, widgetService) {
    var controller = this;
    
    console.log('Loading Category Icon');
    
    controller.init = function() {
      $scope.icon = {id:'', label:'', file:null, timeStamp:new Date().getTime()};
      $scope.show = false;
      
      var connection = {
        onSuccess : function(response) {
          for(var i=0; i<response.icons.length; i++){
        	  //timestamps are only needed to force angular re-render of image get request
        	  response.icons[i].timeStamp = new Date().getTime(); 
          }
          $scope.icons = response.icons;
            
          $scope.$apply();
        } 
      };      
      
      categoryIconService.getAll(connection);
    }
    
    controller.cleanFileName = function(file){
      var name = file.name;
      
      // TODO: remove this assumption
      return name.replace(".png", "");
    }
    
    controller.setFile = function(files) {
      if(files.length > 0) {
        var file = files[0];
      
        if($scope.icon.label == null || $scope.icon.label == '') {
          var label = controller.cleanFileName(file);
                  
          $scope.icon.label = label;
        }
        
        $scope.icon.type = file.type;
        $scope.icon.file = file;        
      }
    }
    
    controller.getIndex = function(icon) {
      for(var i = 0; i < $scope.icons.length; i++) {
        if($scope.icons[i].id === icon.id) {
          return i;
        }
      }
        
      return null;
    }
    
    controller.clear = function() {
      $scope.icon = {id:'', label:'', file:null, timeStamp:new Date().getTime()};
      $scope.editIcon = null;      
      $scope.errors = [];
    }
    
    controller.add = function() {
      $scope.show = true;
    }
    
    controller.cancel = function() {
      controller.clear();
      
      $scope.show = false;
    }
    
    controller.edit = function(icon) {
      var connection = {
        elementId : '#innerFrameHtml',
        onSuccess : function() {

        	var tempFile = {name:icon.label, type:icon.type, filePath:icon.filePath, fileReference:"NONE"}
            
        	$scope.editIcon = icon.id;
            $scope.icon = {id:icon.id, label:icon.label, file:tempFile};
            $scope.show = true;            
            $scope.$apply();
        }
      };
                        
      categoryIconService.edit(connection, icon.id);            
    }
    
    controller.remove = function(icon) {
      var title = localizationService.localize("category.icon", "deleteTitle", "Delete category icon");

      var message = localizationService.localize("category.icon", "removeContent", "Are you sure you want to delete the icon [{0}]?");
      message = message.replace('{0}', icon.label);
      
      var buttons = [];
      buttons.push({
      label : localizationService.localize("category.icon", "delete", "Delete"),
      config : {class:'btn btn-primary'},
        callback : function(){
          var connection = {
            elementId : '#innerFrameHtml',
            onSuccess : function() {
                       
              var index = controller.getIndex(icon);
                        
              if(index != null) {
                $scope.icons.splice(index, 1);        
              }
              
              $scope.$apply();
            },
            onFailure : function(e){
              $scope.errors.push(e.message);
              
              $scope.$apply();
            }
          
          };
          
          // Reset the errors          
          $scope.errors = [];
                    
          categoryIconService.remove(connection, icon.id);            
        }                    
      });
      buttons.push({
        label : localizationService.localize("category.icon", "cancel", "Cancel"),
        config : {class:'btn'},
      });
      
      widgetService.createDialog(title, message, buttons);
    }
    
    /*
     * Create icon section
     */
    controller.create = function() {
      var connection = {
        elementId : '#innerFrameHtml',
        onSuccess : function(result) {
        	
          controller.addCategoryIcons(result);
          controller.clear();
          
          $scope.show = false;                      
          $scope.$apply();
        },
        onFailure : function(e){
          $scope.errors.push(e.message);
          
          $scope.$apply();
        }
      };
      
      // Reset the file Errors
      $scope.errors = [];
      categoryIconService.create(connection, $scope.icon.file, $scope.icon.label);
    }    
    
    
    /*
     * Apply edits to an icon
     */
    controller.apply = function() {
      var connection = {
        elementId : '#innerFrameHtml',
        onSuccess : function(result) {               
          controller.updateCategoryIcons([result]);
          controller.clear();
          
          $scope.show = false;          
          $scope.$apply();
        },
        onFailure : function(e){
          $scope.errors.push(e.message);
          
          $scope.$apply();
        }
      };
      
      // Reset the file Errors
      $scope.errors = [];
      categoryIconService.apply(connection, $scope.editIcon, $scope.icon.file, $scope.icon.label);
    }    
    
    controller.addCategoryIcons = function(icons) {
      for(var i = 0; i < icons.length; i++) {
        var icon = icons[i];
        //timestamps are only needed to force angular re-render of image get request
        icon.timeStamp = new Date().getTime(); 
      
        if(!controller.exists(icon)) {
          $scope.icons.push(icon);        
        }
      }
    }
    
    controller.updateCategoryIcons = function(icons) {
        for(var i = 0; i < icons.length; i++) {
          var icon = icons[i];
        
          if(controller.exists(icon)) {
        	controller.updateIcon(icon);        
          }
        }
    }
    
    controller.exists = function(icon) {
      for(var i = 0; i < $scope.icons.length; i++) {
        if($scope.icons[i].id == icon.id) {
          return true;
        }
      }
      
      return false;
    }
    
    controller.updateIcon = function(icon) {
      for(var i = 0; i < $scope.icons.length; i++) {
        if($scope.icons[i].id == icon.id) {
          //timestamps are only needed to force angular re-render of image get request
          icon.timeStamp = new Date().getTime();
          $scope.icons[i] = icon;
        }
      }
    }
    
    $scope.$watch('icon.file', function(file){
      controller.form.$setValidity('file', (file != null) && (file.type == 'image/png' || $scope.editIcon));    	
    }, true);
    
    controller.init();
  }
  
  
  angular.module("category-icon", ['ngFileUpload', "category-icon-service", "localization-service", "widget-service", "runway-service"]);
  angular.module("category-icon")
  .controller('CategoryIconController', CategoryIconController)
})();
