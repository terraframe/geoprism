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
    
    controller.init = function() {
      $scope.icon = {label:'', file:null};
      
      var connection = {
        onSuccess : function(response) {
          $scope.icons = response.icons;
            
          $scope.$apply();
        } 
      };      
      
      categoryIconService.getAll(connection);
    }
    
    controller.cleanFileName = function(file){
      var name = file.name;
      
      return name.replace(".png", "");
    }
    
    controller.setFile = function(files) {
      if(files.length > 0) {
        var file = files[0];
      
        if($scope.icon.label == null || $scope.icon.label == '') {
          var label = controller.cleanFileName(file);
                  
          $scope.icon.label = label;
        }
        
        $scope.icon.file= file;        
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
    
    controller.edit = function(icon) {
      var connection = {
        elementId : '#innerFrameHtml',
        onSuccess : function() {
          // Find and remove the dataset from the datasets array         
          var index = controller.getIndex(icon);
                            
          if(index != null) {
            $scope.icons.splice(index, 1);        
          }
                              
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
              // Find and remove the dataset from the datasets array         
              var index = controller.getIndex(icon);
                        
              if(index != null) {
                $scope.icons.splice(index, 1);        
              }
                          
              $scope.$apply();
            }
          };
                    
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
          
          $scope.icon = {label:'', file:null};
          $scope.$apply();
        },
        onFailure : function(e){
          $scope.errors.push(e.message);
          
          $scope.$apply();
          
          $('#app-container').parent().parent().animate({ scrollTop: 0 }, 'slow');
        }
      };
      
      // Reset the file Errors
      $scope.errors = [];
      categoryIconService.create(connection, $scope.icon.file, $scope.icon.label);
    }    
    
    controller.addCategoryIcons = function(icons) {
      for(var i = 0; i < icons.length; i++) {
        var icon = icons[i];
      
        if(!controller.exists(icon)) {
          $scope.icons.push(icon);        
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
    
    $scope.$watch('icon.file', function(file){
      controller.form.$setValidity('file', (file != null) && (file.type == 'image/png'));
    }, true);
    
    controller.init();
  }

  angular.module("category-icon", ['ngFileUpload', "category-icon-service", "localization-service", "widget-service", "runway-service"]);
  angular.module("category-icon")
  .controller('CategoryIconController', CategoryIconController)
})();
