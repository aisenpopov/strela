<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" isELIgnored="false"%>
<%@ include file="include.jsp"%>

<!DOCTYPE html>
<html class="boxed smoothscroll wow-animation" ng-app="app">
<head>
    <title ng-bind="htmlTitle + ' | Strela'">Strela</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="author" content="Strela"/>
    <meta name="description" content="${htmlDescription}"/>
    <meta name="keywords" content="${htmlKeywords}"/>

    <meta name="format-detection" content="telephone=no"/>
    <meta name="viewport" content="width=device-width, height=device-height, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"/>

    <link rel="icon" href="/resources/favicon.ico" type="/favicon.ico"/>
    <!-- Stylesheets -->
    <link href='http://fonts.googleapis.com/css?family=Ubuntu:300%7CRoboto+Condensed:700' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="/resources/css/libs/angular-csp.css">
    <link rel="stylesheet" href="/resources/css/libs/angular-select.css">
    <link rel="stylesheet" href="/resources/css/libs/select2.css">
    <link rel="stylesheet" href="/resources/css/libs/bootstrap-table.css">
    <link rel="stylesheet" href="/resources/css/libs/bootstrap-glyphicons.css">
    <link rel="stylesheet" href="/resources/css/libs/bootstrap-modals.css">
    <link rel="stylesheet" href="/resources/css/libs/jquery.loading-indicator.css">
    <link rel="stylesheet" href="/resources/css/style.css">
    <link rel="stylesheet" href="/resources/css/fix.css">

    <!--[if lt IE 10]>
    <script src="/resources/js/html5shiv.min.js"></script>
    <![endif]-->

    <!-- Yandex.Metrika counter -->
    <script type="text/javascript">
        (function (d, w, c) {
            (w[c] = w[c] || []).push(function() {
                try {
                    w.yaCounter41498034 = new Ya.Metrika({
                        id:41498034,
                        clickmap:true,
                        trackLinks:true,
                        accurateTrackBounce:true,
                        webvisor:true,
                        trackHash:true
                    });
                } catch(e) { }
            });

            var n = d.getElementsByTagName("script")[0],
                    s = d.createElement("script"),
                    f = function () { n.parentNode.insertBefore(s, n); };
            s.type = "text/javascript";
            s.async = true;
            s.src = "https://mc.yandex.ru/metrika/watch.js";

            if (w.opera == "[object Opera]") {
                d.addEventListener("DOMContentLoaded", f, false);
            } else { f(); }
        })(document, window, "yandex_metrika_callbacks");
    </script>
    <noscript><div><img src="https://mc.yandex.ru/watch/41498034" style="position:absolute; left:-9999px;" alt="" /></div></noscript>
    <!-- /Yandex.Metrika counter -->
</head>
<body ng-controller="CommonCtrl" ng-cloak>
<div class="page text-center">

    <!--For older internet explorer-->
    <div class="old-ie"
         style='background: #212121; padding: 10px 0; box-shadow: 3px 3px 5px 0 rgba(0,0,0,.3); clear: both; text-align:center; position: relative; z-index:1;'>
        <a href="http://windows.microsoft.com/en-US/internet-explorer/..">
            <img src="/resources/img/ie8-panel/warning_bar_0000_us.jpg" height="42" width="820"
                 alt="You are using an outdated browser. For a faster, safer browsing experience, upgrade for free today."/>
        </a>
    </div>
    <!--END block for older internet explorer-->

    <div ng-include="'/resources/views/include/header.html'"></div>

    <main class="page-content" ng-view></main>

    <div ng-include="'/resources/views/include/footer.html'"></div>
</div>
</body>

<!-- Core Scripts -->
<script src="/resources/js/core.min.js"></script>

<!-- Additional Functionality Scripts -->
<script src="/resources/js/script.js"></script>

<!-- Libs -->
<script src="https://api-maps.yandex.ru/2.1/?lang=ru_RU"></script>
<script src="/resources/js/libs/jquery-ui-1.10.3.min.js"></script>
<script src="/resources/js/libs/owl.carousel.min.js"></script>
<script src="/resources/js/libs/jquery.loading-indicator.min.js"></script>
<script src="/resources/js/libs/angularjs/angular.min.js"></script>
<script src="/resources/js/libs/angularjs/angular-route.min.js"></script>
<script src="/resources/js/libs/angularjs/angular-animate.min.js"></script>
<script src="/resources/js/libs/angularjs/angular-touch.min.js"></script>
<script src="/resources/js/libs/angularjs/angular-sanitize.min.js"></script>
<script src="/resources/js/libs/angularjs/angular-resource.min.js"></script>
<script src="/resources/js/libs/angularjs/ui-bootstrap-tpls-2.0.0.js"></script>
<script src="/resources/js/libs/angularjs/select.min.js"></script>

<!-- App -->
<script src="/resources/js/web/App.js"></script>
<script src="/resources/js/web/Route.js"></script>
<script src="/resources/js/web/Directives.js"></script>
<script src="/resources/js/web/Services.js"></script>

<script src="/resources/js/web/LoginCtrl.js"></script>
<script src="/resources/js/web/GymCtrls.js"></script>
<script src="/resources/js/web/NewsCtrls.js"></script>

<script src="/resources/js/web/AccountCtrl.js"></script>
<script src="/resources/js/web/RecoveryCtrl.js"></script>
<script src="/resources/js/web/BalanceCtrl.js"></script>
<script src="/resources/js/web/PaymentCtrl.js"></script>
<script src="/resources/js/web/PaymentListCtrl.js"></script>
<script src="/resources/js/web/PaymentStatusCtrls.js"></script>
<script src="/resources/js/web/TransactionCtrls.js"></script>
<script src="/resources/js/web/CouponCtrls.js"></script>
<script src="/resources/js/web/TariffCtrls.js"></script>
<script src="/resources/js/web/AthleteCtrls.js"></script>
<script src="/resources/js/web/TeamCtrls.js"></script>

</html>
