(function() {
  'use strict';

  // WebApp-Modul abrufen ...
  angular
    .module('lunchboxWebapp')
    .controller('OffersController', OffersController);

  // ... und Controller für Offers-View erzeugen
  function OffersController($scope, $filter, _, LunchModel) {
    $scope.model = LunchModel;

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
      var providersForLocation = $filter('filterProvidersByLocation')($scope.model.providers, $scope.model.location);
      $scope.offersForLocation = $filter('filterOffersByProviders')($scope.model.offers, providersForLocation);
    }

    function refreshDaysInOffersForLocation() {
      $scope.daysInOffersForLocation = $filter('filterDaysInOffers')($scope.offersForLocation);
    }

    function refreshVisibleOffers() {
      $scope.visibleOffers = $filter('filterOffersByDay')($scope.offersForLocation, $scope.selectedDay);
    }

    $scope.$watchGroup(['model.offers', 'model.providers', 'model.location'], function () {
      refreshOffersForLocation();
      refreshDaysInOffersForLocation();
      refreshVisibleOffers();
    }, true);

    $scope.$watch('selectedDay', function () {
      refreshVisibleOffers();
    }, true);

  }

})();
