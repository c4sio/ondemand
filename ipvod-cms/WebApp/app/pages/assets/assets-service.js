(function (angular, vodModule) {
    'use strict';
    
    vodModule.factory('assetsService', [
        '$log', 
        'helpers', 
        '$http', 
        '$q', 
        'API_BASE_URL', 
        function ($log, helpers, $http, $q, API_BASE_URL) {
        $log.info('assetsService initialized'); 

            var normalizeAndTrim = function normalizeAndTrim (str) {
                var normalizedString = helpers.stringNormalize(str).toLowerCase();
                return helpers.trim(normalizedString).replace(' ', '-');
            },
            filterParser = function filterParser (filterParams, urlPrefix) {
                var url = [];
                angular.forEach(filterParams, function (value, key) {
                    if ( key === 'filters' ) {
                        if ( typeof value !== 'object' ) {
                            url.push(key+'=' + value);
                            return;
                        } else {
                            url.push( key+'='+encodeURI(JSON.stringify(value)) );
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
            // Adds extra info to the server response
            categoriesParser = function categoriesParser (categories) {
                angular.forEach(categories, function (category) {
                    category.name = normalizeAndTrim(category.description);
                    category.url = category.name;
                });
                
                return categories;
            },

            service = {
                getCategories : function getCategories (params) {
                    var makeUrl = function makeUrl (filterParams) {
                            var urlPrefix = [API_BASE_URL + '/IPVOD/rest/category/complex?'],
                                url = filterParser(filterParams, urlPrefix);
                            return url;
                        }, 
                        url = makeUrl(params), 
                        deferred = $q.defer();

                     $http.get(url)
                     .success(function(data) {
                         var categories = categoriesParser(data.list);
                         data.list = categories;
                         deferred.resolve(data);
                     })
                     .error(function(msg, code) {
                         deferred.reject(msg);
                         $log.error(msg, code);
                     });
                     return deferred.promise;
                },
                getCategoryIdByName: function getCategoryIdByName (categoryName) {
                    var deferred = $q.defer();
                    this.getCategories().then(function (categories) {
                        angular.forEach(categories, function (category) {
                            if ( category.name === categoryName ) {
                                deferred.resolve(category.id);
                            }
                        });
                    });                
                    return deferred.promise;
                },

                getCategoryById: function getCategoryById (categoryId) {
                    var deferred = $q.defer();
                    this.getCategories().then(function (categories) {
                        angular.forEach(categories.list, function (category) {
                            if ( category.categoryId === parseInt(categoryId) ) {
                                deferred.resolve(category.description);
                            }
                        });
                    });
                    return deferred.promise;
                },

                getAssetsByCategory: function getAssetsByCategory (categoryId) {
                    var deferred = $q.defer();
                    $http.get(API_BASE_URL + '/IPVOD/rest/category/'+ categoryId +'/asset')
                    .success(function(data) {
                        deferred.resolve(data);
                    })
                    .error(function(msg, code) {
                        deferred.reject(msg);
                        $log.error(msg, code);
                    });

                    return deferred.promise;
                },

                getProvider: function getProvider () {
                    var deferred = $q.defer();

                    $http.get(API_BASE_URL + '/IPVOD/rest/contentProvider')
                    .success(function(data) {
                        deferred.resolve(data);
                    })
                    .error(function(msg, code) {
                        deferred.reject(msg);
                        $log.error(msg, code);
                    });

                    return deferred.promise;
                },
                
                getProviderUnique: function getProviderUnique () {
                    var deferred = $q.defer();

                    $http.get(API_BASE_URL + '/IPVOD/rest/contentProvider/unique')
                    .success(function(data) {
                        deferred.resolve(data);
                    })
                    .error(function(msg, code) {
                        deferred.reject(msg);
                        $log.error(msg, code);
                    });

                    return deferred.promise;
                },

                getHistory: function getAssetsByCategory (assetId) {
                    var deferred = $q.defer();
                    $http.get(API_BASE_URL + '/IPVOD/rest/history/asset/'+ assetId)
                    .success(function(data) {
                        deferred.resolve(data);
                    })
                    .error(function(msg, code) {
                        deferred.reject(msg);
                        $log.error(msg, code);
                    });

                    return deferred.promise;
                },

                getPackages: function getPackages (params, id) {
                    var deferred = $q.defer(), url;

                    if ( params ) {
                        var makeUrl = function makeUrl (filterParams) {
                            var urlPrefix = [API_BASE_URL + '/IPVOD/rest/package/complex?'];
                                url = filterParser(filterParams, urlPrefix);
                            return url;
                        };

                        url = makeUrl(params);                        
                    }

                    if ( id ) {
                        url = API_BASE_URL + '/IPVOD/rest/package/' + id;
                    }

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

                getPackageById: function getPackageById (packageId) {
                    return this.getPackages(null, packageId);
                },

                getPackageType: function getPackageType () {
                    var deferred = $q.defer(),
                        url = API_BASE_URL + '/IPVOD/rest/package/type';

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
                getAssetPackage: function getPackageType (params, id) {
			        var deferred = $q.defer(), url;
			
			        if ( params ) {
			            var makeUrl = function makeUrl (filterParams) {
			                var urlPrefix = [API_BASE_URL + '/IPVOD/rest/package/assetPackage?'];
			                    url = filterParser(filterParams, urlPrefix);
			                return url;
			            };
			
			            url = makeUrl(params);                        
			        }
			
			        if ( id ) {
			            url = API_BASE_URL + '/IPVOD/rest/package/' + id;
			        }
			
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

                deletePackage: function deletePackage (packageId) { 
                     var deferred = $q.defer();
                     // using bracket method invocation for IE8 compatability
                     $http['delete'](API_BASE_URL + '/IPVOD/rest/package/' + packageId)
                     .success(function(data) {
                         deferred.resolve(data);
                     })
                     .error(function(msg, code) {
                         deferred.reject(msg);
                         $log.error(msg, code);
                     });
                     
                     return deferred.promise;
                },

                getRating: function getRating () {
                    var deferred = $q.defer(),
                        url = API_BASE_URL + '/IPVOD/rest/rating';

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

                getAssetsFilter: function getAssetsFilter (params) {
                    var makeUrl = function makeUrl (filterParams) {
                            var urlPrefix = [API_BASE_URL + '/IPVOD/rest/asset?'],
                                url = filterParser(filterParams, urlPrefix);
                            return url;
                        }, 
                        url = makeUrl(params);

                    var deferred = $q.defer();
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

                getAllAssets: function getAllAssets () {
                    var deferred = $q.defer();
                    $http.get(API_BASE_URL + '/IPVOD/rest/asset')
                    .success(function(data) {
                        deferred.resolve(data);
                    })
                    .error(function(msg, code) {
                        deferred.reject(msg);
                        $log.error(msg, code);
                    });
                    
                    return deferred.promise;
                },
                deleteCategory: function deleteCategory (categoryId) { 
                	 var deferred = $q.defer();
                     // using bracket method invocation for IE8 compatability
                	 $http['delete'](API_BASE_URL + '/IPVOD/rest/category/' + categoryId)
                     .success(function(data) {
                         deferred.resolve(data);
                     })
                     .error(function(msg, code) {
                         deferred.reject(msg);
                         $log.error(msg, code);
                     });
                     
                     return deferred.promise;
                },
                save: function save (data, type) {
                    var deferred = $q.defer();
                    
                    
                    // recebe o token da session
    				var token = $http.defaults.headers.common['cms-token'];                                        
					                    

                     $http.post(API_BASE_URL + '/IPVOD/rest/' + type, data )
                     .success(function(data) {
                         deferred.resolve(data);
                     })
                     .error(function(msg, code) {
                         deferred.reject(msg);
                         $log.error(msg, code);
                     });
                     
                     return deferred.promise;
                },
                getAsset: function getAsset (id) {
                    var deferred = $q.defer();

                     $http.get(API_BASE_URL + '/IPVOD/rest/asset/' + id )
                     .success(function(data) {
                         deferred.resolve(data);
                     })
                     .error(function(msg, code) {
                         deferred.reject(msg);
                         $log.error(msg, code);
                     });
                     
                     return deferred.promise;
                },
                updateAsset: function updateAsset (data) {

					// if (data.price <= 0) {
					//   return;
					// }                	                	
                	
                    var deferred = $q.defer();

                     $http.post(API_BASE_URL + '/IPVOD/rest/asset', data )
                     .success(function(data) {
                         deferred.resolve(data);
                     })
                     .error(function(msg, code) {
                         deferred.reject(msg);
                         $log.error(msg, code);
                     });
                     
                     return deferred.promise;
                },
                saveTempAsset: function saveAsset (temp) {

					// if (data.price <= 0) {
					//   return;
					// }                	                	
                	
                    var deferred = $q.defer();
                    var assetId = temp.data.assetId;
                    temp.data = JSON.stringify(temp.data);  
                     $http.post(API_BASE_URL + '/IPVOD/rest/asset/save/'+assetId, temp )
                     .success(function(data) {
                         deferred.resolve(data);
                     })
                     .error(function(msg, code) {
                         deferred.reject(msg);
                         $log.error(msg, code);
                     });
                     
                     return deferred.promise;
                },
                cancelTempAsset: function saveAsset (data) {

                    var deferred = $q.defer();

                     $http.post(API_BASE_URL + '/IPVOD/rest/asset/cancel/'+data.assetId)
                     .success(function(data) {
                         deferred.resolve(data);
                     })
                     .error(function(msg, code) {
                         deferred.reject(msg);
                         $log.error(msg, code);
                     });
                     
                     return deferred.promise;
                },
                updateCategory: function updatecategory (categoryId, assetId, returnFunction) {

                    var deferred = $q.defer();

                     $http.post(API_BASE_URL + '/IPVOD/rest/asset/updatePartial/'+assetId+'?categoryId='+categoryId)
                     .success(function(data) {
                         deferred.resolve(data);
                         if(returnFunction) {
                        	 returnFunction();
                         }
                     })
                     .error(function(msg, code) {
                         deferred.reject(msg);
                         $log.error(msg, code);
                     });
                     
                     return deferred.promise;
                },
                updateBillingId: function updateBillingId (assetId, billingId, returnFunction) {

                    var deferred = $q.defer();

                     $http.post(API_BASE_URL + '/IPVOD/rest/asset/updatePartial/'+assetId+'?billingId='+billingId)
                     .success(function(data) {
                         deferred.resolve(data);
                         if(returnFunction) {
                        	 returnFunction();
                         }
                     })
                     .error(function(msg, code) {
                         deferred.reject(msg);
                         $log.error(msg, code);
                     });
                     
                     return deferred.promise;
                },
                updatePrice: function updatePrice (assetId, price, returnFunction) {

                    var deferred = $q.defer();

                     $http.post(API_BASE_URL + '/IPVOD/rest/asset/updatePartial/'+assetId+'?price='+price)
                     .success(function(data) {
                         deferred.resolve(data);
                         if(returnFunction) {
                        	 returnFunction();
                         }
                     })
                     .error(function(msg, code) {
                         deferred.reject(msg);
                         $log.error(msg, code);
                     });
                     
                     return deferred.promise;
                },
                finishRevision: function finishRevision (assetId, returnFunction) {

                    var deferred = $q.defer();

                     $http.post(API_BASE_URL + '/IPVOD/rest/asset/updatePartial/'+assetId+'?finishRevision=true')
                     .success(function(data) {
                         deferred.resolve(data);
                         if(returnFunction) {
                        	 returnFunction();
                         }
                     })
                     .error(function(msg, code) {
                         deferred.reject(msg);
                         $log.error(msg, code);
                     });
                     
                     return deferred.promise;
                },
                getRelationships: function getRelationships (id) {
                    var deferred = $q.defer();
                    $http.get(API_BASE_URL + '/IPVOD/rest/menu/asset/' + id)
                     .success(function(data) {
                         deferred.resolve(data);
                     })
                     .error(function(msg, code) {
                         deferred.reject(msg);
                         $log.error(msg, code);
                     });
                     return deferred.promise;
                },
                getLancamentos: function getLancamentos () {
                    var deferred = $q.defer(), 
                        lancamentos = [
                            {"order": 1, "title": "Mama", "assetId": 9200 },
                            {"order": 2, "title": "Lorem ipsum dolor", "assetId": 9300 },
                            {"order": 2, "title": "Lorem ipsum dolor sit amet", "assetId": 9400 }
                        ];
                    // $http.get(API_BASE_URL + '/IPVOD/rest/asset/release/list')
                    
                    deferred.resolve(lancamentos);

                    return deferred.promise;

                },
                updateLancamentos: function updateLancamentos (data) {
                    var deferred = $q.defer();

                    $http.post(API_BASE_URL + '/IPVOD/rest/asset/release/update', data)
                    .success(function(data) {
                        deferred.resolve(data);
                    })
                    .error(function(msg, code) {
                        deferred.reject(msg);
                    });

                    return deferred.promise;
                },
                getVisualMenu: function getVisualMenu (data) {
                    var deferred = $q.defer();

                    $http.get(API_BASE_URL + '/IPVOD/rest/menu/categorize')
                    .success(function(data) {
                        deferred.resolve(data);
                    })
                    .error(function(msg, code) {
                        deferred.reject(msg);
                    });
                    return deferred.promise;
                },
                removeMenus: function removeMenus (list) {
                    var deferred = $q.defer();

                    $http.post(API_BASE_URL + '/IPVOD/rest/menu/remove', list)
    	            .success(function(data) {
                        deferred.resolve(data);
                    })
                    .error(function(msg, code) {
                        deferred.reject(msg);
                    });
                    return deferred.promise;
                },
                insertMenus: function insertMenus (menuAsset) {
                    var deferred = $q.defer();

                    $http.post(API_BASE_URL + '/IPVOD/rest/menu/insert', menuAsset)
    	            .success(function(data) {
                        deferred.resolve(data);
                    })
                    .error(function(msg, code) {
                        deferred.reject(msg);
                    });
                    return deferred.promise;
                }
            };

        $log.info('assetsService execution ended');

        return service;
    }]);
    
})(window.angular, window.angular.module('vod'));