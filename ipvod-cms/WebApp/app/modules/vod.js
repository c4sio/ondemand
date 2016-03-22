(function (angular) {
'use strict';
    
/**
 * Defines the base module for the web application and variables for
 * services and controllers.
 */

// Modules definitions
angular
  .module('vod', [
    // Third-party dependencies
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ui.bootstrap',
    'toaster',
	'breadcrumb',
	'gvtMenu',
    'ui.tree', 
    'ui.select',
    'paginator'
  ])
// Make sure the user is authenticated before running the app
.run(['PUBLIC_PAGE_URL', '$log', '$window', '$rootScope', 'auth', '$location', 'siteLocation', 'helpers', 'breadcrumbService',
    function (PUBLIC_PAGE_URL, $log, $window, $rootScope, auth, $location, siteLocation, helpers, breadcrumbService ) {

    if ( !auth.isAuthenticated() ) {
        $log.error('Aborted! Not authenticated.');
        $window.location.href = PUBLIC_PAGE_URL;
        return;
    }

    var user = auth.getUser();

    $rootScope.authenticated = true;
    $log.info('Authenticated as %s (%s)', user.username, user.userSysId); 

    // Location change start event
    $rootScope
    .$on('$locationChangeStart', function locationChangeStart (evt, next, current) {
        var area = $location.$$path;
        // Checking user permissions.
        // Gives him a 403 page if he's not allowed in the current site location
        if ( !auth.userHasPermission(area) ) { $location.path('/403'); }
    });

    // Route change success event
    $rootScope
    .$on('$routeChangeSuccess', function routeChangeSuccess (event, current) {
        var loc = siteLocation($location.$$path),
        params = current.params;

        // There are params in the URL.
        if ( !helpers.isEmpty(params) ) {
            // pegar "assets"
            var arr = current.$$route.title.split(' - ');
            if ( arr.length > 1 ) { arr = [arr[0]]; }

            angular.forEach(params, function (val) {
                arr.push(val);
            });

            var title = arr.join(' - ');

            current.$$route.title = title;
        }

        // There's a route defined for the current location
        if ( current.$$route ) {
            // Update the page title based on the route information
            $rootScope.title = helpers.stringSeparator([current.$$route.prefix, current.$$route.title, current.$$route.suffix].join('|'));

            // Add items to the Breadcrumb 
            breadcrumbService.add(loc.fragments, current.$$route.title);

            // Set the current location class for specific css rules
            $rootScope.locationClass = loc.locationClass();
        } else {
            // An invalid route was inserted into the address bar
            $rootScope.invalidRoute = $window.location.hash;
        }

        // Scroll to top
        angular.element("html, body").animate({scrollTop:0}, '500', 'swing');
    });
}]);

})(window.angular);
