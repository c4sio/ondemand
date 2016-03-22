(function (angular, vodModule) {
    'use strict';

    vodModule
      .factory('auth', [
        '$window', 
        '$http',
        '$q',
        '$log', 
        function ($window, $http, $q, $log) { 

        $log.info('auth service initialized');

        var TOKEN_PARAM = 'gvtToken',
            USER_PARAM = 'gvtUser',
            throwError = function throwError(msg) { throw new Error(msg); },
            setTokenAsHttpHeader = function setTokenAsHttpHeader (token) {
                $http.defaults.headers.common['cms-token'] = token; 
                $http.defaults.headers.common['CMS'] = true; 
            },
            getStorageMethod = function getStorageMethod () {
                var storageMethods = ['sessionStorage', 'localStorage'], str;
                for ( var i in storageMethods ) {
                    var method = $window[storageMethods[i]];
                    if ( method.getItem(TOKEN_PARAM) ) {
                        str = storageMethods[i];
                        break;
                    }
                }
                return str;
            },
            storageMethod = $window[getStorageMethod()],
            PERMISSIONS = {
                'dashboard': ['ADM','VOC', 'MKT', 'OPR'],
                'conteudo': ['ADM','VOC', 'MKT', 'OPR'],
                '403': ['ADM','VOC', 'MKT', 'OPR'],
                '404': ['ADM','VOC', 'MKT', 'OPR'],
                'opcoes-do-menu': ['ADM','VOC', 'MKT'],
                'assets': ['ADM','VOC', 'MKT', 'OPR'],
                'usuarios': ['ADM'],
                'assinantes': ['ADM'],
                'purchase': ['ADM','MKT','VOC']
            },
            auth = {};

        /**
         * Server response example:
            {
                "token": "026d372d9244536d05c4b0ce7d96f6587a4db6e17ca908b4c3cdf55d9b16e3f9",
                "user": {
                    "userSysId": 68,
                    "username": "admin",
                    "password": "21232f297a57a5a743894a0e4a801fc3",
                    "passwordConfirm": null,
                    "email": "toniogro@gmail.com",
                    "role": "ADM",
                    "active": true,
                    "contentProvider": null,
                    "registerType": null
                }
            }
        */
        auth.setToken = function setToken (token, remember) {
            var sm = remember ? $window.localStorage : $window.sessionStorage;
            sm.setItem(TOKEN_PARAM, token);
            return this;
        };

        auth.getToken = function getToken () {
            return storageMethod.getItem(TOKEN_PARAM);
        };

        auth.setUser = function setUser (usr) {
            if (typeof usr !== 'object') { throwError('Invalid user'); } 

            // set user credentials into into the sessionStorage;
            var userObjToString = $window.JSON.stringify(usr);

            if ( !storageMethod ) { storageMethod = $window[getStorageMethod()]; }

            storageMethod.setItem(USER_PARAM, userObjToString);
        };

        auth.isActive = function isActive () {
          // Check that the current session is still valid and active, e.g.
          // that the token has not expired yet.

          // Passes `false` to the promise callback while session information
          // is not set.

          // Must return a promise.

          // TODO implement auth.isActive()
        };

        auth.getUser = function getUser () { 
            var user = storageMethod.getItem(USER_PARAM);
            return $window.JSON.parse(user); 
        };

        auth.startSession = function startSession (sessionData, rememberCredentials) { 
            var authToken = sessionData.token, userInfo;

            sessionData || throwError('No sessionData informed');

            userInfo = sessionData.user;
            auth.setToken(authToken, rememberCredentials);
            auth.setUser(userInfo);
            setTokenAsHttpHeader(authToken);

            return $q.when(true);
        };

        auth.isAuthenticated = function isAuthenticated () {
            if ( !storageMethod ) { return false; }

            var user = this.getUser(),
                authToken = storageMethod.getItem(TOKEN_PARAM);
            setTokenAsHttpHeader(authToken);
            return !!user; 
        };

        auth.getUserRole = function getUserRole () {
            var role = this.getUser().role;
            return role;
        };

        auth.userRoleIs = function userRoleIs (role) {
            var user = this.getUser();
            if ( !user ) { return false; }

            user = $window.JSON.parse(user);
            return user.role === role;
        };

        auth.userHasPermission = function userHasPermission (area) {
            var role = this.getUserRole(), valid = false;

            area = area.replace('/', '');

            if ( !area ) {
                $log.error('Site area not informed.');
            }

            angular.forEach(PERMISSIONS, function (val, key) {
                if ( area.indexOf(key) !== -1 ) { area = key; }
            })

            if ( !PERMISSIONS.hasOwnProperty(area) ) {
                $log.error('Site area is invalid.');
            }

            // PERMISSIONS = {
            //     'dashboard': ['ADM', 'VOC', 'MKT', 'OPR'],
            //     'conteudo': ['ADM', 'VOC', 'MKT', 'OPR'],
            //     '403': ['ADM', 'VOC', 'MKT', 'OPR'],
            //     '404': ['ADM', 'VOC', 'MKT', 'OPR'],
            //     'menu': ['ADM', 'VOC', 'MKT'],
            //     'assets': ['ADM', 'VOC', 'MKT', 'OPR'],
            //     'usuarios': ['ADM']
            // }

            if ( PERMISSIONS[area] && PERMISSIONS[area].indexOf(role) !== -1 ) {
                valid = true;
            }
            return valid;
        };

        auth.destroySession = function destroySession () {
            storageMethod.removeItem(TOKEN_PARAM);
            storageMethod.removeItem(USER_PARAM);            
        };

        return auth;
    }]);

})(window.angular, window.angular.module('vod'));