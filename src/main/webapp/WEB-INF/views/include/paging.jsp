<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" isELIgnored="false"%>

<nav class="text-md-left" ng-controller="PagingCtrl" ng-show="pages.length > 1">
    <ul class="pagination">
        <li ng-if="page.number != 0">
            <a ng-click="list(page.number)" class="next material-design-keyboard54"></a>
        </li>
        <li ng-repeat="item in pages" ng-class="{'active' : item == page.number + 1}">
            <a ng-click="item != page.number + 1 && list(item)">{{item}}</a>
        </li>
        <li ng-if="page.number != page.totalPages - 1">
            <a ng-click="list(page.number + 2)" class="next material-design-keyboard53"></a>
        </li>
    </ul>
</nav>