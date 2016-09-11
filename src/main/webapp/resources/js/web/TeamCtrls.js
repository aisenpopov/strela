/**
 * Тарифы
 */

var app = angular.module("app");

app.controller("TeamListCtrl", function ($scope, $http, $location, ModalService) {

    var size = 10;
    $scope.list = function (page, query) {
        var search = $location.search(),
            page = page || search.page || 1,
            query = query || search.query;

        $http.get("/account/team/list", {params: {page: page, size: size, query: query}}).then(function (resp) {
            var data = resp.data.data;
            if (data) {
                $scope.teams = data.teams;
                $scope.page = data.page;
                $location.search("page", page);
                $location.search("query", query);
            }
        });
    };
    $scope.list();

    $scope.onClick = function (id) {
        $location.url("/account/team/edit/" + (id ? id  + "/" : ""));
    };

    $scope.onClickNew = function () {
        $scope.onClick();
    };

    $scope.onClickRemove = function (id, $event) {
        $event.stopPropagation();
        ModalService.openConfirmModal("Уверены что хотите удалить команду?", function () {
            $http.post("/account/team/remove/" + id + "/").then(function (resp) {
                var data = resp.data;
                if (!data.statusError) {
                    $scope.list();
                } else {
                    ModalService.openMessageModal("Не удалось удалить команду");
                }
            });
        });
    };

});

app.controller("TeamCtrl", function ($scope, $http, ModalService,
                                       $location, CommonService, $routeParams) {

    $scope.team = {
        id: $routeParams.id
    };

    if ($scope.team.id) {
        CommonService.loader(true);
        CommonService.post("/account/team/getTeam", {id: $scope.team.id}).then(function (resp) {
            var data = resp.data.data;
            if (data.team) {
                var team = data.team;
                $scope.team = team;

                $scope.team.city.text = team.city.name;
                $scope.team.chiefInstructor.text = team.chiefInstructor.displayName;
            }
        }).finally(function () {
            CommonService.loader(false);
        });
    }

    $scope.onClickButton = function () {
        $scope.setFormFieldsDirty($scope.teamForm);

        if ($scope.teamForm.$invalid) {
            return;
        }

        var params = {
            id: $scope.team.id ? $scope.team.id : 0,
            name: $scope.team.name,
            "city.id": $scope.team.city.id,
            "chiefInstructor.id": $scope.team.chiefInstructor.id
        };
        
        CommonService.post("/account/team/save", params).then(function (resp) {
            var data = resp.data;
            if (data.statusError) {
                $scope.showFormErrorMessages($scope.teamForm, data.fieldsMessages);
            } else {
                $scope.clearFormErrorMessages($scope.teamForm);
                $location.path("account/team");
            }
        });

    };

});