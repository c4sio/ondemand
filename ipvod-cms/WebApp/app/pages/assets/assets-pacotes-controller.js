(function (jQuery, angular, vodModule) {
    'use strict';

    vodModule
    .controller('AssetsPacotesCtrl', [
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
    function ($window, $log, $location, $document, $scope, $routeParams, toaster, assetsService, API_BASE_URL, $modal, breadcrumbService, helpers) {
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

            availableAssetsGridOptions = angular.extend({
                index: 'title', 
                width: 200, 
                sortable: false,
                searchoptions:{ sopt:['eq','ne','lt','le','gt','ge','bw','bn','in','ni','ew','en','cn','nc'] }, 
                beforeSelectRow: function beforeSelectRow(id, e) {
                    var td = e.target, index = $.jgrid.getCellIndex(td);
                    if ( index === 0 ) {
                        var $el = angular.element('#'+id),
                            movieTitle = $el.find('td').eq(1).text(),
                            moviePrice = helpers.numberToJsFormat($el.find('td').eq(2).text()),
                            data = { 'ipvodAsset': { 'title': movieTitle }, 'price': moviePrice },
                    
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
            selectedAssetsGridOptions = angular.extend({
                'cellEdit': true,
                'cellsubmit': 'clientArray',
                // 'afterEditCell': function (rowId, cellname, value, iRow, iCol) {
                //     debugger;
                // },
                'beforeSaveCell': function beforeSaveCell (rowId, cellname, value, iRow, iCol) {
                    if ( parseInt(value) > 999.99 ) {
                        $window.alert('O valor deve ser menor que R$ 999,99');
                        // toaster.pop({'type': 'error',  'title': 'O valor deve ser menor que R$ 999,99'});
                        return 999.99;
                    }
                    // Formats the value to a JS friendly format
                    return parseFloat(value.replace(',', '.'));
                }
            }, gridOptions),

            action = checkActions(), 

            actionHandler = {};

        availableAssetsGridOptions.caption = 'Assets Disponíveis para Seleção';
        selectedAssetsGridOptions.caption = 'Assets Pertencentes ao Pacote';
        selectedAssetsGridOptions.colModel[1].editable = true;
        selectedAssetsGridOptions.colModel[1].editoptions = {
            dataEvents: [
                { 
                    type: 'blur', 
                    data: {'foo': 'bar'}, 
                    fn: function fn (e) {
                        var selectedAssetsTable = angular.element('#selected-assets .grid-table'),
                            rowId = angular.element(e.currentTarget).closest('tr').prop('id'),
                            iRow = angular.element('#'+rowId)[0].rowIndex,
                            iCol = 2;

                        selectedAssetsTable.jqGrid('saveCell', iRow, iCol);
                    }
                }
            ]};

        $scope.action = action;

        // Form Submit handler
        // @description
        // Sends info to the server
        $scope.submitForm = function submitForm () {
            var data = {}, arr = [];

            // check to make sure the form is completely valid
            if ( !$scope.packageForm.$valid ) { return; }

            // angular.element('#grid-01C')
            angular.element('#selected-assets table.grid-table')
            .find('tr.ui-widget-content')
            .each(function (key, value) {
                var cell = angular.element(this).find('td'),
                    priceCell = angular.element(cell[2]),
                    price =  priceCell.find('input').length ? priceCell.find('input').val() : priceCell.text();

                arr.push({ 'price': helpers.numberToJsFormat(price), 'ipvodAsset': { 'assetId': value.id } });
            });

            data.description = $scope.packageName;
            data.dateStart = $scope.dateStart;
            data.dateStart = setTimezone($scope.dateStart);
            data.dateEnd = setTimezone($scope.dateEnd);
            data.ipvodRating = $scope.rating;

            data.price = $scope.price;

            data.ipvodAssetPackages = arr; // Property used to store the assets related to the given category
            data.ipvodPackageType = {
                'packageTypeId': $scope.type.packageTypeId,
                'description': $scope.type.description
            };
            data.zindex = $scope.zindex;
            
            if ( action === 'editar' ) { data.packageId = $scope.packageId; } 

            assetsService.save(data, 'package')
            .then(function () {
                toaster.pop({'type': 'success',  'title': 'Operação realizada com sucesso'});
                $location.path('/assets/pacotes');
            })
            .catch(function (response) {
                toaster.pop({ 'type': 'error',  'title': response.errorMessage });
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

        // Grid onSelectRow event handler
        selectedAssetsGridOptions.onSelectRow = function beforeSelectRow (id, e) {
            var td = e.target, index = $.jgrid.getCellIndex(td);
            if ( index === 0 ) {
                var $el = angular.element('#'+id),
                    movieTitle = $el.find('td').eq(1).text(),
                    data = { 'title': movieTitle },

                    $availableAssetsTable = angular.element('#available-assets').find('table.grid-table'),
                    $selectedItemsTable = angular.element('#selected-assets').find('table.grid-table');

                // Removing previous  record
                $selectedItemsTable.jqGrid('delRowData', id);
                // Adding new record to the Selected Items Table
                $availableAssetsTable.addRowData(id, data, 'last');
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
            var getRating = assetsService.getRating(),
                getPackageType = assetsService.getPackageType(),
                colModel;

            selectedAssetsGridOptions.url = null;
            availableAssetsGridOptions.datatype = 'json';

            getRating.then(function (packageRatings) {
                $scope.packageRatings = packageRatings;
            });
            getPackageType.then(function (packageTypes) {
                $scope.packageTypes = packageTypes;
            });

            selectedAssetsGridOptions.url = null;
            selectedAssetsGridOptions.datatype = 'local';

            colModel = selectedAssetsGridOptions.colModel.slice(0);
            colModel[0] = {
                name:'ipvodAsset.title',
                index:'ipvodAsset.title', 
                width: 200, 
                sorttype:'string', 
                searchoptions: { sopt:['bw','eq','ne','lt','le','gt','ge','bn','in','ni','ew','en','cn','nc'] }
            };

            selectedAssetsGridOptions.colModel = colModel;
            delete selectedAssetsGridOptions.root;
            delete selectedAssetsGridOptions.total;
            selectedAssetsGridOptions.localReader = {'id': 'ipvodAsset.assetId'}; 

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

            $scope.packageId = $routeParams.type;

            var 
                getPackage = assetsService.getPackageById($scope.packageId),
                getPackageType = assetsService.getPackageType(),
                getRating = assetsService.getRating(),
                colModel;

//            availableAssetsSearchRules.rules.push({'field': 'ipvodCategory1.categoryId', 'op': 'ne', 'data': $routeParams.type});
            availableAssetsGridOptionsUrlFilters = JSON.stringify(availableAssetsSearchRules);
            availableAssetsGridOptionsUrlFilters = window.escape( availableAssetsGridOptionsUrlFilters );
            availableAssetsGridOptions.url = availableAssetsGridOptions.url + '?filters=' + availableAssetsGridOptionsUrlFilters;
            availableAssetsGridOptions.datatype = 'json';

//            selectedAssetsSearchRules.rules.push({'field': 'ipvodCategory1.categoryId', 'op': 'eq', 'data': $routeParams.type});
            selectedAssetsGridOptionsUrlFilters = JSON.stringify(selectedAssetsSearchRules);
            selectedAssetsGridOptionsUrlFilters = window.escape( selectedAssetsGridOptionsUrlFilters );
            selectedAssetsGridOptions.url = null;
            selectedAssetsGridOptions.datatype = 'local';

            colModel = selectedAssetsGridOptions.colModel.slice(0);

            colModel[0] = {
                name:'ipvodAsset.title',
                index:'ipvodAsset.title', 
                width: 200, 
                sorttype:'string', 
                searchoptions: { sopt:['bw','eq','ne','lt','le','gt','ge','bn','in','ni','ew','en','cn','nc'] }
            };

            selectedAssetsGridOptions.colModel = colModel;
            delete selectedAssetsGridOptions.root;
            delete selectedAssetsGridOptions.total;
            selectedAssetsGridOptions.localReader = {'id': 'ipvodAsset.assetId'}; 

            // Exposing it to the scope in order to initialize the plugin
            $scope.availableAssetsGridOptions = availableAssetsGridOptions;
            $scope.availableAssetsGridNavOptions = navOptions;

            getPackageType.then(function (packageTypes) {
                $scope.packageTypes = packageTypes;
            });

            getRating.then(function (packageRatings) {
                $scope.packageRatings = packageRatings;
            });

            getPackage.then(function (packge) {
                // Breadcrumb Update
                breadcrumbService.add([
                    {'label': 'Assets', 'href': '#/assets/pacotes/'}, 
                    {'label': 'Pacotes', 'href': '#/assets/pacotes'},
                    {'label': packge.description, 'href': '#/assets/pacotes/' + $scope.packageId},
                    {'label': 'Editar'}
                ]);
                $scope.packageName = packge.description;
                $scope.rating = packge.ipvodRating;
                $scope.type = packge.ipvodPackageType;
                $scope.dateEnd = packge.dateEnd;
                $scope.dateStart = packge.dateStart;
                $scope.otherId = packge.otherId;
                $scope.price = packge.price;
                $scope.zindex = packge.zindex;
                
                selectedAssetsGridOptions.data = packge.ipvodAssetPackages;

                $scope.selectedAssetsGridOptions = selectedAssetsGridOptions;
                $scope.selectedAssetsGridNavOptions = navOptions;
            });
        };

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

                if ( mustListAssetsByPackage ) { loadParams = assetsByPackageParams; }

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
            $scope.loadData = function loadData (page, loadDataParams) {
                var currentPage = page || 1, 
                    searchRules = {
                        'rows': $scope.form.visibleRows,
                        'page': currentPage,
                        'filters': { 'groupOp':'AND', 'rules': [] }
                    },
                    loadFn = (mustListAssetsByPackage) ? assetsService.getAssetPackage : assetsService.getPackages;

                $scope.hasPreviousFilterParams = filterParams && filterParams.filters.rules.length;
                
                if ( loadDataParams ) {
                    searchRules.filters.rules.push(loadDataParams);
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
                });
            };
            // Actions for assets list
            if ( mustListAssetsByPackage ) {

                // Breadcrumb Update
                assetsService.getPackageById($routeParams.type)
                .then(function (packages) {
                    breadcrumbService.add([
                        {'label': 'Assets', 'href': '#/assets/pacotes/'}, 
                        {'label': 'Pacotes', 'href': '#/assets/pacotes'},
                        {'label': packages.description}
                    ]);
                });

                // Load data
                $scope.loadData(1, assetsByPackageParams);
                $scope.listPackages = false;
            }

            // Actions for categories list
            else {
                $scope.removeItem = function removeItem (event, packageId, packageLabel) {
                    debugger;
                    var 
                        fadeRow = function fadeRow () {
                            var element = angular.element(event.currentTarget).closest('tr');
                            element.fadeOut(500);
                        },
                        updateScope = function updateScope () {
                            // Find the removed item from the $scope
                            // var index = -1;
                            // for( var i = 0; i < $scope.dataList.length; i++ ) {
                            //     if( $scope.dataList[i].packageId === packageId ) {
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
                        'packageId': function () { return packageId; },
                        'packageLabel': function () { return packageLabel; },
                        'updateScope': function () { return updateScope; },
                        'fadeRow': function () { return fadeRow; }
                    };
                    $modal.open(modalOptions); 
                };

                // Flag for listPackages
                $scope.listPackages = true;
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
        $scope.searchBar = function(searchStr) {
        	if ($routeParams.type) {
        		$scope.loadData(1, { 'field': 'ipvodAsset.title', 'op': 'cn', 'data': searchStr });
        	} else {
        		$scope.loadData(1, { 'field': 'description', 'op': 'cn', 'data': searchStr });
        	}
        };
        // Handler execution
        actionHandler[action]();

        $log.info('AssetsPacotesCtrl execution ended');
    }])
    
    // Controller for the modal
    .controller('CategoriesModalCtrl', ['$scope', '$timeout', '$log', '$modalInstance', 'packageId', 'packageLabel', 'assetsService', 'toaster', 'updateScope', 'fadeRow',
         function ($scope, $timeout, $log, $modalInstance, packageId, packageLabel, assetsService, toaster, updateScope, fadeRow) {
            $scope.modalTitle = null;
            $scope.modalBody = 'Deseja realmente remover o ítem "' + packageLabel + '" ?';
            $scope.ok = function ok () {
                // Make delete request here
                assetsService.deletePackage(packageId)
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