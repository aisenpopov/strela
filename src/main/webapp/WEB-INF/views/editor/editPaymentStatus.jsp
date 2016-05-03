<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include.jsp" %>

<te:page title="${paymentStatus.id > 0 ? 'Дата истечения' : 'Новая дата истечения'}">
	<jsp:attribute name="initScript">
		E.initPaymentStatusPage();
	</jsp:attribute>
    <jsp:body>

        <div class="payment-status-editor">
            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <h1 class="page-title txt-color-blueDark">
                        <i class="fa fa-pencil-square-o fa-fw fa"></i>${paymentStatus.id > 0 ? 'Дата истечения' : 'Новая дата истечения'}
                        <a href="/editor/${activeMenu.href}/" class="btn btn-default right-header-button" role="button">Список</a>
                        <a href="/editor/${activeMenu.href}/edit/" class="btn btn-info right-header-button margin-right-5" role="button">Добавить</a>
                    </h1>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12 col-md-12 col-lg-10">
                    <div class="jarviswidget jarviswidget-color-blueDark">
                        <header role="heading">
                            <h2>${paymentStatus.id > 0 ? 'Дата истечения' : 'Новая дата истечения'}</h2>
                        </header>
                        <div role="content">
                            <div class="widget-body no-padding">
                                <form:form class="smart-form" commandName="paymentStatus" role="form" method="post">
                                    <form:hidden path="id"/>

                                    <fieldset>
                                        <section>
                                            <label class="label">Атлет <span>*</span></label>
                                            <label class="input">
                                                <form:input class="form-control" type="text" path="athlete"/>
                                                <form:errors class="help-block error" path="athlete"/>
                                            </label>
                                        </section>
                                        <section>
                                            <label class="label">Зал <span>*</span></label>
                                            <label class="input">
                                            	<form:input class="form-control" type="text" path="gym"/>
												<form:errors class="help-block error" path="gym"/>
                                            </label>
                                        </section>
                                        <section>
                                            <label class="label">Дата истечения <span>*</span></label>
                                            <label class="input">
                                                <form:input class="form-control datepicker" type="text" path="payedTill"/>
                                                <form:errors class="help-block error" path="payedTill"/>
                                            </label>
                                        </section>
                                    </fieldset>
                                    <footer>
                                        <button class="btn btn-primary" type="submit" name="save">
                                            <i class="fa fa-save"></i> Сохранить
                                        </button>
                                    </footer>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</te:page>