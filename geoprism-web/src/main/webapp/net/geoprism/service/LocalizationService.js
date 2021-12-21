/*
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Geoprism(tm).
 *
 * Geoprism(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Geoprism(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.
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
          if(number.length > 0 && Number(number)) {
            number = Number(value);        	  
          }
          else {
            return "";
          }
        }
            
        //convert data from model format to view format
        return service.formatter(number);
      }
            
      return null;
    }
    
    service.localize = function(bundle, key) {
      return com.runwaysdk.Localize.localize(bundle, key);
    }
    
    service.get = function(key) {
      return com.runwaysdk.Localize.get(key);
    }
    

    return service;
  }
  
  angular.module("localization-service", []);
  angular.module("localization-service")
    .factory('localizationService', LocalizationService)
})();