'use strict';

// App-Modul abrufen ...
var app = angular.module('lunchboxWebapp');

// ... und Filter erzeugen
app.filter('filterByProvider', function () {
  return function(offers, provider) {
    function isOfProvider(offer) {
      return offer.provider === provider.id;
    }
    return offers.filter(isOfProvider);
  };
});

app.filter('filterByDay', function () {
  return function(offers, day) {
    function isOfDay(offer) {
      return Date.parse(offer.day + 'T00:00:00.000Z') === day;
    }
    return offers.filter(isOfDay);
  };
});

app.filter('filterByLocation', function () {
  return function(providers, location) {
    function isOfLocation(provider) {
      return provider.location === location;
    }
    return providers.filter(isOfLocation);
  };
});

app.filter('formatEuro', function () {
  return function(cent) {
    return Math.floor(cent / 100) + ',' + (cent % 100) + ' €';
  };
});
