/**
 * Мой баланс
 */

var app = angular.module("app");

app.controller("BalanceCtrl", function ($scope, $http, $timeout, ModalService, ValidateService) {
    function init() {
        $scope.amount = null;
        $http.post("/account/balance/getBalance").then(function (resp) {
            var data = resp.data.data;
            if (!resp.data.statusError && data) {
                $scope.balance = data.balance;
                $scope.hasBalance = true;
                $scope.balanceForm.$setPristine();
            } else {
                $scope.hasBalance = false;
            }
        });
    }
    init();

    $scope.onClickButton = function () {
        $scope.balanceForm.amount.$setDirty();

        if ($scope.balanceForm.$invalid) {
            return;
        }

        $http.post("/account/balance/debit", { amount: $scope.amount.toString() }).then(function (resp) {
            var data = resp.data.data;
            if (resp.data.statusError) {
                $scope.showFieldErrorMessage($scope.balanceForm.amount, data.errorMessage);
            } else {
                $scope.clearFieldErrorMessage($scope.balanceForm.amount);
                ModalService.openMessageModal("Сумма успешно списана");

                init();
            }
        });
    };
});