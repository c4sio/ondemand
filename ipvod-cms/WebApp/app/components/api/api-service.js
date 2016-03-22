(function (angular, vodModule) {
  'use strict';

  vodModule
    .factory('api', function (API_BASE_URL, $log, $resource) {
        $log.info('api service initialized');

        var api = {};

        function getUrl(endpoint) {
            endpoint = endpoint || '/';
            return API_BASE_URL + endpoint;
        }

        api.contents = $resource(getUrl('/content'), null, {
            list : {method : 'GET'},
            create : {method : 'POST'}
        });

        api.content = $resource(
            getUrl('/content/:contentId'),
            {contentId : '@id'},
            {
                'get' : {method : 'GET'},
                'update' : {method : 'PUT'},
                'delete' : {method : 'DELETE'}
            }
        );

        return api;
    });

})(window.angular, window.angular.module('vod'));