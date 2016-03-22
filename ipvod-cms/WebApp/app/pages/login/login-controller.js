angular
  .module('vod')
  .controller('LoginCtrl', [
    '$log', '$scope', '$window', '$http', '$location', 'toaster','API_BASE_URL', 'loginService', 'toaster', 'auth',
   function ($log, $scope, $window, $http, $location, toaster, API_BASE_URL, loginService, toaster, auth) {

    $scope.userLogin = {};
    $scope.userLogin.username = "";
    $scope.userLogin.password = "";
    $scope.userLogin.keepLoggedIn = false;
    $scope.login = function login (userLogin) {
            
        if (userLogin.username == "" || userLogin.username == undefined ||
            userLogin.password == "" || userLogin.password == undefined) {
            return;
        }

        loginService.login(userLogin,
            function loginOnSuccess (data) {
                // Auth session initialize
                auth
                .startSession(data, $scope.userLogin.keepLoggedIn)
                .then(function () {
                    // Redirect to home page
                    $window.location.href = $window.location.pathname.replace('-public','') + "#/dashboard";
                    // $window.location = "/vod.html#/dashboard";
                });
            },
            function loginOnError (data) {
                debugger;
                toaster.pop({'type': 'error', 'title': data.errorMessage});
            }
        );
    };

    $scope.userEmail = {};
    $scope.recoverEmail = function(userEmail) {
        if (userEmail.email == "" || userEmail.email == undefined) {
                return;
        }
        if (userEmail.email.indexOf("@gvt") != -1) {
          toaster.pop({'type': 'error', 'title': 'Favor abrir um chamado via SAN ou ligar 2626.'});
          return;
        }
        loginService.recoverEmail(userEmail,
            function (data) {
                toaster.pop({'type': 'error', 'title': data.errorMessage});
            }
        );
    };
}]);


jQuery(function($) {    
    $(document).on('click', '.toolbar a[data-target]', function(e) {
        e.preventDefault();
        var target = $(this).data('target');
        $('.widget-box.visible').removeClass('visible');//hide others
        $(target).addClass('visible');//show target
    });
});
