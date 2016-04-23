(function() {
  'use strict';

  angular
    .module('lunchboxWebapp')
    .controller('DaySelectorController', DaySelectorController);

  function DaySelectorController($scope, $filter, _) {
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
      return angular.isDefined($scope.prevDay());
    };

    $scope.hasNextDay = function() {
      return angular.isDefined($scope.nextDay());
    };
  }

})();
