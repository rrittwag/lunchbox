(function() {
  'use strict';

  describe('offers filter', function() {
    var $filter;
    var locationNB = {name: 'Neubrandenburg'};
    var provider1_NB = {id: 1, name: 'Anbieter 1', location: 'Neubrandenburg'};
    var provider2_NB = {id: 2, name: 'Anbieter 2', location: 'Neubrandenburg'};
    var provider3_B = {id: 3, name: 'Anbieter 3', location: 'Berlin'};
    var testProviders = [provider1_NB, provider2_NB, provider3_B];
    var offer1_P1 = {id: 1, name: 'Angebot 1', day: '2015-04-15', price: 550, provider: 1};
    var offer2_P2 = {id: 2, name: 'Angebot 2', day: '2015-04-15', price: 450, provider: 2};
    var offer3_P1 = {id: 3, name: 'Angebot 3', day: '2015-01-01', price: 450, provider: 1};
    var testOffers = [offer1_P1, offer2_P2, offer3_P1];

    beforeEach(function() {
      module('lunchboxWebapp');
      inject(function(_$filter_) { $filter = _$filter_; });
    });



    describe('filterProvidersByLocation', function() {
      var testFilter;
      beforeEach(function() {
        testFilter = $filter('filterProvidersByLocation');
      });

      it('returns provider in location', function() {
        expect(testFilter(testProviders, locationNB)).toEqual([provider1_NB, provider2_NB]);
      });

      it('returns no providers when input no providers', function() {
        expect(testFilter([], locationNB)).toEqual([]);
      });

      it('returns no providers when input undefined', function() {
        expect(function() { testFilter(null, locationNB); }).toThrowError(Error);
      });

      it('returns no providers when location not matching', function() {
        expect(testFilter(testProviders, {name: 'New York'})).toEqual([]);
      });

      it('returns all providers when no location', function() {
        expect(testFilter(testProviders, null)).toEqual(testProviders);
      });
    });



    describe('filterOffersByProviders', function() {
      var testFilter;
      beforeEach(function() {
        testFilter = $filter('filterOffersByProviders');
      });

      it('returns offers for 2 providers', function() {
        expect(testFilter(testOffers, [provider1_NB, provider3_B])).toEqual([offer1_P1, offer3_P1]);
      });

      it('returns offers for 1 provider', function() {
        expect(testFilter(testOffers, [provider1_NB])).toEqual([offer1_P1, offer3_P1]);
      });

      it('returns nothing for []', function() {
        expect(testFilter(testOffers, [])).toEqual([]);
      });
    });



    describe('filterOffersByProvider', function() {
      var testFilter;
      beforeEach(function() {
        testFilter = $filter('filterOffersByProvider');
      });

      it('returns offers for provider', function() {
        expect(testFilter(testOffers, provider1_NB)).toEqual([offer1_P1, offer3_P1]);
      });

      it('returns nothing for null', function() {
        expect(testFilter(testOffers, null)).toEqual([]);
      });
    });



    describe('filterOffersByDay', function() {
      var testFilter;
      beforeEach(function() {
        testFilter = $filter('filterOffersByDay');
      });

      it('returns offers for 2015-04-15', function() {
        var date = new Date(Date.UTC(2015, 3, 15));
        expect(testFilter(testOffers, date)).toEqual([offer1_P1, offer2_P2]);
      });

      it('returns no offers for 2015-04-16', function() {
        var date = new Date(Date.UTC(2015, 3, 16));
        expect(testFilter(testOffers, date)).toEqual([]);
      });

      it('returns nothing for null', function() {
        expect(testFilter(testOffers, null)).toEqual([]);
      });
    });



    describe('filterProvidersByOffers', function() {
      var testFilter;
      beforeEach(function() {
        testFilter = $filter('filterProvidersByOffers');
      });

      it('returns providers for 2 offers', function() {
        expect(testFilter(testProviders, [offer1_P1, offer3_P1])).toEqual([provider1_NB]);
      });

      it('returns providers for 1 offer', function() {
        expect(testFilter(testProviders, [offer1_P1])).toEqual([provider1_NB]);
      });

      it('returns nothing for []', function() {
        expect(testFilter(testProviders, [])).toEqual([]);
      });
    });



    describe('filterDaysInOffers', function() {
      var testFilter;
      beforeEach(function() {
        testFilter = $filter('filterDaysInOffers');
      });

      it('should return unique dates', function() {
        var yesterday = new Date(Date.UTC(2015, 1, 4));
        var today = new Date(Date.UTC(2015, 1, 5));
        var tomorrow = new Date(Date.UTC(2015, 1, 6));
        var offers = [{id: 1, name: 'Angebot 1', day: today, price: 550, provider: 1},
                      {id: 2, name: 'Angebot 2', day: yesterday, price: 550, provider: 1},
                      {id: 3, name: 'Angebot 3', day: today, price: 550, provider: 1},
                      {id: 4, name: 'Angebot 4', day: tomorrow, price: 550, provider: 1},
                      {id: 5, name: 'Angebot 5', day: tomorrow, price: 550, provider: 1},
                      {id: 6, name: 'Angebot 6', day: today, price: 550, provider: 1}];

        var result = testFilter(offers);

        expect(result.length).toBe(3);
        expect(result).toContain(yesterday);
        expect(result).toContain(today);
        expect(result).toContain(tomorrow);
      });

      it('should return sorted dates', function() {
        var yesterday = new Date(Date.UTC(2015, 1, 4));
        var today = new Date(Date.UTC(2015, 1, 5));
        var tomorrow = new Date(Date.UTC(2015, 1, 6));
        var offers = [{id: 1, name: 'Angebot 1', day: today, price: 550, provider: 1},
                      {id: 2, name: 'Angebot 2', day: tomorrow, price: 550, provider: 1},
                      {id: 3, name: 'Angebot 3', day: yesterday, price: 550, provider: 1}];

        var result = testFilter(offers);

        expect(result).toEqual([yesterday, today, tomorrow]);
      });

      it('should be empty when no offers exist', function() {
        var offers = [];

        var result = testFilter(offers);

        expect(result.length).toBe(0);
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

      it('returns "5,00 €" when input is 500', function() {
        expect(formatEuro(500)).toEqual('5,00 €');
      });

      it('returns "20,30 €" when input is 2030', function() {
        expect(formatEuro(2030)).toEqual('20,30 €');
      });

      it('returns Error when input is undefined', function() {
        expect(function() { formatEuro(undefined); }).toThrowError(Error);
      });
    });



    describe('formatToWeekday', function() {
      var formatToWeekday;
      beforeEach(function() {
        formatToWeekday = $filter('formatToWeekday');
      });

      it('returns Error when date undefined', function() {
        expect(formatToWeekday(undefined)).toEqual('');
      });

      it('returns Error when date is some object', function() {
        expect(function() { formatToWeekday({}); }).toThrowError(Error);
      });

      it('returns "Sonntag" for 07.06.2015', function() {
        expect(formatToWeekday(new Date(Date.UTC(2015, 5, 7)))).toEqual('Sonntag');
      });

      it('returns "Montag" for 08.06.2015', function() {
        expect(formatToWeekday(new Date(Date.UTC(2015, 5, 8)))).toEqual('Montag');
      });

      it('returns "Samstag" for 13.06.2015', function() {
        expect(formatToWeekday(new Date(Date.UTC(2015, 5, 13)))).toEqual('Samstag');
      });
    });

  });

})();
