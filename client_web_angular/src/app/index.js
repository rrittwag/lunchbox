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

app.config(function ($locationProvider) {
  // Ab HTML5 sind semantische Adresspfade (ohne #) für SPAs möglich.
  // Angular unterstützt das, inklusive Fallback.
  $locationProvider.html5Mode(true);
});
