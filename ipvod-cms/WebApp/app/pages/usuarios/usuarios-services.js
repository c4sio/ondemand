(function(angular, vodModule) {
    'use strict';

    vodModule.factory('usuariosService', ['$http', '$cookieStore', 'API_BASE_URL',
                                       function($http, $cookieStore, API_BASE_URL) {
        var users = [],
            get = function get (id) {
                if ( id ) { return getById(id); }
                return users;
            },
            getById = function getById (id) {
            	var deferred = $q.defer();
    	        $http.get(API_BASE_URL + '/IPVOD/rest/user' + id)
    	        .success(function(data) {
    	            deferred.resolve(data);
    	        })
    	        .error(function(msg, code) {
    	            deferred.reject(msg);
    	            $log.error(msg, code);
    	        });
    	        
    	        return deferred.promise;
            },
            create = function (userRegister, messageFunction) {
	        	var responsePromise = $http.post(API_BASE_URL + "/IPVOD/rest/user/users", userRegister, {});
	
	            if ( typeof messageFunction === 'function' ) { 
	            	responsePromise.success(function (responseData) {
	            		messageFunction(responseData);
	            	});
	            
	            	responsePromise.error(function (responseData) {
	            		messageFunction(responseData);
	            	});
	            }
	            
            },
            getUserList = function () {
    	        var deferred = $q.defer();
    	        $http.get(API_BASE_URL + '/IPVOD/rest/user')
    	        .success(function(data) {
    	            deferred.resolve(data);
    	        })
    	        .error(function(msg, code) {
    	            deferred.reject(msg);
    	            $log.error(msg, code);
    	        });
    	        
    	        return deferred.promise;
            };
        
        return { 
            create : create,
            get: get,
            getUserList : getUserList
        };
    }]);
    
 })(window.angular, window.angular.module('vod'));