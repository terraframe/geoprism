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
  function DataManagementController($scope) {
  }
  
  function DataManagementConfig($routeProvider) {
    $routeProvider
      
     // route for the icons page
     .when('/locations', {
       bundle : 'location.management',
       key : 'title',       
       templateUrl: com.runwaysdk.__applicationContextPath + '/partial/data/browser/location-management.jsp',
       controller : 'LocationController',
       controllerAs : 'ctrl'
     })
     
     // route for the dataset page and default page
     .otherwise({
       bundle : 'location.management',
       key : 'title',       
       templateUrl: com.runwaysdk.__applicationContextPath + '/partial/data/browser/location-management.jsp',
       controller : 'LocationController',
       controllerAs : 'ctrl'
     });    
  }
  
  angular.module("data-management", ["ngRoute", "localization-service", "webgl-map-service", 'location-management']);
  angular.module("data-management")
   .config(["$routeProvider", DataManagementConfig])
   .controller('DataManagementController', DataManagementController)
   .run(['$rootScope', '$route', 'localizationService', function($rootScope, $route, localizationService) {
     $rootScope.$on('$routeChangeSuccess', function() {
       document.title = localizationService.localize($route.current.bundle, $route.current.key, document.title);
     });
   }]);  
})();
