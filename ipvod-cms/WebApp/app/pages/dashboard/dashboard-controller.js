
(function (angular, vodModule) {
    'use strict';
    
    vodModule
    .controller('DashboardCtrl', ['$log', '$scope', '$location', 'toaster', 'overviewService', 'equipamentosService',
    function ($log, $scope, $location, toaster, overviewService, equipamentosService) {
        $log.info('DashboardCtrl initialized');
         
        // Overview component        
        // Callbacks
        overviewService.get(function successHandler (data) {
            $scope.overview = data;
        }, function errorHandler () {
            //toaster.pop({'type': 'error', 	'title': 'Houve um erro ao processar a requisição'});
        });

        equipamentosService.get()
        .then(function (data) {
            $scope.equipamentos = data;
        });

        $scope.setTab = function setTab (tab, index) {
            tab = index + 1;
            $scope.activeTab = tab;
        };
        $scope.activeTab = 1;

        $log.info('DashboardCtrl execution ended');
    }]);

})(window.angular, window.angular.module('vod'));