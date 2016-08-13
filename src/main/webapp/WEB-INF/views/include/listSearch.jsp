<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" isELIgnored="false"%>
<%@ include file="../include.jsp"%>

<c:set var="newLabel" value="${param.newLabel}"/>

<form class='rd-mailform search' name="searchForm" ng-controller="ListSearchCtrl">
    <fieldset>
        <div class="row flow-offset-4">
            <div class="col-md-4">
                <label>
                    <span class="title">Поиск</span>
                    <input placeholder="Введите запрос" name="query" ng-model="query"/>
                </label>
            </div>
            <div class="col-md-4">
                <button class="btn btn-md btn-default" ng-click="onClickNew()">${newLabel}</button>
            </div>
        </div>
    </fieldset>
</form>