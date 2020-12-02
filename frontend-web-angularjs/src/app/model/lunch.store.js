(function() {
  'use strict';

  // App-Modul abrufen
  angular
    .module('lunchboxWebapp')
    .service('LunchProviderStore', LunchProviderStore)
    .service('LunchOfferStore', LunchOfferStore);

  // Service zum Abrufen der LunchProvider bereitstellen
  function LunchProviderStore($resource) {
      return $resource('api/v1/lunchProvider/:providerId', {providerId:'@id'}, {
        query: {method:'GET', isArray: true} // nur das GET über alle Werte wird benötigt
    });
  }

  // Service zum Abrufen der LunchOffer bereitstellen
  function LunchOfferStore($resource) {
      return $resource('api/v1/lunchOffer/:offerId', {offerId:'@id'}, {
        query: {method:'GET', isArray: true} // nur das GET über alle Werte wird benötigt
    });
  }

})();
