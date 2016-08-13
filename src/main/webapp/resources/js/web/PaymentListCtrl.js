/**
 * Платежи
 */

var app = angular.module("app");

app.controller("PaymentListCtrl", function ($scope, $http, $window, $location, ModalService, CommonService) {
    var size = 8;
    CommonService.preloader.show();
    $scope.list = function (page, query) {
        var search = $location.search(),
            page = page || search.page || 1,
            query = query || search.query;
        
        $http.get("/account/payment/list/", {params: {page: page, size: size, query: query}}).then(function (resp) {
            var data = resp.data.data;
            if (data) {
                $scope.payments = data.payments;
                $scope.page = data.page;
                $location.search("page", page);
                $location.search("query", query);
            }
        }).finally(function () {
            CommonService.preloader.hide();
        });
    };
    $scope.list();

    $scope.onClickPayment = function (id) {
        $window.location = "/account/payment/edit/" + (id ? "#?id=" + id : "");
    };
    
    $scope.onClickNew = function () {
        $scope.onClickPayment();
    };

    $scope.onClickRemove = function (id, $event) {
        $event.stopPropagation();
        ModalService.openConfirmModal("Уверены что хотите удалить платеж?", function () {
            $http.post("/account/payment/remove/" + id + "/").then(function (resp) {
                var data = resp.data;
                if (!data.statusError) {
                    $scope.list();
                } else {
                    ModalService.openMessageModal("Не удалось удалить платеж");
                }
            });
        });
    };
});