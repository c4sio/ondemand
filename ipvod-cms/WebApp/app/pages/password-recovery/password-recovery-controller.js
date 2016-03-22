angular
  .module('vod')
  .controller('PasswordRecoveryCtrl', 
  ['$scope', '$routeParams', '$http', 'toaster', 'loginService',
  function ($scope, $routeParams, $http, toaster, loginService) {
	$scope.pwRec = {};
	$scope.recoverEmail = function(pwRec) {
		pwRec.pwRecCd = $routeParams.pwRec;
    	if (pwRec.password == ""  || pwRec.password == undefined ||
			pwRec.password2 == "" || pwRec.password2 == undefined ) {
    		return;
    	} else if (pwRec.password != pwRec.password2) {
    		toaster.pop({'type': 'error', 'title':'Senhas não conferem.'});
    		return;
    	} else {
    		loginService.restartPassword(pwRec);
    	}
    };
}]);
