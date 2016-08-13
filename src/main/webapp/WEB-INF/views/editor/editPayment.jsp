<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include.jsp" %>

<te:page title="${payment.id > 0 ? 'Платеж' : 'Новый платеж'}">
	<jsp:attribute name="initScript">
		E.initPaymentPage();
	</jsp:attribute>
    <jsp:body>

        <div class="payment-editor">
            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <h1 class="page-title txt-color-blueDark">
                        <i class="fa fa-pencil-square-o fa-fw fa"></i>${payment.id > 0 ? 'Платеж' : 'Новый платеж'}
                        <a href="/editor/${activeMenu.href}/" class="btn btn-default right-header-button" role="button">Список</a>
                        <a href="/editor/${activeMenu.href}/edit/" class="btn btn-info right-header-button margin-right-5" role="button">Добавить</a>
                    </h1>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <div class="jarviswidget jarviswidget-color-blueDark">
                        <header role="heading">
                            <h2>${payment.id > 0 ? 'Платеж' : 'Новый платеж'}</h2>
                        </header>
                        <div role="content">
                            <div class="widget-body no-padding">
                                <form:form class="smart-form" commandName="payment" role="form" method="post">
                                    <form:hidden path="id"/>
                                    <form:hidden path="date"/>
                                    <form:hidden path="operator"/>

                                    <fieldset>
                                        <section>
                                           <label class="label">Атлет <span>*</span></label>
                                           <label class="input">
                                               <form:input class="form-control" type="text" path="athleteTariff.athlete"/>
                                               <%--<span class="help-block error hidden" name="athleteTariff.athlete">
                                                   Выберите атлета
                                               </span>--%>
                                               <form:errors class="help-block error" path="athleteTariff.athlete"/>
                                           </label>
                                        </section>
                                        <section>
                                            <label class="label">Зал <span>*</span></label>
                                            <label class="input">
                                                <form:input class="form-control" type="text" path="athleteTariff.tariff.gym"/>
                                                <form:errors class="help-block error" path="athleteTariff.tariff.gym"/>
                                            </label>
                                        </section>
                                        <%--<section>
                                            <label class="label">Тариф атлета <span>*</span></label>
                                            <label class="input">
                                                <form:input class="form-control" type="text" path="athleteTariff"/>
                                                <form:errors class="help-block error" path="athleteTariff"/>
                                            </label>
                                        </section>
                                        <section>
                                            <a href="#" class="sys-add-athlete-tariff"><i class="glyphicon glyphicon-plus"></i>Добавить</a>
                                        </section>--%>
										<section>
                                            <label class="label">Сумма <span>*</span></label>
                                            <label class="input">
                                            	<form:input class="form-control" type="text" path="amount"/>
												<form:errors class="help-block error" path="amount"/>
                                            </label>
                                        </section>
									</fieldset>
                                    <footer>
                                        <button class="btn btn-primary" type="submit" name="save">
                                            <i class="fa fa-save"></i> ${payment.id > 0 ? 'Перепровести' : 'Провести'}
                                        </button>
                                    </footer>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <%--<jsp:include page="panel/athleteTariffModal.jsp"/>--%>
        </div>
    </jsp:body>
</te:page>