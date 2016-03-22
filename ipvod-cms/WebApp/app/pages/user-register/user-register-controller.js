angular.module('vod')
  .controller('UserRegisterCtrl', 
		  ['$scope', '$http', 'toasterMessage', 'userService', 'API_BASE_URL',
          function($scope, $http, toasterMessage,userService, API_BASE_URL) {
    $scope.registerTypes = ['GVT','OPERADORAS'];
    $scope.userRegister = {};
    $scope.userRegister.registerType = $scope.registerTypes[0];
    $scope.userRoles = ['VOC','MKT', 'ADM'];
    var promisse = $http.get(API_BASE_URL + '/IPVOD/rest/contentProvider');
    promisse.success(function (responseData) {
    	$scope.contentProviders = responseData;
    }); 
    
    $scope.reset = function(){
    	$scope.userRegister.username = "";
    	$scope.userRegister.password = "";
    	$scope.userRegister.passwordConfirm = "";
    	$scope.userRegister.email = "";
    	$scope.userRegister.role = $scope.userRoles[0];
    };
    $scope.reset();
    
    $scope.sendForm = function(userRegister) {
    	
    	if (userRegister.registerType == 'GVT') {
    		if (userRegister.username == "" || userRegister.username == undefined) {
    			return;
    		}
    		userRegister.contentProvider = null;
			userRegister.password = "";
			userRegister.passwordConfirm = "";
			userRegister.email = "";
    	} else {
    		if (userRegister.username == "" || userRegister.username == undefined ||
				userRegister.password == "" || userRegister.password == undefined ||
				userRegister.passwordConfirm == "" || userRegister.passwordConfirm == undefined ||
				userRegister.email == "" || userRegister.email == undefined ||
				userRegister.registerType == "" || userRegister.registerType == undefined) {
    			return;
    		}
    		if (userRegister.passwordConfirm != userRegister.password) {
    			toasterMessage.setMessage('passwordMismatch');
    			return;
    		}
    		userRegister.role = 'OPR';
    	}
    	userService.create(userRegister, function (data) {
			toasterMessage.setMessage(data.message);
		});
	};
}]);