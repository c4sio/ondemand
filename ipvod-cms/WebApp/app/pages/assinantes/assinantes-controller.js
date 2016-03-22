angular.module('vod')
  .controller('AssinantesCtrl', 
[
    '$scope',
    '$location',
    '$http',
    '$timeout',
    '$q',
    'assinantesService',
    '$routeParams',
    '$modal',
    'breadcrumbService',
    'toaster',
function($scope, $location, $timeout, $http, $q, assinantesService, $routeParams, $modal, breadcrumbService, toaster) {
    'use strict';

    // Available actions: 'cadastrar', 'editar', 'listar'.
    var checkActions = function checkActions () {
                var path = $location.$$path;
                if ( /editar/.test(path) ) { return 'editar'; }
                else { return 'listar'; }
            },        
            action = checkActions(),
            actionHandler = {};

    /**
     * Handler for "listar"
     **/
    actionHandler.listar = function listar () {
        // var mustListAssetsByCategory = $routeParams.type,
        //     assetsByCategoryParams = { 'field': 'ipvodCategory1.categoryId', 'op': 'eq', 'data': $routeParams.type };

        // Sorting
        $scope.orderByField = 'crmCustomerId';
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
                // loadFn = (mustListAssetsByCategory) ? assetsService.getAssetsFilter : assetsService.getCategories;
                loadFn = assinantesService.get;

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

        // Paginator Control click event
        $scope.onPaginatorControlClick = function onPaginatorControlClick (page) {
            $scope.loadData(page);
        };

        $scope.loadData(1);
    };

    /**
     * Handler for "editar"
     **/
    actionHandler.editar = function editar () {
        var crmCustomerId = $routeParams.id, getData, getAllPackages;

        $scope.selectComponent = {
            selectedPackages: []
        };

        // Queue assinantes and all packages
        $q.all(
            [assinantesService.getById(crmCustomerId), assinantesService.getAllPackages()]
        ).then(function (data) {
            var assinantes = data[0],
                allPackages = data[1],
                packages = [];

            breadcrumbService.add([
                {'label': 'Assinantes', 'href': '#/assinantes'},
                {'label': crmCustomerId, 'href': '#/assinantes'},
                {'label': 'Editar'}
            ]);

            $scope.data = assinantes;

            assinantes.products.forEach(function (product) {
                packages.push({ otherId: product.id });

            });

            var arr1 = [], arr2 = [];
            packages.forEach(function (item) {
                for ( var i = 0; i < allPackages.list.length; i++ ) {
                    if ( item.otherId === allPackages.list[i].otherId ) {
                        arr1.push(i);
                        break;
                    }
                }
            });
            arr1.forEach(function(val) {
                arr2.push(allPackages.list[val]);
            });

            // packages = [arr[1], arr[3], arr[6]]
            $scope.selectComponent.selectedPackages = arr2;

            $scope.packages = allPackages.list;
        });

        // Equipment Types
        assinantesService.getEquipments().then(function (data) {
            $scope.equipmentTypes = data;
        });

        // Form Equipment data model
        $scope.formEquipmentData = {};

        // Packages on select
        $scope.onSelect = function onSelect (item, model) {
            var array = [];
            $scope.selectComponent.selectedPackages.forEach(function (obj) {
                array.push({id: obj.otherId});
            });
            assinantesService.savePackages(crmCustomerId, array)
            .then(function () {
                toaster.pop({title: 'Inserido com sucesso!', type: 'success'});
            })
            .catch(function (message) {
                toaster.pop({title: 'Erro ao completar a operação solicitada.', type: 'danger', body: message});
            });
        };

        // Remove a package
        $scope.onRemove = function onRemove (item) {
            assinantesService.removePackage(crmCustomerId, item.otherId)
            .then(function () {
                toaster.pop({title: 'Removido com sucesso!', type: 'success'});
            })
            .catch(function (message) {
                toaster.pop({title: 'Erro ao completar a operação solicitada.', type: 'danger', body: message});
            });
        };

        // Sorting
        $scope.orderByFieldEquipments = 'serial';
        $scope.reverseSortEquipments = false;
        $scope.sortEquipments = function sortEquipments (field) {
            $scope.orderByFieldEquipments = field;
            $scope.reverseSortEquipments = !$scope.reverseSortEquipments;
        };

        // Add a new Equip
        $scope.submitFormEquipments = function submitFormEquipments () {
            if ( $scope.formEquipments.$valid ) {
                assinantesService.createEquipment(crmCustomerId, [$scope.formEquipmentData])
                .then(function () {
                    var equipData = JSON.parse(JSON.stringify($scope.formEquipmentData));

                    // Success Message
                    toaster.pop({ title: 'Equipamento criado com sucesso.', 'type': 'success' });

                    // Push the new data to the table
                    $scope.data.equipments.push(equipData);
                });
            }
        };

        // Remove an Equip
        $scope.removeEquipment = function removeEquipment (event, serial) {
            var $el = angular.element(event.currentTarget).closest('tr');

            assinantesService.removeEquipment(crmCustomerId, serial)
            .then(function () {
                // Success Message
                toaster.pop({ title: 'Equipamento removido com sucesso.', 'type': 'success' });

                // Remove item from table
                $el.remove();
            });
        };


        // Tabs
        $scope.activeTab = 1;
        // Change tab visibility
        $scope.setTab = function setTab (tab, index) {
            tab = index;
            $scope.activeTab = tab;
        };
    };

    $scope.action = action;

    // Execute action handler
    actionHandler[action]();
    
}])

// Controller for the modal
.controller('AssinantesModalCtrl', 
    ['$scope', '$log', '$modalInstance', 'id', 'label', function ($scope, $log, $modalInstance, id, label) {
    'use strict';

    $scope.modalTitle = null;
    $scope.modalBody = 'Deseja realmente remover o usuário "' + label + '"?';
    $scope.ok = function ok () {
        // Make delete request here
        $modalInstance.close();
        $log.info('Modal OK');
    };
    $scope.cancel = function cancel () {
        $modalInstance.close();
        $log.info('Modal Canceled');
    };

}]);