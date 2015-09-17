(function() {
  'use strict';

  var app = angular.module('lunchboxWebapp');

  app.directive('daySelector', function() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: 'app/components/dayselector/dayselector.html',
      controller: 'DaySelectorCtrl',
      scope: { // eigenständiger (isolated) Scope
            selectedDay: '=', // 2-way-binding scope-Wert via HTML-Attribut "selected-day"
            days: '=' // Input-Parameter via HTML-Attribut "days" hereingeben
          }
      };
  });

})();
