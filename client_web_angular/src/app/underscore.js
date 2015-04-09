'use strict';

// Angular-Modul für Bibliothek underscore.js bereitstellen
var underscore = angular.module('underscore', []);

underscore.factory('_', function($window) {
  return $window._;
});
