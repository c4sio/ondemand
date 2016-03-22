(function(angular, vodModule) {
    'use strict';

    vodModule.factory('overviewService', ['$log', '$resource', '$http', 'API_BASE_URL', function($log, $resource, $http, API_BASE_URL) {
        $log.info('Overview Service initialized');
        var get = function (successCallback, errorCallback) {
               var typeHandler = {
        		    'assinantes': { 'icon': 'ace-icon fa fa-group', 'cssClass': 'infobox-green', 'label': 'novos assinantes' },
        		    'logon': { 'icon': 'ace-icon fa fa-sign-in', 'cssClass': 'infobox-blue', 'label': 'Acessos' },
                    'conteudo': { 'icon': 'ace-icon fa fa-film', 'cssClass': 'infobox-purple', 'label': 'novos conteúdos' },
                    'audiencia': { 'icon': 'ace-icon fa fa-desktop', 'cssClass': 'infobox-blue', 'label': 'audiência média diária' },
                    'hits': { 'icon': 'ace-icon fa fa-bullseye', 'cssClass': 'infobox-pink', 'label': 'hits totais' },
                    'pacotes': { 'icon': 'ace-icon fa fa-dollar', 'cssClass': 'infobox-red', 'label': 'pacotes adquiridos' },
                    'minutos': { 'icon': 'ace-icon fa fa-clock-o', 'cssClass': 'infobox-blue', 'label': 'minutos' },
                    'googleplus': { 'icon': 'ace-icon fa fa-google-plus', 'cssClass': 'infobox-red', 'label': '+1s' },
                    'facebook': { 'icon': 'ace-icon fa fa-facebook', 'cssClass': 'infobox-blue', 'label': 'likes and shares' },
                    'tweets': { 'icon': 'ace-icon fa fa-twitter', 'cssClass': 'infobox-blue', 'label': 'tweets' }
                },
                
                // Parses the raw data from the server, adding to it new info related to screen rendering
                parse = function parse (rawData) {
                    angular.forEach(rawData, function (value) {
                        value.icon = typeHandler[value.type].icon;
                        value.cssClass = typeHandler[value.type].cssClass;
                        value.variationCssClass = typeHandler[value.type].variationCssClass = (value.variation > 0) ? 'stat-success' : 'stat-important';
                        value.variationIndicator = typeHandler[value.type].variationIndicator = (value.variation > 0) ? '+' : '';
                        value.label = typeHandler[value.type].label = typeHandler[value.type].label;

                        if ( value.variation > 0 ) {
                            value.variationCssClass = typeHandler[value.type].variationCssClass = 'stat-success';
                        }
                        else if ( value.variation < 0 ) {
                            value.variationCssClass = typeHandler[value.type].variationCssClass = 'stat-important';
                        }
                        else { 
                            value.variationCssClass = typeHandler[value.type].variationCssClass = 'hide';
                        }
                    });

                    return rawData;
                },
                
                // Retrieves data from the server
                retrieve1 = function retrieve1 () {
                    // $http.defaults.headers.common.foo = 'bar';
                    var overview = $http.get(API_BASE_URL + '/IPVOD/rest/dashboard/overview'),
                    // var overview = $http({method: 'GET', url: API_BASE_URL + '/IPVOD/rest/dashboard/overview', 
                    //     headers: {'fuck' : 'you'} }),
                        parsedData = null;

                    // Error Handler
                    if ( typeof successCallback === 'function' ) { 
                        overview.success(function successHandler (rawData) {
                            parsedData = parse(rawData);
                            successCallback(rawData);
                        });
                    }

                    // Success Handler
                    if ( typeof errorCallback === 'function' ) { 
                        overview.error(function errorHandler (rawData) {
                            errorCallback(rawData);
                        }); 
                    }
                },
             // Retrieves data from the server
                retrieve = function retrieve () {
                    var parsedData = [];
                    var counter = 0;
                    var rawData = [];
                    getOverviewNewUsers(parsedData, parse);
                    getOverviewNewAssets(parsedData, parse);
//                    getOverviewLogonRegion(parsedData, parse);
//                    getOverviewTotalLogonByRegion(parsedData, parse);
//                    getOverviewPurchaseByRegion(parsedData, parse);
                    getOverviewMinutesPlayedByRegion(parsedData, parse);
                    
                    // Error Handler
                    if ( typeof successCallback === 'function' ) { 
                    	successCallback(parsedData);
                    }

                    // Success Handler
                    if ( typeof errorCallback === 'function' ) { 
                    	errorCallback(parsedData);
                    }
                };
            
            return retrieve();
        };
        $log.info('Overview Service execution ended');
        
        var getOverviewNewUsers = function(rawData, parse) {
        	var newUsers = $http.get(API_BASE_URL + '/IPVOD/rest/dashboard/overview/newUsers');
            newUsers.success(function successHandler (resp) {
            	rawData.push(parse(resp)[0]);
            });
            newUsers.error(function errorHandler (resp) {
            }); 
        };
        var getOverviewNewAssets = function(rawData, parse) {
	        var newAssets = $http.get(API_BASE_URL + '/IPVOD/rest/dashboard/overview/newAssets');
	        newAssets.success(function successHandler (resp) {
	        	rawData.push(parse(resp)[0]);
	        });
	        newAssets.error(function errorHandler (resp) {
	        });
        };
        var getOverviewLogonRegion = function(rawData, parse) {
        	var logonRegion = $http.get(API_BASE_URL + '/IPVOD/rest/dashboard/overview/logonRegion');
            logonRegion.success(function successHandler (resp) {
            	rawData.push(parse(resp)[0]);
            });
            logonRegion.error(function errorHandler (resp) {
            }); 
        };
        var getOverviewTotalLogonByRegion = function(rawData, parse) {
        	var totalLogonByRegion = $http.get(API_BASE_URL + '/IPVOD/rest/dashboard/overview/totalLogonByRegion');
            totalLogonByRegion.success(function successHandler (resp) {
            	rawData.push(parse(resp)[0]);
            });
            totalLogonByRegion.error(function errorHandler (resp) {
            }); 
        };
        var getOverviewPurchaseByRegion = function(rawData, parse) {
        	var purchaseByRegion = $http.get(API_BASE_URL + '/IPVOD/rest/dashboard/overview/purchaseByRegion');
            purchaseByRegion.success(function successHandler (resp) {
            	rawData.push(parse(resp)[0]);
            });
            purchaseByRegion.error(function errorHandler (resp) {
            }); 
        };
        var getOverviewMinutesPlayedByRegion = function(rawData, parse) {
        	var minutesPlayedByRegion = $http.get(API_BASE_URL + '/IPVOD/rest/dashboard/overview/minutesPlayedByRegion');
            minutesPlayedByRegion.success(function successHandler (resp) {
            	rawData.push(parse(resp)[0]);
            });
            minutesPlayedByRegion.error(function errorHandler (resp) {
            }); 
        };
        
        return { get: get };
    }]);
    
 })(window.angular, window.angular.module('vod'));