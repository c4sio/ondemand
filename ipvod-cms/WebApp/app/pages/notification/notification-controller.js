angular.module('vod')
  .controller('NotificationCtrl', 
['$scope', '$location', '$http', '$routeParams', '$modal', 'API_BASE_URL', 
function($scope, $location, $http, $routeParams, $modal, API_BASE_URL) {
    'use strict';

    var response = $http.get(API_BASE_URL + '/IPVOD/rest/notification/'+$routeParams.notificationId);
    response.success(function (responseData) {
    	 $scope.notification = responseData;
    	 $('#notificationBody')[0].innerHTML = responseData.text;
    });
    response.error(function (responseData) {
    	$log.info(responseData.errorMessage);
    });
}]);