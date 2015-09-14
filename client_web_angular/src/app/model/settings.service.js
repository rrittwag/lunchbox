(function() {
  'use strict';

  var app = angular.module('lunchboxWebapp');

  app.service('Settings', function(/* _, ... */) {
    var thisService = this;

    this.location = null; // TODO: lokal speichern und auslesen
    this.setLocation = function(location) {
      thisService.location = location;
    };
  });

})();
