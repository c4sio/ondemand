(function (angular, vodModule) {
    'use strict';
    
    /**
     * AngularJS default filter with the following expression:
     * "person in people | filter: {name: $select.search, age: $select.search}"
     * performs a AND between 'name: $select.search' and 'age: $select.search'.
     * We want to perform a OR.
     * Used by ui.select
     */
    vodModule.filter('propsFilter', function() {
        return function(items, props) {
            var out = [];
            if ( !items ) { debugger; return out; }
            if (angular.isArray(items)) {
                angular.forEach('items', function (item) {
                    var itemMatches = false,
                        getObjectKeys = function getObjectKeys (properties) {
                            var arr = [];
                            angular.forEach(properties, function (value, key) {
                                arr.push(key);
                            });
                            return arr;
                        },
                        keys = getObjectKeys(props);

                    for (var i = 0; i < keys.length; i++) {
                        var prop = keys[i];
                        var text = props[prop].toLowerCase();
                        if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                            itemMatches = true;
                            break;
                        }
                    }
                    if (itemMatches) { out.push(item); }
                });
            } else {
              // Let the output be the input untouched
              out = items;
            }

            return out;
        };
    });

})(window.angular, window.angular.module('vod'));