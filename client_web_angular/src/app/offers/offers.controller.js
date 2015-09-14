(function() {
  'use strict';

  // WebApp-Modul abrufen ...
  var app = angular.module('lunchboxWebapp');

  // ... und Controller für Offers-View erzeugen
  app.controller('OffersCtrl', function ($scope, $filter, _, LunchProviderStore, LunchOfferStore, LunchModel, Settings) {
    $scope.model = LunchModel;
    $scope.settings = Settings;

    $scope.$watchGroup(['model.offers', 'model.providers', 'settings.location'], function () {
      refreshOffersForLocation();
      refreshDaysInOffersForLocation();
      refreshVisibleOffers();
    }, true);

    $scope.$watch('model.selectedDay', function () {
      refreshVisibleOffers();
    }, true);

    var offersForLocation = [];
    $scope.daysInOffersForLocation = []; // TODO: ist nur für Unit-Test eine Scope-Variable
    $scope.visibleOffers = [];

    function refreshOffersForLocation() {
      var providersForLocation = $filter('filterProvidersByLocation')($scope.model.providers, $scope.settings.location);
      offersForLocation = $filter('filterOffersByProviders')($scope.model.offers, providersForLocation);
    }

    function refreshDaysInOffersForLocation() {
      $scope.daysInOffersForLocation = $filter('filterDaysInOffers')(offersForLocation);
    }

    function refreshVisibleOffers() {
      $scope.visibleOffers = $filter('filterOffersByDay')(offersForLocation, $scope.model.selectedDay);
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
    };

    $scope.goNextDay = function() {
      $scope.model.selectedDay = $scope.nextDay();
    };

    $scope.hasPrevDay = function() {
      return $scope.prevDay() !== undefined;
    };

    $scope.hasNextDay = function() {
      return $scope.nextDay() !== undefined;
    };

  });

})();
