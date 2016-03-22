(function (angular, vodModule) {
	'use strict';

    vodModule
    .factory('equipamentosService', ['$log', '$resource', 'API_BASE_URL', '$q', function($log, $resource, API_BASE_URL, $q) {
        $log.info('equipamentosService initialized');
        
        // Statuses
        // 0: DOWN
        // 1: UP
        // 2: WORKING
        var equipamentos = [
            { 'id': 1, 'name': 'Equipamento 1', 'status': 0 },
            { 'id': 2, 'name': 'Equipamento 2', 'status': 1 },
            { 'id': 3, 'name': 'Equipamento 3', 'status': 2 },
            { 'id': 4, 'name': 'Equipamento 4', 'status': 1 }
        ];

        var parse = function parse () {
                equipamentos.forEach(function(val) {
                    if ( val.status === 0 ) { 
                        val.icon = 'fa-thumbs-o-down icon-animated-vertical'; 
                        val.statusText = 'DOWN'; 
                        val.class = 'btn-danger'; 
                    }
                    if ( val.status === 1 ) { 
                        val.icon = 'fa-thumbs-o-up icon-animated-vertical'; 
                        val.statusText = 'UP'; 
                        val.class = 'btn-success'; 
                    }
                    if ( val.status === 2 ) { 
                        val.icon = 'fa-cog fa-spin';
                        val.statusText = 'BUSY';
                        val.class = 'btn-warning';
                    }
                });
            },
            service = {
                get: function get (id) {
                    var equipamento = null, 
                        deferred = $q.defer();

                    parse();

                    if ( id ) {
                        for ( var i in equipamentos ) {
                            if ( equipamentos[i].id === id ) { 
                                equipamento = equipamentos[i];
                                deferred.resolve(equipamento);
                                break;
                            }
                        }
                    }

                    else {
                        deferred.resolve(equipamentos);
                    }

                    return deferred.promise;
                }
            };

        
        $log.info('equipamentosService execution ended');
        
        return service;
    }]);

})(window.angular, window.angular.module('vod'));