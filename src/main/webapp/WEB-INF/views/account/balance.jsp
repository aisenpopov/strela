<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" isELIgnored="false"%>
<%@ include file="../include.jsp"%>

<t:page htmlTitle="Мой баланс" ctrl="BalanceCtrl">
	<jsp:attribute name="initScript">
    </jsp:attribute>
    <jsp:body>
        <section class="md-well well--inset-2" ng-controller="BalanceCtrl" ng-cloak>
            <div class="container">
                <div class="row">
                    <div class="col-md-3"></div>
                    <div class="col-md-6">
                        <div ng-show="hasBalance">
                            <h5 class="text-center">ваш баланс: {{balance}} руб.</h5>
                            <form class='rd-mailform' name="balanceForm">
                                <fieldset>
                                    <div class="row flow-offset-4">
                                        <div class="col-md-12">
                                            <label>
                                                <span class="title">Списать с баланса</span>
                                                <div class="field-error" ng-show="isInvalid(balanceForm.amount)">
                                                    <span class="form-error" ng-show="balanceForm.amount.$error.required">Поле не может быть пустым</span>
                                                    <span class="form-error" ng-show="balanceForm.amount.$error.max">Не может быть больше баланса</span>
                                                    <span class="form-error" ng-show="balanceForm.amount.$error.min">Должно быть положительным</span>
                                                    <span class="form-error" ng-show="balanceForm.amount.$error.server">{{balanceForm.amount.serverErrorMessage}}</span>
                                                </div>
                                                <input ng-class="{'error': isInvalid(balanceForm.amount)}" type="number" placeholder="Введите сумму" max="{{balance}}" min="1"
                                                       name="amount" ng-model="amount" ng-required="true"/>
                                            </label>
                                        </div>
                                    </div>

                                    <div class="mfControls btn-group text-center text-md-left">
                                        <button class="btn btn-md btn-default" ng-click="onClickButton()">Списать</button>
                                    </div>
                                </fieldset>
                            </form>
                        </div>
                        <div ng-show="!hasBalance">
                            <h5 class="text-center">У вас пока нет принятых оплат</h5>
                        </div>
                    </div>
                    <div class="col-md-3"></div>
                </div>
            </div>
        </section>
    </jsp:body>
</t:page>
