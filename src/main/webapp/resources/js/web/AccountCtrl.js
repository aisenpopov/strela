/**
 * Мой профиль
 */

var app = angular.module("app");

app.controller("AccountCtrl", function ($scope, $timeout, ModalService, CommonService) {

    $scope.athlete = null;
    $scope.hasAthlete = false;
    $scope.athleteTariffs = [];
    $scope.changePassword = null;
    CommonService.loader(true);
    CommonService.post("/account/getCurrentAthlete").then(function (resp) {
        var data = resp.data.data;
        if (data.athlete) {
            $scope.athlete = data.athlete;
            $scope.hasAthlete = true;
        }
        if (data.athleteTariffs) {
            $scope.athleteTariffs = data.athleteTariffs;
        }
        if (data.changePassword) {
            $scope.changePassword = data.changePassword;
        }
        CommonService.loader(false);
    });

    $scope.saveAthlete = function () {
        $scope.setFormFieldsDirty($scope.athleteForm);

        if ($scope.athleteForm.$invalid) {
            CommonService.scrollToError(1000);
            return;
        }

        var params = $.extend({}, $scope.athlete);
        params.person = null;
        params['person.id'] = $scope.athlete.person.id;
        params['person.login'] = $scope.athlete.person.login;
        CommonService.post("/account/saveAthlete", params).then(function (resp) {
            var data = resp.data;
            if (data.statusError) {
                $scope.showFormErrorMessages($scope.athleteForm, data.fieldsMessages, 5000);
                $timeout(function () {
                    CommonService.scrollToError(1000);
                }, 300);
            } else {
                $scope.clearFormErrorMessages($scope.athleteForm);
                ModalService.openMessageModal("Изменения профиля сохранены");
            }
        });
    };

    $scope.savePassword = function () {
        $scope.setFormFieldsDirty($scope.passwordForm);

        if ($scope.passwordForm.$invalid) {
            CommonService.scrollToError(1000);
            return;
        }

        CommonService.post("/account/savePassword", $scope.changePassword).then(function (resp) {
            var data = resp.data;
            if (data.statusError) {
                $scope.showFormErrorMessages($scope.passwordForm, data.fieldsMessages, 5000);
                $timeout(function () {
                    CommonService.scrollToError(1000);
                }, 300);
            } else {
                $scope.changePassword.oldPassword = "";
                $scope.changePassword.newPassword = "";
                $scope.changePassword.repPassword = "";
                $scope.setFormFieldsPristine($scope.passwordForm);
                $scope.clearFormErrorMessages($scope.passwordForm);

                ModalService.openMessageModal("Пароль изменен");
            }
        });
    };

});