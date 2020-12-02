(function() {
  'use strict';

  angular
    .module('lunchboxWebapp')
    .directive('daySelector', daySelector);

  function daySelector() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: 'app/components/dayselector/dayselector.html',
      controller: 'DaySelectorController',
      scope: { // eigenständiger (isolated) Scope
            selectedDay: '=', // 2-way-binding scope-Wert via HTML-Attribut "selected-day"
            days: '=' // Input-Parameter via HTML-Attribut "days" hereingeben
          }
      };
  }

})();
