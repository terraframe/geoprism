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
	
  // GLOBAL code to close pop-ups on any click
  $('body').on('click', function () {
    $('.styled-select-options').hide();
  });

  function StyledCheckBoxController($scope) {
    var controller = this;
      
    controller.toggle = function() {
      $scope.model = !$scope.model;  
    }
  }
  
  function StyledCheckBox() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl : '/partial/inputs/styled-check-box.jsp',    
      scope: {
        model:'=',
        label:'@',
      },
      controller : StyledCheckBoxController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
        if(attrs.id) {
          scope.id = attrs.id;
        }
        
        if(attrs.name) {
          scope.name = attrs.name;
        }
      }
    }    
  }  
  
  function StyledBasicSelectController($scope, $window) {
    var controller = this;
    controller.expand = false;
    
    // Close the pop-up on any click
    angular.element($window).bind('click', function (event) {
      controller.expand = false;
      $scope.$apply();
    });
    
    controller.toggle = function(e){
      e.stopPropagation();
      
      controller.offset = $(e.currentTarget).offset();      
      controller.width = $(e.currentTarget).width();      
      
      controller.expand = !controller.expand;       
    }
    
    controller.setValue = function(option) {
      $scope.model = option;
      
      controller.expand = false;
    }
    
    controller.isSelected = function(option) {
      return ($scope.model == option);
    }
    
    controller.style = function(value) {
      if($scope.style) {
        return {'font-family' : value};
      }
      
      return {};
    }
  }
  
  function StyledBasicSelect() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl : '/partial/inputs/styled-basic-select.jsp',    
      scope: {
        model:'=',
        options:'&',
        style:'@'        
      },
      controller : StyledBasicSelectController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
        scope.selectClass = {};
          
        if (attrs['class']) {
          scope.selectClass = attrs['class'];        
        }
      }
    }    
  }  
  
  function StyledSelectController($scope, $window) {
    var controller = this;
    controller.cache = {};
    controller.labelNamePossibilites = ["label", "displayLabel"];
    
    // Set default value and label attributes
    if($scope.value == null) {
      $scope.value = 'value';  
    }
    
    if($scope.label == null) {
    	for(var i = 0; i < controller.labelNamePossibilites.length; i++) {
    		var possibleLabel = controller.labelNamePossibilites[i];
            if(controller.label = $scope.options[i][possibleLabel]) {
            	$scope.label = possibleLabel;
            	break;
            }
        }
    }   
    
    controller.init = function() {
      var options = $scope.options;
      
      if(options != null) {
        var cache = {};
        
        for(var i = 0; i < options.length; i++) {
          var option = options[i];
          
          if(option != null) {
            var id = option[$scope.value];
            var label = option[$scope.label];
              
            cache[id] = label;            
          }
        }
        
        controller.cache = cache;
      }          
    }
    
    controller.toggle = function(e){
      e.stopPropagation();
    
      controller.offset = $(e.currentTarget).offset();      
      controller.width = $(e.currentTarget).width();      
      
      $(e.currentTarget).next().show();      
    }
    
    controller.setValue = function(option) {
      $scope.model = option[$scope.value];
      
      controller.label = option[$scope.label];      
      controller.expand = false;
    }
    
    controller.isSelected = function(option) {
      return ($scope.model == option);
    }
    
    $scope.$watch('options', function(newValue){
      if(newValue != null) {
        controller.init();            
      }
    });
  } 
  
  function StyledSelect() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl : '/partial/inputs/styled-select.jsp',    
      scope: {
        model:'=',
        options:'=',
        value:'@',
        label:'@'
      },
      controller : StyledSelectController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
        scope.selectClass = {};
          
        if (attrs['class']) {
          scope.selectClass = attrs['class'];        
        }
      }
    }    
  }  
  
  function NumberController($scope, localizationService) {
    var controller = this;
    
    controller.parseNumber = function(value) {
      return localizationService.parseNumber(value);
    }
    
    controller.formatNumber = function(value) {
      return localizationService.formatNumber(value);      
    }
  }
  
  function IntegerOnly() {
    return {
      restrict: 'A',
      controller : NumberController,
      controllerAs : 'ctrl',      
      require: ['ngModel', 'integerOnly'],
      link: function (scope, element, attrs, ctrls) {
        var ngModel = ctrls[0];
        var ctrl = ctrls[1];
      
        ngModel.$parsers.push(ctrl.parseNumber);
        ngModel.$formatters.push(ctrl.formatNumber);
        
        ngModel.$validators.integer = function(modelValue, viewValue) {
          if (ngModel.$isEmpty(viewValue)) {
            // consider empty models to be valid
            return true;
          }
            
          var number = ctrl.parseNumber( viewValue );
          var valid = ($.isNumeric(number) && Math.floor(number) == number);
          
          return valid;        
        }
      }
    }    
  }
  
  function NumberOnly() {
    return {
      restrict: 'A',
      controller : NumberController,
      controllerAs : 'ctrl',      
      require: ['ngModel', 'numberOnly'],
      link: function (scope, element, attrs, ctrls) {
        var ngModel = ctrls[0];
        var ctrl = ctrls[1];
      
        ngModel.$parsers.push(ctrl.parseNumber);
        ngModel.$formatters.push(ctrl.formatNumber);

      
        ngModel.$validators.integer = function(modelValue, viewValue) {
          if (ngModel.$isEmpty(viewValue)) {
            // consider empty models to be valid
            return true;
          }
          
          var number = ctrl.parseNumber( viewValue );
          
          return $.isNumeric(number);        
        }
      }
    }    
  }
  
  
  
  angular.module("styled-inputs", ["localization-service"]);
  angular.module("styled-inputs")
    .directive('styledCheckBox', StyledCheckBox)
    .directive('styledBasicSelect', StyledBasicSelect)
    .directive('styledSelect', StyledSelect)
    .directive('numberOnly', NumberOnly)
    .directive('integerOnly', IntegerOnly);    
})();
