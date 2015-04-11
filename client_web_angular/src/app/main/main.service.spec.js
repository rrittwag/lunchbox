'use strict';

describe('Service: mainService', function() {
  var scope;
  beforeEach(module('lunchboxWebapp'));
  beforeEach(inject(function($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should attach a list of awesomeThings to the service', function() {
    expect(scope.awesomeThings.length).toBe(3);
  });

});
