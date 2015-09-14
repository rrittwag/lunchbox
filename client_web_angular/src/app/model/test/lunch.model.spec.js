(function() {
  'use strict';

  describe('lunch model', function() {
    var model;
    var $httpBackend; // fake http backend, initialized via initHttpBackend

    var testProviders = [{id: 1, name: 'Anbieter 1', location: 'Neubrandenburg'},
                         {id: 2, name: 'Anbieter 2', location: 'Berlin'}];
    var testOffers = [{id: 1, name: 'Angebot 1', day: '2015-04-15', price: 550, provider: 1},
                      {id: 2, name: 'Angebot 2', day: '2015-04-15', price: 450, provider: 2},
                      {id: 3, name: 'Angebot 3', day: '2015-01-01', price: 450, provider: 1}];

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

    // Custom Matcher, der beim Vergleich AngularJS-Wrapper kaschiert (z.B. Promise, Resource)
    var angularEqualMatcher = {
      toAngularEqual: function() {
        return {
          compare : function(actual, expected) {
            return { pass : angular.equals(actual, expected) };
          }
        };
      }
    };



    beforeEach(function() {
      module('lunchboxWebapp');
      inject(function(LunchModel) { model = LunchModel; });
      jasmine.addMatchers(angularEqualMatcher);
    });



    describe('instantiation', function() {
      beforeEach(function() {
        initHttpBackend();
      });

      it('should init selected day to today (in UTC)', function() {
        var now = new Date();
        expect(model.selectedDay).toBeDefined();
        expect(model.selectedDay.getTime()).toBe(Date.UTC(now.getFullYear(), now.getMonth(), now.getDate()));
      });

      it('should init status to LOADING', function() {
        expect(model.isLoading()).toBeTruthy();
        expect(model.isLoadFinished()).toBeFalsy();
        expect(model.isLoadFailed()).toBeFalsy();
      });

      it('should init offers to []', function() {
        expect(model.offers).toEqual([]);
      });

      it('should init providers to []', function() {
        expect(model.providers).toEqual([]);
      });
    });



    describe('query offers', function() {
      it('should set status to LOAD_FINISHED when all queries done', function() {
        initHttpBackend();

        $httpBackend.flush();

        expect(model.providers).toAngularEqual(testProviders);
        expect(model.offers).toAngularEqual(testOffers);

        expect(model.isLoading()).toBeFalsy();
        expect(model.isLoadFinished()).toBeTruthy();
        expect(model.isLoadFailed()).toBeFalsy();
      });

      it('should set status to LOADING when at least one query loading', function() {
        initHttpBackend();

        $httpBackend.flush(1);

        expect(model.isLoading()).toBeTruthy();
        expect(model.isLoadFinished()).toBeFalsy();
        expect(model.isLoadFailed()).toBeFalsy();
      });

      it('should set status to LOAD_FAILED when some query fails', function() {
        initHttpBackend(true);

        $httpBackend.flush();

        expect(model.isLoading()).toBeFalsy();
        expect(model.isLoadFinished()).toBeFalsy();
        expect(model.isLoadFailed()).toBeTruthy();
      });
    });

  });

})();
