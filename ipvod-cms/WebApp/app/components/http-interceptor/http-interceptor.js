(function (angular, vodModule) {
    'use strict';
    vodModule
    .factory('httpInterceptor', [
    '$log', '$timeout', '$window', '$location', '$q', 'toaster', 
    function ($log, $timeout, $window, $location, $q, toaster) {
        return {
            // optional method
            'request': function request (config) {
                // do something on success
                return config;
            },

            // optional method 
           'requestError': function requestError (rejection) {
                return $q.reject(rejection);
            },

            // optional method
            'response': function response (resp) {
                // do something on success
                return resp;
            },

            // optional method
           'responseError': function responseError (rejection) {
                var responseStatus = rejection.status,
                    accessNotAuthorized = responseStatus === 401,
                    accessDenied = responseStatus === 403,
                    isLogin = $window.location.hash.indexOf('login') !== -1,
                    redirect = function redirect (loc) {
                        $window.location = 'http://' + $window.location.host + '/' + loc;
                        // window.location = $window.location.host + '/' + loc;
                    },
                    toast = {'type': 'error', 'title': 'Acesso Negado'};

                if ( accessNotAuthorized || accessDenied ) {
                    if ( accessDenied ) {
                        toast.body = 'Você precisa efetuar o login antes de continuar.';
                        if ( !isLogin ) { redirect('vod-public.html#/login'); }
                    } else {
                        toast.body = 'Você não tem permissão para visualizar este conteúdo.';
                        redirect('vod.html#/');
                    }

                    toaster.pop(toast);
                }
                
                return $q.reject(rejection);
            }
        };

    }]);
    
})(window.angular, window.angular.module('vod'));