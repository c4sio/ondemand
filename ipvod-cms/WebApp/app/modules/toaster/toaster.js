(function (angular){
    'use strict';

    /**
     * AngularJS Toaster (modified)
     * @description
     * Using it:
     * In a controller, require the toaster module as a dependency
     * 
     * toaster.pop({'type': 'error',  'title': 'Houve um erro ao processar a requisição', 'body': 'Lorem Ipsum dolor sit Amet'});
     * 
     * @module toaster
     * @method pop Pops a toast
     * @param {} options
     * @param string options.type Available types: 'info', 'warning', 'error', 'success'
     * @param string options.title A title for the toast.
     * @param string options.
     * 
     * Directive toaster-container
     * Usage: <toaster-container toaster-options="{}"></toaster-container>
     * @param {} toaster-options
     * @param number toaster-options.time-out Milliseconds for the toast to fade out. Fade is cancelled on mousehover.
     * @param string toaster-options.position-class Set the toast position. 
     *      Availabe options: toast-top-left, toast-top-right, toast-top-center, toast-top-full-width
     */
     angular.module('toaster', [])

     .service('toaster', function ($log, $timeout, $rootScope) {
        this.pop = function (options) {

            options = options || {};

            this.toast = {};
            angular.extend(this.toast, options);

            // Sanity check
            if ( !this.toast.title && !this.toast.body ) {
                throw new Error('options.title or options.body must be informed');
            }

            // Broadcast toast options to create a new one
            $rootScope.$broadcast('toaster-newToast', this);
        };
    })

     .constant('toasterConfig', {
        'tap-to-dismiss': true,
        'newest-on-top': true,
        //'on-fade-in': undefined,    // not implemented
        //'fade-out': 1000,           // done in css
        // 'on-fade-out': undefined,  // not implemented
        //'extended-time-out': 1000,    // not implemented
        'time-out': 5000, // Set timeOut and extendedTimeout to 0 to make it sticky
        'icon-classes': {
            error: 'fa fa-ban',
            info: 'fa fa-info-circle',
            success: 'fa fa-check-square-o',
            warning: 'fa fa-exclamation-circle'
        },
        'icon-class': 'fa fa-info-circle',
        'alert-class': 'alert-info',
        'alert-classes': {
            error: 'alert-danger',
            info: 'alert-info',
            success: 'alert-success', 
            warning: 'alert-warning'
        },
        'position-class': 'toast-top-center',
        'title-class': 'toast-title',
        'message-class': 'toast-message'
    })

    .directive('toasterContainer', [
        '$compile', 
        '$timeout', 
        'toasterConfig',
        function ($compile, $timeout, toasterConfig) {
          return {
            replace: true,
            restrict: 'EA',

            link: function link (scope, elm, attrs) {
                var id = 0, mergedConfig = toasterConfig;

                if (attrs.toasterOptions) {
                    angular.extend(mergedConfig, scope.$eval(attrs.toasterOptions));
                }

                scope.config = {
                    position: mergedConfig['position-class'],
                    title: mergedConfig['title-class'],
                    message: mergedConfig['message-class'],
                    tap: mergedConfig['tap-to-dismiss']
                };

                function setTimeout(toast, time) {
                    toast.timeout= $timeout(function () { 
                        scope.removeToast(toast.id);
                    }, time);
                }

                // Add a new toast
                function addToast (toast) {
                    var type = toast.type; 

                    toast.type = mergedConfig['alert-classes'][type];
                    toast.icon = mergedConfig['icon-classes'][type]; 

                    if (!toast.type) {
                        toast.type = mergedConfig['alert-class'];
                    }
                    if (!toast.icon) {
                        toast.icon = mergedConfig['icon-class'];
                    }

                    id++;
                    angular.extend(toast, { id: id });

                    if (mergedConfig['time-out'] > 0) {
                        setTimeout(toast, mergedConfig['time-out']);
                    }


                    if (mergedConfig['newest-on-top'] === true) {
                        scope.toasters.unshift(toast);
                    }
                    else {
                        scope.toasters.push(toast);
                    }
                }      

                scope.toasters = [];
                scope.$on('toaster-newToast', function (event, args) {
                    addToast(args.toast);
                });
            },
            controller: function($scope, $element, $attrs) {

                $scope.stopTimer = function(toast){
                    if(toast.timeout) {
                        $timeout.cancel(toast.timeout);
                    }
                };

                $scope.removeToast = function (id) {
                    var i = 0;
                    for (i; i < $scope.toasters.length; i++){
                        if($scope.toasters[i].id === id) {
                            break;
                        }
                    }
                    $scope.toasters.splice(i, 1);
                };

                $scope.remove = function(id){
                    if ($scope.config.tap === true) {
                        $scope.removeToast(id);
                    }
                };
            },
            templateUrl : 'modules/toaster/toaster.html'
        };
    }]);

})(window.angular);