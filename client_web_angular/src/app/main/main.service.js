'use strict';

// App-Modul abrufen
var app = angular.module('lunchboxWebapp');

// Service zum Abrufen der LunchProvider bereitstellen
app.service('LunchProviderStore', function($resource) {
    return $resource('api/v1/lunchProvider/:providerId', {providerId:'@id'}, {
      query: {method:'GET', isArray: true} // nur das GET über alle Werte wird benötigt
  });
});

// Service zum Abrufen der LunchOffer bereitstellen
app.service('LunchOfferStore', function($resource) {
    return $resource('api/v1/lunchOffer/:offerId', {offerId:'@id'}, {
      query: {method:'GET', isArray: true} // nur das GET über alle Werte wird benötigt
  });
});
