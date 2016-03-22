(function (angular, vodModule) {
    'use strict';
    
    vodModule
    .controller('AssetsCtrl', ['$log', '$scope', '$routeParams', 'toaster', '$http', 'API_BASE_URL',
    function ($log, $scope, $routeParams, toaster, $http, API_BASE_URL) {
        $log.info('AssetsCtrl initialized');
        $scope.assets = [];
        $scope.categoria = '';

        var response = $http.get(API_BASE_URL + '/IPVOD/rest/category/'+ $routeParams.categ +'/asset/page/1');
        response.success(function (responseData) {
        	 $scope.assets = responseData.ipvodAssets1;
        	 var paginas = responseData.ipvodAssetsCount / 20;
        	 $scope.paginas = [];
        	 for (var i = 0; i<paginas; i++ ) {
        		 $scope.paginas[i] = {
        				 num: i+1
        		 };
        	 }
        	 if ($scope.assets.length === 0) {
        		 toaster.pop({'type': 'error', 	'title': 'Não existem Assets para a categoria ' + responseData.description});
        	 }
             $scope.categoria = responseData.description;
        });
        response.error(function (responseData) {
        	$log.info(responseData.errorMessage);
        });

        $scope.paginar = function (page) {
        	var response = $http.get(API_BASE_URL + '/IPVOD/rest/category/'+ $routeParams.categ +'/asset/page/'+page);
            response.success(function (responseData) {
            	 $scope.assets = responseData.ipvodAssets1;
            });
            response.error(function (responseData) {
            	$log.info(responseData.errorMessage);
            });
        };
        $log.info('AssetsCtrl execution ended');
    }]);

})(window.angular, window.angular.module('vod'));