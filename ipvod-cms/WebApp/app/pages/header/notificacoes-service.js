(function (angular, vodModule) {
    'use strict';

    vodModule.factory('notificacoesService', ['$log', '$http', '$q', 'API_BASE_URL', function ($log, $http, $q, API_BASE_URL) {
        $log.info('Notifications Service initialized');
        var notifications = [
            { 'id': 1, 'type': 'assets', 'msg': 'Novos ítens foram adicionados', 'items': 3 },
            { 'id': 2, 'type': 'login', 'msg': 'O usuário John Doe entrou no sistema' }
        ];
        var notificationTypes = {
                'assets': function assets () {
                    return 'fa-film';
                },
                'login': function login () {
                    return 'fa-user';
                }
            },
            service = {
                'get': function get () {
                    var deferred = $q.defer();
                    $http.get(API_BASE_URL + '/IPVOD/rest/notification')
                    .success(function (notifications) {
                        notifications.forEach(function(value, index) {
                            value.type = value.type || 'assets';
                            value.icon = notificationTypes[value.type]();
                        });
                        deferred.resolve(notifications);
                    })
                    .catch(function (error) {
                        deferred.reject(error);
                        $log.error(error.statusText, error.status);
                    });

                    return deferred.promise;
                }
            };

        $log.info('Notifications Service execution ended');

        return service;
    }]);

})(window.angular, window.angular.module('vod'));