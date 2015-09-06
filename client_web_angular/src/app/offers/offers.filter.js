(function() {
  'use strict';

  // App-Modul abrufen ...
  var app = angular.module('lunchboxWebapp');

  function assert(condition, message) {
      if (!condition) {
        throw new Error(message || 'Assertion failed');
      }
  }

  // ... und Filter erzeugen
  app.filter('filterProvidersByLocation', function (_) {
    return function(providers, location) {
      assert(angular.isArray(providers));
      function isOfLocation(provider) {
        return provider.location === location.name;
      }
      return _.filter(providers, isOfLocation);
    };
  });

  app.filter('filterOffersByProviders', function (_) {
    return function(offers, providers) {
      assert(angular.isArray(offers));
      var providerIds = _.chain(providers)
                .map(function(prov) { return prov.id; })
                .uniq(false)
                .value();
      function isInProviders(offer) {
        return _.contains(providerIds, offer.provider);
      }
      return _.filter(offers, isInProviders);
    };
  });

  app.filter('filterOffersByProvider', function ($filter) {
    return function(offers, provider) {
      return $filter('filterOffersByProviders')(offers, [provider]);
    };
  });

  app.filter('filterOffersByDay', function (_) {
    return function(offers, day) {
      assert(angular.isArray(offers));
      function isOfDay(offer) {
        return Date.parse(offer.day) === day.getTime();
      }
      return _.filter(offers, isOfDay);
    };
  });

  app.filter('filterProvidersByOffers', function (_) {
    return function(providers, offers) {
      assert(angular.isArray(providers));
      var providerIds = _.chain(offers)
                .map(function(offer) { return offer.provider; })
                .uniq(false)
                .value();
      function isInProviders(provider) {
        return _.contains(providerIds, provider.id);
      }
      return _.filter(providers, isInProviders);
    };
  });

  app.filter('filterDaysInOffers', function(_) {
    return function(offers) {
      assert(angular.isArray(offers));
      return _.chain(offers)
        .map(function(offer) { return new Date(Date.parse(offer.day)); })
        .uniq(false, function(offer) { return offer.getTime(); })
        .sortBy(function(offer) { return offer.getTime(); })
        .value();
    };
  });

  app.filter('formatEuro', function () {
    return function(cent) {
      assert(typeof cent === 'number');
      var centString = ("0" + (cent % 100)).slice(-2);
      return Math.floor(cent / 100) + ',' + centString + ' €';
    };
  });

  app.filter('formatToWeekday', function () {
    return function(date) {
      assert(typeof date === 'object');
      assert(typeof date.getDay === 'function');
      // TODO: i18n
      var weekdays = ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag'];
      return weekdays[date.getDay()];
    };
  });

})();
