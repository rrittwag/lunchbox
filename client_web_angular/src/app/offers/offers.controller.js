(function() {
  'use strict';

  // WebApp-Modul abrufen ...
  var app = angular.module('lunchboxWebapp');

  // ... und Controller für Offers-View erzeugen
  app.controller('OffersCtrl', function ($scope, $filter, _, LunchModel, Settings) {
    $scope.model = LunchModel;
    $scope.settings = Settings;

    // vom Nutzer ausgewählter Tag (Default: heute)
    function today() {
      var localNow = new Date();
      return new Date(Date.UTC(localNow.getFullYear(), localNow.getMonth(), localNow.getDate()));
    }
    $scope.selectedDay = today();

    // Zwischenwerte, um den Filter-Aufwand gering zu halten
    $scope.offersForLocation = [];
    $scope.daysInOffersForLocation = [];
    $scope.visibleOffers = [];

    function refreshOffersForLocation() {
      var providersForLocation = $filter('filterProvidersByLocation')($scope.model.providers, $scope.settings.location);
      $scope.offersForLocation = $filter('filterOffersByProviders')($scope.model.offers, providersForLocation);
    }

    function refreshDaysInOffersForLocation() {
      $scope.daysInOffersForLocation = $filter('filterDaysInOffers')($scope.offersForLocation);
    }

    function refreshVisibleOffers() {
      $scope.visibleOffers = $filter('filterOffersByDay')($scope.offersForLocation, $scope.selectedDay);
    }

    $scope.$watchGroup(['model.offers', 'model.providers', 'settings.location'], function () {
      refreshOffersForLocation();
      refreshDaysInOffersForLocation();
      refreshVisibleOffers();
    }, true);

    $scope.$watch('selectedDay', function () {
      refreshVisibleOffers();
    }, true);

  });

})();
