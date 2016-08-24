/**
 * Тарифы
 */

var app = angular.module("app");

app.controller("TariffListCtrl", function ($scope, $http, $location, ModalService, CommonService) {

    var size = 10;
    CommonService.loader(true);
    $scope.list = function (page, query) {
        var search = $location.search(),
            page = page || search.page || 1,
            query = query || search.query;

        $http.get("/account/tariff/list", {params: {page: page, size: size, query: query}}).then(function (resp) {
            var data = resp.data.data;
            if (data) {
                $scope.tariffs = data.tariffs;
                $scope.page = data.page;
                $location.search("page", page);
                $location.search("query", query);
            }
        }).finally(function () {
            CommonService.loader(false);
        });
    };
    $scope.list();

    $scope.onClick = function (id) {
        $location.url("/account/tariff/edit/" + (id ? id  + "/" : ""));
    };

    $scope.onClickNew = function () {
        $scope.onClick();
    };

    $scope.onClickRemove = function (id, $event) {
        $event.stopPropagation();
        ModalService.openConfirmModal("Уверены что хотите удалить тариф?", function () {
            $http.post("/account/tariff/remove/" + id + "/").then(function (resp) {
                var data = resp.data;
                if (!data.statusError) {
                    $scope.list();
                } else {
                    ModalService.openMessageModal("Не удалось удалить тариф");
                }
            });
        });
    };

});

app.controller("TariffCtrl", function ($scope, $http, ModalService,
                                       $location, CommonService, $routeParams) {

    $scope.tariff = {
        id: $routeParams.id,
        name: null,
        gym: null,
        priceMonth: null,
        priceQuarter: null,
        priceHalfYear: null,
        priceYear: null,
        expiration: null
    };

    $scope.gyms = [];
    $scope.search = function (query) {
        CommonService.search({
            type: "gym",
            q: query
        }).then(function (resp) {
            $scope.gyms = resp.data;
        });
    };

    if ($scope.tariff.id) {
        CommonService.loader(true);
        CommonService.post("/account/tariff/info", {id: $scope.tariff.id}).then(function (resp) {
            var data = resp.data.data;
            if (data.tariff) {
                var tariff = data.tariff;
                $scope.tariff.name = tariff.name;
                $scope.tariff.priceMonth = tariff.priceMonth;
                $scope.tariff.priceQuarter = tariff.priceQuarter;
                $scope.tariff.priceHalfYear = tariff.priceHalfYear;
                $scope.tariff.priceYear = tariff.priceYear;
                $scope.tariff.expiration = tariff.expiration;

                $scope.tariff.gym = {
                    id: tariff.gymId,
                    text: tariff.gymName
                };
            }
        }).finally(function () {
            CommonService.loader(false);
        });
    }

    $scope.onClickButton = function () {
        $scope.setFormFieldsDirty($scope.tariffForm);

        var invalid = false;
        if ($scope.tariffForm.$invalid) {
            invalid = true;
        }
        if (!$scope.tariff.priceMonth && !$scope.tariff.priceQuarter
                    && !$scope.tariff.priceHalfYear && !$scope.tariff.priceYear) {
            var message = "Заполните хотя бы одну цену";
            $scope.showFieldErrorMessage($scope.tariffForm.priceMonth, message);
            $scope.showFieldErrorMessage($scope.tariffForm.priceQuarter, message);
            $scope.showFieldErrorMessage($scope.tariffForm.priceHalfYear, message);
            $scope.showFieldErrorMessage($scope.tariffForm.priceYear, message);
            invalid = true;
        }
        if (invalid) {
            return;
        }

        var params = {
            id: $scope.tariff.id ? $scope.tariff.id : 0,
            name: $scope.tariff.name,
            gym: $scope.tariff.gym.id,
            priceMonth: $scope.tariff.priceMonth,
            priceQuarter: $scope.tariff.priceQuarter,
            priceHalfYear: $scope.tariff.priceHalfYear,
            priceYear: $scope.tariff.priceYear,
            expiration: $scope.tariff.expiration
        };
        
        CommonService.post("/account/tariff/save", params).then(function (resp) {
            var data = resp.data;
            if (data.statusError) {
                $scope.showFormErrorMessages($scope.tariffForm, data.fieldsMessages);
            } else {
                $scope.clearFormErrorMessages($scope.tariffForm);
                $location.path("account/tariff");
            }
        });

    };

});