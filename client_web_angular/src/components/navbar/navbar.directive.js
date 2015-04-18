'use strict';

var app = angular.module('lunchboxWebapp');

app.directive('navbar', function() {
  return {
    restrict: 'E',
    replace: true,
    templateUrl: 'components/navbar/navbar.html',
    controller: 'NavbarCtrl'
  };
});
