(function () {
  'use strict';

  /**
   * Production settings
   */
  angular
    .module('vod')

    // FIXME Set production base URL for the API
    .value('API_BASE_URL', 'http://186.215.183.213')

    // FIXME Set production URL for the public part of the application
    .value('PUBLIC_PAGE_URL', null)

    // FIXME Set production URL for the protected part of the application
    .value('PROTECTED_PAGE_URL', null)
  
    // General App Config
    .config([
        '$compileProvider', 
        '$httpProvider', 
        'requestNotificationProvider', 
        'httpInterceptorProvider',
        function ($compileProvider, $httpProvider, requestNotificationProvider, httpInterceptorProvider) {
        // requestNotification Provider
        // keeps track of the server requests and uses the requestNotificationProvider to show/hide a status message
        var fireRequestStarted = function fireRequestStarted (data) {
                requestNotificationProvider.fireRequestStarted(data);
                return data;
            },
            fireRequestEnded = function fireRequestEnded (data) {
                requestNotificationProvider.fireRequestEnded(data);
                return data;
            };

        // The following protocols will be white-listed
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|file|javascript):/);
      
        //Enable cross domain calls
        $httpProvider.defaults.useXDomain = true;

        //Remove the header used to identify ajax call  that would prevent CORS from working
        delete $httpProvider.defaults.headers.common['X-Requested-With'];
        
        $httpProvider.defaults.transformRequest
            .push(fireRequestStarted);

        $httpProvider.defaults.transformResponse
            .push(fireRequestEnded);

        // Augment http interceptors with our interceptor
        $httpProvider.interceptors
            .push('httpInterceptor');
    }]);
})();