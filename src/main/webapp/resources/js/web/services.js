/**
 * Сервисы
 */

var app = angular.module("app");

app.factory('CommonService', function ($http) {
    var service = {};

    service.preloader = $('body').loadingIndicator({
        showOnInit: false,
        useImage: false
    }).data("loadingIndicator");

    service.post = function (url, params) {
        return $http.post(url, $.param(params),
            { headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' }});
    };

    service.search = function (params) {
        return service.post("/autocomplete/" + params.type + "/find", params);
    };

    return service;
});

app.factory('ModalService', function ($uibModal) {
    var service = {};

    service.openConfirmModal = function (message, onConfirm) {

        var modalInstance = $uibModal.open({
            animation: true,
            templateUrl: 'confirmModal',
            controller: 'ConfirmModalInstanceCtrl',
            resolve: {
                message: function () {
                    return message;
                }
            }
        });

        modalInstance.result.then(function () {
            onConfirm();
        });
    };

    service.openMessageModal = function (message) {

        var modalInstance = $uibModal.open({
            animation: true,
            templateUrl: 'messageModal',
            controller: 'MessageModalInstanceCtrl',
            resolve: {
                message: function () {
                    return message;
                }
            }
        });

    };

    return service;
});