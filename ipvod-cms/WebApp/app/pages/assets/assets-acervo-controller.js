(function (jQuery, angular, vodModule) {
    'use strict';

    vodModule
    .controller('AssetsAcervoCtrl', [
        '$window',
        '$log', 
        '$location', 
        '$document', 
        '$scope', 
        '$routeParams', 
        'toaster', 
        'assetsService', 
        'API_BASE_URL', 
        '$modal', 
        'breadcrumbService',
        'helpers',
        '$filter',
    function ($window, $log, $location, $document, $scope, $routeParams, toaster, assetsService, API_BASE_URL, $modal, breadcrumbService, helpers,$filter) {
        $log.info('AssetsPacotesCtrl initialized');

        var 
            // FN to check the current form actions
            checkActions = function checkActions () {
                var path = $location.$$path;
                if ( /cadastrar/.test(path) ) { return 'cadastrar'; }
                else if ( /editar/.test(path) ) { return 'editar'; }
                else { return 'listar'; }
            },

            selectGridRow = function selectGridRow () {},

            // Shared Grid Options
            gridOptions = {
                url: API_BASE_URL + '/IPVOD/rest/asset', 
                height: 250,                
                colNames: ['Nome', 'Preço'],
                colModel: [{
                    name:'title',
                    index:'title', 
                    width: 200, 
                    sorttype:'string', 
                    searchoptions: { sopt:['eq','ne','lt','le','gt','ge','bw','bn','in','ni','ew','en','cn','nc'] }
                }, {
                    name:'price',
                    index:'price', 
                    width:200, 
                    sorttype:'currency', 
                    formatter: 'currency',
                    searchoptions: { sopt:['eq','ne','lt','le','gt','ge','bw','bn','cn','nc'] }
                }],
                jsonReader: {
                    root: 'list',
                    total: 'count',
                    id: 'assetId' 
                },
                prmNames: {'order': 'order'},
                gridView: true,
                // search : {
                //     caption: 'Search...',
                //     Find: 'Find',
                //     Reset: 'Reset',
                //     odata : ['equal', 'not equal', 'less', 'less or equal','greater','greater or equal', 'begins with','does not begin with','is in','is not in','ends with','does not end with','contains','does not contain'],
                //     groupOps: [{ op: 'AND', text: 'all' }, { op: 'OR', text: 'any' }],
                //     matchText: ' match',
                //     rulesText: ' rules'
                // },
                sortorder: 'asc',
                sortname: 'title',
                viewrecords : true,
                rowNum: 10,
                rowList:[10,20,30],
//                rowNum:-1,
//                rowList:[],
                altRows: true,
//                postData: '',
                //toppager: true,
                //multikey: 'ctrlKey',
//                editurl: '/dummy.html' //nothing is saved
            },
            navOptions = [{
                edit: true,
                add: true,
                del: true,
                view: true,
                viewicon : 'ace-icon fa fa-search-plus grey'
            }, {
                //edit record form
                //closeAfterEdit: true,
                width: 700
            }, {
                //new record form
                //width: 700,
                closeAfterAdd: true,
                recreateForm: true,
                viewPagerButtons: false
            }, {
                //delete record form
                recreateForm: true,
                onClick : function() {}
            }, {
                //search form
                recreateForm: true,
                multipleSearch: true,
                /**
                multipleGroup:true,
                showQuery: true
                */
            }, {
                //view record form
                recreateForm: true
            }],

            

            action = checkActions(), 

            actionHandler = {};

       

        $scope.action = action;

        // Form Submit handler
        // @description
        // Sends info to the server
        
        

        var availableAssetsSearchRules = {groupOp: 'AND', rules: [] },
            selectedAssetsSearchRules = {groupOp: 'AND', rules: [] },
            availableAssetsGridOptionsUrlFilters,
            selectedAssetsGridOptionsUrlFilters;

        

        /**
         * Handler for "listar"
         **/
        actionHandler.listar = function listar () {
            var mustListAssetsByPackage = $routeParams.type,
                assetsByPackageParams = { 'field':'ipvodPackage.packageId', 'op': 'eq','data':$routeParams.type},
                modalOptions = {
                    'template': angular.element('#modal-template').html(),
                    'controller': 'CategoriesModalCtrl',
                    'size': 'sm'
                };
			
            // Sorting
            $scope.orderByField = 'description';
            $scope.reverseSort = false;
            $scope.sort = function sort ($event) {
                $event.preventDefault();
                $scope.orderByField = $event.currentTarget.dataset.sort;
                $scope.reverseSort = !$scope.reverseSort;
            };

            $scope.asset = {};
            assetsService.getProvider()
            .then(function (responseData) {
                $scope.contentProviders = responseData;
                for (var i = 0; i< $scope.contentProviders.length; i++) {
                    if ( $scope.asset.ipvodContentProvider && ($scope.asset.ipvodContentProvider.contentProviderId == $scope.contentProviders[i].contentProviderId ) ) {
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

                if ( mustListAssetsByPackage ) { loadParams = assetsByPackageParams; }

                $scope.loading = true;

                $scope.loadData(1, loadParams);
            };

            // Load Data Fn
            $scope.loadData = function loadData (page, loadDataParams) {
                var currentPage = page || 1, 
                    searchRules = {
                        'rows': $scope.form.visibleRows,
                        'page': currentPage,
                        'filters': { 'groupOp':'AND', 'rules': [] }
                    },
                    loadFn = assetsService.getAssetsFilter;
                searchRules.filters.rules.push({ 'field': 'licenseWindowEnd', 'op': 'ge', 'data': $filter('date')(new Date,'dd/MM/yyyy') });
                $scope.hasPreviousFilterParams = filterParams && filterParams.filters.rules.length;
                
                if ( loadDataParams ) {
                    searchRules.filters.rules = loadDataParams;
                    searchRules.filters.rules.push({ 'field': 'licenseWindowEnd', 'op': 'ge', 'data': $filter('date')(new Date,'dd/MM/yyyy') });
                }
                if ( $scope.hasPreviousFilterParams ) {
                    // searchRules = filterParams;
                    angular.forEach(filterParams.filters.rules, function (rule) {
                        searchRules.filters.rules.push(rule);
                    });
                }

                filterParams = searchRules;
                $scope.hasPreviousFilterParams = filterParams.filters.rules.length;

                // Get data from the server
                loadFn(searchRules)
                .then(function (data) {
                    $scope.assetsPaginatorCurrentPage = currentPage;
                    $scope.dataList = data.list || data;
                    $scope.dataListVisibleRows = $scope.form.visibleRows; 
                    $scope.totalRows = data.count || data.length || 0;
                    $scope.totalPages = window.Math.ceil($scope.totalRows / $scope.form.visibleRows);

                    $scope.loading = false;
                });
            };
            // Actions for assets list

			// Flag for listPackages
			$scope.listPackages = false;
            $scope.loading = true;
			$scope.loadData(1);

            // Paginator Control click event
            $scope.onPaginatorControlClick = function onPaginatorControlClick (page) {
                $scope.loadData(page);
                $scope.loading = true;
            };

            $scope.list = true;

            $scope.searchBar = function searchBar (searchStr, billingId, provider) {
                var arr = [];
                if ( searchStr ) { arr.push({ 'field': 'title', 'op': 'cn', 'data': searchStr }); }
                if ( billingId ) { arr.push({ 'field': 'billingID', 'op': 'eq', 'data': billingId }); }
                if ( provider ) { arr.push({ 'field': 'ipvodContentProvider.contentProviderId', 'op': 'eq', 'data': provider }); }

                $scope.loading = true;
                filterParams = false;

                $scope.loadData(1, arr);
            };
        };

        // Handler execution
        actionHandler[action]();

        $log.info('AssetsPacotesCtrl execution ended');
    }]);

})(window.$, window.angular, window.angular.module('vod'));