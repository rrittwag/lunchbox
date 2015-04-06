'use strict';

angular.module('lunchboxWebapp')
  .controller('MainCtrl', function ($scope) {
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
        day: '2015-03-30'
      },
      {
        name: 'Gebackene Rauchwürstchen mit Zigeunersauce, Bratkartoffeln & Salat',
        provider: 2,
        price: 550,
        id: 42,
        day: '2015-03-30'
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

  });
