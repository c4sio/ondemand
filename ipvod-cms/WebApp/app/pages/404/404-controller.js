(function (angular, vodModule) {
    'use strict';
    
    vodModule.controller('NotFoundCtrl', ['$log', '$scope', '$rootScope', function NotFoundCtrl ($log, $scope, $rootScope) {
        $log.info('NotFoundCtrl initialized');
        
        $scope.invalidRoute = $rootScope.invalidRoute;

        $log.info('NotFoundCtrl execution ended');
    }]);
    
})(window.angular, window.angular.module('vod'));