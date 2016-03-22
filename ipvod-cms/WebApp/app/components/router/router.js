(function (vodModule) {
    'use strict';
    
    vodModule// Url setup
    .config(['$routeProvider', function ($routeProvider) {

        // $routeProvider's shared config
        var routeParams = {
            prefix: '',
            suffix: 'GVT Video on Demand'
        };

        $routeProvider
        .when('/', {
            redirectTo: '/dashboard'
        })
        
        // Página Inicial (dashboard)
        .when('/dashboard', angular.extend({
            title: 'Dashboard',
            templateUrl: 'pages/dashboard/dashboard.html',
            controller: 'DashboardCtrl'
        }, routeParams))
        
        // Ingestão
        .when('/conteudo/ingestao', angular.extend({
            title: 'Conteúdo - Ingestão',
            templateUrl: 'pages/conteudo/conteudo.html',
            controller: 'ConteudoCtrl'
        }, routeParams))
        
        // Cadastrar Assets
        .when('/assets/categorias/cadastrar', angular.extend({
            title: 'Assets - Categorias - Cadastrar Categorias',
            templateUrl: 'pages/assets/assets-categorias-form.html',
            controller: 'AssetsCategoriasCtrl'
        }, routeParams))
        
        // Listar categorias de assets e categoria por tipo
        .when('/assets/categorias/:type?', angular.extend({
            title: 'Assets - Categorias',
            templateUrl: 'pages/assets/assets-categorias.html',
            controller: 'AssetsCategoriasCtrl'
        }, routeParams))
        
        // Editar assets da categoria especificada
        .when('/assets/categorias/:type?/:action?', angular.extend({
            title: 'Assets - Categorias',
            templateUrl: 'pages/assets/assets-categorias-form.html',
            controller: 'AssetsCategoriasCtrl'
        }, routeParams))

        // Cadastrar Pacotes
        .when('/assets/acervo', angular.extend({
            title: 'Assets - Acervo',
            templateUrl: 'pages/assets/assets-acervo.html',
            controller: 'AssetsAcervoCtrl'
        }, routeParams))
        
        // Cadastrar Pacotes
        .when('/assets/pacotes/cadastrar', angular.extend({
            title: 'Assets - Pacotes - Cadastrar Pacotes',
            templateUrl: 'pages/assets/assets-pacotes-form.html',
            controller: 'AssetsPacotesCtrl'
        }, routeParams))
        
        // Listar pacotes por tipo
        .when('/assets/pacotes/:type?', angular.extend({
            title: 'Assets - Pacotes',
            templateUrl: 'pages/assets/assets-pacotes.html',
            controller: 'AssetsPacotesCtrl'
        }, routeParams))
        
        // Editar assets do pacote especificado
        .when('/assets/pacotes/:type?/:action?', angular.extend({
            title: 'Assets - Pacotes',
            templateUrl: 'pages/assets/assets-pacotes-form.html',
            controller: 'AssetsPacotesCtrl'
        }, routeParams))
        
        // Login
        .when('/login', angular.extend({
            title: 'Login',
            templateUrl: 'pages/login/login.html',
            controller: 'LoginCtrl'
        }, routeParams))
        
        // Usuários
        .when('/usuarios', angular.extend({
            title: 'Usuários',
            templateUrl: 'pages/user/user-list.html',
            controller: 'UserRegisterCtrl'
        }, routeParams))

        // Usuários: Cadastrar
        .when('/usuarios/cadastrar', angular.extend({
            title: 'Usuários - Cadastrar',
            templateUrl: 'pages/user/user-register.html',
            controller: 'UserRegisterCtrl'
        }, routeParams))

        // Usuários: Cadastrar
        .when('/usuarios/:id/editar', angular.extend({
            title: 'Usuários - Editar',
            templateUrl: 'pages/user/user-register.html',
            controller: 'UserRegisterCtrl'
        }, routeParams))

        // Assinantes
        .when('/assinantes', angular.extend({
            title: 'Assinantes',
            templateUrl: 'pages/assinantes/assinantes.html',
            controller: 'AssinantesCtrl'
        }, routeParams))

        // Assinantes: Editar
        .when('/assinantes/:id/editar', angular.extend({
            title: 'Assinantes - Editar',
            templateUrl: 'pages/assinantes/assinantes-form.html',
            controller: 'AssinantesCtrl'
        }, routeParams))

        .when('/opcoes-do-menu/definicao', angular.extend({
            title: 'Definição do menu',
            templateUrl: 'pages/conf-menu-def/conf-menu-def.html',
            controller: 'ConfMenuDefCtrl'
        }, routeParams))

      .when('/opcoes-do-menu/menu/:menuId', angular.extend({
            title: 'Definição do menu - Ordenação e renomeação',
            templateUrl: 'pages/conf-menu-def/menu-detail.html',
            controller: 'MenuDetailCtrl'
        }, routeParams))
        
        // Editar assets da categoria especificada
        .when('/conteudo/ingestao/categorizacao', angular.extend({
            title: 'Ingestão - Categorização de dados',
            templateUrl: 'pages/conteudo/categorizacao.html',
            controller: 'CategorizacaoCtrl'
        }, routeParams))
        
         // Editar assets da categoria especificada
        .when('/conteudo/categorizacao', angular.extend({
            title: 'Ingestão - Categorização de Dados',
            templateUrl: 'pages/conteudo/categorizacao-lista.html',
            controller: 'CategorizacaoListaController'
        }, routeParams))
        
        .when('/notification/:notificationId', angular.extend({
            title: 'Notificações',
            templateUrl: 'pages/notification/notification-detail.html',
            controller: 'NotificationCtrl'
        }, routeParams))
        
        // Assets Lançamentos
        .when('/assets/lancamentos', angular.extend({
            title: 'Assets - Lançamentos',
            templateUrl: 'pages/assets/assets-lancamentos.html',
            controller: 'LancamentosCtrl'
        }, routeParams))

        // Assets Lançamentos
        .when('/assets/destaques', angular.extend({
            title: 'Assets - Destaques',
            templateUrl: 'pages/assets/assets-destaques.html',
            controller: 'LancamentosCtrl'
        }, routeParams))

        // Página não encontrada
        .when('/404', angular.extend({
            title: 'Não encontrado',
            templateUrl: 'pages/404/404.html',
            controller: 'NotFoundCtrl'
        }, routeParams))

        // Acesso Negado
        .when('/403', angular.extend({
            title: 'Acesso Negado',
            templateUrl: 'pages/403/403.html',
            controller: 'AccessDeniedCtrl'
        }, routeParams))
        
                // Usuários: Cadastrar
        .when('/purchase', angular.extend({
            title: 'Compras - Consulta / Exportar',
            templateUrl: 'pages/purchase/purchase.html',
            controller: 'PurchaseCtrl'
        }, routeParams))
        
        // Todas as outras ações
        .otherwise({ redirectTo: '/404' });
    }]);
    
})(window.angular.module('vod'));