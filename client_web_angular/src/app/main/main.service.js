'use strict';

// App-Modul abrufen
var app = angular.module('lunchboxWebapp');

// LunchProvider-Service bereitstellen
app.service('LunchProviderStore', function($resource) {
    return $resource('api/v1/lunchProvider/:providerId', {providerId:'@id'}, {
      query: {method:'GET', isArray: true} // nur das GET über alle Werte wird benötigt
  });
});

// Factory zum Erzeugen des LunchOffer-Service bereitstellen
app.service('LunchOfferStore', function($resource) {
    return $resource('api/v1/lunchOffer/:offerId', {offerId:'@id'}, {
      query: {method:'GET', isArray: true} // nur das GET über alle Werte wird benötigt
  });
});
