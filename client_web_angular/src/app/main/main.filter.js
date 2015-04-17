'use strict';

// App-Modul abrufen ...
var app = angular.module('lunchboxWebapp');

function assert(condition, message) {
    if (!condition) {
      throw new Error(message || 'Assertion failed');
    }
}

// ... und Filter erzeugen
app.filter('filterByLocation', function () {
  return function(providers, location) {
    assert(angular.isArray(providers));
    function isOfLocation(provider) {
      return provider.location === location;
    }
    return providers.filter(isOfLocation);
  };
});

app.filter('formatEuro', function () {
  return function(cent) {
    assert(typeof cent === 'number');
    return Math.floor(cent / 100) + ',' + (cent % 100) + ' €';
  };
});
