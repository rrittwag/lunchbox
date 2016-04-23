(function() {
  'use strict';

  // App-Modul abrufen ...
  angular
    .module('lunchboxWebapp')
    .filter('filterProvidersByLocation', FilterProvidersByLocation)
    .filter('filterOffersByProviders', FilterOffersByProviders)
    .filter('filterOffersByProvider', FilterOffersByProvider)
    .filter('filterOffersByDay', FilterOffersByDay)
    .filter('filterProvidersByOffers', FilterProvidersByOffers)
    .filter('filterDaysInOffers', FilterDaysInOffers)
    .filter('formatEuro', FormatEuro)
    .filter('formatToWeekday', FormatToWeekday);

  function assert(condition, message) {
      if (!condition) {
        throw new Error(message || 'Assertion failed');
      }
  }

  // ... und Filter erzeugen
  function FilterProvidersByLocation(_) {
    return function(providers, location) {
      assert(angular.isArray(providers));
      function isOfLocation(provider) {
        return !location || provider.location === location.name;
      }
      return _.filter(providers, isOfLocation);
    };
  }

  function FilterOffersByProviders(_) {
    return function(offers, providers) {
      assert(angular.isArray(offers));
      var providerIds = _.chain(providers)
                .map(function(prov) { return prov.id; })
                .value();
      function isInProviders(offer) {
        return _.contains(providerIds, offer.provider);
      }
      return _.filter(offers, isInProviders);
    };
  }

  function FilterOffersByProvider($filter) {
    return function(offers, provider) {
      var providerArray = [provider];
      if (!provider) { providerArray = []; }
      return $filter('filterOffersByProviders')(offers, providerArray);
    };
  }

  function FilterOffersByDay(_) {
    return function(offers, day) {
      assert(angular.isArray(offers));
      function isOfDay(offer) {
        return day && Date.parse(offer.day) === day.getTime();
      }
      return _.filter(offers, isOfDay);
    };
  }

  function FilterProvidersByOffers(_) {
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
  }

  function FilterDaysInOffers(_) {
    return function(offers) {
      assert(angular.isArray(offers));
      return _.chain(offers)
        .map(function(offer) { return new Date(Date.parse(offer.day)); })
        .uniq(false, function(offer) { return offer.getTime(); })
        .sortBy(function(offer) { return offer.getTime(); })
        .value();
    };
  }

  function FormatEuro() {
    return function(cent) {
      assert(angular.isNumber(cent));
      var centString = ("0" + (cent % 100)).slice(-2);
      return Math.floor(cent / 100) + ',' + centString + ' €';
    };
  }

  function FormatToWeekday() {
    return function(date) {
      if ( !date ) { return ''; }
      assert(angular.isObject(date));
      assert(angular.isFunction(date.getDay));
      // TODO: i18n
      var weekdays = ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag'];
      return weekdays[date.getDay()];
    };
  }

})();
