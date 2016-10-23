/**
 * Новости
 */

var app = angular.module("app");

app.controller("NewsListCtrl", function ($scope, CommonService, $location) {

    $scope.newsList = [];
    $scope.list = function (page) {
        var search = $location.search(),
            page = page || search.page || 1;
        
        CommonService.post("/news/", {
            page: page,
            size: 6
        }).then(function (resp) {
            var data = resp.data.data;
            if (data.newsList) {
                $scope.newsList = data.newsList;
                $scope.page = data.page;
                $location.search("page", page);
            }
        });
    };
    $scope.list();

});

app.controller("NewsCtrl", function ($scope, CommonService, $routeParams, $rootScope, $sce) {

    $scope.news = {};
    CommonService.post("/news/" + $routeParams.path).then(function (resp) {
        var data = resp.data.data;
        if (data) {
            data.text = $sce.trustAsHtml(data.text);
            $scope.news = data;
            $rootScope.htmlTitle = data.name;
        }
    });

});

app.controller("StaticPageCtrl", function ($scope, CommonService, $routeParams, $rootScope, $sce) {

    $scope.staticPage = {};
    CommonService.post("/static/" + $routeParams.path).then(function (resp) {
        var data = resp.data.data;
        if (data) {
            data.text = $sce.trustAsHtml(data.text);
            $scope.staticPage = data;
            $rootScope.htmlTitle = data.name;
        }
    });

});