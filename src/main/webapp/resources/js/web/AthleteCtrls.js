/**
 * Тарифы
 */

var app = angular.module("app");

app.controller("AthleteListCtrl", function ($scope, $http, $location, ModalService, CommonService) {

    var size = 15;
    CommonService.loader(true);
    $scope.list = function (page, query) {
        var search = $location.search(),
            page = page || search.page || 1,
            query = query || search.query;

        $http.get("/account/athlete/list", {params: {page: page, size: size, query: query}}).then(function (resp) {
            var data = resp.data.data;
            if (data) {
                $scope.athletes = data.athletes;
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
        $location.url("/account/athlete/edit/" + (id ? id  + "/" : ""));
    };

    $scope.onClickNew = function () {
        $scope.onClick();
    };

    $scope.onClickRemove = function (id, $event) {
        $event.stopPropagation();
        ModalService.openConfirmModal("Уверены что хотите удалить атлета?", function () {
            $http.post("/account/athlete/remove/" + id + "/").then(function (resp) {
                var data = resp.data;
                if (!data.statusError) {
                    $scope.list();
                } else {
                    ModalService.openMessageModal(data.errorMessage);
                }
            });
        });
    };

});

app.controller("AthleteCtrl", function ($scope, $http, ModalService,
                                       $location, CommonService, $routeParams, $timeout) {

    $scope.athlete = {
        sex: "male",
        firstName: "",
        lastName: "",
        middleName: ""
    };
    $scope.athleteTariffs = [];
    if ($routeParams.id) {
        CommonService.loader(true);
        getAthleteTariffs();
        CommonService.post("/account/athlete/getAthlete", {id: $routeParams.id}).then(function (resp) {
            var data = resp.data.data;

            $scope.athlete = data.athlete;
            $scope.athleteTariffs = data.athleteTariffs;

            if ($scope.athlete.team) {
                $scope.athlete.team.text = $scope.athlete.team.name;
            }
            if ($scope.athlete.registrationRegion) {
                $scope.athlete.registrationRegion.text = $scope.athlete.registrationRegion.name;
            }
        }).finally(function () {
            CommonService.loader(false);
        });
    }

    function getAthleteTariffs() {
        CommonService.post("/account/athlete/getAthleteTariffs", {id: $routeParams.id}).then(function (resp) {
            var data = resp.data.data;

            $scope.athleteTariffs = data.athleteTariffs;
        });
    }

    $scope.addAthleteTariff = function () {
        ModalService.openAthleteTariffModal($routeParams.id, null, function () {
            getAthleteTariffs();
        });
    };

    $scope.editAthleteTariff = function (id, $event) {
        $event.preventDefault();
        ModalService.openAthleteTariffModal($routeParams.id, id, function () {
            getAthleteTariffs();
        });
    };

    $scope.removeAthleteTariff = function (id, $event) {
        $event.preventDefault();
        CommonService.post("/account/athlete/checkRemoveAthleteTariff", {
            athleteTariffId: id
        }).then(function (resp) {
            var data = resp.data;
            if (data.statusError) {
                ModalService.openMessageModal(data.errorMessage);
            } else {
                ModalService.openConfirmModal("Уверены что хотите удалить тариф?", function () {
                    CommonService.post("/account/athlete/removeAthleteTariff", {
                        athleteTariffId: id
                    }).then(function (resp) {
                        var data = resp.data;
                        if (data.statusError) {
                            ModalService.openMessageModal(data.errorMessage);
                        } else {
                            getAthleteTariffs();
                        }
                    });
                });
            }
        });
    };

    $scope.saveAthlete = function () {
        $scope.setFormFieldsDirty($scope.athleteForm);

        if ($scope.athleteForm.$invalid) {
            CommonService.scrollToError(1000);
            return;
        }

        var params = $.extend({}, $scope.athlete);
        params.person = $scope.athlete.person.id;
        params['person.login'] = $scope.athlete.person.login;
        params['person.password'] = $scope.athlete.person.password;
        params['person.admin'] = !!$scope.athlete.person.admin;
        params['person.disabled'] = !!$scope.athlete.person.disabled;
        params.team = $scope.athlete.team ? $scope.athlete.team.id : null;
        params.registrationRegion = $scope.athlete.registrationRegion ? $scope.athlete.registrationRegion.id : null;

        CommonService.post("/account/athlete/saveAthlete", params).then(function (resp) {
            var data = resp.data;
            if (data.statusError) {
                $scope.showFormErrorMessages($scope.athleteForm, data.fieldsMessages, 5000);
                $timeout(function () {
                    CommonService.scrollToError(1000);
                }, 300);
            } else {
                $scope.clearFormErrorMessages($scope.athleteForm);
                if (!params.id) {
                    $location.url("/account/athlete/edit/" + data.data.id + "/");
                    ModalService.openMessageModal("Атлет успешно создан");
                } else {
                    ModalService.openMessageModal("Изменения атлета сохранены");
                }
            }
        });
    };

});

app.controller('AthleteTariffModalInstanceCtrl', function ($scope, $uibModalInstance, CommonService, athleteId, athleteTariffId) {

    $scope.athleteTariff = {
        athlete: {
            id: athleteId
        }
    };
    if (athleteTariffId) {
        CommonService.post("/account/athlete/getAthleteTariff", {id: athleteTariffId}).then(function (resp) {
            var data = resp.data.data;

            $scope.athleteTariff = data.athleteTariff;
            if ($scope.athleteTariff.tariff) {
                $scope.athleteTariff.tariff.text = $scope.athleteTariff.tariff.name;
            }
            if ($scope.athleteTariff.coupon) {
                $scope.athleteTariff.coupon.text = $scope.athleteTariff.coupon.name;
            }
        });
    }

    $scope.save = function () {
        $scope.setFormFieldsDirty($scope.athleteTariffForm);

        if ($scope.athleteTariffForm.$invalid) {
            return;
        }

        var params = {};
        if ($scope.athleteTariff.id) {
            params.id = $scope.athleteTariff.id;
        }
        params.athlete = $scope.athleteTariff.athlete.id;
        params.tariff = $scope.athleteTariff.tariff ? $scope.athleteTariff.tariff.id : null;
        params.coupon = $scope.athleteTariff.coupon ? $scope.athleteTariff.coupon.id : null;

        CommonService.post("/account/athlete/saveAthleteTariff", params).then(function (resp) {
            var data = resp.data;
            if (data.statusError) {
                $scope.showFormErrorMessages($scope.athleteTariffForm, data.fieldsMessages, 5000);
            } else {
                $scope.clearFormErrorMessages($scope.athleteTariffForm);
                $uibModalInstance.close();
            }
        });

    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});