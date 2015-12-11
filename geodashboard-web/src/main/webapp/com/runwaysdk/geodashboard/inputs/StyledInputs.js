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
  $(document).on('click', function () {
    $('.styled-select-options').hide();
    
    $('.styled-select-area').removeClass('select-focus');      
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
    controller.resized = false;
    
    controller.toggle = function($event){
      $event.preventDefault();      
      $event.stopPropagation();
      
      var dropdown = $($event.currentTarget).next();      
      
      if(!controller.resized) {
        var offset = $($event.currentTarget).offset();      
        var width = $($event.currentTarget).width(); 
        var height = $($event.currentTarget).height();
          
        dropdown.css('top', (offset.top + height + 2));
        dropdown.css('width', (width + 2));        
        
        controller.resized = true;
      }
      
      dropdown.toggle();      
      
//      $($event.currentTarget).focus();
    }
    
    controller.focus = function($event) {
      $($event.currentTarget).addClass('select-focus');                	
    }
    
    controller.blur = function($event) {
      $($event.currentTarget).removeClass('select-focus');                  
    }    
    
    controller.keypress = function($event) {    	
      // Up arrow
      if ($event.keyCode == 38) {
        $event.preventDefault();            
        $event.stopPropagation();          
    	  
        // Find the currently select value
        var dropdown = $($event.currentTarget).next();
        var element = dropdown.find('.current-selected').prev('.styled-option');
        
        if(element.length > 0) {
          var value = element.find('.styled-option-value').data('value');
          
          $scope.model = value;
        }
      }
      // Down arrow
      else if ($event.keyCode == 40) {
        $event.preventDefault();            
        $event.stopPropagation();          
    	  
        // Find the currently select value
        var dropdown = $($event.currentTarget).next();
        var element = dropdown.find('.current-selected').next('.styled-option');
          
        if(element.length > 0) {
          var value = element.find('.styled-option-value').data('value');
            
          $scope.model = value;
        }        
      }
      // Enter
      else if ($event.keyCode == 13) {        
        controller.toggle($event);
      }      
    }
    
    controller.setValue = function(option) {
      $scope.model = option;
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
        if (attrs['class']) {
          scope.selectClass = attrs['class'];        
        }
      }
    }    
  }  
  
  function StyledSelectController($scope, $window) {
    var controller = this;
    controller.resized = false;    
    controller.cache = {};
    
    // Set default value and label attributes
    if($scope.value == null) {
      $scope.value = 'value';  
    }
    
    if($scope.label == null) {
      $scope.label = 'label';
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
    
    controller.setOption = function(option) {
      controller.setValue(option[$scope.value]);
    }
    
    controller.setValue = function(value) {
      if(value != $scope.model) {
        $scope.model = value;
        
        // Fire the on-change callback if it exists
        var onChange = $scope.onChange();
        
        if(onChange != null) {
          onChange(value);  
        }        
      }
    }
    
    controller.isSelected = function(option) {
      return ($scope.model == option);
    }
    
    $scope.$watch('options', function(newValue){
      if(newValue != null) {
        controller.init();            
      }
    });
    
    controller.toggle = function($event){
      $event.preventDefault();      
      $event.stopPropagation();
      
      var dropdown = $($event.currentTarget).next();      
        
      if(!controller.resized) {
        var offset = $($event.currentTarget).offset();      
        var width = $($event.currentTarget).width(); 
        var height = $($event.currentTarget).height();
        var topOffset = offset.top + height + 2
            
        //// TEMPORARY HACK to account for offset from modal to window
        if( $("#builder-div").length > 0 ){
        	topOffset = topOffset - $("#builder-div").offset().top + 8;
        }
        
        dropdown.css('top', topOffset);
        dropdown.css('width', (width + 2));        
          
        controller.resized = true;
      }
        
      dropdown.toggle();      
        
//      $($event.currentTarget).focus();
    }
    
    controller.focus = function($event) {
      $($event.currentTarget).addClass('select-focus');                  
    }
    
    controller.blur = function($event) {
      $($event.currentTarget).removeClass('select-focus');                  
    }
      
    controller.keypress = function($event) {      
      // Up arrow
      if ($event.keyCode == 38) {      
        $event.preventDefault();
        $event.stopPropagation();
    	  
        // Find the currently select value
        var dropdown = $($event.currentTarget).next();
        var element = dropdown.find('.current-selected').prev('.styled-option');
          
        if(element.length > 0) {
          var value = element.find('.styled-option-value').data('value');
            
          controller.setValue(value);
        }
      }
      // Down arrow
      else if ($event.keyCode == 40) {
        $event.preventDefault();
        $event.stopPropagation();
    	  
        // Find the currently select value
        var dropdown = $($event.currentTarget).next();
        var element = dropdown.find('.current-selected').next('.styled-option');
            
        if(element.length > 0) {
          var value = element.find('.styled-option-value').data('value');
              
          controller.setValue(value);
        }        
      }
      // Enter
      else if ($event.keyCode == 13) {
        controller.toggle($event);
      }
    }
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
        label:'@',
        onChange:'&'
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
  
  function ConvertToNumber() {
    return {
      require: 'ngModel',
      link: function(scope, element, attrs, ngModel) {
        ngModel.$parsers.push(function(val) {
          return parseFloat(val);
        });
       
        ngModel.$formatters.push(function(val) {
          return '' + val;
        });
      }
    };
  }
  
  function ConvertToPercent() {
    return {
      require: 'ngModel',
      link: function(scope, element, attrs, ngModel) {
        ngModel.$parsers.push(function(val) {
          return parseFloat(val);
        });
        
        ngModel.$formatters.push(function(val) {
          return '' + (val * 100);
        });
      }
    };
  }
  
  
  
  angular.module("styled-inputs", ["localization-service"]);
  angular.module("styled-inputs")
    .directive('styledCheckBox', StyledCheckBox)
    .directive('styledBasicSelect', StyledBasicSelect)
    .directive('styledSelect', StyledSelect)
    .directive('convertToPercent', ConvertToPercent)
    .directive('convertToNumber', ConvertToNumber)
    .directive('numberOnly', NumberOnly)
    .directive('integerOnly', IntegerOnly);    
})();
