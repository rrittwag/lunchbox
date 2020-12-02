(function() {
  'use strict';

  angular
    .module('lunchboxWebapp')
    .config(appConfig)
    .config(storageConfig);

  function appConfig($logProvider, $locationProvider) {
    // Ab HTML5 sind semantische Adresspfade (ohne #) für SPAs möglich.
    // Angular unterstützt das, inklusive Fallback.
    $locationProvider.html5Mode(true);

    // Enable log
    $logProvider.debugEnabled(true);
  }

  function storageConfig(localStorageServiceProvider) {
    localStorageServiceProvider.setPrefix('lunchboxWebapp');
  }

})();
