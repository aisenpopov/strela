/**
 * Залы
 */

var app = angular.module("app");

app.controller("GymListCtrl", function ($scope, CommonService, $routeParams, $rootScope) {

    var cityId = $routeParams.cityId;
    $scope.gyms = [];
    function getGyms(cityId) {
        var params = {};
        if (cityId) {
            params.cityId = cityId;
        }
        CommonService.post("/gym/list", params).then(function (resp) {
            var data = resp.data.data;
            if (data.gyms) {
                $scope.gyms = data.gyms;
            }
        }).finally(function () {
            if (cityId) {
                angular.forEach($rootScope.citiesHasGym, function (value) {
                    if (value.id == cityId) {
                        $rootScope.selectedCity = value;
                    }
                });
            }
        });
    }
    getGyms(cityId);

});

app.controller("GymCtrl", function ($scope, CommonService, $routeParams, $rootScope) {

    var id = $routeParams.id;
    $scope.gym = {};
    CommonService.post("/gym/", { id: id }).then(function (resp) {
        var data = resp.data.data;
        if (data) {
            $scope.gym = data;
            $rootScope.htmlTitle = data.name;
            initYandexMap();
        }
    });

    function initYandexMap() {
        ymaps.ready(function() {
            var $map = $("#map"),
                longitude = $map.data("longitude"),
                latitude = $map.data("latitude"),
                coordinates,
                moscowCoordinates = [55.753559, 37.609218];

            if (longitude && latitude) {
                coordinates = [latitude, longitude];
            }
            if (!coordinates) {
                coordinates = moscowCoordinates;
            }
            var map = new ymaps.Map("map", {
                    center: coordinates,
                    zoom: 17,
                    controls: ['typeSelector', 'fullscreenControl', 'zoomControl']
                }),
                placemark = new ymaps.Placemark(
                    coordinates,
                    {},
                    {
                        draggable: false,
                        preset: 'islands#whiteStretchyIcon'
                    }
                );
            map.geoObjects.add(placemark);
        });
    }

});