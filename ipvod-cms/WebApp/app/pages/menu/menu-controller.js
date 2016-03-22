(function (vodModule) {
    'use strict';
    
    vodModule
      .controller('MenuCtrl', function ($log, $location, $scope, $route, MenuItemsService) {        
        $log.info('MenuCtrl initialized');
        
        var loc = function loc () {
                var l = $location.$$path.split('/');
                return l;
            },
            menu = {
                items: MenuItemsService.get(),
                active: null,
                checkStatus: function checkStatus (href) {
                    var l = loc();
                    if ( l.indexOf(href) !== -1 ) { return true; } 
                    return false;
                }
            };
        
        // Loc fornece um array com os fragmentos da url
        // No ng-repeat, o template verifica se o item da iteração atual está contido no array com os fragmentos
        // 
        debugger;
        
        $scope.menu = menu;
        
        $log.info('MenuCtrl execution ended');
      });
})(window.angular.module('vod'));