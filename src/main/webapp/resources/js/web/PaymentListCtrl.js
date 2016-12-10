/**
 * Платежи
 */

var app = angular.module("app");

app.controller("PaymentListCtrl", function ($scope, $http, $location, ModalService, CommonService) {

    var size = 15;
    $scope.list = function (page, query) {
        var search = $location.search(),
            page = page || search.page || 1,
            query = query || search.query;
        
        var filter = {
            pageNumber: page,
            pageSize: size,
            query: query
        };
        
        CommonService.post("/account/payment/list", filter).then(function (resp) {
            var data = resp.data.data;
            if (data) {
                $scope.payments = data.payments;
                $scope.page = data.page;
                $location.search("page", page);
                $location.search("query", query);
            }
        });
    };
    $scope.list();

    $scope.onClickPayment = function (id) {
        $location.url("/account/payment/edit/" + (id ? id  + "/" : ""))
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

    $scope.onExport = function () {
        var search = $location.search(),
            query = search.query;

        CommonService.export({
            fileName: 'Платежи от ' + $scope.formatDateDDMMYYYY(new Date()),
            exporterName: 'payment',
            query: query
        });
    };
});