'use strict';

describe('main controller', function(){
  var $controller; // $controller ist der Angular-Service, der Controller erzeugt
  var scope; // Scope für zu instanziierenden Controller

  beforeEach(function() {
    module('lunchboxWebapp');
    inject(function(_$controller_) { $controller = _$controller_; });
    inject(function($rootScope) { scope = $rootScope.$new(); });
  });


  describe('instantiation', function() {
    beforeEach(function() {
      // main controller mit entsprechenden Parametern erzeugen
      $controller('MainCtrl', {
        $scope: scope,
        // _: {}, // TODO: Underscore als Util einbinden? ECMA6, RequireJS?
        LunchProviderStore: { query: function() {} }, // TODO: ngMock nutzen?!
        LunchOfferStore: { query: function() {} } // TODO: ngMock nutzen?!
      });
    });

    it('should default day to today', function() {
      var now = new Date();
      expect(scope.day).toBeDefined();
      expect(scope.day.getTime()).toBe(Date.UTC(now.getFullYear(), now.getMonth(), now.getDate()));
//      expect(angular.isArray(scope.awesomeThings)).toBeTruthy();
//      expect(scope.awesomeThings.length > 5).toBeTruthy();
    });

    it('should default location to Neubrandenburg', function() {
      expect(scope.location).toBe('Neubrandenburg');
    });

    it('should be loading data', function() {
      expect(scope.isLoading()).toBeTruthy();
      expect(scope.isLoadFinished()).toBeFalsy();
      expect(scope.isLoadFinishedWithNoVisibleOffers()).toBeFalsy();
      expect(scope.isLoadFailed()).toBeFalsy();
    });

    it('should default visibleOffers to {}', function() {
      expect(angular.equals(scope.visibleOffers, {})).toBeTruthy();
    });

  });
});
