'use strict';

var mod = angular.module('lunchboxWebapp');

mod.filter('offersForProvider', function () {
    return function(offers, provider) {
      function hasProvider(offer) {
        return offer.provider === provider.id;
      }
      return offers.filter(hasProvider);
    };
  });

mod.filter('formatEuro', function () {
    return function(cent) {
      return Math.floor(cent / 100) + ',' + (cent % 100) + ' €';
    };
  });
