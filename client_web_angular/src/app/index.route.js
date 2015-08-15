(function() {
  'use strict';

  angular
    .module('lunchboxWebapp')
    .config(routeConfig);

  // Route-Konfiguration
  function routeConfig($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'app/main/main.html',
        controller: 'MainCtrl',
        navbarName: 'Mittagsangebote'
      })
      .when('/about', {
        templateUrl: 'app/about/about.html',
  //      controller: 'MainCtrl'
        navbarName: 'Info'
      })
      .otherwise({
        redirectTo: '/'
      });
  }

})();
