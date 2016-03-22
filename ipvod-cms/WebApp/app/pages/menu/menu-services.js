(function (vodModule) {
    'use strict';
    
    vodModule
        .factory('MenuItemsService', ['$log', '$http', 'API_BASE_URL', function MenuItemsService($log, $http, API_BASE_URL) {
            $log.info('MenuItemsService initialized.');
        	var responsePromise = $http.get(API_BASE_URL + "/IPVOD/rest/category");
        	var categs = [];
			responsePromise.success(function(dataFromServer, status, headers, config) {
                //categs = [];
				angular.forEach(dataFromServer, function (value) {
					var categ = {}; 
					categ.href = "?categ=" + value.categoryId;
					categ.text = value.description;
					categ.id = value.categoryId;
					categs.push(categ);
				});
				angular.forEach(items, function (item) {
					if (item.text == "Assets") {
						item.items = categs;
					}
				});
    		});
    		responsePromise.error(function(data, status, headers, config) {
    			$log.info(data);
    		});
            var items = [{
                'href': 'dashboard',
                'text': 'Dashboard',
                'icon': 'fa fa-tachometer',
                'items': []
            }, {
                'href': 'conteudo',
                'text': 'Conteúdo',
                'icon': 'fa fa-archive',
                'items': [
                    { 'href': 'ingestao', 'text': 'Ingestão' }                
                ]
            }, {
                'href': 'opcoes-do-menu',
                'text': 'Opções do menu',
                'icon': 'fa fa-bars',
                'items': [
                    { 'href': 'definicao', 'text': 'Definição' }
                ]
            }, {
                'href': 'campanhas', 
                'text': 'Campanhas',
                'icon': 'fa fa-flag-o',
                'items': []
            }, {
                'href': 'assets',
                'text': 'Assets',
                'icon': 'fa fa-film',
                'items': []
            }, {
                'href': 'user-register', 
                'text': 'Cadastrar Usuário',
                'icon': 'fa fa-user',
                'items': []
            }],
            get = function get () {
                return categs;
            };
            
            $log.info('MenuItemsService execution ended.');
            return {
                'get': get
            };
        }]);
        
})(window.angular.module('vod'));