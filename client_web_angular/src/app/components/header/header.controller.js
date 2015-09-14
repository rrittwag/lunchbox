(function() {
  'use strict';

  var app = angular.module('lunchboxWebapp');

  app.controller('HeaderCtrl', function ($scope, $route, $location, $rootScope, LunchModel, Settings) {
    // Initialisierung
    var initialLocation = function() {
      if (!Settings.location) {
        if (LunchModel.locations.length > 0) {
          Settings.setLocation(LunchModel.locations[0]);
        }
      }
      return Settings.location;
    };
    var initialRoutes = function() {
      var result = [];
      angular.forEach($route.routes, function (route, path) {
        if (route.navbarName) {
          result.push({
            path: path,
            name: route.navbarName
          });
        }
      });
      return result;
    };

    $scope.header = {
      routes: initialRoutes(),
      selectedLocation: initialLocation(),
    };
    $scope.model = LunchModel;


    // Aktiv-Status für Tabs abfragen
    $scope.header.activeRoute = function (route) {
      return route.path === $location.path();
  //    return $location.path().indexOf(route.path) === 0;
    };

    // Setzen der Location aus Dropdown
    $scope.header.selectLocation = function(location) {
      $scope.header.selectedLocation = location;
      Settings.setLocation(location);
    };
  });

})();
