/**
 * loadingWidget
 * @description
 * Directive to show/hide a loading message, that can be replaced by a loading gif, svg or whatever possible.
 **/
(function (angular, vodModule) {
    'use strict';

    vodModule
    .directive('loadingWidget', ['$log', 'requestNotification', function ($log, requestNotification) {
        $log.info('loadingWidget initialized');
        return {
            restrict: 'E',
            replace: true,
            template: '<div class="loading-widget"><i class="fa fa-circle-o-notch fa-spin"></i> Carregando. Por favor, aguarde...</div>',
            link: function (scope, element) {
                //subscribe to listen when a request starts
                requestNotification
                .subscribeOnRequestStarted(function() {
                    // show the spinner!
                    element.addClass('visible');
                    $log.info('request started');
                });
                
                //subscribe to listen when a request ends
                requestNotification
                .subscribeOnRequestEnded(function() {
                    // hide the spinner if there are no more pending requests
                    var count = requestNotification.getRequestCount();
                    if (count === 0) {
                        element.removeClass('visible');
                    }
                    $log.info('request ended');
                });
                
                $log.info('loadingWidget execution ended');
            }
        };
    }]);

})(window.angular, window.angular.module('vod'));