(function() {
  'use strict';

  angular
    .module('lunchboxWebapp')
    .config(config);

  function config($logProvider, $locationProvider) {
    // Ab HTML5 sind semantische Adresspfade (ohne #) für SPAs möglich.
    // Angular unterstützt das, inklusive Fallback.
    $locationProvider.html5Mode(true);

    // Enable log
    $logProvider.debugEnabled(true);
  }

})();
