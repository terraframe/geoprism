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
  function DatasetController($scope, $timeout, datasetService, localizationService, widgetService) {
    var controller = this;
    
    controller.init = function() {
      var connection = {
        onSuccess : function(response) {
          $scope.datasets = response.datasets;
            
          $scope.$apply();
        } 
      };      
      
      datasetService.getAll(connection);
    }
    
    controller.getIndex = function(dataset) {
      for(var i = 0; i < $scope.datasets.length; i++) {
        if($scope.datasets[i].id === dataset.id) {
          return i;
        }
      }
      
      return null;
    }
    
    controller.remove = function(dataset) {
      var title = localizationService.localize("dataset", "deleteDatasetTitle", "Delete dataset");

      var message = localizationService.localize("dataset", "removeContent", "Are you sure you want to delete the dataset [{0}]?");
      message = message.replace('{0}', dataset.label);
      
      var buttons = [];
      buttons.push({
    	label : localizationService.localize("dataset", "delete", "Delete"),
    	config : {class:'btn btn-primary'},
        callback : function(){
          var connection = {
            elementId : '#innerFrameHtml',
            onSuccess : function() {
              // Find and remove the dataset from the datasets array         
              var index = controller.getIndex(dataset);
                        
              if(index != null) {
                $scope.datasets.splice(index, 1);        
              }
                          
              $scope.$apply();
            }
          };
                    
          datasetService.remove(connection, dataset.id);            
        }                	  
      });
      buttons.push({
        label : localizationService.localize("dataset", "cancel", "Cancel"),
        config : {class:'btn'},
      });
      
      widgetService.createDialog(title, message, buttons);
    }
    
    /*
     * Data Upload Section
     */
    controller.uploadFile = function(files) {
      var connection = {
        elementId : '#innerFrameHtml',
        onSuccess : function(result) {               
          $scope.$emit('dataUpload', {information:result.information, options:result.options});            
                
          // Hide modal, but preserve the elements and values        
          $scope.hidden = true;
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
      datasetService.uploadSpreadsheet(connection, files[0]);
    }    
    
    controller.addDatasets = function(datasets) {
      for(var i = 0; i < datasets.length; i++) {
        var dataset = datasets[i];
      
        if(!controller.exists(dataset)) {
          $scope.datasets.push(dataset);        
        }
      }
    }
    
    controller.exists = function(dataset) {
      for(var i = 0; i < $scope.datasets.length; i++) {
        if($scope.datasets[i].id == dataset.id) {
          return true;
        }
      }
      
      return false;
    }
    
    $scope.$on('datasetChange', function(event, data){    
      if(data.datasets != null) {
        controller.addDatasets(data.datasets);
      }
    });    
    
    controller.init();
  }

  angular.module("data-set", ["data-uploader", "styled-inputs", 'ngFileUpload', "dataset-service", "localization-service", "widget-service", "runway-service"]);
  angular.module("data-set")
  .controller('DatasetController', DatasetController)
})();
