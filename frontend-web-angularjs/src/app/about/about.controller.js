(function() {
  'use strict';

  angular
    .module('lunchboxWebapp')
    .controller('AboutController', AboutController);

  function AboutController() {
    var vm = this;

    vm.feedDropdownToggled = function(open) {
      if (open) {
        vm.tooltipIsOpen = false;
      }
    };
  }

})();
