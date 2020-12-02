(function() {
  'use strict';

  var toJqliteContainMatchers = {
    toJqliteContain : function () {
      return {
        compare : function (actual, expected) {
          if (angular.isUndefined(expected)) {
            expected = function () { return false; };
          }
          var result = {};
          result.pass = false;
          for (var index = 0; index < actual.length; ++index) {
            result.pass = result.pass || expected(actual.eq(index));
          }
          return result;
        }
      };
    }
  };

  describe('Header Directive', function () {
    var node, scope;

    beforeEach(function () {
      module('lunchboxWebapp');
      // LunchModel service mocken
      module(function($provide) {
        $provide.value('LunchModel', { locations: [], selectedLocation: null });
      });
      // einfaches Scope erzeugen
      inject(function ($rootScope) { scope = $rootScope.$new(); });
      // Scope an einfaches HTML-Template binden, das lediglich den Header enthält
      inject(function ($compile) {
        node = $compile('<div header></div>')(scope);
        scope.$digest(); // Daten aus Scope in Template übertragen
      });
      jasmine.addMatchers(toJqliteContainMatchers);
    });

    it('contains <nav> as root element', function () {
      // node ist ein Angular-Wrapper-Objekt (jqLite).
      // Durch [0] wird ein DOM-Element draus, welches die interessanten Properties rausrückt.
      expect(node[0].tagName.toLowerCase()).toEqual('nav');
    });

    xit('navbar contains root link', function () {
      var links = node.find('ul.navbar-right > li > a'); // TODO: jqLite unterstützt nur einfache Tag-Namen, keine CSS-Selektoren. Das mächtigere jQuery verwenden? Oder DOM's querySelectorAll?
      expect(links).toJqliteContain(function (elem) {
        return elem.attr('href') === '/';
      });
    });

    xit('navbar contains active root link', function () {
      var lis = node.find('ul.navbar-right > li'); // TODO: jqLite unterstützt nur einfache Tag-Namen, keine CSS-Selektoren.Das mächtigere jQuery verwenden? Oder DOM's querySelectorAll?
      expect(lis).toJqliteContain(function (elem) {
        return elem.hasClass('active') && elem.find('a').eq(0).attr('href') === '/';
      });
    });
  });

})();
