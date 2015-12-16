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
    
    // TODO: setup initial model value so dropdown has selected value by default
//    $scope.model = value;
    
    
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
        
        // TODO: setup initial model value so dropdown has selected value by default
//        $scope.model = options[0].value;
        
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
    
    
    $scope.$watch('options', function(newValue){
      if(newValue != null) {
        controller.init();            
      }
    });
    
    // TESTING
    $scope.$watch('model', function(newValue){
          console.log(newValue);          
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
  
  /**
   * Directive attached to all basic category input elements to hook the actual 
   * autocomplete action.
   */
  function CategoryAutoComplete($timeout) {
    return {
      restrict: "A",
      scope:{
        source : "&",
        ngModel : '='
      },
      require : 'ngModel',
      link: function (scope, element, attr, ngModel) {
       
        $timeout(function(){
         
          $(element).autocomplete({
            source: scope.source(),
            minLength: 1,
            select : function(event, ui) {
              ngModel.$setViewValue(ui.item.value);
            }
          });
        }, 500); 
      }
    };
  };
  
  angular.module("styled-inputs", ["localization-service"]);
  angular.module("styled-inputs")
    .directive('styledCheckBox', StyledCheckBox)
    .directive('styledBasicSelect', StyledBasicSelect)
    .directive('styledSelect', StyledSelect)
    .directive('convertToPercent', ConvertToPercent)
    .directive('convertToNumber', ConvertToNumber)
    .directive('numberOnly', NumberOnly)
    .directive('integerOnly', IntegerOnly)
    .directive('categoryAutoComplete', CategoryAutoComplete);    
})();
