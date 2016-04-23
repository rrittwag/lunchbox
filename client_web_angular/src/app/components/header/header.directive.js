(function() {
  'use strict';

  angular.module('lunchboxWebapp')
    .directive('header', header);

  function header() {
    return {
      restrict: 'A',
      replace: true,
      templateUrl: 'app/components/header/header.html',
      controller: 'HeaderController'
    };
  }

})();
