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
    
    // Set default value and label attributes
    if($scope.value == null) {
      $scope.value = 'value';  
    }
    
    if($scope.label == null) {
      $scope.label = 'label';
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
        
        // Clean up the auto-complete widget on destroy
        scope.$on("$destroy",function handleDestroyEvent() {
          $(element).autocomplete( "destroy" );
        });        
      }
    };
  };
  
  
  function ShowOnReady() {
    return {
      restrict: "A",
      link: function (scope, element, attr, ngModel) {
        scope.$on('angular-ready', function(event, data) {
          $(element).show();
          
          event.stopPropagation();
        });      
      }
    };
  };
  
  function FireOnReady($timeout) {
    return {
      restrict: "A",
      link: function (scope, element, attr, ngModel) {
        element.ready(function(){
          $timeout(function(){
            scope.$emit('angular-ready', {});            
          }, 0);          
        });
      }
    };
  };
  
  
  function StyledColorPickerController() {
    var controller = this;

    controller.generateId = function() {
      var S4 = function() {
        return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
      };
        
      return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
    }
    
    /**
     * Converts rgb or rgba to hex equivilent.
     * 
     * @param rgb or rgba 
     */
    controller.rgb2hex = function(rgb) {
      if(rgb != null) {
             
        if (/^#[0-9A-F]{6}$/i.test(rgb)){
          return rgb;
        }

        var rgbMatch = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);        
        if(rgbMatch){
          function hex(x) {
            return ("0" + parseInt(x).toString(16)).slice(-2);
          }
            
          return "#" + hex(rgbMatch[1]) + hex(rgbMatch[2]) + hex(rgbMatch[3]);
        }
                
        var rgbaMatch = rgb.match(/^rgba?[\s+]?\([\s+]?(\d+)[\s+]?,[\s+]?(\d+)[\s+]?,[\s+]?(\d+)[\s+]?/i);
        if(rgbaMatch){
          return (rgbaMatch && rgbaMatch.length === 4) ? "#" +
            ("0" + parseInt(rgbaMatch[1],10).toString(16)).slice(-2) +
            ("0" + parseInt(rgbaMatch[2],10).toString(16)).slice(-2) +
            ("0" + parseInt(rgbaMatch[3],10).toString(16)).slice(-2) : '';
        }
      }
    };  
  }
  
  function StyledColorPicker($timeout) {
    return {
      restrict: 'E',
      replace: true,
      templateUrl : '/partial/inputs/styled-color-picker.jsp',
      scope: {
        model:'=',
        scroll:'@',
      },
      controller : StyledColorPickerController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
    	scope.id = ctrl.generateId();
        
        element.ready(function(){
          $timeout(function(){
            // Hook up the color picker
            $(element).colpick({
              submit:0,  // removes the "ok" button which allows verification of selection and memory for last color
              onShow:function(colPickObj){
                var that = this;
                  
                // Set the current value of the color picker
                $(this).colpickSetColor(scope.model,false);
                  
                $(scope.scroll).scroll(function(){  
                  var colorPicker = $(".colpick.colpick_full.colpick_full_ns:visible");
                  var colPick = $(that);
                  var diff = colPick.offset().top + colPick.height() + 2; 
                  var diffStr = diff.toString() + "px";
                    
                  colorPicker.css({ top: diffStr });
                });
              },
              onChange: function(hsb,hex,rgb,el,bySetColor) {
                var hexStr = '#'+hex;
                $(el).find(".ico").css('background', hexStr);
              },
              onHide:function(el) {              
                var rgb = $(element).find(".ico").css('background-color');
                var value = ctrl.rgb2hex(rgb);
                    
                scope.model = value;                        
                scope.$apply();
              }
            });
          }, 0);       
        });
        
        scope.$on("$destroy",function handleDestroyEvent() {
          $(element).colpickDestroy();
        });
      }
    }    
  }
  
  
  angular.module("styled-inputs", ["localization-service"]);
  angular.module("styled-inputs")
    .directive('styledCheckBox', StyledCheckBox)
    .directive('styledBasicSelect', StyledBasicSelect)
    .directive('styledSelect', StyledSelect)
    .directive('styledColorPicker', StyledColorPicker)
    .directive('convertToPercent', ConvertToPercent)
    .directive('convertToNumber', ConvertToNumber)
    .directive('showOnReady', ShowOnReady)
    .directive('fireOnReady', FireOnReady)
    .directive('numberOnly', NumberOnly)
    .directive('integerOnly', IntegerOnly)
    .directive('categoryAutoComplete', CategoryAutoComplete);    
})();
