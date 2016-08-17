/**
 * Авторизация
 */

var app = angular.module("app");

app.controller("LoginCtrl", function ($scope, CommonService, $location) {

    $scope.onClickLogin = function() {
        $scope.setFormFieldsDirty($scope.loginForm);

        if ($scope.loginForm.$invalid) {
            return;
        }

        var headers = {
            'X-Ajax': 'true',
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        };
        CommonService.post("/account/sign_in", {
            remember: true,
            username: $scope.username,
            password: $scope.password
        }, headers).then(function (resp) {
            if (resp.data === "success") {
                CommonService.updatePerson(true);
                $location.path("account");
            } else {
                $scope.showFieldErrorMessage($scope.loginForm.username, "Неправильный login или пароль");
            }
        });
    };

});