<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" isELIgnored="false"%>
<%@ include file="../include.jsp"%>

<t:page htmlTitle="Платеж" ctrl="PaymentCtrl">
	<jsp:attribute name="initScript">
    </jsp:attribute>
    <jsp:body>
        <section class="md-well well--inset-2 payment-page" ng-controller="PaymentCtrl" ng-cloak>
            <div class="container">
                <div class="row">
                    <div class="col-md-3"></div>
                    <div class="col-md-6">
                        <h5 class="text-center">платеж</h5>

                        <form class='rd-mailform' name="paymentForm">
                            <fieldset>
                                <div class="row flow-offset-4">
                                    <div class="col-md-12">
                                        <label>
                                            <span class="title">Атлет</span>
                                            <div class="field-error" ng-show="isInvalid(paymentForm.athlete)">
                                                <span class="form-error" ng-show="paymentForm.athlete.$error.required">Поле не может быть пустым</span>
                                                <span class="form-error" ng-show="paymentForm.athlete.$error.server">{{paymentForm.athlete.serverErrorMessage}}</span>
                                            </div>
                                            <ui-select ng-model="athlete.selected" name="athlete" ng-required="true" theme="select2" style="min-width: 300px;" title="Выберите атлета">
                                                <ui-select-match allow-clear="true" placeholder="Выберите атлета">{{$select.selected.text}}</ui-select-match>
                                                <ui-select-choices repeat="athlete in athletes"
                                                                   refresh="search('athlete', $select.search)" refresh-delay="1000">
                                                    <div ng-bind-html="athlete.text | highlight: $select.search"></div>
                                                </ui-select-choices>
                                            </ui-select>
                                        </label>
                                    </div>
                                </div>

                                <div class="row flow-offset-4">
                                    <div class="col-md-12">
                                        <label>
                                            <span class="title">Зал</span>
                                            <div class="field-error" ng-show="isInvalid(paymentForm.gym)">
                                                <span class="form-error" ng-show="paymentForm.gym.$error.required">Поле не может быть пустым</span>
                                                <span class="form-error" ng-show="paymentForm.gym.$error.server">{{paymentForm.gym.serverErrorMessage}}</span>
                                            </div>
                                            <ui-select ng-model="gym.selected" name="gym" ng-required="true" theme="select2" style="min-width: 300px;" title="Выберите зал">
                                                <ui-select-match allow-clear="true" placeholder="Выберите зал">{{$select.selected.text}}</ui-select-match>
                                                <ui-select-choices repeat="gym in gyms"
                                                                   refresh="search('gym', $select.search)" refresh-delay="1000">
                                                    <div ng-bind-html="gym.text | highlight: $select.search"></div>
                                                </ui-select-choices>
                                            </ui-select>
                                        </label>
                                    </div>
                                </div>

                                <div class="row flow-offset-4">
                                    <div class="col-md-12">
                                        <label>
                                            <span class="title">Сумма</span>
                                            <div class="field-error" ng-show="isInvalid(paymentForm.amount)">
                                                <span class="form-error" ng-show="paymentForm.amount.$error.required">Поле не может быть пустым</span>
                                                <span class="form-error" ng-show="paymentForm.amount.$error.min">Должно быть положительным</span>
                                                <span class="form-error" ng-show="paymentForm.amount.$error.server">{{paymentForm.amount.serverErrorMessage}}</span>
                                            </div>
                                            <input ng-class="{'error': isInvalid(paymentForm.amount)}" type="number" placeholder="Введите сумму" min="1"
                                                   name="amount" ng-model="amount" ng-required="true"/>
                                        </label>
                                    </div>
                                </div>

                                <div class="mfControls btn-group text-center text-md-left">
                                    <button class="btn btn-md btn-default" ng-click="onClickButton()">{{buttonLabel}}</button>
                                    <a href="/account/payment/" class="btn btn-md">Список</a>
                                </div>
                            </fieldset>
                        </form>
                    </div>
                    <div class="col-md-3"></div>
                </div>
            </div>
        </section>
    </jsp:body>
</t:page>
