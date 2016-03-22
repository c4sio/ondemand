angular
  .module('vod')
  .factory('notification', function ($log) {
    $log.info('notification service initialized');

    var notification = {};

    return notification;
  });

