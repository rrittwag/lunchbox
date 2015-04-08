'use strict';

var mod = angular.module('lunchboxWebapp');

mod.filter('filterByProvider', function () {
    return function(offers, provider) {
      function isOfProvider(offer) {
        return offer.provider === provider.id;
      }
      return offers.filter(isOfProvider);
    };
  });

mod.filter('filterByDay', function () {
    return function(offers, day) {
      function isOfDay(offer) {
        return Date.parse(offer.day + 'T00:00:00.000Z') === day;
      }
      return offers.filter(isOfDay);
    };
  });

mod.filter('filterByLocation', function () {
    return function(providers, location) {
      function isOfLocation(provider) {
        return provider.location === location;
      }
      return providers.filter(isOfLocation);
    };
  });

mod.filter('formatEuro', function () {
    return function(cent) {
      return Math.floor(cent / 100) + ',' + (cent % 100) + ' €';
    };
  });
