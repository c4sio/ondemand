angular.module('vod')
  .controller('UserRegisterCtrl', [
        '$scope', 
        '$http', 
        '$location', 
        '$routeParams',
        'toaster', 
        'userService', 
        'API_BASE_URL', 
        '$modal', 
        '$rootScope',
        'breadcrumbService',
      function($scope, $http, $location, $routeParams, toaster, userService, API_BASE_URL, $modal, $rootScope, breadcrumbService) {
    var checkActions = function checkActions () {
        var path = $location.$$path;
        if ( /cadastrar/.test(path) ) { return 'cadastrar'; } 
        else if ( /editar/.test(path) ) { return 'editar'; }
        else { return 'listar'; }
    };
    var action = checkActions();
    var actionHandler = {};
    
    actionHandler.cadastrar = function cadastrar () {
        initializeForm();
        $scope.userRegister = {};
        $scope.userRegister.registerType = $scope.registerTypes[0];
        $scope.reset();
    };
    
    var initializeForm = function (run) {
        var promisse = $http.get(API_BASE_URL + '/IPVOD/rest/contentProvider');
        promisse.success(function (responseData) {
            $scope.contentProviders = responseData;
            if ( typeof run === 'function' ) {
                run();
            }
        }); 
        $scope.registerTypes = ['ADM','GVT','OPERADORAS'];
        $scope.userRoles = ['MKT','VOC'];
    };
    
    $scope.reset = function(){
        $scope.userRegister.username = "";
        $scope.userRegister.password = "";
        $scope.userRegister.passwordConfirm = "";
        $scope.userRegister.email = "";
        $scope.userRegister.role = $scope.userRoles[0];
    };
    
    actionHandler.listar = function listar () {
        userService.getUserList().then(function (dataFromServer) {
            $scope.users = dataFromServer;
        });
        var modalOptions = {
            'template': angular.element('#modal-template').html(),
            'controller': 'UsersListModalCtrl',
            'size': 'sm'
        };
        $scope.removeUser = function removeUser (id, label) {
            modalOptions.resolve = {
                'id': function () { return id; },
                'label': function () { return label; }
            };
            $modal.open(modalOptions);
        };
    };

    actionHandler.editar = function editar () {
        
        initializeForm(function () {
            userService.getById($routeParams.id)
            .then(
                function(data) {
                    // Update breadcrumb
                    breadcrumbService.add([
                        {'label': 'Usuários', 'href': '#/usuarios'},
                        {'label': data.username }
                    ]);

                    $scope.userRegister = data;	
                    
                    if (data.role == 'VOC' || data.role == 'MKT' ) {
                        $scope.userRegister.registerType = 'GVT';
                    } else if (data.role == 'ADM') {
                    	$scope.userRegister.registerType = 'ADM';
                    } else {
                        $scope.userRegister.registerType = 'OPERADORAS';

                        for (var i = 0; i< $scope.contentProviders.length; i++){
                            if ( $scope.userRegister.contentProvider && ($scope.userRegister.contentProvider.contentProviderId == $scope.contentProviders[i].contentProviderId ) ) {
                                $scope.userRegister.contentProvider = $scope.contentProviders[i];
                                break;
                            }
                        }
                    }
                }
            );
        });
    };

    $rootScope.reloadList = function () {
         userService.getUserList().then(function (dataFromServer) {
            $scope.users = dataFromServer;
         });
    };
    $scope.sendForm = function(userRegister) {

        if (userRegister.registerType == 'GVT') {
            if (userRegister.username == "" || userRegister.username == undefined) {
                return;
            }
            userRegister.contentProvider = null;
            userRegister.password = "";
            userRegister.passwordConfirm = "";
            userRegister.email = "";
        } else if (userRegister.registerType == 'ADM') {
        	 if (userRegister.username == "" || userRegister.username == undefined ||
                     userRegister.password == "" || userRegister.password == undefined) {
                 return;
             }
        	 if (userRegister.passwordConfirm != userRegister.password) {
                 toaster.pop({'type': 'error', 'title': 'Senhas não conferem.'});
                 return;
             }
        	 userRegister.role = 'ADM';
        } else {
            if (userRegister.username == "" || userRegister.username == undefined ||
                userRegister.password == "" || userRegister.password == undefined ||
                userRegister.passwordConfirm == "" || userRegister.passwordConfirm == undefined ||
                userRegister.email == "" || userRegister.email == undefined ||
                userRegister.registerType == "" || userRegister.registerType == undefined) {
                return;
            }
            if (userRegister.passwordConfirm != userRegister.password) {
                toaster.pop({'type': 'error', 'title': 'Senhas não conferem.'});
                return;
            }
            userRegister.role = 'OPR';
        }
        userRegister.active = true;
        userService.create(userRegister, function (data) {
            if ($scope.action == 'cadastrar')
                toaster.pop({'type': 'success', 'title': 'Usuário criado.'});
            else if ($scope.action == 'editar' )
                toaster.pop({'type': 'success', 'title': 'Usuário alterado.'});
            window.location.hash = "#/usuarios";
        });
    };
    
    $scope.deactivate = function (id, username) {
        userService.deactivateUser(id).then(
            function (){
                actionHandler.listar();
                toaster.pop({'type': 'success', 'title': 'Usuário ' + username + ' Desativado'});
            }
        );
    };
    
    $scope.activate = function (id, username) {
        userService.activateUser(id).then(
            function (){
                actionHandler.listar();
                toaster.pop({'type': 'success', 'title': 'Usuário ' + username + ' Ativado'});
            }
        );
    };
    
    $scope.action = action;
    
    // Execute action handler
    actionHandler[action]();
}])

 // Controller for the modal
.controller('UsersListModalCtrl', 
    ['$scope', '$log', '$modalInstance', 'id', 'label', 'userService', 'toaster', '$rootScope', 
     function ($scope, $log, $modalInstance, id, label, userService, toaster, $rootScope) {
        $scope.modalTitle = null;
        $scope.modalBody = 'Deseja realmente remover o usuário "' + label + '"?';
        $scope.ok = function ok () {
            // Make delete request here
            userService.deleteUser(id).then(
                    function (data) {
                        toaster.pop({'type': 'success', 'title': 'Usuário excluído'});
                        $rootScope.reloadList();
                    }
            );
            $modalInstance.close();
        };
        $scope.cancel = function cancel () {
            $modalInstance.close();
        };
}]);
