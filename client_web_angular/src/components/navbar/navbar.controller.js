'use strict';

var app = angular.module('lunchboxWebapp');

app.controller('NavbarCtrl', function ($scope) {
  $scope.date = new Date();
});
