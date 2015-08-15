(function() {
  'use strict';

  describe('Footer Directive', function () {

    var node, scope;

    beforeEach(function () {
      module('lunchboxWebapp');
      inject(function ($rootScope) { scope = $rootScope.$new(); });
      inject(function ($compile) {
        var jQueryNode = $compile('<div footer></div>')(scope);
        scope.$digest(); // Daten aus Scope in Template übertragen
        node = jQueryNode[0]; // Element 0 enthält den Root-Node des Templates
      });
    });

    it('contains <footer> element', function () {
      expect(node.nodeType).toEqual(node.ELEMENT_NODE);
      expect(node.tagName.toLowerCase()).toEqual('footer');
    });

  });

})();
