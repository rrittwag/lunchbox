'use strict';

// Angular-Modul für WebApp beschreiben (samt Abhängigkeiten)
var app = angular.module('lunchboxWebapp', ['ngResource', 'ngRoute', 'ui.bootstrap', 'underscore']);

// Route-Konfiguration
app.config(function ($routeProvider) {
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
});
