(function (angular, vodModule) {
    'use strict';

    vodModule.factory('mensagensService', ['$log', '$http', '$q', function ($log, $http, $q) {
        $log.info('Mensagens Service initialized');
        var messages = [
            { 'id': 1, 'msg': 'Ciao sociis natoque penatibulus et auctor...' },
            { 'id': 2, 'msg': 'Vestibulum id ligula porta felis euismod ...' },
            { 'id': 3, 'msg': 'Nullam quis risus eget urna mollis ornare ...' },
            { 'id': 4, 'msg': 'Lorem ipsum dolor sit amet ...' },
            { 'id': 5, 'msg': 'Consectetur amurabilis maritatum lararirium' }
        ];
        var service = {
            'get': function get () {
                var deferred = $q.defer();
                deferred.resolve(messages);

                return deferred.promise;
            }
        };

        $log.info('Mensagens Service execution ended');

        return service;
    }]);

})(window.angular, window.angular.module('vod'));