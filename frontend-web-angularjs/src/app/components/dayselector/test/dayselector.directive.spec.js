(function() {
  'use strict';

  describe('DaySelector Directive', function () {
    var node, scope;

    beforeEach(function () {
      module('lunchboxWebapp');
      // einfaches Scope erzeugen
      inject(function ($rootScope) { scope = $rootScope.$new(); });
      // Scope an einfaches HTML-Template binden, das lediglich den Header enthält
      inject(function ($compile) {
        node = $compile('<day-selector></day-selector>')(scope);
        scope.$digest(); // Daten aus Scope in Template übertragen
      });
    });

    it('contains root element with class day-control', function () {
      // node ist ein Angular-Wrapper-Objekt (jqLite).
      // Durch [0] wird ein DOM-Element draus, welches die interessanten Properties rausrückt.
      expect(node[0].classList).toContain('day-control');
    });
  });

})();
