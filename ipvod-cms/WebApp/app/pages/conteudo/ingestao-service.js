(function(angular, vodModule) {
	'use strict';

	vodModule.factory('ingestaoService', [
			'$log',
			'$q', 
			'$http',
			'$resource',
			'API_BASE_URL',
			function($log, $q, $http, $resource, API_BASE_URL) {
				$log.info('ingestaoService initialized');

                var filterParser = function filterParser (filterParams, urlPrefix) {
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
                };

				var get = function get(params, justCompleted) {

                    var makeUrl = function makeUrl (filterParams) {
                            var urlPrefix = justCompleted
                                            ? [API_BASE_URL + '/IPVOD/rest/ingest/revision?']
                                            : [API_BASE_URL + '/IPVOD/rest/ingest/execution?'],
                                url = filterParser(filterParams, urlPrefix);

                            return url;
                        },
                        url = makeUrl(params),
                        deferred = $q.defer();

					$log.info('Just completed: ' + justCompleted);

                    $http.get(url)
                    .success(function (data) {
                        deferred.resolve(data);
                    })
                    .error(function () {
                        deferred.reject();
                    });

                    return deferred.promise;
				};
				
				// Atualiza a prioridade do filme no processo de ingestão
				var updatePriority = function updatePriority(idIngest, priorityValue, returnFunction){
					var deferred = $q.defer();
					var req = {
						method : 'POST',
						url : API_BASE_URL + '/IPVOD/rest/ingest/priority',
						data : {
							id : idIngest,
							priority : priorityValue
						},
						headers : {
							'Content-Type' : 'application/json'
						}
					};
					
					$http(req).success(function(data) {
                         deferred.resolve(data);
                         if(returnFunction) {
                        	 returnFunction();
                         }
                     })
			        .error(function(msg) {
			        	deferred.reject(msg);
                        $log.error(msg, code);
			        }); 
			        return deferred.promise;
				};

				$log.info('ingestaoService execution ended');

				return {
					get : get,
					updatePriority : updatePriority
				};
			} ]);
})(window.angular, window.angular.module('vod'));