(function (jQuery, angular, vodModule) {
    'use strict';

    vodModule
    .controller('AssetsCategoriasCtrl', [
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
    function ($log, $location, $document, $scope, $routeParams, toaster, assetsService, API_BASE_URL, $modal, breadcrumbService) {
        $log.info('AssetsCategoriasCtrl initialized');
        var 
            // FN to check the current form actions
            checkActions = function checkActions () {
                var path = $location.$$path;
                if ( /cadastrar/.test(path) ) { return 'cadastrar'; }
                else if ( /editar/.test(path) ) { return 'editar'; }
                else { return 'listar'; }
            },

            // Shared Grid Options
            gridOptions = {
                url: API_BASE_URL + '/IPVOD/rest/asset', 
                height: 250,
                colNames: ['Nome'],
                colModel: [{
                    name:'title',
                    index:'title', 
                    width:200, 
                    sorttype:'string', 
                    editable: true,
                    searchoptions: { sopt:['bw','eq','ne','lt','le','gt','ge','bw','bn','in','ni','ew','en','cn','nc'] }
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
                onClick : function(e) {}
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

            availableAssetsGridOptions = angular.extend({
                index:'title', 
                width:200, 
//                        sorttype:'string',
                sortable: false,
                searchoptions:{ sopt:['bw','eq','ne','lt','le','gt','ge','bn','in','ni','ew','en','cn','nc'] }, 
                
                beforeSelectRow: function beforeSelectRow(id, e) {
                    var td = e.target, index = $.jgrid.getCellIndex(td);
                    if ( index === 0 ) {
                        var $el = angular.element('#'+id),
                            movieTitle = $el.find('td').eq(1).text(),
                            data = { 'title': movieTitle },
                    
                            $availableAssetsTable = angular.element('#available-assets').find('table.grid-table'),
                            $selectedItemsTable = angular.element('#selected-assets').find('table.grid-table');

                        // Removing previous  record
                        $availableAssetsTable.jqGrid('delRowData', id);

                        // Adding new record to the Selected Items Table
                        $selectedItemsTable.addRowData(id, data, 'last');
                        return true; 
                    }

                    return false;
                }
            }, gridOptions),

            // Specific grid options for selected assets
            selectedAssetsGridOptions = angular.extend({}, gridOptions),

            action = checkActions(), 

            actionHandler = {};

        availableAssetsGridOptions.caption = 'Assets Disponíveis para Seleção';
        selectedAssetsGridOptions.caption = 'Assets Pertencentes à Categoria';
        
        $scope.action = action;

        // Form Submit handler
        // @description
        // Sends info to the server
        $scope.submitForm = function submitForm () {
            var data = {}, arr = [];

            // check to make sure the form is completely valid
            if ( !$scope.categoryForm.$valid ) { return; }

            // angular.element('#grid-01C')
            angular.element('#selected-assets table.grid-table')
            .find('tr.ui-widget-content').each(function (key, value) {
                var cellText = angular.element(this).find('td')[1].innerHTML;
                arr.push({ assetId: value.id, title: cellText });
            });

            data.description = $scope.categoryName;
            data.categoryId = $routeParams.type;
            data.ipvodAssets1 = arr; // Property used to store the assets related to the given category
            
            assetsService.save(data, 'category')
            .then(function () {
                toaster.pop({'type': 'success',  'title': 'Operação realizada com sucesso'});
                $location.path('/assets/categorias');
            })
            ['catch'](function (msg) {
                toaster.pop({'type': 'error',  'title': msg});
            });
        };

        selectedAssetsGridOptions.beforeSelectRow = function beforeSelectRow (id, e) {
            var td = e.target, index = $.jgrid.getCellIndex(td),
                $availableAssetsTable = angular.element('#available-assets').find('table.grid-table'),
                $selectedItemsTable = angular.element('#selected-assets').find('table.grid-table'),
                iRow = angular.element('#' + id)[0].rowIndex;

            if ( index === 0 ) {
                var $el = angular.element('#'+id),
                    movieTitle = $el.find('td').eq(1).text(),
                    data = { 'title': movieTitle };

                // Removing previous  record
                $selectedItemsTable.jqGrid('delRowData', id);

                // Adding new record to the Selected Items Table
                $availableAssetsTable.addRowData(id, data, 'last');
                return true; 
            }

            if ( index === 2 && !angular.element(td).is('input[type=text]') ) {
                return true;      
            }

            return false;
        };

        var availableAssetsSearchRules = {groupOp: 'AND', rules: [] },
            selectedAssetsSearchRules = {groupOp: 'AND', rules: [] },
            availableAssetsGridOptionsUrlFilters,
            selectedAssetsGridOptionsUrlFilters;


        /**
         * Handler for "cadastrar"
         **/
        actionHandler.cadastrar = function cadastrar () {
            selectedAssetsGridOptions.url = null;
            availableAssetsGridOptions.datatype = 'json';

            $scope.availableAssetsGridOptions = availableAssetsGridOptions;
            $scope.availableAssetsGridNavOptions = navOptions;
            $scope.selectedAssetsGridOptions = selectedAssetsGridOptions;
            $scope.selectedAssetsGridNavOptions = navOptions;
            $scope.selectedAssetsGridOptions.datatype = 'local';
        };

        /**
         * Handler for "editar"
         **/
        actionHandler.editar = function editar () {

            var categoryId = $routeParams.type,
                getCategoryName = assetsService.getCategoryById(categoryId);

            availableAssetsSearchRules.rules.push({'field': 'ipvodCategory1.categoryId', 'op': 'ne', 'data': $routeParams.type});
            availableAssetsGridOptionsUrlFilters = JSON.stringify(availableAssetsSearchRules);

            selectedAssetsSearchRules.rules.push({'field': 'ipvodCategory1.categoryId', 'op': 'eq', 'data': $routeParams.type});
            selectedAssetsGridOptionsUrlFilters = JSON.stringify(selectedAssetsSearchRules);

            availableAssetsGridOptionsUrlFilters = window.escape( availableAssetsGridOptionsUrlFilters );
            selectedAssetsGridOptionsUrlFilters = window.escape( selectedAssetsGridOptionsUrlFilters );

            availableAssetsGridOptions.url = availableAssetsGridOptions.url + '?filters=' + availableAssetsGridOptionsUrlFilters;
            selectedAssetsGridOptions.url = selectedAssetsGridOptions.url + '?filters=' + selectedAssetsGridOptionsUrlFilters;
            availableAssetsGridOptions.datatype = 'json';
            selectedAssetsGridOptions.datatype = 'json';

            $scope.availableAssetsGridOptions = availableAssetsGridOptions;
            $scope.availableAssetsGridNavOptions = navOptions;
            $scope.selectedAssetsGridOptions = selectedAssetsGridOptions;
            $scope.selectedAssetsGridNavOptions = navOptions;

            getCategoryName.then(function (categoryName) {
                // Breadcrumb Update
                breadcrumbService.add([
                    {'label': 'Assets', 'href': '#/assets/categorias/'}, 
                    {'label': 'Categorias', 'href': '#/assets/categorias'},
                    {'label': categoryName, 'href': '#/assets/categorias/'+categoryId},
                    {'label': 'Editar'}
                ]);
                $scope.categoryName = categoryName;
            });
        };

        /**
         * Handler for "listar"
         **/
        actionHandler.listar = function listar () {
            var mustListAssetsByCategory = $routeParams.type,
                assetsByCategoryParams = { 'field': 'ipvodCategory1.categoryId', 'op': 'eq', 'data': $routeParams.type },
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

                if ( mustListAssetsByCategory ) { loadParams = assetsByCategoryParams; }

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
                    loadFn = (mustListAssetsByCategory) ? assetsService.getAssetsFilter : assetsService.getCategories;

                if ( loadDataParams ) {
                    searchRules.filters.rules.push(loadDataParams);
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
                loadFn(searchRules)
                .then(function (data) {
                    $scope.assetsPaginatorCurrentPage = currentPage;
                    $scope.dataList = data.list || data.ipvodAssets || data.ipvodCategories;
                    $scope.dataListVisibleRows = $scope.form.visibleRows; 
                    $scope.totalRows = data.count || 0;
                    $scope.totalPages = window.Math.ceil($scope.totalRows / $scope.form.visibleRows);
                });
            };

            // Actions for assets list
            if ( mustListAssetsByCategory ) {
				// Breadcrumb Update
				var categoryId = $routeParams.type;
				assetsService.getCategoryById(categoryId)
				.then(function (categoryName) {
				    breadcrumbService.add([
				        {'label': 'Assets', 'href': '#/assets/categorias/'}, 
				        {'label': 'Categorias', 'href': '#/assets/categorias'},
				        {'label': categoryName, 'href': '#/assets/categorias/'+categoryId}
				    ]);
				});

				// Load data
				$scope.loadData(1, assetsByCategoryParams);
				$scope.listCategories = false;
            }

            // Actions for categories list
            else {
                $scope.removeItem = function removeItem (event, categoryId, categoryLabel) {
                    var 
                        fadeRow = function fadeRow () {
                            var element = angular.element(event.currentTarget).closest('tr');
                            element.fadeOut(500);
                        },
                        updateScope = function updateScope () {
                            // Find the removed item from the $scope
                            // var index = -1;
                            // for( var i = 0; i < $scope.dataList.length; i++ ) {
                            //     if( $scope.dataList[i].categoryId === categoryId ) {
                            //         index = i;
                            //         break;
                            //     }
                            // }

                            // if( index !== -1 ) { $scope.dataList.splice( index, 1 ); }

                            // $scope.totalRows =  $scope.totalRows - 1;
                            // $scope.totalPages = window.Math.ceil($scope.totalRows / $scope.form.visibleRows);

                            // Fetch the data again
                            $scope.loadData(1);
                        };

                    modalOptions.resolve = {
                        'categoryId': function () { return categoryId; },
                        'categoryLabel': function () { return categoryLabel; },
                        'updateScope': function () { return updateScope; },
                        'fadeRow': function () { return fadeRow; }
                    };
                    $modal.open(modalOptions); 
                };

                // Flag for listCategories
                $scope.listCategories = true;
                $scope.loadData(1);
                breadcrumbService.add([
                    {'label': 'Assets', 'href': 'javascript: void(0);'}, 
                    {'label': 'Categorias', 'href': '#/assets/categorias'}
                ]);
            }

            // Paginator Control click event
            $scope.onPaginatorControlClick = function onPaginatorControlClick (page) {
                $scope.loadData(page);
            };

            $scope.list = true;
        };

        // Handler execution
        actionHandler[action]();

        $log.info('AssetsCategoriasCtrl execution ended');
    }])
    
    // Controller for the modal
    .controller('CategoriesModalCtrl', ['$scope', '$timeout', '$log', '$modalInstance', 'categoryId', 'categoryLabel', 'assetsService', 'toaster', 'updateScope', 'fadeRow',
         function ($scope, $timeout, $log, $modalInstance, categoryId, categoryLabel, assetsService, toaster, updateScope, fadeRow) {
            $scope.modalTitle = null;
            $scope.modalBody = 'Deseja realmente remover o ítem "' + categoryLabel + '" ?';
            $scope.ok = function ok () {
                // Make delete request here
                assetsService.deleteCategory(categoryId)
                .then(function () {
                    fadeRow();
                    $timeout(updateScope, 1000);
                    toaster.pop({ 'type': 'success',  'title': 'Operação realizada com sucesso' });
                })
                ['catch'](function (msg) {
                    if (!msg || msg === '') { msg = 'Houve um erro ao processar a requisição'; }
                    toaster.pop({'type': 'error',  'title': msg});
                });

                $modalInstance.close();
                $log.info('Modal OK');
            };
            $scope.cancel = function cancel () {
                $modalInstance.close();
                $log.info('Modal Canceled');
            };
    }]);

})(window.$, window.angular, window.angular.module('vod'));