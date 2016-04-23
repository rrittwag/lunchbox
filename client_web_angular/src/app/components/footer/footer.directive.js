(function() {
  'use strict';

  angular
    .module('lunchboxWebapp')
    .directive('footer', footer);

  function footer() {
    return {
      restrict: 'A',
      replace: true,
      templateUrl: 'app/components/footer/footer.html'
    };
  }

})();
