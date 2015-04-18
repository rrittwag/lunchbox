'use strict';

describe('main filter', function() {
  var $filter;
  var providerNB1 = {id: 1, name: 'Anbieter 1', location: 'Neubrandenburg'};
  var providerNB2 = {id: 2, name: 'Anbieter 2', location: 'Neubrandenburg'};
  var testProviders = [providerNB1, providerNB2, {id: 3, name: 'Anbieter 3', location: 'Berlin'}];

  beforeEach(function() {
    module('lunchboxWebapp');
    inject(function(_$filter_) { $filter = _$filter_; });
  });



  describe('filterByLocation', function() {
    var filterByLocation;
    beforeEach(function() {
      filterByLocation = $filter('filterByLocation');
    });

    it('returns provider in location', function() {
      expect(filterByLocation(testProviders, 'Neubrandenburg')).toEqual([providerNB1, providerNB2]);
    });

    it('returns no providers when input no providers', function() {
      expect(filterByLocation([], 'Neubrandenburg')).toEqual([]);
    });

    it('returns no providers when input undefined', function() {
      expect(function() { filterByLocation(undefined, 'Neubrandenburg'); }).toThrowError(Error);
    });

    it('returns no providers when location not matching', function() {
      expect(filterByLocation(testProviders, 'New York')).toEqual([]);
    });
  });



  describe('formatEuro', function() {
    var formatEuro;
    beforeEach(function() {
      formatEuro = $filter('formatEuro');
    });

    it('returns "1,23 €" when input is 123', function() {
      expect(formatEuro(123)).toEqual('1,23 €');
    });

    it('returns "20,30 €" when input is 2030', function() {
      expect(formatEuro(2030)).toEqual('20,30 €');
    });

    it('returns Error when input is undefined', function() {
      expect(function() { formatEuro(undefined); }).toThrowError(Error);
    });
  });

});
