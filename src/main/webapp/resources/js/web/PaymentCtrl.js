/**
 * Платеж
 */

var app = angular.module("app");

app.controller("PaymentCtrl", function ($scope, $http, ModalService,
                                        $location, CommonService, $routeParams) {
    
    $scope.id = $routeParams.id;
    $scope.athlete = {};
    $scope.gym = {};
    $scope.amount = null;

    $scope.buttonLabel = "Провести";

    var oldAmount, oldGymId;
    if ($scope.id) {
        $scope.buttonLabel = "Перепровести";
        CommonService.loader(true);
        CommonService.post("/account/payment/info", {id: $scope.id}).then(function (resp) {
            var data = resp.data.data;
            if (data) {
                $scope.amount = data.amount;
                oldAmount = data.amount;
                $scope.athlete = {
                    selected: {
                        id: data.athleteId,
                        text: data.athleteDisplayName
                    }
                };
                $scope.gym = {
                    selected: {
                        id: data.gymId,
                        text: data.gymName
                    }
                };
                oldGymId = data.gymId;
            }
        }).finally(function () {
            CommonService.loader(false);
        });
    } else {
        CommonService.loader(true);
        CommonService.post("/account/payment/checkSingleGym").then(function (resp) {
            var data = resp.data.data;
            if (data && data.gym) {
                $scope.gym = {
                    selected: {
                        id: data.gym.id,
                        text: data.gym.name
                    }
                };
            }
        }).finally(function () {
            CommonService.loader(false);
        });
    }
    
    $scope.onClickButton = function () {
        $scope.paymentForm.athlete.$setDirty();
        $scope.paymentForm.gym.$setDirty();
        $scope.paymentForm.amount.$setDirty();

        if ($scope.paymentForm.$invalid) {
            return;
        }

        function save() {
            var params = {
                id: $scope.id ? $scope.id : 0,
                amount: $scope.amount,
                'athleteTariff.athlete': $scope.athlete.selected.id,
                'athleteTariff.tariff.gym': $scope.gym.selected.id
            };

            CommonService.post("/account/payment/save", params).then(function (resp) {
                var data = resp.data;
                if (data.statusError) {
                    $scope.showFormErrorMessages($scope.paymentForm, data.fieldsMessages);
                } else {
                    $scope.clearFormErrorMessages($scope.paymentForm);
                    $location.path("account/payment");
                }
            });
        }

        if ($scope.id && $scope.id > 0
            && ($scope.gym.selected.id != oldGymId || $scope.amount != oldAmount)) {
            ModalService.openConfirmModal("Вы действительно хотите изменить платеж? Дата истечения атлета будет пересчитана.", function () {
               save();
            });
        } else {
            save();
        }
    };

});