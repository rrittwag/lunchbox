(function() {
  'use strict';

  describe('about route', function() {
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



    it('should be loaded on /about', function () {
      location.path('/about');
      rootScope.$digest();
      expect(route.current.templateUrl).toBe('app/about/about.html');
      expect(route.current.controller).toBeUndefined();
    });

    it('should not be loaded on /some_address', function () {
      location.path('/some_address');
      rootScope.$digest();
      expect(route.current.templateUrl).not.toBe('app/about/about.html');
    });
  });

})();
