(function() {
  'use strict';

  angular
    .module('lunchboxWebapp')
    .service('LunchModel', LunchModel);

  function LunchModel(_, LunchProviderStore, LunchOfferStore, localStorageService) {
    var thisService = this;

    // Locations
    var locationNB = {
      name: 'Neubrandenburg',
      shortName: 'NB'
    };
    var locationBerlin = {
      name: 'Berlin Springpfuhl',
      shortName: 'B'
    };
    this.locations = [locationNB, locationBerlin];

    this.location = _.findWhere(this.locations, {name: localStorageService.get("STORAGEKEY_LOCATION")} );
    this.setLocation = function(location) {
      thisService.location = location;
      localStorageService.set("STORAGEKEY_LOCATION", location.name);
    };

    // providers & offers
    var LoadStatusEnum = Object.freeze({LOADING: 0, LOADED: 1, FAILED: 2});
    var loadStatus = {
      providers: LoadStatusEnum.LOADING,
      offers: LoadStatusEnum.LOADING
    };

    this.providers = [];
    LunchProviderStore.query(
      function(result) { // on success
        thisService.providers = result;
        loadStatus.providers = LoadStatusEnum.FINISHED;
      }, function() { // on error
        loadStatus.providers = LoadStatusEnum.FAILED;
      }
    );

    this.offers = [];
    LunchOfferStore.query(
      function(result) { // on success
        thisService.offers = result;
        loadStatus.offers = LoadStatusEnum.FINISHED;
      }, function() { // on error
        loadStatus.offers = LoadStatusEnum.FAILED;
      }
    );

    this.isLoading = function() {
      var statusArray = _.map(loadStatus);
      return _.some(statusArray, function(value) {
        return value === LoadStatusEnum.LOADING;
      });
    };

    this.isLoadFinished = function() {
      var statusArray = _.map(loadStatus);
      return _.every(statusArray, function(value) {
        return value === LoadStatusEnum.FINISHED;
      });
    };

    this.isLoadFailed = function() {
      var statusArray = _.map(loadStatus);
      return _.some(statusArray, function(value) {
        return value === LoadStatusEnum.FAILED;
      });
    };

  }

})();
