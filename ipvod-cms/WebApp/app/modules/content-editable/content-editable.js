(function (angular, vodModule) {
    'use strict';

vodModule
.directive('contenteditable', ['$sanitize', '$sce', 'helpers', function($sanitize, $sce, helpers) { 

    return {
        require: "ngModel",
        link: function(scope, element, attrs, ngModel) {
            var emptyText = 'Vazio';

            function read(e) {
                var html = attrs.stripBr ?  element.text() : element.html(),
                    trimmedString = html.trim();

                if ( trimmedString === '' || trimmedString === ' ' ) {
                    html = emptyText;
                    element.text(emptyText);
                }

                if ( e.which === 13 ) {
                    if ( !e.shiftKey || attrs.stripBr ) {
                        element.html( helpers.htmlToPlaintext(html) );
                    }
                }

                ngModel.$setViewValue(html);
            }

            ngModel.$render = function() {
                element.html(ngModel.$viewValue || '');
            };

            element.bind("blur keyup change", function(event) {
                scope.$apply(function () {
                    read(event);
                });
            });
        }
    };
}]);

})( window.angular, window.angular.module('vod') );