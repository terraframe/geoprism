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
  function DatasetController($scope, $timeout, datasetService) {
    var controller = this;
    
    controller.init = function() {
      var onSuccess = function(json) {
        var response = JSON.parse(json);
        
        $scope.datasets = response.datasets;
        
        $scope.$apply();
      };
      
      datasetService.getAll(onSuccess);	
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
      var fac = com.runwaysdk.ui.Manager.getFactory();
        
      var message = com.runwaysdk.Localize.localize("dataset", "removeContent", "Are you sure you want to delete the dataset [{0}]?");
      message = message.replace('{0}', dataset.label);
      
      var onRemove = function(){
        dialog.close();
        
        var onSuccess = function() {
          // Find and remove the dataset from the datasets array         
          var index = controller.getIndex(dataset);
        
          if(index != null) {
            $scope.datasets.splice(index, 1);        
          }
          
          $scope.$apply();
        }
              
        datasetService.remove(dataset.id, "#app-container" , onSuccess);            
      }
      
      var onDelete = function() {
        dialog.close();
      }
      
      var title = com.runwaysdk.Localize.localize("dataset", "deleteDatasetTitle", "Delete dataset");
      
      var dialog = fac.newDialog(title);
      dialog.appendContent(message);
      dialog.addButton(com.runwaysdk.Localize.localize("dataset", "delete", "Delete"), onRemove, null, {class:'btn btn-primary'});
      dialog.addButton(com.runwaysdk.Localize.localize("dataset", "cancel", "Cancel"), onDelete, null, {class:'btn'});            
      dialog.render();
    }
    
    /*
     * Data Upload Section
     */
    controller.uploadFile = function(files) {
      var onSuccess = function(result) {
        
        $scope.$emit('dataUpload', {information:result.information, options:result.options});            
        
        // Hide modal, but preserve the elements and values        
        $scope.hidden = true;
        $scope.$apply();
      }
      
      var onFailure = function(e){
        $scope.errors.push(e.message);
                         
        $scope.$apply();
                
        $('#app-container').parent().parent().animate({ scrollTop: 0 }, 'slow');
      };             

      // Reset the file Errors
      $scope.errors = [];
      datasetService.uploadSpreadsheet(files[0], '#builder-div', onSuccess, onFailure);
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
    
    $scope.$on('closeUploader', function(event, data){    
      if(data.datasets != null) {
        controller.addDatasets(data.datasets);
      }
    });    
    
    controller.init();
  }
	
  angular.module("data-set", ["data-uploader", "dataset-service", "styled-inputs", 'ngFileUpload']);
  angular.module("data-set")
  .controller('DatasetController', DatasetController)
})();
