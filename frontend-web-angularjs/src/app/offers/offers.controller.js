(function() {
  'use strict';

  // WebApp-Modul abrufen ...
  angular
    .module('lunchboxWebapp')
    .controller('OffersController', OffersController);

  // ... und Controller für Offers-View erzeugen
  function OffersController($scope, $filter, _, LunchModel) {
    var vm = this;
    vm.model = LunchModel;

    // vom Nutzer ausgewählter Tag (Default: heute)
    function today() {
      var localNow = new Date();
      return new Date(Date.UTC(localNow.getFullYear(), localNow.getMonth(), localNow.getDate()));
    }
    vm.selectedDay = today();

    // Zwischenwerte, um den Filter-Aufwand gering zu halten
    vm.offersForLocation = [];
    vm.daysInOffersForLocation = [];
    vm.visibleOffers = [];

    function refreshOffersForLocation() {
      var providersForLocation = $filter('filterProvidersByLocation')(vm.model.providers, vm.model.location);
      vm.offersForLocation = $filter('filterOffersByProviders')(vm.model.offers, providersForLocation);
    }

    function refreshDaysInOffersForLocation() {
      vm.daysInOffersForLocation = $filter('filterDaysInOffers')(vm.offersForLocation);
    }

    function refreshVisibleOffers() {
      vm.visibleOffers = $filter('filterOffersByDay')(vm.offersForLocation, vm.selectedDay);
    }

    $scope.$watchGroup([function() { return vm.model.offers; },
                        function() { return vm.model.providers; },
                        function() { return vm.model.location; }], function () {
      refreshOffersForLocation();
      refreshDaysInOffersForLocation();
      refreshVisibleOffers();
    }, true);

    $scope.$watch(function () { return vm.selectedDay; }, function () {
      refreshVisibleOffers();
    }, true);

  }

})();
