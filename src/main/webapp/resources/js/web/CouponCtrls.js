/**
 * Купоны
 */

var app = angular.module("app");

app.controller("CouponListCtrl", function ($scope, $http, $location, ModalService, CommonService) {

    var size = 10;
    CommonService.loader(true);
    $scope.list = function (page, query) {
        var search = $location.search(),
            page = page || search.page || 1,
            query = query || search.query;

        $http.get("/account/coupon/list", {params: {page: page, size: size, query: query}}).then(function (resp) {
            var data = resp.data.data;
            if (data) {
                $scope.coupons = data.coupons;
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
        $location.url("/account/coupon/edit/" + (id ? id  + "/" : ""));
    };

    $scope.onClickNew = function () {
        $scope.onClick();
    };

    $scope.onClickRemove = function (id, $event) {
        $event.stopPropagation();
        ModalService.openConfirmModal("Уверены что хотите удалить купон?", function () {
            $http.post("/account/coupon/remove/" + id + "/").then(function (resp) {
                var data = resp.data;
                if (!data.statusError) {
                    $scope.list();
                } else {
                    ModalService.openMessageModal("Не удалось удалить купон");
                }
            });
        });
    };
    
});

app.controller("CouponCtrl", function ($scope, $http, ModalService,
                                        $location, CommonService, $routeParams) {

    $scope.coupon = {
        id: $routeParams.id,
        name: null,
        discountPercent: null,
        expiration: null
    };
    
    if ($scope.coupon.id) {
        CommonService.loader(true);
        CommonService.post("/account/coupon/info", {id: $scope.coupon.id}).then(function (resp) {
            var data = resp.data.data;
            if (data.coupon) {
                var coupon = data.coupon;
                $scope.coupon.name = coupon.name;
                $scope.coupon.discountPercent = coupon.discountPercent;
                $scope.coupon.expiration = coupon.expiration;
            }
        }).finally(function () {
            CommonService.loader(false);
        });
    }
    
    $scope.onClickButton = function () {
        $scope.setFormFieldsDirty($scope.couponForm);
    
        if ($scope.couponForm.$invalid) {
            return;
        }
    
        var params = {
            id: $scope.coupon.id ? $scope.coupon.id : 0,
            name: $scope.coupon.name,
            discountPercent: $scope.coupon.discountPercent,
            expiration: $scope.coupon.expiration
        };
    
        CommonService.post("/account/coupon/save", params).then(function (resp) {
            var data = resp.data;
            if (data.statusError) {
                $scope.showFormErrorMessages($scope.couponForm, data.fieldsMessages);
            } else {
                $scope.clearFormErrorMessages($scope.couponForm);
                $location.path("account/coupon");
            }
        });
    
    };

});