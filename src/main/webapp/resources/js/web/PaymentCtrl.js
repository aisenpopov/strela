/**
 * Платеж
 */

var app = angular.module("app");

app.controller("PaymentCtrl", function ($scope, $http, ModalService, $timeout,
                                        $location, CommonService, $routeParams) {
    
    $scope.payment = {};

    $scope.buttonLabel = "Провести";

    var oldAmount, oldGymId;
    if ($routeParams.id) {
        $scope.buttonLabel = "Перепровести";
        CommonService.post("/account/payment/getPayment", {id: $routeParams.id}).then(function (resp) {
            var data = resp.data.data;
            if (data && data.payment) {
                $scope.payment = data.payment;
                $scope.payment.athleteTariff.athlete.text = $scope.payment.athleteTariff.athlete.displayName;
                $scope.payment.athleteTariff.tariff.gym.text = $scope.payment.athleteTariff.tariff.gym.name;
                
                oldAmount = data.payment.amount;
                oldGymId = data.payment.athleteTariff.tariff.gym.id;
            }
        });
    } else {
        CommonService.post("/account/payment/checkSingleGym").then(function (resp) {
            var data = resp.data.data;
            if (data && data.gym) {
                $scope.payment.athleteTariff = {
                    tariff: {
                        gym: data.gym
                    }
                };
                $scope.payment.athleteTariff.tariff.gym.text = data.gym.name;
            }
        });
    }

    function getNewPaymentAmount(athleteId, gymId) {
        CommonService.post("/account/payment/getNewPaymentAmount", {
            athleteId: athleteId,
            gymId: gymId
        }).then(function (resp) {
            var data = resp.data.data;
            if (data && data.amount) {
                $scope.payment.amount = data.amount;
            }
        });
    }

    if (!$routeParams.id) {
        $scope.$watchGroup(["payment.athleteTariff.athlete", "payment.athleteTariff.tariff.gym"], function (newValues) {
            var athlete = newValues[0],
                gym = newValues[1];
            if (athlete && athlete.id && gym && gym.id) {
                getNewPaymentAmount(athlete.id, gym.id);
            }
        });
    }

    $scope.onClickButton = function () {
        $scope.setFormFieldsDirty($scope.paymentForm);

        if ($scope.paymentForm.$invalid) {
            return;
        }

        function save() {
            var params = {
                id: $scope.payment.id ? $scope.payment.id : 0,
                amount: $scope.payment.amount,
                'athleteTariff.athlete': $scope.payment.athleteTariff.athlete.id,
                'athleteTariff.tariff.gym': $scope.payment.athleteTariff.tariff.gym.id
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

        if ($scope.payment.id
            && ($scope.payment.athleteTariff.tariff.gym.id != oldGymId || $scope.payment.amount != oldAmount)) {
            ModalService.openConfirmModal("Вы действительно хотите изменить платеж? Дата истечения атлета будет пересчитана.", function () {
               save();
            });
        } else {
            save();
        }
    };

});