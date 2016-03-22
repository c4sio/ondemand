(function(angular, vodModule) {
    'use strict';

    vodModule
    .factory('loginService', [
        '$http', 
        '$cookieStore', 
        'API_BASE_URL', 
        'toaster',
        function($http, $cookieStore, API_BASE_URL, toaster) {

        var login = function (userLogin, successFunction, errorFunction) {
			var dataObj = {
				username : userLogin.username,
				password : userLogin.password	
			};
        	var responsePromise = $http.post(API_BASE_URL + "/IPVOD/rest/user/login", dataObj, {});
        	// Success Handler
            if ( typeof successFunction === 'function' ) { 
            	responsePromise.success(function (responseData) {
            		$cookieStore.put("session-key", responseData.token);
            		$cookieStore.put("keepLoggedIn", userLogin.keepLoggedIn);
            		successFunction(responseData);
            	});
            }
            
            // Error Handler
            if ( typeof errorFunction === 'function' ) { 
            	responsePromise.error(function (responseData) {
            		toaster.pop({'type': 'error', 'title': responseData.errorMessage});
            	});
            }
        };
        
        var recoverEmail = function (userEmail) {
        	var responsePromise = $http.get(API_BASE_URL + "/IPVOD/rest/user/recoverPassword/" + userEmail.email);
        	responsePromise.success(function(data) {
        		toaster.pop({'type': 'success', 'title': 'E-mail para redefinição de senha enviado.'});
			});
			responsePromise.error(function(data) {
				toaster.pop({'type': 'success', 'title': data.errorMessage});
			});
        };
        
        var restartPassword = function (pwRec) {
        	var dataObject = {
    				passwordRecoverCode : pwRec.pwRecCd,
    				ipvodUserSys : {
    					password :  pwRec.password
    				}
    		};
    		
    		var responsePromise = $http.post(API_BASE_URL + "/IPVOD/rest/user/restartPassword", dataObject, {});
	        responsePromise.success(function(data) {
	        	toaster.pop({'type': 'success', 'title': 'Senha redefinida com sucesso.'});
    		});
    		responsePromise.error(function(data) {
    			toaster.pop({'type': 'error', 'title': data.errorMessage});
    		});
    		
        };
        return { login : login,
        		 recoverEmail : recoverEmail,
        		 restartPassword : restartPassword
        };
    }]);
    
 })(window.angular, window.angular.module('vod'));