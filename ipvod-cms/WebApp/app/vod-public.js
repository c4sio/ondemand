
(function (angular) {
    'use strict';
/**
 * Defines the module and routes for the app's public pages.
 */

// Modules definitions
angular
    .module('vod', [
    // Third-party dependencies
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'toaster'
    ])

    // Url setup
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider
          .when('/', {
            templateUrl: 'pages/index/index.html', 
            controller: 'IndexCtrl'
          })

          .when('/login', {
            templateUrl: 'pages/login/login.html',
            controller: 'LoginCtrl'
          })

          .when('/password', {
            templateUrl: 'pages/password-recovery/password-recovery.html',
            controller: 'PasswordRecoveryCtrl'
          })

          .when('/twitter', {
            templateUrl: 'twitter.html',
            controller: 'TwitterCtrl'
          })
          
          // TODO (mkretschek) redirect to a '404 Not Found' page
          .otherwise({
            redirectTo: '/'
          });
    }])

    // Make sure the user is not authenticated
    .run(['PROTECTED_PAGE_URL', '$log', '$window', 'auth', 
    function (PROTECTED_PAGE_URL, $log, $window, auth) { 
        if (auth.isAuthenticated()) {
            return $window.location.href = PROTECTED_PAGE_URL;
        }
    }]);

})(window.angular);