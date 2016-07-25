/**
 * Мой баланс
 */

var app = angular.module("app");

app.controller("BalanceCtrl", function ($scope, $http, $timeout) {
    function init() {
        $scope.amount = null;
        $timeout(function () {
            $scope.showSuccessMessage = false;
        }, 3000);
        $http.post("/account/balance/getBalance").then(function (resp) {
            var data = resp.data.data;
            if (!resp.data.statusError && data) {
                $scope.balance = data.balance;
            }
        });
        $timeout(function () {
            $scope.balanceForm.$setPristine();
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
                $scope.balanceForm.amount.$setValidity("server", false);
                $scope.balanceForm.amount.serverErrorMessage = data.errorMessage;
                $timeout(function () {
                    $scope.balanceForm.amount.$setValidity("server", true);
                    $scope.balanceForm.amount.serverErrorMessage = "";
                }, 3000);
            } else {
                $scope.balanceForm.amount.$setValidity("server", true);
                $scope.balanceForm.amount.serverErrorMessage = "";
                $scope.showSuccessMessage = true;

                init();
            }
        });
    };
});