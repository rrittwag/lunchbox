(function() {
  'use strict';

  var app = angular.module('lunchboxWebapp');

  app.controller('DaySelectorCtrl', function ($scope, $filter, _) {
    $scope.prevDay = function() {
      return _.chain($scope.days)
          .filter(function(day) { return day < $scope.selectedDay; })
          .last().value();
    };

    $scope.nextDay = function() {
      return _.chain($scope.days)
          .filter(function(day) { return day > $scope.selectedDay; })
          .first().value();
    };

    $scope.goPrevDay = function() {
      $scope.selectedDay = $scope.prevDay();
    };

    $scope.goNextDay = function() {
      $scope.selectedDay = $scope.nextDay();
    };

    $scope.hasPrevDay = function() {
      return $scope.prevDay() !== undefined;
    };

    $scope.hasNextDay = function() {
      return $scope.nextDay() !== undefined;
    };
  });

})();
