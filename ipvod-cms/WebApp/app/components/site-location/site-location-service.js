/**
 * 
 * Turn the current route string into a class that
 * can be used for page-specific css rules
 * 
 * */
(function (vodModule) {
    'use strict';
    
    vodModule
    .factory('siteLocation', ['$log', '$location', function siteLocation($log, $location) {
        $log.info('Site Location service initialized');
        
        var prefix = 'gvt',
            innerClass = 'gvt-inner',
            locationHandler = function locationHandler (currentRoute) {
                var loc = {
                    locationClass: function locationClass () {
                        var str;
                        if ( !this.fragments.length ) { innerClass = ''; }                    
                        str = innerClass + ' ' + prefix;                        
                        if ( this.fragments.length ) { 
                            str = str + '-' + this.fragments[0].toLowerCase();
                        }
                        
                        return str;
                    },
                    fragments: null,
                    parse: function parse () {
                        if ( currentRoute ) {
                            var arr = currentRoute.split('/'), 
                                clone = Array.prototype.slice.call(arr),                                
                                lastIndex = arr.length-1,
                                reversed = clone.reverse();

                            // If the last array index is empty, take it out
                            if ( reversed[0] === '' ) { arr.splice(lastIndex); }

                            // Remove the first index, as it is always empty
                            arr.splice(0,1);

                            this.fragments = arr;
                        }
                    },
                    // getArea: function getArea () {
                    //     var area = null,
                    //         path = $location.$$path,

                    //         // As propriedades deste objeto devem conter parte da string contida na definição da rota
                    //         // Ex. "opcoes-do-menu" -  "menu", ou "opcoes" ou "opcoes-do".
                    //         obj = {
                    //             'dashboard': 'dashboard',
                    //             'conteudo': 'conteudo',
                    //             'opcoes-do-menu': 'menu',
                    //             'assets': 'assets',
                    //             'usuarios': 'usuarios'
                    //         };

                    //     angular.forEach(obj, function(value, key) {
                    //         if ( path.indexOf(key) !== -1 ) {
                    //             area = value;
                    //         }
                    //     });

                    //     return area;                    
                    // }
                };                

                loc.parse();

                return {
                    locationClass: loc.locationClass,
                    fragments: loc.fragments,
                    // getArea: loc.getArea
                };
            };
        
        $log.info('Site Location service execution ended');

        return locationHandler;
    }]);

})(window.angular.module('vod'));