(function (angular) {
    'use strict';
    angular.module('breadcrumb', [])
    
    .factory('breadcrumbService', ['$rootScope', '$log', 
      function($rootScope, $log) {
          $log.info('breadcrumbService initialized.');
            var data = [],

            bcLabel = null,

            broadcast = function broadcast () {
                $rootScope.$broadcast( 'breadcrumbRefresh' );
            },

            getItems = function getItems (index, list) {
                var arr = [];
                angular.forEach(list, function (value, key) {
                    if ( key <= index ) { arr.push(value); }
                });

                return arr;
            },

            createLabel = function createLabel () {
                if ( !bcLabel ) { return ''; }
                return bcLabel.split('-');
            },

            // Parse the items to a format we can handle 
            parse = function parse (items) {
                var arr = [], label = createLabel();

                // If the first position is an empty string. Return an empty array.
                if (items[0] === '') { return arr; }

                angular.forEach(items, function(val, key) {
                    // There is a '/' in the end of the URL. Do nothing in this case.
                    if ( val === '' ) { return; }

                    var item = {};
                    item.label = label[key];

                    if ( key === 0 ) { item.href = '#/' + val; } 
                    else { item.href = '#/' + getItems(key, items).join('/'); }

                    arr.push(item);
                });

                return arr;
            };

          $log.info('breadcrumbService execution ended.');
          return {
            /**
             * @description Add multiple items to the breadcrumb
             * @param items {Array} URL params. Ex: ['dashboard', 'foo']
             * @param title {String} The document title, used to automatically create of breadcrumb items
             **/
            add: function add (items, title) {
                bcLabel = title;
                var parsedItems, 
                    itShouldParseItems = !!title;

                if ( itShouldParseItems ) { 
                    data = [];
                    parsedItems = parse(items); 
                    angular.forEach(parsedItems, function(val) {
                        data.push(val);
                    });
                }
                else { data = items; }

                broadcast();
            },

            // Get the breadcrumb items
            get: function get () { return data; },

            // Remove the items to the right, related to the current array index
            clearRight: function clearRight (index) {
                var arr = [];                
                angular.forEach(data, function (val, idx) {
                    if ( idx > index )  { arr.push(val); }
                });
                data = arr;
            }
        };
    }])
    .directive('breadcrumb', ['$window', '$log', 'breadcrumbService', 
       function($window, $log, breadcrumbService) { 
        try{window.ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
        return {
            restrict: 'AE',
            templateUrl : 'modules/breadcrumb/breadcrumb.html',
            replace: true,
            link: function link (scope, element, attrs) {
                var updateBreadcrumb = function updateBreadcrumb() {
                    var items = breadcrumbService.get();
                    scope.breadcrumb = [];
                    if ( items.length ) {
                        angular.forEach(items, function(v) {
                            scope.breadcrumb.push(v);
                        });
                    }
                };

                updateBreadcrumb();

                scope.unregisterBreadCrumb = function unregisterBreadCrumb ( event, index ) {
                    var $el = angular.element(event.currentTarget),
                        href = $el.attr('href'),
                        mustUnregister = href.search('void') === -1;

                    if ( !mustUnregister ) { return; }

                    breadcrumbService.clearRight( index );
                    updateBreadcrumb();
                };

                scope.$on( 'breadcrumbRefresh', function refreshBreadcrumb () {
                    updateBreadcrumb();
                });
            }
        };
    }]);
    
})(window.angular);