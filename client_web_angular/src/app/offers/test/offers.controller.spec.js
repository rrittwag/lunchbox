(function() {
  'use strict';

  describe('offers controller', function(){
    var scope; // Scope für zu instanziierenden Controller

    var locationNB = {name: 'Neubrandenburg', shortName: 'NB'};
    var locationB = {name: 'Berlin', shortName: 'B'};

    var today = new Date(Date.UTC(2015, 1, 5));

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

      it('should init selected day to today (in UTC)', function() {
        var now = new Date();
        expect(scope.selectedDay).toBeDefined();
        expect(scope.selectedDay.getTime()).toBe(Date.UTC(now.getFullYear(), now.getMonth(), now.getDate()));
      });
    });



    describe('visibleOffers', function() {
      it('should refresh when changing offers', function() {
        scope.model.providers = testProviders;
        scope.selectedDay = today;
        scope.model.location = locationNB;
        scope.$apply(); // stößt den watch-Aufruf an
        expect(scope.visibleOffers.length).toBe(0);

        scope.model.offers = testOffers;
        scope.$apply(); // stößt den watch-Aufruf an

        expect(scope.visibleOffers.length).toBe(2);
      });

      it('should refresh when changing providers', function() {
        scope.model.offers = testOffers;
        scope.selectedDay = today;
        scope.model.location = locationNB;
        scope.$apply(); // stößt den watch-Aufruf an
        expect(scope.visibleOffers.length).toBe(0);

        scope.model.providers = testProviders;
        scope.$apply(); // stößt den watch-Aufruf an

        expect(scope.visibleOffers.length).toBe(2);
      });

      it('should refresh when changing selectedLocation', function() {
        scope.model.providers = testProviders;
        scope.model.offers = testOffers;
        scope.selectedDay = today;
        scope.model.location = null;
        scope.$apply(); // stößt den watch-Aufruf an
        expect(scope.visibleOffers.length).toBe(3);

        scope.model.location = locationB;
        scope.$apply(); // stößt den watch-Aufruf an

        expect(scope.visibleOffers.length).toBe(1);
      });

      it('should refresh when changing selectedDay', function() {
        scope.model.providers = testProviders;
        scope.model.offers = testOffers;
        scope.model.location = locationNB;
        scope.$apply(); // stößt den watch-Aufruf an
        expect(scope.visibleOffers.length).toBe(0);

        scope.selectedDay = today;
        scope.$apply(); // stößt den watch-Aufruf an

        expect(scope.visibleOffers.length).toBe(2);
      });
    });

  });

})();
