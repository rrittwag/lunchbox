(function() {
  'use strict';

  angular
    .module('lunchboxWebapp')
    .controller('HeaderController', HeaderController);

  function HeaderController($scope, $route, $location, $rootScope, LunchModel) {
    $scope.header = {
      routes: []
    };
    $scope.model = LunchModel;

    var init = function () {
      // Initialisierung der Route
      angular.forEach($route.routes, function (route, path) {
        if (route.navbarName) {
          $scope.header.routes.push({
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
    $scope.header.activeRoute = function (route) {
      return route.path === $location.path();
  //    return $location.path().indexOf(route.path) === 0;
    };

    // Setzen der Location aus Dropdown
    $scope.header.selectLocation = function(location) {
      LunchModel.setLocation(location);
    };

  }

})();
