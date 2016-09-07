/**
 * Списания
 */

var app = angular.module("app");

app.controller("TransactionListCtrl", function ($scope, $http, $location, ModalService) {

    var size = 10;
    $scope.list = function (page, query) {
        var search = $location.search(),
            page = page || search.page || 1,
            query = query || search.query;

        $http.get("/account/transaction/list", {params: {page: page, size: size, query: query}}).then(function (resp) {
            var data = resp.data.data;
            if (data) {
                $scope.transactions = data.transactions;
                $scope.page = data.page;
                $location.search("page", page);
                $location.search("query", query);
            }
        });
    };
    $scope.list();

    $scope.onClick = function (id) {
        if ($scope.person.admin) {
            $location.url("/account/transaction/edit/" + (id ? id  + "/" : ""));
        }
    };

    $scope.onClickRemove = function (id, $event) {
        $event.stopPropagation();
        ModalService.openConfirmModal("Уверены что хотите удалить списание?", function () {
            $http.post("/account/transaction/remove/" + id + "/").then(function (resp) {
                var data = resp.data;
                if (!data.statusError) {
                    $scope.list();
                } else {
                    ModalService.openMessageModal("Не удалось удалить списание");
                }
            });
        });
    };
    
});

app.controller("TransactionCtrl", function ($scope, $http, ModalService,
                                        $location, CommonService, $routeParams) {

    $scope.transaction = {
        id: $routeParams.id,
        operator: null,
        operatorId: null,
        date: null,
        amount: null
    };
    
    if ($scope.transaction.id) {
        CommonService.loader(true);
        CommonService.post("/account/transaction/info", {id: $scope.transaction.id}).then(function (resp) {
            var data = resp.data.data;
            if (data.transaction) {
                var transaction = data.transaction;
                $scope.transaction.operatorId = transaction.operatorId;
                $scope.transaction.operator = transaction.operator;
                $scope.transaction.amount = transaction.amount;
                $scope.transaction.date = transaction.date;
            }
        }).finally(function () {
            CommonService.loader(false);
        });
    }
    
    $scope.onClickButton = function () {
        $scope.setFormFieldsDirty($scope.transactionForm);
    
        if ($scope.transactionForm.$invalid) {
            return;
        }
    
        var params = {
            id: $scope.transaction.id ? $scope.transaction.id : 0,
            person: $scope.transaction.operatorId,
            date: $scope.transaction.date,
            amount: $scope.transaction.amount
        };
    
        CommonService.post("/account/transaction/save", params).then(function (resp) {
            var data = resp.data;
            if (data.statusError) {
                $scope.showFormErrorMessages($scope.transactionForm, data.fieldsMessages);
            } else {
                $scope.clearFormErrorMessages($scope.transactionForm);
                $location.path("account/transaction");
            }
        });
    
    };

});