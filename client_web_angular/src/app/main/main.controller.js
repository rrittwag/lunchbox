'use strict';

angular.module('lunchboxWebapp')
  .controller('MainCtrl', function ($scope, _) {
    function today() {
      var result = new Date();
      result.setUTCHours(0,0,0,0);
      return result;
    }

    $scope.day = today();
    $scope.location = 'Neubrandenburg';

    $scope.providers = [
      {
        name: 'Schweinestall',
        location: 'Neubrandenburg',
        id: 1,
      },
      {
        name: 'Hotel am Ring',
        location: 'Neubrandenburg',
        id: 2
      },
      {
        name: 'AOK Cafeteria',
        location: 'Neubrandenburg',
        id: 3
      },
      {
        name: 'Suppenkulttour',
        location: 'Neubrandenburg',
        id: 4
      }
    ];
    $scope.offers = [
      {
        name: 'Germknödel mit Kirschfüllung und Vanillesauce',
        provider: 2,
        price: 520,
        id: 41,
        day: '2015-04-07'
      },
      {
        name: 'Gebackene Rauchwürstchen mit Zigeunersauce, Bratkartoffeln & Salat',
        provider: 2,
        price: 550,
        id: 42,
        day: '2015-04-07'
      },
      {
        name: 'Grützwurst mit Sauerkraut und Salzkartoffeln',
        provider: 3,
        price: 450,
        id: 18,
        day: '2015-03-30'
      },
      {
        name: 'Putengulasch mit Nudeln',
        provider: 3,
        price: 530,
        id: 19,
        day: '2015-03-30'
      }
    ];

    $scope.visibleOffers = {};

    function refreshVisibleOffers() {
      var providerIdsForLocation = _.chain($scope.providers)
                .filter(function(p) { return p.location === $scope.location; })
                .map(function(p) { return p.id; })
                .value();
      var offersForDayAndLocation = $scope.offers.filter( function(o){
          return Date.parse(o.day) === $scope.day.getTime() &&
                 _.contains(providerIdsForLocation, o.provider);
        });
      $scope.visibleOffers = _.groupBy(offersForDayAndLocation, function(o) { return o.provider; });
    }

    $scope.hasVisibleOffers = function(providerId) {
      if (!providerId) {
        return $scope.visibleOffers && !angular.equals($scope.visibleOffers, {});
      } else {
        return $scope.visibleOffers[providerId] && $scope.visibleOffers[providerId].length > 0;
      }
    };

    refreshVisibleOffers();
  });
