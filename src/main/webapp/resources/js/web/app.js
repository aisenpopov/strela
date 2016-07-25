/**
 * Основной модуль
 */

var app = angular.module("app", []);

app.controller("MainCtrl", function ($scope) {

    $scope.isInvalid = function (field) {
        return field.$invalid && field.$dirty;
    };
    
});