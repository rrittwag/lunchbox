'use strict';

var app = angular.module('lunchboxWebapp');

app.directive('footer', function() {
  return {
    restrict: 'A',
    replace: true,
    templateUrl: 'components/footer/footer.html'
  };
});
