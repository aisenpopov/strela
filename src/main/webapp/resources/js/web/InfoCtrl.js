/**
 * Информация о пользователе.
 */

var app = angular.module("app");

app.controller("InfoCtrl", function ($scope, $timeout, ModalService, CommonService) {

    $scope.athlete = null;
    $scope.paymentStatuses = [];
    $scope.athleteTariffs = [];

    CommonService.post("/account/getCurrentAthleteInfo").then(function (resp) {
        var data = resp.data.data;

        $scope.athlete = data.athlete;
        $scope.paymentStatuses = data.paymentStatuses;
        $scope.athleteTariffs = data.athleteTariffs;
    });

});