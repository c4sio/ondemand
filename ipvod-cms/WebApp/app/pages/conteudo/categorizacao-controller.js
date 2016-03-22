(function(angular, vodModule) {
    'use strict';
    
    vodModule.controller('CategorizacaoCtrl', [
        '$log', 
        '$window',
        '$scope', 
        'assetsService', 
        'toaster', 
        'breadcrumbService',
        '$routeParams',
        '$http',
        'API_BASE_URL',
        '$timeout',
        function($log, $window, $scope, assetsService, toaster, breadcrumbService, $routeParams, $http, API_BASE_URL,$timeout) {
        $log.info('CategorizacaoCtrl initialized');
        var assetId = $routeParams.asset,
            nullToEmptyText = function nullToEmptyText (data) {
                for ( var key in data ) {
                    if ( data.hasOwnProperty(key) ) { 
                        if ( data[key] === null || data[key] === '' ) { data[key] = 'Vazio'; }
                    }
                }
                return data;
            },
            parseCanResumeCombobox = function parseCanResumeCombobox (canResumeOptions, d) {
                canResumeOptions.forEach(function (value, key) {
                    if ( value.val === d.canResume ) { d.canResume = $scope.canResumeOptions[key]; }
                });
            };
        $scope.menus = [];
        assetsService.getVisualMenu()
            .then(function (data) {
            	$scope.menus = data;
            	$scope.loadMenuBreadcrumbs();
            });
        $scope.loadAssetData = function () {

            $scope.toggleItemVisibility = function toggleItemVisibility (event) {
                event.stopImmediatePropagation();
                var $el = angular.element(event.currentTarget),
                    hasChildren = $el.find('.tree-branch-children').eq(0).children().length,
                    $parentItem = $el.closest('li.tree-branch'),
                    isOpen = $parentItem.hasClass('tree-open');

                if ( isOpen ) { 
                    $el.find('.icon-folder').eq(0)
                    .removeClass('tree-minus').addClass('tree-plus');
                } else {
                    $el.find('.icon-folder').eq(0)
                    .removeClass('tree-plus').addClass('tree-minus');
                }

                if ( !hasChildren ) { return; }

                $el.toggleClass('tree-open')
                .find('.tree-branch-children').eq(0)
                .toggleClass('hide');
            };

	    assetsService.getAsset(assetId)
            .then(function (data) {
            	if (data.temp != null) {
            		$scope.temp = data.temp; 
            		if ($scope.temp.scheduleId != 0) {
            			toaster.pop({ 'type': 'warning',  'title': 'Asset com alteração não publicada'});
            		}
            	} else {
            		$scope.temp = {};
            	}
                data = nullToEmptyText(data.asset);
                $scope.getHistory(assetId);
                $scope.canResumeOptions = [{ 'label': 'Não', 'val': false }, { 'label': 'Sim', 'val': true }]; 

                // Can Resume model: parsing for ng-options
                parseCanResumeCombobox($scope.canResumeOptions, data);

                $scope.edit = function edit (id) {
                    var $el = angular.element('#'+id),
                        selectElementText = function selectElementText () {
                            var range, sel;
                            range = document.createRange();
                            range.selectNodeContents($el.get(0));

                            sel = window.getSelection();
                            sel.removeAllRanges();
                            sel.addRange(range);
                        };

                    selectElementText();
                };
            	if (!isNaN(data.licenseWindowStart)) {
            		var date = new Date(data.licenseWindowStart);
            		var day = date.getDate();
            	    var month = date.getMonth() +1;
            	    if (month < 10)
            	    	month = "0" + month;
            	    var year = date.getFullYear();
            	    data.licenseWindowStart = year + "-" + month + "-" + day;
            	}
            	if (!isNaN(data.licenseWindowEnd)) {
            		var date = new Date(data.licenseWindowEnd);
            		var day = date.getDate();
            	    var month = date.getMonth() +1;
            	    if (month < 10)
            	    	month = "0" + month;
            	    var year = date.getFullYear();
            	    data.licenseWindowEnd = year + "-" + month + "-" + day;
            	}
            	$scope.asset = data;
				$scope.oldMenus = data.ipvodVisualMenus;

                assetsService.getProvider()
     	        .then(function (responseData) {     	            $scope.contentProviders = responseData;
     	            for (var i = 0; i< $scope.contentProviders.length; i++){
                         if ( $scope.asset.ipvodContentProvider && ($scope.asset.ipvodContentProvider.contentProviderId == $scope.contentProviders[i].contentProviderId ) ) {
                             $scope.asset.ipvodContentProvider = $scope.contentProviders[i];
                             break;
                         }
                     }
     	        });

     	        var promisse = $http.get(API_BASE_URL + '/IPVOD/rest/rating');
    	        promisse.success(function (responseData) {
    	            $scope.ratings = responseData;
    	            for (var i = 0; i< $scope.ratings.length; i++){
                        if ( $scope.asset.rating && ($scope.asset.rating.ratingLevel == $scope.ratings[i].ratingLevel ) ) {
                            $scope.asset.rating = $scope.ratings[i];
                            break;
                        }
                    }
    	        });

    	        if (data.ipvodMediaAssets) {
    	        	for (var i = 0; i < data.ipvodMediaAssets.length; i++) {
    	        		if (data.ipvodMediaAssets[i].ipvodMediaType != null && data.ipvodMediaAssets[i].ipvodMediaType.mediaTypeId == 1) {
    	        			$scope.mediaAsset = data.ipvodMediaAssets[i];
    	        			break;
    	        		}
    	        	}
    	        }
            })
            .catch(function (msg) {
                toaster.pop({'type': 'error',  'body': msg.errorMessage});
            });

            assetsService.getRelationships(assetId)
            .then(function (data) {
                $scope.relationships = data;
            });

	        // Breadcrumb Update
	        breadcrumbService.add([
	            {'label': 'Conteúdo', 'href': 'javascript: void(0);'},
	            {'label': 'Categorização'} 
	        ]);
        
        }
        if (assetId) {
        	 $scope.loadAssetData();
        }
        $scope.diffLabels = [];
        $scope.getFormattedlabel = function(label) {
        	
        	if($scope.diffLabels.length == 0) {
        		$scope.diffLabels["licenseWindowStart"] = "Início";
        		$scope.diffLabels["licenseWindowEnd"] = "Fim";
        		$scope.diffLabels["releaseYear"] = "Ano Lançamento";
        		$scope.diffLabels["fileSize"] = "Tamanho";
        		$scope.diffLabels["languages"] = "Linguagens";
        		$scope.diffLabels["title"] = "Título";
        		$scope.diffLabels["description"] = "Descrição";
        		$scope.diffLabels["isHD"] = "HD";
        		$scope.diffLabels["dubbedLanguage"] = "Dublagens";
        		$scope.diffLabels["country"] = "País";
        		$scope.diffLabels["audioType"] = "Áudio";
        		$scope.diffLabels["canResume"] = "Pode Retomar";
        		$scope.diffLabels["product"] = "Produto";
        		$scope.diffLabels["creationDate"] = "Data de Criação";
        		$scope.diffLabels["price"] = "Valor";
        		$scope.diffLabels["billingID"] = "Billing ID";
        		$scope.diffLabels["rating"] = "Classificação";
        		$scope.diffLabels["isRevised"] = "Revisado";
        		$scope.diffLabels["totalTime"] = "Tempo";
        		$scope.diffLabels["episodeName"] = "Nome Episódio";
        		$scope.diffLabels["subtitles"] = "Legendas";
        		$scope.diffLabels["director"] = "Diretor";
        	}
        	
        	return $scope.diffLabels[label];
        	
        }
		
		$scope.diff = [];
		
		$scope.dialogUser;
		$scope.dialogDate;
		
		$scope.openDiff = function(history) {
			
			$scope.dialogUser = history.user.username;
			$scope.dialogDate = history.changedAt;
			
			$scope.diff = [];
			
			if(!history.newValue.rating) {
				eval("history.newValue = " + history.newValue);
				eval("history.oldValue = " + history.oldValue);
			}
			
			for (var prop in history.newValue) {
				//formatting values
				if(prop == "rating") {
					if(history.oldValue[prop].rating) {
						history.oldValue[prop] = history.oldValue[prop].rating;
						history.newValue[prop] = history.newValue[prop].rating;
					}
				} else if(history.oldValue[prop].toString().toLowerCase() == "true") {
					history.oldValue[prop] = "Sim";
					history.newValue[prop] = "Sim";
				} else if(history.oldValue[prop].toString().toLowerCase() == "false") {
					history.oldValue[prop] = "Não";
					history.newValue[prop] = "Não";
				}
				
				var __ = {
						"prop" : $scope.getFormattedlabel(prop),
						"oldValue" : history.oldValue[prop],
						"newValue" : history.newValue[prop],
						"isDiferent" : (history.oldValue[prop] != history.newValue[prop])
					}
				$scope.diff.push(__);

				//alert(angular.toJson(__));
			}
			
		}
		
        $scope.assetHistory = [];
        $scope.getHistory = function(assetId) {
        	
        	assetsService.getHistory(assetId)
            .then(
    			function (data) {
    				//alert(angular.toJson(data));
    				$scope.assetHistory = data;
    			}
			)
            .catch(
                function (data) {
                	alert(angular.toJson(data));
                    toaster.pop({'type': 'error', 'title': 'Houve um erro na requisição do histórico de alterações.'});                    
                }
            );
        	
        }
        
        $scope.updateAsset = function(asset) {
            asset.canResume = asset.canResume.val;
        	// 'Vazio' to null
            for ( var key in asset ) {
                if ( asset.hasOwnProperty(key) ) {
                    if ( asset[key] === 'Vazio' ) { asset[key] = null; }
                }
            }
            if (asset.title === '' || !asset.title) { 
                toaster.pop({'type': 'error',  'title': 'Nome é obrigatório.'});
            }
//        	if (asset.price <= 0) {
//                toaster.pop({'type': 'error', 'title': 'Preço deve ser maior do que zero.'});
//        	}
            asset.licenseWindowStart = setTimezone(asset.licenseWindowStart);
            asset.licenseWindowEnd = setTimezone(asset.licenseWindowEnd);
            
            assetsService.updateAsset(asset)
            .then(
        	    function (data) {
    			toaster.pop({'type': 'success', 'title': 'Asset alterado com sucesso.'});
    			nullToEmptyText(asset);
    			parseCanResumeCombobox($scope.canResumeOptions, asset);
    			$scope.getHistory(asset.assetId);
    			$scope.removeMenus();
        	    }
            )
            .catch(
                function (data) {
                    toaster.pop({'type': 'error', 'title': 'Houve um erro ao processar a operação.'});
                    nullToEmptyText(asset);
                }
            ); 
        	
        };
        $scope.removeMenus = function(){
        	var lista = new Array();
        	for (var i = 0; i < $scope.oldMenus.length; i++){ 
        		var notFound = true;
        		for (var j = 0; j < $scope.asset.ipvodVisualMenus.length; j++) {
        			if ($scope.oldMenus[i].menuId == $scope.asset.ipvodVisualMenus[j].menuId) {
        				notFound = false;
        				$scope.asset.ipvodVisualMenus[j];
        				break;
        			};
        		}
        		if (notFound) {
        			var menuAsset = { ipvodVisualMenu: {menuId : $scope.oldMenus[i].menuId},
	        					  ipvodAsset: { assetId : $scope.asset.assetId }};
        			lista.push(menuAsset);
        		};
        	};

        	if (lista.length > 0) {
        		assetsService.removeMenus(lista)
	            .then(function (data) {
	            	$scope.loadAssetData();
	            } )
	            .catch(function (msg) {
	                toaster.pop({title: 'Erro ao completar a operação solicitada.', type: 'danger', body: msg});
	            });
        	} else {
        		$scope.loadAssetData();
        	}

        	
        };
        $scope.cancelTempAsset = function(asset) {
        	assetsService.cancelTempAsset(asset)
            .then(
        	    function (data) {
        		toaster.pop({'type': 'success', 'title': 'Alterações temporárias canceladas.'});
                        nullToEmptyText(asset);
                        parseCanResumeCombobox($scope.canResumeOptions, asset);
                        $scope.getHistory(asset.assetId);
                        $scope.loadAssetData();
        	    }
            )
            .catch(
                function (data) {
                    toaster.pop({'type': 'error', 'title': 'Houve um erro ao processar a operação.'});
                    nullToEmptyText(asset);
                }
            );
        };
        
        $scope.loadMenuBreadcrumbs = function () {
        	if ($scope.asset != null && $scope.menus != null) {
        		for (var j = 0; j < $scope.asset.ipvodVisualMenus.length; j++) {
        			for (var i = 0; i < $scope.menus.length; i++) {
        				if ($scope.menus[i].menuId == $scope.asset.ipvodVisualMenus[j].menuId) {
        					$scope.asset.ipvodVisualMenus[j].breadcrumbs = $scope.menus[i].breadcrumbs;
        					break;
        				};
        			};
        		};
        	} else {
        		$timeout(function(){$scope.loadMenuBreadcrumbs();}, 500);
        	};
        }
        
        $scope.saveTempAsset = function (asset) {
            var temp = $scope.temp;
            temp.data = asset;
            temp.data.canResume = temp.data.canResume.val;
//            temp.data.licenseWindowStart = setTimezone(temp.data.licenseWindowStart);
//            temp.data.licenseWindowEnd = setTimezone(temp.data.licenseWindowEnd);
            assetsService.saveTempAsset(temp)
            	.then(
            		function (data) {
				toaster.pop({'type': 'success', 'title': 'Asset salvo com sucesso, para publicar clique em publicar.'});
                                nullToEmptyText(asset);
                                parseCanResumeCombobox($scope.canResumeOptions, asset);
                                $scope.getHistory(asset.assetId);
                                $scope.loadAssetData();
			}
            	)
		.catch(
			function (data) {
			    	toaster.pop({'type': 'error', 'title': 'Houve um erro ao processar a operação.'});
			    	nullToEmptyText(asset);
			}
		);
        };
        
        $scope.uploadFileToUrl = function(file, uploadUrl){
            var fd = new FormData();
            fd.append('file', file);
            $http.post(uploadUrl, fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
            .success(function(data){
            	console.log(data);
            })
            .error(function(data){
            	console.log(data);
            });
        }

        $scope.uploadFile = function(){
            var file = $scope.myFile;
            console.log('file is ' );
            console.dir(file);
            var uploadUrl = API_BASE_URL + '/IPVOD/rest/asset/uploadImg/'+$scope.asset.assetId;
            $scope.uploadFileToUrl(file, uploadUrl);
        };
            
        $log.info('CategorizacaoCtrl execution ended');
    }]);
    
    vodModule.directive('fileModel', ['$parse', function ($parse) {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                var model = $parse(attrs.fileModel);
                var modelSetter = model.assign;
                
                element.bind('change', function(){
                    scope.$apply(function(){
                        modelSetter(scope, element[0].files[0]);
                    });
                });
            }
        };
    }]);
})(window.angular, window.angular.module('vod'));

