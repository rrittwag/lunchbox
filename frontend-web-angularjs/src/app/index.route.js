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
        controller: 'OffersController',
        controllerAs: 'offers',
        navbarName: 'Mittagsangebote'
      })
      .when('/settings', {
        templateUrl: 'app/settings/settings.html',
        controller: 'LocationSelectorController',
        controllerAs: 'settings',
        navbarName: 'Einstellungen'
      })
      .when('/about', {
        templateUrl: 'app/about/about.html',
        controller: 'AboutController',
        controllerAs: 'about',
        navbarName: 'Info'
      })
      .otherwise({
        redirectTo: '/'
      });
  }

})();
