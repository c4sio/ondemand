(function(angular, vodModule) {
	'use strict';

	vodModule.controller('ConfMenuStbCtrl', [
			'$log',
			'$scope',
			'$location',
			'toaster',
			'overviewService',
			'API_BASE_URL',
			'$http',
			function($log, $scope, $location, toaster, overviewService,
					API_BASE_URL, $http) {
				$log.info('ConfMenuStbCtrl initialized');

				$scope.treedata = {};

				// Overview component
				var responsePromise = $http.get(API_BASE_URL
						+ "/IPVOD/rest/menu/show");

				responsePromise.success(function(dataFromServer, status,
						headers, config) {
					$scope.treedata = dataFromServer;
					$log.info(dataFromServer);
					$log.info(status);
				});
				responsePromise.error(function(data, status, headers, config) {
					toaster.setMessage(data.message);
				});

				$scope.$watch('treeMenu.currentNode', function(newObj, oldObj) {
					if ($scope.treeMenu
							&& angular.isObject($scope.treeMenu.currentNode)) {
						$log.info('Node Selected!!');
						$log.info($scope.treeMenu.currentNode);
					}
				}, false);

				$log.info('ConfMenuStbCtrl execution ended');
			} ]);

})(window.angular, window.angular.module('vod'));