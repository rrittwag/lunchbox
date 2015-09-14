(function() {
  'use strict';

  describe('offers controller', function(){
    var scope; // Scope für zu instanziierenden Controller

    var locationNB = {name: 'Neubrandenburg', shortName: 'NB'};
    var locationB = {name: 'Berlin', shortName: 'B'};

    var inTwoDays = new Date(Date.UTC(2015, 1, 7));
    var tomorrow = new Date(Date.UTC(2015, 1, 6));
    var today = new Date(Date.UTC(2015, 1, 5));
    var yesterday = new Date(Date.UTC(2015, 1, 4));
    var twoDaysAgo = new Date(Date.UTC(2015, 1, 3));

    var testProviders = [{id: 1, name: 'Anbieter 1', location: locationNB.name},
                         {id: 2, name: 'Anbieter 2', location: locationB.name}];
    var testOffers = [{id: 1, name: 'Angebot 1', day: '2015-02-05', price: 550, provider: 1},
                      {id: 2, name: 'Angebot 2', day: '2015-02-05', price: 450, provider: 2},
                      {id: 3, name: 'Angebot 3', day: '2015-02-05', price: 450, provider: 1}];



    beforeEach(function() {
      module('lunchboxWebapp');
      // LunchModel service mocken
      var model = {
            offers: [],
            providers: [],
            selectedDay: new Date(),
            selectedLocation: locationNB
          }; // gemocktes, (fast) leeres LunchModel
      module(function($provide) {
        $provide.value('LunchModel', model);
      });
      inject(function($rootScope) { scope = $rootScope.$new(); });
      inject(function($controller) {
        $controller('OffersCtrl', { $scope: scope });
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
      it('should init model on scope', function() {
        expect(scope.model).toBeDefined();
        expect(scope.model.providers).toEqual([]);
        expect(scope.model.offers).toEqual([]);
      });

      it('should init visibleOffers to []', function() {
        expect(scope.visibleOffers).toEqual([]);
      });
    });



    describe('visibleOffers', function() {
      it('should refresh when changing offers', function() {
        scope.model.providers = testProviders;
        scope.model.selectedDay = today;
        scope.settings.location = locationNB;
        scope.$apply(); // stößt den watch-Aufruf an
        expect(scope.visibleOffers.length).toBe(0);

        scope.model.offers = testOffers;
        scope.$apply(); // stößt den watch-Aufruf an

        expect(scope.visibleOffers.length).toBe(2);
      });

      it('should refresh when changing providers', function() {
        scope.model.offers = testOffers;
        scope.model.selectedDay = today;
        scope.settings.location = locationNB;
        scope.$apply(); // stößt den watch-Aufruf an
        expect(scope.visibleOffers.length).toBe(0);

        scope.model.providers = testProviders;
        scope.$apply(); // stößt den watch-Aufruf an

        expect(scope.visibleOffers.length).toBe(2);
      });

      it('should refresh when changing selectedLocation', function() {
        scope.model.providers = testProviders;
        scope.model.offers = testOffers;
        scope.model.selectedDay = today;
        scope.settings.location = null;
        scope.$apply(); // stößt den watch-Aufruf an
        expect(scope.visibleOffers.length).toBe(3);

        scope.settings.location = locationB;
        scope.$apply(); // stößt den watch-Aufruf an

        expect(scope.visibleOffers.length).toBe(1);
      });

      it('should refresh when changing selectedDay', function() {
        scope.model.providers = testProviders;
        scope.model.offers = testOffers;
        scope.settings.location = locationNB;
        scope.$apply(); // stößt den watch-Aufruf an
        expect(scope.visibleOffers.length).toBe(0);

        scope.model.selectedDay = today;
        scope.$apply(); // stößt den watch-Aufruf an

        expect(scope.visibleOffers.length).toBe(2);
      });
    });



    describe('selectedDay', function() {
      it('should change on goPrevDay', function() {
        scope.model.selectedDay = today;
        scope.daysInOffersForLocation = [yesterday];

        scope.goPrevDay();

        expect(scope.model.selectedDay).toBe(yesterday);
      });

      it('should change on goNextDay', function() {
        scope.model.selectedDay = today;
        scope.daysInOffersForLocation = [tomorrow];

        scope.goNextDay();

        expect(scope.model.selectedDay).toBe(tomorrow);
      });
    });



    describe('prevDay', function() {
      it('should be yesterday when offer for yesterday exists', function() {
        scope.model.selectedDay = today;
        scope.daysInOffersForLocation = [yesterday];

        var result = scope.prevDay();

        expect(result).toBeDefined();
        expect(result.getTime()).toBe(yesterday.getTime());
        expect(scope.hasPrevDay()).toBeTruthy();
      });

      it('should be two days ago when no offer for yesterday exists', function() {
        scope.model.selectedDay = today;
        scope.daysInOffersForLocation = [twoDaysAgo];

        var result = scope.prevDay();

        expect(result).toBeDefined();
        expect(result.getTime()).toBe(twoDaysAgo.getTime());
        expect(scope.hasPrevDay()).toBeTruthy();
      });

      it('should be nearest past day when offers in past exists', function() {
        scope.model.selectedDay = today;
        scope.daysInOffersForLocation = [twoDaysAgo, yesterday];

        var result = scope.prevDay();

        expect(result).toBeDefined();
        expect(result.getTime()).toBe(yesterday.getTime());
        expect(scope.hasPrevDay()).toBeTruthy();
      });

      it('should be undefined when no offers exist', function() {
        scope.model.selectedDay = today;
        scope.daysInOffersForLocation = [];

        var result = scope.prevDay();

        expect(result).toBeUndefined();
        expect(scope.hasPrevDay()).toBeFalsy();
      });

      it('should be undefined when all offers are in future', function() {
        scope.model.selectedDay = today;
        scope.daysInOffersForLocation = [tomorrow];

        var result = scope.prevDay();

        expect(result).toBeUndefined();
        expect(scope.hasPrevDay()).toBeFalsy();
      });

      it('should be undefined when only offer for today exists', function() {
        scope.model.selectedDay = today;
        scope.daysInOffersForLocation = [today];

        var result = scope.prevDay();

        expect(result).toBeUndefined();
        expect(scope.hasPrevDay()).toBeFalsy();
      });
    });



    describe('nextDay', function() {
      it('should be tomorrow when offer for tomorrow exists', function() {
        scope.model.selectedDay = today;
        scope.daysInOffersForLocation = [tomorrow];

        var result = scope.nextDay();

        expect(result).toBeDefined();
        expect(result.getTime()).toBe(tomorrow.getTime());
        expect(scope.hasNextDay()).toBeTruthy();
      });

      it('should be in two days when no offer for tomorrow exists', function() {
        scope.model.selectedDay = today;
        scope.daysInOffersForLocation = [inTwoDays];

        var result = scope.nextDay();

        expect(result).toBeDefined();
        expect(result.getTime()).toBe(inTwoDays.getTime());
        expect(scope.hasNextDay()).toBeTruthy();
      });

      it('should be nearest future day when multiple offers in future exists', function() {
        scope.model.selectedDay = today;
        scope.daysInOffersForLocation = [tomorrow, inTwoDays];

        var result = scope.nextDay();

        expect(result).toBeDefined();
        expect(result.getTime()).toBe(tomorrow.getTime());
        expect(scope.hasNextDay()).toBeTruthy();
      });

      it('should be undefined when no offers exist', function() {
        scope.model.selectedDay = today;
        scope.daysInOffersForLocation = [];

        var result = scope.nextDay();

        expect(result).toBeUndefined();
        expect(scope.hasNextDay()).toBeFalsy();
      });

      it('should be undefined when all offers are in past', function() {
        scope.model.selectedDay = today;
        scope.daysInOffersForLocation = [yesterday];

        var result = scope.nextDay();

        expect(result).toBeUndefined();
        expect(scope.hasNextDay()).toBeFalsy();
      });

      it('should be undefined when only offer for today exists', function() {
        scope.model.selectedDay = today;
        scope.daysInOffersForLocation = [today];

        var result = scope.nextDay();

        expect(result).toBeUndefined();
        expect(scope.hasNextDay()).toBeFalsy();
      });
    });

  });

})();
