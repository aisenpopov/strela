/**
 * Основной модуль
 */

var app = angular.module("app", ['ngAnimate', 'ngRoute', 'ui.bootstrap', 'ngSanitize', 'ui.select']);

app.config(function($routeProvider) {

    $routeProvider
        .when('/', {
            title: 'Главная',
            templateUrl: '/resources/views/main.html',
            controller: 'MainCtrl',
            showBanner: true
        })
        .when('/login', {
            title: 'Войти',
            templateUrl: '/resources/views/account/login.html',
            controller:  'LoginCtrl'
        })
        .when('/recovery', {
            title: 'Восстановление пароля',
            templateUrl: '/resources/views/account/recovery.html',
            controller:  'RecoveryCtrl'
        })
        .when('/gym', {
            title: 'Залы',
            templateUrl: '/resources/views/gyms.html',
            controller:  'GymListCtrl',
            showBanner: true
        })
        .when('/gym/:id', {
            title: 'Зал',
            templateUrl: '/resources/views/gym.html',
            controller:  'GymCtrl'
        })
        .when('/news', {
            title: 'Новости',
            templateUrl: '/resources/views/newsList.html',
            controller:  'NewsListCtrl',
            showBanner: true
        })
        .when('/news/:path', {
            title: 'Новость',
            templateUrl: '/resources/views/news.html',
            controller:  'NewsCtrl'
        })
        .when('/static/:path', {
            title: 'Страница',
            templateUrl: '/resources/views/staticPage.html',
            controller:  'StaticPageCtrl'
        })
        .when('/account', {
            title: 'Профиль',
            templateUrl: '/resources/views/account/account.html',
            controller:  'AccountCtrl',
            resolve: {
                factory: checkPerson
            }
        })
        .when('/account/balance', {
            title: 'Баланс',
            templateUrl: '/resources/views/account/balance.html',
            controller:  'BalanceCtrl',
            resolve: {
                factory: checkPerson
            }
        })
        .when('/account/payment', {
            title: 'Платежи',
            templateUrl: '/resources/views/account/payments.html',
            controller:  'PaymentListCtrl',
            resolve: {
                factory: checkPerson
            }
        })
        .when('/account/payment/edit/:id?', {
            title: 'Платеж',
            templateUrl: '/resources/views/account/editPayment.html',
            controller:  'PaymentCtrl',
            resolve: {
                factory: checkPerson
            }
        })
        .otherwise({ redirectTo: '/' });

});

function checkPerson($q, $rootScope, $location, $http) {
    console.log("check person: start");

    function notLogged() {
        console.log("check person: not logged");
        deferred.reject();
        $location.path("login");
    }

    if ($rootScope.person) {
        console.log("check person: logged");

        return true;
    } else {
        var deferred = $q.defer();

        console.log("check person: get current person");
        $http.post("/getCurrentPerson").then(function (resp) {
            var data = resp.data.data;
            $rootScope.person = data.person;

            if ($rootScope.person) {
                deferred.resolve(true);
            } else {
                notLogged();
            }
        }, function () {
            notLogged();
        });

        return deferred.promise;
    }
}

app.run(function($rootScope) {
    $rootScope.$on('$routeChangeSuccess', function (event, current) {
        var currentRoute = current.$$route;
        if (currentRoute) {
            $rootScope.htmlTitle = currentRoute.title;
            $rootScope.showBanner = currentRoute.showBanner;
        }
    });
});

app.directive('ngDatepicker', function() {
    return {
        link: function(scope, element) {
            $(element).datepicker({
                dateFormat: 'dd.mm.yy',
                nextText: '',
                prevText: '',
                beforeShow: function () {
                    $(".ui-datepicker").addClass("open");
                },
                onClose: function () {
                    $(".ui-datepicker").removeClass("open");
                }
            });
        }
    };
});

app.controller("CommonCtrl", function ($scope, $timeout, $location, $http, CommonService) {

    $scope.location = $location;

    $scope.logout = function () {
        $http.get("/account/logout").then(function (resp) {
            CommonService.updatePerson(true);
            $location.path("");
            $location.search("");
        });
    };
    
    $scope.hideMenu = function () {
        $(".rd-navbar, .rd-navbar-toggle").removeClass("active");
        $(".rd-navbar-submenu").removeClass("focus");
    };

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

    $scope.showFormErrorMessages = function (form, fieldsMessages, duration) {
        for (var p in fieldsMessages) {
            $scope.showFieldErrorMessage(form[p], fieldsMessages[p], duration);
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

    $scope.setFormFieldsDirty = function (form) {
        for (var p in form) {
            var value = form[p];
            if (typeof value === 'object' && value.hasOwnProperty('$modelValue')) {
                value.$setDirty();
            }
        }
    };

    $scope.setFormFieldsPristine = function (form) {
        for (var p in form) {
            var value = form[p];
            if (typeof value === 'object' && value.hasOwnProperty('$modelValue')) {
                value.$setPristine();
            }
        }
    };

    $scope.currentYear = (new Date).getFullYear();

});

app.controller("HeaderCtrl", function ($scope, CommonService, $q, $timeout, $rootScope) {

    $rootScope.citiesHasGym = [];
    $rootScope.selectedCity = null;
    $rootScope.setSelectedCity = function (city) {
        $rootScope.selectedCity = city;
    };
    var citiesPromise = CommonService.post("/city/hasGym").then(function (resp) {
        var data = resp.data.data;
        if (data.citiesHasGym) {
            $rootScope.citiesHasGym = data.citiesHasGym;
        }
    });

    var personPromise = CommonService.updatePerson();

    $q.all([citiesPromise, personPromise]).then(function() {
        initNavbar();
    });

    var firstShow = true;
    $scope.bannerList = [];
    $scope.$watch("showBanner", function (newValue) {
        if (newValue && firstShow) {
            firstShow = false;
            CommonService.post("/banner").then(function (resp) {
                var data = resp.data.data;
                $scope.bannerList = data.bannerList;

                $timeout(function () {
                    initOwlCarousel();
                });
            });
        }
    });
    
});

app.controller("MainCtrl", function ($scope, CommonService) {

    $scope.newsList = [];
    CommonService.post("/news/", {
        page: 1,
        size: 3
    }).then(function (resp) {
        var data = resp.data.data;
        if (data.newsList) {
            $scope.newsList = data.newsList;
        }
    });
    
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