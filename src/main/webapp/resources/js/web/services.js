/**
 * Сервисы
 */

var app = angular.module("app");

app.factory('CommonService', function ($http, $location, $q, $rootScope) {
    var service = {};

    var preloader = $('body').loadingIndicator({
        showOnInit: false,
        useImage: false
    }).data("loadingIndicator");
    
    service.loader = function (show) {
        if (show) {
            preloader.show();
        } else {
            preloader.hide();
        }
    };

    service.post = function (url, params, headers) {
        var headers = headers || {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            },
            promise = $http.post(url, params ? $.param(params) : {}, { headers: headers });
        
        return promise.then(function (resp) {
            if (resp.data.redirect) {
                $location.path(resp.data.redirect);
                return $q.reject(resp);
            }
            return resp;
        });
    };

    service.search = function (params) {
        return service.post("/autocomplete/" + params.type + "/find", params);
    };

    service.scrollTo = function (position, duration, done) {
        var params = {
            duration: duration || 500,
            done: done
        };
        $("html, body").animate({
            scrollTop: position
        }, params);
    };

    service.scrollToError = function (duration, done) {
        var errors = $(".form-error:visible");
        if (errors.length) {
            service.scrollTo(errors.first().offset().top - 100, duration, done);
        }
    };

    service.updatePerson = function (initMenu) {
        return service.post("/getCurrentPerson").then(function (resp) {
            var data = resp.data.data;
            $rootScope.person = data.person;
            if (initMenu) {
                initNavbar();
            }
        });
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