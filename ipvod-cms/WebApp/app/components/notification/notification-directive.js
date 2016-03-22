angular
  .module('vod')
  .directive('gvtNotification', function ($log, $timeout, $animate) {
    $log.info('gvtNotification directive initialized');

    return {
      templateUrl : 'components/notification/notification.html',

      restrict : 'E',

      scope : {
        notification : '=src',
        ttl : '@',
        title : '@',
        description : '@'
      },

      link : function (scope, element) {
        var notification = scope.notification;
        var ttl = Number(scope.ttl || (notification && notification.ttl));
        var timeoutPromise;

        delete scope.notification;
        delete scope.ttl;

        scope.showDescription = false;

        if (notification) {
          scope.title = scope.title || notification.title;
          scope.description = scope.description || notification.description;
        }

        scope.toggleDescription = function () {
          scope.showDescription = !scope.showDescription;
          resetTimeout();
        };

        function resetTimeout() {
          if (timeoutPromise) {
            $timeout.cancel(timeoutPromise);
          }

          if (ttl) {
            timeoutPromise = $timeout(
              function () { $animate.leave(element); },
              // If the description is visible, gives the user some extra time
              // to read it
              scope.showDescription ? ttl * 3 : ttl
            );
          }
        }

        $animate.enter(element, element.parent(), element.prev(), resetTimeout);
      }
    };
  });
