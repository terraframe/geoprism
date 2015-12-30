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
  
  function LocalizationService() {
    var service = {};    
    service.parser = Globalize.numberParser();
    service.formatter = Globalize.numberFormatter();
    
    service.parseNumber = function(value) {
      if(value != null && value.length > 0) {            
        //convert data from view format to model format
        var number = service.parser( value );
        
        return number;
      }
          
      return null;
    }
    
    service.formatNumber = function(value) {
      if(value != null) {
        var number = value;
            
        if(typeof number === 'string') {
          number = parseInt(value);
        }
            
        //convert data from model format to view format
        return service.formatter(number);
      }
            
      return null;
    }

    return service;
  }
  
  angular.module("localization-service", []);
  angular.module("localization-service")
    .factory('localizationService', LocalizationService)
})();