/**
 * Информация о пользователе.
 */

var app = angular.module("app");

app.controller("InfoCtrl", function ($scope, $timeout, $location, ModalService, CommonService) {

    $scope.athlete = null;
    $scope.paymentStatuses = [];
    $scope.athleteTariffs = [];

    CommonService.post("/account/getCurrentAthleteInfo").then(function (resp) {
        var data = resp.data.data;

        $scope.athlete = data.athlete;
        $scope.paymentStatuses = data.paymentStatuses;
        $scope.athleteTariffs = data.athleteTariffs;
    }).then(function () {
        $scope.list();
    });

    var size = 5;
    $scope.list = function (page, query) {
        if ($scope.athlete) {
            var filter = {
                pageNumber: page || 1,
                pageSize: size,
                'athlete.id': $scope.athlete.id
            };

            CommonService.post("/account/payment/list", filter).then(function (resp) {
                var data = resp.data.data;
                if (data) {
                    $scope.payments = data.payments;
                    $scope.page = data.page;
                }
            });
        }
    };

});