/* *
 * Grid Directive
 * @description: 
 * Directive to handle jQGrid plugin. The plugin's configuration must be set into the controller scope.
 * Multiple instances are supported. Decorator functions and shared configuration/tasks are defined here. 
 * Specifique methods should be set at instantiation, supplied as a config parameter.
 * 
 * @param {Object} gridOptions: The jQGrid table configuration. See the site docs for detailed info.
 * @param {Array} navOptions: The jQGrid nav configuration. See the site docs for detailed info.
 * 
 * */
(function (angular, vodModule) {
    'use strict';
    
    vodModule
    .directive('grid', ['$log', '$document', '$timeout', function($log, $document, $timeout) {
        $log.info('grid component initialized.');
        var 
            // Utility function to create unique ids for the grid table and nav
            uniqueGridId = function uniqueGridId (prefix) {
                var idPrefix = prefix || 'grid-', 
                    idCounter = 1, id, 
                    hasElement = function (counter) {
                        return !!document.getElementById(idPrefix+counter) === true;
                    };

                while ( hasElement(idCounter) ) { idCounter++; }

                id = idPrefix + idCounter;
                return id;                    
            },

            // Grid Initializer
            init = function init(gridOptions, navOptions, element, scope, elementAttrs) {

                if ( !gridOptions ) { return; }

                var 
                    // Grid Events
                    selectRowEvent = gridOptions.onSelectRow,
                    beforeSelectRowEvent = gridOptions.beforeSelectRow,
                    
                    gridId = uniqueGridId(), 
                    gridNavId = uniqueGridId('grid-nav-'),
                    gridElement = element.find('.grid-table'),
                    gridNavElement = element.find('.grid-nav'),
                    relativeSizeTo = elementAttrs.relativeSizeTo,
                    jQGridDefaultOptions = {
                        height: 250,
                        multiselect: true,
                        viewrecords : true,
                        pager : gridNavElement,
                        altRows: true,                  
                        multiboxonly: true,
                        loadError: function loadError (xhr, errorText) {
                            var table = this,
                                $el = angular.element(table);
                            displayAlert($el, 'Houve um erro ao recuperar os dados: '+ errorText, 'danger');
                        },
                        loadComplete : function loadComplete () {
                            var table = this;

                            updateActionIcons(table);
                            updatePagerIcons(table);
                            enableTooltips(table);
                        },
                        gridComplete: function gridComplete () {
                            var table = this,
                                $el = angular.element(table),
                                records = $el.jqGrid('getGridParam', 'records'), 
                                removeAlert = function removeAlert () {
                                    // Remove message
                                    var alertElement = angular.element('#'+id).closest('.grid-showing-alert'),
                                        hasAlert = alertElement.length;

                                    if ( hasAlert ) {
                                        alertElement
                                            .removeClass('grid-showing-alert')
                                            .find('.alert').remove();
                                    }
                                };

                            if ( records <= 0 ) { displayAlert($el); }
                            else {
                                $el.closest('.grid-showing-alert').removeClass('grid-showing-alert')
                                .find('.alert').remove();
                            }
                        },
                        beforeSelectRow: function beforeSelectRow (id, event) {
                            if ( typeof beforeSelectRowEvent === 'function' ) {

                                var removeAlert = function removeAlert () {
                                    // Remove message
                                    var alertElement = angular.element('#'+id).closest('.grid-showing-alert'),
                                        hasAlert = alertElement.length;

                                    if ( hasAlert ) {
                                        alertElement
                                            .removeClass('grid-showing-alert')
                                            .find('.alert').remove();
                                    }
                                };
                                removeAlert();
                                return beforeSelectRowEvent(id, event);
                            }
                        }, 
                        onSelectRow: function onSelectRow (id, status, event) {
                            debugger;
                            if ( typeof selectRowEvent === 'function' ) {
                                selectRowEvent(id);

                                var removeAlert = function removeAlert () {
                                    // Remove message
                                    var alertElement = angular.element('#'+id).closest('.grid-showing-alert'),
                                        hasAlert = alertElement.length;

                                    if ( hasAlert ) {
                                        alertElement
                                            .removeClass('grid-showing-alert')
                                            .find('.alert').remove();
                                    }
                                };

                                removeAlert();
                            }
                        }
                    },

                    //navbar options
                    jQGridNavDefaultOptions = [{
                            editicon : 'ace-icon fa fa-pencil blue',
                            addicon : 'ace-icon fa fa-plus-circle purple',
                            delicon : 'ace-icon fa fa-trash-o red',
                            search: true,
                            searchicon : 'ace-icon fa fa-search orange',
                            refresh: true,
                            refreshicon : 'ace-icon fa fa-refresh green',
                            viewicon : 'ace-icon fa fa-search-plus grey'
                        }, {
                            //edit record form
                            //closeAfterEdit: true,
                            //width: 700,
                            recreateForm: true,
                            beforeShowForm : function(e) {
                                var form = angular.element(e[0]);
                                form.closest('.ui-jqdialog')
                                    .find('.ui-jqdialog-titlebar')
                                    .wrapInner('<div class="widget-header" />');

                                styleEditForm(form);
                            }
                        }, {
                            //new record form
                            //width: 700,
                            closeAfterAdd: true,
                            recreateForm: true,
                            viewPagerButtons: false,
                            beforeShowForm : function(e) {
                                var form = angular.element(e[0]);
                                form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar')
                                    .wrapInner('<div class="widget-header" />');
                                styleEditForm(form);
                            }
                        }, {
                            //delete record form
                            recreateForm: true,
                            beforeShowForm : function(e) {
                                var form = angular.element(e[0]);
                                
                                if(form.data('styled')) { return false; }

                                form.closest('.ui-jqdialog')
                                    .find('.ui-jqdialog-titlebar')
                                    .wrapInner('<div class="widget-header" />');
                                styleDeleteForm(form);

                                form.data('styled', true);
                            },
                            onClick : function(e) { }
                        }, {
                            //search form
                            recreateForm: true,
                            afterShowSearch: function(e) {
                                var form = angular.element(e[0]);
                                form.closest('.ui-jqdialog').find('.ui-jqdialog-title').wrap('<div class="widget-header" />');
                                styleSearchForm(form);
                            },
                            afterRedraw: function(){
                                styleSearchFilters(angular.element(this));
                            },
                            multipleSearch: true,
                            /**
                            multipleGroup:true,
                            showQuery: true
                            */
                        }, {
                            //view record form
                            recreateForm: true,
                            beforeShowForm: function(e) {
                                var form = angular.element(e[0]);
                                form.closest('.ui-jqdialog').find('.ui-jqdialog-title').wrap('<div class="widget-header" />');
                            }
                        }],

                        jQGridOptions = angular.extend(gridOptions,jQGridDefaultOptions),
                        jQGridNavOptions = angular.extend(navOptions, jQGridNavDefaultOptions);

                    //resize to fit page size
                    angular.element(window).on('resize.jqGrid', function () {
                        gridElement.jqGrid( 'setGridWidth', angular.element(relativeSizeTo).width() );
                    });

                    //resize on sidebar collapse/expand
                    var parentColumn = gridElement.closest('[class*="col-"]');
                    $document.on('settings.ace.jqGrid' , function(ev, eventName, collapsed) {
                        if( eventName === 'sidebar_collapsed' || eventName === 'main_container_fixed' ) {
                            //setTimeout is for webkit only to give time for DOM changes and then redraw!!!
                            setTimeout(function() {
                                gridElement.jqGrid( 'setGridWidth', parentColumn.width() );
                            }, 0);
                        }
                    });

                    $timeout(function () {
                        gridElement.jqGrid(jQGridOptions);
                        gridElement.jqGrid('filterToolbar', {searchOperators : true, stringResult: true});
                        //navButtons
                        gridElement.jqGrid('navGrid', gridNavElement, jQGridNavOptions);
                        angular.element(window).triggerHandler('resize.jqGrid'); //trigger window resize to make the grid get the correct size
                    }, 1000);
                
                    function displayAlert( element, text ) {
                        var 
                            txt = text || $.jgrid.defaults.emptyrecords, // get the text
                            alertType = alertType || 'info',                        
                            container = element.parents('.ui-jqgrid-view'), // find the grid's container
                            alertContainer = angular.element('<div>', {'class': 'alert alert-' + alertType}).text(txt),
                            hasAlert = container.find('.alert').length;
                            
                            container.addClass('grid-showing-alert'); // hide the column headers and the cells below
                            // container.find('.ui-jqgrid-titlebar').after(alertContainer); // insert the empty data text
                            // 
                            if ( !hasAlert ) { 
                                container
                                    .find('.ui-jqgrid-bdiv')
                                    .append(alertContainer); // insert the empty data text
                            }
                    }

                    //switch element when editing inline
                    function aceSwitch( cellvalue, options, cell ) {
                        angular.element(cell) .find('input[type=checkbox]')
                            .addClass('ace ace-switch ace-switch-5')
                            .after('<span class="lbl"></span>');
                    }

                    function styleEditForm(form) {
                        //enable datepicker on 'sdate' field and switches for 'stock' field
                        form.find('input[name=sdate]').datepicker({format:'yyyy-mm-dd' , autoclose:true})
                            .end().find('input[name=stock]')
                                .addClass('ace ace-switch ace-switch-5').after('<span class="lbl"></span>');

                        //update buttons classes
                        var buttons = form.next().find('.EditButton .fm-button');
                        buttons.addClass('btn btn-sm').find('[class*="-icon"]').hide();//ui-icon, s-icon
                        buttons.eq(0).addClass('btn-primary').prepend('<i class="ace-icon fa fa-check"></i>');
                        buttons.eq(1).prepend('<i class="ace-icon fa fa-times"></i>');

                        buttons = form.next().find('.navButton a');
                        buttons.find('.ui-icon').hide();
                        buttons.eq(0).append('<i class="ace-icon fa fa-chevron-left"></i>');
                        buttons.eq(1).append('<i class="ace-icon fa fa-chevron-right"></i>');       
                    }

                    function styleDeleteForm(form) {
                        var buttons = form.next().find('.EditButton .fm-button');
                        buttons.addClass('btn btn-sm btn-white btn-round').find('[class*="-icon"]').hide();//ui-icon, s-icon
                        buttons.eq(0).addClass('btn-danger').prepend('<i class="ace-icon fa fa-trash-o"></i>');
                        buttons.eq(1).addClass('btn-default').prepend('<i class="ace-icon fa fa-times"></i>');
                    }

                    function styleSearchFilters(form) {
                        form.find('.delete-rule').val('X');
                        form.find('.add-rule').addClass('btn btn-xs btn-primary');
                        form.find('.add-group').addClass('btn btn-xs btn-success');
                        form.find('.delete-group').addClass('btn btn-xs btn-danger');
                    }
                
                    function styleSearchForm(form) {
                        var dialog = form.closest('.ui-jqdialog');
                        var buttons = dialog.find('.EditTable');
                        buttons.find('.EditButton a[id*="_reset"]').addClass('btn btn-sm btn-info').find('.ui-icon').attr('class', 'ace-icon fa fa-retweet');
                        buttons.find('.EditButton a[id*="_query"]').addClass('btn btn-sm btn-inverse').find('.ui-icon').attr('class', 'ace-icon fa fa-comment-o');
                        buttons.find('.EditButton a[id*="_search"]').addClass('btn btn-sm btn-purple').find('.ui-icon').attr('class', 'ace-icon fa fa-search');
                    }

                    //unlike navButtons icons, action icons in rows seem to be hard-coded
                    //you can change them like this in here if you want
                    function updateActionIcons(table) {
                        /**
                        var replacement = 
                        {
                            'ui-ace-icon fa fa-pencil' : 'ace-icon fa fa-pencil blue',
                            'ui-ace-icon fa fa-trash-o' : 'ace-icon fa fa-trash-o red',
                            'ui-icon-disk' : 'ace-icon fa fa-check green',
                            'ui-icon-cancel' : 'ace-icon fa fa-times red'
                        };
                        angular.element(table).find('.ui-pg-div span.ui-icon').each(function(){
                            var icon = angular.element(this);
                            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
                            if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
                        })
                        */
                    }

                    //replace icons with FontAwesome icons like above
                    function updatePagerIcons(table) {
                        var replacement = 
                        {
                            'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
                            'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
                            'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
                            'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
                        };
                        angular.element('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function() {
                            var icon = angular.element(this);
                            var $class = angular.element.trim(icon.attr('class').replace('ui-icon', ''));

                            if ($class in replacement) {icon.attr('class', 'ui-icon '+replacement[$class]);}
                        });
                    }

                    function enableTooltips(table) {
                        angular.element('.navtable .ui-pg-button').tooltip({container:'body'});
                        angular.element(table).find('.ui-pg-div').tooltip({container:'body'});
                    }

                    //var selr = gridElement.jqGrid('getGridParam','selrow');

                    $document.on('ajaxloadstart', function(e) {
                        gridElement.jqGrid('GridUnload');
                        angular.element('.ui-jqdialog').remove();
                    });
            },

            link = function link (scope, element, attrs) {
                scope.$watch('gridOptions', function(value) {
                    init(scope.gridOptions, scope.navOptions, element, scope, attrs);
                });
            },

            controller = function controller ($scope, $element, $attrs) {
                $scope.grid = { id: 'grid-' + $scope.$id };
                $scope.gridNav = { id: 'gridNavId-' + $scope.$id };
            };

        $log.info('grid component execution ended.');

        return {
            restrict: 'E',
            replace: true,
            templateUrl : 'components/grid/grid.html',
            scope: {
                'gridOptions': '=gridOptions',
                'navOptions': '=navOptions'
            },
            link: link,
            controller: controller
        };
    }]);
    
})( angular, angular.module('vod') );