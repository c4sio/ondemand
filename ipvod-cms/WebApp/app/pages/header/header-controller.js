(function(angular, vodModule) {
    'use strict';

    vodModule
    .controller('HeaderCtrl', [
    '$window', '$timeout','$scope', '$log', 'auth', '$interval', 'mensagensService', 'notificacoesService',
    function ($window, $timeout, $scope, $log, auth, $interval, mensagensService, notificacoesService) {
        $log.info('HeaderCtrl initialized');

        var user = auth.getUser(),
            userRole = user.role.toLowerCase(),
            messagesInterval = null,
            notificationsInterval = null,
            setMessagesInterval = function setMessagesInterval () { 
                if ( messagesInterval ) { 
                    $interval.cancel(messagesInterval);
                    $log.info('Messages interval cancelled');
                }
                // messagesInterval = $interval(fetchMessages, 60000);
                messagesInterval = $interval(fetchMessages, 60000);
                $log.info('Messages interval initialized');
            },
            setNotificationsInterval = function setNotificationsInterval () {
                if ( notificationsInterval ) { 
                    $interval.cancel(notificationsInterval);
                    $log.info('Notifications interval cancelled');
                }
                notificationsInterval = $interval(fetchNotifications, 60000);
                $log.info('Notifications interval initialized');
            },
            logout = function logout () {
                auth.destroySession();

                // Redirect to the public area
                $window.location =  $window.location.protocol + '//' + $window.location.host + '/ipvod-cms/app/vod-public.html';
            },
            fetchMessages = function fetchMessages () {
                $log.info('Trying to fetch messages...');
                mensagensService.get().then(function getMessages (messages) {
                    $scope.messages = messages.slice();

                    setMessagesInterval();

                    angular.element('#messages-icon')
                        .removeClass('icon-animated-vertical');
                    $timeout(function () {
                        angular.element('#messages-icon')
                        .addClass('icon-animated-vertical');
                    }, 200);
                    
                    $log.info('Messages fetched successfuly');
                });
            },
            fetchNotifications = function fetchNotifications () {
                $log.info('Trying to fetch notifications...');
                notificacoesService.get().then(function getNotifications (notifications) {
                    $scope.notifications = notifications.slice();

                    setNotificationsInterval();

                    angular.element('#notifications-icon')
                        .removeClass('icon-animated-bell');

                    $timeout(function () {
                        angular.element('#notifications-icon')
                        .addClass('icon-animated-vertical');
                    }, 200);
                    
                    $log.info('Notifications fetched successfuly');
                });
            },
            removeMessage = function removeMessage (event) {
                // Prevents dropdown from closing
                event.stopImmediatePropagation();

                setMessagesInterval();

                var $el = angular.element(event.currentTarget),
                    $messageHolder = $el.closest('li'),
                    messageId = parseInt( $messageHolder.attr('id').replace('msg-', '') ),
                    msgIndex;

                for ( var i in $scope.messages ) {
                    if ( $scope.messages[i].id === messageId ) {
                        msgIndex = i; break;
                    }
                }

                $messageHolder.slideUp(800, function() {
                    $messageHolder.remove();
                    angular.element('#header-messages-holder li').show();
                });

                $timeout(function() {
                    // Removes the given index from the array
                    $scope.messages.splice(msgIndex, 1);
                }, 1000);
            },
            removeNotification = function removeNotification (event) {
                // Prevents dropdown from closing
                event.stopImmediatePropagation();

                setNotificationsInterval();

                var $el = angular.element(event.currentTarget),
                    $notificationHolder = $el.closest('li'),
                    notificationId = parseInt( $notificationHolder.attr('id').replace('not-', '') ),
                    notifIndex;

                for ( var i in $scope.notifications ) {
                    if ( $scope.notifications[i].id === notificationId ) {
                        notifIndex = i; break;
                    }
                }

                $notificationHolder.slideUp(800, function() {
                    $notificationHolder.remove();
                    angular.element('#header-notifications-holder li').show();
                });

                $timeout(function() {
                    // Removes the given index from the array
                    $scope.notifications.splice(notifIndex, 1);
                }, 1000);
            };

        user.picture = userRole === 'opr' ? 'user' : 'user-admin';

        fetchMessages();

        fetchNotifications();

        // Fetching messages
        // setMessagesInterval();
        // $interval(fetchMessages, 10000);

        $scope.messages = [];
        $scope.notifications = [];
        $scope.user = user;
        $scope.logout = logout;
        $scope.removeMessage = removeMessage;
        $scope.removeNotification = removeNotification;

        $log.info('HeaderCtrl execution ended');
    }]);

})(angular, angular.module('vod'));