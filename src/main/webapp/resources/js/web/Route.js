/**
 * Маршрутизация
 */

var app = angular.module("app");

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
            reloadOnSearch: false,
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
        .when('/account/payment_status', {
            title: 'Даты истечения',
            templateUrl: '/resources/views/account/paymentStatuses.html',
            controller:  'PaymentStatusListCtrl',
            reloadOnSearch: false,
            resolve: {
                factory: checkPerson
            }
        })
        .when('/account/payment_status/edit/:id?', {
            title: 'Дата истечения',
            templateUrl: '/resources/views/account/editPaymentStatus.html',
            controller:  'PaymentStatusCtrl',
            resolve: {
                factory: checkPerson
            }
        })
        .when('/account/transaction', {
            title: 'Списания',
            templateUrl: '/resources/views/account/transactions.html',
            controller:  'TransactionListCtrl',
            reloadOnSearch: false,
            resolve: {
                factory: checkPerson
            }
        })
        .when('/account/transaction/edit/:id?', {
            title: 'Списание',
            templateUrl: '/resources/views/account/editTransaction.html',
            controller:  'TransactionCtrl',
            resolve: {
                factory: checkPerson
            }
        })
        .when('/account/coupon', {
            title: 'Купоны',
            templateUrl: '/resources/views/account/coupons.html',
            controller:  'CouponListCtrl',
            reloadOnSearch: false,
            resolve: {
                factory: checkPerson
            }
        })
        .when('/account/coupon/edit/:id?', {
            title: 'Купон',
            templateUrl: '/resources/views/account/editCoupon.html',
            controller:  'CouponCtrl',
            resolve: {
                factory: checkPerson
            }
        })
        .when('/account/tariff', {
            title: 'Тарифы',
            templateUrl: '/resources/views/account/tariffs.html',
            controller:  'TariffListCtrl',
            reloadOnSearch: false,
            resolve: {
                factory: checkPerson
            }
        })
        .when('/account/tariff/edit/:id?', {
            title: 'Тариф',
            templateUrl: '/resources/views/account/editTariff.html',
            controller:  'TariffCtrl',
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