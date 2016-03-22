(function(angular, vodModule) {
    'use strict';
    
    vodModule.controller('ConteudoCtrl', [
        '$log', 
        '$scope', 
        'ingestaoService', 
        'toaster', 
        'breadcrumbService',
        function($log, $scope, ingestaoService, toaster, breadcrumbService) {
        $log.info('ConteudoCtrl initialized');

        // Breadcrumb Update
        breadcrumbService.add([
            {'label': 'Conteúdo', 'href': 'javascript: void(0);'},
            {'label': 'Ingestão'}
        ]);

        /* var successHandler = function successHandler (data) {
                $scope.dataList = data;
                $scope.totalRows = data.count || data.length || 0;
            },
            errorHandler = function errorHandler (data) {
                toaster.pop({ type: 'error', title: 'Houve um erro ao buscar os dados do servidor.' });
            }; */

        // Sorting
        $scope.orderByField = 'priority';
        $scope.reverseSort = false;
        $scope.sort = function sort ($event) {
            $event.preventDefault();
            $scope.orderByField = $event.currentTarget.dataset.sort;
            $scope.reverseSort = !$scope.reverseSort;
        };

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
                    'filters': { 'groupOp':'OR', 'rules': [] }
                },
            // loadFn = (mustListAssetsByCategory) ? assetsService.getAssetsFilter : assetsService.getCategories;
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
            loadFn(searchRules, false)
            .then(function (data) {
                $scope.assetsPaginatorCurrentPage = currentPage;
                $scope.dataList = data.list || data.ipvodAssets || data.ipvodCategories;
                $scope.dataListVisibleRows = $scope.form.visibleRows;
                $scope.totalRows = data.count || 0;
                $scope.totalPages = window.Math.ceil($scope.totalRows / $scope.form.visibleRows);
				//create percentage variables for ordenation
                if ($scope.dataList != null) {
                	for (var i = 0; i < $scope.dataList.length; i++) {
                		if ($scope.dataList[i].balancerVO != null) {
                			$scope.dataList[i].percentCompBalancer = $scope.dataList[i].balancerVO[0].percentCompBalancer;
                		} else {
                			$scope.dataList[i].percentCompBalancer = null;
                		}
                	} 
                	for (var i = 0; i < $scope.dataList.length; i++) {
                		if ($scope.dataList[i].convoyVO != null) {
                			$scope.dataList[i].percentCompConvoy = $scope.dataList[i].convoyVO[0].percentCompConvoy;
                		} else {
                			$scope.dataList[i].percentCompConvoy = null;
                		}
                	}
                	for (var i = 0; i < $scope.dataList.length; i++) {
                		if ($scope.dataList[i].drmVO != null) {
                			$scope.dataList[i].percentCompDrm = $scope.dataList[i].drmVO[0].percentCompDrm;
                		} else {
                			$scope.dataList[i].percentCompDrm = null;
                		}
                	}
                }
                
                $scope.loading = false;
            });
        };

        // Paginator Control click event
        $scope.onPaginatorControlClick = function onPaginatorControlClick (page) {
            $scope.loadData(page);
            $scope.loading = true;
        };

        $scope.loading = true;
        $scope.loadData(1);

        // Definindo valores de prioridades fila Ingest
        $scope.prioritys = [0, 1, 2, 3, 4, 5];
        $scope.ingestIdEdit;
        $scope.setIngestEdit = function(ingestId) {
        	$scope.ingestIdEdit = ingestId;
        };
        
        $scope.updatePriority = function(idIngest){
        	var priorityValue = document.getElementById("select_" + idIngest).options[document.getElementById("select_" + idIngest).selectedIndex].value;
        	ingestaoService.updatePriority(idIngest, priorityValue, function(){$scope.setIngestEdit(null);});
        };
        
        // ingestaoService.get(successHandler, errorHandler, false);

        $scope.searchBar = function searchBar (searchStr) {

            var arr = [];
            if ( !searchStr ) { return; }

            arr.push({ 'field': 'asset.title', 'op': 'cn', 'data': searchStr });

            if ( $scope.form.metadata ) {
                arr.push({ 'field': 'asset.assetId', 'op': 'eq', 'data': searchStr });
                arr.push({ 'field': 'asset.audioType', 'op': 'cn', 'data': searchStr });
                arr.push({ 'field': 'asset.country', 'op': 'cn', 'data': searchStr });
                arr.push({ 'field': 'asset.description', 'op': 'cn', 'data': searchStr });
                arr.push({ 'field': 'asset.director', 'op': 'cn', 'data': searchStr });
                arr.push({ 'field': 'asset.dubbedLanguage', 'op': 'cn', 'data': searchStr });
                arr.push({ 'field': 'asset.episodeName', 'op': 'cn', 'data': searchStr });
                arr.push({ 'field': 'asset.languages', 'op': 'cn', 'data': searchStr });
                arr.push({ 'field': 'asset.price', 'op': 'eq', 'data': searchStr });
                arr.push({ 'field': 'asset.releaseYear', 'op': 'eq', 'data': searchStr });
                arr.push({ 'field': 'asset.screenFormat', 'op': 'cn', 'data': searchStr });
                arr.push({ 'field': 'asset.season', 'op': 'eq', 'data': searchStr });
                arr.push({ 'field': 'asset.subtitles', 'op': 'cn', 'data': searchStr });
                arr.push({ 'field': 'asset.totalTime', 'op': 'eq', 'data': searchStr });
            }

            $scope.loading = true;
            filterParams = false;

            $scope.loadData(1, arr);
        };
        
        $log.info('ConteudoCtrl execution ended');
    }]);
    
})(window.angular, window.angular.module('vod'));