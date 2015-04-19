'use strict';

var app = angular.module('lunchboxWebapp');

app.directive('header', function() {
  return {
    restrict: 'A',
    replace: true,
    templateUrl: 'components/header/header.html',
    controller: 'HeaderCtrl'
  };
});
