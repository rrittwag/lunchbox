'use strict';

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
  var $controller, scope;

  beforeEach(function () {
    module('lunchboxWebapp');
    inject(function (_$controller_) { $controller = _$controller_; });
    inject(function ($rootScope) { scope = $rootScope.$new(); });
    jasmine.addMatchers(someAndEveryMatchers);
  });



  describe('instantiation', function () {
    beforeEach(function () {
      $controller('HeaderCtrl', { $scope: scope });
    });

    it('should init routes with attributes path and name', function () {
      expect(scope.routes).every(function (elem) {
        return elem.path !== undefined && elem.name !== undefined;
      });
    });

    it('should init routes with at least home route', function () {
      expect(scope.routes).some(function (elem) {
        return elem.path === '/';
      });
    });
  });



  describe('activeRoute', function () {
    var location;

    beforeEach(function () {
      location = {};
      $controller('HeaderCtrl', { $scope: scope, $location: location });
    });

    it('should be true if on root path', function () {
      location.path = function () { return '/'; };
      expect(scope.activeRoute(rootRoute)).toBeTruthy();
    });

    it('should be false if not on root path', function () {
      location.path = function () { return '/somewhere'; };
      expect(scope.activeRoute(rootRoute)).toBeFalsy();
    });
  });

});
