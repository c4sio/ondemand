(function (angular, vodModule) {
    'use strict';

    vodModule
    .controller('LancamentosCtrl', [
        '$log',
        '$document',
        '$filter',
        '$timeout',
        '$scope',
        'toaster',
        '$http',
        'assetsService',
        'API_BASE_URL',
        '$modal',
        'breadcrumbService',
        function($log, $document, $filter, $timeout, $scope, toaster, $http, assetsService, API_BASE_URL, $modal, breadcrumbService) {
            $log.info('LancamentosCtrl initialized');

            $scope.menutreedata = [];
            $scope.menudeleted = [];
            var callMenus = function callMenus () {
                assetsService.getLancamentos()
                .then(function(dataFromServer) {
                    var iterator = function iterator (obj) {

                        for (var i = 0; i < obj.length; i++) {
                            var ob = obj[i];
                            if ( ob.avaliableSince && ob.avaliableUntil !== '' ) {
                                ob.avaliableSince = $filter('date')(ob.avaliableSince, 'yyyy-MM-dd');
                            }
                            if ( ob.avaliableUntil && ob.avaliableUntil !== '' ) {
                                ob.avaliableUntil = $filter('date')(ob.avaliableUntil, 'yyyy-MM-dd');
                            }
                            if ( ob.ipvodVisualMenus && ob.ipvodVisualMenus.length ) {
                                iterator(ob.ipvodVisualMenus);
                            }
                        };
                    };

                    iterator(dataFromServer);

                    $scope.menutreedata = dataFromServer;
                });
            };
            callMenus();
            $scope.assettreedata = [];              

            // Select component
            $scope.selectComponent = {};
            $scope.selectComponent.selectedMovieCategories = [];
            $scope.selectComponent.movieCategories = [];

            assetsService.getCategories().then(function (data) {
                $scope.selectComponent.movieCategories = data.list;
            });

            var currentDate = new Date(),
                day = currentDate.getDate(),
                month = currentDate.getMonth() + 1;

            if ( day < 10 ) { day = '0'+day; }
            if ( month < 10 ) { month = '0'+month; }

            // Datepicker: From
            $scope.datepickerFrom = null;
            $scope.datepickerTo = null;
            $scope.datepickerMaxDate = currentDate.getFullYear() + '-' + month +  '-' + day;

            // Filter box visibility toggle 
            $scope.filterBoxCollapsed = false;
            $scope.toggleFilter = function toggleFilter (status) {
                var collapsed = (status !== undefined) ? status : !$scope.filterBoxCollapsed;
                $scope.filterBoxCollapsed = collapsed;
            };

            // Filter Fixed Positioning
            var $treeFilter = angular.element('#conf-menu-filter'), 
                $treeCol = angular.element('#tree-col'),
                windowHeight = angular.element(window).height(),
                elementOffset = $treeFilter.offset().top,
                setTreeFilterWidth = function setTreeFilterWidth () {
                    $treeFilter.css({ 'width': angular.element('#search-form-col').width() + 'px' });
                },
                removeStyle = function removeStyle () {
                    $treeFilter.removeAttr('style'); 
                };

            angular.element(window).on('resize', function () {
                if ( $treeFilter.hasClass('gvt-sticky') ) {
                    setTreeFilterWidth();
                } else {
                    removeStyle();
                }
            });
            $document.on('scroll', function() {
                var scrollTop = angular.element(window).scrollTop(),
                    distance = (elementOffset - scrollTop),
                    treeFilterHeight = $treeFilter.height(),
                    treeColHeight = $treeCol.height();

                // if ( (distance < 0) && (treeFilterHeight < windowHeight ) ) { 
                if ( (distance < 0) && (treeColHeight > treeFilterHeight) ) {
                    $treeFilter.addClass('gvt-sticky'); 
                    setTreeFilterWidth();

                    if ( treeFilterHeight > windowHeight ) {
                        $timeout(function() {
                            $scope.toggleFilter(true);    
                        }, 0);
                    }
                }
                else { 
                    $treeFilter.removeClass('gvt-sticky'); 
                    removeStyle();
                }
            });

            var rows = 10, filterParams = null;

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

            // Paginator Control click event
            $scope.onPaginatorControlClick = function onPaginatorControlClick (page, loadDataParams) {
                $scope.loadData(page, filterParams.filters.rules, true);
            };

            // Load Data Fn
            $scope.currentPage;
            $scope.loadData = function loadData (page, loadDataParams) {
                var currentPage = page || 1, 
                    searchRules = {
                        'rows': rows,
                        'page': currentPage,
                        'filters': { 'groupOp':'AND', 'rules': [] },
                		'order': 'desc',
                		'orderBy': 'creationDate'
                    };
                $scope.currentPage = currentPage;
//              $scope.hasPreviousFilterParams = filterParams && filterParams.filters.rules.length;

                if ( loadDataParams ) {
                    angular.forEach(loadDataParams, function (value, key) {
                    	if (value.data == null || value.data.length == 0) {
                    		return;
                    	}
                        if ( value.field === 'releaseYear' ) { 
                        	value.data = new Date(value.data).getUTCFullYear(); 
                        }
                        if ( value.field === 'ipvodCategory1.categoryId' ) {
                            angular.forEach(value.data, function (val) {
                                var obj = angular.extend(value);
                                obj.data = val.categoryId;
                                searchRules.filters.rules.push(obj);
                            });

                            return;
                        }
                        if ( value.field === 'ipvodVisualMenus' ) { 
                        	if (value.data === false) {
                        		return;
                        	} 
                        }
                        searchRules.filters.rules.push(value);
                    });
                }
//                if ( $scope.hasPreviousFilterParams ) {
//                    // searchRules = filterParams;
//                    angular.forEach(filterParams.filters.rules, function (rule) {
//                        searchRules.filters.rules.push(rule);
//                    });
//                }

                filterParams = searchRules;
//                $scope.hasPreviousFilterParams = filterParams.filters.rules.length;

                // Get data from the server
                assetsService.getAssetsFilter(searchRules)
                .then(function (data) {
                    $scope.assettreedata = $scope.dataList = data.list;
                    $scope.assetsPaginatorCurrentPage = currentPage;
                    $scope.dataList = data.list;
                    $scope.dataListVisibleRows = rows; 
                    $scope.totalRows = data.count || 0;
                    $scope.totalPages = window.Math.ceil($scope.totalRows / rows);
                });
            };

            // Reset the form and performs a clean search
            $scope.resetFilters = function resetFilters () {
                $scope.datepickerFrom = null;
                $scope.datepickerTo = null;
                $scope.selectComponent.selectedMovieCategories = [];
                $scope.searchMovie = null;
                filterParams = null;
                
                $scope.loadData(1);
            };

            // Loading the data
            $scope.loadData(1);

            $scope.treeOptions = {
                dropped : function (event) {
                    $scope.orderList($scope.menutreedata);
                    //DESTINATION != SOURCE
                    if (event.source.nodeScope.$parentNodeScope != undefined &&
                        event.dest.nodesScope.$parent != undefined &&
                        event.source.nodeScope.$parentNodeScope.$modelValue.menuId != event.dest.nodesScope.$parent.$modelValue.menuId) {
                        //REMOVE NODE FROM SOURCE
                        for (var i = 0; i < event.source.nodeScope.$parentNodeScope.$modelValue.ipvodAssets.length; i++) {
                            if (event.source.nodeScope.$parentNodeScope.$modelValue.ipvodAssets[i].assetId == event.source.nodeScope.$modelValue.assetId ) {
                                event.source.nodeScope.$parentNodeScope.$modelValue.ipvodAssets.splice(i, 1);
                                break;
                            }
                        }
                    }
                    $scope.loadData($scope.currentPage);
                },
                accept: function(sourceNodeScope, destNodesScope, destIndex) {
                    return true;
                },
                dragMove: function(evt) {
                	if($(window).height() - (evt.pos.nowY-document.body.scrollTop) < 80) {                		
                		document.body.scrollTop += 25;
                	} else if(evt.pos.nowY-document.body.scrollTop < 80 && (evt.pos.startY+document.body.scrollTop) > (evt.pos.nowY)) {  
                		document.body.scrollTop -= 25;
                	}
                },
            };

            $scope.orderList = function (ipvodMenuList) {
                for (var i = 0; i < ipvodMenuList.length; i++) {
                    menuOrder(ipvodMenuList[i]);
                }
            };

            var menuOrder = function menuOrder (ipvodMenu) {
                var menus = new Array();
                var assets = new Array();
                var all = new Array();
                if (ipvodMenu.ipvodAssets !== undefined) {
                    all = all.concat(ipvodMenu.ipvodAssets);
                }
                if (ipvodMenu.ipvodVisualMenus !== undefined) {
                    all = all.concat(ipvodMenu.ipvodVisualMenus);
                }
                for (var i =0; i < all.length; i++) {
                    var menu = all[i];
                    if (menu.assetId !== undefined) {
                        if (!listHasAsset(assets, menu)) {
                            delete menu.ipvodAssets;
                            assets.push(menu);
                        }
                    } else {
                        if (all.length > 0) {
                            menuOrder(menu);
                        }
                        menus.push(menu); 
                    }
                }
                if (ipvodMenu.ipvodAssets !== undefined) {
                    ipvodMenu.ipvodAssets = assets;
                }
                if (ipvodMenu.ipvodVisualMenus !== undefined) {
                    ipvodMenu.ipvodVisualMenus = menus;
                }
            };

            var listHasAsset = function (assets, menu) {
                var has = false;
                for (var i = 0; i < assets.length; i++ ) {
                    if (assets[i].assetId !== undefined && assets[i].assetId == menu.assetId) {
                        has = true;
                        break;
                    }
                }
                return has;
            };

            $scope.save = function save () {
                var modalOptions = {
                    'template': angular.element('#modal-confirm-template').html(),
                    'controller': 'MenuConfirmModalCtrl',
                    'size': 'sm'
                },

                okCallback = function okCallback (modalInstance, modalScope) {
                    var responsePromise = $http.post(API_BASE_URL + '/IPVOD/rest/menu/save', { 'save': $scope.menutreedata, 'delete': $scope.menudeleted }, {}),
                        afterSave = function afterSave (toasterMessage) {
                            callMenus()
                            .then(function () {
                                modalInstance.close();
                                toasterMessage();
                                $timeout(function() {
                                    modalScope.saving = false;
                                }, 1000);
                            });
                        };

                    modalScope.saving = true;
                    responsePromise.success(function(dataFromServer, status, headers, config) {
                        var toasterMessage = function toasterMessage () {
                            toaster.pop({'type': 'success', 'title': 'Estrutura de menus salva!'});
                        };
                        afterSave(toasterMessage);
                    });
                    responsePromise.error(function(dataFromServer, status, headers, config) {
                        var toasterMessage = function toasterMessage () {
                            toaster.pop({'type': 'error', 'title': dataFromServer.errorMessage});
                        };
                        afterSave(toasterMessage);
                    });
                };

                modalOptions.resolve = {
                    callback: function callback () {  return okCallback; }
                };

                $modal.open(modalOptions);
            };

            $scope.edit = function (obj) {
                var modalOptions = {
                    'template': angular.element('#modal-template').html(),
                    'controller': 'MenuModalCtrl',
                    'size': 'sm',
                    'backdrop': 'static',
                    'keyboard': false
                };
                $scope.name = '';
                modalOptions.resolve = {
                    'editObject': function () { return obj; },
                    'parentMenuList': function () { return null; },
                    'itemType': function () { return 'menu'; }
                };
                $modal.open(modalOptions);
            };

            $scope.editAsset = function (obj) {
                var modalOptions = {
                    'template': angular.element('#modal-template').html(),
                    'controller': 'MenuModalCtrl',
                    'size': 'sm'
                };

                $scope.name = '';
                modalOptions.resolve = {
                    'editObject': function () { return obj; },
                    'parentMenuList': function () { return null; },
                    'itemType': function () { return 'asset'; }
                };
                $modal.open(modalOptions);
            };

            $scope.removeMenu = function (obj) {
                $scope.menudeleted.push(obj.$modelValue);
                obj.remove();
            };

            $scope.removie = function (obj) {
                obj.remove();
            };
            $log.info('LancamentosCtrl execution ended');
        }
    ])

    //Controller for the modal
    .controller('MenuConfirmModalCtrl', 
        ['$scope', '$http', '$log', '$modalInstance', 'toaster', 'callback',
        function ($scope, $http, $log, $modalInstance, toaster, callback) {

            $scope.modalTitle = 'Confirmar';
            $scope.text = 'Deseja realmente salvar os dados?';
            $scope.saving = false;

            $scope.ok = function ok (menu, saving) {
                if ( saving ) { return; }
                return callback($modalInstance, $scope);
            };

            $scope.cancel = function cancel (saving) {
                if ( saving ) { return; }
                $modalInstance.close();
            };
    }]);

})(window.angular, window.angular.module('vod'));