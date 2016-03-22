(function(angular, vodModule) {
    'use strict';

    vodModule.factory('assinantesService', ['$log', '$http', '$q', '$cookieStore', 'API_BASE_URL',
    function($log, $http, $q, $cookieStore, API_BASE_URL) {
        var 
            filterParser = function filterParser (filterParams, urlPrefix) {
                var url = [];
                angular.forEach(filterParams, function (value, key) {
                    if ( key === 'filters' ) {
                        if ( typeof value !== 'object' ) {
                            url.push(key+'=' + value);
                            return;
                        } else {
                            url.push( key+'='+window.escape(JSON.stringify(value)) );
                        }
                    }
                    else {
                        if ( typeof value !== 'object' ) {
                            url.push(key+'=' + value);
                            return;
                        }
                        filterParser(value);
                    }
                });
                url = [url.join('&')];
                return urlPrefix.concat(url).join('');
            },

            service = {
                get : function get (params) {
                    var makeUrl = function makeUrl (filterParams) {
                            var urlPrefix = [API_BASE_URL + '/IPVOD/rest/user/list?'],
                                url = filterParser(filterParams, urlPrefix);
                            return url;
                        }, 
                        url = makeUrl(params), 
                        deferred = $q.defer();

                     $http.get(url)
                     .success(function(data) {
                         deferred.resolve(data);
                     })
                     .error(function(msg, code) {
                         deferred.reject(msg);
                         $log.error(msg, code);
                     });
                     return deferred.promise;
                },
                getById: function getById (id) {
                    if ( !id ) {
                        $log.error('Id não informado');
                        return false;
                    }
                    var deferred = $q.defer();

                    $http.get(API_BASE_URL + '/IPVOD/rest/services/subscriber/' + id,
                        {
                            data: '',
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }
                    )
                    .success(deferred.resolve)
                    .error(function(msg, code) {
                        deferred.reject(msg);
                        $log.error(msg, code);
                    });
                    return deferred.promise;
                },
                getEquipments: function getEquipments () {
                    var deferred = $q.defer();
                    $http.get(API_BASE_URL + '/IPVOD/rest/equipment/types',
                        {
                            data: '',
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }
                    )
                    .success(deferred.resolve)
                    .error(function(msg, code) {
                        deferred.reject(msg);
                        $log.error(msg, code);
                    });

                    return deferred.promise;
                },
                createEquipment: function createEquipment (id, data) {
                    var deferred = $q.defer(), obj = JSON.parse(JSON.stringify(data));

                    obj[0].type = obj[0].typeDesc.equipmentTypeId;
                    delete obj[0].typeDesc;

                    $http.post(API_BASE_URL + '/IPVOD/rest/services/subscriber/' + id + '/equipment', obj)
                    .success(deferred.resolve)
                    .error(deferred.reject);

                    return deferred.promise;
                },
                removeEquipment: function removeEquipment (crmCustomerId, serial) {
                    var deferred = $q.defer();

                    $http.delete(API_BASE_URL + '/IPVOD/rest/services/subscriber/'+crmCustomerId + '/equipment/'+serial,
                        {
                            data: '',
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }
                    )
                    .success(deferred.resolve)
                    .error(deferred.reject);

                    return deferred.promise;
                },
                getAllPackages: function getAllPackages () {
                    var deferred = $q.defer();

                    $http.get(API_BASE_URL + '/IPVOD/rest/package/complex')
                    .success(deferred.resolve)
                    .error(deferred.reject);

                    return deferred.promise;
                },
                savePackages: function savePackages (crmCustomerId, data) {
                    var deferred = $q.defer();

                    $http.post(API_BASE_URL + '/IPVOD/rest/services/subscriber/'+ crmCustomerId + '/product', data)
                    .success(deferred.resolve)
                    .error(deferred.reject);

                    return deferred.promise;
                },
                removePackage: function removePackage (crmCustomerId, otherId) {
                    var deferred = $q.defer();

                    $http.delete(API_BASE_URL + '/IPVOD/rest/services/subscriber/'+crmCustomerId + '/product/' + otherId,
                        {
                            data: '',
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }
                    )
                        .success(deferred.resolve)
                        .error(deferred.reject);

                    return deferred.promise;
                }
            };
        
        return service;
    }]);
    
 })(window.angular, window.angular.module('vod'));