<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" isELIgnored="false"%>
<%@ include file="../include.jsp"%>

<t:page htmlTitle="Платежи" ctrl="PaymentListCtrl">
	<jsp:attribute name="initScript">
    </jsp:attribute>
    <jsp:body>
        <section class="md-well well--inset-2 payments-page" ng-controller="PaymentListCtrl" ng-cloak>
            <div class="container">
                <div class="row">
                    <div class="col-md-12">
                        <h5 class="text-center">платежи</h5>

                        <jsp:include page="../include/listSearch.jsp">
                            <jsp:param name="newLabel" value="Новый платеж"/>
                        </jsp:include>

                        <div class="table-responsive payments">
                            <table class="table table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Атлет</th>
                                        <th>Тариф</th>
                                        <th>Купон</th>
                                        <th>Сумма</th>
                                        <th>Оператор</th>
                                        <th>Дата</th>
                                        <th><i class="glyphicon glyphicon-cog"></i></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr ng-repeat="payment in payments" ng-click="onClickPayment(payment.id)">
                                        <td>{{payment.id}}</td>
                                        <td>{{payment.athlete}}</td>
                                        <td>{{payment.tariff}}</td>
                                        <td>{{payment.coupon}}</td>
                                        <td>{{payment.amount}}</td>
                                        <td>{{payment.operator}}</td>
                                        <td>{{payment.date}}</td>
                                        <td>
                                            <i class="glyphicon glyphicon-remove" ng-click="onClickRemove(payment.id, $event)"></i>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <jsp:include page="../include/paging.jsp"></jsp:include>
                    </div>
                </div>
            </div>
        </section>
    </jsp:body>
</t:page>
