(function (angular, vodModule) {
    'use strict';
    
    vodModule.controller('AccessDeniedCtrl', ['$log', function AccessDeniedCtrl ($log) {
        $log.info('AccessDeniedCtrl initialized');
        $log.info('AccessDeniedCtrl execution ended');
    }]);
    
})(window.angular, window.angular.module('vod'));