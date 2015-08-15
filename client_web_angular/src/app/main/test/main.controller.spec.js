(function() {
  'use strict';

  describe('main controller', function(){
    var $httpBackend; // fake http backend, initialized via initHttpBackend
    var scope; // Scope für zu instanziierenden Controller

    var testProviders = [{id: 1, name: 'Anbieter 1', location: 'Neubrandenburg'}, {id: 2, name: 'Anbieter 2', location: 'Berlin'}];
    var testOffers = [{id: 1, name: 'Angebot 1', day: '2015-04-15', price: 550, provider: 1}, {id: 2, name: 'Angebot 2', day: '2015-04-15', price: 450, provider: 2}, {id: 3, name: 'Angebot 3', day: '2015-01-01', price: 450, provider: 1}];

    var initHttpBackend = function(withHttpError, offers, providers) {
      if (!offers) { offers = testOffers; }
      if (!providers) { providers = testProviders; }

      inject(function(_$httpBackend_) {
        $httpBackend = _$httpBackend_;
        if (withHttpError) {
          $httpBackend.whenGET('api/v1/lunchProvider').respond(500, '');
        } else {
          $httpBackend.whenGET('api/v1/lunchProvider').respond(providers);
        }
        $httpBackend.whenGET('api/v1/lunchOffer').respond(offers);
      });
    };



    beforeEach(function() {
      module('lunchboxWebapp');
      inject(function($rootScope) { scope = $rootScope.$new(); });
      inject(function($controller) {
        $controller('MainCtrl', { $scope: scope });
      });

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
      it('should set status to LOAD_FINISHED when all queries done', function() {
        initHttpBackend();

        $httpBackend.flush();

        expect(scope.providers).toAngularEqual(testProviders);
        expect(scope.offers).toAngularEqual(testOffers);

        expect(scope.isLoading()).toBeFalsy();
        expect(scope.isLoadFinished()).toBeTruthy();
        expect(scope.isLoadFailed()).toBeFalsy();
      });

      it('should set status to LOADING when at least one query loading', function() {
        initHttpBackend();

        $httpBackend.flush(1);

        expect(scope.isLoading()).toBeTruthy();
        expect(scope.isLoadFinished()).toBeFalsy();
        expect(scope.isLoadFailed()).toBeFalsy();
      });

      it('should set status to LOAD_FAILED when some query fails', function() {
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

      it('should not resolve visible offers when not matches date', function() {
        initHttpBackend();
        scope.day = new Date(Date.UTC(2000, 1, 1));
        scope.location = 'Neubrandenburg';

        $httpBackend.flush(); // refreshVisibleOffers wird implizit aufgerufen

        expect(scope.visibleOffers).toAngularEqual({});
        expect(scope.hasVisibleOffers()).toBeFalsy();
        expect(scope.isLoadFinishedWithNoVisibleOffers()).toBeTruthy();
      });

      it('should not resolve visible offers when not matches location', function() {
        initHttpBackend();
        scope.day = new Date(Date.UTC(2015, 3, 15));
        scope.location = 'New York';

        $httpBackend.flush(); // refreshVisibleOffers wird implizit aufgerufen

        expect(scope.visibleOffers).toAngularEqual({});
        expect(scope.hasVisibleOffers()).toBeFalsy();
        expect(scope.isLoadFinishedWithNoVisibleOffers()).toBeTruthy();
      });

      it('should not resolve visible offers when loading failed', function() {
        initHttpBackend(true);
        scope.day = new Date(Date.UTC(2015, 3, 15));
        scope.location = 'Neubrandenburg';

        $httpBackend.flush(); // refreshVisibleOffers wird implizit aufgerufen

        expect(scope.visibleOffers).toAngularEqual({});
        expect(scope.hasVisibleOffers()).toBeFalsy();
        expect(scope.isLoadFinishedWithNoVisibleOffers()).toBeFalsy();
      });
    });



    describe('daysInOffers', function() {
      it('should return unique dates', function() {
        var yesterday = new Date(Date.UTC(2015, 1, 4));
        var today = new Date(Date.UTC(2015, 1, 5));
        var tomorrow = new Date(Date.UTC(2015, 1, 6));
        scope.offers = [{id: 1, name: 'Angebot 1', day: today, price: 550, provider: 1},
                        {id: 2, name: 'Angebot 2', day: yesterday, price: 550, provider: 1},
                        {id: 3, name: 'Angebot 3', day: today, price: 550, provider: 1},
                        {id: 4, name: 'Angebot 4', day: tomorrow, price: 550, provider: 1},
                        {id: 5, name: 'Angebot 5', day: tomorrow, price: 550, provider: 1},
                        {id: 6, name: 'Angebot 6', day: today, price: 550, provider: 1}];

        var result = scope.daysInOffers();

        expect(result.length).toBe(3);
        expect(result).toContain(yesterday);
        expect(result).toContain(today);
        expect(result).toContain(tomorrow);
      });

      it('should return sorted dates', function() {
        var yesterday = new Date(Date.UTC(2015, 1, 4));
        var today = new Date(Date.UTC(2015, 1, 5));
        var tomorrow = new Date(Date.UTC(2015, 1, 6));
        scope.offers = [{id: 1, name: 'Angebot 1', day: today, price: 550, provider: 1},
                        {id: 2, name: 'Angebot 2', day: tomorrow, price: 550, provider: 1},
                        {id: 3, name: 'Angebot 3', day: yesterday, price: 550, provider: 1}];

        var result = scope.daysInOffers();

        expect(result).toEqual([yesterday, today, tomorrow]);
      });

      it('should be empty when no offers exist', function() {
        scope.offers = [];

        var result = scope.daysInOffers();

        expect(result.length).toBe(0);
      });
    });



    describe('prevDay', function() {
      it('should be yesterday when offer for yesterday exists', function() {
        scope.day = new Date(Date.UTC(2015, 1, 5)); // "today"
        var yesterday = new Date(Date.UTC(2015, 1, 4));
        scope.offers = [{id: 1, name: 'Angebot 1', day: yesterday, price: 550, provider: 1}];

        var result = scope.prevDay();

        expect(result).toBeDefined();
        expect(result.getTime()).toBe(yesterday.getTime());
        expect(scope.hasPrevDay()).toBeTruthy();
      });

      it('should be two days ago when no offer for yesterday exists', function() {
        scope.day = new Date(Date.UTC(2015, 1, 5)); // "today"
        var twoDaysAgo = new Date(Date.UTC(2015, 1, 3));
        scope.offers = [{id: 1, name: 'Angebot 1', day: twoDaysAgo, price: 550, provider: 1}];

        var result = scope.prevDay();

        expect(result).toBeDefined();
        expect(result.getTime()).toBe(twoDaysAgo.getTime());
        expect(scope.hasPrevDay()).toBeTruthy();
      });

      it('should be nearest past day when offers in past exists', function() {
        scope.day = new Date(Date.UTC(2015, 1, 5)); // "today"
        var yesterday = new Date(Date.UTC(2015, 1, 4));
        var twoDaysAgo = new Date(Date.UTC(2015, 1, 3));
        scope.offers = [{id: 1, name: 'Angebot 1', day: twoDaysAgo, price: 550, provider: 1},
                        {id: 2, name: 'Angebot 2', day: yesterday, price: 550, provider: 1},
                        {id: 3, name: 'Angebot 3', day: twoDaysAgo, price: 550, provider: 1}];

        var result = scope.prevDay();

        expect(result).toBeDefined();
        expect(result.getTime()).toBe(yesterday.getTime());
        expect(scope.hasPrevDay()).toBeTruthy();
      });

      it('should be undefined when no offers exist', function() {
        scope.day = new Date(Date.UTC(2015, 1, 5)); // "today"
        scope.offers = [];

        var result = scope.prevDay();

        expect(result).toBeUndefined();
        expect(scope.hasPrevDay()).toBeFalsy();
      });

      it('should be undefined when all offers are in future', function() {
        scope.day = new Date(Date.UTC(2015, 1, 5)); // "today"
        var tomorrow = new Date(Date.UTC(2015, 1, 6));
        scope.offers = [{id: 1, name: 'Angebot 1', day: tomorrow, price: 550, provider: 1}];

        var result = scope.prevDay();

        expect(result).toBeUndefined();
        expect(scope.hasPrevDay()).toBeFalsy();
      });

      it('should be undefined when only offer for today exists', function() {
        scope.day = new Date(Date.UTC(2015, 1, 5)); // "today"
        scope.offers = [{id: 1, name: 'Angebot 1', day: scope.day, price: 550, provider: 1}];

        var result = scope.prevDay();

        expect(result).toBeUndefined();
        expect(scope.hasPrevDay()).toBeFalsy();
      });
    });



    describe('nextDay', function() {
      it('should be tomorrow when offer for tomorrow exists', function() {
        scope.day = new Date(Date.UTC(2015, 1, 5)); // "today"
        var tomorrow = new Date(Date.UTC(2015, 1, 6));
        scope.offers = [{id: 1, name: 'Angebot 1', day: tomorrow, price: 550, provider: 1}];

        var result = scope.nextDay();

        expect(result).toBeDefined();
        expect(result.getTime()).toBe(tomorrow.getTime());
        expect(scope.hasNextDay()).toBeTruthy();
      });

      it('should be in two days when no offer for tomorrow exists', function() {
        scope.day = new Date(Date.UTC(2015, 1, 5)); // "today"
        var inTwoDays = new Date(Date.UTC(2015, 1, 7));
        scope.offers = [{id: 1, name: 'Angebot 1', day: inTwoDays, price: 550, provider: 1}];

        var result = scope.nextDay();

        expect(result).toBeDefined();
        expect(result.getTime()).toBe(inTwoDays.getTime());
        expect(scope.hasNextDay()).toBeTruthy();
      });

      it('should be nearest future day when multiple offers in future exists', function() {
        scope.day = new Date(Date.UTC(2015, 1, 5)); // "today"
        var tomorrow = new Date(Date.UTC(2015, 1, 6));
        var inTwoDays = new Date(Date.UTC(2015, 1, 7));
        scope.offers = [{id: 1, name: 'Angebot 1', day: inTwoDays, price: 550, provider: 1},
                        {id: 2, name: 'Angebot 2', day: tomorrow, price: 550, provider: 1},
                        {id: 3, name: 'Angebot 3', day: inTwoDays, price: 550, provider: 1}];

        var result = scope.nextDay();

        expect(result).toBeDefined();
        expect(result.getTime()).toBe(tomorrow.getTime());
        expect(scope.hasNextDay()).toBeTruthy();
      });

      it('should be undefined when no offers exist', function() {
        scope.day = new Date(Date.UTC(2015, 1, 5)); // "today"
        scope.offers = [];

        var result = scope.nextDay();

        expect(result).toBeUndefined();
        expect(scope.hasNextDay()).toBeFalsy();
      });

      it('should be undefined when all offers are in past', function() {
        scope.day = new Date(Date.UTC(2015, 1, 5)); // "today"
        var yesterday = new Date(Date.UTC(2015, 1, 4));
        scope.offers = [{id: 1, name: 'Angebot 1', day: yesterday, price: 550, provider: 1}];

        var result = scope.nextDay();

        expect(result).toBeUndefined();
        expect(scope.hasNextDay()).toBeFalsy();
      });

      it('should be undefined when only offer for today exists', function() {
        scope.day = new Date(Date.UTC(2015, 1, 5)); // "today"
        scope.offers = [{id: 1, name: 'Angebot 1', day: scope.day, price: 550, provider: 1}];

        var result = scope.nextDay();

        expect(result).toBeUndefined();
        expect(scope.hasNextDay()).toBeFalsy();
      });
    });



    describe('goPrevDay', function() {
      it('should change day and visible offers to yesterday', function() {
        var today = new Date(Date.UTC(2015, 1, 5));
        var yesterday = new Date(Date.UTC(2015, 1, 4));
        var providers = [{id: 1, name: 'Anbieter 1', location: 'Neubrandenburg'}];
        var offers = [{id: 1, name: 'Angebot 1', day: today, price: 550, provider: 1},
                      {id: 2, name: 'Angebot 2', day: yesterday, price: 550, provider: 1},
                      {id: 3, name: 'Angebot 3', day: yesterday, price: 550, provider: 1}];
        initHttpBackend(false, offers, providers);
        scope.day = today;
        scope.location = 'Neubrandenburg';
        $httpBackend.flush();
        expect(scope.isLoadFinished()).toBeTruthy();
        expect(scope.hasVisibleOffers()).toBeTruthy();

        scope.goPrevDay();

        expect(scope.day).toEqual(yesterday);
        expect(scope.hasVisibleOffers()).toBeTruthy();
        expect(scope.visibleOffers).toAngularEqual(
          { 1: [{id: 2, name: 'Angebot 2', day: yesterday, price: 550, provider: 1},
                {id: 3, name: 'Angebot 3', day: yesterday, price: 550, provider: 1}] }
        );

      });
    });



    describe('goNextDay', function() {
      it('should change day and visible offers to tomorrow', function() {
        var today = new Date(Date.UTC(2015, 1, 5));
        var tomorrow = new Date(Date.UTC(2015, 1, 6));
        var providers = [{id: 1, name: 'Anbieter 1', location: 'Neubrandenburg'}];
        var offers = [{id: 1, name: 'Angebot 1', day: today, price: 550, provider: 1},
                      {id: 2, name: 'Angebot 2', day: tomorrow, price: 550, provider: 1},
                      {id: 3, name: 'Angebot 3', day: tomorrow, price: 550, provider: 1}];
        initHttpBackend(false, offers, providers);
        scope.day = today;
        scope.location = 'Neubrandenburg';
        $httpBackend.flush();
        expect(scope.isLoadFinished()).toBeTruthy();
        expect(scope.hasVisibleOffers()).toBeTruthy();

        scope.goNextDay();

        expect(scope.day).toEqual(tomorrow);
        expect(scope.hasVisibleOffers()).toBeTruthy();
        expect(scope.visibleOffers).toAngularEqual(
          { 1: [{id: 2, name: 'Angebot 2', day: tomorrow, price: 550, provider: 1},
                {id: 3, name: 'Angebot 3', day: tomorrow, price: 550, provider: 1}] }
        );

      });
    });

  });

})();
