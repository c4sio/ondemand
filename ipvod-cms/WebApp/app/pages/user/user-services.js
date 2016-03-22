(function(angular, vodModule) {
    'use strict';

    vodModule.factory('userService', [
    	'$http', 
    	'$cookieStore', 
    	'API_BASE_URL', 
    	'toaster', 
    	'$q', 
    	'$log', 
		function($http, $cookieStore, API_BASE_URL, toaster, $q, $log) {

		$log.info('userService initialized');

        var create = function (userRegister, messageFunction) {
        	var responsePromise = $http.post(API_BASE_URL + '/IPVOD/rest/user', userRegister, {});

            if ( typeof messageFunction === 'function' ) { 
            	responsePromise.success(function (responseData) {
            		messageFunction(responseData);
            	});
            
            	responsePromise.error(function (responseData) {
            		toaster.pop({'type': 'error', 'title': responseData.errorMessage});
            	});
            }
        },
        getById = function getById (id) {
        	var deferred = $q.defer();
	        $http.get(API_BASE_URL + '/IPVOD/rest/user/' + id)
	        .success(function(data) {
	            deferred.resolve(data);
	        })
	        .error(function(msg) {
	            deferred.reject(msg);
	            toaster.pop({'type': 'error', 'title': msg.errorMessage});
	        });
	        
	        return deferred.promise;
        },
        getUserList = function () {
	        var deferred = $q.defer();
	        $http.get(API_BASE_URL + '/IPVOD/rest/user')
	        .success(function(data) {
	            deferred.resolve(data);
	        })
	        .error(function(msg) {
	            deferred.reject(msg);
	            toaster.pop({'type': 'error', 'title': msg.errorMessage});
	        });
	        
	        return deferred.promise;
        },
        deactivateUser = function (id) {
        	return userActivation(id, 'deactivate');
        },
        activateUser = function (id) {
        	return userActivation(id, 'activate');
        },
        userActivation = function (id, status) {
        	var deferred = $q.defer();
	        $http.post(API_BASE_URL + '/IPVOD/rest/user/' + id + '/' + status)
	        .success(function(data) {
	            deferred.resolve(data);
	        })
	        .error(function(msg) {
	            deferred.reject(msg);
	            toaster.pop({'type': 'error', 'title': msg.errorMessage});
	        }); 
	        return deferred.promise;
        },
        deleteUser = function (id) {
        	var deferred = $q.defer();
        	// using bracket method invocation for IE8 compatability
	        $http['delete'](API_BASE_URL + '/IPVOD/rest/user/' + id)
	        .success(function(data) {
	            deferred.resolve(data);
	        })
	        .error(function(msg) {
	            deferred.reject(msg);
	            toaster.pop({'type': 'error', 'title': msg.errorMessage});
	        }); 
	        return deferred.promise;
        };
        
        $log.info('userService execution ended');

        return {
			create : create,
    		getUserList : getUserList,
    		getById : getById, 
    		deactivateUser : deactivateUser,
    		activateUser : activateUser,
    		deleteUser : deleteUser
    	};
    }]);
    
 })(window.angular, window.angular.module('vod'));