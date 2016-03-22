angular
  .module('vod')
  .controller('TwitterCtrl', function ($http, $scope, $log, API_BASE_URL) {
     $scope.sendForm = function(num) {
    	 if (num != null && num != "") {
    		  var response = $http.get(API_BASE_URL + '/IPVOD/rest/twitter/auth/pin/'+num);
    	        response.success(function (responseData) {
    	        	window.location.href = responseData;
    	        });
    	        response.error(function (responseData) {
    	        	$log.info(responseData.errorMessage);
    	        });
    	        return;
    	 } else {
    		 return;
    	 }
     }; 
  });