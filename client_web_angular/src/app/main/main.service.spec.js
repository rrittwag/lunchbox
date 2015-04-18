'use strict';

describe('main service', function() {
  var $httpBackend;

  var testProviders = [{id: 1, name: 'Anbieter 1', location: 'Neubrandenburg'}, {id: 2, name: 'Anbieter 2', location: 'Berlin'}];
  var testOffers = [{id: 1, name: 'Angebot 1', day: '2015-04-15', price: 550, provider: 1}, {id: 2, name: 'Angebot 2', day: '2015-04-15', price: 450, provider: 2}, {id: 3, name: 'Angebot 3', day: '2015-01-01', price: 450, provider: 1}];

  beforeEach(function() {
    module('lunchboxWebapp');
    inject(function(_$httpBackend_) { $httpBackend = _$httpBackend_; });

    // Custom Matcher, der beim Vergleich AngularJS-Wrapper kaschiert (z.B. Promise, Resource)
    jasmine.addMatchers({
      toAngularEqual: function() {
        return {
          compare : function(actual, expected) {
            return { pass : angular.equals(actual, expected) };
          }
        };
      }
    });
  });



  describe('LunchProviderStore', function() {
    var store;

    beforeEach(function() {
      $httpBackend.expectGET('api/v1/lunchProvider').respond(testProviders);
      inject(function(LunchProviderStore) { store = LunchProviderStore; });
    });

    it('should query for api v1 providers', function() {
      var queryResult = store.query();
      $httpBackend.flush();
      expect(queryResult).toAngularEqual(testProviders);
    });
  });



  describe('LunchOfferStore', function() {
    var store;

    beforeEach(function() {
      $httpBackend.expectGET('api/v1/lunchOffer').respond(testOffers);
      inject(function(LunchOfferStore) { store = LunchOfferStore; });
    });

    it('should query for api v1 offers', function() {
      var queryResult = store.query();
      $httpBackend.flush();
      expect(queryResult).toAngularEqual(testOffers);
    });
  });

});
