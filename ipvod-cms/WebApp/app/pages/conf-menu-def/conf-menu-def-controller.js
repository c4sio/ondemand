(function (angular, vodModule) {
    'use strict';

    vodModule
    .controller('ConfMenuDefCtrl', [
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
            $log.info('ConfMenuStbCtrl initialized');

            // Breadcrumb Update
            breadcrumbService.add([
                { 'label': 'Opções do menu', 'href': 'javascript: void(0);' },
                { 'label': 'Definição' }
            ]);
            
            $scope.today = $filter('date')(new Date,'yyyy-MM-dd');
            
            $scope.menutreedata = [];
            $scope.menudeleted = [];
            var callMenus = function callMenus () {
                var responsePromise = $http.get(API_BASE_URL + '/IPVOD/rest/menu/main');
                responsePromise.success(function(dataFromServer, status, headers, config) {
                    var iterator = function iterator (obj) {
                        // se avaliableSince e avaliableUntil estiver setado, está agendado
                        // se a data atual não estiver entre avaliableSince e avaliableUntil, está agendado
                        for (var i = 0; i < obj.length; i++) {
                            var ob = obj[i], now = new Date().getTime();
                            ob.scheduled = false;

                            if ( ob.avaliableUntil && (now < ob.avaliableUntil) ) {
                                ob.scheduled = true; 
                            }
                            // if ( (ob.avaliableSince && ob.avaliableUntil) && (now < ob.avaliableSince && now < ob.avaliableUntil) ) { 
                            //     ob.scheduled = true;
                            // }
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
                responsePromise.error(function(dataFromServer, status, headers, config) {
                    $log.info(status);
                });
                return responsePromise;
            };
            callMenus();
            $scope.assettreedata = [];              

            // Select component
            $scope.selectComponent = {};
            $scope.selectComponent.selectedMovieCategories = [];
            $scope.selectComponent.movieCategories = [];
        
//            assetsService.getCategories().then(function (data) {
//                $scope.selectComponent.movieCategories = data.list;
//            });
            
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

            // Filter box visibility toggle 
            $scope.filterBoxCollapsed = false;
            $scope.toggleFilter = function toggleFilter (status) {
                var collapsed = (status !== undefined) ? status : !$scope.filterBoxCollapsed;
                $scope.filterBoxCollapsed = collapsed;
            };

            // Filter Fixed Positioning
//            var $confMenuFilter = angular.element('#conf-menu-filter'), 
//                $treeCol = angular.element('#tree-col'),
//                windowHeight = angular.element(window).height(),
//                elementOffset = $confMenuFilter.offset().top,
//                setConfMenuFilterWidth = function setConfMenuFilterWidth () {
//                    $confMenuFilter.css({ 'width': angular.element('#search-form-col').width() + 'px' });
//                },
//                removeStyle = function removeStyle () {
//                    $confMenuFilter.removeAttr('style'); 
//                };

//            angular.element(window).on('resize', function () {
//                if ( $confMenuFilter.hasClass('gvt-sticky') ) {
//                    setConfMenuFilterWidth();
//                } else {
//                    removeStyle();
//                }
//            });
//            $document.on('scroll', function() {
//                var scrollTop = angular.element(window).scrollTop(),
//                    distance = (elementOffset - scrollTop),
//                    confMenuFilterHeight = $confMenuFilter.height(),
//                    treeColHeight = $treeCol.height();
//
//                // if ( (distance < 0) && (confMenuFilterHeight < windowHeight ) ) { 
//                if ( (distance < 0) && (treeColHeight > confMenuFilterHeight) ) {
//                    $confMenuFilter.addClass('gvt-sticky'); 
//                    setConfMenuFilterWidth();
//
//                    if ( confMenuFilterHeight > windowHeight ) {
//                        $timeout(function() {
//                            $scope.toggleFilter(true);    
//                        }, 0);
//                    }
//                }
//                else { 
//                    $confMenuFilter.removeClass('gvt-sticky'); 
//                    removeStyle();
//                }
//            });


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
//                assetsService.getAssetsFilter(searchRules)
//                .then(function (data) {
//                    $scope.assettreedata = $scope.dataList = data.list;
//                    $scope.assetsPaginatorCurrentPage = currentPage;
//                    $scope.dataList = data.list;
//                    $scope.dataListVisibleRows = rows; 
//                    $scope.totalRows = data.count || 0;
//                    $scope.totalPages = window.Math.ceil($scope.totalRows / rows);
//                });
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
            $scope.toggleMenu = function(obj, active) {
                var menu = null;
                if (obj.$parentNodeScope !== undefined && 
                    obj.$parentNodeScope.$modelValue !== undefined &&
                    obj.$parentNodeScope.$modelValue.active === 0) {
                    active = 0;
                }
                if (obj.$modelValue !== undefined) {
                    menu = obj.$modelValue;
                } else {
                    menu = obj;
                }
                if (active !== undefined) {
                    toggleActivation(menu, active);
                } else {
                    toggleActivation(menu);
                }
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
                for (var i = 0; i < ipvodMenuList.length; i++) {
                    menuOrder(ipvodMenuList[i]);
                }
            };
                    
            var menuOrder = function (ipvodMenu) {
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
                    var iterator = function iterator (obj) {
                        for (var i = 0; i < obj.length; i++) {
                            var ob = obj[i];

                            // describe properties to remove here
                            delete ob.scheduled;

                            if ( ob.avaliableSince ) {
                                ob.avaliableSince = new Date(ob.avaliableSince).getTime();
                            }

                            if ( ob.avaliableUntil ) {
                                ob.avaliableUntil = new Date(ob.avaliableUntil).getTime();
                            }

                            if ( ob.ipvodVisualMenus && ob.ipvodVisualMenus.length ) {
                                iterator(ob.ipvodVisualMenus);
                            }
                        };
                    };

                    iterator($scope.menutreedata);

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
                            toaster.pop({'type': 'error', 'title': dataFromServer.errorMessage || 'Erro ao processar a requisição'});
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
            };

            $scope.removeMenu = function (obj) {
                $scope.menudeleted.push(obj.$modelValue);
                obj.remove();
            };
                    
            $scope.removie = function (obj) {
                console.log(obj);
                obj.remove();
            };
            
            $scope.getDetails = function (obj) {
                
                var responsePromise = $http.get(API_BASE_URL + '/IPVOD/rest/menu/cms/'+obj.$modelValue.menuId);
                responsePromise.success(function(dataFromServer, status, headers, config) {
                	obj.$modelValue.ipvodVisualMenus = dataFromServer.ipvodVisualMenus;
                	obj.$modelValue.ipvodAssets = dataFromServer.ipvodAssets;
                	obj.$modelValue.expanded = true;
                });
                
            };
            $log.info('ConfMenuStbCtrl execution ended');
        } ])


    //Controller for the modal
    .controller('MenuModalCtrl', 
        ['$scope', '$log', '$modalInstance', 'parentMenuList', 'itemType', 'editObject', '$http', 'API_BASE_URL',
         function ($scope, $log, $modalInstance, parentMenuList, itemType, editObject, $http, API_BASE_URL) {

    	var responsePromise = $http.get(API_BASE_URL + '/IPVOD/rest/rating'),
            type = {'asset': 'Asset', 'menu': 'Menu'},
            typeName = type[itemType] || type.menu;

        $scope.modalTitle = (editObject) ? 'Editar' : 'Novo Menu';

        $scope.ratingList = [];
        $scope.itemType = itemType;
        $scope.itemTypeName = typeName;

        responsePromise.success(function(dataFromServer) {
            var ratings = dataFromServer;
            $scope.completeRatingList = dataFromServer;
            $scope.ratingList = ratings;

            if ( !$scope.menu.ipvodRating ) {
                $scope.ipvodRating = $scope.ratingList[0];
            } else {
                for ( var i = 0; i < $scope.ratingList.length; i++) {
                    if ($scope.menu.ipvodRating.ratingLevel === $scope.ratingList[i].ratingLevel) {
                        $scope.ipvodRating = $scope.ratingList[i];
                    }
                }
            }
        });

        responsePromise.error(function(dataFromServer, status) {
            $log.info(status);
        });

        $scope.menu = {};

        if ( parentMenuList ) {
            $scope.menu.adult = false;

            $scope.ok = function ok () {
                var dateSince = new Date($scope.menu.avaliableSince).getTime(),
                    dateUntil = new Date($scope.menu.avaliableUntil).getTime(),
                    isValid = true;

                if ( dateSince > dateUntil ) {
                    $scope.menuForm.avaliableSince.$setValidity('avaliableSinceRangeInvalid', false);
                    isValid = false;
                }
                if ( dateUntil < dateSince ) {
                    $scope.menuForm.avaliableUntil.$setValidity('avaliableUntilRangeInvalid', false);
                    isValid = false;
                }

                if ( !isValid ) { return; }

                if ($scope.menuForm.menuName.$modelValue != '') {
                    parentMenuList.push({
                        active: 1,
                        ipvodAssets: new Array(),
                        ipvodVisualComponent: null,
                        ipvodVisualMenu: null,
                        ipvodVisualMenus: new Array(),
                        menuId: null,
                        ipvodRating : $scope.menuForm.rating.$modelValue,
                        name: $scope.menuForm.menuName.$modelValue,
                        avaliableSince: $scope.menuForm.avaliableSince.$modelValue === '' ? null : $scope.menuForm.avaliableSince.$modelValue,
                        avaliableUntil: $scope.menuForm.avaliableUntil.$modelValue === '' ? null : $scope.menuForm.avaliableUntil.$modelValue
                    });
                }
                $modalInstance.close();
            };
        } else {
            $scope.menu = editObject.$modelValue;

            // Form Models
            $scope.name = $scope.menu.name;
            $scope.title = $scope.menu.title;        
            $scope.avaliableSince = $scope.menu.avaliableSince;
            $scope.avaliableUntil = $scope.menu.avaliableUntil;
            $scope.ipvodRating = $scope.ratingList[0];
            $scope.ipvodPackages = $scope.ipvodPackages;
            		
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

            $scope.ok = function ok () {
                
                var now = new Date().getTime(),
                    avaliableSinceTime = new Date($scope.menuForm.avaliableSince.$modelValue).getTime(),
                    avaliableUntilTime = new Date($scope.menuForm.avaliableUntil.$modelValue).getTime();

                if ( $scope.menu.ipvodAssets ) {
                    $scope.menu.name = $scope.menuForm.menuName.$modelValue;
                } else {
                    $scope.menu.title = $scope.menuForm.menuName.$modelValue;
                }

                if ( $scope.menuForm.avaliableUntil.$modelValue && (now < avaliableUntilTime) ) {
                    $scope.menu.scheduled = true; 
                } else {
                    $scope.menu.scheduled = false;
                }

                $scope.menu.avaliableSince = $scope.menuForm.avaliableSince.$modelValue === '' ? null : $scope.menuForm.avaliableSince.$modelValue;
                $scope.menu.avaliableUntil = $scope.menuForm.avaliableUntil.$modelValue === '' ? null : $scope.menuForm.avaliableUntil.$modelValue;
                $scope.menu.ipvodRating = $scope.menuForm.rating.$modelValue;

                $modalInstance.close();
            };
        }

        $scope.getRating = function(rating, adult) {
        	if (adult) {
        		return $scope.completeRatingList[$scope.completeRatingList.length];
        	} else {
        		for ( var i = 0; i < $scope.ratingList.length; i++) {
        			if (rating == $scope.ratingList[i].ratingLevel) {
        				return $scope.ratingList[i];
        			}
        		}
        	}
        };
        $scope.validateAdult = function() {
            if ($scope.menu.ipvodRating.rating != '18') {
                $scope.menu.ipvodRating.adult = false;
            }
        };
        $scope.validateRating = function() {
            if ($scope.menu.ipvodRating.adult) {
                $scope.menu.ipvodRating = $scope.getRating($scope.ratingList.rating, $scope.ratingList.adult);
                List[$scope.ratingList.length-1];
            }
        };
        $scope.cancel = function cancel (event, saving) {       
            if ( saving ) { return; }

            // Prevent form submit
            event.preventDefault();
            
            // Prevent a new item to be created
            parentMenuList = null;

            // Close modal
            $modalInstance.close();
        };
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