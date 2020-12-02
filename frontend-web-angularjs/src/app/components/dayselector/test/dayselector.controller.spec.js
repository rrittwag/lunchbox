(function() {
  'use strict';

  describe('DaySelector Controller', function () {
    var scope; // Scope für zu instanziierenden Controller

    var inTwoDays = new Date(Date.UTC(2015, 1, 7));
    var tomorrow = new Date(Date.UTC(2015, 1, 6));
    var today = new Date(Date.UTC(2015, 1, 5));
    var yesterday = new Date(Date.UTC(2015, 1, 4));
    var twoDaysAgo = new Date(Date.UTC(2015, 1, 3));



    beforeEach(function() {
      module('lunchboxWebapp');
      // LunchModel service mocken
      inject(function($rootScope) { scope = $rootScope.$new(); });
      inject(function($controller) {
        $controller('DaySelectorController', { $scope: scope });
      });
    });



    describe('day', function() {
      it('should change on goPrevDay', function() {
        scope.selectedDay = today;
        scope.days = [yesterday];

        scope.goPrevDay();

        expect(scope.selectedDay).toBe(yesterday);
      });

      it('should change on goNextDay', function() {
        scope.selectedDay = today;
        scope.days = [tomorrow];

        scope.goNextDay();

        expect(scope.selectedDay).toBe(tomorrow);
      });
    });



    describe('prevDay', function() {
      it('should be yesterday when offer for yesterday exists', function() {
        scope.selectedDay = today;
        scope.days = [yesterday];

        var result = scope.prevDay();

        expect(result).toBeDefined();
        expect(result.getTime()).toBe(yesterday.getTime());
        expect(scope.hasPrevDay()).toBeTruthy();
      });

      it('should be two days ago when no offer for yesterday exists', function() {
        scope.selectedDay = today;
        scope.days = [twoDaysAgo];

        var result = scope.prevDay();

        expect(result).toBeDefined();
        expect(result.getTime()).toBe(twoDaysAgo.getTime());
        expect(scope.hasPrevDay()).toBeTruthy();
      });

      it('should be nearest past day when offers in past exists', function() {
        scope.selectedDay = today;
        scope.days = [twoDaysAgo, yesterday];

        var result = scope.prevDay();

        expect(result).toBeDefined();
        expect(result.getTime()).toBe(yesterday.getTime());
        expect(scope.hasPrevDay()).toBeTruthy();
      });

      it('should be undefined when no offers exist', function() {
        scope.selectedDay = today;
        scope.days = [];

        var result = scope.prevDay();

        expect(result).toBeUndefined();
        expect(scope.hasPrevDay()).toBeFalsy();
      });

      it('should be undefined when all offers are in future', function() {
        scope.selectedDay = today;
        scope.days = [tomorrow];

        var result = scope.prevDay();

        expect(result).toBeUndefined();
        expect(scope.hasPrevDay()).toBeFalsy();
      });

      it('should be undefined when only offer for today exists', function() {
        scope.selectedDay = today;
        scope.days = [today];

        var result = scope.prevDay();

        expect(result).toBeUndefined();
        expect(scope.hasPrevDay()).toBeFalsy();
      });
    });



    describe('nextDay', function() {
      it('should be tomorrow when offer for tomorrow exists', function() {
        scope.selectedDay = today;
        scope.days = [tomorrow];

        var result = scope.nextDay();

        expect(result).toBeDefined();
        expect(result.getTime()).toBe(tomorrow.getTime());
        expect(scope.hasNextDay()).toBeTruthy();
      });

      it('should be in two days when no offer for tomorrow exists', function() {
        scope.selectedDay = today;
        scope.days = [inTwoDays];

        var result = scope.nextDay();

        expect(result).toBeDefined();
        expect(result.getTime()).toBe(inTwoDays.getTime());
        expect(scope.hasNextDay()).toBeTruthy();
      });

      it('should be nearest future day when multiple offers in future exists', function() {
        scope.selectedDay = today;
        scope.days = [tomorrow, inTwoDays];

        var result = scope.nextDay();

        expect(result).toBeDefined();
        expect(result.getTime()).toBe(tomorrow.getTime());
        expect(scope.hasNextDay()).toBeTruthy();
      });

      it('should be undefined when no offers exist', function() {
        scope.selectedDay = today;
        scope.days = [];

        var result = scope.nextDay();

        expect(result).toBeUndefined();
        expect(scope.hasNextDay()).toBeFalsy();
      });

      it('should be undefined when all offers are in past', function() {
        scope.selectedDay = today;
        scope.days = [yesterday];

        var result = scope.nextDay();

        expect(result).toBeUndefined();
        expect(scope.hasNextDay()).toBeFalsy();
      });

      it('should be undefined when only offer for today exists', function() {
        scope.selectedDay = today;
        scope.days = [today];

        var result = scope.nextDay();

        expect(result).toBeUndefined();
        expect(scope.hasNextDay()).toBeFalsy();
      });
    });

  });

})();
