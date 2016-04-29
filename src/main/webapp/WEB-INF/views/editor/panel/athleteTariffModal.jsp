<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" isELIgnored="false"%>
<%@ include file="../../include.jsp" %>

<div class="modal fade edit-athlete-tariff-panel sys-edit-athlete-tariff" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">

            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Тариф атлета</h4>
            </div>

            <div class="modal-body">
                <t:ajaxUpdate id="athleteTariffForm">
                    <form:form method="post" commandName="athleteTariff">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="form-group">
                                    <label>Тариф <span class="required">*</span></label>
                                    <form:input path="tariff" type="text" class="form-control"/>
                                    <form:errors class="help-block error" path="tariff"/>
                                </div>
                                <div class="form-group">
                                    <label>Купон</label>
                                    <form:input path="coupon" type="text" class="form-control"/>
                                    <form:errors class="help-block error" path="coupon"/>
                                </div>

                                <form:input path="id" type="hidden"/>
                                <form:input path="athlete" type="hidden"/>
                            </div>
                        </div>
                    </form:form>
                </t:ajaxUpdate>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    Отмена
                </button>
                <button type="button" class="btn btn-primary sys-save">
                    Сохранить
                </button>
            </div>

        </div>
    </div>
</div>