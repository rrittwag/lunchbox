(function() {
  'use strict';

  angular
    .module('lunchboxWebapp')
    .controller('HeaderController', HeaderController);

  function HeaderController($route, $location, $rootScope, LunchModel) {
    var vm = this;

    vm.routes = [];
    vm.model = LunchModel;

    var init = function () {
      // Initialisierung der Route
      angular.forEach($route.routes, function (route, path) {
        if (route.navbarName) {
          vm.routes.push({
            path: path,
            name: route.navbarName
          });
        }
      });
      // Initialisierung der Location
      if (!LunchModel.location) {
        if (LunchModel.locations.length > 0) {
          LunchModel.setLocation(LunchModel.locations[0]);
        }
      }
    };
    init();


    // Aktiv-Status für Tabs abfragen
    vm.activeRoute = function (route) {
      return route.path === $location.path();
  //    return $location.path().indexOf(route.path) === 0;
    };

    // Setzen der Location aus Dropdown
    vm.selectLocation = function(location) {
      LunchModel.setLocation(location);
    };

  }

})();
