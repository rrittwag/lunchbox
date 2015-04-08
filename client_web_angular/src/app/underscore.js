'use strict';

var underscore = angular.module('underscore', []);

underscore.factory('_', function($window) {
  return $window._;
});
