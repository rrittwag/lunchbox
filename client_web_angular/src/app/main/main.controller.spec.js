'use strict';

describe('main controller', function(){
  var $controller; // $controller ist der Angular-Service, der Controller erzeugt
  var scope; // Scope für zu instanziierenden Controller

  var testProviders = [{id: 1, name: 'Anbieter 1', location: 'Neubrandenburg'}, {id: 2, name: 'Anbieter 2', location: 'Berlin'}];
  var testOffers = [{id: 1, name: 'Angebot 1', day: '2015-04-15', price: 550, provider: 1}, {id: 2, name: 'Angebot 2', day: '2015-04-15', price: 450, provider: 2}, {id: 3, name: 'Angebot 3', day: '2015-01-01', price: 450, provider: 1}];

  beforeEach(function() {
    module('lunchboxWebapp');
    inject(function(_$controller_) { $controller = _$controller_; });
    inject(function($rootScope) { scope = $rootScope.$new(); });

    // Custom Matcher, der angular.equals() nutzt, um Wrapper (z.B. Promise, Resource) auszuwerten
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



  describe('instantiation', function() {
    beforeEach(function() {
      // main controller mit entsprechenden Parametern erzeugen
      $controller('MainCtrl', {
        $scope: scope,
        // _: {}, // TODO: Underscore als Util einbinden? ECMA6, RequireJS?
        LunchProviderStore: { query: function() {} }, // query-Funktion wegmocken
        LunchOfferStore: { query: function() {} } // query-Funktion wegmocken
      });
    });

    it('should init day to today (in UTC)', function() {
      var now = new Date();
      expect(scope.day).toBeDefined();
      expect(scope.day.getTime()).toBe(Date.UTC(now.getFullYear(), now.getMonth(), now.getDate()));
    });

    it('should init location to Neubrandenburg', function() {
      expect(scope.location).toBe('Neubrandenburg');
    });

    it('should init status to LOADING', function() {
      expect(scope.isLoading()).toBeTruthy();
      expect(scope.isLoadFinished()).toBeFalsy();
      expect(scope.isLoadFinishedWithNoVisibleOffers()).toBeFalsy();
      expect(scope.isLoadFailed()).toBeFalsy();
    });

    it('should init visibleOffers to {}', function() {
      expect(scope.visibleOffers).toEqual({});
      expect(scope.hasVisibleOffers()).toBeFalsy();
    });

  });



  describe('query offers', function() {
    var $httpBackend;

    beforeEach(function() {
      inject(function(_$httpBackend_) {
        $httpBackend = _$httpBackend_;
        $httpBackend.expectGET('api/v1/lunchProvider').respond(testProviders);
        $httpBackend.expectGET('api/v1/lunchOffer').respond(testOffers);
      });

      $controller('MainCtrl', { $scope: scope });
    });

    it('should set status to LOAD_FINISHED if all queries done', function() {
      expect(scope.providers).toAngularEqual([]);
      expect(scope.offers).toAngularEqual([]);

      $httpBackend.flush();

      expect(scope.providers).toAngularEqual(testProviders);
      expect(scope.offers).toAngularEqual(testOffers);

      expect(scope.isLoading()).toBeFalsy();
      expect(scope.isLoadFinished()).toBeTruthy();
      expect(scope.isLoadFailed()).toBeFalsy();
    });
  });



  describe('refreshVisibleOffers', function() {
    var $httpBackend;

    beforeEach(function() {
      inject(function(_$httpBackend_) {
        $httpBackend = _$httpBackend_;
        $httpBackend.expectGET('api/v1/lunchProvider').respond(testProviders);
        $httpBackend.expectGET('api/v1/lunchOffer').respond(testOffers);
      });

      $controller('MainCtrl', { $scope: scope });
    });

    it('should resolve visible offers', function() {
      scope.day = new Date(Date.UTC(2015, 3, 15));
      scope.location = 'Neubrandenburg';
      expect(scope.visibleOffers).toAngularEqual({});

      $httpBackend.flush(); // refreshVisibleOffers wird implizit aufgerufen

      expect(scope.visibleOffers).toAngularEqual({
        1: [{id: 1, name: 'Angebot 1', day: '2015-04-15', price: 550, provider: 1}]
      });
    });
  });

});
