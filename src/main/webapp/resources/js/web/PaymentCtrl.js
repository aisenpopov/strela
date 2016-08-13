/**
 * Платеж
 */

var app = angular.module("app");

app.controller("PaymentCtrl", function ($scope, $http, $window, ModalService,
                                        $location, CommonService) {
    $scope.id = $location.search().id;
    $scope.athlete = {};
    $scope.gym = {};
    $scope.amount = null;

    $scope.buttonLabel = "Провести";

    $scope.athletes = [];
    $scope.gyms = [];
    $scope.search = function (type, query) {
        CommonService.search({
            type: type,
            q: query,
            instructor: true
        }).then(function (resp) {
            if (type === "athlete") {
                $scope.athletes = resp.data;
            } else {
                $scope.gyms = resp.data;
            }
        });
    };

    var oldAmount, oldGymId;
    if ($scope.id) {
        $scope.buttonLabel = "Перепровести";
        CommonService.preloader.show();
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
            CommonService.preloader.hide();
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
                    $window.location = "/account/payment/";
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