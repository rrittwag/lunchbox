(function() {
  'use strict';

  var app = angular.module('lunchboxWebapp');

  app.controller('HeaderCtrl', function ($scope, $route, $location) {
    $scope.routes = [];
    // TODO: Logik auslagern in Service?
    angular.forEach($route.routes, function (route, path) {
      if (route.navbarName) {
        $scope.routes.push({
          path: path,
          name: route.navbarName
        });
      }
    });

    $scope.activeRoute = function (route) {
      return route.path === $location.path();
  //    return $location.path().indexOf(route.path) === 0;
    };
  });

})();
