angular.module('vod')
  .controller('PurchaseCtrl', 
		  ['$scope', '$http', 'API_BASE_URL', 'toaster',
          function($scope, $http, API_BASE_URL, toaster) {
			  $scope.getPurchases = function (date){
				  console.log(date);
				  if (date != null) {
					  var responsePromise = $http.get(API_BASE_URL + "/IPVOD/rest/purchase/"+date);
	
					  responsePromise.success(function (responseData) {
						  $scope.purchases = responseData;
					  });
	            
					  responsePromise.error(function (responseData) {
						  toaster.pop({'type': 'error', 'title': responseData.message});
					  });
				  } else {
					  toaster.pop({'type': 'error', 'title': 'Data deve ser preenchida.'});
				  }
			  };

			  $scope.downloadFile = function (date){
				  console.log(date);
				  if (date != null) {
					  window.open(API_BASE_URL + "/IPVOD/rest/purchase/download/"+date);
				  } else {
					  toaster.pop({'type': 'error', 'title': 'Data deve ser preenchida.'});
				  }
//				  var responsePromise = $http.get(API_BASE_URL + "/IPVOD/rest/purchase/download/"+new Date(date).getTime());
//
//				  responsePromise.success(function (responseData) {
//				  });
//            
//				  responsePromise.error(function (responseData) {
//					  toaster.pop({'type': 'success', 'title': responseData.message});
//				  });
			  };
}]);