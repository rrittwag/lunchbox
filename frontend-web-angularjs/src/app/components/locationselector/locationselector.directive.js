(function() {
  'use strict';

  angular
    .module('lunchboxWebapp')
    .directive('locationSelector', locationSelector);

  function locationSelector() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: 'app/components/locationselector/locationselector.html',
      controller: 'LocationSelectorController',
      controllerAs: 'locationSelector'
    };
  }

})();
