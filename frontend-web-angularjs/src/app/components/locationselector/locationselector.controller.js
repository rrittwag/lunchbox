(function() {
  'use strict';

  angular
    .module('lunchboxWebapp')
    .controller('LocationSelectorController', LocationSelectorController);

  function LocationSelectorController(LunchModel) {
    var vm = this;

    vm.model = LunchModel;

    // Setzen der Location aus Dropdown
    vm.selectLocation = function(location) {
      LunchModel.setLocation(location);
    };
  }

})();
