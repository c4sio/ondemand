(function (angular, vodModule) {
    'use strict';

    vodModule
    .controller('MenuDetailCtrl', [
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
        '$routeParams',
        '$window',
        function($log, $document, $filter, $timeout, $scope, toaster, $http, assetsService, API_BASE_URL, $modal, breadcrumbService, $routeParams, $window) {
            $log.info('MenuDetailCtrl initialized');

            // Breadcrumb Update
            breadcrumbService.add([
                { 'label': 'Opções do menu', 'href': 'javascript: void(0);' },
                { 'label': 'Ordenação e renomeação' }
            ]);

            $scope.today = $filter('date')(new Date,'yyyy-MM-dd');
            
            $scope.menutreedata = [];
            $scope.menudeleted = [];
            var callMenus = function callMenus () {
                var responsePromise = $http.get(API_BASE_URL + '/IPVOD/rest/menu/cms/'+$routeParams.menuId);
                $scope.mainMenuId = $routeParams.menuId;
                responsePromise.success(function(dataFromServer, status, headers, config) {
                    $scope.menutreedata = [dataFromServer];
                    $scope.originaltreedata = [angular.copy(dataFromServer)];
                    $scope.saved = true;
                    $scope.areEqual = angular.equals($scope.menutreedata, $scope.originaltreedata);
                });
                responsePromise.error(function(dataFromServer, status, headers, config) {
                    $log.info(status);
                });
                return responsePromise;
            };
            callMenus();
            $scope.assettreedata = [];              
            
            //RETRIEVE DATA FOR PACKAGES MULTI-SELECT  
            var responsePromise = $http.get(API_BASE_URL + '/IPVOD/rest/package');
            responsePromise.success(function(dataFromServer, status, headers, config) {
            	$scope.packages = dataFromServer;
            });
            
            //
            var responsePromise = $http.get(API_BASE_URL + '/IPVOD/rest/rating');
            responsePromise.success(function(dataFromServer) {
                $scope.ratingList = dataFromServer;
            });
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

             // $scope.datepickerMaxDate = currentDate.getFullYear() + '-' + month +  '-' + day;

            // Datepicker: From
            $scope.datepickerFrom = null;
            $scope.datepickerTo = null;
            $scope.datepickerMaxDate = currentDate.getFullYear() + '-' + month +  '-' + day;

            // Edit Menu visibility toggle
            $scope.editMenuCollapsed = false;
            $scope.toggleEditMenu = function toggleEditMenu (status) {
                var collapsed = (status !== undefined) ? status : !$scope.editMenuCollapsed;
                $scope.editMenuCollapsed = collapsed;
            };

            // Filter box visibility toggle 
            $scope.filterBoxCollapsed = false;
            $scope.toggleFilter = function toggleFilter (status) {
                var collapsed = (status !== undefined) ? status : !$scope.filterBoxCollapsed;
                $scope.filterBoxCollapsed = collapsed;
            };

            // Filter Fixed Positioning
            var $confMenuFilter = angular.element('#conf-menu-filter'), 
                $treeCol = angular.element('#tree-col'),
                windowHeight = angular.element(window).height(),
                elementOffset = $confMenuFilter.offset().top,
                setConfMenuFilterWidth = function setConfMenuFilterWidth () {
                    $confMenuFilter.css({ 'width': angular.element('#search-form-col').width() + 'px' });
                },
                removeStyle = function removeStyle () {
                    $confMenuFilter.removeAttr('style'); 
                };

            angular.element(window).on('resize', function () {
                if ( $confMenuFilter.hasClass('gvt-sticky') ) {
                    setConfMenuFilterWidth();
                } else {
                    removeStyle();
                }
            });
            $document.on('scroll', function() {
                var scrollTop = angular.element(window).scrollTop(),
                    distance = (elementOffset - scrollTop),
                    confMenuFilterHeight = $confMenuFilter.height(),
                    treeColHeight = $treeCol.height();

                // if ( (distance < 0) && (confMenuFilterHeight < windowHeight ) ) { 
                if ( (distance < 0) && (treeColHeight > confMenuFilterHeight) ) {
                    $confMenuFilter.addClass('gvt-sticky'); 
                    setConfMenuFilterWidth();

                    if ( confMenuFilterHeight > windowHeight ) {
                        $timeout(function() {
                            $scope.toggleFilter(true);    
                        }, 0);
                    }
                }
                else { 
                    $confMenuFilter.removeClass('gvt-sticky'); 
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
            $scope.loadDataParams;
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
                $scope.loadDataParams = loadDataParams;
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
                        if ( value.field === 'ipvodVisualMenuAsset' ) { 
                        	if (value.data === false) {
                        		return;
                        	} 
                        }
                        searchRules.filters.rules.push(value);
                    });
                    searchRules.filters.rules.push({ 'field': 'licenseWindowEnd', 'op': 'ge', 'data': $filter('date')(new Date,'dd/MM/yyyy') });
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

            $scope.loadData(1);

            // Toggles menu visibility
            $scope.toggleMenu = function(menu) {
                toggleActivation(menu);
                if (menu.ipvodVisualMenus.length !== 0) {
                    for (var i = 0; i < menu.ipvodVisualMenus.length; i++) {
                        $scope.toggleMenu(menu.ipvodVisualMenus[i], menu.active);
                    }
                }
            };

            var toggleActivation = function (menu, active) {
                if (active !== undefined) {
                    menu.active = active;
                } else {
                    if (menu.active === 1) {
                        menu.active = 0;
                    } else {
                        menu.active = 1;
                    }
                }
            };
                    
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
                    $scope.loadData($scope.currentPage, $scope.loadDataParams);
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
            	console.log('order');
//            	for (var i = ipvodMenuList.length - 1; i >= 0; i--) {
            	for (var i = 0; i < ipvodMenuList.length; i++) {
            		if (ipvodMenuList[i].menuId == undefined || ipvodMenuList[i].menuId != $routeParams.menuId) {
            			var menu = ipvodMenuList.splice(i, 1);
            			ipvodMenuList.push(menu[0]);
            		}
            	}
                for (var i = ipvodMenuList.length - 1; i > 0; i--) {
                	ipvodMenuList[0].ipvodVisualMenus.push(ipvodMenuList.pop());
                }
                menuOrder(ipvodMenuList[0]);
            };
                    
            var menuOrder = function (ipvodMenu) {
            	if (ipvodMenu == null) {
            		return;
            	}
                var menus = new Array();
                var assets = new Array();
                var all = new Array();
                if (ipvodMenu.ipvodAssets != null && ipvodMenu.ipvodAssets.length > 0) {
                    all = all.concat(ipvodMenu.ipvodAssets);
                }
                if (ipvodMenu.ipvodVisualMenus !== null || ipvodMenu.ipvodVisualMenus.length > 0) {
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
//                            menuOrder(menu);	
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
                console.log('order fim');
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

            $scope.validateAvaliableSince = function validateAvaliableSince (event) {
                var dateSince = new Date($scope.menuForm.avaliableSince.$modelValue).getTime(),
                    dateUntil = new Date($scope.menuForm.avaliableUntil.$modelValue).getTime();

                if ( isNaN(dateUntil) ) { return; }
                if ( dateSince > dateUntil ) {
                    $scope.menuForm.avaliableSince.$setValidity('avaliableSinceRangeInvalid', false);
                } else {
                    $scope.menuForm.avaliableSince.$setValidity('avaliableSinceRangeInvalid', true);
                    $scope.menuForm.avaliableUntil.$setValidity('avaliableUntilRangeInvalid', true);
                }
            };
            $scope.validateAvaliableUntil = function validateAvaliableUntil (event) {
                var dateSince = new Date($scope.menuForm.avaliableSince.$modelValue).getTime(),
                    dateUntil = new Date($scope.menuForm.avaliableUntil.$modelValue).getTime();

                if ( isNaN(dateSince) ) { return; }
                if ( dateUntil < dateSince ) {
                    $scope.menuForm.avaliableUntil.$setValidity('avaliableUntilRangeInvalid', false);
                } else {
                    $scope.menuForm.avaliableSince.$setValidity('avaliableSinceRangeInvalid', true);
                    $scope.menuForm.avaliableUntil.$setValidity('avaliableUntilRangeInvalid', true);
                }
            };
            
            $scope.newSubItem = function(scope) {

                var modalOptions = {
                    'template': angular.element('#modal-template').html(),
                    'controller': 'MenuModalCtrl',
                    'size': 'sm',
                    'backdrop': 'static',
                    'keyboard': false
                };
                $scope.name = '';
                modalOptions.resolve = {
                    'parentMenuList': function () { return scope.$modelValue.ipvodVisualMenus; },
                    'editObject': function () { return null; },
                    'itemType': function () { return 'menu'; }
                };
                $modal.open(modalOptions);
            };

            $scope.save = function save () {
                var modalOptions = {
                    'template': angular.element('#modal-confirm-template').html(),
                    'controller': 'MenuConfirmModalCtrl',
                    'size': 'sm'
                },

                okCallback = function okCallback (modalInstance, modalScope) {
//                    var responsePromise = $http.post(API_BASE_URL + '/IPVOD/rest/menu/'+$scope.menutreedata[0].menuId, $scope.menutreedata[0], {}),
                	$scope.menutreedata[0].avaliableSince = setTimezone($scope.menutreedata[0].avaliableSince);
                	$scope.menutreedata[0].avaliableUntil = setTimezone($scope.menutreedata[0].avaliableUntil);
                	var responsePromise = $http.post(API_BASE_URL + '/IPVOD/rest/menu/'+$scope.menutreedata[0].menuId, { 'save': $scope.menutreedata, 'delete': $scope.menudeleted }, {}),
                	 
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
                    // url: function url () { return API_BASE_URL + '/IPVOD/rest/menu/save'; }, 
                    // data: function data () { return $scope.menutreedata; },
                    // deletedData: function deletedData () { return $scope.menudeleted; },
                    callback: function callback () {  return okCallback; }
                };

                $modal.open(modalOptions);
            };

            $scope.editMenu = function (menuId) {
            	$window.location.hash = '#/opcoes-do-menu/menu/' + menuId; 
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

            $scope.editAsset = function (obj, assetId) {
            	
            	document.location = "#/conteudo/ingestao/categorizacao?asset="+assetId;
				
                /*var modalOptions = {
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
				
                $modal.open(modalOptions);*/
            };

            $scope.removeMenu = function (obj) {
                $scope.menudeleted.push(obj.$modelValue);
                obj.remove();
            };
                    
            $scope.removie = function (obj) {
                console.log(obj);
                obj.remove();
            };
            $log.info('MenuDetailCtrl execution ended');
        }])

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

var setTimezone = function(date) {
	if (date != null && date != "") {
		var formatted = date + "T00:00:00.000" + -(new Date().getTimezoneOffset()/60); 
		if (formatted.substring(23).length == 2) {
			 formatted = formatted.substring(0, 24) + "0" + formatted.substring(24);
		}
		return formatted;
	}
	return null;
};
