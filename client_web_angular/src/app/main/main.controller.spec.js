'use strict';

describe('main controller', function(){
  var $httpBackend; // fake http backend, initialized via initHttpBackend
  var $controller; // $controller ist der Angular-Service, der Controller erzeugt
  var scope; // Scope für zu instanziierenden Controller

  var testProviders = [{id: 1, name: 'Anbieter 1', location: 'Neubrandenburg'}, {id: 2, name: 'Anbieter 2', location: 'Berlin'}];
  var testOffers = [{id: 1, name: 'Angebot 1', day: '2015-04-15', price: 550, provider: 1}, {id: 2, name: 'Angebot 2', day: '2015-04-15', price: 450, provider: 2}, {id: 3, name: 'Angebot 3', day: '2015-01-01', price: 450, provider: 1}];

  var initHttpBackend = function(withHttpError) {
    inject(function(_$httpBackend_) {
      $httpBackend = _$httpBackend_;
      if (withHttpError) {
        $httpBackend.whenGET('api/v1/lunchProvider').respond(500, '');
      } else {
        $httpBackend.whenGET('api/v1/lunchProvider').respond(testProviders);
      }
      $httpBackend.whenGET('api/v1/lunchOffer').respond(testOffers);

      $controller('MainCtrl', { $scope: scope });
    });
  };



  beforeEach(function() {
    module('lunchboxWebapp');
    inject(function(_$controller_) { $controller = _$controller_; });
    inject(function($rootScope) { scope = $rootScope.$new(); });

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



  describe('instantiation', function() {
    beforeEach(function() {
      initHttpBackend();
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
    it('should set status to LOAD_FINISHED if all queries done', function() {
      initHttpBackend();

      $httpBackend.flush();

      expect(scope.providers).toAngularEqual(testProviders);
      expect(scope.offers).toAngularEqual(testOffers);

      expect(scope.isLoading()).toBeFalsy();
      expect(scope.isLoadFinished()).toBeTruthy();
      expect(scope.isLoadFailed()).toBeFalsy();
    });

    it('should set status to LOADING if at least one query loading', function() {
      initHttpBackend();

      $httpBackend.flush(1);

      expect(scope.isLoading()).toBeTruthy();
      expect(scope.isLoadFinished()).toBeFalsy();
      expect(scope.isLoadFailed()).toBeFalsy();
    });

    it('should set status to LOAD_FAILED if one query fails', function() {
      initHttpBackend(true);

      $httpBackend.flush();

      expect(scope.isLoading()).toBeFalsy();
      expect(scope.isLoadFinished()).toBeFalsy();
      expect(scope.isLoadFailed()).toBeTruthy();
    });
  });



  describe('refreshVisibleOffers', function() {
    it('should resolve visible offers', function() {
      initHttpBackend();
      scope.day = new Date(Date.UTC(2015, 3, 15));
      scope.location = 'Neubrandenburg';

      $httpBackend.flush(); // refreshVisibleOffers wird implizit aufgerufen

      expect(scope.visibleOffers).toAngularEqual({
        1: [{id: 1, name: 'Angebot 1', day: '2015-04-15', price: 550, provider: 1}]
      });
      expect(scope.hasVisibleOffers()).toBeTruthy();
      expect(scope.isLoadFinishedWithNoVisibleOffers()).toBeFalsy();
    });

    it('should not resolve visible offers if not matches date', function() {
      initHttpBackend();
      scope.day = new Date(Date.UTC(2000, 1, 1));
      scope.location = 'Neubrandenburg';

      $httpBackend.flush(); // refreshVisibleOffers wird implizit aufgerufen

      expect(scope.visibleOffers).toAngularEqual({});
      expect(scope.hasVisibleOffers()).toBeFalsy();
      expect(scope.isLoadFinishedWithNoVisibleOffers()).toBeTruthy();
    });

    it('should not resolve visible offers if not matches location', function() {
      initHttpBackend();
      scope.day = new Date(Date.UTC(2015, 3, 15));
      scope.location = 'New York';

      $httpBackend.flush(); // refreshVisibleOffers wird implizit aufgerufen

      expect(scope.visibleOffers).toAngularEqual({});
      expect(scope.hasVisibleOffers()).toBeFalsy();
      expect(scope.isLoadFinishedWithNoVisibleOffers()).toBeTruthy();
    });

    it('should not resolve visible offers if loading failed', function() {
      initHttpBackend(true);
      scope.day = new Date(Date.UTC(2015, 3, 15));
      scope.location = 'Neubrandenburg';

      $httpBackend.flush(); // refreshVisibleOffers wird implizit aufgerufen

      expect(scope.visibleOffers).toAngularEqual({});
      expect(scope.hasVisibleOffers()).toBeFalsy();
      expect(scope.isLoadFinishedWithNoVisibleOffers()).toBeFalsy();
    });
  });

});
