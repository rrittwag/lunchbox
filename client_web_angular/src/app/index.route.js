(function() {
  'use strict';

  angular
    .module('lunchboxWebapp')
    .config(routeConfig);

  // Route-Konfiguration
  function routeConfig($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'app/offers/offers.html',
        controller: 'OffersCtrl',
        navbarName: 'Mittagsangebote'
      })
      .when('/about', {
        templateUrl: 'app/about/about.html',
  //      controller: 'OffersCtrl'
        navbarName: 'Info'
      })
      .otherwise({
        redirectTo: '/'
      });
  }

})();
