(function($, angular) {
    'use strict';
    
    angular.module('gvtMenu', [])
    .factory('gvtMenuService', ['$log', '$location', 'auth', function gvtMenuService($log, $location, auth) {
        $log.info('gvtMenu initialized.');
        
        var items = [{
                'href': 'dashboard/',
                'text': 'Dashboard',
                'icon': 'fa fa-tachometer',
                'items': []
            }, {
                'href': 'conteudo/',
                'text': 'Conteúdo',
                'icon': 'fa fa-archive',
                'items': [{ 'href': 'ingestao', 'text': 'Ingestão' }, { 'href': 'categorizacao', 'text': 'Categorização' }]
            }, {
                'href': 'opcoes-do-menu/',
                'text': 'Opções do menu',
                'icon': 'fa fa-bars',
                'items': [
                    { 'href': 'definicao', 'text': 'Definição' }
                ]
            }, {
                'href': 'assets/',
                'text': 'Assets',
                'icon': 'fa fa-film',
                'items': [
	                  {
						'href': 'acervo/',
						'text': 'Acervo',
						'items': [] 
	                  }, {
						'href': 'categorias/',
						'text': 'Categorias',
						'items': [] 
	                  }, {
	                    'href': 'pacotes/',
	                    'text': 'Pacotes',
	                    'items': [] 
                      }, {
                          'href': 'lancamentos/',
                          'text': 'Lançamentos',
                          'items': []
                      }, {
                          'href': 'destaques/',
                          'text': 'Destaques',
                          'items': []
                      }
                ]
            }, {
                'href': 'usuarios/',
                'text': 'Usuários',
                'icon': 'fa fa-user',
                'items': []
            }, {
                'href': 'assinantes/',
                'text': 'Assinantes',
                'icon': 'fa fa-group',
                'items': []
            }, {
                'href': 'purchase/',
                'text': 'Compras',
                'icon': 'fa fa-credit-card',
                'items': []
            }
            ],
            get = function get () {
                var arrLen = items.length, 
                newItems = [];

                // Check available items to the current user
                for ( var i = 0; i < items.length; i++ ) {
                    var area = items[i].href.replace('/', '');
                    if ( auth.userHasPermission(area) ) {
                        newItems.push(items[i]);
                    }
                }

                return newItems;
            };
            
            $log.info('gvtMenu execution ended.');
            
            return {
                'get': get               
            };
    }])
    .directive('gvtMenu', [
        '$window', 
        '$log', 
        '$route', 
        '$rootScope', 
        '$location', 
        'gvtMenuService', 
        '$routeParams', 
        'helpers', 
        function ($window, $log, $route, $rootScope, $location, gvtMenuService, $routeParams, helpers) {
            
        try{ $window.ace.settings.check('sidebar' , 'fixed'); }catch(e){}
        try{ $window.ace.settings.check('sidebar' , 'collapsed'); }catch(e){}

        return {
            restrict: 'E',
            templateUrl : 'modules/menu/menu.html',
            replace: true,
            link: function link (scope) {
                var loc = function loc (path) {
                        var l = path.split('/');
                        l.splice(0,1);
                        return l;
                    },

                    menu = {
                        items: gvtMenuService.get(),

                        checkStatus: function checkStatus (href) {
                            var fragments = loc($location.$$path), 
                                locationArray = href.split('/'), 
                                comparator,
                                hasSearch = !helpers.isEmpty($location.$$search);
                            
//                            locationArray.reverse();
//                            if( href.indexOf('categorias') !== -1 ) { debugger; }
                            comparator = locationArray[0];
                            
                            // Multilevel menu. Get always the last fragment
                            if ( locationArray.length > 1 ) { 
                                var hasEmptyIndex = locationArray.indexOf('') !== -1;
                                comparator = locationArray[(hasEmptyIndex) ? locationArray.length - 2 : locationArray.length -1]; 
                            }

                            // If has a "?" sign, strip it from the string
                            if ( hasSearch ) { comparator = comparator.split('?')[0]; }

                            if ( comparator === '' ) {
                                comparator = locationArray.reverse()[0];
                            }

                            if ( fragments.indexOf(comparator) !== -1 ) {
                                return true; 
                            }
                            return false;
                        },
                        menuItemTemplate: 'menu-item.html'
                    };

                scope.menu = menu;
            }
        };
    }]);
    
})(window.$, window.angular);