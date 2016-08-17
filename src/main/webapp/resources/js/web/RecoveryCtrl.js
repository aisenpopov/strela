/**
 * Восстановление пароля
 */

var app = angular.module("app");

app.controller("RecoveryCtrl", function ($scope, CommonService, $http, $location, $routeParams) {

    $scope.state = "";
    if ($routeParams.code && $routeParams.email) {


        CommonService.post("/recovery/check_code", {
            email: $routeParams.email,
            code: $routeParams.code
        }).then(function (resp) {
            if (resp.data.statusError) {
                $scope.state = "wrong_code";
            } else {
                $scope.state = "change_password";
            }
        });

        $scope.changePassword = function () {
            $scope.setFormFieldsDirty($scope.passwordForm);

            if ($scope.passwordForm.$invalid) {
                return;
            }

            $http.post("/recovery/save_new_password", {
                email: $routeParams.email,
                newPassword: $scope.newPassword,
                repPassword: $scope.repPassword
            }).then(function (resp) {
                console.log(resp);
                if (resp.data.statusError) {
                    $scope.showFormErrorMessages($scope.passwordForm, resp.data.fieldsMessages);
                } else {
                    $scope.state = "complete_recovery";
                }
            });
        };
    } else {
        $scope.email = "";
        $scope.state = "recovery";
        $scope.sendCode = function () {
            $scope.setFormFieldsDirty($scope.sendCodeForm);

            if ($scope.sendCodeForm.$invalid) {
                return;
            }

            $http.post("/recovery/send_code", {
                email: $scope.email
            }).then(function (resp) {
                if (resp.data.statusError) {
                    $scope.showFormErrorMessages($scope.sendCodeForm, resp.data.fieldsMessages);
                } else {
                    $scope.state = "code_send";
                }
            });
        };
    }


});