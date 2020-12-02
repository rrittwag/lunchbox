(function() {
  'use strict';

  var locationNB = {name: 'Neubrandenburg', shortName: 'NB'};



  describe('LocationSelector controller', function () {
    var $controller, ctrl, model;

    var initController = function() {
      // Header-Controller erzeugen
      ctrl = $controller('LocationSelectorController', {});
    };

    beforeEach(function () {
      module('lunchboxWebapp');
      inject(function (_$controller_) { $controller = _$controller_; });
      inject(function (LunchModel) { model = LunchModel; });
    });


    describe('setLocation', function () {
      it('should change settings\' location', function () {
        model.location = null;
        model.locations = [];
        initController();
        expect(model.location).toBe(null);

        ctrl.selectLocation(locationNB);

        expect(model.location).toBe(locationNB);
      });
    });

  });
})();
