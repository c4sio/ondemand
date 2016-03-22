(function(angular, vodModule) {
    'use strict';

    vodModule.factory('userService', ['$http', '$cookieStore', 'API_BASE_URL', 'toasterMessage',
                                       function($http, $cookieStore, API_BASE_URL, toasterMessage) {
        var create = function (userRegister, messageFunction) {
        	var responsePromise = $http.put(API_BASE_URL + "/IPVOD/rest/user", userRegister, {});

            if ( typeof messageFunction === 'function' ) { 
            	responsePromise.success(function (responseData) {
            		messageFunction(responseData);
            	});
            
            	responsePromise.error(function (responseData) {
            		messageFunction(responseData);
            	});
            }
        };
        
        return { create : create };
    }]);
    
 })(window.angular, window.angular.module('vod'));