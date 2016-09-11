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
  function CategoryController($scope, $timeout, categoryService) {
    var controller = this;
    
    controller.init = function() {
      $scope.showForm = false;
      
      var connection = {
        onSuccess : function(response) {
          $scope.categories = response;
            
          $scope.$apply();
        } 
      };      
      
      categoryService.getAll(connection);
    }    
    
    controller.edit = function(category) {
      var connection = {
        elementId : '#innerFrameHtml',
        onSuccess : function(response) {
          $scope.$emit('categoryEdit', response);            
          
          $scope.$apply();
        }
      };

      categoryService.get(connection, category.value);
    }
    
    $scope.$on('categoryOk', function(event, data){
      if(data != null && data.category != null) {
        var index = -1;
      
        for (var i = 0; i < $scope.categories.length; i++) {
          if(data.category.id =  $scope.categories[i].value) {
            index = i;
          }
        }
    
        if(index != -1){
          $scope.categories[index].label = data.category.label;
        }    
      }
    });    
    
    controller.init();
  }
  
  function CategoryModalController($scope, $rootScope, categoryService) {
    var controller = this;
      
    controller.init = function() {
      $scope.show = false;
    }
      
    controller.load = function(category) {
      if(category != null) {
        $scope.category = category;        
      }
      
      $scope.page = 'CATEGORY';
      $scope.show = true;
    }
      
    controller.ok = function() {      
      $scope.show = false;
    }
    
    $scope.$on('editOption', function(event, data){
      $scope.option = data;
      $scope.page = 'OPTION';
    
      event.stopPropagation();
    });
    
    $scope.$on('editOptionDone', function(event, data){
      $scope.option = undefined;
    
      controller.load(data);
      
      event.stopPropagation();
    });
    
    $scope.$on('categoryOk', function(event, data){
      controller.ok();
    });
      
    $rootScope.$on('categoryEdit', function(event, data) {
      controller.load(data);
    });      
     
    controller.init();
  }
  
  function CategoryModal() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/data/browser/category-modal.jsp',
      scope: {
      },
      controller : CategoryModalController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs, ctrl) {
      }
    }   
  }
  
  function CategoryPageController($scope, categoryService, datasetService, widgetService, localizationService, $window) {
    var controller = this;
    
    controller.init = function() {
      $scope.instance = {
        isNew : false,
        label : '',
        parentId : $scope.category.id
      };      
      
      $window.onclick = null;
    }
      
    controller.ok = function() {
      if(controller.form.$dirty) {
        var connection = {
          elementId : '#innerFrameHtml',
          onSuccess : function() {
            $scope.$emit('categoryOk', {category : $scope.category});        

            $scope.$apply();
          },
          onFailure : function(e){
            $scope.errors.push(e.message);
                   
            $scope.$apply();
          }        
        };
              
        $scope.errors = [];
              
        categoryService.updateCategory(connection, JSON.stringify($scope.category));
      }
      else {
        $scope.$emit('categoryOk');        
      }
    }
    
    controller.newInstance = function() {
      $scope.instance.isNew = true;
      
      $window.onclick = function (event) {
        if(!event.target.classList.contains("list-table-input") && !event.target.classList.contains("fa") && event.target.type !== 'button' ){
            if( $scope.instance.isNew && $scope.instance.label.length > 0 ){
              controller.apply();
            }
            else{
              $scope.instance.isNew = false;
            }
            
            $scope.$apply();
        }
      };
    }
    
    controller.isUniqueLabel = function(label, ngModel, scope) {
      var connection = {
        onSuccess : function() {
          ngModel.$setValidity('unique', true);       
          scope.$apply();          
        },
        onFailure : function(e){
          ngModel.$setValidity('unique', false);          
          scope.$apply();
        }
      };
              
      if(label != null && label != '') {      
        datasetService.validateCategoryName(connection, label, $scope.category.id);
      }        
    }        
    
    controller.apply = function() {
      var connection = {
        elementId : '#innerFrameHtml',
        onSuccess : function(option) {
          $scope.category.descendants.push(option);
          
          controller.init();          
                
          $scope.$apply();
        },
        onFailure : function(e){
          $scope.errors.push(e.message);
                         
          $scope.$apply();
        }        
      };
              
      $scope.errors = [];
              
      categoryService.createOption(connection, JSON.stringify($scope.instance));
    }
    
    controller.cancel = function() {
      controller.init();      
    }
    
    
    controller.removeDescendant = function(descendantId) {
      var index = -1;
      
      for(var i = 0; i < $scope.category.descendants.length; i++) {
        var descendant = $scope.category.descendants[i];
    
        if(descendant.id == descendantId) {
          index = i;
        }
      }
      
      if(index != -1) {
        $scope.category.descendants.splice(index, 1);      
      }
    }    
      
    controller.remove = function(descendant) {
      var title = localizationService.localize("category.management", "removeOptionTitle", "Delete option");

      var message = localizationService.localize("category.management", "removeOptionConfirm", "Are you sure you want to delete the category option [{0}]?");
      message = message.replace('{0}', descendant.label);
          
      var buttons = [];
      buttons.push({
        label : localizationService.localize("layer.category", "ok", "Ok"),
        config : {class:'btn btn-primary'},
        callback : function(){
          controller.performRemove(descendant);
        }
      });
      buttons.push({
        label : localizationService.localize("dataset", "cancel", "Cancel"),
        config : {class:'btn'}
      });
          
      widgetService.createDialog(title, message, buttons);    
    }
    
    controller.performRemove = function(descendant) {    
      var connection = {
        elementId : '#innerFrameHtml',
        onSuccess : function() {
          controller.removeDescendant(descendant.id);
                          
          $scope.$apply();
        },
        onFailure : function(e){
          $scope.errors.push(e.message);
                                   
          $scope.$apply();
        }        
      };
                      
      $scope.errors = [];
                        
      categoryService.deleteOption(connection, descendant.id);         
    }
    
    controller.edit = function(descendant) {
      var connection = {
        elementId : '#innerFrameHtml',
        onSuccess : function(response) {
          $scope.$emit('editOption', response);
        
          $scope.$apply();
        },
        onFailure : function(e){
          $scope.errors.push(e.message);
          
          $scope.$apply();
        }        
      };

      $scope.errors = [];
      categoryService.edit(connection, descendant.id);      
    }
    

    controller.init();
  }  
  
  function CategoryPage() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/data/browser/category-page.jsp',
      scope: true,
      controller : CategoryPageController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs, ctrl) {
      }
    }   
  }
  
  function OptionPageController($scope, categoryService, widgetService, localizationService) {
    var controller = this;
    
    $scope.actions = {
      synonym : '',
      restore : []
    }
      
    controller.apply = function() {
      var connection = {
        elementId : '#innerFrameHtml',
        onSuccess : function(response) {
          $scope.$emit('editOptionDone', response);
        
          $scope.$apply();
        },
        onFailure : function(e){
          $scope.errors.push(e.message);
                 
          $scope.$apply();
        }        
      };
      
      $scope.errors = [];
      
      var config = {
        categoryId : $scope.category.id,
        option : $scope.option,
        synonym : $scope.actions.synonym,
        restore : $scope.actions.restore
      }
      
      categoryService.applyOption(connection, JSON.stringify(config));
    }
    
    controller.cancel = function() {
      var connection = {
        elementId : '#innerFrameHtml',
        onSuccess : function() {
          $scope.$emit('editOptionDone');
                
          $scope.$apply();
        },
        onFailure : function(e){
          $scope.errors.push(e.message);
                         
          $scope.$apply();
        }        
      };
          
      $scope.errors = [];
      
      categoryService.unlock(connection, $scope.option.id);              
    }
    
    controller.filterOption = function() {
      return function (item) {
        return (item.id != $scope.option.id);
      };
    }
    
    controller.removeSynonym = function(synonymId) {
      var index = -1;
      
      for(var i = 0; i < $scope.option.synonyms.length; i++) {
        var synonym = $scope.option.synonyms[i];
    
        if(synonym.id == synonymId) {
          index = i;
        }
      }
      
      if(index != -1) {
        $scope.option.synonyms.splice(index, 1);      
      }
    }
    
    controller.restore = function(synonym) {
      var title = localizationService.localize("category.management", "restoreSynonymTitle", "Restore synonym");

      var message = localizationService.localize("category.management", "restoreConfirm", "Are you sure you want to restore the the synonym [{0}] to its own category option?");
      message = message.replace('{0}', synonym.label);
        
      var buttons = [];
      buttons.push({
        label : localizationService.localize("layer.category", "ok", "Ok"),
        config : {class:'btn btn-primary'},
        callback : function(){
          $scope.actions.restore.push(synonym.id);
          controller.removeSynonym(synonym.id);
          
          $scope.$apply();
        }
      });
      buttons.push({
        label : localizationService.localize("dataset", "cancel", "Cancel"),
        config : {class:'btn'}
      });
        
      widgetService.createDialog(title, message, buttons);    
    } 
  }  
  
  function OptionPage() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/data/browser/option-page.jsp',
      scope: true,
      controller : OptionPageController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
      }
    }   
  }
  
  angular.module("category-management", ["styled-inputs", "category-service", "dataset-service", "widget-service", "localization-service"]);
  angular.module("category-management")
   .controller('CategoryController', CategoryController)
   .directive('categoryModal', CategoryModal)
   .directive('categoryPage', CategoryPage)
   .directive('optionPage', OptionPage);
})();
