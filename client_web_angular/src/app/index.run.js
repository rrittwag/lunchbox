(function() {
  'use strict';

  angular
    .module('lunchboxWebapp')
    .run(runBlock);

  /** @ngInject */
  function runBlock($log) {

    $log.debug('runBlock end');
  }

})();
