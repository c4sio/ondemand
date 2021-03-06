/**
 * requestNotification
 * @description Provider used by loading-widget to handle the server requests.
 **/
(function(angular, vodModule) {
    'use strict';
    
    vodModule.provider('requestNotification', function requestNotification () {
         // This is where we keep subscribed listeners
         var onRequestStartedListeners = [],
             onRequestEndedListeners = [],

            // This is a utility to easily increment the request count
            count = 0,
            requestCounter = {
                increment: function() { count++; },
                decrement: function() {
                    if (count > 0) { count--; }
                },
                getCount: function() { return count; }
            };

        // Subscribe to be notified when request starts
        this.subscribeOnRequestStarted = function(listener) {
            onRequestStartedListeners.push(listener);
        };

        // Tell the provider, that the request has started.
        this.fireRequestStarted = function(request) {
            // Increment the request count
            requestCounter.increment();
            //run each subscribed listener
            angular.forEach(onRequestStartedListeners, function(listener) {
                // call the listener with request argument
                listener(request);
            });                
            return request;
        };

        // this is a complete analogy to the Request START
        this.subscribeOnRequestEnded = function(listener) {
            onRequestEndedListeners.push(listener);
        };


        this.fireRequestEnded = function() {
            requestCounter.decrement();
            var passedArgs = arguments;
            angular.forEach(onRequestEndedListeners, function(listener) {
                listener.apply(this, passedArgs);
            });
            return arguments[0];
        };

        this.getRequestCount = requestCounter.getCount;

        //This will be returned as a service
        this.$get = function() {
            var that = this;
            // just pass all the functions
            return {
                subscribeOnRequestStarted: that.subscribeOnRequestStarted,
                subscribeOnRequestEnded: that.subscribeOnRequestEnded,
                fireRequestEnded: that.fireRequestEnded,
                fireRequestStarted: that.fireRequestStarted,
                getRequestCount: that.getRequestCount
            };
        };
    });
    
})(window.angular, window.angular.module('vod'));