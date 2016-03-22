 /**
  * @description
  * Directive to handle data pagination.
  * @param {Array} paginatorListData The data to paginate
  * @param {number} paginatorListVisibleRows The number of rows being displayed
  * @param {number} paginatorCurrentPage The active page
  * @param {number} paginatorOffset The offset of pages being displayed in the paginator
  * @param {number} paginatorTotalPages The total number of pages
  * @param {Function} paginatorControlOnClick The callback function executed on the paginator control click
  */
(function(angular) {
    'use strict';

    angular
    .module('paginator', [])
    .factory('paginatorService', [function() {
        var defaultParams = { paginatorOffset: 5 },

            paginationMaker = function paginationMaker (totalPages, itemsPerPage) {
                if ( totalPages === undefined ) { throw new Error('No total pages supplied'); }

                var pages = [],
                    paginatorCurrentPage,
                    listVisibleRows,
                    offsetGroups = [],
                    pageCount = totalPages,

                    // Format: [[1,2,3,4,5], [6,7,8,9,10]]
                    makeOffsetGroups = function makeOffsetGroups () {
                        var pagesIndex, offsetControlIndex = 0, arr = [];

                        if ( pageCount <= 1 ) {
                            arr.push(1);
                            offsetGroups.push(arr);
                            return;
                        }

                        for ( pagesIndex = 1; pagesIndex <= pageCount; pagesIndex++ ) {
                            if ( offsetControlIndex < itemsPerPage ) {
                                arr.push( pagesIndex );
                            } else {
                                offsetGroups.push(arr);
                                arr = [];
                                arr.push( pagesIndex );
                                offsetControlIndex = 0;
                            }
                            offsetControlIndex++;
                        }

                        // If there are any items remaining in arr, push them
                        // to the offsetGroups.
                        if ( arr.length ) { offsetGroups.push(arr); }
                    },
                    getOffset = function getOffset (page) {
                        var offsetIndex = 0, found = false;
                        angular.forEach(offsetGroups, function (value, key) {
                            angular.forEach(value, function (v) {
                                if ( v === page ) { 
                                    offsetIndex = key; 
                                    found = true;
                                }
                            });
                        });
                        return offsetIndex;
                    },
                    // getOffsetPosition = function getOffsetPosition (page, offset) {
                    //     var index = offsetGroups[offset];
                    //     return index.indexOf(page);
                    // },
                    makePages = function makePages (page, totalPageCount, totalVisibleRows) {
                        var totalPageCountHasChanged, listVisibleRowsHasChanged;
                        totalVisibleRows = parseInt(totalVisibleRows);

                        listVisibleRowsHasChanged = listVisibleRows !== totalVisibleRows;

                        if ( totalPageCount ) { 
                            totalPageCountHasChanged = totalPageCount !== pageCount;
                            pageCount = totalPageCount;
                        } else {
                            return 0;
                        }

                        if ( !page ) { throw new Error('A Page must be informed.'); }

                        if ( totalPageCountHasChanged || listVisibleRowsHasChanged ) {
                            makeOffsetGroups();
                        }

                        var currentOffset = getOffset(page),
                            offsetGroup = offsetGroups[currentOffset],
                            arr = [];

                        paginatorCurrentPage = page;
                        
                        angular.forEach(offsetGroup, function (pageNumber) {
                            arr.push({ 'num': pageNumber, 'active': pageNumber === paginatorCurrentPage });
                        });

                        pages = arr;

                        listVisibleRows = totalVisibleRows;

                        return pages;
                    };

                // Initializing it the paginator.
                makeOffsetGroups();

                return { 
                    makePages: makePages
                };
            };
        
        return { 
            defaultParams: defaultParams,
            paginationMaker: paginationMaker
        };
        
    }])
    .directive('paginator', ['$timeout', 'paginatorService', function ($timeout, paginatorService) {
        var paginatorDirective = {};
        paginatorDirective.restrict = 'E';
        paginatorDirective.templateUrl = 'modules/paginator/paginator.html';
        paginatorDirective.replace = true;

        paginatorDirective.scope = {
            paginatorListData: '=paginatorListData',
            paginatorListVisibleRows: '=paginatorListVisibleRows',
            paginatorCurrentPage: '=paginatorCurrentPage',
            paginatorOffset: '=paginatorOffset',
            paginatorItemsPerPage: '=paginatorItemsPerPage',
            paginatorTotalPages: '=paginatorTotalPages',
            paginatorControlOnClick: '=paginatorControlOnClick',
            paginatorPageButtonOnClick: '=paginatorPageButtonOnClick'
        };

        // Directive link
        paginatorDirective.link = function link (scope) {
            var itemsPerPage = scope.paginatorItemsPerPage || paginatorService.defaultParams.paginatorOffset,
                init = function init () {
                    if ( !scope.paginatorCurrentPage ) { return; }

                    // The paginator first load
                    if ( !scope.paginator ) {
                        scope.paginator = paginatorService.paginationMaker( scope.paginatorTotalPages, itemsPerPage );
                    }
                    scope.paginatorLastPage = scope.paginatorTotalPages || 1;

                    scope.pages = scope.paginator.makePages(scope.paginatorCurrentPage, scope.paginatorTotalPages, scope.paginatorListVisibleRows);                    
                };

            scope.$watch('paginatorCurrentPage', init);
            scope.$watch('paginatorListVisibleRows', init);
            scope.$watch('paginatorTotalPages', init);
        };

        // Directive Controller
        paginatorDirective.controller = function controller ($scope) {
            // Actions for the paginator controllers prev() and next()
            $scope.paginatorControl = function paginatorControl (controlType) {
                var pageOperation = $scope.paginatorCurrentPage,
                    mustLoad = true,
                    paginatorNavHandler = {
                        // Go to the first paginator page
                        first: function first () {
                            if ( $scope.paginatorCurrentPage === 1 ) { mustLoad = false; }
                            pageOperation = 1;
                        },
                        // Go to the previous paginator page
                        prev: function prev () {
                            if ( $scope.paginatorCurrentPage === 1 ) { mustLoad = false; }
                            pageOperation = pageOperation-1;
                        },
                        // Go to the next paginator page
                        next: function next () {
                            if ( $scope.paginatorCurrentPage === $scope.paginatorLastPage ) { mustLoad = false; }
                            pageOperation = pageOperation+1;
                        },
                        // Go to the last paginator page
                        last: function last () {
                            if ( $scope.paginatorCurrentPage === $scope.paginatorLastPage ) { mustLoad = false; }
                            pageOperation = $scope.paginatorLastPage;
                        }
                    };

                // Call Paginator Handler
                paginatorNavHandler[controlType]();

                if ( mustLoad ) {
                    $scope.paginatorControlOnClick(pageOperation);
                }
            };
        };

        return paginatorDirective;
    }]);

})(window.angular);