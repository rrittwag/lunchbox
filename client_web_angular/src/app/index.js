'use strict';

angular.module('lunchboxWebapp', ['ngResource', 'ngRoute', 'ui.bootstrap', 'underscore'])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'app/main/main.html',
        controller: 'MainCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  })
;
