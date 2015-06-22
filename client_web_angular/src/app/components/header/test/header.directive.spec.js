'use strict';

var toJqliteContainMatchers = {
  toJqliteContain : function () {
    return {
      compare : function (actual, expected) {
        if (expected === undefined) {
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
    module('templates');
    inject(function ($rootScope) { scope = $rootScope.$new(); });
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

  it('navbar contains root link', function () {
    var links = node.find('li > a'); // CSS-Selektoren werden unterstützt, XPath nicht
    expect(links).toJqliteContain(function (elem) {
      return elem.attr('href') === '/';
    });
  });

  it('navbar contains active root link', function () {
    var lis = node.find('li');
    expect(lis).toJqliteContain(function (elem) {
      return elem.hasClass('active') && elem.find('a').eq(0).attr('href') === '/';
    });
  });
});
