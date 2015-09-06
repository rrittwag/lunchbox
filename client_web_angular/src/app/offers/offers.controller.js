(function() {
  'use strict';

  // WebApp-Modul abrufen ...
  var app = angular.module('lunchboxWebapp');

  // ... und Controller für Offers-View erzeugen
  app.controller('OffersCtrl', function ($scope, $filter, _, LunchProviderStore, LunchOfferStore, LunchModel) {
    $scope.model = LunchModel;

    $scope.$watchGroup(['model.offers', 'model.providers', 'model.selectedLocation'], function () {
      refreshOffersForLocation();
      refreshDaysInOffersForLocation();
      refreshVisibleOffers();
    }, true);

    $scope.$watch('model.selectedDay', function () {
      refreshVisibleOffers();
    }, true);

    $scope.offersForLocation = [];
    $scope.daysInOffersForLocation = [];
    $scope.visibleOffers = [];

    function refreshOffersForLocation() {
      var providersForLocation = $filter('filterProvidersByLocation')($scope.model.providers, $scope.model.selectedLocation);
      $scope.offersForLocation = $filter('filterOffersByProviders')($scope.model.offers, providersForLocation);
    }

    function refreshDaysInOffersForLocation() {
      $scope.daysInOffersForLocation = $filter('filterDaysInOffers')($scope.offersForLocation);
    }

    function refreshVisibleOffers() {
      $scope.visibleOffers = $filter('filterOffersByDay')($scope.offersForLocation, $scope.model.selectedDay);
    }

    $scope.prevDay = function() {
      return _.chain($scope.daysInOffersForLocation)
          .filter(function(day) { return day < $scope.model.selectedDay; })
          .last().value();
    };

    $scope.nextDay = function() {
      return _.chain($scope.daysInOffersForLocation)
          .filter(function(day) { return day > $scope.model.selectedDay; })
          .first().value();
    };

    $scope.goPrevDay = function() {
      $scope.model.selectedDay = $scope.prevDay();
      refreshVisibleOffers();
    };

    $scope.goNextDay = function() {
      $scope.model.selectedDay = $scope.nextDay();
      refreshVisibleOffers();
    };

    $scope.hasPrevDay = function() {
      return $scope.prevDay() !== undefined;
    };

    $scope.hasNextDay = function() {
      return $scope.nextDay() !== undefined;
    };

  });

})();
