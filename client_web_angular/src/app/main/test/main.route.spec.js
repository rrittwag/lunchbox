(function() {
  'use strict';

  describe('main route', function() {
    var location, route, rootScope;

    beforeEach(function () {
      module('lunchboxWebapp');
      inject(function (_$location_) { location = _$location_; });
      inject(function (_$route_) { route = _$route_; });
      inject(function (_$rootScope_) { rootScope = _$rootScope_; });
      inject(function ($httpBackend) {
        $httpBackend.expectGET().respond(200);
      });
    });



    it('should be loaded on /', function () {
      location.path('/');
      rootScope.$digest();
      expect(route.current.templateUrl).toBe('app/main/main.html');
      expect(route.current.controller).toBe('MainCtrl');
    });

    it('should be default on some undefined address', function () {
      location.path('/some_undefined_address');
      rootScope.$digest();
      expect(route.current.templateUrl).toBe('app/main/main.html');
      expect(route.current.controller).toBe('MainCtrl');
    });
  });

})();
