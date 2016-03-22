(function(angular, vodModule) {
    'use strict';
    
    vodModule.controller('CategorizacaoListaController', [
        '$log', 
        '$scope', 
        'ingestaoService', 
        'toaster', 
        'breadcrumbService',
        'MenuItemsService',
		'assetsService',
		'$http',
		'API_BASE_URL',
		'$modal',
        function($log, $scope, ingestaoService, toaster, breadcrumbService, MenuItemsService, assetsService, $http, API_BASE_URL, $modal) {
        $log.info('CategorizacaoListaController initialized');

        // Breadcrumb Update
        breadcrumbService.add([
            {'label': 'Conteúdo', 'href': 'javascript: void(0);'},
            {'label': 'Categorização'}
        ]);
        $scope.menus = [];
        $scope.loadMenus = function () {
        	assetsService.getVisualMenu()
        	.then(function (data) {
        		$scope.menus = data;
        	});
        };
        $scope.loadMenus();
        
        $scope.ingestIdEdit;
		$scope.assetIdEdit;
        
        $scope.setIngestEdit = function(ingestId, assetId) {
        	$scope.ingestIdEdit = ingestId;       
			$scope.assetIdEdit = assetId;       			
        };
        
        $scope.updateCategory = function(idIngest) {
			var categoryId = document.getElementById("select_" + idIngest).value;
			var func = function(){
				$scope.setIngestEdit(null);
				document.getElementById("text_" + idIngest).innerHTML = document.getElementById("select_" + idIngest).options[document.getElementById("select_" + idIngest).selectedIndex].text;
				toaster.pop({'type': 'success',  'title': 'Categoria Atualizada com sucesso'});
			};
			assetsService.updateCategory(categoryId, $scope.assetIdEdit, func);
		};
		$scope.updateBillingId = function(idIngest, assetId, billingId) {
			var func = function () {
				$scope.blurBillingId(idIngest);
				toaster.pop({'type': 'success',  'title': 'Billing ID Atualizado com sucesso'});
			};
			assetsService.updateBillingId(assetId, billingId,func);
		};
		$scope.updatePrice = function(idIngest, assetId, price) {
			var func = function () {
				$scope.blurPrice(idIngest); 
				toaster.pop({'type': 'success',  'title': 'Preço Atualizado com sucesso'});
			};
			assetsService.updatePrice(assetId, price, func);
		};
		$scope.selectIngestion = function(ingest) {
			$scope.selectedIngest = ingest; 
		};
		$scope.finishRevision = function() {
			var func = function(){
				toaster.pop({'type': 'success',  'title': 'Revisão do asset ' +  $scope.selectedIngest.assetId + ' - ' + $scope.selectedIngest.assetTitle + ' finalizada!'});
				$scope.selectIngestion(null);
				$scope.loadData(1);
			};
			assetsService.finishRevision($scope.selectedIngest.assetId, func);
		};
		$scope.focusBillingId = function(idIngest) {
			eval('$scope.showBillingId'+idIngest+'=true');
		};
		$scope.blurBillingId = function(idIngest) {
			eval('$scope.showBillingId'+idIngest+'=false');
		};
		
		$scope.focusPrice = function(idIngest) {
			eval('$scope.showPrice'+idIngest+'=true');
		};
		$scope.blurPrice = function(idIngest) {
			eval('$scope.showPrice'+idIngest+'=false');
		};
		
        $scope.categories = MenuItemsService.get();
        $scope.orderByField = 'priority';
        $scope.reverseSort = false;
        $scope.sort = function sort ($event) {
            $event.preventDefault();
            $scope.orderByField = $event.currentTarget.dataset.sort;
            $scope.reverseSort = !$scope.reverseSort;
        };

        $scope.asset = {};
        assetsService.getProviderUnique()
        .then(function (responseData) {
            $scope.contentProviders = responseData;
            for (var i = 0; i< $scope.contentProviders.length; i++) {
                if ( $scope.asset.ipvodContentProvider && ($scope.asset.ipvodContentProvider.providerId == $scope.contentProviders[i].providerId ) ) {
                    $scope.asset.ipvodContentProvider = $scope.contentProviders[i];
                    break;
                }
            }
        });

        // Filter Data
        var filterParams = null;
        $scope.form = {};
        $scope.form.search = null;
        $scope.form.showRows = [10, 25, 50, 100];
        $scope.form.visibleRows = 10;
        $scope.hasPreviousFilterParams = false;

        // The Clear Filter Button action
        $scope.clearFilterParams = function clearFilterParams () {
            var loadParams;
            filterParams = null;
            $scope.hasPreviousFilterParams = false;
            $scope.form.search = null;
            $scope.asset.ipvodContentProvider = {};

            // if ( mustListAssetsByCategory ) { loadParams = assetsByCategoryParams; }
            $scope.loading = true;

            $scope.loadData(1, loadParams);
        };

        /**
         * Paginator required params:
         *
         * paginator-list-data="dataList"
         * paginator-list-visible-rows="dataListVisibleRows"
         * paginator-current-page="assetsPaginatorCurrentPage"
         * paginator-offset="assetsPaginatorOffset"
         * paginator-total-pages="totalPages"
         * paginator-control-on-click="onPaginatorControlClick"
         * paginator-page-button-on-click="loadData"
         **/
        // Load Data Fn
        $scope.loadData = function loadData (page, loadDataParams, isSearchForm) {
            var currentPage = page || 1,
                searchRules = {
                    'rows': $scope.form.visibleRows,
                    'page': currentPage,
                    'filters': { 'groupOp':'AND', 'rules': [] }
                },
                loadFn = ingestaoService.get;

            $scope.hasPreviousFilterParams = filterParams && filterParams.filters.rules.length;

            if ( loadDataParams ) {
                searchRules.filters.rules = loadDataParams;
            }
            if ( $scope.hasPreviousFilterParams && !isSearchForm ) {
                // searchRules = filterParams;
                angular.forEach(filterParams.filters.rules, function (rule) {
                    searchRules.filters.rules.push(rule);
                });
            }

            filterParams = searchRules;
            $scope.hasPreviousFilterParams = filterParams.filters.rules.length;

            // Get data from the server
            loadFn(searchRules, true)
                .then(function (data) {
                    $scope.assetsPaginatorCurrentPage = currentPage;
                    $scope.dataList = data.list || data.ipvodAssets || data.ipvodCategories;

                    $scope.dataListVisibleRows = $scope.form.visibleRows;
                    $scope.totalRows = data.count || 0;
                    $scope.totalPages = window.Math.ceil($scope.totalRows / $scope.form.visibleRows);

                    $scope.loading = false;
                });
        };

        // Paginator Control click event
        $scope.onPaginatorControlClick = function onPaginatorControlClick (page) {
            $scope.loading = true;
            $scope.loadData(page);
        };

        $scope.loading = true;
        $scope.loadData(1);
        
        assetsService.getVisualMenu()
        .then(function (data) {
        	$scope.menus = data;
        });
        
        $scope.onSelectMenu = function (menuId, assetId) {
        	var menuAsset = { ipvodVisualMenu: {menuId : menuId},
					  ipvodAsset: { assetId : assetId }};
        	assetsService.insertMenus(menuAsset)
            .then(function (data) {
            	toaster.pop({title: 'Menu incluído.', type: 'success'});
            } )
            .catch(function (msg) {
                toaster.pop({title: 'Erro ao incluir menu.', type: 'danger', body: msg});
            });
        };

        $scope.searchBar = function searchBar (searchStr, billingId, provider) {
            var arr = [];
            if ( searchStr ) { arr.push({ 'field': 'asset.title', 'op': 'cn', 'data': searchStr }); }
            if ( billingId ) { arr.push({ 'field': 'asset.billingID', 'op': 'eq', 'data': billingId }); }
            if ( provider ) { arr.push({ 'field': 'asset.ipvodContentProvider.providerId', 'op': 'eq', 'data': provider }); }

            $scope.loading = true;
            filterParams = null;

            $scope.loadData(1, arr);
        };

        // Remove a package
        $scope.onRemoveMenu = function (menuId, assetId) {
        	var lista = new Array();
        	var menuAsset = { ipvodVisualMenu: {menuId : menuId},
					  ipvodAsset: { assetId : assetId }};
        	lista.push(menuAsset);
        	assetsService.removeMenus(lista)
            .then(function (data) {
            	toaster.pop({title: 'Menu excluído.', type: 'success'});
            } )
            .catch(function (msg) {
                toaster.pop({title: 'Erro ao excluir menu.', type: 'danger', body: msg});
            });
        };
        
        var responsePromise = $http.get(API_BASE_URL + '/IPVOD/rest/rating');
        responsePromise.success(function(dataFromServer) {
            $scope.ratingList = dataFromServer;
        });

        $scope.openModal = function () {
        	 var modalOptions = {
                     'template': angular.element('#modal-template').html(),
                     'controller': 'MenuModalCtrl',
                     'size': 'sm',
                     'backdrop': 'static',
                     'keyboard': false
                 };
                 $modal.open(modalOptions);
        }
        
        $scope.validateAvaliableSince = function(event) {
//            var dateSince = new Date($scope.avaliableSince).getTime(),
//                dateUntil = new Date($scope.avaliableUntil).getTime();
//
//            if ( isNaN(dateUntil) ) { return; }
//            
//            $scope.menuForm.avaliableUntil = dateUntil;
//            $scope.menuForm.avaliableSince = dateSince;
//            if ( dateSince > dateUntil ) {
//                $scope.menuForm.avaliableSince.$setValidity('avaliableSinceRangeInvalid', false);
//            } else {
//                $scope.menuForm.avaliableSince.$setValidity('avaliableSinceRangeInvalid', true);
//                $scope.menuForm.avaliableUntil.$setValidity('avaliableUntilRangeInvalid', true);
//            }
        };
        $scope.validateAvaliableUntil = function(event) {
//        	var dateSince = new Date($scope.avaliableSince).getTime(),
//                dateUntil = new Date($scope.avaliableUntil).getTime();
//        	
//            if ( isNaN(dateSince) ) { return; }
//            
//            $scope.menuForm.avaliableUntil = dateUntil;
//            $scope.menuForm.avaliableSince = dateSince;
//            
//            if ( dateUntil < dateSince ) {
//                $scope.menuForm.avaliableUntil.$setValidity('avaliableUntilRangeInvalid', false);
//            } else {
//                $scope.menuForm.avaliableSince.$setValidity('avaliableSinceRangeInvalid', true);
//                $scope.menuForm.avaliableUntil.$setValidity('avaliableUntilRangeInvalid', true);
//            }
        };
        $scope.validateMenuRequired = function(event) {
//            if (
            		console.log($scope.menuForm.ipvodVisualMenu);
//            		$scope.menuForm.ipvodVisualMenu.$setValidity('avaliableSinceRangeInvalid', true);
        };
        $scope.sendMenu = function () {
        	var menu = {
        		name: $scope.menuForm.name,
    			ipvodRating: $scope.menuForm.ipvodRating,
    			ipvodVisualMenu: $scope.menuForm.ipvodVisualMenu,
    			avaliableSince: $scope.avaliableSince,
    			avaliableUntil: $scope.avaliableUntil 
        	};
        	if (menu.name == null ||
    			menu.ipvodRating == null ||
    			menu.ipvodVisualMenu == null //||
    				) {
        		return;
        	}
        	if (menu.avaliableSince != null && menu.avaliableSince != "" && 
    			menu.avaliableUntil != null && menu.avaliableUntil != ""
        	) {
        		var timeSince = new Date(menu.avaliableSince).getTime();
        		var timeUntil = new Date(menu.avaliableUntil).getTime();
        		if (timeSince > timeUntil) {
        			return;
        		}
        	}
        	menu.avaliableSince = setTimezone(menu.avaliableSince);
        	menu.avaliableUntil = setTimezone(menu.avaliableUntil);
        	var responsePromise = $http.post(API_BASE_URL + '/IPVOD/rest/menu', menu);
            responsePromise.success(function(dataFromServer) {
               toaster.pop({'type': 'success',  'title': 'Menu criado com sucesso'});
               $scope.loadMenus();
               $scope.clearMenu();
            });
            responsePromise.error(function(dataFromServer) {
                console.log(dataFromServer);
             });
        };
        $scope.clearMenu = function () {
	        $scope.menuForm.name = '';
	        $scope.menuForm.ipvodRating = {};
	        $scope.menuForm.ipvodVisualMenu = {};
	        $scope.avaliableSince = '';
	        $scope.avaliableUntil = '';
        };
        $log.info('CategorizacaoListaController execution ended');
    }]);
    
})(window.angular, window.angular.module('vod'));