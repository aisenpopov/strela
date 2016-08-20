/**
 * Статусы платежей
 */

var app = angular.module("app");

app.controller("PaymentStatusListCtrl", function ($scope, $http, $location, ModalService, CommonService) {

    var size = 8;
    CommonService.loader(true);
    $scope.list = function (page, query) {
        var search = $location.search(),
            page = page || search.page || 1,
            query = query || search.query;

        $http.get("/account/payment_status/list", {params: {page: page, size: size, query: query}}).then(function (resp) {
            var data = resp.data.data;
            if (data) {
                $scope.paymentStatuses = data.paymentStatuses;
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
        $location.url("/account/payment_status/edit/" + (id ? id  + "/" : ""))
    };
    
    $scope.onClickNew = function () {
        $scope.onClick();
    };

});

app.controller("PaymentStatusCtrl", function ($scope, $http, ModalService,
                                        $location, CommonService, $routeParams) {

    $scope.paymentStatus = {
        id: $routeParams.id,
        athlete: null,
        gym: null,
        payedTill: null
    };

    $scope.athletes = [];
    $scope.gyms = [];
    $scope.search = function (type, query) {
        CommonService.search({
            type: type,
            q: query
        }).then(function (resp) {
            if (type === "athlete") {
                $scope.athletes = resp.data;
            } else {
                $scope.gyms = resp.data;
            }
        });
    };

    if ($scope.paymentStatus.id) {
        CommonService.loader(true);
        CommonService.post("/account/payment_status/info", {id: $scope.paymentStatus.id}).then(function (resp) {
            var data = resp.data.data;
            if (data.paymentStatus) {
                var paymentStatus = data.paymentStatus;
                $scope.paymentStatus.athlete = {
                    id: paymentStatus.athleteId,
                    text: paymentStatus.athleteDisplayName
                };
                $scope.paymentStatus.gym = {
                    id: paymentStatus.gymId,
                    text: paymentStatus.gymName
                };
                $scope.paymentStatus.payedTill = paymentStatus.payedTill;
            }
        }).finally(function () {
            CommonService.loader(false);
        });
    }

    $scope.onClickButton = function () {
        $scope.setFormFieldsDirty($scope.paymentStatusForm);
    
        if ($scope.paymentStatusForm.$invalid) {
            return;
        }
    
        var params = {
            id: $scope.paymentStatus.id ? $scope.paymentStatus.id : 0,
            payedTill: $scope.paymentStatus.payedTill,
            athlete: $scope.paymentStatus.athlete.id,
            gym: $scope.paymentStatus.gym.id
        };

        CommonService.post("/account/payment_status/save", params).then(function (resp) {
            var data = resp.data;
            if (data.statusError) {
                $scope.showFormErrorMessages($scope.paymentStatusForm, data.fieldsMessages);
            } else {
                $scope.clearFormErrorMessages($scope.paymentStatusForm);
                $location.path("account/payment_status");
            }
        });

    };

});