/**
 * Основной модуль
 */

var app = angular.module("app", ['ngAnimate', 'ui.bootstrap', 'ngSanitize', 'ui.select']);

app.controller("MainCtrl", function ($scope, $timeout) {

    $scope.isInvalid = function (field) {
        return field.$invalid && field.$dirty;
    };

    $scope.showFieldErrorMessage = function (field, errorMessage, duration) {
        var duration = duration || 2000;
        field.$setValidity("server", false);
        field.serverErrorMessage = errorMessage;
        $timeout(function () {
            $scope.clearFieldErrorMessage(field);
        }, duration);
    };

    $scope.clearFieldErrorMessage = function (field) {
        field.$setValidity("server", true);
        field.serverErrorMessage = "";
    };

    $scope.showFormErrorMessages = function (form, fieldsMessages) {
        for (var p in fieldsMessages) {
            $scope.showFieldErrorMessage(form[p], fieldsMessages[p]);
        }
    };

    $scope.clearFormErrorMessages = function (form) {
        for (var p in form) {
            var value = form[p];
            if (typeof value === 'object' && value.hasOwnProperty('$modelValue')) {
                $scope.clearFieldErrorMessage(value);
            }
        }
    };
    
});

app.controller("PagingCtrl", function ($scope) {

    $scope.$watch("page", function (newValue) {
        if (newValue) {
            calculatePages(newValue);
        }
    });

    function calculatePages(page) {
        var pc = 3;

        var number = page.number + 1,
            start = Math.max(1, number - Math.floor(pc / 2)),
            over = start + pc - page.totalPages - 1;

        if (over > 0) {
            start -= over;
        }
        if (start < 1) {
            start = 1;
        }

        var pages = [],
            max = page.totalPages + 1,
            c = 0;
        while(start < max && c < pc) {
            pages.push(start++);
            c++;
        }

        $scope.pages = pages;
    }

});

app.controller("ListSearchCtrl", function ($scope, $location, $timeout) {

    $scope.query = $location.search().query;

    var timer;
    $scope.$watch("query", function (newValue, oldValue) {
        if (newValue !== oldValue) {
            $timeout.cancel(timer);
            timer = $timeout(search, 500);
        }
    });

    function search() {
        if (!$scope.query) {
            $location.search("query", null);
        }
        $scope.list(1, $scope.query);
    }

});

app.controller('ConfirmModalInstanceCtrl', function ($scope, $uibModalInstance, message) {

    $scope.message = message;

    $scope.ok = function () {
        $uibModalInstance.close();
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});

app.controller('MessageModalInstanceCtrl', function ($scope, $uibModalInstance, message) {

    $scope.message = message;

    $scope.ok = function () {
        $uibModalInstance.close();
    };
});