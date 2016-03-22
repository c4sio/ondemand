// Karma configuration
// http://karma-runner.github.io/0.12/config/configuration-file.html
// Generated on 2014-09-01 using
// generator-karma 0.8.3

module.exports = function (config) {
    'use strict';

    config.set({
        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: true,

        // base path, that will be used to resolve files and exclude
        basePath: '../',

        // testing framework to use (jasmine/mocha/qunit/...)
        frameworks: ['mocha'],

        // list of files / patterns to load in the browser
        files: [
            'bower_components/angular/angular.js',
            'bower_components/angular-mocks/angular-mocks.js',
            'bower_components/angular-animate/angular-animate.js',
            'bower_components/angular-cookies/angular-cookies.js',
            'bower_components/angular-resource/angular-resource.js',
            'bower_components/angular-route/angular-route.js',
            'bower_components/angular-sanitize/angular-sanitize.js',
            
            // App's dependencies
            'app/modules/vod.js',            
            'app/config.js',            
            'app/modules/toaster/toaster.js',
            'app/modules/breadcrumb/breadcrumb.js',
            'app/components/auth/auth-service.js',
            'app/components/api/api-service.js',
            'app/components/site-location/site-location-service.js',
            'app/components/helpers/helpers-service.js',
            
            'app/pages/conteudo/ingestao-service.js',
            
            //Test-Specific Code
            'node_modules/chai/chai.js',
            'test/chai/chai-should.js',
            'test/chai/chai-expect.js',
            
            //Test files
            'app/**/*_test.js'
        ],

        // list of files / patterns to exclude
        exclude: [
            'app/assets/**/*.js',
            'test/**/*.js'
        ],

        // web server port
        port: 9999,

        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera
        // - Safari (only Mac)
        // - PhantomJS
        // - IE (only Windows)
        browsers: [
//            'PhantomJS'
            'Chrome'
        ],

        // Which plugins to enable
        plugins: [
            'karma-phantomjs-launcher',
            'karma-chrome-launcher',
            'karma-mocha'
        ],

        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun: false,

        colors: true,

        // level of logging
        // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
        logLevel: config.LOG_INFO

        // Uncomment the following lines if you are using grunt's server to run the tests
        // proxies: {
        //   '/': 'http://localhost:9000/'
        // },
        // URL root prevent conflicts with the site root
        // urlRoot: '_karma_'
    });
};
