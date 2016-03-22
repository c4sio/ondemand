angular.module('vod')
  .controller('UsuariosCtrl', 
['$scope', '$location', '$http', 'toasterMessage', 'usuariosService', '$routeParams', '$modal',
function($scope, $location, $http, toasterMessage, usuariosService, $routeParams, $modal) {
    'use strict';

    // Available actions: 'cadastrar', 'editar', 'listar'.
    var checkActions = function checkActions () {
                var path = $location.$$path;
                if ( /cadastrar/.test(path) ) { return 'cadastrar'; } 
                else if ( /editar/.test(path) ) { return 'editar'; }
                else { return 'listar'; }
            },        
            action = checkActions(),
            actionHandler = {};
    
    $scope.submitForm = function() {
        if ( $scope.userForm.password !== $scope.userForm.passwordConfirm ) {
            $scope.form.password.$invalid = true;
            $scope.form.passwordConfirm.$invalid = true;
            $scope.form.$valid = false;
        }

        if ( $scope.form.$valid ) {
            usuariosService.create($scope.userForm, function (data) {
                toasterMessage.setMessage(data.message);
            });
        }

    };

    actionHandler.listar = function listar () {
        $scope.users = usuariosService.get();
        var modalOptions = {
            'template': angular.element('#modal-template').html(),
            'controller': 'UsersModalCtrl',
            'size': 'sm'
        };
        $scope.removeItem = function removeItem (id, label) {
            modalOptions.resolve = {
                'id': function () { return id; },
                'label': function () { return label; }
            };
            $modal.open(modalOptions);
        };
    };

    actionHandler.cadastrar = function cadastrar () {
        // User ADD scope variables
        $scope.userRoles = ['USER','ADMIN'];
        $scope.userForm = {};
        $scope.userForm.username = '';
        $scope.userForm.password = '';
        $scope.userForm.passwordConfirm = '';
        $scope.userForm.email = '';
        $scope.userForm.role = $scope.userRoles[0];
        
    };
    actionHandler.editar = function editar () {
        $scope.userRoles = ['USER','ADMIN'];
        $scope.userForm = usuariosService.get($routeParams.id);
//        angular.extend($scope, usuariosService.get($routeParams.id));
    };
    
    $scope.action = action;
    
    // Execute action handler
    actionHandler[action]();
    
}])

// Controller for the modal
.controller('UsersModalCtrl', 
    ['$scope', '$log', '$modalInstance', 'id', 'label', function ($scope, $log, $modalInstance, id, label) {
    'use strict';

    $scope.modalTitle = null;
    $scope.modalBody = 'Deseja realmente remover o usuário "' + label + '"?';
    $scope.ok = function ok () {
        // Make delete request here
        $modalInstance.close();
        $log.info('Modal OK');
    };
    $scope.cancel = function cancel () {
        $modalInstance.close();
        $log.info('Modal Canceled');
    };

}]);