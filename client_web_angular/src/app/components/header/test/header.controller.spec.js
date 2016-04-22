(function() {
  'use strict';

  var locationNB = {name: 'Neubrandenburg', shortName: 'NB'};
  var locationB = {name: 'Berlin', shortName: 'B'};

  var someAndEveryMatchers = {
    some : function () {
      return {
        compare : function (actual, expected) {
          if (expected === undefined) {
            expected = function () { return false; };
          }
          var result = {};
          result.pass = false;
          for (var index = 0; index < actual.length; ++index) {
            result.pass = result.pass || expected(actual[index]);
          }
          return result;
        }
      };
    },
    every : function () {
      return {
        compare : function (actual, expected) {
          if (expected === undefined) {
            expected = function () { return false; };
          }
          var result = {};
          result.pass = true;
          for (var index = 0; index < actual.length; ++index) {
            result.pass = result.pass && expected(actual[index]);
          }
          return result;
        }
      };
    }
  };

  var rootRoute = { path: '/', name: 'root' };

  describe('Header controller', function () {
    var $controller, scope, model;

    var initController = function() {
      // Header-Controller erzeugen
      $controller('HeaderCtrl', { $scope: scope });
    };

    beforeEach(function () {
      module('lunchboxWebapp');
      inject(function (_$controller_) { $controller = _$controller_; });
      inject(function ($rootScope) { scope = $rootScope.$new(); });
      inject(function (LunchModel) { model = LunchModel; });
      jasmine.addMatchers(someAndEveryMatchers);
    });



    describe('instantiation', function () {
      it('should init routes with attributes path and name', function () {
        initController();

        expect(scope.header.routes).every(function (elem) {
          return elem.path !== undefined && elem.name !== undefined;
        });
      });

      it('should init routes with at least home route', function () {
        initController();

        expect(scope.header.routes).some(function (elem) {
          return elem.path === '/';
        });
      });

      it('should init location with first of model.locations', function () {
        model.location = null;
        model.locations = [locationNB, locationB];

        initController();

        expect(model.location).toBe(locationNB);
      });

      it('should not init location if already set', function () {
        model.location = locationB;
        model.locations = [locationNB, locationB];

        initController();

        expect(model.location).toBe(locationB);
      });

      it('should init location to null if no locations found', function () {
        model.location = null;
        model.locations = [];

        initController();

        expect(model.location).toBe(null);
      });
    });



    describe('activeRoute', function () {
      var location;

      beforeEach(function () {
        inject(function ($location) { location = $location; });
        initController();
      });

      it('should be true if on root path', function () {
        location.path('/');
        expect(scope.header.activeRoute(rootRoute)).toBeTruthy();
      });

      it('should be false if not on root path', function () {
        location.path('/somewhere');
        expect(scope.header.activeRoute(rootRoute)).toBeFalsy();
      });
    });



    describe('setLocation', function () {
      it('should change settings\' location', function () {
        model.location = null;
        model.locations = [];
        initController();
        expect(model.location).toBe(null);

        scope.header.selectLocation(locationNB);

        expect(model.location).toBe(locationNB);
      });
    });

  });

})();
